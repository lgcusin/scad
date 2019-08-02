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
   
 ARCHIVO:     AdministracionParaleloForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de  Paralelo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
05-04-2017			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) AdministracionParaleloForm.
 * Managed Bean que maneja las peticiones para la administración de la tabla Paralelo.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name="administracionParaleloForm")
@SessionScoped
public class AdministracionParaleloForm implements Serializable{

	private static final long serialVersionUID = -397102404723196895L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	
		//OBJETOS
		private Usuario apfUsuario;
		private PeriodoAcademico apfPeriodoAcademico;
		private CarreraDto apfCarreraDto ;
		private CarreraDto apfCarreraDtoNivelacion ;
		private ParaleloDto apfParaleloDto;
		private NivelDto apfNivelDto;
		private String apfTipoCarrera;
		private Integer apfTipoUsuario;
		//LISTA DE OBJETOS
		
		private List<PeriodoAcademico> apfListPeriodoAcademico;
		private List<CarreraDto> apfListCarreraDto;
		private List<CarreraDto> apfListCarreraDtoNivelacion;
		private List<ParaleloDto> apfListParaleloDto;
		private List<Nivel> apfListNivel;
		private List<MallaCurricularDto> apfListMallaCurricularDto;
//		private List<MallaCurricularDto> apfListMallaCurricularDtoModulos;
		private List<MallaCurricularParaleloDto> apfListMallaCurricularParaleloDto;
		private List<MallaCurricularDto> apfListMallaCurricularDtoAgregar;
		private MallaCurricularParaleloDto apfParaleloEditarCupo;
		private MallaCurricularParaleloDto apfParaleloEditarCupoReserva;
		private Integer apfNuevoCupo;
		private List<UsuarioRolJdbcDto> apfListRoles;
		
	     //AUXILIARES		
		   
		private Integer apfValidadorClick;
		private Integer apfValidadorEdicion;
		private boolean apfHabilitadorNivel;
		private boolean apfHabilitadorArea;
		private boolean apfHabilitadorSeleccion;
		private boolean apfHabilitadorGuardar;
		private boolean apfHabilitadorGuardarMaterias;
		private Integer apfSeleccionarTodos;
		private String apfTituloModal;
		private String apfMensajeModal;
		private String apfMensajeActivacion;
		private Integer apfOpcionSeleccionada;
		private boolean apfHabilitarPeriodo;
		
	   // OBJETOS PARA EDITAR Y CREAR NUEVO
		private ParaleloDto apfParaleloDtoEditar;
		private CarreraDto apfCarreraDtoEditar;
		private PeriodoAcademico apfPeriodoAcademicoEditar;
		private Carrera apfCarreraEditar;
		private Paralelo apfParalelo;
		private String sufijoNivelacion;
		
		
		private List<ModalidadDto> apfListModalidadDto;
		
		//  SE COMENTA: NO SE NECESITA ELIMINAR PARALELO PUES SE DEBE DESHABILITAR
           //	private ParaleloDto apfParaleloEliminar;
           //	private boolean apfAlertaParaleloEliminar;
		
	
	
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
		
	@PostConstruct
	public void inicializar(){
		
	}
	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
		
	@EJB private ParaleloDtoServicioJdbc servApfParaleloDto;
	@EJB private ParaleloServicio servApfParalelo;
	@EJB private PeriodoAcademicoServicio servApfPeriodoAcademico;
	@EJB private CarreraDtoServicioJdbc servApfCarreraDto;
	@EJB private CarreraServicio servApfCarrera;
	@EJB private NivelServicio servNivelDtoServicio;
	@EJB private ParaleloDtoServicioJdbc servParaleloDtoServicioJdbc;
	@EJB private MallaMateriaDtoServicioJdbc servMallaMateriaDtoServicioJdbc;
	@EJB private MallaCurricularParaleloDtoServicioJdbc servMallaCurricularParaleloDtoServicioJdbc;
	@EJB private MallaCurricularParaleloServicio servMallaCurricularParaleloServicio;
	@EJB private UsuarioRolServicio servApfUsuarioRolServicio;
	@EJB private UsuarioRolDtoServicioJdbc servApfUsuarioRolDtoServicioJdbc;

	private int apfModalidadId;
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	
	/**
	 * Método que permite iniciar la administración del paralelo
	 * @param usuario - el usuario que ingresa 
	 * @return  Navegacion a la pagina xhtml de Administración Paralelo.
	 */
	public String irAdministracionParalelo(Usuario usuario) { 
		
		apfUsuario= usuario;
		
		try {
			
			List<UsuarioRol> usro = servApfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
					apfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					apfTipoCarrera="carreras";
					apfHabilitarPeriodo=true;
					if(usuario.getUsrNick().equals("jjlarar")){
						apfTipoUsuario = RolConstantes.ROL_SECRENIVELACION_VALUE.intValue();
						apfTipoCarrera="areas";
						iniciarParametros();
						apfHabilitadorArea = true;
						return "irAdministracionParaleloNivelacion";
					}
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					apfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					apfTipoCarrera="programas";
					apfHabilitarPeriodo=false;
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
					apfTipoUsuario = RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue();
					apfTipoCarrera="programas";
					apfHabilitarPeriodo=false;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					apfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
					apfHabilitarPeriodo=true;
					apfListRoles = servApfUsuarioRolDtoServicioJdbc.buscarRolesUsuarioTodosActivos(apfUsuario.getUsrIdentificacion());
					
					for (UsuarioRolJdbcDto itemRolDep : apfListRoles) {
						//idiomas
						if(itemRolDep.getFclId() ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE){
							apfTipoCarrera=DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL;
						}
						//cultura fisica
						if(itemRolDep.getFclId() ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE){
							apfTipoCarrera=DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL;
						}
						//informatica
						if(itemRolDep.getFclId() ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE){
							apfTipoCarrera=DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL;
						}
					}
					
				}
			}
		} catch (UsuarioRolJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		iniciarParametros();
		
		return "irAdministracionParalelo";
	}
	
	/**
	 * Método que direcciona el regresar a la página de listar paralelos
	 * @return retorna al listar la página de paralelos
	 */
	public String irListar(){
		String retorno = null;
		llenarParalelos();
		retorno = "irAdministracionParaleloNivelacion";
		return retorno;
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados
	 * @return 
	 */
	public void limpiar()  {
			iniciarParametros();
	}
	
	public String cancelarAgregarMateria(){
//		iniciarParametros();
		return "cancelarAgregarMaterias";
	}
	
	/**
	 * Método para limpiar la lista de paralelos cuando se cambia la carrera, y no se presiona buscar
	 * @return 
	 */
	public String limpiarParalelos(){
		
	apfListParaleloDto = new ArrayList<ParaleloDto>();
		return null;
	}
	
	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametros() {
		sufijoNivelacion=null;
		apfHabilitadorGuardarMaterias=true;
		apfHabilitadorNivel = true;
		apfHabilitadorGuardar = true;
		apfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
		apfHabilitadorSeleccion = true; 
		apfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico

		apfListParaleloDto = new ArrayList<ParaleloDto>(); //Inicio lista de Paralelos Dto


		apfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
		apfListCarreraDtoNivelacion= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
		apfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel
		apfNivelDto = new NivelDto();

		apfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto

		apfCarreraDto= new CarreraDto();    //Instancio el objeto carrera
		apfCarreraDtoNivelacion= new CarreraDto();    //Instancio el objeto carrera
		apfParaleloDto= new ParaleloDto();    //Instancio Objeto paralelo

		apfParaleloDtoEditar= new ParaleloDto(); //Instancio Objeto a editar

		apfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
		apfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

		apfValidadorClick = 0;
		apfValidadorEdicion = 0;

		llenarPeriodos(); //Listar todos los peridos academicos

		llenarCarreras();   //Listar  todos las carreras por el  usuario

		llenarNiveles(); //Listar todos los niveles

		//			llenarParalelos();   //Listar  todos los paralelos por el  usuario 


		apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
		apfListMallaCurricularDtoAgregar = new ArrayList<MallaCurricularDto>();
		//			s = new ArrayList<MallaCurricularDto>();
	}
	
	 /**
	 * Método para llenar la lista de periodos 
	 */
	
	public void llenarPeriodos(){
		apfListPeriodoAcademico= null;
		List<String> periodos = new ArrayList<>();
		periodos.add(String.valueOf(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE));
//		periodos.add(String.valueOf(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE));
		
		if(apfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()
				|| apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
			
			try {
				apfListPeriodoAcademico= servApfPeriodoAcademico.buscarPeriodos(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE,periodos);
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PeriodoAcademicoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else if (apfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
			apfListPeriodoAcademico= servApfPeriodoAcademico.listarTodosPosgradoActivo();			
		}else if(apfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
			//para suficiencia idiomas
			if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL)){
				apfListPeriodoAcademico= servApfPeriodoAcademico.listarTodosActivoXtipoPeriodo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
			}
			//para suficiencia cultura física
			if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL)){
				apfListPeriodoAcademico = servApfPeriodoAcademico.listarTodosActivoXtipoPeriodo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
			}
			//para suficiencia informatica
			if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL)){
				apfListPeriodoAcademico = servApfPeriodoAcademico.listarTodosActivoXtipoPeriodo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE);
			}
		}else{
			apfListPeriodoAcademico = new ArrayList<PeriodoAcademico>();
			try {
				apfListPeriodoAcademico.add(servApfPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE));
			} catch (Exception e) {
			
			}
			try {
				apfListPeriodoAcademico.add(servApfPeriodoAcademico.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE));
			} catch (Exception e) {
			}
		}
	}
	
	 /**
	 * Método para llenar la lista de niveles 
	 */
	
	public void llenarNiveles(){
		apfListNivel= new ArrayList<Nivel>();
		try {
			if(apfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()
					|| apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
				if(apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
					apfListNivel = servNivelDtoServicio.listarNivelacion();
					apfTipoCarrera="areas";
				}else{
					apfListNivel= servNivelDtoServicio.listarTodos();
				}
			}else if (apfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				apfListNivel= servNivelDtoServicio.listarTodosPosgrado();			
			}else if(apfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				//idiomas
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL)){
					apfListNivel = servNivelDtoServicio.listarSuficienciaIdiomas();
				}
				//cultura fisica
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL)){
					apfListNivel = servNivelDtoServicio.listarSuficienciaCulturaFisica();
				}
				//informatica
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL)){
					Nivel nivel = servNivelDtoServicio.listarNivelXNumeral(1);
					List<Nivel> niveles = new ArrayList<>();
					niveles.add(nivel);
					apfListNivel = niveles;
				}
			}else if(apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
				apfListNivel = servNivelDtoServicio.listarNivelacion();
				apfTipoCarrera="areas";
			}
		} catch (NivelNoEncontradoException e) {
		} catch (NivelException e) {
		}
	}
	
	/**
	 * Método para llenar la lista de Carreras por Usuario,  y Periodo activo
	 *
	 */
	public void llenarCarreras(){
		
		apfListCarreraDto= null;
		apfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
		apfListParaleloDto=null;
		
		try {
			 //listo las carreras en el combo, de acuerdo al Usuario y el periodo seleccionado
			
			//	apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXPeriodoAcademico(apfUsuario.getUsrId(), apfPeriodoAcademico.getPracId()); //Metodo alternativo sin importar ROL estado de RolFlujoCarrera
			//creo una lista de Carreras  X Usuario X ROL Activo y el periodo académico activo)
			if(apfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				if(apfTipoCarrera.equals("areas")){
					apfListCarreraDto=servApfCarreraDto.listarTodosRediseno();	
				}else{
					apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrr(apfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);	
				}
				
			}else if (apfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrPosgrado(apfUsuario.getUsrId(), RolConstantes.ROL_SECREPOSGRADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);			
			}else if (apfTipoUsuario == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
				apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrNivelacionRediseno(apfUsuario.getUsrId(), RolConstantes.ROL_USUARIONIVELACION_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);			
			}else if(apfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				//idiomas
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL)){
					apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrXDependencia(apfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE);
				}
				//cultura fisica
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL)){
					apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrXDependencia(apfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE);
				}
				//informatica
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL)){
					apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrXDependencia(apfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE);
				}
			}else if(apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
				apfListCarreraDto=servApfCarreraDto.listarTodosRediseno();	
			}
			
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.llenar.carreras.por.id.Periodo.no.encontrado.exception")));
			iniciarParametros();	
		}
	}
	
	
	/**
	 * Método para llenar la lista de Carreras por Usuario,  y Periodo activo
	 *
	 */
	
	
	public void llenarAreas(){
		
		apfListParaleloDto=null;
		
		try {
				apfListCarreraDtoNivelacion=servApfCarreraDto.listarAreasXCrrId(apfCarreraDto.getCrrId());
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.llenar.carreras.por.id.Periodo.no.encontrado.exception")));
			iniciarParametros();	
		}
	}
	
	/**
	 * Método para llenar la Lista de paralelos por la Carrera y el Periodo 
	 * 
	 */

	public void llenarParalelos(){
		apfListParaleloDto= null;
	try {
				//realiza la busquedad de los paralelos por carrera y periodo
//			apfListParaleloDto= servApfParaleloDto.listarXusuarioXcarreraXperiodo(apfUsuario.getUsrId(),apfCarreraDto.getCrrId(), apfPeriodoAcademico.getPracId());
		if(apfTipoCarrera.equals("areas")){
			if(apfPeriodoAcademico.getPracId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor seleccione el período académico.");
			}else if(apfCarreraDto.getCrrId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor seleccione la carrera.");
			}else if (apfCarreraDtoNivelacion.getCrrId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor seleccione el área de nivelación.");
			}else if(apfNivelDto.getNvlId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor seleccione el nivel.");
			}else{
				apfListParaleloDto= servApfParaleloDto.listarXusuarioXcarreraXperiodoXNivelXArea(apfUsuario.getUsrId(),apfCarreraDtoNivelacion.getCrrId(), apfPeriodoAcademico.getPracId(),apfNivelDto.getNvlId(),apfCarreraDto.getCrrId());
				try {
					apfCarreraDto=servApfCarreraDto.buscarXId(apfCarreraDto.getCrrId());
				} catch (CarreraDtoJdbcException | CarreraDtoJdbcNoEncontradoException e) {
				}				
			}

		}else{
			apfListParaleloDto= servApfParaleloDto.listarXusuarioXcarreraXperiodoXNivel(apfUsuario.getUsrId(),apfCarreraDto.getCrrId(), apfPeriodoAcademico.getPracId(),apfNivelDto.getNvlId());
		}
		} catch (ParaleloDtoException e) {
					
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
			
		}catch (ParaleloDtoNoEncontradoException e) {
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();		
				
    	}
	}
		

	
	/**
	 * Método que permite ver  o editar un paralelo
	 * @return navegación al xhtml ver Paralelo o editar Paralelo
	 * @param  El objeto paralelo y el tipo de transacción (ver o editar)
	 */
	
	
	public String verDatosParalelo(ParaleloDto paralelo, int transaccion){
	
		apfParaleloDto= null;
		apfParaleloDto= paralelo;
		if(apfParaleloDto.getPrlEstado()==ParaleloConstantes.ESTADO_INACTIVO_VALUE){
			apfMensajeActivacion = "Activar paralelo";
		}else{
			apfMensajeActivacion = "Desactivar paralelo";
		}
	     String retorno= null;
	     try {
	    	 apfListMallaCurricularParaleloDto = new ArrayList<MallaCurricularParaleloDto>();
	    	 apfListMallaCurricularParaleloDto = servMallaCurricularParaleloDtoServicioJdbc.listarMallasXParaleloXMateria(apfParaleloDto.getPrlId());
//	    	 Iterator<MallaCurricularDto> it = apfListMallaCurricularDto.iterator();
//				while(it.hasNext()) {
//			        MallaCurricularDto element = it.next();
//			        if(element.getMtrSubId()!=GeneralesConstantes.APP_ID_BASE){
//			       	 it.remove();
//			        }
//			    }
	    	 
		} catch (Exception e) {
		}
	      
	   
		if(transaccion==0){ // Pregunta si quiere ver o editar el paralelo de acuerdo a la transacción,   0: ver el paralelo
		
			retorno = "irVerDatosParalelo";
		}
		
		else if (transaccion==1){  //  1: editar el paralelo
			
			apfPeriodoAcademico = new PeriodoAcademico(); //Instancio el  periodo académico
			try {
				apfPeriodoAcademico= servApfPeriodoAcademico.buscarPorId(apfParaleloDto.getPracId()); //busco el periodo académico por Id
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (PeriodoAcademicoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}  
			
			if (apfPeriodoAcademico.getPracEstado()==PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE){	//verifico que el periodo académico del paralelo seleccionado esta activo
			
			retorno= "irEditarParalelo";   // solo se puede editar un paralelo del período académico activo.
			
			}else // si el periodo del paralelo esta inactivo, presento mensaje de error
	    	{
	    		if(apfTipoUsuario==RolConstantes.ROL_SECRENIVELACION_VALUE){
	    			if (apfPeriodoAcademico.getPracEstado()==PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE){
	    				retorno= "irEditarParalelo";   // solo se puede editar un paralelo del período académico activo.			
	    			}else{
	    				FacesUtil.limpiarMensaje();
	    	    		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.editar.paralelo.validacion.periodo.inactivo")));	
	    	    	    iniciarParametros();		
	    			}
	    		}	else{
	    			FacesUtil.limpiarMensaje();
		    		FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.editar.paralelo.validacion.periodo.inactivo")));	
		    	    iniciarParametros();	
	    		}
	    		
	    	
	    	retorno="irAdministracionParalelo";
	    	
	    	}
		}
		return retorno;
	}
	
	

	
	/**
	 * Método para editar 
	 * @return navegación al xhtml listarParalelos
	 */
	public String editarEliminar(){
		apfParaleloDtoEditar= new ParaleloDto();
		apfParaleloDtoEditar = apfParaleloDto;
		if(apfOpcionSeleccionada==1){
			
				    try {
				    	
						servApfParalelo.editar(apfParaleloDtoEditar);// Se envía el paralelo a editar y esperamos se realice o no el cambio
	                //  Igualmente se puede enviar los paramteros a editar	                   
	                //	if(servApfParalelo.editarXParametros(apfParaleloDtoEditar.getPrlId(), apfParaleloDtoEditar.getPrlCupo(), apfParaleloDtoEditar.getPrlDescripcion(), apfParaleloDtoEditar.getPrlCodigo(), apfParaleloDtoEditar.getPrlEstado()))
						
						FacesUtil.limpiarMensaje();			
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.editar.paralelo.validacion.exitoso")));
						iniciarParametros();
					    } catch (ParaleloValidacionException e) {
						
					    FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(e.getMessage());	
						iniciarParametros();
					   
					    }  catch (ParaleloException e) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(e.getMessage());
							iniciarParametros();
					    } 
					
		}else{
			boolean op = false;
			for (MallaCurricularParaleloDto item : apfListMallaCurricularParaleloDto) {
				if(item.getMlcrprInscritos()!=0){
					op=true;	
				}
			}
			if(!op){
				try {
					if(apfParaleloDto.getPrlEstado()==ParaleloConstantes.ESTADO_ACTIVO_VALUE){
						servApfParalelo.desactivarPprlId(apfParaleloDtoEditar.getPrlId());
						FacesUtil.limpiarMensaje();		
						//TODO mensajes
						FacesUtil.mensajeInfo("El paralelo seleccionado ha sido desactivado.");	
					}else{
						servApfParalelo.activarPprlId(apfParaleloDtoEditar.getPrlId());
						FacesUtil.limpiarMensaje();		
						//TODO mensajes
						FacesUtil.mensajeInfo("El paralelo seleccionado ha sido activado.");
					}
					
				} catch (ParaleloNoEncontradoException | ParaleloException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				}
			}else{
				FacesUtil.limpiarMensaje();			
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.desactivar.paralelo.exception.inscritos.mayor.cero")));
				verificarClickNo();
				return null;
			}
		}
		iniciarParametros();
		verificarClickNo();
		return "irAdministracionParalelo";
	}
	
	
	/**
	 * Método para ir a generar el  nuevo paralelo
	 * @return navagación al xhtml nuevo
	 */
	public String irNuevoParalelo(){
		
		apfListCarreraDto= new ArrayList<CarreraDto>() ;
		llenarCarreras();
		apfPeriodoAcademico= new PeriodoAcademico();
		apfCarreraDto = new CarreraDto();
		apfNivelDto.setNvlId(GeneralesConstantes.APP_ID_BASE);
		apfParaleloDto= new ParaleloDto(); //instancio el objeto Paralelo para la creación del mismo
		apfHabilitadorNivel=true;
		try {
			if(apfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()
					||apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
				apfPeriodoAcademico= servApfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);//busco el período academico activo, pues solo en el activo puede añadirse nuevos paralelos
			}else if (apfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				apfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);			
			}else if(apfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				//idiomas
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL)){
					apfPeriodoAcademico= servApfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				}
				//cultura fisica
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL)){
					apfPeriodoAcademico= servApfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
				}
				//informatica
				if(apfTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL)){
					apfPeriodoAcademico= servApfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE);
					
					apfListModalidadDto = new ArrayList<>();
					apfListModalidadDto.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE, ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL));
					apfListModalidadDto.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE, ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL));
					apfListModalidadDto.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE, ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL));
					return "irNuevoParaleloInformatica";
				}
			}
		} catch (PeriodoAcademicoNoEncontradoException e) {
		    	 FacesUtil.mensajeError(e.getMessage());
		    } catch (PeriodoAcademicoException e) {
		    	 FacesUtil.mensajeError(e.getMessage());
		    }
		llenarNiveles();
		
//		try {
//			
//			//creo una lista de Carreras  X Usuario X ROL Activo y el periodo académico activo)
//			apfListCarreraDto=servApfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(apfUsuario.getUsrId(), RolConstantes.ROL_BD_DIRCARRERA, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, apfPeriodoAcademico.getPracId());
//		
//		} catch (CarreraDtoJdbcException e) {
//			e.printStackTrace();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());	
//			iniciarParametros();
//
//		} catch (CarreraDtoJdbcNoEncontradoException e) {
//			e.printStackTrace();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());	
//			iniciarParametros();
//			
//
//		}
			
		return "irNuevoParalelo";  // Llamo a la pagina xhtml para cargar los datos conseguidos en este método.
	}
	
	/**
	 * Método para ir a generar el  nuevo paralelo
	 * @return navagación al xhtml nuevo
	 */
	public String irNuevoParaleloNivelacion(){
		
		apfListCarreraDtoNivelacion= new ArrayList<CarreraDto>() ;
		apfPeriodoAcademico= new PeriodoAcademico();
		apfCarreraDto = new CarreraDto();
		apfNivelDto.setNvlId(GeneralesConstantes.APP_ID_BASE);
		apfParaleloDto= new ParaleloDto(); //instancio el objeto Paralelo para la creación del mismo
		apfHabilitadorNivel=true;
		llenarCarreras();
		try {
			if(apfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()){
				apfPeriodoAcademico= servApfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);//busco el período academico activo, pues solo en el activo puede añadirse nuevos paralelos
			}else if (apfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				apfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);			
			}
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			e.printStackTrace();
		    	 FacesUtil.mensajeError(e.getMessage());
		    } catch (PeriodoAcademicoException e) {
		    	 FacesUtil.mensajeError(e.getMessage());
		    }
		apfCarreraDtoNivelacion= new CarreraDto();	
		return "irNuevoParaleloNivelacion";  // Llamo a la pagina xhtml para cargar los datos conseguidos en este método.
	}
	
	/**
	 * Método para  grabar un nuevo paralelo
	 * @return navagación al xhtml AdministracionParalelo
	 */
	
	public String crearParalelo(){
		apfPeriodoAcademicoEditar=new PeriodoAcademico();
		apfCarreraEditar = new Carrera();
	   try	{	
		   apfParaleloDto.setPracId(apfPeriodoAcademico.getPracId()); //seteo el Id del Periodo Académico en el ParaleloDto,
		   
		   
		   servApfParalelo.anadirParaleloMlCrPr(apfParaleloDto, apfListMallaCurricularDto);  //llamo al método añadir paralelo y envío como parametros los objetos (ParaleloDto, ListAMallaCurricular)
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.crear.nuevo.paralelo.exitoso")));
		   iniciarParametros();
	   } catch (ParaleloValidacionException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(e.getMessage());
		   iniciarParametros();
	   } catch (ParaleloException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(e.getMessage());
		   iniciarParametros();
	   }
	   return "irAdministracionParalelo";
	}
	
	/**
	 * Método para  grabar un nuevo paralelo
	 * @return navagación al xhtml AdministracionParalelo
	 */
	
	public String crearParaleloNivelacion(){
		apfPeriodoAcademicoEditar=new PeriodoAcademico();
		apfCarreraEditar = new Carrera();
	   try	{	
		   apfParaleloDto.setPrlCodigo(apfParaleloDto.getPrlCodigo()+sufijoNivelacion.toUpperCase());
		   apfParaleloDto.setPrlDescripcion(apfParaleloDto.getPrlCodigo());
		   apfParaleloDto.setPracId(apfPeriodoAcademico.getPracId()); //seteo el Id del Periodo Académico en el ParaleloDto,
		   
		   servApfParalelo.anadirParaleloMlCrPrNivelacion(apfParaleloDto, apfListMallaCurricularDto);  //llamo al método añadir paralelo y envío como parametros los objetos (ParaleloDto, ListAMallaCurricular)
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.crear.nuevo.paralelo.exitoso")));
		   iniciarParametros();
	   } catch (ParaleloValidacionException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(e.getMessage());
		   iniciarParametros();
	   } catch (ParaleloException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(e.getMessage());
		   iniciarParametros();
	   }
	   return "irAdministracionParalelo";
	}
	
	/**
	 * Método para  grabar un mallas materia paralelo
	 * @return navagación al xhtml AdministracionParalelo
	 */
	
	public String agregarMateriasParalelo(){
	   try	{	
		   servApfParalelo.anadirMallasAParalelo(apfParaleloDto, apfListMallaCurricularDtoAgregar);  
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeInfo("Las materias seleccionadas fueron agregadas correctamente.");
		   iniciarParametros();
	   } catch (ParaleloValidacionException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(e.getMessage());
		   iniciarParametros();
	   } catch (ParaleloException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(e.getMessage());
		   iniciarParametros();
	   }
	   return "cancelarAgregarMaterias";
	}
	
	public void cambiarCarrera(){
		List<ParaleloDto> listaParalelos = new ArrayList<ParaleloDto>();
		
		try {
			
			apfHabilitadorGuardar=true;
			StringBuilder sb = new StringBuilder();
			
			if(apfParaleloDto.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
				Carrera crrAux = servApfCarrera.buscarPorId(apfParaleloDto.getCrrId());
				String[] arregloNombre = crrAux.getCrrDescripcion().split(" ");
				
				for (int j = 0; j < arregloNombre.length; j++) {
					if(arregloNombre[j].length()>3){
							sb.append(arregloNombre[j].substring(0, 1));
					}
				}
				apfHabilitadorNivel=false;
			}else{
				apfParaleloDto.setPrlCodigo(null);
				apfParaleloDto.setPrlDescripcion(null);
				apfHabilitadorNivel=true;
				apfHabilitadorSeleccion = true; 
				apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
			}
			
			if(apfNivelDto.getNvlId()==GeneralesConstantes.APP_ID_BASE){
				apfParaleloDto.setPrlCodigo(null);
				apfParaleloDto.setPrlDescripcion(null);
				apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
			}else{
				sb.append(apfNivelDto.getNvlId());
				sb.append("-");
				try {
//					listaParalelos = servParaleloDtoServicioJdbc.listarXNivelCarreraXperiodo(apfParaleloDto.getCrrId(),apfPeriodoAcademico.getPracId(), apfNivelDto.getNvlId());
					listaParalelos = servParaleloDtoServicioJdbc.listarXNivelCarreraXperiodoNumMaxParalelo(apfParaleloDto.getCrrId(),apfPeriodoAcademico.getPracId(), apfNivelDto.getNvlId());
					if(listaParalelos.size()==0){
						sb.append(1);
					}else{
//						String codigo = listaParalelos.get(listaParalelos.size()-1).getPrlCodigo();
////						sb.append(codigo.substring(0,codigo.length()-1));
//						sb.append(Integer.valueOf(codigo.substring(codigo.length()-1))+1);
//						if(Integer.parseInt(sb.toString().substring(sb.toString().length() - 2))>=10){
//							listaParalelos = servParaleloDtoServicioJdbc.listarMayorParaleloXNivelCarreraXperiodo(apfParaleloDto.getCrrId(),apfPeriodoAcademico.getPracId(), apfNivelDto.getNvlId());
//							sb = new StringBuilder();
//							Integer codigoAux=Integer.parseInt(listaParalelos.get(listaParalelos.size()-1).getPrlCodigo().substring(sb.toString().length() - 2));

						sb.append(listaParalelos.size()+1);
//						}
					}
				} catch (ParaleloDtoException | ParaleloDtoNoEncontradoException e) {
					sb.append(1);
				}
				
				apfParaleloDto.setPrlCodigo(sb.toString());
				apfParaleloDto.setPrlDescripcion(sb.toString());
				
				try {
					apfListMallaCurricularDto= servMallaMateriaDtoServicioJdbc.listarMallasMateriasXNivelXCarrera(apfParaleloDto.getCrrId(), apfNivelDto.getNvlId());
//					Iterator<MallaCurricularDto> it = apfListMallaCurricularDto.iterator();
//					while(it.hasNext()) {
//				        MallaCurricularDto element = it.next();
//				        if(element.getMtrSubId()!=GeneralesConstantes.APP_ID_BASE){
//				        	apfListMallaCurricularDtoModulos.add(element);
//				       	 	it.remove();
//				        }
//				    }
					apfHabilitadorGuardar = false;
					apfHabilitadorSeleccion = false; 
					apfHabilitadorGuardar=false;
				} catch (MallaCurricularDtoException | MallaCurricularDtoNoEncontradoException e) {
					apfParaleloDto.setPrlCodigo(null);
					apfParaleloDto.setPrlDescripcion(null);
					apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
					apfHabilitadorGuardar = true;
					apfHabilitadorSeleccion = true; 
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.buscar.no.result.exception")));
				}
			}
		} catch (CarreraNoEncontradoException | CarreraException e) {
			apfHabilitadorNivel=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.no.result.exception")));
		}
		
	}
	
	
	
	public void cambiarNivel(){
			if(apfParaleloDto.getNvlId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.llenar.paralelos.id.carrera.id.nivel.validacion.exception")));
				
			}
	}
	
	
	public void cambiarCarreraNivelacion(){
		apfCarreraDtoNivelacion = new CarreraDto();
		try {
			apfCarreraDto= servApfCarreraDto.buscarXId(apfParaleloDto.getCrrId());
		} catch (CarreraDtoJdbcException | CarreraDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			
		}
		llenarAreas();
		apfHabilitadorArea= false;
		
	}
	
	public void cambiarAreaNivelacion(){
		apfHabilitadorNivel=false;	
	}
	
	public void cambiarNivelNivelacion(){
		try {
			apfHabilitadorGuardar=true;
			StringBuilder sb = new StringBuilder();
			if(apfParaleloDto.getMlcrprNivelacionCrrId()!=GeneralesConstantes.APP_ID_BASE){
				Carrera crrAux = servApfCarrera.buscarPorId(apfParaleloDto.getMlcrprNivelacionCrrId());
				String[] arregloNombre = crrAux.getCrrDescripcion().split(" ");
				
				for (int j = 0; j < arregloNombre.length; j++) {
					if(arregloNombre[j].length()>3){
							sb.append(arregloNombre[j].substring(0, 1));
					}
				}
				
			}else{
				apfParaleloDto.setPrlCodigo(null);
				apfParaleloDto.setPrlDescripcion(null);
				apfHabilitadorNivel=true;
				apfHabilitadorSeleccion = true; 
				apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
			}
			if(apfNivelDto.getNvlId()==GeneralesConstantes.APP_ID_BASE){
				apfParaleloDto.setPrlCodigo(null);
				apfParaleloDto.setPrlDescripcion(null);
				apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();

//			
			}else{
				sb.append(apfNivelDto.getNvlId());
				sb.append("-");
				try {
					List<ParaleloDto> listaParalelos = new ArrayList<ParaleloDto>();
					listaParalelos = servParaleloDtoServicioJdbc.listarXNivelCarreraXperiodo(apfParaleloDto.getMlcrprNivelacionCrrId(),apfPeriodoAcademico.getPracId(), apfNivelDto.getNvlId());
					if(listaParalelos.size()==0){
						sb.append(1);
					}else{
//						String codigo = listaParalelos.get(listaParalelos.size()-1).getPrlCodigo();
////						sb.append(codigo.substring(0,codigo.length()-1));
//						sb.append(Integer.valueOf(codigo.substring(codigo.length()-1))+1);
//						if(Integer.parseInt(sb.toString().substring(sb.toString().length() - 2))>=10){
//							listaParalelos = servParaleloDtoServicioJdbc.listarMayorParaleloXNivelCarreraXperiodo(apfParaleloDto.getCrrId(),apfPeriodoAcademico.getPracId(), apfNivelDto.getNvlId());
//							sb = new StringBuilder();
//							Integer codigoAux=Integer.parseInt(listaParalelos.get(listaParalelos.size()-1).getPrlCodigo().substring(sb.toString().length() - 2));
							
							sb.append(listaParalelos.size()+1);
//						}
					}
				} catch (ParaleloDtoException | ParaleloDtoNoEncontradoException e) {
					sb.append(1);
				}
				
				apfParaleloDto.setPrlCodigo(sb.toString());
				apfParaleloDto.setPrlDescripcion(sb.toString());
				try {
					apfListMallaCurricularDto= servMallaMateriaDtoServicioJdbc.listarMallasMateriasXNivelXCarrera(apfParaleloDto.getMlcrprNivelacionCrrId(), apfNivelDto.getNvlId());
//					Iterator<MallaCurricularDto> it = apfListMallaCurricularDto.iterator();
//					while(it.hasNext()) {
//				        MallaCurricularDto element = it.next();
//				        if(element.getMtrSubId()!=GeneralesConstantes.APP_ID_BASE){
//				        	apfListMallaCurricularDtoModulos.add(element);
//				       	 	it.remove();
//				        }
//				    }
					apfHabilitadorGuardar = false;
					apfHabilitadorSeleccion = false; 
					apfHabilitadorGuardar=false;
				} catch (MallaCurricularDtoException | MallaCurricularDtoNoEncontradoException e) {
					apfParaleloDto.setPrlCodigo(null);
					apfParaleloDto.setPrlDescripcion(null);
					apfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
					apfHabilitadorGuardar = true;
					apfHabilitadorSeleccion = true; 
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.buscar.no.result.exception")));
				}
			}
		} catch (CarreraNoEncontradoException | CarreraException e) {
			apfHabilitadorNivel=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.no.result.exception")));
		}
		
	}
	
	public void buscarSubMaterias(MallaCurricularDto entidad){
		for (MallaCurricularDto item : apfListMallaCurricularDto) {
			if(entidad.getMtrId()==item.getMtrSubId()){
				item.setMlcrprCupo(entidad.getMlcrprCupo());
				item.setMtrAsignada(true);
			}
		}
		
	}
	
	/**
	* verifica que haga click en el boton editar paralelo
	*/
	public void eliminarItem(MallaCurricularParaleloDto item){
		if(item.getMlcrprInscritos()>0){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.eliminar.registro.malla.curricular.paralelo.exception.inscritos.mayor.cero")));
		}else{
			try {
				if(servMallaCurricularParaleloServicio.eliminarPmlcrprId(item.getMlcrprId())){
					apfListMallaCurricularParaleloDto = new ArrayList<MallaCurricularParaleloDto>();
			    	apfListMallaCurricularParaleloDto = servMallaCurricularParaleloDtoServicioJdbc.listarMallasXParaleloXMateria(apfParaleloDto.getPrlId());
			    	FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.eliminar.registro.malla.curricular.paralelo")));
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaCurricularParalelo.buscar.por.id.malla.materia.exception")));
				}
			} catch (Exception e) {
			}
		}
	}
	
	
	/**
	* direcciona a la página de agregar materias
	*/
	public String agregarMaterias(){
		apfHabilitadorGuardarMaterias=true;
		apfListMallaCurricularDtoAgregar = new ArrayList<MallaCurricularDto>();
    	try {
    		List<MallaCurricularDto> auxLista = new ArrayList<MallaCurricularDto>();
    		auxLista = servMallaMateriaDtoServicioJdbc.listarMallasMateriasXNivelXCarrera(apfParaleloDto.getCrrId(), apfParaleloDto.getNvlId());
    		for (MallaCurricularDto item1 : auxLista) {
				boolean op= true;
				for (MallaCurricularParaleloDto item2 : apfListMallaCurricularParaleloDto) {
					if(item2.getMtrDescripcion().equals(item1.getMtrDescripcion())){
						op=false;
					}
				}
				if(op){
					apfListMallaCurricularDtoAgregar.add(item1);
				}
			}
    		if(apfListMallaCurricularDtoAgregar.size()>0){
				apfHabilitadorGuardarMaterias=false;
			}else{
				apfHabilitadorGuardarMaterias=true;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn("No existen materias disponibles para el paralelo seleccionado.");
			}
		} catch ( MallaCurricularDtoException | MallaCurricularDtoNoEncontradoException e) {
		}
		return "irAgregarMaterias";
	}
//	/**
//	 * Método que elimina paralelo
//	 * @return Navegacion a la misma pagina.
//	 */
//	public String eliminarParalelo(){
//		String retorno = null;
//		try{
//			apfAlertaParaleloEliminar = servApfParalelo.buscarPorId(apfParaleloEliminar.getPrlId());
//			if (apfAlertaParalelolEliminar == false){
//				servApfParalelo.eliminarParalelo(apfParaleloEliminar.getPrlId());
//				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.eliminar.paralelo.exitoso")));
//			}else{
//				apfValidadorClick = 0;
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.eliminar.paralelo.ocupado")));
//			}
//			
//		}catch (ParaleloNoEncontradoException e) {
//			apfValidadorClick = 0;
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (ParaleloException e) {
//			apfValidadorClick = 0;
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (ParaleloValidacionException e) {
//			apfValidadorClick = 0;
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (Exception e) {
//			apfValidadorClick = 0;
//			FacesUtil.mensajeError(e.getMessage());
//		}
//		iniciarParametros();
//		return retorno;
//	}
	
	
	    // ********************************************************************/
		// *********************** METODOS AUXILIARES *************************/
		// ********************************************************************/
			
		/**
		* verifica que haga click en el boton nuevo paralelo
		*/
		public String verificarClickNuevoParalelo(){
			boolean op = false;
			for (MallaCurricularDto item : apfListMallaCurricularDto) {
				if(item.isMtrAsignada()){
					op= true;
					break;
				}
			}
			if(op){
				
				boolean op2 = false;
				for (MallaCurricularDto item : apfListMallaCurricularDto) {
					if(item.isMtrAsignada() && !(item.getMlcrprCupo()!=null) ){
						op2= true;
						break;
					}
				}
				if(op2){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.crear.sin.cupo.validacion.exception")));
					apfValidadorClick= 0;
				}else{
					apfValidadorClick= 1;	
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
				apfValidadorClick= 0;
			}
			return null;
		}
		
		/**
		* verifica que haga click en el boton agregar materias
		*/
		public String verificarClickNuevaMateria(){
			boolean op = false;
			for (MallaCurricularDto item : apfListMallaCurricularDtoAgregar) {
				if(item.isMtrAsignada()){
					op= true;
					break;
				}
			}
			if(op){
				
				boolean op2 = false;
				for (MallaCurricularDto item : apfListMallaCurricularDtoAgregar) {
					if(item.isMtrAsignada() && !(item.getMlcrprCupo()!=null)){
						op2= true;
						break;
					}
				}
				if(op2){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.crear.sin.cupo.validacion.exception")));
					apfValidadorClick= 0;
				}else{
					apfValidadorClick= 1;	
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
				apfValidadorClick= 0;
			}
			return null;
		}
		
		/**
		* verifica que haga click en el boton editar paralelo
		*/
		public String verificarClick(Integer opcion){
			if(opcion==1){
				apfValidadorClick= 2;
				apfTituloModal="Editar paralelo";
				apfMensajeModal="¿Seguro que desea guardar la edición del paralelo?";
				apfOpcionSeleccionada=1;
			}else if (opcion == 2){
				apfValidadorClick= 2;
				if(apfParaleloDto.getPrlEstado()==ParaleloConstantes.ESTADO_ACTIVO_VALUE){
					apfTituloModal="Desactivar paralelo";
					apfMensajeModal="¿Seguro que desea desactivar el paralelo y sus materias?";
					apfOpcionSeleccionada=2;
				}else{
					apfTituloModal="Activar paralelo";
					apfMensajeModal="¿Seguro que desea activar el paralelo y sus materias?";
					apfOpcionSeleccionada=2;	
				}
				
			}
		
			return null;
		}
		
		/**
		* verifica que haga click en el boton seleccionar todos
		*/
		public void seleccionarTodos(){
			if(apfSeleccionarTodos!=GeneralesConstantes.APP_ID_BASE){
				for (MallaCurricularDto item : apfListMallaCurricularDto) {
					if(apfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE){
						item.setMtrAsignada(true);
						
					}else if (apfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_NADA_VALUE){
						item.setMtrAsignada(false);
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
					}	
				}	
			}
		}
		
		/**
		* verifica que haga click en el boton seleccionar todos
		*/
		public void seleccionarTodosAgregar(){
			if(apfSeleccionarTodos!=GeneralesConstantes.APP_ID_BASE){
				for (MallaCurricularDto item : apfListMallaCurricularDtoAgregar) {
					if(apfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE){
						item.setMtrAsignada(true);
//						apfHabilitadorGuardarMaterias=false;
						
					}else if (apfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_NADA_VALUE){
						item.setMtrAsignada(false);
//						apfHabilitadorGuardarMaterias=true;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
					}	
				}	
			}
		}
		
		public void actualizarListaMalla(){
			for (MallaCurricularDto item : apfListMallaCurricularDto) {
				if(item.isMtrAsignada()){
				}else {
					item.setMlcrprCupo(null);
				}	
			}
		}
		
		/**
		* verifica que haga click en el boton editar cupo
		*/
		public void editarCupo(MallaCurricularParaleloDto item){
			apfParaleloEditarCupo = item;
			apfNuevoCupo = null;
			apfValidadorEdicion = 1;
		}
		
		/**
		* verifica que haga click en el boton editar cupo reserva
		*/
		public void editarCupoReserva(MallaCurricularParaleloDto item){
			apfParaleloEditarCupoReserva = item;
			apfNuevoCupo = null;
			apfValidadorEdicion = 2;
		}
		
		/**
		* verifica que haga click en el boton editar cupo
		*/
		public void editarCupoNo(){
			apfValidadorEdicion = 0;
		}
//		
		public void guardarCupo(){
			if(apfNuevoCupo!=null){
				if(apfNuevoCupo<apfParaleloEditarCupo.getMlcrprInscritos()){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("El cupo no puede ser menor al número de inscritos del paralelo.");
				}else if(apfNuevoCupo==0){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("El cupo no puede tener el valor de cero.");
				}else{
					try {
						servMallaCurricularParaleloServicio.editarCupoPorMlcrprId(apfParaleloEditarCupo.getMlcrprId(), apfNuevoCupo);
						for (MallaCurricularParaleloDto item : apfListMallaCurricularParaleloDto) {
							String nota1 = Integer.toString(apfParaleloEditarCupo.getMtrId());
							String nota2 = Integer.toString(item.getMtrSubId());
							if (nota1.equals(nota2)) {
								item.setMlcrprCupo(apfNuevoCupo);
								servMallaCurricularParaleloServicio.editarCupoPorMlcrprId(item.getMlcrprId(), apfNuevoCupo);
							}
							nota1 = null;
							nota2 = null;
						}
					} catch (MallaCurricularParaleloNoEncontradoException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("No se encontró la malla curricular del paralelo.");
					} catch (MallaCurricularParaleloException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Error al guardar el cupo en el paralelo, intente más tarde.");
					}
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Se ha modificado correctamente el cupo del paralelo seleccionado.");
					verDatosParalelo(apfParaleloDto, 0);	
				}
				
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
			}
			apfValidadorEdicion = 0;
			
		}
		
		public void guardarCupoReserva(){
			if(apfNuevoCupo!=null){
				if(apfNuevoCupo>apfParaleloEditarCupoReserva.getMlcrprInscritos()){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("El cupo reserva no puede ser mayor al número de inscritos del paralelo.");
				} else if(apfNuevoCupo>apfParaleloEditarCupoReserva.getMlcrprCupo()){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("El cupo reserva no puede ser mayor al número de cupo del paralelo.");
				} else if((apfNuevoCupo.intValue() + apfParaleloEditarCupoReserva.getMlcrprInscritos().intValue()) > apfParaleloEditarCupoReserva.getMlcrprCupo().intValue() ){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("El cupo reserva no puede ser mayor a la suma de inscritos");
				} else if(apfNuevoCupo < 0){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("El cupo reserva no puede ser negativo");
				} else{
					try {
						servMallaCurricularParaleloServicio.editarCupoPorMlcrprIdReserva(apfParaleloEditarCupoReserva.getMlcrprId(), apfNuevoCupo);
						for (MallaCurricularParaleloDto item : apfListMallaCurricularParaleloDto) {
							String nota1 = Integer.toString(apfParaleloEditarCupoReserva.getMtrId());
							String nota2 = Integer.toString(item.getMtrSubId());
							if (nota1.equals(nota2)) {
								item.setMlcrprReservaRepetidos(apfNuevoCupo);
								servMallaCurricularParaleloServicio.editarCupoPorMlcrprIdReserva(item.getMlcrprId(), apfNuevoCupo);
							}
							nota1 = null;
							nota2 = null;
						}
					} catch (MallaCurricularParaleloNoEncontradoException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("No se encontró la malla curricular del paralelo.");
					} catch (MallaCurricularParaleloException e) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Error al guardar el cupo de reserva en el paralelo, intente más tarde.");
					}
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Se ha modificado correctamente el cupo de reserva para repetidos del paralelo seleccionado.");
					verDatosParalelo(apfParaleloDto, 0);	
				}
				
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
			}
			apfValidadorEdicion = 0;
			
		}
		
//		/**
//		 * Redirecciona al modal para eliminar
//		 */
//		public void guardarTemporalActividadEsencial(ActividadEsencial actividadesencial){
//			this.aaefActividadEsencialEliminar = actividadesencial;
//			verificarClickEliminarActividadEsencial(); 
//		}
//		
//		/**
//		 * verifica que haga click en el boton para eliminar 
//		 */
//		public String verificarClickEliminarActividadEsencial(){
//			aaefValidadorClick = 3;
//			return null;
//		}
//		
		/**
		 * setea el verificador del click a 0 
		 */
		public void verificarClickNo(){
			apfValidadorClick  = 0;
		}
//	
	
	
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	
	public Usuario getApfUsuario() {
		return apfUsuario;
	}

	public void setApfUsuario(Usuario apfUsuario) {
		this.apfUsuario = apfUsuario;
	}

	public List<ParaleloDto> getApfListParaleloDto() {
		apfListParaleloDto = apfListParaleloDto==null?(new ArrayList<ParaleloDto>()):apfListParaleloDto;
		return apfListParaleloDto;
	}

	public void setApfListParaleloDto(List<ParaleloDto> apfListParaleloDto) {
		this.apfListParaleloDto = apfListParaleloDto;
	}

	public List<PeriodoAcademico> getApfListPeriodoAcademico() {
		
		apfListPeriodoAcademico = apfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):apfListPeriodoAcademico;
		return apfListPeriodoAcademico;
	}

	public void setApfListPeriodoAcademico(List<PeriodoAcademico> apfListPeriodoAcademico) {
		this.apfListPeriodoAcademico = apfListPeriodoAcademico;
	}

	public PeriodoAcademico getApfPeriodoAcademico() {
		return apfPeriodoAcademico;
	}

	public void setApfPeriodoAcademico(PeriodoAcademico apfPeriodoAcademico) {
		this.apfPeriodoAcademico = apfPeriodoAcademico;
	}

	public CarreraDto getApfCarreraDto() {
		return apfCarreraDto;
	}

	public void setApfCarreraDto(CarreraDto apfCarreraDto) {
		this.apfCarreraDto = apfCarreraDto;
	}

	public List<CarreraDto> getApfListCarreraDto() {
		apfListCarreraDto = apfListCarreraDto==null?(new ArrayList<CarreraDto>()):apfListCarreraDto;
		return apfListCarreraDto;
	}

	public void setApfListCarreraDto(List<CarreraDto> apfListCarreraDto) {
		this.apfListCarreraDto = apfListCarreraDto;
	}

	public ParaleloDto getApfParaleloDto() {
		return apfParaleloDto;
	}

	public void setApfParaleloDto(ParaleloDto apfParaleloDto) {
		this.apfParaleloDto = apfParaleloDto;
	}

	public Integer getApfValidadorClick() {
		return apfValidadorClick;
	}

	public void setApfValidadorClick(Integer apfValidadorClick) {
		this.apfValidadorClick = apfValidadorClick;
	}

	public ParaleloDto getApfParaleloDtoEditar() {
		return apfParaleloDtoEditar;
	}

	public void setApfParaleloDtoEditar(ParaleloDto apfParaleloDtoEditar) {
		this.apfParaleloDtoEditar = apfParaleloDtoEditar;
	}

	public CarreraDto getApfCarreraDtoEditar() {
		return apfCarreraDtoEditar;
	}

	public void setApfCarreraDtoEditar(CarreraDto apfCarreraDtoEditar) {
		this.apfCarreraDtoEditar = apfCarreraDtoEditar;
	}

	public PeriodoAcademico getApfPeriodoAcademicoEditar() {
		return apfPeriodoAcademicoEditar;
	}

	public void setApfPeriodoAcademicoEditar(PeriodoAcademico apfPeriodoAcademicoEditar) {
		this.apfPeriodoAcademicoEditar = apfPeriodoAcademicoEditar;
	}

	public Carrera getApfCarreraEditar() {
		return apfCarreraEditar;
	}

	public void setApfCarreraEditar(Carrera apfCarreraEditar) {
		this.apfCarreraEditar = apfCarreraEditar;
	}

	public Paralelo getApfParalelo() {
		return apfParalelo;
	}

	public void setApfParalelo(Paralelo apfParalelo) {
		this.apfParalelo = apfParalelo;
	}


	public NivelDto getApfNivelDto() {
		return apfNivelDto;
	}


	public void setApfNivelDto(NivelDto apfNivelDto) {
		this.apfNivelDto = apfNivelDto;
	}


	public boolean isApfHabilitadorNivel() {
		return apfHabilitadorNivel;
	}


	public void setApfHabilitadorNivel(boolean apfHabilitadorNivel) {
		this.apfHabilitadorNivel = apfHabilitadorNivel;
	}


	public List<MallaCurricularDto> getApfListMallaCurricularDto() {
		apfListMallaCurricularDto = apfListMallaCurricularDto==null?(new ArrayList<MallaCurricularDto>()):apfListMallaCurricularDto;
		return apfListMallaCurricularDto;
	}


	public void setApfListMallaCurricularDto(List<MallaCurricularDto> apfListMallaCurricularDto) {
		this.apfListMallaCurricularDto = apfListMallaCurricularDto;
	}


	public boolean isApfHabilitadorGuardar() {
		return apfHabilitadorGuardar;
	}


	public void setApfHabilitadorGuardar(boolean apfHabilitadorGuardar) {
		this.apfHabilitadorGuardar = apfHabilitadorGuardar;
	}


	public Integer getApfSeleccionarTodos() {
		return apfSeleccionarTodos;
	}


	public void setApfSeleccionarTodos(Integer apfSeleccionarTodos) {
		this.apfSeleccionarTodos = apfSeleccionarTodos;
	}


	public boolean isApfHabilitadorSeleccion() {
		return apfHabilitadorSeleccion;
	}

	public void setApfHabilitadorSeleccion(boolean apfHabilitadorSeleccion) {
		this.apfHabilitadorSeleccion = apfHabilitadorSeleccion;
	}

	public List<MallaCurricularParaleloDto> getApfListMallaCurricularParaleloDto() {
		return apfListMallaCurricularParaleloDto;
	}

	public void setApfListMallaCurricularParaleloDto(List<MallaCurricularParaleloDto> apfListMallaCurricularParaleloDto) {
		this.apfListMallaCurricularParaleloDto = apfListMallaCurricularParaleloDto;
	}


	public String getApfTituloModal() {
		return apfTituloModal;
	}


	public void setApfTituloModal(String apfTituloModal) {
		this.apfTituloModal = apfTituloModal;
	}


	public String getApfMensajeModal() {
		return apfMensajeModal;
	}


	public void setApfMensajeModal(String apfMensajeModal) {
		this.apfMensajeModal = apfMensajeModal;
	}


	public Integer getApfOpcionSeleccionada() {
		return apfOpcionSeleccionada;
	}


	public void setApfOpcionSeleccionada(Integer apfOpcionSeleccionada) {
		this.apfOpcionSeleccionada = apfOpcionSeleccionada;
	}


	public List<MallaCurricularDto> getApfListMallaCurricularDtoAgregar() {
		return apfListMallaCurricularDtoAgregar;
	}


	public void setApfListMallaCurricularDtoAgregar(List<MallaCurricularDto> apfListMallaCurricularDtoAgregar) {
		this.apfListMallaCurricularDtoAgregar = apfListMallaCurricularDtoAgregar;
	}


	public boolean isApfHabilitadorGuardarMaterias() {
		return apfHabilitadorGuardarMaterias;
	}


	public void setApfHabilitadorGuardarMaterias(boolean apfHabilitadorGuardarMaterias) {
		this.apfHabilitadorGuardarMaterias = apfHabilitadorGuardarMaterias;
	}


	public String getApfMensajeActivacion() {
		return apfMensajeActivacion;
	}


	public void setApfMensajeActivacion(String apfMensajeActivacion) {
		this.apfMensajeActivacion = apfMensajeActivacion;
	}


	public String getApfTipoCarrera() {
		return apfTipoCarrera;
	}


	public void setApfTipoCarrera(String apfTipoCarrera) {
		this.apfTipoCarrera = apfTipoCarrera;
	}


	public Integer getApfTipoUsuario() {
		return apfTipoUsuario;
	}


	public void setApfTipoUsuario(Integer apfTipoUsuario) {
		this.apfTipoUsuario = apfTipoUsuario;
	}


	public List<Nivel> getApfListNivel() {
		apfListNivel = apfListNivel==null?(new ArrayList<Nivel>()):apfListNivel;
		return apfListNivel;
	}


	public void setApfListNivel(List<Nivel> apfListNivel) {
		this.apfListNivel = apfListNivel;
	}


	public boolean isApfHabilitarPeriodo() {
		return apfHabilitarPeriodo;
	}


	public void setApfHabilitarPeriodo(boolean apfHabilitarPeriodo) {
		this.apfHabilitarPeriodo = apfHabilitarPeriodo;
	}


	public MallaCurricularParaleloDto getApfParaleloEditarCupo() {
		return apfParaleloEditarCupo;
	}


	public void setApfParaleloEditarCupo(MallaCurricularParaleloDto apfParaleloEditarCupo) {
		this.apfParaleloEditarCupo = apfParaleloEditarCupo;
	}


	public Integer getApfValidadorEdicion() {
		return apfValidadorEdicion;
	}


	public void setApfValidadorEdicion(Integer apfValidadorEdicion) {
		this.apfValidadorEdicion = apfValidadorEdicion;
	}


	public Integer getApfNuevoCupo() {
		return apfNuevoCupo;
	}


	public void setApfNuevoCupo(Integer apfNuevoCupo) {
		this.apfNuevoCupo = apfNuevoCupo;
	}


	public List<UsuarioRolJdbcDto> getApfListRoles() {
		return apfListRoles;
	}


	public void setApfListRoles(List<UsuarioRolJdbcDto> apfListRoles) {
		this.apfListRoles = apfListRoles;
	}


	public CarreraDto getApfCarreraDtoNivelacion() {
		return apfCarreraDtoNivelacion;
	}


	public void setApfCarreraDtoNivelacion(CarreraDto apfCarreraDtoNivelacion) {
		this.apfCarreraDtoNivelacion = apfCarreraDtoNivelacion;
	}


	public List<CarreraDto> getApfListCarreraDtoNivelacion() {
		return apfListCarreraDtoNivelacion;
	}


	public void setApfListCarreraDtoNivelacion(List<CarreraDto> apfListCarreraDtoNivelacion) {
		this.apfListCarreraDtoNivelacion = apfListCarreraDtoNivelacion;
	}


	public String getSufijoNivelacion() {
		return sufijoNivelacion;
	}


	public void setSufijoNivelacion(String sufijoNivelacion) {
		this.sufijoNivelacion = sufijoNivelacion;
	}


	public boolean isApfHabilitadorArea() {
		return apfHabilitadorArea;
	}


	public void setApfHabilitadorArea(boolean apfHabilitadorArea) {
		this.apfHabilitadorArea = apfHabilitadorArea;
	}


	public MallaCurricularParaleloDto getApfParaleloEditarCupoReserva() {
		return apfParaleloEditarCupoReserva;
	}


	public void setApfParaleloEditarCupoReserva(MallaCurricularParaleloDto apfParaleloEditarCupoReserva) {
		this.apfParaleloEditarCupoReserva = apfParaleloEditarCupoReserva;
	}

	public int getApfModalidadId() {
		return apfModalidadId;
	}

	public void setApfModalidadId(int apfModalidadId) {
		this.apfModalidadId = apfModalidadId;
	}

	public List<ModalidadDto> getApfListModalidadDto() {
		return apfListModalidadDto;
	}

	public void setApfListModalidadDto(List<ModalidadDto> apfListModalidadDto) {
		this.apfListModalidadDto = apfListModalidadDto;
	}

	
	
}
	
	
	
	
	
	
	
	
	
	
