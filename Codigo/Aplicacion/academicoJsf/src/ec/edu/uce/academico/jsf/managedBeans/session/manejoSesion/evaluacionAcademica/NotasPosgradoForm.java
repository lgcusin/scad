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
   
 ARCHIVO:     NotasPosgradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja las notas de los estudiantes por docente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 03/03/2018 			Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
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
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPosgradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteNotasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.IdentifidorCliente;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * Clase (managed bean) NotasPosgradoForm.
 * Managed Bean que administra los estudiantes para el ingreso de las notas por docentes.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="notasPosgradoForm")
@SessionScoped
public class NotasPosgradoForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario npfUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto npfDocente;

	//PARA BUSQUEDA
	private EstudianteJdbcDto npfEstudianteBuscar;
	private List<CarreraDto> npfListCarreraBusq;
	private List<NivelDto> npfListNivelBusq;
	private List<MateriaDto> npfListMateriaBusq;
	private List<EstudianteJdbcDto> npfListEstudianteBusq;
	private List<ParaleloDto> npfListParaleloBusq;
	private List<ParaleloDto> npfListParaleloBusqIndividual;
	
	private List<RolFlujoCarrera> npfListRolFlujoCarreraBusq;
	
	private List<DocenteJdbcDto> npffListCarreraDocenteBusq;
	private List<DocenteJdbcDto> npffListNivel;
	private Integer npfCrrId;
	
	private ParaleloDto npfParaleloDtoEditar;
	
	private CronogramaActividadJdbcDto npfCronogramaActividadJdbcDtoBuscar;
	
	private Dependencia npfDependenciaBuscar;
	
	//PARA GUARDAR LA ASISTENCIA DEL DOCENTE DEL FORM
	private Integer npfAsistenciaDocente;
	private Integer npfAsistenciaDocente2;
	//PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private Integer npfValidadorClic;
	private String npfEstado;
	//PARA GUARDAR LOS REGISTROS DE LA SESION DEL CLIENTE HOSTNAME, IPPUBLICA, IPPRIVADA
	private String npfRegCliente;
	private Integer npfContadorEstudiante;
	
	//private String thisIpAddress;
	
	//campos para el envio de la notificacion del ingreso final de notas al mail del docente	
	private String npfNomCarrera;
	private String npfNomMateria;
	private String npfNomParalelo;
	private boolean npfActivadorGuardar;
	private CarreraDto npfCarrera;
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
			
	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	private CarreraDtoServicioJdbc servNpfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servNpfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servNpfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servNpfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servNpfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servNpfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servNpfParaleloDtoServicioJdbc;
	@EJB
	private NotasPosgradoDtoServicioJdbc servNpfNotasPosgradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servNpfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNpfMateriaDto;
	@EJB
	private CalificacionServicio servNpfCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servNpfRolFlujoCarreraServicio;
	
	@EJB
	private UsuarioRolServicio servNpfUsuarioRolServicio;
	
	@EJB
	private DependenciaServicio servNpfDependenciaServicio;
	
	@EJB
	private CronogramaActividadDtoServicioJdbc servNpfCronogramaActividadDtoServicioJdbcServicio;
	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;
	@EJB
	private PersonaServicio servPersonaServicio;
	@EJB
	private RecordEstudianteServicio servNpfRecordEstudianteServicio;
	@EJB
	private FichaInscripcionServicio servNpfFichaInscripcionServicio;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irAnotaPosgrado(Usuario usuario){
		npfUsuario = usuario;
		String retorno = null;
		try {
			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(npfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERAPOSGRADO_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
//			npfListRolFlujoCarreraBusq = servNpfRolFlujoCarreraServicio.buscarPorIdUsuario(npfUsuario);
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			npffListCarreraDocenteBusq = servNpfDocenteDtoServicioJdbc.buscarCarrerasDirectorPosgrado(npfUsuario.getUsrIdentificacion());
			npfListParaleloBusq = null;
			
			
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
//			npfListCarreraBusq = servNpfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivo(npfUsuario.getUsrId(), RolConstantes.ROL_BD_DOCENTE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, npfListRolFlujoCarreraBusq);
			retorno = "irAnotaPosgrado";
//		} catch (CarreraDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (CarreraDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError("No se encontraron programas de posgrados asignados al coordinador.");
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontraron programas de posgrados asignados al coordinador.");
		} 
		return retorno;
	}
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		iniciarParametros();
		return "irInicio";
	}
	
	
	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * @param idCarrera - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera){
		try {
			npfCarrera = servNpfCarreraDtoServicioJdbc.buscarXId(idCarrera);
		} catch (Exception e1) {
		} 
		idCarrera = npfEstudianteBuscar.getCrrId();
		npfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		npfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		npfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		npffListNivel = null;
		npfListMateriaBusq = null;
		npfListParaleloBusq = null;
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				 try {
					Persona prsAux = servPersonaServicio.buscarPorIdentificacion(npfUsuario.getUsrIdentificacion());
					npfDocente = new DocenteJdbcDto();
					npfDocente.setPrsNombres(prsAux.getPrsNombres());
					npfDocente.setPrsPrimerApellido(prsAux.getPrsPrimerApellido());
					npfDocente.setPrsSegundoApellido(prsAux.getPrsSegundoApellido());
					npfDocente.setPrsMailInstitucional(prsAux.getPrsMailInstitucional());
				} catch (PersonaNoEncontradoException | PersonaException e) {
				}
//				npfDocente = servNpfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodoActivo(npfUsuario.getUsrId(), npfEstudianteBuscar.getCrrId(), TipoPuestoConstantes.TIPO_DOCENTE_VALUE);
				
				//LISTO LOS NIVELES
				npffListNivel = servDocenteDtoServicioJdbc.buscarNivelesProgramaPosgrado(npfEstudianteBuscar.getCrrId());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
			}
		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.detalle.puesto.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
		}
	}
	
	
	/**
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idNivel){
		idNivel = npfEstudianteBuscar.getNvlId();
		npfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		npfListMateriaBusq = null;
		npfCrrId = npfEstudianteBuscar.getCrrId();
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				npfListMateriaBusq = servNpfMateriaDtoServicioJdbc.listarMateriasActivasPosgrado(npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.no.encontrado.exception")));
		}
	}
	
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		npfListParaleloBusq = null;
		try {
			if(npfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			}else if(npfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else if(npfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			}else{
				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
				npfListParaleloBusq = servNpfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelPosgrado(npfEstudianteBuscar.getCrrId(), npfEstudianteBuscar.getNvlId(), npfEstudianteBuscar.getMtrId());
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Metodo que llama los datos para la genracion del reporte
	 */
	public String  reporteNotas(){
		return "irInicio";
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas(ParaleloDto prl){
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar =  servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
			if(npfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
			}else{
				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_POSGRADO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
			}
			Timestamp myDate = new Timestamp(new Date().getTime());
			
			if(myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			
			if(myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS POSGRADO COORDINADOR");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");
			
			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");	
				} catch (Exception e) {
				}
			}
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			boolean op=true;
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(item.getClfAsistenciaDocente1() != null && op){
					npfAsistenciaDocente = item.getClfAsistenciaDocente1();
					op=false;
				}else{
					npfAsistenciaDocente = null;
				}
				if(item.getClfNota2()!=null){
					item.setClfNota1(item.getClfNota2());
					item.setClfAsistenciaEstudiante1(item.getClfAsistenciaEstudiante2());
					item.setClfAsistenciaDocente1(item.getClfAsistenciaDocente2());
				}
				
			}
//			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
//				if(item.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("No puede registrar notas y asistencia ya que realizo un guardado final");
//					return null;
//				}
//				break;
//				
//			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
//		} catch (DependenciaException e) {
//			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irIngresarNotas";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotas2(ParaleloDto prl){
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar =  servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS POSGRADO COORDINADOR");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");
			
			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");	
				} catch (Exception e) {
				}
			}
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			
		
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente1() != null)){
//					if(item.getClfAsistenciaDocente1()!=0){
						
//					}else{
						
//					}
						npfAsistenciaDocente = null;
//						break;
					
				}else{
					npfAsistenciaDocente = item.getClfAsistenciaDocente1();
					break;
				}
				
			}
//			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
//				if(item.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("No puede registrar notas y asistencia ya que realizo un guardado final");
//					return null;
//				}
//				break;
//				
//			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron estudiantes con los parámetros seleccionados.");
			 
		} 
		return "irIngresarNotas2";
	}
	
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotasIndividual(ParaleloDto prl){
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar =  servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
			if(npfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
			}else{
				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_PRIMER_PARCIAL_VALUE);
			}
			Timestamp myDate = new Timestamp(new Date().getTime());
			
			if(myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
				return null;
			}
			
			if(myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Está fuera de las fechas establecidas.");
				return null;
			}
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS POSGRADO COORDINADOR");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");
			
			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");	
				} catch (Exception e) {
				}
			}
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			
		
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente1() != null)){
//					if(item.getClfAsistenciaDocente1()!=0){
						
//					}else{
						
//					}
						npfAsistenciaDocente = null;
//						break;
					
				}else{
					npfAsistenciaDocente = item.getClfAsistenciaDocente1();
					break;
				}
				
			}
//			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
//				if(item.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("No puede registrar notas y asistencia ya que realizo un guardado final");
//					return null;
//				}
//				break;
//				
//			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
//		} catch (DependenciaException e) {
//			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irIngresarNotas";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irIngresarNotasIndividual2(ParaleloDto prl){
		npfContadorEstudiante = 0;
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar =  servNpfDependenciaServicio.buscarFacultadXcrrId(npfCrrId);
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: NOTAS POSGRADO COORDINADOR");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");
			
			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");	
				} catch (Exception e) {
				}
			}
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(item.getClfAsistenciaDocente1() != null ){
					npfAsistenciaDocente = item.getClfAsistenciaDocente1();
					break;
				}else{
					npfAsistenciaDocente = null;
				}
				if(item.getClfAsistenciaDocente2() != null ){
					npfAsistenciaDocente = item.getClfAsistenciaDocente2();
					break;
				}else{
					npfAsistenciaDocente = null;
				}
			}
			
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				
				if(item.getClfNota2()!=null){
					item.setClfNota1(item.getClfNota2());
					item.setClfAsistenciaEstudiante1(item.getClfAsistenciaEstudiante2());
					item.setClfAsistenciaDocente1(item.getClfAsistenciaDocente2());
				}
				}
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				npfContadorEstudiante++;
				item.setPrsNumeracion(npfContadorEstudiante);
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron estudiantes con los parámetros seleccionados.");
		} 
		return "irIngresarNotas2";
	}
	
	
	/*
	 * Metodo que permite generar el reporte
	 */
	public void generarReporte(ParaleloDto prl){
		
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
	}
	
	@SuppressWarnings("static-access")
	public String irVerNotas(ParaleloDto prl){
		ReporteNotasForm reporte = new ReporteNotasForm();
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			activacion();
			reporte.generarReporteNotas(npfListEstudianteBusq, npfUsuario);
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(item.getClfNota1()!=null ){
					String nota = item.getClfNota1().toString();
					int puntoDecimalUbc = nota.indexOf('.');
					int totalDecimales = nota.length() - puntoDecimalUbc -1;
					if(puntoDecimalUbc==-1){
						item.setClfNota1String(nota+".00");
					}else if (totalDecimales==1){
						item.setClfNota1String(nota+"0");
					}else{
						item.setClfNota1String(nota);
					}
				}
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas";
	}
	
	@SuppressWarnings("static-access")
	public String irVerNotas2(ParaleloDto prl){
		ReporteNotasForm reporte = new ReporteNotasForm();
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npfListEstudianteBusq = servNpfNotasPosgradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npfParaleloDtoEditar.getCrrId(), npfParaleloDtoEditar.getMlcrmtNvlId(), npfParaleloDtoEditar.getPrlId(), npfParaleloDtoEditar.getMtrId(),npfParaleloDtoEditar.getMlcrprId());
			activacion();
			reporte.generarReporteNotasPosgrado(npfListEstudianteBusq, npfUsuario);
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(item.getClfNota1()!=null ){
					String nota = item.getClfNota1().toString();
					int puntoDecimalUbc = nota.indexOf('.');
					int totalDecimales = nota.length() - puntoDecimalUbc -1;
					if(puntoDecimalUbc==-1){
						item.setClfNota1String(nota+".00");
					}else if (totalDecimales==1){
						item.setClfNota1String(nota+"0");
					}else{
						item.setClfNota1String(nota);
					}
				}else{
					try {
						String nota = item.getClfNota2().toString();
						int puntoDecimalUbc = nota.indexOf('.');
						int totalDecimales = nota.length() - puntoDecimalUbc -1;
						if(puntoDecimalUbc==-1){
							item.setClfNota2String(nota+".00");
						}else if (totalDecimales==1){
							item.setClfNota2String(nota+"0");
						}else{
							item.setClfNota2String(nota);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas2";
	}
	
	/**
	 * Método que valida si el valor de la celda ha cambiado
	 * @param event.- cambio de foco de la celda selecionada
	 */
//	public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//        if(newValue != null && !newValue.equals(oldValue)) {
//        	 FacesMessage msg = new FacesMessage("Nota cambiada", "Anterior: " + oldValue + ", Nueva:" + newValue);
//        	 msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//             FacesContext.getCurrentInstance().addMessage(null, msg);
//        	npfListEstudianteBusq.get(event.getRowIndex()).getFcesId();
//        	npfListEstudianteBusq.get(event.getRowIndex()).getMlcrprId();
//        }
//    }
	
	
	
	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por carrera nivel y materia 
	 * @return Xhtml listar
	 */
	public String irCancelar(){
		npfParaleloDtoEditar = null;
		npfListEstudianteBusq = null;
		npfValidadorClic = new Integer(0);
		return "irListarParalelos";
	}
	
	/**
	 * Método calcula el porcentaje de asistencia
	 * @param porcentaje .- valor del porcentaje del calculo
	 * @param asitenciaEst.- valor de la asistencia de estudiante
	 * @param asitenciaDoc.- valor de la asistencia del docente
	 * @return.- valor del porsentaje en bigdecimal con dos decimales sin redondeo
	 */
	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		 BigDecimal itemCost  = BigDecimal.ZERO;
	     itemCost  = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje)).divide(new BigDecimal(asitenciaDoc), 2, RoundingMode.HALF_UP);
	     return itemCost;
	}
	
	/**
	 * verifica que haga click en el boton guardar temporal la nota
	 */
	public String verificarClicGuardadoTemporal(){
//		si la asistencia docente es distinta de null
		if(npfAsistenciaDocente!=null){
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				item.setClfAsistenciaDocente1(npfAsistenciaDocente);
				if(item.getClfNota1()!=null&&item.getClfAsistenciaEstudiante1()!=null){
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
					if(!validadorAsistencia(cadena, npfAsistenciaDocente)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es válida");
						npfValidadorClic = 0;
						return null;
					}
					if(!validador(item.getClfNota1())){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es válida");
						npfValidadorClic = 0;
						return null;
					}
				}
				
				if(item.getClfNota1()==null){
					if(item.getClfAsistenciaEstudiante1()!=null){
						String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
						if(!validadorAsistencia(cadena, npfAsistenciaDocente)){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
							npfValidadorClic = 0;
							return null;
						}
					}
				}
				
				if(item.getClfAsistenciaEstudiante1()==null){
					if(item.getClfNota1()!=null){
						if(!validador(item.getClfNota1())){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
							npfValidadorClic = 0;
							return null;
						}
					}
				}
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor, ingrese las horas de clase del docente en la asignatura");
			return null;
		}
		npfValidadorClic = 1;
		return null;
	}
	
	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal(){
		boolean op = true;
		if(npfAsistenciaDocente!=null){
			
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				item.setClfAsistenciaDocente1(npfAsistenciaDocente);
				if(item.getClfNota1()!=null&&item.getClfAsistenciaEstudiante1()!=null){
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
					if(!validadorAsistencia(cadena, npfAsistenciaDocente)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
						npfValidadorClic = 0;
						op = false;
						break;
					}
					if(!validador(item.getClfNota1())){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
						npfValidadorClic = 0;
						op = false;
						break;
					}
					
				}
				
				if(!(item.getClfNota1()!=null) && item.getClfAsistenciaEstudiante1()!=null){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
							npfValidadorClic = 0;
							op = false;
							break;
				}
				
				if(!(item.getClfAsistenciaEstudiante1()!=null) && item.getClfNota1()!=null){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
							npfValidadorClic = 0;
							op = false;
							break;
				}
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor, ingrese las horas de clase del docente en la asignatura");
			return null;
		}
		if(op){
			npfValidadorClic = 2;
		}else{
			npfValidadorClic = 0;
		}
		
		
		return null;
	}
	
	
	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal2(){
		boolean op = true;
		if(npfAsistenciaDocente!=null){
			
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				item.setClfAsistenciaDocente1(npfAsistenciaDocente);
				if(item.getClfNota1()!=null&&item.getClfAsistenciaEstudiante1()!=null){
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante1());
					if(!validadorAsistencia(cadena, npfAsistenciaDocente)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
						npfValidadorClic = 0;
						op = false;
						break;
					}
					if(!validador(item.getClfNota1())){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
						npfValidadorClic = 0;
						op = false;
						break;
					}
					
				}
				
				if(!(item.getClfNota1()!=null) && item.getClfAsistenciaEstudiante1()!=null){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
							npfValidadorClic = 0;
							op = false;
							break;
				}
				
				if(!(item.getClfAsistenciaEstudiante1()!=null) && item.getClfNota1()!=null){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
							npfValidadorClic = 0;
							op = false;
							break;
				}
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor, ingrese las horas de clase del docente en la asignatura");
			return null;
		}
		if(op){
			npfValidadorClic = 2;
		}else{
			npfValidadorClic = 0;
		}
		
		
		return null;
	}
	
	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista de paralelos
	 * @return XHTML listar paralelos
	 */
	public String guardar(){
		try {
			if(npfAsistenciaDocente!=null){
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setClfAsistenciaDocente1(npfAsistenciaDocente);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
//					if(item.getClfNota1()!=null||item.getClfAsistenciaEstudiante1()!=null){
//						RecordEstudianteDto rcesAux = new RecordEstudianteDto();
//						rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
//						servNpfCalificacionServicio.guardarNotasPregradoPrimerHemi(rcesAux, item , npfRegCliente);
//						npfValidadorClic=0;
//					}
					if(item.getClfNota1()==null ^ item.getClfAsistenciaEstudiante1()==null){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("EL registro por estudiante debe tener la nota y asistencia de la asignatura.");
						npfValidadorClic=0;
						return null;
					}
					
					try {
						servNpfCalificacionServicio.guardarEdicionNotasPrimerHemi(rcesAux, item, npfRegCliente);
					} catch (CalificacionValidacionException e) {
					} catch (CalificacionException e) {
					} catch (SQLIntegrityConstraintViolationException  | ConstraintViolationException | PersistenceException e) {
						npfValidadorClic=0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Ocurrió un error al guardar las notas, por favor revise los listados de notas.");
						return null;
					}
					npfValidadorClic=0;
					
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Ingreso de notas exitoso");
				
				
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
					
					JasperReport jasperReport = null;
					JasperPrint jasperPrint;
					StringBuilder pathGeneralReportes = new StringBuilder();
					pathGeneralReportes.append(FacesContext.getCurrentInstance()
							.getExternalContext().getRealPath("/"));
					pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/primerHemi");
					jasperReport = (JasperReport) JRLoader.loadObject(new File(
							(pathGeneralReportes.toString() + "/reporteNota1Hemi.jasper")));
					List<Map<String, Object>> frmAdjuntoCampos = null;
					Map<String, Object> frmAdjuntoParametros = null;
//					String facultadMail = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
//					String carreraMail = GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
					
					
					
					frmAdjuntoParametros = new HashMap<String, Object>();
					SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
					String fecha = formato.format(new Date());
					frmAdjuntoParametros.put("fecha",fecha);
					frmAdjuntoParametros.put("docente", npfDocente.getPrsNombres()+" "+npfDocente.getPrsPrimerApellido()+" "+npfDocente.getPrsSegundoApellido());
					frmAdjuntoParametros.put("nick", npfUsuario.getUsrNick());
					StringBuilder sbAsistenciaDocente = new StringBuilder();
					for (EstudianteJdbcDto item : npfListEstudianteBusq) {
						npfNomCarrera =  item.getCrrDescripcion().toString();
						npfNomMateria = item.getMtrDescripcion().toString();
						npfNomParalelo = item.getPrlDescripcion().toString();;
						frmAdjuntoParametros.put("periodo", item.getPracDescripcion());
						frmAdjuntoParametros.put("facultad", item.getDpnDescripcion());
						frmAdjuntoParametros.put("carrera", item.getCrrDetalle());
						frmAdjuntoParametros.put("curso", item.getNvlDescripcion());
						frmAdjuntoParametros.put("paralelo", item.getPrlDescripcion());
						frmAdjuntoParametros.put("materia", item.getMtrDescripcion());
						sbAsistenciaDocente.append(item.getClfAsistenciaDocente1());
						frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());	
						break;
					}
					frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
					frmAdjuntoParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
					
					frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
					Map<String, Object> datoEstudiantes = null;
					
					int cont = 1;
					for (EstudianteJdbcDto item : npfListEstudianteBusq) {
						StringBuilder sbNota1 = new StringBuilder();
						StringBuilder sbAsistencia1 = new StringBuilder();
						StringBuilder sbContador = new StringBuilder();
						datoEstudiantes = new HashMap<String, Object>();
						datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
						datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
						datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
						datoEstudiantes.put("nombres", item.getPrsNombres());
						if(item.getClfNota1() != null){
							sbNota1.append(item.getClfNota1());
							datoEstudiantes.put("nota1", sbNota1.toString());
						}else{
							sbNota1.append("-");
							datoEstudiantes.put("nota1", sbNota1.toString());
						}
						if(item.getClfAsistenciaEstudiante1() != null){
							sbAsistencia1.append(item.getClfAsistenciaEstudiante1());
							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
						}else{
							sbAsistencia1.append("-");
							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
						}
						sbContador.append(cont);
						datoEstudiantes.put("numero", sbContador.toString());
						
						frmAdjuntoCampos.add(datoEstudiantes);
						cont = cont +1;
					}
					
					JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);
					
					
					jasperPrint = JasperFillManager.fillReport(jasperReport,
						frmAdjuntoParametros, dataSource);
					
					
					byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
	//				AdjuntoDto adjuntoDto = new AdjuntoDto();
	//				adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
	//				adjuntoDto.setAdjunto(arreglo);
					
					//lista de correos a los que se enviara el mail
					List<String> correosTo = new ArrayList<>();
					correosTo.add(npfDocente.getPrsMailInstitucional());
					//path de la plantilla del mail
					ProductorMailJson pmail = null;
					StringBuilder sbCorreo= new StringBuilder();
					sbCorreo= GeneralesUtilidades.generarAsunto(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
							npfDocente.getPrsPrimerApellido()+" "+npfDocente.getPrsSegundoApellido()+" "+npfDocente.getPrsNombres() , npfNomCarrera , npfNomMateria , npfNomParalelo);
					pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_REGISTRO_NOTAS,
							sbCorreo.toString()
							  , "admin", "dt1c201s", true, arreglo, "RegistroNotas."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
					String jsonSt = pmail.generarMail();
					Gson json = new Gson();
					MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
				// 	Iniciamos el envío de mensajes
					ObjectMessage message = session.createObjectMessage(mailDto);
					producer.send(message);
			}  catch (JMSException e) {
			} catch (JRException e) {
			} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
			}
				
				//******************************************************************************
				//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
				//******************************************************************************
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor, ingrese las horas de clase del docente en la asignatura");
				return null;
			}
		} catch (RecordEstudianteDtoException e) {
			 FacesUtil.mensajeError(e.getMessage());
			 
		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		}
		return "irListarParalelos";
	}	
	
	/**
	 * Dirige la navegacion hacia la pagina de edicion de notas 
	 * @param prl .- paralelo seleccionado
	 * @return Xhtml editar
	 */
	public String irRecuperacionNotas(ParaleloDto prl){
		npfActivadorGuardar=false;
		npfParaleloDtoEditar = new ParaleloDto(); 
		npfParaleloDtoEditar = prl;
		npfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		npfListParaleloBusqIndividual = new ArrayList<ParaleloDto>();
		npfListParaleloBusqIndividual.add(npfParaleloDtoEditar);
		try {
			npfDependenciaBuscar = new Dependencia();
			npfDependenciaBuscar =  servNpfDependenciaServicio.buscarFacultadXcrrId(npfParaleloDtoEditar.getCrrId());
//			if(npfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
//				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
//			}else{
//				npfCronogramaActividadJdbcDtoBuscar = servNpfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
//			}
//			Timestamp myDate = new Timestamp(new Date().getTime());
//			if(myDate.after(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
//				return null;
//			}
//			if(myDate.before(npfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
//				return null;
//			}
		
			List<String> datosCliente = new ArrayList<>();
			datosCliente = IdentifidorCliente.obtenerDatosCliente();
			Date fechaActual = new Date();
			npfRegCliente = "Fecha de registro: " + fechaActual.toString().concat("|ACCION: REGISTRO NOTAS DOCENTE");
			StringBuilder sb = new StringBuilder();
			sb.append(npfRegCliente);
			sb.append("|");
			for (String item : datosCliente) {
				try {
					sb.append(item);
					sb.append("|");	
				} catch (Exception e) {
				}
			}
			npfRegCliente = sb.toString();
			
			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente1() != null)){
//					if(item.getClfAsistenciaDocente1()!=0){
						
//					}else{
						
//					}
						npfAsistenciaDocente = null;
//						break;
					
				}else{
					npfAsistenciaDocente2 = item.getClfAsistenciaDocente2();
					break;
				}
				
			}
//			for (EstudianteJdbcDto item : npfListEstudianteBusq) {
//				if(item.getRcesIngersoNota()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("No puede registrar notas y asistencia ya que realizo un guardado final");
//					return null;
//				}
//				break;
//				
//			}
//		} catch (EstudianteDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//			 
//		} catch (EstudianteDtoJdbcNoEncontradoException e) {
//			npfActivadorGuardar=true;
//			FacesUtil.mensajeError(e.getMessage());
//			 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
//		} catch (DependenciaException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (CronogramaActividadDtoJdbcException e) {
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
//			FacesUtil.mensajeError(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista de paralelos
	 * @return XHTML listar paralelos
	 */
	public String guardarPosgrado(){
		try {
			if(npfAsistenciaDocente!=null){
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setClfAsistenciaDocente1(npfAsistenciaDocente);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getRcesId());
//					if(item.getClfNota1()!=null||item.getClfAsistenciaEstudiante1()!=null){
//						RecordEstudianteDto rcesAux = new RecordEstudianteDto();
//						rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
//						servNpfCalificacionServicio.guardarNotasPregradoPrimerHemi(rcesAux, item , npfRegCliente);
//						npfValidadorClic=0;
//					}
					if(item.getClfNota1()==null ^ item.getClfAsistenciaEstudiante1()==null){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("EL registro por estudiante debe tener la nota y asistencia respectivamente.");
						npfValidadorClic=0;
						return null;
					}else if(item.getClfNota1()!=null && item.getClfAsistenciaEstudiante1()!=null){
						try {
							//calculo la suma de na nota1 + nota2 con redondeo de una cifra decimal
							BigDecimal sumaParciales = BigDecimal.ZERO;
							sumaParciales = item.getClfNota1();
							item.setClfSumaP1P2(sumaParciales);
							
							//calculo de la suma de asistencia del estudiante de los dos parciales
							int sumaAsistenciaParciales= 0;
							sumaAsistenciaParciales = item.getClfAsistenciaEstudiante1();
							item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));
							
							//calculo de la suma de la asistencia del docente de los dos parciales
							int sumaAsistenciaDoc = 0;
							sumaAsistenciaDoc = item.getClfAsistenciaDocente1() ;
							item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));
							
							//calcula el promedio de la asistencia del estudiante
							item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, item.getClfAsistenciaTotal().intValue(), item.getClfAsistenciaTotalDoc().intValue()));
							CarreraDto aux = servNpfCarreraDtoServicioJdbc.buscarXId(npfCrrId);
							int com = 0;
							if(aux.getCrrTipoEvaluacion()==CarreraConstantes.CRR_TIPO_EVALUACION_SOBRE_20_VALUE){
								com = item.getClfSumaP1P2().compareTo(new BigDecimal(16));
							}else{
								com = item.getClfSumaP1P2().compareTo(new BigDecimal(7));
							}
							
							//si la suma de los parciales es mayor o igual a 7
							if(com == 1 || com == 0){
								int promedioAsistencia = 0;
								promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
									// si el promedio de asistencia es mayor o igual a 80
								if(promedioAsistencia == 1 || promedioAsistencia == 0){
									//calcula la nota final del semestre y el estado a aprobado
									item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
								}else{// si el promedio de asistencia es menor a 80
									item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.DOWN));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
							}else{
									int promedioAsistencia = 0;
									promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
										if(promedioAsistencia == 1 || promedioAsistencia == 0){
//											BigDecimal itemCost  = BigDecimal.ZERO;
//											itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
//											item.setClfParamRecuperacion1(itemCost);
											item.setClfNotalFinalSemestre(sumaParciales.setScale(1, RoundingMode.HALF_UP));
											item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
										}else{
//											BigDecimal itemCost  = BigDecimal.ZERO;
//											itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
//											item.setClfParamRecuperacion1(itemCost);
											item.setClfNotalFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP));
											item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
										}
							}
						} catch (Exception e) {
						}
						try {
							servNpfCalificacionServicio.guardarNotasPosgrado(rcesAux, item, npfRegCliente);
							List<RecordEstudiante> listaRecord = new ArrayList<RecordEstudiante>();
							listaRecord=servNpfRecordEstudianteServicio.buscarEstadoMateriasActualesPosgrado(item.getPrsIdentificacion(), item.getNvlId());
							boolean op=true;
							Integer conteoReprobadas = 0;
							for (RecordEstudiante itemRces : listaRecord) {
								if(itemRces.getRcesEstado()==RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE){
									op=false;
								}else if(itemRces.getRcesEstado()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
									conteoReprobadas++;
								}
							}
							if(op){
								if(conteoReprobadas>2){
									servNpfFichaInscripcionServicio.desactivarFichaInscripcionPosgradoXFcesId(item.getFcesId());
								}else{
									servNpfRecordEstudianteServicio.registrarNuevoNivelPosgrado(item.getFcesId(), item.getNvlId(), item.getPracId());	
								}
							}
						} catch (Exception e) {
						}	
					}
					npfValidadorClic=0;
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Ingreso de notas exitoso");
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
					
					JasperReport jasperReport = null;
					JasperPrint jasperPrint;
					StringBuilder pathGeneralReportes = new StringBuilder();
					pathGeneralReportes.append(FacesContext.getCurrentInstance()
							.getExternalContext().getRealPath("/"));
					pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/notasPosgrado");
					jasperReport = (JasperReport) JRLoader.loadObject(new File(
							(pathGeneralReportes.toString() + "/reporteNotaPosgrado.jasper")));
					List<Map<String, Object>> frmAdjuntoCampos = null;
					Map<String, Object> frmAdjuntoParametros = null;
//					String facultadMail = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
//					String carreraMail = GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
					
					
					
					frmAdjuntoParametros = new HashMap<String, Object>();
					SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
					String fecha = formato.format(new Date());
					frmAdjuntoParametros.put("fecha",fecha);
					frmAdjuntoParametros.put("docente", npfDocente.getPrsNombres()+" "+npfDocente.getPrsPrimerApellido()+" "+npfDocente.getPrsSegundoApellido());
					frmAdjuntoParametros.put("nick", npfUsuario.getUsrNick());
					StringBuilder sbAsistenciaDocente = new StringBuilder();
					for (EstudianteJdbcDto item : npfListEstudianteBusq) {
						npfNomCarrera =  item.getCrrDescripcion().toString();
						npfNomMateria = item.getMtrDescripcion().toString();
						npfNomParalelo = item.getPrlDescripcion().toString();;
						frmAdjuntoParametros.put("periodo", item.getPracDescripcion());
						frmAdjuntoParametros.put("facultad", item.getDpnDescripcion());
						frmAdjuntoParametros.put("carrera", item.getCrrDetalle());
						frmAdjuntoParametros.put("curso", item.getNvlDescripcion());
						frmAdjuntoParametros.put("paralelo", item.getPrlDescripcion());
						frmAdjuntoParametros.put("materia", item.getMtrDescripcion());
						sbAsistenciaDocente.append(item.getClfAsistenciaDocente2());
						frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());	
						break;
					}
					frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
					frmAdjuntoParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
					
					frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
					Map<String, Object> datoEstudiantes = null;
					
					int cont = 1;
					for (EstudianteJdbcDto item : npfListEstudianteBusq) {
						StringBuilder sbNota1 = new StringBuilder();
						StringBuilder sbAsistencia1 = new StringBuilder();
						StringBuilder sbContador = new StringBuilder();
						datoEstudiantes = new HashMap<String, Object>();
						datoEstudiantes.put("identificacion", item.getPrsIdentificacion());
						datoEstudiantes.put("apellido_paterno", item.getPrsPrimerApellido());
						datoEstudiantes.put("apellido_materno", item.getPrsSegundoApellido());
						datoEstudiantes.put("nombres", item.getPrsNombres());
						if(item.getClfNota1() != null){
							sbNota1.append(item.getClfNota1());
							datoEstudiantes.put("nota1", sbNota1.toString());
						}else{
							sbNota1.append("-");
							datoEstudiantes.put("nota1", sbNota1.toString());
						}
						if(item.getClfAsistenciaEstudiante1() != null){
							sbAsistencia1.append(item.getClfAsistenciaEstudiante1());
							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
						}else{
							sbAsistencia1.append("-");
							datoEstudiantes.put("asistencia1", sbAsistencia1.toString());
						}
						sbContador.append(cont);
						datoEstudiantes.put("numero", sbContador.toString());
						
						frmAdjuntoCampos.add(datoEstudiantes);
						cont = cont +1;
					}
					
					JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);
					
					
					jasperPrint = JasperFillManager.fillReport(jasperReport,
						frmAdjuntoParametros, dataSource);
					
					
					byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
	//				AdjuntoDto adjuntoDto = new AdjuntoDto();
	//				adjuntoDto.setNombreAdjunto("ComprobanteRolPago."+MailDtoConstantes.TIPO_PDF);
	//				adjuntoDto.setAdjunto(arreglo);
					
					//lista de correos a los que se enviara el mail
					List<String> correosTo = new ArrayList<>();
					correosTo.add(npfDocente.getPrsMailInstitucional());
					//path de la plantilla del mail
					ProductorMailJson pmail = null;
					StringBuilder sbCorreo= new StringBuilder();
					sbCorreo= GeneralesUtilidades.generarAsunto(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
							npfDocente.getPrsPrimerApellido()+" "+npfDocente.getPrsSegundoApellido()+" "+npfDocente.getPrsNombres() , npfNomCarrera , npfNomMateria , npfNomParalelo);
					pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_REGISTRO_NOTAS,
							sbCorreo.toString()
							  , "admin", "dt1c201s", true, arreglo, "RegistroNotas."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
					String jsonSt = pmail.generarMail();
					Gson json = new Gson();
					MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
				// 	Iniciamos el envío de mensajes
					ObjectMessage message = session.createObjectMessage(mailDto);
					producer.send(message);
			}  catch (JMSException e) {
			} catch (JRException e) {
			} catch (ec.edu.uce.mailDto.excepciones.ValidacionMailException e) {
			}
				
				//******************************************************************************
				//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
				//******************************************************************************
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor, ingrese las horas de clase del docente en la asignatura");
				npfValidadorClic=0;
				return null;
			}
		} catch (RecordEstudianteDtoException e) {
			 FacesUtil.mensajeError(e.getMessage());
			 
		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		}
		return "irListarParalelos";
	}	
	
	public boolean activacion(){
		boolean retorno = false;
		
		for (EstudianteJdbcDto item : npfListEstudianteBusq) {
			int aux = item.getRcesIngersoNota2();
			if(aux == 2){
				retorno = true;
				break;
			}
		}
		return retorno;
	}
		
	/**
	 * Meétodo que guarde de forma temporal los registros de la lista de estudiantes
	 * @return XHTML listar paralelos
	 */
	public String guardoTemporal(){
		try {
			if(npfAsistenciaDocente!=null){
				for (EstudianteJdbcDto item : npfListEstudianteBusq) {
					item.setClfAsistenciaDocente1(npfAsistenciaDocente);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
//					if(item.getClfNota1()!=null||item.getClfAsistenciaEstudiante1()!=null){
//						RecordEstudianteDto rcesAux = new RecordEstudianteDto();
//						rcesAux = servNpfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
//						servNpfCalificacionServicio.guardoTemporalNotasPregradoPrimerHemi(rcesAux, item , npfRegCliente);
//						npfValidadorClic=0;
//					}
					if(item.getClfNota1()==null ^ item.getClfAsistenciaEstudiante1()==null){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("EL registro por estudiante debe tener la nota y asistencia de la asignatura");
						npfValidadorClic=0;
						return null;
					}
//					if (item.getClfNota1()!=null && item.getClfAsistenciaEstudiante1()!=null){
						
						servNpfCalificacionServicio.guardoTemporalNotasPregradoPrimerHemi(rcesAux, item, npfRegCliente);
						npfValidadorClic=0;
//					}
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Guardado temporal de notas exitoso");
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Debe ingresar la asistencia del docente");
				npfValidadorClic=0;
			}
		} catch (RecordEstudianteDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CalificacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CalificacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irListarParalelos";
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	
	
	public Boolean validador(BigDecimal valor)  {
		Boolean retorno = false;
		String valorSt = GeneralesUtilidades.eliminarEspaciosEnBlanco(valor.toString());
		valorSt=valorSt.replace(",", ".");
		try {
		     Float.parseFloat(valorSt);
		     int puntoDecimalUbc = valorSt.indexOf('.');
		     if(puntoDecimalUbc==0){
		    	FacesUtil.limpiarMensaje();
		    	FacesUtil.mensajeError("No se permiten números sin parte entera");
				return false;
		     }
		     int totalDecimales = valorSt.length() - puntoDecimalUbc - 1;
		     if(puntoDecimalUbc==-1){
		    	 if(Float.parseFloat(valorSt)>20){
		    		 FacesUtil.limpiarMensaje();
		    		 FacesUtil.mensajeError("Calificación máximo sobre 10 puntos");
		    		 return false;
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("No se permiten números negativos");
					 return false;
				 } 
		     }else{
		    	 if(totalDecimales>2){
		    		 FacesUtil.limpiarMensaje();
		    		 FacesUtil.mensajeError("Sólo permite máximo 2 número decimales");
		    		 return false;
				 }else if(Float.parseFloat(valorSt)>20){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("Calificación máximo sobre 10 puntos");
					 return false;
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("No se permiten números negativos");
					 return false;
				 } 
		     }
		}
		catch (NumberFormatException ex) {
			ex.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Sólo números y signos decimales");
			 return false;
		}
		retorno  = true;
		return retorno;
	}
	
	
	public Boolean validadorAsistencia(String asistenciaEst,Integer asistenciaDocente)  {
		Boolean retorno = false;
		try{
			int valor =  Integer.parseInt(asistenciaEst);
			if(valor>asistenciaDocente){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("La asitencia no puede ser mayor a la asistencia del docente");
				return false;
			}
		}catch(Exception e){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Por favor, ingrese las horas de clase del docente en la asignatura");
			return false;
		}
		retorno = true;
		return retorno;
		
	}
	
	
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		npfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO ID
		npfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		npfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL ID
		npfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO ID
		npfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA MATERIA ID
		npfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		//ANULO LA LISTA DE NIVEL
		npfListNivelBusq = null;
		//ANULO LA LISTA DE PARALELOS
		npfListParaleloBusq = null;
		//ANULO LA LISTA DE MATERIAS
		npfListMateriaBusq = null;
		npfActivadorGuardar = false;
		npfContadorEstudiante = 0;
	}
	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada(){
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getNpfUsuario() {
		return npfUsuario;
	}
	public void setNpfUsuario(Usuario npfUsuario) {
		this.npfUsuario = npfUsuario;
	}
	public DocenteJdbcDto getNpfDocente() {
		return npfDocente;
	}
	public void setNpfDocente(DocenteJdbcDto npfDocente) {
		this.npfDocente = npfDocente;
	}
	public EstudianteJdbcDto getNpfEstudianteBuscar() {
		return npfEstudianteBuscar;
	}
	public void setNpfEstudianteBuscar(EstudianteJdbcDto npfEstudianteBuscar) {
		this.npfEstudianteBuscar = npfEstudianteBuscar;
	}
	public List<CarreraDto> getNpfListCarreraBusq() {
		npfListCarreraBusq = npfListCarreraBusq==null?(new ArrayList<CarreraDto>()):npfListCarreraBusq;
		return npfListCarreraBusq;
	}
	public void setNpfListCarreraBusq(List<CarreraDto> npfListCarreraBusq) {
		this.npfListCarreraBusq = npfListCarreraBusq;
	}
	public List<NivelDto> getNpfListNivelBusq() {
		npfListNivelBusq = npfListNivelBusq==null?(new ArrayList<NivelDto>()):npfListNivelBusq;
		return npfListNivelBusq;
	}
	public void setNpfListNivelBusq(List<NivelDto> npfListNivelBusq) {
		this.npfListNivelBusq = npfListNivelBusq;
	}
	public List<MateriaDto> getNpfListMateriaBusq() {
		npfListMateriaBusq = npfListMateriaBusq==null?(new ArrayList<MateriaDto>()):npfListMateriaBusq;
		return npfListMateriaBusq;
	}
	public void setNpfListMateriaBusq(List<MateriaDto> npfListMateriaBusq) {
		this.npfListMateriaBusq = npfListMateriaBusq;
	}
	public List<EstudianteJdbcDto> getNpfListEstudianteBusq() {
		npfListEstudianteBusq = npfListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):npfListEstudianteBusq;
		return npfListEstudianteBusq;
	}
	public void setNpfListEstudianteBusq(List<EstudianteJdbcDto> npfListEstudianteBusq) {
		this.npfListEstudianteBusq = npfListEstudianteBusq;
	}
	public List<ParaleloDto> getNpfListParaleloBusq() {
		npfListParaleloBusq = npfListParaleloBusq==null?(new ArrayList<ParaleloDto>()):npfListParaleloBusq;
		return npfListParaleloBusq;
	}
	public void setNpfListParaleloBusq(List<ParaleloDto> npfListParaleloBusq) {
		this.npfListParaleloBusq = npfListParaleloBusq;
	}

	public ParaleloDto getNpfParaleloDtoEditar() {
		return npfParaleloDtoEditar;
	}

	public void setNpfParaleloDtoEditar(ParaleloDto npfParaleloDtoEditar) {
		this.npfParaleloDtoEditar = npfParaleloDtoEditar;
	}

	public Integer getNpfAsistenciaDocente() {
		return npfAsistenciaDocente;
	}


	public void setNpfAsistenciaDocente(Integer npfAsistenciaDocente) {
		this.npfAsistenciaDocente = npfAsistenciaDocente;
	}


	public Integer getNpfValidadorClic() {
		return npfValidadorClic;
	}


	public void setNpfValidadorClic(Integer npfValidadorClic) {
		this.npfValidadorClic = npfValidadorClic;
	}

//	private void setIpAdd() {
//        try {
//            InetAddress thisIp = InetAddress.getLocalHost();
//            thisIpAddress = thisIp.getHostAddress().toString();
//            
//            thisIp = InetAddress.getLocalHost();
//
//    		NetworkInterface network = NetworkInterface.getByInetAddress(thisIp);
//
//    		byte[] mac = network.getHardwareAddress();
//
//    		System.out.print("Current MAC address : ");
//
//    		StringBuilder sb = new StringBuilder();
//    		for (int i = 0; i < mac.length; i++) {
//    			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//    		}
//        } catch (Exception e) {
//        }
//    }

//    protected String getIpAddress() {
//        setIpAdd();
//        return thisIpAddress;
//    }


	public String getNpfRegCliente() {
		return npfRegCliente;
	}
	public void setNpfRegCliente(String npfRegCliente) {
		this.npfRegCliente = npfRegCliente;
	}


	public String getNpfEstado() {
		return npfEstado;
	}
	public void setNpfEstado(String npfEstado) {
		this.npfEstado = npfEstado;
	}

	public String getNpfNomCarrera() {
		return npfNomCarrera;
	}


	public void setNpfNomCarrera(String npfNomCarrera) {
		this.npfNomCarrera = npfNomCarrera;
	}


	public String getNpfNomMateria() {
		return npfNomMateria;
	}


	public void setNpfNomMateria(String npfNomMateria) {
		this.npfNomMateria = npfNomMateria;
	}


	public String getNpfNomParalelo() {
		return npfNomParalelo;
	}


	public void setNpfNomParalelo(String npfNomParalelo) {
		this.npfNomParalelo = npfNomParalelo;
	}

	public List<RolFlujoCarrera> getNpfListRolFlujoCarreraBusq() {
		npfListRolFlujoCarreraBusq = npfListRolFlujoCarreraBusq==null?(new ArrayList<RolFlujoCarrera>()):npfListRolFlujoCarreraBusq;
		return npfListRolFlujoCarreraBusq;
	}


	public void setNpfListRolFlujoCarreraBusq(List<RolFlujoCarrera> npfListRolFlujoCarreraBusq) {
		this.npfListRolFlujoCarreraBusq = npfListRolFlujoCarreraBusq;
	}


	public CronogramaActividadJdbcDto getNpfCronogramaActividadJdbcDtoBuscar() {
		return npfCronogramaActividadJdbcDtoBuscar;
	}


	public void setNpfCronogramaActividadJdbcDtoBuscar(CronogramaActividadJdbcDto npfCronogramaActividadJdbcDtoBuscar) {
		this.npfCronogramaActividadJdbcDtoBuscar = npfCronogramaActividadJdbcDtoBuscar;
	}


	public Dependencia getNpfDependenciaBuscar() {
		return npfDependenciaBuscar;
	}


	public void setNpfDependenciaBuscar(Dependencia npfDependenciaBuscar) {
		this.npfDependenciaBuscar = npfDependenciaBuscar;
	}


	public List<DocenteJdbcDto> getNpffListCarreraDocenteBusq() {
		npffListCarreraDocenteBusq = npffListCarreraDocenteBusq==null?(new ArrayList<DocenteJdbcDto>()):npffListCarreraDocenteBusq;
		return npffListCarreraDocenteBusq;
	}


	public void setNpffListCarreraDocenteBusq(List<DocenteJdbcDto> npffListCarreraDocenteBusq) {
		this.npffListCarreraDocenteBusq = npffListCarreraDocenteBusq;
	}


	public List<ParaleloDto> getNpfListParaleloBusqIndividual() {
		return npfListParaleloBusqIndividual;
	}


	public void setNpfListParaleloBusqIndividual(List<ParaleloDto> npfListParaleloBusqIndividual) {
		this.npfListParaleloBusqIndividual = npfListParaleloBusqIndividual;
	}


	public Integer getNpfCrrId() {
		return npfCrrId;
	}


	public void setNpfCrrId(Integer npfCrrId) {
		this.npfCrrId = npfCrrId;
	}


	public List<DocenteJdbcDto> getNpffListNivel() {
		return npffListNivel;
	}


	public void setNpffListNivel(List<DocenteJdbcDto> npffListNivel) {
		this.npffListNivel = npffListNivel;
	}


	public Integer getNpfAsistenciaDocente2() {
		return npfAsistenciaDocente2;
	}


	public void setNpfAsistenciaDocente2(Integer npfAsistenciaDocente2) {
		this.npfAsistenciaDocente2 = npfAsistenciaDocente2;
	}


	public boolean isNpfActivadorGuardar() {
		return npfActivadorGuardar;
	}


	public void setNpfActivadorGuardar(boolean npfActivadorGuardar) {
		this.npfActivadorGuardar = npfActivadorGuardar;
	}


	public CarreraDto getNpfCarrera() {
		return npfCarrera;
	}


	public void setNpfCarrera(CarreraDto npfCarrera) {
		this.npfCarrera = npfCarrera;
	}
	
	
	
	
}
