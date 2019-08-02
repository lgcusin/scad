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
   
 ARCHIVO:     AdministracionUsuarioForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de la tabla convocatoria.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
15 - Febrero - 2017		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionUsuarioForm.
 * Managed Bean que maneja las peticiones de la tabla Usuario , Usuario_rol , Rol_flujo_carrera.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="administracionUsuarioForm")
@SessionScoped
public class AdministracionUsuarioForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
	// *****************************************************************/
	// ******************* Variables de AdministracionUsuarioForm********/
	// *****************************************************************/
	
	private Dependencia aufDependencia;
	private Rol aufRol;
	private List<Dependencia> aufListDependencia;
	private List<Rol> aufListRol;
	private Carrera aufCarrera;
	private List<Carrera> aufListCarrera;
	private UsuarioRolJdbcDto aufUsuarioRolJdbcDtoBuscar;
	private UsuarioRolJdbcDto aufUsuarioRolJdbcDtoBuscarVer;
	private List<UsuarioRolJdbcDto> aufListCarrerasXIdentificacion;
	private UsuarioRolJdbcDto aufUsuarioRolJdbcDtoEditar;
	private List<UsuarioRolJdbcDto> aufListaufUsuarioRolJdbcDtoBuscar;
	private boolean aufHabilitadorCarrera;
	private Integer aufValidadorClic;
	private String mensajeModal;
	private String mensajeTituloModal;
	private UsuarioRolJdbcDto aufUsuarioRolJdbcDtoEditarGuardar;
	private Usuario aufUsuarioRegistra;

	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private DependenciaServicio servAufDependenciaServicio;
	@EJB
	private CarreraServicio servAufCarreraServicio;
	@EJB
	private PersonaServicio servAufPersonaServicio;
	@EJB
	private UsuarioRolDtoServicioJdbc servAufUsuarioRolDtoServicioJdbc;
	@EJB
	private	RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	@EJB
	private	RolServicio servRolServicio;
	@EJB
	private FichaInscripcionDtoServicioJdbc servFichaInscripcionDtoServicioJdbc;
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	
	//****************************************************************/
	//******** METODOS DE NAVEGACION ********** **********************/
	//****************************************************************/
	
	/**
	 * Método que dirige a la página de Listar Usuarios 
	 * @param usuario
	 * @return
	 */
	public String irListar(Usuario usuario){
			try {
				UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionSoporte(usuario.getUsrIdentificacion());
				if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
					return null;
				}
				aufListaufUsuarioRolJdbcDtoBuscar=null;
				iniciarParametros();
				aufListDependencia = servAufDependenciaServicio.listarTodos();
				aufListCarrera = new ArrayList<Carrera>();
				aufListRol = servRolServicio.listarTodosparaAdministracion();
			} catch (UsuarioRolNoEncontradoException e) {
			} catch (UsuarioRolException e) {
			} catch (DependenciaNoEncontradoException e) {
			}
		return "irAdministracionUsuarios";
	}

	/**
	 * Método que dirige a la página de Listar Usuarios 
	 * @param usuario
	 * @return
	 */
	public String irRoles(Usuario usuario){
		aufUsuarioRegistra = usuario;
			try {
				UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionSoporte(usuario.getUsrIdentificacion());
				if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
					return null;
				}
				aufListaufUsuarioRolJdbcDtoBuscar=null;
				iniciarParametros();
				aufListDependencia = servAufDependenciaServicio.listarTodos();
				aufListCarrera = new ArrayList<Carrera>();
				aufListRol = servRolServicio.listarTodosparaAdministracion();
			} catch (UsuarioRolNoEncontradoException e) {
			} catch (UsuarioRolException e) {
			} catch (DependenciaNoEncontradoException e) {
			}
//			return null;
		return "irAdministracionRoles";
	}
	
	/**
	 * Maneja de cancelacion del listar usuarios
	 * @return - cadena de navegación a la página de inicio
	 */
	public String cancelarListar(){
		aufListaufUsuarioRolJdbcDtoBuscar=null;
		return "irMenuPrincipal";
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	/**
	 * Método para limpiar los componentes del formulario
	 * 
	 */
	public void limpiar() {
		aufListCarrera= new ArrayList<Carrera>();
		aufHabilitadorCarrera=true;
		aufUsuarioRolJdbcDtoEditar=null;
		aufDependencia.setDpnId(GeneralesConstantes.APP_ID_BASE);
		aufValidadorClic = 0;
		aufRol = new Rol();
		aufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
		aufUsuarioRolJdbcDtoBuscar = new UsuarioRolJdbcDto();
		aufUsuarioRolJdbcDtoBuscar.setPrsPrimerApellido(null);
		aufUsuarioRolJdbcDtoBuscar.setPrsIdentificacion(null);
		aufListaufUsuarioRolJdbcDtoBuscar=new ArrayList<UsuarioRolJdbcDto>();
	}
	
	/**
	 * Lista los estudiantes validados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarUsuarios(){
		if(aufDependencia.getDpnId()==GeneralesConstantes.APP_ID_BASE){
			aufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
		}
		try {
			aufListaufUsuarioRolJdbcDtoBuscar = servAufUsuarioRolDtoServicioJdbc.buscarXIdentificacionXFacultadXCarreaConRol(aufUsuarioRolJdbcDtoBuscar.getPrsIdentificacion(),  aufRol.getRolId(), aufUsuarioRolJdbcDtoBuscar.getPrsPrimerApellido());
			if(aufListaufUsuarioRolJdbcDtoBuscar.size()==0){
				aufListaufUsuarioRolJdbcDtoBuscar = servAufUsuarioRolDtoServicioJdbc.buscarXIdentificacionXFacultadXCarreaAlterno(aufUsuarioRolJdbcDtoBuscar.getPrsIdentificacion(),  aufRol.getRolId(), aufUsuarioRolJdbcDtoBuscar.getPrsPrimerApellido());
				if(aufListaufUsuarioRolJdbcDtoBuscar.size()==0){
					limpiar();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");					
				}
	
			}
		} catch (UsuarioRolJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Excepción al buscar usuarios con los parámetros ingresados");
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");
		}
	}
	
	/**
	 * Lista los estudiantes validados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void activarDesactivarCarrera(UsuarioRolJdbcDto entidad){
		this.aufUsuarioRolJdbcDtoBuscar = entidad;
		try {
			if(aufUsuarioRolJdbcDtoBuscar.getRoflcrEstado()==RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE){
				servRolFlujoCarreraServicio.desactivarRolFlujoCarreraXUsuarioRol(aufUsuarioRolJdbcDtoBuscar.getRoflcrId());	
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha desactivado la carrera seleccionada para el usuario actual");
				irVerUsuario(aufUsuarioRolJdbcDtoEditar);
			}else if(aufUsuarioRolJdbcDtoBuscar.getRoflcrEstado()==RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE){
				servRolFlujoCarreraServicio.activarRolFlujoCarreraXUsuarioRol(aufUsuarioRolJdbcDtoBuscar.getRoflcrId(),aufUsuarioRolJdbcDtoBuscar.getRolId() );
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha activado la carrera seleccionada para el usuario actual");
				irVerUsuario(aufUsuarioRolJdbcDtoEditar);
			}
		}  catch (RolFlujoCarreraException e) {
			irVerUsuario(aufUsuarioRolJdbcDtoEditar);
		} catch (RolFlujoCarreraNoEncontradoException e) {
		} catch (RolFlujoCarreraValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}catch (Exception e){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public String desactivar(){
		try {
			if(aufUsuarioRolJdbcDtoEditar.getUsrEstado()==UsuarioRolConstantes.ESTADO_ACTIVO_VALUE){
				servUsuarioRolServicio.desactivarUsuarioRolXid(null, aufUsuarioRolJdbcDtoEditar.getUsrId());
//				servRolFlujoCarreraServicio.desactivarRolFlujoCarrerasXListaUsuarioRol(aufUsuarioRolJdbcDtoEditar.getUsroId());	
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("El usuario ha sido deshabilitado del sistema");
			}else if(aufUsuarioRolJdbcDtoEditar.getUsrEstado()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				servUsuarioRolServicio.activarUsuarioRolXid(null, aufUsuarioRolJdbcDtoEditar.getUsrId());
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("El usuario ha sido habilitado en el sistema");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		}
		aufListaufUsuarioRolJdbcDtoBuscar = null;
		verificarClickAceptarTramiteNo();
		return "irInicio";
	}
	
	
	public String irVerUsuario(UsuarioRolJdbcDto entidad){
		
		
		aufUsuarioRolJdbcDtoBuscar = entidad;
		aufUsuarioRolJdbcDtoBuscarVer = aufUsuarioRolJdbcDtoBuscar;
			try {
				aufListCarrerasXIdentificacion = servAufUsuarioRolDtoServicioJdbc.buscarCarrerasXIdentificacion(aufUsuarioRolJdbcDtoBuscar.getPrsIdentificacion());
			} catch (UsuarioRolJdbcDtoException e) {
			} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			}
			return "irVerUsuario";
		
		
		
	}
	
	
	public String irCancelarVer(){
//		limpiar();
		return "irCancelarVer";
	}
	
	
	public String irEditar(UsuarioRolJdbcDto entidad){
		aufUsuarioRolJdbcDtoEditar = entidad;
		aufUsuarioRolJdbcDtoBuscar = entidad;
		return "irEditarUsuario";
	}
	
	public String irCancelarEdicion(){
		aufUsuarioRolJdbcDtoEditarGuardar = new UsuarioRolJdbcDto();
		aufUsuarioRolJdbcDtoBuscar = aufUsuarioRolJdbcDtoBuscarVer;
//		System.out.println(aufUsuarioRolJdbcDtoBuscar);
//		limpiar();
		return "irCancelarEdicion";
	}

	/**
	 * Guarda el Usuario a editar
	 * @return navegacion a la pagina de informacion general
	 */
		public String guardarUsuario(UsuarioRolJdbcDto entidad){
			aufUsuarioRolJdbcDtoEditarGuardar = new UsuarioRolJdbcDto();
			aufUsuarioRolJdbcDtoEditarGuardar = entidad;
			try {
				if(aufUsuarioRolJdbcDtoEditarGuardar.getPrsTipoIdentificacion() == GeneralesConstantes.APP_ID_BASE){
					FacesUtil.mensajeError("Debe seleccionar el tipo de identificación");
					return null;
				}else if(aufUsuarioRolJdbcDtoEditarGuardar.getPrsIdentificacion() == null || aufUsuarioRolJdbcDtoEditarGuardar.getPrsIdentificacion().equals("")){
					FacesUtil.mensajeError("Debe ingresar el número de identificación");
					return null;
				}else if(aufUsuarioRolJdbcDtoEditarGuardar.getPrsNombres() == null 	|| aufUsuarioRolJdbcDtoEditarGuardar.getPrsNombres().equals("")){
					FacesUtil.mensajeError("Debe ingresar el nombre");
					return null;
				}else if(aufUsuarioRolJdbcDtoEditarGuardar.getPrsPrimerApellido() == null || aufUsuarioRolJdbcDtoEditarGuardar.getPrsPrimerApellido().equals("")){
					FacesUtil.mensajeError("Debe ingresar el primer apellido");
					return null;
				}else if(aufUsuarioRolJdbcDtoEditarGuardar.getPrsMailPersonal() == null || aufUsuarioRolJdbcDtoEditar.getPrsMailPersonal().equals("")){
					FacesUtil.mensajeError("Debe ingresar el mail personal");
					return null;
				}else if(aufUsuarioRolJdbcDtoEditarGuardar.getPrsMailInstitucional() == null || aufUsuarioRolJdbcDtoEditar.getPrsMailInstitucional().equals("")){
					FacesUtil.mensajeError("Debe ingresar el mail institucional");
					return null;
				}else if(aufUsuarioRolJdbcDtoEditarGuardar.getUsrEstado() == GeneralesConstantes.APP_ID_BASE){
					FacesUtil.mensajeError("Debe seleccionar el estado");
					return null;
				}else{
					//busco que no exista cedula repetida
					
					//busco que no exista correo institucional repetido 
					
					//busco que no exista correo personal repetido
					
					servAufPersonaServicio.editarXId(aufUsuarioRolJdbcDtoEditarGuardar);
					limpiar();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Usuario editado correctamente.");
					return "irInicio";
				}
			} catch (PersonaValidacionException | PersonaNoEncontradoException
					| PersonaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al editar el usuario...Intente más tarde.");
			}
			return "irInicio";
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		aufUsuarioRolJdbcDtoBuscar =  null;
		aufUsuarioRolJdbcDtoBuscar  = new UsuarioRolJdbcDto();
		aufRol = null;
		aufRol = new Rol();
		aufCarrera = null;
		aufCarrera = new Carrera();
		aufDependencia = null;
		aufDependencia = new Dependencia();
		aufHabilitadorCarrera=true;
		aufValidadorClic = 0;
		aufListRol = new ArrayList<Rol>();
		aufUsuarioRolJdbcDtoBuscarVer = new UsuarioRolJdbcDto();
	}

	/**
	 * Método para cargar la lista de carreras de acuerdo a la Dependencia seleccionada
	 * 
	 */
	public void cambiarDependencia() {
		if(aufDependencia!=null || aufDependencia.getDpnId()!=GeneralesConstantes.APP_ID_BASE){
			try {
				aufListCarrera=servAufCarreraServicio.listarCarrerasXFacultad(aufDependencia.getDpnId());
				aufHabilitadorCarrera=false;
			} catch (CarreraNoEncontradoException e) {
				limpiar();
			} catch (CarreraException e) {
				limpiar();
			}	
		}else{
			
		}
	}

	public String verificarClickAceptarTramite(UsuarioRolJdbcDto entidad){
		this.aufUsuarioRolJdbcDtoEditar=entidad;
		aufValidadorClic = 1;
		if(aufUsuarioRolJdbcDtoEditar.getUsrEstado()==UsuarioRolConstantes.ESTADO_ACTIVO_VALUE){
			mensajeTituloModal="Estado usuario";
			mensajeModal="¿Está seguro que desea desactivar el usuario? Recuerde... desactivará el usuario del sistema";
		}else{
				mensajeModal="¿Está seguro? Esta acción solo activará el usuario";
		}
		
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		aufValidadorClic = 0;
		return null;
	}

	//****************************************************************/
	//******************* METODOS GET / SET **************************/
	//****************************************************************/	

	public Dependencia getAufDependencia() {
		return aufDependencia;
	}


	public void setAufDependencia(Dependencia aufDependencia) {
		this.aufDependencia = aufDependencia;
	}


	public List<Dependencia> getAufListDependencia() {
		aufListDependencia = aufListDependencia==null?(new ArrayList<Dependencia>()):aufListDependencia;
		return aufListDependencia;
	}


	public void setAufListDependencia(List<Dependencia> aufListDependencia) {
		this.aufListDependencia = aufListDependencia;
	}


	public Carrera getAufCarrera() {
		return aufCarrera;
	}


	public void setAufCarrera(Carrera aufCarrera) {
		this.aufCarrera = aufCarrera;
	}


	public List<Carrera> getAufListCarrera() {
		aufListCarrera = aufListCarrera==null?(new ArrayList<Carrera>()):aufListCarrera;
		return aufListCarrera;
	}


	public void setAufListCarrera(List<Carrera> aufListCarrera) {
		this.aufListCarrera = aufListCarrera;
	}


	public UsuarioRolJdbcDto getAufUsuarioRolJdbcDtoBuscar() {
		return aufUsuarioRolJdbcDtoBuscar;
	}


	public void setAufUsuarioRolJdbcDtoBuscar(
			UsuarioRolJdbcDto aufUsuarioRolJdbcDtoBuscar) {
		this.aufUsuarioRolJdbcDtoBuscar = aufUsuarioRolJdbcDtoBuscar;
	}


	public UsuarioRolJdbcDto getAufUsuarioRolJdbcDtoEditar() {
		return aufUsuarioRolJdbcDtoEditar;
	}


	public void setAufUsuarioRolJdbcDtoEditar(
			UsuarioRolJdbcDto aufUsuarioRolJdbcDtoEditar) {
		this.aufUsuarioRolJdbcDtoEditar = aufUsuarioRolJdbcDtoEditar;
	}


	public List<UsuarioRolJdbcDto> getAufListaufUsuarioRolJdbcDtoBuscar() {
		aufListaufUsuarioRolJdbcDtoBuscar = aufListaufUsuarioRolJdbcDtoBuscar==null?(new ArrayList<UsuarioRolJdbcDto>()):aufListaufUsuarioRolJdbcDtoBuscar;
		return aufListaufUsuarioRolJdbcDtoBuscar;
	}


	public void setAufListaufUsuarioRolJdbcDtoBuscar(
			List<UsuarioRolJdbcDto> aufListaufUsuarioRolJdbcDtoBuscar) {
		this.aufListaufUsuarioRolJdbcDtoBuscar = aufListaufUsuarioRolJdbcDtoBuscar;
	}


	public boolean isAufHabilitadorCarrera() {
		return aufHabilitadorCarrera;
	}


	public void setAufHabilitadorCarrera(boolean aufHabilitadorCarrera) {
		this.aufHabilitadorCarrera = aufHabilitadorCarrera;
	}


	public List<UsuarioRolJdbcDto> getAufListCarrerasXIdentificacion() {
		aufListCarrerasXIdentificacion = aufListCarrerasXIdentificacion==null?(new ArrayList<UsuarioRolJdbcDto>()):aufListCarrerasXIdentificacion;
		return aufListCarrerasXIdentificacion;
	}


	public void setAufListCarrerasXIdentificacion(
			List<UsuarioRolJdbcDto> aufListCarrerasXIdentificacion) {
		this.aufListCarrerasXIdentificacion = aufListCarrerasXIdentificacion;
	}
	
	
	
	public Integer getAufValidadorClic() {
		return aufValidadorClic;
	}


	public void setAufValidadorClic(Integer aufValidadorClic) {
		this.aufValidadorClic = aufValidadorClic;
	}


	public String getMensajeModal() {
		return mensajeModal;
	}


	public void setMensajeModal(String mensajeModal) {
		this.mensajeModal = mensajeModal;
	}


	public String getMensajeTituloModal() {
		return mensajeTituloModal;
	}


	public void setMensajeTituloModal(String mensajeTituloModal) {
		this.mensajeTituloModal = mensajeTituloModal;
	}


	public Rol getAufRol() {
		return aufRol;
	}


	public void setAufRol(Rol aufRol) {
		this.aufRol = aufRol;
	}


	public List<Rol> getAufListRol() {
		return aufListRol;
	}


	public void setAufListRol(List<Rol> aufListRol) {
		this.aufListRol = aufListRol;
	}


	public UsuarioRolJdbcDto getAufUsuarioRolJdbcDtoBuscarVer() {
		return aufUsuarioRolJdbcDtoBuscarVer;
	}


	public void setAufUsuarioRolJdbcDtoBuscarVer(UsuarioRolJdbcDto aufUsuarioRolJdbcDtoBuscarVer) {
		this.aufUsuarioRolJdbcDtoBuscarVer = aufUsuarioRolJdbcDtoBuscarVer;
	}


	public UsuarioRolJdbcDto getAufUsuarioRolJdbcDtoEditarGuardar() {
		return aufUsuarioRolJdbcDtoEditarGuardar;
	}


	public void setAufUsuarioRolJdbcDtoEditarGuardar(UsuarioRolJdbcDto aufUsuarioRolJdbcDtoEditarGuardar) {
		this.aufUsuarioRolJdbcDtoEditarGuardar = aufUsuarioRolJdbcDtoEditarGuardar;
	}


	public Usuario getAufUsuarioRegistra() {
		return aufUsuarioRegistra;
	}


	public void setAufUsuarioRegistra(Usuario aufUsuarioRegistra) {
		this.aufUsuarioRegistra = aufUsuarioRegistra;
	}
	
	

}
