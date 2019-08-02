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
   
 ARCHIVO:     SolicitudRetiroFortuitoMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja el retiro de la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 28-11-2018			 Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reajusteMatricula;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
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

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CausalException;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoCausalConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSolicitudRetiroFortuitoForm;
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
 * Clase (session bean) SolicitudRetiroFortuitoMatriculaForm. 
 * Bean de sesion que maneja el retiro de la matricula del estudiante. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "solicitudRetiroFortuitoMatriculaForm")
@SessionScoped
public class SolicitudRetiroFortuitoMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = 9210281719911332407L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario srfmfUsuario;
	
	
	//PARA BUSQUEDA
	private PeriodoAcademico srfmfPeriodoAcademicoBuscar;
	private CarreraDto srfmfCarreraDtoBuscar;
	private List<FichaMatriculaDto> srfmfListFichaMatriculaDtoBusq;
	private List<CarreraDto> srfmfListCarreraDtoBusq;
	private List<PeriodoAcademico> srfmfListPeriodoAcademicoBusq;
	//private List<FichaMatriculaDto> srfmfListFichaMatriculaDtoBusqCarr;
	
	//PARA VISUALIZACIÓN
	private List<EstudianteJdbcDto> srfmfListEstudianteDto;
	private FichaMatriculaDto srfmfFichaMatriculaDtoSeleccionado;
	private Integer srfmfVisualizadorBotones;
	
	private FichaMatriculaDto srfmfFichaMatriculaEstado;
	private List<EstudianteJdbcDto> srfmfListMatriculaEstado;
	private List<Causal> srfmfListCausalRetiroFortuito;
	
	
	//PARA EL RETIRO
	private Integer srfmfValidadorSeleccion;
	private Integer srfmfValidadorClic;
	private Integer srfmftipoCronograma;
	private CronogramaActividadJdbcDto srfmfCronogramaActividad;
	private PersonaDto srfmfDirCarrera;
	private Dependencia srfmfDependencia;
	private Carrera srfmfCarrera;
	private PeriodoAcademico srfmfPeriodoActivo;
	private List<EstudianteJdbcDto> srfmfListaGuardada;
	private int srfmfActivadorReporte;
	private List<EstudianteJdbcDto> srfmfListaMateriasReporte;
	private boolean srfmfDeshabilitaReportePdf;
	private boolean srfmfDeshabilitaFinalizar;
	
	private int srfmfEstadoCambioDtmt;
	private int srfmfEstadoRecordEstudiante;
	
	//PARA CARGA DE ARCHIVO
	private String srfmfNombreArchivoAuxiliar;
	private String srfmfNombreArchivoSubido;

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
	PeriodoAcademicoServicio servSrfmfPeriodoAcademicoServicio;
	@EJB 
	FichaMatriculaDtoServicioJdbc servSrfmfFichaMatriculaDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servSrfmfEstudianteDtoServicioJdbc;
	@EJB 
	CronogramaActividadDtoServicioJdbc servSrfmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	MatriculaServicio servSrfmfMatriculaServicio;
	@EJB
	UsuarioRolDtoServicioJdbc servSrfmfUsuarioRolDtoServicioJdbc;
	@EJB
	CausalServicio servSrfmfCausalRetiro;
	@EJB
	CarreraServicio servSrfmfCarrera;
	@EJB	
	PersonaDtoServicioJdbc servSrfmfPersonaRol;
	@EJB 
	DependenciaServicio servSrfmfDependencialServicio;
	@EJB
	PeriodoAcademicoServicio servSrfmPeriodoAcademico;
	

	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/

	/**
	 * Dirige la navegación hacia la página de listarMatriculas
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar matricula
	 */
	public String irAListarMatricula(Usuario usuario) {
		srfmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			
			//BUSCA EL PERIODO ACADEMICO ACTIVO
	//		srfmfPeriodoAcademicoBuscar = servSrfmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			//BUSCO PERIODOS ACADEMICOS PREGRANDO Y SUFICIENCIA
	//		srfmfListPeriodoAcademicoBusq = servSrfmfPeriodoAcademicoServicio.buscarXestadoPracXtipoPracTodos(GeneralesConstantes.APP_ID_BASE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			//Busco Periodo Activo y en cierre de pregrados
	//		srfmfListPeriodoAcademicoBusq =servSrfmfPeriodoAcademicoServicio.listarXestadoXtipoPeriodoActivoEnCierre(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			
			// las matricular que deseen que se muestre:  15 abril 2019
			List<PeriodoAcademico> listPeriodoTodosAux = servSrfmPeriodoAcademico.listarTodosPreGradoOrdenadosXId();
			srfmfListPeriodoAcademicoBusq.add(listPeriodoTodosAux.get(0));  //solo el periodo activo
		//	srfmfListPeriodoAcademicoBusq.add(listPeriodoTodosAux.get(1));  //1 periodo anterior por quitar  MQ:6 mar 2019
		//	srfmfListPeriodoAcademicoBusq.add(listPeriodoTodosAux.get(2)); // dos periodos anteriores por quitar MQ:6 mar 2019
		
			//Solo periodo activo pregrado	
			//srfmfListPeriodoAcademicoBusq =servSrfmfPeriodoAcademicoServicio.listarTodosActivo();  
					
			
			//BUSCA LA LISTA DE MATRÍCULAS DEL ESTUDIANTE
			srfmfListFichaMatriculaDtoBusq = servSrfmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraTodosPregrado(GeneralesConstantes.APP_ID_BASE, usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE,srfmfListPeriodoAcademicoBusq );
			//ANULO LA LISTA DE MATRICULAS
			getSrfmfListCarreraDtoBusq();
			//OBTENER LA LISTA DE CARRERAS QUE LOS ESTUDIANTES ESTAN MATRICULADOS
			for (FichaMatriculaDto itemMatricula : srfmfListFichaMatriculaDtoBusq) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
				Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
				for (CarreraDto itemCarrera : srfmfListCarreraDtoBusq) { // recorro la lista de carreras a las que el estudiante esta matriculado 
					if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
						encontro = true; // asigno el booleano a verdado
					}
				} //fin recorrido de lista auxiliar de carreras
				if(encontro == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
					CarreraDto carreraAgregar = new CarreraDto();
					carreraAgregar.setCrrId(itemMatricula.getCrrId());
					carreraAgregar.setCrrDetalle(itemMatricula.getCrrDetalle());
					carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
					srfmfListCarreraDtoBusq.add(carreraAgregar);
				}
			}
			retorno = "irListarMatriculas";
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.iniciar.solicitud.retiro.ficha.matricula.exception")));
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
	 * Método que permite ir a la visualización de la matricula (materias y demas datos)
	 * @param fichaMatriculaDto - fichaMatriculaDto objeto matricula del estudiante que envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerMatricula(FichaMatriculaDto fichaMatriculaDto){
		String retorno = null;
		srfmfActivadorReporte=0;
		srfmfDeshabilitaReportePdf= true;
		srfmfDeshabilitaFinalizar= false;
		try {
			
			srfmfListCausalRetiroFortuito = servSrfmfCausalRetiro.listarxTipo(TipoCausalConstantes.TIPO_CAUSAL_RETIRO_ASIGNATURA_FORTUITO_VALUE);
			
			srfmfFichaMatriculaDtoSeleccionado = fichaMatriculaDto;
			//LLENO LA LISTA DE MATERIAS DEL ESTUDIANTE QUE TIENE DE LA MATICULA CONSULTADA
			srfmfListEstudianteDto = servSrfmfEstudianteDtoServicioJdbc.buscarEstudianteRetiroFortuitoMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(srfmfUsuario.getUsrPersona().getPrsId(),srfmfFichaMatriculaDtoSeleccionado.getFcmtId(), srfmfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			if(srfmfListEstudianteDto.size() > 0){
				Boolean verificar = true;
				for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
					
					if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE){
						verificar = false;
					}
					//En el caso de que sea materia de nivelación se selecciona automáticamente para retiro todas las materias de Nivelación
					if(item.getNvlId()==NivelConstantes.NIVEL_NIVELACION_VALUE){
						item.setSeleccionado(true);
					}
				}
				
				if(verificar){
					srfmfVisualizadorBotones = 1;
					retorno = "irVerMatricula";
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ir.ver..matricula.legalizada.validacion")));
					return null;
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ver.solicitud.retiro.ficha.matricula.no.hay.datos")));
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ver.solicitud.retiro.ficha.matricula.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ver.solicitud.retiro.ficha.matricula.no.encontrado.exception")));
		} catch (CausalNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Método que permite ir a la visualización del estado de retiro matricula (materias y demas datos)
	 * @param fichaMatriculaDto - fichaMatriculaDto objeto matricula del estudiante que envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver estado retiro matrícula
	 */
	public String irVerEstadoRetiro(FichaMatriculaDto fichaMatriculaDto){
		String retorno = null;
		try {
			srfmfFichaMatriculaEstado = fichaMatriculaDto;
			//LLENO LA LISTA DE MATERIAS DEL ESTUDIANTE QUE ESTA MATRÍCULADO
			srfmfListMatriculaEstado=servSrfmfEstudianteDtoServicioJdbc.buscarEstudianteRetiroFortuitoMatriculaXprsIdXmtrIdXpracIdXdtmtParaEstado(srfmfUsuario.getUsrPersona().getPrsId(),srfmfFichaMatriculaEstado.getFcmtId(), srfmfFichaMatriculaEstado.getPracId(), GeneralesConstantes.APP_ID_BASE);
			
			
			if(srfmfListMatriculaEstado.size() > 0){
				Boolean verificar = true;
				for (EstudianteJdbcDto item : srfmfListMatriculaEstado) {
					if(item.getRcesEstado() == -1){
						verificar = false;
					}
					//En el caso de que sea materia de nivelación se selecciona automáticamente para retiro todas las materias de Nivelación
					if(item.getNvlId()==NivelConstantes.NIVEL_NIVELACION_VALUE){
						item.setSeleccionado(true);
					}
				}
				if(verificar){
					retorno = "irVerEstadoRetiro";
				}else{
					
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ir.ver..matricula.legalizada.validacion")));
				
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ver.solicitud.retiro.ficha.matricula.no.hay.datos")));
				
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ver.solicitud.retiro.ficha.matricula.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ver.solicitud.retiro.ficha.matricula.no.encontrado.exception")));
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Método que permite ir a la página de listar matriculas
	 * @return La navegación hacia la pagina xhtml de listar matriculas
	 */
	public String irRegresarMatricula(){
//		fmfDesactivar = "false";
		srfmfListEstudianteDto = null;
		return "irRegresarListarMatricula";
	}
	
	/**
	 * Método que agrega materias para retiro por casos fortuitos
	 * @return retorna - la navegación de la página listar matriculas
	 */
	public String generarSolicitudRetiroFortuito(){
		String retorno = null;
		//VERIFICO QUE EXISTAN MATERIAS PARA RETIRO DE LA MATRICULA 
		if(srfmfListEstudianteDto!=null && srfmfListEstudianteDto.size()>0){
			try {
				//ASIGNO LAS MATERIAS SELECCIONADAS A OTRA LISTA PARA GUARDAR
				List<EstudianteJdbcDto> listaSeleccionada = new ArrayList<>();
				for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
					if(item.isSeleccionado()){
						listaSeleccionada.add(item);
					}
				}
				//CAMBIO DE ESTADO A SOLICITUD DE RETIRO EN DETALLE MATRICULA CON LA LISTA DE MATERIAS
				String extension = GeneralesUtilidades.obtenerExtension(srfmfNombreArchivoSubido);
				List<EstudianteJdbcDto> listaSeleccionadaGuarda = new ArrayList<>();
				List<EstudianteJdbcDto> listaSeleccionadaNoGuarda = new ArrayList<>();
				String rutaNombre = null;
				String rutaTemporal = null;
				String archivoGuardado = null;
				
				if(servSrfmfMatriculaServicio.generarSolicitudRetiroFortuito(listaSeleccionada, srfmfNombreArchivoSubido)){
					//PARA CARGA DE ARCHIVO EN EL SERVIDOR
					if(srfmfNombreArchivoSubido != null){
						for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
							if(item.isSeleccionado()){
								rutaNombre = DetalleMatriculaConstantes.DTMT_ARCHIVO_SOLICITUD_RETIRO_FORTUITO_LABEL+"-"+item.getDtmtId()+ "." + extension;
								archivoGuardado = rutaNombre;
								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + srfmfNombreArchivoSubido;
								
//								String smbUrl = "smb://produ:12345.a@10.20.1.63/siiuArch/archivos/"+GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre ;
								
								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre);
								listaSeleccionadaGuarda.add(item);
							}else{
								listaSeleccionadaNoGuarda.add(item);
							}
							item.setRutaPdf(archivoGuardado); //guardo en la lista el nombre del pdf
							
							rutaNombre = null;
							rutaTemporal = null;
						}
					
					//BUSCAR LA CARRERA
						//Carrera crrAux2= new Carrera();
						//crrAux2 = 
						
						srfmfCarrera= new Carrera();
						
						srfmfCarrera=servSrfmfCarrera.buscarPorId(srfmfFichaMatriculaDtoSeleccionado.getCrrId());
						
						
						try {
							//******************************************************************************
							  //************************* ACA INICIA EL ENVIO DE MAIL ************************
							//******************************************************************************
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
							
							
							JasperReport jasperReport = null;
							JasperPrint jasperPrint;
							List<Map<String, Object>> frmRrmCampos = null;
							Map<String, Object> frmRrmParametros = null;
							
							String frmRrmNombreReporte = null;
							List<Causal> listaCausalaux = new ArrayList<>();
							// *******************************************************************************************************//
							// ********* GENERACION DEL REPORTE DE LA SOLICITUD DE RETIRO FORTUITO PARA ENVIAR EL PDF ADJUNTO*********//
							// *******************************************************************************************************//
							// *******************************************************************************************************//
							frmRrmNombreReporte = "solicitudRetiroFortuito";
							frmRrmParametros = new HashMap<String, Object>();
							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
							//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
							String fecha = formato.format(new Date());
							frmRrmParametros.put("fecha",fecha);

							String nombres = null;
							if(srfmfFichaMatriculaDtoSeleccionado!=null){
							 nombres=srfmfFichaMatriculaDtoSeleccionado.getPrsNombres()+" "+srfmfFichaMatriculaDtoSeleccionado.getPrsPrimerApellido().toUpperCase()+" "
									+(srfmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido() == null?" ":srfmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido());
							}else{
								nombres= " ";
								
							}
							
							String dirCarrera= null;
							
							if(srfmfDirCarrera!=null){
							  dirCarrera = srfmfDirCarrera.getPrsNombres()+" "+srfmfDirCarrera.getPrsPrimerApellido().toUpperCase()+" "
									+(srfmfDirCarrera.getPrsSegundoApellido() == null?" ":srfmfDirCarrera.getPrsSegundoApellido());
							}else{
								dirCarrera=" ";
								
							}
							
							StringBuilder sbTextoInicial = new StringBuilder();
							sbTextoInicial.append("Señor(a)");sbTextoInicial.append("\n");
							sbTextoInicial.append(dirCarrera);sbTextoInicial.append("\n");
							sbTextoInicial.append("DIRECTOR(A) DE LA CARRERA DE ");
							if(srfmfCarrera.getCrrDescripcion()!=null){
								sbTextoInicial.append(srfmfCarrera.getCrrDescripcion());sbTextoInicial.append("\n");
							}else{
								sbTextoInicial.append(" ");sbTextoInicial.append("\n");
							}
							sbTextoInicial.append("FACULTAD DE ");
							if(srfmfDependencia.getDpnDescripcion()!=null){
								sbTextoInicial.append(srfmfDependencia.getDpnDescripcion());sbTextoInicial.append("\n");
							}else{
								sbTextoInicial.append(" ");sbTextoInicial.append("\n");
							}
							sbTextoInicial.append("Presente.- ");sbTextoInicial.append("\n\n");
							sbTextoInicial.append("Señor(a) Director(a)");sbTextoInicial.append("\n");
							
							frmRrmParametros.put("textoInicial", sbTextoInicial.toString());
							
							StringBuilder sbTexto = new StringBuilder();
							sbTexto.append("Yo, "); 
							sbTexto.append(nombres);
							sbTexto.append(" con identificación No. ");
							sbTexto.append(srfmfFichaMatriculaDtoSeleccionado.getPrsIdentificacion());
							sbTexto.append(" estudiante de la Carrera de ");
							if(srfmfCarrera.getCrrDescripcion()!=null){
								sbTexto.append(srfmfCarrera.getCrrDescripcion());
								}else{
									sbTexto.append(" ");
								}
							sbTexto.append(" de la Facultad de ");
							if(srfmfDependencia.getDpnDescripcion()!=null){
								sbTexto.append(srfmfDependencia.getDpnDescripcion());
								}else{
									sbTexto.append(" ");
								}
						
							sbTexto.append(", solicito a usted muy comedidamente considerar  y analizar en el Consejo Directivo de la Facultad mi pedido de retiro de asignaturas por situaciones fortuitas o de fuerza mayor de acuerdo al siguiente detalle: ");
							
							frmRrmParametros.put("texto", sbTexto.toString());
							StringBuilder sbPeriodo = new StringBuilder();
							StringBuilder sbCodigo = new StringBuilder();
							StringBuilder sbAsignatura = new StringBuilder();
							StringBuilder sbCausal = new StringBuilder();
							StringBuilder sbEvidencia = new StringBuilder();
							
							for (EstudianteJdbcDto item : listaSeleccionada) {
								if(item.getMtrDescripcion().length() <= 44){
									if(srfmfPeriodoActivo.getPracDescripcion()!=null){
										sbPeriodo.append(srfmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n");
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
										
										if(item.getRutaPdf()!=null){
										sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n");
										}else{
											sbEvidencia.append(" ");sbEvidencia.append("\n\n");
										}
								}
								if(item.getMtrDescripcion().length() > 44 && item.getMtrDescripcion().length() <= 88){
									if(srfmfPeriodoActivo.getPracDescripcion()!=null){
										sbPeriodo.append(srfmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n");
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
									
									if(item.getRutaPdf()!=null){
									sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n\n");
									}else{
										sbEvidencia.append(" ");sbEvidencia.append("\n\n\n");
									}
									
								}
								if(item.getMtrDescripcion().length() > 88 && item.getMtrDescripcion().length() <= 132){
									if(srfmfPeriodoActivo.getPracDescripcion()!=null){
										sbPeriodo.append(srfmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n");
									}
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n");
									}else{
										sbCodigo.append("");sbCodigo.append("\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n");
									
								
									   if(item.getCslCodigo()!=null){
										sbCausal.append(item.getCslCodigo());sbCausal.append("\n\n\n\n");
										}else{
										 sbCausal.append(" ");sbCausal.append("\n\n\n\n");
										}
										
										if(item.getRutaPdf()!=null){
										sbEvidencia.append(item.getRutaPdf());sbEvidencia.append("\n\n\n\n");
										}else{
											sbEvidencia.append(" ");sbEvidencia.append("\n\n\n\n");
										}
								}
								if(item.getMtrDescripcion().length() > 132 && item.getMtrDescripcion().length() <= 176){
									if(srfmfPeriodoActivo.getPracDescripcion()!=null){
										sbPeriodo.append(srfmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n");
									}else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n");
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n");
									}else{
										sbCodigo.append("");sbCodigo.append("\n\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
									
									if (item.getCslCodigo() != null) {
										sbCausal.append(item.getCslCodigo());
										sbCausal.append("\n\n\n\n\n");
									} else {
										sbCausal.append(" ");
										sbCausal.append("\n\n\n\n\n");
									}

									if (item.getRutaPdf() != null) {
										sbEvidencia.append(item.getRutaPdf());
										sbEvidencia.append("\n\n\n\n\n");
									} else {
										sbEvidencia.append(" ");
										sbEvidencia.append("\n\n\n\n\n");
									}
								}
								if(item.getMtrDescripcion().length() > 176 && item.getMtrDescripcion().length() <= 220){
									if(srfmfPeriodoActivo.getPracDescripcion()!=null){
										sbPeriodo.append(srfmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n");
								   }else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n");
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n");
									}else{
										sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
									
									if (item.getCslCodigo() != null) {
										sbCausal.append(item.getCslCodigo());
										sbCausal.append("\n\n\n\n\n\n");
									} else {
										sbCausal.append(" ");
										sbCausal.append("\n\n\n\n\n\n");
									}

									if (item.getRutaPdf() != null) {
										sbEvidencia.append(item.getRutaPdf());
										sbEvidencia.append("\n\n\n\n\n\n");
									} else {
										sbEvidencia.append(" ");
										sbEvidencia.append("\n\n\n\n\n\n");
									}
								}
								if(item.getMtrDescripcion().length() > 220 && item.getMtrDescripcion().length() <= 264){
									if(srfmfPeriodoActivo.getPracDescripcion()!=null){
										sbPeriodo.append(srfmfPeriodoActivo.getPracDescripcion());sbPeriodo.append("\n\n\n\n\n\n\n");
								   }else{
										sbPeriodo.append(" ");sbPeriodo.append("\n\n\n\n\n\n\n");
									}
									
									if(item.getMtrCodigo() != null){
										sbCodigo.append(item.getMtrCodigo());sbCodigo.append("\n\n\n\n\n\n\n");
									}else{
										sbCodigo.append("");sbCodigo.append("\n\n\n\n\n\n\n");
									}
									sbAsignatura.append(item.getMtrDescripcion());sbAsignatura.append("\n\n\n");
									
									
									if (item.getCslCodigo() != null) {
										sbCausal.append(item.getCslCodigo());
										sbCausal.append("\n\n\n\n\n\n\n");
									} else {
										sbCausal.append(" ");
										sbCausal.append("\n\n\n\n\n\n\n");
									}

									if (item.getRutaPdf() != null) {
										sbEvidencia.append(item.getRutaPdf());
										sbEvidencia.append("\n\n\n\n\n\n\n");
									} else {
										sbEvidencia.append(" ");
										sbEvidencia.append("\n\n\n\n\n\n\n");
									}
								}
								frmRrmParametros.put("periodo", sbPeriodo.toString());
								frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
								frmRrmParametros.put("asignatura", sbAsignatura.toString());
								frmRrmParametros.put("causal", sbCausal.toString());
								frmRrmParametros.put("evidencia", sbEvidencia.toString());
								
								if(srfmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido() != null){
									frmRrmParametros.put("estudiante", srfmfFichaMatriculaDtoSeleccionado.getPrsNombres()+" "+srfmfFichaMatriculaDtoSeleccionado.getPrsPrimerApellido()+" "+srfmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido());
								}else{
									frmRrmParametros.put("estudiante", srfmfFichaMatriculaDtoSeleccionado.getPrsNombres()+" "+srfmfFichaMatriculaDtoSeleccionado.getPrsPrimerApellido()+" "+srfmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido());
									
								}
								if(srfmfUsuario.getUsrNick()!=null){
									frmRrmParametros.put("nick", srfmfUsuario.getUsrNick());
								}else{
									frmRrmParametros.put("nick", " ");
								}
																				
								Boolean encontrado = false;
								Causal objetoCausal= new Causal();
								for (Causal causal : listaCausalaux) {//LLENO LA LISTA DE CAUSALES
									if(causal.getCslCodigo().equals(item.getCslCodigo())){// SI YA EXISTE CONTINUO
										encontrado = true;
										break;
									}
								}
								
								if(encontrado ==false){ //SINO EXIST EL CAUSAL EN LA LISTA  LA AGREGO
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
							pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteSolicitudRetiroFortuito");
							frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabecera.png");
							frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pie.png");
							frmRrmParametros.put("uce_logo", pathGeneralReportes+"/uce_logo.png");
							
							frmRrmCampos = new ArrayList<Map<String, Object>>();
							Map<String, Object> datoTercera = null;
							datoTercera = new HashMap<String, Object>();
							frmRrmCampos.add(datoTercera);
						
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/solicitudRetiroFortuito.jasper")));
							jasperPrint = JasperFillManager.fillReport(jasperReport,frmRrmParametros, new JREmptyDataSource());
							
							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
										
							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(srfmfUsuario.getUsrPersona().getPrsMailInstitucional());
							//path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo= new StringBuilder();
							formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
							String fechaCorreo = formato.format(new Date());
							String carreraCorreo= null;
							if(srfmfCarrera.getCrrDescripcion()!=null){
								 carreraCorreo=srfmfCarrera.getCrrDescripcion();
							}else{
								 carreraCorreo= " ";
							}
								
							sbCorreo= GeneralesUtilidades.generarAsuntoSolicitudRetiroFortuito(GeneralesUtilidades.generaStringParaCorreo(fechaCorreo.toString()),
									nombres, GeneralesUtilidades.generaStringParaCorreo(carreraCorreo));
							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_RETIRO_POR_SITUACION_FORTUITA,
												sbCorreo.toString()
												, "admin", "dt1c201s", true, arreglo, "solicitudRetiroFortuito."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
							String jsonSt = pmail.generarMail();
							Gson json = new Gson();
							MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
							// 	Iniciamos el envío de mensajes
							ObjectMessage message = session.createObjectMessage(mailDto);
							producer.send(message);
							
							// Establecemos en el atributo de la sesión la lista de mapas de
							// datos frmCrpCampos y parámetros frmCrpParametros para abrir pdf generado

							FacesContext context = FacesContext.getCurrentInstance();
							HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
							HttpSession httpSession = request.getSession(false);
							httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
							httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
							httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
							
							srfmfActivadorReporte = 1;
							
						} catch (Exception e) {
							e.printStackTrace();
						}
							//******************************************************************************
							//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
							//******************************************************************************
								
						

					}
					
					
					//copio las lista para el reporte PDF
				
					srfmfListaMateriasReporte =new ArrayList<>();
					srfmfListaMateriasReporte.addAll(listaSeleccionada);
				   srfmfDeshabilitaReportePdf= false;
				   srfmfDeshabilitaFinalizar= true;
					
					//BUSCO LAS MATERIAS MATRICULADAS QUE TIENE EL ESTUDIANTE
									
					srfmfListEstudianteDto = listaSeleccionadaNoGuarda;
					listaSeleccionada = null;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.exitoso")));
				}else{
					srfmfListEstudianteDto = listaSeleccionadaGuarda;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.no.generada.exception")));
				}
						
				
				
				srfmfValidadorClic = 0;
				srfmfNombreArchivoAuxiliar = null;
				srfmfNombreArchivoSubido = null;
				retorno = "irVerMatricula";
			} catch (Exception e) {
				e.getStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		}else{
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.no.seleccion.materia.exception")));
		}
		srfmfValidadorClic = 0;
		return retorno;
	}
	
	/**
	 * Método que busca la lista de matriculas de acuerdo a los parametros ingresados.
	 * 
	 */
	
	
	public void buscar(){
		try {
			
			srfmfListFichaMatriculaDtoBusq = servSrfmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraTodosPregrado(srfmfPeriodoAcademicoBuscar.getPracId(), srfmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE,srfmfListPeriodoAcademicoBusq );

		//	srfmfListFichaMatriculaDtoBusq = servSrfmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraTodos(srfmfPeriodoAcademicoBuscar.getPracId(), srfmfUsuario.getUsrPersona().getPrsIdentificacion(), srfmfCarreraDtoBuscar.getCrrId());
	
		} catch (FichaMatriculaException e) {
			//e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
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
		srfmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//SETEO PERIODO ACADMEICO
		srfmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INSTANCIO LA CARRERA DTO
		srfmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO LA CRONOGRAMA ACTIVIDAD DTO
		srfmfCronogramaActividad = new CronogramaActividadJdbcDto();
		//ANULO LA LISTA DE MATRICULAS
		srfmfListFichaMatriculaDtoBusq = null;
		//ANULO LA LISTA DE CARRERAS
		srfmfListCarreraDtoBusq = null;
		//INICIALIZO LA VARIABLE PARA EL MODAL DE ANULACIÓN DE MATRICULAS
		srfmfValidadorClic = 0;
		//INICIALIZO LA VARIABLE DE ARCHIVO SUBIDO PARA VACIAR DATOS 
		srfmfNombreArchivoSubido = null;
		//INICIALIZO LA VARIABLE DE ARCHIVO AUXILIAR PARA VACIAR DATOS 
		srfmfNombreArchivoAuxiliar = null;
		srfmfDeshabilitaReportePdf= true;
		srfmfDeshabilitaFinalizar= false;
		srfmfListPeriodoAcademicoBusq = new ArrayList<>();
	
	}
	
	//SELECCIONA TODOS LOS ITEMS DE LA LISTA
	/**
	 * Método que realiza la selección del check box de todos 
	 * o de ninguna materia para el retiro de matrícula 
	 */
	public void seleccionarTodosAgregarRetiro(){
		if(srfmfListEstudianteDto!= null && srfmfListEstudianteDto.size()>0){
			for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
				item.setSeleccionado(srfmfValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
			}
		}
	}
	
	//CARGA DE ARCHIVO
	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event - event archivo oficio que presenta el estudiante para anular matrícula
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		srfmfNombreArchivoSubido = archivo.getFile().getFileName();
		srfmfNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + srfmfNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(),	rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.handleFileUpload.carga.archivo.exception")));
		}
	}
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton agregar materias para registrar el retiro de las asignaturas
	 * @return retora null para para cualquier cosa pero setea a 6 la variable srfmfValidadorClic
	 * estado para poder agregra materias para anular
	 */
	public String verificarClickAgregarRetiroFortuito(){
		
		Date fechaActual = new Date();
		PeriodoAcademico PeriodoActivoRealAux= new PeriodoAcademico();
		int numeroDias = GeneralesConstantes.APP_ID_BASE;		
		//DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		if(srfmfFichaMatriculaDtoSeleccionado.getCrrDependencia() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){ //si es nivelación
			srfmftipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE; 
		}else{ //si es otra, en este caso va ha tener de carrera o academico
			srfmftipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE; 
		}
		
		if(srfmfFichaMatriculaDtoSeleccionado.getCrrTipo() == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
			srfmftipoCronograma = CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE;
		}
		
		//BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			srfmfCronogramaActividad = servSrfmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(srfmfFichaMatriculaDtoSeleccionado.getCrrId(), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, srfmftipoCronograma, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE, ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.no.encontrado.exception")));
		}
		
		
		
		try {
			srfmfDirCarrera=servSrfmfPersonaRol.buscarDirectorCarreraxidCarrera(srfmfFichaMatriculaDtoSeleccionado.getCrrId());
			srfmfDependencia = servSrfmfDependencialServicio.buscarFacultadXcrrId(srfmfFichaMatriculaDtoSeleccionado.getCrrId());
			srfmfPeriodoActivo =servSrfmfPeriodoAcademicoServicio.buscarPorId(srfmfFichaMatriculaDtoSeleccionado.getPracId());  //Es el periodo academico de la ficha matricula seleccionada
			
			PeriodoActivoRealAux= servSrfmfPeriodoAcademicoServicio.buscarPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
		
		} catch (PersonaDtoException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.personaDto.exception")));
			return null;
	
		} catch (PersonaDtoNoEncontradoException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.personaDto.no.encontrado.exception")));

			return null;
		} catch (DependenciaNoEncontradoException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.dependencia.no.encontrado.exception")));

			return null;
		} catch (PeriodoAcademicoNoEncontradoException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.periodo.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.periodo.exception")));
			return null;
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}

		
	
		
		//CALCULO EL NÚMERO DE DIAS
		if(srfmfCronogramaActividad.getPlcrFechaInicio() != null){
			numeroDias = GeneralesUtilidades.calcularDiferenciFechasEnDiasAbsolutos(srfmfCronogramaActividad.getPlcrFechaInicio(), fechaActual); //realizo la diferencia entre las dos fechas
			//VALIDACIÓN DENTRO DE LO ESTABLECIDO
			
			boolean  cumpleMayortreintaDias = false;
			
			if(srfmfFichaMatriculaDtoSeleccionado.getPracId() !=PeriodoActivoRealAux.getPracId() ){
				cumpleMayortreintaDias= true;
				
			}else{
				if(numeroDias > ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE && numeroDias >= 0){
					cumpleMayortreintaDias= true;
					
				}
				
			}
			
			
		//  if(numeroDias > ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE && numeroDias >= 0){   //  CAMBIO  8 marzo 2019
			
			if(cumpleMayortreintaDias){
						
				//VERIFICA QUE SE HA SELECCIONADO MATERIAS
				boolean existeSeleccionados = false;
				for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
					if(item.isSeleccionado()){
						existeSeleccionados = true;
					}
				}
				
				//VERIFICA QUE TODOS LOS SELECCIONADOS TENGAN CAUSAL
				boolean CausalSeleccionado = true;
				for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
					if(item.isSeleccionado()){
						if(item.getCslId()==GeneralesConstantes.APP_ID_BASE){
						CausalSeleccionado = false;
						}
					}
				}
				
				
			//VERIFICO  QUE EXISTA EL DIRECTORIO PARA GUARDAR EL PDF	
	        String pathDirGuardar =GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE;
				
	        File directorio = new File(pathDirGuardar);
	        boolean existeDirectorio= true;
				if(!directorio.exists()){
					
					existeDirectorio= false;
				}
				
				
				
			try{	
				
				for (EstudianteJdbcDto item : srfmfListEstudianteDto) {
					if(item.isSeleccionado()){
						if(item.getCslId()!=GeneralesConstantes.APP_ID_BASE){
												
							Causal	auxCausal = servSrfmfCausalRetiro.buscarPorId(item.getCslId());
							item.setCslCodigo(auxCausal.getCslCodigo());
							item.setCslDescripcion(auxCausal.getCslDescripcion());
							item.setCslEstado(auxCausal.getCslEstado());
													
							
						}
					}
				}
				
		} catch (CausalNoEncontradoException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.causal.no.encontrado.exception")));
			return null;
		} catch (CausalException e) {
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.causal.exception")));
			return null;
		}
				
				
			if(existeDirectorio){	
				//VERIFICA QUE SE HA SUBIDO O CARGADO EL ARCHIVO
				if(existeSeleccionados){ //verifica que ha seleccionado materias
					
				if(CausalSeleccionado){
					
					if(srfmfNombreArchivoSubido != null){ //verifica que ha subido el archivo
						srfmfValidadorClic = 6;
					} else{
						srfmfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.archivo.no.cargado.validacion")));
					}
				} else{//CausalSeleccionado
					srfmfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.sin.causal.validacion")));
				
				}
				
				
				} else{
					srfmfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.no.seleccionada.validacion")));
				}
				
			}else{
				srfmfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.acceso.carpeta.validacion")));
				
			}
				
				
			}else{
				srfmfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.antes.30.dias.validacion")));
			}
		}else{
			srfmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.carga.cronograma.validacion")));
		}
		return null;
	}
	
	/**
	 * Método que consulta la carrera dependiendo del periodo seleccionado
	 */
	public void cambiarPeriodo(){
		try {
			srfmfListFichaMatriculaDtoBusq = servSrfmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraTodos(srfmfPeriodoAcademicoBuscar.getPracId(), srfmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
		} catch (FichaMatriculaException e) {
	//		e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	

	public void setearComboCausal(EstudianteJdbcDto itemMateria){
		
		itemMateria.setCslId(GeneralesConstantes.APP_ID_BASE);
		
		
	}
	
	/**
	 * Método que llama al reporte
	 */
	public void llamarReporte(){
		
		//GENERAR REPORTE
		 ReporteSolicitudRetiroFortuitoForm.generarReporteSolicitudRetiroFortuito(srfmfListaMateriasReporte, srfmfDependencia, srfmfCarrera, srfmfUsuario.getUsrPersona(), srfmfPeriodoActivo, srfmfUsuario, srfmfDirCarrera );
		 //ACTIVO MODAL PARA QUE SE ABRA EL REPORTE GENERADO Y DESCARGUE
		 srfmfActivadorReporte=1;
		 
		 srfmfDeshabilitaReportePdf= true;
	
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getSrfmfUsuario() {
		return srfmfUsuario;
	}
	public void setSrfmfUsuario(Usuario srfmfUsuario) {
		this.srfmfUsuario = srfmfUsuario;
	}
	public PeriodoAcademico getSrfmfPeriodoAcademicoBuscar() {
		return srfmfPeriodoAcademicoBuscar;
	}
	public void setSrfmfPeriodoAcademicoBuscar(PeriodoAcademico srfmfPeriodoAcademicoBuscar) {
		this.srfmfPeriodoAcademicoBuscar = srfmfPeriodoAcademicoBuscar;
	}
	public CarreraDto getSrfmfCarreraDtoBuscar() {
		return srfmfCarreraDtoBuscar;
	}
	public void setSrfmfCarreraDtoBuscar(CarreraDto srfmfCarreraDtoBuscar) {
		this.srfmfCarreraDtoBuscar = srfmfCarreraDtoBuscar;
	}
	public List<FichaMatriculaDto> getSrfmfListFichaMatriculaDtoBusq() {
		srfmfListFichaMatriculaDtoBusq = srfmfListFichaMatriculaDtoBusq==null?(new ArrayList<FichaMatriculaDto>()):srfmfListFichaMatriculaDtoBusq;
		return srfmfListFichaMatriculaDtoBusq;
	}
	public void setSrfmfListFichaMatriculaDtoBusq(List<FichaMatriculaDto> srfmfListFichaMatriculaDtoBusq) {
		this.srfmfListFichaMatriculaDtoBusq = srfmfListFichaMatriculaDtoBusq;
	}
	public List<CarreraDto> getSrfmfListCarreraDtoBusq() {
		srfmfListCarreraDtoBusq = srfmfListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):srfmfListCarreraDtoBusq;
		return srfmfListCarreraDtoBusq;
	}
	public void setSrfmfListCarreraDtoBusq(List<CarreraDto> srfmfListCarreraDtoBusq) {
		this.srfmfListCarreraDtoBusq = srfmfListCarreraDtoBusq;
	}
	public List<PeriodoAcademico> getSrfmfListPeriodoAcademicoBusq() {
		srfmfListPeriodoAcademicoBusq = srfmfListPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):srfmfListPeriodoAcademicoBusq;
		return srfmfListPeriodoAcademicoBusq;
	}
	public void setSrfmfListPeriodoAcademicoBusq(List<PeriodoAcademico> srfmfListPeriodoAcademicoBusq) {
		this.srfmfListPeriodoAcademicoBusq = srfmfListPeriodoAcademicoBusq;
	}
//	public List<FichaMatriculaDto> getSrfmfListFichaMatriculaDtoBusqCarr() {
//		return srfmfListFichaMatriculaDtoBusqCarr;
//	}
//	public void setSrfmfListFichaMatriculaDtoBusqCarr(List<FichaMatriculaDto> srfmfListFichaMatriculaDtoBusqCarr) {
//		this.srfmfListFichaMatriculaDtoBusqCarr = srfmfListFichaMatriculaDtoBusqCarr;
//	}
	public List<EstudianteJdbcDto> getSrfmfListEstudianteDto() {
		srfmfListEstudianteDto = srfmfListEstudianteDto==null?(new ArrayList<EstudianteJdbcDto>()):srfmfListEstudianteDto;
		return srfmfListEstudianteDto;
	}
	public void setSrfmfListEstudianteDto(List<EstudianteJdbcDto> srfmfListEstudianteDto) {
		this.srfmfListEstudianteDto = srfmfListEstudianteDto;
	}
	public FichaMatriculaDto getSrfmfFichaMatriculaDtoSeleccionado() {
		return srfmfFichaMatriculaDtoSeleccionado;
	}
	public void setSrfmfFichaMatriculaDtoSeleccionado(FichaMatriculaDto srfmfFichaMatriculaDtoSeleccionado) {
		this.srfmfFichaMatriculaDtoSeleccionado = srfmfFichaMatriculaDtoSeleccionado;
	}
	public Integer getSrfmfVisualizadorBotones() {
		return srfmfVisualizadorBotones;
	}
	public void setSrfmfVisualizadorBotones(Integer srfmfVisualizadorBotones) {
		this.srfmfVisualizadorBotones = srfmfVisualizadorBotones;
	}
	public FichaMatriculaDto getSrfmfFichaMatriculaEstado() {
		return srfmfFichaMatriculaEstado;
	}
	public void setSrfmfFichaMatriculaEstado(FichaMatriculaDto srfmfFichaMatriculaEstado) {
		this.srfmfFichaMatriculaEstado = srfmfFichaMatriculaEstado;
	}
	public List<EstudianteJdbcDto> getSrfmfListMatriculaEstado() {
		srfmfListMatriculaEstado = srfmfListMatriculaEstado==null?(new ArrayList<EstudianteJdbcDto>()):srfmfListMatriculaEstado;
		return srfmfListMatriculaEstado;
	}
	public void setSrfmfListMatriculaEstado(List<EstudianteJdbcDto> srfmfListMatriculaEstado) {
		this.srfmfListMatriculaEstado = srfmfListMatriculaEstado;
	}
	public Integer getSrfmfValidadorSeleccion() {
		return srfmfValidadorSeleccion;
	}
	public void setSrfmfValidadorSeleccion(Integer srfmfValidadorSeleccion) {
		this.srfmfValidadorSeleccion = srfmfValidadorSeleccion;
	}
	public Integer getSrfmfValidadorClic() {
		return srfmfValidadorClic;
	}
	public void setSrfmfValidadorClic(Integer srfmfValidadorClic) {
		this.srfmfValidadorClic = srfmfValidadorClic;
	}
	public Integer getSrfmftipoCronograma() {
		return srfmftipoCronograma;
	}
	public void setSrfmftipoCronograma(Integer srfmftipoCronograma) {
		this.srfmftipoCronograma = srfmftipoCronograma;
	}
	public CronogramaActividadJdbcDto getSrfmfCronogramaActividad() {
		return srfmfCronogramaActividad;
	}
	public void setSrfmfCronogramaActividad(CronogramaActividadJdbcDto srfmfCronogramaActividad) {
		this.srfmfCronogramaActividad = srfmfCronogramaActividad;
	}
	public String getSrfmfNombreArchivoAuxiliar() {
		return srfmfNombreArchivoAuxiliar;
	}
	public void setSrfmfNombreArchivoAuxiliar(String srfmfNombreArchivoAuxiliar) {
		this.srfmfNombreArchivoAuxiliar = srfmfNombreArchivoAuxiliar;
	}
	public String getSrfmfNombreArchivoSubido() {
		return srfmfNombreArchivoSubido;
	}
	public void setSrfmfNombreArchivoSubido(String srfmfNombreArchivoSubido) {
		this.srfmfNombreArchivoSubido = srfmfNombreArchivoSubido;
	}

	

	public int getSrfmfEstadoCambioDtmt() {
		return srfmfEstadoCambioDtmt;
	}

	public void setSrfmfEstadoCambioDtmt(int srfmfEstadoCambioDtmt) {
		this.srfmfEstadoCambioDtmt = srfmfEstadoCambioDtmt;
	}

	public int getSrfmfEstadoRecordEstudiante() {
		return srfmfEstadoRecordEstudiante;
	}

	public void setSrfmfEstadoRecordEstudiante(int srfmfEstadoRecordEstudiante) {
		this.srfmfEstadoRecordEstudiante = srfmfEstadoRecordEstudiante;
	}

	public List<Causal> getSrfmfListCausalRetiroFortuito() {
		return srfmfListCausalRetiroFortuito;
	}

	public void setSrfmfListCausalRetiroFortuito(List<Causal> srfmfListCausalRetiroFortuito) {
		this.srfmfListCausalRetiroFortuito = srfmfListCausalRetiroFortuito;
	}

	public PersonaDto getSrfmfDirCarrera() {
		return srfmfDirCarrera;
	}

	public void setSrfmfDirCarrera(PersonaDto srfmfDirCarrera) {
		this.srfmfDirCarrera = srfmfDirCarrera;
	}

	public Dependencia getSrfmfDependencia() {
		return srfmfDependencia;
	}

	public void setSrfmfDependencia(Dependencia srfmfDependencia) {
		this.srfmfDependencia = srfmfDependencia;
	}

	public PeriodoAcademico getSrfmfPeriodoActivo() {
		return srfmfPeriodoActivo;
	}

	public void setSrfmfPeriodoActivo(PeriodoAcademico srfmfPeriodoActivo) {
		this.srfmfPeriodoActivo = srfmfPeriodoActivo;
	}

	public List<EstudianteJdbcDto> getSrfmfListaGuardada() {
		srfmfListaGuardada = srfmfListaGuardada==null?(new ArrayList<EstudianteJdbcDto>()):srfmfListaGuardada;
		return srfmfListaGuardada;
	}

	public void setSrfmfListaGuardada(List<EstudianteJdbcDto> srfmfListaGuardada) {
		this.srfmfListaGuardada = srfmfListaGuardada;
	}

	public int getSrfmfActivadorReporte() {
		return srfmfActivadorReporte;
	}

	public void setSrfmfActivadorReporte(int srfmfActivadorReporte) {
		this.srfmfActivadorReporte = srfmfActivadorReporte;
	}

	public List<EstudianteJdbcDto> getSrfmfListaMateriasReporte() {
		srfmfListaMateriasReporte = srfmfListaMateriasReporte==null?(new ArrayList<EstudianteJdbcDto>()):srfmfListaMateriasReporte;
		return srfmfListaMateriasReporte;
	}

	public void setSrfmfListaMateriasReporte(List<EstudianteJdbcDto> srfmfListaMateriasReporte) {
		this.srfmfListaMateriasReporte = srfmfListaMateriasReporte;
	}

	public CarreraServicio getServSrfmfCarrera() {
		return servSrfmfCarrera;
	}

	public void setServSrfmfCarrera(CarreraServicio servSrfmfCarrera) {
		this.servSrfmfCarrera = servSrfmfCarrera;
	}

	public Carrera getSrfmfCarrera() {
		return srfmfCarrera;
	}

	public void setSrfmfCarrera(Carrera srfmfCarrera) {
		this.srfmfCarrera = srfmfCarrera;
	}

	public boolean isSrfmfDeshabilitaReportePdf() {
		return srfmfDeshabilitaReportePdf;
	}

	public void setSrfmfDeshabilitaReportePdf(boolean srfmfDeshabilitaReportePdf) {
		this.srfmfDeshabilitaReportePdf = srfmfDeshabilitaReportePdf;
	}

	public boolean isSrfmfDeshabilitaFinalizar() {
		return srfmfDeshabilitaFinalizar;
	}

	public void setSrfmfDeshabilitaFinalizar(boolean srfmfDeshabilitaFinalizar) {
		this.srfmfDeshabilitaFinalizar = srfmfDeshabilitaFinalizar;
	}


	
	
	
	
}
