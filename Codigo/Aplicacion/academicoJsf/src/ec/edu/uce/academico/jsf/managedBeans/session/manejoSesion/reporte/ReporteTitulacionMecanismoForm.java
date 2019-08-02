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

 ARCHIVO:     ReporteDocentesMateriaCarreraForm.java	  
 DESCRIPCION: Bean de sesion que maneja materias del docente
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 06-NOV-2018 			Fatima Tobar                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.jboss.resteasy.spi.HttpResponse;

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.TitulacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioDto;
import ec.edu.uce.academico.ejb.excepciones.TitulacionException;
import ec.edu.uce.academico.ejb.excepciones.TitulacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ReporteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TitulacionServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase (managed bean) ReporteDocentesMateriaCarreraForm. Managed Bean de
 * sesion que maneja materias del docente
 * 
 * @author fktobar.
 * @version 1.0
 */

@ManagedBean(name = "reporteTitulacionMecanismoForm")
@SessionScoped
public class ReporteTitulacionMecanismoForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;

	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	// GENERAL
	private Usuario rpdfUsuario;
	private UsuarioDto rpdfUsuarioDto;

	// VARIABLES DE BUSQUEDA
	private TitulacionDto rpdfConvocatoria;
	private TitulacionDto rpdfDependencia;
	private TitulacionDto rpdfCarrera;
	// LISTA DE OBJETOS
	private List<TitulacionDto> rpdfListConvocatoria;
	private List<TitulacionDto> rpdfListDependencia;
	private List<TitulacionDto> rpdfListCarrera;

	

	private String rpdfTipoCarrera; // diferenciar el tipo de carrera que
									// ingreso - por carreras
	private Integer rpdfTipoUsuario; // diferenciar el tipo de usuario que
										// ingreso - por carreras
	private Integer rpdfDpnId; // diferenciar el tipo de usuario que ingreso -
								// por carreras
	private List<TitulacionDto> rpdfListNumeroExamenComplexivoBusq;
	private List<TitulacionDto>rpdfListProyectoTitulacionBusq;

	private DocenteJdbcDto rpdfDocentesMateriaBuscar;

	
	private List<Nivel> rpdfListNivel;
	

	// AUXILIARES
	private Integer rpdfSeleccionarTodos;
	private String rpdfTituloModal;
	private String rpdfMensajeModal;
	private String rpdfMensajeActivacion;
	private Integer rpdfOpcionSeleccionada;
	private boolean rpdfHabilitarPeriodo;
	private boolean rpdfHabilitarExportar;

	private Integer rpdfValidadorClick;
	private Integer rpdfValidadorEdicion;
	private boolean rpdfHabilitadorNivel;
	private boolean rpdfHabilitadorSeleccion;
	private boolean rpdfHabilitadorGuardar;

	// reporte
	protected HttpResponse httpRes;

	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {

	}

	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB
	TitulacionServicioJdbc servRpfTitulacion;
	@EJB
	CarreraDtoServicioJdbc servRpfCarreraDto;
	@EJB
	CarreraServicio servRpfCarrera;
	@EJB
	NivelServicio servNivelDtoServicio;
	@EJB
	private UsuarioRolServicio servRpfUsuarioRolServicio;
	
	@EJB
	EstudianteDtoServicioJdbc servEstudianteDtoServicioJdbc;
	@EJB
	private ReporteDtoServicioJdbc servReporteDtoServicioJdbc;
	@EJB
	private DocenteDtoServicioJdbc servDocenteDtoServicioJdbc;

	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * 
	 * @param usuario
	 *            - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de Administración .
	 */
	public String irReporteTitulacion(Usuario usuario) {
		rpdfUsuario = usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if (item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()) {
					rpdfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					rpdfTipoCarrera = "carrrtmfTipoUsuarioeras";
					rpdfHabilitarPeriodo = true;
				} else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
					rpdfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					rpdfTipoCarrera = "soporte";
					rpdfHabilitarPeriodo = true;
				}
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irReporteTitulacion";
	}
	
	
	/**
	 * Método que permite iniciar los reportes de matriculados en pregrado
	 * 
	 * @param usuario
	 *            - el usuario que ingresa
	 * @return Navegacion a la pagina xhtml de Administración .
	 */
	public String irReporteProyectoTitulacion(Usuario usuario) {
		
		rpdfUsuario = usuario;
		try {
			List<UsuarioRol> usro = servRpfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if (item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()) {
					rpdfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					rpdfTipoCarrera = "carrrtmfTipoUsuarioeras";
					rpdfHabilitarPeriodo = true;
				} else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
					rpdfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					rpdfTipoCarrera = "soporte";
					rpdfHabilitarPeriodo = true;
				}
			}
			iniciarParametros();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return "irReporteProyectoTitulacion";
	}

	/**
	 * Método para regresar al menú pricipal, limpia los objetos instaciados al
	 * iniciar la funcionalidad
	 * 
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
	 * 
	 * @return
	 */
	public void limpiar() {
		FacesUtil.limpiarMensaje();
		iniciarParametros();
	}

	public void limpiarReporte() {
		iniciarParametrosReporteDocentesMaterias();
	}

	public void iniciarParametrosReporteDocentesMaterias() {
		try {
			rpdfHabilitarExportar = true;
			rpdfUsuarioDto = new UsuarioDto();
			// inicio listas
			rpdfSeleccionarTodos = GeneralesConstantes.APP_ID_BASE;
			rpdfListConvocatoria = new ArrayList<TitulacionDto>(); // inicio
																			// la
																			// lista
																			// de
																			// PeriodoAcademico
			rpdfListDependencia = new ArrayList<TitulacionDto>(); // inicio la
																// lista de
																// PeriodoAcademico

			rpdfListCarrera = new ArrayList<TitulacionDto>(); // inicio la lista
																// de Carreras
																// Dto
			rpdfListCarrera = new ArrayList<TitulacionDto>(); // inicio la lista de
														// Carreras
			rpdfListNivel = new ArrayList<Nivel>(); // inicio la lista de Nivel

			// inicio objetos
			rpdfConvocatoria = new TitulacionDto(); // Inicio los objetos
															// PeriodoAcademico,
															// CarreraDto y
															// ParaleloDto
			
			rpdfCarrera = new TitulacionDto(); // Instancio el objeto carrera
			// rpdfConvocatoria.setPracId(GeneralesConstantes.APP_ID_BASE);//Seteo
			// en -99 los valores iniciales IdPeriodoAcademico

			rpdfConvocatoria = new TitulacionDto();
			rpdfListNumeroExamenComplexivoBusq = null;
			rpdfListProyectoTitulacionBusq=null;
			
			rpdfDocentesMateriaBuscar = new DocenteJdbcDto();
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	public void iniciarParametros() {
		try {
			rpdfHabilitarExportar = true;
			rpdfUsuarioDto = new UsuarioDto();
			// inicio listas
			rpdfSeleccionarTodos = GeneralesConstantes.APP_ID_BASE;
			rpdfListConvocatoria = new ArrayList<TitulacionDto>(); 
			rpdfListCarrera = new ArrayList<TitulacionDto>(); 
			rpdfListDependencia = new ArrayList<TitulacionDto>();
			// inicio objetos
			rpdfConvocatoria = new TitulacionDto();
			rpdfDependencia = new TitulacionDto();
			rpdfCarrera= new TitulacionDto();
			rpdfCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE); 
			rpdfDependencia.setFclId(GeneralesConstantes.APP_ID_BASE);
			rpdfListNumeroExamenComplexivoBusq = null;
			rpdfListProyectoTitulacionBusq=null;
			
			llenarPeriodos();
			llenarDependencia();
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método que instancia la lista de resultados cuando cambio de período
	 */
	/*public void cambiarPeriodo() {
		try {
			rpdfListDocentesMateriasBusq = new ArrayList<>();
			rpdfListDependencia = new ArrayList<>();
			if (rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()) {
				rpdfListDependencia = servRpfDependenciaServicio.listarFacultadesxUsuario(rpdfUsuario.getUsrId());
			} else if (rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
				rpdfListDependencia = servRpfDependenciaServicio
						.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			}
			rpdfHabilitarExportar = true;
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}*/

	public void llenarPeriodos() throws TitulacionNoEncontradoException, TitulacionException {	
		rpdfListConvocatoria = null;
		if (rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue() || rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()) {
			rpdfListConvocatoria = servRpfTitulacion.listarConvocatoria();
			
		} else {
			FacesUtil.mensajeError("No tiene permisos para acceder a la búsqueda");
		}
	}

	/**
	 * Método para llenar la lista Dependencias
	 * @throws TitulacionException 
	 * @throws TitulacionNoEncontradoException 
	 */
	public void llenarDependencia() throws TitulacionNoEncontradoException, TitulacionException {
		
		try {
			rpdfListDependencia = null;
			if (rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {
			
				rpdfListDependencia = servRpfTitulacion.listarTodasFacultades();
			} 		
			
			rpdfHabilitarExportar=true;
		} catch (TitulacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	/**
	 * Método para llenar la lista de Carreras por Dependencia
	 * @throws TitulacionException 
	 * @throws TitulacionNoEncontradoException 
	 */
	public void llenarCarreras() throws TitulacionNoEncontradoException, TitulacionException {
		
		try {
			rpdfListCarrera = null;
			rpdfCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
			if (rpdfDependencia.getFclId() != GeneralesConstantes.APP_ID_BASE) {
				if (rpdfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rpdfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()) {

					rpdfListCarrera = servRpfTitulacion.listarTodasCarreraXFacultad(rpdfDependencia.getFclId());
				}
			} else {
				rpdfListNumeroExamenComplexivoBusq = null;
			}
			rpdfHabilitarExportar = true;
			rpdfListNumeroExamenComplexivoBusq = null;
	
		} catch (TitulacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existen carreras para la dependencia seleccionada");
		}
		
	}
		

	public void cambiarCarrera() {
		rpdfListNumeroExamenComplexivoBusq = new ArrayList<>();
		rpdfHabilitarExportar = true;
	}

	/**
	 * verifica que haga click en el boton buscar
	 */

	public void buscarExamenComplexivo() {
		try {
			if (rpdfConvocatoria.getCnvId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la convocatoria.");
			} else if (rpdfDependencia.getFclId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la Facultad.");
			} else {
				rpdfListNumeroExamenComplexivoBusq = servRpfTitulacion.ListarTitulacionExamenComplexivo(
						rpdfConvocatoria.getCnvId(), rpdfDependencia.getFclId(), rpdfCarrera.getCrrId());
				if (rpdfListNumeroExamenComplexivoBusq.size() > 0) {
					cargarReporteEC();
					rpdfHabilitarExportar = false;
				} else {
					FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
				}
			}
		} catch (Exception e) {
			rpdfHabilitarExportar = true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
		}
	}

	/**
	 * verifica que haga click en el boton buscar
	 */

	public void buscarProyectoTitulacion() {
		try {
			if (rpdfConvocatoria.getCnvId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la convocatoria.");
			} else if (rpdfDependencia.getFclId() == GeneralesConstantes.APP_ID_BASE) {
				FacesUtil.mensajeError("Debe seleccionar la Facultad.");
			} else {
				rpdfListProyectoTitulacionBusq = servRpfTitulacion.ListarTitulacionProyectoTitulacion(
						rpdfConvocatoria.getCnvId(), rpdfDependencia.getFclId(), rpdfCarrera.getCrrId());
				if (rpdfListProyectoTitulacionBusq.size() > 0) {
					cargarReportePT();
					rpdfHabilitarExportar = false;
				} else {
					FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
				}
			}
		} catch (Exception e) {
			rpdfHabilitarExportar = true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron resultados con los parámetros ingresados.");
		}
	}
	
	public void cargarReporteEC() {
		try {
			List<Map<String, Object>> frmCrpCampos = null;
			Map<String, Object> frmCrpParametros = null;
			String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DEL REPORTEO ****************//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "TitulacionExamenComplexivo";
			java.util.Date date = new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha", fecha);
			frmCrpParametros.put("periodo", rpdfListConvocatoria.get(0).getCnvDescripcion());
			frmCrpParametros.put("facultad", rpdfListDependencia.get(0).getFclDescripcion());
			if(rpdfCarrera.getCrrId()==-99){
				frmCrpParametros.put("carrera", "TODAS");
			}else{
				frmCrpParametros.put("carrera", rpdfListCarrera.get(0).getCrrDescripcion());
			}
			

			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			

			for (TitulacionDto item : rpdfListNumeroExamenComplexivoBusq) {

				dato = new HashMap<String, Object>();
				dato.put("inscritos", item.getNumeroInscritos());
				dato.put("aprobadosGracia", item.getNumeroAprobadosSinGracia());
				dato.put("aprobadosSinGracia", item.getNumeroAprobadosGracia());
				dato.put("reprobados", item.getNumeroReprobados());
				
				frmCrpCampos.add(dato);

				
			}
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
			httpSession.setAttribute("frmCargaParametros", frmCrpParametros);
			httpSession.setAttribute("frmCargaNombreReporte", frmCrpNombreReporte);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
		}

	}

	
	public void cargarReportePT() {
		try {
			List<Map<String, Object>> frmCrpCampos = null;
			Map<String, Object> frmCrpParametros = null;
			String frmCrpNombreReporte = null;
			// ****************************************************************//
			// ***************** GENERACION DEL REPORTEO ****************//
			// ****************************************************************//
			// ****************************************************************//
			frmCrpNombreReporte = "TitulacionProyecto";
			java.util.Date date = new java.util.Date();
			frmCrpParametros = new HashMap<String, Object>();
			String fecha = new Timestamp(date.getTime()).toString();
			frmCrpParametros.put("fecha", fecha);
			frmCrpParametros.put("periodo", rpdfListConvocatoria.get(0).getCnvDescripcion());
			frmCrpParametros.put("facultad", rpdfListDependencia.get(0).getFclDescripcion());
			if(rpdfCarrera.getCrrId()==-99){
				frmCrpParametros.put("carrera", "TODAS");
			}else{
				frmCrpParametros.put("carrera", rpdfListCarrera.get(0).getCrrDescripcion());
			}
			

			frmCrpCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> dato = null;
			

			for (TitulacionDto item : rpdfListProyectoTitulacionBusq) {

				dato = new HashMap<String, Object>();
				dato.put("periodo", item.getCnvDescripcion());
				dato.put("nombres", item.getNombres());
				dato.put("proyectoNombre", item.getAsttTemaTrabajo());
				dato.put("tutor", item.getAsttTutor());
				dato.put("notaFinal", item.getAsnoNotaFinal());
				dato.put("numeroActa", item.getFcesNumeroActa());
				dato.put("fechaRegistro", item.getFcesFechaActaGrado());
				frmCrpCampos.add(dato);

				
			}
			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
			httpSession.setAttribute("frmCargaParametros", frmCrpParametros);
			httpSession.setAttribute("frmCargaNombreReporte", frmCrpNombreReporte);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
		}

	}

	
	public Usuario getRpdfUsuario() {
		return rpdfUsuario;
	}

	public void setRpdfUsuario(Usuario rpdfUsuario) {
		this.rpdfUsuario = rpdfUsuario;
	}

	public UsuarioDto getRpdfUsuarioDto() {
		return rpdfUsuarioDto;
	}

	public void setRpdfUsuarioDto(UsuarioDto rpdfUsuarioDto) {
		this.rpdfUsuarioDto = rpdfUsuarioDto;
	}

	

	public TitulacionDto getRpdfConvocatoria() {
		return rpdfConvocatoria;
	}

	public void setRpdfConvocatoria(TitulacionDto rpdfConvocatoria) {
		this.rpdfConvocatoria = rpdfConvocatoria;
	}


	public String getRpdfTipoCarrera() {
		return rpdfTipoCarrera;
	}

	public void setRpdfTipoCarrera(String rpdfTipoCarrera) {
		this.rpdfTipoCarrera = rpdfTipoCarrera;
	}

	public Integer getRpdfTipoUsuario() {
		return rpdfTipoUsuario;
	}

	public void setRpdfTipoUsuario(Integer rpdfTipoUsuario) {
		this.rpdfTipoUsuario = rpdfTipoUsuario;
	}

	public Integer getRpdfDpnId() {
		return rpdfDpnId;
	}

	public void setRpdfDpnId(Integer rpdfDpnId) {
		this.rpdfDpnId = rpdfDpnId;
	}

	
	public List<TitulacionDto> getRpdfListNumeroExamenComplexivoBusq() {
		return rpdfListNumeroExamenComplexivoBusq;
	}

	public void setRpdfListNumeroExamenComplexivoBusq(List<TitulacionDto> rpdfListNumeroExamenComplexivoBusq) {
		this.rpdfListNumeroExamenComplexivoBusq = rpdfListNumeroExamenComplexivoBusq;
	}

	public DocenteJdbcDto getRpdfDocentesMateriaBuscar() {
		return rpdfDocentesMateriaBuscar;
	}

	public void setRpdfDocentesMateriaBuscar(DocenteJdbcDto rpdfDocentesMateriaBuscar) {
		this.rpdfDocentesMateriaBuscar = rpdfDocentesMateriaBuscar;
	}


	public List<TitulacionDto> getRpdfListConvocatoria() {
		return rpdfListConvocatoria;
	}

	public void setRpdfListConvocatoria(List<TitulacionDto> rpdfListConvocatoria) {
		this.rpdfListConvocatoria = rpdfListConvocatoria;
	}


	public List<TitulacionDto> getRpdfListDependencia() {
		return rpdfListDependencia;
	}

	public void setRpdfListDependencia(List<TitulacionDto> rpdfListDependencia) {
		this.rpdfListDependencia = rpdfListDependencia;
	}

	public void setRpdfDependencia(TitulacionDto rpdfDependencia) {
		this.rpdfDependencia = rpdfDependencia;
	}

	public TitulacionDto getRpdfDependencia() {
		return rpdfDependencia;
	}

	public List<Nivel> getRpdfListNivel() {
		rpdfListNivel = rpdfListNivel == null ? (new ArrayList<Nivel>()) : rpdfListNivel;
		return rpdfListNivel;
	}

	public void setRpdfListNivel(List<Nivel> rpdfListNivel) {
		this.rpdfListNivel = rpdfListNivel;
	}

	public Integer getRpdfSeleccionarTodos() {
		return rpdfSeleccionarTodos;
	}

	public void setRpdfSeleccionarTodos(Integer rpdfSeleccionarTodos) {
		this.rpdfSeleccionarTodos = rpdfSeleccionarTodos;
	}

	public String getRpdfTituloModal() {
		return rpdfTituloModal;
	}

	public void setRpdfTituloModal(String rpdfTituloModal) {
		this.rpdfTituloModal = rpdfTituloModal;
	}

	public String getRpdfMensajeModal() {
		return rpdfMensajeModal;
	}

	public void setRpdfMensajeModal(String rpdfMensajeModal) {
		this.rpdfMensajeModal = rpdfMensajeModal;
	}

	public String getRpdfMensajeActivacion() {
		return rpdfMensajeActivacion;
	}

	public void setRpdfMensajeActivacion(String rpdfMensajeActivacion) {
		this.rpdfMensajeActivacion = rpdfMensajeActivacion;
	}

	public Integer getRpdfOpcionSeleccionada() {
		return rpdfOpcionSeleccionada;
	}

	public void setRpdfOpcionSeleccionada(Integer rpdfOpcionSeleccionada) {
		this.rpdfOpcionSeleccionada = rpdfOpcionSeleccionada;
	}

	public boolean isRpdfHabilitarPeriodo() {
		return rpdfHabilitarPeriodo;
	}

	public void setRpdfHabilitarPeriodo(boolean rpdfHabilitarPeriodo) {
		this.rpdfHabilitarPeriodo = rpdfHabilitarPeriodo;
	}

	public boolean isRpdfHabilitarExportar() {
		return rpdfHabilitarExportar;
	}

	public void setRpdfHabilitarExportar(boolean rpdfHabilitarExportar) {
		this.rpdfHabilitarExportar = rpdfHabilitarExportar;
	}

	public Integer getRpdfValidadorClick() {
		return rpdfValidadorClick;
	}

	public void setRpdfValidadorClick(Integer rpdfValidadorClick) {
		this.rpdfValidadorClick = rpdfValidadorClick;
	}

	public Integer getRpdfValidadorEdicion() {
		return rpdfValidadorEdicion;
	}

	public void setRpdfValidadorEdicion(Integer rpdfValidadorEdicion) {
		this.rpdfValidadorEdicion = rpdfValidadorEdicion;
	}

	public boolean isRpdfHabilitadorNivel() {
		return rpdfHabilitadorNivel;
	}

	public void setRpdfHabilitadorNivel(boolean rpdfHabilitadorNivel) {
		this.rpdfHabilitadorNivel = rpdfHabilitadorNivel;
	}

	public boolean isRpdfHabilitadorSeleccion() {
		return rpdfHabilitadorSeleccion;
	}

	public void setRpdfHabilitadorSeleccion(boolean rpdfHabilitadorSeleccion) {
		this.rpdfHabilitadorSeleccion = rpdfHabilitadorSeleccion;
	}

	public boolean isRpdfHabilitadorGuardar() {
		return rpdfHabilitadorGuardar;
	}

	public void setRpdfHabilitadorGuardar(boolean rpdfHabilitadorGuardar) {
		this.rpdfHabilitadorGuardar = rpdfHabilitadorGuardar;
	}

	public TitulacionDto getRpdfCarrera() {
		return rpdfCarrera;
	}

	public void setRpdfCarrera(TitulacionDto rpdfCarrera) {
		this.rpdfCarrera = rpdfCarrera;
	}

	public List<TitulacionDto> getRpdfListCarrera() {
		return rpdfListCarrera;
	}

	public void setRpdfListCarrera(List<TitulacionDto> rpdfListCarrera) {
		this.rpdfListCarrera = rpdfListCarrera;
	}


	public List<TitulacionDto> getRpdfListProyectoTitulacionBusq() {
		return rpdfListProyectoTitulacionBusq;
	}


	public void setRpdfListProyectoTitulacionBusq(List<TitulacionDto> rpdfListProyectoTitulacionBusq) {
		this.rpdfListProyectoTitulacionBusq = rpdfListProyectoTitulacionBusq;
	}

	

}
