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


/**
 * Clase (managed bean) ReporteParaleloForm
 * Managed Bean que maneja las peticiones del reporte de paralelo.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="reporteMatriculaNivelesForm")
@SessionScoped
public class reporteMatriculaNivelesForm implements Serializable{

	private static final long serialVersionUID = -7458138879145808577L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	//OBJETOS
	private Usuario rmnfUsuario;
	private PeriodoAcademico rmnfPeriodoAcademico;
	private DependenciaDto rmnfDependenciaDto;
	private Carrera rmnfCarrera;
	private CarreraDto rmnfCarreraDto;
	private NivelDto rmnfNivelDto;
	private String rmnfTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer rmnfTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer rmnfDpnId; //diferenciar el tipo de usuario que ingreso - por carreras
	private List<ReportesGeneralesDto> rmnfListMatriculasNivelesBusq;
	

	
	
	//LISTA DE OBJETOS
	private List<PeriodoAcademico> rmnfListPeriodoAcademico;
	private List<Dependencia> rmnfListDependencia;
	private List<CarreraDto> rmnfListCarreraDto;
	private List<Nivel> rmnfListNivel;
	private List<Carrera> rmnfListCarrera;

	//AUXILIARES		
	private Integer rmnfSeleccionarTodos;
	private String rmnfTituloModal;
	private String rmnfMensajeModal;
	private String rmnfMensajeActivacion;
	private Integer rmnfOpcionSeleccionada;
	private boolean rmnfHabilitarPeriodo;
	private boolean rmnfHabilitarExportar;

	private Integer rmnfValidadorClick;
	private Integer rmnfValidadorEdicion;
	private boolean rmnfHabilitadorNivel;
	private boolean rmnfHabilitadorSeleccion;
	private boolean rmnfHabilitadorGuardar;
	
	//PARA HABILITAR
	private Integer rpfHabilitarBoton;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
		
	// OBJETOS PARA EDITAR Y CREAR NUEVO
		private ParaleloDto rmnfParaleloDtoEditar;
		private CarreraDto rmnfCarreraDtoEditar;
		private PeriodoAcademico rmnfPeriodoAcademicoEditar;
		private Carrera rmnfCarreraEditar;
		private Paralelo rmnfParalelo;
	
	
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
	PeriodoAcademicoServicio servRpfPeriodoAcademicoServicio;
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
	
	public String irReporteMatriculadosNiveles(Usuario usuario) { 
		rmnfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
						rmnfTipoUsuario = RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue();
					}
					rmnfTipoCarrera="carrrtmfTipoUsuarioeras";
					rmnfHabilitarPeriodo=false;
				}
			}
			rmnfPeriodoAcademico = new PeriodoAcademico();
			rmnfPeriodoAcademico = servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
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
		return "irReporteMatriculadosNiveles";
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
			rmnfHabilitarExportar=true;
			
			//inicio listas
			rmnfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			rmnfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			rmnfListDependencia= new ArrayList<Dependencia>(); //inicio la lista de PeriodoAcademico
			rmnfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			rmnfListCarrera= new ArrayList<Carrera>(); //inicio la lista de Carreras Dto
			rmnfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel
			
			//inicio objetos
			
			rmnfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			rmnfDependenciaDto = new DependenciaDto();
			rmnfCarreraDto= new CarreraDto();    //Instancio el objeto carrera
			rmnfCarrera= new Carrera();    //Instancio el objeto carrera
			rmnfNivelDto = new NivelDto();
			rmnfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
			rmnfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

			
			llenarPeriodos(); //Listar todos los peridos academicos
            llenarDependencia(); 
            //llenarCarreras();   //Listar  todos las carreras por el  usuario
			//llenarNiveles();
            rmnfPeriodoAcademico = new PeriodoAcademico();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método para llenar la lista de periodos 
	 */

	public void llenarPeriodos(){
		rmnfListPeriodoAcademico= null;
		if(rmnfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()|| rmnfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rmnfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
			rmnfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos
		}else if (rmnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
				|| rmnfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
			rmnfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos			
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
		rmnfListDependencia= null;
		if(rmnfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
				try {
					rmnfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(rmnfUsuario.getUsrId());
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
		
		rmnfListCarreraDto= null;
		rmnfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
		try {
			rmnfListCarreraDto=servRpfCarreraDto.listarXfacultades(rmnfListDependencia);
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
		rmnfListNivel= new ArrayList<Nivel>();
		try {
			if(rmnfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()|| rmnfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rmnfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				int contador = 0;
				for (CarreraDto itemCarr : rmnfListCarreraDto) {
					if(itemCarr.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						contador = contador + 1;
					}
				}
				if(contador >= 1){
					rmnfListNivel = servNivelDtoServicio.listarNivelacion();
				}else{
					rmnfListNivel= servNivelDtoServicio.listarTodos();
				}
			}else if (rmnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()
					|| rmnfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				rmnfListNivel= servNivelDtoServicio.listarTodosPosgrado();			
			}
		} catch (NivelNoEncontradoException e) {
		} catch (NivelException e) {
		}
	}
	
	
	public void verificarClickNo(){
		rmnfValidadorClick  = 0;
	}


	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir(){
		//			try {
		if(rmnfListMatriculasNivelesBusq.size() > 0){
			generarReporteNiveles(rmnfListMatriculasNivelesBusq);
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
		if(rmnfListMatriculasNivelesBusq.size() > 0){
			generarReporteNiveles(rmnfListMatriculasNivelesBusq);
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
		rmnfValidadorClick = 0;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		rmnfValidadorClick = 1;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion2(){
		rmnfValidadorClick = 2;
	}

	
	/**
	 * verifica que haga click en el boton buscar 
	 */
	public void buscarTotalMatriculados(){
		try {
			if (rmnfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar el periodo académico");
			}else if (rmnfCarreraDto.getCrrId() == GeneralesConstantes.APP_ID_BASE) {
						FacesUtil.mensajeError("Debe seleccionar la carrera");
			}else if (rmnfNivelDto.getNvlId() == GeneralesConstantes.APP_ID_BASE){
						FacesUtil.mensajeError("Debe seleccionar la nivel");
			}else {
				rmnfListMatriculasNivelesBusq=servReporteDtoServicioJdbc.buscarTotalMatriculadosxCarreraXNivel(rmnfPeriodoAcademico.getPracId(),rmnfCarreraDto.getCrrId(), rmnfNivelDto.getNvlId(),rmnfListDependencia);

			}			
			System.out.println(rmnfPeriodoAcademico.getPracId());
			for (ReportesGeneralesDto item : rmnfListMatriculasNivelesBusq) {
				System.out.println(item.getCrrDescripcion());
				System.out.println(item.getRcesNumeroMatriculados());
				System.out.println(item.getNvlId());
			}
			rmnfHabilitarExportar=false;
		} catch (ReportesGeneralesDtoException e) {
			e.printStackTrace();
			rmnfHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ReportesGeneralesDtoNoEncontradoException e) {
			rmnfHabilitarExportar=true;
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
			periodoAcademico = servRpfPeriodoAcademicoServicio.buscarPorId(rmnfPeriodoAcademico.getPracId());

			frmRrmParametros.put("periodo", periodoAcademico.getPracDescripcion());
			//frmRrmParametros.put("facultad", facultad);
			frmRrmParametros.put("carrera", rmnfCarrera.getCrrDescripcion());
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

	public Usuario getRmnfUsuario() {
		return rmnfUsuario;
	}
	
	public void setRmnfUsuario(Usuario rmnfUsuario) {
		this.rmnfUsuario = rmnfUsuario;
	}

	public PeriodoAcademico getRmnfPeriodoAcademico() {
		return rmnfPeriodoAcademico;
	}

	public void setRmnfPeriodoAcademico(PeriodoAcademico rmnfPeriodoAcademico) {
		this.rmnfPeriodoAcademico = rmnfPeriodoAcademico;
	}

	
	public NivelDto getRmnfNivelDto() {
		return rmnfNivelDto;
	}


	public void setRmnfNivelDto(NivelDto rmnfNivelDto) {
		this.rmnfNivelDto = rmnfNivelDto;
	}

	public String getRmnfTipoCarrera() {
		return rmnfTipoCarrera;
	}

	public void setRmnfTipoCarrera(String rmnfTipoCarrera) {
		this.rmnfTipoCarrera = rmnfTipoCarrera;
	}

	public Integer getRpfTipoUsuario() {
		return rmnfTipoUsuario;
	}
	public void setRpfTipoUsuario(Integer rpfTipoUsuario) {
		this.rmnfTipoUsuario = rmnfTipoUsuario;
	}
	

	public CarreraDto getRmnfCarreraDto() {
		return rmnfCarreraDto;
	}


	public void setRmnfCarreraDto(CarreraDto rmnfCarreraDto) {
		this.rmnfCarreraDto = rmnfCarreraDto;
	}
	
	public List<PeriodoAcademico> getRmnfListPeriodoAcademico() {
		rmnfListPeriodoAcademico = rmnfListPeriodoAcademico == null? (new ArrayList<PeriodoAcademico>()):rmnfListPeriodoAcademico;
		return rmnfListPeriodoAcademico;
	}


	public void setRmnfListPeriodoAcademico(List<PeriodoAcademico> rmnfListPeriodoAcademico) {
		this.rmnfListPeriodoAcademico = rmnfListPeriodoAcademico;
	}


	public List<Dependencia> getRmnfListDependencia() {
		rmnfListDependencia = rmnfListDependencia == null? (new ArrayList<Dependencia>()):rmnfListDependencia;
		return rmnfListDependencia;
	}


	public void setRmnfListDependencia(List<Dependencia> rmnfListDependencia) {
		this.rmnfListDependencia = rmnfListDependencia;
	}


	public List<CarreraDto> getRmnfListCarreraDto() {
		rmnfListCarreraDto = rmnfListCarreraDto==null?(new ArrayList<CarreraDto>()):rmnfListCarreraDto;
		return rmnfListCarreraDto;
	}


	public void setRmnfListCarreraDto(List<CarreraDto> rmnfListCarreraDto) {
		this.rmnfListCarreraDto = rmnfListCarreraDto;
	}

	
	public List<Nivel> getRmnfListNivel() {
		rmnfListNivel = rmnfListNivel==null?(new ArrayList<Nivel>()):rmnfListNivel;
		return rmnfListNivel;
	}

	
	public List<ReportesGeneralesDto> getRmnfListMatriculasNivelesBusq() {
		return rmnfListMatriculasNivelesBusq;
	}


	public void setRmnfListMatriculasNivelesBusq(List<ReportesGeneralesDto> rmnfListMatriculasNivelesBusq) {
		this.rmnfListMatriculasNivelesBusq = rmnfListMatriculasNivelesBusq;
	}


	public void setRmnfListNivel(List<Nivel> rmnfListNivel) {
		this.rmnfListNivel = rmnfListNivel;
	}

	
	public boolean isRmnfHabilitadorNivel() {
		return rmnfHabilitadorNivel;
	}


	public void setRmnfHabilitadorNivel(boolean rmnfHabilitadorNivel) {
		this.rmnfHabilitadorNivel = rmnfHabilitadorNivel;
	}


	public boolean isRmnfHabilitadorSeleccion() {
		return rmnfHabilitadorSeleccion;
	}


	public void setRmnfHabilitadorSeleccion(boolean rmnfHabilitadorSeleccion) {
		this.rmnfHabilitadorSeleccion = rmnfHabilitadorSeleccion;
	}


	public boolean isRmnfHabilitadorGuardar() {
		return rmnfHabilitadorGuardar;
	}


	public void setRmnfHabilitadorGuardar(boolean rmnfHabilitadorGuardar) {
		this.rmnfHabilitadorGuardar = rmnfHabilitadorGuardar;
	}

	public Integer getRmnfSeleccionarTodos() {
		return rmnfSeleccionarTodos;
	}


	public void setRmnfSeleccionarTodos(Integer rmnfSeleccionarTodos) {
		this.rmnfSeleccionarTodos = rmnfSeleccionarTodos;
	}


	public String getRmnfTituloModal() {
		return rmnfTituloModal;
	}


	public void setRmnfTituloModal(String rmnfTituloModal) {
		this.rmnfTituloModal = rmnfTituloModal;
	}


	public String getRmnfMensajeModal() {
		return rmnfMensajeModal;
	}


	public void setRmnfMensajeModal(String rmnfMensajeModal) {
		this.rmnfMensajeModal = rmnfMensajeModal;
	}


	public String getRmnfMensajeActivacion() {
		return rmnfMensajeActivacion;
	}


	public void setRmnfMensajeActivacion(String rmnfMensajeActivacion) {
		this.rmnfMensajeActivacion = rmnfMensajeActivacion;
	}


	public Integer getRmnfOpcionSeleccionada() {
		return rmnfOpcionSeleccionada;
	}


	public void setRmnfOpcionSeleccionada(Integer rmnfOpcionSeleccionada) {
		this.rmnfOpcionSeleccionada = rmnfOpcionSeleccionada;
	}


	public boolean isRmnfHabilitarPeriodo() {
		return rmnfHabilitarPeriodo;
	}


	public void setRmnfHabilitarPeriodo(boolean rmnfHabilitarPeriodo) {
		this.rmnfHabilitarPeriodo = rmnfHabilitarPeriodo;
	}

	
	public CarreraDto getRmnfCarreraDtoEditar() {
		return rmnfCarreraDtoEditar;
	}


	public void setRmnfCarreraDtoEditar(CarreraDto rmnfCarreraDtoEditar) {
		this.rmnfCarreraDtoEditar = rmnfCarreraDtoEditar;
	}


	public Carrera getRmnfCarreraEditar() {
		return rmnfCarreraEditar;
	}


	public void setRmnfCarreraEditar(Carrera rmnfCarreraEditar) {
		this.rmnfCarreraEditar = rmnfCarreraEditar;
	}

	
	public boolean isRmnfHabilitarExportar() {
		return rmnfHabilitarExportar;
	}


	public void setRmnfHabilitarExportar(boolean rmnfHabilitarExportar) {
		this.rmnfHabilitarExportar = rmnfHabilitarExportar;
	}

	public Integer getRpfHabilitarBoton() {
		return rpfHabilitarBoton;
	}
	public void setRpfHabilitarBoton(Integer rpfHabilitarBoton) {
		this.rpfHabilitarBoton = rpfHabilitarBoton;
	}


	public Integer getRmnfDpnId() {
		return rmnfDpnId;
	}


	public void setRmnfDpnId(Integer rmnfDpnId) {
		this.rmnfDpnId = rmnfDpnId;
	}


	public PeriodoAcademico getRmnfPeriodoAcademicoEditar() {
		return rmnfPeriodoAcademicoEditar;
	}


	public void setRmnfPeriodoAcademicoEditar(PeriodoAcademico rmnfPeriodoAcademicoEditar) {
		this.rmnfPeriodoAcademicoEditar = rmnfPeriodoAcademicoEditar;
	}


	public DependenciaDto getRmnfDependenciaDto() {
		return rmnfDependenciaDto;
	}


	public void setRmnfDependenciaDto(DependenciaDto rmnfDependenciaDto) {
		this.rmnfDependenciaDto = rmnfDependenciaDto;
	}


	public List<Carrera> getRmnfListCarrera() {
		return rmnfListCarrera;
	}


	public void setRmnfListCarrera(List<Carrera> rmnfListCarrera) {
		this.rmnfListCarrera = rmnfListCarrera;
	}
	
	
	
	
}