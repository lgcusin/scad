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
   
 ARCHIVO:     AdministracionAulaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para los programas de posgrado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
20-06-2018			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionPosgrado;

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

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ProgramaPosgradoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseAulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ModalidadServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProgramaPosgradoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.VigenciaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Duracion;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.Modalidad;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemico;
import ec.edu.uce.academico.jpa.entidades.publico.NucleoProblemicoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacion;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSede;
import ec.edu.uce.academico.jpa.entidades.publico.Titulo;
import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.Vigencia;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) AdministracionProgramaPosgradoForm.
 * Managed Bean que maneja las peticiones para la administración de los programas de posgrado.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="administracionProgramaPosgradoForm")
@SessionScoped
public class AdministracionProgramaPosgradoForm implements Serializable{

	private static final long serialVersionUID = -397102404723196895L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	// OBJETOS
	private Usuario appfUsuario;
	private ProgramaPosgradoDto programaPosgradoDto;
	private boolean appfValidadorPrograma;
	private boolean appfValidadorCreacionPrograma;
	private boolean appfValidadorGuardar;
	private InputStream appfArchivo;
	private Integer appfValidadorClick;
	private List<Dependencia> appfListDependencia;
	private List<Carrera> appfListCarrera;
	private boolean appfProgramaExiste;
	private Carrera crrAuxiliar;
	
	private ConfiguracionCarrera appfCncrAuxiliar;
	private List<Modalidad> appfListaModalidad;
	private List<Vigencia> appfListaVigencia;
	private List<TipoFormacion> appfListaTipoFormacion;
	private List<TipoSede> appfListaTipoSede;
	private List<Duracion> appfListaDuracion;
	private List<Ubicacion> appfListaUbicacion;
	
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
		
	}
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB
	DependenciaServicio servAppfDependencia;
	@EJB
	TituloServicio servTituloServicio;
	@EJB
	EdificioDtoServicioJdbc servAafEdificioDto;	
	@EJB
	AulaServicio servAafAulaServicio;
	@EJB
	CarreraServicio servCarreraServicio;
	@EJB
	HoraClaseAulaServicio servAafHoraClaseAulaServicio;
	@EJB
	HoraClaseServicio servAafHoraClaseServicio;
	@EJB
	HorarioAcademicoServicio servAafHorarioAcademicoServicio;
	@EJB
	ProgramaPosgradoServicio servProgramaPosgradoServicio;
	@EJB
	ConfiguracionCarreraServicio servConfiguracionCarreraServicio;
	@EJB
	MallaCurricularServicio servMallaCurricularServicio;
	@EJB
	MallaMateriaDtoServicioJdbc servMallaMateriaDtoServicioJdbc;
	@EJB
	ModalidadServicio servModalidadServicio;
	@EJB
	VigenciaServicio servVigenciaServicio;
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	//IR A ADMINISTRAR PROGRAMAS POSGRADO
	/**
	 * Método que permite iniciar la administración del programa de posgrado
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración del programa de posgrado.
	 */
	
	public String irAdministracionProgramaPosgrado(Usuario usuario) {
		appfUsuario = usuario; 
		iniciarParametros();
		return "irAdministracionPosgrado";
	}
	
	public String irCreacionProgramaPosgrado(Usuario usuario) {
		appfUsuario = usuario; 
		crrAuxiliar = new Carrera();
		iniciarParametros();
		return "irCreacionPosgrado";
	}
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados y paneles
	 * @return 
	 */
	public String limpiar() {
		iniciarParametros();
		return null;
	}
	
	/**
	 * Método para limpiar la lista de Aula cuando se cambia el edificio, y no se presiona buscar
	 * @return 
	 */
	public String limpiarAulas() {
//		aafListAulaDto = new ArrayList<AulaDto>();
		return null;
	}
	
	/**
	 * Método para iniciar los parametros
	 */
	public void iniciarParametros() {
//		aafDependenciaDtoBuscar = new DependenciaDto();// Inicio el objeto Dependencia
//		aafEdificioDtoBuscar = new EdificioDto(); // Inicio el objetos Edificio
//		aafListAulaDto = null;// inicio la lista de Aula
		appfListDependencia = new ArrayList<Dependencia>(); // inicio la lista de Dependencias
		appfListCarrera = new ArrayList<Carrera>(); // inicio la lista de carreras
//		aafDependenciaDtoBuscar.setDpnId(GeneralesConstantes.APP_ID_BASE);// Seteo en -99 el valor inicial IdDependencia del objeto Dependencia
//		aafEdificioDtoBuscar.setEdfId(GeneralesConstantes.APP_ID_BASE);// Seteo en -99 el valor inicial IdEdificio del objeto Edificio
		appfValidadorClick = 0; // Seteo en cero el valor de click editar/nuevo
		llenarDependencia(); // 
		appfValidadorPrograma=true;
		appfValidadorGuardar = true;
		programaPosgradoDto = new ProgramaPosgradoDto();
		programaPosgradoDto.setListaMateriaDto(new ArrayList<MateriaDto>());
		appfProgramaExiste=false;
		appfValidadorCreacionPrograma=false;
		appfCncrAuxiliar = new ConfiguracionCarrera();
		appfListaModalidad = new ArrayList<Modalidad>();
		appfListaVigencia = new ArrayList<Vigencia>();
		appfListaTipoFormacion = new ArrayList<TipoFormacion>();
		appfListaTipoSede = new ArrayList<TipoSede>();
		appfListaDuracion = new ArrayList<Duracion>();
		appfListaUbicacion = new ArrayList<Ubicacion>();
		try {
			appfListaModalidad = servModalidadServicio.listarTodos();
			appfListaVigencia= servVigenciaServicio.listarTodos();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	/**
	* Método para llenar la lista de Dependencias
	*/
	public void llenarDependencia() {
		appfListDependencia = null;
		try {
			appfListDependencia = servAppfDependencia.listarFacultadesActivas(3);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	
	/**
	 * Método para llenar la lista de programas 
	 *
	 */
	public void llenarProgramas() {
		appfListCarrera = null;
		if (programaPosgradoDto.getDpnId() != GeneralesConstantes.APP_ID_BASE) { // Si selecciona una Dependencia
			try {
				appfListCarrera = servCarreraServicio.listarCarrerasXFacultadPosgrado(programaPosgradoDto.getDpnId());
				Carrera carreraNueva = new Carrera();
				carreraNueva.setCrrId(appfListCarrera.get(appfListCarrera.size()-1).getCrrId()+1);
				carreraNueva.setCrrDescripcion("NUEVO PROGRAMA");
				Dependencia dpnAux = new Dependencia();
				dpnAux = servAppfDependencia.buscarPorId(programaPosgradoDto.getDpnId());
				carreraNueva.setCrrId(-1);
				carreraNueva.setCrrDependencia(dpnAux);
				appfListCarrera.add(carreraNueva);
				appfValidadorPrograma = false;
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("No existen programas en la facultad seleccionada.");
			} catch (CarreraException e) {
			} catch (DependenciaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Error al buscar la facultad seleccionada, intente más tarde.");
			} catch (DependenciaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Error al buscar la facultad seleccionada, intente más tarde.");
			}catch (Exception e) {
				e.printStackTrace();
			}
		} else { 
			// Sino selecciona una carrera, limpia los mensajes e inicia los parametros nuevamente, para que desliegue todas las aulas sin filtro
			FacesUtil.limpiarMensaje();// no es necesario enviar mensjae al usuario
			limpiar(); // se limpia, inicializa los objetos
		}
		if(appfListCarrera.size()==0){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No existen programas en la facultad seleccionada.");
		}
	}
	
	/**
	 * Método para llenar la lista de programas 
	 *
	 */
	public void llenarCarreraNueva() {
		try {
			programaPosgradoDto.setPracDescripcion(programaPosgradoDto.getPracDetalleFechaCohorte()+" POSGRADO "+crrAuxiliar.getCrrDescripcion());
			programaPosgradoDto.setCrnDescripcion("POSGRADO "+crrAuxiliar.getCrrDescripcion());
			
			programaPosgradoDto.setCrnTipo(CronogramaConstantes.TIPO_POSGRADO_VALUE);
			programaPosgradoDto.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * Método para llenar la descripcion del período académico
	 *
	 */
	public void llenarPracDescripcion() {
		try {
			Carrera crrAux = servCarreraServicio.buscarPorId(programaPosgradoDto.getCrrId());
			programaPosgradoDto.setPracDescripcion(programaPosgradoDto.getPracDetalleFechaCohorte()+" POSGRADO "+crrAux.getCrrDescripcion());
			programaPosgradoDto.setCrnDescripcion("POSGRADO "+crrAux.getCrrDescripcion());
			programaPosgradoDto.setCrnTipo(CronogramaConstantes.TIPO_POSGRADO_VALUE);
			programaPosgradoDto.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CarreraNoEncontradoException | CarreraException e) {
		}
		
	}
	
	public void llenarConfiguracionCarrera() {
		Titulo ttlAux = null;
		ttlAux = servTituloServicio.buscarTituloXDescripcion(programaPosgradoDto.getTtlDescripcion());
		if(ttlAux!=null){
			programaPosgradoDto.setTtlId(ttlAux.getTtlId());
		}else{
			try {
				Carrera crrAux = servCarreraServicio.buscarPorId(programaPosgradoDto.getCrrId());
				programaPosgradoDto.setConfiguracionCarrera(new ConfiguracionCarrera());
				programaPosgradoDto.getConfiguracionCarrera().setCncrCarrera(crrAux);
				programaPosgradoDto.setTtlId(GeneralesConstantes.APP_ID_BASE);	
			} catch (CarreraNoEncontradoException | CarreraException e) {
			}
		}
		
		
	}
	
	public void cambiarCarrera() {
		if(programaPosgradoDto.getCrrId()!=-1){
			try {
				Carrera crrAux = servCarreraServicio.buscarPorId(programaPosgradoDto.getCrrId());
				ConfiguracionCarrera cncrAux = new ConfiguracionCarrera();
				cncrAux = servConfiguracionCarreraServicio.buscarXcrr(crrAux.getCrrId());
				Titulo ttlAux = new Titulo();
				ttlAux = servTituloServicio.buscarTituloXId(cncrAux.getCncrTitulo().getTtlId());
				programaPosgradoDto.setTtlId(ttlAux.getTtlId());
				programaPosgradoDto.setTtlDescripcion(ttlAux.getTtlDescripcion());
				programaPosgradoDto.setConfiguracionCarrera(cncrAux);
				MallaCurricular mlcrAux = servMallaCurricularServicio.buscarXcarreraXvigenciaXestado(programaPosgradoDto.getCrrId(), 0, 0);
				programaPosgradoDto.setMlcrId(mlcrAux.getMlcrId());
				programaPosgradoDto.setMlcrDescripcion(mlcrAux.getMlcrDescripcion());
				programaPosgradoDto.setMlcrCodigo(mlcrAux.getMlcrCodigo());
				programaPosgradoDto.setMlcrFechaInicio(mlcrAux.getMlcrFechaInicio());
				programaPosgradoDto.setMlcrFechaFin(mlcrAux.getMlcrFechaFin());
				List<MallaCurricularDto> listaMaterias = new ArrayList<MallaCurricularDto>();
				listaMaterias= servMallaMateriaDtoServicioJdbc.listarMallasMateriasXCarrera(programaPosgradoDto.getCrrId());
				programaPosgradoDto.setListaMateriaDto(new ArrayList<MateriaDto>());
				for (MallaCurricularDto mallaCurricularDto : listaMaterias) {
					MateriaDto mtrAux = new MateriaDto();
					mtrAux.setNvlId(mallaCurricularDto.getNvlId());
					mtrAux.setMtrCodigo(mallaCurricularDto.getMtrCodigo());
					mtrAux.setMtrDescripcion(mallaCurricularDto.getMtrDescripcion());
					mtrAux.setMlcrmtId(mallaCurricularDto.getMlcrmtId());
					mtrAux.setCrrId(programaPosgradoDto.getCrrId());
					programaPosgradoDto.getListaMateriaDto().add(mtrAux);
					programaPosgradoDto.setNiveles(mallaCurricularDto.getNvlId());
				}
				appfProgramaExiste=true;
				appfValidadorGuardar = false;
			} catch (Exception e) {
			}
		}else{
			appfValidadorCreacionPrograma=true;
			crrAuxiliar = new Carrera();
			crrAuxiliar.setCrrId(-1);
			crrAuxiliar.setCrrDescripcion(null);
		}
	}
	
	
	public void llenarNucleoProblemico() {
		programaPosgradoDto.setListaNucleoProblemico(new ArrayList<NucleoProblemico>());
		programaPosgradoDto.setListaNucleoProblemicoCarrera(new ArrayList<NucleoProblemicoCarrera>());
		Carrera crrAux = new Carrera();
		try {
			crrAux = servCarreraServicio.buscarPorId(programaPosgradoDto.getCrrId());
			programaPosgradoDto.setMlcrCarrera(crrAux);
		} catch (CarreraNoEncontradoException | CarreraException e) {
		}
		for (int i = 0;i< programaPosgradoDto.getNiveles();i++) {
			NucleoProblemico ncprAux= new NucleoProblemico();
			ncprAux.setNcprDescripcion(crrAux.getCrrDescripcion()+" NIVEL "+i);
			ncprAux.setNcprEstado(0);
			programaPosgradoDto.getListaNucleoProblemico().add(ncprAux);
			NucleoProblemicoCarrera ncprcrrAux= new NucleoProblemicoCarrera();
			ncprcrrAux.setNcprcrCarrera(crrAux);
			ncprcrrAux.setNcprcrEstado(0);
			programaPosgradoDto.getListaNucleoProblemicoCarrera().add(ncprcrrAux);
		}
		
	}
	
	
	 public void handleFileUpload(FileUploadEvent event) {
		  try {
			  if(event != null){
				  appfArchivo = event.getFile().getInputstream();
				  appfValidadorGuardar = false; // archivo cargado correctamente
				  FacesUtil.limpiarMensaje();
				  FacesUtil.mensajeInfo("Archivo : " + event.getFile().getFileName() + " se cargó con éxito"); 
				  validarArchivo();
			  }else{
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
				while (itFilas.hasNext()) { //recorro las filas 
					filaActual = itFilas.next();
					Iterator<Cell> itCeldas = filaActual.cellIterator();
					MateriaDto mtrAux = new MateriaDto();
					while (itCeldas.hasNext()) { // recorro las celdas de esa fila
						celdaActual= itCeldas.next(); //guardo la celda en una variable
							
							if(celdaActual.getColumnIndex() == 0){
								mtrAux.setNvlId(Integer.valueOf(celdaActual.getStringCellValue()));	
							}
							
							
							if(celdaActual.getColumnIndex() == 1){
								mtrAux.setMtrDescripcion(celdaActual.getStringCellValue());	
							}
							
							if(celdaActual.getColumnIndex() == 2){
								mtrAux.setMtrCodigo(celdaActual.getStringCellValue());	
							}
							
//							if(itCeldas.hasNext() == false && itFilas.hasNext() == false){
//								FacesUtil.limpiarMensaje();
//								FacesUtil.mensajeInfo("Validación exitosa");
//							}
					}
					if(mtrAux.getNvlId()!=0){
						programaPosgradoDto.getListaMateriaDto().add(mtrAux);	
					}
					

			}
				appfValidadorGuardar=false;
			} catch (IOException e) {
				FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
			} catch (EncryptedDocumentException e) {
				FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
//			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
//				FacesUtil.mensajeError("Error al validar el archivo, por favor intente nuevamente");
//				rafValidarArchivo = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	 
	 
	
	/**
	 * Método que elimina Aula
	 * navagación al xhtml AdministracionAula
	 */
	public String guardarConfiguracion() {
		try {
			servProgramaPosgradoServicio.guardarConfiguracionPrograma(programaPosgradoDto, appfProgramaExiste);
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Se ha guardado correctamente la configuración respectiva del programa de posgrado.");
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al guardar la confguración del programa.");
		}
		limpiar();
		return "irInicio";
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
			
		/**
		* verifica que haga click en el boton nuevo paralelo
		*/
		public String verificarClickNuevoPrograma(){
			appfValidadorClick= 1;
			return null;
		}

		/**
	 * setea el verificador del click a 0 para nuevas validaciones
	 */
		public void setearVerificadorClick(){
			appfValidadorClick = 0;
		}
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	
	public Usuario getAppfUsuario() {
		return appfUsuario;
	}

	public void setAppfUsuario(Usuario appfUsuario) {
		this.appfUsuario = appfUsuario;
	}

	public ProgramaPosgradoDto getProgramaPosgradoDto() {
		return programaPosgradoDto;
	}

	public void setProgramaPosgradoDto(ProgramaPosgradoDto programaPosgradoDto) {
		this.programaPosgradoDto = programaPosgradoDto;
	}

	public List<Dependencia> getAppfListDependencia() {
		return appfListDependencia;
	}

	public void setAppfListDependencia(List<Dependencia> appfListDependencia) {
		this.appfListDependencia = appfListDependencia;
	}

	public List<Carrera> getAppfListCarrera() {
		return appfListCarrera;
	}

	public void setAppfListCarrera(List<Carrera> appfListCarrera) {
		this.appfListCarrera = appfListCarrera;
	}

	public boolean isAppfValidadorPrograma() {
		return appfValidadorPrograma;
	}

	public void setAppfValidadorPrograma(boolean appfValidadorPrograma) {
		this.appfValidadorPrograma = appfValidadorPrograma;
	}

	public boolean isAppfValidadorGuardar() {
		return appfValidadorGuardar;
	}

	public void setAppfValidadorGuardar(boolean appfValidadorGuardar) {
		this.appfValidadorGuardar = appfValidadorGuardar;
	}

	public InputStream getAppfArchivo() {
		return appfArchivo;
	}

	public void setAppfArchivo(InputStream appfArchivo) {
		this.appfArchivo = appfArchivo;
	}

	public Integer getAppfValidadorClick() {
		return appfValidadorClick;
	}

	public void setAppfValidadorClick(Integer appfValidadorClick) {
		this.appfValidadorClick = appfValidadorClick;
	}

	public boolean isAppfProgramaExiste() {
		return appfProgramaExiste;
	}

	public void setAppfProgramaExiste(boolean appfProgramaExiste) {
		this.appfProgramaExiste = appfProgramaExiste;
	}

	public boolean isAppfValidadorCreacionPrograma() {
		return appfValidadorCreacionPrograma;
	}

	public void setAppfValidadorCreacionPrograma(boolean appfValidadorCreacionPrograma) {
		this.appfValidadorCreacionPrograma = appfValidadorCreacionPrograma;
	}

	public Carrera getCrrAuxiliar() {
		return crrAuxiliar;
	}

	public void setCrrAuxiliar(Carrera crrAuxiliar) {
		this.crrAuxiliar = crrAuxiliar;
	}

	public ConfiguracionCarrera getAppfCncrAuxiliar() {
		return appfCncrAuxiliar;
	}

	public void setAppfCncrAuxiliar(ConfiguracionCarrera appfCncrAuxiliar) {
		this.appfCncrAuxiliar = appfCncrAuxiliar;
	}

	public List<Modalidad> getAppfListaModalidad() {
		return appfListaModalidad;
	}

	public void setAppfListaModalidad(List<Modalidad> appfListaModalidad) {
		this.appfListaModalidad = appfListaModalidad;
	}

	public List<Vigencia> getAppfListaVigencia() {
		return appfListaVigencia;
	}

	public void setAppfListaVigencia(List<Vigencia> appfListaVigencia) {
		this.appfListaVigencia = appfListaVigencia;
	}

	public List<TipoFormacion> getAppfListaTipoFormacion() {
		return appfListaTipoFormacion;
	}

	public void setAppfListaTipoFormacion(List<TipoFormacion> appfListaTipoFormacion) {
		this.appfListaTipoFormacion = appfListaTipoFormacion;
	}

	public List<TipoSede> getAppfListaTipoSede() {
		return appfListaTipoSede;
	}

	public void setAppfListaTipoSede(List<TipoSede> appfListaTipoSede) {
		this.appfListaTipoSede = appfListaTipoSede;
	}

	public List<Duracion> getAppfListaDuracion() {
		return appfListaDuracion;
	}

	public void setAppfListaDuracion(List<Duracion> appfListaDuracion) {
		this.appfListaDuracion = appfListaDuracion;
	}

	public List<Ubicacion> getAppfListaUbicacion() {
		return appfListaUbicacion;
	}

	public void setAppfListaUbicacion(List<Ubicacion> appfListaUbicacion) {
		this.appfListaUbicacion = appfListaUbicacion;
	}
	
	
	
	
	
	
		
}
	
	
	
	
	
	
	
	
	
	
