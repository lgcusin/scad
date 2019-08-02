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
   
 ARCHIVO:     RegistroAutomaticoUbicacionIdiomasForm.java	  
 DESCRIPCION: Bean de sesion que maneja el registro en bloque del los estudiantes a un nivel de idioma luego de la prueba de ubicación. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-07-2018			 Marcelo Quishpe                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.registro;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RegistroDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroAutomaticoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PlanificacionCronogramaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RegistroAutomaticoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaInscripcionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.FichaMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) RegistroAutomaticoUbicacionIdiomasForm. 
 * Bean de sesion que maneja el registro en bloque de los estudiantes en el sistema a a un nivel de idioma luego de la prueba de ubicación.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "registroAutomaticoUbicacionIdiomasForm")
@SessionScoped
public class RegistroAutomaticoUbicacionIdiomasForm implements Serializable {
	
	private static final long serialVersionUID = 1881903750536586483L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private RegistroDto rauifRegistroDto;
	private Integer rauifValidarArchivo;
	private InputStream rauifArchivo;
	private List<RegistroDto> rauifListRegistroDto;
	private Integer rauifTipoUsuario;
	private Integer rauifPracId;
	private List<PeriodoAcademico> rauifListaPeriodoAcademico;
	private PlanificacionCronograma rauifPlanificacionCronograma;
	
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
	FichaInscripcionDtoServicioJdbc servRauifFichaInscripcionDto;
	@EJB 
	PeriodoAcademicoServicio servRauifPeriodoAcademico;
	@EJB 
	ConfiguracionCarreraServicio servRauifConfiguracionCarrera;
	@EJB 
	TipoFormacionServicio  servRauifTipoFormacion;
	@EJB 
	RegistroAutomaticoServicio  servRauifRegistroAutomatico;
	@EJB 
	UsuarioRolServicio  servRauifUsuarioRolServicio;
	
	@EJB
	CarreraServicio servRauifCarreraServicio;
	@EJB
	MallaCurricularServicio servRauifMallaCurricular;
	@EJB
	MateriaDtoServicioJdbc servRauifMateriaDto;
	@EJB
	ParaleloDtoServicioJdbc servRauifParaleloDto;
	@EJB
	PlanificacionCronogramaServicio servRauifPlanificacionCronograma;

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	
	/**
	 * Inicar parámetros para la funcionalidad
	 */
	public void iniciarParametros(){
		rauifValidarArchivo = 0;
		rauifListRegistroDto = null;
		rauifRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los datos
	}
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad de de ubicación automática
	 * @return navegacion IniciarRegistroUbicacion
	 */
	public String iniciarRegistroUbicacionForm(Usuario usuario) {
		List<UsuarioRol> usro;
		try {
			usro = servRauifUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
			 if(item.getUsroRol().getRolId() ==RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				  rauifTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
			     }
			}
			
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("El usuario no tiene roles asignados");
		
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar roles del usuario");
		}
		iniciarParametros();
		return "irIniciarRegistroUbicacion";
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		rauifArchivo = null;
		rauifRegistroDto = null;
		rauifValidarArchivo = null;
		rauifArchivo = null;
		rauifListRegistroDto = null;
		try {
			if(rauifArchivo != null){
				rauifArchivo.close();
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
		rauifArchivo = null; 
		iniciarParametros();
		try {
			if(rauifArchivo != null){
				rauifArchivo.close();
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
//			 Workbook libro = WorkbookFactory.create(rauifArchivo);  // creo el libro excel del archivo
			Workbook libro = new XSSFWorkbook(rauifArchivo);  // creo el libro excel del archivo
			 Sheet hoja = libro.getSheetAt(0); // selecciono la hoja nuemero 1
			 Row filaActual; // creo el objeto para la fila que va a ir recorriendo
			 Cell celdaActual; // creo el objeto para la celda que va a ir recorriendo
			
			 getRauifListRegistroDto(); //creo la lista donde se guardará cada registro del excel a guardar en la base de datos
			 
			//validar que cada celda tenga datos y que el dato sea del tipo que se requiere
			Iterator<Row> itFilas = hoja.rowIterator();
			if(itFilas.hasNext()){ //devuelve verdadero si existe al menos una fila
			
			itFilas.next(); //salto la primera fila debido a que son cabeceras
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
					if(celdaActual.getColumnIndex() == 0 || celdaActual.getColumnIndex() == 5 
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
					} else if(celdaActual.getColumnIndex() == 8){
						//validar nota ubicacion
						valAux = 6;
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
										
							
								//validar
								try {
									setearDatosEnRegistroDto(rauifRegistroDto, celdaActual, contCeldas);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
									FacesUtil.mensajeError(e.getMessage());//catch añadido 12/04/2019
									break recFilas;
								}
							
							
		
						}
						if(itCeldas.hasNext() == false ){
							rauifListRegistroDto.add(rauifRegistroDto); //agrego a la lista los registros del excel ya validados
							rauifRegistroDto = new RegistroDto(); // creo el objeto para ir seteando los dato
							
						}
						
						if(itCeldas.hasNext() == false && itFilas.hasNext() == false){
							FacesUtil.mensajeInfo("Validación exitosa");
							rauifValidarArchivo = 2;
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
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rauifValidarArchivo = 1;
		} catch (EncryptedDocumentException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			rauifValidarArchivo = 1;
//		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
//			FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
//			rauifValidarArchivo = 1;
		} catch (RegistroAutomaticoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			rauifValidarArchivo = 1;
		}
		
	}
	
	/**
	 * Metodo que genera el registro automatico 
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String generarRegistroUbicacion(){
		try {
			//Lista de carreras que tienen autorizado el ingreso de estudiantes directo a carrera
			//Ingles, frances, Italiano, Coreano, Kichwa
			List<Integer> listaCarrerasPermitidas= Arrays.asList(185,186,187,188,189);
			//Inserto en la base de datos 
			//Verifico que el usuario tenga permiso para cargar el archivo
			if(rauifTipoUsuario==RolConstantes.ROL_SOPORTE_VALUE){
			
				//Verifico que la lsita tenga el tipo de ingreso correcto para directos a carrera
				boolean dentroRango= false;
				for (RegistroDto registroDto : rauifListRegistroDto) {
				     if(registroDto.getFcinTipoIngreso()==FichaInscripcionConstantes.TIPO_INGRESO_UBICACION_IDIOMAS_AUTOMATICO_VALUE){
				    	dentroRango = true;
				    	 
				     }else{
				    	 dentroRango= false;
				    	 break;
				     }
				     
				}
				
				 boolean dentroCarrerasPermitidas= false;
				
				 for (RegistroDto registroDto : rauifListRegistroDto) {
				//Verifico que todos los estudiantes tengan alguna carrera de idiomas
			     int contadorLista=0;
			     for (int idCarrera : listaCarrerasPermitidas) {
			    	contadorLista++;
			    	 if(registroDto.getFcinCarreraSiiu()==idCarrera){
				    	 dentroCarrerasPermitidas= true;
				    	 break;				    	 
				     }else{
				    	 dentroCarrerasPermitidas= false;
				    	
				     }
			    	 
			    
			     }
			    	 
				 if(!dentroCarrerasPermitidas&&listaCarrerasPermitidas.size()==contadorLista){
		    		 break;
		    	 } 
				    	 
				}
				
				// proceso para buscar la planificacion cronograma activa
					// creo la lista de proceso flujo correspondiente a la matricula
					List<Integer> listProcesoFlujoAux = new ArrayList<Integer>();
					listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE);
					listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE);
					listProcesoFlujoAux.add(ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE);
					
					// Busqueda del período academico HOMOLOGACION
					PeriodoAcademico periodoAcademicoAux = servRauifPeriodoAcademico.buscarPDescripcion(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_LABEL);
					
				
					// listo planificaciones cronograma de matriculas

					// TODO: VERIFICAR EL TIPO DE CRONOGRAMA
					List<PlanificacionCronograma> listPlanificacionCronogramaAux;
					listPlanificacionCronogramaAux = servRauifPlanificacionCronograma
							.buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(periodoAcademicoAux.getPracId(),
									listProcesoFlujoAux, CronogramaConstantes.TIPO_HOMOLOGACION_VALUE);
					
					
					// validacion de las fechas del cronograma
					int cont = 0;
					int contNumPlanificacion = 0;
					for (PlanificacionCronograma item : listPlanificacionCronogramaAux) {
						contNumPlanificacion++;
						if (item.getPlcrEstado() == PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE) {
							rauifPlanificacionCronograma = item;
										cont++;
						}
						if (cont == 0 && contNumPlanificacion == listPlanificacionCronogramaAux.size()) {
							// TODO: GENERAR EL MENSAJE
							// FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GenerarMatricula.irGenerarMatricula.encuesta.validacion.exception")));
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError("No existen planificacion cronomograma activo, por favor comuniquese con el administrador del sistema");
							return null;
						}
					}
 
					
				 //************** Valores a ingresar en tabla administrable
//					BigDecimal valorMin1 = new BigDecimal(00.00); //VA A PRIMER NIVEL
//				    BigDecimal valorMax1 = new BigDecimal(17.50);
//				    
//				    BigDecimal valorMin2 = new BigDecimal(17.51);//VA A SEGUNDO NIVEL, APRUEBA PRIMERO
//				    BigDecimal valorMax2 = new BigDecimal(35.70);
//				    
//				    BigDecimal valorMin3 = new BigDecimal(35.71); //VA A TERCER NIVEL, APRUEBA HASTA SEGUNDO
//				    BigDecimal valorMax3 = new BigDecimal(53.20);
//				    
//				    BigDecimal valorMin4 = new BigDecimal(53.21);  //VA A CUARTO NIVEL, APRUEBA HASTA TERCERO
//				    BigDecimal valorMax4 = new BigDecimal(63.00);
//				    
//				    BigDecimal valorMin5 = new BigDecimal(63.01);  //VA A QUINTO NIVEL, APRUEBA HASTA CUARTO
//				    BigDecimal valorMax5 = new BigDecimal(70.00);
					
				//***********************************************	
				    
					//CREO ARCHIVO PLANO PARA GUARDAR VALORES
				    Properties prop = new Properties();
					InputStream is = null;
					
					StringBuilder pathGeneralRequisitos = new StringBuilder();
                    pathGeneralRequisitos.append(FacesContext.getCurrentInstance()
                            .getExternalContext().getRealPath("/"));
                    pathGeneralRequisitos.append("/academico/reportes/archivosPlanos/variablesUbicacionIdiomas.properties");    
					
					try {
						is = new FileInputStream(pathGeneralRequisitos.toString());
						prop.load(is);
					} catch(IOException e) {
						//System.out.println(e.toString());
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Error desconocido al abrir el archivo de valores de ubicación, comuníquese con el administrador del sistema"); 
						return null;  
					}
				   
				   
				    BigDecimal valorMin1 = new BigDecimal(prop.getProperty("valorMin1"));//VA A PRIMER NIVEL
				    BigDecimal valorMax1 = new BigDecimal(prop.getProperty("valorMax1"));
				    
				    BigDecimal valorMin2 = new BigDecimal(prop.getProperty("valorMin2"));//VA A SEGUNDO NIVEL, APRUEBA PRIMERO
				    BigDecimal valorMax2 = new BigDecimal(prop.getProperty("valorMax2"));
				    
				    BigDecimal valorMin3 = new BigDecimal(prop.getProperty("valorMin3"));//VA A TERCER NIVEL, APRUEBA HASTA SEGUNDO
				    BigDecimal valorMax3 = new BigDecimal(prop.getProperty("valorMax3"));
				    
				    BigDecimal valorMin4 = new BigDecimal(prop.getProperty("valorMin4"));//VA A CUARTO NIVEL, APRUEBA HASTA TERCERO
				    BigDecimal valorMax4 = new BigDecimal(prop.getProperty("valorMax4"));
				    
				    BigDecimal valorMin5 = new BigDecimal(prop.getProperty("valorMin5"));//VA A QUINTO NIVEL, APRUEBA HASTA CUARTO,  POR AHORA CERTIFICACION A2
				    BigDecimal valorMax5= new BigDecimal(prop.getProperty("valorMax5"));
				    
				 //UBICAR A CADA ESTUDIANTE EN UN NIVEL
				 
				 for (RegistroDto registroDto : rauifListRegistroDto) {
					
					  //Declaro Variables Auxiliares
						  //Carrera  carreraAux= new Carrera();
						MallaCurricular mallaCurricularAux= new MallaCurricular();
						List<MateriaDto> listMateriasMallaAux = new ArrayList<MateriaDto>();
						
						//BUSCO LA CARRERA CON crr_id de la ficha Inscripcion
			              // carreraAux = servRauifCarreraServicio.buscarPorId(registroDto.getFcinCarreraSiiu());
						
						// Buscar la malla curricular vigente y activa de la carrera
						mallaCurricularAux = servRauifMallaCurricular.buscarXcarreraXvigenciaXestado(registroDto.getFcinCarreraSiiu(),MallaConstantes.VIGENTE_MALLA_SI_VALUE, MallaConstantes.ESTADO_MALLA_ACTIVO_VALUE);
						
						//Busco materias de toda la malla
						listMateriasMallaAux =   servRauifMateriaDto.listarXmalla(mallaCurricularAux.getMlcrId());
						
						//CARGO PARARLELO HOMOLOGACION A TODAS LAS MATERIAS DE LA MALLA
						for (MateriaDto itmMateria : listMateriasMallaAux) {
							
							//BUSCO PARALELO HOMOLOGACION
							ParaleloDto	ParaleloHistoricoAux = servRauifParaleloDto.buscarXmallaMateriaXperiodoXDescripcion(
										itmMateria.getMlcrmtId(), PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE,ParaleloConstantes.PARALELO_HOMOLOGACION_LABEL);
								
							itmMateria.setPrlId(ParaleloHistoricoAux.getPrlId()); //Se agrega a cada materia el paralelo homologación
							itmMateria.setPrlDescripcion(ParaleloHistoricoAux.getPrlDescripcion());
							itmMateria.setMlcrprId(ParaleloHistoricoAux.getMlcrprId()); //Se una mlcrprId a la materia
							
						}
					    
					    //UBICO EL NIVEL APROBADO					    
						if((registroDto.getFcinNotaUbicacion().compareTo(valorMin1)>=0)&&(registroDto.getFcinNotaUbicacion().compareTo(valorMax1)<=0)){ //VA A PRIMER NIVEL
							registroDto.setListaMaterias(null);
							
						}else  if((registroDto.getFcinNotaUbicacion().compareTo(valorMin2)>=0)&&(registroDto.getFcinNotaUbicacion().compareTo(valorMax2)<=0))  { //VA A SEGUNDO NIVEL APRUEBA EL PRIMERO
							List<MateriaDto> nivelesAprobados = new ArrayList<>();
							for (MateriaDto materiaDto : listMateriasMallaAux) {
								if(materiaDto.getNvlId()==NivelConstantes.NIVEL_PRIMERO_VALUE){
									materiaDto.setAprobado(true);
									nivelesAprobados.add(materiaDto);
									
								}
							}
							
							registroDto.setListaMaterias(nivelesAprobados);
							registroDto.setFcmtNivelUbicacion(FichaMatriculaConstantes.PRIMER_NIVEL_VALUE);
							
						}else  if((registroDto.getFcinNotaUbicacion().compareTo(valorMin3)>=0)&&(registroDto.getFcinNotaUbicacion().compareTo(valorMax3)<=0))  { //VA A TERCER NIVEL APRUEBA PRIMERO Y SEGUNDO
                            List<MateriaDto> nivelesAprobados = new ArrayList<>();
							
							for (MateriaDto materiaDto : listMateriasMallaAux) {
								if((materiaDto.getNvlId()==NivelConstantes.NIVEL_PRIMERO_VALUE)||(materiaDto.getNvlId()==NivelConstantes.NIVEL_SEGUNDO_VALUE)){
									materiaDto.setAprobado(true);
									nivelesAprobados.add(materiaDto);
									
								}
							}
							registroDto.setListaMaterias(nivelesAprobados);
							registroDto.setFcmtNivelUbicacion(FichaMatriculaConstantes.SEGUNDO_NIVEL_VALUE);
							
						}else  if((registroDto.getFcinNotaUbicacion().compareTo(valorMin4)>=0)&&(registroDto.getFcinNotaUbicacion().compareTo(valorMax4)<=0))  {  //VA A CUARTO NIVEL: APRUEBA PRIMER, SEGUNDO Y TERCERO
							
                         List<MateriaDto> nivelesAprobados = new ArrayList<>();
							
							for (MateriaDto materiaDto : listMateriasMallaAux) {
								if((materiaDto.getNvlId()==NivelConstantes.NIVEL_PRIMERO_VALUE)||(materiaDto.getNvlId()==NivelConstantes.NIVEL_SEGUNDO_VALUE)||(materiaDto.getNvlId()==NivelConstantes.NIVEL_TERCER_VALUE)){
									materiaDto.setAprobado(true);
									nivelesAprobados.add(materiaDto);
									
								}
							}
							registroDto.setListaMaterias(nivelesAprobados);
							registroDto.setFcmtNivelUbicacion(FichaMatriculaConstantes.TERCERO_NIVEL_VALUE);
							
						}
						else  if((registroDto.getFcinNotaUbicacion().compareTo(valorMin5)>=0)&&(registroDto.getFcinNotaUbicacion().compareTo(valorMax5)<=0))  {  //VA A QUINTO NIVEL: APRUEBA PRIMER, SEGUNDO, TERCERO Y CUARTO
							
	                         List<MateriaDto> nivelesAprobados = new ArrayList<>();
								
								for (MateriaDto materiaDto : listMateriasMallaAux) {
									if((materiaDto.getNvlId()==NivelConstantes.NIVEL_PRIMERO_VALUE)
											||(materiaDto.getNvlId()==NivelConstantes.NIVEL_SEGUNDO_VALUE)
											||(materiaDto.getNvlId()==NivelConstantes.NIVEL_TERCER_VALUE)
											||(materiaDto.getNvlId()==NivelConstantes.NIVEL_CUARTO_VALUE)){
										materiaDto.setAprobado(true);
										nivelesAprobados.add(materiaDto);
										
									}
								}
								registroDto.setListaMaterias(nivelesAprobados);
								registroDto.setFcmtNivelUbicacion(FichaMatriculaConstantes.CUARTO_NIVEL_VALUE);
								
							}else{
							
							FacesUtil.limpiarMensaje();
						    FacesUtil.mensajeError("La nota de la prueba de suficiencia esta fuera de los parámetros establecidos, identificación: "+registroDto.getPrsIdentificacion());
							return null;
							
						}
					 
				}
				 
				 //Se procede a generar el registro
				 
				  if(dentroCarrerasPermitidas) {
				     if(dentroRango==true){
				    	 
				         servRauifRegistroAutomatico.generarRegistroUbicacionIdiomas(rauifListRegistroDto, rauifPlanificacionCronograma);	
				    	 
			          	FacesUtil.limpiarMensaje();
			            FacesUtil.mensajeInfo("Registro automático de estudiantes en suficiencia idiomas por ubicación realizado con exito");
				     }else{
					    FacesUtil.limpiarMensaje();
					    FacesUtil.mensajeError("Todos los estudiantes deben estar en el tipo de ingreso ubicación idiomas automático, no se ha ingresado estudiante alguno");
						return null;
				     }
				  }else{
					  FacesUtil.limpiarMensaje();
					  FacesUtil.mensajeError("Todos los estudiantes deben estar en  un idioma, no se ha ingresado estudiante alguno"); 
						return null;  
				  }
				     
			    }else{
				FacesUtil.mensajeError("No se cargo el archivo, no tiene permisos para crear usuarios");
				return null;
			   }
			
			
			
		} catch (RegistroAutomaticoValidacionException e ) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RegistroAutomaticoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar paralelo HOMOLOGACION, comuníquese con el administrador del sistema"); 
			return null;  
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró paralelo HOMOLOGACION al uno de los niveles, comuníquese con el administrador del sistema");
			return null;  
		} catch (MateriaDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar materias en la malla curricular"); 
			return null;  
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró materias al buscar en la malla curricular, comuníquese con el administrador del sistema");
			return null;  
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró malla curricular vigente y activa en el idioma.");
			return null;  
		} catch (MallaCurricularException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar malla curricular vigente y activa en el idioma, comuníquese con el administrador del sistema"); 
			return null;  
		} catch (PlanificacionCronogramaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró planificacion cronograma  en el periodo HOMOLOGACION");
			return null;  
		} catch (PlanificacionCronogramaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar planificacion cronograma, comuníquese con el administrador del sistema"); 
			return null; 
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontró periodo académico homologación");
			return null; 
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error desconocido al buscar periodo académico homologación, comuníquese con el administrador del sistema"); 
			return null;
		}  finally {
			try {
				if(rauifArchivo != null){
					rauifArchivo.close();
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
				  rauifArchivo = event.getFile().getInputstream();
				  rauifValidarArchivo = 1; // archivo cargado correctamente
				  FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName() + " se cargó con éxito");  
			  }else{
				  FacesUtil.mensajeError("Archivo erroneo");
			  }
		  
		  } catch (IOException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al intentar cargar el archivo, por favor intente nuevamente");
			}
		  
	    }
	  
	  
	  /**
	   * Setea los datos del Dto 
	   * @param registro - DTO de registro para llenar los datos
	   * @param dato - dato a ingresar
	   * @param numeroDato - numero de celda para saber que dato debe setear
	   */
	  private void setearDatosEnRegistroDto(RegistroDto registro, Cell dato , int numeroDato ) throws Exception {
		  switch (numeroDato){
		  case 1:
			  try{
			  registro.setPrsTipoIdentificacion(Integer.parseInt(dato.getStringCellValue()));
		     } catch (Exception e) {
			  	throw new Exception("Error del texto ingresado en el campo tipo identificación en el registro de "+ registro.getPrsIdentificacion());
			}
			  break;
		  case 2:
			  try{
			  registro.setPrsIdentificacion(dato.getStringCellValue());
		      } catch (Exception e) {
		    	  throw new Exception("Error del texto ingresado en el campo identificación en el registro de "+ registro.getPrsIdentificacion());
			  }
			  break;
		  case 3:
			  try{
			  registro.setPrsPrimerApellido(dato.getStringCellValue());
			  } catch (Exception e) {
				  throw new Exception("Error del texto ingresado en el campo primer apellido en el registro de "+ registro.getPrsIdentificacion());
			  }
			  break;
		  case 4:
			  try{
			  registro.setPrsSegundoApellido(dato.getStringCellValue());
		  } catch (Exception e) {
			  throw new Exception("Error del texto ingresado en el campo segundo apellido en el registro de "+ registro.getPrsIdentificacion());
		  }
			  break;
		  case 5:
		       try{
			  registro.setPrsNombres(dato.getStringCellValue());
		      } catch (Exception e) {
		    	  throw new Exception("Error del texto ingresado en el campo nombres en el registro de "+ registro.getPrsIdentificacion());
		      }
			  break;
		  case 6:
			  try{
			  registro.setPrsSexo(Integer.parseInt(dato.getStringCellValue()));
		      } catch (Exception e) {
		    	  throw new Exception("Error del texto ingresado en el campo sexo en el registro de "+ registro.getPrsIdentificacion());
		      }
			  break;
		  case 7:
			  try{
			  registro.setPrsMailPersonal(dato.getStringCellValue());
			  } catch (Exception e) {
				  throw new Exception("Error del texto ingresado en el campo mail personal en el registro de "+ registro.getPrsIdentificacion());
			  }
			  break;
		  case 8:
			  try{
			  registro.setPrsMailInstitucional(dato.getStringCellValue());
		      } catch (Exception e) {
		    	  throw new Exception("Error del texto ingresado en el campo mail institucional en el registro de "+ registro.getPrsIdentificacion());
		      }
			  break;
		  case 9:
			  try {
				  registro.setFcinNotaUbicacion(new BigDecimal(dato.getStringCellValue()));
				 
			     } catch (Exception e) {
			    	 throw new Exception("Error del texto ingresado en el campo nota Ubicación en el registro de "+ registro.getPrsIdentificacion());
				
			     }
			  break;
		  case 10:
			  try{
			  registro.setFcinCarreraSiiu(Integer.parseInt(dato.getStringCellValue()));
		       } catch (Exception e) {
		    	   throw new Exception("Error del texto ingresado en el campo carrera en el registro de "+ registro.getPrsIdentificacion());
			
		       }
			  break;
		  case 11:
			  try{
			  registro.setFcinTipoIngreso(Integer.parseInt(dato.getStringCellValue()));
		       } catch (Exception e) {
		    	   throw new Exception("Error del texto ingresado en el campo tipo ingreso en el registro de "+ registro.getPrsIdentificacion());
			
		        } 
			  break;
		  case 12:
			  try{
			  registro.setFcinTipoUniversidad(Integer.parseInt(dato.getStringCellValue()));
		        } catch (Exception e) {
		        	 throw new Exception("Error del texto ingresado en el campo tipo universidad en el registro de "+ registro.getPrsIdentificacion());
			
		        } 
			  break;
		  
		  }
	  }
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/
	
	
	public RegistroDto getRauifRegistroDto() {
		return rauifRegistroDto;
	}
	public void setRauifRegistroDto(RegistroDto rauifRegistroDto) {
		this.rauifRegistroDto = rauifRegistroDto;
	}
	public Integer getRauifValidarArchivo() {
		return rauifValidarArchivo;
	}
	public void setRauifValidarArchivo(Integer rauifValidarArchivo) {
		this.rauifValidarArchivo = rauifValidarArchivo;
	}
	public InputStream getRauifArchivo() {
		return rauifArchivo;
	}
	public void setRauifArchivo(InputStream rauifArchivo) {
		this.rauifArchivo = rauifArchivo;
	}
	public List<RegistroDto> getRauifListRegistroDto() {
		rauifListRegistroDto = rauifListRegistroDto==null?(new ArrayList<RegistroDto>()):rauifListRegistroDto;
		return rauifListRegistroDto;
	}
	public void setRauifListRegistroDto(List<RegistroDto> rauifListRegistroDto) {
		this.rauifListRegistroDto = rauifListRegistroDto;
	}

	public Integer getRauifTipoUsuario() {
		return rauifTipoUsuario;
	}

	public void setRauifTipoUsuario(Integer rauifTipoUsuario) {
		this.rauifTipoUsuario = rauifTipoUsuario;
	}

	public Integer getRauifPracId() {
		return rauifPracId;
	}

	public void setRauifPracId(Integer rauifPracId) {
		this.rauifPracId = rauifPracId;
	}

	public List<PeriodoAcademico> getRauifListaPeriodoAcademico() {
		return rauifListaPeriodoAcademico;
	}

	public void setRauifListaPeriodoAcademico(List<PeriodoAcademico> rauifListaPeriodoAcademico) {
		this.rauifListaPeriodoAcademico = rauifListaPeriodoAcademico;
	}

	public PlanificacionCronograma getRauifPlanificacionCronograma() {
		return rauifPlanificacionCronograma;
	}

	public void setRauifPlanificacionCronograma(PlanificacionCronograma rauifPlanificacionCronograma) {
		this.rauifPlanificacionCronograma = rauifPlanificacionCronograma;
	}
	
	
	
}
