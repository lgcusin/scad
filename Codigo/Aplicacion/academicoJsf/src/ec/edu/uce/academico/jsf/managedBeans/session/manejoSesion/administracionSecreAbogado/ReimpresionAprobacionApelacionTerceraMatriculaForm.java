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
   
 ARCHIVO:     ReimpresionAprobacionApelacionTerceraMatriculaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la reimpresion de aprobacion/negacion hecha por el Consejo Directivo a la apelación de tercera matricula realizada por el estudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-03-2018		 Marcelo Quishpe                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSecreAbogado;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
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
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteAprobacionApelacionTerceraForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) ReimpresionAprobacionApelacionTerceraMatriculaForm. 
 * Bean de sesion que maneja la la aprobación/negación hecha por el Consejo Directivo a la apelación de tercera matricula realizada por el estudiante. 
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name = "reimpresionAprobacionApelacionTerceraMatriculaForm")
@SessionScoped
public class ReimpresionAprobacionApelacionTerceraMatriculaForm implements Serializable {

	private static final long serialVersionUID = -3997600778963238092L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/

	//GENERAL
	private Usuario raatmfUsuario;
	//PARA BUSQUEDA
	private PeriodoAcademico raatmfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> raatmfListaPeriodoAcademicoBusq;
	private PeriodoAcademico raatmfPeriodoAcademicoActivo;
	private CarreraDto raatmfCarreraDtoBuscar;
	private List<CarreraDto> raatmfListCarreraDtoBusq;
	private EstudianteJdbcDto raatmfEstudianteBuscar;
	//PARA VISUALIZACIÓN
	//LISTA DE ESTUDIANTES QUE REALIZARON LA APELACION
	private List<SolicitudTerceraMatriculaDto> raatmfListaSolicitudesDto;
	//VER MATERIAS
	private Persona raatmfPersonaSeleccionadaVer;
	private PeriodoAcademico raatmfPeriodoSolicitudesVer;
	private Carrera raatmfCarreraVer;
	private FichaInscripcionDto raatmfFichaInscripcion;
	private List<SolicitudTerceraMatriculaDto> raatmfListaMateriasSolicitadasDto;
	private Integer raatmfValidadorSeleccion;
	private Integer raatmfClickGuardarResolver;
	private Integer raatmfTipoCronograma;
	private Dependencia raatmfDependencia;
	private CronogramaActividadJdbcDto raatmfCronogramaActividad;
	private String raatmfArchivoSelSt;
	
	// MODAL SUBIR RESOLUCION
	private Integer raatmfActivaModalCargarResolucion = 1;
	private SolicitudTerceraMatriculaDto raatmfMateriaSeleccionada;
	
	private String raatmfNombreArchivoSubido; 
	private String raatmfNombreArchivoAuxiliar;
	
	private Integer raatmfActivaReportePDF; //activa el modal para presenar el reporte
	private Boolean raatmfDesactivaBotonReporte; // desactiva el boton reporte
	private Boolean raatmfDesactivaBotonGuardar;  //desactiva el boton Guardar
	//PARA EL REPORTE
	private String raatmfObservacionFinal;
	private List<SolicitudTerceraMatriculaDto>  raatmfListaMateriasReporte;
	private String raatmfObservacionReporte;

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
	PeriodoAcademicoServicio servRaatmfPeriodoAcademicoServicio;
	@EJB
	CarreraDtoServicioJdbc servRaatmfCarreraDtoServicioJdbc;
	@EJB 
	EstudianteDtoServicioJdbc  servRaatmfEstudianteDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaDtoServicioJdbc servRaatmfSolicitudTerceraMatriculaDtoServicioJdbc;
	@EJB 
	PersonaServicio servRaatmfPersonaServicio;
	@EJB 
	CarreraServicio servRaatmfCarreraServicio;
	@EJB 
	DependenciaServicio servRaatmfDependencialServicio;
	@EJB 
	CronogramaActividadDtoServicioJdbc servRaatmfCronogramaActividadDtoServicioJdbc;
	@EJB 
	SolicitudTerceraMatriculaServicio servRaatmfSolicitudTerceraMatriculaServicioServicioJdbc;
	@EJB 
	FichaInscripcionDtoServicioJdbc servRaatmfFichaInscripcionDto;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegación hacia la página de listarEstudiantes
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar estudiantes
	 */
	public String irResolverApelacionListarEstudiantesReporte(Usuario usuario) {
		raatmfUsuario = usuario;
		String retorno = null;
		try {
			//INICIO PARAMETROS
		iniciarParametros();
			//BUSCA EL PERIODO ACADEMICO ACTIVO
		   raatmfPeriodoAcademicoActivo = servRaatmfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		   raatmfListaPeriodoAcademicoBusq=servRaatmfPeriodoAcademicoServicio.listarTodos();
			//BUSCO LAS CARRERAS QUE ESTAN ASIGNADOS
			raatmfListCarreraDtoBusq = servRaatmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(raatmfUsuario.getUsrId(), RolConstantes.ROL_SECREABOGADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, raatmfPeriodoAcademicoActivo.getPracId());
			retorno = "irApruebaApelacionListarEstudReporte";
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
		raatmfListaSolicitudesDto=new ArrayList<SolicitudTerceraMatriculaDto>();
			try {
				//Verifico que se seleecione una carrera
				if(raatmfCarreraDtoBuscar.getCrrId()!=GeneralesConstantes.APP_ID_BASE){
					//LISTA DE APELACIONES APROBADAS Y NEGADAS EN LA CARRERA
					raatmfListaSolicitudesDto=servRaatmfSolicitudTerceraMatriculaDtoServicioJdbc.listarApelacionesXPeriodoXCarreraXPrimerApellidoXIdentidadXAprobasNegadas(raatmfPeriodoAcademicoBuscar.getPracId(), raatmfCarreraDtoBuscar.getCrrId(), raatmfEstudianteBuscar.getPrsPrimerApellido(), raatmfEstudianteBuscar.getPrsIdentificacion());
				}else{
					raatmfListaSolicitudesDto=null;
					FacesUtil.limpiarMensaje();
				    FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.buscarEstudiantes.seleccionar.carrera.validacion")));
				}
			} catch (SolicitudTerceraMatriculaDtoException e) {
				raatmfListaSolicitudesDto=null;
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
		String retorno=null;
		raatmfListaMateriasSolicitadasDto= new ArrayList<>();
		raatmfPersonaSeleccionadaVer= new Persona();
		raatmfPeriodoSolicitudesVer= new PeriodoAcademico();
		raatmfCarreraVer=new Carrera();
		raatmfObservacionFinal= null;
		//Seteo como activo la opción de descarga del archivo
		for (SolicitudTerceraMatriculaDto item : raatmfListaMateriasSolicitadasDto) {
			item.setVisualizador(false);
			item.setRespuestaSolicitud(GeneralesConstantes.APP_ID_BASE);
		}
		    //Se busca el periodo, persona, carrera y lista de materias en las que solicito y verifico la solicitud
		try {
			raatmfPeriodoSolicitudesVer=servRaatmfPeriodoAcademicoServicio.buscarPorId(solicitudesEstudiante.getPracId());
			raatmfPersonaSeleccionadaVer=servRaatmfPersonaServicio.buscarPorId(solicitudesEstudiante.getPrsId());
			raatmfCarreraVer=servRaatmfCarreraServicio.buscarPorId(solicitudesEstudiante.getCrrId());
			raatmfDependencia = servRaatmfDependencialServicio.buscarFacultadXcrrId(raatmfCarreraVer.getCrrId());
			//LISTA DE SOLICITUDES EN MATERIAS DEL ESTUDIANTE SELECCIONADO
			raatmfListaMateriasSolicitadasDto=servRaatmfSolicitudTerceraMatriculaDtoServicioJdbc.listarApelacionesXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(solicitudesEstudiante.getPracId(), solicitudesEstudiante.getCrrId(), solicitudesEstudiante.getFcesId(), SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_VALUE);
			//GUARDO LA PRIMERA OBSERVACION REGISTRADA EN LA LISTA DE MATERIAS SI LA LISTA EXISTE
			if((raatmfListaMateriasSolicitadasDto!=null)||(raatmfListaMateriasSolicitadasDto.size()>0)){
			setRaatmfObservacionReporte(raatmfListaMateriasSolicitadasDto.get(0).getSltrmtObservacionFinal());
            }
           	retorno= "irVerMateriasApeladas";
           	
		} catch (SolicitudTerceraMatriculaDtoException e) {
			raatmfPeriodoSolicitudesVer=null;
			raatmfPersonaSeleccionadaVer=null;
			raatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  catch (PeriodoAcademicoNoEncontradoException e) {
			raatmfPeriodoSolicitudesVer=null;
			raatmfPersonaSeleccionadaVer=null;
			raatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			raatmfPeriodoSolicitudesVer=null;
			raatmfPersonaSeleccionadaVer=null;
			raatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			raatmfPeriodoSolicitudesVer=null;
			raatmfPersonaSeleccionadaVer=null;
			raatmfListaMateriasSolicitadasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			raatmfPeriodoSolicitudesVer=null;
			raatmfPersonaSeleccionadaVer=null;
			raatmfListaMateriasSolicitadasDto=null;
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
		return retorno;
	}
	
	/**
	 * Método que llama al reporte
	 */
	public void llamarReporte(){
		//GENERAR REPORTE
		 ReporteAprobacionApelacionTerceraForm.generarReporteAprobacionApelacionTercera(raatmfListaMateriasSolicitadasDto, raatmfDependencia, raatmfCarreraVer, raatmfPersonaSeleccionadaVer, raatmfPeriodoAcademicoActivo, raatmfUsuario,raatmfObservacionReporte );
		//ACTIVO MODAL PARA QUE SE ABRA EL REPORTE GENERADO Y DESCARGUE
		 raatmfActivaReportePDF=1;
	}
	
	/**
	 * Método que llama al generar el reporte
	 * 
	 */
	public String regresarListarEstudiantes(){
		//INSTANCIO EL PERIODO ACADÉMICO
		raatmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		raatmfEstudianteBuscar = new EstudianteJdbcDto();
		//raatmfListaSolicitudesDto= new ArrayList<>();
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		raatmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		raatmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		raatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		raatmfClickGuardarResolver=0;
		raatmfActivaReportePDF=0;
		raatmfDesactivaBotonReporte= Boolean.TRUE;
		raatmfDesactivaBotonGuardar=Boolean.FALSE;
		// INICIO VARIABLES DE VER MATERIAS
		raatmfListaMateriasSolicitadasDto= new ArrayList<>();
		raatmfPersonaSeleccionadaVer= new Persona();
		raatmfPeriodoSolicitudesVer= new PeriodoAcademico();
		raatmfCarreraVer=new Carrera();
		raatmfObservacionFinal= null;
		raatmfObservacionReporte= null;
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
		raatmfPeriodoAcademicoBuscar = new PeriodoAcademico();
		//INSTANCIO LA CARRERA DTO
		raatmfCarreraDtoBuscar = new CarreraDto();
		//INSTANCIO EL ESTUDIANTE JDB DTO
		raatmfEstudianteBuscar = new EstudianteJdbcDto();
		raatmfListaSolicitudesDto=null;
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		raatmfEstudianteBuscar.setPrsIdentificacion("");
		//SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		raatmfEstudianteBuscar.setPrsPrimerApellido("");
		//SETEO EL ID DE CARRERA DTO
		raatmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		raatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		raatmfValidadorSeleccion=GeneralesConstantes.APP_ID_BASE;
		raatmfClickGuardarResolver=0;
		raatmfActivaReportePDF=0;
		raatmfActivaModalCargarResolucion = 0;
		raatmfDesactivaBotonReporte= Boolean.TRUE;
		raatmfDesactivaBotonGuardar=Boolean.FALSE;
		raatmfMateriaSeleccionada= null;
		raatmfObservacionReporte= null;
		
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda 
	 */
	public void limpiar(){
		// SETEO EL ID DE CARRERA DTO
		raatmfCarreraDtoBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		raatmfPeriodoAcademicoBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		raatmfEstudianteBuscar.setPrsIdentificacion("");
		// SETEO LA IDENTIFICACIÓN DEL ESTUDIANTE
		raatmfEstudianteBuscar.setPrsPrimerApellido("");
		// SETEO LA LISTA DE SOLICITUDES
		raatmfListaSolicitudesDto = null;
				
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
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(SolicitudTerceraMatriculaDto materiaSeleccionada){
		raatmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_APELACION_TERCERA_MATRICULA+materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(raatmfArchivoSelSt != null){
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
				FileObject fo = VFS.getManager().resolveFile(raatmfArchivoSelSt,opts);
				destn.copyFrom( fo,Selectors.SELECT_SELF);
				 destn.close();
				return new DefaultStreamedContent((InputStream) destn,GeneralesUtilidades.obtenerContentType(raatmfArchivoSelSt),nombre);
			}catch(FileNotFoundException fnfe){
				raatmfArchivoSelSt = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AprobacionSolicitudTerceraMatricula.StreamedContent.descargar.archivo.exception")));
				return null;
			} catch (Exception e) {
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
	
	public Usuario getRaatmfUsuario() {
		return raatmfUsuario;
	}

	public void setRaatmfUsuario(Usuario raatmfUsuario) {
		this.raatmfUsuario = raatmfUsuario;
	}
	
	public PeriodoAcademico getRaatmfPeriodoAcademicoBuscar() {
		return raatmfPeriodoAcademicoBuscar;
	}

	public void setRaatmfPeriodoAcademicoBuscar(PeriodoAcademico raatmfPeriodoAcademicoBuscar) {
		this.raatmfPeriodoAcademicoBuscar = raatmfPeriodoAcademicoBuscar;
	}

	public CarreraDto getRaatmfCarreraDtoBuscar() {
		return raatmfCarreraDtoBuscar;
	}

	public void setRaatmfCarreraDtoBuscar(CarreraDto raatmfCarreraDtoBuscar) {
		this.raatmfCarreraDtoBuscar = raatmfCarreraDtoBuscar;
	}

	
	public List<CarreraDto> getRaatmfListCarreraDtoBusq() {
		return raatmfListCarreraDtoBusq;
	}

	public void setRaatmfListCarreraDtoBusq(List<CarreraDto> raatmfListCarreraDtoBusq) {
		this.raatmfListCarreraDtoBusq = raatmfListCarreraDtoBusq;
	}

	public EstudianteJdbcDto getRaatmfEstudianteBuscar() {
		return raatmfEstudianteBuscar;
	}

	public void setRaatmfEstudianteBuscar(EstudianteJdbcDto raatmfEstudianteBuscar) {
		this.raatmfEstudianteBuscar = raatmfEstudianteBuscar;
	}

	public List<SolicitudTerceraMatriculaDto> getRaatmfListaSolicitudesDto() {
		raatmfListaSolicitudesDto = raatmfListaSolicitudesDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):raatmfListaSolicitudesDto;
		return raatmfListaSolicitudesDto;
	}

	public void setRaatmfListaSolicitudesDto(List<SolicitudTerceraMatriculaDto> raatmfListaSolicitudesDto) {
		this.raatmfListaSolicitudesDto = raatmfListaSolicitudesDto;
	}

	public Persona getRaatmfPersonaSeleccionadaVer() {
		return raatmfPersonaSeleccionadaVer;
	}

	public void setRaatmfPersonaSeleccionadaVer(Persona raatmfPersonaSeleccionadaVer) {
		this.raatmfPersonaSeleccionadaVer = raatmfPersonaSeleccionadaVer;
	}

	public PeriodoAcademico getRaatmfPeriodoSolicitudesVer() {
		return raatmfPeriodoSolicitudesVer;
	}

	public void setRaatmfPeriodoSolicitudesVer(PeriodoAcademico raatmfPeriodoSolicitudesVer) {
		this.raatmfPeriodoSolicitudesVer = raatmfPeriodoSolicitudesVer;
	}

	public Carrera getRaatmfCarreraVer() {
		return raatmfCarreraVer;
	}

	public void setRaatmfCarreraVer(Carrera raatmfCarreraVer) {
		this.raatmfCarreraVer = raatmfCarreraVer;
	}

	public List<SolicitudTerceraMatriculaDto> getRaatmfListaMateriasSolicitadasDto() {
		raatmfListaMateriasSolicitadasDto = raatmfListaMateriasSolicitadasDto==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):raatmfListaMateriasSolicitadasDto;
		return raatmfListaMateriasSolicitadasDto;
	}

	public void setRaatmfListaMateriasSolicitadasDto(List<SolicitudTerceraMatriculaDto> raatmfListaMateriasSolicitadasDto) {
		this.raatmfListaMateriasSolicitadasDto = raatmfListaMateriasSolicitadasDto;
	}

	public Integer getRaatmfValidadorSeleccion() {
		return raatmfValidadorSeleccion;
	}

	public void setRaatmfValidadorSeleccion(Integer raatmfValidadorSeleccion) {
		this.raatmfValidadorSeleccion = raatmfValidadorSeleccion;
	}

	

	public List<PeriodoAcademico> getRaatmfListaPeriodoAcademicoBusq() {
		raatmfListaPeriodoAcademicoBusq = raatmfListaPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademico>()):raatmfListaPeriodoAcademicoBusq;
		return raatmfListaPeriodoAcademicoBusq;
	}

	public void setRaatmfListaPeriodoAcademicoBusq(List<PeriodoAcademico> raatmfListaPeriodoAcademicoBusq) {
		this.raatmfListaPeriodoAcademicoBusq = raatmfListaPeriodoAcademicoBusq;
	}

	public Integer getRaatmfTipoCronograma() {
		return raatmfTipoCronograma;
	}

	public void setRaatmfTipoCronograma(Integer raatmfTipoCronograma) {
		this.raatmfTipoCronograma = raatmfTipoCronograma;
	}

	public Dependencia getRaatmfDependencia() {
		return raatmfDependencia;
	}

	public void setRaatmfDependencia(Dependencia raatmfDependencia) {
		this.raatmfDependencia = raatmfDependencia;
	}

	public CronogramaActividadJdbcDto getRaatmfCronogramaActividad() {
		return raatmfCronogramaActividad;
	}

	public void setRaatmfCronogramaActividad(CronogramaActividadJdbcDto raatmfCronogramaActividad) {
		this.raatmfCronogramaActividad = raatmfCronogramaActividad;
	}

	public PeriodoAcademico getRaatmfPeriodoAcademicoActivo() {
		return raatmfPeriodoAcademicoActivo;
	}

	public void setRaatmfPeriodoAcademicoActivo(PeriodoAcademico raatmfPeriodoAcademicoActivo) {
		this.raatmfPeriodoAcademicoActivo = raatmfPeriodoAcademicoActivo;
	}

	public String getRaatmfArchivoSelSt() {
		return raatmfArchivoSelSt;
	}

	public void setRaatmfArchivoSelSt(String raatmfArchivoSelSt) {
		this.raatmfArchivoSelSt = raatmfArchivoSelSt;
	}

	public Integer getRaatmfActivaModalCargarResolucion() {
		return raatmfActivaModalCargarResolucion;
	}

	public void setRaatmfActivaModalCargarResolucion(Integer raatmfActivaModalCargarResolucion) {
		this.raatmfActivaModalCargarResolucion = raatmfActivaModalCargarResolucion;
	}

	public SolicitudTerceraMatriculaDto getRaatmfMateriaSeleccionada() {
		return raatmfMateriaSeleccionada;
	}

	public void setRaatmfMateriaSeleccionada(SolicitudTerceraMatriculaDto raatmfMateriaSeleccionada) {
		this.raatmfMateriaSeleccionada = raatmfMateriaSeleccionada;
	}

	public Integer getRaatmfClickGuardarResolver() {
		return raatmfClickGuardarResolver;
	}

	public void setRaatmfClickGuardarResolver(Integer raatmfClickGuardarResolver) {
		this.raatmfClickGuardarResolver = raatmfClickGuardarResolver;
	}

	public FichaInscripcionDto getRaatmfFichaInscripcion() {
		return raatmfFichaInscripcion;
	}

	public void setRaatmfFichaInscripcion(FichaInscripcionDto raatmfFichaInscripcion) {
		this.raatmfFichaInscripcion = raatmfFichaInscripcion;
	}

	public String getRaatmfNombreArchivoSubido() {
		return raatmfNombreArchivoSubido;
	}

	public void setRaatmfNombreArchivoSubido(String raatmfNombreArchivoSubido) {
		this.raatmfNombreArchivoSubido = raatmfNombreArchivoSubido;
	}

	public String getRaatmfNombreArchivoAuxiliar() {
		return raatmfNombreArchivoAuxiliar;
	}

	public void setRaatmfNombreArchivoAuxiliar(String raatmfNombreArchivoAuxiliar) {
		this.raatmfNombreArchivoAuxiliar = raatmfNombreArchivoAuxiliar;
	}

	public String getRaatmfObservacionFinal() {
		return raatmfObservacionFinal;
	}

	public void setRaatmfObservacionFinal(String raatmfObservacionFinal) {
		this.raatmfObservacionFinal = raatmfObservacionFinal;
	}

	public List<SolicitudTerceraMatriculaDto> getRaatmfListaMateriasReporte() {
		raatmfListaMateriasReporte = raatmfListaMateriasReporte==null?(new ArrayList<SolicitudTerceraMatriculaDto>()):raatmfListaMateriasReporte;
	return raatmfListaMateriasReporte;
	}

	public void setRaatmfListaMateriasReporte(List<SolicitudTerceraMatriculaDto> raatmfListaMateriasReporte) {
		this.raatmfListaMateriasReporte = raatmfListaMateriasReporte;
	}

	public Boolean getRaatmfDesactivaBotonGuardar() {
		return raatmfDesactivaBotonGuardar;
	}

	public void setRaatmfDesactivaBotonGuardar(Boolean raatmfDesactivaBotonGuardar) {
		this.raatmfDesactivaBotonGuardar = raatmfDesactivaBotonGuardar;
	}

	public String getRaatmfObservacionReporte() {
		return raatmfObservacionReporte;
	}

	public void setRaatmfObservacionReporte(String raatmfObservacionReporte) {
		this.raatmfObservacionReporte = raatmfObservacionReporte;
	}

	public Integer getRaatmfActivaReportePDF() {
		return raatmfActivaReportePDF;
	}

	public void setRaatmfActivaReportePDF(Integer raatmfActivaReportePDF) {
		this.raatmfActivaReportePDF = raatmfActivaReportePDF;
	}

	public Boolean getRaatmfDesactivaBotonReporte() {
		return raatmfDesactivaBotonReporte;
	}

	public void setRaatmfDesactivaBotonReporte(Boolean raatmfDesactivaBotonReporte) {
		this.raatmfDesactivaBotonReporte = raatmfDesactivaBotonReporte;
	}
	
	
	
	

}
