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

 ARCHIVO:     EvaluacionDirectivoForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Directores de Carrera. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Arturo Villafuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionDocente.administracionEvaluacionDocente;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.EvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.TipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionDirectivoForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del Directivo.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "evaluacionDocenteForm")
@SessionScoped
public class ListarEvaluacionDocenteForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario edfUsuario; 

	private Integer edfPeriodoAcademicoBuscar;
	private PeriodoAcademico edfPeriodoAcademico;
	private List<PeriodoAcademico> edfListaPeriodosAcademicos;
	private List<PeriodoAcademico> edfListaTodosPeriodosAcademicos;

	private Evaluacion edfEvaluacionBuscar;
	private List<Evaluacion> edfListaEvaluaciones;

	private String edfDescripcionEvaluacion;
	private Date edfFechaInicioEvaluacion;
	private Date edfFechaFinEvaluacion;
	private Integer edfEstadoEvaluacion;

	private Integer edfTipoEvaluacionBuscar;
	private Evaluacion edfTipoEvaluacion;
	private List<TipoEvaluacion> edfListaTipoEvaluaciones;

	// 0 - Nuevo // 1 - Editar
	private Integer edfEstadoNuevoEditar; 


	//--v3
	//URL reporte
	private String edfLinkReporte;

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
	CarreraServicio servEdfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servEdfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servEdfPersonaServicio;
	@EJB
	PeriodoAcademicoServicio servEdfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servEdfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servEdfUsuarioRolServicio;
	@EJB
	RolFlujoCarreraServicio servEdfRolFlujoCarreraServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servEdfDocenteDatosDtoServicioJdbc;
	@EJB
	DocenteDtoServicioJdbc servEdfDocenteDtoServicioJdbc;
	@EJB
	EvaluacionServicio servEdfEvaluacionServicio;
	@EJB
	TipoEvaluacionServicio servEdfTipoEvaluacionServicio;

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/** 
	 * Inicia los parametros de la funcionalidad
	 */
	private void inicarParametros() { 
		verificarAcceso(); 
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irListarEvaluacionDocente(Usuario usuario) {
		edfUsuario = usuario;
		inicarParametros();
		return "irListarEvaluacionDocente";
	}


	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		edfUsuario = null; 

		edfPeriodoAcademicoBuscar = null;
		edfPeriodoAcademico = null;
		edfListaPeriodosAcademicos = null;

		edfEvaluacionBuscar = null;
		edfListaEvaluaciones = null;

		edfDescripcionEvaluacion = null;
		edfFechaInicioEvaluacion = null;
		edfFechaFinEvaluacion = null;
		edfEstadoEvaluacion = null;

		edfTipoEvaluacionBuscar = null;
		edfTipoEvaluacion = null;
		edfListaTipoEvaluaciones = null;
		
		edfEstadoNuevoEditar = null; 
		edfLinkReporte = null;
		
		edfListaTodosPeriodosAcademicos = null;
		
		return "irInicio";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de crear nuevo
	 * @return Navegacion a la pagina nuevo.
	 */
	public String irNuevo() {
		edfEstadoNuevoEditar = 0; 
		edfListaTodosPeriodosAcademicos = servEdfPeriodoAcademicoServicio.listarTodos();
		return "irNuevoEvaluacionDocente";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de modificacion
	 * @return Navegacion a la pagina editar.
	 */
	public String irEditar(Evaluacion evaluacion) {

		edfListaTodosPeriodosAcademicos = servEdfPeriodoAcademicoServicio.listarTodos();
		edfEstadoNuevoEditar = 1;
		edfPeriodoAcademico = evaluacion.getEvPeriodoAcademico();
		edfPeriodoAcademicoBuscar = evaluacion.getEvPeriodoAcademico().getPracId();
		edfDescripcionEvaluacion = evaluacion.getEvaDescripcion();
		edfEstadoEvaluacion = evaluacion.getEvaEstado();
		edfTipoEvaluacionBuscar = evaluacion.getEvTipoEvaluacion().getTpevId();
		edfFechaInicioEvaluacion = evaluacion.getEvaCronogramaInicio();
		edfFechaFinEvaluacion = evaluacion.getEvaCronogramaFin();

		edfEvaluacionBuscar = evaluacion;

		return "irEditarEvaluacionDocente";
	}


	/**
	 * limpia y regresa a la ventana anterior
	 * @return Navegacion a la pagina anterior.
	 */
	public String irRegresar() {
		edfListaTodosPeriodosAcademicos = null;
		limpiarEdicion();
		inicarParametros();
		return "irListarEvaluacionDocente";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		edfLinkReporte = null;
		inicarParametros();
	}

	/**
	 * Limpia los parametros de edicion/nuevo
	 */
	public void limpiarEdicion(){
		edfPeriodoAcademico = null;
		edfPeriodoAcademicoBuscar = null;
		edfDescripcionEvaluacion = null;
		edfEstadoEvaluacion = null;
		edfFechaInicioEvaluacion = null;
		edfFechaFinEvaluacion = null;
		edfTipoEvaluacionBuscar = null;
		edfTipoEvaluacion = null;
		edfListaTipoEvaluaciones = null;
		edfEvaluacionBuscar = null;
	}


	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		try { 
			if(edfPeriodoAcademicoBuscar != GeneralesConstantes.APP_ID_BASE){
				edfListaEvaluaciones = servEdfEvaluacionServicio.listarXPeriodo(edfPeriodoAcademicoBuscar);
			}else{
				edfListaEvaluaciones = servEdfEvaluacionServicio.listarTodos();
			}
		} catch (EvaluacionNoEncontradoException e) {
			edfListaEvaluaciones = null;
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

	}

	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------


	/**
	 * Guarda los parametros del formulario
	 **/
	public String guardar() {
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		
		if(verificarGuardar()){
			try {
				Evaluacion evaluacion = new Evaluacion();
				evaluacion.setEvTipoEvaluacion(servEdfTipoEvaluacionServicio.buscarPorId(edfTipoEvaluacionBuscar));
				evaluacion.setEvaDescripcion(evaluacion.getEvTipoEvaluacion().getTpevDescripcion()+" - "+edfPeriodoAcademico.getPracDescripcion());
				evaluacion.setEvaEstado(edfEstadoEvaluacion);
				evaluacion.setEvPeriodoAcademico(edfPeriodoAcademico);
				evaluacion.setEvaUsuario(edfUsuario.getUsrNick());
				evaluacion.setEvaFecha(fechaActual);
				evaluacion.setEvaCronogramaInicio(new Timestamp(edfFechaInicioEvaluacion.getTime()));
				evaluacion.setEvaCronogramaFin(new Timestamp(edfFechaFinEvaluacion.getTime()));
				
				evaluacion = servEdfEvaluacionServicio.anadir(evaluacion);

				if(evaluacion == null){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ListarEvaluacionDocente.guardar.no.exito")));
				}else{
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ListarEvaluacionDocente.guardar.exito")));
				}
			} catch (EvaluacionValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EvaluacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (TipoEvaluacionNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (TipoEvaluacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} 
		}  

		limpiarEdicion();
		inicarParametros();
		return "irListarEvaluacionDocente"; 
	}

	/**
	 * Guarda los parametros del formulario
	 **/
	public String guardarCambios() {
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		
		if(verificarGuardar()){
			try {
				Evaluacion evaluacion = servEdfEvaluacionServicio.buscarPorId(edfEvaluacionBuscar.getEvaId());
				
				evaluacion.setEvaEstado(edfEstadoEvaluacion);
				evaluacion.setEvPeriodoAcademico(edfPeriodoAcademico);
				evaluacion.setEvTipoEvaluacion(servEdfTipoEvaluacionServicio.buscarPorId(edfTipoEvaluacionBuscar));
				evaluacion.setEvaDescripcion(evaluacion.getEvTipoEvaluacion().getTpevDescripcion()+" - "+edfPeriodoAcademico.getPracDescripcion());
				evaluacion.setEvaUsuario(edfUsuario.getUsrNick());
				evaluacion.setEvaFecha(fechaActual);
				evaluacion.setEvaCronogramaInicio(new Timestamp(edfFechaInicioEvaluacion.getTime()));
				evaluacion.setEvaCronogramaFin(new Timestamp(edfFechaFinEvaluacion.getTime()));

				if(!servEdfEvaluacionServicio.editar(evaluacion)){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ListarEvaluacionDocente.guardar.cambios.no.exito")));
				}else{
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ListarEvaluacionDocente.guardar.cambios.exito")));

				}
			} catch (EvaluacionValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EvaluacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EvaluacionNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (TipoEvaluacionNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (TipoEvaluacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		}  
		limpiarEdicion();
		inicarParametros();
		return "irListarEvaluacionDocente"; 
	}

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------

	/**
	 * Busca si existe un periodo activo
	 * @return retorna la entidad PeriodoAcademico activo en pregrado
	 **/
	public List<PeriodoAcademico> listarPeriodos(){
		return servEdfPeriodoAcademicoServicio.listarTodos();
	}

	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------

	/**
	 * Verifica el acceso del docente a la autoevaluacion
	 * otorgando un estado true / false para activar la evaluacion
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void verificarAcceso(){

		try { 
			edfListaEvaluaciones = servEdfEvaluacionServicio.listarTodos();
			edfListaPeriodosAcademicos = new ArrayList<>();
			for(Evaluacion item: edfListaEvaluaciones){
				edfListaPeriodosAcademicos.add(item.getEvPeriodoAcademico());
			}  
			
			HashSet hs = new HashSet();   
		    hs.addAll(edfListaPeriodosAcademicos);	     
		    edfListaPeriodosAcademicos.clear();
		    edfListaPeriodosAcademicos.addAll(hs);
		 
			edfListaTipoEvaluaciones = servEdfTipoEvaluacionServicio.listarActivos(); 
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoEvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoEvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

	}

	/**
	 * Verifica campos, parametos antes de guardar las distintas cargas horarias
	 * @return Estado false por culquier error al ingresar, true - pasa las validaciones 
	 **/
	public Boolean verificarGuardar() {

		Boolean verificar = false;
		try {
			edfPeriodoAcademico = servEdfPeriodoAcademicoServicio.buscarPorId(edfPeriodoAcademicoBuscar);
			if(edfFechaFinEvaluacion.after(edfFechaInicioEvaluacion)){
				if(verificarEvaluacionActiva()){
					verificar = true;
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ListarEvaluacionDocente.verificar.guardar.mal.fecha")));
			}

		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return verificar;
	}

	public Boolean verificarEvaluacionActiva(){
		Boolean verificar = false;
			if(edfEstadoEvaluacion == EvaluacionConstantes.ESTADO_ACTIVO_VALUE){
				Evaluacion evaluacionActiva = null;
				
				try{
					evaluacionActiva = servEdfEvaluacionServicio.buscarActivoXTipo(edfTipoEvaluacionBuscar);
				}catch (EvaluacionException e) {
					evaluacionActiva = null;
					FacesUtil.mensajeError(e.getMessage());
				} catch (EvaluacionValidacionException e) {
					evaluacionActiva = null;
					FacesUtil.mensajeError(e.getMessage());
				} catch (EvaluacionNoEncontradoException e) {
					evaluacionActiva = null;
					FacesUtil.mensajeError(e.getMessage());
				}
				
				if(evaluacionActiva == null){
						verificar = true;
				}else{
					
					if(edfEvaluacionBuscar != null && edfEvaluacionBuscar.getEvaId() == evaluacionActiva.getEvaId()){
						verificar = true;
					}else{
						verificar = false;
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ListarEvaluacionDocente.verificar.evaluacion.activa.existente")));
					}
				}
			}else{
				verificar = true;
			}
		return verificar;
	}

	/**
	 * Transforma el id de estado a descripcion
	 **/
	public String mostrarEstado(Integer estado){
		if(estado == 0){
			return EvaluacionConstantes.ESTADO_ACTIVO_LABEL;
		}else{
			return EvaluacionConstantes.ESTADO_INACTIVO_LABEL;
		} 
	}


	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getEdfUsuario() {
		return edfUsuario;
	}

	public void setEdfUsuario(Usuario edfUsuario) {
		this.edfUsuario = edfUsuario;
	}

	public Integer getEdfPeriodoAcademicoBuscar() {
		return edfPeriodoAcademicoBuscar;
	}

	public void setEdfPeriodoAcademicoBuscar(Integer edfPeriodoAcademicoBuscar) {
		this.edfPeriodoAcademicoBuscar = edfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getEdfListaPeriodosAcademicos() {
		return edfListaPeriodosAcademicos;
	}

	public void setEdfListaPeriodosAcademicos(List<PeriodoAcademico> edfListaPeriodosAcademicos) {
		this.edfListaPeriodosAcademicos = edfListaPeriodosAcademicos;
	} 

	public String getEdfLinkReporte() {
		return edfLinkReporte;
	}

	public void setEdfLinkReporte(String edfLinkReporte) {
		this.edfLinkReporte = edfLinkReporte;
	}

	public PeriodoAcademico getEdfPeriodoAcademico() {
		return edfPeriodoAcademico;
	}

	public void setEdfPeriodoAcademico(PeriodoAcademico edfPeriodoAcademico) {
		this.edfPeriodoAcademico = edfPeriodoAcademico;
	}

	public List<Evaluacion> getEdfListaEvaluaciones() {
		return edfListaEvaluaciones;
	}

	public void setEdfListaEvaluaciones(List<Evaluacion> edfListaEvaluaciones) {
		this.edfListaEvaluaciones = edfListaEvaluaciones;
	}

	public String getEdfDescripcionEvaluacion() {
		return edfDescripcionEvaluacion;
	}

	public void setEdfDescripcionEvaluacion(String edfDescripcionEvaluacion) {
		this.edfDescripcionEvaluacion = edfDescripcionEvaluacion;
	}

	public Date getEdfFechaInicioEvaluacion() {
		return edfFechaInicioEvaluacion;
	}

	public void setEdfFechaInicioEvaluacion(Date edfFechaInicioEvaluacion) {
		this.edfFechaInicioEvaluacion = edfFechaInicioEvaluacion;
	}

	public Date getEdfFechaFinEvaluacion() {
		return edfFechaFinEvaluacion;
	}

	public void setEdfFechaFinEvaluacion(Date edfFechaFinEvaluacion) {
		this.edfFechaFinEvaluacion = edfFechaFinEvaluacion;
	}

	public Integer getEdfEstadoEvaluacion() {
		return edfEstadoEvaluacion;
	}

	public void setEdfEstadoEvaluacion(Integer edfEstadoEvaluacion) {
		this.edfEstadoEvaluacion = edfEstadoEvaluacion;
	}

	public Evaluacion getEdfEvaluacionBuscar() {
		return edfEvaluacionBuscar;
	}

	public void setEdfEvaluacionBuscar(Evaluacion edfEvaluacionBuscar) {
		this.edfEvaluacionBuscar = edfEvaluacionBuscar;
	}

	public Integer getEdfEstadoNuevoEditar() {
		return edfEstadoNuevoEditar;
	}

	public void setEdfEstadoNuevoEditar(Integer edfEstadoNuevoEditar) {
		this.edfEstadoNuevoEditar = edfEstadoNuevoEditar;
	}

	public Integer getEdfTipoEvaluacionBuscar() {
		return edfTipoEvaluacionBuscar;
	}

	public void setEdfTipoEvaluacionBuscar(Integer edfTipoEvaluacionBuscar) {
		this.edfTipoEvaluacionBuscar = edfTipoEvaluacionBuscar;
	}

	public Evaluacion getEdfTipoEvaluacion() {
		return edfTipoEvaluacion;
	}

	public void setEdfTipoEvaluacion(Evaluacion edfTipoEvaluacion) {
		this.edfTipoEvaluacion = edfTipoEvaluacion;
	}

	public List<TipoEvaluacion> getEdfListaTipoEvaluaciones() {
		return edfListaTipoEvaluaciones;
	}

	public void setEdfListaTipoEvaluaciones(List<TipoEvaluacion> edfListaTipoEvaluaciones) {
		this.edfListaTipoEvaluaciones = edfListaTipoEvaluaciones;
	}

	public List<PeriodoAcademico> getEdfListaTodosPeriodosAcademicos() {
		return edfListaTodosPeriodosAcademicos;
	}

	public void setEdfListaTodosPeriodosAcademicos(List<PeriodoAcademico> edfListaTodosPeriodosAcademicos) {
		this.edfListaTodosPeriodosAcademicos = edfListaTodosPeriodosAcademicos;
	}

}