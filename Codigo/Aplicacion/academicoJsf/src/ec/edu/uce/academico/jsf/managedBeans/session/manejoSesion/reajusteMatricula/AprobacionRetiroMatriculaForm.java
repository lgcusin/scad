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
   
 ARCHIVO:     AprobacionRetiroMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la aprobación del retiro de la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-06-2017			 Dennis Collaguazo                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reajusteMatricula;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;

/**
 * Clase (session bean) RetiroMatriculaForm. 
 * Bean de sesion que maneja la aprobación del retiro de la matricula del estudiante. 
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name = "aprobacionRetiroMatriculaForm")
@SessionScoped
public class AprobacionRetiroMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario armfUsuario;
	private Integer armfTipoUsuario;
	private String armfTipoCarrera;
	
	//PARA BUSQUEDA
	private PeriodoAcademico armfPeriodoAcademicoBuscar;
	private CarreraDto armfCarreraDtoBuscar;
	private List<FichaMatriculaDto> armfListFichaMatriculaDtoBusq;
	private List<CarreraDto> armfListCarreraDtoBusq;
	private EstudianteJdbcDto armfEstudianteBuscar;
	private List<PeriodoAcademico> armfListPeriodoAcademicoBuscar;
	
	//PARA VISUALIZACIÓN
	private List<EstudianteJdbcDto> armfListEstudianteDto;
	private List<EstudianteJdbcDto> armfListMateriasTodas;
	private List<EstudianteJdbcDto> armfListMateriasInactivas;
	private FichaMatriculaDto armfFichaMatriculaDtoSeleccionado;
	private Integer armfVisualizadorBotones;
	
	//PARA DESCARGAR ARCHIVO
	private String armfArchivoSelSt;
	private Integer armfVisualizadorLink;
	
	//PARA APROBACIÓN RETIRO
	private Integer armfValidadorClic;
	private Integer armftipoCronograma;
	private boolean armfRetiroMatriculaTotal;
	private Integer armfTipoSolicitud;
	
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
	PeriodoAcademicoServicio servArmfPeriodoAcademicoServicio;
	@EJB 
	FichaMatriculaDtoServicioJdbc servArmfFichaMatriculaDtoServicioJdbc;
	@EJB
	CarreraDtoServicioJdbc servArmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servArmfEstudianteDtoServicioJdbc;
	@EJB 
	MatriculaServicio servArmfMatriculaServicio;
	@EJB 
	RolFlujoCarreraServicio servArmfRolFlujoCarreraServicio;
	@EJB
	DependenciaServicio servArmfDependenciaServicio;
	@EJB
	PersonaServicio servArmfPersonaServicio;
	@EJB
	DetalleMatriculaServicio servArmfDetalleMatriculaServicio;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarMatriculas
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar matricula
	 */
	public String irAListarMatricula(Usuario usuario) {
		armfUsuario = usuario;
		String retorno = null;
		armfTipoSolicitud=0;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			List<RolFlujoCarrera> roflcrLista = servArmfRolFlujoCarreraServicio.buscarXRolXUsuarioId(armfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE);
			for (RolFlujoCarrera item : roflcrLista) {
				//buscar dependencia
				Dependencia depen = new Dependencia();
				depen = servArmfDependenciaServicio.buscarDependenciaXcrrId(item.getRoflcrCarrera().getCrrId());
				if(depen.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE){
					armfTipoCarrera="suficiencias";
				}else{
					armfTipoCarrera="carreras";
				}
			}
			//BUSCA EL PERIODO ACADEMICO ACTIVO
			if(armfTipoCarrera == "suficiencias"){
				armfListPeriodoAcademicoBuscar = servArmfPeriodoAcademicoServicio.listarXestadoXtipoPeriodoActivoEnCierre(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
			}else if(armfTipoCarrera == "carreras"){
				armfListPeriodoAcademicoBuscar = servArmfPeriodoAcademicoServicio.listarXestadoXtipoPeriodoActivoEnCierre(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			}
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			armfListCarreraDtoBusq = servArmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoEnCierre(armfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, armfPeriodoAcademicoBuscar.getPracId());
			//BUSCA LA LISTA DE MATRÍCULAS DEL ESTUDIANTE
			armfListFichaMatriculaDtoBusq = servArmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoActivoEnCierreXapellidoXidentificacionXcarrera(armfPeriodoAcademicoBuscar.getPracId(), armfListCarreraDtoBusq, armfCarreraDtoBuscar.getCrrId(), "", "");
			retorno = "irListarApruebaRetiroMatricula";
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
		return retorno;
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}
	
	/**
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron el retiro de matrícula con los parámetros ingresados
	 */
	public void buscarMatriculas(){
		try {
			armfListFichaMatriculaDtoBusq = servArmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXapellidoXidentificacionXcarrera(armfPeriodoAcademicoBuscar.getPracId(), armfListCarreraDtoBusq, armfCarreraDtoBuscar.getCrrId(), armfEstudianteBuscar.getPrsIdentificacion(), armfEstudianteBuscar.getPrsPrimerApellido());
			if(armfListFichaMatriculaDtoBusq.size() == 0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.buscar.matriculas.no.encontrado.exception")));
				armfListFichaMatriculaDtoBusq = null;
			}
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.buscar.matriculas.exception")));
		}
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		armfTipoSolicitud=0;
		irAListarMatricula(armfUsuario);
		buscarMatriculas();
	}
	
	/**
	 * Método que permite ir a la visualización de la matricula (materias y demas datos)
	 * @param fichaMatriculaDto - fichaMatriculaDto objeto matricula del estudiante que envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerSolicitud(FichaMatriculaDto fichaMatriculaDto){
		try {
			armfFichaMatriculaDtoSeleccionado = fichaMatriculaDto;
			armfVisualizadorBotones = 1;
			//LLENO LA LISTA DE MATERIAS SOLICITADAS POR EL ESTUDIANTE
			armfListEstudianteDto = servArmfEstudianteDtoServicioJdbc.buscarEstudianteValidarRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(armfFichaMatriculaDtoSeleccionado.getPrsId(), armfFichaMatriculaDtoSeleccionado.getFcmtId(), armfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE);
			//LLENO LA LISTA DE MATERIAS TOTAL DEL ESTUDIANTE
			armfListMateriasTodas = servArmfEstudianteDtoServicioJdbc.buscarEstudianteXIdPersonaXIdMatricula(armfFichaMatriculaDtoSeleccionado.getPrsId(), armfFichaMatriculaDtoSeleccionado.getFcmtId(), armfFichaMatriculaDtoSeleccionado.getPracId());
			//LLENO LA LISTA DE MATERIAS INACTIVAS APROBADAS SOLICITUD
//			armfListMateriasInactivas = servArmfEstudianteDtoServicioJdbc.buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstado(armfFichaMatriculaDtoSeleccionado.getPrsId(), armfFichaMatriculaDtoSeleccionado.getFcmtId(), armfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE, DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);
			armfListMateriasInactivas = servArmfEstudianteDtoServicioJdbc.buscarEstudianteMateriaRetiradaXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstado(armfFichaMatriculaDtoSeleccionado.getPrsId(), armfFichaMatriculaDtoSeleccionado.getFcmtId(), armfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE, DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE);
			for (EstudianteJdbcDto item : armfListEstudianteDto) {
				item.setVisualizador(false);
				if(item.getNvlId()==NivelConstantes.NIVEL_NIVELACION_VALUE){
					armfTipoSolicitud=NivelConstantes.NIVEL_NIVELACION_VALUE;
				}
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerSolicitud";
	}
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(EstudianteJdbcDto materiaSeleccionada){
		armfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+materiaSeleccionada.getDtmtArchivoEstudiantes();
		if(armfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getDtmtArchivoEstudiantes();
			try{
				System.out.println("asdfasdfasdfasdf "+armfArchivoSelSt);
//				File f=new File(GeneralesConstantes.APP_PATH_ARCHIVOS_TEMPORAL+materiaSeleccionada.getDtmtArchivoEstudiantes());
//				if(f.exists())
//				{
//				    f.delete();
//				}
//				f.createNewFile(); 
//				FileObject destn = (FileObject) VFS.getManager().resolveFile(f.getAbsolutePath());
//				UserAuthenticator auth = new StaticUserAuthenticator("", "produ", "12345.a");
//				FileSystemOptions opts = new FileSystemOptions();
//
//				DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
//				FileObject fo = (FileObject) VFS.getManager().resolveFile(armfArchivoSelSt,opts);
//				destn.copyFrom( fo,Selectors.SELECT_SELF);
//				destn.close();
//				return new DefaultStreamedContent(destn.getContent().getInputStream(),GeneralesUtilidades.obtenerContentType(armfArchivoSelSt),nombre);
				
//					FileInputStream  fis = new FileInputStream(vstmfArchivoSelSt);
					URL oracle = new URL("file:"+armfArchivoSelSt);
//					 URL urlObject = new URL("/");
					    URLConnection urlConnection = oracle.openConnection();
					    InputStream inputStream = urlConnection.getInputStream();
					return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(armfArchivoSelSt),nombre);
				
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
				armfArchivoSelSt = null;
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (Exception e) {
			}
		}else{
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.StreamedContent.descargar.archivo.no.encontrado.exception")));
			return null;
		}
		return null;
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas 
	 * @param materiaDeshabilitar - materiaDeshabilitar entidad seleccionada para desabhilitar el link
	 */
	public void deshabilitar(EstudianteJdbcDto materiaDeshabilitar){
		for (EstudianteJdbcDto item : armfListEstudianteDto) {
			if(materiaDeshabilitar.getDtmtId() == item.getDtmtId()){
				item.setVisualizador(true);
				break;
			}
		}
	}
	
	/**
	 * Método que permite ir a la página de listar matriculas
	 * @return La navegación hacia la pagina xhtml de listar matriculas
	 */
	public String irRegresarSolicitud(){
		//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
		armfValidadorClic = 0;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		armfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		armfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		armfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		armfListEstudianteDto = null;
		return "irRegresarListarSolicitud";
	}
	
	/**
	 * Método que aprueba solicitud de retiro de materias matriculadas
	 * @return retorna - la navegación de la página listar matriculas retiro
	 */
	public String aprobarSolicitudRetiroMatricula(){
		
		String retorno = null;
		//REALIZA LA ACTUALIZACIÓN DE LOS ESTADOS DE DETALLE MATRICULA, RECORD ACADEMICO Y FICHA MATRICULA
		try {
			if(armfListEstudianteDto!=null && armfListEstudianteDto.size()>0 ){ //verifica que la lisa no este nula
				//proceso de transacción
				if(servArmfMatriculaServicio.aprobarSolicitudRetiroMatricula(armfListEstudianteDto, armfRetiroMatriculaTotal, DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE, DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE, armfFichaMatriculaDtoSeleccionado.getFcmtId(), FichaMatriculaConstantes.ESTADO_INACTIVO_VALUE, armfUsuario)){
					
					/******************************************************************************/
					/************************* PROCESO DE ENVIÓ DE MAIL ***************************/
					/******************************************************************************/
					
					//mail estudiante
					String mailEstudiante = null;
					String apellido1 = null;
					String apellido2 = null;
					String nombres = null;
					String carrera = null;
					String archivoGuardado = null;
					for (EstudianteJdbcDto item : armfListEstudianteDto) {
						mailEstudiante = item.getPrsMailInstitucional();
						apellido1 = item.getPrsPrimerApellido();
						apellido2 = item.getPrsSegundoApellido();
						nombres = item.getPrsNombres();
						carrera = item.getCrrDescripcion();
						archivoGuardado = item.getDtmtArchivoEstudiantes();
						break;
					}

					//datos del director de carrera
					Persona persona = new Persona();
					persona = servArmfPersonaServicio.buscarPersonaPorIdentificacion(armfUsuario.getUsrIdentificacion());

					//******************************************************************************
					//************************* ACA INICIA EL ENVIO DE MAIL CON ADJUNTO ************
					//******************************************************************************

					try{
						Connection connection = null;
						Session session = null;
						MessageProducer producer = null;
						ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",GeneralesConstantes.APP_NIO_ACTIVEMQ);
						connection = connectionFactory.createConnection();
						connection.start();
						session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
						// Creamos un productor
						producer = session.createProducer(destino);
						StringBuilder pathGeneralRequisitos = new StringBuilder();
						pathGeneralRequisitos.append(GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ archivoGuardado);
						Path path = Paths.get(pathGeneralRequisitos.toString());
						byte[] arregloRequisitos = Files.readAllBytes(path);

						//lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(mailEstudiante); //estudiante
						correosTo.add(persona.getPrsMailInstitucional()); //director de carrera
//						correosTo.add("dcollaguazo@uce.edu.ec"); 
						//path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo= new StringBuilder();
						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
						String fecha = formato.format(new Date());

						List<EstudianteJdbcDto> listaMaterias = new ArrayList<>();
						
						for (EstudianteJdbcDto item : armfListEstudianteDto) {
							item.setDtmtEstadoRespuesta(servArmfDetalleMatriculaServicio.buscarPorId(item.getDtmtId()).getDtmtEstadoRespuesta());
							listaMaterias.add(item);
						}
						
						sbCorreo= GeneralesUtilidades.generarAsuntoRespuestaRetiroMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
								apellido1+" "+apellido2+" "+nombres , GeneralesUtilidades.generaStringConTildes(carrera), listaMaterias);	

						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_SOLICITUD_MATRICULA,
								sbCorreo.toString()
								, "admin", "dt1c201s", true, arregloRequisitos, "Retiro de matricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );

						String jsonSt = pmail.generarMail();
						Gson json = new Gson();
						MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
						// 	Iniciamos el envío de mensajes
						ObjectMessage message = session.createObjectMessage(mailDto);
						producer.send(message);
					}  catch (Exception e) {
						e.printStackTrace();
					}

					//******************************************************************************
					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
					//******************************************************************************

					/******************************************************************************/
					/******************************************************************************/

					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.aprobar.solicitud.validacion.exitoso")));
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.aprobar.solicitud.validacion.no.aprobado")));
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.aprobar.solicitud.validacion.no.existen.materias.solicitadas")));
			}
			//SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			armfValidadorClic = 0;
			//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
			armfEstudianteBuscar.setPrsIdentificacion("");
			//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
			armfEstudianteBuscar.setPrsPrimerApellido("");
			//SETEO EL ID DE CARRERA DTO
			armfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//BUSCA LA LISTA DE MATRÍCULAS DEL ESTUDIANTE
			armfListFichaMatriculaDtoBusq = null;
			armfListFichaMatriculaDtoBusq = servArmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXapellidoXidentificacionXcarrera(armfPeriodoAcademicoBuscar.getPracId(), armfListCarreraDtoBusq, armfCarreraDtoBuscar.getCrrId(), "", "");
			retorno = "irRegresarListarSolicitud";
		} catch (MatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaMatriculaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e1) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e1.getMessage());
		}
		return retorno;
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		//INSTANCIO EL PERIODO ACADÉMICO
		armfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		armfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		armfEstudianteBuscar = new EstudianteJdbcDto();
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		armfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		armfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		armfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL ID DE PERIODO ACADEMICO 
		armfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//ANULO LA LISTA DE MATRICULAS
		armfListFichaMatriculaDtoBusq = null;
		//ANULO LA LISTA DE CARRERAS
		armfListCarreraDtoBusq = null;
		//INICIALIZO LA VARIABLE PARA INHABILITAR EL LINK DE DESCARGAR
		armfVisualizadorLink = 0;
		//INICIALIZO LA VARIABLE PARA EL MODAL DE ANULACIÓN DE MATRICULAS
		armfValidadorClic = 0;
	}
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton agregar materias para anular matrícula
	 * @return retora null para para cualquier cosa pero setea a 6 la variable rmfValidadorClic
	 * estado para poder agregra materias para anular
	 */
	public String verificarClickAgregarMateriaRetiro(){
		if(armfTipoSolicitud==NivelConstantes.NIVEL_NIVELACION_VALUE){
			boolean op = true;
			lazo:
			for (int i=0;i<armfListEstudianteDto.size();i++) {
				EstudianteJdbcDto comparador = new EstudianteJdbcDto();
				comparador = armfListEstudianteDto.get(i);
				for (int j=i;j<armfListEstudianteDto.size();j++) {
					if(!comparador.getEstadoSolicitudRetiro().equals(armfListEstudianteDto.get(j).getEstadoSolicitudRetiro())){
						op=false;
						break lazo;
					}
				}
			}
			if(!op){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor debe seleccionar una sola opción para todas las asignaturas de Nivelación.");
				armfValidadorClic = 0;
				return null;	
			}
			
		}
		
		//VERICO QUE EXISTAN MATERIAS PARA EL RETIRO
		if(armfListEstudianteDto.size() >= 1){
			//DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
			if(armfFichaMatriculaDtoSeleccionado.getCrrDependencia() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){ //si es nivelación
				armftipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE; 
			}else{ //si es otra, en este caso va ha tener de carrera o academico
				armftipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE; 
			}
			//VERIFICA QUE LA LISTA TENGA EL PATH DE LOS ARCHIVOS
			for (EstudianteJdbcDto materia : armfListEstudianteDto) {
				if(materia.getDtmtArchivoEstudiantes() == null){
					armfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.verifica.solicitud.no.existe.archivo.validacion")));
					break;
				}else if(materia.getEstadoSolicitudRetiro() == GeneralesConstantes.APP_ID_BASE){
					armfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Debe seleccionar el estado de todas las solicitudes");
					break;
				}else{
					armfValidadorClic = 6;
				}
			}
//			armfValidadorClic = 6;
//			//VERICACION SI HACE EL RETIRO EN TODAS LAS MATERIAS - PARA RETIRAR O ANULAR LA MATRICULA
//			if(armfListMateriasTodas != null && armfListEstudianteDto != null && armfListMateriasInactivas != null){
//				if(armfListMateriasTodas.size() == (armfListEstudianteDto.size() + armfListMateriasInactivas.size())){ //si el total de la suma de materias solicitadas mas las materias inactivas aprobadas solicitud
//					armfRetiroMatriculaTotal = true;
//				}else{
//					armfRetiroMatriculaTotal = false;
//				}
//			}
			//número de materias solicitadas para aprobar, en estado aprobado
			int contadorListaEstudiante = 0;
			for (EstudianteJdbcDto materiaAux : armfListEstudianteDto) {
				if(materiaAux.getEstadoSolicitudRetiro() == 0){
					contadorListaEstudiante = contadorListaEstudiante + 1;
				}
			}
			//numero de materias aprobadas el retiro
			int contadorListMatAproRetiro = 0;
			for (EstudianteJdbcDto materiaRetirada : armfListMateriasInactivas) {
				contadorListMatAproRetiro = contadorListMatAproRetiro + 1;
			}
			//numero total de materias que esta matriculado
			int contadorListMatTotal = 0;
			for (EstudianteJdbcDto materiaTotal : armfListMateriasTodas) {
				contadorListMatTotal = contadorListMatTotal + 1;
			}
			
			//VERICACION SI HACE EL RETIRO EN TODAS LAS MATERIAS - PARA RETIRAR O ANULAR LA MATRICULA
			if(armfListMateriasTodas != null && armfListEstudianteDto != null && armfListMateriasInactivas != null){
				if(contadorListMatTotal == (contadorListaEstudiante + contadorListMatAproRetiro)){ //si el total de la suma de materias solicitadas mas las materias inactivas aprobadas solicitud
					armfRetiroMatriculaTotal = true;
				}else{
					armfRetiroMatriculaTotal = false;
				}
			}
			
		}else{
			armfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.verifica.solicitud.no.existe.materias.validacion")));
		}
		return null;
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	public Usuario getArmfUsuario() {
		return armfUsuario;
	}
	public void setArmfUsuario(Usuario armfUsuario) {
		this.armfUsuario = armfUsuario;
	}
	public Integer getArmfTipoUsuario() {
		return armfTipoUsuario;
	}
	public void setArmfTipoUsuario(Integer armfTipoUsuario) {
		this.armfTipoUsuario = armfTipoUsuario;
	}
	public String getArmfTipoCarrera() {
		return armfTipoCarrera;
	}
	public void setArmfTipoCarrera(String armfTipoCarrera) {
		this.armfTipoCarrera = armfTipoCarrera;
	}
	public PeriodoAcademico getArmfPeriodoAcademicoBuscar() {
		return armfPeriodoAcademicoBuscar;
	}
	public void setArmfPeriodoAcademicoBuscar(PeriodoAcademico armfPeriodoAcademicoBuscar) {
		this.armfPeriodoAcademicoBuscar = armfPeriodoAcademicoBuscar;
	}
	public CarreraDto getArmfCarreraDtoBuscar() {
		return armfCarreraDtoBuscar;
	}
	public void setArmfCarreraDtoBuscar(CarreraDto armfCarreraDtoBuscar) {
		this.armfCarreraDtoBuscar = armfCarreraDtoBuscar;
	}
	public List<FichaMatriculaDto> getArmfListFichaMatriculaDtoBusq() {
		armfListFichaMatriculaDtoBusq = armfListFichaMatriculaDtoBusq==null?(new ArrayList<FichaMatriculaDto>()):armfListFichaMatriculaDtoBusq;
		return armfListFichaMatriculaDtoBusq;
	}
	public void setArmfListFichaMatriculaDtoBusq(List<FichaMatriculaDto> armfListFichaMatriculaDtoBusq) {
		this.armfListFichaMatriculaDtoBusq = armfListFichaMatriculaDtoBusq;
	}
	public List<CarreraDto> getArmfListCarreraDtoBusq() {
		armfListCarreraDtoBusq = armfListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):armfListCarreraDtoBusq;
		return armfListCarreraDtoBusq;
	}
	public void setArmfListCarreraDtoBusq(List<CarreraDto> armfListCarreraDtoBusq) {
		this.armfListCarreraDtoBusq = armfListCarreraDtoBusq;
	}
	public EstudianteJdbcDto getArmfEstudianteBuscar() {
		return armfEstudianteBuscar;
	}
	public void setArmfEstudianteBuscar(EstudianteJdbcDto armfEstudianteBuscar) {
		this.armfEstudianteBuscar = armfEstudianteBuscar;
	}
	public List<PeriodoAcademico> getArmfListPeriodoAcademicoBuscar() {
		return armfListPeriodoAcademicoBuscar;
	}
	public void setArmfListPeriodoAcademicoBuscar(List<PeriodoAcademico> armfListPeriodoAcademicoBuscar) {
		this.armfListPeriodoAcademicoBuscar = armfListPeriodoAcademicoBuscar;
	}
	public List<EstudianteJdbcDto> getArmfListEstudianteDto() {
		armfListEstudianteDto = armfListEstudianteDto==null?(new ArrayList<EstudianteJdbcDto>()):armfListEstudianteDto;
		return armfListEstudianteDto;
	}
	public void setArmfListEstudianteDto(List<EstudianteJdbcDto> armfListEstudianteDto) {
		this.armfListEstudianteDto = armfListEstudianteDto;
	}
	public List<EstudianteJdbcDto> getArmfListMateriasTodas() {
		armfListMateriasTodas = armfListMateriasTodas==null?(new ArrayList<EstudianteJdbcDto>()):armfListMateriasTodas;
		return armfListMateriasTodas;
	}
	public void setArmfListMateriasTodas(List<EstudianteJdbcDto> armfListMateriasTodas) {
		this.armfListMateriasTodas = armfListMateriasTodas;
	}
	public List<EstudianteJdbcDto> getArmfListMateriasInactivas() {
		armfListMateriasInactivas = armfListMateriasInactivas==null?(new ArrayList<EstudianteJdbcDto>()):armfListMateriasInactivas;
		return armfListMateriasInactivas;
	}
	public void setArmfListMateriasInactivas(List<EstudianteJdbcDto> armfListMateriasInactivas) {
		this.armfListMateriasInactivas = armfListMateriasInactivas;
	}
	public FichaMatriculaDto getArmfFichaMatriculaDtoSeleccionado() {
		return armfFichaMatriculaDtoSeleccionado;
	}
	public void setArmfFichaMatriculaDtoSeleccionado(FichaMatriculaDto armfFichaMatriculaDtoSeleccionado) {
		this.armfFichaMatriculaDtoSeleccionado = armfFichaMatriculaDtoSeleccionado;
	}
	public Integer getArmfVisualizadorBotones() {
		return armfVisualizadorBotones;
	}
	public void setArmfVisualizadorBotones(Integer armfVisualizadorBotones) {
		this.armfVisualizadorBotones = armfVisualizadorBotones;
	}
	public String getArmfArchivoSelSt() {
		return armfArchivoSelSt;
	}
	public void setArmfArchivoSelSt(String armfArchivoSelSt) {
		this.armfArchivoSelSt = armfArchivoSelSt;
	}
	public Integer getArmfVisualizadorLink() {
		return armfVisualizadorLink;
	}
	public void setArmfVisualizadorLink(Integer armfVisualizadorLink) {
		this.armfVisualizadorLink = armfVisualizadorLink;
	}
	public Integer getArmfValidadorClic() {
		return armfValidadorClic;
	}
	public void setArmfValidadorClic(Integer armfValidadorClic) {
		this.armfValidadorClic = armfValidadorClic;
	}
	public Integer getArmftipoCronograma() {
		return armftipoCronograma;
	}
	public void setArmftipoCronograma(Integer armftipoCronograma) {
		this.armftipoCronograma = armftipoCronograma;
	}
	public boolean isArmfRetiroMatriculaTotal() {
		return armfRetiroMatriculaTotal;
	}
	public void setArmfRetiroMatriculaTotal(boolean armfRetiroMatriculaTotal) {
		this.armfRetiroMatriculaTotal = armfRetiroMatriculaTotal;
	}
}
