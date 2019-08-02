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
   
 ARCHIVO:     notasPregradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja las notas de los estudiantes por docente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-JUNIO-2017 			Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPregradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteNotasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.IdentifidorCliente;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (managed bean) notasPregradoForm. Managed Bean que administra los
 * estudiantes para el ingreso de las notas por docentes.
 * 
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name = "notasPregradoForm")
@SessionScoped
public class NotasPregradoForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	// GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario npfUsuario;

	// PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto npfDocente;

	// PARA BUSQUEDA
	private EstudianteJdbcDto npfEstudianteBuscar;
	private List<CarreraDto> npfListCarreraBusq;
	private List<NivelDto> npfListNivelBusq;
	private List<MateriaDto> npfListMateriaBusq;
	private List<EstudianteJdbcDto> npfListEstudianteBusq;
	private List<ParaleloDto> npfListParaleloBusq;
	private List<ParaleloDto> npfListParaleloBusqIndividual;

	private List<DocenteJdbcDto> npfListMateriaBusqAux;

	private PeriodoAcademicoDto npfPeriodoAcademicoBusq;

	private List<RolFlujoCarrera> npfListRolFlujoCarreraBusq;

	private List<DocenteJdbcDto> npffListCarreraDocenteBusq;
	private List<DocenteJdbcDto> npffListNivel;
	private Integer npfCrrId;
	private Carrera npfCarrera;
	private String npfTextoCabecera;
	private String npfTextoOpcion;
	private Integer npfActivadorCohorte;
	private String npfTextoCarrera;
	private ParaleloDto npfParaleloDtoEditar;

	private CronogramaActividadJdbcDto npfCronogramaActividadJdbcDtoBuscar;
	private CronogramaActividadJdbcDto npfCronogramaActividadJdbcDtoBuscarFin;

	private Dependencia npfDependenciaBuscar;

	// PARA GUARDAR LA ASISTENCIA DEL DOCENTE DEL FORM
	private Integer npfAsistenciaDocente;
	private Integer npfAsistenciaDocente2;
	// PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private Integer npfValidadorClic;
	private String npfEstado;
	// PARA GUARDAR LOS REGISTROS DE LA SESION DEL CLIENTE HOSTNAME, IPPIBLICA,
	// IPPRIVADA
	private String npfRegCliente;
	private boolean npfActivadorGuardar;
	private boolean npfActivadorEnLinea;
	private Materia mtrModulo;

	// private String thisIpAddress;

	// campos para el envio de la notificacion del ingreso final de notas al
	// mail del docente
	private String npfNomCarrera;
	private String npfNomMateria;
	private String npfNomParalelo;
	private Carrera npfCarreraIngreso;
	private boolean activadorAsistencia;

	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {

	}

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB
	private CarreraDtoServicioJdbc servNpfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servNpfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servNpfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servNpfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servNpfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servNpfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servNpfParaleloDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servNpfNotasPregradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servNpfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNpfMateriaDto;
	@EJB
	private CalificacionServicio servNpfCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servNpfRolFlujoCarreraServicio;
	@EJB
	private CarreraServicio servNpfCarreraServicio;
	@EJB
	private NivelServicio servNpfNivelServicio;
	@EJB
	private UsuarioRolServicio servNpfUsuarioRolServicio;

	@EJB
	private DependenciaServicio servNpfDependenciaServicio;

	@EJB
	private CronogramaActividadDtoServicioJdbc servNpfCronogramaActividadDtoServicioJdbcServicio;
	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNpfMateriaServicio;
	@EJB
	private MallaCurricularParaleloDtoServicioJdbc servNpfMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	private MallaCurricularParaleloServicio servNpfMallaCurricularParaleloServicio;
	@EJB
	private MallaCurricularMateriaServicio servNpfMallaCurricularMateriaServicio;
	@EJB
	private PeriodoAcademicoServicio servNpfPeriodoAcademicoServicio;
	@EJB
	private RecordEstudianteServicio servNpfRecordEstudianteServicio;
	@EJB
	private FichaInscripcionServicio servNpfFichaInscripcionServicio;

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/
	// PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irAnotaPregrado(Usuario usuario) {
		npfActivadorEnLinea = false;
		npfUsuario = usuario;
		String retorno = null;
		try {
			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(npfUsuario.getUsrId(),
					RolConstantes.ROL_DOCENTE_VALUE);
			if (usro.getUsroEstado().intValue() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}

			// INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			npfListParaleloBusq = null;

			// busco el periodo academico
			try {
				npfPeriodoAcademicoBusq = servNpfPeriodoAcademicoDtoServicioJdbc
						.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			} catch (Exception e) {
				npfPeriodoAcademicoBusq = servNpfPeriodoAcademicoDtoServicioJdbc
						.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			}
			npfDocente = servDocenteDtoServicioJdbc.buscarDatosDocenteXUsuario(npfUsuario.getUsrId());
			npffListCarreraDocenteBusq = new ArrayList<>();
			npffListCarreraDocenteBusq = servNpfDocenteDtoServicioJdbc
					.buscarCarrerasDocenteXPracEstadoActivoCierre(npfUsuario.getUsrId());

			try {
				List<DocenteJdbcDto> npffListCarreraDocenteBusq1 = new ArrayList<>();
				npffListCarreraDocenteBusq1 = servDocenteDtoServicioJdbc.buscarProgramasDocente(npfUsuario.getUsrId());
				npffListCarreraDocenteBusq.addAll(npffListCarreraDocenteBusq1);

			} catch (Exception e) {
			}
			// Solo para usar el período 350 para áreas en línea
			try {
				npffListCarreraDocenteBusq.addAll(
						servNpfDocenteDtoServicioJdbc.buscarCarrerasEnLineaDocente2018_2019(npfUsuario.getUsrId()));
				npfActivadorEnLinea = true;
			} catch (Exception e1) {
			}

			Collections.sort(npffListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
				public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
				}
			});
			retorno = "irAnotaPregrado";
		} catch (Exception e) {
			try {
				List<DocenteJdbcDto> npffListCarreraDocenteBusq1 = new ArrayList<>();
				npffListCarreraDocenteBusq1 = servDocenteDtoServicioJdbc.buscarProgramasDocente(npfUsuario.getUsrId());
				npffListCarreraDocenteBusq.addAll(npffListCarreraDocenteBusq1);

			} catch (Exception e1) {
			}

			// Solo para usar el período 350 para áreas en línea
			try {
				npffListCarreraDocenteBusq.addAll(
						servNpfDocenteDtoServicioJdbc.buscarCarrerasEnLineaDocente2018_2019(npfUsuario.getUsrId()));
				npfActivadorEnLinea = true;
			} catch (Exception e1) {
			}
			retorno = "irAnotaPregrado";
			if (npffListCarreraDocenteBusq.size() == 0) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontraron carreras asignadas al docente.");
				return null;
			}

		}
		if (npffListCarreraDocenteBusq.size() == 0) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron carreras asignadas al docente.");
			return null;
		}
		return retorno;
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		iniciarParametros();
		return "irInicio";
	}

	/**
	 * Regresa a la página de listar paralelos
	 * 
	 * @return
	 */
	public String irListarIndividual() {
		return "irListarParalelos";
	}

	/**
	 * Regresa a la página de lista paralelo seleccionado
	 * 
	 * @return
	 */
	public String irListarParaleloSeleccionado() {
		return "irListarParaleloSeleccionado";
	}

	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * 
	 * @param idCarrera
	 *            - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera) {
		idCarrera = npfEstudianteBuscar.getCrrId();
		npfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		npfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		npfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		npffListNivel = null;
		npfListMateriaBusq = null;
		npfListParaleloBusq = null;
		npfListMateriaBusqAux = null;
		npfListParaleloBusqIndividual = null;
		npfListParaleloBusq = null;

		if (idCarrera != GeneralesConstantes.APP_ID_BASE) {

			if (idCarrera == CarreraConstantes.CARRERA_AREA_1L_EDUCACION_EN_LINEA_VALUE
					|| idCarrera == CarreraConstantes.CARRERA_AREA_8L_CIENCIAS_SOCIALES_Y_DERECHO_EN_LINEA_VALUE) {
				npfActivadorEnLinea = true;
				try {
					npffListNivel = servNpfDocenteDtoServicioJdbc.buscarNivelesDocenteEnLineaX350(npfUsuario.getUsrId(),
							npfEstudianteBuscar.getCrrId());
					Collections.sort(npffListNivel, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
							return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
						}
					});
					npfTextoCarrera = "Carrera";
					npfTextoOpcion = "1er Hemisemestre";
					npfActivadorCohorte = 1;
					npfTextoCabecera = CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_LABEL;
					npfCarrera = servNpfCarreraServicio.buscarPorId(npfEstudianteBuscar.getCrrId());
				} catch (Exception e) {
				}
			} else {
				// //LISTO LOS NIVELES
				try {
					npffListNivel = servNpfDocenteDtoServicioJdbc.buscarNivelesDocenteXCarrera(npfUsuario.getUsrId(),
							npfEstudianteBuscar.getCrrId());
					Collections.sort(npffListNivel, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
							return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
						}
					});
				} catch (NivelDtoJdbcNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil
							.mensajeError("No se encontraron niveles asignados al docente en la carrera seleccionada.");
				}

				try {
					npfCarrera = servNpfCarreraServicio.buscarPorId(npfEstudianteBuscar.getCrrId());
					if (npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {
						npfTextoOpcion = "Registrar";
						npfActivadorCohorte = 0;
						npfTextoCarrera = "Programa";
						if (npfCarrera.getCrrTipoEvaluacion() == CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_10_VALUE) {
							npfTextoCabecera = CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_10_LABEL;
						} else {
							npfTextoCabecera = CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_LABEL;
						}
					} else {
						npfTextoCarrera = "Carrera";
						npfTextoOpcion = "1er Hemisemestre";
						npfActivadorCohorte = 1;
						npfTextoCabecera = CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_LABEL;
					}

				} catch (Exception e) {
				}
			}

		} else {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
					new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
		}

	}

	/**
	 * Método que permite buscar la lista de materias por el por el id de
	 * paralelo
	 * 
	 * @param idParalelo
	 *            - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idNivel) {
		activadorAsistencia = false;
		idNivel = npfEstudianteBuscar.getNvlId();
		npfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		npfListMateriaBusq = null;
		npfListMateriaBusqAux = null;
		npfCrrId = npfEstudianteBuscar.getCrrId();
		npfListParaleloBusqIndividual = null;
		npfListParaleloBusq = null;
		try {
			if (idNivel != GeneralesConstantes.APP_ID_BASE) {
				if (npfActivadorEnLinea) {
					npfListMateriaBusqAux = servNpfDocenteDtoServicioJdbc
							.buscarMateriasDocenteXCarreraXNivelEnLineaX350(npfUsuario.getUsrId(),
									npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId());

					Collections.sort(npfListMateriaBusqAux, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
						}
					});

				} else {
					npfListMateriaBusqAux = servNpfDocenteDtoServicioJdbc.buscarMateriasDocenteXCarreraXNivel(
							npfUsuario.getUsrId(), npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId());

					Collections.sort(npfListMateriaBusqAux, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
						}
					});

				}
			} else {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
						"Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron materias asignadas en el nivel seleccionado.");
		}
	}

	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public String buscar() {
		// anulo la lista de estudiantes
		npfListParaleloBusq = null;
		try {
			if (npfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			} else if (npfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			} else if (npfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			} else {
				List<ParaleloDto> listNoCompartida = new ArrayList<>();
				npfListParaleloBusq = new ArrayList<>();
				listNoCompartida = servNpfParaleloDtoServicioJdbc.listarParalelosXcarreraXnivelXdocenteXmateriaNoComp(
						npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId(), npfUsuario.getUsrId(),
						npfEstudianteBuscar.getMtrId());
				List<ParaleloDto> listCompartida = new ArrayList<>();
				try {
					listCompartida = servNpfParaleloDtoServicioJdbc
							.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaComp(
									npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfUsuario.getUsrId(), npfEstudianteBuscar.getMtrId());
					if (listCompartida.size() != 0) {

						for (ParaleloDto item : listCompartida) {
							npfListParaleloBusq.add(item);
						}
					}
				} catch (Exception e) {
				}
				// asignación a una sola lista
				for (ParaleloDto item : listNoCompartida) {
					npfListParaleloBusq.add(item);
				}
			}

			if (npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {
				try {
					List<ParaleloDto> listNoCompartida = new ArrayList<>();
					listNoCompartida = servNpfParaleloDtoServicioJdbc
							.listarParalelosXProgramaXnivelXdocenteXmateriaNoComp(npfEstudianteBuscar.getCrrId(),
									npfEstudianteBuscar.getNvlId(), npfUsuario.getUsrId(),
									npfEstudianteBuscar.getMtrId());
					npfListParaleloBusq.addAll(listNoCompartida);
				} catch (Exception e) {
				}
			}
			if (npfActivadorEnLinea) {
				try {
					List<ParaleloDto> listNoCompartida = new ArrayList<>();
					listNoCompartida = servNpfParaleloDtoServicioJdbc
							.listarParalelosXProgramaXnivelXdocenteXmateriaNoCompEnLineaX350(
									npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfUsuario.getUsrId(), npfEstudianteBuscar.getMtrId());
					npfListParaleloBusq.addAll(listNoCompartida);
				} catch (Exception e) {
				}
			}
			Iterator<ParaleloDto> it = npfListParaleloBusq.iterator();
			while (it.hasNext()) {
				ParaleloDto item = it.next();
				int contador = 0;
				Iterator<ParaleloDto> it2 = npfListParaleloBusq.iterator();
				while (it2.hasNext()) {
					ParaleloDto item2 = it2.next();
					if (item.getPrlDescripcion().equals(item2.getPrlDescripcion())) {
						contador++;
					}
				}
				if (contador > 1) {
					it.remove();
				}
			}
			Collections.sort(npfListParaleloBusq, new Comparator<ParaleloDto>() {
				public int compare(ParaleloDto obj1, ParaleloDto obj2) {
					return new Integer(obj1.getPrlId()).compareTo(new Integer(obj2.getPrlId()));
				}
			});
			npfCarreraIngreso = servNpfCarreraServicio.buscarPorId(npfEstudianteBuscar.getCrrId());
		} catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return null;
	}

	/**
	 * Metodo que llama los datos para la genracion del reporte
	 */
	public void reporteNotas() {
	}

	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar() {
		iniciarParametros();
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas(ParaleloDto prl) {
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = null;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			if (npfCarreraIngreso.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {
				npfTextoCarrera = "Programa";
				npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
						.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
								npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
								npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
								GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					try {
						item.setClfNota1String(item.getClfNota1().toString());
						npfAsistenciaDocente = item.getClfAsistenciaDocente1();
					} catch (Exception e) {
					}
				}
				npfEstudianteBuscar.setMtrId(npfParaleloDtoEditar.getMtrId());
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					if ((item.getClfAsistenciaDocente1() != null)) {
						npfAsistenciaDocente = item.getClfAsistenciaDocente1();
						break;
					} else {
						npfAsistenciaDocente = null;
					}

				}
				npfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(npfListEstudianteBusq);
				
				if(npfListEstudianteBusq.size()==0){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No existen estudiantes para registrar notas de posgrado.");
				}
			} else {

				npfDependenciaBuscar = new Dependencia();
				npfDependenciaBuscar = servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
				npfDependenciaBuscar = new Dependencia();
				npfDependenciaBuscar = servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
				if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
					npfTextoCarrera = "Área";
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
				} else if (npfCrrId == CarreraConstantes.CARRERA_MEDICINA_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
					npfTextoCarrera = "Carrera";
				} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE,
									ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);
					npfTextoCarrera = "Suficiencia";
				} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					npfTextoCarrera = "Suficiencia";
				} else if(npfCrrId == CarreraConstantes.CARRERA_CIENCIAS_POLICIALES_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasPeriodoActivoPorProceso(CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_CIENCIAS_POLICIALES_VALUE);

					npfTextoCarrera = "Carrera";
				}else {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasPeriodoActivoPorProceso(CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);

					npfTextoCarrera = "Carrera";
				}
				// }
				if (npfUsuario.getUsrNick().equals("fesempertegui")) {

				} else {

					if ((npfActivadorEnLinea) && npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE) {
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						Date date = dateFormat.parse("02/08/2019");
						long time = date.getTime();
						new Timestamp(time);

						Timestamp myDate = new Timestamp(new Date().getTime());
						if (myDate.after(new Timestamp(time))) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas");
							return null;
						}

					} else {
						Timestamp myDate = new Timestamp(new Date().getTime());
						if (myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas");
							return null;
						}
						if (myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está antes de las fechas establecidas");
							return null;
						}
					}

				}

			}

		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Cronograma no habilitado");
		} catch (Exception e) {
		}
		return "irIngresarNotas";

	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas2(ParaleloDto prl) {
		if (npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Opción no habilitada para el Centro de Idiomas.");
			return null;
		}
		npfTextoCarrera = "Carrera";
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = null;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar = servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
			PeriodoAcademico pracCierre = null;
			try {
				pracCierre = servNpfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
			} catch (Exception e) {
			}
			if (pracCierre != null) {
				if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				} else if (npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
						|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
				} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				} else if(npfCrrId == CarreraConstantes.CARRERA_CIENCIAS_POLICIALES_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_VALUE);

					npfTextoCarrera = "Carrera";
				}else {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				}
			} else {
				if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				} else if (npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
						|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
				} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				} else if (npfCrrId == CarreraConstantes.CARRERA_CIENCIAS_POLICIALES_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_VALUE);
				}else {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				}

			}
			if (npfUsuario.getUsrNick().equals("fesempertegui")) {

			} else if ((npfActivadorEnLinea) && npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date = dateFormat.parse("02/08/2019");
				long time = date.getTime();
				new Timestamp(time);
				Timestamp myDate = new Timestamp(new Date().getTime());
				if (myDate.after(new Timestamp(time))) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está fuera de las fechas establecidas");
					return null;
				}
			} else {
				Timestamp myDate = new Timestamp(new Date().getTime());
				Date maxDate = new Date(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin().getTime());
				if (myDate.after(maxDate)) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está fuera de las fechas establecidas");
					return null;
				}
				if (myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está antes de las fechas establecidas");
					return null;
				}
			}
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS 2");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");

			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");
				} catch (Exception e) {
				}
			}
			npfRegCliente = sb.toString();
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}

							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}
					} catch (Exception ex) {
					}
				}
			} else {
				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					if (npfActivadorEnLinea) {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteEnLinea(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
						boolean op = true;
						for (EstudianteJdbcDto item : npfListEstudianteBusq) {
							if (item.getClfNota1() != null) {
								op = false;
							}
						}
						if (op) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(
									"No existen estudiantes con notas del primer hemisemestre, por favor regístrelas antes de continuar.");
							return null;
						}
					} else {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					}

				} else {// Compartida o dependiente de otra
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
				}
			}

			npfEstudianteBuscar.setMtrId(npfParaleloDtoEditar.getMtrId());
			// System.out.println(npfParaleloDtoEditar.getMtrId());
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				try {
					item.setClfNota1String(item.getClfNota2().toString());
					npfAsistenciaDocente = item.getClfAsistenciaDocente2();
				} catch (Exception e) {
				}
			}
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if ((item.getClfAsistenciaDocente2() != null)) {
					npfAsistenciaDocente = item.getClfAsistenciaDocente2();
					break;
				} else {
					npfAsistenciaDocente = null;
				}

			}
			npfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(npfListEstudianteBusq);

		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			// } catch (DependenciaException e) {
			// FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError("Cronograma no habilitado");
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		} catch (Exception e) {
		}
		return "irIngresarNotas2";
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotasIndividual(ParaleloDto prl) {
		npfParaleloDtoEditar = null;
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = null;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		// npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		// npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			if (npfCarreraIngreso.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {
				npfTextoCarrera = "Programa";
				npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
						.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
								npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
								npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
								GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());

				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					try {
						item.setClfNota1String(item.getClfNota1().toString());
						npfAsistenciaDocente = item.getClfAsistenciaDocente1();
					} catch (Exception e) {
						try {
							item.setClfNota1String(item.getClfNota2().toString());
							npfAsistenciaDocente = item.getClfAsistenciaDocente2();
						} catch (Exception e2) {
							npfAsistenciaDocente = null;
						}
					}
				}
				npfEstudianteBuscar.setMtrId(npfParaleloDtoEditar.getMtrId());
				// for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				// if ((item.getClfAsistenciaDocente1() != null)) {
				// npfAsistenciaDocente = item.getClfAsistenciaDocente1();
				// break;
				// } else {
				// npfAsistenciaDocente = null;
				// }
				//
				// }
				npfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(npfListEstudianteBusq);

				List<String> datosCliente = new ArrayList<>();
				datosCliente = IdentifidorCliente.obtenerDatosCliente();
				Date fechaActual = new Date();
				npfRegCliente = "Fecha de registro: "
						+ fechaActual.toString().concat("|ACCION: REGISTRO NOTAS DOCENTE");
				StringBuilder sb = new StringBuilder();
				sb.append(npfRegCliente);
				sb.append("|");

				for (String item : datosCliente) {
					try {
						sb.append(item);
						sb.append("|");
					} catch (Exception e) {
					}
				}
				npfRegCliente = sb.toString();

			} else {

				npfDependenciaBuscar = new Dependencia();
				npfDependenciaBuscar = servNpfDependenciaServicio.buscarDependenciaXcrrId(npfCrrId);
				if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					npfTextoCarrera = "Área";
				} else if (npfCrrId == CarreraConstantes.CARRERA_MEDICINA_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
					npfTextoCarrera = "Carrera";
				} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
					npfTextoCarrera = "Suficiencia";
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE,
									ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);
				} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					npfTextoCarrera = "Suficiencia";
				} else if(npfCrrId == CarreraConstantes.CARRERA_CIENCIAS_POLICIALES_VALUE){
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasPeriodoActivoPorProceso(CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_CIENCIAS_POLICIALES_VALUE);
					npfTextoCarrera = "Carrera";
				}else {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasPeriodoActivoPorProceso(CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
					npfTextoCarrera = "Carrera";
				}
				// }
				if (npfUsuario.getUsrNick().equals("fesempertegui")) {

				} else if (npfCarrera.getCrrTipo() != CarreraConstantes.TIPO_POSGRADO_VALUE) {

					if ((npfActivadorEnLinea) && npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE) {
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						Date date = dateFormat.parse("02/08/2019");
						long time = date.getTime();
						new Timestamp(time);
						Timestamp myDate = new Timestamp(new Date().getTime());
						if (myDate.after(new Timestamp(time))) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas");
							return null;
						}

					} else {

						Timestamp myDate = new Timestamp(new Date().getTime());

						if (myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
							return null;
						}

						if (myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
							return null;
						}
					}
				} else {
					npfTextoCarrera = "Programa";
				}
				Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
				if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;
					List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();
					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());
					npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota1(null);
							item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante1(null);
							item.setClfNota1(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota1(null);
								item.setClfAsistenciaEstudiante1(null);
								item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} else {
					if (npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {

						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXmlcrprIdPosgrado(npfParaleloDtoEditar.getMlcrprId());
						Iterator<EstudianteJdbcDto> itr = npfListEstudianteBusq.iterator();
						while (itr.hasNext()) {
							EstudianteJdbcDto element = itr.next();
							if (element.getClfId() != 0) {
								npfAsistenciaDocente = element.getClfAsistenciaDocente1();
								activadorAsistencia = true;
								itr.remove();
							}
						}

					} else {
						// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE
						// ESTUDIANTES
						// SI COMPARTEN O NO COMPARTEN
						if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																					// comparte
																					// o
																					// no
																					// tiene
																					// compartidas
																					// con
																					// nadie
							if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
								npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
										.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCulturaFisica(
												npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
												npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
												npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
							} else {
								if (npfActivadorEnLinea) {
									npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
											.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteEnLinea(
													npfParaleloDtoEditar.getCrrId(),
													npfParaleloDtoEditar.getMlcrmtNvlId(),
													npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
													npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
								} else {

									npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
											.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
													npfParaleloDtoEditar.getCrrId(),
													npfParaleloDtoEditar.getMlcrmtNvlId(),
													npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
													npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
								}
							}

						} else {// Compartida o dependiente de otra
							if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
								npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
										.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaCulturaFisica(
												npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
												npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
												GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
							} else {
								npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
										.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
												npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
												npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
												GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
							}

						}
					}
				}
				npfEstudianteBuscar.setMtrId(npfParaleloDtoEditar.getMtrId());
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					if (item.getClfAsistenciaDocente1() == GeneralesConstantes.APP_ID_BASE) {
						if (activadorAsistencia) {

						} else {
							npfAsistenciaDocente = null;
						}

					} else {
						if (activadorAsistencia) {

						} else {
							npfAsistenciaDocente = item.getClfAsistenciaDocente1();
						}
						break;
					}

				}
			}

			npfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(npfListEstudianteBusq);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Cronograma no habilitado");
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		} catch (MallaCurricularParaleloNoEncontradoException e) {
		} catch (MallaCurricularParaleloException e) {
		} catch (Exception e) {
		}
		if (npfListEstudianteBusq.size() != 0) {
			return "irIngresarNotas";
		} else {
			if (npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen estudiante pendientes de registrar notas de posgrado.");
			}
			return null;
		}

	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotasIndividual2(ParaleloDto prl) {
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar = servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
			PeriodoAcademico pracCierre = null;
			try {
				pracCierre = servNpfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
			} catch (Exception e) {
			}

			if (pracCierre != null) {
				if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				} else if (npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
						|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {

					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				} else if (npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_CIENCIAS_POLICIALES_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}else {

					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
				}
			} else {
				if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_NIVELACION_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				} else if (npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
						|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
				} else if (npfParaleloDtoEditar
						.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasSuficienciaCulturaFisica(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				} else {
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
					npfCronogramaActividadJdbcDtoBuscarFin = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
									CronogramaConstantes.TIPO_ACADEMICO_VALUE,
									ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
				}

			}

			if (npfUsuario.getUsrNick().equals("fesempertegui")) {

			} else if ((npfActivadorEnLinea) && npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date;
				try {
					date = dateFormat.parse("02/08/2019");
					long time = date.getTime();
					new Timestamp(time);
					Timestamp myDate = new Timestamp(new Date().getTime());
					if (myDate.after(new Timestamp(time))) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas");
						return null;
					}
				} catch (ParseException e) {
				}

			}

			else {
				Timestamp myDate = new Timestamp(new Date().getTime());

				if (pracCierre != null) {
					Date siguientePracDate = new Date(
							npfCronogramaActividadJdbcDtoBuscarFin.getPlcrFechaInicio().getTime());
					Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
					if (siguientePracDate.after(maxDate)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas");
						return null;
					}
				} else {
					Date maxDate = new Date(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin().getTime());
					if (myDate.after(maxDate)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas");
						return null;
					}
				}
			}
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS 2");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");

			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");
				} catch (Exception e) {
				}
			}
			npfRegCliente = sb.toString();
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {

				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			} else {
				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					if (npfActivadorEnLinea) {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteEnLinea(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					} else {
						npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
										npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					}
				} else {// Compartida o dependiente de otra
					npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
				}
			}

			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if (item.getClfAsistenciaDocente2() != null) {
					npfAsistenciaDocente2 = item.getClfAsistenciaDocente2();
					break;
				} else {
					npfAsistenciaDocente2 = null;
				}
			}
			npfEstudianteBuscar.setMtrId(npfParaleloDtoEditar.getMtrId());

			if (npfListEstudianteBusq.isEmpty()) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(
						"No existen estudiantes en el paralelo para rectificar notas de 2do Hemisemestre.");
				return null;
			} else {
				npfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(npfListEstudianteBusq);
			}
			return "irIngresarNotas2";
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			// } catch (DependenciaException e) {
			// FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Cronograma no habilitado");
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		}
		return null;
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRecuperacionNotas(ParaleloDto prl) {
		npfListParaleloBusqIndividual = new ArrayList<>();
		npfListParaleloBusqIndividual.add(prl);

		if (npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
				|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
			try {
				npfPeriodoAcademicoBusq = servNpfPeriodoAcademicoDtoServicioJdbc
						.buscarXEstadoIdiomas(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				if (npfPeriodoAcademicoBusq.getPracId() == 490) {
					Timestamp myDate = new Timestamp(new Date().getTime());
					DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					// Date fechaInicio = format.parse("01/03/2019");
					// Date fechaFin = format.parse("08/03/2019");
					npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
							.buscarRangoFechasxPracIdXtipoFlujo(490,
									ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);

					if (myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas");
						return null;
					}
					if (myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está antes de las fechas establecidas");
						return null;
					}
				} else {

					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Opción no habilitada para suficiencia de idiomas.");
					return null;
				}
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Opción no habilitada para suficiencia de idiomas.");
				return null;
			}

		}
		npfActivadorGuardar = false;
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar = servNpfDependenciaServicio.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());

			if (npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {

			} else {
				PeriodoAcademico pracCierre = null;
				;
				try {
					pracCierre = servNpfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
				} catch (Exception e) {
				}

				if (pracCierre != null) {
					if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_NIVELACION_VALUE,
										ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					} else if (npfCrrId == CarreraConstantes.CARRERA_MEDICINA_VALUE
							|| npfCrrId == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE

					) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_ACADEMICO_VALUE,
										ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
					} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
										ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
					} else {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_ACADEMICO_VALUE,
										ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					}
				} else {
					if (npfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_NIVELACION_VALUE,
										ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					} else if (npfCrrId == CarreraConstantes.CARRERA_MEDICINA_VALUE
							|| npfCrrId == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_ACADEMICO_VALUE,
										ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
					} else if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE,
										ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
					} else if (npfCrrId == CarreraConstantes.CARRERA_CIENCIAS_POLICIALES_VALUE) {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_ACADEMICO_VALUE,
										ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_CIENCIAS_POLICIALES_VALUE);
					}else {
						npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
								.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
										CronogramaConstantes.TIPO_ACADEMICO_VALUE,
										ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
					}
				}

				// System.out.println(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin());
				if (npfUsuario.getUsrNick().equals("fesempertegui")) {

				} else if ((npfActivadorEnLinea)
						&& npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE) {
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date date;
					try {
						date = dateFormat.parse("02/08/2019");
						long time = date.getTime();
						new Timestamp(time);
						Timestamp myDate = new Timestamp(new Date().getTime());
						if (myDate.after(new Timestamp(time))) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Está fuera de las fechas establecidas");
							return null;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Timestamp myDate = new Timestamp(new Date().getTime());
					if (myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas");
						return null;
					}
					if (myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está antes de las fechas establecidas");
						return null;
					}
				}
			}

			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS RECUPERACION");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");

			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");
				} catch (Exception e) {
				}
			}
			npfRegCliente = sb.toString();
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				// FacesUtil.limpiarMensaje();
				// FacesUtil.mensajeError("El proceso de recuperación para
				// materias modulares no se encuentra disponible en estos
				// momentos.");
				// return null;
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}

							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}

					} catch (Exception ex) {
					}
				}

			} else {
				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCulturaFisica(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					} else {
						if (npfActivadorEnLinea) {
							npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteEnLinea(
											npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
											npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
											npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
						} else {
							npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
											npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
											npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
											npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
						}
					}
				} else {// Compartida o dependiente de otra
					if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaCulturaFisica(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
					} else {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
					}

				}
			}
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if (item.getClfAsistenciaDocente2() == GeneralesConstantes.APP_ID_BASE) {
					npfAsistenciaDocente2 = null;
				} else {
					npfAsistenciaDocente2 = item.getClfAsistenciaDocente2();
					break;
				}
			}
			if (npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
					|| npfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
				Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
				while (it.hasNext()) {
					EstudianteJdbcDto aux = new EstudianteJdbcDto();
					aux = it.next();
					if (aux.getRcesEstado() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE) {
						// Se mantiene en la lista
					} else {
						if (aux.getRcesIngersoNota3() == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE) {
							// Se mantiene en la lista
						} else {
							// sale de la lista
							it.remove();
						}
					}
				}
			} else {
				Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
				while (it.hasNext()) {
					EstudianteJdbcDto aux = new EstudianteJdbcDto();
					aux = it.next();
					if (aux.getRcesEstado() != RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE) {
						if (aux.getRcesIngersoNota3() != RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE) {
							it.remove();
						}
					} else {
						it.remove();
					}
				}
			}
			return "irRecuperacionPregrado";
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			npfActivadorGuardar = true;
			FacesUtil.mensajeError(e.getMessage());

		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			if ((npfActivadorEnLinea) && npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date;
				try {
					date = dateFormat.parse("02/08/2019");
					long time = date.getTime();
					new Timestamp(time);
					Timestamp myDate = new Timestamp(new Date().getTime());
					if (myDate.after(new Timestamp(time))) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas");
						return null;
					}
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteEnLinea(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
					while (it.hasNext()) {
						EstudianteJdbcDto aux = new EstudianteJdbcDto();
						aux = it.next();
						if (aux.getRcesEstado() != RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE) {
							if (aux.getRcesIngersoNota3() != RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE) {
								it.remove();
							}
						} else {
							it.remove();
						}
					}
					return "irRecuperacionPregrado";
				} catch (Exception e1) {
				}

			} else {
				FacesUtil.mensajeError("Cronograma no habilitado");
			}
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		}
		return null;
	}

	/*
	 * Metodo que permite generar el reporte
	 */
	public void generarReporte(ParaleloDto prl) {

		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
					.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(),
							npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(),
							npfParaleloDtoEditar.getMtrId(), npfDocente.getFcdcId(),
							npfParaleloDtoEditar.getMlcrprId());

		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	@SuppressWarnings("static-access")
	public String irVerNotas(ParaleloDto prl) {
		ReporteNotasForm reporte = new ReporteNotasForm();
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}

							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}

					} catch (Exception ex) {
					}
				}
			} else {

				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
				} else {// Compartida o dependiente de otra
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
				}
			}

			activacion();
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				try {
					Materia mtrAux1 = servNpfMateriaServicio.buscarPorId(mtrModulo.getMtrId());
					item.setMtrDescripcion(item.getMtrDescripcion() + " - " + mtrAux1.getMtrDescripcion());

				} catch (MateriaNoEncontradoException | MateriaException e) {
				} catch (NullPointerException e) {
					item.setMtrDescripcion(item.getMtrDescripcion());
				}
				try {
					Carrera crrAux = servNpfCarreraServicio.buscarPorId(npfParaleloDtoEditar.getCrrId());
					Dependencia fclAux = servNpfDependenciaServicio
							.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
					Nivel nvlAux = servNpfNivelServicio.buscarPorId(npfEstudianteBuscar.getNvlId());
					item.setCrrDescripcion(crrAux.getCrrDescripcion());
					item.setCrrDetalle(crrAux.getCrrDetalle());
					item.setNvlDescripcion(nvlAux.getNvlDescripcion());
					item.setDpnDescripcion(fclAux.getDpnDescripcion());
				} catch (Exception e) {
				}

			}

			reporte.generarReporteNotas(npfListEstudianteBusq, npfUsuario);

			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if (item.getClfNota1() != null) {
					String nota = item.getClfNota1().toString();
					int puntoDecimalUbc = nota.indexOf('.');
					int totalDecimales = nota.length() - puntoDecimalUbc - 1;
					if (puntoDecimalUbc == -1) {
						item.setClfNota1String(nota + ".00");
					} else if (totalDecimales == 1) {
						item.setClfNota1String(nota + "0");
					} else {
						item.setClfNota1String(nota);
					}
				}
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas";
	}

	@SuppressWarnings("static-access")
	public String irVerNotas2(ParaleloDto prl) {
		ReporteNotasForm reporte = new ReporteNotasForm();
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {

				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}

							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}

					} catch (Exception ex) {
					}
				}

			} else {
				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
				} else {// Compartida o dependiente de otra
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
					npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
				}
				////////////////////////////////////////////////////////////////////////////////////////////////////
			}

			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				try {
					Materia mtrAux1 = servNpfMateriaServicio.buscarPorId(npfEstudianteBuscar.getMtrId());
					item.setMtrDescripcion(mtrAux1.getMtrDescripcion());

				} catch (MateriaNoEncontradoException | MateriaException e) {
				} catch (NullPointerException e) {
					item.setMtrDescripcion(item.getMtrDescripcion());
				}
				try {
					Carrera crrAux = servNpfCarreraServicio.buscarPorId(npfParaleloDtoEditar.getCrrId());
					Dependencia fclAux = servNpfDependenciaServicio
							.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
					Nivel nvlAux = servNpfNivelServicio.buscarPorId(npfEstudianteBuscar.getNvlId());
					item.setCrrDescripcion(crrAux.getCrrDescripcion());
					item.setCrrDetalle(crrAux.getCrrDetalle());
					item.setNvlDescripcion(nvlAux.getNvlDescripcion());
					item.setDpnDescripcion(fclAux.getDpnDescripcion());
				} catch (Exception e) {
				}
				item.setRcesIngersoNota2(2);
				item.setRcesIngersoNota(2);
			}
			activacion();
			reporte.generarReporteNotasSegundoHemisemestre(npfListEstudianteBusq, npfUsuario);
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if (item.getClfNota1() != null) {
					String nota = item.getClfNota1().toString();
					int puntoDecimalUbc = nota.indexOf('.');
					int totalDecimales = nota.length() - puntoDecimalUbc - 1;
					if (puntoDecimalUbc == -1) {
						item.setClfNota1String(nota + ".00");
					} else if (totalDecimales == 1) {
						item.setClfNota1String(nota + "0");
					} else {
						item.setClfNota1String(nota);
					}
				}
			}

		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e1) {
		} catch (MateriaException e1) {
		}
		return "irVerNotas2";
	}

	@SuppressWarnings("static-access")
	public String irVerNotasRecuperacion(ParaleloDto prl) {
		ReporteNotasForm reporte = new ReporteNotasForm();
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}

							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}

					} catch (Exception ex) {
					}
				}
			} else {
				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
				} else {// Compartida o dependiente de otra
					npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
									npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
									npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
									GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
				}
			}
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if (item.getClfAsistenciaDocente2() == GeneralesConstantes.APP_ID_BASE) {
					npfAsistenciaDocente2 = null;
				} else {
					npfAsistenciaDocente2 = item.getClfAsistenciaDocente2();
					break;
				}
			}
			Carrera crrAux = servNpfCarreraServicio.buscarPorId(npfCrrId);
			Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
			while (it.hasNext()) {
				EstudianteJdbcDto aux = new EstudianteJdbcDto();
				aux = it.next();
				int comparadorMayor = aux.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
				int comparadorMenor = aux.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
				if (comparadorMayor == -1 && (comparadorMenor == 0 || comparadorMenor == 1)) {
					aux.setMtrDescripcion(mtrAux.getMtrDescripcion());
					aux.setDpnDescripcion(npfDependenciaBuscar.getDpnDescripcion());
					aux.setCrrDescripcion(crrAux.getCrrDescripcion());
				} else {
					it.remove();
				}
			}
			activacion();
			reporte.generarReporteNotasRecuperacion(npfListEstudianteBusq, npfUsuario);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaNoEncontradoException e) {
		} catch (MateriaException e) {
		} catch (Exception e) {
		}
		return "irVerNotasRecuperacion";
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotasRecuperacion(ParaleloDto prl) {
		npfParaleloDtoEditar = new ParaleloDto();
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar = servNpfDependenciaServicio.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
			if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
					|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
					|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
					|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
					|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
				Timestamp myDate = new Timestamp(new Date().getTime());
				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio
						.buscarRangoFechasxPracIdXtipoFlujo(490,
								ProcesoFlujoConstantes.PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE);

				if (myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está fuera de las fechas establecidas");
					return null;
				}
				if (myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Está antes de las fechas establecidas");
					return null;
				}
			}
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: "
					+ fechaActual.toString().concat("|ACCION: REGISTRO RECUPERACION NOTAS DOCENTE");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");

			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");
				} catch (Exception e) {
				}
			}
			npfRegCliente = sb.toString();
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

				try {
					Integer mlcrprAuxId = servNpfMallaCurricularParaleloDtoServicioJdbc
							.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
									npfDocente.getPrsIdentificacion(), npfEstudianteBuscar.getNvlId(),
									npfParaleloDtoEditar.getPrlId());
					MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
					mlcrprAux = servNpfMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
					mtrModulo = mtrAux;

					listaprueba = servNpfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
									npfParaleloDtoEditar.getCrrId(), npfEstudianteBuscar.getNvlId(),
									mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
									npfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
									npfParaleloDtoEditar.getMlcrprId());

					List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto item : listaprueba) {
						item.setMateriaModulo(true);
						// En caso de que no existe el resultado de búsqueda
						if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
							item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
							item.setClfNota2(null);
							item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
							item.setClfAsistenciaEstudiante2(null);
							item.setClfNota2(null);
							npfListEstudianteBusqPrueba.add(item);
						} else {
							// En caso de que sean la misma
							// malla_curricula_paralelo
							if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
								npfListEstudianteBusqPrueba.add(item);
							} else {
								item.setClfId(GeneralesConstantes.APP_ID_BASE);
								item.setClfNota2(null);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
								npfListEstudianteBusqPrueba.add(item);
							}
						}
					}
					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

							if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
									.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
									&& (npfListEstudianteBusqPrueba.get(i).getClfId() == npfListEstudianteBusqPrueba
											.get(j).getClfId())) {
								npfListEstudianteBusqPrueba.remove(j);
								j--;
							}
						}
					}

					for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
						boolean op = true;
						for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
							if (i != j) {
								if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
										.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
									if (npfListEstudianteBusqPrueba.get(i)
											.getClfId() == GeneralesConstantes.APP_ID_BASE) {
										op = false;
									}
								}
							}
						}
						if (op) {
							npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
						}
					}
				} catch (Exception e) {
					try {
						listaprueba = new ArrayList<EstudianteJdbcDto>();
						try {
							// Integer mlcrprAuxId =
							// servNrctfMallaCurricularParaleloDtoServicioJdbc.buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(
							// nrctfParaleloDtoEditar.getMlcrprId());
							// System.out.println(mlcrprAuxId);
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorId(npfParaleloDtoEditar.getMlcrprId());
							MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
							mlcrmtAux = servNpfMallaCurricularMateriaServicio
									.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
							mtrAux = servNpfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
							mtrAux = servNpfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
							mlcrmtAux = servNpfMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(mtrAux.getMtrId(),
									MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
							mlcrprAux = servNpfMallaCurricularParaleloServicio
									.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), npfParaleloDtoEditar.getPrlId());
							mtrModulo = mtrAux;

							listaprueba = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == npfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(npfParaleloDtoEditar.getMlcrprId());
										npfListEstudianteBusqPrueba.add(item);
									}
								}
							}
							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

									if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
											.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
											&& (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == npfListEstudianteBusqPrueba.get(j).getClfId())) {
										npfListEstudianteBusqPrueba.remove(j);
										j--;
									}
								}
							}

							for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
								boolean op = true;
								for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
									if (i != j) {
										if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
												.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
											if (npfListEstudianteBusqPrueba.get(i)
													.getClfId() == GeneralesConstantes.APP_ID_BASE) {
												op = false;
											}
										}
									}
								}
								if (op) {
									npfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception ex) {
						}

					} catch (Exception ex) {
					}
				}

			} else {
				// IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				if (npfParaleloDtoEditar.getHracMlcrprIdComp() == null) {// No
																			// comparte
																			// o
																			// no
																			// tiene
																			// compartidas
																			// con
																			// nadie
					if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCulturaFisica(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
					} else {
						if (npfActivadorEnLinea) {
							npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteEnLinea(
											npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
											npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
											npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
						} else {
							npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(
											npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
											npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
											npfDocente.getFcdcId(), npfParaleloDtoEditar.getMlcrprId());
						}

					}

				} else {// Compartida o dependiente de otra
					if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartidaCulturaFisica(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
					} else {
						npfListEstudianteBusq = servNpfNotasPregradoDtoServicioJdbc
								.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteCompartida(
										npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(),
										npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),
										GeneralesConstantes.APP_ID_BASE, npfParaleloDtoEditar.getMlcrprId());
					}

				}
			}
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if (item.getClfAsistenciaDocente2() == GeneralesConstantes.APP_ID_BASE) {
					npfAsistenciaDocente2 = null;
				} else {
					npfAsistenciaDocente2 = item.getClfAsistenciaDocente2();
					break;
				}
			}
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
				while (it.hasNext()) {
					EstudianteJdbcDto aux = new EstudianteJdbcDto();
					aux = it.next();
					try {
						int comparadorMayor = aux.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
						int comparadorMenor = aux.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
						if (comparadorMayor == -1 && (comparadorMenor == 0 || comparadorMenor == 1)) {

						} else {
							it.remove();
						}
					} catch (Exception e) {
						it.remove();
					}

				}
			} else {
				if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
						|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
					Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
					while (it.hasNext()) {
						EstudianteJdbcDto aux = new EstudianteJdbcDto();
						aux = it.next();
						if (aux.getRcesEstado() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE) {
							// Se mantiene en la lista
						} else {
							if (aux.getRcesIngersoNota3() == RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE) {
								// Se mantiene en la lista
							} else {
								// sale de la lista
								it.remove();
							}
						}
					}

				} else {
					Iterator<EstudianteJdbcDto> it = npfListEstudianteBusq.iterator();
					while (it.hasNext()) {
						EstudianteJdbcDto aux = new EstudianteJdbcDto();
						aux = it.next();
						if (aux.getRcesEstado() == RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE) {
							// Se mantiene en la lista
						} else if (aux.getRcesEstado() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) {
							if (aux.getClfSupletorio() != null) {

							} else {
								it.remove();
							}

						} else {
							try {
								int comparador = aux.getClfSumaP1P2().compareTo(new BigDecimal(
										CalificacionConstantes.NOTA_MINIMA_INGRESAR_RECUPERACION_MATERIA_VALUE));
								if (comparador == 0 || comparador == 1) {
									comparador = aux.getClfSumaP1P2().compareTo(
											new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
									if (comparador == 0 || comparador == 2) {
										// sale de la lista
										it.remove();
									}
								} else {
									// sale de la lista
									it.remove();
								}
							} catch (Exception e) {
								it.remove();
							}

						}
					}

				}
			}

			npfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(npfListEstudianteBusq);

			npfAsistenciaDocente = GeneralesConstantes.APP_ID_BASE;
			return "irIngresarNotasRecuperacion";
		} catch (EstudianteDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
			npfActivadorGuardar = true;
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
			npfActivadorGuardar = true;
		} catch (DependenciaNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError("Cronograma no habilitado");
		} catch (MateriaNoEncontradoException e) {
			e.printStackTrace();
		} catch (MateriaException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		npfActivadorGuardar = true;
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("Error al buscar los estudiantes por favor, intente más tarde...");
		return null;
	}

	/**
	 * Método que valida si el valor de la celda ha cambiado
	 * 
	 * @param event.-
	 *            cambio de foco de la celda selecionada
	 */
	// public void onCellEdit(CellEditEvent event) {
	// Object oldValue = event.getOldValue();
	// Object newValue = event.getNewValue();
	// if(newValue != null && !newValue.equals(oldValue)) {
	// FacesMessage msg = new FacesMessage("Nota cambiada", "Anterior: " +
	// oldValue + ", Nueva:" + newValue);
	// msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	// FacesContext.getCurrentInstance().addMessage(null, msg);
	// npfListEstudianteBusq.get(event.getRowIndex()).getFcesId();
	// npfListEstudianteBusq.get(event.getRowIndex()).getMlcrprId();
	// }
	// }

	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por
	 * carrera nivel y materia
	 * 
	 * @return Xhtml listar
	 */
	public String irCancelar() {
		// npfParaleloDtoEditar = null;
		npfListEstudianteBusq = null;
		npfValidadorClic = new Integer(0);
		npfActivadorGuardar = false;
		// if(opModulo){
		// iniciarParametros();
		// return "irInicio";
		// }
		// if(npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
		// return "irCancelarSuficiencia";
		// }else{
		return "irCancelar";
		// }
	}

	/**
	 * Método calcula el porcentaje de asistencia
	 * 
	 * @param porcentaje
	 *            .- valor del porsentaje del calculo
	 * @param asitenciaEst.-
	 *            valor de la asistencia de estudiante
	 * @param asitenciaDoc.-
	 *            valor de la asistencia del docente @return.- valor del
	 *            porsentaje en bigdecimal con dos decimales sin redondeo
	 */
	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		BigDecimal itemCost = BigDecimal.ZERO;
		itemCost = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje))
				.divide(new BigDecimal(asitenciaDoc), 2, RoundingMode.HALF_UP);
		return itemCost;
	}

	/**
	 * verifica que haga click en el boton guardar temporal la nota
	 */
	public String verificarClicGuardadoTemporal() {
		// si la asistencia docente es distinta de null
		if (npfAsistenciaDocente != null) {
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				item.setClfAsistenciaDocente1(npfAsistenciaDocente);
				if (item.getClfNota1() != null && item.getClfAsistenciaEstudiante1() != null) {
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
					if (!validadorAsistencia(cadena, npfAsistenciaDocente)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " "
								+ item.getPrsNombres() + " la asistencia no es válida");
						npfValidadorClic = 0;
						return null;
					}
					if (!validador(item.getClfNota1())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " "
								+ item.getPrsNombres() + " la nota no es válida");
						npfValidadorClic = 0;
						return null;
					}
				}

				if (item.getClfNota1() == null) {
					if (item.getClfAsistenciaEstudiante1() != null) {
						String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
						if (!validadorAsistencia(cadena, npfAsistenciaDocente)) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("NOMBRE ESTUDIANTE (" + item.getPrsPrimerApellido() + " "
									+ item.getPrsSegundoApellido() + " " + item.getPrsNombres()
									+ ") la asistencia es mayor a las horas clase registradas");
							npfValidadorClic = 0;
							return null;
						}
					}
				}

				if (item.getClfAsistenciaEstudiante1() == null) {
					if (item.getClfNota1() != null) {
						if (!validador(item.getClfNota1())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la nota no es valida");
							npfValidadorClic = 0;
							return null;
						}
					}
				}
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar las horas clase del hemisemestre");
			return null;
		}
		npfValidadorClic = 1;
		return null;
	}

	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal() {
		boolean op = true;
		if (npfAsistenciaDocente != null) {

			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				item.setClfAsistenciaDocente1(npfAsistenciaDocente);
				if (item.getClfNota1() != null && item.getClfAsistenciaEstudiante1() != null) {
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
					if (!validadorAsistencia(cadena, npfAsistenciaDocente)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("NOMBRE ESTUDIANTE (" + item.getPrsPrimerApellido() + " "
								+ item.getPrsSegundoApellido() + " " + item.getPrsNombres()
								+ ") la asistencia es mayor a las horas clase registradas");
						npfValidadorClic = 0;
						op = false;
						break;
					}
					if (!validador(item.getClfNota1())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " "
								+ item.getPrsNombres() + " la nota no es valida");
						npfValidadorClic = 0;
						op = false;
						break;
					}

				}

				if (!(item.getClfNota1() != null) && item.getClfAsistenciaEstudiante1() != null) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " "
							+ item.getPrsNombres() + " la nota no es valida");
					npfValidadorClic = 0;
					op = false;
					break;
				}

				if (!(item.getClfAsistenciaEstudiante1() != null) && item.getClfNota1() != null) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("NOMBRE ESTUDIANTE (" + item.getPrsPrimerApellido() + " "
							+ item.getPrsSegundoApellido() + " " + item.getPrsNombres()
							+ ") la asistencia es mayor a las horas clase registradas");
					npfValidadorClic = 0;
					op = false;
					break;
				}
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar las horas clase del hemisemestre");
			return null;
		}
		if (op) {
			npfValidadorClic = 2;
		} else {
			npfValidadorClic = 0;
		}

		return null;
	}

	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal2() {
		boolean op = true;
		if (npfAsistenciaDocente2 != null) {

			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				item.setClfAsistenciaDocente2(npfAsistenciaDocente2);
				if (item.getClfNota2() != null && item.getClfAsistenciaEstudiante2() != null) {
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
					if (!validadorAsistencia(cadena, npfAsistenciaDocente2)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("NOMBRE ESTUDIANTE (" + item.getPrsPrimerApellido() + " "
								+ item.getPrsSegundoApellido() + " " + item.getPrsNombres()
								+ ") la asistencia es mayor a las horas clase registradas");
						npfValidadorClic = 0;
						op = false;
						break;
					}
					if (!validador(item.getClfNota2())) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " "
								+ item.getPrsNombres() + " la nota no es valida");
						npfValidadorClic = 0;
						op = false;
						break;
					}

				}

				if (!(item.getClfNota2() != null) && item.getClfAsistenciaEstudiante2() != null) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido() + " "
							+ item.getPrsNombres() + " la nota no es valida");
					npfValidadorClic = 0;
					op = false;
					break;
				}

				if (!(item.getClfAsistenciaEstudiante2() != null) && item.getClfNota2() != null) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("NOMBRE ESTUDIANTE (" + item.getPrsPrimerApellido() + " "
							+ item.getPrsSegundoApellido() + " " + item.getPrsNombres()
							+ ") la asistencia es mayor a las horas clase registradas");
					npfValidadorClic = 0;
					op = false;
					break;
				}
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar las horas clase del hemisemestre");
			return null;
		}
		if (op) {
			npfValidadorClic = 2;
		} else {
			npfValidadorClic = 0;
		}

		return null;
	}

	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoRecuperacion() {
		npfValidadorClic = 2;
		return null;
	}

	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista
	 * de paralelos
	 * 
	 * @return XHTML listar paralelos
	 */
	public String guardar() {
		Materia mtrAux = null;
		// boolean op=false;
		try {

			BigDecimal cont1 = BigDecimal.ZERO;
			BigDecimal suma = BigDecimal.ZERO;
			if (npfAsistenciaDocente != null) {
				boolean op = false;
				boolean guardado = false;
				boolean duplicado = false;
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setClfAsistenciaDocente1(npfAsistenciaDocente);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getRcesId());
					if (item.getClfNota1() == null ^ item.getClfAsistenciaEstudiante1() == null) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(
								"EL registro por estudiante debe tener la nota y asistencia del primer hemisemestre");
						npfValidadorClic = 0;
						return null;
					}
					try {
						if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
								|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
								|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
								|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
								|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
							try {
								// calculo la suma de la nota1 + nota2 con
								// redondeo de una cifra decimal
								BigDecimal sumaParciales = BigDecimal.ZERO;
								sumaParciales = item.getClfNota1();
								item.setClfSumaP1P2(sumaParciales);

								// calculo de la suma de asistencia del
								// estudiante de los dos parciales
								int sumaAsistenciaParciales = 0;
								sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1();
								item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));

								// calculo de la suma de la asistencia del
								// docente de los dos parciales
								int sumaAsistenciaDoc = 0;
								sumaAsistenciaDoc = item.getClfAsistenciaDocente1();
								item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));

								// calcula el promedio de la asistencia del
								// estudiante
								item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(
										CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE,
										item.getClfAsistenciaTotal().intValue(),
										item.getClfAsistenciaTotalDoc().intValue()));

								int com = item.getClfSumaP1P2().compareTo(new BigDecimal(14));
								// si la suma de los parciales es mayor o igual
								// a 14
								if (com == 1 || com == 0) {
									int promedioAsistencia = 0;
									promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									// si el promedio de asistencia es mayor o
									// igual a 80
									if (promedioAsistencia == 1 || promedioAsistencia == 0) {
										// calcula la nota final del semestre y
										// el estado a aprobado
										item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
									} else {// si el promedio de asistencia es
											// menor a 80
										item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.DOWN));
										item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								} else {
									item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							} catch (Exception e) {
							}
						}

						if (item.getClfNota1() != null && item.getClfAsistenciaEstudiante1() != null) {
							item.setCrrId(npfCrrId);
							if (npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {

								int sumaAsistenciaParciales = 0;
								sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1();
								item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));

								// calculo de la suma de la asistencia del
								// docente de los dos parciales
								int sumaAsistenciaDoc = 0;
								sumaAsistenciaDoc = item.getClfAsistenciaDocente1();
								item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));

								// calcula el promedio de la asistencia del
								// estudiante
								item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(
										CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE,
										item.getClfAsistenciaTotal().intValue(),
										item.getClfAsistenciaTotalDoc().intValue()));
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
								// si el promedio de asistencia es mayor o
								// igual a 80
								if (promedioAsistencia == 1 || promedioAsistencia == 0) {
									// calcula la nota final del semestre y
									// el estado a aprobado
									item.setClfNotalFinalSemestre(item.getClfNota1().setScale(0, RoundingMode.DOWN));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								} else {// si el promedio de asistencia es
										// menor a 80
									item.setClfPromedioNotas(item.getClfNota1().setScale(0, RoundingMode.DOWN));
									item.setClfNotalFinalSemestre(item.getClfNota1().setScale(0, RoundingMode.DOWN));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
								int comparar = 0;
								if (npfCarrera
										.getCrrTipoEvaluacion() == CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_VALUE) {
									comparar = item.getClfNotalFinalSemestre().compareTo(new BigDecimal(16));
								} else {
									comparar = item.getClfNotalFinalSemestre().compareTo(new BigDecimal(7));
								}
								if (comparar == -1) {
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								} else {
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								}
								item.setCrrTipoEvaluacion(npfCarrera.getCrrTipoEvaluacion());
								guardado = servNpfCalificacionServicio.guardarEdicionNotasPosgrado(rcesAux, item,
										npfRegCliente);
								List<RecordEstudiante> listaRecord = new ArrayList<RecordEstudiante>();
								listaRecord = servNpfRecordEstudianteServicio.buscarEstadoMateriasActualesPosgrado(
										item.getPrsIdentificacion(), item.getNvlId());
								boolean opRecord = true;
								Integer conteoReprobadas = 0;
								for (RecordEstudiante itemRces : listaRecord) {
									if (itemRces
											.getRcesEstado() == RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE) {
										opRecord = false;
									} else if (itemRces
											.getRcesEstado() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE) {
										conteoReprobadas++;
									}
								}
								if (opRecord) {
									if (conteoReprobadas > 2) {
										servNpfFichaInscripcionServicio
												.desactivarFichaInscripcionPosgradoXFcesId(item.getFcesId());
									} else {
										servNpfRecordEstudianteServicio.registrarNuevoNivelPosgrado(item.getFcesId(),
												item.getNvlId(), item.getPracId());
									}
								}

							} else {
								guardado = servNpfCalificacionServicio.guardarEdicionNotasPrimerHemi(rcesAux, item,
										npfRegCliente);
								op = false;
								try {
									if (mtrAux.getMtrTipoMateria()
											.getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
										servNpfCalificacionServicio.verificarModulos(rcesAux, item, npfRegCliente);
									}
								} catch (Exception e) {
								}

								BigDecimal nota1 = item.getClfNota1().setScale(2, RoundingMode.HALF_UP);
								cont1 = cont1.add(new BigDecimal(1));
								suma = suma.add(nota1);
							}

						}

					} catch (SQLIntegrityConstraintViolationException | ConstraintViolationException
							| PersistenceException e) {
						duplicado = true;
					} catch (Exception e) {
						op = true;
					}
					npfValidadorClic = 0;

				}
				if (duplicado) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(
							"Se produjo un error al guardar las notas de la asignatura, por favor revise los listados de notas.");
				} else {
					if (op || !guardado) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(
								"Se produjo un error al guardar las notas de la asignatura, por favor intente más tarde.");
					} else {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Ingreso de notas exitoso");

						// ******************************************************************************
						// ************************* ACA INICIA EL ENVIO DE MAIL
						// CON
						// ADJUNTO ************
						// ******************************************************************************

						try {
							Connection connection = null;
							Session session = null;
							MessageProducer producer = null;
							ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
									"admin", GeneralesConstantes.APP_NIO_ACTIVEMQ);
							connection = connectionFactory.createConnection();
							connection.start();
							session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
							Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
							// Creamos un productor
							producer = session.createProducer(destino);

							JasperReport jasperReport = null;
							JasperPrint jasperPrint;
							StringBuilder pathGeneralReportes = new StringBuilder();
							pathGeneralReportes
									.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
							if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
								pathGeneralReportes
										.append("/academico/reportes/archivosJasper/reporteNotas/notasIdiomas");
								jasperReport = (JasperReport) JRLoader.loadObject(
										new File((pathGeneralReportes.toString() + "/reporteNotasIdiomas.jasper")));
							} else if (npfCarrera.getCrrTipo() == CarreraConstantes.TIPO_POSGRADO_VALUE) {
								pathGeneralReportes.append(
										FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
								pathGeneralReportes
										.append("/academico/reportes/archivosJasper/reporteNotas/notasPosgrado");
								jasperReport = (JasperReport) JRLoader.loadObject(
										new File((pathGeneralReportes.toString() + "/reporteNotaPosgrado.jasper")));
							} else {
								pathGeneralReportes
										.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
								jasperReport = (JasperReport) JRLoader.loadObject(
										new File((pathGeneralReportes.toString() + "/reporteNota1Hemi.jasper")));
							}

							List<Map<String, Object>> frmAdjuntoCampos = null;
							Map<String, Object> frmAdjuntoParametros = null;
							// String facultadMail =
							// GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
							// String carreraMail =
							// GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());

							frmAdjuntoParametros = new HashMap<String, Object>();
							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss",
									new Locale("es", "ES"));
							String fecha = formato.format(new Date());
							frmAdjuntoParametros.put("fecha", fecha);
							frmAdjuntoParametros.put("docente", npfDocente.getPrsNombres() + " "
									+ npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido());
							frmAdjuntoParametros.put("nick", npfUsuario.getUsrNick());
							StringBuilder sbAsistenciaDocente = new StringBuilder();

							for (EstudianteJdbcDto item : npfListEstudianteBusq) {
								try {
									mtrAux = servNpfMateriaServicio.buscarPorId(mtrModulo.getMtrId());
									frmAdjuntoParametros.put("materia",
											item.getMtrDescripcion() + " - " + mtrAux.getMtrDescripcion());
									Carrera crrAux = servNpfCarreraServicio
											.buscarPorId(npfParaleloDtoEditar.getCrrId());
									Dependencia fclAux = servNpfDependenciaServicio
											.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
									Nivel nvlAux = servNpfNivelServicio.buscarPorId(npfEstudianteBuscar.getNvlId());
									item.setCrrDescripcion(crrAux.getCrrDescripcion());
									item.setCrrDetalle(crrAux.getCrrDetalle());
									item.setNvlDescripcion(nvlAux.getNvlDescripcion());
									item.setDpnDescripcion(fclAux.getDpnDescripcion());
								} catch (MateriaNoEncontradoException | MateriaException e) {
									frmAdjuntoParametros.put("materia", item.getMtrDescripcion());
								} catch (NivelNoEncontradoException e) {
								} catch (NivelException e) {
								} catch (CarreraNoEncontradoException e) {
								} catch (CarreraException e) {
								} catch (DependenciaNoEncontradoException e) {
								} catch (Exception e) {
									frmAdjuntoParametros.put("materia", item.getMtrDescripcion());
								}
								npfNomCarrera = item.getCrrDescripcion().toString();
								npfNomMateria = item.getMtrDescripcion().toString();
								npfNomParalelo = item.getPrlDescripcion().toString();
								frmAdjuntoParametros.put("periodo", item.getPracDescripcion());
								frmAdjuntoParametros.put("facultad", item.getDpnDescripcion());
								frmAdjuntoParametros.put("carrera", item.getCrrDetalle());
								frmAdjuntoParametros.put("curso", item.getNvlDescripcion());
								frmAdjuntoParametros.put("paralelo", item.getPrlDescripcion());

								sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
								frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
								break;
							}
							frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes + "/cabeceraNotas.png");
							frmAdjuntoParametros.put("imagenPie", pathGeneralReportes + "/pieNotas.png");

							frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
							Map<String, Object> datoEstudiantes = null;

							int cont = 1;
							for (EstudianteJdbcDto item : npfListEstudianteBusq) {
								StringBuilder sbNota1 = new StringBuilder();
								StringBuilder sbAsistencia1 = new StringBuilder();
								StringBuilder sbContador = new StringBuilder();
								datoEstudiantes = new HashMap<String, Object>();
								datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
								datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
								datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
								datoEstudiantes.put("nombres", item.getPrsNombres());
								if (item.getClfNota1() != null) {
									sbNota1.append(item.getClfNota1());
									datoEstudiantes.put("nota1", sbNota1.toString());
								} else {
									sbNota1.append("-");
									datoEstudiantes.put("nota1", sbNota1.toString());
								}
								if (item.getClfAsistenciaEstudiante1() != null) {
									if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
										sbAsistencia1
												.append(item.getClfPromedioAsistencia().setScale(0, RoundingMode.DOWN));
										datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
										if (item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE) {
											datoEstudiantes.put("estado", "APROB.");
										} else {
											datoEstudiantes.put("estado", "REPROB.");
										}
									} else {
										sbAsistencia1.append(item.getClfAsistenciaEstudiante1());
										datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
									}

								} else {
									sbAsistencia1.append("-");
									datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
									if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
											|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
										datoEstudiantes.put("estado", "-");
									}
								}
								sbContador.append(cont);
								datoEstudiantes.put("numero", sbContador.toString());

								frmAdjuntoCampos.add(datoEstudiantes);
								cont = cont + 1;
							}

							StringBuilder sbPromedio = new StringBuilder();
							if (cont1 != BigDecimal.ZERO || suma != BigDecimal.ZERO) {
								BigDecimal promedio = suma.divide(cont1, 2, RoundingMode.CEILING);
								if (promedio != null) {
									sbPromedio.append(promedio);
									frmAdjuntoParametros.put("promedio", sbPromedio.toString());
								} else {
									sbPromedio.append("-");
									frmAdjuntoParametros.put("promedio", sbPromedio.toString());
								}
							} else {
								sbPromedio.append("-");
								frmAdjuntoParametros.put("promedio", sbPromedio.toString());
							}
							JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);

							jasperPrint = JasperFillManager.fillReport(jasperReport, frmAdjuntoParametros, dataSource);

							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);

							// lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(npfDocente.getPrsMailInstitucional());
							// path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo = new StringBuilder();

							if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
									|| npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
								sbCorreo = GeneralesUtilidades.generarAsuntoIdiomas(
										GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
										npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido()
												+ " " + npfDocente.getPrsNombres(),
										GeneralesUtilidades.generaStringConTildes(npfNomCarrera),
										GeneralesUtilidades.generaStringConTildes(npfNomMateria), npfNomParalelo);
							} else {
								sbCorreo = GeneralesUtilidades.generarAsunto(
										GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
										npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido()
												+ " " + npfDocente.getPrsNombres(),
										GeneralesUtilidades.generaStringConTildes(npfNomCarrera),
										GeneralesUtilidades.generaStringConTildes(npfNomMateria), npfNomParalelo);
							}

							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
									GeneralesConstantes.APP_REGISTRO_NOTAS, sbCorreo.toString(), "admin", "dt1c201s",
									true, arreglo, "RegistroNotas." + MailDtoConstantes.TIPO_PDF,
									MailDtoConstantes.TIPO_PDF);
							String jsonSt = pmail.generarMail();
							Gson json = new Gson();
							MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
							// Iniciamos el envío de mensajes
							ObjectMessage message = session.createObjectMessage(mailDto);
							producer.send(message);
						} catch (Exception e) {
						}

						// ******************************************************************************
						// *********************** ACA FINALIZA EL ENVIO DE MAIL
						// ************************
						// ******************************************************************************

					}
				}

			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Debe ingresar las horas clase para registrar la asistencia.");
				return null;
			}
		} catch (RecordEstudianteDtoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		}

		// if(opModulo){
		// return "irInicio";
		// }else{

		// }
		// if(npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
		// || npfParaleloDtoEditar.getCrrId() ==
		// CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE){
		// return "irCancelarSuficiencia";
		// }else{
		return "irListarParalelos";
		// }
	}

	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista
	 * de paralelos
	 * 
	 * @return XHTML listar paralelos
	 */
	public String guardarSegundoHemi() {
		try {
			boolean op = true;
			if (npfAsistenciaDocente2 != null) {
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setClfAsistenciaDocente2(npfAsistenciaDocente2);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getRcesId());
					// if(item.getClfNota1()!=null||item.getClfAsistenciaEstudiante1()!=null){
					// RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					// rcesAux =
					// servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(),
					// item.getMlcrprId());
					// servNpfCalificacionServicio.guardarNotasPregradoPrimerHemi(rcesAux,
					// item , npfRegCliente);
					// npfValidadorClic=0;
					// }
					if (item.getClfNota2() == null ^ item.getClfAsistenciaEstudiante2() == null) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(
								"El registro por estudiante debe tener la nota y asistencia del segundo hemisemestre");
						npfValidadorClic = 0;
						return null;
					} else if (item.getClfNota2() != null && item.getClfAsistenciaEstudiante2() != null) {
						try {
							// calculo la suma de na nota1 + nota2 con redondeo
							// de una cifra decimal
							BigDecimal sumaParciales = BigDecimal.ZERO;
							sumaParciales = item.getClfNota1().setScale(2, RoundingMode.DOWN)
									.add(item.getClfNota2().setScale(2, RoundingMode.DOWN));
							item.setClfSumaP1P2(sumaParciales);

							// calculo de la suma de asistencia del estudiante
							// de los dos parciales
							int sumaAsistenciaParciales = 0;
							sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1()
									+ item.getClfAsistenciaEstudiante2();
							item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));

							// calculo de la suma de la asistencia del docente
							// de los dos parciales
							int sumaAsistenciaDoc = 0;
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() + item.getClfAsistenciaDocente2();
							item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));

							// calcula el promedio de la asistencia del
							// estudiante
							item.setClfPromedioAsistencia(
									calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE,
											item.getClfAsistenciaTotal().intValue(),
											item.getClfAsistenciaTotalDoc().intValue()));

							int com = item.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
							// si la suma de los parciales es mayor o igual a
							// 27.5
							if (com == 1 || com == 0) {
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
								// si el promedio de asistencia es mayor o igual
								// a 80
								if (promedioAsistencia == 1 || promedioAsistencia == 0) {
									// calcula la nota final del semestre y el
									// estado a aprobado
									item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								} else {// si el promedio de asistencia es menor
										// a 80
									item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							} else {
								// if(item.getDtmtNumero()==3){
								// item.setClfNotalFinalSemestre(sumaParciales.setScale(2,
								// RoundingMode.HALF_UP));
								// item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								// }else{
								int minNota = item.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
								// si la suma de los parciales el menor a 27.5 y
								// es mayor o igual a 8.8
								if (minNota == 0 || minNota == 1) {
									int promedioAsistencia = 0;
									promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									if (promedioAsistencia == 1 || promedioAsistencia == 0) {
										BigDecimal itemCost = BigDecimal.ZERO;
										itemCost = sumaParciales
												.multiply(
														new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE))
												.divide(new BigDecimal(
														CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2,
														RoundingMode.DOWN);
										item.setClfParamRecuperacion1(itemCost);
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										// if(item.getDtmtNumero()==3){
										// item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
										// }else{

										if (npfCrrId == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
											item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
										} else {
											item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
										}

										// }

									} else {
										BigDecimal itemCost = BigDecimal.ZERO;
										itemCost = sumaParciales
												.multiply(
														new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE))
												.divide(new BigDecimal(
														CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2,
														RoundingMode.DOWN);
										item.setClfParamRecuperacion1(itemCost);
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								} else {
									item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
								// }

							}
						} catch (Exception e) {
						}

						try {
							servNpfCalificacionServicio.guardarEdicionNotasSegundoHemi(rcesAux, item, npfRegCliente);

						} catch (CalificacionValidacionException e) {
							op = false;
						} catch (CalificacionException e) {
							op = false;
						}
					} else {
						try {
							// calculo la suma de na nota1 + nota2 con redondeo
							// de una cifra decimal
							item.setClfNota2(BigDecimal.ZERO);
							item.setClfAsistenciaEstudiante2(Integer.valueOf(0));
							BigDecimal sumaParciales = BigDecimal.ZERO;
							sumaParciales = item.getClfNota1().setScale(2, RoundingMode.DOWN)
									.add(item.getClfNota2().setScale(2, RoundingMode.DOWN));
							item.setClfSumaP1P2(sumaParciales);

							// calculo de la suma de asistencia del estudiante
							// de los dos parciales
							int sumaAsistenciaParciales = 0;
							sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1()
									+ item.getClfAsistenciaEstudiante2();
							item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));

							// calculo de la suma de la asistencia del docente
							// de los dos parciales
							int sumaAsistenciaDoc = 0;
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() + item.getClfAsistenciaDocente2();
							item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));

							// calcula el promedio de la asistencia del
							// estudiante
							item.setClfPromedioAsistencia(
									calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE,
											item.getClfAsistenciaTotal().intValue(),
											item.getClfAsistenciaTotalDoc().intValue()));

							int com = item.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
							// si la suma de los parciales es mayor o igual a
							// 27.5
							if (com == 1 || com == 0) {
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
								// si el promedio de asistencia es mayor o igual
								// a 80
								if (promedioAsistencia == 1 || promedioAsistencia == 0) {
									// calcula la nota final del semestre y el
									// estado a aprobado
									item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								} else {// si el promedio de asistencia es menor
										// a 80
									item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							} else {
								// if(item.getDtmtNumero()==3){
								// item.setClfNotalFinalSemestre(sumaParciales.setScale(2,
								// RoundingMode.HALF_UP));
								// item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								// }else{
								int minNota = item.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
								// si la suma de los parciales el menor a 27.5 y
								// es mayor o igual a 8.8
								if (minNota == 0 || minNota == 1) {
									int promedioAsistencia = 0;
									promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									if (promedioAsistencia == 1 || promedioAsistencia == 0) {
										BigDecimal itemCost = BigDecimal.ZERO;
										itemCost = sumaParciales
												.multiply(
														new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE))
												.divide(new BigDecimal(
														CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2,
														RoundingMode.DOWN);
										item.setClfParamRecuperacion1(itemCost);
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
									} else {
										BigDecimal itemCost = BigDecimal.ZERO;
										itemCost = sumaParciales
												.multiply(
														new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE))
												.divide(new BigDecimal(
														CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2,
														RoundingMode.DOWN);
										item.setClfParamRecuperacion1(itemCost);
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}
								} else {

									int notaExclusion = item.getClfSumaP1P2().compareTo(new BigDecimal(8.5));
									if (notaExclusion == -1) {
										item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									} else {
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
									}

								}
								// }

							}
						} catch (Exception e) {
						}

						try {
							servNpfCalificacionServicio.guardarEdicionNotasSegundoHemi(rcesAux, item, npfRegCliente);
						} catch (CalificacionValidacionException e) {
							op = false;
						} catch (CalificacionException e) {
							op = false;
						} catch (Exception e) {
							op = false;
						}
					}

					
					try {
						servNpfCalificacionServicio.guardarCorreccionFull(item.getClfId());
					} catch (Exception e) {
					}
					npfValidadorClic = 0;

				}
				if (op) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Ingreso de notas exitoso");

					// ******************************************************************************
					// ************************* ACA INICIA EL ENVIO DE MAIL CON
					// ADJUNTO ************
					// ******************************************************************************

					try {
						Connection connection = null;
						Session session = null;
						MessageProducer producer = null;
						ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
								GeneralesConstantes.APP_NIO_ACTIVEMQ);
						connection = connectionFactory.createConnection();
						connection.start();
						session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
						// Creamos un productor
						producer = session.createProducer(destino);

						JasperReport jasperReport = null;
						JasperPrint jasperPrint;
						StringBuilder pathGeneralReportes = new StringBuilder();
						pathGeneralReportes
								.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/segundoHemi");
						jasperReport = (JasperReport) JRLoader
								.loadObject(new File((pathGeneralReportes.toString() + "/reporteNota2Hemi.jasper")));
						List<Map<String, Object>> frmAdjuntoCampos = null;
						Map<String, Object> frmAdjuntoParametros = null;
						// String facultadMail =
						// GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
						// String carreraMail =
						// GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());

						try {
							npfDocente = servDocenteDtoServicioJdbc.buscarDatosDocenteXUsuario(npfUsuario.getUsrId());
						} catch (DetallePuestoDtoJdbcException e2) {
						} catch (DetallePuestoDtoJdbcNoEncontradoException e2) {
						}
						frmAdjuntoParametros = new HashMap<String, Object>();
						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss",
								new Locale("es", "ES"));
						String fecha = formato.format(new Date());
						frmAdjuntoParametros.put("fecha", fecha);
						try {
							frmAdjuntoParametros.put("docente", npfDocente.getPrsNombres() + " "
									+ npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido());
						} catch (Exception e) {
							frmAdjuntoParametros.put("docente",
									npfDocente.getPrsNombres() + " " + npfDocente.getPrsPrimerApellido());
						}

						frmAdjuntoParametros.put("nick", npfUsuario.getUsrNick());
						StringBuilder sbAsistenciaDocente = new StringBuilder();
						for (EstudianteJdbcDto item : npfListEstudianteBusq) {
							Materia mtrAux = new Materia();
							try {
								mtrAux = servNpfMateriaServicio.buscarPorId(npfEstudianteBuscar.getMtrId());
							} catch (MateriaNoEncontradoException | MateriaException e1) {
							}
							try {

								// frmAdjuntoParametros.put("materia",
								// GeneralesUtilidades.generaStringConTildes(item.getMtrDescripcion()+"
								// - "+mtrAux.getMtrDescripcion()));
								Carrera crrAux = servNpfCarreraServicio.buscarPorId(npfParaleloDtoEditar.getCrrId());
								Dependencia fclAux = servNpfDependenciaServicio
										.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
								Nivel nvlAux = servNpfNivelServicio.buscarPorId(npfEstudianteBuscar.getNvlId());
								item.setCrrDescripcion(crrAux.getCrrDescripcion());
								item.setCrrDetalle(crrAux.getCrrDetalle());
								item.setNvlDescripcion(nvlAux.getNvlDescripcion());
								item.setDpnDescripcion(fclAux.getDpnDescripcion());

							} catch (NivelNoEncontradoException e) {
							} catch (NivelException e) {
							} catch (CarreraNoEncontradoException e) {
							} catch (CarreraException e) {
							} catch (DependenciaNoEncontradoException e) {
							} catch (Exception e) {
							}
							npfNomCarrera = item.getCrrDescripcion().toString();
							frmAdjuntoParametros.put("materia", mtrAux.getMtrDescripcion());
							npfNomParalelo = item.getPrlDescripcion().toString();
							frmAdjuntoParametros.put("periodo", item.getPracDescripcion());
							frmAdjuntoParametros.put("facultad", item.getDpnDescripcion());
							frmAdjuntoParametros.put("carrera", item.getCrrDetalle());
							frmAdjuntoParametros.put("curso", item.getNvlDescripcion());
							frmAdjuntoParametros.put("paralelo", item.getPrlDescripcion());
							sbAsistenciaDocente.append(item.getClfAsistenciaDocente2());
							frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
							break;
						}
						frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes + "/cabeceraNotas.png");
						frmAdjuntoParametros.put("imagenPie", pathGeneralReportes + "/pieNotas.png");

						frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
						Map<String, Object> datoEstudiantes = null;

						int cont = 1;
						BigDecimal suma = BigDecimal.ZERO;
						for (EstudianteJdbcDto item : npfListEstudianteBusq) {
							StringBuilder sbNota1 = new StringBuilder();
							StringBuilder sbAsistencia1 = new StringBuilder();
							StringBuilder sbContador = new StringBuilder();
							datoEstudiantes = new HashMap<String, Object>();
							datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
							datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
							datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
							datoEstudiantes.put("nombres", item.getPrsNombres());

							if (item.getClfNota2() != null) {
								sbNota1.append(item.getClfNota2());
								datoEstudiantes.put("nota1", sbNota1.toString());
								suma = suma.add(item.getClfNota2());
							} else {
								sbNota1.append("-");
								datoEstudiantes.put("nota1", sbNota1.toString());
							}
							if (item.getClfAsistenciaEstudiante2() != null) {
								sbAsistencia1.append(item.getClfAsistenciaEstudiante2());
								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
							} else {
								sbAsistencia1.append("-");
								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
							}
							sbContador.append(cont);
							datoEstudiantes.put("numero", sbContador.toString());

							frmAdjuntoCampos.add(datoEstudiantes);
							cont = cont + 1;
						}
						StringBuilder sbPromedio = new StringBuilder();
						if ((new BigDecimal(cont) != BigDecimal.ZERO) || suma != BigDecimal.ZERO) {
							BigDecimal promedio = suma.divide(new BigDecimal(cont), 2, RoundingMode.CEILING);
							if (promedio != null) {
								sbPromedio.append(promedio);
								frmAdjuntoParametros.put("promedio", sbPromedio.toString());
							} else {
								sbPromedio.append("-");
								frmAdjuntoParametros.put("promedio", sbPromedio.toString());
							}
						} else {
							sbPromedio.append("-");
							frmAdjuntoParametros.put("promedio", sbPromedio.toString());
						}

						JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);

						jasperPrint = JasperFillManager.fillReport(jasperReport, frmAdjuntoParametros, dataSource);

						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
						// AdjuntoDto adjuntoDto = new AdjuntoDto();
						// adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
						// adjuntoDto.setAdjunto(arreglo);

						// lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(npfDocente.getPrsMailInstitucional());
						// path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo = new StringBuilder();
						try {
							Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfEstudianteBuscar.getMtrId());
							try {
								sbCorreo = GeneralesUtilidades.generarAsunto(
										GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
										npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido()
												+ " " + npfDocente.getPrsNombres(),
										npfNomCarrera,
										GeneralesUtilidades.generaStringConTildes(mtrAux.getMtrDescripcion()),
										npfNomParalelo);

							} catch (Exception e) {
								sbCorreo = GeneralesUtilidades.generarAsunto(
										GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
										npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsNombres(),
										npfNomCarrera,
										GeneralesUtilidades.generaStringConTildes(mtrAux.getMtrDescripcion()),
										npfNomParalelo);

							}

						} catch (Exception e) {
						}

						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
								GeneralesConstantes.APP_REGISTRO_NOTAS, sbCorreo.toString(), "admin", "dt1c201s", true,
								arreglo, "RegistroNotas." + MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF);
						String jsonSt = pmail.generarMail();
						Gson json = new Gson();
						MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
						// Iniciamos el envío de mensajes
						ObjectMessage message = session.createObjectMessage(mailDto);
						producer.send(message);
					} catch (JMSException e) {
					} catch (JRException e) {
					} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
					} catch (Exception e) {
						e.printStackTrace();
					}

					// ******************************************************************************
					// *********************** ACA FINALIZA EL ENVIO DE MAIL
					// ************************
					// ******************************************************************************

				} else {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(
							"Se produjo un error al guardar las notas, por favor revise la información ingresada.");
				}
			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Debe ingresar las horas clase del hemisemestre");
				return null;
			}
		} catch (RecordEstudianteDtoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		}
		return "irListarParalelos";
	}

	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista
	 * de paralelos
	 * 
	 * @return XHTML listar paralelos
	 */
	public String guardarRecuperacion() {
		try {
			boolean op = false;
			Materia mtrAux = servNpfMateriaServicio.buscarPorId(npfParaleloDtoEditar.getMtrId());
			if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setMateriaModulo(true);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(),
							item.getMlcrprId());
					if (item.getClfSupletorio() != null) {
						servNpfCalificacionServicio.guardarNotasPregradoRecuperacion(rcesAux, item, npfRegCliente);
						npfValidadorClic = 0;
						op = true;
						servNpfCalificacionServicio.verificarModulos(rcesAux, item, npfRegCliente);

					} else {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Debe ingresar la nota de recuperación de todos los estudiantes");
						return null;
					}
				}
			} else {
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {

					if (npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_INGLES_VALUE
							|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_COREANO_VALUE
							|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_FRANCES_VALUE
							|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_ITALIANO_VALUE
							|| npfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_KICHWA_VALUE) {
						RecordEstudianteDto rcesAux = new RecordEstudianteDto();
						item.setMateriaModulo(false);
						rcesAux = servNpfRecordEstudianteDtoServicioJdbc
								.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
						if (item.getClfSupletorio() != null) {
							int estadoRces = item.getClfSupletorio().compareTo(
									new BigDecimal(CalificacionConstantes.NOTA_APROBACION_INGLES_RECUPERACION_VALUE));
							if (estadoRces == 1 || estadoRces == 0) {
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								item.setClfNotalFinalSemestre(
										item.getClfSupletorio().setScale(0, RoundingMode.HALF_UP));
							} else {
								item.setClfNotalFinalSemestre(item.getClfSupletorio().setScale(2, RoundingMode.DOWN));
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							}
							servNpfCalificacionServicio.guardarNotasPregradoRecuperacion(rcesAux, item, npfRegCliente);
							npfValidadorClic = 0;
							op = true;
						} else {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Debe ingresar la nota de recuperación de todos los estudiantes");
							return null;
						}
					} else {
						RecordEstudianteDto rcesAux = new RecordEstudianteDto();
						item.setMateriaModulo(false);
						rcesAux = servNpfRecordEstudianteDtoServicioJdbc
								.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
						if (item.getClfSupletorio() != null) {
							BigDecimal parametro2Aux = BigDecimal.ZERO;
							parametro2Aux = item.getClfSupletorio()
									.multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE))
									.setScale(2, RoundingMode.HALF_UP);
							item.setClfParamRecuperacion2(parametro2Aux);
							BigDecimal parametro1Aux = BigDecimal.ZERO;
							parametro1Aux = item.getClfSumaP1P2()
									.multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE))
									.setScale(2, RoundingMode.HALF_UP);
							;
							item.setClfParamRecuperacion1(parametro1Aux);

							BigDecimal sumaParametros = BigDecimal.ZERO;
							sumaParametros = parametro2Aux.add(parametro1Aux);
							// item.setClfNotalFinalSemestre(sumaParametros.setScale(2,
							// RoundingMode.HALF_UP));
							// item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							int estadoRces = sumaParametros
									.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
							if (estadoRces == 1 || estadoRces == 0) {
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								item.setClfNotalFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP));
							} else {
								item.setClfNotalFinalSemestre(sumaParametros.setScale(2, RoundingMode.DOWN));
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								rcesAux.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
							}
							servNpfCalificacionServicio.guardarNotasPregradoRecuperacion(rcesAux, item, npfRegCliente);
							npfValidadorClic = 0;
							op = true;
						} else {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("Debe ingresar la nota de recuperación de todos los estudiantes");
							return null;
						}
					}

					// ******************************************************************************
					// ************************* ACA INICIA EL ENVIO DE MAIL CON
					// ADJUNTO ************
					// ******************************************************************************
					// if(op){
					// try{
					// Connection connection = null;
					// Session session = null;
					// MessageProducer producer = null;
					// ActiveMQConnectionFactory connectionFactory = new
					// ActiveMQConnectionFactory("admin","admin",GeneralesConstantes.APP_NIO_ACTIVEMQ);
					// connection = connectionFactory.createConnection();
					// connection.start();
					// session = connection.createSession(false,
					// Session.AUTO_ACKNOWLEDGE);
					// Destination destino =
					// session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
					// // Creamos un productor
					// producer = session.createProducer(destino);
					//
					// JasperReport jasperReport = null;
					// JasperPrint jasperPrint;
					// StringBuilder pathGeneralReportes = new StringBuilder();
					// pathGeneralReportes.append(FacesContext.getCurrentInstance()
					// .getExternalContext().getRealPath("/"));
					// pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/recuperacion");
					// jasperReport = (JasperReport) JRLoader.loadObject(new
					// File(
					// (pathGeneralReportes.toString() +
					// "/reporteNotaRecuperacion.jasper")));
					// List<Map<String, Object>> frmAdjuntoCampos = null;
					// Map<String, Object> frmAdjuntoParametros = null;
					//// String facultadMail =
					// GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
					//// String carreraMail =
					// GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
					//
					//
					//
					// frmAdjuntoParametros = new HashMap<String, Object>();
					// SimpleDateFormat formato = new SimpleDateFormat("d 'de'
					// MMMM 'de' yyyy", new Locale("es", "ES"));
					// String fecha = formato.format(new Date());
					// frmAdjuntoParametros.put("fecha",fecha);
					//// StringBuilder sbAsistenciaDocente = new
					// StringBuilder();
					// frmAdjuntoParametros.put("docente",
					// npfDocente.getPrsNombres()+"
					// "+npfDocente.getPrsPrimerApellido()+"
					// "+npfDocente.getPrsSegundoApellido());
					// frmAdjuntoParametros.put("nick",
					// npfUsuario.getUsrNick());
					// for (EstudianteJdbcDto item1 : npfListEstudianteBusq) {
					// npfNomCarrera = item1.getCrrDescripcion().toString();
					// npfNomMateria = item1.getMtrDescripcion().toString();
					// npfNomParalelo = item1.getPrlDescripcion().toString();;
					// frmAdjuntoParametros.put("periodo",
					// item1.getPracDescripcion());
					// frmAdjuntoParametros.put("facultad",
					// item1.getDpnDescripcion());
					// frmAdjuntoParametros.put("carrera",
					// item1.getCrrDetalle());
					// frmAdjuntoParametros.put("curso",
					// item1.getNvlDescripcion());
					// frmAdjuntoParametros.put("paralelo",
					// item1.getPrlDescripcion());
					// frmAdjuntoParametros.put("materia",
					// mtrAux.getMtrDescripcion());
					// break;
					// }
					// frmAdjuntoParametros.put("imagenCabecera",
					// pathGeneralReportes+"/cabeceraNotas.png");
					// frmAdjuntoParametros.put("imagenPie",
					// pathGeneralReportes+"/pieNotas.png");
					//
					// frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
					// Map<String, Object> datoEstudiantes = null;
					// int cont = 1;
					//
					// for (EstudianteJdbcDto item2 : npfListEstudianteBusq) {
					// StringBuilder sbNota3 = new StringBuilder();
					//// StringBuilder sbAsistencia2 = new StringBuilder();
					// StringBuilder sbContador = new StringBuilder();
					// datoEstudiantes = new HashMap<String, Object>();
					// datoEstudiantes.put("identificacion",
					// item2.getPrsIdentificacion());
					// datoEstudiantes.put("apellido_paterno",
					// item2.getPrsPrimerApellido());
					// datoEstudiantes.put("apellido_materno",
					// item2.getPrsSegundoApellido());
					// datoEstudiantes.put("nombres", item2.getPrsNombres());
					// if(item2.getClfSupletorio() != null){
					// sbNota3.append(item2.getClfSupletorio());
					// datoEstudiantes.put("nota1", sbNota3.toString());
					// }else{
					// sbNota3.append("-");
					// datoEstudiantes.put("nota1", sbNota3.toString());
					// }
					// sbContador.append(cont);
					// datoEstudiantes.put("numero", sbContador.toString());
					//
					// frmAdjuntoCampos.add(datoEstudiantes);
					// cont = cont + 1;
					// }
					//
					// JRDataSource dataSource = new
					// JRBeanCollectionDataSource(frmAdjuntoCampos);
					//
					//
					// jasperPrint = JasperFillManager.fillReport(jasperReport,
					// frmAdjuntoParametros, dataSource);
					//
					//
					// byte[] arreglo =
					// JasperExportManager.exportReportToPdf(jasperPrint);
					//// AdjuntoDto adjuntoDto = new AdjuntoDto();
					//// adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
					//// adjuntoDto.setAdjunto(arreglo);
					//
					// //lista de correos a los que se enviara el mail
					// List<String> correosTo = new ArrayList<>();
					// correosTo.add(npfDocente.getPrsMailInstitucional());
					// //path de la plantilla del mail
					// ProductorMailJson pmail = null;
					// StringBuilder sbCorreo= new StringBuilder();
					// sbCorreo=
					// GeneralesUtilidades.generarAsunto(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
					// npfDocente.getPrsPrimerApellido()+"
					// "+npfDocente.getPrsSegundoApellido()+"
					// "+npfDocente.getPrsNombres() ,
					// GeneralesUtilidades.generaStringParaCorreo(npfNomCarrera)
					// , mtrAux.getMtrDescripcion() , npfNomParalelo);
					// pmail = new ProductorMailJson(correosTo, null, null,
					// GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_REGISTRO_NOTAS,
					// sbCorreo.toString()
					// , "admin", "dt1c201s", true, arreglo,
					// "RegistroNotas."+MailDtoConstantes.TIPO_PDF,
					// MailDtoConstantes.TIPO_PDF );
					// String jsonSt = pmail.generarMail();
					// Gson json = new Gson();
					// MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
					// // Iniciamos el envío de mensajes
					// ObjectMessage message =
					// session.createObjectMessage(mailDto);
					// producer.send(message);
					// } catch (JMSException e) {
					// } catch (JRException e) {
					// } catch
					// (ec.edu.uce.mailDto.excepciones.ValidacionMailException
					// e) {
					// }
					//
					// }
					// FacesUtil.limpiarMensaje();
					// FacesUtil.mensajeInfo("Ingreso de notas exitoso");
					//
					// //******************************************************************************
					// //*********************** ACA FINALIZA EL ENVIO DE MAIL
					// ************************
					// //******************************************************************************
				}
				// ******************************************************************************
				// ************************* ACA INICIA EL ENVIO DE MAIL CON
				// ADJUNTO ************
				// ******************************************************************************
				if (op) {
					try {
						Connection connection = null;
						Session session = null;
						MessageProducer producer = null;
						ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
								GeneralesConstantes.APP_NIO_ACTIVEMQ);
						connection = connectionFactory.createConnection();
						connection.start();
						session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
						// Creamos un productor
						producer = session.createProducer(destino);

						JasperReport jasperReport = null;
						JasperPrint jasperPrint;
						StringBuilder pathGeneralReportes = new StringBuilder();
						pathGeneralReportes
								.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
						pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/recuperacion");
						jasperReport = (JasperReport) JRLoader.loadObject(
								new File((pathGeneralReportes.toString() + "/reporteNotaRecuperacion.jasper")));
						List<Map<String, Object>> frmAdjuntoCampos = null;
						Map<String, Object> frmAdjuntoParametros = null;
						// String facultadMail =
						// GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
						// String carreraMail =
						// GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());

						frmAdjuntoParametros = new HashMap<String, Object>();
						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy",
								new Locale("es", "ES"));
						String fecha = formato.format(new Date());
						frmAdjuntoParametros.put("fecha", fecha);
						// StringBuilder sbAsistenciaDocente = new
						// StringBuilder();
						try {
							frmAdjuntoParametros.put("docente", npfDocente.getPrsNombres() + " "
									+ npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido());
						} catch (Exception e) {
							frmAdjuntoParametros.put("docente",
									npfDocente.getPrsNombres() + " " + npfDocente.getPrsPrimerApellido());
						}

						frmAdjuntoParametros.put("nick", npfUsuario.getUsrNick());
						for (EstudianteJdbcDto item1 : npfListEstudianteBusq) {
							npfNomCarrera = item1.getCrrDescripcion().toString();
							npfNomMateria = item1.getMtrDescripcion().toString();
							npfNomParalelo = item1.getPrlDescripcion().toString();
							;
							frmAdjuntoParametros.put("periodo", item1.getPracDescripcion());
							frmAdjuntoParametros.put("facultad", item1.getDpnDescripcion());
							frmAdjuntoParametros.put("carrera", item1.getCrrDetalle());
							frmAdjuntoParametros.put("curso", item1.getNvlDescripcion());
							frmAdjuntoParametros.put("paralelo", item1.getPrlDescripcion());
							frmAdjuntoParametros.put("materia", mtrAux.getMtrDescripcion());
							break;
						}
						frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes + "/cabeceraNotas.png");
						frmAdjuntoParametros.put("imagenPie", pathGeneralReportes + "/pieNotas.png");

						frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
						Map<String, Object> datoEstudiantes = null;
						int cont = 1;

						for (EstudianteJdbcDto item2 : npfListEstudianteBusq) {
							StringBuilder sbNota3 = new StringBuilder();
							// StringBuilder sbAsistencia2 = new
							// StringBuilder();
							StringBuilder sbContador = new StringBuilder();
							datoEstudiantes = new HashMap<String, Object>();
							datoEstudiantes.put("identificacion", item2.getPrsIdentificacion());
							datoEstudiantes.put("apellido_paterno", item2.getPrsPrimerApellido());
							datoEstudiantes.put("apellido_materno", item2.getPrsSegundoApellido());
							datoEstudiantes.put("nombres", item2.getPrsNombres());
							if (item2.getClfSupletorio() != null) {
								sbNota3.append(item2.getClfSupletorio());
								datoEstudiantes.put("nota1", sbNota3.toString());
							} else {
								sbNota3.append("-");
								datoEstudiantes.put("nota1", sbNota3.toString());
							}
							sbContador.append(cont);
							datoEstudiantes.put("numero", sbContador.toString());

							frmAdjuntoCampos.add(datoEstudiantes);
							cont = cont + 1;
						}

						JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);

						jasperPrint = JasperFillManager.fillReport(jasperReport, frmAdjuntoParametros, dataSource);

						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
						// AdjuntoDto adjuntoDto = new AdjuntoDto();
						// adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
						// adjuntoDto.setAdjunto(arreglo);

						// lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(npfDocente.getPrsMailInstitucional());
						// path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo = new StringBuilder();
						sbCorreo = GeneralesUtilidades.generarAsunto(
								GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
								npfDocente.getPrsPrimerApellido() + " " + npfDocente.getPrsSegundoApellido() + " "
										+ npfDocente.getPrsNombres(),
								GeneralesUtilidades.generaStringParaCorreo(npfNomCarrera), mtrAux.getMtrDescripcion(),
								npfNomParalelo);
						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
								GeneralesConstantes.APP_REGISTRO_NOTAS, sbCorreo.toString(), "admin", "dt1c201s", true,
								arreglo, "RegistroNotas." + MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF);
						String jsonSt = pmail.generarMail();
						Gson json = new Gson();
						MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
						// Iniciamos el envío de mensajes
						ObjectMessage message = session.createObjectMessage(mailDto);
						producer.send(message);
					} catch (JMSException e) {
					} catch (JRException e) {
					} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
					}

				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Ingreso de notas exitoso");

				// ******************************************************************************
				// *********************** ACA FINALIZA EL ENVIO DE MAIL
				// ************************
				// ******************************************************************************
			}

		} catch (RecordEstudianteDtoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());

		} catch (Exception e) {

		}
		return "irListarParalelos";
	}

	public boolean activacion() {
		boolean retorno = false;

		for (EstudianteJdbcDto item : npfListEstudianteBusq) {
			int aux = item.getRcesIngersoNota();
			if (aux == 2) {
				retorno = true;
				break;
			}

		}
		return retorno;
	}

	/**
	 * Método que guarde de forma temporal los registros de la lista de
	 * estudiantes
	 * 
	 * @return XHTML listar paralelos
	 */
	public String guardoTemporal() {
		try {
			if (npfAsistenciaDocente != null) {
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setClfAsistenciaDocente1(npfAsistenciaDocente);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(),
							item.getMlcrprId());
					// if(item.getClfNota1()!=null||item.getClfAsistenciaEstudiante1()!=null){
					// RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					// rcesAux =
					// servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(),
					// item.getMlcrprId());
					// servNpfCalificacionServicio.guardoTemporalNotasPregradoPrimerHemi(rcesAux,
					// item , npfRegCliente);
					// npfValidadorClic=0;
					// }
					if (item.getClfNota1() == null ^ item.getClfAsistenciaEstudiante1() == null) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(
								"EL registro por estudiante debe tener la nota y asistencia del primer hemisemestre");
						npfValidadorClic = 0;
						return null;
					}
					// if (item.getClfNota1()!=null &&
					// item.getClfAsistenciaEstudiante1()!=null){

					servNpfCalificacionServicio.guardoTemporalNotasPregradoPrimerHemi(rcesAux, item, npfRegCliente);
					npfValidadorClic = 0;
					// }
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Guardado temporal de notas exitoso");
			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Debe ingresar la asistencia del docente");
				npfValidadorClic = 0;
			}
		} catch (RecordEstudianteDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CalificacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CalificacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irListarParalelos";
	}

	// ****************************************************************/
	// ******************* METODOS AUXILIARES *************************/
	// ****************************************************************/

	public Boolean validador(BigDecimal valor) {
		Boolean retorno = false;
		String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(valor.toString());
		valorSt = valorSt.replace(",", ".");
		try {
			Float.parseFloat(valorSt);
			int puntoDecimalUbc = valorSt.indexOf('.');
			if (puntoDecimalUbc == 0) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se permiten números sin parte entera");
				return false;
			}
			int totalDecimales = valorSt.length() - puntoDecimalUbc - 1;
			if (puntoDecimalUbc == -1) {
				if (Float.parseFloat(valorSt) > 20) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Calificación máximo sobre 20 puntos");
					return false;
				} else if (Float.parseFloat(valorSt) < 0) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No se permiten números negativos");
					return false;
				}
			} else {
				if (totalDecimales > 2) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Sólo permite máximo 2 número decimales");
					return false;
				} else if (Float.parseFloat(valorSt) > 20) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Calificación máximo sobre 20 puntos");
					return false;
				} else if (Float.parseFloat(valorSt) < 0) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No se permiten números negativos");
					return false;
				}
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Sólo números y signos decimales");
			return false;
		}
		retorno = true;
		return retorno;
	}

	public void validarPuntoDecimal() {
		for (EstudianteJdbcDto item : npfListEstudianteBusq) {
			try {
				String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(item.getClfNota1String());
				valorSt = valorSt.replace(",", ".");
				item.setClfNota1String(valorSt);
			} catch (Exception e) {
			}
		}
	}

	public Boolean validadorAsistencia(String asistenciaEst, Integer asistenciaDocente) {
		Boolean retorno = false;
		try {
			int valor = Integer.parseInt(asistenciaEst);
			if (valor > asistenciaDocente) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La asitencia no puede ser mayor a la asistencia del docente");
				return false;
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar la asistencia del docente en el hemisemestre");
			return false;
		}
		retorno = true;
		return retorno;

	}

	// INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros() {
		// INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		npfEstudianteBuscar = new EstudianteJdbcDto();
		// INICIALIZO EL PERIODO ACADEMICO ID
		npfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// INICIALIZO LA CARRERA ID
		npfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		// INICIALIZO EL NIVEL ID
		npfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		// INICIALIZO EL PARALELO ID
		npfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		// INICIALIZO LA MATERIA ID
		npfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		// ANULO LA LISTA DE NIVEL
		npfListNivelBusq = null;
		// ANULO LA LISTA DE PARALELOS
		npfListParaleloBusq = null;
		// ANULO LA LISTA DE MATERIAS
		npfListMateriaBusq = null;
		npfListMateriaBusqAux = null;
		// ANULO LA LISTA DE NIVELES
		npffListNivel = null;
		mtrModulo = new Materia();
		npfAsistenciaDocente = null;
		npfCarreraIngreso = new Carrera();

		npfCarrera = new Carrera();
		npfCarrera.setCrrTipo(CarreraConstantes.TIPO_POSGRADO_VALUE);
		npfActivadorCohorte = 1;
		npfTextoCabecera = " ";
		npfTextoOpcion = "1er Hemisemestre";
		npfTextoCarrera = "Carrera";
		activadorAsistencia = false;
	}

	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada() {
	}

	// Suma los días recibidos a la fecha
	public Date sumarRestarDiasFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o
													// restar en caso de días<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos
									// días añadidos
	}

	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/
	public Usuario getNpfUsuario() {
		return npfUsuario;
	}

	public void setNpfUsuario(Usuario npfUsuario) {
		this.npfUsuario = npfUsuario;
	}

	public DocenteJdbcDto getNpfDocente() {
		return npfDocente;
	}

	public void setNpfDocente(DocenteJdbcDto npfDocente) {
		this.npfDocente = npfDocente;
	}

	public EstudianteJdbcDto getNpfEstudianteBuscar() {
		return npfEstudianteBuscar;
	}

	public void setNpfEstudianteBuscar(EstudianteJdbcDto npfEstudianteBuscar) {
		this.npfEstudianteBuscar = npfEstudianteBuscar;
	}

	public List<CarreraDto> getNpfListCarreraBusq() {
		npfListCarreraBusq = npfListCarreraBusq == null ? (new ArrayList<CarreraDto>()) : npfListCarreraBusq;
		return npfListCarreraBusq;
	}

	public void setNpfListCarreraBusq(List<CarreraDto> npfListCarreraBusq) {
		this.npfListCarreraBusq = npfListCarreraBusq;
	}

	public List<NivelDto> getNpfListNivelBusq() {
		npfListNivelBusq = npfListNivelBusq == null ? (new ArrayList<NivelDto>()) : npfListNivelBusq;
		return npfListNivelBusq;
	}

	public void setNpfListNivelBusq(List<NivelDto> npfListNivelBusq) {
		this.npfListNivelBusq = npfListNivelBusq;
	}

	public List<MateriaDto> getNpfListMateriaBusq() {
		npfListMateriaBusq = npfListMateriaBusq == null ? (new ArrayList<MateriaDto>()) : npfListMateriaBusq;
		return npfListMateriaBusq;
	}

	public void setNpfListMateriaBusq(List<MateriaDto> npfListMateriaBusq) {
		this.npfListMateriaBusq = npfListMateriaBusq;
	}

	public List<EstudianteJdbcDto> getNpfListEstudianteBusq() {
		npfListEstudianteBusq = npfListEstudianteBusq == null ? (new ArrayList<EstudianteJdbcDto>())
				: npfListEstudianteBusq;
		return npfListEstudianteBusq;
	}

	public void setNpfListEstudianteBusq(List<EstudianteJdbcDto> npfListEstudianteBusq) {
		this.npfListEstudianteBusq = npfListEstudianteBusq;
	}

	public List<ParaleloDto> getNpfListParaleloBusq() {
		npfListParaleloBusq = npfListParaleloBusq == null ? (new ArrayList<ParaleloDto>()) : npfListParaleloBusq;
		return npfListParaleloBusq;
	}

	public void setNpfListParaleloBusq(List<ParaleloDto> npfListParaleloBusq) {
		this.npfListParaleloBusq = npfListParaleloBusq;
	}

	public ParaleloDto getNpfParaleloDtoEditar() {
		return npfParaleloDtoEditar;
	}

	public void setNpfParaleloDtoEditar(ParaleloDto npfParaleloDtoEditar) {
		this.npfParaleloDtoEditar = npfParaleloDtoEditar;
	}

	public Integer getNpfAsistenciaDocente() {
		return npfAsistenciaDocente;
	}

	public void setNpfAsistenciaDocente(Integer npfAsistenciaDocente) {
		this.npfAsistenciaDocente = npfAsistenciaDocente;
	}

	public Integer getNpfValidadorClic() {
		return npfValidadorClic;
	}

	public void setNpfValidadorClic(Integer npfValidadorClic) {
		this.npfValidadorClic = npfValidadorClic;
	}

	// private void setIpAdd() {
	// try {
	// InetAddress thisIp = InetAddress.getLocalHost();
	// thisIpAddress = thisIp.getHostAddress().toString();
	//
	// thisIp = InetAddress.getLocalHost();
	//
	// NetworkInterface network = NetworkInterface.getByInetAddress(thisIp);
	//
	// byte[] mac = network.getHardwareAddress();
	//
	// System.out.print("Current MAC address : ");
	//
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < mac.length; i++) {
	// sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" :
	// ""));
	// }
	// } catch (Exception e) {
	// }
	// }

	// protected String getIpAddress() {
	// setIpAdd();
	// return thisIpAddress;
	// }

	public String getNpfRegCliente() {
		return npfRegCliente;
	}

	public void setNpfRegCliente(String npfRegCliente) {
		this.npfRegCliente = npfRegCliente;
	}

	public String getNpfEstado() {
		return npfEstado;
	}

	public void setNpfEstado(String npfEstado) {
		this.npfEstado = npfEstado;
	}

	public String getNpfNomCarrera() {
		return npfNomCarrera;
	}

	public void setNpfNomCarrera(String npfNomCarrera) {
		this.npfNomCarrera = npfNomCarrera;
	}

	public String getNpfNomMateria() {
		return npfNomMateria;
	}

	public void setNpfNomMateria(String npfNomMateria) {
		this.npfNomMateria = npfNomMateria;
	}

	public String getNpfNomParalelo() {
		return npfNomParalelo;
	}

	public void setNpfNomParalelo(String npfNomParalelo) {
		this.npfNomParalelo = npfNomParalelo;
	}

	public List<RolFlujoCarrera> getNpfListRolFlujoCarreraBusq() {
		npfListRolFlujoCarreraBusq = npfListRolFlujoCarreraBusq == null ? (new ArrayList<RolFlujoCarrera>())
				: npfListRolFlujoCarreraBusq;
		return npfListRolFlujoCarreraBusq;
	}

	public void setNpfListRolFlujoCarreraBusq(List<RolFlujoCarrera> npfListRolFlujoCarreraBusq) {
		this.npfListRolFlujoCarreraBusq = npfListRolFlujoCarreraBusq;
	}

	public CronogramaActividadJdbcDto getNpfCronogramaActividadJdbcDtoBuscar() {
		return npfCronogramaActividadJdbcDtoBuscar;
	}

	public void setNpfCronogramaActividadJdbcDtoBuscar(CronogramaActividadJdbcDto npfCronogramaActividadJdbcDtoBuscar) {
		this.npfCronogramaActividadJdbcDtoBuscar = npfCronogramaActividadJdbcDtoBuscar;
	}

	public Dependencia getNpfDependenciaBuscar() {
		return npfDependenciaBuscar;
	}

	public void setNpfDependenciaBuscar(Dependencia npfDependenciaBuscar) {
		this.npfDependenciaBuscar = npfDependenciaBuscar;
	}

	public List<DocenteJdbcDto> getNpffListCarreraDocenteBusq() {
		npffListCarreraDocenteBusq = npffListCarreraDocenteBusq == null ? (new ArrayList<DocenteJdbcDto>())
				: npffListCarreraDocenteBusq;
		return npffListCarreraDocenteBusq;
	}

	public void setNpffListCarreraDocenteBusq(List<DocenteJdbcDto> npffListCarreraDocenteBusq) {
		this.npffListCarreraDocenteBusq = npffListCarreraDocenteBusq;
	}

	public List<ParaleloDto> getNpfListParaleloBusqIndividual() {
		return npfListParaleloBusqIndividual;
	}

	public void setNpfListParaleloBusqIndividual(List<ParaleloDto> npfListParaleloBusqIndividual) {
		this.npfListParaleloBusqIndividual = npfListParaleloBusqIndividual;
	}

	public PeriodoAcademicoDto getNpfPeriodoAcademicoBusq() {
		return npfPeriodoAcademicoBusq;
	}

	public void setNpfPeriodoAcademicoBusq(PeriodoAcademicoDto npfPeriodoAcademicoBusq) {
		this.npfPeriodoAcademicoBusq = npfPeriodoAcademicoBusq;
	}

	public Integer getNpfCrrId() {
		return npfCrrId;
	}

	public void setNpfCrrId(Integer npfCrrId) {
		this.npfCrrId = npfCrrId;
	}

	public List<DocenteJdbcDto> getNpffListNivel() {
		return npffListNivel;
	}

	public void setNpffListNivel(List<DocenteJdbcDto> npffListNivel) {
		this.npffListNivel = npffListNivel;
	}

	public Integer getNpfAsistenciaDocente2() {
		return npfAsistenciaDocente2;
	}

	public void setNpfAsistenciaDocente2(Integer npfAsistenciaDocente2) {
		this.npfAsistenciaDocente2 = npfAsistenciaDocente2;
	}

	public boolean isNpfActivadorGuardar() {
		return npfActivadorGuardar;
	}

	public void setNpfActivadorGuardar(boolean npfActivadorGuardar) {
		this.npfActivadorGuardar = npfActivadorGuardar;
	}

	public List<DocenteJdbcDto> getNpfListMateriaBusqAux() {
		npfListMateriaBusqAux = npfListMateriaBusqAux == null ? (new ArrayList<DocenteJdbcDto>())
				: npfListMateriaBusqAux;
		return npfListMateriaBusqAux;
	}

	public void setNpfListMateriaBusqAux(List<DocenteJdbcDto> npfListMateriaBusqAux) {
		this.npfListMateriaBusqAux = npfListMateriaBusqAux;
	}

	public Carrera getNpfCarreraIngreso() {
		return npfCarreraIngreso;
	}

	public void setNpfCarreraIngreso(Carrera npfCarreraIngreso) {
		this.npfCarreraIngreso = npfCarreraIngreso;
	}

	public Carrera getNpfCarrera() {
		return npfCarrera;
	}

	public void setNpfCarrera(Carrera npfCarrera) {
		this.npfCarrera = npfCarrera;
	}

	public String getNpfTextoCabecera() {
		return npfTextoCabecera;
	}

	public void setNpfTextoCabecera(String npfTextoCabecera) {
		this.npfTextoCabecera = npfTextoCabecera;
	}

	public String getNpfTextoOpcion() {
		return npfTextoOpcion;
	}

	public void setNpfTextoOpcion(String npfTextoOpcion) {
		this.npfTextoOpcion = npfTextoOpcion;
	}

	public Integer getNpfActivadorCohorte() {
		return npfActivadorCohorte;
	}

	public void setNpfActivadorCohorte(Integer npfActivadorCohorte) {
		this.npfActivadorCohorte = npfActivadorCohorte;
	}

	public String getNpfTextoCarrera() {
		return npfTextoCarrera;
	}

	public void setNpfTextoCarrera(String npfTextoCarrera) {
		this.npfTextoCarrera = npfTextoCarrera;
	}

	public boolean isActivadorAsistencia() {
		return activadorAsistencia;
	}

	public void setActivadorAsistencia(boolean activadorAsistencia) {
		this.activadorAsistencia = activadorAsistencia;
	}

	public boolean isNpfActivadorEnLinea() {
		return npfActivadorEnLinea;
	}

	public void setNpfActivadorEnLinea(boolean npfActivadorEnLinea) {
		this.npfActivadorEnLinea = npfActivadorEnLinea;
	}

}
