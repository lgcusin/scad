/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducciÃ³n o distribuciÃ³n no autorizada de este programa, 
 * o cualquier porciÃ³n de Ã©l, puede dar lugar a sanciones criminales y 
 * civiles severas, y serÃ¡n procesadas con el grado mÃ¡ximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     FichaGenerarMatriculaPregradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiantede pregrado. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 02-02-2018			 Arturo Villafuerte                   EmisiÃ³n Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula.pregrado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
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
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadException;
import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ArancelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
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
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HistorialAcademicoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularNivelServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PrerequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.ArancelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoGratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.SolicitudTerceraMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.mail.MailRegistroMatricula;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteRegistroMatriculaForm;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes; 

/**
 * Clase (session bean) FichaMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante de pregrado.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "fichaGenerarMatriculaPregradoForm")
@SessionScoped
public class FichaGenerarMatriculaPregradoForm extends HistorialAcademicoForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario fgmpgfUsuario;
	private PeriodoAcademico fgmpgfPeriodoAcademico;
	private PlanificacionCronograma fgmpgfPlanificacionCronograma;
	private Dependencia fgmpgfDependenciaBuscar;
	private Carrera fgmpgfCarreraSeleccion;
	private TipoGratuidad fgmpgfTipoGratuidadParcial;
	private TipoGratuidad fgmpgfTipoGratuidadDefinitiva;
	private TipoGratuidad fgmpgfTipoGratuidadGratuidad;

	private MallaCurricularParaleloDto fgmpgfDisponibiliadCupo;
	private CronogramaActividadJdbcDto fgmpgfFechasMatriculaOrdinaria;
	private CronogramaActividadJdbcDto fgmpgfFechasMatriculaExtraordinaria;
	private CronogramaActividadJdbcDto fgmpgfFechasMatriculaEspecial;
	private CronogramaActividadJdbcDto fgmpgfProcesoFlujo;
	private FichaInscripcionDto fgmpgfBuscarFichaInscripcionDto;
	private FichaInscripcionDto fgmpgfFichaInscripcionDto;
	private CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaOrdinaria;
	private CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaExtraordinaria;
	private CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaEspecial;


	private List<Arancel> fgmpgfListArancel;
	private List<HorarioAcademicoDto> fgmpgfListHorarioAcademico;
	private List<HorarioAcademicoDto> fgmpgfListMateriasHorarioSel;
	private List<MateriaDto> fgmpgfListMateria;
	private List<MateriaDto> fgmpgfMateriasMatriculadas;
	private List<MateriaDto> fgmpgfMallaMaterias;
	private List<FichaInscripcionDto> fgmpgfListFichaInscripcionDto;
	private List<CarreraDto> fgmpgfListCarreraDto;
	private List<MateriaDto> fgmpgfListRecordEstudiante;
	private List<MateriaDto> fgmpgfListMateriaDto;
	private List<MateriaDto> fgmpgfListMateriaDtoHomologadas;
	private List<MateriaDto> fgmpgfListMateriaDtoTruncadaNivel;
	private List<MateriaDto> fgmpgfListMateriaDtoDepuracion;
	private List<MateriaDto> fgmpgfListMateriaDtoAMatricularse;
	private List<ParaleloDto> fgmpgfListParaleloDto;
	private List<CarreraDto> fgmpgfListMateriasCarrera;
	private List<MateriaDto> fgmpgfListMateriasEstado;
	private List<MallaCurricularNivelDto> fgmpgfListMallaCurricularNivelDto;



	private int activarModalConfirmacion;

	private Integer fgmpgfRecordEstudianteIDConNivelacion;
	private Integer fgmpgfCarrerId;
	private Integer fgmpgfPeriodoReinicioId;
	private Integer fgmpgfNivelInferior;
	private Integer fgmpgfNivelSuperior;
	private Integer fgmpgfNivelMaximo;
	private Integer fgmpgfMaximoCreditosNivelInferior;
	private Integer fgmpgfMaximoCreditosNivelSuperior;
	private Integer fgmpgfMaximoCreditosNivelMaximo;
	private Integer fgmpgfNivelUbicacion;

	private Boolean fgmpgfEstudianteNuevo;
	private Boolean disableBoton;
	private Boolean isRegularMatricula;
	private Boolean isEstudianteReingresoConReinicio;
	private Boolean isEstudianteConTercera;
	private Boolean fgmpgfPerdidaGratuidadParcial;
	private Boolean fgmpgfPerdidaGratuidadDefinitiva;

	private String fgmpgfMensajeDlg;

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB private FichaInscripcionDtoServicioJdbc servFgmpgfFichaInscripcionDto;
	@EJB private RecordEstudianteDtoServicioJdbc servFgmpgfRecordEstudianteDto;
	@EJB private MateriaDtoServicioJdbc servFgmpgfMateriaDto;
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
	@EJB private MatriculaServicioJdbc servJdbcErafMatricula;
	@EJB private MallaCurricularNivelServicioJdbc servJdbcMallaCurricularNivel;
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto; 

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
	@EJB private DependenciaServicio servFgmpgfDependenciaServicio;
	@EJB private UsuarioRolServicio servNpfUsuarioRolServicio;
	@EJB private CronogramaServicio servFgmpgfCronograma;
	@EJB private ProcesoFlujoServicio servFgmpgfProcesoFlujo;
	@EJB private PlanificacionCronogramaServicio servFgmpgfPlanificacionCronograma;
	@EJB private CronogramaProcesoFlujoServicio servFgmpgfCronogramaProcesoFlujo;
	@EJB private PeriodoAcademicoServicio servFgmpgfPeriodoAcademico;
	@EJB private MatriculaServicio servFgmpgfMatriculaServicio;
	@EJB private MallaCurricularServicio servFgmpgfMallaCurricular;
	@EJB private HistorialAcademicoServicioJdbc servJdbcHistorialAcademico;

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/


	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Método que permite listar las fichas inscripcion activas del estudiante de pregrado.
	 * -> CONSIDERACIONES
	 * - traer todas las fichas inscripcion de pregrado
	 * - agregar a la lista solo las activas
	 * - buscar si es reingreso y ya tubo reinicio
	 * @return listarFichasInscripcionPregrado.xhtml
	 */
	public String irListarFichaInscripcionPregrado(Usuario usuario) {

		try {

			iniciarFormListarFichasInscripcion();

			fgmpgfUsuario = usuario;

			CronogramaDto tipoMatriculas = servFgmpgfCronogramaDtoServicioJdbc.buscarTipoMatriculaGeneral(CronogramaConstantes.TIPO_ACADEMICO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, ProcesoFlujoConstantes.PRFL_MATRICULAS_REGULARES_VALUE);
			if (tipoMatriculas != null) {
				if (tipoMatriculas.getPlcrEstado().equals(ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE) && tipoMatriculas.getPrflId() == ProcesoFlujoConstantes.PRFL_MATRICULAS_REGULARES_VALUE) {
					isRegularMatricula = Boolean.TRUE;
				}
			}

			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(fgmpgfUsuario.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
			if(!usro.getUsroEstado().equals(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE)){
				List<FichaInscripcionDto> fichasInscripcion = servFgmpgfFichaInscripcionDto.buscarFichasInscripcion(fgmpgfUsuario.getUsrPersona().getPrsIdentificacion(), CarreraConstantes.TIPO_PREGRADO_VALUE);
				if (!fichasInscripcion.isEmpty()) {

					for (FichaInscripcionDto it : fichasInscripcion) {
						if (it.getFcinEstado().equals(FichaInscripcionConstantes.ACTIVO_VALUE)) {
							fgmpgfListFichaInscripcionDto.add(it);
						}
					}

					if (!fgmpgfListFichaInscripcionDto.isEmpty()) {
						for (FichaInscripcionDto it : fichasInscripcion) {
							if (it.getFcinTipoIngreso().equals(FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)) {
								isEstudianteReingresoConReinicio = Boolean.TRUE;
								fgmpgfPeriodoReinicioId = it.getPracId();
							}
						}
					}

				}

			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.listar.ficha.inscripcion.pregrado.validacion.estado.usuario")));
				return null;
			}

		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.listar.ficha.inscripcion.pregrado.ficha.inscripcion.no.encontrado.exception")));
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		return "irListarFichaInscripcionPregrado";
	}



	private void iniciarFormListarFichasInscripcion() {
		fgmpgfUsuario = new Usuario();

		fgmpgfListFichaInscripcionDto = new ArrayList<>();

		isRegularMatricula = Boolean.FALSE;
		isEstudianteReingresoConReinicio = Boolean.FALSE;
		isEstudianteConTercera = Boolean.FALSE;

		fgmpgfPeriodoReinicioId = GeneralesConstantes.APP_ID_BASE;
		fgmpgfNivelInferior = GeneralesConstantes.APP_ID_BASE;
		fgmpgfNivelSuperior = GeneralesConstantes.APP_ID_BASE;
		fgmpgfNivelMaximo = GeneralesConstantes.APP_ID_BASE;

		fgmpgfMaximoCreditosNivelInferior = Integer.valueOf(0);
		fgmpgfMaximoCreditosNivelSuperior = Integer.valueOf(0);
		fgmpgfMaximoCreditosNivelMaximo = Integer.valueOf(0);
	}



	/**
	 * Método que permite cargar los parametros necesarios para poder generar la matricula.
	 *  -> CONSIDERACIONES
	 *  - verificar si tiene matricula en el periodo en cierre.
	 *  - verificar si tiene algun estado pendiente en el record
	 *  - 
	 *  @return generarMatriculaPregrado.xhtml
	 */
	public String irGenerarMatriculaPregrado(FichaInscripcionDto fichaInscripcion) {
		String retorno = null;
		
		disableBoton = false; 
		activarModalConfirmacion = 0;

		fgmpgfBuscarFichaInscripcionDto = fichaInscripcion;
		fgmpgfPerdidaGratuidadDefinitiva = false;

		Date fechaActual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
		String strDate = sdf.format(fechaActual);

		fgmpgfListMateriasHorarioSel = new ArrayList<>();
		fgmpgfListMateria = new ArrayList<>(); 
		fgmpgfDisponibiliadCupo = new MallaCurricularParaleloDto();
		fgmpgfListMateriaDtoHomologadas= new ArrayList<>();
		fgmpgfMensajeDlg = new String();

		try {
			
//			if (fichaInscripcion.getCrrId().equals(CarreraConstantes.CARRERA_MEDICINA_VALUE) || fichaInscripcion.getCrrId().equals(CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE)) {
//				isRegularMatricula = Boolean.TRUE;
//			}

			fgmpgfCarreraSeleccion = servFgmpgfCarreraServicio.buscarPorId(fichaInscripcion.getCrrId());
			fgmpgfPeriodoAcademico = servFgmpgfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			fgmpgfDependenciaBuscar = servFgmpgfDependenciaServicio.buscarPorId(fichaInscripcion.getDpnId());
			fgmpgfProcesoFlujo = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarPlanificacionCronogramaPorFechasFull(strDate, CronogramaConstantes.TIPO_ACADEMICO_VALUE);
			fgmpgfPlanificacionCronograma = servFgmpgfPlanificacionCronograma.buscarPorId(fgmpgfProcesoFlujo.getPlcrId());
			fgmpgfTipoGratuidadParcial = servFgmpgfTipoGratuidadServicio.buscarPorId(TipoGratuidadConstantes.PERDIDA_TEMPORAL_VALUE);
			fgmpgfTipoGratuidadDefinitiva = servFgmpgfTipoGratuidadServicio.buscarPorId(TipoGratuidadConstantes.PERDIDA_DEFINITIVA_VALUE); 
			fgmpgfTipoGratuidadGratuidad = servFgmpgfTipoGratuidadServicio.buscarPorId(TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE);
			fgmpgfListMallaCurricularNivelDto = servJdbcMallaCurricularNivel.buscar(fgmpgfBuscarFichaInscripcionDto.getCrrId()); 


			if(validarCronograma()){
				if(verificarNoMatriculado()){

					fgmpgfMallaMaterias = servFgmpgfMateriaDto.listarMateriasxCarreraFull(fichaInscripcion.getCrrId(), new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});

					if(fgmpgfCarreraSeleccion.getCrrEspeCodigo() == null){
						fgmpgfCarreraSeleccion.setCrrEspeCodigo(GeneralesConstantes.APP_ID_BASE);
					}

					List<RecordEstudianteDto> historialAcademico = cargarHistorialAcademicoSAIUHomologado(fichaInscripcion.getPrsIdentificacion());
					
					List<MateriaDto> recordEstudianteSAIU = null;	

					// para reinicios
					if (fichaInscripcion.getFcinTipoIngreso().equals(FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)  || isEstudianteReingresoConReinicio) {
						recordEstudianteSAIU = filtrarHistorialAcademicoPorCarrera(historialAcademico, fichaInscripcion.getFcinReinicioOrigen(), fgmpgfPeriodoReinicioId, fichaInscripcion.getCrrId());
					}else {
						recordEstudianteSAIU = filtrarHistorialAcademicoPorCarrera(historialAcademico, fichaInscripcion.getCrrId());
					}
					
//					for (MateriaDto materia : recordEstudianteSAIU) {
//						System.out.println(materia.getMtrCodigo()+ " paso al siiu - > " + materia.getMtrCreditos()+" -> " + materia.getMtrHoras());
//
//					}

					// verificar si tiene estados pendientes en el periodo en cierre.
					if (!verificarEstadosPendientesHistorial(recordEstudianteSAIU)) {
						// para segundas - terceras y reingresos
						if(verificar2da3raReingreso(fichaInscripcion, recordEstudianteSAIU) == 0){
							calculoPerdidaGratuidad(null);

							fgmpgfListRecordEstudiante = new ArrayList<>(); 
							if(recordEstudianteSAIU != null){
								fgmpgfListRecordEstudiante.addAll(recordEstudianteSAIU);
							}

							if (fichaInscripcion.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE
									|| fichaInscripcion.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_MIGRADO_SAU_VALUE
									|| fichaInscripcion.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_HOMOLOGACION_VALUE
									|| fichaInscripcion.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE) {

								// Estudiante Nuevo.
								if ((fgmpgfListRecordEstudiante==null || fgmpgfListRecordEstudiante.size() == 0)) {
									fgmpgfListMateriaDtoAMatricularse = cargarMateriasAMatricularNuevo(fichaInscripcion); 
									if( fgmpgfListMateriaDtoAMatricularse == null || fgmpgfListMateriaDtoAMatricularse.size() == 0){
										FacesUtil.mensajeError("No se encontró materias para matricularse.");
										return null;
									}
								}

								// Estudiante existente en SAIU.
								if ((fgmpgfListRecordEstudiante != null && fgmpgfListRecordEstudiante.size() > 0)) {
									if (isRegularMatricula) {
										fgmpgfListMateriaDtoAMatricularse = cargarMateriasAMatricularseRegulares(fgmpgfListRecordEstudiante);
										if( fgmpgfListMateriaDtoAMatricularse == null){
											return null;
										}
									}else {
										fgmpgfListMateriaDtoAMatricularse = cargarMateriasAMatricularseDosNiveles(fgmpgfListRecordEstudiante);
										if( fgmpgfListMateriaDtoAMatricularse == null){
											FacesUtil.mensajeError("No se encontró materias para matricularse.");
											return null;
										}
									}
								}

								calcularNivelAsignado();
								informacionSeleccion();

								if(fgmpgfNivelUbicacion != null){
									if(verificarCronogramaSemestreCarrera(3, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
										retorno = "irGenerarMatriculaPregrado"; 
									}
								}
							}
							
						}
					} 
				}
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
			FacesUtil.mensajeError("Cronograma no habilitado.");
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
		} catch (TipoGratuidadNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoGratuidadException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularNivelException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularNivelNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró registros de Malla Curricular Por Nivel, acérquese a la Secretaria de su Carrera.");
		}

		return retorno;
	}


	private boolean verificarEstadosPendientesHistorial(List<MateriaDto> recordEstudianteSAIU) {
		boolean retorno = false;

		for (MateriaDto item : recordEstudianteSAIU) {
			if (item.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL)
					|| item.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL)
					|| item.getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_SUSPENSO_LABEL)) {
				retorno = true;
				FacesUtil.mensajeError("Ud. no puede generar matrícula debido que la Asignatura " + item.getMtrDescripcion() + " se encuentra en estado " + item.getMtrEstadoLabel() +".");
			}
		}

		return retorno;
	}



	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){

		fgmpgfUsuario = null;
		fgmpgfListFichaInscripcionDto = null;
		fgmpgfBuscarFichaInscripcionDto = null;
		fgmpgfListCarreraDto = null;
		fgmpgfListRecordEstudiante = null;
		fgmpgfEstudianteNuevo = null;
		fgmpgfListMateriaDto = null;
		fgmpgfListMateriaDtoHomologadas = null;
		fgmpgfListMateriaDtoTruncadaNivel = null;
		fgmpgfListMateriaDtoDepuracion = null;
		fgmpgfListMateriaDtoAMatricularse = null;
		fgmpgfListParaleloDto = null;
		fgmpgfPeriodoAcademico = null;
		fgmpgfFichaInscripcionDto = null;
		fgmpgfPlanificacionCronograma = null;
		fgmpgfCronogramaActividadMatriculaOrdinaria = null;
		fgmpgfCronogramaActividadMatriculaExtraordinaria = null;
		fgmpgfCronogramaActividadMatriculaEspecial = null;
		fgmpgfListMateriasCarrera = null;
		fgmpgfListMateriasEstado = null;
		fgmpgfDependenciaBuscar = null;
		fgmpgfFechasMatriculaOrdinaria = null;
		fgmpgfFechasMatriculaExtraordinaria = null;
		fgmpgfFechasMatriculaEspecial = null;
		fgmpgfProcesoFlujo = null;
		fgmpgfCarrerId = null;
		fgmpgfRecordEstudianteIDConNivelacion = null; 
		fgmpgfListHorarioAcademico = null;
		fgmpgfListMateriasHorarioSel = null;
		fgmpgfListMateria = null;
		fgmpgfDisponibiliadCupo = null; 
		fgmpgfPerdidaGratuidadParcial = null;
		fgmpgfPerdidaGratuidadDefinitiva = null;
		fgmpgfNivelUbicacion = null;
		fgmpgfMateriasMatriculadas = null;
		fgmpgfCarreraSeleccion = null;
		fgmpgfMallaMaterias = null;
		fgmpgfListArancel = null;

		FacesUtil.limpiarMensaje();

		return "irInicio";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String cancelarGenerarMatricula(){

		fgmpgfFichaInscripcionDto = new FichaInscripcionDto();
		fgmpgfPeriodoAcademico = new PeriodoAcademico();
		fgmpgfListRecordEstudiante = null;
		fgmpgfEstudianteNuevo = new Boolean(false);
		fgmpgfListMateriaDto = null; 
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

		if(verificarNoMatriculado()){
			if(verificarCronogramaSemestreCarrera(3, fgmpgfNivelUbicacion, fgmpgfProcesoFlujo.getPrlfId(), fgmpgfCarreraSeleccion.getCrrId(), true)){
				if(verificarSeleccion(true)){

					disableBoton = true;

					Boolean verificarCupos = false;
					Nivel nivel = null;

					try {

						List<MateriaDto> materiasMatricula = new ArrayList<>();
						for(MateriaDto item: fgmpgfListMateriaDtoAMatricularse){
							if(item.getMtrCmbEstado()){
								Paralelo paralelo = servFgmpgfParaleloServicio.buscarPorId(item.getPrlId());
								item.setPrlDescripcion(paralelo.getPrlDescripcion());
								item.setValorMatricula(calcularValorMateria(item));
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

							numeroComprobante = servFgmpgfMatriculaServicio.generarMatriculaPregradoFull(
									materiasMatricula, 
									fgmpgfBuscarFichaInscripcionDto, 
									fgmpgfEstudianteNuevo, 
									fgmpgfProcesoFlujo, 
									fgmpgfPlanificacionCronograma, 
									fgmpgfRecordEstudianteIDConNivelacion, 
									nivel.getNvlId(), 
									fgmpgfPeriodoAcademico,
									calcularValorTotal(materiasMatricula),
									calcularGratuidad(),
									fgmpgfListArancel
									);

							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.generar.matricula.exitoso")));

							contadorUnicoMatriculas = contadorUnicoMatriculas+1;

							return enviarMailRegistroMatricula(fgmpgfBuscarFichaInscripcionDto, nivel, numeroComprobante, fgmpgfMateriasMatriculadas);

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

	// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------


	/**
	 * Carga el record academico concatenando SAU / SIIU
	 * @param carreraSau Carera de SAU a buscar
	 * @return retorna la lista de materias.
	 */
	@SuppressWarnings("rawtypes")
	public List<MateriaDto> cargarRecordEstudianteSAIU(Carrera carreraSau, FichaInscripcionDto fichaInscripcion){

		List<MateriaDto> retorno = null;

		List<MateriaDto> recordSau = null;
		List<MateriaDto> recordSiiu = null;

		try {

			if((fichaInscripcion.getFcinTipoIngreso() == GeneralesConstantes.APP_ID_BASE && fichaInscripcion.getFcinEstadoIngreso() == GeneralesConstantes.APP_ID_BASE) 
					|| (fichaInscripcion.getFcinTipoIngreso() != null && fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE && fichaInscripcion.getFcinEstadoIngreso() != null 
					&& fichaInscripcion.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE)){

				//				Revisar xq el not in
				recordSiiu = servFgmpgfMateriaDto.buscarRecordEstudianteXidentificacionXpracActivoXprcacFull(fgmpgfUsuario.getUsrIdentificacion(),fichaInscripcion.getFcinCncrArea(), GeneralesConstantes.APP_ID_BASE);

			}else{
				if((fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE && fichaInscripcion.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE)
						|| (fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)
						|| (fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE && fichaInscripcion.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE)
						|| (fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE && fichaInscripcion.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE)
						|| (fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE && fichaInscripcion.getFcinEstadoIngreso() == FichaInscripcionConstantes.ESTADO_HOMOLOGADO_VALUE)
						|| (fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)
						){

					//					Revisar xq el not in
					recordSiiu = servFgmpgfMateriaDto.buscarRecordEstudianteXidentificacionXpracActivoXprcacFull(fgmpgfUsuario.getUsrIdentificacion(),fichaInscripcion.getFcinCncrArea(), fichaInscripcion.getPracId());
					List<MateriaDto> materiasHomologadas = servFgmpgfMateriaDto.buscarRecordEstudianteXidentificacionXpracActivoXprcacFull(fichaInscripcion.getPrsIdentificacion(),fichaInscripcion.getFcinCncrArea(), PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
					if(recordSiiu != null ){
						if(materiasHomologadas != null){
							recordSiiu.addAll(materiasHomologadas);
						}
					}else{
						recordSiiu = new ArrayList<>();
						recordSiiu.addAll(materiasHomologadas);
					}
				}
			}

			if((fichaInscripcion.getFcinTipoIngreso() == GeneralesConstantes.APP_ID_BASE && fichaInscripcion.getFcinEstadoIngreso() == GeneralesConstantes.APP_ID_BASE) 
					|| (fichaInscripcion.getFcinTipoIngreso() != null && fichaInscripcion.getFcinTipoIngreso() == FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE 
					&& fichaInscripcion.getFcinEstadoIngreso() != null && fichaInscripcion.getFcinEstadoIngreso()  == FichaInscripcionConstantes.ESTADO_NO_HOMOLOGADO_VALUE)){

				List<RecordEstudianteSAUDto> record = servFgmpgfMatriculaServicioJdbc.buscarRecordAcademicoSAU(fichaInscripcion.getPrsIdentificacion(), carreraSau.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);

				if(record != null){

					List<RecordEstudianteSAUDto> recordAux = new ArrayList<>();
					recordAux.addAll(record);
					for(RecordEstudianteSAUDto item: recordAux){
						Iterator itera = record.iterator();
						while(itera.hasNext()){
							RecordEstudianteSAUDto cad = (RecordEstudianteSAUDto) itera.next();
							if( cad.getCodigoMateria().equals(item.getCodigoMateria()) && (cad.getNumeroMtricula() < item.getNumeroMtricula())  ){
								itera.remove();
							}
						}	
					}

					recordSau = new ArrayList<>();
					for(RecordEstudianteSAUDto item: record){

						MateriaDto mtrDto = new MateriaDto();
						mtrDto.setRcesId(GeneralesConstantes.APP_ID_BASE.intValue());
						mtrDto.setRcesEstado(item.getEstado());
						mtrDto.setNvlNumeral(item.getSemestre());
						mtrDto.setNumMatricula(item.getNumeroMtricula());

						Materia mtr = servFgmpgfMateriaServicio.buscarXCodigoXEspeCodigo(item.getCodigoMateria(), item.getCarreraId());
						mtrDto.setMtrId(mtr.getMtrId()); 

						recordSau.add(mtrDto);

					}
				}
			}

		} catch (RecordEstudianteNoEncontradoException e) { 
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		retorno = new ArrayList<>();

		if(recordSau != null && recordSau.size() > 0){
			retorno.addAll(recordSau);
		}
		if(recordSiiu != null && recordSiiu.size() > 0){
			retorno.addAll(recordSiiu);
		}

		return retorno;
	}
	
	/**
	 * Método que permite cargar las materias a matricularse en un solo nivel, si solo si tiene aprobado todo el nivel y viene pianito pianito.
	 */
	private List<MateriaDto> cargarMateriasAMatricularseRegulares(List<MateriaDto> recordEstudiantil){
		List<MateriaDto> retorno = null;
		fgmpgfEstudianteNuevo = false;
		boolean continuar = true;

		

		List<MateriaDto> materiasAprobadas = new ArrayList<>();
		List<MateriaDto> materiasReprobadas = new ArrayList<>();

		//Materias aprobadas / reprobadas
		materiasAprobadas = cargarMateriasAprobadas(recordEstudiantil);
		materiasReprobadas = cargarMateriasReprobadas(recordEstudiantil);

		// quitar las aprobadas de la malla
		List<MateriaDto> materiasAMatricularse =  null;
		if (!materiasAprobadas.isEmpty()) {
			materiasAMatricularse = quitarMateriasReprobadasQueAprobo(fgmpgfMallaMaterias, materiasAprobadas);	
		}else {
			materiasAMatricularse = fgmpgfMallaMaterias;
		}
		
		
		if(materiasAMatricularse != null && materiasAMatricularse.size() > 0){

			for (MateriaDto item : materiasAMatricularse) {
				item.setNumMatricula(calcularNumeroMatricula(materiasReprobadas, item));
				if(item.getNumMatricula() > SAUConstantes.TERCERA_MATRICULA_VALUE){
					continuar  = false;
					break;
				}else if(item.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE) {
					if(!verificarSolicitudTerceraMatricula(item.getMtrId(), item.getMtrDescripcion(), fgmpgfBuscarFichaInscripcionDto.getFcesId())){
						continuar  = false;
						break;
					}
				}
			}
			

			if (continuar) {
				// nivel maximo APROBADO
				MateriaDto materiaAprobadaNivelMaximoRecord = materiasAprobadas.stream().max(Comparator.comparing(MateriaDto::getNvlNumeral)).get();
				// nivel maximo MALLA
				MateriaDto materiaNivelMaximoMalla = fgmpgfMallaMaterias.stream().max(Comparator.comparing(MateriaDto::getNvlNumeral)).get();
				
				// total creditos aprobadas nivel
				int totalCreditosAprobadosRecord = materiasAprobadas.stream().filter(it-> it.getNvlNumeral()==materiaAprobadaNivelMaximoRecord.getNvlNumeral()).mapToInt(it-> it.getMtrCreditos()!=null?it.getMtrCreditos():it.getMtrHoras()).sum();
				// total creditos malla nivel -> 
				int totalCreditosMallaCurricular = fgmpgfMallaMaterias.stream().filter(it-> it.getNvlNumeral()==materiaAprobadaNivelMaximoRecord.getNvlNumeral()).mapToInt(it-> it.getMtrCreditos()!=0?it.getMtrCreditos():it.getMtrHoras()).sum();

				if (totalCreditosAprobadosRecord == totalCreditosMallaCurricular) {

					if (materiaAprobadaNivelMaximoRecord.getNvlNumeral().equals(materiaNivelMaximoMalla.getNvlNumeral())) {
						FacesUtil.mensajeInfo("No se encontraron materias a matricularse. Usted ha culminado la malla curricular de la carrera "+fgmpgfCarreraSeleccion.getCrrDetalle()+".");
						return retorno;	
					}else {

						materiasAMatricularse.sort(Comparator.comparing(MateriaDto::getNvlNumeral).reversed());

						List<Integer> niveles = verificarSiHayDosNiveles(materiasAMatricularse);
						if(niveles != null && niveles.size() > 0){
							niveles.sort(Comparator.naturalOrder());
							fgmpgfNivelSuperior = niveles.get(0);

							Iterator<MateriaDto> iter = materiasAMatricularse.iterator();
							while (iter.hasNext()) {
								MateriaDto item = (MateriaDto) iter.next();
								if(!item.getNvlNumeral().equals(fgmpgfNivelSuperior)){
									iter.remove();
								}
							}
							
							//buscar paralelos
							materiasAMatricularse.sort(Comparator.comparing(MateriaDto::getNvlNumeral).reversed());
							retorno = filtrarPrerequisitosAMatricular(materiasAMatricularse);

							for (MateriaDto aux : retorno) {
								try{
									aux.setMtrCmbEstado(Boolean.TRUE);
									aux.setMtrListParalelo(servFgmpgfParaleloDto.ListarXMateriaId(aux.getMtrId()));
								} catch (ParaleloDtoNoEncontradoException e) {
									FacesUtil.mensajeError(e.getMessage());
								} catch (ParaleloDtoException e) {
									FacesUtil.mensajeError(e.getMessage());
								}
							}

							calculoPerdidaGratuidad(materiasReprobadas);
							return retorno;
						}else {
							return retorno;
						}
					}
				}else{
					FacesUtil.mensajeInfo("Cronograma de matrículas habilitado para todas las asignaturas de un solo nivel y paralelo.");
					return retorno;
				}		
			}else {
				FacesUtil.mensajeInfo("Verifique el Record Académico del Estudiante.");
				return retorno;
			}
		}else{				
			FacesUtil.mensajeInfo("No se encontró materias para matriculación. Usted ha culminado la malla curricular de la carrera "+fgmpgfCarreraSeleccion.getCrrDetalle()+".");
			return retorno;
		}

	}	


	/**
	 * Método que permite cargar las materias para estudiantes irregulares.
	 */
	private List<MateriaDto> cargarMateriasAMatricularseDosNiveles(List<MateriaDto> recordEstudiantil){

		List<MateriaDto> retorno = null;
		fgmpgfEstudianteNuevo = false;
		boolean continuar = true;

		List<MateriaDto> listMateriaAprobadas = new ArrayList<>();
		List<MateriaDto> listMateriaReprobadas = new ArrayList<>();

		//Materias aprobadas / reprobadas
		listMateriaAprobadas = cargarMateriasAprobadas(recordEstudiantil);
		listMateriaReprobadas = cargarMateriasReprobadas(recordEstudiantil);

		// quitar las aprobadas de la malla
		List<MateriaDto> materiasAMatricularse =  null;
		if (listMateriaAprobadas != null && listMateriaAprobadas.size() > 0) {
			materiasAMatricularse = quitarMateriasReprobadasQueAprobo(fgmpgfMallaMaterias, listMateriaAprobadas);	
		}else {
			materiasAMatricularse = fgmpgfMallaMaterias;
		}

		for (MateriaDto item : materiasAMatricularse) {
			item.setNumMatricula(calcularNumeroMatricula(listMateriaReprobadas, item));
			if(item.getNumMatricula() > SAUConstantes.TERCERA_MATRICULA_VALUE){
				FacesUtil.mensajeInfo("Ud ha reprobado con tercera matrícula la asignatura "+ item.getMtrDescripcion() + ".");
				continuar  = false;
				break;
			}else if(item.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE) {
				if(!verificarSolicitudTerceraMatricula(item.getMtrId(), item.getMtrDescripcion(), fgmpgfBuscarFichaInscripcionDto.getFcesId())){
					continuar  = false;
					break;
				}
			}
		}

		if (continuar) {

			if(materiasAMatricularse != null && !materiasAMatricularse.isEmpty()){
				materiasAMatricularse.sort(Comparator.comparing(MateriaDto::getNvlNumeral).reversed());

				// si la lista integer es mayor a uno tomar como niveles min y max
				List<Integer> niveles = verificarSiHayDosNiveles(materiasAMatricularse);
				if(niveles.size() > 1){
					niveles.sort(Comparator.naturalOrder());
					fgmpgfNivelInferior = niveles.get(0);
					fgmpgfNivelSuperior = niveles.get(1);

					//quitar todo lo que no sirve
					Iterator<MateriaDto> iter = materiasAMatricularse.iterator();
					while (iter.hasNext()) {
						MateriaDto item = (MateriaDto) iter.next();
						if (!item.getNvlNumeral().equals(fgmpgfNivelInferior) && !item.getNvlNumeral().equals(fgmpgfNivelSuperior)) {
							iter.remove();
						}
					}

					if (isEstudianteConTercera) {
						BigDecimal limitePorcentaje = BigDecimal.ZERO; 

						int creditosNivelInferior = 0;
						int creditosNivelSuperior = 0;
						int creditos3eraNivelInferior = 0;
						int creditos3eraNivelSuperior = 0;

						List<MateriaDto> materiasNivelInferior = new ArrayList<>();
						List<MateriaDto> materiasNivelSuperior = new ArrayList<>();

						for (MateriaDto item : materiasAMatricularse) {
							if (item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
								materiasNivelInferior.add(item);
								creditosNivelInferior = creditosNivelInferior +  (item.getMtrCreditos() == 0 ? item.getMtrHoras():item.getMtrCreditos());
								if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
									creditos3eraNivelInferior = creditos3eraNivelInferior +  (item.getMtrCreditos() == 0 ? item.getMtrHoras():item.getMtrCreditos());
								}
							}else if (item.getNvlNumeral().equals(fgmpgfNivelSuperior)) {
								materiasNivelSuperior.add(item);
								creditosNivelSuperior = creditosNivelSuperior +  (item.getMtrCreditos() == 0 ? item.getMtrHoras():item.getMtrCreditos());
								if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
									creditos3eraNivelSuperior = creditos3eraNivelSuperior +  (item.getMtrCreditos() == 0 ? item.getMtrHoras():item.getMtrCreditos());
								}
							}
						}



						Iterator<MallaCurricularNivelDto> iterMCN = fgmpgfListMallaCurricularNivelDto.iterator();
						while (iterMCN.hasNext()) {
							MallaCurricularNivelDto item = (MallaCurricularNivelDto) iterMCN.next();
							if (item.getMlcrnvNivelDto().getNvlNumeral().equals(fgmpgfNivelInferior)) {
								fgmpgfMaximoCreditosNivelInferior = item.getMlcrnvHoras();
							}else if (item.getMlcrnvNivelDto().getNvlNumeral().equals(fgmpgfNivelSuperior)) {
								fgmpgfMaximoCreditosNivelSuperior = item.getMlcrnvHoras();
							}
						}

						limitePorcentaje = calcularCuarentaPorCientoPorNivel(Long.valueOf(fgmpgfMaximoCreditosNivelInferior));
						if (creditos3eraNivelInferior > 0) {
							if (new BigDecimal(creditos3eraNivelInferior).floatValue() > limitePorcentaje.floatValue()) {
								// dejar a libre opcion sin materias en primera o segunda
								iter = materiasAMatricularse.iterator();
								while (iter.hasNext()) {
									MateriaDto item = (MateriaDto) iter.next();
									if (item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
										if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
											item.setMtrCmbEstado(Boolean.FALSE);
										}else {
											iter.remove();	
										}
									}else {
										iter.remove();
									}
								}
								FacesUtil.mensajeInfo("Ud. supera el 40% de créditos con tercera matrícula por ello debe seleccionar una combinación inferior al porcentaje indicado.");
							}else if (new BigDecimal(creditos3eraNivelInferior+creditosNivelInferior).floatValue() > limitePorcentaje.floatValue()) {
								// obligar  a tomar las terceras y restringir hasta el 40%
								iter = materiasAMatricularse.iterator();
								while (iter.hasNext()) {
									MateriaDto item = (MateriaDto) iter.next();
									if (item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
										if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
											item.setMtrCmbEstado(Boolean.TRUE);
										}else {
											item.setMtrCmbEstado(Boolean.FALSE);
										}
									}else {
										iter.remove();
									}
								}
								FacesUtil.mensajeInfo("Ud. tiene Asignaturas con 3era matrícula por ello debe seleccionar una combinación inferior al 40% de créditos en el Nivel.");
							}else {

								if (creditos3eraNivelInferior > 0 && creditos3eraNivelSuperior > 0) {
									// ver nivel de mas creditos
									limitePorcentaje = calcularCuarentaPorCientoPorNivel(Long.valueOf(fgmpgfMaximoCreditosNivelSuperior));
									// sumar 3eras de dos niveles y verificar si supera 40%
									if (new BigDecimal(creditos3eraNivelInferior+creditosNivelInferior+creditos3eraNivelSuperior).floatValue() > limitePorcentaje.floatValue()) {
										// si supera el limite dar solo nivel inferior
										iter = materiasAMatricularse.iterator();
										while (iter.hasNext()) {
											MateriaDto item = (MateriaDto) iter.next();
											if (item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
												if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
													item.setMtrCmbEstado(Boolean.TRUE);
												}else {
													item.setMtrCmbEstado(Boolean.FALSE);
												}
											}else {
												iter.remove();		
											}
										}
										FacesUtil.mensajeInfo("Ud. supera el 40% de créditos con tercera matrícula en el siguiente nivel por ello debe seleccionar una combinación inferior al 40% en el nivel asignado.");
									}else {
										// caso contrario dar solo las 3eras obligatorias
										iter = materiasAMatricularse.iterator();
										while (iter.hasNext()) {
											MateriaDto item = (MateriaDto) iter.next();
											if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE && item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
												item.setMtrCmbEstado(Boolean.TRUE);
											}else {
												item.setMtrCmbEstado(Boolean.FALSE);
											}
										}
									}
								}else {
									// obligar  a tomar las terceras y restringir hasta el 40%
									iter = materiasAMatricularse.iterator();
									while (iter.hasNext()) {
										MateriaDto item = (MateriaDto) iter.next();
										if (item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
											item.setMtrCmbEstado(Boolean.TRUE);
										}else {
											item.setMtrCmbEstado(Boolean.FALSE);
										}
									}
									FacesUtil.mensajeInfo("Ud. tiene Asignaturas con 3era matrícula por ello debe seleccionar una combinación inferior al 40% de créditos en el Nivel.");
								}

							}
						}else if (creditos3eraNivelSuperior > 0) {
							// quitar las del nivel superior excepto la tercera y dejo las del nivel inferior
							iter = materiasAMatricularse.iterator();
							while (iter.hasNext()) {
								MateriaDto item = (MateriaDto) iter.next();
								if (item.getNvlNumeral().equals(fgmpgfNivelInferior)
										|| (item.getNvlNumeral().equals(fgmpgfNivelSuperior) && item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE)) {
									item.setMtrCmbEstado(Boolean.TRUE);
								}else {
									iter.remove();
								}
							}
						}else {
							iter = materiasAMatricularse.iterator();
							while (iter.hasNext()) {
								MateriaDto item = (MateriaDto) iter.next();
								if (item.getNvlNumeral().equals(fgmpgfNivelInferior)
										|| (item.getNvlNumeral().equals(fgmpgfNivelSuperior) && item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE)) {
									item.setMtrCmbEstado(Boolean.TRUE);
								}else {
									item.setMtrCmbEstado(Boolean.FALSE);
								}
							}
						}

					}else{
						iter = materiasAMatricularse.iterator();
						while (iter.hasNext()) {
							MateriaDto item = (MateriaDto) iter.next();
							if (item.getNvlNumeral().equals(fgmpgfNivelInferior)) {
								item.setMtrCmbEstado(Boolean.TRUE);
							}else {
								if (item.getNumMatricula() > SAUConstantes.PRIMERA_MATRICULA_VALUE) {
									item.setMtrCmbEstado(Boolean.TRUE);
								}else{
									item.setMtrCmbEstado(Boolean.FALSE);
								}
							}
						}

					}

					//buscar paralelos
					materiasAMatricularse.sort(Comparator.comparing(MateriaDto::getNvlNumeral).reversed());
					retorno = filtrarPrerequisitosAMatricular(materiasAMatricularse);

					for (MateriaDto aux : retorno) {

						try{
							aux.setMtrListParalelo(servFgmpgfParaleloDto.ListarXMateriaId(aux.getMtrId()));
						} catch (ParaleloDtoException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (ParaleloDtoNoEncontradoException e) {
							FacesUtil.mensajeError(e.getMessage() + " : " + aux.getMtrDescripcion());
						}
					}

					calculoPerdidaGratuidad(listMateriaReprobadas);

					return retorno;
				}else{
					fgmpgfNivelMaximo = niveles.get(0);

					Iterator<MateriaDto> iter = materiasAMatricularse.iterator();
					if (isEstudianteConTercera) {
						BigDecimal limitePorcentaje = BigDecimal.ZERO; 

						int creditosNivelMaximo = 0;
						int creditos3eraNivelMaximo = 0;

						List<MateriaDto> materiasNivelSuperior = new ArrayList<>();

						for (MateriaDto item : materiasAMatricularse) {
							materiasNivelSuperior.add(item);
							creditosNivelMaximo = creditosNivelMaximo +  (item.getMtrCreditos() == 0 ? item.getMtrHoras():item.getMtrCreditos());
							if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
								creditos3eraNivelMaximo = creditos3eraNivelMaximo +  (item.getMtrCreditos() == 0 ? item.getMtrHoras():item.getMtrCreditos());
							}
						}

						Iterator<MallaCurricularNivelDto> iterMCN = fgmpgfListMallaCurricularNivelDto.iterator();
						while (iterMCN.hasNext()) {
							MallaCurricularNivelDto item = (MallaCurricularNivelDto) iterMCN.next();
							if (item.getMlcrnvNivelDto().getNvlNumeral().equals(fgmpgfNivelMaximo)) {
								fgmpgfMaximoCreditosNivelMaximo = item.getMlcrnvHoras();
							}
						}

						limitePorcentaje = calcularCuarentaPorCientoPorNivel(Long.valueOf(fgmpgfMaximoCreditosNivelMaximo));
						if (new BigDecimal(creditos3eraNivelMaximo).floatValue() > limitePorcentaje.floatValue()) {
							// dejar a libre opcion sin materias en primera o segunda
							iter = materiasAMatricularse.iterator();
							while (iter.hasNext()) {
								MateriaDto item = (MateriaDto) iter.next();
								if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
									item.setMtrCmbEstado(Boolean.FALSE);
								}
							}
							FacesUtil.mensajeInfo("Ud. supera el 40% de créditos con tercera matrícula por ello debe seleccionar una combinación inferior al porcentaje indicado.");
						}else{
							// si supera el limite dar solo nivel inferior
							iter = materiasAMatricularse.iterator();
							while (iter.hasNext()) {
								MateriaDto item = (MateriaDto) iter.next();
								if (item.getNumMatricula() > SAUConstantes.SEGUNDA_MATRICULA_VALUE) {
									item.setMtrCmbEstado(Boolean.TRUE);
								}else {
									item.setMtrCmbEstado(Boolean.FALSE);
								}
							}
							FacesUtil.mensajeInfo("Ud. supera el 40% de créditos con tercera matrícula en el siguiente nivel por ello debe seleccionar una combinación inferior al 40% en el nivel asignado.");
						}
						
						//buscar paralelos
						for (MateriaDto aux : materiasAMatricularse) {

							try{
								aux.setMtrListParalelo(servFgmpgfParaleloDto.ListarXMateriaId(aux.getMtrId()));
							} catch (ParaleloDtoException e) {
								FacesUtil.mensajeError(e.getMessage());
							} catch (ParaleloDtoNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage() + " : " + aux.getMtrDescripcion());
							}

						}

					} else {
						//buscar paralelos
						for (MateriaDto aux : materiasAMatricularse) {

							try{
								aux.setMtrCmbEstado(Boolean.TRUE);
								aux.setMtrListParalelo(servFgmpgfParaleloDto.ListarXMateriaId(aux.getMtrId()));
							} catch (ParaleloDtoException e) {
								FacesUtil.mensajeError(e.getMessage());
							} catch (ParaleloDtoNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage() + " : " + aux.getMtrDescripcion());
							}

						}
					}

					calculoPerdidaGratuidad(listMateriaReprobadas);
					return  materiasAMatricularse;

				}

			}else{				
				FacesUtil.mensajeInfo("No se encontró Asignaturas para generar matricula.");
				return retorno;
			}
		}else {
			return retorno;
		}

	}


	/**
	 * Método que permite calcular el 40% de Creditos de la Malla Curricular Nivel
	 * @param horasPorNivel - horas por semana total
	 * @return 40% de horas por semana
	 */
	private BigDecimal calcularCuarentaPorCientoPorNivel(Long horasPorNivel) {
		MathContext mc = new MathContext(6);
		BigDecimal divisor = new BigDecimal("100");
		BigDecimal constante = new BigDecimal("40");
		constante = constante.multiply(new BigDecimal(horasPorNivel,mc));
		return establecerNumeroSinAproximacion(constante.divide(divisor,mc),2);
	}


	/**
	 * Método que indica si hay mas de un nivel para matricular.
	 * @param materiasAmatricular 
	 * @return true-> si hay mas de un nivel.
	 */
	private List<Integer> verificarSiHayDosNiveles(List<MateriaDto> materiasAmatricular){
		Map<Integer,List<MateriaDto>> materiasPorNiveles=materiasAmatricular.stream().collect(Collectors.groupingBy(MateriaDto::getNvlNumeral));
		List <Integer> niveles = new ArrayList<>();
		niveles.addAll(materiasPorNiveles.keySet());
		return niveles;
	}

	
	//TODO: verificar si efectivamente aprobo 
	//TODO: obligatoriedad a tomar la asignatura - disabled
	/**
	 * Carga las materias a inscribirse un estudiante nuevo en Educacion inicial o no.
	 * @param fichaInscripcion .- Dto para la busqueda 
	 * @return Materias a matricular.
	 */
	public List<MateriaDto> cargarMateriasAMatricularNuevo(FichaInscripcionDto fichaInscripcion){
		
		fgmpgfEstudianteNuevo = true;	
		List<MateriaDto> listMateriasAMatricularse = new ArrayList<>();
		listMateriasAMatricularse.addAll(fgmpgfMallaMaterias);

//			try {
//				List<RecordEstudianteDto> matriculas = servJdbcHistorialAcademico.buscarHistorialAcademicoSIIU(fichaInscripcion.getPrsIdentificacion());
//				if (!matriculas.isEmpty()) {
//					Iterator<RecordEstudianteDto> iterator = matriculas.iterator();
//					while (iterator.hasNext()) {
//						RecordEstudianteDto item = (RecordEstudianteDto) iterator.next();
//						if (!item.getRcesCarreraDto().getCrrTipo().equals(CarreraConstantes.TIPO_NIVELEACION_VALUE)) {
//							iterator.remove();
//						}
//					}
//				}
//				
//				if (!matriculas.isEmpty()) {
//					
//					// quitar de la malla las aprobadas
//					// 
//					
//					for (RecordEstudianteDto item : matriculas) {
//						if (!item.getRcesEstado().equals(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE)) {
//							FacesUtil.mensajeError("Ud. no puede generar matrícula debido que la Asignatura " + item.getRcesMateriaDto().getMtrDescripcion() + " se encuentra en estado " + item.getRcesEstadoLabel()+".");
//						}
//					}
//				}
//				
//			} catch (RecordEstudianteNoEncontradoException e1) {
//			} catch (RecordEstudianteException e1) {
//			}
//		
		
//		MateriaDto recordEstudianteConNivelacion = null; 
		
//		try {
//			recordEstudianteConNivelacion = servFgmpgfMateriaDto.buscarRecordEstudiante(fgmpgfUsuario.getUsrIdentificacion());
//		} catch (MateriaDtoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (MateriaDtoNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		}

//		if(recordEstudianteConNivelacion==null){
//			fgmpgfEstudianteNuevo = true;
			
			//AGREGADO POR MQ, al ingresar a matricula existen consultas con fces y sino se adiciona esta linea al guardar en BDD se crea otra fces en la carrera
			//Son estudiantes con registro automatico directo a pregrado sin nivelacion
//			if(fichaInscripcion.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_DIRECTO_CARRERA_VALUE){
//				fgmpgfEstudianteNuevo = false;
//			}

//		}else{
//			fgmpgfEstudianteNuevo = false;
//			fgmpgfRecordEstudianteIDConNivelacion= recordEstudianteConNivelacion.getRcesId();
//		}

		Iterator<MateriaDto> iter = listMateriasAMatricularse.iterator();
		while (iter.hasNext()) {
			MateriaDto item = (MateriaDto) iter.next();
			if(!item.getNvlNumeral().equals(NivelConstantes.NIVEL_PRIMERO_VALUE)){
				iter.remove();
			}
		}

		for (MateriaDto aux : listMateriasAMatricularse) {
			try {
				aux.setNumMatricula(SAUConstantes.PRIMERA_MATRICULA_VALUE);
				aux.setMtrCmbEstado(Boolean.TRUE);
				aux.setMtrCmbEstadoDisable(Boolean.TRUE);//obligar a tomar
				aux.setMtrListParalelo(servFgmpgfParaleloDto.ListarXMateriaId(aux.getMtrId()));
			} catch (ParaleloDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (ParaleloDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

		}

		return listMateriasAMatricularse;
	}

	/**
	 * Muestra la informacion de cuantas materias, Horas/Creditos, porcentaje de creditos seleccionados y calcula gratuidad temporal
	 * @return retorna el arreglo de la descripcion.
	 */
	public String[] informacionSeleccion(){

		String[] retorno = new String[8];
		DecimalFormat df = new DecimalFormat("#.00"); 
		List<MateriaDto> seleccion = new ArrayList<>();
		Integer numTotal = 0;
		Integer numCreditos = 0;
		Integer numHoras = 0;
		Integer numCreditosTotales = 0;
		Integer numHorasTotales = 0;
		Double gratuidadPorcentaje = 0.0;
		Boolean creditosHoras = false; // false - horas // true creditos

		String gratuidad = null;

		if(fgmpgfListMateriaDtoAMatricularse != null && fgmpgfListMateriaDtoAMatricularse.size() >0){

			for(MateriaDto item: fgmpgfMallaMaterias){
				if(item.getNvlNumeral() == fgmpgfNivelUbicacion){

					if(item.getMtrHoras() != null && item.getMtrHoras().intValue() != 0){
						creditosHoras = false; 
						numTotal = numTotal+item.getMtrHoras();
					}
					if(item.getMtrCreditos() != null && item.getMtrCreditos().intValue() != 0){
						creditosHoras = true;
						numTotal = numTotal+item.getMtrCreditos();
					}
				}
			}

			for(MateriaDto item: fgmpgfListMateriaDtoAMatricularse){
				if(item.getNumMatricula() == SAUConstantes.PRIMERA_MATRICULA_VALUE){
					if(item.getMtrCmbEstado() != null && item.getMtrCmbEstado()){
						if(item.getMtrCreditos() != null && item.getMtrCreditos().intValue() != 0){
							numCreditos = numCreditos+item.getMtrCreditos().intValue();
						}
						if(item.getMtrHoras() != null && item.getMtrHoras().intValue() != 0){
							numHoras = numHoras+item.getMtrHoras().intValue();
						}
					}
				}
			}

			for(MateriaDto item: fgmpgfListMateriaDtoAMatricularse){
				if(item.getMtrCmbEstado() != null && item.getMtrCmbEstado()){
					seleccion.add(item);
					if(item.getMtrCreditos() != null && item.getMtrCreditos().intValue() != 0){
						numCreditosTotales = numCreditosTotales+item.getMtrCreditos().intValue();
					}
					if(item.getMtrHoras() != null && item.getMtrHoras().intValue() != 0){
						numHorasTotales = numHorasTotales+item.getMtrHoras().intValue();
					}
				}
			}


			if(numCreditos != null && numHoras != null && ( numCreditos > fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue() || numHoras > fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue())){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.informacion.seleccion.maximo.horas", fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue(), cabeceraCreditosHoras(fgmpgfListMateriaDtoAMatricularse.get(fgmpgfListMateriaDtoAMatricularse.size()-1).getMtrCreditos(), fgmpgfListMateriaDtoAMatricularse.get(fgmpgfListMateriaDtoAMatricularse.size()-1).getMtrHoras()))));
			}

			if(numTotal.intValue() != 0){
				if(!creditosHoras){
					gratuidadPorcentaje = (Double.valueOf(numHoras.intValue())*100)/Double.valueOf(numTotal.intValue());
				}

				if(creditosHoras){
					gratuidadPorcentaje = (Double.valueOf(numCreditos.intValue())*100)/Double.valueOf(numTotal.intValue());
				}

				gratuidadPorcentaje = Double.valueOf(df.format(gratuidadPorcentaje).replace(",", "."));

			}else{
				gratuidadPorcentaje = 0.0;
			}

			if(gratuidadPorcentaje >= Double.valueOf(fgmpgfTipoGratuidadParcial.getTigrPorcentajeGratuidads().get(fgmpgfTipoGratuidadParcial.getTigrPorcentajeGratuidads().size()-1).getPrgrValor().toString())){
				gratuidad = fgmpgfTipoGratuidadGratuidad.getTigrDescripcion();
				fgmpgfPerdidaGratuidadParcial = false;
			}else{
				gratuidad = fgmpgfTipoGratuidadParcial.getTigrDescripcion();
				fgmpgfPerdidaGratuidadParcial = true;
			}
		}

		if(seleccion.size() != 0){
			retorno[0] = String.valueOf(seleccion.size());
		}else{
			retorno[0] = null;
		}
		if(creditosHoras){
			retorno[1] = String.valueOf(numCreditosTotales);
		}else{
			retorno[1] = null;
		}

		if(!creditosHoras){
			retorno[2] = String.valueOf(numHorasTotales);
		}else{
			retorno[2] = null;
		}

		if(seleccion.size() != 0.0){
			retorno[3] = String.valueOf(gratuidadPorcentaje);
		}else{
			retorno[3] = null;
		}

		if(fgmpgfPerdidaGratuidadDefinitiva != null && fgmpgfPerdidaGratuidadDefinitiva){
			retorno[4] = TipoGratuidadConstantes.PERDIDA_DEFINITIVA_LABEL;
		}else{
			retorno[4] = gratuidad;
		}
		retorno[5] = String.valueOf(fgmpgfNivelUbicacion);

		if(creditosHoras){
			retorno[6] = String.valueOf(numCreditos);
		}else{
			retorno[6] = null;
		}

		if(!creditosHoras){
			retorno[7] = String.valueOf(numHoras);
		}else{
			retorno[7] = null;
		}

		return retorno; 
	}


	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------


	/**
	 * Método que permite verificar si un estudiante hace tercera matricula y aun puede continuar estudiando. 
	 * -- verificar si en homologacion entran con 2da Reprobada
	 * -- verificar si reprueban segunda matricula y deben hacer solicitud de 3era.
	 * -- verificar si Reprobo la 3era y ya queda fuera.
	 */
	public Integer verificar2da3raReingreso(FichaInscripcionDto fichaInscripcion, List<MateriaDto> recordEstudiantilPorCarrera){

		Integer retornar = 0;

		if (recordEstudiantilPorCarrera != null && recordEstudiantilPorCarrera.size() > 0) {

			List<MateriaDto> materiasReprobadas = cargarMateriasReprobadas(recordEstudiantilPorCarrera);
			List<MateriaDto> materiasAprobadas = cargarMateriasAprobadas(recordEstudiantilPorCarrera);

			// quitar materias reprobadas que ya aprobaron
			List<MateriaDto> materiasReprobadasFiltro = quitarMateriasReprobadasQueAprobo(materiasReprobadas, materiasAprobadas); //TODO: metodo para sacar sin dobles

			// contar el numero de matricula.
			if (materiasReprobadasFiltro != null && !materiasReprobadasFiltro.isEmpty() ) {
				for (MateriaDto materia : materiasReprobadasFiltro) {
					materia.setNumMatricula(calcularNumeroMatriculaVerificacionTerceras(materiasReprobadas, materia));
					if(materia.getNumMatricula() >= 3){
						FacesUtil.mensajeError("Ud. ha reprobado la Asignatura " + materia.getMtrCodigo()+ " - "+  materia.getMtrDescripcion() + " con tercera matrícula.");
						retornar = 3;
						break;
					}else if(materia.getNumMatricula() == 2) {
						if(!verificarSolicitudTerceraMatricula(materia.getMtrId(), materia.getMtrDescripcion(), fichaInscripcion.getFcesId())){
							retornar = 1;
						}
					} 
				}
			}
		}


		return retornar;	
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
				.filter(itemRecord->itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) || itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL))
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
	 * Metodo para reinicios de carrera.
	 * @param historialAcademico - todo su historico
	 * @param origen - aplicaba desde donde pero se toma referencia a si se toma en cuenta el historial academico o no
	 * @param periodoReinicio - si aplica se considera desde ese periodo en el cual se inscribio.
	 * @param carreraId - id de la carrera
	 * @return
	 */
	private List<MateriaDto> filtrarHistorialAcademicoPorCarrera(List<RecordEstudianteDto> historialAcademico, Integer origen, Integer periodoReinicio, int carreraId){
		List<MateriaDto> retorno = new ArrayList<>();

		if (origen != null) {
			if (periodoReinicio != null) {
				List<RecordEstudianteDto> recordSAIU = new ArrayList<>();

				if (origen.equals(FichaInscripcionConstantes.TIPO_REINICIO_ORIGEN_SIN_RECORD_ANTERIOR_VALUE)) {
					// solo considerar del SIIU
					for (RecordEstudianteDto item : historialAcademico) {
						if (item.getRcesCarreraDto().getCrrId() == carreraId
								&& item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU) 
//								&& item.getRcesPeriodoAcademicoDto().getPracId() >= periodoReinicio
								&& item.getRcesNivelDto().getNvlNumeral().intValue() != 0 ) { // TODO: CHEQUEAR LO DE NIVELACION
							recordSAIU.add(item);
						}
					}

				}else {
					for (RecordEstudianteDto item : historialAcademico) {
						if (item.getRcesCarreraDto().getCrrId() == carreraId
//								&& item.getRcesPeriodoAcademicoDto().getPracId() >= periodoReinicio
								&& item.getRcesNivelDto().getNvlNumeral().intValue() != 0) {
							recordSAIU.add(item);
						}
					}
				}


				if (!recordSAIU.isEmpty()) {
					for (RecordEstudianteDto it : recordSAIU) {
						MateriaDto materia = new MateriaDto();
						materia.setNvlNumeral(it.getRcesNivelDto().getNvlNumeral());
						
						if (it.getRcesMateriaDto().getMtrCreditos() != null && it.getRcesMateriaDto().getMtrCreditos() != 0) {
							materia.setMtrCreditos(it.getRcesMateriaDto().getMtrCreditos()); // horas y creditos	
						}else if (it.getRcesMateriaDto().getMtrHoras() != null && it.getRcesMateriaDto().getMtrHoras()!=0) {
							materia.setMtrCreditos(it.getRcesMateriaDto().getMtrHoras()); // horas y creditos
						}
						
						materia.setRcesEstado(it.getRcesMateriaDto().getMtrEstado());
						materia.setMtrEstadoLabel(it.getRcesMateriaDto().getMtrEstadoLabel());
						materia.setMtrCodigo(it.getRcesMateriaDto().getMtrCodigo());
						materia.setMtrDescripcion(it.getRcesMateriaDto().getMtrDescripcion());
						materia.setMtrId(it.getRcesMateriaDto().getMtrId());
						materia.setNumMatricula(it.getRcesMateriaDto().getNumMatricula());
						materia.setPracId(it.getRcesPeriodoAcademicoDto().getPracId());
						//							materia.setMlcrprId(it.getRcesMallaCurricularParalelo()); 
						retorno.add(materia);
					}
				}

			}else {
				FacesUtil.mensajeError("Ud. tiene un reinicio de Carrera sin especificar el periodo de inicio.");	
			}
		}else{
			FacesUtil.mensajeError("Ud. tiene un reinicio de Carrera sin especificar el Origen.");		
		}

		return retorno;
	}

	/**
	 * Método que realiza la conversion de objetos para mostrar en pantalla.
	 * @param historialAcademico - todo el historico del estudiante.
	 * @return record de la carrera seleccionada.
	 */
	private List<MateriaDto> filtrarHistorialAcademicoPorCarrera(List<RecordEstudianteDto> historialAcademico, int carreraId){
		List<MateriaDto> retorno = new ArrayList<>();

		if (!historialAcademico.isEmpty()) {
			for (RecordEstudianteDto it : historialAcademico) {
				// TODO: TOMAR EN CUENTA CUANDO SE MATRICULE NIVELACION - > misma carrera y diferente de nivelacion
				if (it.getRcesCarreraDto().getCrrId() == carreraId && it.getRcesNivelDto().getNvlNumeral().intValue() != 0) {
					MateriaDto materia = new MateriaDto();
					materia.setNvlNumeral(it.getRcesNivelDto().getNvlNumeral());
					
					if (it.getRcesMateriaDto().getMtrCreditos() != null && it.getRcesMateriaDto().getMtrCreditos() != 0) {
						materia.setMtrCreditos(it.getRcesMateriaDto().getMtrCreditos()); // horas y creditos	
					}else if (it.getRcesMateriaDto().getMtrHoras() != null && it.getRcesMateriaDto().getMtrHoras()!=0) {
						materia.setMtrCreditos(it.getRcesMateriaDto().getMtrHoras()); // horas y creditos
					}

					materia.setRcesEstado(it.getRcesMateriaDto().getMtrEstado());
					materia.setMtrEstadoLabel(it.getRcesMateriaDto().getMtrEstadoLabel());
					materia.setMtrCodigo(it.getRcesMateriaDto().getMtrCodigo());
					materia.setMtrDescripcion(it.getRcesMateriaDto().getMtrDescripcion());
					materia.setMtrId(it.getRcesMateriaDto().getMtrId());
					materia.setNumMatricula(it.getRcesMateriaDto().getNumMatricula());
					materia.setPracId(it.getRcesPeriodoAcademicoDto().getPracId());
					
					retorno.add(materia);
				}
			}
		}

		return retorno;
	}


	/**
	 * Verifica los parametros necesarios en ls elementos seleccionados
	 * @return valida o no para generar matricula
	 */
	public Boolean verificarSeleccion(Boolean mensaje){
		Boolean verificar = true;

		List<MateriaDto> materiasSeleccionadas = new ArrayList<>();

		if (fgmpgfListMateriaDtoAMatricularse != null && fgmpgfListMateriaDtoAMatricularse.size() > 0  ) {

			Integer numeroTotalCreditosHoras = 0;
			Boolean seleccionMaterias = false;
			MateriaDto paraleloNoSeleccionado = null;

			for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
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

			for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
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

			if(numeroTotalCreditosHoras.intValue() <= fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue()){

				List<List<MateriaDto>> listasMateriasSeleccion = new ArrayList<>();

				Integer nivelAsignacion = 0;
				Integer numeroCreditos = 0;
				List<Integer> creditosXSemestre = new ArrayList<>();
				List<Integer> nivelXSemestre = new ArrayList<>();

				for(MateriaDto item: materiasSeleccionadas){
					List<MateriaDto> list = new ArrayList<>();
					for(MateriaDto itemR: materiasSeleccionadas){
						if( item.getNvlNumeral() == itemR.getNvlNumeral()){
							list.add(itemR);
						} 
					}
					if(list.size() > 0){
						listasMateriasSeleccion.add(list);
					}
				}

				for (int i = 0; i < listasMateriasSeleccion.size(); i++) {

					for(MateriaDto itemR: listasMateriasSeleccion.get(i)){

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

					for (int i = 0; i < creditosXSemestre.size(); i++) {
						for (int j = 0; j < creditosXSemestre.size(); j++) {
							if(creditosXSemestre.get(i) > creditosXSemestre.get(j)){
								numeroCreditos = creditosXSemestre.get(i);
								nivelAsignacion = nivelXSemestre.get(i);
							}
							if(creditosXSemestre.get(i) == creditosXSemestre.get(j)){
								if(nivelXSemestre.get(i) < nivelXSemestre.get(j)){
									numeroCreditos = creditosXSemestre.get(i);
									nivelAsignacion = nivelXSemestre.get(i);
								}
							}
						}
					}

				}else{
					numeroCreditos = creditosXSemestre.get(creditosXSemestre.size()-1);
					nivelAsignacion = nivelXSemestre.get(nivelXSemestre.size()-1);
				}

				fgmpgfNivelUbicacion = nivelAsignacion;

			}else{
				FacesUtil.limpiarMensaje();
				if(!mensaje){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.seleccion.seleccione.maximo.creditos.horas", fgmpgfCarreraSeleccion.getCrrNumMaxCreditos(), cabeceraCreditosHoras(fgmpgfListMateriaDtoAMatricularse.get(fgmpgfListMateriaDtoAMatricularse.size()-1).getMtrCreditos(), fgmpgfListMateriaDtoAMatricularse.get(fgmpgfListMateriaDtoAMatricularse.size()-1).getMtrHoras() ))));
				}
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
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.cupos.llenos")));
			verificar = false;
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.cupos.llenos")));
			verificar = false;
		}

		return verificar;
	}

	/**
	 * Verifica cronogramas de matriculas para calcular pago
	 * @return retorna si se encuentra en algun periodo
	 */
	public Boolean validarCronograma(){

		Date fechaActual = new Date();
		Boolean verificar =  false; 

		try {

			if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
				fgmpgfFechasMatriculaOrdinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
				fgmpgfFechasMatriculaExtraordinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
				fgmpgfFechasMatriculaEspecial = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			} else {
				fgmpgfFechasMatriculaOrdinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
				fgmpgfFechasMatriculaExtraordinaria = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
				fgmpgfFechasMatriculaEspecial = servFgmpgfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			}

			Date fechaInicioMatriculasOrdinarias = new Date(fgmpgfFechasMatriculaOrdinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasOrdinarias = new Date(fgmpgfFechasMatriculaOrdinaria.getPlcrFechaFin().getTime());

			Date fechaInicioMatriculasExtraordinarias = new Date(fgmpgfFechasMatriculaExtraordinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasExtraordinarias = new Date(fgmpgfFechasMatriculaExtraordinaria.getPlcrFechaFin().getTime());

			Date fechaInicioMatriculasEspecial = new Date(fgmpgfFechasMatriculaEspecial.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasEspecial = new Date(fgmpgfFechasMatriculaEspecial.getPlcrFechaFin().getTime());

			if(fechaFinMatriculasEspecial.after(fechaActual)){
				FacesUtil.limpiarMensaje();
				if(fechaInicioMatriculasEspecial.before(fechaActual)){ 
					verificar = true;
					FacesUtil.mensajeInfo("Periodo de matriculas especiales.");
				}else{
					FacesUtil.mensajeError("Periodo de matriculas especiales no ha iniciado.");
				}
			}else{
				FacesUtil.mensajeError("Periodo de matriculas especiales ha expirado.");
			}

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

			//			if(!verificar){
			//				FacesUtil.limpiarMensaje();
			//				FacesUtil.mensajeError("Periodo de matriculas ha expirado.");
			//			}

		} catch (CronogramaActividadDtoJdbcException e) { 
			e.printStackTrace();
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) { 
		}

		return verificar;
	}


	/**
	 * Método que permite validar si existe cronograma activo para ese nivel.
	 * @return true - si hay cronograma.
	 */
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
				FacesUtil.mensajeError("Cronograma no habilitado.");
			}
			
			if(!mensaje){
				FacesUtil.limpiarMensaje();
			}

		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Cronograma no habilitado.");
		}

		return verificar;
	}

	/**
	 * Verifica que el usuario no refleje matricula vigente en este periodo
	 * @return Retorna si esta amtriculado o no
	 */
	public Boolean verificarNoMatriculado(){

		Boolean verificar = true;
		List<FichaMatriculaDto> fichaMatriculaDtoAux = null;

		try {

			fichaMatriculaDtoAux = servFgmpgfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(fgmpgfPeriodoAcademico.getPracId(), fgmpgfUsuario.getUsrIdentificacion(), fgmpgfBuscarFichaInscripcionDto.getCrrId());
			verificar = false;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.matricula.generada.exception",fgmpgfBuscarFichaInscripcionDto.getCrrDetalle())));

		} catch (FichaMatriculaException e) {
			//			FacesUtil.mensajeError(e.getMessage());
		}

		if (fichaMatriculaDtoAux != null && fichaMatriculaDtoAux.size() > 0) {
			verificar = false;
		}

		return verificar;
	}



	/**
	 * Deshabilita los checks para las materias obligatorias 
	 * @param materia .- Dto para la busqueda
	 * @return retorna la disable / no disable
	 */
	public Boolean disableCheck(MateriaDto materia){

		Boolean verificar = false;

		if (!isEstudianteConTercera) {// gente normal
			if (!fgmpgfNivelSuperior.equals(GeneralesConstantes.APP_ID_BASE) && !fgmpgfNivelInferior.equals(GeneralesConstantes.APP_ID_BASE)) {
				if(materia.getNvlNumeral().equals(fgmpgfNivelInferior)){
					materia.setMtrCmbEstado(true);
					verificar = true;
				}
			}else {// un solo nivel
				materia.setMtrCmbEstado(true);
				verificar = true;
			}
		}else {// desactivar en funcion al 40%
			if (materia.getMtrCmbEstado()) {
				verificar = true;
			}
		}
		
		return verificar;
	}


	/**
	 * Calcula la perdida de gratuidad definitiva del estudiante
	 * @param materiasReprobadas .- Lista de materias reprobadas
	 * @return retorna si perdio o no gratuidad
	 */
	public Boolean calculoPerdidaGratuidad(List<MateriaDto> materiasReprobadas){

		DecimalFormat df = new DecimalFormat("#.00"); 

		List<MateriaDto> mallaMaterias = null;
		Integer numeroTotalMalla = 0;
		Integer numeroTotalPeridido = 0;
		Double gratuidaPorcentaje = 0.0;
		boolean perdidaDefinitiva = false;

		try {

			try {
				FichaEstudiante fichaEstudiante = servFgmpgfFichaEstudianteServicio.buscarPorId(fgmpgfBuscarFichaInscripcionDto.getFcesId());
				if(fichaEstudiante!= null && fichaEstudiante.getFcesTipoUnivEstudPrev() != null && fichaEstudiante.getFcesTipoUnivEstudPrev() == FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE){
					perdidaDefinitiva = true;
				}
			} catch (FichaEstudianteNoEncontradoException e) {
			} catch (FichaEstudianteException e) {
			}

			if(fgmpgfBuscarFichaInscripcionDto.getFcinTipoIngreso() != FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE
					|| !perdidaDefinitiva){

				if(materiasReprobadas != null && materiasReprobadas.size() > 0 ){
					mallaMaterias = servFgmpgfMateriaDto.listarMateriasxCarreraFull(fgmpgfBuscarFichaInscripcionDto.getCrrId(), new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_EN_CIERRE_VALUE});

					for(MateriaDto item: mallaMaterias){
						if(item.getMtrHoras() != null && item.getMtrHoras() != 0){
							numeroTotalMalla = numeroTotalMalla+item.getMtrHoras();
						}
						if(item.getMtrCreditos() != null && item.getMtrCreditos() != 0){
							numeroTotalMalla = numeroTotalMalla+item.getMtrCreditos();
						} 
					}

					for(MateriaDto item: materiasReprobadas){
						for(MateriaDto it: mallaMaterias){
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
							perdidaDefinitiva = true;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.calculo.perdida.gratuidad.definitiva")));
						}

					}

				}
			}else{
				try {
					FichaEstudiante fichaEstudiante = servFgmpgfFichaEstudianteServicio.buscarPorId(fgmpgfBuscarFichaInscripcionDto.getFcesId());
					if(fichaEstudiante.getFcesTipoUnivEstudPrev() == FichaEstudianteConstantes.TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE){
						perdidaDefinitiva = true;
						FacesUtil.mensajeError("Usted ha perdido Gratuidad definitiva, ya que está cursando una segunda carrera.");
					}
				} catch (FichaEstudianteNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FichaEstudianteException e) {
					FacesUtil.mensajeError(e.getMessage());
				}

			}


		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		fgmpgfPerdidaGratuidadDefinitiva = perdidaDefinitiva; 

		return perdidaDefinitiva;
	}

	/**
	 * Metodo de verificacion de disponibilidad de la materia en el paralelo seleccionado
	 */
	public void verificarClickDisponibilidad(MateriaDto materiaSeleccionada, Boolean isSelected){
		boolean asignarParalelos = false; 

		if(isSelected){

			if (isRegularMatricula) {

				if(materiaSeleccionada.getPrlId() != GeneralesConstantes.APP_ID_BASE){

					if (verificarMateriasConHorario(fgmpgfListMateriaDtoAMatricularse, materiaSeleccionada.getPrlId())) { // verificar horarios /  si es modular ->  a los módulos
						asignarParalelos = true;
					}


					if (asignarParalelos) {
						for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
							it.setPrlId(materiaSeleccionada.getPrlId());
							it.setMtrCmbEstado(true);
						}
					}else {
						for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
							it.setPrlId(GeneralesConstantes.APP_ID_BASE);
							it.setMtrCmbEstado(true);
						}
					}
				}else{
					for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
						it.setPrlId(GeneralesConstantes.APP_ID_BASE);
						it.setMtrCmbEstado(true);
					}
				}

			}else {
				if(materiaSeleccionada.getPrlId() != GeneralesConstantes.APP_ID_BASE){

					if (verificarCruceHorarios(materiaSeleccionada, fgmpgfListMateriaDtoAMatricularse)) {// verificar horarios /  si es modular ->  a los módulos
//						 materiaSeleccionada.setMtrCmbEstado(redisableSeleccionMaterias(materiaSeleccionada).getMtrCmbEstado());
						materiaSeleccionada.setMtrCmbEstado(false);
						materiaSeleccionada.setPrlId(GeneralesConstantes.APP_ID_BASE);
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
		}

		calcularNivelAsignado();
		informacionSeleccion();
	}

	/**
	 * Método que permite verificar si la asignatura tiene cargado Horas de Clase
	 * @param materiasAmatricular
	 * @param paraleloId 
	 * @return false - si hay alguna asignatura sin horario.
	 */
	public boolean verificarMateriasConHorario(List<MateriaDto> materiasAmatricular, int paraleloId) {

		boolean  estado = true;

		if (materiasAmatricular != null && materiasAmatricular.size() > 0) {

			for (MateriaDto mat : materiasAmatricular) {

				List<HorarioAcademicoDto> horarioCarrito = null;
				try {

					if (!mat.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
						horarioCarrito = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(paraleloId, mat.getMlcrmtId(), fgmpgfPeriodoAcademico.getPracId());
						
						
						if (horarioCarrito != null && horarioCarrito.size() > 0) {
							for (HorarioAcademicoDto it : horarioCarrito) {
								if (it.getHracDia() == null || it.getHoclHoInicio() == null || it.getHoclHoFin() == null) {
									FacesUtil.mensajeError("El Horario de Clases no ha sido cargado en la Asignatura "+mat.getMtrDescripcion()+". Comuníque a la Secretaría de la Carrera.");
									estado = false;
									break;
								}
							}
						}
						
						
						
					}else {
						try {
							List<MateriaDto> modulos = servJdbcMallaCurricularMateriaDto.buscarModulos(mat.getMtrId());
							if (!modulos.isEmpty()) {
								for (MateriaDto modulo : modulos) {
									horarioCarrito = servHorarioAcademicoDtoServicioJdbc.buscarHorarioFull(paraleloId, modulo.getMlcrmtId(), fgmpgfPeriodoAcademico.getPracId());
									if (horarioCarrito != null && horarioCarrito.size() > 0) {
										for (HorarioAcademicoDto it : horarioCarrito) {
											if (it.getHracDia() == null || it.getHoclHoInicio() == null || it.getHoclHoFin() == null) {
												FacesUtil.mensajeError("El Horario de Clases no ha sido cargado en la Asignatura "+modulo.getMtrDescripcion()+". Comuníque a la Secretaría de la Carrera.");
												estado = false;
												break;
											}
										}
									}
								}
							}
						} catch (MallaCurricularMateriaException e) {
							estado = false;
							FacesUtil.mensajeError(e.getMessage());
						} catch (MallaCurricularMateriaValidacionException e) {
							estado = false;
							FacesUtil.mensajeError(e.getMessage());
						} catch (MallaCurricularMateriaNoEncontradoException e) {
							estado = false;
							FacesUtil.mensajeError(e.getMessage());
						}

					}

				} catch (HorarioAcademicoDtoException e) {
					estado = false;
					FacesUtil.mensajeError(e.getMessage());
				} catch (HorarioAcademicoDtoNoEncontradoException e) {
					estado = false;
					FacesUtil.mensajeError("El Horario de Clases no ha sido cargado en la Asignatura "+mat.getMtrDescripcion()+". Comuníque a la Secretaría de la Carrera.");
				}

			}

		} else {
			estado = false;
		}

		if (estado) {//si hay horarios
			boolean cupos = true;
			for (MateriaDto materia : materiasAmatricular) {
				cupos = verificarCupos(materia.getMlcrmtId(), paraleloId, fgmpgfPeriodoAcademico.getPracId(), true);// true - si hay cupos
				if (!cupos) {
					estado = false;
				}
			}
			
		}
		
		return estado;
	}


	/**
	 * MÃ©todo que verifica el cruce de horas.
	 * @param materia materia por verificar si existe algÃºn cruce en el horario.
	 * @param materias lista de materias tomadas por el estudiante.
	 * @return true si hay algun cruce de horas o false si no hay ningun cruce de horas.
	 */
	private boolean verificarCruceHorarios(MateriaDto materiaElejida, List<MateriaDto> materiasAmatricular) {
		boolean retorno = false;

		List<HorarioAcademicoDto> horarioSeleccion =  new ArrayList<>();
		if (!materiasAmatricular.isEmpty()) {

			if(!fgmpgfBuscarFichaInscripcionDto.getCncrModalidad().equals(ModalidadConstantes.TIPO_MODALIDAD_DISTANCIA_VALUE)){

				horarioSeleccion = cargarHorarioAcademico(materiaElejida);
				if (!horarioSeleccion.isEmpty()) {

					for (MateriaDto materia : materiasAmatricular) {

						List<HorarioAcademicoDto> horarioCarrito = new ArrayList<>();
						if(materia.getMtrCmbEstado() != null && materia.getMtrCmbEstado() && materia.getPrlId()!= null && materia.getPrlId() != GeneralesConstantes.APP_ID_BASE){

							horarioCarrito = cargarHorarioAcademico(materia);
							if (!horarioCarrito.isEmpty()) {
								if(materia.getPrlId() != materiaElejida.getPrlId() && materiaElejida.getMlcrmtId() != materia.getMlcrmtId()){
									for (HorarioAcademicoDto hor1 : horarioCarrito) {
										for (HorarioAcademicoDto hor2 : horarioSeleccion) {
											if (hor2.getHracDia().equals(hor1.getHracDia())) {
												if ( hor2.getHoclHoInicio().equals(hor1.getHoclHoInicio()) && hor2.getHoclHoFin().equals(hor1.getHoclHoFin())) {
													FacesUtil.mensajeError("El paralelo seleccionado genera un cruce en el horario de su matrícula." + materia.getMtrDescripcion());
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
			}else{
				// TODO:  VERIFICAR QUE TENGAN HORARIO
				horarioSeleccion = cargarHorarioAcademico(materiaElejida);
				if (horarioSeleccion.isEmpty()) {
					retorno = true;
				}
			}
		}
		
		if (!retorno) {//si no hay cruces
			boolean cupos = verificarCupos(materiaElejida.getMlcrmtId(), materiaElejida.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), true);// true - si hay cupos
			if (!cupos) {
				retorno = true;
			}
		}
		
		return retorno;
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
				List<Arancel> aranceles = servFgmpgfArancelServicio.listarXGratuidadXTipoMatriculaXModalidadXTipoArancel(calcularGratuidad().intValue(), fgmpgfProcesoFlujo.getPrlfId(), fgmpgfBuscarFichaInscripcionDto.getCncrModalidad(), tipoArancel);
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
	
	
	private boolean verificarSolicitudTerceraMatricula(int materiaId, String mtrDescripcion, int fcesId){
		boolean retorno = false;
		
		try {
			SolicitudTerceraMatricula solicitud = servFgmpgfSolicitudTerceraMatriculaServicio.buscarSolicitudXMtrIdXFcesIdxEstado(materiaId, fcesId);
			//si la materia se encuentra en estado solicitud 
			if(solicitud.getSltrmtTipo() == SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_VALUE){
				// si solicitud aprobada
				if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE){
					isEstudianteConTercera = Boolean.TRUE;
					retorno = Boolean.TRUE;
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.solicitud.aprobada", mtrDescripcion)));
				}
			}
			//si la materia se encuentra en estado apelacion 
			if(solicitud.getSltrmtTipo() == SolicitudTerceraMatriculaConstantes.TIPO_APELACION_VALUE){
				if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE){
					isEstudianteConTercera = Boolean.TRUE;
					retorno = Boolean.TRUE;
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.apelacion.aprobada", mtrDescripcion)));
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
	
	/**
	 * Verificacion de resultado de solicitud y apelacion de teceras matriculas
	 * @param mtrId .- Materia id a buscar
	 * @param fcesId .- Ficha estudiante id a buscar
	 * @return true si la tercera esta aprobada.
	 **/
//	public Boolean verificarSolicitudTerceraMatricula(Materia materia, int fcesId){
//		
//		try {
//			
//			
////			SolicitudTerceraMatricula solicitud = servFgmpgfSolicitudTerceraMatriculaServicio.buscarSolicitudXMtrIdXFcesId(materia.getMtrId(), fcesId);
//			SolicitudTerceraMatricula solicitud = servFgmpgfSolicitudTerceraMatriculaServicio.buscarSolicitudXMtrIdXFcesIdxEstado(materia.getMtrId(), fcesId);
//
//			//si la materia se encuentra en estado solicitud 
//			if(solicitud.getSltrmtTipo() == SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_VALUE){
//				// si solicitud aprobada
//				if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE){
//					isEstudianteConTercera = Boolean.TRUE;
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.solicitud.aprobada", materia.getMtrDescripcion())));
//				}
////				
////				else if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE){
////					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.solicitud.negada", materia.getMtrDescripcion())));
////				}else{
////					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.solicitud.proceso", materia.getMtrDescripcion())));
////				}
//			}
//
//			//si la materia se encuentra en estado apelacion 
//			if(solicitud.getSltrmtTipo() == SolicitudTerceraMatriculaConstantes.TIPO_APELACION_VALUE){
//				if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE){
//					isEstudianteConTercera = Boolean.TRUE;
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.apelacion.aprobada", materia.getMtrDescripcion())));
//				}
////				else if(solicitud.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE){
////					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.apelacion.negada", materia.getMtrDescripcion())));
////				}else{
////					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.solicitud.tercera.matricula.apelacion.proceso", materia.getMtrDescripcion())));
////				}
//			}
//
//		} catch (SolicitudTerceraMatriculaNoEncontradoException e) {
//			FacesUtil.mensajeError("No se encontró solicitud aprobada de tercera matrícula en la materia solicitada. " + materia.getMtrDescripcion());
//		} catch (SolicitudTerceraMatriculaValidacionException e) {
//			FacesUtil.mensajeError("Se encontró varias solicitudes aprobadas de tercera matrícula para la materia solicitada. " + materia.getMtrDescripcion());
//		} catch (SolicitudTerceraMatriculaException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		}
//		
//		return isEstudianteConTercera;
//	}
	
	/**
	 * Calcula el nivel asignado al estudiante segun sus materias seleccionadas y numero total de creditos
	 * @param mensaje .- Si se emite mensaje o no. 
	 **/
	public void calcularNivelAsignado(){

		List<MateriaDto> materiasSeleccionadas = new ArrayList<>();
		Integer numeroTotalCreditosHoras = 0; 

		if (fgmpgfListMateriaDtoAMatricularse != null && fgmpgfListMateriaDtoAMatricularse.size() > 0  ) {

			for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
				if(it.getMtrCmbEstado() != null && it.getMtrCmbEstado()){
					materiasSeleccionadas.add(it);
					if(it.getMtrCreditos() != null && it.getMtrCreditos() != 0){
						numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrCreditos();
					}
					if(it.getMtrHoras() != null && it.getMtrHoras() != 0){
						numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHoras();
					}
				}
			}
			
			if(materiasSeleccionadas.size() == 0){
				for (MateriaDto it : fgmpgfListMateriaDtoAMatricularse) {
					materiasSeleccionadas.add(it);
					if(it.getMtrCreditos() != null && it.getMtrCreditos() != 0){
						numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrCreditos();
					}
					if(it.getMtrHoras() != null && it.getMtrHoras() != 0){
						numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHoras();
					}
				}
			}

//			if(numeroTotalCreditosHoras.intValue() <= fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue()){

				List<List<MateriaDto>> listasMateriasSeleccion = new ArrayList<>();

				Integer nivelAsignacion = 0;
				Integer numeroCreditos = 0;
				List<Integer> creditosXSemestre = new ArrayList<>();
				List<Integer> nivelXSemestre = new ArrayList<>();

				for(MateriaDto item: materiasSeleccionadas){
					if(item.getMtrCmbEstado() != null && item.getMtrCmbEstado()){
						List<MateriaDto> list = new ArrayList<>();
						for(MateriaDto itemR: materiasSeleccionadas){
							if( item.getNvlNumeral() == itemR.getNvlNumeral()){
								list.add(itemR);
							} 
						}
						if(list.size() > 0){
							listasMateriasSeleccion.add(list);
						}
					}
				}

				if(listasMateriasSeleccion.size() == 0){
					for(MateriaDto item: materiasSeleccionadas){
						List<MateriaDto> list = new ArrayList<>();
						for(MateriaDto itemR: materiasSeleccionadas){
							if( item.getNvlNumeral() == itemR.getNvlNumeral()){
								list.add(itemR);
							} 
						}
						if(list.size() > 0){
							listasMateriasSeleccion.add(list);
						}
					}
				} 
				
				for (int i = 0; i < listasMateriasSeleccion.size(); i++) {

					for(MateriaDto itemR: listasMateriasSeleccion.get(i)){

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

					for (int i = 0; i < creditosXSemestre.size(); i++) {
						for (int j = 0; j < creditosXSemestre.size(); j++) {
							if(creditosXSemestre.get(i) > creditosXSemestre.get(j)){
								numeroCreditos = creditosXSemestre.get(i);
								nivelAsignacion = nivelXSemestre.get(i);
							}
							if(creditosXSemestre.get(i) == creditosXSemestre.get(j)){
								if(nivelXSemestre.get(i) < nivelXSemestre.get(j)){
									numeroCreditos = creditosXSemestre.get(i);
									nivelAsignacion = nivelXSemestre.get(i);
								}
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
//		}
	}
	
 
	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/
	
	// >>----------------------------------OTROS_METODOS----------------------------------------
	
	public void leyendaDialogo(){
		if (fgmpgfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
			fgmpgfMensajeDlg = "Está seguro de generar la matrícula, no se podrá alterar el registro después de este proceso.";
		}else {
			fgmpgfMensajeDlg = "Está seguro de generar la matrícula, no se podrá aumentar ni disminuir materias después de este proceso.";
		}
	}
	
	
	/**
	 * Calcula la gratuidad de la matricula
	 * @return retorna el id de TipoGratuidad
	 */
	public Integer calcularGratuidad(){
		Integer gratuidadId = null;

			if(!fgmpgfPerdidaGratuidadDefinitiva){
				if(!fgmpgfPerdidaGratuidadParcial){
					gratuidadId = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_VALUE;
				}else{
					gratuidadId = GratuidadConstantes.GRATUIDAD_PERDIDA_TEMPORAL_VALUE;
				}
			}else{
				gratuidadId = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_VALUE;
			}
			 
		return gratuidadId;
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
	
	/**
	 * Cabecera de la columna que varia entre creditos y horas 
	 */
	public String obtenerDescripcionGratuidad(int tipoGratuidad){
		
		String descripcion = null;
		
		switch (tipoGratuidad) {
		case 1:
			descripcion = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_LABEL;
			break;
		case 2:
			descripcion = GratuidadConstantes.GRATUIDAD_PERDIDA_TEMPORAL_LABEL;
			break;
		case 3:
			descripcion = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_LABEL;
			break;

		default:
			descripcion = GratuidadConstantes.GRATUIDAD_ERROR;
			break;
		}
		return descripcion;
	}
	
	/**
	 * Setear estado de seleccion de las materias
	 * @param materia .- Materia a afectar 
	 * @return Efecto sobre la materia
	 */
	public MateriaDto redisableSeleccionMaterias(MateriaDto materia){

		if (!isEstudianteConTercera) {// gente normal
			if (!fgmpgfNivelSuperior.equals(GeneralesConstantes.APP_ID_BASE) && !fgmpgfNivelInferior.equals(GeneralesConstantes.APP_ID_BASE)) {
				if(materia.getNvlNumeral().equals(fgmpgfNivelInferior)){
					materia.setMtrCmbEstado(Boolean.TRUE);
					materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
				}
			}else {// un solo nivel
				materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
				materia.setMtrCmbEstado(Boolean.FALSE);
			}
		}else {
			if (materia.getNumMatricula() == SAUConstantes.TERCERA_MATRICULA_VALUE) {
				materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
				materia.setMtrCmbEstado(true);
			}else{
				if (!fgmpgfNivelSuperior.equals(GeneralesConstantes.APP_ID_BASE) && !fgmpgfNivelInferior.equals(GeneralesConstantes.APP_ID_BASE)) {
					if(materia.getNvlNumeral().equals(fgmpgfNivelInferior)){
						materia.setMtrCmbEstado(Boolean.TRUE);
						materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
					}
				}else {// un solo nivel
					materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
					materia.setMtrCmbEstado(Boolean.FALSE);
				}
			}
		}

		for(MateriaDto item: fgmpgfListMateriaDtoAMatricularse){
			if(item.getPrlId() != null && item.getPrlId() != GeneralesConstantes.APP_ID_BASE){
				if(verificarCupos(item.getMlcrmtId(), item.getPrlId(), fgmpgfPeriodoAcademico.getPracId(), false)){
					item.setMtrCmbEstado(Boolean.TRUE);
				}else{
					item.setMtrCmbEstado(Boolean.FALSE);
					item.setPrlId(GeneralesConstantes.APP_ID_BASE);;
				}
			}
		}
		
		return materia;
	}
	
	/**
	 * Reinicia el valor del check y paralelo de la materia seleccionada
	 * @param materia .- materia a usar
	 */
	public void resetComboParalelo(MateriaDto materia) {
		calcularNivelAsignado();
		
		//verificar el 40%
		if (isEstudianteConTercera) {
			BigDecimal limitePorcentaje = BigDecimal.ZERO;

			int creditosSeleccionados = 0;
			Iterator<MateriaDto> iter = fgmpgfListMateriaDtoAMatricularse.iterator();
			while (iter.hasNext()) {
				MateriaDto mat = (MateriaDto) iter.next();
				if (mat.getMtrCmbEstado()) {
					creditosSeleccionados = creditosSeleccionados + (mat.getMtrCreditos() == 0 ? mat.getMtrHoras(): mat.getMtrCreditos());
				}
			}

			int horasMaximaPorNivel = 0;
			if (fgmpgfNivelUbicacion.equals(fgmpgfNivelInferior)) {
				horasMaximaPorNivel = fgmpgfMaximoCreditosNivelInferior;
			}else if (fgmpgfNivelUbicacion.equals(fgmpgfNivelSuperior)) {
				horasMaximaPorNivel = fgmpgfMaximoCreditosNivelSuperior;
			}else if (fgmpgfNivelUbicacion.equals(fgmpgfNivelMaximo)) {
				horasMaximaPorNivel = fgmpgfMaximoCreditosNivelMaximo;
			}

			limitePorcentaje = calcularCuarentaPorCientoPorNivel(Long.valueOf(horasMaximaPorNivel));
			if (new BigDecimal(creditosSeleccionados).floatValue() > limitePorcentaje.floatValue()) {
				if (fgmpgfListMateriaDtoAMatricularse.size()>1) {
					materia.setMtrCmbEstado(Boolean.FALSE);
					FacesUtil.mensajeInfo("Ud. tiene Asignaturas con tercera matrícula y supera el 40% de créditos/horas en el nivel, si desea hacer cambios recargue la página y vuelva a seleccionar.");					
				}
			}
		}
		
		informacionSeleccion();
		filtrarCorequisitosAMatricular(materia);
		materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
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
				List<MateriaDto> prerequisitos = servPrerequisitoDtoServicioJdbc.listarXidMateriaFull(item.getMtrId());
				 
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
	 * Filtra corequisits de las materias a matricular
	 * @param materiasPorTomar .- Materias  filtar
	 * @return lista de materias a matricular filtradas por prerequisitos
	 */
	public void filtrarCorequisitosAMatricular(MateriaDto materia) {
		
		List<MateriaDto> corequisitos = null;
		try{
			corequisitos = servFgmpgfCorrequisitoServicioJdbc.listarXidMateriaFull(materia.getMtrId());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			corequisitos = null;
		}

		if(corequisitos != null && corequisitos.size() >0){
			for(MateriaDto it: corequisitos){
				for(MateriaDto item: fgmpgfListMateriaDtoAMatricularse){
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
					obtenerDescripcionGratuidad(calcularGratuidad()),
					numeroComprobante.getCmpaTotalPago().toString()
					);
			
			pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,MailConstantes.ENCABEZADO_CORREO_REGISTRO_MATRICULA_LABEL+fgmpgfPeriodoAcademico.getPracDescripcion().replace("-", " - "),
					sbCorreo.toString() , "admin", "dt1c201s", true, ReporteRegistroMatriculaForm.generarReporteRegistroMatriculaMail(listMateriaDto, nivel, fgmpgfPeriodoAcademico, fichaInscripcionDto, fgmpgfUsuario, fgmpgfDependenciaBuscar, fgmpgfCarreraSeleccion, obtenerDescripcionGratuidad(calcularGratuidad())), "Registro de Matricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
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

			sbCorreo= MailRegistroMatricula.generarMailOrdenCobro(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
					GeneralesUtilidades.generaStringParaCorreo(sbNombres.toString()) , GeneralesUtilidades.generaStringParaCorreo(fgmpgfCarreraSeleccion.getCrrDescripcion()),
					GeneralesUtilidades.generaStringParaCorreo(nivel.getNvlDescripcion()),  
					fgmpgfDependenciaBuscar.getDpnDescripcion(),
					obtenerDescripcionGratuidad(calcularGratuidad()),
					numeroComprobante.getCmpaTotalPago().toString()
					);

			pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,MailConstantes.ENCABEZADO_CORREO_ORDEN_COBRO_LABEL+fgmpgfPeriodoAcademico.getPracDescripcion().replace("-", " - "),
					sbCorreo.toString() , "admin", "dt1c201s", true, ReporteRegistroMatriculaForm.generarReporteOrdenCobroMail(listMateriaDto, nivel, numeroComprobante, fgmpgfPeriodoAcademico, fichaInscripcionDto, fgmpgfUsuario, fgmpgfDependenciaBuscar, fgmpgfCarreraSeleccion, obtenerDescripcionGratuidad(calcularGratuidad())), "Orden de Cobro."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
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
	
	

	

	private int calcularNumeroMatricula(List<MateriaDto> reprobadas, MateriaDto item){
		int retorno = 1;
		
		if (!reprobadas.isEmpty()) {
			reprobadas.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			for (MateriaDto itemRecord : reprobadas) {
				if (itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && itemRecord.getMtrCodigo().equals(item.getMtrCodigo())) {
					if (itemRecord.getNumMatricula() == 2 && itemRecord.getPracId() == PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE) {
						retorno = itemRecord.getNumMatricula();
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
	
	private int calcularNumeroMatriculaVerificacionTerceras(List<MateriaDto> reprobadas, MateriaDto item){
		int retorno = 0;
		
		if (!reprobadas.isEmpty()) {
			reprobadas.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
			for (MateriaDto itemRecord : reprobadas) {
				if ((itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL) && itemRecord.getMtrCodigo().equals(item.getMtrCodigo())) || (itemRecord.getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL) && itemRecord.getMtrCodigo().equals(item.getMtrCodigo()))) {
					if (itemRecord.getNumMatricula() == 2 && itemRecord.getPracId() == PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE) {
						retorno = retorno + itemRecord.getNumMatricula();
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

	public List<CarreraDto> getFmfListCarreraDto() {
		fgmpgfListCarreraDto = fgmpgfListCarreraDto==null?(new ArrayList<CarreraDto>()):fgmpgfListCarreraDto;
		return fgmpgfListCarreraDto;
	}

	public void setFmfListCarreraDto(List<CarreraDto> fgmpgfListCarreraDto) {
		this.fgmpgfListCarreraDto = fgmpgfListCarreraDto;
	}

	public List<FichaInscripcionDto> getFgmpgfListFichaInscripcionDto() {
		fgmpgfListFichaInscripcionDto = fgmpgfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmpgfListFichaInscripcionDto;
		return fgmpgfListFichaInscripcionDto;
	}

	public void setFgmpgfListFichaInscripcionDto(List<FichaInscripcionDto> fgmpgfListFichaInscripcionDto) {
		this.fgmpgfListFichaInscripcionDto = fgmpgfListFichaInscripcionDto;
	}

	public List<CarreraDto> getFgmpgfListCarreraDto() {
		fgmpgfListCarreraDto = fgmpgfListCarreraDto==null?(new ArrayList<CarreraDto>()):fgmpgfListCarreraDto;
		return fgmpgfListCarreraDto;
	}

	public void setFgmpgfListCarreraDto(List<CarreraDto> fgmpgfListCarreraDto) {
		this.fgmpgfListCarreraDto = fgmpgfListCarreraDto;
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
	 
	public List<MateriaDto> getFgmpgfListMateriaDto() {
		fgmpgfListMateriaDto = fgmpgfListMateriaDto==null?(new ArrayList<MateriaDto>()):fgmpgfListMateriaDto;
		return fgmpgfListMateriaDto;
	}

	public void setFgmpgfListMateriaDto(List<MateriaDto> fgmpgfListMateriaDto) {
		this.fgmpgfListMateriaDto = fgmpgfListMateriaDto;
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

	public FichaInscripcionDto getFgmpgfFichaInscripcionDto() {
		return fgmpgfFichaInscripcionDto;
	}

	public void setFgmpgfFichaInscripcionDto(FichaInscripcionDto fgmpgfFichaInscripcionDto) {
		this.fgmpgfFichaInscripcionDto = fgmpgfFichaInscripcionDto;
	}

	public PlanificacionCronograma getFgmpgfPlanificacionCronograma() {
		return fgmpgfPlanificacionCronograma;
	}

	public void setFgmpgfPlanificacionCronograma(PlanificacionCronograma fgmpgfPlanificacionCronograma) {
		this.fgmpgfPlanificacionCronograma = fgmpgfPlanificacionCronograma;
	}

	public CronogramaActividadJdbcDto getFgmpgfCronogramaActividadMatriculaOrdinaria() {
		return fgmpgfCronogramaActividadMatriculaOrdinaria;
	}

	public void setFgmpgfCronogramaActividadMatriculaOrdinaria(
			CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaOrdinaria) {
		this.fgmpgfCronogramaActividadMatriculaOrdinaria = fgmpgfCronogramaActividadMatriculaOrdinaria;
	}

	public CronogramaActividadJdbcDto getFgmpgfCronogramaActividadMatriculaEstraordinaria() {
		return fgmpgfCronogramaActividadMatriculaExtraordinaria;
	}

	public void setFgmpgfCronogramaActividadMatriculaEstraordinaria(
			CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaEstraordinaria) {
		this.fgmpgfCronogramaActividadMatriculaExtraordinaria = fgmpgfCronogramaActividadMatriculaEstraordinaria;
	}

	public CronogramaActividadJdbcDto getFgmpgfCronogramaActividadMatriculaEspecial() {
		return fgmpgfCronogramaActividadMatriculaEspecial;
	}

	public void setFgmpgfCronogramaActividadMatriculaEspecial(
			CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaEspecial) {
		this.fgmpgfCronogramaActividadMatriculaEspecial = fgmpgfCronogramaActividadMatriculaEspecial;
	}

	public CronogramaActividadJdbcDto getFgmpgfCronogramaActividadMatriculaExtraordinaria() {
		return fgmpgfCronogramaActividadMatriculaExtraordinaria;
	}

	public void setFgmpgfCronogramaActividadMatriculaExtraordinaria(
			CronogramaActividadJdbcDto fgmpgfCronogramaActividadMatriculaExtraordinaria) {
		this.fgmpgfCronogramaActividadMatriculaExtraordinaria = fgmpgfCronogramaActividadMatriculaExtraordinaria;
	}

	public List<CarreraDto> getFgmpgfListMateriasCarrera() {
		return fgmpgfListMateriasCarrera;
	}

	public void setFgmpgfListMateriasCarrera(List<CarreraDto> fgmpgfListMateriasCarrera) {
		this.fgmpgfListMateriasCarrera = fgmpgfListMateriasCarrera;
	}

	public List<MateriaDto> getFgmpgfListMateriasEstado() {
		return fgmpgfListMateriasEstado;
	}

	public void setFgmpgfListMateriasEstado(List<MateriaDto> fgmpgfListMateriasEstado) {
		this.fgmpgfListMateriasEstado = fgmpgfListMateriasEstado;
	}

	public Dependencia getFgmpgfDependenciaBuscar() {
		return fgmpgfDependenciaBuscar;
	}

	public void setFgmpgfDependenciaBuscar(Dependencia fgmpgfDependenciaBuscar) {
		this.fgmpgfDependenciaBuscar = fgmpgfDependenciaBuscar;
	}

	public CronogramaActividadJdbcDto getFgmpgfFechasMatriculaOrdinaria() {
		return fgmpgfFechasMatriculaOrdinaria;
	}

	public void setFgmpgfFechasMatriculaOrdinaria(CronogramaActividadJdbcDto fgmpgfFechasMatriculaOrdinaria) {
		this.fgmpgfFechasMatriculaOrdinaria = fgmpgfFechasMatriculaOrdinaria;
	}

	public CronogramaActividadJdbcDto getFgmpgfFechasMatriculaExtraordinaria() {
		return fgmpgfFechasMatriculaExtraordinaria;
	}

	public void setFgmpgfFechasMatriculaExtraordinaria(CronogramaActividadJdbcDto fgmpgfFechasMatriculaExtraordinaria) {
		this.fgmpgfFechasMatriculaExtraordinaria = fgmpgfFechasMatriculaExtraordinaria;
	}

	public CronogramaActividadJdbcDto getFgmpgfFechasMatriculaEspecial() {
		return fgmpgfFechasMatriculaEspecial;
	}

	public void setFgmpgfFechasMatriculaEspecial(CronogramaActividadJdbcDto fgmpgfFechasMatriculaEspecial) {
		this.fgmpgfFechasMatriculaEspecial = fgmpgfFechasMatriculaEspecial;
	}

	public FichaInscripcionDto getFgmpgfBuscarFichaInscripcionDto() {
		return fgmpgfBuscarFichaInscripcionDto;
	}

	public void setFgmpgfBuscarFichaInscripcionDto(FichaInscripcionDto fgmpgfBuscarFichaInscripcionDto) {
		this.fgmpgfBuscarFichaInscripcionDto = fgmpgfBuscarFichaInscripcionDto;
	}

	public Integer getFgmpgfCarrerId() {
		return fgmpgfCarrerId;
	}

	public void setFgmpgfCarrerId(Integer fgmpgfCarrerId) {
		this.fgmpgfCarrerId = fgmpgfCarrerId;
	}

	public List<MateriaDto> getFgmpgfListMateriaDtoTruncadaNivel() {
		fgmpgfListMateriaDtoTruncadaNivel = fgmpgfListMateriaDtoTruncadaNivel==null?(new ArrayList<MateriaDto>()):fgmpgfListMateriaDtoTruncadaNivel;
		return fgmpgfListMateriaDtoTruncadaNivel;
	}

	public void setFgmpgfListMateriaDtoTruncadaNivel(List<MateriaDto> fgmpgfListMateriaDtoTruncadaNivel) {
		this.fgmpgfListMateriaDtoTruncadaNivel = fgmpgfListMateriaDtoTruncadaNivel;
	}

	public List<MateriaDto> getFgmpgfListMateriaDtoAMatricularse() {
		fgmpgfListMateriaDtoAMatricularse = fgmpgfListMateriaDtoAMatricularse==null?(new ArrayList<MateriaDto>()):fgmpgfListMateriaDtoAMatricularse;
		return fgmpgfListMateriaDtoAMatricularse;
	}

	public void setFgmpgfListMateriaDtoAMatricularse(List<MateriaDto> fgmpgfListMateriaDtoAMatricularse) {
		this.fgmpgfListMateriaDtoAMatricularse = fgmpgfListMateriaDtoAMatricularse;
	}

	public List<MateriaDto> getFgmpgfListMateriaDtoDepuracion() {
		fgmpgfListMateriaDtoDepuracion = fgmpgfListMateriaDtoDepuracion==null?(new ArrayList<MateriaDto>()):fgmpgfListMateriaDtoDepuracion;
		return fgmpgfListMateriaDtoDepuracion;
	}

	public void setFgmpgfListMateriaDtoDepuracion(List<MateriaDto> fgmpgfListMateriaDtoDepuracion) {
		this.fgmpgfListMateriaDtoDepuracion = fgmpgfListMateriaDtoDepuracion;
	}

	public List<HorarioAcademicoDto> getFgmpgfListHorarioAcademico() {
		fgmpgfListHorarioAcademico = fgmpgfListHorarioAcademico==null?(new ArrayList<HorarioAcademicoDto>()):fgmpgfListHorarioAcademico;
		return fgmpgfListHorarioAcademico;
	}

	public void setFgmpgfListHorarioAcademico(List<HorarioAcademicoDto> fgmpgfListHorarioAcademico) {
		this.fgmpgfListHorarioAcademico = fgmpgfListHorarioAcademico;
	}

	public List<HorarioAcademicoDto> getFgmpgfListMateriasHorarioSel() {
		return fgmpgfListMateriasHorarioSel;
	}

	public void setFgmpgfListMateriasHorarioSel(List<HorarioAcademicoDto> fgmpgfListMateriasHorarioSel) {
		this.fgmpgfListMateriasHorarioSel = fgmpgfListMateriasHorarioSel;
	}

	public MallaCurricularParaleloDto getFgmpgfDisponibiliadCupo() {
		return fgmpgfDisponibiliadCupo;
	}

	public void setFgmpgfDisponibiliadCupo(MallaCurricularParaleloDto fgmpgfDisponibiliadCupo) {
		this.fgmpgfDisponibiliadCupo = fgmpgfDisponibiliadCupo;
	}

	public CronogramaActividadJdbcDto getFgmpgfProcesoFlujo() {
		return fgmpgfProcesoFlujo;
	}

	public void setFgmpgfProcesoFlujo(CronogramaActividadJdbcDto fgmpgfProcesoFlujo) {
		this.fgmpgfProcesoFlujo = fgmpgfProcesoFlujo;
	}

	public Integer getFgmpgfRecordEstudianteIDConNivelacion() {
		return fgmpgfRecordEstudianteIDConNivelacion;
	}

	public void setFgmpgfRecordEstudianteIDConNivelacion(Integer fgmpgfRecordEstudianteIDConNivelacion) {
		this.fgmpgfRecordEstudianteIDConNivelacion = fgmpgfRecordEstudianteIDConNivelacion;
	}

	public List<MateriaDto> getFgmpgfListMateria() {
		return fgmpgfListMateria;
	}

	public void setFgmpgfListMateria(List<MateriaDto> fgmpgfListMateria) {
		this.fgmpgfListMateria = fgmpgfListMateria;
	}

	public List<MateriaDto> getFgmpgfListMateriaDtoHomologadas() {
		fgmpgfListMateriaDtoHomologadas = fgmpgfListMateriaDtoHomologadas==null?(new ArrayList<MateriaDto>()):fgmpgfListMateriaDtoHomologadas;
		return fgmpgfListMateriaDtoHomologadas;
	}

	public void setFgmpgfListMateriaDtoHomologadas(List<MateriaDto> fgmpgfListMateriaDtoHomologadas) {
		this.fgmpgfListMateriaDtoHomologadas = fgmpgfListMateriaDtoHomologadas;
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

	public Boolean getDisableBoton() {
		return disableBoton;
	}

	public void setDisableBoton(Boolean disableBoton) {
		this.disableBoton = disableBoton;
	}

	public int getActivarModalConfirmacion() {
		return activarModalConfirmacion;
	}

	public void setActivarModalConfirmacion(int activarModalConfirmacion) {
		this.activarModalConfirmacion = activarModalConfirmacion;
	}

	public String getFgmpgfMensajeDlg() {
		return fgmpgfMensajeDlg;
	}

	public void setFgmpgfMensajeDlg(String fgmpgfMensajeDlg) {
		this.fgmpgfMensajeDlg = fgmpgfMensajeDlg;
	}

	public Boolean getIsRegularMatricula() {
		return isRegularMatricula;
	}

	public void setIsRegularMatricula(Boolean isRegularMatricula) {
		this.isRegularMatricula = isRegularMatricula;
	}

	public Boolean getIsEstudianteReingresoConReinicio() {
		return isEstudianteReingresoConReinicio;
	}

	public void setIsEstudianteReingresoConReinicio(Boolean isEstudianteReingresoConReinicio) {
		this.isEstudianteReingresoConReinicio = isEstudianteReingresoConReinicio;
	}



	public Integer getFgmpgfNivelInferior() {
		return fgmpgfNivelInferior;
	}



	public void setFgmpgfNivelInferior(Integer fgmpgfNivelInferior) {
		this.fgmpgfNivelInferior = fgmpgfNivelInferior;
	}



	public Integer getFgmpgfNivelSuperior() {
		return fgmpgfNivelSuperior;
	}



	public void setFgmpgfNivelSuperior(Integer fgmpgfNivelSuperior) {
		this.fgmpgfNivelSuperior = fgmpgfNivelSuperior;
	}



	public List<MallaCurricularNivelDto> getFgmpgfListMallaCurricularNivelDto() {
		return fgmpgfListMallaCurricularNivelDto;
	}



	public void setFgmpgfListMallaCurricularNivelDto(List<MallaCurricularNivelDto> fgmpgfListMallaCurricularNivelDto) {
		this.fgmpgfListMallaCurricularNivelDto = fgmpgfListMallaCurricularNivelDto;
	}



	public Integer getFgmpgfNivelMaximo() {
		return fgmpgfNivelMaximo;
	}



	public void setFgmpgfNivelMaximo(Integer fgmpgfNivelMaximo) {
		this.fgmpgfNivelMaximo = fgmpgfNivelMaximo;
	}
	
	

	
	
	
}