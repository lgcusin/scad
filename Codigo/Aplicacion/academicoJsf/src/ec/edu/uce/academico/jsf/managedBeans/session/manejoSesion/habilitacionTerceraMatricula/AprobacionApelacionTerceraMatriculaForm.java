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
   
 ARCHIVO:     AprobacionApelacionTerceraMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la aprobacion/negacion hecha por el Consejo Directivo a la apelación de tercera matricula realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 22-02-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.habilitacionTerceraMatricula;
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
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.SolicitudTerceraMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteAprobacionApelacionTerceraForm;
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
 * Clase (session bean) AprobarApelacionTercerMatriculaForm. 
 * Bean de sesion que maneja la la aprobación/negación hecha por el Consejo Directivo a la apelación de tercera matricula realizada por el estudiante. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "aprobacionApelacionTerceraMatriculaForm")
@SessionScoped
public class AprobacionApelacionTerceraMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario aatmfUsuario;
	//PARA BUSQUEDA
	private PeriodoAcademico aatmfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> aatmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico aatmfPeriodoAcademicoActivo;
	private CarreraDto aatmfCarreraDtoBuscar;
	private List<CarreraDto> aatmfListCarreraDtoBusq;
	private EstudianteJdbcDto aatmfEstudianteBuscar;
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA APELACION
	private List<SolicitudTerceraMatriculaDto> aatmfListaSolicitudesDto;
	//VER MATERIAS
	private Persona aatmfPersonaSeleccionadaVer;
	private PeriodoAcademico aatmfPeriodoSolicitudesVer;
	private Carrera aatmfCarreraVer;
	private FichaInscripcionDto aatmfFichaInscripcion;
	private List<SolicitudTerceraMatriculaDto> aatmfListaMateriasSolicitadasDto;
	private Integer aatmfValidadorSeleccion;
	private Integer aatmfClickGuardarResolver;
	private Integer aatmfTipoCronograma;
	private Dependencia aatmfDependencia;
	private CronogramaActividadJdbcDto aatmfCronogramaActividad;
	private String aatmfArchivoSelSt;
	
	// MODAL SUBIR RESOLUCION
	private Integer aatmfActivaModalCargarResolucion = 1;
	private SolicitudTerceraMatriculaDto aatmfMateriaSeleccionada;
	
	private String aatmfNombreArchivoSubido; 
	private String aatmfNombreArchivoAuxiliar;
	
	private Integer aatmfActivaReportePDF; //activa el modal para presenar el reporte
	private Boolean aatmfDesactivaBotonReporte; // desactiva el boton reporte
	private Boolean aatmfDesactivaBotonGuardar;  //desactiva el boton Guardar
	//PARA EL REPORTE
	private String aatmfObservacionFinal;
	private List<SolicitudTerceraMatriculaDto>  aatmfListaMateriasReporte;
	private String aatmfObservacionReporte;

	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar() {
	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB 
	PeriodoAcademicoServicio servAatmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servAatmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servAatmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servAatmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servAatmfPersonaServicio;
	@EJB 
	CarreraServicio servAatmfCarreraServicio;
	@EJB 
	DependenciaServicio servAatmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servAatmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servAatmfSolicitudTerceraMatriculaServicioServicioJdbc;
	@EJB 
	FichaInscripcionDtoServicioJdbc servAatmfFichaInscripcionDto;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irResolverApelacionListarEstudiantes(Usuario usuario) {
		aatmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		   aatmfPeriodoAcademicoActivo = servAatmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		   aatmfListaPeriodoAcademicoBusq=servAatmfPeriodoAcademicoServicio.listarTodos();
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			aatmfListCarreraDtoBusq = servAatmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(aatmfUsuario.getUsrId(), RolConstantes.ROL_SECREABOGADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, aatmfPeriodoAcademicoActivo.getPracId());
			retorno = "irApruebaApelacionListarEstudiantes";
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
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron tercera matrícula con los parámetros ingresados
	 */
	public void buscarEstudiantes(){
		aatmfListaSolicitudesDto=new ArrayList<SolicitudTerceraMatriculaDto>();
			try {
				//Verifico que se seleecione una carrera
				if(aatmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					aatmfListaSolicitudesDto=servAatmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitudSIIUSAU(aatmfPeriodoAcademicoBuscar.getPracId(), aatmfCarreraDtoBuscar.getCrrId(), aatmfEstudianteBuscar.getPrsPrimerApellido(), aatmfEstudianteBuscar.getPrsIdentificacion(), SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE);
				}else{
					aatmfListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
			} catch (SolicitudTerceraMatriculaDtoException e) {
				aatmfListaSolicitudesDto=null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.buscarEstudiantes.exception")));
			}
	}
	
	
	
	/**
	 * Método que permite ir a la visualización de las materias
	 * @param solicitudesEstudiante - solicitudesEstudiante objeto que se envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerMateriasSolicitadas(SolicitudTerceraMatriculaDto solicitudesEstudiante){
		String retorno=null;
		aatmfListaMateriasSolicitadasDto= new ArrayList<>();
		aatmfPersonaSeleccionadaVer= new Persona();
		aatmfPeriodoSolicitudesVer= new PeriodoAcademico();
		aatmfCarreraVer=new Carrera();
		aatmfObservacionFinal= null;
		//Seteo como activo la opción de descarga del archivo
		for (SolicitudTerceraMatriculaDto item : aatmfListaMateriasSolicitadasDto) {
			item.setVisualizador(false);
			item.setRespuestaSolicitud(GeneralesConstantes.APP_ID_BASE);
		}
		    //Se busca el periodo, persona, carrera y lista de materias en las que solicito y verifico la solicitud
		try {
			aatmfPeriodoSolicitudesVer=servAatmfPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			aatmfPersonaSeleccionadaVer=servAatmfPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			aatmfCarreraVer=servAatmfCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			aatmfListaMateriasSolicitadasDto=servAatmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(solicitudesEstudiante.getPracId(), solicitudesEstudiante.getCrrId(), solicitudesEstudiante.getFcesId(), SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_VALUE);
            retorno= "irVerMateriasApeladas"	;
		} catch (SolicitudTerceraMatriculaDtoException e) {
			aatmfPeriodoSolicitudesVer=null;
			aatmfPersonaSeleccionadaVer=null;
			aatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  catch (PeriodoAcademicoNoEncontradoException e) {
			aatmfPeriodoSolicitudesVer=null;
			aatmfPersonaSeleccionadaVer=null;
			aatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			aatmfPeriodoSolicitudesVer=null;
			aatmfPersonaSeleccionadaVer=null;
			aatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			aatmfPeriodoSolicitudesVer=null;
			aatmfPersonaSeleccionadaVer=null;
			aatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			aatmfPeriodoSolicitudesVer=null;
			aatmfPersonaSeleccionadaVer=null;
			aatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	

	
	/**
	 * Método que guarda la respuesta a la apelación de tercera matrícula en las materias solicitadas
	 * @return retorna - la navegación de la página listar estudiantes.
	 */
	public String guardarResolver(){
	String retorno = null;
	aatmfFichaInscripcion= new FichaInscripcionDto();
	aatmfListaMateriasReporte= new ArrayList<>();
	
		//REALIZA LA ACTUALIZACIÓN DE LOS ESTADOS DE  RECORD ACADEMICO Y SOLICITUD TERCERA MATRICULA
			if(aatmfListaMateriasSolicitadasDto!=null && aatmfListaMateriasSolicitadasDto.size()>0 ){ //verifica que la lista no este nula
				
//				int estadoFichaInscripcionAux= FichaInscripcionConstantes.BLOQUEADO_TERCERA_MATRICULA_VALUE;
//				//verificacmos si se aprobo todas las solicitudes para crera el estado de activo en ficha inscripcion
//				for (SolicitudTerceraMatriculaDto materia : aatmfListaMateriasSolicitadasDto) {
//					if(materia.getRespuestaSolicitud()==SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE){
//						estadoFichaInscripcionAux= FichaInscripcionConstantes.DESBLOQUEADO_TERCERA_MATRICULA_VALUE;
//						
//					}else{
//						estadoFichaInscripcionAux= FichaInscripcionConstantes.BLOQUEADO_TERCERA_MATRICULA_VALUE;
//						break;
//					}
//						
//				}
			//	Busco la ficha Inscripcion del estudiante para luego activarla si es el caso
				try {
					aatmfFichaInscripcion=servAatmfFichaInscripcionDto.buscarFcinXidentificacionXcarrera(aatmfPersonaSeleccionadaVer.getPrsIdentificacion(), aatmfCarreraVer.getCrrId());   
				} catch (FichaInscripcionDtoException e1) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e1.getMessage());
				} catch (FichaInscripcionDtoNoEncontradoException e1) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e1.getMessage());
				}		
				//proceso de transacción
				String rutaNombre = null;
				String rutaTemporal = null;
												
				try{
					if(servAatmfSolicitudTerceraMatriculaServicioServicioJdbc.generarAprobacionApelacionTerceraMatricula(aatmfListaMateriasSolicitadasDto,aatmfFichaInscripcion.getFcinId(), aatmfUsuario.getUsrNick(), aatmfObservacionFinal)){  
						//Si grabo bien en base procedo a grabar los archivos pdf desde el cache de la maquina  al servidor de archivos.		
						for (SolicitudTerceraMatriculaDto item : aatmfListaMateriasSolicitadasDto) {
								String extension = GeneralesUtilidades.obtenerExtension(item.getSltrmtDocumentoResolucion());
								rutaNombre = SolicitudTerceraMatriculaConstantes.RESOLUCION_APELACION_TERCERA_MATRICULA_LABEL+"-"+item.getSltrmtId()+ "." + extension;
								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + item.getSltrmtDocumentoResolucion();
								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RESOLUCION_APELACION_TERCERA_MATRICULA+ rutaNombre);
							rutaNombre = null;
							rutaTemporal = null;
						}
						
						
						//copio las lista para el reporte PDF
						for (SolicitudTerceraMatriculaDto solicitudDto : aatmfListaMateriasSolicitadasDto) {
							try {
								SolicitudTerceraMatricula solicitudBase=servAatmfSolicitudTerceraMatriculaServicioServicioJdbc.buscarPorId(solicitudDto.getSltrmtId());
								solicitudDto.setSltrmtEstado(solicitudBase.getSltrmtEstado());
								aatmfListaMateriasReporte.add(solicitudDto);
							} catch (SolicitudTerceraMatriculaNoEncontradoException e) {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(e.getMessage());
							} catch (SolicitudTerceraMatriculaException e) {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(e.getMessage());
							}
							
						}
						
						
						
						try {
							//******************************************************************************
							  //************************* ACA INICIA EL ENVIO DE MAIL ************************
							//******************************************************************************
							  //abrir conexion con server mail
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
							 // fin abrir conexion server mail
							
								//Defino variables para el reporte				
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
							
							if(aatmfPersonaSeleccionadaVer!=null){
							frmRrmNombreReporte=aatmfPersonaSeleccionadaVer.getPrsNombres()+" "+aatmfPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
									+(aatmfPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":aatmfPersonaSeleccionadaVer.getPrsSegundoApellido());
							}else{
								frmRrmNombreReporte="Reporte-Aprobacion";
							}
							
							frmRrmParametros = new HashMap<String, Object>();
							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
							//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
							String fecha = formato.format(new Date());
							frmRrmParametros.put("fecha",fecha);
							
							String nombres=null;
							if(aatmfPersonaSeleccionadaVer!=null){
							 nombres=aatmfPersonaSeleccionadaVer.getPrsNombres()+" "+aatmfPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
									+(aatmfPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":aatmfPersonaSeleccionadaVer.getPrsSegundoApellido());
							}else{
								nombres=" ";
							}
							
							frmRrmParametros.put("nombre",nombres );
							
							StringBuilder sbTextoInicial = new StringBuilder();
							sbTextoInicial.append("Señor(a)(ita) ");sbTextoInicial.append("\n");
							sbTextoInicial.append(nombres);sbTextoInicial.append("\n");
							sbTextoInicial.append("ESTUDIANTE DE LA CARRERA DE ");
							if(aatmfCarreraVer.getCrrDescripcion()!=null){
							sbTextoInicial.append(aatmfCarreraVer.getCrrDescripcion());sbTextoInicial.append("\n");
							}else{
								sbTextoInicial.append(" ");sbTextoInicial.append("\n");
							}
							
							sbTextoInicial.append("FACULTAD DE ");
							if(aatmfCarreraVer.getCrrDependencia().getDpnDescripcion()!=null){
								sbTextoInicial.append(aatmfCarreraVer.getCrrDependencia().getDpnDescripcion());sbTextoInicial.append("\n");
							}else{
								sbTextoInicial.append(" ");sbTextoInicial.append("\n");								
							}
							sbTextoInicial.append("Presente.-"); sbTextoInicial.append("\n\n");
							frmRrmParametros.put("textoInicial", sbTextoInicial.toString());			
							
							StringBuilder sbTexto = new StringBuilder();
							sbTexto.append("Una vez que el Consejo Directivo analizó su pedido de apelación de solicitudes de tercera matrícula y anexos presentados a la misma, resolvió lo siguiente:");
							frmRrmParametros.put("texto", sbTexto.toString());
							
							StringBuilder sbPeriodo = new StringBuilder();
							StringBuilder sbCodigo = new StringBuilder();
							StringBuilder sbAsignatura = new StringBuilder();
							StringBuilder sbHora = new StringBuilder();
							StringBuilder sbCausal = new StringBuilder();
							StringBuilder sbEvidencia = new StringBuilder();
							StringBuilder sbEstado = new StringBuilder();
							
							for (SolicitudTerceraMatriculaDto item : aatmfListaMateriasReporte) {
								if(item.getMtrDescripcion().length() <= 44){
									if(item.getPracDescripcion()!=null){
										sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n");
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n");
									}else{
										sbCodigo.append(" ");sbCodigo.append("\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
									
									if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n");
									}else{
										sbCausal.append(" ");sbCausal.append("\n\n");
									}
									
									if(item.getSltrmtDocumentoSolicitud()!=null){
										sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n");
									}
									
									if(item.getSltrmtEstado()!=null){
									if(item.getSltrmtEstado()==6){
										sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
									}
									if(item.getSltrmtEstado()==7){
										sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
									}
									if(item.getSltrmtEstado()==8){
										sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
									}
									}else{
										sbEstado.append(" ");sbEstado.append("\n\n");
									}
									
									
								}
								if(item.getMtrDescripcion().length() > 44 && item.getMtrDescripcion().length() <= 88){
                                   
									if(item.getPracDescripcion()!=null){
										sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n");
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n");
									}else{
										sbCodigo.append(" ");sbCodigo.append("\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
													
									if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n");
									}else{
										sbCausal.append(" ");sbCausal.append("\n\n\n");
									}
									
									if(item.getSltrmtDocumentoSolicitud()!=null){
										sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n");
									}
									
									if(item.getSltrmtEstado()!=null){
										if(item.getSltrmtEstado()==6){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==7){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==8){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										}else{
											sbEstado.append(" ");sbEstado.append("\n\n");
										}
										
								}
								if(item.getMtrDescripcion().length() > 88 && item.getMtrDescripcion().length() <= 132){
                                    
									if(item.getPracDescripcion()!=null){
										sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n");
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
									}else{
										sbCodigo.append(" ");sbCodigo.append("\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
									
									
									if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n");
									}else{
										sbCausal.append(" ");sbCausal.append("\n\n\n\n");
									}
									
									if(item.getSltrmtDocumentoSolicitud()!=null){
										sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n");
									}
									
									if(item.getSltrmtEstado()!=null){
										if(item.getSltrmtEstado()==6){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==7){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==8){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										}else{
											sbEstado.append(" ");sbEstado.append("\n\n");
										}
										
								}
								if(item.getMtrDescripcion().length() > 132 && item.getMtrDescripcion().length() <= 176){
									
                                    if(item.getPracDescripcion()!=null){
                                    	sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n");	
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
									}else{
										sbCodigo.append(" ");sbCodigo.append("\n\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
									
									
									if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n\n");
									}else{
										sbCausal.append(" ");sbCausal.append("\n\n\n\n\n");
									}
									
									if(item.getSltrmtDocumentoSolicitud()!=null){
										sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n\n");
									}
									
									if(item.getSltrmtEstado()!=null){
										if(item.getSltrmtEstado()==6){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==7){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==8){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										}else{
											sbEstado.append(" ");sbEstado.append("\n\n");
										}
										
									
								}
								if(item.getMtrDescripcion().length() > 176 && item.getMtrDescripcion().length() <= 220){
									
                                    if(item.getPracDescripcion()!=null){
                                    	sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n");	
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
									}else{
										sbCodigo.append(" ");sbCodigo.append("\n\n\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
									
									
									if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n\n\n");
									}else{
										sbCausal.append(" ");sbCausal.append("\n\n\n\n\n\n");
									}
									
									if(item.getSltrmtDocumentoSolicitud()!=null){
										sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n\n\n");
									}
									
									if(item.getSltrmtEstado()!=null){
										if(item.getSltrmtEstado()==6){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==7){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==8){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										}else{
											sbEstado.append(" ");sbEstado.append("\n\n");
										}
										
									
									
								}
								if(item.getMtrDescripcion().length() > 220 && item.getMtrDescripcion().length() <= 264){
									
                                    if(item.getPracDescripcion()!=null){
                                    	sbPeriodo.append(item.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n\n");
										
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
									}else{
										sbCodigo.append(" ");sbCodigo.append("\n\n\n\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
									
									if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n\n\n\n");
									}else{
										sbCausal.append(" ");sbCausal.append("\n\n\n\n\n\n\n");
									}
									
									if(item.getSltrmtDocumentoSolicitud()!=null){
										sbEvidencia.append(item.getSltrmtDocumentoSolicitud());sbEvidencia.append("\n\n\n\n\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n\n\n\n");
									}
									
									if(item.getSltrmtEstado()!=null){
										if(item.getSltrmtEstado()==6){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==7){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										if(item.getSltrmtEstado()==8){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
										}
										}else{
											sbEstado.append(" ");sbEstado.append("\n\n");
										}
										
									
								}
								frmRrmParametros.put("periodo", sbPeriodo.toString());
								frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
								frmRrmParametros.put("asignatura", sbAsignatura.toString());
								frmRrmParametros.put("numero", sbHora.toString());
								frmRrmParametros.put("causal", sbCausal.toString());
								frmRrmParametros.put("evidencia", sbEvidencia.toString());
								frmRrmParametros.put("sltrmtEstado", sbEstado.toString());
								
																							
									String secreAbogado= null;
									
									if(aatmfUsuario!=null){
										secreAbogado = aatmfUsuario.getUsrPersona().getPrsNombres()+" "+aatmfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
											+(aatmfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":aatmfUsuario.getUsrPersona().getPrsSegundoApellido());
									}else{
										secreAbogado=" ";
									
									}
									frmRrmParametros.put("secreAbogado",secreAbogado);
															
								StringBuilder sbObservaciones = new StringBuilder();
								
								if(aatmfObservacionFinal!=null){
								sbObservaciones.append("Observaciones: ");sbObservaciones.append("\n");
								sbObservaciones.append(aatmfObservacionFinal);
								}else{
									sbObservaciones.append("Observaciones: ");sbObservaciones.append("\n");
									
								}
								frmRrmParametros.put("observaciones", sbObservaciones.toString());
								
								if( aatmfUsuario.getUsrNick()!=null){
									frmRrmParametros.put("nick", aatmfUsuario.getUsrNick());
									}else{
										frmRrmParametros.put("nick", " ");
									}
								
					
								Boolean encontrado = false;
								Causal objetoCausal= new Causal();
								for (Causal causal : listaCausalaux) {
									if(causal.getCslCodigo().equals(item.getCslCodigo())){
										encontrado = true;
										break;
									}
								}
								
								if(encontrado ==false){
									objetoCausal.setCslCodigo(item.getCslCodigo());
									objetoCausal.setCslDescripcion(item.getCslDescripcion());
									listaCausalaux.add(objetoCausal);
								}
								
							}
							StringBuilder sbCslCodigo= new StringBuilder();
							StringBuilder sbCslDescripcion= new StringBuilder();
							for (Causal causal2 : listaCausalaux) {
														
								  if(causal2.getCslCodigo()!=null){
								    	sbCslCodigo.append(causal2.getCslCodigo());sbCslCodigo.append("\n");	
								    }else{
										sbCslCodigo.append(" ");sbCslCodigo.append("\n");
								    }
									
									if(causal2.getCslDescripcion()!=null){
										sbCslDescripcion.append(causal2.getCslDescripcion());sbCslDescripcion.append("\n");
									}else{
										sbCslDescripcion.append(" ");sbCslDescripcion.append("\n");
										
									}
								
								frmRrmParametros.put("cslCodigo", sbCslCodigo.toString());
								frmRrmParametros.put("cslDescripcion", sbCslDescripcion.toString());
							}
							
							
							StringBuilder pathGeneralReportes = new StringBuilder();
							pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
							pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteAprobarApelacionTercera");
							frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
							frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
							frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
							
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
												
							jasperReport = (JasperReport) JRLoader.loadObject(new File((pathGeneralReportes.toString() + "/reporteSecreAbogado.jasper")));
						
							jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());//llena el reporte
							
							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
										
							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(aatmfPersonaSeleccionadaVer.getPrsMailInstitucional());
							//path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo= new StringBuilder();
							formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
							fecha = formato.format(new Date());
							sbCorreo= GeneralesUtilidades.generarAsuntoAprobacionTercera(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									nombres, GeneralesUtilidades.generaStringParaCorreo(aatmfCarreraVer.getCrrDescripcion()));
							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_RESPUESTA_APELACION_TERCERA_MATRICULA,
												sbCorreo.toString()
												, "admin", "dt1c201s", true, arreglo, "respuestaApelacionTerceraMatricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
							String jsonSt = pmail.generarMail();
							Gson json = new Gson();
							MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
							// 	Iniciamos el envío de mensajes
							ObjectMessage message = session.createObjectMessage(mailDto);
							producer.send(message);
							/*
							 * FIN ENVIO AL SERVER MAIL
							 */
							
							// Establecemos en el atributo de la sesión la lista de mapas de
							// datos frmCrpCampos y parámetros frmCrpParametros
							FacesContext context = FacesContext.getCurrentInstance();
							HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
							HttpSession httpSession = request.getSession(false);
							httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
							httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
							httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
							
							aatmfActivaReportePDF=1;
						} catch (Exception e) {
							e.printStackTrace();
						}
							//******************************************************************************
							//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
							//******************************************************************************
						
						aatmfDesactivaBotonReporte= Boolean.FALSE; //activo boton generar PDF
						aatmfDesactivaBotonGuardar=Boolean.TRUE;
						
						aatmfListaMateriasSolicitadasDto=null;		
						aatmfClickGuardarResolver = 0;
						//setAatmfObservacionReporte(getAatmfObservacionFinal());// necesito guardarme el valor en otra variable antes de que se anule la variable y se refresque la pantalla al guardar la solicitudes
												
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.validacion.con.exito")));
					
					
					}else{
						aatmfClickGuardarResolver = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.validacion.sin.exito")));
					}
					//retorno = "irAListarEstudiantesApruebaSolicitud"; //No necesita retornar
					
					
				} catch (SolicitudTerceraMatriculaException e) {
					aatmfClickGuardarResolver = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.exception")));
				} catch (Exception e) {
					aatmfClickGuardarResolver = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.exception")));
				}
			}else{
				aatmfClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.validacion.no.existe.materias")));
			}
			//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			aatmfClickGuardarResolver = 0;
            //VACIO LA LISTA DE SOLICITUDES   
			aatmfListaSolicitudesDto=null;
			setAatmfObservacionReporte(getAatmfObservacionFinal());// necesito guardarme el valor en otra variable antes de que se anule la variable y se refresque la pantalla al guardar la solicitudes
			aatmfObservacionFinal= null;
		return retorno;
	}
	
	/**
	 * Método que llama al reporte
	 */
	public void llamarReporte(){
		
		//GENERAR REPORTE
		 ReporteAprobacionApelacionTerceraForm.generarReporteAprobacionApelacionTercera(aatmfListaMateriasReporte, aatmfDependencia, aatmfCarreraVer, aatmfPersonaSeleccionadaVer, aatmfPeriodoAcademicoActivo, aatmfUsuario,aatmfObservacionReporte );
		//ACTIVO MODAL PARA QUE SE ABRA EL REPORTE GENERADO Y DESCARGUE
		 aatmfActivaReportePDF=1;
	
	}
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/

	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros
	 */
	public void iniciarParametros(){
		//INSTANCIO EL PERIODO ACADÉMICO
		aatmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		aatmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		aatmfEstudianteBuscar = new EstudianteJdbcDto();
		aatmfListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		aatmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		aatmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		aatmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		aatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		aatmfValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		aatmfClickGuardarResolver=0;
		aatmfActivaReportePDF=0;
		aatmfActivaModalCargarResolucion = 0;
		aatmfDesactivaBotonReporte= Boolean.TRUE;
		aatmfDesactivaBotonGuardar=Boolean.FALSE;
		aatmfMateriaSeleccionada= null;
		aatmfObservacionReporte= null;
		
	}
	
	
	/**
	 * Método para activar el modal de carga de resolucion
	 * @param materia - materia que se selecciona de la lista para cargar resolucion
	 */
	public void activaModalCargarSolicitud(SolicitudTerceraMatriculaDto materia) {
		aatmfActivaModalCargarResolucion = 1;
		aatmfClickGuardarResolver = 0;  //desactiva modal guardar resolucion apelacion
		aatmfMateriaSeleccionada = new SolicitudTerceraMatriculaDto();
		aatmfMateriaSeleccionada = materia;
	}
	
	
	
	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event - event archivo oficio que presenta el estudiante para solicitar la autorización de tercera matriucl
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		aatmfNombreArchivoSubido = archivo.getFile().getFileName();
		aatmfNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir") + File.separator + aatmfNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(), rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {

			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Solicitud.tercera.matricula.handleFileUpload.carga.archivo.exception")));
		}
	}
	
	
	/**
	 * Método para guardar el archivo y la causal en el item
	 * @param materia -materia que se selecciona de la lista para cargar causal y la solicitud
	 */

	public void guardarSubirArchivo(SolicitudTerceraMatriculaDto materia) {
		
			if (aatmfNombreArchivoSubido != null) { //verifico que se haya cargado un archivo
					// Buscamos la materia en la lista de materias para guardar los valores
					for (SolicitudTerceraMatriculaDto itemMtr : aatmfListaMateriasSolicitadasDto) {
						if (itemMtr.getMtrId() == materia.getMtrId()) {
							// Guardamos el nombre del archivo en el objeto
							itemMtr.setSltrmtDocumentoResolucion(aatmfNombreArchivoSubido);
							break;
						}
					}
				aatmfActivaModalCargarResolucion = 0;
				aatmfNombreArchivoAuxiliar = null;
				aatmfNombreArchivoSubido = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("mensaje", "Documento cargado con éxito"); //Mensaje en modal
			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje", "Debe seleccionar el documento"); //Mensaje en modal
			}
	}
	
	/**
	 * Método para cerrar la ventana de subir archivo
	 */
	
	public void CancelarSubirArchivo(){
		aatmfMateriaSeleccionada= null;
		aatmfActivaModalCargarResolucion=0;
			
	}
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		aatmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		aatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		aatmfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		aatmfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		aatmfListaSolicitudesDto = null;
				
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton guardar  para verificar que cumpla las reglas antes de presentar el modal guardar
	 * @return retora null para para cualquier cosa 
	 */
	public String controlarClickGuardarResolver(){
		Boolean selecionadosTodos=false;
	//	Boolean dentroCronograma= false;
		Boolean resolucionTodos= false;
		aatmfClickGuardarResolver = 0;
		
		
		//VERIFICO  QUE EXISTA EL DIRECTORIO PARA GUARDAR EL PDF	
        String pathDirGuardar =GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RESOLUCION_APELACION_TERCERA_MATRICULA;
			
        File directorio = new File(pathDirGuardar);
        boolean existeDirectorio= true;
			if(!directorio.exists()){
				
				existeDirectorio= false;
			}
			
			
			
		//VERICO QUE EXISTAN MATERIAS 
		if(aatmfListaMateriasSolicitadasDto.size() >= 1){
			for (SolicitudTerceraMatriculaDto materia : aatmfListaMateriasSolicitadasDto) {
				if(materia.getRespuestaSolicitud()!=GeneralesConstantes.APP_ID_BASE){
					selecionadosTodos=true;
				}else{
					selecionadosTodos=false;
					break;					
				}
			}
		//VERIFICO QUE TODAS LAS MATERIAS TENGAN LA RESOLUCION SUBIDA	
			for (SolicitudTerceraMatriculaDto materia : aatmfListaMateriasSolicitadasDto) {
				if(materia.getSltrmtDocumentoResolucion()!=null){
					resolucionTodos=true;
				}else{
					resolucionTodos=false;
					break;					
				}
			}
			
			
			
			//VERIFICO QUE ESTE ACTIVO EL CRONOGRAMA DE TERCERA MATRICULA
			
//			if(aatmfCarreraVer.getCrrId()==82 ||aatmfCarreraVer.getCrrId()==157){ //cronograma abierto para Medicina y medicna rediseño
//				dentroCronograma = true;
//				
//			}else{
//				
//				dentroCronograma=verificarCronogramaSolicitarTerceraMatricula(); //si no cumple dentro de cronograma termina todo el proceso//termina el proceso sino se cumple el cronograma
//				
//			}
			
			
			
			if(existeDirectorio){
				if(verificarCronogramaSolicitarTerceraMatricula()){
			//if(dentroCronograma==true){
			    if(selecionadosTodos==true){
			    	if(resolucionTodos==true){
			    		if((getAatmfObservacionFinal() != null) && (getAatmfObservacionFinal().length() >=1)){	
			    		aatmfClickGuardarResolver = 1;
			    		}else{
			    			aatmfClickGuardarResolver = 0;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.observacion.final.validacion.exception")));
			    		}
			    		
			    	}else{
			    		aatmfClickGuardarResolver = 0;
			    		FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.documento.resolucion.todos.validacion.exception")));
			    		
			    	}
				
		     	}else{
				aatmfClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.seleccionar.todos.validacion.exception")));
			     }
			}//no es necesario el else, el verificar cronograma termina el proceso
			
			}else{
				aatmfClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existe el directorio para guardar la resolución de apelación de tercera matrícula.");
				
			}
			
			
		}else{
			aatmfClickGuardarResolver = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.no.existe.materias.validacion.exception")));
		}
		return null;
	}

	/**
    * Método que desactiva el el botón guardar.
    */
	public void desactivaModalGuardar(){
		aatmfClickGuardarResolver = 0;
	}
	/**
	 * Verifica que el proceso de solicitar tercera matricula exista y se encuentre activo en la fecha actual
	 * @return retora True, si esta el proceso dentro de las fechas y false si no esta dentro de las fechas del cronograma
	 */
	public Boolean verificarCronogramaSolicitarTerceraMatricula() {
		Date fechaActual = new Date();
		Boolean retorno = false;
		// DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		try {
			aatmfDependencia = servAatmfDependencialServicio.buscarFacultadXcrrId(aatmfCarreraVer.getCrrId());
		} catch (DependenciaNoEncontradoException e1) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		if (aatmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			aatmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			aatmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			aatmfCronogramaActividad = servAatmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(aatmfCarreraVer.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, aatmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_RESOLUCION_APELACIONES_TERCERA_MATRICULA_VALUE,
							ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.encontrado.exception")));
		}
		if (aatmfCronogramaActividad != null) {
			if ((aatmfCronogramaActividad.getPlcrFechaInicio() != null)
					&& (aatmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(aatmfCronogramaActividad.getPlcrFechaInicio(),
						aatmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
					retorno = true;
				} else {
					retorno = false;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.habilitado.exception")));
				}
			} else {
				retorno = false;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.sin.fechas.exception")));
			}
		} else {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.encontrado.exception")));
		}
		return retorno;
	}
	
	
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(SolicitudTerceraMatriculaDto materiaSeleccionada){
		aatmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_APELACION_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(aatmfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getSltrmtDocumentoSolicitud();
			try{
//				FileInputStream  fis = new FileInputStream(aatmfArchivoSelSt);
				URL oracle = new URL("file:"+aatmfArchivoSelSt);
//				 URL urlObject = new URL("/");
				    URLConnection urlConnection = oracle.openConnection();
				    InputStream inputStream = urlConnection.getInputStream();
				return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(aatmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				aatmfArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (Exception e) {
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.no.encontrado.exception")));
			return null;
		}
		return null;
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas 
	 * @param materiaDeshabilitar - materiaDeshabilitar entidad seleccionada para deshabilitar el link
	 */
	public void deshabilitar(SolicitudTerceraMatriculaDto materiaDeshabilitar){
		for (SolicitudTerceraMatriculaDto item : aatmfListaMateriasSolicitadasDto) {
			if(materiaDeshabilitar.getRcesId() == item.getRcesId()){
				item.setVisualizador(true);
				break;
			}
		}
	}
	
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public Usuario getAatmfUsuario() {
		return aatmfUsuario;
	}

	public void setAatmfUsuario(Usuario aatmfUsuario) {
		this.aatmfUsuario = aatmfUsuario;
	}
	
	public PeriodoAcademico getAatmfPeriodoAcademicoBuscar() {
		return aatmfPeriodoAcademicoBuscar;
	}

	public void setAatmfPeriodoAcademicoBuscar(PeriodoAcademico aatmfPeriodoAcademicoBuscar) {
		this.aatmfPeriodoAcademicoBuscar = aatmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getAatmfCarreraDtoBuscar() {
		return aatmfCarreraDtoBuscar;
	}

	public void setAatmfCarreraDtoBuscar(CarreraDto aatmfCarreraDtoBuscar) {
		this.aatmfCarreraDtoBuscar = aatmfCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getAatmfListCarreraDtoBusq() {
		return aatmfListCarreraDtoBusq;
	}

	public void setAatmfListCarreraDtoBusq(List<CarreraDto> aatmfListCarreraDtoBusq) {
		this.aatmfListCarreraDtoBusq = aatmfListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getAatmfEstudianteBuscar() {
		return aatmfEstudianteBuscar;
	}

	public void setAatmfEstudianteBuscar(EstudianteJdbcDto aatmfEstudianteBuscar) {
		this.aatmfEstudianteBuscar = aatmfEstudianteBuscar;
	}

	public List<SolicitudTerceraMatriculaDto> getAatmfListaSolicitudesDto() {
		aatmfListaSolicitudesDto = aatmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):aatmfListaSolicitudesDto;
		return aatmfListaSolicitudesDto;
	}

	public void setAatmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> aatmfListaSolicitudesDto) {
		this.aatmfListaSolicitudesDto = aatmfListaSolicitudesDto;
	}

	public Persona getAatmfPersonaSeleccionadaVer() {
		return aatmfPersonaSeleccionadaVer;
	}

	public void setAatmfPersonaSeleccionadaVer(Persona aatmfPersonaSeleccionadaVer) {
		this.aatmfPersonaSeleccionadaVer = aatmfPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getAatmfPeriodoSolicitudesVer() {
		return aatmfPeriodoSolicitudesVer;
	}

	public void setAatmfPeriodoSolicitudesVer(PeriodoAcademico aatmfPeriodoSolicitudesVer) {
		this.aatmfPeriodoSolicitudesVer = aatmfPeriodoSolicitudesVer;
	}

	public Carrera getAatmfCarreraVer() {
		return aatmfCarreraVer;
	}

	public void setAatmfCarreraVer(Carrera aatmfCarreraVer) {
		this.aatmfCarreraVer = aatmfCarreraVer;
	}

	public List<SolicitudTerceraMatriculaDto> getAatmfListaMateriasSolicitadasDto() {
		aatmfListaMateriasSolicitadasDto = aatmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):aatmfListaMateriasSolicitadasDto;
		return aatmfListaMateriasSolicitadasDto;
	}

	public void setAatmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> aatmfListaMateriasSolicitadasDto) {
		this.aatmfListaMateriasSolicitadasDto = aatmfListaMateriasSolicitadasDto;
	}

	public Integer getAatmfValidadorSeleccion() {
		return aatmfValidadorSeleccion;
	}

	public void setAatmfValidadorSeleccion(Integer aatmfValidadorSeleccion) {
		this.aatmfValidadorSeleccion = aatmfValidadorSeleccion;
	}

	

	public List<PeriodoAcademico> getAatmfListaPeriodoAcademicoBusq() {
		aatmfListaPeriodoAcademicoBusq = aatmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):aatmfListaPeriodoAcademicoBusq;
		return aatmfListaPeriodoAcademicoBusq;
	}

	public void setAatmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> aatmfListaPeriodoAcademicoBusq) {
		this.aatmfListaPeriodoAcademicoBusq = aatmfListaPeriodoAcademicoBusq;
	}

	public Integer getAatmfTipoCronograma() {
		return aatmfTipoCronograma;
	}

	public void setAatmfTipoCronograma(Integer aatmfTipoCronograma) {
		this.aatmfTipoCronograma = aatmfTipoCronograma;
	}

	public Dependencia getAatmfDependencia() {
		return aatmfDependencia;
	}

	public void setAatmfDependencia(Dependencia aatmfDependencia) {
		this.aatmfDependencia = aatmfDependencia;
	}

	public CronogramaActividadJdbcDto getAatmfCronogramaActividad() {
		return aatmfCronogramaActividad;
	}

	public void setAatmfCronogramaActividad(CronogramaActividadJdbcDto aatmfCronogramaActividad) {
		this.aatmfCronogramaActividad = aatmfCronogramaActividad;
	}

	public PeriodoAcademico getAatmfPeriodoAcademicoActivo() {
		return aatmfPeriodoAcademicoActivo;
	}

	public void setAatmfPeriodoAcademicoActivo(PeriodoAcademico aatmfPeriodoAcademicoActivo) {
		this.aatmfPeriodoAcademicoActivo = aatmfPeriodoAcademicoActivo;
	}

	public String getAatmfArchivoSelSt() {
		return aatmfArchivoSelSt;
	}

	public void setAatmfArchivoSelSt(String aatmfArchivoSelSt) {
		this.aatmfArchivoSelSt = aatmfArchivoSelSt;
	}

	public Integer getAatmfActivaModalCargarResolucion() {
		return aatmfActivaModalCargarResolucion;
	}

	public void setAatmfActivaModalCargarResolucion(Integer aatmfActivaModalCargarResolucion) {
		this.aatmfActivaModalCargarResolucion = aatmfActivaModalCargarResolucion;
	}

	public SolicitudTerceraMatriculaDto getAatmfMateriaSeleccionada() {
		return aatmfMateriaSeleccionada;
	}

	public void setAatmfMateriaSeleccionada(SolicitudTerceraMatriculaDto aatmfMateriaSeleccionada) {
		this.aatmfMateriaSeleccionada = aatmfMateriaSeleccionada;
	}

	public Integer getAatmfClickGuardarResolver() {
		return aatmfClickGuardarResolver;
	}

	public void setAatmfClickGuardarResolver(Integer aatmfClickGuardarResolver) {
		this.aatmfClickGuardarResolver = aatmfClickGuardarResolver;
	}

	public FichaInscripcionDto getAatmfFichaInscripcion() {
		return aatmfFichaInscripcion;
	}

	public void setAatmfFichaInscripcion(FichaInscripcionDto aatmfFichaInscripcion) {
		this.aatmfFichaInscripcion = aatmfFichaInscripcion;
	}

	public String getAatmfNombreArchivoSubido() {
		return aatmfNombreArchivoSubido;
	}

	public void setAatmfNombreArchivoSubido(String aatmfNombreArchivoSubido) {
		this.aatmfNombreArchivoSubido = aatmfNombreArchivoSubido;
	}

	public String getAatmfNombreArchivoAuxiliar() {
		return aatmfNombreArchivoAuxiliar;
	}

	public void setAatmfNombreArchivoAuxiliar(String aatmfNombreArchivoAuxiliar) {
		this.aatmfNombreArchivoAuxiliar = aatmfNombreArchivoAuxiliar;
	}

	public String getAatmfObservacionFinal() {
		return aatmfObservacionFinal;
	}

	public void setAatmfObservacionFinal(String aatmfObservacionFinal) {
		this.aatmfObservacionFinal = aatmfObservacionFinal;
	}

	public List<SolicitudTerceraMatriculaDto> getAatmfListaMateriasReporte() {
		aatmfListaMateriasReporte = aatmfListaMateriasReporte==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):aatmfListaMateriasReporte;
	return aatmfListaMateriasReporte;
	}

	public void setAatmfListaMateriasReporte(List<SolicitudTerceraMatriculaDto> aatmfListaMateriasReporte) {
		this.aatmfListaMateriasReporte = aatmfListaMateriasReporte;
	}

	public Boolean getAatmfDesactivaBotonGuardar() {
		return aatmfDesactivaBotonGuardar;
	}

	public void setAatmfDesactivaBotonGuardar(Boolean aatmfDesactivaBotonGuardar) {
		this.aatmfDesactivaBotonGuardar = aatmfDesactivaBotonGuardar;
	}

	public String getAatmfObservacionReporte() {
		return aatmfObservacionReporte;
	}

	public void setAatmfObservacionReporte(String aatmfObservacionReporte) {
		this.aatmfObservacionReporte = aatmfObservacionReporte;
	}

	public Integer getAatmfActivaReportePDF() {
		return aatmfActivaReportePDF;
	}

	public void setAatmfActivaReportePDF(Integer aatmfActivaReportePDF) {
		this.aatmfActivaReportePDF = aatmfActivaReportePDF;
	}

	public Boolean getAatmfDesactivaBotonReporte() {
		return aatmfDesactivaBotonReporte;
	}

	public void setAatmfDesactivaBotonReporte(Boolean aatmfDesactivaBotonReporte) {
		this.aatmfDesactivaBotonReporte = aatmfDesactivaBotonReporte;
	}
	
	
	
	

}
