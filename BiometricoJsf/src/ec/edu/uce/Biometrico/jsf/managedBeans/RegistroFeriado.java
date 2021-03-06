/**
 * 
 */
package ec.edu.uce.Biometrico.jsf.managedBeans;

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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.RowEditEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvRegistroFeriadoLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Carrera;
import ec.edu.uce.biometrico.jpa.Dependencia;
import ec.edu.uce.biometrico.jpa.Feriado;

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
	@EJB
	private SrvEmpleadoLocal srvEmp;
	private Login beanLogin;

	private Dependencia selectDependencia;
	private Carrera selectCarrera;
	private List<Dependencia> lstDependencia;
	private List<Carrera> lstCarrera;
	private String fechaInicio;
	private String fechaFin;
	private List<Feriado> lstFeriados;
	private Feriado feriadoRegistro;
	private String fechaFeriado;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		initRegistroFeriado();
		lstDependencia = srvFer.listarFacultades();
		// lstDependencia.add(beanLogin.getUsuarioRol().getUsuario().getPersona().getFichaEmpleados().get(0)
		// .getDetallePuestos().get(0).getCarrera().getDependencia());
	}

	/**
	 * Metodo que inicializa el objeto a guardar.
	 */
	private void initRegistroFeriado() {
		lstDependencia = new ArrayList<>();
		lstCarrera = new ArrayList<>();
		lstFeriados = new ArrayList<>();
		selectDependencia = new Dependencia();
		selectCarrera = new Carrera();
		feriadoRegistro = new Feriado();
	}

	/**
	 * Metodo que permite limpiar los filtros ingresados.
	 */
	public void limpiarFiltros() {
		selectDependencia.setDpnId(GeneralesConstantes.APP_ID_BASE);
		fechaInicio = null;
		fechaFin = null;
	}

	/**
	 * Metodo que permite guardar un nuevo registro de feriado.
	 */
	public void guardarFeriado() {
		feriadoRegistro.setFrdCarrera(selectCarrera);
		srvFer.guardarActualizarFeriado(feriadoRegistro);
	}

	/**
	 * Metodo definido para la busqueda de feriados por facultad seleccionada.
	 */
	public void buscarFeriados() {
		if (validarDependencia() && validarCarrera()) {
			try {
				if (validarFechas()) {
					Date inicio = new SimpleDateFormat(FORMATOFECHA).parse(fechaInicio);
					Date fin = new SimpleDateFormat(FORMATOFECHA).parse(fechaFin);
					if (selectCarrera.getCrrId() == 0) {
						lstFeriados = srvFer.listarFeriados(true, selectDependencia.getDpnId(), inicio, fin);
					} else {
						lstFeriados = srvFer.listarFeriados(false, selectCarrera.getCrrId(), inicio, fin);
					}
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

	private boolean validarCarrera() {
		if (selectCarrera.getCrrId() != GeneralesConstantes.APP_ID_BASE) {
			return true;
		} else {
			FacesUtil.mensajeError("La carrera es requerida, verifique por favor.");
			limpiarFiltros();
			return false;
		}
	}

	/**
	 * Metodo que permite setear el id de la facultad seleccionada.
	 * 
	 * @param event
	 */
	public void setDependenciaID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectDependencia.setDpnId((Integer) event.getNewValue());
			lstCarrera = srvEmp.listarCarrerasxFacultad((Integer) event.getNewValue());
		} else {
			selectDependencia.setDpnId(GeneralesConstantes.APP_ID_BASE);
		}
	}

	/**
	 * Metodo definido para setear el id de la facultad seleccionada.
	 * 
	 * @param event
	 */
	public void setCarreraID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectCarrera.setCrrId((Integer) event.getNewValue());
		} else {
			selectCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado una facultad.",
					"Warning!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	/**
	 * Metodo definido para agregar una nueva fila de registro feriado.
	 */
	public void onAddNew() {
		if (validarDependencia() && validarCarrera()) {
			Feriado feriadoNuevo = new Feriado();
			lstFeriados.add(feriadoNuevo);
		}
	}

	/**
	 * Metodo que permite guardar los cambios editados por fila.
	 * 
	 * @param event
	 */
	public void onRowEdit(RowEditEvent event) {
		Feriado feriado = (Feriado) event.getObject();
		if (feriado.getFrdFecha() != null && feriado.getFrdInicio() != null && feriado.getFrdFin() != null) {
			actualizarFeriado((Feriado) event.getObject());
			FacesUtil.mensajeInfo("Registro ingresado exitosamente");
		} else {
			FacesUtil.mensajeError("Los campos ingresados son erroneas, intente de nuevo");
		}

	}

	/**
	 * Metodo que actualiza cambios en registro de feriado.
	 * 
	 * @param feriado
	 * @return
	 */
	private String actualizarFeriado(Feriado feriado) {
		try {
			if (feriado.getFrdId() != GeneralesConstantes.APP_ID_BASE) {
				/** registro editado */
				feriado.setFrdCarrera(selectCarrera);
				srvFer.guardarActualizarFeriado(feriado);
			} else {
				/** nuevo registro */
				if (selectCarrera.getCrrId() == 0) {
					for (Carrera cr : lstCarrera) {
						feriado.setFrdId(GeneralesConstantes.APP_ID_BASE);
						feriado.setFrdCarrera(cr);
						srvFer.guardarActualizarFeriado(feriado);
					}
				} else {
					feriado.setFrdCarrera(selectCarrera);
					srvFer.guardarActualizarFeriado(feriado);
				}
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizar feriado: " + e);
		}
		buscarFeriados();
		return null;
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
	 * Metodo definido para validar que exista una facultad seleccionada.
	 * 
	 * @return
	 */
	private boolean validarDependencia() {
		if (selectDependencia.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
			return true;
		} else {
			FacesUtil.mensajeError("La facultad es requerida, verifique por favor.");
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

	public String regresar() {
		selectDependencia = null;
		selectCarrera = null;
		lstDependencia = null;
		lstCarrera = null;
		fechaInicio = null;
		fechaFin = null;
		lstFeriados = null;
		feriadoRegistro = null;
		return "principal";
	}

	/**
	 * Metodo definido para mostrar mensajes.
	 */
	private void mostrarMensaje(String mensaje, String tipo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, tipo);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * The selectDependencia to get.
	 * 
	 * @return the selectDependencia
	 */
	public Dependencia getSelectDependencia() {
		return selectDependencia;
	}

	/**
	 * @return the selectCarrera
	 */
	public Carrera getSelectCarrera() {
		return selectCarrera;
	}

	/**
	 * @param selectCarrera
	 *            the selectCarrera to set
	 */
	public void setSelectCarrera(Carrera selectCarrera) {
		this.selectCarrera = selectCarrera;
	}

	/**
	 * The selectDependencia to set.
	 * 
	 * @param selectDependencia
	 */
	public void setSelectDependencia(Dependencia selectDependencia) {
		this.selectDependencia = selectDependencia;
	}

	/**
	 * The lstDependencia to get.
	 * 
	 * @return the lstDependencia
	 */
	public List<Dependencia> getLstDependencia() {
		return lstDependencia;
	}

	/**
	 * The lstDependencia to set.
	 * 
	 * @param lstDependencia
	 */
	public void setLstDependencia(List<Dependencia> lstDependencia) {
		this.lstDependencia = lstDependencia;
	}

	/**
	 * @return the lstCarrera
	 */
	public List<Carrera> getLstCarrera() {
		return lstCarrera;
	}

	/**
	 * @param lstCarrera
	 *            the lstCarrera to set
	 */
	public void setLstCarrera(List<Carrera> lstCarrera) {
		this.lstCarrera = lstCarrera;
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
