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
   
 ARCHIVO:     ConsultaUsuarioForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la consulta de usuarios
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
04-SEPT-2018			Dennis Collaguazo 				Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte;

import java.io.Serializable;
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

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ConsultaUsuarioForm.
 * Managed Bean que maneja las peticiones para la consulta de usuarios
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="consultaUsuarioForm")
@SessionScoped
public class ConsultaUsuarioForm implements Serializable{

	private static final long serialVersionUID = 8930514798128102692L;
	
	//*******************************************************************/
	//******************* ATRIBUTOS DE LA CLASE *************************/
	//*******************************************************************/
	
	//GENERAL
	private Usuario ammfUsuario;
	private Integer ammfTipoUsuario;
	private String ammfTipoCarrera;
	
	//PARA BUSQUEDA
	private List<Dependencia> ammfListFacultadBusq;
	private List<Carrera> ammfListCarreraBusq;
	private MallaCurricularDto ammfMallaCurricularDtoBusq;
	private Rol ammfRolBusq;
	private List<Rol> ammfListRolBusq;
	private int ammfEstadoRoflcr;
	private List<UsuarioRolJdbcDto> ammfListUsuarioRolBusq;
	private int ammfPostgradoBusq;
	private List<Carrera> ammfListPostgradoBusq;
	private boolean ammfHabilitaSinRoflcrBusq;
	
	//PARA HABILITAR REPORTE
	private Integer ammfValidadorClic;
	
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
	UsuarioRolServicio servAmmfUsuarioRolServicio;
	@EJB 
	DependenciaServicio servAmmfDependenciaServicio;
	@EJB 
	CarreraServicio servAmmfCarreraServicio;
	@EJB
	RolServicio servAmmfRolServicio;
	@EJB
	UsuarioRolDtoServicioJdbc serAmmfUsuarioRolDtoServicioJdbc;
	
	// ********************************************************************/
	// ************* METODOS GENERALES DE LA CLASE ************************/
	// ********************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarUsuario
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar usuario
	 */
	public String irListarUsuario(Usuario usuario){
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			//ANULO LA LISTA DE FACULTADES
			ammfListFacultadBusq = null;
			//INSTANCIO LA MALLA CURRICULAR DTO
			ammfMallaCurricularDtoBusq = new MallaCurricularDto();
			iniciarParametros(); //inicializa los parametros
			List<UsuarioRol> usroLista = servAmmfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usroLista) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					ammfTipoUsuario = RolConstantes.ROL_ADMINDGA_VALUE.intValue();
					ammfTipoCarrera="carreras";
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
					ammfTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
					ammfTipoCarrera="programas";
				}
			}
			ammfListFacultadBusq = servAmmfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);//busca la lista de facultades activas	
			ammfListRolBusq = servAmmfRolServicio.listarTodos();
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.irMallaMateria.dependencia.no.encontrado.Exception")));
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.irMallaMateria.usuarioRol.exception")));
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.irMallaMateria.usuarioRol.no.encontrado.exception")));
		}
		return "irListarUsuarioConsulta";
	}
	
	/**
	 * Método que busca la carrera por facultad
	 * @param idFacultad - idFacultad id de la facultad a buscar
	 */
	public void cambiarCarrera(int idFacultad){
		try {
			//SETEO LA CARRERA
			ammfMallaCurricularDtoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MALLA
			ammfMallaCurricularDtoBusq.setMlcrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL NIVEL
			ammfMallaCurricularDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ROL
			ammfRolBusq.setRolId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ESTADO
			ammfEstadoRoflcr = GeneralesConstantes.APP_ID_BASE;
			//ANULO LA LISTA DE USUARIO ROL
			ammfListUsuarioRolBusq = null;
			//LISTO LAS CARRERAS POR FACULTAD
			if(ammfTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || ammfTipoUsuario == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || ammfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				ammfListCarreraBusq =  servAmmfCarreraServicio.listarCarrerasXFacultad(idFacultad);
			}else if(ammfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				ammfListCarreraBusq = servAmmfCarreraServicio.listarCarrerasXFacultadPosgrado(idFacultad);
			}
			ammfListPostgradoBusq = servAmmfCarreraServicio.listarCarrerasXFacultadPosgrado(idFacultad);
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
//	public void cambiarRol(){
//		ammfRolBusq.setRolId(GeneralesConstantes.APP_ID_BASE);
//		if(ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE || ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
//			ammfListRolBusq = null;
//			FacesUtil.mensajeError("Debe seleccionar parámetros anteriores");
//		}else{
//			ammfListRolBusq = servAmmfRolServicio.listarTodos();
//		}
//		
//	}
	public void cambiarRol(){
		//ANULO LA LISTA DE USUARIO ROL
		ammfListUsuarioRolBusq = null;
		
	}
	
	public void cambiarEstado(){
		ammfEstadoRoflcr = GeneralesConstantes.APP_ID_BASE;
		//ANULO LA LISTA DE USUARIO ROL
		ammfListUsuarioRolBusq = null;
	}

	/**
	 * Método que busca usuarios por facultad y por carrera rol y estado
	 */
	public void buscar(){
		try {
			//VERIFICO QUE HA SELECCIONADO UNO DE ESTOS ROLES PARA OTRO TIPO DE CONSULTA
			if(ammfRolBusq.getRolId() == RolConstantes.ROL_ADMIN_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_SOPORTE_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_DIRRRHH_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINNIVELACION_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINDGA_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINDPP_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_DIRVINCULACIONSOCIEDAD_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_COORDINVESTIGACION_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_PRESCOMITEETICA_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_COORDGESTION_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_DIRINVESTIGACION_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ESTUD_PREGRADO_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ACTUALIZACIONDOCENTE_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_COORDINADORAREA_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_MOVILIDADESTUDIANTIL_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINREPORTEPREGRADO_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINSEGCALIDADPREGRADO_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINREPORTEPOSGRADO_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINSEGCALIDADPOSGRADO_VALUE
					|| ammfRolBusq.getRolId() == RolConstantes.ROL_ADMINFACULTADPOSGRADO_VALUE					){
				//BUSCO EL USUARIO O USUARIOS POR LOS PARAMETROS DE BUSQUEDA
				ammfListUsuarioRolBusq = serAmmfUsuarioRolDtoServicioJdbc.buscarUsuarioXrolIdXestado(ammfRolBusq.getRolId(), ammfEstadoRoflcr);
				ammfHabilitaSinRoflcrBusq = true;
			}else{
//				//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
//				if(ammfMallaCurricularDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
//					FacesUtil.mensajeError("Debe seleccionar la facultad");
//				}else {
//					//BUSCO EL USUARIO O USUARIOS POR LOS PARAMETROS DE BUSQUEDA
//					ammfListUsuarioRolBusq = serAmmfUsuarioRolDtoServicioJdbc.buscarUsuarioXFacultadXCarreraXRolXEstado(ammfMallaCurricularDtoBusq.getDpnId(), ammfMallaCurricularDtoBusq.getCrrId(), ammfPostgradoBusq, ammfRolBusq.getRolId(), ammfEstadoRoflcr);
//					ammfHabilitaSinRoflcrBusq = false;
//				}
				//BUSCO EL USUARIO O USUARIOS POR LOS PARAMETROS DE BUSQUEDA
				ammfListUsuarioRolBusq = serAmmfUsuarioRolDtoServicioJdbc.buscarUsuarioXFacultadXCarreraXRolXEstado(ammfMallaCurricularDtoBusq.getDpnId(), ammfMallaCurricularDtoBusq.getCrrId(), ammfPostgradoBusq, ammfRolBusq.getRolId(), ammfEstadoRoflcr);
				ammfHabilitaSinRoflcrBusq = false;
			}
		} catch (UsuarioRolJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 *Método que limpia los parametros de busqueda
	 */
	public void limpiar(){
		iniciarParametros(); //inicaliza los parametros
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}

	/**
	 * Método que genera el reporte
	 */
	public void generarReporteUsuarios(List<UsuarioRolJdbcDto> listaUsuario){
		try {
			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE HORARIO *********//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "reporteUsuarios";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);

			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoUsuario = null;

			for (UsuarioRolJdbcDto item : listaUsuario) {
				datoUsuario = new HashMap<String, Object>();
				datoUsuario.put("identificacion", item.getPrsIdentificacion());
				datoUsuario.put("apellido1", item.getPrsPrimerApellido() +" "+ item.getPrsSegundoApellido());
				datoUsuario.put("nombre", item.getPrsNombres());
				datoUsuario.put("nick", item.getUsrNick());
				if(ammfHabilitaSinRoflcrBusq){
					datoUsuario.put("facultad", "N/D");
					datoUsuario.put("carrera", "N/D");
				}else{
					datoUsuario.put("facultad", item.getFclDescripcion());
					datoUsuario.put("carrera", item.getCrrDescripcion());
				}
				datoUsuario.put("rol", item.getRolDetalle());
				
				if(ammfHabilitaSinRoflcrBusq){
					if(item.getUsroEstado() == UsuarioRolConstantes.ESTADO_ACTIVO_VALUE){
						datoUsuario.put("estado", UsuarioRolConstantes.ESTADO_ACTIVO_LABEL);
					}else if(item.getUsroEstado() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
						datoUsuario.put("estado", UsuarioRolConstantes.ESTADO_INACTIVO_LABEL);
					}
				}else{
					if(item.getRoflcrEstado() == RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE){
						datoUsuario.put("estado", RolFlujoCarreraConstantes.ESTADO_ACTIVO_LABEL);
					}else if(item.getRoflcrEstado() == RolFlujoCarreraConstantes.ESTADO_INACTIVO_VALUE){
						datoUsuario.put("estado", RolFlujoCarreraConstantes.ESTADO_INACTIVO_LABEL);
					}
				}
				frmRrmCampos.add(datoUsuario);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/soporte/administracionUsuario");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");

			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		//INSTANCIO LA MALLA CURRICULAR DTO
		ammfMallaCurricularDtoBusq = new MallaCurricularDto();
		//INSTANCIO LOS ROLES
		ammfRolBusq = new Rol();
		//ANULO LA LISTA DE CARRERAS
		ammfListCarreraBusq = null;
		//ANULO LA LISTA DE USUARIO ROL
		ammfListUsuarioRolBusq = null;
		//ANULO LA LISTA DE POSTGRADOS
		ammfListPostgradoBusq = null;
		//SETEO LA FACULTAD
		ammfMallaCurricularDtoBusq.setDpnId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA
		ammfMallaCurricularDtoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA MALLA
		ammfMallaCurricularDtoBusq.setMlcrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL ESTADO
		ammfMallaCurricularDtoBusq.setMlcrmtEstado(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL UNIDAD DE FORMACION
		ammfMallaCurricularDtoBusq.setUnfrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL NIVEL
		ammfMallaCurricularDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL ID DE POSTGRADO
		ammfPostgradoBusq = GeneralesConstantes.APP_ID_BASE;
		//SETEO EL ESTADO ROFLCR
		ammfEstadoRoflcr = GeneralesConstantes.APP_ID_BASE;
		//INICIALIZO LA HABILITACIÓN DEL PANEL
		ammfHabilitaSinRoflcrBusq = false;
		//BLOQUEA EL MODAL
		bloqueaModal();
	}
	
	//HABILITA PARA LA IMPRESIÓN
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del excel
	 */
	public void verificarClickImprimir(){
		if(ammfListUsuarioRolBusq.size() > 0){
			generarReporteUsuarios(ammfListUsuarioRolBusq);
			habilitaModalImpresion();
		}else{
			FacesUtil.mensajeError("No existe información para presentar");
			bloqueaModal();
		}
	}
	
	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		ammfValidadorClic = 0;
	}
	
	//HABILITA MODAL IMPRESION DE EXCEL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		ammfValidadorClic = 1;
	}
	
	// ********************************************************************/
	// *********************** METODOS GET Y SET **************************/
	// ********************************************************************/
	public Usuario getAmmfUsuario() {
		return ammfUsuario;
	}
	public void setAmmfUsuario(Usuario ammfUsuario) {
		this.ammfUsuario = ammfUsuario;
	}
	public List<Dependencia> getAmmfListFacultadBusq() {
		ammfListFacultadBusq = ammfListFacultadBusq==null?(new ArrayList<Dependencia>()):ammfListFacultadBusq;
		return ammfListFacultadBusq;
	}
	public void setAmmfListFacultadBusq(List<Dependencia> ammfListFacultadBusq) {
		this.ammfListFacultadBusq = ammfListFacultadBusq;
	}
	public List<Carrera> getAmmfListCarreraBusq() {
		ammfListCarreraBusq = ammfListCarreraBusq==null?(new ArrayList<Carrera>()):ammfListCarreraBusq;
		return ammfListCarreraBusq;
	}
	public void setAmmfListCarreraBusq(List<Carrera> ammfListCarreraBusq) {
		this.ammfListCarreraBusq = ammfListCarreraBusq;
	}
	public MallaCurricularDto getAmmfMallaCurricularDtoBusq() {
		return ammfMallaCurricularDtoBusq;
	}
	public void setAmmfMallaCurricularDtoBusq(MallaCurricularDto ammfMallaCurricularDtoBusq) {
		this.ammfMallaCurricularDtoBusq = ammfMallaCurricularDtoBusq;
	}
	public Rol getAmmfRolBusq() {
		return ammfRolBusq;
	}
	public void setAmmfRolBusq(Rol ammfRolBusq) {
		this.ammfRolBusq = ammfRolBusq;
	}
	public List<Rol> getAmmfListRolBusq() {
		ammfListRolBusq = ammfListRolBusq==null?(new ArrayList<Rol>()):ammfListRolBusq;
		return ammfListRolBusq;
	}
	public void setAmmfListRolBusq(List<Rol> ammfListRolBusq) {
		this.ammfListRolBusq = ammfListRolBusq;
	}
	public int getAmmfEstadoRoflcr() {
		return ammfEstadoRoflcr;
	}
	public void setAmmfEstadoRoflcr(int ammfEstadoRoflcr) {
		this.ammfEstadoRoflcr = ammfEstadoRoflcr;
	}
	public List<UsuarioRolJdbcDto> getAmmfListUsuarioRolBusq() {
		ammfListUsuarioRolBusq = ammfListUsuarioRolBusq==null?(new ArrayList<UsuarioRolJdbcDto>()):ammfListUsuarioRolBusq;
		return ammfListUsuarioRolBusq;
	}
	public void setAmmfListUsuarioRolBusq(List<UsuarioRolJdbcDto> ammfListUsuarioRolBusq) {
		this.ammfListUsuarioRolBusq = ammfListUsuarioRolBusq;
	}
	public Integer getAmmfTipoUsuario() {
		return ammfTipoUsuario;
	}
	public void setAmmfTipoUsuario(Integer ammfTipoUsuario) {
		this.ammfTipoUsuario = ammfTipoUsuario;
	}
	public String getAmmfTipoCarrera() {
		return ammfTipoCarrera;
	}
	public void setAmmfTipoCarrera(String ammfTipoCarrera) {
		this.ammfTipoCarrera = ammfTipoCarrera;
	}
	public int getAmmfPostgradoBusq() {
		return ammfPostgradoBusq;
	}
	public void setAmmfPostgradoBusq(int ammfPostgradoBusq) {
		this.ammfPostgradoBusq = ammfPostgradoBusq;
	}
	public List<Carrera> getAmmfListPostgradoBusq() {
		return ammfListPostgradoBusq;
	}
	public void setAmmfListPostgradoBusq(List<Carrera> ammfListPostgradoBusq) {
		this.ammfListPostgradoBusq = ammfListPostgradoBusq;
	}
	public boolean isAmmfHabilitaSinRoflcrBusq() {
		return ammfHabilitaSinRoflcrBusq;
	}
	public void setAmmfHabilitaSinRoflcrBusq(boolean ammfHabilitaSinRoflcrBusq) {
		this.ammfHabilitaSinRoflcrBusq = ammfHabilitaSinRoflcrBusq;
	}
	public Integer getAmmfValidadorClic() {
		return ammfValidadorClic;
	}
	public void setAmmfValidadorClic(Integer ammfValidadorClic) {
		this.ammfValidadorClic = ammfValidadorClic;
	}
}
