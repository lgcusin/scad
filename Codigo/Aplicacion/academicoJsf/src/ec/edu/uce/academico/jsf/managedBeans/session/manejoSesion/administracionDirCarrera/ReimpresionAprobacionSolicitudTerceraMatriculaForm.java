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
   
 ARCHIVO:     ReimpresionAprobacionSolicitudTerceraMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la reimpresion de la aprobacion/negacion hecha por el director de carrera a la solicitud de tercera matricula. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26-03-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionDirCarrera;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.UserAuthenticator;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.auth.StaticUserAuthenticator;
import org.apache.commons.vfs.impl.DefaultFileSystemConfigBuilder;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.SolicitudTerceraMatriculaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaActividadDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaInscripcionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SolicitudTerceraMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteAprobacionSolicitudTerceraForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) ReimpresionAprobacionSolicitudTerceraMatriculaForm. 
 * Bean de sesion que maneja la la aprobación/negación hecha por el director de carrera a la solicitud de tercera matricula realizada por el estudiante. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "reimpresionAprobacionSolicitudTerceraMatriculaForm")
@SessionScoped
public class ReimpresionAprobacionSolicitudTerceraMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario rastmfUsuario;
	//PARA BUSQUEDA
	private PeriodoAcademico rastmfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> rastmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico rastmfPeriodoAcademicoActivo;
	private CarreraDto rastmfCarreraDtoBuscar;
	private List<CarreraDto> rastmfListCarreraDtoBusq;
	private EstudianteJdbcDto rastmfEstudianteBuscar;
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA SOLICITUD
	private List<SolicitudTerceraMatriculaDto> rastmfListaSolicitudesDto;
	//VER MATERIAS
	private Persona rastmfPersonaSeleccionadaVer;
	private PeriodoAcademico rastmfPeriodoSolicitudesVer;
	private Carrera rastmfCarreraVer;
	private FichaInscripcionDto rastmfFichaInscripcion;
	private List<SolicitudTerceraMatriculaDto> rastmfListaMateriasSolicitadasDto;
	private Integer rastmfClickGuardarResolver;
	private Integer rastmfTipoCronograma;
	private Dependencia rastmfDependencia;
	private CronogramaActividadJdbcDto rastmfCronogramaActividad;
	private String rastmfArchivoSelSt;
	private Boolean rastmfDesactivaBotonGuardar;
	private String rastmfObservacionFinal;
	//REPORTE
	private Integer rastmfActivaReportePDF;
	private Boolean rastmfDesactivaBotonReporte;
	private List<SolicitudTerceraMatriculaDto>  rastmfListaMateriasReporte;
	private String rastmfObservacionReporte;


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
	PeriodoAcademicoServicio servRastmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servRastmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servRastmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servRastmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servRastmfPersonaServicio;
	@EJB 
	CarreraServicio servRastmfCarreraServicio;
	@EJB 
	DependenciaServicio servRastmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servRastmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servRastmfSolicitudTerceraMatriculaServicioServicioJdbc;
	@EJB 
	FichaInscripcionDtoServicioJdbc servRastmfFichaInscripcionDto;
	@EJB 
	UsuarioRolDtoServicioJdbc servRastmfUsuarioRolDto;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantesReporte
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes Reporte
	 */
	public String irAListarEstudiantesReporte(Usuario usuario) {
		rastmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		   rastmfPeriodoAcademicoActivo = servRastmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		   rastmfListaPeriodoAcademicoBusq=servRastmfPeriodoAcademicoServicio.listarTodos();
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			rastmfListCarreraDtoBusq = servRastmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rastmfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rastmfPeriodoAcademicoActivo.getPracId());
			retorno = "irAListarEstudiantesReporte";
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Metodo que sirve para buscar la lista de estudiantes que solicitaron tercera matrícula con los parámetros ingresados
	 */
	public void buscarEstudiantes(){
		rastmfListaSolicitudesDto=new ArrayList<SolicitudTerceraMatriculaDto>();
			try {
				//Verifico que se seleecione una carrera
				if(rastmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					//LISTA DE SOLICITUDES APROBADAS Y NEGADAS EN LA CARRERA
					rastmfListaSolicitudesDto=servRastmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXPrimerApellidoXIdentidadXAprobasNegadas(rastmfPeriodoAcademicoBuscar.getPracId(), rastmfCarreraDtoBuscar.getCrrId(), rastmfEstudianteBuscar.getPrsPrimerApellido(), rastmfEstudianteBuscar.getPrsIdentificacion());
				}else{
					rastmfListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
				
			} catch (SolicitudTerceraMatriculaDtoException e) {
				rastmfListaSolicitudesDto=null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.buscarEstudiantes.exception")));
			} 
	}
	
	
	/**
	 * Método que permite ir a la visualización de las materias
	 * @param solicitudesEstudiante - solicitudesEstudiante objeto que se envia como parámetro para la consulta
	 * @return La navegación hacia la página xhtml de ver matricula
	 */
	public String irVerMateriasSolicitadas(SolicitudTerceraMatriculaDto solicitudesEstudiante){
		
		String retorno =null;
		rastmfListaMateriasSolicitadasDto= new ArrayList<>();
		rastmfPersonaSeleccionadaVer= new Persona();
		rastmfPeriodoSolicitudesVer= new PeriodoAcademico();
		rastmfCarreraVer=new Carrera();
		rastmfObservacionFinal= null;
	
		    //Se busca el periodo, persona, carrera y lista de materias en las que solicito y verifico la solicitud
		try {
			rastmfPeriodoSolicitudesVer=servRastmfPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			rastmfPersonaSeleccionadaVer=servRastmfPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			rastmfCarreraVer=servRastmfCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			rastmfDependencia = servRastmfDependencialServicio.buscarFacultadXcrrId(rastmfCarreraVer.getCrrId());
			//LISTA DE SOLICITUDES EN MATERIAS DEL ESTUDIANTE SELECCIONADO
			rastmfListaMateriasSolicitadasDto=servRastmfSolicitudTerceraMatriculaDtoServicioJdbc.listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(solicitudesEstudiante.getPracId(), solicitudesEstudiante.getCrrId(), solicitudesEstudiante.getFcesId(), SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE);
			//GUARDO LA PRIMERA OBSERVACION REGISTRADA EN LA LISTA DE MATERIAS
			setRastmfObservacionReporte(rastmfListaMateriasSolicitadasDto.get(0).getSltrmtObservacionFinal());
			retorno="irVerMateriasSolicitadas";
		
		} catch (SolicitudTerceraMatriculaDtoException e) {
			rastmfPeriodoSolicitudesVer=null;
			rastmfPersonaSeleccionadaVer=null;
			rastmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  catch (PeriodoAcademicoNoEncontradoException e) {
			rastmfPeriodoSolicitudesVer=null;
			rastmfPersonaSeleccionadaVer=null;
			rastmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			rastmfPeriodoSolicitudesVer=null;
			rastmfPersonaSeleccionadaVer=null;
			rastmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			rastmfPeriodoSolicitudesVer=null;
			rastmfPersonaSeleccionadaVer=null;
			rastmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			rastmfPeriodoSolicitudesVer=null;
			rastmfPersonaSeleccionadaVer=null;
			rastmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno ;
	}
	

	
	/**
	 * Método que guarda la respuesta a la solicitud de tercera matricula en las materias solicitadas
	 * @return retorna - la navegación de la página listar estudiantes.
	 */
	public String guardarResolver(){
	String retorno = null;
		return retorno;
	}
	
	/**
	 * Método que llama al generar el reporte
	 * 
	 */
	public void llamarReporte(){
		//GENERAR REPORTE
		 ReporteAprobacionSolicitudTerceraForm.generarReporteAprobacionTercera(rastmfListaMateriasSolicitadasDto, rastmfDependencia, rastmfCarreraVer, rastmfPersonaSeleccionadaVer, rastmfPeriodoAcademicoActivo, rastmfUsuario,rastmfObservacionReporte );
		//Activo el pdf
		 rastmfActivaReportePDF=1;
	}
	
	/**
	 * Método que llama al generar el reporte
	 * 
	 */
	public String regresarListarEstudiantes(){
		//INSTANCIO EL PERIODO ACADÉMICO
		rastmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		rastmfEstudianteBuscar = new EstudianteJdbcDto();
		//rastmfListaSolicitudesDto= new ArrayList<>();
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rastmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rastmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		rastmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		rastmfClickGuardarResolver=0;
		rastmfActivaReportePDF=0;
		rastmfDesactivaBotonReporte= Boolean.TRUE;
		rastmfDesactivaBotonGuardar=Boolean.FALSE;
		// INICIO VARIABLES DE VER MATERIAS
		rastmfListaMateriasSolicitadasDto= new ArrayList<>();
		rastmfPersonaSeleccionadaVer= new Persona();
		rastmfPeriodoSolicitudesVer= new PeriodoAcademico();
		rastmfCarreraVer=new Carrera();
		rastmfObservacionFinal= null;
		rastmfObservacionReporte= null;
		buscarEstudiantes();
		return "irAListarEstudiantesReporte";
		
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros
	 */
	public void iniciarParametros(){
		//INSTANCIO EL PERIODO ACADÉMICO
		rastmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		rastmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		rastmfEstudianteBuscar = new EstudianteJdbcDto();
		rastmfListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rastmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rastmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		rastmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		rastmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		rastmfClickGuardarResolver=0;
		rastmfActivaReportePDF=0;
		rastmfDesactivaBotonReporte= Boolean.TRUE;
		rastmfDesactivaBotonGuardar=Boolean.FALSE;
		rastmfObservacionFinal= null;
		rastmfObservacionReporte= null;
		
	}
	
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		rastmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		rastmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rastmfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		rastmfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		rastmfListaSolicitudesDto = null;
				
	}
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		return "irInicio";
	}

	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(SolicitudTerceraMatriculaDto materiaSeleccionada){
		rastmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(rastmfArchivoSelSt != null){
			String nombre = materiaSeleccionada.getSltrmtDocumentoSolicitud();
			try{
				File f=new File(GeneralesConstantes.APP_PATH_ARCHIVOS_TEMPORAL+materiaSeleccionada.getSltrmtDocumentoSolicitud());
				if(f.exists())
				{
				    f.delete();
				}
				f.createNewFile(); 
				FileObject destn =  VFS.getManager().resolveFile(f.getAbsolutePath());
				UserAuthenticator auth = new StaticUserAuthenticator("", "produ", "12345.a");
				FileSystemOptions opts = new FileSystemOptions();

				DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
				FileObject fo = VFS.getManager().resolveFile(rastmfArchivoSelSt,opts);
				destn.copyFrom( fo,Selectors.SELECT_SELF);
				 destn.close();
				return new DefaultStreamedContent((InputStream) destn,GeneralesUtilidades.obtenerContentType(rastmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				rastmfArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (MalformedURLException e) {
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.no.encontrado.exception")));
			return null;
		}
		return null;
	}
	

	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	
	public Usuario getRastmfUsuario() {
		return rastmfUsuario;
	}

	public void setRastmfUsuario(Usuario rastmfUsuario) {
		this.rastmfUsuario = rastmfUsuario;
	}
	
	public PeriodoAcademico getRastmfPeriodoAcademicoBuscar() {
		return rastmfPeriodoAcademicoBuscar;
	}

	public void setRastmfPeriodoAcademicoBuscar(PeriodoAcademico rastmfPeriodoAcademicoBuscar) {
		this.rastmfPeriodoAcademicoBuscar = rastmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getRastmfCarreraDtoBuscar() {
		return rastmfCarreraDtoBuscar;
	}

	public void setRastmfCarreraDtoBuscar(CarreraDto rastmfCarreraDtoBuscar) {
		this.rastmfCarreraDtoBuscar = rastmfCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getRastmfListCarreraDtoBusq() {
		return rastmfListCarreraDtoBusq;
	}

	public void setRastmfListCarreraDtoBusq(List<CarreraDto> rastmfListCarreraDtoBusq) {
		this.rastmfListCarreraDtoBusq = rastmfListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getRastmfEstudianteBuscar() {
		return rastmfEstudianteBuscar;
	}

	public void setRastmfEstudianteBuscar(EstudianteJdbcDto rastmfEstudianteBuscar) {
		this.rastmfEstudianteBuscar = rastmfEstudianteBuscar;
	}

	public List<SolicitudTerceraMatriculaDto> getRastmfListaSolicitudesDto() {
		rastmfListaSolicitudesDto = rastmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):rastmfListaSolicitudesDto;
		return rastmfListaSolicitudesDto;
	}

	public void setRastmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> rastmfListaSolicitudesDto) {
		this.rastmfListaSolicitudesDto = rastmfListaSolicitudesDto;
	}

	public Persona getRastmfPersonaSeleccionadaVer() {
		return rastmfPersonaSeleccionadaVer;
	}

	public void setRastmfPersonaSeleccionadaVer(Persona rastmfPersonaSeleccionadaVer) {
		this.rastmfPersonaSeleccionadaVer = rastmfPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getRastmfPeriodoSolicitudesVer() {
		return rastmfPeriodoSolicitudesVer;
	}

	public void setRastmfPeriodoSolicitudesVer(PeriodoAcademico rastmfPeriodoSolicitudesVer) {
		this.rastmfPeriodoSolicitudesVer = rastmfPeriodoSolicitudesVer;
	}

	public Carrera getRastmfCarreraVer() {
		return rastmfCarreraVer;
	}

	public void setRastmfCarreraVer(Carrera rastmfCarreraVer) {
		this.rastmfCarreraVer = rastmfCarreraVer;
	}

	public List<SolicitudTerceraMatriculaDto> getRastmfListaMateriasSolicitadasDto() {
		rastmfListaMateriasSolicitadasDto = rastmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):rastmfListaMateriasSolicitadasDto;
		return rastmfListaMateriasSolicitadasDto;
	}

	public void setRastmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> rastmfListaMateriasSolicitadasDto) {
		this.rastmfListaMateriasSolicitadasDto = rastmfListaMateriasSolicitadasDto;
	}

	
	public Integer getRastmfClickGuardarResolver() {
		return rastmfClickGuardarResolver;
	}

	
	public void setRastmfClickGuardarResolver(Integer rastmfClickGuardarResolver) {
		this.rastmfClickGuardarResolver = rastmfClickGuardarResolver;
	}
	

	
	public Integer getRastmfActivaReportePDF() {
		return rastmfActivaReportePDF;
	}

	public void setRastmfActivaReportePDF(Integer rastmfActivaReportePDF) {
		this.rastmfActivaReportePDF = rastmfActivaReportePDF;
	}

	public List<PeriodoAcademico> getRastmfListaPeriodoAcademicoBusq() {
		rastmfListaPeriodoAcademicoBusq = rastmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):rastmfListaPeriodoAcademicoBusq;
		return rastmfListaPeriodoAcademicoBusq;
	}

	public void setRastmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> rastmfListaPeriodoAcademicoBusq) {
		this.rastmfListaPeriodoAcademicoBusq = rastmfListaPeriodoAcademicoBusq;
	}

	public Integer getRastmfTipoCronograma() {
		return rastmfTipoCronograma;
	}

	public void setRastmfTipoCronograma(Integer rastmfTipoCronograma) {
		this.rastmfTipoCronograma = rastmfTipoCronograma;
	}

	public Dependencia getRastmfDependencia() {
		return rastmfDependencia;
	}

	public void setRastmfDependencia(Dependencia rastmfDependencia) {
		this.rastmfDependencia = rastmfDependencia;
	}

	public CronogramaActividadJdbcDto getRastmfCronogramaActividad() {
		return rastmfCronogramaActividad;
	}

	public void setRastmfCronogramaActividad(CronogramaActividadJdbcDto rastmfCronogramaActividad) {
		this.rastmfCronogramaActividad = rastmfCronogramaActividad;
	}

	public PeriodoAcademico getRastmfPeriodoAcademicoActivo() {
		return rastmfPeriodoAcademicoActivo;
	}

	public void setRastmfPeriodoAcademicoActivo(PeriodoAcademico rastmfPeriodoAcademicoActivo) {
		this.rastmfPeriodoAcademicoActivo = rastmfPeriodoAcademicoActivo;
	}

	public String getRastmfArchivoSelSt() {
		return rastmfArchivoSelSt;
	}

	public void setRastmfArchivoSelSt(String rastmfArchivoSelSt) {
		this.rastmfArchivoSelSt = rastmfArchivoSelSt;
	}

	public FichaInscripcionDto getRastmfFichaInscripcion() {
		return rastmfFichaInscripcion;
	}

	public void setRastmfFichaInscripcion(FichaInscripcionDto rastmfFichaInscripcion) {
		this.rastmfFichaInscripcion = rastmfFichaInscripcion;
	}

	public List<SolicitudTerceraMatriculaDto> getRastmfListaMateriasReporte() {
		rastmfListaMateriasReporte = rastmfListaMateriasReporte==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):rastmfListaMateriasReporte;
		return rastmfListaMateriasReporte;
	}

	public void setRastmfListaMateriasReporte(List<SolicitudTerceraMatriculaDto> rastmfListaMateriasReporte) {
		this.rastmfListaMateriasReporte = rastmfListaMateriasReporte;
	}

	public Boolean getRastmfDesactivaBotonReporte() {
		return rastmfDesactivaBotonReporte;
	}

	public void setRastmfDesactivaBotonReporte(Boolean rastmfDesactivaBotonReporte) {
		this.rastmfDesactivaBotonReporte = rastmfDesactivaBotonReporte;
	}

	public Boolean getRastmfDesactivaBotonGuardar() {
		return rastmfDesactivaBotonGuardar;
	}

	public void setRastmfDesactivaBotonGuardar(Boolean rastmfDesactivaBotonGuardar) {
		this.rastmfDesactivaBotonGuardar = rastmfDesactivaBotonGuardar;
	}

	public String getRastmfObservacionFinal() {
		return rastmfObservacionFinal;
	}

	public void setRastmfObservacionFinal(String rastmfObservacionFinal) {
		this.rastmfObservacionFinal = rastmfObservacionFinal;
	}

	public String getRastmfObservacionReporte() {
		return rastmfObservacionReporte;
	}

	public void setRastmfObservacionReporte(String rastmfObservacionReporte) {
		this.rastmfObservacionReporte = rastmfObservacionReporte;
	}
	
	

}
