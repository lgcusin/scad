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
   
 ARCHIVO:     VerificacionSolicitudRetiroFortuitoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la verificación de la secretaria de la solicitud de retiro fotuito realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-Dic-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reajusteMatricula;

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
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
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
 * Clase (session bean) VerificacionSolicitudRetiroFortuitoForm. 
 * Bean de sesion que maneja la verificación de la secretaria de la solicitud de retiro fortuito realizada por el estudiante. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "verificacionSolicitudRetiroFortuitoForm")
@SessionScoped
public class VerificacionSolicitudRetiroFortuitoForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario vsrffUsuario;
	
	
	//PARA BUSQUEDA
	private PeriodoAcademico vsrffPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> vsrffListaPeriodoAcademicoBusq;
	private PeriodoAcademico vsrffPeriodoAcademicoActivo;
	private CarreraDto vsrffCarreraDtoBuscar;
	private List<CarreraDto> vsrffListCarreraDtoBusq;
	private EstudianteJdbcDto vsrffEstudianteBuscar;
	
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD
	private List<FichaMatriculaDto> vsrffListaSolicitudesDto;
	//VER MATERIAS
	private Persona vsrffPersonaSeleccionadaVer;
	private PeriodoAcademico vsrffPeriodoSolicitudesVer;
	private Carrera vsrffCarreraVer;
	private List<EstudianteJdbcDto> vsrffListaMateriasSolicitadasDto; //lista de materias
	private Integer vsrffValidadorSeleccion;
	private Integer vsrffClickGuardarVerificacion;
	private Integer vsrffTipoCronograma;
	private Dependencia vsrffDependencia;
	private CronogramaActividadJdbcDto vsrffCronogramaActividad;
	
	
	private String vsrffArchivoSelSt;

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
	PeriodoAcademicoServicio servVsrffPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servVsrffCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servVsrffEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servVsrffSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servVsrffPersonaServicio;
	@EJB 
	CarreraServicio servVsrffCarreraServicio;
	@EJB 
	DependenciaServicio servVsrffDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servVsrffCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servVsrffSolicitudTerceraMatriculaServicioServicioJdbc;
	@EJB
	FichaMatriculaDtoServicioJdbc servVsrffFichaMatriculaDtoServicioJdbc;
	@EJB
	MatriculaServicio  servVsrffMatricula;
	
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irAListarEstudiantes(Usuario usuario) {
		vsrffUsuario = usuario;
		String retorno = null;
	
			//INICIO PARAMETROS
		iniciarParametros();
		
		   

			retorno = "irAListarEstudiantesRetiroFortuito";
		
		return retorno;
	}
	
	
	/**
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron la tercera matrícula con los parámetros ingresados
	 */
	public void buscarEstudiantes(){
		vsrffListaSolicitudesDto=new ArrayList<>();
	
		try {
				//Verifico que se seleecione una carrera
				if(vsrffCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
						vsrffListaSolicitudesDto=servVsrffFichaMatriculaDtoServicioJdbc.buscarSolicitudRetiroFortuitoXPeriodoXapellidoXidentificacionXcarrera(GeneralesConstantes.APP_ID_BASE, vsrffListCarreraDtoBusq, vsrffCarreraDtoBuscar.getCrrId(),  vsrffEstudianteBuscar.getPrsIdentificacion(), vsrffEstudianteBuscar.getPrsPrimerApellido(),DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE);
					
				}else{
					vsrffListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
				
		} catch (FichaMatriculaException e) {
			//	e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}	
		
	}
	
	
	
	/**
	 * Método que permite ir a la visualización de las materias que se solicito tercera matrícula.
	 * @param solicitudesEstudiante - solicitudesEstudiante objeto  estudiante que se ha seleccionado
	 * @return La navegación hacia la página xhtml de ver materias solcitadas tercera matrícula.
	 */
	public String irVerMateriasSolicitadas(FichaMatriculaDto solicitudesEstudiante){
	  String retorno=null;
		vsrffListaMateriasSolicitadasDto= new ArrayList<>();
		vsrffPersonaSeleccionadaVer= new Persona();
		vsrffPeriodoSolicitudesVer= new PeriodoAcademico();
		vsrffCarreraVer=new Carrera();

		try {
			vsrffPeriodoSolicitudesVer=servVsrffPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			vsrffPersonaSeleccionadaVer=servVsrffPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			vsrffCarreraVer=servVsrffCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			
			vsrffListaMateriasSolicitadasDto=servVsrffEstudianteDtoServicioJdbc.buscarAsignaturasValidarRetiroFortuitoXprsIdXmtrIdXpracIdXdtmtEstado(solicitudesEstudiante.getPrsId(), solicitudesEstudiante.getFcmtId(), solicitudesEstudiante.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			
			retorno="irVerMateriasSolicitadas";
		}  catch (PeriodoAcademicoNoEncontradoException e) {
			vsrffPeriodoSolicitudesVer=null;
			vsrffPersonaSeleccionadaVer=null;
			vsrffListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			vsrffPeriodoSolicitudesVer=null;
			vsrffPersonaSeleccionadaVer=null;
			vsrffListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			vsrffPeriodoSolicitudesVer=null;
			vsrffPersonaSeleccionadaVer=null;
			vsrffListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			vsrffPeriodoSolicitudesVer=null;
			vsrffPersonaSeleccionadaVer=null;
			vsrffListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcException e) {
			vsrffPeriodoSolicitudesVer=null;
			vsrffPersonaSeleccionadaVer=null;
			vsrffListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			vsrffPeriodoSolicitudesVer=null;
			vsrffPersonaSeleccionadaVer=null;
			vsrffListaMateriasSolicitadasDto=null;
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
			if(vsrffListaMateriasSolicitadasDto!=null && vsrffListaMateriasSolicitadasDto.size()>0 ){ //verifica que la lisa no este nula
				//proceso de transacción
				try{
					if(servVsrffMatricula.verificarSolicitudRetiroFortuito(vsrffListaMateriasSolicitadasDto, DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE, vsrffUsuario)){
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.guardarVerificacion.validacion.con.exito")));
					  
					    
					    //******************************************************************************
						//************************* ACA INICIA EL ENVIO DE MAIL SIN ADJUNTO************************
						//******************************************************************************
						//defino los datos para la plantilla
						Map<String, Object> parametros = new HashMap<String, Object>();
						
						parametros.put("secreCarrera",vsrffUsuario.getUsrPersona().getPrsNombres()+" "+vsrffUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
								+(vsrffUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":vsrffUsuario.getUsrPersona().getPrsSegundoApellido()));
						
						
						parametros.put("estudiante", vsrffPersonaSeleccionadaVer.getPrsNombres()+" "+vsrffPersonaSeleccionadaVer.getPrsPrimerApellido().toUpperCase()+" "
								+(vsrffPersonaSeleccionadaVer.getPrsSegundoApellido() == null?" ":vsrffPersonaSeleccionadaVer.getPrsSegundoApellido()));
						
							
						
						SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
						//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
						parametros.put("fechaHora", sdf.format(new Date()));
						parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
						parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(vsrffCarreraVer.getCrrDependencia().getDpnDescripcion()));
						parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(vsrffCarreraVer.getCrrDetalle())); 
							
						//lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(vsrffPersonaSeleccionadaVer.getPrsMailInstitucional());
						
						//path de la plantilla del mail
						String pathTemplate = "/ec/edu/uce/academico/jsf/velocity/plantillas/template-verificacion-retiro-fortuito.vm";
						
						//llamo al generador de mails
						GeneradorMails genMail = new GeneradorMails();
						String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_VERIFICAR_RETIRO_POR_SITUACION_FORTUITA, 
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
					    
					    vsrffListaMateriasSolicitadasDto=null;
					}else{
						vsrffClickGuardarVerificacion = 0;
						FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.guardarVerificacion.validacion.sin.exito")));
					}
					
					iniciarParametros();
					retorno = "irAListarEstudiantesRetiroFortuito";
					
				} catch (MatriculaException e) {
					vsrffClickGuardarVerificacion = 0;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.guardarVerificacion.matricula.exception")));
				} catch (Exception e) {
					vsrffClickGuardarVerificacion = 0;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.guardarVerificacion.exception")));
				}
					
			}else{
				vsrffClickGuardarVerificacion = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.guardarVerificacion.validacion.no.existe.materias")));
			}
			//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			vsrffClickGuardarVerificacion = 0;
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
		vsrffPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		vsrffCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		vsrffEstudianteBuscar = new EstudianteJdbcDto();
		vsrffPeriodoAcademicoActivo= new PeriodoAcademico() ;
		vsrffListaPeriodoAcademicoBusq= null;
		//vsrffListCarreraDtoBusq= null;
		vsrffListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vsrffEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vsrffEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		vsrffCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		vsrffPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		vsrffValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		vsrffClickGuardarVerificacion=0;
		try {
		//BUSCA EL PERIODO ACADEMICO ACTIVO
		   vsrffPeriodoAcademicoActivo = servVsrffPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		  
		   vsrffListaPeriodoAcademicoBusq=servVsrffPeriodoAcademicoServicio.listarTodos();
		
		//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
		
			vsrffListCarreraDtoBusq = servVsrffCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(vsrffUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, vsrffPeriodoAcademicoActivo.getPracId());
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
	
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		vsrffCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		vsrffPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vsrffEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		vsrffEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		vsrffListaSolicitudesDto = null;
				
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
		if(vsrffListaMateriasSolicitadasDto!= null && vsrffListaMateriasSolicitadasDto.size()>0){
			for (EstudianteJdbcDto item : vsrffListaMateriasSolicitadasDto) {
				item.setSeleccionado(vsrffValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
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
		//Boolean dentroCronograma= false;
		//VERICO QUE EXISTAN MATERIAS
		if(vsrffListaMateriasSolicitadasDto.size() >= 1){
			//VERIFICO QUE SE SELECCIONE TODAS
			for (EstudianteJdbcDto materia : vsrffListaMateriasSolicitadasDto) {
				if(materia.isSeleccionado()==true){
					selecionadosTodos=true;
				}else{
					selecionadosTodos=false;
					break;					
				}
			}	
			//NO TIENE CRONOGRAMA ESTABLECIDO, verificar funcionamiento del metodo cronograma
		
			
			//CUMPLE CON LAS REGLAS PARA PRESENTAR EL MODAL GUARDAR
			
		       if(selecionadosTodos==true){
				vsrffClickGuardarVerificacion = 1;  //activa el modal
			  
		       }else{
				vsrffClickGuardarVerificacion = 0;  //desactiva el modal
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.controlarClickGuardarVerificacion.seleccionar.todos.validacion.exception")));
		
			     }
			}

		return null;
	}

	
	public void desactivaModalVerificar(){
		vsrffClickGuardarVerificacion = 0;
		
	}
	
	
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(EstudianteJdbcDto materiaSeleccionada){
		vsrffArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+materiaSeleccionada.getDtmtArchivoEstudiantes();
		if(vsrffArchivoSelSt != null){
			String nombre = materiaSeleccionada.getDtmtArchivoEstudiantes();
			try{
//				FileInputStream  fis = new FileInputStream(vsrffArchivoSelSt);
				URL oracle = new URL("file:"+vsrffArchivoSelSt);
//				 URL urlObject = new URL("/");
				    URLConnection urlConnection = oracle.openConnection();
				    InputStream inputStream = urlConnection.getInputStream();
				return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(vsrffArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				vsrffArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (Exception e) {
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerificacionSolicitudRetiroFortuito.StreamedContent.descargar.archivo.no.encontrado.exception")));
			return null;
		}
		return null;
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public Usuario getVsrffUsuario() {
		return vsrffUsuario;
	}

	public void setVsrffUsuario(Usuario vsrffUsuario) {
		this.vsrffUsuario = vsrffUsuario;
	}
	
	public PeriodoAcademico getVsrffPeriodoAcademicoBuscar() {
		return vsrffPeriodoAcademicoBuscar;
	}

	public void setVsrffPeriodoAcademicoBuscar(PeriodoAcademico vsrffPeriodoAcademicoBuscar) {
		this.vsrffPeriodoAcademicoBuscar = vsrffPeriodoAcademicoBuscar;
	}

	public CarreraDto getVsrffCarreraDtoBuscar() {
		return vsrffCarreraDtoBuscar;
	}

	public void setVsrffCarreraDtoBuscar(CarreraDto vsrffCarreraDtoBuscar) {
		this.vsrffCarreraDtoBuscar = vsrffCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getVsrffListCarreraDtoBusq() {
		vsrffListCarreraDtoBusq = vsrffListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):vsrffListCarreraDtoBusq;
		return vsrffListCarreraDtoBusq;
	}

	public void setVsrffListCarreraDtoBusq(List<CarreraDto> vsrffListCarreraDtoBusq) {
		this.vsrffListCarreraDtoBusq = vsrffListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getVsrffEstudianteBuscar() {
		return vsrffEstudianteBuscar;
	}

	public void setVsrffEstudianteBuscar(EstudianteJdbcDto vsrffEstudianteBuscar) {
		this.vsrffEstudianteBuscar = vsrffEstudianteBuscar;
	}

	


	public List<FichaMatriculaDto> getVsrffListaSolicitudesDto() {
		vsrffListaSolicitudesDto = vsrffListaSolicitudesDto==null?(new ArrayList<FichaMatriculaDto>()):vsrffListaSolicitudesDto;

		return vsrffListaSolicitudesDto;
	}


	public void setVsrffListaSolicitudesDto(List<FichaMatriculaDto> vsrffListaSolicitudesDto) {
		this.vsrffListaSolicitudesDto = vsrffListaSolicitudesDto;
	}


	public Persona getVsrffPersonaSeleccionadaVer() {
		return vsrffPersonaSeleccionadaVer;
	}

	public void setVsrffPersonaSeleccionadaVer(Persona vsrffPersonaSeleccionadaVer) {
		this.vsrffPersonaSeleccionadaVer = vsrffPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getVsrffPeriodoSolicitudesVer() {
		return vsrffPeriodoSolicitudesVer;
	}

	public void setVsrffPeriodoSolicitudesVer(PeriodoAcademico vsrffPeriodoSolicitudesVer) {
		this.vsrffPeriodoSolicitudesVer = vsrffPeriodoSolicitudesVer;
	}

	public Carrera getVsrffCarreraVer() {
		return vsrffCarreraVer;
	}

	public void setVsrffCarreraVer(Carrera vsrffCarreraVer) {
		this.vsrffCarreraVer = vsrffCarreraVer;
	}

	public List<EstudianteJdbcDto> getVsrffListaMateriasSolicitadasDto() {
		vsrffListaMateriasSolicitadasDto = vsrffListaMateriasSolicitadasDto==null?(new ArrayList<EstudianteJdbcDto>()):vsrffListaMateriasSolicitadasDto;
		return vsrffListaMateriasSolicitadasDto;
	}

	public void setVsrffListaMateriasSolicitadasDto(List<EstudianteJdbcDto> vsrffListaMateriasSolicitadasDto) {
		this.vsrffListaMateriasSolicitadasDto = vsrffListaMateriasSolicitadasDto;
	}

	public Integer getVsrffValidadorSeleccion() {
		return vsrffValidadorSeleccion;
	}

	public void setVsrffValidadorSeleccion(Integer vsrffValidadorSeleccion) {
		this.vsrffValidadorSeleccion = vsrffValidadorSeleccion;
	}

	public Integer getVsrffClickGuardarVerificacion() {
		return vsrffClickGuardarVerificacion;
	}

	public void setVsrffClickGuardarVerificacion(Integer vsrffClickGuardarVerificacion) {
		this.vsrffClickGuardarVerificacion = vsrffClickGuardarVerificacion;
	}

	public List<PeriodoAcademico> getVsrffListaPeriodoAcademicoBusq() {
		vsrffListaPeriodoAcademicoBusq = vsrffListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):vsrffListaPeriodoAcademicoBusq;
		return vsrffListaPeriodoAcademicoBusq;
	}

	public void setVsrffListaPeriodoAcademicoBusq(List<PeriodoAcademico> vsrffListaPeriodoAcademicoBusq) {
		this.vsrffListaPeriodoAcademicoBusq = vsrffListaPeriodoAcademicoBusq;
	}

	public Integer getVsrffTipoCronograma() {
		return vsrffTipoCronograma;
	}

	public void setVsrffTipoCronograma(Integer vsrffTipoCronograma) {
		this.vsrffTipoCronograma = vsrffTipoCronograma;
	}

	public Dependencia getVsrffDependencia() {
		return vsrffDependencia;
	}

	public void setVsrffDependencia(Dependencia vsrffDependencia) {
		this.vsrffDependencia = vsrffDependencia;
	}

	public CronogramaActividadJdbcDto getVsrffCronogramaActividad() {
		return vsrffCronogramaActividad;
	}

	public void setVsrffCronogramaActividad(CronogramaActividadJdbcDto vsrffCronogramaActividad) {
		this.vsrffCronogramaActividad = vsrffCronogramaActividad;
	}

	public PeriodoAcademico getVsrffPeriodoAcademicoActivo() {
		return vsrffPeriodoAcademicoActivo;
	}

	public void setVsrffPeriodoAcademicoActivo(PeriodoAcademico vsrffPeriodoAcademicoActivo) {
		this.vsrffPeriodoAcademicoActivo = vsrffPeriodoAcademicoActivo;
	}


	public String getVsrffArchivoSelSt() {
		return vsrffArchivoSelSt;
	}


	public void setVsrffArchivoSelSt(String vsrffArchivoSelSt) {
		this.vsrffArchivoSelSt = vsrffArchivoSelSt;
	}

	
}
