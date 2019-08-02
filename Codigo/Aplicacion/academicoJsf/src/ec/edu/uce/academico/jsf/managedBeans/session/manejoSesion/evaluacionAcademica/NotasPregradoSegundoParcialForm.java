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
   
 ARCHIVO:     NotasPregradoSegundoParcialForm.java	  
 DESCRIPCION: Bean de sesion que maneja las notas del segundo parcial por docentes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-JULIO-2017 			Gabriel Mafla                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionAcademica;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (managed bean) NotasPregradoSegundoParcialForm.
 * Managed Bean que administra los estudiantes para el ingreso de las notas del segundo parcial por docentes.
 * @author ghmafla.
 * @version 1.0
 */

@ManagedBean(name="notasPregradoSegundoParcialForm")
@SessionScoped
public class NotasPregradoSegundoParcialForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;

	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL DATOS DEL USUARIO QUE INGRESA
	private Usuario npspfUsuario;
	
	//PARA BUSQUEDA DOCENTE
	private DocenteJdbcDto npspfDocente;

	//PARA BUSQUEDA
	private EstudianteJdbcDto npspfEstudianteBuscar;
	private List<CarreraDto> npspfListCarreraBusq;
	private List<NivelDto> npspfListNivelBusq;
	private List<MateriaDto> npspfListMateriaBusq;
	private List<EstudianteJdbcDto> npspfListEstudianteBusq;
	private List<ParaleloDto> npspfListParaleloBusq;
	private ParaleloDto npspfParaleloDtoEditar;
	
	private List<RolFlujoCarrera> npspfListRolFlujoCarreraBusq;
	
	private CronogramaActividadJdbcDto npspfCronogramaActividadJdbcDtoBuscar;
	
	private Dependencia npspfDependenciaBuscar;
	
	//PARA GUARDAR LA ASISTENCIA DEL DOCENTE DEL FORM
	private Integer npspfAsistenciaDocente;
	//PARA LA ACTIVACIÓN Y DESACTIVACIÓN DE BOTONES PARA EL REPORTE
	private Integer npspfValidadorClic;
	private String npspfEstado;
	//PARA GUARDAR LOS REGISTROS DE LA SESION DEL CLIENTE HOSTNAME, IPPIBLICA, IPPRIVADA
	private String npspfRegCliente;
	//private String thisIpAddress;
	//campos para el envio de la notificacion del ingreso final de notas al mail del docente	
	private String npspfNomCarrera;
	private String npspfNomMateria;
	private String npspfNomParalelo;

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
	private CarreraDtoServicioJdbc servNpspfCarreraDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servNpspfNivelDtoServicioJdbc;
	@EJB
	private MateriaDtoServicioJdbc servNpspfMateriaDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servNpspfEstudianteDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servNpspfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servNpspfDocenteDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servNpspfParaleloDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servNpspfNotasPregradoDtoServicioJdbc;
	@EJB
	private RecordEstudianteDtoServicioJdbc servNpspfRecordEstudianteDtoServicioJdbc;
	@EJB
	private MateriaServicio servNpspfMateriaDto;
	@EJB
	private CalificacionServicio servNpspfCalificacionServicio;
	@EJB
	private RolFlujoCarreraServicio servNpfRolFlujoCarreraServicio;
	
	@EJB
	private UsuarioRolServicio servNpfUsuarioRolServicio;
	@EJB
	private DependenciaServicio servNpspfDependenciaServicio;
	
	@EJB
	private CronogramaActividadDtoServicioJdbc servNpspfCronogramaActividadDtoServicioJdbcServicio;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarParalelos por docente
	 */
	public String irAnotaPregradoSegundoParcial(Usuario usuario){
		npspfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			
			UsuarioRol usro = servNpfUsuarioRolServicio.buscarXUsuarioXrol(npspfUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.Academica.usuario.rol.estado.desactivado")));
				return null;
			}
			
			npspfListRolFlujoCarreraBusq = servNpfRolFlujoCarreraServicio.buscarPorIdUsuario(npspfUsuario);
			iniciarParametros();
			//LISTO LOS CARRERAS ASIGNADAS AL DOCENTE
			npspfListCarreraBusq = servNpspfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivo(npspfUsuario.getUsrId(), RolConstantes.ROL_BD_DOCENTE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, npspfListRolFlujoCarreraBusq);
			retorno = "irAnotaPregradoSegundoParcial";
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
		idCarrera = npspfEstudianteBuscar.getCrrId();
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//BUSCO EL DOCENTE PARA LAS MATERIAS
				npspfDocente = servNpspfDocenteDtoServicioJdbc.buscarDocenteXUsuarioXCarreraXPeriodoActivo(npspfUsuario.getUsrId(), idCarrera, TipoPuestoConstantes.TIPO_DOCENTE_VALUE);
				//LISTO LOS NIVELES
				npspfListNivelBusq = servNpspfNivelDtoServicioJdbc.listarNivelXPeriodoActivoXCarrera(npspfEstudianteBuscar.getCrrId(), npspfDocente.getDtpsId());
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
		idNivel = npspfEstudianteBuscar.getNvlId();
		try {
			if(idNivel != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				npspfListMateriaBusq = servNpspfMateriaDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXparaleloXdocente( npspfEstudianteBuscar.getCrrId(), idNivel , npspfDocente.getDtpsId());
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
		npspfListParaleloBusq = null;
		try {
			if(npspfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.carrera.validacion.exception")));
			}else if(npspfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.nivel.validacion.exception")));
			}else if(npspfEstudianteBuscar.getMtrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.buscar.materia.validacion.exception")));
			}else{
				//BUSCO LA LISTA DE PARALELOS POR PERIODO ACTIVO, CARRERA, NIVEL,, MATERIA
				npspfListParaleloBusq = servNpspfParaleloDtoServicioJdbc.listarXperiodoActivoXcarreraXnivelXdocente(npspfEstudianteBuscar.getCrrId(), npspfEstudianteBuscar.getNvlId(), npspfDocente.getDtpsId(), npspfEstudianteBuscar.getMtrId());
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
		npspfParaleloDtoEditar = new ParaleloDto(); 
		npspfParaleloDtoEditar = prl;
		npspfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npspfDependenciaBuscar = new Dependencia();
			npspfDependenciaBuscar =  servNpspfDependenciaServicio.buscarPorId(npspfEstudianteBuscar.getCrrId());
			if(npspfDependenciaBuscar.getDpnId()==DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
				npspfCronogramaActividadJdbcDtoBuscar = servNpspfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
			}else{
				npspfCronogramaActividadJdbcDtoBuscar = servNpspfCronogramaActividadDtoServicioJdbcServicio.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoConstantes.PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE);
			}
			Timestamp myDate = new Timestamp(new Date().getTime());
			
			if(npspfUsuario.getUsrNick().equals("fesempertegui")){
				
			}else{
				if(myDate.after(npspfCronogramaActividadJdbcDtoBuscar.getPlcrFechaFin())){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
					return null;
				}
				
				if(myDate.before(npspfCronogramaActividadJdbcDtoBuscar.getPlcrFechaInicio())){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Esta fuera de las fechas establecidas");
					return null;
				}
			}
			
			
			List<String> datosCliente = new ArrayList<>();
			datosCliente=IdentifidorCliente.obtenerDatosCliente();
			String idHostAux =  new String();
			String ipLocalClienteAux =  new String();
//			String ipPublicaClienteAux = new String();
			idHostAux=datosCliente.get(0);
			ipLocalClienteAux=datosCliente.get(1);
//			ipPublicaClienteAux=datosCliente.get(6);
//			npspfRegCliente  = idHostAux.concat(" "+ipLocalClienteAux);
			Date fechaActual = new Date();
			npspfRegCliente ="Fecha de registro: "+fechaActual.toString().concat("|ACCION: NOTAS 1 ");
			npspfListEstudianteBusq = servNpspfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npspfParaleloDtoEditar.getCrrId(), npspfParaleloDtoEditar.getMlcrmtNvlId(), npspfParaleloDtoEditar.getPrlId(), npspfParaleloDtoEditar.getMtrId(),npspfDocente.getFcdcId(),npspfParaleloDtoEditar.getMlcrprId());
			for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
				if(!(item.getClfAsistenciaDocente2() != null)){
						npspfAsistenciaDocente = null;
				}else{
					npspfAsistenciaDocente = item.getClfAsistenciaDocente2();
					break;
				}
				
			}
			for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
				if(item.getRcesIngersoNota2()==RecordEstudianteConstantes.ESTADO_NOTA_GUARDADO_FINAL_VALUE){
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
	
	/*
	 * Metodo que permite generar el reporte
	 */
	public void generarReporte(ParaleloDto prl){
		
		npspfParaleloDtoEditar = new ParaleloDto(); 
		npspfParaleloDtoEditar = prl;
		npspfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npspfListEstudianteBusq = servNpspfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npspfParaleloDtoEditar.getCrrId(), npspfParaleloDtoEditar.getMlcrmtNvlId(), npspfParaleloDtoEditar.getPrlId(), npspfParaleloDtoEditar.getMtrId(),npspfDocente.getFcdcId(),npspfParaleloDtoEditar.getMlcrprId());
			
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
	}
	
	
	@SuppressWarnings("static-access")
	public String irVerNotas(ParaleloDto prl){
		ReporteNotasForm reporte = new ReporteNotasForm();
		npspfParaleloDtoEditar = new ParaleloDto(); 
		npspfParaleloDtoEditar = prl;
		npspfListEstudianteBusq = new ArrayList<EstudianteJdbcDto>();
		try {
			npspfListEstudianteBusq = servNpspfNotasPregradoDtoServicioJdbc.buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente(npspfParaleloDtoEditar.getCrrId(), npspfParaleloDtoEditar.getMlcrmtNvlId(), npspfParaleloDtoEditar.getPrlId(), npspfParaleloDtoEditar.getMtrId(),npspfDocente.getFcdcId(),npspfParaleloDtoEditar.getMlcrprId());
			
			activacion();
			reporte.generarReporteNotasSegundoHemisemestre(npspfListEstudianteBusq, npspfUsuario);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerNotas";
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
//        	npspfListEstudianteBusq.get(event.getRowIndex()).getFcesId();
//        	npspfListEstudianteBusq.get(event.getRowIndex()).getMlcrprId();
//        }
//    }
	
	
	
	/**
	 * Dirige la navegacion hacia la pagina de listar paralelos del docente por carrera nivel y materia 
	 * @return Xhtml listar
	 */
	public String irCancelar(){
		npspfParaleloDtoEditar = null;
		npspfListEstudianteBusq = null;
		npspfValidadorClic = new Integer(0);
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
	     itemCost  = new BigDecimal(asitenciaEst).multiply(new BigDecimal(porcentaje)).divide(new BigDecimal(asitenciaDoc), 0, RoundingMode.HALF_UP);
	     return itemCost;
	}
	
	/**
	 * verifica que haga click en el boton guardar temporal la nota
	 */
	public String verificarClicGuardadoTemporal(){
		if(npspfAsistenciaDocente!=null){
			for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
				item.setClfAsistenciaDocente2(npspfAsistenciaDocente);
				if(item.getClfNota2()!=null&&item.getClfAsistenciaEstudiante2()!=null){
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
					if(!validadorAsistencia(cadena, npspfAsistenciaDocente)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
						npspfValidadorClic = 0;
						return null;
					}
					if(!validador(item.getClfNota2())){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
						npspfValidadorClic = 0;
						return null;
					}
				}
				
				if(item.getClfNota2()==null){
					if(item.getClfAsistenciaEstudiante2()!=null){
						String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
						if(!validadorAsistencia(cadena, npspfAsistenciaDocente)){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
							npspfValidadorClic = 0;
							return null;
						}
					}
				}
				
				if(item.getClfAsistenciaEstudiante2()==null){
					if(item.getClfNota2()!=null){
						if(!validador(item.getClfNota2())){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
							npspfValidadorClic = 0;
							return null;
						}
					}
				}
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
			return null;
		}
		npspfValidadorClic = 1;
		return null;
	}
	
	/**
	 * verifica que haga click en el boton guardar final la nota
	 */
	public String verificarClicGuardadoFinal(){
		if(npspfAsistenciaDocente!=null){
			for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
				item.setClfAsistenciaDocente2(npspfAsistenciaDocente);
				if(item.getClfNota2()!=null&&item.getClfAsistenciaEstudiante2()!=null){
					String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
					if(!validadorAsistencia(cadena, npspfAsistenciaDocente)){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
						npspfValidadorClic = 0;
						return null;
					}
					if(!validador(item.getClfNota2())){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
						npspfValidadorClic = 0;
						return null;
					}
				}
				
				if(item.getClfNota2()==null){
					if(item.getClfAsistenciaEstudiante2()!=null){
						String cadena = String.valueOf(item.getClfAsistenciaEstudiante2());
						if(!validadorAsistencia(cadena, npspfAsistenciaDocente)){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la asistencia no es valida");
							npspfValidadorClic = 0;
							return null;
						}
					}
				}
				if(item.getClfAsistenciaEstudiante2()==null){
					if(item.getClfNota2()!=null){
						if(!validador(item.getClfNota2())){
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres()+" la nota no es valida");
							npspfValidadorClic = 0;
							return null;
						}
					}
				}
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
			return null;
		}
		npspfValidadorClic = 2;
		return null;
	}
	
	/**
	 * Metodo que guarda de forma definitiva los registros editados de la lista de paralelos
	 * @return XHTML listar paralelos
	 */
	public String guardar(){
		try {
			if(npspfAsistenciaDocente!=null){
				for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
					if(item.getClfNota2()==null ^ item.getClfAsistenciaEstudiante2()==null){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("EL registro por estudiante debe tener la nota y asistencia del primer hemisemestre");
						npspfValidadorClic=0;
						return null;
					}
					item.setClfAsistenciaDocente2(npspfAsistenciaDocente);
					RecordEstudianteDto rcesAux = new RecordEstudianteDto();
					rcesAux = servNpspfRecordEstudianteDtoServicioJdbc.buscarXRcesId(item.getFcesId());
					BigDecimal nota1Aux = BigDecimal.ZERO;
					BigDecimal nota2Aux = BigDecimal.ZERO;
					int asistenciaEstudiante1Aux;
					int asistenciaEstudiante2Aux;
					
					if(item.getClfNota1()!=null){
						nota1Aux=item.getClfNota1();
					}
					
					if(item.getClfNota2()!=null){
						nota2Aux = item.getClfNota2();
					}
					
					if(item.getClfAsistenciaEstudiante1()==null){
						asistenciaEstudiante1Aux = 0;
					}else{
						asistenciaEstudiante1Aux = item.getClfAsistenciaEstudiante1().intValue();
					}
					
					
					if(item.getClfAsistenciaEstudiante2()==null){
						asistenciaEstudiante2Aux = 0;
					}else{
						asistenciaEstudiante2Aux = item.getClfAsistenciaEstudiante2().intValue();
					}
					
					//calculo la suma de na nota1 + nota2 con redondeo de una cifra decimal
					BigDecimal sumaParciales = BigDecimal.ZERO;
					sumaParciales = nota1Aux.setScale(2, RoundingMode.DOWN).add(nota2Aux.setScale(2, RoundingMode.DOWN));
					item.setClfSumaP1P2(sumaParciales);
					
					//calculo de la suma de asistencia del estudiante de los dos parciales
					int sumaAsistenciaParciales= 0;
					sumaAsistenciaParciales = asistenciaEstudiante1Aux+asistenciaEstudiante2Aux;
					item.setClfAsistenciaTotal(new BigDecimal(sumaAsistenciaParciales));
					
					//calculo de la suma de la asistencia del docente de los dos parciales
					int sumaAsistenciaDoc = 0;
					sumaAsistenciaDoc = item.getClfAsistenciaDocente1() +  item.getClfAsistenciaDocente2();
					item.setClfAsistenciaTotalDoc(new BigDecimal(sumaAsistenciaDoc));
					
					//calcula el promedio de la asistencia del estudiante
					item.setClfPromedioAsistencia(calcularPorcentajeAsistencia(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE, item.getClfAsistenciaTotal().intValue(), item.getClfAsistenciaTotalDoc().intValue()));
					
					int com = item.getClfSumaP1P2().compareTo(new BigDecimal(27.5));
					//si la suma de los parciales es mayor o igual a 27.5
					if(com == 1 || com == 0){
						int promedioAsistencia = 0;
						promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
							// si el promedio de asistencia es mayor o igual a 80
						if(promedioAsistencia == 1 || promedioAsistencia == 0){
							//calcula la nota final del semestre y el estado a aprobado
							item.setClfNotalFinalSemestre(sumaParciales.setScale(1, RoundingMode.HALF_UP));
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
						}else{// si el promedio de asistencia es menor a 80
							item.setClfPromedioNotas(sumaParciales.setScale(0, RoundingMode.HALF_UP));
							item.setClfNotalFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP));
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
					}else{
						int minNota = item.getClfSumaP1P2().compareTo(new BigDecimal(8.8));
						//si la suma de los parciales el menor a 27.5 y es mayor o igual a 8.8
						if(minNota==0 || minNota==1){
							int promedioAsistencia = 0;
							promedioAsistencia = item.getClfPromedioAsistencia().compareTo(new BigDecimal(80));
								if(promedioAsistencia == 1 || promedioAsistencia == 0){
									BigDecimal itemCost  = BigDecimal.ZERO;
									itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
									item.setClfParamRecuperacion1(itemCost);
									item.setClfNotalFinalSemestre(sumaParciales.setScale(1, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
								}else{
									BigDecimal itemCost  = BigDecimal.ZERO;
									itemCost  = sumaParciales.multiply(new BigDecimal(CalificacionConstantes.PUNTAJE_40_PUNTOS_VALUE)).divide(new BigDecimal(CalificacionConstantes.PORCENTAJE_100_PORCIENTO_VALUE), 2, RoundingMode.DOWN);
									item.setClfParamRecuperacion1(itemCost);
									item.setClfNotalFinalSemestre(sumaParciales.setScale(1, RoundingMode.HALF_UP));
									item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
								}
						}else{
							item.setClfNotalFinalSemestre(sumaParciales.setScale(1, RoundingMode.HALF_UP));
							item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
						}
					}
						servNpspfCalificacionServicio.guardarNotasPregradoSegundoHemi(rcesAux, item , npspfRegCliente);
						try {
							servNpspfCalificacionServicio.guardarCorreccionFull(item.getClfId());
						} catch (Exception e) {
						}
						npspfValidadorClic=0;
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Ingreso de notas exitoso");
					}
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Debe ingresar la asistencia docente en el hemisemestre");
					return null;
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
				pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteNotas/segundoHemi");
				jasperReport = (JasperReport) JRLoader.loadObject(new File(
						(pathGeneralReportes.toString() + "/reporteNota2Hemi.jasper")));
				List<Map<String, Object>> frmAdjuntoCampos = null;
				Map<String, Object> frmAdjuntoParametros = null;
//				String facultadMail = GeneralesUtilidades.generaStringParaCorreo(item.getFclDescripcion());
//				String carreraMail = GeneralesUtilidades.generaStringParaCorreo(item.getCrrDescripcion());
				
				
				
				frmAdjuntoParametros = new HashMap<String, Object>();
				SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
				String fecha = formato.format(new Date());
				frmAdjuntoParametros.put("fecha",fecha);
				StringBuilder sbAsistenciaDocente = new StringBuilder();
				frmAdjuntoParametros.put("docente", npspfDocente.getPrsNombres()+" "+npspfDocente.getPrsPrimerApellido()+" "+npspfDocente.getPrsSegundoApellido());
				frmAdjuntoParametros.put("nick", npspfUsuario.getUsrNick());
				for (EstudianteJdbcDto itemAux : npspfListEstudianteBusq) {
					npspfNomCarrera =  itemAux.getCrrDescripcion().toString();
					npspfNomMateria = itemAux.getMtrDescripcion().toString();
					npspfNomParalelo = itemAux.getPrlDescripcion().toString();
					frmAdjuntoParametros.put("periodo", itemAux.getPracDescripcion());
					frmAdjuntoParametros.put("facultad", itemAux.getDpnDescripcion());
					frmAdjuntoParametros.put("carrera", itemAux.getCrrDetalle());
					frmAdjuntoParametros.put("curso", itemAux.getNvlDescripcion());
					frmAdjuntoParametros.put("paralelo", itemAux.getPrlDescripcion());
					frmAdjuntoParametros.put("materia", itemAux.getMtrDescripcion());
					sbAsistenciaDocente.append(itemAux.getClfAsistenciaDocente1());
					frmAdjuntoParametros.put("asistencia_docente", sbAsistenciaDocente.toString());	
					break;
				}
				frmAdjuntoParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraNotas.png");
				frmAdjuntoParametros.put("imagenPie", pathGeneralReportes+"/pieNotas.png");
				
				frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
				Map<String, Object> datoEstudiantes = null;
				int cont = 1;
				
				for (EstudianteJdbcDto itemAux2 : npspfListEstudianteBusq) {
					StringBuilder sbNota2 = new StringBuilder();
					StringBuilder sbAsistencia2 = new StringBuilder();
					StringBuilder sbContador = new StringBuilder();
					datoEstudiantes = new HashMap<String, Object>();
					datoEstudiantes.put("identificacion", itemAux2.getPrsIdentificacion());
					datoEstudiantes.put("apellido_paterno", itemAux2.getPrsPrimerApellido());
					datoEstudiantes.put("apellido_materno", itemAux2.getPrsSegundoApellido());
					datoEstudiantes.put("nombres", itemAux2.getPrsNombres());
					if(itemAux2.getClfNota2() != null){
						sbNota2.append(itemAux2.getClfNota2());
						datoEstudiantes.put("nota1", sbNota2.toString());
					}else{
						sbNota2.append("-");
						datoEstudiantes.put("nota1", sbNota2.toString());
					}
					if(itemAux2.getClfAsistenciaEstudiante2() != null){
						sbAsistencia2.append(itemAux2.getClfAsistenciaEstudiante2());
						datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
					}else{
						sbAsistencia2.append("-");
						datoEstudiantes.put("asistencia1", sbAsistencia2.toString());
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
				correosTo.add(npspfDocente.getPrsMailInstitucional());
				//path de la plantilla del mail
				ProductorMailJson pmail = null;
				StringBuilder sbCorreo= new StringBuilder();
				sbCorreo= GeneralesUtilidades.generarAsunto(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
						npspfDocente.getPrsPrimerApellido()+" "+npspfDocente.getPrsSegundoApellido()+" "+npspfDocente.getPrsNombres() , npspfNomCarrera , npspfNomMateria , npspfNomParalelo);
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
			 
		} catch (CalificacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CalificacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irListarParalelos";
	}	
	
	public boolean activacion(){
		boolean retorno = false;
		
		for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
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
			if(npspfAsistenciaDocente!=null){
				for (EstudianteJdbcDto item : npspfListEstudianteBusq) {
					if(item.getClfNota2()==null ^ item.getClfAsistenciaEstudiante2()==null){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("EL registro por estudiante debe tener la nota y asistencia del primer hemisemestre");
						npspfValidadorClic=0;
						return null;
					}else{
						item.setClfAsistenciaDocente2(npspfAsistenciaDocente);
//						if (item.getClfNota2()!=null && item.getClfAsistenciaEstudiante2()!=null){
							RecordEstudianteDto rcesAux = new RecordEstudianteDto();
							rcesAux = servNpspfRecordEstudianteDtoServicioJdbc.buscarXFcesIdXMlcrprXPracActivo(item.getFcesId(), item.getMlcrprId());
							servNpspfCalificacionServicio.guardoTemporalNotasPregradoSegundoHemi(rcesAux, item, npspfRegCliente);
							npspfValidadorClic=0;
//						}
					}
				}
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Guardado temporal de notas exitoso");
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Debe ingresar la asistencia del docente");
				npspfValidadorClic=0;
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
			FacesUtil.mensajeError("Debe ingresar la asistencia del docente en el hemisemestre");
			return false;
		}
		retorno = true;
		return retorno;
		
	}
	
	
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		
		npspfEstudianteBuscar = null;
//		npspfListCarreraBusq = null;
		npspfListNivelBusq = null;
		npspfListMateriaBusq= null;
		
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		npspfEstudianteBuscar = new EstudianteJdbcDto();
//		//INICIALIZO EL PERIODO ACADEMICO ID
		npspfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO LA CARRERA ID
		npspfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO EL NIVEL ID
		npspfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO EL PARALELO ID
//		npspfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		//INICIALIZO LA MATERIA ID
		npspfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
//		//ANULO LA LISTA DE NIVEL
//		npspfListNivelBusq = null;
		//ANULO LA LISTA DE PARALELOS
		npspfListParaleloBusq = null;
//		//ANULO LA LISTA DE MATERIAS
//		npspfListMateriaBusq = null;
		
	}
	/**
	 * Método que sirve para la activación de botones
	 */
	public void nada(){
	}
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getNpspfUsuario() {
		return npspfUsuario;
	}
	public void setNpspfUsuario(Usuario npspfUsuario) {
		this.npspfUsuario = npspfUsuario;
	}
	public DocenteJdbcDto getNpspfDocente() {
		return npspfDocente;
	}
	public void setNpspfDocente(DocenteJdbcDto npspfDocente) {
		this.npspfDocente = npspfDocente;
	}
	public EstudianteJdbcDto getNpspfEstudianteBuscar() {
		return npspfEstudianteBuscar;
	}
	public void setNpspfEstudianteBuscar(EstudianteJdbcDto npspfEstudianteBuscar) {
		this.npspfEstudianteBuscar = npspfEstudianteBuscar;
	}
	public List<CarreraDto> getNpspfListCarreraBusq() {
		npspfListCarreraBusq = npspfListCarreraBusq==null?(new ArrayList<CarreraDto>()):npspfListCarreraBusq;
		return npspfListCarreraBusq;
	}
	public void setNpspfListCarreraBusq(List<CarreraDto> npspfListCarreraBusq) {
		this.npspfListCarreraBusq = npspfListCarreraBusq;
	}
	public List<NivelDto> getNpspfListNivelBusq() {
		npspfListNivelBusq = npspfListNivelBusq==null?(new ArrayList<NivelDto>()):npspfListNivelBusq;
		return npspfListNivelBusq;
	}
	public void setNpspfListNivelBusq(List<NivelDto> npspfListNivelBusq) {
		this.npspfListNivelBusq = npspfListNivelBusq;
	}
	public List<MateriaDto> getNpspfListMateriaBusq() {
		npspfListMateriaBusq = npspfListMateriaBusq==null?(new ArrayList<MateriaDto>()):npspfListMateriaBusq;
		return npspfListMateriaBusq;
	}
	public void setNpspfListMateriaBusq(List<MateriaDto> npspfListMateriaBusq) {
		this.npspfListMateriaBusq = npspfListMateriaBusq;
	}
	public List<EstudianteJdbcDto> getNpspfListEstudianteBusq() {
		npspfListEstudianteBusq = npspfListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):npspfListEstudianteBusq;
		return npspfListEstudianteBusq;
	}
	public void setNpspfListEstudianteBusq(List<EstudianteJdbcDto> npspfListEstudianteBusq) {
		this.npspfListEstudianteBusq = npspfListEstudianteBusq;
	}
	public List<ParaleloDto> getNpspfListParaleloBusq() {
		npspfListParaleloBusq = npspfListParaleloBusq==null?(new ArrayList<ParaleloDto>()):npspfListParaleloBusq;
		return npspfListParaleloBusq;
	}
	public void setNpspfListParaleloBusq(List<ParaleloDto> npspfListParaleloBusq) {
		this.npspfListParaleloBusq = npspfListParaleloBusq;
	}

	public ParaleloDto getNpspfParaleloDtoEditar() {
		return npspfParaleloDtoEditar;
	}

	public void setNpspfParaleloDtoEditar(ParaleloDto npspfParaleloDtoEditar) {
		npspfListMateriaBusq = npspfListMateriaBusq==null?(new ArrayList<MateriaDto>()):npspfListMateriaBusq;
		this.npspfParaleloDtoEditar = npspfParaleloDtoEditar;
	}

	public Integer getNpspfAsistenciaDocente() {
		return npspfAsistenciaDocente;
	}


	public void setNpspfAsistenciaDocente(Integer npspfAsistenciaDocente) {
		this.npspfAsistenciaDocente = npspfAsistenciaDocente;
	}


	public Integer getNpspfValidadorClic() {
		return npspfValidadorClic;
	}


	public void setNpspfValidadorClic(Integer npspfValidadorClic) {
		this.npspfValidadorClic = npspfValidadorClic;
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


	public String getNpspfRegCliente() {
		return npspfRegCliente;
	}
	public void setNpspfRegCliente(String npspfRegCliente) {
		this.npspfRegCliente = npspfRegCliente;
	}


	public String getNpspfEstado() {
		return npspfEstado;
	}
	public void setNpspfEstado(String npspfEstado) {
		this.npspfEstado = npspfEstado;
	}

	public String getNpspfNomCarrera() {
		return npspfNomCarrera;
	}


	public void setNpspfNomCarrera(String npspfNomCarrera) {
		this.npspfNomCarrera = npspfNomCarrera;
	}


	public String getNpspfNomMateria() {
		return npspfNomMateria;
	}


	public void setNpspfNomMateria(String npspfNomMateria) {
		this.npspfNomMateria = npspfNomMateria;
	}


	public String getNpspfNomParalelo() {
		return npspfNomParalelo;
	}


	public void setNpspfNomParalelo(String npspfNomParalelo) {
		this.npspfNomParalelo = npspfNomParalelo;
	}


	public List<RolFlujoCarrera> getNpspfListRolFlujoCarreraBusq() {
		npspfListRolFlujoCarreraBusq = npspfListRolFlujoCarreraBusq==null?(new ArrayList<RolFlujoCarrera>()):npspfListRolFlujoCarreraBusq;
		return npspfListRolFlujoCarreraBusq;
	}


	public void setNpspfListRolFlujoCarreraBusq(List<RolFlujoCarrera> npspfListRolFlujoCarreraBusq) {
		this.npspfListRolFlujoCarreraBusq = npspfListRolFlujoCarreraBusq;
	}


	public CronogramaActividadJdbcDto getNpspfCronogramaActividadJdbcDtoBuscar() {
		return npspfCronogramaActividadJdbcDtoBuscar;
	}


	public void setNpspfCronogramaActividadJdbcDtoBuscar(CronogramaActividadJdbcDto npspfCronogramaActividadJdbcDtoBuscar) {
		this.npspfCronogramaActividadJdbcDtoBuscar = npspfCronogramaActividadJdbcDtoBuscar;
	}


	public Dependencia getNpspfDependenciaBuscar() {
		return npspfDependenciaBuscar;
	}


	public void setNpspfDependenciaBuscar(Dependencia npspfDependenciaBuscar) {
		this.npspfDependenciaBuscar = npspfDependenciaBuscar;
	}
	
	
}
