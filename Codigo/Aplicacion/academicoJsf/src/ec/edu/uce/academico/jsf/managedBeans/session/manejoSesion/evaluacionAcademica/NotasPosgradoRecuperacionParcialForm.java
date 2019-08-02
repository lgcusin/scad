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
   
 ARCHIVO:     NotasPosgradoRecuperacionParcialForm.java	  
 DESCRIPCION: Bean de sesion que maneja las notas del recuperacion por de posgradodocentes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 18/03/2018 			Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPregradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoPuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteNotasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.academico.jsf.utilidades.IdentifidorCliente;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;

import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import com.google.gson.Gson;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.io.File;

/**
 * Clase (managed bean) NotasPosgradoRecuperacionParcialForm.
 * Managed Bean que administra los estudiantes para el ingreso de las notas del recuperacion de posgrado por docentes.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="notasPosgradoRecuperacionForm")
@SessionScoped
public class NotasPosgradoRecuperacionParcialForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario nprfUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto nprfDocente;

	//PARA BUSQUEDA
	private EstudianteJdbcDto nprfEstudianteBuscar;
	private List<CarreraDto> nprfListCarreraBusq;
	private List<NivelDto> nprfListNivelBusq;
	private List<MateriaDto> nprfListMateriaBusq;
	private List<EstudianteJdbcDto> nprfListEstudianteBusq;
	private List<ParaleloDto> nprfListParaleloBusq;
	private ParaleloDto nprfParaleloDtoEditar;
	
	private List<RolFlujoCarrera> nprfListRolFlujoCarreraBusq;
	
	private Dependencia nprfDependenciaBuscar;
	private CronogramaActividadJdbcDto nprfCronogramaActividadJdbcDtoBuscar;
	
	//PARA GUARDAR LA ASISTENCIA DEL DOCENTE DEL FORM
	private Integer nprfAsistenciaDocente;
	//PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private Integer nprfValidadorClic;
	private String nprfEstado;
	//PARA GUARDAR LOS REGISTROS DE LA SESION DEL CLIENTE HOSTNAME, IPPIBLICA, IPPRIVADA
	private String nprfRegCliente;
	//private String thisIpAddress;
	//campos para el envio de la notificacion del ingreso final de notas al mail del docente	
	private String nprfNomCarrera;
	private String nprfNomMateria;
	private String nprfNomParalelo;

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
	private CarreraDtoServicioJdbc servNprfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servNprfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servNprfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servNprfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servNprfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servNprfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servNprfParaleloDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servNprfNotasPregradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servNprfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNprfMateriaDto;
	@EJB
	private CalificacionServicio servNprfCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servNprfRolFlujoCarreraServicio;
	
	@EJB
	private UsuarioRolServicio servNprfUsuarioRolServicio;
	
	@EJB
	private DependenciaServicio servNprfDependenciaServicio;
	
	@EJB
	private CronogramaActividadDtoServicioJdbc	servNprfCronogramaActividadDtoServicioJdbcServicio;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irAnotaPregradoRecuperacion(Usuario usuario){
		nprfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			UsuarioRol usro = servNprfUsuarioRolServicio.buscarXUsuarioXrol(nprfUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			nprfListRolFlujoCarreraBusq = servNprfRolFlujoCarreraServicio.buscarPorIdUsuario(nprfUsuario);
			iniciarParametros();
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			nprfListCarreraBusq = servNprfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivo(nprfUsuario.getUsrId(), RolConstantes.ROL_BD_DOCENTE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, nprfListRolFlujoCarreraBusq);
			retorno = "irAnotaPregradoRecuperacion";
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
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
		idCarrera = nprfEstudianteBuscar.getCrrId();
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				nprfDocente = servNprfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodoActivo(nprfUsuario.getUsrId(), idCarrera, TipoPuestoConstantes.TIPO_DOCENTE_VALUE);
				//LISTO LOS NIVELES
				nprfListNivelBusq = servNprfNivelDtoServicioJdbc.listarNivelXPeriodoActivoXCarrera(nprfEstudianteBuscar.getCrrId(), nprfDocente.getDtpsId());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.exception")));
			}
		} catch (NivelDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
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
		idNivel = nprfEstudianteBuscar.getNvlId();
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				nprfListMateriaBusq = servNprfMateriaDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXparaleloXdocente( nprfEstudianteBuscar.getCrrId(), idNivel , nprfDocente.getDtpsId());
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
		nprfListParaleloBusq = null;
		try {
			if(nprfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			}else if(nprfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else if(nprfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			}else{
				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
				nprfListParaleloBusq = servNprfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocente(nprfEstudianteBuscar.getCrrId(), nprfEstudianteBuscar.getNvlId(), nprfDocente.getDtpsId(), nprfEstudianteBuscar.getMtrId());
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
	public void reporteNotas(){
		
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
		nprfParaleloDtoEditar = new ParaleloDto(); 
		nprfParaleloDtoEditar = prl;
		nprfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			
			nprfDependenciaBuscar = new Dependencia();
			nprfDependenciaBuscar =  servNprfDependenciaServicio.buscarPorId(nprfParaleloDtoEditar.getCrrId());
			if(nprfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
				nprfCronogramaActividadJdbcDtoBuscar = servNprfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
			}else{
				nprfCronogramaActividadJdbcDtoBuscar = servNprfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
			}
			Timestamp myDate = new Timestamp(new Date().getTime());
			if(myDate.after(nprfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			if(myDate.before(nprfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			nprfRegCliente  = idHostAux.concat(" "+ipLocalClienteAux.concat(" " + ipPublicaClienteAux));
			Date fechaActual = new Date();
			nprfRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 1 ");
			nprfListEstudianteBusq = servNprfNotasPregradoDtoServicioJdbc.buscarEstudianteRecuperacionXperiodoXcarreraXnivelXparaleloXmateriaXdocente(nprfParaleloDtoEditar.getCrrId(), nprfParaleloDtoEditar.getMlcrmtNvlId(), nprfParaleloDtoEditar.getPrlId(), nprfParaleloDtoEditar.getMtrId(),nprfDocente.getFcdcId(),nprfParaleloDtoEditar.getMlcrprId());
			for (EstudianteJdbcDto item : nprfListEstudianteBusq) {
				if(item.getRcesIngersoNota3()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No puede registrar notas y asistencia ya que realizo un guardado final");
					return null;
				}
				break;
				
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
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
	public String irRecuperacionNotas(ParaleloDto prl){
		nprfParaleloDtoEditar = new ParaleloDto(); 
		nprfParaleloDtoEditar = prl;
		nprfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			
			nprfDependenciaBuscar = new Dependencia();
			nprfDependenciaBuscar =  servNprfDependenciaServicio.buscarPorId(nprfEstudianteBuscar.getCrrId());
			if(nprfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
				nprfCronogramaActividadJdbcDtoBuscar = servNprfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
			}else{
				nprfCronogramaActividadJdbcDtoBuscar = servNprfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_RECUPERACION_VALUE);
			}
			Timestamp myDate = new Timestamp(new Date().getTime());
			
			if(myDate.after(nprfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			
			if(myDate.before(nprfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
				return null;
			}
			
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			nprfRegCliente  = idHostAux.concat(" "+ipLocalClienteAux.concat(" " + ipPublicaClienteAux));
			Date fechaActual = new Date();
			nprfRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 1 ");
			nprfListEstudianteBusq = servNprfNotasPregradoDtoServicioJdbc.buscarEstudianteRecuperacionXperiodoXcarreraXnivelXparaleloXmateriaXdocente(nprfParaleloDtoEditar.getCrrId(), nprfParaleloDtoEditar.getMlcrmtNvlId(), nprfParaleloDtoEditar.getPrlId(), nprfParaleloDtoEditar.getMtrId(),nprfDocente.getFcdcId(),nprfParaleloDtoEditar.getMlcrprId());
			for (EstudianteJdbcDto item : nprfListEstudianteBusq) {
				if(item.getRcesIngersoNota3()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No puede registrar notas y asistencia ya que realizo un guardado final");
					return null;
				}
				break;
				
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CronogramaActividadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irRecuperacionPregrado";
	}
	
	/*
	 * Metodo que permite generar el reporte
	 */
	public void generarReporte(ParaleloDto prl){
		
		nprfParaleloDtoEditar = new ParaleloDto(); 
		nprfParaleloDtoEditar = prl;
		nprfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			nprfListEstudianteBusq = servNprfNotasPregradoDtoServicioJdbc.buscarEstudianteRecuperacionReporte(nprfParaleloDtoEditar.getCrrId(), nprfParaleloDtoEditar.getMlcrmtNvlId(), nprfParaleloDtoEditar.getPrlId(), nprfParaleloDtoEditar.getMtrId(),nprfDocente.getFcdcId(),nprfParaleloDtoEditar.getMlcrprId());
			
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
	}
	
	
	@SuppressWarnings("static-access")
	public String irVerNotas(ParaleloDto prl){
		ReporteNotasForm reporte = new ReporteNotasForm();
		nprfParaleloDtoEditar = new ParaleloDto(); 
		nprfParaleloDtoEditar = prl;
		nprfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			nprfListEstudianteBusq = servNprfNotasPregradoDtoServicioJdbc.buscarEstudianteRecuperacionReporte(nprfParaleloDtoEditar.getCrrId(), nprfParaleloDtoEditar.getMlcrmtNvlId(), nprfParaleloDtoEditar.getPrlId(), nprfParaleloDtoEditar.getMtrId(),nprfDocente.getFcdcId(),nprfParaleloDtoEditar.getMlcrprId());
			activacion();
			reporte.generarReporteNotasRecuperacion(nprfListEstudianteBusq, nprfUsuario);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas";
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por carrera nivel y materia 
	 * @return Xhtml listar
	 */
	public String irCancelar(){
		nprfParaleloDtoEditar = null;
		nprfListEstudianteBusq = null;
		nprfValidadorClic = new Integer(0);
		return "irCancelar";
	}
	
	/**
	 * Método calcula el porcentaje de asistencia
	 * @param porcentaje .- valor del porsentaje del calculo
	 * @param asitenciaEst.- valor de la asistencia de estudiante
	 * @param asitenciaDoc.- valor de la asistencia del docente
	 * @return.- valor del porsentaje en bigdecimal con dos decimales sin redondeo
	 */
	public BigDecimal calcularPorcentajeAsistencia(int porcentaje, int asitenciaEst, int asitenciaDoc) {
		 BigDecimal itemCost  = BigDecimal.ZERO;
	     itemCost  = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje)).divide(new BigDecimal(asitenciaDoc), 2, RoundingMode.DOWN);
	     return itemCost;
	}
	
//	/**
//	 * verifica que haga click en el boton guardar temporal la nota
//	 */
//	public String verificarClicGuardadoTemporal(){
//		if(nprfAsistenciaDocente!=null){
//			for (EstudianteJdbcDto item : nprfListEstudianteBusq) {
//				item.setClfAsistenciaDocente2(nprfAsistenciaDocente);
//				if(item.getClfNota2()!=null&&item.getClfAsistenciaEstudiante2()!=null){
//					String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
//					if(!validadorAsistencia(cadena, nprfAsistenciaDocente)){
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
//						nprfValidadorClic = 0;
//						return null;
//					}
//					if(!validador(item.getClfNota2())){
//						FacesUtil.limpiarMensaje();
//						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
//						nprfValidadorClic = 0;
//						return null;
//					}
//				}
//				
//				if(item.getClfNota2()==null){
//					if(item.getClfAsistenciaEstudiante2()!=null){
//						String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
//						if(!validadorAsistencia(cadena, nprfAsistenciaDocente)){
//							FacesUtil.limpiarMensaje();
//							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
//							nprfValidadorClic = 0;
//							return null;
//						}
//					}
//				}
//				
//				if(item.getClfAsistenciaEstudiante2()==null){
//					if(item.getClfNota2()!=null){
//						if(!validador(item.getClfNota2())){
//							FacesUtil.limpiarMensaje();
//							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
//							nprfValidadorClic = 0;
//							return null;
//						}
//					}
//				}
//			}
//		}else{
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
//			return null;
//		}
//		nprfValidadorClic = 1;
//		return null;
//	}
	
	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal(){
			for (EstudianteJdbcDto item : nprfListEstudianteBusq) {
				if(item.getClfSupletorio()!=null){
					if(!validador(item.getClfSupletorio())){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
						nprfValidadorClic = 0;
						return null;
					}
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" debe ingresar la nota de recuperación");
					nprfValidadorClic = 0;
					return null;
				}
				
			}
		nprfValidadorClic = 2;
		return null;
	}
	
	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista de paralelos
	 * @return XHTML listar paralelos
	 */
	public String guardar(){
		try {
			for (EstudianteJdbcDto item : nprfListEstudianteBusq) {
				RecordEstudianteDto rcesAux = new RecordEstudianteDto();
				rcesAux = servNprfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
				if(item.getClfSupletorio()!=null){
					
					BigDecimal parametro2Aux  = BigDecimal.ZERO;
					parametro2Aux  = item.getClfSupletorio().multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).divide(new BigDecimal(CalificacionConstantes.NOTA_MAX_PARCIAL_VALUE), 2, RoundingMode.DOWN);
					item.setClfParamRecuperacion2(parametro2Aux);
						
					BigDecimal sumaParametros = BigDecimal.ZERO;
					sumaParametros = item.getClfParamRecuperacion1().setScale(2, RoundingMode.DOWN).add(item.getClfParamRecuperacion2().setScale(2, RoundingMode.DOWN));
					item.setClfNotalFinalSemestre(sumaParametros.setScale(1, RoundingMode.HALF_UP));
					
					int estadoRces = item.getClfNotalFinalSemestre().compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));
					if(estadoRces == 1 || estadoRces ==0){
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
					}else{
						item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					}
					servNprfCalificacionServicio.guardarNotasPregradoRecuperacion(rcesAux, item , nprfRegCliente);
					nprfValidadorClic=0;
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Ingreso de notas exitoso");
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Debe ingresar la nota de recuperación de todos los estudiantes");
					return null;
				}
			}
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
				pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/recuperacion");
				jasperReport = (JasperReport) JRLoader.loadObject(new File(
						(pathGeneralReportes.toString() + "/reporteNotaRecuperacion.jasper")));
				List<Map<String, Object>> frmAdjuntoCampos = null;
				Map<String, Object> frmAdjuntoParametros = null;
//				String facultadMail = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
//				String carreraMail = GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
				
				
				
				frmAdjuntoParametros = new HashMap<String, Object>();
				SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
				String fecha = formato.format(new Date());
				frmAdjuntoParametros.put("fecha",fecha);
//				StringBuilder sbAsistenciaDocente = new StringBuilder();
				frmAdjuntoParametros.put("docente", nprfDocente.getPrsNombres()+" "+nprfDocente.getPrsPrimerApellido()+" "+nprfDocente.getPrsSegundoApellido());
				frmAdjuntoParametros.put("nick", nprfUsuario.getUsrNick());
				for (EstudianteJdbcDto item1 : nprfListEstudianteBusq) {
					nprfNomCarrera =  item1.getCrrDescripcion().toString();
					nprfNomMateria = item1.getMtrDescripcion().toString();
					nprfNomParalelo = item1.getPrlDescripcion().toString();;
					frmAdjuntoParametros.put("periodo", item1.getPracDescripcion());
					frmAdjuntoParametros.put("facultad", item1.getDpnDescripcion());
					frmAdjuntoParametros.put("carrera", item1.getCrrDetalle());
					frmAdjuntoParametros.put("curso", item1.getNvlDescripcion());
					frmAdjuntoParametros.put("paralelo", item1.getPrlDescripcion());
					frmAdjuntoParametros.put("materia", item1.getMtrDescripcion());
					break;
				}
				frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
				frmAdjuntoParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
				
				frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
				Map<String, Object> datoEstudiantes = null;
				int cont = 1;
				
				for (EstudianteJdbcDto item2 : nprfListEstudianteBusq) {
					StringBuilder sbNota3 = new StringBuilder();
//					StringBuilder sbAsistencia2 = new StringBuilder();
					StringBuilder sbContador = new StringBuilder();
					datoEstudiantes = new HashMap<String, Object>();
					datoEstudiantes.put("identificacion", item2.getPrsIdentificacion());
					datoEstudiantes.put("apellido_paterno", item2.getPrsPrimerApellido());
					datoEstudiantes.put("apellido_materno", item2.getPrsSegundoApellido());
					datoEstudiantes.put("nombres", item2.getPrsNombres());
					if(item2.getClfSupletorio() != null){
						sbNota3.append(item2.getClfSupletorio());
						datoEstudiantes.put("nota1", sbNota3.toString());
					}else{
						sbNota3.append("-");
						datoEstudiantes.put("nota1", sbNota3.toString());
					}
					sbContador.append(cont);
					datoEstudiantes.put("numero", sbContador.toString());
					
					frmAdjuntoCampos.add(datoEstudiantes);
					cont = cont + 1;
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
				correosTo.add(nprfDocente.getPrsMailInstitucional());
				//path de la plantilla del mail
				ProductorMailJson pmail = null;
				StringBuilder sbCorreo= new StringBuilder();
				sbCorreo= GeneralesUtilidades.generarAsunto(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
						nprfDocente.getPrsPrimerApellido()+" "+nprfDocente.getPrsSegundoApellido()+" "+nprfDocente.getPrsNombres() , nprfNomCarrera , nprfNomMateria , nprfNomParalelo);
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
			
		} catch (RecordEstudianteDtoException e) {
			 FacesUtil.mensajeError(e.getMessage());
			 
		} catch (RecordEstudianteDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			 
		}
		return "irListarParalelos";
	}	
	
	
	
	
	
	public boolean activacion(){
		boolean retorno = false;
		
		for (EstudianteJdbcDto item : nprfListEstudianteBusq) {
			int aux = item.getRcesIngersoNota3();
			if(aux == 2){
				retorno = true;
				break;
			}
			
		}
		return retorno;
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
		    		 FacesUtil.mensajeError("Calificación máximo sobre 20 puntos");
		    		 return false;
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("No se permiten números negativos");
					 return false;
				 } 
		     }else{
		    	 if(totalDecimales>1){
		    		 FacesUtil.limpiarMensaje();
		    		 FacesUtil.mensajeError("Sólo permite máximo 1 número decimales");
		    		 return false;
				 }else if(Float.parseFloat(valorSt)>20){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("Calificación máximo sobre 20 puntos");
					 return false;
				 }else if(Float.parseFloat(valorSt)<0){
					 FacesUtil.limpiarMensaje();
					 FacesUtil.mensajeError("No se permiten números negativos");
					 return false;
				 } 
		     }
		}
		catch (NumberFormatException ex) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Sólo números y signos decimales");
			 return false;
		}
		retorno  = true;
		return retorno;
	}
	
	
//	public Boolean validadorAsistencia(String asistenciaEst,Integer asistenciaDocente)  {
//		Boolean retorno = false;
//		try{
//			int valor =  Integer.parseInt(asistenciaEst);
//			if(valor>asistenciaDocente){
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("La asitencia no puede ser mayor a la asistencia del docente");
//				return false;
//			}
//		}catch(Exception e){
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError("Debe ingresar la asistencia del docente en el hemisemestre");
//			return false;
//		}
//		retorno = true;
//		return retorno;
//		
//	}
	
	
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		
		nprfEstudianteBuscar = null;
//		nprfListCarreraBusq = null;
		nprfListNivelBusq = null;
		nprfListMateriaBusq= null;
		
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		nprfEstudianteBuscar = new EstudianteJdbcDto();
//		//INICIALIZO EL PERIODO ACADEMICO ID
		nprfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO LA CARRERA ID
		nprfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO EL NIVEL ID
		nprfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO EL PARALELO ID
//		nprfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO LA MATERIA ID
		nprfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
//		//ANULO LA LISTA DE NIVEL
//		nprfListNivelBusq = null;
		//ANULO LA LISTA DE PARALELOS
		nprfListParaleloBusq = null;
//		//ANULO LA LISTA DE MATERIAS
//		nprfListMateriaBusq = null;
		
	}
	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada(){
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getNprfUsuario() {
		return nprfUsuario;
	}
	public void setNprfUsuario(Usuario nprfUsuario) {
		this.nprfUsuario = nprfUsuario;
	}
	public DocenteJdbcDto getNprfDocente() {
		return nprfDocente;
	}
	public void setNprfDocente(DocenteJdbcDto nprfDocente) {
		this.nprfDocente = nprfDocente;
	}
	public EstudianteJdbcDto getNprfEstudianteBuscar() {
		return nprfEstudianteBuscar;
	}
	public void setNprfEstudianteBuscar(EstudianteJdbcDto nprfEstudianteBuscar) {
		this.nprfEstudianteBuscar = nprfEstudianteBuscar;
	}
	public List<CarreraDto> getNprfListCarreraBusq() {
		nprfListCarreraBusq = nprfListCarreraBusq==null?(new ArrayList<CarreraDto>()):nprfListCarreraBusq;
		return nprfListCarreraBusq;
	}
	public void setNprfListCarreraBusq(List<CarreraDto> nprfListCarreraBusq) {
		this.nprfListCarreraBusq = nprfListCarreraBusq;
	}
	public List<NivelDto> getNprfListNivelBusq() {
		nprfListNivelBusq = nprfListNivelBusq==null?(new ArrayList<NivelDto>()):nprfListNivelBusq;
		return nprfListNivelBusq;
	}
	public void setNprfListNivelBusq(List<NivelDto> nprfListNivelBusq) {
		this.nprfListNivelBusq = nprfListNivelBusq;
	}
	public List<MateriaDto> getNprfListMateriaBusq() {
		nprfListMateriaBusq = nprfListMateriaBusq==null?(new ArrayList<MateriaDto>()):nprfListMateriaBusq;
		return nprfListMateriaBusq;
	}
	public void setNprfListMateriaBusq(List<MateriaDto> nprfListMateriaBusq) {
		this.nprfListMateriaBusq = nprfListMateriaBusq;
	}
	public List<EstudianteJdbcDto> getNprfListEstudianteBusq() {
		nprfListEstudianteBusq = nprfListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):nprfListEstudianteBusq;
		return nprfListEstudianteBusq;
	}
	public void setNprfListEstudianteBusq(List<EstudianteJdbcDto> nprfListEstudianteBusq) {
		this.nprfListEstudianteBusq = nprfListEstudianteBusq;
	}
	public List<ParaleloDto> getNprfListParaleloBusq() {
		nprfListParaleloBusq = nprfListParaleloBusq==null?(new ArrayList<ParaleloDto>()):nprfListParaleloBusq;
		return nprfListParaleloBusq;
	}
	public void setNprfListParaleloBusq(List<ParaleloDto> nprfListParaleloBusq) {
		this.nprfListParaleloBusq = nprfListParaleloBusq;
	}

	public ParaleloDto getNprfParaleloDtoEditar() {
		return nprfParaleloDtoEditar;
	}

	public void setNprfParaleloDtoEditar(ParaleloDto nprfParaleloDtoEditar) {
		nprfListMateriaBusq = nprfListMateriaBusq==null?(new ArrayList<MateriaDto>()):nprfListMateriaBusq;
		this.nprfParaleloDtoEditar = nprfParaleloDtoEditar;
	}

	public Integer getNprfAsistenciaDocente() {
		return nprfAsistenciaDocente;
	}


	public void setNprfAsistenciaDocente(Integer nprfAsistenciaDocente) {
		this.nprfAsistenciaDocente = nprfAsistenciaDocente;
	}


	public Integer getNprfValidadorClic() {
		return nprfValidadorClic;
	}


	public void setNprfValidadorClic(Integer nprfValidadorClic) {
		this.nprfValidadorClic = nprfValidadorClic;
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


	public String getNprfRegCliente() {
		return nprfRegCliente;
	}
	public void setNprfRegCliente(String nprfRegCliente) {
		this.nprfRegCliente = nprfRegCliente;
	}


	public String getNprfEstado() {
		return nprfEstado;
	}
	public void setNprfEstado(String nprfEstado) {
		this.nprfEstado = nprfEstado;
	}

	public String getNprfNomCarrera() {
		return nprfNomCarrera;
	}


	public void setNprfNomCarrera(String nprfNomCarrera) {
		this.nprfNomCarrera = nprfNomCarrera;
	}


	public String getNprfNomMateria() {
		return nprfNomMateria;
	}


	public void setNprfNomMateria(String nprfNomMateria) {
		this.nprfNomMateria = nprfNomMateria;
	}


	public String getNprfNomParalelo() {
		return nprfNomParalelo;
	}


	public void setNprfNomParalelo(String nprfNomParalelo) {
		this.nprfNomParalelo = nprfNomParalelo;
	}


	public List<RolFlujoCarrera> getNprfListRolFlujoCarreraBusq() {
		nprfListRolFlujoCarreraBusq = nprfListRolFlujoCarreraBusq==null?(new ArrayList<RolFlujoCarrera>()):nprfListRolFlujoCarreraBusq;
		return nprfListRolFlujoCarreraBusq;
	}


	public void setNprfListRolFlujoCarreraBusq(List<RolFlujoCarrera> nprfListRolFlujoCarreraBusq) {
		this.nprfListRolFlujoCarreraBusq = nprfListRolFlujoCarreraBusq;
	}


	public Dependencia getNprfDependenciaBuscar() {
		return nprfDependenciaBuscar;
	}


	public void setNprfDependenciaBuscar(Dependencia nprfDependenciaBuscar) {
		this.nprfDependenciaBuscar = nprfDependenciaBuscar;
	}


	public CronogramaActividadJdbcDto getNprfCronogramaActividadJdbcDtoBuscar() {
		return nprfCronogramaActividadJdbcDtoBuscar;
	}


	public void setNprfCronogramaActividadJdbcDtoBuscar(CronogramaActividadJdbcDto nprfCronogramaActividadJdbcDtoBuscar) {
		this.nprfCronogramaActividadJdbcDtoBuscar = nprfCronogramaActividadJdbcDtoBuscar;
	}
	


	
	
}
