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
 09-05-2017			 David Arellano                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.registro;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import ec.edu.uce.academico.ejb.dtos.RegistroDto;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RegistroAutomaticoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) RegistroAutomaticoForm. 
 * Bean de sesion que maneja el registro en bloque de los estudiantes en el sistema.
 * @author darellano.
 * @version 1.0
 */

@ManagedBean(name = "registroAutomaticoForm")
@SessionScoped
public class RegistroAutomaticoForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private RegistroDto rafRegistroDto;
	private Integer rafValidarArchivo;
	private InputStream rafArchivo;
	private List<RegistroDto> rafListRegistroDto;
	private Integer rafTipoUsuario;
	private Integer rafPracId;
	private List<PeriodoAcademico> rafListaPeriodoAcademico;
	
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
	FichaInscripcionDtoServicioJdbc servRafFichaInscripcionDto;
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

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Inicar parámetros para la funcionalidad
	 */
	public void iniciarParametros(){
		rafValidarArchivo = 0;
		rafListRegistroDto = null;
		rafRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los datos
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
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rafTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
					rafTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
					rafListaPeriodoAcademico = new ArrayList<PeriodoAcademico>();
					rafListaPeriodoAcademico = servRafPeriodoAcademico.listarTodosPosgradoActivo();
					if(rafListaPeriodoAcademico.size()==0){
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("No existen cohortes activas para cargar posgradistas.");
					}
				}else if(item.getUsroRol().getRolId() ==RolConstantes.ROL_ADMINDGA_VALUE.intValue()){
					rafTipoUsuario = RolConstantes.ROL_ADMINDGA_VALUE.intValue();
					
				}else if(item.getUsroRol().getRolId() ==RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue()){
				rafTipoUsuario = RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue();
				
			}
					
					
			}
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (UsuarioRolException e) {
		}
		iniciarParametros();
		return "irIniciarRegistro";
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		rafArchivo = null;
		rafRegistroDto = null;
		rafValidarArchivo = null;
		rafArchivo = null;
		rafListRegistroDto = null;
		try {
			if(rafArchivo != null){
				rafArchivo.close();
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
		rafArchivo = null; 
		iniciarParametros();
		try {
			if(rafArchivo != null){
				rafArchivo.close();
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
//			 Workbook libro = WorkbookFactory.create(rafArchivo);  // creo el libro excel del archivo
			Workbook libro = new XSSFWorkbook(rafArchivo);  // creo el libro excel del archivo
			 Sheet hoja = libro.getSheetAt(0); // selecciono la hoja nuemero 1
			 Row filaActual; // creo el objeto para la fila que va a ir recorriendo
			 Cell celdaActual; // creo el objeto para la celda que va a ir recorriendo
			
			 getRafListRegistroDto(); //creo la lista donde se guardará cada registro del excel a guardar en la base de datos
			 
			//validar que cada celda tenga datos y que el dato sea del tipo que se requiere
			Iterator<Row> itFilas = hoja.rowIterator();
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
					if(contCeldas <11 && itCeldas.hasNext() == false ){ // validacion que verifica si tiene algún dato en blanco la fila que se esta recorriendo
						FacesUtil.mensajeError("Existen datos en blanco en la fila: "+ (filaActual.getRowNum()+1));
						break recFilas;
					}
						
					int valAux = 0; //variable para asignar la validacion
					//evaluar en que columna se encuentra para enviar la validación correcta
					if(celdaActual.getColumnIndex() == 0 || celdaActual.getColumnIndex() == 5 || celdaActual.getColumnIndex() == 8
							|| celdaActual.getColumnIndex() == 9 || celdaActual.getColumnIndex() == 10 || celdaActual.getColumnIndex() == 11
							|| celdaActual.getColumnIndex() == 12){
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
							setearDatosEnRegistroDto(rafRegistroDto, celdaActual, contCeldas);
		
						}
						if(itCeldas.hasNext() == false ){
							rafListRegistroDto.add(rafRegistroDto); //agrego a la lista los registros del excel ya validados
							rafRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los dato
							
						}
						
						if(itCeldas.hasNext() == false && itFilas.hasNext() == false){
							FacesUtil.mensajeInfo("Validación exitosa");
							rafValidarArchivo = 2;
						}
					}else{
						//Error de verificación ya que val Aux debe tener un valor distinto de 0
						FacesUtil.mensajeError("Error al validar, por favor vuelva a cargar el archivo");
						break recFilas;
					}
				}

		}
			
		} catch (IOException e) {
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rafValidarArchivo = 1;
		} catch (EncryptedDocumentException e) {
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rafValidarArchivo = 1;
		} catch (RegistroAutomaticoException e) {
			FacesUtil.mensajeError(e.getMessage());
			rafValidarArchivo = 1;
		}
		
	}
	
	/**
	 * Metodo que genera el registro automatico 
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String generarRegistro(){
		try {
			//Inserto en la base de datos
			if(rafTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
				servRafRegistroAutomatico.generarRegistro(rafListRegistroDto);	
				FacesUtil.mensajeInfo("Usuarios creados exitosamente.");
			}else if(rafTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
				if(rafPracId!=null && rafPracId!=GeneralesConstantes.APP_ID_BASE){
					servRafRegistroAutomatico.generarRegistroPosgrado(rafListRegistroDto, rafPracId);
					FacesUtil.mensajeInfo("Usuarios creados exitosamente.");
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeWarn("Por favor seleccione el período académico.");
					return null;
				}
			}
		} catch (RegistroAutomaticoValidacionException e ) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RegistroAutomaticoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} finally {
			try {
				if(rafArchivo != null){
					rafArchivo.close();
				}
			} catch (IOException e) {
			}
		}
		return "irInicio";
	}
	

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	/**
	 * Metodo que permite guardar el archivo cargado en una variable 
	*/
	  public void handleFileUpload(FileUploadEvent event) {
		  try {
			  if(event != null){
				  rafArchivo = event.getFile().getInputstream();
				  rafValidarArchivo = 1; // archivo cargado correctamente
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
			  System.out.println(dato.getStringCellValue());
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
			  registro.setRgdArea(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  case 12:
			  registro.setRgdNuevo(Integer.parseInt(dato.getStringCellValue()));
			  break;
		  }
	  }
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	
	public RegistroDto getRafRegistroDto() {
		return rafRegistroDto;
	}
	public void setRafRegistroDto(RegistroDto rafRegistroDto) {
		this.rafRegistroDto = rafRegistroDto;
	}
	public Integer getRafValidarArchivo() {
		return rafValidarArchivo;
	}
	public void setRafValidarArchivo(Integer rafValidarArchivo) {
		this.rafValidarArchivo = rafValidarArchivo;
	}
	public InputStream getRafArchivo() {
		return rafArchivo;
	}
	public void setRafArchivo(InputStream rafArchivo) {
		this.rafArchivo = rafArchivo;
	}
	public List<RegistroDto> getRafListRegistroDto() {
		rafListRegistroDto = rafListRegistroDto==null?(new ArrayList<RegistroDto>()):rafListRegistroDto;
		return rafListRegistroDto;
	}
	public void setRafListRegistroDto(List<RegistroDto> rafListRegistroDto) {
		this.rafListRegistroDto = rafListRegistroDto;
	}

	public Integer getRafTipoUsuario() {
		return rafTipoUsuario;
	}

	public void setRafTipoUsuario(Integer rafTipoUsuario) {
		this.rafTipoUsuario = rafTipoUsuario;
	}

	public Integer getRafPracId() {
		return rafPracId;
	}

	public void setRafPracId(Integer rafPracId) {
		this.rafPracId = rafPracId;
	}

	public List<PeriodoAcademico> getRafListaPeriodoAcademico() {
		return rafListaPeriodoAcademico;
	}

	public void setRafListaPeriodoAcademico(List<PeriodoAcademico> rafListaPeriodoAcademico) {
		this.rafListaPeriodoAcademico = rafListaPeriodoAcademico;
	}
	
	
	
}
