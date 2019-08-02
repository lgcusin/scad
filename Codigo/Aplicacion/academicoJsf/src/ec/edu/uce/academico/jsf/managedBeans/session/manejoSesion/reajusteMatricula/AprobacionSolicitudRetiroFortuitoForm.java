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
   
 ARCHIVO:     AprobacionSolicitudRetiroFortuitoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la aprobacion/negacion hecha por el Consejo Directivo a la solicitud de retiro por situaciones fortuitas de asignaturas  realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-12-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reajusteMatricula;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteAprobacionRetiroFortuitoForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (session bean) AprobacionSolicitudRetiroFortuitoForm. Bean de sesion
 * que maneja la la aprobación/negación hecha por el Consejo Directivo a la a la
 * solicitud de retiro por situaciones fortuitas de asignaturas realizada por el
 * estudiante..
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "aprobacionSolicitudRetiroFortuitoForm")
@SessionScoped
public class AprobacionSolicitudRetiroFortuitoForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/

	// GENERAL
	private Usuario asrffUsuario;
	// PARA BUSQUEDA
	private PeriodoAcademico asrffPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> asrffListaPeriodoAcademicoBusq;
	private PeriodoAcademico asrffPeriodoAcademicoActivo;
	private CarreraDto asrffCarreraDtoBuscar;
	private List<CarreraDto> asrffListCarreraDtoBusq;
	private EstudianteJdbcDto asrffEstudianteBuscar;
	// PARA VISUALIZACIÓN
	// LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD Y HA SIDO VERIFICADA
	private List<FichaMatriculaDto> asrffListaSolicitudesDto;
	private FichaMatriculaDto asrffSolicitudesEstudiante;

	// VER MATERIAS
	private Persona asrffPersonaSeleccionadaVer;
	private PeriodoAcademico asrffPeriodoSolicitudesVer;
	private Carrera asrffCarreraVer;
	// private FichaInscripcionDto asrffFichaInscripcion;
	private List<EstudianteJdbcDto> asrffListaMateriasSolicitadasDto; // lista de materias
	private Integer asrffValidadorSeleccion;
	private Integer asrffClickGuardarResolver;
	// private Integer asrffTipoCronograma;
	private Dependencia asrffDependencia;
	// private CronogramaActividadJdbcDto asrffCronogramaActividad;
	private String asrffArchivoSelSt;
	private List<EstudianteJdbcDto> asrffListMateriasTodas; // Para contar todas las asignaturas
	private List<EstudianteJdbcDto> asrffListMateriasInactivas; // Para contar asignaturas inactivas
	private boolean asrffRetiroMatriculaTotal;

	// MODAL SUBIR RESOLUCION
	private Integer asrffActivaModalCargarResolucion = 1;
	private EstudianteJdbcDto asrffMateriaSeleccionada;
	private Boolean asrffDesactivaCargarDoc;

	private String asrffNombreArchivoSubido;
	private String asrffNombreArchivoAuxiliar;

	private Integer asrffActivaReporteFinal; // activa el modal para presenar el reporte
	private Boolean asrffDesactivaBotonReporte; // desactiva el boton reporte
	private Boolean asrffDesactivaBotonGuardar; // desactiva el boton Guardar

	// PARA EL REPORTE
	private String asrffObservacionFinal;
	private List<EstudianteJdbcDto> asrffListaMateriasReporte;
	private String asrffObservacionReporte;

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
	PeriodoAcademicoServicio servAsrffPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servAsrffCarreraDtoServicioJdbc;
	@EJB
	EstudianteDtoServicioJdbc servAsrffEstudianteDtoServicioJdbc;
	@EJB
	PersonaServicio servAsrffPersonaServicio;
	@EJB
	CarreraServicio servAsrffCarreraServicio;
	@EJB
	DependenciaServicio servAsrffDependencialServicio;
	@EJB
	FichaMatriculaDtoServicioJdbc servAsrffFichaMatriculaDtoServicioJdbc;
	@EJB
	MatriculaServicio servAsrffMatriculaServicio;

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/

	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * 
	 * @param usuario
	 *            - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irListarEstudiantesResolvRetiroFortuito(Usuario usuario) {
		asrffUsuario = usuario;
		String retorno = null;
		try {
			// INICIO PARAMETROS
			iniciarParametros();
			// BUSCA EL PERIODO ACADEMICO ACTIVO
			asrffPeriodoAcademicoActivo = servAsrffPeriodoAcademicoServicio
					.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			asrffListaPeriodoAcademicoBusq = servAsrffPeriodoAcademicoServicio.listarTodos();
			// BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			asrffListCarreraDtoBusq = servAsrffCarreraDtoServicioJdbc
					.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(asrffUsuario.getUsrId(),
							RolConstantes.ROL_SECREABOGADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE,
							asrffPeriodoAcademicoActivo.getPracId());
			retorno = "irAListarEstudiantesResolvRetiroFortuito";
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}

	/**
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron
	 * retiro fortuito y se ha verificado con los parámetros ingresados
	 */
	public void buscarEstudiantes() {
		asrffListaSolicitudesDto = new ArrayList<>();
		try {
			// Verifico que se seleecione una carrera
			if (asrffCarreraDtoBuscar.getCrrId() != GeneralesConstantes.APP_ID_BASE) {

				// CREAR METODO para buscar estudiantes verificados
				asrffListaSolicitudesDto = servAsrffFichaMatriculaDtoServicioJdbc
						.buscarSolicitudRetiroFortuitoXPeriodoXapellidoXidentificacionXcarrera(
								GeneralesConstantes.APP_ID_BASE, asrffListCarreraDtoBusq,
								asrffCarreraDtoBuscar.getCrrId(), asrffEstudianteBuscar.getPrsIdentificacion(),
								asrffEstudianteBuscar.getPrsPrimerApellido(),
								DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE);

			} else {
				asrffListaSolicitudesDto = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades(
						"AprobacionSolicitudRetiroFortuito.buscarEstudiantes.seleccionar.carrera.validacion")));
			}
		} catch (FichaMatriculaException e) {
			asrffListaSolicitudesDto = null;
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método que permite ir a la visualización de las materias
	 * 
	 * @param solicitudesEstudiante
	 *            - solicitudesEstudiante objeto que se envia como parámetro
	 *            para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerMateriasSolicitadas(FichaMatriculaDto solicitudesEstudiante) {
		String retorno = null;
		asrffListaMateriasSolicitadasDto = new ArrayList<>();

		asrffPersonaSeleccionadaVer = new Persona();
		asrffPeriodoSolicitudesVer = new PeriodoAcademico();
		asrffCarreraVer = new Carrera();
		asrffObservacionFinal = null;
		asrffSolicitudesEstudiante = new FichaMatriculaDto();
		asrffSolicitudesEstudiante = solicitudesEstudiante;
		asrffActivaReporteFinal = 0;

		// Se busca el periodo, persona, carrera y lista de materias en las que
		// solicito y verifico la solicitud
		try {
			asrffPeriodoSolicitudesVer = servAsrffPeriodoAcademicoServicio
					.buscarPorId(solicitudesEstudiante.getPracId());
			asrffPersonaSeleccionadaVer = servAsrffPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			asrffCarreraVer = servAsrffCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());

			// LLENO LISTA DE TODAS LAS ASIGNATURAS
			asrffListMateriasTodas = servAsrffEstudianteDtoServicioJdbc.buscarEstudianteXIdPersonaXIdMatricula(
					solicitudesEstudiante.getPrsId(), solicitudesEstudiante.getFcmtId(),
					solicitudesEstudiante.getPracId());
			// LLENO LA LISTA DE ASIGNATURAS INACTIVAS  (APROBADAS SOLICITUD DE RETIRO Y RETIRO FORTUITO)
			asrffListMateriasInactivas = servAsrffEstudianteDtoServicioJdbc
					.buscarEstudianteMateriaInactivasRetiroAprobadoXIdEstudianteXIdMatriculaXidPeriodoxEstados(
							solicitudesEstudiante.getPrsId(), solicitudesEstudiante.getFcmtId(),
							solicitudesEstudiante.getPracId(), DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE,
							GeneralesConstantes.APP_ID_BASE
							);
			
			//LISTA DE ASIGNATURAS QUE ESTAN EN ESTADO VERIFICADO LA SOLICITUD DE RETIRO FORTUITO
			asrffListaMateriasSolicitadasDto = servAsrffEstudianteDtoServicioJdbc
					.buscarAsignaturasAprobarRetiroFortuitoXprsIdXmtrIdXpracIdXdtmtEstado(
							solicitudesEstudiante.getPrsId(), solicitudesEstudiante.getFcmtId(),
							solicitudesEstudiante.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);

			// Seteo como activo la opción de descarga del archivo
			for (EstudianteJdbcDto item : asrffListaMateriasSolicitadasDto) {
				item.setVisualizador(false);
				item.setEstadoSolicitudRetiro(GeneralesConstantes.APP_ID_BASE);

			}

			retorno = "irVerMateriasSolicitadas";
		} catch (PeriodoAcademicoNoEncontradoException e) {
			asrffPeriodoSolicitudesVer = null;
			asrffPersonaSeleccionadaVer = null;
			asrffListaMateriasSolicitadasDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			asrffPeriodoSolicitudesVer = null;
			asrffPersonaSeleccionadaVer = null;
			asrffListaMateriasSolicitadasDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			asrffPeriodoSolicitudesVer = null;
			asrffPersonaSeleccionadaVer = null;
			asrffListaMateriasSolicitadasDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			asrffPeriodoSolicitudesVer = null;
			asrffPersonaSeleccionadaVer = null;
			asrffListaMateriasSolicitadasDto = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}

	/**
	 * Método que guarda la respuesta a la solicitud de retiro de las materias
	 * 
	 * @return retorna - la navegación de la página listar estudiantes.
	 */
	public void guardarResolver() {

		// asrffFichaInscripcion= new FichaInscripcionDto();
		asrffListaMateriasReporte = new ArrayList<>();

		if (asrffListaMateriasSolicitadasDto != null && asrffListaMateriasSolicitadasDto.size() > 0) { // verifica que la lista no este nula proceso de transacción
			String rutaNombre = null;
			String rutaTemporal = null;

			try {

				// FALTA COLOCAR UNA VARIABLE EN LUGAR DEL FALSE, cuando se
				// retira de todas las materias
				if (servAsrffMatriculaServicio.aprobarSolicitudRetiroFortuitoMatricula(asrffListaMateriasSolicitadasDto,
						asrffRetiroMatriculaTotal, asrffSolicitudesEstudiante.getFcmtId(), asrffUsuario,
						asrffObservacionFinal)) {
					// Si grabo bien en base procedo a grabar los archivos pdf
					// desde el cache de la maquina al servidor de archivos.
					for (EstudianteJdbcDto item : asrffListaMateriasSolicitadasDto) {
						String extension = GeneralesUtilidades.obtenerExtension(item.getDtmtArchivoRespuesta());
						rutaNombre = DetalleMatriculaConstantes.DTMT_ARCHIVO_RESOLUCION_RETIRO_FORTUITO_LABEL + "-"	+ item.getDtmtId() + "." + extension;
						rutaTemporal = System.getProperty("java.io.tmpdir") + File.separator+ item.getDtmtArchivoRespuesta();
						GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS+ GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE + rutaNombre);
						rutaNombre = null;
						rutaTemporal = null;
					}

					// copio las lista para el reporte PDF
					for (EstudianteJdbcDto solicitudDto : asrffListaMateriasSolicitadasDto) {
						asrffListaMateriasReporte.add(solicitudDto);
					}

					try {
						// ******************************************************************************
						// ************************* ACA INICIA EL ENVIO DE MAIL ************************
						// ******************************************************************************
						// abrir conexion con server mail
						Connection connection = null;
						Session session = null;
						MessageProducer producer = null;
						ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
								"nio://10.20.1.64:61616");
						connection = connectionFactory.createConnection();
						connection.start();
						session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						Destination destino = session.createQueue("COLA_MAIL_DTO");
						// Creamos un productor
						producer = session.createProducer(destino);
						// fin abrir conexion server mail

						// Defino variables para el reporte
						JasperReport jasperReport = null;
						JasperPrint jasperPrint;
						List<Map<String, Object>> frmRrmCampos = null;
						Map<String, Object> frmRrmParametros = null;
						String frmRrmNombreReporte = null;
						List<Causal> listaCausalaux = new ArrayList<>();
						// ****************************************************************//
						// ********* GENERACION DEL REPORTE *********//
						// ****************************************************************//
						// ****************************************************************//

						if (asrffPersonaSeleccionadaVer != null) {
							frmRrmNombreReporte = asrffPersonaSeleccionadaVer.getPrsNombres() + " "
									+ asrffPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase() + " "
									+ (asrffPersonaSeleccionadaVer.getPrsSegundoApellido() == null ? " "
											: asrffPersonaSeleccionadaVer.getPrsSegundoApellido());
						} else {
							frmRrmNombreReporte = "Reporte-Aprobacion";
						}

						frmRrmParametros = new HashMap<String, Object>();
						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy",
								new Locale("es", "ES"));
						// SimpleDateFormat formato = new SimpleDateFormat("d
						// 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
						String fecha = formato.format(new Date());
						frmRrmParametros.put("fecha", fecha);

						String nombres = null;
						if (asrffPersonaSeleccionadaVer != null) {
							nombres = asrffPersonaSeleccionadaVer.getPrsNombres() + " "
									+ asrffPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase() + " "
									+ (asrffPersonaSeleccionadaVer.getPrsSegundoApellido() == null ? " "
											: asrffPersonaSeleccionadaVer.getPrsSegundoApellido());
						} else {
							nombres = " ";
						}

						frmRrmParametros.put("nombre", nombres);

						StringBuilder sbTextoInicial = new StringBuilder();
						sbTextoInicial.append("Señor(a)(ita) ");
						sbTextoInicial.append("\n");
						sbTextoInicial.append(nombres);
						sbTextoInicial.append("\n");
						sbTextoInicial.append("ESTUDIANTE DE LA CARRERA DE ");
						if (asrffCarreraVer.getCrrDescripcion() != null) {
							sbTextoInicial.append(asrffCarreraVer.getCrrDescripcion());
							sbTextoInicial.append("\n");
						} else {
							sbTextoInicial.append(" ");
							sbTextoInicial.append("\n");
						}

						sbTextoInicial.append("FACULTAD DE ");
						if (asrffCarreraVer.getCrrDependencia().getDpnDescripcion() != null) {
							sbTextoInicial.append(asrffCarreraVer.getCrrDependencia().getDpnDescripcion());
							sbTextoInicial.append("\n");
						} else {
							sbTextoInicial.append(" ");
							sbTextoInicial.append("\n");
						}
						sbTextoInicial.append("Presente.-");
						sbTextoInicial.append("\n\n");
						frmRrmParametros.put("textoInicial", sbTextoInicial.toString());

						StringBuilder sbTexto = new StringBuilder();
						sbTexto.append("Una vez analizada(s) y validada(s) la(s) solicitud(es) de retiro de asignatura(s) por situaciones fortuitas o de fuerza mayor y considerando la normativa vigente, "
								+ "a continuación se detalla el informe final de acuerdo a la resolución del Consejo Directivo de la Facultad:");
						frmRrmParametros.put("texto", sbTexto.toString());

						StringBuilder sbPeriodo = new StringBuilder();
						StringBuilder sbCodigo = new StringBuilder();
						StringBuilder sbAsignatura = new StringBuilder();
						StringBuilder sbHora = new StringBuilder();
						StringBuilder sbCausal = new StringBuilder();
						StringBuilder sbEvidencia = new StringBuilder();
						StringBuilder sbEstado = new StringBuilder();

						for (EstudianteJdbcDto item : asrffListaMateriasSolicitadasDto) {
							if (item.getMtrDescripcion().length() <= 44) {
								if (item.getPracDescripcion() != null) {
									sbPeriodo.append(item.getPracDescripcion());
									sbPeriodo.append("\n\n");
								} else {
									sbPeriodo.append(" ");
									sbPeriodo.append("\n\n");
								}

								if (item.getMtrCodigo() != null) {
									sbCodigo.append(item.getMtrCodigo());
									sbCodigo.append("\n\n");
								} else {
									sbCodigo.append(" ");
									sbCodigo.append("\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());
								sbAsignatura.append("\n\n");

								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n");
								}

								if (item.getDtmtArchivoEstudiantes() != null) {
									sbEvidencia.append(item.getDtmtArchivoEstudiantes());
									sbEvidencia.append("\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n");
								}

								if (item.getEstadoSolicitudRetiro() != null) {
									if (item.getEstadoSolicitudRetiro() == 0) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);
										sbEstado.append("\n\n");
									}
									if (item.getEstadoSolicitudRetiro() == 1) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);
										sbEstado.append("\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n");
								}

							}
							if (item.getMtrDescripcion().length() > 44 && item.getMtrDescripcion().length() <= 88) {

								if (item.getPracDescripcion() != null) {
									sbPeriodo.append(item.getPracDescripcion());
									sbPeriodo.append("\n\n\n");
								} else {
									sbPeriodo.append(" ");
									sbPeriodo.append("\n\n\n");
								}

								if (item.getMtrCodigo() != null) {
									sbCodigo.append(item.getMtrCodigo());
									sbCodigo.append("\n\n\n");
								} else {
									sbCodigo.append(" ");
									sbCodigo.append("\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());
								sbAsignatura.append("\n\n\n");

								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n");
								}

								if (item.getDtmtArchivoEstudiantes() != null) {
									sbEvidencia.append(item.getDtmtArchivoEstudiantes());
									sbEvidencia.append("\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n");
								}

								if (item.getEstadoSolicitudRetiro() != null) {
									if (item.getEstadoSolicitudRetiro() == 0) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);
										sbEstado.append("\n\n\n");
									}
									if (item.getEstadoSolicitudRetiro() == 1) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);
										sbEstado.append("\n\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n\n");
								}

							}
							if (item.getMtrDescripcion().length() > 88 && item.getMtrDescripcion().length() <= 132) {

								if (item.getPracDescripcion() != null) {
									sbPeriodo.append(item.getPracDescripcion());
									sbPeriodo.append("\n\n\n\n");
								} else {
									sbPeriodo.append(" ");
									sbPeriodo.append("\n\n\n\n");
								}

								if (item.getMtrCodigo() != null) {
									sbCodigo.append(item.getMtrCodigo());
									sbCodigo.append("\n\n\n\n");
								} else {
									sbCodigo.append(" ");
									sbCodigo.append("\n\n\n\n");
								}

								sbAsignatura.append(item.getMtrDescripcion());
								sbAsignatura.append("\n\n\n");

								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n");
								}

								if (item.getDtmtArchivoEstudiantes() != null) {
									sbEvidencia.append(item.getDtmtArchivoEstudiantes());
									sbEvidencia.append("\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n");
								}

								if (item.getEstadoSolicitudRetiro() != null) {
									if (item.getEstadoSolicitudRetiro() == 0) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);
										sbEstado.append("\n\n\n\n");
									}
									if (item.getEstadoSolicitudRetiro() == 1) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);
										sbEstado.append("\n\n\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n\n\n");
								}

							}
							if (item.getMtrDescripcion().length() > 132 && item.getMtrDescripcion().length() <= 176) {

								if (item.getPracDescripcion() != null) {
									sbPeriodo.append(item.getPracDescripcion());
									sbPeriodo.append("\n\n\n\n\n");
								} else {
									sbPeriodo.append(" ");
									sbPeriodo.append("\n\n\n\n\n");
								}

								if (item.getMtrCodigo() != null) {
									sbCodigo.append(item.getMtrCodigo());
									sbCodigo.append("\n\n\n\n\n");
								} else {
									sbCodigo.append(" ");
									sbCodigo.append("\n\n\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());
								sbAsignatura.append("\n\n\n");

								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n");
								}

								if (item.getDtmtArchivoEstudiantes() != null) {
									sbEvidencia.append(item.getDtmtArchivoEstudiantes());
									sbEvidencia.append("\n\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n\n");
								}

								if (item.getEstadoSolicitudRetiro() != null) {
									if (item.getEstadoSolicitudRetiro() == 0) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);
										sbEstado.append("\n\n\n\n\n");
									}
									if (item.getEstadoSolicitudRetiro() == 1) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);
										sbEstado.append("\n\n\n\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n\n\n\n");
								}

							}
							if (item.getMtrDescripcion().length() > 176 && item.getMtrDescripcion().length() <= 220) {

								if (item.getPracDescripcion() != null) {
									sbPeriodo.append(item.getPracDescripcion());
									sbPeriodo.append("\n\n\n\n\n\n");
								} else {
									sbPeriodo.append(" ");
									sbPeriodo.append("\n\n\n\n\n\n");
								}

								if (item.getMtrCodigo() != null) {
									sbCodigo.append(item.getMtrCodigo());
									sbCodigo.append("\n\n\n\n\n\n");
								} else {
									sbCodigo.append(" ");
									sbCodigo.append("\n\n\n\n\n\n");
								}
								sbAsignatura.append(item.getMtrDescripcion());
								sbAsignatura.append("\n\n\n");

								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n\n");
								}

								if (item.getDtmtArchivoEstudiantes() != null) {
									sbEvidencia.append(item.getDtmtArchivoEstudiantes());
									sbEvidencia.append("\n\n\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n\n\n");
								}

								if (item.getEstadoSolicitudRetiro() != null) {
									if (item.getEstadoSolicitudRetiro() == 0) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);
										sbEstado.append("\n\n\n\n\n\n");
									}
									if (item.getEstadoSolicitudRetiro() == 1) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);
										sbEstado.append("\n\n\n\n\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n\n\n\n\n");
								}

							}
							if (item.getMtrDescripcion().length() > 220 && item.getMtrDescripcion().length() <= 264) {

								if (item.getPracDescripcion() != null) {
									sbPeriodo.append(item.getPracDescripcion());
									sbPeriodo.append("\n\n\n\n\n\n\n");
								} else {
									sbPeriodo.append(" ");
									sbPeriodo.append("\n\n\n\n\n\n\n");

								}

								if (item.getMtrCodigo() != null) {
									sbCodigo.append(item.getMtrCodigo());
									sbCodigo.append("\n\n\n\n\n\n\n");
								} else {
									sbCodigo.append(" ");
									sbCodigo.append("\n\n\n\n\n\n\n");
								}

								sbAsignatura.append(item.getMtrDescripcion());
								sbAsignatura.append("\n\n\n");

								if (item.getCslCodigo() != null) {
									sbCausal.append(item.getCslCodigo());
									sbCausal.append("\n\n\n\n\n\n\n");
								} else {
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n\n\n");
								}

								if (item.getDtmtArchivoEstudiantes() != null) {
									sbEvidencia.append(item.getDtmtArchivoEstudiantes());
									sbEvidencia.append("\n\n\n\n\n\n\n");
								} else {
									sbEvidencia.append(" ");
									sbEvidencia.append("\n\n\n\n\n\n\n");
								}

								if (item.getEstadoSolicitudRetiro() != null) {
									if (item.getEstadoSolicitudRetiro() == 0) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL);
										sbEstado.append("\n\n\n\n\n\n\n");
									}
									if (item.getEstadoSolicitudRetiro() == 1) {
										sbEstado.append(
												DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL);
										sbEstado.append("\n\n\n\n\n\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n");
								}

							}
							frmRrmParametros.put("periodo", sbPeriodo.toString());
							frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
							frmRrmParametros.put("asignatura", sbAsignatura.toString());
							frmRrmParametros.put("numero", sbHora.toString());
							frmRrmParametros.put("causal", sbCausal.toString());
							frmRrmParametros.put("evidencia", sbEvidencia.toString());
							frmRrmParametros.put("solicitudEstado", sbEstado.toString());

							String secreAbogado = null;

							if (asrffUsuario != null) {
								secreAbogado = asrffUsuario.getUsrPersona().getPrsNombres() + " "
										+ asrffUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase() + " "
										+ (asrffUsuario.getUsrPersona().getPrsSegundoApellido() == null ? " "
												: asrffUsuario.getUsrPersona().getPrsSegundoApellido());
							} else {
								secreAbogado = " ";

							}
							frmRrmParametros.put("secreAbogado", secreAbogado);

							StringBuilder sbObservaciones = new StringBuilder();

							if (asrffObservacionFinal != null) {
								sbObservaciones.append("Observaciones: ");
								sbObservaciones.append("\n");
								sbObservaciones.append(asrffObservacionFinal);
							} else {
								sbObservaciones.append("Observaciones: ");
								sbObservaciones.append("\n");

							}
							frmRrmParametros.put("observaciones", sbObservaciones.toString());

							if (asrffUsuario.getUsrNick() != null) {
								frmRrmParametros.put("nick", asrffUsuario.getUsrNick());
							} else {
								frmRrmParametros.put("nick", " ");
							}

							Boolean encontrado = false;
							Causal objetoCausal = new Causal();
							for (Causal causal : listaCausalaux) {
								if (causal.getCslCodigo().equals(item.getCslCodigo())) {
									encontrado = true;
									break;
								}
							}

							if (encontrado == false) {
								objetoCausal.setCslCodigo(item.getCslCodigo());
								objetoCausal.setCslDescripcion(item.getCslDescripcion());
								listaCausalaux.add(objetoCausal);
							}

						}
						StringBuilder sbCslCodigo = new StringBuilder();
						StringBuilder sbCslDescripcion = new StringBuilder();
						for (Causal causal2 : listaCausalaux) {

							if (causal2.getCslCodigo() != null) {
								sbCslCodigo.append(causal2.getCslCodigo());
								sbCslCodigo.append("\n");
							} else {
								sbCslCodigo.append(" ");
								sbCslCodigo.append("\n");
							}

							if (causal2.getCslDescripcion() != null) {
								sbCslDescripcion.append(causal2.getCslDescripcion());
								sbCslDescripcion.append("\n");
							} else {
								sbCslDescripcion.append(" ");
								sbCslDescripcion.append("\n");

							}

							frmRrmParametros.put("cslCodigo", sbCslCodigo.toString());
							frmRrmParametros.put("cslDescripcion", sbCslDescripcion.toString());
						}

						StringBuilder pathGeneralReportes = new StringBuilder();
						pathGeneralReportes
								.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
						pathGeneralReportes
								.append("/academico/reportes/archivosJasper/reporteAprobacionRetiroFortuito");
						frmRrmParametros.put("imagenCabecera", pathGeneralReportes + "/cabecera.png");
						frmRrmParametros.put("imagenPie", pathGeneralReportes + "/pie.png");
						frmRrmParametros.put("uce_logo", pathGeneralReportes + "/uce_logo.png");

						frmRrmCampos = new ArrayList<Map<String, Object>>();
						Map<String, Object> datoTercera = null;
						datoTercera = new HashMap<String, Object>();
						frmRrmCampos.add(datoTercera);
						// ****************************************************************//
						// *********FIN GENERACION DEL REPORTE *********//
						// ****************************************************************//
						// ****************************************************************//

						/*
						 * ENVIA AL SERVER MAIL
						 */

						jasperReport = (JasperReport) JRLoader
								.loadObject(new File((pathGeneralReportes.toString() + "/reporteSecreAbogado.jasper")));

						jasperPrint = JasperFillManager.fillReport(jasperReport, frmRrmParametros,
								new JREmptyDataSource());// llena el reporte

						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);

						// lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(asrffPersonaSeleccionadaVer.getPrsMailInstitucional());
						// path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo = new StringBuilder();
						formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss",
								new Locale("es", "ES"));
						fecha = formato.format(new Date());
						sbCorreo = GeneralesUtilidades.generarAsuntoAprobacionRetiroFortuito(
								GeneralesUtilidades.generaStringParaCorreo(fecha.toString()), nombres,
								GeneralesUtilidades.generaStringParaCorreo(asrffCarreraVer.getCrrDescripcion()));
						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
								GeneralesConstantes.APP_ASUNTO_RESPUESTA_RETIRO_POR_SITUACION_FORTUITA,
								sbCorreo.toString(), "admin", "dt1c201s", true, arreglo,
								"respuestaSolicitudRetiroFortuito." + MailDtoConstantes.TIPO_PDF,
								MailDtoConstantes.TIPO_PDF);
						String jsonSt = pmail.generarMail();
						Gson json = new Gson();
						MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
						// Iniciamos el envío de mensajes
						ObjectMessage message = session.createObjectMessage(mailDto);
						producer.send(message);
						/*
						 * FIN ENVIO AL SERVER MAIL
						 */

						// Establecemos en el atributo de la sesión la lista de mapas dedatos frmCrpCampos y parámetros frmCrpParametros

						FacesContext context = FacesContext.getCurrentInstance();
						HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
						HttpSession httpSession = request.getSession(false);
						httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
						httpSession.setAttribute("frmCargaNombreReporte", frmRrmNombreReporte);
						httpSession.setAttribute("frmCargaParametros", frmRrmParametros);

						asrffActivaReporteFinal = 1;

					} catch (Exception e) {
						e.printStackTrace();
					}
					// ******************************************************************************
					// *********************** ACA FINALIZA EL ENVIO DE MAIL ************************
					// ******************************************************************************

					asrffDesactivaBotonReporte = Boolean.FALSE; // activo boton
																// generar PDF
					asrffDesactivaBotonGuardar = Boolean.TRUE;
					asrffActivaReporteFinal = 1;
					asrffListaMateriasSolicitadasDto = null;
					asrffClickGuardarResolver = 0;
					// setAsrffObservacionReporte(getAsrffObservacionFinal());//
					// necesito guardarme el valor en otra variable antes de que se anule la variable y se refresque la pantalla al guardar la solicitudes

					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.guardarResolver.validacion.con.exito")));

				} else {
					asrffClickGuardarResolver = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.guardarResolver.validacion.sin.exito")));
				}

			} catch (Exception e) {
				asrffClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.guardarResolver.exception")));
			}
		} else {
			asrffClickGuardarResolver = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.guardarResolver.validacion.no.existe.materias")));
		}
		// SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
		asrffClickGuardarResolver = 0;
		// VACIO LA LISTA DE SOLICITUDES
		asrffListaSolicitudesDto = null;
		setAsrffObservacionReporte(getAsrffObservacionFinal());// necesito guardarme elvalor en otra variableantes de quese anule la variable y se refresque la pantalla al guardar la solicitudes
		asrffObservacionFinal = null;

	}

	/**
	 * Método que llama al reporte
	 */
	public void llamarReporte() {

		// GENERAR REPORTE
		ReporteAprobacionRetiroFortuitoForm.generarReporteAprobacionRetiroFortuito(asrffListaMateriasReporte,asrffCarreraVer, asrffPersonaSeleccionadaVer, asrffPeriodoAcademicoActivo, asrffUsuario,
				asrffObservacionReporte);
		// ACTIVO MODAL PARA QUE SE ABRA EL REPORTE GENERADO Y DESCARGUE
		asrffActivaReporteFinal = 1;

	}
	// ****************************************************************/
	// ******************* METODOS AUXILIARES *************************/
	// ****************************************************************/

	// INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros
	 */
	public void iniciarParametros() {
		// INSTANCIO EL PERIODO ACADÉMICO
		asrffPeriodoAcademicoBuscar = new PeriodoAcademico();
		// INSTANCIO LA CARRERA DTO
		asrffCarreraDtoBuscar = new CarreraDto();
		// INSTANCIO EL ESTUDIANTE JDB DTO
		asrffEstudianteBuscar = new EstudianteJdbcDto();
		asrffListaSolicitudesDto = null;
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		asrffEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		asrffEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO EL ID DE CARRERA DTO
		asrffCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		asrffPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		asrffValidadorSeleccion = GeneralesConstantes.APP_ID_BASE;
		asrffClickGuardarResolver = 0;
		// asrffActivaReportePDF=0;
		asrffActivaModalCargarResolucion = 0;
		asrffDesactivaBotonReporte = Boolean.TRUE;
		asrffDesactivaBotonGuardar = Boolean.FALSE;
		asrffDesactivaCargarDoc = false;
		asrffMateriaSeleccionada = null;
		asrffObservacionReporte = null;

	}

	/**
	 * Método para activar el modal de carga de resolucion
	 * @param materia- materia que se selecciona de la lista para cargar resolucion
	 */
	public void activaModalCargarSolicitud(EstudianteJdbcDto materia) {
		asrffActivaModalCargarResolucion = 1;
		asrffClickGuardarResolver = 0; // desactiva modal guardar resolucion apelacion
		asrffMateriaSeleccionada = new EstudianteJdbcDto();
		asrffMateriaSeleccionada = materia;
		asrffDesactivaCargarDoc = false;
	}

	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event  - event archivo oficio que presenta el
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		asrffNombreArchivoSubido = archivo.getFile().getFileName();
		asrffNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir") + File.separator + asrffNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(), rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {

			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.handleFileUpload.carga.archivo.exception")));
		}
	}

	/**
	 * Método para guardar el archivo de la resolucion
	 * @param materia-materia que se selecciona de la lista para el archivo de la resolucion
	 */

	public void guardarSubirArchivo(EstudianteJdbcDto materia) {

		if (asrffNombreArchivoSubido != null) { // verifico que se haya cargado un archivo
			// Buscamos la materia en la lista de materias para guardar los valores
			for (EstudianteJdbcDto itemMtr : asrffListaMateriasSolicitadasDto) {
				if (itemMtr.getMtrId() == materia.getMtrId()) {
					// Guardamos el nombre del archivo en el objeto
					itemMtr.setDtmtArchivoRespuesta(asrffNombreArchivoSubido);
					break;
				}
			}
			asrffActivaModalCargarResolucion = 0;
			asrffDesactivaCargarDoc = true;
			asrffNombreArchivoAuxiliar = null;
			asrffNombreArchivoSubido = null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("mensaje", "Documento cargado con éxito"); // Mensaje en modal

		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("mensaje", "Debe seleccionar el documento"); // Mensaje en modal
		}
	}

	/**
	 * Método para cerrar la ventana de subir archivo
	 */

	public void CancelarSubirArchivo() {
		asrffMateriaSeleccionada = null;
		asrffActivaModalCargarResolucion = 0;
		asrffDesactivaCargarDoc = false;

	}

	/**
	 * Limpia los parámetros ingresados en el panel de busqueda
	 */
	public void limpiar() {
		// SETEO EL ID DE CARRERA DTO
		asrffCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		asrffPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		asrffEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		asrffEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		asrffListaSolicitudesDto = null;

	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a lapagina de inicio
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		return "irInicio";
	}

	// HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton guardar para verificar que cumpla lasreglas antes de presentar el modal guardar
	 * @return retora null para para cualquier cosa
	 */
	public String controlarClickGuardarResolver() {
		Boolean selecionadosTodos = false;
		// Boolean dentroCronograma= false;
		Boolean resolucionTodos = false;
		asrffClickGuardarResolver = 0;

		// VERIFICO QUE EXISTA EL DIRECTORIO PARA GUARDAR EL PDF
		String pathDirGuardar = GeneralesConstantes.APP_PATH_ARCHIVOS
				+ GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE;

		File directorio = new File(pathDirGuardar);
		boolean existeDirectorio = true;
		if (!directorio.exists()) {

			existeDirectorio = false;
		}

		// VERICO QUE LAS ASIGNATURAS
		if (asrffListaMateriasSolicitadasDto.size() >= 1) {
			// VERICO QUE LAS ASIGNATURAS ESTEN RESUELTAS
			for (EstudianteJdbcDto materia : asrffListaMateriasSolicitadasDto) {
				if (materia.getEstadoSolicitudRetiro() != GeneralesConstantes.APP_ID_BASE) {
					selecionadosTodos = true;
				} else {
					selecionadosTodos = false;
					break;
				}
			}

			// VERIFICO QUE TODAS LAS MATERIAS TENGAN LA RESOLUCION SUBIDA
			for (EstudianteJdbcDto materia : asrffListaMateriasSolicitadasDto) {
				if (materia.getDtmtArchivoRespuesta() != null) {
					resolucionTodos = true;
				} else {
					resolucionTodos = false;
					break;
				}
			}

			// VERIFICO QUE ESTE ACTIVO EL CRONOGRAMA --  NO TIENE CRONOGRAMA

			// dentroCronograma=verificarCronograma();

			if (existeDirectorio) {
				// if(dentroCronograma==true){

				if (selecionadosTodos == true) {
					if (resolucionTodos == true) {
						if ((getAsrffObservacionFinal() != null) && (getAsrffObservacionFinal().length() >= 1)) {
							asrffClickGuardarResolver = 1;
						} else {
							asrffClickGuardarResolver = 0;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.observacion.final.validacion.exception")));
						}

					} else {
						asrffClickGuardarResolver = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.documento.resolucion.todos.validacion.exception")));

					}

				} else {
					asrffClickGuardarResolver = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.seleccionar.todos.validacion.exception")));
				}
				// }

			} else {

				asrffClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.directorio.guardar.archivo.validacion.exception")));
				// FacesUtil.mensajeError("No se encontro el diretorio para realizar el guardado del archivo de resolución.");

			}

		} else {
			asrffClickGuardarResolver = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.no.existe.materias.validacion.exception")));
		}

		// número de materias solicitadas para aprobar, en estado aprobado
		int contadorListaEstudiante = 0;
		for (EstudianteJdbcDto materiaAux : asrffListaMateriasSolicitadasDto) {
			if (materiaAux.getEstadoSolicitudRetiro() == 0) {
				contadorListaEstudiante = contadorListaEstudiante + 1;
			}
		}
		// numero de materias aprobadas el retiro
		int contadorListMatAproRetiro = 0;
		for (EstudianteJdbcDto materiaRetirada : asrffListMateriasInactivas) {
			contadorListMatAproRetiro = contadorListMatAproRetiro + 1;
		}
		// numero total de materias que esta matriculado
		int contadorListMatTotal = 0;
		for (EstudianteJdbcDto materiaTotal : asrffListMateriasTodas) {
			contadorListMatTotal = contadorListMatTotal + 1;
		}

		// VERICACION SI HACE EL RETIRO EN TODAS LAS MATERIAS - PARA RETIRAR O desactivar LA FICHA MATRICULA
		if (asrffListMateriasTodas != null && asrffListaMateriasSolicitadasDto != null
				&& asrffListMateriasInactivas != null) {
			if (contadorListMatTotal == (contadorListaEstudiante + contadorListMatAproRetiro)) { // si el total de la suma de materias solicitadas mas las materias inactivas aprobadas solicitud
				asrffRetiroMatriculaTotal = true;
			} else {
				asrffRetiroMatriculaTotal = false;
			}
		}

		return null;
	}

	/**
	 * Método que desactiva el el botón guardar.
	 */
	public void desactivaModalGuardar() {
		asrffClickGuardarResolver = 0;
	}

	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return
	 */
	public StreamedContent descargaArchivo(EstudianteJdbcDto materiaSeleccionada) {
		asrffArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+ GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ materiaSeleccionada.getDtmtArchivoEstudiantes();
		if (asrffArchivoSelSt != null) {
			String nombre = materiaSeleccionada.getDtmtArchivoEstudiantes();
			try {
				// FileInputStream fis = new FileInputStream(asrffArchivoSelSt);
				URL oracle = new URL("file:" + asrffArchivoSelSt);
				// URL urlObject = new URL("/");
				URLConnection urlConnection = oracle.openConnection();
				InputStream inputStream = urlConnection.getInputStream();
				return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(asrffArchivoSelSt), nombre);
			} catch (FileNotFoundException fnfe) {
				asrffArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (Exception e) {
			}
		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.StreamedContent.descargar.archivo.no.encontrado.exception")));
			return null;
		}
		return null;
	}


	// ****************************************************************/
	// ******************* GETTERS Y SETTERS **************************/
	// ****************************************************************/

	public Usuario getAsrffUsuario() {
		return asrffUsuario;
	}

	public void setAsrffUsuario(Usuario asrffUsuario) {
		this.asrffUsuario = asrffUsuario;
	}

	public PeriodoAcademico getAsrffPeriodoAcademicoBuscar() {
		return asrffPeriodoAcademicoBuscar;
	}

	public void setAsrffPeriodoAcademicoBuscar(PeriodoAcademico asrffPeriodoAcademicoBuscar) {
		this.asrffPeriodoAcademicoBuscar = asrffPeriodoAcademicoBuscar;
	}

	public CarreraDto getAsrffCarreraDtoBuscar() {
		return asrffCarreraDtoBuscar;
	}

	public void setAsrffCarreraDtoBuscar(CarreraDto asrffCarreraDtoBuscar) {
		this.asrffCarreraDtoBuscar = asrffCarreraDtoBuscar;
	}

	public List<CarreraDto> getAsrffListCarreraDtoBusq() {
		return asrffListCarreraDtoBusq;
	}

	public void setAsrffListCarreraDtoBusq(List<CarreraDto> asrffListCarreraDtoBusq) {
		this.asrffListCarreraDtoBusq = asrffListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getAsrffEstudianteBuscar() {
		return asrffEstudianteBuscar;
	}

	public void setAsrffEstudianteBuscar(EstudianteJdbcDto asrffEstudianteBuscar) {
		this.asrffEstudianteBuscar = asrffEstudianteBuscar;
	}

	public List<FichaMatriculaDto> getAsrffListaSolicitudesDto() {
		asrffListaSolicitudesDto = asrffListaSolicitudesDto == null ? (new ArrayList<FichaMatriculaDto>())
				: asrffListaSolicitudesDto;
		return asrffListaSolicitudesDto;
	}

	public void setAsrffListaSolicitudesDto(List<FichaMatriculaDto> asrffListaSolicitudesDto) {
		this.asrffListaSolicitudesDto = asrffListaSolicitudesDto;
	}

	public Persona getAsrffPersonaSeleccionadaVer() {
		return asrffPersonaSeleccionadaVer;
	}

	public void setAsrffPersonaSeleccionadaVer(Persona asrffPersonaSeleccionadaVer) {
		this.asrffPersonaSeleccionadaVer = asrffPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getAsrffPeriodoSolicitudesVer() {
		return asrffPeriodoSolicitudesVer;
	}

	public void setAsrffPeriodoSolicitudesVer(PeriodoAcademico asrffPeriodoSolicitudesVer) {
		this.asrffPeriodoSolicitudesVer = asrffPeriodoSolicitudesVer;
	}

	public Carrera getAsrffCarreraVer() {
		return asrffCarreraVer;
	}

	public void setAsrffCarreraVer(Carrera asrffCarreraVer) {
		this.asrffCarreraVer = asrffCarreraVer;
	}

	public List<EstudianteJdbcDto> getAsrffListaMateriasSolicitadasDto() {
		asrffListaMateriasSolicitadasDto = asrffListaMateriasSolicitadasDto == null
				? (new ArrayList<EstudianteJdbcDto>()) : asrffListaMateriasSolicitadasDto;
		return asrffListaMateriasSolicitadasDto;
	}

	public void setAsrffListaMateriasSolicitadasDto(List<EstudianteJdbcDto> asrffListaMateriasSolicitadasDto) {
		this.asrffListaMateriasSolicitadasDto = asrffListaMateriasSolicitadasDto;
	}

	public Integer getAsrffValidadorSeleccion() {
		return asrffValidadorSeleccion;
	}

	public void setAsrffValidadorSeleccion(Integer asrffValidadorSeleccion) {
		this.asrffValidadorSeleccion = asrffValidadorSeleccion;
	}

	public List<PeriodoAcademico> getAsrffListaPeriodoAcademicoBusq() {
		asrffListaPeriodoAcademicoBusq = asrffListaPeriodoAcademicoBusq == null ? (new ArrayList<PeriodoAcademico>())
				: asrffListaPeriodoAcademicoBusq;
		return asrffListaPeriodoAcademicoBusq;
	}

	public void setAsrffListaPeriodoAcademicoBusq(List<PeriodoAcademico> asrffListaPeriodoAcademicoBusq) {
		this.asrffListaPeriodoAcademicoBusq = asrffListaPeriodoAcademicoBusq;
	}

	// public Integer getAsrffTipoCronograma() {
	// return asrffTipoCronograma;
	// }
	//
	// public void setAsrffTipoCronograma(Integer asrffTipoCronograma) {
	// this.asrffTipoCronograma = asrffTipoCronograma;
	// }

	public Dependencia getAsrffDependencia() {
		return asrffDependencia;
	}

	public void setAsrffDependencia(Dependencia asrffDependencia) {
		this.asrffDependencia = asrffDependencia;
	}

	// public CronogramaActividadJdbcDto getAsrffCronogramaActividad() {
	// return asrffCronogramaActividad;
	// }
	//
	// public void setAsrffCronogramaActividad(CronogramaActividadJdbcDto
	// asrffCronogramaActividad) {
	// this.asrffCronogramaActividad = asrffCronogramaActividad;
	// }

	public PeriodoAcademico getAsrffPeriodoAcademicoActivo() {
		return asrffPeriodoAcademicoActivo;
	}

	public void setAsrffPeriodoAcademicoActivo(PeriodoAcademico asrffPeriodoAcademicoActivo) {
		this.asrffPeriodoAcademicoActivo = asrffPeriodoAcademicoActivo;
	}

	public String getAsrffArchivoSelSt() {
		return asrffArchivoSelSt;
	}

	public void setAsrffArchivoSelSt(String asrffArchivoSelSt) {
		this.asrffArchivoSelSt = asrffArchivoSelSt;
	}

	public Integer getAsrffActivaModalCargarResolucion() {
		return asrffActivaModalCargarResolucion;
	}

	public void setAsrffActivaModalCargarResolucion(Integer asrffActivaModalCargarResolucion) {
		this.asrffActivaModalCargarResolucion = asrffActivaModalCargarResolucion;
	}

	public EstudianteJdbcDto getAsrffMateriaSeleccionada() {
		return asrffMateriaSeleccionada;
	}

	public void setAsrffMateriaSeleccionada(EstudianteJdbcDto asrffMateriaSeleccionada) {
		this.asrffMateriaSeleccionada = asrffMateriaSeleccionada;
	}

	public Integer getAsrffClickGuardarResolver() {
		return asrffClickGuardarResolver;
	}

	public void setAsrffClickGuardarResolver(Integer asrffClickGuardarResolver) {
		this.asrffClickGuardarResolver = asrffClickGuardarResolver;
	}

	// public FichaInscripcionDto getAsrffFichaInscripcion() {
	// return asrffFichaInscripcion;
	// }
	//
	// public void setAsrffFichaInscripcion(FichaInscripcionDto
	// asrffFichaInscripcion) {
	// this.asrffFichaInscripcion = asrffFichaInscripcion;
	// }

	public String getAsrffNombreArchivoSubido() {
		return asrffNombreArchivoSubido;
	}

	public void setAsrffNombreArchivoSubido(String asrffNombreArchivoSubido) {
		this.asrffNombreArchivoSubido = asrffNombreArchivoSubido;
	}

	public String getAsrffNombreArchivoAuxiliar() {
		return asrffNombreArchivoAuxiliar;
	}

	public void setAsrffNombreArchivoAuxiliar(String asrffNombreArchivoAuxiliar) {
		this.asrffNombreArchivoAuxiliar = asrffNombreArchivoAuxiliar;
	}

	public String getAsrffObservacionFinal() {
		return asrffObservacionFinal;
	}

	public void setAsrffObservacionFinal(String asrffObservacionFinal) {
		this.asrffObservacionFinal = asrffObservacionFinal;
	}

	public List<EstudianteJdbcDto> getAsrffListaMateriasReporte() {
		asrffListaMateriasReporte = asrffListaMateriasReporte == null ? (new ArrayList<EstudianteJdbcDto>())
				: asrffListaMateriasReporte;
		return asrffListaMateriasReporte;
	}

	public void setAsrffListaMateriasReporte(List<EstudianteJdbcDto> asrffListaMateriasReporte) {
		this.asrffListaMateriasReporte = asrffListaMateriasReporte;
	}

	public Boolean getAsrffDesactivaBotonGuardar() {
		return asrffDesactivaBotonGuardar;
	}

	public void setAsrffDesactivaBotonGuardar(Boolean asrffDesactivaBotonGuardar) {
		this.asrffDesactivaBotonGuardar = asrffDesactivaBotonGuardar;
	}

	public String getAsrffObservacionReporte() {
		return asrffObservacionReporte;
	}

	public void setAsrffObservacionReporte(String asrffObservacionReporte) {
		this.asrffObservacionReporte = asrffObservacionReporte;
	}

	public Boolean getAsrffDesactivaBotonReporte() {
		return asrffDesactivaBotonReporte;
	}

	public void setAsrffDesactivaBotonReporte(Boolean asrffDesactivaBotonReporte) {
		this.asrffDesactivaBotonReporte = asrffDesactivaBotonReporte;
	}

	public FichaMatriculaDto getAsrffSolicitudesEstudiante() {
		return asrffSolicitudesEstudiante;
	}

	public void setAsrffSolicitudesEstudiante(FichaMatriculaDto asrffSolicitudesEstudiante) {
		this.asrffSolicitudesEstudiante = asrffSolicitudesEstudiante;
	}

	public List<EstudianteJdbcDto> getAsrffListMateriasTodas() {
		return asrffListMateriasTodas;
	}

	public void setAsrffListMateriasTodas(List<EstudianteJdbcDto> asrffListMateriasTodas) {
		this.asrffListMateriasTodas = asrffListMateriasTodas;
	}

	public List<EstudianteJdbcDto> getAsrffListMateriasInactivas() {
		return asrffListMateriasInactivas;
	}

	public void setAsrffListMateriasInactivas(List<EstudianteJdbcDto> asrffListMateriasInactivas) {
		this.asrffListMateriasInactivas = asrffListMateriasInactivas;
	}

	public boolean isAsrffRetiroMatriculaTotal() {
		return asrffRetiroMatriculaTotal;
	}

	public void setAsrffRetiroMatriculaTotal(boolean asrffRetiroMatriculaTotal) {
		this.asrffRetiroMatriculaTotal = asrffRetiroMatriculaTotal;
	}

	public Integer getAsrffActivaReporteFinal() {
		return asrffActivaReporteFinal;
	}

	public void setAsrffActivaReporteFinal(Integer asrffActivaReporteFinal) {
		this.asrffActivaReporteFinal = asrffActivaReporteFinal;
	}

	public Boolean getAsrffDesactivaCargarDoc() {
		return asrffDesactivaCargarDoc;
	}

	public void setAsrffDesactivaCargarDoc(Boolean asrffDesactivaCargarDoc) {
		this.asrffDesactivaCargarDoc = asrffDesactivaCargarDoc;
	}

}
