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
   
 ARCHIVO:     verificacionApelacionTerceraMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la verificación hecha por la secretaria de la apelación de tercera matricula realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-02-2018		 Marcelo Quishpe                     Emisión Inicial
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
 * Clase (session bean) verificacionApelacionTerceraMatriculaForm. 
 * Bean de sesion que maneja la verificación hecha por la secretaria de la apelación de tercera matrícula realizada por el estudiante.. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "verificacionApelacionTerceraMatriculaForm")
@SessionScoped
public class VerificacionApelacionTerceraMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario vatmfUsuario;
	
	//PARA BUSQUEDA
	private PeriodoAcademico vatmfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> vatmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico vatmfPeriodoAcademicoActivo;
	private CarreraDto vatmfCarreraDtoBuscar;
	private List<CarreraDto> vatmfListCarreraDtoBusq;
	private EstudianteJdbcDto vatmfEstudianteBuscar;
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD
	private List<SolicitudTerceraMatriculaDto> vatmfListaSolicitudesDto;
	//VER MATERIAS
	private Persona vatmfPersonaSeleccionadaVer;
	private PeriodoAcademico vatmfPeriodoSolicitudesVer;
	private Carrera vatmfCarreraVer;
	private List<SolicitudTerceraMatriculaDto> vatmfListaMateriasSolicitadasDto;
	private Integer vatmfValidadorSeleccion;
	private Integer vatmfClickGuardarVerificacion;
	private Integer vatmfTipoCronograma;
	private Dependencia vatmfDependencia;
	private CronogramaActividadJdbcDto vatmfCronogramaActividad;
	private String astmfArchivoSelSt;

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
	PeriodoAcademicoServicio servVatmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servVatmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servVatmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servVatmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servVatmfPersonaServicio;
	@EJB 
	CarreraServicio servVatmfCarreraServicio;
	@EJB 
	DependenciaServicio servVatmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servVatmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servVatmfSolicitudTerceraMatriculaServicioServicioJdbc;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irVerificarApelacionListarEstudiantes(Usuario usuario) {
		vatmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		   vatmfPeriodoAcademicoActivo = servVatmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		   vatmfListaPeriodoAcademicoBusq=servVatmfPeriodoAcademicoServicio.listarTodos();
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
	    	vatmfListCarreraDtoBusq = servVatmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(vatmfUsuario.getUsrId(), RolConstantes.ROL_SECRESECREABOGADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, vatmfPeriodoAcademicoActivo.getPracId());
			retorno = "irVerificarApelacionListarEstudiantes";
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
		vatmfListaSolicitudesDto=new ArrayList<SolicitudTerceraMatriculaDto>();
			try {
				
				//Verifico que se seleccione una carrera
				if(vatmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					vatmfListaSolicitudesDto=servVatmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitudSIIUSAU(vatmfPeriodoAcademicoBuscar.getPracId(), vatmfCarreraDtoBuscar.getCrrId(), vatmfEstudianteBuscar.getPrsPrimerApellido(), vatmfEstudianteBuscar.getPrsIdentificacion(), SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE);
				}else{
					vatmfListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
				
			} catch (SolicitudTerceraMatriculaDtoException e) {
				   vatmfListaSolicitudesDto=null;
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
		vatmfListaMateriasSolicitadasDto= new ArrayList<>();
		vatmfPersonaSeleccionadaVer= new Persona();
		vatmfPeriodoSolicitudesVer= new PeriodoAcademico();
		vatmfCarreraVer=new Carrera();
	
		try {
			vatmfPeriodoSolicitudesVer=servVatmfPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			vatmfPersonaSeleccionadaVer=servVatmfPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			vatmfCarreraVer=servVatmfCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			
			vatmfListaMateriasSolicitadasDto=servVatmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(solicitudesEstudiante.getPracId(), solicitudesEstudiante.getCrrId(), solicitudesEstudiante.getFcesId(), SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE, SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_VALUE);
	       
			retorno="irVerMateriasApeladas";
			
		} catch (SolicitudTerceraMatriculaDtoException e) {
			vatmfPeriodoSolicitudesVer=null;
			vatmfPersonaSeleccionadaVer=null;
			vatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			
		}  catch (PeriodoAcademicoNoEncontradoException e) {
			vatmfPeriodoSolicitudesVer=null;
			vatmfPersonaSeleccionadaVer=null;
			vatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			vatmfPeriodoSolicitudesVer=null;
			vatmfPersonaSeleccionadaVer=null;
			vatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			vatmfPeriodoSolicitudesVer=null;
			vatmfPersonaSeleccionadaVer=null;
			vatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			vatmfPeriodoSolicitudesVer=null;
			vatmfPersonaSeleccionadaVer=null;
			vatmfListaMateriasSolicitadasDto=null;
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
			if(vatmfListaMateriasSolicitadasDto!=null && vatmfListaMateriasSolicitadasDto.size()>0 ){ //verifica que la lisa no este nula
				//proceso de transacción
				try{
					if(servVatmfSolicitudTerceraMatriculaServicioServicioJdbc.generarVerificacionSolicitudTerceraMatricula(vatmfListaMateriasSolicitadasDto, SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE, vatmfUsuario.getUsrNick())){
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.validacion.con.exito")));
					
					    //******************************************************************************
						//************************* ACA INICIA EL ENVIO DE MAIL SIN ADJUNTO************************
						//******************************************************************************
						//defino los datos para la plantilla
						Map<String, Object> parametros = new HashMap<String, Object>();
						
						parametros.put("secreSecreAbogado",vatmfUsuario.getUsrPersona().getPrsNombres()+" "+vatmfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
								+(vatmfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":vatmfUsuario.getUsrPersona().getPrsSegundoApellido()));
						
						
						parametros.put("estudiante", vatmfPersonaSeleccionadaVer.getPrsNombres()+" "+vatmfPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
								+(vatmfPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":vatmfPersonaSeleccionadaVer.getPrsSegundoApellido()));
						
							
						
						SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
						//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
						parametros.put("fechaHora", sdf.format(new Date()));
						parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
						parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(vatmfCarreraVer.getCrrDependencia().getDpnDescripcion()));
						parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(vatmfCarreraVer.getCrrDetalle())); 
							
						//lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(vatmfPersonaSeleccionadaVer.getPrsMailInstitucional());
						
						//path de la plantilla del mail
						String pathTemplate = "/ec/edu/uce/academico/jsf/velocity/plantillas/template-verifica-apelacion-tercera.vm";
						
						//llamo al generador de mails
						GeneradorMails genMail = new GeneradorMails();
						String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_VERIFICACION_APELACION_TERCERA_MATRICULA, 
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
					
						vatmfListaMateriasSolicitadasDto=null;
					
					}else{
						vatmfClickGuardarVerificacion = 0;
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.validacion.sin.exito")));
					}
					retorno = "irListarEstudiantesVerificaApelacion";
				} catch (SolicitudTerceraMatriculaException e) {
					vatmfClickGuardarVerificacion = 0;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.exception")));
				} catch (Exception e) {
					vatmfClickGuardarVerificacion = 0;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.exception")));
				}
					
			}else{
				vatmfClickGuardarVerificacion = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.guardarVerificacion.validacion.no.existe.materias")));
			}
			//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			vatmfClickGuardarVerificacion = 0;
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
		vatmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		vatmfCarreraDtoBuscar = new CarreraDto();
		vatmfPeriodoAcademicoActivo= new PeriodoAcademico() ;
		vatmfListaPeriodoAcademicoBusq= null;
		vatmfListCarreraDtoBusq= null;
		vatmfListaSolicitudesDto=null;
		
		//INSTANCIO EL ESTUDIANTE
		vatmfEstudianteBuscar = new EstudianteJdbcDto();
		
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vatmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vatmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		vatmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		vatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		vatmfValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		vatmfClickGuardarVerificacion=0;
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		vatmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		vatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vatmfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vatmfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		vatmfListaSolicitudesDto = null;
				
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
		if(vatmfListaMateriasSolicitadasDto!= null && vatmfListaMateriasSolicitadasDto.size()>0){
			for (SolicitudTerceraMatriculaDto item : vatmfListaMateriasSolicitadasDto) {
				item.setIsSeleccionado(vatmfValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
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
	//	Boolean dentroCronograma= false;
		//VERICO QUE EXISTAN MATERIAS
		if(vatmfListaMateriasSolicitadasDto.size() >= 1){
			//VERIFICO QUE SE SELECCIONE TODAS
			for (SolicitudTerceraMatriculaDto materia : vatmfListaMateriasSolicitadasDto) {
				if(materia.getIsSeleccionado()==true){
					selecionadosTodos=true;
				}else{
					selecionadosTodos=false;
					break;					
				}
			}	
			//VERIFICO QUE ESTE DENTRO DEL CRONOGRAMA DE SOLICITUD DE TERCERA MATRIUCULA
			
//			if(vatmfCarreraVer.getCrrId()==82 ||vatmfCarreraVer.getCrrId()==157){ //cronograma abierto para Medicina y medicna rediseño
//				dentroCronograma = true;
//				
//			}else{
//				
//				dentroCronograma=verificarCronogramaSolicitarTerceraMatricula();//otras carreras se rigen al cronograma
//				
//			}
			
		
			
			//CUMPLE CON LAS REGLAS PARA PRESENTAR EL MODAL GUARDAR
			
			if(verificarCronogramaSolicitarTerceraMatricula()){
	//		if(dentroCronograma==true){
		       if(selecionadosTodos==true){
				vatmfClickGuardarVerificacion = 1;  //activa el modal
			  
		       }else{
				vatmfClickGuardarVerificacion = 0;  //desactiva el modal
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.controlarClickGuardarVerificacion.seleccionar.todos.validacion.exception")));
		
			     }
			}
		}else{
			vatmfClickGuardarVerificacion = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudTerceraMatricula.controlarClickGuardarVerificacion.no.existe.materias.validacion.exception")));

		}
		return null;
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
			vatmfDependencia = servVatmfDependencialServicio.buscarFacultadXcrrId(vatmfCarreraVer.getCrrId());
		} catch (DependenciaNoEncontradoException e1) {
			retorno = false;
			FacesUtil.limpiarMensaje();
		    FacesUtil.mensajeError(e1.getMessage());			
		}
		// DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		if (vatmfDependencia.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE) { // si es nivelación
			vatmfTipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE;
		} else { // si es otra, en este caso va ha tener de carrera o academico
			vatmfTipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE;
		}
		// BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			vatmfCronogramaActividad = servVatmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(vatmfCarreraVer.getCrrId(),
							PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, vatmfTipoCronograma,
							ProcesoFlujoConstantes.PROCESO_RECEPCION_APELACIONES_TERCERA_MATRICULA_VALUE,
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
		
		if (vatmfCronogramaActividad != null) {
			if ((vatmfCronogramaActividad.getPlcrFechaInicio() != null)
					&& (vatmfCronogramaActividad.getPlcrFechaFin() != null)) {
				//realizo la diferencia entre las dos fechas
				// VALIDACIÓN DENTRO DE LO ESTABLECIDO
				if (GeneralesUtilidades.verificarEntreFechas(vatmfCronogramaActividad.getPlcrFechaInicio(),
						vatmfCronogramaActividad.getPlcrFechaFin(), fechaActual)) {
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
		astmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_APELACION_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(astmfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getSltrmtDocumentoSolicitud();
			try{
				
				URL oracle = new URL("file:"+astmfArchivoSelSt);
				System.out.println(astmfArchivoSelSt);
//				 URL urlObject = new URL("/");
				    URLConnection urlConnection = oracle.openConnection();
				    InputStream inputStream = urlConnection.getInputStream();
//				FileInputStream  fis = new FileInputStream(astmfArchivoSelSt);
				return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(astmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
				astmfArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	public Usuario getVatmfUsuario() {
		return vatmfUsuario;
	}

	public void setVatmfUsuario(Usuario vatmfUsuario) {
		this.vatmfUsuario = vatmfUsuario;
	}
	
	public PeriodoAcademico getVatmfPeriodoAcademicoBuscar() {
		return vatmfPeriodoAcademicoBuscar;
	}

	public void setVatmfPeriodoAcademicoBuscar(PeriodoAcademico vatmfPeriodoAcademicoBuscar) {
		this.vatmfPeriodoAcademicoBuscar = vatmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getVatmfCarreraDtoBuscar() {
		return vatmfCarreraDtoBuscar;
	}

	public void setVatmfCarreraDtoBuscar(CarreraDto vatmfCarreraDtoBuscar) {
		this.vatmfCarreraDtoBuscar = vatmfCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getVatmfListCarreraDtoBusq() {
		return vatmfListCarreraDtoBusq;
	}

	public void setVatmfListCarreraDtoBusq(List<CarreraDto> vatmfListCarreraDtoBusq) {
		this.vatmfListCarreraDtoBusq = vatmfListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getVatmfEstudianteBuscar() {
		return vatmfEstudianteBuscar;
	}

	public void setVatmfEstudianteBuscar(EstudianteJdbcDto vatmfEstudianteBuscar) {
		this.vatmfEstudianteBuscar = vatmfEstudianteBuscar;
	}

	public List<SolicitudTerceraMatriculaDto> getVatmfListaSolicitudesDto() {
		vatmfListaSolicitudesDto = vatmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):vatmfListaSolicitudesDto;
		return vatmfListaSolicitudesDto;
	}

	public void setVatmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> vatmfListaSolicitudesDto) {
		this.vatmfListaSolicitudesDto = vatmfListaSolicitudesDto;
	}

	public Persona getVatmfPersonaSeleccionadaVer() {
		return vatmfPersonaSeleccionadaVer;
	}

	public void setVatmfPersonaSeleccionadaVer(Persona vatmfPersonaSeleccionadaVer) {
		this.vatmfPersonaSeleccionadaVer = vatmfPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getVatmfPeriodoSolicitudesVer() {
		return vatmfPeriodoSolicitudesVer;
	}

	public void setVatmfPeriodoSolicitudesVer(PeriodoAcademico vatmfPeriodoSolicitudesVer) {
		this.vatmfPeriodoSolicitudesVer = vatmfPeriodoSolicitudesVer;
	}

	public Carrera getVatmfCarreraVer() {
		return vatmfCarreraVer;
	}

	public void setVatmfCarreraVer(Carrera vatmfCarreraVer) {
		this.vatmfCarreraVer = vatmfCarreraVer;
	}

	public List<SolicitudTerceraMatriculaDto> getVatmfListaMateriasSolicitadasDto() {
		vatmfListaMateriasSolicitadasDto = vatmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):vatmfListaMateriasSolicitadasDto;
		return vatmfListaMateriasSolicitadasDto;
	}

	public void setVatmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> vatmfListaMateriasSolicitadasDto) {
		this.vatmfListaMateriasSolicitadasDto = vatmfListaMateriasSolicitadasDto;
	}

	public Integer getVatmfValidadorSeleccion() {
		return vatmfValidadorSeleccion;
	}

	public void setVatmfValidadorSeleccion(Integer vatmfValidadorSeleccion) {
		this.vatmfValidadorSeleccion = vatmfValidadorSeleccion;
	}

	public Integer getVatmfClickGuardarVerificacion() {
		return vatmfClickGuardarVerificacion;
	}

	public void setVatmfClickGuardarVerificacion(Integer vatmfClickGuardarVerificacion) {
		this.vatmfClickGuardarVerificacion = vatmfClickGuardarVerificacion;
	}

	public List<PeriodoAcademico> getVatmfListaPeriodoAcademicoBusq() {
		vatmfListaPeriodoAcademicoBusq = vatmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):vatmfListaPeriodoAcademicoBusq;
		return vatmfListaPeriodoAcademicoBusq;
	}

	public void setVatmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> vatmfListaPeriodoAcademicoBusq) {
		this.vatmfListaPeriodoAcademicoBusq = vatmfListaPeriodoAcademicoBusq;
	}

	public Integer getVatmfTipoCronograma() {
		return vatmfTipoCronograma;
	}

	public void setVatmfTipoCronograma(Integer vatmfTipoCronograma) {
		this.vatmfTipoCronograma = vatmfTipoCronograma;
	}

	public Dependencia getVatmfDependencia() {
		return vatmfDependencia;
	}

	public void setVatmfDependencia(Dependencia vatmfDependencia) {
		this.vatmfDependencia = vatmfDependencia;
	}

	public CronogramaActividadJdbcDto getVatmfCronogramaActividad() {
		return vatmfCronogramaActividad;
	}

	public void setVatmfCronogramaActividad(CronogramaActividadJdbcDto vatmfCronogramaActividad) {
		this.vatmfCronogramaActividad = vatmfCronogramaActividad;
	}

	public PeriodoAcademico getVatmfPeriodoAcademicoActivo() {
		return vatmfPeriodoAcademicoActivo;
	}

	public void setVatmfPeriodoAcademicoActivo(PeriodoAcademico vatmfPeriodoAcademicoActivo) {
		this.vatmfPeriodoAcademicoActivo = vatmfPeriodoAcademicoActivo;
	}


	public String getAstmfArchivoSelSt() {
		return astmfArchivoSelSt;
	}


	public void setAstmfArchivoSelSt(String astmfArchivoSelSt) {
		this.astmfArchivoSelSt = astmfArchivoSelSt;
	}
	

}
