package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;

@ManagedBean(name = "detalleAss")
@ViewScoped
public class DetalleAsistencia {
	/**
	 * Constante definida para el formato de fecha.
	 */
	private static final String FORMATOFECHA = "yyyy-MM-dd";

	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	@EJB
	private SrvSeguimientoLocal srvSgm;
	@ManagedProperty(value = "#{login}")
	public Login beanLogin;

	private String fechaInicio;
	private String fechaFin;
	private boolean mostrarFiltros;
	public List<Carrera> lstCarreras;
	public List<FichaDocente> lstD;
	public List<Asistencia> lstA;

	public Integer crrId;
	public Integer fcdId;
	public Carrera selectCrr;
	public FichaDocente selectDcn;
	private Asistencia selectAss;
	private TemplatePDF templatePDF;

	@PostConstruct
	public void init() {
		selectAss = new Asistencia();
		selectCrr = new Carrera();
		selectDcn = new FichaDocente();
		if (beanLogin.adminFacultad) {
			mostrarFiltros = true;
			lstCarreras = srvEmp
					.listarCarrerasxFacultad(beanLogin.getDt().get(0).getCarrera().getDependencia().getDpnId());
		} else {
			mostrarFiltros = false;
		}
		templatePDF = new TemplatePDF();
	}

	public void listarDcnts(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstD = srvEmp.listarDocentesxCarrera(crrId);
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
					if (beanLogin.Docente) {
						lstA = srvDnt.listarAsistencia(beanLogin.getUsuarioRol().getUsuario().getPersona()
								.getFichaDocentes().get(0).getFcdcId(), inicio, fin, null);
					} else if (beanLogin.adminFacultad) {
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
		if (fcdId != null || beanLogin.Docente) {
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
		lstA = null;
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
			HorarioAcademico horario = srvDnt.findHorarioByAsistencia(asistencia);
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
	private void validarDataAsistencia(Asistencia asistencia, HorarioAcademico horario) {
		if (asistencia.getAssHoraEntrada() == null && asistencia.getAssHoraSalida() == null) {
			asistencia.setAssEstado("JUSTIFICACION COMPLETA");
			asistencia.setAssHoraEntrada(horario.getHracHoraInicio().toString());
			asistencia.setAssHoraSalida(horario.getHracHoraFin().toString());
		} else {
			if (asistencia.getAssHoraEntrada() == null) {
				asistencia.setAssHoraEntrada(horario.getHracHoraInicio().toString());
				asistencia.setAssEstado("JUSTIFICACION HORA ENTRADA");
			}
			if (asistencia.getAssHoraSalida() == null) {
				asistencia.setAssHoraSalida(horario.getHracHoraFin().toString());
				asistencia.setAssEstado("JUSTIFICACION HORA SALIDA");
			}
		}
	}

	/**
	 * Metodo definido para validar fechas del filtro de busqueda.
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

	public void generarpdf() {
		if (!lstA.isEmpty()) {
			String docente = getInfoDocente();
			Date fechaActual = new Date();
			DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			String fechaProceso = formatoFecha.format(fechaActual);
			DateFormat formatoFechaArchivo = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
			String fechaArchivo = formatoFechaArchivo.format(fechaActual);
			String[] encabezado = { "Fecha", "Materia", "Hora entrada", "Hora salida", "Estado" };
			String textoIntroduccion = "Detalle de asistencia docentes";
			String infoDocente = "Docente: " + docente;
			String textoFechaDesde = "Fecha desde: " + fechaInicio;
			String textoFechaHasta = "Fecha hasta: " + fechaFin;
			templatePDF.openDocument(docente + "_" + fechaArchivo);
			templatePDF.addImageCabecera();
			templatePDF.addMetaData("Sistema Biométrico UCE", "Asistencia Docente", docente);
			templatePDF.addTitulo("Reporte Asistencia Docente", "Sistema Biométrico UCE", fechaProceso);
			templatePDF.addParrafo(textoFechaDesde);
			templatePDF.addParrafo(textoFechaHasta);
			templatePDF.addParrafoDetalle(textoIntroduccion);
			templatePDF.addParrafo(infoDocente);
			if (!lstA.isEmpty()) {
				templatePDF.createTable(encabezado, getAsistenciaDocentes());
			}
			templatePDF.addImagePie();
			templatePDF.closeDocument();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Archivo generado en la carpeta SistemaBiometricoUCE del su disco local C, verifique por favor.",
					"");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			mostrarMensaje("No existen asistencias registradas.", "");
		}
	}

	private String getInfoDocente() {
		String docente = "";
		if (beanLogin.Docente) {
			docente = beanLogin.getUsuarioRol().getUsuario().getPersona().getPrsNombres() + " "
					+ beanLogin.getUsuarioRol().getUsuario().getPersona().getPrsPrimerApellido() + " "
					+ beanLogin.getUsuarioRol().getUsuario().getPersona().getPrsSegundoApellido();
		} else {
			if (!lstD.isEmpty()) {
				for (FichaDocente fichaDocente : lstD) {
					if (fcdId != null && fichaDocente.getFcdcId().equals(fcdId)) {
						docente = fichaDocente.getPersona().getPrsNombres() + " "
								+ fichaDocente.getPersona().getPrsPrimerApellido() + " "
								+ fichaDocente.getPersona().getPrsSegundoApellido();
						break;
					}
				}
			}
		}
		return docente;
	}

	private ArrayList<String[]> getAsistenciaDocentes() {
		ArrayList<String[]> rows = new ArrayList<>();
		for (Asistencia asistencia : lstA) {
			rows.add(new String[] { getFormatoFecha(asistencia),
					asistencia.getHorarioAcademico().getMallaCurricularParalelo().getMallaCurricularMateria()
							.getMateria().getMtrDescripcion(),
					asistencia.getAssHoraEntrada(), asistencia.getAssHoraSalida(), asistencia.getAssEstado() });
		}
		return rows;
	}

	/**
	 * @param asistencia
	 * @return
	 */
	private String getFormatoFecha(Asistencia asistencia) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String strDate = dateFormat.format(asistencia.getAssFecha());
		return strDate;
	}

	public void verActividades() {
		if (beanLogin.adminFacultad) {
			// selectAss.setSeguimientos(srvSgm.getSeguimiento(selectAss.getHorarioAcademico().getMateria().getMtrId(),
			// fcdId));
		} else {
			// selectAss.setSeguimientos(srvSgm.getSeguimiento(selectAss.getHorarioAcademico().getMateria().getMtrId(),
			// beanLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId()));
		}

	}

	// setters and getters Docente

	public List<Asistencia> getLstA() {
		return lstA;
	}

	/**
	 * @return the selectAss
	 */
	public Asistencia getSelectAss() {
		return selectAss;
	}

	/**
	 * @param selectAss
	 *            the selectAss to set
	 */
	public void setSelectAss(Asistencia selectAss) {
		this.selectAss = selectAss;
	}

	public void setLstA(List<Asistencia> lstA) {
		this.lstA = lstA;
	}

	// setters and getters adminFacultad
	public List<Carrera> getLstCarreras() {
		return lstCarreras;
	}

	public void setLstCarreras(List<Carrera> lstCarreras) {
		this.lstCarreras = lstCarreras;
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
	 * @return the beanLogin
	 */
	public Login getBeanLogin() {
		return beanLogin;
	}

	/**
	 * @param beanLogin
	 *            the beanLogin to set
	 */
	public void setBeanLogin(Login beanLogin) {
		this.beanLogin = beanLogin;
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