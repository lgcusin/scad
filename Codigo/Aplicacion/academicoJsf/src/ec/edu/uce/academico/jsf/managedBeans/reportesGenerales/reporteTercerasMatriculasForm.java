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
   
 ARCHIVO:     ReporteParaleloForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones del reporte de paralelo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
03-ABRIL-2018			Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportesGenerales;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.ReportesGeneralesDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoException;
import ec.edu.uce.academico.ejb.excepciones.ReportesGeneralesDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReporteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) ReporteParaleloForm
 * Managed Bean que maneja las peticiones del reporte de paralelo.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="reporteTercerasMatriculasForm")
@SessionScoped
public class reporteTercerasMatriculasForm implements Serializable{

	private static final long serialVersionUID = -7458138879145808577L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/

	//OBJETOS
	private Usuario rtmfUsuario;
	private PeriodoAcademico rtmfPeriodoAcademico;
	private DependenciaDto rtmfDependenciaDto;
	private Carrera rmnfCarrera;
	private CarreraDto rtmfCarreraDto;
	private NivelDto rtmfNivelDto;

	private String rtmfTipoCarrera;  //diferenciar el tipo de carrera que ingreso - por carreras
	private Integer rtmfTipoUsuario; //diferenciar el tipo de usuario que ingreso - por carreras
	private Integer rtmfDpnId; //diferenciar el tipo de usuario que ingreso - por carreras
	private List<ReportesGeneralesDto> rtmfListEstadosTercerasBusq;
	
	private ReportesGeneralesDto rtmTercerasBuscar;

	
	private Integer rtmfEstadoTerceras; 
	
	//LISTA DE OBJETOS
	private List<PeriodoAcademico> rtmfListPeriodoAcademico;
	private List<Dependencia> rtmfListDependencia;
	private List<CarreraDto> rtmfListCarreraDto;
	private List<Nivel> rtmfListNivel;
	private List<Carrera> rtmfListCarrera;

	//AUXILIARES		
	private Integer rtmfSeleccionarTodos;
	private String rtmfTituloModal;
	private String rtmfMensajeModal;
	private String rtmfMensajeActivacion;
	private Integer rtmfOpcionSeleccionada;
	private boolean rtmfHabilitarPeriodo;
	private boolean rtmfHabilitarExportar;

	private Integer rtmfValidadorClick;
	private Integer rtmfValidadorEdicion;
	private boolean rtmfHabilitadorNivel;
	private boolean rtmfHabilitadorSeleccion;
	private boolean rtmfHabilitadorGuardar;
	
	//PARA HABILITAR
	private Integer rtmfHabilitarBoton;

	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
		
	// OBJETOS PARA EDITAR Y CREAR NUEVO
		private ParaleloDto rtmfParaleloDtoEditar;
		private CarreraDto rtmfCarreraDtoEditar;
		private Carrera rtmfCarreraEditar;
		private Paralelo rtmfParalelo;
	
	
	@PostConstruct
	public void inicializar(){
		
	}
	
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB 
	PeriodoAcademicoServicio servRpfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servRpfCarreraDto;
	@EJB
	CarreraServicio servRpfCarrera;
	@EJB
	NivelServicio servNivelDtoServicio;
	@EJB
	private UsuarioRolServicio servRpfUsuarioRolServicio;
	@EJB
	DependenciaServicio servRpfDependenciaServicio;
	@EJB
	EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	@EJB
	private ReporteDtoServicioJdbc servReporteDtoServicioJdbc;
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/

	
	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de Administración .
	 */
	
	public String irReporteTercerasMatriculas(Usuario usuario) { 
		rtmfUsuario= usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
						rtmfTipoUsuario = RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue();
					}
					rtmfTipoCarrera="carrrtmfTipoUsuarioeras";
					rtmfHabilitarPeriodo=true;
				}
			}
			rtmfPeriodoAcademico = new PeriodoAcademico();
			rtmfPeriodoAcademico = servRpfPeriodoAcademico.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			llenarDependencia();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteTercerasMatriculas();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteTercerasMatriculas();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
			iniciarParametrosReporteTercerasMatriculas();
		}
		iniciarParametrosReporteTercerasMatriculas();
		return "irReporteTercerasMatriculas";
	}
	
	
	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al iniciar la funcionalidad 
	 * @return navegacion al inicio.
	  */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	
	public String irInicioReporte() {
		limpiarReporte();
		return "irInicio";
	}
	
	/**
	 * Método para limpiar los parámetros de busqueda ingresados
	 * @return 
	 */
	public void limpiar()  {
		iniciarParametrosReporteTercerasMatriculas();
	}
	
	public void limpiarReporte()  {
		iniciarParametrosReporteTercerasMatriculas();
	}
	
	
		
	public void iniciarParametrosReporteTercerasMatriculas(){
		//iniciar el objeto de búsqueda
		rtmTercerasBuscar = new ReportesGeneralesDto();
		//inicio la lista de peridodos academicos
			try {
				rtmfListPeriodoAcademico = servRpfPeriodoAcademico.listarTodos();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			} 
		
		
		//inicio el validador del click en 0
		rtmfValidadorClick = 0;
		rtmfPeriodoAcademico= new PeriodoAcademico();
	}
	

	
	public void verificarClickNo(){
		rtmfValidadorClick  = 0;
	}

	public void llenarDependencia(){
		rtmfListDependencia= null;
		if(rtmfTipoUsuario == RolConstantes.ROL_CONSULTOREPORTES_VALUE.intValue()){
				try {
					rtmfListDependencia= servRpfDependenciaServicio.listarFacultadesxUsuario(rtmfUsuario.getUsrId());
				} catch (DependenciaNoEncontradoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// llamo al servicio de Dependecia para listar todos
		}
	}

	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir(){
		//			try {
		if(rtmfListEstadosTercerasBusq.size() > 0){
			generarReporteTerceras(rtmfListEstadosTercerasBusq);
			habilitaModalImpresion();
		}else{
			bloqueaModal();
		}
		rtmfHabilitarBoton = 0;
	

	}

	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir2(){
		//					try {
		if(rtmfListEstadosTercerasBusq.size() > 0){
			generarReporteTerceras(rtmfListEstadosTercerasBusq);
			habilitaModalImpresion2();
		}else{
			bloqueaModal();
		}
		rtmfHabilitarBoton = 0;
		//					} catch (HorarioAcademicoDtoException e) {
		//						FacesUtil.limpiarMensaje();
		//						FacesUtil.mensajeError(e.getMessage());
		//					} catch (HorarioAcademicoDtoNoEncontradoException e) {
		//						FacesUtil.limpiarMensaje();
		//						FacesUtil.mensajeError(e.getMessage());
		//					}

	}


	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		rtmfValidadorClick = 0;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		rtmfValidadorClick = 1;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion2(){
		rtmfValidadorClick = 2;
	}

	
	/**
	 * verifica que haga click en el boton buscar 
	 */
	public void buscarEstadosTercerasMatriculas(){
		try {
			
			 if (rtmfEstadoTerceras == GeneralesConstantes.APP_ID_BASE){
						FacesUtil.mensajeError("Debe seleccionar el estado");
			}else {
				rtmfListEstadosTercerasBusq=servReporteDtoServicioJdbc.buscarTotalSolicitudesTerceras(rtmfPeriodoAcademico.getPracId(),rtmfEstadoTerceras,rtmfListDependencia);
				System.out.println(rtmfPeriodoAcademico.getPracId());
				for (ReportesGeneralesDto item : rtmfListEstadosTercerasBusq) {
					System.out.println(item.getCrrDescripcion());
					System.out.println(item.getPrsPrimerApellido());
					System.out.println(item.getPrsNombres());
					
					
				}
				rtmfHabilitarExportar=false;
			}
			
		} catch (ReportesGeneralesDtoException e) {
			e.printStackTrace();
			rtmfHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ReportesGeneralesDtoNoEncontradoException e) {
			rtmfHabilitarExportar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
		}
	
		
	}	
	

	/**
	 * Método que genera el reporte
	 */
	public void generarReporteTerceras(List<ReportesGeneralesDto> listaEstadosTerceras){
		try {

			int totalMatriculados = 0;
			String facultad = null;
			for (ReportesGeneralesDto item : listaEstadosTerceras) {
				//totalInscritos = totalInscritos + item.getMlcrprInscritos().intValue();
				totalMatriculados = totalMatriculados + item.getRcesNumeroMatriculados().intValue();
				facultad = item.getDpnDescripcion();
			}

			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			
			
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE HORARIO *********//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "reporteTotalMatriculados";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			PeriodoAcademico periodoAcademico = new PeriodoAcademico();
			
			periodoAcademico = servRpfPeriodoAcademico.buscarPorId(rtmfPeriodoAcademico.getPracId());
			//periodoAcademico = servRpfPeriodoAcademicoServicio.buscarPorId(rmnfPeriodoAcademico.getPracId());

			frmRrmParametros.put("periodo", periodoAcademico.getPracDescripcion());
			//frmRrmParametros.put("facultad", facultad);
			frmRrmParametros.put("carrera", rmnfCarrera.getCrrDescripcion());
			frmRrmParametros.put("total_matriculados", String.valueOf(totalMatriculados));


			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoHorario = null;


			for (ReportesGeneralesDto item : listaEstadosTerceras) {
				datoHorario = new HashMap<String, Object>();

				datoHorario.put("carrera", item.getCrrDetalle());
				datoHorario.put("nivel", item.getNvlDescripcion());
				datoHorario.put("asignatura", item.getMtrDescripcion());
				datoHorario.put("paralelo", item.getPrlDescripcion());
				datoHorario.put("aula", item.getAlaDescripcion());
				datoHorario.put("cupo", item.getMlcrprCupo().toString());
				datoHorario.put("inscritos", item.getMlcrprInscritos().toString());
				datoHorario.put("matriculados", item.getRcesNumeroMatriculados().toString());

				frmRrmCampos.add(datoHorario);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportesGenerales/archivosJasper");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");


			//			frmRrmCampos.add(datoHorario);
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 


	}



	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	
	public Usuario getRtmfUsuario() {
		return rtmfUsuario;
	}


	public void setRtmfUsuario(Usuario rtmfUsuario) {
		this.rtmfUsuario = rtmfUsuario;
	}


	public PeriodoAcademico getRtmfPeriodoAcademico() {
		return rtmfPeriodoAcademico;
	}


	public void setRtmfPeriodoAcademico(PeriodoAcademico rtmfPeriodoAcademico) {
		this.rtmfPeriodoAcademico = rtmfPeriodoAcademico;
	}


	public DependenciaDto getRtmfDependenciaDto() {
		return rtmfDependenciaDto;
	}


	public void setRtmfDependenciaDto(DependenciaDto rtmfDependenciaDto) {
		this.rtmfDependenciaDto = rtmfDependenciaDto;
	}


	public Carrera getRmnfCarrera() {
		return rmnfCarrera;
	}


	public void setRmnfCarrera(Carrera rmnfCarrera) {
		this.rmnfCarrera = rmnfCarrera;
	}


	public CarreraDto getRtmfCarreraDto() {
		return rtmfCarreraDto;
	}


	public void setRtmfCarreraDto(CarreraDto rtmfCarreraDto) {
		this.rtmfCarreraDto = rtmfCarreraDto;
	}


	public NivelDto getRtmfNivelDto() {
		return rtmfNivelDto;
	}


	public void setRtmfNivelDto(NivelDto rtmfNivelDto) {
		this.rtmfNivelDto = rtmfNivelDto;
	}


	public String getRtmfTipoCarrera() {
		return rtmfTipoCarrera;
	}


	public void setRtmfTipoCarrera(String rtmfTipoCarrera) {
		this.rtmfTipoCarrera = rtmfTipoCarrera;
	}



	public Integer getRtmfDpnId() {
		return rtmfDpnId;
	}


	public void setRtmfDpnId(Integer rtmfDpnId) {
		this.rtmfDpnId = rtmfDpnId;
	}

	public ReportesGeneralesDto getRtmTercerasBuscar() {
		return rtmTercerasBuscar;
	}


	public void setRtmTercerasBuscar(ReportesGeneralesDto rtmTercerasBuscar) {
		this.rtmTercerasBuscar = rtmTercerasBuscar;
	}


	public Integer getRtmfEstadoTerceras() {
		return rtmfEstadoTerceras;
	}


	public void setRtmfEstadoTerceras(Integer rtmfEstadoTerceras) {
		this.rtmfEstadoTerceras = rtmfEstadoTerceras;
	}


	public List<PeriodoAcademico> getRtmfListPeriodoAcademico() {
		return rtmfListPeriodoAcademico;
	}


	public void setRtmfListPeriodoAcademico(List<PeriodoAcademico> rtmfListPeriodoAcademico) {
		this.rtmfListPeriodoAcademico = rtmfListPeriodoAcademico;
	}


	public List<Dependencia> getRtmfListDependencia() {
		return rtmfListDependencia;
	}


	public void setRtmfListDependencia(List<Dependencia> rtmfListDependencia) {
		this.rtmfListDependencia = rtmfListDependencia;
	}


	public List<CarreraDto> getRtmfListCarreraDto() {
		return rtmfListCarreraDto;
	}


	public void setRtmfListCarreraDto(List<CarreraDto> rtmfListCarreraDto) {
		this.rtmfListCarreraDto = rtmfListCarreraDto;
	}


	public List<Nivel> getRtmfListNivel() {
		return rtmfListNivel;
	}


	public void setRtmfListNivel(List<Nivel> rtmfListNivel) {
		this.rtmfListNivel = rtmfListNivel;
	}


	public List<Carrera> getRtmfListCarrera() {
		return rtmfListCarrera;
	}


	public void setRtmfListCarrera(List<Carrera> rtmfListCarrera) {
		this.rtmfListCarrera = rtmfListCarrera;
	}


	public Integer getRtmfSeleccionarTodos() {
		return rtmfSeleccionarTodos;
	}


	public void setRtmfSeleccionarTodos(Integer rtmfSeleccionarTodos) {
		this.rtmfSeleccionarTodos = rtmfSeleccionarTodos;
	}


	public String getRtmfTituloModal() {
		return rtmfTituloModal;
	}


	public void setRtmfTituloModal(String rtmfTituloModal) {
		this.rtmfTituloModal = rtmfTituloModal;
	}


	public String getRtmfMensajeModal() {
		return rtmfMensajeModal;
	}


	public void setRtmfMensajeModal(String rtmfMensajeModal) {
		this.rtmfMensajeModal = rtmfMensajeModal;
	}


	public String getRtmfMensajeActivacion() {
		return rtmfMensajeActivacion;
	}


	public void setRtmfMensajeActivacion(String rtmfMensajeActivacion) {
		this.rtmfMensajeActivacion = rtmfMensajeActivacion;
	}


	public Integer getRtmfOpcionSeleccionada() {
		return rtmfOpcionSeleccionada;
	}


	public void setRtmfOpcionSeleccionada(Integer rtmfOpcionSeleccionada) {
		this.rtmfOpcionSeleccionada = rtmfOpcionSeleccionada;
	}


	public boolean isRtmfHabilitarPeriodo() {
		return rtmfHabilitarPeriodo;
	}


	public void setRtmfHabilitarPeriodo(boolean rtmfHabilitarPeriodo) {
		this.rtmfHabilitarPeriodo = rtmfHabilitarPeriodo;
	}


	public boolean isRtmfHabilitarExportar() {
		return rtmfHabilitarExportar;
	}


	public void setRtmfHabilitarExportar(boolean rtmfHabilitarExportar) {
		this.rtmfHabilitarExportar = rtmfHabilitarExportar;
	}


	public Integer getRtmfValidadorClick() {
		return rtmfValidadorClick;
	}


	public void setRtmfValidadorClick(Integer rtmfValidadorClick) {
		this.rtmfValidadorClick = rtmfValidadorClick;
	}


	public Integer getRtmfValidadorEdicion() {
		return rtmfValidadorEdicion;
	}


	public void setRtmfValidadorEdicion(Integer rtmfValidadorEdicion) {
		this.rtmfValidadorEdicion = rtmfValidadorEdicion;
	}


	public boolean isRtmfHabilitadorNivel() {
		return rtmfHabilitadorNivel;
	}


	public void setRtmfHabilitadorNivel(boolean rtmfHabilitadorNivel) {
		this.rtmfHabilitadorNivel = rtmfHabilitadorNivel;
	}


	public boolean isRtmfHabilitadorSeleccion() {
		return rtmfHabilitadorSeleccion;
	}


	public void setRtmfHabilitadorSeleccion(boolean rtmfHabilitadorSeleccion) {
		this.rtmfHabilitadorSeleccion = rtmfHabilitadorSeleccion;
	}


	public boolean isRtmfHabilitadorGuardar() {
		return rtmfHabilitadorGuardar;
	}


	public void setRtmfHabilitadorGuardar(boolean rtmfHabilitadorGuardar) {
		this.rtmfHabilitadorGuardar = rtmfHabilitadorGuardar;
	}


	public Integer getRtmfHabilitarBoton() {
		return rtmfHabilitarBoton;
	}


	public void setRtmfHabilitarBoton(Integer rtmfHabilitarBoton) {
		this.rtmfHabilitarBoton = rtmfHabilitarBoton;
	}


	public ParaleloDto getRtmfParaleloDtoEditar() {
		return rtmfParaleloDtoEditar;
	}


	public void setRtmfParaleloDtoEditar(ParaleloDto rtmfParaleloDtoEditar) {
		this.rtmfParaleloDtoEditar = rtmfParaleloDtoEditar;
	}


	public CarreraDto getRtmfCarreraDtoEditar() {
		return rtmfCarreraDtoEditar;
	}


	public void setRtmfCarreraDtoEditar(CarreraDto rtmfCarreraDtoEditar) {
		this.rtmfCarreraDtoEditar = rtmfCarreraDtoEditar;
	}


	public Carrera getRtmfCarreraEditar() {
		return rtmfCarreraEditar;
	}


	public void setRtmfCarreraEditar(Carrera rtmfCarreraEditar) {
		this.rtmfCarreraEditar = rtmfCarreraEditar;
	}


	public Paralelo getRtmfParalelo() {
		return rtmfParalelo;
	}


	public void setRtmfParalelo(Paralelo rtmfParalelo) {
		this.rtmfParalelo = rtmfParalelo;
	}


	public Integer getRtmfTipoUsuario() {
		return rtmfTipoUsuario;
	}


	public void setRtmfTipoUsuario(Integer rtmfTipoUsuario) {
		this.rtmfTipoUsuario = rtmfTipoUsuario;
	}


	public List<ReportesGeneralesDto> getRtmfListEstadosTercerasBusq() {
		return rtmfListEstadosTercerasBusq;
	}


	public void setRtmfListEstadosTercerasBusq(List<ReportesGeneralesDto> rtmfListEstadosTercerasBusq) {
		this.rtmfListEstadosTercerasBusq = rtmfListEstadosTercerasBusq;
	}

	
	
	/**
	 * Genera el reporte de registrados y lo establece en la sesión como atributo
	 * @param List<RegistradosDto> frmRfRegistradosDto
	 * @return void
	 */
//	public static void generarReporteEstados(List<EstudianteJdbcDto> listaEstados){
//		List<Map<String, Object>> frmCrpCampos = null;
//		Map<String, Object> frmCrpParametros = null;
//		String frmCrpNombreReporte = null;
//		// ****************************************************************//
//		// ***************** GENERACION DEL REPORTE DE REGISTRADOS ********//
//		// ****************************************************************//
//		// ****************************************************************//
//		frmCrpNombreReporte = "ReporteMatriculados";
//		java.util.Date date= new java.util.Date();
//		frmCrpParametros = new HashMap<String, Object>();
//		String fecha = new Timestamp(date.getTime()).toString();
//		frmCrpParametros.put("fecha",fecha);
//
//		frmCrpCampos = new ArrayList<Map<String, Object>>();
//		Map<String, Object> dato = null;
//		for (EstudianteJdbcDto item : listaEstados) {
//			dato = new HashMap<String, Object>();
//			dato.put("convocatoria",item.getPracDescripcion());
//			dato.put("cedula", item.getPrsIdentificacion());
//			dato.put("apellido1", item.getPrsPrimerApellido());
//			dato.put("apellido2", item.getPrsSegundoApellido());
//			dato.put("nombres", item.getPrsNombres());
//			dato.put("facultad", item.getDpnDescripcion());
//			dato.put("carrera", item.getCrrDescripcion());
//			dato.put("email", item.getPrsMailInstitucional());
//			ListasCombosForm lcfAux = new ListasCombosForm();
//			dato.put("estado", lcfAux.getEstadoMatricula(item.getFcinMatriculado()));
//			frmCrpCampos.add(dato);
//		}
//		// Establecemos en el atributo de la sesión la lista de mapas de
//		// datos frmCrpCampos
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//		HttpSession httpSession = request.getSession(false);
//		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
//		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
//		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
//		// ******************FIN DE GENERACION DE REPORTE ************//	
//	} 


	





	
	
	
}