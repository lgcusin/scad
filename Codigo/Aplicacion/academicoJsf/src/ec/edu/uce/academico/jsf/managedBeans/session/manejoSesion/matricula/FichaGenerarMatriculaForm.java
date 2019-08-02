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
   
 ARCHIVO:     FichaMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-03-2017			 David Arellano                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteMatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoGratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.GeneradorMails;
import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
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
 * Clase (session bean) FichaMatriculaForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author darellano.
 * @version 1.0
 */

@ManagedBean(name = "fichaGenerarMatriculaForm")
@SessionScoped
public class FichaGenerarMatriculaForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario fgmfUsuario;
	private List<FichaInscripcionDto> fgmfListFichaInscripcionDto;
	private List<CarreraDto> fgmfListCarreraDto;
	private List<RecordEstudianteDto> fgmfListRecordEstudiante;
	private Boolean fgmfEstudianteNuevo;
	private MallaCurricular fgmfMallaCurricular;
	private List<MateriaDto> fgmfListMateriaDto;
	private List<RecordEstudianteDto> fgmfListMateriaDtoAprobadas;
	private List<RecordEstudianteDto> fgmfListMateriaDtoReprobadas;
	private List<ParaleloDto> fgmfListParaleloDto;
	private PeriodoAcademico fgmfPeriodoAcademico;
	private FichaInscripcionDto fgmfFichaInscripcionDto;
	private Integer fgmfValidadorClic;
	private PlanificacionCronograma fgmfPlanificacionCronograma;
	private CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaOrdinaria;
	private CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaExtraordinaria;
	private CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaEspecial;
	private List<CarreraDto> fgmfListMateriasCarrera;
	private List<MateriaDto> fgmfListMateriasEstado;
	private Dependencia fgmfDependenciaBuscar;
	private int validadorNuevo;
	private int gratuidad;
	
	private CronogramaActividadJdbcDto fgmfFechasMatriculaOrdinaria;
	private CronogramaActividadJdbcDto fgmfFechasMatriculaExtraordinaria;
	private CronogramaActividadJdbcDto fgmfFechasMatriculaEspecial;
	private CronogramaActividadJdbcDto fgmfProcesoFlujo;
//	private CarreraDto fgmfCarreraDto ;
	private Integer fgmfBloqueoModal;  // 1.- no   0.- si   para evitar que recarguen la página y se pueda generar la matricula una y otra vez
	
	
	
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
	FichaInscripcionDtoServicioJdbc servFgmfFichaInscripcionDto;
	@EJB 
	RecordEstudianteDtoServicioJdbc servFgmfRecordEstudianteDto;
	@EJB 
	MallaCurricularServicio servFgmfMallaCurricular;
	@EJB 
	MateriaDtoServicioJdbc servFgmfMateriaDto;
	@EJB 
	ParaleloDtoServicioJdbc servFgmfParaleloDto;
	@EJB 
	PeriodoAcademicoServicio servFgmfPeriodoAcademico;
	@EJB 
	MatriculaServicio servFgmfMatriculaServicio;
	@EJB 
	FichaMatriculaDtoServicioJdbc servFgmfFichaMatriculaDto;
	@EJB 
	CronogramaServicio servFgmfCronograma;
	@EJB 
	ProcesoFlujoServicio servFgmfProcesoFlujo;
	@EJB 
	PlanificacionCronogramaServicio servFgmfPlanificacionCronograma;
	@EJB 
	CronogramaProcesoFlujoServicio servFgmfCronogramaProcesoFlujo;

	@EJB
	private CronogramaActividadDtoServicioJdbc servFgmfCronogramaActividadDtoServicioJdbcServicio;
	
	@EJB
	private CarreraDtoServicioJdbc servFgmfCarreraDtoServicioJdbc;

	@EJB
	private	MateriaDtoServicioJdbc servFgmfMateriaDtoServicioJdbc;
	
	@EJB
	private	DependenciaServicio servFgmfDependenciaServicio;
	@EJB
	private CarreraServicio servFgmfCarreraServicio;
	@EJB
	private MallaCurricularParaleloServicio servFgmfMallaCurricularParaleloServicio;
	@EJB
	private FichaEstudianteServicio servFgmfFichaEstudianteServicio;
	@EJB
	private ConfiguracionCarreraServicio servFgmfConfiguracionCarreraServicio;
	
//	private CronogramaActividadJdbcDto nrctfCronogramaActividadJdbcDtoBuscarFechaInicioSigPeriodoAcademico;
	
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula
	 * @return navegacion al listar matricula
	 */
	public String iniciarGenerarMatricula(Usuario usuario) {
		try {
			//debloqueo el modal
			fgmfBloqueoModal = 1;
			//Guardamos el usuario en una variable
			fgmfUsuario = usuario;
			//listo las fichas inscripciones activas del usuario
			fgmfListFichaInscripcionDto = servFgmfFichaInscripcionDto.buscarXidentificacionXestado(fgmfUsuario.getUsrPersona().getPrsIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
			//validador click en 0
			fgmfValidadorClic = new Integer(0);
			
			
			
			
				
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.iniciarGenerarMatricula.ficha.inscripcion.dto.exception")));
			return null;
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			
			try {
				fgmfListFichaInscripcionDto = servFgmfFichaInscripcionDto.buscarXidentificacionXestadoXMatriculado(fgmfUsuario.getUsrPersona().getPrsIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
				if(fgmfListFichaInscripcionDto.size()>0){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.matricula.fcin.matriculado.activo.nivelacion")));
					return null;
				}
			} catch (FichaInscripcionDtoException e1) {
			} catch (FichaInscripcionDtoNoEncontradoException e1) {
			}
		} 
		return "irListarFichaInscripcionCarrera";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		fgmfUsuario = null;
		fgmfListFichaInscripcionDto = null;
		fgmfListCarreraDto = null;
		fgmfValidadorClic = new Integer(0);
		//debloqueo el modal
		fgmfBloqueoModal = 1;
		return "irInicio";
	}
	
	/**
	 * Metodo que permite dirige a la página de generar matricula
	 * @return  Navegacion a la pagina de generar matricula.
	 */
	public String irGenerarMatricula(FichaInscripcionDto fichaInscripcionDto){
//		validadorNuevo=0;
		try {
			
			//guardo la fichaInscripcion seleccionada
			fgmfFichaInscripcionDto = fichaInscripcionDto;
			
			if(fgmfFichaInscripcionDto.getFcinMatriculado()==FichaInscripcionConstantes.SI_MATRICULADO_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.matricula.fcin.matriculado.activo.nivelacion")));
				return null;
			}
			
			//validacion de encuesta llena
			
//			if(fgmfFichaInscripcionDto.getFcinEncuesta().intValue() == FichaInscripcionConstantes.NO_ENCUESTA_LLENA_VALUE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.encuesta.validacion.exception")));
//				return null;
//			}
			
			//validacion de las fechas del cronograma
			// Timestamp fechaHoy = new Timestamp(new Date().getTime());
			Date fechaActual = new Date();
			//busqueda del período academico activo
			fgmfPeriodoAcademico = servFgmfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
//			fgmfDependenciaBuscar =  servFgmfDependenciaServicio.buscarPorId(fichaInscripcionDto.getCrrId());
			if(fgmfFichaInscripcionDto.getFcinCncrArea()==CarreraConstantes.CARRERA_AREA_1L_EDUCACION_EN_LINEA_VALUE
					|| fgmfFichaInscripcionDto.getFcinCncrArea()==CarreraConstantes.CARRERA_AREA_8L_CIENCIAS_SOCIALES_Y_DERECHO_EN_LINEA_VALUE){
				fgmfFechasMatriculaOrdinaria= servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_VALUE, ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
				fgmfFechasMatriculaExtraordinaria =  servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_VALUE, ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			}else{
				fgmfFechasMatriculaOrdinaria= servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
				fgmfFechasMatriculaExtraordinaria =  servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
				fgmfFechasMatriculaEspecial = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			}
			
			Date fechaInicioMatriculasOrdinarias = new Date(fgmfFechasMatriculaOrdinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasOrdinarias = new Date(fgmfFechasMatriculaOrdinaria.getPlcrFechaFin().getTime());
			Date fechaInicioMatriculasExtraordinarias = new Date(fgmfFechasMatriculaExtraordinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasExtraordinarias = new Date(fgmfFechasMatriculaExtraordinaria.getPlcrFechaFin().getTime());
			
//			Date fechaInicioMatriculasEspecial = new Date(fgmfFechasMatriculaEspecial.getPlcrFechaInicio().getTime());
//			Date fechaFinMatriculasEspecial = new Date(fgmfFechasMatriculaEspecial.getPlcrFechaFin().getTime());
			if((fechaActual.before(fechaFinMatriculasOrdinarias) && fechaActual.after(fechaInicioMatriculasOrdinarias))||
					(fechaActual.before(fechaFinMatriculasExtraordinarias) && fechaActual.after(fechaInicioMatriculasExtraordinarias))){
			
			//validadcion de si tiene matricula generada
				List<FichaMatriculaDto> fichaMatriculaDtoAux = new ArrayList<FichaMatriculaDto>();
				try {
					fichaMatriculaDtoAux = servFgmfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(fgmfPeriodoAcademico.getPracId(), fgmfUsuario.getUsrIdentificacion(), fgmfFichaInscripcionDto.getCrrId());		
				} catch (Exception e) {
				}
			
			
			
			if(fichaMatriculaDtoAux.size() > 0){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.matricula.generada.exception",fgmfFichaInscripcionDto.getCrrDetalle())));
				return null;
			}
			
			//busco el record del estudiante en todos los períodos
			try {
//				Buscar la malla curricular vigente y activa de la carrera que quiere generar la matricula
				fgmfListRecordEstudiante = servFgmfRecordEstudianteDto.buscarXidentificacionXpracActivoXprcacEnCierre(fgmfUsuario.getUsrIdentificacion());	
			} catch (Exception e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.record.estudiante.dto.exception")));
				return null;
			}
			
			fgmfMallaCurricular = servFgmfMallaCurricular.buscarXcarreraXvigenciaXestado(fgmfFichaInscripcionDto.getCrrId(), MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			if(fgmfListRecordEstudiante.size() == 0 || fgmfFichaInscripcionDto.getFcinObservacion().equals(FichaInscripcionConstantes.OBS_NUEVO_CUPO_NIVELACION)){//es estudiante nuevo
				if(fgmfFichaInscripcionDto.getFcinTipo().equals(FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE)) {
					//buscar las materias de nivelacion de esa malla
					fgmfListMateriaDto = servFgmfMateriaDto.listarXmallaXnivel(fgmfMallaCurricular.getMlcrId(), NivelConstantes.NIVEL_NIVELACION_VALUE);
					//seteo variable de estudiante nuevo
					fgmfEstudianteNuevo = true;
					//lleno los paralelos de las materias
					for (MateriaDto itmMateria : fgmfListMateriaDto) {
						//busco los paralelos de esa materia
						fgmfListParaleloDto = servFgmfParaleloDto.listarXmallaMateriaXperiodoNivelacion(itmMateria.getMlcrmtId(), fgmfPeriodoAcademico.getPracId(),fgmfFichaInscripcionDto.getFcinCarreraSiiu());
						//guardo los paralelos de esa materia
						itmMateria.setListParalelos(fgmfListParaleloDto);
						// seteo la variable de seleccion ya que los estudiantes nuevos cogen todas las materias obligatoriamente
						itmMateria.setIsSeleccionado(true); 
						// seteo el numero de matricula a primera
						itmMateria.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
					}
				}
			}else if(fgmfListRecordEstudiante.size() > 0){	
				//determino si la inscripcion es para nivelacion o para carrera
				if(fgmfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE){
					//buscar las materias de nivelacion de esa malla
					fgmfListMateriaDto = servFgmfMateriaDto.listarXmallaXnivel(fgmfMallaCurricular.getMlcrId(), NivelConstantes.NIVEL_NIVELACION_VALUE);
					//estudiante de nivelación
					//seteo variable de estudiante nuevo
					fgmfEstudianteNuevo = false;
//					int contMateria = 0;
//					Boolean encontro = false;
					//determinar materias a matricularse
					for (RecordEstudianteDto itemRecord : fgmfListRecordEstudiante) {
						if(itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
							Iterator<MateriaDto> it = fgmfListMateriaDto.iterator();
							while (it.hasNext()) {
								MateriaDto mtDto = it.next();
//								elimino las materias 
								if(itemRecord.getMtrId()==(mtDto.getMtrId())){
									it.remove();
								}
							}
							
						}
					}
					for (RecordEstudianteDto itemRecord : fgmfListRecordEstudiante) {
						//Busco si tiene aprobado la materia ETICA Y PENSAMIENTO UNIVERSITARIO VIRTUAL/ INVESTIGACION, PROYECTOS INTEGRADORES
						if(itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE
								&& itemRecord.getMtrCodigo().equals("01")){
							Iterator<MateriaDto> it = fgmfListMateriaDto.iterator();
							while (it.hasNext()) {
								MateriaDto mtDto = it.next();
//								elimino las materias ETICA Y PENSAMIENTO UNIVERSITARIO VIRTUAL y INVESTIGACIÓN, PROYECTO INTEGRADOR DE SABERES VIRTUAL
								if(mtDto.getMtrCodigo().equals("31") || mtDto.getMtrCodigo().equals("32")){
									it.remove();
								}
							}
						}
//						if(itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
//							Iterator<MateriaDto> it = fgmfListMateriaDto.iterator();
//							while (it.hasNext()) {
//								MateriaDto mtDto = it.next();
////								elimino las materias ETICA Y PENSAMIENTO UNIVERSITARIO VIRTUAL y INVESTIGACIÓN, PROYECTO INTEGRADOR DE SABERES VIRTUAL
//								if(mtDto.getMtrCodigo().equals(mtDto.getMtrCodigo())){
//									it.remove();
//								}
//							}
//						}
						
					}
					if(fgmfListMateriaDto.size()!=0){
					//determinar segundas o terceras matriculas
						for (MateriaDto itmMateria : fgmfListMateriaDto) {
							
						//busco los paralelos de esa materia
							fgmfListParaleloDto = servFgmfParaleloDto.listarXmallaMateriaXperiodo(itmMateria.getMlcrmtId(), fgmfPeriodoAcademico.getPracId());
							//guardo los paralelos de esa materia
							itmMateria.setListParalelos(fgmfListParaleloDto);
							// seteo la variable de seleccion ya que los estudiantes nuevos cogen todas las materias obligatoriamente
							itmMateria.setIsSeleccionado(true); 
							// calcular el numero de matricula
							int contMatricula = 0;
							
							for (RecordEstudianteDto itemRecord : fgmfListRecordEstudiante) {
								if(itemRecord.getMtrCodigo().equals(itmMateria.getMtrCodigo()) && (itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE
										|| itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE)){
									contMatricula++;
								}
							}
							if(contMatricula == 0){
								itmMateria.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
									validadorNuevo=0;
									
							}else if(contMatricula == 1){
								itmMateria.setNumMatricula(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE);
								validadorNuevo=1;
							}else if(contMatricula == 2){
								itmMateria.setNumMatricula(DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE);
								validadorNuevo=1;
							}
							
						}
					}
					int contEtica = 0;
					for (RecordEstudianteDto itemRecord : fgmfListRecordEstudiante) {
						if(itemRecord.getMtrCodigo().equals("01")&& itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
							contEtica++;
						}
					}
					if(contEtica == 0){
						for (MateriaDto itmMateria1 : fgmfListMateriaDto) {
							if(itmMateria1.getMtrCodigo().equals("31") || itmMateria1.getMtrCodigo().equals("32")){
								validadorNuevo=1;
								itmMateria1.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
							}
						}
					}else if(contEtica == 1){
						for (MateriaDto itmMateria1 : fgmfListMateriaDto) {
							if(itmMateria1.getMtrCodigo().equals("31") || itmMateria1.getMtrCodigo().equals("32")){
								validadorNuevo=1;
								itmMateria1.setNumMatricula(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE);
							}
							
						}
					}else if(contEtica == 2){
						for (MateriaDto itmMateria1 : fgmfListMateriaDto) {
							if(itmMateria1.getMtrCodigo().equals("31") || itmMateria1.getMtrCodigo().equals("32")){
								validadorNuevo=1;
								itmMateria1.setNumMatricula(DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE);
							}
						}
					}
					List<PeriodoAcademico> listaperiodos = servFgmfPeriodoAcademico.listarTodosPreGradoOrdenadosXId();
					List<FichaMatriculaDto> listamatriculas = null;
					try {
						listamatriculas = servFgmfFichaMatriculaDto.buscarFichaMatriculaXPeriodoXIdentificacion(listaperiodos.get(1).getPracId(), fgmfUsuario.getUsrIdentificacion());
					} catch (Exception e) {
						
					}
					if(listamatriculas==null || listamatriculas.size()==0){
						// En caso de no poseer matrícula
						for (MateriaDto itmMateria1 : fgmfListMateriaDto) {
							validadorNuevo=1;
							itmMateria1.setNumMatricula(itmMateria1.getNumMatricula()+1);
							// Si tiene más de tres matrículas se detiene el trámite
							if(itmMateria1.getNumMatricula()>3) validadorNuevo=2;
						}
					}
					
					
				}
					
			}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.cronograma.validacion.exception")));
				return null;
			}
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.malla.curricular.no.encontrado.exception")));
			return null;
		} catch (MallaCurricularException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.malla.curricular.exception")));
			return null;
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.materia.dto.exception")));
			return null;
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.materia.dto.no.encontrado.exception")));
			return null;
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.paralelo.dto.exception")));
			return null;
  		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.paralelo.dto.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.periodo.academico.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.periodo.academico.exception")));
			return null;
		} catch (CronogramaActividadDtoJdbcException e) {
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
		}  
		CronogramaActividadJdbcDto cracAuxInicio = new CronogramaActividadJdbcDto();
		CronogramaActividadJdbcDto cracAuxExtraordinarias = new CronogramaActividadJdbcDto();
		if(fgmfFichaInscripcionDto.getFcinCncrArea()==CarreraConstantes.CARRERA_AREA_1L_EDUCACION_EN_LINEA_VALUE
				|| fgmfFichaInscripcionDto.getFcinCncrArea()==CarreraConstantes.CARRERA_AREA_8L_CIENCIAS_SOCIALES_Y_DERECHO_EN_LINEA_VALUE){
			try {
				cracAuxInicio = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
				cracAuxExtraordinarias = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_VERANO_NIVELACION_EN_LINEA_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			} catch (CronogramaActividadDtoJdbcException | CronogramaActividadDtoJdbcNoEncontradoException e1) {
			}
		}else{
			try {
				cracAuxInicio = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
				cracAuxExtraordinarias = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			} catch (CronogramaActividadDtoJdbcException | CronogramaActividadDtoJdbcNoEncontradoException e1) {
			}	
		}
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(cracAuxInicio.getPlcrFechaInicio());
//		cal.add(Calendar.DAY_OF_WEEK, 30);
//		cracAuxInicio.setPlcrFechaInicio( new Timestamp(cal.getTime().getTime()));
		
		//Transformamos a tipo Date
		Date fechaInicioMatriculaNivelacion = new Date(cracAuxInicio.getPlcrFechaInicio().getTime());
		Date fechaFinMatriculaNivelacion = new Date(cracAuxInicio.getPlcrFechaFin().getTime());
		Date fechaInicioExtraordinariaNivelacion = new Date(cracAuxExtraordinarias.getPlcrFechaInicio().getTime());
		Date fechaFinExtraordinariaNivelacion = new Date(cracAuxExtraordinarias.getPlcrFechaFin().getTime());
		Date fechaActual = new Date();
//		for (MateriaDto itmMateria : fgmfListMateriaDto) {
//			if(itmMateria.getDtmtNumero()!=null){
//				if(itmMateria.getDtmtNumero()>0){
//					validadorNuevo=1;
//				}else{
//					validadorNuevo=0;
//				}	
//			}else{
//				itmMateria.setNumMatricula(DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE);
//				validadorNuevo=1;
//			}
//			
//		}
		
			if(validadorNuevo==1){
				if(fechaActual.before(fechaInicioExtraordinariaNivelacion) || fechaActual.after(fechaFinExtraordinariaNivelacion)){
					
					FacesUtil.mensajeError("El cronograma no se encuentra en la fecha de matriculación de nivelación para estudiantes antiguos del sistema.");
					return null;
				}else{
					return "irGenerarMatricula";
				}
			}else if (validadorNuevo==0){
				if(fechaActual.before(fechaInicioMatriculaNivelacion) || fechaActual.after(fechaFinMatriculaNivelacion)){
					FacesUtil.mensajeError("El cronograma no se encuentra en la fecha de matriculación de nivelación para estudiantes nuevos del sistema.");
					return null;
				}else{
					return "irGenerarMatricula";
				}
			}else {
				FacesUtil.mensajeError("Usted no puede matricularse ya que posee más de tres matrículas en una misma materia.");
				return null;
			}
	}
	
	
	

	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String cancelarGenerarMatricula(){
		
		fgmfFichaInscripcionDto = new FichaInscripcionDto();
		fgmfPeriodoAcademico = new PeriodoAcademico();
		fgmfListRecordEstudiante = null;
		fgmfEstudianteNuevo = new Boolean(false);
		fgmfMallaCurricular = new MallaCurricular();
		fgmfListMateriaDto = null;
		fgmfListMateriaDtoAprobadas = null;
		fgmfListMateriaDtoReprobadas = null;
		fgmfListParaleloDto = null;
		fgmfValidadorClic = new Integer(0);
		fgmfBloqueoModal = 1;
		irInicio();
		return "cancelarMatricula";
	}
	
	
	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public String generarMatricula(){
		if(fgmfBloqueoModal == 1){
			fgmfBloqueoModal = 0;
			try {
//				if(fgmfEstudianteNuevo){ //estudiante nuevo  asignacion de 1 cupo por todas las materias
					//Asignación de paralelos
					ParaleloDto paraleloAux = null ;
					for (ParaleloDto paraleloDtoItem : fgmfListParaleloDto) { // recorro los paralelos disponibles
						if(fgmfEstudianteNuevo){
							try {
								if(paraleloDtoItem.getMlcrprInscritos().intValue() < paraleloDtoItem.getMlcrprCupo().intValue()-paraleloDtoItem.getMlcrprReservaRepetidos().intValue()){ // si el cupo no esta lleno
									paraleloAux = paraleloDtoItem; // guardo el paralelo con cupo disponible
									break;
								}		
							} catch (Exception e) {
								if(paraleloDtoItem.getMlcrprInscritos().intValue() < paraleloDtoItem.getMlcrprCupo().intValue()){ // si el cupo no esta lleno
									paraleloAux = paraleloDtoItem; // guardo el paralelo con cupo disponible
									break;
								}	
							}
							
						}else{
							if(paraleloDtoItem.getMlcrprInscritos().intValue() < paraleloDtoItem.getMlcrprCupo().intValue()){ // si el cupo no esta lleno
								paraleloAux = paraleloDtoItem; // guardo el paralelo con cupo disponible
								break;
							}
						}
					} 
					if(paraleloAux != null){ // si existio paralelo disponible
						for (MateriaDto materiasItem : fgmfListMateriaDto) { // recorro la lista de materias 
							materiasItem.setPrlId(paraleloAux.getPrlId()); // asigno el paralelo a las materias
							materiasItem.setPrlDescripcion(paraleloAux.getPrlDescripcion());
							materiasItem.setMlcrprId(paraleloAux.getMlcrprId()); //
							//busco la malla curricular paralelo
							//asignar 1 solo cupo a ese paralelo 
						}
					}else{ // si no exite cupos disponibles en ningun paralelo
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatricula.paralelo.validacion.exception")));
						fgmfValidadorClic = new Integer(0);
						return null;
					}
					Carrera carreraAux = new Carrera();
					try {
						carreraAux = servFgmfCarreraServicio.buscarPorId(fgmfFichaInscripcionDto.getCrrId());
					} catch (CarreraNoEncontradoException e) {
					} catch (CarreraException e) {
					}
					//nivel ubicacion se setea con NIVELACIÓN ya que son estudiantes nuevos
					//tipo matricula calcular con el cronograma
					//tipo gratuidad se envia con tiene gratuidad ya que es estudiante nuevo
					gratuidad=DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE;
					int gratuidadTipo=TipoGratuidadConstantes.APLICA_GRATUIDAD_VALUE;
					BigDecimal costoMatricula = ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_PRIMERA_MATRICULA_VALUE;
					for (MateriaDto item : fgmfListMateriaDto) {
						if(item.getNumMatricula()==DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE){
							gratuidad=DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE;
							costoMatricula = ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_TERCERA_MATRICULA_VALUE;
							gratuidadTipo=TipoGratuidadConstantes.PERDIDA_TEMPORAL_VALUE;
							try {
								FichaEstudiante fcesAux = servFgmfFichaEstudianteServicio.buscarXpersonaId(fgmfFichaInscripcionDto.getPrsId());
								if(fcesAux.getFcesObservacion()!=null){
									if(fcesAux.getFcesObservacion().equals(FichaEstudianteConstantes.ESTADO_TERCERA_MATRICULA_APROBADO_LABEL)){
										break;
									}else{
										servFgmfFichaEstudianteServicio.editarObservacionTerceraMatricula(fcesAux.getFcesId());
										throw new MatriculaValidacionException("Para matricularse debe presentar su solicitud  de tercera matrícula en la Dirección Académica.");		
									}	
								}else{
									servFgmfFichaEstudianteServicio.editarObservacionTerceraMatricula(fcesAux.getFcesId());
									throw new MatriculaValidacionException("Para matricularse debe presentar su solicitud  de tercera matrícula en la Dirección Académica.");
								}
								
							} catch (FichaEstudianteNoEncontradoException e) {
							} catch (FichaEstudianteException e) {
							}
						}else if (item.getNumMatricula()==DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE){
							gratuidad=DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE;
							costoMatricula = ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE;
							gratuidadTipo=TipoGratuidadConstantes.PERDIDA_TEMPORAL_VALUE;
						}else if (item.getNumMatricula()==DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE){
							continue;
						}else{
							gratuidad=DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE;
							costoMatricula = ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_TERCERA_MATRICULA_VALUE;
							gratuidadTipo=TipoGratuidadConstantes.PERDIDA_TEMPORAL_VALUE;
//							FacesUtil.mensajeError("Usted ya registra tres matrículas reprobadas en el curso de nivelación, no puede continuar.");
//							fgmfValidadorClic = new Integer(0);
//							return null;
						}
					}
					Date fechaActual = new Date();
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/YYYY");
					String strDate = sdf1.format(fechaActual);
					
					if(fgmfFichaInscripcionDto.getFcinCncrArea()==CarreraConstantes.CARRERA_AREA_1L_EDUCACION_EN_LINEA_VALUE
							|| fgmfFichaInscripcionDto.getFcinCncrArea()==CarreraConstantes.CARRERA_AREA_8L_CIENCIAS_SOCIALES_Y_DERECHO_EN_LINEA_VALUE){
						fgmfProcesoFlujo = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarPlanificacionCronogramaPorFechasNivelacionCursoVerano(strDate);
						fgmfPlanificacionCronograma = servFgmfPlanificacionCronograma.buscarPorId(fgmfProcesoFlujo.getPlcrId());
						
					}else{
						fgmfProcesoFlujo = servFgmfCronogramaActividadDtoServicioJdbcServicio.buscarPlanificacionCronogramaPorFechasNivelacion(strDate);
						fgmfPlanificacionCronograma = servFgmfPlanificacionCronograma.buscarPorId(fgmfProcesoFlujo.getPlcrId());
						
					
					}
					
					if(gratuidad==DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE){
						String numComprobante=null;
						numComprobante=servFgmfMatriculaServicio.generarMatricula(fgmfListMateriaDto, fgmfUsuario.getUsrPersona().getPrsId(), fgmfFichaInscripcionDto
								,FichaMatriculaConstantes.NIVELACION_VALUE //nivel ubicacion 
								, gratuidad 										 //tipo matricula
								, gratuidadTipo											 //tipo gratuidad
								, costoMatricula, fgmfMallaCurricular, fgmfEstudianteNuevo,fgmfPlanificacionCronograma, fgmfPeriodoAcademico);
					if(!numComprobante.equals("N/A")){
						StringBuilder pathGeneralImagenes = new StringBuilder();
						pathGeneralImagenes.append(FacesContext.getCurrentInstance()
								.getExternalContext().getRealPath("/"));
						pathGeneralImagenes.append("/academico/reportes/archivosJasper/matriculasNivelacion/codigoBarras.png");
							// Generamos el código de barras para el adjunto del voucher de pago
						try {
//							//Es el tipo de clase 
							Barcode128 code128 = new Barcode128();
							//Seteo el tipo de codigo
							code128.setCodeType(Barcode.CODE128);
							code128.setBarHeight(15f); 
							code128.setStartStopText(false);
							code128.setExtended(true);
							code128.setX(1f);
							//Setep el codigo
							code128.setCode(numComprobante);
							//Guardo la imagen
							java.awt.Image rawImage = code128.createAwtImage(Color.BLACK, Color.WHITE);
							BufferedImage outImage = new BufferedImage(rawImage.getWidth(null), rawImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
							outImage.getGraphics().drawImage(rawImage, 0, 0, null);
							ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
							ImageIO.write(outImage, "png", bytesOut);
							bytesOut.flush();
							byte[] data = bytesOut.toByteArray();
							
							BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
							File file = new File(pathGeneralImagenes.toString());
							if (file.exists()) {
								file.delete();     
							}
							file = new File(pathGeneralImagenes.toString());
							ImageIO.write(bi, "PNG", file);
						  }catch (java.io.IOException ex) {
						  }
						Dependencia facultadAux = null;
						try {
							facultadAux = servFgmfDependenciaServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
							generarReporteOrdenCobro(facultadAux.getDpnDescripcion(),numComprobante ,fgmfUsuario.getUsrPersona(),carreraAux);
						} catch (DependenciaNoEncontradoException e) {
						}
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
							List<Map<String, Object>> frmCrpCampos = null;
							Map<String, Object> frmCrpParametros = null;
							// ****************************************************************//
							// ***************** GENERACION DE LA ORDEN DE PAGO  *************//
							// ****************************************************************//
							// ****************************************************************//
							frmCrpParametros = new HashMap<String, Object>();
							int comparador = costoMatricula.compareTo(ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE);
							
							if(comparador==0){
								frmCrpParametros.put("textoNivelacion","MATRICULA SEGUNDA VEZ - NIVELACION");
							}else if(comparador==1){
								frmCrpParametros.put("textoNivelacion","MATRICULA TERCERA VEZ - NIVELACION");
							}else{
								frmCrpParametros.put("textoNivelacion","MATRICULA PRIMERA VEZ - NIVELACION");
							}
							frmCrpParametros.put("facultad",facultadAux.getDpnDescripcion());
							SimpleDateFormat formato = 
									new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
							String fecha = formato.format(new Date());
							Calendar c = Calendar.getInstance();
							c.setTime(new Date()); 
							c.add(Calendar.DATE, 4);
							String fechaCaducidad = formato.format(c.getTime());
							frmCrpParametros.put("fecha",fecha);
							frmCrpParametros.put("fechaCaducidad",fechaCaducidad);
							
							frmCrpParametros.put("numComprobante",numComprobante);
							frmCrpCampos = new ArrayList<Map<String, Object>>();
							Map<String, Object> dato = null;
							dato = new HashMap<String, Object>();
							String nombres=fgmfUsuario.getUsrPersona().getPrsNombres()+" "+GeneralesUtilidades.generaStringParaCorreo(fgmfUsuario.getUsrPersona().getPrsPrimerApellido()).toUpperCase()+" "
									+(fgmfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":GeneralesUtilidades.generaStringParaCorreo(fgmfUsuario.getUsrPersona().getPrsSegundoApellido()).toUpperCase());
							frmCrpParametros.put("carrera", carreraAux.getCrrDescripcion());
							frmCrpParametros.put("identificacion", fgmfUsuario.getUsrPersona().getPrsIdentificacion());
							
							frmCrpParametros.put("postulante", nombres);
							
							StringBuilder sb = new StringBuilder();
							if(fgmfUsuario.getUsrPersona().getPrsSectorDomicilio()!=null){
								sb.append(fgmfUsuario.getUsrPersona().getPrsSectorDomicilio());
								sb.append(" ");	
							}
							if(fgmfUsuario.getUsrPersona().getPrsCallePrincipal()!=null){
								sb.append(fgmfUsuario.getUsrPersona().getPrsCallePrincipal());
								sb.append(" ");	
							}
							if(fgmfUsuario.getUsrPersona().getPrsNumeroCasa()!=null){
								sb.append(fgmfUsuario.getUsrPersona().getPrsNumeroCasa());
								sb.append(" ");
							}
							if(fgmfUsuario.getUsrPersona().getPrsReferenciaDomicilio()!=null){
								sb.append(fgmfUsuario.getUsrPersona().getPrsReferenciaDomicilio());	
							}
							frmCrpParametros.put("direccion", sb.toString());
								frmCrpParametros.put("valorPagar", costoMatricula.toString()+".00");	
										
							frmCrpParametros.put("email", fgmfUsuario.getUsrPersona().getPrsMailInstitucional());
							if(fgmfUsuario.getUsrPersona().getPrsTelefono()!=null){
								frmCrpParametros.put("telefono", fgmfUsuario.getUsrPersona().getPrsTelefono());	
							}else{
								frmCrpParametros.put("telefono", " ");
							}
							
							StringBuilder pathGeneralReportes = new StringBuilder();
							pathGeneralReportes.append(FacesContext.getCurrentInstance()
									.getExternalContext().getRealPath("/"));
							pathGeneralReportes.append("/academico/reportes/archivosJasper/matriculasNivelacion");
							frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/logoUce.png");
							frmCrpParametros.put("imagenCodigoBarras", pathGeneralReportes+"/codigoBarras.png");
							frmCrpCampos.add(dato);
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/VoucherMatricula.jasper")));
							jasperPrint = JasperFillManager.fillReport(jasperReport,frmCrpParametros, new JREmptyDataSource());
							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
										
							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(fgmfUsuario.getUsrPersona().getPrsMailInstitucional());
							//path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo= new StringBuilder();
							formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
							fecha = formato.format(new Date());
							try {
								carreraAux = servFgmfCarreraServicio.buscarPorId(fgmfFichaInscripcionDto.getFcinCncrArea());
							} catch (CarreraNoEncontradoException e) {
							} catch (CarreraException e) {
							}
							Carrera carreraSiiu = new Carrera();
							try {
								carreraSiiu = servFgmfCarreraServicio.buscarPorId(fgmfFichaInscripcionDto.getFcinCarreraSiiu());
							} catch (CarreraNoEncontradoException e) {
							} catch (CarreraException e) {
							}
							sbCorreo= GeneralesUtilidades.generarAsuntoNivelacion(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									nombres, GeneralesUtilidades.generaStringParaCorreo(carreraAux.getCrrDescripcion()),"NIVELACION",
									String.valueOf(paraleloAux.getPrlCodigo())
									, facultadAux.getDpnDescripcion(),carreraSiiu.getCrrDescripcion());
							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_MATRICULA,
												sbCorreo.toString()
												, "admin", "dt1c201s", true, arreglo, "Comprobante."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
							String jsonSt = pmail.generarMail();
							Gson json = new Gson();
							MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
							// 	Iniciamos el envío de mensajes
							ObjectMessage message = session.createObjectMessage(mailDto);
							producer.send(message);
						} catch (Exception e) {
						}
							//******************************************************************************
							//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
							//******************************************************************************
												
					}else{
						//******************************************************************************
						//************************* ACA INICIA EL ENVIO DE MAIL ************************
						//******************************************************************************
						//defino los datos para la plantilla
						Map<String, Object> parametros = new HashMap<String, Object>();
						
						parametros.put("nombres", fgmfUsuario.getUsrPersona().getPrsNombres());
						StringBuilder cadenaNombres = new StringBuilder(); 
						cadenaNombres.append(fgmfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase());
						cadenaNombres.append(" ");
						if(fgmfUsuario.getUsrPersona().getPrsSegundoApellido()!=null){
							cadenaNombres.append(fgmfUsuario.getUsrPersona().getPrsSegundoApellido().toUpperCase());
						}
						parametros.put("apellidos",cadenaNombres); 
						
						SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
						parametros.put("fechaHora", sdf.format(new Date()));
						parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
						parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(fgmfMallaCurricular.getMlcrCarrera().getCrrDependencia().getDpnDescripcion()));
						parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(fgmfFichaInscripcionDto.getCrrDetalle())); 
						if(fgmfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE){
							parametros.put("nivel", GeneralesUtilidades.generaStringParaCorreo(FichaMatriculaConstantes.NIVELACION_LABEL));
						}else{
							
							if(fgmfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE){
								parametros.put("nivel", FichaMatriculaConstantes.PRIMER_NIVEL_LABEL);
							}
							
							
						}
						try {
							carreraAux = servFgmfCarreraServicio.buscarPorId(fgmfFichaInscripcionDto.getFcinCarreraSiiu());
						} catch (CarreraNoEncontradoException e) {
						} catch (CarreraException e) {
						}
						parametros.put("carrerasiiu", carreraAux.getCrrDescripcion());
						parametros.put("paralelo", GeneralesUtilidades.generaStringConTildes(fgmfListMateriaDto.get(0).getPrlDescripcion()));
						//lista de correos a los que se enviara el mail
						List<String> correosTo = new ArrayList<>();
						correosTo.add(fgmfUsuario.getUsrPersona().getPrsMailInstitucional());
						
						//path de la plantilla del mail
						String pathTemplate = "/ec/edu/uce/academico/jsf/velocity/plantillas/template-matriculacion.vm";
						
						//llamo al generador de mails
						GeneradorMails genMail = new GeneradorMails();
						String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, 
								GeneralesConstantes.APP_ASUNTO_MATRICULA, 
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
						
					}
				
				fgmfValidadorClic = new Integer(0);
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatricula.exitoso")));
				return "irConfirmacionMatricula";
				
				}else{
					Date fechaActualMatricula = new Date();
					//busqueda del período academico activo
						Calendar calendar = new GregorianCalendar(2018,9,12);
						if(fechaActualMatricula.before(calendar.getTime())){
							FacesUtil.mensajeError("El registro de matrícula para estudiantes que repiten es a partir del 11 de Abril del 2019");
							fgmfValidadorClic = new Integer(0);
							return null;
						}else{
							String numComprobante=null;
							numComprobante=servFgmfMatriculaServicio.generarMatricula(fgmfListMateriaDto, fgmfUsuario.getUsrPersona().getPrsId(), fgmfFichaInscripcionDto
									,FichaMatriculaConstantes.NIVELACION_VALUE //nivel ubicacion 
									, gratuidad 										 //tipo matricula
									, gratuidadTipo											 //tipo gratuidad
									, costoMatricula, fgmfMallaCurricular, fgmfEstudianteNuevo,fgmfPlanificacionCronograma, fgmfPeriodoAcademico);
						if(!numComprobante.equals("N/A")){
							StringBuilder pathGeneralImagenes = new StringBuilder();
							pathGeneralImagenes.append(FacesContext.getCurrentInstance()
									.getExternalContext().getRealPath("/"));
							pathGeneralImagenes.append("/academico/reportes/archivosJasper/matriculasNivelacion/codigoBarras.png");
								// Generamos el código de barras para el adjunto del voucher de pago
							try {
//								//Es el tipo de clase 
								Barcode128 code128 = new Barcode128();
								//Seteo el tipo de codigo
								code128.setCodeType(Barcode.CODE128);
								code128.setBarHeight(15f); 
								code128.setStartStopText(false);
								code128.setExtended(true);
								code128.setX(1f);
								//Setep el codigo
								code128.setCode(numComprobante);
								//Guardo la imagen
								java.awt.Image rawImage = code128.createAwtImage(Color.BLACK, Color.WHITE);
								BufferedImage outImage = new BufferedImage(rawImage.getWidth(null), rawImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
								outImage.getGraphics().drawImage(rawImage, 0, 0, null);
								ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
								ImageIO.write(outImage, "png", bytesOut);
								bytesOut.flush();
								byte[] data = bytesOut.toByteArray();
								
								BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
								File file = new File(pathGeneralImagenes.toString());
								if (file.exists()) {
									file.delete();     
								}
								file = new File(pathGeneralImagenes.toString());
								ImageIO.write(bi, "PNG", file);
							  }catch (java.io.IOException ex) {
							  }
							Dependencia facultadAux = null;
							try {
								facultadAux = servFgmfDependenciaServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
								generarReporteOrdenCobro(facultadAux.getDpnDescripcion(),numComprobante ,fgmfUsuario.getUsrPersona(),carreraAux);
							} catch (DependenciaNoEncontradoException e) {
							}
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
								List<Map<String, Object>> frmCrpCampos = null;
								Map<String, Object> frmCrpParametros = null;
								// ****************************************************************//
								// ***************** GENERACION DE LA ORDEN DE PAGO  *************//
								// ****************************************************************//
								// ****************************************************************//
								frmCrpParametros = new HashMap<String, Object>();
								int comparador = costoMatricula.compareTo(ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE);
								
								if(comparador==0){
									frmCrpParametros.put("textoNivelacion","MATRICULA SEGUNDA VEZ - NIVELACION");
								}else if(comparador==1){
									frmCrpParametros.put("textoNivelacion","MATRICULA TERCERA VEZ - NIVELACION");
								}else{
									frmCrpParametros.put("textoNivelacion","MATRICULA PRIMERA VEZ - NIVELACION");
								}
								frmCrpParametros.put("facultad",facultadAux.getDpnDescripcion());
								SimpleDateFormat formato = 
										new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
								String fecha = formato.format(new Date());
								Calendar c = Calendar.getInstance();
								c.setTime(new Date()); 
								c.add(Calendar.DATE, 4);
								String fechaCaducidad = formato.format(c.getTime());
								frmCrpParametros.put("fecha",fecha);
								frmCrpParametros.put("fechaCaducidad",fechaCaducidad);
								
								frmCrpParametros.put("numComprobante",numComprobante);
								frmCrpCampos = new ArrayList<Map<String, Object>>();
								Map<String, Object> dato = null;
								dato = new HashMap<String, Object>();
								String nombres=fgmfUsuario.getUsrPersona().getPrsNombres()+" "+GeneralesUtilidades.generaStringParaCorreo(fgmfUsuario.getUsrPersona().getPrsPrimerApellido()).toUpperCase()+" "
										+(fgmfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":GeneralesUtilidades.generaStringParaCorreo(fgmfUsuario.getUsrPersona().getPrsSegundoApellido()).toUpperCase());
								frmCrpParametros.put("carrera", carreraAux.getCrrDescripcion());
								frmCrpParametros.put("identificacion", fgmfUsuario.getUsrPersona().getPrsIdentificacion());
								
								frmCrpParametros.put("postulante", nombres);
								
								StringBuilder sb = new StringBuilder();
								if(fgmfUsuario.getUsrPersona().getPrsSectorDomicilio()!=null){
									sb.append(fgmfUsuario.getUsrPersona().getPrsSectorDomicilio());
									sb.append(" ");	
								}
								if(fgmfUsuario.getUsrPersona().getPrsCallePrincipal()!=null){
									sb.append(fgmfUsuario.getUsrPersona().getPrsCallePrincipal());
									sb.append(" ");	
								}
								if(fgmfUsuario.getUsrPersona().getPrsNumeroCasa()!=null){
									sb.append(fgmfUsuario.getUsrPersona().getPrsNumeroCasa());
									sb.append(" ");
								}
								if(fgmfUsuario.getUsrPersona().getPrsReferenciaDomicilio()!=null){
									sb.append(fgmfUsuario.getUsrPersona().getPrsReferenciaDomicilio());	
								}
								frmCrpParametros.put("direccion", sb.toString());
									frmCrpParametros.put("valorPagar", costoMatricula.toString()+".00");	
											
								frmCrpParametros.put("email", fgmfUsuario.getUsrPersona().getPrsMailInstitucional());
								if(fgmfUsuario.getUsrPersona().getPrsTelefono()!=null){
									frmCrpParametros.put("telefono", fgmfUsuario.getUsrPersona().getPrsTelefono());	
								}else{
									frmCrpParametros.put("telefono", " ");
								}
								
								StringBuilder pathGeneralReportes = new StringBuilder();
								pathGeneralReportes.append(FacesContext.getCurrentInstance()
										.getExternalContext().getRealPath("/"));
								pathGeneralReportes.append("/academico/reportes/archivosJasper/matriculasNivelacion");
								frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/logoUce.png");
								frmCrpParametros.put("imagenCodigoBarras", pathGeneralReportes+"/codigoBarras.png");
								frmCrpCampos.add(dato);
								jasperReport = (JasperReport) JRLoader.loadObject(new File(
										(pathGeneralReportes.toString() + "/VoucherMatricula.jasper")));
								jasperPrint = JasperFillManager.fillReport(jasperReport,frmCrpParametros, new JREmptyDataSource());
								byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
											
								//lista de correos a los que se enviara el mail
								List<String> correosTo = new ArrayList<>();
								correosTo.add(fgmfUsuario.getUsrPersona().getPrsMailInstitucional());
								//path de la plantilla del mail
								ProductorMailJson pmail = null;
								StringBuilder sbCorreo= new StringBuilder();
								formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
								fecha = formato.format(new Date());
								try {
									ConfiguracionCarrera cncrAux = servFgmfConfiguracionCarreraServicio.buscarXid(fgmfFichaInscripcionDto.getCncrId());
									
									carreraAux = servFgmfCarreraServicio.buscarPorId(cncrAux.getCncrCarrera().getCrrId());
								} catch (CarreraNoEncontradoException e) {
								} catch (CarreraException e) {
								}
								Carrera carreraSiiu = new Carrera();
								try {
									carreraSiiu = servFgmfCarreraServicio.buscarPorId(fgmfFichaInscripcionDto.getFcinCarreraSiiu());
								} catch (CarreraNoEncontradoException e) {
								} catch (CarreraException e) {
								}
								sbCorreo= GeneralesUtilidades.generarAsuntoNivelacion(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
										nombres, GeneralesUtilidades.generaStringParaCorreo(carreraAux.getCrrDescripcion()),"NIVELACION",
										String.valueOf(paraleloAux.getPrlCodigo())
										, facultadAux.getDpnDescripcion(),carreraSiiu.getCrrDescripcion());
								pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_MATRICULA,
													sbCorreo.toString()
													, "admin", "dt1c201s", true, arreglo, "Comprobante."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
								String jsonSt = pmail.generarMail();
								Gson json = new Gson();
								MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
								// 	Iniciamos el envío de mensajes
								ObjectMessage message = session.createObjectMessage(mailDto);
								producer.send(message);
							} catch (Exception e) {
								e.printStackTrace();
							}
								//******************************************************************************
								//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
								//******************************************************************************
													
						}else{
							//******************************************************************************
							//************************* ACA INICIA EL ENVIO DE MAIL ************************
							//******************************************************************************
							//defino los datos para la plantilla
							Map<String, Object> parametros = new HashMap<String, Object>();
							
							parametros.put("nombres", fgmfUsuario.getUsrPersona().getPrsNombres());
							StringBuilder cadenaNombres = new StringBuilder(); 
							cadenaNombres.append(fgmfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase());
							cadenaNombres.append(" ");
							if(fgmfUsuario.getUsrPersona().getPrsSegundoApellido()!=null){
								cadenaNombres.append(fgmfUsuario.getUsrPersona().getPrsSegundoApellido().toUpperCase());
							}
							parametros.put("apellidos",cadenaNombres); 
							
							SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
							parametros.put("fechaHora", sdf.format(new Date()));
							parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
							parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(fgmfMallaCurricular.getMlcrCarrera().getCrrDependencia().getDpnDescripcion()));
							parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(fgmfFichaInscripcionDto.getCrrDetalle())); 
							if(fgmfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE){
								parametros.put("nivel", FichaMatriculaConstantes.NIVELACION_LABEL);
							}else{
								
								//TODO:  Presentar  primer nivel en el mail: MQ
								if(fgmfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NORMAL_VALUE){
									parametros.put("nivel", FichaMatriculaConstantes.PRIMER_NIVEL_LABEL);
								}
								
								
							}
							try {
								carreraAux = servFgmfCarreraServicio.buscarPorId(fgmfFichaInscripcionDto.getFcinCarreraSiiu());
							} catch (CarreraNoEncontradoException e) {
							} catch (CarreraException e) {
							}
							parametros.put("carrerasiiu", carreraAux.getCrrDescripcion());
							parametros.put("paralelo", GeneralesUtilidades.generaStringConTildes(fgmfListMateriaDto.get(0).getPrlDescripcion()));
							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(fgmfUsuario.getUsrPersona().getPrsMailInstitucional());
							
							//path de la plantilla del mail
							String pathTemplate = "/ec/edu/uce/academico/jsf/velocity/plantillas/template-matriculacion.vm";
							
							//llamo al generador de mails
							GeneradorMails genMail = new GeneradorMails();
							String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, 
									GeneralesConstantes.APP_ASUNTO_MATRICULA, 
									GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
							//****envio el mail a la cola
							//cliente web service
							Client client = ClientBuilder.newClient();
							WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
							MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
							postForm.add("mail", mailjsonSt);
//							String responseData = target.request().post(Entity.form(postForm),String.class);
							target.request().post(Entity.form(postForm),String.class);
							
							//******************************************************************************
							//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
							//******************************************************************************
							
						}
					
					fgmfValidadorClic = new Integer(0);
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatricula.exitoso")));
					return "irConfirmacionMatricula";
						}
					
					
					
				}
					
					
			} catch (MatriculaValidacionException e) {
				e.printStackTrace();
				FacesUtil.mensajeInfo(e.getMessage());
				fgmfValidadorClic = new Integer(0);
				return null;
			} catch (MatriculaException e) {
				e.printStackTrace();
				FacesUtil.mensajeInfo(e.getMessage());
				fgmfValidadorClic = new Integer(0);
				return null;
			} catch (ValidacionMailException e) {
				e.printStackTrace();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatricula.validacion.mail.exception")));
				fgmfValidadorClic = new Integer(0);
				return "irConfirmacionMatricula";
			} catch (CronogramaActividadDtoJdbcException e) {
				e.printStackTrace();
			} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
				e.printStackTrace();
			} catch (PlanificacionCronogramaNoEncontradoException e) {
				e.printStackTrace();
			} catch (PlanificacionCronogramaException e) {
				e.printStackTrace();
			} catch (EstudianteMatriculaValidacionException e) {
				fgmfValidadorClic = new Integer(0);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
				return null;
			}
			
		}
		fgmfValidadorClic = new Integer(0);
		return "irInicio";
	}

	
	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/
	/**
	 * verifica que haga click en el boton generar matricula
	 */
	public String verificarClickGenerarMatricula(){
		fgmfValidadorClic = 1;
		return null;
	}
	
	
	
	/**
	* Genera el voucher de pago y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return void
	*/
	public void generarReporteOrdenCobro(String facultadDescripcion, String numComprobante, Persona persona, Carrera carrera){
		try {
			List<Map<String, Object>> frmCrpCampos = null;
			 Map<String, Object> frmCrpParametros = null;
			 String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DE LA ORDEN DE COBRO  *************//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "VoucherMatricula";
			frmCrpParametros = new HashMap<String, Object>();
			
			frmCrpParametros.put("facultad",facultadDescripcion);
			SimpleDateFormat formato = 
					new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); 
			c.add(Calendar.DATE, 4); 
			String fechaCaducidad = formato.format(c.getTime());
			frmCrpParametros.put("fecha",fecha);
			frmCrpParametros.put("fechaCaducidad",fechaCaducidad);
			
			frmCrpParametros.put("numComprobante",numComprobante);
			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
				dato = new HashMap<String, Object>();
				frmCrpParametros.put("carrera", carrera.getCrrDescripcion());
//				if(item.getPstDireccion()!=null){
					frmCrpParametros.put("identificacion", persona.getPrsIdentificacion());
//					/////////////////////////////////////////////////////////////////////////////////
//					frmCrpParametros.put("direccion", "NO HAY");
					frmCrpParametros.put("postulante", persona.getPrsPrimerApellido()+" "+persona.getPrsSegundoApellido()+" "+persona.getPrsNombres());
//				}else{
//					frmCrpParametros.put("postulante", "CONSUMIDOR FINAL");
//					frmCrpParametros.put("identificacion", "9999999999");
					frmCrpParametros.put("direccion", "S/N");
//				}
				frmCrpParametros.put("email", persona.getPrsMailPersonal());
				frmCrpParametros.put("telefono", persona.getPrsTelefono());
				
				StringBuilder pathGeneralReportes = new StringBuilder();
				pathGeneralReportes.append(FacesContext.getCurrentInstance()
						.getExternalContext().getRealPath("/"));
				pathGeneralReportes.append("/academico/reportes/archivosJasper/matriculaPosgrado");
				frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/logoUce.png");
				frmCrpParametros.put("imagenCodigoBarras", pathGeneralReportes+"/codigoBarras.png");
				frmCrpCampos.add(dato);
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmCrpParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getFgmfUsuario() {
		return fgmfUsuario;
	}

	public void setFgmfUsuario(Usuario fgmfUsuario) {
		this.fgmfUsuario = fgmfUsuario;
	}

	public List<FichaInscripcionDto> getFmfListFichaInscripcionDto() {
		fgmfListFichaInscripcionDto = fgmfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmfListFichaInscripcionDto;
		return fgmfListFichaInscripcionDto;
	}

	public void setFmfListFichaInscripcionDto(List<FichaInscripcionDto> fgmfListFichaInscripcionDto) {
		this.fgmfListFichaInscripcionDto = fgmfListFichaInscripcionDto;
	}

	public List<CarreraDto> getFmfListCarreraDto() {
		fgmfListCarreraDto = fgmfListCarreraDto==null?(new ArrayList<CarreraDto>()):fgmfListCarreraDto;
		return fgmfListCarreraDto;
	}

	public void setFmfListCarreraDto(List<CarreraDto> fgmfListCarreraDto) {
		this.fgmfListCarreraDto = fgmfListCarreraDto;
	}

	public List<FichaInscripcionDto> getFgmfListFichaInscripcionDto() {
		fgmfListFichaInscripcionDto = fgmfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmfListFichaInscripcionDto;
		return fgmfListFichaInscripcionDto;
	}

	public void setFgmfListFichaInscripcionDto(List<FichaInscripcionDto> fgmfListFichaInscripcionDto) {
		this.fgmfListFichaInscripcionDto = fgmfListFichaInscripcionDto;
	}

	public List<CarreraDto> getFgmfListCarreraDto() {
		fgmfListCarreraDto = fgmfListCarreraDto==null?(new ArrayList<CarreraDto>()):fgmfListCarreraDto;
		return fgmfListCarreraDto;
	}

	public void setFgmfListCarreraDto(List<CarreraDto> fgmfListCarreraDto) {
		this.fgmfListCarreraDto = fgmfListCarreraDto;
	}

	public List<RecordEstudianteDto> getFgmfListRecordEstudiante() {
		fgmfListRecordEstudiante = fgmfListRecordEstudiante==null?(new ArrayList<RecordEstudianteDto>()):fgmfListRecordEstudiante;
		return fgmfListRecordEstudiante;
	}

	public void setFgmfListRecordEstudiante(List<RecordEstudianteDto> fgmfListRecordEstudiante) {
		this.fgmfListRecordEstudiante = fgmfListRecordEstudiante;
	}

	public Boolean getFgmfEstudianteNuevo() {
		return fgmfEstudianteNuevo;
	}

	public void setFgmfEstudianteNuevo(Boolean fgmfEstudianteNuevo) {
		this.fgmfEstudianteNuevo = fgmfEstudianteNuevo;
	}

	public MallaCurricular getFgmfMallaCurricular() {
		return fgmfMallaCurricular;
	}

	public void setFgmfMallaCurricular(MallaCurricular fgmfMallaCurricular) {
		this.fgmfMallaCurricular = fgmfMallaCurricular;
	}

	public List<MateriaDto> getFgmfListMateriaDto() {
		fgmfListMateriaDto = fgmfListMateriaDto==null?(new ArrayList<MateriaDto>()):fgmfListMateriaDto;
		return fgmfListMateriaDto;
	}

	public void setFgmfListMateriaDto(List<MateriaDto> fgmfListMateriaDto) {
		this.fgmfListMateriaDto = fgmfListMateriaDto;
	}
	
	
	public List<RecordEstudianteDto> getFgmfListMateriaDtoAprobadas() {
		fgmfListMateriaDtoAprobadas = fgmfListMateriaDtoAprobadas==null?(new ArrayList<RecordEstudianteDto>()):fgmfListMateriaDtoAprobadas;
		return fgmfListMateriaDtoAprobadas;
	}

	public void setFgmfListMateriaDtoAprobadas(List<RecordEstudianteDto> fgmfListMateriaDtoAprobadas) {
		this.fgmfListMateriaDtoAprobadas = fgmfListMateriaDtoAprobadas;
	}

	public List<RecordEstudianteDto> getFgmfListMateriaDtoReprobadas() {
		fgmfListMateriaDtoReprobadas = fgmfListMateriaDtoReprobadas==null?(new ArrayList<RecordEstudianteDto>()):fgmfListMateriaDtoReprobadas;
		return fgmfListMateriaDtoReprobadas;
	}

	public void setFgmfListMateriaDtoReprobadas(List<RecordEstudianteDto> fgmfListMateriaDtoReprobadas) {
		this.fgmfListMateriaDtoReprobadas = fgmfListMateriaDtoReprobadas;
	}
	
	
	
	public List<ParaleloDto> getFgmfListParaleloDto() {
		fgmfListParaleloDto = fgmfListParaleloDto==null?(new ArrayList<ParaleloDto>()):fgmfListParaleloDto;
		return fgmfListParaleloDto;
	}

	public void setFgmfListParaleloDto(List<ParaleloDto> fgmfListParaleloDto) {
		this.fgmfListParaleloDto = fgmfListParaleloDto;
	}

	public PeriodoAcademico getFgmfPeriodoAcademico() {
		return fgmfPeriodoAcademico;
	}

	public void setFgmfPeriodoAcademico(PeriodoAcademico fgmfPeriodoAcademico) {
		this.fgmfPeriodoAcademico = fgmfPeriodoAcademico;
	}

	public FichaInscripcionDto getFgmfFichaInscripcionDto() {
		return fgmfFichaInscripcionDto;
	}

	public void setFgmfFichaInscripcionDto(FichaInscripcionDto fgmfFichaInscripcionDto) {
		this.fgmfFichaInscripcionDto = fgmfFichaInscripcionDto;
	}

	public Integer getFgmfValidadorClic() {
		return fgmfValidadorClic;
	}

	public void setFgmfValidadorClic(Integer fgmfValidadorClic) {
		this.fgmfValidadorClic = fgmfValidadorClic;
	}

	public PlanificacionCronograma getFgmfPlanificacionCronograma() {
		return fgmfPlanificacionCronograma;
	}

	public void setFgmfPlanificacionCronograma(PlanificacionCronograma fgmfPlanificacionCronograma) {
		this.fgmfPlanificacionCronograma = fgmfPlanificacionCronograma;
	}

	public CronogramaActividadJdbcDto getFgmfCronogramaActividadMatriculaOrdinaria() {
		return fgmfCronogramaActividadMatriculaOrdinaria;
	}

	public void setFgmfCronogramaActividadMatriculaOrdinaria(
			CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaOrdinaria) {
		this.fgmfCronogramaActividadMatriculaOrdinaria = fgmfCronogramaActividadMatriculaOrdinaria;
	}

	public CronogramaActividadJdbcDto getFgmfCronogramaActividadMatriculaEstraordinaria() {
		return fgmfCronogramaActividadMatriculaExtraordinaria;
	}

	public void setFgmfCronogramaActividadMatriculaEstraordinaria(
			CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaEstraordinaria) {
		this.fgmfCronogramaActividadMatriculaExtraordinaria = fgmfCronogramaActividadMatriculaEstraordinaria;
	}

	public CronogramaActividadJdbcDto getFgmfCronogramaActividadMatriculaEspecial() {
		return fgmfCronogramaActividadMatriculaEspecial;
	}

	public void setFgmfCronogramaActividadMatriculaEspecial(
			CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaEspecial) {
		this.fgmfCronogramaActividadMatriculaEspecial = fgmfCronogramaActividadMatriculaEspecial;
	}

	public CronogramaActividadJdbcDto getFgmfCronogramaActividadMatriculaExtraordinaria() {
		return fgmfCronogramaActividadMatriculaExtraordinaria;
	}

	public void setFgmfCronogramaActividadMatriculaExtraordinaria(
			CronogramaActividadJdbcDto fgmfCronogramaActividadMatriculaExtraordinaria) {
		this.fgmfCronogramaActividadMatriculaExtraordinaria = fgmfCronogramaActividadMatriculaExtraordinaria;
	}

	public List<CarreraDto> getFgmfListMateriasCarrera() {
		return fgmfListMateriasCarrera;
	}

	public void setFgmfListMateriasCarrera(List<CarreraDto> fgmfListMateriasCarrera) {
		this.fgmfListMateriasCarrera = fgmfListMateriasCarrera;
	}

	public List<MateriaDto> getFgmfListMateriasEstado() {
		return fgmfListMateriasEstado;
	}

	public void setFgmfListMateriasEstado(List<MateriaDto> fgmfListMateriasEstado) {
		this.fgmfListMateriasEstado = fgmfListMateriasEstado;
	}

	public Dependencia getFgmfDependenciaBuscar() {
		return fgmfDependenciaBuscar;
	}

	public void setFgmfDependenciaBuscar(Dependencia fgmfDependenciaBuscar) {
		this.fgmfDependenciaBuscar = fgmfDependenciaBuscar;
	}

	public CronogramaActividadJdbcDto getFgmfFechasMatriculaOrdinaria() {
		return fgmfFechasMatriculaOrdinaria;
	}

	public void setFgmfFechasMatriculaOrdinaria(CronogramaActividadJdbcDto fgmfFechasMatriculaOrdinaria) {
		this.fgmfFechasMatriculaOrdinaria = fgmfFechasMatriculaOrdinaria;
	}

	public CronogramaActividadJdbcDto getFgmfFechasMatriculaExtraordinaria() {
		return fgmfFechasMatriculaExtraordinaria;
	}

	public void setFgmfFechasMatriculaExtraordinaria(CronogramaActividadJdbcDto fgmfFechasMatriculaExtraordinaria) {
		this.fgmfFechasMatriculaExtraordinaria = fgmfFechasMatriculaExtraordinaria;
	}

	public CronogramaActividadJdbcDto getFgmfFechasMatriculaEspecial() {
		return fgmfFechasMatriculaEspecial;
	}

	public void setFgmfFechasMatriculaEspecial(CronogramaActividadJdbcDto fgmfFechasMatriculaEspecial) {
		this.fgmfFechasMatriculaEspecial = fgmfFechasMatriculaEspecial;
	}

	public int getValidadorNuevo() {
		return validadorNuevo;
	}

	public void setValidadorNuevo(int validadorNuevo) {
		this.validadorNuevo = validadorNuevo;
	}

	public int getGratuidad() {
		return gratuidad;
	}

	public void setGratuidad(int gratuidad) {
		this.gratuidad = gratuidad;
	}

	public CronogramaActividadJdbcDto getFgmfProcesoFlujo() {
		return fgmfProcesoFlujo;
	}

	public void setFgmfProcesoFlujo(CronogramaActividadJdbcDto fgmfProcesoFlujo) {
		this.fgmfProcesoFlujo = fgmfProcesoFlujo;
	}

	
	
}
