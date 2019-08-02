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
   
 ARCHIVO:    AnulacionMatriculaForm.java	  
 DESCRIPCION: Managed Bean que maneja la anulación de la matricula del estudiantes por parte del secretario abogado de la facultad.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
10-01-2018			Marcelo Quishpe                        Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.CausalDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CausalDtoException;
import ec.edu.uce.academico.ejb.excepciones.CausalDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CausalException;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalDetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CausalDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoCausalConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;
import ec.edu.uce.academico.jpa.entidades.publico.CausalDetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
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
 * Clase (managed bean) RegistrarSegundaCarreraForm. Managed Bean que maneja la anulación de la matricula del estudiantes por parte del secretario abogado de la facultad.
 * 
 * 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "anulacionMatriculaForm")
@SessionScoped
public class AnulacionMatriculaForm implements Serializable {

	private static final long serialVersionUID = -397102404723196895L;

	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	// Objetos
	// PANTALLA LISTAR ESTUDIANTES
	private Usuario amfUsuario;
	private PersonaDto amfPersonaDtoBuscar; // Guardar la identificación
	private CarreraDto amfCarreraDtoBuscar; // Guardar la carrera
	private List<CarreraDto> amfListCarreraDto; // Combo a seleccionar carreras
	private PeriodoAcademico amfPeriodoBuscar;   //Periodo seleccionado a buscar
	private List<PeriodoAcademico> amfListaPeriodos;  //Lista de periodos
	private List<EstudianteJdbcDto> amfListaEstudiantes; //estudiante resultado de la busqueda
	
	//----Ver Matricula
	
	private EstudianteJdbcDto amfEstudianteSeleccionado;  //Estudiante seleccionado
	private List<EstudianteJdbcDto> amfListAsignaturasMatriculaEstud;  //Asignaturas
	private boolean amfVisualizadorBotones;  //desactiva opcion de anular
	private boolean amfActivadorReporte;  //activa reporte PDF.
	private String amfObservacionFinal;
	private List<EstudianteJdbcDto> amfListMateriasInactivas;
	private boolean amfRetiroMatriculaTotal;
	//private Integer amfValidadorSeleccion;
	
	//MODAL PARA CARGA DE ARCHIVO
	private String amfNombreArchivoAuxiliar;
	private String amfNombreArchivoSubido;
	private EstudianteJdbcDto amfAsignaturaSeleccionada;
	private Integer amfActivaModalCargarResolucion;
	private List<CausalDto> amfListCausalAnulacion ;
	private boolean amfRenderSelectCausales;
	private boolean amfDesactivaBotonSubirInfo;
		
	//GUARDAR FINAL
	private Integer amfActivaReporteFinal;
	private Integer amfActivaModalGuardarAnulacion;
	List<EstudianteJdbcDto> amfListaSeleccionadaPorGuardar;
	private String amfObservacionReporte;
	private boolean amfDesactivaBotonFinalizar;

	//--------------
	private EstudianteJdbcDto amfEstudianteEditar;  //NO SE USA
	private EstudianteJdbcDto amfEstudianteGuardar;

	private boolean amfBloqueaCamposAdicionales; 
	private PeriodoAcademico amfPeriodoAcademicoActivo;
	private List<InstitucionAcademicaDto> amfListUniversidadesDto;
	private List<TituloDto> amfListTitulosUniversidadDto;
	
	// *********************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	// *********************************************************************/
	@PostConstruct
	public void inicializar() {

	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	// Para la busqueda

	@EJB
	CarreraDtoServicioJdbc servAmfCarreraDto;
	@EJB
	PeriodoAcademicoServicio servAmfPeriodoAcademico;
	@EJB
	EstudianteDtoServicioJdbc servAmfEstudianteDtoJdbc;
	@EJB
	FichaEstudianteServicio servAmfFichaEstudianteServicio;
	@EJB
	CausalServicio servAmfCausalServicio;
	@EJB
	CausalDtoServicioJdbc servAmfCausalDtoServicioJdbc;
	@EJB
	MatriculaServicio servAmfMatriculaServicio;
	@EJB 
	DependenciaServicio servAmfDependencia;
	@EJB
	CausalDetalleMatriculaServicio servAmfCausalDetalleMatricula;


	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al
	 * iniciar la funcionalidad
	 * 
	 * @return navegacion al inicio.
	 */
	public String irInicio() {
		return "irInicio";
	}

	/**
	 * Método que permite iniciar el registro de anulación de matricula Sancion y adminitrativo por parte del secretario abogado.
	 * @param usuario   - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de busqueda de estudiante
	 */

	public String irAlistarEstudianteAnulacion(Usuario usuario) {
		amfUsuario = usuario;
		iniciarParametros();
		return "irAlistarEstudianteAnulacion";
	}


	/**
	 * Método para iniciar los parametros de la funcionalidad
	 */
	public void iniciarParametros() {
		try {

	//		amfListPersonas = new ArrayList<>();
			// Inicio los objetos
		     amfActivaModalCargarResolucion =0;
			 amfActivaModalGuardarAnulacion = 0;
			 amfActivaReporteFinal= 0;
			 amfRenderSelectCausales  = false;
			 amfDesactivaBotonFinalizar = false;
			
			amfPersonaDtoBuscar = new PersonaDto();
			amfPersonaDtoBuscar.setPrsIdentificacion("");
			amfCarreraDtoBuscar = new CarreraDto();
			// seteo la carrera para que busque por todas
			amfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
			// seteo el periodo a buscar todos
			amfPeriodoBuscar = new PeriodoAcademico();
			amfPeriodoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
			// inicio la lista de Personas
			
			amfListCarreraDto = null;
			amfListaEstudiantes= null;
			amfVisualizadorBotones = true;
			amfListaPeriodos= new ArrayList<>();
			
			// busco el periodo academico activo
			amfPeriodoAcademicoActivo = servAmfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			// lleno las carreras/
			// amfListCarreraDto=servAmfCarreraDto.listarXfacultad(DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE);
			amfListCarreraDto = servAmfCarreraDto.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPregrado(
					amfUsuario.getUsrId(), RolConstantes.ROL_SECREABOGADO_VALUE,
					RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, amfPeriodoAcademicoActivo.getPracId());
			
			List<PeriodoAcademico> listPeriodoTodosAux = servAmfPeriodoAcademico.listarTodosPreGradoOrdenadosXId();
			amfListaPeriodos.add(listPeriodoTodosAux.get(0));  //solo el periodo activo
		//	amfListaPeriodos.add(listPeriodoTodosAux.get(1));  //1 periodo anterior por quitar  MQ:17 abril 2019
		//	amfListaPeriodos.add(listPeriodoTodosAux.get(2)); // dos periodos anteriores por quitar MQ:6 mar 2019
				
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.iniciar.parametros.periodo.academico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}

	}

	/**
	 * Método para buscar las personas con los parámetros ingresados en los
	 * filtros de busqueda
	 */
	public void buscar() {
		// buscar elestudiante por Identificación y carrera
	
		amfListaEstudiantes= new ArrayList<>(); 
			
			try {
				if((amfPersonaDtoBuscar.getPrsIdentificacion()!=null)&&(amfPersonaDtoBuscar.getPrsIdentificacion().trim().length() >=1)){
					
					amfListaEstudiantes = servAmfEstudianteDtoJdbc.buscarEstudianteXIdentificacionXCarreraXPeriodo(amfPersonaDtoBuscar.getPrsIdentificacion().trim(), amfCarreraDtoBuscar.getCrrId(), amfPeriodoBuscar.getPracId(), amfListCarreraDto, amfListaPeriodos);
				
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarSegundaCarrera.buscar.sin.identificacion.validacion.exception")));
				}
				
			} catch (EstudianteDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (EstudianteDtoJdbcNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		
	}


	
	/**
	 * Método que permite ir a la visualización de la matricula (materias y demas datos)
	 * @param fichaMatriculaDto - fichaMatriculaDto objeto matricula del estudiante que envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerMatricula(EstudianteJdbcDto estudianteDto){
		String retorno = null;
		amfActivaModalCargarResolucion = 0; 
		 amfActivaModalGuardarAnulacion = 0;
		 amfActivaReporteFinal= 0;
		 amfRenderSelectCausales  = false;
		 amfDesactivaBotonFinalizar = false;
		amfAsignaturaSeleccionada = new EstudianteJdbcDto();
		
//		srfmfActivadorReporte=0;      //Modal descargar Pdf al presionar GenerarPDF
//		srfmfDeshabilitaReportePdf= true; //Boton GenerarPDF

		try {
			
	
			amfEstudianteSeleccionado = estudianteDto;
		
			// LLENO LA LISTA DE ASIGNATURAS INACTIVAS  (APROBADAS SOLICITUD DE RETIRO Y RETIRO FORTUITO)
			amfListMateriasInactivas = servAmfEstudianteDtoJdbc.buscarEstudianteMateriaInactivasRetiroAprobadoXIdEstudianteXIdMatriculaXidPeriodoxEstados(
							amfEstudianteSeleccionado.getPrsId(), amfEstudianteSeleccionado.getFcmtId(),
							amfEstudianteSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE,GeneralesConstantes.APP_ID_BASE);
			
            //LISTA DE TODAS LAS ASIGNATURAS EN LA MATRICULA, RETIRADOS O NO			
			amfListAsignaturasMatriculaEstud = servAmfEstudianteDtoJdbc.buscarAsignaturasMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(amfEstudianteSeleccionado.getPrsId(),amfEstudianteSeleccionado.getFcmtId() , amfEstudianteSeleccionado.getPracId());
			
			if(amfListAsignaturasMatriculaEstud.size() > 0){
				Boolean verificar = true;
				for (EstudianteJdbcDto item : amfListAsignaturasMatriculaEstud) {
					
					//Verifico si esta inscrito
					if(item.getRcesEstado() == RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE){
						verificar = false;
						break;
					}
					
					if(item.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE
						||	item.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_VALUE){
						
						item.setVisualizador(true);//Si ya esta anulada, deshabilito link anular
						
					}else{
						item.setVisualizador(false); // habilito link anular
						
					}
				
					//busco si la asignatura tiene lista de causales de anulacion
					List<CausalDetalleMatricula> listaCausalesAnulacionAux = servAmfCausalDetalleMatricula.listarxdtmtId(item.getDtmtId());
					
					//Si tiene las coloco en un campo para que se visualice al ingresar a ver las asignaturas de la matricula
					if(listaCausalesAnulacionAux!=null&&listaCausalesAnulacionAux.size()>0){

						StringBuilder causalesSeleccionadosAux = new StringBuilder();
						   for (CausalDetalleMatricula causaldetallaMatricula : listaCausalesAnulacionAux) {
							   Causal causalAux = servAmfCausalServicio.buscarPorId(causaldetallaMatricula.getCsdtmtCausal().getCslId());
								   causalesSeleccionadosAux.append(" ");
								   causalesSeleccionadosAux.append(causalAux.getCslCodigo());
						    }
						  
						   item.setCausalesSeleccionados(causalesSeleccionadosAux.toString());
						
					}
					
				}
				
				if(verificar){
					//amfVisualizadorBotones = false;
					retorno = "irVerMatriculaAnular";
				}else{
					//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("SolicitudRetiroFortuito.matricula.ir.ver..matricula.legalizada.validacion")));
					FacesUtil.mensajeError("No puede generar la anulación de asignaturas, no se encuentra legalizada la matrícula.");
					return null;
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.matricula.ver.matricula.no.hay.datos")));
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.matricula.ver.matricula.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.matricula.ver.matricula.no.encontrado.exception")));
		} catch (CausalNoEncontradoException e) {
			//	e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CausalException e) {
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 

		return retorno;
	}
	
	
	
	
	// HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton guardar para verificar que cumpla lasreglas antes de presentar el modal guardar
	 * @return retora null para para cualquier cosa
	 */
	public String controlarClickGuardarAnulacion() {
		Boolean existeSelecionado = false;
		// Boolean dentroCronograma= false;
		
		amfActivaModalGuardarAnulacion = 0;

		// VERIFICO QUE EXISTA EL DIRECTORIO PARA GUARDAR EL PDF
		String pathDirGuardar = GeneralesConstantes.APP_PATH_ARCHIVOS+ GeneralesConstantes.APP_PATH_ARCHIVOS_ANULACION_MATRICULA;

		File directorio = new File(pathDirGuardar);
		boolean existeDirectorio = true;
		if (!directorio.exists()) {

			existeDirectorio = false;
		}

		// VERICO QUE LAS ASIGNATURAS
	if (amfListAsignaturasMatriculaEstud.size() >= 1) {

		//			// VERICO QUE LAS ASIGNATURAS ESTEN RESUELTAS
//			for (EstudianteJdbcDto materia : amfListAsignaturasMatriculaEstud) {
//				if ((materia.getDtmtTipoAnulacion()== DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE) 
//					||	(materia.getDtmtTipoAnulacion()== DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_VALUE)	)	{
//					existeSelecionado = true;
//				} 
//			}

		
			amfListaSeleccionadaPorGuardar = new ArrayList<>();
			
			for (EstudianteJdbcDto estudianteJdbcDto : amfListAsignaturasMatriculaEstud) {
				
				if((estudianteJdbcDto.isVisualizador()==false)&&(estudianteJdbcDto.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE||
				    estudianteJdbcDto.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_VALUE)){
					amfListaSeleccionadaPorGuardar.add(estudianteJdbcDto);
					
				}
			}

			if(amfListaSeleccionadaPorGuardar.size()>0){
				existeSelecionado = true;
			}
				
			

			if (existeDirectorio) {

				if (existeSelecionado) {
					
						if ((getAmfObservacionFinal() != null) && (getAmfObservacionFinal().length() >= 1)) {
							amfActivaModalGuardarAnulacion = 1;
						
						} else {
							amfActivaModalGuardarAnulacion = 0;
							FacesUtil.limpiarMensaje();
						//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.observacion.final.validacion.exception")));
					FacesUtil.mensajeError("Ingrese la observación");
						}

				} else {
					amfActivaModalGuardarAnulacion = 0;
					FacesUtil.limpiarMensaje();
				//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.seleccionar.todos.validacion.exception")));
					FacesUtil.mensajeError("No existe anulación generada  a guardar");
				}
			

			} else {

				amfActivaModalGuardarAnulacion = 0;
				FacesUtil.limpiarMensaje();
				//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.controlarClickGuardarResolver.directorio.guardar.archivo.validacion.exception")));
			 FacesUtil.mensajeError("No se encontro el diretorio para realizar el guardado del archivo de resolución.");

			}

		} else {
			amfActivaModalGuardarAnulacion = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.controlarClickGuardarAnulacion.no.existe.materias.validacion.exception")));
		}

		
	
		
		// número de materias solicitadas para aprobar, en estado aprobado
		int contadorListaEstudiante = 0;
		for (EstudianteJdbcDto materiaAux : amfListaSeleccionadaPorGuardar) {
			if (materiaAux.getDtmtEstado() == DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE) { // me cuenta siempre y cuando  este activo al anular pasarian a incativos
				contadorListaEstudiante = contadorListaEstudiante + 1;
			}
		}
		
		// numero de materias aprobadas el retiro
		int contadorListMatAproRetiro = 0;
		for (EstudianteJdbcDto materiaRetirada : amfListMateriasInactivas) {
			contadorListMatAproRetiro = contadorListMatAproRetiro + 1;
		}
		
		// numero total de materias que esta matriculado
		int contadorListMatTotal = 0;
		for (EstudianteJdbcDto materiaTotal : amfListAsignaturasMatriculaEstud) {
			contadorListMatTotal = contadorListMatTotal + 1;
		}

		// VERICACION SI HACE EL RETIRO EN TODAS LAS MATERIAS - PARA RETIRAR O desactivar LA FICHA MATRICULA

		if (amfListAsignaturasMatriculaEstud != null && amfListaSeleccionadaPorGuardar != null
				&& amfListMateriasInactivas != null) {
			if (contadorListMatTotal == (contadorListaEstudiante + contadorListMatAproRetiro)) { // si el total de la suma de materias solicitadas mas las materias inactivas aprobadas solicitud
				amfRetiroMatriculaTotal = true;
			} else {
				amfRetiroMatriculaTotal = false;
			}
		}

		return null;
	}

	
	/**
	 * Método que guarda la respuesta a la solicitud de retiro de las materias
	 * 
	 * @return retorna - la navegación de la página listar estudiantes.
	 */
	public void  guardarResolver() {

	//	String retorno = null;
		// asrffFichaInscripcion= new FichaInscripcionDto();
		//amfListaMateriasReporte = new ArrayList<>();

	//	if (amfListAsignaturasMatriculaEstud != null && amfListAsignaturasMatriculaEstud.size() > 0) { // verifica que la lista no este nula proceso de transacción
			String rutaNombre = null;
			String rutaTemporal = null;
			
			try {
				// FALTA COLOCAR UNA VARIABLE EN LUGAR DEL FALSE, cuando se
				// retira de todas las materias
				
					if (servAmfMatriculaServicio.guardarAnulacionMatricula(amfListaSeleccionadaPorGuardar,amfRetiroMatriculaTotal, amfEstudianteSeleccionado.getFcmtId(), amfUsuario, amfObservacionFinal)) {
					// Si grabo bien en base procedo a grabar los archivos pdf
					// desde el cache de la maquina al servidor de archivos.
						
					for (EstudianteJdbcDto item : amfListaSeleccionadaPorGuardar) {
						String extension = GeneralesUtilidades.obtenerExtension(item.getDtmtArchivoAnulacion());
						rutaNombre = DetalleMatriculaConstantes.DTMT_ARCHIVO_RESOLUCION_ANULACION_LABEL + "-"	+ item.getDtmtId() + "." + extension;
						rutaTemporal = System.getProperty("java.io.tmpdir") + File.separator+ item.getDtmtArchivoAnulacion();
						GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS+ GeneralesConstantes.APP_PATH_ARCHIVOS_ANULACION_MATRICULA + rutaNombre);
						rutaNombre = null;
						rutaTemporal = null;
					}

					// copio las lista para el reporte PDF
//					for (EstudianteJdbcDto solicitudDto : asrffListaMateriasSolicitadasDto) {
//						asrffListaMateriasReporte.add(solicitudDto);
//					}

					
					Dependencia dependenciaAux=servAmfDependencia.buscarDependenciaXcrrId(amfEstudianteSeleccionado.getCrrId());
					
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

						if (amfEstudianteSeleccionado != null) {
							frmRrmNombreReporte = amfEstudianteSeleccionado.getPrsNombres() + " "
									+ amfEstudianteSeleccionado.getPrsPrimerApellido().toUpperCase() + " "
									+ (amfEstudianteSeleccionado.getPrsSegundoApellido() == null ? " "
											: amfEstudianteSeleccionado.getPrsSegundoApellido());
						} else {
							frmRrmNombreReporte = "Informe anulación matrícula";
						}

						frmRrmParametros = new HashMap<String, Object>();
						SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy",new Locale("es", "ES"));
						// SimpleDateFormat formato = new SimpleDateFormat("d
						// 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
						String fecha = formato.format(new Date());
						frmRrmParametros.put("fecha", fecha);

						String nombres = null;
						if (amfEstudianteSeleccionado != null) {
							nombres = amfEstudianteSeleccionado.getPrsNombres() + " "
									+ amfEstudianteSeleccionado.getPrsPrimerApellido().toUpperCase() + " "
									+ (amfEstudianteSeleccionado.getPrsSegundoApellido() == null ? " "
											: amfEstudianteSeleccionado.getPrsSegundoApellido());
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
						if (amfEstudianteSeleccionado.getCrrDescripcion() != null) {
							sbTextoInicial.append(amfEstudianteSeleccionado.getCrrDescripcion());
							sbTextoInicial.append("\n");
						} else {
							sbTextoInicial.append(" ");
							sbTextoInicial.append("\n");
						}

						sbTextoInicial.append("FACULTAD DE ");
						if (dependenciaAux.getDpnDescripcion() != null) {
							sbTextoInicial.append(dependenciaAux.getDpnDescripcion());
							sbTextoInicial.append("\n");
						} else {
							sbTextoInicial.append(" ");
							sbTextoInicial.append("\n");
						}
						sbTextoInicial.append("Presente.-");
						sbTextoInicial.append("\n\n");
						frmRrmParametros.put("textoInicial", sbTextoInicial.toString());

						StringBuilder sbTexto = new StringBuilder();
						sbTexto.append("Una vez analizada y validada la documentación respectiva sobre el pedido de anulación de matrícula y considerando la normativa vigente; "
								+ "a continuación, se detalla el informe de acuerdo a la resolución del Consejo Directivo de la Facultad:");
						frmRrmParametros.put("texto", sbTexto.toString());

						StringBuilder sbPeriodo = new StringBuilder();
						StringBuilder sbCodigo = new StringBuilder();
						StringBuilder sbAsignatura = new StringBuilder();
						StringBuilder sbHora = new StringBuilder();
						StringBuilder sbCausal = new StringBuilder();
					//	StringBuilder sbEvidencia = new StringBuilder();
						StringBuilder sbEstado = new StringBuilder();

						for (EstudianteJdbcDto item : amfListaSeleccionadaPorGuardar) {
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

								Integer numItems = 0;
								if(item.getListaCausalesDto().size()>0){
                                 for (CausalDto causalDto : item.getListaCausalesDto()) {
                            	    numItems++;
                            	     if(numItems<item.getListaCausalesDto().size()){
                            	       sbCausal.append(causalDto.getCslCodigo()+", ");
                            	      }else{
                            		    sbCausal.append(causalDto.getCslCodigo()+" ");  
                            	       }
							     }
                                 sbCausal.append("\n\n");
								}else{
									sbCausal.append(" ");
									sbCausal.append("\n\n");
									
								}
								
								
								
								if (item.getDtmtTipoAnulacion() != null) {
									if (item.getDtmtTipoAnulacion() == 1) {
										sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL);
										sbEstado.append("\n\n");
									}
									if (item.getDtmtTipoAnulacion() == 2) {
										sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL);
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

								Integer numItems = 0;
								if(item.getListaCausalesDto().size()>0){
                                 for (CausalDto causalDto : item.getListaCausalesDto()) {
                            	    numItems++;
                            	     if(numItems<item.getListaCausalesDto().size()){
                            	       sbCausal.append(causalDto.getCslCodigo()+", ");
                            	      }else{
                            		    sbCausal.append(causalDto.getCslCodigo()+" ");  
                            	       }
							     }
                                 sbCausal.append("\n\n\n");
								}else{
									sbCausal.append(" ");
									sbCausal.append("\n\n\n");
									
								}

					
								if (item.getDtmtTipoAnulacion() != null) {
									if (item.getDtmtTipoAnulacion() != null) {
										if (item.getDtmtTipoAnulacion() == 1) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL);
											sbEstado.append("\n\n\n");
										}
										if (item.getDtmtTipoAnulacion() == 2) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL);
											sbEstado.append("\n\n\n");
										}

									} else {
										sbEstado.append(" ");
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

								Integer numItems = 0;
								if(item.getListaCausalesDto().size()>0){
                                 for (CausalDto causalDto : item.getListaCausalesDto()) {
                            	    numItems++;
                            	     if(numItems<item.getListaCausalesDto().size()){
                            	       sbCausal.append(causalDto.getCslCodigo()+", ");
                            	      }else{
                            		    sbCausal.append(causalDto.getCslCodigo()+" ");  
                            	       }
							     }
                                 sbCausal.append("\n\n\n\n");
								}else{
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n");
									
								}

								if (item.getDtmtTipoAnulacion() != null) {
									if (item.getDtmtTipoAnulacion() != null) {
										if (item.getDtmtTipoAnulacion() == 1) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL);
											sbEstado.append("\n\n\n\n");
										}
										if (item.getDtmtTipoAnulacion() == 2) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL);
											sbEstado.append("\n\n\n\n");
										}

									} else {
										sbEstado.append(" ");
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

								Integer numItems = 0;
								if(item.getListaCausalesDto().size()>0){
                                 for (CausalDto causalDto : item.getListaCausalesDto()) {
                            	    numItems++;
                            	     if(numItems<item.getListaCausalesDto().size()){
                            	       sbCausal.append(causalDto.getCslCodigo()+", ");
                            	      }else{
                            		    sbCausal.append(causalDto.getCslCodigo()+" ");  
                            	       }
							     }
                                 sbCausal.append("\n\n\n\n\n");
								}else{
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n");
									
								}
								
								if (item.getDtmtTipoAnulacion() != null) {
									if (item.getDtmtTipoAnulacion() != null) {
										if (item.getDtmtTipoAnulacion() == 1) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL);
											sbEstado.append("\n\n\n\n\n");
										}
										if (item.getDtmtTipoAnulacion() == 2) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL);
											sbEstado.append("\n\n\n\n\n");
										}

									} else {
										sbEstado.append(" ");
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

								Integer numItems = 0;
								if(item.getListaCausalesDto().size()>0){
                                 for (CausalDto causalDto : item.getListaCausalesDto()) {
                            	    numItems++;
                            	     if(numItems<item.getListaCausalesDto().size()){
                            	       sbCausal.append(causalDto.getCslCodigo()+", ");
                            	      }else{
                            		    sbCausal.append(causalDto.getCslCodigo()+" ");  
                            	       }
							     }
                                 sbCausal.append("\n\n\n\n\n\n");
								}else{
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n\n");
									
								}

								if (item.getDtmtTipoAnulacion() != null) {
									if (item.getDtmtTipoAnulacion() != null) {
										if (item.getDtmtTipoAnulacion() == 1) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL);
											sbEstado.append("\n\n\n\n\n\n");
										}
										if (item.getDtmtTipoAnulacion() == 2) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL);
											sbEstado.append("\n\n\n\n\n\n");
										}

									} else {
										sbEstado.append(" ");
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

								Integer numItems = 0;
								if(item.getListaCausalesDto().size()>0){
                                 for (CausalDto causalDto : item.getListaCausalesDto()) {
                            	    numItems++;
                            	     if(numItems<item.getListaCausalesDto().size()){
                            	       sbCausal.append(causalDto.getCslCodigo()+", ");
                            	      }else{
                            		    sbCausal.append(causalDto.getCslCodigo()+" ");  
                            	       }
							     }
                                 sbCausal.append("\n\n\n\n\n\n\n");
								}else{
									sbCausal.append(" ");
									sbCausal.append("\n\n\n\n\n\n\n");
									
								}

								if (item.getDtmtTipoAnulacion() != null) {
									if (item.getDtmtTipoAnulacion() != null) {
										if (item.getDtmtTipoAnulacion() == 1) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_LABEL);
											sbEstado.append("\n\n\n\n\n\n\n");
										}
										if (item.getDtmtTipoAnulacion() == 2) {
											sbEstado.append(DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL);
											sbEstado.append("\n\n\n\n\n\n\n");
										}

									} else {
										sbEstado.append(" ");
										sbEstado.append("\n\n\n\n\n\n\n");
									}

								} else {
									sbEstado.append(" ");
									sbEstado.append("\n\n\n\n\n\n\n");
								}
							}
							frmRrmParametros.put("periodo", sbPeriodo.toString());
							frmRrmParametros.put("cod_asignatura", sbCodigo.toString());
							frmRrmParametros.put("asignatura", sbAsignatura.toString());
							frmRrmParametros.put("numero", sbHora.toString());
							frmRrmParametros.put("causal", sbCausal.toString());
						//	frmRrmParametros.put("evidencia", sbEvidencia.toString());
							frmRrmParametros.put("solicitudEstado", sbEstado.toString());

							String secreAbogado = null;

							if (amfUsuario != null) {
								secreAbogado = amfUsuario.getUsrPersona().getPrsNombres() + " "
										+ amfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase() + " "
										+ (amfUsuario.getUsrPersona().getPrsSegundoApellido() == null ? " "
												: amfUsuario.getUsrPersona().getPrsSegundoApellido());
							} else {
								secreAbogado = " ";

							}
							frmRrmParametros.put("secreAbogado", secreAbogado);

							StringBuilder sbObservaciones = new StringBuilder();

							if (amfObservacionFinal != null) {
								sbObservaciones.append("Observaciones: ");
								sbObservaciones.append("\n");
								sbObservaciones.append(amfObservacionFinal);
							} else {
								sbObservaciones.append("Observaciones: ");
								sbObservaciones.append("\n");

							}
							frmRrmParametros.put("observaciones", sbObservaciones.toString());

							if (amfUsuario.getUsrNick() != null) {
								frmRrmParametros.put("nick", amfUsuario.getUsrNick());
							} else {
								frmRrmParametros.put("nick", " ");
							}

						
						for (CausalDto causalDto: item.getListaCausalesDto()){
							Boolean encontrado = false;
							for (Causal causal : listaCausalaux) {
								if (causal.getCslCodigo().equals(causalDto.getCslCodigo())) {
									encontrado = true;
									break;
								}
							}
							
							if (encontrado == false) {
								Causal objetoCausal = new Causal();
								objetoCausal.setCslCodigo(causalDto.getCslCodigo());
								objetoCausal.setCslDescripcion(causalDto.getCslDescripcion());
								listaCausalaux.add(objetoCausal);
							}
							
							
							
						}
						
					}//fin for lista de asignaturas
					
					//	StringBuilder sbCslCodigo = new StringBuilder();
						
					if(listaCausalaux.size()>0)	{
						StringBuilder sbCslDescripcion = new StringBuilder();
						for (Causal causal2 : listaCausalaux) {

//							if (causal2.getCslCodigo() != null) {
//								sbCslCodigo.append(causal2.getCslCodigo());
//								sbCslCodigo.append("\n");
//							} else {
//								sbCslCodigo.append(" ");
//								sbCslCodigo.append("\n");
//							}

							if (causal2.getCslDescripcion() != null) {
								sbCslDescripcion.append(causal2.getCslCodigo()+":  ");
								sbCslDescripcion.append(causal2.getCslDescripcion());
								sbCslDescripcion.append("\n");
							} else {
								sbCslDescripcion.append(" ");
								sbCslDescripcion.append("\n");

							}

							//frmRrmParametros.put("cslCodigo", sbCslCodigo.toString());
							frmRrmParametros.put("cslDescripcion", sbCslDescripcion.toString());
						}
					}else{
						frmRrmParametros.put("cslDescripcion", " ");
						
					}
						StringBuilder pathGeneralReportes = new StringBuilder();
						pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
						pathGeneralReportes
								.append("/academico/reportes/archivosJasper/reporteAnulacionMatricula");
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

						jasperReport = (JasperReport) JRLoader.loadObject(new File((pathGeneralReportes.toString() + "/reporteSecreAbogado.jasper")));

						jasperPrint = JasperFillManager.fillReport(jasperReport, frmRrmParametros,
								new JREmptyDataSource()); // llena el reporte

						byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);

						// lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(amfEstudianteSeleccionado.getPrsMailInstitucional());
						// path de la plantilla del mail
						ProductorMailJson pmail = null;
						StringBuilder sbCorreo = new StringBuilder();
						formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss",
								new Locale("es", "ES"));
						fecha = formato.format(new Date());
						sbCorreo = GeneralesUtilidades.generarAsuntoAnulacionMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()), nombres,
								GeneralesUtilidades.generaStringParaCorreo(amfEstudianteSeleccionado.getCrrDescripcion()));
						pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,
								GeneralesConstantes.APP_ASUNTO_INFORME_ANULACION_MATRICULA,
								sbCorreo.toString(), "admin", "dt1c201s", true, arreglo,
								"InformeAnulacionMatricula." + MailDtoConstantes.TIPO_PDF,
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

						 amfActivaReporteFinal = 1;

					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
//					 ******************************************************************************
//					 *********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//					 ******************************************************************************

//					asrffDesactivaBotonReporte = Boolean.FALSE; // activo boton  generar PDF
					amfActivaReporteFinal = 1;
					amfDesactivaBotonFinalizar = true;
					amfListaSeleccionadaPorGuardar = null;
				//	amfListAsignaturasMatriculaEstud= null;
                   setAmfObservacionReporte(getAmfObservacionFinal());//
					// necesito guardarme el valor en otra variable antes de que se anule la variable y se refresque la pantalla al guardar la solicitudes
					amfActivaModalGuardarAnulacion = 0;
					
					// deshabilito link anular	en todas las asignaturas para refrescar la vista de asignaturas.
					for (EstudianteJdbcDto item : amfListAsignaturasMatriculaEstud) {
						  item.setVisualizador(true);// deshabilito link anular	en todas las asignaturas						
						
					}
					
			//		iniciarParametros();
			//		retorno ="irAlistarEstudianteAnulacion";
					
					FacesUtil.limpiarMensaje();
				//	FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudRetiroFortuito.guardarResolver.validacion.con.exito")));
					FacesUtil.mensajeInfo("Se ha guardado la anulación con exito ");

				} else {
					amfActivaModalGuardarAnulacion= 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.guardarAnulacion.validacion.sin.exito")));
				}

			} catch (Exception e) {
				amfActivaModalGuardarAnulacion = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.guardarAnulacion.exception")));
			}

		// SETEO EL CLIC PARA QUE NO SE EJECUTE NUEVAMENTE EL MODAL
			amfActivaModalGuardarAnulacion = 0;
			
		// VACIO LA LISTA DE SOLICITUDES

		setAmfObservacionReporte(getAmfObservacionFinal());// necesito guardarme elvalor en otra variableantes de quese anule la variable y se refresque la pantalla al guardar la solicitudes
		amfObservacionFinal = null;
		//amfListaSeleccionadaPorGuardar = null;
		//amfListAsignaturasMatriculaEstud= null;
		
			//return retorno;

	}


	
	/**
	 * Método que llama al reporte
	 */
	public void llamarReporte() {

//		// GENERAR REPORTE
//		ReporteAprobacionRetiroFortuitoForm.generarReporteAprobacionRetiroFortuito(asrffListaMateriasReporte,asrffCarreraVer, amfEstudianteSeleccionado, asrffPeriodoAcademicoActivo, asrffUsuario,
//				asrffObservacionReporte);
//		// ACTIVO MODAL PARA QUE SE ABRA EL REPORTE GENERADO Y DESCARGUE
//		asrffActivaReporteFinal = 1;

	}
	
	
	public void habilitarCausales(int tipoAnulacion){
		
		for (CausalDto causalDto : amfListCausalAnulacion) {
			causalDto.setSeleccionado(false);
		}
		
		if(tipoAnulacion==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE){
			
			amfRenderSelectCausales  = true;
			
		}else  if(tipoAnulacion==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_VALUE) {
			
			amfRenderSelectCausales  = false;
			
		}else {
			amfRenderSelectCausales  = false;
			FacesUtil.limpiarMensaje();
            FacesUtil.mensajeError("mensaje","Debe selecccionar un tipo de anulación");
			
		}
		
		
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	/**
	 * Método para limpiar
	 */
	public void limpiar(){
		iniciarParametros();
		
	}
	

		
		/**
		 * Método para cancelar el el ingresi de información de segunda carrera
		 * @return navegación a la pagina de listar estudiantes a homologar.
		 */
		public String cancelarAnulacion() {
			iniciarParametros();
			return "irAlistarEstudianteAnulacion";
		}	
	
		
	
		
		//CARGA DE ARCHIVO
		/**
		 * Método para cargar el archivo en ruta temporal
		 * @param event - event archivo oficio que presenta el estudiante para anular matrícula
		 */
		public void handleFileUpload(FileUploadEvent archivo) {
			amfNombreArchivoSubido = archivo.getFile().getFileName();
			amfNombreArchivoAuxiliar = archivo.getFile().getFileName();
			String rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + amfNombreArchivoSubido;
			try {
				GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(),	rutaTemporal);
				archivo.getFile().getInputstream().close();
			} catch (IOException ioe) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Anulacion.matricula.handleFileUpload.carga.archivo.exception")));
			}
		}
		
		
		
		
		/**
		 * Método para activar el modal de carga de resolucion
		 * @param materia   - materia que se selecciona de la lista para cargar resolucion
		 */
		public void activaModalSubirInformacion(EstudianteJdbcDto materia) {
			
			amfAsignaturaSeleccionada= new EstudianteJdbcDto();
			//Coloco el objeto sleccionado de la lista en otro objeto para presentar los datos hasta grabar
			amfAsignaturaSeleccionada.setMtrId(materia.getMtrId());
			amfAsignaturaSeleccionada.setMtrCodigo(materia.getMtrCodigo());
			amfAsignaturaSeleccionada.setMtrDescripcion(materia.getMtrDescripcion());
			amfAsignaturaSeleccionada.setDtmtTipoAnulacion(GeneralesConstantes.APP_ID_BASE);
			amfAsignaturaSeleccionada.setCausalesSeleccionados(" ");
		
			amfDesactivaBotonSubirInfo = false;
			amfRenderSelectCausales  = false;
			amfActivaModalCargarResolucion = 1;  //activo modal para subir Informacion de anulacion
			
			amfListCausalAnulacion = new ArrayList<>();			
			
			//amfClickGuardarResolver = 0; // desactiva modal guardar resolucion apelacion
			amfDesactivaBotonSubirInfo = false;
							
				try {
					amfListCausalAnulacion = servAmfCausalDtoServicioJdbc.listarXidTipoCausal(TipoCausalConstantes.TIPO_CAUSAL_ANULACION_ASIGNATURA_VALUE);
				} catch (CausalDtoException e) {
	
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("mensaje","Error desconocido al buscar causales por tipo de causal para la anulación de matrícula");
					
				} catch (CausalDtoNoEncontradoException e) {
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("mensaje","No se encontró  causales al buscar por tipo de causal para la anulación de matrícula");
				}
							
		}
		
		
		/**
		 * Método para cerrar la ventana de subir archivo
		 */

		public void CancelarSubirArchivo() {
			amfAsignaturaSeleccionada = null;
			amfActivaModalCargarResolucion = 0;
			amfDesactivaBotonSubirInfo = false;
			amfNombreArchivoAuxiliar = null;
			amfNombreArchivoSubido = null;

		}

		
		
		/**
		 * Método para guardar el archivo y la causal en el item
		 * @param materia   -materia que se selecciona de la lista para cargar causal y la  solicitud
		 */

		public void guardarSubirInformacion(EstudianteJdbcDto materia, List<CausalDto> listaCausales) {
			
			 boolean existeCausal = false;
			 if(materia.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE){
			      for (CausalDto causalDto : listaCausales) {
				     if(causalDto.getSeleccionado()){
					    existeCausal =true;//  Si es sanción debe seleccionar al menos un causal.
				        }
			        }
			
			    }else{
			    	  existeCausal =true; //Administrativo no necesita causales
			    	
			    }
			 
			 
			if(materia.getDtmtTipoAnulacion()!=GeneralesConstantes.APP_ID_BASE){
			if (amfNombreArchivoSubido != null) { // verifico que se haya cargadoun archivo
				
			if(existeCausal)	{
				// Buscamos la materia en la lista de materias para guardar los valores								
				for (EstudianteJdbcDto itemMtr :amfListAsignaturasMatriculaEstud) {
					if (itemMtr.getMtrId() == materia.getMtrId()) {
						
						itemMtr.setDtmtTipoAnulacion(materia.getDtmtTipoAnulacion());
						// Guardamos el nombre del archivo en el objeto
						itemMtr.setDtmtArchivoAnulacion(amfNombreArchivoSubido);
						
						itemMtr.setListaCausalesDto(new ArrayList<CausalDto>());
						
					if(materia.getDtmtTipoAnulacion()==DetalleMatriculaConstantes.DTMT_TIPO_ANULACION_SANCION_VALUE){
						StringBuilder causalesSeleccionadosAux = new StringBuilder();
						   for (CausalDto causalDto : listaCausales) {
							    if(causalDto.getSeleccionado()){
								    itemMtr.getListaCausalesDto().add(causalDto);
								    causalesSeleccionadosAux.append(" ");
								   causalesSeleccionadosAux.append(causalDto.getCslCodigo());
							     }
						    }
						  itemMtr.setCausalesSeleccionados(causalesSeleccionadosAux.toString());
				     	
						
					}else {
							itemMtr.getListaCausalesDto().clear();
							itemMtr.setCausalesSeleccionados(" ");
						}
						break;
					}
				}
				
				amfNombreArchivoAuxiliar = null;
				amfNombreArchivoSubido = null;
				amfAsignaturaSeleccionada = new EstudianteJdbcDto();
				amfAsignaturaSeleccionada.setDtmtTipoAnulacion(GeneralesConstantes.APP_ID_BASE);
			     amfDesactivaBotonSubirInfo = true;
			     amfActivaModalCargarResolucion = 0;
			     
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("mensaje", "Información de anulacion subida con éxito"); // Mensaje en modal
			
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje", "Debe seleccionar al menos una causal"); // Mensaje en modal
			}
				
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje", "Debe seleccionar el documento"); // Mensaje en modal

				
			}
				
			} else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("mensaje", "Debe seleccionar el tipo de anulación"); // Mensaje en modal
			}
			
//			
//			for (EstudianteJdbcDto itemMtr :amfListAsignaturasMatriculaEstud) {
//				System.out.println(itemMtr.getMtrDescripcion());
//				
//				for (CausalDto causalDto : itemMtr.getListaCausalesDto()) {
//					System.out.println(causalDto.getCslDescripcion());
//					
//					
//				}
//			}
			
		}
		
		/**
		 * Método que desactiva el el botón guardar.
		 */
		public void desactivaModalGuardar() {
			amfActivaModalGuardarAnulacion = 0;
		}

	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getAmfUsuario() {
		return amfUsuario;
	}

	public void setAmfUsuario(Usuario amfUsuario) {
		this.amfUsuario = amfUsuario;
	}

	public PersonaDto getAmfPersonaDtoBuscar() {
		return amfPersonaDtoBuscar;
	}

	public void setAmfPersonaDtoBuscar(PersonaDto amfPersonaDtoBuscar) {
		this.amfPersonaDtoBuscar = amfPersonaDtoBuscar;
	}

	public List<CarreraDto> getAmfListCarreraDto() {
		amfListCarreraDto = amfListCarreraDto == null ? (new ArrayList<CarreraDto>()) : amfListCarreraDto;
		return amfListCarreraDto;
	}

	public void setAmfListCarreraDto(List<CarreraDto> amfListCarreraDto) {
		this.amfListCarreraDto = amfListCarreraDto;
	}

	public CarreraDto getAmfCarreraDtoBuscar() {
		return amfCarreraDtoBuscar;
	}

	public void setAmfCarreraDtoBuscar(CarreraDto amfCarreraDtoBuscar) {
		this.amfCarreraDtoBuscar = amfCarreraDtoBuscar;
	}

		
	public PeriodoAcademico getAmfPeriodoAcademicoActivo() {
		return amfPeriodoAcademicoActivo;
	}

	public void setAmfPeriodoAcademicoActivo(PeriodoAcademico amfPeriodoAcademicoActivo) {
		this.amfPeriodoAcademicoActivo = amfPeriodoAcademicoActivo;
	}

	
	public List<EstudianteJdbcDto> getAmfListaEstudiantes() {
		amfListaEstudiantes = amfListaEstudiantes == null ? (new ArrayList<EstudianteJdbcDto>()) : amfListaEstudiantes;
		return amfListaEstudiantes;
	}

	public void setAmfListaEstudiantes(List<EstudianteJdbcDto> amfListaEstudiantes) {
		this.amfListaEstudiantes = amfListaEstudiantes;
	}

	public EstudianteJdbcDto getAmfEstudianteEditar() {
		return amfEstudianteEditar;
	}

	public void setAmfEstudianteEditar(EstudianteJdbcDto amfEstudianteEditar) {
		this.amfEstudianteEditar = amfEstudianteEditar;
	}

	public List<InstitucionAcademicaDto> getAmfListUniversidadesDto() {
		amfListUniversidadesDto = amfListUniversidadesDto == null ? (new ArrayList<InstitucionAcademicaDto>()) : amfListUniversidadesDto;
		return amfListUniversidadesDto;
	}

	public void setAmfListUniversidadesDto(List<InstitucionAcademicaDto> amfListUniversidadesDto) {
		this.amfListUniversidadesDto = amfListUniversidadesDto;
	}

	public List<TituloDto> getAmfListTitulosUniversidadDto() {
		amfListTitulosUniversidadDto = amfListTitulosUniversidadDto == null ? (new ArrayList<TituloDto>()) : amfListTitulosUniversidadDto;
		return amfListTitulosUniversidadDto;
	}

	public void setAmfListTitulosUniversidadDto(List<TituloDto> amfListTitulosUniversidadDto) {
		this.amfListTitulosUniversidadDto = amfListTitulosUniversidadDto;
	}

	public EstudianteJdbcDto getAmfEstudianteGuardar() {
		return amfEstudianteGuardar;
	}

	public void setAmfEstudianteGuardar(EstudianteJdbcDto amfEstudianteGuardar) {
		this.amfEstudianteGuardar = amfEstudianteGuardar;
	}

	public boolean isAmfBloqueaCamposAdicionales() {
		return amfBloqueaCamposAdicionales;
	}

	public void setAmfBloqueaCamposAdicionales(boolean amfBloqueaCamposAdicionales) {
		this.amfBloqueaCamposAdicionales = amfBloqueaCamposAdicionales;
	}

	public PeriodoAcademico getAmfPeriodoBuscar() {
		return amfPeriodoBuscar;
	}

	public void setAmfPeriodoBuscar(PeriodoAcademico amfPeriodoBuscar) {
		this.amfPeriodoBuscar = amfPeriodoBuscar;
	}

	public List<PeriodoAcademico> getAmfListaPeriodos() {
		
		amfListaPeriodos = amfListaPeriodos == null ? (new ArrayList<PeriodoAcademico>()) : amfListaPeriodos;
		
		return amfListaPeriodos;
	}

	public void setAmfListaPeriodos(List<PeriodoAcademico> amfListaPeriodos) {
		this.amfListaPeriodos = amfListaPeriodos;
	}

	public EstudianteJdbcDto getAmfEstudianteSeleccionado() {
		return amfEstudianteSeleccionado;
	}

	public void setAmfEstudianteSeleccionado(EstudianteJdbcDto amfEstudianteSeleccionado) {
		this.amfEstudianteSeleccionado = amfEstudianteSeleccionado;
	}

	public List<EstudianteJdbcDto> getAmfListAsignaturasMatriculaEstud() {
		amfListAsignaturasMatriculaEstud = amfListAsignaturasMatriculaEstud == null ? (new ArrayList<EstudianteJdbcDto>()) : amfListAsignaturasMatriculaEstud;

		return amfListAsignaturasMatriculaEstud;
	}

	public void setAmfListAsignaturasMatriculaEstud(List<EstudianteJdbcDto> amfListAsignaturasMatriculaEstud) {
		this.amfListAsignaturasMatriculaEstud = amfListAsignaturasMatriculaEstud;
	}

	public boolean isAmfVisualizadorBotones() {
		return amfVisualizadorBotones;
	}

	public void setAmfVisualizadorBotones(boolean amfVisualizadorBotones) {
		this.amfVisualizadorBotones = amfVisualizadorBotones;
	}

	public String getAmfNombreArchivoAuxiliar() {
		return amfNombreArchivoAuxiliar;
	}

	public void setAmfNombreArchivoAuxiliar(String amfNombreArchivoAuxiliar) {
		this.amfNombreArchivoAuxiliar = amfNombreArchivoAuxiliar;
	}

	public String getAmfNombreArchivoSubido() {
		return amfNombreArchivoSubido;
	}

	public void setAmfNombreArchivoSubido(String amfNombreArchivoSubido) {
		this.amfNombreArchivoSubido = amfNombreArchivoSubido;
	}

//	public Integer getAmfValidadorSeleccion() {
//		return amfValidadorSeleccion;
//	}
//
//	public void setAmfValidadorSeleccion(Integer amfValidadorSeleccion) {
//		this.amfValidadorSeleccion = amfValidadorSeleccion;
//	}

	public boolean isAmfActivadorReporte() {
		return amfActivadorReporte;
	}

	public void setAmfActivadorReporte(boolean amfActivadorReporte) {
		this.amfActivadorReporte = amfActivadorReporte;
	}

	

	public Integer getAmfActivaModalCargarResolucion() {
		return amfActivaModalCargarResolucion;
	}

	public void setAmfActivaModalCargarResolucion(Integer amfActivaModalCargarResolucion) {
		this.amfActivaModalCargarResolucion = amfActivaModalCargarResolucion;
	}

	public EstudianteJdbcDto getAmfAsignaturaSeleccionada() {
		return amfAsignaturaSeleccionada;
	}

	public void setAmfAsignaturaSeleccionada(EstudianteJdbcDto amfAsignaturaSeleccionada) {
		this.amfAsignaturaSeleccionada = amfAsignaturaSeleccionada;
	}

	public Integer getAmfActivaModalGuardarAnulacion() {
		return amfActivaModalGuardarAnulacion;
	}

	public void setAmfActivaModalGuardarAnulacion(Integer amfActivaModalGuardarAnulacion) {
		this.amfActivaModalGuardarAnulacion = amfActivaModalGuardarAnulacion;
	}

	public Integer getAmfActivaReporteFinal() {
		return amfActivaReporteFinal;
	}

	public void setAmfActivaReporteFinal(Integer amfActivaReporteFinal) {
		this.amfActivaReporteFinal = amfActivaReporteFinal;
	}

	public String getAmfObservacionFinal() {
		return amfObservacionFinal;
	}

	public void setAmfObservacionFinal(String amfObservacionFinal) {
		this.amfObservacionFinal = amfObservacionFinal;
	}

	public List<CausalDto> getAmfListCausalAnulacion() {
		amfListCausalAnulacion = amfListCausalAnulacion == null ? (new ArrayList<CausalDto>()) : amfListCausalAnulacion;
		return amfListCausalAnulacion;
	}

	public void setAmfListCausalAnulacion(List<CausalDto> amfListCausalAnulacion) {
		this.amfListCausalAnulacion = amfListCausalAnulacion;
	}

	public boolean isAmfRenderSelectCausales() {
		return amfRenderSelectCausales;
	}

	public void setAmfRenderSelectCausales(boolean amfRenderSelectCausales) {
		this.amfRenderSelectCausales = amfRenderSelectCausales;
	}

	public boolean isAmfDesactivaBotonSubirInfo() {
		return amfDesactivaBotonSubirInfo;
	}

	public void setAmfDesactivaBotonSubirInfo(boolean amfDesactivaBotonSubirInfo) {
		this.amfDesactivaBotonSubirInfo = amfDesactivaBotonSubirInfo;
	}

	public List<EstudianteJdbcDto> getAmfListMateriasInactivas() {
		amfListMateriasInactivas = amfListMateriasInactivas == null ? (new ArrayList<EstudianteJdbcDto>()) : amfListMateriasInactivas;
		return amfListMateriasInactivas;
	}

	public void setAmfListMateriasInactivas(List<EstudianteJdbcDto> amfListMateriasInactivas) {
		this.amfListMateriasInactivas = amfListMateriasInactivas;
	}

	public List<EstudianteJdbcDto> getAmfListaSeleccionadaPorGuardar() {
		amfListaSeleccionadaPorGuardar = amfListaSeleccionadaPorGuardar == null ? (new ArrayList<EstudianteJdbcDto>()) : amfListMateriasInactivas;
		return amfListaSeleccionadaPorGuardar;
	}

	public void setAmfListaSeleccionadaPorGuardar(List<EstudianteJdbcDto> amfListaSeleccionadaPorGuardar) {
		this.amfListaSeleccionadaPorGuardar = amfListaSeleccionadaPorGuardar;
	}

	public boolean isAmfRetiroMatriculaTotal() {
		return amfRetiroMatriculaTotal;
	}

	public void setAmfRetiroMatriculaTotal(boolean amfRetiroMatriculaTotal) {
		this.amfRetiroMatriculaTotal = amfRetiroMatriculaTotal;
	}

	public String getAmfObservacionReporte() {
		return amfObservacionReporte;
	}

	public void setAmfObservacionReporte(String amfObservacionReporte) {
		this.amfObservacionReporte = amfObservacionReporte;
	}

	public boolean isAmfDesactivaBotonFinalizar() {
		return amfDesactivaBotonFinalizar;
	}

	public void setAmfDesactivaBotonFinalizar(boolean amfDesactivaBotonFinalizar) {
		this.amfDesactivaBotonFinalizar = amfDesactivaBotonFinalizar;
	}

	

	
	
}
