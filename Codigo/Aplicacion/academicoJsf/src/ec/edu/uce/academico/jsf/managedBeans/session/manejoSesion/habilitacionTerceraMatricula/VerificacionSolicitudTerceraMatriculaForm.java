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
   
 ARCHIVO:     VerificacionSolicitudTercerMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la verificación hecha por la secretaria de la solicitud de tercera matricula realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-02-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.habilitacionTerceraMatricula;

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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
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
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.GeneradorMails;

/**
 * Clase (session bean) VerificacionSolicitudTercerMatriculaForm. 
 * Bean de sesion que maneja la verificación hecha por la secretaria de la solicitud de tercera matricula realizada por el estudiante.. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "verificacionSolicitudTerceraMatriculaForm")
@SessionScoped
public class VerificacionSolicitudTerceraMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario vstmfUsuario;
	
	//PARA BUSQUEDA
	private PeriodoAcademico vstmfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> vstmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico vstmfPeriodoAcademicoActivo;
	private CarreraDto vstmfCarreraDtoBuscar;
	private List<CarreraDto> vstmfListCarreraDtoBusq;
	private EstudianteJdbcDto vstmfEstudianteBuscar;
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD
	private List<SolicitudTerceraMatriculaDto> vstmfListaSolicitudesDto;
	//VER MATERIAS
	private Persona vstmfPersonaSeleccionadaVer;
	private PeriodoAcademico vstmfPeriodoSolicitudesVer;
	private Carrera vstmfCarreraVer;
	private List<SolicitudTerceraMatriculaDto> vstmfListaMateriasSolicitadasDto;
	private Integer vstmfValidadorSeleccion;
	private Integer vstmfClickGuardarVerificacion;
	private Integer vstmfTipoCronograma;
	private Dependencia vstmfDependencia;
	private CronogramaActividadJdbcDto vstmfCronogramaActividad;
	
	
	private String vstmfArchivoSelSt;

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
	PeriodoAcademicoServicio servVstmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servVstmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servVstmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servVstmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servVstmfPersonaServicio;
	@EJB 
	CarreraServicio servVstmfCarreraServicio;
	@EJB 
	DependenciaServicio servVstmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servVstmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servVstmfSolicitudTerceraMatriculaServicioServicioJdbc;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irAListarEstudiantes(Usuario usuario) {
		vstmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		   vstmfPeriodoAcademicoActivo = servVstmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		  
		   vstmfListaPeriodoAcademicoBusq=servVstmfPeriodoAcademicoServicio.listarTodos();
		   
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			vstmfListCarreraDtoBusq = servVstmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(vstmfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, vstmfPeriodoAcademicoActivo.getPracId());
			
			retorno = "irAListarEstudiantes";
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
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron la tercera matrícula con los parámetros ingresados
	 */
	public void buscarEstudiantes(){
		vstmfListaSolicitudesDto=new ArrayList<SolicitudTerceraMatriculaDto>();
	
			try {
				//Verifico que se seleecione una carrera
				if(vstmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					
					vstmfListaSolicitudesDto=servVstmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitudSIIUSAU(vstmfPeriodoAcademicoBuscar.getPracId(), vstmfCarreraDtoBuscar.getCrrId(), vstmfEstudianteBuscar.getPrsPrimerApellido(), vstmfEstudianteBuscar.getPrsIdentificacion(), SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE);
                        
				}else{
					vstmfListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
				
			} catch (SolicitudTerceraMatriculaDtoException e) {
				   vstmfListaSolicitudesDto=null;
				   FacesUtil.limpiarMensaje();
				   FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.buscarEstudiantes.exception")));
			}
		
	}
	
	
	
	/**
	 * Método que permite ir a la visualización de las materias que se solicito tercera matrícula.
	 * @param solicitudesEstudiante - solicitudesEstudiante objeto  estudiante que se ha seleccionado
	 * @return La navegación hacia la página xhtml de ver materias solcitadas tercera matrícula.
	 */
	public String irVerMateriasSolicitadas(SolicitudTerceraMatriculaDto solicitudesEstudiante){
	  String retorno=null;
		vstmfListaMateriasSolicitadasDto= new ArrayList<>();
		vstmfPersonaSeleccionadaVer= new Persona();
		vstmfPeriodoSolicitudesVer= new PeriodoAcademico();
		vstmfCarreraVer=new Carrera();

		try {
			vstmfPeriodoSolicitudesVer=servVstmfPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			vstmfPersonaSeleccionadaVer=servVstmfPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			vstmfCarreraVer=servVstmfCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			
			vstmfListaMateriasSolicitadasDto=servVstmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(solicitudesEstudiante.getPracId(), solicitudesEstudiante.getCrrId(), solicitudesEstudiante.getFcesId(), SolicitudTerceraMatriculaConstantes.ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE);
			
			retorno="irVerMateriasSolicitadas";
		} catch (SolicitudTerceraMatriculaDtoException e) {
			vstmfPeriodoSolicitudesVer=null;
			vstmfPersonaSeleccionadaVer=null;
			vstmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			
		} catch (PeriodoAcademicoNoEncontradoException e) {
			vstmfPeriodoSolicitudesVer=null;
			vstmfPersonaSeleccionadaVer=null;
			vstmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			vstmfPeriodoSolicitudesVer=null;
			vstmfPersonaSeleccionadaVer=null;
			vstmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			vstmfPeriodoSolicitudesVer=null;
			vstmfPersonaSeleccionadaVer=null;
			vstmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			vstmfPeriodoSolicitudesVer=null;
			vstmfPersonaSeleccionadaVer=null;
			vstmfListaMateriasSolicitadasDto=null;
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
	 * Método que guarda la verificación de solicitud de tercera matrícula
	 * @return retorna - la navegación de la página listar estudiantes
	 */
	public String guardarVerificacion(){
	String retorno = null;
	
		//REALIZA LA ACTUALIZACIÓN DE LOS ESTADOS DE  RECORD ACADEMICO Y SOLICITUD TERCERA MATRICULA
			if(vstmfListaMateriasSolicitadasDto!=null && vstmfListaMateriasSolicitadasDto.size()>0 ){ //verifica que la lisa no este nula
				//proceso de transacción
				try{
					if(servVstmfSolicitudTerceraMatriculaServicioServicioJdbc.generarVerificacionSolicitudTerceraMatricula(vstmfListaMateriasSolicitadasDto, SolicitudTerceraMatriculaConstantes.ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE, vstmfUsuario.getUsrNick())){
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.validacion.con.exito")));
					  
					    
					    //******************************************************************************
						//************************* ACA INICIA EL ENVIO DE MAIL SIN ADJUNTO************************
						//******************************************************************************
						//defino los datos para la plantilla
						Map<String, Object> parametros = new HashMap<String, Object>();
						
						parametros.put("secreCarrera",vstmfUsuario.getUsrPersona().getPrsNombres()+" "+vstmfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
								+(vstmfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":vstmfUsuario.getUsrPersona().getPrsSegundoApellido()));
						
						
						parametros.put("estudiante", vstmfPersonaSeleccionadaVer.getPrsNombres()+" "+vstmfPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
								+(vstmfPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":vstmfPersonaSeleccionadaVer.getPrsSegundoApellido()));
						
							
						
						SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
						//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
						parametros.put("fechaHora", sdf.format(new Date()));
						parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
						parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(vstmfCarreraVer.getCrrDependencia().getDpnDescripcion()));
						parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(vstmfCarreraVer.getCrrDetalle())); 
							
						//lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(vstmfPersonaSeleccionadaVer.getPrsMailInstitucional());
						
						//path de la plantilla del mail
						String pathTemplate = "/ec/edu/uce/academico/jsf/velocity/plantillas/template-solicitud-tercera.vm";
						
						//llamo al generador de mails
						GeneradorMails genMail = new GeneradorMails();
						String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_VERIFICACION_SOLICITUD_TERCERA_MATRICULA, 
								GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
						//****envio el mail a la cola
						//cliente web service
						Client client = ClientBuilder.newClient();
						WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
						MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
						postForm.add("mail", mailjsonSt);
//						String responseData = target.request().post(Entity.form(postForm),String.class);
						target.request().post(Entity.form(postForm),String.class);
						
						//******************************************************************************
						//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
						//******************************************************************************
					    
					    vstmfListaMateriasSolicitadasDto=null;
					}else{
						vstmfClickGuardarVerificacion = 0;
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.validacion.sin.exito")));
					}
					
					
					retorno = "irRegresarListarEstudiantes";
				} catch (SolicitudTerceraMatriculaException e) {
					vstmfClickGuardarVerificacion = 0;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.exception")));
				} catch (Exception e) {
					vstmfClickGuardarVerificacion = 0;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.exception")));
				}
					
			}else{
				vstmfClickGuardarVerificacion = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.validacion.no.existe.materias")));
			}
			//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			vstmfClickGuardarVerificacion = 0;
            //VACIO LA LISTA DE SOLICITUDES   
			
		return retorno;
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
		vstmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		vstmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		vstmfEstudianteBuscar = new EstudianteJdbcDto();
		vstmfPeriodoAcademicoActivo= new PeriodoAcademico() ;
		vstmfListaPeriodoAcademicoBusq= null;
		vstmfListCarreraDtoBusq= null;
		vstmfListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vstmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vstmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		vstmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		vstmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		vstmfValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		vstmfClickGuardarVerificacion=0;
	
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		vstmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		vstmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vstmfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vstmfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		vstmfListaSolicitudesDto = null;
				
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}
	
	//SELECCIONA TODOS LOS ITEMS DE LA LISTA
	/**
	 * Método que realiza la selección del check box de todos 
	 * o de ninguna materia para el retiro de matrícula 
	 */
	public void seleccionarTodosVerificar(){
		if(vstmfListaMateriasSolicitadasDto!= null && vstmfListaMateriasSolicitadasDto.size()>0){
			for (SolicitudTerceraMatriculaDto item : vstmfListaMateriasSolicitadasDto) {
				item.setIsSeleccionado(vstmfValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
			}
		}
	}

	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton guardar y se cumpla con las reglas para activar el modal guardar.
	 * @return retora null para para cualquier cosa 
	 */
	public String controlarClickGuardarVerificacion(){
		
		Boolean selecionadosTodos=false;
//		Boolean dentroCronograma= false;
		//VERICO QUE EXISTAN MATERIAS
		if(vstmfListaMateriasSolicitadasDto.size() >= 1){
			//VERIFICO QUE SE SELECCIONE TODAS
			for (SolicitudTerceraMatriculaDto materia : vstmfListaMateriasSolicitadasDto) {
				if(materia.getIsSeleccionado()==true){
					selecionadosTodos=true;
				}else{
					selecionadosTodos=false;
					break;					
				}
			}	
			//VERIFICO QUE ESTE DENTRO DEL CRONOGRAMA DE SOLICITUD DE TERCERA MATRIUCULA PARA PODER VERIFICAR
								
//			if(vstmfCarreraVer.getCrrId()==82 ||vstmfCarreraVer.getCrrId()==157){ //cronograma abierto para Medicina y medicna rediseño
//				dentroCronograma = true;
//				
//			}else{
//				
//				dentroCronograma=verificarCronogramaSolicitarTerceraMatricula();//otras carreras se rigen al cronograma
//				
//			}
			
			
			
		if(verificarCronogramaSolicitarTerceraMatricula())	{
			//CUMPLE CON LAS REGLAS PARA PRESENTAR EL MODAL GUARDAR
		//	if(dentroCronograma==true){
		       if(selecionadosTodos==true){
				vstmfClickGuardarVerificacion = 1;  //activa el modal
			  
		       }else{
				vstmfClickGuardarVerificacion = 0;  //desactiva el modal
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.controlarClickGuardarVerificacion.seleccionar.todos.validacion.exception")));
		
			     }
			}
		}else{
			vstmfClickGuardarVerificacion = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.controlarClickGuardarVerificacion.no.existe.materias.validacion.exception")));

		}
		return null;
	}

	
	public void desactivaModalVerificar(){
		vstmfClickGuardarVerificacion = 0;
		
	}
	
	/**
	 * Verifica que el proceso de solicitar tercera matricula exista y se encuentre activo en la fecha actual
	 * @return retora True, si esta el proceso dentro de las fechas y false si no esta dentro de las fechas del cronograma
	 */
	public Boolean verificarCronogramaSolicitarTerceraMatricula() {
		Date fechaActual = new Date();
		Boolean retorno = false;
		//BUSCO LA DEPENDENCIA
		try {
			vstmfDependencia = servVstmfDependencialServicio.buscarFacultadXcrrId(vstmfCarreraVer.getCrrId());
		} catch (DependenciaNoEncontradoException e1) {
			retorno = false;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(e1.getMessage());			
		}
		// DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		if (vstmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			vstmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			vstmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			vstmfCronogramaActividad = servVstmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(vstmfCarreraVer.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, vstmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_RECEPCION_SOLICITUDES_TERCERA_MATRICULA_VALUE,
							ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			retorno = false;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.encontrado.exception")));

		}
		
		if (vstmfCronogramaActividad != null) {
			if ((vstmfCronogramaActividad.getPlcrFechaInicio() != null)
					&& (vstmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(vstmfCronogramaActividad.getPlcrFechaInicio(),
						vstmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
					retorno = true;
		
				} else {
					retorno = false;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.habilitado.exception")));
				}
			} else {
				retorno = false;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.sin.fechas.exception")));
			}

		} else {
			retorno = false;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.verificarCronogramaSolicitarTerceraMatricula.cronograma.no.encontrado.exception")));
		}

		return retorno;
	}
	
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(SolicitudTerceraMatriculaDto materiaSeleccionada){
		vstmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(vstmfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getSltrmtDocumentoSolicitud();
			try{
//				FileInputStream  fis = new FileInputStream(vstmfArchivoSelSt);
				URL oracle = new URL("file:"+vstmfArchivoSelSt);
//				 URL urlObject = new URL("/");
				    URLConnection urlConnection = oracle.openConnection();
				    InputStream inputStream = urlConnection.getInputStream();
				return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(vstmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				vstmfArchivoSelSt = null;
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
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public Usuario getVstmfUsuario() {
		return vstmfUsuario;
	}

	public void setVstmfUsuario(Usuario vstmfUsuario) {
		this.vstmfUsuario = vstmfUsuario;
	}
	
	public PeriodoAcademico getVstmfPeriodoAcademicoBuscar() {
		return vstmfPeriodoAcademicoBuscar;
	}

	public void setVstmfPeriodoAcademicoBuscar(PeriodoAcademico vstmfPeriodoAcademicoBuscar) {
		this.vstmfPeriodoAcademicoBuscar = vstmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getVstmfCarreraDtoBuscar() {
		return vstmfCarreraDtoBuscar;
	}

	public void setVstmfCarreraDtoBuscar(CarreraDto vstmfCarreraDtoBuscar) {
		this.vstmfCarreraDtoBuscar = vstmfCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getVstmfListCarreraDtoBusq() {
		return vstmfListCarreraDtoBusq;
	}

	public void setVstmfListCarreraDtoBusq(List<CarreraDto> vstmfListCarreraDtoBusq) {
		this.vstmfListCarreraDtoBusq = vstmfListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getVstmfEstudianteBuscar() {
		return vstmfEstudianteBuscar;
	}

	public void setVstmfEstudianteBuscar(EstudianteJdbcDto vstmfEstudianteBuscar) {
		this.vstmfEstudianteBuscar = vstmfEstudianteBuscar;
	}

	public List<SolicitudTerceraMatriculaDto> getVstmfListaSolicitudesDto() {
		vstmfListaSolicitudesDto = vstmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):vstmfListaSolicitudesDto;
		return vstmfListaSolicitudesDto;
	}

	public void setVstmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> vstmfListaSolicitudesDto) {
		this.vstmfListaSolicitudesDto = vstmfListaSolicitudesDto;
	}

	public Persona getVstmfPersonaSeleccionadaVer() {
		return vstmfPersonaSeleccionadaVer;
	}

	public void setVstmfPersonaSeleccionadaVer(Persona vstmfPersonaSeleccionadaVer) {
		this.vstmfPersonaSeleccionadaVer = vstmfPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getVstmfPeriodoSolicitudesVer() {
		return vstmfPeriodoSolicitudesVer;
	}

	public void setVstmfPeriodoSolicitudesVer(PeriodoAcademico vstmfPeriodoSolicitudesVer) {
		this.vstmfPeriodoSolicitudesVer = vstmfPeriodoSolicitudesVer;
	}

	public Carrera getVstmfCarreraVer() {
		return vstmfCarreraVer;
	}

	public void setVstmfCarreraVer(Carrera vstmfCarreraVer) {
		this.vstmfCarreraVer = vstmfCarreraVer;
	}

	public List<SolicitudTerceraMatriculaDto> getVstmfListaMateriasSolicitadasDto() {
		vstmfListaMateriasSolicitadasDto = vstmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):vstmfListaMateriasSolicitadasDto;
		return vstmfListaMateriasSolicitadasDto;
	}

	public void setVstmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> vstmfListaMateriasSolicitadasDto) {
		this.vstmfListaMateriasSolicitadasDto = vstmfListaMateriasSolicitadasDto;
	}

	public Integer getVstmfValidadorSeleccion() {
		return vstmfValidadorSeleccion;
	}

	public void setVstmfValidadorSeleccion(Integer vstmfValidadorSeleccion) {
		this.vstmfValidadorSeleccion = vstmfValidadorSeleccion;
	}

	public Integer getVstmfClickGuardarVerificacion() {
		return vstmfClickGuardarVerificacion;
	}

	public void setVstmfClickGuardarVerificacion(Integer vstmfClickGuardarVerificacion) {
		this.vstmfClickGuardarVerificacion = vstmfClickGuardarVerificacion;
	}

	public List<PeriodoAcademico> getVstmfListaPeriodoAcademicoBusq() {
		vstmfListaPeriodoAcademicoBusq = vstmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):vstmfListaPeriodoAcademicoBusq;
		return vstmfListaPeriodoAcademicoBusq;
	}

	public void setVstmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> vstmfListaPeriodoAcademicoBusq) {
		this.vstmfListaPeriodoAcademicoBusq = vstmfListaPeriodoAcademicoBusq;
	}

	public Integer getVstmfTipoCronograma() {
		return vstmfTipoCronograma;
	}

	public void setVstmfTipoCronograma(Integer vstmfTipoCronograma) {
		this.vstmfTipoCronograma = vstmfTipoCronograma;
	}

	public Dependencia getVstmfDependencia() {
		return vstmfDependencia;
	}

	public void setVstmfDependencia(Dependencia vstmfDependencia) {
		this.vstmfDependencia = vstmfDependencia;
	}

	public CronogramaActividadJdbcDto getVstmfCronogramaActividad() {
		return vstmfCronogramaActividad;
	}

	public void setVstmfCronogramaActividad(CronogramaActividadJdbcDto vstmfCronogramaActividad) {
		this.vstmfCronogramaActividad = vstmfCronogramaActividad;
	}

	public PeriodoAcademico getVstmfPeriodoAcademicoActivo() {
		return vstmfPeriodoAcademicoActivo;
	}

	public void setVstmfPeriodoAcademicoActivo(PeriodoAcademico vstmfPeriodoAcademicoActivo) {
		this.vstmfPeriodoAcademicoActivo = vstmfPeriodoAcademicoActivo;
	}


	public String getVstmfArchivoSelSt() {
		return vstmfArchivoSelSt;
	}


	public void setVstmfArchivoSelSt(String vstmfArchivoSelSt) {
		this.vstmfArchivoSelSt = vstmfArchivoSelSt;
	}

	
}
