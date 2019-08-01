package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvAdministrarParametroLocal;
import ec.uce.edu.biometrico.jpa.Dependencia;
import ec.uce.edu.biometrico.jpa.Parametro;

/**
 * @author wespana
 *
 */

@ManagedBean(name = "administrarParametro")
@ViewScoped
public class AdministrarParametro {

	/**
	 * Interfaz definida para los servicios de administracion parametros.
	 */
	@EJB
	private SrvAdministrarParametroLocal srvAdm;
	private Login beanLogin;

	private List<Parametro> lstParametro;
	private Parametro registroParametro;
	private Dependencia selectFacultad;
	private List<Dependencia> lstFacultad = new ArrayList<>();
	private String horaEntradaAntes;
	private String horaSalidaAntes;
	private String horaEntradaDespues;
	private String horaSalidaDespues;
	private String horaAtraso;
	private static String[] camposParametros = { "ENTRADA ANTES", "ENTRADA DESPUES", "ATRASO", "SALIDA ANTES",
			"SALIDA DESPUES" };

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		lstFacultad.add(beanLogin.getDt().get(0).getCarrera().getDependencia());
		// lstFacultad = srvAdm.listarFacultades();
		initRegistroParametro();
	}

	/**
	 * Metodo que inicializa el objeto a guardar.
	 */
	private void initRegistroParametro() {
		registroParametro = new Parametro();
		selectFacultad = new Dependencia();
	}

	public void setFacultadID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectFacultad.setDpnId((Integer) event.getNewValue());
		} else {
			selectFacultad.setDpnId(null);
		}
	}

	/**
	 * Metodo que busca los parametros segun la facultad seleccionada.
	 */
	public void buscarParametros() {
		if (validarFacultad()) {
			lstParametro = srvAdm.listarParametroxFacultad(selectFacultad.getDpnId());
			if (!lstParametro.isEmpty()) {
				buscarParametroVista(0);
				buscarParametroVista(1);
				buscarParametroVista(2);
				buscarParametroVista(3);
				buscarParametroVista(4);
				mostrarMensaje("Par\u00e1metros encontrados.", "Info!");
			} else {
				horaEntradaAntes = "";
				horaSalidaAntes = "";
				horaEntradaDespues = "";
				horaSalidaDespues = "";
				horaAtraso = "";
				FacesMessage msg = new FacesMessage("No existen par\u00e1metros para la facultad seleccionada.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	/**
	 * Metodo que busca los nombres de los parametros correspondientes a las
	 * variables.
	 */
	private void buscarParametroVista(int index) {
		if (lstParametro.get(index).getPrmDescripcion().equalsIgnoreCase(camposParametros[0])) {
			horaEntradaAntes = lstParametro.get(index).getPrmValor();
		} else if (lstParametro.get(index).getPrmDescripcion().equalsIgnoreCase(camposParametros[1])) {
			horaEntradaDespues = lstParametro.get(index).getPrmValor();
		} else if (lstParametro.get(index).getPrmDescripcion().equalsIgnoreCase(camposParametros[2])) {
			horaAtraso = lstParametro.get(index).getPrmValor();
		} else if (lstParametro.get(index).getPrmDescripcion().equalsIgnoreCase(camposParametros[3])) {
			horaSalidaAntes = lstParametro.get(index).getPrmValor();
		} else {
			horaSalidaDespues = lstParametro.get(index).getPrmValor();
		}
	}

	/**
	 * Metodo definido para validar que exista una facultad seleccionada.
	 * 
	 * @return
	 */
	private boolean validarFacultad() {
		if (selectFacultad.getDpnId() != null) {
			return true;
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La facultad es requerida, verifique por favor.", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			horaEntradaAntes = "";
			horaSalidaAntes = "";
			horaEntradaDespues = "";
			horaSalidaDespues = "";
			horaAtraso = "";
			return false;
		}
	}

	/**
	 * Metodo para guardar o actualizar los parametros.
	 */
	public void guardarParametro() {
		if (validarFacultad()) {
			registroParametro.setDependencia(selectFacultad);
			validarListaParametro(0);
			registroParametro.setPrmValor(horaEntradaAntes);
			srvAdm.guardarActualizarParametro(registroParametro);
			validarListaParametro(1);
			registroParametro.setPrmValor(horaEntradaDespues);
			srvAdm.guardarActualizarParametro(registroParametro);
			validarListaParametro(2);
			registroParametro.setPrmValor(horaAtraso);
			srvAdm.guardarActualizarParametro(registroParametro);
			validarListaParametro(3);
			registroParametro.setPrmValor(horaSalidaAntes);
			srvAdm.guardarActualizarParametro(registroParametro);
			validarListaParametro(4);
			registroParametro.setPrmValor(horaSalidaDespues);
			srvAdm.guardarActualizarParametro(registroParametro);
			mostrarMensaje("Se han guardado los cambios.", "Success!");
		}
	}

	/**
	 * Metodo que valida si la lista de parametros esta llena setea el id del
	 * parametro a actualizar caso contrario envia 0 para guardar un nuevo
	 * registro.
	 */
	private void validarListaParametro(int index) {
		if (!lstParametro.isEmpty()) {
			registroParametro.setPrmId(lstParametro.get(index).getPrmId());
			registroParametro.setPrmDescripcion(lstParametro.get(index).getPrmDescripcion());
		} else {
			registroParametro.setPrmId(0);
			registroParametro.setPrmDescripcion(camposParametros[index]);
		}
	}

	/**
	 * Metodo definido para mostrar mensajes.
	 */
	private void mostrarMensaje(String mensaje, String tipo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, tipo);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * The srvAdm to get.
	 * 
	 * @return the srvAdm
	 */
	public SrvAdministrarParametroLocal getSrvAdm() {
		return srvAdm;
	}

	/**
	 * The srvAdm to set.
	 * 
	 * @param srvAdm
	 */
	public void setSrvAdm(SrvAdministrarParametroLocal srvAdm) {
		this.srvAdm = srvAdm;
	}

	/**
	 * The lstParametro to get.
	 * 
	 * @return the lstParametro
	 */
	public List<Parametro> getLstParametro() {
		return lstParametro;
	}

	/**
	 * The lstParametro to set.
	 * 
	 * @param lstParametro
	 */
	public void setLstParametro(List<Parametro> lstParametro) {
		this.lstParametro = lstParametro;
	}

	/**
	 * The registroParametro to get.
	 * 
	 * @return the registroParametro
	 */
	public Parametro getRegistroParametro() {
		return registroParametro;
	}

	/**
	 * The registroParametro to set.
	 * 
	 * @param registroParametro
	 */
	public void setRegistroParametro(Parametro registroParametro) {
		this.registroParametro = registroParametro;
	}

	/**
	 * The selectFacultad to get.
	 * 
	 * @return the selectFacultad
	 */
	public Dependencia getSelectFacultad() {
		return selectFacultad;
	}

	/**
	 * The selectFacultad to set.
	 * 
	 * @param selectFacultad
	 */
	public void setSelectFacultad(Dependencia selectFacultad) {
		this.selectFacultad = selectFacultad;
	}

	/**
	 * The lstFacultad to get.
	 * 
	 * @return the lstFacultad
	 */
	public List<Dependencia> getLstFacultad() {
		return lstFacultad;
	}

	/**
	 * The lstFacultad to set.
	 * 
	 * @param lstFacultad
	 */
	public void setLstFacultad(List<Dependencia> lstFacultad) {
		this.lstFacultad = lstFacultad;
	}

	/**
	 * The horaEntradaAntes to get.
	 * 
	 * @return the horaEntradaAntes
	 */
	public String getHoraEntradaAntes() {
		return horaEntradaAntes;
	}

	/**
	 * The horaEntradaAntes to set.
	 * 
	 * @param horaEntradaAntes
	 */
	public void setHoraEntradaAntes(String horaEntrada) {
		this.horaEntradaAntes = horaEntrada;
	}

	/**
	 * The horaSalidaAntes to get.
	 * 
	 * @return the horaSalidaAntes
	 */
	public String getHoraSalidaAntes() {
		return horaSalidaAntes;
	}

	/**
	 * The horaSalidaAntes to set.
	 * 
	 * @param horaSalidaAntes
	 */
	public void setHoraSalidaAntes(String horaSalida) {
		this.horaSalidaAntes = horaSalida;
	}

	/**
	 * @return the horaEntradaDespues
	 */
	public String getHoraEntradaDespues() {
		return horaEntradaDespues;
	}

	/**
	 * @param horaEntradaDespues
	 *            the horaEntradaDespues to set
	 */
	public void setHoraEntradaDespues(String horaEntradaDespues) {
		this.horaEntradaDespues = horaEntradaDespues;
	}

	/**
	 * @return the horaSalidaDespues
	 */
	public String getHoraSalidaDespues() {
		return horaSalidaDespues;
	}

	/**
	 * @param horaSalidaDespues
	 *            the horaSalidaDespues to set
	 */
	public void setHoraSalidaDespues(String horaSalidaDespues) {
		this.horaSalidaDespues = horaSalidaDespues;
	}

	/**
	 * @return the horaAtraso
	 */
	public String getHoraAtraso() {
		return horaAtraso;
	}

	/**
	 * @param horaAtraso
	 *            the horaAtraso to set
	 */
	public void setHoraAtraso(String horaAtraso) {
		this.horaAtraso = horaAtraso;
	}

}