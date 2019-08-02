/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribuciÃ³n no autorizada de este programa, 
 * o cualquier porcion de el, puede dar lugar a sanciones criminales y 
 * civiles severas, y seran procesadas con el grado maximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     FichaGenerarMatriculaSuficienciasForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiante de suficiencias. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 30-MAY-2018			 Freddy Guzmán                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula.suficiencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ArancelException;
import ec.edu.uce.academico.ejb.excepciones.ArancelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
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
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ArancelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
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
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.GratuidadServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularNivelServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PrerequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaInformaticaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ArancelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MailConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SuficienciaInformaticaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoAperturaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.mail.MailRegistroMatricula;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteRegistroMatriculaForm;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes; 

/**
 * Clase (session bean) FichaGenerarMatriculaSuficienciasForm. 
 * Bean de sesion que maneja la matricula del estudiante de suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "fichaGenerarMatriculaSuficienciasForm")
@SessionScoped
public class FichaGenerarMatriculaSuficienciasForm extends HistorialAcademicoForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario fgmpgfUsuario;
	private PeriodoAcademico fgmpgfPeriodoAcademico;
	private PlanificacionCronograma fgmpgfPlanificacionCronograma;
	private Carrera fgmpgfCarreraSeleccion;
	private Dependencia fgmpgfDependenciaBuscar;
	private FichaInscripcionDto fgmpgfFichaInscripcionDto;
	private CronogramaActividadJdbcDto fgmpgfProcesoFlujo;

	private Boolean fgmpgfPerdidaGratuidadDefinitiva;
	private Integer fgmpgfNivelUbicacion;
	private int activarModalConfirmacion;
	private Boolean fgmpgfEstudianteNuevo;
	private Boolean fgmpgfEstudiantePosgrado;
	
	
	private List<ParaleloDto> fgmpgfListParaleloDto;
	private List<FichaInscripcionDto> fgmpgfListFichaInscripcionDto;
	private List<MateriaDto> fgmpgfListMallaCurricularMateria;
	// idiomas
	private Boolean fgmsfModalidadOnLine;
	private Integer fgmpgfCarrerId;
	private Integer fgmsfDependenciaId;
	
	
	//informatica
	private Boolean fgmpgfPrimeraExoneracion; 
	
	private List<Arancel> fgmpgfListArancel;
	private List<MateriaDto> fgmpgfMateriasMatriculadas;
	private List<MateriaDto> fgmpgfListRecordEstudiante;
//	private List<MateriaDto> fgmpgfListRecordEstudianteActivo;
	
	private List<MateriaDto> fgmpgfListMateriasAmatricularMateriaDto;//materias a matricularse -> mostrar
	private List<RecordEstudianteDto> fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto;//historial matriculas SHI
	private List<RecordEstudianteDto> fgmpgfListMatriculasPregradoRecordEstudianteDto;//matricula activa pregrado + aumentar suficiencias
	private List<RecordEstudianteDto> fgmpgfListHistorialAcademicoPregradoRecordEstudianteDto;//historial academico del estudiante
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB private FichaInscripcionDtoServicioJdbc servFgmpgfFichaInscripcionDto;
	@EJB private RecordEstudianteDtoServicioJdbc servFgmpgfRecordEstudianteDto;
	@EJB private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB private ParaleloDtoServicioJdbc servFgmpgfParaleloDto;
	@EJB private FichaMatriculaDtoServicioJdbc servFgmpgfFichaMatriculaDto;
	@EJB private CronogramaActividadDtoServicioJdbc servFgmpgfCronogramaActividadDtoServicioJdbcServicio;
	@EJB private CarreraDtoServicioJdbc servFgmpgfCarreraDtoServicioJdbc;
	@EJB private MateriaDtoServicioJdbc servFgmpgfMateriaDtoServicioJdbc;
	@EJB private PrerequisitoDtoServicioJdbc servPrerequisitoDtoServicioJdbc;
	@EJB private HorarioAcademicoDtoServicioJdbc servHorarioAcademicoDtoServicioJdbc;
	@EJB private MallaCurricularParaleloDtoServicioJdbc servFgmpfMallaCurricularParaleloDtoServicioJdbc;
	@EJB private MatriculaServicioJdbc servFgmpgfMatriculaServicioJdbc;
	@EJB private EstudianteDtoServicioJdbc servFgmpgfEstudianteDto;
	@EJB private CorequisitoDtoServicioJdbc servFgmpgfCorrequisitoServicioJdbc;
	@EJB private CronogramaDtoServicioJdbc servFgmpgfCronogramaDtoServicioJdbc;
	@EJB private GratuidadServicioJdbc servJdbcGratuidad;
	@EJB private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB private SuficienciaInformaticaServicioJdbc servJdbcSuficienciaInformatica;
	@EJB private MallaCurricularNivelServicioJdbc servJdbcMallaCurricularNivel;
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto; 

	@EJB private CronogramaServicio servFgmpgfCronograma;
	@EJB private ProcesoFlujoServicio servFgmpgfProcesoFlujo;
	@EJB private PlanificacionCronogramaServicio servFgmpgfPlanificacionCronograma;
	@EJB private CronogramaProcesoFlujoServicio servFgmpgfCronogramaProcesoFlujo;
	@EJB private PeriodoAcademicoServicio servFgmpgfPeriodoAcademico;
	@EJB private MatriculaServicio servFgmpgfMatriculaServicio;
	@EJB private MallaCurricularServicio servFgmpgfMallaCurricular;	
	@EJB private DependenciaServicio servFgmpgfDependenciaServicio;
	@EJB private UsuarioRolServicio servNpfUsuarioRolServicio;
	@EJB private MallaCurricularParaleloServicio servFgmpfMallaCurricularParaleloServicio;
	@EJB private ParaleloServicio servFgmpgfParaleloServicio;
	@EJB private CarreraServicio servFgmpgfCarreraServicio;
	@EJB private MateriaServicio servFgmpgfMateriaServicio;
	@EJB private NivelServicio servFgmpgfNivelServicio;
	@EJB private ArancelServicio servFgmpgfArancelServicio;
	@EJB private ConfiguracionCarreraServicio servFgmpgfConfiguracionCarreraServicio;
	@EJB private SolicitudTerceraMatriculaServicio servFgmpgfSolicitudTerceraMatriculaServicio;
	@EJB private TipoGratuidadServicio servFgmpgfTipoGratuidadServicio;
	@EJB private FichaEstudianteServicio servFgmpgfFichaEstudianteServicio;
	@EJB private CarreraServicio servCarrera;
	@EJB private FichaInscripcionServicio servFichaInscripcionServicio;

	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula de las suficiencias.
	 */
	public String irListarFichaInscripcionSuficiencias(Usuario usuario) {
		String retorno = null;
		
		try {
			fgmpgfUsuario = usuario;
			
			List<FichaInscripcionDto> fichasInscripcion = servFgmpgfFichaInscripcionDto.buscarFichasInscripcion(fgmpgfUsuario.getUsrPersona().getPrsIdentificacion(), new Integer[]{FichaInscripcionConstantes.ACTIVO_VALUE,FichaInscripcionConstantes.FCIN_ESTADO_APROBADO_VALUE});
			
			if (fichasInscripcion != null && fichasInscripcion.size() > 0) {
				List<FichaInscripcionDto> auxListFcin = new ArrayList<>();
				Map<Integer, FichaInscripcionDto> mapFichas = new HashMap<Integer, FichaInscripcionDto>();
				
				for (FichaInscripcionDto it : fichasInscripcion) {
					if (it.getFcinTipo()!= null && it.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE) {
						auxListFcin.add(it);
					}
				}
				
				if (auxListFcin != null && auxListFcin.size() > 0) {
					fgmpgfListFichaInscripcionDto = new ArrayList<>();
					for(FichaInscripcionDto it : auxListFcin) {
						mapFichas.put(it.getFcinId(), it);
					}
					
					for(Entry<Integer, FichaInscripcionDto> fcin : mapFichas.entrySet()) {
						fgmpgfListFichaInscripcionDto.add(fcin.getValue());
					}
				}
				
			}
			
			fgmpgfEstudiantePosgrado = false;
			fgmpgfListHistorialAcademicoPregradoRecordEstudianteDto = cargarHistorialAcademicoSAIUHomologado(fgmpgfUsuario.getUsrIdentificacion());
			fgmpgfPerdidaGratuidadDefinitiva = verificarGratuidadPerdidaDefinitivaInformatica();
			retorno = "irListarFichaInscripcionSuficiencia";
			
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno; 
	}
	
	
	/**
	 * Mpetodo que permite direccionar hacia el xhtml generar matricula.
	 * @param estudiante - estudiante con datos necesarios para continuar con la matricula.
	 * @return el xhtml generarMatricula.
	 */
	public String irGenerarMatriculaSuficiencias(FichaInscripcionDto estudiante) {
		String retorno = null;
		
		fgmpgfFichaInscripcionDto = estudiante;
		activarModalConfirmacion = 0;
		fgmsfModalidadOnLine = false;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");


		try {

			fgmpgfCarreraSeleccion = servFgmpgfCarreraServicio.buscarPorId(estudiante.getCrrId());
			fgmpgfDependenciaBuscar = servFgmpgfDependenciaServicio.buscarPorId(estudiante.getDpnId());
			fgmpgfListMallaCurricularMateria = servJdbcMateriaDto.listarMateriasxCarreraFull(estudiante.getCrrId(), new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});						

			if (estudiante.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
				
				if (!verificarSiAproboSuficiencia()) {
					
					if (validarCronogramaSuficiencias(CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE)) {
						
						fgmpgfPeriodoAcademico = servFgmpgfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
						fgmpgfProcesoFlujo = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarPlanificacionCronogramaPorFechasFull(sdf.format(GeneralesUtilidades.getFechaActualSistema()), CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE);
						fgmpgfPlanificacionCronograma = servFgmpgfPlanificacionCronograma.buscarPorId(fgmpgfProcesoFlujo.getPlcrId());
						
						if(!verificarNoMatriculado()){

							if (verificarMatriculaPregrado(estudiante.getPrsIdentificacion())
									|| verificarSiEstudianteEgreso() 
									|| estudiante.getFcinEstadoRetiro().equals(FichaInscripcionConstantes.ESTADO_RETIRO_EGRESADO_VALUE)) {
								FacesUtil.limpiarMensaje();
								return cargarParametrosMatriculaIdiomas(estudiante , false);

							}else {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Ud. no puede matrícularse en la Suficiencia ya que no registra una Matrícula Vigente.");
							}
							
						}
					}
				}else {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Ud. ha finalizado la suficiencia en Idiomas.");
				}
				
			} else if (estudiante.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {

				fgmpgfPeriodoAcademico = servFgmpgfPeriodoAcademico.buscarXestadoXtipoPeriodo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
				fgmpgfProcesoFlujo = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarPlanificacionCronogramaPorFechasFull(sdf.format(GeneralesUtilidades.getFechaActualSistema()), CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE);
				fgmpgfPlanificacionCronograma = servFgmpgfPlanificacionCronograma.buscarPorId(fgmpgfProcesoFlujo.getPlcrId());
				
				if (fgmpgfFichaInscripcionDto.getFcinEstado().equals(FichaInscripcionConstantes.FCIN_ESTADO_APROBADO_VALUE)) {
					FacesUtil.mensajeInfo("Usted ha finalizado la suficiencia en Cultura Física.");
					return retorno;
				}else {
					if (fgmpgfFichaInscripcionDto.getFcinTipoIngreso().equals(FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
						Integer aprobado = aprobarEstudiantesMigracionCulturaFisica(estudiante);
						if (aprobado == null) { // si es null no termina aun la SCF
							long nAprobados = 0;
							List<RecordEstudianteDto> recordCulturaFisicaSAIU = buscarRecordAcademico();				
							if (recordCulturaFisicaSAIU != null && recordCulturaFisicaSAIU.size() > 0) {
								List<RecordEstudianteDto> filtrado = filtrarNuevasCondiciones(recordCulturaFisicaSAIU);
								// si tienen estados difeentes al de aprobado o reprobado sacarles

								Predicate<RecordEstudianteDto> predicate = s-> s.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL);
								nAprobados = filtrado.stream().filter(predicate).count();

								if (nAprobados > 1) {
									FacesUtil.mensajeInfo("Usted ya aprobó la Suficiencia en Cultura Física.");
									return retorno;
								}else {

									if (validarCronogramaSuficiencias(CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
										// si tienen o no una matricula vigente en este periodo.
										if(!verificarNoMatriculado()){

											if( fgmpgfListMallaCurricularMateria == null || fgmpgfListMallaCurricularMateria.size() == 0){
												FacesUtil.mensajeError("No se encontró materias para matricularse, verifique la malla curricular.");
												return retorno;
											}
											// VERIFICAR QUE LA FICHA SE ENCUENTRE ACTIVA
											if (fgmpgfFichaInscripcionDto.getFcinEstado() == FichaInscripcionConstantes.ACTIVO_VALUE) {
												// Estudiante Nuevo.
												if (nAprobados == 0) {

													cargarParalelosSuficienciaCulturaFisica(1 , true, recordCulturaFisicaSAIU);

													if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
														// record academico del periodo activo.
//														List<MateriaDto> auxListMateriaDto = cargarRegistroMateriasMatriculaPregrado();
//														if (auxListMateriaDto !=null && auxListMateriaDto.size() > 0) {
//															fgmpgfListRecordEstudianteActivo = new ArrayList<>();
//															fgmpgfListRecordEstudianteActivo = 	auxListMateriaDto;
//														}
														return "irGenerarMatriculaCulturaFisica";	
													}else {
														return retorno;
													}


												}else if (nAprobados == 1) {

													cargarParalelosSuficienciaCulturaFisica(2 , false, recordCulturaFisicaSAIU);

													if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
//														// record academico del periodo activo.
//														List<MateriaDto> auxListMateriaDto = cargarRegistroMateriasMatriculaPregrado();
//														if (auxListMateriaDto !=null && auxListMateriaDto.size() > 0) {
//															fgmpgfListRecordEstudianteActivo = new ArrayList<>();
//															fgmpgfListRecordEstudianteActivo = 	auxListMateriaDto;
//														}
														return "irGenerarMatriculaCulturaFisica";	
													}else {
														return retorno;
													}

												}else{ 
													return retorno; 
												}

											}else {
												return retorno;
											}

										}else {
											return retorno;
										}

									}else {
										return retorno;	
									}

								}
							}else {
								// SI ES NUEVO
								if (validarCronogramaSuficiencias(CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
									// si tienen o no una matricula vigente en este periodo.
									if(!verificarNoMatriculado()){

										if( fgmpgfListMallaCurricularMateria == null || fgmpgfListMallaCurricularMateria.size() == 0){
											FacesUtil.mensajeError("No se encontró materias para matricularse, cargue la malla curricular.");
											return retorno;
										}

										// VERIFICAR QUE LA FICHA SE ENCUENTRE ACTIVA
										if (fgmpgfFichaInscripcionDto.getFcinEstado() == FichaInscripcionConstantes.ACTIVO_VALUE) {
											// Estudiante Nuevo.
											cargarParalelosSuficienciaCulturaFisica(1 , true, recordCulturaFisicaSAIU);

											if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
												return "irGenerarMatriculaCulturaFisica";	
											}else {
												return retorno;
											}
										}else {
											return retorno;
										}

									}else {
										return retorno;
									}

								}else {
									return retorno;	
								}
							}

						}else {
							return retorno;
						}
					}else {// SCF -> intensivos
						
						Integer aprobado = aprobarEstudiantesMigracionCulturaFisica(estudiante);
						if (aprobado == null) { // si es null no termina aun la SCF
							long nAprobados = 0;
							List<RecordEstudianteDto> recordCulturaFisicaSAIU = buscarRecordAcademico();				
							if (recordCulturaFisicaSAIU != null && recordCulturaFisicaSAIU.size() > 0) {
								List<RecordEstudianteDto> filtrado = filtrarNuevasCondiciones(recordCulturaFisicaSAIU);
								// si tienen estados difeentes al de aprobado o reprobado sacarles

								Predicate<RecordEstudianteDto> predicate = s-> s.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL);
								nAprobados = filtrado.stream().filter(predicate).count();

								if (nAprobados > 1) {
									FacesUtil.mensajeInfo("Usted ya aprobó la Suficiencia en Cultura Física.");
									return retorno;
								}else {

									if (validarCronogramaSuficiencias(CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
										// si tienen o no una matricula vigente en este periodo.
										if(!verificarNoMatriculado()){

											if( fgmpgfListMallaCurricularMateria == null || fgmpgfListMallaCurricularMateria.size() == 0){
												FacesUtil.mensajeError("No se encontró materias para matricularse, verifique la malla curricular.");
												return retorno;
											}
											// VERIFICAR QUE LA FICHA SE ENCUENTRE ACTIVA
											if (fgmpgfFichaInscripcionDto.getFcinEstado() == FichaInscripcionConstantes.ACTIVO_VALUE) {
												// Estudiante Nuevo.
												if (nAprobados == 0) {

													// cargar nivel maximo para intensivos - > una sola materia
													cargarParalelosSuficienciaCulturaFisica(-1 , true, recordCulturaFisicaSAIU);

													if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
														return "irGenerarMatriculaCulturaFisica";	
													}else {
														return retorno;
													}


												}else if (nAprobados == 1) {

													cargarParalelosSuficienciaCulturaFisica(-1 , false, recordCulturaFisicaSAIU);

													if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
														return "irGenerarMatriculaCulturaFisica";	
													}else {
														return retorno;
													}

												}else{ 
													return retorno; 
												}

											}else {
												return retorno;
											}

										}else {
											return retorno;
										}

									}else {
										return retorno;	
									}

								}
							}else {
								// SI ES NUEVO
								if (validarCronogramaSuficiencias(CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
									// si tienen o no una matricula vigente en este periodo.
									if(!verificarNoMatriculado()){

										if( fgmpgfListMallaCurricularMateria == null || fgmpgfListMallaCurricularMateria.size() == 0){
											FacesUtil.mensajeError("No se encontró materias para matricularse, cargue la malla curricular.");
											return retorno;
										}

										// VERIFICAR QUE LA FICHA SE ENCUENTRE ACTIVA
										if (fgmpgfFichaInscripcionDto.getFcinEstado() == FichaInscripcionConstantes.ACTIVO_VALUE) {
											// Estudiante Nuevo.
											cargarParalelosSuficienciaCulturaFisica(-1 , true, recordCulturaFisicaSAIU);

											if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
												return "irGenerarMatriculaCulturaFisica";	
											}else {
												return retorno;
											}
										}else {
											return retorno;
										}

									}else {
										return retorno;
									}

								}else {
									return retorno;	
								}
							}

						}else {
							return retorno;
						}
						
					}
				}
				
			} else if (estudiante.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {

				// si ya aprobo o no
				if (!verificarAprobacionSuficienciaInformatica(estudiante.getPrsIdentificacion())) {
					// si esta matriculado o no
					if (!verificarMatriculaSuficienciaInformatica(estudiante.getPrsIdentificacion())) {
						// si es estudiante de PREGRADO y cuenta con una matricula en PERIODO activo o en cierre.
						if (verificarMatriculaPregrado(estudiante.getPrsIdentificacion()) 
								|| verificarSiEstudianteEgreso()
								|| estudiante.getFcinEstadoRetiro().equals(FichaInscripcionConstantes.ESTADO_RETIRO_EGRESADO_VALUE)) {
							
							FacesUtil.limpiarMensaje();
							// si la ficha del estudiante se encuentra activa
							if (estudiante.getFcinEstado().equals(FichaInscripcionConstantes.ACTIVO_VALUE)) {

								//malla curricular materia
								if(!fgmpgfListMallaCurricularMateria.isEmpty()){
									List<ModalidadDto> modalidades = new ArrayList<>();
									modalidades.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE, ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL));
									modalidades.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE, ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL));
									modalidades.add(new ModalidadDto(ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE, ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL));

									for (MateriaDto item : fgmpgfListMallaCurricularMateria) {
										item.setMtrCmbEstado(Boolean.TRUE);
										item.setMtrCmbEstadoDisable(Boolean.TRUE);
										item.setMtrListModalidadDto(modalidades);
										item.setMtrListParalelo(null);
									}

									fgmpgfListMateriasAmatricularMateriaDto = fgmpgfListMallaCurricularMateria;
									fgmpgfNivelUbicacion = NivelConstantes.NIVEL_PRIMERO_VALUE;
									fgmpgfFichaInscripcionDto = estudiante;

									fgmpgfEstudianteNuevo = true;

									return "irGenerarMatriculaInformatica";													
								}

							}else {
								FacesUtil.mensajeError("Ficha Inscripción inactiva, verifique en la Secretaria que sus datos se encuentren registrados correctamente.");
								return retorno;
							}
						}else {
							FacesUtil.mensajeInfo("Ud no puede matrícularse en la Suficiencia en Herramientas Informáticas ya que no posee una Matrícula Vigente.");
							return retorno;
						}
					}else {
						FacesUtil.mensajeInfo("Ud ya registra una Matrícula Activa en la Suficiencia en Herramientas Informáticas.");
						return retorno;	
					}
				}else {
					FacesUtil.mensajeInfo("Ud ya aprobó la Suficiencia en Herramientas Informáticas.");
					return retorno;
				}
			}else {
				FacesUtil.mensajeError("Error al buscar Ficha Inscripción, comuníquese con el Administrador del Sistema.");
				return retorno;
			}

		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.carrera.no.encontrado.exception")));
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.periodo.academico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.cronograma.actividad.dto.jdbc.no.encontrado.exception")));
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.dependencia.no.encontrado.exception")));
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.planificacion.cronograma.no.encontrado.exception")));
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.materia.dto.no.encontrado.exception")));
		}
		
		return retorno;
	}
	
	
	private boolean verificarSiAproboSuficiencia() {
		boolean retorno = false;
		
		fgmpgfListRecordEstudiante = cargarRecordAcademicoIdiomaSeleccionado();
		if (!fgmpgfListRecordEstudiante.isEmpty()) {
			for (MateriaDto materia : fgmpgfListRecordEstudiante) {
				if ((materia.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) && materia.getNvlNumeral().equals(NivelConstantes.NIVEL_APROBACION_VALUE))
						|| (materia.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) && materia.getNvlNumeral().equals(NivelConstantes.NIVEL_CUARTO_VALUE))) {
					retorno = true;
				}
			}
		}
		
		return retorno;
	}


	/**
	 * Metodo que permite cargar los parametros necesarios para generar la matricula en Idiomas.
	 * @param estudiante - ficha inscripcion del estudiante.
	 * @param isPlanContingencia - true si el plan contingencia esta activo
	 * @return ruta de navegacion.
	 */
	private String cargarParametrosMatriculaIdiomas(FichaInscripcionDto estudiante, boolean isPlanContingencia) {
		String retorno = null;

		if (isPlanContingencia) {

			List<MateriaDto> recordIdioma = cargarRecordAcademicoIdiomaSeleccionado();

			fgmpgfListRecordEstudiante = new ArrayList<>(); 
			if(recordIdioma != null && recordIdioma.size() >0){
				fgmpgfListRecordEstudiante = recordIdioma;								
			}

			// verificar si el estudiante tiene matricula en periodo vigente  en cierre 
			if (verificarMatriculaPregrado(estudiante.getPrsIdentificacion())) {

//				// record academico - carrera
//				List<MateriaDto> auxListRecordCarrera =  cargarRegistroMateriasMatriculaPregrado();
//				if (auxListRecordCarrera != null && auxListRecordCarrera.size() > 0) {
//					fgmpgfListRecordEstudianteActivo = new ArrayList<>();
//					fgmpgfListRecordEstudianteActivo = 	auxListRecordCarrera;
//				}

				// VERIFICAR QUE LA FICHA SE ENCUENTRE ACTIVA
				if (fgmpgfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_SUFICIENCIAS_VALUE && 
						fgmpgfFichaInscripcionDto.getFcinEstado() == FichaInscripcionConstantes.ACTIVO_VALUE) {

					// Contingencia -> aplica solo a ingles
					if (fgmpgfCarreraSeleccion.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE) {

						// ubicar el nivel y continuar
						if (verificarContingenciaNivelMatricula(fgmpgfListMatriculasPregradoRecordEstudianteDto)) {

							// estudiante viejo
							if (fgmpgfListRecordEstudiante.size() > 0) {

								// si tiene aprobado hasta A2.1 -> matricula normal 
								if (verificarNivelMatricula(recordIdioma)) {

									// matricula todo lo que le falta
									fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,0);
									if(fgmpgfListMateriasAmatricularMateriaDto == null){
										FacesUtil.mensajeError("No se encontró materias para matricularse..");
										return null;
									}

									if (!verificarCronogramaPorNivelUbicacion()) {
										return null;
									}

								}else {
									// matricula todo lo que le falta
									fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
									if(fgmpgfListMateriasAmatricularMateriaDto == null){
										FacesUtil.mensajeError("No se encontró materias para matricularse..");
										return null;
									}

									if (!verificarCronogramaPorNivelUbicacion()) {
										return null;
									}

								}

								// estudiante nuevo	
							}else {
								// matricula todo lo que le falta
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse..");
									return null;
								}

								if (!verificarCronogramaPorNivelUbicacion()) {
									return null;
								}

							}
						}else {
							// matricula normal -> nivel que le corresponda.
							// Estudiante existente o tiene homologacion.
							if ((fgmpgfListRecordEstudiante.size() > 0)) {
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,0);
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse..");
									return null;
								}
							}else {
								// Estudiante nuevo 
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularNuevo(); 
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse.");
									return null;
								}
							}

							if (!verificarCronogramaPorNivelUbicacion()) {
								return null;
							}
						}

					}else {

						// CONTINGENCIA LOS DEMAS IDIOMAS
						if (verificarNivelMatricula(recordIdioma)) {
							// matricula una sola de todo lo que le falta
							fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,0);
							if(fgmpgfListMateriasAmatricularMateriaDto == null){
								FacesUtil.mensajeError("No se encontró materias para matricularse..");
								return null;
							}

							if (!verificarCronogramaPorNivelUbicacion()) {
								return null;
							}

						}else {
							// matricula nivel de aprobacion
							fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
							if(fgmpgfListMateriasAmatricularMateriaDto == null){
								FacesUtil.mensajeError("No se encontró materias para matricularse..");
								return null;
							}

							if (!verificarCronogramaPorNivelUbicacion()) {
								return null;
							}

						}

					}

				}else {
					FacesUtil.mensajeError("Acérquese a la secretaria de idiomas para mayor información. Ficha inscripción inactiva. ");
					return null;
				}							


			}else {

				// estudiante egresado
				if (verificarSiEstudianteEgreso()) {
					if (fgmpgfCarreraSeleccion.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE) {

						if(recordIdioma != null && recordIdioma.size() >0){
							if (verificarNivelMatricula(recordIdioma)) {
								// matricula un solo nivel A2.2
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,0);
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse..");
									return null;
								}

								if (!verificarCronogramaPorNivelUbicacion()) {
									return null;
								}

							}else {
								// matricula todo lo que le falta - contingencia
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse..");
									return null;
								}

								if (!verificarCronogramaPorNivelUbicacion()) {
									return null;
								}

							}

						}else {
							// matricula todo lo que le falta - contingencia
							fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
							if(fgmpgfListMateriasAmatricularMateriaDto == null){
								FacesUtil.mensajeError("No se encontró materias para matricularse..");
								return null;
							}

							if (!verificarCronogramaPorNivelUbicacion()) {
								return null;
							}

						}

					}else { // otros idiomas

						if(recordIdioma != null && recordIdioma.size() >0){
							if (verificarNivelMatricula(recordIdioma)) {
								// matricula un solo nivel A2.2
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,0);
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse..");
									return null;
								}

								if (!verificarCronogramaPorNivelUbicacion()) {
									return null;
								}

							}else {
								// matricula todo lo que le falta - contingencia
								fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
								if(fgmpgfListMateriasAmatricularMateriaDto == null){
									FacesUtil.mensajeError("No se encontró materias para matricularse..");
									return null;
								}

								if (!verificarCronogramaPorNivelUbicacion()) {
									return null;
								}

							}

						}else {
							// matricula todo lo que le falta - contingencia
							fgmpgfListMateriasAmatricularMateriaDto = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante,1);
							if(fgmpgfListMateriasAmatricularMateriaDto == null){
								FacesUtil.mensajeError("No se encontró materias para matricularse..");
								return null;
							}

							if (!verificarCronogramaPorNivelUbicacion()) {
								return null;
							}

						}
					}

				}
			}
		}else {

			List<MateriaDto> materiasAmatricular = cargarMateriasAMatricularse(fgmpgfListRecordEstudiante, UBICACION_MATRICULA_NORMAL);
			if (materiasAmatricular != null && !materiasAmatricular.isEmpty()) {
				fgmpgfListMateriasAmatricularMateriaDto = materiasAmatricular;
				if (verificarCronogramaPorNivelUbicacion()) {
					fgmpgfPerdidaGratuidadDefinitiva = calculoPerdidaGratuidadPorSegundoIdioma();
					retorno = "irGenerarMatriculaIdiomas";
				}
			}
			

		}

		return retorno;
	}


	/**
	 * Método que permite buscar si el usuario que ingrese tiene vigente la matricula en pregrado.
	 * @param identificacion - cedula o pasaporte.
	 * @return true - si tiene matricula en el periodo vigente o en cierre.
	 */
	private boolean verificarMatriculaPregrado(String identificacion) {
		boolean retorno = false;
		List<RecordEstudianteDto> matriculas = new ArrayList<>();

		if (fgmpgfEstudiantePosgrado) {
			fgmpgfListMatriculasPregradoRecordEstudianteDto = fgmpgfListHistorialAcademicoPregradoRecordEstudianteDto;
		}else {
			List<PeriodoAcademico> periodos = cargarPeriodosAcademicosPregrado();
			if (!periodos.isEmpty()) {
				if (!periodos.isEmpty() && periodos.size() > 1) {
					fgmpgfListMatriculasPregradoRecordEstudianteDto = new ArrayList<>();
					retorno = true;
				}else {
					
					try {
						matriculas = servJdbcSuficienciaInformatica.buscarMatriculaActivaPregrado(identificacion, periodos.get(periodos.size()-1).getPracId());
						if (!matriculas.isEmpty()) {
							fgmpgfListMatriculasPregradoRecordEstudianteDto = matriculas;
							retorno = true;
						}

					} catch (FichaMatriculaNoEncontradoException e) {
						fgmpgfListMatriculasPregradoRecordEstudianteDto = new ArrayList<>();
					} catch (FichaMatriculaException e) {
						FacesUtil.mensajeError(e.getMessage());
					}
					
				}
			}

		}

		if (!retorno) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró Matrículas Vigentes en Pregrado, ud no puede continuar con la Matricula en Herramientas Informáticas.");
		}

		return retorno;
	}
	


	private List<PeriodoAcademico> cargarPeriodosAcademicosPregrado() {
		List<PeriodoAcademico> retorno = new ArrayList<>();
		
		try {
			retorno = servFgmpgfPeriodoAcademico.buscarPeriodos(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, Arrays.asList(new String[]{String.valueOf(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE), String.valueOf(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE)}));
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	/**
	 * Método que permite buscar al estudiante en la Base de Datos de la Suficiencia en Informatica.
	 * Consideraciones:
	 * BASE SUFICIENCIA INFORMATICA 
	 *  - Exoneraciones
	 *  - Presenciales
	 * ACADEMICO
	 *  - Exoneraciones
	 *  - Regulares
	 *  - Intensivos 
	 * @return true - aprobado.
	 */
	private boolean verificarAprobacionSuficienciaInformatica(String identificacion) {
		boolean retorno = false;
		
		fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto = new ArrayList<>(); 
		try {
			fgmpgfPrimeraExoneracion = true;
			List<RecordEstudianteDto> exonerados = servJdbcSuficienciaInformatica.buscarHistorialMatriculasExoneracion(identificacion);
			if (!exonerados.isEmpty()) {
				fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto.addAll(exonerados);			
				for (RecordEstudianteDto item1 : exonerados) {
					if (item1.getRcesEstadoLabel().equals(SuficienciaInformaticaConstantes.RCES_EXONERACION_ESTADO_APROBADO_LABEL)) {
						retorno = true;
						break;
					}
				}
				
				if (!retorno) {
					fgmpgfPrimeraExoneracion = false;
				}
				
			}
			
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		try {
			List<RecordEstudianteDto> presenciales = servJdbcSuficienciaInformatica.buscarHistorialMatriculasPresencial(identificacion);
			if (!presenciales.isEmpty()) {
				fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto.addAll(presenciales);
				for (RecordEstudianteDto item2 : presenciales) {
					if (item2.getRcesEstadoLabel().equals(SuficienciaInformaticaConstantes.RCES_PRESENCIAL_ESTADO_APROBADO_LABEL)) {
						retorno = true;
						break;
					}
				}
			}
			
		} catch (RecordEstudianteNoEncontradoException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		} catch (RecordEstudianteException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}
		
		try {
			List<RecordEstudianteDto> matriculasSIIU = servJdbcSuficienciaInformatica.buscarHistorialMatriculasSuficiencia(identificacion);
			if (!matriculasSIIU.isEmpty()) {
				fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto.addAll(matriculasSIIU);
				for (RecordEstudianteDto item3 : matriculasSIIU) {
					item3.setRcesEstadoLabel(ListasCombosForm.getListaEstadoRecorAcademico(item3.getRcesEstado()));
					if (item3.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
						retorno = true;
						break;
					}
				}
			}
			
		} catch (RecordEstudianteNoEncontradoException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		} catch (RecordEstudianteException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}
		
		FacesUtil.limpiarMensaje();
		return retorno;
	}
	
	/**
	 *  * ESTUDIANTES - Pagan si solo si PIERDEN GRATUIDAD DEFINITIVA.
	 * -> INTENSIVOS Y REGULARES - Siempre pagan 60.00
	 * -> EXONERACIONES - 1era vez 60.00 / 2da a n_veces -> 10.00  
	 */
	private BigDecimal calcularValorAPagarPorSuficienciaInformatica() {
		BigDecimal retorno = BigDecimal.valueOf(0);

		if (fgmpgfPerdidaGratuidadDefinitiva) {

			if (fgmpgfProcesoFlujo.getPrlfId().equals(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_VALUE)) {
				if (!fgmpgfPrimeraExoneracion) {
					if (fgmpgfEstudiantePosgrado) {
						retorno = BigDecimal.valueOf(60);	
					}else {
						retorno = BigDecimal.valueOf(10);	
					}
				}else {
					retorno = BigDecimal.valueOf(60);	
				}
			}else {
				retorno = BigDecimal.valueOf(60);
			}
		}


//		try {
//
//			List<Arancel> aranceles = servFgmpgfArancelServicio.listarXGratuidadXTipoMatriculaXModalidadXTipoArancel(calcularGratuidad().intValue(), fgmpgfProcesoFlujo.getPrlfId(), fgmpgfFichaInscripcionDto.getCncrModalidad(), ArancelConstantes.TIPO_ARANCEL_DISENO_VALUE);
//			if(aranceles != null && aranceles.size() > 0 ){
//				fgmpgfListArancel = new ArrayList<>();
//				fgmpgfListArancel.addAll(aranceles);
//			}
//
//		} catch (ArancelNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (ArancelException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		}  

		return retorno;
	}
	
	
	/**
	 * Método que permite verificar si el estudiante ya perdio la gratuidad.
	 * - Consideraciones:
	 * ESTUDIANTES MATRICULADOS  - Pagan si solo si PIERDEN GRATUIDAD DEFINITIVA.
	 * -> INTENSIVOS Y REGULARES - Siempre pagan 60.00
	 * -> EXONERACIONES - 1era vez 60.00 / 2da a n_veces -> 10.00   
	 * ESTUDIANTES EGRESADOS- Pagan si solo si PIERDEN GRATUIDAD DEFINITIVA.
	 * -> INTENSIVOS Y REGULARES - Siempre pagan 60.00
	 * -> EXONERACIONES - 1era vez 60.00 / 2da a n_veces -> 10.00
	 * ESTUDIANTES PREGRADO
	 * -> Consideramos a todos los estudiantes MATRICULADOS en periodo ACTIVO  (Instructivo Art 7)
	 *    No aplican:
	 *    INSCRITOS
	 *    ANULADOS MATRICULA
	 * -> Consideramos solo a EGRESADOS
	 * PARTICULARES Y ESTUDIANTES POSTGRADO
	 * -> Siempre pagan 60.00
	 */
	@SuppressWarnings("rawtypes")
	private boolean verificarGratuidadPerdidaDefinitivaInformatica() {
		boolean retorno = false;
		List<RecordEstudianteDto> matriculas = fgmpgfListHistorialAcademicoPregradoRecordEstudianteDto;

		try {// POR SEGUNDA CARRERA PREGRADO // DESPUES DE TITULADO O EN PARALELO
			retorno = servJdbcGratuidad.buscarSegundaCarrera(fgmpgfUsuario.getUsrIdentificacion());
			FacesUtil.mensajeInfo("Usted registra Segunda Carrera, por ello no aplica Gratuidad.");

		} catch (GratuidadNoEncontradoException e) {// POR PERDER GRATUIDAD CON 30% REPROBADAS EN SAIU

			List<String> auxListCarreras = new ArrayList<>();
			if (!matriculas.isEmpty()) {
				Set<CarreraDto> carreras = matriculas.stream().collect(Collectors.groupingBy(RecordEstudianteDto::getRcesCarreraDto)).keySet();
				if (!carreras.isEmpty()) {
					Iterator it = carreras.iterator();
					while (it.hasNext()) {
						CarreraDto item = (CarreraDto) it.next();
						auxListCarreras.add(String.valueOf(item.getCrrId()));
					}
				}

				//lista de malla curricular nivel -> creditos acumulados por Malla
				List<MallaCurricularNivelDto> creditosAcumuladosPorMalla = new ArrayList<>();
				try {
					if (!auxListCarreras.isEmpty()) {
						creditosAcumuladosPorMalla = servJdbcMallaCurricularNivel.buscarCreditosPorMalla(auxListCarreras);	
					}
				} catch (MallaCurricularNivelException e1) {
				} catch (MallaCurricularNivelNoEncontradoException e1) {
				}

				//Si excede el 30% de creditos reprobado en la malla pierde gratuidad.
				if (!creditosAcumuladosPorMalla.isEmpty()) {

					int creditosReprobados = 0;
					for (MallaCurricularNivelDto carrera : creditosAcumuladosPorMalla) {

						for (RecordEstudianteDto materia : matriculas) {
							if (carrera.getMlcrnvCarreraDto().getCrrId() == materia.getRcesCarreraDto().getCrrId()) {
								// si reprobo a sumar creditos reprobados
								if (materia.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
									creditosReprobados = creditosReprobados + materia.getRcesMateriaDto().getMtrCreditos();
									System.out.println(materia.getRcesMateriaDto().getMtrCodigo() + " - " + materia.getRcesMateriaDto().getNumMatricula() + " - " + materia.getRcesMateriaDto().getMtrCreditos());
								}else {
									creditosReprobados = creditosReprobados +  establecerNumeroCreditos(materia, matriculas);
								}
							}
						}

						// calcular el 30 % de creditos
						BigDecimal limitePorcentaje = calcularTreintaPorCientoMalla(Long.valueOf(carrera.getMlcrnvMallaCurricularDto().getMlcrTotalCreditos()));
						if (new BigDecimal(creditosReprobados).floatValue() >= limitePorcentaje.floatValue()) {
							retorno = true;
							creditosReprobados = 0;
						}else {
							creditosReprobados = 0;
						}

					}

					if (retorno) {
						FacesUtil.mensajeInfo("La cantidad de créditos reprobados es mayor o igual al 30% del total de la Malla Curricular por ello No Aplica Gratuidad.");
					}

				}

			}

		} catch (GratuidadException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		try {
			servNpfUsuarioRolServicio.buscarXUsuarioXrol(fgmpgfUsuario.getUsrId(), RolConstantes.ROL_ESTUDIANTEPOSGRADO_VALUE);
			fgmpgfEstudiantePosgrado = true;
			retorno = true;
		} catch (UsuarioRolException e) {
		} catch (UsuarioRolNoEncontradoException e) {
		}

		return retorno;
	}
	
	/**
	 * Método que permite calcular el 30% de Creditos de la Malla Curricular
	 * @param totalHorasPorSemana - horas por semana total
	 * @return 30% de horas por semana
	 */
	private BigDecimal calcularTreintaPorCientoMalla(Long totalHorasPorSemana) {
		MathContext mc = new MathContext(6);
		BigDecimal divisor = new BigDecimal("100");
		BigDecimal constante = new BigDecimal("30");
		constante = constante.multiply(new BigDecimal(totalHorasPorSemana,mc));
		return establecerNumeroSinAproximacion(constante.divide(divisor,mc),2);
	}

	
	/**
	 * Método que permite setear dos cifras despues del punto decimal, sin incremetar al inmediato superior.
	 * @param numero - BigDEcimal mayor a 0.00
	 * @param digitos - cantidad de decimales requeridos.
	 * @return Bigdecimal con Formato ##.##
	 */
	public static BigDecimal establecerNumeroSinAproximacion(BigDecimal numero, int digitos){
		BigInteger entero = BigInteger.ZERO;BigInteger decimal = BigInteger.ZERO;
		MathContext mc = new MathContext(6);
		
		entero = numero.toBigInteger();
		BigDecimal decimas = numero.remainder(BigDecimal.ONE, mc);
		decimal = decimas.multiply(BigDecimal.TEN.pow(digitos)).toBigInteger();
		
		return new BigDecimal(entero+"."+setearMascara(decimal));
	}
	
	/**
	 * Método que permite dar mascara al decimal obtenido.
	 * @param number - entero tipo BigInteger
	 * @return entero con mascara ##
	 */
	private static String setearMascara(BigInteger number) {
		return String.format("%02d", number);
	}


	/**
	 * Método que permite establecer el numero de matricula de las asignaturas del SAU especialmente.
	 * Si registra un Aprobado con tercera o segunda verificar que se encuentren los dos registros anteriores.
	 */
	private int establecerNumeroCreditos(RecordEstudianteDto materia, List<RecordEstudianteDto> matriculas) {
		int retorno = 0;

		int repeticiones = 0;
		for (RecordEstudianteDto item : matriculas) {// busco las veces que reprobo
			if (item.getRcesMateriaDto().getMtrCodigo().equals(materia.getRcesMateriaDto().getMtrCodigo())
					&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
				repeticiones = repeticiones + 1;
			}
		}

		if (!(repeticiones == (materia.getRcesMateriaDto().getNumMatricula()-1))) {
			retorno = materia.getRcesMateriaDto().getMtrCreditos() * (materia.getRcesMateriaDto().getNumMatricula()-1);
		}

		return retorno;
	}


	@SuppressWarnings("rawtypes")
	private boolean verificarSiEstudianteEgreso(){
		boolean retorno = false;
		List<RecordEstudianteDto> matriculas = fgmpgfListHistorialAcademicoPregradoRecordEstudianteDto;

		List<String> auxListCarreras = new ArrayList<>();
		if (!matriculas.isEmpty()) {
			Set<CarreraDto> carreras = matriculas.stream().collect(Collectors.groupingBy(RecordEstudianteDto::getRcesCarreraDto)).keySet();
			if (!carreras.isEmpty()) {
				Iterator it = carreras.iterator();
				while (it.hasNext()) {
					CarreraDto item = (CarreraDto) it.next();
					auxListCarreras.add(String.valueOf(item.getCrrId()));
				}
			}

			//lista de malla curricular nivel -> creditos acumulados por Malla
			List<MallaCurricularNivelDto> creditosAcumuladosPorMalla = new ArrayList<>();
			try {
				if (!auxListCarreras.isEmpty()) {
					creditosAcumuladosPorMalla = servJdbcMallaCurricularNivel.buscarCreditosPorMalla(auxListCarreras);	
				}
			} catch (MallaCurricularNivelException e1) {
			} catch (MallaCurricularNivelNoEncontradoException e1) {
			}

			//Si la sumatoria de creditos aprobados es igual a el total de horas registrados en malla curricular es egresado.
			if (!creditosAcumuladosPorMalla.isEmpty()) {

				int creditosAprobados = 0;
				for (MallaCurricularNivelDto carrera : creditosAcumuladosPorMalla) {

					if (carrera.getMlcrnvCarreraDto().getCrrId() != CarreraConstantes.CARRERA_MEDICINA_VALUE
							|| carrera.getMlcrnvCarreraDto().getCrrId() != CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE
							|| carrera.getMlcrnvCarreraDto().getCrrId() != CarreraConstantes.CARRERA_ENFERMERIA_DISENO_VALUE
							|| carrera.getMlcrnvCarreraDto().getCrrId() != CarreraConstantes.CARRERA_OBSTETRICIA_DISENO_VALUE) {
						for (RecordEstudianteDto materia : matriculas) {
							if (carrera.getMlcrnvCarreraDto().getCrrId() == materia.getRcesCarreraDto().getCrrId()) {
								if (materia.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) 
										||	materia.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)
										||	materia.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) 
										||	materia.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)) {
									creditosAprobados = creditosAprobados + materia.getRcesMateriaDto().getMtrCreditos();
								}
							}
						}

						if (creditosAprobados == carrera.getMlcrnvMallaCurricularDto().getMlcrTotalCreditos().intValue()) {
							retorno = true;
						}
					}else {
						retorno = true;
						break;
					}

				}

			}

		}

		return retorno;
	}
	
	
	private boolean verificarCronogramaSuficienciaInformatica(int modalidad){

		boolean verificar =  false; 
		int periodo = GeneralesConstantes.APP_ID_BASE;

		CronogramaActividadJdbcDto cronograma = null;
		fgmpgfPeriodoAcademico = new PeriodoAcademico();

		switch (modalidad) {
		case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
			periodo = PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE;
			cronograma = cargarPlanificacionCronogramaGeneralMatriculas(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE);
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
			periodo = PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE;
			cronograma = cargarPlanificacionCronogramaGeneralMatriculas(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_INTENSIVOS_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE);
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
			periodo = PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE;
			cronograma = cargarPlanificacionCronogramaGeneralMatriculas(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_REGULARES_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_VALUE);
			break;
		}
		
		
		if(cronograma != null){
			if (cronograma.getPlcrFechaInicio() != null && cronograma.getPlcrFechaFin() != null) {
				if(cronograma.getPlcrFechaFin().after(GeneralesUtilidades.getFechaActualSistemaTimestamp())){
					if(cronograma.getPlcrFechaInicio().before(GeneralesUtilidades.getFechaActualSistemaTimestamp())){ 
						verificar = true;
						fgmpgfPlanificacionCronograma = new PlanificacionCronograma();
						fgmpgfPlanificacionCronograma.setPlcrId(cronograma.getPlcrId());	
					}
				}
			}
		}

		if (verificar) {
			try {
				PeriodoAcademicoDto periodoDto = servJdbcPeriodoAcademicoDto.buscar(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, periodo);
				fgmpgfPeriodoAcademico.setPracId(periodoDto.getPracId());
				fgmpgfPeriodoAcademico.setPracDescripcion(periodoDto.getPracDescripcion());
				fgmpgfPeriodoAcademico.setPracTipo(periodoDto.getPracTipo());
				FacesUtil.limpiarMensaje();
			} catch (PeriodoAcademicoDtoJdbcException e) {
			} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			}
		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró cronograma para la Modalidad seleccionada.");			
		}

		return verificar;
	
	}
	
	
	/**
	 * Metodo de verificacion de disponibilidad de la materia en el paralelo seleccionado para idiomas y cultura fisica.
	 */
	public void verificarClickDisponibilidadSuficiencias(MateriaDto item, Boolean seleccion){

		if(item.getPrlId() != GeneralesConstantes.APP_ID_BASE){
			
			
			if (verificarExisteHorarioAsignado(item)) { 
				if(verificarCupos(item.getMlcrmtId(), item.getPrlId(),fgmpgfPeriodoAcademico.getPracId() , true)){
					if (verificarCruceHorarioSuficienciaPregrado(item, fgmpgfListMatriculasPregradoRecordEstudianteDto)) { 
						item.setPrlId(GeneralesConstantes.APP_ID_BASE);
					}
				}else{
					item.setPrlId(GeneralesConstantes.APP_ID_BASE);
				}
			}else {
				item.setPrlId(GeneralesConstantes.APP_ID_BASE);
			}
			
			
			
		}else{
			item.setPrlId(GeneralesConstantes.APP_ID_BASE);
		}
		
	}
	
	
	/**
	 * Metodo de verificacion de disponibilidad de la materia en el paralelo seleccionado para informatica.
	 */
	public void verificarDisponibilidadSuficienciaInformatica(MateriaDto item){

		if(item.getPrlId() != GeneralesConstantes.APP_ID_BASE){
			MallaCurricularParalelo paralelo = cargarDatosParalelo(item.getPrlId());
			if (paralelo != null && paralelo.getMlcrprNivelacionCrrId() != null &&  paralelo.getMlcrprNivelacionCrrId() != 0) {
				if (!fgmpgfListMatriculasPregradoRecordEstudianteDto.isEmpty()) {
					if (fgmpgfListMatriculasPregradoRecordEstudianteDto.get(fgmpgfListMatriculasPregradoRecordEstudianteDto.size()-1).getRcesDependenciaDto().getDpnId() == paralelo.getMlcrprNivelacionCrrId()) {
						if (verificarExisteHorarioAsignado(item)) { 
							if(verificarCupos(item.getMlcrmtId(), item.getPrlId(),fgmpgfPeriodoAcademico.getPracId() , true)){
								if (verificarCruceHorarioSuficienciaPregrado(item, fgmpgfListMatriculasPregradoRecordEstudianteDto)) { 
									item.setPrlId(GeneralesConstantes.APP_ID_BASE);
								}
							}else{
								item.setPrlId(GeneralesConstantes.APP_ID_BASE);
							}
						}else {
							item.setPrlId(GeneralesConstantes.APP_ID_BASE);
						}
					}else {
						item.setPrlId(GeneralesConstantes.APP_ID_BASE);
						FacesUtil.mensajeInfo("Paralelo de uso particular, intente con otro.");
					}
				}else {
					item.setPrlId(GeneralesConstantes.APP_ID_BASE);
					FacesUtil.mensajeInfo("Paralelo de uso particular, intente con otro.");
				}
			}else {
				if (verificarExisteHorarioAsignado(item)) { 
					if(verificarCupos(item.getMlcrmtId(), item.getPrlId(),fgmpgfPeriodoAcademico.getPracId() , true)){
						if (verificarCruceHorarioSuficienciaPregrado(item, fgmpgfListMatriculasPregradoRecordEstudianteDto)) { 
							item.setPrlId(GeneralesConstantes.APP_ID_BASE);
						}
					}else{
						item.setPrlId(GeneralesConstantes.APP_ID_BASE);
					}
				}else {
					item.setPrlId(GeneralesConstantes.APP_ID_BASE);
				}
			}
			
		}else{
			item.setPrlId(GeneralesConstantes.APP_ID_BASE);
		}
		
	}
	


	private MallaCurricularParalelo cargarDatosParalelo(Integer prlId) {
		MallaCurricularParalelo retorno = new MallaCurricularParalelo();
		
			try {
				retorno = servFgmpfMallaCurricularParaleloServicio.buscarPorParalelo(prlId);
			} catch (MallaCurricularParaleloNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MallaCurricularParaleloValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MallaCurricularParaleloException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

		return retorno;
	}
	

	public void buscarParalelosPorModalidad(MateriaDto materia, int modalidad) {

		if (verificarCronogramaSuficienciaInformatica(modalidad)) {
			fgmpgfProcesoFlujo = new CronogramaActividadJdbcDto();
			
			switch (modalidad) {
			case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
				fgmpgfProcesoFlujo.setPrlfId(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_VALUE);
				//	if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, NivelConstantes.NIVEL_PRIMERO_VALUE, fgmpgfProcesoFlujo.getPrlfId(), CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE, true)){
				materia.setNumMatricula(calcularNumeroMatriculaInformatica(modalidad));
				materia.setMtrListParalelo(cargarParalelosPorModalidad(modalidad, materia.getMtrId()));
				materia.setMtrCmbEstadoDisable(Boolean.FALSE);
				break;
			case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
				fgmpgfProcesoFlujo.setPrlfId(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_INTENSIVOS_VALUE);
				materia.setNumMatricula(calcularNumeroMatriculaInformatica(modalidad));
				materia.setMtrListParalelo(cargarParalelosPorModalidad(modalidad, materia.getMtrId()));
				materia.setMtrCmbEstadoDisable(Boolean.FALSE);
				break;
			case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
				fgmpgfProcesoFlujo.setPrlfId(ProcesoFlujoConstantes.PRFL_INFORMATICA_MATRICULA_MODALIDAD_REGULARES_VALUE);
				materia.setNumMatricula(calcularNumeroMatriculaInformatica(modalidad));
				materia.setMtrListParalelo(cargarParalelosPorModalidad(modalidad, materia.getMtrId()));
				materia.setMtrCmbEstadoDisable(Boolean.FALSE);
				break;
			default:
				materia.setNumMatricula(0);
				materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
				materia.setMtrCmbEstadoDisable(Boolean.TRUE);
				materia.setMtrListParalelo(null);
				break;

			}

		}else {
			materia.setNumMatricula(0);
			materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
			materia.setMtrCmbEstadoDisable(Boolean.TRUE);
			materia.setMtrListParalelo(null);
		}
	}
	
	private List<ParaleloDto> cargarParalelosPorModalidad(int modalidad, int materiaId){
		List<ParaleloDto> retorno = null;

		try {
			retorno = servFgmpgfParaleloDto.buscarParalelosPorModalidad(modalidad, materiaId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Paralelos en la Modalidad seleccionada.");
		}

		return retorno;
	}
	
	/**
	 * Método que permite calcular el número de matricula.
	 *@return numero matricula.
	 */
	private int calcularNumeroMatriculaInformatica(int modalidad){
		int retorno = 1;
		
		if (!fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto.isEmpty()) {
			for (RecordEstudianteDto item : fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto) {
				if ((item.getRcesEstadoLabel().equals(SuficienciaInformaticaConstantes.RCES_EXONERACION_ESTADO_REPROBADO_LABEL) && item.getRcesModalidadDto().getMdlId()==modalidad)
						|| (item.getRcesEstadoLabel().equals(SuficienciaInformaticaConstantes.RCES_PRESENCIAL_ESTADO_REPROBADO_LABEL) && item.getRcesModalidadDto().getMdlId()==modalidad) 
						|| (item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && item.getRcesModalidadDto().getMdlId()==modalidad)) {
					retorno = retorno + 1;
				} 
			}
		}

		return retorno;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	/**
	 * Método que permite verificar si esta asignado un nivel y si exite cronogrma habilitado.
	 * @return true - todo ok.
	 */
	private boolean verificarCronogramaPorNivelUbicacion(){
		boolean retorno = true;
		
		if(fgmpgfNivelUbicacion != null){
			if(!verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
				retorno = false; 
			}
		}else{ 
			retorno = false; 
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite verificar si el estudiante tiene aprobado hasta el A2.1
	 * @param recordIdioma - recor academico del idioma.
	 * @return true si aplica al plan contingencia
	 */
	private boolean verificarNivelMatricula(List<MateriaDto> recordIdioma) {
		boolean retorno = false;
		
		for (MateriaDto item : recordIdioma) {
			if (item.getNvlNumeral().intValue() == NivelConstantes.NIVEL_TERCER_VALUE && item.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
				retorno = true;
			}
		}
		
		return retorno;
	}


	/**
	 * Método que permite verificar si el estudiante cumple con los requisitos para matricularse en PLAN CONTINGENCIA.
	 * @param fichasMatricula - matriculas del periodo vigente - pregrado
	 * @return true si aplica al plan de contingencia.
	 */
	private boolean verificarContingenciaNivelMatricula(List<RecordEstudianteDto> fichasMatricula) {
		boolean retorno  = false;
		
		for (RecordEstudianteDto item : fichasMatricula) {
			if (item.getRcesDependenciaDto().getDpnId() == DependenciaConstantes.DPN_CIENCIAS_MEDICAS_VALUE
				|| item.getRcesDependenciaDto().getDpnId() ==  DependenciaConstantes.DPN_ODONTOLOGIA_VALUE
				|| item.getRcesDependenciaDto().getDpnId() ==  DependenciaConstantes.DPN_CIENCIAS_ECONOMICAS_VALUE
				|| item.getRcesDependenciaDto().getDpnId() ==  DependenciaConstantes.DPN_INGENIERIA_GEOLOGIA_MINAS_PETROLEOS_AMBIENTAL_VALUE
				|| item.getRcesDependenciaDto().getDpnId() ==  DependenciaConstantes.DPN_CIENCIAS_ADMINISTRATIVAS_VALUE
				|| item.getRcesDependenciaDto().getDpnId() ==  DependenciaConstantes.DPN_CIENCIAS_AGRICOLAS_VALUE
				|| item.getRcesDependenciaDto().getDpnId() ==  DependenciaConstantes.DPN_FILOSOFIA_LETRAS_CIENCIAS_EDUCACION_VALUE) {
				fgmsfModalidadOnLine = true;
				fgmsfDependenciaId = item.getDpnId();
			}
				
			if (item.getCrrId() == CarreraConstantes.CARRERA_ENFERMERIA_DISENO_VALUE
					|| item.getRcesCarreraDto().getCrrId() == CarreraConstantes.CARRERA_ENFERMERIA_REDISENO_VALUE
					|| item.getRcesCarreraDto().getCrrId() == CarreraConstantes.CARRERA_OBSTETRICIA_DISENO_VALUE
					|| item.getRcesCarreraDto().getCrrId() == CarreraConstantes.CARRERA_OBSTETRICIA_REDISENO_VALUE
					|| item.getRcesCarreraDto().getCrrId() == CarreraConstantes.CARRERA_TRABAJO_SOCIAL_VALUE) {		
				if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().intValue() > NivelConstantes.NIVEL_TERCER_VALUE) {
					retorno = true;
				}
			}else {
				if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().intValue() > NivelConstantes.NIVEL_QUINTO_VALUE) {
					retorno = true;
				}
			}
		}
		
		return retorno;
	}

	
//	private boolean verificarContingenciaNivelMatricula(List<FichaMatriculaDto> fichasMatricula) {
//		boolean retorno  = false;
//		
//		for (FichaMatriculaDto item : fichasMatricula) {
//			if (item.getDpnId().intValue() == DependenciaConstantes.DPN_CIENCIAS_MEDICAS_VALUE
//				|| item.getDpnId().intValue() ==  DependenciaConstantes.DPN_ODONTOLOGIA_VALUE
//				|| item.getDpnId().intValue() ==  DependenciaConstantes.DPN_CIENCIAS_ECONOMICAS_VALUE
//				|| item.getDpnId().intValue() ==  DependenciaConstantes.DPN_INGENIERIA_GEOLOGIA_MINAS_PETROLEOS_AMBIENTAL_VALUE
//				|| item.getDpnId().intValue() ==  DependenciaConstantes.DPN_CIENCIAS_ADMINISTRATIVAS_VALUE
//				|| item.getDpnId().intValue() ==  DependenciaConstantes.DPN_CIENCIAS_AGRICOLAS_VALUE
//				|| item.getDpnId().intValue() ==  DependenciaConstantes.DPN_FILOSOFIA_LETRAS_CIENCIAS_EDUCACION_VALUE) {
//				fgmsfModalidadOnLine = true;
//				fgmsfDependenciaId = item.getDpnId();
//			}
//				
//			if (item.getCrrId() == CarreraConstantes.CARRERA_ENFERMERIA_DISENO_VALUE
//					|| item.getCrrId() == CarreraConstantes.CARRERA_ENFERMERIA_REDISENO_VALUE
//					|| item.getCrrId() == CarreraConstantes.CARRERA_OBSTETRICIA_DISENO_VALUE
//					|| item.getCrrId() == CarreraConstantes.CARRERA_OBSTETRICIA_REDISENO_VALUE
//					|| item.getCrrId() == CarreraConstantes.CARRERA_TRABAJO_SOCIAL_VALUE) {		
//				if (item.getFcmtNivelUbicacion().intValue() > NivelConstantes.NIVEL_TERCER_VALUE) {
//					retorno = true;
//				}
//			}else {
//				if (item.getFcmtNivelUbicacion().intValue() > NivelConstantes.NIVEL_QUINTO_VALUE) {
//					retorno = true;
//				}
//			}
//		}
//		
//		return retorno;
//	}

//	private List<FichaMatriculaDto> cargarFichasMatriculaPregrado() {
//		PeriodoAcademico periodo = null;
//
//		try {
//			periodo = cargarPeriodoAcademicoPorTipoEstado(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			if (periodo != null) {
//				return servFgmpgfFichaMatriculaDto.buscarFichasMatricula(fgmpgfUsuario.getUsrIdentificacion(), periodo.getPracId());
//			}else {
//				return null;	
//			}
//		} catch (FichaMatriculaNoEncontradoException e) {
//			FacesUtil.mensajeError("El estudiante no dispone de una matrícula activa en el período actual.");
//			return null;
//		} catch (FichaMatriculaException e) {
//			return null;
//		}
//	}
	
//	/**
//	 * Método que permite buscar un periodo academico 
//	 * @param tipoPeriodo
//	 * @param estadoPeriodo
//	 * @return PeriodoAcademico
//	 */
//	private PeriodoAcademico cargarPeriodoAcademicoPorTipoEstado(int tipoPeriodo, int estadoPeriodo){
//		try {
//			return servFgmpgfPeriodoAcademico.buscarXestadoXtipoPeriodo(estadoPeriodo, tipoPeriodo);
//		} catch (PeriodoAcademicoNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//			return null;
//		} catch (PeriodoAcademicoException e) {
//			return null;
//		}
//	}




	/**
	 * Verifica si se encuentra matriculado en algun tipo de periodo academico de la SHI.
	 * @param identificacion - cedula o pasaporte
	 * @return true - si tiene alguna matricula activa en la suficiencia.
	 */
	private boolean verificarMatriculaSuficienciaInformatica(String identificacion) {
		boolean retorno = false;
		
		try {
			List<FichaMatriculaDto> matriculas = servJdbcSuficienciaInformatica.buscarMatriculasActivas(identificacion, new Integer[]{PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE});
			if (!matriculas.isEmpty()) {
				retorno = true;
			}
		} catch (FichaMatriculaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		FacesUtil.limpiarMensaje();
		return retorno;
	}

	

	
	

	/**
	 * Método que permite filtrar el historial academico de Cultura Fisica segun normativa aprobada por HCU.
	 * @param historialCulturaFisica - todo el historial de esta persona en la suficiencia en cultura fisica. 
	 * @return solo lo que sirve para el las nuevas matriculas.
	 */
	private List<RecordEstudianteDto> filtrarNuevasCondiciones(List<RecordEstudianteDto>  historialCulturaFisica){
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		for (RecordEstudianteDto item : historialCulturaFisica) {
			if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SAU)) {
				if (item.getRcesCarreraDto().getCrrEspeCodigo().equals(SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA)) {
					retorno.add(item);
				}
			}else {
				retorno.add(item);
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Método que permite cargar el record del estudiante segun la ficha inscripcion.
	 * @return matrias aprobadas/reprobadas que registra en el sistema.
	 */
	private List<MateriaDto> cargarRecordAcademicoIdiomaSeleccionado(){ 

		try {
			return servJdbcMateriaDto.buscarRecordEstudianteXidentificacionXcarrera(fgmpgfUsuario.getUsrIdentificacion(), fgmpgfCarreraSeleccion.getCrrId());
		} catch (MateriaDtoException e) {
			return new ArrayList<>();
		}

	}	
	
		

	
	private final int UBICACION_MATRICULA_NORMAL = 0;
	private final int UBICACION_MATRICULA_CONTINGENCIA = 1;
	/**
	 * Método que permite cargar las materias a matricular.
	 * @param recordIdioma - record del estudiante.
	 * @param tipo -> {0- normal , 1 - todas}
	 * @return materias a matricular.
	 */
	private List<MateriaDto> cargarMateriasAMatricularse(List<MateriaDto> recordIdioma, int tipo){
		fgmpgfEstudianteNuevo = false;
		
		List<MateriaDto> retorno = null;
		boolean continuar = true;

		List<MateriaDto> listMateriaAprobadas = new ArrayList<>();
		List<MateriaDto> listMateriaReprobadas = new ArrayList<>();


		//Materias aprobadas / reprobadas
		listMateriaAprobadas = cargarMateriasAprobadas(fgmpgfListRecordEstudiante);
		listMateriaReprobadas = cargarMateriasReprobadas(fgmpgfListRecordEstudiante);

		// quitar las aprobadas de la malla
		List<MateriaDto> materiasAMatricularse =  null;
		if (listMateriaAprobadas != null && listMateriaAprobadas.size() > 0) {
			materiasAMatricularse = quitarMateriasReprobadasQueAprobo(fgmpgfListMallaCurricularMateria, listMateriaAprobadas);	
		}else {
			materiasAMatricularse = fgmpgfListMallaCurricularMateria;
		}

		for (MateriaDto item : materiasAMatricularse) {
			item.setNumMatricula(calcularNumeroMatricula(listMateriaReprobadas, item));
			item.setMtrCmbEstado(true);
			
			if(item.getNumMatricula() >= 4){ // SI TIENE MATRICULA > 3 COBRAR 60 HASTA QUE FINALICE EL NIVEL
				continuar  = false;
				break;
			}

		}


		if (continuar) {

			if(materiasAMatricularse != null && materiasAMatricularse.size() > 0){
				materiasAMatricularse.sort(Comparator.comparing(MateriaDto::getNvlNumeral).reversed());
				
				if (tipo == UBICACION_MATRICULA_NORMAL) {
					// una sola hasta el A2.2
					Iterator<MateriaDto> itera = materiasAMatricularse.iterator();
					while(itera.hasNext()){
						MateriaDto cad = (MateriaDto) itera.next();
						if(cad.getNvlId() != NivelConstantes.NIVEL_APROBACION_VALUE){

							try{
								cad.setMtrCmbEstado(true);
								cad.setNumMatricula(calcularNumeroMatricula(listMateriaReprobadas, cad));
								cad.setMtrListParalelo(servFgmpgfParaleloDto.ListarXMateriaId(cad.getMtrId()));
							} catch (ParaleloDtoException e) {
							} catch (ParaleloDtoNoEncontradoException e) {
//								FacesUtil.mensajeError(e.getMessage() + " : " + cad.getMtrDescripcion());
							}							

						}else {
							itera.remove();
						}
					}
					
					if (materiasAMatricularse.size() > 0) {
						Optional<MateriaDto> materia = materiasAMatricularse.stream().min(Comparator.comparing(MateriaDto::getNvlNumeral));
						retorno = new ArrayList<>();
						retorno.add(materia.get());
						fgmpgfNivelUbicacion = materia.get().getNvlNumeral();
					}
					
				}else if(tipo == UBICACION_MATRICULA_CONTINGENCIA){// solo la materia de aprobacion -> ya no todas hasta el A2.2
					
					Iterator<MateriaDto> itera = materiasAMatricularse.iterator();
					while(itera.hasNext()){
						MateriaDto cad = (MateriaDto) itera.next();
						if(cad.getNvlId() == NivelConstantes.NIVEL_APROBACION_VALUE){
							cad.setMtrCmbEstado(true);
							cad.setNumMatricula(calcularNumeroMatricula(listMateriaReprobadas, cad));

							try{
								List<ParaleloDto> paralelos = servFgmpgfParaleloDto.ListarXMateriaId(cad.getMtrId());
								
								if (paralelos != null && paralelos.size() > 0) {
									Iterator<ParaleloDto> itParalelo = paralelos.iterator();
									while(itParalelo.hasNext()){
										ParaleloDto item = (ParaleloDto) itParalelo.next();
										if (fgmsfModalidadOnLine) {
											if (fgmsfDependenciaId.intValue() != DependenciaConstantes.DPN_CIENCIAS_ADMINISTRATIVAS_VALUE) {
												if (item.getMlcrprModalidad().intValue() != ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE) {
													itParalelo.remove();
												}
											}
										}else {
											if (item.getMlcrprModalidad().intValue() == ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE) {
												itParalelo.remove();
											}	
										}

									}
								}

								cad.setMtrListParalelo(paralelos);
								fgmpgfNivelUbicacion = cad.getNvlNumeral();
								
							} catch (ParaleloDtoException e) {
							} catch (ParaleloDtoNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage() + " : " + cad.getMtrDescripcion());
							}							

						}else {
							itera.remove();
						}

					}
					
					if (materiasAMatricularse.size() > 0) {
						retorno = materiasAMatricularse;	
					}
				}
				
				
				return retorno;
				
			}else{				
				FacesUtil.mensajeInfo("No se encontraron materias a matricularse. Usted ha culminado la malla curricular de la carrera "+fgmpgfCarreraSeleccion.getCrrDetalle()+".");
				return retorno;
			}
		}else {
			FacesUtil.mensajeInfo("Usted ha reprobado el nivel por tercera vez, acérquese al Centro de Idiomas para volver a iniciar la Suficiencia.");
			return retorno;
		}

	}
	

	/**
	 * Método que permite calcular el numero de matricula.
	 * @param reprobadas - materias reprobadas
	 * @param item - materia seleccionada
	 * @return numero de matricula.
	 */
	private int calcularNumeroMatricula(List<MateriaDto> reprobadas, MateriaDto item){
		
		if (reprobadas != null && reprobadas.size() > 0) {
			
			reprobadas.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			int contMatricula = 1;
			
			for (MateriaDto itemRecord : reprobadas) {
				
				if (itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && itemRecord.getMtrCodigo().equals(item.getMtrCodigo())) {
					if (itemRecord.getNumMatricula() == 2 && itemRecord.getPracId() == PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE) {
						contMatricula = itemRecord.getNumMatricula();
						contMatricula++;
					}else {
						contMatricula++;	
					}
				}
				
			}
			return contMatricula;
		}else {
			return 1;			
		}

	}
	
	/**
	 * Método que  elimina las materias reprobadas que ya aprobo el estudiante.
	 * @param  materiasAprobadas
	 * @param  materiasReprobadas
	 * @return materias reprobadas que aun no aprueba.
	 */
	private List<MateriaDto> quitarMateriasReprobadasQueAprobo(List<MateriaDto> materiasReprobadas, List<MateriaDto> materiasAprobadas) {
		List<MateriaDto> reprobadas = new ArrayList<>();
		List<MateriaDto> aprobadas = new ArrayList<>();
		
		if(materiasReprobadas!=null && materiasAprobadas!=null){
			if (materiasReprobadas.size() > 0 ) {
				if (materiasAprobadas.size() > 0) {
					// quitar mas de uno
					Map<String, MateriaDto> mapReprobadas =  new HashMap<String, MateriaDto>();
					for (MateriaDto it : materiasReprobadas) {
						mapReprobadas.put(it.getMtrCodigo(), it);
					}
					
					for (Entry<String, MateriaDto> item : mapReprobadas.entrySet()) {
						reprobadas.add(item.getValue());
					}
					// quitar mas de uno
					Map<String, MateriaDto> mapAprobadas =  new HashMap<String, MateriaDto>();
					for (MateriaDto it : materiasAprobadas) {
						mapAprobadas.put(it.getMtrCodigo(), it);
					}
					for (Entry<String, MateriaDto> carrera : mapAprobadas.entrySet()) {
						aprobadas.add(carrera.getValue());
					}
					
					// eliminar las reprobadas que fueron aprobadas
					Iterator<MateriaDto> itAs = aprobadas.iterator();
					while (itAs.hasNext()) {
						Iterator<MateriaDto> itDb = reprobadas.iterator();
						String aux1 = itAs.next().getMtrCodigo();
						while (itDb.hasNext()) {
							String aux2 = itDb.next().getMtrCodigo();
							if (aux1.equals(aux2)) {
								itDb.remove();
							}
						}
					}
				}else {
					// porque no hay aprobadas
					return null;
				}
			}else{
				// por que no hay reprobadas
				return null;			
			}
		}else{
			//lanzar exception "Lista de materias reprobadas o aprobadas nula"
			return null;
		}
		return reprobadas;
	}		

	
	/**
	 * Método que permite cargar solo materias reprobadas
	 * @param recordEstudiantil - record academico de la carrera seleccionada para matricula.
	 * @return materias reprobadas.
	 */
	private List<MateriaDto> cargarMateriasReprobadas(List<MateriaDto> recordEstudiantil){
		List<MateriaDto> materiasReprobadas = recordEstudiantil.stream()
				.filter(itemRecord->itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL))
				.collect(Collectors.toList());
		return materiasReprobadas;
	}
		
	/**
	 * Método que permite cargar solo materias aprobadas, convalidadas y homologadas
	 * @param recordEstudiantil - record academico de la carrera seleccionada para matricula.
	 * @return materias reprobadas.
	 */
	private List<MateriaDto> cargarMateriasAprobadas(List<MateriaDto> recordEstudiantil){
		List<MateriaDto> materiasAprobadas = recordEstudiantil.stream().filter(itemRecord->
		itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) || 
		itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)||
		itemRecord.getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL) ||
		itemRecord.getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)).collect(Collectors.toList());
		return materiasAprobadas;
	}
	
	
	
	
	


	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		
		fgmpgfUsuario = null;
		fgmpgfListFichaInscripcionDto = null;
		fgmpgfFichaInscripcionDto = null;
		fgmpgfListRecordEstudiante = null;
		fgmpgfEstudianteNuevo = null;
		fgmpgfListMateriasAmatricularMateriaDto = null;
		fgmpgfListParaleloDto = null;
		fgmpgfPeriodoAcademico = null;
		fgmpgfPlanificacionCronograma = null;
		fgmpgfDependenciaBuscar = null;
		fgmpgfProcesoFlujo = null;
		fgmpgfCarrerId = null;
		fgmpgfPerdidaGratuidadDefinitiva = null;
		fgmpgfNivelUbicacion = null;
		fgmpgfMateriasMatriculadas = null;
		fgmpgfCarreraSeleccion = null;
		fgmpgfListMallaCurricularMateria = null;
		fgmpgfListArancel = null;
		
		
		fgmpgfListMateriasAmatricularMateriaDto = null;
		fgmpgfListHistorialMatriculasSuficienciaRecordEstudianteDto = null;
		fgmpgfListMatriculasPregradoRecordEstudianteDto = null;
		fgmpgfListHistorialAcademicoPregradoRecordEstudianteDto = null;
		
		
		FacesUtil.limpiarMensaje();
		
		return "irInicio";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String cancelarGenerarMatricula(){

		fgmpgfPeriodoAcademico = new PeriodoAcademico();
		fgmpgfListRecordEstudiante = null;
		fgmpgfEstudianteNuevo = new Boolean(false);
		fgmpgfListParaleloDto = null;
		fgmpgfListArancel = null;
		
		irInicio();
		
		return "cancelarMatricula";
	}

	
	// >>-------------------------------------------LIMPIEZA-------------------------------------------------
	
	
	
	
	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------
	
	
	private int contadorUnicoMatriculas = 0;
	
	/**
	 * Genera la matricula del estudiante.
	 */
public String generarMatricula(){
		
		if(!verificarNoMatriculado()){
			if(verificarCronogramaSemestreCarrera(3, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
				if(verificarSeleccion(true)){
					
					Boolean verificarCupos = false;
					Nivel nivel = null;

					try {

						List<MateriaDto> materiasMatricula = new ArrayList<>();
						for(MateriaDto item: fgmpgfListMateriasAmatricularMateriaDto){
							if(item.getMtrCmbEstado()){
								Paralelo paralelo = servFgmpgfParaleloServicio.buscarPorId(item.getPrlId());
								item.setPrlDescripcion(paralelo.getPrlDescripcion());

								if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
									if (fgmpgfPerdidaGratuidadDefinitiva) {
										item.setValorMatricula(BigDecimal.valueOf(60));	
									}else {
										item.setValorMatricula(BigDecimal.ZERO);
									}
								} else if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
									item.setValorMatricula(BigDecimal.ZERO);
								} else if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
									item.setValorMatricula(calcularValorAPagarPorSuficienciaInformatica());
								}

								materiasMatricula.add(item);

								if(!verificarCupos(item.getMlcrmtId(), item.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), true)){
									if(!verificarCupos){
										verificarCupos = true;
									}
								}								

							}
						}

						if(!verificarCupos){
							contadorUnicoMatriculas = contadorUnicoMatriculas+1;
						}
						
						if(!verificarCupos && contadorUnicoMatriculas == 1){
							
							fgmpgfMateriasMatriculadas = new ArrayList<>();
							fgmpgfMateriasMatriculadas.addAll(materiasMatricula);

							nivel = servFgmpgfNivelServicio.listarNivelXNumeral(fgmpgfNivelUbicacion);
							ComprobantePago numeroComprobante ;
							
							numeroComprobante = servFgmpgfMatriculaServicio.generarMatriculaSuficiencias(
									materiasMatricula, 
									fgmpgfFichaInscripcionDto, 
									fgmpgfEstudianteNuevo, 
									fgmpgfProcesoFlujo, 
									fgmpgfPlanificacionCronograma, 
									null, 
									nivel.getNvlId(), 
									fgmpgfPeriodoAcademico,
									materiasMatricula.get(0).getValorMatricula(),// Solo mando el valor a pagar
									calcularGratuidad(),//P
									fgmpgfListArancel
									);
							

							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.generar.matricula.exitoso")));
							
							contadorUnicoMatriculas = contadorUnicoMatriculas+1;
							
							return enviarMailRegistroMatricula(fgmpgfFichaInscripcionDto, nivel, numeroComprobante, fgmpgfMateriasMatriculadas);

						}else{  
							contadorUnicoMatriculas = 0;
							return null; 
						}

					}catch (ParaleloNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (ParaleloException e) {
						FacesUtil.mensajeError(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (NivelNoEncontradoException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (NivelException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (MatriculaValidacionException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (MatriculaException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					}  

				}
				if(!verificarSeleccion(false)){
					contadorUnicoMatriculas = 0;
					return null;
				}
			}else{
				contadorUnicoMatriculas = 0;
				return null;
			}
		}else{
			contadorUnicoMatriculas = 0;
			return "irInicio";
		}
		
		return "irInicio";
	}



public String generarMatriculaInfomatica(){

	if(!verificarMatriculaSuficienciaInformatica(fgmpgfFichaInscripcionDto.getPrsIdentificacion())){
		if(verificarSeleccion(true)){

			Boolean verificarCupos = false;
			Nivel nivel = null;

			try {

				List<MateriaDto> materiasMatricula = new ArrayList<>();
				for(MateriaDto item: fgmpgfListMateriasAmatricularMateriaDto){
					if(item.getMtrCmbEstado()){
						Paralelo paralelo = servFgmpgfParaleloServicio.buscarPorId(item.getPrlId());
						item.setPrlDescripcion(paralelo.getPrlDescripcion());

						if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
							if (fgmpgfPerdidaGratuidadDefinitiva) {
								item.setValorMatricula(BigDecimal.valueOf(60));	
							}else {
								item.setValorMatricula(BigDecimal.ZERO);
							}
						} else if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
							item.setValorMatricula(BigDecimal.ZERO);
						} else if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
							item.setValorMatricula(calcularValorAPagarPorSuficienciaInformatica());
						}

						materiasMatricula.add(item);

						if(!verificarCupos(item.getMlcrmtId(), item.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), true)){
							if(!verificarCupos){
								verificarCupos = true;
							}
						}								

					}
				}

				if(!verificarCupos){
					contadorUnicoMatriculas = contadorUnicoMatriculas+1;
				}

				if(!verificarCupos && contadorUnicoMatriculas == 1){

					fgmpgfMateriasMatriculadas = new ArrayList<>();
					fgmpgfMateriasMatriculadas.addAll(materiasMatricula);

					nivel = servFgmpgfNivelServicio.listarNivelXNumeral(fgmpgfNivelUbicacion);
					ComprobantePago numeroComprobante ;

					numeroComprobante = servFgmpgfMatriculaServicio.generarMatriculaSuficiencias(
							materiasMatricula, 
							fgmpgfFichaInscripcionDto, 
							fgmpgfEstudianteNuevo, 
							fgmpgfProcesoFlujo, 
							fgmpgfPlanificacionCronograma, 
							null, 
							nivel.getNvlId(), 
							fgmpgfPeriodoAcademico,
							materiasMatricula.get(0).getValorMatricula(),// Solo mando el valor a pagar
							calcularGratuidad(),//P
							fgmpgfListArancel
							);


					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.generar.matricula.exitoso")));

					contadorUnicoMatriculas = contadorUnicoMatriculas+1;

					return enviarMailRegistroMatricula(fgmpgfFichaInscripcionDto, nivel, numeroComprobante, fgmpgfMateriasMatriculadas);

				}else{  
					contadorUnicoMatriculas = 0;
					return null; 
				}

			}catch (ParaleloNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
				contadorUnicoMatriculas = 0;
				return "irInicio";
			} catch (ParaleloException e) {
				FacesUtil.mensajeError(e.getMessage());
				contadorUnicoMatriculas = 0;
				return "irInicio";
			} catch (NivelNoEncontradoException e) {
				FacesUtil.mensajeInfo(e.getMessage());
				contadorUnicoMatriculas = 0;
				return "irInicio";
			} catch (NivelException e) {
				FacesUtil.mensajeInfo(e.getMessage());
				contadorUnicoMatriculas = 0;
				return "irInicio";
			} catch (MatriculaValidacionException e) {
				FacesUtil.mensajeInfo(e.getMessage());
				contadorUnicoMatriculas = 0;
				return "irInicio";
			} catch (MatriculaException e) {
				FacesUtil.mensajeInfo(e.getMessage());
				contadorUnicoMatriculas = 0;
				return "irInicio";
			}  

		}
		if(!verificarSeleccion(false)){
			contadorUnicoMatriculas = 0;
			return null;
		}
	}else{
		contadorUnicoMatriculas = 0;
		return "irInicio";
	}

	return "irInicio";
}


// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------





/**
 * Carga las materias a inscribirse un estudiante nuevo en Educacion inicial o no.
 * @param fichaInscripcionDto .- Dto para la busqueda 
 * @return Materias a matricular.
 */
private List<MateriaDto> cargarMateriasAMatricularNuevo(){

	List<MateriaDto> retorno = null;
	List<MateriaDto> listMateriasAMatricularse = null;
	Optional<MateriaDto> materiaRetorno = Optional.empty();

	listMateriasAMatricularse = new ArrayList<>();
	listMateriasAMatricularse.addAll(fgmpgfListMallaCurricularMateria);

	if (listMateriasAMatricularse != null) {
		materiaRetorno = listMateriasAMatricularse.stream().min(Comparator.comparing(MateriaDto::getNvlNumeral));
		materiaRetorno.get().setMtrCmbEstado(true);
		materiaRetorno.get().setNumMatricula(SAUConstantes.PRIMERA_MATRICULA_VALUE);
		try{	
			fgmpgfListParaleloDto = servFgmpgfParaleloDto.ListarXMateriaId(materiaRetorno.get().getMtrId()); // lista de paralelos por la materia
			materiaRetorno.get().setMtrListParalelo(fgmpgfListParaleloDto);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	if (materiaRetorno.isPresent()) {
		retorno = new ArrayList<>();
		retorno.add(materiaRetorno.get());
		fgmpgfEstudianteNuevo = true;
	}



	return retorno;
}



	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	/**
	 * Verifica los parametros necesarios en ls elementos seleccionados
	 * @return valida o no para generar matricula
	 */
	public Boolean verificarSeleccion(Boolean mensaje){
		Boolean verificar = true;

		List<MateriaDto> materiasSeleccionadas = new ArrayList<>();
		
		if (fgmpgfListMateriasAmatricularMateriaDto != null && fgmpgfListMateriasAmatricularMateriaDto.size() > 0  ) {
			
			Integer numeroTotalCreditosHoras = 0;
			Boolean seleccionMaterias = false;
			MateriaDto paraleloNoSeleccionado = null;
			
			for (MateriaDto it : fgmpgfListMateriasAmatricularMateriaDto) {
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
			
			for (MateriaDto it : fgmpgfListMateriasAmatricularMateriaDto) {
				if (it.getMtrCmbEstado()) {
					if (it.getPrlId() != null && it.getPrlId() != GeneralesConstantes.APP_ID_BASE) {
						materiasSeleccionadas.add(it);
						if(it.getMtrCreditos() != null && it.getMtrCreditos() != 0){
							numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrCreditos();
						}
						if(it.getMtrHoras() != null && it.getMtrHoras() != 0){
							numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHoras();
						}
					}else{
						verificar = false;
						break;
					}
				}
			}
			
			if(!(numeroTotalCreditosHoras.intValue() <= fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue())){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.seleccion.seleccione.maximo.creditos.horas", fgmpgfCarreraSeleccion.getCrrNumMaxCreditos(), cabeceraCreditosHoras(fgmpgfListMateriasAmatricularMateriaDto.get(fgmpgfListMateriasAmatricularMateriaDto.size()-1).getMtrCreditos(), fgmpgfListMateriasAmatricularMateriaDto.get(fgmpgfListMateriasAmatricularMateriaDto.size()-1).getMtrHoras() ))));
				verificar = false;
			}
		}else{
			verificar = false;
		} 
		
		return verificar;
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
				List<Integer> cupos = servFgmpgfMatriculaServicioJdbc.buscarCuposAndMatriculados(mlcrmtID, prlId, pracId);

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
	 * Verifica cronogramas de matriculas para suficiencias.
	 * @param tipo - {5- idiomas, 6-cultura fisica}
	 * @return true - periodo para matriculas ACTIVO.
	 */
	public Boolean validarCronogramaSuficiencias(int tipo){
		Date fechaActual = new Date();
		Boolean verificar =  false; 
		
		try {
			CronogramaActividadJdbcDto fgmpgfFechasMatriculaOrdinaria = new CronogramaActividadJdbcDto();
			CronogramaActividadJdbcDto fgmpgfFechasMatriculaExtraordinaria = new CronogramaActividadJdbcDto();
			if (tipo == CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE) {
				fgmpgfFechasMatriculaOrdinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarCronograma(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE);
				fgmpgfFechasMatriculaExtraordinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarCronograma(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE);

			}else if (tipo == CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE) {
				fgmpgfFechasMatriculaOrdinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarCronograma(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE);
				fgmpgfFechasMatriculaExtraordinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarCronograma(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE, CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE);
				
			}
			
			Date fechaInicioMatriculasOrdinarias = new Date(fgmpgfFechasMatriculaOrdinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasOrdinarias = new Date(fgmpgfFechasMatriculaOrdinaria.getPlcrFechaFin().getTime());
			Date fechaInicioMatriculasExtraordinarias = new Date(fgmpgfFechasMatriculaExtraordinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasExtraordinarias = new Date(fgmpgfFechasMatriculaExtraordinaria.getPlcrFechaFin().getTime());


			if(!verificar){
				if(fechaFinMatriculasExtraordinarias.after(fechaActual)){
					FacesUtil.limpiarMensaje();
					if(fechaInicioMatriculasExtraordinarias.before(fechaActual)){ 
						verificar = true;
						FacesUtil.mensajeInfo("Periodo de matriculas extraordinarias.");
					}else{
						FacesUtil.mensajeError("Periodo de matriculas extraordinarias no ha iniciado.");
					}
				}else{
					FacesUtil.mensajeError("Periodo de matriculas extraordinarias ha expirado.");
				}
			}

			if(!verificar){
				if(fechaFinMatriculasOrdinarias.after(fechaActual)){
					FacesUtil.limpiarMensaje();
					if(fechaInicioMatriculasOrdinarias.before(fechaActual)){
						verificar = true;
					}else{
						FacesUtil.mensajeError("Periodo de matriculas ordinarias no ha iniciado.");
					}
				}else{
					FacesUtil.mensajeError("Periodo de matriculas ordinarias ha expirado.");
				}
			}


		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) { 
			FacesUtil.mensajeError("Registre los cronogramas para matriculas ordinarias y extraordinarias.");
		}

		return verificar;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private CronogramaActividadJdbcDto cargarPlanificacionCronogramaGeneralMatriculas(int proceso, int crnTipo) {
		CronogramaActividadJdbcDto retorno = null;
		
		try {
			retorno = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarCronograma(proceso, crnTipo);
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) { 
			FacesUtil.mensajeError("Registre cronogramas para Matrículas.");
		}

		return retorno;
	}

	
	
	
	
	
	
	
	

	public Boolean verificarCronogramaSemestreCarrera(int tipoApertura, int numeral, int procesoFlujo, int crrId, boolean mensaje){
		Boolean verificar = false;
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		try {
			CronogramaDto cronograma = servFgmpgfCronogramaDtoServicioJdbc.buscarCronogramaXTipoAperturaXNivelAperturaXprocesoFlujo(procesoFlujo, tipoApertura, numeral, crrId);
			  
			if(cronograma != null){
				if(cronograma.getPlcrFechaFin() != null && cronograma.getPlcrFechaInicio() != null){
					
					if(cronograma.getPlcrFechaFin().after(fechaActual)){
						if(cronograma.getPlcrFechaInicio().before(fechaActual)){ 
							verificar = true;
						}else{ 
							FacesUtil.mensajeError("El cronograma para "+cronograma.getCrnDescripcion()+" no ha empezado."); 
						}
					}else{ 
						FacesUtil.mensajeError("El cronograma para "+cronograma.getCrnDescripcion()+" ha expirado."); 
					}
				}else{
					FacesUtil.mensajeError("No se ha asignado cronograma para "+cronograma.getCrnDescripcion()+".");
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se ha asignado cronograma para matrículas.");
			}
			
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se ha asignado cronograma para matrículas.");
		}
		
		return verificar;
	}

	
	
	/**
	 * Verifica que el usuario no registre matricula vigente en este periodo
	 * @return true - si esta matriculado.
	 */
	private Boolean verificarNoMatriculado(){
		Boolean retorno = true;

		try {
			List<FichaMatriculaDto> matricula = servFgmpgfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarreraXsuficiencia(fgmpgfPeriodoAcademico.getPracId(), fgmpgfUsuario.getUsrIdentificacion(), fgmpgfFichaInscripcionDto.getCrrId());
			if (matricula != null && matricula.size()>0) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.matricula.generada.exception",fgmpgfFichaInscripcionDto.getCrrDetalle())));	
			}else {
				retorno = false;
			}
			
		} catch (FichaMatriculaException e) {
			retorno = false;
		}

		return retorno;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Calcula la perdida de gratuidad definitiva del estudiante
	 * @return retorna true si perdio la gratuidad definitiva.
	 */
	public Boolean calculoPerdidaGratuidadPorSegundoIdioma(){
		Boolean retorno = Boolean.FALSE;

		// POR OTRO IDIOMA
		if (fgmpgfFichaInscripcionDto.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_IDIOMA_ADICIONAL_VALUE) {
			retorno =  Boolean.TRUE;
			FacesUtil.mensajeInfo("Usted esta tomando un Idioma Adicional, por ello pierde gratuidad.");
		}
		
		return retorno;

	}
	
	
	
	

//		/**
//		 * Método que verifica el cruce de horas del item seleccionado con la matrícula que tiene vigente en el periodo activo.
//		 * @param materiaSeleccionada - materia que selecciona un paralelo.
//		 * @param matriculaActivaPregrado - materias tomadas por el estudiante en periodo activo.
//		 * @return true si hay algun cruce de horas.
//		 */
//		private boolean verificarCruceHorariosSuficiencias(MateriaDto materiaSeleccionada, List<MateriaDto> matriculaActivaPregrado) {
//			boolean retorno = false;
//			
//			List<HorarioAcademicoDto> horarioMateriaSeleccionada = null;
//			
//			try {
//				horarioMateriaSeleccionada = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(materiaSeleccionada.getPrlId(),materiaSeleccionada.getMlcrmtId(), fgmpgfPeriodoAcademico.getPracId());
//
//				if (horarioMateriaSeleccionada != null && horarioMateriaSeleccionada.size() > 0) {
//
//					if (matriculaActivaPregrado != null && matriculaActivaPregrado.size() > 0) {
//						
//						for (MateriaDto itemRecord : matriculaActivaPregrado) {
//
//							List<HorarioAcademicoDto> horarioPeriodoVigente = null;
//
//							try {
//								horarioPeriodoVigente = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(itemRecord.getPrlId(), itemRecord.getMlcrmtId(), itemRecord.getPracId());
//								if (horarioPeriodoVigente != null && horarioPeriodoVigente.size() > 0) {
//									if(itemRecord.getPrlId() != materiaSeleccionada.getPrlId() && materiaSeleccionada.getMlcrmtId() != itemRecord.getMlcrmtId()){
//										for (HorarioAcademicoDto hor1 : horarioPeriodoVigente) {
//											for (HorarioAcademicoDto hor2 : horarioMateriaSeleccionada) {
//												if (hor2.getHracDia().equals(hor1.getHracDia())) {
//													if ( hor2.getHoclHoInicio().equals(hor1.getHoclHoInicio()) && hor2.getHoclHoFin().equals(hor1.getHoclHoFin())) {
//														FacesUtil.mensajeError("El paralelo seleccionado genera cruce con el Horario de su matrícula de Pregrado.");
//														retorno = true;
//													}
//												}
//											}
//										}
//									}
//								}
//							
//							} catch (HorarioAcademicoDtoException e) {
//								retorno = true;
//								FacesUtil.mensajeError(e.getMessage());
//							} catch (HorarioAcademicoDtoNoEncontradoException e) {
//								retorno = true;
//								FacesUtil.mensajeError("El horario no ha sido cargado en la Asignatura "+itemRecord.getMtrDescripcion()+". Comuníquese con el Administrador de la Carrera de Grado.");
//							}
//						}
//						
//					}
//					
//				}
//
//
//			} catch (HorarioAcademicoDtoException e) {
//				retorno = true;
//				FacesUtil.mensajeError(e.getMessage());
//			} catch (HorarioAcademicoDtoNoEncontradoException e) {
//				retorno = true;
//				FacesUtil.mensajeError("El horario no ha sido cargado en la Asignatura "+materiaSeleccionada.getMtrDescripcion()+". Comuníque a la Secretaría de la Suficiencia.");
//			}
//			
//			return retorno;
//		}
		
		/**
		 * Método que verifica el cruce de horas del item seleccionado con la matrícula que tiene vigente en el periodo activo.
		 * @param materiaSeleccionada - materia que selecciona un paralelo.
		 * @param matriculaActivaPregrado - materias tomadas por el estudiante en periodo activo.
		 * @return true si hay algun cruce de horas.
		 */
		private boolean verificarCruceHorarioSuficienciaPregrado(MateriaDto materiaSeleccionada, List<RecordEstudianteDto> matriculaActivaPregrado) {
			boolean retorno = false;

			if (!matriculaActivaPregrado.isEmpty() && !fgmpgfEstudiantePosgrado) {

				if(!matriculaActivaPregrado.get(matriculaActivaPregrado.size()-1).getRcesFichaMatriculaDto().getCncrModalidad().equals(ModalidadConstantes.TIPO_MODALIDAD_DISTANCIA_VALUE)){
					
						List<HorarioAcademicoDto> horarioSeleccion = cargarHorarioAcademico(materiaSeleccionada); 
						if (!horarioSeleccion.isEmpty()) {

							for (RecordEstudianteDto itemRecord : matriculaActivaPregrado) {
								
								if (itemRecord.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE) ||
										itemRecord.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE)) {
									List<HorarioAcademicoDto> horarioMatriculaActiva = cargarHorarioAcademicoMatriculaActiva(itemRecord);
									if (!horarioMatriculaActiva.isEmpty()) {
										if(itemRecord.getRcesParaleloDto().getPrlId() != materiaSeleccionada.getPrlId() && materiaSeleccionada.getMlcrmtId() != itemRecord.getMlcrmtMtrId()){
											for (HorarioAcademicoDto hor1 : horarioMatriculaActiva) {
												for (HorarioAcademicoDto hor2 : horarioSeleccion) {
													if (hor2.getHracDia().equals(hor1.getHracDia())) {
														if ( hor2.getHoclHoInicio().equals(hor1.getHoclHoInicio()) && hor2.getHoclHoFin().equals(hor1.getHoclHoFin())) {
															FacesUtil.mensajeError("El paralelo seleccionado genera cruce con el Horario de su matrícula de Pregrado. " + itemRecord.getRcesMateriaDto().getMtrDescripcion());
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
		
		
		
		private List<HorarioAcademicoDto> cargarHorarioAcademicoMatriculaActiva(RecordEstudianteDto materia) {
			List<HorarioAcademicoDto> retorno =  new ArrayList<>();

			try {
				
				if (materia.getRcesMateriaDto().getMtrTpmtId() != TipoMateriaConstantes.TIPO_MODULAR_VALUE) {
					retorno = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(materia.getRcesParaleloDto().getPrlId(), materia.getMlcrmtMtrId(), materia.getRcesPeriodoAcademicoDto().getPracId());
				}else {
					List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(materia.getRcesMateriaDto().getMtrId());
					if (!modulos.isEmpty()) {
						List<HorarioAcademicoDto>  horarioModulo = null;
						for (MateriaDto modulo : modulos) {
							horarioModulo = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(materia.getRcesParaleloDto().getPrlId(), modulo.getMlcrmtId(), materia.getRcesPeriodoAcademicoDto().getPracId());
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
				FacesUtil.mensajeError("El Horario de Clases no ha sido cargado en la Asignatura "+materia.getRcesMateriaDto().getMtrDescripcion()+". Comuníque a la Secretaría de la Carrera.");
			}
			
			return retorno ;
		}


		private List<HorarioAcademicoDto> cargarHorarioAcademico(MateriaDto materia) {
			List<HorarioAcademicoDto> retorno =  new ArrayList<>();

			try {
				if (!materia.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
					retorno = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(materia.getPrlId(), materia.getMlcrmtId(), fgmpgfPeriodoAcademico.getPracId());
				}else {
					List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(materia.getMtrId());
					if (!modulos.isEmpty()) {
						List<HorarioAcademicoDto>  horarioModulo = null;
						for (MateriaDto modulo : modulos) {
							horarioModulo = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(materia.getPrlId(), modulo.getMlcrmtId(), fgmpgfPeriodoAcademico.getPracId());
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
		 * Método que veifica si exiten horarios asignados al paralelo.
		 * @param materia - materia elegida 
		 * @return true si existe horario.
		 */
		private boolean verificarExisteHorarioAsignado(MateriaDto materia) {
			boolean  retorno = true;
			try {
				List<HorarioAcademicoDto> horarioCarrito = null;
				if(materia.getMtrCmbEstado() != null && materia.getMtrCmbEstado() && materia.getPrlId()!= null && materia.getPrlId() != GeneralesConstantes.APP_ID_BASE){
					horarioCarrito = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(materia.getPrlId(), materia.getMlcrmtId(), fgmpgfPeriodoAcademico.getPracId());
				}
				if (horarioCarrito != null && horarioCarrito.size() > 0) {
					for (HorarioAcademicoDto it : horarioCarrito) {
						if (it.getHracDia() == null || it.getHoclHoInicio() == null || it.getHoclHoFin() == null) {
							FacesUtil.mensajeError("El horario no ha sido cargado en la asignatura "+it.getMtrDescripcion()+". Comuníque a la Secretaría de la Suficiencia.");
							retorno = false;
							break;
						}
					}
				}
			} catch (HorarioAcademicoDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
				retorno = false;
			} catch (HorarioAcademicoDtoNoEncontradoException e) {
				FacesUtil.mensajeError("El horario no ha sido cargado en la asignatura "+materia.getMtrDescripcion()+". Comuníque a la Secretaría de la Suficiencia.");
				retorno = false;
			}
			return retorno;
		}

		
		
	/**
	 * Calcula el valor a pagar por materria
	 * @param materia .- Materia a calcular
	 **/
		public BigDecimal calcularValorMateria(MateriaDto materia){

			BigDecimal valor = BigDecimal.ZERO;
			Arancel arancelXCobrar = new Arancel();

			try {

				Integer tipoArancel = null;
				if(materia.getMtrHorasCien() != null && materia.getMtrHorasCien() != 0){
					tipoArancel = ArancelConstantes.TIPO_ARANCEL_REDISENO_VALUE;
				}else{
					tipoArancel = ArancelConstantes.TIPO_ARANCEL_DISENO_VALUE;
				}
				List<Arancel> aranceles = servFgmpgfArancelServicio.listarXGratuidadXTipoMatriculaXModalidadXTipoArancel(calcularGratuidad().intValue(), fgmpgfProcesoFlujo.getPrlfId(), fgmpgfFichaInscripcionDto.getCncrModalidad(), tipoArancel);
				fgmpgfListArancel = new ArrayList<>();
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
					//Carreras RediseÃ±o
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
					//Carreras RediseÃ±o
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
					//Carreras RediseÃ±o
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
			return valor;
		}
	
	
 
	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/
	
	
	
	
	// >>----------------------------------OTROS_METODOS----------------------------------------
	
	
	/**
	 * Calcula la gratuidad de la matricula
	 * @return retorna el id de TipoGratuidad
	 */
	public Integer calcularGratuidad(){
		Integer retorno = null;

			if(fgmpgfPerdidaGratuidadDefinitiva){
				retorno = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_VALUE;
			}else{
				retorno = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_VALUE;				
			}
			 
		return retorno;
	}
	
	
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
		return "Error en el regimen.";
	}
	
	public String obtenerDescripcionGratuidad(boolean tipoGratuidad){

		String retorno = "";
		
		if (tipoGratuidad) {
			retorno = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_LABEL;
		}else {
			retorno = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_LABEL;			
		}
		
		return retorno;
	}
	
	/**
	 * Reinicia el valor del check y paralelo de la materia seleccionada
	 * @param materia .- materia a usar
	 */
	public void resetComboParalelo(MateriaDto materia) {
		materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
	}
	

	// ****************************************************************/
	// **************************** MAIL ******************************/
	// ****************************************************************/

	// >>----------------------------------OTROS_METODOS----------------------------------------
		
	
	/**
	 * Envia mail de registro de matricula 
	 *  @param fichaInscripcionDto .- ficha inscripcion a procesar
	 *  @param nivel .- nivel calculado
	 **/
	public String enviarMailRegistroMatricula(FichaInscripcionDto fichaInscripcionDto, Nivel nivel, ComprobantePago numeroComprobante, List<MateriaDto> listMateriaDto){

		try{

			StringBuilder sbNombres = new StringBuilder(); 
			sbNombres.append(fgmpgfUsuario.getUsrPersona().getPrsPrimerApellido());sbNombres.append(" ");
			sbNombres.append(fgmpgfUsuario.getUsrPersona().getPrsSegundoApellido());sbNombres.append(" ");
			sbNombres.append(fgmpgfUsuario.getUsrPersona().getPrsNombres());sbNombres.append(" ");

			Connection connection = null;
			Session session = null;
			MessageProducer producer = null;
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin","nio://10.20.1.64:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destino = session.createQueue("COLA_MAIL_DTO");

			// Creamos un productor
			producer = session.createProducer(destino); 

			//lista de correos a los que se enviara el mail
			List<String> correosTo = new ArrayList<>();
			correosTo.add(fgmpgfUsuario.getUsrPersona().getPrsMailInstitucional());

			//path de la plantilla del mail
			ProductorMailJson pmail = null;
			StringBuilder sbCorreo= new StringBuilder();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			
			sbCorreo= MailRegistroMatricula.generarMailRegistroMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
					GeneralesUtilidades.generaStringParaCorreo(sbNombres.toString()) , GeneralesUtilidades.generaStringParaCorreo(
					fgmpgfCarreraSeleccion.getCrrDescripcion()),
					GeneralesUtilidades.generaStringParaCorreo(nivel.getNvlDescripcion()),  
					fgmpgfDependenciaBuscar.getDpnDescripcion(),
					obtenerDescripcionGratuidad(fgmpgfPerdidaGratuidadDefinitiva),
					numeroComprobante.getCmpaTotalPago()==null? String.valueOf(0.00):numeroComprobante.getCmpaTotalPago().toString()
					);
			
			pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,MailConstantes.ENCABEZADO_CORREO_REGISTRO_MATRICULA_LABEL+fgmpgfPeriodoAcademico.getPracDescripcion().replace("-", " - "),
					sbCorreo.toString() , "admin", "dt1c201s", true, ReporteRegistroMatriculaForm.generarReporteRegistroMatriculaMail(listMateriaDto, nivel, fgmpgfPeriodoAcademico, fichaInscripcionDto, fgmpgfUsuario, fgmpgfDependenciaBuscar, fgmpgfCarreraSeleccion, obtenerDescripcionGratuidad(fgmpgfPerdidaGratuidadDefinitiva)), "Registro de Matricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
			String jsonSt = pmail.generarMail();
			Gson json = new Gson();
			MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
			// 	Iniciamos el envÃ­o de mensajes
			ObjectMessage message = session.createObjectMessage(mailDto);
			producer.send(message);

		} catch (Exception e) {
			System.out.println("Error Codigo: AX324 : Error al enviar el registro de matriculas por mail.");
		}

		return "irConfirmacionMatricula";
		
//		Se restringue el envio de la oreden de cobro por no generacion de comprobante hasta la generacion en la tarde.
//		return enviarMailOrdenCobro(fichaInscripcionDto, nivel, numeroComprobante, listMateriaDto);

	}
	
	
	/**
	 * Envia mail de registro de matricula 
	 *  @param fichaInscripcionDto .- ficha inscripcion a procesar
	 *  @param nivel .- nivel calculado
	 **/
	public String enviarMailOrdenCobro(FichaInscripcionDto fichaInscripcionDto, Nivel nivel, ComprobantePago numeroComprobante, List<MateriaDto> listMateriaDto){

		StringBuilder sbNombres = new StringBuilder(); 
		sbNombres.append(fgmpgfUsuario.getUsrPersona().getPrsPrimerApellido());sbNombres.append(" ");
		sbNombres.append(fgmpgfUsuario.getUsrPersona().getPrsSegundoApellido());sbNombres.append(" ");
		sbNombres.append(fgmpgfUsuario.getUsrPersona().getPrsNombres());sbNombres.append(" ");

		try{

			Connection connection = null;
			Session session = null;
			MessageProducer producer = null;
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin","nio://10.20.1.64:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destino = session.createQueue("COLA_MAIL_DTO");

			// Creamos un productor
			producer = session.createProducer(destino); 

			//lista de correos a los que se enviara el mail
			List<String> correosTo = new ArrayList<>();
			correosTo.add(fgmpgfUsuario.getUsrPersona().getPrsMailInstitucional());

			//path de la plantilla del mail
			ProductorMailJson pmail = null;
			StringBuilder sbCorreo= new StringBuilder();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
			String fecha = formato.format(new Date());

			sbCorreo= MailRegistroMatricula.generarMailRegistroMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
					GeneralesUtilidades.generaStringParaCorreo(sbNombres.toString()) , GeneralesUtilidades.generaStringParaCorreo(
					fgmpgfCarreraSeleccion.getCrrDescripcion()),
					GeneralesUtilidades.generaStringParaCorreo(nivel.getNvlDescripcion()),  
					fgmpgfDependenciaBuscar.getDpnDescripcion(),
					obtenerDescripcionGratuidad(fgmpgfPerdidaGratuidadDefinitiva),
					numeroComprobante.getCmpaTotalPago()==null? String.valueOf(0.00):numeroComprobante.getCmpaTotalPago().toString() 
					);

			pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,MailConstantes.ENCABEZADO_CORREO_ORDEN_COBRO_LABEL+fgmpgfPeriodoAcademico.getPracDescripcion().replace("-", " - "),
					sbCorreo.toString() , "admin", "dt1c201s", true, ReporteRegistroMatriculaForm.generarReporteOrdenCobroMail(listMateriaDto, nivel, numeroComprobante, fgmpgfPeriodoAcademico, fichaInscripcionDto, fgmpgfUsuario, fgmpgfDependenciaBuscar, fgmpgfCarreraSeleccion, obtenerDescripcionGratuidad(fgmpgfPerdidaGratuidadDefinitiva)), "Orden de Cobro."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
			String jsonSt = pmail.generarMail();
			Gson json = new Gson();
			MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
			// 	Iniciamos el envÃ­o de mensajes
			ObjectMessage message = session.createObjectMessage(mailDto);
			producer.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "irConfirmacionMatricula";
	}
	

	/**
	 * Método que permite unir los records academicos del estudiante.
	 * @return record academico consolidado.
	 */
	private List<RecordEstudianteDto> buscarRecordAcademico(){
		List<RecordEstudianteDto> record = new ArrayList<>();
		
		List<RecordEstudianteDto> recordSAU = cargarRecordAcademicoCulturaFisicaSAU();
		if (recordSAU != null && recordSAU.size() > 0) {
			
			for (RecordEstudianteDto it : recordSAU) {
				try {
					Carrera entidad = servCarrera.buscarCarreraXEspeCodigo(setearCarreraId(it.getRcesCarreraDto().getCrrId()));
					it.getRcesCarreraDto().setCrrId(entidad.getCrrId());
					it.getRcesCarreraDto().setCrrDescripcion(entidad.getCrrDescripcion());
					it.getRcesCarreraDto().setDpnId(entidad.getCrrDependencia().getDpnId());
				} catch (CarreraNoEncontradoException e) {
					it.getRcesCarreraDto().setCrrId(-1);
					it.getRcesCarreraDto().setCrrDescripcion("CARRERA NO REGISTRADA EN SIIU");
				} catch (CarreraException e) {
				}
			}
			
			record.addAll(recordSAU);
		}

		List<RecordEstudianteDto> recordSIIU = cargarRecordAcademicoCulturaFisicaSIIU();
		if (recordSIIU != null && recordSIIU.size() > 0) {
			record.addAll(recordSIIU);
		}

		if (record.size() > 0) {
			return record;
		}else {
			return null;
		}
	}
	
	private int setearCarreraId(int carreraId) {

		if (carreraId == SAUConstantes.INGLES_APROBACION_NIVELES 
				|| carreraId == SAUConstantes.INGLES_APROBACION_CERTIFICADO
				|| carreraId == SAUConstantes.INGLES_APROBACION_IDIOMA_ADICIONAL
				|| carreraId == SAUConstantes.INGLES_APROBACION_SUFICIENCIA 
				|| carreraId == SAUConstantes.INGLES_APROBACION_A1
				|| carreraId == SAUConstantes.INGLES_APROBACION_A2
				|| carreraId ==  SAUConstantes.INGLES_APROBACION_INTENSIVO1
				|| carreraId ==  SAUConstantes.INGLES_APROBACION_INTENSIVO2
				|| carreraId == SAUConstantes.INGLES_APROBACION_ONLINE){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}else if (carreraId == SAUConstantes.FRANCES_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.FRANCES_APROBACION_A1
				|| carreraId == SAUConstantes.FRANCES_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_FRANCES;
		}else if (carreraId == SAUConstantes.ITALIANO_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.ITALIANO_APROBACION_A1
				|| carreraId == SAUConstantes.ITALIANO_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_ITALIANO;
		}else if (carreraId == SAUConstantes.COREANO_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.COREANO_APROBACION_A1
				|| carreraId == SAUConstantes.COREANO_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_COREANO;
		}else if (carreraId == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA1
				|| carreraId == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_KICHWA;
		}else if (carreraId == SAUConstantes.AFR_DEFENSA_PERSONAL
				|| carreraId == SAUConstantes.AFR_ACOND_FISICO
				|| carreraId == SAUConstantes.AFR_FUTBOL
				|| carreraId == SAUConstantes.AFR_AEREOBICOS
				|| carreraId == SAUConstantes.AFR_GIMNASIA_PESAS
				|| carreraId == SAUConstantes.AFR_BASQUETBALL
				|| carreraId == SAUConstantes.AFR_VOLEYBALL
				|| carreraId == SAUConstantes.AFR_TENIS
				|| carreraId == SAUConstantes.AFR_BAILE
				|| carreraId == SAUConstantes.AFR_BAILE_TROPICAL
				|| carreraId == SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_CULTURA_FISICA;
			// no se sabe en que idoma cayeron
		}else if (carreraId == SAUConstantes.CHINO_MANDARIN_APROBACION
				|| carreraId == SAUConstantes.APROBADO_A1A2
				|| carreraId == SAUConstantes.APROBADO_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}

		return carreraId;
	}
	
	
	private List<RecordEstudianteDto> cargarRecordAcademicoCulturaFisicaSAU (){ 

		try {
			return servFgmpgfMatriculaServicioJdbc.buscarRecordAcademicoSuficienciaCulturaFisicaSAU(fgmpgfFichaInscripcionDto.getPrsIdentificacion());
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	// si tiene dos aprobados sau &&  tiene fcin prac 210 -> update estado aprobado en SAU {0 - actualizar aprobado SAU}
	// si tiene un aprobado sau && tiene un aprobado siiu &&  tiene fcin prac 210 -> update estado aprobado en SIIU {1 - actualizar aprobado SIIU}
	
	// si tiene un aprobado sau && espe_codigo == 2570291 - AFR && si no hay nada en siiu -> mostrar segundo nivel {2- mostrar segundo nivel}
	// si no tiene nada en sau && si tiene un nivel aprobado siiu  -> mostrar segundo nivel {3- mostrar segundo nivel}
	// si no tiene nada en sau && si no tiene nada en siiu -> mostrar primer nivel {4 - mostrar primer nivel}
	// erores -> -99
	private Integer aprobarEstudiantesMigracionCulturaFisica(FichaInscripcionDto fichaInscripcion){
		Integer retorno = null;

		List<RecordEstudianteDto> recordSAU = cargarRecordAcademicoCulturaFisicaSAU();
		List<RecordEstudianteDto> recordSIIU = cargarRecordAcademicoCulturaFisicaSIIU();
		
		//buscar fcin en prac=210 -> solo hasta aqui aplica cualquier deporte
		if (fichaInscripcion.getPracId().equals(PeriodoAcademicoConstantes.PRAC_PERIODO_MIGRACION_SUFICIENCIA_CULTURA_FISICA_VALUE)) {
			int contador = 0;
			
			if (recordSAU != null && recordSAU.size() > 0) {
				for (RecordEstudianteDto it : recordSAU) {
					if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
						contador++;
					}
				}
			}
			
			if (contador > 1) {
				try {

					if (fichaInscripcion.getFcinId() != null && fichaInscripcion.getFcinId() != 0) {
						FichaInscripcion fcin = servFichaInscripcionServicio.updateEstadoFichaInscripcion(fichaInscripcion.getFcinId(), FichaInscripcionConstantes.FCIN_ESTADO_APROBADO_VALUE);
						fgmpgfFichaInscripcionDto.setFcinEstado(fcin.getFcinEstado());
						retorno = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Actualización exitosa de estados por haber aprobado la Suficiencia en Cultura Fisica en SiAc. ");
					}
					
				} catch (FichaInscripcionDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FichaInscripcionDtoNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
			// si aprobo una sola en sau y completo en siiu 210
			}else if (contador == 1){
				int auxContador = 0;
				
				if (recordSIIU != null && recordSIIU.size() > 0) {
					for (RecordEstudianteDto item : recordSIIU) {
						if (item.getRcesFichaMatriculaDto().getPracId() == PeriodoAcademicoConstantes.PRAC_PERIODO_MIGRACION_SUFICIENCIA_CULTURA_FISICA_VALUE
								&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
							auxContador = auxContador++;	
						}
					}
					
					if (auxContador >= 1) {
						try {

							if (fichaInscripcion.getFcinId() != null && fichaInscripcion.getFcinId() != 0) {
								FichaInscripcion fcin = servFichaInscripcionServicio.updateEstadoFichaInscripcion(fichaInscripcion.getFcinId(), FichaInscripcionConstantes.FCIN_ESTADO_APROBADO_VALUE);
								fgmpgfFichaInscripcionDto.setFcinEstado(fcin.getFcinEstado());
								retorno = 0;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Se ha actualizado con éxito el estado de aprobación de la Suficiencia en Cultura Fisica. ");
							}
							
						} catch (FichaInscripcionDtoException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (FichaInscripcionDtoNoEncontradoException e) {
							FacesUtil.mensajeError(e.getMessage());
						}

					}
				
				}
				
			}
	
		}

		return retorno;
	}
	
	
	
	private List<RecordEstudianteDto> cargarRecordAcademicoCulturaFisicaSIIU (){ 
		List<String> carreras = new ArrayList<>();
		carreras.add(String.valueOf(fgmpgfFichaInscripcionDto.getCrrId()));
		
		try {
			return servFgmpgfMatriculaServicioJdbc.buscarRecordEstudianteSIIU(fgmpgfFichaInscripcionDto.getPrsIdentificacion(), carreras, PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	

	/**
	 * Método que permite cargar las materias a matricularse.
	 * @param nivel - nivel al que se matriucla
	 * @param grupoEstudiante - true si es Nuevo
	 */
	private void cargarParalelosSuficienciaCulturaFisica(int nivel, boolean grupoEstudiante, List<RecordEstudianteDto> recordCulturaFisica){
		fgmpgfListMateriasAmatricularMateriaDto = new ArrayList<>();
		fgmpgfEstudianteNuevo = grupoEstudiante;
		fgmpgfNivelUbicacion = nivel;		


		for (MateriaDto it : fgmpgfListMallaCurricularMateria) {
			if (it.getNvlNumeral().intValue() == nivel) {
				MateriaDto materia = new MateriaDto();

				materia.setMtrCmbEstado(true);

				if (recordCulturaFisica != null) {
					materia.setNumMatricula(calcularNumeroMatriculaCulturaFisica(recordCulturaFisica));	
				}else {
					materia.setNumMatricula(SAUConstantes.PRIMERA_MATRICULA_VALUE);
				}

				materia.setMtrCodigo(it.getMtrCodigo());
				materia.setMtrDescripcion(it.getMtrDescripcion());
				materia.setNvlId(it.getNvlId());
				materia.setNvlNumeral(it.getNvlNumeral());
				materia.setMtrCreditos(it.getMtrCreditos());
				materia.setMtrHoras(it.getMtrHoras());
				materia.setMlcrmtId(it.getMlcrmtId());

				try {
					fgmpgfListParaleloDto = servFgmpgfParaleloDto.ListarXMateriaId(it.getMtrId());
				} catch (ParaleloDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (ParaleloDtoNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage()+ it.getMtrId());
				}

				materia.setMtrListParalelo(fgmpgfListParaleloDto);
				fgmpgfListMateriasAmatricularMateriaDto.add(materia);
			}
		}
	}
	
	
	
	/**
	 * Método que permite calcular el número de matricula.
	 *@return numero matricula.
	 */
	private int calcularNumeroMatriculaCulturaFisica( List<RecordEstudianteDto> recordCulturaFisica){
		if (recordCulturaFisica != null && recordCulturaFisica.size() > 0) {
			recordCulturaFisica.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracId));
			int contMatricula = 1;
			
			for (RecordEstudianteDto itemRecord : recordCulturaFisica) {
				if (itemRecord.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
					contMatricula++;
				}else {
					contMatricula = 1;
				}
			}
			
			return contMatricula;
		}else {
			return 1;			
		}

	}
	
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getFgmpgfUsuario() {
		return fgmpgfUsuario;
	}

	public void setFgmpgfUsuario(Usuario fgmpgfUsuario) {
		this.fgmpgfUsuario = fgmpgfUsuario;
	}

	public List<FichaInscripcionDto> getFmfListFichaInscripcionDto() {
		fgmpgfListFichaInscripcionDto = fgmpgfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmpgfListFichaInscripcionDto;
		return fgmpgfListFichaInscripcionDto;
	}

	public void setFmfListFichaInscripcionDto(List<FichaInscripcionDto> fgmpgfListFichaInscripcionDto) {
		this.fgmpgfListFichaInscripcionDto = fgmpgfListFichaInscripcionDto;
	}


	public List<FichaInscripcionDto> getFgmpgfListFichaInscripcionDto() {
		fgmpgfListFichaInscripcionDto = fgmpgfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmpgfListFichaInscripcionDto;
		return fgmpgfListFichaInscripcionDto;
	}

	public void setFgmpgfListFichaInscripcionDto(List<FichaInscripcionDto> fgmpgfListFichaInscripcionDto) {
		this.fgmpgfListFichaInscripcionDto = fgmpgfListFichaInscripcionDto;
	}


	public List<MateriaDto> getFgmpgfListRecordEstudiante() {
		fgmpgfListRecordEstudiante = fgmpgfListRecordEstudiante==null?(new ArrayList<MateriaDto>()):fgmpgfListRecordEstudiante;
		return fgmpgfListRecordEstudiante;
	}

	public void setFgmpgfListRecordEstudiante(List<MateriaDto> fgmpgfListRecordEstudiante) {
		this.fgmpgfListRecordEstudiante = fgmpgfListRecordEstudiante;
	}

	public Boolean getFgmpgfEstudianteNuevo() {
		return fgmpgfEstudianteNuevo;
	}

	public void setFgmpgfEstudianteNuevo(Boolean fgmpgfEstudianteNuevo) {
		this.fgmpgfEstudianteNuevo = fgmpgfEstudianteNuevo;
	}
	 
 
	public List<ParaleloDto> getFgmpgfListParaleloDto() {
		fgmpgfListParaleloDto = fgmpgfListParaleloDto==null?(new ArrayList<ParaleloDto>()):fgmpgfListParaleloDto;
		return fgmpgfListParaleloDto;
	}

	public void setFgmpgfListParaleloDto(List<ParaleloDto> fgmpgfListParaleloDto) {
		this.fgmpgfListParaleloDto = fgmpgfListParaleloDto;
	}

	public PeriodoAcademico getFgmpgfPeriodoAcademico() {
		return fgmpgfPeriodoAcademico;
	}

	public void setFgmpgfPeriodoAcademico(PeriodoAcademico fgmpgfPeriodoAcademico) {
		this.fgmpgfPeriodoAcademico = fgmpgfPeriodoAcademico;
	}


	public PlanificacionCronograma getFgmpgfPlanificacionCronograma() {
		return fgmpgfPlanificacionCronograma;
	}

	public void setFgmpgfPlanificacionCronograma(PlanificacionCronograma fgmpgfPlanificacionCronograma) {
		this.fgmpgfPlanificacionCronograma = fgmpgfPlanificacionCronograma;
	}


	public Dependencia getFgmpgfDependenciaBuscar() {
		return fgmpgfDependenciaBuscar;
	}

	public void setFgmpgfDependenciaBuscar(Dependencia fgmpgfDependenciaBuscar) {
		this.fgmpgfDependenciaBuscar = fgmpgfDependenciaBuscar;
	}


	public Integer getFgmpgfCarrerId() {
		return fgmpgfCarrerId;
	}

	public void setFgmpgfCarrerId(Integer fgmpgfCarrerId) {
		this.fgmpgfCarrerId = fgmpgfCarrerId;
	}

	public CronogramaActividadJdbcDto getFgmpgfProcesoFlujo() {
		return fgmpgfProcesoFlujo;
	}

	public void setFgmpgfProcesoFlujo(CronogramaActividadJdbcDto fgmpgfProcesoFlujo) {
		this.fgmpgfProcesoFlujo = fgmpgfProcesoFlujo;
	}

	public Boolean getFgmpgfPerdidaGratuidadDefinitiva() {
		return fgmpgfPerdidaGratuidadDefinitiva;
	}

	public void setFgmpgfPerdidaGratuidadDefinitiva(Boolean fgmpgfPerdidaGratuidadDefinitiva) {
		this.fgmpgfPerdidaGratuidadDefinitiva = fgmpgfPerdidaGratuidadDefinitiva;
	}

	public List<MateriaDto> getFgmpgfMateriasMatriculadas() {
		return fgmpgfMateriasMatriculadas;
	}

	public void setFgmpgfMateriasMatriculadas(List<MateriaDto> fgmpgfMateriasMatriculadas) {
		this.fgmpgfMateriasMatriculadas = fgmpgfMateriasMatriculadas;
	}

	public Integer getFgmpgfNivelUbicacion() {
		return fgmpgfNivelUbicacion;
	}

	public void setFgmpgfNivelUbicacion(Integer fgmpgfNivelUbicacion) {
		this.fgmpgfNivelUbicacion = fgmpgfNivelUbicacion;
	}


	public int getActivarModalConfirmacion() {
		return activarModalConfirmacion;
	}

	public void setActivarModalConfirmacion(int activarModalConfirmacion) {
		this.activarModalConfirmacion = activarModalConfirmacion;
	}


//	public List<MateriaDto> getFgmpgfListRecordEstudianteActivo() {
//		return fgmpgfListRecordEstudianteActivo;
//	}
//
//	public void setFgmpgfListRecordEstudianteActivo(List<MateriaDto> fgmpgfListRecordEstudianteActivo) {
//		this.fgmpgfListRecordEstudianteActivo = fgmpgfListRecordEstudianteActivo;
//	}


	public Integer getFgmsfDependenciaId() {
		return fgmsfDependenciaId;
	}


	public void setFgmsfDependenciaId(Integer fgmsfDependenciaId) {
		this.fgmsfDependenciaId = fgmsfDependenciaId;
	}


	public List<MateriaDto> getFgmpgfListMateriasAmatricularMateriaDto() {
		return fgmpgfListMateriasAmatricularMateriaDto;
	}


	public void setFgmpgfListMateriasAmatricularMateriaDto(List<MateriaDto> fgmpgfListMateriasAmatricularMateriaDto) {
		this.fgmpgfListMateriasAmatricularMateriaDto = fgmpgfListMateriasAmatricularMateriaDto;
	}


	public FichaInscripcionDto getFgmpgfFichaInscripcionDto() {
		return fgmpgfFichaInscripcionDto;
	}


	public void setFgmpgfFichaInscripcionDto(FichaInscripcionDto fgmpgfFichaInscripcionDto) {
		this.fgmpgfFichaInscripcionDto = fgmpgfFichaInscripcionDto;
	}


	
}