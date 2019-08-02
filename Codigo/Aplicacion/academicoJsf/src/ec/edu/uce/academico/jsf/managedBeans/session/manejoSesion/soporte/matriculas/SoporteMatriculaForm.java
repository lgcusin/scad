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
   
 ARCHIVO:     SoporteMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja temas de soporte para matriculas. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-AGO-2018 			Freddy Guzmán						Sopote para matriculas pregrado
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte.matriculas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobantePagoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MailConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SysRecConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.mail.MailRegistroMatricula;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteProblemasGeneralesForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;

/**
 * Clase (managed bean) SoporteMatriculaForm. Managed Bean que
 * administra temas relacionados al soporte de matriculas de pregrado.
 * @author fgguzman v1.
 * @version 1.0
 */

@ManagedBean(name = "soporteMatriculaForm")
@SessionScoped
public class SoporteMatriculaForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	private Usuario smfUsuario;
	
	private String smfIdentificacion;
	private Integer smfPeriodoId;
	private Integer smfCarreraId;
	
	private EstudianteJdbcDto smfEstudianteJdbcDto;
	private List<CarreraDto> smfListCarreraDto;
	private List<ComprobantePagoDto> smfListComprobantePagoDto;
	private List<RecordEstudianteDto> smfListRecordEstudianteDto;
	private List<PeriodoAcademicoDto> smfListPeriodoAcademicoDto;
	private List<EstudianteJdbcDto> smfListEstudianteJdbcDto;
	private Date smfFechaBusqueda;
	
	private int smfActivarReporte;
	private String smfNombreReporte;
	private String smfNombreArchivo;

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB private MatriculaServicioJdbc servJdbcErafMatricula;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private PersonaServicio servPersona;
	@EJB private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB private CarreraServicio servCarrera;
	@EJB private UsuarioRolServicio servUsuarioRolServicio;
	@EJB private ComprobantePagoServicioJdbc servComprobantePagoServicioJdbc;
	@EJB private ComprobantePagoServicio servComprobantePago;
	@EJB private CronogramaDtoServicioJdbc servCronogramaServicioJdbc;
	@EJB private PeriodoAcademicoDtoServicioJdbc serJdbcPeriodoAcademicoDto;

	// *******************************************************************/
	// **************** METODOS GENERALES DE NAVEGACIÓN  *****************/
	// *******************************************************************/

//  IDA
	
	public String irBuscarEstudiantes(Usuario usuario) {
		
		smfUsuario = usuario;
		smfActivarReporte = 0;
		smfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		smfIdentificacion = new String();
		smfListEstudianteJdbcDto = null;
		
		List<PeriodoAcademicoDto> periodos = null;
		try {
			periodos = serJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE},new Integer[]{PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE} );
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		if (periodos != null && periodos.size() > 0) {
			smfPeriodoId = periodos.get(periodos.size()-1).getPracId();
			smfListPeriodoAcademicoDto = periodos;
			return "irListarEstudiantesSoporte";
		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se encontró períodos académicos activos o en cierre ");
			return null;
		}
	}
	
	
	public String irBuscarComprobantes(Usuario usuario) {
		smfUsuario = usuario;
		smfFechaBusqueda = null;
		smfListComprobantePagoDto = null;
		return "irListarComprobantesSoporte";
	}
	
	
	public String irComprobantesPago(EstudianteJdbcDto estudiante){
		smfEstudianteJdbcDto = estudiante;
		smfCarreraId = estudiante.getCrrId();
		
		List<ComprobantePagoDto> comprobantes = cargarComprobantesPago(estudiante.getPrsIdentificacion());
		if (comprobantes != null && comprobantes.size() > 0) {
			
			BigDecimal valorApagar = BigDecimal.ZERO;
			
			try {
				
				smfListRecordEstudianteDto = new ArrayList<>();
				
				List<RecordEstudianteDto> materiasMatricula = servComprobantePagoServicioJdbc.buscarMatricula(estudiante.getPrsIdentificacion(), estudiante.getCrrId(),smfPeriodoId);
				if (!materiasMatricula.isEmpty()) {
					smfListRecordEstudianteDto = materiasMatricula;
					for (RecordEstudianteDto item : materiasMatricula) {
						if (item.getRcesDetalleMatriculaDto().getDtmtValorPorMateria()!= null) {
							valorApagar = valorApagar.add(item.getRcesDetalleMatriculaDto().getDtmtValorPorMateria());
						}
					}
				}
				
			} catch (RecordEstudianteNoEncontradoException e) {
				FacesUtil.mensajeError("El estudiante no registra matrícula en la carrera seleccionada.");
			} catch (RecordEstudianteException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
			for (ComprobantePagoDto it : comprobantes) {
				it.setCmpaAcceso(Boolean.TRUE);//disable
				it.setCmpaTotalMatricula(valorApagar);

				if (it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_COMPROBANTE_PAGADO_VALUE) 
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_RECIBO_CERRADO_VALUE)
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_RECIBO_PAGADO_VALUE)
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_VOUCHER_FACTURADA_VALUE)
						|| it.getCmpaEstadoValue().equals(SysRecConstantes.ESDC_VOUCHER_PREFACTURADA_VALUE)) {
					if (cambiarBigDecimalToString(valorApagar,1).equals(cambiarBigDecimalToString(it.getCmpaTotalAPagar(),1))) {
						it.setCmpaAcceso(Boolean.FALSE);
						it.setCmpaTotalMatricula(valorApagar);
					}
					
				}
			}
			
			
			smfListComprobantePagoDto = comprobantes;
			
			return "irListarComprobantes";	
		}else {
			FacesUtil.mensajeInfo("El estudiante con identificación " + estudiante.getPrsIdentificacion() + " no tiene comprobantes en SYSREC.");
			return null;
		}
		
	}
	
	


//	RETORNO
	public String irInicio() {
		limpiarFormEstudiantes();
		smfEstudianteJdbcDto = null;
		smfListEstudianteJdbcDto = null;
		smfListRecordEstudianteDto= null;
		smfPeriodoId = null;
		smfCarreraId = null;
		smfFechaBusqueda = null;
		smfListComprobantePagoDto = null;
		return "irInicio";
	}
	
	public String irEstudiantes() {
		limpiarFormEstudiantes();
		smfPeriodoId = smfListPeriodoAcademicoDto.get(smfListPeriodoAcademicoDto.size()-1).getPracId();
		smfEstudianteJdbcDto = null;
		smfListEstudianteJdbcDto = null;
		smfListRecordEstudianteDto= null;
		return "irEstudiantes";
	}
	
	
	
	public void limpiarFormEstudiantes() {
		smfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		smfIdentificacion = new String();
		smfListEstudianteJdbcDto = null;
	}
	
	
	public void limpiarFormLegalizar() {
		smfFechaBusqueda = null;
		smfListComprobantePagoDto = null;
	}
	

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/
	
	/**
	 * Método que permite actualizar el estado de la matricula, pasar inscrito a matriculado.
	 * @param comprobantePago - recibo o comprobante cobrados.
	 */
	public void legalizarMatricula(ComprobantePagoDto comprobantePago){
		
		if (!comprobantePago.getCmpaAcceso()) {

			if (!smfListRecordEstudianteDto.isEmpty()) {

				try {
					Integer retorno = servComprobantePagoServicioJdbc.legalizarMatricula(smfIdentificacion, smfCarreraId, smfPeriodoId);
					if (!retorno.equals(0)) {
						FacesUtil.mensajeInfo("Matrícula legalizada con éxito. Materias legalizas: " + retorno);							

						try {
							comprobantePago.setCmpaId(smfListRecordEstudianteDto.get(smfListRecordEstudianteDto.size()-1).getRcesComprobantePagoDto().getCmpaId());
							servComprobantePago.actualizarComprobantePago(comprobantePago);
						} catch (ComprobantePagoNoEncontradoException e) {
							FacesUtil.mensajeError(e.getMessage());
						} catch (ComprobantePagoValidacionException e) {
							FacesUtil.mensajeError(e.getMessage());
						}
						
					}else {
						FacesUtil.mensajeError("La matrícula ya fue legalizada.");							
					}
				} catch (RecordEstudianteValidacionException e) {
					FacesUtil.mensajeError("Comuníquese con el administrador del sistema, para solventar problemas al legalizar su matrícula.");
				} catch (RecordEstudianteException e) {
					FacesUtil.mensajeError(e.getMessage());
				}

			}else {
				FacesUtil.mensajeError("Error al cargar el registro de matrícula del estudiante.");
			}

		}
		
	}
	
	
	
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscarEstudiantesSIIU() {
		smfListEstudianteJdbcDto = null;
		
		if (smfPeriodoId != GeneralesConstantes.APP_ID_BASE) {
			if (smfIdentificacion.length() > 0 || smfIdentificacion.length() > 0) {

				List<String> auxCarreras = new ArrayList<>();
				auxCarreras.add(String.valueOf(GeneralesConstantes.APP_ID_BASE));
				
				List<EstudianteJdbcDto> estudiantes = cargarEstudiantesSIIU(smfIdentificacion, auxCarreras, 0);
				if (estudiantes != null && estudiantes.size() > 0) {
					List<EstudianteJdbcDto> aux = new ArrayList<>();
					Map<Integer, EstudianteJdbcDto> mapPerson =  new HashMap<Integer, EstudianteJdbcDto>();

					for (EstudianteJdbcDto it : estudiantes) {
						mapPerson.put(it.getCrrId(),it);
					}
					for (Entry<Integer, EstudianteJdbcDto> it : mapPerson.entrySet()) {
						aux.add(it.getValue());
					}
					
					smfListEstudianteJdbcDto = aux;
					smfListEstudianteJdbcDto.sort(Comparator.comparing(EstudianteJdbcDto::getPrsIdentificacion).thenComparing(EstudianteJdbcDto::getCrrDescripcion).thenComparing(EstudianteJdbcDto:: getPrsPrimerApellido).thenComparing(EstudianteJdbcDto:: getPrsSegundoApellido).thenComparing(EstudianteJdbcDto:: getPrsNombres));
					
				}else {
					FacesUtil.mensajeInfo("No se encontró resultados con la identificación ingresada. Intente con otra cédula o pasaporte.");	
				}

			} else {
				FacesUtil.mensajeInfo("Ingrese la cédula o pasaporte del estudiante para continuar.");
			}

		} else {
			FacesUtil.mensajeInfo("Seleccione un Período Académico para continuar.");
		}
		
	}
	
	public void buscarComprobantesCancelados(){
		smfListComprobantePagoDto = null;	
		
		if (smfFechaBusqueda != null) {
			
			List<ComprobantePagoDto> comprobantes = cargarComprobantesPagados();
			if (comprobantes != null && comprobantes.size() > 0) {
				List<ComprobantePagoDto> auxComprobantes = new ArrayList<>();
				for (ComprobantePagoDto item : comprobantes) {
					ComprobantePagoDto cmp = verificarEstadoEnSIIU(item);
					if (cmp != null) {
						auxComprobantes.add(item);
					}
				}
				
				if (auxComprobantes.size() > 0) {
					smfListComprobantePagoDto = auxComprobantes;	
				}else {
					FacesUtil.mensajeInfo("No se encontró comprobantes de pago Cancelados sin Legalizar matrícula en SIIU.");
				}
				
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontró comprobantes con los parámetros ingresados.");
			}	
		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Seleccione una fecha en el Calendario para continuar con la búsqueda.");
		}
		
	}
	
	private ComprobantePagoDto verificarEstadoEnSIIU(ComprobantePagoDto item) {
		try {
			return servComprobantePagoServicioJdbc.buscarComprobante(item.getCmpaNumero());
		} catch (ComprobantePagoException e) {
			return null;
		} catch (ComprobantePagoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage() + item.getCmpaNumero());
			return null;
		} catch (ComprobantePagoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage() + item.getCmpaNumero());
			return null;
		}
	}


	public void establecerFecha(Date fecha){
		
		if (fecha != null) {
			smfFechaBusqueda = fecha;	
		}
		
	}
	
	private List<EstudianteJdbcDto> cargarEstudiantesSIIU(String param, List<String> carreras, int tipoBusqueda){
		try {
			return servJdbcEstudianteDto.buscarEstudiantePorCarrerasSIIU(param, carreras, tipoBusqueda);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	


	private List<ComprobantePagoDto> cargarComprobantesPago(String prsIdentificacion) {
		
		try {
			List<CronogramaDto> procesos = cargarProcesoFlujo();
			CronogramaDto cronograma = null;
			if (procesos != null && procesos.size() >0) {
				
				for (CronogramaDto it : procesos) {
					if (it.getPrflId() == ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE && it.getPracId() == smfPeriodoId.intValue()) {
						cronograma = it;
						break;
					}
				}
				
			}
			
			
			if (cronograma != null && cronograma.getPlcrFechaInicial() != null) {
				return servComprobantePagoServicioJdbc.buscarComprobantesDePago(prsIdentificacion, cambiarTimestampToString(cronograma.getPlcrFechaInicial() , "yyyy-MM-dd"));
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("No existe cronograma para el proceso de Matrículas Ordinarias.");
				return null;
			}
			
		} catch (ComprobantePagoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ComprobantePagoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} 
		
	}
	
	
	
	/**
	 * Metodo que convierte timestamp en String.
	 * @param timestamp - fecha
	 * @param entrada - formato
	 * @return Java.Util.Date
	 */
	private String cambiarTimestampToString(Timestamp timestamp, String salida) {
		String retorno = "";
		SimpleDateFormat sdf = new SimpleDateFormat(salida);
		Date fecha = null;
		
		try {
			fecha = sdf.parse(timestamp.toString());
			retorno = sdf.format(fecha);
		} catch (ParseException e) {
		}
		
		return retorno;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private  List<ComprobantePagoDto> cargarComprobantesPagados(){
		
		try{
			return  servComprobantePagoServicioJdbc.buscarComprobantesDePagoFecha(GeneralesUtilidades.cambiarDateToStringFormatoFecha(smfFechaBusqueda, "yyyy-MM-dd"));
		} catch (ComprobantePagoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ComprobantePagoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
	}
	
	public void cambiarEstadoInscritoMatriculado(){
		boolean actualizados = true;
		
		if (smfListComprobantePagoDto != null && smfListComprobantePagoDto.size() >0) {
			
			List<ComprobantePagoDto> problemas = new ArrayList<>();
			for (ComprobantePagoDto it : smfListComprobantePagoDto) {


				try {
					servComprobantePagoServicioJdbc.legalizarMatricula(it.getCmpaEstudianteDto().getPrsIdentificacion(), it.getCmpaNumero());
					servComprobantePagoServicioJdbc.actualizarComprobateMatricula(it.getCmpaEstudianteDto().getPrsIdentificacion(), it);
				} catch (RecordEstudianteValidacionException e) {
					actualizados = false;
					problemas.add(it);
					FacesUtil.mensajeError(e.getMessage());
				} catch (RecordEstudianteException e) {
					actualizados = false;
					problemas.add(it);
					FacesUtil.mensajeError(e.getMessage());
				}

			}
			
			if (!actualizados) {
				if (problemas.size() > 0) {
					enviarMailRegistroMatricula(problemas);
					FacesUtil.limpiarMensaje();
				}
			}
			
			smfListComprobantePagoDto = null;
			FacesUtil.mensajeInfo("Comprobantes actualizados con éxito.");

		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No hay comprobantes para legalizar matrículas.");
		}
	}
	
	public void enviarMailRegistroMatricula(List<ComprobantePagoDto> listMateriaDto){

		try{

			StringBuilder sbNombres = new StringBuilder(); 
			sbNombres.append("FREDDY GEOVANNY GUZMÁN ALARCÓN");

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
//			correosTo.add(smfUsuario.getUsrPersona().getPrsMailInstitucional());
			correosTo.add("fgguzman@uce.edu.ec");

			//path de la plantilla del mail
			ProductorMailJson pmail = null;
			StringBuilder sbCorreo= new StringBuilder();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			
			sbCorreo= MailRegistroMatricula.generarMailCorreccionesAtiempoLegalizarMatricula(
					GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
					GeneralesUtilidades.generaStringParaCorreo(sbNombres.toString()),
					GeneralesUtilidades.generaStringParaCorreo("DIRECCIÓN DE TECNOLOGÍAS DE INFORMACIÓN Y COMUNICACIÓN"));
			
			pmail = new ProductorMailJson(
					correosTo, 
					null,
					null, 
					GeneralesConstantes.APP_MAIL_BASE,
					MailConstantes.ENCABEZADO_CORREO_MATRICULAS_DUPLICADAS_LABEL,
					sbCorreo.toString(),
					"admin", 
					"dt1c201s", 
					true,
					ReporteProblemasGeneralesForm.generarReporteLegalizarMatriculas(smfUsuario,listMateriaDto),
					"Notificación de Errores."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
			String jsonSt = pmail.generarMail();
			Gson json = new Gson();
			MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
			// 	Iniciamos el envío de mensajes
			ObjectMessage message = session.createObjectMessage(mailDto);
			producer.send(message);

		} catch (Exception e) {
			System.out.println("Error Codigo: AX325 : Error al enviar el registro de matriculas por mail.");
		}

	}

	/**
	 * Método que permite recuperar el flujo de los procesos con la planificacion segun cronogramas.
	 */
	private List<CronogramaDto> cargarProcesoFlujo(){
		try {
			return servCronogramaServicioJdbc.buscarProcesoFlujo(CronogramaConstantes.TIPO_ACADEMICO_VALUE, new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE});
		} catch (CronogramaDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CronogramaDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	
	
	
	public String getComprobanteEstadoLabel(int param){
		String estado =  " - ";
		
		switch (param) {
		case SysRecConstantes.ESDC_COMPROBANTE_EMITIDO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_EMITIDO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_ANULADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_ANULADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_ENVIADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_ENVIADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_FACTURADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_FACTURADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_PAGADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_PAGADO_LABEL;
			break;
		case SysRecConstantes. ESDC_COMPROBANTE_CADUCADO_VALUE:
			estado = SysRecConstantes.ESDC_COMPROBANTE_CADUCADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_PAGADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_PAGADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_ANULADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_ANULADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_CERRADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_CERRADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_FACTURADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_FACTURADO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_DEVUELTO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_DEVUELTO_LABEL;
			break;
		case SysRecConstantes. ESDC_RECIBO_DEPOSITADO_VALUE:
			estado = SysRecConstantes.ESDC_RECIBO_DEPOSITADO_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_PREFACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_PREFACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_FACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_FACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_ANULADA_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_ANULADA_LABEL;
			break;
		case SysRecConstantes. ESDC_TRANSFERENCIA_POR_COBRAR_VALUE:
			estado = SysRecConstantes.ESDC_TRANSFERENCIA_POR_COBRAR_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_PREFACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_PREFACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_FACTURADA_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_FACTURADA_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_ANULADA_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_ANULADA_LABEL;
			break;
		case SysRecConstantes. ESDC_VOUCHER_POR_COBRAR_VALUE:
			estado = SysRecConstantes.ESDC_VOUCHER_POR_COBRAR_LABEL;
			break;
		}
		
		return estado;
	}
	
	public String cambiarTimestampToString(Timestamp param){
		if (param != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date fecha;
			
			try {
				fecha = sdf.parse(param.toString());
			} catch (ParseException e) {
				return "";
			}
			
			return sdf.format(fecha);
		}else {
			return "";
		}
	}
	
	public static String cambiarBigDecimalToString(BigDecimal param, int simbolo){
		
		if (param != null && param.intValue() != 0) {
			if (simbolo == 0) {
				return String.format("%.2f", param) + " %";	
			}else if(param.intValue() == -1 ){
				return "";
			}else if(simbolo == 1 ){
				return String.format("%.2f", param);
			}else if (simbolo == 2) {
				return String.valueOf(param.intValue());
			}else if (simbolo == 3) {
				return String.format("$ %.2f", param);	
			}
		}
		
		return "";
	}
// ****************************************************************/
// ******************* GETTERS Y SETTERS **************************/
// ****************************************************************/


	public Usuario getSmfUsuario() {
		return smfUsuario;
	}


	public void setSmfUsuario(Usuario smfUsuario) {
		this.smfUsuario = smfUsuario;
	}


	public String getSmfIdentificacion() {
		return smfIdentificacion;
	}


	public void setSmfIdentificacion(String smfIdentificacion) {
		this.smfIdentificacion = smfIdentificacion;
	}


	public List<RecordEstudianteDto> getSmfListRecordEstudianteDto() {
		return smfListRecordEstudianteDto;
	}


	public void setSmfListRecordEstudianteDto(List<RecordEstudianteDto> smfListRecordEstudianteDto) {
		this.smfListRecordEstudianteDto = smfListRecordEstudianteDto;
	}


	public int getSmfActivarReporte() {
		return smfActivarReporte;
	}


	public void setSmfActivarReporte(int smfActivarReporte) {
		this.smfActivarReporte = smfActivarReporte;
	}


	public String getSmfNombreReporte() {
		return smfNombreReporte;
	}


	public void setSmfNombreReporte(String smfNombreReporte) {
		this.smfNombreReporte = smfNombreReporte;
	}


	public String getSmfNombreArchivo() {
		return smfNombreArchivo;
	}


	public void setSmfNombreArchivo(String smfNombreArchivo) {
		this.smfNombreArchivo = smfNombreArchivo;
	}


	public List<ComprobantePagoDto> getSmfListComprobantePagoDto() {
		return smfListComprobantePagoDto;
	}


	public void setSmfListComprobantePagoDto(List<ComprobantePagoDto> smfListComprobantePagoDto) {
		this.smfListComprobantePagoDto = smfListComprobantePagoDto;
	}


	public Integer getSmfPeriodoId() {
		return smfPeriodoId;
	}


	public void setSmfPeriodoId(Integer smfPeriodoId) {
		this.smfPeriodoId = smfPeriodoId;
	}


	public List<PeriodoAcademicoDto> getSmfListPeriodoAcademicoDto() {
		return smfListPeriodoAcademicoDto;
	}


	public void setSmfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> smfListPeriodoAcademicoDto) {
		this.smfListPeriodoAcademicoDto = smfListPeriodoAcademicoDto;
	}



	public Date getSmfFechaBusqueda() {
		return smfFechaBusqueda;
	}


	public void setSmfFechaBusqueda(Date smfFechaBusqueda) {
		this.smfFechaBusqueda = smfFechaBusqueda;
	}


	public Integer getSmfCarreraId() {
		return smfCarreraId;
	}


	public void setSmfCarreraId(Integer smfCarreraId) {
		this.smfCarreraId = smfCarreraId;
	}


	public List<CarreraDto> getSmfListCarreraDto() {
		return smfListCarreraDto;
	}


	public void setSmfListCarreraDto(List<CarreraDto> smfListCarreraDto) {
		this.smfListCarreraDto = smfListCarreraDto;
	}


	public List<EstudianteJdbcDto> getSmfListEstudianteJdbcDto() {
		return smfListEstudianteJdbcDto;
	}


	public void setSmfListEstudianteJdbcDto(List<EstudianteJdbcDto> smfListEstudianteJdbcDto) {
		this.smfListEstudianteJdbcDto = smfListEstudianteJdbcDto;
	}


	public EstudianteJdbcDto getSmfEstudianteJdbcDto() {
		return smfEstudianteJdbcDto;
	}


	public void setSmfEstudianteJdbcDto(EstudianteJdbcDto smfEstudianteJdbcDto) {
		this.smfEstudianteJdbcDto = smfEstudianteJdbcDto;
	}

	

	
}
