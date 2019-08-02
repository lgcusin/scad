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
   
 ARCHIVO:     EstudianteRecordAcademicoForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 08-SEPT-2017 			Vinicio Rosales                     Emisión Inicial
 17-JUL-2018 			Freddy Guzmán						Integracion SAU
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.certificado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularNivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularNivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteRecordAcademicoForm. Managed Bean que
 * administra los estudiantes para la visualización de los certificados de
 * notas.
 * @author fgguzman v1.
 * @version 1.0
 */

@ManagedBean(name = "estudianteRecordAcademicoForm")
@SessionScoped
public class EstudianteRecordAcademicoForm extends HistorialAcademicoForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;
	public static final String PATH_GENERAL_REPORTE = "/academico/reportes/";
	public static final String PATH_GENERAL_IMG_PIE = "/academico/reportes/imagenes/plantillaPie.png";
	public static final String PATH_GENERAL_IMG_CABECERA = "/academico/reportes/imagenes/plantillaCabecera.png";
	public static final String GENERAL_NOMBRE_INSTITUCION = "UNIVERSIDAD \nCENTRAL \nDEL ECUADOR";
	public static final String GENERAL_TITULO_REPORTE_RECORD_ACADEMICO = "RÉCORD ACADÉMICO";
	public static final String GENERAL_TITULO_REPORTE_HISTORIAL_ACADEMICO = "HISTORIAL ACADÉMICO";
	public static final String GENERAL_PIE_PAGINA = "Copyright Universidad Central del Ecuador 2018";
	public static final String GENERAL_DOC_AUTOGENERADO = "Documento generado en siiu.uce.edu.ec el " +  GeneralesUtilidades.cambiarDateToStringFormatoFecha(Date.from(Instant.now()), "dd/MM/yyyy HH:mm:ss");
	
	
	public static final int PERIODO_SAU_NOTAS_SOBRE_DIEZ = 27;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	private Usuario erafUsuario;
	
	private String erafPrimerApellido;
	private String erafIdentificacion;
	private String erafTituloPorNivelCarrera;
	private String erafTituloPorNivelPeriodo;
	
	private Integer erafCrrId;
	private Integer erafTipoUsuario;
	private Integer erafTipoBusqueda;
	private Integer erafOpcionReporteId;

	private BigDecimal erafPromedioGeneral;
	private BigDecimal erafPromedioInternado;
	private BigDecimal erafPorcentajeMallaAprobado;
	
	private Boolean erafAcceso;
	private Boolean erafAccesoCertificado;
	
	private EstudianteJdbcDto erafPersona;
	private CarreraDto erafCarreraDto;
	private RecordEstudianteDto erafPeriodoAcademicoDto;
	
	private List<EstudianteJdbcDto> erafListPersona;
	private List<CarreraDto> erafListCarreraDto;
	private List<CarreraDto> erafListCarreraDtoSecretaria;
	private List<RecordEstudianteDto> erafListRecordEstudianteDto;
	private List<RecordEstudianteDto> erafListRecordEstudianteDtoSeleccion;
	private List<RecordEstudianteDto> erafListRecordEstudianteDtoPeriodoDto;
	private List<RecordEstudianteDto> erafListRecordEstudianteDtoMateriaDto;
	private List<PeriodoAcademicoDto> erafListPeriodoAcademicoDto;	
	private List<SelectItem> erafListOpcionReporte;

	private int ecnfActivarReporte;
	
	private String erafNombreReporte;
	private String erafNombreArchivo;
	
	private String[] erafParametros;
	
	//Reporte suficiencias.
	private String erafTipoArchivo;
	private String erafTokenServlet;
	private String erafNombreJasper;
	private Integer erafPeriodoId;
	private Integer erafDependenciaId;
	private Integer erafActivarReporte;
	
	private List<PeriodoAcademico> erafListPeriodoAcademico;
	private List<Dependencia> erafListDependencia;
	private List<PersonaDto> erafListPersonaDto;
	private List<Map<String, Object>>  erafMapReporteGeneral ;
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB private MatriculaServicioJdbc servJdbcErafMatricula;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private PersonaServicio servPersona;
	@EJB private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB private CarreraServicio servCarrera;
	@EJB private UsuarioRolServicio servUsuarioRolServicio;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private MallaCurricularNivelServicio servMallaCurricularNivel;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private DependenciaServicio servDependencia;
	
	// ****************************************************************/
	// *************** REPORTE APROBACION SUFICIENCIAS ****************/
	// ****************************************************************/
	
	public String irReporteSuficiencias(Usuario usuario) {
		String retorno = "";
		
		erafPeriodoId = GeneralesConstantes.APP_ID_BASE;
		erafDependenciaId = GeneralesConstantes.APP_ID_BASE;
		erafActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		erafListPersonaDto = new ArrayList<>();
		erafMapReporteGeneral = new ArrayList<>();
		
		erafTipoArchivo = new String("");
		erafTokenServlet = new String("");
		erafNombreJasper = new String("");
		
		try {
			erafListPeriodoAcademico = servPeriodoAcademico.buscarPeriodos(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, Arrays.asList(new String[]{String.valueOf(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE), String.valueOf(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE)}));
			erafListDependencia = servDependencia.buscarDependencias(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
			retorno = "irFormSuficiencias";
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	public String irPrincipal() {
		erafPeriodoId = null;
		erafDependenciaId = null;
		erafActivarReporte = null;
		
		erafListPersonaDto = null;
		erafListPeriodoAcademico = null;
		erafListDependencia = null;
		erafMapReporteGeneral = null;
		
		erafTipoArchivo = null;
		erafTokenServlet = null;
		erafNombreJasper = null;
		return "irInicio";
	}

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/
	public String irInicio() {
		limpiarFormEstudiantes();
		limpiarFormRecordAcademico();
		erafAcceso = Boolean.FALSE;
		erafAccesoCertificado = Boolean.FALSE;
		ecnfActivarReporte = 0;
		erafUsuario = null;
		erafPrimerApellido = null;
		erafIdentificacion = null;
		erafCrrId= null;
		erafTipoUsuario= null;
		erafTipoBusqueda= null;
		erafPromedioGeneral= null;
		erafAcceso= null;
		erafPersona= null;
		erafListPersona= null;
		erafListCarreraDto= null;
		erafListCarreraDtoSecretaria= null;
		erafListRecordEstudianteDto= null;
		erafListRecordEstudianteDtoSeleccion= null;
		erafListRecordEstudianteDtoPeriodoDto= null;
		erafListRecordEstudianteDtoMateriaDto= null;
		erafListPeriodoAcademicoDto= null;
		return "irInicio";
	}
	

	public String irRecordAcademicoCarreras() {
		ecnfActivarReporte = 0;
		return "irCarrerasEstudiante";
	}
	
	public String irPeriodosEstudiante(){
		
		return "irPeriodosEstudiante";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiante(Usuario usuario, int tipoRol) {
		String retorno = null;

		erafUsuario = usuario;
		ecnfActivarReporte = 0;
		erafPrimerApellido = new String();
		erafIdentificacion = new String();
		erafTituloPorNivelCarrera = new String("Carrera");
		erafTituloPorNivelPeriodo = new String("Período académico");

		erafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		erafOpcionReporteId = GeneralesConstantes.APP_ID_BASE;
		
		erafAcceso = Boolean.FALSE;
		erafAccesoCertificado = Boolean.FALSE;

		erafTipoUsuario = tipoRol;
		erafListPersona = null;

		if (RolConstantes.ROL_CONSULTOREPORTES_VALUE.equals(tipoRol)) {
			
			try {
				List<CarreraDto> carreras = servJdbcCarreraDto.listarCarrerasTodas();
				if (!carreras.isEmpty()) {
					erafListCarreraDtoSecretaria = carreras;
					erafListOpcionReporte = cargarOpcionesReportePregrado();
					retorno = "irListarEstudiantes";
				}
			} catch (CarreraDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		//impresion de reportes
		}else if (RolConstantes.ROL_SECRECARRERA_VALUE.equals(tipoRol)) {
			List<CarreraDto> carreras = new ArrayList<>();
			
			try {
				
				List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE);
				for(RolFlujoCarrera rlflcr: rolflujocarrera){
					Carrera carrera = servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId());
					carreras.add(new CarreraDto(carrera.getCrrId(), carrera.getCrrDescripcion()));
				}
				
				if (!carreras.isEmpty()) {
					erafListCarreraDtoSecretaria = carreras;
					
					for (CarreraDto it : carreras) {
						if (it.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE) {
							erafAccesoCertificado = Boolean.TRUE;
						}
					}
					
				}

				erafListOpcionReporte = cargarOpcionesReportePregrado();
				
				retorno = "irListarEstudiantes";	
			} catch (RolFlujoCarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (RolFlujoCarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
			} catch (CarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else if (RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.equals(tipoRol)) {

			List<CarreraDto> carreras = new ArrayList<>();

			try {

				List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE);
				for(RolFlujoCarrera rlflcr: rolflujocarrera){
					Carrera carrera = servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId());
					carreras.add(new CarreraDto(carrera.getCrrId(), carrera.getCrrDescripcion()));
				}

				if (!carreras.isEmpty()) {
					erafListCarreraDtoSecretaria = carreras;
				}

				erafListOpcionReporte = cargarOpcionesReporteGenerico();

				retorno = "irListarEstudiantes";	
			} catch (RolFlujoCarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (RolFlujoCarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
			} catch (CarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

		}else if (RolConstantes.ROL_SECRENIVELACION_VALUE.equals(tipoRol)) {

			List<CarreraDto> carreras = new ArrayList<>();

			try {

				List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_SECRENIVELACION_VALUE);
				for(RolFlujoCarrera rlflcr: rolflujocarrera){
					Carrera carrera = servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId());
					carreras.add(new CarreraDto(carrera.getCrrId(), carrera.getCrrDescripcion()));
				}

				if (!carreras.isEmpty()) {
					erafListCarreraDtoSecretaria = carreras;
				}
				
				erafListOpcionReporte = cargarOpcionesReporteGenerico();
				retorno = "irListarEstudiantes";	
			} catch (RolFlujoCarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (RolFlujoCarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
			} catch (CarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else if (RolConstantes.ROL_SECREPOSGRADO_VALUE.equals(tipoRol) ) {
			List<CarreraDto> carreras = new ArrayList<>();

			try {

				List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_SECREPOSGRADO_VALUE);
				for(RolFlujoCarrera rlflcr: rolflujocarrera){
					Carrera carrera = servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId());
					carreras.add(new CarreraDto(carrera.getCrrId(), carrera.getCrrDescripcion()));
				}

				if (!carreras.isEmpty()) {
					erafListCarreraDtoSecretaria = carreras;
				}

				erafTituloPorNivelCarrera = new String("Programa");
				erafTituloPorNivelPeriodo = new String("Cohorte");
				erafListOpcionReporte = cargarOpcionesReporteGenerico();

				retorno = "irListarEstudiantes";	
			} catch (RolFlujoCarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (RolFlujoCarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
			} catch (CarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

		}else if (RolConstantes.ROL_ESTUD_PREGRADO_VALUE.equals(tipoRol)) {

			try {
				retorno = irCarrerasEstudiante(servJdbcEstudianteDto.buscarEstudianteXIdPersona(erafUsuario.getUsrPersona().getPrsId()));
				if (retorno != null) {
					retorno = "irListarCarrerasEstudiante";
				}
			} catch (EstudianteDtoJdbcException e) {
			} catch (EstudianteDtoJdbcNoEncontradoException e) {
			}

		}else if (RolConstantes.ROL_SOPORTE_VALUE.equals(tipoRol)) {
			// impresion de reportes
			try {
				List<CarreraDto> carreras = servJdbcCarreraDto.listarCarrerasTodas();
				if (!carreras.isEmpty()) {
					erafListCarreraDtoSecretaria = carreras;
					erafListOpcionReporte = cargarOpcionesReportePregrado();
					retorno = "irListarEstudiantes";
				}
			} catch (CarreraDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		}

		return retorno;
	}
	 
	
	
	
	public void setearTipoReporte(){
		cancelarGenerarReportes();
		erafNombreArchivo = null;
		erafNombreReporte = null;
	}
	
	public String irCarrerasEstudiante(EstudianteJdbcDto estudiante){
		String retorno = null;
		
		erafPersona = estudiante;
		erafListCarreraDto = null;
		erafListRecordEstudianteDto = null;
		
		List<RecordEstudianteDto> historial = new ArrayList<>();
		if (!erafTipoUsuario.equals(RolConstantes.ROL_SECREPOSGRADO_VALUE)) {
			historial = cargarHistorialAcademicoSAIUHomologado(erafPersona.getPrsIdentificacion());	
			if (!historial.isEmpty()) {
				erafListRecordEstudianteDto = historial;
				erafListCarreraDto = cargarCarrerasPorHistorial(historial);
				erafListCarreraDto.sort(Comparator.comparing(CarreraDto::getCrrDescripcion));
				retorno = "irCarrerasEstudiante";
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("El estudiante con identificación " + erafPersona.getPrsIdentificacion() + " no registra un historial académico en el SIIU - Académico." );
			}
		}else {
			historial = cargarHistorialAcademicoPosgradoSIIU(erafPersona.getPrsIdentificacion());	
			if (!historial.isEmpty()) {
				erafListRecordEstudianteDto = historial;
				erafListCarreraDto = cargarCarrerasPorHistorial(historial);
				erafListCarreraDto.sort(Comparator.comparing(CarreraDto::getCrrDescripcion));
				retorno = "irCarrerasEstudiante";
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("El estudiante con identificación " + erafPersona.getPrsIdentificacion() + " no registra un historial académico en el SIIU - Académico." );
			}
		}
		
		return retorno;
	}
	

	private List<CarreraDto> cargarCarrerasPorHistorial(List<RecordEstudianteDto> historial) {
		List<CarreraDto>  retorno = new ArrayList<>();
		
		Set<CarreraDto> carreras = historial.stream().collect(Collectors.groupingBy(RecordEstudianteDto::getRcesCarreraDto)).keySet();
		if (!carreras.isEmpty()) {
			Iterator<CarreraDto> it = carreras.iterator();
			while (it.hasNext()) {
				CarreraDto item = (CarreraDto) it.next();
				item.setCrrAcceso(Boolean.TRUE);
				retorno.add(item);
			}
		}
		
		return retorno;
	}

	

	/**
	 * Método que permite direccionar al historial academico.
	 * @param estudiante - estudiante de pregrado
	 */
	public String irRecordAcademico(CarreraDto carrera){
		String retorno = null;

		erafPorcentajeMallaAprobado = BigDecimal.ZERO;
		erafPromedioGeneral = BigDecimal.ZERO;
		erafParametros = new String[38];
		erafCrrId = carrera.getCrrId();
		erafCarreraDto = carrera;


		List<RecordEstudianteDto> recordCarreraSeleccionada = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, carrera.getCrrId());
		if (!recordCarreraSeleccionada.isEmpty()) {
			
			switch (carrera.getCrrTipo().intValue()) {
			case CarreraConstantes.TIPO_NIVELEACION_VALUE:
				cargarParametrosRecordAcademicoNivelacion(recordCarreraSeleccionada);
				retorno =  "irRecordAcademico";
				break;
			case CarreraConstantes.TIPO_PREGRADO_VALUE:
				List<MallaCurricularNivel> creditosAcumuladosPorMalla = cargarMallaCurricularNivelPorCarrera(carrera.getCrrId());
				if (!creditosAcumuladosPorMalla.isEmpty()) {
					
//					MallaCurricularNivel nivelMaximo = null;
//					if (carrera.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE) {
//						for (MallaCurricularNivel item : creditosAcumuladosPorMalla) {
//							if (item.getMlcrnvNivel().getNvlId() == NivelConstantes.NIVEL_DECIMO_VALUE) {
//								nivelMaximo = item;
//								break;
//							}
//						}
//					}else {
//					 nivelMaximo = creditosAcumuladosPorMalla.stream().max(Comparator.comparing(MallaCurricularNivel::getMlcrnvCreditosAcumulado)).get();
//					}
					
					MallaCurricularNivel nivelMaximo = creditosAcumuladosPorMalla.stream().max(Comparator.comparing(MallaCurricularNivel::getMlcrnvCreditosAcumulado)).get();
					cargarParametrosRecordAcademicoPregrado(recordCarreraSeleccionada, carrera, nivelMaximo.getMlcrnvCreditosAcumulado());						
					retorno =  "irRecordAcademico";
				}else {
					FacesUtil.mensajeError("Acérquese a la Dirección Académica y actualice la información de la curricular.");
				}
				
				break;
			case CarreraConstantes.TIPO_POSGRADO_VALUE:
				cargarParametrosRecordAcademicoPostgrado(recordCarreraSeleccionada);
				retorno =  "irRecordAcademico";
				break;
			case CarreraConstantes.TIPO_SUFICIENCIA_VALUE:
				cargarParametrosRecordAcademicoSuficiencias(recordCarreraSeleccionada);
				retorno =  "irRecordAcademico";
				break;
			}

			permisosImpresionRecord(carrera.getCrrId());
			erafListRecordEstudianteDtoSeleccion = recordCarreraSeleccionada;
			erafListRecordEstudianteDtoSeleccion.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracDescripcion));
			RecordEstudianteDto maxPeriodo = recordCarreraSeleccionada.get(recordCarreraSeleccionada.size()-1);
			erafParametros[33] = maxPeriodo.getRcesPeriodoAcademicoDto().getPracDescripcion(); 
		}else {
			FacesUtil.mensajeError("El estudiante aun no dispone de asignaturas con estados requeridos para generar el récord académico.");
		}

		return retorno;
	}
	
	private void cargarParametrosRecordAcademicoSuficiencias(List<RecordEstudianteDto> recordCarreraSeleccionada) {
		
		try {
			List<BigDecimal> aprobadas = new ArrayList<>();
			for (RecordEstudianteDto it : recordCarreraSeleccionada) {
				if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) || it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) {
					aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());
				}
			}
			
			BigDecimal suma = aprobadas.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			suma = establecerNumeroSinAproximacion(suma.divide(new BigDecimal(aprobadas.size()),new MathContext(6)),2);
			erafPromedioGeneral = establecerNumeroSinAproximacion(suma.divide(new BigDecimal(2),new MathContext(6)),2);	
		} catch (Exception e) {
		}
		
	}

	private void cargarParametrosRecordAcademicoNivelacion(List<RecordEstudianteDto> recordCarreraSeleccionada) {
		
		try {
			List<BigDecimal> aprobadas = new ArrayList<>();
			for (RecordEstudianteDto it : recordCarreraSeleccionada) {
				if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) || it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) {
					
					aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());
					
				}
			}
			
			BigDecimal suma = aprobadas.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			suma = establecerNumeroSinAproximacion(suma.divide(new BigDecimal(aprobadas.size()),new MathContext(6)),2);
			erafPromedioGeneral = establecerNumeroSinAproximacion(suma.divide(new BigDecimal(2),new MathContext(6)),2);	
		} catch (Exception e) {
		}
		
	}

	private void cargarParametrosRecordAcademicoPregrado(List<RecordEstudianteDto> recordCarreraSeleccionada, CarreraDto carrera, int totalHorasMalla) {

		setearPanelInfoAsignaturas(recordCarreraSeleccionada , carrera , totalHorasMalla);
		
		// GRATUIDAD
		if (verificarPerdidaDefinitivaGratuidad(erafPersona.getPrsIdentificacion(), recordCarreraSeleccionada, carrera.getCrrId(), totalHorasMalla)) {
			erafParametros[18] = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_LABEL;
		}else {
			erafParametros[18] = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_LABEL;
		}

		List<BigDecimal> aprobadasCarrera = new ArrayList<>();
		List<BigDecimal> aprobadasInternado = new ArrayList<>();
		int totalHorasAprobadasCarrera = 0;
//		if (carrera.getCrrId() != CarreraConstantes.CARRERA_MEDICINA_VALUE) {
			for (RecordEstudianteDto it : recordCarreraSeleccionada) {
				
				try {
					if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
							|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL)
							|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) {

						if (carrera.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE) {
							if (it.getRcesPeriodoAcademicoDto().getPracId() <=  PERIODO_SAU_NOTAS_SOBRE_DIEZ && it.getRcesCalificacionDto().getClfNotaFinalSemestre().floatValue() <= BigDecimal.valueOf(10).floatValue()) {// si prac_id <= 2015-2016 /10 (grupo 27)
								aprobadasCarrera.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre().multiply(BigDecimal.valueOf(4)));							
							}else {
								aprobadasCarrera.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());							
							}
							
						}else {
							aprobadasCarrera.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());							
						}

						// Calculo porcentajes
						if (it.getRcesCarreraDto().getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_LABEL)) {
							totalHorasAprobadasCarrera = totalHorasAprobadasCarrera + it.getRcesMateriaDto().getMtrHoras();
						}else {
							totalHorasAprobadasCarrera = totalHorasAprobadasCarrera + it.getRcesMateriaDto().getMtrCreditos();
						}
					}
				} catch (Exception e) {

				}
				
				
			}
//		}else {
//			for (RecordEstudianteDto it : recordCarreraSeleccionada) {
//
//				if ((it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
//						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL)
//						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL))
//						&& it.getRcesNivelDto().getNvlNumeral() <= NivelConstantes.NUMERAL_DECIMO_VALUE) {
//
//					aprobadasCarrera.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());
//
//					// Calculo porcentajes
//					if (it.getRcesCarreraDto().getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_LABEL)) {
//						totalHorasAprobadasCarrera = totalHorasAprobadasCarrera + it.getRcesMateriaDto().getMtrHoras();
//					}else {
//						totalHorasAprobadasCarrera = totalHorasAprobadasCarrera + it.getRcesMateriaDto().getMtrCreditos();
//					}
//				}
//				
//			}
//			
//			for (RecordEstudianteDto it : recordCarreraSeleccionada) {
//
//				if ((it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
//						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL)
//						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL))
//						&& it.getRcesNivelDto().getNvlNumeral() == NivelConstantes.NUMERAL_DECIMO_PRIMERO_VALUE) {
//
////					if (it.getRcesPeriodoAcademicoDto().getPracId() ==  PERIODO_SAU_NOTAS_SOBRE_DIEZ && it.getRcesCalificacionDto().getClfNotaFinalSemestre().floatValue() <= BigDecimal.valueOf(10).floatValue()) {// si prac_id <= 2015-2016 /10 (grupo 27)
////						aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre().multiply(BigDecimal.valueOf(4)));							
////					}else {
////						aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());							
////					}
//
//					aprobadasInternado.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());
//
//				}
//				
//			}
//			
//		}
		

		// INICIO CARRERA
		RecordEstudianteDto primerPeriodo = recordCarreraSeleccionada.stream().min(Comparator.comparing(RecordEstudianteDto::getRcesPracId)).get();
		erafParametros[19] = primerPeriodo.getRcesPracDescripcion();


		// FIN CARRERA
		BigDecimal porcentaje = calcularPorcentajeMallaAprobada(totalHorasMalla, totalHorasAprobadasCarrera);
		if (!porcentaje.equals(BigDecimal.ZERO) && porcentaje.floatValue() <=  new BigDecimal("100.00").floatValue()) {
			erafPorcentajeMallaAprobado = porcentaje;
		}else {
			erafPorcentajeMallaAprobado = new BigDecimal("100.00");
		}
		erafParametros[35] = getBigDecimal(erafPorcentajeMallaAprobado, 0);


		if (erafPorcentajeMallaAprobado.equals(new BigDecimal("100.00"))) {
			RecordEstudianteDto ultimoPeriodo = recordCarreraSeleccionada.stream().max(Comparator.comparing(RecordEstudianteDto::getRcesPracId)).get();
			erafParametros[20] = ultimoPeriodo.getRcesPracDescripcion();
		}


		try {
			BigDecimal suma = aprobadasCarrera.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			suma = establecerNumeroSinAproximacion(suma.divide(new BigDecimal(aprobadasCarrera.size()),new MathContext(6)),2);
			erafPromedioGeneral = establecerNumeroSinAproximacion(suma.divide(new BigDecimal(2),new MathContext(6)),2);	
			erafParametros[21] = getBigDecimal(erafPromedioGeneral,1); 
		} catch (Exception e) {
			erafParametros[21] = "Revisar calificaciones para realizar el cálculo."; 
		}
		
		try {
			BigDecimal suma = aprobadasInternado.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			erafParametros[25] = getBigDecimal(establecerNumeroSinAproximacion(suma, 2) , 1); 
		} catch (Exception e) {
			erafParametros[25] = "Revisar calificaciones para realizar el cálculo."; 
		}


		
		// TODO: SUFICIENCIAS
		erafParametros[22] = new String("NO REGISTRA");
		erafParametros[23] = new String("NO REGISTRA");
		erafParametros[24] = new String("NO REGISTRA");
		
		List<RecordEstudianteDto> recordCulturaFisica = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE);
		RecordEstudianteDto suficienciaCulturaFisica = verificarEstadoSuficienciaActividadFisica(recordCulturaFisica);
		if (suficienciaCulturaFisica != null) {
			StringBuilder periodo = new StringBuilder();
			periodo.append("APROBADO");
			periodo.append(suficienciaCulturaFisica.getRcesPeriodoAcademicoDto() == null ? " ": " - " +suficienciaCulturaFisica.getRcesPeriodoAcademicoDto().getPracDescripcion());
			erafParametros[22] = periodo.toString(); 
		}
		
		RecordEstudianteDto suficienciaInformatica = verificarEstadoSuficienciaInformatica(erafIdentificacion, CarreraConstantes.TIPO_PREGRADO_VALUE);
		if (suficienciaInformatica != null) {
			StringBuilder periodo = new StringBuilder();
			periodo.append("APROBADO");
			periodo.append(suficienciaInformatica.getRcesPeriodoAcademicoDto() == null ? " ": suficienciaInformatica.getRcesPeriodoAcademicoDto().getPracId() == 0 ? "" : " - " +suficienciaInformatica.getRcesPeriodoAcademicoDto().getPracDescripcion());
			erafParametros[23] = periodo.toString(); 
		}
		
		List<RecordEstudianteDto> recordIdiomas = new ArrayList<>();
		List<RecordEstudianteDto> recordIngles = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE);
		if (!recordIngles.isEmpty()) {
			recordIdiomas.addAll(recordIngles);
		}
		List<RecordEstudianteDto> recordFrances = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE);
		if (!recordFrances.isEmpty()) {
			recordIdiomas.addAll(recordFrances);
		}
		List<RecordEstudianteDto> recordItaliano = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE);
		if (!recordItaliano.isEmpty()) {
			recordIdiomas.addAll(recordItaliano);
		}
		List<RecordEstudianteDto> recordCoreano = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE);
		if (!recordCoreano.isEmpty()) {
			recordIdiomas.addAll(recordCoreano);
		}
		List<RecordEstudianteDto> recordKichwa = cargarRecordAcademicoCarreraSelecionada(erafListRecordEstudianteDto, CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE);
		if (!recordKichwa.isEmpty()) {
			recordIdiomas.addAll(recordKichwa);
		}
		
		RecordEstudianteDto suficienciaIdiomas = verificarEstadoSuficienciaIdiomas(recordIdiomas);
		if (suficienciaIdiomas != null) {
			StringBuilder periodo = new StringBuilder();
			periodo.append("APROBADO");
			periodo.append(" - " + suficienciaIdiomas.getRcesCarreraDto().getCrrDescripcion());
			periodo.append(suficienciaIdiomas.getRcesPeriodoAcademicoDto() == null ? " ": " - " +suficienciaIdiomas.getRcesPeriodoAcademicoDto().getPracDescripcion());
			erafParametros[24] = periodo.toString(); 
		}
		
	}
	
	private void setearPanelInfoAsignaturas(List<RecordEstudianteDto> recordCarreraSeleccionada, CarreraDto carrera , int totalHorasMalla) {
		long aprob_asig_1 = 0; long aprob_asig_2 =0; long aprob_asig_3 = 0;
		long reprob_asig_1 = 0; long reprob_asig_2 =0; long reprob_asig_3 = 0;
		long hom_asig_1 = 0; long hom_asig_2 =0; long hom_asig_3 = 0;
		
		long aprob_horas_1 = 0;long aprob_horas_2 = 0;long aprob_horas_3 = 0;
		long reprob_horas_1 = 0;long reprob_horas_2 = 0;long reprob_horas_3 = 0;
		long hom_horas_1 = 0;long hom_horas_2 = 0;long hom_horas_3 = 0;
		
		long suma_aprob_asig = 0;long suma_reprob_asig = 0;
		long suma_aprob_horas = 0;long suma_reprob_horas = 0;
		
		aprob_asig_1 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==1).count(); 
		aprob_asig_2 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==2).count(); 
		aprob_asig_3 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()>=3).count();

		reprob_asig_1 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==1).count();
		reprob_asig_2 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==2).count(); 
		reprob_asig_3 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()>=3).count();

		hom_asig_1 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==1).count(); 
		hom_asig_2 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==2).count(); 
		hom_asig_3 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()>=3).count();

		erafParametros[0]  = String.valueOf(aprob_asig_1);
		erafParametros[1]  = String.valueOf(aprob_asig_2);
		erafParametros[2]  = String.valueOf(aprob_asig_3);
		erafParametros[26]  = String.valueOf(aprob_asig_1 + aprob_asig_2 + aprob_asig_3);
		
		erafParametros[3]  = String.valueOf(reprob_asig_1);
		erafParametros[4]  = String.valueOf(reprob_asig_2);
		erafParametros[5]  = String.valueOf(reprob_asig_3);
		erafParametros[27]  = String.valueOf(reprob_asig_1 + reprob_asig_2 + reprob_asig_3);

		erafParametros[6]  = String.valueOf(hom_asig_1);
		erafParametros[7]  = String.valueOf(hom_asig_2);
		erafParametros[8]  = String.valueOf(hom_asig_3);
		erafParametros[28]  = String.valueOf(hom_asig_1 + hom_asig_2 + hom_asig_3);

		
		if (carrera.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
			aprob_horas_1 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			aprob_horas_2 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==2).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			aprob_horas_3 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()>=3).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();

			reprob_horas_1 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			reprob_horas_2 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==2).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			reprob_horas_3 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()>=3).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();

			hom_horas_1 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			hom_horas_2 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==2).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			hom_horas_3 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()>=3).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			
			erafParametros[9]  = String.valueOf(aprob_horas_1);
			erafParametros[10]  = String.valueOf(aprob_horas_2);
			erafParametros[11]  = String.valueOf(aprob_horas_3);
			erafParametros[29]  = String.valueOf(aprob_horas_1 + aprob_horas_2 + aprob_horas_3);
			
			erafParametros[12]  = String.valueOf(reprob_horas_1);
			erafParametros[13]  = String.valueOf(reprob_horas_2);
			erafParametros[14]  = String.valueOf(reprob_horas_3);
			erafParametros[30]  = String.valueOf(reprob_horas_1 + reprob_horas_2 + reprob_horas_3);

			erafParametros[15]  = String.valueOf(hom_horas_1);
			erafParametros[16]  = String.valueOf(hom_horas_2);
			erafParametros[17]  = String.valueOf(hom_horas_3);
			erafParametros[31]  = String.valueOf(hom_horas_1 + hom_horas_2 + hom_horas_3);

		}else {
			aprob_horas_1 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			aprob_horas_2 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==2).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			aprob_horas_3 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()>=3).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();

			reprob_horas_1 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			reprob_horas_2 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()==2).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			reprob_horas_3 = recordCarreraSeleccionada.stream().filter(item -> item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesMateriaDto().getNumMatricula()>=3).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();

			hom_horas_1 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			hom_horas_2 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==2).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			hom_horas_3 = recordCarreraSeleccionada.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()>=3).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			
			erafParametros[9]  = String.valueOf(aprob_horas_1);
			erafParametros[10]  = String.valueOf(aprob_horas_2);
			erafParametros[11]  = String.valueOf(aprob_horas_3);
			erafParametros[29]  = String.valueOf(aprob_horas_1 + aprob_horas_2 + aprob_horas_3);

			erafParametros[12]  = String.valueOf(reprob_horas_1);
			erafParametros[13]  = String.valueOf(reprob_horas_2);
			erafParametros[14]  = String.valueOf(reprob_horas_3);
			erafParametros[30]  = String.valueOf(reprob_horas_1 + reprob_horas_2 + reprob_horas_3);

			erafParametros[15]  = String.valueOf(hom_horas_1);
			erafParametros[16]  = String.valueOf(hom_horas_2);
			erafParametros[17]  = String.valueOf(hom_horas_3);
			erafParametros[31]  = String.valueOf(hom_horas_1 + hom_horas_2 + hom_horas_3);
		}
		
		suma_aprob_asig = aprob_asig_1 + aprob_asig_2 + aprob_asig_3 + hom_asig_1 + hom_asig_2 + hom_asig_3;
		suma_reprob_asig = reprob_asig_1 + reprob_asig_2 + reprob_asig_3;
		
		suma_aprob_horas = aprob_horas_1 + aprob_horas_2 + aprob_horas_3 + hom_horas_1 + hom_horas_2 + hom_horas_3;
		suma_reprob_horas = reprob_horas_1 + reprob_horas_2 + reprob_horas_3;
		
		erafParametros[32]  = String.valueOf(suma_aprob_asig);
		erafParametros[34]  = String.valueOf(suma_reprob_asig);
		
		erafParametros[36]  = String.valueOf(suma_aprob_horas);
		erafParametros[37]  = String.valueOf(suma_reprob_horas);

	}

	private void cargarParametrosRecordAcademicoPostgrado(List<RecordEstudianteDto> recordCarreraSeleccionada) {

		List<BigDecimal> aprobadas = new ArrayList<>();
		BigDecimal suma = BigDecimal.ZERO;
		aprobadas = new ArrayList<>();
		
		//considerar notas sobre que nota hay que promediar
		for (RecordEstudianteDto it : recordCarreraSeleccionada) {
			if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)) {
				aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());							
				suma = suma.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());
			}

		}

		erafPromedioGeneral = suma.divide(new BigDecimal(aprobadas.size()),1,RoundingMode.HALF_UP);
		erafListRecordEstudianteDtoSeleccion = recordCarreraSeleccionada;
		erafListRecordEstudianteDtoSeleccion.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracDescripcion));

	}
	

	private List<MallaCurricularNivel> cargarMallaCurricularNivelPorCarrera(int carreraId){
		List<MallaCurricularNivel> retorno = new ArrayList<>();
		
			try {
				retorno = servMallaCurricularNivel.buscarCreditosPorNivel(carreraId);
			} catch (MallaCurricularNivelNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MallaCurricularNivelValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MallaCurricularNivelException e) {
				FacesUtil.mensajeError(e.getMessage());
			}	
		
		return retorno;
	}
	
	
	private void permisosImpresionRecord(int carreraId) {
		erafAcceso = Boolean.FALSE;
		
		if (erafTipoUsuario.equals(RolConstantes.ROL_SECRECARRERA_VALUE) ||
				erafTipoUsuario.equals(RolConstantes.ROL_SECRENIVELACION_VALUE) ||
				erafTipoUsuario.equals(RolConstantes.ROL_SECRESUFICIENCIAS_VALUE) ||
				erafTipoUsuario.equals(RolConstantes.ROL_SECREPOSGRADO_VALUE)) {
			for (CarreraDto item : erafListCarreraDtoSecretaria) {
				if (item.getCrrId() == carreraId) {
					erafAcceso = Boolean.TRUE;
				}
			}
		}
		
	}
	

	private List<RecordEstudianteDto> cargarRecordAcademicoCarreraSelecionada(List<RecordEstudianteDto> historialAcademico, int carreraId) {
		 List<RecordEstudianteDto> retorno = new ArrayList<>();
		 
		for (RecordEstudianteDto it : historialAcademico) {
//			System.out.println(it.getRcesCarreraDto().getCrrId() + " espe_codigo " + it.getRcesCarreraDto().getCrrEspeCodigo());
			if (it.getRcesCarreraDto().getCrrId() == carreraId) {
				if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL)
						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)
						|| it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
					retorno.add(it);						
				}
			}
		}
		
		return retorno;
	}

	public String  irMatriculasEstudiante(CarreraDto carrera){
		if (erafListRecordEstudianteDto != null && erafListRecordEstudianteDto.size() > 0) {
			erafCrrId = null;
			erafCrrId = carrera.getCrrId();
			List<RecordEstudianteDto> record = erafListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
					recordSeleccion.add(it);
				}
			}

			if (recordSeleccion.size() > 0) {
				erafListPeriodoAcademicoDto = null;
				erafListPeriodoAcademicoDto = llenarHistorialMatricula(recordSeleccion);
			}else {
				FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
				return null;
			}
			
			return "irMatriculasEstudiante";	
		}else {
			FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
			return null;
		}
	}
	
	
	public String  irPeriodosAcademicosEstudiante(CarreraDto carrera){
		
		if (erafListRecordEstudianteDto != null && erafListRecordEstudianteDto.size() > 0) {
			erafCrrId = null;
			erafCrrId = carrera.getCrrId();
			List<RecordEstudianteDto> record = erafListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
					recordSeleccion.add(it);
				}
			}

			if (recordSeleccion.size() > 0) {
				erafListRecordEstudianteDtoPeriodoDto = null;
				erafListRecordEstudianteDtoPeriodoDto = llenarPeriodosAcademicos(recordSeleccion);
			}else {
				FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
				return null;
			}
			
			return "irPeriodosEstudiante";	
		}else {
			FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
			return null;
		}
		
	}
	
	public String  irNotasEstudiante(RecordEstudianteDto periodo){
		erafPeriodoAcademicoDto = periodo;
		
		if (erafListRecordEstudianteDto != null && erafListRecordEstudianteDto.size() > 0) {
			
			List<RecordEstudianteDto> record = erafListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == erafCrrId && it.getRcesPeriodoAcademicoDto().getPracDescripcion().equals(periodo.getRcesPeriodoAcademicoDto().getPracDescripcion())) {
					recordSeleccion.add(it);
				}
			}

			if (recordSeleccion.size() > 0) {
				erafListRecordEstudianteDtoMateriaDto  = null;
				erafListRecordEstudianteDtoMateriaDto  = recordSeleccion;
				
			}else {
				FacesUtil.mensajeError("Error, no existen registros de notas para este período.");
				return null;
			}
			
			return "irNotasEstudiante";
			
			
		}else {
			FacesUtil.mensajeError("Error, no existen registros de notas para este período.");
			return null;
		}
		
	}
	
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiarFormEstudiantes() {
		
		erafIdentificacion = new String();
		erafPrimerApellido = new String();
		erafListPersona = null;
		
	}
	
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiarFormRecordAcademico(){
		erafListRecordEstudianteDto = null;
		erafCrrId = GeneralesConstantes.APP_ID_BASE;
	}
	

	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscarEstudiantesSIIU() {

		if (erafTipoBusqueda.intValue() != GeneralesConstantes.APP_ID_BASE) {

			String param = null;
			if (erafTipoBusqueda == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = erafIdentificacion;
			}else {
				param = erafPrimerApellido;
			}

			List<EstudianteJdbcDto>  estudiantes = cargarEstudianteSAIU(param, null, erafTipoBusqueda);
			if (estudiantes != null && estudiantes.size() > 0) {
				erafListPersona = estudiantes;
			}else {
				erafListPersona = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}

		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar con la búsqueda.");
		}

	}
	
	
	public void busquedaPorIdentificacion(){
		
		if (erafIdentificacion.length() > 0) {
			erafPrimerApellido = new String();
			erafTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (erafPrimerApellido.length() > 0) {
			erafIdentificacion = new String();
			erafTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	
	public void cancelarGenerarReportes(){
		erafOpcionReporteId = GeneralesConstantes.APP_ID_BASE;
		ecnfActivarReporte = GeneralesConstantes.APP_ID_BASE;
	}
	
	
	
	/**
	 * Método que genera el reporte
	 */
	private void cargarReporteRecordAcademicoNivelacion(List<RecordEstudianteDto> listaRecord){
		
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "RÉCORD ACADÉMICO_" + erafPersona.getPrsIdentificacion().toUpperCase();
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_RECORD_ACADEMICO);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		
		frmRrmParametros.put("encabezado_adicional", erafPersona.getPrsIdentificacion() + " \n"+ erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido() + " " + erafPersona.getPrsNombres());
		frmRrmParametros.put("estudiante", erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido() + " " + erafPersona.getPrsNombres()  );
		frmRrmParametros.put("identificacion",  erafPersona.getPrsIdentificacion());
		frmRrmParametros.put("codigo_estudiantil",  String.valueOf(erafPersona.getPrsId()));
		frmRrmParametros.put("facultad", listaRecord.get(0).getRcesDependenciaDto().getDpnDescripcion());
		frmRrmParametros.put("carrera", listaRecord.get(0).getRcesCarreraDto().getCrrDescripcion());
		frmRrmParametros.put("nick", erafUsuario.getUsrNick());
		
		frmRrmParametros.put("promedio_general",   getBigDecimal(erafPromedioGeneral,1));
		
		PersonaDto director = cargarDirectorCarrera();
		if (director != null) {
			frmRrmParametros.put("director_carrera", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director_carrera", "Registre al Director de su Carrera");
		}
		
		
		PersonaDto secretario =  cargarSecretarioAbogado();
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : listaRecord) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("periodo", item.getRcesPeriodoAcademicoDto().getPracDescripcion());
			datoHorario.put("codigo_matricula", String.valueOf(item.getRcesFichaMatriculaDto().getFcmtId()));
			datoHorario.put("codigo_materia", item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("numero_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			
			if (item.getRcesCarreraDto().getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
				datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrHoras()+item.getRcesMateriaDto().getMtrHorasPorSemanaPAE()));
			}else {
				datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrCreditos()));
			}
			
			datoHorario.put("nota1", getBigDecimal(item.getRcesCalificacionDto().getClfNota1(),1));
			datoHorario.put("nota2", getBigDecimal(item.getRcesCalificacionDto().getClfNota2(),1));
			datoHorario.put("recuperacion", getBigDecimal(item.getRcesCalificacionDto().getClfSupletorio(),1));
			datoHorario.put("nota_final", getBigDecimal(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("asistencia", getBigDecimal(item.getRcesCalificacionDto().getClfPorcentajeAsistencia(),0));
			datoHorario.put("estado", item.getRcesMateriaDto().getMtrEstadoLabel());
			frmRrmCampos.add(datoHorario);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRrmParametros);
		
	}
	
	/**
	 * Método que genera el reporte
	 */
	private void cargarReporteRecordAcademicoSuficiencias(List<RecordEstudianteDto> listaRecord){
		
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "RÉCORD ACADÉMICO_" + erafPersona.getPrsIdentificacion().toUpperCase();
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_RECORD_ACADEMICO);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		
		frmRrmParametros.put("encabezado_adicional", erafPersona.getPrsIdentificacion() + " \n"+ erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido() + " " + erafPersona.getPrsNombres());
		frmRrmParametros.put("estudiante", erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido() + " " + erafPersona.getPrsNombres()  );
		frmRrmParametros.put("identificacion",  erafPersona.getPrsIdentificacion());
		frmRrmParametros.put("codigo_estudiantil",  String.valueOf(erafPersona.getPrsId()));
		frmRrmParametros.put("facultad", listaRecord.get(0).getRcesDependenciaDto().getDpnDescripcion());
		frmRrmParametros.put("carrera", listaRecord.get(0).getRcesCarreraDto().getCrrDescripcion());
		frmRrmParametros.put("nick", erafUsuario.getUsrNick());
		
		frmRrmParametros.put("promedio_general",   getBigDecimal(erafPromedioGeneral,1));
		
		PersonaDto director = cargarDirectorCarrera();
		if (director != null) {
			frmRrmParametros.put("director_carrera", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director_carrera", "Registre al Director de su Carrera");
		}
		
		
		PersonaDto secretario =  cargarSecretarioAbogado();
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : listaRecord) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("periodo", item.getRcesPeriodoAcademicoDto().getPracDescripcion());
			datoHorario.put("codigo_matricula", String.valueOf(item.getRcesFichaMatriculaDto().getFcmtId()));
			datoHorario.put("codigo_materia", item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("numero_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			
			if (item.getRcesCarreraDto().getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
				datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrHoras()+item.getRcesMateriaDto().getMtrHorasPorSemanaPAE()));
			}else {
				datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrCreditos()));
			}
			
			datoHorario.put("nota1", getBigDecimal(item.getRcesCalificacionDto().getClfNota1(),1));
			datoHorario.put("nota2", getBigDecimal(item.getRcesCalificacionDto().getClfNota2(),1));
			datoHorario.put("recuperacion", getBigDecimal(item.getRcesCalificacionDto().getClfSupletorio(),1));
			datoHorario.put("nota_final", getBigDecimal(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("asistencia", getBigDecimal(item.getRcesCalificacionDto().getClfPorcentajeAsistencia(),0));
			datoHorario.put("estado", item.getRcesMateriaDto().getMtrEstadoLabel());
			frmRrmCampos.add(datoHorario);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRrmParametros);
		
	}
	
	
	/**
	 * Método que genera el reporte
	 */
	private void cargarReporteRecordAcademicoPorCarrera(List<RecordEstudianteDto> listaRecord){
		
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "RÉCORD ACADÉMICO_" + erafPersona.getPrsIdentificacion().toUpperCase();
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_RECORD_ACADEMICO);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		
		frmRrmParametros.put("encabezado_adicional", erafPersona.getPrsIdentificacion() + " \n"+ erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido() + " " + erafPersona.getPrsNombres());
		frmRrmParametros.put("estudiante", erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido() + " " + erafPersona.getPrsNombres()  );
		frmRrmParametros.put("identificacion",  erafPersona.getPrsIdentificacion());
		frmRrmParametros.put("codigo_estudiantil",  String.valueOf(erafPersona.getPrsId()));
		frmRrmParametros.put("facultad", listaRecord.get(0).getRcesDependenciaDto().getDpnDescripcion());
		frmRrmParametros.put("carrera", listaRecord.get(0).getRcesCarreraDto().getCrrDescripcion());
		frmRrmParametros.put("nick", erafUsuario.getUsrNick());
		
		frmRrmParametros.put("aprob_asig_1",  erafParametros[0]);
		frmRrmParametros.put("aprob_asig_2",  erafParametros[1]);
		frmRrmParametros.put("aprob_asig_3",  erafParametros[2]);
		frmRrmParametros.put("reprob_asig_1",  erafParametros[3]);
		frmRrmParametros.put("reprob_asig_2",  erafParametros[4]);
		frmRrmParametros.put("reprob_asig_3",  erafParametros[5]);
		frmRrmParametros.put("hom_asig_1",  erafParametros[6]);
		frmRrmParametros.put("hom_asig_2",  erafParametros[7]);
		frmRrmParametros.put("hom_asig_3",  erafParametros[8]);
		
		frmRrmParametros.put("aprob_horas_1",  erafParametros[9]);
		frmRrmParametros.put("aprob_horas_2",  erafParametros[10]);
		frmRrmParametros.put("aprob_horas_3",  erafParametros[11]);
		frmRrmParametros.put("reprob_horas_1",  erafParametros[12]);
		frmRrmParametros.put("reprob_horas_2",  erafParametros[13]);
		frmRrmParametros.put("reprob_horas_3",  erafParametros[14]);
		frmRrmParametros.put("hom_horas_1",  erafParametros[15]);
		frmRrmParametros.put("hom_horas_2",  erafParametros[16]);
		frmRrmParametros.put("hom_horas_3",  erafParametros[17]);
		
		frmRrmParametros.put("gratuidad",  erafParametros[18]);
		frmRrmParametros.put("inicio_carrera",  erafParametros[19]);
		frmRrmParametros.put("fin_carrera",  erafParametros[20]);
		frmRrmParametros.put("promedio_general",  erafParametros[21]);
		
		frmRrmParametros.put("actividad_fisica",  erafParametros[22]);
		frmRrmParametros.put("herramientas_informaticas",  erafParametros[23]);
		frmRrmParametros.put("idiomas",  erafParametros[24]);
		
		if (erafCarreraDto.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE) {
			frmRrmParametros.put("promedio_internado",  erafParametros[25]);
		}
		
		frmRrmParametros.put("suma_aprob_asig",  erafParametros[26]);
		frmRrmParametros.put("suma_reprob_asig",  erafParametros[27]);
		frmRrmParametros.put("suma_hom_asig",  erafParametros[28]);
		
		frmRrmParametros.put("suma_aprob_horas",  erafParametros[29]);
		frmRrmParametros.put("suma_reprob_horas",  erafParametros[30]);
		frmRrmParametros.put("suma_hom_horas",  erafParametros[31]);
		
		frmRrmParametros.put("aprobadas_asig",  erafParametros[32]);
		frmRrmParametros.put("reprobadas_asig",  erafParametros[34]);
		frmRrmParametros.put("porcentaje_aprobado",  erafParametros[35]);
		frmRrmParametros.put("aprobadas_horas",  erafParametros[36]);
		frmRrmParametros.put("reprobadas_horas",  erafParametros[37]);
		
		frmRrmParametros.put("periodo_group",  erafParametros[33]);
		
		
		
		PersonaDto director = cargarDirectorCarrera();
		if (director != null) {
			frmRrmParametros.put("director_carrera", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director_carrera", "Registre al Director de su Carrera");
		}
		
		
		PersonaDto secretario =  cargarSecretarioAbogado();
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : listaRecord) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("periodo", item.getRcesPeriodoAcademicoDto().getPracDescripcion());
			datoHorario.put("codigo_matricula", String.valueOf(item.getRcesFichaMatriculaDto().getFcmtId()));
			datoHorario.put("codigo_materia", item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("numero_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			
			if (item.getRcesCarreraDto().getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
				datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrHoras()+item.getRcesMateriaDto().getMtrHorasPorSemanaPAE()));
			}else {
				datoHorario.put("horas", String.valueOf(item.getRcesMateriaDto().getMtrCreditos()));
			}
			
			datoHorario.put("nota1", getBigDecimal(item.getRcesCalificacionDto().getClfNota1(),1));
			datoHorario.put("nota2", getBigDecimal(item.getRcesCalificacionDto().getClfNota2(),1));
			datoHorario.put("recuperacion", getBigDecimal(item.getRcesCalificacionDto().getClfSupletorio(),1));
			datoHorario.put("nota_final", getBigDecimal(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("asistencia", getBigDecimal(item.getRcesCalificacionDto().getClfPorcentajeAsistencia(),0));
			datoHorario.put("estado", item.getRcesMateriaDto().getMtrEstadoLabel());
			frmRrmCampos.add(datoHorario);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRrmParametros);
		
	}
	
	
	public void cargarReporteRecordAcademicoAprobadasMedicina(List<RecordEstudianteDto> listaRecord){
		
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "RÉCORD ACADÉMICO_" + erafPersona.getPrsIdentificacion().toUpperCase();
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_RECORD_ACADEMICO);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		
		frmRrmParametros.put("estudiante", erafPersona.getPrsNombres() + " "+ erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  erafPersona.getPrsIdentificacion());
		frmRrmParametros.put("codigo_estudiantil",  String.valueOf(erafPersona.getPrsId()));
		frmRrmParametros.put("facultad", listaRecord.get(0).getRcesDependenciaDto().getDpnDescripcion());
		frmRrmParametros.put("carrera", listaRecord.get(0).getRcesCarreraDto().getCrrDescripcion());
		frmRrmParametros.put("nick", erafUsuario.getUsrNick());
		
		PersonaDto director = cargarDirectorCarrera();
		if (director != null) {
			frmRrmParametros.put("director_carrera", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director_carrera", "Registre al Director de su Carrera");
		}
		
		
		PersonaDto secretario =  cargarSecretarioAbogado();
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		
		frmRrmParametros.put("promedio_carrera", getBigDecimal(erafPromedioGeneral,1));
//		frmRrmParametros.put("promedio_internado", getBigDecimal(erafPromedioInternado,1));

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : listaRecord) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("periodo", item.getRcesPeriodoAcademicoDto().getPracDescripcion());
			datoHorario.put("codigo_matricula", String.valueOf(item.getRcesFichaMatriculaDto().getFcmtId()));
			datoHorario.put("codigo_materia", item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("numero_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			datoHorario.put("nota_final", getBigDecimal(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("estado_materia", item.getRcesMateriaDto().getMtrEstadoLabel());
			frmRrmCampos.add(datoHorario);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRrmParametros);
		
	}


	public void cargarReporteRecordAcademicoPosgrado(List<RecordEstudianteDto> listaRecord){
		
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

		frmRrmNombreReporte = "RÉCORD ACADÉMICO_" + erafPersona.getPrsIdentificacion().toUpperCase();
		
		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagenPie",  pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION );
		frmRrmParametros.put("encabezado_reporte", GENERAL_TITULO_REPORTE_RECORD_ACADEMICO);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO);
		
		frmRrmParametros.put("estudiante", erafPersona.getPrsNombres() + " "+ erafPersona.getPrsPrimerApellido() + " "+ erafPersona.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  erafPersona.getPrsIdentificacion());
		frmRrmParametros.put("codigo_estudiantil",  String.valueOf(erafPersona.getPrsId()));
		frmRrmParametros.put("facultad", listaRecord.get(0).getRcesDependenciaDto().getDpnDescripcion());
		frmRrmParametros.put("carrera", listaRecord.get(0).getRcesCarreraDto().getCrrDescripcion());
		frmRrmParametros.put("nick", erafUsuario.getUsrNick());
		
		PersonaDto director = cargarDirectorPosgrado();
		if (director != null) {
			frmRrmParametros.put("director_carrera", director.getPrsNombres() +" "+ director.getPrsPrimerApellido() + " "+ director.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("director_carrera", "Registre al Director de Posgrado");
		}
		
		
		PersonaDto secretario =  cargarSecretarioAbogado();
		if (secretario != null) {
			frmRrmParametros.put("secretario_abogado", secretario.getPrsNombres() +" "+ secretario.getPrsPrimerApellido() + " "+ secretario.getPrsSegundoApellido());
		}else {
			frmRrmParametros.put("secretario_abogado", "Registre al Secretario de su Dependencia");
		}
		
		frmRrmParametros.put("promedio", getBigDecimal(erafPromedioGeneral,1));

		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		for (RecordEstudianteDto item : listaRecord) {
			datoHorario = new HashMap<String, Object>();			
			datoHorario.put("periodo", item.getRcesPeriodoAcademicoDto().getPracDescripcion());
			datoHorario.put("codigo_matricula", String.valueOf(item.getRcesFichaMatriculaDto().getFcmtId()));
			datoHorario.put("codigo_materia", item.getRcesMateriaDto().getMtrCodigo());
			datoHorario.put("materia", item.getRcesMateriaDto().getMtrDescripcion());
			datoHorario.put("numero_matricula", String.valueOf(item.getRcesMateriaDto().getNumMatricula()));
			datoHorario.put("nota_final", getBigDecimal(item.getRcesCalificacionDto().getClfNotaFinalSemestre(),1));
			datoHorario.put("asistencia", getBigDecimal(item.getRcesCalificacionDto().getClfPorcentajeAsistencia(),0));
			datoHorario.put("estado", item.getRcesMateriaDto().getMtrEstadoLabel());
			frmRrmCampos.add(datoHorario);
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros", frmRrmParametros);
		
	}
	
	
	
	
	/**
	 * Método que da formato de dos decimales a un BigDecimal.
	 * @param param - bigdecimal
	 * @param simbolo - adiciona simbolo.
	 * @return valor en formato string.
	 */
	public static String getBigDecimal(BigDecimal param, int simbolo){
		
		if (param != null && param.intValue() != 0 && param.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (simbolo == 0) {
				return String.format("%.2f", param) + " %";	
			}else if(param.intValue() == -1 ){
				return "";
			}else if(simbolo == 1 ){
				return String.format("%.2f", param);
			}else if (simbolo == 2) {
				return String.valueOf(param.intValue());
			}
		}
		
		return "";
	}
	
	private List<SelectItem> cargarOpcionesReportePregrado() {
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(new Integer(RECORD_SIN_RESUMEN_GENERAL_VALUE), new String(RECORD_SIN_RESUMEN_GENERAL_LABEL)));
		retorno.add(new SelectItem(new Integer(RECORD_CON_RESUMEN_GENERAL_VALUE), new String(RECORD_CON_RESUMEN_GENERAL_LABEL)));
		retorno.add(new SelectItem(new Integer(RECORD_SOLO_APROBADAS_VALUE), new String(RECORD_SOLO_APROBADAS_LABEL)));
		return retorno;
	}
	
	private List<SelectItem> cargarOpcionesReporteGenerico() {
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(new Integer(RECORD_SIN_RESUMEN_GENERAL_VALUE), new String(RECORD_SIN_RESUMEN_GENERAL_LABEL)));
		return retorno;
	}
	
	private final int RECORD_SIN_RESUMEN_GENERAL_VALUE = 1;
	private final int RECORD_CON_RESUMEN_GENERAL_VALUE = 2;
	private final int RECORD_SOLO_APROBADAS_VALUE = 3;
	
	private String RECORD_SIN_RESUMEN_GENERAL_LABEL = "GENERAL";
	private String RECORD_CON_RESUMEN_GENERAL_LABEL = "CON RESUMEN POR CARRERA";
	private String RECORD_SOLO_APROBADAS_LABEL = "SOLO APROBADAS";
	
	
	public void generarReporte() {

		ecnfActivarReporte = 0;
		if (!erafOpcionReporteId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!erafListRecordEstudianteDtoSeleccion.isEmpty()) {
				
//				if (erafCarreraDto.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE) {
//					
//					switch (erafOpcionReporteId) {
//					case RECORD_SIN_RESUMEN_GENERAL_VALUE:
//						setearParametrosGeneracionReporte("medicinaPlantillaB" , erafOpcionReporteId);
//						break;
//					case RECORD_CON_RESUMEN_GENERAL_VALUE:
//						setearParametrosGeneracionReporte("medicinaPlantillaA" , erafOpcionReporteId);
//						break;
//					case RECORD_SOLO_APROBADAS_VALUE:
//						setearParametrosGeneracionReporte("medicinaPlantillaC" , erafOpcionReporteId);
//						break;
//					}
//					
//				}else {
					
					switch (erafOpcionReporteId) {
					case RECORD_CON_RESUMEN_GENERAL_VALUE:
						setearParametrosGeneracionReporte("carrerasPlantillaA" , erafOpcionReporteId);
						break;
					case RECORD_SIN_RESUMEN_GENERAL_VALUE:
						setearParametrosGeneracionReporte("carrerasPlantillaB" , erafOpcionReporteId);
						break;
					case RECORD_SOLO_APROBADAS_VALUE:
						setearParametrosGeneracionReporte("carrerasPlantillaC" , erafOpcionReporteId);
						break;
					}
					
//				}
				
				
			}else {
				FacesUtil.mensajeInfo("No existe registro de asignaturas para generar el reporte.");
			}
			
		}else {
			cancelarGenerarReportes();
			FacesUtil.mensajeInfo("Seleccione una opcion para generar el reporte");
		}

	}

	private void setearParametrosGeneracionReporte(String nombreArchivo, int tipoReporte){
		
		if (tipoReporte != RECORD_SOLO_APROBADAS_VALUE) {
			
			switch (erafCarreraDto.getCrrTipo().intValue()) {
			case CarreraConstantes.TIPO_NIVELEACION_VALUE:
				erafNombreReporte = "NIVELACION_CERTIFICADOS_RECORD";
				erafNombreArchivo  = "carrerasPlantillaA";
				cargarReporteRecordAcademicoNivelacion(erafListRecordEstudianteDtoSeleccion);
				break;
			case CarreraConstantes.TIPO_PREGRADO_VALUE:
				erafNombreReporte = "PREGRADO_CERTIFICADOS_RECORD";
				erafNombreArchivo  = nombreArchivo;
				cargarReporteRecordAcademicoPorCarrera(erafListRecordEstudianteDtoSeleccion);
				break;
			case CarreraConstantes.TIPO_POSGRADO_VALUE:
				erafNombreReporte = "CERTIFICADO_RECORD_ACADEMICO_POSGRADO";
				erafNombreArchivo  = "reporteRecordAcademico";
				cargarReporteRecordAcademicoPosgrado(erafListRecordEstudianteDtoSeleccion);
				break;
			case CarreraConstantes.TIPO_SUFICIENCIA_VALUE:
				erafNombreReporte = "SUFICIENCIAS_CERTIFICADOS_RECORD";
				erafNombreArchivo  = "record";
				cargarReporteRecordAcademicoSuficiencias(erafListRecordEstudianteDtoSeleccion);
				break;
			}
			
		}else {
			erafNombreReporte = "PREGRADO_CERTIFICADOS_RECORD";
			erafNombreArchivo  = nombreArchivo;
			
			List<RecordEstudianteDto> recordAprobadas = new ArrayList<>();
			recordAprobadas .addAll(erafListRecordEstudianteDtoSeleccion);
			
			Iterator<RecordEstudianteDto> iterator = recordAprobadas.iterator();
			while (iterator.hasNext()) {
				RecordEstudianteDto item = (RecordEstudianteDto) iterator.next();
				if (!item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)
						|| !item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_CONVALIDADO_LABEL)
						|| !item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) {
					iterator.remove();
				}
			}
			
			cargarReporteRecordAcademicoPorCarrera(recordAprobadas);
		}
		
		ecnfActivarReporte = 1;	

	}
	
	
	/**
	 * Mètodo que permite cargar datos del director de carrera
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarDirectorCarrera(){
		
		PersonaDto director  = null;
		
		try {
			director = servJdbcPersonaDto.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, erafCrrId);
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró Director de Carrera.");
		}
		
		return director;
	}
	
	private PersonaDto cargarDirectorPosgrado(){
		
		PersonaDto director  = null;
		
		try {
			director = servJdbcPersonaDto.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRPOSGRADO_VALUE, erafCrrId);
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró Director de Posgrado de la Facultad.");
		}
		
		return director;
	}

	/**
	 * Mètodo que permite cargar datos del secretario abogado de la facultada
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarSecretarioAbogado(){
		
		PersonaDto secretario  = null;
		
		if (erafCrrId != GeneralesConstantes.APP_ID_BASE) {
			CarreraDto carrera = null;
			
			try {
				carrera = servJdbcCarreraDto.buscarXId(erafCrrId);
			} catch (CarreraDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeInfo("No se encontró la Carrera solicitada.");
			}
			
			if (carrera != null) {
				try {
					secretario = servJdbcPersonaDto.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE, carrera.getCrrDpnId());
				} catch (PersonaDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (PersonaDtoNoEncontradoException e) {
					FacesUtil.mensajeInfo("No se encontró Secretario Abogado.");
				}	
			}

		}
		
		return secretario;
	}
	
	
	private List<PeriodoAcademicoDto> llenarHistorialMatricula(List<RecordEstudianteDto> recordSeleccion) {
		List<PeriodoAcademicoDto> periodos = new ArrayList<>();		
		
		Map<PeriodoAcademicoDto, List<RecordEstudianteDto>> mapMaterias = recordSeleccion.stream().collect(Collectors.groupingBy(RecordEstudianteDto::getRcesPeriodoAcademicoDto));
		mapMaterias.forEach((k,v)->{
			periodos.add(k);
			List<RecordEstudianteDto> materias = new ArrayList<>();
			k.setPracListRecordEstudianteDto(materias);
			v.forEach(it->{
				materias.add(it);
			});
		});
		
		periodos.sort(Comparator.comparing(PeriodoAcademicoDto::getPracDescripcion));

		return periodos;
	}
	
	
	private List<RecordEstudianteDto> llenarPeriodosAcademicos(List<RecordEstudianteDto> recordSeleccion) {
		
		List<RecordEstudianteDto> periodos = new ArrayList<>();	
		Map<String, RecordEstudianteDto> mapHorarios= new HashMap<String, RecordEstudianteDto>();
		for (RecordEstudianteDto periodo : recordSeleccion) {
			mapHorarios.put(periodo.getRcesPeriodoAcademicoDto().getPracDescripcion(), periodo);
		}
		for (Entry<String, RecordEstudianteDto> medico : mapHorarios.entrySet()) {
			periodos.add(medico.getValue());
		}
		periodos.sort(Comparator.comparing(RecordEstudianteDto::getRcesPeriodoAcademicoDto));

		return periodos;
	}
	
	// ****************************************************************/
	// *************** REPORTE APROBACION SUFICIENCIAS ****************/
	// ****************************************************************/
	
	public void buscarEstudiantesRegistrados(){
		if (!erafPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			try {
				erafListPersonaDto = servJdbcPersonaDto.buscarEstudiantesRegistrados(erafPeriodoId, erafDependenciaId);
				if (!erafListPersonaDto.isEmpty()) {
					erafMapReporteGeneral = new ArrayList<>();
					erafActivarReporte = GeneralesConstantes.APP_ID_BASE;
					
					for (PersonaDto item : erafListPersonaDto) {
						List<RecordEstudianteDto> historial = cargarHistorialAcademicoSAIUHomologado(item.getPrsIdentificacion());
						if (!historial.isEmpty()) {
							CarreraDto carrera = new CarreraDto(item.getCrrId(), item.getCrrDescripcion());
							carrera.setCrrProceso(item.getCrrProceso());
							setearReporte(item, carrera, historial);
						}
						
					}
					
					generarReporteAprobadosSuficiencias();
					
				}
			} catch (PersonaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		}else {
			FacesUtil.mensajeInfo("Seleccione un período académico para continuar.");
		}
	}
	
	
	private void setearReporte(PersonaDto estudiante, CarreraDto carrera, List<RecordEstudianteDto> historial){
		erafTipoArchivo = "XLSX";
		erafTokenServlet = "PREGRADO_ESTUDIANTES_SUFICIENCIAS";
		erafNombreJasper = "aprobacionSuficiencias";
		
		List<MallaCurricularNivel> creditosAcumuladosPorMalla = cargarMallaCurricularNivelPorCarrera(carrera.getCrrId());
		if (!creditosAcumuladosPorMalla.isEmpty()) {
			MallaCurricularNivel nivelMaximo = creditosAcumuladosPorMalla.stream().max(Comparator.comparing(MallaCurricularNivel::getMlcrnvCreditosAcumulado)).get();

			erafMapReporteGeneral.add(setearParametrosRecordAcademicoPregrado(estudiante, historial, carrera, nivelMaximo.getMlcrnvCreditosAcumulado()));

		}

	}
	
	private Map<String, Object> setearParametrosRecordAcademicoPregrado(PersonaDto estudiante, List<RecordEstudianteDto> historial, CarreraDto carrera, int totalHorasMalla) {

		Map<String, Object> retorno = new HashMap<String, Object>();
		
		retorno.put("facultad", estudiante.getDpnDescripcion());
		retorno.put("carrera", estudiante.getCrrDescripcion());
		retorno.put("identificacion", estudiante.getPrsIdentificacion());
		retorno.put("apellidos_nombres", estudiante.getPrsApellidosNombres());
		retorno.put("nivel", estudiante.getPrsNivelDto().getNvlNumeral().toString());
		
		List<RecordEstudianteDto> record = cargarRecordAcademicoCarreraSelecionada(historial, carrera.getCrrId());
		if (!record.isEmpty()) {
			long totalHorasAprobadasCarrera = 0;
			if (carrera.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
				totalHorasAprobadasCarrera = record.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) || item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrHoras()).sum();
			}else {
				totalHorasAprobadasCarrera = record.stream().filter(item -> (item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL) || item.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) || item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) && item.getRcesMateriaDto().getNumMatricula()==1).mapToInt(it-> it.getRcesMateriaDto().getMtrCreditos()).sum();
			}
			
			BigDecimal porcentaje = calcularPorcentajeMallaAprobada(totalHorasMalla, (int)totalHorasAprobadasCarrera);
			if (!porcentaje.equals(BigDecimal.ZERO) && porcentaje.floatValue() <=  new BigDecimal("100.00").floatValue()) {
				retorno.put("porcentaje_malla", getBigDecimal(porcentaje, 0));
			}else {
				retorno.put("porcentaje_malla", getBigDecimal( new BigDecimal("100.00"), 0));
			}
		}
		
		List<RecordEstudianteDto> recordCulturaFisica = cargarRecordAcademicoCarreraSelecionada(historial, CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE);
		RecordEstudianteDto suficienciaCulturaFisica = verificarEstadoSuficienciaActividadFisica(recordCulturaFisica);
		if (suficienciaCulturaFisica != null) {
			StringBuilder periodo = new StringBuilder();
			periodo.append("APROBADO");
			periodo.append(suficienciaCulturaFisica.getRcesPeriodoAcademicoDto() == null ? " ": " - " +suficienciaCulturaFisica.getRcesPeriodoAcademicoDto().getPracDescripcion());
			retorno.put("cultura_fisica", periodo.toString());
		}
		
		RecordEstudianteDto suficienciaInformatica = verificarEstadoSuficienciaInformatica(estudiante.getPrsIdentificacion(), CarreraConstantes.TIPO_PREGRADO_VALUE);
		if (suficienciaInformatica != null) {
			StringBuilder periodo = new StringBuilder();
			periodo.append("APROBADO");
			periodo.append(suficienciaInformatica.getRcesPeriodoAcademicoDto() == null ? " ": suficienciaInformatica.getRcesPeriodoAcademicoDto().getPracId() == 0 ? "" : " - " +suficienciaInformatica.getRcesPeriodoAcademicoDto().getPracDescripcion());
			retorno.put("informatica", periodo.toString());
		}
		
		List<RecordEstudianteDto> recordIdiomas = new ArrayList<>();
		List<RecordEstudianteDto> recordIngles = cargarRecordAcademicoCarreraSelecionada(historial, CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE);
		if (!recordIngles.isEmpty()) {
			recordIdiomas.addAll(recordIngles);
		}
		List<RecordEstudianteDto> recordFrances = cargarRecordAcademicoCarreraSelecionada(historial, CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE);
		if (!recordFrances.isEmpty()) {
			recordIdiomas.addAll(recordFrances);
		}
		List<RecordEstudianteDto> recordItaliano = cargarRecordAcademicoCarreraSelecionada(historial, CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE);
		if (!recordItaliano.isEmpty()) {
			recordIdiomas.addAll(recordItaliano);
		}
		List<RecordEstudianteDto> recordCoreano = cargarRecordAcademicoCarreraSelecionada(historial, CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE);
		if (!recordCoreano.isEmpty()) {
			recordIdiomas.addAll(recordCoreano);
		}
		List<RecordEstudianteDto> recordKichwa = cargarRecordAcademicoCarreraSelecionada(historial, CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE);
		if (!recordKichwa.isEmpty()) {
			recordIdiomas.addAll(recordKichwa);
		}
		
		RecordEstudianteDto suficienciaIdiomas = verificarEstadoSuficienciaIdiomas(recordIdiomas);
		if (suficienciaIdiomas != null) {
			StringBuilder periodo = new StringBuilder();
			periodo.append("APROBADO");
			periodo.append(" - " + suficienciaIdiomas.getRcesCarreraDto().getCrrDescripcion());
			periodo.append(suficienciaIdiomas.getRcesPeriodoAcademicoDto() == null ? " ": " - " +suficienciaIdiomas.getRcesPeriodoAcademicoDto().getPracDescripcion());
			retorno.put("idiomas", periodo.toString());
		}
		
		return retorno;
	}
	
	
	
	public void limpiarFormSuficiencias(){
		erafPeriodoId = GeneralesConstantes.APP_ID_BASE;
		erafDependenciaId = GeneralesConstantes.APP_ID_BASE;
		erafActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		erafListPersonaDto = new ArrayList<>();
		
		erafTipoArchivo = new String();
		erafTokenServlet = new String();
		erafNombreJasper = new String();
		
	}
	
	public void generarReporteAprobadosSuficiencias(){
		erafActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		if (!erafListPersonaDto.isEmpty()) {
			if (!erafMapReporteGeneral.isEmpty()) {
				cargarReporteGeneral(erafMapReporteGeneral);
				erafActivarReporte = 1;
			}
		}else {
			FacesUtil.mensajeError("No hay datos para generar el informe.");
		}
	}
	
	private void cargarReporteGeneral(List<Map<String, Object>> campos){

		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "ESTUDIANTES PREGRADO - SUFICIENCIAS";

		frmParametros = new HashMap<String, Object>();
		frmParametros.put("fecha", GeneralesUtilidades.getFechaActualSistema().toString());
		
		frmCampos = campos;
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	}
	
	
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS **************************/
	// ****************************************************************/

	public Usuario getErafUsuario() {
		return erafUsuario;
	}

	public void setErafUsuario(Usuario erafUsuario) {
		this.erafUsuario = erafUsuario;
	}

	public String getErafPrimerApellido() {
		return erafPrimerApellido;
	}

	public void setErafPrimerApellido(String erafPrimerApellido) {
		this.erafPrimerApellido = erafPrimerApellido;
	}

	public String getErafIdentificacion() {
		return erafIdentificacion;
	}

	public void setErafIdentificacion(String erafIdentificacion) {
		this.erafIdentificacion = erafIdentificacion;
	}

	public List<RecordEstudianteDto> getErafListRecordEstudianteDto() {
		return erafListRecordEstudianteDto;
	}

	public void setErafListRecordEstudianteDto(List<RecordEstudianteDto> erafListRecordEstudianteDto) {
		this.erafListRecordEstudianteDto = erafListRecordEstudianteDto;
	}


	public Integer getErafTipoUsuario() {
		return erafTipoUsuario;
	}

	public void setErafTipoUsuario(Integer erafTipoUsuario) {
		this.erafTipoUsuario = erafTipoUsuario;
	}

	public int getEcnfActivarReporte() {
		return ecnfActivarReporte;
	}

	public void setEcnfActivarReporte(int ecnfActivarReporte) {
		this.ecnfActivarReporte = ecnfActivarReporte;
	}

	public List<EstudianteJdbcDto> getErafListPersona() {
		return erafListPersona;
	}

	public void setErafListPersona(List<EstudianteJdbcDto> erafListPersona) {
		this.erafListPersona = erafListPersona;
	}

	public EstudianteJdbcDto getErafPersona() {
		return erafPersona;
	}

	public void setErafPersona(EstudianteJdbcDto erafPersona) {
		this.erafPersona = erafPersona;
	}

	public Integer getErafCrrId() {
		return erafCrrId;
	}

	public void setErafCrrId(Integer erafCrrId) {
		this.erafCrrId = erafCrrId;
	}

	public List<CarreraDto> getErafListCarreraDto() {
		return erafListCarreraDto;
	}

	public void setErafListCarreraDto(List<CarreraDto> erafListCarreraDto) {
		this.erafListCarreraDto = erafListCarreraDto;
	}

	public List<CarreraDto> getErafListCarreraDtoSecretaria() {
		return erafListCarreraDtoSecretaria;
	}

	public void setErafListCarreraDtoSecretaria(List<CarreraDto> erafListCarreraDtoSecretaria) {
		this.erafListCarreraDtoSecretaria = erafListCarreraDtoSecretaria;
	}

	public Boolean getErafAcceso() {
		return erafAcceso;
	}

	public void setErafAcceso(Boolean erafAcceso) {
		this.erafAcceso = erafAcceso;
	}

	public BigDecimal getErafPromedioGeneral() {
		return erafPromedioGeneral;
	}

	public void setErafPromedioGeneral(BigDecimal erafPromedioGeneral) {
		this.erafPromedioGeneral = erafPromedioGeneral;
	}

	public Integer getErafTipoBusqueda() {
		return erafTipoBusqueda;
	}

	public void setErafTipoBusqueda(Integer erafTipoBusqueda) {
		this.erafTipoBusqueda = erafTipoBusqueda;
	}

	public List<RecordEstudianteDto> getErafListRecordEstudianteDtoSeleccion() {
		return erafListRecordEstudianteDtoSeleccion;
	}

	public void setErafListRecordEstudianteDtoSeleccion(List<RecordEstudianteDto> erafListRecordEstudianteDtoSeleccion) {
		this.erafListRecordEstudianteDtoSeleccion = erafListRecordEstudianteDtoSeleccion;
	}

	public List<PeriodoAcademicoDto> getErafListPeriodoAcademicoDto() {
		return erafListPeriodoAcademicoDto;
	}

	public void setErafListPeriodoAcademicoDto(List<PeriodoAcademicoDto> erafListPeriodoAcademicoDto) {
		this.erafListPeriodoAcademicoDto = erafListPeriodoAcademicoDto;
	}

	public List<RecordEstudianteDto> getErafListRecordEstudianteDtoPeriodoDto() {
		return erafListRecordEstudianteDtoPeriodoDto;
	}

	public void setErafListRecordEstudianteDtoPeriodoDto(List<RecordEstudianteDto> erafListRecordEstudianteDtoPeriodoDto) {
		this.erafListRecordEstudianteDtoPeriodoDto = erafListRecordEstudianteDtoPeriodoDto;
	}

	public List<RecordEstudianteDto> getErafListRecordEstudianteDtoMateriaDto() {
		return erafListRecordEstudianteDtoMateriaDto;
	}

	public void setErafListRecordEstudianteDtoMateriaDto(List<RecordEstudianteDto> erafListRecordEstudianteDtoMateriaDto) {
		this.erafListRecordEstudianteDtoMateriaDto = erafListRecordEstudianteDtoMateriaDto;
	}

	public Boolean getErafAccesoCertificado() {
		return erafAccesoCertificado;
	}

	public void setErafAccesoCertificado(Boolean erafAccesoCertificado) {
		this.erafAccesoCertificado = erafAccesoCertificado;
	}

	public BigDecimal getErafPromedioInternado() {
		return erafPromedioInternado;
	}

	public void setErafPromedioInternado(BigDecimal erafPromedioInternado) {
		this.erafPromedioInternado = erafPromedioInternado;
	}

	public String getErafNombreReporte() {
		return erafNombreReporte;
	}

	public void setErafNombreReporte(String erafNombreReporte) {
		this.erafNombreReporte = erafNombreReporte;
	}

	public String getErafNombreArchivo() {
		return erafNombreArchivo;
	}

	public void setErafNombreArchivo(String erafNombreArchivo) {
		this.erafNombreArchivo = erafNombreArchivo;
	}

	public String[] getErafParametros() {
		return erafParametros;
	}

	public void setErafParametros(String[] erafParametros) {
		this.erafParametros = erafParametros;
	}

	public String getErafTituloPorNivelCarrera() {
		return erafTituloPorNivelCarrera;
	}

	public void setErafTituloPorNivelCarrera(String erafTituloPorNivelCarrera) {
		this.erafTituloPorNivelCarrera = erafTituloPorNivelCarrera;
	}

	public String getErafTituloPorNivelPeriodo() {
		return erafTituloPorNivelPeriodo;
	}

	public void setErafTituloPorNivelPeriodo(String erafTituloPorNivelPeriodo) {
		this.erafTituloPorNivelPeriodo = erafTituloPorNivelPeriodo;
	}

	public CarreraDto getErafCarreraDto() {
		return erafCarreraDto;
	}

	public void setErafCarreraDto(CarreraDto erafCarreraDto) {
		this.erafCarreraDto = erafCarreraDto;
	}

	public RecordEstudianteDto getErafPeriodoAcademicoDto() {
		return erafPeriodoAcademicoDto;
	}

	public void setErafPeriodoAcademicoDto(RecordEstudianteDto erafPeriodoAcademicoDto) {
		this.erafPeriodoAcademicoDto = erafPeriodoAcademicoDto;
	}

	public Integer getErafOpcionReporteId() {
		return erafOpcionReporteId;
	}

	public void setErafOpcionReporteId(Integer erafOpcionReporteId) {
		this.erafOpcionReporteId = erafOpcionReporteId;
	}

	public BigDecimal getErafPorcentajeMallaAprobado() {
		return erafPorcentajeMallaAprobado;
	}

	public void setErafPorcentajeMallaAprobado(BigDecimal erafPorcentajeMallaAprobado) {
		this.erafPorcentajeMallaAprobado = erafPorcentajeMallaAprobado;
	}

	public List<SelectItem> getErafListOpcionReporte() {
		return erafListOpcionReporte;
	}

	public void setErafListOpcionReporte(List<SelectItem> erafListOpcionReporte) {
		this.erafListOpcionReporte = erafListOpcionReporte;
	}

	public Integer getErafPeriodoId() {
		return erafPeriodoId;
	}

	public void setErafPeriodoId(Integer erafPeriodoId) {
		this.erafPeriodoId = erafPeriodoId;
	}

	public Integer getErafDependenciaId() {
		return erafDependenciaId;
	}

	public void setErafDependenciaId(Integer erafDependenciaId) {
		this.erafDependenciaId = erafDependenciaId;
	}

	public List<PeriodoAcademico> getErafListPeriodoAcademico() {
		return erafListPeriodoAcademico;
	}

	public void setErafListPeriodoAcademico(List<PeriodoAcademico> erafListPeriodoAcademico) {
		this.erafListPeriodoAcademico = erafListPeriodoAcademico;
	}

	public List<Dependencia> getErafListDependencia() {
		return erafListDependencia;
	}

	public void setErafListDependencia(List<Dependencia> erafListDependencia) {
		this.erafListDependencia = erafListDependencia;
	}

	public List<PersonaDto> getErafListPersonaDto() {
		return erafListPersonaDto;
	}

	public void setErafListPersonaDto(List<PersonaDto> erafListPersonaDto) {
		this.erafListPersonaDto = erafListPersonaDto;
	}

	public String getErafTipoArchivo() {
		return erafTipoArchivo;
	}

	public void setErafTipoArchivo(String erafTipoArchivo) {
		this.erafTipoArchivo = erafTipoArchivo;
	}

	public String getErafTokenServlet() {
		return erafTokenServlet;
	}

	public void setErafTokenServlet(String erafTokenServlet) {
		this.erafTokenServlet = erafTokenServlet;
	}

	public String getErafNombreJasper() {
		return erafNombreJasper;
	}

	public void setErafNombreJasper(String erafNombreJasper) {
		this.erafNombreJasper = erafNombreJasper;
	}

	public Integer getErafActivarReporte() {
		return erafActivarReporte;
	}

	public void setErafActivarReporte(Integer erafActivarReporte) {
		this.erafActivarReporte = erafActivarReporte;
	}

	



	
	
}
