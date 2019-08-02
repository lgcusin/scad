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
   
 ARCHIVO:     RegistroHomologacionPosgradoForm.java	  
 DESCRIPCION: Managed Bean que maneja el registro de las materias homologadas de los estudiantes de posgrado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
05-oct-2018			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.homologacion;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionNoEncontradoException;
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
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoSistemaCalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
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

@ManagedBean(name = "registroHomologacionPosgradoForm")
@SessionScoped
public class RegistroHomologacionPosgradoForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// Objetos
	// PANTALLA LISTAR ESTUDIANTES
	private Usuario rhpfUsuario;
	private PersonaDto rhpfPersonaDtoBuscar; // Guardar la cedula
	private CarreraDto rhpfCarreraDtoBuscar; // Guardar la carrera
	private ParaleloDto rhpfParaleloHomologacion;// paralelo unico de homologación por carrera.
	private PersonaDto rhpfPersonaDtoEditar;
	private PeriodoAcademico rhpfPeriodoAcademico;
	private MallaCurricular rhpfMallaCurricular;
	private TipoSistemaCalificacion rhpfTipoSistemaCalificacion;
	private SistemaCalificacion rhpfSistemaCalificacion;
	private PlanificacionCronograma rhpfPlanificacionCronograma;
	//private PeriodoAcademico rhpfPeriodoAcademicoActivo; No se utiliza 23 nov 2018
	private FichaEstudiante rhpfFichaEstudiante;
	private Integer rhpfValidadorClic;
	private PeriodoAcademico rhpfPeriodoAcademicoPosgrado;
	private UsuarioRol rhpfUsuarioRol;
	private Rol rhpfRol;
	private boolean rhpfCrearMatriculaPosgrado;
	private Integer rhpfNivelActualPosgrado;
	private PlanificacionCronograma rhpfPlanificacionCronogramaPosgrado;
	private List<ParaleloDto> rhpfListaParaleloNuevaMatrPosgrado;
	
	// Listas de Objetos
	private List<CarreraDto> rhpfListCarreraDto; // Combo a seleccionar carreras
	private List<PersonaDto> rhpfListPersonaDto; // Listado de personas
	private List<MateriaDto> rhpfListMateriaDto;  //Lista de materias de la malla curricular.
	private List<ParaleloDto> rhpfListParaleloDto; 
	private List<MateriaDto> rhpfListHomologadosDto;//Lista de materias homologadas a guardar en BDD
	private List<MateriaDto> rhpfListRecordTotal;
	private List<MateriaDto> rhpfListRecordCarreraAnterior;
	private List<InstitucionAcademicaDto> rhpfListUniversidadesDto;
	private List<TituloDto> rhpfListTitulosUniversidadDto;
	private List<Rol> rhpfListaRolesUsuario;
	private List<MateriaDto> rhpfListMateriaNivelPosgrado;
	
	//Subir archivo pdf
	private String rhpfNombreArchivoAuxiliar;
	private String rhpfNombreArchivoSubido;
	//Listas homologación Posgrado
	private List<PeriodoAcademico> rhpfListaPracActivoPosgrado;
	private List<PeriodoAcademico> rhpfListaPeriodosPorCarreraPosGrado;
	// bloquea opciones
	private Integer rhpfBloqueoModal; // 1.- no 0.- si para evitar que recarguen  la página y se pueda generar la matricula una y otra vez
	//private boolean rhpfBloqueaNumMatricula;
		private boolean rhpfBloqueaEstadoIngreso;
	//private boolean rhpfBloqueaCampoReingresoRediseno;
	private boolean rhpfBloqueaBotonFinalizar;
	
	private boolean rhpfBloqueaHomologacion;   //activa e inactiva opciones de homologacion (homologa o no)
	private boolean rhpfBloqueaPeriodoPosgrado;
	private boolean rhpfBloqueaSubirArchivo; //activa e inactiva opcion de subir archivo
	private Integer rhpfActivadorReporte;
	
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
	PersonaDtoServicioJdbc servRhpfPersonaDto;
	@EJB
	CarreraDtoServicioJdbc servRhpfCarreraDto;
	@EJB
	PeriodoAcademicoServicio servRhpfPeriodoAcademico;
	@EJB
	MallaCurricularServicio servRhpfMallaCurricular;
	@EJB
	MateriaDtoServicioJdbc servRhpfMateriaDto;
	@EJB
	ParaleloDtoServicioJdbc servRhpfParaleloDto;
	@EJB
	TipoSistemaCalificacionServicio servRhpfTipoSistemaCalificacion;
	@EJB
	SistemaCalificacionServicio servRhpfSistemaCalificacion;
	@EJB
	PlanificacionCronogramaServicio servRhpfPlanificacionCronograma;
	@EJB
	MatriculaServicio servRhpfMatriculaServicio;
	@EJB
	CarreraServicio servRhpfCarreraServicio;
	@EJB
	MateriaServicio servRhpfMateriaServicio;
	@EJB
	FichaEstudianteServicio servRhpfFichaEstudiante;
	@EJB
	UsuarioRolServicio servRhpfUsuarioRol;
	@EJB
	RolServicio servRhpfRol;
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
		rhpfUsuario = usuario;
		rhpfBloqueoModal = 1;
		iniciarParametros();
		return "irAlistarEstudianteHistoricoPosgrado";
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
			rhpfBloqueaHomologacion= false; //render  No se presnte chk homologacion.
			rhpfBloqueaBotonFinalizar= false;  //Por defecto se habilita el botón
			rhpfBloqueaPeriodoPosgrado= false; // No se presenta la lista de periodos posgrado
			rhpfBloqueaSubirArchivo= true;  // disabled presenta por defecto
			// Inicio los objetos
			rhpfPersonaDtoBuscar = new PersonaDto();
			rhpfPersonaDtoBuscar.setPrsIdentificacion("");
			rhpfCarreraDtoBuscar = new CarreraDto();
			rhpfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
			// seteo la carrera para que busque por todas
			rhpfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			// inicio la lista de Personas
			rhpfPersonaDtoEditar = null;
			rhpfListPersonaDto=null;
			rhpfListCarreraDto = new ArrayList<>();
		
			rhpfNombreArchivoAuxiliar=null;
		    rhpfNombreArchivoSubido=null;
		   // rhpfPeriodoAcademicoActivo= null;
		    rhpfListaPracActivoPosgrado= new ArrayList<>();
		 
			List<CarreraDto> listCarrerasDtoAux= new ArrayList<>();	 
			
			// busco el periodo academico activo pregrado
			//rhpfPeriodoAcademicoActivo = servRhpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			// busco los periodo academico activo posgrado
			rhpfListaPracActivoPosgrado = servRhpfPeriodoAcademico.buscarXestadolistPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
				
			//Busco carreras si el usuario es secreposgrado
	
			if((rhpfListaPracActivoPosgrado!=null)&&(rhpfListaPracActivoPosgrado.size()>0)){
			listCarrerasDtoAux = servRhpfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXListaPrac(
					rhpfUsuario.getUsrId(), RolConstantes.ROL_SECREPOSGRADO_VALUE,
					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rhpfListaPracActivoPosgrado);
		  }
		
			if((listCarrerasDtoAux!=null)&&(listCarrerasDtoAux.size()>0)){
			rhpfListCarreraDto.addAll(listCarrerasDtoAux); //agrego las carreras de posgrado
			}
	    	
			
			if((rhpfListCarreraDto!=null)||(rhpfListCarreraDto.size()>0)){
				
				// BUSCO PERSONAS SIN HOMOLOGAR DE  POSGRADO  SIN IMPORTAR EL PERIODO DE LA FICHA_INSCRIPCION				
				rhpfListPersonaDto = servRhpfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcion(
						rhpfListCarreraDto, rhpfCarreraDtoBuscar.getCrrIdentificador(),
						GeneralesConstantes.APP_ID_BASE, FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE,
						FichaInscripcionConstantes.INACTIVO_VALUE,
						rhpfPersonaDtoBuscar.getPrsIdentificacion().trim());
				
			}else{
				//TODO:  
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontró carreras asignadas a su usuario.");
			}
			rhpfValidadorClic = new Integer(0);
			rhpfFichaEstudiante = null;
			
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró periodo académico activo y de pregrado ");
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			//FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró estudiantes inscritos para registrar homologación en la carrera");
		}

	}

	/**
	 * Método para buscar las personas con los parámetros ingresados en los
	 * filtros de busqueda
	 */
	public void buscar() {
		// buscar los estudiantes por carrera y descripción
		try {
			rhpfListPersonaDto = null;
			//PREGRADO Y POSGRADO  SIN IMPORTAR EL PERIODO DE LA FICHA_INSCRIPCION		
			rhpfListPersonaDto = servRhpfPersonaDto.listarXCarreraXRolXTipoInscripcionXestadoInscripcion(
					rhpfListCarreraDto, rhpfCarreraDtoBuscar.getCrrIdentificador(),
					GeneralesConstantes.APP_ID_BASE, FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE,
					FichaInscripcionConstantes.INACTIVO_VALUE,
					rhpfPersonaDtoBuscar.getPrsIdentificacion().trim());
		} catch (PersonaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró estudiantes inscritos para realizar el registro.");
		}
	}

	/**
	 * Método para inicar el registro de homologación del estudiante seleccionado
	 * @return navegación a la pagina de registrar las homologaciones del estudiante
	 */
	public String irRegistrarHomologacion(PersonaDto rhpfPersonaDto) {
		//VARIABLES
		rhpfPersonaDtoEditar = rhpfPersonaDto;
		rhpfValidadorClic = 0;
		rhpfBloqueaEstadoIngreso= true;  //disables :  Si por defecto
		rhpfBloqueaHomologacion= false; //render: oculto por defecto 
		rhpfBloqueaBotonFinalizar=false;   // Por defecto se habilita el boton
		rhpfBloqueaPeriodoPosgrado= false; // render, escondo la lista de periodos posgrado
		rhpfBloqueaSubirArchivo= true;  // render, presenta por defecto
		rhpfNombreArchivoAuxiliar=null;
	    rhpfNombreArchivoSubido=null;

	    rhpfRol= null;
	    rhpfUsuarioRol= null;
	    rhpfCrearMatriculaPosgrado=false;
	    rhpfNivelActualPosgrado=0;
	    //LISTAS
	    rhpfListMateriaDto= new ArrayList<>();
	
		rhpfListHomologadosDto = new ArrayList<MateriaDto>();
		rhpfListRecordTotal = new ArrayList<>();
		rhpfListRecordCarreraAnterior = new ArrayList<MateriaDto>();
		rhpfListaRolesUsuario = null;
		rhpfListMateriaNivelPosgrado= new ArrayList<>();
		
		//SETEO DE PARAMETROS
		rhpfPersonaDtoEditar.setFcinEstadoIngreso(GeneralesConstantes.APP_ID_BASE);
		rhpfPersonaDtoEditar.setFcinPeriodoPosgradoId(GeneralesConstantes.APP_ID_BASE);
		//LISTAS AUXILIARES
		List<MateriaDto> listMateriasMallaAux = new ArrayList<MateriaDto>();
	    				
		//OBJETOS AUXILIARES
		Carrera carreraAux = new Carrera(); 
		
		//BUSCO LA LISTA DE ROLES DEL USUARIO
		rhpfListaRolesUsuario=servRhpfRol.listarRolesXUsrId(rhpfUsuario.getUsrId());	
		if(rhpfListaRolesUsuario!=null){
			boolean rolEncontrado= false;
		  //BUSCO EL ROL DE SECRETARIA DE CARRERA
			for (Rol rol : rhpfListaRolesUsuario) {
			if(rol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE){
				
				try {
					rhpfRol=servRhpfRol.buscarPorId(rol.getRolId());
				} catch (RolNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No se encontró rol al buscar por id");
					return null;
				} catch (RolException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Error desconocido al buscar rol por id");
					return null;
				}
				rolEncontrado = true;
				break;
			}
		}
		
		
		if(!rolEncontrado){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Su usuario no tienen el rol adecuado para continuar con el proceso");
			return null;
		  }
		
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se ha encontrado rol alguno asignado a su usuario");
			return null;
			
		}
	
		       
		try {
			
			//Busco el usuario rol del usuario
			rhpfUsuarioRol= servRhpfUsuarioRol.buscarXUsuarioXrol(rhpfUsuario.getUsrId(), rhpfRol.getRolId());
				
		
			// proceso para buscar la planificacion cronograma activa
			// creo la lista de proceso flujo correspondiente a la matricula
			List<Integer> listProcesoFlujoAux = new ArrayList<Integer>();
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			
			// Busqueda del período academico HOMOLOGACION
			rhpfPeriodoAcademico = servRhpfPeriodoAcademico.buscarPDescripcion(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_LABEL);
			
		
			// listo planificaciones cronograma de matriculas

			// TODO: VERIFICAR EL TIPO DE CRONOGRAMA
			List<PlanificacionCronograma> listPlanificacionCronogramaAux;
			listPlanificacionCronogramaAux = servRhpfPlanificacionCronograma
					.buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(rhpfPeriodoAcademico.getPracId(),
							listProcesoFlujoAux, CronogramaConstantes.TIPO_HOMOLOGACION_VALUE);
			
			
			// validacion de las fechas del cronograma
			int cont = 0;
			int contNumPlanificacion = 0;
			for (PlanificacionCronograma item : listPlanificacionCronogramaAux) {
				contNumPlanificacion++;
				if (item.getPlcrEstado() == PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE) {
					rhpfPlanificacionCronograma = item;
					cont++;
				}
				if (cont == 0 && contNumPlanificacion == listPlanificacionCronogramaAux.size()) {
					// TODO: GENERAR EL MENSAJE
					// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.encuesta.validacion.exception")));
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No existen planificacion cronomograma activo, por favor comuniquese con el administrador del sistema");
					return null;
				}
			}

			
		 	//BUSCO LA CARRERA CON crr_id de la ficha Inscripcion
            carreraAux = servRhpfCarreraServicio.buscarPorId(rhpfPersonaDtoEditar.getCrrId());
			
			// Buscar la malla curricular vigente y activa de la carrera
			rhpfMallaCurricular = servRhpfMallaCurricular.buscarXcarreraXvigenciaXestado(rhpfPersonaDtoEditar.getCrrId(),MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			
			//Busco materias de toda la malla
			listMateriasMallaAux =   servRhpfMateriaDto.listarXmalla(rhpfMallaCurricular.getMlcrId());
			
			//CARGO PARARLELO HISTORICO A TODAS LAS MATERIAS DE LA MALLA
			for (MateriaDto itmMateria : listMateriasMallaAux) {
								
				
				//BUSCO EL PARALELO HOMOLOGACiÓN  o PARALELO HISTORICO  SEGUN EL CASO
				if (carreraAux.getCrrTipo()==CarreraConstantes.TIPO_POSGRADO_VALUE) {
					rhpfParaleloHomologacion = servRhpfParaleloDto.buscarXmallaMateriaXperiodoXDescripcion(
							itmMateria.getMlcrmtId(), PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE,ParaleloConstantes.PARALELO_REGISTRO_HISTORICO_LABEL);
					
				}else{
					FacesUtil.limpiarMensaje();
					// TODO: COLOCAR ESTE MENSAJE
					FacesUtil.mensajeError("La carerra no tiene un tipo valido");
					return null;
					
				}
				
				itmMateria.setPrlId(rhpfParaleloHomologacion.getPrlId()); //Se agrega a cada materia el paralelo homologación
				itmMateria.setPrlDescripcion(rhpfParaleloHomologacion.getPrlDescripcion());
				itmMateria.setMlcrprId(rhpfParaleloHomologacion.getMlcrprId());
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
				//materia.setNotaUno(null);
				//materia.setNotaDos(null);
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
				/*
				 * BLOQUE DE CONTROL DE OPCIONES EN LA VISTA
				 */
				
			 if(rhpfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_VALUE){
				   
				   rhpfListaPeriodosPorCarreraPosGrado=servRhpfPeriodoAcademico.listarXestadoPosgrado(rhpfPersonaDtoEditar.getCrrId(), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				   rhpfPersonaDtoEditar.setFcinEstadoIngreso(FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE); //HOMOLOGA
				   rhpfPersonaDtoEditar.setFcinPeriodoPosgradoId(GeneralesConstantes.APP_ID_BASE);
				   rhpfBloqueaEstadoIngreso= true; //Se bloquea el combo estado Ingreso
					rhpfBloqueaHomologacion= true;  //Se habilita lista de checks Rendered
					rhpfBloqueaPeriodoPosgrado=true; //Se presenta el combo para seleccionar el periodo de posgrado
					
			   } else{
				   FacesUtil.limpiarMensaje();
				   FacesUtil.mensajeError("El tipo de ingreso  del estudiante no es valido");
				   rhpfBloqueaBotonFinalizar= true; //bloqueo el boton finalizar
			   }
				
				
			//ALTERNO:  COPIO  LA MALLA COMPLETA SIN RETIRAR MATERIAS TOMADAS POR EL ESTUDIANTE
		if(listMateriasMallaAux!=null){
			for (MateriaDto materiaDtoMalla : listMateriasMallaAux) {
				rhpfListMateriaDto.add(materiaDtoMalla);
			}
		}	
			
			
			// buscar el tipo de sistema de calificaciones por la descripcion :"PREGRADO"
			rhpfTipoSistemaCalificacion = servRhpfTipoSistemaCalificacion.buscarXDescripcion(TipoSistemaCalificacionConstantes.SISTEMA_CALIFICACION_PREGRADO_LABEL);
			
			// buscar el sistema de calificaciones por el periodo academiico y por el tipo sistema de calificaciones
			rhpfSistemaCalificacion = servRhpfSistemaCalificacion.listarSistemaCalificacionXPracXtissclXEstado(
					rhpfPeriodoAcademico.getPracId(), rhpfTipoSistemaCalificacion.getTissclId(),
					SistemaCalificacionConstantes.INACTIVO_VALUE);
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			// TODO: COLOCAR ESTE MENSAJE
			FacesUtil.mensajeError("No existe periodo academico con descripcion 'HOMOLOGACIÓN', comuniquese con el administrador del sistema.");
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MallaCurricularNoEncontradoException e) {
			//FacesUtil.mensajeError(e.getMessage());
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró malla curricular vigente y activa, comuniquese con el administrador del sistema.");
			return null;
		} catch (MallaCurricularException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
     		FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			//	FacesUtil.mensajeError(e.getMessage());
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró materias en la malla curricular, comuniquese con el administrador del sistema.");
			return null;
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			// TODO: COLOCAR ESTE MENSAJE
			FacesUtil.mensajeError("Existen materias sin el paralelo HOMOLOGACIÓN O HISTÓRICO, comuniquese con el administrador del sistema. ");
			return null;
		} catch (TipoSistemaCalificacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró tipo de sistema de calificación: PREGRADO, comuniquese con el administrador del sistema.");
			return null;
		} catch (TipoSistemaCalificacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (SistemaCalificacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró sistema de calificación de tipo PREGRADO e inactivo, comuniquese con el administrador del sistema.");
			return null;
		} catch (SistemaCalificacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró planificación cronográma de tipo CRONOGRAMA HOMOLOGACIÓN , comuniquese con el administrador del sistema.");
			return null;
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}  catch (CarreraNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		} catch (CarreraException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
			return null;
		}  catch (UsuarioRolException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar usuario-rol.");
			return null;
		} catch (UsuarioRolNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se ha encontrado usuario-rol asignado a su usuario.");
			return null;
		}
		
		
		
  if(rhpfPersonaDtoEditar.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_VALUE){
	     return "irRegistrarPosgrado"; //vista para registrar historial de posgrado.
	  
      }else{
    	FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("El estudiante no tiene un tipo de ingreso valido para registro por homologacion.");
		return null;
     }
     
	}

		

	
	/**
	 * Método para inicar el registro del notas de posgrado del estudiante, en BDD.
	 * @return navegación a la pagina xhtml listar estudiantes.
	 */
	public String guardarRegistroPosgrado() {
			rhpfValidadorClic = new Integer(0);
			
			try {
				
				if(servRhpfMatriculaServicio.generarMatriculaRegHistoricoPosgrado(rhpfListHomologadosDto, 
						rhpfPersonaDtoEditar, 0, 0,GeneralesConstantes.APP_CERO_VALUE, rhpfPlanificacionCronogramaPosgrado, rhpfNombreArchivoSubido, rhpfFichaEstudiante, rhpfUsuarioRol, 
						rhpfCrearMatriculaPosgrado, rhpfListMateriaNivelPosgrado, rhpfNivelActualPosgrado)){
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Registro  guardado exitosamente");
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Se produjo un error al guardar el registro");
				}
				
			} catch (MatriculaValidacionException e) {
				// validador click en 0
				rhpfValidadorClic = new Integer(0);
				FacesUtil.mensajeInfo(e.getMessage());
			} catch (MatriculaException e) {
				// validador click en 0
				rhpfValidadorClic = new Integer(0);
				FacesUtil.mensajeInfo(e.getMessage());
			} 
			iniciarParametros();
		
		return "irAlistarEstudianteHistoricoPosgrado";
	}
	
	

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	/**
	 * Método para inicar el registro del histórico del estudiante
	 * @return navegación a la pagina de listar estudiantes a homologar.
	 */
	public String cancelarHistorico() {
		rhpfPlanificacionCronograma = new PlanificacionCronograma();
		rhpfPeriodoAcademico = new PeriodoAcademico();
		rhpfMallaCurricular = new MallaCurricular();
		rhpfListMateriaDto = null;
		rhpfListParaleloDto = null;
		rhpfCarreraDtoBuscar.setCrrIdentificador(GeneralesConstantes.APP_ID_BASE);
		rhpfTipoSistemaCalificacion = new TipoSistemaCalificacion();
		rhpfSistemaCalificacion = new SistemaCalificacion();
		rhpfValidadorClic = new Integer(0);
		rhpfPersonaDtoEditar= null;
		return "irAlistarEstudianteHistoricoPosgrado";
	}

	
	

	
	/**
	 * Verifica información al hacer click en el boton guardar  Posgrado
	 */
	public String verificarClickRegistrarPosgrado() {
	
		rhpfFichaEstudiante = new FichaEstudiante();
		rhpfListHomologadosDto = new ArrayList<>();
		rhpfListMateriaNivelPosgrado = new ArrayList<>();
		rhpfPeriodoAcademicoPosgrado=null;
		rhpfPlanificacionCronogramaPosgrado= null;
		rhpfListaParaleloNuevaMatrPosgrado= new ArrayList<>();
		
		//VARIABLES
		rhpfValidadorClic = 0;
		//AUXILIARES
		List<FichaEstudiante> auxListaFichasEstudiante = new ArrayList<>();
	 	
	   //BUSCO FICHAS ESTUDIANTE POR LA FICHAS_INSCRIPCION EN LA CARRERA
		 try {
			auxListaFichasEstudiante = servRhpfFichaEstudiante.listarPorFcinId(rhpfPersonaDtoEditar.getFcinId());
			
			if (auxListaFichasEstudiante == null || auxListaFichasEstudiante.size() <= 0) {// NO TIENE FCES en la carrera
				rhpfFichaEstudiante = null;

			} else if (auxListaFichasEstudiante.size() == 1) { // TIENE FICHA ESTUDIANTE

				//rhpfFichaEstudiante = servRhpfFichaEstudiante.buscarPorId(auxListaFichasEstudiante.get(0).getFcesId());
				rhpfFichaEstudiante = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El estudiante presenta ficha estudiante en la carrera, comuniquese con el administrador del sistema.");
				return null;
			} else {
				rhpfFichaEstudiante = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El estudiante presenta varias fichas estudiante en la carrera, comuniquese con el administrador del sistema.");
				return null;
			}
			
		//VERIFICACIONES PARA PERMITIR GRABAR
			     //Verifico que seleccione período académico
				if((rhpfPersonaDtoEditar.getFcinPeriodoPosgradoId()!=GeneralesConstantes.APP_ID_BASE) ){ 
					
					// proceso para buscar la planificacion cronograma activa
					// creo la lista de proceso flujo correspondiente a la matricula
					List<Integer> listProcesoFlujoAux = new ArrayList<Integer>();
					listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
					listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
					listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
					// Busqueda del período academico HOMOLOGACION
			
					// Busqueda del periodo de ultima fichaInscripcion del estudiante
					rhpfPeriodoAcademicoPosgrado= servRhpfPeriodoAcademico.buscarPorId(rhpfPersonaDtoEditar.getFcinPeriodoPosgradoId());
					// listo planificaciones cronograma de matriculas
					// VERIFICAR EL TIPO DE CRONOGRAMA
					List<PlanificacionCronograma> listPlanificacionCronogramaAux;
					listPlanificacionCronogramaAux = servRhpfPlanificacionCronograma
							.buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(rhpfPeriodoAcademicoPosgrado.getPracId(),
									listProcesoFlujoAux, CronogramaConstantes.TIPO_POSGRADO_VALUE);
					
					// validacion de las fechas del cronograma
					int cont = 0;
					int contNumPlanificacion = 0;
					for (PlanificacionCronograma item : listPlanificacionCronogramaAux) {
						contNumPlanificacion++;
						if (item.getPlcrEstado() == PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE) {
							rhpfPlanificacionCronogramaPosgrado = item;
							cont++;
						}
						if (cont == 0 && contNumPlanificacion == listPlanificacionCronogramaAux.size()) {
							// TODO: GENERAR EL MENSAJE
							// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.encuesta.validacion.exception")));
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("No existen planificacion cronomograma activo, por favor comuniquese con el administrador del sistema");
							return null;
						}
					}
					
					//Posgrado por defecto se homologa.
					if(rhpfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){
						
						for (MateriaDto it : rhpfListMateriaDto) {
							if (it.getEsHomologado()==true) {
								rhpfListHomologadosDto.add(it); //agrego las materias selecionadas y con notas a la lista de materias homologadas
								
							}
						}
					
						//Verifico que exista alguna materia homologada para permitir guardar.
						if (rhpfListHomologadosDto.size() <= 0) { // no existe materias homologadas
							rhpfValidadorClic = 0;  //inactivo modal guardar
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("No se han generado registros a guardar.");
						} else {
							
							//BUSCAMOS EL PARALELO PARA LA NUEVA MATRICULA EN EL PERIODO, NIVEL Y CARRRERA SELECCIONADOS:  EXISTE SOLO UN PARALELO POR NIVEL.
							rhpfListaParaleloNuevaMatrPosgrado=servRhpfParaleloDto.listarParaleloXPeriodoCarreraNivelPostgradoNuevo(rhpfPersonaDtoEditar.getFcinPeriodoPosgradoId(), rhpfPersonaDtoEditar.getCrrId(), rhpfNivelActualPosgrado+1);
			
							if((rhpfListaParaleloNuevaMatrPosgrado!=null)&&(rhpfListaParaleloNuevaMatrPosgrado.size()>0)){
								
							  if(rhpfListaParaleloNuevaMatrPosgrado.size()==1){ 
							
							     //Selecciono materias del nivel siguiente de posgrado
								for (MateriaDto materiaDto : rhpfListMateriaDto) {
								   if(materiaDto.getNvlId()==(rhpfNivelActualPosgrado+1)){
									MateriaDto materiaDtoAux= new MateriaDto();	
									materiaDtoAux.setMtrId(materiaDto.getMtrId());
									materiaDtoAux.setNvlId(materiaDto.getNvlId());
									MallaCurricularParaleloDto rhpfMlcrprDtoPosgradoAux;
									rhpfMlcrprDtoPosgradoAux=servMallaCurricularParaleloDto.buscarCupoMlcrprXMateriaXParalelo(rhpfListaParaleloNuevaMatrPosgrado.get(0).getPrlId(), materiaDto.getMtrId());
									materiaDtoAux.setMlcrprId(rhpfMlcrprDtoPosgradoAux.getMlcrprId()); //GUARDO MALLA CURRICULAR PARALELO
									rhpfListMateriaNivelPosgrado.add(materiaDtoAux);//CREO LA LISTA DE MATERIAS CON MLCRPR  PARA LA MATRICULA DEL SIGUIENTE NIVEL DE POSGRADO
										
								    }
						     	 }
							   }else{
								  FacesUtil.limpiarMensaje();
								  FacesUtil.mensajeError("Error se encontró mas de un paralelo de posgrado en el nivel");
							      return null;
							}
							
							}else{
								//PUEDE NO EXISTIR PARALELO EN EL SIGUIENTE NIVEL, SIEMPRE Y CUANDO LA LISTA DE HOMOLOGADOS SEA IGUAL A TODA LA MALLA.
								if(rhpfListHomologadosDto.size()!=rhpfListMateriaDto.size()){
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeError("No se encontró paralelo  del siguiente nivel al buscar  por carrera, nivel y periodo.");
									return null;
								}
															
								
							}
							
							
							//verifico nuevamente si se genera o no la nueva matricula
							Integer numMateriaReprobadas=0;
							for (MateriaDto it2 : rhpfListHomologadosDto) {
							if((it2.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)&&(it2.getEstadoHomologacionReg2()!=RecordEstudianteConstantes.ESTADO_APROBADO_VALUE)){
								numMateriaReprobadas++;				
							   }
							
							}
							
							
							boolean materiaReprobadoSegundaVez=false;
							for (MateriaDto itemMateria : rhpfListMateriaDto) {
								if ((itemMateria.getEsHomologado()==true)&&(itemMateria.getEsHomologadoReg2()==true)){
								   if((itemMateria.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)&&(itemMateria.getEstadoHomologacionReg2()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)){
									   materiaReprobadoSegundaVez=true;
									   break;
								   }
								}
							}
							
							//verifico si se debe realizar o no la matricula en el siguiente nivel
							if((numMateriaReprobadas>2)||(materiaReprobadoSegundaVez)||(rhpfListHomologadosDto.size()==rhpfListMateriaDto.size())){
								rhpfCrearMatriculaPosgrado=false;
							}else{
								rhpfCrearMatriculaPosgrado=true;
							}
							
							
							for (MateriaDto it1 : rhpfListHomologadosDto) {
								if (validarEstadoMateriaPosgrado(it1)) {//verifico nuevamente que todos las materia seleccionadas tengan un estado.
								   //Si se activo segundo registro se valida que exista Observación en cada registro
									
									if(validarModoAprobacionPosgrado(it1)	){
									if(it1.getEsHomologadoReg2()){
									  if(validarObservacionPosgradoReg2(it1)){
										  rhpfValidadorClic = 3; //activo modal guardar posgrado
									  }else{
										  rhpfValidadorClic = 0;
											FacesUtil.limpiarMensaje();
											FacesUtil.mensajeError("Los asignaturas con segundo registro de notas debe contener una observación");
											break;  
									  }
									
									}else{
									rhpfValidadorClic = 3;  //activo modal guardar posgrado
									}				
								
									
									}else{
										
										 rhpfValidadorClic = 0;
											FacesUtil.limpiarMensaje();
											FacesUtil.mensajeError("Seleccione un modo de aprobacion en todas las asignaturas aprobadas.");
											break;  
										
									}
									
									
								}else {
									rhpfValidadorClic = 0;
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeError("Los asignaturas seleccionadas deben tener un estado");
									break;
								}
								
								
							}
							
						}
					}else if (rhpfPersonaDtoEditar.getFcinEstadoIngreso()==FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE){
						rhpfValidadorClic = 3;
						rhpfListHomologadosDto= null;
					}else{
						rhpfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Debe seleccionar el estado de ingreso");
					}
	       }else{
	    	   rhpfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Debe seleccionar la cohorte del posgrado.");
	       }
		} catch (FichaEstudianteException e1) {
			rhpfFichaEstudiante = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar ficha estudiante por ficha inscripción.");
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró período académico seleccionado al buscar por id.");
			
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al al buscar período academico seleccionado por id.");
		
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró planificación cronograma de tipo posgrado.");
			
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar planificación cronograma de tipo posgrado.");
			
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar paralelo por carrera, nivel y periodo.");
			
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar  mallaCurricularParalelo  en posgrado por paralelo y materia.");
			
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró  mallaCurricularParalelo  en posgrado al buscar  por paralelo y materia.");
		
		}

		return null;
	}
	
	/**
	 * Metodo que habilita o no la columna check registrar homologacion
	 */
	public void bloqueaHomologacion(){
		
		if(rhpfPersonaDtoEditar.getFcinEstadoIngreso()!=FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE){ //No homologado
			rhpfBloqueaHomologacion= false; //ocultamos la columna check homologacion 
		}else{ //es homologado
			rhpfBloqueaHomologacion= true; //presentamos la columna check homologacion
		}
		
		if(rhpfListMateriaDto!=null){// si existen materia en la lista
			
			for (MateriaDto materiaDto : rhpfListMateriaDto) {
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
	 * Permite validar los que la información ingresada cumpla con os parametros establecidos.
	 */
	private boolean validarCasosDeUsoPosgrado(MateriaDto it) {
		boolean caso = true;
		if (it.getNotaSuma() == null || it.getNotaSuma().equals(new BigDecimal(0))) {
			caso = false;
			rhpfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos de cero en el Campo Nota .");
		}


		if (it.getClfPromedioAsistencia()== null || it.getClfPromedioAsistencia().equals(new BigDecimal(0))) {
			caso = false;
			rhpfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos de cero en el Campo porcentaje asistencia.");
		}

		
		return caso;
	}
	
	
	/**
	 * Permite validar los que la información ingresada cumpla con os parametros establecidos.
	 */
	private boolean validarCasosDeUsoPosgradoReg2(MateriaDto it) {
		boolean caso = true;
		if (it.getNotaSumaReg2() == null || it.getNotaSumaReg2().equals(new BigDecimal(0))) {
			caso = false;
			rhpfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos de cero en el Campo Nota .");
		}


		if (it.getClfPromedioAsistenciaReg2()== null || it.getClfPromedioAsistenciaReg2().equals(new BigDecimal(0))) {
			caso = false;
			rhpfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Ingrese valores distintos de cero en el Campo porcentaje asistencia.");
		}

		
		return caso;
	}
	
		
	
	/**
	 * Permite validar que los combos estado de reg1 y reg2 tengan estado si estan seleccionados
	 */
	private boolean validarEstadoMateriaPosgrado(MateriaDto it) {
		boolean caso = true;
		
		if ( it.getEstadoHomologacion()==GeneralesConstantes.APP_ID_BASE) {
			caso = false;
			rhpfValidadorClic = 0;
		}
		
		if(it.getEsHomologadoReg2()){
		  if ( it.getEstadoHomologacionReg2()==GeneralesConstantes.APP_ID_BASE) {
			caso = false;
			rhpfValidadorClic = 0;
		  }
		}
		
		
		return caso;
	}
	
	
	/**
	 * Permite validar que los combos modo de aprobacion de reg1 y reg2 tengan modo de aprobacion si el estado es aprobado
	 */
	private boolean validarModoAprobacionPosgrado(MateriaDto it) {
		boolean caso = true;
		
		if (( it.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) && (it.getRcesModoAprobacionNota1()==GeneralesConstantes.APP_ID_BASE)){
			caso = false;
			rhpfValidadorClic = 0;
		}
		
		if(it.getEsHomologadoReg2()){
		  if (( it.getEstadoHomologacionReg2()==RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) && (it.getRcesModoAprobacionNota2()==GeneralesConstantes.APP_ID_BASE)) {
			caso = false;
			rhpfValidadorClic = 0;
		  }
		}
		
		
		return caso;
	}
	/**
	 * Permite validar los que la información de observacion en cada fila cumpla con los parametros establecidos.
	 */
	private boolean validarObservacionPosgradoReg2(MateriaDto it) {
		boolean caso = true;
		
		if ((it.getRcesObservacion()==null)||( it.getRcesObservacion().length()==0)||(it.getRcesObservacion().trim().isEmpty())) {
			caso = false;
			rhpfValidadorClic = 0;
		}
		
		return caso;
	}
	
		
	/**
	 * calcula el promedio de las dos notas y asistencia
	 */
	public void calcularPromedioPosgrado(MateriaDto materiaDto) {

		if (validarCasosDeUsoPosgrado(materiaDto)) {
			BigDecimal notaSuma = materiaDto.getNotaSuma();
			Float promedio = materiaDto.getClfPromedioAsistencia();
			BigDecimal valor = new BigDecimal(7.0); //defino variable BigDecimal y asigno el valor de aprobacion de posgrado
			int result = notaSuma.compareTo(valor); //comparo si la nota ingresada es mayor o menor que 7.0
			
			if((result>=0)&&(promedio>=80)){
				// aprobado por nota y promedio
				materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula
				materiaDto.setAprobado(true); // seteo el aprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
				
				//Si se calcula aprobado luego de haber estado reprobado, se setea nuevamente el segundo registro
				materiaDto.setEsHomologadoReg2(false);
				//materia.setNotaUno(null);
				//materia.setNotaDos(null);
				materiaDto.setNotaSumaReg2(null);
				materiaDto.setClfPromedioAsistenciaReg2(null);
				materiaDto.setNumMatriculaReg2(GeneralesConstantes.APP_ID_BASE);
				materiaDto.setEstadoHomologacionReg2(GeneralesConstantes.APP_ID_BASE);
				materiaDto.setRcesObservacion(null);
				materiaDto.setRcesModoAprobacionNota1(GeneralesConstantes.APP_ID_BASE);
				materiaDto.setRcesComboModoAprobacion1(Boolean.FALSE); //habilito combo modo de aprobacion
			
				
			}else{
				materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula
				materiaDto.setAprobado(false); // seteo el reprobado de la materia
				materiaDto.setEstadoHomologacion(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				materiaDto.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
				materiaDto.setRcesModoAprobacionNota1(GeneralesConstantes.APP_ID_BASE);
				materiaDto.setRcesComboModoAprobacion1(Boolean.TRUE); //deshabilito combo modo de aprobacion
				
			}
			
			//CALCULO EL NÚMERO DE ESTADOS REPROBADOS--MAXIMO PUEDE REPROBAR 2 MATERIAS
			Integer numMateriaReprobadas =0;
			for (MateriaDto itemMateria : rhpfListMateriaDto) {
				if (itemMateria.getEsHomologado()==true){
				   if((itemMateria.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)&&(itemMateria.getEstadoHomologacionReg2()!=RecordEstudianteConstantes.ESTADO_APROBADO_VALUE)){
					numMateriaReprobadas++;				
				   }
				}
			}
			
			//CALCULO SI UNA MATERIA ESTA REPROBADA POR SEGUNDA VEZ
			boolean materiaReprobadoSegundaVez=false;
			for (MateriaDto itemMateria : rhpfListMateriaDto) {
				if ((itemMateria.getEsHomologado()==true)&&(itemMateria.getEsHomologadoReg2()==true)){
				   if((itemMateria.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)&&(itemMateria.getEstadoHomologacionReg2()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)){
					   materiaReprobadoSegundaVez=true;
					   break;
				   }
				}
			}
			
			
			if(numMateriaReprobadas>2){
				
				rhpfCrearMatriculaPosgrado=false;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El estudiante presenta más de 2 asignaturas reprobada, el registro se guardará pero no se realizará la nueva matrícula ");
				
			}else if(materiaReprobadoSegundaVez){
				rhpfCrearMatriculaPosgrado=false;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El estudiante ha reprobado por segunda vez una materia, el registro se guardará pero no se realizará la nueva matrícula ");
				
			}else{
				rhpfCrearMatriculaPosgrado=true;
				
			}
			
		}
		
	}

	
	/**
	 * calcula el promedio de las dos notas y asistencia
	 */
	public void calcularPromedioPosgradoReg2(MateriaDto materiaDto) {

		if (validarCasosDeUsoPosgradoReg2(materiaDto)) {
			BigDecimal notaSumaReg2 = materiaDto.getNotaSumaReg2();
			Float promedioReg2 = materiaDto.getClfPromedioAsistenciaReg2();
			BigDecimal valorReg2 = new BigDecimal(7.0); //defino variable BigDecimal y asigno el valor de aprobacion de posgrado
			int resultReg2 = notaSumaReg2.compareTo(valorReg2); //comparo si la nota ingresada es mayor o menor que 7.0
			
			if((resultReg2>=0)&&(promedioReg2>=80)){
				// aprobado por nota y promedio
				materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula
				materiaDto.setAprobadoReg2(true); // seteo el aprobado de la materia
				materiaDto.setEstadoHomologacionReg2(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
				materiaDto.setNumMatriculaReg2(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE);
				materiaDto.setRcesModoAprobacionNota2(GeneralesConstantes.APP_ID_BASE);
				materiaDto.setRcesComboModoAprobacion2(Boolean.FALSE); //habilito combo de modo de aprobacion 2
				
			}else{
				materiaDto.setMtrCmbEstado(Boolean.TRUE); //deshabilita vista de combo NumMatricula
				materiaDto.setAprobadoReg2(false); // seteo el reprobado de la materia
				materiaDto.setEstadoHomologacionReg2(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
				materiaDto.setNumMatriculaReg2(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE);
				materiaDto.setRcesModoAprobacionNota2(GeneralesConstantes.APP_ID_BASE);
				materiaDto.setRcesComboModoAprobacion2(Boolean.TRUE); //deshabilito combo de modo de aprobacion 2 
				
			}
			
		}
		
		
		//CALCULO EL NÚMERO DE ESTADOS REPROBADOS--MAXIMO PUEDE REPROBAR 2 MATERIAS
		Integer numMateriaReprobadas =0;
		for (MateriaDto itemMateria : rhpfListMateriaDto) {
			if (itemMateria.getEsHomologado()==true){
			   if((itemMateria.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)&&(itemMateria.getEstadoHomologacionReg2()!=RecordEstudianteConstantes.ESTADO_APROBADO_VALUE)){
				numMateriaReprobadas++;				
			   }
			}
		}
		
		boolean materiaReprobadoSegundaVez=false;
		for (MateriaDto itemMateria : rhpfListMateriaDto) {
			if ((itemMateria.getEsHomologado()==true)&&(itemMateria.getEsHomologadoReg2()==true)){
			   if((itemMateria.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)&&(itemMateria.getEstadoHomologacionReg2()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)){
				   materiaReprobadoSegundaVez=true;
				   break;
			   }
			}
		}
		
		
		if(numMateriaReprobadas>2){
			
			rhpfCrearMatriculaPosgrado=false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("El estudiante presenta más de 2 asignaturas reprobada, el registro se guardará pero no se realizará la nueva matrícula ");
			
		}else if(materiaReprobadoSegundaVez){
			rhpfCrearMatriculaPosgrado=false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("El estudiante ha reprobado por segunda vez una materia, el registro se guardará pero no se realizará la nueva matrícula ");
			
		}else{
			rhpfCrearMatriculaPosgrado=true;
			
		}
		
	}
	
	
	/**
	 * método que setear campos  al checkear es homologado en cada fila
	 */

		public void setearCamposPosgrado(MateriaDto materia) {
		
			
			if (materia.getEsHomologado()) {
				rhpfNivelActualPosgrado=materia.getNvlId();
				for (MateriaDto itemMateria : rhpfListMateriaDto) {
					if(itemMateria.getNvlId()<=materia.getNvlId()){
						itemMateria.setEsHomologado(true);
					}
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Ingrese valores distintos de cero en los campos Nota y Porcentaje de Asistencia.");
			}else {
				if(materia.getNvlId()>0){
				rhpfNivelActualPosgrado=materia.getNvlId()-1;
				
				
				for (MateriaDto itemMateria : rhpfListMateriaDto) {
					if(itemMateria.getNvlId()>=materia.getNvlId()){
						itemMateria.setEsHomologado(false);
						itemMateria.setNotaUno(null);
						itemMateria.setNotaDos(null);
						itemMateria.setNotaSuma(null);
						itemMateria.setClfPromedioAsistencia(null);
						itemMateria.setMtrCmbEstado(Boolean.TRUE);
						itemMateria.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
						itemMateria.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
						itemMateria.setRcesModoAprobacionNota1(GeneralesConstantes.APP_ID_BASE); //Modo de aprobacion del registro1 de la nota
						
						itemMateria.setEsHomologadoReg2(false);
						//itemMateria.setNotaUno(null);
						//itemMateria.setNotaDos(null);
						itemMateria.setNotaSumaReg2(null);
						itemMateria.setClfPromedioAsistenciaReg2(null);
						itemMateria.setNumMatriculaReg2(GeneralesConstantes.APP_ID_BASE);
						itemMateria.setEstadoHomologacionReg2(GeneralesConstantes.APP_ID_BASE);
						itemMateria.setRcesModoAprobacionNota2(GeneralesConstantes.APP_ID_BASE);  //Modo de aprobacion del registro2 de la nota
						itemMateria.setRcesObservacion(null);
					   itemMateria.setRcesComboModoAprobacion1(Boolean.TRUE); //Por defecto dehabilito combo de modo de aprobacion 1
					   itemMateria.setRcesComboModoAprobacion2(Boolean.TRUE); //Por defecto dehabilito combo de modo de aprobacion 2

						
					}
					
				}
				
			
	//			rhpfBloqueaNumMatricula = true;

				}
//				materia.setNotaUno(null);
//				materia.setNotaDos(null);
//				materia.setNotaSuma(null);
//				materia.setClfPromedioAsistencia(null);
//				materia.setMtrCmbEstado(Boolean.TRUE);
//				materia.setNumMatricula(GeneralesConstantes.APP_ID_BASE);
//				materia.setEstadoHomologacion(GeneralesConstantes.APP_ID_BASE);
//				rhpfBloqueaNumMatricula = true;

				
			}
		

	}

		
	/**
	 * método que setear campos  al checkear es homologado en cada fila, para el segundo registro de notas posgrado
	 */

			public void setearCamposPosgradoReg2(MateriaDto materia) {
		  //Comprobar que el primer ingreso de notas debe estar en estado reprobado
			if((materia.getEsHomologado())&&(materia.getEstadoHomologacion()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE)){
				
				if (materia.getEsHomologadoReg2()) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Ingrese valores distintos de cero en los campos Nota y Porcentaje de Asistencia del segundo registro.");
				}else {
					materia.setEsHomologadoReg2(false);
					//materia.setNotaUno(null);
					//materia.setNotaDos(null);
					materia.setNotaSumaReg2(null);
					materia.setClfPromedioAsistenciaReg2(null);
				    materia.setMtrCmbEstado(Boolean.TRUE);
					materia.setNumMatriculaReg2(GeneralesConstantes.APP_ID_BASE);
					materia.setEstadoHomologacionReg2(GeneralesConstantes.APP_ID_BASE);
					materia.setRcesModoAprobacionNota2(GeneralesConstantes.APP_ID_BASE);
					materia.setRcesObservacion(null);
					materia.setRcesComboModoAprobacion2(Boolean.TRUE);//deshabilito combo modo de aprobacion
				
				//	rhpfBloqueaNumMatricula = true;
				}
				
			}else{
				materia.setEsHomologadoReg2(false);
				//materia.setNotaUno(null);
				//materia.setNotaDos(null);
				materia.setNotaSumaReg2(null);
				materia.setClfPromedioAsistenciaReg2(null);
			    materia.setMtrCmbEstado(Boolean.TRUE);
				materia.setNumMatriculaReg2(GeneralesConstantes.APP_ID_BASE);
				materia.setEstadoHomologacionReg2(GeneralesConstantes.APP_ID_BASE);
				materia.setRcesModoAprobacionNota2(GeneralesConstantes.APP_ID_BASE);
				materia.setRcesObservacion(null);
				materia.setRcesComboModoAprobacion2(Boolean.TRUE);//deshabilito combo modo de aprobacion
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El primero registro de notas en esta asignatura debe presentar el estado reprobado");
				
			}

		}

	
	
	
	//CARGA DE ARCHIVO
		/**
		 * Método para cargar el archivo en ruta temporal
		 * @param event - event archivo oficio que presenta el estudiante para anular matrícula
		 */
		public void handleFileUpload(FileUploadEvent archivo) {
			rhpfNombreArchivoSubido = archivo.getFile().getFileName();
			rhpfNombreArchivoAuxiliar = archivo.getFile().getFileName();
			String rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rhpfNombreArchivoSubido;
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
			
			ReporteHomologacionForm.generarReporteHomologacion(rhpfListHomologadosDto, rhpfUsuario, rhpfPersonaDtoEditar );
			rhpfActivadorReporte = 1;
							
		}
		
		/**
		 * Método para desactivar modal guardar
		 * 
		 */ 
		public void desactivaModalGuardar(){
			rhpfValidadorClic = 0;
		}
		
	
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getRhpfUsuario() {
		return rhpfUsuario;
	}

	public void setRhpfUsuario(Usuario rhpfUsuario) {
		this.rhpfUsuario = rhpfUsuario;
	}

	public List<PersonaDto> getRhpfListPersonaDto() {
		rhpfListPersonaDto = rhpfListPersonaDto == null ? (new ArrayList<PersonaDto>()) : rhpfListPersonaDto;
		return rhpfListPersonaDto;
	}

	public void setRhpfListPersonaDto(List<PersonaDto> rhpfListPersonaDto) {
		this.rhpfListPersonaDto = rhpfListPersonaDto;
	}

	public PersonaDto getRhpfPersonaDtoBuscar() {
		return rhpfPersonaDtoBuscar;
	}

	public void setRhpfPersonaDtoBuscar(PersonaDto rhpfPersonaDtoBuscar) {
		this.rhpfPersonaDtoBuscar = rhpfPersonaDtoBuscar;
	}

	public List<CarreraDto> getRhpfListCarreraDto() {
		rhpfListCarreraDto = rhpfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : rhpfListCarreraDto;
		return rhpfListCarreraDto;
	}

	public void setRhpfListCarreraDto(List<CarreraDto> rhpfListCarreraDto) {
		this.rhpfListCarreraDto = rhpfListCarreraDto;
	}

	public CarreraDto getRhpfCarreraDtoBuscar() {
		return rhpfCarreraDtoBuscar;
	}

	public void setRhpfCarreraDtoBuscar(CarreraDto rhpfCarreraDtoBuscar) {
		this.rhpfCarreraDtoBuscar = rhpfCarreraDtoBuscar;
	}

	public PeriodoAcademico getRhpfPeriodoAcademico() {
		return rhpfPeriodoAcademico;
	}

	public void setRhpfPeriodoAcademico(PeriodoAcademico rhpfPeriodoAcademico) {
		this.rhpfPeriodoAcademico = rhpfPeriodoAcademico;
	}

	public MallaCurricular getRhpfMallaCurricular() {
		return rhpfMallaCurricular;
	}

	public void setRhpfMallaCurricular(MallaCurricular rhpfMallaCurricular) {
		this.rhpfMallaCurricular = rhpfMallaCurricular;
	}

	public PersonaDto getRhpfPersonaDtoEditar() {
		return rhpfPersonaDtoEditar;
	}

	public void setRhpfPersonaDtoEditar(PersonaDto rhpfPersonaDtoEditar) {
		this.rhpfPersonaDtoEditar = rhpfPersonaDtoEditar;
	}

	public List<MateriaDto> getRhpfListMateriaDto() {
		rhpfListMateriaDto = rhpfListMateriaDto == null ? (new ArrayList<MateriaDto>()) : rhpfListMateriaDto;
		return rhpfListMateriaDto;
	}

	public void setRhpfListMateriaDto(List<MateriaDto> rhpfListMateriaDto) {
		this.rhpfListMateriaDto = rhpfListMateriaDto;
	}

	public List<ParaleloDto> getRhpfListParaleloDto() {
		rhpfListParaleloDto = rhpfListParaleloDto == null ? (new ArrayList<ParaleloDto>()) : rhpfListParaleloDto;
		return rhpfListParaleloDto;
	}

	public void setRhpfListParaleloDto(List<ParaleloDto> rhpfListParaleloDto) {
		this.rhpfListParaleloDto = rhpfListParaleloDto;
	}

	public TipoSistemaCalificacion getRhpfTipoSistemaCalificacion() {
		return rhpfTipoSistemaCalificacion;
	}

	public void setRhpfTipoSistemaCalificacion(TipoSistemaCalificacion rhpfTipoSistemaCalificacion) {
		this.rhpfTipoSistemaCalificacion = rhpfTipoSistemaCalificacion;
	}

	public SistemaCalificacion getRhpfSistemaCalificacion() {
		return rhpfSistemaCalificacion;
	}

	public void setRhpfSistemaCalificacion(SistemaCalificacion rhpfSistemaCalificacion) {
		this.rhpfSistemaCalificacion = rhpfSistemaCalificacion;
	}

	public PlanificacionCronograma getRhpfPlanificacionCronograma() {
		return rhpfPlanificacionCronograma;
	}

	public void setRhpfPlanificacionCronograma(PlanificacionCronograma rhpfPlanificacionCronograma) {
		this.rhpfPlanificacionCronograma = rhpfPlanificacionCronograma;
	}

//	public PeriodoAcademico getRhpfPeriodoAcademicoActivo() {
//		return rhpfPeriodoAcademicoActivo;
//	}
//
//	public void setRhpfPeriodoAcademicoActivo(PeriodoAcademico rhpfPeriodoAcademicoActivo) {
//		this.rhpfPeriodoAcademicoActivo = rhpfPeriodoAcademicoActivo;
//	}

	public Integer getRhpfValidadorClic() {
		return rhpfValidadorClic;
	}

	public void setRhpfValidadorClic(Integer rhpfValidadorClic) {
		this.rhpfValidadorClic = rhpfValidadorClic;
	}

	public Integer getRhpfBloqueoModal() {
		return rhpfBloqueoModal;
	}

	public void setRhpfBloqueoModal(Integer rhpfBloqueoModal) {
		this.rhpfBloqueoModal = rhpfBloqueoModal;
	}
	

	public ParaleloDto getRhpfParaleloHomologacion() {
		return rhpfParaleloHomologacion;
	}

	public void setRhpfParaleloHomologacion(ParaleloDto rhpfParaleloHomologacion) {
		this.rhpfParaleloHomologacion = rhpfParaleloHomologacion;
	}

	public List<MateriaDto> getRhpfListHomologadosDto() {
		rhpfListHomologadosDto = rhpfListHomologadosDto == null ? (new ArrayList<MateriaDto>()) : rhpfListHomologadosDto;

		return rhpfListHomologadosDto;
	}

	public void setRhpfListHomologadosDto(List<MateriaDto> rhpfListHomologadosDto) {
		this.rhpfListHomologadosDto = rhpfListHomologadosDto;
	}

	public String getRhpfNombreArchivoAuxiliar() {
		return rhpfNombreArchivoAuxiliar;
	}

	public void setRhpfNombreArchivoAuxiliar(String rhpfNombreArchivoAuxiliar) {
		this.rhpfNombreArchivoAuxiliar = rhpfNombreArchivoAuxiliar;
	}

	public String getRhpfNombreArchivoSubido() {
		return rhpfNombreArchivoSubido;
	}

	public void setRhpfNombreArchivoSubido(String rhpfNombreArchivoSubido) {
		this.rhpfNombreArchivoSubido = rhpfNombreArchivoSubido;
	}

	

	public List<MateriaDto> getRhpfListRecordTotal() {
		rhpfListRecordTotal = rhpfListRecordTotal == null ? (new ArrayList<MateriaDto>()) : rhpfListRecordTotal;
		return rhpfListRecordTotal;
	}

	public void setRhpfListRecordTotal(List<MateriaDto> rhpfListRecordTotal) {
		this.rhpfListRecordTotal = rhpfListRecordTotal;
	}

	

	public List<InstitucionAcademicaDto> getRhpfListUniversidadesDto() {
		rhpfListUniversidadesDto = rhpfListUniversidadesDto == null ? (new ArrayList<InstitucionAcademicaDto>()) : rhpfListUniversidadesDto;
		return rhpfListUniversidadesDto;
	}

	public void setRhpfListUniversidadesDto(List<InstitucionAcademicaDto> rhpfListUniversidadesDto) {
		this.rhpfListUniversidadesDto = rhpfListUniversidadesDto;
	}

	public List<TituloDto> getRhpfListTitulosUniversidadDto() {
		rhpfListTitulosUniversidadDto = rhpfListTitulosUniversidadDto == null ? (new ArrayList<TituloDto>()) : rhpfListTitulosUniversidadDto;
		return rhpfListTitulosUniversidadDto;
	}

	public void setRhpfListTitulosUniversidadDto(List<TituloDto> rhpfListTitulosUniversidadDto) {
		this.rhpfListTitulosUniversidadDto = rhpfListTitulosUniversidadDto;
	}

	public List<MateriaDto> getRhpfListRecordCarreraAnterior() {
		rhpfListRecordCarreraAnterior = rhpfListRecordCarreraAnterior == null ? (new ArrayList<MateriaDto>()) : rhpfListRecordCarreraAnterior;
		return rhpfListRecordCarreraAnterior;
	}

	public void setRhpfListRecordCarreraAnterior(List<MateriaDto> rhpfListRecordCarreraAnterior) {
		this.rhpfListRecordCarreraAnterior = rhpfListRecordCarreraAnterior;
	}

	public  FichaEstudiante getRhpfFichaEstudiante() {
		return rhpfFichaEstudiante;
	}

	public  void setRhpfFichaEstudiante(FichaEstudiante rhpfFichaEstudiante) {
		this.rhpfFichaEstudiante = rhpfFichaEstudiante;
	}

	public List<PeriodoAcademico> getRhpfListaPracActivoPosgrado() {
		rhpfListaPracActivoPosgrado = rhpfListaPracActivoPosgrado == null ? (new ArrayList<PeriodoAcademico>()) : rhpfListaPracActivoPosgrado;
		return rhpfListaPracActivoPosgrado;
	}

	public  void setRhpfListaPracActivoPosgrado(List<PeriodoAcademico> rhpfListaPracActivoPosgrado) {
		this.rhpfListaPracActivoPosgrado = rhpfListaPracActivoPosgrado;
	}

	public  List<PeriodoAcademico> getRhpfListaPeriodosPorCarreraPosGrado() {
		rhpfListaPeriodosPorCarreraPosGrado = rhpfListaPeriodosPorCarreraPosGrado == null ? (new ArrayList<PeriodoAcademico>()) : rhpfListaPeriodosPorCarreraPosGrado;
		return rhpfListaPeriodosPorCarreraPosGrado;
	}

	public void setRhpfListaPeriodosPorCarreraPosGrado(
			List<PeriodoAcademico> rhpfListaPeriodosPorCarreraPosGrado) {
		this.rhpfListaPeriodosPorCarreraPosGrado = rhpfListaPeriodosPorCarreraPosGrado;
	}




	public  boolean isRhpfBloqueaEstadoIngreso() {
		return rhpfBloqueaEstadoIngreso;
	}

	public   void setRhpfBloqueaEstadoIngreso(boolean rhpfBloqueaEstadoIngreso) {
		this.rhpfBloqueaEstadoIngreso = rhpfBloqueaEstadoIngreso;
	}

	public  boolean isRhpfBloqueaBotonFinalizar() {
		return rhpfBloqueaBotonFinalizar;
	}

	public  void setRhpfBloqueaBotonFinalizar(boolean rhpfBloqueaBotonFinalizar) {
		this.rhpfBloqueaBotonFinalizar = rhpfBloqueaBotonFinalizar;
	}

	public  boolean isRhpfBloqueaHomologacion() {
		return rhpfBloqueaHomologacion;
	}

	public  void setRhpfBloqueaHomologacion(boolean rhpfBloqueaHomologacion) {
		this.rhpfBloqueaHomologacion = rhpfBloqueaHomologacion;
	}

	
	public  boolean isRhpfBloqueaPeriodoPosgrado() {
		return rhpfBloqueaPeriodoPosgrado;
	}

	public  void setRhpfBloqueaPeriodoPosgrado(boolean rhpfBloqueaPeriodoPosgrado) {
		this.rhpfBloqueaPeriodoPosgrado = rhpfBloqueaPeriodoPosgrado;
	}
	

	public UsuarioRol getRhpfUsuarioRol() {
		return rhpfUsuarioRol;
	}

	public void setRhpfUsuarioRol(UsuarioRol rhpfUsuarioRol) {
		this.rhpfUsuarioRol = rhpfUsuarioRol;
	}

	public List<Rol> getRhpfListaRolesUsuario() {
		rhpfListaRolesUsuario = rhpfListaRolesUsuario == null ? (new ArrayList<Rol>()) : rhpfListaRolesUsuario;
		return rhpfListaRolesUsuario;
	}

	public void setRhpfListaRolesUsuario(List<Rol> rhpfListaRolesUsuario) {
		this.rhpfListaRolesUsuario = rhpfListaRolesUsuario;
	}

	public Rol getRhpfRol() {
		return rhpfRol;
	}

	public void setRhpfRol(Rol rhpfRol) {
		this.rhpfRol = rhpfRol;
	}

	public boolean isRhpfCrearMatriculaPosgrado() {
		return rhpfCrearMatriculaPosgrado;
	}

	public void setRhpfCrearMatriculaPosgrado(boolean rhpfCrearMatriculaPosgrado) {
		this.rhpfCrearMatriculaPosgrado = rhpfCrearMatriculaPosgrado;
	}

	public Integer getRhpfNivelActualPosgrado() {
		return rhpfNivelActualPosgrado;
	}

	public void setRhpfNivelActualPosgrado(Integer rhpfNivelActualPosgrado) {
		this.rhpfNivelActualPosgrado = rhpfNivelActualPosgrado;
	}

	public List<MateriaDto> getRhpfListMateriaNivelPosgrado() {
		rhpfListMateriaNivelPosgrado = rhpfListMateriaNivelPosgrado == null ? (new ArrayList<MateriaDto>()) : rhpfListMateriaNivelPosgrado;
		return rhpfListMateriaNivelPosgrado;
	}

	public void setRhpfListMateriaNivelPosgrado(List<MateriaDto> rhpfListMateriaNivelPosgrado) {
		this.rhpfListMateriaNivelPosgrado = rhpfListMateriaNivelPosgrado;
	}

	public PeriodoAcademico getRhpfPeriodoAcademicoPosgrado() {
		return rhpfPeriodoAcademicoPosgrado;
	}

	public void setRhpfPeriodoAcademicoPosgrado(PeriodoAcademico rhpfPeriodoAcademicoPosgrado) {
		this.rhpfPeriodoAcademicoPosgrado = rhpfPeriodoAcademicoPosgrado;
	}

	public PlanificacionCronograma getRhpfPlanificacionCronogramaPosgrado() {
		return rhpfPlanificacionCronogramaPosgrado;
	}

	public void setRhpfPlanificacionCronogramaPosgrado(PlanificacionCronograma rhpfPlanificacionCronogramaPosgrado) {
		this.rhpfPlanificacionCronogramaPosgrado = rhpfPlanificacionCronogramaPosgrado;
	}

	public List<ParaleloDto> getRhpfListaParaleloNuevaMatrPosgrado() {
		rhpfListaParaleloNuevaMatrPosgrado = rhpfListaParaleloNuevaMatrPosgrado == null ? (new ArrayList<ParaleloDto>()) : rhpfListaParaleloNuevaMatrPosgrado;
		return rhpfListaParaleloNuevaMatrPosgrado;
	}

	public void setRhpfListaParaleloNuevaMatrPosgrado(List<ParaleloDto> rhpfListaParaleloNuevaMatrPosgrado) {
		this.rhpfListaParaleloNuevaMatrPosgrado = rhpfListaParaleloNuevaMatrPosgrado;
	}

	
	public boolean isRhpfBloqueaSubirArchivo() {
		return rhpfBloqueaSubirArchivo;
	}

	public void setRhpfBloqueaSubirArchivo(boolean rhpfBloqueaSubirArchivo) {
		this.rhpfBloqueaSubirArchivo = rhpfBloqueaSubirArchivo;
	}

	public Integer getRhpfActivadorReporte() {
		return rhpfActivadorReporte;
	}

	public void setRhpfActivadorReporte(Integer rhpfActivadorReporte) {
		this.rhpfActivadorReporte = rhpfActivadorReporte;
	}

	
}
