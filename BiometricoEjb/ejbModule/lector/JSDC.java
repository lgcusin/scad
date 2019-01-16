package lector;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.midi.Soundbank;
import javax.sql.rowset.serial.SerialException;
import javax.swing.ImageIcon;

import SecuGen.FDxSDKPro.jni.*;
import model.FichaDocente;

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
	private byte[] vrfMin = new byte[400];
	private SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
	private BufferedImage imgRegistration1;
	private BufferedImage imgRegistration2;
	private BufferedImage imgVerification;
	private boolean r1Captured = false;
	private boolean r2Captured = false;
	private boolean v1Captured = false;
	private static int MINIMUM_QUALITY = 60; // User defined
	private static int MINIMUM_NUM_MINUTIAE = 20; // User defined
	private static int MAXIMUM_NFIQ = 2;

	public JSDC() {
		bLEDOn = false;
	}

	@Override
	public void inicializar() {
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

					imgRegistration1 = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight,
							BufferedImage.TYPE_BYTE_GRAY);
					imgRegistration2 = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight,
							BufferedImage.TYPE_BYTE_GRAY);
					imgVerification = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight,
							BufferedImage.TYPE_BYTE_GRAY);

				} else
					System.out.println("GetDeviceInfo() Error [" + ret + "]");
			} else
				System.out.println("OpenDevice() Error [" + ret + "]");

		} else
			System.out.println("JSGFPLib Initialization Failed");

	}

	/*
	 * @Override public BufferedImage capturar(int indice) { int[] quality = new
	 * int[1]; long nfiqvalue; BufferedImage img1gray = new
	 * BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight,
	 * BufferedImage.TYPE_BYTE_GRAY); byte[] imageBuffer1 =
	 * ((java.awt.image.DataBufferByte)
	 * img1gray.getRaster().getDataBuffer()).getData(); switch (indice) { case
	 * 1: System.out.println("huella 1:"); regMin1=imageBuffer1; break; case 2:
	 * System.out.println("huella 2:"); regMin2=imageBuffer1; break; case 3:
	 * System.out.println("huella 3:"); vrfMin=imageBuffer1; break;
	 * 
	 * }
	 * 
	 * if (fplib != null) { // ret = fplib.GetImageEx(imageBuffer1,
	 * jSliderSeconds.getValue() * // 1000, 0, jSliderQuality.getValue()); ret =
	 * fplib.GetImageEx(imageBuffer1, 5 * 1000, 0, 50); if (ret ==
	 * SGFDxErrorCode.SGFDX_ERROR_NONE) { ImageIcon huella = new
	 * ImageIcon(img1gray); System.out.println(img1gray); long ret2 =
	 * fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight,
	 * imageBuffer1, quality); nfiqvalue = fplib.ComputeNFIQ(imageBuffer1,
	 * deviceInfo.imageWidth, deviceInfo.imageHeight);
	 * System.out.println("getImage() Success [" + ret + "] --- Image Quality ["
	 * + quality[0] + "] --- NFIQ Value [" + nfiqvalue + "]"); } else {
	 * System.out.println("GetImageEx() Error [" + ret + "]"); } } else {
	 * System.out.println("JSGFPLib is not Initialized"); } return img1gray; }
	 */

	@Override
	public boolean verificar() {
		long iError;
		long secuLevel = (long) 4;// NORMAL
		boolean[] matched = new boolean[1];
		matched[0] = false;

		iError = fplib.MatchTemplate(regMin1, vrfMin, secuLevel, matched);
		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			if (matched[0]) {
				System.out.println("Verification Success (1st template)");
				System.out.println("Huella Captura 1" + Arrays.toString(regMin1));
				System.out.println("Huella Verificacion" + Arrays.toString(vrfMin));
				return true;
			} else {
				iError = fplib.MatchTemplate(regMin2, vrfMin, secuLevel, matched);
				if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE)
					if (matched[0]) {
						System.out.println("Verification Success (2nd template)");
						System.out.println("Huella Captura 1" + Arrays.toString(regMin1));
						System.out.println("Huella Verificacion" + Arrays.toString(vrfMin));
						return true;
					} else {
						System.out.println("Verification Fail");
						return false;
					}
				else
					System.out.println("Verification Attempt 2 Fail - MatchTemplate() Error : " + iError);
				return false;
			}
		} else
			System.out.println("Verification Attempt 1 Fail - MatchTemplate() Error : " + iError);
		return false;
	}

	@Override
	public BufferedImage capturar1() throws SerialException, IOException, SQLException {
		int[] quality = new int[1];
		int[] numOfMinutiae = new int[1];
		byte[] imageBuffer1 = ((java.awt.image.DataBufferByte) imgRegistration1.getRaster().getDataBuffer()).getData();
		long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;

		iError = fplib.GetImageEx(imageBuffer1, 5 * 1000, 0, 50);
		fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
		System.out.println("Calidad de huella 1:" + quality[0]);
		SGFingerInfo fingerInfo = new SGFingerInfo();
		fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
		fingerInfo.ImageQuality = quality[0];
		fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
		fingerInfo.ViewNumber = 1;

		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			// this.jButtonVerify.setEnabled(false);
			// this.jButtonRegister.setEnabled(false);
			// this.jLabelRegisterImage1.setIcon(new
			// ImageIcon(imgRegistration1.getScaledInstance(130,150,Image.SCALE_DEFAULT)));
			if (quality[0] < MINIMUM_QUALITY)
				System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0]
						+ "]. Please try again");
			else {
				System.out.println("GetImageEx() Success [" + ret + "]");

				iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, regMin1);
				if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
					long nfiqvalue;
					long ret2 = fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1,
							quality);
					nfiqvalue = fplib.ComputeNFIQ(imageBuffer1, deviceInfo.imageWidth, deviceInfo.imageHeight);
					ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMin1, numOfMinutiae);

					if ((quality[0] >= MINIMUM_QUALITY) && (nfiqvalue <= MAXIMUM_NFIQ)
							&& (numOfMinutiae[0] >= MINIMUM_NUM_MINUTIAE)) {
						System.out.println("Reg. Capture 1 PASS QC. Qual[" + quality[0] + "] NFIQ[" + nfiqvalue
								+ "] Minutiae[" + numOfMinutiae[0] + "]");
						r1Captured = true;
						guardarImagen(imgRegistration1);
						// this.enableRegisterAndVerifyControls();
					} else {
						System.out.println("Reg. Capture 1 FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue
								+ "] Minutiae[" + numOfMinutiae[0] + "]");
						// this.jButtonVerify.setEnabled(false);
						// this.jButtonRegister.setEnabled(false);
					}
				} else
					System.out.println("CreateTemplate() Error : " + iError);
			}
		} else
			System.out.println("GetImageEx() Error : " + iError);
		return imgRegistration1;
	}



	@Override
	public BufferedImage capturar2() {
		int[] quality = new int[1];
		int[] numOfMinutiae = new int[1];
		byte[] imageBuffer1 = ((java.awt.image.DataBufferByte) imgRegistration2.getRaster().getDataBuffer()).getData();
		long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;

		iError = fplib.GetImageEx(imageBuffer1, 5 * 1000, 0, 50);
		fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
		System.out.println("Calidad huella 2:" + quality[0]);
		SGFingerInfo fingerInfo = new SGFingerInfo();
		fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
		fingerInfo.ImageQuality = quality[0];
		fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
		fingerInfo.ViewNumber = 1;

		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			// this.jLabelRegisterImage2.setIcon(new
			// ImageIcon(imgRegistration2.getScaledInstance(130,150,Image.SCALE_DEFAULT)));
			if (quality[0] < MINIMUM_QUALITY)
				System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0]
						+ "]. Please try again");
			else {
				System.out.println("GetImageEx() Success [" + ret + "]");

				iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, regMin2);
				if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {

					long nfiqvalue;
					long ret2 = fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1,
							quality);
					nfiqvalue = fplib.ComputeNFIQ(imageBuffer1, deviceInfo.imageWidth, deviceInfo.imageHeight);
					ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMin2, numOfMinutiae);
					if ((quality[0] >= MINIMUM_QUALITY) && (nfiqvalue <= MAXIMUM_NFIQ)
							&& (numOfMinutiae[0] >= MINIMUM_NUM_MINUTIAE)) {
						System.out.println("Reg. Capture 2 PASS QC. Qual[" + quality[0] + "] NFIQ[" + nfiqvalue
								+ "] Minutiae[" + numOfMinutiae[0] + "]");
						r2Captured = true;
						// this.enableRegisterAndVerifyControls();
					} else {
						System.out.println("Reg. Capture 2 FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue
								+ "] Minutiae[" + numOfMinutiae[0] + "]");
						// this.jButtonVerify.setEnabled(false);
						// this.jButtonRegister.setEnabled(false);
					}

				} else
					System.out.println("CreateTemplate() Error : " + iError);
			}
		} else

			System.out.println("GetImageEx() Error : " + iError);

		return imgRegistration2;
	}

	@Override
	public BufferedImage capturar3() {
		int[] quality = new int[1];
		int[] numOfMinutiae = new int[1];
		byte[] imageBuffer1 = ((java.awt.image.DataBufferByte) imgVerification.getRaster().getDataBuffer()).getData();
		long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;

		iError = fplib.GetImageEx(imageBuffer1, 5 * 1000, 0, 50);
		fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
		System.out.println("Calidad huella 3:" + quality[0]);
		SGFingerInfo fingerInfo = new SGFingerInfo();
		fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
		fingerInfo.ImageQuality = quality[0];
		fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
		fingerInfo.ViewNumber = 1;

		if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			// this.jLabelVerifyImage.setIcon(new
			// ImageIcon(imgVerification.getScaledInstance(130,150,Image.SCALE_DEFAULT)));
			if (quality[0] < MINIMUM_QUALITY)
				System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0]
						+ "]. Please try again");
			else {
				System.out.println("GetImageEx() Success [" + ret + "]");

				iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, vrfMin);
				if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE) {
					long nfiqvalue;
					long ret2 = fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1,
							quality);
					nfiqvalue = fplib.ComputeNFIQ(imageBuffer1, deviceInfo.imageWidth, deviceInfo.imageHeight);
					ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, vrfMin, numOfMinutiae);

					if ((quality[0] >= MINIMUM_QUALITY) && (nfiqvalue <= MAXIMUM_NFIQ)
							&& (numOfMinutiae[0] >= MINIMUM_NUM_MINUTIAE)) {
						System.out.println("Verification Capture PASS QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue
								+ "] Minutiae[" + numOfMinutiae[0] + "]");
						v1Captured = true;
						// this.enableRegisterAndVerifyControls();
					} else {
						System.out.println("Verification Capture FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue
								+ "] Minutiae[" + numOfMinutiae[0] + "]");
						// this.jButtonVerify.setEnabled(false);
					}
				} else
					System.out.println("CreateTemplate() Error : " + iError);
			}
		} else
			System.out.println("GetImageEx() Error : " + iError);

		return imgVerification;
	}

	@Override
	public void onLED() {
		if (fplib != null) {
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
	public void guardarImagen(BufferedImage bimg) throws IOException, SerialException, SQLException{
		ImageIcon imgi = new ImageIcon(bimg.getScaledInstance(130, 150, Image.SCALE_DEFAULT));
		Image img= imgi.getImage();
		//Graphics2D g2d = bimg.createGraphics();
		//g2d.drawImage(img, 0, 0, null);
		ByteArrayOutputStream baos = null;
		try {
		    baos = new ByteArrayOutputStream();
		    ImageIO.write(bimg, "png", baos);
		} finally {
		    try {
		        baos.close();
		    } catch (Exception e) {
		    }
		}
		//ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		Blob huella = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
		FichaDocente fcdc= new FichaDocente();
		fcdc.setFcdcId(2);
		fcdc.setFcdcPrimerNombre("lino");
		fcdc.setFcdcSegundoNombre("geovany");
		fcdc.setFcdcApellidos("cusin");
		fcdc.setFcdcHuellaPulgar1(huella);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		em.merge(fcdc);
		
	}
}