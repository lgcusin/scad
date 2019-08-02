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
   
 ARCHIVO:     AdministracionPromocionNivelacionForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de la promoción de nivelación.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
13-03-2019			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CalificacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaInscripcionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaPeriodoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaPeriodoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionTerceraNivelacionForm. Managed Bean que
 * maneja las peticiones para la administración de la tercera matrícula de nivelación
 * 
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name = "administracionTerceraNivelacionForm")
@SessionScoped
public class AdministracionTerceraNivelacionForm implements Serializable {

	private static final long serialVersionUID = -5706899311603575398L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	private List<EstudianteJdbcDto> listaAprobados;
	private List<EstudianteJdbcDto> listaReprobados;
	private List<Carrera> apnfListaAreas;
	private List<Persona> apnfListaAprobados;
	private Integer apnfValidadorClic;
	private Usuario apnfUsuario;
	private EstudianteJdbcDto apnfEstudianteBuscar;
	private Carrera apnfAreaBuscar;
	private Boolean apnfHabilitadorGenerar;
	private InputStream appfArchivo;
	private boolean appfValidadorGuardar;
	private Integer acfValidadorClick;
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB	private EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	@EJB	private MateriaDtoServicioJdbc servMateriaDtoServicioJdbc;
	@EJB	private FichaInscripcionServicio servFichaInscripcionServicio;
	@EJB	private FichaEstudianteServicio servFichaEstudianteServicio;
	@EJB	private CarreraServicio servCarreraServicio;
	@EJB	private PersonaServicio servPersonaServicio;
	
	@EJB	private PeriodoAcademicoServicio servApafPeriodoAcademico;
	@EJB	private UsuarioRolServicio servApafUsuarioRolServicio;
	@EJB	private MallaPeriodoDtoServicioJdbc servApafMallaPeriodoDtoServicioJdbc;
	@EJB	private MallaPeriodoServicio servApafMallaPeriodoServicio;
	@EJB	private ProcesoFlujoServicio servProcesoFlujoServicio;
	@EJB	private MatriculaServicioJdbc servJdbcMatricula;
	@EJB	private FichaInscripcionServicio servFichaInscripcion;
	@EJB	private CalificacionServicio servCalificacionServicio;
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/

	/**
	 * Incicializa las variables para la funcionalidad 
	 * @return navegacion al xhtml de listar Estudiantes
	 */
	public String irListarEstudianteTerceras(Usuario usuario) {
		apnfUsuario = usuario;
			try {
				servApafUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
				iniciarParametros();
				apnfEstudianteBuscar = new EstudianteJdbcDto();
				return "irBuscarTerceraNivelacion";
			} catch (UsuarioRolNoEncontradoException e) {
			} catch (UsuarioRolException e) {
			}
			return null;			
	}

	public String irCargaArchivoPromocion(Usuario usuario) {
		apnfUsuario = usuario;
		return "irCargaPromocion";
	}
	
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			if (event != null) {
				appfArchivo = event.getFile().getInputstream();
				appfValidadorGuardar = false; // archivo cargado correctamente
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName() + " se cargó con éxito");
				validarArchivo();
			} else {
				FacesUtil.mensajeError("Archivo erroneo");
			}
		} catch (IOException e) {
			FacesUtil.mensajeError("Error al intentar cargar el archivo, por favor intente nuevamente");
		}
	}
	
	 
	 /**
		 * Valida que el Excel este en el formato correcto y guarda los datos del archivo en un objeto
		 */
		public void validarArchivo(){
			try {
//				 Workbook libro = WorkbookFactory.create(rafArchivo);  // creo el libro excel del archivo
				Workbook libro = new XSSFWorkbook(appfArchivo);  // creo el libro excel del archivo
				 Sheet hoja = libro.getSheetAt(0); // selecciono la hoja nuemero 1
				 Row filaActual; // creo el objeto para la fila que va a ir recorriendo
				 Cell celdaActual; // creo el objeto para la celda que va a ir recorriendo
				
				 
				//validar que cada celda tenga datos y que el dato sea del tipo que se requiere
				Iterator<Row> itFilas = hoja.rowIterator();
				itFilas.next();
				itFilas.next();
				itFilas.next();
				itFilas.next();
				apnfListaAprobados = new ArrayList<Persona>();
				while (itFilas.hasNext()) { //recorro las filas 
					filaActual = itFilas.next();
					Iterator<Cell> itCeldas = filaActual.cellIterator();
					String cedula = null;
					while (itCeldas.hasNext()) { // recorro las celdas de esa fila
						celdaActual= itCeldas.next(); //guardo la celda en una variable
							
							if(celdaActual.getColumnIndex() == 1){
								cedula=(celdaActual.getStringCellValue());	
								Persona prs = new Persona();
								prs = servPersonaServicio.buscarPorIdentificacion(cedula);
								boolean op=true;
								for (Persona item : apnfListaAprobados) {
									if(item.getPrsIdentificacion().equals(prs.getPrsIdentificacion())){
										op=false;
									}
								}
								if(op){
									apnfListaAprobados.add(prs);	
								}
							}
					}
				}
//				List<Persona> apnfListaAprobados1 = new ArrayList<Persona>();
//				apnfListaAprobados1.add(apnfListaAprobados.get(0));
//				for (int i = 1; i < apnfListaAprobados.size(); i++) {
//					if(apnfListaAprobados1.get(i-1).getPrsIdentificacion().equals(apnfListaAprobados.get(i).getPrsIdentificacion())){
//						continue;
//					}else{
//						apnfListaAprobados1.add(apnfListaAprobados.get(i));
//					}
//				}
//				apnfListaAprobados = new ArrayList<Persona>(); 
//				apnfListaAprobados = apnfListaAprobados1;
				Collections.sort(apnfListaAprobados, new Comparator<Persona>() {
					public int compare(Persona obj1, Persona obj2) {
						return new String(obj1.getPrsPrimerApellido()).compareTo(new String(obj2.getPrsPrimerApellido()));
					}
				});
				
				
				appfValidadorGuardar=false;
			} catch (Exception e) {
			}
			
		}
	
	/**
	 * Método que limpia los objetos instaciados al
	 * iniciar la funcionalidad
	 * 
	 */
	public void iniciarParametros() {
		listaAprobados = new ArrayList<EstudianteJdbcDto>();
		listaReprobados = new ArrayList<EstudianteJdbcDto>();
		apnfValidadorClic=0;
		apnfListaAreas = new ArrayList<Carrera>();
		apnfHabilitadorGenerar = true;
		apnfListaAprobados = new ArrayList<>();
		apnfAreaBuscar = new Carrera();
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instanciados 
	 * 
	 * @return navegacion al inicio
	 */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}

	/**
	 * Método para limpiar los parámtros de busqueda ingresados
	 */
	public void limpiar() {
		iniciarParametros();
	}

	/**
	 * Método para cancelar la edicion o creacion de un periodo academico
	 * 
	 * @return navegacion al xhtml de listar periodo academico
	 */
	public String cancelar() {
		limpiar();
		return "cancelar";
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	public String buscarEstudiantes(){
			try {
				listaReprobados = servEstudianteDtoServicioJdbc.buscarEstudianteReprobadoNivelacion(apnfEstudianteBuscar.getPrsIdentificacion());
				for (EstudianteJdbcDto item : listaReprobados) {
					Carrera crr = servCarreraServicio.buscarPorId(item.getFcinCarreraSiiu());
					item.setCrrDescripcion(crr.getCrrDescripcion());
					crr = servCarreraServicio.buscarPorId(item.getFcinCncrArea());
					item.setCrrDescripcionArea(crr.getCrrDescripcion());
				}
			} catch (Exception e) {
			}
	
		
		return null;
	}
	
	/**
	 * setea el verificador del click a 0 para nuevas validaciones
	 */
	public void setearVerificadorClick() {
		apnfValidadorClic = 1;
	}

	
	public void habilitarTerceraMatricula(EstudianteJdbcDto item){
		try {
			servFichaEstudianteServicio.habilitarObservacionTerceraMatricula(item.getFcesId());
		} catch (FichaEstudianteNoEncontradoException e) {
		} catch (FichaEstudianteException e) {
		}
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeInfo("Se activó la tercera matrícula para el estudiante seleccionado.");
		listaReprobados = new ArrayList<>();
		apnfEstudianteBuscar = new EstudianteJdbcDto();
	} 
	
	
	public String promover(){
		
		try {
			servFichaInscripcion.pormoverNivelacionGrado(apnfListaAprobados);
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Los estudiantes fueron promovidos exitosamente a carrera.");
		} catch (FichaEstudianteNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al promover los estudiantes a carrera, comuníquese con el administrador del sistema.");
		}
		iniciarParametros();
		acfValidadorClick = 1;
		return "irInicio";
	}
	
	public void verificarClick(){
		acfValidadorClick = 0;
	}

	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	public List<EstudianteJdbcDto> getListaAprobados() {
		return listaAprobados;
	}

	public void setListaAprobados(List<EstudianteJdbcDto> listaAprobados) {
		this.listaAprobados = listaAprobados;
	}

	public List<EstudianteJdbcDto> getListaReprobados() {
		return listaReprobados;
	}

	public void setListaReprobados(List<EstudianteJdbcDto> listaReprobados) {
		this.listaReprobados = listaReprobados;
	}

	public Integer getApnfValidadorClic() {
		return apnfValidadorClic;
	}

	public void setApnfValidadorClic(Integer apnfValidadorClic) {
		this.apnfValidadorClic = apnfValidadorClic;
	}

	public Usuario getApnfUsuario() {
		return apnfUsuario;
	}

	public void setApnfUsuario(Usuario apnfUsuario) {
		this.apnfUsuario = apnfUsuario;
	}

	public List<Carrera> getApnfListaAreas() {
		return apnfListaAreas;
	}

	public void setApnfListaAreas(List<Carrera> apnfListaAreas) {
		this.apnfListaAreas = apnfListaAreas;
	}

	public EstudianteJdbcDto getApnfEstudianteBuscar() {
		return apnfEstudianteBuscar;
	}

	public void setApnfEstudianteBuscar(EstudianteJdbcDto apnfEstudianteBuscar) {
		this.apnfEstudianteBuscar = apnfEstudianteBuscar;
	}

	public Carrera getApnfAreaBuscar() {
		return apnfAreaBuscar;
	}

	public void setApnfAreaBuscar(Carrera apnfAreaBuscar) {
		this.apnfAreaBuscar = apnfAreaBuscar;
	}

	public Boolean getApnfHabilitadorGenerar() {
		return apnfHabilitadorGenerar;
	}

	public void setApnfHabilitadorGenerar(Boolean apnfHabilitadorGenerar) {
		this.apnfHabilitadorGenerar = apnfHabilitadorGenerar;
	}

	public InputStream getAppfArchivo() {
		return appfArchivo;
	}

	public void setAppfArchivo(InputStream appfArchivo) {
		this.appfArchivo = appfArchivo;
	}

	public boolean isAppfValidadorGuardar() {
		return appfValidadorGuardar;
	}

	public void setAppfValidadorGuardar(boolean appfValidadorGuardar) {
		this.appfValidadorGuardar = appfValidadorGuardar;
	}

	public List<Persona> getApnfListaAprobados() {
		return apnfListaAprobados;
	}

	public void setApnfListaAprobados(List<Persona> apnfListaAprobados) {
		this.apnfListaAprobados = apnfListaAprobados;
	}

	

	

	

}
