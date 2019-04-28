/**
 * 
 */
package managedBeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.RowEditEvent;

import model.Facultad;
import model.Feriado;
import servicios.SrvRegistroFeriadoLocal;

/**
 * @author wespana
 *
 */

@ManagedBean(name = "registroFeriado")
@ViewScoped
public class RegistroFeriado {

	/**
	 * Constante definida para el formato de fecha.
	 */
	private static final String FORMATOFECHA = "yyyy-MM-dd";

	/**
	 * Interfaz definida para los servicios de administracion feriados.
	 */
	@EJB
	private SrvRegistroFeriadoLocal srvFer;

	private Facultad selectFacultad;
	private List<Facultad> lstFacultad;
	private String fechaInicio;
	private String fechaFin;
	private List<Feriado> lstFeriados;
	private Feriado feriadoRegistro;
	private String fechaFeriado;

	@PostConstruct
	public void init() {
		lstFacultad = srvFer.listarFacultades();
		initRegistroFeriado();
	}

	/**
	 * Metodo que inicializa el objeto a guardar.
	 */
	private void initRegistroFeriado() {
		feriadoRegistro = new Feriado();
		selectFacultad = new Facultad();
	}

	/**
	 * Metodo que permite limpiar los filtros ingresados.
	 */
	public void limpiarFiltros() {
		selectFacultad.setFclId(null);
		fechaInicio = null;
		fechaFin = null;
	}

	/**
	 * Metodo que permite guardar un nuevo registro de feriado.
	 */
	public void guardarFeriado() {
		feriadoRegistro.setFclId(selectFacultad.getFclId());
		srvFer.guardarActualizarFeriado(feriadoRegistro);
	}

	/**
	 * Metodo definido para la busqueda de feriados por facultad seleccionada.
	 */
	public void buscarFeriados() {
		if (validarFacultad()) {
			try {
				if (validarFechas()) {
					Date inicio = new SimpleDateFormat(FORMATOFECHA).parse(fechaInicio);
					Date fin = new SimpleDateFormat(FORMATOFECHA).parse(fechaFin);
					lstFeriados = srvFer.listarFeriados(selectFacultad.getFclId(), inicio, fin);
					if (!lstFeriados.isEmpty()) {
						mostrarMensaje("Feriados encontrados.", "");
					} else {
						mostrarMensaje("No existen feriados definidos.", "");
					}
				}
			} catch (ParseException e) {
				System.out.println("Error al convertir fechas para consulta: " + e);
			}
		}
	}

	/**
	 * Metodo que permite setear el id de la facultad seleccionada.
	 * 
	 * @param event
	 */
	public void setFacultadID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectFacultad.setFclId((Integer) event.getNewValue());
		} else {
			selectFacultad.setFclId(null);
		}
	}

	/**
	 * Metodo definido para eliminar un registro de feriado.
	 * 
	 * @param feriado
	 */
	public void eliminarFeriado(Feriado feriado) {
		List<Feriado> lstAux = new ArrayList<>();
		for (Feriado f : lstFeriados) {
			if (f.getFrdId() != feriado.getFrdId()) {
				lstAux.add(f);
			} else {
				srvFer.eliminarFeriado(f);
			}
		}
		/** lista restante de feriados */
		lstFeriados = lstAux;
	}

	/**
	 * Metodo que permite guardar los cambios editados por fila.
	 * 
	 * @param event
	 */
	public void onRowEdit(RowEditEvent event) {
		actualizarFeriado((Feriado) event.getObject());
		FacesMessage msg = new FacesMessage("Registro editado", ((Feriado) event.getObject()).getFrdFecha() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Metodo que actualiza cambios en registro de feriado.
	 * 
	 * @param feriado
	 * @return
	 */
	private String actualizarFeriado(Feriado feriado) {
		try {
			if (feriado.getFrdId() > 0) {
				/** registro editado */
				srvFer.guardarActualizarFeriado(feriado);
			} else {
				/** nuevo registro */
				feriado.setFclId(selectFacultad.getFclId());
				srvFer.guardarActualizarFeriado(feriado);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizar feriado: " + e);
		}
		buscarFeriados();
		return "registroFeriados";
	}

	/**
	 * Metodo definido para la accion de cancelar edicion.
	 * 
	 * @param event
	 */
	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Metodo definido para agregar una nueva fila de registro feriado.
	 */
	public void onAddNew() {
		if (validarFacultad()) {
			Feriado feriadoNuevo = new Feriado();
			if (lstFeriados.isEmpty()) {
				feriadoNuevo.setFclId(selectFacultad.getFclId());
			} else {
				feriadoNuevo.setFclId(lstFeriados.get(0).getFclId());
			}
			lstFeriados.add(feriadoNuevo);

			FacesMessage msg = new FacesMessage("Nuevo registro agregado.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
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
			limpiarFiltros();
			return false;
		}
	}

	/**
	 * Metodo definido para validar fechas del filtro de búsqueda.
	 * 
	 * @return
	 * @throws ParseException
	 */
	private boolean validarFechas() throws ParseException {
		if (fechaInicio != null && fechaFin != null && fechaInicio.length() > 0 && fechaFin.length() > 0) {
			Date inicio = new SimpleDateFormat(FORMATOFECHA).parse(fechaInicio);
			Date fin = new SimpleDateFormat(FORMATOFECHA).parse(fechaFin);
			if (inicio.getTime() <= fin.getTime()) {
				return true;
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La fecha inicio debe ser menor que la fecha fin, verifique por favor.", "");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return false;
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fecha inicio y fecha fin requeridas, verifique por favor.", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarFiltros();
			return false;
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
	 * The srvFer to get.
	 * 
	 * @return the srvFer
	 */
	public SrvRegistroFeriadoLocal getSrvFer() {
		return srvFer;
	}

	/**
	 * The srvFer to set.
	 * 
	 * @param srvFer
	 */
	public void setSrvFer(SrvRegistroFeriadoLocal srvFer) {
		this.srvFer = srvFer;
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
	 * The lstFeriados to get.
	 * 
	 * @return the lstFeriados
	 */
	public Collection<Feriado> getLstFeriados() {
		return lstFeriados;
	}

	/**
	 * The lstFeriados to set.
	 * 
	 * @param lstFeriados
	 */
	public void setLstFeriados(List<Feriado> lstFeriados) {
		this.lstFeriados = lstFeriados;
	}

	/**
	 * The fechaInicio to get.
	 * 
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * The fechaInicio to set.
	 * 
	 * @param fechaInicio
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * The fechaFin to get.
	 * 
	 * @return the fechaFin
	 */
	public String getFechaFin() {
		return fechaFin;
	}

	/**
	 * The fechaFin to set.
	 * 
	 * @param fechaFin
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * The fechaFeriado to get.
	 * 
	 * @return the fechaFeriado
	 */
	public String getFechaFeriado() {
		return fechaFeriado;
	}

	/**
	 * The fechaFeriado to set.
	 * 
	 * @param fechaFeriado
	 */
	public void setFechaFeriado(String fechaFeriado) {
		this.fechaFeriado = fechaFeriado;
	}

	/**
	 * The feriadoRegistro to get.
	 * 
	 * @return the feriadoRegistro
	 */
	public Feriado getFeriadoRegistro() {
		return feriadoRegistro;
	}

	/**
	 * The feriadoRegistro to set.
	 * 
	 * @param feriadoRegistro
	 */
	public void setFeriadoRegistro(Feriado feriadoRegistro) {
		this.feriadoRegistro = feriadoRegistro;
	}
}
