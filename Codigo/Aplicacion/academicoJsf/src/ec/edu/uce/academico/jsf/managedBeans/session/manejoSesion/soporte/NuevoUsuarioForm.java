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
   
 ARCHIVO:     NuevoUsuarioForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de la tabla Usuario , Usuario_rol , Rol_flujo_carrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02 - marzo - 2017		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.DocenteTHJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.academico.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralException;
import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionException;
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RelacionLaboralServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TiempoDedicacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteTHDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PuestoEnum;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.Etnia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;
import ec.edu.uce.academico.jpa.entidades.publico.RelacionLaboral;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.TiempoDedicacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) NuevoUsuarioForm.
 * Managed Bean que maneja las peticiones de la tabla Usuario , Usuario_rol , Rol_flujo_carrera.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="nuevoUsuarioForm")
@SessionScoped
public class NuevoUsuarioForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
		
	// *****************************************************************/
	// ******************* Variables de AdministracionUsuarioForm********/
	// *****************************************************************/
	
	private Dependencia nufFacultad;
	private List<Dependencia> nufListFacultad;
	private Carrera nufCarrera;
	private List<Carrera> nufListCarrera;
	private Rol nufRol;
	private List<Rol> nufListRol;
	private UsuarioRolJdbcDto nufUsuarioRolJdbcDtoBuscar;
	private List<UsuarioRolJdbcDto> nufListUsuariosXCarreraXRol;
	private UsuarioCreacionDto nufUsuarioCreacionDtoCrear;
	private UsuarioCreacionDto nufUsuarioCreacionDtoCrearConsulta;
	private UsuarioRolJdbcDto nufUsuarioRolJdbcDtoCrear;
	private List<Etnia> nufListEtnias;
	private List<UbicacionDto> nufListPaises;
	private List<UsuarioRolJdbcDto> nufListaRolesUsuario;
	private List<UsuarioRolJdbcDto> nufListaufUsuarioRolJdbcDtoBuscar;
	private boolean nufHabilitadorFacultad;
	private boolean nufHabilitadorCarrera;
	private boolean nufHabilitadorRol;
	private boolean nufHabilitadorGuardar;
	private boolean nufHabilitadorDocente;
	private List<UsuarioRolJdbcDto> nufListUsuarioXRol;
	private Integer nufNivelAcadémico;
	private boolean nufHabilitaNivelAcademico;
	private Usuario nufUsuario;
	
	private DocenteTHJdbcDto nufDocenteTH;
	
	//PARA DETALLE PUESTO
	private List<DocenteJdbcDto> nufListDetallePuestoDocente;
	private DocenteJdbcDto nufDocenteDetallePuestoEdit;
	private List<PeriodoAcademicoDto> nufListPeriodoAcademicoPosgrado;
	private PeriodoAcademicoDto nufPeriodoAcademicoPosgrado;
	private boolean nufHabilitaPeriodoPosgrado;
	private List<RelacionLaboral> nufListRelacionLaboral;
	private List<TiempoDedicacion> nufListTiempoDedicacion;
	private List<Puesto> nufListCategoria;
	private RelacionLaboral nufRelacionLaboral;
	private TiempoDedicacion nufTiempoDedicacion;
	private Puesto nufPuesto;
	private boolean nufHabiltaPuesto;
	private boolean nufHabilitaCategoria;
	private Integer nufCategoria;
	private boolean nufHabilitaRango;
	private boolean nufTiempo;
	private Integer nufRango;
	
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private DependenciaServicio servNufFacultadServicio;
	@EJB
	private CarreraServicio servNufCarreraServicio;
	@EJB
	private UsuarioRolDtoServicioJdbc servNufUsuarioRolDtoServicioJdbc;
	
	@EJB
	private RolServicio servNufRolServicio;
	@EJB
	private EtniaServicio servRpfEtnia;
	@EJB
	private UbicacionDtoServicioJdbc servRpfUbicacionJdbc;
	
	@EJB
	private	RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	@EJB 
	private UsuarioServicio servUsuarioServicio;
	@EJB 
	private PersonaServicio servPersonaServicio;
	
	@EJB
	private FichaDocenteServicio servFichaDocenteServicio;
	@EJB
	private DetallePuestoServicio servDetallePuestoServicio;
	
	@EJB
	private DocenteTHDtoServicioJdbc servDocenteTHDtoServicioJdbc;
	
	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servPeriodoAcademicoDtoServicioJdbc;
	@EJB	
	private RelacionLaboralServicio servRelacionLaboralServicio;
	@EJB
	private TiempoDedicacionServicio servTiempoDedicacionServicio;
	@EJB
	private PuestoServicio servPuestoServicio;
	
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
	public String irNuevo(Usuario usuario){
			try {
				List<UsuarioRol> usroLista = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
				for (UsuarioRol item : usroLista) {
					if(item.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE && item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
						return null;
					}
				}
				iniciarParametros();
				nufListRol = servNufRolServicio.listarTodosparaAdministracion();
				Iterator<Rol> iterador = nufListRol.listIterator();
				while(iterador.hasNext()){
					Rol rolAux = iterador.next();
					if(rolAux.getRolId()==RolConstantes.ROL_ESTUD_PREGRADO_VALUE
							|| rolAux.getRolId()==RolConstantes.ROL_ESTUD_VALUE
							||rolAux.getRolId()==RolConstantes.ROL_ESTUDIANTEPOSGRADO_VALUE){
						iterador.remove();
					}
				}
				nufHabilitadorGuardar=true;
				return "irUsuarioNuevo";
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Rol no encontrado para el usuario con identificacion " + usuario.getUsrIdentificacion());
			} catch (UsuarioRolException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Excepción al buscar el rol del usuario");
			} 
			return null;
	}

	/**
	 * Método que dirige a la página de administrarCarreras
	 * 
	 * @param usuario
	 * @return
	 */
	public String irAdministrarCarreras(UsuarioRolJdbcDto entidad, Usuario usuarioRegistra) {
		nufUsuario = usuarioRegistra;
		iniciarParametros();
		nufListUsuarioXRol = new ArrayList<>();
		nufUsuarioCreacionDtoCrear = new UsuarioCreacionDto();
		nufUsuarioCreacionDtoCrear.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
		nufUsuarioCreacionDtoCrear.setPrsIdentificacion(entidad.getPrsIdentificacion());
		nufUsuarioCreacionDtoCrear.setPrsPrimerApellido(entidad.getPrsPrimerApellido());
		nufUsuarioCreacionDtoCrear.setPrsSegundoApellido(entidad.getPrsSegundoApellido());
		nufUsuarioCreacionDtoCrear.setPrsNombres(entidad.getPrsNombres());
		nufUsuarioCreacionDtoCrear.setPrsMailInstitucional(entidad.getPrsMailInstitucional());
		nufUsuarioCreacionDtoCrear.setUsrNick(entidad.getUsrNick());
		nufUsuarioCreacionDtoCrear.setUsrId(entidad.getUsrId());
		nufUsuarioCreacionDtoCrear.setPrsId(entidad.getPrsId());
		nufListUsuariosXCarreraXRol = new ArrayList<UsuarioRolJdbcDto>();
		if(entidad.getUsrId() != 0){
			nufUsuarioCreacionDtoCrearConsulta = nufUsuarioCreacionDtoCrear;
		}
		
		//listar roles de usuario
		try {
//			nufListUsuarioXRol = servNufUsuarioRolDtoServicioJdbc.buscarXIdentificacionXFacultadXCarrea(nufUsuarioCreacionDtoCrear.getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE, null);
			nufListUsuarioXRol = servNufUsuarioRolDtoServicioJdbc.buscarXIdentificacion(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
		} catch (UsuarioRolJdbcDtoException e1) {
			limpiar();
		} catch (UsuarioRolJdbcDtoNoEncontradoException e1) {
			limpiar();
		}
		
		nufHabilitadorRol = false;
		try {
			nufListUsuariosXCarreraXRol=servNufUsuarioRolDtoServicioJdbc.buscarUsuarioXFacultadXCarreraTodas(entidad.getPrsIdentificacion(),nufRol.getRolId(), nufFacultad.getDpnId(), nufCarrera.getCrrId());
		}  catch (UsuarioRolJdbcDtoException e) {
			limpiar();
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			limpiar();
		}
		
		try {
			nufListRol = servNufRolServicio.listarTodos();
			nufListFacultad = servNufFacultadServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			nufHabilitadorFacultad=true;
//			try {
//				System.out.println("dfsdfdfsdfsdf");
//				nufListaRolesUsuario= servNufUsuarioRolDtoServicioJdbc.buscarRolesXIdentificacion(entidad.getPrsIdentificacion());
//				for (UsuarioRolJdbcDto item : nufListaRolesUsuario) {
//					System.out.println(item.getRolDescripcion());
//				}
//				nufHabilitadorDocente=true;
//			} catch (UsuarioRolJdbcDtoException e) {
//				System.out.println("error1");
//			} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
//				System.out.println("error2");
//			}
			
			//INGRESA LA INFORMACIÓN DE DETALLE PUESTO DEL DOCENTE
			nufListDetallePuestoDocente = new ArrayList<>();
			nufListDetallePuestoDocente = servDocenteDtoServicioJdbc.listarDocentesConDetallePuestoXPersona(entidad.getPrsId());
			
			return "irAdministrarCarreras";
		} catch (DependenciaNoEncontradoException e) {
			e.getMessage();
		} catch (DocenteDtoException e) {
			e.getMessage();
		}
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("No se encontraron dependencias para agregar a los usuarios.");
		return null;
	}
	
	/**
	 * Maneja de cancelacion del listar usuarios
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarNuevo(){
//		limpiar();
		
		return "irlistarUsuarios";
	}
	
	/**
	 * Maneja la navegación hacia nuevoAgregarCarreras.xhtml
	 * @return - cadena de navegacion a la pagina 
	 */
	public String irGuardarUsuario(){
		nufUsuarioCreacionDtoCrear.setPrsMailPersonal(nufUsuarioCreacionDtoCrear.getPrsMailPersonal().toLowerCase());
		
		nufUsuarioCreacionDtoCrear.setUsrIdentificacion(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
		nufUsuarioCreacionDtoCrear.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
		nufUsuarioCreacionDtoCrear.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
		nufUsuarioCreacionDtoCrear.setUsrEstSesion(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
		nufUsuarioCreacionDtoCrear.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
		nufUsuarioCreacionDtoCrear.setUsrFechaCreacion(new Timestamp((new Date()).getTime()));
		Date fechaActual = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(fechaActual);
		c.add(Calendar.YEAR, 1);
		Date fechaCaducidad = new Date();
		fechaCaducidad = c.getTime();
		nufUsuarioCreacionDtoCrear.setUsrFechaCaducidad(fechaCaducidad);
		nufUsuarioCreacionDtoCrear.setUsrFechaCadPass(fechaCaducidad);
		int indice = nufUsuarioCreacionDtoCrear.getPrsMailInstitucional().indexOf("@");
		String usrNick = nufUsuarioCreacionDtoCrear.getPrsMailInstitucional().substring(0,indice);
		nufUsuarioCreacionDtoCrear.setUsrNick(usrNick);
		nufUsuarioCreacionDtoCrear.setPrsEtn(null);
		nufUsuarioCreacionDtoCrear.setPrsFechaNacimiento(null);
		nufUsuarioCreacionDtoCrear.setPrsTelefono(null);
		nufUsuarioCreacionDtoCrear.setPrsUbcNac(null);
		nufUsuarioCreacionDtoCrear.setPrsTelefono(null);
		if(servUsuarioServicio.crearPersonaUsuario(nufUsuarioCreacionDtoCrear)){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("El usuario ha sido creado exitósamente.");	
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Error al crear el usuario, por favor intente más tarde.");
		}
		return "irlistarUsuarios";
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	/**
	 * Método para limpiar los componenetes del formulario
	 * 
	 */
	public void limpiar() {
		nufUsuarioCreacionDtoCrear= new UsuarioCreacionDto();
		nufListCarrera= new ArrayList<Carrera>();
		nufHabilitadorCarrera=true;
		nufListaufUsuarioRolJdbcDtoBuscar=new ArrayList<UsuarioRolJdbcDto>();
		nufHabilitadorCarrera=true;
		nufHabilitadorRol=true;
		nufHabilitadorGuardar=true;
		nufHabilitadorFacultad= true;
		nufHabilitadorDocente = false;
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		nufUsuarioCreacionDtoCrear =  null;
		nufUsuarioCreacionDtoCrear = new UsuarioCreacionDto();
		nufUsuarioRolJdbcDtoCrear = new UsuarioRolJdbcDto();
		nufCarrera = null;
		nufCarrera = new Carrera();
		nufFacultad = null;
		nufFacultad = new Dependencia();
		nufRol = null;
		nufRol = new Rol();
		nufHabilitadorFacultad= true;
		nufHabilitadorCarrera=true;
		nufHabilitadorRol=true;
		nufHabilitadorGuardar=true;
		nufListUsuariosXCarreraXRol= new ArrayList<UsuarioRolJdbcDto>();
		nufListRol = new ArrayList<Rol>();
		nufHabilitadorDocente = false;
		nufListaRolesUsuario = new ArrayList<UsuarioRolJdbcDto>();
		nufNivelAcadémico = GeneralesConstantes.APP_ID_BASE;
		nufHabilitaNivelAcademico = false;
		nufPeriodoAcademicoPosgrado = new PeriodoAcademicoDto();
		nufListPeriodoAcademicoPosgrado = new ArrayList<>();
		nufHabilitaPeriodoPosgrado = false;
		nufListRelacionLaboral = new ArrayList<>();
		nufListTiempoDedicacion = new ArrayList<>(); 
		nufListCategoria = new ArrayList<>();
		nufRelacionLaboral = new RelacionLaboral();
		nufTiempoDedicacion = new TiempoDedicacion();
		nufPuesto = new Puesto();
		nufHabiltaPuesto = false;
		nufHabilitaCategoria = true;
		nufCategoria = GeneralesConstantes.APP_ID_BASE;
		nufHabilitaRango = true;
		nufTiempo = true;
		nufRango = GeneralesConstantes.APP_ID_BASE;
		
	}

	/**
	 * Método para buscar el usuario que se ingresa su identificación
	 * 
	 */
	public void buscarUsuario() {
		nufUsuarioRolJdbcDtoBuscar = null;
		try {
			this.nufUsuarioRolJdbcDtoBuscar=servNufUsuarioRolDtoServicioJdbc.buscarUsuariosXIdentificacionAgregarCarrera(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
			nufUsuarioCreacionDtoCrear.setPrsNombres(nufUsuarioRolJdbcDtoBuscar.getPrsNombres());
			nufUsuarioCreacionDtoCrear.setPrsPrimerApellido(nufUsuarioRolJdbcDtoBuscar.getPrsPrimerApellido());
			nufUsuarioCreacionDtoCrear.setPrsSegundoApellido(nufUsuarioRolJdbcDtoBuscar.getPrsSegundoApellido());
			if(nufUsuarioCreacionDtoCrear.getPrsPrimerApellido()!=null){
				nufHabilitadorFacultad= false;	
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");
			}
		}  catch (UsuarioRolJdbcDtoException e) {
			limpiar();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Excepción al buscar usuarios con los parámetros ingresados");
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			limpiar();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");
		}catch (Exception e) {
		}
	}
	
	
	/**
	 * Método para cargar la lista de carreras de acuerdo a la facultad seleccionada
	 * 
	 */
	public void cambiarFacultad() {
		nufHabilitadorDocente = false;
		nufListPeriodoAcademicoPosgrado = null;
		nufCategoria = GeneralesConstantes.APP_ID_BASE;
		nufRango = GeneralesConstantes.APP_ID_BASE;
		nufTiempoDedicacion.setTmddId(GeneralesConstantes.APP_ID_BASE);
		nufRelacionLaboral.setRllbId(GeneralesConstantes.APP_ID_BASE);
		nufPeriodoAcademicoPosgrado.setPracId(GeneralesConstantes.APP_ID_BASE);
		if(nufFacultad!=null && nufFacultad.getDpnId()!=GeneralesConstantes.APP_ID_BASE ){
			if(nufRol.getRolId()==RolConstantes.ROL_ADMINFACULTAD_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_ADMINNIVELACION_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SECREABOGADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SECRESECREABOGADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DECANO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SUBDECANO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_CONSULTOREPORTES_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DIRPOSGRADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_INFORMATICOFACULTAD_VALUE) {
//					||nufRol.getRolId()==RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE) {
				List<Rol> listaRoles = new ArrayList<Rol>(); 
				listaRoles = servNufRolServicio.listarRolesXUsrId(nufUsuarioCreacionDtoCrear.getUsrId());
				if(listaRoles.size()!=0){
					boolean op = false;
					for (Rol rol : listaRoles) {
						if(nufRol.getRolId()==rol.getRolId()){
							try {
								List<RolFlujoCarrera> listaRolFlujoCarrera = servRolFlujoCarreraServicio.buscarXRolXUsuarioIdXDependencia(nufUsuarioCreacionDtoCrear.getUsrId(),nufRol.getRolId(),nufFacultad.getDpnId());
								//lista de todas las carreras por dependencia
								List<Carrera> carreraDpn = new ArrayList<>();
								carreraDpn = servNufCarreraServicio.listarCarrerasXFacultad(nufFacultad.getDpnId());
								//comparación de carreras por rol, para ver si estan asignadas las que faltan
								boolean asigna = true;
								List<Carrera> carrerasAsignar = new ArrayList<>();
								for (Carrera itemCarrera : carreraDpn) {
									asigna = true;
									for (RolFlujoCarrera itemRoflcr : listaRolFlujoCarrera) {
										if(itemCarrera.getCrrId() == itemRoflcr.getRoflcrCarrera().getCrrId()){
											asigna = false;
											break;
										}
									}
									if(asigna){
										carrerasAsignar.add(itemCarrera);
									}
								}
								
//								if(listaRolFlujoCarrera.size()!=0){
//									op=true;
//									break;		
//								}
								
								if(carrerasAsignar.size() <=0){
									op=true;
									break;		
								}
								
							} catch (RolFlujoCarreraNoEncontradoException e) {
							} catch (RolFlujoCarreraException e) {
							} catch (CarreraNoEncontradoException e) {
							} catch (CarreraException e) {
							}
						}
					}
					if(op){
						nufHabilitadorGuardar = true;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("El usuario ingresado ya posee el rol en la facultad seleccionada.");
					}else{
						nufHabilitadorGuardar=false;
						nufHabilitadorCarrera=true;
						nufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
					}
				}else{
					nufHabilitadorGuardar=false;
					nufHabilitadorCarrera=true;
					nufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);	
				}
			}else {
				if(nufRol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE
						|| nufRol.getRolId()==RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE){
					try {
						nufListCarrera=servNufCarreraServicio.listarCarrerasXFacultadPosgrado(nufFacultad.getDpnId());
						nufHabilitadorCarrera=false;
					} catch (Exception e) {
						limpiar();
					}	
				}else{
					try {
//						if(nufFacultad.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE){
//							nufListCarrera=servNufCarreraServicio.listarCarrerasYSuficienciaXFacultad(nufFacultad.getDpnId());
//						}else{
//							nufListCarrera=servNufCarreraServicio.listarCarrerasXFacultad(nufFacultad.getDpnId());
//						}
						
						//listo carreras cuando el caso que haya seleccionado docente dependiendo del nivel  académico
						if(nufRol.getRolId() == RolConstantes.ROL_DOCENTE_VALUE){
							if(nufNivelAcadémico == CarreraConstantes.TIPO_PREGRADO_VALUE){
								nufListCarrera=servNufCarreraServicio.buscarCarreras(nufFacultad.getDpnId(), CarreraConstantes.TIPO_PREGRADO_VALUE);
							} else if(nufNivelAcadémico == CarreraConstantes.TIPO_POSGRADO_VALUE){
								nufListCarrera=servNufCarreraServicio.buscarCarreras(nufFacultad.getDpnId(), CarreraConstantes.TIPO_POSGRADO_VALUE);
							} else if(nufNivelAcadémico == CarreraConstantes.TIPO_NIVELEACION_VALUE){
								nufListCarrera=servNufCarreraServicio.buscarCarreras(nufFacultad.getDpnId(), CarreraConstantes.TIPO_NIVELEACION_VALUE);
							} else if(nufNivelAcadémico == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
								nufListCarrera=servNufCarreraServicio.buscarCarreras(nufFacultad.getDpnId(), CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
							} else{
								nufListCarrera = null;
							}
						}else{
							nufListCarrera=servNufCarreraServicio.listarCarrerasXFacultad(nufFacultad.getDpnId());
						}
						
						nufHabilitadorCarrera=false;
					} catch (Exception e) {
						limpiar();
					}		
				}
				
			}
		}else{
			nufHabilitadorCarrera=true;
			nufHabilitadorGuardar=true;
		}
	}

	/**
	 * Método para cargar la lista de carreras de acuerdo a la facultad seleccionada
	 * 
	 */
	public void cambiarFacultadAgregarCarrera() {
		nufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
		nufHabilitadorGuardar=true;
		nufListUsuariosXCarreraXRol=null;
		nufListUsuariosXCarreraXRol = new ArrayList<UsuarioRolJdbcDto>();
		if(nufFacultad!=null && nufFacultad.getDpnId()!=GeneralesConstantes.APP_ID_BASE ){
			try {
				// Listamos las carreras por facultad
				nufListCarrera = servNufCarreraServicio.listarCarrerasXFacultad(nufFacultad.getDpnId());
				nufHabilitadorCarrera = false;
			} catch (Exception e) {
				limpiar();
			}	
		}else{
			nufHabilitadorCarrera=true;
			nufHabilitadorRol=true;
			nufHabilitadorGuardar=true;
		}
	}
	
	/**
	 * Método para cargar la lista de usuarios de acuerdo a la carrera seleccionada
	 * 
	 */
	public void cambiarCarrera() {
		nufHabilitadorDocente = false;
		nufHabiltaPuesto = false;
		nufHabilitaCategoria = true;
		nufHabilitaRango = true;
		nufTiempo = true;
		nufListPeriodoAcademicoPosgrado = null;
		nufCategoria = GeneralesConstantes.APP_ID_BASE;
		nufRango = GeneralesConstantes.APP_ID_BASE;
		nufTiempoDedicacion.setTmddId(GeneralesConstantes.APP_ID_BASE);
		nufRelacionLaboral.setRllbId(GeneralesConstantes.APP_ID_BASE);
		if(nufCarrera!=null && nufCarrera.getCrrId()!=GeneralesConstantes.APP_ID_BASE && nufCarrera.getCrrId()!=0){
			Carrera carrAux = new Carrera();
			try {
				carrAux = servNufCarreraServicio.buscarPorId(nufCarrera.getCrrId());
				nufCarrera.setCrrTipo(carrAux.getCrrTipo());
			} catch (CarreraNoEncontradoException e1) {
				e1.getMessage();
			} catch (CarreraException e1) {
				e1.getMessage();
			}
			
			List<Rol> listaRoles = new ArrayList<Rol>(); 
//			listaRoles = servNufRolServicio.listarRolesXUsrId(nufUsuarioCreacionDtoCrear.getUsrId());
			listaRoles = servNufRolServicio.listarRolesXUsrIdXRoflcr(nufUsuarioCreacionDtoCrearConsulta.getUsrId());
			if(listaRoles.size()!=0){
				boolean op = false;
				for (Rol rol : listaRoles) {
					if(nufRol.getRolId()==rol.getRolId()){
						try {
							RolFlujoCarrera roflcrrAux = servRolFlujoCarreraServicio.buscarPorCarrera(nufCarrera.getCrrId(),nufUsuarioCreacionDtoCrear.getUsrId(),nufRol.getRolId());
							if(roflcrrAux!=null){						
								op=true;
								break;		
							}
						} catch (RolFlujoCarreraNoEncontradoException e) {
						} catch (RolFlujoCarreraException e) {
						}
					}
				}
				if(op){
					nufHabilitadorGuardar=true;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("El usuario ingresado ya posee el rol en la carrera seleccionada.");
				}else{
					nufHabilitadorGuardar=false;
				}
			}else{
				nufHabilitadorGuardar=false;	
			}
		}else{
			
			nufHabilitadorGuardar=true;
		}
		nufPeriodoAcademicoPosgrado = new PeriodoAcademicoDto();
		nufPeriodoAcademicoPosgrado.setPracId(GeneralesConstantes.APP_ID_BASE);
		if(!nufHabilitadorGuardar){
			//buscar periodo de maestría
			if(nufCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE){
				try {
					nufListPeriodoAcademicoPosgrado = new ArrayList<>();
					nufListPeriodoAcademicoPosgrado = servPeriodoAcademicoDtoServicioJdbc.listarPeriodoPosgradoXCarrera(nufCarrera.getCrrId());
					if(nufListPeriodoAcademicoPosgrado.size() > 0){
						if(nufListPeriodoAcademicoPosgrado.size() == 1){
							for (PeriodoAcademicoDto item : nufListPeriodoAcademicoPosgrado) {
								nufPeriodoAcademicoPosgrado.setPracId(item.getPracId());
								nufPeriodoAcademicoPosgrado.setPracDescripcion(item.getPracDescripcion());
								nufPeriodoAcademicoPosgrado.setPracFechaIncio(item.getPracFechaIncio());
								nufPeriodoAcademicoPosgrado.setPracFechaFin(item.getPracFechaFin());
								nufPeriodoAcademicoPosgrado.setPracEstado(item.getPracEstado());
								nufPeriodoAcademicoPosgrado.setPracTipo(item.getPracTipo());
							}
						}
					}
				} catch (PeriodoAcademicoDtoJdbcException e) {
					e.getMessage();
				} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
					e.getMessage();
				}
			}
		}
		if(!nufHabilitadorGuardar && nufNivelAcadémico == CarreraConstantes.TIPO_PREGRADO_VALUE){
			try {
				nufListRelacionLaboral = new ArrayList<>();
				nufListRelacionLaboral= servRelacionLaboralServicio.listarTodos();
				nufHabiltaPuesto = true;
//				List<RelacionLaboral> listRelacionLabAux = new ArrayList<>();
//				listRelacionLabAux = servRelacionLaboralServicio.listarTodos();
//				for (RelacionLaboral item : listRelacionLabAux) {
//					if(item.getRllbId() != 2){
//						nufListRelacionLaboral.add(item);
//					}
//				}
			} catch (RelacionLaboralNoEncontradoException e) {
				e.getMessage();
			} catch (RelacionLaboralException e) {
				e.getMessage();
			}
		}
	}
	
	/**
	 * Método para cargar la lista de usuarios de acuerdo a la carrera seleccionada
	 * 
	 */
	public void cambiarCarreraAgregar() {
		nufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
		nufListUsuariosXCarreraXRol=null;
		nufListUsuariosXCarreraXRol=new ArrayList<UsuarioRolJdbcDto>();
		if(nufCarrera!=null && nufCarrera.getCrrId()!=GeneralesConstantes.APP_ID_BASE && nufCarrera.getCrrId()!=0){
			nufHabilitadorGuardar=false;
			nufListRol = servNufRolServicio.listarTodosparaAgregarCarreras();
			nufHabilitadorRol=false;
		}else{
			nufHabilitadorRol=true;
			nufHabilitadorGuardar=true;
		}
	}
	/**
	 * Método para cargar la lista de usuarios de acuerdo a la carrera seleccionada
	 * 
	 */
	public void cambiarRol() {
//		nufListUsuariosXCarreraXRol=null;
//		nufListUsuariosXCarreraXRol=new ArrayList<UsuarioRolJdbcDto>();
//		if(nufRol!=null && nufRol.getRolId()!=GeneralesConstantes.APP_ID_BASE && nufRol.getRolId()!=0){
//			nufHabilitadorCarrera=false;
//			try {
//				nufListUsuariosXCarreraXRol=servNufUsuarioRolDtoServicioJdbc.buscarUsuarioXFacultadXCarreraXRol(nufRol.getRolId(), nufFacultad.getDpnId(), nufCarrera.getCrrId());
//			}  catch (UsuarioRolJdbcDtoException e) {
//				limpiar();
//			} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
//				limpiar();
//			}	
//		}else{
//			nufHabilitadorGuardar=true;
//		}
		
		boolean estadoUsro = true;
		for (UsuarioRolJdbcDto itemRoles : nufListUsuarioXRol) {
			if(itemRoles.getRolId() == nufRol.getRolId() && itemRoles.getUsroEstado() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				estadoUsro = false;
				break;
			}
		}
		
		try {
			if(nufRol != null){
				nufRol.setRolDescripcion(servNufRolServicio.buscarPorId(nufRol.getRolId()).getRolDescripcion());
			}
		} catch (RolNoEncontradoException e1) {
			e1.getMessage();
		} catch (RolException e1) {
			e1.getMessage();
		}
		nufHabilitaNivelAcademico = false;
		nufNivelAcadémico = GeneralesConstantes.APP_ID_BASE;
		if(!estadoUsro){
			nufHabilitadorGuardar = true;
			nufHabilitadorCarrera = true;
			nufHabilitadorFacultad = true;
			nufFacultad.setDpnId(GeneralesConstantes.APP_ID_BASE);
			nufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
			
			FacesUtil.mensajeError("Debe activar el rol: "+nufRol.getRolDescripcion()+", para realizar la asignación");
		}else {
			nufFacultad.setDpnId(GeneralesConstantes.APP_ID_BASE);
			nufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
			nufListCarrera = null;
			nufHabilitadorDocente = false;
			if(nufRol.getRolId()==RolConstantes.ROL_SECRECARRERA_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DOCENTE_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DIRCARRERA_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_ADMINFACULTAD_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SECREABOGADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SECRESECREABOGADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_ADMINNIVELACION_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DECANO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SUBDECANO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_DIRPOSGRADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_CONSULTOREPORTES_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SECREPOSGRADO_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_SECRESUFICIENCIAS_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_INFORMATICOFACULTAD_VALUE
					||nufRol.getRolId()==RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE) {
				boolean habilita = true;
				boolean habilitaDocente = false;
				if(nufRol.getRolId()==RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE){
					habilita = false;
					for (UsuarioRolJdbcDto itemUsro : nufListUsuarioXRol) {
						if(itemUsro.getRolId() == RolConstantes.ROL_DECANO_VALUE || itemUsro.getRolId() == RolConstantes.ROL_SUBDECANO_VALUE){
							habilita = true;
							break;
						}
					}
					//busco solo facultades de acuerdo a la asignacion
					List<Dependencia> todasDep = new ArrayList<>();
					try {
						todasDep = servNufFacultadServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
					} catch (DependenciaNoEncontradoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					nufListFacultad = new ArrayList<>();
					for (Dependencia itemTodDep : todasDep) {
						for (UsuarioRolJdbcDto itemAsignadas : nufListUsuariosXCarreraXRol) {
							if(itemTodDep.getDpnId() == itemAsignadas.getFclId()){
								if(itemAsignadas.getRolId() == RolConstantes.ROL_DECANO_VALUE || itemAsignadas.getRolId() == RolConstantes.ROL_SUBDECANO_VALUE){
									nufListFacultad.add(itemTodDep);
									break;
								}
							}
						}
					}
//					nufListFacultad = servNufFacultadServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
					
				}else{
					try {
						nufListFacultad = servNufFacultadServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
						if(nufRol.getRolId()==RolConstantes.ROL_DOCENTE_VALUE){
							habilitaDocente = true;
						}
					} catch (DependenciaNoEncontradoException e) {
					}
				}
				if(habilita){
					nufHabilitadorGuardar=true;
					nufHabilitadorFacultad=false;
					if(habilitaDocente){
						nufHabilitaNivelAcademico = true;
						nufHabilitadorFacultad = true;
					}
				}else{
					nufHabilitadorGuardar=false;
					nufHabilitadorFacultad=true;
					nufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
					FacesUtil.mensajeError("No puede asignar este rol, debe tener el rol de Decano o Subdecano");
				}
			}else {
				nufHabilitadorDocente = true;

				List<Rol> listaRoles = new ArrayList<Rol>(); 
				listaRoles = servNufRolServicio.listarRolesXUsrId(nufUsuarioCreacionDtoCrear.getUsrId());
				if(listaRoles.size()!=0){
					boolean op = false;
					for (Rol rol : listaRoles) {
						if(nufRol.getRolId()==rol.getRolId()){
							op=true;
							break;
						}
					}
					if(op){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("El usuario ingresado ya posee el rol seleccionado.");
					}else{
						nufHabilitadorGuardar=false;
						nufHabilitadorFacultad=true;
						nufFacultad.setDpnId(GeneralesConstantes.APP_ID_BASE);
					}
					try {
						nufListaRolesUsuario= servNufUsuarioRolDtoServicioJdbc.buscarCarrerasXIdentificacion(nufUsuarioCreacionDtoCrear.getUsrIdentificacion());
						nufHabilitadorDocente=true;
					} catch (UsuarioRolJdbcDtoException e) {
					} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
					}
				}else{
					try {
						nufListaRolesUsuario= servNufUsuarioRolDtoServicioJdbc.buscarCarrerasXIdentificacion(nufUsuarioCreacionDtoCrear.getUsrIdentificacion());
						nufHabilitadorDocente=true;
					} catch (UsuarioRolJdbcDtoException e) {
					} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
					}
					nufHabilitadorGuardar=false;
					nufHabilitadorFacultad=true;
					nufFacultad.setDpnId(GeneralesConstantes.APP_ID_BASE);	
				}
			}
		}
	}
	
	/**
	 * Método para cargar la lista de carreras de acuerdo al tipo de rol seleccionado
	 * 
	 */
	public void cambiarNivel(){
		nufHabilitadorFacultad = false;
		nufHabilitaPeriodoPosgrado = false;
		nufFacultad.setDpnId(GeneralesConstantes.APP_ID_BASE);
		nufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
		nufListCarrera = null;
		nufListPeriodoAcademicoPosgrado = null;
		nufPeriodoAcademicoPosgrado.setPracId(GeneralesConstantes.APP_ID_BASE);
		try {
			if(nufNivelAcadémico != GeneralesConstantes.APP_ID_BASE){
				//listo facultades cuando el caso que haya seleccionado docente dependiendo del nivel  académico
				if(nufRol.getRolId() == RolConstantes.ROL_DOCENTE_VALUE) {
					if(nufNivelAcadémico == CarreraConstantes.TIPO_PREGRADO_VALUE){
						nufListFacultad = servNufFacultadServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
					} else if(nufNivelAcadémico == CarreraConstantes.TIPO_POSGRADO_VALUE){
						nufListFacultad = servNufFacultadServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_POSGRADO_VALUE);
					} else if(nufNivelAcadémico == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						nufListFacultad = servNufFacultadServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_NIVELEACION_VALUE);
					} else if(nufNivelAcadémico == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
						nufListFacultad = servNufFacultadServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
					} 
				}else{
					nufListFacultad = servNufFacultadServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				}
			}else{
				nufListFacultad = null;
			}
		} catch (DependenciaNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(nufNivelAcadémico == CarreraConstantes.TIPO_POSGRADO_VALUE){
			nufHabilitaPeriodoPosgrado = true;
			
		}
		if(nufNivelAcadémico != CarreraConstantes.TIPO_PREGRADO_VALUE){
			nufHabiltaPuesto = false;
			nufCategoria = GeneralesConstantes.APP_ID_BASE;
			nufRango = GeneralesConstantes.APP_ID_BASE;
			nufTiempoDedicacion.setTmddId(GeneralesConstantes.APP_ID_BASE);
			nufRelacionLaboral.setRllbId(GeneralesConstantes.APP_ID_BASE);
		}else{
			nufHabiltaPuesto = true;
		}
		
	}
	
	/**
	 * Método para desactivar la carrera del usuario seleccionado
	 * 
	 */
	public void desactivarUsuario(UsuarioRolJdbcDto entidad) {
		entidad.setPrsId(nufUsuarioCreacionDtoCrear.getPrsId());
		entidad.setPrsMailInstitucional(nufUsuarioCreacionDtoCrear.getPrsMailInstitucional());
		entidad.setUsrNick(nufUsuarioCreacionDtoCrear.getUsrNick());
		entidad.setUsrId(nufUsuarioCreacionDtoCrear.getUsrId());
		try {
			boolean estado = false;
			for (UsuarioRolJdbcDto item : nufListUsuarioXRol) {
				if(item.getRolId() == entidad.getRolId() && item.getUsroEstado() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					estado = true;
					break;
				}
			}
			if(!estado){
				servRolFlujoCarreraServicio.desactivarRolFlujoCarreraXUsuarioRol(entidad.getRoflcrId());
				//verificar si tiene detalle puesto al activar
				//buscar rol_flujo_carrera
				RolFlujoCarrera rolFlujoCarrera = new RolFlujoCarrera();
				rolFlujoCarrera = servRolFlujoCarreraServicio.buscarPorCarrera(entidad.getCrrId(), entidad.getUsrId(), entidad.getRolId());
				if(rolFlujoCarrera.getRoflcrEstado() == RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE){ //si se activa el estado de esta entidad, busco en detalle_puesto
					//busco ficha_docente
					FichaDocente fichaDocente = new FichaDocente();
					fichaDocente = servFichaDocenteServicio.buscarPorPrsId(entidad.getPrsId());
					if(fichaDocente != null){
						//busco la carrera
						Carrera carrera = new Carrera();
						carrera = servNufCarreraServicio.buscarPorId(entidad.getCrrId());
						//busco detalle_puesto
						DetallePuesto detallePuesto = new DetallePuesto();
						if(carrera != null){
							if(carrera.getCrrTipo() == CarreraConstantes.TIPO_PREGRADO_VALUE || carrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
								detallePuesto = servDetallePuestoServicio.buscarPorFcdcId(fichaDocente.getFcdcId(), carrera.getCrrTipo());
							}
							if(carrera.getCrrTipo() == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
								detallePuesto = servDetallePuestoServicio.buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioSuficienciasAux(fichaDocente.getFcdcId(), carrera.getCrrTipo(), carrera.getCrrDependencia().getDpnId());
							}
							if(carrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE){
								detallePuesto = servDetallePuestoServicio.buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioPosgradoAux(fichaDocente.getFcdcId(), carrera.getCrrTipo(), carrera.getCrrId());
							}
							if(detallePuesto == null){
								//crear el detalle puesto
								DetallePuesto detallePuestoAux = new DetallePuesto();
								detallePuestoAux.setDtpsFichaDocente(fichaDocente);
								detallePuestoAux.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
								detallePuestoAux.setDtpsCarrera(carrera);
								detallePuestoAux.setDtpsUsuario(nufUsuario.getUsrNick());
								if(servDetallePuestoServicio.agregar(detallePuestoAux)){
									System.out.println("DetallePuesto agregado");
								}
								
							}
						}
						
					}
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Debe activar el rol del usuario para activar la carrera");
			}
			
		} catch (RolFlujoCarreraException
				| RolFlujoCarreraNoEncontradoException e) {
		} catch (FichaDocenteNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FichaDocenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DetallePuestoNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DetallePuestoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CarreraNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CarreraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DetallePuestoValidacionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		irAdministrarCarreras(entidad, nufUsuario);
		
	}
	
	/**
	 * Método para desactivar la carrera del usuario seleccionado
	 * 
	 */
	public void desactivarUsuarioRol(UsuarioRolJdbcDto entidad) {
		entidad.setPrsId(nufUsuarioCreacionDtoCrear.getPrsId());
		entidad.setPrsMailInstitucional(nufUsuarioCreacionDtoCrear.getPrsMailInstitucional());
		entidad.setUsrNick(nufUsuarioCreacionDtoCrear.getUsrNick());
		entidad.setUsrId(nufUsuarioCreacionDtoCrear.getUsrId());
		try {
			if(entidad.getUsroEstado() == UsuarioRolConstantes.ESTADO_ACTIVO_VALUE){
				servUsuarioRolServicio.desactivarUsuarioRol(entidad.getUsroId());
			}
			if(entidad.getUsroEstado() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				servUsuarioRolServicio.activarUsuarioRol(entidad.getUsroId());
			}
			
		} catch (UsuarioRolException e) {
		}
		irAdministrarCarreras(entidad, nufUsuario);
		
	}
	
	
	/**
	 * Método para guardar el rol y la carrera asignada al usuario
	 * 
	 */
	public String guardarUsuario() {
		//se hace la inserción en la base de datos
		try {
			boolean estado = false;
			if(nufHabilitaPeriodoPosgrado && nufPeriodoAcademicoPosgrado.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe seleccionar el período académico");
				return null;
			}else {
				for (UsuarioRolJdbcDto item : nufListUsuarioXRol) {
					if(item.getRolId() == nufRol.getRolId() && item.getUsroEstado() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
						estado = true;
						break;
					}
				}
				if(!estado){
					//obtener el docente de Talento Humano
					nufDocenteTH = new DocenteTHJdbcDto();
//					nufDocenteTH = servDocenteTHDtoServicioJdbc.buscarDocenteTHXCedula(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
					
					
					if(nufNivelAcadémico == CarreraConstantes.TIPO_PREGRADO_VALUE && nufRelacionLaboral.getRllbId() == GeneralesConstantes.APP_ID_BASE 
							&& nufCategoria == GeneralesConstantes.APP_ID_BASE && nufRango == GeneralesConstantes.APP_ID_BASE && nufTiempoDedicacion.getTmddId() == GeneralesConstantes.APP_ID_BASE){
						FacesUtil.mensajeError("Debe seleccionar todos los parámetros");
						return null;
					}else{
						//buscar puesto pregrado
						Puesto puesto = new Puesto();
						if(nufNivelAcadémico == CarreraConstantes.TIPO_PREGRADO_VALUE){
							//enviar 
							//relacion_laboral
							//
							if(nufCategoria == PuestoEnum.PST_CATEGORIA_DOCENTE_HONORARIO.getValue() || nufCategoria == PuestoEnum.PST_CATEGORIA_DOCENTE_INVITADO.getValue() || nufCategoria == PuestoEnum.PST_CATEGORIA_DOCENTE_OCASIONAL.getValue()){
								nufRango = GeneralesConstantes.APP_ID_BASE;
							}
							
							puesto = servPuestoServicio.buscarPuesto(nufTiempoDedicacion.getTmddId(), nufCategoria, nufRango);
						}
						if(servRolFlujoCarreraServicio.anadirUsuarioRolFlujoCarrera(nufUsuarioCreacionDtoCrear,nufRol.getRolId(),nufCarrera.getCrrId(),nufFacultad.getDpnId(),nufNivelAcadémico, nufUsuario, nufDocenteTH, nufPeriodoAcademicoPosgrado, puesto, nufRelacionLaboral.getRllbId())){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Se ha creado el usuario con su rol respectivo.");
							limpiar();
							return "irInicio";	
						}else{
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Error al crear el usuario.");
							limpiar();
							return "irInicio";
						}
					}
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Debe activar el rol del usuario para la asignación de carrera");
					return null;
				}
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			limpiar();
			return "irInicio";
		}
		
		
	}
	
	/**
	 * Método para guardar el rol y la carrera asignada al usuario
	 * 
	 */
	public String guardarRolFlujoCarrera() {
		//se hace la inserción en la base de datos
		try {
			servRolFlujoCarreraServicio.anadirRolFlujoCarrera(nufUsuarioRolJdbcDtoBuscar,nufRol.getRolId(),nufCarrera.getCrrId());
		} catch (PersonaValidacionException
				| PersonaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			limpiar();
			return "irInicio";
		}catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al guardar la carrera en el usuario, el usuario ya tiene asignada la carrera con el rol seleccionado.");
			limpiar();
			return "irInicio";
		}
		
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeInfo("Se ha agregado la carrera seleccionada con el rol asignado al usuario actual");
		limpiar();
		return "irInicio";
	}
	
	public void buscarIdentificacion(){
		try {
			servPersonaServicio.buscarPorIdentificacion(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error, la identificación ingresada ya existe.");
			nufHabilitadorGuardar=true;
		} catch (PersonaNoEncontradoException e) {
			nufHabilitadorGuardar=false;
		} catch (PersonaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	public String verificarClickAceptarTramite(){
//		aufValidadorClic = 1;
//		return null;
//	}
//	
//	public String verificarClickAceptarTramiteNo(){
//		aufValidadorClic = 0;
//		return null;
//	}
	
	/**
	 * Método que direcciona al modal para editar el detalle puesto
	 * @param detallePuesto - detallePuesto entidad dto que contiene la información a editar
	 */
	public void irEditarDetallePuesto(DocenteJdbcDto detallePuesto){
		nufDocenteDetallePuestoEdit = new DocenteJdbcDto();
		nufDocenteDetallePuestoEdit = detallePuesto;
	}
	
	public void cambiarRelacionLaboral(){
		nufHabilitaCategoria = true;
		nufHabilitaRango = true;
		nufTiempo = true;
		nufCategoria = GeneralesConstantes.APP_ID_BASE;
		nufRango = GeneralesConstantes.APP_ID_BASE;
		nufTiempoDedicacion.setTmddId(GeneralesConstantes.APP_ID_BASE);
		if(nufRelacionLaboral.getRllbId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError("Debe seleccionar la relación laboral");
		}else{
			nufHabilitaCategoria = false;
			//buscar la categoria
//			nufListCategoria = 
		}
	}
	
	public void cambiarCategoria(){
		nufHabilitaRango = true;
		nufTiempo = true;
		nufRango = GeneralesConstantes.APP_ID_BASE;
		nufTiempoDedicacion.setTmddId(GeneralesConstantes.APP_ID_BASE);
		if(nufCategoria == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError("Debe seleccionar la categoria");
		}else{
			nufHabilitaRango = false;
		}
	}
	
	public void cambiarNivelRango(){
		try {
			nufTiempo = true;
			nufTiempoDedicacion.setTmddId(GeneralesConstantes.APP_ID_BASE);
			if(nufRango == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe seleccionar el nivel");
			}else{
				nufListTiempoDedicacion = new ArrayList<>();
				nufListTiempoDedicacion = servTiempoDedicacionServicio.listarTodos();
				nufTiempo = false;
			}
		} catch (TiempoDedicacionNoEncontradoException e) {
			e.getMessage();
		} catch (TiempoDedicacionException e) {
			e.getMessage();
		}
	}

	//****************************************************************/
	//******************* METODOS GET / SET **************************/
	//****************************************************************/	

	public List<UsuarioRolJdbcDto> getNufListUsuariosXCarreraXRol() {
		nufListUsuariosXCarreraXRol = nufListUsuariosXCarreraXRol==null?(new ArrayList<UsuarioRolJdbcDto>()):nufListUsuariosXCarreraXRol;
		return nufListUsuariosXCarreraXRol;
	}

	public void setNufListUsuariosXCarreraXRol(
			List<UsuarioRolJdbcDto> nufListUsuariosXCarreraXRol) {
		this.nufListUsuariosXCarreraXRol = nufListUsuariosXCarreraXRol;
	}

	public Dependencia getNufFacultad() {
		return nufFacultad;
	}

	public void setNufFacultad(Dependencia nufFacultad) {
		this.nufFacultad = nufFacultad;
	}

	public List<Dependencia> getNufListFacultad() {
		nufListFacultad = nufListFacultad==null?(new ArrayList<Dependencia>()):nufListFacultad;
		return nufListFacultad;
	}

	public void setNufListFacultad(List<Dependencia> nufListFacultad) {
		this.nufListFacultad = nufListFacultad;
	}

	public Carrera getNufCarrera() {
		return nufCarrera;
	}

	public void setNufCarrera(Carrera nufCarrera) {
		this.nufCarrera = nufCarrera;
	}

	public List<Carrera> getNufListCarrera() {
		nufListCarrera = nufListCarrera==null?(new ArrayList<Carrera>()):nufListCarrera;
		return nufListCarrera;
	}

	public void setNufListCarrera(List<Carrera> nufListCarrera) {
		this.nufListCarrera = nufListCarrera;
	}

	public UsuarioRolJdbcDto getNufUsuarioRolJdbcDtoBuscar() {
		return nufUsuarioRolJdbcDtoBuscar;
	}

	public void setNufUsuarioRolJdbcDtoBuscar(
			UsuarioRolJdbcDto nufUsuarioRolJdbcDtoBuscar) {
		this.nufUsuarioRolJdbcDtoBuscar = nufUsuarioRolJdbcDtoBuscar;
	}

	public List<UsuarioRolJdbcDto> getNufListaufUsuarioRolJdbcDtoBuscar() {
		nufListaufUsuarioRolJdbcDtoBuscar = nufListaufUsuarioRolJdbcDtoBuscar==null?(new ArrayList<UsuarioRolJdbcDto>()):nufListaufUsuarioRolJdbcDtoBuscar;
		return nufListaufUsuarioRolJdbcDtoBuscar;
	}

	public void setNufListaufUsuarioRolJdbcDtoBuscar(
			List<UsuarioRolJdbcDto> nufListaufUsuarioRolJdbcDtoBuscar) {
		this.nufListaufUsuarioRolJdbcDtoBuscar = nufListaufUsuarioRolJdbcDtoBuscar;
	}

	public boolean isNufHabilitadorCarrera() {
		return nufHabilitadorCarrera;
	}

	public void setNufHabilitadorCarrera(boolean nufHabilitadorCarrera) {
		this.nufHabilitadorCarrera = nufHabilitadorCarrera;
	}


	public UsuarioRolServicio getServUsuarioRolServicio() {
		return servUsuarioRolServicio;
	}

	public void setServUsuarioRolServicio(UsuarioRolServicio servUsuarioRolServicio) {
		this.servUsuarioRolServicio = servUsuarioRolServicio;
	}

	public DependenciaServicio getServNufFacultadServicio() {
		return servNufFacultadServicio;
	}

	public void setServNufFacultadServicio(DependenciaServicio servNufFacultadServicio) {
		this.servNufFacultadServicio = servNufFacultadServicio;
	}

	public CarreraServicio getServNufCarreraServicio() {
		return servNufCarreraServicio;
	}

	public void setServNufCarreraServicio(CarreraServicio servNufCarreraServicio) {
		this.servNufCarreraServicio = servNufCarreraServicio;
	}

	public UsuarioRolDtoServicioJdbc getServNufUsuarioRolDtoServicioJdbc() {
		return servNufUsuarioRolDtoServicioJdbc;
	}

	public void setServNufUsuarioRolDtoServicioJdbc(
			UsuarioRolDtoServicioJdbc servNufUsuarioRolDtoServicioJdbc) {
		this.servNufUsuarioRolDtoServicioJdbc = servNufUsuarioRolDtoServicioJdbc;
	}

	public UsuarioCreacionDto getNufUsuarioCreacionDtoCrear() {
		return nufUsuarioCreacionDtoCrear;
	}

	public void setNufUsuarioCreacionDtoCrear(
			UsuarioCreacionDto nufUsuarioCreacionDtoCrear) {
		this.nufUsuarioCreacionDtoCrear = nufUsuarioCreacionDtoCrear;
	}

	public UsuarioRolJdbcDto getNufUsuarioRolJdbcDtoCrear() {
		return nufUsuarioRolJdbcDtoCrear;
	}

	public void setNufUsuarioRolJdbcDtoCrear(
			UsuarioRolJdbcDto nufUsuarioRolJdbcDtoCrear) {
		this.nufUsuarioRolJdbcDtoCrear = nufUsuarioRolJdbcDtoCrear;
	}

	public Rol getNufRol() {
		return nufRol;
	}

	public void setNufRol(Rol nufRol) {
		this.nufRol = nufRol;
	}

	public List<Rol> getNufListRol() {
		nufListRol = nufListRol==null?(new ArrayList<Rol>()):nufListRol;
		return nufListRol;
	}

	public void setNufListRol(List<Rol> nufListRol) {
		this.nufListRol = nufListRol;
	}

	public List<Etnia> getNufListEtnias() {
		return nufListEtnias;
	}

	public void setNufListEtnias(List<Etnia> nufListEtnias) {
		this.nufListEtnias = nufListEtnias;
	}

	public List<UbicacionDto> getNufListPaises() {
		return nufListPaises;
	}

	public void setNufListPaises(List<UbicacionDto> nufListPaises) {
		this.nufListPaises = nufListPaises;
	}

	public boolean isNufHabilitadorRol() {
		return nufHabilitadorRol;
	}

	public void setNufHabilitadorRol(boolean nufHabilitadorRol) {
		this.nufHabilitadorRol = nufHabilitadorRol;
	}

	public boolean isNufHabilitadorGuardar() {
		return nufHabilitadorGuardar;
	}

	public void setNufHabilitadorGuardar(boolean nufHabilitadorGuardar) {
		this.nufHabilitadorGuardar = nufHabilitadorGuardar;
	}


	public boolean isNufHabilitadorFacultad() {
		return nufHabilitadorFacultad;
	}


	public void setNufHabilitadorFacultad(boolean nufHabilitadorFacultad) {
		this.nufHabilitadorFacultad = nufHabilitadorFacultad;
	}


	public boolean isNufHabilitadorDocente() {
		return nufHabilitadorDocente;
	}


	public void setNufHabilitadorDocente(boolean nufHabilitadorDocente) {
		this.nufHabilitadorDocente = nufHabilitadorDocente;
	}


	public List<UsuarioRolJdbcDto> getNufListaRolesUsuario() {
		return nufListaRolesUsuario;
	}


	public void setNufListaRolesUsuario(List<UsuarioRolJdbcDto> nufListaRolesUsuario) {
		this.nufListaRolesUsuario = nufListaRolesUsuario;
	}


	public UsuarioCreacionDto getNufUsuarioCreacionDtoCrearConsulta() {
		return nufUsuarioCreacionDtoCrearConsulta;
	}


	public void setNufUsuarioCreacionDtoCrearConsulta(UsuarioCreacionDto nufUsuarioCreacionDtoCrearConsulta) {
		this.nufUsuarioCreacionDtoCrearConsulta = nufUsuarioCreacionDtoCrearConsulta;
	}


	public List<UsuarioRolJdbcDto> getNufListUsuarioXRol() {
		nufListUsuarioXRol = nufListUsuarioXRol==null?(new ArrayList<UsuarioRolJdbcDto>()):nufListUsuarioXRol;
		return nufListUsuarioXRol;
	}


	public void setNufListUsuarioXRol(List<UsuarioRolJdbcDto> nufListUsuarioXRol) {
		this.nufListUsuarioXRol = nufListUsuarioXRol;
	}


	public Integer getNufNivelAcadémico() {
		return nufNivelAcadémico;
	}


	public void setNufNivelAcadémico(Integer nufNivelAcadémico) {
		this.nufNivelAcadémico = nufNivelAcadémico;
	}


	public boolean isNufHabilitaNivelAcademico() {
		return nufHabilitaNivelAcademico;
	}


	public void setNufHabilitaNivelAcademico(boolean nufHabilitaNivelAcademico) {
		this.nufHabilitaNivelAcademico = nufHabilitaNivelAcademico;
	}

	public Usuario getNufUsuario() {
		return nufUsuario;
	}
	public void setNufUsuario(Usuario nufUsuario) {
		this.nufUsuario = nufUsuario;
	}


	public DocenteTHJdbcDto getNufDocenteTH() {
		return nufDocenteTH;
	}

	public void setNufDocenteTH(DocenteTHJdbcDto nufDocenteTH) {
		this.nufDocenteTH = nufDocenteTH;
	}
	public List<DocenteJdbcDto> getNufListDetallePuestoDocente() {
		nufListDetallePuestoDocente = nufListDetallePuestoDocente==null?(new ArrayList<DocenteJdbcDto>()):nufListDetallePuestoDocente;
		return nufListDetallePuestoDocente;
	}
	public void setNufListDetallePuestoDocente(List<DocenteJdbcDto> nufListDetallePuestoDocente) {
		this.nufListDetallePuestoDocente = nufListDetallePuestoDocente;
	}
	public DocenteJdbcDto getNufDocenteDetallePuestoEdit() {
		return nufDocenteDetallePuestoEdit;
	}
	public void setNufDocenteDetallePuestoEdit(DocenteJdbcDto nufDocenteDetallePuestoEdit) {
		this.nufDocenteDetallePuestoEdit = nufDocenteDetallePuestoEdit;
	}
	public List<PeriodoAcademicoDto> getNufListPeriodoAcademicoPosgrado() {
		nufListPeriodoAcademicoPosgrado = nufListPeriodoAcademicoPosgrado==null?(new ArrayList<PeriodoAcademicoDto>()):nufListPeriodoAcademicoPosgrado;
		return nufListPeriodoAcademicoPosgrado;
	}
	public void setNufListPeriodoAcademicoPosgrado(List<PeriodoAcademicoDto> nufListPeriodoAcademicoPosgrado) {
		this.nufListPeriodoAcademicoPosgrado = nufListPeriodoAcademicoPosgrado;
	}
	public PeriodoAcademicoDto getNufPeriodoAcademicoPosgrado() {
		return nufPeriodoAcademicoPosgrado;
	}
	public void setNufPeriodoAcademicoPosgrado(PeriodoAcademicoDto nufPeriodoAcademicoPosgrado) {
		this.nufPeriodoAcademicoPosgrado = nufPeriodoAcademicoPosgrado;
	}
	public boolean isNufHabilitaPeriodoPosgrado() {
		return nufHabilitaPeriodoPosgrado;
	}
	public void setNufHabilitaPeriodoPosgrado(boolean nufHabilitaPeriodoPosgrado) {
		this.nufHabilitaPeriodoPosgrado = nufHabilitaPeriodoPosgrado;
	}
	public List<RelacionLaboral> getNufListRelacionLaboral() {
		nufListRelacionLaboral = nufListRelacionLaboral==null?(new ArrayList<RelacionLaboral>()):nufListRelacionLaboral;
		return nufListRelacionLaboral;
	}
	public void setNufListRelacionLaboral(List<RelacionLaboral> nufListRelacionLaboral) {
		this.nufListRelacionLaboral = nufListRelacionLaboral;
	}
	public List<TiempoDedicacion> getNufListTiempoDedicacion() {
		nufListTiempoDedicacion = nufListTiempoDedicacion==null?(new ArrayList<TiempoDedicacion>()):nufListTiempoDedicacion;
		return nufListTiempoDedicacion;
	}
	public void setNufListTiempoDedicacion(List<TiempoDedicacion> nufListTiempoDedicacion) {
		this.nufListTiempoDedicacion = nufListTiempoDedicacion;
	}
	public List<Puesto> getNufListCategoria() {
		nufListCategoria = nufListCategoria==null?(new ArrayList<Puesto>()):nufListCategoria;
		return nufListCategoria;
	}
	public void setNufListCategoria(List<Puesto> nufListCategoria) {
		this.nufListCategoria = nufListCategoria;
	}
	public RelacionLaboral getNufRelacionLaboral() {
		return nufRelacionLaboral;
	}
	public void setNufRelacionLaboral(RelacionLaboral nufRelacionLaboral) {
		this.nufRelacionLaboral = nufRelacionLaboral;
	}
	public TiempoDedicacion getNufTiempoDedicacion() {
		return nufTiempoDedicacion;
	}
	public void setNufTiempoDedicacion(TiempoDedicacion nufTiempoDedicacion) {
		this.nufTiempoDedicacion = nufTiempoDedicacion;
	}
	public Puesto getNufPuesto() {
		return nufPuesto;
	}
	public void setNufPuesto(Puesto nufPuesto) {
		this.nufPuesto = nufPuesto;
	}
	public boolean isNufHabiltaPuesto() {
		return nufHabiltaPuesto;
	}
	public void setNufHabiltaPuesto(boolean nufHabiltaPuesto) {
		this.nufHabiltaPuesto = nufHabiltaPuesto;
	}


	public boolean isNufHabilitaCategoria() {
		return nufHabilitaCategoria;
	}


	public void setNufHabilitaCategoria(boolean nufHabilitaCategoria) {
		this.nufHabilitaCategoria = nufHabilitaCategoria;
	}


	public Integer getNufCategoria() {
		return nufCategoria;
	}


	public void setNufCategoria(Integer nufCategoria) {
		this.nufCategoria = nufCategoria;
	}


	public boolean isNufHabilitaRango() {
		return nufHabilitaRango;
	}


	public void setNufHabilitaRango(boolean nufHabilitaRango) {
		this.nufHabilitaRango = nufHabilitaRango;
	}


	public boolean isNufTiempo() {
		return nufTiempo;
	}


	public void setNufTiempo(boolean nufTiempo) {
		this.nufTiempo = nufTiempo;
	}


	public Integer getNufRango() {
		return nufRango;
	}


	public void setNufRango(Integer nufRango) {
		this.nufRango = nufRango;
	}


	
	
}
