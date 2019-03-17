package managedBeans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialException;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.sun.javafx.iio.ImageStorage.ImageType;

import servicios.SrvDocenteLocal;
import lector.JSDCLocal;
import model.FichaDocente;
import model.TipoHuella;

@ManagedBean(name = "huella", eager = true)
@ViewScoped
public class Huellas {

	// Interfaz de servicio
	@EJB
	private SrvDocenteLocal srvDcnt;
	@EJB
	private JSDCLocal jsdcBean;

	private FichaDocente selectDcnt;

	private Integer tpId;
	private TipoHuella selectTp;

	private List<TipoHuella> lstTphl;

	private StreamedContent graphic1;
	private StreamedContent graphic2;
	private BufferedImage bimg1;
	private BufferedImage bimg2;
	private int quality1;
	private int quality2;
	private boolean flagVrf;
	private boolean flagDvc;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Registro beanDcnt = context.getApplication().evaluateExpressionGet(context, "#{registro}", Registro.class);
		if (jsdcBean.inicializar()) {
			jsdcBean.onLED();
			flagDvc = true;
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Lector no encontrado", null);
			ctx.addMessage(null, msg);
			flagDvc = false;
		}
		selectDcnt = beanDcnt.getSelectDcnt();
		lstTphl = srvDcnt.listarDedos();
		flagVrf = true;

	}

	public void apagar() {
		jsdcBean.cerrar();
		bimg1 = null;
		bimg2 = null;
		graphic1 = null;
		graphic2 = null;
		quality1 = 0;
		quality2 = 0;
		lstTphl = null;
	}

	public void capturar1() throws IOException {
		bimg1 = jsdcBean.capturar();
		if (bimg1 == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Captura de huella fallida", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			quality1 = jsdcBean.calidad();
			graphic1 = toImage(bimg1);
		}

	}

	public void capturar2() throws IOException {
		bimg2 = jsdcBean.capturar();
		if (bimg2 == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Captura de huella fallida", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			quality2 = jsdcBean.calidad();
			graphic2 = toImage(bimg2);
		}
	}

	public String verificar() throws SerialException, IOException, SQLException {
		if (bimg1 == null || bimg2 == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Huellas vacias", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		flagVrf = !jsdcBean.verificar();
		if (flagVrf) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verificacion de huellas fallida", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Huellas coinciden", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}

	}

	public void registrar() throws SerialException, IOException, SQLException {
		if (tpId != null && selectDcnt != null) {
			selectTp = new TipoHuella();
			selectTp.setTphlId(tpId);
			srvDcnt.guardarImagen(bimg1, bimg2, selectDcnt, selectTp);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Huellas guardadas", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			jsdcBean.cerrar();
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Escoja el dedo al que pertece las huellas",
					null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public StreamedContent toImage(BufferedImage bimg) throws IOException {
		ByteArrayOutputStream baos = null;
		StreamedContent grafico = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(bimg, "png", baos);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				baos.close();
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
		grafico = new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()));
		return grafico;
	}

	public String regresar() {
		return "registro";
	}

	public String limpiar() {
		bimg1 = null;
		bimg2 = null;
		graphic1 = null;
		graphic2 = null;
		quality1 = 0;
		quality2 = 0;
		flagVrf = true;
		jsdcBean.inicializar();
		return null;
	}
	// ####### Setters y Getters Tipo Huella #######

	public List<TipoHuella> getLstTphl() {
		return lstTphl;
	}

	public void setLstTphl(List<TipoHuella> lstTphl) {
		this.lstTphl = lstTphl;
	}

	// ####### Setters y Getters Panel Huella #######

	public Integer getTpId() {
		return tpId;
	}

	public boolean isFlagDvc() {
		return flagDvc;
	}

	public void setFlagDvc(boolean flagDvc) {
		this.flagDvc = flagDvc;
	}

	public void setTpId(Integer tpId) {
		this.tpId = tpId;
	}

	public StreamedContent getGraphic1() {
		return graphic1;
	}

	public void setGraphic1(StreamedContent graphic1) {
		this.graphic1 = graphic1;
	}

	public StreamedContent getGraphic2() {
		return graphic2;
	}

	public void setGraphic2(StreamedContent graphic2) {
		this.graphic2 = graphic2;
	}

	public int getQuality1() {
		return quality1;
	}

	public void setQuality1(int quality1) {
		this.quality1 = quality1;
	}

	public int getQuality2() {
		return quality2;
	}

	public void setQuality2(int quality2) {
		this.quality2 = quality2;
	}

	public boolean isFlagVrf() {
		return flagVrf;
	}

	public void setFlagVrf(boolean flagVrf) {
		this.flagVrf = flagVrf;
	}

}
