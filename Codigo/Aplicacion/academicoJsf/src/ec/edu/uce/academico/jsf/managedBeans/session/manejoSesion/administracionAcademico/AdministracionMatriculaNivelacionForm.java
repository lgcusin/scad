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
   
 ARCHIVO:     AdministracionMatriculaNivelacionForm.java	  
 DESCRIPCION: Managed Bean que maneja las
 * peticiones para la administración de la Matrícula de los estudiantes de nivelación.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
02-05-2019			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ArancelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.GratuidadServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionMatriculaNivelacionForm. Managed Bean que maneja las
 * peticiones para la administración de la Matrícula de los estudiantes de nivelación.
 * 
 * @author dalbuja v1.0
 * @version 1.0
 */

@ManagedBean(name = "administracionMatriculaNivelacionForm")
@SessionScoped
public class AdministracionMatriculaNivelacionForm extends HistorialAcademicoForm implements Serializable {

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
	private boolean amfHabilitadorCambioTotal;

	private EstudianteJdbcDto amfEstudianteJdbcDto;
	private EstudianteJdbcDto amfEstudianteJdbcDtoCambio;
	private FichaMatriculaDto amfMateriaParaModificacion;
	
	private Integer amfParaleloSeleccionado;
	
	private Dependencia fgmpgfDependenciaBuscar;
	
	private Boolean fgmpgfPerdidaGratuidadParcial;
	private Boolean fgmpgfPerdidaGratuidadDefinitiva;
	
	private FichaInscripcionDto amfFichaInscripcionDto;
	
	private List<PeriodoAcademicoDto> amfListPeriodoAcademicoDto;
	private List<Carrera> amfListCarrera;
	private List<Carrera> amfListCarreraSiiu;
	private List<NivelDto> amfListNivelDto;
	
	private List<MateriaDto> fgmpgfMallaMaterias;
	
	private List<EstudianteJdbcDto> amfListEstudiantesMatriculados;
	private List<FichaMatriculaDto> amfListMatriculaSeleccionada;
	private List<MateriaDto> amfListMatriculaCambio;
	
	private List<RecordEstudianteDto> amfListHistorialAcademico;
	
	private List<ParaleloDto> amfListParalelosParaCambio;
	private List<MateriaDto> amfListMateriaDtoCorrequisitos;

	private List<MateriaDto> amfListMateriasParaAgregacion;

	private MallaCurricularParaleloDto amfMallaCurricularParaleloEdit;
	private MallaCurricular amfMallaCurricular;
	
	private int amfValidadorClic;
	private boolean amfHabilitaTotal;
	
	private int amfNumeroMatricula;
	
	
	

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
	@EJB	private MallaCurricularServicio servMallaCurricular;
	
	
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
	public String irAdministracionMatricula(Usuario usuario) {
		String retorno = null;
		
		try {

			amfUsuario = usuario;
			
			List<PeriodoAcademicoDto> periodos = new ArrayList<>();
			List<Carrera> carreras = new ArrayList<>();
			iniciarParametros();	

				amfTipoCarrera="carreras";
				periodos = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE,  PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE});
				if(amfUsuario.getUsrNick().equals("wltorres")
						||amfUsuario.getUsrNick().equals("vjcruzc")
						||amfUsuario.getUsrNick().equals("eglarac")){
					periodos.add(servJdbcPeriodoAcademicoDto.buscar(350));	
				}
				carreras = servCarrera.listarCarrerasXFacultad(DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE);
			
			if (!periodos.isEmpty()) {
				periodos.sort(Comparator.comparing(PeriodoAcademicoDto::getPracId).reversed());
				amfListPeriodoAcademicoDto = periodos;
			}

			if (!carreras.isEmpty()) {
				carreras.sort(Comparator.comparing(Carrera::getCrrDescripcion));
				amfListCarrera = carreras;
			}
			amfListCarreraSiiu = servCarrera.listarTodos();
			Iterator<Carrera> it1 = amfListCarreraSiiu.iterator();
			while (it1.hasNext()) {
				Carrera tmp = it1.next();
				if (tmp.getCrrDescripcion().contains("(R)")) {

				} else {
					it1.remove();
				}
			}
			amfListCarreraSiiu.sort(Comparator.comparing(Carrera::getCrrDescripcion));
			
			retorno = "irAdministracionMatriculaNivelacion";
			
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
	 * Método que dirige a la visualizacion de las materias matriculadas
	 * @param estudiante - entidad estudiante a ser consultado
	 * @return retorna a la navegación a la pagina de ver matetrias matricula
	 */
	public String irVerMatriculaEstudiante(EstudianteJdbcDto estudiante){
		String retorno = null;
		amfEstudianteJdbcDto = estudiante;
		amfEstudianteJdbcDtoCambio = estudiante;
		
		try {
			amfFichaInscripcionDto = servJdbcFichaInscripcionDto.buscarFichaInscripcionNivelacionXidentificacion(estudiante.getPrsIdentificacion());
			amfListMatriculaSeleccionada = servJdbcFichaMatriculaDto.ListarXidPersonaXcarrera(amfEstudianteJdbcDto.getPrsId(), amfFichaInscripcionDto.getCrrId(), amfEstudianteJdbcDto.getPracId());
			Carrera crr = servCarrera.buscarPorId(amfFichaInscripcionDto.getFcinCarreraSiiu());
			amfEstudianteJdbcDto.setCrrDescripcionArea(crr.getCrrDescripcion());
			amfEstudianteJdbcDto.setFcinCarreraSiiu(amfFichaInscripcionDto.getFcinCarreraSiiu());
			amfEstudianteJdbcDto.setFcinCncrArea(amfFichaInscripcionDto.getFcinCncrArea());
			retorno = "irVerMatriculaNivelacion";
			amfListMatriculaSeleccionada.sort(Comparator.comparing(FichaMatriculaDto:: getMtrDescripcion));
			amfNumeroMatricula=1;
			for (FichaMatriculaDto item : amfListMatriculaSeleccionada) {
				if(item.getDtmtNumero()>amfNumeroMatricula) amfNumeroMatricula=item.getDtmtNumero();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return retorno;
	}
	
	public void eliminarMateria(MateriaDto item){
		
		Iterator<MateriaDto> it = amfListMatriculaCambio.iterator();
		while(it.hasNext()){
			MateriaDto mtr = it.next();
			if(mtr.getMtrId()==item.getMtrId()){
				it.remove();
			}
		}
		
	}
	
	
	public void cambioCarrera(){
		amfListCarrera = new ArrayList<Carrera>();
		amfParaleloSeleccionado=GeneralesConstantes.APP_ID_BASE;
		amfListMatriculaCambio = new ArrayList<>();
		
		try {
			amfListCarrera.add(servCarrera.buscarAreaXCarrera(amfEstudianteJdbcDtoCambio.getFcinCarreraSiiu()));
			amfEstudianteJdbcDtoCambio.setFcinCncrArea(amfListCarrera.get(0).getCrrId());
			amfHabilitadorCambioTotal=false;
			try {
				amfMallaCurricular = servMallaCurricular.buscarXcarreraXvigenciaXestado(amfEstudianteJdbcDtoCambio.getFcinCncrArea(), MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);

				//buscar las materias de nivelacion de esa malla
				fgmpgfMallaMaterias = servJdbcMateriaDto.listarXmallaXnivel(amfMallaCurricular.getMlcrId(), NivelConstantes.NIVEL_NIVELACION_VALUE);
				//lleno los paralelos de las materias
				amfListParalelosParaCambio = new ArrayList<ParaleloDto>();
//				for (MateriaDto itmMateria : fgmpgfMallaMaterias) {
//					//busco los paralelos de esa materia
				amfListParalelosParaCambio = servJdbcParaleloDto.ListarXMateriaIdNivelacion(fgmpgfMallaMaterias.get(0).getMtrId(),amfPeriodoAcademicoId,amfEstudianteJdbcDtoCambio.getFcinCncrArea() );
//					//guardo los paralelos de esa materia
//					itmMateria.setListParalelos(amfListParalelosParaCambio);
//					// seteo la variable de seleccion ya que los estudiantes nuevos cogen todas las materias obligatoriamente
//					itmMateria.setIsSeleccionado(true); 
//					amfListParalelosParaCambio.addAll(amfListParalelosParaCambio1);
//				}
//				Set<ParaleloDto> set = new HashSet<ParaleloDto>(amfListParalelosParaCambio);
//				amfListParalelosParaCambio.clear();
//				amfListParalelosParaCambio.addAll(set);
				
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen cupos en los paralelos del área y carrera seleccionadas.");
				
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen cupos en los paralelos del área y carrera seleccionadas.");
		}
	}
	
	
	public void seleccionarParalelo(){
		if(amfParaleloSeleccionado!=GeneralesConstantes.APP_ID_BASE){
			amfHabilitadorCambioTotal=true;
			try {
				amfListMatriculaCambio= servJdbcMateriaDto.buscarMateriasPorParalelo(amfParaleloSeleccionado);
				amfListMatriculaCambio.sort(Comparator.comparing(MateriaDto:: getMtrDescripcion));
			} catch (Exception e) {
			}
		}else{
			amfHabilitadorCambioTotal=false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn("Por favor seleccione el paralelo a registrar.");
		}
	}
	
	/**
	 * Método de navegación que regresa a la pagina de listar o buscar estudiatnes
	 * @return retorna a la pagina de listar o buscar estudiatnes
	 */
	public String irVerListaEstudiante(){
		amfListParalelosParaCambio = null;
		return "irVerListaEstudianteNivelacion";
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
	 * Método que inicializa parametros de búsqueda
	 */
	private void iniciarParametros(){
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
		amfListCarreraSiiu = new ArrayList<Carrera>();
		amfHabilitadorCambioTotal = false;
		amfParaleloSeleccionado=GeneralesConstantes.APP_ID_BASE;
	}
	
	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		amfValidadorClic = 0;
	}
	
	
	/**
	 * Método que dirige a la página de agregar materia matricula del estudiante
	 * @return retorno - retorna a la página de agregar materia matricula del estudiante
	 */
	public void irAgregarMateria(){
		amfValidadorClic=3;
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
		
		iniciarParametros(); 
	}
	
	
	
	public String guardarCambios(){
		 
		try {
			List<FichaEstudianteDto> registroActual = servJdbcRecordEstudianteDto.buscarRecordEstudianteDetalleMatricula(amfEstudianteJdbcDto.getPrsIdentificacion(),amfPeriodoAcademicoId);
			for (FichaEstudianteDto fichaEstudianteDto : registroActual) {
				fichaEstudianteDto.setDtmtId(servJdbcRecordEstudianteDto.buscarDetalleMatricula(fichaEstudianteDto.getRcesMlcrprId(),fichaEstudianteDto.getFcesId(),amfPeriodoAcademicoId));
			}
			try {
				servJdbcFichaInscripcionDto.actualizarCarreraAreaXFcinId(registroActual.get(0).getFcesFcinId(),amfEstudianteJdbcDtoCambio.getFcinCarreraSiiu(),amfEstudianteJdbcDtoCambio.getFcinCncrArea());
			} catch (FichaInscripcionDtoException e) {
			}
			if(amfListMatriculaCambio.size()<amfListMatriculaSeleccionada.size()){
				Integer contadorRegistro=0;
				for (MateriaDto itemActualizar : amfListMatriculaCambio) {
					servJdbcRecordEstudianteDto.actualizarRecordEstudianteDetalleMatricula(registroActual.get(contadorRegistro).getRcesId()
							,registroActual.get(contadorRegistro).getDtmtId(),itemActualizar.getMlcrprId());
					
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(registroActual.get(contadorRegistro).getRcesMlcrprId(), 0);
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(itemActualizar.getMlcrprId(), 1);
					contadorRegistro++;
				}
					servJdbcRecordEstudianteDto.eliminarRecordEstudianteDetalleMatricula(registroActual.get(registroActual.size()-1).getRcesId(),
							registroActual.get(registroActual.size()-1).getDtmtId());
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(registroActual.get(registroActual.size()-1).getRcesMlcrprId(), 0);
			}else if(amfListMatriculaCambio.size()==amfListMatriculaSeleccionada.size()){
				Integer contadorRegistro=0;
				for (MateriaDto itemActualizar : amfListMatriculaCambio) {
					servJdbcRecordEstudianteDto.actualizarRecordEstudianteDetalleMatricula(registroActual.get(contadorRegistro).getRcesId()
							,registroActual.get(contadorRegistro).getDtmtId(),itemActualizar.getMlcrprId());
					
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(registroActual.get(contadorRegistro).getRcesMlcrprId(), 0);
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(itemActualizar.getMlcrprId(), 1);
					contadorRegistro++;
				}
			}else{
				Integer contadorRegistro=0;
				for (FichaEstudianteDto itemActualizar : registroActual) {
					servJdbcRecordEstudianteDto.actualizarRecordEstudianteDetalleMatricula(itemActualizar.getRcesId()
							,itemActualizar.getDtmtId(),amfListMatriculaCambio.get(contadorRegistro).getMlcrprId());
					
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(registroActual.get(contadorRegistro).getRcesMlcrprId(), 0);
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(amfListMatriculaCambio.get(contadorRegistro).getMlcrprId(), 1);
					contadorRegistro++;
				}
					servJdbcRecordEstudianteDto.insertarRecordEstudianteDetalleMatricula(registroActual.get(0).getRcesId(),
							registroActual.get(0).getDtmtId(),amfListMatriculaCambio.get(amfListMatriculaCambio.size()-1).getMlcrprId()
							,amfListMatriculaSeleccionada.get(0).getDtmtNumero());
					servJdbcMallaCurricularParaleloDto.modificarCupoMallaCurricularParalelo(amfListMatriculaCambio.get(amfListMatriculaCambio.size()-1).getMlcrprId(), 1);
			}
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("El cambio se realizó satisfactoriamente.");
		} catch (Exception e) {
			e.printStackTrace();
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al guardar el reajuste de matrícula, por favor intente más tarde.");
		}
		
		
		return "irInicio" ;
	}
	
	public void buscarEstudiantesMatriculados(){
		if (!amfPeriodoAcademicoId.equals(GeneralesConstantes.APP_ID_BASE)) {
				try {
//					fgmpgfListMateriasInscrito = new ArrayList<>();
					amfListEstudiantesMatriculados = servJdbcEstudianteDto.buscarXPeriodoNivelacionMatriculadoLista(amfPeriodoAcademicoId,  amfIdentificacion);
				} catch (EstudianteDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (EstudianteDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError("No se encontró resultados con los parámetros ingresados.");
				}
		}else {
			FacesUtil.mensajeError("Seleccione un Período Académico para continuar.");
		}
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

	public List<Carrera> getAmfListCarreraSiiu() {
		return amfListCarreraSiiu;
	}

	public void setAmfListCarreraSiiu(List<Carrera> amfListCarreraSiiu) {
		this.amfListCarreraSiiu = amfListCarreraSiiu;
	}

	public EstudianteJdbcDto getAmfEstudianteJdbcDtoCambio() {
		return amfEstudianteJdbcDtoCambio;
	}

	public void setAmfEstudianteJdbcDtoCambio(EstudianteJdbcDto amfEstudianteJdbcDtoCambio) {
		this.amfEstudianteJdbcDtoCambio = amfEstudianteJdbcDtoCambio;
	}

	public boolean isAmfHabilitadorCambioTotal() {
		return amfHabilitadorCambioTotal;
	}

	public void setAmfHabilitadorCambioTotal(boolean amfHabilitadorCambioTotal) {
		this.amfHabilitadorCambioTotal = amfHabilitadorCambioTotal;
	}

	public List<MateriaDto> getFgmpgfMallaMaterias() {
		return fgmpgfMallaMaterias;
	}

	public void setFgmpgfMallaMaterias(List<MateriaDto> fgmpgfMallaMaterias) {
		this.fgmpgfMallaMaterias = fgmpgfMallaMaterias;
	}

	public List<MateriaDto> getAmfListMatriculaCambio() {
		return amfListMatriculaCambio;
	}

	public void setAmfListMatriculaCambio(List<MateriaDto> amfListMatriculaCambio) {
		this.amfListMatriculaCambio = amfListMatriculaCambio;
	}

	public Integer getAmfParaleloSeleccionado() {
		return amfParaleloSeleccionado;
	}

	public void setAmfParaleloSeleccionado(Integer amfParaleloSeleccionado) {
		this.amfParaleloSeleccionado = amfParaleloSeleccionado;
	}

	public int getAmfNumeroMatricula() {
		return amfNumeroMatricula;
	}

	public void setAmfNumeroMatricula(int amfNumeroMatricula) {
		this.amfNumeroMatricula = amfNumeroMatricula;
	}

	
	
	
	

	
}