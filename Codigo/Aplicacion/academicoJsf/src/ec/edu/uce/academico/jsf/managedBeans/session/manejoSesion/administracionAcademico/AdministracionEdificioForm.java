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
   
 ARCHIVO:     AdministracionEdificioForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de  Edificio.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
21-08-2017			Edgar Ulloa                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EdificioDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EdificioServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Edificio;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionEdificioForm.
 * Managed Bean que maneja las peticiones para la administración de la tabla Edificio.
 * @author emulloa.
 * @version 1.0
 */

@ManagedBean(name="administracionEdificioForm")
@SessionScoped
public class AdministracionEdificioForm implements Serializable{

	private static final long serialVersionUID = -397102404723196895L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	//GENERAL
	private Usuario aefUsuario;
	
	//PARA BUSQUEDA
	private DependenciaDto aefDependenciaDto;
	private List<DependenciaDto> aefListaDependenciaDto;
	private List<EdificioDto> aefListEdificioDtoBusq;
	
	//PARA EDICION
	private EdificioDto aefEdificioEdit;
	
	//PARA HABILITAR
	private Integer aefValidadorClic;
	
	//PARA NUEVO
	private EdificioDto aefEdificioNuevo;
	
	//PARA ELIMINAR
	private EdificioDto aefEdificioEliminar;
	
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
	UsuarioRolServicio servAefUsuarioRolServicio;
	@EJB
	DependenciaDtoServicioJdbc servAefDependenciaDtoServicioJdbc;
	@EJB 
	EdificioDtoServicioJdbc servAefEdificioDtoServicioJdbc;
	@EJB 
	EdificioServicio servAefEdificioServicio;
	@EJB
	DependenciaServicio servAefDependenciaServicio;
	@EJB 
	AulaDtoServicioJdbc servAefAulaDtoServicioJdbc;
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/

	/**
	 * Método que permite iniciar la administración del edificio
	 * @param usuario - el usuario que ingresa 
	 * @return  Navegacion a la pagina xhtml de Administración edificio.
	 */
	public String irAdministracionEdificio(Usuario usuario) { 
		aefUsuario= usuario;
		iniciarParametros();
		try {
			//busca usuario activo con el rol correspondiente
			UsuarioRol usro = servAefUsuarioRolServicio.buscarXUsuarioXrol(aefUsuario.getUsrId(), RolConstantes.ROL_ADMINFACULTAD_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.usuario.rol.estado.desactivado")));
				return null;
			}
			aefListaDependenciaDto = new ArrayList<DependenciaDto>();
			//busca dependencias de acuerdo al usuario
			aefListaDependenciaDto = servAefDependenciaDtoServicioJdbc.listarXUsuario(aefUsuario.getUsrId());
			//busca lisa de edificios de aucerdo a la dependencia
			aefListEdificioDtoBusq = servAefEdificioDtoServicioJdbc.listarXDependencia(GeneralesConstantes.APP_ID_BASE, aefListaDependenciaDto);
		}  catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EdificioDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EdificioDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
		return "irAdministracionEdificio";
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}

	/**
	 * Método para limpiar los parámetros de busqueda ingresados
	 * @return 
	 */
	public void limpiar()  {
		iniciarParametros();
		try {
			//busca lisa de edificios de aucerdo a la dependencia
			aefListEdificioDtoBusq = servAefEdificioDtoServicioJdbc.listarXDependencia(GeneralesConstantes.APP_ID_BASE, aefListaDependenciaDto);
		} catch (EdificioDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EdificioDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método que busca la lista de edificios por id de dependencia
	 */
	public void buscar(int idDependencia){
		try {
			//busca lisa de edificios de aucerdo a la dependencia
			aefListEdificioDtoBusq = servAefEdificioDtoServicioJdbc.listarXDependencia(idDependencia, aefListaDependenciaDto);
		} catch (EdificioDtoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EdificioDtoNoEncontradoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	} 
	
	/**
	 * Método que navega hacia la pagina de editar edificio
	 * @param entidadEditar - entidadEditar entidad edificio ha editar
	 * @return  Navegacion a la pagina de editar edificio.
	 */
	public String irEditarEdificio(EdificioDto entidadEditar){
		String retorno = null;
		aefEdificioEdit = entidadEditar;
		retorno = "irEditarEdificio";
		return retorno;
	}
	
	/**
	 * Método que navega hacia la página de listar edificio
	 * @return  Navegacion a la pagina de listar edificio.
	 */
	public String irListarEdificio(){
		String retorno = null;
		limpiar();
		retorno = "irListarEdificio";
		return retorno;
	}
	
	/**
	 * Método que guarda el editar de edificios
	 * @return retorna hacia la navegación de la pagina de listar
	 */
	public String guardarEditar(){
		String retorno = null;
		try {
			//INSTANCIO EL EDIFICIO PARA GUARDAR EL EDITAR
			Edificio entidad = new Edificio();
			//SETEO LOS VALOES A EDITAR
			entidad.setEdfId(aefEdificioEdit.getEdfId());
//			entidad.setEdfCodigo(aefEdificioEdit.getEdfCodigo().trim().toUpperCase());
			entidad.setEdfDescripcion(aefEdificioEdit.getEdfDescripcion().trim().toUpperCase());
//			entidad.setEdfLocalizacion(aefEdificioEdit.getEdfLocalizacion());
			entidad.setEdfEstado(aefEdificioEdit.getEdfEstado());
			entidad.setEdfDependencia(servAefDependenciaServicio.buscarPorId(aefEdificioEdit.getDpnId()));
			//PROCESO QUE EDITA LA INFORMACIÓN DEL EDIFICIO
			if(servAefEdificioServicio.editar(entidad)){ // si se guarda la edición devuelve verdadero
				iniciarParametros(); //inicializa parametros
				aefListEdificioDtoBusq = servAefEdificioDtoServicioJdbc.listarXDependencia(GeneralesConstantes.APP_ID_BASE, aefListaDependenciaDto);
				retorno = "irListarEdificio";
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.guardar.editar.exitoso")));
			}else{ // caso que no se ejecute el editar
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.guardar.editar.no.guardado")));
			}
			bloqueaModal(); //bloquea el modal para que no se ejecute nuevamente
		} catch (EdificioException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que navega hacia la pagina de nuevo edificio
	 * @return  Navegacion a la pagina de nuevo edificio.
	 */
	public String irNuevoEdificio(){
		String retorno = null;
		limpiar();
		retorno = "irNuevoEdificio";
		return retorno;
	}
	
	/**
	 * Método que guarda el nuevo edificio
	 * @return retorna hacia la navegación de la pagina de listar
	 */
	public String guardarNuevo(){
		String retorno = null;
		try {
			//INSTANCIO EL EDIFICIO PARA GUARDAR EL NUEVO
			Edificio entidad = new Edificio();
			//SETEO LOS VALOES PARA NUEVO
//			entidad.setEdfCodigo(aefEdificioNuevo.getEdfCodigo().trim().toUpperCase());
			entidad.setEdfDescripcion(aefEdificioNuevo.getEdfDescripcion().trim().toUpperCase());
//			entidad.setEdfLocalizacion(aefEdificioNuevo.getEdfLocalizacion());
			entidad.setEdfEstado(aefEdificioNuevo.getEdfEstado());
			entidad.setEdfDependencia(servAefDependenciaServicio.buscarPorId(aefEdificioNuevo.getDpnId()));
			//PROCESO QUE AGREGA UN NUEVO EDIFICIO
			if(servAefEdificioServicio.nuevo(entidad)){ // si se guarda la edición devuelve verdadero
				iniciarParametros(); //inicializa parametros
				aefListEdificioDtoBusq = servAefEdificioDtoServicioJdbc.listarXDependencia(GeneralesConstantes.APP_ID_BASE, aefListaDependenciaDto);
				retorno = "irListarEdificio";
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.guardar.nuevo.exitoso")));
			}else{ // caso que no se ejecute
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.guardar.nuevo.no.guardado")));
			}
			bloqueaModal(); //bloquea el modal para que no se ejecute nuevamente
		} catch (EdificioException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Metodo que elimina el edificio seleccionado
	 */
	public void eliminar(){
		try {
			if(aefEdificioEliminar.getEdfId() != 0){ //verifico que no sea nulo o vacio
				//PROCESO DE ELIMINAR EDIFICIO
				if(servAefEdificioServicio.eliminar(aefEdificioEliminar.getEdfId())){ //devuelve verdadero si ejecuta la transacción
					iniciarParametros(); //inicializa parametros
					aefListEdificioDtoBusq = servAefEdificioDtoServicioJdbc.listarXDependencia(GeneralesConstantes.APP_ID_BASE, aefListaDependenciaDto);
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.eliminado.exitoso")));
				}else{ // caso que no se ejecute la transacción
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.eliminado.no.ejecutado")));
				}
			}else{ // caso que sea nulo o vacio
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.eliminado.no.seleccionado")));
			}
			bloqueaModal(); // bloquea el modal para que no se ejecute nuevamente
		} catch (EdificioException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
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
	 * Método para iniciar los parametros
	 */
	public void iniciarParametros() {
		//INSTANCIO LA FACULTAD
		aefDependenciaDto = new DependenciaDto();
		//INSTANCIO EL EDIFICIO
		aefEdificioNuevo = new EdificioDto();
		//SETEO LA FACULTAD
		aefDependenciaDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
		//ANULO LA LISTA DE EDIFICIOS
		aefListEdificioDtoBusq = null;
		//SETEO EL CODIGO
		aefEdificioNuevo.setEdfCodigo("");
		//SETEO LA DESCRIPCION
		aefEdificioNuevo.setEdfDescripcion("");
		//SETEO LA LOCALIZACIÓN
		aefEdificioNuevo.setEdfLocalizacion(null);
		//SETEO EL ESTADO
		aefEdificioNuevo.setEdfEstado(GeneralesConstantes.APP_ID_BASE);
		//ASIGNO VALOR INICIAL PARA EJECUCION DE MODALES
		bloqueaModal();
	}
	
	//HABILITA EL MODAL PARA GUARDAR EDITAR
	/**
	 * Verifica que haga click en el boton editar edificio 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable aefValidadorClic
	 * estado para poder editar  edificio
	 */
	public String verificarClickGuardarEdicion(){
		//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
//		if(aefEdificioEdit.getEdfCodigo() == "" || aefEdificioEdit.getEdfCodigo() == null){
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.edicion.codigo.no.seleccionado")));
//		} 
		if(aefEdificioEdit.getDpnDescripcion() == "" || aefEdificioEdit.getDpnDescripcion() == null){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.edicion.descripcion.no.seleccionado")));
//		} else if(aefEdificioEdit.getEdfLocalizacion() == GeneralesConstantes.APP_ID_BASE || aefEdificioEdit.getEdfLocalizacion() == null){
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.edicion.localizacion.no.seleccionado")));
		} else if(aefEdificioEdit.getEdfEstado() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.edicion.estado.no.seleccionado")));
		} else{
			//HABILITO EL MODAL DE EDITAR
			habilitaModalEditar();
		}
		return null;
	}
	
	//HABILITA EL MODAL PARA GUARDAR NUEVO
	/**
	 * Verifica que haga click en el boton nuevo edificio 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable aefValidadorClic
	 * estado para poder editar  edificio
	 */
	public String verificarClickGuardarNuevo(){
		//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
		if(aefEdificioNuevo.getDpnId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.nuevo.facultad.no.seleccionado")));
//		} else if(aefEdificioNuevo.getEdfCodigo() == ""){
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.nuevo.codigo.no.seleccionado")));
		} else if(aefEdificioNuevo.getDpnDescripcion() == ""){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.nuevo.descripcion.no.seleccionado")));
//		} else if(aefEdificioNuevo.getEdfLocalizacion() == null){
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.nuevo.localizacion.no.seleccionado")));
		} else if(aefEdificioNuevo.getEdfEstado() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.guardar.nuevo.estado.no.seleccionado")));
		} else{
			//HABILITO EL MODAL DE NUEVO
			habilitaModalNuevo();
		}
		return null;
	}
	
	//HABILITA EL MODAL PARA ELIMINAR
	/**
	 * Verifica que haga click en el boton eliminar edificio 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable aefValidadorClic
	 * estado para poder eliminar  edificio
	 */
	public String verificarClickEliminar(EdificioDto entidadEliminar){
		try {
			aefEdificioEliminar = entidadEliminar;
			if(!servAefAulaDtoServicioJdbc.buscarPIdEdificio(aefEdificioEliminar.getEdfId())){
				//HABILITO EL MODAL DE NUEVO
				habilitaModalEliminar();
			}else{
				bloqueaModal();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.edificio.verificar.eliminar.no.eliminado")));
			}
		} catch (AulaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return null;
	}
	
	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		aefValidadorClic = 0;
	}
	
	//HABILITA EL MODAL PARA EDITAR
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar edición
	 */
	public void habilitaModalEditar(){
		aefValidadorClic = 1;
	}
	
	//HABILITA EL MODAL PARA NUEVO
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar edición
	 */
	public void habilitaModalNuevo(){
		aefValidadorClic = 2;
	}
	
	//HABILITA EL MODAL PARA ELIMINAR
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar edición
	 */
	public void habilitaModalEliminar(){
		aefValidadorClic = 3;
	}

	// ********************************************************************/
	// *********************** METODOS GET Y SET **************************/
	// ********************************************************************/
	public Usuario getAefUsuario() {
		return aefUsuario;
	}
	public void setAefUsuario(Usuario aefUsuario) {
		this.aefUsuario = aefUsuario;
	}
	public DependenciaDto getAefDependenciaDto() {
		return aefDependenciaDto;
	}
	public void setAefDependenciaDto(DependenciaDto aefDependenciaDto) {
		this.aefDependenciaDto = aefDependenciaDto;
	}
	public List<DependenciaDto> getAefListaDependenciaDto() {
		aefListaDependenciaDto = aefListaDependenciaDto==null?(new ArrayList<DependenciaDto>()):aefListaDependenciaDto;
		return aefListaDependenciaDto;
	}
	public void setAefListaDependenciaDto(List<DependenciaDto> aefListaDependenciaDto) {
		this.aefListaDependenciaDto = aefListaDependenciaDto;
	}
	public List<EdificioDto> getAefListEdificioDtoBusq() {
		aefListEdificioDtoBusq = aefListEdificioDtoBusq==null?(new ArrayList<EdificioDto>()):aefListEdificioDtoBusq;
		return aefListEdificioDtoBusq;
	}
	public void setAefListEdificioDtoBusq(List<EdificioDto> aefListEdificioDtoBusq) {
		this.aefListEdificioDtoBusq = aefListEdificioDtoBusq;
	}
	public EdificioDto getAefEdificioEdit() {
		return aefEdificioEdit;
	}
	public void setAefEdificioEdit(EdificioDto aefEdificioEdit) {
		this.aefEdificioEdit = aefEdificioEdit;
	}
	public Integer getAefValidadorClic() {
		return aefValidadorClic;
	}
	public void setAefValidadorClic(Integer aefValidadorClic) {
		this.aefValidadorClic = aefValidadorClic;
	}
	public EdificioDto getAefEdificioNuevo() {
		return aefEdificioNuevo;
	}
	public void setAefEdificioNuevo(EdificioDto aefEdificioNuevo) {
		this.aefEdificioNuevo = aefEdificioNuevo;
	}
	public EdificioDto getAefEdificioEliminar() {
		return aefEdificioEliminar;
	}
	public void setAefEdificioEliminar(EdificioDto aefEdificioEliminar) {
		this.aefEdificioEliminar = aefEdificioEliminar;
	}
}
	
	
	
	
	
	
	
	
	
	
