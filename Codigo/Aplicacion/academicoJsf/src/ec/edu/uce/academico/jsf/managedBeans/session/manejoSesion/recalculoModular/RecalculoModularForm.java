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
   
 ARCHIVO:     RecalculoModularForm.java	  
 DESCRIPCION: Bean de sesion que maneja el recálculo de las materias modulares.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19-09-2018			 Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.recalculoModular;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.NoResultException;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NotasPregradoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) RecalculoModularForm. 
 * Bean de sesion que maneja el recálculo de las materias modulares. 
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name = "recalculoModularForm")
@SessionScoped
public class RecalculoModularForm implements Serializable {
	
	private static final long serialVersionUID = 9210281719911332407L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario rmfUsuario;
	
	private List<PeriodoAcademico> rmfListaPeriodoAcademico;
	//PARA BUSQUEDA
	private Dependencia rmfDependencia;
	private PeriodoAcademico rmfPeriodoAcademicoBuscar;
	private CarreraDto rmfCarreraDtoBuscar;
	
	//PARA VISUALIZACIÓN
	private List<Materia> rmfListMateria;
	private Materia rmfMateriaConsultar;
	private List<EstudianteJdbcDto> rmfListEstudianteDto;
	
	//PARA LA ANULACIÓN
	private Integer rmfValidadorClic;
	private Integer rmfValidadorCalcular;
	private Integer rmfValidadorGuardar;
	
	
	private String rmfPrsIdentificacion;
	private String rmfNombreEstudiante;
	private List<MateriaDto> rmfListMateriasModulos;
	private List<MateriaDto> rmfListMateriasModular;
	
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
	MateriaServicio servRmfMateriaServicio;
	@EJB
	RolFlujoCarreraServicio servRmfRolFlujoCarreraServicio;
	@EJB
	CarreraServicio servRmfCarreraServicio;
	@EJB
	DependenciaServicio servRmfDependenciaServicio;
	@EJB
	private NotasPregradoDtoServicioJdbc servRmfNotasPregradoDtoServicioJdbc;
	
	@EJB
	private CalificacionServicio servRmfCalificacionServicio;
	@EJB
	private MateriaDtoServicioJdbc servMateriaDtoServicioJdbc;
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/

	/**
	 * Dirige la navegación hacia la página de listarMatriculas
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar matricula
	 */
	public String irAListar(Usuario usuario) {
		rmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			retorno = "irListarRecalculoModular";
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Esta opción no se encuentra habilitada.");
			return null;
		} catch (NoResultException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron materias modulares en la facultad.");
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
	
	public String irRecalculo(){
		
	
		try {
			List<Materia> listaModulos = new ArrayList<>();
			listaModulos = servRmfMateriaServicio.listarTodosModulos(rmfListMateriasModular.get(0).getMtrId());
			if(rmfListMateriasModulos.size()==listaModulos.size()){
				
				BigDecimal asistenciaPromedio = BigDecimal.ZERO;
				BigDecimal asistenciaDocente = BigDecimal.ZERO;
				BigDecimal asistenciaSuma = BigDecimal.ZERO;
				BigDecimal asistenciaSumaDocente = BigDecimal.ZERO;
				BigDecimal asistenciaDocente1 = BigDecimal.ZERO;
				BigDecimal asistenciaDocente2 = BigDecimal.ZERO;
				BigDecimal asistenciaEstudiante1 = BigDecimal.ZERO;
				BigDecimal asistenciaEstudiante2 = BigDecimal.ZERO;
				BigDecimal notEstudiante2 = BigDecimal.ZERO;
				BigDecimal notEstudiante1 = BigDecimal.ZERO;
				BigDecimal notaSuma = BigDecimal.ZERO;
				BigDecimal notaSumaP1P2 = BigDecimal.ZERO;
				BigDecimal notaPromedio = BigDecimal.ZERO;
				Float asistenciaMenor = Float.valueOf(100);
				Float notaMenor = Float.valueOf(100);
				for (MateriaDto item : rmfListMateriasModulos) {
					if(item.getClfNotaFinalSemestre()!=null || item.getClfNotaFinalSemestre()!=GeneralesConstantes.APP_ID_BASE.floatValue()){
						notaSuma=notaSuma.add(new BigDecimal(item.getClfNotaFinalSemestre()).setScale(2, RoundingMode.DOWN));
						notEstudiante1=notEstudiante1.add(new BigDecimal(item.getClfNota1()).setScale(2, RoundingMode.DOWN));
						notEstudiante2=notEstudiante2.add(new BigDecimal(item.getClfNota2()).setScale(2, RoundingMode.DOWN));
						
						asistenciaDocente1=asistenciaDocente1.add(new BigDecimal(item.getClfAsistenciaDocente1()).setScale(2, RoundingMode.DOWN));
						asistenciaDocente2=asistenciaDocente2.add(new BigDecimal(item.getClfAsistenciaDocente2()).setScale(2, RoundingMode.DOWN));
						asistenciaEstudiante1=asistenciaEstudiante1.add(new BigDecimal(item.getClfAsistencia1()).setScale(2, RoundingMode.DOWN));
						asistenciaEstudiante2=asistenciaEstudiante2.add(new BigDecimal(item.getClfAsistencia2()).setScale(2, RoundingMode.DOWN));
						asistenciaSumaDocente=asistenciaSumaDocente.add(new BigDecimal(item.getClfAsistenciaTotalDoc()).setScale(2, RoundingMode.DOWN));
						asistenciaSuma=asistenciaSuma.add(asistenciaEstudiante1.add(asistenciaEstudiante2));
						notaMenor = item.getClfNotaFinalSemestre();
						if(item.getClfNotaFinalSemestre()<notaMenor){
							notaMenor = item.getClfNotaFinalSemestre();
						}	
					}
					
				}
				notEstudiante1=notEstudiante1.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
				notEstudiante2=notEstudiante2.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
				asistenciaEstudiante1=asistenciaEstudiante1.divide(new BigDecimal(listaModulos.size()),0,RoundingMode.DOWN);
				asistenciaEstudiante2=asistenciaEstudiante2.divide(new BigDecimal(listaModulos.size()),0,RoundingMode.DOWN);
				asistenciaDocente1=asistenciaDocente1.divide(new BigDecimal(listaModulos.size()),0,RoundingMode.DOWN);
				asistenciaDocente2=asistenciaDocente2.divide(new BigDecimal(listaModulos.size()),0,RoundingMode.DOWN);
				notaSumaP1P2=notaSumaP1P2.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
				notaSuma=notaSuma.divide(new BigDecimal(listaModulos.size()),2,RoundingMode.DOWN);
				asistenciaPromedio=asistenciaSuma.divide(asistenciaSumaDocente,2,RoundingMode.HALF_UP).multiply(new BigDecimal(100));
				if((asistenciaPromedio.compareTo(new BigDecimal(100))==1)){
					asistenciaPromedio= new BigDecimal(100); 
				}
				rmfListMateriasModular.get(0).setClfAsistencia1(asistenciaEstudiante1.intValue());
				rmfListMateriasModular.get(0).setClfAsistencia2(asistenciaEstudiante2.intValue());
				rmfListMateriasModular.get(0).setClfAsistenciaDocente1(asistenciaDocente1.intValue());
				rmfListMateriasModular.get(0).setClfAsistenciaDocente2(asistenciaDocente2.intValue());
				rmfListMateriasModular.get(0).setClfNota1(notEstudiante1.floatValue());
				rmfListMateriasModular.get(0).setClfNota2(notEstudiante2.floatValue());
				rmfListMateriasModular.get(0).setClfSumaP1P2(notaSumaP1P2.floatValue());
				rmfListMateriasModular.get(0).setClfNotaFinalSemestre(notaSuma.floatValue());
				rmfListMateriasModular.get(0).setClfPromedioAsistencia(asistenciaPromedio.floatValue());
				
				if(rmfListMateriasModular.get(0).getClfNotaFinalSemestre()>27.5){
					rmfListMateriasModular.get(0).setRcesEstado(1);
				}else{
					rmfListMateriasModular.get(0).setRcesEstado(2);
				}
				rmfValidadorGuardar=1;
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al calcular la materia modular, por favor comuníquese con el administrador del sistema.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al calcular la materia modular, por favor comuníquese con el administrador del sistema.");
		}
		return  null;
	}
	
	
	
	public String guardar(){
		try {
//			List<MateriaDto> listaMateriasModulares = new ArrayList<MateriaDto>();
//			listaMateriasModulares = servMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoEnCierreXcarreraXMateriaModular(rmfListMateriasModular.get(0).getPrsIdentificacion(), rmfListMateriasModular.get(0).getRcesId());
//			
			servRmfCalificacionServicio.guardarCorreccionModularesFull(rmfListMateriasModulos);
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Se ha guardado correctamente el recálculo de la materia modular.");
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al guardar la materia modular, por favor comuníquese con el administrador del sistema.");
		}
		return  null;
	}
	
	
//	/**
//	 * Método que agrega materias para anular la matrícula
//	 * @return retorna - la navegación de la página listar matriculas
//	 */
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
//				String archivoGuardado = null;
//				if(servRmfMatriculaServicio.generarSolicitudRetiroMatricula(listaSeleccionada, DetalleMatriculaConstantes.ESTADO_INACTIVO_VALUE, rmfNombreArchivoSubido, DetalleMatriculaConstantes.ESTADO_HISTORICO_RETIRO_SOLICITADO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_SOLICITADO_VALUE)){
//					//PARA CARGA DE ARCHIVO EN EL SERVIDOR
//					if(rmfNombreArchivoSubido != null){
//						for (EstudianteJdbcDto item : rmfListEstudianteDto) {
//							if(item.isSeleccionado()){
//								rutaNombre = item.getDtmtId()+ "." + extension;
//								archivoGuardado = rutaNombre;
//								rutaTemporal = System.getProperty("java.io.tmpdir")+ File.separator + rmfNombreArchivoSubido;
//								
////								String smbUrl = "smb://produ:12345.a@10.20.1.63/siiuArch/archivos/"+GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre ;
//								
//								GeneralesUtilidades.copiarArchivo(new FileInputStream(new File(rutaTemporal)),GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ rutaNombre);
//								listaSeleccionadaGuarda.add(item);
//							}else{
//								listaSeleccionadaNoGuarda.add(item);
//							}
//							rutaNombre = null;
//							rutaTemporal = null;
//						}
//						/******************************************************************************/
//						/************************* PROCESO DE ENVIÓ DE MAIL ***************************/
//						/******************************************************************************/
//
//						//datos estudiante para envio
//						rmfFichaMatriculaDtoSeleccionado.getPrsMailInstitucional();
//
//						//datos del director de carrera
//						UsuarioRolJdbcDto usrAuxDoc = new UsuarioRolJdbcDto();
//						usrAuxDoc = servRmfUsuarioRolDtoServicioJdbc.buscarUsuarioXCarreraXRol(RolConstantes.ROL_DIRCARRERA_VALUE, rmfFichaMatriculaDtoSeleccionado.getCrrId());
//						System.out.println("mail director carrera "+usrAuxDoc.getPrsMailInstitucional());
//						//lista de materias retiradas por el estudiante para envió
//
//						//******************************************************************************
//						//************************* ACA INICIA EL ENVIO DE MAIL CON ADJUNTO ************
//						//******************************************************************************
//
//						try{
//							Connection connection = null;
//							Session session = null;
//							MessageProducer producer = null;
//							ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",GeneralesConstantes.APP_NIO_ACTIVEMQ);
//							connection = connectionFactory.createConnection();
//							connection.start();
//							session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//							Destination destino = session.createQueue(GeneralesConstantes.APP_COLA_ACTIVEMQ);
//							// Creamos un productor
//							producer = session.createProducer(destino);
//
////							JasperReport jasperReport = null;
////							JasperPrint jasperPrint;
////							List<Map<String, Object>> frmAdjuntoCampos = null;
////							Map<String, Object> frmAdjuntoParametros = null;
//
////							frmAdjuntoParametros = new HashMap<String, Object>();
////							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
////							String fecha = formato.format(new Date());
////							frmAdjuntoParametros.put("fecha",fecha);
////							frmAdjuntoParametros.put("estudiante", rmfFichaMatriculaDtoSeleccionado.getPrsNombres()+" "+rmfFichaMatriculaDtoSeleccionado.getPrsPrimerApellido()+" "+rmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido());
////
////							frmAdjuntoParametros.put("periodo", rmfFichaMatriculaDtoSeleccionado.getPracDescripcion());
////							//							frmAdjuntoParametros.put("facultad", rmfFichaMatriculaDtoSeleccionado.getDpnDescripcion());
////							frmAdjuntoParametros.put("carrera", rmfFichaMatriculaDtoSeleccionado.getCrrDetalle());
////
////							frmAdjuntoCampos = new ArrayList<Map<String, Object>>();
////							Map<String, Object> datoMateriaRetirada = null;
////
////
////							for (EstudianteJdbcDto item : listaSeleccionada) {
////								StringBuilder sbRetiro = new StringBuilder();
////								datoMateriaRetirada = new HashMap<String, Object>();
////
////								datoMateriaRetirada.put("codigo", item.getMtrCodigo());
////								datoMateriaRetirada.put("materia", GeneralesUtilidades.generaStringConTildes(item.getMtrDescripcion()));
////
////								frmAdjuntoCampos.add(datoMateriaRetirada);
////							}
////
////							JRDataSource dataSource = new JRBeanCollectionDataSource(frmAdjuntoCampos);
////
////							jasperPrint = JasperFillManager.fillReport(jasperReport,frmAdjuntoParametros, dataSource);
////
////							byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
//
//							
//                             StringBuilder pathGeneralRequisitos = new StringBuilder();
////                             pathGeneralRequisitos.append("C:\\archivos\\5_1717975989.pdf");  
//                             pathGeneralRequisitos.append(GeneralesConstantes.APP_PATH_ARCHIVOS +GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+ archivoGuardado);
//                             Path path = Paths.get(pathGeneralRequisitos.toString());
//                             byte[] arregloRequisitos = Files.readAllBytes(path);
//                             
//							//lista de correos a los que se enviara el mail
//							List<String> correosTo = new ArrayList<>();
//							correosTo.add(rmfFichaMatriculaDtoSeleccionado.getPrsMailInstitucional()); //estudiante
//							correosTo.add(usrAuxDoc.getPrsMailInstitucional()); //director de carrera
//							correosTo.add("dcollaguazo@uce.edu.ec");
//							//path de la plantilla del mail
//							ProductorMailJson pmail = null;
//							StringBuilder sbCorreo= new StringBuilder();
//							SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es", "ES"));
//							String fecha = formato.format(new Date());
//
//							sbCorreo= GeneralesUtilidades.generarAsuntoSolicitudRetiroMatricula(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
//									rmfFichaMatriculaDtoSeleccionado.getPrsPrimerApellido()+" "+rmfFichaMatriculaDtoSeleccionado.getPrsSegundoApellido()+" "+rmfFichaMatriculaDtoSeleccionado.getPrsNombres() , GeneralesUtilidades.generaStringConTildes(rmfFichaMatriculaDtoSeleccionado.getCrrDescripcion()),listaSeleccionada);	
//
//							pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_SOLICITUD_MATRICULA,
//                                    sbCorreo.toString()
//                                      , "admin", "dt1c201s", true, arregloRequisitos, "Retiro de matrícula."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
//							
//							String jsonSt = pmail.generarMail();
//							Gson json = new Gson();
//							MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
//							// 	Iniciamos el envío de mensajes
//							ObjectMessage message = session.createObjectMessage(mailDto);
//							producer.send(message);
//						}  catch (Exception e) {
//							e.printStackTrace();
//						}
//
//						//******************************************************************************
//						//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//						//******************************************************************************
//
//
//						/******************************************************************************/
//						/******************************************************************************/
//
//					}
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
	
	public void buscar(){
		rmfValidadorCalcular = 0;
		rmfValidadorGuardar = 0;
		if(rmfMateriaConsultar.getMtrId()!=GeneralesConstantes.APP_ID_BASE && rmfPrsIdentificacion!=null){
			rmfListMateriasModular = new ArrayList<MateriaDto>();
			rmfListMateriasModulos = new ArrayList<MateriaDto>();
			try {
//				PeriodoAcademico prac = servRmfPeriodoAcademicoServicio.buscarPeriodoEnCierre();
//				if(prac!=null){
//					
//				}else{
//					prac = servRmfPeriodoAcademicoServicio.buscarPeriodoXestadoXtipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE,PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//				}
				
				
				
				rmfListMateriasModular = servRmfNotasPregradoDtoServicioJdbc.buscarEstudianteNotasMateriaModularXMateriaXPeriodoXIdentificacion(
						rmfMateriaConsultar.getMtrId(), rmfPeriodoAcademicoBuscar.getPracId(), rmfPrsIdentificacion);
				
				if(rmfListMateriasModular.get(0).getPrsSegundoApellido()!=null){
					rmfNombreEstudiante = rmfListMateriasModular.get(0).getPrsNombres()+" "+rmfListMateriasModular.get(0).getPrsPrimerApellido()+" "+rmfListMateriasModular.get(0).getPrsSegundoApellido();
				}else{
					rmfNombreEstudiante = rmfListMateriasModular.get(0).getPrsNombres()+" "+rmfListMateriasModular.get(0).getPrsPrimerApellido();	
				}
				rmfListMateriasModulos = servRmfNotasPregradoDtoServicioJdbc.buscarEstudianteNotasMateriasModulosXMateriaXPeriodoXIdentificacion(
						rmfListMateriasModular.get(0).getRcesId(), rmfPeriodoAcademicoBuscar.getPracId(), rmfPrsIdentificacion);
				rmfValidadorCalcular = 1;
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El estudiante no posee registros de calificaciones de la materia seleccionada en el período actual.");
			}	
		}else{
			if(rmfMateriaConsultar.getMtrId()==GeneralesConstantes.APP_ID_BASE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor debe seleccionar la materia para la búsqueda.");
				rmfPrsIdentificacion = null;
			}else if(rmfPrsIdentificacion==null){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Por favor debe ingresar la identificación del estudiante para la búsqueda.");
			}
		}
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		rmfPrsIdentificacion =null;
		//INSTANCIO LA CARRERA DTO
		rmfCarreraDtoBuscar = new CarreraDto();
		//INICIALIZO LA VARIABLE PARA EL MODAL DE ANULACIÓN DE MATRICULAS
		rmfValidadorClic = 0;
		rmfListMateria = new ArrayList<Materia>();
		rmfMateriaConsultar = new Materia();
		rmfNombreEstudiante = null;
		rmfListMateriasModular = null;
		rmfListMateriasModulos = null;
		rmfValidadorCalcular = 0;
		rmfValidadorGuardar = 0;
		rmfMateriaConsultar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		rmfListaPeriodoAcademico= new ArrayList<PeriodoAcademico>();
		rmfListaPeriodoAcademico = servRmfPeriodoAcademicoServicio.listarTodos();
		rmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		try {
//			// BUSCA EL PERIODO ACADEMICO ACTIVO
//			rmfPeriodoAcademicoBuscar = servRmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
//			if (rmfPeriodoAcademicoBuscar != null) {
//
//			} else {
//				rmfPeriodoAcademicoBuscar = servRmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
//			}
			List<RolFlujoCarrera> listaRoflcr = new ArrayList<RolFlujoCarrera>();
			listaRoflcr = servRmfRolFlujoCarreraServicio.buscarPorIdUsuario(rmfUsuario);
			Carrera crrAux = servRmfCarreraServicio.buscarFacultadXCarrera(listaRoflcr.get(0).getRoflcrCarrera().getCrrId());
			rmfListMateria = servRmfMateriaServicio.listarTodosModularesXFacultad(crrAux.getCrrDependencia().getDpnId());
			rmfDependencia = servRmfDependenciaServicio.buscarDependenciaXcrrId(crrAux.getCrrId());
		} catch (Exception e) {
			throw new NoResultException();
		}
		if (rmfListMateria.size() == 0) {
			throw new NoResultException();
		}

	}
	
	public void limpiar(){
		iniciarParametros();
	}
	
	


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
	public List<EstudianteJdbcDto> getRmfListEstudianteDto() {
		rmfListEstudianteDto = rmfListEstudianteDto==null?(new ArrayList<EstudianteJdbcDto>()):rmfListEstudianteDto;
		return rmfListEstudianteDto;
	}
	public void setRmfListEstudianteDto(List<EstudianteJdbcDto> rmfListEstudianteDto) {
		this.rmfListEstudianteDto = rmfListEstudianteDto;
	}
	public Integer getRmfValidadorClic() {
		return rmfValidadorClic;
	}
	public void setRmfValidadorClic(Integer rmfValidadorClic) {
		this.rmfValidadorClic = rmfValidadorClic;
	}

	public List<Materia> getRmfListMateria() {
		return rmfListMateria;
	}

	public void setRmfListMateria(List<Materia> rmfListMateria) {
		this.rmfListMateria = rmfListMateria;
	}

	public Dependencia getRmfDependencia() {
		return rmfDependencia;
	}

	public void setRmfDependencia(Dependencia rmfDependencia) {
		this.rmfDependencia = rmfDependencia;
	}

	public Materia getRmfMateriaConsultar() {
		return rmfMateriaConsultar;
	}

	public void setRmfMateriaConsultar(Materia rmfMateriaConsultar) {
		this.rmfMateriaConsultar = rmfMateriaConsultar;
	}

	public String getRmfPrsIdentificacion() {
		return rmfPrsIdentificacion;
	}

	public void setRmfPrsIdentificacion(String rmfPrsIdentificacion) {
		this.rmfPrsIdentificacion = rmfPrsIdentificacion;
	}

	public String getRmfNombreEstudiante() {
		return rmfNombreEstudiante;
	}

	public void setRmfNombreEstudiante(String rmfNombreEstudiante) {
		this.rmfNombreEstudiante = rmfNombreEstudiante;
	}

	public List<MateriaDto> getRmfListMateriasModulos() {
		return rmfListMateriasModulos;
	}

	public void setRmfListMateriasModulos(List<MateriaDto> rmfListMateriasModulos) {
		this.rmfListMateriasModulos = rmfListMateriasModulos;
	}

	public List<MateriaDto> getRmfListMateriasModular() {
		return rmfListMateriasModular;
	}

	public void setRmfListMateriasModular(List<MateriaDto> rmfListMateriasModular) {
		this.rmfListMateriasModular = rmfListMateriasModular;
	}

	public Integer getRmfValidadorCalcular() {
		return rmfValidadorCalcular;
	}

	public void setRmfValidadorCalcular(Integer rmfValidadorCalcular) {
		this.rmfValidadorCalcular = rmfValidadorCalcular;
	}

	public Integer getRmfValidadorGuardar() {
		return rmfValidadorGuardar;
	}

	public void setRmfValidadorGuardar(Integer rmfValidadorGuardar) {
		this.rmfValidadorGuardar = rmfValidadorGuardar;
	}

	public List<PeriodoAcademico> getRmfListaPeriodoAcademico() {
		return rmfListaPeriodoAcademico;
	}

	public void setRmfListaPeriodoAcademico(List<PeriodoAcademico> rmfListaPeriodoAcademico) {
		this.rmfListaPeriodoAcademico = rmfListaPeriodoAcademico;
	}

	
	
	
	
	
}
