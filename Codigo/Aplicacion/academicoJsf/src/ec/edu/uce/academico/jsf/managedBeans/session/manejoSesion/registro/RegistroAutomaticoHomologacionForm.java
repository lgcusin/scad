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
   
 ARCHIVO:     RegistroAutomaticoHomologacionForm.java	  
 DESCRIPCION: Bean de sesion que maneja el registro en bloque del los estudiantes a homologar. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-03-2018			 Marcelo Quishpe                         Emisión Inicial
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

import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.dtos.RegistroDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
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
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) RegistroAutomaticoForm. 
 * Bean de sesion que maneja el registro en bloque de los estudiantes en el sistema 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "registroAutomaticoHomologacionForm")
@SessionScoped
public class RegistroAutomaticoHomologacionForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private RegistroDto rahfRegistroDto;
	private Integer rahfValidarArchivo;
	private InputStream rahfArchivo;
	private List<RegistroDto> rahfListRegistroDto;
	private Integer rahfTipoUsuario;
	private Integer rahfPracId;
	private List<PeriodoAcademico> rahfListaPeriodoAcademico;
	private PeriodoAcademico rahfPeriodoAcademico;
	
	private Carrera rahfCarreraEstudiante;
	private Dependencia  rahfFaculadEstudiante;
	
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
	FichaInscripcionDtoServicioJdbc servRahfFichaInscripcionDto;
	@EJB 
	PeriodoAcademicoServicio servRahfPeriodoAcademico;
	@EJB 
	ConfiguracionCarreraServicio servRahfConfiguracionCarrera;
	@EJB
	CarreraServicio servRahfCarreraServicio;
	@EJB
	DependenciaServicio servRahfDependenciaServicio;
	@EJB 
	TipoFormacionServicio  servRahfTipoFormacion;
	@EJB 
	RegistroAutomaticoServicio  servRahfRegistroAutomatico;
	@EJB 
	UsuarioRolServicio  servUsuarioRolServicio;
	@EJB
	MatriculaServicioJdbc servRahfRecordEstudianteSAU;
	@EJB
	CarreraServicio servRahfCarrera;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Inicar parámetros para la funcionalidad
	 */
	public void iniciarParametros(){
		rahfValidarArchivo = 0;
		rahfListRegistroDto = null;
		rahfRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los datos
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
			if(item.getUsroRol().getRolId() ==RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue() ){
				rahfTipoUsuario = RolConstantes.ROL_ADMINFACULTAD_VALUE.intValue();
				
			}else if(item.getUsroRol().getRolId() ==RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				rahfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
				
			}
					
					
			}
		} catch (UsuarioRolNoEncontradoException e) {
		} catch (UsuarioRolException e) {
		}
		iniciarParametros();
		return "irIniciarRegistroHomologacion";
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		rahfArchivo = null;
		rahfRegistroDto = null;
		rahfValidarArchivo = null;
		rahfArchivo = null;
		rahfListRegistroDto = null;
		try {
			if(rahfArchivo != null){
				rahfArchivo.close();
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
		rahfArchivo = null; 
		iniciarParametros();
		try {
			if(rahfArchivo != null){
				rahfArchivo.close();
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
//			 Workbook libro = WorkbookFactory.create(rahfArchivo);  // creo el libro excel del archivo
			Workbook libro = new XSSFWorkbook(rahfArchivo);  // creo el libro excel del archivo
			 Sheet hoja = libro.getSheetAt(0); // selecciono la hoja nuemero 1
			 Row filaActual; // creo el objeto para la fila que va a ir recorriendo
			 Cell celdaActual; // creo el objeto para la celda que va a ir recorriendo
			
			 getRahfListRegistroDto(); //creo la lista donde se guardará cada registro del excel a guardar en la base de datos
			 
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
							setearDatosEnRegistroDto(rahfRegistroDto, celdaActual, contCeldas);
		
						}
						if(itCeldas.hasNext() == false ){
							rahfListRegistroDto.add(rahfRegistroDto); //agrego a la lista los registros del excel ya validados
							rahfRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los dato
							
						}
						
						if(itCeldas.hasNext() == false && itFilas.hasNext() == false){
							FacesUtil.mensajeInfo("Validación exitosa");
							rahfValidarArchivo = 2;
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
			rahfValidarArchivo = 1;
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rahfValidarArchivo = 1;
//		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
//			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
//			rahfValidarArchivo = 1;
		} catch (RegistroAutomaticoException e) {
			// TODO Auto-generated catch block
			FacesUtil.mensajeError(e.getMessage());
			rahfValidarArchivo = 1;
		}
		
	}
	
	/**
	 * Metodo que genera el registro automatico 
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String generarRegistro(){
		try {
			//Inserto en la base de datos
					
			if(rahfTipoUsuario==RolConstantes.ROL_ADMINFACULTAD_VALUE || rahfTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
				Boolean dentroRango= false;
				String identificacionError = null;
				for (RegistroDto registroDto : rahfListRegistroDto) {
				     if((registroDto.getFcinTipoIngreso()>=0)&&(registroDto.getFcinTipoIngreso()<=6)){ //TIPOS MOVILIDAD
				    	//Al momento no existe mas de 1 malla por carrera
				    	//MQ: Pedido de retirar opción Segunda Carrera UCE-DA- 2019-1734-O 9 jul 2019
				    	if(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE
				    		||	registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){  
				    					    		
				    		dentroRango= false;
				    		identificacionError= registroDto.getPrsIdentificacion();
					    	break;
				    	}else{				    	 
				    	 dentroRango = true;
				    	}
				     }else if ((registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTERCAMBIO_VALUE)
				    		 ||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_VALUE)
				    		 ||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE)
				    		 ||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE)){// 7:Intercambios, 8: Ubicacion Idiomas,  10: Suficiencia Cultura Fisica , 11: Cultura Fisica Intensivo
				    	 dentroRango = true;
				    	 
				     } else{
				    	 dentroRango= false;
				    	 identificacionError= registroDto.getPrsIdentificacion();
				    	 break;
				     }
				}
					

			//Verifico que ningun estudiante ya haya hecho pedido de movilidad en este periodo
			boolean existeInscripcionPeriodo = false; //verificar segun el tipo de ingreso que tenga o no ficha inscripcion con cambio de carrera
			String identificacionError4 = null;
			for (RegistroDto registroDto : rahfListRegistroDto) {
				
				rahfPeriodoAcademico= servRahfPeriodoAcademico.buscarPeriodo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				 if((registroDto.getFcinTipoIngreso()>=0)&&(registroDto.getFcinTipoIngreso()<=6)){ //TIPOS MOVILIDAD 
					 
					 
			    	  //Busco lista de fichas inscripcion 
			    	 List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
			    	  auxListaFichaInscripcion =servRahfFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(registroDto.getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE, GeneralesConstantes.APP_ID_BASE);
			    	  
			    	  if((auxListaFichaInscripcion==null)||(auxListaFichaInscripcion.size()<=0)){ 
			    		  //ESTUDIANTE NO TIENE FICHA INSCRIPCION ALGUNA  PUEDE SER REGISTRADO
			    		  existeInscripcionPeriodo = false;
			    	  }else{
			    		  for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
			    			  //BUSCO SI TIENE ALGUN CAMBIO DE CARRERA EN EL SIIU
							  if(fichaInscripcionDto.getFcinPeriodoAcademico()!=rahfPeriodoAcademico.getPracId()){
								  existeInscripcionPeriodo = false;
								  
							  }else{
								  existeInscripcionPeriodo= true;
					    		  identificacionError4= registroDto.getPrsIdentificacion();
							     break; 
							  }
						}
			    		  if(existeInscripcionPeriodo){
			    			  break; 
			    			   }  
			    		  
			    	  }
			    		
			    
			     } //Tipo Movilidad
			    	  
		    	}
			
			
			
			
			//verificar segun el tipo de ingreso que tenga o no ficha inscripcion en la carrera en la que se quiere cargar
				
				Boolean verificaFichaInscripcion= false; 
				String identificacionError2 = null;
				for (RegistroDto registroDto : rahfListRegistroDto) {
				     if((registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
				    		 ||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)){
				    	 verificaFichaInscripcion = true; //SOLO REINGRESOS Y REINICIOS PUEDEN CONTAR CON FICHA INSCRIPCION ANTERIOR
				     }else{  // CASOS DE SEGUNDA CARRERA, CAMBIO DE CARRERA, CAMBIO UNIVERSIDAD,CAMBIO DE MALLA, REINGRESO CON REDISEÑO-CAMBIO CRR POR REDISEÑO,
				    	     //INTERCAMBIOS, REGISTRO IDIOMAS, POSGRADO, CULTURA FISICA NO DEBEN ESTAR EN LA CARRERA.
				    	  List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
				    	  auxListaFichaInscripcion =servRahfFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(registroDto.getPrsIdentificacion(), registroDto.getFcinCarreraSiiu(), GeneralesConstantes.APP_ID_BASE);
				    	
				    	 
				    	  Carrera carreraAux = null;
				    	  List<RecordEstudianteSAUDto> recordEstudianteSauAux = new ArrayList<>();	
				    		carreraAux = servRahfCarrera.buscarPorId(registroDto.getFcinCarreraSiiu());
						   
				          //BUSCO RECORD EN SAU, EN LA CARRERA A LA QUE INGRESA
				           if(carreraAux.getCrrEspeCodigo()!=null){
				             recordEstudianteSauAux=servRahfRecordEstudianteSAU.buscarRecordAcademicoSAU(registroDto.getPrsIdentificacion(), carreraAux.getCrrEspeCodigo(), SAUConstantes.PERIODO_ACADEMICO_2017_2018_VALUE);
					         }
				    	  
				    	
				    	  if((auxListaFichaInscripcion==null)||(auxListaFichaInscripcion.size()<=0)){
				    		  
				    		  if(recordEstudianteSauAux==null||(auxListaFichaInscripcion.size()<=0)){
				    		  //ESTUDIANTE NO TIENE FICHA INSCRIPCION EN LA CARRERA en siiu y sau Y PUEDE SER REGISTRADO
				    		  verificaFichaInscripcion = true;
				    		  }else{
				    			  verificaFichaInscripcion= false;
					    		  identificacionError2= registroDto.getPrsIdentificacion();
							     break; 
				    			  
				    		  }
				    	  }else{
				    		 // 
				    		  verificaFichaInscripcion= false;
				    		  identificacionError2= registroDto.getPrsIdentificacion();
						     break; 
				    	  }
				    	  
				    					    	 
				     }
			    	}
				
				
				
				//Verifico que ningun estudiante ya haya hecho cambio de carrera antes.
				Boolean tieneCambioCarreraPrevio= false; //verificar segun el tipo de ingreso que tenga o no ficha inscripcion con cambio de carrera
				String identificacionError3 = null;
				for (RegistroDto registroDto : rahfListRegistroDto) {
				     if(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
				    	  //Busco lista de fichas inscripcion 
				    	 List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
				    	  auxListaFichaInscripcion =servRahfFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(registroDto.getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE, GeneralesConstantes.APP_ID_BASE);
				    	  
				    	  if((auxListaFichaInscripcion==null)||(auxListaFichaInscripcion.size()<=0)){ 
				    		  //ESTUDIANTE NO TIENE FICHA INSCRIPCION ALGUNA  PUEDE SER REGISTRADO
				    		  tieneCambioCarreraPrevio = false;
				    	  }else{
				    		  for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
				    			  //BUSCO SI TIENE ALGUN CAMBIO DE CARRERA EN EL SIIU
								  if(fichaInscripcionDto.getFcinTipoIngreso()!=FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
									  tieneCambioCarreraPrevio = false;
									  
								  }else{
									  tieneCambioCarreraPrevio= true;
						    		  identificacionError3= registroDto.getPrsIdentificacion();
								     break; 
								  }
							}
				    		  if(tieneCambioCarreraPrevio){
				    			  break; 
				    			   }  
				    		  
				    	  }
				    		
				     }
			    	}
				
				
				//Verifico que ningun estudiante ya haya hecho cambio de carrera antes.
				Boolean tieneCambioUniversidadPrevio= false; //verificar segun el tipo de ingreso que tenga o no ficha inscripcion con cambio de carrera
				String identificacionError5 = null;
				for (RegistroDto registroDto : rahfListRegistroDto) {
				     if(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
				    	  //Busco lista de fichas inscripcion 
				    	 List<FichaInscripcionDto> auxListaFichaInscripcion= new ArrayList<>();
				    	  auxListaFichaInscripcion =servRahfFichaInscripcionDto.buscarXidentificacionXcarreraXEstado(registroDto.getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE, GeneralesConstantes.APP_ID_BASE);
				    	  
				    	  if((auxListaFichaInscripcion==null)||(auxListaFichaInscripcion.size()<=0)){ 
				    		  //ESTUDIANTE NO TIENE FICHA INSCRIPCION ALGUNA  PUEDE SER REGISTRADO
				    		  tieneCambioUniversidadPrevio = false;
				    	  }else{
				    		  for (FichaInscripcionDto fichaInscripcionDto : auxListaFichaInscripcion) {
				    			  //BUSCO SI TIENE ALGUN CAMBIO DE CARRERA EN EL SIIU
								  if(fichaInscripcionDto.getFcinTipoIngreso()!=FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
									  tieneCambioUniversidadPrevio = false;
									  
								  }else{
									  tieneCambioCarreraPrevio= true;
						    		  identificacionError5= registroDto.getPrsIdentificacion();
								     break; 
								  }
							}
				    		  if(tieneCambioUniversidadPrevio){
				    			  break; 
				    			   }  
				    		  
				    	  }
				    		
				     }
			    	}
				
				
			if(!existeInscripcionPeriodo){
			if(verificaFichaInscripcion)	{
				
			 if(dentroRango==true){
				if(!tieneCambioUniversidadPrevio){	
					
				if(!tieneCambioCarreraPrevio){	
					
					servRahfRegistroAutomatico.generarRegistroHomologacion(rahfListRegistroDto)	;

					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.automatico.generar.registro.homologacion.validar.exito")));
					
					//ENVIAR MENSAJE A TODA LA LISTA SUBIDA, si el proceso anterior finaliza con exito
					
//					if(rahfListRegistroDto!=null && rahfListRegistroDto.size()>0 ){
//					for (RegistroDto registroDto : rahfListRegistroDto) {
//									
//					
//					//******************************************************************************
//					//************************* ACA INICIA EL ENVIO DE MAIL SIN ADJUNTO************************
//					//******************************************************************************
//					
//					if((registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE)
//						||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE)
//						||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE)
//						||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE)
//						||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE)
//						||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE)
//						||(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE)){
//						
//						//defino los datos para la plantilla
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
//					rahfFaculadEstudiante= servRahfDependenciaServicio.buscarDependenciaXcrrIdSinException(registroDto.getFcinCarreraSiiu());
//					rahfCarreraEstudiante= servRahfCarreraServicio.buscarPorIdSinException(registroDto.getFcinCarreraSiiu());
//					
//					if(rahfFaculadEstudiante!=null){
//					parametros.put("facultad", GeneralesUtilidades.generaStringConTildes(rahfFaculadEstudiante.getDpnDescripcion()));
//					}else{
//						parametros.put("facultad", " ");
//					}
//					
//					if(rahfCarreraEstudiante!=null){
//					parametros.put("carrera",GeneralesUtilidades.generaStringConTildes(rahfCarreraEstudiante.getCrrDetalle())); 
//					}else{
//						parametros.put("carrera", " ");
//					}
//					
//					if(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_CARRERA_LABEL)); 
//					}else if (registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_LABEL)); 
//					}else if (registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_UNIVERSIDAD_LABEL)); 
//					}else if (registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_REINICIO_LABEL)); 
//					}else if (registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_REINGRESO_CON_REDISENO_LABEL)); 
//					}else if (registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_CAMBIO_MALLA_LABEL)); 
//					}else if (registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_VALUE){
//						parametros.put("tipoIngreso",GeneralesUtilidades.generaStringConTildes(FichaInscripcionConstantes.TIPO_INGRESO_SEGUNDA_CARRERA_LABEL)); 
//					}else{
//						parametros.put("tipoIngreso"," "); 
//						
//					}
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
//					
//					} // FIN IF TIPO DE INGRESO
//					
//					}	// FIN FOR
//				
//					} // FIN LISTA CON DATOS
				         }else{ // tiene cambio de carrera previo
					          FacesUtil.limpiarMensaje();
					           FacesUtil.mensajeError(identificacionError3+":  El estudiante tiene ya un cambio de carrera anterior, no se ha ingresado estudiante alguno.");
					
				              }
				
				}else{ //tiene cambio de universidad previo
					
					FacesUtil.limpiarMensaje();
			           FacesUtil.mensajeError(identificacionError5+":  El estudiante tiene ya un cambio de universidad anterior, no se ha ingresado estudiante alguno.");
					
				}
				
				
				      }else{// esta dentro del rango de tipos de ingreso validos
					            FacesUtil.limpiarMensaje();
					        FacesUtil.mensajeError(identificacionError+":  Todos los estudiantes deben estar con el tipo de ingreso correcto para homologacion, no se ha ingresado estudiante alguno.");
				          }
			
			    }else{ //ya tiene fichaInscripcion en la carrera
				    FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(identificacionError2+" : El estudiante ya presenta inscripción en la carrera verifique SIIU y SAU, no se ha ingresado estudiante alguno.");
				 
			      }
			
			   }else{
				   FacesUtil.limpiarMensaje();
				   FacesUtil.mensajeError(identificacionError4+" : El estudiante ya presenta registro de movilidad en este período activo, no se ha ingresado estudiante alguno.");
				
			      }
			
			}else{
				FacesUtil.mensajeError("No se cargo el archivo, Usted no tiene permisos para crear usuarios, no se ha ingresado estudiante alguno.");
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
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar ficha inscripción");
		} catch (PeriodoAcademicoNoEncontradoException e) {
			// TODO Auto-generated catch block
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontro período academico activo");
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar período acádemico activo");
		} catch (PeriodoAcademicoValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CarreraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordEstudianteNoEncontradoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordEstudianteException e) {
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
				if(rahfArchivo != null){
					rahfArchivo.close();
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
				  rahfArchivo = event.getFile().getInputstream();
				  rahfValidarArchivo = 1; // archivo cargado correctamente
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
	
	
	public RegistroDto getRahfRegistroDto() {
		return rahfRegistroDto;
	}
	public void setRahfRegistroDto(RegistroDto rahfRegistroDto) {
		this.rahfRegistroDto = rahfRegistroDto;
	}
	public Integer getRahfValidarArchivo() {
		return rahfValidarArchivo;
	}
	public void setRahfValidarArchivo(Integer rahfValidarArchivo) {
		this.rahfValidarArchivo = rahfValidarArchivo;
	}
	public InputStream getRahfArchivo() {
		return rahfArchivo;
	}
	public void setRahfArchivo(InputStream rahfArchivo) {
		this.rahfArchivo = rahfArchivo;
	}
	public List<RegistroDto> getRahfListRegistroDto() {
		rahfListRegistroDto = rahfListRegistroDto==null?(new ArrayList<RegistroDto>()):rahfListRegistroDto;
		return rahfListRegistroDto;
	}
	public void setRahfListRegistroDto(List<RegistroDto> rahfListRegistroDto) {
		this.rahfListRegistroDto = rahfListRegistroDto;
	}

	public Integer getRahfTipoUsuario() {
		return rahfTipoUsuario;
	}

	public void setRahfTipoUsuario(Integer rahfTipoUsuario) {
		this.rahfTipoUsuario = rahfTipoUsuario;
	}

	public Integer getRahfPracId() {
		return rahfPracId;
	}

	public void setRahfPracId(Integer rahfPracId) {
		this.rahfPracId = rahfPracId;
	}

	public List<PeriodoAcademico> getRahfListaPeriodoAcademico() {
		return rahfListaPeriodoAcademico;
	}

	public void setRahfListaPeriodoAcademico(List<PeriodoAcademico> rahfListaPeriodoAcademico) {
		this.rahfListaPeriodoAcademico = rahfListaPeriodoAcademico;
	}

	public Carrera getRahfCarreraEstudiante() {
		return rahfCarreraEstudiante;
	}

	public void setRahfCarreraEstudiante(Carrera rahfCarreraEstudiante) {
		this.rahfCarreraEstudiante = rahfCarreraEstudiante;
	}

	public Dependencia getRahfFaculadEstudiante() {
		return rahfFaculadEstudiante;
	}

	public void setRahfFaculadEstudiante(Dependencia rahfFaculadEstudiante) {
		this.rahfFaculadEstudiante = rahfFaculadEstudiante;
	}

	public PeriodoAcademico getRahfPeriodoAcademico() {
		return rahfPeriodoAcademico;
	}

	public void setRahfPeriodoAcademico(PeriodoAcademico rahfPeriodoAcademico) {
		this.rahfPeriodoAcademico = rahfPeriodoAcademico;
	}
	
	
	
}
