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
   
 ARCHIVO:     ReporteParaleloForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones del reporte de paralelo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
03-ABRIL-2018			Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
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
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
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
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) ReporteParaleloForm
 * Managed Bean que maneja las peticiones del reporte de paralelo.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="reporteParaleloForm")
@SessionScoped
public class ReporteParaleloForm implements Serializable{

	private static final long serialVersionUID = -7458138879145808577L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	//OBJETOS
	private Usuario rpfUsuario;
	private PeriodoAcademico rpfPeriodoAcademico;
	private CarreraDto rpfCarreraDto ;
	private ParaleloDto rpfParaleloDto;
	private NivelDto rpfNivelDto;
	private String rpfTipoCarrera;
	private Integer rpfTipoUsuario;
	
	private List<HorarioAcademicoDto> rpfListHorarioAcademicoBusq;
	
	//LISTA DE OBJETOS
	private List<PeriodoAcademico> rpfListPeriodoAcademico;
	private List<CarreraDto> rpfListCarreraDto;
	private List<ParaleloDto> rpfListParaleloDto;
	private List<Nivel> rpfListNivel;
	private List<MallaCurricularDto> rpfListMallaCurricularDto;
	private List<MallaCurricularDto> rpfListMallaCurricularDtoModulos;
	private List<MallaCurricularParaleloDto> rpfListMallaCurricularParaleloDto;
	private List<MallaCurricularDto> rpfListMallaCurricularDtoAgregar;
	private MallaCurricularParaleloDto rpfParaleloEditarCupo;
	private List<EstudianteJdbcDto> rpfListaEstudiantes;
	
	private Integer rpfNuevoCupo;
	//AUXILIARES		

	private Integer rpfValidadorClick;
	private Integer rpfValidadorEdicion;
	private boolean rpfHabilitadorNivel;
	private boolean rpfHabilitadorSeleccion;
	private boolean rpfHabilitadorGuardar;
	private boolean rpfHabilitadorGuardarMaterias;
	private Integer rpfSeleccionarTodos;
	private String rpfTituloModal;
	private String rpfMensajeModal;
	private String rpfMensajeActivacion;
	private Integer rpfOpcionSeleccionada;
	private boolean rpfHabilitarPeriodo;
	private boolean rpfHabilitarExportar;
	
	// OBJETOS PARA EDITAR Y CREAR NUEVO
	private ParaleloDto rpfParaleloDtoEditar;
	private CarreraDto rpfCarreraDtoEditar;
	private PeriodoAcademico rpfPeriodoAcademicoEditar;
	private Carrera rpfCarreraEditar;
	private Paralelo rpfParalelo;
	
	//PARA HABILITAR
	private Integer rpfHabilitarBoton;

	//  SE COMENTA: NO SE NECESITA ELIMINAR PARALELO PUES SE DEBE DESHABILITAR
	//	private ParaleloDto rpfParaleloEliminar;
	//	private boolean rpfAlertaParaleloEliminar;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
		
	@PostConstruct
	public void inicializar(){
		
	}
	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
		
	@EJB 
	ParaleloDtoServicioJdbc servRpfParaleloDto;
	@EJB 
	ParaleloServicio servRpfParalelo;
	@EJB 
	PeriodoAcademicoServicio servRpfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servRpfCarreraDto;
	@EJB
	CarreraServicio servRpfCarrera;
	@EJB
	NivelServicio servNivelDtoServicio;
	@EJB
	ParaleloDtoServicioJdbc servParaleloDtoServicioJdbc;
	@EJB
	MallaMateriaDtoServicioJdbc servMallaMateriaDtoServicioJdbc;
	@EJB
	MallaCurricularParaleloDtoServicioJdbc servMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	MallaCurricularParaleloServicio servMallaCurricularParaleloServicio;
	@EJB
	private UsuarioRolServicio servRpfUsuarioRolServicio;
	@EJB
	HorarioAcademicoDtoServicioJdbc servRpfHorarioAcademicoDtoServicioJdbc;
	@EJB
	PeriodoAcademicoServicio servRpfPeriodoAcademicoServicio;
	@EJB
	DependenciaServicio servRpfDependenciaServicio;
	
	@EJB
	EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	
	/**
	 * Método que permite iniciar la administración del paralelo
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración Paralelo.
	 */
	
	public String irReporteParalelo(Usuario usuario) { 
		rpfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rpfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rpfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rpfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					}
					rpfTipoCarrera="carreras";
					rpfHabilitarPeriodo=true;
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rpfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					rpfTipoCarrera="programas";
					rpfHabilitarPeriodo=false;
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
					rpfTipoUsuario = RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue();
					rpfTipoCarrera="programas";
					rpfHabilitarPeriodo=false;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rpfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
				}
			}
			rpfPeriodoAcademico = new PeriodoAcademico();
			rpfPeriodoAcademico = servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		}
		iniciarParametros();
		return "irReporteParalelo";
	}
	
	
	/**
	 * Método que permite iniciar los reportes de matriculados en posgrado
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración Paralelo.
	 */
	
	public String irReporteMatriculadosPosgrado(Usuario usuario) { 
		try {
			rpfUsuario= usuario;
			rpfHabilitarPeriodo=false;
			rpfTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
			iniciarParametrosPosgrado();
			return "irReportePosgrado";	
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	
	public String irInicioPosgrado() {
		limpiarPosgrado();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados
	 * @return 
	 */
	public void limpiar()  {
			iniciarParametros();
	}
	
	public void limpiarPosgrado()  {
		iniciarParametrosPosgrado();
	}
	
	public String cancelarAgregarMateria(){
		iniciarParametros();
		return "cancelarAgregarMaterias";
	}
	
//	/**
//	 * Método para limpiar la lista de paralelos cuando se cambia la carrera, y no se presiona buscar
//	 * @return 
//	 */
//	public String limpiarParalelos(){
//		
//	rpfListParaleloDto = new ArrayList<ParaleloDto>();
//		return null;
//	}
	
	/**
	 * Método para limpiar la lista de paralelos cuando se cambia la carrera, y no se presiona buscar
	 * @return 
	 */
	public String limpiarMaterias(){
		rpfListHorarioAcademicoBusq = null;
		return null;
	}
	
	
	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametros() {
		rpfHabilitadorGuardarMaterias=true;
		rpfHabilitadorNivel = true;
		rpfHabilitadorGuardar = true;
		rpfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
		rpfHabilitadorSeleccion = true; 
		rpfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico


		rpfListParaleloDto = new ArrayList<ParaleloDto>(); //Inicio lista de Paralelos Dto


		rpfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
		rpfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel
		rpfNivelDto = new NivelDto();

		//			rpfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto

		rpfCarreraDto= new CarreraDto();    //Instancio el objeto carrera

		rpfParaleloDto= new ParaleloDto();    //Instancio Objeto paralelo

		rpfParaleloDtoEditar= new ParaleloDto(); //Instancio Objeto a editar

		//			rpfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
		rpfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

		rpfValidadorClick = 0;
		rpfValidadorEdicion = 0;

		llenarPeriodos(); //Listar todos los peridos academicos

		llenarCarreras();   //Listar  todos las carreras por el  usuario

		llenarNiveles(); //Listar todos los niveles



		//			llenarParalelos();   //Listar  todos los paralelos por el  usuario 


		rpfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
		rpfListMallaCurricularDtoAgregar = new ArrayList<MallaCurricularDto>();
		rpfListMallaCurricularDtoModulos = new ArrayList<MallaCurricularDto>();
		rpfListHorarioAcademicoBusq = new ArrayList<>();
		rpfHabilitarBoton = 0;
	}

	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametrosPosgrado() {
		try {
			rpfHabilitarExportar=true;
			rpfHabilitadorGuardarMaterias=true;
			rpfHabilitadorNivel = true;
			rpfHabilitadorGuardar = true;
			rpfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			rpfHabilitadorSeleccion = true; 
			rpfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			rpfListaEstudiantes = new ArrayList<EstudianteJdbcDto>();	


			rpfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			rpfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel
			rpfNivelDto = new NivelDto();

			rpfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto

			rpfCarreraDto= new CarreraDto();    //Instancio el objeto carrera

			rpfParaleloDto= new ParaleloDto();    //Instancio Objeto paralelo

			rpfParaleloDtoEditar= new ParaleloDto(); //Instancio Objeto a editar

			rpfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
			rpfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

			rpfValidadorClick = 0;
			rpfValidadorEdicion = 0;

			llenarPeriodos(); //Listar todos los peridos academicos

			llenarCarreras();   //Listar  todos las carreras por el  usuario



		} catch (Exception e) {
		}
	}
	
	public void llenarProgramasPosgradoPorCohorte(){
		try {
			rpfListCarreraDto = servRpfCarreraDto.buscarXPracIdPosgrado(rpfParaleloDtoEditar.getPracId());
		} catch (CarreraDtoJdbcException | CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	/**
	 * Método para llenar la lista de periodos 
	 */

	public void llenarPeriodos(){
		rpfListPeriodoAcademico= null;
		if(rpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
			rpfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosActivo();// llamo al servicio de periodoAcademico para listar todos
		}else if (rpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
				|| rpfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
			rpfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosPosgradoActivo();// llamo al servicio de periodoAcademico para listar todos			
		}else if(rpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
			rpfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosActivoXtipoPeriodo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
		}
	}

	 /**
	 * Método para llenar la lista de niveles 
	 */
	
	public void llenarNiveles(){
		rpfListNivel= new ArrayList<Nivel>();
		try {
			if(rpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				int contador = 0;
				for (CarreraDto itemCarr : rpfListCarreraDto) {
					if(itemCarr.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						contador = contador + 1;
					}
				}
				if(contador >= 1){
					rpfListNivel = servNivelDtoServicio.listarNivelacion();
				}else{
					rpfListNivel= servNivelDtoServicio.listarTodos();
				}
			}else if (rpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
					|| rpfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				rpfListNivel= servNivelDtoServicio.listarTodosPosgrado();			
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

		rpfListCarreraDto= null;
		rpfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
		rpfListParaleloDto=null;

		try {
			//listo las carreras en el combo, de acuerdo al Usuario y el periodo seleccionado

			//	rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXPeriodoAcademico(rpfUsuario.getUsrId(), rpfPeriodoAcademico.getPracId()); //Metodo alternativo sin importar ROL estado de RolFlujoCarrera
			//creo una lista de Carreras  X Usuario X ROL Activo y el periodo académico activo)
			if(rpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				if(rpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrr(rpfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				}
				if(rpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrr(rpfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				}
				if(rpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rpfListCarreraDto=servRpfCarreraDto.listarTodos();
				}

			}else if (rpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrPosgrado(rpfUsuario.getUsrId(), RolConstantes.ROL_SECREPOSGRADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);			
			}else if (rpfTipoUsuario == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
				rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrNivelacion(rpfUsuario.getUsrId(), RolConstantes.ROL_USUARIONIVELACION_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);			
			}else if (rpfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				rpfListCarreraDto=servRpfCarreraDto.listarTodosPosgrado();
			}else if(rpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXIdRolXEstadoRolFlCrrXTipo(rpfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
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
	 * Método para llenar la Lista de paralelos por la Carrera y el Periodo 
	 * 
	 */
	public void buscarMaterias(){
		rpfListHorarioAcademicoBusq= null;

		try {
			if(rpfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe selecctionar el periodo académico");	
			}else {
				//realiza la busquedad de los paralelos por carrera y periodo
				//			rpfListParaleloDto= servRpfParaleloDto.listarXusuarioXcarreraXperiodo(rpfUsuario.getUsrId(),rpfCarreraDto.getCrrId(), rpfPeriodoAcademico.getPracId());
				//				rpfListParaleloDto= servRpfParaleloDto.listarXusuarioXcarreraXperiodoXNivel(rpfUsuario.getUsrId(),rpfCarreraDto.getCrrId(), rpfPeriodoAcademico.getPracId(),rpfNivelDto.getNvlId());

				List<MallaCurricularParaleloDto> listaMallaCurricularParalelo = new ArrayList<>();

				listaMallaCurricularParalelo = servMallaCurricularParaleloDtoServicioJdbc.listarMallasXCarrera(rpfCarreraDto.getCrrId(), rpfListCarreraDto);

				rpfListHorarioAcademicoBusq = servRpfHorarioAcademicoDtoServicioJdbc.listarHorarioXpracIdXcrrId(rpfPeriodoAcademico.getPracId(), rpfCarreraDto.getCrrId(), rpfListCarreraDto , listaMallaCurricularParalelo);
				if(rpfListHorarioAcademicoBusq.size() > 0){
					rpfHabilitarBoton = GeneralesConstantes.APP_ID_BASE;
				}
			}

		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametros();
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
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

		rpfParaleloDto= null;
		rpfParaleloDto= paralelo;
		if(rpfParaleloDto.getPrlEstado()==ParaleloConstantes.ESTADO_INACTIVO_VALUE){
			rpfMensajeActivacion = "Activar paralelo";
		}else{
			rpfMensajeActivacion = "Desactivar paralelo";
		}
		String retorno= null;
		try {
			rpfListMallaCurricularParaleloDto = new ArrayList<MallaCurricularParaleloDto>();
			rpfListMallaCurricularParaleloDto = servMallaCurricularParaleloDtoServicioJdbc.listarMallasXParaleloXMateria(rpfParaleloDto.getPrlId());
			//	    	 Iterator<MallaCurricularDto> it = rpfListMallaCurricularDto.iterator();
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

			rpfPeriodoAcademico = new PeriodoAcademico(); //Instancio el  periodo académico
			try {
				rpfPeriodoAcademico= servRpfPeriodoAcademico.buscarPorId(rpfParaleloDto.getPracId()); //busco el periodo académico por Id
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (PeriodoAcademicoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}  

			if (rpfPeriodoAcademico.getPracEstado()==PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE){	//verifico que el periodo académico del paralelo seleccionado esta activo

				retorno= "irEditarParalelo";   // solo se puede editar un paralelo del período académico activo.

			}

			else // si el periodo del paralelo esta inactivo, presento mensaje de error
			{

				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.editar.paralelo.validacion.periodo.inactivo")));	
				iniciarParametros();

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
		rpfParaleloDtoEditar= new ParaleloDto();
		rpfParaleloDtoEditar = rpfParaleloDto;
		if(rpfOpcionSeleccionada==1){

			try {

				servRpfParalelo.editar(rpfParaleloDtoEditar);// Se envía el paralelo a editar y esperamos se realice o no el cambio
				//  Igualmente se puede enviar los paramteros a editar	                   
				//	if(servRpfParalelo.editarXParametros(rpfParaleloDtoEditar.getPrlId(), rpfParaleloDtoEditar.getPrlCupo(), rpfParaleloDtoEditar.getPrlDescripcion(), rpfParaleloDtoEditar.getPrlCodigo(), rpfParaleloDtoEditar.getPrlEstado()))

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
			for (MallaCurricularParaleloDto item : rpfListMallaCurricularParaleloDto) {
				if(item.getMlcrprInscritos()!=0){
					op=true;	
				}
			}
			if(!op){
				try {
					if(rpfParaleloDto.getPrlEstado()==ParaleloConstantes.ESTADO_ACTIVO_VALUE){
						servRpfParalelo.desactivarPprlId(rpfParaleloDtoEditar.getPrlId());
						FacesUtil.limpiarMensaje();		
						//TODO mensajes
						FacesUtil.mensajeInfo("El paralelo seleccionado ha sido desactivado.");	
					}else{
						servRpfParalelo.activarPprlId(rpfParaleloDtoEditar.getPrlId());
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

		rpfListCarreraDto= new ArrayList<CarreraDto>() ;
		llenarCarreras();
		rpfPeriodoAcademico= new PeriodoAcademico();
		rpfCarreraDto = new CarreraDto();
		rpfNivelDto.setNvlId(GeneralesConstantes.APP_ID_BASE);
		rpfParaleloDto= new ParaleloDto(); //instancio el objeto Paralelo para la creación del mismo
		rpfHabilitadorNivel=true;
		try {
			if(rpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				rpfPeriodoAcademico= servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);//busco el período academico activo, pues solo en el activo puede añadirse nuevos paralelos
			}else if (rpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				rpfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);			
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
		//			rpfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rpfUsuario.getUsrId(), RolConstantes.ROL_BD_DIRCARRERA, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rpfPeriodoAcademico.getPracId());
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
	 * Método para  grabar un nuevo paralelo
	 * @return navagación al xhtml AdministracionParalelo
	 */
	public String crearParalelo(){
		rpfPeriodoAcademicoEditar=new PeriodoAcademico();
		rpfCarreraEditar = new Carrera();
		try	{	
			rpfParaleloDto.setPracId(rpfPeriodoAcademico.getPracId()); //seteo el Id del Periodo Académico en el ParaleloDto,


			servRpfParalelo.anadirParaleloMlCrPr(rpfParaleloDto, rpfListMallaCurricularDto);  //llamo al método añadir paralelo y envío como parametros los objetos (ParaleloDto, ListAMallaCurricular)
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
			servRpfParalelo.anadirMallasAParalelo(rpfParaleloDto, rpfListMallaCurricularDtoAgregar);  
			FacesUtil.limpiarMensaje();
			//TODO MENSAJE
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
		try {
			rpfHabilitadorGuardar=true;
			StringBuilder sb = new StringBuilder();
			if(rpfParaleloDto.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
				Carrera crrAux = servRpfCarrera.buscarPorId(rpfParaleloDto.getCrrId());
				String[] arregloNombre = crrAux.getCrrDescripcion().split(" ");

				for (int j = 0; j < arregloNombre.length; j++) {
					if(arregloNombre[j].length()>3){
						sb.append(arregloNombre[j].substring(0, 1));
					}
				}
				rpfHabilitadorNivel=false;
			}else{
				rpfParaleloDto.setPrlCodigo(null);
				rpfParaleloDto.setPrlDescripcion(null);
				rpfHabilitadorNivel=true;
				rpfHabilitadorSeleccion = true; 
				rpfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
			}
			if(rpfNivelDto.getNvlId()==GeneralesConstantes.APP_ID_BASE){
				rpfParaleloDto.setPrlCodigo(null);
				rpfParaleloDto.setPrlDescripcion(null);
				rpfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();

				//			
			}else{
				sb.append(rpfNivelDto.getNvlId());
				sb.append("-");
				try {
					List<ParaleloDto> listaParalelos = new ArrayList<ParaleloDto>();
					listaParalelos = servParaleloDtoServicioJdbc.listarXNivelCarreraXperiodo(rpfParaleloDto.getCrrId(),rpfPeriodoAcademico.getPracId(), rpfNivelDto.getNvlId());
					if(listaParalelos.size()==0){
						sb.append(1);
					}else{
						//						String codigo = listaParalelos.get(listaParalelos.size()-1).getPrlCodigo();
						////						sb.append(codigo.substring(0,codigo.length()-1));
						//						sb.append(Integer.valueOf(codigo.substring(codigo.length()-1))+1);
						//						if(Integer.parseInt(sb.toString().substring(sb.toString().length() - 2))>=10){
						//							listaParalelos = servParaleloDtoServicioJdbc.listarMayorParaleloXNivelCarreraXperiodo(rpfParaleloDto.getCrrId(),rpfPeriodoAcademico.getPracId(), rpfNivelDto.getNvlId());
						//							sb = new StringBuilder();
						//							Integer codigoAux=Integer.parseInt(listaParalelos.get(listaParalelos.size()-1).getPrlCodigo().substring(sb.toString().length() - 2));

						sb.append(listaParalelos.size()+1);
						//						}
					}
				} catch (ParaleloDtoException | ParaleloDtoNoEncontradoException e) {
					sb.append(1);
				}
				rpfParaleloDto.setPrlCodigo(sb.toString());
				rpfParaleloDto.setPrlDescripcion(sb.toString());
				try {
					rpfListMallaCurricularDto= servMallaMateriaDtoServicioJdbc.listarMallasMateriasXNivelXCarrera(rpfParaleloDto.getCrrId(), rpfNivelDto.getNvlId());
					//					Iterator<MallaCurricularDto> it = rpfListMallaCurricularDto.iterator();
					//					while(it.hasNext()) {
					//				        MallaCurricularDto element = it.next();
					//				        if(element.getMtrSubId()!=GeneralesConstantes.APP_ID_BASE){
					//				        	rpfListMallaCurricularDtoModulos.add(element);
					//				       	 	it.remove();
					//				        }
					//				    }
					rpfHabilitadorGuardar = false;
					rpfHabilitadorSeleccion = false; 
					rpfHabilitadorGuardar=false;
				} catch (MallaCurricularDtoException | MallaCurricularDtoNoEncontradoException e) {
					rpfParaleloDto.setPrlCodigo(null);
					rpfParaleloDto.setPrlDescripcion(null);
					rpfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
					rpfHabilitadorGuardar = true;
					rpfHabilitadorSeleccion = true; 
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.buscar.no.result.exception")));
				}
			}
		} catch (CarreraNoEncontradoException | CarreraException e) {
			rpfHabilitadorNivel=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraDto.buscar.carrera.por.usuario.no.result.exception")));
		}
	}
	
	public void cambiarNivel(){
		if(rpfParaleloDto.getNvlId()==GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.llenar.paralelos.id.carrera.id.nivel.validacion.exception")));
		}
	}

	public void buscarSubMaterias(MallaCurricularDto entidad){
		for (MallaCurricularDto item : rpfListMallaCurricularDto) {
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
					rpfListMallaCurricularParaleloDto = new ArrayList<MallaCurricularParaleloDto>();
					rpfListMallaCurricularParaleloDto = servMallaCurricularParaleloDtoServicioJdbc.listarMallasXParaleloXMateria(rpfParaleloDto.getPrlId());
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
	 * verifica que haga click en el boton editar paralelo
	 */
	public void buscarEstudiantesPosgrado(){
		try {
			rpfListaEstudiantes=servEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXProgramaPosgrado(rpfParaleloDtoEditar.getPracId(), rpfCarreraDto.getCrrId());
			
			generarReporteEstados(rpfListaEstudiantes);
			rpfHabilitarExportar=false;
		} catch (EstudianteDtoJdbcException e) {
			rpfHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			rpfHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
		}
	}

	/**
	 * direcciona a la página de agregar materias
	 */
	public String agregarMaterias(){
		rpfHabilitadorGuardarMaterias=true;
		rpfListMallaCurricularDtoAgregar = new ArrayList<MallaCurricularDto>();
		try {
			List<MallaCurricularDto> auxLista = new ArrayList<MallaCurricularDto>();
			auxLista = servMallaMateriaDtoServicioJdbc.listarMallasMateriasXNivelXCarrera(rpfParaleloDto.getCrrId(), rpfParaleloDto.getNvlId());
			for (MallaCurricularDto item1 : auxLista) {
				boolean op= true;
				for (MallaCurricularParaleloDto item2 : rpfListMallaCurricularParaleloDto) {
					if(item2.getMtrDescripcion().equals(item1.getMtrDescripcion())){
						op=false;
					}
				}
				if(op){
					rpfListMallaCurricularDtoAgregar.add(item1);
				}

			}
			if(rpfListMallaCurricularDtoAgregar.size()>0){
				rpfHabilitadorGuardarMaterias=false;
			}else{
				rpfHabilitadorGuardarMaterias=true;
				FacesUtil.limpiarMensaje();
				//TODO mensajes
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
	//			rpfAlertaParaleloEliminar = servRpfParalelo.buscarPorId(rpfParaleloEliminar.getPrlId());
	//			if (rpfAlertaParalelolEliminar == false){
	//				servRpfParalelo.eliminarParalelo(rpfParaleloEliminar.getPrlId());
	//				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.eliminar.paralelo.exitoso")));
	//			}else{
	//				rpfValidadorClick = 0;
	//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.eliminar.paralelo.ocupado")));
	//			}
	//			
	//		}catch (ParaleloNoEncontradoException e) {
	//			rpfValidadorClick = 0;
	//			FacesUtil.mensajeError(e.getMessage());
	//		} catch (ParaleloException e) {
	//			rpfValidadorClick = 0;
	//			FacesUtil.mensajeError(e.getMessage());
	//		} catch (ParaleloValidacionException e) {
	//			rpfValidadorClick = 0;
	//			FacesUtil.mensajeError(e.getMessage());
	//		} catch (Exception e) {
	//			rpfValidadorClick = 0;
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
		for (MallaCurricularDto item : rpfListMallaCurricularDto) {
			if(item.isMtrAsignada()){
				op= true;
				break;
			}
		}
		if(op){

			boolean op2 = false;
			for (MallaCurricularDto item : rpfListMallaCurricularDto) {
				if(item.isMtrAsignada() && !(item.getMlcrprCupo()!=null) ){
					op2= true;
					break;
				}
			}
			if(op2){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.crear.sin.cupo.validacion.exception")));
				rpfValidadorClick= 0;
			}else{
				rpfValidadorClick= 1;	
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
			rpfValidadorClick= 0;
		}
		return null;
	}

	/**
	 * verifica que haga click en el boton agregar materias
	 */
	public String verificarClickNuevaMateria(){
		boolean op = false;
		for (MallaCurricularDto item : rpfListMallaCurricularDtoAgregar) {
			if(item.isMtrAsignada()){
				op= true;
				break;
			}
		}
		if(op){

			boolean op2 = false;
			for (MallaCurricularDto item : rpfListMallaCurricularDtoAgregar) {
				if(item.isMtrAsignada() && !(item.getMlcrprCupo()!=null)){
					op2= true;
					break;
				}
			}
			if(op2){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionParalelo.crear.sin.cupo.validacion.exception")));
				rpfValidadorClick= 0;
			}else{
				rpfValidadorClick= 1;	
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
			rpfValidadorClick= 0;
		}
		return null;
	}

	/**
	 * verifica que haga click en el boton editar paralelo
	 */
	public String verificarClick(Integer opcion){
		if(opcion==1){
			rpfValidadorClick= 2;
			rpfTituloModal="Editar paralelo";
			rpfMensajeModal="¿Seguro que desea guardar la edición del paralelo?";
			rpfOpcionSeleccionada=1;
		}else if (opcion == 2){
			rpfValidadorClick= 2;
			if(rpfParaleloDto.getPrlEstado()==ParaleloConstantes.ESTADO_ACTIVO_VALUE){
				rpfTituloModal="Desactivar paralelo";
				rpfMensajeModal="¿Seguro que desea desactivar el paralelo y sus materias?";
				rpfOpcionSeleccionada=2;
			}else{
				rpfTituloModal="Activar paralelo";
				rpfMensajeModal="¿Seguro que desea activar el paralelo y sus materias?";
				rpfOpcionSeleccionada=2;	
			}

		}

		return null;
	}

	/**
	 * verifica que haga click en el boton seleccionar todos
	 */
	public void seleccionarTodos(){
		if(rpfSeleccionarTodos!=GeneralesConstantes.APP_ID_BASE){
			for (MallaCurricularDto item : rpfListMallaCurricularDto) {
				if(rpfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE){
					item.setMtrAsignada(true);

				}else if (rpfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_NADA_VALUE){
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
		if(rpfSeleccionarTodos!=GeneralesConstantes.APP_ID_BASE){
			for (MallaCurricularDto item : rpfListMallaCurricularDtoAgregar) {
				if(rpfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE){
					item.setMtrAsignada(true);
					//						rpfHabilitadorGuardarMaterias=false;

				}else if (rpfSeleccionarTodos==GeneralesConstantes.APP_TIPO_SELECCION_NADA_VALUE){
					item.setMtrAsignada(false);
					//						rpfHabilitadorGuardarMaterias=true;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
				}	
			}	
		}
	}

	public void actualizarListaMalla(){
		for (MallaCurricularDto item : rpfListMallaCurricularDto) {
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
		rpfParaleloEditarCupo = item;
		rpfNuevoCupo = null;
		rpfValidadorEdicion = 1;
	}

	/**
	 * verifica que haga click en el boton editar cupo
	 */
	public void editarCupoNo(){
		rpfValidadorEdicion = 0;
	}
	//		
	public void guardarCupo(){
		if(rpfNuevoCupo!=null){
			if(rpfNuevoCupo<rpfParaleloEditarCupo.getMlcrprInscritos()){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn("El cupo no puede ser menor al número de inscritos del paralelo.");
			}else if(rpfNuevoCupo==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn("El cupo no puede tener el valor de cero.");
			}else{
				try {
					servMallaCurricularParaleloServicio.editarCupoPorMlcrprId(rpfParaleloEditarCupo.getMlcrprId(), rpfNuevoCupo);
					for (MallaCurricularParaleloDto item : rpfListMallaCurricularParaleloDto) {
						String nota1 = Integer.toString(rpfParaleloEditarCupo.getMtrId());
						String nota2 = Integer.toString(item.getMtrSubId());
						if (nota1.equals(nota2)) {
							item.setMlcrprCupo(rpfNuevoCupo);
							servMallaCurricularParaleloServicio.editarCupoPorMlcrprId(item.getMlcrprId(), rpfNuevoCupo);
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
				verDatosParalelo(rpfParaleloDto, 0);	
			}

		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Paralelo.malla.curricular.seleccionar.materias")));
		}
		rpfValidadorEdicion = 0;

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
		rpfValidadorClick  = 0;
	}


	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir(){
		//			try {
		if(rpfListHorarioAcademicoBusq.size() > 0){
			generarReporteParalelo(rpfListHorarioAcademicoBusq);
			habilitaModalImpresion();
		}else{
			bloqueaModal();
		}
		rpfHabilitarBoton = 0;
		//			} catch (HorarioAcademicoDtoException e) {
		//				FacesUtil.limpiarMensaje();
		//				FacesUtil.mensajeError(e.getMessage());
		//			} catch (HorarioAcademicoDtoNoEncontradoException e) {
		//				FacesUtil.limpiarMensaje();
		//				FacesUtil.mensajeError(e.getMessage());
		//			}

	}

	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir2(){
		//					try {
		if(rpfListHorarioAcademicoBusq.size() > 0){
			generarReporteParalelo(rpfListHorarioAcademicoBusq);
			habilitaModalImpresion2();
		}else{
			bloqueaModal();
		}
		rpfHabilitarBoton = 0;
		//					} catch (HorarioAcademicoDtoException e) {
		//						FacesUtil.limpiarMensaje();
		//						FacesUtil.mensajeError(e.getMessage());
		//					} catch (HorarioAcademicoDtoNoEncontradoException e) {
		//						FacesUtil.limpiarMensaje();
		//						FacesUtil.mensajeError(e.getMessage());
		//					}

	}


	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		rpfValidadorClick = 0;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		rpfValidadorClick = 1;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion2(){
		rpfValidadorClick = 2;
	}


	/**
	 * Método que genera el reporte
	 */
	public void generarReporteParalelo(List<HorarioAcademicoDto> listaHorarioParalelo){
		try {

			int totalInscritos = 0;
			int totalMatriculados = 0;
			String facultad = null;
			for (HorarioAcademicoDto item : listaHorarioParalelo) {
				totalInscritos = totalInscritos + item.getMlcrprInscritos().intValue();
				totalMatriculados = totalMatriculados + item.getRcesNumeroMatriculados().intValue();
				facultad = item.getDpnDescripcion();
			}

			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE HORARIO *********//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "reporteParalelos";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			PeriodoAcademico periodoAcademico = new PeriodoAcademico();
			periodoAcademico = servRpfPeriodoAcademicoServicio.buscarPorId(rpfPeriodoAcademico.getPracId());

			frmRrmParametros.put("periodo", periodoAcademico.getPracDescripcion());
			frmRrmParametros.put("facultad", facultad);
			frmRrmParametros.put("nick", rpfUsuario.getUsrNick());
			frmRrmParametros.put("total_inscritos", String.valueOf(totalInscritos));
			frmRrmParametros.put("total_matriculados", String.valueOf(totalMatriculados));


			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoHorario = null;


			for (HorarioAcademicoDto item : listaHorarioParalelo) {
				datoHorario = new HashMap<String, Object>();

				datoHorario.put("carrera", item.getCrrDetalle());
				datoHorario.put("nivel", item.getNvlDescripcion());
				datoHorario.put("asignatura", item.getMtrDescripcion());
				datoHorario.put("paralelo", item.getPrlDescripcion());
				datoHorario.put("aula", item.getAlaDescripcion());
				datoHorario.put("cupo", item.getMlcrprCupo().toString());
				datoHorario.put("inscritos", item.getMlcrprInscritos().toString());
				datoHorario.put("matriculados", item.getRcesNumeroMatriculados().toString());

				frmRrmCampos.add(datoHorario);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/paralelo");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");


			//			frmRrmCampos.add(datoHorario);
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 


	}

	/**
	 * Genera el reporte de registrados y lo establece en la sesión como atributo
	 * @param List<RegistradosDto> frmRfRegistradosDto
	 * @return void
	 */
	public static void generarReporteEstados(List<EstudianteJdbcDto> listaEstados){
		List<Map<String, Object>> frmCrpCampos = null;
		Map<String, Object> frmCrpParametros = null;
		String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
		// ****************************************************************//
		// ****************************************************************//
		frmCrpNombreReporte = "ReporteMatriculados";
		java.util.Date date= new java.util.Date();
		frmCrpParametros = new HashMap<String, Object>();
		String fecha = new Timestamp(date.getTime()).toString();
		frmCrpParametros.put("fecha",fecha);

		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		for (EstudianteJdbcDto item : listaEstados) {
			dato = new HashMap<String, Object>();
			dato.put("convocatoria",item.getPracDescripcion());
			dato.put("cedula", item.getPrsIdentificacion());
			dato.put("apellido1", item.getPrsPrimerApellido());
			dato.put("apellido2", item.getPrsSegundoApellido());
			dato.put("nombres", item.getPrsNombres());
			dato.put("facultad", item.getDpnDescripcion());
			dato.put("carrera", item.getCrrDescripcion());
			dato.put("email", item.getPrsMailInstitucional());
			ListasCombosForm lcfAux = new ListasCombosForm();
			dato.put("estado", lcfAux.getEstadoMatricula(item.getFcinMatriculado()));
			frmCrpCampos.add(dato);
		}
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		// ******************FIN DE GENERACION DE REPORTE ************//	
	} 

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getRpfUsuario() {
		return rpfUsuario;
	}
	public void setRpfUsuario(Usuario rpfUsuario) {
		this.rpfUsuario = rpfUsuario;
	}
	public PeriodoAcademico getRpfPeriodoAcademico() {
		return rpfPeriodoAcademico;
	}
	public void setRpfPeriodoAcademico(PeriodoAcademico rpfPeriodoAcademico) {
		this.rpfPeriodoAcademico = rpfPeriodoAcademico;
	}
	public CarreraDto getRpfCarreraDto() {
		return rpfCarreraDto;
	}
	public void setRpfCarreraDto(CarreraDto rpfCarreraDto) {
		this.rpfCarreraDto = rpfCarreraDto;
	}
	public ParaleloDto getRpfParaleloDto() {
		return rpfParaleloDto;
	}
	public void setRpfParaleloDto(ParaleloDto rpfParaleloDto) {
		this.rpfParaleloDto = rpfParaleloDto;
	}
	public NivelDto getRpfNivelDto() {
		return rpfNivelDto;
	}
	public void setRpfNivelDto(NivelDto rpfNivelDto) {
		this.rpfNivelDto = rpfNivelDto;
	}
	public String getRpfTipoCarrera() {
		return rpfTipoCarrera;
	}
	public void setRpfTipoCarrera(String rpfTipoCarrera) {
		this.rpfTipoCarrera = rpfTipoCarrera;
	}
	public Integer getRpfTipoUsuario() {
		return rpfTipoUsuario;
	}
	public void setRpfTipoUsuario(Integer rpfTipoUsuario) {
		this.rpfTipoUsuario = rpfTipoUsuario;
	}
	public List<PeriodoAcademico> getRpfListPeriodoAcademico() {
		rpfListPeriodoAcademico = rpfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):rpfListPeriodoAcademico;
		return rpfListPeriodoAcademico;
	}
	public void setRpfListPeriodoAcademico(List<PeriodoAcademico> rpfListPeriodoAcademico) {
		this.rpfListPeriodoAcademico = rpfListPeriodoAcademico;
	}
	public List<CarreraDto> getRpfListCarreraDto() {
		rpfListCarreraDto = rpfListCarreraDto==null?(new ArrayList<CarreraDto>()):rpfListCarreraDto;
		return rpfListCarreraDto;
	}
	public void setRpfListCarreraDto(List<CarreraDto> rpfListCarreraDto) {
		this.rpfListCarreraDto = rpfListCarreraDto;
	}
	public List<ParaleloDto> getRpfListParaleloDto() {
		rpfListParaleloDto = rpfListParaleloDto==null?(new ArrayList<ParaleloDto>()):rpfListParaleloDto;
		return rpfListParaleloDto;
	}
	public void setRpfListParaleloDto(List<ParaleloDto> rpfListParaleloDto) {
		this.rpfListParaleloDto = rpfListParaleloDto;
	}
	public List<Nivel> getRpfListNivel() {
		rpfListNivel = rpfListNivel==null?(new ArrayList<Nivel>()):rpfListNivel;
		return rpfListNivel;
	}
	public void setRpfListNivel(List<Nivel> rpfListNivel) {
		this.rpfListNivel = rpfListNivel;
	}
	public List<MallaCurricularDto> getRpfListMallaCurricularDto() {
		rpfListMallaCurricularDto = rpfListMallaCurricularDto==null?(new ArrayList<MallaCurricularDto>()):rpfListMallaCurricularDto;
		return rpfListMallaCurricularDto;
	}
	public void setRpfListMallaCurricularDto(List<MallaCurricularDto> rpfListMallaCurricularDto) {
		this.rpfListMallaCurricularDto = rpfListMallaCurricularDto;
	}
	public List<MallaCurricularDto> getRpfListMallaCurricularDtoModulos() {
		rpfListMallaCurricularDtoModulos = rpfListMallaCurricularDtoModulos==null?(new ArrayList<MallaCurricularDto>()):rpfListMallaCurricularDtoModulos;
		return rpfListMallaCurricularDtoModulos;
	}
	public void setRpfListMallaCurricularDtoModulos(List<MallaCurricularDto> rpfListMallaCurricularDtoModulos) {
		this.rpfListMallaCurricularDtoModulos = rpfListMallaCurricularDtoModulos;
	}
	public List<MallaCurricularParaleloDto> getRpfListMallaCurricularParaleloDto() {
		rpfListMallaCurricularParaleloDto = rpfListMallaCurricularParaleloDto==null?(new ArrayList<MallaCurricularParaleloDto>()):rpfListMallaCurricularParaleloDto;
		return rpfListMallaCurricularParaleloDto;
	}
	public void setRpfListMallaCurricularParaleloDto(List<MallaCurricularParaleloDto> rpfListMallaCurricularParaleloDto) {
		this.rpfListMallaCurricularParaleloDto = rpfListMallaCurricularParaleloDto;
	}
	public List<MallaCurricularDto> getRpfListMallaCurricularDtoAgregar() {
		rpfListMallaCurricularDtoAgregar = rpfListMallaCurricularDtoAgregar==null?(new ArrayList<MallaCurricularDto>()):rpfListMallaCurricularDtoAgregar;
		return rpfListMallaCurricularDtoAgregar;
	}
	public void setRpfListMallaCurricularDtoAgregar(List<MallaCurricularDto> rpfListMallaCurricularDtoAgregar) {
		this.rpfListMallaCurricularDtoAgregar = rpfListMallaCurricularDtoAgregar;
	}
	public MallaCurricularParaleloDto getRpfParaleloEditarCupo() {
		return rpfParaleloEditarCupo;
	}
	public void setRpfParaleloEditarCupo(MallaCurricularParaleloDto rpfParaleloEditarCupo) {
		this.rpfParaleloEditarCupo = rpfParaleloEditarCupo;
	}
	public Integer getRpfNuevoCupo() {
		return rpfNuevoCupo;
	}
	public void setRpfNuevoCupo(Integer rpfNuevoCupo) {
		this.rpfNuevoCupo = rpfNuevoCupo;
	}
	public Integer getRpfValidadorClick() {
		return rpfValidadorClick;
	}
	public void setRpfValidadorClick(Integer rpfValidadorClick) {
		this.rpfValidadorClick = rpfValidadorClick;
	}
	public Integer getRpfValidadorEdicion() {
		return rpfValidadorEdicion;
	}
	public void setRpfValidadorEdicion(Integer rpfValidadorEdicion) {
		this.rpfValidadorEdicion = rpfValidadorEdicion;
	}
	public boolean isRpfHabilitadorNivel() {
		return rpfHabilitadorNivel;
	}
	public void setRpfHabilitadorNivel(boolean rpfHabilitadorNivel) {
		this.rpfHabilitadorNivel = rpfHabilitadorNivel;
	}
	public boolean isRpfHabilitadorSeleccion() {
		return rpfHabilitadorSeleccion;
	}
	public void setRpfHabilitadorSeleccion(boolean rpfHabilitadorSeleccion) {
		this.rpfHabilitadorSeleccion = rpfHabilitadorSeleccion;
	}
	public boolean isRpfHabilitadorGuardar() {
		return rpfHabilitadorGuardar;
	}
	public void setRpfHabilitadorGuardar(boolean rpfHabilitadorGuardar) {
		this.rpfHabilitadorGuardar = rpfHabilitadorGuardar;
	}
	public boolean isRpfHabilitadorGuardarMaterias() {
		return rpfHabilitadorGuardarMaterias;
	}
	public void setRpfHabilitadorGuardarMaterias(boolean rpfHabilitadorGuardarMaterias) {
		this.rpfHabilitadorGuardarMaterias = rpfHabilitadorGuardarMaterias;
	}
	public Integer getRpfSeleccionarTodos() {
		return rpfSeleccionarTodos;
	}
	public void setRpfSeleccionarTodos(Integer rpfSeleccionarTodos) {
		this.rpfSeleccionarTodos = rpfSeleccionarTodos;
	}
	public String getRpfTituloModal() {
		return rpfTituloModal;
	}
	public void setRpfTituloModal(String rpfTituloModal) {
		this.rpfTituloModal = rpfTituloModal;
	}
	public String getRpfMensajeModal() {
		return rpfMensajeModal;
	}
	public void setRpfMensajeModal(String rpfMensajeModal) {
		this.rpfMensajeModal = rpfMensajeModal;
	}
	public String getRpfMensajeActivacion() {
		return rpfMensajeActivacion;
	}
	public void setRpfMensajeActivacion(String rpfMensajeActivacion) {
		this.rpfMensajeActivacion = rpfMensajeActivacion;
	}
	public Integer getRpfOpcionSeleccionada() {
		return rpfOpcionSeleccionada;
	}
	public void setRpfOpcionSeleccionada(Integer rpfOpcionSeleccionada) {
		this.rpfOpcionSeleccionada = rpfOpcionSeleccionada;
	}
	public boolean isRpfHabilitarPeriodo() {
		return rpfHabilitarPeriodo;
	}
	public void setRpfHabilitarPeriodo(boolean rpfHabilitarPeriodo) {
		this.rpfHabilitarPeriodo = rpfHabilitarPeriodo;
	}
	public ParaleloDto getRpfParaleloDtoEditar() {
		return rpfParaleloDtoEditar;
	}
	public void setRpfParaleloDtoEditar(ParaleloDto rpfParaleloDtoEditar) {
		this.rpfParaleloDtoEditar = rpfParaleloDtoEditar;
	}
	public CarreraDto getRpfCarreraDtoEditar() {
		return rpfCarreraDtoEditar;
	}
	public void setRpfCarreraDtoEditar(CarreraDto rpfCarreraDtoEditar) {
		this.rpfCarreraDtoEditar = rpfCarreraDtoEditar;
	}
	public PeriodoAcademico getRpfPeriodoAcademicoEditar() {
		return rpfPeriodoAcademicoEditar;
	}
	public void setRpfPeriodoAcademicoEditar(PeriodoAcademico rpfPeriodoAcademicoEditar) {
		this.rpfPeriodoAcademicoEditar = rpfPeriodoAcademicoEditar;
	}
	public Carrera getRpfCarreraEditar() {
		return rpfCarreraEditar;
	}
	public void setRpfCarreraEditar(Carrera rpfCarreraEditar) {
		this.rpfCarreraEditar = rpfCarreraEditar;
	}
	public Paralelo getRpfParalelo() {
		return rpfParalelo;
	}
	public void setRpfParalelo(Paralelo rpfParalelo) {
		this.rpfParalelo = rpfParalelo;
	}
	public List<HorarioAcademicoDto> getRpfListHorarioAcademicoBusq() {
		rpfListHorarioAcademicoBusq = rpfListHorarioAcademicoBusq==null?(new ArrayList<HorarioAcademicoDto>()):rpfListHorarioAcademicoBusq;
		return rpfListHorarioAcademicoBusq;
	}
	public void setRpfListHorarioAcademicoBusq(List<HorarioAcademicoDto> rpfListHorarioAcademicoBusq) {
		this.rpfListHorarioAcademicoBusq = rpfListHorarioAcademicoBusq;
	}
	public List<EstudianteJdbcDto> getRpfListaEstudiantes() {
		return rpfListaEstudiantes;
	}
	public void setRpfListaEstudiantes(List<EstudianteJdbcDto> rpfListaEstudiantes) {
		this.rpfListaEstudiantes = rpfListaEstudiantes;
	}
	public boolean isRpfHabilitarExportar() {
		return rpfHabilitarExportar;
	}
	public void setRpfHabilitarExportar(boolean rpfHabilitarExportar) {
		this.rpfHabilitarExportar = rpfHabilitarExportar;
	}
	public Integer getRpfHabilitarBoton() {
		return rpfHabilitarBoton;
	}
	public void setRpfHabilitarBoton(Integer rpfHabilitarBoton) {
		this.rpfHabilitarBoton = rpfHabilitarBoton;
	}
}