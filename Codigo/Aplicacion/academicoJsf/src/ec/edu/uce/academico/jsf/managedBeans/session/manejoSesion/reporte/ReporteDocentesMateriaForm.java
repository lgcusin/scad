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
   
 ARCHIVO:     EstudianteListaForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-NOV-2017 			Vinicio Rosales                     Emisión Inicial
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
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
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
 * Clase (managed bean) EstudianteListaForm.
 * Managed Bean que administra los estudiantes para la visualización de matriculados.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name="reporteDocentesMateriaForm")
@SessionScoped
public class ReporteDocentesMateriaForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario dmfUsuario;
	private UsuarioDto dmfUsuarioDto; 
	
	//VARIABLES DE BUSQUEDA
	private PeriodoAcademico dmfPeriodoAcademico;
	private DependenciaDto dmfDependenciaDto;
	private Carrera dmfCarrera;
	
	
	private CarreraDto dmfCarreraDto;
	
	private String dmfTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer dmfTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer dmfDpnId; //diferenciar el tipo de usuario que ingreso - por carreras
	private List<DocenteJdbcDto> dmfListDocentesMateriasBusq;
	

	private DocenteJdbcDto dmfDocentesMateriaBuscar;


	//LISTA DE OBJETOS
	private List<PeriodoAcademico> dmfListPeriodoAcademico;
	private List<Dependencia> dmfListDependencia;
	private List<CarreraDto> dmfListCarreraDto;
	private List<Nivel> dmfListNivel;
	private List<Carrera> dmfListCarrera;


	//AUXILIARES		
	private Integer dmfSeleccionarTodos;
	private String dmfTituloModal;
	private String dmfMensajeModal;
	private String dmfMensajeActivacion;
	private Integer dmfOpcionSeleccionada;
	private boolean dmfHabilitarPeriodo;
	private boolean dmfHabilitarExportar;

	private Integer dmfValidadorClick;
	private Integer dmfValidadorEdicion;
	private boolean dmfHabilitadorNivel;
	private boolean dmfHabilitadorSeleccion;
	private boolean dmfHabilitadorGuardar;
	
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
	
	public String irReporteDocentesMateria(Usuario usuario) { 
		dmfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						dmfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					dmfTipoCarrera = "carrrtmfTipoUsuarioeras";
					dmfHabilitarPeriodo=true;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					dmfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					dmfTipoCarrera ="soporte";
					dmfHabilitarPeriodo=true;
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					dmfTipoUsuario = RolConstantes.ROL_SECREABOGADO_VALUE.intValue();
					dmfTipoCarrera ="secreAbogado";
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_DECANO_VALUE.intValue()){
					dmfTipoUsuario = RolConstantes.ROL_DECANO_VALUE.intValue();
					dmfTipoCarrera ="decano";
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SUBDECANO_VALUE.intValue()){
					dmfTipoUsuario = RolConstantes.ROL_SUBDECANO_VALUE.intValue();
					dmfTipoCarrera ="subDecano";
				} else if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					dmfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					dmfTipoCarrera ="dirCarrera";
				}
			}
			dmfPeriodoAcademico = new PeriodoAcademico();
			dmfPeriodoAcademico = servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			llenarDependencia();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteDocentesMaterias();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteDocentesMaterias();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteDocentesMaterias();
		}
		iniciarParametrosReporteDocentesMaterias();
		
		return "irReporteDocentesMateria";
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
		iniciarParametrosReporteDocentesMaterias();
	}
	
	public void limpiarReporte()  {
		iniciarParametrosReporteDocentesMaterias();
	}
	
	/**
	 * Método que inicializa la lista al momento de cambiar de período
	 */
	public void cambiarPeriodo(){
		dmfListDocentesMateriasBusq = null;
		dmfHabilitarExportar = true;
	}

	public void iniciarParametrosReporteDocentesMaterias(){
		try {
			dmfHabilitarExportar=true;
			dmfUsuarioDto = new UsuarioDto();
			//inicio listas
			dmfSeleccionarTodos=GeneralesConstantes.APP_ID_BASE;
			dmfListPeriodoAcademico= new ArrayList<PeriodoAcademico>(); //inicio la lista de PeriodoAcademico
			dmfListDependencia= new ArrayList<Dependencia>(); //inicio la lista de PeriodoAcademico
			
			dmfListCarreraDto= new ArrayList<CarreraDto>(); //inicio la lista de Carreras Dto
			dmfListCarrera= new ArrayList<Carrera>(); //inicio la lista de Carreras Dto
			dmfListNivel= new ArrayList<Nivel>(); //inicio la lista de Nivel
			
			//inicio objetos
			
			dmfPeriodoAcademico = new PeriodoAcademico(); //Inicio los objetos PeriodoAcademico, CarreraDto y ParaleloDto
			dmfDependenciaDto = new DependenciaDto();
			dmfCarreraDto= new CarreraDto();    //Instancio el objeto carrera
			dmfCarrera= new Carrera();    //Instancio el objeto carrera
//			dmfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo  en -99 los valores iniciales IdPeriodoAcademico 
//			dmfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //e IdCarrera de los objetos PeriodoAcademico y CarreraDto

			
			llenarPeriodos(); //Listar todos los peridos academicos
//            llenarDependencia(); 
//            llenarCarreras();   //Listar  todos las carreras por el  usuario
	        dmfPeriodoAcademico = new PeriodoAcademico();
	        dmfListDocentesMateriasBusq = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

//	public void llenarPeriodos(){
//		dmfListPeriodoAcademico= null;
//		if(dmfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
////			dmfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos
//			dmfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosConCargaHoraria();// llamo al servicio de periodoAcademico para listar todos
//		}else if (dmfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()) {
//			dmfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();// llamo al servicio de periodoAcademico para listar todos			
//		}else if(dmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
////			dmfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodos();
//			dmfListPeriodoAcademico= servRpfPeriodoAcademico.listarTodosConCargaHoraria();
//		}
//	}
	
	public void llenarPeriodos(){
		dmfListPeriodoAcademico = null;
		dmfListPeriodoAcademico = servRpfPeriodoAcademico.listarTodosConCargaHoraria();// llamo al servicio de periodoAcademico para listar todos
		if(dmfListPeriodoAcademico.size() <= 0 ){
			dmfListPeriodoAcademico = null;
			FacesUtil.mensajeError("No se encontró períodos académicos");
		}
	}

	/**
	 * Método para llenar la lista Dependencias
	 */
	public void llenarDependencia(){
		try {
			dmfListDependencia= null;
			if(dmfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				dmfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(dmfUsuario.getUsrId());
			}else if(dmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				dmfListDependencia= servRpfDependenciaServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
			}
		} catch (DependenciaNoEncontradoException e) {
			e.getMessage();
		}
	}
	
	/**
	 * Método para llenar la lista de Carreras por Dependencia
	 */
	public void llenarCarreras(){
		
		dmfListCarreraDto= null;
		dmfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE); //inicializo el valor Id del objeto carrera en -99
		try {
			dmfListCarreraDto=servRpfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFl(dmfUsuario.getUsrId(),RolConstantes.ROL_BD_SECRECARRERA,RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteDocentesMaterias();
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen carreras para la dependencia seleccionada");
			iniciarParametrosReporteDocentesMaterias();	
		}
	}
	
	/**
	 * verifica que haga click en el boton buscar 
	 */
	public void buscarDocentesXMateria(){
		try {
			if (dmfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar el periodo académico");
			}else if (dmfUsuarioDto.getPrsIdentificacion().length()==0) {
						FacesUtil.mensajeError("Por favor, ingrese la identificación.");
            }else {
			dmfListDocentesMateriasBusq =servDocenteDtoServicioJdbc.listarXDocentesXMateria(dmfPeriodoAcademico.getPracId(),dmfUsuarioDto.getPrsIdentificacion(), dmfUsuario);
			//dmfListDocentesMateriasBusq =servDocenteDtoServicioJdbc.listarXDocentesXMateria(dmfPeriodoAcademico.getPracId(),dmfDependenciaDto.getDpnId(), dmfCarreraDto.getCrrId(), dmfUsuarioDto.getPrsIdentificacion(), dmfUsuario);

				if (dmfListDocentesMateriasBusq.size() > 0) {
					cargarReporte();
					dmfHabilitarExportar=false;
				}
			}
						
			
			
		} catch (Exception e) {
			e.printStackTrace();
			dmfHabilitarExportar=true;
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
			frmCrpParametros.put("periodo",dmfListDocentesMateriasBusq.get(0).getPracDescripcion());
			frmCrpParametros.put("facultad",dmfListDocentesMateriasBusq.get(0).getDpnDescripcion());
			
			
			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			int cont = 1;
			for (DocenteJdbcDto item : dmfListDocentesMateriasBusq) {
				StringBuilder sbNumero = new StringBuilder();
				dato = new HashMap<String, Object>();
				dato.put("identificacion", item.getPrsIdentificacion());
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
			e.printStackTrace();
		}
		
		
}

	
	
		//****************************************************************/
		//******************* GETTERS Y SETTERS *************************/
		//****************************************************************/

		public Usuario getDmfUsuario() {
			return dmfUsuario;
		}


		public void setDmfUsuario(Usuario dmfUsuario) {
			this.dmfUsuario = dmfUsuario;
		}


		public PeriodoAcademico getDmfPeriodoAcademico() {
			return dmfPeriodoAcademico;
		}


		public void setDmfPeriodoAcademico(PeriodoAcademico dmfPeriodoAcademico) {
			this.dmfPeriodoAcademico = dmfPeriodoAcademico;
		}


		public DependenciaDto getDmfDependenciaDto() {
			return dmfDependenciaDto;
		}


		public void setDmfDependenciaDto(DependenciaDto dmfDependenciaDto) {
			this.dmfDependenciaDto = dmfDependenciaDto;
		}


		public Carrera getDmfCarrera() {
			return dmfCarrera;
		}


		public void setDmfCarrera(Carrera dmfCarrera) {
			this.dmfCarrera = dmfCarrera;
		}


		public CarreraDto getDmfCarreraDto() {
			return dmfCarreraDto;
		}


		public void setDmfCarreraDto(CarreraDto dmfCarreraDto) {
			this.dmfCarreraDto = dmfCarreraDto;
		}


		public String getDmfTipoCarrera() {
			return dmfTipoCarrera;
		}


		public void setDmfTipoCarrera(String dmfTipoCarrera) {
			this.dmfTipoCarrera = dmfTipoCarrera;
		}


		public Integer getDmfTipoUsuario() {
			return dmfTipoUsuario;
		}


		public void setDmfTipoUsuario(Integer dmfTipoUsuario) {
			this.dmfTipoUsuario = dmfTipoUsuario;
		}


		public Integer getDmfDpnId() {
			return dmfDpnId;
		}


		public void setDmfDpnId(Integer dmfDpnId) {
			this.dmfDpnId = dmfDpnId;
		}


		public List<PeriodoAcademico> getDmfListPeriodoAcademico() {
			return dmfListPeriodoAcademico;
		}


		public void setDmfListPeriodoAcademico(List<PeriodoAcademico> dmfListPeriodoAcademico) {
			this.dmfListPeriodoAcademico = dmfListPeriodoAcademico;
		}


		public List<Dependencia> getDmfListDependencia() {
			return dmfListDependencia;
		}


		public void setDmfListDependencia(List<Dependencia> dmfListDependencia) {
			this.dmfListDependencia = dmfListDependencia;
		}


		public List<CarreraDto> getDmfListCarreraDto() {
			return dmfListCarreraDto;
		}


		public void setDmfListCarreraDto(List<CarreraDto> dmfListCarreraDto) {
			this.dmfListCarreraDto = dmfListCarreraDto;
		}


		public List<Nivel> getDmfListNivel() {
			return dmfListNivel;
		}


		public void setDmfListNivel(List<Nivel> dmfListNivel) {
			this.dmfListNivel = dmfListNivel;
		}


		public List<Carrera> getDmfListCarrera() {
			return dmfListCarrera;
		}


		public void setDmfListCarrera(List<Carrera> dmfListCarrera) {
			this.dmfListCarrera = dmfListCarrera;
		}


		public Integer getDmfSeleccionarTodos() {
			return dmfSeleccionarTodos;
		}


		public void setDmfSeleccionarTodos(Integer dmfSeleccionarTodos) {
			this.dmfSeleccionarTodos = dmfSeleccionarTodos;
		}


		public String getDmfTituloModal() {
			return dmfTituloModal;
		}


		public void setDmfTituloModal(String dmfTituloModal) {
			this.dmfTituloModal = dmfTituloModal;
		}


		public String getDmfMensajeModal() {
			return dmfMensajeModal;
		}


		public void setDmfMensajeModal(String dmfMensajeModal) {
			this.dmfMensajeModal = dmfMensajeModal;
		}


		public String getDmfMensajeActivacion() {
			return dmfMensajeActivacion;
		}


		public void setDmfMensajeActivacion(String dmfMensajeActivacion) {
			this.dmfMensajeActivacion = dmfMensajeActivacion;
		}


		public Integer getDmfOpcionSeleccionada() {
			return dmfOpcionSeleccionada;
		}


		public void setDmfOpcionSeleccionada(Integer dmfOpcionSeleccionada) {
			this.dmfOpcionSeleccionada = dmfOpcionSeleccionada;
		}


		public boolean isDmfHabilitarPeriodo() {
			return dmfHabilitarPeriodo;
		}


		public void setDmfHabilitarPeriodo(boolean dmfHabilitarPeriodo) {
			this.dmfHabilitarPeriodo = dmfHabilitarPeriodo;
		}


		public boolean isDmfHabilitarExportar() {
			return dmfHabilitarExportar;
		}


		public void setDmfHabilitarExportar(boolean dmfHabilitarExportar) {
			this.dmfHabilitarExportar = dmfHabilitarExportar;
		}


		public Integer getDmfValidadorClick() {
			return dmfValidadorClick;
		}


		public void setDmfValidadorClick(Integer dmfValidadorClick) {
			this.dmfValidadorClick = dmfValidadorClick;
		}


		public Integer getDmfValidadorEdicion() {
			return dmfValidadorEdicion;
		}


		public void setDmfValidadorEdicion(Integer dmfValidadorEdicion) {
			this.dmfValidadorEdicion = dmfValidadorEdicion;
		}


		public boolean isDmfHabilitadorNivel() {
			return dmfHabilitadorNivel;
		}


		public void setDmfHabilitadorNivel(boolean dmfHabilitadorNivel) {
			this.dmfHabilitadorNivel = dmfHabilitadorNivel;
		}


		public boolean isDmfHabilitadorSeleccion() {
			return dmfHabilitadorSeleccion;
		}


		public void setDmfHabilitadorSeleccion(boolean dmfHabilitadorSeleccion) {
			this.dmfHabilitadorSeleccion = dmfHabilitadorSeleccion;
		}


		public boolean isDmfHabilitadorGuardar() {
			return dmfHabilitadorGuardar;
		}


		public void setDmfHabilitadorGuardar(boolean dmfHabilitadorGuardar) {
			this.dmfHabilitadorGuardar = dmfHabilitadorGuardar;
		}


		public UsuarioDto getDmfUsuarioDto() {
			return dmfUsuarioDto;
		}


		public void setDmfUsuarioDto(UsuarioDto dmfUsuarioDto) {
			this.dmfUsuarioDto = dmfUsuarioDto;
		}

		
		public List<DocenteJdbcDto> getDmfListDocentesMateriasBusq() {
			return dmfListDocentesMateriasBusq;
		}


		public void setDmfListDocentesMateriasBusq(List<DocenteJdbcDto> dmfListDocentesMateriasBusq) {
			this.dmfListDocentesMateriasBusq = dmfListDocentesMateriasBusq;
		}


		public DocenteJdbcDto getDmfDocentesMateriaBuscar() {
			return dmfDocentesMateriaBuscar;
		}


		public void setDmfDocentesMateriaBuscar(DocenteJdbcDto dmfDocentesMateriaBuscar) {
			this.dmfDocentesMateriaBuscar = dmfDocentesMateriaBuscar;
		}
		
}

		
	
		
		
		
	
	
	
	
	