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

import org.jboss.resteasy.spi.HttpResponse;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReporteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
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

@ManagedBean(name="reportePlanificacionDocentesForm")
@SessionScoped
public class ReportePlanificacionDocentesForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario rpdfUsuario;
	private UsuarioDto rpdfUsuarioDto; 

	//VARIABLES DE BUSQUEDA
	private PeriodoAcademico rpdfPeriodoAcademico;
	private DependenciaDto rpdfDependenciaDto;
	private Dependencia rpdfDependencia;
	private Carrera rpdfCarrera;

	private CarreraDto rpdfCarreraDto;

	private String rpdfTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer rpdfTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer rpdfDpnId; //diferenciar el tipo de usuario que ingreso - por carreras
	private List<DocenteJdbcDto> rpdfListDocentesMateriasBusq;
	
	

	private DocenteJdbcDto rpdfDocentesMateriaBuscar;

	//LISTA DE OBJETOS
	private List<PeriodoAcademico> rpdfListPeriodoAcademico;
	private List<Dependencia> rpdfListDependencia;
	private List<CarreraDto> rpdfListCarreraDto;
	private List<Nivel> rpdfListNivel;
	private List<Carrera> rpdfListCarrera;

	//AUXILIARES		
	private Integer rpdfSeleccionarTodos;
	private String rpdfTituloModal;
	private String rpdfMensajeModal;
	private String rpdfMensajeActivacion;
	private Integer rpdfOpcionSeleccionada;
	private boolean rpdfHabilitarPeriodo;
	private boolean rpdfHabilitarExportar;

	private Integer rpdfValidadorClick;
	private Integer rpdfValidadorEdicion;
	private boolean rpdfHabilitadorNivel;
	private boolean rpdfHabilitadorSeleccion;
	private boolean rpdfHabilitadorGuardar;
	
	
	//reporte
	protected HttpResponse httpRes;

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
	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;
	
	

	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * @param usuario - el usuario que ingresa 
	 * @return  Navegacion a la pagina xhtml de Administración .
	 */
	public String irReportePlanificacionDocentes(Usuario usuario) { 
		rpdfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					rpdfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					rpdfTipoCarrera="carrrtmfTipoUsuarioeras";
					rpdfHabilitarPeriodo=true;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rpdfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					rpdfTipoCarrera="soporte";
					rpdfHabilitarPeriodo=true;
				}
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} 
		return "irReportePlanificacionDocentes";
	}

	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * @param usuario - el usuario que ingresa 
	 * @return  Navegacion a la pagina xhtml de Administración .
	 */
	public String irReporteDocenciaPorParalelo(Usuario usuario) { 
		rpdfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					rpdfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					rpdfTipoCarrera="carrrtmfTipoUsuarioeras";
					rpdfHabilitarPeriodo=true;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rpdfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					rpdfTipoCarrera="soporte";
					rpdfHabilitarPeriodo=true;
				}
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} 
		return "irReporteDocenciaPorParalelo";
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
		iniciarParametrosReporteDocentesMaterias();
	}

	public void iniciarParametrosReporteDocentesMaterias(){
		try {
			rpdfHabilitarExportar=true;
			rpdfUsuarioDto = new UsuarioDto();
			//inicio listas
			rpdfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			rpdfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			rpdfListDependencia= new ArrayList<Dependencia>(); //inicio la lista de PeriodoAcademico

			rpdfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			rpdfListCarrera= new ArrayList<Carrera>(); //inicio la lista de Carreras 
			rpdfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel

			//inicio objetos
			rpdfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			rpdfDependenciaDto = new DependenciaDto();
			rpdfCarreraDto= new CarreraDto();    //Instancio el objeto carrera DTO
			rpdfCarrera= new Carrera();    //Instancio el objeto carrera
			//			rpdfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
						rpdfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

			rpdfPeriodoAcademico = new PeriodoAcademico();
			rpdfListDocentesMateriasBusq = null;
			
			rpdfDocentesMateriaBuscar=new DocenteJdbcDto();
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	public void iniciarParametros(){
		try {
			rpdfHabilitarExportar=true;
			rpdfUsuarioDto = new UsuarioDto();
			//inicio listas
			rpdfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			rpdfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			rpdfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			//inicio objetos
			rpdfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			rpdfDependenciaDto = new DependenciaDto();
			rpdfCarreraDto= new CarreraDto();    //Instancio el objeto carrera DTO
			rpdfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto
			rpdfDependenciaDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
			rpdfListDocentesMateriasBusq = null;
			
			rpdfListDependencia = new ArrayList<>();
			llenarPeriodos();
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que instancia la lista de resultados cuando cambio de período
	 */
	public void cambiarPeriodo() {
		try {
			rpdfListDocentesMateriasBusq = new ArrayList<>();
			rpdfListDependencia = new ArrayList<>();
			if(rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
				rpdfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(rpdfUsuario.getUsrId());
			}else if(rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				rpdfListDependencia= servRpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			}
			if(rpdfUsuario.getUsrNick().equals("jareinoso")){
				rpdfListDependencia= servRpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			}
			rpdfHabilitarExportar=true;
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	public void llenarPeriodos(){
		Integer tipoPeriodo = null;
		rpdfListPeriodoAcademico= null;
		if(rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
			rpdfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosConCargaHoraria();
		}else if(rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
			if(rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()) {
				tipoPeriodo = PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE;
			}
			if(tipoPeriodo != null) {
				rpdfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos
				//				rpdfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosConDocentes(tipoPeriodo);// llamo al servicio de periodoAcademico para listar todos	
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
		try {
			rpdfListDependencia= null;
			if(rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
				if(rpdfDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
					rpdfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuarioXDependencia(rpdfUsuario.getUsrId(), rpdfDependenciaDto.getDpnId());
				}
			}else if(rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				if(rpdfDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
					rpdfListDependencia= servRpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				}
			}
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		}
	}

	/**
	 * Método para llenar la lista de Carreras por Dependencia
	 */
	public void llenarCarreras(){
		try {
			rpdfListCarreraDto= null;
			rpdfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
			if(rpdfDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
				if(rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					rpdfListCarreraDto=servRpfCarreraDto.listarXUsuarioXDependencia(rpdfUsuario.getUsrId(),rpdfDependenciaDto.getDpnId());
				}else if(rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rpdfListCarreraDto=servRpfCarreraDto.listarXDocentesHorasClaseXPeriodoXDependencia(rpdfPeriodoAcademico.getPracId(), rpdfDependenciaDto.getDpnId());
				}
				
			}else {
				rpdfListDocentesMateriasBusq = null;
			}
			rpdfHabilitarExportar=true;
			rpdfListDocentesMateriasBusq = null;
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			try {
				rpdfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
				if(rpdfUsuario.getUsrNick().equals("jareinoso")){
					rpdfListCarreraDto=servRpfCarreraDto.listarXfacultad(rpdfDependenciaDto.getDpnId());
				}	
			} catch (Exception e2) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen carreras para la dependencia seleccionada");
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	public void cambiarCarrera() {
		rpdfListDocentesMateriasBusq = new ArrayList<>();
		rpdfHabilitarExportar=true;
	}

	/**
	 * verifica que haga click en el boton buscar 
	 */
	
	public void buscarDocentesXMateriaXCarrera(){
		try {
			if (rpdfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}else if (rpdfDependenciaDto.getDpnId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la Facultad.");
			}else {
				rpdfListDocentesMateriasBusq =servDocenteDtoServicioJdbc.listarXPeriodoAcademicoXDependenciaXCarrera(rpdfPeriodoAcademico.getPracId(),rpdfDependenciaDto.getDpnId(), rpdfCarreraDto.getCrrId(), rpdfUsuario, rpdfTipoUsuario);
				if (rpdfListDocentesMateriasBusq.size() > 0) {
					cargarReporte();
					rpdfHabilitarExportar=false;
				}else {
					FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
				}
			}
		} catch (Exception e) {
			rpdfHabilitarExportar=true;
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
			frmCrpNombreReporte = "CargaHorariaXDocente";
			java.util.Date date= new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha",fecha);
			frmCrpParametros.put("periodo",rpdfListDocentesMateriasBusq.get(0).getPracDescripcion());
			frmCrpParametros.put("facultad",rpdfListDocentesMateriasBusq.get(0).getDpnDescripcion());


			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
		
			for (DocenteJdbcDto item : rpdfListDocentesMateriasBusq) {
				
				dato = new HashMap<String, Object>();
				//dato.put("identificacion", item.getPrsIdentificacion());
				//dato.put("carrera", item.getCrrId());
				StringBuilder sb=new StringBuilder();
				sb.append(item.getPrsPrimerApellido());
				try {
					sb.append(" ");
					sb.append(item.getPrsSegundoApellido());
				} catch (Exception e) {
				}		
				
				dato.put("cedula", item.getPrsIdentificacion());
				dato.put("nombres", sb.toString()+" "+ item.getPrsNombres());
				dato.put("relacionLaboral", item.getRllbDescripcion());
				dato.put("tiempoDedicacion", item.getTmddDescripcion());
				dato.put("carrera", item.getCrrDescripcion());
				dato.put("asignatura", item.getMtrDescripcion());
				dato.put("paralelo", item.getPrlDescripcion());
				dato.put("imparticionClases", item.getCrhrDescripcionSuma());
			

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
		}


	}
	
	public void cargarReporteDocenciaParalelo(){
		try {
			List<Map<String, Object>> frmCrpCampos = null;
			Map<String, Object> frmCrpParametros = null;
			String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DEL REPORTEO  ****************//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "ReporteDocenciaPorParalelo";
			java.util.Date date= new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha",fecha);
			frmCrpParametros.put("periodo",rpdfListDocentesMateriasBusq.get(0).getPracDescripcion());
			if(rpdfCarrera.getCrrId()==GeneralesConstantes.APP_ID_BASE){
				frmCrpParametros.put("carrera", "TODAS");
			}else{
				frmCrpParametros.put("carrera", rpdfListDocentesMateriasBusq.get(0).getCrrDescripcion());
			}
			frmCrpParametros.put("facultad",rpdfListDocentesMateriasBusq.get(0).getDpnDescripcion());
			
			


			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
		
			for (DocenteJdbcDto item : rpdfListDocentesMateriasBusq) {
				
				dato = new HashMap<String, Object>();
			
				dato.put("facultad", item.getDpnDescripcion());
				dato.put("carrera", item.getCrrDescripcion());
				dato.put("asignatura", item.getMtrDescripcion());
				dato.put("paralelo", item.getPrlDescripcion());
				dato.put("numeroMatriculados", item.getNumeroMatriculados());
			

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
		}


	}
	


	public Usuario getRpdfUsuario() {
		return rpdfUsuario;
	}

	public void setRpdfUsuario(Usuario rpdfUsuario) {
		this.rpdfUsuario = rpdfUsuario;
	}

	public UsuarioDto getRpdfUsuarioDto() {
		return rpdfUsuarioDto;
	}

	public void setRpdfUsuarioDto(UsuarioDto rpdfUsuarioDto) {
		this.rpdfUsuarioDto = rpdfUsuarioDto;
	}

	public PeriodoAcademico getRpdfPeriodoAcademico() {
		return rpdfPeriodoAcademico;
	}

	public void setRpdfPeriodoAcademico(PeriodoAcademico rpdfPeriodoAcademico) {
		this.rpdfPeriodoAcademico = rpdfPeriodoAcademico;
	}

	public DependenciaDto getRpdfDependenciaDto() {
		return rpdfDependenciaDto;
	}

	public void setRpdfDependenciaDto(DependenciaDto rpdfDependenciaDto) {
		this.rpdfDependenciaDto = rpdfDependenciaDto;
	}

	public Carrera getRpdfCarrera() {
		return rpdfCarrera;
	}

	public void setRpdfCarrera(Carrera rpdfCarrera) {
		this.rpdfCarrera = rpdfCarrera;
	}

	public CarreraDto getRpdfCarreraDto() {
		return rpdfCarreraDto;
	}

	public void setRpdfCarreraDto(CarreraDto rpdfCarreraDto) {
		this.rpdfCarreraDto = rpdfCarreraDto;
	}

	public String getRpdfTipoCarrera() {
		return rpdfTipoCarrera;
	}

	public void setRpdfTipoCarrera(String rpdfTipoCarrera) {
		this.rpdfTipoCarrera = rpdfTipoCarrera;
	}

	public Integer getRpdfTipoUsuario() {
		return rpdfTipoUsuario;
	}

	public void setRpdfTipoUsuario(Integer rpdfTipoUsuario) {
		this.rpdfTipoUsuario = rpdfTipoUsuario;
	}

	public Integer getRpdfDpnId() {
		return rpdfDpnId;
	}

	public void setRpdfDpnId(Integer rpdfDpnId) {
		this.rpdfDpnId = rpdfDpnId;
	}

	public List<DocenteJdbcDto> getRpdfListDocentesMateriasBusq() {
		return rpdfListDocentesMateriasBusq;
	}

	public void setRpdfListDocentesMateriasBusq(List<DocenteJdbcDto> rpdfListDocentesMateriasBusq) {
		this.rpdfListDocentesMateriasBusq = rpdfListDocentesMateriasBusq;
	}

	public DocenteJdbcDto getRpdfDocentesMateriaBuscar() {
		return rpdfDocentesMateriaBuscar;
	}

	public void setRpdfDocentesMateriaBuscar(DocenteJdbcDto rpdfDocentesMateriaBuscar) {
		this.rpdfDocentesMateriaBuscar = rpdfDocentesMateriaBuscar;
	}

	public List<PeriodoAcademico> getRpdfListPeriodoAcademico() {
		rpdfListPeriodoAcademico = rpdfListPeriodoAcademico == null? (new ArrayList<PeriodoAcademico>()) : rpdfListPeriodoAcademico;
		return rpdfListPeriodoAcademico;
	}

	public void setRpdfListPeriodoAcademico(List<PeriodoAcademico> rpdfListPeriodoAcademico) {
		this.rpdfListPeriodoAcademico = rpdfListPeriodoAcademico;
	}

	public List<Dependencia> getRpdfListDependencia() {
		rpdfListDependencia = rpdfListDependencia == null? (new ArrayList<Dependencia>()) : rpdfListDependencia;
		return rpdfListDependencia;
	}

	public void setRpdfListDependencia(List<Dependencia> rpdfListDependencia) {
		this.rpdfListDependencia = rpdfListDependencia;
	}

	public List<CarreraDto> getRpdfListCarreraDto() {
		rpdfListCarrera = rpdfListCarrera == null? (new ArrayList<Carrera>()) : rpdfListCarrera;
		return rpdfListCarreraDto;
	}

	public void setRpdfListCarreraDto(List<CarreraDto> rpdfListCarreraDto) {
		this.rpdfListCarreraDto = rpdfListCarreraDto;
	}

	public List<Nivel> getRpdfListNivel() {
		rpdfListNivel = rpdfListNivel == null? (new ArrayList<Nivel>()) : rpdfListNivel;
		return rpdfListNivel;
	}

	public void setRpdfListNivel(List<Nivel> rpdfListNivel) {
		this.rpdfListNivel = rpdfListNivel;
	}

	public List<Carrera> getRpdfListCarrera() {
		return rpdfListCarrera;
	}

	public void setRpdfListCarrera(List<Carrera> rpdfListCarrera) {
		this.rpdfListCarrera = rpdfListCarrera;
	}

	public Integer getRpdfSeleccionarTodos() {
		return rpdfSeleccionarTodos;
	}

	public void setRpdfSeleccionarTodos(Integer rpdfSeleccionarTodos) {
		this.rpdfSeleccionarTodos = rpdfSeleccionarTodos;
	}

	public String getRpdfTituloModal() {
		return rpdfTituloModal;
	}

	public void setRpdfTituloModal(String rpdfTituloModal) {
		this.rpdfTituloModal = rpdfTituloModal;
	}

	public String getRpdfMensajeModal() {
		return rpdfMensajeModal;
	}

	public void setRpdfMensajeModal(String rpdfMensajeModal) {
		this.rpdfMensajeModal = rpdfMensajeModal;
	}

	public String getRpdfMensajeActivacion() {
		return rpdfMensajeActivacion;
	}

	public void setRpdfMensajeActivacion(String rpdfMensajeActivacion) {
		this.rpdfMensajeActivacion = rpdfMensajeActivacion;
	}

	public Integer getRpdfOpcionSeleccionada() {
		return rpdfOpcionSeleccionada;
	}

	public void setRpdfOpcionSeleccionada(Integer rpdfOpcionSeleccionada) {
		this.rpdfOpcionSeleccionada = rpdfOpcionSeleccionada;
	}

	public boolean isRpdfHabilitarPeriodo() {
		return rpdfHabilitarPeriodo;
	}

	public void setRpdfHabilitarPeriodo(boolean rpdfHabilitarPeriodo) {
		this.rpdfHabilitarPeriodo = rpdfHabilitarPeriodo;
	}

	public boolean isRpdfHabilitarExportar() {
		return rpdfHabilitarExportar;
	}

	public void setRpdfHabilitarExportar(boolean rpdfHabilitarExportar) {
		this.rpdfHabilitarExportar = rpdfHabilitarExportar;
	}

	public Integer getRpdfValidadorClick() {
		return rpdfValidadorClick;
	}

	public void setRpdfValidadorClick(Integer rpdfValidadorClick) {
		this.rpdfValidadorClick = rpdfValidadorClick;
	}

	public Integer getRpdfValidadorEdicion() {
		return rpdfValidadorEdicion;
	}

	public void setRpdfValidadorEdicion(Integer rpdfValidadorEdicion) {
		this.rpdfValidadorEdicion = rpdfValidadorEdicion;
	}

	public boolean isRpdfHabilitadorNivel() {
		return rpdfHabilitadorNivel;
	}

	public void setRpdfHabilitadorNivel(boolean rpdfHabilitadorNivel) {
		this.rpdfHabilitadorNivel = rpdfHabilitadorNivel;
	}

	public boolean isRpdfHabilitadorSeleccion() {
		return rpdfHabilitadorSeleccion;
	}

	public void setRpdfHabilitadorSeleccion(boolean rpdfHabilitadorSeleccion) {
		this.rpdfHabilitadorSeleccion = rpdfHabilitadorSeleccion;
	}

	public boolean isRpdfHabilitadorGuardar() {
		return rpdfHabilitadorGuardar;
	}

	public void setRpdfHabilitadorGuardar(boolean rpdfHabilitadorGuardar) {
		this.rpdfHabilitadorGuardar = rpdfHabilitadorGuardar;
	}

	public Dependencia getRpdfDependencia() {
		return rpdfDependencia;
	}

	public void setRpdfDependencia(Dependencia rpdfDependencia) {
		this.rpdfDependencia = rpdfDependencia;
	}

	

	
	
}










