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
   
 ARCHIVO:     AdministracionMallaMateriaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de malla curricular materia.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
04-AGOS-2017			Dennis Collaguazo 				Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UnidadFormacionException;
import ec.edu.uce.academico.ejb.excepciones.UnidadFormacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UnidadFormacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.UnidadFormacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionMallaMateriaForm.
 * Managed Bean que maneja las peticiones para la administración de la malla curricular materia.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="administracionMallaMateriaForm")
@SessionScoped
public class AdministracionMallaMateriaForm implements Serializable{

	private static final long serialVersionUID = 8930514798128102692L;
	
	//*******************************************************************/
	//******************* ATRIBUTOS DE LA CLASE *************************/
	//*******************************************************************/
	
	//GENERAL
	private Usuario ammfUsuario;
	private Integer ammfCarrera;
	private Integer ammfTipoUsuario;
	private String ammfTipoCarrera;
	
	//PARA BUSQUEDA
	private List<Dependencia> ammfListFacultadBusq;
	private Dependencia ammfFacultadBusq;
	private Carrera ammfCarreraBusq;
	private List<Carrera> ammfListCarreraBusq;
	private MallaCurricularDto ammfMallaCurricularDtoBusq;
	private List<MallaCurricularDto> ammfListMallaCurricularDtoBusq;
	private List<MallaCurricularDto> ammfListMallaMateriaBusq;
	private List<Nivel> ammfListNivelBusq;
	
	//PARA VER
	private MallaCurricularDto ammfMallaMateriaVer;
	
	//PARA EDITAR
	private MallaCurricularDto ammfMallaMateriaEdit;
	private List<UnidadFormacion> ammfListUnidadFormacionEdit;
	private List<Nivel> ammfListNivelEdit;
	
	//PARA HABILITAR
	private Integer ammfValidadorClic;
	
	//PARA AGREGAR
	private List<MateriaDto> ammfListMateriaAgr;
	private MateriaDto ammfMateriaBusq;
	private List<MateriaDto> ammfListMateriaBusq;
	
	//PARA ELIMINAR
	private MallaCurricularDto ammfMallaMateriaEliminar;
	
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
	MallaCurricularDtoServicioJdbc servAmmfMallaCurricularDtoServicioJdbc;
	@EJB 
	MallaMateriaDtoServicioJdbc servAmmfMallaMateriaDtoServicioJdbc;
	@EJB 
	NivelServicio servAmmfNivelServicio;
	@EJB 
	UnidadFormacionServicio servAmmfUnidadFormacionServicio;
	@EJB
	MallaCurricularMateriaServicio servAmmfMallaCurricularMateriaServicio;
	@EJB
	MateriaDtoServicioJdbc servAmmfMateriaDtoServicioJdbc;
	@EJB
	MallaCurricularParaleloServicio servAmmfMallaCurricularParaleloServicio;
	
	// ********************************************************************/
	// ************* METODOS GENERALES DE LA CLASE ************************/
	// ********************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarMallaMateria
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar malla materia
	 */
	public String irMallaMateria(Usuario usuario){
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			//INSTANCIO LA FACULTAD
			ammfFacultadBusq = new Dependencia();
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
//			ammfListFacultadBusq = servAmmfDependenciaServicio.listarFacultadesxUsuario(usuario.getUsrId());//busca la lista de facultades asignadas al usuario		
			ammfListFacultadBusq = servAmmfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);//busca la lista de facultades activas	
			
//			UsuarioRol usro = servAmmfUsuarioRolServicio.buscarXUsuarioXrol(usuario.getUsrId(),RolConstantes.ROL_ADMINDGA_VALUE.intValue()); //busca el usuario rol del usuario
//			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){ //verifica si el usuario esta activo
//				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.ir.malla.materia.usuario.inactivo")));
//				return null;
//			}else if(usro.getUsroRol().getRolId() != RolConstantes.ROL_ADMINDGA_VALUE.intValue()){
//				UsuarioRol usro1 = servAmmfUsuarioRolServicio.buscarXUsuarioXrol(usuario.getUsrId(),RolConstantes.ROL_ADMINDPP_VALUE.intValue()); //busca el usuario rol del usuario
//				if(usro1.getUsroRol().getRolId() != RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.ir.malla.materia.usuario.inactivo")));
//					return null;	
//				}
//				
//			}else{
//				
//			}

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
		if(ammfTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()){
			return "irMallaMateria";
		}else{
			return "irMallaMateriaPosgrado";
		}
		
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
			//ANULO LA LISTA DE RESULTADOS
			ammfListMallaMateriaBusq = null;
			//LISTO LAS CARRERAS POR FACULTAD
			if(ammfTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || ammfTipoUsuario == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || ammfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				ammfListCarreraBusq =  servAmmfCarreraServicio.listarCarrerasXFacultad(idFacultad);
			}else if(ammfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				ammfListCarreraBusq = servAmmfCarreraServicio.listarCarrerasXFacultadPosgrado(idFacultad);
			}
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca la malla por facultad y por carrera
	 * @param idFacultad - idFacultad id de la facultad a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 */
	public void cambiarMalla(int idCarrera){
		try {
			//SETEO LA MALLA
			ammfMallaCurricularDtoBusq.setMlcrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL NIVEL
			ammfMallaCurricularDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE RESULTADOS
			ammfListMallaMateriaBusq = null;
			//LISTO LAS MALLAS POR CARRERA
			ammfListMallaCurricularDtoBusq =  servAmmfMallaCurricularDtoServicioJdbc.listarMallasXCarreraXVigenciaXEstadoXestadoPeriodo(ammfMallaCurricularDtoBusq.getCrrId(), MallaCurricularConstantes.VIGENTE_MALLA_SI_VALUE, MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE,PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (MallaCurricularDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca el nivel
	 * @param idFacultad - idFacultad id de la facultad a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 */
	public void cambiarNivel(){
		try {
			//SETEO EL NIVEL
			ammfMallaCurricularDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE RESULTADOS
			ammfListMallaMateriaBusq = null;
			//LISTO LOS NIVELES
			if(ammfTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || ammfTipoUsuario == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || ammfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				ammfListNivelBusq = servAmmfNivelServicio.listarTodos();
			}else if(ammfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
				ammfListNivelBusq = servAmmfNivelServicio.listarTodosPosgrado();
			}
			
		} catch (NivelNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método que busca la malla materias por facultad y por carrera y malla
	 */
	public void buscar(){
		try {
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ammfMallaCurricularDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.facultad")));
			}else if(ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.carrera")));
			}else if(ammfMallaCurricularDtoBusq.getMlcrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.malla")));
			}else if(ammfMallaCurricularDtoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.nivel")));
			}else {
				//BUSCO LAS MALLAS CURRICULARES MATERIA - ES DECIR LAS MALLAS CON LAS MATERIAS INGRESADAS POR LOS PARAMETROS DE BUSQUEDA
				ammfListMallaMateriaBusq = servAmmfMallaMateriaDtoServicioJdbc.listarMallasMateriasXIdMallaXEstadoPeriodo(ammfMallaCurricularDtoBusq.getMlcrId(), MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE, MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE, ammfMallaCurricularDtoBusq.getNvlId(), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			}
		} catch (MallaCurricularDtoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			iniciarParametros();
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
	 * Navega hacia la página de ver malla curricular materia pero enviando el dto de malla materia
	 * @param mallaMateria - mallaMateria entidad dto de malla curricular materia
	 * @return  Navegacion a la pagina de ver malla materia.
	 */
	public String irVerMallaMateria(MallaCurricularDto mallaMateria){
		String retorno = null;
		try {
			ammfMallaMateriaVer = servAmmfMallaMateriaDtoServicioJdbc.buscarXId(mallaMateria.getMlcrmtId()); //busca las mallas materias por id de materia
			if(ammfMallaMateriaVer != null){ //si el resultado de la consulta es distinto de nulo 
				retorno = "irVerMallaMateria";
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.irVerMallaMateria.exception")));
			}
		} catch (MallaCurricularDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Navega hacia la página de listar malla curricular materia pero enviando el dto de malla materia
	 * @return  Navegacion a la pagina de listar malla materia.
	 */
	public String irListarMallaMateria(){
		String retorno = null;
		//iniciarParametros(); //inicializa los parametros
		retorno = "irListarMallaMateria";
		return retorno;
	}
	
	/**
	 * Método que navega hacia la pagina de editar malla curricular materia
	 * @param entidadEditar - entidadEditar entidad malla curricular materia ha editar
	 * @return  Navegacion a la pagina de editar malla materia.
	 */
	public String irEditarMallaMateria(MallaCurricularDto entidadEditar){
		String retorno = null;
		try {
			//ASIGNO A LA MALLA MATERIA PARA EDITAR
			ammfMallaMateriaEdit = entidadEditar;
			ammfListUnidadFormacionEdit = servAmmfUnidadFormacionServicio.listarTodos(); //busco las unidades de formación
			ammfListNivelEdit = ammfListNivelBusq; // asigno al nivel editar el de busqueda
			retorno = "irEditarMallaMateria";
		} catch (UnidadFormacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UnidadFormacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}

	/**
	 * Método que guarda el editar de la malla curricular materia
	 * @return retorna hacia la navegación de la pagina de listar
	 */
	public String guardarEditar(){
		String retorno = null;
		try {
			//INSTANCIO LA MALLA CURRICULAR MATERIA
			MallaCurricularMateria entidad = new MallaCurricularMateria();
			//SETEO DE VALORES
			entidad.setMlcrmtId(ammfMallaMateriaEdit.getMlcrmtId());  //seteo el id
			entidad.setMlcrmtEstado(ammfMallaMateriaEdit.getMlcrmtEstado()); //seteo el estado
			entidad.setMlcrmtUnidadFormacion(servAmmfUnidadFormacionServicio.buscarPorId(ammfMallaMateriaEdit.getUnfrId())); //seteo la unidad de formación
			entidad.setMlcrmtNivel(servAmmfNivelServicio.buscarPorId(ammfMallaMateriaEdit.getNvlId())); //seteo el nivel
			//PROCESO QUE EDITA LA INFORMACIÓN DE MALLA CURRICULAR MATERIA
			if(servAmmfMallaCurricularMateriaServicio.editar(entidad)){ // si se guarda la edición devuelve verdadero
				//iniciarParametros(); //inicializa parametros
				buscar();
				retorno = "irMallaMateria";
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.editar.exitoso")));
			}else{ // caso que no se ejecute el editar
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.editar.no.ejecutado")));
			}
			bloqueaModal(); //bloquea el modal para que no se ejecute nuevamente
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.editar.malla.curricular.materia.exception")));
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.editar.exception")));
		}
		return retorno;
	}
	
	/**
	 * Método que dirige a la pantalla de agregar nueva malla curricular materia
	 * @return envia a la pagina para agregara malla curricular materia
	 */
	public String irAgregar(){
		String retorno = null;
		try {
			iniciarParametros(); //inicializa los parametros
			ammfListUnidadFormacionEdit = servAmmfUnidadFormacionServicio.listarTodos(); //lista todas las unidades de formación
			ammfListNivelBusq = servAmmfNivelServicio.listarTodos(); //lista todos los niveles
			retorno = "irAgregarMallaMateria";
		} catch (UnidadFormacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UnidadFormacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que busca la lista de materias por carrera y por descripción
	 */
	public void buscarMateria(){
		try {
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ammfMallaCurricularDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.facultad")));
			}else if(ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.carrera")));
			}else if(ammfMallaCurricularDtoBusq.getMlcrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.malla")));
			}else if(ammfMallaCurricularDtoBusq.getMlcrmtEstado() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.estado")));
			}else if(ammfMallaCurricularDtoBusq.getUnfrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.unidad.formacion")));
			}else if(ammfMallaCurricularDtoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.nivel")));
			}else {
				// INSTANCIO LA LISTA DE MATERIAS A BUSCAR
				ammfListMateriaBusq = new ArrayList<MateriaDto>();
				List<MateriaDto> listAux = null; //anulo la lista auxiliar a buscar
				//BUSCO LAS MATERIAS POR CARRERA Y DESCRIPCIÓN
				listAux = servAmmfMateriaDtoServicioJdbc.listarXDescripcionXcarrera(ammfMateriaBusq.getMtrDescripcion(), ammfMallaCurricularDtoBusq.getCrrId());
				//COMPARO SI LA LISTA DE MATERIAS AGREGADAS ESTA VACIA
				if(ammfListMateriaAgr.size() <= 0){ //si esta vacia
					ammfListMateriaBusq = listAux; //asigno la lista auxiliar a la de busqueda
				}else{ //no esta vacia
					//ELIMINO LAS MATERIAS AGREGADAS EN EL FORM PARA LA BUSQUEDA
					Iterator<MateriaDto> it =  listAux.iterator();
					while(it.hasNext()){
						Integer aux= it.next().getMtrId();
						Iterator<MateriaDto> itMateria = ammfListMateriaAgr.iterator();
						while(itMateria.hasNext()){
							if(itMateria.next().getMtrId() == aux){
								it.remove();
							}
						}
					}
					ammfListMateriaBusq = listAux; // asigno la lista auxiliar a la de busqueda para mostrar
				}
			}
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que agrega materias a la lista
	 * @param materia - materia entidad dto materia para ser agregada
	 */
	public void agregarMateria(MateriaDto materia){
		//PROCESO PARA AGREGAR MATERIAS TEMPORALES DE LA LISTA
		ammfListMateriaAgr.add(materia);
		//ANULO LA LISTA
		ammfListMateriaBusq = null;
		bloqueaModal();  //bloquea el modal para que no se ejecute nuevamente
	}
	
	/**
	 * Método que elimina materias de la lista
	 * @param materia - materia entidad dto de materia ha ser eliminada
	 */
	public void eliminarMateria(MateriaDto materia){
		//PROCESO PARA ELIMINAR MATERIAS TEMPORALES DE LA LISTA
		ammfListMateriaAgr.remove(materia);
	}
	
	/**
	 * Método que instancia la lista de busquedas
	 */
	public void actualizaBusquedaMateria(){
		//INSTANCIO LA LISTA DE MATERIAS A BUSCAR
		ammfListMateriaBusq = new ArrayList<MateriaDto>();
		bloqueaModal(); // bloquea el modal para que no se ejecute nuevamente
	}
	
	/**
	 * Método que limpia el modal de busqueda de materias
	 */
	public void limpiarModal(){
		//SETEO LA DESCRIPCIÓN DE LA MATERIA
		ammfMateriaBusq.setMtrDescripcion("");
		buscarMateria(); //busco la materia
	}
	
	/**
	 * Método que guarda una nueva malla curricular materia
	 * @return retorna la navegación de la página de listar mallas curriculares
	 */
	public String guardarNuevo(){
		String retorno = null;
		try {
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ammfMallaCurricularDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.facultad")));
			}else if(ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.carrera")));
			}else if(ammfMallaCurricularDtoBusq.getMlcrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.malla")));
			}else if(ammfMallaCurricularDtoBusq.getMlcrmtEstado() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.estado")));
			}else if(ammfMallaCurricularDtoBusq.getUnfrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.unidad.formacion")));
			}else if(ammfMallaCurricularDtoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.nivel")));
			}else if(ammfListMateriaAgr.size() <= 0){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.agregar.materias")));
			}else{
				// PROCESO PARA AGREGAR UNA NUEVA MALLA CURRICULAR MATERIA
				if(servAmmfMallaCurricularMateriaServicio.nuevo(ammfMallaCurricularDtoBusq.getUnfrId(), ammfMallaCurricularDtoBusq.getNvlId(), ammfMallaCurricularDtoBusq.getMlcrId(), ammfListMateriaAgr, ammfMallaCurricularDtoBusq.getMlcrmtEstado())){ //devuelve verdadero si se ejecuta la transacción
					iniciarParametros(); // inicializa todos los parámetros
					retorno = "irListarMallaMateria";
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.nuevo.exitoso")));
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.nuevo.no.ejecutado")));
				}
			}
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.guardar.nuevo.malla.curricular.materia.exception")));
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.nuevo.editar.exception")));
		}
		bloqueaModal(); //bloquea el modal para que no se despliegue nuevamente
		return retorno;
	}
	
	/**
	 * Metodo que elimina una malla curricular materia seleccionada
	 */
	public void eliminarMallaMateria(){
		try {
			if(ammfMallaMateriaEliminar.getMlcrmtId() != 0){ //verifico que no sea nulo o vacio
				//PROCESO DE ELIMINAR MALLA CURRICULAR MATERIA
				if(servAmmfMallaCurricularMateriaServicio.eliminar(ammfMallaMateriaEliminar.getMlcrmtId())){ //devuelve verdadero si ejecuta la transacción
					buscar(); //busca las materias para actualizar el panel
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.eliminar.exitoso")));
				}else{ // caso que no se ejecute la transacción
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.eliminar.no.ejecutado")));
				}
			}else{ // caso que sea nulo o vacio
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.eliminar.parametro.vacio.no.ejecutado")));
			}
			bloqueaModal(); // bloquea el modal para que no se ejecute nuevamente
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.eliminar.malla.curricular.materia.exception")));
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.eliminar.exception")));
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
		//INSTANCIO LA CARRERA DTO
		ammfCarreraBusq = new Carrera();
		//INSTANCIO LA MALLA CURRICULAR DTO
		ammfMallaCurricularDtoBusq = new MallaCurricularDto();
		//INSTANCIO LA MALLA MATERIA DTO
		ammfMallaMateriaVer = new MallaCurricularDto();
		//INSTANCIO LA MATERIA DTO
		ammfMateriaBusq = new MateriaDto();
		//INSTANCIO LA MALLA CURRICULAR MATERIA
		ammfMallaMateriaEliminar = new MallaCurricularDto();
		//ANULO LA LISTA DE CARRERAS
		ammfListCarreraBusq = null;
		//ANULO LA LISTA DE MALLA CURRICULAR		
		ammfListMallaCurricularDtoBusq = null;
		//ANULO LA LISTA DE MALLA MATERIA
		ammfListMallaMateriaBusq = null;
		//ANULO LA LISTA DE MATERIA AGREGAR
		ammfListMateriaAgr = null;
		//ANULO LA LISTA DE MATERIAS BUSCAR
		ammfListMateriaBusq = null;
		//ANULO LA LISTA DE NIVEL
		ammfListNivelBusq = null;
		//ANULO LA LISTA DE UNIDAD DE FORMACION
		ammfListUnidadFormacionEdit = null;
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
		//SETEO LA DESCRIPCIÓN DE MATERIA
		ammfMateriaBusq.setMtrDescripcion("");
		//ASIGNO VALOR INICIAL PARA EJECUCION DE MODALES
		bloqueaModal();
	}
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton editar malla curricular materias 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ammfValidadorClic
	 * estado para poder editar malla curricular materia
	 */
	public String verificarClickGuardarEdicion(){
		//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
		if(ammfMallaMateriaEdit.getMlcrmtEstado() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.estado")));
		} else if(ammfMallaMateriaEdit.getUnfrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.unidad.formacion")));
		} else if(ammfMallaMateriaEdit.getNvlId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.nivel")));
		} else{
			//HABILITO EL MODAL DE EDITAR
			habilitaModal();
		}
		return null;
	}
	
	//HABILITA EL MODAL BUSCAR
	/**
	 * Verifica que haga click en el boton editar malla curricular materias 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ammfValidadorClic
	 * estado para poder editar malla curricular materia
	 */
	public String verificarClickBuscarMateria(){
		//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
		if(ammfMallaCurricularDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.facultad")));
		}else if(ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.carrera")));
		}else if(ammfMallaCurricularDtoBusq.getMlcrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.malla")));
		}else if(ammfMallaCurricularDtoBusq.getMlcrmtEstado() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.estado")));
		}else if(ammfMallaCurricularDtoBusq.getUnfrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.unidad.formacion")));
		}else if(ammfMallaCurricularDtoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.nivel")));
		}else {
			//HABILITO EL MODAL DE BUSCAR
			habilitaModalBuscarMateria();
		}
		return null;
	}
	
	//HABILITA EL MODAL GUARDAR NUEVO
	/**
	 * Verifica que haga click en el boton guardar nueva malla curricular materia 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ammfValidadorClic
	 * estado para poder guardar una nueva malla curricular materia
	 */
	public String verificarClickGuardarNuevo(){
		//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
		if(ammfMallaCurricularDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.facultad")));
		}else if(ammfMallaCurricularDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.carrera")));
		}else if(ammfMallaCurricularDtoBusq.getMlcrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.malla")));
		}else if(ammfMallaCurricularDtoBusq.getMlcrmtEstado() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.estado")));
		}else if(ammfMallaCurricularDtoBusq.getUnfrId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.unidad.formacion")));
		}else if(ammfMallaCurricularDtoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.nivel")));
		}else if(ammfListMateriaAgr.size() <= 0){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.agregar.materias")));
		}else{
			//HABILITO EL MODAL DE GUARDAR NUEVO
			habilitaModalGuardarNuevo();
		}
		return null;
	}
	
	//HABILITA EL MODAL DE ELIMINAR MALLA MATERIA
	/**
	 * Verifica que haga click en el boton guardar nueva malla curricular materia 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ammfValidadorClic
	 * estado para poder guardar una nueva malla curricular materia
	 */
	public String verificarClickEliminarMallaMateria(MallaCurricularDto entidad){
		ammfMallaMateriaEliminar = entidad;
		try {
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ammfMallaMateriaEliminar.getMlcrmtId() == 0){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.seleccion.malla.materia")));
			} else if(servAmmfMallaCurricularParaleloServicio.buscarXMallaMateria(ammfMallaMateriaEliminar.getMlcrmtId())){ //si encuentra mallas curriculares paralelo ya registrados
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.malla.materia.eliminar.malla.materia.no.ejecutado")));
			}else{
				//HABILITO EL MODAL DE ELIMINAR
				habilitaModalEliminarMallaMateria();
			}
		} catch (MallaCurricularParaleloNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloException e) {
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
		ammfValidadorClic = 0;
	}
	
	//HABILITA EL MODAL
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar edición
	 */
	public void habilitaModal(){
		ammfValidadorClic = 1;
	}
	
	//HABILITA EL MODAL PARA BUSCAR MATERIAS
	/**
	 * Método que habilita el modal para busqueda de materias
	 */
	public void habilitaModalBuscarMateria(){
		ammfValidadorClic = 2;
	}
	
	//HABILITA EL MODAL PARA GUARDAR NUEVO
	/**
	 * Método que habilita el modal para guardar una nueva maalla curricular materia
	 */
	public void habilitaModalGuardarNuevo(){
		ammfValidadorClic = 3;
	}
	
	//HABILITA EL MODAL PARA ELIMINAR
	/**
	 * Método que habilita el modal para eliminar  una malla curricular materia
	 */
	public void habilitaModalEliminarMallaMateria(){
		ammfValidadorClic = 4;
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
	public Integer getAmmfCarrera() {
		return ammfCarrera;
	}
	public void setAmmfCarrera(Integer ammfCarrera) {
		this.ammfCarrera = ammfCarrera;
	}
	public List<Dependencia> getAmmfListFacultadBusq() {
		ammfListFacultadBusq = ammfListFacultadBusq==null?(new ArrayList<Dependencia>()):ammfListFacultadBusq;
		return ammfListFacultadBusq;
	}
	public void setAmmfListFacultadBusq(List<Dependencia> ammfListFacultadBusq) {
		this.ammfListFacultadBusq = ammfListFacultadBusq;
	}
	public Dependencia getAmmfFacultadBusq() {
		return ammfFacultadBusq;
	}
	public void setAmmfFacultadBusq(Dependencia ammfFacultadBusq) {
		this.ammfFacultadBusq = ammfFacultadBusq;
	}
	public Carrera getAmmfCarreraBusq() {
		return ammfCarreraBusq;
	}
	public void setAmmfCarreraBusq(Carrera ammfCarreraBusq) {
		this.ammfCarreraBusq = ammfCarreraBusq;
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
	public List<MallaCurricularDto> getAmmfListMallaCurricularDtoBusq() {
		ammfListMallaCurricularDtoBusq = ammfListMallaCurricularDtoBusq==null?(new ArrayList<MallaCurricularDto>()):ammfListMallaCurricularDtoBusq;
		return ammfListMallaCurricularDtoBusq;
	}
	public void setAmmfListMallaCurricularDtoBusq(List<MallaCurricularDto> ammfListMallaCurricularDtoBusq) {
		this.ammfListMallaCurricularDtoBusq = ammfListMallaCurricularDtoBusq;
	}
	public List<MallaCurricularDto> getAmmfListMallaMateriaBusq() {
		ammfListMallaMateriaBusq = ammfListMallaMateriaBusq==null?(new ArrayList<MallaCurricularDto>()):ammfListMallaMateriaBusq;
		return ammfListMallaMateriaBusq;
	}
	public void setAmmfListMallaMateriaBusq(List<MallaCurricularDto> ammfListMallaMateriaBusq) {
		this.ammfListMallaMateriaBusq = ammfListMallaMateriaBusq;
	}
	public List<Nivel> getAmmfListNivelBusq() {
		ammfListNivelBusq = ammfListNivelBusq==null?(new ArrayList<Nivel>()):ammfListNivelBusq;
		return ammfListNivelBusq;
	}
	public void setAmmfListNivelBusq(List<Nivel> ammfListNivelBusq) {
		this.ammfListNivelBusq = ammfListNivelBusq;
	}
	public MallaCurricularDto getAmmfMallaMateriaVer() {
		return ammfMallaMateriaVer;
	}
	public void setAmmfMallaMateriaVer(MallaCurricularDto ammfMallaMateriaVer) {
		this.ammfMallaMateriaVer = ammfMallaMateriaVer;
	}
	public MallaCurricularDto getAmmfMallaMateriaEdit() {
		return ammfMallaMateriaEdit;
	}
	public void setAmmfMallaMateriaEdit(MallaCurricularDto ammfMallaMateriaEdit) {
		this.ammfMallaMateriaEdit = ammfMallaMateriaEdit;
	}
	public List<UnidadFormacion> getAmmfListUnidadFormacionEdit() {
		ammfListUnidadFormacionEdit = ammfListUnidadFormacionEdit==null?(new ArrayList<UnidadFormacion>()):ammfListUnidadFormacionEdit;
		return ammfListUnidadFormacionEdit;
	}
	public void setAmmfListUnidadFormacionEdit(List<UnidadFormacion> ammfListUnidadFormacionEdit) {
		this.ammfListUnidadFormacionEdit = ammfListUnidadFormacionEdit;
	}
	public List<Nivel> getAmmfListNivelEdit() {
		ammfListNivelEdit = ammfListNivelEdit==null?(new ArrayList<Nivel>()):ammfListNivelEdit;
		return ammfListNivelEdit;
	}
	public void setAmmfListNivelEdit(List<Nivel> ammfListNivelEdit) {
		this.ammfListNivelEdit = ammfListNivelEdit;
	}
	public Integer getAmmfValidadorClic() {
		return ammfValidadorClic;
	}
	public void setAmmfValidadorClic(Integer ammfValidadorClic) {
		this.ammfValidadorClic = ammfValidadorClic;
	}
	public List<MateriaDto> getAmmfListMateriaAgr() {
		ammfListMateriaAgr = ammfListMateriaAgr==null?(new ArrayList<MateriaDto>()):ammfListMateriaAgr;
		return ammfListMateriaAgr;
	}
	public void setAmmfListMateriaAgr(List<MateriaDto> ammfListMateriaAgr) {
		this.ammfListMateriaAgr = ammfListMateriaAgr;
	}
	public MateriaDto getAmmfMateriaBusq() {
		return ammfMateriaBusq;
	}
	public void setAmmfMateriaBusq(MateriaDto ammfMateriaBusq) {
		this.ammfMateriaBusq = ammfMateriaBusq;
	}
	public List<MateriaDto> getAmmfListMateriaBusq() {
		ammfListMateriaBusq = ammfListMateriaBusq==null?(new ArrayList<MateriaDto>()):ammfListMateriaBusq;
		return ammfListMateriaBusq;
	}
	public void setAmmfListMateriaBusq(List<MateriaDto> ammfListMateriaBusq) {
		this.ammfListMateriaBusq = ammfListMateriaBusq;
	}
	public MallaCurricularDto getAmmfMallaMateriaEliminar() {
		return ammfMallaMateriaEliminar;
	}
	public void setAmmfMallaMateriaEliminar(MallaCurricularDto ammfMallaMateriaEliminar) {
		this.ammfMallaMateriaEliminar = ammfMallaMateriaEliminar;
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
	
	
	
}
