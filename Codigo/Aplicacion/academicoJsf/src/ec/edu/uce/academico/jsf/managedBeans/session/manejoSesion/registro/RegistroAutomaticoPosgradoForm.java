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
   
 ARCHIVO:     RegistroAutomaticoPosgradoForm.java	  
 DESCRIPCION: Bean de sesion que maneja el registro en bloque del los estudiantes a homologar. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 03-10-2018			 Marcelo Quishpe                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.registro;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MultivaluedHashMap;
//import javax.ws.rs.core.MultivaluedMap;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.RegistroDto;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RegistroAutomaticoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
//import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
//import ec.edu.uce.academico.jsf.utilidades.GeneradorMails;
//import ec.edu.uce.envioMail.excepciones.ValidacionMailException;

/**
 * Clase (session bean) RegistroAutomaticoPosgradoForm. 
 * Bean de sesion que maneja el registro en bloque de los estudiantes en el sistema 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "registroAutomaticoPosgradoForm")
@SessionScoped
public class RegistroAutomaticoPosgradoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private RegistroDto rapfRegistroDto;
	private Integer rapfValidarArchivo;
	private InputStream rapfArchivo;
	private List<RegistroDto> rapfListRegistroDto;
	private Integer rapfTipoUsuario;
	private Integer rapfPracId;
	private List<PeriodoAcademico> rapfListaPeriodoAcademico;
	
	private Carrera rapfCarreraEstudiante;
	private Dependencia  rapfFaculadEstudiante;
	
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
	FichaInscripcionDtoServicioJdbc servRapfFichaInscripcionDto;
	@EJB 
	PeriodoAcademicoServicio servRapfPeriodoAcademico;
	@EJB 
	ConfiguracionCarreraServicio servRapfConfiguracionCarrera;
	@EJB
	CarreraServicio servRapfCarreraServicio;
	@EJB
	DependenciaServicio servRapfDependenciaServicio;
	@EJB 
	TipoFormacionServicio  servRapfTipoFormacion;
	@EJB 
	RegistroAutomaticoServicio  servRapfRegistroAutomatico;
	@EJB 
	UsuarioRolServicio  servUsuarioRolServicio;


	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Inicar parámetros para la funcionalidad
	 */
	public void iniciarParametros(){
		rapfValidarArchivo = 0;
		rapfListRegistroDto = null;
		rapfRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los datos
	}
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de matricula
	 * @return navegacion al listar matricula
	 */
	public String iniciarRegistroForm(Usuario usuario) {
		List<UsuarioRol> usro;
		try {
			usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
			 if(item.getUsroRol().getRolId() ==RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				rapfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
				
			}
					
			}
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (UsuarioRolException e) {
		}
		iniciarParametros();
		return "irIniciarRegistroPosgrado";
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		rapfArchivo = null;
		rapfRegistroDto = null;
		rapfValidarArchivo = null;
		rapfArchivo = null;
		rapfListRegistroDto = null;
		try {
			if(rapfArchivo != null){
				rapfArchivo.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return "irInicio";
	}
	
	/**
	 * Limpia los parámetros de la funcionalidad
	 * @return  Navegacion que recarga la página.
	 */
	public void cambiarArchivo(){
		FacesUtil.limpiarMensaje();
		rapfArchivo = null; 
		iniciarParametros();
		try {
			if(rapfArchivo != null){
				rapfArchivo.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	/**
	 * Valida que el Excel este en el formato correcto y guarda los datos del archivo en un objeto
	 */
	public void validarArchivo(){
		try {
//			 Workbook libro = WorkbookFactory.create(rapfArchivo);  // creo el libro excel del archivo
			Workbook libro = new XSSFWorkbook(rapfArchivo);  // creo el libro excel del archivo
			 Sheet hoja = libro.getSheetAt(0); // selecciono la hoja nuemero 1
			 Row filaActual; // creo el objeto para la fila que va a ir recorriendo
			 Cell celdaActual; // creo el objeto para la celda que va a ir recorriendo
			
			 getRapfListRegistroDto(); //creo la lista donde se guardará cada registro del excel a guardar en la base de datos
			 
			//validar que cada celda tenga datos y que el dato sea del tipo que se requiere
			Iterator<Row> itFilas = hoja.rowIterator();
		
			if(itFilas.hasNext()){
			itFilas.next();
			int contCeldas;// variable para verificar que el numero de celdas validadas sea 18
			Integer tipoId = GeneralesConstantes.APP_ID_BASE; //variable para guardar el tipo de identificacion y enviar al validar de cédula
			recFilas:while (itFilas.hasNext()) { //recorro las filas 
				filaActual = itFilas.next();
				Iterator<Cell> itCeldas = filaActual.cellIterator();
				contCeldas = 0;
				while (itCeldas.hasNext()) { // recorro las celdas de esa fila
					contCeldas++; //aumento el contador para verificar que valide 12 columnas
					celdaActual= itCeldas.next(); //guardo la celda en una variable
					if(contCeldas < 11 && itCeldas.hasNext() == false ){ // validacion que verifica si tiene algún dato en blanco la fila que se esta recorriendo  
						FacesUtil.mensajeError("Existen datos en blanco en la fila: "+ (filaActual.getRowNum()+1));
						break recFilas;
					}
						
					int valAux = 0; //variable para asignar la validacion
					//evaluar en que columna se encuentra para enviar la validación correcta
					if(celdaActual.getColumnIndex() == 0 || celdaActual.getColumnIndex() == 5 || celdaActual.getColumnIndex() == 8
							|| celdaActual.getColumnIndex() == 9 || celdaActual.getColumnIndex() == 10 
							){
						//validar numero
						valAux = 4;
					}else if(celdaActual.getColumnIndex() == 2 || celdaActual.getColumnIndex() == 3 || celdaActual.getColumnIndex() == 4 ){
						//validar string 
						valAux = 2;
					} else if(celdaActual.getColumnIndex() == 6 || celdaActual.getColumnIndex() == 7){
						//validar mail
						valAux = 3;
					} else if(celdaActual.getColumnIndex() == 1){
						//validar cedula
						valAux = 1;
					}
							
					if(valAux !=0){ // verifico que todos los datos pasen por la validacion
						try {
							celdaActual.getStringCellValue();
						} catch (IllegalStateException e) {
							throw new RegistroAutomaticoException("El tipo de dato en la columna : "+ (GeneralesUtilidades.obtenerLetraColumna(celdaActual.getColumnIndex())) + " fila: "+(filaActual.getRowNum()+1)+" no es correcto" );
						}
						if(celdaActual.getColumnIndex() != (contCeldas-1)){
							//Error de del tipo de identificacion
							FacesUtil.mensajeError("Existen datos en blanco en la fila: " +(filaActual.getRowNum()+1));
							break recFilas;
						}
						if(GeneralesUtilidades.validarXtipoDato(valAux, celdaActual.getStringCellValue(), tipoId) != true){
							FacesUtil.mensajeError("Los datos ingresados en la fila: "+(filaActual.getRowNum()+1) +"  columna : " + (GeneralesUtilidades.obtenerLetraColumna(celdaActual.getColumnIndex()))+ " no corresponde al tipo de dato que debería tener");
							break recFilas; 
						}else{
							//guardo el tipo de identificacion como variable
							if(contCeldas == 1){
								try {
									tipoId = Integer.parseInt(celdaActual.getStringCellValue());
								} catch (NumberFormatException e) {
									//Error de del tipo de identificacion
									FacesUtil.mensajeError("Existen datos en blanco en la fila: " +(filaActual.getRowNum()+1));
									break recFilas;
								}
							}
							
							
							//guardar el dato que no paso por validación
							setearDatosEnRegistroDto(rapfRegistroDto, celdaActual, contCeldas);
		
						}
						if(itCeldas.hasNext() == false ){
							rapfListRegistroDto.add(rapfRegistroDto); //agrego a la lista los registros del excel ya validados
							rapfRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los dato
							
						}
						
						if(itCeldas.hasNext() == false && itFilas.hasNext() == false){
							FacesUtil.mensajeInfo("Validación exitosa");
							rapfValidarArchivo = 2;
						}
					}else{
						//Error de verificación ya que val Aux debe tener un valor distinto de 0
						FacesUtil.mensajeError("Error al validar, por favor vuelva a cargar el archivo");
						break recFilas;
					}
				}

		}
		
		}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al validar el archivo, la primera hoja de excel esta vacia");
				
			}	
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rapfValidarArchivo = 1;
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rapfValidarArchivo = 1;
//		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
//			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
//			rapfValidarArchivo = 1;
		} catch (RegistroAutomaticoException e) {
			// TODO Auto-generated catch block
			FacesUtil.mensajeError(e.getMessage());
			rapfValidarArchivo = 1;
		}
		
	}
	
	/**
	 * Metodo que genera el registro automatico 
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String generarRegistro(){
		try {
			//Inserto en la base de datos
			if(rapfTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
				Boolean dentroRango= false;
				for (RegistroDto registroDto : rapfListRegistroDto) {
				     if(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_VALUE){
				    	dentroRango = true;
				    	 
				     }else{
				    	 dentroRango= false;
				    	 break;
				     }
				}
					
				
				
				
				Boolean verificaFichaInscripcion= false; //verificar segun si existe ficha inscripcion en el posgrado en la que se quiere cargar
				String identificacionError2 = null;
				for (RegistroDto registroDto : rapfListRegistroDto) {
				       // POSGRADO
				    	  List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
				    	  auxListaFichaInscripcion =servRapfFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(registroDto.getPrsIdentificacion(), registroDto.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
				    	  if((auxListaFichaInscripcion==null)||(auxListaFichaInscripcion.size()<=0)){
				    		  //ESTUDIANTE NO TIENE FICHA INSCRIPCION EN LA CARRERA Y PUEDE SER REGISTRADO
				    		  verificaFichaInscripcion = true;
				    	  }else{
				    		 // 
				    		  verificaFichaInscripcion= false;
				    		  identificacionError2= registroDto.getPrsIdentificacion();
						     break; 
				    	  }
				    					    	 
				     
			    	}
				
				
				
			if(verificaFichaInscripcion)	{
				
				if(dentroRango==true){
			
					servRapfRegistroAutomatico.generarRegistroHomologacionPosgrado(rapfListRegistroDto)	;

					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.generar.registro.homologacion.validar.exito")));
					
					//ENVIAR MENSAJE A TODA LA LISTA SUBIDA, si el proceso anterior finaliza con exito
					
//					if(rapfListRegistroDto!=null && rapfListRegistroDto.size()>0 ){
//					for (RegistroDto registroDto : rapfListRegistroDto) {
									
					
					//******************************************************************************
					//************************* ACA INICIA EL ENVIO DE MAIL SIN ADJUNTO************************
					//******************************************************************************
					
											
						//defino los datos para la plantilla
//					Map<String, Object> parametros = new HashMap<String, Object>();
//					
//							//DATOS DEL ESTUDIANTE			
//					parametros.put("estudiante", registroDto.getPrsNombres()+" "+registroDto.getPrsPrimerApellido().toUpperCase()+" "
//							+(registroDto.getPrsSegundoApellido() == null?" ":registroDto.getPrsSegundoApellido()));
//					
//						
//					
//					SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
//					//SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a' 'las' HH:mm:ss", new Locale("es", "ES"));
//					parametros.put("fechaHora", sdf.format(new Date()));
//					parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
//					
//					rapfFaculadEstudiante= servRapfDependenciaServicio.buscarDependenciaXcrrIdSinException(registroDto.getFcinCarreraSiiu());
//					rapfCarreraEstudiante= servRapfCarreraServicio.buscarPorIdSinException(registroDto.getFcinCarreraSiiu());
//					
//					if(rapfFaculadEstudiante!=null){
//					parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(rapfFaculadEstudiante.getDpnDescripcion()));
//					}else{
//						parametros.put("facultad", " ");
//					}
//					
//					if(rapfCarreraEstudiante!=null){
//					parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(rapfCarreraEstudiante.getCrrDetalle())); 
//					}else{
//						parametros.put("carrera", " ");
//					}
//					
//					parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_POSGRADO_LABEL)); 
//					
//					//lista de correos a los que se enviara el mail
//					List<String> correosTo = new ArrayList<>();
//					correosTo.add(registroDto.getPrsMailInstitucional());
//					
//					//path de la plantilla del mail
//					String pathTemplate = "/ec/edu/uce/academico/jsf/velocity/plantillas/template-movilidad-estudiantil.vm";
//					
//					//llamo al generador de mails
//					GeneradorMails genMail = new GeneradorMails();
//					String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_MOVILIDAD_ESTUDIANTIL,GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
//					
//					//****envio el mail a la cola
//					//cliente web service
//					Client client = ClientBuilder.newClient();
//					WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
//					MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
//					postForm.add("mail", mailjsonSt);
////					String responseData = target.request().post(Entity.form(postForm),String.class);
//					target.request().post(Entity.form(postForm),String.class);
//					
//					//******************************************************************************
//					//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
//					//******************************************************************************	
//				
					
					
					
//					}	// FIN FOR
				
//				} // FIN LISTA CON DATOS
				
				
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("Todos los estudiantes deben estar con el tipo de ingreso correcto para posgrado, no se ha ingresado estudiante alguno");
				}
			
			}else{
				
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(identificacionError2+" : El estudiante ya presenta ficha inscripción en la carrera, verifique el tipo de ingreso.");
				
			}
			
			}else{
				FacesUtil.mensajeError("No se cargo el archivo, no tiene permisos para crear usuarios");
			}
		
		} catch (RegistroAutomaticoValidacionException e ) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RegistroAutomaticoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (FichaInscripcionDtoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		catch (ValidacionMailException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//			
//		}  
		finally {
			try {
				if(rapfArchivo != null){
					rapfArchivo.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		return "irInicio";
	}
	

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	/**
	 * Metodo que permite guardar el archivo Excel cargado en una variable 
	*/
	  public void handleFileUpload(FileUploadEvent event) {
		  try {
			  if(event != null){
				  rapfArchivo = event.getFile().getInputstream();
				  rapfValidarArchivo = 1; // archivo cargado correctamente
				  FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName() + " se cargó con éxito");  
			  }else{
				  FacesUtil.mensajeError("Archivo erroneo");
			  }
		  
		  } catch (IOException e) {
				// TODO Auto-generated catch block
				FacesUtil.mensajeError("Error al intentar cargar el archivo, por favor intente nuevamente");
			}
		  
	    }
	  
	  
	  /**
	   * Setea los datos del Dto 
	   * @param registro - DTO de registro para llenar los datos
	   * @param dato - dato a ingresar
	   * @param numeroDato - numero de celda para saber que dato debe setear
	   */
	  private void setearDatosEnRegistroDto(RegistroDto registro, Cell dato , int numeroDato ) {
		  switch (numeroDato){
		  case 1:
			  registro.setPrsTipoIdentificacion(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  case 2:
			  registro.setPrsIdentificacion(dato.getStringCellValue());
			  break;
		  case 3:
			  registro.setPrsPrimerApellido(dato.getStringCellValue());
			  break;
		  case 4:
			  registro.setPrsSegundoApellido(dato.getStringCellValue());
			  break;
		  case 5:
			  registro.setPrsNombres(dato.getStringCellValue());
			  break;
		  case 6:
			  registro.setPrsSexo(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  case 7:
			  registro.setPrsMailPersonal(dato.getStringCellValue());
			  break;
		  case 8:
			  registro.setPrsMailInstitucional(dato.getStringCellValue());
			  break;
		  case 9:
			  registro.setFcinNotaEnes(Float.parseFloat(dato.getStringCellValue()));
			  break;
		  case 10:
			  registro.setFcinCarreraSiiu(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  case 11:
			  registro.setFcinTipoIngreso(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  case 12:
			  registro.setFcinTipoUniversidad(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  
		  }
	  }
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	
	public RegistroDto getRapfRegistroDto() {
		return rapfRegistroDto;
	}
	public void setRapfRegistroDto(RegistroDto rapfRegistroDto) {
		this.rapfRegistroDto = rapfRegistroDto;
	}
	public Integer getRapfValidarArchivo() {
		return rapfValidarArchivo;
	}
	public void setRapfValidarArchivo(Integer rapfValidarArchivo) {
		this.rapfValidarArchivo = rapfValidarArchivo;
	}
	public InputStream getRapfArchivo() {
		return rapfArchivo;
	}
	public void setRapfArchivo(InputStream rapfArchivo) {
		this.rapfArchivo = rapfArchivo;
	}
	public List<RegistroDto> getRapfListRegistroDto() {
		rapfListRegistroDto = rapfListRegistroDto==null?(new ArrayList<RegistroDto>()):rapfListRegistroDto;
		return rapfListRegistroDto;
	}
	public void setRapfListRegistroDto(List<RegistroDto> rapfListRegistroDto) {
		this.rapfListRegistroDto = rapfListRegistroDto;
	}

	public Integer getRapfTipoUsuario() {
		return rapfTipoUsuario;
	}

	public void setRapfTipoUsuario(Integer rapfTipoUsuario) {
		this.rapfTipoUsuario = rapfTipoUsuario;
	}

	public Integer getRapfPracId() {
		return rapfPracId;
	}

	public void setRapfPracId(Integer rapfPracId) {
		this.rapfPracId = rapfPracId;
	}

	public List<PeriodoAcademico> getRapfListaPeriodoAcademico() {
		return rapfListaPeriodoAcademico;
	}

	public void setRapfListaPeriodoAcademico(List<PeriodoAcademico> rapfListaPeriodoAcademico) {
		this.rapfListaPeriodoAcademico = rapfListaPeriodoAcademico;
	}

	public Carrera getRapfCarreraEstudiante() {
		return rapfCarreraEstudiante;
	}

	public void setRapfCarreraEstudiante(Carrera rapfCarreraEstudiante) {
		this.rapfCarreraEstudiante = rapfCarreraEstudiante;
	}

	public Dependencia getRapfFaculadEstudiante() {
		return rapfFaculadEstudiante;
	}

	public void setRapfFaculadEstudiante(Dependencia rapfFaculadEstudiante) {
		this.rapfFaculadEstudiante = rapfFaculadEstudiante;
	}
	
	
	
}
