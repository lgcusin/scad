/**************************************************************************
p *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     AdministracionParaleloForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de  Paralelo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
05-04-2017			Marcelo Quishpe                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.DetalleMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ArancelException;
import ec.edu.uce.academico.ejb.excepciones.ArancelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadException;
import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ArancelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.GratuidadServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoGratuidadServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CorequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PrerequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ArancelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoGratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.SolicitudTerceraMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionParaleloForm. Managed Bean que maneja las
 * peticiones para la administración de la tabla Paralelo.
 * 
 * @author lmquishpei v1.0
 * @author fgguzman v2.0
 * @version 2.0
 */

@ManagedBean(name = "administracionMatriculaForm")
@SessionScoped
public class AdministracionMatriculaForm extends HistorialAcademicoForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	//*******************************************************************/
	//******************* ATRIBUTOS DE LA CLASE *************************/
	//*******************************************************************/

	private Usuario amfUsuario;
	
	private Integer amfPeriodoAcademicoId;
	private Integer amfCarreraId;
	private Integer amfNivelId;
	
	private Integer amfParaleloId;
	
	private String amfTipoCarrera;
	private String amfIdentificacion;
	private Integer amfTipoUsuario;
	
	private boolean amfPanelInformacion;
	private boolean amfDisableParaleloNuevo;

	private EstudianteJdbcDto amfEstudianteJdbcDto;
	private FichaMatriculaDto amfMateriaParaModificacion;
	
	private Integer fgmpgfNivelUbicacion;
	
	private PeriodoAcademico fgmpgfPeriodoAcademico;
	private Carrera fgmpgfCarreraSeleccion;
	private Dependencia fgmpgfDependenciaBuscar;
	private CronogramaActividadJdbcDto fgmpgfProcesoFlujo;
	
	private TipoGratuidad fgmpgfTipoGratuidadParcial;
	private TipoGratuidad fgmpgfTipoGratuidadDefinitiva;
	private TipoGratuidad fgmpgfTipoGratuidadGratuidad;
	
	private Boolean fgmpgfPerdidaGratuidadParcial;
	private Boolean fgmpgfPerdidaGratuidadDefinitiva;
	
	private FichaInscripcionDto amfFichaInscripcionDto;
	
	private List<PeriodoAcademicoDto> amfListPeriodoAcademicoDto;
	private List<Carrera> amfListCarrera;
	private List<NivelDto> amfListNivelDto;
	
	private List<MateriaDto> fgmpgfMallaMaterias;
	
	private List<EstudianteJdbcDto> amfListEstudiantesMatriculados;
	private List<FichaMatriculaDto> amfListMatriculaSeleccionada;
	private List<MateriaDto> fgmpgfListMateriasInscrito;
	
	private List<RecordEstudianteDto> amfListHistorialAcademico;
	
	private List<ParaleloDto> amfListParalelosParaCambio;
	private List<MateriaDto> amfListMateriaDtoCorrequisitos;

	private List<MateriaDto> amfListMateriasParaAgregacion;

	private MallaCurricularParaleloDto amfMallaCurricularParaleloEdit;
	
	
	private int amfValidadorClic;
	private boolean amfHabilitaTotal;
	
	
	
	
	

	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	@EJB 	private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB	private	DependenciaServicio servDependenciaServicio;
	@EJB 	private PlanificacionCronogramaServicio servPlanificacionCronograma;
	@EJB	private TipoGratuidadServicio servTipoGratuidadServicio;
	@EJB 	private CarreraServicio servCarrera;
	@EJB	private FichaEstudianteServicio servFichaEstudiante;
	@EJB	private SolicitudTerceraMatriculaServicio servSolicitudTerceraMatricula;
	@EJB	private MateriaServicio servMateria;
	@EJB	private ArancelServicio servArancel;
	@EJB	private ParaleloServicio servParalelo;
	@EJB	private NivelServicio servNivel;
	@EJB	private GratuidadServicio servGratuidad;
	@EJB	private UsuarioRolServicio servUsuarioRol;
	@EJB	private RolFlujoCarreraServicio servRolFlujoCarrera; 
	@EJB	private MallaCurricularParaleloServicio servMallaCurricularParalelo;
	@EJB	private DetalleMatriculaServicio servDetalleMatricula;
	@EJB	private MatriculaServicio servMatriculaServicio; 
	@EJB	private MateriaServicio servMateriaServicio;
	
	
	@EJB	private CronogramaActividadDtoServicioJdbc servJdbcCronogramaActividadDto;
	@EJB 	private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB 	private FichaInscripcionDtoServicioJdbc servJdbcFichaInscripcionDto;
	@EJB	private PrerequisitoDtoServicioJdbc servJdbcPrerequisitoDto;
	@EJB 	private ParaleloDtoServicioJdbc servJdbcParaleloDto;
	@EJB	private FichaMatriculaDtoServicioJdbc servJdbcFichaMatriculaDto;
	@EJB	private CronogramaDtoServicioJdbc servJdbcCronogramaDto;
	@EJB	private HorarioAcademicoDtoServicioJdbc servJdbcHorarioAcademicoDto;
	@EJB	private CorequisitoDtoServicioJdbc servJdbcCorrequisitoDto;
	@EJB	private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB	private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB	private MallaCurricularParaleloDtoServicioJdbc servJdbcMallaCurricularParaleloDto;
	@EJB	private RecordEstudianteDtoServicioJdbc servJdbcRecordEstudianteDto;
	@EJB    private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB	private MallaCurricularDtoServicioJdbc servJdbcMallaCurricularDto;
	@EJB	private MatriculaServicioJdbc servJdbcMatricula;
	@EJB	private NivelDtoServicioJdbc servJdbcNivelDto;
	@EJB 	private SolicitudTerceraMatriculaServicio servFgmpgfSolicitudTerceraMatriculaServicio;
	@EJB	private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto; 

	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/

	/**
	 * Método que permite iniciar la administración del paralelo
	 * @param usuario  - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de Administración Paralelo.
	 */
	public String irAdministracionMatricula(Usuario usuario, int usrRol) {
		String retorno = null;
		
		try {

			amfUsuario = usuario;
			amfTipoUsuario = usrRol;
			
			List<PeriodoAcademicoDto> periodos = new ArrayList<>();
			List<Carrera> carreras = new ArrayList<>();
			iniciarFormListarEstudiantes();	

			if (RolConstantes.ROL_SECRENIVELACION_VALUE.equals(usrRol)) {
				amfPanelInformacion = false;
				periodos = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE,  PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE});
				carreras = servCarrera.buscarCarrerasPorUsuarioRol(usuario.getUsrId(), usrRol, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			}else if (RolConstantes.ROL_SECRECARRERA_VALUE.equals(usrRol)) {
				amfTipoCarrera="carreras";
				periodos = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE,  PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE});
				carreras = servCarrera.buscarCarrerasPorUsuarioRol(usuario.getUsrId(), usrRol, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			}else if (RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.equals(usrRol)) {
				amfPanelInformacion = false;
				amfTipoCarrera="suficiencias";
				carreras = servCarrera.buscarCarrerasPorUsuarioRol(usuario.getUsrId(), usrRol, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				
				asignarTipoDeSuficiencia(carreras);
				
				if(amfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE)){
					periodos = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE});
				}else if(amfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE)){
					periodos = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE});
				}else if(amfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE)){
					periodos = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE});
				}
				
			}
			
			if (!periodos.isEmpty()) {
				periodos.sort(Comparator.comparing(PeriodoAcademicoDto::getPracId).reversed());
				amfListPeriodoAcademicoDto = periodos;
			}

			if (!carreras.isEmpty()) {
				carreras.sort(Comparator.comparing(Carrera::getCrrDescripcion));
				amfListCarrera = carreras;
			}
			
			retorno = "irAdministracionMatricula";
			
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;

	}

	
	/**
	 *Método que limpia los parametros de busqueda
	 */
	public void limpiarFormListarEstudiantes(){
		amfPeriodoAcademicoId = GeneralesConstantes.APP_ID_BASE;
		amfCarreraId = GeneralesConstantes.APP_ID_BASE;
		amfNivelId = GeneralesConstantes.APP_ID_BASE;
		amfIdentificacion = new String();
		amfListNivelDto = null;
		amfListEstudiantesMatriculados = null;
		
		iniciarFormListarEstudiantes(); 
	}
	
	
	
	
	/**
	 * Método de navegación que regresa a la pagina de listar o buscar estudiatnes
	 * @return retorna a la pagina de listar o buscar estudiatnes
	 */
	public String irVerListaEstudiante(){
		amfListParalelosParaCambio = null;
		return "irVerListaEstudiante";
	}
	
	
	/**
	 * Método que dirige a realizar el cambio de paralelo del estudiante que se ha matriculado
	 *@param materia entidad que corresonde a la ficha matricula
	 * -- estados si solo si son matriculado o inscrito
	 *@return retorna la navegación a la pagina de cambiar paralelo
	 */
	public String irCambiarParalelo(FichaMatriculaDto materia){
		String retorno = null;

		if (verificarCronogramaCambioParalelo()) {

			if (materia.getRcesEstadoValue().equals(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE) || materia.getRcesEstadoValue().equals(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE)){
				amfMateriaParaModificacion = materia;
				
				amfDisableParaleloNuevo = Boolean.TRUE;
				amfParaleloId = GeneralesConstantes.APP_ID_BASE;
				
				amfMallaCurricularParaleloEdit = new MallaCurricularParaleloDto();

				try {
					List<ParaleloDto> paralelos = servJdbcParaleloDto.ListarXMateriaIdXDisponibilidadCupoXTipoPeriodo(materia.getMtrId(),amfPeriodoAcademicoId);
					if (!paralelos.isEmpty()) {
						Iterator<ParaleloDto> iter = paralelos.iterator();
						while (iter.hasNext()) {
							ParaleloDto item = (ParaleloDto) iter.next();
							if (item.getPrlId() == materia.getPrlId()) {
								iter.remove();
							}
						}
					}
					
					if (!paralelos.isEmpty()) {
						paralelos.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
						amfListParalelosParaCambio = paralelos;
						retorno = "irCambioParalelo";	
					}else {
						FacesUtil.mensajeError("No se encontró paralelos adicionales para realizar el cambio.");
					}

				} catch (ParaleloDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (ParaleloDtoNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
			}else {
				materia.setRcesEstadoLabel(etiquetarEstadoRecordEstudiantil(materia.getRcesEstadoValue()));
				FacesUtil.mensajeInfo("No es posible cambiar de paralelo a la asignatura " + materia.getMtrDescripcion() + " porque se encuentra en estado " + materia.getRcesEstadoLabel()+".");
			}

		} 

		return retorno;
	}
	
	/**
	 * Método de navegación que regresa a la pagina de ver materias matriculadas
	 * @return retorna a la pagina de ver materias matriculadas
	 */
	public String irVerMateriaMatricula(){
		String retorno = null;

		try {
			amfListMatriculaSeleccionada = servJdbcFichaMatriculaDto.ListarXidPersonaXcarrera(amfEstudianteJdbcDto.getPrsId(), amfEstudianteJdbcDto.getCrrId(), amfEstudianteJdbcDto.getPracId());
			retorno = "irVerMateriaMatricula";
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	/**
	 * Método que actualiza la información de total inscritos y de cupo del paralelo selecionado
	 */
	public void verificarDisponibilidadCupo(){
		
		try {

			if(!amfParaleloId.equals(GeneralesConstantes.APP_ID_BASE)){
				
				amfMallaCurricularParaleloEdit = servJdbcMallaCurricularParaleloDto.buscarCupoMlcrprXMateriaXParaleloAlterno(amfParaleloId, amfMateriaParaModificacion.getMtrId());
				if (amfMallaCurricularParaleloEdit.getMlcrprInscritos() < amfMallaCurricularParaleloEdit.getMlcrprCupo()) {
				
					List<HorarioAcademicoDto> horariosNuevoParalelo = cargarHorarioAcademico(amfPeriodoAcademicoId, amfParaleloId,  amfMateriaParaModificacion);
					if (!horariosNuevoParalelo.isEmpty()) {
						
						boolean existeError = false;
						for (HorarioAcademicoDto itemHorarioNuevoParalelo : horariosNuevoParalelo) {

							if (!existeError) {
								for (FichaMatriculaDto itemMatriculaVigente : amfListMatriculaSeleccionada) {

									List<HorarioAcademicoDto> horariosMatricula = cargarHorarioAcademico(amfPeriodoAcademicoId, itemMatriculaVigente.getPrlId(), itemMatriculaVigente);
									if(!horariosMatricula.isEmpty()){
										for (HorarioAcademicoDto itemHorarioConsulta : horariosMatricula) {
											if(itemMatriculaVigente.getPrlId() != amfMateriaParaModificacion.getPrlId() && amfMateriaParaModificacion.getMlcrmtId() != itemMatriculaVigente.getMlcrmtId() && !itemMatriculaVigente.getRcesEstadoValue().equals(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE)){
												if(itemHorarioNuevoParalelo.getHoclHoraInicio().equals(itemHorarioConsulta.getHoclHoraInicio())
														&& itemHorarioNuevoParalelo.getHracDia().equals(itemHorarioConsulta.getHracDia())){
													FacesUtil.mensajeError("El paralelo seleccionado genera cruce con el horario de la matrícula en la asignatura " + itemMatriculaVigente.getMtrDescripcion());
													existeError = true;
													break;
												}
											}
										}
									}else{
										existeError = true;
									}

								}
							}
						}
						
						if (!existeError) {
							amfDisableParaleloNuevo = Boolean.FALSE;
						}
						
					}else {
						amfDisableParaleloNuevo = Boolean.TRUE;
						FacesUtil.mensajeInfo("El paralelo seleccionado no tiene horario de clases asignado.");	
					}
					
				}else {
					amfDisableParaleloNuevo = Boolean.TRUE;
					FacesUtil.mensajeInfo("No hay cupo suficiente para realizar el cambio.");
				}
				
			}else {
				amfDisableParaleloNuevo = Boolean.TRUE;
				FacesUtil.mensajeInfo("Seleccione un paralelo para continuar.");
			}
			
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	private List<HorarioAcademicoDto> cargarHorarioAcademico(int periodoId, int paraleloId, FichaMatriculaDto materia) {
		List<HorarioAcademicoDto> retorno =  new ArrayList<>();

		try {
			if (!materia.getTimtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
				retorno = servJdbcHorarioAcademicoDto.buscarHorarioFull(paraleloId, materia.getMlcrmtId(), periodoId);
			}else {
				List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(materia.getMtrId());
				if (!modulos.isEmpty()) {
					List<HorarioAcademicoDto>  horarioModulo = null;
					for (MateriaDto modulo : modulos) {
						horarioModulo = servJdbcHorarioAcademicoDto.buscarHorarioFull(paraleloId, modulo.getMlcrmtId(), periodoId);
						if (horarioModulo != null && horarioModulo.size() > 0) {
							retorno.addAll(horarioModulo);
						}
					}
				}
			}
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError("El Horario de Clases no ha sido cargado en la Asignatura "+materia.getMtrDescripcion()+". Comuníque a la Secretaría de la Carrera.");
		}

		return retorno;
	}
	
	/**
	 * Método que guarda el cambio de paralelo
	 * @return retorna a la pagina de ver la lista de materias matriculadas ya actualizada con el cambio
	 */
	public String guardarCambioParalelo(){
		String retorno = null;

		DetalleMatriculaDto detalleVigente = new DetalleMatriculaDto();
		detalleVigente.setDtmtId(amfMateriaParaModificacion.getDtmtId());
		detalleVigente.setDtmtMallaCurricularParaleloDto(new MallaCurricularParaleloDto(amfMateriaParaModificacion.getMlcrprId()));
		
		RecordEstudianteDto recordVigente = new RecordEstudianteDto();
		recordVigente.setRcesId(amfMateriaParaModificacion.getRcesId());
		recordVigente.setRcesMallaCurricularParaleloDto(new MallaCurricularParaleloDto(amfMateriaParaModificacion.getMlcrprId()));
		recordVigente.setFcesId(amfMateriaParaModificacion.getFcesId());
		
		try {
			
			if(servDetalleMatricula.editar(detalleVigente, recordVigente, amfMallaCurricularParaleloEdit.getMlcrprId())){
				amfListMatriculaSeleccionada = servJdbcFichaMatriculaDto.ListarXidPersonaXcarrera(amfEstudianteJdbcDto.getPrsId(), amfEstudianteJdbcDto.getCrrId(), amfPeriodoAcademicoId);
				FacesUtil.mensajeInfo("Cambio de paralelo exitoso");
				bloqueaModal();
				retorno = "irVerMateriaMatricula";
			}else{
				FacesUtil.mensajeError("No se puede guardar los cambios, consulte con el administrador del sistema");
			}
			
		} catch (DetalleMatriculaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetalleMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}	

		return retorno;
	}

	

	/**
	 * Método que inicializa parametros de búsqueda
	 */
	private void iniciarFormListarEstudiantes(){
		amfPeriodoAcademicoId = GeneralesConstantes.APP_ID_BASE;
		amfCarreraId = GeneralesConstantes.APP_ID_BASE;
		amfNivelId = GeneralesConstantes.APP_ID_BASE;;
		amfIdentificacion = new String();
		amfParaleloId = GeneralesConstantes.APP_ID_BASE;
		amfMateriaParaModificacion = new FichaMatriculaDto();
		amfMallaCurricularParaleloEdit = new MallaCurricularParaleloDto();
		amfMateriaParaModificacion = new FichaMatriculaDto();
		amfListEstudiantesMatriculados = null;
		amfListMateriasParaAgregacion = null;
		amfListMatriculaSeleccionada = null;
		bloqueaModal();
		amfPanelInformacion = true;
	}
	
	/**
	 * Método que verifica todas las validaciones para seleccionar el paralelo
	 * @return retorna la habilitación del modal para guardar la edición o cambio de paralelo
	 */
	public void verificarClickGuardarEdicion(){
		habilitaModalGuardarEditar();
	}
	
	
	
	
	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		amfValidadorClic = 0;
	}
	
	//HABILITA EL MODAL GUARDAR EDITAR
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar edición
	 */
	public void habilitaModalGuardarEditar(){
		amfValidadorClic = 1;
	}
	
	//HABILITA EL MODAL GUARDAR NUEVO
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para guardar nuevo
	 */
	public void habilitaModalGuardarNuevo(){
		amfValidadorClic = 2;
	}
	
	//HABILITA EL MODAL ELIMINAR MATERIA MATRICULA
	/**
	 * Método que habilita el modal para que se despliegue en pantalla para eliminar materia matricula
	 */
	public void habilitaModalEliminarMateriaMatricula(){
		amfValidadorClic = 3;
	}
	
	
	/**
	 * Método que dirige a la página de agregar materia matricula del estudiante
	 * @return retorno - retorna a la página de agregar materia matricula del estudiante
	 */
	public String irAgregarMateria(){
		String retorno = null;

		if (verificarCronogramaAgregacionEliminacion()) {

			boolean isOrdenCobroCancelada = false;
			boolean isPosibleModificarMatricula = false;
			
			for(MateriaDto item: fgmpgfListMateriasInscrito){
				if(!item.getValorMatricula().equals(BigDecimal.ZERO) && item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE.intValue()){
					isOrdenCobroCancelada = true;
				}
				if (item.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE) || item.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE)) {
					isPosibleModificarMatricula = true;
				}

			}

			if(isOrdenCobroCancelada){ 
				FacesUtil.mensajeError("Orden de cobro cancelada. No se puede agregar/eliminar Asignatura.");
				bloqueaModal();
			}else{
				if (isPosibleModificarMatricula) {

					fgmpgfPerdidaGratuidadDefinitiva = calculoPerdidaGratuidad(new ArrayList<>(), amfFichaInscripcionDto);
				
					calcularNivelAsignado();
					informacionSeleccion();
					cargarMateriasParaAgregacion(amfListHistorialAcademico , amfListMatriculaSeleccionada , amfFichaInscripcionDto);

					if(!amfListMateriasParaAgregacion.isEmpty()){
						
						if (amfTipoUsuario.equals(RolConstantes.ROL_SECRECARRERA_VALUE)) {
							calcularNivelAsignado();
							informacionSeleccion();
						}
						
						retorno = "irAgregarMateria";
						
					}else {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("No se encontró materias para agregar.");
					}

				}else {
					FacesUtil.mensajeError("Se ha realizado modificaciones en la matrícula. No se puede agregar/eliminar Asignaturas.");
				}

			}


		}

		return retorno;

	}
	
	
	private List<RecordEstudianteDto> filtrarHistorialAcademicoPorCarrera(List<RecordEstudianteDto> historialAcademico, int carreraId) {
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		
		for (RecordEstudianteDto item : historialAcademico) {
			if (item.getRcesCarreraDto().getCrrId() == carreraId) {
				retorno.add(item);
			}
		}
		
		return retorno;
	}
	
	public List<MateriaDto> quitarMateriasDeLaMallaCurricular(List<FichaMatriculaDto> materiasMatriculado, List<RecordEstudianteDto> historialPorCarrera){
		 
		List<MateriaDto> materiasDisonibles = new ArrayList<>();
		List<RecordEstudianteDto> materiasAprobadas = cargarMateriasAprobadas(historialPorCarrera);
		List<RecordEstudianteDto> materiasReprobadas = cargarMateriasReprobadas(historialPorCarrera);
		List<MateriaDto> mallaCurricularMateria = new ArrayList<>();
		mallaCurricularMateria.addAll(fgmpgfMallaMaterias);
		// quitar las aprobadas de la malla
		List<MateriaDto> mallaSinAprobadas =  quitarDeMallaCurricularMateriasAprobadas(mallaCurricularMateria, materiasAprobadas);
		if (mallaSinAprobadas.isEmpty()) {
			mallaSinAprobadas = mallaCurricularMateria;
		}
		
		// quitar las que se matriculo de la malla
		List<MateriaDto> mallaSinMatriculadas = new ArrayList<>();
		if (!mallaSinAprobadas.isEmpty()) {
			mallaSinMatriculadas = quitarDeMallaCurricularMateriasMatriculado(materiasMatriculado, mallaSinAprobadas);
		}

		if (!mallaSinMatriculadas.isEmpty()) {
			//TODO: MANDAR LAS APROBADAS
			materiasDisonibles = filtrarPrerequisitosAMatricular(ordenarDescendente(mallaSinMatriculadas));
		}
		
		if (!materiasDisonibles.isEmpty()) {
			boolean continuar = true;

			for (MateriaDto item : materiasDisonibles) {
				item.setNumMatricula(calcularNumeroMatricula(materiasReprobadas, item));
				if(item.getNumMatricula() > SAUConstantes.TERCERA_MATRICULA_VALUE){
					continuar  = false;
					break;
				}else if(item.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE) {
					if(!verificarSolicitudTerceraMatricula(item.getMtrId(), item.getMtrDescripcion(), amfFichaInscripcionDto.getFcesId())){
						continuar  = false;
						break;
					}
				}
			}
			
			if (continuar) {

				for (MateriaDto aux : materiasDisonibles) {
					try {
						aux.setMtrCmbEstado(Boolean.FALSE);
						aux.setPrlId(GeneralesConstantes.APP_ID_BASE);
						aux.setMtrListParalelo(servJdbcParaleloDto.buscarParlelosPorMateriaPeriodo(aux.getMtrId(), amfPeriodoAcademicoId));
					} catch (ParaleloDtoException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (ParaleloDtoNoEncontradoException e) {
//						FacesUtil.mensajeError("No se encontró paralelos para la asignatura: " + aux.getMtrDescripcion());
					}
				}

			}else {
				materiasDisonibles = new ArrayList<>();
			}
			
		}
		
		return materiasDisonibles;
	}
	
	
	private boolean verificarSolicitudTerceraMatricula(int materiaId, String mtrDescripcion, int fcesId){
		boolean retorno = false;
		
		try {
			SolicitudTerceraMatricula solicitud = servFgmpgfSolicitudTerceraMatriculaServicio.buscarSolicitudXMtrIdXFcesIdxEstado(materiaId, fcesId);
			//si la materia se encuentra en estado solicitud 
			if(solicitud.getSltrmtTipo() == SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_VALUE){
				// si solicitud aprobada
				if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE){
					retorno = Boolean.TRUE;
				}
			}
			//si la materia se encuentra en estado apelacion 
			if(solicitud.getSltrmtTipo() == SolicitudTerceraMatriculaConstantes.TIPO_APELACION_VALUE){
				if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE){
					retorno = Boolean.TRUE;
				}
			}
		} catch (SolicitudTerceraMatriculaNoEncontradoException e) {
			FacesUtil.mensajeError("Su solicitud de Tercera matrícula para la asignatura " + mtrDescripcion + " no se encuentra aprobada.");
		} catch (SolicitudTerceraMatriculaValidacionException e) {
			FacesUtil.mensajeError("Se encontró varias solicitudes aprobadas de tercera matrícula para la materia solicitada. " + mtrDescripcion);
		} catch (SolicitudTerceraMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	
	private int calcularNumeroMatricula(List<RecordEstudianteDto> reprobadas, MateriaDto item){
		int retorno = 1;
		
		if (!reprobadas.isEmpty()) {
			reprobadas.sort(Comparator.comparing(RecordEstudianteDto::getMtrCodigo));
			for (RecordEstudianteDto itemRecord : reprobadas) {
				if (itemRecord.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && itemRecord.getMtrCodigo().equals(item.getMtrCodigo())) {
					if (itemRecord.getRcesMateriaDto().getNumMatricula() == 2 && itemRecord.getRcesPracId() == PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE) {
						retorno = itemRecord.getRcesMateriaDto().getNumMatricula();
						retorno++;
					}else {
						retorno++;	
					}
				}
			}
			return retorno;
		}else {
			return retorno;			
		}

	}

	private List<RecordEstudianteDto> cargarMateriasReprobadas(List<RecordEstudianteDto> recordEstudiantil){
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		
		try {
			retorno = recordEstudiantil.stream().filter(itemRecord->
			itemRecord.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) ||
			itemRecord.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL)).collect(Collectors.toList());
		} catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	/**
	 * Método que permite cargar solo materias aprobadas, convalidadas y homologadas
	 * @param recordEstudiantil - record academico de la carrera seleccionada para matricula.
	 * @return materias reprobadas.
	 */
	private List<RecordEstudianteDto> cargarMateriasAprobadas(List<RecordEstudianteDto> recordEstudiantil){
		List<RecordEstudianteDto> materiasAprobadas = recordEstudiantil.stream().filter(itemRecord->
		itemRecord.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) || 
		itemRecord.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)||
		itemRecord.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) ||
		itemRecord.getRcesEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)).collect(Collectors.toList());
		return materiasAprobadas;
	}
	
	/**
	 * M��todo que  elimina las materias reprobadas que ya aprobo el estudiante.
	 * @param  materiasAprobadas
	 * @param  materiasMalla
	 * @return materias reprobadas que aun no aprueba.
	 */
	private List<MateriaDto> quitarDeMallaCurricularMateriasAprobadas(List<MateriaDto> materiasMalla, List<RecordEstudianteDto> materiasAprobadas) {
		List<MateriaDto> materiasAtomar = new ArrayList<>();
		List<RecordEstudianteDto> aprobadas = new ArrayList<>();

		if(materiasMalla!=null && materiasAprobadas!=null){
			if (materiasMalla.size() > 0 ) {
				if (materiasAprobadas.size() > 0) {
					// quitar mas de uno
					Map<String, MateriaDto> mapMateriasMalla =  new HashMap<String, MateriaDto>();
					for (MateriaDto it : materiasMalla) {
						mapMateriasMalla.put(it.getMtrCodigo(), it);
					}
					for (Entry<String, MateriaDto> item : mapMateriasMalla.entrySet()) {
						materiasAtomar.add(item.getValue());
					}
					// quitar mas de uno
					Map<String, RecordEstudianteDto> mapAprobadas =  new HashMap<String, RecordEstudianteDto>();
					for (RecordEstudianteDto it : materiasAprobadas) {
						mapAprobadas.put(it.getRcesMateriaDto().getMtrCodigo(), it);
					}
					for (Entry<String, RecordEstudianteDto> item : mapAprobadas.entrySet()) {
						aprobadas.add(item.getValue());
					}

					// eliminar de la malla las que fueron aprobadas
					Iterator<RecordEstudianteDto> itMateriasAprobadas = aprobadas.iterator();
					while (itMateriasAprobadas.hasNext()) {
						Iterator<MateriaDto> itMateriasMalla = materiasAtomar.iterator();
						String aux1 = itMateriasAprobadas.next().getRcesMateriaDto().getMtrCodigo();
						while (itMateriasMalla.hasNext()) {
							String aux2 = itMateriasMalla.next().getMtrCodigo();
							if (GeneralesUtilidades.quitarEspaciosEnBlanco(aux1).equals(GeneralesUtilidades.quitarEspaciosEnBlanco(aux2))) {
								itMateriasMalla.remove();
							}
						}
					}
				}else {
					// porque no hay aprobadas
					return new ArrayList<>();
				}
			}else{
				// por que no hay reprobadas
				return new ArrayList<>();			
			}
		}else{
			//lanzar exception "Lista de materias reprobadas o aprobadas nula"
			return new ArrayList<>();
		}

		return materiasAtomar;
	}	
	
	private List<MateriaDto> quitarDeMallaCurricularMateriasMatriculado(List<FichaMatriculaDto> materiasMatriculado, List<MateriaDto> mallaSinAprobadas) {

		for(FichaMatriculaDto item: materiasMatriculado){
			Iterator<MateriaDto> itera = mallaSinAprobadas.iterator();
			while(itera.hasNext()){
				MateriaDto cad = (MateriaDto) itera.next();
				if(cad.getMtrId() == item.getMtrId()){
					itera.remove();
				}
			}	
		}

		return mallaSinAprobadas;
	}
	
	
	
	/**
	 * Ordena una lista decendentemente
	 */
	public static List<MateriaDto> ordenarDescendente(List<MateriaDto> lista) {
		int i, j;
		for(i=0;i<lista.size()-1;i++)
			for(j=0;j<lista.size()-i-1;j++){
				if (lista.get(j+1).getNvlNumeral()>lista.get(j).getNvlNumeral()) {
					MateriaDto aux = new MateriaDto();
					aux=lista.get(j+1);
					lista.set(j+1,lista.get(j));
					lista.set(j,aux);
				}
			}
		return lista;
	}
	
	/** 
	 * Filtra prerequisitos de las materias a matricular
	 * @param materiasPorTomar .- Materias  filtar
	 * @return lista de materias a matricular filtradas por prerequisitos
	 */
	@SuppressWarnings("rawtypes")
	public List<MateriaDto> filtrarPrerequisitosAMatricular(List<MateriaDto> materiasPorTomar) {
		 
		List<MateriaDto> retorno = new ArrayList<>();
		retorno.addAll(materiasPorTomar);
		
		try{
			
			for(MateriaDto item: materiasPorTomar){
				List<MateriaDto> prerequisitos = servJdbcPrerequisitoDto.listarXidMateriaFull(item.getMtrId());
				 
				Boolean remover = false;
				for(MateriaDto ite: materiasPorTomar){
					for(MateriaDto it: prerequisitos){
						if(ite.getMtrId() == it.getMtrId() ){
							remover = true;
						}
					}
				}
				
				if(remover){
					Iterator itera = retorno.iterator();
					while(itera.hasNext()){
						MateriaDto cad = (MateriaDto) itera.next();
						if( cad.getMtrId() == item.getMtrId() ){
							itera.remove();
						}
					}
				}
			}
			
		} catch (MateriaDtoException e) {
			e.printStackTrace();
		} catch (MateriaDtoNoEncontradoException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	/**
	 * Calcula la perdida de gratuidad definitiva del estudiante
	 * @param materiasReprobadas .- Lista de materias reprobadas
	 * @return retorna si perdio o no gratuidad
	 */
	public boolean calculoPerdidaGratuidad(List<MateriaDto> materiasReprobadas , FichaInscripcionDto fichaInscripcion ){

		boolean retorno = false;
		DecimalFormat df = new DecimalFormat("#.00"); 
		Integer numeroTotalMalla = 0;
		Integer numeroTotalPeridido = 0;
		Double gratuidaPorcentaje = 0.0;

		if(!fichaInscripcion.getFcinTipoIngreso().equals(FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)){

			if(materiasReprobadas != null && materiasReprobadas.size() > 0 ){
				for(MateriaDto item: fgmpgfMallaMaterias){
					if(item.getMtrHoras() != null && item.getMtrHoras() != 0){
						numeroTotalMalla = numeroTotalMalla+item.getMtrHoras();
					}
					if(item.getMtrCreditos() != null && item.getMtrCreditos() != 0){
						numeroTotalMalla = numeroTotalMalla+item.getMtrCreditos();
					} 
				}

				for(MateriaDto item: materiasReprobadas){
					for(MateriaDto it: fgmpgfMallaMaterias){
						if(item.getMtrId() == it.getMtrId()){
							if(it.getMtrHoras() != null && it.getMtrHoras() != 0){
								numeroTotalPeridido = numeroTotalPeridido+it.getMtrHoras();
							}
							if(it.getMtrCreditos() != null && it.getMtrCreditos() != 0){
								numeroTotalPeridido = numeroTotalPeridido+it.getMtrCreditos();
							}
						}
					}	
				}

				gratuidaPorcentaje = (Double.valueOf(numeroTotalPeridido)*100)/Double.valueOf(numeroTotalMalla);
				if(gratuidaPorcentaje != 0.0){ 
					gratuidaPorcentaje = Double.valueOf(df.format(gratuidaPorcentaje).replace(",", "."));

					if(gratuidaPorcentaje >= Double.valueOf(fgmpgfTipoGratuidadDefinitiva.getTigrPorcentajeGratuidads().get(fgmpgfTipoGratuidadDefinitiva.getTigrPorcentajeGratuidads().size()-1).getPrgrValor().toString())){
						retorno = true;
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.calculo.perdida.gratuidad.definitiva")));
					}

				}
				
			}
			
			try {
				FichaEstudiante fichaEstudiante = servFichaEstudiante.buscarPorId(fichaInscripcion.getFcesId());
				if(fichaEstudiante.getFcesTipoUnivEstudPrev() != null && fichaEstudiante.getFcesTipoUnivEstudPrev().equals(FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE)){
					retorno = true;
					FacesUtil.mensajeError("Usted pierde la Gratuidad definitivamente, ya que está cursando una segunda carrera.");
				}
			} catch (FichaEstudianteNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (FichaEstudianteException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else{
			
			try {
				FichaEstudiante fichaEstudiante = servFichaEstudiante.buscarPorId(fichaInscripcion.getFcesId());
				if(fichaEstudiante.getFcesTipoUnivEstudPrev() != null && fichaEstudiante.getFcesTipoUnivEstudPrev().equals(FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE)){
					retorno = true;
					FacesUtil.mensajeError("Usted pierde la Gratuidad definitivamente, ya que está cursando una segunda carrera.");
				}
			} catch (FichaEstudianteNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (FichaEstudianteException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}

		return retorno; 
	}
	
	private boolean verificarCronogramaCambioParalelo(){
		boolean retorno = false;
		CronogramaActividadJdbcDto reajuste = null;

		CronogramaActividadJdbcDto tipoCronograma = cargarTipoCronogramaDelEstudiante();
		if (tipoCronograma != null) {
			try {
				reajuste = servJdbcCronogramaActividadDto.buscarCronogramaPorPeriodo(ProcesoFlujoConstantes.PROCESO_FLUJO_REAJUSTE_VALUE, tipoCronograma.getCrnTipo(),amfPeriodoAcademicoId);
				if (reajuste != null) {
					if (GeneralesUtilidades.getFechaActualSistema().before(reajuste.getPlcrFechaFin()) && GeneralesUtilidades.getFechaActualSistema().after(reajuste.getPlcrFechaInicio())) {
						retorno = true;
					}else{
						FacesUtil.mensajeInfo("La agregación y eliminación de Asignaturas se encuentra fuera de fechas según cronograma.");
					}
				}
			} catch (CronogramaActividadDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError("Error al buscar Cronograma para rejuste de matrículas.");
			}
		}else {
			FacesUtil.mensajeError("Error al buscar el Cronograma de matriculación.");
		}

		return retorno;
	}

	private boolean verificarCronogramaAgregacionEliminacion(){
		boolean retorno = false;
		CronogramaActividadJdbcDto reajuste = null;
		CarreraDto carrera = null;
		
		CronogramaActividadJdbcDto tipoCronograma = cargarTipoCronogramaDelEstudiante();
		if (tipoCronograma != null) {
			try {
				reajuste = servJdbcCronogramaActividadDto.buscarCronogramaPorPeriodo(ProcesoFlujoConstantes.PROCESO_FLUJO_REAJUSTE_VALUE, tipoCronograma.getCrnTipo(),amfPeriodoAcademicoId);
				if (reajuste != null) {
					if (GeneralesUtilidades.getFechaActualSistema().before(reajuste.getPlcrFechaFin()) && GeneralesUtilidades.getFechaActualSistema().after(reajuste.getPlcrFechaInicio())) {
						try {
							carrera = servJdbcCarreraDto.buscarXId(amfCarreraId);
							if (carrera != null && carrera.getCrrReajusteMatricula().intValue() == 0) {
								retorno = true;
							}else {
								FacesUtil.mensajeInfo("La agregación y eliminación de Asignaturas se encuentra desactivada para la carrera " + carrera.getCrrDescripcion());
							}
						} catch (CarreraDtoJdbcException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (CarreraDtoJdbcNoEncontradoException e) {
							FacesUtil.mensajeError(e.getMessage());
						}
					}else{
						FacesUtil.mensajeInfo("La agregación y eliminación de Asignaturas se encuentra fuera de fechas según cronograma.");
					}
				}
			} catch (CronogramaActividadDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError("Error al buscar Cronograma para rejuste de matrículas.");
			}
		}else {
			FacesUtil.mensajeError("Error al buscar el Cronograma de matriculación.");
		}
		
		return retorno;
	}
	
	private CronogramaActividadJdbcDto cargarTipoCronogramaDelEstudiante() {
		CronogramaActividadJdbcDto retorno = null;

		try {
			retorno = servJdbcCronogramaActividadDto.buscarCronogramaPorPlanificacionCronograma(amfEstudianteJdbcDto.getFcmtPlcrId());
		} catch (CronogramaActividadDtoJdbcException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}

		return retorno;
	}



	
	/**
	 * Verifica que el usuario no refleje matricula vigente en este periodo
	 * @return Retorna si esta amtriculado o no
	 */
	public Boolean verificarMatriculaRegistrada(FichaInscripcionDto fichaInscripcion){
		Boolean retorno = false;
		
		List<FichaMatriculaDto> materias = new ArrayList<>();
		try {
			materias = servJdbcFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(amfPeriodoAcademicoId, fichaInscripcion.getPrsIdentificacion(), fichaInscripcion.getCrrId());
			if (materias != null && !materias.isEmpty()) {
				retorno = true;
			}
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError("No se ha registrado una matrícula para agregar materias.");
		}

		return retorno;
	}

	/**
	 * Calcula el nivel asignado al estudiante segun sus materias seleccionadas y numero total de creditos
	 * @param mensaje .- Si se emite mensaje o no. 
	 **/
	public void calcularNivelAsignado(){

		List<MateriaDto> materiasParaRecalculo = new ArrayList<>();
		Integer numeroTotalCreditosHoras = 0;

		if(!fgmpgfListMateriasInscrito.isEmpty()){
			
			for(MateriaDto item: fgmpgfListMateriasInscrito){
				
				item.setMtrCmbEstado(Boolean.TRUE);
				
				if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
					numeroTotalCreditosHoras = numeroTotalCreditosHoras + item.getMtrCreditos();
				}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
					numeroTotalCreditosHoras = numeroTotalCreditosHoras + item.getMtrHoras();
				}
				
				materiasParaRecalculo.add(item);
				
			}
			
		}

		if(!amfListMateriasParaAgregacion.isEmpty() || !fgmpgfListMateriasInscrito.isEmpty()){

			if(!amfListMateriasParaAgregacion.isEmpty()){
				
				for (MateriaDto item : amfListMateriasParaAgregacion) {
					
					if(item.getMtrCmbEstado()){
						
						materiasParaRecalculo.add(item);

						if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
							numeroTotalCreditosHoras = numeroTotalCreditosHoras + item.getMtrCreditos();
						}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
							numeroTotalCreditosHoras = numeroTotalCreditosHoras + item.getMtrHoras();
						}
						
					}
					
				}
				
			}

			
			List<List<MateriaDto>> materiasAnidadas = new ArrayList<>();

			Integer nivelAsignacion = 0;
			Integer numeroCreditos = 0;

			List<Integer> creditosXSemestre = new ArrayList<>();
			List<Integer> nivelXSemestre = new ArrayList<>();

			for(MateriaDto item: materiasParaRecalculo){
				
				if(item.getMtrCmbEstado()){
					
					List<MateriaDto> grupos = new ArrayList<>();
					for(MateriaDto itemReplica: materiasParaRecalculo){
						
						if( item.getNvlNumeral() == itemReplica.getNvlNumeral()){
							grupos.add(itemReplica);
						}
						
					}
					
					if(grupos.size() > 0){
						materiasAnidadas.add(grupos);
					}
				}
				
			}


			for (int i = 0; i < materiasAnidadas.size(); i++) {

				for(MateriaDto itemR: materiasAnidadas.get(i)){

					if(itemR.getMtrCreditos() != null && itemR.getMtrCreditos() != 0){
						numeroCreditos = numeroCreditos+itemR.getMtrCreditos();
						nivelAsignacion = itemR.getNvlNumeral();
					}
					if(itemR.getMtrHoras() != null && itemR.getMtrHoras() != 0){
						numeroCreditos = numeroCreditos+itemR.getMtrHoras();
						nivelAsignacion = itemR.getNvlNumeral();
					}
				}

				Boolean adjuntar = true;

				for (int j = 0; j < nivelXSemestre.size(); j++) {

					if(nivelXSemestre.get(j) == nivelAsignacion){
						adjuntar = false;
					}
				}

				if(adjuntar){
					creditosXSemestre.add(numeroCreditos);
					nivelXSemestre.add(nivelAsignacion);
				}

				numeroCreditos = 0;
				nivelAsignacion = 0;
			}

			if(nivelXSemestre.size() > 1){

				for(int i=0; i<creditosXSemestre.size() && i<nivelXSemestre.size(); i++){ 
					if(creditosXSemestre.get(i)>numeroCreditos){ // 
						numeroCreditos = creditosXSemestre.get(i);
						nivelAsignacion = nivelXSemestre.get(i);
					}
					if(creditosXSemestre.get(i)==numeroCreditos){ // 
						if(nivelXSemestre.get(i)<nivelAsignacion){ // 
							numeroCreditos = creditosXSemestre.get(i);
							nivelAsignacion = nivelXSemestre.get(i);
						}
					}
				}

			}else{
				if(creditosXSemestre.size() != 0){
					numeroCreditos = creditosXSemestre.get(creditosXSemestre.size()-1);
				}else{
					numeroCreditos = 0;
				}
				if(nivelXSemestre.size() != 0){
					nivelAsignacion = nivelXSemestre.get(nivelXSemestre.size()-1);
				}else{
					nivelAsignacion = 0;
				}
			}

			fgmpgfNivelUbicacion = nivelAsignacion;
		} 
	}
	
	/**
	 * Muestra la informacion de cuantas materias, Horas/Creditos, porcentaje de creditos seleccionados y calcula gratuidad temporal
	 * @return retorna el arreglo de la descripcion.
	 */
	public String[] informacionSeleccion(){

		String[] retorno = new String[8];

		Integer creditosPrimera = 0;
		Integer creditosNivelUbicacion = 0;
		Integer totalCreditos = 0;
		String gratuidad = null;
		

		if(!fgmpgfListMateriasInscrito.isEmpty()){
			for(MateriaDto item: fgmpgfListMateriasInscrito){
				
				if(item.getNumMatricula() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
					
					if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
						creditosPrimera = creditosPrimera + item.getMtrCreditos();
					}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
						creditosPrimera = creditosPrimera + item.getMtrHoras();
					}
					
				}
				
			}
		}
		
		if (!fgmpgfMallaMaterias.isEmpty()) {
			for(MateriaDto item: fgmpgfMallaMaterias){
				if(item.getNvlNumeral().equals(fgmpgfNivelUbicacion)){

					if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
						creditosNivelUbicacion = creditosNivelUbicacion + item.getMtrCreditos();
					}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
						creditosNivelUbicacion = creditosNivelUbicacion + item.getMtrHoras();
					}

				}
			}
		}

		Double gratuidadPorcentaje = 0.0;
		List<MateriaDto> materiasParaRecalculo = new ArrayList<>();
		if(!amfListMateriasParaAgregacion.isEmpty() || !fgmpgfListMateriasInscrito.isEmpty()){

			if(!amfListMateriasParaAgregacion.isEmpty()){
				for(MateriaDto item: amfListMateriasParaAgregacion){
					
					if(item.getMtrCmbEstado()){
						materiasParaRecalculo.add(item);
						
						if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
							
							totalCreditos = totalCreditos + item.getMtrCreditos();

							if(item.getNumMatricula() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
								creditosPrimera = creditosPrimera + item.getMtrCreditos();
							}
							
						}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
							
							totalCreditos = totalCreditos + item.getMtrHoras();

							if(item.getNumMatricula() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
								creditosPrimera = creditosPrimera + item.getMtrHoras();
							}
							
						}
						
					}
					
				}

			}
			
			if(!fgmpgfListMateriasInscrito.isEmpty()){
				for(MateriaDto item: fgmpgfListMateriasInscrito){
					
					item.setMtrCmbEstado(Boolean.TRUE);
					if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
						totalCreditos = totalCreditos + item.getMtrCreditos();
					}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
						totalCreditos = totalCreditos + item.getMtrHoras();
					}
					materiasParaRecalculo.add(item);
					
				}
			}

			if(creditosPrimera > fgmpgfCarreraSeleccion.getCrrNumMaxCreditos()){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.informacion.seleccion.maximo.horas", fgmpgfCarreraSeleccion.getCrrNumMaxCreditos(), etiquetarSiEsHoraCredito())));
			}

			DecimalFormat df = new DecimalFormat("#.00"); 
			if(creditosNivelUbicacion != null && !creditosNivelUbicacion.equals(0)){
				gratuidadPorcentaje = (Double.valueOf(creditosPrimera.intValue())*100)/Double.valueOf(creditosNivelUbicacion.intValue());
				gratuidadPorcentaje = (Double.valueOf(df.format(gratuidadPorcentaje).replace(",", ".")));
			}

			if(gratuidadPorcentaje >= Double.valueOf(fgmpgfTipoGratuidadParcial.getTigrPorcentajeGratuidads().get(fgmpgfTipoGratuidadParcial.getTigrPorcentajeGratuidads().size()-1).getPrgrValor().toString())){
				gratuidad = fgmpgfTipoGratuidadGratuidad.getTigrDescripcion();
				fgmpgfPerdidaGratuidadParcial = false;//aplica gratuidad
			}else{
				gratuidad = fgmpgfTipoGratuidadParcial.getTigrDescripcion();
				fgmpgfPerdidaGratuidadParcial = true;//perdida temporal
			}
		}

		if(!materiasParaRecalculo.isEmpty()){
			retorno[0] = String.valueOf(materiasParaRecalculo.size());
			retorno[3] = String.valueOf(gratuidadPorcentaje);
		}else{
			retorno[0] = null;
			retorno[3] = null;
		}
		
		if(establecerSiEsHoraCredito()){
			retorno[1] = String.valueOf(totalCreditos);
			retorno[6] = String.valueOf(creditosPrimera);
			retorno[7] = null;
			retorno[2] = null;
		}else{//horas
			retorno[2] = String.valueOf(totalCreditos);
			retorno[7] = String.valueOf(creditosPrimera);
			retorno[1] = null;
			retorno[6] = null;
		}


		if(fgmpgfPerdidaGratuidadDefinitiva){
			retorno[4] = TipoGratuidadConstantes.PERDIDA_DEFINITIVA_LABEL;
		}else{
			retorno[4] = gratuidad;
		}
		
		retorno[5] = String.valueOf(fgmpgfNivelUbicacion);

		return retorno; 
	}


	private Boolean establecerSiEsHoraCredito() {
		Boolean retorno = null;
		
		if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
			retorno = Boolean.TRUE;
		}else if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	public String etiquetarSiEsHoraCredito(){
		if (amfFichaInscripcionDto.getCrrTipo().equals(CarreraConstantes.TIPO_PREGRADO_VALUE)) {
			if (amfFichaInscripcionDto.getCrrProceso().equals(CarreraConstantes.PROCESO_MIGRACION_SIAC_A_SIIU_VALUE)) {
				return "Créditos";
			}else {
				return "Horas";
			}
		}else {
			return "Créditos";
		}
	}
	
	
	/**
	 * Metodo de verificacion de disponibilidad de la materia en el paralelo seleccionado
	 */
	public void verificarClickDisponibilidad(MateriaDto materiaSeleccionada, Boolean seleccion){

		
		List<MateriaDto> materiasNuevaMatricula = new ArrayList<>();
		materiasNuevaMatricula.addAll(amfListMateriasParaAgregacion);
		materiasNuevaMatricula.addAll(fgmpgfListMateriasInscrito);
	
		if(seleccion){
			if(materiaSeleccionada.getPrlId() != GeneralesConstantes.APP_ID_BASE){
				if(verificarCupos(materiaSeleccionada.getMlcrmtId(), materiaSeleccionada.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), true)){
					
					if (verificarCruceHorarios(materiaSeleccionada, materiasNuevaMatricula, amfFichaInscripcionDto)) { 
						redisableSeleccionMaterias(materiaSeleccionada);
						disableCheck(materiaSeleccionada);
					}else {
						FacesUtil.limpiarMensaje();
					}
					
				}else{
					materiaSeleccionada.setMtrCmbEstado(redisableSeleccionMaterias(materiaSeleccionada).getMtrCmbEstado());
					redisableSeleccionMaterias(materiaSeleccionada);
					disableCheck(materiaSeleccionada);
				}
			}else{
				materiaSeleccionada.setMtrCmbEstado(false);
				materiaSeleccionada.setPrlId(GeneralesConstantes.APP_ID_BASE);
				redisableSeleccionMaterias(materiaSeleccionada);
				disableCheck(materiaSeleccionada);
			}
		}

		calcularNivelAsignado();
		informacionSeleccion();
	}

	/**
	 * Verifica la existencia de cupos disponibles.
	 * @param mlcrmtID .- id de Malla curricular materia 
	 * @param prlId .- id de Paralelo 
	 * @return Retorna la respuesta
	 */
	public Boolean verificarCupos(int mlcrmtID, int prlId, int pracId, boolean mensaje){
		Boolean verificar = true;
		try {
			if(prlId != GeneralesConstantes.APP_ID_BASE){
				List<Integer> cupos = servJdbcMatricula.buscarCuposAndMatriculados(mlcrmtID, prlId, pracId);

				if(cupos.get(1) >= cupos.get(0) ){
					verificar = false;
				}

				if(mensaje){
					if(!verificar){
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.cupos.llenos")));
					}
				}
			}

		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return verificar;
	}
	
	/**
	 * Setear estado de seleccion de las materias
	 * @param materia .- Materia a afectar 
	 * @return Efecto sobre la materia
	 */
	public MateriaDto redisableSeleccionMaterias(MateriaDto materia){

		if (materia.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE) {
			materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
			materia.setMtrCmbEstado(true);
		}else{
			materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
			materia.setMtrCmbEstado(false);
		}
		
		List<MateriaDto> mallaMaterias = new ArrayList<>();
		mallaMaterias.addAll(amfListMateriasParaAgregacion);
		
		
		for(MateriaDto item: amfListMateriasParaAgregacion){
			if(item.getPrlId() != null && item.getPrlId() != GeneralesConstantes.APP_ID_BASE){
				if(verificarCupos(item.getMlcrmtId(), item.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), false)){
					item.setMtrCmbEstado(true);
				}else{
					item.setMtrCmbEstado(false);
					item.setPrlId(GeneralesConstantes.APP_ID_BASE);;
				}
			}
		}
		
		return materia;
	}
	
	/**
	 * Deshabilita los checks para las materias obligatorias 
	 * @param materia .- Dto para la busqueda
	 * @return retorna la disable / no disable
	 */
	public Boolean disableCheck(MateriaDto materia){

		return true;
	}
	
	/**
	 * Método que verifica el cruce de horas.
	 * @param materia materia por verificar si existe algún cruce en el horario.
	 * @param materias lista de materias tomadas por el estudiante.
	 * @return true si hay algun cruce de horas o false si no hay ningun cruce de horas.
	 */
	private boolean verificarCruceHorarios(MateriaDto materiaSeleccionada, List<MateriaDto> materiasNuevaMatricula, FichaInscripcionDto fichaInscripcion) {
		boolean retorno = false;

		if (!materiasNuevaMatricula.isEmpty()) {

			if(!fichaInscripcion.getCncrModalidad().equals(ModalidadConstantes.TIPO_MODALIDAD_DISTANCIA_VALUE)){

				List<HorarioAcademicoDto> horarioMateriaSeleccionada = cargarHorarioAcademico(amfPeriodoAcademicoId, materiaSeleccionada.getPrlId(), materiaSeleccionada);
				if (!horarioMateriaSeleccionada.isEmpty()) {

					for (MateriaDto materiaNuevaMatricula : materiasNuevaMatricula) {

						if(materiaNuevaMatricula.getMtrCmbEstado() != null && materiaNuevaMatricula.getMtrCmbEstado() && materiaNuevaMatricula.getPrlId()!= null && materiaNuevaMatricula.getPrlId() != GeneralesConstantes.APP_ID_BASE){

							List<HorarioAcademicoDto> horarioMateriaNuevaMatricula = cargarHorarioAcademico(amfPeriodoAcademicoId, materiaNuevaMatricula.getPrlId(), materiaNuevaMatricula);
							if (!horarioMateriaNuevaMatricula.isEmpty()) {

								if(materiaNuevaMatricula.getPrlId() != materiaSeleccionada.getPrlId() && materiaSeleccionada.getMlcrmtId() != materiaNuevaMatricula.getMlcrmtId()  && !materiaNuevaMatricula.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE)){
									for (HorarioAcademicoDto hor1 : horarioMateriaNuevaMatricula) {
										for (HorarioAcademicoDto hor2 : horarioMateriaSeleccionada) {
											if (hor2.getHracDia().equals(hor1.getHracDia())) {
												if (hor2.getHoclHoInicio().equals(hor1.getHoclHoInicio()) && hor2.getHoclHoFin().equals(hor1.getHoclHoFin())) {
													FacesUtil.mensajeError("El paralelo seleccionado genera cruce con el horario de la asignatura " + materiaNuevaMatricula.getMtrDescripcion());
													retorno = true;
												}
											}
										}
									}
								}
							}else {
								retorno = true;
							}
						}
					}
				}else {
					retorno = true;
				}

			}
		}

		return retorno;
	}
	
	private List<HorarioAcademicoDto> cargarHorarioAcademico(int periodoId, int paraleloId, MateriaDto materia) {
		List<HorarioAcademicoDto> retorno =  new ArrayList<>();

		try {
			if (materia.getTpmtId() != TipoMateriaConstantes.TIPO_MODULAR_VALUE) {
				retorno = servJdbcHorarioAcademicoDto.buscarHorarioFull(paraleloId, materia.getMlcrmtId(), periodoId);
			}else {
				List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(materia.getMtrId());
				if (!modulos.isEmpty()) {
					List<HorarioAcademicoDto>  horarioModulo = null;
					for (MateriaDto modulo : modulos) {
						horarioModulo = servJdbcHorarioAcademicoDto.buscarHorarioFull(paraleloId, modulo.getMlcrmtId(), periodoId);
						if (horarioModulo != null && horarioModulo.size() > 0) {
							retorno.addAll(horarioModulo);
						}
					}
				}
			}
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError("El Horario de Clases no ha sido cargado en la Asignatura "+materia.getMtrDescripcion()+". Comuníque a la Secretaría de la Carrera.");
		}

		return retorno;
	}
	
	/**
	 * Reinicia el valor del check y paralelo de la materia seleccionada
	 * @param materia .- materia a usar
	 */
	public void resetComboParalelo(MateriaDto materia) {
		calcularNivelAsignado();
		informacionSeleccion();
		filtrarCorequisitosAMatricular(materia);
		materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/** 
	 * Filtra corequisits de las materias a matricular
	 * @param materiasPorTomar .- Materias  filtar
	 * @return lista de materias a matricular filtradas por prerequisitos
	 */
	public void filtrarCorequisitosAMatricular(MateriaDto materia) {
		
		List<MateriaDto> corequisitos = null;
		try{
			corequisitos = servJdbcCorrequisitoDto.listarXidMateriaFull(materia.getMtrId());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			corequisitos = null;
		}

		if(corequisitos != null && corequisitos.size() >0){
			for(MateriaDto it: corequisitos){
				for(MateriaDto item: amfListMateriasParaAgregacion){
					if(item.getMtrId() == it.getCrqMtrCorequisitoId()){
						if(materia.getMtrCmbEstado()){ 
							item.setMtrCmbEstado(true);
							item.setMtrCmbEstadoDisable(true);
						}else{
							item.setMtrCmbEstadoDisable(false);
						}
					}
				}
			}
		}
	 
	}
	
	/**
	 * Método que verifica que no exista restricciones para matricular la materia seleccionarndo el paralelo disponible
	 */
	public void verificarClickGuardarNuevo(){

	 
			
			if(verificarMatriculaRegistrada(amfFichaInscripcionDto)){

				if(verificarSeleccion(false)){

					Boolean verificarCupos = false;

					try {

						List<MateriaDto> materiasMatricula = new ArrayList<>();
						for(MateriaDto item: amfListMateriasParaAgregacion){
							if(item.getMtrCmbEstado() != null && item.getMtrCmbEstado()){
								Paralelo paralelo = servParalelo.buscarPorId(item.getPrlId());
								item.setPrlDescripcion(paralelo.getPrlDescripcion());
								item.setValorMatricula(calcularValorMateria(item));
								materiasMatricula.add(item);
								if(!verificarCupos(item.getMlcrmtId(), item.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), true)){
									if(!verificarCupos){
										verificarCupos = true;
									}else{
										bloqueaModal();
									}
								}else{
									bloqueaModal();
								}
							}else{
								bloqueaModal();
							}
						}


						if(!verificarCupos ){

							habilitaModalGuardarNuevo();

						}else{
							bloqueaModal();
						}

					}catch (ParaleloNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
						bloqueaModal();
					} catch (ParaleloException e) {
						FacesUtil.mensajeError(e.getMessage());
						bloqueaModal();
					} 
				}else{
					bloqueaModal();
				}

			}else{
				bloqueaModal();
			}
	 
		
		
	}

	
	/**
	 * Verifica los parametros necesarios en ls elementos seleccionados
	 * @return valida o no para generar matricula
	 */
	public Boolean verificarSeleccion(Boolean mensaje){
		Boolean verificar = true;

		if (amfListMateriasParaAgregacion != null && amfListMateriasParaAgregacion.size() > 0  ) {

			Boolean seleccionMaterias = false;
			MateriaDto paraleloNoSeleccionado = null;

			for (MateriaDto it : amfListMateriasParaAgregacion) {
				if (it.getMtrCmbEstado()) {
					seleccionMaterias = true;
					if(it.getPrlId() == null || it.getPrlId() == GeneralesConstantes.APP_ID_BASE){
						paraleloNoSeleccionado = it;
					}
				}
			}

			if(!seleccionMaterias){
				if(!mensaje){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.seleccion.seleccione.asignaturas")));
				} 
				return  false;
			}

			if(paraleloNoSeleccionado != null){
				if(!mensaje){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.seleccion.seleccione.paralelo", paraleloNoSeleccionado.getMtrDescripcion())));
				}
				return  false;
			}

		}else{

			verificar = false;
		} 

		return verificar;
	}
	
	/**
	 * Calcula el valor a pagar por materria
	 * @param materia .- Materia a calcular
	 **/
	public BigDecimal calcularValorMateria(MateriaDto materia){
		BigDecimal valor = BigDecimal.ZERO;

		if (amfTipoUsuario.equals(RolConstantes.ROL_SECRECARRERA_VALUE)) {

			Arancel arancelXCobrar = new Arancel();

			try {

				Integer tipoArancel = null;
				if(materia.getMtrHorasCien() != null && materia.getMtrHorasCien() != 0){
					tipoArancel = ArancelConstantes.TIPO_ARANCEL_REDISENO_VALUE;
				}else{
					tipoArancel = ArancelConstantes.TIPO_ARANCEL_DISENO_VALUE;
				}
				List<Arancel> aranceles = servArancel.listarXGratuidadXTipoMatriculaXModalidadXTipoArancel(calcularGratuidad().intValue(), fgmpgfProcesoFlujo.getPrlfId(), amfFichaInscripcionDto.getCncrModalidad(), tipoArancel);
				List<Arancel>  fgmpgfListArancel = new ArrayList<>();
				if(aranceles != null && aranceles.size() > 0 ){
					fgmpgfListArancel.addAll(aranceles);
				}

				switch (materia.getNumMatricula()) {
				case 1:

					for(Arancel item: aranceles){
						if(item.getArnTipoNumMatricula() == materia.getNumMatricula()){
							arancelXCobrar = item;
						}
					}
					//Carreras Rediseño
					if(materia.getMtrHorasCien() != null && materia.getMtrHorasCien() != 0){
						valor = arancelXCobrar.getArnValor().multiply(new BigDecimal(materia.getMtrHorasCien()));
						//Carreras Actuales
					}else{
						valor = arancelXCobrar.getArnValor().multiply(new BigDecimal(materia.getMtrCreditos()));
					} 

					break;
				case 2:
					for(Arancel item: aranceles){
						if(item.getArnTipoNumMatricula() == materia.getNumMatricula()){
							arancelXCobrar = item;
						}
					}
					//Carreras Rediseño
					if(materia.getMtrHorasCien() != null && materia.getMtrHorasCien() != 0){
						valor = arancelXCobrar.getArnValor().multiply(new BigDecimal(materia.getMtrHorasCien()));
						//Carreras Actuales
					}else{
						valor = arancelXCobrar.getArnValor().multiply(new BigDecimal(materia.getMtrCreditos()));
					}

					break;
				case 3:
					for(Arancel item: aranceles){
						if(item.getArnTipoNumMatricula() == materia.getNumMatricula()){
							arancelXCobrar = item;
						}
					}
					//Carreras Rediseño
					if(materia.getMtrHorasCien() != null && materia.getMtrHorasCien() != 0){
						valor = arancelXCobrar.getArnValor().multiply(new BigDecimal(materia.getMtrHorasCien()));
						//Carreras Actuales
					}else{
						valor = arancelXCobrar.getArnValor().multiply(new BigDecimal(materia.getMtrCreditos()));
					}

					break;

				default:
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.calcular.valor.materia.num.matricula")));
					break;
				}


			} catch (ArancelNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (ArancelException e) {
				FacesUtil.mensajeError(e.getMessage());
			}  
		}

		return valor;
	}
	
	public Integer calcularGratuidad(){
		Integer retorno = null;

		if(!fgmpgfPerdidaGratuidadDefinitiva){
			if(!fgmpgfPerdidaGratuidadParcial){
				retorno = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_VALUE;
			}else{
				retorno = GratuidadConstantes.GRATUIDAD_PERDIDA_TEMPORAL_VALUE;
			}
		}else{
			retorno = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_VALUE;
		}

		return retorno;
	}
	
	/**
	 * Método que guarda una nueva materia y agrega a la ficha matricula del estudiante
	 * @return retorna la navegacion a la vista de las materias matriculadas del estudiante
	 */
	public String guardarNuevo(){
		
		String retorno = null;
		
		if(verificarMatriculaRegistrada(amfFichaInscripcionDto)){
			
				if(verificarSeleccion(false)){
				
					Boolean verificarCupos = false;
					Nivel nivel = null;

					try {

						fgmpgfPerdidaGratuidadDefinitiva = calculoPerdidaGratuidad(new ArrayList<>(), amfFichaInscripcionDto);
						calcularNivelAsignado();
						informacionSeleccion();
						 
						List<MateriaDto> materiasMatricula = new ArrayList<>();
						
						for(MateriaDto item: amfListMateriasParaAgregacion){
							if(item.getMtrCmbEstado()){
								item.setPrlDescripcion(servParalelo.buscarPorId(item.getPrlId()).getPrlDescripcion());
								item.setValorMatricula(calcularValorMateria(item));
								
								materiasMatricula.add(item);
									if(!verificarCupos(item.getMlcrmtId(), item.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), true)){
										if(!verificarCupos){
											verificarCupos = true;
										}
									}
								}
							
						}
						
						for(MateriaDto item: fgmpgfListMateriasInscrito){
							if(item.getMtrCmbEstado()){
								item.setPrlDescripcion(servParalelo.buscarPorId(item.getPrlId()).getPrlDescripcion());
								item.setValorMatricula(calcularValorMateria(item));
								materiasMatricula.add(item);
							}
						}

						if(!verificarCupos ){
							
							List<MateriaDto> amfListMateriasParaAgregar = new ArrayList<>();
							amfListMateriasParaAgregar.addAll(materiasMatricula);
							
							nivel = servNivel.listarNivelXNumeral(fgmpgfNivelUbicacion);

							int fcmtId = 0;
							int cmpaId = 0;
							int fcesId = 0;
							
							for (FichaMatriculaDto itemMateriaMatricula : amfListMatriculaSeleccionada) {
								fcmtId = itemMateriaMatricula.getFcmtId();
								cmpaId = itemMateriaMatricula.getCmpaId();
								fcesId = itemMateriaMatricula.getFcesId();
								break;
							}

							
							if(fcmtId != 0 && cmpaId != 0 && fcesId != 0){ 
								
								BigDecimal valorTotal = calcularValorTotal(materiasMatricula);
								Integer tipoGratidadId = calcularGratuidad();
								Integer estadoMatricula = null;
								
								
								if(valorTotal != null && valorTotal.equals(BigDecimal.ZERO)){
									estadoMatricula = RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE;
								}else if(valorTotal != null && !valorTotal.equals(BigDecimal.ZERO)){
									estadoMatricula = RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE;
								}else if(valorTotal == null){
									bloqueaModal();
									FacesUtil.mensajeError("Ocurrió al calcular el valor de la matrícula. Intente nuevamente.");
									return null;
								}
								
								Gratuidad gratuidad = servGratuidad.buscarGratuidadXFichaMatricula(fcmtId);
								
								if(servMatriculaServicio.agregarMateriasMatriculaFull(fcmtId, cmpaId, fcesId, estadoMatricula, amfListMateriasParaAgregar, nivel, valorTotal, gratuidad, tipoGratidadId, amfUsuario)){

									amfListMatriculaSeleccionada = servJdbcFichaMatriculaDto.ListarXidPersonaXcarrera(amfEstudianteJdbcDto.getPrsId(), amfEstudianteJdbcDto.getCrrId(), amfEstudianteJdbcDto.getPracId());
									fgmpgfListMateriasInscrito = servJdbcMateriaDto.buscarRecordEstudianteAModificarXidentificacionXpracActivoXprcacFull(amfFichaInscripcionDto.getPrsIdentificacion(), amfPeriodoAcademicoId , amfFichaInscripcionDto.getCrrId());
					
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeInfo("Se ha agregado la/las asignaturas correctamente.");
									
									calcularNivelAsignado();
									informacionSeleccion();
									cargarMateriasParaAgregacion(amfListHistorialAcademico , amfListMatriculaSeleccionada , amfFichaInscripcionDto);

									retorno = "irVerMateriaMatricula";
									
								}else{
									FacesUtil.mensajeError("Ocurrió un error al guardar. Intente nuevamente.");
									bloqueaModal();
									return null;	
								}
							}else{
								FacesUtil.mensajeError("Ocorrió un error con los parametros de matrícula. Intente nuevamente.");
								bloqueaModal();
								return null;	
							}
							
						}else{
							bloqueaModal();
							return null;
						}

						bloqueaModal();
					}catch (ParaleloNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
						bloqueaModal();
						return null; 
					} catch (ParaleloException e) {
						FacesUtil.mensajeError(e.getMessage());
						bloqueaModal();
						return null; 
					} catch (NivelNoEncontradoException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null; 
					} catch (NivelException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null; 
					} catch (MatriculaException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null; 
					} catch (FichaMatriculaException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null;
					} catch (GratuidadNoEncontradoException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null;
					} catch (GratuidadException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null;
					} catch (MateriaDtoException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						bloqueaModal();
						return null;
					}
				}else{
					bloqueaModal();
					return null;
				}		
		}else{
			bloqueaModal();
			return null; 
		}
		
		
		return retorno;

	}
	
	/**
	 * Calcula el valor total a pagar
	 * @param materias .- Lista de materias a calcular
	 **/
	public BigDecimal calcularValorTotal(List<MateriaDto> materias){
		BigDecimal valor = BigDecimal.ZERO;
		for(MateriaDto item: materias){
			if(item.getMtrCmbEstado()){
				valor = valor.add(item.getValorMatricula());
			}
		}
 
		return valor;
	}
	
	/**
	 * Método que verifica todas las validaciones para que pueda eliminar la matricula de la materia del estudiante
	 * @param materia - entidad entidad de tipo ficha matricula dto a eliminar
	 */
	public void verificarClickEliminarMateriaMatricula(FichaMatriculaDto materia){

		if (verificarCronogramaAgregacionEliminacion()) {
			
			if (amfListMatriculaSeleccionada.size() > 1) {

				if (materia.getRcesEstadoValue().equals(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE) || materia.getRcesEstadoValue().equals(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE)){

					Boolean isOrdenCobroCancelada = true;
					for(MateriaDto item: fgmpgfListMateriasInscrito){
						if(!item.getValorMatricula().equals(BigDecimal.ZERO) && item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE.intValue()){
							isOrdenCobroCancelada = false;
						}
					}

					if(!isOrdenCobroCancelada){ 
						FacesUtil.mensajeError("Orden de cobro cancelada. No se puede agregar/eliminar asignaturas de la matrícula.");
						bloqueaModal();
					}else{
						amfListMateriaDtoCorrequisitos = new ArrayList<>();

						List<MateriaDto> correquisitos = verificarSiTieneCorrequisitos(materia);
						if (correquisitos.isEmpty()) {
							FacesUtil.limpiarMensaje();
							amfMateriaParaModificacion = materia;
							habilitaModalEliminarMateriaMatricula();
						}else {
							if (verificarSiCorrequitosCorrectos(correquisitos)) {
								if ((correquisitos.size() + 1) < amfListMatriculaSeleccionada.size()) {
									amfMateriaParaModificacion = materia;
									amfListMateriaDtoCorrequisitos = correquisitos;
									habilitaModalEliminarMateriaMatricula();
								}else {
									FacesUtil.mensajeError("No es posible eliminar la asignatura debido a que tiene correquisitos y se eliminaría toda la matrícula.");
									bloqueaModal();
								}
							}else {
								FacesUtil.mensajeError("Verifique en la malla curricular que los correquisitos esten registrados en las " + (correquisitos.size()+1)+ " asignaturas.");
								bloqueaModal();
							}
						}
					}

				}else {
					materia.setRcesEstadoLabel(etiquetarEstadoRecordEstudiantil(materia.getRcesEstadoValue()));
					FacesUtil.mensajeInfo("No es posible eliminar la asignatura " + materia.getMtrDescripcion() + " por que se encuentra en estado " + materia.getRcesEstadoLabel()+".");
				}

			}else {
				FacesUtil.mensajeError("No puede eliminar las asignaturas en su totalidad.");
				bloqueaModal();
			}

		}

	}
	
	private boolean verificarSiCorrequitosCorrectos(List<MateriaDto> correquisitos) {
		boolean retorno = true;

		for (MateriaDto item : correquisitos) {
			if (retorno) {

				FichaMatriculaDto ficha = new FichaMatriculaDto();
				ficha.setMtrId(item.getCrqMtrCorequisitoId());

				List<MateriaDto> materias  = verificarSiTieneCorrequisitos(ficha);
				if (!materias.isEmpty()) {
					if (materias.size()!=correquisitos.size()) {
						retorno = false;
					}
				}else {
					retorno = false;
				}

			}
		}


		return retorno;
	}


	private List<MateriaDto> verificarSiTieneCorrequisitos(FichaMatriculaDto materia) {
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcCorrequisitoDto.listarXidMateriaFull(materia.getMtrId());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	/**
	 * Método que elimina la materia matricula del estudiante
	 */
	public void eliminarMateriaMatricula(){
		
		List<FichaMatriculaDto> matriculaFinal = new ArrayList<>();

		if (amfListMateriaDtoCorrequisitos !=null && !amfListMateriaDtoCorrequisitos.isEmpty()) {

			Iterator<MateriaDto> correquisitos = amfListMateriaDtoCorrequisitos.iterator();
			while (correquisitos.hasNext()) {
				MateriaDto itemCorrequisito = (MateriaDto) correquisitos.next();

				Iterator<MateriaDto> matriculas = fgmpgfListMateriasInscrito.iterator();
				while (matriculas.hasNext()) {
					MateriaDto itemMatricula = (MateriaDto) matriculas.next();
					if(itemMatricula.getMtrId() == itemCorrequisito.getCrqMtrCorequisitoId() || itemMatricula.getMtrId() == amfMateriaParaModificacion.getMtrId()){ 
						matriculas.remove();
					}
				}

			}

			calcularNivelAsignado();
			informacionSeleccion();
			
			for(MateriaDto item: fgmpgfListMateriasInscrito){
				item.setValorMatricula(calcularValorMateria(item));
			}
			
			for (FichaMatriculaDto matricula : amfListMatriculaSeleccionada) {
				for (MateriaDto correquisito : amfListMateriaDtoCorrequisitos) {

					if(matricula.getMtrId() == correquisito.getCrqMtrCorequisitoId()){ 
						if (!eliminarAsignaturaDeLaMatricula(
								matricula.getDtmtId(), 
								matricula.getCmpaId(), 
								matricula.getRcesId(), 
								matricula.getMlcrprId(),
								matricula.getFcmtId(),
								fgmpgfListMateriasInscrito
								)) {
							FacesUtil.mensajeError("No se ha eliminado la materia "+matricula.getMtrDescripcion()+".");
						}

					}

				}
			}
			
			if (!eliminarAsignaturaDeLaMatricula(
					amfMateriaParaModificacion.getDtmtId(), 
					amfMateriaParaModificacion.getCmpaId(), 
					amfMateriaParaModificacion.getRcesId(), 
					amfMateriaParaModificacion.getMlcrprId(),
					amfMateriaParaModificacion.getFcmtId(),
					fgmpgfListMateriasInscrito
					)) {
				FacesUtil.mensajeError("No se ha eliminado la materia "+amfMateriaParaModificacion.getMtrDescripcion()+".");
			}

			matriculaFinal = cargarRegistrosMatricula(amfEstudianteJdbcDto.getPrsId(), amfEstudianteJdbcDto.getCrrId(), amfEstudianteJdbcDto.getPracId());
			if (!matriculaFinal.isEmpty()) {
				FacesUtil.mensajeInfo("Se ha realizado la eliminación de la asignatura con sus correquisitos.");
				bloqueaModal();
			}
		}else {
			Iterator<MateriaDto> iter = fgmpgfListMateriasInscrito.iterator();
			while (iter.hasNext()) {
				MateriaDto item = (MateriaDto) iter.next();
				if(item.getDtmtId() == amfMateriaParaModificacion.getDtmtId() && amfMateriaParaModificacion.getRcesId() == item.getRcesId()){ 
					iter.remove();
				}
			}

			calcularNivelAsignado();
			informacionSeleccion();
			for(MateriaDto item: fgmpgfListMateriasInscrito){
				item.setValorMatricula(calcularValorMateria(item));
			}

			if (!eliminarAsignaturaDeLaMatricula(
					amfMateriaParaModificacion.getDtmtId(), 
					amfMateriaParaModificacion.getCmpaId(), 
					amfMateriaParaModificacion.getRcesId(), 
					amfMateriaParaModificacion.getMlcrprId(),
					amfMateriaParaModificacion.getFcmtId(),
					fgmpgfListMateriasInscrito
					)) {
				FacesUtil.mensajeError("No se ha eliminado la asignatura "+amfMateriaParaModificacion.getMtrDescripcion()+".");
			}

			matriculaFinal = cargarRegistrosMatricula(amfEstudianteJdbcDto.getPrsId(), amfEstudianteJdbcDto.getCrrId(), amfEstudianteJdbcDto.getPracId());
			if (!matriculaFinal.isEmpty()) {
				amfListMatriculaSeleccionada = matriculaFinal;
				FacesUtil.mensajeInfo("Se ha eliminado la asignatura "+amfMateriaParaModificacion.getMtrDescripcion()+" correctamente.");
				bloqueaModal();
			}
		}
		
	
		try {
			fgmpgfListMateriasInscrito = servJdbcMateriaDto.buscarRecordEstudianteAModificarXidentificacionXpracActivoXprcacFull(amfFichaInscripcionDto.getPrsIdentificacion(), amfPeriodoAcademicoId , amfFichaInscripcionDto.getCrrId());
			amfListMatriculaSeleccionada = matriculaFinal;
			calcularNivelAsignado();
			informacionSeleccion();
			cargarMateriasParaAgregacion(amfListHistorialAcademico , amfListMatriculaSeleccionada , amfFichaInscripcionDto);
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		

	}


	private boolean eliminarAsignaturaDeLaMatricula(int dtmtId, int cmpaId, int rcesId, int mlcrprId, int fcmtId, List<MateriaDto> matriculaFinal) {
		boolean retorno = false;

		BigDecimal valorTotal = calcularValorTotal(matriculaFinal);
		Integer tipoGratidadId = calcularGratuidad();

		try {
			servDetalleMatricula.eliminarFull(
					dtmtId, 
					cmpaId, 
					rcesId, 
					mlcrprId, 
					matriculaFinal, 
					servGratuidad.buscarGratuidadXFichaMatricula(fcmtId), 
					tipoGratidadId, 
					asignarEstadoGeneralMatricula(valorTotal), 
					fcmtId, 
					cargarNivelPorUbicacion(fgmpgfNivelUbicacion));
			retorno = true;
		} catch (DetalleMatriculaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetalleMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GratuidadNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GratuidadException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}



	private Integer asignarEstadoGeneralMatricula(BigDecimal valorTotal) {
		Integer retorno = null;

		if(valorTotal != null){
			if(valorTotal.equals(BigDecimal.ZERO)){
				retorno = RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE;
			}else{
				retorno = RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE;
			}
		}else {
			retorno = GeneralesConstantes.APP_ID_BASE;
			FacesUtil.mensajeError("Error al calcular el valor de la matrícula. Comuníquese con el administrador del sistema.");
		}
		
		return retorno;
	}


	private Nivel cargarNivelPorUbicacion(int nivelUbicacion) {
		Nivel nivel = null;
		
		try {
			nivel = servNivel.listarNivelXNumeral(nivelUbicacion);
		} catch (NivelNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return nivel;
	}


	/**
	 * Método que dirige a la visualizacion de las materias matriculadas
	 * @param estudiante - entidad estudiante a ser consultado
	 * @return retorna a la navegación a la pagina de ver matetrias matricula
	 */
	public String irVerMatriculaEstudiante(EstudianteJdbcDto estudiante){
		String retorno = null;

		FichaInscripcionDto fichaInscripcion = verificarFichaEstudiante(estudiante);
		if(fichaInscripcion != null){

			if(verificarMatriculaRegistrada(fichaInscripcion)){

				List<FichaMatriculaDto> matricula = cargarRegistrosMatricula(estudiante.getPrsId(), estudiante.getCrrId(), estudiante.getPracId());
				if (!matricula.isEmpty()) {
					
					try{
						amfEstudianteJdbcDto = estudiante;
						amfFichaInscripcionDto = fichaInscripcion;
						amfListMatriculaSeleccionada = matricula;
						
						fgmpgfListMateriasInscrito = servJdbcMateriaDto.buscarRecordEstudianteAModificarXidentificacionXpracActivoXprcacFull(fichaInscripcion.getPrsIdentificacion(), amfPeriodoAcademicoId , fichaInscripcion.getCrrId());
						fgmpgfPeriodoAcademico = servPeriodoAcademico.buscarPorId(amfPeriodoAcademicoId);
						fgmpgfProcesoFlujo = servJdbcCronogramaActividadDto.buscarCronogramaPorPlanificacionCronograma(estudiante.getFcmtPlcrId());
						fgmpgfCarreraSeleccion = servCarrera.buscarPorId(estudiante.getCrrId());
						fgmpgfDependenciaBuscar = servDependenciaServicio.buscarPorId(fgmpgfCarreraSeleccion.getCrrDependencia().getDpnId());
						fgmpgfTipoGratuidadParcial = servTipoGratuidadServicio.buscarPorId(TipoGratuidadConstantes.PERDIDA_TEMPORAL_VALUE);
						fgmpgfTipoGratuidadDefinitiva = servTipoGratuidadServicio.buscarPorId(TipoGratuidadConstantes.PERDIDA_DEFINITIVA_VALUE); 
						fgmpgfTipoGratuidadGratuidad = servTipoGratuidadServicio.buscarPorId(TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
						fgmpgfMallaMaterias = servJdbcMateriaDto.listarMateriasxCarreraFull(estudiante.getCrrId(), new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
						fgmpgfCarreraSeleccion.setCrrEspeCodigo(fgmpgfCarreraSeleccion.getCrrEspeCodigo() == null ? GeneralesConstantes.APP_ID_BASE:fgmpgfCarreraSeleccion.getCrrEspeCodigo());

						amfListHistorialAcademico = cargarHistorialAcademicoSAIUHomologado(amfEstudianteJdbcDto.getPrsIdentificacion());
						cargarMateriasParaAgregacion(amfListHistorialAcademico, matricula , fichaInscripcion);
						calcularNivelAsignado();
						
						if (fgmpgfCarreraSeleccion.getCrrTipo().equals(CarreraConstantes.TIPO_PREGRADO_VALUE)) {
							informacionSeleccion();	
						}
						
						if(fgmpgfNivelUbicacion != null){
							retorno = "irVerMatricula";
						}
						

					} catch (CarreraNoEncontradoException e) {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.carrera.no.encontrado.exception")));
					} catch (CarreraException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (MateriaDtoException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (DependenciaNoEncontradoException e) {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.dependencia.no.encontrado.exception")));
					} catch (DependenciaException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (MateriaDtoNoEncontradoException e) {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.materia.dto.no.encontrado.exception")));
					} catch (TipoGratuidadNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (TipoGratuidadException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (CronogramaActividadDtoJdbcException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (PeriodoAcademicoNoEncontradoException e) {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.periodo.academico.no.encontrado.exception")));
					} catch (PeriodoAcademicoException e) {
						FacesUtil.mensajeError(e.getMessage());
					}

				}
			}
		}

		return retorno;
	}
	


	private void cargarMateriasParaAgregacion(List<RecordEstudianteDto> historialAcademico, List<FichaMatriculaDto> matricula, FichaInscripcionDto fichaInscripcion) {

		List<MateriaDto> materiasParaAgregacion = new ArrayList<>(); 

		List<RecordEstudianteDto> historialPorCarrera = new ArrayList<>();
				
		if (!historialAcademico.isEmpty()) {
			
			historialPorCarrera = filtrarHistorialAcademicoPorCarrera(historialAcademico, amfEstudianteJdbcDto.getCrrId());
			
			materiasParaAgregacion = quitarMateriasDeLaMallaCurricular(matricula, historialPorCarrera);
			
			List<RecordEstudianteDto> materiasReprobadas = cargarMateriasReprobadas(historialPorCarrera);
			if (!materiasReprobadas.isEmpty()) {
				List<MateriaDto> reprobadas = new ArrayList<>();
				for (RecordEstudianteDto item : materiasReprobadas) {
					reprobadas.add(item.getRcesMateriaDto());
				}
				fgmpgfPerdidaGratuidadDefinitiva = calculoPerdidaGratuidad(reprobadas, fichaInscripcion);	
			}else {
				fgmpgfPerdidaGratuidadDefinitiva = calculoPerdidaGratuidad(new ArrayList<>(), fichaInscripcion);
			}

		}else {
			materiasParaAgregacion = quitarMateriasDeLaMallaCurricular(matricula, historialPorCarrera);
			fgmpgfPerdidaGratuidadDefinitiva = calculoPerdidaGratuidad(new ArrayList<>(), fichaInscripcion);
		}
		
		if (!materiasParaAgregacion.isEmpty()) {
			materiasParaAgregacion.sort(Comparator.comparing(MateriaDto::getNvlNumeral));	
			amfListMateriasParaAgregacion = materiasParaAgregacion;
		}else {
			amfListMateriasParaAgregacion = new ArrayList<>();
		}
		
	}


	private FichaInscripcionDto verificarFichaEstudiante(EstudianteJdbcDto estudiante) {
		FichaInscripcionDto retorno = null;
		
		List<FichaInscripcionDto> fichasInscripcion = new ArrayList<>();
		try {
			fichasInscripcion = servJdbcFichaInscripcionDto.buscarFichaInscripcionPorCarrera(estudiante.getPrsIdentificacion(), estudiante.getCrrId());
			if (!fichasInscripcion.isEmpty() && fichasInscripcion.size() == 1) {
				retorno =  fichasInscripcion.get(fichasInscripcion.size()-1);
			}else {
				FacesUtil.mensajeError("Error al buscar la ficha inscripción del estudiante con identificación: "+estudiante.getPrsIdentificacion()+".");
			}
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	private List<FichaMatriculaDto> cargarRegistrosMatricula(int prsId, int crrId, int pracId) {
		List<FichaMatriculaDto>  retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcFichaMatriculaDto.ListarXidPersonaXcarrera(prsId, crrId, pracId);
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	

	/**
	 * Método que dirige a la página de ver el registro de materias matriculadas del estudiante
	 * @return retorna a la página de ver el registro de materias matriculadas del estudiante
	 */
	public String irVerEstudiante(){
		bloqueaModal();
		return  "irVerMateriaMatricula";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}
	
	
	
	

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	public void buscarCarreras(){
		if (!amfPeriodoAcademicoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			limpiarCombosCarrera();
		}else {
			limpiarCombosCarrera();
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	private void limpiarCombosCarrera(){
		amfCarreraId = GeneralesConstantes.APP_ID_BASE;
		amfNivelId = GeneralesConstantes.APP_ID_BASE;
		amfIdentificacion = new String();
		amfListEstudiantesMatriculados = null;
	}
	
	public void buscarNiveles(){
		if (!amfPeriodoAcademicoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!amfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
				limpiarCombosNivel();
				try {
					amfListNivelDto = servJdbcNivelDto.listarNivelXCarrera(amfCarreraId);
					FacesUtil.mensajeInfo("Ingrese la Identificación del Estudiante o seleccione un Nivel, luego haga clic en Buscar.");
				} catch (NivelDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (NivelDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
			}else {
				limpiarCombosNivel();
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			limpiarCombosCarrera();
			FacesUtil.mensajeError("Seleccione un Período Académico para continuar.");
		}
	}
	
	private void limpiarCombosNivel(){
		amfNivelId = GeneralesConstantes.APP_ID_BASE;
		amfIdentificacion = new String();
		amfListNivelDto = null;
		amfListEstudiantesMatriculados = null;
	}
	
	
	
	public void buscarEstudiantesMatriculados(){
		if (!amfPeriodoAcademicoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!amfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
				try {
					fgmpgfListMateriasInscrito = new ArrayList<>();
					amfListEstudiantesMatriculados = servJdbcEstudianteDto.buscarXPeriodocarreraNivelMatriculadoLista(amfPeriodoAcademicoId, amfCarreraId, amfNivelId, amfIdentificacion);
				} catch (EstudianteDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (EstudianteDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError("No se encontró resultados con los parámetros ingresados.");
				}
			}else {
				limpiarCombosNivel();
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			limpiarCombosCarrera();
			FacesUtil.mensajeError("Seleccione un Período Académico para continuar.");
		}
	}
	
	
	
	public void establecerBusquedaPorNivel(){
		if (!amfPeriodoAcademicoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!amfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!amfNivelId.equals(GeneralesConstantes.APP_ID_BASE)) {
					amfIdentificacion = new String();
					amfListEstudiantesMatriculados = null;	
				}else {
					amfIdentificacion = new String();
					amfListEstudiantesMatriculados = null;	
					FacesUtil.mensajeError("Seleccione un Nivel o ingrese la Identificación del Estudiante y haga clic en Buscar para continuar.");
				}
			}else {
				limpiarCombosNivel();
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			limpiarCombosCarrera();
			FacesUtil.mensajeError("Seleccione un Período Académico para continuar.");
		}
	}
	
	public void establecerBusquedaPorIdentificacion(){
		
		if (GeneralesUtilidades.quitarEspaciosEnBlanco(amfIdentificacion).length() > 0) {
			amfNivelId = GeneralesConstantes.APP_ID_BASE;
			amfListEstudiantesMatriculados = null;
		}
		
	}

	
	private void asignarTipoDeSuficiencia(List<Carrera> carreras) {

		if (!carreras.isEmpty()) {
			for (Carrera it : carreras) {
				if (it.getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					amfTipoUsuario = DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE;
					break;
				}else if (it.getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
					amfTipoUsuario = DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE;
					break;
				}else if (it.getCrrDependencia().getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
					amfTipoUsuario =  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE;
					break;
				}
			}
		}

	}
	
	public String etiquetarEstadoRecordEstudiantil(int rcesEstado) {
		String retorno = "";
		if(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_APROBADO_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_APROBADO_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE.equals(rcesEstado)){
			retorno = RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_LABEL;
		}else if (RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE.equals(rcesEstado)){
			retorno =  RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL;
		}else{
			retorno = SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL;			
		}

		return retorno;
	}
	// ********************************************************************/
	// *********************** ENCAPSULAMIENTO   **************************/
	// ********************************************************************/	
	
	public Usuario getAmfUsuario() {
		return amfUsuario;
	}
	public void setAmfUsuario(Usuario amfUsuario) {
		this.amfUsuario = amfUsuario;
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
	public UsuarioRolServicio getServAmfUsuarioRolServicio() {
		return servUsuarioRol;
	}
	public void setServAmfUsuarioRolServicio(UsuarioRolServicio servAmfUsuarioRolServicio) {
		this.servUsuarioRol = servAmfUsuarioRolServicio;
	}
	public boolean isAmfPanelInformacion() {
		return amfPanelInformacion;
	}
	public void setAmfPanelInformacion(boolean amfPanelInformacion) {
		this.amfPanelInformacion = amfPanelInformacion;
	}

	public MallaCurricularParaleloDto getAmfMallaCurricularParaleloEdit() {
		return amfMallaCurricularParaleloEdit;
	}
	public void setAmfMallaCurricularParaleloEdit(MallaCurricularParaleloDto amfMallaCurricularParaleloEdit) {
		this.amfMallaCurricularParaleloEdit = amfMallaCurricularParaleloEdit;
	}
	public int getAmfValidadorClic() {
		return amfValidadorClic;
	}
	public void setAmfValidadorClic(int amfValidadorClic) {
		this.amfValidadorClic = amfValidadorClic;
	}
	public boolean isAmfHabilitaTotal() {
		return amfHabilitaTotal;
	}
	public void setAmfHabilitaTotal(boolean amfHabilitaTotal) {
		this.amfHabilitaTotal = amfHabilitaTotal;
	}


	public Boolean getFgmpgfPerdidaGratuidadParcial() {
		return fgmpgfPerdidaGratuidadParcial;
	}

	public void setFgmpgfPerdidaGratuidadParcial(Boolean fgmpgfPerdidaGratuidadParcial) {
		this.fgmpgfPerdidaGratuidadParcial = fgmpgfPerdidaGratuidadParcial;
	}

	public Boolean getFgmpgfPerdidaGratuidadDefinitiva() {
		return fgmpgfPerdidaGratuidadDefinitiva;
	}

	public void setFgmpgfPerdidaGratuidadDefinitiva(Boolean fgmpgfPerdidaGratuidadDefinitiva) {
		this.fgmpgfPerdidaGratuidadDefinitiva = fgmpgfPerdidaGratuidadDefinitiva;
	}



	public List<PeriodoAcademicoDto> getAmfListPeriodoAcademicoDto() {
		return amfListPeriodoAcademicoDto;
	}



	public void setAmfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> amfListPeriodoAcademicoDto) {
		this.amfListPeriodoAcademicoDto = amfListPeriodoAcademicoDto;
	}



	public Integer getAmfPeriodoAcademicoId() {
		return amfPeriodoAcademicoId;
	}



	public void setAmfPeriodoAcademicoId(Integer amfPeriodoAcademicoId) {
		this.amfPeriodoAcademicoId = amfPeriodoAcademicoId;
	}



	public Integer getAmfCarreraId() {
		return amfCarreraId;
	}



	public void setAmfCarreraId(Integer amfCarreraId) {
		this.amfCarreraId = amfCarreraId;
	}



	public Integer getAmfNivelId() {
		return amfNivelId;
	}



	public void setAmfNivelId(Integer amfNivelId) {
		this.amfNivelId = amfNivelId;
	}


	public String getAmfIdentificacion() {
		return amfIdentificacion;
	}



	public void setAmfIdentificacion(String amfIdentificacion) {
		this.amfIdentificacion = amfIdentificacion;
	}



	public List<NivelDto> getAmfListNivelDto() {
		return amfListNivelDto;
	}



	public void setAmfListNivelDto(List<NivelDto> amfListNivelDto) {
		this.amfListNivelDto = amfListNivelDto;
	}


	public List<Carrera> getAmfListCarrera() {
		return amfListCarrera;
	}



	public void setAmfListCarrera(List<Carrera> amfListCarrera) {
		this.amfListCarrera = amfListCarrera;
	}


	public EstudianteJdbcDto getAmfEstudianteJdbcDto() {
		return amfEstudianteJdbcDto;
	}


	public void setAmfEstudianteJdbcDto(EstudianteJdbcDto amfEstudianteJdbcDto) {
		this.amfEstudianteJdbcDto = amfEstudianteJdbcDto;
	}


	public Dependencia getFgmpgfDependenciaBuscar() {
		return fgmpgfDependenciaBuscar;
	}


	public void setFgmpgfDependenciaBuscar(Dependencia fgmpgfDependenciaBuscar) {
		this.fgmpgfDependenciaBuscar = fgmpgfDependenciaBuscar;
	}


	public boolean isAmfDisableParaleloNuevo() {
		return amfDisableParaleloNuevo;
	}


	public void setAmfDisableParaleloNuevo(boolean amfDisableParaleloNuevo) {
		this.amfDisableParaleloNuevo = amfDisableParaleloNuevo;
	}



	public List<MateriaDto> getAmfListMateriaDtoCorrequisitos() {
		return amfListMateriaDtoCorrequisitos;
	}


	public void setAmfListMateriaDtoCorrequisitos(List<MateriaDto> amfListMateriaDtoCorrequisitos) {
		this.amfListMateriaDtoCorrequisitos = amfListMateriaDtoCorrequisitos;
	}


	public List<EstudianteJdbcDto> getAmfListEstudiantesMatriculados() {
		return amfListEstudiantesMatriculados;
	}


	public void setAmfListEstudiantesMatriculados(List<EstudianteJdbcDto> amfListEstudiantesMatriculados) {
		this.amfListEstudiantesMatriculados = amfListEstudiantesMatriculados;
	}


	public List<FichaMatriculaDto> getAmfListMatriculaSeleccionada() {
		return amfListMatriculaSeleccionada;
	}


	public void setAmfListMatriculaSeleccionada(List<FichaMatriculaDto> amfListMatriculaSeleccionada) {
		this.amfListMatriculaSeleccionada = amfListMatriculaSeleccionada;
	}


	public List<RecordEstudianteDto> getAmfListHistorialAcademico() {
		return amfListHistorialAcademico;
	}


	public void setAmfListHistorialAcademico(List<RecordEstudianteDto> amfListHistorialAcademico) {
		this.amfListHistorialAcademico = amfListHistorialAcademico;
	}


	public List<ParaleloDto> getAmfListParalelosParaCambio() {
		return amfListParalelosParaCambio;
	}


	public void setAmfListParalelosParaCambio(List<ParaleloDto> amfListParalelosParaCambio) {
		this.amfListParalelosParaCambio = amfListParalelosParaCambio;
	}


	public List<MateriaDto> getAmfListMateriasParaAgregacion() {
		return amfListMateriasParaAgregacion;
	}


	public void setAmfListMateriasParaAgregacion(List<MateriaDto> amfListMateriasParaAgregacion) {
		this.amfListMateriasParaAgregacion = amfListMateriasParaAgregacion;
	}


	public FichaMatriculaDto getAmfMateriaParaModificacion() {
		return amfMateriaParaModificacion;
	}


	public void setAmfMateriaParaModificacion(FichaMatriculaDto amfMateriaParaModificacion) {
		this.amfMateriaParaModificacion = amfMateriaParaModificacion;
	}


	public Integer getAmfParaleloId() {
		return amfParaleloId;
	}


	public void setAmfParaleloId(Integer amfParaleloId) {
		this.amfParaleloId = amfParaleloId;
	}

	
	
	
	

	
}