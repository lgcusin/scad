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
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
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

@ManagedBean(name="reporteDocentesMateriaCarreraForm")
@SessionScoped
public class ReporteDocentesMateriaCarreraForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario dmfcUsuario;
	private UsuarioDto dmfcUsuarioDto; 

	//VARIABLES DE BUSQUEDA
	private PeriodoAcademico dmfcPeriodoAcademico;
	private DependenciaDto dmfcDependenciaDto;
	private Carrera dmfcCarrera;

	private CarreraDto dmfcCarreraDto;

	private String dmfcTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer dmfcTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer dmfcDpnId; //diferenciar el tipo de usuario que ingreso - por carreras
	private List<DocenteJdbcDto> dmfcListDocentesMateriasBusq;

	private DocenteJdbcDto dmfcDocentesMateriaBuscar;

	//LISTA DE OBJETOS
	private List<PeriodoAcademico> dmfcListPeriodoAcademico;
	private List<Dependencia> dmfcListDependencia;
	private List<CarreraDto> dmfcListCarreraDto;
	private List<Nivel> dmfcListNivel;
	private List<Carrera> dmfcListCarrera;

	//AUXILIARES		
	private Integer dmfcSeleccionarTodos;
	private String dmfcTituloModal;
	private String dmfcMensajeModal;
	private String dmfcMensajeActivacion;
	private Integer dmfcOpcionSeleccionada;
	private boolean dmfcHabilitarPeriodo;
	private boolean dmfcHabilitarExportar;

	private Integer dmfcValidadorClick;
	private Integer dmfcValidadorEdicion;
	private boolean dmfcHabilitadorNivel;
	private boolean dmfcHabilitadorSeleccion;
	private boolean dmfcHabilitadorGuardar;

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
	public String irReporteDocentesMateriaCarrera(Usuario usuario) { 
		dmfcUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					dmfcTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					dmfcTipoCarrera="carrrtmfTipoUsuarioeras";
					dmfcHabilitarPeriodo=true;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					dmfcTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					dmfcTipoCarrera="soporte";
					dmfcHabilitarPeriodo=true;
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					dmfcTipoUsuario = RolConstantes.ROL_SECREABOGADO_VALUE.intValue();
					dmfcTipoCarrera="secreAbogado";
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_DECANO_VALUE.intValue()){
					dmfcTipoUsuario = RolConstantes.ROL_DECANO_VALUE.intValue();
					dmfcTipoCarrera="decano";
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SUBDECANO_VALUE.intValue()){
					dmfcTipoUsuario = RolConstantes.ROL_SUBDECANO_VALUE.intValue();
					dmfcTipoCarrera="subDecano";
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					dmfcTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					dmfcTipoCarrera="dirCarrera";
				}
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} 
		return "irReporteDocentesMateriaCarrera";
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
			dmfcHabilitarExportar=true;
			dmfcUsuarioDto = new UsuarioDto();
			//inicio listas
			dmfcSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			dmfcListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			dmfcListDependencia= new ArrayList<Dependencia>(); //inicio la lista de PeriodoAcademico

			dmfcListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			dmfcListCarrera= new ArrayList<Carrera>(); //inicio la lista de Carreras 
			dmfcListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel

			//inicio objetos
			dmfcPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			dmfcDependenciaDto = new DependenciaDto();
			dmfcCarreraDto= new CarreraDto();    //Instancio el objeto carrera DTO
			dmfcCarrera= new Carrera();    //Instancio el objeto carrera
			//			dmfcPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
						dmfcCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

			dmfcPeriodoAcademico = new PeriodoAcademico();
			dmfcListDocentesMateriasBusq = null;
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	public void iniciarParametros(){
		try {
			dmfcHabilitarExportar=true;
			dmfcUsuarioDto = new UsuarioDto();
			//inicio listas
			dmfcSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			dmfcListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			dmfcListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			//inicio objetos
			dmfcPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			dmfcDependenciaDto = new DependenciaDto();
			dmfcCarreraDto= new CarreraDto();    //Instancio el objeto carrera DTO
			dmfcCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto
			dmfcDependenciaDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
			dmfcListDocentesMateriasBusq = null;
			dmfcListDependencia = new ArrayList<>();
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
			dmfcListDocentesMateriasBusq = new ArrayList<>();
			dmfcListDependencia = new ArrayList<>();
			if(dmfcTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || dmfcTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()
					|| dmfcTipoUsuario == RolConstantes.ROL_DECANO_VALUE.intValue() || dmfcTipoUsuario == RolConstantes.ROL_SUBDECANO_VALUE.intValue()
					|| dmfcTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()	){
				dmfcListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(dmfcUsuario.getUsrId());
			}else if(dmfcTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				dmfcListDependencia= servRpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			}
			dmfcHabilitarExportar=true;
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	public void llenarPeriodos(){
		Integer tipoPeriodo = null;
		dmfcListPeriodoAcademico= null;
		if(dmfcTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue() || dmfcTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue() 
				|| dmfcTipoUsuario == RolConstantes.ROL_DECANO_VALUE.intValue() || dmfcTipoUsuario == RolConstantes.ROL_SUBDECANO_VALUE.intValue()
				|| dmfcTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() ){
			dmfcListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosConCargaHoraria();
		}else if(dmfcTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
			if(dmfcTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()) {
				tipoPeriodo = PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE;
			}
			if(tipoPeriodo != null) {
				dmfcListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos
				//				dmfcListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosConDocentes(tipoPeriodo);// llamo al servicio de periodoAcademico para listar todos	
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
			dmfcListDependencia= null;
			if(dmfcTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				if(dmfcDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
					dmfcListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuarioXDependencia(dmfcUsuario.getUsrId(), dmfcDependenciaDto.getDpnId());
				}
			}else if(dmfcTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				if(dmfcDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
					dmfcListDependencia= servRpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
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
			dmfcListCarreraDto= null;
			dmfcCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
			if(dmfcDependenciaDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) {
				if(dmfcTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || dmfcTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()
						|| dmfcTipoUsuario == RolConstantes.ROL_DECANO_VALUE.intValue() || dmfcTipoUsuario == RolConstantes.ROL_SUBDECANO_VALUE.intValue()
						|| dmfcTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()	){
//					dmfcListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXDependencia(dmfcUsuario.getUsrId(),RolConstantes.ROL_BD_SECRECARRERA,RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE,dmfcDependenciaDto.getDpnId());
					dmfcListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXDependencia(dmfcUsuario.getUsrId(),dmfcTipoUsuario,RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE,dmfcDependenciaDto.getDpnId());
				}else if(dmfcTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					dmfcListCarreraDto=servRpfCarreraDto.listarXDocentesHorasClaseXPeriodoXDependencia(dmfcPeriodoAcademico.getPracId(), dmfcDependenciaDto.getDpnId());
				}
			}else {
				dmfcListDocentesMateriasBusq = null;
			}
			dmfcHabilitarExportar=true;
			dmfcListDocentesMateriasBusq = null;
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen carreras para la dependencia seleccionada");
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	public void cambiarCarrera() {
		dmfcListDocentesMateriasBusq = new ArrayList<>();
		dmfcHabilitarExportar=true;
	}

	/**
	 * verifica que haga click en el boton buscar 
	 */
	
	public void buscarDocentesXMateriaXCarrera(){
		try {
			if (dmfcPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}else if (dmfcDependenciaDto.getDpnId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la Facultad.");
			}else {
				dmfcListDocentesMateriasBusq =servDocenteDtoServicioJdbc.listarXDocentesXMateriaXCarreraXMateria(dmfcPeriodoAcademico.getPracId(),dmfcDependenciaDto.getDpnId(), dmfcCarreraDto.getCrrId(), dmfcUsuario, dmfcTipoUsuario);
				if (dmfcListDocentesMateriasBusq.size() > 0) {
					cargarReporte();
					dmfcHabilitarExportar=false;
				}else {
					FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
				}
			}
		} catch (Exception e) {
			dmfcHabilitarExportar=true;
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
			frmCrpNombreReporte = "MateriasXDocente";
			java.util.Date date= new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha",fecha);
			frmCrpParametros.put("periodo",dmfcListDocentesMateriasBusq.get(0).getPracDescripcion());
			frmCrpParametros.put("facultad",dmfcListDocentesMateriasBusq.get(0).getDpnDescripcion());


			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			int cont = 1;
			for (DocenteJdbcDto item : dmfcListDocentesMateriasBusq) {
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
				}		
				dato.put("apellidos", sb.toString());
				dato.put("nombres", item.getPrsNombres());
				sbNumero.append(cont);
				dato.put("numero", sbNumero.toString());
				dato.put("paralelo", item.getPrlDescripcion());
				dato.put("facultad", item.getDpnDescripcion());
				dato.put("carrera", item.getCrrDescripcion());
				dato.put("materia", item.getMtrDescripcion());
				
				if(item.getCrrDescripcionComp() != null){
					dato.put("carreracomp", item.getCrrDescripcionComp());
				}else{
					dato.put("carreracomp", " ");
				}
				if(item.getMtrDescripcionComp() != null){
					dato.put("materiacomp", item.getMtrDescripcionComp());
				}else{
					dato.put("materiacomp", " ");
				}
				if(item.getPrlDescripcionComp() != null){
					dato.put("paralelocomp", item.getPrlDescripcionComp());
				}else{
					dato.put("paralelocomp", " ");
				}

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
		}


	}

	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getDmfcUsuario() {
		return dmfcUsuario;
	}
	public void setDmfcUsuario(Usuario dmfcUsuario) {
		this.dmfcUsuario = dmfcUsuario;
	}
	public UsuarioDto getDmfcUsuarioDto() {
		return dmfcUsuarioDto;
	}
	public void setDmfcUsuarioDto(UsuarioDto dmfcUsuarioDto) {
		this.dmfcUsuarioDto = dmfcUsuarioDto;
	}
	public PeriodoAcademico getDmfcPeriodoAcademico() {
		return dmfcPeriodoAcademico;
	}
	public void setDmfcPeriodoAcademico(PeriodoAcademico dmfcPeriodoAcademico) {
		this.dmfcPeriodoAcademico = dmfcPeriodoAcademico;
	}
	public DependenciaDto getDmfcDependenciaDto() {
		return dmfcDependenciaDto;
	}
	public void setDmfcDependenciaDto(DependenciaDto dmfcDependenciaDto) {
		this.dmfcDependenciaDto = dmfcDependenciaDto;
	}
	public Carrera getDmfcCarrera() {
		return dmfcCarrera;
	}
	public void setDmfcCarrera(Carrera dmfcCarrera) {
		this.dmfcCarrera = dmfcCarrera;
	}
	public CarreraDto getDmfcCarreraDto() {
		return dmfcCarreraDto;
	}
	public void setDmfcCarreraDto(CarreraDto dmfcCarreraDto) {
		this.dmfcCarreraDto = dmfcCarreraDto;
	}
	public String getDmfcTipoCarrera() {
		return dmfcTipoCarrera;
	}
	public void setDmfcTipoCarrera(String dmfcTipoCarrera) {
		this.dmfcTipoCarrera = dmfcTipoCarrera;
	}
	public Integer getDmfcTipoUsuario() {
		return dmfcTipoUsuario;
	}
	public void setDmfcTipoUsuario(Integer dmfcTipoUsuario) {
		this.dmfcTipoUsuario = dmfcTipoUsuario;
	}
	public Integer getDmfcDpnId() {
		return dmfcDpnId;
	}
	public void setDmfcDpnId(Integer dmfcDpnId) {
		this.dmfcDpnId = dmfcDpnId;
	}
	public List<DocenteJdbcDto> getDmfcListDocentesMateriasBusq() {
		dmfcListDocentesMateriasBusq = dmfcListDocentesMateriasBusq == null ? (new ArrayList<DocenteJdbcDto>()) : dmfcListDocentesMateriasBusq;
		return dmfcListDocentesMateriasBusq;
	}
	public void setDmfcListDocentesMateriasBusq(List<DocenteJdbcDto> dmfcListDocentesMateriasBusq) {
		this.dmfcListDocentesMateriasBusq = dmfcListDocentesMateriasBusq;
	}
	public DocenteJdbcDto getDmfcDocentesMateriaBuscar() {
		return dmfcDocentesMateriaBuscar;
	}
	public void setDmfcDocentesMateriaBuscar(DocenteJdbcDto dmfcDocentesMateriaBuscar) {
		this.dmfcDocentesMateriaBuscar = dmfcDocentesMateriaBuscar;
	}
	public List<PeriodoAcademico> getDmfcListPeriodoAcademico() {
		dmfcListPeriodoAcademico = dmfcListPeriodoAcademico == null ? (new ArrayList<PeriodoAcademico>()) : dmfcListPeriodoAcademico;
		return dmfcListPeriodoAcademico;
	}
	public void setDmfcListPeriodoAcademico(List<PeriodoAcademico> dmfcListPeriodoAcademico) {
		this.dmfcListPeriodoAcademico = dmfcListPeriodoAcademico;
	}
	public List<Dependencia> getDmfcListDependencia() {
		dmfcListDependencia = dmfcListDependencia == null ? (new ArrayList<Dependencia>()) : dmfcListDependencia;
		return dmfcListDependencia;
	}
	public void setDmfcListDependencia(List<Dependencia> dmfcListDependencia) {
		this.dmfcListDependencia = dmfcListDependencia;
	}
	public List<CarreraDto> getDmfcListCarreraDto() {
		dmfcListCarreraDto = dmfcListCarreraDto == null ? (new ArrayList<CarreraDto>()) : dmfcListCarreraDto;
		return dmfcListCarreraDto;
	}
	public void setDmfcListCarreraDto(List<CarreraDto> dmfcListCarreraDto) {
		this.dmfcListCarreraDto = dmfcListCarreraDto;
	}
	public List<Nivel> getDmfcListNivel() {
		dmfcListNivel = dmfcListNivel == null ? (new ArrayList<Nivel>()) : dmfcListNivel;
		return dmfcListNivel;
	}
	public void setDmfcListNivel(List<Nivel> dmfcListNivel) {
		this.dmfcListNivel = dmfcListNivel;
	}
	public List<Carrera> getDmfcListCarrera() {
		dmfcListCarrera = dmfcListCarrera == null ? (new ArrayList<Carrera>()) : dmfcListCarrera;
		return dmfcListCarrera;
	}
	public void setDmfcListCarrera(List<Carrera> dmfcListCarrera) {
		this.dmfcListCarrera = dmfcListCarrera;
	}
	public Integer getDmfcSeleccionarTodos() {
		return dmfcSeleccionarTodos;
	}
	public void setDmfcSeleccionarTodos(Integer dmfcSeleccionarTodos) {
		this.dmfcSeleccionarTodos = dmfcSeleccionarTodos;
	}
	public String getDmfcTituloModal() {
		return dmfcTituloModal;
	}
	public void setDmfcTituloModal(String dmfcTituloModal) {
		this.dmfcTituloModal = dmfcTituloModal;
	}
	public String getDmfcMensajeModal() {
		return dmfcMensajeModal;
	}
	public void setDmfcMensajeModal(String dmfcMensajeModal) {
		this.dmfcMensajeModal = dmfcMensajeModal;
	}
	public String getDmfcMensajeActivacion() {
		return dmfcMensajeActivacion;
	}
	public void setDmfcMensajeActivacion(String dmfcMensajeActivacion) {
		this.dmfcMensajeActivacion = dmfcMensajeActivacion;
	}
	public Integer getDmfcOpcionSeleccionada() {
		return dmfcOpcionSeleccionada;
	}
	public void setDmfcOpcionSeleccionada(Integer dmfcOpcionSeleccionada) {
		this.dmfcOpcionSeleccionada = dmfcOpcionSeleccionada;
	}
	public boolean isDmfcHabilitarPeriodo() {
		return dmfcHabilitarPeriodo;
	}
	public void setDmfcHabilitarPeriodo(boolean dmfcHabilitarPeriodo) {
		this.dmfcHabilitarPeriodo = dmfcHabilitarPeriodo;
	}
	public boolean isDmfcHabilitarExportar() {
		return dmfcHabilitarExportar;
	}
	public void setDmfcHabilitarExportar(boolean dmfcHabilitarExportar) {
		this.dmfcHabilitarExportar = dmfcHabilitarExportar;
	}
	public Integer getDmfcValidadorClick() {
		return dmfcValidadorClick;
	}
	public void setDmfcValidadorClick(Integer dmfcValidadorClick) {
		this.dmfcValidadorClick = dmfcValidadorClick;
	}
	public Integer getDmfcValidadorEdicion() {
		return dmfcValidadorEdicion;
	}
	public void setDmfcValidadorEdicion(Integer dmfcValidadorEdicion) {
		this.dmfcValidadorEdicion = dmfcValidadorEdicion;
	}
	public boolean isDmfcHabilitadorNivel() {
		return dmfcHabilitadorNivel;
	}
	public void setDmfcHabilitadorNivel(boolean dmfcHabilitadorNivel) {
		this.dmfcHabilitadorNivel = dmfcHabilitadorNivel;
	}
	public boolean isDmfcHabilitadorSeleccion() {
		return dmfcHabilitadorSeleccion;
	}
	public void setDmfcHabilitadorSeleccion(boolean dmfcHabilitadorSeleccion) {
		this.dmfcHabilitadorSeleccion = dmfcHabilitadorSeleccion;
	}
	public boolean isDmfcHabilitadorGuardar() {
		return dmfcHabilitadorGuardar;
	}
	public void setDmfcHabilitadorGuardar(boolean dmfcHabilitadorGuardar) {
		this.dmfcHabilitadorGuardar = dmfcHabilitadorGuardar;
	}
}










