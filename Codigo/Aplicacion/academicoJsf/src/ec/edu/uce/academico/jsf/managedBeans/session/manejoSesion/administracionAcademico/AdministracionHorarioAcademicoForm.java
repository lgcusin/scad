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
   
 ARCHIVO:     AdministracionHorarioAcademicoForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de horario académico.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
19-SEPT-2017			Dennis Collaguazo 			      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EdificioDto;
import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AulaException;
import ec.edu.uce.academico.ejb.excepciones.AulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseAulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HoraClaseConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionHorarioAcademicoForm.
 * Managed Bean que maneja las peticiones para la administración de horario académico.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="administracionHorarioAcademicoForm")
@SessionScoped
public class AdministracionHorarioAcademicoForm implements Serializable{

	private static final long serialVersionUID = 2823249100491640215L;
	
	//*******************************************************************/
	//******************* ATRIBUTOS DE LA CLASE *************************/
	//*******************************************************************/

	//GENERAL
	private Usuario ahafUsuario;
	private Integer ahafTipoUsuario;
	private String ahafTipoCarrera;
	private List<UsuarioRolJdbcDto>  ahafListRoles;
	
	//PARA BUSQUEDA DE HORARIO ACADEMICO
	private HorarioAcademicoDto ahafHorarioAcademicoBusq;
	private List<PeriodoAcademicoDto> ahafListPeriodoAcademicoDtoBusq;
	private List<CarreraDto> ahafListCarreraDtoBusq;
	private List<Nivel> ahafListNivelBusq;
	private List<ParaleloDto> ahafListParaleloDtoBusq;
	private List<MateriaDto> ahafListMateriaDtoBusq;
	private List<DocenteJdbcDto> ahafListDocenteJdbcDto;
	
	//PARA VER
	private List<HorarioAcademicoDto> ahafListHorarioAcademicoBusq;
	private MateriaDto ahafMateriaVer;
	
	//PARA EDITAR
	private HorarioAcademicoDto ahafHorarioAcademicoEdit; 
	private List<AulaDto> ahafListAulaDtoEdit;
	private List<HoraClaseDto> ahafListHoraClaseDtoEdit;
	private int ahafHabilitaAulaEdit;
	private int ahafHoraCambioAulaEdit;
	private int ahafDiaCambioAula;
	
	//PARA AGREGAR
	private HorarioAcademicoDto ahafHorarioAcademicoNew; 
	private List<AulaDto> ahafListAulaDtoNew;
	private List<HoraClaseDto> ahafListHoraClaseDtoNew;
	private List<DependenciaDto> ahafListaDependenciaNew;
	private List<EdificioDto> ahafListEdificioDtoNew;
	
	//PARA HABILITAR
	private Integer ahafValidadorClic;
	private Integer ahafModoAsignatura;
	private boolean ahafDeshabilitaDia;
	
	//PARA BUSQUEDA DE DOCENTE
	private DocenteJdbcDto ahafDocenteJdbcDtoBusq;
	private List<Dependencia> ahafListFacultadBusq;
	private List<Carrera> ahafListCarreraBusq;
	private List<DocenteJdbcDto> ahafListDocenteJdbcDtoBusq;
	
	//PARA ELIMINAR DOCENTE DE CARGA HORARIA
	private DocenteJdbcDto ahafDocenteJdbcDtoElimi;
	
	//PARA ELIMINAR HORA CLASE DEL HORARIO ACADEMICO
	private HorarioAcademicoDto ahafHoraClaseElimi;
	
	//BANDERA PARA EDITAR O CREAR COMPARTIR
	private int bandera = 0;
	
	//PARA VER HORARIO ACADEMICO DEL DOCENTE
	private List<HorarioAcademicoDto> ahafListHorarioAcademicoDocente;
	
	//PARA VER DOCENTE
	private DocenteJdbcDto ahafDocente;
	
	//PARA COMPARTIR HORARIO
	private MateriaDto ahafMateriaCompartir;
	private List<HorarioAcademicoDto> ahafListHorarioAcademicoCompartir;
	private List<HorarioAcademicoDto> ahafListHorarioAcademicoCompBusq;
	private HorarioAcademicoDto ahafHorarioAcademicoCompBusq;
	
	private List<PeriodoAcademicoDto> ahafListPeriodoAcademicoDtoCompBusq;
	private List<CarreraDto> ahafListCarreraDtoCompBusq;
	private List<Nivel> ahafListNivelCompBusq;
	private List<ParaleloDto> ahafListParaleloDtoCompBusq;
	private List<MateriaDto> ahafListMateriaDtoCompBusq;
	
	//PARA COMPARTIR DOCENTE
	private DocenteJdbcDto ahafDocenteJdbcDtoComp;
	
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
	UsuarioRolServicio servAhafUsuarioRolServicio;
	@EJB
	PeriodoAcademicoDtoServicioJdbc servAhafPeriodoAcademicoDtoServicioJdbc;
	@EJB
	CarreraDtoServicioJdbc servAhafCarreraDtoServicioJdbc;
	@EJB
	NivelServicio servAhafNivelServicio;
	@EJB
	ParaleloDtoServicioJdbc servAhafParaleloDtoServicioJdbc;
	@EJB
	MateriaDtoServicioJdbc servAhafMateriaDtoServicioJdbc;
	@EJB
	HorarioAcademicoDtoServicioJdbc servAhafHorarioAcademicoDtoServicioJdbc;
	@EJB
	PeriodoAcademicoServicio servAhafPeriodoAcademicoServicio;
	@EJB
	AulaDtoServicioJdbc servAhafAulaDtoServicioJdbc;
	@EJB
	HoraClaseDtoServicioJdbc servAhafHoraClaseDtoServicioJdbc;
	@EJB
	HoraClaseAulaServicio servAhafHoraClaseAulaServicio;
	@EJB
	MallaCurricularParaleloServicio servAhafMallaCurricularParaleloServicio;
	@EJB
	HorarioAcademicoServicio servAhafHorarioAcademicoServicio;
	@EJB
	DocenteDtoServicioJdbc servAhafDocenteDtoServicioJdbc;
	@EJB
	DependenciaServicio servAhafDependenciaServicio;
	@EJB
	CarreraServicio servAhafCarreraServicio;
	@EJB
	DetallePuestoServicio servAhafDetallePuestoServicio;
	@EJB
	CargaHorariaServicio servAhafCargaHorariaServicio;
	@EJB
	DependenciaDtoServicioJdbc servAhafDependenciaDtoServicioJdbc;
	@EJB 
	EdificioDtoServicioJdbc servAhafEdificioDtoServicioJdbc;
	@EJB
	AulaServicio servAhafAulaServicio;
	@EJB
	MallaCurricularParaleloDtoServicioJdbc servAhafMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	UsuarioRolDtoServicioJdbc servAhafUsuarioRolDtoServicioJdbc;
	
	// ********************************************************************/
	// ************* METODOS GENERALES DE LA CLASE ************************/
	// ********************************************************************/
	/**
	 * Dirige la navegación hacia la página de listar
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar 
	 */
	public String irHorarioAcademico(Usuario usuario){
		
		ahafUsuario = usuario;
		
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros(); //inicializa los parametros
			List<UsuarioRol> usroLista = servAhafUsuarioRolServicio.buscarXUsuario(ahafUsuario.getUsrId());
			for (UsuarioRol item : usroLista) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ahafTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					ahafTipoCarrera="carreras";
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ahafTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					ahafTipoCarrera="programas";
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					ahafTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
					ahafListRoles = servAhafUsuarioRolDtoServicioJdbc.buscarRolesUsuarioTodosActivos(ahafUsuario.getUsrIdentificacion());
					for (UsuarioRolJdbcDto itemRolDep : ahafListRoles) {
						//idiomas
						if(itemRolDep.getFclId() ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE){
							ahafTipoCarrera=DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL;
						}
						//cultura fisica
						if(itemRolDep.getFclId() ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE){
							ahafTipoCarrera=DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL;
						}
						//informatica
						if(itemRolDep.getFclId() ==  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE){
							ahafTipoCarrera=DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL;
						}
					}
				}
			}
			//LISTO LOS PERIODOS ACADEMICOS
			if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				ahafListPeriodoAcademicoDtoBusq = servAhafPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoBusq;
			}else if (ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
//				ahafListPeriodoAcademicoDtoBusq = servAhafPeriodoAcademicoDtoServicioJdbc.listarXEstadoPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);	
				ahafListPeriodoAcademicoDtoBusq = servAhafPeriodoAcademicoDtoServicioJdbc.listarXUsuarioPosgrado(ahafUsuario.getUsrId());
				ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoBusq;
			}else if(ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				//para suficiencia idiomas
				if(ahafTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL)){
					ahafListPeriodoAcademicoDtoBusq = servAhafPeriodoAcademicoDtoServicioJdbc.listarXEstadoXTipo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
					ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoBusq;
				}
				//para suficiencia cultura física
				if(ahafTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL)){
					ahafListPeriodoAcademicoDtoBusq = servAhafPeriodoAcademicoDtoServicioJdbc.listarXEstadoXTipo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
					ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoBusq;
				}
				//para suficiencia informatica
				if(ahafTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL)){
					ahafListPeriodoAcademicoDtoBusq = servAhafPeriodoAcademicoDtoServicioJdbc.listarXEstadoXTipo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE);
					ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoBusq;
				}
			}
			//LISTO LAS FACULTADES
			ahafListFacultadBusq = servAhafDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			//LISTO LAS CARRERAS
			ahafListCarreraBusq = servAhafCarreraServicio.listarTodos();
			//busca dependencias de acuerdo al usuario
			ahafListaDependenciaNew = servAhafDependenciaDtoServicioJdbc.listarXUsuario(ahafUsuario.getUsrId());
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irHorarioAcademico";
	}
	
	/**
	 * Método que busca las carreras por usuario de acuerdo al periodo
	 * @param pracId - pracId id de la del periodo academico a buscar
	 */
	public void cambiarCarrera(int pracId){
		try {
			//ASIGNO EL PERIODO ACADEMICO EN EL QUE SE ESTA TRABAJANDO
//			pracId = ahafHorarioAcademicoBusq.getPracId();
			//SETEO LA CARRERA
			ahafHorarioAcademicoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL NIVEL
			ahafHorarioAcademicoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL PARALELO
			ahafHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			ahafHorarioAcademicoBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE CARRERA
			ahafListCarreraDtoBusq = null;
			//ANULO LA LISTA DE NIVEL
			ahafListNivelBusq = null;
			//ANULO LA LISTA DE PARALELO
			ahafListParaleloDtoBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoBusq = null;
			//LISTO LAS CARRERAS POR PERIODO ACADEMICO
			if(pracId != GeneralesConstantes.APP_ID_BASE){
				if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ahafListCarreraDtoBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ahafUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
				}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ahafListCarreraDtoBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(ahafUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
				}else if(ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					ahafListCarreraDtoBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ahafUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
				}
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.carrera.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca las carreras por usuario de acuerdo al periodo
	 * @param pracId - pracId id de la del periodo academico a buscar
	 */
	public void cambiarCarreraCompartir(int pracId){
		try {
			//SETEO LA CARRERA
			ahafHorarioAcademicoCompBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL NIVEL
			ahafHorarioAcademicoCompBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL PARALELO
			ahafHorarioAcademicoCompBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE CARRERA
			ahafListCarreraDtoCompBusq = null;
			//ANULO LA LISTA DE NIVEL
			ahafListNivelCompBusq = null;
			//ANULO LA LISTA DE PARALELO
			ahafListParaleloDtoCompBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoCompBusq = null;
			//LISTO LAS CARRERAS POR PERIODO ACADEMICO
			if(pracId != GeneralesConstantes.APP_ID_BASE){
				if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					//Habilita todas las carreras de la dependencia que pertenece el usuario
//					ahafListCarreraDtoCompBusq = servAhafCarreraDtoServicioJdbc.listarXfacultad(ahafMateriaCompartir.getCrrDpnId());
//					ahafListCarreraDtoCompBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ahafUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
					ahafListCarreraDtoCompBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoXFacultad(ahafUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId, ahafListaDependenciaNew);
				}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ahafListCarreraDtoCompBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(ahafUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
				}else if(ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					ahafListCarreraDtoCompBusq = servAhafCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoXFacultad(ahafUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId, ahafListaDependenciaNew);	
				}
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.carrera.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca el nivel por carrera
	 */
	public void cambiarNivel(){
		try {
			//SETEO EL NIVEL
			ahafHorarioAcademicoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL PARALELO
			ahafHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			ahafHorarioAcademicoBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE NIVEL
			ahafListNivelBusq = null;
			//ANULO LA LISTA DE PARALELO
			ahafListParaleloDtoBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoBusq = null;
			//LISTO LOS NIVELES
			if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				int contador = 0;
				for (CarreraDto itemCarr : ahafListCarreraDtoBusq) {
					if(itemCarr.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						contador = contador + 1;
					}
				}
				if(contador >= 1){
					ahafListNivelBusq = servAhafNivelServicio.listarNivelacion();
				}else{
					ahafListNivelBusq = servAhafNivelServicio.listarTodos();
				}
			}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ahafListNivelBusq = servAhafNivelServicio.listarTodosPosgrado();
			} else if(ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				//idiomas
				if(ahafTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL)){
					ahafListNivelBusq = servAhafNivelServicio.listarSuficienciaIdiomas();
				}
				//cultura fisica
				if(ahafTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL)){
					ahafListNivelBusq = servAhafNivelServicio.listarSuficienciaCulturaFisica();
				}
				//informatica
				if(ahafTipoCarrera.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL)){
					Nivel nivel = servAhafNivelServicio.buscarPorId(1);
					List<Nivel> niveles = new ArrayList<>();
					niveles.add(nivel);
					ahafListNivelBusq = niveles; 
				}
			}
		} catch (NivelNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.nivel.no.encontrado.exception")));
		} catch (NivelException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca el nivel por carrera
	 */
	public void cambiarNivelCompartir(){
		try {
			//SETEO EL NIVEL
			ahafHorarioAcademicoCompBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL PARALELO
			ahafHorarioAcademicoCompBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE NIVEL
			ahafListNivelCompBusq = null;
			//ANULO LA LISTA DE PARALELO
			ahafListParaleloDtoCompBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoCompBusq = null;
			//LISTO LOS NIVELES
			if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				int contador = 0;
				for (CarreraDto itemCarr : ahafListCarreraDtoCompBusq) {
					if(itemCarr.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						contador = contador + 1;
					}
				}
				if(contador >= 1){
					ahafListNivelCompBusq = servAhafNivelServicio.listarNivelacion();
				}else{
					ahafListNivelCompBusq = servAhafNivelServicio.listarTodos();
				}
			}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ahafListNivelCompBusq = servAhafNivelServicio.listarTodosPosgrado();
			}
		} catch (NivelNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.nivel.no.encontrado.exception")));
		} catch (NivelException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca los paralelos 
	 */
	public void cambiarParalelo(){
		try {
			//SETEO EL PARALELO
			ahafHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			ahafHorarioAcademicoBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE PARALELO
			ahafListParaleloDtoBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoBusq = null;
			//LISTO LOS PARALELOS
			if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				ahafListParaleloDtoBusq = servAhafParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getCrrId(), ahafHorarioAcademicoBusq.getNvlId());
			}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ahafListParaleloDtoBusq = servAhafParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivelPostgrado(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getCrrId(), ahafHorarioAcademicoBusq.getNvlId());
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.paralelo.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca los paralelos 
	 */
	public void cambiarParaleloCompartir(){
		try {
			//SETEO EL PARALELO
			ahafHorarioAcademicoCompBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			
			//ANULO LA LISTA DE PARALELO
			ahafListParaleloDtoCompBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoCompBusq = null;
			//LISTO LOS PARALELOS
			if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				ahafListParaleloDtoCompBusq = servAhafParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(ahafHorarioAcademicoCompBusq.getPracId(), ahafHorarioAcademicoCompBusq.getCrrId(), ahafHorarioAcademicoCompBusq.getNvlId());
			}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ahafListParaleloDtoCompBusq = servAhafParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivelPostgrado(ahafHorarioAcademicoCompBusq.getPracId(), ahafHorarioAcademicoCompBusq.getCrrId(), ahafHorarioAcademicoCompBusq.getNvlId());
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.paralelo.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca las asignaturas
	 */
	public void cambiarAsignatura(){
		try {
			//SETEO LA MATERIA
			ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoCompBusq = null;
			//BUSCO LAS ASIGNATURAS
			if(ahafHorarioAcademicoCompBusq.getPrlId() != GeneralesConstantes.APP_ID_BASE){
				//***********************************************************************************//
				//******************** CON CONTROL DE NUMERO DE HORAS O CREDITOS ********************//
				//***********************************************************************************//
				ahafListMateriaDtoCompBusq = servAhafMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlIdSinMateria(ahafHorarioAcademicoCompBusq.getPracId(), ahafHorarioAcademicoCompBusq.getCrrId(), ahafHorarioAcademicoCompBusq.getNvlId(), ahafHorarioAcademicoCompBusq.getPrlId(),ahafMateriaCompartir);
				
				//**********************************************************************************//
				//******************** SIN CONTROL DE NUMERO DE HORAS O CREDITOS *******************//
				//**********************************************************************************//
//				ahafListMateriaDtoCompBusq = servAhafMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlIdSinMateriaSinControlNumHorasCreditos(ahafHorarioAcademicoCompBusq.getPracId(), ahafHorarioAcademicoCompBusq.getCrrId(), ahafHorarioAcademicoCompBusq.getNvlId(), ahafHorarioAcademicoCompBusq.getPrlId(),ahafMateriaCompartir);
			}else{
				//SETEO LA MATERIA
				ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			}
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No dispone de asignaturas para compartir el horario académico, se necesita que no se encuentren asiganados horas para el proceso de compartir horarios");
		}
	}
	
	/**
	 * Método que busca materias del periodo , carrera , nivel y paralelo
	 */
	public void buscar(){
		try {
			//ANULO LA LISTA DE RESULTADOS
			ahafListMateriaDtoBusq = null;
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ahafHorarioAcademicoBusq.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.academico.no.seleccionado")));
			}else if(ahafHorarioAcademicoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.carrera.no.seleccionado")));
			}else if(ahafHorarioAcademicoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.nivel.no.seleccionado")));
			}else if(ahafHorarioAcademicoBusq.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else {
				//BUSCO LAS MATERIAS POR LOS PARAMETROS INGRESADOS
				// sin proceso de modulares
//				ahafListMateriaDtoBusq = servAhafMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getCrrId(), ahafHorarioAcademicoBusq.getNvlId(), ahafHorarioAcademicoBusq.getPrlId());
				//***********************************************************************************//
				//**************** busca materias con sub materias sin materias modulares ***********//
				//***********************************************************************************//
				ahafListMateriaDtoBusq = servAhafMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlIdModular(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getCrrId(), ahafHorarioAcademicoBusq.getNvlId(), ahafHorarioAcademicoBusq.getPrlId());
				//***********************************************************************************//
//				int salto = 0;
//				for (MateriaDto item : ahafListMateriaDtoBusq) {
//					if(item.getMtrHoras() == null && item.getMtrHoras() == 0 && item.getMtrCreditos() == null && item.getMtrCreditos() == 0 ){
//						salto = 1;
//						break;
//					}
//				}
//				if(salto == 1){
//					
//				}
			}
		} catch (MateriaDtoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.carrera.nivel.paralelo.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca materias del periodo , carrera , nivel , paralelo y  materia
	 */
	public void consultaHorario(){
		try {
			//ANULO LA LISTA DE HORARIOS PARA BUSCAR SI TIENE ASIGNADOS
			ahafListHorarioAcademicoCompBusq = null;
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ahafHorarioAcademicoCompBusq.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.academico.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.carrera.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.nivel.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else {
				//BUSCO LOS HORARIOS ACADEMICOS DE LA MATERIA
				ahafListHorarioAcademicoCompBusq = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoCompBusq.getPracId(), ahafHorarioAcademicoCompBusq.getPrlId(), ahafHorarioAcademicoCompBusq.getMtrId());
				if(ahafListHorarioAcademicoCompBusq.size() <= 0 || ahafListHorarioAcademicoCompBusq == null){
					bandera = 3;
				}else{
					bandera = 0;
					FacesUtil.mensajeError("No puede compartir este horario, ya tiene un horario académico definido");
				}
			}
		} catch (HorarioAcademicoDtoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
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
		iniciarParametros();
		return "irInicio";
	}
	
	/**
	 * Navega hacia la página de ver horario academico
	 * @param materiaDto - materiaDto entidad dto de materia
	 * @return  Navegacion a la pagina de ver horario academico.
	 */
	public String irVerHorario(MateriaDto materiaDto){
		String retorno = null;
		ahafMateriaVer = materiaDto;
		try {
			ahafDeshabilitaDia = false;
			//busca los horarios academicos por periodo paralelo y materia
			ahafListHorarioAcademicoBusq = new ArrayList<>();
			ahafListHorarioAcademicoBusq = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
			Integer HracDtpsId = GeneralesConstantes.APP_ID_BASE;
			Integer mlcrpr = GeneralesConstantes.APP_ID_BASE;
			if(ahafListHorarioAcademicoBusq.size() > 0){ // si existe al menos un registro
				for (HorarioAcademicoDto item : ahafListHorarioAcademicoBusq) {
					HracDtpsId = item.getHracMlcrprIdComp();
					mlcrpr = item.getMlcrprId();
					if(item.getPrsId() != null){
						ahafMateriaVer.setPrsNombres(item.getPrsNombres());
						ahafMateriaVer.setPrsPrimerApellido(item.getPrsPrimerApellido());
						ahafMateriaVer.setPrsSegundoApellido(item.getPrsSegundoApellido());
						HracDtpsId = item.getHracMlcrprIdComp();
						break;
					}
				}
			}else{ // no existe registros
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.ver.horario.horario.academico.no.existe")));
			}
			
			//busca el docente de la materia
			List<DocenteJdbcDto> verificaCompartida = new ArrayList<>();
			verificaCompartida = servAhafDocenteDtoServicioJdbc.listarHorarioXpracIdXprlIdXmtrIdDocente(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId(), HracDtpsId.intValue());
			if(verificaCompartida.size() > 0){//obtengo docente de la padre (normal)
				ahafListDocenteJdbcDto = verificaCompartida;
			}else{
				if(HracDtpsId != GeneralesConstantes.APP_ID_BASE && HracDtpsId != 0 && mlcrpr != GeneralesConstantes.APP_ID_BASE && mlcrpr != 0){//obtengo docente de la compartida padre
					ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioXpracIdXprlIdXmtrIdDocente(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId(), mlcrpr.intValue());
				}else{//obtengo docente de la compartida hija
					ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioXpracIdXprlIdXmtrIdDocenteCompratida(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId(), mlcrpr.intValue());
				}
			}
//			ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioXpracIdXprlIdXmtrIdDocente(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId(), HracDtpsId.intValue());

			if(ahafListDocenteJdbcDto.size() <= 0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.ver.horario.docente.no.existe")));
			}
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DocenteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  
		retorno = "irVerHorario";
		return retorno;
	}
	
	/**
	 * Métódo que dirige hacia el listar horarios academicos, materias
	 */
	public String irListarHorario(){
		String retorno = null;
		//ANULO LA LISTA DE HORARIO ACADEMICO BUSQ
		ahafListHorarioAcademicoBusq = null;
		//ANULO LA LISTA DE DOCENTE
		ahafListDocenteJdbcDto = null;
		//inicializo los parametros para uno nuevo
		ahafListAulaDtoNew = null;
		ahafListHoraClaseDtoNew = null;
//		ahafListaDependenciaNew = null;
		ahafListEdificioDtoNew = null;
		ahafHorarioAcademicoNew.setEdfId(GeneralesConstantes.APP_ID_BASE);
		ahafHorarioAcademicoNew.setDpnId(GeneralesConstantes.APP_ID_BASE);
		ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
		ahafHorarioAcademicoNew.setAlaId(GeneralesConstantes.APP_ID_BASE);
		ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
		ahafDocente = new DocenteJdbcDto();
		retorno = "irHorarioAcademico";
		return retorno;
	}
	
	/**
	 * Método que navega hacia la pagina de editar horario academico
	 * @param entidadEditar - entidadEditar entidad horario academico ha editar
	 * @return  Navegacion a la pagina de editar horario academico.
	 */
	public String irEditarHorarios(HorarioAcademicoDto entidadEditar){
		//editar
		String retorno = null;
		try {
			ahafHabilitaAulaEdit = GeneralesConstantes.APP_ID_BASE;
			//VERIFICO QUE NO TENGA MATRICULADOS PARA EDITAR
			List<MallaCurricularParaleloDto> mallaCurricularParaleloMatriculados = new ArrayList<>();
			if(ahafTipoUsuario != RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){ //no hay contron de inscritos para suficiencias
				mallaCurricularParaleloMatriculados = servAhafMallaCurricularParaleloDtoServicioJdbc.buscarMatriculadosMlcrprXMateriaXParalelo(ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId());
			}
			if(mallaCurricularParaleloMatriculados.size() > 0){
				FacesUtil.mensajeError("No puede cambiar horarios, existen estudiantes matriculados, únicamente puede realizar el cambio de aula si es requerido");
				bloqueaModal();
				ahafHabilitaAulaEdit = 0;
				ahafListAulaDtoEdit = null;

			}
			//VERIFICO SI ES PARA EDITAR
			if(entidadEditar.getHracId() != null){
				bandera = 2;
			}
			//ASIGNO A EL HORARIO ACADEMICO PARA EDITAR
			ahafHorarioAcademicoEdit = entidadEditar;
			//asigno la hora
			ahafHoraCambioAulaEdit = entidadEditar.getHoclId();
			//asigno el dia
			ahafDiaCambioAula = entidadEditar.getHracDia();
			//busca dependencias de acuerdo al usuario
			ahafListaDependenciaNew = servAhafDependenciaDtoServicioJdbc.listarXUsuario(ahafUsuario.getUsrId());
			//busca lisa de edificios de aucerdo a la dependencia
			ahafListEdificioDtoNew = servAhafEdificioDtoServicioJdbc.listarXDependenciaXEstadoActivo(ahafHorarioAcademicoEdit.getDpnId(), ahafListaDependenciaNew);
			if(ahafHabilitaAulaEdit == 0){ //si es cambio de aula
				ahafListAulaDtoEdit = null;
				//					ahafHorarioAcademicoEdit.setAlaId(GeneralesConstantes.APP_ID_BASE); //ojo
				List<AulaDto> ahafListaAulaOcupada = new ArrayList<>();
				ahafListaAulaOcupada = null;
				ahafListaAulaOcupada = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificioXHoraXDia(ahafUsuario.getUsrId(), ahafHorarioAcademicoEdit.getDpnId(), ahafHorarioAcademicoEdit.getEdfId(), ahafHorarioAcademicoEdit.getHoclId(), ahafHorarioAcademicoEdit.getHracDia());

				//lista de todas las aulas del edificio
				List<AulaDto> ahafListaAulaTodas = new ArrayList<>();
				ahafListaAulaTodas = null;
				ahafListaAulaTodas = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificio(ahafUsuario.getUsrId(), ahafHorarioAcademicoEdit.getDpnId(), ahafHorarioAcademicoEdit.getEdfId());
				ahafListAulaDtoEdit = new ArrayList<>();

				boolean agrega = true;
				for (AulaDto itemTodas : ahafListaAulaTodas) {
					agrega = true;
					for (AulaDto itemOcupadas : ahafListaAulaOcupada) {
						if(itemTodas.getAlaId() == itemOcupadas.getAlaId()){
							agrega = false;
							break;
						}
					}
					if(agrega){
						ahafListAulaDtoEdit.add(itemTodas);
					}
				}

				//anulo la lista si selecciona seleccione
				if(ahafHorarioAcademicoEdit.getEdfId() == GeneralesConstantes.APP_ID_BASE){
					ahafListAulaDtoEdit = null;
				}
			}else{//si es edición de horas - clase
				//ASIGNO LA LISTA DE AULAS
				//					ahafListAulaDtoEdit = servAhafAulaDtoServicioJdbc.listarPusrIdPdpnIdP(ahafUsuario.getUsrId(), ahafMateriaVer.getCrrDpnId());
				ahafListAulaDtoEdit = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificio(ahafUsuario.getUsrId(), ahafHorarioAcademicoEdit.getDpnId(), ahafHorarioAcademicoEdit.getEdfId());
				//ASIGNO LA LISTA DE HORA CLASE DISPONIBLE
				ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), ahafListHorarioAcademicoBusq);
				//BUSCO LISTA DOCENTE
				ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
			}
			retorno = "irEditarHorario";
		} catch (AulaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.editar.horario.aula.no.encontrado.exception")));
		} catch (HoraClaseDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.editar.hora.clase.no.encontrado.exception")));
		} catch (DependenciaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.editar.dependencia.no.encontrado.exception")));
		} catch (EdificioDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EdificioDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.editar.edificio.no.encontrado.exception")));
		} catch (DocenteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	/**
	 * Método que busca los edificios de acuerdo a la dependencia seleccionada
	 */
	public void cambiarDependencia(){
		try {
			if(bandera == 0){ //cuando hubo problemas
				FacesUtil.mensajeError("No se puede cargar las edificios, por favor intente nuevamente.");
			}else if(bandera == 1){ // cuando es nuevo
				//busca lisa de edificios de aucerdo a la dependencia
				ahafListEdificioDtoNew = servAhafEdificioDtoServicioJdbc.listarXDependenciaXEstadoActivo(ahafHorarioAcademicoNew.getDpnId(), ahafListaDependenciaNew);
				//anulo la lista si selecciona seleccione
				if(ahafHorarioAcademicoNew.getDpnId() == GeneralesConstantes.APP_ID_BASE){
					ahafListEdificioDtoNew = null;
				}
				//SETEO VALORES INICIALES
				ahafHorarioAcademicoNew.setEdfId(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoNew.setAlaId(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
				ahafListAulaDtoNew = null;
				ahafListHoraClaseDtoNew = null;
			}else if(bandera == 2){ // cuando es edición
				//busca lisa de edificios de aucerdo a la dependencia
				ahafListEdificioDtoNew = servAhafEdificioDtoServicioJdbc.listarXDependenciaXEstadoActivo(ahafHorarioAcademicoEdit.getDpnId(), ahafListaDependenciaNew);
				//anulo la lista si selecciona seleccione
				if(ahafHorarioAcademicoEdit.getDpnId() == GeneralesConstantes.APP_ID_BASE){
					ahafListEdificioDtoNew = null;
				}
				//SETEO VALORES INICIALES
				ahafHorarioAcademicoEdit.setEdfId(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoEdit.setAlaId(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoEdit.setHracDia(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoEdit.setHoclId(GeneralesConstantes.APP_ID_BASE);
				ahafListAulaDtoEdit = null;
				ahafListHoraClaseDtoEdit = null;
			}
		} catch (EdificioDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EdificioDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.dependencia.edificio.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca aulas de acuerdo al edificio seleccionado, sea para edicion o nuevos horarios
	 */
	public void cambiarEdificio(){
		try {
			if(bandera == 0){ //cuando hubo problemas
				FacesUtil.mensajeError("No se puede cargar las aulas, por favor intente nuevamente.");
			}else if(bandera == 1){ //busca lista de aulas cuando es nueva
				ahafListAulaDtoNew = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificio(ahafUsuario.getUsrId(), ahafHorarioAcademicoNew.getDpnId(), ahafHorarioAcademicoNew.getEdfId());
				//anulo la lista si selecciona seleccione
				if(ahafHorarioAcademicoNew.getEdfId() == GeneralesConstantes.APP_ID_BASE){
					ahafListAulaDtoNew = null;
				}
				//SETEO VALORES INICIALES
				ahafHorarioAcademicoNew.setAlaId(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
				ahafListHoraClaseDtoNew = null;

			} else if(bandera == 2){ //busca lista de aulas cuando es editar
				ahafListAulaDtoEdit = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificio(ahafUsuario.getUsrId(), ahafHorarioAcademicoEdit.getDpnId(), ahafHorarioAcademicoEdit.getEdfId());
				//anulo la lista si selecciona seleccione
				if(ahafHorarioAcademicoEdit.getEdfId() == GeneralesConstantes.APP_ID_BASE){
					ahafListAulaDtoEdit = null;
				}
				//SETEO VALORES INICIALES
				ahafHorarioAcademicoEdit.setAlaId(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoEdit.setHracDia(GeneralesConstantes.APP_ID_BASE);
				ahafHorarioAcademicoEdit.setHoclId(GeneralesConstantes.APP_ID_BASE);
				ahafListHoraClaseDtoEdit = null;
			}
		} catch (AulaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca aulas de acuerdo al edificio seleccionado, sea para edicion del cambio del aula
	 */
	public void cambiarEdificioAula(){
		try {
			if(bandera == 0){ //cuando hubo problemas
				FacesUtil.mensajeError("No se puede cargar las aulas, por favor intente nuevamente.");
			}else if(bandera == 1){ //busca lista de aulas cuando es nueva

			} else if(bandera == 2){ //busca lista de aulas cuando es editar
				List<AulaDto> ahafListaAulaOcupada = new ArrayList<>();
				ahafListaAulaOcupada = null;
				ahafListaAulaOcupada = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificioXHoraXDia(ahafUsuario.getUsrId(), ahafHorarioAcademicoEdit.getDpnId(), ahafHorarioAcademicoEdit.getEdfId(), ahafHoraCambioAulaEdit, ahafDiaCambioAula);
				
				//lista de todas las aulas del edificio
				List<AulaDto> ahafListaAulaTodas = new ArrayList<>();
				ahafListaAulaTodas = null;
				ahafListaAulaTodas = servAhafAulaDtoServicioJdbc.listarXUsuarioXDependenciaXEdificio(ahafUsuario.getUsrId(), ahafHorarioAcademicoEdit.getDpnId(), ahafHorarioAcademicoEdit.getEdfId());
				ahafListAulaDtoEdit = new ArrayList<>();
				
				boolean agrega = true;
				for (AulaDto itemTodas : ahafListaAulaTodas) {
					agrega = true;
					for (AulaDto itemOcupadas : ahafListaAulaOcupada) {
						if(itemTodas.getAlaId() == itemOcupadas.getAlaId()){
							agrega = false;
							break;
						}
					}
					if(agrega){
						ahafListAulaDtoEdit.add(itemTodas);
					}
				}
				//anulo la lista si selecciona seleccione
				if(ahafHorarioAcademicoEdit.getEdfId() == GeneralesConstantes.APP_ID_BASE){
					ahafListAulaDtoEdit = null;
				}
			}
		} catch (AulaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	/**
	 * Método que busca o habilita los días para el cambio de aula
	 */
	public void cambiarAula(){
		Aula capacidadAula = new Aula();
		if(bandera == 0){ //cuando hubo problemas
			FacesUtil.mensajeError("No se puede cargar las horas, por favor intente nuevamente.");
		}else if(bandera == 1){ //cuando es nuevo
			try {
				//SETEO EL DÍA NUEVO
				ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
				//SETEO LA HORA CLASE NUEVO
				ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
				//ANULO LA LISTA DE HORA CLASE NUEVO
				ahafListHoraClaseDtoNew = null;
				
				//VALIDO LA CAPACIDAD DEL AULA CON EL PARALELO
				capacidadAula = servAhafAulaServicio.buscarPorId(ahafHorarioAcademicoNew.getAlaId().intValue());
				if(ahafMateriaVer.getMlcrprCupo() <= capacidadAula.getAlaCapacidad().intValue()){
					ahafDeshabilitaDia = false;
				}else{
					ahafDeshabilitaDia = true;
					FacesUtil.mensajeError("El aula: "+capacidadAula.getAlaDescripcion()+", con capacidad: "+capacidadAula.getAlaCapacidad()+", no abastese al paralelo: "+ahafMateriaVer.getPrlDescripcion()+" con cupo de: "+ahafMateriaVer.getMlcrprCupo());
				}
			} catch (AulaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (AulaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		}else if(bandera == 2){ //cuando es edición
			try {
				//SETEO EL DÍA
				ahafHorarioAcademicoEdit.setHracDia(GeneralesConstantes.APP_ID_BASE);
				//SETEO LA HORA CLASE
				ahafHorarioAcademicoEdit.setHoclId(GeneralesConstantes.APP_ID_BASE);
				//ANULO LA LISTA DE HORA CLASE EDITAR
				ahafListHoraClaseDtoEdit = null;
				
				//VALIDO LA CAPACIDAD DEL AULA CON EL PARALELO
				capacidadAula = servAhafAulaServicio.buscarPorId(ahafHorarioAcademicoEdit.getAlaId().intValue());
				if(ahafMateriaVer.getMlcrprCupo() <= capacidadAula.getAlaCapacidad().intValue()){
					ahafDeshabilitaDia = false;
				}else{
					ahafDeshabilitaDia = true;
					FacesUtil.mensajeError("El aula: "+capacidadAula.getAlaDescripcion()+", con capacidad: "+capacidadAula.getAlaCapacidad()+", no abastese al paralelo: "+ahafMateriaVer.getPrlDescripcion()+" con cupo de: "+ahafMateriaVer.getMlcrprCupo());
				}
			} catch (AulaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (AulaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		}
	}
	
	/**
	 * Método que busca o habilita la hora clase disponible
	 */
	public void cambiarDia(){
		try {
			if(bandera == 0){ //cuando hubo problemas
				FacesUtil.mensajeError("No se puede cargar las horas, por favor intente nuevamente.");
			}else if(bandera == 1){ //cuando es nuevo
				//si se selecciona seleccione, para anular la lista
				if(ahafHorarioAcademicoNew.getHracDia() == GeneralesConstantes.APP_ID_BASE){
					ahafListHoraClaseDtoNew = null;
				}else {
					//*************************************************************************//
					//************** CONTROL TOTAL DE CRUCES DE HORARIOS PARA TODOS ***********//
					//*************************************************************************//
					//LISTA DE HORARIO TOTAL DE LA MATERIA  //DIA - HORA CLASE
					//HORARIO POR DIA SIN AULA
					List<HorarioAcademicoDto> listaConsultaHorarioAux = new ArrayList<>();
					//					listaConsultaHorarioAux = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafMateriaVer.getPracId(), ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId());
					listaConsultaHorarioAux = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrIdPhracDia(ahafMateriaVer.getPracId(), ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId(), ahafHorarioAcademicoNew.getHracDia());
					//ASIGNO LA LISTA DE HORA CLASE DISPONIBLE
					//				ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), ahafListHorarioAcademicoBusq);
					//					ahafListHoraClaseDtoNew = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoNew.getAlaId().intValue(), ahafHorarioAcademicoNew.getHracDia().intValue(), ahafMateriaVer.getPracId(), listaConsultaHorarioAux);
					ahafListHoraClaseDtoNew = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHoclNuevo(ahafHorarioAcademicoNew.getAlaId().intValue(), ahafHorarioAcademicoNew.getHracDia().intValue(), ahafMateriaVer.getPracId(), listaConsultaHorarioAux);
					//*************************************************************************//
					
					//*************************************************************************//
					//********************* CONTROL CON MATERIAS MODULARES ********************//
					//*************************************************************************//
					//LISTA DE HORARIO TOTAL DE LA MATERIA  //DIA - HORA CLASE
					
//					if(ahafMateriaVer.getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
//						ahafListHoraClaseDtoNew = servAhafHoraClaseDtoServicioJdbc.listarTodos();
//					}else{
//						//HORARIO POR DIA SIN AULA
//						List<HorarioAcademicoDto> listaConsultaHorarioAux = new ArrayList<>();
//						//					listaConsultaHorarioAux = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafMateriaVer.getPracId(), ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId());
//						listaConsultaHorarioAux = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrIdPhracDia(ahafMateriaVer.getPracId(), ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId(), ahafHorarioAcademicoNew.getHracDia());
//						//ASIGNO LA LISTA DE HORA CLASE DISPONIBLE
//						//				ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), ahafListHorarioAcademicoBusq);
//						//					ahafListHoraClaseDtoNew = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoNew.getAlaId().intValue(), ahafHorarioAcademicoNew.getHracDia().intValue(), ahafMateriaVer.getPracId(), listaConsultaHorarioAux);
//						ahafListHoraClaseDtoNew = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHoclNuevo(ahafHorarioAcademicoNew.getAlaId().intValue(), ahafHorarioAcademicoNew.getHracDia().intValue(), ahafMateriaVer.getPracId(), listaConsultaHorarioAux);
//					}
					
					//*************************************************************************//
				}
			}else if(bandera == 2){ //cuando es edición
				//*************************************************************************//
				//************** CONTROL TOTAL DE CRUCES DE HORARIOS PARA TODOS ***********//
				//*************************************************************************//
				//SETEO LA HORA CLASE
				ahafHorarioAcademicoEdit.setHoclId(GeneralesConstantes.APP_ID_BASE);
				//si se selecciona seleccione, para anular la lista
				if(ahafHorarioAcademicoEdit.getHracDia() == GeneralesConstantes.APP_ID_BASE){
					ahafListHoraClaseDtoEdit = null;
				}else {
					//LISTA DE HORARIO TOTAL DE LA MATERIA  //DIA - HORA CLASE
					//HORARIO POR DIA SIN AULA
					List<HorarioAcademicoDto> listaConsultaHorario = new ArrayList<>();
					listaConsultaHorario = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrIdPhracDia(ahafMateriaVer.getPracId(), ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId(), ahafHorarioAcademicoEdit.getHracDia());
					//ASIGNO LA LISTA DE HORA CLASE DISPONIBLE
					//				ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), ahafListHorarioAcademicoBusq);
					ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), listaConsultaHorario);
				}
				//*************************************************************************//
				
				//*************************************************************************//
				//********************* CONTROL CON MATERIAS MODULARES ********************//
				//*************************************************************************//
//				if(ahafMateriaVer.getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
//					ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarTodos();
//				}else{
//					//SETEO LA HORA CLASE
//					ahafHorarioAcademicoEdit.setHoclId(GeneralesConstantes.APP_ID_BASE);
//					//si se selecciona seleccione, para anular la lista
//					if(ahafHorarioAcademicoEdit.getHracDia() == GeneralesConstantes.APP_ID_BASE){
//						ahafListHoraClaseDtoEdit = null;
//					}else {
//						//LISTA DE HORARIO TOTAL DE LA MATERIA  //DIA - HORA CLASE
//						//HORARIO POR DIA SIN AULA
//						List<HorarioAcademicoDto> listaConsultaHorario = new ArrayList<>();
//						listaConsultaHorario = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrIdPhracDia(ahafMateriaVer.getPracId(), ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId(), ahafHorarioAcademicoEdit.getHracDia());
//						//ASIGNO LA LISTA DE HORA CLASE DISPONIBLE
//						//				ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), ahafListHorarioAcademicoBusq);
//						ahafListHoraClaseDtoEdit = servAhafHoraClaseDtoServicioJdbc.listarPalaIdPhracDiaPpracIdPlistHocl(ahafHorarioAcademicoEdit.getAlaId().intValue(), ahafHorarioAcademicoEdit.getHracDia().intValue(), ahafMateriaVer.getPracId(), listaConsultaHorario);
//					}
//				}
				//*************************************************************************//
			}
		} catch (HoraClaseDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.dia.hora.clase.no.encontrado.exception")));
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.dia.horario.academico.no.encontrado.exception")));
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que guarda el editar del horario academico
	 * @return retorna hacia la navegación de la pagina de listar
	 */
	public String guardarEditar(){
		String retorno = null;
		try {
			//INSTANCIO LA MALLA CURRICULAR MATERIA
			HorarioAcademico entidad = new HorarioAcademico();
			//SETEO DE VALORES
			entidad.setHracId(ahafHorarioAcademicoEdit.getHracId());  //seteo el id
			entidad.setHracDia(ahafHorarioAcademicoEdit.getHracDia()); //seteo el día
			entidad.setHracEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE); //seteo el estado
			if(ahafHabilitaAulaEdit == 0){
				entidad.setHracDia(ahafDiaCambioAula); //seteo el día
				entidad.setHracHoraClaseAula(servAhafHoraClaseAulaServicio.buscarPalaIdPhoclId(ahafHorarioAcademicoEdit.getAlaId(), ahafHoraCambioAulaEdit));
			}else{
				entidad.setHracHoraClaseAula(servAhafHoraClaseAulaServicio.buscarPalaIdPhoclId(ahafHorarioAcademicoEdit.getAlaId(), ahafHorarioAcademicoEdit.getHoclId()));
			}
			entidad.setHracMallaCurricularParalelo(servAhafMallaCurricularParaleloServicio.buscarPorId(ahafHorarioAcademicoEdit.getMlcrprId()));

			if(entidad.getHracDia() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.no.guardado.dia")));
			}else if(entidad.getHracEstado() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.no.guardado.estado")));
			}else if(entidad.getHracHoraClaseAula() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.no.guardado.hora.clase.aula")));
			}else if(entidad.getHracMallaCurricularParalelo() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.no.guardado.malla.curricular.paralelo")));
				//PROCESO QUE EDITA LA INFORMACIÓN DE HORARIO ACADEMICO
			}else if(servAhafHorarioAcademicoServicio.editar(entidad)){ // si se guarda la edición devuelve verdadero
				ahafDeshabilitaDia = false;
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.exitoso")));
				retorno = irVerHorario(ahafMateriaVer);
			}else{ // caso que no se ejecute el editar
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.no.guardado")));
			}
			bloqueaModal(); //bloquea el modal para que no se ejecute nuevamente
		}  catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.editar.exception")));
		}
		bandera = 0; //encero la variable
		return retorno;
	}
	
	/**
	 * Método que busca la carrera por facultad para docentes
	 */
	public void cambiarCarreraDocente(){
		try {
			//SETEO LA CARRERA
			ahafDocenteJdbcDtoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//LISTO LAS CARRERAS POR FACULTAD
			if(ahafTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || ahafTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				ahafListCarreraBusq =  servAhafCarreraServicio.listarCarrerasXFacultad(ahafDocenteJdbcDtoBusq.getDpnId());
			}else if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ahafListCarreraBusq = servAhafCarreraServicio.listarCarrerasXFacultadPosgrado(ahafDocenteJdbcDtoBusq.getDpnId());
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
	 * Método que busca la lista los docentes por facultad , carrera identificación y apellido paterno
	 */
	public void buscarDocente(){
		try {
//			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
//			if(ahafDocenteJdbcDtoBusq.getDpnId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError("mensaje",MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.docente.facultad.no.seleccionado")));
//			}else if(ahafDocenteJdbcDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError("mensaje",MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.docente.carrera.no.seleccionado")));
//			}else {
//				// INSTANCIO LA LISTA DE MATERIAS A BUSCAR
//				ahafListDocenteJdbcDtoBusq = new ArrayList<DocenteJdbcDto>();
//				//BUSCO DOCENTES POR PARAMETROS INGRESADOS
//				ahafListDocenteJdbcDtoBusq = servAhafDocenteDtoServicioJdbc.listarPcrrIdPprsPrimerApellidoPprsIdentificacion(ahafDocenteJdbcDtoBusq.getCrrId(), ahafDocenteJdbcDtoBusq.getPrsPrimerApellido(), ahafDocenteJdbcDtoBusq.getPrsIdentificacion());
//			}
			// INSTANCIO LA LISTA DE MATERIAS A BUSCAR
			ahafListDocenteJdbcDtoBusq = new ArrayList<DocenteJdbcDto>();
			//BUSCO DOCENTES POR PARAMETROS INGRESADOS
			boolean buscar = true;
			if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				if(ahafDocenteJdbcDtoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
					buscar = false;
					FacesUtil.mensajeError("mensaje","Debe seleccionar el programa");
				}
			}
			if(buscar){
				ahafListDocenteJdbcDtoBusq = servAhafDocenteDtoServicioJdbc.listarPcrrIdPprsPrimerApellidoPprsIdentificacionPTipoRol(ahafDocenteJdbcDtoBusq.getCrrId(), ahafDocenteJdbcDtoBusq.getPrsPrimerApellido().toUpperCase(), ahafDocenteJdbcDtoBusq.getPrsIdentificacion(),ahafTipoUsuario);
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje",MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.docente.puesto.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que cierra el modal y actualiza el docente
	 */
	public void actualizaDocente(){
		bloqueaModal(); // bloquea el modal para que no se ejecute nuevamente
		limpiarModal(); // limpia los parametros
	}
	
	/**
	 * Método que limpia el modal de busqueda de materias
	 */
	public void limpiarModal(){
		//SETEO LA FACULTAD
		ahafDocenteJdbcDtoBusq.setDpnId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA
		ahafDocenteJdbcDtoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL APELLIDO
		ahafDocenteJdbcDtoBusq.setPrsPrimerApellido("");
		//SETEO LA IDENTIFICACIÓN
		ahafDocenteJdbcDtoBusq.setPrsIdentificacion("");
		if(ahafTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
			//ANULO LA LISTA DE CARRERAS
			ahafListCarreraBusq = null;
		}
		//ANULO LA LISTA DE DOCENTES A BUSCAR
		ahafListDocenteJdbcDtoBusq = null;
	}
	
	/**
	 * Método que agrega docente a la materia
	 * @param materia - materia entidad dto materia para ser agregada
	 */
	public void agregarDocente(DocenteJdbcDto docente){
		ahafDocente = docente;
		try {
			//PROCESO PARA AGREGAR DOCENTE
			//EDITA
			if(ahafListDocenteJdbcDto.size() >= 1){ 
				Integer crhrDtpsIdBusq = null;
				Integer mlcrprIdBusq = null;
				Integer crhrIdBusq = null;
				//busco id docente en lista agregada
				for (DocenteJdbcDto item : ahafListDocenteJdbcDto) {
					if(item.getCrhrDtpsId() != null){
						crhrDtpsIdBusq = item.getCrhrDtpsId();
						mlcrprIdBusq = item.getCrhrMlcrprId();
						crhrIdBusq = item.getCrhrId();
						break;
					}
				}
				//VERIFICO QUE ESTEN LLENOS TODOS LOS PARAMETROS
				if(crhrDtpsIdBusq != null && crhrDtpsIdBusq != docente.getDtpsId()){
					//edita
					
					//busco detalle puesto
					DetallePuesto detallePuesto = new DetallePuesto();
					detallePuesto = servAhafDetallePuestoServicio.buscarPorId(ahafDocente.getDtpsId());
					//busco periodo academico
					PeriodoAcademico periodoAcademico = new PeriodoAcademico();
					periodoAcademico = servAhafPeriodoAcademicoServicio.buscarPorId(ahafMateriaVer.getPracId());
					
					//******************************************************************************************************************//
					//*******************************SIN CONTROL DE CRUCE DOCENTE MODULARES ********************************************//
					//******************************************************************************************************************//
//					if(ahafMateriaVer.getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
//						//******************************************************************************************************************//
//						//*******************************SIN CONTROL DE CRUCE DOCENTE ******************************************************//
//						//******************************************************************************************************************//
//						//ejecuto la transacción de carga horaria
////						if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras(), ahafMateriaVer.getMtrCreditos())){
//						if(servAhafCargaHorariaServicio.editarDocente(crhrIdBusq, ahafDocente.getDtpsId(), mlcrprIdBusq, ahafMateriaVer.getPracId(), ahafMateriaVer.getMtrHoras(),ahafMateriaVer.getMtrCreditos())){
//							//busca el docente de la materia
//							ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.exitoso")));
//						}else{
//							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.editado")));
//						}
//						//******************************************************************************************************************//
//						//******************************************************************************************************************//
//					}else{
//						
//					}
					//******************************************************************************************************************//
					
					//******************************************************************************************************************//
					//*******************************CON CONTROL DE CRUCE DOCENTE ******************************************************//
					//******************************************************************************************************************//
					//busco el horario academico del docente
					ahafListHorarioAcademicoDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXPracIdXDtpsId(periodoAcademico.getPracId(), detallePuesto.getDtpsId());
					int cruce = 0;
					if(ahafListHorarioAcademicoDocente.size() <= 0){ //no tiene asignado horario academico
//						//ejecuto la transacción de carga horaria
//						if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras())){
//							//busca el docente de la materia
//							ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
//						}else{
//							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
//						}
						cruce = 0;
					}else{ //tiene asignado carga horaria, compara con el horario del paralelo y materia
						if(ahafListHorarioAcademicoBusq.size() <= 0){
//							//ejecuto la transacción de carga horaria
//							if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras())){
//								//busca el docente de la materia
//								ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//								FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
//							}else{
//								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
//							}
							cruce = 0;
						}else{
							//comparo los cruces del horario docente y el horario del paralelo actual
							for (HorarioAcademicoDto itemDocente : ahafListHorarioAcademicoDocente) {
								for (HorarioAcademicoDto itemHorario : ahafListHorarioAcademicoBusq) {
									if(itemDocente.getHracDia() == itemHorario.getHracDia() && itemDocente.getHoclId() == itemHorario.getHoclId()){
										FacesUtil.mensajeError("No puede asignar el docente: "+ahafDocente.getPrsPrimerApellido()+" "+ahafDocente.getPrsSegundoApellido()+" "+ahafDocente.getPrsNombres()+" en el día: "+HorarioAcademicoConstantes.traerDiaHorarioAcademico(itemHorario.getHracDia())+" de: "+itemHorario.getHoclDescripcion()+" , tiene cruce de horas en el paralelo: "+itemDocente.getPrlDescripcion()+" , en la materia: "+itemDocente.getMtrDescripcion());
										cruce = cruce + 1;
										break;
									}
								}
							}
						}
					}
					if(cruce == 0){
						//ejecuto la transacción de carga horaria
//						if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras(), ahafMateriaVer.getMtrCreditos())){
						if(servAhafCargaHorariaServicio.editarDocente(crhrIdBusq, ahafDocente.getDtpsId(), mlcrprIdBusq, ahafMateriaVer.getPracId(), ahafMateriaVer.getMtrHoras(),ahafMateriaVer.getMtrCreditos())){
							//busca el docente de la materia
							ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.exitoso")));
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.editado")));
						}
					}
					//******************************************************************************************************************//
					//******************************************************************************************************************//
					
					
					
//					if(servAhafCargaHorariaServicio.editarDocente(crhrIdBusq, docente.getDtpsId(), mlcrprIdBusq, ahafMateriaVer.getPracId(), ahafMateriaVer.getMtrHoras(),ahafMateriaVer.getMtrCreditos())){
//						ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.exitoso")));
//					}else{
//						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.editado")));
//					}
					
					
				}else{
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.selecciona.mismo.docente")));
				}
				//AGREGA
			}else{ 
				//AGREGAR EL DOCENTE A CARGA HORARIA
				//busco detalle puesto
				DetallePuesto detallePuesto = new DetallePuesto();
				detallePuesto = servAhafDetallePuestoServicio.buscarPorId(docente.getDtpsId());
				//busco malla curricular paralelo
				MallaCurricularParalelo mallaCurricularParalelo = new MallaCurricularParalelo();
				mallaCurricularParalelo = servAhafMallaCurricularParaleloServicio.buscarPorId(ahafMateriaVer.getMlcrprId());
				//busco periodo academico
				PeriodoAcademico periodoAcademico = new PeriodoAcademico();
				periodoAcademico = servAhafPeriodoAcademicoServicio.buscarPorId(ahafMateriaVer.getPracId());
				
				//******************************************************************************************************************//
				//*******************************SIN CONTROL DE CRUCE DOCENTE PARA MODULARES ***************************************//
				//******************************************************************************************************************//
//				if(ahafMateriaVer.getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
//					//******************************************************************************************************************//
//					//*******************************SIN CONTROL DE CRUCE DOCENTE ******************************************************//
//					//******************************************************************************************************************//
//					//ejecuto la transacción de carga horaria
//					if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras(), ahafMateriaVer.getMtrCreditos())){
//						//busca el docente de la materia
//						ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
//					}else{
//						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
//					}
//					//******************************************************************************************************************//
//					//******************************************************************************************************************//
//				}else{
//					
//				}
				//******************************************************************************************************************//
				
				//******************************************************************************************************************//
				//*******************************CON CONTROL DE CRUCE DOCENTE ******************************************************//
				//******************************************************************************************************************//
				//busco el horario academico del docente
				ahafListHorarioAcademicoDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXPracIdXDtpsId(periodoAcademico.getPracId(), detallePuesto.getDtpsId());
				int cruce = 0;
				if(ahafListHorarioAcademicoDocente.size() <= 0){ //no tiene asignado horario academico
//					//ejecuto la transacción de carga horaria
//					if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras())){
//						//busca el docente de la materia
//						ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
//					}else{
//						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
//					}
					cruce = 0;
				}else{ //tiene asignado carga horaria, compara con el horario del paralelo y materia
					if(ahafListHorarioAcademicoBusq.size() <= 0){
//						//ejecuto la transacción de carga horaria
//						if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras())){
//							//busca el docente de la materia
//							ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
//						}else{
//							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
//						}
						cruce = 0;
					}else{
						//comparo los cruces del horario docente y el horario del paralelo actual
						for (HorarioAcademicoDto itemDocente : ahafListHorarioAcademicoDocente) {
							for (HorarioAcademicoDto itemHorario : ahafListHorarioAcademicoBusq) {
								if(itemDocente.getHracDia() == itemHorario.getHracDia() && itemDocente.getHoclId() == itemHorario.getHoclId()){
//									FacesUtil.mensajeError("No puede asignar el horario en el día: "+HorarioAcademicoConstantes.traerDiaHorarioAcademico(itemHorario.getHracDia())+" de: "+itemHorario.getHoclDescripcion()+" , cruze de horas en el paralelo: "+itemDocente.getPrlDescripcion()+" , en la materia: "+itemDocente.getMtrDescripcion());
									FacesUtil.mensajeError("No puede asignar el docente: "+ahafDocente.getPrsPrimerApellido()+" "+ahafDocente.getPrsSegundoApellido()+" "+ahafDocente.getPrsNombres()+" en el día: "+HorarioAcademicoConstantes.traerDiaHorarioAcademico(itemHorario.getHracDia())+" de: "+itemHorario.getHoclDescripcion()+" , tiene cruce de horas en el paralelo: "+itemDocente.getPrlDescripcion()+" , en la materia: "+itemDocente.getMtrDescripcion());
									cruce = cruce + 1;
									break;
								}
							}
						}
					}
				}
				if(cruce == 0){
					//ejecuto la transacción de carga horaria
					if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras(), ahafMateriaVer.getMtrCreditos())){
						//busca el docente de la materia
						ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
					}
				}
				//******************************************************************************************************************//
				//******************************************************************************************************************//
				
				//******************************************************************************************************************//
				//*******************************SIN CONTROL DE CRUCE DOCENTE ******************************************************//
				//******************************************************************************************************************//
				//ejecuto la transacción de carga horaria
//				if(servAhafCargaHorariaServicio.nuevo(detallePuesto, mallaCurricularParalelo, periodoAcademico, ahafMateriaVer.getMtrHoras())){
//					//busca el docente de la materia
//					ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.guardado.exitoso")));
//				}else{
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.no.guardado")));
//				}
				//******************************************************************************************************************//
				//******************************************************************************************************************//
			}
		} catch (HorarioAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DocenteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());;
		} catch (DocenteDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.docente.docente.no.encontrado.exception")));
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		bloqueaModal();  //bloquea el modal para que no se ejecute nuevamente
	}
	
	/**
	 * Método que sirve para eliminar el docente de una carga horaria asiganada
	 */
	public void eliminarDocente(){
		try {
			//VERIFICO QUE SE HA SELECCIONADO EL DOCENTE
			if(ahafDocenteJdbcDtoElimi.getCrhrId() != 0){ // si se ha seleccionado el docente
				//PROCESO PARA ELIMINAR DOCENTE
				//busco las materias que tengan el id de malla curricular paralelo en el campo mlcrpridCompartido de la tabla horario academico
				List<HorarioAcademicoDto> listaHorarioEliminarDocente = new ArrayList<>();
				listaHorarioEliminarDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXpracIdXprlIdXmlcrprId(ahafMateriaVer.getPracId(), ahafDocenteJdbcDtoElimi.getMlcrprId());
//				if((listaHorarioEliminarDocente.size() <= 0 || listaHorarioEliminarDocente == null) && (ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp() != null || ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp().intValue() != 0)){
////				if((listaHorarioEliminarDocente.size() <= 0 & listaHorarioEliminarDocente == null) && (ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp() != null & ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp().intValue() != 0)){
//					listaHorarioEliminarDocente = ahafListHorarioAcademicoBusq;
//				}
				if(ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp() != 0 && ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp() != null){
					listaHorarioEliminarDocente = ahafListHorarioAcademicoBusq;
				}
				if(servAhafCargaHorariaServicio.eliminarDocente(ahafDocenteJdbcDtoElimi.getCrhrId(), listaHorarioEliminarDocente, ahafDocenteJdbcDtoElimi.getHracMlcrprIdComp())){ //devuelve verdadero si se ejecuto el elliminar
					//BUSCO O ACTUALIZO LA NUEVA LISTA DE DOCENTES
					ahafListDocenteJdbcDto = servAhafDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
					//VERIFICO QUE EXISTAN DOCENTES
					if(ahafListDocenteJdbcDto.size() <= 0){//no existen docentes
						ahafListDocenteJdbcDto = null;
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.docente.no.existe")));
					}
					ahafListHorarioAcademicoBusq = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
					ahafDocente = new DocenteJdbcDto();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.docente.exitoso")));
				}else{//no se realizo el proceso de eliminar docente
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.docente.no.puede.eliminar")));
				}
			}else{//no se selecciono el docente
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.docente.seleccione.docente")));
			}
		} catch (CargaHorariaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		bloqueaModal();  //bloquea el modal para que no se ejecute nuevamente
	}
	
	/**
	 * Método que sirve para eliminar la hora clase del horario academico
	 */
	public void eliminarHora(){
		try {
			//VERIFICA QUE SE HA SELECCIONADO LA HORA CLASE A ELIMINAR
			if(ahafHoraClaseElimi.getHracId() != null){ //si se selecciono la hora clase
				if(servAhafHorarioAcademicoServicio.eliminar(ahafHoraClaseElimi.getHracId())){
					//ACTUALIZA LA LISTA DE HORARIOS ACADEMICOS
					ahafListHorarioAcademicoBusq = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaVer.getMtrId());
					//VERIFICA QUE QUE EXISTAN HORARIOS ACADEMICOS
					if(ahafListHorarioAcademicoBusq.size() <= 0){ //no existen horas clase
						ahafListHorarioAcademicoBusq = null;
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.hora.no.existe")));
					}
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.hora.exitoso")));
				}else{//no se ejecuto la transaccion de eliminar
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.hora.no.puede.eliminar")));
				}
			}else{//no selecciono hora clase
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.eliminar.hora.seleccione.hora")));
			}
		} catch (HorarioAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		bloqueaModal();  //bloquea el modal para que no se ejecute nuevamente
	}
	
	/**
	 * Método que dirige a la pagina de editar o agregar hora clase de horario academico
	 * @return retorna la pagina de editar o agregar docente
	 */
	public String agregarHora(){
		//nuevo
		String retorno = null;
		//validacion para que no se agregue más del número de horas definida en la materia
//		if(ahafListHorarioAcademicoBusq.size() < ahafMateriaVer.getMtrHoras().intValue() ){
//			//SETEO LOS PARAMETROS PARA UNA NUEVA HORA CLASE
//			ahafHorarioAcademicoNew = new HorarioAcademicoDto();
//			//SETEO EL ID DE DE DEPENDENCIA
//			ahafHorarioAcademicoNew.setDpnId(GeneralesConstantes.APP_ID_BASE);
//			//SETEO EL ID DE EDIFICIO
//			ahafHorarioAcademicoNew.setEdfId(GeneralesConstantes.APP_ID_BASE);
//			//SETEO EL ID DEL AULA
//			ahafHorarioAcademicoNew.setAlaId(GeneralesConstantes.APP_ID_BASE);
//			//SETEO EL DIA 
//			ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
//			//SETEO EL ID DE HORA CLASE
//			ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
//			//ANULO LA LISTA DE DEPENDENCIA
//			ahafListaDependenciaNew = null;
//			//ANULO LA LISTA DE EDIFICIO
//			ahafListEdificioDtoNew = null;
//			//ANULO LA LISTA DE HORARIO NEW
//			ahafListHoraClaseDtoNew = null;
//			//ANULO LA LISTA DE AULA			
//			ahafListAulaDtoNew = null;
//			//ASIGNO LA LISTA DE DEPENDENCIA
//			try {
//				//busca dependencias de acuerdo al usuario
//				ahafListaDependenciaNew = servAhafDependenciaDtoServicioJdbc.listarXUsuario(ahafUsuario.getUsrId());
//			} catch (DependenciaDtoException e) {
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError(e.getMessage());
//			} catch (DependenciaDtoNoEncontradoException e) {
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.hora.no.encontrado.exception")));
//			}
//			//ASIGNO EL VALOR PARA AGREGAR UNA NUEVA HORA CLASE
//			bandera = 1; //agrega
//			retorno = "irAgregarHorario";
//		}else{
//			FacesUtil.mensajeError("No puede agregar más horas al horario académico, la asignatura tiene un número de horas "+ahafMateriaVer.getMtrHoras().intValue()+" a la semana.");
//		}
		
		if(ahafMateriaVer.getMtrHoras() != null && ahafMateriaVer.getMtrHoras() !=0 || ahafMateriaVer.getMtrCreditos() != null && ahafMateriaVer.getMtrCreditos() !=0){
			//SETEO LOS PARAMETROS PARA UNA NUEVA HORA CLASE
			ahafHorarioAcademicoNew = new HorarioAcademicoDto();
			//SETEO EL ID DE DE DEPENDENCIA
			ahafHorarioAcademicoNew.setDpnId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ID DE EDIFICIO
			ahafHorarioAcademicoNew.setEdfId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ID DEL AULA
			ahafHorarioAcademicoNew.setAlaId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL DIA 
			ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ID DE HORA CLASE
			ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE DEPENDENCIA
			ahafListaDependenciaNew = null;
			//ANULO LA LISTA DE EDIFICIO
			ahafListEdificioDtoNew = null;
			//ANULO LA LISTA DE HORARIO NEW
			ahafListHoraClaseDtoNew = null;
			//ANULO LA LISTA DE AULA			
			ahafListAulaDtoNew = null;
		}
		
		//reviso el tipo de materia 0 = creditos , 1 = horas
		ahafModoAsignatura = modalidadCreditosHoras(ahafMateriaVer.getMtrCreditos(), ahafMateriaVer.getMtrHoras());
		//para setear o inicializar
		if(ahafModoAsignatura == 0 || ahafModoAsignatura == 1){
			//SETEO LOS PARAMETROS PARA UNA NUEVA HORA CLASE
			ahafHorarioAcademicoNew = new HorarioAcademicoDto();
			//SETEO EL ID DE DE DEPENDENCIA
			ahafHorarioAcademicoNew.setDpnId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ID DE EDIFICIO
			ahafHorarioAcademicoNew.setEdfId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ID DEL AULA
			ahafHorarioAcademicoNew.setAlaId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL DIA 
			ahafHorarioAcademicoNew.setHracDia(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL ID DE HORA CLASE
			ahafHorarioAcademicoNew.setHoclId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE DEPENDENCIA
			ahafListaDependenciaNew = null;
			//ANULO LA LISTA DE EDIFICIO
			ahafListEdificioDtoNew = null;
			//ANULO LA LISTA DE HORARIO NEW
			ahafListHoraClaseDtoNew = null;
			//ANULO LA LISTA DE AULA			
			ahafListAulaDtoNew = null;
		}
		
		if(ahafModoAsignatura == 0){ //creditos
			if(ahafListHorarioAcademicoBusq.size() < ahafMateriaVer.getMtrCreditos().intValue() ){
				try {
					//busca dependencias de acuerdo al usuario
					ahafListaDependenciaNew = servAhafDependenciaDtoServicioJdbc.listarXUsuario(ahafUsuario.getUsrId());
					//ASIGNO EL VALOR PARA AGREGAR UNA NUEVA HORA CLASE
					bandera = 1; //agrega
					retorno = "irAgregarHorario";
				} catch (DependenciaDtoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				} catch (DependenciaDtoNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.hora.no.encontrado.exception")));
				}
			}else{
				FacesUtil.mensajeError("No puede agregar más créditos al horario académico, la asignatura tiene un número de créditos "+ahafMateriaVer.getMtrCreditos().intValue()+" a la semana.");
			}
		}else if(ahafModoAsignatura == 1){ //horas
			if(ahafListHorarioAcademicoBusq.size() < ahafMateriaVer.getMtrHoras().intValue() ){
				try {
					//busca dependencias de acuerdo al usuario
					ahafListaDependenciaNew = servAhafDependenciaDtoServicioJdbc.listarXUsuario(ahafUsuario.getUsrId());
					//ASIGNO EL VALOR PARA AGREGAR UNA NUEVA HORA CLASE
					bandera = 1; //agrega
					retorno = "irAgregarHorario";
				} catch (DependenciaDtoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				} catch (DependenciaDtoNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.agregar.hora.no.encontrado.exception")));
				}
			}else{
				FacesUtil.mensajeError("No puede agregar más horas al horario académico, la asignatura tiene un número de horas "+ahafMateriaVer.getMtrHoras().intValue()+" a la semana.");
			}
		}else {
			FacesUtil.mensajeError("Hubo un problema en cargar las asignaturas, por favor comunicarse con el administrador del sistema. ");
		}
		
		return retorno;
	}
	
	/**
	 * Método que guarda nuevo horario academico
	 * @return retorna hacia la navegación de la pagina de listar
	 */
	public String guardarNuevo(){
		String retorno = null;
		try {
			//INSTANCIO LA MALLA CURRICULAR MATERIA
			HorarioAcademico entidad = new HorarioAcademico();
			//SETEO DE VALORES
			entidad.setHracDia(ahafHorarioAcademicoNew.getHracDia()); //seteo el día
			entidad.setHracEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE); //seteo el estado activo
			entidad.setHracHoraClaseAula(servAhafHoraClaseAulaServicio.buscarPalaIdPhoclId(ahafHorarioAcademicoNew.getAlaId(), ahafHorarioAcademicoNew.getHoclId())); //seteo la entidad hora clase aula
//			entidad.setHracMallaCurricularParalelo(servAhafMallaCurricularParaleloServicio.buscarPorId(ahafHorarioAcademicoNew.getMlcrprId())); //seteo la entidad malla curricular paralelo
			entidad.setHracMallaCurricularParalelo(servAhafMallaCurricularParaleloServicio.buscarPorId(ahafMateriaVer.getMlcrprId())); //seteo la entidad malla curricular paralelo
			//VALIDACIÓN ANTES DE GUARDAR
			if(entidad.getHracDia() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.nuevo.horario.academico.dia.no.almacenado")));
			}else if(entidad.getHracEstado() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.nuevo.horario.academico.estado.no.almacenado")));
			}else if(entidad.getHracHoraClaseAula() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.nuevo.horario.academico.hora.clase.aula.no.almacenado")));
			}else if(entidad.getHracMallaCurricularParalelo() == null){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.nuevo.horario.academico.malla.curricular.paralelo.no.almacenado")));
				//PROCESO QUE AGREGA UN NUEVO HORARIO ACADEMICO
			}else if(servAhafHorarioAcademicoServicio.nuevo(entidad)){ // si se guarda la edición devuelve verdadero
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.nuevo.horario.academico.exitoso")));
				ahafDeshabilitaDia = false;
				retorno = irVerHorario(ahafMateriaVer);
			}else{ // caso que no se ejecute el agregar
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.guardar.nuevo.horario.academico.no.guardado")));
			}
			bloqueaModal(); //bloquea el modal para que no se ejecute nuevamente
		}  catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		//SETEO LA BANDERA A CERO PARA QUE NO SE EJECUTE
		bandera = 0;
		return retorno;
	}
	
	/**
	 * Navega hacia la página de compartir horario
	 * @param materiaDto - materiaDto entidad dto de materia
	 * @return  Navegacion a la pagina de compartir horario academico.
	 */
	public String irVerCompartirHorario(MateriaDto materiaDto){
		String retorno = null;
		try {
			ahafMateriaCompartir = materiaDto; //materia padre a ser compartida
			//ANULO LA LISTA DE HORARIOS PARA BUSCAR SI TIENE ASIGNADOS
			ahafListHorarioAcademicoCompBusq = null;
			//ANULO LA LISTA DE CARRERAS COMPARTIR
			ahafListCarreraDtoCompBusq = null;
			//ANULO LA LISTA DE NIVEL COMPARTIR
			ahafListNivelCompBusq = null;
			//ANULO LA LISTA DE PARALELO COMPARTIR
			ahafListParaleloDtoCompBusq = null;
			//ANULO LA LISTA DE ASIGNATURA COMPARTIR
			ahafListMateriaDtoCompBusq = null;
			//INSTANCIO EL HORARIO
			ahafHorarioAcademicoCompBusq = new HorarioAcademicoDto();
			//SETEO PERIODO ACADEMICO COMPARTIR
			ahafHorarioAcademicoCompBusq.setPracId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA CARRERA COMPARTIR
			ahafHorarioAcademicoCompBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL NIVEL COMPARTIR
			ahafHorarioAcademicoCompBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO PARALELO COMPARTIR
			ahafHorarioAcademicoCompBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA COMPARTIR
			ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//BUSCO LOS HORARIOS ACADEMICOS DE LA MATERIA PADRE A SER COMPARTIDA
			ahafListHorarioAcademicoCompartir = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(ahafMateriaCompartir.getPracId(), ahafMateriaCompartir.getPrlId(), ahafMateriaCompartir.getMtrId());
			//BUSCO EL DOCENTE A SER COMPARTIDOS
			ahafDocenteJdbcDtoComp = servAhafDocenteDtoServicioJdbc.docenteXpracIdXprlIdXmtrId(ahafHorarioAcademicoBusq.getPracId(), ahafHorarioAcademicoBusq.getPrlId(), ahafMateriaCompartir.getMtrId());
			if(ahafListHorarioAcademicoCompartir.size() <= 0){
				FacesUtil.mensajeError("No puede compartir el horario, no tiene asignado horas academicas");
			}else if(ahafDocenteJdbcDtoComp == null){
				FacesUtil.mensajeError("No puede compartir el horario, no tiene asignado docente");
			}else{
				retorno = "irVerCompartirHorario";
			}
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DocenteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Verificación si existe horarios academicos asignados a la materia
	 * @param materiaConsulta - materiaConsulta entidad dto de materia
	 * @return  True o False segun el caso.
	 */
	public boolean verificaHorario(MateriaDto materiaConsulta){
		boolean existeHorario = true;
		//BUSCO LOS HORARIOS ACADEMICOS DE LA MATERIA PADRE A SER COMPARTIDA
		try {
			//BUSCO LA LISTA DE HORARIOS A COMPARTIR
			ahafListHorarioAcademicoCompartir = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(materiaConsulta.getPracId(), materiaConsulta.getPrlId(), materiaConsulta.getMtrId());
			//BUSCO EL DOCENTE A SER COMPARTIDOS
			ahafDocenteJdbcDtoComp = servAhafDocenteDtoServicioJdbc.docenteXpracIdXprlIdXmtrId(materiaConsulta.getPracId(), materiaConsulta.getPrlId(), materiaConsulta.getMtrId());
			if(ahafListHorarioAcademicoCompartir.size() > 0 && ahafDocenteJdbcDtoComp != null){
				existeHorario = false;
			}
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DocenteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return existeHorario;
	}
	
	
	/**
	 *Navega hacia la pagina del listar las materias del paralelo
	 *@return  Navegacion a la pagina del listar las materias del paralelo.
	 */
	public String irListarHorarioAcademico(){
		String retorno = null;
		retorno = "irHorarioAcademico";
		return retorno;
	}
	
	/**
	 * Método que guarda el los horarios academicos a otra materia de otro paralelo, de igual manera el docente
	 */
	public String guardarCompartir(){
		String retorno = null;
		try {
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ahafHorarioAcademicoCompBusq.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.academico.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.carrera.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.nivel.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			} else if(servAhafHorarioAcademicoServicio.compartir(ahafHorarioAcademicoCompBusq, ahafListHorarioAcademicoCompartir, ahafDocenteJdbcDtoComp)){
				FacesUtil.mensajeInfo("Se ha compartido el horario exitosamente");
				ahafValidadorClic = 0;
				retorno = "irHorarioAcademico";
			}else{
				ahafValidadorClic = 0;
			}
		} catch (HorarioAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		//INSTANCIO HORARIO ACADEMICO BUSQ
		ahafHorarioAcademicoBusq = new HorarioAcademicoDto();
		//INSTANCIO MATERIA
		ahafMateriaVer = new MateriaDto();
		//INSTANCIO HORARIO ACADEMICO EDIT
		ahafHorarioAcademicoEdit = new HorarioAcademicoDto();
		//INSTANCIO HORARIO ACADEMICO NEW
		ahafHorarioAcademicoNew = new HorarioAcademicoDto();
		//INSTANCIO DOCENTE PARA BUSQ
		ahafDocenteJdbcDtoBusq = new DocenteJdbcDto();
		//INSTANCIO DOCENTE PARA ELIMINAR
		ahafDocenteJdbcDtoElimi = new DocenteJdbcDto();
		//INSTANCIO HORARIO ACADEMICO COMPARTIR BUSQS
		ahafHorarioAcademicoCompBusq = new HorarioAcademicoDto();
		//INSTANCIO DOCENTES A COMPARTIR
		ahafDocenteJdbcDtoComp = new DocenteJdbcDto();
		//INSTANCIO EL DOCENTE
		ahafDocente = new DocenteJdbcDto();
		//ANULO LA LISTA DE CARRERAS 
		ahafListCarreraDtoBusq = null;
		//ANULO LA LISTA DE NIVELES
		ahafListNivelBusq = null;
		//ANULO LA LISTA DE PARALELOS
		ahafListParaleloDtoBusq = null;
		//ANULO LA LISTA DE MATERIAS
		ahafListMateriaDtoBusq = null;
		//ANULO LA LISTA DE AULAS EDIT
		ahafListAulaDtoEdit = null;
		//ANULO LA LISTA DE AULAS NEW
		ahafListAulaDtoNew = null;
		//ANULO LA LISTA DE HORA_CLASE EDIT
		ahafListHoraClaseDtoEdit = null;
		//ANULO LA LISTA DE HORA_CLASE NEW
		ahafListHoraClaseDtoNew = null;
		//ANULO LA LISTA DE HORARIO ACADEMICO BUSQ
		ahafListHorarioAcademicoBusq = null;
		//ANULO LA LISTA DE DOCENTE
		ahafListDocenteJdbcDto = null;
		//ANULO LA LISTA DE CARRERAS BUSQ DOC
//		ahafListCarreraBusq = null;
		//ANULO LA LISTA DE DEPLENDENCIAS PARA NUEVO
//		ahafListaDependenciaNew = null;
		//ANULO LA LISTA DE EDIFICIOS PARA NUEVO
		ahafListEdificioDtoNew = null;
		//ANULO LA LISTA DE HORARIO ACADEMICO COMPARTIR
		ahafListHorarioAcademicoCompartir = null;
		//ANULO LA LISTA DE HORARIOS PARA BUSCAR SI TIENE ASIGNADOS
		ahafListHorarioAcademicoCompBusq = null;
		//ANULO LA LISTA DE CARRERAS COMPARTIR
		ahafListCarreraDtoCompBusq = null;
		//ANULO LA LISTA DE NIVEL COMPARTIR
		ahafListNivelCompBusq = null;
		//ANULO LA LISTA DE PARALELO COMPARTIR
		ahafListParaleloDtoCompBusq = null;
		//ANULO LA LISTA DE ASIGNATURA COMPARTIR
		ahafListMateriaDtoCompBusq = null;
		//ASIGNO EL VALOR -99 A LA VALIDACIÓN
		ahafModoAsignatura = GeneralesConstantes.APP_ID_BASE;
		//SETEO HABILITA DIA - EDITA - NUEVO
		ahafDeshabilitaDia = false;
		//ASIGNO EL VALOR -99 PARA HABILITAR EDICIÓN DE HORAS -  0: CAMBIO DE AULA
		ahafHabilitaAulaEdit = GeneralesConstantes.APP_ID_BASE;
		//SETEO EL VAOR DE -99 PARA LA HORA - CAMBIO AULA
		ahafHoraCambioAulaEdit = GeneralesConstantes.APP_ID_BASE;
		//SETEO EL VALOR DE -99 PARA EL DÍA - CAMBIO AULA
		ahafDiaCambioAula = GeneralesConstantes.APP_ID_BASE;
		//SETEO PERIODO ACADEMICO
		ahafHorarioAcademicoBusq.setPracId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA
		ahafHorarioAcademicoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL NIVEL
		ahafHorarioAcademicoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//SETEO PARALELO
		ahafHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//SETEO PERIODO ACADEMICO COMPARTIR
		ahafHorarioAcademicoCompBusq.setPracId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA COMPARTIR
		ahafHorarioAcademicoCompBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL NIVEL COMPARTIR
		ahafHorarioAcademicoCompBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//SETEO PARALELO COMPARTIR
		ahafHorarioAcademicoCompBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA MATERIA COMPARTIR
		ahafHorarioAcademicoCompBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
		//ASIGNO VALOR INICIAL PARA EJECUCION DE MODALES
		bloqueaModal();
	}
	
	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Verifica que haga click en el boton editar horario academico 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ahafValidadorClic
	 * estado para poder editar horario academicoa
	 */
	public String verificarClickGuardarEdicion(){
		if(ahafHabilitaAulaEdit == 0){ //habilito para el guardar cambio de aula
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ahafHorarioAcademicoEdit.getDpnId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.dependencia")));
			} else if(ahafHorarioAcademicoEdit.getEdfId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.edificio")));
			} else if(ahafHorarioAcademicoEdit.getAlaId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.aula")));
			} else {
				//habilito el modal para guardar cambio de aula
				habilitaModalGuardarEditar();
			}
		}else{ //habilito para guardar hora clase
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ahafHorarioAcademicoEdit.getDpnId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.dependencia")));
			} else if(ahafHorarioAcademicoEdit.getEdfId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.edificio")));
			} else if(ahafHorarioAcademicoEdit.getAlaId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.aula")));
			} else if(ahafHorarioAcademicoEdit.getHracDia() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.dia")));
			} else if(ahafHorarioAcademicoEdit.getHoclId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.edicion.seleccione.hora.inicio.fin")));
			} else{
				//**********************************************************************************//
				//**************************** CONTROL MODULAR *************************************//
				//**********************************************************************************//
				//			if(ahafMateriaVer.getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
				//				//*****************************************************************************//
				//				//***************** SIN VALIDACIÓN DE HORARIOS ASIGNADOS AL DOCENTE ***********//
				//				//*****************************************************************************//
				//				
				//				//HABILITO EL MODAL DE EDITAR
				//				habilitaModalGuardarEditar();
				//				
				//				//*****************************************************************************//
				//				//*****************************************************************************//
				//			}else{
				//				
				//			}
				//**********************************************************************************//

				//*****************************************************************************//
				//***************** VALIDACION CON LAS HORAS DEL DOCENTE SI ESTA CARGADO *****//
				//*****************************************************************************//
				try {
					int cruce = 0;
					int dtpsId = 0;
					String apellidoPaterno = null;
					String apellidoMaterno = null;
					String nombres = null;
					String apellidos = null;
					if(ahafListDocenteJdbcDto.size() >= 1){ 
						//busco id docente en lista agregada
						for (DocenteJdbcDto item : ahafListDocenteJdbcDto) {
							if(item.getCrhrDtpsId() != null){
								dtpsId = item.getCrhrDtpsId();
								apellidoPaterno = item.getPrsPrimerApellido();
								apellidoMaterno = item.getPrsSegundoApellido();
								nombres = item.getPrsNombres();
								if(apellidoMaterno == null){
									apellidos = apellidoPaterno;
								}else{
									apellidos = apellidoPaterno+" "+apellidoMaterno;
								}
								break;
							}
						}
					}
					ahafListHorarioAcademicoDocente = new ArrayList<>();
					//AGREGAR EL DOCENTE A CARGA HORARIA
					//busco detalle puesto
					DetallePuesto detallePuesto = new DetallePuesto();
					detallePuesto = servAhafDetallePuestoServicio.buscarPorId(ahafDocente.getDtpsId());
					//busco el horario academico del docente
					if(detallePuesto != null && detallePuesto.getDtpsId() != 0){
						apellidoPaterno = ahafDocente.getPrsPrimerApellido();
						apellidoMaterno = ahafDocente.getPrsSegundoApellido();
						nombres = ahafDocente.getPrsNombres();
						if(apellidoMaterno == null){
							apellidos = ahafDocente.getPrsPrimerApellido();
						}else{
							apellidos = ahafDocente.getPrsPrimerApellido()+" "+ahafDocente.getPrsSegundoApellido();
						}
						ahafListHorarioAcademicoDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXPracIdXDtpsId(ahafMateriaVer.getPracId(), detallePuesto.getDtpsId());
					} else if(ahafListDocenteJdbcDto.size() >= 1){
						ahafListHorarioAcademicoDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXPracIdXDtpsId(ahafMateriaVer.getPracId(), dtpsId);
					}
					if(ahafListHorarioAcademicoDocente.size() > 0){
						for (HorarioAcademicoDto itemDocente : ahafListHorarioAcademicoDocente) {
							if(itemDocente.getHracDia() == ahafHorarioAcademicoEdit.getHracDia() &&  itemDocente.getHoclId() == ahafHorarioAcademicoEdit.getHoclId()){
								FacesUtil.mensajeError("No puede asignar el horario, el docente: "+apellidos+" "+nombres+" , se ecuentra asignado en el paralelo: "+itemDocente.getPrlDescripcion()+" , en la materia: "+itemDocente.getMtrDescripcion()+" , el día: "+HorarioAcademicoConstantes.traerDiaHorarioAcademico(itemDocente.getHracDia())+" de: "+HoraClaseConstantes.traerHoraClase(ahafHorarioAcademicoEdit.getHoclId()));
								cruce = cruce + 1;
								break;
							}
						}
					}

					if(cruce == 0){
						//HABILITO EL MODAL DE EDITAR
						habilitaModalGuardarEditar();
					}
				} catch (DetallePuestoNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				} catch (DetallePuestoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				} catch (HorarioAcademicoDtoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				} catch (HorarioAcademicoDtoNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e.getMessage());
				}
				//*****************************************************************************//
				//*****************************************************************************//

				//*****************************************************************************//
				//***************** SIN VALIDACIÓN DE HORARIOS ASIGNADOS AL DOCENTE ***********//
				//*****************************************************************************//

				//			//HABILITO EL MODAL DE EDITAR
				//			habilitaModalGuardarEditar();

				//*****************************************************************************//
				//*****************************************************************************//
			}
		}
		return null;
	}
	
	//HABILITA EL MODAL BUSCAR DOCENTE
	/**
	 * Verifica que haga click en el boton agregar editar docente 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ahafValidadorClic
	 * estado para poder agregar editar docente
	 */
	public String verificarClickBuscarDocente(){
		//LIMPRIO PARAMETROS
		limpiarModal();		
		//HABILITO EL MODAL DE BUSCAR DOCENTE
		habilitaModalBuscarDocente();
		return null;
	}
	
	//HABILITA EL MODAL ELIMINAR DOCENTE
	/**
	 * Verifica que haga click en el boton eliminar docente 
	 * @param docente - docente entidad docente dto a verificar
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ahafValidadorClic
	 */
	public String verificarClickEliminarDocente(DocenteJdbcDto docente){
		ahafDocenteJdbcDtoElimi = docente;
//		try {
			//VERIFICO QUE NO TENGA MATRICULADOS PARA EDITAR
//			List<MallaCurricularParaleloDto> mallaCurricularParaleloMatriculados = new ArrayList<>();
//			mallaCurricularParaleloMatriculados = servAhafMallaCurricularParaleloDtoServicioJdbc.buscarMatriculadosMlcrprXMateriaXParalelo(ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId());
//			if(mallaCurricularParaleloMatriculados.size() > 0){
//				FacesUtil.mensajeError("No puede elimiar el docente, existen estudiantes matriculados");
//				bloqueaModal();
//			}else{
				//ASIGNO PARAMETROS PARA LAS RESPECTIVAS CONSULTAS DE HORARIOS mlcrprid (hijas) y hracMlcrprIdComp (padre) según el caso
				if(ahafListHorarioAcademicoBusq.size() > 0){
					for (HorarioAcademicoDto itemDocente : ahafListHorarioAcademicoBusq) {
						if(itemDocente.getMlcrprId() != null){
							ahafDocenteJdbcDtoElimi.setMlcrprId(itemDocente.getMlcrprId());
							ahafDocenteJdbcDtoElimi.setHracMlcrprIdComp(itemDocente.getHracMlcrprIdComp());
							break;
						}
					}
				}else{
					ahafDocenteJdbcDtoElimi.setMlcrprId(0);
					ahafDocenteJdbcDtoElimi.setHracMlcrprIdComp(0);
				}
				//VERIFICO QUE SE HA SELECCIONADO EL DOCENTE
				if(ahafDocenteJdbcDtoElimi.getCrhrId() == 0){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.elimina.docente.seleccione.docente")));
				} else{
					//HABILITO EL MODAL DE ELIMINAR DOCENTE
					habilitaModalEliminarDocente();
				}
//			}
//		} catch (MallaCurricularParaleloDtoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
		return null;
	}
	
	//HABILITA EL MODAL ELIMINAR HORA CLASE DEL HORARIO
	/**
	 * Verifica que haga click en el boton eliminar hora clase de horario 
	 * @param horaClase - horaClase entidad horario academico que contiene la hora clase ha eliminar
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ahafValidadorClic
	 */
	public String verificarClickEliminarHora(HorarioAcademicoDto horaClase){
		ahafHoraClaseElimi = horaClase;
		//***************************************************************************//
		//************************* CONTROL DE MATRICULADOS *************************//
		//***************************************************************************//
		try {
			//VERIFICO QUE NO ESTEN MATRICULADOS
			List<MallaCurricularParaleloDto> mallaCurricularParaleloMatriculados = new ArrayList<>();
			mallaCurricularParaleloMatriculados = servAhafMallaCurricularParaleloDtoServicioJdbc.buscarMatriculadosMlcrprXMateriaXParalelo(ahafMateriaVer.getPrlId(), ahafMateriaVer.getMtrId());
			if(mallaCurricularParaleloMatriculados.size() > 0){
				FacesUtil.mensajeError("No puede eliminar horarios, existen estudiantes matriculados");
				bloqueaModal();
			}else{
				//VERIFICO QUE SE HA SELECCIONADO LA HORA 
				if(ahafHoraClaseElimi.getHracId() == null){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.elimina.hora.seleccione.hora.clase")));
				} else{
					//HABILITO EL MODAL DE ELIMINAR HORA CLASE DEL HORARIO
					habilitaModalEliminarHora();
				}
			}
			mallaCurricularParaleloMatriculados = null;
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		//***************************************************************************//
		//*********************** SIN CONTROL DE MATRICULADOS ***********************//
		//***************************************************************************//
//		habilitaModalEliminarHora();
		return null;
	}
	
	//HABILITA EL MODAL GUARDAR UN NUEVO HORA CLASE EN EL HORARIO ACADEMICO
	/**
	 * Verifica que haga click en el boton nuevo horario academico 
	 * @return retorna null para para cualquier cosa pero setea a 0 la variable ahafValidadorClic
	 */
	public String verificarClickGuardarNuevo(){
		//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
		if(ahafHorarioAcademicoNew.getDpnId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.nuevo.horario.seleccione.dependencia")));
		} else if(ahafHorarioAcademicoNew.getEdfId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.nuevo.horario.seleccione.edificio")));
		} else if(ahafHorarioAcademicoNew.getAlaId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.nuevo.horario.seleccione.aula")));
		} else if(ahafHorarioAcademicoNew.getHracDia() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.nuevo.horario.seleccione.dia")));
		} else if(ahafHorarioAcademicoNew.getHoclId() == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.verificar.nuevo.horario.seleccione.hora.inicio.fin")));
		} else{
			//**********************************************************************************//
			//**************************** CONTROL MODULAR *************************************//
			//**********************************************************************************//
//			if(ahafMateriaVer.getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE){
//				//*****************************************************************************//
//				//***************** SIN VALIDACIÓN DE HORARIOS ASIGNADOS AL DOCENTE ***********//
//				//*****************************************************************************//
//				
//				//HABILITO EL MODAL DE GUARDAR NUEVO
//				habilitaModalGuardarNuevo();
//				
//				//*****************************************************************************//
//				//*****************************************************************************//
//			}else{
//				
//			}
			//**********************************************************************************//
			
			//*****************************************************************************//
			//***************** VALIDACION CON LAS HORAS DEL DOCENTE SI ESTA CARGADO *****//
			//*****************************************************************************//
			try {
				int cruce = 0;
				int dtpsId = 0;
				String apellidoPaterno = null;
				String apellidoMaterno = null;
				String nombres = null;
				String apellidos = null;
				if(ahafListDocenteJdbcDto.size() >= 1){ 
					//busco id docente en lista agregada
					for (DocenteJdbcDto item : ahafListDocenteJdbcDto) {
						if(item.getCrhrDtpsId() != null){
							dtpsId = item.getCrhrDtpsId();
							apellidoPaterno = item.getPrsPrimerApellido();
							apellidoMaterno = item.getPrsSegundoApellido();
							nombres = item.getPrsNombres();
							if(apellidoMaterno == null){
								apellidos = apellidoPaterno;
							}else{
								apellidos = apellidoPaterno+" "+apellidoMaterno;
							}
							break;
						}
					}
				}
				ahafListHorarioAcademicoDocente = new ArrayList<>();
				//AGREGAR EL DOCENTE A CARGA HORARIA
				//busco detalle puesto
				DetallePuesto detallePuesto = new DetallePuesto();
				detallePuesto = servAhafDetallePuestoServicio.buscarPorId(ahafDocente.getDtpsId());
				//busco el horario academico del docente
				if(detallePuesto != null && detallePuesto.getDtpsId() != 0){
					apellidoPaterno = ahafDocente.getPrsPrimerApellido();
					apellidoMaterno = ahafDocente.getPrsSegundoApellido();
					nombres = ahafDocente.getPrsNombres();
					if(apellidoMaterno == null){
						apellidos = ahafDocente.getPrsPrimerApellido();
					}else{
						apellidos = ahafDocente.getPrsPrimerApellido()+" "+ahafDocente.getPrsSegundoApellido();
					}
					ahafListHorarioAcademicoDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXPracIdXDtpsId(ahafMateriaVer.getPracId(), detallePuesto.getDtpsId());
				} else if(ahafListDocenteJdbcDto.size() >= 1){
					ahafListHorarioAcademicoDocente = servAhafHorarioAcademicoDtoServicioJdbc.listarHorarioXPracIdXDtpsId(ahafMateriaVer.getPracId(), dtpsId);
				}
				if(ahafListHorarioAcademicoDocente.size() > 0){
					for (HorarioAcademicoDto itemDocente : ahafListHorarioAcademicoDocente) {
						if(itemDocente.getHracDia() == ahafHorarioAcademicoNew.getHracDia() &&  itemDocente.getHoclId() == ahafHorarioAcademicoNew.getHoclId()){
							FacesUtil.mensajeError("No puede asignar el horario, el docente: "+apellidos+" "+nombres+" , se ecuentra asignado en el paralelo: "+itemDocente.getPrlDescripcion()+" , en la materia: "+itemDocente.getMtrDescripcion()+" , el día: "+HorarioAcademicoConstantes.traerDiaHorarioAcademico(itemDocente.getHracDia())+" de: "+HoraClaseConstantes.traerHoraClase(ahafHorarioAcademicoNew.getHoclId()));
							cruce = cruce + 1;
							break;
						}
					}
				}
				if(cruce == 0){
					//HABILITO EL MODAL DE GUARDAR NUEVO
					habilitaModalGuardarNuevo();
				}
			} catch (DetallePuestoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (DetallePuestoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (HorarioAcademicoDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (HorarioAcademicoDtoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
			//*****************************************************************************//
			//*****************************************************************************//
			
			
			//*****************************************************************************//
			//***************** SIN VALIDACIÓN DE HORARIOS ASIGNADOS AL DOCENTE ***********//
			//*****************************************************************************//
			
//			//HABILITO EL MODAL DE GUARDAR NUEVO
//			habilitaModalGuardarNuevo();
			
			//*****************************************************************************//
			//*****************************************************************************//
		}
		return null;
	}
	
	//HABILITA EL MODAL PARA QUE EL HORARIO DE LA MATERIA SEA COMPARTIDO
	/**
	 * Método que valida materias del periodo , carrera , nivel , paralelo y  materia
	 */
	public String verificarClickGuardarCompartir(){
		try {
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(ahafHorarioAcademicoCompBusq.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.academico.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.carrera.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.nivel.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else if(ahafHorarioAcademicoCompBusq.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else {
				//PROCESO DE COMPARTIR
				if(bandera == 3){
					ahafHorarioAcademicoCompBusq.setMlcrprId(servAhafMallaCurricularParaleloDtoServicioJdbc.buscarCupoMlcrprXMateriaXParalelo(ahafHorarioAcademicoCompBusq.getPrlId(), ahafHorarioAcademicoCompBusq.getMtrId()).getMlcrprId());
					ahafHorarioAcademicoCompBusq.setMtrHoras(servAhafMallaCurricularParaleloDtoServicioJdbc.buscarCupoMlcrprXMateriaXParalelo(ahafHorarioAcademicoCompBusq.getPrlId(), ahafHorarioAcademicoCompBusq.getMtrId()).getMtrHoras());
					ahafHorarioAcademicoCompBusq.setMtrCreditos(servAhafMallaCurricularParaleloDtoServicioJdbc.buscarCupoMlcrprXMateriaXParalelo(ahafHorarioAcademicoCompBusq.getPrlId(), ahafHorarioAcademicoCompBusq.getMtrId()).getMtrCreditos());
					//habiliatar validador para compartir
					habilitaModalGuardarCompartir(); 
				}
			}
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
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
		ahafValidadorClic = 0;
	}
	
	//HABILITA EL MODAL GUARDAR EDITAR
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar edición
	 */
	public void habilitaModalGuardarEditar(){
		ahafValidadorClic = 1;
	}
	
	//HABILITA EL MODAL BUSCAR DOCENTE
	/**
	 * Método que habilita el modal para que se despliegue en pantalla buscar docentes
	 */
	public void habilitaModalBuscarDocente(){
		ahafValidadorClic = 2;
	}
	
	//HABILITA EL MODAL ELIMINAR DOCENTE
	/**
	 * Método que habilita el modal para confirmación de eliminar docente de carga horaria
	 */
	public void habilitaModalEliminarDocente(){
		ahafValidadorClic = 3;
	}
	
	//HABILITA EL MODAL ELIMINAR LA HORA CLASE DEL HORARIO
	/**
	 * Método que habilita el modal para confirmación de eliminar docente de carga horaria
	 */
	public void habilitaModalEliminarHora(){
		ahafValidadorClic = 4;
	}
	
	//HABILITA EL MODAL GUARDAR NUEVO HORA CLASE DEL HORARIO
	/**
	 * Método que habilita el modal para que se despliegue en pantalla el nuevo docente
	 */
	public void habilitaModalGuardarNuevo(){
		ahafValidadorClic = 5;
	}
	
	//HABILITA EL MODAL GUARDAR NUEVO HORA CLASE DEL HORARIO
	/**
	 * Método que habilita el modal para que el horario pueda ser compartido
	 */
	public void habilitaModalGuardarCompartir(){
		ahafValidadorClic = 6;
	}
	
	//HABILITA SEGÚN SEA EL CASO EN CABECERA DEL XHTML DE HORAS O CREDITOS
	/**
	 * Cabecera de la columna que varia entre creditos y horas 
	 */
	public String cabeceraCreditosHoras(Integer creditos, Integer horas){
		if(creditos != null && creditos != 0){
			return "Créditos";
		}
		if(horas != null && horas != 0){
			return "Horas";
		}
		return "Horas - Créditos N/D";
	}
	
	//HABILITA EL TIPO DE ASIGNATURA - CREDITOS U HORAS
	/**
	 * Modalidad de la asignatura entre creditos y horas 
	 */
	public Integer modalidadCreditosHoras(Integer creditos, Integer horas){
		if(creditos != null && creditos != 0){
			return 0;
		}
		if(horas != null && horas != 0){
			return 1;
		}
		return GeneralesConstantes.APP_ID_BASE;
	}
	
	// ********************************************************************/
	// *********************** METODOS GET Y SET **************************/
	// ********************************************************************/
	
	public Usuario getAhafUsuario() {
		return ahafUsuario;
	}
	public void setAhafUsuario(Usuario ahafUsuario) {
		this.ahafUsuario = ahafUsuario;
	}
	public Integer getAhafTipoUsuario() {
		return ahafTipoUsuario;
	}
	public void setAhafTipoUsuario(Integer ahafTipoUsuario) {
		this.ahafTipoUsuario = ahafTipoUsuario;
	}
	public String getAhafTipoCarrera() {
		return ahafTipoCarrera;
	}
	public void setAhafTipoCarrera(String ahafTipoCarrera) {
		this.ahafTipoCarrera = ahafTipoCarrera;
	}
	public List<UsuarioRolJdbcDto> getAhafListRoles() {
		return ahafListRoles;
	}
	public void setAhafListRoles(List<UsuarioRolJdbcDto> ahafListRoles) {
		this.ahafListRoles = ahafListRoles;
	}
	public HorarioAcademicoDto getAhafHorarioAcademicoBusq() {
		return ahafHorarioAcademicoBusq;
	}
	public void setAhafHorarioAcademicoBusq(HorarioAcademicoDto ahafHorarioAcademicoBusq) {
		this.ahafHorarioAcademicoBusq = ahafHorarioAcademicoBusq;
	}
	public List<PeriodoAcademicoDto> getAhafListPeriodoAcademicoDtoBusq() {
		ahafListPeriodoAcademicoDtoBusq = ahafListPeriodoAcademicoDtoBusq==null?(new ArrayList<PeriodoAcademicoDto>()):ahafListPeriodoAcademicoDtoBusq;
		return ahafListPeriodoAcademicoDtoBusq;
	}
	public void setAhafListPeriodoAcademicoDtoBusq(List<PeriodoAcademicoDto> ahafListPeriodoAcademicoDtoBusq) {
		this.ahafListPeriodoAcademicoDtoBusq = ahafListPeriodoAcademicoDtoBusq;
	}
	public List<CarreraDto> getAhafListCarreraDtoBusq() {
		ahafListCarreraDtoBusq = ahafListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):ahafListCarreraDtoBusq;
		return ahafListCarreraDtoBusq;
	}
	public void setAhafListCarreraDtoBusq(List<CarreraDto> ahafListCarreraDtoBusq) {
		this.ahafListCarreraDtoBusq = ahafListCarreraDtoBusq;
	}
	public List<Nivel> getAhafListNivelBusq() {
		ahafListNivelBusq = ahafListNivelBusq==null?(new ArrayList<Nivel>()):ahafListNivelBusq;
		return ahafListNivelBusq;
	}
	public void setAhafListNivelBusq(List<Nivel> ahafListNivelBusq) {
		this.ahafListNivelBusq = ahafListNivelBusq;
	}
	public List<ParaleloDto> getAhafListParaleloDtoBusq() {
		ahafListParaleloDtoBusq = ahafListParaleloDtoBusq==null?(new ArrayList<ParaleloDto>()):ahafListParaleloDtoBusq;
		return ahafListParaleloDtoBusq;
	}
	public void setAhafListParaleloDtoBusq(List<ParaleloDto> ahafListParaleloDtoBusq) {
		this.ahafListParaleloDtoBusq = ahafListParaleloDtoBusq;
	}
	public List<MateriaDto> getAhafListMateriaDtoBusq() {
		ahafListMateriaDtoBusq = ahafListMateriaDtoBusq==null?(new ArrayList<MateriaDto>()):ahafListMateriaDtoBusq;
		return ahafListMateriaDtoBusq;
	}
	public void setAhafListMateriaDtoBusq(List<MateriaDto> ahafListMateriaDtoBusq) {
		this.ahafListMateriaDtoBusq = ahafListMateriaDtoBusq;
	}
	public List<DocenteJdbcDto> getAhafListDocenteJdbcDto() {
		ahafListDocenteJdbcDto = ahafListDocenteJdbcDto==null?(new ArrayList<DocenteJdbcDto>()):ahafListDocenteJdbcDto;
		return ahafListDocenteJdbcDto;
	}
	public void setAhafListDocenteJdbcDto(List<DocenteJdbcDto> ahafListDocenteJdbcDto) {
		this.ahafListDocenteJdbcDto = ahafListDocenteJdbcDto;
	}
	public List<HorarioAcademicoDto> getAhafListHorarioAcademicoBusq() {
		ahafListHorarioAcademicoBusq = ahafListHorarioAcademicoBusq==null?(new ArrayList<HorarioAcademicoDto>()):ahafListHorarioAcademicoBusq;
		return ahafListHorarioAcademicoBusq;
	}
	public void setAhafListHorarioAcademicoBusq(List<HorarioAcademicoDto> ahafListHorarioAcademicoBusq) {
		this.ahafListHorarioAcademicoBusq = ahafListHorarioAcademicoBusq;
	}
	public MateriaDto getAhafMateriaVer() {
		return ahafMateriaVer;
	}
	public void setAhafMateriaVer(MateriaDto ahafMateriaVer) {
		this.ahafMateriaVer = ahafMateriaVer;
	}
	public HorarioAcademicoDto getAhafHorarioAcademicoEdit() {
		return ahafHorarioAcademicoEdit;
	}
	public void setAhafHorarioAcademicoEdit(HorarioAcademicoDto ahafHorarioAcademicoEdit) {
		this.ahafHorarioAcademicoEdit = ahafHorarioAcademicoEdit;
	}
	public List<AulaDto> getAhafListAulaDtoEdit() {
		ahafListAulaDtoEdit = ahafListAulaDtoEdit==null?(new ArrayList<AulaDto>()):ahafListAulaDtoEdit;
		return ahafListAulaDtoEdit;
	}
	public void setAhafListAulaDtoEdit(List<AulaDto> ahafListAulaDtoEdit) {
		this.ahafListAulaDtoEdit = ahafListAulaDtoEdit;
	}
	public List<HoraClaseDto> getAhafListHoraClaseDtoEdit() {
		ahafListHoraClaseDtoEdit = ahafListHoraClaseDtoEdit==null?(new ArrayList<HoraClaseDto>()):ahafListHoraClaseDtoEdit;
		return ahafListHoraClaseDtoEdit;
	}
	public void setAhafListHoraClaseDtoEdit(List<HoraClaseDto> ahafListHoraClaseDtoEdit) {
		this.ahafListHoraClaseDtoEdit = ahafListHoraClaseDtoEdit;
	}
	public int getAhafHabilitaAulaEdit() {
		return ahafHabilitaAulaEdit;
	}
	public void setAhafHabilitaAulaEdit(int ahafHabilitaAulaEdit) {
		this.ahafHabilitaAulaEdit = ahafHabilitaAulaEdit;
	}
	public int getAhafHoraCambioAulaEdit() {
		return ahafHoraCambioAulaEdit;
	}
	public void setAhafHoraCambioAulaEdit(int ahafHoraCambioAulaEdit) {
		this.ahafHoraCambioAulaEdit = ahafHoraCambioAulaEdit;
	}
	public int getAhafDiaCambioAula() {
		return ahafDiaCambioAula;
	}
	public void setAhafDiaCambioAula(int ahafDiaCambioAula) {
		this.ahafDiaCambioAula = ahafDiaCambioAula;
	}
	public HorarioAcademicoDto getAhafHorarioAcademicoNew() {
		return ahafHorarioAcademicoNew;
	}
	public void setAhafHorarioAcademicoNew(HorarioAcademicoDto ahafHorarioAcademicoNew) {
		this.ahafHorarioAcademicoNew = ahafHorarioAcademicoNew;
	}
	public List<AulaDto> getAhafListAulaDtoNew() {
		ahafListAulaDtoNew = ahafListAulaDtoNew==null?(new ArrayList<AulaDto>()):ahafListAulaDtoNew;
		return ahafListAulaDtoNew;
	}
	public void setAhafListAulaDtoNew(List<AulaDto> ahafListAulaDtoNew) {
		this.ahafListAulaDtoNew = ahafListAulaDtoNew;
	}
	public List<HoraClaseDto> getAhafListHoraClaseDtoNew() {
		ahafListHoraClaseDtoNew = ahafListHoraClaseDtoNew==null?(new ArrayList<HoraClaseDto>()):ahafListHoraClaseDtoNew;
		return ahafListHoraClaseDtoNew;
	}
	public void setAhafListHoraClaseDtoNew(List<HoraClaseDto> ahafListHoraClaseDtoNew) {
		this.ahafListHoraClaseDtoNew = ahafListHoraClaseDtoNew;
	}
	public List<DependenciaDto> getAhafListaDependenciaNew() {
		ahafListaDependenciaNew = ahafListaDependenciaNew==null?(new ArrayList<DependenciaDto>()):ahafListaDependenciaNew;
		return ahafListaDependenciaNew;
	}
	public void setAhafListaDependenciaNew(List<DependenciaDto> ahafListaDependenciaNew) {
		this.ahafListaDependenciaNew = ahafListaDependenciaNew;
	}
	public List<EdificioDto> getAhafListEdificioDtoNew() {
		ahafListEdificioDtoNew = ahafListEdificioDtoNew==null?(new ArrayList<EdificioDto>()):ahafListEdificioDtoNew;
		return ahafListEdificioDtoNew;
	}
	public void setAhafListEdificioDtoNew(List<EdificioDto> ahafListEdificioDtoNew) {
		this.ahafListEdificioDtoNew = ahafListEdificioDtoNew;
	}
	public Integer getAhafValidadorClic() {
		return ahafValidadorClic;
	}
	public void setAhafValidadorClic(Integer ahafValidadorClic) {
		this.ahafValidadorClic = ahafValidadorClic;
	}
	public Integer getAhafModoAsignatura() {
		return ahafModoAsignatura;
	}
	public void setAhafModoAsignatura(Integer ahafModoAsignatura) {
		this.ahafModoAsignatura = ahafModoAsignatura;
	}
	public boolean isAhafDeshabilitaDia() {
		return ahafDeshabilitaDia;
	}
	public void setAhafDeshabilitaDia(boolean ahafDeshabilitaDia) {
		this.ahafDeshabilitaDia = ahafDeshabilitaDia;
	}
	public DocenteJdbcDto getAhafDocenteJdbcDtoBusq() {
		return ahafDocenteJdbcDtoBusq;
	}
	public void setAhafDocenteJdbcDtoBusq(DocenteJdbcDto ahafDocenteJdbcDtoBusq) {
		this.ahafDocenteJdbcDtoBusq = ahafDocenteJdbcDtoBusq;
	}
	public List<Dependencia> getAhafListFacultadBusq() {
		ahafListFacultadBusq = ahafListFacultadBusq==null?(new ArrayList<Dependencia>()):ahafListFacultadBusq;
		return ahafListFacultadBusq;
	}
	public void setAhafListFacultadBusq(List<Dependencia> ahafListFacultadBusq) {
		this.ahafListFacultadBusq = ahafListFacultadBusq;
	}
	public List<Carrera> getAhafListCarreraBusq() {
		ahafListCarreraBusq = ahafListCarreraBusq==null?(new ArrayList<Carrera>()):ahafListCarreraBusq;
		return ahafListCarreraBusq;
	}
	public void setAhafListCarreraBusq(List<Carrera> ahafListCarreraBusq) {
		this.ahafListCarreraBusq = ahafListCarreraBusq;
	}
	public List<DocenteJdbcDto> getAhafListDocenteJdbcDtoBusq() {
		ahafListDocenteJdbcDtoBusq = ahafListDocenteJdbcDtoBusq==null?(new ArrayList<DocenteJdbcDto>()):ahafListDocenteJdbcDtoBusq;
		return ahafListDocenteJdbcDtoBusq;
	}
	public void setAhafListDocenteJdbcDtoBusq(List<DocenteJdbcDto> ahafListDocenteJdbcDtoBusq) {
		this.ahafListDocenteJdbcDtoBusq = ahafListDocenteJdbcDtoBusq;
	}
	public DocenteJdbcDto getAhafDocenteJdbcDtoElimi() {
		return ahafDocenteJdbcDtoElimi;
	}
	public void setAhafDocenteJdbcDtoElimi(DocenteJdbcDto ahafDocenteJdbcDtoElimi) {
		this.ahafDocenteJdbcDtoElimi = ahafDocenteJdbcDtoElimi;
	}
	public HorarioAcademicoDto getAhafHoraClaseElimi() {
		return ahafHoraClaseElimi;
	}
	public void setAhafHoraClaseElimi(HorarioAcademicoDto ahafHoraClaseElimi) {
		this.ahafHoraClaseElimi = ahafHoraClaseElimi;
	}
	public int getBandera() {
		return bandera;
	}
	public void setBandera(int bandera) {
		this.bandera = bandera;
	}
	public List<HorarioAcademicoDto> getAhafListHorarioAcademicoDocente() {
		ahafListHorarioAcademicoDocente = ahafListHorarioAcademicoDocente==null?(new ArrayList<HorarioAcademicoDto>()):ahafListHorarioAcademicoDocente;
		return ahafListHorarioAcademicoDocente;
	}
	public void setAhafListHorarioAcademicoDocente(List<HorarioAcademicoDto> ahafListHorarioAcademicoDocente) {
		this.ahafListHorarioAcademicoDocente = ahafListHorarioAcademicoDocente;
	}
	public DocenteJdbcDto getAhafDocente() {
		return ahafDocente;
	}
	public void setAhafDocente(DocenteJdbcDto ahafDocente) {
		this.ahafDocente = ahafDocente;
	}
	public MateriaDto getAhafMateriaCompartir() {
		return ahafMateriaCompartir;
	}
	public void setAhafMateriaCompartir(MateriaDto ahafMateriaCompartir) {
		this.ahafMateriaCompartir = ahafMateriaCompartir;
	}
	public List<HorarioAcademicoDto> getAhafListHorarioAcademicoCompartir() {
		ahafListHorarioAcademicoCompartir = ahafListHorarioAcademicoCompartir==null?(new ArrayList<HorarioAcademicoDto>()):ahafListHorarioAcademicoCompartir;
		return ahafListHorarioAcademicoCompartir;
	}
	public void setAhafListHorarioAcademicoCompartir(List<HorarioAcademicoDto> ahafListHorarioAcademicoCompartir) {
		this.ahafListHorarioAcademicoCompartir = ahafListHorarioAcademicoCompartir;
	}
	public List<HorarioAcademicoDto> getAhafListHorarioAcademicoCompBusq() {
		ahafListHorarioAcademicoCompBusq = ahafListHorarioAcademicoCompBusq==null?(new ArrayList<HorarioAcademicoDto>()):ahafListHorarioAcademicoCompBusq;
		return ahafListHorarioAcademicoCompBusq;
	}
	public void setAhafListHorarioAcademicoCompBusq(List<HorarioAcademicoDto> ahafListHorarioAcademicoCompBusq) {
		this.ahafListHorarioAcademicoCompBusq = ahafListHorarioAcademicoCompBusq;
	}
	public HorarioAcademicoDto getAhafHorarioAcademicoCompBusq() {
		return ahafHorarioAcademicoCompBusq;
	}
	public void setAhafHorarioAcademicoCompBusq(HorarioAcademicoDto ahafHorarioAcademicoCompBusq) {
		this.ahafHorarioAcademicoCompBusq = ahafHorarioAcademicoCompBusq;
	}
	public List<PeriodoAcademicoDto> getAhafListPeriodoAcademicoDtoCompBusq() {
		ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoCompBusq==null?(new ArrayList<PeriodoAcademicoDto>()):ahafListPeriodoAcademicoDtoCompBusq;
		return ahafListPeriodoAcademicoDtoCompBusq;
	}
	public void setAhafListPeriodoAcademicoDtoCompBusq(List<PeriodoAcademicoDto> ahafListPeriodoAcademicoDtoCompBusq) {
		this.ahafListPeriodoAcademicoDtoCompBusq = ahafListPeriodoAcademicoDtoCompBusq;
	}
	public List<CarreraDto> getAhafListCarreraDtoCompBusq() {
		ahafListCarreraDtoCompBusq = ahafListCarreraDtoCompBusq==null?(new ArrayList<CarreraDto>()):ahafListCarreraDtoCompBusq;
		return ahafListCarreraDtoCompBusq;
	}
	public void setAhafListCarreraDtoCompBusq(List<CarreraDto> ahafListCarreraDtoCompBusq) {
		this.ahafListCarreraDtoCompBusq = ahafListCarreraDtoCompBusq;
	}
	public List<Nivel> getAhafListNivelCompBusq() {
		ahafListNivelCompBusq = ahafListNivelCompBusq==null?(new ArrayList<Nivel>()):ahafListNivelCompBusq;
		return ahafListNivelCompBusq;
	}
	public void setAhafListNivelCompBusq(List<Nivel> ahafListNivelCompBusq) {
		this.ahafListNivelCompBusq = ahafListNivelCompBusq;
	}
	public List<ParaleloDto> getAhafListParaleloDtoCompBusq() {
		ahafListParaleloDtoCompBusq = ahafListParaleloDtoCompBusq==null?(new ArrayList<ParaleloDto>()):ahafListParaleloDtoCompBusq;
		return ahafListParaleloDtoCompBusq;
	}
	public void setAhafListParaleloDtoCompBusq(List<ParaleloDto> ahafListParaleloDtoCompBusq) {
		this.ahafListParaleloDtoCompBusq = ahafListParaleloDtoCompBusq;
	}
	public List<MateriaDto> getAhafListMateriaDtoCompBusq() {
		ahafListMateriaDtoCompBusq = ahafListMateriaDtoCompBusq==null?(new ArrayList<MateriaDto>()):ahafListMateriaDtoCompBusq;
		return ahafListMateriaDtoCompBusq;
	}
	public void setAhafListMateriaDtoCompBusq(List<MateriaDto> ahafListMateriaDtoCompBusq) {
		this.ahafListMateriaDtoCompBusq = ahafListMateriaDtoCompBusq;
	}
	public DocenteJdbcDto getAhafDocenteJdbcDtoComp() {
		return ahafDocenteJdbcDtoComp;
	}
	public void setAhafDocenteJdbcDtoComp(DocenteJdbcDto ahafDocenteJdbcDtoComp) {
		this.ahafDocenteJdbcDtoComp = ahafDocenteJdbcDtoComp;
	}
	
}
