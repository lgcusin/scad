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
   
 ARCHIVO:     RegistroHomologacionForm.java	  
 DESCRIPCION: Managed Bean que maneja el registro de las materias homologadas de los estudiantes.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
21-SEP-2017			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.homologacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoSistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoSistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.SistemaCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSistemaCalificacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteHomologacionForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;




/**
 * Clase (managed bean) RegistroHomologacionForm. Managed Bean que maneja el
 * registro de las materias homologadas de los estudiantes.
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "registroHomologacionForm")
@SessionScoped
public class RegistroHomologacionForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// Objetos
	// PANTALLA LISTAR ESTUDIANTES
	private Usuario rhfUsuario;
	private PersonaDto rhfPersonaDtoBuscar; // Guardar la cedula
	private CarreraDto rhfCarreraDtoBuscar; // Guardar la carrera
	private ParaleloDto rhfParaleloHomologacion;// paralelo unico de homologación por carrera.
	private PersonaDto rhfPersonaDtoEditar;
	private PeriodoAcademico rhfPeriodoAcademico;
	private MallaCurricular rhfMallaCurricular;
	private TipoSistemaCalificacion rhfTipoSistemaCalificacion;
	private SistemaCalificacion rhfSistemaCalificacion;
	private PlanificacionCronograma rhfPlanificacionCronograma;
	private PeriodoAcademico rhfPeriodoAcademicoActivo;
	private FichaEstudiante rhfFichaEstudiante;
	private Integer rhfValidadorClic;
	private PeriodoAcademico rhfPeriodoAcademicoInscripcion;
	private PeriodoAcademico rhfPeriodoAcademicoIdioma;
	private PeriodoAcademico rhfPeriodoAcademicoCulturaFisica;
	private UsuarioRol rhfUsuarioRol;
	private Rol rhfRol;
	private PlanificacionCronogramaDto rhfPlanificacionCronogramaDtoIntercambio;
	
	// Listas de Objetos
	private List<CarreraDto> rhfListCarreraDto; // Combo a seleccionar carreras
	private List<PersonaDto> rhfListPersonaDto; // Listado de personas
	private List<MateriaDto> rhfListMateriaDto;  //Lista de materias de la malla curricular.
	private List<ParaleloDto> rhfListParaleloDto; 
	private List<MateriaDto> rhfListHomologadosDto;//Lista de materias homologadas a guardar en BDD
	private List<MateriaDto> rhfListRecordTotal;
	private List<MateriaDto> rhfListRecordCarreraAnterior;
	private List<InstitucionAcademicaDto> rhfListUniversidadesDto;
	private List<TituloDto> rhfListTitulosUniversidadDto;
	private List<CarreraDto> rhfListCarrerasDtoTodas;
	private List<Rol> rhfListaRolesUsuario;
	
	//Subir archivo pdf
	private String rhfNombreArchivoAuxiliar;
	private String rhfNombreArchivoSubido;

	// bloquea opciones
	private Integer rhfBloqueoModal; // 1.- no 0.- si para evitar que recarguen  la página y se pueda generar la matricula una y otra vez
	//private boolean rhfBloqueaNumMatricula;  //Se usa para Posgrados
	private boolean rhfBloqueaCamposSegundaCrr;  //activa e inactiva campos de segunda carrera
	private boolean rhfBloqueaAnioReinicio;
	private boolean rhfBloqueaEstadoIngreso;
	private boolean rhfBloqueaCampoReingresoRediseno;
	private boolean rhfBloqueaBotonFinalizar;
	
	private boolean rhfBloqueaHomologacion;   //activa e inactiva opciones de homologacion (homologa o no)
	private boolean rhfBloqueaUbicacionIngles; //activa e inactiva opciones de ubicacion ingles
	private boolean rhfInactivaAprobarUbicacion;  //visible o no combo aprobar
	private boolean rhfBloqueaSubirArchivo; //activa e inactiva opcion de subir archivo
	private Integer rhfActivadorReporte;
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {

	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	// Para la busqueda

	@EJB
	PersonaDtoServicioJdbc servRhfPersonaDto;
	@EJB
	CarreraDtoServicioJdbc servRhfCarreraDto;
	@EJB
	PeriodoAcademicoServicio servRhfPeriodoAcademico;
	@EJB
	MallaCurricularServicio servRhfMallaCurricular;
	@EJB
	MateriaDtoServicioJdbc servRhfMateriaDto;
	@EJB
	ParaleloDtoServicioJdbc servRhfParaleloDto;
	@EJB
	TipoSistemaCalificacionServicio servRhfTipoSistemaCalificacion;
	@EJB
	SistemaCalificacionServicio servRhfSistemaCalificacion;
	@EJB
	PlanificacionCronogramaServicio servRhfPlanificacionCronograma;
	@EJB
	MatriculaServicio servRhfMatriculaServicio;
	@EJB
	CarreraServicio servRhfCarreraServicio;
	@EJB
	MateriaServicio servRhfMateriaServicio;
	@EJB
	MatriculaServicioJdbc servRhfRecordEstudianteSAU;
	@EJB
	RecordEstudianteDtoServicioJdbc servRhfRecordEstudianteSIIU;
	@EJB
	MateriaServicio servRhfMateriaSAU;
	@EJB
	InstitucionAcademicaDtoServicioJdbc servInstitucionesAcademicasDto;
	@EJB
	TituloDtoServicioJdbc servTitulosDtoUniversitarios;
	@EJB
	FichaEstudianteServicio servRhfFichaEstudiante;
	@EJB
	UsuarioRolServicio servRhfUsuarioRol;
	@EJB
	RolServicio servRhfRol;
	@EJB
	MallaCurricularParaleloDtoServicioJdbc servMallaCurricularParaleloDto;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servPlanificacionCronogramaDto;

	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	

	/**
	 * Método que permite ir a listar los estudiantes a homologar
	 * @param usuario   - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml listar estudiantes a homologar
	 */

	public String irAlistarEstudiante(Usuario usuario) {
		rhfUsuario = usuario;
		rhfBloqueoModal = 1;
		iniciarParametros();
		return "irAlistarEstudianteHomologacion";
	}

	/**
	 * Método para limpiar los parámetros de busqueda ingresados 
	 */
	public void limpiar() {
		iniciarParametros();
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instanciados al
	 * iniciar la funcionalidad
	 * 
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		limpiar();
		FacesUtil.limpiarMensaje();
		return "irInicio";
	}
	
	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
		try {
			rhfBloqueaCamposSegundaCrr= false; //No se presenta los campos rendered.
			rhfBloqueaAnioReinicio= false; //rendered No presenta por defecto
			rhfBloqueaHomologacion= false; //render  No se presnte chk homologacion.
			rhfBloqueaCampoReingresoRediseno= false; //render no se presenta
			rhfBloqueaBotonFinalizar= false;  //Por defecto se habilita el botón
			//rhfBloqueaPeriodoPosgrado= false; // No se presenta la lista de periodos posgrado
			rhfBloqueaUbicacionIngles= false;
			rhfBloqueaSubirArchivo= true;  // disabled presenta por defecto
			// Inicio los objetos
			rhfPersonaDtoBuscar = new PersonaDto();
			rhfPersonaDtoBuscar.setPrsIdentificacion("");
			rhfCarreraDtoBuscar = new CarreraDto();
			rhfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
			// seteo la carrera para que busque por todas
			rhfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			// inicio la lista de Personas
			rhfPersonaDtoEditar = null;
			rhfListPersonaDto=null;
			rhfListCarreraDto = new ArrayList<>();
			rhfListCarrerasDtoTodas= new ArrayList<>();	
			rhfNombreArchivoAuxiliar=null;
		    rhfNombreArchivoSubido=null;
		    rhfPeriodoAcademicoActivo= null;

		    rhfPeriodoAcademicoInscripcion= null;
			List<CarreraDto> listCarrerasDtoAux= new ArrayList<>();	 
			List<CarreraDto> listCarrerasDtoAuxIdioma= new ArrayList<>();
			List<CarreraDto> listCarrerasDtoAuxCulturaFisica= new ArrayList<>();
			// busco el periodo academico activo pregrado
			rhfPeriodoAcademicoActivo = servRhfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			// busco los periodo academico activo posgrado
		
			
		// busco los periodo academico activo Idioma
			rhfPeriodoAcademicoIdioma= servRhfPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			// busco los periodo academico activo Cultura Física
	    	rhfPeriodoAcademicoCulturaFisica=servRhfPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			// lleno las carreras/
			
		
			rhfListCarreraDto = servRhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(
					rhfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE,
					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rhfPeriodoAcademicoActivo.getPracId());
			
		
			if((listCarrerasDtoAux!=null)&&(listCarrerasDtoAux.size()>0)){
			rhfListCarreraDto.addAll(listCarrerasDtoAux); //agrego las carreras de posgrado
			}
	    	
			if(rhfPeriodoAcademicoIdioma!=null){
			//Busco carreras En Suficiencia idiomas
			listCarrerasDtoAuxIdioma = servRhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(
					rhfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE,
					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rhfPeriodoAcademicoIdioma.getPracId());
			}
			
			if((listCarrerasDtoAuxIdioma!=null)&&(listCarrerasDtoAuxIdioma.size()>0)){
			rhfListCarreraDto.addAll(listCarrerasDtoAuxIdioma); //Agrego las carreras de Suficiencia idiomas
			}
			
			if(rhfPeriodoAcademicoCulturaFisica!=null){
				//Busco carreras En Suficiencia Cultura física
			listCarrerasDtoAuxCulturaFisica = servRhfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPrac(
					rhfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE,
					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rhfPeriodoAcademicoCulturaFisica.getPracId());
			}
			
			if((listCarrerasDtoAuxCulturaFisica!=null)&&(listCarrerasDtoAuxCulturaFisica.size()>0)){
			rhfListCarreraDto.addAll(listCarrerasDtoAuxCulturaFisica);  //Agrego las carreras de suficiencia CCFF
			}
			
			if((rhfListCarreraDto!=null)&&(rhfListCarreraDto.size()>0)){
				
				// SOLO PREGRADO
//				rhfListPersonaDto = servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(
//					rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(),
//					GeneralesConstantes.APP_ID_BASE, FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE,
//					FichaInscripcionConstantes.INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,
//					rhfPersonaDtoBuscar.getPrsIdentificacion());
				
				//PREGRADO Y POSGRADO  SIN IMPORTAR EL PERIODO DE LA FICHA_INSCRIPCION				
				rhfListPersonaDto = servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcion(
						rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(),
						GeneralesConstantes.APP_ID_BASE, FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE,
						FichaInscripcionConstantes.INACTIVO_VALUE,
						rhfPersonaDtoBuscar.getPrsIdentificacion().trim());
				
			}else{
			
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.iniciarParametros.sin.carrera.validacion.exception")));
			}
			rhfValidadorClic = new Integer(0);
			rhfFichaEstudiante = null;
			
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.iniciarParametros.periodoAcademico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			//FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.iniciarParametros.personaDto.no.encontrado.exception")));
		}

	}

	/**
	 * Método para buscar las personas con los parámetros ingresados en los
	 * filtros de busqueda
	 */
	public void buscar() {
		// buscar los estudiantes por carrera y descripción
		try {
			rhfListPersonaDto = null;
//			rhfListPersonaDto = servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(
//					rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(),RolConstantes.ROL_ESTUD_PREGRADO_VALUE, 
//					FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE,
//					FichaInscripcionConstantes.INACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,
//					rhfPersonaDtoBuscar.getPrsIdentificacion());
			
			//PREGRADO Y POSGRADO  SIN IMPORTAR EL PERIODO DE LA FICHA_INSCRIPCION		
			rhfListPersonaDto = servRhfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcion(
					rhfListCarreraDto, rhfCarreraDtoBuscar.getCrrIdentificador(),
					GeneralesConstantes.APP_ID_BASE, FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE,
					FichaInscripcionConstantes.INACTIVO_VALUE,
					rhfPersonaDtoBuscar.getPrsIdentificacion().trim());
		} catch (PersonaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.buscar.personaDto.no.encontrado.exception")));
		}
	}

	/**
	 * Método para inicar el registro de homologación del estudiante seleccionado
	 * @return navegación a la pagina de registrar las homologaciones del estudiante
	 */
	public String irRegistrarHomologacion(PersonaDto rhfPersonaDto) {
		//VARIABLES
		rhfPersonaDtoEditar = rhfPersonaDto;
		rhfValidadorClic = 0;
		rhfBloqueaCamposSegundaCrr= false; //rendered:  oculto por defecto
		rhfBloqueaAnioReinicio=false;
		rhfBloqueaEstadoIngreso= true;  //disables :  Si por defecto
		rhfBloqueaHomologacion= false; //render: oculto por defecto 
		rhfBloqueaCampoReingresoRediseno= false;  //render oculto campos Reingreso con rediseño
		rhfBloqueaBotonFinalizar=false;   // Por defecto se habilita el boton
		//rhfBloqueaPeriodoPosgrado= false; // render, escondo la lista de periodos posgrado
		rhfBloqueaSubirArchivo= true;  // render, presenta por defecto
		rhfInactivaAprobarUbicacion=true;	//inactiva opcion de aprobar materia
		rhfBloqueaUbicacionIngles= false;
		rhfNombreArchivoAuxiliar=null;
	    rhfNombreArchivoSubido=null;
	    rhfPeriodoAcademicoInscripcion= null;
	    rhfRol= null;
	    rhfUsuarioRol= null;
	    rhfPlanificacionCronogramaDtoIntercambio= null;
	  
	    //LISTAS
	    rhfListMateriaDto= new ArrayList<>();
	    rhfListCarrerasDtoTodas= new ArrayList<>();
		rhfListHomologadosDto = new ArrayList<MateriaDto>();
		rhfListRecordTotal = new ArrayList<>();
		rhfListRecordCarreraAnterior = new ArrayList<MateriaDto>();
		rhfListaRolesUsuario = null;
		
		//SETEO DE PARAMETROS
		rhfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
		rhfPersonaDtoEditar.setFcesTipoUnivEstudprev(GeneralesConstantes.APP_ID_BASE);
		rhfPersonaDtoEditar.setFcinAnioAbandonaCarrera(null);
		rhfPersonaDtoEditar.setFcesEstadoEstudprev(GeneralesConstantes.APP_ID_BASE);
		rhfPersonaDtoEditar.setFcesRegTituloPrev(null);
		rhfPersonaDtoEditar.setFcesTitEstudPrevId(GeneralesConstantes.APP_ID_BASE);
		rhfPersonaDtoEditar.setFcesUnivEstudPrevId(GeneralesConstantes.APP_ID_BASE);
		rhfPersonaDtoEditar.setFcinCrrAnteriorId(GeneralesConstantes.APP_ID_BASE);
		rhfPersonaDtoEditar.setFcinPeriodoPosgradoId(GeneralesConstantes.APP_ID_BASE);
		//LISTAS AUXILIARES
		List<MateriaDto> listMateriasMallaAux = new ArrayList<MateriaDto>();
		List<RecordEstudianteDto> recordEstudianteSIIUAux = new ArrayList<>();
		List<RecordEstudianteSAUDto> recordEstudianteSauAux = new ArrayList<>();		    				
		//OBJETOS AUXILIARES
		Carrera carreraAux = new Carrera(); 
		
		//BUSCO LA LISTA DE ROLES DEL USUARIO
		rhfListaRolesUsuario=servRhfRol.listarRolesXUsrId(rhfUsuario.getUsrId());	
		if(rhfListaRolesUsuario!=null){
			boolean rolEncontrado= false;
		  //BUSCO EL ROL DE SECRETARIA DE CARRERA
			for (Rol rol : rhfListaRolesUsuario) {
			if(rol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE || rol.getRolId()==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE || rol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE){
				
				try {
					rhfRol=servRhfRol.buscarPorId(rol.getRolId());
				} catch (RolNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.rol.no.encontrado.exception")));
					FacesUtil.mensajeError("No se encontró rol al buscar por id");
					return null;
				} catch (RolException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.rol.exception")));
					FacesUtil.mensajeError("Error desconocido al buscar rol por id");
					return null;
				}
				rolEncontrado = true;
				break;
			}
		}
		
		
		if(!rolEncontrado){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.rol.validacion.exception")));
			return null;
		  }
		
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.sin.rol.validacion.exception")));
			return null;
			
		}
	
		       
		try {
			
			//Busco el usuario rol del usuario
			rhfUsuarioRol= servRhfUsuarioRol.buscarXUsuarioXrol(rhfUsuario.getUsrId(), rhfRol.getRolId());
			 //Listo las carreras migradas del SIAC, para que seleccione  como carrera anterior al cambiar de carrera
		//	rhfListCarrerasDtoTodas=servRhfCarreraDto.listarTodosXCrrProceso(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE);
			
			if((rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
				||(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE)){
			//LISTO TODAS LAS CARRERA EXISTENTES EN EL SIIU; PARA QUE SE SELECCIONE LA CARRERA ANTERIOR AL CAMBIAR DE CARRERA
			rhfListCarrerasDtoTodas=servRhfCarreraDto.listarCarrerasTodas();
			}		
			// proceso para buscar la planificacion cronograma activa
			// creo la lista de proceso flujo correspondiente a la matricula
			List<Integer> listProcesoFlujoAux = new ArrayList<Integer>();
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			
			// Busqueda del período academico HOMOLOGACION
			rhfPeriodoAcademico = servRhfPeriodoAcademico.buscarPDescripcion(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_LABEL);
			
			// Busqueda del objeto periodo academico de la fichaInscripcion del estudiante, por el id_prac de la fcin
			if(rhfPersonaDto.getFcinPeriodoAcademico()!=null){
			rhfPeriodoAcademicoInscripcion= servRhfPeriodoAcademico.buscarPorId(rhfPersonaDto.getFcinPeriodoAcademico());
			}
			// listo planificaciones cronograma de matriculas

			// TODO: VERIFICAR EL TIPO DE CRONOGRAMA
			List<PlanificacionCronograma> listPlanificacionCronogramaAux;
			listPlanificacionCronogramaAux = servRhfPlanificacionCronograma
					.buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(rhfPeriodoAcademico.getPracId(),
							listProcesoFlujoAux, CronogramaConstantes.TIPO_HOMOLOGACION_VALUE);
			
			
			// validacion de las fechas del cronograma
			int cont = 0;
			int contNumPlanificacion = 0;
			for (PlanificacionCronograma item : listPlanificacionCronogramaAux) {
				contNumPlanificacion++;
				if (item.getPlcrEstado() == PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE) {
					rhfPlanificacionCronograma = item;
					cont++;
				}
				if (cont == 0 && contNumPlanificacion == listPlanificacionCronogramaAux.size()) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.sin.planificacion.cronograma.validacion.exception")));
					return null;
				}
			}

			
		 	//BUSCO LA CARRERA CON crr_id de la ficha Inscripcion
            carreraAux = servRhfCarreraServicio.buscarPorId(rhfPersonaDtoEditar.getCrrId());
			
			// Buscar la malla curricular vigente y activa de la carrera
			rhfMallaCurricular = servRhfMallaCurricular.buscarXcarreraXvigenciaXestado(rhfPersonaDtoEditar.getCrrId(),MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			
			//Busco materias de toda la malla
		//	listMateriasMallaAux =   servRhfMateriaDto.listarXmalla(rhfMallaCurricular.getMlcrId());
			//SOLO SE HOMOLGA MATERIAS PADRE (MODULAR) EN CASO DE MODULARES Y MATERIAS NORMALES
			listMateriasMallaAux =   servRhfMateriaDto.listarXmallaSinAsignaturasModulo(rhfMallaCurricular.getMlcrId());
			
			//CARGO PARARLELO HOMOLOGACION A TODAS LAS MATERIAS DE LA MALLA
			for (MateriaDto itmMateria : listMateriasMallaAux) {
								
				
				//BUSCO EL PARALELO HOMOLOGACiÓN  o PARALELO HISTORICO  SEGUN EL CASO
				if((carreraAux.getCrrTipo()==CarreraConstantes.TIPO_PREGRADO_VALUE)||(carreraAux.getCrrTipo()==CarreraConstantes.TIPO_SUFICIENCIA_VALUE)){
				rhfParaleloHomologacion = servRhfParaleloDto.buscarXmallaMateriaXperiodoXDescripcion(
						itmMateria.getMlcrmtId(), PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE,ParaleloConstantes.PARALELO_HOMOLOGACION_LABEL);
									
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.carrera.sin.tipo.correcto.validacion.exception")));
					return null;
					
				}
				itmMateria.setPrlId(rhfParaleloHomologacion.getPrlId()); //Se agrega a cada materia el paralelo homologación
				itmMateria.setPrlDescripcion(rhfParaleloHomologacion.getPrlDescripcion());
				itmMateria.setMlcrprId(rhfParaleloHomologacion.getMlcrprId());
				itmMateria.setEsHomologado(false);
				// seteo el numero de matricula a primera
				itmMateria.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
				// asignar valores por defecto de las notas
				itmMateria.setNotaUno(null);
				itmMateria.setNotaDos(null);
				itmMateria.setNotaTres(null);
				itmMateria.setClfPromedioAsistencia(null);
				itmMateria.setNotaSuma(null);
				itmMateria.setAprobado(false);
				itmMateria.setMtrCmbEstado(true);
				itmMateria.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
				
				//Campos para segunda registro de notas
				itmMateria.setEsHomologadoReg2(false);
				itmMateria.setNotaSumaReg2(null);
				itmMateria.setClfPromedioAsistenciaReg2(null);
				itmMateria.setMtrCmbEstado(Boolean.TRUE);
				itmMateria.setNumMatriculaReg2(GeneralesConstantes.APP_ID_BASE);
				itmMateria.setEstadoHomologacionReg2(GeneralesConstantes.APP_ID_BASE);
				itmMateria.setRcesModoAprobacionNota1(GeneralesConstantes.APP_ID_BASE);
				itmMateria.setRcesModoAprobacionNota2(GeneralesConstantes.APP_ID_BASE);
				itmMateria.setRcesComboModoAprobacion1(Boolean.TRUE);//Por defecto dehabilito combo de modo de aprobacion 1
				itmMateria.setRcesComboModoAprobacion2(Boolean.TRUE); //Por defecto dehabilito combo de modo de aprobacion 2
				itmMateria.setRcesObservacion(null);
			}

		        
			
			       //BUSCO RECORD EN SIIU, EN CARRERA A LA QUE INGRESA
		            recordEstudianteSIIUAux=servRhfRecordEstudianteSIIU.buscarXidentificacionXcarrera(rhfPersonaDtoEditar.getPrsIdentificacion(), carreraAux.getCrrId());
		
		           //BUSCO RECORD EN SAU, EN LA CARRERA A LA QUE INGRESA
		        	if(carreraAux.getCrrEspeCodigo()!=null){
		            recordEstudianteSauAux=servRhfRecordEstudianteSAU.buscarRecordAcademicoSAUconAnulados(rhfPersonaDtoEditar.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
			         }
			
			
		        	//AGREGO LA LISTA DE RECORD SIIU A RECORD TOTAL
			if((recordEstudianteSIIUAux!=null)&&(recordEstudianteSIIUAux.size()>0))	{	
		    	for (RecordEstudianteDto recordEstudianteSiiu : recordEstudianteSIIUAux) {
			//	if (recordEstudianteSiiu.getRcesEstado()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
					MateriaDto materiaDtoAux= new MateriaDto();
					 Materia materiaAux = new Materia();
					 try {
						materiaAux= servRhfMateriaServicio.buscarPorId(recordEstudianteSiiu.getMlcrmtMtrId()) ;
					} catch (MateriaNoEncontradoException e) {
						materiaAux= null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.materia.no.encontrada.exception")));
	
					} catch (MateriaException e) {
						materiaAux= null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.materia.exception")));
					}
					
					 if(materiaAux!=null){
					 materiaDtoAux.setMtrId(materiaAux.getMtrId());
					 materiaDtoAux.setMtrCodigo(materiaAux.getMtrCodigo());
					 materiaDtoAux.setMtrDescripcion(materiaAux.getMtrDescripcion());
				     rhfListRecordTotal.add(materiaDtoAux);
					 }
			//	}
				
			   } //fin for
			
		    }	
			//AGREGO LA LISTA DE RECORD SAU A RECORD TOTAL
				if((recordEstudianteSauAux != null)&&(recordEstudianteSauAux.size()>0)) {
					for (RecordEstudianteSAUDto recordEstudianteSau : recordEstudianteSauAux) {
						// if (recordEstudianteSau.getEstado()==SAUConstantes.MATERIA_APROBADA_VALUE){
						MateriaDto materiaDtoAux2 = new MateriaDto();
						Materia materiaAux2 = new Materia();
						try {
							materiaAux2 = servRhfMateriaSAU.buscarXCodigoXEspeCodigo(
									recordEstudianteSau.getCodigoMateria(), carreraAux.getCrrEspeCodigo());
						} catch (MateriaValidacionException e) {
							materiaAux2 = null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.materia.sau.validacion.exception")));
						//	FacesUtil.mensajeError("No existe la materia al buscar por el record estudiante, Sau");
						} catch (MateriaException e) {
							materiaAux2 = null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.materia.sau.exception")));
						//	FacesUtil.mensajeError("Error desconocido al buscar por el record estudiante, Sau");
						}

						if (materiaAux2 != null) {
							materiaDtoAux2.setMtrId(materiaAux2.getMtrId());
							materiaDtoAux2.setMtrCodigo(materiaAux2.getMtrCodigo());
							materiaDtoAux2.setMtrDescripcion(materiaAux2.getMtrDescripcion());
							rhfListRecordTotal.add(materiaDtoAux2);
						}
						// }
					}
			
			}
			// AGREGO  a la lista de materias para homologar las que no esten tomdas(EN EL RECORD)
//			boolean encontrado=false; 
//			for (MateriaDto MateriaDtoMalla: listMateriasMallaAux) {
//				for (MateriaDto MateriaAprobada : rhfListRecordTotal) {
//					if(MateriaAprobada.getMtrId()==MateriaDtoMalla.getMtrId()){
//						encontrado= true;
//						break;
//					}
//				}
//				
//				if(encontrado==false){
//					rhfListMateriaDto.add(MateriaDtoMalla);
//				}
//				encontrado=false;
//			}
			
				
				/*
				 * BLOQUE DE CONTROL DE OPCIONES EN LA VISTA
				 */
				
				if ((rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
						|| (rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE)
						||(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE)) {
					
					  if(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
						  rhfBloqueaCampoReingresoRediseno= true; //Se presenta el campo para seleccionar carrera anterior
						 
//						  rhfBloqueaEstadoIngreso= false;  //Debe elegir entre homologa o no homologa  --cambiado el 9 Ago 2018
//						  rhfBloqueaHomologacion= false; //Por defecto se bloque el check --cambiado el 9 Ago 2018
						  
						  rhfBloqueaEstadoIngreso= true; //Se bloquea el combo estado Ingreso solo homologa
						  rhfBloqueaHomologacion= true; //Se habilita lista de checks Rendered
						  rhfPersonaDtoEditar.setFcinEstadoIngreso(FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE); //HOMOLOGA  POR DEFECTO
						  
					  }else{
							rhfPersonaDtoEditar.setFcinEstadoIngreso(FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE); //HOMOLOGA  POR DEFECTO
							rhfBloqueaEstadoIngreso= true; //Se bloquea el combo estado Ingreso solo homologa
						  rhfBloqueaHomologacion= true; //Se habilita lista de checks Rendered
					  }
						  
					
					
				}else if (rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
					      
					       rhfPersonaDtoEditar.setFcinEstadoIngreso(FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE); //NO HOMOLOGA
						    rhfBloqueaEstadoIngreso= true;//Se bloquea el combo estado Ingreso
				    	  
			   }else if ((rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)){
				  
				  // rhfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
				   // rhfBloqueaEstadoIngreso= false;//Se habilita combo para seleccionar homologa o no		

				   rhfBloqueaAnioReinicio= true; //Se presenta el txt para ingreso del año que dejo la carrera  
				   
				   rhfPersonaDtoEditar.setFcinEstadoIngreso(FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE); //NO HOMOLOGA  //Se cambia 5 feb 2019
				    rhfBloqueaEstadoIngreso= true;//Se bloquea el combo estado Ingreso //Se cambia 5 feb 2019
				    
			   }else if(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
				  
				   rhfListUniversidadesDto= servInstitucionesAcademicasDto.listarXNivel(InstitucionAcademicaConstantes.NIVEL_UNIVERSIDAD_VALUE); 
				   rhfListTitulosUniversidadDto=servTitulosDtoUniversitarios.listarXTipo(TituloConstantes.TIPO_TERCER_NIVEL_VALUE); 
				   rhfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
				   rhfBloqueaCamposSegundaCrr= true; //Se presentan campos para registrar segunda carrera
				   rhfBloqueaEstadoIngreso= false; //Se habilita combo para seleccionar homologa o no
				   
			   }else if(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
				   
				   rhfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
				   rhfBloqueaCampoReingresoRediseno= true; //Se presenta el campo para seleccionar carrera anterior
				   rhfBloqueaEstadoIngreso= false;	//Se habilita combo para seleccionar homologa o no
				   
			   } else if(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
				    
				   rhfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
				    rhfBloqueaEstadoIngreso= false; //Se habilita combo para seleccionar homologa o no
														
			   }else if (rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE){
				      
			       rhfPersonaDtoEditar.setFcinEstadoIngreso(FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE); //NO HOMOLOGA
				    rhfBloqueaEstadoIngreso= true;  //Se bloquea el combo estado Ingreso
		    	  
	          } else if((rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
	        	  ||(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE)){
				   rhfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
				    rhfBloqueaEstadoIngreso= false; //disabled-Se habilita combo para seleccionar homologa o no
				    //rhfBloqueaSubirArchivo= false;	//render- se oculta
				    
			   }else{
				   FacesUtil.limpiarMensaje();
				   FacesUtil.mensajeError("El tipo de ingreso  del estudiante no es valido");
				   rhfBloqueaBotonFinalizar= true; //bloqueo el boton finalizar
			   }
				
				
			//ALTERNO:  COPIO  LA MALLA COMPLETA SIN RETIRAR MATERIAS TOMADAS POR EL ESTUDIANTE
		if(listMateriasMallaAux!=null){
			for (MateriaDto materiaDtoMalla : listMateriasMallaAux) {
				rhfListMateriaDto.add(materiaDtoMalla);
			}
		}	
			
			
			// buscar el tipo de sistema de calificaciones por la descripcion :"PREGRADO"
			rhfTipoSistemaCalificacion = servRhfTipoSistemaCalificacion.buscarXDescripcion(TipoSistemaCalificacionConstantes.SISTEMA_CALIFICACION_PREGRADO_LABEL);
			
			// buscar el sistema de calificaciones por el periodo academiico y por el tipo sistema de calificaciones
			rhfSistemaCalificacion = servRhfSistemaCalificacion.listarSistemaCalificacionXPracXtissclXEstado(
					rhfPeriodoAcademico.getPracId(), rhfTipoSistemaCalificacion.getTissclId(),
					SistemaCalificacionConstantes.INACTIVO_VALUE);
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.periodoAcademico.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.malla.curricular.no.encontrado.exception")));
			return null;
		} catch (MallaCurricularException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
     		FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.materiaDto.no.encontrado.exception")));
			return null;
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.ParaleloDto.no.encontrado.exception")));
			return null;
		} catch (TipoSistemaCalificacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.tipo.sistema.calificacion.no.encontrado.exception")));
			return null;
		} catch (TipoSistemaCalificacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (SistemaCalificacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.sistema.calificacion.no.encontrado.exception")));
			return null;
		} catch (SistemaCalificacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.planificacion.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
			
		} catch (RecordEstudianteException e) {
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
        } catch (RecordEstudianteDtoException e)	{
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			//MO devuelve exception al buscar record SAU
			return null;
		} catch (CarreraNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} catch (CarreraException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} catch (InstitucionAcademicaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (InstitucionAcademicaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (TituloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (TituloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}catch (CarreraDtoJdbcException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.carreraDto.exception")));
			return null;
		} 	catch (UsuarioRolException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.UsuarioRol.exception")));
			return null;
		} catch (UsuarioRolNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.usuarioRol.no.encontrado.exception")));
			return null;
		}
		
		// Me dirijo a JSF diferente de acuerdo al tipo de Ingreso: Idiomas, cultura Fisica o Movilidad  	  
   	  if(rhfPersonaDto.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE){
    	 return "irRegistrarUbicacion"; //vista para registrar ubicación del nivel de ingles.
    	 
     }else if((rhfPersonaDtoEditar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
    		 ||(rhfPersonaDtoEditar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE)
    		 ||(rhfPersonaDtoEditar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
    		 ||(rhfPersonaDtoEditar.getFcinTipoIngreso()== FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE)
    		 ||(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE)
    		 ){  //MQ: Pedido de retirar opción de segunda Carrera UCE-DA- 2019-1734-O 9 jul 2019
    	         //MQ: Por pedido de Producción se quita la opcion de reinicio 26 jul 2019 
		
    	 return "irRegistrarHomologacion"; // vista para registrar homologaciones
		
     }else if((rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
    		 ||(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE) ){
    	 return "irRegistrarSufCulturaFisica"; 
     }else{
    	FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.irRegistrarHomologacion.tipo.ingreso.validacion.exception")));
		return null;
     }
     
	}

		
	/**
	 * Método para inicar el registro del notas de la materias homologadas del estudiante en BDD.
	 * @return navegación a la pagina xhtml. Listar estudiantes a homologar.
	 */
	public String guardarHomologacion() {
		
			rhfValidadorClic = new Integer(0);
			try {
				String extension = GeneralesUtilidades.obtenerExtension(rhfNombreArchivoSubido);
				String rutaNombre = null;
				String rutaTemporal = null;
				
				if(servRhfMatriculaServicio.generarMatriculaHomologacion(rhfListHomologadosDto, rhfPersonaDtoEditar, 0, 0,GeneralesConstantes.APP_CERO_VALUE, rhfPlanificacionCronograma
						, rhfNombreArchivoSubido, rhfFichaEstudiante, rhfPlanificacionCronogramaDtoIntercambio,rhfUsuarioRol )){
				if(rhfNombreArchivoSubido != null){
							rutaNombre = rhfPersonaDtoEditar.getPrsIdentificacion()+"-"+rhfPersonaDtoEditar.getFcinId()+ "." + extension;
							rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rhfNombreArchivoSubido;
//							String smbUrl ="smb://produ:12345.a@10.20.1.63/siiuArch/archivos/" +GeneralesConstantes.APP_PATH_ARCHIVOS_HOMOLOGACIONES+ rutaNombre;
							GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_HOMOLOGACIONES+ rutaNombre);
						rutaNombre = null;
						rutaTemporal = null;
					
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarHomologacion.con.exito.exception")));
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarHomologacion.sin.exito.exception")));
				}
				
			} catch (MatriculaValidacionException e) {
				//e.printStackTrace();
				// validador click en 0
				rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			} catch (MatriculaException e) {
				// validador click en 0
				rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			} catch (FileNotFoundException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarHomologacion.guardar.archivo.exception")));
			}  
			iniciarParametros();
		return "irAlistarEstudianteHomologacion";
	}

	
	/**
	 * Método para inicar el registro del notas de la materias homologadas del estudiante en BDD.
	 * @return navegación a la pagina xhtml. Listar estudiantes a homologar.
	 */
	public String guardarSufCulturaFisica() {
		
			rhfValidadorClic = new Integer(0);
			try {
		
				
				if(servRhfMatriculaServicio.generarMatriculaHomologacionSufCulturaFisica(rhfListHomologadosDto, rhfPersonaDtoEditar, 0, 0,GeneralesConstantes.APP_CERO_VALUE, rhfPlanificacionCronograma,rhfUsuarioRol)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarSufCulturaFisica.con.exito.exception")));
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarSufCulturaFisica.sin.exito.exception")));
				}
				
			} catch (MatriculaValidacionException e) {
				//e.printStackTrace();
				// validador click en 0
				rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			} catch (MatriculaException e) {
				// validador click en 0
				rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			} 
			iniciarParametros();
		return "irAlistarEstudianteHomologacion";
	}
	
	
	/**
	 * Método para inicar el registro de los niveles aprobados del estudiante en idiomas, en la BDD.
	 * @return navegación a la pagina xhtml. Listar estudiantes a homologar.
	 */
	public String guardarUbicacionIdiomas() {
			rhfValidadorClic = new Integer(0);
			
			try {
				
				if(servRhfMatriculaServicio.generarMatriculaUbicacion(rhfListHomologadosDto, rhfPersonaDtoEditar, 0, 0,GeneralesConstantes.APP_CERO_VALUE, rhfPlanificacionCronograma, rhfNombreArchivoSubido, rhfFichaEstudiante, rhfUsuarioRol)){
				//Si se cargo el archivo, no es obligatorio para idiomas
					if(rhfNombreArchivoSubido != null){
						String rutaNombre = null;
						String rutaTemporal = null;
						String extension = GeneralesUtilidades.obtenerExtension(rhfNombreArchivoSubido);
							rutaNombre = rhfPersonaDtoEditar.getPrsIdentificacion()+"-"+rhfPersonaDtoEditar.getFcinId()+ "." + extension;
							rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rhfNombreArchivoSubido;
//							String smbUrl = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_HOMOLOGACIONES+ rutaNombre;
							GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_APELACION_TERCERA_MATRICULA+ rutaNombre);
//							GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),smbUrl );
						rutaNombre = null;
						rutaTemporal = null;
					
				    }
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarUbicacionIdiomas.con.exito.exception")));
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarUbicacionIdiomas.sin.exito.exception")));
				}
				
			} catch (MatriculaValidacionException e) {
				// validador click en 0
				rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
				
			} catch (MatriculaException e) {
				// validador click en 0
				rhfValidadorClic = new Integer(0);
				FacesUtil.mensajeError(e.getMessage());
			} catch (FileNotFoundException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.guardarUbicacionIdiomas.guardar.archivo.exception")));
			}  
			iniciarParametros();
		
		
		return "irAlistarEstudianteHomologacion";
	}
	
		

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	/**
	 * Método cancelar el registro de homologación 
	 * @return navegación a la pagina de listar estudiantes a homologar.
	 */
	public String cancelarHistorico() {
		rhfPlanificacionCronograma = new PlanificacionCronograma();
		rhfPeriodoAcademico = new PeriodoAcademico();
		rhfMallaCurricular = new MallaCurricular();
		rhfListMateriaDto = null;
		rhfListParaleloDto = null;
		rhfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
		rhfTipoSistemaCalificacion = new TipoSistemaCalificacion();
		rhfSistemaCalificacion = new SistemaCalificacion();
		rhfValidadorClic = new Integer(0);
		rhfPersonaDtoEditar= null;
		return "irAlistarEstudianteHomologacion";
	}

	/**
	 * Verifica información al hacer click en el botón generar matrícula
	 */
	public String verificarClickRegistrarHomologacion() {
		//OBJETOS
		rhfFichaEstudiante = new FichaEstudiante();
		//LISTAS
		rhfListHomologadosDto = new ArrayList<>();
		rhfListRecordCarreraAnterior = new ArrayList<MateriaDto>();
		rhfPlanificacionCronogramaDtoIntercambio= null;
	    //VARIABLE
		rhfValidadorClic = 0;
		//AUXILIARES
	    boolean datosCompletos = false;  //verifica que los datos esten completos, de acuerdo a cada tpo de ingreso
	    boolean ingresoConDatosRediseno = false;
	    boolean ingresoConDatos= false;
	    boolean archivoSubido=false;
	 
		Carrera carreraAux2 = null;
		List<FichaEstudiante> auxListaFichasEstudiante = new ArrayList<>();
		List<RecordEstudianteDto> recordSIIUAux = new ArrayList<>();
		List<RecordEstudianteSAUDto> recordSAUAux = new ArrayList<>();
	   //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION EN LA CARRERA, DEPENDE DEL TIPO DE INGRESO PARA QUE PUEDA CONTINUAR
		 try {
			auxListaFichasEstudiante = servRhfFichaEstudiante.listarPorFcinId(rhfPersonaDtoEditar.getFcinId());
			
			if(auxListaFichasEstudiante==null||auxListaFichasEstudiante.size()<=0){//NO TIENE FCES en la carrera
				rhfFichaEstudiante= null;
				
		    	}else if(auxListaFichasEstudiante.size()==1){ //TIENE UNA FICHA ESTUDIANTE
			
			    	if((rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
						||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)
						||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE)
						
						){
					     try {
						   rhfFichaEstudiante = servRhfFichaEstudiante.buscarPorId(auxListaFichasEstudiante.get(0).getFcesId());
					 
					     } catch (FichaEstudianteNoEncontradoException e) {
						rhfFichaEstudiante = null;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.fichaEstudiante.no.encontrado.exception")));
						return null;
					      }
			        }else  if((rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
							||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE )
							||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)
							||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE )
							||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE)
							){
			        //CAMBIO DE CARRERA, UNIVERSIDAD, SEGUNDA CRR  NO DEBEN TENER FCES EN LA CARRERA.
			        	
			        	
			        	rhfFichaEstudiante = null;
			        	
				    FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.fichaEstudiante.validacion.exception")));
				    return null;
			       }
				
				
			    }else{ //TIENE MAS DE UNA FCES EN LA CARRERA
				rhfFichaEstudiante = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.varias.fichaEstudiante.validacion.exception")));
				return null;
			   }
		} catch (FichaEstudianteException e1) {
			rhfFichaEstudiante = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.fichaEstudiante.exception")));
			return null;
		}
		
		 //BUSCO PLANIFICACION CRONOGRAMA EN PERIODO ACTUAL PARA INTERCAMBIOS
		 if(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE){
			 
			 try {
				rhfPlanificacionCronogramaDtoIntercambio= servPlanificacionCronogramaDto.buscarXProcesoFlujoXTipoCrnXPeriodo(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE, CronogramaConstantes.TIPO_ACADEMICO_VALUE, rhfPeriodoAcademicoActivo.getPracId());
			} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.planificacion.cronogramano.no.encontrado.exception")));
				return null;
			} catch (PlanificacionCronogramaDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.planificacion.cronogramano.exception")));
				return null;
			}
			
		 }
		 
		 //REVISO QUE SEGUN EL CASO LOS DATOS ESTEN COMPLETOS
		if(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
			if((rhfPersonaDtoEditar.getFcesTipoUnivEstudprev()==GeneralesConstantes.APP_ID_BASE)
					||(rhfPersonaDtoEditar.getFcesEstadoEstudprev()==GeneralesConstantes.APP_ID_BASE)
					||(rhfPersonaDtoEditar.getFcesUnivEstudPrevId()==GeneralesConstantes.APP_ID_BASE)
					){
				datosCompletos= false;
			}else {
				datosCompletos= true;
			}
			
			if(rhfPersonaDtoEditar.getFcesEstadoEstudprev()==FichaEstudianteConstantes.ESTADO_ESTUD_PREV_TITULADO_VALUE){
				if((rhfPersonaDtoEditar.getFcesTitEstudPrevId()==GeneralesConstantes.APP_ID_BASE)||(rhfPersonaDtoEditar.getFcesRegTituloPrev()==null)
						||(rhfPersonaDtoEditar.getFcesRegTituloPrev().equals(" "))||(rhfPersonaDtoEditar.getFcesRegTituloPrev().trim().length()==0)){
					datosCompletos= false;
					
				}
				
			}
			
			
		}else if(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){//Otros tipos de ingreso
			if(rhfPersonaDtoEditar.getFcinAnioAbandonaCarrera()!=null ){
				
				Calendar cal= Calendar.getInstance();
				int anioActual= cal.get(Calendar.YEAR);
				
				if(anioActual>rhfPersonaDtoEditar.getFcinAnioAbandonaCarrera()){
				if((anioActual-rhfPersonaDtoEditar.getFcinAnioAbandonaCarrera())>5){
					datosCompletos= true; //datos completos para este caso
				}else{					
					
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Para considerarse un registro como reinicio debio transcurrir mas de 5 años desde el ultimo periodo de estudio.");
					return null;
				
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El año ingresado es mayor al año actual:  "+anioActual);
				return null;
				
			}
				
			}else{
				
				datosCompletos= false;
				
			}	
			
		}else{	
			
			datosCompletos= true;// no necesita seleccionar el tipo de universidad
			}
		
		
	//REINGRESO  DEBE TENER RECORD EN LA CARRERA A LA QUE INGRESA
    	if((rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
    			||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE) 
    			||(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)
    			){

    		if(rhfListRecordTotal.size()>0){
	            ingresoConDatos = true;
			}else{
				ingresoConDatos= false;
			}
    	
    		
		}else{//Otros tipos de ingreso
			ingresoConDatos= true;// no necesita tener record anterior

		}
    	
    	
    	//CAMBIO DE CARRERA Y REINGRESO CON REDISEÑO DEBE TENER RECORD EN LA CARRERA ANTERIOR DESDE LA QUE VIENE
    	if((rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE)
    			||(rhfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE))	{
	       
			try {
				
				if(rhfPersonaDtoEditar.getFcinCrrAnteriorId()!=GeneralesConstantes.APP_ID_BASE){
				carreraAux2 = servRhfCarreraServicio.buscarPorId(rhfPersonaDtoEditar.getFcinCrrAnteriorId());
			
    		
    				if(carreraAux2.getCrrEspeCodigo()!=null){
    					 //BUSCO RECORD EN EL SAU, DE LA CARRERA QUE VIENE
        				recordSAUAux=servRhfRecordEstudianteSAU.buscarRecordAcademicoSAUconAnulados(rhfPersonaDtoEditar.getPrsIdentificacion(), carreraAux2.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
    				}
    				
    			    //BUSCO RECORD EN SIIU, DE LA CARRERA QUE VIENE
		            recordSIIUAux=servRhfRecordEstudianteSIIU.buscarXidentificacionXcarrera(rhfPersonaDtoEditar.getPrsIdentificacion(), carreraAux2.getCrrId());
    				
		   	            
		            
		          //AGREGO LA LISTA DE RECORD SIIU A RECORD TOTAL
					if(recordSIIUAux!=null)	{	
				    	for (RecordEstudianteDto recordEstudianteSiiu : recordSIIUAux) {
							MateriaDto materiaDtoAux= new MateriaDto();
							 Materia materiaAux = new Materia();
							 try {
								materiaAux= servRhfMateriaServicio.buscarPorId(recordEstudianteSiiu.getMlcrmtMtrId()) ;
							} catch (MateriaNoEncontradoException e) {
								materiaAux= null;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.materia.no.encontrada.exception")));
							} catch (MateriaException e) {
								materiaAux= null;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.materia.exception")));
							}
							
							 if(materiaAux!=null){
							 materiaDtoAux.setMtrId(materiaAux.getMtrId());
							 materiaDtoAux.setMtrCodigo(materiaAux.getMtrCodigo());
							 materiaDtoAux.setMtrDescripcion(materiaAux.getMtrDescripcion());
							 rhfListRecordCarreraAnterior.add(materiaDtoAux);
							 }
				
						
					   } //fin for
					
				    }	
					//AGREGO LA LISTA DE RECORD SAU A RECORD TOTAL
						if (recordSAUAux != null) {
							for (RecordEstudianteSAUDto recordEstudianteSau : recordSAUAux) {
								
								MateriaDto materiaDtoAux2 = new MateriaDto();
								Materia materiaAux2 = new Materia();
								try {
									materiaAux2 = servRhfMateriaSAU.buscarXCodigoXEspeCodigo(
											recordEstudianteSau.getCodigoMateria(), carreraAux2.getCrrEspeCodigo());
								} catch (MateriaValidacionException e) {
									materiaAux2 = null;
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.materia.validacion.sau.exception")));
								} catch (MateriaException e) {
									materiaAux2 = null;
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.materia.sau.exception")));
								}

								if (materiaAux2 != null) {
									materiaDtoAux2.setMtrId(materiaAux2.getMtrId());
									materiaDtoAux2.setMtrCodigo(materiaAux2.getMtrCodigo());
									materiaDtoAux2.setMtrDescripcion(materiaAux2.getMtrDescripcion());
									rhfListRecordCarreraAnterior.add(materiaDtoAux2);
								}
							
							}
					
					}
    				
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.ficha.inscripcion.carrera.validacion.exception")));
					return null;
				}
			} catch (CarreraNoEncontradoException e) {
				rhfListRecordCarreraAnterior = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.carrera.no.encontrada.exception")));
				return null;
			} catch (CarreraException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.carrera.exception")));
				return null;
			} catch (RecordEstudianteNoEncontradoException e) {
				rhfListRecordCarreraAnterior = null;
			} catch (RecordEstudianteException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.record.estudiante.exception")));
				return null;			
			} catch (RecordEstudianteDtoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.record.estudianteDto.exception")));
				return null;	
			}
			//VERIFICO QUE EXISTA RECORD EN LA CARRERA DE LA QUE PROVIENE EL ESTUDIANTE
			if((rhfListRecordCarreraAnterior==null)||(rhfListRecordCarreraAnterior.size()<=0)){
				ingresoConDatosRediseno= false;
			}else{
				ingresoConDatosRediseno = true;	
	         }
			
		}else{//Otros tipos de ingreso
			ingresoConDatosRediseno= true;// no necesita tener record anterior en carrera de diseño
		}
    	
    	
    	//Verificar si se subio el archivo
      		
    		if(rhfNombreArchivoSubido!= null){
    			archivoSubido= true;
    		}else{
    			archivoSubido= false;
    			
    		}
    		
    	
    	
    	
    	
		if(archivoSubido){
			
			if(ingresoConDatosRediseno){
			
			if(ingresoConDatos){
			
			if(datosCompletos){


				if(rhfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){

					for (MateriaDto it : rhfListMateriaDto) {
						if (it.getEsHomologado()==true) {
							rhfListHomologadosDto.add(it); //agrego las materias selecionadas y con notas a la lista de materias homologadas
						}
					}

								
					//Verifico que exista alguna materia homologada para permitir guardar.
					if (rhfListHomologadosDto.size() <= 0) {
						rhfValidadorClic = 0;  //inactivo modal guardar
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.lista.homologacion.vacia.validacion.exception")));
					} else {
						
						//Valido el numero de matricula lleno
	                      boolean validacionNumMatricula = false;
							for (MateriaDto it : rhfListHomologadosDto) {
								if (it.getNumMatricula()!=GeneralesConstantes.APP_ID_BASE) {
									validacionNumMatricula= true;
								}else {
									validacionNumMatricula= false;
									break;
								}
							}
						
						
						
						boolean validacionFinal = false;
						for (MateriaDto it1 : rhfListHomologadosDto) {
							if (vaidarCasosDeUso(it1)) {//verifico nuevamente que todos las materia seleccionadas tengan notas.
							   validacionFinal= true;
							}else {
								 validacionFinal= false;
								break;
							}

						}
						
						boolean numAsignaturasCorrecto = false;
						
						if(rhfPersonaDtoEditar.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE ){
							
							if(rhfListHomologadosDto.size() >=2){ //Cambio de universidad requiere homologar al menos 2 asignaturas, pedido producción 29 jul 2019
								numAsignaturasCorrecto = true;
							}
							
							
						}else{ //Otros tipos de ingreso es suficiente con una asignatura homologada.
							
							numAsignaturasCorrecto = true;
							
						}
						
					
					if(validacionNumMatricula)	{
						if(validacionFinal){
							if(numAsignaturasCorrecto){
						//TODO:
					    // llamarReporte();
					     rhfValidadorClic = 1;  //activo modal guardar
							}else{
								
								rhfValidadorClic = 0;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.datos.numero.asignaturas.validacion.exception")));
								
							}
								
								
								
						} else{
						   rhfValidadorClic = 0;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.datos.sin.ingresar.validacion.exception")));
					   }
					
					}else{
						rhfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Verifique el número de matricula de los registros seleccionados");						
					}		
						
						
					}

				}else if (rhfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE){

					rhfValidadorClic = 1;
					rhfListHomologadosDto= null;

				}else{
					rhfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.estado.ingreso.validacion.exception")));
			
				}

			}else{
				rhfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.datos.por.tipo.ingreso.validacion.exception")));
			
			}
			
			
		    }else{
			rhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.sin.record.estudiante.validacion.exception")));
	    	}
		
		}else{
			rhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.sin.record.estudiante.rediseño.validacion.exception")));
		}
		     
		
		}else{
			rhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarHomologacion.informe.aprobatorio.validacion.exception")));
		}

		return null;
	}

	
	/**
	 * Verifica información al hacer click en el botón generar matrícula
	 */
	public String verificarClickSufCulturaFisica() {
		//OBJETOS
		rhfFichaEstudiante = new FichaEstudiante();
		//LISTAS
		rhfListHomologadosDto = new ArrayList<>();
		rhfListRecordCarreraAnterior = new ArrayList<MateriaDto>();
		rhfPlanificacionCronogramaDtoIntercambio= null;
	    //VARIABLE
		rhfValidadorClic = 0;

		List<FichaEstudiante> auxListaFichasEstudiante = new ArrayList<>();
	
	   //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION EN LA CARRERA, DEPENDE DEL TIPO DE INGRESO PARA QUE PUEDA CONTINUAR
		 try {
			auxListaFichasEstudiante = servRhfFichaEstudiante.listarPorFcinId(rhfPersonaDtoEditar.getFcinId());
			
			if(auxListaFichasEstudiante==null||auxListaFichasEstudiante.size()<=0){//NO TIENE FCES en la carrera
				rhfFichaEstudiante= null;
				
		    	}else if(auxListaFichasEstudiante.size()==1){ //TIENE UNA FICHA ESTUDIANTE
			        	rhfFichaEstudiante = null;
				    FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickSufCulturaFisica.ficha.estudiante.validacion.exception")));
				    return null;
			       }
			 
		} catch (FichaEstudianteException e1) {
			rhfFichaEstudiante = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickSufCulturaFisica.ficha.estudiante.exception")));
			return null;
		}
		
				if(rhfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){

					for (MateriaDto it : rhfListMateriaDto) {
						if (it.getEsHomologado()==true) {
							rhfListHomologadosDto.add(it); //agrego las materias selecionadas y con notas a la lista de materias homologadas
						}
					}


					//Verifico que exista alguna materia homologada para permitir guardar.
					if (rhfListHomologadosDto.size() <= 0) {
						rhfValidadorClic = 0;  //inactivo modal guardar
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickSufCulturaFisica.homologacion.vacia.validacion.exception")));
					} else {
						

						//Valido el numero de matricula lleno
                      boolean validacionNumMatricula = false;
						for (MateriaDto it : rhfListHomologadosDto) {
							if (it.getNumMatricula()!=GeneralesConstantes.APP_ID_BASE) {
								validacionNumMatricula= true;
							}else {
								validacionNumMatricula= false;
								break;
							}
						}
						
						boolean validacionFinal = false;
						for (MateriaDto it1 : rhfListHomologadosDto) {
							if (validarCasosDeUsoSufCulturaFisica(it1)) {//verifico nuevamente que todos las materia seleccionadas tengan notas.
							   validacionFinal= true;
							}else {
								 validacionFinal= false;
								break;
							}

						}
						
					if(validacionNumMatricula){
						if(validacionFinal){
						//TODO:
					    // llamarReporte();  //llamar al reporte antes de guardar la homologación
					     rhfValidadorClic = 1;  //activo modal guardar
					     
						} else{
						   rhfValidadorClic = 0;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickSufCulturaFisica.datos.incompletos.validacion.exception")));
					   }
					}else{
						rhfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Verifique el número de matricula de los registros seleccionados");						
					}
						
						
						
					}

				}else if (rhfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE){

					rhfValidadorClic = 1;
					rhfListHomologadosDto= null;

				}else{
					rhfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickSufCulturaFisica.estado.ingreso.validacion.exception")));

				}

		

		return null;
	}

	
	
	/**
	 * Verifica información al hacer click en el boton guardar  de ubicación idiomas
	 */
	public String verificarClickRegistrarUbicacion() {
		
		rhfFichaEstudiante = new FichaEstudiante();
		rhfListHomologadosDto = new ArrayList<>();
		
		//VARIABLES
		rhfValidadorClic = 0;
		//AUXILIARES
		List<FichaEstudiante> auxListaFichasEstudiante = new ArrayList<>();
		
	   //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION EN EL IDIOMA
		 try {
			auxListaFichasEstudiante = servRhfFichaEstudiante.listarPorFcinId(rhfPersonaDtoEditar.getFcinId());
			
			if (auxListaFichasEstudiante == null || auxListaFichasEstudiante.size() <= 0) {// NO TIENE FCES en el idioma
				rhfFichaEstudiante = null;

			} else if (auxListaFichasEstudiante.size() == 1) { // TIENE 1 FICHA ESTUDIANTE, NO SE DEBE HOMOLOGAR 2 VECES EN IDIOMAS

				//rhfFichaEstudiante = servRhfFichaEstudiante.buscarPorId(auxListaFichasEstudiante.get(0).getFcesId());
				rhfFichaEstudiante = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarUbicacion.ficha.estudiante.validacion.exception")));
				return null;
			} else {  //TIENE VARIAS FICHAS ESTUDIANTE
				rhfFichaEstudiante = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarUbicacion.varias.ficha.estudiante.validacion.exception")));
				return null;
			}
		
	
		//	if(rhfNombreArchivoSubido!= null){

					if(rhfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){

						for (MateriaDto it : rhfListMateriaDto) {
							if (it.getEsHomologado()==true) {
								rhfListHomologadosDto.add(it); //agrego las materias selecionadas y con notas a la lista de materias homologadas
							}
						}

						//Verifico que exista alguna materia homologada para permitir guardar.
						if (rhfListHomologadosDto.size() <= 0) {
							rhfValidadorClic = 0;  //inactivo modal guardar
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarUbicacion.homologacion.vacia.validacion.exception")));
						} else {
							for (MateriaDto it1 : rhfListHomologadosDto) {
								if (validarEstadoMateria(it1)) {//verifico nuevamente que todos las materia seleccionadas tengan un estado.
									rhfValidadorClic = 2;  //activo modal guardar Ubicacion
								}else {
									rhfValidadorClic = 0;
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarUbicacion.informacion.incompleta.validacion.exception")));
									break;
								}

							}
						}

					}else if (rhfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE){

						rhfValidadorClic = 2;
						rhfListHomologadosDto= null;

					}else{
						rhfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarUbicacion.estado.ingreso.validacion.exception")));

					}
			
//		    	}else{
//					rhfValidadorClic = 0;
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("Debe ingresar el informe aprobatorio");
//				}
				
		} catch (FichaEstudianteException e1) {
			rhfFichaEstudiante = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.verificarClickRegistrarUbicacion.ficha.estudiante.exception")));
			return null;
		} 

		return null;
	}

	/**
	 * Metodo que habilita o no la columna check registrar homologacion
	 */
	public void bloqueaHomologacion(){
		
		if(rhfPersonaDtoEditar.getFcinEstadoIngreso()!=FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){ //No homologado
			rhfBloqueaHomologacion= false; //ocultamos la columna check homologacion 
		}else{ //es homologado
			rhfBloqueaHomologacion= true; //presentamos la columna check homologacion
		}
		
		if(rhfListMateriaDto!=null){// si existen materia en la lista
			
			for (MateriaDto materiaDto : rhfListMateriaDto) {
			materiaDto.setEsHomologado(false);
			// seteo el numero de matricula a primera
			materiaDto.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
			// asignar valores por defecto de las notas
			materiaDto.setNotaUno(null);
			materiaDto.setNotaDos(null);
			materiaDto.setNotaTres(null);
			materiaDto.setClfPromedioAsistencia(null);
			materiaDto.setNotaSuma(null);
			materiaDto.setAprobado(false);
			materiaDto.setMtrCmbEstado(true); //deshabilita la vista del combo numMatricula
			materiaDto.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
		  }
		
		}
		
	}
	
	/**
	 * Habilita o dehabilita opciones según desea ubicar o no al estudiante de idiomas
	 */
	public void bloqueaUbicacionIdiomas(){
		
		if(rhfPersonaDtoEditar.getFcinEstadoIngreso()!=FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){ //No homologado
			rhfBloqueaUbicacionIngles= false; //ocultamos la columna check homologacion 
		}else{ //es homologado
			rhfBloqueaUbicacionIngles= true; //presentamos la columna check homologacion
			//rhfInactivaAprobarUbicacion=true;	//inactiva opción de aprobar
		}
		
		if(rhfListMateriaDto!=null){// si existen materia en la lista
			
			for (MateriaDto materiaDto : rhfListMateriaDto) {
			materiaDto.setEsHomologado(false);
			materiaDto.setAprobado(false);  
			materiaDto.setMtrCmbEstado(true); //deshabilita la vista del combo aprobar
			materiaDto.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
		  }
		
		}
		
	}
	
	/**
	 * Permite validar los que la información ingresada cumpla con os parametros establecidos.
	 */
	private boolean vaidarCasosDeUso(MateriaDto it) {
		boolean caso = true;
	//	if (it.getNotaUno() == null || it.getNotaUno().equals(new BigDecimal(0))) {
		if (it.getNotaUno() == null ) {
			caso = false;
			rhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos el Campo Nota 1.");
		}


//		if (it.getNotaDos()== null || it.getNotaDos().equals(new BigDecimal(0))) {
			
	   if (it.getNotaDos()== null) {
			caso = false;
			rhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos en el Campo Nota 2.");
		}

		
		return caso;
	}
	
	
	
	/**
	 * Permite validar los que la información ingresada cumpla con los parametros establecidos suficiencia cultura física.
	 */
	private boolean validarCasosDeUsoSufCulturaFisica(MateriaDto it) {
		boolean caso = true;
		
		if (it.getClfPromedioAsistencia()== null || it.getClfPromedioAsistencia().equals(new BigDecimal(0))) {
			caso = false;
			rhfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos de cero en el Campo porcentaje asistencia.");
		}

		if (it.getNotaUno() == null || it.getNotaUno().equals(new BigDecimal(0))) {
	//		if (it.getNotaUno() == null ) {   //ACEPTA CERO
				caso = false;
				rhfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Ingrese valores distintos de cero el Campo Nota 1.");
			}


		if (it.getNotaDos()== null || it.getNotaDos().equals(new BigDecimal(0))) {
				
		//   if (it.getNotaDos()== null) {   //ACEPTA CERO
				caso = false;
				rhfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Ingrese valores distintos de cero en el Campo Nota 2.");
			}
		
		return caso;
	}
	
	
	
	/**
	 * Permite validar los que la información ingresada en cada fila cumpla con los parametros establecidos.
	 */
	private boolean validarEstadoMateria(MateriaDto it) {
		boolean caso = true;
		
		if ( it.getEstadoHomologacion()==GeneralesConstantes.APP_ID_BASE) {
			caso = false;
			rhfValidadorClic = 0;
		}
		
		if(it.getEsHomologadoReg2()){
		  if ( it.getEstadoHomologacionReg2()==GeneralesConstantes.APP_ID_BASE) {
			caso = false;
			rhfValidadorClic = 0;
		  }
		}
		
		
		return caso;
	}
	
	/**
	 * calcula el promedio de las dos notas
	 */
	public void calcularPromedio(MateriaDto materiaDto) {

		if (vaidarCasosDeUso(materiaDto)) {
			BigDecimal notaSuma = materiaDto.getNotaUno().add(materiaDto.getNotaDos());
			
			//materiaDto.setNotaSuma(notaSuma);  
						
			BigDecimal valor = new BigDecimal(27.5);
			BigDecimal sumaredond = new BigDecimal(0);
			
			if(notaSuma.compareTo(valor)>=0){
				sumaredond=	notaSuma.setScale(0, RoundingMode.HALF_UP);	
			}else{
				sumaredond=notaSuma;
			}
						
			materiaDto.setNotaSuma(sumaredond);
			
			int result = sumaredond.compareTo(new BigDecimal(rhfSistemaCalificacion.getSsclNotaMinimaAprobacion().toString()));
			
			if (result >= 0) {
				// aprobado por notas
				//materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula  MQ: Cambio solicitado producción  20 jun 2019
				materiaDto.setMtrCmbEstado(Boolean.FALSE); //habilita vista de combo NumMatricula
				materiaDto.setAprobado(true); // seteo el aprobado de la materia
				//materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);// Constante para num matricula  MQ: Cambio solicitado producción  20 jun 2019
				//estado de homologacion varia si es suficiencia de cultura fisica.
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE);
				
			} else {
				// reprobado por notas
				materiaDto.setMtrCmbEstado(Boolean.FALSE); //habilita vista de combo NumMatricula
				materiaDto.setAprobado(false); // seteo el reprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				//materiaDto.setNumMatricula(0);
				//materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE); MQ: Cambio solicitado producción  20 jun 2019
				
			}
			
		}
		
	}
	
	
	/**
	 * calcula el promedio de las dos notas y asistencia Suf Cultura física
	 */
	public void calcularPromedioSufCulturaFisica(MateriaDto materiaDto) {

		if (validarCasosDeUsoSufCulturaFisica(materiaDto)) {
		
            BigDecimal notaSuma = materiaDto.getNotaUno().add(materiaDto.getNotaDos());
            Float promedio = materiaDto.getClfPromedioAsistencia();
									
			BigDecimal valor = new BigDecimal(27.5);
			BigDecimal sumaredond = new BigDecimal(0);
			
			if(notaSuma.compareTo(valor)>=0){
				sumaredond=	notaSuma.setScale(0, RoundingMode.HALF_UP);	
			}else{
				sumaredond=notaSuma;
			}
						
			materiaDto.setNotaSuma(sumaredond);
			
			int result = sumaredond.compareTo(new BigDecimal(rhfSistemaCalificacion.getSsclNotaMinimaAprobacion().toString()));
			
			if((result>=0)&&(promedio>=80)){
				// aprobado por nota y promedio
				materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula
				materiaDto.setAprobado(true); // seteo el aprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
			}else{
				materiaDto.setMtrCmbEstado(Boolean.FALSE); //habilita vista de combo NumMatricula
				materiaDto.setAprobado(false); // seteo el reprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
					
			}
			
		}
		
	}
	

	
	/**
	 * método que setear campos  al checkear es homologado en cada fila
	 */

	public void setearCampos(MateriaDto materia) {
  
		if (materia.getEsHomologado()) {
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Ingrese valores distintos de cero en los campos Nota1 y Nota2.");
		}else {
			materia.setNotaUno(null);
			materia.setNotaDos(null);
			materia.setNotaSuma(null);
			materia.setClfPromedioAsistencia(null);
			materia.setMtrCmbEstado(Boolean.TRUE);
			materia.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
			materia.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
//			rhfBloqueaNumMatricula = true;
			
//			if (rhfListMateriaDto.size() >= 0) {
//				rhfListMateriaDto.remove(materia);	
//			}
			
		}
	
	}
	
	/**
	 * método que setear campos  al checkear es homologado en cada fila en suficiencia cultura Fisica
	 */
	public void setearCamposSufCulturaFisica(MateriaDto materia) {
  
		if (materia.getEsHomologado()) {
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Ingrese valores distintos de cero en los campos Nota1 y Nota2.");
		}else {
			materia.setNotaUno(null);
			materia.setNotaDos(null);
			materia.setNotaSuma(null);
			materia.setClfPromedioAsistencia(null);
			materia.setMtrCmbEstado(Boolean.TRUE);
			materia.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
			materia.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
			
		}
	
	}
	
	
	

	/**
	 * método que setear campos  al checkear es homologado en cada fila
	 */

	public void setearCamposIdiomas(MateriaDto materia) {

		if (materia.getEsHomologado()) {
			//rhfInactivaAprobarUbicacion=false; //Activo seleccion
			materia.setMtrCmbEstado(Boolean.FALSE);
			materia.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Debe registrar el estado del nivel");
			
		}else {
			materia.setMtrCmbEstado(Boolean.TRUE);
			materia.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
			//rhfInactivaAprobarUbicacion=true;			
//			if (rhfListMateriaDto.size() >= 0) {
//				rhfListMateriaDto.remove(materia);	
//			}
			
		}

	}
	
	
	//CARGA DE ARCHIVO
		/**
		 * Método para cargar el archivo en ruta temporal
		 * @param event - event archivo oficio que presenta el estudiante para anular matrícula
		 */
		public void handleFileUpload(FileUploadEvent archivo) {
			rhfNombreArchivoSubido = archivo.getFile().getFileName();
			rhfNombreArchivoAuxiliar = archivo.getFile().getFileName();
			String rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rhfNombreArchivoSubido;
			try {
				GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(),	rutaTemporal);
				archivo.getFile().getInputstream().close();
			} catch (IOException ioe) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistroHomologacion.handleFileUpload.carga.archivo.exception")));
			}
		}
	
		
		
		/**
		 * llama a generar el reporte PDF
		 */
		public void llamarReporte(){
			
			ReporteHomologacionForm.generarReporteHomologacion(rhfListHomologadosDto, rhfUsuario, rhfPersonaDtoEditar );
			rhfActivadorReporte = 1;
							
		}
		
		/**
		 * Método para desactivar modal guardar
		 * 
		 */ 
		public void desactivaModalGuardar(){
			rhfValidadorClic = 0;
		}
		
	
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getRhfUsuario() {
		return rhfUsuario;
	}

	public void setRhfUsuario(Usuario rhfUsuario) {
		this.rhfUsuario = rhfUsuario;
	}

	public List<PersonaDto> getRhfListPersonaDto() {
		rhfListPersonaDto = rhfListPersonaDto == null ? (new ArrayList<PersonaDto>()) : rhfListPersonaDto;
		return rhfListPersonaDto;
	}

	public void setRhfListPersonaDto(List<PersonaDto> rhfListPersonaDto) {
		this.rhfListPersonaDto = rhfListPersonaDto;
	}

	public PersonaDto getRhfPersonaDtoBuscar() {
		return rhfPersonaDtoBuscar;
	}

	public void setRhfPersonaDtoBuscar(PersonaDto rhfPersonaDtoBuscar) {
		this.rhfPersonaDtoBuscar = rhfPersonaDtoBuscar;
	}

	public List<CarreraDto> getRhfListCarreraDto() {
		rhfListCarreraDto = rhfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : rhfListCarreraDto;
		return rhfListCarreraDto;
	}

	public void setRhfListCarreraDto(List<CarreraDto> rhfListCarreraDto) {
		this.rhfListCarreraDto = rhfListCarreraDto;
	}

	public CarreraDto getRhfCarreraDtoBuscar() {
		return rhfCarreraDtoBuscar;
	}

	public void setRhfCarreraDtoBuscar(CarreraDto rhfCarreraDtoBuscar) {
		this.rhfCarreraDtoBuscar = rhfCarreraDtoBuscar;
	}

	public PeriodoAcademico getRhfPeriodoAcademico() {
		return rhfPeriodoAcademico;
	}

	public void setRhfPeriodoAcademico(PeriodoAcademico rhfPeriodoAcademico) {
		this.rhfPeriodoAcademico = rhfPeriodoAcademico;
	}

	public MallaCurricular getRhfMallaCurricular() {
		return rhfMallaCurricular;
	}

	public void setRhfMallaCurricular(MallaCurricular rhfMallaCurricular) {
		this.rhfMallaCurricular = rhfMallaCurricular;
	}

	public PersonaDto getRhfPersonaDtoEditar() {
		return rhfPersonaDtoEditar;
	}

	public void setRhfPersonaDtoEditar(PersonaDto rhfPersonaDtoEditar) {
		this.rhfPersonaDtoEditar = rhfPersonaDtoEditar;
	}

	public List<MateriaDto> getRhfListMateriaDto() {
		rhfListMateriaDto = rhfListMateriaDto == null ? (new ArrayList<MateriaDto>()) : rhfListMateriaDto;
		return rhfListMateriaDto;
	}

	public void setRhfListMateriaDto(List<MateriaDto> rhfListMateriaDto) {
		this.rhfListMateriaDto = rhfListMateriaDto;
	}

	public List<ParaleloDto> getRhfListParaleloDto() {
		rhfListParaleloDto = rhfListParaleloDto == null ? (new ArrayList<ParaleloDto>()) : rhfListParaleloDto;
		return rhfListParaleloDto;
	}

	public void setRhfListParaleloDto(List<ParaleloDto> rhfListParaleloDto) {
		this.rhfListParaleloDto = rhfListParaleloDto;
	}

	public TipoSistemaCalificacion getRhfTipoSistemaCalificacion() {
		return rhfTipoSistemaCalificacion;
	}

	public void setRhfTipoSistemaCalificacion(TipoSistemaCalificacion rhfTipoSistemaCalificacion) {
		this.rhfTipoSistemaCalificacion = rhfTipoSistemaCalificacion;
	}

	public SistemaCalificacion getRhfSistemaCalificacion() {
		return rhfSistemaCalificacion;
	}

	public void setRhfSistemaCalificacion(SistemaCalificacion rhfSistemaCalificacion) {
		this.rhfSistemaCalificacion = rhfSistemaCalificacion;
	}

	public PlanificacionCronograma getRhfPlanificacionCronograma() {
		return rhfPlanificacionCronograma;
	}

	public void setRhfPlanificacionCronograma(PlanificacionCronograma rhfPlanificacionCronograma) {
		this.rhfPlanificacionCronograma = rhfPlanificacionCronograma;
	}

	public PeriodoAcademico getRhfPeriodoAcademicoActivo() {
		return rhfPeriodoAcademicoActivo;
	}

	public void setRhfPeriodoAcademicoActivo(PeriodoAcademico rhfPeriodoAcademicoActivo) {
		this.rhfPeriodoAcademicoActivo = rhfPeriodoAcademicoActivo;
	}

	public Integer getRhfValidadorClic() {
		return rhfValidadorClic;
	}

	public void setRhfValidadorClic(Integer rhfValidadorClic) {
		this.rhfValidadorClic = rhfValidadorClic;
	}

	public Integer getRhfBloqueoModal() {
		return rhfBloqueoModal;
	}

	public void setRhfBloqueoModal(Integer rhfBloqueoModal) {
		this.rhfBloqueoModal = rhfBloqueoModal;
	}
	

	public ParaleloDto getRhfParaleloHomologacion() {
		return rhfParaleloHomologacion;
	}

	public void setRhfParaleloHomologacion(ParaleloDto rhfParaleloHomologacion) {
		this.rhfParaleloHomologacion = rhfParaleloHomologacion;
	}

	public List<MateriaDto> getRhfListHomologadosDto() {
		rhfListHomologadosDto = rhfListHomologadosDto == null ? (new ArrayList<MateriaDto>()) : rhfListHomologadosDto;

		return rhfListHomologadosDto;
	}

	public void setRhfListHomologadosDto(List<MateriaDto> rhfListHomologadosDto) {
		this.rhfListHomologadosDto = rhfListHomologadosDto;
	}

	public String getRhfNombreArchivoAuxiliar() {
		return rhfNombreArchivoAuxiliar;
	}

	public void setRhfNombreArchivoAuxiliar(String rhfNombreArchivoAuxiliar) {
		this.rhfNombreArchivoAuxiliar = rhfNombreArchivoAuxiliar;
	}

	public String getRhfNombreArchivoSubido() {
		return rhfNombreArchivoSubido;
	}

	public void setRhfNombreArchivoSubido(String rhfNombreArchivoSubido) {
		this.rhfNombreArchivoSubido = rhfNombreArchivoSubido;
	}

	

	public List<MateriaDto> getRhfListRecordTotal() {
		rhfListRecordTotal = rhfListRecordTotal == null ? (new ArrayList<MateriaDto>()) : rhfListRecordTotal;
		return rhfListRecordTotal;
	}

	public void setRhfListRecordTotal(List<MateriaDto> rhfListRecordTotal) {
		this.rhfListRecordTotal = rhfListRecordTotal;
	}

	

	public List<InstitucionAcademicaDto> getRhfListUniversidadesDto() {
		rhfListUniversidadesDto = rhfListUniversidadesDto == null ? (new ArrayList<InstitucionAcademicaDto>()) : rhfListUniversidadesDto;
		return rhfListUniversidadesDto;
	}

	public void setRhfListUniversidadesDto(List<InstitucionAcademicaDto> rhfListUniversidadesDto) {
		this.rhfListUniversidadesDto = rhfListUniversidadesDto;
	}

	public List<TituloDto> getRhfListTitulosUniversidadDto() {
		rhfListTitulosUniversidadDto = rhfListTitulosUniversidadDto == null ? (new ArrayList<TituloDto>()) : rhfListTitulosUniversidadDto;
		return rhfListTitulosUniversidadDto;
	}

	public void setRhfListTitulosUniversidadDto(List<TituloDto> rhfListTitulosUniversidadDto) {
		this.rhfListTitulosUniversidadDto = rhfListTitulosUniversidadDto;
	}

	public List<CarreraDto> getRhfListCarrerasDtoTodas() {
		rhfListCarrerasDtoTodas = rhfListCarrerasDtoTodas == null ? (new ArrayList<CarreraDto>()) : rhfListCarrerasDtoTodas;
		return rhfListCarrerasDtoTodas;
	}

	public void setRhfListCarrerasDtoTodas(List<CarreraDto> rhfListCarrerasDtoTodas) {
		this.rhfListCarrerasDtoTodas = rhfListCarrerasDtoTodas;
	}

	public List<MateriaDto> getRhfListRecordCarreraAnterior() {
		rhfListRecordCarreraAnterior = rhfListRecordCarreraAnterior == null ? (new ArrayList<MateriaDto>()) : rhfListRecordCarreraAnterior;
		return rhfListRecordCarreraAnterior;
	}

	public void setRhfListRecordCarreraAnterior(List<MateriaDto> rhfListRecordCarreraAnterior) {
		this.rhfListRecordCarreraAnterior = rhfListRecordCarreraAnterior;
	}

	public  FichaEstudiante getRhfFichaEstudiante() {
		return rhfFichaEstudiante;
	}

	public  void setRhfFichaEstudiante(FichaEstudiante rhfFichaEstudiante) {
		this.rhfFichaEstudiante = rhfFichaEstudiante;
	}

//	public List<PeriodoAcademico> getRhfListaPracActivoPosgrado() {
//		rhfListaPracActivoPosgrado = rhfListaPracActivoPosgrado == null ? (new ArrayList<PeriodoAcademico>()) : rhfListaPracActivoPosgrado;
//		return rhfListaPracActivoPosgrado;
//	}
//
//	public  void setRhfListaPracActivoPosgrado(List<PeriodoAcademico> rhfListaPracActivoPosgrado) {
//		this.rhfListaPracActivoPosgrado = rhfListaPracActivoPosgrado;
//	}

//	public  List<PeriodoAcademico> getRhfListaPeriodosPorCarreraPosGrado() {
//		rhfListaPeriodosPorCarreraPosGrado = rhfListaPeriodosPorCarreraPosGrado == null ? (new ArrayList<PeriodoAcademico>()) : rhfListaPeriodosPorCarreraPosGrado;
//		return rhfListaPeriodosPorCarreraPosGrado;
//	}
//
//	public void setRhfListaPeriodosPorCarreraPosGrado(
//			List<PeriodoAcademico> rhfListaPeriodosPorCarreraPosGrado) {
//		this.rhfListaPeriodosPorCarreraPosGrado = rhfListaPeriodosPorCarreraPosGrado;
//	}

	public  boolean isRhfBloqueaCamposSegundaCrr() {
		return rhfBloqueaCamposSegundaCrr;
	}

	public  void setRhfBloqueaCamposSegundaCrr(boolean rhfBloqueaCamposSegundaCrr) {
		this.rhfBloqueaCamposSegundaCrr = rhfBloqueaCamposSegundaCrr;
	}

	public  boolean isRhfBloqueaAnioReinicio() {
		return rhfBloqueaAnioReinicio;
	}

	public  void setRhfBloqueaAnioReinicio(boolean rhfBloqueaAnioReinicio) {
		this.rhfBloqueaAnioReinicio = rhfBloqueaAnioReinicio;
	}

	public  boolean isRhfBloqueaEstadoIngreso() {
		return rhfBloqueaEstadoIngreso;
	}

	public   void setRhfBloqueaEstadoIngreso(boolean rhfBloqueaEstadoIngreso) {
		this.rhfBloqueaEstadoIngreso = rhfBloqueaEstadoIngreso;
	}

	public  boolean isRhfBloqueaCampoReingresoRediseno() {
		return rhfBloqueaCampoReingresoRediseno;
	}

	public  void setRhfBloqueaCampoReingresoRediseno(boolean rhfBloqueaCampoReingresoRediseno) {
		this.rhfBloqueaCampoReingresoRediseno = rhfBloqueaCampoReingresoRediseno;
	}

	public  boolean isRhfBloqueaBotonFinalizar() {
		return rhfBloqueaBotonFinalizar;
	}

	public  void setRhfBloqueaBotonFinalizar(boolean rhfBloqueaBotonFinalizar) {
		this.rhfBloqueaBotonFinalizar = rhfBloqueaBotonFinalizar;
	}

	public  boolean isRhfBloqueaHomologacion() {
		return rhfBloqueaHomologacion;
	}

	public  void setRhfBloqueaHomologacion(boolean rhfBloqueaHomologacion) {
		this.rhfBloqueaHomologacion = rhfBloqueaHomologacion;
	}

	public  boolean isRhfBloqueaUbicacionIngles() {
		return rhfBloqueaUbicacionIngles;
	}

	public  void setRhfBloqueaUbicacionIngles(boolean rhfBloqueaUbicacionIngles) {
		this.rhfBloqueaUbicacionIngles = rhfBloqueaUbicacionIngles;
	}

//	public  boolean isRhfBloqueaPeriodoPosgrado() {
//		return rhfBloqueaPeriodoPosgrado;
//	}
//
//	public  void setRhfBloqueaPeriodoPosgrado(boolean rhfBloqueaPeriodoPosgrado) {
//		this.rhfBloqueaPeriodoPosgrado = rhfBloqueaPeriodoPosgrado;
//	}

	public boolean isRhfInactivaAprobarUbicacion() {
		return rhfInactivaAprobarUbicacion;
	}

	public void setRhfInactivaAprobarUbicacion(boolean rhfInactivaAprobarUbicacion) {
		this.rhfInactivaAprobarUbicacion = rhfInactivaAprobarUbicacion;
	}

	public PeriodoAcademico getRhfPeriodoAcademicoInscripcion() {
		return rhfPeriodoAcademicoInscripcion;
	}

	public void setRhfPeriodoAcademicoInscripcion(PeriodoAcademico rhfPeriodoAcademicoInscripcion) {
		this.rhfPeriodoAcademicoInscripcion = rhfPeriodoAcademicoInscripcion;
	}

	public UsuarioRol getRhfUsuarioRol() {
		return rhfUsuarioRol;
	}

	public void setRhfUsuarioRol(UsuarioRol rhfUsuarioRol) {
		this.rhfUsuarioRol = rhfUsuarioRol;
	}

	public List<Rol> getRhfListaRolesUsuario() {
		rhfListaRolesUsuario = rhfListaRolesUsuario == null ? (new ArrayList<Rol>()) : rhfListaRolesUsuario;
		return rhfListaRolesUsuario;
	}

	public void setRhfListaRolesUsuario(List<Rol> rhfListaRolesUsuario) {
		this.rhfListaRolesUsuario = rhfListaRolesUsuario;
	}

	public Rol getRhfRol() {
		return rhfRol;
	}

	public void setRhfRol(Rol rhfRol) {
		this.rhfRol = rhfRol;
	}

//	public boolean isRhfCrearMatriculaPosgrado() {
//		return rhfCrearMatriculaPosgrado;
//	}
//
//	public void setRhfCrearMatriculaPosgrado(boolean rhfCrearMatriculaPosgrado) {
//		this.rhfCrearMatriculaPosgrado = rhfCrearMatriculaPosgrado;
//	}

//	public Integer getRhfNivelActualPosgrado() {
//		return rhfNivelActualPosgrado;
//	}
//
//	public void setRhfNivelActualPosgrado(Integer rhfNivelActualPosgrado) {
//		this.rhfNivelActualPosgrado = rhfNivelActualPosgrado;
//	}

//	public List<MateriaDto> getRhfListMateriaNivelPosgrado() {
//		rhfListMateriaNivelPosgrado = rhfListMateriaNivelPosgrado == null ? (new ArrayList<MateriaDto>()) : rhfListMateriaNivelPosgrado;
//		return rhfListMateriaNivelPosgrado;
//	}

//	public void setRhfListMateriaNivelPosgrado(List<MateriaDto> rhfListMateriaNivelPosgrado) {
//		this.rhfListMateriaNivelPosgrado = rhfListMateriaNivelPosgrado;
//	}

//	public PeriodoAcademico getRhfPeriodoAcademicoPosgrado() {
//		return rhfPeriodoAcademicoPosgrado;
//	}

//	public void setRhfPeriodoAcademicoPosgrado(PeriodoAcademico rhfPeriodoAcademicoPosgrado) {
//		this.rhfPeriodoAcademicoPosgrado = rhfPeriodoAcademicoPosgrado;
//	}

//	public PlanificacionCronograma getRhfPlanificacionCronogramaPosgrado() {
//		return rhfPlanificacionCronogramaPosgrado;
//	}

//	public void setRhfPlanificacionCronogramaPosgrado(PlanificacionCronograma rhfPlanificacionCronogramaPosgrado) {
//		this.rhfPlanificacionCronogramaPosgrado = rhfPlanificacionCronogramaPosgrado;
//	}

//	public List<ParaleloDto> getRhfListaParaleloNuevaMatrPosgrado() {
//		rhfListaParaleloNuevaMatrPosgrado = rhfListaParaleloNuevaMatrPosgrado == null ? (new ArrayList<ParaleloDto>()) : rhfListaParaleloNuevaMatrPosgrado;
//		return rhfListaParaleloNuevaMatrPosgrado;
//	}
//
//	public void setRhfListaParaleloNuevaMatrPosgrado(List<ParaleloDto> rhfListaParaleloNuevaMatrPosgrado) {
//		this.rhfListaParaleloNuevaMatrPosgrado = rhfListaParaleloNuevaMatrPosgrado;
//	}

	public PeriodoAcademico getRhfPeriodoAcademicoIdioma() {
		return rhfPeriodoAcademicoIdioma;
	}

	public void setRhfPeriodoAcademicoIdioma(PeriodoAcademico rhfPeriodoAcademicoIdioma) {
		this.rhfPeriodoAcademicoIdioma = rhfPeriodoAcademicoIdioma;
	}

	public PlanificacionCronogramaDto getRhfPlanificacionCronogramaDtoIntercambio() {
		return rhfPlanificacionCronogramaDtoIntercambio;
	}

	public void setRhfPlanificacionCronogramaDtoIntercambio(
			PlanificacionCronogramaDto rhfPlanificacionCronogramaDtoIntercambio) {
		this.rhfPlanificacionCronogramaDtoIntercambio = rhfPlanificacionCronogramaDtoIntercambio;
	}

	public boolean isRhfBloqueaSubirArchivo() {
		return rhfBloqueaSubirArchivo;
	}

	public void setRhfBloqueaSubirArchivo(boolean rhfBloqueaSubirArchivo) {
		this.rhfBloqueaSubirArchivo = rhfBloqueaSubirArchivo;
	}

	public PeriodoAcademico getRhfPeriodoAcademicoCulturaFisica() {
		return rhfPeriodoAcademicoCulturaFisica;
	}

	public void setRhfPeriodoAcademicoCulturaFisica(PeriodoAcademico rhfPeriodoAcademicoCulturaFisica) {
		this.rhfPeriodoAcademicoCulturaFisica = rhfPeriodoAcademicoCulturaFisica;
	}

	public Integer getRhfActivadorReporte() {
		return rhfActivadorReporte;
	}

	public void setRhfActivadorReporte(Integer rhfActivadorReporte) {
		this.rhfActivadorReporte = rhfActivadorReporte;
	}

	
}
