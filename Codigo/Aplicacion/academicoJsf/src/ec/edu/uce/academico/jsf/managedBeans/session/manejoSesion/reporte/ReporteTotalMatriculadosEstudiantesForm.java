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

 ARCHIVO:     ReporteDocentesMateriaCarreraForm.java	  
 DESCRIPCION: Bean de sesion que maneja materias del docente
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 06-NOV-2018 			Fatima Tobar                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReporteDocentesMateriaCarreraForm.
 * Managed Bean de sesion que maneja materias del docente
 * @author fktobar.
 * @version 1.0
 */

@ManagedBean(name="reporteTotalMatriculadosEstudiantesForm")
@SessionScoped
public class ReporteTotalMatriculadosEstudiantesForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario tmefUsuario;
	private UsuarioDto tmefUsuarioDto; 

	//VARIABLES DE BUSQUEDA
	private PeriodoAcademico tmefPeriodoAcademico;
	private DependenciaDto tmefDependenciaDto;
	private Carrera tmefCarrera;
	private FichaMatricula tmefFichaMatricula; 


	private CarreraDto tmefCarreraDto;
	private NivelDto tmefNivelDto;

	private String tmefTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer tmefTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer tmefDpnId; //diferenciar el tipo de usuario que ingreso - por carreras
	private List<DocenteJdbcDto> tmefListDocentesMateriasBusq;


	
	private List<EstudianteJdbcDto> tmefListEstudiantesNivelBusq;


	//LISTA DE OBJETOS
	private List<PeriodoAcademico> tmefListPeriodoAcademico;
	private List<Dependencia> tmefListDependencia;
	private List<CarreraDto> tmefListCarreraDto;
	private List<NivelDto> tmefListNivelDto;
	private List<Nivel> tmefListNivel;
	private List<Carrera> tmefListCarrera;


	//AUXILIARES		
	private Integer tmefSeleccionarTodos;
	private String tmefTituloModal;
	private String tmefMensajeModal;
	private String tmefMensajeActivacion;
	private Integer tmefOpcionSeleccionada;
	private boolean tmefHabilitarPeriodo;
	private boolean tmefHabilitarExportar;

	private Integer tmefValidadorClick;
	private Integer tmefValidadorEdicion;
	private boolean tmefHabilitadorNivel;
	private boolean tmefHabilitadorSeleccion;
	private boolean tmefHabilitadorGuardar;

	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){

	}

	//********************************************************************/
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
	DependenciaServicio servRpfDependenciaServicio;
	@EJB
	EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	@EJB
	private ReporteDtoServicioJdbc servReporteDtoServicioJdbc;


	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * @param usuario - el usuario que ingresa 
	 * @return  Navegacion a la pagina xhtml de Administración .
	 */

	public String irReporteTotalEstudiantes(Usuario usuario) { 
		tmefUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					tmefTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					tmefTipoCarrera="carrrtmfTipoUsuarioeras";
					tmefHabilitarPeriodo=true;
				}
			}
			iniciarParametros();
			tmefListDependencia = new ArrayList<>();
			tmefListDependencia= servRpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		return "irReporteTotalEstudiantes";
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
		FacesUtil.limpiarMensaje();
		iniciarParametros();
	}

	public void limpiarReporte()  {
		iniciarParametrosReporteTotalMatriculados();
	}


	public void iniciarParametrosReporteTotalMatriculados(){
		try {
			tmefHabilitarExportar=true;
			tmefUsuarioDto = new UsuarioDto();
			//inicio listas
			tmefSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			tmefListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			tmefListDependencia= new ArrayList<Dependencia>(); //inicio la lista de PeriodoAcademico

			tmefListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			tmefListCarrera= new ArrayList<Carrera>(); //inicio la lista de Carreras 
			tmefListNivelDto= new ArrayList<NivelDto>(); //inicio la lista de Nivel

			//inicio objetos

			tmefPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			tmefDependenciaDto = new DependenciaDto();
			tmefCarreraDto= new CarreraDto();    //Instancio el objeto carrera DTO
			tmefCarrera= new Carrera();    //Instancio el objeto carrera
			//			dmfcPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
			tmefCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto


//			llenarPeriodos(); //Listar todos los peridos academicos
//			llenarDependencia(); 
//			llenarCarreras();   //Listar  todos las carreras por el  usuario
			tmefPeriodoAcademico = new PeriodoAcademico();
			tmefListDocentesMateriasBusq = null;
			tmefListEstudiantesNivelBusq = null; 

		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	public void iniciarParametros(){
		try {
			tmefHabilitarExportar=true;
			tmefUsuarioDto = new UsuarioDto();
			//inicio listas
			tmefSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			tmefListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			tmefListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			//inicio objetos
			tmefPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			tmefDependenciaDto = new DependenciaDto();
			tmefCarreraDto= new CarreraDto();    //Instancio el objeto carrera DTO
			tmefCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto
			tmefDependenciaDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
			tmefListDocentesMateriasBusq = null;
			tmefListEstudiantesNivelBusq = null;
			tmefNivelDto = new NivelDto();
			llenarPeriodos();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que instancia la lista de resultados cuando cambio de período
	 */
	public void cambiarPeriodo() {
		tmefListDocentesMateriasBusq = new ArrayList<>();
	}


	public void llenarPeriodos(){
		Integer tipoPeriodo = null;
		tmefListPeriodoAcademico= null;
		if(tmefTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
			if(tmefTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
				tipoPeriodo = PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE;
			}
			if(tipoPeriodo != null) {
				tmefListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos
			}else {
				FacesUtil.mensajeError("No se encontró períodos académicos");
			}
			
		}else {
			FacesUtil.mensajeError("No tiene permisos para acceder a la búsqueda");
		}
	}

	/**
	 * Método para llenar la lista Dependencias
	 */
	public void llenarDependencia(){
		tmefListDependencia= null;
		if(tmefTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
			try {
				//					System.out.println(dmfcUsuario.getUsrId());
				if(tmefDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
					tmefListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuarioXDependencia(tmefUsuario.getUsrId(), tmefDependenciaDto.getDpnId());
				}
			} catch (DependenciaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());	
			}
			// llamo al servicio de Dependecia para listar todos
		}
	}

	/**
	 * Método para llenar la lista de Carreras por Dependencia
	 */
	public void llenarCarreras(){
		tmefListCarreraDto= null;
		tmefCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
		try {
			if(tmefDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
				tmefListCarreraDto=servRpfCarreraDto.listarXfacultad(tmefDependenciaDto.getDpnId());
			}else {
				tmefListCarreraDto = null;
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen carreras para la dependencia seleccionada");
		}
	}
	
	
	 /**
		 * Método para llenar la lista de niveles 
		 */
		
		public void llenarNiveles(){
			tmefListNivel= new ArrayList<Nivel>();
			try {
				tmefListNivel= servNivelDtoServicio.listarTodos();	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	public void cambiarCarrera() {
		tmefListDocentesMateriasBusq = new ArrayList<>();
	}

	/**
	 * verifica que haga click en el boton buscar 
	 */
	public void buscarEstudianteXPeriodoXFacultadXCarreraXNivel(){
		try {
			if (tmefPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}else if (tmefDependenciaDto.getDpnId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la Facultad.");
			}else {
				tmefListEstudiantesNivelBusq = new ArrayList<EstudianteJdbcDto>();
				tmefListEstudiantesNivelBusq=servEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXFacultadXCarreraXNivel(tmefPeriodoAcademico.getPracId(),tmefDependenciaDto.getDpnId(), tmefCarreraDto.getCrrId(),tmefNivelDto.getNvlId());
				if (tmefListEstudiantesNivelBusq.size() > 0) {
					cargarReporte();
					tmefHabilitarExportar=false;
				}else {
					FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tmefHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
		}
	}	


	public void cargarReporte(){
		try {
			List<Map<String, Object>> frmCrpCampos = null;
			Map<String, Object> frmCrpParametros = null;
			String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DEL REPORTEO  ****************//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "EstudiantesXNivel";
			java.util.Date date= new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha",fecha);
			frmCrpParametros.put("periodo",tmefListEstudiantesNivelBusq.get(0).getPracDescripcion()); 
			frmCrpParametros.put("facultad",tmefListEstudiantesNivelBusq.get(0).getDpnDescripcion());
			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			int cont = 1;
			for (EstudianteJdbcDto item : tmefListEstudiantesNivelBusq) {
				StringBuilder sbNumero = new StringBuilder();
				dato = new HashMap<String, Object>();
				dato.put("identificacion", item.getPrsIdentificacion());
				dato.put("carrera", item.getCrrId());
				StringBuilder sb=new StringBuilder();
				sb.append(item.getPrsPrimerApellido());
				try {
					sb.append(" ");
					sb.append(item.getPrsSegundoApellido());
				} catch (Exception e) {
					// TODO: handle exception
				}		
				dato.put("apellidos", sb.toString());
				dato.put("nombres", item.getPrsNombres());
				sbNumero.append(cont);
				dato.put("numero", sbNumero.toString());
				dato.put("facultad", item.getDpnDescripcion());
				dato.put("carrera", item.getCrrDescripcion());
				dato.put("nivel", String.valueOf(item.getFcmtNivelUbicacion()));

				cont ++;

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
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	
	public Usuario getTmefUsuario() {
		return tmefUsuario;
	}


	public void setTmefUsuario(Usuario tmefUsuario) {
		this.tmefUsuario = tmefUsuario;
	}


	public UsuarioDto getTmefUsuarioDto() {
		return tmefUsuarioDto;
	}


	public void setTmefUsuarioDto(UsuarioDto tmefUsuarioDto) {
		this.tmefUsuarioDto = tmefUsuarioDto;
	}


	public PeriodoAcademico getTmefPeriodoAcademico() {
		return tmefPeriodoAcademico;
	}


	public void setTmefPeriodoAcademico(PeriodoAcademico tmefPeriodoAcademico) {
		this.tmefPeriodoAcademico = tmefPeriodoAcademico;
	}


	public DependenciaDto getTmefDependenciaDto() {
		return tmefDependenciaDto;
	}


	public void setTmefDependenciaDto(DependenciaDto tmefDependenciaDto) {
		this.tmefDependenciaDto = tmefDependenciaDto;
	}


	public Carrera getTmefCarrera() {
		return tmefCarrera;
	}


	public void setTmefCarrera(Carrera tmefCarrera) {
		this.tmefCarrera = tmefCarrera;
	}


	public CarreraDto getTmefCarreraDto() {
		return tmefCarreraDto;
	}


	public void setTmefCarreraDto(CarreraDto tmefCarreraDto) {
		this.tmefCarreraDto = tmefCarreraDto;
	}


	public String getTmefTipoCarrera() {
		return tmefTipoCarrera;
	}


	public void setTmefTipoCarrera(String tmefTipoCarrera) {
		this.tmefTipoCarrera = tmefTipoCarrera;
	}


	public Integer getTmefTipoUsuario() {
		return tmefTipoUsuario;
	}


	public void setTmefTipoUsuario(Integer tmefTipoUsuario) {
		this.tmefTipoUsuario = tmefTipoUsuario;
	}


	public Integer getTmefDpnId() {
		return tmefDpnId;
	}


	public void setTmefDpnId(Integer tmefDpnId) {
		this.tmefDpnId = tmefDpnId;
	}


	public List<DocenteJdbcDto> getTmefListDocentesMateriasBusq() {
		tmefListDocentesMateriasBusq = tmefListDocentesMateriasBusq == null ? (new ArrayList<DocenteJdbcDto>()): tmefListDocentesMateriasBusq;
		return tmefListDocentesMateriasBusq;
	}


	public void setTmefListDocentesMateriasBusq(List<DocenteJdbcDto> tmefListDocentesMateriasBusq) {
		this.tmefListDocentesMateriasBusq = tmefListDocentesMateriasBusq;
	}



	public List<PeriodoAcademico> getTmefListPeriodoAcademico() {
		tmefListPeriodoAcademico = tmefListPeriodoAcademico == null ? (new ArrayList<PeriodoAcademico>()): tmefListPeriodoAcademico;
		return tmefListPeriodoAcademico;
	}


	public void setTmefListPeriodoAcademico(List<PeriodoAcademico> tmefListPeriodoAcademico) {
		this.tmefListPeriodoAcademico = tmefListPeriodoAcademico;
	}


	public List<Dependencia> getTmefListDependencia() {
		return tmefListDependencia;
	}


	public void setTmefListDependencia(List<Dependencia> tmefListDependencia) {
		this.tmefListDependencia = tmefListDependencia;
	}


	public List<CarreraDto> getTmefListCarreraDto() {
		tmefListCarreraDto = tmefListCarreraDto == null ? (new ArrayList<CarreraDto>()): tmefListCarreraDto;
		return tmefListCarreraDto;
	}


	public void setTmefListCarreraDto(List<CarreraDto> tmefListCarreraDto) {
		this.tmefListCarreraDto = tmefListCarreraDto;
	}


	public List<Carrera> getTmefListCarrera() {
		tmefListCarrera = tmefListCarrera == null ? (new ArrayList<Carrera>()): tmefListCarrera;
		return tmefListCarrera;
	}


	public void setTmefListCarrera(List<Carrera> tmefListCarrera) {
		this.tmefListCarrera = tmefListCarrera;
	}


	public Integer getTmefSeleccionarTodos() {
		return tmefSeleccionarTodos;
	}


	public void setTmefSeleccionarTodos(Integer tmefSeleccionarTodos) {
		this.tmefSeleccionarTodos = tmefSeleccionarTodos;
	}


	public String getTmefTituloModal() {
		return tmefTituloModal;
	}


	public void setTmefTituloModal(String tmefTituloModal) {
		this.tmefTituloModal = tmefTituloModal;
	}


	public String getTmefMensajeModal() {
		return tmefMensajeModal;
	}


	public void setTmefMensajeModal(String tmefMensajeModal) {
		this.tmefMensajeModal = tmefMensajeModal;
	}


	public String getTmefMensajeActivacion() {
		return tmefMensajeActivacion;
	}


	public void setTmefMensajeActivacion(String tmefMensajeActivacion) {
		this.tmefMensajeActivacion = tmefMensajeActivacion;
	}


	public Integer getTmefOpcionSeleccionada() {
		return tmefOpcionSeleccionada;
	}


	public void setTmefOpcionSeleccionada(Integer tmefOpcionSeleccionada) {
		this.tmefOpcionSeleccionada = tmefOpcionSeleccionada;
	}


	public boolean isTmefHabilitarPeriodo() {
		return tmefHabilitarPeriodo;
	}


	public void setTmefHabilitarPeriodo(boolean tmefHabilitarPeriodo) {
		this.tmefHabilitarPeriodo = tmefHabilitarPeriodo;
	}


	public boolean isTmefHabilitarExportar() {
		return tmefHabilitarExportar;
	}


	public void setTmefHabilitarExportar(boolean tmefHabilitarExportar) {
		this.tmefHabilitarExportar = tmefHabilitarExportar;
	}


	public Integer getTmefValidadorClick() {
		return tmefValidadorClick;
	}


	public void setTmefValidadorClick(Integer tmefValidadorClick) {
		this.tmefValidadorClick = tmefValidadorClick;
	}


	public Integer getTmefValidadorEdicion() {
		return tmefValidadorEdicion;
	}


	public void setTmefValidadorEdicion(Integer tmefValidadorEdicion) {
		this.tmefValidadorEdicion = tmefValidadorEdicion;
	}


	public boolean isTmefHabilitadorNivel() {
		return tmefHabilitadorNivel;
	}


	public void setTmefHabilitadorNivel(boolean tmefHabilitadorNivel) {
		this.tmefHabilitadorNivel = tmefHabilitadorNivel;
	}


	public boolean isTmefHabilitadorSeleccion() {
		return tmefHabilitadorSeleccion;
	}


	public void setTmefHabilitadorSeleccion(boolean tmefHabilitadorSeleccion) {
		this.tmefHabilitadorSeleccion = tmefHabilitadorSeleccion;
	}


	public boolean isTmefHabilitadorGuardar() {
		return tmefHabilitadorGuardar;
	}


	public void setTmefHabilitadorGuardar(boolean tmefHabilitadorGuardar) {
		this.tmefHabilitadorGuardar = tmefHabilitadorGuardar;
	}


	public List<NivelDto> getTmefListNivelDto() {
		return tmefListNivelDto;
	}


	public void setTmefListNivelDto(List<NivelDto> tmefListNivelDto) {
		this.tmefListNivelDto = tmefListNivelDto;
	}


	public List<Nivel> getTmefListNivel() {
		return tmefListNivel;
	}


	public void setTmefListNivel(List<Nivel> tmefListNivel) {
		this.tmefListNivel = tmefListNivel;
	}


	public NivelDto getTmefNivelDto() {
		return tmefNivelDto;
	}


	public void setTmefNivelDto(NivelDto tmefNivelDto) {
		this.tmefNivelDto = tmefNivelDto;
	}


	public List<EstudianteJdbcDto> getTmefListEstudiantesNivelBusq() {
		return tmefListEstudiantesNivelBusq;
	}


	public void setTmefListEstudiantesNivelBusq(List<EstudianteJdbcDto> tmefListEstudiantesNivelBusq) {
		this.tmefListEstudiantesNivelBusq = tmefListEstudiantesNivelBusq;
	}


 
	
	
}










