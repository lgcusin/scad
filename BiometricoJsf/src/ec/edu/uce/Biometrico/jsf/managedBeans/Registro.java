package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javax.faces.event.ValueChangeEvent;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialException;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.Biometrico.ejb.servicios.impl.SrvLogin;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.JSDCLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvLoginLocal;
import ec.edu.uce.biometrico.jpa.FichaDocente;
import ec.edu.uce.biometrico.jpa.TipoHuella;

@ManagedBean(name = "registro")
@SessionScoped
public class Registro {

	// Interfaz de servicio
	@EJB
	private SrvDocenteLocal srvDcnt;
	@EJB
	private JSDCLocal srvDvc;
	@EJB
	private SrvLoginLocal srvLog;
	public Login beanLogin;
	// ObjetoF
	private FichaDocente selectDcnt;
	private TipoHuella selectTp;

	// Listas de objetos
	public List<FichaDocente> lstDcnt;
	private List<TipoHuella> lstTphl;

	// Entradas y salidas
	public String parametro;
	private Integer tpId;
	private BufferedImage bimg1;
	private BufferedImage bimg2;
	private StreamedContent graphic1;
	private StreamedContent graphic2;
	private int quality1;
	private int quality2;
	private boolean flagDvc;
	private boolean flagVrf;
	private boolean flagTipo;
	private boolean flagMovil;
	private boolean flagSinHuella;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		lstDcnt = srvDcnt.listarDocentesxParametroxFacultad("  ", 0);
	}

	// ####### Pagina busqueda #######
	public void listar() {
		lstDcnt = srvDcnt.listarDocentesxParametroxFacultad(parametro,
				beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId());
	}

	public String verHuellas() {
		if (selectDcnt == null) {
			FacesMessage msg = new FacesMessage("No ha seleccionado ningun docente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		} else {
			if (srvDvc.inicializar()) {
				srvDvc.onLED();
				selectDcnt.setFcdcDetallePuestos(srvLog.listarDetallePuestoDocente(selectDcnt.getFcdcId()));
				lstTphl = srvDcnt.listarTipoHuellas();
				flagDvc = true;
				flagVrf = true;
				flagTipo = true;
				return "huellas";
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Lector no encontrado", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				flagDvc = false;
				return null;
			}

		}
	}

	public String regresar() {
		parametro = null;
		lstDcnt = null;
		selectDcnt = null;
		return "principal";
	}

	public String limpiar() {
		lstDcnt = null;
		parametro = " ";
		return null;
	}

	/* ##################### METODOS DE HUELLAS ########################### */
	public void apagar() {
		srvDvc.cerrar();
		bimg1 = null;
		bimg2 = null;
		graphic1 = null;
		graphic2 = null;
		quality1 = 0;
		quality2 = 0;
		lstTphl = null;
	}

	public void verificarTipoId(ValueChangeEvent event) {
		if ((Integer) event.getNewValue() == 5 || (Integer) event.getNewValue() == 4) {
			flagTipo = false;
			flagVrf = false;
			 getEstados();
		} else {
			flagTipo = true;
			flagVrf = true;
		}
	}

	public void capturar1() throws IOException {
		bimg1 = srvDvc.capturar();
		if (bimg1 == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Captura de huella fallida", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			quality1 = srvDvc.calidad();
			graphic1 = toImage(bimg1);
		}

	}

	public void capturar2() throws IOException {
		bimg2 = srvDvc.capturar();
		if (bimg2 == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Captura de huella fallida", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			quality2 = srvDvc.calidad();
			graphic2 = toImage(bimg2);
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

	public String verificar() throws SerialException, IOException, SQLException {
		if (bimg1 == null || bimg2 == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Huellas vacias", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		flagVrf = !srvDvc.verificar();
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
			if (selectTp.getTphlId() == 1 || selectTp.getTphlId() == 2 || selectTp.getTphlId() == 3) {
				srvDcnt.guardarImagen(bimg1, bimg2, selectDcnt, selectTp);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Huellas guardadas", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				clean();
				srvDvc.cerrar();
			} else {
				srvDcnt.guardarActualizarEstados(selectDcnt, selectTp, flagMovil, flagSinHuella);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Estados guardados", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				 getEstados();
			}

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Escoja el dedo al que pertece las huellas o tipo de activacion", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String volver() {
		selectDcnt = null;
		apagar();
		return "registro";
	}
	
	public void getEstados(){
		List<Boolean> lstE= srvDcnt.listarestados(selectDcnt.getFcdcId());
		if(!lstE.isEmpty()){
			flagSinHuella= lstE.get(0);
			flagMovil= lstE.get(1);
		}
	}

	public String clean() {
		bimg1 = null;
		bimg2 = null;
		graphic1 = null;
		graphic2 = null;
		quality1 = 0;
		quality2 = 0;
		flagVrf = true;
		srvDvc.inicializar();
		return null;
	}
	// ####### Setters y Getters Busqueda #######

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public List<FichaDocente> getLstDcnt() {
		return lstDcnt;
	}

	public void setLstDcnt(List<FichaDocente> lstDcnt) {
		this.lstDcnt = lstDcnt;
	}

	public FichaDocente getSelectDcnt() {
		return selectDcnt;
	}

	public void setSelectDcnt(FichaDocente selectDcnt) {
		this.selectDcnt = selectDcnt;
	}

	// ##########getters and setters huellas######
	// Setters y Getters Tipo Huella

	public List<TipoHuella> getLstTphl() {
		return lstTphl;
	}

	public void setLstTphl(List<TipoHuella> lstTphl) {
		this.lstTphl = lstTphl;
	}

	/**
	 * @return the selectTp
	 */
	public TipoHuella getSelectTp() {
		return selectTp;
	}

	/**
	 * @param selectTp
	 *            the selectTp to set
	 */
	public void setSelectTp(TipoHuella selectTp) {
		this.selectTp = selectTp;
	}

	// Setters y Getters Panel Huella

	/**
	 * @return the flagMovil
	 */
	public boolean isFlagMovil() {
		return flagMovil;
	}

	/**
	 * @param flagMovil
	 *            the flagMovil to set
	 */
	public void setFlagMovil(boolean flagMovil) {
		this.flagMovil = flagMovil;
	}

	/**
	 * @return the flagSinHuella
	 */
	public boolean isFlagSinHuella() {
		return flagSinHuella;
	}

	/**
	 * @param flagSinHuella
	 *            the flagSinHuella to set
	 */
	public void setFlagSinHuella(boolean flagSinHuella) {
		this.flagSinHuella = flagSinHuella;
	}

	public Integer getTpId() {
		return tpId;
	}

	public void setTpId(Integer tpId) {
		this.tpId = tpId;
	}

	public boolean isFlagDvc() {
		return flagDvc;
	}

	public void setFlagDvc(boolean flagDvc) {
		this.flagDvc = flagDvc;
	}

	/**
	 * @return the flagTipo
	 */
	public boolean isFlagTipo() {
		return flagTipo;
	}

	/**
	 * @param flagTipo
	 *            the flagTipo to set
	 */
	public void setFlagTipo(boolean flagTipo) {
		this.flagTipo = flagTipo;
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
