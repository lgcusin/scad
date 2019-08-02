/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     NotasInformaticaForm.java	  
 DESCRIPCION: Bean de sesion que maneja las notas de los estudiantes por docente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-ABRIL-2019 			Freddy Guzmán                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSuficienciaInformaticaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.IdentifidorCliente;

/**
 * Clase (managed bean) NotasInformaticaForm. Managed Bean que administra los
 * estudiantes para el ingreso de las notas por docentes.
 * 
 * @author fgguzman.
 * @version 1.0
 */

@SessionScoped
@ManagedBean(name = "notasInformaticaForm")
public class NotasInformaticaForm extends HttpServlet implements Serializable , HttpSessionBindingListener, HttpSessionListener  {

	private static final long serialVersionUID = -3658624364967177082L;
	private final int NOTA_MINIMA_APROBACION_INFORMATICA = 7;
	private final String ASISTENCIA_MINIMA_APROBACION_INFORMATICA = "80";
	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	private Usuario nifUsuario;

	private Integer nifCarreraId;
	private Integer nifAsignaturaId;
	private Integer nifAsistenciaGeneral;
	private Integer nifActivarReporte;
	private Integer nifOpcionId;
	private String nifTipoArchivo;
	private String nifNombreJasper;
	private String nifTokenServlet;
	private Boolean nifRenderAsistencia;
	
	private ParaleloDto nifParaleloDto;	

	private List<Carrera> nifListCarrera;
	private List<MateriaDto> nifListMateriaDto;
	private List<ParaleloDto> nifListParaleloDto;
	private List<PersonaDto> nifListPersonaDto;
	private List<SelectItem> nifListOpcionReporte;

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB	private CalificacionServicio servCalificacion;
	@EJB	private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB	private CarreraServicio servCarrera;
	@EJB	private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB	private ParaleloDtoServicioJdbc servJdbcParaleloDto;
	@EJB 	private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB	private CronogramaActividadDtoServicioJdbc servJdbcCronogramaActividadDto;
	
	// ****************************************************************/
	// **************** METODOS DE NAVEGACIÓN  ************************/
	// ****************************************************************/
	
	public void sessionCreated(HttpSessionEvent se) {
        // increase online users by 1
//		ServletContext contexto = se.getSession().getServletContext();
//		System.out.println("NUEVA SESSION" + se.getSession().getId());
//		contexto.log("" + (new java.util.Date()));
    }
 
 
    public void sessionDestroyed(HttpSessionEvent se) {
//		ServletContext contexto = se.getSession().getServletContext();
//		contexto.log("" + (new java.util.Date()));
//        // decrease online users by 1
//    	System.out.println("CERRAR SESSION" + se.getSession().getId());
    }

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
//		ServletContext contexto = event.getSession().getServletContext();
//		synchronized (contexto){
//			contexto.log("" + (new java.util.Date()) + " Binding " + event.getName() + " to session "  + event.getSession().getId());
//			Enumeration<String> e = event.getSession().getAttributeNames();
//			while(e.hasMoreElements()){
//				String param = (String) e.nextElement();
//				System.out.println(param + " : " + e.nextElement());
//			}

//		}
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
//		ServletContext contexto = event.getSession().getServletContext();
//		synchronized (contexto){
//			contexto.log("" + (new java.util.Date()) + " Unbinding " + event.getName() + " from session "
//			        + event.getSession().getId());
//
//		}
	}
	
	public String irParalelos(Usuario usuario) {
		nifUsuario = usuario;
		
		nifListCarrera = cargarCarreraSuficiencia();
		nifListMateriaDto = cargarAsignaturaSuficiencia();
		
		nifCarreraId = GeneralesConstantes.APP_ID_BASE;
		nifAsignaturaId = GeneralesConstantes.APP_ID_BASE;
		nifOpcionId = GeneralesConstantes.APP_ID_BASE;
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		nifListOpcionReporte = cargarOpcionesImpresion();
		
		nifListParaleloDto = null;
		nifTipoArchivo = null;
		nifNombreJasper = null;
		nifTokenServlet = null;
		
		return "irParalelosSuficienciaInformatica";
	}

	

	public String irInicio() {
		nifCarreraId = null;
		nifAsignaturaId = null;
		nifAsistenciaGeneral = null;
		nifActivarReporte = null;
		nifTipoArchivo = null;
		nifNombreJasper = null;
		nifTokenServlet = null;
		nifRenderAsistencia = null;
		
		nifParaleloDto = null;	

		nifListCarrera = null;
		nifListMateriaDto = null;
		nifListParaleloDto = null;
		nifListPersonaDto = null;
		return "irInicio";
	}
	
	public String irEstudiantes(ParaleloDto  paralelo) {
		String retorno = null; 
		nifActivarReporte = 0;
		nifParaleloDto = paralelo;
		nifAsistenciaGeneral = null;
		nifRenderAsistencia = Boolean.FALSE;
		
		if (verificarPlanificacionPasoNotas(paralelo)) {
			nifListPersonaDto = cargarEstudiantesPorParalelo(paralelo);
			if (!nifListPersonaDto.isEmpty()) {
				
				boolean asistencia = false;
				for (PersonaDto item : nifListPersonaDto) {
					if (!asistencia) {
						if (item.getPrsCalificacionDto().getClfAsistenciaDocente1() != null) {
							nifAsistenciaGeneral = item.getPrsCalificacionDto().getClfAsistenciaDocente1().intValue();
							asistencia = true;
						}	
					}
					
				}
				
				nifRenderAsistencia = asistencia;
				
				retorno = "irCalificacionesSuficienciaInformatica";
				
			}else {
				FacesUtil.mensajeInfo("No se encontró estudiantes matriculados.");
			}
		}else {
			FacesUtil.mensajeInfo("No se encuentra en fechas para el paso de notas.");
		}
		
		return retorno;
	}
	
	private boolean verificarPlanificacionPasoNotas(ParaleloDto paralelo) {
		boolean retorno = false;
		
		CronogramaActividadJdbcDto cronograma = null;
		try {
			cronograma = servJdbcCronogramaActividadDto.buscarCronogramaPorProcesoPeriodo(ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE, paralelo.getPrlPeriodoAcademicoDto().getPracId());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró cronograma activo para el paso de notas.");
		}
		
		if (cronograma != null) {
			if (GeneralesUtilidades.getFechaActualSistemaTimestamp().after(cronograma.getPlcrFechaInicio())
					&& GeneralesUtilidades.getFechaActualSistemaTimestamp().before(cronograma.getPlcrFechaFin())) {
				retorno = true;
			}
		}
		
		return retorno;
	}



	public String irFormParalelos() {
		nifActivarReporte = 0;

		nifCarreraId = GeneralesConstantes.APP_ID_BASE;
		nifAsignaturaId = GeneralesConstantes.APP_ID_BASE;
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		nifTipoArchivo = null;
		nifNombreJasper = null;
		nifTokenServlet = null;
		nifListParaleloDto = null;
		nifAsistenciaGeneral = null;
		nifRenderAsistencia = null;
		
		nifParaleloDto = null;	
		nifListParaleloDto = null;
		nifListPersonaDto = null;
		return "irFormParalelosSuficienciaInformatica";
	}
	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/
	
	public void asignarParalelo(ParaleloDto paralelo){
		nifParaleloDto = paralelo;
		cancelarGenerarReportes();
		nifTipoArchivo = null;
		nifNombreJasper = null;
		nifTokenServlet = null;
	}
	
	public void generarReportes(){
		if (!nifOpcionId.equals(GeneralesConstantes.APP_ID_BASE)) {
			switch (nifOpcionId) {
			case 1:
				reporteMatriculados(nifParaleloDto);
				break;
			case 2:
				reporteTemplatePruebas(nifParaleloDto);
				break;
			}
		}else {
			cancelarGenerarReportes();
			FacesUtil.mensajeInfo("Seleccione una opcion para generar el reporte");
		}
	}
	
	public void cancelarGenerarReportes(){
		nifOpcionId = GeneralesConstantes.APP_ID_BASE;
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
	}
	
	
	public void actualizarAsistenciaGeneral(){
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		for (PersonaDto estudiante : nifListPersonaDto) {
			if (!estudiante.getPrsRecordEstudianteDto().getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE)) {
				if (estudiante.getPrsCalificacionDto().getClfNota1() != null && estudiante.getPrsCalificacionDto().getClfAsistencia1() != null) {

					estudiante.getPrsCalificacionDto().setClfNota1(estudiante.getPrsCalificacionDto().getClfNota1());
					estudiante.getPrsCalificacionDto().setClfSumaNotas(estudiante.getPrsCalificacionDto().getClfNota1());
					estudiante.getPrsCalificacionDto().setClfNotaFinalSemestre(estudiante.getPrsCalificacionDto().getClfNota1());
					
					estudiante.getPrsCalificacionDto().setClfAsistencia1(new BigDecimal("0"));
					estudiante.getPrsCalificacionDto().setClfAsistenciaTotal(new BigDecimal("0"));

					estudiante.getPrsCalificacionDto().setClfAsistenciaDocente1(new BigDecimal(nifAsistenciaGeneral.toString()));
					estudiante.getPrsCalificacionDto().setClfAsistenciaTotalDocente(new BigDecimal(nifAsistenciaGeneral.toString()));
					
					estudiante.getPrsCalificacionDto().setClfPorcentajeAsistencia(new BigDecimal("0"));
					
					estudiante.getPrsRecordEstudianteDto().setRcesEstado(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE);
					servCalificacion.registrarCalificacion(estudiante, "IP-ADDRESS: buscar los medios");
				}
			}
		}
		
		FacesUtil.mensajeError("Es necesario volver a ingresar las asistencias para recalcular estados.");

	}
	
	public void registrarAsistenciaGeneral(){
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		if (nifAsistenciaGeneral != null) {
			nifRenderAsistencia = Boolean.TRUE;
			FacesUtil.mensajeInfo("Asistencia general registrada con éxito. Continue ingresando las calificaciones.");
		}else {
			FacesUtil.mensajeInfo("Es necesario ingresar la asistencia general para continuar.");
		}
	}
	
	
	private void reporteMatriculados(ParaleloDto paralelo){
		nifListPersonaDto = cargarEstudiantesPorParalelo(paralelo);
		if (!nifListPersonaDto.isEmpty()) {

			nifTipoArchivo = "PDF";
			nifNombreJasper = "asistencias";
			nifTokenServlet = "SUF_INF_ASISTENCIA_INTENSIVO_REGULAR";
			nifActivarReporte = 1;
			
			PersonaDto director = cargarDirectorCarrera();
			PersonaDto docente = cargarDatosDocente();
			
			
			
			if (paralelo.getPrlInicioClase() == null) {
				CronogramaActividadJdbcDto cronograma = cargarCronogramaInicioClase(paralelo);
				if (cronograma != null) {
					paralelo.setPrlInicioClase(cronograma.getPlcrFechaInicio());
					paralelo.setPrlFinClase(cronograma.getPlcrFechaFin());
				}
			}
			
			ReporteSuficienciaInformaticaForm.generarEstudiantesMatriculados(nifUsuario, director, docente, nifListPersonaDto, paralelo);	
		}else {
			nifActivarReporte = 0;
			FacesUtil.mensajeInfo("No hay datos para mostrar., inténtelo mas tarde");
		} 		
	}
	
	private void reporteTemplatePruebas(ParaleloDto paralelo){
		nifListPersonaDto = cargarEstudiantesPorParalelo(paralelo);
		if (!nifListPersonaDto.isEmpty()) {

			nifTipoArchivo = "PDF";
			nifNombreJasper = "pruebas";
			nifTokenServlet = "SUF_INF_PRUEBAS_INT_REG";
			nifActivarReporte = 1;
			
			PersonaDto docente = cargarDatosDocente();
			
			if (paralelo.getPrlInicioClase() == null) {
				CronogramaActividadJdbcDto cronograma = cargarCronogramaInicioClase(paralelo);
				if (cronograma != null) {
					paralelo.setPrlInicioClase(cronograma.getPlcrFechaInicio());
					paralelo.setPrlFinClase(cronograma.getPlcrFechaFin());
				}
			}
			
			ReporteSuficienciaInformaticaForm.generarTemplatePruebas(nifUsuario, docente, nifListPersonaDto, paralelo);	
		}else {
			nifActivarReporte = 0;
			FacesUtil.mensajeInfo("No hay datos para mostrar, inténtelo mas tarde");
		} 		
	}
	
	public void reporteCalificaciones(){
		nifListPersonaDto = cargarEstudiantesPorParalelo(nifParaleloDto);
		if (!nifListPersonaDto.isEmpty()) {

			nifTipoArchivo = "PDF";
			nifNombreJasper = "calificaciones";
			nifTokenServlet = "SUF_INF_CALIFICACIONES_INT_REG";
			nifActivarReporte = 1;
			
			PersonaDto docente = cargarDatosDocente();
			
			if (nifParaleloDto.getPrlInicioClase() == null) {
				CronogramaActividadJdbcDto cronograma = cargarCronogramaInicioClase(nifParaleloDto);
				if (cronograma != null) {
					nifParaleloDto.setPrlInicioClase(cronograma.getPlcrFechaInicio());
					nifParaleloDto.setPrlFinClase(cronograma.getPlcrFechaFin());
				}
			}
			List<PersonaDto> personas = new ArrayList<>();
			personas.addAll(nifListPersonaDto);
			Iterator<PersonaDto> iter = personas.iterator();
			while (iter.hasNext()) {
				PersonaDto item = (PersonaDto) iter.next();
				if (item.getPrsRecordEstudianteDto().getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE)
						|| item.getPrsRecordEstudianteDto().getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE)) {
					iter.remove();
				}
			}
			
			ReporteSuficienciaInformaticaForm.generarCalificacionEstudiantes(nifUsuario, docente, personas, nifParaleloDto);	
		}else {
			nifActivarReporte = 0;
			FacesUtil.mensajeInfo("No hay datos para mostrar, inténtelo mas tarde");
		} 		
	}
	
	private CronogramaActividadJdbcDto cargarCronogramaInicioClase(ParaleloDto paralelo) {
		CronogramaActividadJdbcDto retorno = null;
		
		try {
			retorno = servJdbcCronogramaActividadDto.buscarCronogramaPorProcesoPeriodo(ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE, paralelo.getPrlPeriodoAcademicoDto().getPracId());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	
	/**
	 * APROBADOS: 
	 *  - si nota es mayor o igual a 7
	 *  - si asistencias es mayor o igual a 80
	 */
	public void guardarCalificacion(PersonaDto estudiante){
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		if (nifAsistenciaGeneral != null && nifAsistenciaGeneral > Integer.valueOf(0)) {
			if (estudiante.getPrsCalificacionDto().getClfNota1() != null && estudiante.getPrsCalificacionDto().getClfAsistencia1() != null) {
				
				boolean asistencia = false;
				if (estudiante.getPrsCalificacionDto().getClfAsistencia1().floatValue() >= calcularLimiteAsistencia(Long.valueOf(nifAsistenciaGeneral)).floatValue()) {
					asistencia = true;
				}
				
				boolean calificacion = false;
				if (estudiante.getPrsCalificacionDto().getClfNota1().intValue() >= NOTA_MINIMA_APROBACION_INFORMATICA) {
					calificacion = true;
				}
				
				if (!asistencia || !calificacion) {
					FacesUtil.mensajeInfo("Recuerde que sino ingresa la calificación y la asistencia no se guarda el registro.");
				}
				
				
				estudiante.getPrsCalificacionDto().setClfSumaNotas(estudiante.getPrsCalificacionDto().getClfNota1());
				estudiante.getPrsCalificacionDto().setClfNotaFinalSemestre(estudiante.getPrsCalificacionDto().getClfNota1());
				estudiante.getPrsCalificacionDto().setClfAsistenciaTotal(estudiante.getPrsCalificacionDto().getClfAsistencia1());				
				
				estudiante.getPrsCalificacionDto().setClfAsistenciaDocente1(new BigDecimal(nifAsistenciaGeneral.toString()));
				estudiante.getPrsCalificacionDto().setClfAsistenciaTotalDocente(new BigDecimal(nifAsistenciaGeneral.toString()));
				
				estudiante.getPrsCalificacionDto().setClfPorcentajeAsistencia(calcularPorcentajeAsistencia(estudiante.getPrsCalificacionDto().getClfAsistencia1().longValue() , Long.valueOf(nifAsistenciaGeneral)));
				
				if (asistencia && calificacion) {
					estudiante.getPrsRecordEstudianteDto().setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				}else {
					estudiante.getPrsRecordEstudianteDto().setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				}
				
				if (servCalificacion.registrarCalificacion(estudiante, IdentifidorCliente.obtenerDatosCliente().toString())) {
					FacesUtil.mensajeInfo(estudiante.getPrsApellidosNombres() + " - Notas registradas con éxito.");
				}else {
					FacesUtil.mensajeError(estudiante.getPrsApellidosNombres() + " - Error al registrar las notas.");
				}
			}else {
				FacesUtil.mensajeInfo("Recuerde que sino ingresa la calificación y la asistencia no se guarda el registro.");
			}
		}else {
			estudiante.getPrsCalificacionDto().setClfNota1(null);
			estudiante.getPrsCalificacionDto().setClfAsistencia1(null);
			FacesUtil.mensajeError("Ingrese la asistencia general para continuar con el registro de calificaciones.");
		}
		
	}
	
	/**
	 * Método que permite calcular el porcentaje de asistencia del estudiante.
	 */
	private BigDecimal calcularPorcentajeAsistencia(Long asistenciaEstudiante, Long asistenciaGeneral) {
		BigDecimal porcentajeAsistencia = BigDecimal.ZERO;
		porcentajeAsistencia = new BigDecimal(asistenciaEstudiante , new MathContext(6)).multiply(new BigDecimal("100"));
		return establecerNumeroSinAproximacion(porcentajeAsistencia.divide(new BigDecimal(asistenciaGeneral , new MathContext(6)),new MathContext(6)),2);
	}
	
	/**
	 * Método que permite calcular el limite de asistencias para aprobar el curso.
	 * @param asistenciaGeneral - horas por semana total
	 * @return 80% de asistencias.
	 */
	private BigDecimal calcularLimiteAsistencia(Long asistenciaGeneral) {
		MathContext mc = new MathContext(6);
		BigDecimal divisor = new BigDecimal("100");
		BigDecimal constante = new BigDecimal(ASISTENCIA_MINIMA_APROBACION_INFORMATICA);
		constante = constante.multiply(new BigDecimal(asistenciaGeneral , mc));
		return establecerNumeroSinAproximacion(constante.divide(divisor,mc),2);
	}
	
	/**
	 * Método que permite setear dos cifras despues del punto decimal, sin incremetar al inmediato superior.
	 * @param numero - BigDEcimal mayor a 0.00
	 * @param digitos - cantidad de decimales requeridos.
	 * @return Bigdecimal con Formato ##.##
	 */
	public static BigDecimal establecerNumeroSinAproximacion(BigDecimal numero, int digitos){
		BigInteger entero = BigInteger.ZERO;BigInteger decimal = BigInteger.ZERO;
		MathContext mc = new MathContext(6);
		
		entero = numero.toBigInteger();
		BigDecimal decimas = numero.remainder(BigDecimal.ONE, mc);
		decimal = decimas.multiply(BigDecimal.TEN.pow(digitos)).toBigInteger();
		
		return new BigDecimal(entero+"."+setearMascara(decimal));
	}
	
	/**
	 * Método que permite dar mascara al decimal obtenido.
	 * @param number - entero tipo BigInteger
	 * @return entero con mascara ##
	 */
	private static String setearMascara(BigInteger number) {
		return String.format("%02d", number);
	}
	
	
	public void buscarParalelos(){
		if (!nifCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!nifAsignaturaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				nifListParaleloDto = cargarParalelosPorCarreraAsignatura();
			}else {
				FacesUtil.mensajeInfo("Seleccione una asignatura para continuar.");
			}
		}else {
			FacesUtil.mensajeInfo("Seleccione una carrera para continuar.");
		}
	}
	
	
	public void limpiarFormParalelos(){
		nifCarreraId = GeneralesConstantes.APP_ID_BASE;
		nifAsignaturaId = GeneralesConstantes.APP_ID_BASE;
		nifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		nifTipoArchivo = null;
		nifNombreJasper = null;
		nifTokenServlet = null;
		nifListParaleloDto = null;
	}
	
	private List<Carrera> cargarCarreraSuficiencia() {
		List<Carrera> retorno = new ArrayList<>();
		
		try {
			retorno = servCarrera.buscarCarreras(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE, CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<MateriaDto> cargarAsignaturaSuficiencia() {
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcMateriaDto.listarMateriasxCarreraFull(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<ParaleloDto> cargarParalelosPorCarreraAsignatura(){
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelosPorDocente(nifCarreraId, nifAsignaturaId, nifUsuario.getUsrIdentificacion());
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<PersonaDto> cargarEstudiantesPorParalelo(ParaleloDto paralelo){
		List<PersonaDto> retorno = new ArrayList<>();

		try {
			retorno = servJdbcPersonaDto.buscarEstudiantesPorAsignaturaParalelo(paralelo.getMlcrprId());
			if (!retorno.isEmpty()) {
				for (PersonaDto item : retorno) {
					if (item.getPrsRecordEstudianteDto().getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE) ||
							item.getPrsRecordEstudianteDto().getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE)) {
							item.getPrsCalificacionDto().setClfIsDisable(Boolean.TRUE);
					}
				}
			}
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	/**
	 * Mètodo que permite cargar datos del director de carrera
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarDirectorCarrera(){
		
		PersonaDto director  = null;
		
		try {
			director = servJdbcPersonaDto.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE);
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró Director de Carrera.");
		}
		
		return director;
	}

	private PersonaDto cargarDatosDocente(){
		PersonaDto docente  = new PersonaDto();
		docente.setPrsIdentificacion(nifUsuario.getUsrIdentificacion());
		docente.setPrsPrimerApellido(nifUsuario.getUsrPersona().getPrsPrimerApellido());
		docente.setPrsSegundoApellido(nifUsuario.getUsrPersona().getPrsSegundoApellido());
		docente.setPrsNombres(nifUsuario.getUsrPersona().getPrsNombres());
		docente.setPrsSexo(nifUsuario.getUsrPersona().getPrsSexo());
		return docente;
	}
	
	private List<SelectItem> cargarOpcionesImpresion() {
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(new Integer(1), new String("ASISTENCIA")));
		retorno.add(new SelectItem(new Integer(2), new String("PRUEBAS")));
		return retorno;
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getNifUsuario() {
		return nifUsuario;
	}

	public void setNifUsuario(Usuario nifUsuario) {
		this.nifUsuario = nifUsuario;
	}

	public Integer getNifCarreraId() {
		return nifCarreraId;
	}

	public void setNifCarreraId(Integer nifCarreraId) {
		this.nifCarreraId = nifCarreraId;
	}

	public Integer getNifAsignaturaId() {
		return nifAsignaturaId;
	}

	public void setNifAsignaturaId(Integer nifAsignaturaId) {
		this.nifAsignaturaId = nifAsignaturaId;
	}

	public Integer getNifAsistenciaGeneral() {
		return nifAsistenciaGeneral;
	}

	public void setNifAsistenciaGeneral(Integer nifAsistenciaGeneral) {
		this.nifAsistenciaGeneral = nifAsistenciaGeneral;
	}

	public List<MateriaDto> getNifListMateriaDto() {
		return nifListMateriaDto;
	}

	public void setNifListMateriaDto(List<MateriaDto> nifListMateriaDto) {
		this.nifListMateriaDto = nifListMateriaDto;
	}

	public List<PersonaDto> getNifListPersonaDto() {
		return nifListPersonaDto;
	}

	public void setNifListPersonaDto(List<PersonaDto> nifListPersonaDto) {
		this.nifListPersonaDto = nifListPersonaDto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Carrera> getNifListCarrera() {
		return nifListCarrera;
	}

	public void setNifListCarrera(List<Carrera> nifListCarrera) {
		this.nifListCarrera = nifListCarrera;
	}

	public List<ParaleloDto> getNifListParaleloDto() {
		return nifListParaleloDto;
	}

	public void setNifListParaleloDto(List<ParaleloDto> nifListParaleloDto) {
		this.nifListParaleloDto = nifListParaleloDto;
	}

	public ParaleloDto getNifParaleloDto() {
		return nifParaleloDto;
	}

	public void setNifParaleloDto(ParaleloDto nifParaleloDto) {
		this.nifParaleloDto = nifParaleloDto;
	}

	public String getNifTipoArchivo() {
		return nifTipoArchivo;
	}

	public void setNifTipoArchivo(String nifTipoArchivo) {
		this.nifTipoArchivo = nifTipoArchivo;
	}

	public String getNifNombreJasper() {
		return nifNombreJasper;
	}

	public void setNifNombreJasper(String nifNombreJasper) {
		this.nifNombreJasper = nifNombreJasper;
	}

	public String getNifTokenServlet() {
		return nifTokenServlet;
	}

	public void setNifTokenServlet(String nifTokenServlet) {
		this.nifTokenServlet = nifTokenServlet;
	}

	public Integer getNifActivarReporte() {
		return nifActivarReporte;
	}

	public void setNifActivarReporte(Integer nifActivarReporte) {
		this.nifActivarReporte = nifActivarReporte;
	}

	public Boolean getNifRenderAsistencia() {
		return nifRenderAsistencia;
	}

	public void setNifRenderAsistencia(Boolean nifRenderAsistencia) {
		this.nifRenderAsistencia = nifRenderAsistencia;
	}

	public List<SelectItem> getNifListOpcionReporte() {
		return nifListOpcionReporte;
	}

	public void setNifListOpcionReporte(List<SelectItem> nifListOpcionReporte) {
		this.nifListOpcionReporte = nifListOpcionReporte;
	}

	public Integer getNifOpcionId() {
		return nifOpcionId;
	}

	public void setNifOpcionId(Integer nifOpcionId) {
		this.nifOpcionId = nifOpcionId;
	}

	

	
	
}
