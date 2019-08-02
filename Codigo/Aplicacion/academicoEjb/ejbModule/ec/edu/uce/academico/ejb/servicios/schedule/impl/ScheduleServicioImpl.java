/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     CamelScheduleServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar los procesos del schedule. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 29-04-2017       	 Daniel Albuja                          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.schedule.impl;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
/**
 * Clase (Implementacion) CamelScheduleServicioImpl.
 * Bean sin estado encargado de gestionar los procesos del schedule.
 * @author dalbuja.
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

import ec.edu.uce.academico.ejb.dtos.ComprobanteCSVDto;
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.servicios.hilos.ConsultaBean;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RecordEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobanteCSVDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobantePagoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPregradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.RecordEstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.schedule.interfaces.ScheduleServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CalificacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ComprobantePagoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ConstantesSchedule;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.CalificacionModulo;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.mailDto.dto.MailDto;
import ec.edu.uce.mailDto.productor.ProductorMailJson;
import ec.edu.uce.mailDto.utilitarios.MailDtoConstantes;

@Singleton
@Startup
public class ScheduleServicioImpl implements ScheduleServicio{
	
	@EJB
	private ComprobantePagoServicio srvComprobanteServicio;

	@EJB
	private FichaMatriculaServicio srvFichaMatriculaServicio;
	@EJB
	private ComprobantePagoServicioJdbc srvComprobanteServicioJdbc;
	@EJB
	private ComprobanteCSVDtoServicioJdbc srvComprobanteCSVDtoServicioJdbc;
	
	@EJB
	private PeriodoAcademicoServicio srvPeriodoAcademicoServicio;
	
	@EJB
	private CronogramaActividadDtoServicioJdbc srvCronogramaActividadDtoServicioJdbc;
	@EJB
	private CarreraDtoServicioJdbc servCarreraDtoServicioJdbc;

	@EJB
	private	MateriaDtoServicioJdbc servMateriaDtoServicioJdbc;
	
	@EJB
	private EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	
	@EJB
	private FichaInscripcionServicio servFichaInscripcionServicio;

	@EJB
	private FichaEstudianteServicio servFichaEstudianteServicio;
	
	@EJB
	private CalificacionServicio servCalificacionServicio;
	@EJB
	private MateriaServicio servMateriaServicio;
	@EJB
	private RecordEstudianteServicio servRecordEstudianteServicio;
	@EJB
	private FichaInscripcionDtoServicioJdbc servFichaInscripcionDtoServicioJdbc;
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private DetalleMatriculaServicio servDetalleMatriculaServicio;
	@EJB
	private RecordEstudianteDtoServicioJdbc servRecordEstudianteDtoServicioJdbc;
	@EJB
	private NotasPregradoDtoServicioJdbc servNotasPregradoDtoServicioJdbc;
	@EJB
	private PersonaServicio servPersonaServicio;
	
	@Schedule(second="30", minute="30", hour="15",persistent=false)
	public void procesoEnvio(Timer timer){
		try {
			StringBuilder csv= new StringBuilder();
			List<ComprobanteCSVDto> listaComprobantes=new ArrayList<ComprobanteCSVDto>();
			listaComprobantes=srvComprobanteCSVDtoServicioJdbc.buscarEmitidosGeneracionCSV();
			
			csv.append("numero_comprobante,total_pago,total_facultad,fecha_emision,fecha_caduca,"
					+ "estado,espe_codigo,modalidad,matr_tipo,aplica_gratuidad,pai_codigo,est_cedula,est_nombres,"
					+ "est_apellido_paterno,est_apellido_materno,est_direccion,est_telefono,mail,est_celular,"
					+ "id_arancel,cantidad,valor_pagado,tipo_unidad,proc_sau");
			
			for (ComprobanteCSVDto comprobanteCSVDto : listaComprobantes) {
				csv.append("\n");
				csv.append(comprobanteCSVDto.getCmpaNumComprobante());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaTotalPago());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaTotalFacultad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaFechaEmision());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaFechaCaduca());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaEstado());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaEspeCodigo());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaModalidad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaMatrTipo());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaAplicaGratuidad());csv.append(",");
//				csv.append(comprobanteCSVDto.getCmpaPaiCodigo());csv.append(",");
				csv.append(69);csv.append(",");
				csv.append(comprobanteCSVDto.getPrsIdentificacion());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsNombres());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsPrimerApellido());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsSegundoApellido());csv.append(",");
				String direccion = comprobanteCSVDto.getPrsDireccion().replace(",", " ");
				csv.append(direccion);csv.append(",");
				csv.append(comprobanteCSVDto.getPrsTelefono());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsMailPersonal());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsCelular());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaIdArancel());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaCantidad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaValorPagado());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaTipoUnidad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaProcSau());
			}
			if(listaComprobantes.size()==0){
//				System.out.println("No existen comprobantes");
			}else{
				try {
//					System.out.println("INICIO DE CORREO");
					byte[] adjunto = csv.toString().getBytes();
					//******************************************************************************
					  //************************* ACA INICIA EL ENVIO DE MAIL ************************
					  //******************************************************************************
					  Connection connection = null;
					  Session session = null;
					  MessageProducer producer = null;
					  ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",ConstantesSchedule.NIO_ACTIVEMQ);
					  connection = connectionFactory.createConnection();
					  connection.start();
					  session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					  Destination destino = session.createQueue(ConstantesSchedule.COLA_ACTIVEMQ);
					  // Creamos un productor
					  producer = session.createProducer(destino);
//						
					  //lista de correos a los que se enviara el mail
					  List<String> correosTo = new ArrayList<>();
					  correosTo.add(ComprobantePagoConstantes.CORREO_FINANCIERO);
					  correosTo.add(ComprobantePagoConstantes.CORREO_SYSREC_DTIC);
//					  correosTo.add("jdalbuja@uce.edu.ec");
					  //path de la plantilla del mail
					  ProductorMailJson pmail = null;
						SimpleDateFormat formato = 
								new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
						String fecha = formato.format(new Date());
					  StringBuilder sbCorreo= new StringBuilder();
					  sbCorreo= GeneralesUtilidades.generarAsuntoCSVComprobantePago(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
							  ComprobantePagoConstantes.USUARIO_CORREO_FINANCIERO);
								pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,"ARCHIVO CSV SIIU",
										sbCorreo.toString()
										, "admin", "dt1c201s", true, adjunto, "Comprobante."+MailDtoConstantes.TIPO_CSV, MailDtoConstantes.TIPO_CSV );
					String jsonSt = pmail.generarMail();
					Gson json = new Gson();
					MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
					// 	Iniciamos el envío de mensajes
					ObjectMessage message = session.createObjectMessage(mailDto);
					producer.send(message);
					//Cambiamos de estado a los comprobantes
					for (ComprobanteCSVDto comprobanteCSVDto : listaComprobantes) {
						try {
							srvComprobanteServicio.editarEstadoMigrado(comprobanteCSVDto);
						} catch (Exception e) {
						}
					}
					//******************************************************************************
					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
					//******************************************************************************
//					System.out.println("EXITO AL ENVIO DE CORREO");
				} catch (Exception e) {
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Schedule(second="00", minute="00", hour="01",  persistent=false)
	public void procesoCambioFichaInscripcionMatricula(Timer timer){
		
		try {
			//Buscamos si existe un periodo en cierre
			PeriodoAcademico pracCierre = srvPeriodoAcademicoServicio.buscarPeriodoEnCierre();
			if(pracCierre!=null){
				// AQUI SE DEBE CREAR LO NECESARIO PARA PROMOVER A LOS ESTUDIANTES QUE YA APROBARON SUS MATERIAS
				
				
				//Si existe el período en cierre, buscamos la fecha de inicio de clases del nuevo periodo
				CronogramaActividadJdbcDto cracAux = new CronogramaActividadJdbcDto();
				cracAux = srvCronogramaActividadDtoServicioJdbc.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoSchedule(CronogramaConstantes.TIPO_ACADEMICO_VALUE,ProcesoFlujoConstantes.PROCESO_INICIO_CLASES_VALUE);
				//Sumamos 30 días a la fecha de inicio
				Calendar cal = Calendar.getInstance();
				cal.setTime(cracAux.getPlcrFechaInicio());
				cal.add(Calendar.DAY_OF_WEEK, 30);
				cracAux.setPlcrFechaInicio( new Timestamp(cal.getTime().getTime()));
				
				//Transformamos a tipo Date
				Date fechaInicioClasesNuevoPeriodo = new Date(cracAux.getPlcrFechaInicio().getTime());
				Date fechaActual = new Date();
				//Preguntamos si la fecha actual es mayor a la fecha de inicio de clases del nuevo período mas 30 días
				if(fechaActual.after(fechaInicioClasesNuevoPeriodo)){
					//Desactivamos el período en cierre
					srvPeriodoAcademicoServicio.desactivarPeriodoEnCierre(pracCierre);
				}
			}
		} catch (Exception e) {
		}
	}
	
	
	
	@Override
	//Pruebas
//	@Schedule(second="00", minute="*/02", hour="*" , persistent=false)
	//Produccion
//	@Schedule(second="00", minute="00", hour="05",  dayOfMonth="10" , month="10" , year="2017",persistent=false)
	@Schedule(second="00", minute="00", hour="01" , persistent=false)
	public void procesoCorrecionRecordEstudiante(Timer timer){
		List<ComprobantePago> listaComprobantes = new ArrayList<ComprobantePago>();
		try {
			listaComprobantes = srvComprobanteServicio.buscarCmpaPendientePagoNivelacionPosgrado();
			for (ComprobantePago itemComprobantePago : listaComprobantes) {
				try {
					List<ComprobantePagoDto> lista = new ArrayList<ComprobantePagoDto>();
					lista = srvComprobanteServicioJdbc.buscarComprobantesDePagoPagados(itemComprobantePago.getCmpaNumComprobante());
					if(lista.size()>0){
						List<DetalleMatricula> listaDetalleMatricula = new ArrayList<DetalleMatricula>();
						listaDetalleMatricula = servDetalleMatriculaServicio.listarXCmpaId(itemComprobantePago.getCmpaId());
						for (DetalleMatricula itemDetalleMatricula : listaDetalleMatricula) {
							FichaMatricula fcmtAux = new FichaMatricula();
							fcmtAux = srvFichaMatriculaServicio.buscarPorId(itemComprobantePago.getCmpaFichaMatricula().getFcmtId());
							FichaEstudiante fcesAux = new FichaEstudiante();
							fcesAux = servFichaEstudianteServicio.buscarPorId(fcmtAux.getFcmtFichaEstudiante().getFcesId());
							servRecordEstudianteServicio.actualizaMatriculadoPorFcesIdPorMlCrPrId(fcesAux.getFcesId(),itemDetalleMatricula.getDtmtMallaCurricularParalelo().getMlcrprId());
						}
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}
	
	@Schedule(second="0", minute="11", hour="09", dayOfMonth="30,31", month="07", year="2019", persistent=false)
//	@Schedule(second="00", minute="*/1", hour="23", dayOfMonth="19", month="07", year="2019", persistent=false)
	public void procesoCorreccionEstadosNotasPeriodoEnCierre(Timer timer){
		System.out.println("INICIO CORRECCION");
		List<Calificacion> listaEstudiantes = new ArrayList<Calificacion>();
		try {
//			listaEstudiantes = servCalificacionServicio.buscarCalificacionCorrecionIdiomas();
//			for (Calificacion item : listaEstudiantes) {
//				servCalificacionServicio.guardarCorreccionFullIdiomas(item.getClfId());
//			}
//			listaEstudiantes = servCalificacionServicio.buscarCalificacionCorrecionCulturaFisica();
//			for (Calificacion item : listaEstudiantes) {
//				servCalificacionServicio.correcionNivelacionError();
//				servFichaInscripcionDtoServicioJdbc.correcionFichaErrorCargaNivelacion();
//			}
//			servCalificacionServicio.eliminarCalificacionModulo(null);
			
			listaEstudiantes = servCalificacionServicio.buscarCalificacionCorrecionIndividual();
			if(listaEstudiantes.size()>0){
				DatabaseMetaData dbmd = servPersonaServicio.retornarDs();
				Runnable r;
				try {
					r = new ConsultaBean(listaEstudiantes,dbmd.getConnection());
					new Thread(r).start();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
//			for (Calificacion item : listaEstudiantes) {
//				try {
//					servCalificacionServicio.guardarCorreccionFull(item.getClfId());
//					System.out.println(item.getClfId());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			servNotasPregradoDtoServicioJdbc.correccionRecordEstudianteModulares();
		} catch (Exception e) {
		}
		System.out.println("FIN"); 
		
	}
	
	
	
//	@Schedule(second="30", minute="34", hour="23", dayOfMonth="04", month="07", year="2019", persistent=false)
//	@Schedule(second="00", minute="*/10", hour="04,07,08,09,10,11,12,13,16,19,22,23",  persistent=false)
	public void procesoCorreccionModulares(Timer timer){
		List<MateriaDto> listaEstudiantes = new ArrayList<MateriaDto>();
//		System.out.println("sdfsdfdfsdfd");
		try {
			//Buscamos los estudiantes con materias modulares con periodo en cierre
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesXmatriculaXperiodoEnCierreXcarreraXMateriaModular();
			List<MateriaDto> listaEstudiantesModulos = new ArrayList<MateriaDto>(); 
			
			for (MateriaDto item : listaEstudiantes) {
				System.out.println(item.getPrsIdentificacion());
				try {
					List<MateriaDto> listaMateriasModulares = new ArrayList<MateriaDto>();
					listaMateriasModulares = servMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoEnCierreXcarreraXMateriaModular(item.getPrsIdentificacion(), item.getRcesId(),630);
					if(listaMateriasModulares!=null && listaMateriasModulares.size()!=0){
						System.out.println("sssss");
						servCalificacionServicio.guardarCorreccionModularesFull(listaMateriasModulares);
//						
////						List<Materia> listaModulos = new ArrayList<>();
////						
////						listaModulos = servMateriaServicio.listarTodosModulos(listaMateriasModulares.get(0).getMtrId());
////						
////						BigDecimal notaSuma = BigDecimal.ZERO;
////						if(listaMateriasModulares.size()==listaModulos.size()){
////							boolean op=false;
////							for (MateriaDto materiaDto : listaMateriasModulares) {
////								BigDecimal sumaP1P2 = BigDecimal.ZERO;
////								sumaP1P2=(new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2())).setScale(2, RoundingMode.HALF_UP);
////								notaSuma=notaSuma.add(sumaP1P2);
////								System.out.println(materiaDto.getPrsIdentificacion());
////								System.out.println(materiaDto.getMtrDescripcion());
////								System.out.println(materiaDto.getClfSumaP1P2());
////								System.out.println(materiaDto.getClfSupletorio());
////								System.out.println(sumaP1P2);
////							}
////							
////						}
////						
//						
////						List<Materia> listaModulos = new ArrayList<>();
////						listaModulos = servMateriaServicio.listarTodosModulos(item.getMtrId());
////						if(listaMateriasModulares.size()==listaModulos.size()){
////							BigDecimal asistenciaSuma = BigDecimal.ZERO;
////							BigDecimal asistenciaSumaP1P2 = BigDecimal.ZERO;
////							BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
////							BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
////							BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
////							BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
////							BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
////							BigDecimal notEstudiante2 = BigDecimal.ZERO;
////							BigDecimal notEstudiante1 = BigDecimal.ZERO;
////							BigDecimal notaSuma = BigDecimal.ZERO;
////							BigDecimal supletorio = BigDecimal.ZERO;
////							BigDecimal param1 = BigDecimal.ZERO;
////							BigDecimal param2 = BigDecimal.ZERO;
////							
////							try {
////								boolean op=false;
////								for (MateriaDto materiaDto : listaMateriasModulares) {
////									BigDecimal sumaP1P2 = BigDecimal.ZERO;
////									asistenciaSumaP1P2=asistenciaSumaP1P2.add(new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2()));
////									
////									sumaP1P2=(new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2()));
////									int comparador = sumaP1P2.compareTo(new BigDecimal(27.5));
////									if(comparador==0 || comparador==1){
////										try {
////											notaSuma=notaSuma.add(new BigDecimal(materiaDto.getClfNotaFinalSemestre()).setScale(2, RoundingMode.DOWN));	
////										} catch (Exception e) {
////											notaSuma=notaSuma.add(new BigDecimal(materiaDto.getClfSumaP1P2()).setScale(2, RoundingMode.DOWN));
////										}	
////									}else {
////										comparador = sumaP1P2.compareTo(new BigDecimal(8.8));
////										if(comparador==0 || comparador==1){
////											param1=new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2()).multiply(new BigDecimal(0.4)).setScale(2, RoundingMode.DOWN);
////											if(materiaDto.getClfSupletorio()!=-99){
////												supletorio = new BigDecimal(materiaDto.getClfSupletorio()).setScale(2, RoundingMode.HALF_UP);
////											}
////											param2=supletorio.multiply(new BigDecimal(1.2)).setScale(2, RoundingMode.DOWN);
////											notaSuma=notaSuma.add(param1.add(param2));
////										}else{
////											op=true;
////										}	
////									}
////									
////									
////									
////									
////									asistenciaSuma=asistenciaSuma.add(new BigDecimal(materiaDto.getClfAsistenciaTotal()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
////									try {
////										asistenciaSumaDocente =asistenciaSumaDocente.add(new BigDecimal(materiaDto.getClfAsistenciaTotalDoc()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);								
////									} catch (Exception e) {
////									}
////
////									asistenciaDocente1=asistenciaDocente1.add(new BigDecimal(materiaDto.getClfAsistenciaDocente1()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
////									asistenciaDocente2=asistenciaDocente2.add(new BigDecimal(materiaDto.getClfAsistenciaDocente2()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
////									asistenciaEstudiante1=asistenciaEstudiante1.add(new BigDecimal(materiaDto.getClfAsistencia1()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
////									asistenciaEstudiante2=asistenciaEstudiante2.add(new BigDecimal(materiaDto.getClfAsistencia2()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
////									notEstudiante1=notEstudiante1.add(new BigDecimal(materiaDto.getClfNota1()));
////									notEstudiante2=notEstudiante2.add(new BigDecimal(materiaDto.getClfNota2()));
////								}
////								notEstudiante1=notEstudiante1.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
////								notEstudiante2=notEstudiante2.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
////								Calificacion clfAux = new Calificacion();
////								clfAux.setClfAsistencia1(asistenciaEstudiante1.floatValue());
////								clfAux.setClfAsistencia2(asistenciaEstudiante2.floatValue());
////								clfAux.setClfAsistenciaDocente1(asistenciaDocente1.floatValue());
////								clfAux.setClfAsistenciaDocente2(asistenciaDocente2.floatValue());
////								clfAux.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
////								clfAux.setClfAsistenciaTotal(asistenciaSuma.floatValue());
////								clfAux.setClfNota1(notEstudiante1.floatValue());
////								clfAux.setClfNota2(notEstudiante2.floatValue());
////								notaSuma=notaSuma.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
////								clfAux.setClfNotaFinalSemestre(notaSuma.floatValue());
////								RecordEstudiante rcesAux = servRecordEstudianteServicio.buscarPorId(item.getRcesId());
////								clfAux.setRecordEstudiante(rcesAux);
////								servCalificacionServicio.guardarCorreccion(clfAux);	
////							} catch (Exception e) {
////							}
////						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	//PRUEBAS
//		@Schedule(second="0", minute="*/05", hour="*", persistent=false)
		
	////PRODUCCION
//	@Schedule(second="00", minute="51", hour="12", dayOfMonth="13", month="04", year="2019", persistent=false)
//	@Schedule(second="0", minute="*/1" , hour="01-18", persistent=false)
	public void procesoCambioEstadoNIvelacion(Timer timer){
		try {	
//				CronogramaActividadJdbcDto cracAuxInicio = new CronogramaActividadJdbcDto();
//				cracAuxInicio = srvCronogramaActividadDtoServicioJdbc.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
//				CronogramaActividadJdbcDto cracAuxFin = new CronogramaActividadJdbcDto();
//				cracAuxFin = srvCronogramaActividadDtoServicioJdbc.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
				List<EstudianteJdbcDto>  listaEstudianteNivelacion = new ArrayList<EstudianteJdbcDto>();
//				List<MateriaDto> fgmfListMateriasEstado = new ArrayList<MateriaDto>();
//				List<EstudianteJdbcDto> listaAprobados = new ArrayList<EstudianteJdbcDto>();
				List<EstudianteJdbcDto> listaReprobados = new ArrayList<EstudianteJdbcDto>();
				//Transformamos a tipo Date
//				Date fechaInicioMatriculasOrdinarias = new Date(cracAuxInicio.getPlcrFechaInicio().getTime());
//				Date fechaFinMatriculasExtraordinaria = new Date(cracAuxFin.getPlcrFechaFin().getTime());
//				Date fechaActual = new Date();
//				if(fechaActual.after(fechaInicioMatriculasOrdinarias) && fechaActual.before(fechaFinMatriculasExtraordinaria)){
					//lita estudiante nivelacion
					listaEstudianteNivelacion = servEstudianteDtoServicioJdbc.buscarEstudianteReprobadoNivelacion("-99");
//					for (EstudianteJdbcDto item : listaEstudianteNivelacion) {
//						if(item.getPrsIdentificacion().equals("1727020818"))
//							System.out.println("siiiiiiii encontrado");
//					}
//					Integer op = 0;
//					List<EstudianteJdbcDto> apnfListMateriasEstado = new ArrayList<EstudianteJdbcDto>();
					for (EstudianteJdbcDto est : listaEstudianteNivelacion) {
					System.out.println(est.getPrsIdentificacion());
					}
//						try {
//							op = 0;
//							//materias del estudiante
//							try {
//								
//								apnfListMateriasEstado = servMateriaDtoServicioJdbc.listarCalificacionesEstudianteNivelacion(est.getPrsIdentificacion());
//							
//							} catch (MateriaDtoNoEncontradoException e) {
//							}
//							EstudianteJdbcDto estudiante = new EstudianteJdbcDto();
//							if(apnfListMateriasEstado.size()>0){
//								for (EstudianteJdbcDto itemMtr : apnfListMateriasEstado) {
//									estudiante = itemMtr;
//									if(itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
//										op =  1;
//										listaReprobados.add(itemMtr);
//									}else if(itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE||itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE||itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE){
//										op =  2;
//										listaReprobados.add(itemMtr);
//									}
//								}
//								if(op==0 ){
//									boolean op1=true;
//									for (EstudianteJdbcDto item : listaAprobados) {
//										if(item.getPrsIdentificacion().equals(estudiante.getPrsIdentificacion())){
//											op1=false;
//										}
//									}
//									if(op1){
//										listaAprobados.add(estudiante);	
//									}
//									
//								}
//								else if(op==1){
//									listaReprobados.add(estudiante);
//								}
////								else{
////									servFichaInscripcionServicio.modificarMatriculaPendientePasoNotas(est.getFcinId());
////								}
//							}
//							try {
////								if(listaReprobados.size()>0){
////									servFichaInscripcionServicio.activarMatriculaReprobadosNivelacion(listaReprobados);	
////								}
//								List<Persona> listaPersona = new ArrayList<Persona>();
//								if(listaAprobados.size()>0){
//									for (EstudianteJdbcDto item : listaAprobados) {
//										Persona prs = servPersonaServicio.buscarPersonaPorIdentificacion(item.getPrsIdentificacion());
//										listaPersona.add(prs);
//									}
//									servFichaInscripcionServicio.pormoverNivelacionGrado(listaPersona);
//								}
//								
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							
//							
////							else{
//								servFichaInscripcionServicio.desactivarFichaInscripcion(est.getFcinId());
////							}
//					} catch (Exception e) {
//					}
//						
						
						
						
//						try {
//								op = 0;
//								//materias del estudiante
//								try {
//									
//									fgmfListMateriasEstado = servMateriaDtoServicioJdbc.listarNotasEstudianteNivelacion(est.getPrsIdentificacion());
//								
//								} catch (MateriaDtoNoEncontradoException e) {
//								}
//								if(fgmfListMateriasEstado.size()>0){
//									for (MateriaDto itemMtr : fgmfListMateriasEstado) {
//										if(itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
//											op =  1;
//										}else if(itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE||itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE||itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE){
//											op =  2;
//										}
//									}
//									if(op==0){
//										listaAprobados.add(est);
//									}
//									else if(op==1){
//										listaReprobados.add(est);
//									}else{
//										servFichaInscripcionServicio.modificarMatriculaPendientePasoNotas(est.getFcinId());
//									}
//								}else{
//									servFichaInscripcionServicio.desactivarFichaInscripcion(est.getFcinId());
//									System.out.println(est.getPrsIdentificacion());
////								}
//						} catch (Exception e) {
//						}
//					}
//					try {
//						if(listaReprobados.size()>0){
//							servFichaInscripcionServicio.activarMatriculaReprobadosNivelacion(listaEstudianteNivelacion);	
//							System.out.println(listaReprobados.size());
//						}
////						if(listaAprobados.size()>0){
//////							servFichaInscripcionServicio.pormoverNivelacionGrado(listaAprobados);
////						}
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
						
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Schedule(second="30", minute="40", hour="12",  persistent=false)
	public void procesoCorreccionEmergente(Timer timer){
//		System.out.println("INICIO MODULARES");
		List<MateriaDto> listaEstudiantes = new ArrayList<MateriaDto>();
		try {
			//Buscamos los estudiantes con materias modulares con periodo en cierre
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesXmatriculaXperiodoAnteriorXcarreraXMateriaModular();
			List<MateriaDto> listaEstudiantesModulos = new ArrayList<MateriaDto>(); 
			
			for (MateriaDto item : listaEstudiantes) {
				
				try {
					List<MateriaDto> listaMateriasModulares = new ArrayList<MateriaDto>();
					listaMateriasModulares = servMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoEIdXcarreraXMateriaModular(item.getPrsIdentificacion(), item.getRcesId());
					if(listaMateriasModulares!=null && listaMateriasModulares.size()!=0){
						
						servCalificacionServicio.guardarCorreccionModularesFull(listaMateriasModulares);
						
//						List<Materia> listaModulos = new ArrayList<>();
//						
//						listaModulos = servMateriaServicio.listarTodosModulos(listaMateriasModulares.get(0).getMtrId());
//						
//						BigDecimal notaSuma = BigDecimal.ZERO;
//						if(listaMateriasModulares.size()==listaModulos.size()){
//							boolean op=false;
//							for (MateriaDto materiaDto : listaMateriasModulares) {
//								BigDecimal sumaP1P2 = BigDecimal.ZERO;
//								sumaP1P2=(new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2())).setScale(2, RoundingMode.HALF_UP);
//								notaSuma=notaSuma.add(sumaP1P2);
//								System.out.println(materiaDto.getPrsIdentificacion());
//								System.out.println(materiaDto.getMtrDescripcion());
//								System.out.println(materiaDto.getClfSumaP1P2());
//								System.out.println(materiaDto.getClfSupletorio());
//								System.out.println(sumaP1P2);
//							}
//							
//						}
//						
						
//						List<Materia> listaModulos = new ArrayList<>();
//						listaModulos = servMateriaServicio.listarTodosModulos(item.getMtrId());
//						if(listaMateriasModulares.size()==listaModulos.size()){
//							BigDecimal asistenciaSuma = BigDecimal.ZERO;
//							BigDecimal asistenciaSumaP1P2 = BigDecimal.ZERO;
//							BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
//							BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
//							BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
//							BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
//							BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
//							BigDecimal notEstudiante2 = BigDecimal.ZERO;
//							BigDecimal notEstudiante1 = BigDecimal.ZERO;
//							BigDecimal notaSuma = BigDecimal.ZERO;
//							BigDecimal supletorio = BigDecimal.ZERO;
//							BigDecimal param1 = BigDecimal.ZERO;
//							BigDecimal param2 = BigDecimal.ZERO;
//							
//							try {
//								boolean op=false;
//								for (MateriaDto materiaDto : listaMateriasModulares) {
//									BigDecimal sumaP1P2 = BigDecimal.ZERO;
//									asistenciaSumaP1P2=asistenciaSumaP1P2.add(new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2()));
//									
//									sumaP1P2=(new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2()));
//									int comparador = sumaP1P2.compareTo(new BigDecimal(27.5));
//									if(comparador==0 || comparador==1){
//										try {
//											notaSuma=notaSuma.add(new BigDecimal(materiaDto.getClfNotaFinalSemestre()).setScale(2, RoundingMode.DOWN));	
//										} catch (Exception e) {
//											notaSuma=notaSuma.add(new BigDecimal(materiaDto.getClfSumaP1P2()).setScale(2, RoundingMode.DOWN));
//										}	
//									}else {
//										comparador = sumaP1P2.compareTo(new BigDecimal(8.8));
//										if(comparador==0 || comparador==1){
//											param1=new BigDecimal(materiaDto.getClfNota1()+materiaDto.getClfNota2()).multiply(new BigDecimal(0.4)).setScale(2, RoundingMode.DOWN);
//											if(materiaDto.getClfSupletorio()!=-99){
//												supletorio = new BigDecimal(materiaDto.getClfSupletorio()).setScale(2, RoundingMode.HALF_UP);
//											}
//											param2=supletorio.multiply(new BigDecimal(1.2)).setScale(2, RoundingMode.DOWN);
//											notaSuma=notaSuma.add(param1.add(param2));
//										}else{
//											op=true;
//										}	
//									}
//									
//									
//									
//									
//									asistenciaSuma=asistenciaSuma.add(new BigDecimal(materiaDto.getClfAsistenciaTotal()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
//									try {
//										asistenciaSumaDocente =asistenciaSumaDocente.add(new BigDecimal(materiaDto.getClfAsistenciaTotalDoc()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);								
//									} catch (Exception e) {
//									}
//
//									asistenciaDocente1=asistenciaDocente1.add(new BigDecimal(materiaDto.getClfAsistenciaDocente1()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
//									asistenciaDocente2=asistenciaDocente2.add(new BigDecimal(materiaDto.getClfAsistenciaDocente2()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
//									asistenciaEstudiante1=asistenciaEstudiante1.add(new BigDecimal(materiaDto.getClfAsistencia1()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
//									asistenciaEstudiante2=asistenciaEstudiante2.add(new BigDecimal(materiaDto.getClfAsistencia2()).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);;
//									notEstudiante1=notEstudiante1.add(new BigDecimal(materiaDto.getClfNota1()));
//									notEstudiante2=notEstudiante2.add(new BigDecimal(materiaDto.getClfNota2()));
//								}
//								notEstudiante1=notEstudiante1.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
//								notEstudiante2=notEstudiante2.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
//								Calificacion clfAux = new Calificacion();
//								clfAux.setClfAsistencia1(asistenciaEstudiante1.floatValue());
//								clfAux.setClfAsistencia2(asistenciaEstudiante2.floatValue());
//								clfAux.setClfAsistenciaDocente1(asistenciaDocente1.floatValue());
//								clfAux.setClfAsistenciaDocente2(asistenciaDocente2.floatValue());
//								clfAux.setClfAsistenciaTotalDoc(asistenciaSumaDocente.floatValue());
//								clfAux.setClfAsistenciaTotal(asistenciaSuma.floatValue());
//								clfAux.setClfNota1(notEstudiante1.floatValue());
//								clfAux.setClfNota2(notEstudiante2.floatValue());
//								notaSuma=notaSuma.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
//								clfAux.setClfNotaFinalSemestre(notaSuma.floatValue());
//								RecordEstudiante rcesAux = servRecordEstudianteServicio.buscarPorId(item.getRcesId());
//								clfAux.setRecordEstudiante(rcesAux);
//								servCalificacionServicio.guardarCorreccion(clfAux);	
//							} catch (Exception e) {
//							}
//						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
//		System.out.println("FIN");
	}
	

//	@Schedule(second="00", minute="50", hour="19",  persistent=false)
//	@Schedule(second="00", minute="00,02,04,06", hour="19",  persistent=false)
	public void procesoBorradoDuplicadosModulares(Timer timer){
		List<MateriaDto> listaEstudiantes = new ArrayList<MateriaDto>();
		try {
			// Buscamos los estudiantes que tienen duplicados en el registro de calificacion_modulo
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesRegistrosDuplicadosModulares();
			for (int i=0;i<listaEstudiantes.size()-1;i++) {
				lazo:for (int j=i+1;j<listaEstudiantes.size();j++) {
					if(listaEstudiantes.get(i).getRcesId()==listaEstudiantes.get(j).getRcesId()){
						if(listaEstudiantes.get(i).getMlcrprId()==listaEstudiantes.get(j).getMlcrprId()){
							// Si existe el mismo rces_id con el mismo mlcrpr_id_modulo se elimina el más antiguo
							servCalificacionServicio.eliminarCalificacionModulo(listaEstudiantes.get(i).getClfId());
							break lazo;
						}
					}else{
						break lazo;
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	
	@Schedule(second="00", minute="43", hour="08", dayOfMonth="13", month="06", year="2019", persistent=false)
	public void procesoBorradoDuplicadosXRecordEstudiante(Timer timer){
		List<MateriaDto> listaEstudiantes = new ArrayList<MateriaDto>();
		try {
			System.out.println("sdfsdfsdfsdfsdfsdd");
			List<String> lista = new ArrayList<String>();
//			while (true){
				listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesRegistrosDuplicados();
				if(listaEstudiantes.size()==0){

				}else{
					for (int i=0;i<listaEstudiantes.size()-1;i++) {
//						System.out.println(listaEstudiantes.get(i).getClfId());
						lazo:for (int j=i+1;j<listaEstudiantes.size();j++) {
							if(listaEstudiantes.get(i).getRcesId()==listaEstudiantes.get(j).getRcesId()){
								if(listaEstudiantes.get(i).getMlcrprId()==listaEstudiantes.get(j).getMlcrprId()){
									// Si existe el mismo rces_id con el mismo mlcrpr_ido se elimina el más antiguo
//									Calificacion clf1 = servCalificacionServicio.buscarPorId(listaEstudiantes.get(j).getClfId());
//									Calificacion clf2 = servCalificacionServicio.buscarPorId(listaEstudiantes.get(i).getClfId());
//									try {
//										if(clf1.getClfNota1()!=null && clf1.getClfNota2()!=null
//												&& clf2.getClfNota1()!=null && clf2.getClfNota2()!=null){
//											servCalificacionServicio.eliminarCalificacion(listaEstudiantes.get(j).getClfId());
//											break lazo;
//										}
//									} catch (Exception e) {
//									}
//									try {
//										if(clf2.getClfNota1()!=null && clf2.getClfNota2()!=null){
//											servCalificacionServicio.eliminarCalificacion(listaEstudiantes.get(j).getClfId());
//											break lazo;
//										}
//									} catch (Exception e) {
//									}
									
//									System.out.println(listaEstudiantes.get(i).getRcesId());
//									servMateriaDtoServicioJdbc.eliminarDuplicados(listaEstudiantes.get(j).getClfId());	
									lista.add(String.valueOf(listaEstudiantes.get(j).getClfId()));
//									break lazo;
								}
//								break lazo;
							}else{
								break lazo;
							}
						}
					}
				}
				StringBuilder sb = new StringBuilder();
				  for(int i=0; i<lista.size(); i++){
				            sb.append(lista.get(i)+",");
				    }
				WriteToFile(sb.toString()," ");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			servCalificacionServicio.eliminarCalificacionXId();
//		} catch (CalificacionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		List<MateriaDto> listaEstudiantesRecord = new ArrayList<MateriaDto>();
//		try {
//			// Buscamos los estudiantes que tienen duplicados en el registro de calificacion
//			listaEstudiantesRecord = servMateriaDtoServicioJdbc.listadoCorrecionEstado();
//			for (int i=0;i<listaEstudiantesRecord.size()-1;i++) {
//				Calificacion clfAux = new Calificacion();
//				clfAux = servCalificacionServicio.buscarPorId(listaEstudiantesRecord.get(i).getClfId());
//				if(clfAux.getClfPromedioAsistencia()>=80){
//					if(listaEstudiantesRecord.get(i).getClfNotaFinalSemestre()>=27.5){
//						servRecordEstudianteServicio.actualizaEstadoRces(listaEstudiantesRecord.get(i).getRcesId(), RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
//					}else if(listaEstudiantesRecord.get(i).getClfNotaFinalSemestre()>=8.8){
//						if(clfAux.getClfParamRecuperacion2()!=null){
//							servRecordEstudianteServicio.actualizaEstadoRces(listaEstudiantesRecord.get(i).getRcesId(), RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//						}else{
//							servRecordEstudianteServicio.actualizaEstadoRces(listaEstudiantesRecord.get(i).getRcesId(), RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE);
//						}
//					}else{
//						servRecordEstudianteServicio.actualizaEstadoRces(listaEstudiantesRecord.get(i).getRcesId(), RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//					}
//				}else{
//					servRecordEstudianteServicio.actualizaEstadoRces(listaEstudiantesRecord.get(i).getRcesId(), RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
//				}
//			}
//		} catch (Exception e) {
//		}
	}
	
    public static void WriteToFile(String fileContent, String fileName) throws IOException {
//        String projectPath = System.getProperty("user.dir");
//        String tempFile = projectPath + File.separator + fileName;
        File file = new File("d:\\archivos\\revision.csv");
        // if file does exists, then delete and create a new file
        if (file.exists()) {
            try {
                File newFileName = new File("d:\\archivos\\revision.csv");
                file.renameTo(newFileName);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(fileContent);
        System.out.println(fileContent);
        bw.close();

    }
	
	
//	@Schedule(second="00", minute="*/2" , hour="*", persistent=false)
	public void procesoCambioRecuperacion(Timer timer){
		try{
			List<RecordEstudianteDto> listaEstudiantes = new ArrayList<RecordEstudianteDto>();
			listaEstudiantes =  servRecordEstudianteDtoServicioJdbc.listarRecordEstudianteRecuperacion();
			for (RecordEstudianteDto item : listaEstudiantes) {
				BigDecimal suma = item.getClfParamRecuperacion1().add(item.getClfParamRecuperacion2());
				int comparador = suma.compareTo(new BigDecimal(CalificacionConstantes.NOTA_APROBACION_MATERIA_VALUE));;
				
				if(comparador == 1 || comparador ==0){
					item.setRcesEstado(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE);
					servRecordEstudianteServicio.actualizaEstadoRces(item.getRcesId(), item.getRcesEstado());
				}else{
					item.setRcesEstado(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE);
					servRecordEstudianteServicio.actualizaEstadoRces(item.getRcesId(), item.getRcesEstado());
				}
			}
		} catch (Exception e) {
		}
		
	}
	
	

	
	
	
//	@Schedule(second="00", minute="00", hour="20", dayOfMonth="18", month="09", year="2018", persistent=false)
	public void procesoActualizarNotasModulares(Timer timer){
		System.out.println("CORREGIR");
		List<MateriaDto> listaEstudiantes = new ArrayList<MateriaDto>();
		try {
			// Buscamos los estudiantes que tienen edición de notas en cualquier hemi
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesRegistrosEditadosModulares();
			for (MateriaDto item : listaEstudiantes) {
					BigDecimal sumaParciales = BigDecimal.ZERO;
					sumaParciales = (new BigDecimal (item.getClfNota1()).setScale(2, RoundingMode.DOWN).add(new BigDecimal(item.getClfNota2()).setScale(2, RoundingMode.DOWN)));
					int com = sumaParciales.compareTo(new BigDecimal(27.5));
					if(com == 1 || com == 0){
						
							item.setClfNotaFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
							
					}else{
						BigDecimal parametro2Aux  = BigDecimal.ZERO;
						parametro2Aux  = (new BigDecimal(item.getClfSupletorio()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP));
						BigDecimal parametro1Aux  = BigDecimal.ZERO;
						parametro1Aux = (new BigDecimal(item.getClfSumaP1P2()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP));
						
						BigDecimal sumaParametros = BigDecimal.ZERO;
						sumaParametros = parametro1Aux.add(parametro2Aux);
						int comparador = sumaParametros.compareTo(new BigDecimal(27.5));
						if(comparador==-1){
							item.setClfNotaFinalSemestre(sumaParametros.floatValue());						
						}else{
							item.setClfNotaFinalSemestre(sumaParametros.setScale(0, RoundingMode.HALF_UP).floatValue());
						}
					}
					servCalificacionServicio.guardarActualizarModulares(item);
				}
		} catch (Exception e) {
		}
		try {
			listaEstudiantes = new ArrayList<MateriaDto>();
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesSinNotaFinal();
			for (MateriaDto item : listaEstudiantes) {
				BigDecimal sumaParciales = BigDecimal.ZERO;
				sumaParciales = (new BigDecimal (item.getClfSumaP1P2()).setScale(2, RoundingMode.DOWN));
				int com = sumaParciales.compareTo(new BigDecimal(27.5));
				if(com == 1 || com == 0){
					
						item.setClfNotaFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
						item.setClfAsistencia1(item.getClfAsistenciaDocente1());
						item.setClfAsistencia2(item.getClfAsistenciaDocente2());
						item.setClfAsistenciaTotal((float)(item.getClfAsistencia1()+item.getClfAsistencia2()));
						item.setClfPromedioAsistencia((float)100);
						
				}else{
					item.setClfNotaFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP).floatValue());
				}
				servCalificacionServicio.guardarActualizarModulares(item);
			}
		} catch (Exception e) {
		}
		
		try {
			listaEstudiantes = new ArrayList<MateriaDto>();
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesProblemasRecuperacion();
			for (MateriaDto item : listaEstudiantes) {
				BigDecimal parametro1Aux  = BigDecimal.ZERO;
				parametro1Aux = (new BigDecimal(item.getClfSumaP1P2()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP));
				item.setClfParamRecuperacion1(parametro1Aux.floatValue());
				item.setClfParamRecuperacion2((float)0);
				item.setClfSupletorio((float)0);
				item.setClfNotaFinalSemestre(parametro1Aux.floatValue());
				servCalificacionServicio.guardarActualizarModulares(item);
			}
		} catch (Exception e) {
		}
		try {
			listaEstudiantes = new ArrayList<MateriaDto>();
			listaEstudiantes = servMateriaDtoServicioJdbc.listadoEstudiantesRecalculoRecuperacion();
			for (MateriaDto item : listaEstudiantes) {
				BigDecimal parametro1Aux  = BigDecimal.ZERO;
				parametro1Aux = (new BigDecimal(item.getClfSumaP1P2()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO1_VALUE)).setScale(2, RoundingMode.HALF_UP));
				item.setClfParamRecuperacion1(parametro1Aux.floatValue());
				BigDecimal parametro2Aux  = BigDecimal.ZERO;
				parametro2Aux=(new BigDecimal(item.getClfSupletorio()).multiply(new BigDecimal(CalificacionConstantes.PONDERACION_PARAMETRO2_VALUE)).setScale(2, RoundingMode.HALF_UP));
				item.setClfParamRecuperacion2(parametro2Aux.floatValue());
				item.setClfNotaFinalSemestre(parametro1Aux.add(parametro2Aux).floatValue());
				servCalificacionServicio.guardarActualizarModulares(item);
			}
		} catch (Exception e) {
		}
	}
	
//	@Schedule(second="00", minute="51", hour="23", dayOfMonth="22", month="02", year="2019", persistent=false)
	public void procesoCorreccionModularesSumaTotal(Timer timer){
		System.out.println("CORREGIR");
		List<CalificacionModulo> listaEstudiantes = new ArrayList<CalificacionModulo>();
		try {
			// Buscamos los estudiantes que tienen edición de notas en cualquier hemi
			listaEstudiantes = servCalificacionServicio.listarTodosModularesCorreccion();
			for (CalificacionModulo item : listaEstudiantes) {
					BigDecimal sumaParciales = BigDecimal.ZERO;
					sumaParciales = (new BigDecimal (item.getClmdNota1()).setScale(2, RoundingMode.DOWN).add(new BigDecimal(item.getClmdNota2()).setScale(2, RoundingMode.DOWN)));
					int com = sumaParciales.compareTo(new BigDecimal(27.5));
					if(com == 1 || com == 0){
						
							item.setClmdNotaFinalSemestre(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
							item.setClmdSumaP1P2(sumaParciales.setScale(0, RoundingMode.HALF_UP).floatValue());
					}else{
						item.setClmdNotaFinalSemestre(sumaParciales.setScale(2, RoundingMode.HALF_UP).floatValue());
						item.setClmdSumaP1P2(sumaParciales.setScale(2, RoundingMode.HALF_UP).floatValue());
					}
					servCalificacionServicio.guardarActualizarModulares(item);
				}
		} catch (Exception e) {
		}
	}
	
	
	
	
	@Schedule(second="00", minute="00", hour="09", dayOfMonth="11", month="06", year="2019", persistent=false)
	public void procesoEnvioEmergente(Timer timer){
		try {
			StringBuilder csv= new StringBuilder();
			List<ComprobanteCSVDto> listaComprobantes=new ArrayList<ComprobanteCSVDto>();
			listaComprobantes=srvComprobanteCSVDtoServicioJdbc.buscarEmitidosGeneracionCSV();
			
			csv.append("numero_comprobante,total_pago,total_facultad,fecha_emision,fecha_caduca,"
					+ "estado,espe_codigo,modalidad,matr_tipo,aplica_gratuidad,pai_codigo,est_cedula,est_nombres,"
					+ "est_apellido_paterno,est_apellido_materno,est_direccion,est_telefono,mail,est_celular,"
					+ "id_arancel,cantidad,valor_pagado,tipo_unidad,proc_sau");
			
			for (ComprobanteCSVDto comprobanteCSVDto : listaComprobantes) {
				csv.append("\n");
				csv.append(comprobanteCSVDto.getCmpaNumComprobante());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaTotalPago());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaTotalFacultad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaFechaEmision());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaFechaCaduca());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaEstado());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaEspeCodigo());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaModalidad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaMatrTipo());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaAplicaGratuidad());csv.append(",");
//				csv.append(comprobanteCSVDto.getCmpaPaiCodigo());csv.append(",");
				csv.append(69);csv.append(",");
				csv.append(comprobanteCSVDto.getPrsIdentificacion());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsNombres());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsPrimerApellido());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsSegundoApellido());csv.append(",");
				String direccion = comprobanteCSVDto.getPrsDireccion().replace(",", " ");
				csv.append(direccion);csv.append(",");
				csv.append(comprobanteCSVDto.getPrsTelefono());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsMailPersonal());csv.append(",");
				csv.append(comprobanteCSVDto.getPrsCelular());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaIdArancel());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaCantidad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaValorPagado());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaTipoUnidad());csv.append(",");
				csv.append(comprobanteCSVDto.getCmpaProcSau());
			}
			if(listaComprobantes.size()==0){
//				System.out.println("No existen comprobantes");
			}else{
				try {
//					System.out.println("INICIO DE CORREO");
					byte[] adjunto = csv.toString().getBytes();
					//******************************************************************************
					  //************************* ACA INICIA EL ENVIO DE MAIL ************************
					  //******************************************************************************
					  Connection connection = null;
					  Session session = null;
					  MessageProducer producer = null;
					  ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",ConstantesSchedule.NIO_ACTIVEMQ);
					  connection = connectionFactory.createConnection();
					  connection.start();
					  session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					  Destination destino = session.createQueue(ConstantesSchedule.COLA_ACTIVEMQ);
					  // Creamos un productor
					  producer = session.createProducer(destino);
//						
					  //lista de correos a los que se enviara el mail
					  List<String> correosTo = new ArrayList<>();
					  correosTo.add(ComprobantePagoConstantes.CORREO_FINANCIERO);
					  correosTo.add(ComprobantePagoConstantes.CORREO_SYSREC_DTIC);
//					  correosTo.add("jdalbuja@uce.edu.ec");
					  //path de la plantilla del mail
					  ProductorMailJson pmail = null;
						SimpleDateFormat formato = 
								new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
						String fecha = formato.format(new Date());
					  StringBuilder sbCorreo= new StringBuilder();
					  sbCorreo= GeneralesUtilidades.generarAsuntoCSVComprobantePago(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
							  ComprobantePagoConstantes.USUARIO_CORREO_FINANCIERO);
								pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,"ARCHIVO CSV SIIU",
										sbCorreo.toString()
										, "admin", "dt1c201s", true, adjunto, "Comprobante."+MailDtoConstantes.TIPO_CSV, MailDtoConstantes.TIPO_CSV );
					String jsonSt = pmail.generarMail();
					Gson json = new Gson();
					MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
					// 	Iniciamos el envío de mensajes
					ObjectMessage message = session.createObjectMessage(mailDto);
					producer.send(message);
					//Cambiamos de estado a los comprobantes
					for (ComprobanteCSVDto comprobanteCSVDto : listaComprobantes) {
						try {
							srvComprobanteServicio.editarEstadoMigrado(comprobanteCSVDto);
						} catch (Exception e) {
						}
					}
					//******************************************************************************
					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
					//******************************************************************************
//					System.out.println("EXITO AL ENVIO DE CORREO");
				} catch (Exception e) {
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void EscribirFicheroCsv(String fichero_a_escribir, ArrayList<String[]> d)
	    throws UnsupportedEncodingException, FileNotFoundException, IOException{
	    OutputStream fout = new ByteArrayOutputStream();
	    OutputStreamWriter out = new OutputStreamWriter(fout, "UTF8");
	    for(int i=0; i<d.size(); i++){
	        String[] fila=d.get(i);
	        for(int j=0; j<fila.length; j++){
	            out.write(fila[j]+",");
	        }
	        out.write("\n");
	    }
	    out.close();
	    fout.close();
	}
	public void EscribirFicheroCsvLista(String fichero_a_escribir, List<String> d)
		    throws UnsupportedEncodingException, FileNotFoundException, IOException{
	    StringBuilder sb = new StringBuilder();

	    // all but last
	    for(int i = 0; i < d.size() - 1 ; i++) {
	        sb.append(d.get(i));
	        sb.append(",");
	    }

	    // last string, no separator
	    if(d.size() > 0){
	        sb.append(d.get(d.size()-1));
	    }

		
		    OutputStream fout = new ByteArrayOutputStream();
		    OutputStreamWriter out = new OutputStreamWriter(fout, "UTF8");
		    for(int i=0; i<d.size(); i++){
//		        String[] fila=d.get(i);
//		        for(int j=0; j<fila.length; j++){
		            out.write(d.get(i)+",");
//		        }
		        out.write("\n");
		    }
		    out.close();
		    fout.close();
		}
	
	

	//PRUEBAS
//	@Schedule(second="0", minute="00", hour="14", persistent=false)
	
	//PRODUCCION
//	@Schedule(second="00", minute="*/30" , hour="8-12", persistent=false)
//	public void procesoAprobadasoSIAC(Timer timer){
//		try {	
//				CronogramaActividadJdbcDto cracAuxInicio = new CronogramaActividadJdbcDto();
//				cracAuxInicio = srvCronogramaActividadDtoServicioJdbc.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
//				CronogramaActividadJdbcDto cracAuxFin = new CronogramaActividadJdbcDto();
//				cracAuxFin = srvCronogramaActividadDtoServicioJdbc.buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(CronogramaConstantes.TIPO_NIVELACION_VALUE,ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
//				List<EstudianteJdbcDto>  listaEstudianteNivelacion = new ArrayList<EstudianteJdbcDto>();
//				List<MateriaDto> fgmfListMateriasEstado = new ArrayList<MateriaDto>();
//				List<EstudianteJdbcDto> listaAprobados = new ArrayList<EstudianteJdbcDto>();
//				List<EstudianteJdbcDto> listaReprobados = new ArrayList<EstudianteJdbcDto>();
//				//Transformamos a tipo Date
//				Date fechaInicioMatriculasOrdinarias = new Date(cracAuxInicio.getPlcrFechaInicio().getTime());
//				Date fechaFinMatriculasExtraordinaria = new Date(cracAuxFin.getPlcrFechaFin().getTime());
//				Date fechaActual = new Date();
//				if(fechaActual.after(fechaInicioMatriculasOrdinarias) && fechaActual.before(fechaFinMatriculasExtraordinaria)){
//					//lita estudiante nivelacion
//					listaEstudianteNivelacion = servEstudianteDtoServicioJdbc.buscarEstudianteNivelacionMigrarSIAC();
//					Integer op = 0;
//					for (EstudianteJdbcDto est : listaEstudianteNivelacion) {
//						op = 0;
//						//materias del estudiante
//						
////							FichaEstudiante fcesaux = new  FichaEstudiante();
////							fcesaux=servFichaEstudianteServicio.buscarPorFcinId(est.getFcinId());
////							if(fcesaux!=null){
//								try {
//								fgmfListMateriasEstado = servMateriaDtoServicioJdbc.listarNotasEstudianteNivelacion(est.getPrsIdentificacion());
//								} catch (MateriaDtoNoEncontradoException e) {
//								}
//								if(fgmfListMateriasEstado.size()>0){
//									for (MateriaDto itemMtr : fgmfListMateriasEstado) {
//										if(itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE){
//											op =  1;
//										}
//										if(itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE||itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE||itemMtr.getRcesEstado()==RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE){
//											op =  2;
//										}
//									}
//									if(op==0){
//										listaAprobados.add(est);
//									}
//									else if(op==1){
//										listaReprobados.add(est);
//									}
//								}
////							}
//					}
//					servFichaInscripcionServicio.desactivarRegistrosUsuarioAprobadosSiac(listaAprobados);
//					System.out.println(" desactivar fcin finnnnn ");
//				}
	
	

	

	
	
	
//	@Schedule(second="00", minute="30", hour="20", dayOfMonth="11", month="09", year="2018", persistent=false)
	public void procesoRetornoNivelacion(Timer timer){
		System.out.println("INICIO RETORNO");
		List<FichaInscripcionDto> listaRetorno = new ArrayList<FichaInscripcionDto>();
		try {
			boolean op=true;
			while (op){
				listaRetorno = servFichaInscripcionDtoServicioJdbc.listarFcinXRetornoNivelacion();
				if(listaRetorno.size()<5){
					op=false;
				}
				List<FichaInscripcionDto> listaEnvio = new ArrayList<FichaInscripcionDto>();
				lazo2:for (int i = 0; i < listaRetorno.size(); i++) {
					lazo:for (int j = i; j < listaRetorno.size(); j++) {
						if(listaRetorno.get(i).getUsroId().equals(listaRetorno.get(j).getUsroId())){
							listaEnvio.add(listaRetorno.get(j));
						}else{
							if(listaEnvio.size()>1){
								servFichaEstudianteServicio.editarFcesPorRetorno(listaEnvio);
								servUsuarioRolServicio.regresarNivelacionUsuarioRol(listaEnvio.get(0).getUsroId());
								break lazo2;
							}
							break lazo;
						}
					}
				}	
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FIN RETORNO");
	}
	
	
//	@Schedule(second="30", minute="*/1" , hour="01-16", persistent=false)
	public void procesoCorrecionConfiguracionCarrera(Timer timer){
		try {
			List<FichaInscripcionDto> listaRetorno = new ArrayList<FichaInscripcionDto>();
			listaRetorno = servFichaInscripcionDtoServicioJdbc.listarFcinParaCorrecionCncr();
			for (FichaInscripcionDto item : listaRetorno) {
				servFichaInscripcionDtoServicioJdbc.corregirCncrEnFcin(item);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	

	
}







	

