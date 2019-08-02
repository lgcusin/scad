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
   
 ARCHIVO:     AprobacionSolicitudTercerMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la aprobacion/negacion hecha por el director de carrera a la solicitud de tercera matricula realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-02-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.habilitacionTerceraMatricula;
import java.io.File;
import java.io.FileNotFoundException;
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
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteAprobacionSolicitudTerceraForm;
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
 * Clase (session bean) AprobarSolicitudTercerMatriculaForm. 
 * Bean de sesion que maneja la la aprobación/negación hecha por el director de carrera a la solicitud de tercera matricula realizada por el estudiante. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "aprobacionSolicitudTerceraMatriculaForm")
@SessionScoped
public class AprobacionSolicitudTerceraMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario astmfUsuario;
	//PARA BUSQUEDA
	private PeriodoAcademico astmfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> astmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico astmfPeriodoAcademicoActivo;
	private CarreraDto astmfCarreraDtoBuscar;
	private List<CarreraDto> astmfListCarreraDtoBusq;
	private EstudianteJdbcDto astmfEstudianteBuscar;
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD
	private List<SolicitudTerceraMatriculaDto> astmfListaSolicitudesDto;
	//VER MATERIAS
	private Persona astmfPersonaSeleccionadaVer;
	private PeriodoAcademico astmfPeriodoSolicitudesVer;
	private Carrera astmfCarreraVer;
	private FichaInscripcionDto astmfFichaInscripcion;
	private List<SolicitudTerceraMatriculaDto> astmfListaMateriasSolicitadasDto;
	private Integer astmfValidadorSeleccion;
	private Integer astmfClickGuardarResolver;
	private Integer astmfTipoCronograma;
	private Dependencia astmfDependencia;
	private CronogramaActividadJdbcDto astmfCronogramaActividad;
	private String astmfArchivoSelSt;
	private Boolean astmfDesactivaBotonGuardar;
	private String astmfObservacionFinal;
	
	//REPORTE
	private Integer astmfActivaReportePDF;
	private Boolean astmfDesactivaBotonReporte;
	
	private List<SolicitudTerceraMatriculaDto>  astmfListaMateriasReporte;
	private String astmfObservacionReporte;


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
	PeriodoAcademicoServicio servAstmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servAstmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servAstmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servAstmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servAstmfPersonaServicio;
	@EJB 
	CarreraServicio servAstmfCarreraServicio;
	@EJB 
	DependenciaServicio servAstmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servAstmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servAstmfSolicitudTerceraMatriculaServicioServicioJdbc;
	@EJB 
	FichaInscripcionDtoServicioJdbc servAstmfFichaInscripcionDto;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irAListarEstudiantes(Usuario usuario) {
		astmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		   astmfPeriodoAcademicoActivo = servAstmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		   astmfListaPeriodoAcademicoBusq=servAstmfPeriodoAcademicoServicio.listarTodos();
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			astmfListCarreraDtoBusq = servAstmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(astmfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, astmfPeriodoAcademicoActivo.getPracId());
			retorno = "irAListarEstudiantesApruebaSolicitud";
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
		astmfListaSolicitudesDto=new ArrayList<SolicitudTerceraMatriculaDto>();
			try {
				
				//Verifico que se seleecione una carrera
				if(astmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					astmfListaSolicitudesDto=servAstmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitudSIIUSAU(astmfPeriodoAcademicoBuscar.getPracId(), astmfCarreraDtoBuscar.getCrrId(), astmfEstudianteBuscar.getPrsPrimerApellido(), astmfEstudianteBuscar.getPrsIdentificacion(), SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE);
				}else{
					astmfListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
				
			} catch (SolicitudTerceraMatriculaDtoException e) {
				astmfListaSolicitudesDto=null;
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
		
		String retorno =null;
		
		astmfListaMateriasSolicitadasDto= new ArrayList<>();
		astmfPersonaSeleccionadaVer= new Persona();
		astmfPeriodoSolicitudesVer= new PeriodoAcademico();
		astmfCarreraVer=new Carrera();
		astmfObservacionFinal= null;
		//Seteo como activo la opción de descarga del archivo
		for (SolicitudTerceraMatriculaDto item : astmfListaMateriasSolicitadasDto) {
			item.setVisualizador(false);
			item.setRespuestaSolicitud(GeneralesConstantes.APP_ID_BASE);
		}
		    //Se busca el periodo, persona, carrera y lista de materias en las que solicito y verifico la solicitud
		try {
			astmfPeriodoSolicitudesVer=servAstmfPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			astmfPersonaSeleccionadaVer=servAstmfPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			astmfCarreraVer=servAstmfCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			astmfListaMateriasSolicitadasDto=servAstmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(solicitudesEstudiante.getPracId(), solicitudesEstudiante.getCrrId(), solicitudesEstudiante.getFcesId(), SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE);
		    
			retorno="irVerMateriasSolicitadas";
		
		} catch (SolicitudTerceraMatriculaDtoException e) {
			astmfPeriodoSolicitudesVer=null;
			astmfPersonaSeleccionadaVer=null;
			astmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  catch (PeriodoAcademicoNoEncontradoException e) {
			astmfPeriodoSolicitudesVer=null;
			astmfPersonaSeleccionadaVer=null;
			astmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			astmfPeriodoSolicitudesVer=null;
			astmfPersonaSeleccionadaVer=null;
			astmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			astmfPeriodoSolicitudesVer=null;
			astmfPersonaSeleccionadaVer=null;
			astmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			astmfPeriodoSolicitudesVer=null;
			astmfPersonaSeleccionadaVer=null;
			astmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno ;
	}
	

	
	/**
	 * Método que guarda la respuesta a la solicitud de tercera matricula en las materias solicitadas
	 * @return retorna - la navegación de la página listar estudiantes.
	 */
	public String guardarResolver(){
	String retorno = null;
	astmfListaMateriasReporte= new ArrayList<>();
		//REALIZA LA ACTUALIZACIÓN DE LOS ESTADOS DE  RECORD ACADEMICO Y SOLICITUD TERCERA MATRICULA
			if(astmfListaMateriasSolicitadasDto!=null && astmfListaMateriasSolicitadasDto.size()>0 ){ //verifica que la lisa no este nula
			
//				int estadoFichaInscripcionAux= FichaInscripcionConstantes.INACTIVO_VALUE;
//				//verificacmos si se aprobo todas las solicitudes para cambiar el estado de activo en ficha inscripcion
//				for (SolicitudTerceraMatriculaDto materia : astmfListaMateriasSolicitadasDto) {
//					if(materia.getRespuestaSolicitud()==SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE){
//						estadoFichaInscripcionAux= FichaInscripcionConstantes.DESBLOQUEADO_TERCERA_MATRICULA_VALUE;
//						
//					}else{
//						estadoFichaInscripcionAux= FichaInscripcionConstantes.BLOQUEADO_TERCERA_MATRICULA_VALUE;
//						break;
//					}
//						
//				}
				//Busco la ficha Inscripcion del estudiante para luego activarla si es el caso
				try {
					astmfFichaInscripcion=servAstmfFichaInscripcionDto.buscarFcinXidentificacionXcarrera(astmfPersonaSeleccionadaVer.getPrsIdentificacion(), astmfCarreraVer.getCrrId());
				} catch (FichaInscripcionDtoException e1) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e1.getMessage());
				} catch (FichaInscripcionDtoNoEncontradoException e1) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(e1.getMessage());
				}		
				
			
		
				 
				//proceso de transacción
				try{
					if(servAstmfSolicitudTerceraMatriculaServicioServicioJdbc.generarAprobacionSolicitudTerceraMatricula(astmfListaMateriasSolicitadasDto,astmfFichaInscripcion.getFcinId(), astmfUsuario.getUsrNick(), astmfObservacionFinal )){
						
						
						//copio las lista para el reporte PDF
						for (SolicitudTerceraMatriculaDto solicitudDto : astmfListaMateriasSolicitadasDto) {
							try {
								SolicitudTerceraMatricula solicitudBase=servAstmfSolicitudTerceraMatriculaServicioServicioJdbc.buscarPorId(solicitudDto.getSltrmtId());
								solicitudDto.setSltrmtEstado(solicitudBase.getSltrmtEstado());
								astmfListaMateriasReporte.add(solicitudDto);
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
							
							if(astmfPersonaSeleccionadaVer!=null){
							frmRrmNombreReporte=astmfPersonaSeleccionadaVer.getPrsNombres()+" "+astmfPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
									+(astmfPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":astmfPersonaSeleccionadaVer.getPrsSegundoApellido());
							}else{
								frmRrmNombreReporte="Reporte-Aprobacion";
							}
							
							frmRrmParametros = new HashMap<String, Object>();
							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
							//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
							String fecha = formato.format(new Date());
							frmRrmParametros.put("fecha",fecha);
							
//							if(astmfCarreraVer.getCrrDependencia().getDpnDescripcion()!=null){
//							frmRrmParametros.put("facultad", astmfCarreraVer.getCrrDependencia().getDpnDescripcion());
//							}else{
//								frmRrmParametros.put("facultad", " ");
//							}
							
//							if(astmfCarreraVer.getCrrDescripcion()!=null){
//							frmRrmParametros.put("carrera", astmfCarreraVer.getCrrDescripcion());
//							}else{
//								frmRrmParametros.put("carrera", " ");
//							}
							
							String nombres=null;
							if(astmfPersonaSeleccionadaVer!=null){
							 nombres=astmfPersonaSeleccionadaVer.getPrsNombres()+" "+astmfPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
									+(astmfPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":astmfPersonaSeleccionadaVer.getPrsSegundoApellido());
							}else{
								nombres=" ";
							}
							
							frmRrmParametros.put("nombre",nombres );
							
//							if(astmfPersonaSeleccionadaVer.getPrsIdentificacion()!=null){
//							frmRrmParametros.put("identificacion", astmfPersonaSeleccionadaVer.getPrsIdentificacion());
//							}else{
//								frmRrmParametros.put("identificacion", " ");	
//							}
							
							
							
							StringBuilder sbTextoInicial = new StringBuilder();
							sbTextoInicial.append("Señor(a)(ita) ");sbTextoInicial.append("\n");
							sbTextoInicial.append(nombres);sbTextoInicial.append("\n");
							sbTextoInicial.append("ESTUDIANTE DE LA CARRERA DE ");
							if(astmfCarreraVer.getCrrDescripcion()!=null){
							sbTextoInicial.append(astmfCarreraVer.getCrrDescripcion());sbTextoInicial.append("\n");
							}else{
								sbTextoInicial.append(" ");sbTextoInicial.append("\n");
							}
							
							sbTextoInicial.append("FACULTAD DE ");
							if(astmfCarreraVer.getCrrDependencia().getDpnDescripcion()!=null){
								sbTextoInicial.append(astmfCarreraVer.getCrrDependencia().getDpnDescripcion());sbTextoInicial.append("\n");
							}else{
								sbTextoInicial.append(" ");sbTextoInicial.append("\n");								
							}
							sbTextoInicial.append("Presente.-"); sbTextoInicial.append("\n\n");
							frmRrmParametros.put("textoInicial", sbTextoInicial.toString());			
							
							StringBuilder sbTexto = new StringBuilder();
							sbTexto.append("Una vez analizadas y validadas las solicitudes de Tercera Matrícula considerando la normativa vigente, a continuación se detalla el informe final:");
							frmRrmParametros.put("texto", sbTexto.toString());
							
							StringBuilder sbPeriodo = new StringBuilder();
							StringBuilder sbCodigo = new StringBuilder();
							StringBuilder sbAsignatura = new StringBuilder();
							StringBuilder sbHora = new StringBuilder();
							StringBuilder sbCausal = new StringBuilder();
							StringBuilder sbEvidencia = new StringBuilder();
							StringBuilder sbEstado = new StringBuilder();
							
							for (SolicitudTerceraMatriculaDto item : astmfListaMateriasReporte) {
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
									if(item.getSltrmtEstado()==2){
										sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
									}
									if(item.getSltrmtEstado()==3){
										sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
									}
									if(item.getSltrmtEstado()==4){
										sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n");
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
										if(item.getSltrmtEstado()==2){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n");
										}
										if(item.getSltrmtEstado()==3){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n");
										}
										if(item.getSltrmtEstado()==4){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n");
										}
										
									}else{
										sbEstado.append(" ");sbEstado.append("\n\n\n");
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
										if(item.getSltrmtEstado()==2){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n");
										}
										if(item.getSltrmtEstado()==3){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n");
										}
										if(item.getSltrmtEstado()==4){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n");
										}
										
									}else{
										sbEstado.append(" ");sbEstado.append("\n\n\n\n");
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
										if(item.getSltrmtEstado()==2){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n");
										}
										if(item.getSltrmtEstado()==3){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n");
										}
										if(item.getSltrmtEstado()==4){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n");
										}
										
									}else{
										sbEstado.append(" ");sbEstado.append("\n\n\n\n\n");
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
										if(item.getSltrmtEstado()==2){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n\n");
										}
										if(item.getSltrmtEstado()==3){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n\n");
										}
										if(item.getSltrmtEstado()==4){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n\n");
										}
										
									}else{
										sbEstado.append(" ");sbEstado.append("\n\n\n\n\n\n");
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
										if(item.getSltrmtEstado()==2){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n\n\n");
										}
										if(item.getSltrmtEstado()==3){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n\n\n");
										}
										if(item.getSltrmtEstado()==4){
											sbEstado.append(SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL);sbEstado.append("\n\n\n\n\n\n\n");
										}
										
									}else{
										sbEstado.append(" ");sbEstado.append("\n\n\n\n\n\n\n");
									}
									
								}
								frmRrmParametros.put("periodo", sbPeriodo.toString());
								frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
								frmRrmParametros.put("asignatura", sbAsignatura.toString());
								frmRrmParametros.put("numero", sbHora.toString());
								frmRrmParametros.put("causal", sbCausal.toString());
								frmRrmParametros.put("evidencia", sbEvidencia.toString());
								frmRrmParametros.put("sltrmtEstado", sbEstado.toString());
								
																							
									String directorCarrera= null;
									
									if(astmfUsuario!=null){
									  directorCarrera = astmfUsuario.getUsrPersona().getPrsNombres()+" "+astmfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
											+(astmfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":astmfUsuario.getUsrPersona().getPrsSegundoApellido());
									}else{
										directorCarrera=" ";
									
									}
									frmRrmParametros.put("dirCarrera",directorCarrera);
															
								StringBuilder sbObservaciones = new StringBuilder();
								
								if(astmfObservacionFinal!=null){
								sbObservaciones.append("Observaciones: ");sbObservaciones.append("\n");
								sbObservaciones.append(astmfObservacionFinal);
								}else{
									sbObservaciones.append("Observaciones: ");sbObservaciones.append("\n");
									
								}
								frmRrmParametros.put("observaciones", sbObservaciones.toString());
								
								if( astmfUsuario.getUsrNick()!=null){
									frmRrmParametros.put("nick", astmfUsuario.getUsrNick());
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
							pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteAprobacionTercera");
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
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/reporteDirCarrera.jasper")));
							jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
							
							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
										
							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(astmfPersonaSeleccionadaVer.getPrsMailInstitucional());
							//path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo= new StringBuilder();
							formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
							fecha = formato.format(new Date());
							sbCorreo= GeneralesUtilidades.generarAsuntoAprobacionTercera(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									nombres, GeneralesUtilidades.generaStringParaCorreo(astmfCarreraVer.getCrrDescripcion()));
							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_RESPUESTA_SOLICITUD_TERCERA_MATRICULA,
												sbCorreo.toString()
												, "admin", "dt1c201s", true, arreglo, "respuestaSolicitudTerceraMatricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
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
							
							astmfActivaReportePDF=1;
						} catch (Exception e) {
							e.printStackTrace();
						}
							//******************************************************************************
							//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
							//******************************************************************************
						
						astmfDesactivaBotonReporte= Boolean.FALSE; //activo boton generar PDF
						astmfDesactivaBotonGuardar=Boolean.TRUE;
						
						//GENERAR REPORTE AUTOMATICAMENTE
//						 ReporteAprobacionSolicitudTerceraForm.generarReporteAprobacionTercera(astmfListaMateriasReporte, astmfDependencia, astmfCarreraVer, astmfPersonaSeleccionadaVer, astmfPeriodoAcademicoActivo, astmfUsuario);
					
						 //FIN REPORTE 
						astmfListaMateriasSolicitadasDto=null;
						astmfClickGuardarResolver = 0;
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.validacion.con.exito")));
					    
					}else{
						astmfClickGuardarResolver = 0;
					
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.validacion.sin.exito")));
					}
				     
					//retorno = "irAListarEstudiantesApruebaSolicitud";
				} catch (SolicitudTerceraMatriculaException e) {
					astmfClickGuardarResolver = 0;
					
					astmfListaMateriasSolicitadasDto=null;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.exception")));
				} catch (Exception e) {
					astmfClickGuardarResolver = 0;
					
					astmfListaMateriasSolicitadasDto=null;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.exception")));
				}
			}else{
				astmfClickGuardarResolver = 0;
			
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.guardarResolver.validacion.no.existe.materias")));
			}
			//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			astmfClickGuardarResolver = 0;
			setAstmfObservacionReporte(getAstmfObservacionFinal());// necesito guardarme el valor en otra variable antes de que se anule la variable y se refresque la pantalla al guardar la solicitudes
			astmfObservacionFinal= null;
			
            //VACIO LA LISTA DE SOLICITUDES   
			astmfListaSolicitudesDto= null;
		return retorno;
	}
	
	/**
	 * Método que llama al generar el reporte
	 * 
	 */
	
	public void llamarReporte(){
		//Activo el pdf
	   
		//GENERAR REPORTE
		 ReporteAprobacionSolicitudTerceraForm.generarReporteAprobacionTercera(astmfListaMateriasReporte, astmfDependencia, astmfCarreraVer, astmfPersonaSeleccionadaVer, astmfPeriodoAcademicoActivo, astmfUsuario,astmfObservacionReporte );
		 astmfActivaReportePDF=1;
	
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
		astmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		astmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		astmfEstudianteBuscar = new EstudianteJdbcDto();
		astmfListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		astmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		astmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		astmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		astmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		astmfValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		astmfClickGuardarResolver=0;
		astmfActivaReportePDF=0;
		astmfDesactivaBotonReporte= Boolean.TRUE;
		astmfDesactivaBotonGuardar=Boolean.FALSE;
		astmfObservacionFinal= null;
		astmfObservacionReporte= null;
		
	}
	
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		astmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		astmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		astmfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		astmfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		astmfListaSolicitudesDto = null;
				
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
//		Boolean dentroCronograma= false;
		astmfClickGuardarResolver = 0;
		//VERICO QUE EXISTAN MATERIAS 
		if(astmfListaMateriasSolicitadasDto.size() >= 1){
			for (SolicitudTerceraMatriculaDto materia : astmfListaMateriasSolicitadasDto) {
				if(materia.getRespuestaSolicitud()!=GeneralesConstantes.APP_ID_BASE){
					selecionadosTodos=true;
				}else{
					selecionadosTodos=false;
					break;					
				}
			}	
			
			
			
				
			//VERIFICO QUE ESTE ACTIVO EL CRONOGRAMA DE TERCERA MATRICULA
			
//			if(astmfCarreraVer.getCrrId()==82 ||astmfCarreraVer.getCrrId()==157){ //cronograma abierto para Medicina y medicna rediseño
//				dentroCronograma = true;
//				
//			}else{
//				
//				dentroCronograma=verificarCronogramaSolicitarTerceraMatricula();//termina el proceso sino se cumple el cronograma
//				
//			}
			
		if(verificarCronogramaSolicitarTerceraMatricula())	{
	//		if(dentroCronograma==true){
			    if(selecionadosTodos==true){
   	

			    	if((getAstmfObservacionFinal() != null) && (getAstmfObservacionFinal().length() >=1)){
				       astmfClickGuardarResolver = 1;
			    	}else{
			    	astmfClickGuardarResolver = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.observacion.validacion.exception")));
			    	}
		     	}else{
				astmfClickGuardarResolver = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.seleccionar.todos.validacion.exception")));
			     }
			}//no es necesario el else, el verificar cronograma termina el proceso
		}else{
			astmfClickGuardarResolver = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.controlarClickGuardarResolver.no.existe.materias.validacion.exception")));
		}
		return null;
	}

	
	public void desactivaModalGuardar(){
		astmfClickGuardarResolver = 0;
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
			astmfDependencia = servAstmfDependencialServicio.buscarFacultadXcrrId(astmfCarreraVer.getCrrId());
		} catch (DependenciaNoEncontradoException e1) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		if (astmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			astmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			astmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			astmfCronogramaActividad = servAstmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(astmfCarreraVer.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, astmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_VALIDACION_SOLICITUDES_TERCERA_MATRICULA_VALUE,
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
		if (astmfCronogramaActividad != null) {
			if ((astmfCronogramaActividad.getPlcrFechaInicio() != null)
					&& (astmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(astmfCronogramaActividad.getPlcrFechaInicio(),
						astmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
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
		astmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(astmfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getSltrmtDocumentoSolicitud();
			try{
//				FileInputStream  fis = new FileInputStream(astmfArchivoSelSt);
				URL oracle = new URL("file:"+astmfArchivoSelSt);
//				 URL urlObject = new URL("/");
				    URLConnection urlConnection = oracle.openConnection();
				    InputStream inputStream = urlConnection.getInputStream();
				return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(astmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				astmfArchivoSelSt = null;
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
		for (SolicitudTerceraMatriculaDto item : astmfListaMateriasSolicitadasDto) {
			if(materiaDeshabilitar.getRcesId() == item.getRcesId()){
				item.setVisualizador(true);
				break;
			}
		}
	}
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public Usuario getAstmfUsuario() {
		return astmfUsuario;
	}

	public void setAstmfUsuario(Usuario astmfUsuario) {
		this.astmfUsuario = astmfUsuario;
	}
	
	public PeriodoAcademico getAstmfPeriodoAcademicoBuscar() {
		return astmfPeriodoAcademicoBuscar;
	}

	public void setAstmfPeriodoAcademicoBuscar(PeriodoAcademico astmfPeriodoAcademicoBuscar) {
		this.astmfPeriodoAcademicoBuscar = astmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getAstmfCarreraDtoBuscar() {
		return astmfCarreraDtoBuscar;
	}

	public void setAstmfCarreraDtoBuscar(CarreraDto astmfCarreraDtoBuscar) {
		this.astmfCarreraDtoBuscar = astmfCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getAstmfListCarreraDtoBusq() {
		return astmfListCarreraDtoBusq;
	}

	public void setAstmfListCarreraDtoBusq(List<CarreraDto> astmfListCarreraDtoBusq) {
		this.astmfListCarreraDtoBusq = astmfListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getAstmfEstudianteBuscar() {
		return astmfEstudianteBuscar;
	}

	public void setAstmfEstudianteBuscar(EstudianteJdbcDto astmfEstudianteBuscar) {
		this.astmfEstudianteBuscar = astmfEstudianteBuscar;
	}

	public List<SolicitudTerceraMatriculaDto> getAstmfListaSolicitudesDto() {
		astmfListaSolicitudesDto = astmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):astmfListaSolicitudesDto;
		return astmfListaSolicitudesDto;
	}

	public void setAstmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> astmfListaSolicitudesDto) {
		this.astmfListaSolicitudesDto = astmfListaSolicitudesDto;
	}

	public Persona getAstmfPersonaSeleccionadaVer() {
		return astmfPersonaSeleccionadaVer;
	}

	public void setAstmfPersonaSeleccionadaVer(Persona astmfPersonaSeleccionadaVer) {
		this.astmfPersonaSeleccionadaVer = astmfPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getAstmfPeriodoSolicitudesVer() {
		return astmfPeriodoSolicitudesVer;
	}

	public void setAstmfPeriodoSolicitudesVer(PeriodoAcademico astmfPeriodoSolicitudesVer) {
		this.astmfPeriodoSolicitudesVer = astmfPeriodoSolicitudesVer;
	}

	public Carrera getAstmfCarreraVer() {
		return astmfCarreraVer;
	}

	public void setAstmfCarreraVer(Carrera astmfCarreraVer) {
		this.astmfCarreraVer = astmfCarreraVer;
	}

	public List<SolicitudTerceraMatriculaDto> getAstmfListaMateriasSolicitadasDto() {
		astmfListaMateriasSolicitadasDto = astmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):astmfListaMateriasSolicitadasDto;
		return astmfListaMateriasSolicitadasDto;
	}

	public void setAstmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> astmfListaMateriasSolicitadasDto) {
		this.astmfListaMateriasSolicitadasDto = astmfListaMateriasSolicitadasDto;
	}

	public Integer getAstmfValidadorSeleccion() {
		return astmfValidadorSeleccion;
	}

	public void setAstmfValidadorSeleccion(Integer astmfValidadorSeleccion) {
		this.astmfValidadorSeleccion = astmfValidadorSeleccion;
	}

	public Integer getAstmfClickGuardarResolver() {
		return astmfClickGuardarResolver;
	}

	
	public void setAstmfClickGuardarResolver(Integer astmfClickGuardarResolver) {
		this.astmfClickGuardarResolver = astmfClickGuardarResolver;
	}
	

	
	public Integer getAstmfActivaReportePDF() {
		return astmfActivaReportePDF;
	}

	public void setAstmfActivaReportePDF(Integer astmfActivaReportePDF) {
		this.astmfActivaReportePDF = astmfActivaReportePDF;
	}

	public List<PeriodoAcademico> getAstmfListaPeriodoAcademicoBusq() {
		astmfListaPeriodoAcademicoBusq = astmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):astmfListaPeriodoAcademicoBusq;
		return astmfListaPeriodoAcademicoBusq;
	}

	public void setAstmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> astmfListaPeriodoAcademicoBusq) {
		this.astmfListaPeriodoAcademicoBusq = astmfListaPeriodoAcademicoBusq;
	}

	public Integer getAstmfTipoCronograma() {
		return astmfTipoCronograma;
	}

	public void setAstmfTipoCronograma(Integer astmfTipoCronograma) {
		this.astmfTipoCronograma = astmfTipoCronograma;
	}

	public Dependencia getAstmfDependencia() {
		return astmfDependencia;
	}

	public void setAstmfDependencia(Dependencia astmfDependencia) {
		this.astmfDependencia = astmfDependencia;
	}

	public CronogramaActividadJdbcDto getAstmfCronogramaActividad() {
		return astmfCronogramaActividad;
	}

	public void setAstmfCronogramaActividad(CronogramaActividadJdbcDto astmfCronogramaActividad) {
		this.astmfCronogramaActividad = astmfCronogramaActividad;
	}

	public PeriodoAcademico getAstmfPeriodoAcademicoActivo() {
		return astmfPeriodoAcademicoActivo;
	}

	public void setAstmfPeriodoAcademicoActivo(PeriodoAcademico astmfPeriodoAcademicoActivo) {
		this.astmfPeriodoAcademicoActivo = astmfPeriodoAcademicoActivo;
	}

	public String getAstmfArchivoSelSt() {
		return astmfArchivoSelSt;
	}

	public void setAstmfArchivoSelSt(String astmfArchivoSelSt) {
		this.astmfArchivoSelSt = astmfArchivoSelSt;
	}

	public FichaInscripcionDto getAstmfFichaInscripcion() {
		return astmfFichaInscripcion;
	}

	public void setAstmfFichaInscripcion(FichaInscripcionDto astmfFichaInscripcion) {
		this.astmfFichaInscripcion = astmfFichaInscripcion;
	}

	public List<SolicitudTerceraMatriculaDto> getAstmfListaMateriasReporte() {
		astmfListaMateriasReporte = astmfListaMateriasReporte==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):astmfListaMateriasReporte;
		return astmfListaMateriasReporte;
	}

	public void setAstmfListaMateriasReporte(List<SolicitudTerceraMatriculaDto> astmfListaMateriasReporte) {
		this.astmfListaMateriasReporte = astmfListaMateriasReporte;
	}

	public Boolean getAstmfDesactivaBotonReporte() {
		return astmfDesactivaBotonReporte;
	}

	public void setAstmfDesactivaBotonReporte(Boolean astmfDesactivaBotonReporte) {
		this.astmfDesactivaBotonReporte = astmfDesactivaBotonReporte;
	}

	public Boolean getAstmfDesactivaBotonGuardar() {
		return astmfDesactivaBotonGuardar;
	}

	public void setAstmfDesactivaBotonGuardar(Boolean astmfDesactivaBotonGuardar) {
		this.astmfDesactivaBotonGuardar = astmfDesactivaBotonGuardar;
	}

	public String getAstmfObservacionFinal() {
		return astmfObservacionFinal;
	}

	public void setAstmfObservacionFinal(String astmfObservacionFinal) {
		this.astmfObservacionFinal = astmfObservacionFinal;
	}

	public String getAstmfObservacionReporte() {
		return astmfObservacionReporte;
	}

	public void setAstmfObservacionReporte(String astmfObservacionReporte) {
		this.astmfObservacionReporte = astmfObservacionReporte;
	}

		
	
	

}
