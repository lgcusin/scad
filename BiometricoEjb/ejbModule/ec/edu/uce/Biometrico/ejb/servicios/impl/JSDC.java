package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGPPPortAddr;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.JSDCLocal;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.Horario;
import ec.uce.edu.biometrico.jpa.HuellaDactilar;

/**
 * Session Bean implementation class JSDC
 */
@Stateless
@LocalBean
public class JSDC implements JSDCLocal {

	private long deviceName;
	private long devicePort;
	private JSGFPLib fplib = null;
	private long ret;
	private boolean bLEDOn;
	private byte[] regMin1 = new byte[400];
	private byte[] regMin2 = new byte[400];
	private byte[] regMin3 = new byte[400];
	private SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
	private boolean r1Captured = false;
	private boolean r2Captured = false;
	private static int MINIMUM_QUALITY = 60; // User defined
	private static int MINIMUM_NUM_MINUTIAE = 20; // User defined
	private static int MAXIMUM_NFIQ = 2;
	private int[] quality = new int[1];

	@PersistenceContext
	EntityManager em;

	public JSDC() {
		bLEDOn = false;
	}

	@Override
	public boolean inicializar() {
		// AUTO
		// FDU08 Hamster Pro 20A
		// FDU07A Hamster Pro 10AP
		// FDU07 Hamster Pro 10
		// FDU06 Hamster Pro
		// FDU05 Hamster Pro 20
		// FDU04 Hamster IV
		// FDU03 Hamster Plus
		// FDU02 Hamster III
		this.deviceName = SGFDxDeviceName.SG_DEV_FDU03;
		if (fplib != null) {
			fplib.CloseDevice();
			fplib.Close();
			fplib = null;
		}
		fplib = new JSGFPLib();
		ret = fplib.Init(this.deviceName);
		if ((fplib != null) && (ret == SGFDxErrorCode.SGFDX_ERROR_NONE)) {
			System.out.println("JSGFPLib Initialization Success");
			this.devicePort = SGPPPortAddr.AUTO_DETECT;
			ret = fplib.OpenDevice(this.devicePort);
			if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {

				System.out.println("OpenDevice() Success [" + ret + "]");
				ret = fplib.GetDeviceInfo(deviceInfo);
				if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
					System.out.println(new String(deviceInfo.deviceSN()));
					System.out.println(new String(Integer.toString(deviceInfo.brightness)));
					System.out.println(new String(Integer.toString((int) deviceInfo.contrast)));
					System.out.println(new String(Integer.toString(deviceInfo.deviceID)));
					System.out.println(new String(Integer.toHexString(deviceInfo.FWVersion)));
					System.out.println(new String(Integer.toString(deviceInfo.imageDPI)));
					System.out.println(new String(Integer.toString(deviceInfo.imageHeight)));
					System.out.println(new String(Integer.toString(deviceInfo.imageWidth)));
					return true;
				} else {
					System.out.println("GetDeviceInfo() Error [" + ret + "]");
					return false;
				}
			} else {
				System.out.println("OpenDevice() Error [" + ret + "]");
				return false;
			}

		} else {
			System.out.println("JSGFPLib Initialization Failed");
			return false;
		}
	}

	@Override
	public boolean verificar() {
		long iError;
		long secuLevel = (long) 4;// NORMAL
		boolean[] matched = new boolean[1];
		matched[0] = false;

		iError = fplib.MatchTemplate(regMin1, regMin2, secuLevel, matched);
		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			if (matched[0]) {
				System.out.println("Verification Success (1st Attempt)");
				System.out.println("Huella Captura 1" + Arrays.toString(regMin1));
				System.out.println("Huella Captura 2" + Arrays.toString(regMin2));
				return matched[0];
			} else {
				System.out.println("Comparacion Error : " + iError);
				return matched[0];
			}
		} else {
			System.out.println("MatchTemplate() Error : " + iError);
			return matched[0];
		}
	}

	@Override
	public BufferedImage capturar() {
		boolean num = false;
		long nfiqvalue;
		int[] numOfMinutiae = new int[1];
		BufferedImage imgn = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		byte[] imageBuffer1 = ((java.awt.image.DataBufferByte) imgn.getRaster().getDataBuffer()).getData();
		long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;
		if (fplib != null) {
			iError = fplib.GetImageEx(imageBuffer1, 5 * 1000, 0, 50);
			fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
			SGFingerInfo fingerInfo = new SGFingerInfo();
			fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
			fingerInfo.ImageQuality = quality[0];
			fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
			fingerInfo.ViewNumber = 1;
			if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
				if (quality[0] < MINIMUM_QUALITY) {
					System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0]
							+ "]. Please try again");
					return null;
				} else {
					System.out.println("GetImageEx() Success [" + ret + "]");
					if (!r1Captured) {
						iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, regMin1);
						num = true;
					}
					iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, regMin2);

					if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
						long ret2 = fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1,
								quality);
						nfiqvalue = fplib.ComputeNFIQ(imageBuffer1, deviceInfo.imageWidth, deviceInfo.imageHeight);
						if (num) {
							ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMin1,
									numOfMinutiae);
						}
						ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMin2,
								numOfMinutiae);
						if ((quality[0] >= MINIMUM_QUALITY) && (nfiqvalue <= MAXIMUM_NFIQ)
								&& (numOfMinutiae[0] >= MINIMUM_NUM_MINUTIAE)) {
							System.out.println("Reg. Capture PASS QC. Qual[" + quality[0] + "] NFIQ[" + nfiqvalue
									+ "] Minutiae[" + numOfMinutiae[0] + "]");
							if (num) {
								r1Captured = true;
							}
							r2Captured = true;
							return imgn;
						} else {
							System.out.println("Reg. Capture FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue
									+ "] Minutiae[" + numOfMinutiae[0] + "]");
							return null;
						}
					} else {
						System.out.println("CreateTemplate() Error : " + iError);
						return null;
					}
				}
			} else {
				System.out.println("GetImageEx() Error : " + iError);
				return null;
			}
		} else {
			System.out.println("JSGFPLib is not Initialized");
			return null;
		}
	}

	@Override
	public int calidad() {
		return quality[0];
	}

	@Override
	public void onLED() {
		if (fplib != null && bLEDOn == false) {
			bLEDOn = !bLEDOn;
			ret = fplib.SetLedOn(bLEDOn);
			if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
				System.out.println("SetLedOn(" + bLEDOn + ") Success [" + ret + "]");
			} else {
				System.out.println("SetLedOn(" + bLEDOn + ") Error [" + ret + "]");
			}
		} else {
			System.out.println("JSGFPLib is not Initialized");
		}

	}

	@Override
	public void configurar() {
		long iError;

		iError = fplib.Configure(0);
		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			System.out.println("Configure() Success");
			// this.jButtonGetDeviceInfo.doClick();
		} else if (iError == SGFDxErrorCode.SGFDX_ERROR_NOT_USED)
			System.out.println("Configure() not supported on this platform");
		else
			System.out.println("Configure() Error : " + iError);
	}

	@Override
	public void cerrar() {
		long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;
		if (fplib != null) {
			iError = fplib.CloseDevice();
			fplib.Close();
			fplib = null;
			bLEDOn = false;
			r1Captured = false;
			r2Captured = false;

		}
		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			System.out.println("CloseDevice() Success [" + iError + "]");
		} else {
			System.out.println("CloseDevice() Error : " + iError);
		}
	}

	@Override
	public FichaDocente comparar() throws SQLException, IOException {
		long nfiqvalue;
		int[] numOfMinutiae = new int[1];
		BufferedImage imgn = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		byte[] imageBuffer1 = ((java.awt.image.DataBufferByte) imgn.getRaster().getDataBuffer()).getData();
		long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;
		if (fplib != null) {
			iError = fplib.GetImageEx(imageBuffer1, 5 * 1000, 0, 50);
			fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
			SGFingerInfo fingerInfo = new SGFingerInfo();
			fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
			fingerInfo.ImageQuality = quality[0];
			fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
			fingerInfo.ViewNumber = 1;
			if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
				if (quality[0] < MINIMUM_QUALITY) {
					System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0]
							+ "]. Please try again");
				} else {
					System.out.println("GetImageEx() Success [" + ret + "]");
					iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, regMin1);
					if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
						long ret2 = fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1,
								quality);
						nfiqvalue = fplib.ComputeNFIQ(imageBuffer1, deviceInfo.imageWidth, deviceInfo.imageHeight);

						ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMin1,
								numOfMinutiae);
						if ((quality[0] >= MINIMUM_QUALITY) && (nfiqvalue <= MAXIMUM_NFIQ)
								&& (numOfMinutiae[0] >= MINIMUM_NUM_MINUTIAE)) {
							System.out.println("Reg. Capture PASS QC. Qual[" + quality[0] + "] NFIQ[" + nfiqvalue
									+ "] Minutiae[" + numOfMinutiae[0] + "]");
							// Obtenemos una lista de huellas primeras de base
							List<HuellaDactilar> lstHd = em
									.createNamedQuery("HuellaDactilar.findAll", HuellaDactilar.class).getResultList();
							for (HuellaDactilar hd : lstHd) {
								Blob blob = hd.getHldPrimerHuella();
								InputStream in = blob.getBinaryStream();
								BufferedImage image = ImageIO.read(in);
								byte[] imageBuffer2 = ((java.awt.image.DataBufferByte) image.getRaster()
										.getDataBuffer()).getData();
								iError = fplib.CreateTemplate(fingerInfo, imageBuffer2, regMin2);
								if (verificar()) {
									FichaDocente fd = em.createNamedQuery("Docente.findByHdId", FichaDocente.class)
											.setParameter("hdId", hd.getHldcId()).getSingleResult();
									return fd;
								} else {
									blob = hd.getHldSegundaHuella();
									in = blob.getBinaryStream();
									image = ImageIO.read(in);
									imageBuffer2 = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer())
											.getData();
									iError = fplib.CreateTemplate(fingerInfo, imageBuffer2, regMin2);
									if (verificar()) {
										FichaDocente fd = em.createNamedQuery("Docente.findByHdId", FichaDocente.class)
												.setParameter("hdId", hd.getHldcId()).getSingleResult();
										return fd;
									}
								}
							}

						} else {
							System.out.println("Reg. Capture FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue
									+ "] Minutiae[" + numOfMinutiae[0] + "]");
							return null;
						}
					} else {
						System.out.println("CreateTemplate() Error : " + iError);
						return null;
					}
				}
			} else {
				System.out.println("GetImageEx() Error : " + iError);
				return null;
			}
		} else {
			System.out.println("JSGFPLib is not Initialized");
			return null;
		}
		return null;
	}

}
