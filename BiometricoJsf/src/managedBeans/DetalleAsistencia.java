package managedBeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Asistencia;
import model.Carrera;
import model.FichaDocente;
import model.Horario;
import servicios.SrvDocenteLocal;
import servicios.SrvEmpleadoLocal;

@ManagedBean(name = "detalleAss")
@RequestScoped
public class DetalleAsistencia {
	/**
	 * Constante definida para el formato de fecha.
	 */
	private static final String FORMATOFECHA = "yyyy-MM-dd";

	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	@ManagedProperty(value = "#{principal}")
	private Principal principal;

	private String fechaInicio;
	private String fechaFin;
	private boolean mostrarFiltros;
	public List<Carrera> lstC;
	public List<FichaDocente> lstD;
	public List<Asistencia> lstA;

	public Integer crrId;
	public Integer fcdId;
	public Carrera selectCrr;
	public FichaDocente selectDcn;
	private Asistencia selectAss;

	@PostConstruct
	public void init() {
		// FacesContext context = FacesContext.getCurrentInstance();
		// principal = context.getApplication().evaluateExpressionGet(context,
		// "#{principal}", Principal.class);
		selectAss = new Asistencia();
		selectCrr = new Carrera();
		selectDcn = new FichaDocente();
		if (principal.flagEmpleado) {
			mostrarFiltros = true;
			lstC = srvEmp.listarCarreras(principal.fcId);
		} else {
			mostrarFiltros = false;
		}
	}

	public void listarDcnts(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstD = srvEmp.listarDocentes(crrId);
		} else {
			lstD = null;
			System.out.println("No ha seleccionada una carrera: ");
		}

	}

	public void listarAssts() {
		if (validarDocenteSelecionado()) {
			try {
				if (validarFechas()) {
					Date inicio = new SimpleDateFormat(FORMATOFECHA).parse(fechaInicio);
					Date fin = new SimpleDateFormat(FORMATOFECHA).parse(fechaFin);
					if (principal.flagDocente) {
						lstA = srvDnt.listarAsistencia(principal.fdId, inicio, fin, null);
					} else if (principal.flagEmpleado) {
						lstA = srvDnt.listarAsistencia(fcdId, inicio, fin, crrId);
					}

					if (!lstA.isEmpty()) {
						mostrarMensaje("Asistencias encontradas.", "");
					} else {
						mostrarMensaje("No existen asistencias registradas.", "");
					}
				}
			} catch (ParseException e) {
				System.out.println("Error al convertir fechas para consulta: " + e);
			}
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
	 * Metodo definido para validar que exista una facultad seleccionada.
	 * 
	 * @return
	 */
	private boolean validarDocenteSelecionado() {
		if (fcdId != null || principal.flagDocente) {
			return true;
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La facultad y el docente son requeridos, verifique por favor.", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarFiltros();
			return false;

		}
	}

	public String regresar() {
		return "principal";
	}

	/**
	 * Metodo que permite limpiar los filtros ingresados.
	 */
	public void limpiarFiltros() {
		fcdId = null;
		fechaInicio = null;
		fechaFin = null;
		crrId = null;
	}

	public void justificarFalta(Asistencia asistencia, Boolean justificarTodo) {
		System.out.println("Asistencia a justificar");
		if (justificarTodo) {
			if (lstA != null && !lstA.isEmpty()) {
				for (Asistencia justAssistencia : lstA) {
					actualizarAsistencia(justAssistencia);
				}
				listarAssts();
			}
		} else {
			actualizarAsistencia(asistencia);
			listarAssts();
		}
	}

	/**
	 * Metodo que permite actualizar una asistencia justificada.
	 * 
	 * @param asistencia
	 */
	private void actualizarAsistencia(Asistencia asistencia) {
		if (asistencia != null && asistencia.getAssId() != null
				&& (asistencia.getAssHoraEntrada() == null || asistencia.getAssHoraSalida() == null)) {
			Horario horario = srvDnt.findHorarioByAsistencia(asistencia.getAssId());
			if (horario != null) {
				validarDataAsistencia(asistencia, horario);
				srvDnt.actualizarAsistencia(asistencia);
			} else {
				System.out.println("No existe un horario registrado para la asistencia a justificar");
			}
		} else {
			System.out.println("No existe la asistencia a justificar");
		}
	}

	/**
	 * Metodo que permite validar la asistencia a justificar
	 * 
	 * @param asistencia
	 * @param horario
	 */
	private void validarDataAsistencia(Asistencia asistencia, Horario horario) {
		if (asistencia.getAssHoraEntrada() == null && asistencia.getAssHoraSalida() == null) {
			asistencia.setAssEstado("JUSTIFICACION COMPLETA");
			asistencia.setAssHoraEntrada(horario.getHrrInicio());
			asistencia.setAssHoraSalida(horario.getHrrFin());
		} else {
			if (asistencia.getAssHoraEntrada() == null) {
				asistencia.setAssHoraEntrada(horario.getHrrInicio());
				asistencia.setAssEstado("JUSTIFICACION HORA ENTRADA");
			}
			if (asistencia.getAssHoraSalida() == null) {
				asistencia.setAssHoraSalida(horario.getHrrFin());
				asistencia.setAssEstado("JUSTIFICACION HORA SALIDA");
			}
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

	// setters and getters Docente

	public Asistencia getAss() {
		return selectAss;
	}

	public void setAss(Asistencia ass) {
		this.selectAss = ass;
	}

	public List<Asistencia> getLstA() {
		return lstA;
	}

	public void setLstA(List<Asistencia> lstA) {
		this.lstA = lstA;
	}

	// setters and getters Empleado

	public List<Carrera> getLstC() {
		return lstC;
	}

	public void setLstC(List<Carrera> lstC) {
		this.lstC = lstC;
	}

	public Carrera getSelectCrr() {
		return selectCrr;
	}

	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
	}

	public List<FichaDocente> getLstD() {
		return lstD;
	}

	public void setLstD(List<FichaDocente> lstD) {
		this.lstD = lstD;
	}

	public FichaDocente getSelectDcn() {
		return selectDcn;
	}

	public void setSelectDcn(FichaDocente selectDcn) {
		this.selectDcn = selectDcn;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public Integer getFcdId() {
		return fcdId;
	}

	public void setFcdId(Integer fcdId) {
		this.fcdId = fcdId;
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
	 * The principal to get.
	 * 
	 * @return the principal
	 */
	public Principal getPrincipal() {
		return principal;
	}

	/**
	 * The principal to set.
	 * 
	 * @param principal
	 */
	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	/**
	 * The mostrarFiltros to get.
	 * 
	 * @return the mostrarFiltros
	 */
	public boolean isMostrarFiltros() {
		return mostrarFiltros;
	}

	/**
	 * The mostrarFiltros to set.
	 * 
	 * @param mostrarFiltros
	 */
	public void setMostrarFiltros(boolean mostrarFiltros) {
		this.mostrarFiltros = mostrarFiltros;
	}
}
