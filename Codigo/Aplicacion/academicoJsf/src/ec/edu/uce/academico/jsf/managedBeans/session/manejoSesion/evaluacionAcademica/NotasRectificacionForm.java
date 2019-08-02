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
   
 ARCHIVO:     NotasRectificacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja el ingreso de las notas rectificacion por parciales de los docentes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 31-JULIO-2017 			Gabriel Mafla                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLIntegrityConstraintViolationException;
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
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
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
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
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
 * Clase (managed bean) NotaAtrasadaForm. Managed Bean que administra el ingreso
 * de las notas rectificacion por parciales de los docentes.
 * 
 * @author ghmafla.
 * @version 1.0
 */

@ManagedBean(name = "notasRectificacionForm")
@SessionScoped
public class NotasRectificacionForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	// GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario nrctfUsuario;
	private boolean nrctfCarreraMedicinaAnterior;
	// PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto nrctfDocente;
	private PersonaDto nrctfDirCarrera;

	// PARA BUSQUEDA
	private EstudianteJdbcDto nrctfEstudianteBuscar;
	private List<CarreraDto> nrctfListCarreraBusq;
	private List<NivelDto> nrctfListNivelBusq;
	private List<MateriaDto> nrctfListMateriaBusq;
	private List<EstudianteJdbcDto> nrctfListEstudianteBusq;
	private List<EstudianteJdbcDto> nrctfListEstudianteEditar;
	private List<ParaleloDto> nrctfListParaleloBusq;
	private ParaleloDto nrctfParaleloDtoEditar;
	private List<RolFlujoCarrera> nrctfListRolFlujoCarreraBusq;
	private CronogramaActividadJdbcDto nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico;
	private CronogramaActividadJdbcDto nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	private CronogramaActividadJdbcDto nrctfFechaPracCierreFinPresentePeriodoAcademico;
	private CronogramaActividadJdbcDto nrctfFechaPracCierreInicioSigPeriodoAcademico;
	private Dependencia nrctfDependenciaBuscar;
	private List<DocenteJdbcDto> nrctfListCarreraDocenteBusq;
	private List<DocenteJdbcDto> nrctfListNivelxCarreraDocenteBusq;
	private List<DocenteJdbcDto> nrctfListMateriasxCarreraDocenteBusq;

	private List<PeriodoAcademico> nrctfListPeriodoAcademico;
	private PeriodoAcademicoDto nrctfPeriodoAcademicoBusq;
	private PeriodoAcademicoDto nrctfPeriodoAcademicoEdicionAnterior;
	private Materia mtrAux;
	// PARA GUARDAR LA ASISTENCIA DEL DOCENTE DEL FORM
	private Integer nrctfAsistenciaDocente;
	// PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private Integer nrctfValidadorClic;
	private String nrctfEstado;
	private Integer nrctfTipoRectificacion;

	// PARA GUARDAR LOS REGISTROS DE LA SESION DEL CLIENTE HOSTNAME, IPPIBLICA,
	// IPPRIVADA
	private String nrctfRegCliente;
	// private String thisIpAddress;
	// campos para el envio de la notificacion del ingreso final de notas al
	// mail del docente
	private String nrctfNomCarrera;
	private String nrctfNomMateria;
	private String nrctfNomParalelo;
	private String nrctfNomNivel;
	private String nrctfNomNotaRectificacion;

	private Integer nrctfTipoNota;
	// PARA CARGA DE ARCHIVO
	private String nrctfNombreArchivoAuxiliar;
	private String nrctfNombreArchivoSubido;
	private boolean opModulo;
	private PeriodoAcademico pracCierre;
	private Materia mtrModulo;
	private boolean permisoEdicionPeridoAnterior;

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
	private CarreraDtoServicioJdbc servNrctfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servNrctfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servNrctfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servNrctfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servNrctfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servNrctfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servNrctfParaleloDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servNrctfNotasPregradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servNrctfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNrctfMateriaDto;
	@EJB
	private CalificacionServicio servNrctfCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servNpfRolFlujoCarreraServicio;
	@EJB
	private UsuarioRolServicio servNpfUsuarioRolServicio;
	@EJB
	private RecordEstudianteServicio servNpfRecordEstudianteServicio;
	@EJB
	private DependenciaServicio servNrctfDependenciaServicio;
	@EJB
	private CronogramaActividadDtoServicioJdbc servNrctfCronogramaActividadDtoServicioJdbcServicio;

	@EJB
	private PersonaDtoServicioJdbc servNrctfPersonaDtoServicioJdbc;

	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;

	@EJB
	private PeriodoAcademicoServicio servNrctfPeriodoAcademicoServicio;
	@EJB
	private CarreraServicio servNrctfCarreraServicio;

	@EJB
	private MallaCurricularParaleloDtoServicioJdbc servNrctfMallaCurricularParaleloDtoServicioJdbc;

	@EJB
	private MallaCurricularParaleloServicio servMallaCurricularParaleloServicio;
	@EJB
	private MallaCurricularMateriaServicio servMallaCurricularMateriaServicio;

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/
	// PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irListarParalelosXMateriaXCarrera(Usuario usuario) {
		nrctfUsuario = usuario;
		String retorno = null;
		permisoEdicionPeridoAnterior = false;
		nrctfCarreraMedicinaAnterior = false;
		try {
			// INICIO PARAMETROS DE BUSQUEDA

			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(nrctfUsuario.getUsrId(),
					RolConstantes.ROL_DOCENTE_VALUE);
			if (usro.getUsroEstado().intValue() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}

			iniciarParametros();
			// LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = sdf.parse("2019-07-25");
				Date date2 = sdf.parse("2019-07-27");
				Date fechaActual = new Date();
				if (fechaActual.compareTo(date2) > 0 || fechaActual.compareTo(date1) < 0) {

				} else {
					if (nrctfUsuario.getUsrNick().equals("crestrella")
							|| nrctfUsuario.getUsrNick().equals("amchavez")) {
						permisoEdicionPeridoAnterior = true;
					}
				}
			} catch (ParseException e1) {
			}
			if (!permisoEdicionPeridoAnterior) {
				nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc
						.buscarCarrerasDocenteXPracEstadoActivoCierre(nrctfUsuario.getUsrId());
			}

			nrctfListParaleloBusq = null;

			// try {
			// nrctfPeriodoAcademicoBusq =
			// servNrctfPeriodoAcademicoDtoServicioJdbc.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			// } catch (Exception e) {
			nrctfPeriodoAcademicoBusq = servNrctfPeriodoAcademicoDtoServicioJdbc
					.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			// }

			/*********************************************************************/
			/***********
			 * PARA ARREGLAR
			 *******************************************/
			/*********************************************************************/
			// BUSCO EL DOCENTE PARA LAS MATERIAS

			if (permisoEdicionPeridoAnterior) {
				nrctfListCarreraDocenteBusq = new ArrayList<DocenteJdbcDto>();
				nrctfListPeriodoAcademico = new ArrayList<PeriodoAcademico>();
				try {
					nrctfListPeriodoAcademico.add(servNrctfPeriodoAcademicoServicio.buscarPorId(150));
					nrctfPeriodoAcademicoBusq=servNrctfPeriodoAcademicoDtoServicioJdbc.buscar(150);
					nrctfPeriodoAcademicoEdicionAnterior=servNrctfPeriodoAcademicoDtoServicioJdbc.buscar(150);
					// nrctfListPeriodoAcademico.add(servNrctfPeriodoAcademicoServicio.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE,
					// PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE));
				} catch (Exception e) {
					// try {
					// nrctfListPeriodoAcademico.add(servNrctfPeriodoAcademicoServicio.buscarPorId(350));
					// nrctfListPeriodoAcademico.add(servNrctfPeriodoAcademicoServicio.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE,
					// PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE));
					//
					// } catch (Exception e1) {
					// }
				}
			} else {
				if (nrctfPeriodoAcademicoBusq.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE) {
					nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteInactivoXUsuarioXCarreraXPrac(
							nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
							TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());
					Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
						}
					});
				} else {

					nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(
							nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
							TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());
					Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
						}
					});

				}
			}
			retorno = "irListarParalelosXMateriaXCarrera";
		} catch (PeriodoAcademicoDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e1) {
			e1.printStackTrace();
			FacesUtil.mensajeError(e1.getMessage());
		} catch (UsuarioRolException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}

	public String irListarParalelosXMateriaXCarreraMedicinaPeriodoAnterior(Usuario usuario) {
		nrctfUsuario = usuario;
		nrctfCarreraMedicinaAnterior = true;
		String retorno = null;
		permisoEdicionPeridoAnterior = false;
		try {
			// INICIO PARAMETROS DE BUSQUEDA

			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(nrctfUsuario.getUsrId(),
					RolConstantes.ROL_DOCENTE_VALUE);
			if (usro.getUsroEstado().intValue() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}

			iniciarParametros();
			// LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc
					.buscarCarrerasDocenteXPracIdMedicinaAnterior(nrctfUsuario.getUsrId(), 350);
			boolean op = false;
			for (DocenteJdbcDto item : nrctfListCarreraDocenteBusq) {
				if (item.getCrrId() == 82 || item.getCrrId() == 157) {
					op = true;
				}
			}
			if (!op) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta opción solo se encuentra habilitada para la carrera de Medicina.");

				return null;
			}
			nrctfListParaleloBusq = null;

			try {
				nrctfPeriodoAcademicoBusq = servNrctfPeriodoAcademicoDtoServicioJdbc.buscar(350);
			} catch (Exception e) {
				return null;
			}

			/*********************************************************************/
			/***********
			 * PARA ARREGLAR
			 *******************************************/
			/*********************************************************************/
			// BUSCO EL DOCENTE PARA LAS MATERIAS

			nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteInactivoXUsuarioXCarreraXPrac(
					nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE,
					nrctfPeriodoAcademicoBusq.getPracId());
			Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
				public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
				}
			});

			Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
				public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
				}
			});

			retorno = "irListarParalelosXMateriaXCarrera";
		} catch (CarreraDtoJdbcNoEncontradoException e1) {
			e1.printStackTrace();
		} catch (UsuarioRolException e) {
			e.printStackTrace();
		} catch (UsuarioRolNoEncontradoException e) {
			e.printStackTrace();
		} catch (DetallePuestoDtoJdbcException e) {
			e.printStackTrace();
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
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

	public void llenarCarreraEdicionPeriodosAnteriores() {
		try {
			nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc
					.buscarCarrerasDocenteInactivoXPracId(nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getPracId());
			nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteInactivoXUsuarioXCarreraXPrac(
					nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE,
					nrctfEstudianteBuscar.getPracId());

			Collections.sort(nrctfListCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
				public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
					return obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
				}
			});
			// nrctfDocente =
			// servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(nrctfUsuario.getUsrId(),
			// nrctfEstudianteBuscar.getCrrId(),
			// TipoPuestoConstantes.TIPO_DOCENTE_VALUE,
			// nrctfEstudianteBuscar.getPracId());
			// Collections.sort(nrctfListCarreraDocenteBusq, new
			// Comparator<DocenteJdbcDto>() {
			// public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
			// return
			// obj1.getCrrDescripcion().compareTo(obj2.getCrrDescripcion());
			// }
			// });

		} catch (Exception e) {
			try {
				nrctfListCarreraDocenteBusq = servDocenteDtoServicioJdbc
						.buscarCarrerasDocenteXPracId(nrctfUsuario.getUsrId(), 350);
				nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(
						nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
						TipoPuestoConstantes.TIPO_DOCENTE_VALUE, 350);
			} catch (Exception e2) {
			}
		}

	}

	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * 
	 * @param idCarrera
	 *            - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera) {
		// idCarrera = nrctfEstudianteBuscar.getCrrId();
		nrctfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		nrctfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		nrctfListParaleloBusq = null;
		nrctfListNivelxCarreraDocenteBusq = null;
		nrctfListMateriasxCarreraDocenteBusq = null;
		if (permisoEdicionPeridoAnterior) {
			try {
				if (idCarrera != GeneralesConstantes.APP_ID_BASE) {

					if (permisoEdicionPeridoAnterior) {
						// LISTO LOS NIVELES
						try {
							nrctfListNivelxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
									.buscarNivelesDocenteInactivoXCarreraXPracId(nrctfUsuario.getUsrId(),
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getPracId());
						} catch (NivelDtoJdbcNoEncontradoException e) {
							try {
								nrctfListNivelxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
										.buscarNivelesDocenteXCarreraXPracId(nrctfUsuario.getUsrId(),
												nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getPracId());
							} catch (Exception e2) {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(
										"No se encontraron niveles asignados al docente en la carrera seleccionada.");
							}

						}

						Collections.sort(nrctfListNivelxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
							public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
								return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
							}
						});

						// nrctfDocente =
						// servNrctfDocenteDtoServicioJdbc.buscarDocenteInactivoXUsuarioXCarreraXPrac(nrctfUsuario.getUsrId(),
						// nrctfEstudianteBuscar.getCrrId(),
						// TipoPuestoConstantes.TIPO_DOCENTE_VALUE,
						// nrctfEstudianteBuscar.getPracId());

					} else {
						// LISTO LOS NIVELES
						try {
							nrctfListNivelxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
									.buscarNivelesDocenteXCarreraXPracId(nrctfUsuario.getUsrId(),
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getPracId());
						} catch (NivelDtoJdbcNoEncontradoException e) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(
									"No se encontraron niveles asignados al docente en la carrera seleccionada.");
						}

						Collections.sort(nrctfListNivelxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
							public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
								return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
							}
						});

						nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(
								nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
								TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfEstudianteBuscar.getPracId());

					}

					//
				} else {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
							"Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
				}
			} catch (Exception e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
						"Rectificacion.Notas.Docente.Estudiante.Docente.llenarNivel.exception")));
			}
		} else {
			if (nrctfCarreraMedicinaAnterior) {
				try {
					if (idCarrera != GeneralesConstantes.APP_ID_BASE) {

						// LISTO LOS NIVELES
						try {
							nrctfListNivelxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
									.buscarNivelesDocenteInactivoXCarrera(nrctfUsuario.getUsrId(),
											nrctfEstudianteBuscar.getCrrId());
						} catch (NivelDtoJdbcNoEncontradoException e) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(
									"No se encontraron niveles asignados al docente en la carrera seleccionada.");
						}

						Collections.sort(nrctfListNivelxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
							public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
								return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
							}
						});
						nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteInactivoXUsuarioXCarreraXPrac(
								nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
								TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());
						Iterator<DocenteJdbcDto> it = nrctfListNivelxCarreraDocenteBusq.iterator();
						while (it.hasNext()) {
							DocenteJdbcDto i = it.next();
							// elimino si no son DECIMO PRIMERO
							if (i.getNvlId() != 12) {
								it.remove();
							}
						}
						if (nrctfListNivelxCarreraDocenteBusq.size() == 0) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(
									"No se permite le rectificación en niveles inferiores a Décimo primero.");
						}
						//
					} else {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
								"Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
					}
				} catch (Exception e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
							"Rectificacion.Notas.Docente.Estudiante.Docente.llenarNivel.exception")));
				}
			} else {
				try {
					if (idCarrera != GeneralesConstantes.APP_ID_BASE) {

						if (nrctfPeriodoAcademicoBusq
								.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE) {
							// LISTO LOS NIVELES
							try {
								nrctfListNivelxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
										.buscarNivelesDocenteInactivoXCarrera(nrctfUsuario.getUsrId(),
												nrctfEstudianteBuscar.getCrrId());
							} catch (NivelDtoJdbcNoEncontradoException e) {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(
										"No se encontraron niveles asignados al docente en la carrera seleccionada.");
							}

							Collections.sort(nrctfListNivelxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
								public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
									return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
								}
							});
							nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteInactivoXUsuarioXCarreraXPrac(
									nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
									TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());

						} else {
							// LISTO LOS NIVELES
							try {
								nrctfListNivelxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
										.buscarNivelesDocenteXCarrera(nrctfUsuario.getUsrId(),
												nrctfEstudianteBuscar.getCrrId());
							} catch (NivelDtoJdbcNoEncontradoException e) {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(
										"No se encontraron niveles asignados al docente en la carrera seleccionada.");
							}

							Collections.sort(nrctfListNivelxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
								public int compare(DocenteJdbcDto p1, DocenteJdbcDto p2) {
									return new Integer(p1.getNvlId()).compareTo(new Integer(p2.getNvlId()));
								}
							});
							nrctfDocente = servNrctfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPrac(
									nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getCrrId(),
									TipoPuestoConstantes.TIPO_DOCENTE_VALUE, nrctfPeriodoAcademicoBusq.getPracId());

						}

						//
					} else {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
								"Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
					}
				} catch (Exception e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
							"Rectificacion.Notas.Docente.Estudiante.Docente.llenarNivel.exception")));
				}
			}

		}

	}

	/**
	 * Método que permite buscar la lista de materias por el por el id de
	 * paralelo
	 * 
	 * @param idParalelo
	 *            - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idNivel, int carreraId) {
		carreraId = nrctfEstudianteBuscar.getCrrId();
		idNivel = nrctfEstudianteBuscar.getNvlId();
		nrctfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		nrctfListParaleloBusq = null;
		nrctfListMateriasxCarreraDocenteBusq = null;
		if (permisoEdicionPeridoAnterior) {
			try {
				if (idNivel != GeneralesConstantes.APP_ID_BASE) {
					nrctfListMateriasxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
							.buscarMateriasDocenteInactivoXCarreraXNivelXPracId(nrctfUsuario.getUsrId(),
									nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
									nrctfEstudianteBuscar.getPracId());

					Collections.sort(nrctfListMateriasxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
						public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
							return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
						}
					});
				} else {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
							"Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
						"Rectificacion.Notas.Docente.Estudiante.Docente.llenarMateria.exception")));
			}
		} else {
			if (nrctfCarreraMedicinaAnterior) {
				try {
					if (idNivel != GeneralesConstantes.APP_ID_BASE) {
						nrctfListMateriasxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
								.buscarMateriasDocenteInactivoXCarreraXNivel(nrctfUsuario.getUsrId(),
										nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId());

						Collections.sort(nrctfListMateriasxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
							public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
								return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
							}
						});

					} else {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
								"Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
					}
				} catch (Exception e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
							"Rectificacion.Notas.Docente.Estudiante.Docente.llenarMateria.exception")));
				}
			} else {
				try {
					if (idNivel != GeneralesConstantes.APP_ID_BASE) {
						if (nrctfPeriodoAcademicoBusq
								.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE) {
							nrctfListMateriasxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
									.buscarMateriasDocenteInactivoXCarreraXNivel(nrctfUsuario.getUsrId(),
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId());

							Collections.sort(nrctfListMateriasxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
								public int compare(DocenteJdbcDto obj1, DocenteJdbcDto obj2) {
									return obj1.getMtrDescripcion().compareTo(obj2.getMtrDescripcion());
								}
							});
						} else {
							nrctfListMateriasxCarreraDocenteBusq = servNrctfDocenteDtoServicioJdbc
									.buscarMateriasDocenteXCarreraXNivel(nrctfUsuario.getUsrId(),
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId());

							Collections.sort(nrctfListMateriasxCarreraDocenteBusq, new Comparator<DocenteJdbcDto>() {
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
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
							"Rectificacion.Notas.Docente.Estudiante.Docente.llenarMateria.exception")));
				}
			}
		}
	}

	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscar() {
		// anulo la lista de estudiantes
		nrctfListParaleloBusq = null;
		try {
			if (nrctfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			} else if (nrctfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			} else if (nrctfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			} else {

				nrctfListParaleloBusq = new ArrayList<>();

				List<ParaleloDto> listNoCompartida = new ArrayList<>();
				if (permisoEdicionPeridoAnterior) {
					listNoCompartida = servNrctfParaleloDtoServicioJdbc
							.listarParalelosXcarreraXnivelXdocenteInactivoXmateriaNoComp(
									nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
									nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId(),
									nrctfEstudianteBuscar.getPracId());
					List<ParaleloDto> listCompartida = new ArrayList<>();
					try {
						listCompartida = servNrctfParaleloDtoServicioJdbc
								.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteInactivoXMateriaComp(
										nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
										nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId(),
										nrctfEstudianteBuscar.getPracId());
						if (listCompartida.size() != 0) {

							for (ParaleloDto item : listCompartida) {
								nrctfListParaleloBusq.add(item);
							}
						}
					} catch (Exception e) {
					}
					// asignación a una sola lista
					for (ParaleloDto item : listNoCompartida) {
						nrctfListParaleloBusq.add(item);
					}
				} else {
					if (nrctfCarreraMedicinaAnterior) {
						nrctfPeriodoAcademicoBusq = servNrctfPeriodoAcademicoDtoServicioJdbc.buscar(150);
						listNoCompartida = servNrctfParaleloDtoServicioJdbc
								.listarParalelosXcarreraXnivelXdocenteInactivoXmateriaNoComp(
										nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
										nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId(),
										nrctfPeriodoAcademicoBusq.getPracId());
						List<ParaleloDto> listCompartida = new ArrayList<>();
						try {
							listCompartida = servNrctfParaleloDtoServicioJdbc
									.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteInactivoXMateriaComp(
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
											nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId(),
											nrctfPeriodoAcademicoBusq.getPracId());
							if (listCompartida.size() != 0) {

								for (ParaleloDto item : listCompartida) {
									nrctfListParaleloBusq.add(item);
								}
							}
						} catch (Exception e) {
						}
						// asignación a una sola lista
						for (ParaleloDto item : listNoCompartida) {
							nrctfListParaleloBusq.add(item);
						}
					} else {
						if (nrctfPeriodoAcademicoBusq
								.getPracEstado() == PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE) {
							listNoCompartida = servNrctfParaleloDtoServicioJdbc
									.listarParalelosXcarreraXnivelXdocenteInactivoXmateriaNoComp(
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
											nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId(),
											nrctfPeriodoAcademicoBusq.getPracId());
							List<ParaleloDto> listCompartida = new ArrayList<>();
							try {
								listCompartida = servNrctfParaleloDtoServicioJdbc
										.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteInactivoXMateriaComp(
												nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
												nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId(),
												nrctfPeriodoAcademicoBusq.getPracId());
								if (listCompartida.size() != 0) {

									for (ParaleloDto item : listCompartida) {
										nrctfListParaleloBusq.add(item);
									}
								}
							} catch (Exception e) {
							}
							// asignación a una sola lista
							for (ParaleloDto item : listNoCompartida) {
								nrctfListParaleloBusq.add(item);
							}
						} else {
							listNoCompartida = servNrctfParaleloDtoServicioJdbc
									.listarParalelosXcarreraXnivelXdocenteXmateriaNoComp(
											nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
											nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId());
							List<ParaleloDto> listCompartida = new ArrayList<>();
							try {
								listCompartida = servNrctfParaleloDtoServicioJdbc
										.listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaComp(
												nrctfEstudianteBuscar.getCrrId(), nrctfEstudianteBuscar.getNvlId(),
												nrctfUsuario.getUsrId(), nrctfEstudianteBuscar.getMtrId());
								if (listCompartida.size() != 0) {

									for (ParaleloDto item : listCompartida) {
										nrctfListParaleloBusq.add(item);
									}
								}
							} catch (Exception e) {
							}
							// asignación a una sola lista
							for (ParaleloDto item : listNoCompartida) {
								nrctfListParaleloBusq.add(item);
							}
						}
					}
				}
			}
			Iterator<ParaleloDto> it = nrctfListParaleloBusq.iterator();
			while (it.hasNext()) {
				ParaleloDto item = it.next();
				int contador = 0;
				Iterator<ParaleloDto> it2 = nrctfListParaleloBusq.iterator();
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
			Collections.sort(nrctfListParaleloBusq, new Comparator<ParaleloDto>() {
				public int compare(ParaleloDto obj1, ParaleloDto obj2) {
					return new Integer(obj1.getPrlId()).compareTo(new Integer(obj2.getPrlId()));
				}
			});
		} catch (Exception e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
					new MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.Estudiante.Docente.buscar.exception")));
		}
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

	// Suma los días recibidos a la fecha
	public Date sumarRestarDiasFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o
													// restar en caso de días<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos
									// días añadidos
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas primer parcial
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificar1Parcial(ParaleloDto prl) {
		// ParaleloDto prlAux = new ParaleloDto();
		// prlAux = prl;
		nrctfParaleloDtoEditar = new ParaleloDto();
		nrctfParaleloDtoEditar = prl;
		nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		nrctfDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			nrctfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: RECTIFICAR NOTAS 1");
			StringBuilder sb = new StringBuilder();
			sb.append(nrctfRegCliente);
			sb.append("|");

			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");
				} catch (Exception e) {
				}
			}
			Carrera crrAux = new Carrera();
			try {
				crrAux = servNrctfCarreraServicio.buscarPorId(nrctfParaleloDtoEditar.getCrrId());
			} catch (CarreraNoEncontradoException | CarreraException e) {
			}
			nrctfDependenciaBuscar = crrAux.getCrrDependencia();
			mtrAux = servNrctfMateriaDto.buscarPorId(nrctfParaleloDtoEditar.getMtrId());
			if (permisoEdicionPeridoAnterior) {

				nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
				// //IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE ESTUDIANTES
				// SI COMPARTEN O NO COMPARTEN
				nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXParaleloXpracId(
						nrctfParaleloDtoEditar.getPrlId(), nrctfParaleloDtoEditar.getMlcrprId(),
						nrctfEstudianteBuscar.getPracId());

			} else {

				if (nrctfCarreraMedicinaAnterior) {

					nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					// //IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE
					// ESTUDIANTES SI COMPARTEN O NO COMPARTEN
					nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXParaleloInactivoMedicina(nrctfParaleloDtoEditar.getPrlId(),
									nrctfParaleloDtoEditar.getMlcrprId());

				} else {
					pracCierre = null;
					try {
						pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
					} catch (Exception e) {
					}

					if (pracCierre != null) {
						if (nrctfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						} else {
							if (crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							} else {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							}
						}
					} else {
						if (nrctfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						} else {
							if (crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							} else {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							}

						}

					}
					Date myDate = new Date(new Date().getTime());
					Date siguientePracDate = new Date(
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio()
									.getTime());
					Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
					Date finIngresoNota = new Date(nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico
							.getPlcrFechaFin().getTime());
					if (myDate.after(maxDate)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
						return null;
					}

					if (myDate.before(finIngresoNota)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está antes de las fechas establecidas.");
						return null;
					}

					if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
						List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

						try {
							Integer mlcrprAuxId = servNrctfMallaCurricularParaleloDtoServicioJdbc
									.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
											nrctfDocente.getPrsIdentificacion(),
											nrctfParaleloDtoEditar.getMlcrmtNvlId(), nrctfParaleloDtoEditar.getPrlId());
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
							mtrModulo = mtrAux;

							listaprueba = servNrctfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
											nrctfParaleloDtoEditar.getCrrId(), nrctfParaleloDtoEditar.getMlcrmtNvlId(),
											mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
											nrctfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
											nrctfParaleloDtoEditar.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
									item.setClfNota1(null);
									item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante1(null);
									item.setClfNota1(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota1(null);
										item.setClfAsistenciaEstudiante1(null);
										item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
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
									nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
								}
							}
						} catch (Exception e) {
							try {
								listaprueba = new ArrayList<EstudianteJdbcDto>();
								try {
									MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
									mlcrprAux = servMallaCurricularParaleloServicio
											.buscarPorId(nrctfParaleloDtoEditar.getMlcrprId());
									MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
									mlcrmtAux = servMallaCurricularMateriaServicio
											.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
									mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
									mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
									mlcrmtAux = servMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(
											mtrAux.getMtrId(),
											MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
									mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(
											mlcrmtAux.getMlcrmtId(), nrctfParaleloDtoEditar.getPrlId());
									mtrModulo = mtrAux;

									listaprueba = servNrctfNotasPregradoDtoServicioJdbc
											.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

									List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
									for (EstudianteJdbcDto item : listaprueba) {
										item.setMateriaModulo(true);
										// En caso de que no existe el resultado
										// de búsqueda
										if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
											item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
											item.setClfNota1(null);
											item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
											item.setClfAsistenciaEstudiante1(null);
											item.setClfNota1(null);
											npfListEstudianteBusqPrueba.add(item);
										} else {
											// En caso de que sean la misma
											// malla_curricula_paralelo
											if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId()
													.intValue()) {
												npfListEstudianteBusqPrueba.add(item);
											} else {
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota1(null);
												item.setClfAsistenciaEstudiante1(null);
												item.setClfAsistenciaDocente1(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
											}
										}
									}
									for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
										for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

											if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
													.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
													&& (npfListEstudianteBusqPrueba.get(i)
															.getClfId() == npfListEstudianteBusqPrueba.get(j)
																	.getClfId())) {
												npfListEstudianteBusqPrueba.remove(j);
												j--;
											}
										}
									}

									for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
										boolean op = true;
										for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
											if (i != j) {
												if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(
														npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
													if (npfListEstudianteBusqPrueba.get(i)
															.getClfId() == GeneralesConstantes.APP_ID_BASE) {
														op = false;
													}
												}
											}
										}
										if (op) {
											nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
										}
									}
								} catch (Exception ex) {
								}

							} catch (Exception ex) {
							}
						}

					} else {
						nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
						// //IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE
						// ESTUDIANTES SI COMPARTEN O NO COMPARTEN
						nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
								nrctfParaleloDtoEditar.getPrlId(), nrctfParaleloDtoEditar.getMlcrprId());
					}
				}

			}

			for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
				try {
					item.setClfNota1String(item.getClfNota1().toString());
					nrctfAsistenciaDocente = item.getClfAsistenciaDocente1();
				} catch (Exception e) {
				}
			}
			nrctfEstudianteBuscar.setMtrId(nrctfParaleloDtoEditar.getMtrId());
			for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
				if ((item.getClfAsistenciaDocente1() != null)) {
					if (item.getClfAsistenciaDocente1() != 0) {
						nrctfAsistenciaDocente = item.getClfAsistenciaDocente1();
						break;
					}

				} else {
					nrctfAsistenciaDocente = null;
				}

			}

			nrctfDirCarrera = new PersonaDto();
			if (nrctfDependenciaBuscar.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
				if (nrctfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					nrctfDirCarrera.setPrsIdentificacion("1715984959");
					nrctfDirCarrera.setPrsPrimerApellido("CARTAGENA");
					nrctfDirCarrera.setPrsSegundoApellido("ALBARRACIN");
					nrctfDirCarrera.setPrsNombres("JUAN CARLOS");
					nrctfDirCarrera.setPrsMailInstitucional("jcalbarracin@uce.edu.ec");
					nrctfDirCarrera.setUsrNick("jcalbarracin");
				} else {
					nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(
							RolConstantes.ROL_DIRCARRERA_VALUE, nrctfParaleloDtoEditar.getCrrId());
				}

			} else {
				nrctfDirCarrera.setPrsIdentificacion("1713271896");
				nrctfDirCarrera.setPrsPrimerApellido("LARA");
				nrctfDirCarrera.setPrsSegundoApellido("REIMUNDO");
				nrctfDirCarrera.setPrsNombres("JOSE JULIO");
				nrctfDirCarrera.setPrsMailInstitucional("jjlarar@uce.edu.ec");
				nrctfDirCarrera.setUsrNick("jjlarar");
			}
			nrctfTipoRectificacion = new Integer(1);
			if (nrctfListEstudianteBusq.isEmpty()) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(
						"No existen estudiantes en el paralelo para rectificar notas del 1er hemisemestre.");
				return null;
			} else {
				nrctfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(nrctfListEstudianteBusq);
			}
			return "irRectificar1Parcial";
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar1Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar1Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));

		} catch (MateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al cargar el paralelo, por favor revise su carga horaria.");
		}
		return null;
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas segundo parcial
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificar2Parcial(ParaleloDto prl) {
		nrctfParaleloDtoEditar = new ParaleloDto();
		nrctfParaleloDtoEditar = prl;
		nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		nrctfDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			String idHostAux = new String();
			String ipLocalClienteAux = new String();
			// String ipPublicaClienteAux = new String();
			idHostAux = datosCliente.get(0);
			ipLocalClienteAux = datosCliente.get(1);
			// ipPublicaClienteAux=datosCliente.get(6);
			// nrctfRegCliente = "ID
			// SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|"
			// + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			nrctfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: MODIFICA PARCIAL 2");
			Carrera crrAux = new Carrera();
			try {
				crrAux = servNrctfCarreraServicio.buscarPorId(nrctfParaleloDtoEditar.getCrrId());
			} catch (CarreraNoEncontradoException | CarreraException e) {
			}
			nrctfDependenciaBuscar = crrAux.getCrrDependencia();
			mtrAux = servNrctfMateriaDto.buscarPorId(nrctfParaleloDtoEditar.getMtrId());
			if (permisoEdicionPeridoAnterior) {
				nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
				nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXParaleloXpracId(
						nrctfParaleloDtoEditar.getPrlId(), nrctfParaleloDtoEditar.getMlcrprId(),
						nrctfEstudianteBuscar.getPracId());
			} else {
				if (nrctfCarreraMedicinaAnterior) {

					nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					// //IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE
					// ESTUDIANTES SI COMPARTEN O NO COMPARTEN
					nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXParaleloInactivoMedicina(nrctfParaleloDtoEditar.getPrlId(),
									nrctfParaleloDtoEditar.getMlcrprId());

				} else {
					PeriodoAcademico pracCierre = null;
					try {
						pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
					} catch (Exception e) {
					}
					if (pracCierre != null) {
						if (nrctfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						} else {
							if (crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							} else {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							}
						}
					} else {
						if (nrctfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						} else {
							if (crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
									|| crrAux.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							} else {
								nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
								nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
										.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
												CronogramaConstantes.TIPO_ACADEMICO_VALUE,
												ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
							}

						}

					}
					Date myDate = new Date(new Date().getTime());
					Date siguientePracDate = new Date(
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio()
									.getTime());
					Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
					Date finIngresoNota = new Date(nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico
							.getPlcrFechaFin().getTime());
					if (myDate.after(maxDate)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
						return null;
					}
					if (myDate.before(finIngresoNota)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está antes de las fechas establecidas.");
						return null;
					}

					if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {

						List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

						try {
							Integer mlcrprAuxId = servNrctfMallaCurricularParaleloDtoServicioJdbc
									.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
											nrctfDocente.getPrsIdentificacion(),
											nrctfParaleloDtoEditar.getMlcrmtNvlId(), nrctfParaleloDtoEditar.getPrlId());
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
							mtrModulo = mtrAux;

							listaprueba = servNrctfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
											nrctfParaleloDtoEditar.getCrrId(), nrctfParaleloDtoEditar.getMlcrmtNvlId(),
											mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
											nrctfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
											nrctfParaleloDtoEditar.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
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
									nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
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
									mlcrprAux = servMallaCurricularParaleloServicio
											.buscarPorId(nrctfParaleloDtoEditar.getMlcrprId());
									MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
									mlcrmtAux = servMallaCurricularMateriaServicio
											.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
									mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
									mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
									mlcrmtAux = servMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(
											mtrAux.getMtrId(),
											MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
									mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(
											mlcrmtAux.getMlcrmtId(), nrctfParaleloDtoEditar.getPrlId());
									mtrModulo = mtrAux;

									listaprueba = servNrctfNotasPregradoDtoServicioJdbc
											.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

									List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
									for (EstudianteJdbcDto item : listaprueba) {
										item.setMateriaModulo(true);
										// En caso de que no existe el resultado
										// de búsqueda
										if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
											item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
											item.setClfNota2(null);
											item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
											item.setClfAsistenciaEstudiante2(null);
											item.setClfNota2(null);
											npfListEstudianteBusqPrueba.add(item);
										} else {
											// En caso de que sean la misma
											// malla_curricula_paralelo
											if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId()
													.intValue()) {
												npfListEstudianteBusqPrueba.add(item);
											} else {
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
											}
										}
									}
									for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
										for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

											if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
													.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
													&& (npfListEstudianteBusqPrueba.get(i)
															.getClfId() == npfListEstudianteBusqPrueba.get(j)
																	.getClfId())) {
												npfListEstudianteBusqPrueba.remove(j);
												j--;
											}
										}
									}

									for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
										boolean op = true;
										for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
											if (i != j) {
												if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(
														npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
													if (npfListEstudianteBusqPrueba.get(i)
															.getClfId() == GeneralesConstantes.APP_ID_BASE) {
														op = false;
													}
												}
											}
										}
										if (op) {
											nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
										}
									}
								} catch (Exception ex) {
								}

							} catch (Exception ex) {
							}
						}

					} else {
						nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
						nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
								nrctfParaleloDtoEditar.getPrlId(), nrctfParaleloDtoEditar.getMlcrprId());
					}
				}

			}

			for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
				if (item.getClfAsistenciaDocente2() != null) {
					if (item.getClfAsistenciaDocente2() != 0) {
						nrctfAsistenciaDocente = item.getClfAsistenciaDocente2();
						break;
					}
				} else {
					nrctfAsistenciaDocente = null;
				}
			}
			nrctfEstudianteBuscar.setMtrId(nrctfParaleloDtoEditar.getMtrId());
			nrctfDirCarrera = new PersonaDto();
			if (nrctfDependenciaBuscar.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
				if (nrctfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					nrctfDirCarrera.setPrsIdentificacion("1715984959");
					nrctfDirCarrera.setPrsPrimerApellido("CARTAGENA");
					nrctfDirCarrera.setPrsSegundoApellido("ALBARRACIN");
					nrctfDirCarrera.setPrsNombres("JUAN CARLOS");
					nrctfDirCarrera.setPrsMailInstitucional("jcalbarracin@uce.edu.ec");
					nrctfDirCarrera.setUsrNick("jcalbarracin");
				} else {
					nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(
							RolConstantes.ROL_DIRCARRERA_VALUE, nrctfParaleloDtoEditar.getCrrId());
				}

			} else {
				nrctfDirCarrera.setPrsIdentificacion("1713271896");
				nrctfDirCarrera.setPrsPrimerApellido("LARA");
				nrctfDirCarrera.setPrsSegundoApellido("REIMUNDO");
				nrctfDirCarrera.setPrsNombres("JOSE JULIO");
				nrctfDirCarrera.setPrsMailInstitucional("jjlarar@uce.edu.ec");
				nrctfDirCarrera.setUsrNick("jjlarar");
			}
			nrctfTipoRectificacion = new Integer(2);
			if (nrctfListEstudianteBusq.isEmpty()) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(
						"No existen estudiantes en el paralelo para rectificar notas de 2do Hemisemestre.");
				return null;
			} else {
				nrctfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(nrctfListEstudianteBusq);
			}

			return "irRectificar2Parcial";

		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar2Parcial.CronogramaActividadDtoJdbc.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar2Parcial.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar2Parcial.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar2Parcial.EstudianteDtoJdbcc.no.encontrado.exception")));
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificar2Parcial.PersonaDto.no.encontrado.exception")));

		} catch (MateriaNoEncontradoException e1) {
		} catch (MateriaException e1) {
		}
		return null;
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas segundo parcial
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificarRecuperacion(ParaleloDto prl) {
		nrctfParaleloDtoEditar = new ParaleloDto();
		nrctfParaleloDtoEditar = prl;
		nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		nrctfDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			String idHostAux = new String();
			String ipLocalClienteAux = new String();
			// String ipPublicaClienteAux = new String();
			idHostAux = datosCliente.get(0);
			ipLocalClienteAux = datosCliente.get(1);
			// ipPublicaClienteAux=datosCliente.get(6);
			// nrctfRegCliente = "ID
			// SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|"
			// + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			nrctfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: MODIFICA RECUPERACION");
			nrctfDependenciaBuscar = servNrctfDependenciaServicio
					.buscarDependenciaXcrrId(nrctfParaleloDtoEditar.getCrrId());
			mtrAux = servNrctfMateriaDto.buscarPorId(nrctfParaleloDtoEditar.getMtrId());
			if (permisoEdicionPeridoAnterior) {

				if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
					List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

					try {
						Integer mlcrprAuxId = servNrctfMallaCurricularParaleloDtoServicioJdbc
								.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
										nrctfDocente.getPrsIdentificacion(), nrctfParaleloDtoEditar.getMlcrmtNvlId(),
										nrctfParaleloDtoEditar.getPrlId());
						MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
						mlcrprAux = servMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
						mtrModulo = mtrAux;

						listaprueba = servNrctfNotasPregradoDtoServicioJdbc
								.buscarEstudiantesModularXPeriodoAnterior(
										nrctfParaleloDtoEditar.getCrrId(), nrctfParaleloDtoEditar.getMlcrmtNvlId(),
										mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
										nrctfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
										nrctfParaleloDtoEditar.getMlcrprId(),nrctfEstudianteBuscar.getPracId());

						List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
						for (EstudianteJdbcDto item : listaprueba) {
							item.setMateriaModulo(true);
							// En caso de que no existe el resultado de búsqueda
							if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
								item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
								item.setClfNota2(null);
								item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
								item.setClfAsistenciaEstudiante2(null);
								item.setClfNota2(null);
								npfListEstudianteBusqPrueba.add(item);
							} else {
								// En caso de que sean la misma
								// malla_curricula_paralelo
								if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId().intValue()) {
									npfListEstudianteBusqPrueba.add(item);
								} else {
									item.setClfId(GeneralesConstantes.APP_ID_BASE);
									item.setClfNota2(null);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
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
								nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
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
								mlcrprAux = servMallaCurricularParaleloServicio
										.buscarPorId(nrctfParaleloDtoEditar.getMlcrprId());
								MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
								mlcrmtAux = servMallaCurricularMateriaServicio
										.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
								mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
								mlcrmtAux = servMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(
										mtrAux.getMtrId(),
										MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(
										mlcrmtAux.getMlcrmtId(), nrctfParaleloDtoEditar.getPrlId());
								mtrModulo = mtrAux;

								listaprueba = servNrctfNotasPregradoDtoServicioJdbc
										.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

								List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
								for (EstudianteJdbcDto item : listaprueba) {
									item.setMateriaModulo(true);
									// En caso de que no existe el resultado de
									// búsqueda
									if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
										item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
										item.setClfNota2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfNota2(null);
										npfListEstudianteBusqPrueba.add(item);
									} else {
										// En caso de que sean la misma
										// malla_curricula_paralelo
										if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId()
												.intValue()) {
											npfListEstudianteBusqPrueba.add(item);
										} else {
											item.setClfId(GeneralesConstantes.APP_ID_BASE);
											item.setClfNota2(null);
											item.setClfAsistenciaEstudiante2(null);
											item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
											item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
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
											if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(
													npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
												if (npfListEstudianteBusqPrueba.get(i)
														.getClfId() == GeneralesConstantes.APP_ID_BASE) {
													op = false;
												}
											}
										}
									}
									if (op) {
										nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
									}
								}
							} catch (Exception ex) {
							}
						} catch (Exception ex) {
						}
					}

				} else {

					nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXParaleloXpracId(
							nrctfParaleloDtoEditar.getPrlId(), nrctfParaleloDtoEditar.getMlcrprId(),
							nrctfEstudianteBuscar.getPracId());
				}
			} else {
				if (nrctfCarreraMedicinaAnterior) {

					nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
					// //IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE
					// ESTUDIANTES SI COMPARTEN O NO COMPARTEN
					nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc
							.buscarEstudianteXParaleloInactivoMedicina(nrctfParaleloDtoEditar.getPrlId(),
									nrctfParaleloDtoEditar.getMlcrprId());

				} else {

					PeriodoAcademico pracCierre = null;
					try {
						pracCierre = servNrctfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
					} catch (Exception e) {
						// TODO: handle exception
					}
					if (pracCierre != null) {
						if (nrctfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						} else if (nrctfParaleloDtoEditar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
								|| nrctfParaleloDtoEditar
										.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						} else {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE);
						}
					} else {
						if (nrctfDependenciaBuscar.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_NIVELACION_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
						} else if (nrctfEstudianteBuscar.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_VALUE
								|| nrctfEstudianteBuscar
										.getCrrId() == CarreraConstantes.CARRERA_MEDICINA_REDISENO_VALUE) {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_RECUPERACION_MEDICINA_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
						} else {
							nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = servNrctfCronogramaActividadDtoServicioJdbcServicio
									.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(
											CronogramaConstantes.TIPO_ACADEMICO_VALUE,
											ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
						}

					}

					Date myDate = new Date(new Date().getTime());
					Date siguientePracDate = new Date(
							nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico.getPlcrFechaInicio()
									.getTime());
					Date maxDate = sumarRestarDiasFecha(siguientePracDate, 30);
					Date finIngresoNota = new Date(nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico
							.getPlcrFechaFin().getTime());
					if (myDate.after(maxDate)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
						return null;
					}
					if (myDate.before(finIngresoNota)) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Está antes de las fechas establecidas.");
						return null;
					}

					if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
						List<EstudianteJdbcDto> listaprueba = new ArrayList<EstudianteJdbcDto>();

						try {
							Integer mlcrprAuxId = servNrctfMallaCurricularParaleloDtoServicioJdbc
									.buscarPorMtrIdPrsIdentificacionPorPrlId(mtrAux.getMtrId(),
											nrctfDocente.getPrsIdentificacion(),
											nrctfParaleloDtoEditar.getMlcrmtNvlId(), nrctfParaleloDtoEditar.getPrlId());
							MallaCurricularParalelo mlcrprAux = new MallaCurricularParalelo();
							mlcrprAux = servMallaCurricularParaleloServicio.buscarPorId(mlcrprAuxId);
							mtrModulo = mtrAux;

							listaprueba = servNrctfNotasPregradoDtoServicioJdbc
									.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocenteModular(
											nrctfParaleloDtoEditar.getCrrId(), nrctfParaleloDtoEditar.getMlcrmtNvlId(),
											mlcrprAux.getMlcrprParalelo().getPrlId(), mtrAux.getMtrMateria().getMtrId(),
											nrctfDocente.getFcdcId(), mlcrprAux.getMlcrprId(),
											nrctfParaleloDtoEditar.getMlcrprId());

							List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
							for (EstudianteJdbcDto item : listaprueba) {
								item.setMateriaModulo(true);
								// En caso de que no existe el resultado de
								// búsqueda
								if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
									item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
									item.setClfNota2(null);
									item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
									item.setClfAsistenciaEstudiante2(null);
									item.setClfNota2(null);
									npfListEstudianteBusqPrueba.add(item);
								} else {
									// En caso de que sean la misma
									// malla_curricula_paralelo
									if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId().intValue()) {
										npfListEstudianteBusqPrueba.add(item);
									} else {
										item.setClfId(GeneralesConstantes.APP_ID_BASE);
										item.setClfNota2(null);
										item.setClfAsistenciaEstudiante2(null);
										item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
										item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
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
									nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
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
									mlcrprAux = servMallaCurricularParaleloServicio
											.buscarPorId(nrctfParaleloDtoEditar.getMlcrprId());
									MallaCurricularMateria mlcrmtAux = new MallaCurricularMateria();
									mlcrmtAux = servMallaCurricularMateriaServicio
											.buscarPorId(mlcrprAux.getMlcrprMallaCurricularMateria().getMlcrmtId());
									mtrAux = servNrctfMateriaDto.buscarPorId(mlcrmtAux.getMlcrmtMateria().getMtrId());
									mtrAux = servNrctfMateriaDto.buscarPorId(mtrAux.getMtrMateria().getMtrId());
									mlcrmtAux = servMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(
											mtrAux.getMtrId(),
											MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
									mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(
											mlcrmtAux.getMlcrmtId(), nrctfParaleloDtoEditar.getPrlId());
									mtrModulo = mtrAux;

									listaprueba = servNrctfNotasPregradoDtoServicioJdbc
											.buscarEstudianteXMallaCurricularParaleloId(mlcrprAux.getMlcrprId());

									List<EstudianteJdbcDto> npfListEstudianteBusqPrueba = new ArrayList<EstudianteJdbcDto>();
									for (EstudianteJdbcDto item : listaprueba) {
										item.setMateriaModulo(true);
										// En caso de que no existe el resultado
										// de búsqueda
										if (item.getMlcrprIdModulo() == GeneralesConstantes.APP_ID_BASE) {
											item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
											item.setClfNota2(null);
											item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
											item.setClfAsistenciaEstudiante2(null);
											item.setClfNota2(null);
											npfListEstudianteBusqPrueba.add(item);
										} else {
											// En caso de que sean la misma
											// malla_curricula_paralelo
											if (item.getMlcrprIdModulo() == nrctfParaleloDtoEditar.getMlcrprId()
													.intValue()) {
												npfListEstudianteBusqPrueba.add(item);
											} else {
												item.setClfId(GeneralesConstantes.APP_ID_BASE);
												item.setClfNota2(null);
												item.setClfAsistenciaEstudiante2(null);
												item.setClfAsistenciaDocente2(GeneralesConstantes.APP_ID_BASE);
												item.setMlcrprIdModulo(nrctfParaleloDtoEditar.getMlcrprId());
												npfListEstudianteBusqPrueba.add(item);
											}
										}
									}
									for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
										for (int j = i + 1; j < npfListEstudianteBusqPrueba.size(); j++) {

											if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion()
													.equals(npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())
													&& (npfListEstudianteBusqPrueba.get(i)
															.getClfId() == npfListEstudianteBusqPrueba.get(j)
																	.getClfId())) {
												npfListEstudianteBusqPrueba.remove(j);
												j--;
											}
										}
									}

									for (int i = 0; i < npfListEstudianteBusqPrueba.size(); i++) {
										boolean op = true;
										for (int j = 0; j < npfListEstudianteBusqPrueba.size(); j++) {
											if (i != j) {
												if (npfListEstudianteBusqPrueba.get(i).getPrsIdentificacion().equals(
														npfListEstudianteBusqPrueba.get(j).getPrsIdentificacion())) {
													if (npfListEstudianteBusqPrueba.get(i)
															.getClfId() == GeneralesConstantes.APP_ID_BASE) {
														op = false;
													}
												}
											}
										}
										if (op) {
											nrctfListEstudianteBusq.add(npfListEstudianteBusqPrueba.get(i));
										}
									}
								} catch (Exception ex) {
								}
							} catch (Exception ex) {
							}
						}

					} else {
						// //IDENTIFICACIÓN EN LOS RESULTADOS DE LA LISTA DE
						// ESTUDIANTES SI COMPARTEN O NO COMPARTEN

						nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
						nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteXParalelo(
								nrctfParaleloDtoEditar.getPrlId(), nrctfParaleloDtoEditar.getMlcrprId());
						////////////////////////////////////////////////////////////////////////////////////////////////////
					}
				}

			}

			nrctfDirCarrera = new PersonaDto();
			if (nrctfDependenciaBuscar.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
				nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(
						RolConstantes.ROL_DIRCARRERA_VALUE, nrctfParaleloDtoEditar.getCrrId());
			} else {
				nrctfDirCarrera.setPrsIdentificacion("1713271896");
				nrctfDirCarrera.setPrsPrimerApellido("LARA");
				nrctfDirCarrera.setPrsSegundoApellido("REIMUNDO");
				nrctfDirCarrera.setPrsNombres("JOSE JULIO");
				nrctfDirCarrera.setPrsMailInstitucional("jjlarar@uce.edu.ec");
				nrctfDirCarrera.setUsrNick("jjlarar");
			}
			for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
				if (item.getClfAsistenciaDocente2() != null) {
					nrctfAsistenciaDocente = item.getClfAsistenciaDocente2();
					break;
				} else {
					nrctfAsistenciaDocente = null;
				}
			}
			nrctfEstudianteBuscar.setMtrId(nrctfParaleloDtoEditar.getMtrId());
			Iterator<EstudianteJdbcDto> it = nrctfListEstudianteBusq.iterator();
			while (it.hasNext()) {
				EstudianteJdbcDto aux = new EstudianteJdbcDto();
				aux = it.next();
				if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
					int comparador = aux.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
					int comparador1 = aux.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
					if (comparador >= 0 || comparador1 == -1) {
						it.remove();
						continue;
					}
				} else {
					int comparador = aux.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
					int comparador1 = aux.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
					if (comparador >= 0 || comparador1 == -1) {
						it.remove();
						continue;
					}
				}
			}

			// nrctfListEstudianteBusq =
			// servNrctfNotasPregradoDtoServicioJdbc.buscarEstudianteRectificacionRecuperacion(nrctfParaleloDtoEditar.getCrrId(),
			// nrctfParaleloDtoEditar.getMlcrmtNvlId(),
			// nrctfParaleloDtoEditar.getPrlId(),
			// nrctfParaleloDtoEditar.getMtrId(),nrctfDocente.getFcdcId(),nrctfParaleloDtoEditar.getMlcrprId());
			nrctfDirCarrera = new PersonaDto();
			if (nrctfDependenciaBuscar.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
				nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(
						RolConstantes.ROL_DIRCARRERA_VALUE, nrctfParaleloDtoEditar.getCrrId());
			} else {
				nrctfDirCarrera.setPrsIdentificacion("1713271896");
				nrctfDirCarrera.setPrsPrimerApellido("LARA");
				nrctfDirCarrera.setPrsSegundoApellido("REIMUNDO");
				nrctfDirCarrera.setPrsNombres("JOSE JULIO");
				nrctfDirCarrera.setPrsMailInstitucional("jjlarar@uce.edu.ec");
				nrctfDirCarrera.setUsrNick("jjlarar");
			}
			nrctfTipoRectificacion = new Integer(3);
			if (nrctfListEstudianteBusq.isEmpty()) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen estudiantes en el paralelo para rectificar notas de recuperación");
				return null;
			} else {
				nrctfListEstudianteBusq = GeneralesUtilidades.quitarDuplicados(nrctfListEstudianteBusq);
			}
		} catch (Exception e) {
		}
		return "irRectificarRecuperacion";
	}

	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas segundo parcial
	 * 
	 * @param prl
	 *            .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRectificarRecuperacionPosgrado(ParaleloDto prl) {
		nrctfParaleloDtoEditar = new ParaleloDto();
		nrctfParaleloDtoEditar = prl;
		nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		nrctfDependenciaBuscar = new Dependencia();
		try {
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			String idHostAux = new String();
			String ipLocalClienteAux = new String();
			// String ipPublicaClienteAux = new String();
			idHostAux = datosCliente.get(0);
			ipLocalClienteAux = datosCliente.get(1);
			// ipPublicaClienteAux=datosCliente.get(6);
			// nrctfRegCliente = "ID
			// SERVIDOR:".concat(idHostAux).concat("|"+ipLocalClienteAux.concat("|"
			// + datosCliente.get(4).concat("|"+datosCliente.get(6))));
			Date fechaActual = new Date();
			nrctfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: MODIFICA PARCIAL 2");

			nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc
					.buscarEstudianteRectificacionRecuperacionPosgrado(nrctfParaleloDtoEditar.getCrrId(),
							nrctfParaleloDtoEditar.getMlcrmtNvlId(), nrctfParaleloDtoEditar.getPrlId(),
							nrctfParaleloDtoEditar.getMtrId(), nrctfDocente.getFcdcId(),
							nrctfParaleloDtoEditar.getMlcrprId());
			nrctfDirCarrera = new PersonaDto();
			if (nrctfDependenciaBuscar.getDpnId() != DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) {
				nrctfDirCarrera = servNrctfPersonaDtoServicioJdbc.buscarPersonaProlDirCarreraPcrrId(
						RolConstantes.ROL_DIRCARRERA_VALUE, nrctfParaleloDtoEditar.getCrrId());
			} else {
				nrctfDirCarrera.setPrsIdentificacion("1713271896");
				nrctfDirCarrera.setPrsPrimerApellido("LARA");
				nrctfDirCarrera.setPrsSegundoApellido("REIMUNDO");
				nrctfDirCarrera.setPrsNombres("JOSE JULIO");
				nrctfDirCarrera.setPrsMailInstitucional("jjlarar@uce.edu.ec");
				nrctfDirCarrera.setUsrNick("jjlarar");
			}
			nrctfTipoRectificacion = new Integer(3);
			if (nrctfListEstudianteBusq.isEmpty()) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen estudiantes en el paralelo para rectificar notas de recuperación");
				return null;
			}
			// } catch (DependenciaNoEncontradoException e) {
			// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new
			// MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.dependencia.no.encontrado.exception")));
			// } catch (DependenciaException e){
			// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new
			// MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.dependencia.exception")));
			// } catch (CronogramaActividadDtoJdbcException e) {
			// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new
			// MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.CronogramaActividadDtoJdbc.exception")));
			// } catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new
			// MensajeGeneradorUtilidades("Rectificacion.Notas.Docente.irRectificarRecuperacion.CronogramaActividadDtoJdbc.no.encontrado.exception")));
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificarRecuperacion.EstudianteDtoJdbcc.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificarRecuperacion.EstudianteDtoJdbcc.no.encontrado.exception")));
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
					"Rectificacion.Notas.Docente.irRectificarRecuperacion.PersonaDto.no.encontrado.exception")));
			// } catch (PeriodoAcademicoNoEncontradoException e) {
		}
		return "irRectificarRecuperacion";
	}

	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por
	 * carrera nivel y materia
	 * 
	 * @return Xhtml listar
	 */
	public String irCancelar() {
		nrctfParaleloDtoEditar = null;
		nrctfListEstudianteBusq = null;
		nrctfListEstudianteEditar = null;
		nrctfDirCarrera = null;
		nrctfValidadorClic = new Integer(0);
		nrctfTipoRectificacion = new Integer(0);
		return "irCancelar";
	}

	/*
	 * Metodo que permite generar el reporte
	 */
	public void generarReporte(ParaleloDto prl) {

		nrctfParaleloDtoEditar = new ParaleloDto();
		nrctfParaleloDtoEditar = prl;
		nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc
					.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(nrctfParaleloDtoEditar.getCrrId(),
							nrctfParaleloDtoEditar.getMlcrmtNvlId(), nrctfParaleloDtoEditar.getPrlId(),
							nrctfParaleloDtoEditar.getMtrId(), nrctfDocente.getFcdcId(),
							nrctfParaleloDtoEditar.getMlcrprId());

		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	@SuppressWarnings("static-access")
	public String irVerNotas(ParaleloDto prl) {
		ReporteNotasForm reporte = new ReporteNotasForm();
		nrctfParaleloDtoEditar = new ParaleloDto();
		nrctfParaleloDtoEditar = prl;
		nrctfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			nrctfListEstudianteBusq = servNrctfNotasPregradoDtoServicioJdbc
					.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(nrctfParaleloDtoEditar.getCrrId(),
							nrctfParaleloDtoEditar.getMlcrmtNvlId(), nrctfParaleloDtoEditar.getPrlId(),
							nrctfParaleloDtoEditar.getMtrId(), nrctfDocente.getFcdcId(),
							nrctfParaleloDtoEditar.getMlcrprId());

			activacion();
			reporte.generarReporteNotas(nrctfListEstudianteBusq, nrctfUsuario);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas";
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
				.divide(new BigDecimal(asitenciaDoc), 0, RoundingMode.HALF_UP);
		return itemCost;
	}

	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinalRecuperacion() {
		// for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
		// if(item.getClfSupletorio()!=null){
		// if(!validador(item.getClfSupletorio())){
		// FacesUtil.limpiarMensaje();
		// FacesUtil.mensajeError(item.getPrsPrimerApellido()+"
		// "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no
		// es valida");
		// nrctfValidadorClic = 0;
		// return null;
		// }
		// }else{
		// FacesUtil.limpiarMensaje();
		// FacesUtil.mensajeError(item.getPrsPrimerApellido()+"
		// "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" debe
		// ingresar la nota de recuperación");
		// nrctfValidadorClic = 0;
		// return null;
		// }
		// }
		nrctfValidadorClic = 2;
		return null;
	}

	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal() {
		if (nrctfAsistenciaDocente != null) {

			if (nrctfTipoRectificacion.intValue() == 1) {

				for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
					item.setClfAsistenciaDocente1(nrctfAsistenciaDocente);
					if (item.getClfNota1() != null && item.getClfAsistenciaEstudiante1() != null) {
						String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
						if (!validadorAsistencia(cadena, nrctfAsistenciaDocente)) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la asistencia no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
						if (!validador(item.getClfNota1())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la nota no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
					} else {
						if (item.getClfAsistenciaEstudiante1() != null) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la nota no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
						if (item.getClfNota1() != null) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la asistencia no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
					}

				}
			} else if (nrctfTipoRectificacion.intValue() == 2) {
				for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
					item.setClfAsistenciaDocente2(nrctfAsistenciaDocente);
					if (item.getClfNota2() != null && item.getClfAsistenciaEstudiante2() != null) {
						String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
						if (!validadorAsistencia(cadena, nrctfAsistenciaDocente)) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la asistencia no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
						if (!validador(item.getClfNota2())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la nota no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
					} else {
						if (item.getClfAsistenciaEstudiante2() != null) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la nota no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
						if (item.getClfNota2() != null) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido() + " " + item.getPrsSegundoApellido()
									+ " " + item.getPrsNombres() + " la asistencia no es valida");
							nrctfValidadorClic = 0;
							return null;
						}
					}
				}
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
			return null;
		}
		nrctfValidadorClic = 2;
		return null;
	}

	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista
	 * de paralelos
	 * 
	 * @return XHTML listar paralelos
	 */
	public String guardar() {
		try {
			if (nrctfAsistenciaDocente != null) {
				// for (int i = 0; i < nrctfListEstudianteBusq.size(); i++) {
				boolean op = false;
				for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNrctfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getRcesId());
					servNrctfCalificacionServicio.guardarEdicionNotasPrimerHemi(rcesAux, item, nrctfRegCliente);
					try {
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
						// calculo la suma de na nota1 + nota2 con redondeo de
						// una cifra decimal
						BigDecimal sumaParciales = BigDecimal.ZERO;
						sumaParciales = item.getClfNota1().setScale(2, RoundingMode.DOWN)
								.add(item.getClfNota2().setScale(2, RoundingMode.DOWN));
						item.setClfSumaP1P2(sumaParciales);

						// calculo de la suma de asistencia del estudiante de
						// los dos parciales
						int sumaAsistenciaParciales = 0;
						sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1()
								+ item.getClfAsistenciaEstudiante2();
						item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));

						// calculo de la suma de la asistencia del docente de
						// los dos parciales
						int sumaAsistenciaDoc = 0;
						sumaAsistenciaDoc = item.getClfAsistenciaDocente1() + item.getClfAsistenciaDocente2();
						item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));

						// calcula el promedio de la asistencia del estudiante
						item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(
								CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE,
								item.getClfAsistenciaTotal().intValue(), item.getClfAsistenciaTotalDoc().intValue()));

						int com = item.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
						// si la suma de los parciales es mayor o igual a 27.5
						if (com == 1 || com == 0) {
							int promedioAsistencia = 0;
							promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
							// si el promedio de asistencia es mayor o igual a
							// 80
							if (promedioAsistencia == 1 || promedioAsistencia == 0) {
								// calcula la nota final del semestre y el
								// estado a aprobado
								item.setClfPromedioNotas(sumaParciales.setScale(2, RoundingMode.HALF_UP));
								item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
								item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
							} else {// si el promedio de asistencia es menor a
									// 80
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
							// si la suma de los parciales el menor a 27.5 y es
							// mayor o igual a 8.8
							if (minNota == 0 || minNota == 1) {
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
								if (promedioAsistencia == 1 || promedioAsistencia == 0 || mtrAux.getMtrTipoMateria()
										.getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
									BigDecimal itemCost = BigDecimal.ZERO;
									itemCost = sumaParciales
											.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE))
											.divide(new BigDecimal(
													CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2,
													RoundingMode.DOWN);
									int comparador = 0;
									try {
										comparador = item.getClfParamRecuperacion2()
												.compareTo(new BigDecimal(GeneralesConstantes.APP_ID_BASE));
									} catch (Exception e) {
									}
									if (item.getRcesIngersoNota3() != 0) {
										BigDecimal suma = item.getClfParamRecuperacion1()
												.add(item.getClfParamRecuperacion2());
										comparador = suma.compareTo(new BigDecimal(27.5));
										if (comparador == 0 || comparador == 1) {
											item.setClfNotalFinalSemestre(suma.setScale(0, RoundingMode.HALF_UP));
											item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
										} else {
											item.setClfNotalFinalSemestre(suma.setScale(2, RoundingMode.HALF_UP));
											item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
										}

									} else {
										item.setClfParamRecuperacion1(itemCost);
										item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
										item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
									}
								} else {
									BigDecimal itemCost = BigDecimal.ZERO;
									itemCost = sumaParciales
											.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE))
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
						}
						try {
							servNrctfCalificacionServicio.guardarEdicionNotasSegundoHemi(rcesAux, item,
									nrctfRegCliente);
							if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
								servNrctfCalificacionServicio.verificarModulos(rcesAux, item, nrctfRegCliente);
							}
						} catch (Exception e) {
						}
						try {
							servNrctfCalificacionServicio.guardarCorreccionFull(item.getClfId());
						} catch (Exception e) {
						}
					} catch (Exception e) {
					}

					nrctfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Ingreso de notas exitoso");
					op = true;
				}
				//

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

						// *********************************************************//
						// ******PARAMETROS PARA RECTIFICACION 1ER
						// HEMISEMESTR******//
						// *********************************************************//
						if (nrctfTipoRectificacion == 1) {
							pathGeneralReportes
									.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/primerHemi");
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/reporteNota1HemiRectificacion.jasper")));
						}

						if (nrctfTipoRectificacion == 2) {
							pathGeneralReportes.append(
									"/academico/reportes/archivosJasper/reporteNotas/rectificacion/segundoHemi");
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/reporteNota2Hemi2Rectificacion.jasper")));
						}

						if (nrctfTipoRectificacion == 3) {
							pathGeneralReportes.append(
									"/academico/reportes/archivosJasper/reporteNotas/rectificacion/recuperacion");
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/reporteNotaRecuperacionRectificacion.jasper")));
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
						StringBuilder sbAsistenciaDocente = new StringBuilder();
						frmAdjuntoParametros.put("nick", nrctfUsuario.getUsrNick());
						try {
							frmAdjuntoParametros.put("docente", nrctfDocente.getPrsNombres() + " "
									+ nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido());
						} catch (Exception e) {
							frmAdjuntoParametros.put("docente",
									nrctfDocente.getPrsNombres() + " " + nrctfDocente.getPrsPrimerApellido());
						}

						Carrera crrAux = new Carrera();
						try {
							crrAux = servNrctfCarreraServicio.buscarPorId(nrctfParaleloDtoEditar.getCrrId());
							nrctfNomCarrera = crrAux.getCrrDescripcion().toString();
						} catch (CarreraNoEncontradoException | CarreraException e) {
						}
						for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
							if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
								nrctfNomMateria = itemAux.getMtrDescripcion().toString() + " - "
										+ mtrAux.getMtrDescripcion();
							} else {
								nrctfNomMateria = itemAux.getMtrDescripcion().toString();
							}
							nrctfNomParalelo = itemAux.getPrlDescripcion().toString();
							nrctfNomNivel = itemAux.getNvlDescripcion().toString();
							if (nrctfTipoRectificacion == 1) {
								nrctfNomNotaRectificacion = ("PRIMER HEMISEMESTRE");
							}
							if (nrctfTipoRectificacion == 2) {
								nrctfNomNotaRectificacion = ("SEGUNDO HEMISEMESTRE");
							}
							if (nrctfTipoRectificacion == 3) {
								nrctfNomNotaRectificacion = ("RECUPERACIÓN");
							}

							frmAdjuntoParametros.put("periodo", itemAux.getPracDescripcion());
							frmAdjuntoParametros.put("facultad", crrAux.getCrrDependencia().getDpnDescripcion());
							frmAdjuntoParametros.put("carrera", crrAux.getCrrDetalle());
							frmAdjuntoParametros.put("curso", itemAux.getNvlDescripcion());
							frmAdjuntoParametros.put("paralelo", itemAux.getPrlDescripcion());
							frmAdjuntoParametros.put("materia", itemAux.getMtrDescripcion());

							sbAsistenciaDocente.append(itemAux.getClfAsistenciaDocente1());
							frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
							break;
						}
						frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes + "/cabeceraNotas.png");
						frmAdjuntoParametros.put("imagenPie", pathGeneralReportes + "/pieNotas.png");

						frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
						Map<String, Object> datoEstudiantes = null;

						// *********************************************************//
						// ******PARAMETROS PARA RECTIFICACION 1ER
						// HEMISEMESTR******//
						// *********************************************************//
						int cont1 = 1;
						if (nrctfTipoRectificacion == 1) {
							for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
								if (itemAux.getClfNota1() != null) {
									String nota = itemAux.getClfNota1().toString();
									int puntoDecimalUbc = nota.indexOf('.');
									int totalDecimales = nota.length() - puntoDecimalUbc - 1;
									if (puntoDecimalUbc == -1) {
										itemAux.setClfNota1String(nota + ".00");
									} else if (totalDecimales == 1) {
										itemAux.setClfNota1String(nota + "0");
									} else {
										itemAux.setClfNota1String(nota);
									}
								}
								StringBuilder sbNota1 = new StringBuilder();
								StringBuilder sbAsistencia1 = new StringBuilder();
								StringBuilder sbContador1 = new StringBuilder();
								datoEstudiantes = new HashMap<String, Object>();
								datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
								datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
								datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
								datoEstudiantes.put("nombres", itemAux.getPrsNombres());
								if (itemAux.getClfNota1() != null) {
									sbNota1.append(itemAux.getClfNota1String());
									datoEstudiantes.put("nota1", sbNota1.toString());
								} else {
									sbNota1.append("-");
									datoEstudiantes.put("nota1", sbNota1.toString());
								}
								if (itemAux.getClfAsistenciaEstudiante1() != null) {
									sbAsistencia1.append(itemAux.getClfAsistenciaEstudiante1());
									datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
								} else {
									sbAsistencia1.append("-");
									datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
								}
								sbContador1.append(cont1);
								datoEstudiantes.put("numero", sbContador1.toString());

								frmAdjuntoCampos.add(datoEstudiantes);
								cont1 = cont1 + 1;
							}
						}
						// *********************************************************//
						// ******PARAMETROS PARA RECTIFICACION 2do
						// HEMISEMESTR******//
						// *********************************************************//
						int cont2 = 1;
						if (nrctfTipoRectificacion == 2) {
							for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
								StringBuilder sbNota2 = new StringBuilder();
								StringBuilder sbAsistencia2 = new StringBuilder();
								StringBuilder sbContador2 = new StringBuilder();
								datoEstudiantes = new HashMap<String, Object>();
								datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
								datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
								datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
								datoEstudiantes.put("nombres", itemAux.getPrsNombres());
								if (itemAux.getClfNota1() != null) {
									sbNota2.append(itemAux.getClfNota2());
									datoEstudiantes.put("nota1", sbNota2.toString());
								} else {
									sbNota2.append("-");
									datoEstudiantes.put("nota1", sbNota2.toString());
								}
								if (itemAux.getClfAsistenciaEstudiante2() != null) {
									sbAsistencia2.append(itemAux.getClfAsistenciaEstudiante2());
									datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
								} else {
									sbAsistencia2.append("-");
									datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
								}
								sbContador2.append(cont2);
								datoEstudiantes.put("numero", sbContador2.toString());

								frmAdjuntoCampos.add(datoEstudiantes);
								cont2 = cont2 + 1;
							}
						}
						// *********************************************************//
						// ******PARAMETROS PARA RECTIFICACION 2do
						// HEMISEMESTR******//
						// *********************************************************//
						int cont3 = 1;
						if (nrctfTipoRectificacion == 3) {
							for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
								StringBuilder sbNota3 = new StringBuilder();
								StringBuilder sbContador3 = new StringBuilder();
								datoEstudiantes = new HashMap<String, Object>();
								datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
								datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
								datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
								datoEstudiantes.put("nombres", itemAux.getPrsNombres());
								if (itemAux.getClfNota1() != null) {
									sbNota3.append(itemAux.getClfNota2());
									datoEstudiantes.put("nota1", sbNota3.toString());
								} else {
									sbNota3.append("-");
									datoEstudiantes.put("nota1", sbNota3.toString());
								}
								sbContador3.append(cont3);
								datoEstudiantes.put("numero", sbContador3.toString());

								frmAdjuntoCampos.add(datoEstudiantes);
								cont3 = cont3 + 1;
							}
						}

						JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);

						jasperPrint = JasperFillManager.fillReport(jasperReport, frmAdjuntoParametros, dataSource);

						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
						// AdjuntoDto adjuntoDto = new AdjuntoDto();
						// adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
						// adjuntoDto.setAdjunto(arreglo);

						// lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(nrctfDocente.getPrsMailInstitucional());

						// lista de correos copia a los que se enviara el mail
						// al director de carrera
						// List<String> correosCc = new ArrayList<>();
						// correosCc.add(nrctfDirCarrera.getPrsMailInstitucional());

						// path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo = new StringBuilder();
						// path de la plantilla del mail para el director de
						// carrera
						ProductorMailJson pmailDirCarrera = null;
						StringBuilder sbCorreoDirCarrera = new StringBuilder();

						try {
							sbCorreo = GeneralesUtilidades.generarAsuntoRectificacionNotas(
									GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido()
											+ " " + nrctfDocente.getPrsNombres(),
									GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
									nrctfNomParalelo, nrctfNomNotaRectificacion);
						} catch (Exception e) {
							sbCorreo = GeneralesUtilidades.generarAsuntoRectificacionNotas(
									GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsNombres(),
									GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
									nrctfNomParalelo, nrctfNomNotaRectificacion);
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

						try {
							sbCorreoDirCarrera = GeneralesUtilidades.generarAsuntoRectificacionNotasDirCarrera(
									(GeneralesUtilidades.generaStringParaCorreo(fecha.toString())),
									nrctfDirCarrera.getPrsPrimerApellido() + " "
											+ nrctfDirCarrera.getPrsSegundoApellido() + " "
											+ nrctfDirCarrera.getPrsNombres(),
									nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido()
											+ " " + nrctfDocente.getPrsNombres(),
									GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
									nrctfNomNivel, nrctfNomParalelo, nrctfNomNotaRectificacion);
						} catch (Exception e) {
							sbCorreoDirCarrera = GeneralesUtilidades.generarAsuntoRectificacionNotasDirCarrera(
									(GeneralesUtilidades.generaStringParaCorreo(fecha.toString())),
									nrctfDirCarrera.getPrsPrimerApellido() + " "
											+ nrctfDirCarrera.getPrsSegundoApellido() + " "
											+ nrctfDirCarrera.getPrsNombres(),
									nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsNombres(),
									GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
									nrctfNomNivel, nrctfNomParalelo, nrctfNomNotaRectificacion);
						}

						pmailDirCarrera = new ProductorMailJson(correosTo, null, null,
								GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_REGISTRO_NOTAS,
								sbCorreoDirCarrera.toString(), "admin", "dt1c201s", true, arreglo,
								"RegistroNotas." + MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF);
						String jsonStDirCarrera = pmailDirCarrera.generarMail();
						Gson jsonDirCarrera = new Gson();
						MailDto mailDtoDirCarrera = jsonDirCarrera.fromJson(jsonStDirCarrera, MailDto.class);
						// Iniciamos el envío de mensajes
						ObjectMessage messageDirCarrera = session.createObjectMessage(mailDtoDirCarrera);
						producer.send(messageDirCarrera);

					} catch (JMSException e) {
					} catch (JRException e) {
					} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
					}

					// ******************************************************************************
					// *********************** ACA FINALIZA EL ENVIO DE MAIL
					// ************************
					// ******************************************************************************

				}

			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
				return null;
			}
		} catch (SQLIntegrityConstraintViolationException | ConstraintViolationException | PersistenceException e) {
			FacesUtil.mensajeError("Ocurrió un error al guardar las notas, por favor revise los listados de notas.");

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

	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista
	 * de paralelos
	 * 
	 * @return XHTML listar paralelos
	 */
	public String guardarRecuperacion() {
		try {
			// for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
			// RecordEstudianteDto rcesAux = new RecordEstudianteDto();
			// rcesAux =
			// servNrctfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(),
			// item.getMlcrprId());
			// if(item.getClfSupletorio()!=null){
			//
			// BigDecimal parametro2Aux = BigDecimal.ZERO;
			// parametro2Aux = item.getClfSupletorio().multiply(new
			// BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).divide(new
			// BigDecimal(CalificacionConstantes.NOTA_MAX_PARCIAL_VALUE), 2,
			// RoundingMode.DOWN);
			// item.setClfParamRecuperacion2(parametro2Aux);
			//
			// BigDecimal sumaParametros = BigDecimal.ZERO;
			// sumaParametros = item.getClfParamRecuperacion1().setScale(2,
			// RoundingMode.DOWN).add(item.getClfParamRecuperacion2().setScale(2,
			// RoundingMode.DOWN));
			// item.setClfNotalFinalSemestre(sumaParametros.setScale(0,
			// RoundingMode.HALF_UP));
			//
			// int estadoRces = item.getClfNotalFinalSemestre().compareTo(new
			// BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
			// if(estadoRces == 1 || estadoRces ==0){
			// item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
			// }else{
			// item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
			// }
			// servNrctfCalificacionServicio.guardarNotasRectificacionRecuperacion(rcesAux,
			// item , nrctfRegCliente);
			// nrctfValidadorClic=0;
			// FacesUtil.limpiarMensaje();
			// FacesUtil.mensajeInfo("Ingreso de notas exitoso");
			// }else{
			// FacesUtil.limpiarMensaje();
			// FacesUtil.mensajeError("Debe ingresar la nota de recuperación de
			// todos los estudiantes");
			// return null;
			// }
			// }

			boolean op = false;
			for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
				RecordEstudianteDto rcesAux = new RecordEstudianteDto();
				rcesAux = servNrctfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(),
						item.getMlcrprId());
				try {
					int comparador = item.getClfSupletorio().compareTo(new BigDecimal(0));
					if (comparador == 1) {
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
						sumaParametros = item.getClfParamRecuperacion1().setScale(2, RoundingMode.DOWN)
								.add(item.getClfParamRecuperacion2().setScale(2, RoundingMode.DOWN));
						item.setClfNotalFinalSemestre(sumaParametros.setScale(2, RoundingMode.HALF_UP));
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						int estadoRces = item.getClfNotalFinalSemestre()
								.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
						if (estadoRces == 1 || estadoRces == 0) {
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
							item.setClfNotalFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP));
						} else {
							item.setClfNotalFinalSemestre(sumaParametros.setScale(0, RoundingMode.DOWN));
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
						mtrAux = servNrctfMateriaDto.buscarPorId(nrctfParaleloDtoEditar.getMtrId());
						if (mtrAux.getMtrTipoMateria().getTimtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
							try {
								servNrctfCalificacionServicio.guardarNotasRectificacionRecuperacionModular(rcesAux,
										item, nrctfRegCliente);
								List<MateriaDto> listaMateriasModulares = new ArrayList<MateriaDto>();
								if(permisoEdicionPeridoAnterior){
									listaMateriasModulares = servNrctfMateriaDtoServicioJdbc
											.listarXestudianteXmatriculaXperiodoEnCierreXcarreraXMateriaModular(
													item.getPrsIdentificacion(), item.getRcesId(),nrctfPeriodoAcademicoEdicionAnterior.getPracId());	
								}else{
									try {
										nrctfPeriodoAcademicoBusq = servNrctfPeriodoAcademicoDtoServicioJdbc
												.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
									} catch (Exception e) {
										nrctfPeriodoAcademicoBusq = servNrctfPeriodoAcademicoDtoServicioJdbc
												.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
									}
									
									listaMateriasModulares = servNrctfMateriaDtoServicioJdbc
											.listarXestudianteXmatriculaXperiodoEnCierreXcarreraXMateriaModular(
													item.getPrsIdentificacion(), item.getRcesId(),nrctfPeriodoAcademicoBusq.getPracId());
								}
								
								if (listaMateriasModulares != null && listaMateriasModulares.size() != 0) {
									List<Materia> listaModulos = new ArrayList<>();
									listaModulos = servNrctfMateriaDto.listarTodosModulos(item.getMtrId());
									if (listaMateriasModulares.size() == listaModulos.size()) {
										BigDecimal asistenciaSuma = BigDecimal.ZERO;
										BigDecimal asistenciaSumaP1P2 = BigDecimal.ZERO;
										BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
										BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
										BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
										BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
										BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
										BigDecimal notEstudiante2 = BigDecimal.ZERO;
										BigDecimal notEstudiante1 = BigDecimal.ZERO;
										BigDecimal notaSuma = BigDecimal.ZERO;
										try {
											for (MateriaDto materiaDto : listaMateriasModulares) {
												asistenciaSumaP1P2 = asistenciaSumaP1P2
														.add(new BigDecimal(materiaDto.getClfSumaP1P2()));
												try {
													notaSuma = notaSuma
															.add(new BigDecimal(materiaDto.getClfNotaFinalSemestre())
																	.setScale(2, RoundingMode.DOWN));
												} catch (Exception e) {
													notaSuma = notaSuma.add(new BigDecimal(materiaDto.getClfSumaP1P2())
															.setScale(2, RoundingMode.DOWN));
												}
												asistenciaSuma = asistenciaSuma
														.add(new BigDecimal(materiaDto.getClfAsistenciaTotal())
																.setScale(2, RoundingMode.DOWN))
														.setScale(2, RoundingMode.DOWN);
												try {
													asistenciaSumaDocente = asistenciaSumaDocente
															.add(new BigDecimal(materiaDto.getClfAsistenciaTotalDoc())
																	.setScale(2, RoundingMode.DOWN))
															.setScale(2, RoundingMode.DOWN);
												} catch (Exception e) {
												}

												asistenciaDocente1 = asistenciaDocente1
														.add(new BigDecimal(materiaDto.getClfAsistenciaDocente1())
																.setScale(2, RoundingMode.DOWN))
														.setScale(2, RoundingMode.DOWN);
												asistenciaDocente2 = asistenciaDocente2
														.add(new BigDecimal(materiaDto.getClfAsistenciaDocente2())
																.setScale(2, RoundingMode.DOWN))
														.setScale(2, RoundingMode.DOWN);
												;
												asistenciaEstudiante1 = asistenciaEstudiante1
														.add(new BigDecimal(materiaDto.getClfAsistencia1()).setScale(2,
																RoundingMode.DOWN))
														.setScale(2, RoundingMode.DOWN);
												;
												asistenciaEstudiante2 = asistenciaEstudiante2
														.add(new BigDecimal(materiaDto.getClfAsistencia2()).setScale(2,
																RoundingMode.DOWN))
														.setScale(2, RoundingMode.DOWN);
												;
												notEstudiante1 = notEstudiante1
														.add(new BigDecimal(materiaDto.getClfNota1()));
												notEstudiante2 = notEstudiante2
														.add(new BigDecimal(materiaDto.getClfNota2()));
											}
											notEstudiante1 = notEstudiante1.divide(new BigDecimal(listaModulos.size()),
													2, RoundingMode.DOWN);
											notEstudiante2 = notEstudiante2.divide(new BigDecimal(listaModulos.size()),
													2, RoundingMode.DOWN);
											Calificacion clfAuxFinal = new Calificacion();
											clfAuxFinal.setClfAsistencia1(asistenciaEstudiante1.floatValue());
											clfAuxFinal.setClfAsistencia2(asistenciaEstudiante2.floatValue());
											clfAuxFinal.setClfAsistenciaDocente1(asistenciaDocente1.floatValue());
											clfAuxFinal.setClfAsistenciaDocente2(asistenciaDocente2.floatValue());
											clfAuxFinal.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
											clfAuxFinal.setClfAsistenciaTotal(asistenciaSuma.floatValue());
											clfAuxFinal.setClfNota1(notEstudiante1.floatValue());
											clfAuxFinal.setClfNota2(notEstudiante2.floatValue());
											notaSuma = notaSuma.divide(new BigDecimal(listaModulos.size()), 2,
													RoundingMode.DOWN);
											clfAuxFinal.setClfNotaFinalSemestre(notaSuma.floatValue());
											RecordEstudiante rcesAuxFinal = servNpfRecordEstudianteServicio
													.buscarPorId(item.getRcesId());
											clfAuxFinal.setRecordEstudiante(rcesAuxFinal);
											servNrctfCalificacionServicio.guardarCorreccion(clfAuxFinal);
										} catch (Exception e) {
										}
									}
								}
							} catch (Exception e) {
							}

						} else {
							servNrctfCalificacionServicio.guardarNotasRectificacionRecuperacion(rcesAux, item,
									nrctfRegCliente);
						}
						try {
							servNrctfCalificacionServicio.guardarCorreccionFull(item.getClfId());
						} catch (Exception e) {
						}
						nrctfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Ingreso de notas exitoso");
						op = true;
					}
				} catch (Exception e) {
				}
			}
			if (op) {
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
					pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));

					// *********************************************************//
					// ******PARAMETROS PARA RECTIFICACION 1ER
					// HEMISEMESTR******//
					// *********************************************************//
					if (nrctfTipoRectificacion == 1) {
						pathGeneralReportes
								.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/primerHemi");
						jasperReport = (JasperReport) JRLoader.loadObject(
								new File((pathGeneralReportes.toString() + "/reporteNota1HemiRectificacion.jasper")));
					}

					if (nrctfTipoRectificacion == 2) {
						pathGeneralReportes
								.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/segundoHemi");
						jasperReport = (JasperReport) JRLoader.loadObject(
								new File((pathGeneralReportes.toString() + "/reporteNota2Hemi2Rectificacion.jasper")));
					}

					if (nrctfTipoRectificacion == 3) {
						pathGeneralReportes
								.append("/academico/reportes/archivosJasper/reporteNotas/rectificacion/recuperacion");
						jasperReport = (JasperReport) JRLoader.loadObject(new File(
								(pathGeneralReportes.toString() + "/reporteNotaRecuperacionRectificacion.jasper")));
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
					StringBuilder sbAsistenciaDocente = new StringBuilder();
					frmAdjuntoParametros.put("nick", nrctfUsuario.getUsrNick());
					try {
						frmAdjuntoParametros.put("docente", nrctfDocente.getPrsNombres() + " "
								+ nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido());
					} catch (Exception e) {
						frmAdjuntoParametros.put("docente",
								nrctfDocente.getPrsNombres() + " " + nrctfDocente.getPrsPrimerApellido());
					}

					Carrera crrAux = new Carrera();
					try {
						crrAux = servNrctfCarreraServicio.buscarPorId(nrctfParaleloDtoEditar.getCrrId());
						nrctfNomCarrera = crrAux.getCrrDescripcion().toString();
					} catch (CarreraNoEncontradoException | CarreraException e) {
					}
					for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {

						nrctfNomMateria = itemAux.getMtrDescripcion().toString();
						nrctfNomParalelo = itemAux.getPrlDescripcion().toString();
						nrctfNomNivel = itemAux.getNvlDescripcion().toString();
						if (nrctfTipoRectificacion == 1) {
							nrctfNomNotaRectificacion = ("PRIMER HEMISEMESTRE");
						}
						if (nrctfTipoRectificacion == 2) {
							nrctfNomNotaRectificacion = ("SEGUNDO HEMISEMESTRE");
						}
						if (nrctfTipoRectificacion == 3) {
							nrctfNomNotaRectificacion = ("RECUPERACIÓN");
						}

						frmAdjuntoParametros.put("periodo", itemAux.getPracDescripcion());
						frmAdjuntoParametros.put("facultad", crrAux.getCrrDependencia().getDpnDescripcion());
						frmAdjuntoParametros.put("carrera", crrAux.getCrrDetalle());
						frmAdjuntoParametros.put("curso", itemAux.getNvlDescripcion());
						frmAdjuntoParametros.put("paralelo", itemAux.getPrlDescripcion());
						frmAdjuntoParametros.put("materia", itemAux.getMtrDescripcion());

						sbAsistenciaDocente.append(itemAux.getClfAsistenciaDocente1());
						frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());
						break;
					}
					frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes + "/cabeceraNotas.png");
					frmAdjuntoParametros.put("imagenPie", pathGeneralReportes + "/pieNotas.png");

					frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
					Map<String, Object> datoEstudiantes = null;

					// *********************************************************//
					// ******PARAMETROS PARA RECTIFICACION 1ER
					// HEMISEMESTR******//
					// *********************************************************//
					int cont1 = 1;
					if (nrctfTipoRectificacion == 1) {
						for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
							if (itemAux.getClfNota1() != null) {
								String nota = itemAux.getClfNota1().toString();
								int puntoDecimalUbc = nota.indexOf('.');
								int totalDecimales = nota.length() - puntoDecimalUbc - 1;
								if (puntoDecimalUbc == -1) {
									itemAux.setClfNota1String(nota + ".00");
								} else if (totalDecimales == 1) {
									itemAux.setClfNota1String(nota + "0");
								} else {
									itemAux.setClfNota1String(nota);
								}
							}
							StringBuilder sbNota1 = new StringBuilder();
							StringBuilder sbAsistencia1 = new StringBuilder();
							StringBuilder sbContador1 = new StringBuilder();
							datoEstudiantes = new HashMap<String, Object>();
							datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
							datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
							datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
							datoEstudiantes.put("nombres", itemAux.getPrsNombres());
							if (itemAux.getClfNota1() != null) {
								sbNota1.append(itemAux.getClfNota1String());
								datoEstudiantes.put("nota1", sbNota1.toString());
							} else {
								sbNota1.append("-");
								datoEstudiantes.put("nota1", sbNota1.toString());
							}
							if (itemAux.getClfAsistenciaEstudiante1() != null) {
								sbAsistencia1.append(itemAux.getClfAsistenciaEstudiante1());
								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
							} else {
								sbAsistencia1.append("-");
								datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
							}
							sbContador1.append(cont1);
							datoEstudiantes.put("numero", sbContador1.toString());

							frmAdjuntoCampos.add(datoEstudiantes);
							cont1 = cont1 + 1;
						}
					}
					// *********************************************************//
					// ******PARAMETROS PARA RECTIFICACION 2do
					// HEMISEMESTR******//
					// *********************************************************//
					int cont2 = 1;
					if (nrctfTipoRectificacion == 2) {
						for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
							StringBuilder sbNota2 = new StringBuilder();
							StringBuilder sbAsistencia2 = new StringBuilder();
							StringBuilder sbContador2 = new StringBuilder();
							datoEstudiantes = new HashMap<String, Object>();
							datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
							datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
							datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
							datoEstudiantes.put("nombres", itemAux.getPrsNombres());
							if (itemAux.getClfNota1() != null) {
								sbNota2.append(itemAux.getClfNota2());
								datoEstudiantes.put("nota1", sbNota2.toString());
							} else {
								sbNota2.append("-");
								datoEstudiantes.put("nota1", sbNota2.toString());
							}
							if (itemAux.getClfAsistenciaEstudiante2() != null) {
								sbAsistencia2.append(itemAux.getClfAsistenciaEstudiante2());
								datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
							} else {
								sbAsistencia2.append("-");
								datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
							}
							sbContador2.append(cont2);
							datoEstudiantes.put("numero", sbContador2.toString());

							frmAdjuntoCampos.add(datoEstudiantes);
							cont2 = cont2 + 1;
						}
					}
					// *********************************************************//
					// ******PARAMETROS PARA RECTIFICACION 2do
					// HEMISEMESTR******//
					// *********************************************************//
					int cont3 = 1;
					if (nrctfTipoRectificacion == 3) {
						for (EstudianteJdbcDto itemAux : nrctfListEstudianteBusq) {
							StringBuilder sbNota3 = new StringBuilder();
							StringBuilder sbContador3 = new StringBuilder();
							datoEstudiantes = new HashMap<String, Object>();
							datoEstudiantes.put("identificacion", itemAux.getPrsIdentificacion());
							datoEstudiantes.put("apellido_paterno", itemAux.getPrsPrimerApellido());
							datoEstudiantes.put("apellido_materno", itemAux.getPrsSegundoApellido());
							datoEstudiantes.put("nombres", itemAux.getPrsNombres());
							if (itemAux.getClfNota1() != null) {
								sbNota3.append(itemAux.getClfNota2());
								datoEstudiantes.put("nota1", sbNota3.toString());
							} else {
								sbNota3.append("-");
								datoEstudiantes.put("nota1", sbNota3.toString());
							}
							sbContador3.append(cont3);
							datoEstudiantes.put("numero", sbContador3.toString());

							frmAdjuntoCampos.add(datoEstudiantes);
							cont3 = cont3 + 1;
						}
					}

					JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);

					jasperPrint = JasperFillManager.fillReport(jasperReport, frmAdjuntoParametros, dataSource);

					byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
					// AdjuntoDto adjuntoDto = new AdjuntoDto();
					// adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
					// adjuntoDto.setAdjunto(arreglo);

					// lista de correos a los que se enviara el mail
					List<String> correosTo = new ArrayList<>();
					correosTo.add(nrctfDocente.getPrsMailInstitucional());

					// lista de correos copia a los que se enviara el mail al
					// director de carrera
					// List<String> correosCc = new ArrayList<>();
					// correosCc.add(nrctfDirCarrera.getPrsMailInstitucional());

					// path de la plantilla del mail
					ProductorMailJson pmail = null;
					StringBuilder sbCorreo = new StringBuilder();
					// path de la plantilla del mail para el director de carrera
					ProductorMailJson pmailDirCarrera = null;
					StringBuilder sbCorreoDirCarrera = new StringBuilder();

					try {
						sbCorreo = GeneralesUtilidades.generarAsuntoRectificacionNotas(
								GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
								nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido() + " "
										+ nrctfDocente.getPrsNombres(),
								GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
								nrctfNomParalelo, nrctfNomNotaRectificacion);
					} catch (Exception e) {
						sbCorreo = GeneralesUtilidades.generarAsuntoRectificacionNotas(
								GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
								nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsNombres(),
								GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
								nrctfNomParalelo, nrctfNomNotaRectificacion);
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

					try {
						sbCorreoDirCarrera = GeneralesUtilidades.generarAsuntoRectificacionNotasDirCarrera(
								(GeneralesUtilidades.generaStringParaCorreo(fecha.toString())),
								nrctfDirCarrera.getPrsPrimerApellido() + " " + nrctfDirCarrera.getPrsSegundoApellido()
										+ " " + nrctfDirCarrera.getPrsNombres(),
								nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsSegundoApellido() + " "
										+ nrctfDocente.getPrsNombres(),
								GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
								nrctfNomNivel, nrctfNomParalelo, nrctfNomNotaRectificacion);
					} catch (Exception e) {
						sbCorreoDirCarrera = GeneralesUtilidades.generarAsuntoRectificacionNotasDirCarrera(
								(GeneralesUtilidades.generaStringParaCorreo(fecha.toString())),
								nrctfDirCarrera.getPrsPrimerApellido() + " " + nrctfDirCarrera.getPrsSegundoApellido()
										+ " " + nrctfDirCarrera.getPrsNombres(),
								nrctfDocente.getPrsPrimerApellido() + " " + nrctfDocente.getPrsNombres(),
								GeneralesUtilidades.generaStringParaCorreo(nrctfNomCarrera), nrctfNomMateria,
								nrctfNomNivel, nrctfNomParalelo, nrctfNomNotaRectificacion);
					}

					pmailDirCarrera = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
							GeneralesConstantes.APP_REGISTRO_NOTAS, sbCorreoDirCarrera.toString(), "admin", "dt1c201s",
							true, arreglo, "RegistroNotas." + MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF);
					String jsonStDirCarrera = pmailDirCarrera.generarMail();
					Gson jsonDirCarrera = new Gson();
					MailDto mailDtoDirCarrera = jsonDirCarrera.fromJson(jsonStDirCarrera, MailDto.class);
					// Iniciamos el envío de mensajes
					ObjectMessage messageDirCarrera = session.createObjectMessage(mailDtoDirCarrera);
					producer.send(messageDirCarrera);

				} catch (JMSException e) {
				} catch (JRException e) {
				} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
				}
			}

		} catch (RecordEstudianteDtoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());

		} catch (RecordEstudianteDtoNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "irListarParalelos";
	}

	public boolean activacion() {
		boolean retorno = false;

		for (EstudianteJdbcDto item : nrctfListEstudianteBusq) {
			int aux = item.getRcesIngersoNota2();
			if (aux == 2) {
				retorno = true;
				break;
			}

		}
		return retorno;
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
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Sólo números y signos decimales");
			return false;
		}
		retorno = true;
		return retorno;
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
		opModulo = false;
		nrctfEstudianteBuscar = null;
		// nrctfListCarreraBusq = null;
		nrctfListNivelBusq = null;
		nrctfListMateriaBusq = null;
		nrctfListNivelxCarreraDocenteBusq = null;
		nrctfListMateriasxCarreraDocenteBusq = null;
		nrctfValidadorClic = 0;
		// INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		nrctfEstudianteBuscar = new EstudianteJdbcDto();
		// //INICIALIZO EL PERIODO ACADEMICO ID
		nrctfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// //INICIALIZO LA CARRERA ID
		nrctfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		// //INICIALIZO EL NIVEL ID
		nrctfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		// //INICIALIZO EL PARALELO ID
		// nrctfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		// //INICIALIZO LA MATERIA ID
		nrctfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		// //ANULO LA LISTA DE NIVEL
		// nrctfListNivelBusq = null;
		// ANULO LA LISTA DE PARALELOS
		nrctfListParaleloBusq = null;
		// //ANULO LA LISTA DE MATERIAS
		// nrctfListMateriaBusq = null;

	}

	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada() {
	}

	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/
	public Usuario getNrctfUsuario() {
		return nrctfUsuario;
	}

	public void setNrctfUsuario(Usuario nrctfUsuario) {
		this.nrctfUsuario = nrctfUsuario;
	}

	public DocenteJdbcDto getNrctfDocente() {
		return nrctfDocente;
	}

	public void setNrctfDocente(DocenteJdbcDto nrctfDocente) {
		this.nrctfDocente = nrctfDocente;
	}

	public EstudianteJdbcDto getNrctfEstudianteBuscar() {
		return nrctfEstudianteBuscar;
	}

	public void setNrctfEstudianteBuscar(EstudianteJdbcDto nrctfEstudianteBuscar) {
		this.nrctfEstudianteBuscar = nrctfEstudianteBuscar;
	}

	public List<CarreraDto> getNrctfListCarreraBusq() {
		nrctfListCarreraBusq = nrctfListCarreraBusq == null ? (new ArrayList<CarreraDto>()) : nrctfListCarreraBusq;
		return nrctfListCarreraBusq;
	}

	public void setNrctfListCarreraBusq(List<CarreraDto> nrctfListCarreraBusq) {
		this.nrctfListCarreraBusq = nrctfListCarreraBusq;
	}

	public List<NivelDto> getNrctfListNivelBusq() {
		nrctfListNivelBusq = nrctfListNivelBusq == null ? (new ArrayList<NivelDto>()) : nrctfListNivelBusq;
		return nrctfListNivelBusq;
	}

	public void setNrctfListNivelBusq(List<NivelDto> nrctfListNivelBusq) {
		this.nrctfListNivelBusq = nrctfListNivelBusq;
	}

	public List<MateriaDto> getNrctfListMateriaBusq() {
		nrctfListMateriaBusq = nrctfListMateriaBusq == null ? (new ArrayList<MateriaDto>()) : nrctfListMateriaBusq;
		return nrctfListMateriaBusq;
	}

	public void setNrctfListMateriaBusq(List<MateriaDto> nrctfListMateriaBusq) {
		this.nrctfListMateriaBusq = nrctfListMateriaBusq;
	}

	public List<EstudianteJdbcDto> getNrctfListEstudianteBusq() {
		nrctfListEstudianteBusq = nrctfListEstudianteBusq == null ? (new ArrayList<EstudianteJdbcDto>())
				: nrctfListEstudianteBusq;
		return nrctfListEstudianteBusq;
	}

	public void setNrctfListEstudianteBusq(List<EstudianteJdbcDto> nrctfListEstudianteBusq) {
		this.nrctfListEstudianteBusq = nrctfListEstudianteBusq;
	}

	public List<ParaleloDto> getNrctfListParaleloBusq() {
		nrctfListParaleloBusq = nrctfListParaleloBusq == null ? (new ArrayList<ParaleloDto>()) : nrctfListParaleloBusq;
		return nrctfListParaleloBusq;
	}

	public void setNrctfListParaleloBusq(List<ParaleloDto> nrctfListParaleloBusq) {
		this.nrctfListParaleloBusq = nrctfListParaleloBusq;
	}

	public ParaleloDto getNrctfParaleloDtoEditar() {
		return nrctfParaleloDtoEditar;
	}

	public void setNrctfParaleloDtoEditar(ParaleloDto nrctfParaleloDtoEditar) {
		nrctfListMateriaBusq = nrctfListMateriaBusq == null ? (new ArrayList<MateriaDto>()) : nrctfListMateriaBusq;
		this.nrctfParaleloDtoEditar = nrctfParaleloDtoEditar;
	}

	public Integer getNrctfAsistenciaDocente() {
		return nrctfAsistenciaDocente;
	}

	public void setNrctfAsistenciaDocente(Integer nrctfAsistenciaDocente) {
		this.nrctfAsistenciaDocente = nrctfAsistenciaDocente;
	}

	public Integer getNrctfValidadorClic() {
		return nrctfValidadorClic;
	}

	public void setNrctfValidadorClic(Integer nrctfValidadorClic) {
		this.nrctfValidadorClic = nrctfValidadorClic;
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

	public String getNrctfRegCliente() {
		return nrctfRegCliente;
	}

	public void setNrctfRegCliente(String nrctfRegCliente) {
		this.nrctfRegCliente = nrctfRegCliente;
	}

	public String getNrctfEstado() {
		return nrctfEstado;
	}

	public void setNrctfEstado(String nrctfEstado) {
		this.nrctfEstado = nrctfEstado;
	}

	public String getNrctfNomCarrera() {
		return nrctfNomCarrera;
	}

	public void setNrctfNomCarrera(String nrctfNomCarrera) {
		this.nrctfNomCarrera = nrctfNomCarrera;
	}

	public String getNrctfNomMateria() {
		return nrctfNomMateria;
	}

	public void setNrctfNomMateria(String nrctfNomMateria) {
		this.nrctfNomMateria = nrctfNomMateria;
	}

	public String getNrctfNomParalelo() {
		return nrctfNomParalelo;
	}

	public void setNrctfNomParalelo(String nrctfNomParalelo) {
		this.nrctfNomParalelo = nrctfNomParalelo;
	}

	public List<RolFlujoCarrera> getNrctfListRolFlujoCarreraBusq() {
		nrctfListRolFlujoCarreraBusq = nrctfListRolFlujoCarreraBusq == null ? (new ArrayList<RolFlujoCarrera>())
				: nrctfListRolFlujoCarreraBusq;
		return nrctfListRolFlujoCarreraBusq;
	}

	public void setNrctfListRolFlujoCarreraBusq(List<RolFlujoCarrera> nrctfListRolFlujoCarreraBusq) {
		this.nrctfListRolFlujoCarreraBusq = nrctfListRolFlujoCarreraBusq;
	}

	public Dependencia getNrctfDependenciaBuscar() {
		return nrctfDependenciaBuscar;
	}

	public void setNrctfDependenciaBuscar(Dependencia nrctfDependenciaBuscar) {
		this.nrctfDependenciaBuscar = nrctfDependenciaBuscar;
	}

	public Integer getNrctfTipoNota() {
		return nrctfTipoNota;
	}

	public void setNrctfTipoNota(Integer nrctfTipoNota) {
		this.nrctfTipoNota = nrctfTipoNota;
	}

	public String getNrctfNombreArchivoAuxiliar() {
		return nrctfNombreArchivoAuxiliar;
	}

	public void setNrctfNombreArchivoAuxiliar(String nrctfNombreArchivoAuxiliar) {
		this.nrctfNombreArchivoAuxiliar = nrctfNombreArchivoAuxiliar;
	}

	public String getNrctfNombreArchivoSubido() {
		return nrctfNombreArchivoSubido;
	}

	public void setNrctfNombreArchivoSubido(String nrctfNombreArchivoSubido) {
		this.nrctfNombreArchivoSubido = nrctfNombreArchivoSubido;
	}

	public CronogramaActividadJdbcDto getNrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico() {
		return nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico;
	}

	public void setNrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico(
			CronogramaActividadJdbcDto nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico) {
		this.nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico = nrctfCronogramaActividadJdbcDtoBuscarFechaFinPresentePeriodoAcademico;
	}

	public CronogramaActividadJdbcDto getNrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico() {
		return nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	}

	public void setNrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico(
			CronogramaActividadJdbcDto nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico) {
		this.nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico = nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	}

	public Integer getNrctfTipoRectificacion() {
		return nrctfTipoRectificacion;
	}

	public void setNrctfTipoRectificacion(Integer nrctfTipoRectificacion) {
		this.nrctfTipoRectificacion = nrctfTipoRectificacion;
	}

	public List<EstudianteJdbcDto> getNrctfListEstudianteEditar() {
		nrctfListEstudianteEditar = nrctfListEstudianteEditar == null ? (new ArrayList<EstudianteJdbcDto>())
				: nrctfListEstudianteEditar;
		return nrctfListEstudianteEditar;
	}

	public void setNrctfListEstudianteEditar(List<EstudianteJdbcDto> nrctfListEstudianteEditar) {
		this.nrctfListEstudianteEditar = nrctfListEstudianteEditar;
	}

	public PersonaDto getNrctfDirCarrera() {
		return nrctfDirCarrera;
	}

	public void setNrctfDirCarrera(PersonaDto nrctfDirCarrera) {
		this.nrctfDirCarrera = nrctfDirCarrera;
	}

	public String getNrctfNomNivel() {
		return nrctfNomNivel;
	}

	public void setNrctfNomNivel(String nrctfNomNivel) {
		this.nrctfNomNivel = nrctfNomNivel;
	}

	public String getNrctfNomNotaRectificacion() {
		return nrctfNomNotaRectificacion;
	}

	public void setNrctfNomNotaRectificacion(String nrctfNomNotaRectificacion) {
		this.nrctfNomNotaRectificacion = nrctfNomNotaRectificacion;
	}

	public List<DocenteJdbcDto> getNrctfListCarreraDocenteBusq() {
		nrctfListCarreraDocenteBusq = nrctfListCarreraDocenteBusq == null ? (new ArrayList<DocenteJdbcDto>())
				: nrctfListCarreraDocenteBusq;
		return nrctfListCarreraDocenteBusq;
	}

	public void setNrctfListCarreraDocenteBusq(List<DocenteJdbcDto> nrctfListCarreraDocenteBusq) {
		this.nrctfListCarreraDocenteBusq = nrctfListCarreraDocenteBusq;
	}

	public List<DocenteJdbcDto> getNrctfListNivelxCarreraDocenteBusq() {
		nrctfListNivelxCarreraDocenteBusq = nrctfListNivelxCarreraDocenteBusq == null
				? (new ArrayList<DocenteJdbcDto>()) : nrctfListNivelxCarreraDocenteBusq;
		return nrctfListNivelxCarreraDocenteBusq;
	}

	public void setNrctfListNivelxCarreraDocenteBusq(List<DocenteJdbcDto> nrctfListNivelxCarreraDocenteBusq) {
		this.nrctfListNivelxCarreraDocenteBusq = nrctfListNivelxCarreraDocenteBusq;
	}

	public List<DocenteJdbcDto> getNrctfListMateriasxCarreraDocenteBusq() {
		nrctfListMateriasxCarreraDocenteBusq = nrctfListMateriasxCarreraDocenteBusq == null
				? (new ArrayList<DocenteJdbcDto>()) : nrctfListMateriasxCarreraDocenteBusq;
		return nrctfListMateriasxCarreraDocenteBusq;
	}

	public void setNrctfListMateriasxCarreraDocenteBusq(List<DocenteJdbcDto> nrctfListMateriasxCarreraDocenteBusq) {
		this.nrctfListMateriasxCarreraDocenteBusq = nrctfListMateriasxCarreraDocenteBusq;
	}

	public CronogramaActividadJdbcDto getNrctfFechaPracCierreFinPresentePeriodoAcademico() {
		return nrctfFechaPracCierreFinPresentePeriodoAcademico;
	}

	public void setNrctfFechaPracCierreFinPresentePeriodoAcademico(
			CronogramaActividadJdbcDto nrctfFechaPracCierreFinPresentePeriodoAcademico) {
		this.nrctfFechaPracCierreFinPresentePeriodoAcademico = nrctfFechaPracCierreFinPresentePeriodoAcademico;
	}

	public CronogramaActividadJdbcDto getNrctfFechaPracCierreInicioSigPeriodoAcademico() {
		return nrctfFechaPracCierreInicioSigPeriodoAcademico;
	}

	public void setNrctfFechaPracCierreInicioSigPeriodoAcademico(
			CronogramaActividadJdbcDto nrctfFechaPracCierreInicioSigPeriodoAcademico) {
		this.nrctfFechaPracCierreInicioSigPeriodoAcademico = nrctfFechaPracCierreInicioSigPeriodoAcademico;
	}

	public List<PeriodoAcademico> getNrctfListPeriodoAcademico() {
		nrctfListPeriodoAcademico = nrctfListPeriodoAcademico == null ? (new ArrayList<PeriodoAcademico>())
				: nrctfListPeriodoAcademico;
		return nrctfListPeriodoAcademico;
	}

	public void setNrctfListPeriodoAcademico(List<PeriodoAcademico> nrctfListPeriodoAcademico) {
		this.nrctfListPeriodoAcademico = nrctfListPeriodoAcademico;
	}

	public boolean isOpModulo() {
		return opModulo;
	}

	public void setOpModulo(boolean opModulo) {
		this.opModulo = opModulo;
	}

	public PeriodoAcademicoDto getNrctfPeriodoAcademicoBusq() {
		return nrctfPeriodoAcademicoBusq;
	}

	public void setNrctfPeriodoAcademicoBusq(PeriodoAcademicoDto nrctfPeriodoAcademicoBusq) {
		this.nrctfPeriodoAcademicoBusq = nrctfPeriodoAcademicoBusq;
	}

	public PeriodoAcademico getPracCierre() {
		return pracCierre;
	}

	public void setPracCierre(PeriodoAcademico pracCierre) {
		this.pracCierre = pracCierre;
	}

	public Materia getMtrAux() {
		return mtrAux;
	}

	public void setMtrAux(Materia mtrAux) {
		this.mtrAux = mtrAux;
	}

	public boolean isPermisoEdicionPeridoAnterior() {
		return permisoEdicionPeridoAnterior;
	}

	public void setPermisoEdicionPeridoAnterior(boolean permisoEdicionPeridoAnterior) {
		this.permisoEdicionPeridoAnterior = permisoEdicionPeridoAnterior;
	}

}
