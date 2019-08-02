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
   
 ARCHIVO:     RegistroAutomaticoForm.java	  
 DESCRIPCION: Bean de sesion que maneja el registro en bloque del los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19-10-2017			 Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.soporte;

import java.io.Serializable;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.ComprobanteCSVDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ComprobantePagoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RegistroAutomaticoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ComprobanteCSVDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EdicionComprobantePagoForm. 
 * Bean de sesion que maneja la edición del comprobante de pago por parte de soporte.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name = "edicionComprobantePagoForm")
@SessionScoped
public class EdicionComprobantePagoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private String ecpfNumComprobante;
	private ComprobanteCSVDto ecpfComprobantePago;
	private List<ComprobanteCSVDto> ecpfListaComprobantePago;
	private boolean ecpfValidadorBuscar;
	private boolean ecpfValidadorGuardar;
	private String ecpfFechaEmision;
	private String ecpfFechaCaduca;
	private Integer ecpfTipoFacturacion;
	private Integer ecpfValidadorClick;
	private boolean ecpfHabilitarGuardar;
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
	ComprobanteCSVDtoServicioJdbc servComprobanteCSVDtoServicioJdbc;
	@EJB 
	PeriodoAcademicoServicio servRafPeriodoAcademico;
	@EJB 
	ConfiguracionCarreraServicio servRafConfiguracionCarrera;
	@EJB 
	TipoFormacionServicio  servRafTipoFormacion;
	@EJB 
	RegistroAutomaticoServicio  servRafRegistroAutomatico;
	@EJB 
	UsuarioRolServicio  servUsuarioRolServicio;
	@EJB
	private	DependenciaServicio servFgmfDependenciaServicio;
	@EJB 
	ComprobantePagoServicio servComprobantePagoServicio;
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Inicar parámetros para la funcionalidad
	 */
	public void iniciarParametros(){
		ecpfNumComprobante=null;
		ecpfValidadorBuscar=false;
		ecpfComprobantePago=new ComprobanteCSVDto();
		ecpfValidadorGuardar=true;
		ecpfFechaEmision = null;
		ecpfFechaCaduca = null;
		ecpfTipoFacturacion=GeneralesConstantes.APP_ID_BASE;
		ecpfValidadorClick = 0;
		ecpfListaComprobantePago = new ArrayList<ComprobanteCSVDto>();
		ecpfHabilitarGuardar = true;
	}
	
	/**
	 * Inicar parámetros para la funcionalidad
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula
	 * @return navegacion al listar matricula
	 */
	@SuppressWarnings("unused")
	public String iniciarEdicionComprobanteForm(Usuario usuario) {
		List<UsuarioRol> usro = new ArrayList<UsuarioRol>();
		try {
			usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Usuario no encontrado no puede continuar.");
			return null;
		} catch (UsuarioRolException e) {
		}
		iniciarParametros();
		return "irIniciarEdicionComprobante";
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public void buscarComprobantes(){
		try {
			ecpfListaComprobantePago = servComprobanteCSVDtoServicioJdbc.buscarComprobantesPagoParaEditarReimpresion(ecpfNumComprobante);
			ecpfValidadorBuscar=true;
			for (ComprobanteCSVDto item : ecpfListaComprobantePago) {
				if(item.getCmpaDescripcion().equals("MATRICULA NIVELACIÓN NIVELACION")){
					item.setCmpaDescripcion("MATRICULA NIVELACIÓN");
				}
				SimpleDateFormat formato = 
						new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
				item.setFechaEmision(formato.format(new Date(item.getCmpaFechaEmision().getTime())));
				item.setFechaCaduca(formato.format(new Date(item.getCmpaFechaCaduca().getTime())));
			}
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
		}
		if(ecpfNumComprobante!=null){
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
		}
		
	}

	public void verificarTipo(){
		if(ecpfTipoFacturacion!=null){
			if(ecpfTipoFacturacion!=GeneralesConstantes.APP_ID_BASE){
				ecpfValidadorGuardar=false;	
			}else{
				ecpfValidadorGuardar=true;
			}
		}else{
			ecpfValidadorGuardar=true;
		}
	}
	
	public void verificarClickGuardarEdicion(){
		boolean op = false;
		for (int i=0;i<ecpfListaComprobantePago.size()-1;i++) {
			for (int j=i;j<ecpfListaComprobantePago.size();j++) {
				if(ecpfListaComprobantePago.get(i).getPrsIdentificacion().equals(ecpfListaComprobantePago.get(j).getPrsIdentificacion())){
				}else{
					op=true;
				}
			}
		}
		if(op){
			ecpfValidadorClick = 0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("La lista de comprobantes no puede tener identificaciones diferentes para el mismo número de comprobante.");
		}else{
			for (int i=0;i<ecpfListaComprobantePago.size()-1;i++) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ecpfListaComprobantePago.get(i).getCmpaFechaEmision());
				cal.add(Calendar.DAY_OF_WEEK, 10);
				Date date = new Date();
				if(cal.after(date )){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("La fecha de emisión es mayor a 10 días, por favor use la funcionalidad de generar nuevo número de comprobante.");
				}else{
					ecpfValidadorClick = 1;
				}
			}
		}
		op = false;
	}
	
	
	public void verificarClickNoGuardarEdicion(){
		ecpfValidadorClick = 0;
	}
	
	public String guardar(){
		try {
			for (ComprobanteCSVDto item : ecpfListaComprobantePago) {
				servComprobantePagoServicio.editarMontoPagarXid(item.getCmpaId(), GeneralesConstantes.APP_ID_BASE);
			}
//			if(servComprobantePagoServicio.editarMontoPagarXid(ecpfComprobantePago.getCmpaId(), GeneralesConstantes.APP_ID_BASE)){
//				StringBuilder pathGeneralImagenes = new StringBuilder();
//				pathGeneralImagenes.append(FacesContext.getCurrentInstance()
//						.getExternalContext().getRealPath("/"));
//				pathGeneralImagenes.append("/academico/reportes/archivosJasper/matriculasNivelacion/codigoBarras.png");
//					// Generamos el código de barras para el adjunto del voucher de pago
//				try {
////					//Es el tipo de clase 
//					Barcode128 code128 = new Barcode128();
//					//Seteo el tipo de codigo
//					code128.setCodeType(Barcode.CODE128);
//					code128.setBarHeight(15f); 
//					code128.setStartStopText(false);
//					code128.setExtended(true);
//					code128.setX(1f);
//					//Setep el codigo
//					code128.setCode(ecpfComprobantePago.getCmpaNumComprobante());
//					//Guardo la imagen
//					java.awt.Image rawImage = code128.createAwtImage(Color.BLACK, Color.WHITE);
//					BufferedImage outImage = new BufferedImage(rawImage.getWidth(null), rawImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
//					outImage.getGraphics().drawImage(rawImage, 0, 0, null);
//					ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
//					ImageIO.write(outImage, "png", bytesOut);
//					bytesOut.flush();
//					byte[] data = bytesOut.toByteArray();
//					
//					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(data));
//					File file = new File(pathGeneralImagenes.toString());
//					if (file.exists()) {
//						file.delete();     
//					}
//					file = new File(pathGeneralImagenes.toString());
//					ImageIO.write(bi, "PNG", file);
//				  }catch (java.io.IOException ex) {
//					  ex.printStackTrace();
//					  
//				  }
//				Dependencia facultadAux = null;
////				try {
////					facultadAux = servFgmfDependenciaServicio.buscarFacultadXcrrId(carreraAux.getCrrId());
////					generarReporteOrdenCobro(facultadAux.getDpnDescripcion(),ecpfComprobantePago.getCmpaNumComprobante() ,fgmfUsuario.getUsrPersona(),carreraAux);
////				} catch (DependenciaNoEncontradoException e) {
////				}
//				try {
//					//******************************************************************************
//					  //************************* ACA INICIA EL ENVIO DE MAIL ************************
//					//******************************************************************************
//					Connection connection = null;
//					Session session = null;
//					MessageProducer producer = null;
//					ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin","nio://10.20.1.64:61616");
//					connection = connectionFactory.createConnection();
//					connection.start();
//					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//					Destination destino = session.createQueue("COLA_MAIL_DTO");
//					// Creamos un productor
//					producer = session.createProducer(destino);
//					JasperReport jasperReport = null;
//					JasperPrint jasperPrint;
//					List<Map<String, Object>> frmCrpCampos = null;
//					Map<String, Object> frmCrpParametros = null;
//					// ****************************************************************//
//					// ***************** GENERACION DE LA ORDEN DE PAGO  *************//
//					// ****************************************************************//
//					// ****************************************************************//
//					frmCrpParametros = new HashMap<String, Object>();
//					frmCrpParametros.put("facultad","NIVELACION");
//					SimpleDateFormat formato = 
//							new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
//					String fecha = formato.format(ecpfComprobantePago.getCmpaFechaEmision());
//					Calendar c = Calendar.getInstance();
//					c.setTime(ecpfComprobantePago.getCmpaFechaEmision()); 
//					c.add(Calendar.DATE, 4);
//					String fechaCaducidad = formato.format(c.getTime());
//					frmCrpParametros.put("fecha",fecha);
//					frmCrpParametros.put("fechaCaducidad",fechaCaducidad);
//					
//					frmCrpParametros.put("numComprobante",ecpfComprobantePago.getCmpaNumComprobante());
//					frmCrpCampos = new ArrayList<Map<String, Object>>();
//					Map<String, Object> dato = null;
//					dato = new HashMap<String, Object>();
//					String nombres=ecpfComprobantePago.getPrsNombres()+" "+ecpfComprobantePago.getPrsPrimerApellido().toUpperCase()+" "
//							+(ecpfComprobantePago.getPrsSegundoApellido() == null?" ":ecpfComprobantePago.getPrsSegundoApellido());
//					frmCrpParametros.put("carrera", ecpfComprobantePago.getCrrDescripcion());
//					frmCrpParametros.put("identificacion", ecpfComprobantePago.getPrsIdentificacion());
//					
//					frmCrpParametros.put("postulante", nombres);
//					
//					int comparador = ComprobantePagoConstantes.MATRICULA_TIPO_NIVELACION_SEGUNDA_MATRICULA_VALUE.compareTo(new BigDecimal(ecpfTipoFacturacion));
//					if(comparador==0){
//						frmCrpParametros.put("textoNivelacion","MATRICULA SEGUNDA VEZ - NIVELACION");
//						frmCrpParametros.put("valorPagar", "140.00");
//					}else if (comparador==1){
//						frmCrpParametros.put("valorPagar", "00.00");
//						
//					}else{
//						frmCrpParametros.put("valorPagar", "240.00");
//						frmCrpParametros.put("textoNivelacion","MATRICULA TERCERA VEZ - NIVELACION");
//					}	
//								
//					frmCrpParametros.put("direccion", ecpfComprobantePago.getPrsDireccion());
//					
//					frmCrpParametros.put("email", ecpfComprobantePago.getPrsMailPersonal());
//					if(ecpfComprobantePago.getPrsTelefono()!=null){
//						frmCrpParametros.put("telefono", ecpfComprobantePago.getPrsTelefono());	
//					}else{
//						frmCrpParametros.put("telefono", " ");
//					}
//					
//					StringBuilder pathGeneralReportes = new StringBuilder();
//					pathGeneralReportes.append(FacesContext.getCurrentInstance()
//							.getExternalContext().getRealPath("/"));
//					pathGeneralReportes.append("/academico/reportes/archivosJasper/matriculasNivelacion");
//					frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/logoUce.png");
//					frmCrpParametros.put("imagenCodigoBarras", pathGeneralReportes+"/codigoBarras.png");
//					frmCrpCampos.add(dato);
//					jasperReport = (JasperReport) JRLoader.loadObject(new File(
//							(pathGeneralReportes.toString() + "/VoucherMatricula.jasper")));
//					jasperPrint = JasperFillManager.fillReport(jasperReport,frmCrpParametros, new JREmptyDataSource());
//					byte[] arreglo = JasperExportManager.exportReportToPdf(jasperPrint);
//								
//					//lista de correos a los que se enviara el mail
//					List<String> correosTo = new ArrayList<>();
//					correosTo.add(ecpfComprobantePago.getPrsMailPersonal());
//					//path de la plantilla del mail
//					ProductorMailJson pmail = null;
//					StringBuilder sbCorreo= new StringBuilder();
//					formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
//					fecha = formato.format(new Date());
//					sbCorreo= GeneralesUtilidades.generarAsuntoNuevaFactura(GeneralesUtilidades.generaStringParaCorreo(fecha.toString()),
//							nombres, null,"NIVELACION");
//					pmail = new ProductorMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE,GeneralesConstantes.APP_ASUNTO_MATRICULA,
//							sbCorreo.toString()
//							, "admin", "dt1c201s", true, arreglo, "Comprobante."+MailDtoConstantes.TIPO_PDF, MailDtoConstantes.TIPO_PDF );
//					String jsonSt = pmail.generarMail();
//					Gson json = new Gson();
//					MailDto mailDto = json.fromJson(jsonSt, MailDto.class);
//					// 	Iniciamos el envío de mensajes
//					ObjectMessage message = session.createObjectMessage(mailDto);
//					producer.send(message);
//					
//					//******************************************************************************
//					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//					//******************************************************************************
//				} catch (Exception e) {
//				}
			try {
				ecpfListaComprobantePago = servComprobanteCSVDtoServicioJdbc.buscarComprobantesPagoParaEditarReimpresion(ecpfNumComprobante);
				ecpfValidadorBuscar=true;
				for (ComprobanteCSVDto item : ecpfListaComprobantePago) {
					if(item.getCmpaDescripcion().equals("MATRICULA NIVELACIÓN NIVELACION")){
						item.setCmpaDescripcion("MATRICULA NIVELACIÓN");
					}
					SimpleDateFormat formato = 
							new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
					item.setFechaEmision(formato.format(new Date(item.getCmpaFechaEmision().getTime())));
					item.setFechaCaduca(formato.format(new Date(item.getCmpaFechaCaduca().getTime())));
				}
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
			}
				ecpfValidadorClick=0;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha modificado correctamente las fechas del comprobante de pago.");
				return null;	
//			}else{
//				ecpfValidadorClick=0;
//				FacesUtil.limpiarMensaje();
//				FacesUtil.mensajeError("Error al editar el comprobante de pago, intente más tarde...");
//				return null;
//			}
			
		} catch (Exception e) {
			ecpfValidadorClick=0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al editar el comprobante de pago, intente más tarde...");
			return null;
		}
		
		
	}
	
	public void editarNumComprobante(ComprobanteCSVDto item){
		boolean op = false;
		try {
			for (int i=0;i<ecpfListaComprobantePago.size()-1;i++) {
				for (int j=i;j<ecpfListaComprobantePago.size();j++) {
					if(ecpfListaComprobantePago.get(i).getPrsIdentificacion().equals(ecpfListaComprobantePago.get(j).getPrsIdentificacion())){
					}else{
						op=true;
					}
				}
			}
			String numComprobante;
			if(op){
				numComprobante = servComprobantePagoServicio.editarNumComprobanteXCmpaId(item.getCmpaId());
			}else{
				numComprobante = servComprobantePagoServicio.editarNumComprobanteXCmpaNumComprobante(item.getCmpaNumComprobante(),item.getCmpaId());
			}
			ecpfValidadorClick=0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("El comprobante ha sido modificado correctamente, el nuevo número es: "+numComprobante);
			try {
				ecpfListaComprobantePago = servComprobanteCSVDtoServicioJdbc.buscarComprobantesPagoParaEditarReimpresion(ecpfNumComprobante);
				ecpfValidadorBuscar=true;
				for (ComprobanteCSVDto entidad : ecpfListaComprobantePago) {
					if(entidad.getCmpaDescripcion().equals("MATRICULA NIVELACIÓN NIVELACION")){
						entidad.setCmpaDescripcion("MATRICULA NIVELACIÓN");
					}
					SimpleDateFormat formato = 
							new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
					entidad.setFechaEmision(formato.format(new Date(entidad.getCmpaFechaEmision().getTime())));
					entidad.setFechaCaduca(formato.format(new Date(entidad.getCmpaFechaCaduca().getTime())));
				}
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No existen resultados con los parámetros ingresados.");
			}
			
			
		} catch (ComprobantePagoNoEncontradoException e) {
			ecpfValidadorClick=0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al editar el comprobante de pago, intente más tarde...");
		} catch (ComprobantePagoException e) {
			ecpfValidadorClick=0;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al editar el comprobante de pago, intente más tarde...");
		}
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
		}
			
	}
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
		
		
	
	public String getEcpfNumComprobante() {
		return ecpfNumComprobante;
	}

	public void setEcpfNumComprobante(String ecpfNumComprobante) {
		this.ecpfNumComprobante = ecpfNumComprobante;
	}

	public ComprobanteCSVDto getEcpfComprobantePago() {
		return ecpfComprobantePago;
	}

	public void setEcpfComprobantePago(ComprobanteCSVDto ecpfComprobantePago) {
		this.ecpfComprobantePago = ecpfComprobantePago;
	}

	public boolean isEcpfValidadorBuscar() {
		return ecpfValidadorBuscar;
	}

	public void setEcpfValidadorBuscar(boolean ecpfValidadorBuscar) {
		this.ecpfValidadorBuscar = ecpfValidadorBuscar;
	}

	public boolean isEcpfValidadorGuardar() {
		return ecpfValidadorGuardar;
	}

	public void setEcpfValidadorGuardar(boolean ecpfValidadorGuardar) {
		this.ecpfValidadorGuardar = ecpfValidadorGuardar;
	}

	public String getEcpfFechaEmision() {
		return ecpfFechaEmision;
	}

	public void setEcpfFechaEmision(String ecpfFechaEmision) {
		this.ecpfFechaEmision = ecpfFechaEmision;
	}

	public String getEcpfFechaCaduca() {
		return ecpfFechaCaduca;
	}

	public void setEcpfFechaCaduca(String ecpfFechaCaduca) {
		this.ecpfFechaCaduca = ecpfFechaCaduca;
	}

	public Integer getEcpfTipoFacturacion() {
		return ecpfTipoFacturacion;
	}

	public void setEcpfTipoFacturacion(Integer ecpfTipoFacturacion) {
		this.ecpfTipoFacturacion = ecpfTipoFacturacion;
	}

	public Integer getEcpfValidadorClick() {
		return ecpfValidadorClick;
	}

	public void setEcpfValidadorClick(Integer ecpfValidadorClick) {
		this.ecpfValidadorClick = ecpfValidadorClick;
	}

	public List<ComprobanteCSVDto> getEcpfListaComprobantePago() {
		return ecpfListaComprobantePago;
	}

	public void setEcpfListaComprobantePago(List<ComprobanteCSVDto> ecpfListaComprobantePago) {
		this.ecpfListaComprobantePago = ecpfListaComprobantePago;
	}

	public boolean isEcpfHabilitarGuardar() {
		return ecpfHabilitarGuardar;
	}

	public void setEcpfHabilitarGuardar(boolean ecpfHabilitarGuardar) {
		this.ecpfHabilitarGuardar = ecpfHabilitarGuardar;
	}
	
	
	
	
}
