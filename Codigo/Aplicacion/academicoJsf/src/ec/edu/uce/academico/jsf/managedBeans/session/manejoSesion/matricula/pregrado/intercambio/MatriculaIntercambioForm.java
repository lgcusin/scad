/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproduccion o distribucion no autorizada de este programa, 
 * o cualquier porcion de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serÃ¡n procesadas con el grado mÃ¡ximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     MatriculaIntercambioForm.java	  
 DESCRIPCION: Bean de sesion que maneja la matricula del estudiantede pregrado - intercambio. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 28-09-2018			 Freddy Guzmán 							Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.matricula.pregrado.intercambio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaException;
import ec.edu.uce.academico.ejb.excepciones.MatriculaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaIntercambioServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaIntercambioServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GratuidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MailConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoAperturaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.mail.MailRegistroMatricula;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteRegistroMatriculaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes; 

/**
 * Clase (session bean) MatriculaIntercambioForm. 
 * Bean de sesion que maneja la matricula del estudiante de pregrado.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "matriculaIntercambioForm")
@SessionScoped
public class MatriculaIntercambioForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private String mifIdentificacion;
	private String mifPrimerApellido;
	private Integer mifTipoBusqueda;
	private Integer mifPerdidaGratuidad;
	private Integer mifNivelUbicacion;
	private Integer mifFacultadId;
	private Integer mifCarreraId;
	private Integer mifNivelId;
	private Boolean mifIsEstudianteNuevo;
	private Usuario mifUsuario;
	private Usuario mifUsuarioSeleccionado;
	private TipoGratuidad mifTipoGratuidad;
	private CarreraDto mifCarreraDto;
	private FichaInscripcionDto mifFichaInscripcionDto;
	private PeriodoAcademico mifPeriodoAcademico;
	private PlanificacionCronograma mifPlanificacionCronograma;
	private Dependencia mifFacultad;
	private Carrera mifCarrera;
	private CronogramaActividadJdbcDto mifProcesoFlujo;
	private List<Dependencia> mifListFacultad;
	private List<Carrera> mifListCarrera;
	private List<FichaInscripcionDto> mifListFichaInscripcionDto;
	private List<FichaInscripcionDto> mifListSeleccionFichaInscripcionDto;
	private List<FichaInscripcionDto> mifListGeneralFichaInscripcionDto;
	private List<CarreraDto> mifListCarreraDto;
	private List<MateriaDto> mifListMateriaDto;
	private List<MateriaDto> mifListMatriculadoMateriaDto;
	private List<MateriaDto> mifListMallaCurricularMateriaDto;
	private List<ParaleloDto> mifListParaleloDto;
	private List<HorarioAcademicoDto> mifListHorarioAcademicoDto;
	private List<Arancel> mifListArancel;
	private List<NivelDto> mifListNivelDto;
	private List<MateriaDto> mifListMatriculaExitosaMateriaDto;
	private int contadorUnicoMatriculas = 0;	

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB private FichaInscripcionDtoServicioJdbc servJdbcFichaInscripcionDto;
	@EJB private CarreraServicio servCarrera;
	@EJB private DependenciaServicio servDependencia;
	@EJB private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private NivelDtoServicioJdbc servJdbcNivelDto;
	@EJB private NivelServicio servNivel;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private UsuarioServicio servUsuario;
	@EJB private MatriculaIntercambioServicio servMatriculaIntercambio;
	@EJB private MatriculaIntercambioServicioJdbc servJdbcMatriculaIntercambio;
	@EJB private MatriculaServicioJdbc servJdbcMatricula;
	@EJB private MatriculaServicio servMatricula;
	@EJB private CronogramaActividadDtoServicioJdbc servJdbcCronogramaActividadDto;
	@EJB private CronogramaDtoServicioJdbc servJdbcCronogramaDto;
	@EJB private FichaMatriculaDtoServicioJdbc servJdbcFichaMatriculaDto;
	@EJB private ParaleloDtoServicioJdbc servJdbcParaleloDto;
	@EJB private ParaleloServicio servParalelo;
	@EJB private HorarioAcademicoDtoServicioJdbc servJdbcHorarioAcademicoDto;
	@EJB private PlanificacionCronogramaServicio servPlanificacionCronograma;
	// ****************************************************************/
	// *********************** MÉTODOS NAVEGACIÓN *********************/
	// ****************************************************************/
	
	public String irEstudiantesIntercambios(){
		return "irEstudiantesIntercambio";
	}
	
	
	public String irGenerarMatriculaIntercambio(){
		
		return "irGenerarMatriculaIntercambio";
	}
	
	public String irListarEstudiantesIntercambio(){
		
		return "irListarEstudiantesIntercambio";
	}
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula
	 * @return navegacion al listar matricula
	 */
	public String irListarFichaInscripcion(Usuario usuario) {
		mifUsuario = usuario;
		
		try {
			
			PeriodoAcademico periodo = servPeriodoAcademico.buscarPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			if (periodo != null) {
				mifPeriodoAcademico = periodo;
				
				List<FichaInscripcionDto> fichasInscripcion = cargarFichasInscripcionIntercambio(new Integer[]{FichaInscripcionConstantes.ACTIVO_VALUE}, new Integer[]{FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE},periodo.getPracId());
				if (fichasInscripcion != null && fichasInscripcion.size() > 0) {
					mifListGeneralFichaInscripcionDto = fichasInscripcion;
					mifListFichaInscripcionDto = generarListaFichaInscripcion(fichasInscripcion);
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No se han encontrado estudiantes de intercambio registrados en el SIIU.");
					return null;
				}		
			}
			 
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
		return "irListarEstudiantesIntercambio";
	}
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula
	 * @return navegacion al listar matricula
	 */
	public String irListarFichaInscripcionDirectores(Usuario usuario) {
		
		try {


			List<UsuarioRol> usro = servUsuarioRol.buscarXUsuario(usuario.getUsrId());
			if (usro != null && usro.size() > 0) {

				UsuarioRol usuarioRol = null;
				for (UsuarioRol item : usro) {
					if (item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE || item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE) {
						usuarioRol = item;
						break;
					}
				}

				if (usuarioRol != null) {
					List<CarreraDto> carreras = cargarCarrerasPorUsuarioRol(usuarioRol.getUsroId());
							if (carreras != null && carreras.size() > 0) {
								PeriodoAcademico periodo = servPeriodoAcademico.buscarPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);

								if (periodo != null) {
									mifPeriodoAcademico = periodo;
									List<FichaInscripcionDto> fichasInscripcion = cargarFichasInscripcionIntercambio(new Integer[]{FichaInscripcionConstantes.ACTIVO_VALUE}, new Integer[]{FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE},periodo.getPracId());
									if (fichasInscripcion != null && fichasInscripcion.size() > 0) {
										mifListFichaInscripcionDto = cargarEstudiantesPorRolUsuario(fichasInscripcion, carreras);// cargar estudianes solo de su carrera.
									}else{
										FacesUtil.limpiarMensaje();
										FacesUtil.mensajeError("No se han encontrado estudiantes de intercambio registrados en su Carrera.");
										return null;
									}		
								}

							}else {
								FacesUtil.mensajeError("Ud. no tiene el rol requerido para ejecutar el proceso de matrículas para estudiantes de intercambio.");
								return  null;	
							}

				}else{
					FacesUtil.mensajeError("Ud. no tiene el rol requerido para ejecutar el proceso de matrículas para estudiantes de intercambio.");
					return  null;
				}
			}

		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolNoEncontradoException e){
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
		
		return "irListarEstudiantesIntercambioDirectores";
	}
	
	public String irGenerarMatriculaEstudianteIntercambio(FichaInscripcionDto estudiante){
		mifFichaInscripcionDto = estudiante;

		Date fechaActual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
		String strDate = sdf.format(fechaActual);

		if (estudiante.getFcinTipoIngreso().equals(FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE) 
				&& estudiante.getFcinEstado().equals(FichaInscripcionConstantes.ACTIVO_VALUE)) {

			try {
				// buscar planificacion_cronograma segun proceso flujo-> periodo academico activo, tipo pregrado -> estado planificacion cronograma
				// al registrar en sistema no homolagion
				mifProcesoFlujo = servJdbcCronogramaActividadDto.buscarPlanificacionCronogramaPorFechasFull(strDate, CronogramaConstantes.TIPO_ACADEMICO_VALUE);
				mifCarrera = servCarrera.buscarPorId(estudiante.getCrrId());
				mifFacultad = servDependencia.buscarPorId(estudiante.getDpnId());
				mifPlanificacionCronograma = servPlanificacionCronograma.buscarPorId(mifProcesoFlujo.getPlcrId());
				mifUsuarioSeleccionado = servUsuario.buscarUsuarioPorIdentificacion(estudiante.getPrsIdentificacion());
				if (validarCronograma()) {
					if(!verificarSiRegistraMatricula(estudiante)){
						// materias solicitadas
						List<FichaInscripcionDto> fichasInscripcion = new ArrayList<>();fichasInscripcion.add(estudiante);
						mifListMateriaDto = cargarMateriasInscrito(fichasInscripcion);

						for (MateriaDto item : mifListMateriaDto) {
							List<ParaleloDto> listParaleloDto = null;
							
							try{
								listParaleloDto = servJdbcParaleloDto.ListarXMateriaId(item.getMtrId());
							} catch (ParaleloDtoNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage() + item.getMtrDescripcion());
							} catch (ParaleloDtoException e) {
								FacesUtil.mensajeError(e.getMessage());
							}
							
							item.setMtrListParalelo(listParaleloDto);
							item.setNumMatricula(SAUConstantes.PRIMERA_MATRICULA_VALUE);
							item.setMtrCmbEstado(false);
						}

						calcularNivelAsignado();

						if(mifNivelUbicacion != null){
							if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, mifNivelUbicacion, mifProcesoFlujo.getPrlfId(), estudiante.getCrrId(), true)){
								mifListMatriculadoMateriaDto = cargarRecordEstudianteIntercmbio();
								mifIsEstudianteNuevo = Boolean.FALSE;
								return "irGenerarMatriculaIntercambio";
							}else {
								return null; 
							}
						}else {
							return null;
						} 			

					}else{ 
						return null; 
					}
				}else{
					return null; 
				}

			} catch (CronogramaActividadDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.ir.generar.matricula.pregrado.cronograma.actividad.dto.jdbc.no.encontrado.exception")));
				return null;
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (CarreraException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (DependenciaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (DependenciaException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (PlanificacionCronogramaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (PlanificacionCronogramaException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (UsuarioNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (UsuarioException e) {
				FacesUtil.mensajeError(e.getMessage());
				return null;
			}

		}else {
			FacesUtil.mensajeError("Verifique que los datos del estudiante hayan sido ingresados correctamente.");
			return null;
		}
	}

	private List<MateriaDto> cargarRecordEstudianteIntercmbio(){
		List<MateriaDto> retorno = null; 
		List <String> estados = new ArrayList<>();estados.add(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE.toString());

		try {
			retorno = servJdbcMateriaDto.buscarRecordEstudiante(mifFichaInscripcionDto.getPrsIdentificacion(),estados, PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE ,PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (MateriaDtoException e) {
		} catch (MateriaDtoNoEncontradoException e) {
		}
		
		return retorno;

	}
	
	private Boolean validarCronograma(){

		Date fechaActual = new Date();
		Boolean retorno =  false; 

		try {

			CronogramaActividadJdbcDto mifFechasMatriculaOrdinaria = servJdbcCronogramaActividadDto.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
			CronogramaActividadJdbcDto mifFechasMatriculaExtraordinaria = servJdbcCronogramaActividadDto.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
			CronogramaActividadJdbcDto mifFechasMatriculaEspecial = servJdbcCronogramaActividadDto.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);

			Date fechaInicioMatriculasOrdinarias = new Date(mifFechasMatriculaOrdinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasOrdinarias = new Date(mifFechasMatriculaOrdinaria.getPlcrFechaFin().getTime());

			Date fechaInicioMatriculasExtraordinarias = new Date(mifFechasMatriculaExtraordinaria.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasExtraordinarias = new Date(mifFechasMatriculaExtraordinaria.getPlcrFechaFin().getTime());

			Date fechaInicioMatriculasEspecial = new Date(mifFechasMatriculaEspecial.getPlcrFechaInicio().getTime());
			Date fechaFinMatriculasEspecial = new Date(mifFechasMatriculaEspecial.getPlcrFechaFin().getTime());

			if(fechaFinMatriculasEspecial.after(fechaActual)){
				FacesUtil.limpiarMensaje();
				if(fechaInicioMatriculasEspecial.before(fechaActual)){ 
					retorno = true;
					FacesUtil.mensajeInfo("Periodo de matrículas especiales.");
				}else{
					FacesUtil.mensajeError("Periodo de matrículas especiales no ha iniciado.");
				}
			}else{
				FacesUtil.mensajeError("Periodo de matrículas especiales ha expirado.");
			}

			if(!retorno){
				if(fechaFinMatriculasExtraordinarias.after(fechaActual)){
					FacesUtil.limpiarMensaje();
					if(fechaInicioMatriculasExtraordinarias.before(fechaActual)){ 
						retorno = true;
						FacesUtil.mensajeInfo("Periodo de matrículas extraordinarias.");
					}else{
						FacesUtil.mensajeError("Periodo de matrículas extraordinarias no ha iniciado.");
					}
				}else{
					FacesUtil.mensajeError("Periodo de matrículas extraordinarias ha expirado.");
				}
			}

			if(!retorno){
				if(fechaFinMatriculasOrdinarias.after(fechaActual)){
					FacesUtil.limpiarMensaje();
					if(fechaInicioMatriculasOrdinarias.before(fechaActual)){
						retorno = true;
						FacesUtil.mensajeInfo("Periodo de matrículas ordinarias.");
					}else{
						FacesUtil.mensajeError("Periodo de matrículas ordinarias no ha iniciado.");
					}
				}else{
					FacesUtil.mensajeError("Periodo de matrículas ordinarias ha expirado.");
				}
			}

		} catch (CronogramaActividadDtoJdbcException e) { 
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Cronograma de matrículas no encontrado, verifique parametrización.");
		}

		return retorno;
	}
	
	
	public Boolean verificarCronogramaSemestreCarrera(int tipoApertura, int numeral, int procesoFlujo, int crrId, boolean mensaje){
		Boolean verificar = false;
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		
		try {
			CronogramaDto cronograma = servJdbcCronogramaDto.buscarCronogramaXTipoAperturaXNivelAperturaXprocesoFlujo(procesoFlujo, tipoApertura, numeral, crrId);
			  
			if(cronograma != null){
				if(cronograma.getPlcrFechaFin() != null && cronograma.getPlcrFechaInicio() != null){
					
					if(cronograma.getPlcrFechaFin().after(fechaActual)){
						if(cronograma.getPlcrFechaInicio().before(fechaActual)){ 
							verificar = true;
						}else{ 
							FacesUtil.mensajeError("El cronograma para "+cronograma.getCrnDescripcion()+" no ha empezado."); 
						}
					}else{ 
						FacesUtil.mensajeError("El cronograma para "+cronograma.getCrnDescripcion()+" ha expirado."); 
					}
				}else{
					FacesUtil.mensajeError("No se ha asignado cronograma para "+cronograma.getCrnDescripcion()+".");
				}
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se ha asignado cronograma para matrículas.");
			}
			
			if(!mensaje){
				FacesUtil.limpiarMensaje();
			}
			
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se ha asignado cronograma para matrículas.");
		}
	 
		return verificar;
	}

	/**
	 * Calcula el nivel asignado al estudiante segun sus materias seleccionadas y numero total de creditos
	 * @param mensaje .- Si se emite mensaje o no. 
	 **/
	public void calcularNivelAsignado(){

		List<MateriaDto> materiasSeleccionadas = new ArrayList<>();
		Integer numeroTotalCreditosHoras = 0; 

		if (mifListMateriaDto != null && mifListMateriaDto.size() > 0  ) {

			for (MateriaDto it : mifListMateriaDto) {
				if(it.getMtrCmbEstado() != null && it.getMtrCmbEstado()){
					materiasSeleccionadas.add(it);
					if(it.getMtrHorasPorSemana() != null && it.getMtrHorasPorSemana() != 0){
						numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHorasPorSemana();
					}
				}
			}
			
			if(materiasSeleccionadas.size() == 0){
				for (MateriaDto it : mifListMateriaDto) {
					materiasSeleccionadas.add(it);
					if(it.getMtrHorasPorSemana() != null && it.getMtrHorasPorSemana() != 0){
						numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHorasPorSemana();
					}
				}
			}

//			if(numeroTotalCreditosHoras.intValue() <= fgmpgfCarreraSeleccion.getCrrNumMaxCreditos().intValue()){

				List<List<MateriaDto>> listasMateriasSeleccion = new ArrayList<>();

				Integer nivelAsignacion = 0;
				Integer numeroCreditos = 0;
				List<Integer> creditosXSemestre = new ArrayList<>();
				List<Integer> nivelXSemestre = new ArrayList<>();

				for(MateriaDto item: materiasSeleccionadas){
					if(item.getMtrCmbEstado() != null && item.getMtrCmbEstado()){
						List<MateriaDto> list = new ArrayList<>();
						for(MateriaDto itemR: materiasSeleccionadas){
							if( item.getNvlNumeral() == itemR.getNvlNumeral()){
								list.add(itemR);
							} 
						}
						if(list.size() > 0){
							listasMateriasSeleccion.add(list);
						}
					}
				}

				if(listasMateriasSeleccion.size() == 0){
					for(MateriaDto item: materiasSeleccionadas){
						List<MateriaDto> list = new ArrayList<>();
						for(MateriaDto itemR: materiasSeleccionadas){
							if( item.getNvlNumeral() == itemR.getNvlNumeral()){
								list.add(itemR);
							} 
						}
						if(list.size() > 0){
							listasMateriasSeleccion.add(list);
						}
					}
				} 
				
				for (int i = 0; i < listasMateriasSeleccion.size(); i++) {

					for(MateriaDto itemR: listasMateriasSeleccion.get(i)){

						if(itemR.getMtrHorasPorSemana() != null && itemR.getMtrHorasPorSemana() != 0){
							numeroCreditos = numeroCreditos+itemR.getMtrHorasPorSemana();
							nivelAsignacion = itemR.getNvlNumeral();
						}

					}

					Boolean adjuntar = true;
					
					for (int j = 0; j < nivelXSemestre.size(); j++) {

						if(nivelXSemestre.get(j) == nivelAsignacion){
							adjuntar = false;
						}
					}

					if(adjuntar){
						creditosXSemestre.add(numeroCreditos);
						nivelXSemestre.add(nivelAsignacion);
					}

					numeroCreditos = 0;
					nivelAsignacion = 0;
				}

				if(nivelXSemestre.size() > 1){

					for (int i = 0; i < creditosXSemestre.size(); i++) {
						for (int j = 0; j < creditosXSemestre.size(); j++) {
							if(creditosXSemestre.get(i) > creditosXSemestre.get(j)){
								numeroCreditos = creditosXSemestre.get(i);
								nivelAsignacion = nivelXSemestre.get(i);
							}
							if(creditosXSemestre.get(i) == creditosXSemestre.get(j)){
								if(nivelXSemestre.get(i) < nivelXSemestre.get(j)){
									numeroCreditos = creditosXSemestre.get(i);
									nivelAsignacion = nivelXSemestre.get(i);
								}
							}
						}
					}

				}else{
					if(creditosXSemestre.size() != 0){
						numeroCreditos = creditosXSemestre.get(creditosXSemestre.size()-1);
					}else{
						numeroCreditos = 0;
					}
					if(nivelXSemestre.size() != 0){
						nivelAsignacion = nivelXSemestre.get(nivelXSemestre.size()-1);
					}else{
						nivelAsignacion = 0;
					}
				}

				mifNivelUbicacion = nivelAsignacion;
			} 
//		}
	}


	/**
	 * Verifica que el usuario no refleje matricula vigente en este periodo
	 * @return Retorna si esta amtriculado o no
	 */
	private Boolean verificarSiRegistraMatricula(FichaInscripcionDto estudiante){
		boolean retorno = false;
		
		try {
			List<FichaMatriculaDto> matricula = servJdbcFichaMatriculaDto.buscarXPeriodoXidentificacionXcarrera(mifPeriodoAcademico.getPracId(), estudiante.getPrsIdentificacion(), estudiante.getCrrId());
			if (matricula != null && matricula.size() > 0) {
				retorno = true;	
				FacesUtil.mensajeInfo("El estudiante con identificación " + estudiante.getPrsIdentificacion() + " ya registra matrícula en el periodo actual.");
			}
		} catch (FichaMatriculaException e) {
//			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	
	public String irRegistrarMaterias(){
		if (mifListMateriaDto != null && mifListMateriaDto.size() > 0) {
			Boolean registro = null;
			
			try {
				Map<CarreraDto, List<MateriaDto>> materiasPorCarrera =	mifListMateriaDto.stream().collect(Collectors.groupingBy(MateriaDto::getMtrCarreraDto)); 
				registro = servMatriculaIntercambio.guardar(materiasPorCarrera,mifListSeleccionFichaInscripcionDto, mifUsuario);
			} catch (CarreraIntercambioValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraIntercambioException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
			if (registro != null && registro.equals(true)) {
				limpiarModalAgregarMaterias();
				FacesUtil.mensajeInfo("Se han registrado con éxito las materias agregadas.");
				return "irEstudiantesIntercambio";	
			}else {
				FacesUtil.mensajeError("Inténtelo mas tarde, problemas de conexión con el servidor.");
				return null;
			}
			
		}else {
			FacesUtil.mensajeInfo("Agregue por lo menos una materia para continuar.");
			return null;
		}
	}
	
	/**
	 * Método que permite listar las fichas inscripcion de los estudiantes de intercambio segun la carrera del director de carrera.
	 */
	private List<FichaInscripcionDto> cargarEstudiantesPorRolUsuario(List<FichaInscripcionDto> fichasInscripcion, List<CarreraDto> carreras) {
		List<FichaInscripcionDto> retorno = new ArrayList<>();
		
		for (FichaInscripcionDto itemDB : fichasInscripcion) {
			for (CarreraDto itemDir : carreras) {
				if (itemDB.getCrrId().equals(itemDir.getCrrId())) {
					retorno.add(itemDB);
				}
			}
		}
		
		if (retorno.size() > 0) {
			retorno.sort(Comparator.comparing(FichaInscripcionDto::getCrrDescripcion).thenComparing(Comparator.comparing(FichaInscripcionDto::getPrsPrimerApellido)));
		}
		
		return retorno;
	}

	private List<CarreraDto> cargarCarrerasPorUsuarioRol(int usroId){
		List<CarreraDto> retorno = null;
		
		try {
			retorno = servJdbcCarreraDto.buscarCarreras(usroId);
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Ud. no tiene asignadas carreras activas en este momento.");
		}
		
		return retorno;
	}

	
	public String irAgregarCarrerasMaterias(FichaInscripcionDto estudiante){
		mifFichaInscripcionDto = estudiante;
		
		if (mifListGeneralFichaInscripcionDto != null && mifListGeneralFichaInscripcionDto.size() > 0) {
			List<FichaInscripcionDto> fichas = new ArrayList<>();
			for (FichaInscripcionDto item : mifListGeneralFichaInscripcionDto) {
				if (item.getPrsIdentificacion().equals(mifFichaInscripcionDto.getPrsIdentificacion())) {
					fichas.add(item);
				}
			}

			mifListSeleccionFichaInscripcionDto = fichas;
			List<MateriaDto> materias = cargarMateriasInscrito(fichas);
			if (materias.size() > 0) {
				materias.sort(Comparator.comparing(MateriaDto::getCrrDescripcion).thenComparing(Comparator.comparing(MateriaDto::getMtrCodigo)));
			}

			mifListMateriaDto = materias;
			inicializarModalAgregarMaterias(fichas);
			return "irCarreras";
		}else{
			return null;
		}
	}
	
	private List<MateriaDto> cargarMateriasInscrito(List<FichaInscripcionDto> estudiante) {
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcMatriculaIntercambio.buscarMateriasIscripcion(estudiante);
		} catch (CarreraIntercambioNoEncontradoException e) {
			FacesUtil.mensajeError("El estudiante aun no cuenta con materias para su matrícula.");
		} catch (CarreraIntercambioException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}


	public String irInicio(){
		mifIdentificacion= null;
		mifPrimerApellido= null;
		mifTipoBusqueda= null;
		mifPerdidaGratuidad= null;
		mifNivelUbicacion= null;
		mifFacultadId= null;
		mifCarreraId= null;
		mifNivelId= null;
		mifIsEstudianteNuevo= null;
		mifUsuario= null;
		mifTipoGratuidad= null;
		mifCarreraDto= null;
		mifFichaInscripcionDto= null;
		mifPeriodoAcademico= null;
		mifPlanificacionCronograma= null;
		mifProcesoFlujo= null;
		mifListFacultad= null;
		mifListCarrera= null;
		mifListFichaInscripcionDto= null;
		mifListSeleccionFichaInscripcionDto= null;
		mifListGeneralFichaInscripcionDto= null;
		mifListCarreraDto= null;
		mifListMateriaDto= null;
		mifListMallaCurricularMateriaDto= null;
		mifListParaleloDto= null;
		mifListHorarioAcademicoDto= null;
		mifListArancel= null;
		mifListNivelDto= null;
		
		return "irInicio";
	}
	
	// ****************************************************************/
	// *********************** MÉTODOS LIMPIEZA  **********************/
	// ****************************************************************/
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiarFormEstudiantes() {
		
		mifIdentificacion = new String();
		mifPrimerApellido = new String();
		mifListFichaInscripcionDto = null;
		
	}
	
	
	
	

	
	
	// ****************************************************************/
	// *********************** MÉTODOS GENERALES **********************/
	// ****************************************************************/
	public void busquedaPorIdentificacion(){
		
		if (mifIdentificacion.length() > 0) {
			mifPrimerApellido = new String();
			mifTipoBusqueda = Integer.valueOf(0);
		}
		
	}

	public void busquedaPorApellido() {
		
		if (mifPrimerApellido.length() > 0) {
			mifIdentificacion = new String();
			mifTipoBusqueda = Integer.valueOf(1);
		}
		
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void filtroBusquedaEstudiante() {

		if (mifIdentificacion.length() > 0 || mifPrimerApellido.length() > 0) {
			if (mifTipoBusqueda.equals(Integer.valueOf(0))) {
				List<FichaInscripcionDto> fichasInscripcion = cargarFichasInscripcionIntercambioFiltro(mifIdentificacion,new Integer[]{FichaInscripcionConstantes.ACTIVO_VALUE}, new Integer[]{FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE},mifTipoBusqueda, mifPeriodoAcademico.getPracId());
				if (fichasInscripcion != null && fichasInscripcion.size() > 0) {
					mifListFichaInscripcionDto = generarListaFichaInscripcion(fichasInscripcion);	
				}else {
					mifListFichaInscripcionDto = null;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("No se encontró resultados con la identificación ingresada.");	
				}

			}else {
				List<FichaInscripcionDto> fichasInscripcion = cargarFichasInscripcionIntercambioFiltro(mifPrimerApellido,new Integer[]{FichaInscripcionConstantes.ACTIVO_VALUE}, new Integer[]{FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE},mifTipoBusqueda,mifPeriodoAcademico.getPracId());

				if (fichasInscripcion != null && fichasInscripcion.size() > 0) {
					mifListFichaInscripcionDto = generarListaFichaInscripcion(fichasInscripcion);	
				}else {
					mifListFichaInscripcionDto = null;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("No se encontró resultados con la identificación ingresada.");	
				}
			}

		} else {
			
			List<FichaInscripcionDto> fichasInscripcion = cargarFichasInscripcionIntercambio(new Integer[]{FichaInscripcionConstantes.ACTIVO_VALUE}, new Integer[]{FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE},mifPeriodoAcademico.getPracId());
			if (fichasInscripcion != null && fichasInscripcion.size() > 0) {
				mifListFichaInscripcionDto = generarListaFichaInscripcion(fichasInscripcion);	
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se han encontrado alumnos de intercambio registrados en el SIIU.");
			}
			
		}

	}
	
	private List<FichaInscripcionDto> generarListaFichaInscripcion(List<FichaInscripcionDto> fichasInscripcion){
		List<FichaInscripcionDto> retorno = new ArrayList<>();
		
		Map<String, FichaInscripcionDto> mapFichasInscripcion =  new HashMap<String, FichaInscripcionDto>();
		for (FichaInscripcionDto it : fichasInscripcion) {
			mapFichasInscripcion.put(it.getPrsIdentificacion(), it);
		}
		for (Entry<String, FichaInscripcionDto> item : mapFichasInscripcion.entrySet()) {
			retorno.add(item.getValue());
		}

		retorno.sort(Comparator.comparing(FichaInscripcionDto::getPrsPrimerApellido).thenComparing(Comparator.comparing(FichaInscripcionDto::getPrsSegundoApellido)));
		
		return retorno;
	}
	
	public void eliminarMateria(MateriaDto materia){
		mifListMateriaDto.remove(materia);
	}
	
	

	public void inicializarModalAgregarMaterias(List<FichaInscripcionDto> fichasInscripcion){
		List<Carrera> carreras = new ArrayList<>();
		for (FichaInscripcionDto item : fichasInscripcion) {
			if (item.getPrsIdentificacion().equals(mifFichaInscripcionDto.getPrsIdentificacion())) {
				Carrera crr = new Carrera();
				crr.setCrrId(item.getCrrId());
				crr.setCrrDescripcion(item.getCrrDescripcion());
				carreras.add(crr);
			}
		}
		
		mifListCarrera = carreras;
	}

	public void buscarCarreras(int facultadId){
		List<Carrera> carreras = cargarCarreras(facultadId);
		if (carreras != null && carreras.size() > 0) {
			carreras.sort(Comparator.comparing(Carrera::getCrrDescripcion));
			mifListCarrera = carreras;
		}
	}
	
	public void buscarMaterias(int carreraId){
		if (carreraId != GeneralesConstantes.APP_ID_BASE) {
			List<MateriaDto> materias = cargarMallaCurricular(carreraId, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
			if (materias != null && materias.size() > 0) {
				materias.sort(Comparator.comparing(MateriaDto::getNvlNumeral).thenComparing(Comparator.comparing(MateriaDto::getMtrDescripcion)));
				mifListMallaCurricularMateriaDto = materias;	
			}else {
				mifListMallaCurricularMateriaDto = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontró materias con la carrera seleccionada.");
			}
			
			List<NivelDto> niveles = cargarNiveles(carreraId);
			if (niveles != null && niveles.size() > 0) {
				niveles.sort(Comparator.comparing(NivelDto::getNvlId));
				mifNivelId = GeneralesConstantes.APP_ID_BASE;
				mifListNivelDto = niveles;	
			}else{
				mifListNivelDto = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontró niveles en la malla curricular de la carrera seleccionada.");
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Seleccione una carrera para continuar con la búsqueda.");
		}
		 
	}
	
	public void buscarMateriasPorNivel(int nivelId){
		List<MateriaDto> retorno = null;

		if (mifCarreraId != null && mifCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			List<MateriaDto> materias = cargarMallaCurricular(mifCarreraId, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
			
			if (nivelId != GeneralesConstantes.APP_ID_BASE) {
				if (materias != null && materias.size() > 0) {
					retorno = new ArrayList<>();
					for (Iterator<MateriaDto>  iter = materias.iterator(); iter.hasNext();) {
						final MateriaDto item = iter.next();
						if (item.getNvlNumeral().equals(nivelId)) {
							retorno.add(item);
						}
					}
				}
				
				if (retorno != null && retorno.size() > 0) {
					retorno.sort(Comparator.comparing(MateriaDto::getNvlNumeral).thenComparing(Comparator.comparing(MateriaDto::getMtrDescripcion)));
					mifListMallaCurricularMateriaDto = null;
					mifListMallaCurricularMateriaDto = retorno;
				}else {
					FacesUtil.mensajeInfo("No se encontró resultados con los parámetros solicitados.");
				}
				
			}else {
				if (materias != null && materias.size() > 0) {
					materias.sort(Comparator.comparing(MateriaDto::getNvlNumeral).thenComparing(Comparator.comparing(MateriaDto::getMtrDescripcion)));
					mifListMallaCurricularMateriaDto = null;
					mifListMallaCurricularMateriaDto = materias;
				}else {
					FacesUtil.mensajeInfo("No se encontró resultados con los parámetros solicitados.");
				}
			}
		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Seleccione una carrera para continuar con la búsqueda.");;
		}
		
	}
	
	
		
	public void limpiarModalAgregarMaterias(){
		mifListMallaCurricularMateriaDto = new ArrayList<>();
		mifListNivelDto = new ArrayList<>();
		mifCarreraId = GeneralesConstantes.APP_ID_BASE;
	}
	
	
	public void agregarMaterias(MateriaDto  materia){

		MateriaDto retorno = verificarSiExisteEnLista(materia);
		if (retorno == null) {
			for (FichaInscripcionDto item : mifListSeleccionFichaInscripcionDto) {
				if (materia.getCrrId() == item.getCrrId()) {
					CarreraDto carrera = new CarreraDto();
					carrera.setCrrId(item.getCrrId());
					carrera.setCrrDescripcion(item.getCrrDescripcion());
					carrera.setFcinId(item.getFcinId());
					carrera.setCrrEstado(CarreraConstantes.CRR_ESTADO_ACTIVO_VALUE);
					materia.setFcinId(item.getFcinId());
					materia.setMtrCarreraDto(carrera);
					materia.setMtrEstado(MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE);
				}
			}
			mifListMateriaDto.add(materia);	
		}
		
		limpiarModalAgregarMaterias();
		
	}
	
	private MateriaDto verificarSiExisteEnLista(MateriaDto entidad){
		MateriaDto retorno = null;
		
		for (MateriaDto item: mifListMateriaDto) {
			if (item.getMtrId() == entidad.getMtrId()) {
				retorno = item;
			}
		}
		
		return retorno;
	}
	
	
	private List<FichaInscripcionDto> cargarFichasInscripcionIntercambioFiltro(String param, Integer[] estados, Integer[] tiposInscripcion, int tipoBusqueda, int pracId){
		List<FichaInscripcionDto> retorno  = null;
		try {
			
			retorno = servJdbcFichaInscripcionDto.buscarFichasInscripcion(param,estados,tiposInscripcion,tipoBusqueda, pracId);
			
			if (retorno != null && retorno.size() > 0) {
				retorno.sort(Comparator.comparing(FichaInscripcionDto::getPrsPrimerApellido).thenComparing(Comparator.comparing(FichaInscripcionDto::getPrsSegundoApellido)));
			}

			
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;		
	}
	
	private List<FichaInscripcionDto> cargarFichasInscripcionIntercambio(Integer[] estados, Integer[] tiposIngreso, int pracId){
		List<FichaInscripcionDto> retorno  = null;
		
		try {
			retorno = servJdbcFichaInscripcionDto.buscarFichasInscripcion(estados, tiposIngreso, pracId);

			if (retorno != null && retorno.size() > 0) {
				try {
					retorno.sort(Comparator.comparing(FichaInscripcionDto::getPrsPrimerApellido).thenComparing(Comparator.comparing(FichaInscripcionDto::getPrsSegundoApellido)));
				} catch (Exception e) {
				}
			}
			
		} catch (FichaInscripcionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaInscripcionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;		
	}
	
	
//	private List<Dependencia> cargarDependencias() {
//		try {
//			return servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
//		} catch (DependenciaNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
//			return null;
//		}
//	}
	
	private List<Carrera> cargarCarreras(int facultadId){
		try {
			return servCarrera.buscarCarreras(facultadId, CarreraConstantes.TIPO_PREGRADO_VALUE);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	
	
	private List<NivelDto> cargarNiveles(Integer carreraId){
		List<NivelDto> retorno = null;
		
		try {
			retorno = servJdbcNivelDto.listarNivelXCarrera(carreraId);
		} catch (NivelDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno; 
	}
		
	private List<MateriaDto>  cargarMallaCurricular(int carreraId, Integer [] mtrEstados){
		
		try {
			return servJdbcMateriaDto.listarMateriasxCarreraFull(carreraId, mtrEstados);
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}
	
	/**
	 * Metodo de verificacion de disponibilidad de la materia en el paralelo seleccionado
	 */
	public void verificarClickDisponibilidad(MateriaDto item, Boolean seleccion){
		if(seleccion){
			if(item.getPrlId() != GeneralesConstantes.APP_ID_BASE){
				if (verificarCupos(item.getMlcrmtId(), item.getPrlId(), mifPeriodoAcademico.getPracId(), true)) {
					
					boolean isSinCruce = true;
					if (verificarCruceHorarios(item, mifListMateriaDto, 1)) {// con lo que tiene la lista
						item.setPrlId(GeneralesConstantes.APP_ID_BASE);
						item.setMtrCmbEstado(true);
						isSinCruce = false;
					}
					
					if (isSinCruce) {
						if (verificarCruceHorarios(item, mifListMatriculadoMateriaDto, 2)) {// con lo que tiene matriculado
							item.setPrlId(GeneralesConstantes.APP_ID_BASE);
							item.setMtrCmbEstado(true);
						}	
					}
					
				}else {
					item.setPrlId(GeneralesConstantes.APP_ID_BASE);
					item.setMtrCmbEstado(true);
				}

				calcularNivelAsignado();
//				informacionSeleccion();
			}			
		}

	}
	
	public boolean verificarCruceHorarios(MateriaDto materiaSelect, List<MateriaDto> materiasCargadas, int tipo) {
		List<HorarioAcademicoDto> horarioSeleccion = null;

		try {
			horarioSeleccion = servJdbcHorarioAcademicoDto.buscarHorarioFull(materiaSelect.getPrlId(),materiaSelect.getMlcrmtId(), mifPeriodoAcademico.getPracId());

			if (horarioSeleccion != null && horarioSeleccion.size() > 0) {

				if (materiasCargadas != null && materiasCargadas.size() > 0) {

					if (tipo == 1) {

						for (MateriaDto mat : materiasCargadas) {

							try {

								if(mat.getMtrCmbEstado() != null && mat.getMtrCmbEstado() && mat.getPrlId()!= null && mat.getPrlId() != GeneralesConstantes.APP_ID_BASE){

									List<HorarioAcademicoDto> horarioCarrito = servJdbcHorarioAcademicoDto.buscarHorarioFull(mat.getPrlId(), mat.getMlcrmtId(), mifPeriodoAcademico.getPracId());
									if(mat.getPrlId() != materiaSelect.getPrlId() && materiaSelect.getMlcrmtId() != mat.getMlcrmtId()){
										if (horarioCarrito != null) {
											for (HorarioAcademicoDto hor1 : horarioCarrito) {
												for (HorarioAcademicoDto hor2 : horarioSeleccion) {
													if (hor2.getHracDia().equals(hor1.getHracDia())) {
														if ( hor2.getHoclHoInicio().equals(hor1.getHoclHoInicio()) && hor2.getHoclHoFin().equals(hor1.getHoclHoFin())) {
															FacesUtil.mensajeError("El paralelo seleccionado genera un cruce de horario en su matrícula." + mat.getMtrDescripcion());
															return true;
														}
													}
												}
											}
										} else {
											return false;
										}
									}
								}
							} catch (HorarioAcademicoDtoException e) {
								FacesUtil.mensajeError(e.getMessage());
								return true;
							} catch (HorarioAcademicoDtoNoEncontradoException e) {
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError("El horario no ha sido cargado en la asignatura "+mat.getMtrDescripcion()+". Comuníquese con el administrador de la carrera.");
								return true;
							}
						}

					} else {
						return false;
					}

					if (tipo == 2) {
						for (MateriaDto itemRecord : materiasCargadas) {
							List<HorarioAcademicoDto> horarioPeriodoVigente = null;

							try {
								horarioPeriodoVigente = servJdbcHorarioAcademicoDto.buscarHorarioFull(itemRecord.getPrlId(), itemRecord.getMlcrmtId(), itemRecord.getPracId());
								if (horarioPeriodoVigente != null && horarioPeriodoVigente.size() > 0) {
									if(itemRecord.getPrlId() != materiaSelect.getPrlId() && materiaSelect.getMlcrmtId() != itemRecord.getMlcrmtId()){
										for (HorarioAcademicoDto hor1 : horarioPeriodoVigente) {
											for (HorarioAcademicoDto hor2 : horarioSeleccion) {
												if (hor2.getHracDia().equals(hor1.getHracDia())) {
													if ( hor2.getHoclHoInicio().equals(hor1.getHoclHoInicio()) && hor2.getHoclHoFin().equals(hor1.getHoclHoFin())) {
														FacesUtil.mensajeError("El paralelo seleccionado genera cruce con el Horario de su matrícula. " + itemRecord.getMtrDescripcion());
														return true;
													}
												}
											}
										}
									}
								}

							} catch (HorarioAcademicoDtoException e) {
								FacesUtil.mensajeError(e.getMessage());
								return true;
							} catch (HorarioAcademicoDtoNoEncontradoException e) {
								FacesUtil.mensajeError("El horario no ha sido cargado en la asignatura "+itemRecord.getMtrDescripcion()+". Comuníquese con el administrador de la carrera.");
								return true;
							}

						}

					}else {
						return false;
					}


				} else {
					return false;
				}
			}


		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return true;
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.mensajeError("El horario no ha sido cargado en la asignatura "+materiaSelect.getMtrDescripcion()+". Comuníquese con la Secretaria de la Carrera de Idiomas.");
			return true;
		}

		return false;
	}

	
	/**
	 * Verifica la existencia de cupos disponibles.
	 * @param mlcrmtID .- id de Malla curricular materia 
	 * @param prlId .- id de Paralelo 
	 * @return Retorna la respuesta
	 */
	public Boolean verificarCupos(int mlcrmtID, int prlId, int pracId, boolean mensaje){
		Boolean verificar = true;
		try {
			
			if(prlId != GeneralesConstantes.APP_ID_BASE){
				
				List<Integer> cupos = servJdbcMatricula.buscarCuposAndMatriculados(mlcrmtID, prlId, pracId);

				if(cupos.get(1) >= cupos.get(0) ){
					verificar = false;
				}

				if(mensaje){
					if(!verificar){
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.cupos.llenos")));
					}
				}
			}

		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.cupos.llenos")));
			verificar = false;
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.cupos.llenos")));
			verificar = false;
		}
		
		return verificar;
	}

	
	/**
	 * Reinicia el valor del check y paralelo de la materia seleccionada
	 * @param materia .- materia a usar
	 */
	public void resetComboParalelo(MateriaDto materia) {
		calcularNivelAsignado();
//		informacionSeleccion();
		materia.setPrlId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Genera la matricula del estudiante.
	 */
	public String generarMatricula(){
		
		if(!verificarSiRegistraMatricula(mifFichaInscripcionDto)){
			if(verificarCronogramaSemestreCarrera(3, mifNivelUbicacion, mifProcesoFlujo.getPrlfId(), mifFichaInscripcionDto.getCrrId(), true)){
				if(verificarSeleccion(true)){
				 
					Boolean verificarCupos = false;
					Nivel nivel = null;

					try {

						List<MateriaDto> materiasMatricula = new ArrayList<>();
						for(MateriaDto item: mifListMateriaDto){
							if(item.getMtrCmbEstado()){
								Paralelo paralelo = servParalelo.buscarPorId(item.getPrlId());
								item.setPrlDescripcion(paralelo.getPrlDescripcion());
								item.setValorMatricula(BigDecimal.ZERO);
								materiasMatricula.add(item);

								if(!verificarCupos(item.getMlcrmtId(), item.getPrlId(), mifPeriodoAcademico.getPracId(), true)){
									if(!verificarCupos){
										verificarCupos = true;
									}
								}


							}
						}

						if(!verificarCupos){
							contadorUnicoMatriculas = contadorUnicoMatriculas+1;
						}
						
						if(!verificarCupos && contadorUnicoMatriculas == 1){

							nivel = servNivel.listarNivelXNumeral(mifNivelUbicacion);
							ComprobantePago numeroComprobante ;

							numeroComprobante = servMatricula.generarMatriculaPregradoFull(
									materiasMatricula, 
									mifFichaInscripcionDto, 
									mifIsEstudianteNuevo, 
									mifProcesoFlujo, 
									mifPlanificacionCronograma, 
									null, 
									nivel.getNvlId(), 
									mifPeriodoAcademico,
									BigDecimal.ZERO,
									GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_VALUE,
									null
									);

							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.generar.matricula.exitoso")));

							contadorUnicoMatriculas = contadorUnicoMatriculas+1;

							mifListMatriculaExitosaMateriaDto = materiasMatricula;
							
							return enviarMailRegistroMatricula(mifFichaInscripcionDto, nivel, numeroComprobante, mifListMatriculaExitosaMateriaDto);

						}else{  
							contadorUnicoMatriculas = 0;
							return null; 
						}

					}catch (ParaleloNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (ParaleloException e) {
						FacesUtil.mensajeError(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (NivelNoEncontradoException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (NivelException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (MatriculaValidacionException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					} catch (MatriculaException e) {
						FacesUtil.mensajeInfo(e.getMessage());
						contadorUnicoMatriculas = 0;
						return "irInicio";
					}  

				}
				if(!verificarSeleccion(false)){
					contadorUnicoMatriculas = 0;
					return null;
				}
			}else{
				contadorUnicoMatriculas = 0;
				return null;
			}
		}else{
			contadorUnicoMatriculas = 0;
			return "irInicio";
		}
		
		return "irInicio";
	}

	/**
	 * Envia mail de registro de matricula 
	 *  @param fichaInscripcionDto .- ficha inscripcion a procesar
	 *  @param nivel .- nivel calculado
	 **/
	public String enviarMailRegistroMatricula(FichaInscripcionDto fichaInscripcionDto, Nivel nivel, ComprobantePago numeroComprobante, List<MateriaDto> listMateriaDto){

		try{

			StringBuilder sbNombres = new StringBuilder(); 
			sbNombres.append(fichaInscripcionDto.getPrsPrimerApellido());sbNombres.append(" ");
			sbNombres.append(fichaInscripcionDto.getPrsSegundoApellido());sbNombres.append(" ");
			sbNombres.append(fichaInscripcionDto.getPrsNombres());sbNombres.append(" ");

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

			//lista de correos a los que se enviara el mail
			List<String> correosTo = new ArrayList<>();
			correosTo.add(mifUsuarioSeleccionado.getUsrPersona().getPrsMailInstitucional());

			//path de la plantilla del mail
			ProductorMailJson pmail = null;
			StringBuilder sbCorreo= new StringBuilder();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			
			sbCorreo= MailRegistroMatricula.generarMailRegistroMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
					GeneralesUtilidades.generaStringParaCorreo(sbNombres.toString()) , GeneralesUtilidades.generaStringParaCorreo(
					mifCarrera.getCrrDescripcion()),
					GeneralesUtilidades.generaStringParaCorreo(nivel.getNvlDescripcion()),  
					mifFacultad.getDpnDescripcion(),
					obtenerDescripcionGratuidad(3),
					numeroComprobante.getCmpaTotalPago().toString()
					);
			
			pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,MailConstantes.ENCABEZADO_CORREO_REGISTRO_MATRICULA_LABEL + mifPeriodoAcademico.getPracDescripcion().replace("-", " - "),
					sbCorreo.toString() , "admin", "dt1c201s", true, ReporteRegistroMatriculaForm.generarReporteRegistroMatriculaIntercambioMail(listMateriaDto, nivel, mifPeriodoAcademico, fichaInscripcionDto, mifUsuarioSeleccionado, mifFacultad, mifCarrera, obtenerDescripcionGratuidad(3)), "Registro de Matricula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
			String jsonSt = pmail.generarMail();
			Gson json = new Gson();
			MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
			// 	Iniciamos el envÃ­o de mensajes
			ObjectMessage message = session.createObjectMessage(mailDto);
			producer.send(message);

		} catch (Exception e) {
			System.out.println("Error Codigo: AX324 : Error al enviar el registro de matriculas por mail.");
		}

		return "irInicio";
		
//		Se restringue el envio de la oreden de cobro por no generacion de comprobante hasta la generacion en la tarde.
//		return enviarMailOrdenCobro(fichaInscripcionDto, nivel, numeroComprobante, listMateriaDto);

	}
	
	/**
	 * Cabecera de la columna que varia entre creditos y horas 
	 */
	public String obtenerDescripcionGratuidad(int tipoGratuidad){
		
		String descripcion = null;
		
		switch (tipoGratuidad) {
		case 1:
			descripcion = GratuidadConstantes.GRATUIDAD_PERDIDA_DEFINITIVA_LABEL;
			break;
		case 2:
			descripcion = GratuidadConstantes.GRATUIDAD_PERDIDA_TEMPORAL_LABEL;
			break;
		case 3:
			descripcion = GratuidadConstantes.GRATUIDAD_APLICA_GRATUIDAD_LABEL;
			break;

		default:
			descripcion = GratuidadConstantes.GRATUIDAD_ERROR;
			break;
		}
		return descripcion;
	}
	
	
	/**
	 * Verifica los parametros necesarios en ls elementos seleccionados
	 * @return valida o no para generar matricula
	 */
	public Boolean verificarSeleccion(Boolean mensaje){
		Boolean verificar = true;

		List<MateriaDto> materiasSeleccionadas = new ArrayList<>();
		
		if (mifListMateriaDto != null && mifListMateriaDto.size() > 0  ) {
			
			Integer numeroTotalCreditosHoras = 0;
			Boolean seleccionMaterias = false;
			MateriaDto paraleloNoSeleccionado = null;
			
			for (MateriaDto it : mifListMateriaDto) {
				if (it.getMtrCmbEstado()) {
					seleccionMaterias = true;
					if(it.getPrlId() == null || it.getPrlId() == GeneralesConstantes.APP_ID_BASE){
						paraleloNoSeleccionado = it;
					}
				}
			}
			
			if(!seleccionMaterias){
				if(!mensaje){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.seleccion.seleccione.asignaturas")));
				} 
				return  false;
			}
			
			if(paraleloNoSeleccionado != null){
				if(!mensaje){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaGenerarMatriculaPregradoForm.verificar.seleccion.seleccione.paralelo", paraleloNoSeleccionado.getMtrDescripcion())));
				}
				return  false;
			}
			
			for (MateriaDto it : mifListMateriaDto) {
				if (it.getMtrCmbEstado()) {
					if (it.getPrlId() != null && it.getPrlId() != GeneralesConstantes.APP_ID_BASE) {
						materiasSeleccionadas.add(it);
						if(it.getMtrHorasPorSemana() != null && it.getMtrHorasPorSemana() != 0){
							numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHorasPorSemana();
						}
					}else{
						verificar = false;
						break;
					}
				}
			}
			
				
				List<List<MateriaDto>> listasMateriasSeleccion = new ArrayList<>();

				Integer nivelAsignacion = 0;
				Integer numeroCreditos = 0;
				List<Integer> creditosXSemestre = new ArrayList<>();
				List<Integer> nivelXSemestre = new ArrayList<>();

				for(MateriaDto item: materiasSeleccionadas){
					List<MateriaDto> list = new ArrayList<>();
					for(MateriaDto itemR: materiasSeleccionadas){
						if( item.getNvlNumeral() == itemR.getNvlNumeral()){
							list.add(itemR);
						} 
					}
					if(list.size() > 0){
						listasMateriasSeleccion.add(list);
					}
				}

				for (int i = 0; i < listasMateriasSeleccion.size(); i++) {

					for(MateriaDto it: listasMateriasSeleccion.get(i)){

						if(it.getMtrHorasPorSemana() != null && it.getMtrHorasPorSemana() != 0){
							numeroTotalCreditosHoras = numeroTotalCreditosHoras+it.getMtrHorasPorSemana();
						}

					}

					Boolean adjuntar = true;
					for (int j = 0; j < nivelXSemestre.size(); j++) {

						if(nivelXSemestre.get(j) == nivelAsignacion){
							adjuntar = false;
						}
					}

					if(adjuntar){
						creditosXSemestre.add(numeroCreditos);
						nivelXSemestre.add(nivelAsignacion);
					}

					numeroCreditos = 0;
					nivelAsignacion = 0;
				}

				if(nivelXSemestre.size() > 1){

					for (int i = 0; i < creditosXSemestre.size(); i++) {
						for (int j = 0; j < creditosXSemestre.size(); j++) {
							if(creditosXSemestre.get(i) > creditosXSemestre.get(j)){
								numeroCreditos = creditosXSemestre.get(i);
								nivelAsignacion = nivelXSemestre.get(i);
							}
							if(creditosXSemestre.get(i) == creditosXSemestre.get(j)){
								if(nivelXSemestre.get(i) < nivelXSemestre.get(j)){
									numeroCreditos = creditosXSemestre.get(i);
									nivelAsignacion = nivelXSemestre.get(i);
								}
							}
						}
					}

				}else{
					numeroCreditos = creditosXSemestre.get(creditosXSemestre.size()-1);
					nivelAsignacion = nivelXSemestre.get(nivelXSemestre.size()-1);
				}

//				mifNivelUbicacion = nivelAsignacion;

		}else{
			verificar = false;
		} 
		
		return verificar;
	}
	
	// ****************************************************************/
	// *********************** ENCAPSULAMIENTO   **********************/
	// ****************************************************************/

	public String getMifIdentificacion() {
		return mifIdentificacion;
	}

	public void setMifIdentificacion(String mifIdentificacion) {
		this.mifIdentificacion = mifIdentificacion;
	}

	public String getMifPrimerApellido() {
		return mifPrimerApellido;
	}

	public void setMifPrimerApellido(String mifPrimerApellido) {
		this.mifPrimerApellido = mifPrimerApellido;
	}

	public Integer getMifTipoBusqueda() {
		return mifTipoBusqueda;
	}

	public void setMifTipoBusqueda(Integer mifTipoBusqueda) {
		this.mifTipoBusqueda = mifTipoBusqueda;
	}

	public Integer getMifPerdidaGratuidad() {
		return mifPerdidaGratuidad;
	}

	public void setMifPerdidaGratuidad(Integer mifPerdidaGratuidad) {
		this.mifPerdidaGratuidad = mifPerdidaGratuidad;
	}

	public Integer getMifNivelUbicacion() {
		return mifNivelUbicacion;
	}

	public void setMifNivelUbicacion(Integer mifNivelUbicacion) {
		this.mifNivelUbicacion = mifNivelUbicacion;
	}

	public Boolean getMifIsEstudianteNuevo() {
		return mifIsEstudianteNuevo;
	}

	public void setMifIsEstudianteNuevo(Boolean mifIsEstudianteNuevo) {
		this.mifIsEstudianteNuevo = mifIsEstudianteNuevo;
	}

	public Usuario getMifUsuario() {
		return mifUsuario;
	}

	public void setMifUsuario(Usuario mifUsuario) {
		this.mifUsuario = mifUsuario;
	}

	public TipoGratuidad getMifTipoGratuidad() {
		return mifTipoGratuidad;
	}

	public void setMifTipoGratuidad(TipoGratuidad mifTipoGratuidad) {
		this.mifTipoGratuidad = mifTipoGratuidad;
	}

	public CarreraDto getMifCarreraDto() {
		return mifCarreraDto;
	}

	public void setMifCarreraDto(CarreraDto mifCarreraDto) {
		this.mifCarreraDto = mifCarreraDto;
	}

	public FichaInscripcionDto getMifFichaInscripcionDto() {
		return mifFichaInscripcionDto;
	}

	public void setMifFichaInscripcionDto(FichaInscripcionDto mifFichaInscripcionDto) {
		this.mifFichaInscripcionDto = mifFichaInscripcionDto;
	}

	public PeriodoAcademico getMifPeriodoAcademico() {
		return mifPeriodoAcademico;
	}

	public void setMifPeriodoAcademico(PeriodoAcademico mifPeriodoAcademico) {
		this.mifPeriodoAcademico = mifPeriodoAcademico;
	}

	public PlanificacionCronograma getMifPlanificacionCronograma() {
		return mifPlanificacionCronograma;
	}

	public void setMifPlanificacionCronograma(PlanificacionCronograma mifPlanificacionCronograma) {
		this.mifPlanificacionCronograma = mifPlanificacionCronograma;
	}

	public CronogramaActividadJdbcDto getMifProcesoFlujo() {
		return mifProcesoFlujo;
	}

	public void setMifProcesoFlujo(CronogramaActividadJdbcDto mifProcesoFlujo) {
		this.mifProcesoFlujo = mifProcesoFlujo;
	}

	public List<FichaInscripcionDto> getMifListFichaInscripcionDto() {
		return mifListFichaInscripcionDto;
	}

	public void setMifListFichaInscripcionDto(List<FichaInscripcionDto> mifListFichaInscripcionDto) {
		this.mifListFichaInscripcionDto = mifListFichaInscripcionDto;
	}

	public List<CarreraDto> getMifListCarreraDto() {
		return mifListCarreraDto;
	}

	public void setMifListCarreraDto(List<CarreraDto> mifListCarreraDto) {
		this.mifListCarreraDto = mifListCarreraDto;
	}

	public List<MateriaDto> getMifListMateriaDto() {
		return mifListMateriaDto;
	}

	public void setMifListMateriaDto(List<MateriaDto> mifListMateriaDto) {
		this.mifListMateriaDto = mifListMateriaDto;
	}

	public List<ParaleloDto> getMifListParaleloDto() {
		return mifListParaleloDto;
	}

	public void setMifListParaleloDto(List<ParaleloDto> mifListParaleloDto) {
		this.mifListParaleloDto = mifListParaleloDto;
	}

	public List<HorarioAcademicoDto> getMifListHorarioAcademicoDto() {
		return mifListHorarioAcademicoDto;
	}

	public void setMifListHorarioAcademicoDto(List<HorarioAcademicoDto> mifListHorarioAcademicoDto) {
		this.mifListHorarioAcademicoDto = mifListHorarioAcademicoDto;
	}

	public List<Arancel> getMifListArancel() {
		return mifListArancel;
	}

	public void setMifListArancel(List<Arancel> mifListArancel) {
		this.mifListArancel = mifListArancel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Dependencia> getMifListFacultad() {
		return mifListFacultad;
	}

	public void setMifListFacultad(List<Dependencia> mifListFacultad) {
		this.mifListFacultad = mifListFacultad;
	}

	public List<Carrera> getMifListCarrera() {
		return mifListCarrera;
	}

	public void setMifListCarrera(List<Carrera> mifListCarrera) {
		this.mifListCarrera = mifListCarrera;
	}

	public Integer getMifFacultadId() {
		return mifFacultadId;
	}

	public void setMifFacultadId(Integer mifFacultadId) {
		this.mifFacultadId = mifFacultadId;
	}

	public Integer getMifCarreraId() {
		return mifCarreraId;
	}

	public void setMifCarreraId(Integer mifCarreraId) {
		this.mifCarreraId = mifCarreraId;
	}

	public List<MateriaDto> getMifListMallaCurricularMateriaDto() {
		return mifListMallaCurricularMateriaDto;
	}

	public void setMifListMallaCurricularMateriaDto(List<MateriaDto> mifListMallaCurricularMateriaDto) {
		this.mifListMallaCurricularMateriaDto = mifListMallaCurricularMateriaDto;
	}

	public Integer getMifNivelId() {
		return mifNivelId;
	}

	public void setMifNivelId(Integer mifNivelId) {
		this.mifNivelId = mifNivelId;
	}

	public List<NivelDto> getMifListNivelDto() {
		return mifListNivelDto;
	}

	public void setMifListNivelDto(List<NivelDto> mifListNivelDto) {
		this.mifListNivelDto = mifListNivelDto;
	}


	public List<FichaInscripcionDto> getMifListSeleccionFichaInscripcionDto() {
		return mifListSeleccionFichaInscripcionDto;
	}


	public void setMifListSeleccionFichaInscripcionDto(List<FichaInscripcionDto> mifListSeleccionFichaInscripcionDto) {
		this.mifListSeleccionFichaInscripcionDto = mifListSeleccionFichaInscripcionDto;
	}


	public List<FichaInscripcionDto> getMifListGeneralFichaInscripcionDto() {
		return mifListGeneralFichaInscripcionDto;
	}


	public void setMifListGeneralFichaInscripcionDto(List<FichaInscripcionDto> mifListGeneralFichaInscripcionDto) {
		this.mifListGeneralFichaInscripcionDto = mifListGeneralFichaInscripcionDto;
	}


	public List<MateriaDto> getMifListMatriculadoMateriaDto() {
		return mifListMatriculadoMateriaDto;
	}


	public void setMifListMatriculadoMateriaDto(List<MateriaDto> mifListMatriculadoMateriaDto) {
		this.mifListMatriculadoMateriaDto = mifListMatriculadoMateriaDto;
	}


	public List<MateriaDto> getMifListMatriculaExitosaMateriaDto() {
		return mifListMatriculaExitosaMateriaDto;
	}


	public void setMifListMatriculaExitosaMateriaDto(List<MateriaDto> mifListMatriculaExitosaMateriaDto) {
		this.mifListMatriculaExitosaMateriaDto = mifListMatriculaExitosaMateriaDto;
	}


	public Usuario getMifUsuarioSeleccionado() {
		return mifUsuarioSeleccionado;
	}


	public void setMifUsuarioSeleccionado(Usuario mifUsuarioSeleccionado) {
		this.mifUsuarioSeleccionado = mifUsuarioSeleccionado;
	}

	
	
}