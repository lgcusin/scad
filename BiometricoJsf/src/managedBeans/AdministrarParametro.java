package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Facultad;
import model.Parametro;
import servicios.SrvAdministrarParametroLocal;

/**
 * @author wespana
 *
 */

@ManagedBean(name = "administrarParametro")
@SessionScoped
public class AdministrarParametro {

	/**
	 * Interfaz definida para los servicios de administracion parametros.
	 */
	@EJB
	private SrvAdministrarParametroLocal srvAdm;

	private List<Parametro> lstParametro;
	private Parametro registroParametro;
	private Facultad selectFacultad;
	private List<Facultad> lstFacultad;
	private String horaEntrada;
	private String horaSalida;
	private static String[] camposParametros = { "ENTRADA", "SALIDA" };

	@PostConstruct
	public void init() {
		lstFacultad = srvAdm.listarFacultades();
		initRegistroParametro();
	}

	/**
	 * Metodo que inicializa el objeto a guardar.
	 */
	private void initRegistroParametro() {
		registroParametro = new Parametro();
		selectFacultad = new Facultad();
	}

	/**
	 * Metodo que busca los parametros segun la facultad seleccionada.
	 */
	public void buscarParametros() {
		if (validarFacultad()) {
			lstParametro = srvAdm.listarParametro(selectFacultad.getFclId());
			if (!lstParametro.isEmpty()) {
				buscarParametroVista(0);
				buscarParametroVista(1);
				mostrarMensaje("Par\u00e1metros encontrados.", "Info!");
			} else {
				horaEntrada = "";
				horaSalida = "";
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
		if (lstParametro.get(index).getPrmNombre().equalsIgnoreCase(camposParametros[0])) {
			horaEntrada = lstParametro.get(index).getPrmValor();
		} else {
			horaSalida = lstParametro.get(index).getPrmValor();
		}
	}

	/**
	 * Metodo definido para validar que exista una facultad seleccionada.
	 * 
	 * @return
	 */
	private boolean validarFacultad() {
		if (selectFacultad.getFclId() != null) {
			return true;
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La facultad es requerida, verifique por favor.", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			horaEntrada = "";
			horaSalida = "";
			return false;
		}
	}

	/**
	 * Metodo definido para setear el id de la facultad seleccionada.
	 * 
	 * @param event
	 */
	public void setFacultadID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectFacultad.setFclId((Integer) event.getNewValue());
		} else {
			selectFacultad.setFclId(null);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado una facultad.",
					"Warning!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	/**
	 * Metodo para guardar o actualizar los parametros.
	 */
	public void guardarParametro() {
		if (validarFacultad()) {
			registroParametro.setFclId(selectFacultad.getFclId());
			validarListaParametro(0);
			registroParametro.setPrmValor(horaEntrada);
			srvAdm.guardarActualizarParametro(registroParametro);
			validarListaParametro(1);
			registroParametro.setPrmValor(horaSalida);
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
			registroParametro.setPrmNombre(lstParametro.get(index).getPrmNombre());
		} else {
			registroParametro.setPrmId(0);
			registroParametro.setPrmNombre(camposParametros[index]);
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
	public Facultad getSelectFacultad() {
		return selectFacultad;
	}

	/**
	 * The selectFacultad to set.
	 * 
	 * @param selectFacultad
	 */
	public void setSelectFacultad(Facultad selectFacultad) {
		this.selectFacultad = selectFacultad;
	}

	/**
	 * The lstFacultad to get.
	 * 
	 * @return the lstFacultad
	 */
	public List<Facultad> getLstFacultad() {
		return lstFacultad;
	}

	/**
	 * The lstFacultad to set.
	 * 
	 * @param lstFacultad
	 */
	public void setLstFacultad(List<Facultad> lstFacultad) {
		this.lstFacultad = lstFacultad;
	}

	/**
	 * The horaEntrada to get.
	 * 
	 * @return the horaEntrada
	 */
	public String getHoraEntrada() {
		return horaEntrada;
	}

	/**
	 * The horaEntrada to set.
	 * 
	 * @param horaEntrada
	 */
	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	/**
	 * The horaSalida to get.
	 * 
	 * @return the horaSalida
	 */
	public String getHoraSalida() {
		return horaSalida;
	}

	/**
	 * The horaSalida to set.
	 * 
	 * @param horaSalida
	 */
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
}
