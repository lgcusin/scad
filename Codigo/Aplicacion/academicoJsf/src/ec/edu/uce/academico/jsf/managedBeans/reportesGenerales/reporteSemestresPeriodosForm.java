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
package ec.edu.uce.academico.jsf.managedBeans.reportesGenerales;

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
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.ReportesGeneralesDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReporteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import sun.util.locale.provider.AuxLocaleProviderAdapter;


/**
 * Clase (managed bean) ReporteParaleloForm
 * Managed Bean que maneja las peticiones del reporte de paralelo.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="reporteSemestresPeriodosForm")
@SessionScoped
public class reporteSemestresPeriodosForm implements Serializable{

	private static final long serialVersionUID = -7458138879145808577L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	//OBJETOS
	private Usuario rspfUsuario;
	private PeriodoAcademico rspfPeriodoAcademico;
	private DependenciaDto rspfDependenciaDto;
	private Carrera rspfCarrera;
	private CarreraDto rspfCarreraDto;
	private NivelDto rspfNivelDto;
	private String rspfTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer rspfTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer rspfDpnId; //diferenciar el tipo de usuario que ingreso - por carreras}
	private String rspfIdentificacion; 
	private Integer rspfEstado;
	private List<ReportesGeneralesDto> rspfListHistorialEstudiantes;
	
	
	
	//LISTA DE OBJETOS
	private List<PeriodoAcademico> rspfListPeriodoAcademico;
	private List<Dependencia> rspfListDependencia;
	private List<CarreraDto> rspfListCarreraDto;
	private List<Nivel> rspfListNivel;
	private List<Carrera> rspfListCarrera;
	private List<Carrera> rspfListCarreraSAU;
	private Carrera rspfCarreraSAU;
	private Integer rspfFacultadSAU;
	//AUXILIARES		
	private Integer rspfSeleccionarTodos;
	private String rspfTituloModal;
	private String rspfMensajeModal;
	private String rspfMensajeActivacion;
	private Integer rspfOpcionSeleccionada;
	private boolean rspfHabilitarPeriodo;
	private boolean rspfHabilitarExportar;

	private Integer rspfValidadorClick;
	private Integer rspfValidadorEdicion;
	private boolean rspfHabilitadorNivel;
	private boolean rspfHabilitadorSeleccion;
	private boolean rspfHabilitadorGuardar;
	
	//PARA HABILITAR
	private Integer rpfHabilitarBoton;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
		
	// OBJETOS PARA EDITAR Y CREAR NUEVO
		private ParaleloDto rspfParaleloDtoEditar;
		private CarreraDto rspfCarreraDtoEditar;
		private PeriodoAcademico rspfPeriodoAcademicoEditar;
		private Carrera rspfCarreraEditar;
		private Paralelo rspfParalelo;
	
	
	@PostConstruct
	public void inicializar(){
		
	}
	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB 
	PeriodoAcademicoServicio servRpfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servRpfCarreraDto;
	@EJB
	CarreraServicio servRpfCarrera;
	@EJB
	NivelServicio servNivelDtoServicio;
	@EJB
	private UsuarioRolServicio servRpfUsuarioRolServicio;
	@EJB
	PeriodoAcademicoDtoServicioJdbc servRpfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	DependenciaServicio servRpfDependenciaServicio;
	@EJB
	EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	@EJB
	private ReporteDtoServicioJdbc servReporteDtoServicioJdbc;
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/

	
//	/**
//	 * Método que permite iniciar los reportes de matriculados en pregrado
//	 * @param usuario - el usuario que ingresa 
//	  * @return  Navegacion a la pagina xhtml de Administración .
//	 */
//	
//	public String irReporteMatriculados(Usuario usuario) { 
//		rmnfUsuario= usuario;
//		try {
//			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
//			for (UsuarioRol item : usro) {
//				if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()|| item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
//					if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
//						rmnfTipoUsuario = RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue();
//					}
//					if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
//						rmnfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
//					}
//					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
//						rmnfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
//					}
//					rmnfTipoCarrera="carreras";
//					rmnfHabilitarPeriodo=true;
//				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
//					rmnfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
//					rmnfTipoCarrera="programas";
//					rmnfHabilitarPeriodo=false;
//				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
//					rmnfTipoUsuario = RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue();
//					rmnfTipoCarrera="programas";
//					rmnfHabilitarPeriodo=false;
//				}
//			}
//			rmnfPeriodoAcademico = new PeriodoAcademico();
//			rmnfPeriodoAcademico = servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
//			FacesUtil.mensajeError(e.getMessage());	
//			iniciarParametrosReporteMatriculados();
//		} catch (PeriodoAcademicoNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());	
//			iniciarParametrosReporteMatriculados();
//		} catch (PeriodoAcademicoException e) {
//			FacesUtil.mensajeError(e.getMessage());	
//			iniciarParametrosReporteMatriculados();
//		}
//		iniciarParametrosReporteMatriculados();
//		return "irReporteMatriculadoNivel";
//	}
	
	
	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración .
	 */
	
	public String irReporteSemestresPeriodos(Usuario usuario) { 
		rspfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
						rspfTipoUsuario = RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue();
					}
					rspfTipoCarrera="carrrtmfTipoUsuarioeras";
					rspfHabilitarPeriodo=false;
				}
			}
			rspfPeriodoAcademico = new PeriodoAcademico();
			rspfPeriodoAcademico = servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			llenarDependencia();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteMatriculados();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteMatriculados();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteMatriculados();
		}
		iniciarParametrosReporteMatriculados();
		return "irReporteSemestresPeriodos";
	}
	
	
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	
	public String irInicioReporte() {
		limpiarReporte();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados
	 * @return 
	 */
	public void limpiar()  {
		iniciarParametrosReporteMatriculados();
	}
	
	public void limpiarReporte()  {
		iniciarParametrosReporteMatriculados();
	}
	
	

	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametrosReporteMatriculados() {
		try {
			rspfHabilitarExportar=true;
			
			//inicio listas
			rspfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			rspfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			rspfListDependencia= new ArrayList<Dependencia>(); //inicio la lista de PeriodoAcademico
			rspfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			rspfListCarrera= new ArrayList<Carrera>(); //inicio la lista de Carreras Dto
			rspfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel
			
			//inicio objetos
			
			rspfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			rspfDependenciaDto = new DependenciaDto();
			rspfCarreraDto= new CarreraDto();    //Instancio el objeto carrera
			rspfCarrera= new Carrera();    //Instancio el objeto carrera
			rspfNivelDto = new NivelDto();
			rspfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
			rspfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

			
//			llenarPeriodos(); //Listar todos los peridos academicos
            llenarDependencia(); 
            //llenarCarreras();   //Listar  todos las carreras por el  usuario
			//llenarNiveles();
            rspfPeriodoAcademico = new PeriodoAcademico();
            llenarNiveles();
            rspfListCarreraSAU= new ArrayList<Carrera>(); //inicio la lista de Carreras Dto
            rspfListCarreraSAU=servRpfCarrera.listarCarrerasXFacultad(rspfListDependencia.get(0).getDpnId());
            Integer codigo_carrera_SAU = null;
            lazo:while(true) {
            	for (Carrera item : rspfListCarreraSAU) {
                	try {
                		codigo_carrera_SAU  = servReporteDtoServicioJdbc.buscarCrrIdporEspeCodigoSAU(item.getCrrEspeCodigo());
                		rspfFacultadSAU=servReporteDtoServicioJdbc.buscarDpnIdporCarreraidSAU(codigo_carrera_SAU);
                		break lazo;
    				} catch (Exception e) {
    				}
    			}	
            }
            
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Metodo para buscar carreras SAU */
	
	public void llenarCarrerasSAU(){
		try {
			rspfCarreraSAU=servRpfCarrera.buscarPorId(rspfCarreraDto.getCrrId());
			rspfCarreraSAU.setCrrId(servReporteDtoServicioJdbc.buscarCrrIdporEspeCodigoSAU(rspfCarreraSAU.getCrrEspeCodigo()));
			llenarPeriodos();
		} catch (Exception e) {
			rspfCarreraSAU.setCrrId(GeneralesConstantes.APP_ID_BASE);
		}

		
		
	}
	
	
	/**
	 * Método para llenar la lista de periodos 
	 */
	
	public void llenarPeriodos(){
		rspfListPeriodoAcademico= null;
		if(rspfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()|| rspfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rspfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
			rspfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos
		}else if (rspfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
				|| rspfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
			rspfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos	
			
		}
		
		if(rspfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
			List<PeriodoAcademico> listaPrac = new ArrayList<PeriodoAcademico>();
			listaPrac.addAll(rspfListPeriodoAcademico);
			rspfListPeriodoAcademico = new ArrayList<PeriodoAcademico>();
			for (int i = 0; i < listaPrac.size(); i++) {
				rspfListPeriodoAcademico.add(listaPrac.get(i));
				rspfListPeriodoAcademico.get(0).setPracId(listaPrac.get(i).getPracId()+10000);
			}
		}
		
		List<PeriodoAcademicoDto> aux = new ArrayList<PeriodoAcademicoDto>();
		try {
			aux = servRpfPeriodoAcademicoDtoServicioJdbc.listarTodosSAUxEscCodigoxEspeCodigo(rspfCarreraSAU.getCrrId(),rspfCarreraSAU.getCrrEspeCodigo());
			List<PeriodoAcademico> auxLista = new ArrayList<PeriodoAcademico>();
			for (PeriodoAcademicoDto item : aux) {
				PeriodoAcademico prac = new PeriodoAcademico();
				prac.setPracEstado(item.getPracEstado());
				prac.setPracId(item.getPracId());
				prac.setPracFechaFin(item.getPracFechaFin());
				prac.setPracFechaIncio(item.getPracFechaIncio());
				prac.setPracDescripcion(item.getPracDescripcion());
				prac.setPracTipo(item.getPracTipo());
				auxLista.add(prac);
			}
			if(auxLista.size()!=0) {
				rspfListPeriodoAcademico.addAll(auxLista);
			}
		} catch (PeriodoAcademicoDtoJdbcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
//	/**
//	 * Método para llenar la lista de dependencias  
//	 */
//
//	public void llenarDependencia(){
//		rmnfListDependencia= null;
//		if(rmnfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
//				try {
//					rmnfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(rmnfUsuario.getUsrId());
//				} catch (DependenciaNoEncontradoException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			// llamo al servicio de Dependecia para listar todos
//		}else if (rmnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
//				|| rmnfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){			
//			try {
//				rmnfListDependencia = servRpfDependenciaServicio.listarTodos();
//			} catch (DependenciaNoEncontradoException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	public void llenarDependencia(){
		rspfListDependencia= null;
		if(rspfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
				try {
					rspfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(rspfUsuario.getUsrId());
				} catch (DependenciaNoEncontradoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// llamo al servicio de Dependecia para listar todos
		}
	}
	

	
	/**
	 * Método para llenar la lista de Carreras por Usuario,  y Periodo activo
	 *
	 */
	public void llenarCarreras(){
		
		rspfListCarreraDto= null;
		rspfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
		try {
			rspfListCarreraDto=servRpfCarreraDto.listarXfacultades(rspfListDependencia);
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteMatriculados();
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen carreras para la dependencia seleccionada");
			iniciarParametrosReporteMatriculados();	
		}
	}
		
	 /**
	 * Método para llenar la lista de niveles 
	 */
	
	public void llenarNiveles(){
		rspfListNivel= new ArrayList<Nivel>();
		try {
			if(rspfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
				int contador = 0;
				for (CarreraDto itemCarr : rspfListCarreraDto) {
					if(itemCarr.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						contador = contador + 1;
					}
				}
				if(contador >= 1){
					rspfListNivel = servNivelDtoServicio.listarNivelacion();
				}else{
					rspfListNivel= servNivelDtoServicio.listarTodos();
				}
			}else if (rspfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
					|| rspfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				rspfListNivel= servNivelDtoServicio.listarTodosPosgrado();			
			}
		} catch (NivelNoEncontradoException e) {
		} catch (NivelException e) {
		}
	}
	
	
	public void verificarClickNo(){
		rspfValidadorClick  = 0;
	}


	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir(){
		//			try {
		if(rspfListHistorialEstudiantes.size() > 0){
			generarReporteNiveles(rspfListHistorialEstudiantes);
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
		if(rspfListHistorialEstudiantes.size() > 0){
			generarReporteNiveles(rspfListHistorialEstudiantes);
			habilitaModalImpresion2();
		}else{
			bloqueaModal();
		}
		rpfHabilitarBoton = 0;

	}


	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		rspfValidadorClick = 0;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		rspfValidadorClick = 1;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion2(){
		rspfValidadorClick = 2;
	}

	
	/**
	 * verifica que haga click en el boton buscar 
	 */
	public void buscarHistorialEstudiante(){
		try {
			if (rspfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar el periodo académico");
			}else if (rspfCarreraDto.getCrrId() == GeneralesConstantes.APP_ID_BASE) {
						FacesUtil.mensajeError("Debe seleccionar la carrera");
			}else {
				
				
				if(rspfPeriodoAcademico.getPracId()>10000) {
					System.out.println(rspfPeriodoAcademico.getPracId()-10000);
				
					rspfListHistorialEstudiantes=servReporteDtoServicioJdbc.buscarRecordEstudianteSIIU(rspfDpnId, rspfCarreraDto.getCrrId(), rspfPeriodoAcademico.getPracId(), rspfIdentificacion);
				}else {
					try {
						rspfListHistorialEstudiantes=servReporteDtoServicioJdbc.buscarRecordEstudianteSAU(rspfFacultadSAU, rspfCarreraSAU.getCrrEspeCodigo(), rspfPeriodoAcademico.getPracId(), rspfIdentificacion, rspfNivelDto.getNvlId(), rspfEstado);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
			rspfHabilitarExportar=false;
		} catch (Exception e) {
			e.printStackTrace();
			rspfHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
		}
	
		
	}	
	

	/**
	 * Método que genera el reporte
	 */
	public void generarReporteNiveles(List<ReportesGeneralesDto> listaMatriculaNiveles){
		try {

			int totalInscritos = 0;
			int totalMatriculados = 0;
			String facultad = null;
			for (ReportesGeneralesDto item : listaMatriculaNiveles) {
				//totalInscritos = totalInscritos + item.getMlcrprInscritos().intValue();
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
			frmRrmNombreReporte = "reporteTotalMatriculados";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			PeriodoAcademico periodoAcademico = new PeriodoAcademico();
			periodoAcademico = servRpfPeriodoAcademico.buscarPorId(rspfPeriodoAcademico.getPracId());

			frmRrmParametros.put("periodo", periodoAcademico.getPracDescripcion());
			//frmRrmParametros.put("facultad", facultad);
			frmRrmParametros.put("carrera", rspfCarrera.getCrrDescripcion());
			frmRrmParametros.put("total_matriculados", String.valueOf(totalMatriculados));


			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoHorario = null;


			for (ReportesGeneralesDto item : listaMatriculaNiveles) {
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
			pathGeneralReportes.append("/academico/reportesGenerales/archivosJasper");
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
//	public static void generarReporteEstados(List<EstudianteJdbcDto> listaEstados){
//		List<Map<String, Object>> frmCrpCampos = null;
//		Map<String, Object> frmCrpParametros = null;
//		String frmCrpNombreReporte = null;
//		// ****************************************************************//
//		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
//		// ****************************************************************//
//		// ****************************************************************//
//		frmCrpNombreReporte = "ReporteMatriculados";
//		java.util.Date date= new java.util.Date();
//		frmCrpParametros = new HashMap<String, Object>();
//		String fecha = new Timestamp(date.getTime()).toString();
//		frmCrpParametros.put("fecha",fecha);
//
//		frmCrpCampos = new ArrayList<Map<String, Object>>();
//		Map<String, Object> dato = null;
//		for (EstudianteJdbcDto item : listaEstados) {
//			dato = new HashMap<String, Object>();
//			dato.put("convocatoria",item.getPracDescripcion());
//			dato.put("cedula", item.getPrsIdentificacion());
//			dato.put("apellido1", item.getPrsPrimerApellido());
//			dato.put("apellido2", item.getPrsSegundoApellido());
//			dato.put("nombres", item.getPrsNombres());
//			dato.put("facultad", item.getDpnDescripcion());
//			dato.put("carrera", item.getCrrDescripcion());
//			dato.put("email", item.getPrsMailInstitucional());
//			ListasCombosForm lcfAux = new ListasCombosForm();
//			dato.put("estado", lcfAux.getEstadoMatricula(item.getFcinMatriculado()));
//			frmCrpCampos.add(dato);
//		}
//		// Establecemos en el atributo de la sesión la lista de mapas de
//		// datos frmCrpCampos
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//		HttpSession httpSession = request.getSession(false);
//		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
//		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
//		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
//		// ******************FIN DE GENERACION DE REPORTE ************//	
//	} 

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/



	public Integer getRpfHabilitarBoton() {
		return rpfHabilitarBoton;
	}
	public Usuario getRspfUsuario() {
		return rspfUsuario;
	}



	public void setRspfUsuario(Usuario rspfUsuario) {
		this.rspfUsuario = rspfUsuario;
	}



	public PeriodoAcademico getRspfPeriodoAcademico() {
		return rspfPeriodoAcademico;
	}



	public void setRspfPeriodoAcademico(PeriodoAcademico rspfPeriodoAcademico) {
		this.rspfPeriodoAcademico = rspfPeriodoAcademico;
	}



	public DependenciaDto getRspfDependenciaDto() {
		return rspfDependenciaDto;
	}



	public void setRspfDependenciaDto(DependenciaDto rspfDependenciaDto) {
		this.rspfDependenciaDto = rspfDependenciaDto;
	}



	public Carrera getRspfCarrera() {
		return rspfCarrera;
	}



	public void setRspfCarrera(Carrera rspfCarrera) {
		this.rspfCarrera = rspfCarrera;
	}



	public CarreraDto getRspfCarreraDto() {
		return rspfCarreraDto;
	}



	public void setRspfCarreraDto(CarreraDto rspfCarreraDto) {
		this.rspfCarreraDto = rspfCarreraDto;
	}



	public NivelDto getRspfNivelDto() {
		return rspfNivelDto;
	}



	public void setRspfNivelDto(NivelDto rspfNivelDto) {
		this.rspfNivelDto = rspfNivelDto;
	}



	public String getRspfTipoCarrera() {
		return rspfTipoCarrera;
	}



	public void setRspfTipoCarrera(String rspfTipoCarrera) {
		this.rspfTipoCarrera = rspfTipoCarrera;
	}



	public Integer getRspfTipoUsuario() {
		return rspfTipoUsuario;
	}



	public void setRspfTipoUsuario(Integer rspfTipoUsuario) {
		this.rspfTipoUsuario = rspfTipoUsuario;
	}



	public Integer getRspfDpnId() {
		return rspfDpnId;
	}



	public void setRspfDpnId(Integer rspfDpnId) {
		this.rspfDpnId = rspfDpnId;
	}



	public List<ReportesGeneralesDto> getRspfListHistorialEstudiantes() {
		return rspfListHistorialEstudiantes;
	}



	public void setRspfListHistorialEstudiantes(List<ReportesGeneralesDto> rspfListHistorialEstudiantes) {
		this.rspfListHistorialEstudiantes = rspfListHistorialEstudiantes;
	}



	public List<PeriodoAcademico> getRspfListPeriodoAcademico() {
		return rspfListPeriodoAcademico;
	}



	public void setRspfListPeriodoAcademico(List<PeriodoAcademico> rspfListPeriodoAcademico) {
		this.rspfListPeriodoAcademico = rspfListPeriodoAcademico;
	}



	public List<Dependencia> getRspfListDependencia() {
		return rspfListDependencia;
	}



	public void setRspfListDependencia(List<Dependencia> rspfListDependencia) {
		this.rspfListDependencia = rspfListDependencia;
	}



	public List<CarreraDto> getRspfListCarreraDto() {
		return rspfListCarreraDto;
	}



	public void setRspfListCarreraDto(List<CarreraDto> rspfListCarreraDto) {
		this.rspfListCarreraDto = rspfListCarreraDto;
	}



	public List<Nivel> getRspfListNivel() {
		return rspfListNivel;
	}



	public void setRspfListNivel(List<Nivel> rspfListNivel) {
		this.rspfListNivel = rspfListNivel;
	}



	public List<Carrera> getRspfListCarrera() {
		return rspfListCarrera;
	}



	public void setRspfListCarrera(List<Carrera> rspfListCarrera) {
		this.rspfListCarrera = rspfListCarrera;
	}



	public Integer getRspfSeleccionarTodos() {
		return rspfSeleccionarTodos;
	}



	public void setRspfSeleccionarTodos(Integer rspfSeleccionarTodos) {
		this.rspfSeleccionarTodos = rspfSeleccionarTodos;
	}



	public String getRspfTituloModal() {
		return rspfTituloModal;
	}



	public void setRspfTituloModal(String rspfTituloModal) {
		this.rspfTituloModal = rspfTituloModal;
	}



	public String getRspfMensajeModal() {
		return rspfMensajeModal;
	}



	public void setRspfMensajeModal(String rspfMensajeModal) {
		this.rspfMensajeModal = rspfMensajeModal;
	}



	public String getRspfMensajeActivacion() {
		return rspfMensajeActivacion;
	}



	public void setRspfMensajeActivacion(String rspfMensajeActivacion) {
		this.rspfMensajeActivacion = rspfMensajeActivacion;
	}



	public Integer getRspfOpcionSeleccionada() {
		return rspfOpcionSeleccionada;
	}



	public void setRspfOpcionSeleccionada(Integer rspfOpcionSeleccionada) {
		this.rspfOpcionSeleccionada = rspfOpcionSeleccionada;
	}



	public boolean isRspfHabilitarPeriodo() {
		return rspfHabilitarPeriodo;
	}



	public void setRspfHabilitarPeriodo(boolean rspfHabilitarPeriodo) {
		this.rspfHabilitarPeriodo = rspfHabilitarPeriodo;
	}



	public boolean isRspfHabilitarExportar() {
		return rspfHabilitarExportar;
	}



	public void setRspfHabilitarExportar(boolean rspfHabilitarExportar) {
		this.rspfHabilitarExportar = rspfHabilitarExportar;
	}



	public Integer getRspfValidadorClick() {
		return rspfValidadorClick;
	}



	public void setRspfValidadorClick(Integer rspfValidadorClick) {
		this.rspfValidadorClick = rspfValidadorClick;
	}



	public Integer getRspfValidadorEdicion() {
		return rspfValidadorEdicion;
	}



	public void setRspfValidadorEdicion(Integer rspfValidadorEdicion) {
		this.rspfValidadorEdicion = rspfValidadorEdicion;
	}



	public boolean isRspfHabilitadorNivel() {
		return rspfHabilitadorNivel;
	}



	public void setRspfHabilitadorNivel(boolean rspfHabilitadorNivel) {
		this.rspfHabilitadorNivel = rspfHabilitadorNivel;
	}



	public boolean isRspfHabilitadorSeleccion() {
		return rspfHabilitadorSeleccion;
	}



	public void setRspfHabilitadorSeleccion(boolean rspfHabilitadorSeleccion) {
		this.rspfHabilitadorSeleccion = rspfHabilitadorSeleccion;
	}



	public boolean isRspfHabilitadorGuardar() {
		return rspfHabilitadorGuardar;
	}



	public void setRspfHabilitadorGuardar(boolean rspfHabilitadorGuardar) {
		this.rspfHabilitadorGuardar = rspfHabilitadorGuardar;
	}



	public ParaleloDto getRspfParaleloDtoEditar() {
		return rspfParaleloDtoEditar;
	}



	public void setRspfParaleloDtoEditar(ParaleloDto rspfParaleloDtoEditar) {
		this.rspfParaleloDtoEditar = rspfParaleloDtoEditar;
	}



	public CarreraDto getRspfCarreraDtoEditar() {
		return rspfCarreraDtoEditar;
	}



	public void setRspfCarreraDtoEditar(CarreraDto rspfCarreraDtoEditar) {
		this.rspfCarreraDtoEditar = rspfCarreraDtoEditar;
	}



	public PeriodoAcademico getRspfPeriodoAcademicoEditar() {
		return rspfPeriodoAcademicoEditar;
	}



	public void setRspfPeriodoAcademicoEditar(PeriodoAcademico rspfPeriodoAcademicoEditar) {
		this.rspfPeriodoAcademicoEditar = rspfPeriodoAcademicoEditar;
	}



	public Carrera getRspfCarreraEditar() {
		return rspfCarreraEditar;
	}



	public void setRspfCarreraEditar(Carrera rspfCarreraEditar) {
		this.rspfCarreraEditar = rspfCarreraEditar;
	}



	public Paralelo getRspfParalelo() {
		return rspfParalelo;
	}



	public void setRspfParalelo(Paralelo rspfParalelo) {
		this.rspfParalelo = rspfParalelo;
	}



	public void setRpfHabilitarBoton(Integer rpfHabilitarBoton) {
		this.rpfHabilitarBoton = rpfHabilitarBoton;
	}



	public String getRspfIdentificacion() {
		return rspfIdentificacion;
	}



	public void setRspfIdentificacion(String rspfIdentificacion) {
		this.rspfIdentificacion = rspfIdentificacion;
	}



	public Integer getRspfEstado() {
		return rspfEstado;
	}



	public void setRspfEstado(Integer rspfEstado) {
		this.rspfEstado = rspfEstado;
	}



	public List<Carrera> getRspfListCarreraSAU() {
		return rspfListCarreraSAU;
	}



	public void setRspfListCarreraSAU(List<Carrera> rspfListCarreraSAU) {
		this.rspfListCarreraSAU = rspfListCarreraSAU;
	}



	public Integer getRspfFacultadSAU() {
		return rspfFacultadSAU;
	}



	public void setRspfFacultadSAU(Integer rspfFacultadSAU) {
		this.rspfFacultadSAU = rspfFacultadSAU;
	}





	
	
	
	
}