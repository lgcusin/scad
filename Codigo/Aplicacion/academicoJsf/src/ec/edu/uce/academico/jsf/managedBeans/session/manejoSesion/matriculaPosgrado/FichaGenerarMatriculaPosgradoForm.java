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
   
 ARCHIVO:     FichaMatriculaPosgradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-08-2017			 Daniel Albuja                         Emisión Inicial
 30-03-2017			 Gabriel Mafla                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matriculaPosgrado;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.imageio.ImageIO;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
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
 * Clase (session bean) FichaMatriculaPosgradoForm. 
 * Bean de sesion que maneja la matricula del estudiante.
 * @author jdalbuja.
 * @version 1.0
 */

@ManagedBean(name = "fichaGenerarMatriculaPosgradoForm")
@SessionScoped
public class FichaGenerarMatriculaPosgradoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario fgmpfUsuario;
	private List<FichaInscripcionDto> fgmpfListFichaInscripcionDto;
	private List<CarreraDto> fgmpfListCarreraDto;
	private List<RecordEstudianteDto> fgmpfListRecordEstudiante;
	private Boolean fgmpfEstudianteNuevo;
	private MallaCurricular fgmpfMallaCurricular;
	private List<MateriaDto> fgmpfListMateriaDto;
	private List<ParaleloDto> fgmpfListParaleloDto;
	private PeriodoAcademico fgmpfPeriodoAcademico;
	private FichaInscripcionDto fgmpfFichaInscripcionDto;
	private Integer fgmpfValidadorClic;
	private PlanificacionCronograma fgmpfPlanificacionCronograma;
//	private CarreraDto fgmpfCarreraDto ;
	private Integer fgmpfBloqueoModal;  // 1.- no   0.- si   para evitar que recarguen la página y se pueda generar la matricula una y otra vez
	private boolean fgmpfBloqueoGenerar;
	private String fgmpfmensajeModal;
	
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
	FichaInscripcionDtoServicioJdbc servFgmpfFichaInscripcionDto;
	@EJB 
	RecordEstudianteDtoServicioJdbc servFgmpfRecordEstudianteDto;
	@EJB 
	MallaCurricularServicio servFgmpfMallaCurricular;
	@EJB 
	MateriaDtoServicioJdbc servFgmpfMateriaDto;
	@EJB 
	ParaleloDtoServicioJdbc servFgmpfParaleloDto;
	@EJB 
	PeriodoAcademicoServicio servFgmpfPeriodoAcademico;
	@EJB 
	MatriculaServicio servFgmpfMatriculaServicio;
	@EJB 
	FichaMatriculaDtoServicioJdbc servFgmpfFichaMatriculaDto;
	@EJB 
	CronogramaServicio servFgmpfCronograma;
	@EJB 
	ProcesoFlujoServicio servFgmpfProcesoFlujo;
	@EJB 
	PlanificacionCronogramaServicio servFgmpfPlanificacionCronograma;
	@EJB 
	CronogramaProcesoFlujoServicio servFgmpfCronogramaProcesoFlujo;
	@EJB 
	CarreraServicio servFgmpfCarreraServicio;
	@EJB 
	DependenciaServicio servFgmpfDependenciaServicio;

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula de posgrado
	 * @return navegacion al listar matricula de posgrado
	 */
	public String iniciarMatriculaPosgrado(Usuario usuario) {
		try {
			fgmpfBloqueoModal = 1;
			//Guardamos el usuario en una variable
			fgmpfUsuario = usuario;
			//listo las fichas inscripciones activas del usuario de posgrado
			fgmpfListFichaInscripcionDto = servFgmpfFichaInscripcionDto.buscarEstudiantePosgradoPidentificacionPfcinEstado(fgmpfUsuario.getUsrPersona().getPrsIdentificacion(), FichaInscripcionConstantes.ACTIVO_VALUE);
			//validador click en 0
			fgmpfValidadorClic = new Integer(0);
			
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.iniciarGenerarMatricula.ficha.inscripcion.dto.exception")));
			return null;
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.iniciarGenerarMatriculaPosgrado.ficha.inscripcion.dto.no.encontrado.exception")));
			return null;
		}
		return "irMatriculaPosgrado";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		fgmpfUsuario = null;
		fgmpfListFichaInscripcionDto = null;
		fgmpfListCarreraDto = null;
		fgmpfValidadorClic = new Integer(0);
		//debloqueo el modal
		fgmpfBloqueoModal = 1;
		return "irInicio";
	}
	
	/**
	 * Metodo que permite dirige a la página de generar matricula de posgrado
	 * @return  Navegacion a la pagina de generar matricula de posgrado
	 */
	public String irGenerarMatriculaPosgrado(FichaInscripcionDto fichaInscripcionDto){
		try {
			//guardo la fichaInscripcion seleccionada
			fgmpfFichaInscripcionDto = fichaInscripcionDto;
			
			//busqueda del período academico activo
			fgmpfPeriodoAcademico = servFgmpfPeriodoAcademico.buscarPorId(fgmpfFichaInscripcionDto.getPracId());
			
			// creo la lista de proceso flujo correspondiente a la matricula
			List<Integer> listProcesoFlujoAux = new ArrayList<Integer>();
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
			
			//listo planificaciones cronograma de matriculas
			List<PlanificacionCronograma>listPlanificacionCronogramaAux;
			
			listPlanificacionCronogramaAux = servFgmpfPlanificacionCronograma.buscarPlcrPosgradoXestadoCrnXestadoPracXprocesoFlujoXPracId(fgmpfFichaInscripcionDto.getCrrId(),CronogramaConstantes.ESTADO_ACTIVO_VALUE,CronogramaConstantes.TIPO_POSGRADO_VALUE,PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE,listProcesoFlujoAux, fgmpfFichaInscripcionDto.getPracId());
			
			//validacion de las fechas del cronograma
			Timestamp fechaHoy = new Timestamp(new Date().getTime());
			int contPlanificacionCronograma = 0;
			int contadorInactivos = 0;
			boolean op= true;
			
			for (PlanificacionCronograma item : listPlanificacionCronogramaAux) {
				contPlanificacionCronograma++;
				if(item.getPlcrEstado() == PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE){
					fgmpfPlanificacionCronograma = item;
//					System.out.println(item.getPlcrFechaInicio());
//					System.out.println(item.getPlcrFechaFin());
					if((fechaHoy.after(item.getPlcrFechaInicio()) && fechaHoy.before(item.getPlcrFechaFin()))){
						op=false;
					}
				}else{
					contadorInactivos++;
				}
				if(contPlanificacionCronograma == listPlanificacionCronogramaAux.size() && contadorInactivos == listPlanificacionCronogramaAux.size()){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.cronograma.validacion.exception")));
					return null;
				}
			}
			if(op){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.cronograma.validacion.exception")));
				return null;	
			}
			
			try {
				//validadcion de si tiene matricula generada
				List<FichaMatriculaDto> fichaMatriculaDtoAux = servFgmpfFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(fgmpfPeriodoAcademico.getPracId(), fgmpfUsuario.getUsrIdentificacion(), fgmpfFichaInscripcionDto.getCrrId()); 
				if(fichaMatriculaDtoAux.size() > 0){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.matricula.generada.exception",fgmpfFichaInscripcionDto.getCrrDetalle())));
					return null;
				}
			} catch (Exception e) {
			}
			
//			//validacion de encuesta llena
//			if(fgmpfFichaInscripcionDto.getFcinEncuesta() == FichaInscripcionConstantes.NO_ENCUESTA_LLENA_VALUE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.encuesta.validacion.exception")));
//				return null;
//			}
			
			//busco el record del estudiante
			fgmpfListRecordEstudiante = servFgmpfRecordEstudianteDto.buscarXidentificacionXcarrera(fgmpfUsuario.getUsrIdentificacion(), fgmpfFichaInscripcionDto.getCrrId());

	
			//Buscar la malla curricular vigente y activa de la carrera que quiere generar la matricula
			fgmpfMallaCurricular = servFgmpfMallaCurricular.buscarXcarreraXvigenciaXestado(fgmpfFichaInscripcionDto.getCrrId(), MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
			
			
			if(fgmpfListRecordEstudiante.size() == 0){//es estudiante nuevo
				
				
				// BUSCAR MATERIAS en la malla de primer nivel de posgrado: MQ
					if(fgmpfFichaInscripcionDto.getFcinTipo().equals(FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE)){
					fgmpfListMateriaDto = servFgmpfMateriaDto.listarXmallaXnivel(fgmpfMallaCurricular.getMlcrId(),NivelConstantes.NIVEL_PRIMERO_VALUE);
					
				}
				
				//seteo variable de estudiante nuevo
				fgmpfEstudianteNuevo = true;
				
				//lleno los paralelos de las materias
				for (MateriaDto itmMateria : fgmpfListMateriaDto) {
					//busco los paralelos de esa materia
					fgmpfListParaleloDto = servFgmpfParaleloDto.listarXmallaMateriaXperiodo(itmMateria.getMlcrmtId(), fgmpfPeriodoAcademico.getPracId());
					//guardo los paralelos de esa materia
					itmMateria.setListParalelos(fgmpfListParaleloDto);
					// seteo la variable de seleccion ya que los estudiantes de posgrado cogen todas las materias obligatoriamente
					itmMateria.setIsSeleccionado(true); 
					// seteo el numero de matricula a primera
					itmMateria.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
				}
				
			}else{//es estudiante viejo
				
				//determino si la inscripcion es para nivelacion o para carrera
				if(fgmpfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_NIVELACION_VALUE){
					
					//buscar las materias de nivelacion de esa malla
					fgmpfListMateriaDto = servFgmpfMateriaDto.listarXmallaXnivel(fgmpfMallaCurricular.getMlcrId(), NivelConstantes.NIVEL_NIVELACION_VALUE);
					
					//estudiante de nivelación
					//seteo variable de estudiante nuevo
					fgmpfEstudianteNuevo = false;
					
					int contMateria = 0;
					Boolean encontro = false;
					//determinar materias a matricularse
				for (RecordEstudianteDto itemRecord : fgmpfListRecordEstudiante) {
						if(itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_APROBADO_VALUE){
							
							for (MateriaDto itemMateria : fgmpfListMateriaDto) {
								contMateria++;
								if(itemRecord.getMlcrmtMallaCurricularMateria().intValue() == itemMateria.getMlcrmtId()){
									encontro = true;
									break;
								}
							}
						}
						if(encontro == true){
							fgmpfListMateriaDto.remove(contMateria-1);
							contMateria = 0;
							encontro=false; //faltaba colocar: MQ
						}
					}
					//determinar segundas o terceras matriculas
					for (MateriaDto itmMateria : fgmpfListMateriaDto) {
						//busco los paralelos de esa materia
						fgmpfListParaleloDto = servFgmpfParaleloDto.listarXmallaMateriaXperiodo(itmMateria.getMlcrmtId(), fgmpfPeriodoAcademico.getPracId());
						//guardo los paralelos de esa materia
						itmMateria.setListParalelos(fgmpfListParaleloDto);
						// seteo la variable de seleccion ya que los estudiantes nuevos cogen todas las materias obligatoriamente
						itmMateria.setIsSeleccionado(true); 
						// calcular el numero de matricula
						int contMatricula = 0;
						for (RecordEstudianteDto itemRecord : fgmpfListRecordEstudiante) {
							if(itemRecord.getMlcrmtMallaCurricularMateria().intValue() == itmMateria.getMlcrmtId() && itemRecord.getRcesEstado().intValue() == RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
								contMatricula++;
							}
						}
						if(contMatricula == 0){
							itmMateria.setNumMatricula(DetalleMatriculaConstantes.PRIMERA_MATRICULA_VALUE);
						}
						if(contMatricula == 1){
							itmMateria.setNumMatricula(DetalleMatriculaConstantes.SEGUNDA_MATRICULA_VALUE);
						}
						if(contMatricula == 2){
							itmMateria.setNumMatricula(DetalleMatriculaConstantes.TERCERA_MATRICULA_VALUE);
						}
					}
				} else{
				}
			}

		} catch (Exception e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.planificacion.cronograma.exception")));
			return null;
		} 
		return "irGenerarMatriculaPosgrado";
	}
	
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String cancelarGenerarMatricula(){
		
		fgmpfFichaInscripcionDto = new FichaInscripcionDto();
		fgmpfPeriodoAcademico = new PeriodoAcademico();
		fgmpfListRecordEstudiante = null;
		fgmpfEstudianteNuevo = new Boolean(false);
		fgmpfMallaCurricular = new MallaCurricular();
		fgmpfListMateriaDto = null;
		fgmpfListParaleloDto = null;
		fgmpfValidadorClic = new Integer(0);
		fgmpfBloqueoModal = 1;
		irInicio();
		return "cancelarMatricula";
	}
	
	
	/**
	 * Busca Matriculas según los parámetros ingresados en el panel de búsqueda 
	 */
	public String generarMatricula(){
		if(fgmpfBloqueoModal == 1){
			fgmpfBloqueoModal = 0;
			try {
//				if(fgmpfEstudianteNuevo){ //estudiante nuevo  asignacion de 1 cupo por todas las materias
					//Asignación de paralelos
				//TODO BUSCAR CUPOS POR MALLA CURRICULAR 
					ParaleloDto paraleloAux = new ParaleloDto();
					try {
						paraleloAux = servFgmpfParaleloDto.listarXMallaCurricularXCarreraXEstadoActivo(fgmpfFichaInscripcionDto.getCrrId());
					} catch (ParaleloDtoException | ParaleloDtoNoEncontradoException e) {
					}
//					for (ParaleloDto paraleloDtoItem : fgmpfListParaleloDto) { // recorro los paralelos disponibles
//						if(paraleloDtoItem.getMlcrprInscritos().intValue() < paraleloDtoItem.getPrlCupo().intValue()){ // si el cupo no esta lleno
//							paraleloAux = paraleloDtoItem; // guardo el paralelo con cupo disponible
//							break;
//						}
//					} 
//					
					if(paraleloAux != null){ // si existio paralelo disponible
						for (MateriaDto materiasItem : fgmpfListMateriaDto) { // recorro la lista de materias 
							materiasItem.setPrlId(paraleloAux.getPrlId()); // asigno el paralelo a las materias
							materiasItem.setPrlDescripcion(paraleloAux.getPrlDescripcion());
							materiasItem.setMlcrprId(paraleloAux.getMlcrprId()); //
							
							//busco la malla curricular paralelo
							
							//asignar 1 solo cupo a ese paralelo 
						}
					}else{ // si no exite cupos disponibles en ningun paralelo
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatricula.paralelo.validacion.exception")));
					}
					Carrera carreraAux = new Carrera();
					try {
						carreraAux = servFgmpfCarreraServicio.buscarPorId(fgmpfFichaInscripcionDto.getCrrId());
					} catch (CarreraNoEncontradoException e) {
					} catch (CarreraException e) {
					}
					//nivel ubicacion se setea con NIVELACION ya que son estudiantes nuevos
					//tipo matricula calcular con el cronograma
					//tipo gratuidad se envia con tiene gratuidad ya que es estudiante nuevo
					String numComprobante=null;
					BigDecimal arancelPosgrado= new BigDecimal(0);
					arancelPosgrado=carreraAux.getCrrArancel();
					//Se pasa por matrículas extraordinarias
//					 Date date1 = new GregorianCalendar(2018, Calendar.APRIL, 20).getTime();
//						Date todayDate = new Date();
//				        if(todayDate.after(date1)){
//				        	if(fgmpfFichaInscripcionDto.getCrrId()==152){
//				        		arancelPosgrado=new BigDecimal(7730.10);
//							}else if(fgmpfFichaInscripcionDto.getCrrId()==153){
//				        		arancelPosgrado=new BigDecimal(7525);
//							}else if(fgmpfFichaInscripcionDto.getCrrId()==154){
//								arancelPosgrado=new BigDecimal(3533.20);
//							}
//				        }
					if(fgmpfFichaInscripcionDto.getFcinTipo().intValue() == FichaInscripcionConstantes.TIPO_INSCRITO_POSGRADO_VALUE){
						numComprobante = servFgmpfMatriculaServicio.generarMatriculaPosgrado(fgmpfListMateriaDto, fgmpfUsuario.getUsrPersona().getPrsId(), fgmpfFichaInscripcionDto
								,FichaMatriculaConstantes.POSGRADO_PRIMER_NIVEL_VALUE //nivel ubicacion 
								, 0 										 //tipo matricula
								, 3											 //tipo gratuidad
								, arancelPosgrado, fgmpfMallaCurricular, fgmpfEstudianteNuevo,fgmpfPlanificacionCronograma);
					}
//					if(fgmpfFichaInscripcionDto.getCrrId()<109 || fgmpfFichaInscripcionDto.getCrrId()>129){
						StringBuilder pathGeneralImagenes = new StringBuilder();
						pathGeneralImagenes.append(FacesContext.getCurrentInstance()
								.getExternalContext().getRealPath("/"));
						pathGeneralImagenes.append("/academico/reportes/archivosJasper/matriculaPosgrado/codigoBarras.png");
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
							facultadAux = servFgmpfDependenciaServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
							generarReporteOrdenCobro(facultadAux.getDpnDescripcion(),numComprobante ,fgmpfUsuario.getUsrPersona(),carreraAux);
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
							String nombres=GeneralesUtilidades.generaStringParaCorreo(fgmpfUsuario.getUsrPersona().getPrsNombres())+" "+
									GeneralesUtilidades.generaStringParaCorreo(fgmpfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase())+" "
									+(fgmpfUsuario.getUsrPersona().getPrsSegundoApellido() == null?" ":GeneralesUtilidades.generaStringParaCorreo(fgmpfUsuario.getUsrPersona().getPrsSegundoApellido()));
							frmCrpParametros.put("carrera", carreraAux.getCrrDescripcion());
							frmCrpParametros.put("identificacion", fgmpfUsuario.getUsrPersona().getPrsIdentificacion());
							
							frmCrpParametros.put("postulante", nombres);
							
							StringBuilder sb = new StringBuilder();
//							if(fgmpfUsuario.getUsrPersona().getPrsSectorDomicilio()!=null){
							
							try {
								if(fgmpfUsuario.getUsrPersona().getPrsSectorDomicilio().trim().equals("NULL")
										|| fgmpfUsuario.getUsrPersona().getPrsSectorDomicilio().trim().equals("null")
										|| fgmpfUsuario.getUsrPersona().getPrsSectorDomicilio().trim().equals("(null)")
										|| fgmpfUsuario.getUsrPersona().getPrsSectorDomicilio().trim().equals("Null")){
									sb.append(" N/A");	
								}else{
									sb.append(fgmpfUsuario.getUsrPersona().getPrsSectorDomicilio());
									sb.append(" ");	
								}
							} catch (Exception e) {
								sb.append(" N/A");	
							}
								
//							}	
//							if(fgmpfUsuario.getUsrPersona().getPrsCallePrincipal()!=null){
//								if(fgmpfUsuario.getUsrPersona().getPrsCallePrincipal().trim().equals("NULL")
//										|| fgmpfUsuario.getUsrPersona().getPrsCallePrincipal().trim().equals("null")
//										|| fgmpfUsuario.getUsrPersona().getPrsCallePrincipal().trim().equals("(null)")
//										|| fgmpfUsuario.getUsrPersona().getPrsCallePrincipal().trim().equals("Null")){
//								}else{
//									sb.append(fgmpfUsuario.getUsrPersona().getPrsCallePrincipal());
//									sb.append(" ");	
//								}
//							}
								
//							if(fgmpfUsuario.getUsrPersona().getPrsNumeroCasa()!=null){
//								if( fgmpfUsuario.getUsrPersona().getPrsNumeroCasa().trim().equals("NULL")
//										|| fgmpfUsuario.getUsrPersona().getPrsNumeroCasa().trim().equals("null")
//										|| fgmpfUsuario.getUsrPersona().getPrsNumeroCasa().trim().equals("(null)")
//										|| fgmpfUsuario.getUsrPersona().getPrsNumeroCasa().trim().equals("Null")){
//									
//								}else{
//									sb.append(fgmpfUsuario.getUsrPersona().getPrsNumeroCasa());
//									sb.append(" ");
//								}
//							}
//								
//							if(fgmpfUsuario.getUsrPersona().getPrsReferenciaDomicilio()!=null){
//								if( fgmpfUsuario.getUsrPersona().getPrsReferenciaDomicilio().trim().equals("NULL")
//										|| fgmpfUsuario.getUsrPersona().getPrsReferenciaDomicilio().trim().equals("null")
//										|| fgmpfUsuario.getUsrPersona().getPrsReferenciaDomicilio().trim().equals("(null)")
//										|| fgmpfUsuario.getUsrPersona().getPrsReferenciaDomicilio().trim().equals("Null")){
//									
//								}else{
//									sb.append(fgmpfUsuario.getUsrPersona().getPrsReferenciaDomicilio());	
//								}
//							}
								
							frmCrpParametros.put("direccion", sb.toString());
							//BECADA FACULTAD MEDICINA
							if(fgmpfUsuario.getUsrPersona().getPrsIdentificacion().equals("1713915682")){
								frmCrpParametros.put("valorPagar", "00.00");
							}else{
								frmCrpParametros.put("valorPagar", arancelPosgrado.setScale(2, RoundingMode.HALF_UP).toString());	
							}
										
							frmCrpParametros.put("email", fgmpfUsuario.getUsrPersona().getPrsMailInstitucional());
							frmCrpParametros.put("telefono", fgmpfUsuario.getUsrPersona().getPrsTelefono());
							StringBuilder pathGeneralReportes = new StringBuilder();
							pathGeneralReportes.append(FacesContext.getCurrentInstance()
									.getExternalContext().getRealPath("/"));
							pathGeneralReportes.append("/academico/reportes/archivosJasper/matriculaPosgrado");
							frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/logoUce.png");
							frmCrpParametros.put("imagenCodigoBarras", pathGeneralReportes+"/codigoBarras.png");
							frmCrpCampos.add(dato);
							jasperReport = (JasperReport) JRLoader.loadObject(new File(
									(pathGeneralReportes.toString() + "/VoucherMatricula.jasper")));
							jasperPrint = JasperFillManager.fillReport(jasperReport,frmCrpParametros, new JREmptyDataSource());
							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
										
							//lista de correos a los que se enviara el mail
							List<String> correosTo = new ArrayList<>();
							correosTo.add(fgmpfUsuario.getUsrPersona().getPrsMailInstitucional());
							//path de la plantilla del mail
							ProductorMailJson pmail = null;
							StringBuilder sbCorreo= new StringBuilder();
							formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
							fecha = formato.format(new Date());
							sbCorreo= GeneralesUtilidades.generarAsuntoPosgrado(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
									nombres, GeneralesUtilidades.generaStringParaCorreo(carreraAux.getCrrDescripcion()),"PRIMERO",
									String.valueOf(paraleloAux.getPrlCodigo())
									, facultadAux.getDpnDescripcion());
							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_MATRICULA_POSGRADO,
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
						////////// Solo para Posgrados de la Facultad de Medicina CASOS BECADOS Y ESPECIALES
//					}else{
//						
//						//******************************************************************************
//						//************************* ACA INICIA EL ENVIO DE MAIL ************************
//						//******************************************************************************
//						//defino los datos para la plantilla
//						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//						Map<String, Object> parametros = new HashMap<String, Object>();
//						
//						parametros.put("nombres", fgmpfUsuario.getUsrPersona().getPrsNombres().toUpperCase());
//						parametros.put("apellidos", fgmpfUsuario.getUsrPersona().getPrsPrimerApellido().toUpperCase()+" "
//										+(fgmpfUsuario.getUsrPersona().getPrsSegundoApellido().toUpperCase() == null?"":fgmpfUsuario.getUsrPersona().getPrsSegundoApellido().toUpperCase()));
//						sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
//						parametros.put("fechaHora", sdf.format(new Date()));
//						Dependencia facultadAux = null;
//						try {
//							facultadAux = servFgmpfDependenciaServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
//						} catch (DependenciaNoEncontradoException e) {
//						}
//						parametros.put("facultad", facultadAux.getDpnDescripcion());
//						parametros.put("carrera", GeneralesUtilidades.generaStringParaCorreo(carreraAux.getCrrDescripcion()));
//						//lista de correos a los que se enviara el mail
//						List<String> correosTo = new ArrayList<>();
//						
//						correosTo.add(fgmpfUsuario.getUsrPersona().getPrsMailInstitucional());
//						String pathTemplate = null;
//						//path de la plantilla del mail
//						switch (fgmpfUsuario.getUsrIdentificacion()) {
//						case "1718470105":
//						case "1723585889":
//						case "1720232139":
//						case "0605598127":
//						case "1003059803":
//						case "1714005749":
//						case "1714831276":
//						case "1718567843":
//						case "1313525196":
//						case "1719302158":
//						case "1757335813":
//						case "0502030752":
//						case "1311758419":
//						case "1756157986":
//						case "1711932341":
//						case "0401189469":
//						case "2100368345":
//						case "1720559036":
//						case "0603034380":
//						case "1103876197":
//						case "1722454962":
//						case "1400755987":
//						case "1003567516":
//						case "1712637667":
//						case "1720237211":
//						case "1103727069":
//						case "0705335693":
//						case "1718895608":
//						case "1717915142":
//						case "0924196033":
//						case "1804295812":
//						case "0604088120":
//						case "2300116353":
//						case "1104577349":
//						case "1720643426":
//						case "1713848719":
//						case "0302152608":
//						case "1717855421":
//						case "0603933268":
//						case "1717227712":
//						case "1725768582":
//						case "1718315623":
//						case "0603865221":
//						case "1721054326":
//						case "1803698883":
//						case "1721898730":
//						case "1720213188":
//						case "1804309902":
//						case "1719296640":
//						case "1716259120":
//						case "0401140231":
//						case "1712643004":
//						case "1803731353":
//						case "1709574030":
//						case "1709691636":
//						case "1722720974":
//						case "0603337395":
//						case "1723722383":
//						case "1804129086":
//						case "1804263554":
//						case "0604409490":
//						case "1715627475":
//						case "1715351274":
//						case "0401545389":
//						case "1715953210":
//						case "1804242939":
//						case "1309189940":
//						case "1720505385":
//						case "1600484016":
//						case "1103616700":
//						case "1103817167":
//						case "0704466267":
//						case "0604318030":
//						case "1717464323":
//						case "1714471438":
//						case "0201793361":
//						case "1719254904":
//						case "1104870751":
//						case "0703924159":
//						case "1717435216":
//						case "1804605374":
//						case "0301748018":
//						case "1804044046":
//						case "1312848052":
//						case "1311401101":
//						case "1803787918":
//						case "0503513376":
//						case "1003082060":
//						case "0503143521":
//						case "1003569165":
//						case "1803113214":
//						case "0201918315":
//						case "1718412891":
//						case "1309499927":
//						case "1724114929":
//						case "0401593462":
//						case "1719500975":
//						case "1803994563":
//						case "1804611117":
//						case "0201852985":
//						case "0929377091":
//						case "1205490327":
//						case "0604672857":
//						case "1725454621":
//						case "1716865694":
//						case "0603866708":
//						case "1719376657":
//						case "1720533734":
//						case "0603564485":
//						case "1720064847":
//						case "1003559448":
//						case "1721514238":
//						case "0926263989":
//						case "1713757852":
//						case "1721880761":
//						case "0106036395":
//						case "0502074065":
//						case "0703961623":
//						case "0502332380":
//						case "1725201873":
//						case "1715645899":
//						case "1713423794":
//						case "1718524737":
//						case "0705416105":
//						case "1804274007":
//						case "1718851007":
//						case "1716992803":
//						case "1717839359":
//						case "0923566731":
//						case "1718361007":
//						case "1104658701":
//						case "1803605953":
//						case "1312781105":
//						case "1804472858":
//						case "1720748621":
//						case "1311594137":
//						case "0302306584":
//						case "0603462433":
//						case "0502966757":
//						case "1723542773":
//						case "1716386675":
//						case "0705429009":
//						case "1500625270":
//						case "1804041620":
//						case "1722246947":
//						case "1719368670":
//						case "1311040016":
//						case "1804530325":
//						case "1721711149":
//						case "1105030827":
//						case "1716084841":
//						case "0104473772":
//						case "1721071262":
//						case "1719593673":
//						case "1718317108":
//						case "0502550908":
//						case "1803743853":
//						case "0922427018":
//						case "1104350069":
//						case "1711761237":
//						case "1718401472":
//						case "1720688454":
//						case "1722487731":
//						case "1717540569":
//						case "1003821186":
//						case "1312838939":
//						case "1721827978":
//						case "0104434535":
//						case "1722057583":
//						case "1719372631":
//						case "0502578313":
//						case "1717556219":
//						case "1718475443":
//						case "1720897980":
//						case "1716768930":
//						case "0604655456":
//						case "1720990520":
//						case "1804697546":
//						case "0202047130":
//						case "1722634787":
//						case "1803023009":
//						case "0913727780":
//						case "1715510259":
//						case "1718829052":
//						case "1713001194":
//						case "1103302418":
//						case "1721888210":
//						case "1804021077":
//						case "0803039270":
//						case "1802822443":
//						case "1719693358":
//						case "1803191699":
//						case "0923400279":
//						case "1718481664":
//						case "0401609284":
//						case "1722620786":
//						case "1712022258":
//						case "1715781835":
//						case "1721096434":
//						case "1722254388":
//						case "1104446313":
//						case "0401462387":
//						case "1719274027":
//						case "1715782486":
//						case "1723563944":
//						case "1717555948":
//						case "0801978982":
//						case "1722653191":
//						case "1002975181":
//						case "1723867055":
//						case "1721355434":
//						case "0704941277":
//						case "1719050963":
//						case "1900529924":
//						case "1312838582":
//							pathTemplate = "ec/edu/uce/academico/jsf/velocity/plantillas/template-postulacion-posgrado-becado.vm";
//							break;
//						default:
//							pathTemplate = "ec/edu/uce/academico/jsf/velocity/plantillas/template-postulacion-posgrado.vm";
//							break;
//						}
//
//						//llamo al generador de mails
//						GeneradorMails genMail = new GeneradorMails();
//						String mailjsonSt;
//						try {
//							mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_MATRICULA_POSGRADO, 
//									GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
//							//****envio el mail a la cola
//							//cliente web service
//							Client client = ClientBuilder.newClient();
//							WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
//							MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
//							postForm.add("mail", mailjsonSt);
//				//			String responseData = target.request().post(Entity.form(postForm),String.class);
//							target.request().post(Entity.form(postForm),String.class);
//						} catch (ValidacionMailException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						//******************************************************************************
//						//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//						//******************************************************************************
//					}
				fgmpfValidadorClic = new Integer(0);
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.generarMatriculaPosgrado.exitoso")));
				return "irConfirmacionMatricula";
				
			} catch (MatriculaValidacionException e) {
				FacesUtil.mensajeInfo(e.getMessage());
				fgmpfValidadorClic = new Integer(0);
				return null;
			} catch (MatriculaException e) {
				FacesUtil.mensajeInfo(e.getMessage());
				fgmpfValidadorClic = new Integer(0);
				return null;
			} catch (Exception e) {
				fgmpfValidadorClic = new Integer(0);
				return null;
			} 
			
		}
		fgmpfValidadorClic = new Integer(0);
		return "irInicio";
	}

	
	/**
	 * genera el comprobante de pago para los postulantes a Medicina 
	 */
	public String generarComprobantePago(){
		return null;
	}
	
	
	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/
	/**
	 * verifica que haga click en el boton generar matricula
	 */
	public String verificarClickGenerarMatricula(){
		if(fgmpfFichaInscripcionDto.getCrrId()<109 || fgmpfFichaInscripcionDto.getCrrId()>129){
			fgmpfmensajeModal = "Al generar la matrícula, usted recibirá el comprobante de pago ¿está seguro que desea continuar?";
		}else{
			fgmpfmensajeModal = "Su matrícula está siendo generada. ¿está seguro que desea continuar?";
		}
		
		fgmpfValidadorClic = 1;
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

	public Usuario getFgmpfUsuario() {
		return fgmpfUsuario;
	}

	public void setFgmpfUsuario(Usuario fgmpfUsuario) {
		this.fgmpfUsuario = fgmpfUsuario;
	}

	public List<FichaInscripcionDto> getFmfListFichaInscripcionDto() {
		fgmpfListFichaInscripcionDto = fgmpfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmpfListFichaInscripcionDto;
		return fgmpfListFichaInscripcionDto;
	}

	public void setFmfListFichaInscripcionDto(List<FichaInscripcionDto> fgmpfListFichaInscripcionDto) {
		this.fgmpfListFichaInscripcionDto = fgmpfListFichaInscripcionDto;
	}

	public List<CarreraDto> getFmfListCarreraDto() {
		fgmpfListCarreraDto = fgmpfListCarreraDto==null?(new ArrayList<CarreraDto>()):fgmpfListCarreraDto;
		return fgmpfListCarreraDto;
	}

	public void setFmfListCarreraDto(List<CarreraDto> fgmpfListCarreraDto) {
		this.fgmpfListCarreraDto = fgmpfListCarreraDto;
	}

	public List<FichaInscripcionDto> getFgmpfListFichaInscripcionDto() {
		fgmpfListFichaInscripcionDto = fgmpfListFichaInscripcionDto==null?(new ArrayList<FichaInscripcionDto>()):fgmpfListFichaInscripcionDto;
		return fgmpfListFichaInscripcionDto;
	}

	public void setFgmpfListFichaInscripcionDto(List<FichaInscripcionDto> fgmpfListFichaInscripcionDto) {
		this.fgmpfListFichaInscripcionDto = fgmpfListFichaInscripcionDto;
	}

	public List<CarreraDto> getFgmpfListCarreraDto() {
		fgmpfListCarreraDto = fgmpfListCarreraDto==null?(new ArrayList<CarreraDto>()):fgmpfListCarreraDto;
		return fgmpfListCarreraDto;
	}

	public void setFgmpfListCarreraDto(List<CarreraDto> fgmpfListCarreraDto) {
		this.fgmpfListCarreraDto = fgmpfListCarreraDto;
	}

	public List<RecordEstudianteDto> getFgmpfListRecordEstudiante() {
		fgmpfListRecordEstudiante = fgmpfListRecordEstudiante==null?(new ArrayList<RecordEstudianteDto>()):fgmpfListRecordEstudiante;
		return fgmpfListRecordEstudiante;
	}

	public void setFgmpfListRecordEstudiante(List<RecordEstudianteDto> fgmpfListRecordEstudiante) {
		this.fgmpfListRecordEstudiante = fgmpfListRecordEstudiante;
	}

	public Boolean getFgmpfEstudianteNuevo() {
		return fgmpfEstudianteNuevo;
	}

	public void setFgmpfEstudianteNuevo(Boolean fgmpfEstudianteNuevo) {
		this.fgmpfEstudianteNuevo = fgmpfEstudianteNuevo;
	}

	public MallaCurricular getFgmpfMallaCurricular() {
		return fgmpfMallaCurricular;
	}

	public void setFgmpfMallaCurricular(MallaCurricular fgmpfMallaCurricular) {
		this.fgmpfMallaCurricular = fgmpfMallaCurricular;
	}

	public List<MateriaDto> getFgmpfListMateriaDto() {
		fgmpfListMateriaDto = fgmpfListMateriaDto==null?(new ArrayList<MateriaDto>()):fgmpfListMateriaDto;
		return fgmpfListMateriaDto;
	}

	public void setFgmpfListMateriaDto(List<MateriaDto> fgmpfListMateriaDto) {
		this.fgmpfListMateriaDto = fgmpfListMateriaDto;
	}

	public List<ParaleloDto> getFgmpfListParaleloDto() {
		fgmpfListParaleloDto = fgmpfListParaleloDto==null?(new ArrayList<ParaleloDto>()):fgmpfListParaleloDto;
		return fgmpfListParaleloDto;
	}

	public void setFgmpfListParaleloDto(List<ParaleloDto> fgmpfListParaleloDto) {
		this.fgmpfListParaleloDto = fgmpfListParaleloDto;
	}

	public PeriodoAcademico getFgmpfPeriodoAcademico() {
		return fgmpfPeriodoAcademico;
	}

	public void setFgmpfPeriodoAcademico(PeriodoAcademico fgmpfPeriodoAcademico) {
		this.fgmpfPeriodoAcademico = fgmpfPeriodoAcademico;
	}

	public FichaInscripcionDto getFgmpfFichaInscripcionDto() {
		return fgmpfFichaInscripcionDto;
	}

	public void setFgmpfFichaInscripcionDto(FichaInscripcionDto fgmpfFichaInscripcionDto) {
		this.fgmpfFichaInscripcionDto = fgmpfFichaInscripcionDto;
	}

	public Integer getFgmpfValidadorClic() {
		return fgmpfValidadorClic;
	}

	public void setFgmpfValidadorClic(Integer fgmpfValidadorClic) {
		this.fgmpfValidadorClic = fgmpfValidadorClic;
	}

	public PlanificacionCronograma getFgmpfPlanificacionCronograma() {
		return fgmpfPlanificacionCronograma;
	}

	public void setFgmpfPlanificacionCronograma(PlanificacionCronograma fgmpfPlanificacionCronograma) {
		this.fgmpfPlanificacionCronograma = fgmpfPlanificacionCronograma;
	}

	public boolean isFgmpfBloqueoGenerar() {
		return fgmpfBloqueoGenerar;
	}

	public void setFgmpfBloqueoGenerar(boolean fgmpfBloqueoGenerar) {
		this.fgmpfBloqueoGenerar = fgmpfBloqueoGenerar;
	}

	public String getFgmpfmensajeModal() {
		return fgmpfmensajeModal;
	}

	public void setFgmpfmensajeModal(String fgmpfmensajeModal) {
		this.fgmpfmensajeModal = fgmpfmensajeModal;
	}
	
	
	
}
