package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Asistencia;
import ec.edu.uce.biometrico.jpa.Carrera;
import ec.edu.uce.biometrico.jpa.FichaDocente;
import ec.edu.uce.biometrico.jpa.HorarioAcademico;
import ec.edu.uce.biometrico.jpa.Persona;

@ManagedBean(name = "detalleAsistencia")
@ViewScoped
public class DetalleAsistencia {
	/**
	 * Constante definida para el formato de fecha.
	 */
	private static final String FORMATOFECHA = "yyyy-MM-dd";
	public static final String PATH_GENERAL_REPORTE = "/";
	public static final String PATH_GENERAL_IMG_PIE = "/img/plantillaPie.png";
	public static final String PATH_GENERAL_IMG_CABECERA = "/img/plantillaCabecera.png";
	public static final String GENERAL_NOMBRE_INSTITUCION = "UNIVERSIDAD \nCENTRAL \nDEL ECUADOR";
	public static final String GENERAL_PIE_PAGINA = "Copyright Universidad Central del Ecuador 2018";
	public static final String GENERAL_DOC_AUTOGENERADO = "Documento generado en siiu.uce.edu.ec el "
			+ cambiarDateToStringFormatoFecha(Date.from(Instant.now()), "dd/MM/yyyy HH:mm:ss");

	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	@EJB
	private SrvSeguimientoLocal srvSgm;
	
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

	private int rhefValidadorClic;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		if (beanLogin.adminFacultad) {
			mostrarFiltros = true;
			lstCarreras = srvEmp
					.listarCarrerasxFacultad(beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId());
		} else {
			mostrarFiltros = false;
		}
		templatePDF = new TemplatePDF();
	}

	public void iniciar() {
		selectAss = new Asistencia();
		selectCrr = new Carrera();
		selectDcn = new FichaDocente();
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
						lstA = srvDnt.listarAsistencia(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(),
								inicio, fin, null);
					} else {
						lstA = srvDnt.listarAsistencia(fcdId, inicio, fin, null);
						// lstA = srvDnt.listarAsistencia(fcdId, inicio, fin,
						// crrId);
					}

					if (!lstA.isEmpty()) {
						FacesUtil.mensajeInfo("Asistencias encontradas.");
					} else {
						FacesUtil.mensajeError("No existen asistencias registradas.");
					}
				}
			} catch (ParseException e) {
				FacesUtil.mensajeError("rror al convertir fechas para consulta");
			}
		}
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
			FacesUtil.mensajeError("La facultad y el docente son requeridos, verifique por favor.");
			limpiarFiltros();
			return false;

		}
	}

	public String regresar() {
		fechaInicio = null;
		fechaFin = null;
		lstCarreras = null;
		lstD = null;
		lstA = null;
		rhefValidadorClic = 0;
		crrId = null;
		fcdId = null;
		selectCrr = null;
		selectDcn = null;
		selectAss = null;
		return "principal";

	}

	/**
	 * Metodo que permite limpiar los filtros ingresados.
	 */
	public void limpiarFiltros() {
		desactivarModalReporte();
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
		if (asistencia != null && (asistencia.getAssHoraEntrada() == null || asistencia.getAssHoraSalida() == null)) {
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
			asistencia
					.setAssHoraEntrada(horario.getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() + ":00");
			asistencia.setAssHoraSalida(horario.getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin() + ":00");
		} else {
			if (asistencia.getAssHoraEntrada() == null) {
				asistencia.setAssHoraEntrada(
						horario.getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() + ":00");
				asistencia.setAssEstado("JUSTIFICACION HORA ENTRADA");
			}
			if (asistencia.getAssHoraSalida() == null) {
				asistencia
						.setAssHoraSalida(horario.getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin() + ":00");
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
				FacesUtil.mensajeError("La fecha inicio debe ser menor que la fecha fin, verifique por favor.");
				return false;
			}
		} else {
			FacesUtil.mensajeError("Fecha inicio y fecha fin requeridas, verifique por favor.");
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
			FacesUtil.mensajeError(
					"Archivo generado en la carpeta SistemaBiometricoUCE del su disco local C, verifique por favor.");
		} else {
			FacesUtil.mensajeError("No existen asistencias registradas.");
		}
	}

	private String getInfoDocente() {
		String docente = "";
		if (beanLogin.Docente) {
			docente = beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getPrsNombres() + " "
					+ beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getPrsPrimerApellido() + " "
					+ beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getPrsSegundoApellido();
		} else {
			if (!lstD.isEmpty()) {
				for (FichaDocente fichaDocente : lstD) {
					if (fcdId != null && fichaDocente.getFcdcId() == (int) fcdId) {
						docente = fichaDocente.getFcdcPersona().getPrsNombres() + " "
								+ fichaDocente.getFcdcPersona().getPrsPrimerApellido() + " "
								+ fichaDocente.getFcdcPersona().getPrsSegundoApellido();
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
					asistencia.getAssHorarioAcademico().getHracMallaCurricularParalelo()
							.getMlcrprMallaCurricularMateria().getMlcrmtMateria().getMtrDescripcion(),
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

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void activarModalReporte() {
		rhefValidadorClic = 1;
	}

	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void desactivarModalReporte() {
		rhefValidadorClic = 0;
	}

	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en
	 * pantalla del pdf
	 */
	public void verificarClickImprimir() {
		if (lstA != null && !lstA.isEmpty()) {
			activarModalReporte();
			generarReporteAsistenciaDocente(lstA);
		} else {
			FacesUtil.mensajeInfo("No existe registros de asistencias para generar el PDF.");
		}
	}

	/**
	 * Metodo que permite estructurar la informacion para el reporte de
	 * asistencia.
	 * 
	 * @param asistenciasDocente
	 */
	public void generarReporteAsistenciaDocente(List<Asistencia> asistenciasDocente) {
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRadParametros = null;
		String frmRrmNombreReporte = null;

		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "ASISTENCIA DOCENTE";

		// frmRadParametros = new HashMap<>();
		frmRadParametros = new HashMap<String, Object>();
		frmRadParametros.put("imagenCabecera", pathGeneralReportes + PATH_GENERAL_IMG_CABECERA);
		frmRadParametros.put("imagenPie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRadParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRadParametros.put("encabezado_reporte", "REPORTE ASISTENCIA DOCENTE");
		frmRadParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		// frmRadParametros.put("fecha_reporte",
		// getFechaFormato(GeneralesUtilidades.getPrimerDiaDelMesActual()) + " -
		// "
		// + getFechaFormato(GeneralesUtilidades.getUltimoDiaDelMesActual()));
		frmRadParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		// Persona persona =usuarioA.getUsrPersona();
		Persona persona = asistenciasDocente.get(0).getAssFichaDocente().getFcdcPersona();
		frmRadParametros.put("identificacion", persona.getPrsIdentificacion());
		frmRadParametros.put("facultad",
				asistenciasDocente.get(0).getAssHorarioAcademico().getHracMallaCurricularParalelo()
						.getMlcrprMallaCurricularMateria().getMlcrmtMateria().getMtrCarrera().getCrrDependencia()
						.getDpnDescripcion());
		frmRadParametros.put("nombres", getDatosDocente(persona));

		// frmRrmCampos = new ArrayList<>();
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataAsistencia = null;

		int contador = 0;
		for (Asistencia asistencia : asistenciasDocente) {
			String materia = asistencia.getAssHorarioAcademico().getHracMallaCurricularParalelo()
					.getMlcrprMallaCurricularMateria().getMlcrmtMateria().getMtrDescripcion();
			// dataAsistencia = new HashMap<>();
			dataAsistencia = new HashMap<String, Object>();
			dataAsistencia.put("item", String.valueOf(contador += 1));
			dataAsistencia.put("fechas", getFechaFormato(asistencia.getAssFecha()));
			dataAsistencia.put("materias", materia);
			dataAsistencia.put("horasEntrada", asistencia.getAssHoraEntrada());
			dataAsistencia.put("horasSalida", asistencia.getAssHoraSalida());
			dataAsistencia.put("estados", asistencia.getAssEstado());
			frmRrmCampos.add(dataAsistencia);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRadParametros);
	}

	/**
	 * Metodo que permite obtener los datos del docente.
	 * 
	 * @param persona
	 * @return
	 */
	private String getDatosDocente(Persona persona) {
		String result = "";
		if (persona != null) {
			result = persona.getPrsNombres() + " " + persona.getPrsPrimerApellido() + " "
					+ persona.getPrsSegundoApellido();
		}
		return result;
	}

	/**
	 * Metodo que permite obtener el formato de la fecha para el archivo
	 * adjunto.
	 * 
	 * @param assFecha
	 * @return
	 */
	private static Object getFechaFormato(Date assFecha) {
		String fechaProceso = "";
		if (assFecha != null) {
			DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			fechaProceso = formatoFecha.format(assFecha);
		}
		return fechaProceso;
	}

	/**
	 * Método que permite convertir un objeto Date a un String, el formato de
	 * salida es el parametro que se ingrese.
	 * 
	 * @param date
	 * @param formato
	 * @return
	 */
	public static String cambiarDateToStringFormatoFecha(Date date, String formato) {
		SimpleDateFormat formateador = new SimpleDateFormat(formato, new Locale("es", "EC"));
		return formateador.format(date);
	}

	/**
	 * The templatePDF to get.
	 * 
	 * @return the templatePDF
	 */
	public TemplatePDF getTemplatePDF() {
		return templatePDF;
	}

	/**
	 * The templatePDF to set.
	 * 
	 * @param templatePDF
	 */
	public void setTemplatePDF(TemplatePDF templatePDF) {
		this.templatePDF = templatePDF;
	}

	/**
	 * The rhefValidadorClic to get.
	 * 
	 * @return the rhefValidadorClic
	 */
	public Integer getRhefValidadorClic() {
		return rhefValidadorClic;
	}

	/**
	 * The rhefValidadorClic to set.
	 * 
	 * @param rhefValidadorClic
	 */
	public void setRhefValidadorClic(Integer rhefValidadorClic) {
		this.rhefValidadorClic = rhefValidadorClic;
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

	public boolean isMostrarFiltros() {
		return mostrarFiltros;
	}

	public void setMostrarFiltros(boolean mostrarFiltros) {
		this.mostrarFiltros = mostrarFiltros;
	}

}
