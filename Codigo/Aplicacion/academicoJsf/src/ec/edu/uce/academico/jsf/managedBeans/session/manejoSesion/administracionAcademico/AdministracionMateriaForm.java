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
   
 ARCHIVO:     AdministracionMateriaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de Materia.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
12-07-2017			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NucleoProblemicoDto;
import ec.edu.uce.academico.ejb.excepciones.CampoFormacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.NucleoProblemicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.NucleoProblemicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CampoFormacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NucleoProblemicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CampoFormacion;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.TipoMateria;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionMateriaForm. Managed Bean que maneja las
 * peticiones para la administración de la tabla Materia.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "administracionMateriaForm")
@SessionScoped
public class AdministracionMateriaForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// OBJETOS
	private Usuario amfUsuario;
	// Listar materias
	private List<Dependencia> amfListDependencia;
	private List<Carrera> amfListCarrera;
	private List<MateriaDto> amfListMateriaDto;
	private Integer amfFclId;  //Usado para busqueda facultad
	private Integer amfCrrId;  //Usado para busqueda carrera
	private Boolean amfRenderHoras; //habilita y deshabilita campo de vista horas
	private Boolean amfRenderCreditos;  //habilita y deshabilita campo de vista créditos
	private Boolean amfRenderEstadoMateria;  //habilita y deshabilita el Estado de materia para edicion.
	//Para diferencia el tipo de usuario
	private Integer amfTipoUsuario;
	private String amfTipoCarrera;
	private Boolean amfCampoRequerido;
	// EDITAR MATERIA Y MATERIA NUEVA
	private MateriaDto amfMateriaDto;  //Objeto a ver y nuevo
	private MateriaDto amfMateriaDtoEditar; // Objeto a Editar
	private List<TipoMateria> amfListTipoMateria; // Lista de tipo de Materia a
	private List<CampoFormacion> amfListCampoFormacion; // Lista de Campos de
	private List<NucleoProblemicoDto> amfListNucleoProblemicoDto; // Lista de
	// **********AUXILIARES*****************//
	private Integer amfClickModalModificar; // permite habilitar el modalmodificar materia
	private Integer amfClickModalAgregar; // permite habilitar el modal agregar materia
	
	//PARA SABER SI TIENE O NO NUCLEO PROBLEMICO
	private boolean amfNucleoDesabilita = false;
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB
	MateriaDtoServicioJdbc servAmfMateriaDto;
	@EJB
    DependenciaServicio servAmfDependencia;
	@EJB
	CarreraServicio servAmfCarrera;
	@EJB
	TipoMateriaServicio servAmfTipoMateria;
	@EJB
	CampoFormacionServicio servAmfCampoFormacion;
	@EJB
	NucleoProblemicoDtoServicioJdbc servAmfNucleProblemicoDto;
	@EJB
	MateriaServicio servAmfMateriaServicio;
	@EJB
	UsuarioRolServicio servAmfUsuarioRolServicio;

	// ********************************************************************/
	// ********************** METODOS NAVEGACION **************************/
	// ********************************************************************/

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad
	 * @return navegacion al inicio.
	 */
	public String irMenuPrincipal() {
		anularFormListarMaterias();
		return "irInicio";
	}
	
	/**
	 * Método Ir a listar las materia por facultad carrera.
	 * @param usuario - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de Administración materia.
	 */
	public String irListarMaterias(Usuario usuario) {
		// Si el usuario es DGA accede a la Administración de parte de DGA, caso contrario es Postgrado. 
		amfUsuario = usuario;
		iniciarFormListarMaterias();
		try {
			List<UsuarioRol> usro = servAmfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					amfTipoUsuario = RolConstantes.ROL_ADMINDGA_VALUE.intValue();
					amfTipoCarrera="carreras";
					amfCampoRequerido=Boolean.TRUE;
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
					amfTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
					amfTipoCarrera="programas";
					amfCampoRequerido=Boolean.FALSE;
				}
			}
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
		}
		
		return "irAdministracionMateria";
	}
	
	/**
	 * Método Ir a listar las materia por facultad carrera.
	 * @param usuario - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de Administración materia.
	 */
	public String irRegresarListar() {
		return "irAdministracionMateria";
	}
	
	/**
	 * Método para ir a crear una nueva materia (ingreso de datos).
	 * @return navagación al xhtml nueva materia.
	 */
	public String irNuevaMateria() {
		iniciarFormNuevaMaterias();//iniciar campos
		return "irNuevoMateria";
	}
	
	/**
	 * Método que permite ver la materia
	 * @return navegación al xhtml ver materia
	 * @param El  objeto materiaDto
	 */
	public String verDatosMateria(MateriaDto materia) {
		amfMateriaDto= new MateriaDto();  // instancia el objeto materiaDto para presentar la informacion en ver materia.
		amfMateriaDto = materia;   // Se referencia la materia seleccionada en la ventana de listar materias.
		return "irVerDatosMateria";
	}
	
	/**
	 * Método que permite ver la materia
	 * @return navegación al xhtml ver materia
	 */
	public String irRegresarVerMateria() {
		return "irVerDatosMateria";
	}

	
	
	/**
	 * Método que permite editar la materia
	 * @return navegación al xhtml editar la materia
	 * @param El objeto materia
	 */
	public String editarMateria(MateriaDto materia) {
		amfMateriaDtoEditar = new MateriaDto(); // instancia el objeto materiaDtoEditar para presentar la informacion en editar materia.
		amfMateriaDtoEditar = materia; // Se referencia la materia enviada desde verMateria.
		iniciarFormEditarMaterias(); //seteo los valores antes de presentar la pantalla editar.
		renderUnidadMedidaEditar(amfMateriaDtoEditar); //habilito o deshabilito campos de créditos/horas segun lo seleccionado en unidad medida.
		renderEstadoMateria(amfMateriaDtoEditar);
		llenarNucleoProblemico(amfMateriaDtoEditar.getCrrId()); // llamo al método para llenar la lista de nucleos problemicos por la carrera de la materia seleccionada.
		return "irEditarMateria";
	}

	
	/**
	 * Método para modificar materia en la BDD
	 * @return navegación al xhtml listarMaterias
	 */
	public String guardarMateriaEditada() {
		try {
			//LLama al método Editar Materia, con los parametros MateriaEditada, para realizar la modificacion en la BDD
			if(servAmfMateriaServicio.editar(amfMateriaDtoEditar)){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.editar.materia.validacion.exitoso")));
			}else{ // caso que no se ejecute el editar
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.guardar.editar.no.ejecutado")));
			}
			//iniciarFormListarMaterias();// iniciamos los valores antes de regresar a listar materias
			buscarMaterias();
		} catch (MateriaValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarFormListarMaterias();
		} catch (MateriaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irAdministracionMateria";
	}


	/**
	 * Método para grabar un nueva materia en la  BDD 
	 * @return navagación al xhtml ListarMateria
	 */
	public String crearMateriaNueva() {
     
		try {
			amfMateriaDto.setDpnId(amfFclId);
			
			// llamo al servicio que crea la nueva materia en la BDD
			if (servAmfMateriaServicio.anadir(amfMateriaDto)) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.crear.nuevo.materia.exitoso")));
				
			}else{ // caso que no se ejecute el guardar nuevo
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.guardar.nuevo.no.ejecutado")));
			}
			iniciarFormListarMaterias();// iniciamos los valores antes de regresar a listar materias
		} catch (MateriaValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarFormListarMaterias();
		} catch (MateriaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			iniciarFormListarMaterias();
		} 
		return "irAdministracionMateria";
	}


	// ********************************************************************/
	// ******************** METODOS DE INICIALIZACION *********************/
	// ********************************************************************/
	
	@PostConstruct
	public void inicializar() {

	}
	
	/**
	 * Método para iniciar las variables del formulario ListarMaterias
	 */
	public void iniciarFormListarMaterias() {
		// LISTAS
		amfListDependencia = new ArrayList<Dependencia>();
		amfListCarrera = new ArrayList<Carrera>();
		// LLAMAR METODO
		llenarFacultades(); 
		limpiarFormListarMaterias();
		ClickCerrarModalModificar();
		ClickCerrarModalAgregar();
	}
	
	/**
	 * Método para iniciar las variables del formulario NuevaMateria
	*/
	private void iniciarFormNuevaMaterias() {
		amfMateriaDto = new MateriaDto();
		amfListCarrera = new ArrayList<>();
		amfListNucleoProblemicoDto= new ArrayList<>();
		amfRenderHoras = amfRenderCreditos = Boolean.TRUE;
		amfFclId = GeneralesConstantes.APP_ID_BASE;
		amfMateriaDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
		amfMateriaDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
		amfMateriaDto.setMtrEstado(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
		amfMateriaDto.setTpmtId(GeneralesConstantes.APP_ID_BASE);
		amfMateriaDto.setCmfrId(GeneralesConstantes.APP_ID_BASE);
		amfMateriaDto.setNcprCrrId(GeneralesConstantes.APP_ID_BASE);
		amfMateriaDto.setMtrUnidadMedida(GeneralesConstantes.APP_ID_BASE);
		amfMateriaDto.setMtrRelacionTrabajo(GeneralesConstantes.APP_ID_BASE);
		renderUnidadMedida(amfMateriaDto); //habilitamos los campos según los seleccionado, seteo valors de horas y créditos
		llenarFacultades(); //llenamos combo facultades
		llenarTiposMateria(); //llenamos combo tipos de materia
		llenarCamposFormacion();  //llenamos combo de campos de formacion
		
	}
	
	/**
	 * Método para iniciar las variables del formulario EditarMateria
	*/
	private void iniciarFormEditarMaterias(){
		ClickCerrarModalModificar();
		llenarTiposMateria();
		llenarCamposFormacion();
	}
	
	// ********************************************************************/
	// ********************** METODOS GENERALES **************************/
	// ********************************************************************/

	
	// ********************** LIMPIEZA VARIABLES *************************/
	/**
	 * Método para limpiar el form e lista de materias cuando se cambia la carrera, y no se presiona buscar
	 * @return
	 */
	public void limpiarFormListarMaterias() {
		amfListCarrera = new ArrayList<>();
		amfListMateriaDto = new ArrayList<MateriaDto>();
		amfFclId = GeneralesConstantes.APP_ID_BASE;
		amfCrrId = GeneralesConstantes.APP_ID_BASE;
	}

	/**
	 * Método para vaciado de variables
	 */
	public void anularFormListarMaterias() {
		amfListMateriaDto = null;
		amfListDependencia = null;
		amfListCarrera = null;
	    amfFclId = null;
		amfCrrId = null;
	}

	/**
	 * Método para limpiar el panel de resultados de la lista de materias por facultad y carrera.
	 */
	public void limpiarPanelResultadosFormListarMaterias() {
		amfListMateriaDto = null;
	}
	
	   // ********************************************************************/
		// *********************** METODOS PRINCIPALES *************************/
		// ********************************************************************/
	
	/**
	 * Método para llenar la lista de Dependencias
	 */
	public void llenarFacultades() {
		amfListMateriaDto= null;
		amfFclId = GeneralesConstantes.APP_ID_BASE;//seteo la facultad 
		try {
			//Listamos las facultades para llenar el combo
//			amfListDependencia = servAmfDependencia.listarFacultadesxUsuario(amfUsuario.getUsrId());
			amfListDependencia = servAmfDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
		} catch (DependenciaNoEncontradoException e) {
			amfListDependencia = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método para llenar la lista de carreras
	 * @param fclId la facultad seleccionada en el combo facultad
	 */
	public void llenarCarrera(int fclId) {
		amfCrrId = GeneralesConstantes.APP_ID_BASE;
		amfListCarrera=new ArrayList<Carrera>();
		amfListMateriaDto = new ArrayList<MateriaDto>();
		try {
			if (fclId != GeneralesConstantes.APP_ID_BASE) {  //Si la facultad fue seleccionada.
				if(amfTipoUsuario==RolConstantes.ROL_ADMINDGA_VALUE || amfTipoUsuario==RolConstantes.ROL_ADMINFACULTAD_VALUE || amfTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
					amfListCarrera = null;
					amfListCarrera = servAmfCarrera.listarCarrerasXFacultad(fclId);	//Listamos las carreras de pregrado  por facultad para llenar el combo.
					
				}else if (amfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
					amfListCarrera = null;
					amfListCarrera = servAmfCarrera.listarCarrerasXFacultadPosgrado(fclId);	//Listamos las carreras de postgrado por facultad  para llenar el combo.
				}
				
				if(amfListCarrera.size()==0){
					amfListCarrera = null;
					amfListCarrera = servAmfCarrera.listarCarrerasXFacultad(fclId);	//Listamos las carreras de postgrado por facultad  para llenar el combo.
					//Se controla si la lista esta vacio, no funciona el control del servicio.
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.no.encontrado.exception")));
				}
				
			}else{
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.validacion.exception")));
			}
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.no.encontrado.exception")));
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	/**
	 * Método para llenar la Lista de Materias 
	 */
	public void buscarMaterias() {
		amfListMateriaDto = new ArrayList<MateriaDto>();
		if (amfFclId == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.buscar.materias.dependencia.validacion.exception")));
		   }else if(amfCrrId==GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.buscar.materias.carrera.validacion.exception")));
		    }else{
			    try {
				   amfListMateriaDto = servAmfMateriaDto.listarMateriasPdpn_IdPcrr_Id(amfFclId, amfCrrId);//Listamos las materias por facultad y carrera seleccionadas
			       } catch (MateriaDtoException e) {
				    limpiarFormListarMaterias();
				    FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(e.getMessage());
		     	       } catch (MateriaDtoNoEncontradoException e) {
				         limpiarFormListarMaterias();
				          FacesUtil.limpiarMensaje();
				          FacesUtil.mensajeError(e.getMessage());
			             }
		      }	
	}
	
	/**
	 * Método que lista los tipos de materia.
	 */
	private void llenarTiposMateria() {
		try {
			amfListTipoMateria = servAmfTipoMateria.listarTodos(); //Listamos los tipos de materia
		} catch (TipoMateriaNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
	}
	
	
	/**
	 * Método que lista los campos formación
	 */
	private void llenarCamposFormacion() {
		try {
			amfListCampoFormacion = servAmfCampoFormacion.listarTodos(); //Listamos los campos de formación.
		} catch (CampoFormacionNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		
	}
	
	/**
	 * Método para llenar la Lista de Nucleo Problemico en agregar materia
	 */
	public void llenarNucleoProblemico(int idCarrera) {
		amfListNucleoProblemicoDto = null;
		if (idCarrera != GeneralesConstantes.APP_ID_BASE) { // Si selecciona una carrera
			try {
				amfListNucleoProblemicoDto = servAmfNucleProblemicoDto.listarXcarrera(idCarrera);//Listamos los nucleos problemicos según la carrera seleccioada.
				amfNucleoDesabilita = false;
			} catch (NucleoProblemicoDtoException e1) {
				amfListNucleoProblemicoDto = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			} catch (NucleoProblemicoDtoNoEncontradoException e1) {
				amfListNucleoProblemicoDto = null;
				amfNucleoDesabilita = true;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			}
		} else { // Sino selecciona una carrera, limpia los mensajes
			amfListNucleoProblemicoDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.nucleo.problemico.por.id.carrera.validacion.exception")));
		}
	}

	/**
	 * Método para llenar la lista de carreras en formulario nueva materia
	 * @param fclId la facultad seleccionada en el combo facultad
	 */
	public void restablecerFormNuevaMateria(int fclId) {
		try {//Se setean los datos del formulario cada vez que se cambia la facultad, usado solo en xhtml agregarMateria.
			if (fclId != GeneralesConstantes.APP_ID_BASE) {
				amfMateriaDto = new MateriaDto();
				amfListCarrera = new ArrayList<>();
				amfListNucleoProblemicoDto= new ArrayList<>();
				amfRenderHoras = amfRenderCreditos = Boolean.TRUE;  //seteo en deshabilitado  el ingreso de horas y créditos.
				amfMateriaDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
				amfMateriaDto.setMtrEstado(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
				amfMateriaDto.setTpmtId(GeneralesConstantes.APP_ID_BASE);
				amfMateriaDto.setCmfrId(GeneralesConstantes.APP_ID_BASE);
				amfMateriaDto.setNcprCrrId(GeneralesConstantes.APP_ID_BASE);
				amfMateriaDto.setMtrUnidadMedida(GeneralesConstantes.APP_ID_BASE);
				amfMateriaDto.setMtrRelacionTrabajo(GeneralesConstantes.APP_ID_BASE);
				renderUnidadMedida(amfMateriaDto);
				llenarTiposMateria();
				llenarCamposFormacion();
				if(amfTipoUsuario==RolConstantes.ROL_ADMINDGA_VALUE || amfTipoUsuario==RolConstantes.ROL_ADMINFACULTAD_VALUE || amfTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
					amfListCarrera = null;
					amfListCarrera = servAmfCarrera.listarCarrerasXFacultad(fclId);	
				}else if (amfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
					amfListCarrera = null;
					amfListCarrera = servAmfCarrera.listarCarrerasXFacultadPosgrado(fclId);	
				}
				
				if(amfListCarrera.size()==0){
					iniciarFormNuevaMaterias();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.no.encontrado.exception")));
	
				}
			}else{
				iniciarFormNuevaMaterias();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.validacion.exception")));
			}
		} catch (CarreraNoEncontradoException e) {
			iniciarFormNuevaMaterias();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.llenar.carrera.por.id.faculta.no.encontrado.exception")));
		} catch (CarreraException e) {
			iniciarFormNuevaMaterias();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	/**
	 * Método para habilitar los campos creditos u horas dentro del formulario nueva materia y editar, segun se seleccione el campo Unidad de Medida.
	 * @param materia para utilizar MtrUnidadMedida seleccionado en el combo
	 */
	public void renderUnidadMedida(MateriaDto materia){
		// se enceran los campos horas y creditos al cambiar el tipo unidad de medida
		if (materia.getMtrUnidadMedida() == GeneralesConstantes.APP_ID_BASE) {
			amfRenderCreditos = amfRenderHoras = Boolean.TRUE; // deshabilitamos los dos campos
		}
		if (materia.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_HORAS_VALUE) {
			amfRenderHoras = Boolean.FALSE; //habilitamos horas
			amfRenderCreditos  = Boolean.TRUE;  //deshabilitamos  creditos
		}
		if (materia.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_CREDITOS_VALUE) {
			amfRenderCreditos  = Boolean.FALSE;  //deshabilitamos Creditos
			amfRenderHoras = Boolean.TRUE;       //habilitamos horas
		}
		if (materia.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_HORAS_Y_CREDITOS_VALUE) {
			amfRenderCreditos = amfRenderHoras = Boolean.FALSE;  //habilitamos ambos
		}
		setCampos(); 
	}
	
	
	/**
	 * Método para habilitar y deshabilitar el campo Estado si la materia existe en alguna mallaCurricular
	 * @param materia para utilizar MtrUnidadMedida seleccionado en el combo
	 */
	public void renderEstadoMateria(MateriaDto materia){
		try {
			if(servAmfMateriaDto.buscarMateriaDtoPMlcrmt(materia.getMtrId())){
				amfRenderEstadoMateria=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				//Mencaje de información:				
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.buscar.materia.en.malla.curricular.validacion.exception")));
			}
			else
				amfRenderEstadoMateria=Boolean.FALSE;
		} catch (MateriaDtoException e) {
			iniciarFormNuevaMaterias();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
		
		
		
	}
	
	/**
	 * Método para habilitar los campos creditos u horas, solo al llamar al formulario editar materia.
	 * @param materia para utilizar MtrUnidadMedida seleccionado en el combo
	 */
	public void renderUnidadMedidaEditar(MateriaDto materia){
		if (materia.getMtrUnidadMedida() == GeneralesConstantes.APP_ID_BASE) {
			amfRenderCreditos = amfRenderHoras = Boolean.TRUE; // deshabilitamos los dos campos
		}
		if (materia.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_HORAS_VALUE) {
			amfRenderHoras = Boolean.FALSE; //habilitamos horas
			amfRenderCreditos  = Boolean.TRUE;  //deshabilitamos  creditos
			amfMateriaDtoEditar.setMtrCreditos(null);
		}
		if (materia.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_CREDITOS_VALUE) {
			amfRenderCreditos  = Boolean.FALSE;  //deshabilitamos Creditos
			amfRenderHoras = Boolean.TRUE;       //habilitamos horas
			amfMateriaDtoEditar.setMtrHoras(null);
		}
		if (materia.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_HORAS_Y_CREDITOS_VALUE) {
			amfRenderCreditos = amfRenderHoras = Boolean.FALSE;  //habilitamos ambos
			
		}
		//setCampos();
	}
	
	/**
	 * Método para setear los  campos  Creditos y horas
	 */
	public void setCampos(){
		//amfMateriaDto.setMtrCreditos(Integer.valueOf(0));
	   	//amfMateriaDto.setMtrHoras(Integer.valueOf(0));
		amfMateriaDto.setMtrCreditos(null);
	   	amfMateriaDto.setMtrHoras(null);
		
	   
	}

	/**
	 * Método para verificar campos llenos antes de activar modal modificar materia
	 */
	public void VerificaModalModificar() {
	    if (amfMateriaDtoEditar.getMtrEstado() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.estado.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDtoEditar.getTpmtId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.tipo.materia.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDtoEditar.getCmfrId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.campo.formacion.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDtoEditar.getMtrUnidadMedida() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.unidad.medida.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDtoEditar.getNcprCrrId() == GeneralesConstantes.APP_ID_BASE && amfListNucleoProblemicoDto.size() > 0) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.nucleo.problemico.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDtoEditar.getMtrRelacionTrabajo() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.relacion.trabajo.modal.agregar.materia.validacion.exception")));
	     } else if((amfMateriaDtoEditar.getMtrUnidadMedida()==MateriaConstantes.MATERIA_POR_CREDITOS_VALUE)&&(amfMateriaDtoEditar.getMtrCreditos()==0)){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.credito.cero.modal.agregar.materia.validacion.exception")));
	     }else if((amfMateriaDtoEditar.getMtrUnidadMedida()==MateriaConstantes.MATERIA_POR_HORAS_VALUE)&&(amfMateriaDtoEditar.getMtrHoras() < 0)){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.cero.modal.agregar.materia.validacion.exception")));
		} else if ((amfMateriaDtoEditar.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_HORAS_Y_CREDITOS_VALUE)
				&& ((amfMateriaDtoEditar.getMtrHoras() != null) && (amfMateriaDtoEditar.getMtrHoras() < 0)
				||  (amfMateriaDtoEditar.getMtrCreditos() !=null)&&(amfMateriaDtoEditar.getMtrCreditos() < 0))) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));

		} else if(amfMateriaDtoEditar.getMtrHoras().intValue() > amfMateriaDtoEditar.getMtrHorasCien().intValue()){
			FacesUtil.mensajeError("Las horas semanales no pueden ser mayor o igual a las horas semestrales");
		} else if(amfMateriaDtoEditar.getMtrHorasPracticas() == GeneralesConstantes.APP_ID_BASE || amfMateriaDtoEditar.getMtrHorasPracticas() == null){
			FacesUtil.mensajeError("Debe ingresar las horas de práctica semestral");
		} else if(amfMateriaDtoEditar.getMtrHorasTrabAutonomo() == GeneralesConstantes.APP_ID_BASE_BIGDEC || amfMateriaDtoEditar.getMtrHorasTrabAutonomo() == null){
			FacesUtil.mensajeError("Debe ingresar las horas de trabajo autónomo semestral");
		} else if(amfMateriaDtoEditar.getMtrHorasPracSema() == GeneralesConstantes.APP_ID_BASE || amfMateriaDtoEditar.getMtrHorasPracSema() == null){
			FacesUtil.mensajeError("Debe ingresar las horas de práctica semanal");
		} else if(amfMateriaDtoEditar.getMtrHorasAutonoSema() == GeneralesConstantes.APP_ID_BASE_BIGDEC || amfMateriaDtoEditar.getMtrHorasAutonoSema() == null){
			FacesUtil.mensajeError("Debe ingresar las horas de trabajo autónomo semanal");
		} else {
			// habilita Modal Modificar
			ClickModalModificar();
		}
	}

	/**
	 * Método para verificar campos llenos antes de activar modal agregar una nueva materia
	 */
	public void VerificaModalAgregar() {
		if (amfMateriaDto.getDpnId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.facultad.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDto.getCrrId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.carrera.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDto.getTpmtId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.tipo.materia.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDto.getCmfrId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.campo.formacion.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDto.getMtrUnidadMedida() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.unidad.medida.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDto.getMtrHorasCien() == null && amfMateriaDto.getMtrHorasCien().intValue() == 0) {
			FacesUtil.mensajeError("Debe seleccionar las horas al semestre");
		} else if (amfMateriaDto.getNcprCrrId() == GeneralesConstantes.APP_ID_BASE && amfListNucleoProblemicoDto.size() > 0) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.nucleo.problemico.modal.agregar.materia.validacion.exception")));
		} else if (amfMateriaDto.getMtrRelacionTrabajo() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.relacion.trabajo.modal.agregar.materia.validacion.exception")));
		} else if((amfMateriaDto.getMtrUnidadMedida()==MateriaConstantes.MATERIA_POR_CREDITOS_VALUE)&&(amfMateriaDto.getMtrCreditos()!=null)&&(amfMateriaDto.getMtrCreditos()==0)){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.credito.cero.modal.agregar.materia.validacion.exception")));
		}else if((amfMateriaDto.getMtrUnidadMedida()==MateriaConstantes.MATERIA_POR_HORAS_VALUE)&&(amfMateriaDto.getMtrHoras()!=null)&&(amfMateriaDto.getMtrHoras() < 0)){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.cero.modal.agregar.materia.validacion.exception")));
		} else if ((amfMateriaDto.getMtrUnidadMedida() == MateriaConstantes.MATERIA_POR_HORAS_Y_CREDITOS_VALUE)
				&& ((amfMateriaDto.getMtrHoras() != null) && (amfMateriaDto.getMtrHoras() == 0)
						|| (amfMateriaDto.getMtrCreditos() !=null)&&(amfMateriaDto.getMtrCreditos() == 0))) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
		}else {
			ClicActivaModalAgregar();// habilita Modal Agregar
		}
	}

	/**
	 * Método para activar Modal modificar Materia
	 */
	public void ClickModalModificar() {
		amfClickModalModificar = 1; // habilita modal modificar
	}

	/**
	 * Método para desactivar Modal modificar Materias
	 */
	public void ClickCerrarModalModificar() {
		amfClickModalModificar = 0; // cerrer modal modificar
	}

	/**
	 * Método para dactivar Modal agregar Materias
	 */
	public void ClicActivaModalAgregar() {
		amfClickModalAgregar = 1; //habilitar modal agregar
	}

	/**
	 * Método para desactivar Modal agregar Materias
	 */
	public void ClickCerrarModalAgregar() {
		amfClickModalAgregar = 0; //deshabilita modal agregar
	}
	
	public void deshabilitarModal(){
		amfClickModalAgregar = 0; //deshabilita modal agregar
	}
	
	public void calcularHorasTeoricasSemestral(){
		if(amfMateriaDtoEditar.getMtrHoras().intValue() >= 0){
			if(amfMateriaDtoEditar.getCrrId() == 157){
				amfMateriaDtoEditar.setMtrHorasCien(amfMateriaDtoEditar.getMtrHoras().intValue() * 18);
			}else{
				amfMateriaDtoEditar.setMtrHorasCien(amfMateriaDtoEditar.getMtrHoras().intValue() * 16);
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un valor mayor ó igual que 0 en horas teóricas docente semanal");
		}
		
	}
	
	public void calcularHorasTeoricasSemestralAgrega(){
		if(amfMateriaDto.getMtrHoras().intValue() >= 0){
			if(amfMateriaDto.getCrrId() == 157){
				amfMateriaDto.setMtrHorasCien(amfMateriaDto.getMtrHoras().intValue() * 18);
			}else{
				amfMateriaDto.setMtrHorasCien(amfMateriaDto.getMtrHoras().intValue() * 16);
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un valor mayor ó igual que 0 en horas teóricas docente semanal");
		}
	}
	
	public void calcularHorasPracticasSemestral(){
		if(amfMateriaDtoEditar.getMtrHorasPracSema().intValue() >= 0){
			if(amfMateriaDtoEditar.getCrrId() == 157){
				amfMateriaDtoEditar.setMtrHorasPracticas(amfMateriaDtoEditar.getMtrHorasPracSema().intValue() * 18);
			}else{
				amfMateriaDtoEditar.setMtrHorasPracticas(amfMateriaDtoEditar.getMtrHorasPracSema().intValue() * 16);
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un valor mayor ó igual que 0 en horas práctica semanal");
		}
	}
	
	public void calcularHorasPracticasSemestralAgrega(){
		if(amfMateriaDto.getMtrHorasPracSema().intValue() >= 0){
			if(amfMateriaDto.getCrrId() == 157){
				amfMateriaDto.setMtrHorasPracticas(amfMateriaDto.getMtrHorasPracSema().intValue() * 18);
			}else{
				amfMateriaDto.setMtrHorasPracticas(amfMateriaDto.getMtrHorasPracSema().intValue() * 16);
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un valor mayor ó igual que 0 en horas práctica semanal");
		}
	}
	
	public void calcularHorasTrabAutonomoSemestral(){
		int valorA = 18;
		int valorB = 16;
		BigDecimal valorABig = new BigDecimal(valorA);
		BigDecimal valorBBig = new BigDecimal(valorB);
		
		if(amfMateriaDtoEditar.getMtrHorasAutonoSema().intValue() >= 0 ){
			if(amfMateriaDtoEditar.getCrrId() == 157){
				amfMateriaDtoEditar.setMtrHorasTrabAutonomo(amfMateriaDtoEditar.getMtrHorasAutonoSema().multiply(valorABig));
			}else{
				amfMateriaDtoEditar.setMtrHorasTrabAutonomo(amfMateriaDtoEditar.getMtrHorasAutonoSema().multiply(valorBBig));
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un valor mayor ó igual que 0 en horas de trabajo autónomo semanal");
		}
	}
	
	public void calcularHorasTrabAutonomoSemestralAgrega(){
		int valorA = 18;
		int valorB = 16;
		BigDecimal valorABig = new BigDecimal(valorA);
		BigDecimal valorBBig = new BigDecimal(valorB);
		
		if(amfMateriaDto.getMtrHorasAutonoSema().intValue() >= 0){
			if(amfMateriaDto.getCrrId() == 157){
				amfMateriaDto.setMtrHorasTrabAutonomo(amfMateriaDto.getMtrHorasAutonoSema().multiply(valorABig));
			}else{
				amfMateriaDto.setMtrHorasTrabAutonomo(amfMateriaDto.getMtrHorasAutonoSema().multiply(valorBBig));
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un valor mayor ó igual que 0 en horas de trabajo autónomo semanal");
		}
	}
	

	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getAmfUsuario() {
		return amfUsuario;
	}

	public void setAmfUsuario(Usuario amfUsuario) {
		this.amfUsuario = amfUsuario;
	}

	public MateriaDto getAmfMateriaDto() {
		return amfMateriaDto;
	}

	public void setAmfMateriaDto(MateriaDto amfMateriaDto) {
		this.amfMateriaDto = amfMateriaDto;
	}

	public List<MateriaDto> getAmfListMateriaDto() {

		amfListMateriaDto = amfListMateriaDto == null ? (new ArrayList<MateriaDto>()) : amfListMateriaDto;

		return amfListMateriaDto;
	}

	public void setAmfListMateriaDto(List<MateriaDto> amfListMateriaDto) {
		this.amfListMateriaDto = amfListMateriaDto;
	}

	public List<TipoMateria> getAmfListTipoMateria() {
		amfListTipoMateria = amfListTipoMateria == null ? (new ArrayList<TipoMateria>()) : amfListTipoMateria;
		return amfListTipoMateria;
	}

	public void setAmfListTipoMateria(List<TipoMateria> amfListTipoMateria) {
		this.amfListTipoMateria = amfListTipoMateria;
	}

	public MateriaDto getAmfMateriaDtoEditar() {
		return amfMateriaDtoEditar;
	}

	public void setAmfMateriaDtoEditar(MateriaDto amfMateriaDtoEditar) {
		this.amfMateriaDtoEditar = amfMateriaDtoEditar;
	}

	public List<CampoFormacion> getAmfListCampoFormacion() {
		amfListCampoFormacion = amfListCampoFormacion == null ? (new ArrayList<CampoFormacion>()): amfListCampoFormacion;
		return amfListCampoFormacion;
	}

	public void setAmfListCampoFormacion(List<CampoFormacion> amfListCampoFormacion) {
		this.amfListCampoFormacion = amfListCampoFormacion;
	}

	public List<NucleoProblemicoDto> getAmfListNucleoProblemicoDto() {
		amfListNucleoProblemicoDto = amfListNucleoProblemicoDto == null ? (new ArrayList<NucleoProblemicoDto>()): amfListNucleoProblemicoDto;
		return amfListNucleoProblemicoDto;
	}

	public void setAmfListNucleoProblemicoDto(List<NucleoProblemicoDto> amfListNucleoProblemicoDto) {
		this.amfListNucleoProblemicoDto = amfListNucleoProblemicoDto;
	}

	public Integer getAmfClickModalModificar() {
		return amfClickModalModificar;
	}

	public void setAmfClickModalModificar(Integer amfClickModalModificar) {
		this.amfClickModalModificar = amfClickModalModificar;
	}

	public Integer getAmfClickModalAgregar() {
		return amfClickModalAgregar;
	}

	public void setAmfClickModalAgregar(Integer amfClickModalAgregar) {
		this.amfClickModalAgregar = amfClickModalAgregar;
	}

	public List<Dependencia> getAmfListDependencia() {
		amfListDependencia = amfListDependencia == null ? (new ArrayList<Dependencia>()) : amfListDependencia;
		return amfListDependencia;
	}

	public void setAmfListDependencia(List<Dependencia> amfListDependencia) {
		this.amfListDependencia = amfListDependencia;
	}

	public List<Carrera> getAmfListCarrera() {
		amfListCarrera = amfListCarrera == null ? (new ArrayList<Carrera>()) : amfListCarrera;
		return amfListCarrera;
	}

	public void setAmfListCarrera(List<Carrera> amfListCarrera) {
		this.amfListCarrera = amfListCarrera;
	}

	public Integer getAmfFclId() {
		return amfFclId;
	}

	public void setAmfFclId(Integer amfFclId) {
		this.amfFclId = amfFclId;
	}

	public Integer getAmfCrrId() {
		return amfCrrId;
	}

	public void setAmfCrrId(Integer amfCrrId) {
		this.amfCrrId = amfCrrId;
	}

	public Boolean getAmfRenderHoras() {
		return amfRenderHoras;
	}

	public void setAmfRenderHoras(Boolean amfRenderHoras) {
		this.amfRenderHoras = amfRenderHoras;
	}

	public Boolean getAmfRenderCreditos() {
		return amfRenderCreditos;
	}

	public void setAmfRenderCreditos(Boolean amfRenderCreditos) {
		this.amfRenderCreditos = amfRenderCreditos;
	}

	public Integer getAmfTipoUsuario() {
		return amfTipoUsuario;
	}

	public void setAmfTipoUsuario(Integer amfTipoUsuario) {
		this.amfTipoUsuario = amfTipoUsuario;
	}

	public String getAmfTipoCarrera() {
		return amfTipoCarrera;
	}

	public void setAmfTipoCarrera(String amfTipoCarrera) {
		this.amfTipoCarrera = amfTipoCarrera;
	}

	public Boolean getAmfCampoRequerido() {
		return amfCampoRequerido;
	}

	public void setAmfCampoRequerido(Boolean amfCampoRequerido) {
		this.amfCampoRequerido = amfCampoRequerido;
	}

	public Boolean getAmfRenderEstadoMateria() {
		return amfRenderEstadoMateria;
	}

	public void setAmfRenderEstadoMateria(Boolean amfRenderEstadoMateria) {
		this.amfRenderEstadoMateria = amfRenderEstadoMateria;
	}

	public boolean isAmfNucleoDesabilita() {
		return amfNucleoDesabilita;
	}

	public void setAmfNucleoDesabilita(boolean amfNucleoDesabilita) {
		this.amfNucleoDesabilita = amfNucleoDesabilita;
	}

	


	
	


}
