/**************************************************************************
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ConcatenarMateriaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la concatenación de Materias.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
23-08-2017			Marcelo Quishpe                        Emisión Inicial
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
import ec.edu.uce.academico.ejb.excepciones.CorequisitoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PrerequisitoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CorrequisitoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PrerrequisitoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CorequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PrerequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CorequisitoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PrerequisitoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ConcatenacionMateriaForm. Managed Bean que maneja las
 * peticiones para la administración de la tablas Materia, correquisitos y prerequisitos .
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "concatenacionMateriaForm")
@SessionScoped
public class ConcatenacionMateriaForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// OBJETOS
	private Usuario cmfUsuario;
	private Integer cmfTipoUsuario;
	private String cmfTipoCarrera;

	// Form listar materias
	private List<Dependencia> cmfListDependencia;
	private List<Carrera> cmfListCarrera;
	private List<MallaCurricularDto> cmfListMallaCurricularDto;
	private List<Nivel> cmfListNivel;
	private List<MallaCurricularDto> cmfListMallaMateria;

	private Integer cmfFclId;
	private Integer cmfCrrId;
	private Integer cmfMlcrId;
	private Integer cmfNvlId;

	// Form concatenar materias
	private MallaCurricularDto cmfMallaCurricularDto; //Objeto que se recibe del form listar de materias
	private Materia cmfMateria; 
	private List<MateriaDto> cmfListPrerrequisitoMateriaDto; //lista de prerrequsitos asignados
    private List<MateriaDto> cmfListCorrequisitoMateriaDto; //Lista de correquisitos Asignados
	private List<MateriaDto> cmfListPrerrequisitoOriginal;  //Lista original para verificar cambios
	private List<MateriaDto> cmfListCorrequisitoOriginal; //Lista original para verificar cambios
	
	// USADAS EN MODAL
	private String cmfPatronBusqueda;
	private List<MateriaDto> cmfListBuscarPrerrequisito;  //Lista de prerrequisitos a buscarse en BDD
	private List<MateriaDto> cmfListBuscarCorrequisito;   //Lista de  correquisitos a buscarse en BDD

	// ACTIVAR MODALES
	private int cmfModalAgregar;
	private int cmfModalGuardar;	
    private Integer cmfTipoMateria;


	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	@EJB
	UsuarioRolServicio servCmfUsuarioRolServicio;
	@EJB
	DependenciaServicio servCmfDependencia;
	@EJB
	CarreraServicio servCmfCarrera;
	@EJB
	MallaCurricularDtoServicioJdbc servCmfMallaCurricularDtoServicioJdbc;
	@EJB
	MallaMateriaDtoServicioJdbc servCmfMallaMateriaDtoServicioJdbc;
	@EJB
	NivelServicio servCmfNivelServicio;
	@EJB
	MateriaServicio servCmfMateriaServicio;
	@EJB
	PrerequisitoDtoServicioJdbc servCmfPrerequisitoDto;
	@EJB
	CorequisitoDtoServicioJdbc servCmfCorequisitoDto;
	@EJB
	MateriaDtoServicioJdbc servCmfMateriaDto;
	@EJB
	PrerrequisitoServicio servCmfPrerrequisitoServicio;
	@EJB
	CorrequisitoServicio servCmfCorrequisitoServicio;
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	
	@PostConstruct
	public void inicializar() {

	}

	/**
	 * Método para iniciar variables del formulario listarMaterias.
	 */
	private void iniciarFormListarMaterias() {
		cmfListDependencia = cargarFacultades();
		cmfListCarrera = new ArrayList<Carrera>();
		cmfListMallaCurricularDto = new ArrayList<MallaCurricularDto>();
		cmfListNivel = new ArrayList<Nivel>();
		cmfListMallaMateria = new ArrayList<MallaCurricularDto>();
		cmfFclId = GeneralesConstantes.APP_ID_BASE;
		cmfCrrId = GeneralesConstantes.APP_ID_BASE;
		cmfMlcrId = GeneralesConstantes.APP_ID_BASE;
		cmfNvlId = GeneralesConstantes.APP_ID_BASE;
	}

	/**
	 * Método para iniciar variables del formulario concatenarMaterias.
	 */
	private void iniciarFormConcatenarMaterias(MallaCurricularDto mallaMateria) {
		cmfMallaCurricularDto = mallaMateria;
		cmfMateria = buscarMateria(mallaMateria.getMtrId());
		cmfListPrerrequisitoMateriaDto= new ArrayList<MateriaDto>();
		cmfListCorrequisitoMateriaDto= new ArrayList<MateriaDto>();
		cmfListPrerrequisitoOriginal= new ArrayList<MateriaDto>();
		cmfListCorrequisitoOriginal= new ArrayList<MateriaDto>();
		cmfListPrerrequisitoMateriaDto = buscarPrerrequisitosAsignados(mallaMateria.getMtrId());
		cmfListCorrequisitoMateriaDto = buscarCorrequisitosAsignados(mallaMateria.getMtrId());
		cmfListPrerrequisitoOriginal=buscarPrerrequisitosAsignados(mallaMateria.getMtrId());
		cmfListCorrequisitoOriginal=buscarCorrequisitosAsignados(mallaMateria.getMtrId());
		cmfTipoMateria = GeneralesConstantes.APP_ID_BASE;
		desactivaModalAgregar();
		desactivaModalGuardar();
	}
	
	/**
	 * Método que permite iniciar el modal agregar materias. 
	 */
	private void iniciarModalAgregar() {
		activaModalAgregar();
		desactivaModalGuardar();
		cmfListBuscarCorrequisito = new ArrayList<MateriaDto>();
		cmfListBuscarPrerrequisito = new ArrayList<MateriaDto>();
		cmfPatronBusqueda = new String("");
	}


	// ********************************************************************/
	// ***************** METODOS DE NAVEGACION **************************/
	// ********************************************************************/

	/**
	 * Método que permite regresar al formulario menú pricipal.
	 * @return formulario menú principal.
	 */
	public String irInicio() {
		limpiarFormListarMaterias();
		return "irInicio";
	}

	/**
	 * Método que permite ir al formulario listarMaterias.
	 * @param  usuario- usuario que usa el form
	 * @return ir formulario listarMaterias
	 */
	public String irListarMateriaConcatenar(Usuario usuario) {
		cmfUsuario = usuario;
			iniciarFormListarMaterias();
			List<UsuarioRol> usroLista;
			try {
				usroLista = servCmfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
				for (UsuarioRol item : usroLista) {
					if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
						cmfTipoUsuario = RolConstantes.ROL_ADMINDGA_VALUE.intValue();
						cmfTipoCarrera = "carreras";
					} else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
						cmfTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
						cmfTipoCarrera = "programas";
					}
				}
		
			} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
				
			}
		return "irListarMateriaConcatenar";
	}

	
	/**
	 * Método que permite ir al formulario listarMaterias, regresar desde form concatenarMateria
	 * @return ir a formulario listarMaterias
	 */
	public String irConcatenarAlistarMaterias(){
		return "irListarMateriaConcatenar";
	}
	
	/**
	 * Método que permite ir al formulario concatenarMaterias.
	 * @param mallaMateria- objeto malla materia selecionada en el form listar materias.
	 * @return ir al  formulario concatenarMaterias.
	 */
	public String irConcatenarMateria(MallaCurricularDto mallaMateria) {
		iniciarFormConcatenarMaterias(mallaMateria);
		return "irConcatenarMateria";
	}
	
	/**
	 * Método que guardar-editar estados Prerequisitos-Correqusitos 
	 * @return - Ir a xthml Listar Materias
	 */
	public String irGuardandoMaterias() {
		guardarMaterias();
		return "irListarMateriaConcatenar";
	}

	
	// ********************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *********************/
	// ********************************************************************/
	
	/**
	 * Método que permite buscar dependencias.
	 * @return lista de dependencias.
	 */
	public List<Dependencia> cargarFacultades() {

		try {
//			return servCmfDependencia.listarFacultadesxUsuario(cmfUsuario.getUsrId()); //facultades por usuario
			return servCmfDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE); //facultades activas
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return null;
	}

	/**
	 * Método que permite buscar una lista de carreras.
	 * @param fclId - Id de la Facultad.
	 */
	public void cargarCarreras(int fclId) {
		
		try {
			if (fclId != GeneralesConstantes.APP_ID_BASE) {
				cmfCrrId = GeneralesConstantes.APP_ID_BASE;
				//cmfListCarrera = servCmfCarrera.listarCarrerasXFacultad(fclId);
				
				if(cmfTipoUsuario==RolConstantes.ROL_ADMINDGA_VALUE || cmfTipoUsuario==RolConstantes.ROL_ADMINFACULTAD_VALUE || cmfTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
					cmfListCarrera = null;
					cmfListCarrera = servCmfCarrera.listarCarrerasXFacultad(fclId);	//Listamos las carreras de pregrado  por facultad para llenar el combo.
				}else if (cmfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
					cmfListCarrera = null;
					cmfListCarrera = servCmfCarrera.listarCarrerasXFacultadPosgrado(fclId);	//Listamos las carreras de postgrado por facultad  para llenar el combo.
				}
				if(cmfListCarrera.size()==0){//Se controla si la lista esta vacio, no funciona el control del servicio.
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.carrera.por.id.faculta.no.encontrado.exception")));
				}
				limpiarMallaNivel();
			} else {
				limpiarCarreras();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.carrera.por.id.faculta.validacion.exception")));
				
			}
		} catch (CarreraNoEncontradoException e) {
			limpiarCarreras();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.carrera.por.id.faculta.no.encontrado.exception")));
		} catch (CarreraException e) {
			limpiarCarreras();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
	}

	

	/**
	 * Método que permite buscar mallas por carrera.
	 * @param idCarrera - Id de la carrera.
	 */
	public void cargarMallas(int idCarrera) {
		try {
			if (idCarrera != GeneralesConstantes.APP_ID_BASE) {
				cmfMlcrId = GeneralesConstantes.APP_ID_BASE;
				cmfListMallaCurricularDto = servCmfMallaCurricularDtoServicioJdbc.listarMallasXCarreraXVigenciaXEstadoXestadoPeriodo(cmfCrrId, MallaCurricularConstantes.VIGENTE_MALLA_SI_VALUE, MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				limpiarNivel();
			} else {
				limpiarMallaNivel();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.mallas.por.id.carrera.validacion.exception")));
			}
		} catch (MallaCurricularDtoException e) {
			limpiarMallaNivel();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			limpiarMallaNivel();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método que permite buscar niveles por malla.
	 * @param mlcrId -id de la malla curricular de la cual se desea buscar los niveles
	 */
	public void cargarNiveles(int mlcrId) {
		try {
			if (mlcrId != GeneralesConstantes.APP_ID_BASE) {
				if (cmfTipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue() || cmfTipoUsuario == RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() || cmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
					cmfNvlId = GeneralesConstantes.APP_ID_BASE;
					cmfListNivel = servCmfNivelServicio.listarTodos();
				} else if (cmfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()) {
					cmfNvlId = GeneralesConstantes.APP_ID_BASE;
					cmfListNivel = servCmfNivelServicio.listarTodosPosgrado();
				}
				
				if(cmfListNivel.size()==0){// Se controla si no encuentra niveles, no controlado en servicio.
					limpiarNivel();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.niveles.por.id.malla.no.encontrado.exception")));
				}
			} else {
				limpiarNivel();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.niveles.por.id.malla.validacion.exception")));
			}
		} catch (NivelNoEncontradoException e) {
			limpiarNivel();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.niveles.por.id.malla.no.encontrado.exception")));
		} catch (NivelException e) {
			limpiarNivel();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca las materias por facultad, carrera, malla y nivel.
	 */
	public void cargarMateriasForm() {
		try {
			// VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if (cmfFclId == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.materias.validacion.facultad")));
			} else if (cmfCrrId == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.materias.validacion.carrera")));
			} else if (cmfMlcrId == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.materias.validacion.malla")));
			} else if (cmfNvlId == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.cargar.materias.validacion.nivel")));
			} else {
				// BUSCO LAS MALLAS CURRICULARES MATERIA - ES DECIR LAS MATERIAS MATERIAS DE UNA MALLA CURRICULAR POR NIVEL
				cmfListMallaMateria = servCmfMallaMateriaDtoServicioJdbc.listarMallasMateriasXIdMallaXEstadoPeriodo
						(cmfMlcrId,
						MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE,
						MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE,
						MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE, cmfNvlId, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			}
		} catch (MallaCurricularDtoException e) {
			iniciarFormListarMaterias();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			iniciarFormListarMaterias();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	/**
	 * Método que permite recuperar la materia seleccionada para asignación de prerrequisitos y correquisitos.
	 * @param idMateria -Id de la materia, la cual se busca y presentara información.
	 * @return objeto tipo Materia.
	 */
	private Materia buscarMateria(int idMateria) {
     Materia retorno = null;
		try {
			retorno= servCmfMateriaServicio.buscarPorId(idMateria);
		} catch (MateriaNoEncontradoException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		} catch (MateriaException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que permite recuperar los prerrequisitos de una materia.
	 * @param idMateria - id de la Materia.
	 * @return lista de prerrequisitos asignados.
	 */
	private List<MateriaDto> buscarPrerrequisitosAsignados(int idMateria) {
		List<MateriaDto> retorno=null;
		try {
			retorno= servCmfPrerequisitoDto.listarXidMateriaRequisitos(idMateria);
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			//NO requiere mensaje
		}
		return retorno;
	}
	
	/**
	 * Método que permite recuperar los correquisitos de una materia.
	 * @param idMateria - id de la Materia.
	 * @return lista de correquisitos asignados.
	 */
	private List<MateriaDto> buscarCorrequisitosAsignados(int idMateria) {
		List<MateriaDto> retorno=null;
		try {
			retorno= servCmfCorequisitoDto.listarXidMateria(idMateria);
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
		}
		return retorno;
	}
	
	
	
	/**
	 * Método que busca las materias en el modal, al pedido desde el form concatenarMateria
	 *@param patron- patron de busqueda
	 */
	public void cargarMateriasModal(String patron) {

		if (cmfTipoMateria == MateriaConstantes.MATERIA_PRERREQUISITO_VALUE) {
			//se envia la lista de BDD(encontrada) y  la lista de prerequsitos asignados para eliminarse las ya asignadas
			
			if(filtrarMateriasPre(buscarMaterias(patron), cmfListPrerrequisitoMateriaDto)!=null){
			
			cmfListBuscarPrerrequisito = filtrarMateriasPre(buscarMaterias(patron), cmfListPrerrequisitoMateriaDto); 
			}else{
				
				//FacesUtil.limpiarMensaje();
				// TODO:  NO SE PRESENTA MENSAJE EN MODAL
			//	FacesUtil.mensajeError("No existen materias en niveles inferioroes para agregarse como prerrequisitos");
				
			}
				
				
		}

		if (cmfTipoMateria == MateriaConstantes.MATERIA_CORREQUISITO_VALUE) {
			
			if(filtrarMateriasCo(buscarMaterias(patron), cmfListCorrequisitoMateriaDto,cmfMateria)!=null){
			
			cmfListBuscarCorrequisito = filtrarMateriasCo(buscarMaterias(patron), cmfListCorrequisitoMateriaDto,cmfMateria);
			}else{
				
			
				  //FacesUtil.limpiarMensaje();
				// TODO:  NO SE PRESENTA MENSAJE EN MODAL
				// FacesUtil.mensajeError("No existen materias  en el mismo nivel para agregarse como correquisitos");
				
						
			}
			
			
		}
	}

	
	
	/**
	 * Método que busca las materias en la BDD.
	 * @param patron- patron de busqueda
	 * @return Lista de materias enviadas por el servicio de Materia Dto
	 */
	private List<MateriaDto> buscarMaterias(String patron) {
		List<MateriaDto> retorno=null;
		try {
			retorno= servCmfMateriaDto.listarMateriasPIdMlcr_PIdNvl_PDescripcion(cmfMallaCurricularDto.getMlcrId(),
					cmfMallaCurricularDto.getNvlId(), patron, cmfTipoMateria);
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			//No se requiere mensaje
		}
		return retorno;
	}
	

		/**
		 * Método que  elimina las materias asignadas de la lista de la BDD
		 * @param  Lista de materias de la BDD,
		 * @param  Lista de materias Asignadas como prerrequisitos
		 * @return Retorna la lista de materias que pueden ser seleccionadas como prerequisitos 
		 */
		private List<MateriaDto> filtrarMateriasPre(List<MateriaDto> materiasDb, List<MateriaDto> materiasAsig) {
			if(materiasDb!=null && materiasAsig!=null){
			if (materiasDb.size() != 0 && materiasAsig.size() != 0) {
				Iterator<MateriaDto> itAs = materiasAsig.iterator();
				while (itAs.hasNext()) {
					Iterator<MateriaDto> itDb = materiasDb.iterator();
					Integer aux1 = itAs.next().getPrrMtrPrrId();
					while (itDb.hasNext()) {
						Integer aux2 = itDb.next().getMtrId();
						if (aux1.equals(aux2)) {
							itDb.remove();
						}
					}
				}
			}
			}
			return materiasDb;
		}

		
		/**
		 * Método que  elimina las materias asignadas de la lista de la BDD
		 * @param  Lista de materias de la BDD,
		 * @param  Lista de materias Asignadas como prerrequisitos
		 * @return Retorna la lista de materias que pueden ser seleccionadas como prerequisitos 
		 */
		private List<MateriaDto> filtrarMateriasCo(List<MateriaDto> materiasDb, List<MateriaDto> materiasAsig,Materia materia) {
			// retiro las materias que estan ya como corequsiitos
			
			
			if(materiasDb!=null && materiasAsig!=null){
			if (materiasDb.size() != 0 && materiasAsig.size() != 0) {
				Boolean verificar = false;
				Iterator<MateriaDto> itAs = materiasAsig.iterator();
				while (itAs.hasNext()) {
					Iterator<MateriaDto> itDb = materiasDb.iterator();
					Integer aux1 = itAs.next().getCrqMtrCorequisitoId();

					while (itDb.hasNext()) {
						Integer aux2 = itDb.next().getMtrId();
						if (aux2.equals(aux1)) {
							verificar = true;
						}

						if (verificar) {
							itDb.remove();
							verificar = false;
						}

					}
				}

			}
			
			}

			// retiro la materia principal del listado
			if ((materiasDb.size() != 0)&&(materiasDb!=null)) {
				Integer padre = materia.getMtrId();
				Iterator<MateriaDto> itDb = materiasDb.iterator();
				while (itDb.hasNext()) {
					Integer aux2 = itDb.next().getMtrId();
					if (aux2.equals(padre)) {
						itDb.remove();
					}
				}
			}

			return materiasDb;
		}

	
	/**
	 * Método que permite limpiar los parámetros de busqueda del formulario listarMaterias.
	 */
	public void limpiarFormListarMaterias() {
		iniciarFormListarMaterias();
	}
	
	/**
	 * Método que permite limpiar los parámetros de carrrea,malla, nivel y  lista de materias.
	 */
	private void limpiarCarreras() {
        cmfListCarrera=new ArrayList<>();
		cmfCrrId=GeneralesConstantes.APP_ID_BASE;
	    cmfListMallaMateria = new ArrayList<>();
		cmfListMallaCurricularDto = new ArrayList<>();
		cmfMlcrId = GeneralesConstantes.APP_ID_BASE;
		cmfListNivel = new ArrayList<>();
		cmfNvlId = GeneralesConstantes.APP_ID_BASE;
     }
	
	
	/**
	 * Método que permite limpiar los parámetros de malla, nivel y  lista de materias.
	 */
	private void limpiarMallaNivel() {
		cmfListMallaMateria = new ArrayList<>();
		cmfListMallaCurricularDto = new ArrayList<>();
		cmfMlcrId = GeneralesConstantes.APP_ID_BASE;
		cmfListNivel = new ArrayList<>();
		cmfNvlId = GeneralesConstantes.APP_ID_BASE;
	}

	/**
	 * Método que permite limpiar los parámetros  nivel y lista de materias.
	 */
	private void limpiarNivel() {
		cmfListNivel = new ArrayList<>();
		cmfListMallaMateria = new ArrayList<>();
		cmfNvlId = GeneralesConstantes.APP_ID_BASE;
	}
	
	
	/**
	 * Método que permite limpiar la lista de materias.
	 */
	public void limpiarMateriasFormListaMaterias() {
		cmfListMallaMateria = new ArrayList<>();
	}
	
	/**
	 * Método que permite limpiar el modal de agregar prerrequisitos y correquisitos.
	 */
	public void limpiarModalAgregar() {
		iniciarModalAgregar();
	}
	
	/**
	 * Método que agrega las materias de manera temporal a las listas de prerrequisitos y correquisitos.
	 * @param materia - materia a agregar.
	 */
	public void agregarMateriaDto(MateriaDto materia) {
		if (cmfTipoMateria == MateriaConstantes.MATERIA_PRERREQUISITO_VALUE) {
			cmfListPrerrequisitoMateriaDto.add(new MateriaDto(materia.getMtrCodigo(), materia.getMtrDescripcion(),
					materia.getTpmtDescripcion(), cmfMateria.getMtrId(), materia.getMtrId(),
					GeneralesConstantes.APP_ID_BASE));
			desactivaModalAgregar();
		}
		
		if (cmfTipoMateria == MateriaConstantes.MATERIA_CORREQUISITO_VALUE) {
			cmfListCorrequisitoMateriaDto.add(new MateriaDto(materia.getMtrId(), materia.getMtrCodigo(),
					materia.getMtrDescripcion(), materia.getTpmtDescripcion(), cmfMateria.getMtrId(),
					materia.getMtrId(), GeneralesConstantes.APP_ID_BASE));
			desactivaModalAgregar();
		}
	}

	/**
	 * Método que cambiar el estado del prerequisito o eliminar un prerrequisito sin guardar de la lista.
	 * @param materia- materia a cambiar el estado
	 */
	public void cambiarEstadoPrerrequisitoMateriaDto(MateriaDto materia) {
      int eliminarPrerrequisito= 0;
		for (MateriaDto it : cmfListPrerrequisitoMateriaDto) {
			if (it.getPrrMtrPrrId().equals(materia.getPrrMtrPrrId())) {
				if (it.getPrrEstado().equals(PrerequisitoConstantes.ESTADO_PREREQUISITO_ACTIVO_VALUE)) {
					it.setPrrEstado(PrerequisitoConstantes.ESTADO_PREREQUISITO_INACTIVO_VALUE);
				    } else if(it.getPrrEstado().equals(PrerequisitoConstantes.ESTADO_PREREQUISITO_INACTIVO_VALUE)){
					             it.setPrrEstado(PrerequisitoConstantes.ESTADO_PREREQUISITO_ACTIVO_VALUE);
				         }else if(it.getPrrEstado().equals(GeneralesConstantes.APP_ID_BASE)){
				               //ELIMINAR MATERIA
				        	  eliminarPrerrequisito=1; 
			              }
					}
		}
		
		if(eliminarPrerrequisito==1){
			 cmfListPrerrequisitoMateriaDto.remove(materia);//Elimina el prerrequisito si esta en estado SINGUARDAR.
			 eliminarPrerrequisito=0;
		}
		desactivaModalAgregar();
		desactivaModalGuardar();
	}

	
	/**
	 * Método que cambia el estado del correquisito o eliminar un correquisito sin guardar de la lista.
	 * @param materia- materia a cambiar el estado
	 */
	public void cambiarEstadoCorrequisitoMateriaDto(MateriaDto materia) {
		int eliminarCorrequisito =0;
		for (MateriaDto it : cmfListCorrequisitoMateriaDto) {
			if (it.getCrqMtrCorequisitoId().equals(materia.getCrqMtrCorequisitoId())) {
				if (it.getCrqEstado().equals(CorequisitoConstantes.ESTADO_COREQUISITO_ACTIVO_VALUE)) {
					it.setCrqEstado(CorequisitoConstantes.ESTADO_COREQUISITO_INACTIVO_VALUE);
				} else if (it.getCrqEstado().equals(CorequisitoConstantes.ESTADO_COREQUISITO_INACTIVO_VALUE)) {
					it.setCrqEstado(CorequisitoConstantes.ESTADO_COREQUISITO_ACTIVO_VALUE);
				      }else if(it.getCrqEstado().equals(GeneralesConstantes.APP_ID_BASE)){
			               //ELIMINAR MATERIA
				    	  eliminarCorrequisito=1; 
		              }
			}
		}
		
		if(eliminarCorrequisito==1){
			 cmfListCorrequisitoMateriaDto.remove(materia); //Elimina el prerrequisito si esta en estado SINGUARDAR.
			 eliminarCorrequisito=0;
		}
		
	 desactivaModalAgregar();
	 desactivaModalGuardar();
	}

	
	
	/**
	 * Método que permite guardar los prerrequisitos y correquisitos en la BDD.
	 
	 */
	private void guardarMaterias(){
		boolean completoPrr= false;
		boolean completoCrq= false;
		
		if ((cmfListPrerrequisitoMateriaDto.size() == 0) && (cmfListCorrequisitoMateriaDto.size() == 0)) {
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.guardar.materias.vacio.validacion")));

		} else {
			
			if (cmfListPrerrequisitoMateriaDto.size() != 0) {
				for (MateriaDto itPre : cmfListPrerrequisitoMateriaDto) {
					// Seteo el estado en ACTIVO de los objetos que estan sin
					// guardar
					if (itPre.getPrrEstado() == GeneralesConstantes.APP_ID_BASE) {
						itPre.setPrrEstado(PrerequisitoConstantes.ESTADO_PREREQUISITO_ACTIVO_VALUE);
					}
				}

				try {
					if (servCmfPrerrequisitoServicio.guardar(cmfListPrerrequisitoMateriaDto)) {
						// FacesUtil.mensajeError("Guardado con exito PRER");
						completoPrr = true;
					}
				} catch (PrerequisitoException e) {
					desactivaModalGuardar();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());

				} catch (Exception e) {
					desactivaModalGuardar();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				}

			} else {
				completoPrr = true;

			}

			if (cmfListCorrequisitoMateriaDto.size() != 0) {
				for (MateriaDto itCo : cmfListCorrequisitoMateriaDto) {
					// Seteo el estado en ACTIVO de los objetos que estan sin
					// guardar
					if (itCo.getCrqEstado() == GeneralesConstantes.APP_ID_BASE) {
						itCo.setCrqEstado(CorequisitoConstantes.ESTADO_COREQUISITO_ACTIVO_VALUE);
					}

				}
				try {
					if (servCmfCorrequisitoServicio.guardar(cmfListCorrequisitoMateriaDto)) {
						// FacesUtil.mensajeError("Guardado con exito CORQ");
						completoCrq = true;
					}

				} catch (CorequisitoException e) {
					desactivaModalGuardar();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				} catch (Exception e) {
					desactivaModalGuardar();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				}
			} else {
				completoCrq = true;

			}

			if (completoPrr && completoCrq) {
				desactivaModalGuardar();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.guardar.materias.con.exito.validacion")));
			}
					
			desactivaModalGuardar();
			//iniciarFormListarMaterias();
	
	}
}
	
	
	
	/**
	 * Método que permite activar el modal prerrequisito 
	 */

	public void activaModalAgregarPrerrequisito() {
		cmfTipoMateria = MateriaConstantes.MATERIA_PRERREQUISITO_VALUE;
		iniciarModalAgregar();
	}

	/**
	 * Método que permite activar el modal correquisito
	 */
	public void activaModalAgregarCorrequisito() {
		cmfTipoMateria = MateriaConstantes.MATERIA_CORREQUISITO_VALUE;
		iniciarModalAgregar();
	}

	/**
	 * Método que permite activar el modal guardar
	 */
	public void activaModalGuardar() {
		if(listasDiferentes()){
		cmfModalGuardar = 1;}
		else{
			cmfModalGuardar = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConcatenacionMateria.guardar.materias.sin.cambios.validacion")));
		}
	}
	
	/**
	 * Método que permite saber si se realizaron cambios a los prerrequisitos y correquisitos.
	 */
	private boolean listasDiferentes() {
		
		boolean retorno= false;
		boolean preDiferente= false;
		boolean crqDiferente= false;

		if(cmfListPrerrequisitoOriginal==null){
			cmfListPrerrequisitoOriginal= new ArrayList<MateriaDto>();
		}
		if(cmfListPrerrequisitoMateriaDto==null){
			cmfListPrerrequisitoMateriaDto= new ArrayList<MateriaDto>();
		}
		if(cmfListCorrequisitoOriginal==null){
			cmfListCorrequisitoOriginal= new ArrayList<MateriaDto>();
		}
		if(cmfListCorrequisitoMateriaDto==null){
			cmfListCorrequisitoMateriaDto= new ArrayList<MateriaDto>();
		}
			//Comparo el tamaño de los arreglos.
		if (cmfListPrerrequisitoOriginal.size() != cmfListPrerrequisitoMateriaDto.size()) {
			preDiferente = true;
		} else if (cmfListCorrequisitoOriginal.size() != cmfListCorrequisitoMateriaDto.size()) {
			preDiferente = true;
		} else {
            //Verifico cambios en el estado de alguna materia original
			for (MateriaDto materiaOriginal : cmfListPrerrequisitoOriginal) {
				for (MateriaDto materiaDto : cmfListPrerrequisitoMateriaDto) {
					if ((materiaOriginal.getPrrId() == materiaDto.getPrrId())
							&& (materiaOriginal.getPrrEstado() != materiaDto.getPrrEstado())) {
						preDiferente = true;
						break;
					}
					if (materiaDto.getPrrId() == 0) {
						crqDiferente = true;
						break;
					}
				}
			}
			//Verifico cambios en el estado de alguna materia original
			for (MateriaDto materiaOriginal : cmfListCorrequisitoOriginal) {
				for (MateriaDto materiaDto : cmfListCorrequisitoMateriaDto) {
					if ((materiaOriginal.getCrqId() == materiaDto.getCrqId())
							&& (materiaOriginal.getCrqEstado() != materiaDto.getCrqEstado())) {
						crqDiferente = true;
						break;
					}

					if (materiaDto.getCrqId() == 0) {
						crqDiferente = true;
						break;
					}

				}
			}

		}
		
		//Si se realizó algun cambio devuelvo verdadero
         if((preDiferente==true)||(crqDiferente==true)){
			
			retorno =true;
		}else{
			
			retorno= false;
		}
		return retorno;
	}

	/**
    * Método que permite desactivar el modal guardar	
    */
	public void desactivaModalGuardar() {
		cmfModalGuardar = 0;
	}

	/**
	 * Método que permite activar el modal agregar
	 */
	public void activaModalAgregar() {
			cmfModalAgregar = 1;
	}
	
	/**
	 * Método que permite desactivar el modal agregar
	 */
	public void desactivaModalAgregar() {
		cmfTipoMateria = GeneralesConstantes.APP_ID_BASE;
		cmfModalAgregar = 0;
	}

	
	/**
	 * Método que permite desactivar el modal agregar, al presionar en regresar del modal
	 */
	public void regresarModal() {
		desactivaModalAgregar();
		
	}

	// ********************************************************************/
	// *********** METODOS PARA ENCAPSULAMIENTO *************************/
	// ********************************************************************/

	public Usuario getCmfUsuario() {
		return cmfUsuario;
	}

	public void setCmfUsuario(Usuario cmfUsuario) {
		this.cmfUsuario = cmfUsuario;
	}

	public List<Carrera> getCmfListCarrera() {
		cmfListCarrera = cmfListCarrera == null ? (new ArrayList<Carrera>()) : cmfListCarrera;
		return cmfListCarrera;
	}

	public void setCmfListCarrera(List<Carrera> cmfListCarrera) {
		this.cmfListCarrera = cmfListCarrera;
	}

	public List<Dependencia> getCmfListDependencia() {
		cmfListDependencia = cmfListDependencia == null ? (new ArrayList<Dependencia>()) : cmfListDependencia;
		return cmfListDependencia;
	}

	public void setCmfListDependencia(List<Dependencia> cmfListDependencia) {
		this.cmfListDependencia = cmfListDependencia;
	}

	public Integer getCmfFclId() {
		return cmfFclId;
	}

	public void setCmfFclId(Integer cmfFclId) {
		this.cmfFclId = cmfFclId;
	}

	public Integer getCmfCrrId() {
		return cmfCrrId;
	}

	public void setCmfCrrId(Integer cmfCrrId) {
		this.cmfCrrId = cmfCrrId;
	}

	
	
	public String getCmfPatronBusqueda() {
		return cmfPatronBusqueda;
	}

	public void setCmfPatronBusqueda(String cmfPatronBusqueda) {
		this.cmfPatronBusqueda = cmfPatronBusqueda;
	}

	public int getCmfModalAgregar() {
		return cmfModalAgregar;
	}

	public void setCmfModalAgregar(int cmfModalAgregar) {
		this.cmfModalAgregar = cmfModalAgregar;
	}

	public int getCmfModalGuardar() {
		return cmfModalGuardar;
	}

	public void setCmfModalGuardar(int cmfModalGuardar) {
		this.cmfModalGuardar = cmfModalGuardar;
	}

	public Integer getCmfMlcrId() {
		return cmfMlcrId;
	}

	public void setCmfMlcrId(Integer cmfMlcrId) {
		this.cmfMlcrId = cmfMlcrId;
	}

	public Integer getCmfNvlId() {
		return cmfNvlId;
	}

	public void setCmfNvlId(Integer cmfNvlId) {
		this.cmfNvlId = cmfNvlId;
	}

	public List<MallaCurricularDto> getCmfListMallaCurricularDto() {
		cmfListMallaCurricularDto = cmfListMallaCurricularDto == null ? (new ArrayList<MallaCurricularDto>())
				: cmfListMallaCurricularDto;
		return cmfListMallaCurricularDto;
	}

	public void setCmfListMallaCurricularDto(List<MallaCurricularDto> cmfListMallaCurricularDto) {
		this.cmfListMallaCurricularDto = cmfListMallaCurricularDto;
	}

	public Integer getCmfTipoUsuario() {
		return cmfTipoUsuario;
	}

	public void setCmfTipoUsuario(Integer cmfTipoUsuario) {
		this.cmfTipoUsuario = cmfTipoUsuario;
	}

	public String getCmfTipoCarrera() {
		return cmfTipoCarrera;
	}

	public void setCmfTipoCarrera(String cmfTipoCarrera) {
		this.cmfTipoCarrera = cmfTipoCarrera;
	}

	public List<Nivel> getCmfListNivel() {
		cmfListNivel = cmfListNivel == null ? (new ArrayList<Nivel>()) : cmfListNivel;
		return cmfListNivel;
	}

	public void setCmfListNivel(List<Nivel> cmfListNivel) {
		this.cmfListNivel = cmfListNivel;
	}

	public List<MallaCurricularDto> getCmfListMallaMateria() {
		cmfListMallaMateria = cmfListMallaMateria == null ? (new ArrayList<MallaCurricularDto>()) : cmfListMallaMateria;
		return cmfListMallaMateria;
	}

	public void setCmfListMallaMateria(List<MallaCurricularDto> cmfListMallaMateria) {
		this.cmfListMallaMateria = cmfListMallaMateria;
	}

	public Materia getCmfMateria() {
		return cmfMateria;
	}

	public void setCmfMateria(Materia cmfMateria) {
		this.cmfMateria = cmfMateria;
	}

	public MallaCurricularDto getCmfMallaCurricularDto() {
		return cmfMallaCurricularDto;
	}

	public void setCmfMallaCurricularDto(MallaCurricularDto cmfMallaCurricularDto) {
		this.cmfMallaCurricularDto = cmfMallaCurricularDto;
	}

	public Integer getCmfTipoMateria() {
		return cmfTipoMateria;
	}

	public void setCmfTipoMateria(Integer cmfTipoMateria) {
		this.cmfTipoMateria = cmfTipoMateria;
	}

	public List<MateriaDto> getCmfListBuscarPrerrequisito() {
		cmfListBuscarPrerrequisito = cmfListBuscarPrerrequisito == null ? (new ArrayList<MateriaDto>()) : cmfListBuscarPrerrequisito;
		return cmfListBuscarPrerrequisito;
	}

	public void setCmfListBuscarPrerrequisito(List<MateriaDto> cmfListBuscarPrerrequisito) {
		this.cmfListBuscarPrerrequisito = cmfListBuscarPrerrequisito;
	}

	public List<MateriaDto> getCmfListBuscarCorrequisito() {
		cmfListBuscarCorrequisito = cmfListBuscarCorrequisito == null ? (new ArrayList<MateriaDto>()) : cmfListBuscarCorrequisito;
		return cmfListBuscarCorrequisito;
	}

	public void setCmfListBuscarCorrequisito(List<MateriaDto> cmfListBuscarCorrequisito) {
		this.cmfListBuscarCorrequisito = cmfListBuscarCorrequisito;
	}

	public List<MateriaDto> getCmfListPrerrequisitoMateriaDto() {
		cmfListPrerrequisitoMateriaDto = cmfListPrerrequisitoMateriaDto == null ? (new ArrayList<MateriaDto>()) : cmfListPrerrequisitoMateriaDto;
		return cmfListPrerrequisitoMateriaDto;
	}

	public void setCmfListPrerrequisitoMateriaDto(List<MateriaDto> cmfListPrerrequisitoMateriaDto) {
		this.cmfListPrerrequisitoMateriaDto = cmfListPrerrequisitoMateriaDto;
	}

	public List<MateriaDto> getCmfListCorrequisitoMateriaDto() {
		cmfListCorrequisitoMateriaDto = cmfListCorrequisitoMateriaDto == null ? (new ArrayList<MateriaDto>()) : cmfListCorrequisitoMateriaDto;
		return cmfListCorrequisitoMateriaDto;
	}

	public void setCmfListCorrequisitoMateriaDto(List<MateriaDto> cmfListCorrequisitoMateriaDto) {
		this.cmfListCorrequisitoMateriaDto = cmfListCorrequisitoMateriaDto;
	}

	public List<MateriaDto> getCmfListPrerrequisitoOriginal() {
		cmfListPrerrequisitoOriginal = cmfListPrerrequisitoOriginal == null ? (new ArrayList<MateriaDto>()) : cmfListPrerrequisitoOriginal;
		return cmfListPrerrequisitoOriginal;
	}

	public void setCmfListPrerrequisitoOriginal(List<MateriaDto> cmfListPrerrequisitoOriginal) {
		this.cmfListPrerrequisitoOriginal = cmfListPrerrequisitoOriginal;
	}

	public List<MateriaDto> getCmfListCorrequisitoOriginal() {
		cmfListCorrequisitoOriginal = cmfListCorrequisitoOriginal == null ? (new ArrayList<MateriaDto>()) : cmfListCorrequisitoOriginal;
		return cmfListCorrequisitoOriginal;
	}

	public void setCmfListCorrequisitoOriginal(List<MateriaDto> cmfListCorrequisitoOriginal) {
		this.cmfListCorrequisitoOriginal = cmfListCorrequisitoOriginal;
	}
	
	
	
	
}
