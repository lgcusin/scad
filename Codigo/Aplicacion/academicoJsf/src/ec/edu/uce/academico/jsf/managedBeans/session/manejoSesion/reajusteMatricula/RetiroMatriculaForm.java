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
   
 ARCHIVO:     RetiroMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja el retiro de la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-06-2017			 Dennis Collaguazo                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reajusteMatricula;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
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
import org.primefaces.event.FileUploadEvent;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;

/**
 * Clase (session bean) RetiroMatriculaForm. 
 * Bean de sesion que maneja el retiro de la matricula del estudiante. 
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name = "retiroMatriculaForm")
@SessionScoped
public class RetiroMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = 9210281719911332407L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario rmfUsuario;
	
	
	//PARA BUSQUEDA
	private PeriodoAcademico rmfPeriodoAcademicoBuscar;
	private CarreraDto rmfCarreraDtoBuscar;
	private List<FichaMatriculaDto> rmfListFichaMatriculaDtoBusq;
	private List<CarreraDto> rmfListCarreraDtoBusq;
	private List<PeriodoAcademico> rmfListPeriodoAcademicoBusq;
	private List<FichaMatriculaDto> rmfListFichaMatriculaDtoBusqCarr;
	
	//PARA VISUALIZACIÓN
	private List<EstudianteJdbcDto> rmfListEstudianteDto;
	private FichaMatriculaDto rmfFichaMatriculaDtoSeleccionado;
	private Integer rmfVisualizadorBotones;
	
	private FichaMatriculaDto rmfFichaMatriculaEstado;
	private List<EstudianteJdbcDto> rmfListMatriculaEstado;
	
	//PARA LA ANULACIÓN
	private Integer rmfValidadorSeleccion;
	private Integer rmfValidadorClic;
	private Integer rmftipoCronograma;
	private CronogramaActividadJdbcDto rmfCronogramaActividad;
	
	//ACTIVA OPCION DE RETIRO PASADO 30 DÍAS.
	private Boolean rmfActivaRetiroExtemporaneo;
	//
	private int rmfEstadoCambioDtmt;
	private int rmfEstadoRecordEstudiante;
	//PARA CARGA DE ARCHIVO
	private String rmfNombreArchivoAuxiliar;
	private String rmfNombreArchivoSubido;

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
	PeriodoAcademicoServicio servRmfPeriodoAcademicoServicio;
	@EJB 
	FichaMatriculaDtoServicioJdbc servRmfFichaMatriculaDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servRmfEstudianteDtoServicioJdbc;
	@EJB 
	CronogramaActividadDtoServicioJdbc servRmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	MatriculaServicio servRmfMatriculaServicio;
	@EJB
	UsuarioRolDtoServicioJdbc servRmfUsuarioRolDtoServicioJdbc;

	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/

	/**
	 * Dirige la navegación hacia la página de listarMatriculas
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar matricula
	 */
	public String irAListarMatricula(Usuario usuario) {
		rmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO - PREGRADO
			rmfPeriodoAcademicoBuscar = servRmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			//BUSCO PERIODOS ACADEMICOS PREGRANDO Y SUFICIENCIA
//			rmfListPeriodoAcademicoBusq = servRmfPeriodoAcademicoServicio.buscarXestadoPracXtipoPracTodos(GeneralesConstantes.APP_ID_BASE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			//BUSCA LA LISTA DE MATRÍCULAS DEL ESTUDIANTE
//			rmfListFichaMatriculaDtoBusq = servRmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarrera(rmfPeriodoAcademicoBuscar.getPracId(), usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			//solo pregrado
//			rmfListFichaMatriculaDtoBusq = servRmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacsionXcarreraTodos(rmfPeriodoAcademicoBuscar.getPracId(), usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			//solo pregrado y suficiencia en informática
			rmfListFichaMatriculaDtoBusq = servRmfFichaMatriculaDtoServicioJdbc.buscarMatriculaPregradoYSufInformaticaXPeriodoXidentificacionXcarrera(rmfPeriodoAcademicoBuscar.getPracId(), usuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE MATRICULAS
			getRmfListCarreraDtoBusq();
			//OBTENER LA LISTA DE CARRERAS QUE LOS ESTUDIANTES ESTAN MATRICULADOS
			for (FichaMatriculaDto itemMatricula : rmfListFichaMatriculaDtoBusq) { // recorro la lista de matriculas en donde esta la carrera a la que el estudiante esta matriculado
				Boolean encontro = false; // boolenado para determinar si le encontro en la lista 
				for (CarreraDto itemCarrera : rmfListCarreraDtoBusq) { // recorro la lista de carreras a las que el estudiante esta matriculado 
					if(itemMatricula.getCrrId() == itemCarrera.getCrrId()){ // si encuentro la carrera de la matricula en la lista de carreas
						encontro = true; // asigno el booleano a verdado
					}
				} //fin recorrido de lista auxiliar de carreras
				if(encontro == false){ // si no econtró la carrera de la matricula en la lista de carreras agrego esa carrera a la lista de carreas
					CarreraDto carreraAgregar = new CarreraDto();
					carreraAgregar.setCrrId(itemMatricula.getCrrId());
					carreraAgregar.setCrrDetalle(itemMatricula.getCrrDetalle());
					carreraAgregar.setCrrDescripcion(itemMatricula.getCrrDescripcion());
					rmfListCarreraDtoBusq.add(carreraAgregar);
				}
			}
			retorno = "irListarRetiroMatricula";
		} catch (FichaMatriculaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.iniciar.solicitud.retiro.ficha.matricula.exception")));
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
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
	 * Método que permite ir a la visualización de la matricula (materias y demas datos)
	 * @param fichaMatriculaDto - fichaMatriculaDto objeto matricula del estudiante que envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerMatricula(FichaMatriculaDto fichaMatriculaDto){
		String retorno = null;
		try {
			rmfFichaMatriculaDtoSeleccionado = fichaMatriculaDto;
			boolean accedeRetiro = false;
			//VERIFICA LA HABILITACION DE RETIRO DENTRO DE LOS 30 DIAS
			Date fechaActual = new Date();
			int numeroDias = GeneralesConstantes.APP_ID_BASE;		
			//DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
			if(rmfFichaMatriculaDtoSeleccionado.getCrrDependencia() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){ //si es nivelación
				rmftipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE; 
			}else{ //si es otra, en este caso va ha tener de carrera o academico
				rmftipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE; 
			}
			if(rmfFichaMatriculaDtoSeleccionado.getCrrTipo() == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
				rmftipoCronograma = CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_VALUE;
			}
			//BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
			try {
				rmfCronogramaActividad = servRmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(rmfFichaMatriculaDtoSeleccionado.getCrrId(), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, rmftipoCronograma, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE, ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
			} catch (CronogramaActividadDtoJdbcException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.exception")));
			} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.no.encontrado.exception")));
			}
			//CALCULO EL NÚMERO DE DIAS
			if(rmfCronogramaActividad.getPlcrFechaInicio() != null){
				numeroDias = GeneralesUtilidades.calcularDiferenciFechasEnDiasAbsolutos(rmfCronogramaActividad.getPlcrFechaInicio(), fechaActual); //realizo la diferencia entre las dos fechas
				//VALIDACIÓN DE DIAS DENTRO DE LO ESTABLECIDO
				if(rmftipoCronograma == CronogramaConstantes.TIPO_ACADEMICO_VALUE){
					if(numeroDias <= ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE && numeroDias >= 0){
						accedeRetiro = true;
					}else{
						accedeRetiro = false;
					}
				}else if(rmftipoCronograma == CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_VALUE){
					if(numeroDias <= ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_SUF_INFORMATICA_DOS_DIAS_VALUE && numeroDias >= 0){
						accedeRetiro = true;
					}else{
						accedeRetiro = false;
					}
				}
				
//				if(numeroDias <= ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE && numeroDias >= 0){
				if(accedeRetiro){ 
					//LLENO LA LISTA DE MATERIAS DEL ESTUDIANTE QUE TIENE DE LA MATICULA CONSULTADA
//					rmfListEstudianteDto = servRmfEstudianteDtoServicioJdbc.buscarEstudianteRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(rmfUsuario.getUsrPersona().getPrsId(),rmfFichaMatriculaDtoSeleccionado.getFcmtId(), rmfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					rmfListEstudianteDto = servRmfEstudianteDtoServicioJdbc.buscarEstudianteRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstadoCtrlTerceraMatricula(rmfUsuario.getUsrPersona().getPrsId(),rmfFichaMatriculaDtoSeleccionado.getFcmtId(), rmfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					if(rmfListEstudianteDto.size() > 0){
						Boolean verificar = true;
						for (EstudianteJdbcDto item : rmfListEstudianteDto) {
							if(item.getRcesEstado() == -1){
								verificar = false;
							}
							//En el caso de que sea materia de nivelación se selecciona automáticamente para retiro todas las materias de Nivelación
							if(item.getNvlId()==NivelConstantes.NIVEL_NIVELACION_VALUE){
								item.setSeleccionado(true);
							}
						}
						if(verificar){
							rmfVisualizadorBotones = 1;
							retorno = "irVerMatricula";
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.materia.retiro.matricula.solicitud.verifica.legaliza.matricula")));
							return null;
						}
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.no.hay.datos.ver")));
					}
				}else{
					rmfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					if(rmftipoCronograma == CronogramaConstantes.TIPO_ACADEMICO_VALUE){
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.materia.retiro.matricula.solicitud.verifica.agregra.materia.fuera.30.dias.validacion")));
					}else if(rmftipoCronograma == CronogramaConstantes.TIPO_SUFICIENCIA_INFORMATICA_VALUE){
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.materia.retiro.matricula.solicitud.verifica.agregra.materia.fuera.2.dias.validacion")));
					}
					
				}
			}else{
				rmfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.materia.retiro.matricula.solicitud.verifica.agregra.materia.carga.cronograma.validacion")));
			}
		} catch (EstudianteDtoJdbcException e) {
			rmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			rmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.no.encontrado.exception")));
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
			rmfFichaMatriculaEstado = fichaMatriculaDto;
			//LLENO LA LISTA DE MATERIAS DEL ESTUDIANTE QUE ESTA MATRÍCULADO
//			rmfListEstudianteDto = servRmfEstudianteDtoServicioJdbc.buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstado(rmfUsuario.getUsrPersona().getPrsId(),rmfFichaMatriculaDtoSeleccionado.getFcmtId(), rmfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE, GeneralesConstantes.APP_ID_BASE);
//			rmfListEstudianteDto = servRmfEstudianteDtoServicioJdbc.buscarEstudianteRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(rmfUsuario.getUsrPersona().getPrsId(),rmfFichaMatriculaDtoSeleccionado.getFcmtId(), rmfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
			rmfListMatriculaEstado = servRmfEstudianteDtoServicioJdbc.buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstadoRetiroVer(rmfUsuario.getUsrPersona().getPrsId(),rmfFichaMatriculaEstado.getFcmtId(), rmfFichaMatriculaEstado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE, GeneralesConstantes.APP_ID_BASE);
			if(rmfListMatriculaEstado.size() > 0){
				Boolean verificar = true;
				for (EstudianteJdbcDto item : rmfListMatriculaEstado) {
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
					FacesUtil.mensajeError("No puede visualizar el estado de retiro de asignaturas, no se encuentra legalizada la matrícula");
					return null;
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.no.hay.datos")));
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.no.encontrado.exception")));
		}
		return retorno;
	}
	
	// CAMBIOS  PARA  RETIROS PASADO 30 DÏAS
//	public String irVerMatricula(FichaMatriculaDto fichaMatriculaDto){
//		String retorno = null;
//		rmfFichaMatriculaDtoSeleccionado = fichaMatriculaDto;
//		// TODO	 cambiado
//		//VERIFICA LA HABILITACION DE RETIRO DENTRO DE LOS 30 DIAS
//				Date fechaActual = new Date();
//				int numeroDias = GeneralesConstantes.APP_ID_BASE;		
//				//DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
//				if(rmfFichaMatriculaDtoSeleccionado.getCrrDependencia() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){ //si es nivelación
//					rmftipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE; 
//				}else{ //si es otra, en este caso va ha tener de carrera o academico
//					rmftipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE; 
//				}
//				//BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
//				try {
//					rmfCronogramaActividad = servRmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(rmfFichaMatriculaDtoSeleccionado.getCrrId(), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, rmftipoCronograma, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE, ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
//				} catch (CronogramaActividadDtoJdbcException e) {
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.exception")));
//				} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.no.encontrado.exception")));
//				}
//				//CALCULO EL NÚMERO DE DIAS
//		
//
//		if(rmfCronogramaActividad.getPlcrFechaInicio() != null){
//			numeroDias = GeneralesUtilidades.calcularDiferenciFechasEnDiasAbsolutos(rmfCronogramaActividad.getPlcrFechaInicio(), fechaActual); //realizo la diferencia entre las dos fechas
//			//VALIDACIÓN DENTRO DE LO ESTABLECIDO
//		
//		
//	if(numeroDias <= ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE){ 
//		//	if(numeroDias <= 90){	
//			rmfActivaRetiroExtemporaneo=Boolean.TRUE;
//			rmfEstadoCambioDtmt=DetalleMatriculaConstantes.ESTADO_HISTORICO_RETIRO_SOLICITADO_VALUE;
//			rmfEstadoRecordEstudiante= RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE;
//			
//		}else{
//			rmfActivaRetiroExtemporaneo=Boolean.FALSE;
//			rmfEstadoCambioDtmt=DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_VALUE;
//    		rmfEstadoRecordEstudiante=RecordEstudianteConstantes.ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_VALUE;
//			//*****
//			rmfValidadorClic = 0;
////			FacesUtil.limpiarMensaje();
////			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.fuera.30.dias.validacion")));
//	    }
//			
//			
//		try {
//			
//			//LLENO LA LISTA DE MATERIAS DEL ESTUDIANTE QUE TIENE DE LA MATICULA CONSULTADA
//			rmfListEstudianteDto = servRmfEstudianteDtoServicioJdbc.buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstado(rmfUsuario.getUsrPersona().getPrsId(),rmfFichaMatriculaDtoSeleccionado.getFcmtId(), rmfFichaMatriculaDtoSeleccionado.getPracId(), DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE, GeneralesConstantes.APP_ID_BASE);
//			if(rmfListEstudianteDto.size() > 0){
//				rmfVisualizadorBotones = 1;
//				retorno = "irVerMatricula";
//			}else{
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.no.hay.datos")));
//			}
//		} catch (EstudianteDtoJdbcException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.exception")));
//		} catch (EstudianteDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.ver.solicitud.retiro.ficha.matricula.no.encontrado.exception")));
//		}
//		
//		
//		
//		
//		}else{
//			rmfValidadorClic = 0;
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.carga.cronograma.validacion")));
//		}
//		
//		
//		return retorno;
//	}
	
	
	/**
	 * Método que permite ir a la página de listar matriculas
	 * @return La navegación hacia la pagina xhtml de listar matriculas
	 */
	public String irRegresarMatricula(){
//		fmfDesactivar = "false";
		rmfListEstudianteDto = null;
		return "irRegresarListarMatricula";
	}
	
	/**
	 * Método que agrega materias para anular la matrícula
	 * @return retorna - la navegación de la página listar matriculas
	 */
	public String agregarMateriaRetiroMatricula(){
		String retorno = null;
		//VERIFICO QUE EXISTAN MATERIAS PARA RETIRO DE LA MATRICULA 
		if(rmfListEstudianteDto!=null && rmfListEstudianteDto.size()>0){
			try {
				//ASIGNO LAS MATERIAS SELECCIONADAS A OTRA LISTA PARA GUARDAR
				List<EstudianteJdbcDto> listaSeleccionada = new ArrayList<>();
				for (EstudianteJdbcDto item : rmfListEstudianteDto) {
					if(item.isSeleccionado()){
						listaSeleccionada.add(item);
					}
				}
				//CAMBIO DE ESTADO A SOLICITUD DE RETIRO EN DETALLE MATRICULA CON LA LISTA DE MATERIAS
				String extension = GeneralesUtilidades.obtenerExtension(rmfNombreArchivoSubido);
				List<EstudianteJdbcDto> listaSeleccionadaGuarda = new ArrayList<>();
				List<EstudianteJdbcDto> listaSeleccionadaNoGuarda = new ArrayList<>();
				String rutaNombre = null;
				String rutaTemporal = null;
				String archivoGuardado = null;
				if(servRmfMatriculaServicio.generarSolicitudRetiroMatricula(listaSeleccionada, DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE, rmfNombreArchivoSubido, DetalleMatriculaConstantes.ESTADO_HISTORICO_RETIRO_SOLICITADO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE)){
					//PARA CARGA DE ARCHIVO EN EL SERVIDOR
					if(rmfNombreArchivoSubido != null){
						for (EstudianteJdbcDto item : rmfListEstudianteDto) {
							if(item.isSeleccionado()){
								rutaNombre = item.getDtmtId()+ "." + extension;
								archivoGuardado = rutaNombre;
								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rmfNombreArchivoSubido;
								
//								String smbUrl = "smb://produ:12345.a@10.20.1.63/siiuArch/archivos/"+GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre ;
								
								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre);
								listaSeleccionadaGuarda.add(item);
							}else{
								listaSeleccionadaNoGuarda.add(item);
							}
							rutaNombre = null;
							rutaTemporal = null;
						}
						/******************************************************************************/
						/************************* PROCESO DE ENVIÓ DE MAIL ***************************/
						/******************************************************************************/

						//datos estudiante para envio
						rmfFichaMatriculaDtoSeleccionado.getPrsMailInstitucional();

						//datos del director de carrera
						UsuarioRolJdbcDto usrAuxDoc = new UsuarioRolJdbcDto();
						usrAuxDoc = servRmfUsuarioRolDtoServicioJdbc.buscarUsuarioXCarreraXRol(RolConstantes.ROL_DIRCARRERA_VALUE, rmfFichaMatriculaDtoSeleccionado.getCrrId());
						//lista de materias retiradas por el estudiante para envió

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
							//                             pathGeneralRequisitos.append("C:\\archivos\\5_1717975989.pdf");  
							pathGeneralRequisitos.append(GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ archivoGuardado);
							Path path = Paths.get(pathGeneralRequisitos.toString());
							byte[] arregloRequisitos = Files.readAllBytes(path);

							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(rmfFichaMatriculaDtoSeleccionado.getPrsMailInstitucional()); //estudiante
							correosTo.add(usrAuxDoc.getPrsMailInstitucional()); //director de carrera
							//							correosTo.add("dcollaguazo@uce.edu.ec");
							//path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo= new StringBuilder();
							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
							String fecha = formato.format(new Date());

							sbCorreo= GeneralesUtilidades.generarAsuntoSolicitudRetiroMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									rmfFichaMatriculaDtoSeleccionado.getPrsPrimerApellido()+" "+rmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido()+" "+rmfFichaMatriculaDtoSeleccionado.getPrsNombres() , GeneralesUtilidades.generaStringConTildes(rmfFichaMatriculaDtoSeleccionado.getCrrDescripcion()),listaSeleccionada);	

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

					}
					//BUSCO LAS MATERIAS MATRICULADAS QUE TIENE EL ESTUDIANTE
					rmfListEstudianteDto = listaSeleccionadaNoGuarda;
					listaSeleccionada = null;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.exitoso")));
				}else{
					rmfListEstudianteDto = listaSeleccionadaGuarda;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.no.generada.exception")));
				}
				rmfValidadorClic = 0;
				rmfNombreArchivoAuxiliar = null;
				rmfNombreArchivoSubido = null;
				retorno = "irVerMatricula";
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
		}else{
			rmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.no.seleccion.materia.exception")));
		}
		rmfValidadorClic = 0;
		return retorno;
	}
	
	public void buscar(){
		try {
			rmfListFichaMatriculaDtoBusq = servRmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraTodos(rmfPeriodoAcademicoBuscar.getPracId(), rmfUsuario.getUsrPersona().getPrsIdentificacion(), rmfCarreraDtoBuscar.getCrrId());
		} catch (FichaMatriculaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  RETIROS DE MATERIA PASADO 30 DÍAS
//	public String agregarMateriaRetiroMatricula(){
//		String retorno = null;
//		//VERIFICO QUE EXISTAN MATERIAS PARA RETIRO DE LA MATRICULA 
//		if(rmfListEstudianteDto!=null && rmfListEstudianteDto.size()>0){
//			try {
//				//ASIGNO LAS MATERIAS SELECCIONADAS A OTRA LISTA PARA GUARDAR
//				List<EstudianteJdbcDto> listaSeleccionada = new ArrayList<>();
//				for (EstudianteJdbcDto item : rmfListEstudianteDto) {
//					if(item.isSeleccionado()){
//						listaSeleccionada.add(item);
//					}
//				}
//				//CAMBIO DE ESTADO A SOLICITUD DE RETIRO EN DETALLE MATRICULA CON LA LISTA DE MATERIAS
//				String extension = GeneralesUtilidades.obtenerExtension(rmfNombreArchivoSubido);
//				List<EstudianteJdbcDto> listaSeleccionadaGuarda = new ArrayList<>();
//				List<EstudianteJdbcDto> listaSeleccionadaNoGuarda = new ArrayList<>();
//				String rutaNombre = null;
//				String rutaTemporal = null;
//			//TODO:  SE CAMBIO
//				if(servRmfMatriculaServicio.generarSolicitudRetiroMatricula(listaSeleccionada, DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE, rmfNombreArchivoSubido, rmfEstadoCambioDtmt, rmfEstadoRecordEstudiante)){
//					//PARA CARGA DE ARCHIVO EN EL SERVIDOR
//					if(rmfNombreArchivoSubido != null){
//						for (EstudianteJdbcDto item : rmfListEstudianteDto) {
//							if(item.isSeleccionado()){
//								rutaNombre = item.getDtmtId()+ "." + extension;
//								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rmfNombreArchivoSubido;
//								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre);
//								listaSeleccionadaGuarda.add(item);
//							}else{
//								listaSeleccionadaNoGuarda.add(item);
//							}
//							rutaNombre = null;
//							rutaTemporal = null;
//						}
//					}
//					
//					//BUSCO LAS MATERIAS MATRICULADAS QUE TIENE EL ESTUDIANTE
//					rmfListEstudianteDto = listaSeleccionadaNoGuarda;
//					listaSeleccionada = null;
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.exitoso")));
//				}else{
//					rmfListEstudianteDto = listaSeleccionadaGuarda;
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.no.generada.exception")));
//				}
//				rmfValidadorClic = 0;
//				rmfNombreArchivoAuxiliar = null;
//				rmfNombreArchivoSubido = null;
//				retorno = "irVerMatricula";
//			} catch (Exception e) {
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError(e.getMessage());
//			}
//		}else{
//			rmfValidadorClic = 0;
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.no.seleccion.materia.exception")));
//		}
//		rmfValidadorClic = 0;
//		return retorno;
//	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		//INSTANCIO EL PERIODO ACADÉMICO
		rmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//SETEO PERIODO ACADMEICO
		rmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INSTANCIO LA CARRERA DTO
		rmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO LA CRONOGRAMA ACTIVIDAD DTO
		rmfCronogramaActividad = new CronogramaActividadJdbcDto();
		//ANULO LA LISTA DE MATRICULAS
		rmfListFichaMatriculaDtoBusq = null;
		//ANULO LA LISTA DE CARRERAS
		rmfListCarreraDtoBusq = null;
		//INICIALIZO LA VARIABLE PARA EL MODAL DE ANULACIÓN DE MATRICULAS
		rmfValidadorClic = 0;
		//INICIALIZO LA VARIABLE DE ARCHIVO SUBIDO PARA VACIAR DATOS 
		rmfNombreArchivoSubido = null;
		//INICIALIZO LA VARIABLE DE ARCHIVO AUXILIAR PARA VACIAR DATOS 
		rmfNombreArchivoAuxiliar = null;
	}
	
	//SELECCIONA TODOS LOS ITEMS DE LA LISTA
	/**
	 * Método que realiza la selección del check box de todos 
	 * o de ninguna materia para el retiro de matrícula 
	 */
	public void seleccionarTodosAgregarRetiro(){
		if(rmfListEstudianteDto!= null && rmfListEstudianteDto.size()>0){
			for (EstudianteJdbcDto item : rmfListEstudianteDto) {
				item.setSeleccionado(rmfValidadorSeleccion == GeneralesConstantes.APP_TIPO_SELECCION_TODOS_VALUE);
			}
		}
	}
	
	//CARGA DE ARCHIVO
	/**
	 * Método para cargar el archivo en ruta temporal
	 * @param event - event archivo oficio que presenta el estudiante para anular matrícula
	 */
	public void handleFileUpload(FileUploadEvent archivo) {
		rmfNombreArchivoSubido = archivo.getFile().getFileName();
		rmfNombreArchivoAuxiliar = archivo.getFile().getFileName();
		String rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rmfNombreArchivoSubido;
		try {
			GeneralesUtilidades.copiarArchivo(archivo.getFile().getInputstream(),	rutaTemporal);
			archivo.getFile().getInputstream().close();
		} catch (IOException ioe) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.handleFileUpload.carga.archivo.exception")));
		}
	}
	
	//HABILITA EL MODAL
	/**
	 * Verifica que haga click en el boton agregar materias para anular matrícula
	 * @return retora null para para cualquier cosa pero setea a 6 la variable rmfValidadorClic
	 * estado para poder agregra materias para anular
	 */
	public String verificarClickAgregarMateriaRetiro(){
		//VERIFICA LA HABILITACION DE RETIRO DENTRO DE LOS 30 DIAS
		Date fechaActual = new Date();
		int numeroDias = GeneralesConstantes.APP_ID_BASE;		
		//DEFINO EL TIPO DE CRONOGRAMA SI ES CARRERA O NIVELACIÓN
		if(rmfFichaMatriculaDtoSeleccionado.getCrrDependencia() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){ //si es nivelación
			rmftipoCronograma = CronogramaConstantes.TIPO_NIVELACION_VALUE; 
		}else{ //si es otra, en este caso va ha tener de carrera o academico
			rmftipoCronograma = CronogramaConstantes.TIPO_ACADEMICO_VALUE; 
		}
		
		if(rmfFichaMatriculaDtoSeleccionado.getCrrTipo() == CarreraConstantes.TIPO_SUFICIENCIA_VALUE){
			rmftipoCronograma = CronogramaConstantes.TIPO_SUFICIENCIA_IDIOMAS_VALUE;
		}
		
		//BUSCO LA FECHA INICIO DE CLASES EN CRONOGRAMA ACTIVIDAD
		try {
			rmfCronogramaActividad = servRmfCronogramaActividadDtoServicioJdbc.listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(rmfFichaMatriculaDtoSeleccionado.getCrrId(), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, rmftipoCronograma, ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE, ProcesoFlujoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.exception")));
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.cronograma.actividad.no.encontrado.exception")));
		}
		//CALCULO EL NÚMERO DE DIAS
		if(rmfCronogramaActividad.getPlcrFechaInicio() != null){
			numeroDias = GeneralesUtilidades.calcularDiferenciFechasEnDiasAbsolutos(rmfCronogramaActividad.getPlcrFechaInicio(), fechaActual); //realizo la diferencia entre las dos fechas
			//VALIDACIÓN DENTRO DE LO ESTABLECIDO
			if(numeroDias <= ProcesoFlujoConstantes.HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE && numeroDias >= 0){ 
				//VERIFICA QUE SE HA SELECCIONADO MATERIAS
				boolean existeSeleccionados = false;
				for (EstudianteJdbcDto item : rmfListEstudianteDto) {
					if(item.isSeleccionado()){
						existeSeleccionados = true;
					}
				}
				//VERIFICA QUE SE HA SUBIDO O CARGADO EL ARCHIVO
				if(existeSeleccionados){ //verifica que ha seleccionado materias
					if(rmfNombreArchivoSubido != null){ //verifica que ha subido el archivo
						rmfValidadorClic = 6;
					} else{
						rmfValidadorClic = 0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.archivo.no.cargado.validacion")));
					}
				} else{
					rmfValidadorClic = 0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.no.seleccionada.validacion")));
				}
			}else{
				rmfValidadorClic = 0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.fuera.30.dias.validacion")));
			}
		}else{
			rmfValidadorClic = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.carga.cronograma.validacion")));
		}
		return null;
	}
	
	/**
	 * Método que consulta la carrera dependiendo del periodo seleccionado
	 */
	public void cambiarPeriodo(){
		try {
			rmfListFichaMatriculaDtoBusq = servRmfFichaMatriculaDtoServicioJdbc.buscarXPeriodoXidentificacionXcarreraTodos(rmfPeriodoAcademicoBuscar.getPracId(), rmfUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE);
		} catch (FichaMatriculaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//PARA RETIRO PASADO LOS 30 DÍAS
//	public String verificarClickAgregarMateriaRetiro(){
//		//TODO:cambiado
//			
//				//VERIFICA QUE SE HA SELECCIONADO MATERIAS
//				boolean existeSeleccionados = false;
//				for (EstudianteJdbcDto item : rmfListEstudianteDto) {
//					if(item.isSeleccionado()){
//						existeSeleccionados = true;
//					}
//				}
//				//VERIFICA QUE SE HA SUBIDO O CARGADO EL ARCHIVO
//				if(existeSeleccionados){ //verifica que ha seleccionado materias
//					if(rmfNombreArchivoSubido != null){ //verifica que ha subido el archivo
//						
//							rmfValidadorClic = 6;	
//						
//						
//					} else{
//						rmfValidadorClic = 0;
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.archivo.no.cargado.validacion")));
//					}
//				} else{
//					rmfValidadorClic = 0;
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Retiro.matricula.agregar.materia.retiro.matricula.solicitud.verifica.agregra.materia.no.seleccionada.validacion")));
//				}
//			
//		
//		return null;
//	}


	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getRmfUsuario() {
		return rmfUsuario;
	}
	public void setRmfUsuario(Usuario rmfUsuario) {
		this.rmfUsuario = rmfUsuario;
	}
	public PeriodoAcademico getRmfPeriodoAcademicoBuscar() {
		return rmfPeriodoAcademicoBuscar;
	}
	public void setRmfPeriodoAcademicoBuscar(PeriodoAcademico rmfPeriodoAcademicoBuscar) {
		this.rmfPeriodoAcademicoBuscar = rmfPeriodoAcademicoBuscar;
	}
	public CarreraDto getRmfCarreraDtoBuscar() {
		return rmfCarreraDtoBuscar;
	}
	public void setRmfCarreraDtoBuscar(CarreraDto rmfCarreraDtoBuscar) {
		this.rmfCarreraDtoBuscar = rmfCarreraDtoBuscar;
	}
	public List<FichaMatriculaDto> getRmfListFichaMatriculaDtoBusq() {
		rmfListFichaMatriculaDtoBusq = rmfListFichaMatriculaDtoBusq==null?(new ArrayList<FichaMatriculaDto>()):rmfListFichaMatriculaDtoBusq;
		return rmfListFichaMatriculaDtoBusq;
	}
	public void setRmfListFichaMatriculaDtoBusq(List<FichaMatriculaDto> rmfListFichaMatriculaDtoBusq) {
		this.rmfListFichaMatriculaDtoBusq = rmfListFichaMatriculaDtoBusq;
	}
	public List<CarreraDto> getRmfListCarreraDtoBusq() {
		rmfListCarreraDtoBusq = rmfListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):rmfListCarreraDtoBusq;
		return rmfListCarreraDtoBusq;
	}
	public void setRmfListCarreraDtoBusq(List<CarreraDto> rmfListCarreraDtoBusq) {
		this.rmfListCarreraDtoBusq = rmfListCarreraDtoBusq;
	}
	public List<PeriodoAcademico> getRmfListPeriodoAcademicoBusq() {
		rmfListPeriodoAcademicoBusq = rmfListPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):rmfListPeriodoAcademicoBusq;
		return rmfListPeriodoAcademicoBusq;
	}
	public void setRmfListPeriodoAcademicoBusq(List<PeriodoAcademico> rmfListPeriodoAcademicoBusq) {
		this.rmfListPeriodoAcademicoBusq = rmfListPeriodoAcademicoBusq;
	}
	public List<FichaMatriculaDto> getRmfListFichaMatriculaDtoBusqCarr() {
		return rmfListFichaMatriculaDtoBusqCarr;
	}
	public void setRmfListFichaMatriculaDtoBusqCarr(List<FichaMatriculaDto> rmfListFichaMatriculaDtoBusqCarr) {
		this.rmfListFichaMatriculaDtoBusqCarr = rmfListFichaMatriculaDtoBusqCarr;
	}
	public List<EstudianteJdbcDto> getRmfListEstudianteDto() {
		rmfListEstudianteDto = rmfListEstudianteDto==null?(new ArrayList<EstudianteJdbcDto>()):rmfListEstudianteDto;
		return rmfListEstudianteDto;
	}
	public void setRmfListEstudianteDto(List<EstudianteJdbcDto> rmfListEstudianteDto) {
		this.rmfListEstudianteDto = rmfListEstudianteDto;
	}
	public FichaMatriculaDto getRmfFichaMatriculaDtoSeleccionado() {
		return rmfFichaMatriculaDtoSeleccionado;
	}
	public void setRmfFichaMatriculaDtoSeleccionado(FichaMatriculaDto rmfFichaMatriculaDtoSeleccionado) {
		this.rmfFichaMatriculaDtoSeleccionado = rmfFichaMatriculaDtoSeleccionado;
	}
	public Integer getRmfVisualizadorBotones() {
		return rmfVisualizadorBotones;
	}
	public void setRmfVisualizadorBotones(Integer rmfVisualizadorBotones) {
		this.rmfVisualizadorBotones = rmfVisualizadorBotones;
	}
	public FichaMatriculaDto getRmfFichaMatriculaEstado() {
		return rmfFichaMatriculaEstado;
	}
	public void setRmfFichaMatriculaEstado(FichaMatriculaDto rmfFichaMatriculaEstado) {
		this.rmfFichaMatriculaEstado = rmfFichaMatriculaEstado;
	}
	public List<EstudianteJdbcDto> getRmfListMatriculaEstado() {
		rmfListMatriculaEstado = rmfListMatriculaEstado==null?(new ArrayList<EstudianteJdbcDto>()):rmfListMatriculaEstado;
		return rmfListMatriculaEstado;
	}
	public void setRmfListMatriculaEstado(List<EstudianteJdbcDto> rmfListMatriculaEstado) {
		this.rmfListMatriculaEstado = rmfListMatriculaEstado;
	}
	public Integer getRmfValidadorSeleccion() {
		return rmfValidadorSeleccion;
	}
	public void setRmfValidadorSeleccion(Integer rmfValidadorSeleccion) {
		this.rmfValidadorSeleccion = rmfValidadorSeleccion;
	}
	public Integer getRmfValidadorClic() {
		return rmfValidadorClic;
	}
	public void setRmfValidadorClic(Integer rmfValidadorClic) {
		this.rmfValidadorClic = rmfValidadorClic;
	}
	public Integer getRmftipoCronograma() {
		return rmftipoCronograma;
	}
	public void setRmftipoCronograma(Integer rmftipoCronograma) {
		this.rmftipoCronograma = rmftipoCronograma;
	}
	public CronogramaActividadJdbcDto getRmfCronogramaActividad() {
		return rmfCronogramaActividad;
	}
	public void setRmfCronogramaActividad(CronogramaActividadJdbcDto rmfCronogramaActividad) {
		this.rmfCronogramaActividad = rmfCronogramaActividad;
	}
	public String getRmfNombreArchivoAuxiliar() {
		return rmfNombreArchivoAuxiliar;
	}
	public void setRmfNombreArchivoAuxiliar(String rmfNombreArchivoAuxiliar) {
		this.rmfNombreArchivoAuxiliar = rmfNombreArchivoAuxiliar;
	}
	public String getRmfNombreArchivoSubido() {
		return rmfNombreArchivoSubido;
	}
	public void setRmfNombreArchivoSubido(String rmfNombreArchivoSubido) {
		this.rmfNombreArchivoSubido = rmfNombreArchivoSubido;
	}

	public Boolean getRmfActivaRetiroExtemporaneo() {
		return rmfActivaRetiroExtemporaneo;
	}

	public void setRmfActivaRetiroExtemporaneo(Boolean rmfActivaRetiroExtemporaneo) {
		this.rmfActivaRetiroExtemporaneo = rmfActivaRetiroExtemporaneo;
	}

	public int getRmfEstadoCambioDtmt() {
		return rmfEstadoCambioDtmt;
	}

	public void setRmfEstadoCambioDtmt(int rmfEstadoCambioDtmt) {
		this.rmfEstadoCambioDtmt = rmfEstadoCambioDtmt;
	}

	public int getRmfEstadoRecordEstudiante() {
		return rmfEstadoRecordEstudiante;
	}

	public void setRmfEstadoRecordEstudiante(int rmfEstadoRecordEstudiante) {
		this.rmfEstadoRecordEstudiante = rmfEstadoRecordEstudiante;
	}

	

	
	
	
	
	
}
