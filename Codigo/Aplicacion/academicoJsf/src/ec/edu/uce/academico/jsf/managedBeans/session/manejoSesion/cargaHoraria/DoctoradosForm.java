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
   
 ARCHIVO:     DoctoradosForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Doctorados. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Arturo Villafuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoFuncionCargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.TipoFuncionCargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetalleCargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFuncionCargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RelacionLaboralConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoCargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoFuncionCargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleCargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFuncionCargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCargaHorariaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) DoctoradosForm.java Bean de sesión que maneja
 * los atributos del formulario de Doctorados.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "doctoradosForm")
@SessionScoped
public class DoctoradosForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario dfUsuario;

	private List<Dependencia> dfListaFacultades;
	private List<Carrera> dfListaCarreras;
	private List<PersonaDatosDto> dfListaDocentes;
	private List<TipoFuncionCargaHoraria> dfListaFunciones;
	private List<Integer> dfListaHorasSemanales;
	private List<Integer> dfListaPreHorasSemanales;
	private PersonaDatosDto dfDocente;

	private Integer dfFacultadBuscar;
	private Integer dfCarreraBuscar;
	private Integer dfDocenteBuscar;
	private String dfIdentificacionBuscar;

	private TipoFuncionCargaHoraria dfTipoFuncionCargaHorariaSeleccion;
	private Integer dfFuncionSeleccion; 
	
	private String dfUniversidad;
	private String dfTitDoct;
	private Date dfFechaInicio;
	private Date dfFechaFin;
	private Integer dfHorasSemanales;
	
	private PeriodoAcademico dfPeriodoAcademico;
	private Integer dfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> dfListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto dfPlanificacionCronograma;
	
	private List<CargaHoraria> dfListaCargasHorarias;
	private List<DetalleCargaHoraria> dfListaDetalleCargaHoraria;

	private CargaHoraria dfCargaHorariaActual;
	private DetalleCargaHoraria dfDetalleCargaHorariaActual;
	
	private Boolean dfCargaHorariaActiva;
	  
	private String dfCedulaDocenteBusquedaAvanzada;
	private String dfApellidoDocenteBusquedaAvanzada;
	private String dfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> dfListDocentesBusquedaAvanzada;
	
	private String dfPreUniversidad;
	private String dfPreTitDoct;
	private Date dfPreFechaInicio;
	private Date dfPreFechaFin;
	private Integer dfPreHorasSemanales;
	
	private List<Integer> dfFuncionInvestigacionSeleccion;
	private List<Integer> dfFuncionPreinvestigacionSeleccion;
	
	private CargaHoraria dfPreCargaHorariaActual;
	private DetalleCargaHoraria dfPreDetalleCargaHorariaActual;
	
	private Boolean dfActivarTipoDoctorado;
	private Boolean dfActivarTipoPreDoctorado;
	
	//--v3
	//URL reporte
	private String dfLinkReporte;
	  
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
	DependenciaServicio servDfDependenciaServicio;
	@EJB
	CarreraServicio servDfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servDfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servDfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servDfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servDfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servDfPlanificacionCronogramaDtoServicioJdbc; 
	@EJB
	CargaHorariaServicio servDfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servDfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servDfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servDfUsuarioRolServicio;
	
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/**
	 * Inicia los parametros de la funcionalidad
	 */
	@SuppressWarnings("rawtypes")
	private void inicarParametros() {
		Date date = new Date();
		dfFechaInicio = date;
		dfFechaFin = date;
		dfPreFechaInicio = date;
		dfPreFechaFin = date;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

		
		try {	
			dfCargaHorariaActiva = false; 
//			if(validarCronograma()){ // // Cambio Solicitado por Eco. Reinoso 12/12/2017
				validarCronograma();
				cagarPeriodos();
				dfListaFacultades = servDfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = dfListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			}
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}


	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad 
	 * @return navegacion hacia el formulario de aseguramiento de calidad.
	 */
	public String irFormularioDoctorados(Usuario usuario) {
		dfUsuario = usuario;
		inicarParametros();
		return "irFormularioDoctorados";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		dfListaFacultades = null;
		dfListaCarreras = null;
		dfListaDocentes = null;
		dfListaFunciones = null;
		dfDocente = null;
		dfFacultadBuscar = null;
		dfCarreraBuscar = null;
		dfDocenteBuscar = null;
		dfIdentificacionBuscar = null;
		dfTipoFuncionCargaHorariaSeleccion = null;
		dfFuncionSeleccion = null; 
		dfUniversidad = null;
		dfTitDoct = null;
		dfFechaInicio = null;
		dfFechaFin = null;
		dfHorasSemanales = null;
		dfCargaHorariaActual = null;
		dfDetalleCargaHorariaActual = null;
		dfCargaHorariaActiva = false;
		dfPeriodoAcademicoBuscar = null;
		dfListaPeriodosAcademicos = null;
		dfPreUniversidad = null;
		dfPreTitDoct = null;
		dfPreFechaInicio = null;
		dfPreFechaFin = null;
		dfPreHorasSemanales = null;
		dfFuncionInvestigacionSeleccion = null;
		dfFuncionPreinvestigacionSeleccion = null;
		dfActivarTipoDoctorado = false;
		dfActivarTipoPreDoctorado = false;
		dfPreCargaHorariaActual = null;
		dfPreDetalleCargaHorariaActual = null;
		dfLinkReporte = null;
		dfListaHorasSemanales = null;
		dfListaPreHorasSemanales = null;
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

		/**
		 * Setea y nulifica a los valores iniciales de cada parametro
		 */
		public void limpiar() {
			dfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			dfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE; 
			dfIdentificacionBuscar = null;
			dfTipoFuncionCargaHorariaSeleccion = null;
			dfFuncionSeleccion = null;
			dfHorasSemanales = null;
			dfListaFunciones = null;
			dfListaCarreras = null;
			dfListaFacultades = null;
			dfListaDocentes = null; 
			dfDocente = null;
			dfUniversidad = null;
			dfTitDoct = null;
			dfFechaInicio = null;
			dfFechaFin = null;
			dfCargaHorariaActual = null;
			dfDetalleCargaHorariaActual = null;
			dfCargaHorariaActiva = false;
			dfPeriodoAcademico = verificarPeriodoActivo();
			dfPreUniversidad = null;
			dfPreTitDoct = null;
			dfPreFechaInicio = null;
			dfPreFechaFin = null;
			dfPreHorasSemanales = null;
			dfFuncionInvestigacionSeleccion = null;
			dfFuncionPreinvestigacionSeleccion = null;
			dfPreCargaHorariaActual = null;
			dfPreDetalleCargaHorariaActual = null;
			dfActivarTipoDoctorado = false;
			dfActivarTipoPreDoctorado = false;
			dfLinkReporte = null;
			dfListaHorasSemanales = null;
			dfListaPreHorasSemanales = null;
			inicarParametros();
			limpiarBusquedaAvanzada();
		}

		/**
		 * Limpia la seleccion de campos en base al docente
		 */
		public void limpiarInfoDocente(){

			dfListaFunciones = null;
			dfFuncionSeleccion = null;
			dfTipoFuncionCargaHorariaSeleccion = null;
			dfDocente = null;
			dfHorasSemanales = null;
			dfUniversidad = null;
			dfTitDoct = null;
			dfFechaInicio = null;
			dfFechaFin = null;
			dfCargaHorariaActual = null;
			dfDetalleCargaHorariaActual = null;
			dfCargaHorariaActiva = false;
			dfPeriodoAcademico = verificarPeriodoActivo();
			dfPreUniversidad = null;
			dfPreTitDoct = null;
			dfPreFechaInicio = null;
			dfPreFechaFin = null;
			dfPreHorasSemanales = null;
			dfFuncionInvestigacionSeleccion = null;
			dfFuncionPreinvestigacionSeleccion = null;
			dfActivarTipoDoctorado = false;
			dfActivarTipoPreDoctorado = false;
			dfPreCargaHorariaActual = null;
			dfPreDetalleCargaHorariaActual = null;
			dfLinkReporte = null;
			dfListaHorasSemanales = null;
			dfListaPreHorasSemanales = null;
			
			Date date = new Date();
			dfFechaInicio = date;
			dfFechaFin = date;
			dfPreFechaInicio = date;
			dfPreFechaFin = date;
			
			limpiarBusquedaAvanzada();
		}
		
		/**
		 * Limpia parametros del cuadro de dialogo busqueda avanzada
		 */
		public void limpiarBusquedaAvanzada(){
			dfCedulaDocenteBusquedaAvanzada = null;
			dfApellidoDocenteBusquedaAvanzada = null;
			dfListDocentesBusquedaAvanzada = null;
			dfMensajeBusquedaAvanzada = null;
			dfLinkReporte = null;
			dfListaHorasSemanales = null;
			dfListaPreHorasSemanales = null;
		}
		
		// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

		/**
		 * Busca la entidad Docente basado en los parametros de ingreso
		 */
		public void buscar() {
			
//			if(validarCronograma()){	// Cambio Solicitado por Eco. Reinoso 12/12/2017
				try {
					limpiarInfoDocente();
					if (dfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
						if (dfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
							dfPeriodoAcademico = servDfPeriodoAcademicoServicio.buscarPorId(dfPeriodoAcademicoBuscar);
							dfDocente = servDfPersonaDatosServicioJdbc.buscarPorId(dfDocenteBuscar , dfPeriodoAcademicoBuscar); 
							dfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+dfDocente.getPrsIdentificacion()+"&prd="+dfPeriodoAcademico.getPracDescripcion();
							listarFunciones(); 
						} else {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.docente.validacion.exception")));
						}
					} else { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.facultad.validacion.exception")));
					}
	
				} catch (PersonaDatosDtoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.persona.datos.dto.no.encontrado.exception")));
				} catch (PersonaDatosDtoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.persona.datos.dto.exception")));
				} catch (PeriodoAcademicoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.periodo.academico.no.encontrado.exception")));
				} catch (PeriodoAcademicoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.periodo.academico.exception")));
				}
//			}
		}
		
		/**
		 * Carga informacion del docente seleccionado en el dialogo de busqueda avanzada
		 * @param item Item recibido con la infromacion del docente seleccionado
		 */
		public void asignarDocente( PersonaDatosDto item){
			try {
				
				limpiarInfoDocente();
				
				dfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				dfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				dfListaCarreras = null;
				dfListaDocentes = null;
				
					dfDocente = servDfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , dfPeriodoAcademicoBuscar);
					listarFunciones();
					limpiarBusquedaAvanzada();
					dfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+dfPeriodoAcademico.getPracDescripcion();
					
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.asignar.docente.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.asignar.docente.exception")));
			} 
		}
		
		/**
		 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
		 */
		public void buscarDocentes(){
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				try {
					
					if(dfCedulaDocenteBusquedaAvanzada == null){
						dfCedulaDocenteBusquedaAvanzada = "";
					}
					if(dfApellidoDocenteBusquedaAvanzada == null){
						dfApellidoDocenteBusquedaAvanzada = "";
					}
					dfListDocentesBusquedaAvanzada = servDfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(dfCedulaDocenteBusquedaAvanzada, dfApellidoDocenteBusquedaAvanzada);
					
				} catch (PersonaDatosDtoException e) {
					dfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.docente.exception"));
				} catch (PersonaDatosDtoNoEncontradoException e) {
					dfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.buscar.docente.no.encontrado.exception"));
				} 
//			}
		}

	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------

		
	/**
	 * Guarda los parametros del formulario
	 **/
	public void guardar() {
		
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(verificarGuardar()){
					if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){ 
						if(cargarCargaHoraria(dfDocente, 
								dfHorasSemanales, 
								dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								dfPeriodoAcademico, 
								dfTipoFuncionCargaHorariaSeleccion)){
							limpiar();
						} 				
					}
					
					if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
						if(cargarCargaHoraria(dfDocente, 
								dfPreHorasSemanales, 
								dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								dfPeriodoAcademico, 
								dfTipoFuncionCargaHorariaSeleccion)){
							limpiar();
						}   	 
					}
				} 
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.guardar.docente.validacion")));
			}
		}
	}	
	
	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {
		 
		Boolean verificar = true;
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			
			if(verificarDocente()){  
						   
				if(verificarGuardar()){
					
						if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
							verificar = false;  
							if(dfCargaHorariaActual != null){
								dfCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(dfTipoFuncionCargaHorariaSeleccion);
								dfCargaHorariaActual.setCrhrObservacion(dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dfCargaHorariaActual.setCrhrNumHoras(dfHorasSemanales);
								
								if(actualizarCargaHoraria(dfCargaHorariaActual)){ 
									dfDetalleCargaHorariaActual.setDtcrhrUniversidadDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfUniversidad).toUpperCase());
									dfDetalleCargaHorariaActual.setDtcrhrTituloDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfTitDoct).toUpperCase());
									dfDetalleCargaHorariaActual.setDtcrhrFechaInicio(dfFechaInicio);
									dfDetalleCargaHorariaActual.setDtcrhrFechaFin(dfFechaFin);
									dfDetalleCargaHorariaActual.setDtcrhrNumHoras(dfHorasSemanales);
									
									verificar = actualizarDetalleCargaHoraria(dfDetalleCargaHorariaActual); 
									
								}
							}else{ 
								if(cargarCargaHoraria(dfDocente, 
										dfHorasSemanales, 
										dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dfPeriodoAcademico, 
										dfTipoFuncionCargaHorariaSeleccion) != null){
									verificar = true;
								}
							}  			
						}else{
							if(dfCargaHorariaActual != null){ 
								dfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dfCargaHorariaActual)){ 
									dfDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
									verificar = actualizarDetalleCargaHoraria(dfDetalleCargaHorariaActual);
								}
							}
						}
					  
						 if(verificar){
							if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){ 
								verificar = false;  
								if(dfPreCargaHorariaActual != null){
									dfPreCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(dfTipoFuncionCargaHorariaSeleccion);
									dfPreCargaHorariaActual.setCrhrObservacion(dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
									dfPreCargaHorariaActual.setCrhrNumHoras(dfHorasSemanales);
									
									if(actualizarCargaHoraria(dfPreCargaHorariaActual)){ 
										dfPreDetalleCargaHorariaActual.setDtcrhrUniversidadDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfPreUniversidad).toUpperCase());
										dfPreDetalleCargaHorariaActual.setDtcrhrTituloDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfPreTitDoct).toUpperCase());
										dfPreDetalleCargaHorariaActual.setDtcrhrFechaInicio(dfPreFechaInicio);
										dfPreDetalleCargaHorariaActual.setDtcrhrFechaFin(dfPreFechaFin);
										dfPreDetalleCargaHorariaActual.setDtcrhrNumHoras(dfPreHorasSemanales);
										
										verificar = actualizarDetalleCargaHoraria(dfPreDetalleCargaHorariaActual); 
										
									}
								}else{ 
									if(cargarCargaHoraria(dfDocente, 
											dfPreHorasSemanales, 
											dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
											dfPeriodoAcademico, 
											dfTipoFuncionCargaHorariaSeleccion) != null){
										verificar = true;
									}
								} 				
							}else{ 
								if(dfPreCargaHorariaActual != null){ 
									dfPreCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
									if(actualizarCargaHoraria(dfPreCargaHorariaActual)){ 
										dfPreDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
										 verificar = actualizarDetalleCargaHoraria(dfPreDetalleCargaHorariaActual);
									} 
								}
							} 
						 }
						
					if(verificar){
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.guardar.cambios.con.exito.validacion")));
						limpiar();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.guardar.cambios.exception")));
					} 
				}  
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.guardar.cambios.docente.validacion.exception")));
			} 
		}
	}

	/**
	 * Elimina el registro
	 **/
	public void eliminar() {
		
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(dfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					
					Boolean verificar = false; 
					
					if(dfCargaHorariaActual != null){ 
							dfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
							if(actualizarCargaHoraria(dfCargaHorariaActual)){ 
								dfDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								verificar = actualizarDetalleCargaHoraria(dfDetalleCargaHorariaActual);
							} 
					}
					
					if(dfPreCargaHorariaActual != null){ 
							dfPreCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
							if(actualizarCargaHoraria(dfPreCargaHorariaActual)){ 
								dfPreDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								 verificar = actualizarDetalleCargaHoraria(dfPreDetalleCargaHorariaActual);
							}  
					}
					
					if(verificar){ 
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.eliminar.con.exito.validacion")));
						limpiar();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.eliminar.exception")));
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.eliminar.docente.validacion.exception")));
			} 
		}
	}

	/**
	 * Carga los datos y atributos de CARGA_HORARIA sobre el docente
	 * @param docente Docente al que se agrega carga horaria
	 * @param horasSemanales Numero de horas semanales asignadas al docente
	 * @param observacion Observacion descripcion de la carga asignada
	 * @param periodo Periodo Academico actual 
	 * @param tipo tipo funcion carga horaria a asignar
	 * @return retorna la verificacion en true o false del ingreso de carga horaria
	 * 
	 **/
	public Boolean cargarCargaHoraria(PersonaDatosDto docente, Integer horasSemanales, String observacion, PeriodoAcademico periodo, TipoFuncionCargaHoraria tipo){
		
		Boolean verificar = false;
		
		try {
			
			CargaHoraria crhr = new CargaHoraria();
			
			DetallePuesto dtps = new DetallePuesto();
			dtps.setDtpsId(docente.getDtpsId());
			
			crhr.setCrhrDetallePuesto(dtps);
			crhr.setCrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			crhr.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
			crhr.setCrhrNumHoras(horasSemanales);
			crhr.setCrhrObservacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(observacion).toUpperCase());
			crhr.setCrhrPeriodoAcademico(periodo);
			crhr.setCrhrTipoFuncionCargaHoraria(tipo); 
			
			crhr = servDfCargaHorariaServicio.anadir(crhr);
			
			DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
			
			dtcrhr.setDtcrhrCargaHoraria(crhr);
			
			if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
				dtcrhr.setDtcrhrUniversidadDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfUniversidad).toUpperCase());
				dtcrhr.setDtcrhrTituloDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfTitDoct).toUpperCase());
				dtcrhr.setDtcrhrFechaInicio(dfFechaInicio);
				dtcrhr.setDtcrhrFechaFin(dfFechaFin);
				dtcrhr.setDtcrhrNumHoras(dfHorasSemanales); 
			}
			
			if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
				dtcrhr.setDtcrhrUniversidadDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfPreUniversidad).toUpperCase());
				dtcrhr.setDtcrhrTituloDoctorado(GeneralesUtilidades.eliminarEspaciosEnBlanco(dfPreTitDoct).toUpperCase());
				dtcrhr.setDtcrhrFechaInicio(dfPreFechaInicio);
				dtcrhr.setDtcrhrFechaFin(dfPreFechaFin);
				dtcrhr.setDtcrhrNumHoras(dfPreHorasSemanales); 
			}
			
			
			dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);  
			
			servDfDetalleCargaHorariaServicio.anadir(dtcrhr);
			
			verificar = true;
			
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.cargar.carga.horaria.con.exito.validacion", observacion)));
			
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.cargar.carga.horaria.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.cargar.carga.horaria.detalle.carga.horaria.exception")));
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.cargar.carga.horaria.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.cargar.carga.horaria.carga.horaria.exception")));
		}
		
		return verificar;
	}
	
	/**
	 * Carga los campos de carga horaria del dcente
	 */
	public void listarCargaHorariaDocente(){ 
		try { 
			if(dfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: dfListaFunciones){
					
					if(itemFuncion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_TC_VALUE || itemFuncion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_MT_VALUE ||itemFuncion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_TP_VALUE ){
						dfListaCargasHorarias =  servDfCargaHorariaServicio.buscarPorDetallePuesto(dfDocente.getDtpsId(), itemFuncion.getTifncrhrId(), dfPeriodoAcademico.getPracId());
						if(dfListaCargasHorarias.size() > 0 ){
							for(CargaHoraria item: dfListaCargasHorarias ){ 
								dfCargaHorariaActual = item;
								dfListaDetalleCargaHoraria = servDfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
								if(dfListaDetalleCargaHoraria.size() > 0){
									for(DetalleCargaHoraria itemDetalle: dfListaDetalleCargaHoraria){
										dfFuncionInvestigacionSeleccion = new ArrayList<>();
										dfFuncionInvestigacionSeleccion.add(101);
										dfDetalleCargaHorariaActual = itemDetalle;
										dfUniversidad = itemDetalle.getDtcrhrUniversidadDoctorado();
										dfTitDoct = itemDetalle.getDtcrhrTituloDoctorado();
										dfFechaInicio = itemDetalle.getDtcrhrFechaInicio();
										dfFechaFin = itemDetalle.getDtcrhrFechaFin();
										dfHorasSemanales = itemDetalle.getDtcrhrNumHoras();
										dfCargaHorariaActiva = true;
										calcularHoras();
									}
								}	
							} 
						}
					}
					
					
					if(itemFuncion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_PREINVESTIGACION_TC_VALUE || itemFuncion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_PREINVESTIGACION_MT_VALUE ||itemFuncion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_PREINVESTIGACION_TP_VALUE ){
						dfListaCargasHorarias =  servDfCargaHorariaServicio.buscarPorDetallePuesto(dfDocente.getDtpsId(), itemFuncion.getTifncrhrId(), dfPeriodoAcademico.getPracId());
						if(dfListaCargasHorarias.size() > 0 ){
							for(CargaHoraria item: dfListaCargasHorarias ){ 
								dfPreCargaHorariaActual = item;
								dfListaDetalleCargaHoraria = servDfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
								if(dfListaDetalleCargaHoraria.size() > 0){
									for(DetalleCargaHoraria itemDetalle: dfListaDetalleCargaHoraria){
										dfFuncionPreinvestigacionSeleccion = new ArrayList<>();
										dfFuncionPreinvestigacionSeleccion.add(101);
										dfPreDetalleCargaHorariaActual = itemDetalle;
										dfPreUniversidad = itemDetalle.getDtcrhrUniversidadDoctorado();
										dfPreTitDoct = itemDetalle.getDtcrhrTituloDoctorado();
										dfPreFechaInicio = itemDetalle.getDtcrhrFechaInicio();
										dfPreFechaFin = itemDetalle.getDtcrhrFechaFin();
										dfPreHorasSemanales = itemDetalle.getDtcrhrNumHoras();
										dfCargaHorariaActiva = true;
										calcularHoras();
									}
								}	
							} 
						}
					}
				}
			}
								
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.carga.horaria.docente.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.carga.horaria.docente.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.carga.horaria.docente.detalle.carga.horaria.exception")));
		}
	}
	
	/**
	 *Actualiza los atributos de la entidad carga horaria
	 *@param entidad Entidad Carga Horaria actualizada
	 *@return Si si se actualiza No si mp se actualiza
	 */
	public Boolean actualizarCargaHoraria(CargaHoraria entidad){
		
		Boolean verificar = false;
		
		try { 
				verificar = servDfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.actualizar.carga.horaria.exception")));
		}
		return verificar;
	}
	
	/**
	 *Actualiza los atributos de la entidad detalle carga horaria
	 *@param entidad Entidad DetalleCargaHoraria actualizada
	 *@return Si si se actualiza No si mp se actualiza
	 */
	public Boolean actualizarDetalleCargaHoraria(DetalleCargaHoraria entidad){
	
		Boolean verificar = false;
	
		try {
			verificar = servDfDetalleCargaHorariaServicio.editar(entidad);
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.actualizar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.actualizar.detalle.carga.horaria.exception")));
		}
		
		return verificar;
	}
	  
	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------
 
	/**
	 * Lista de Entidades Dependencia al seleccionar un peridoso
	 **/
	public void seleccionarPeriodo() {
		limpiarInfoDocente(); 
		dfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		dfListaDocentes = null;
		dfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		dfListaCarreras = null; 
	}
	
	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {
		
		try {	
			limpiarInfoDocente();
			if(dfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				dfListaCarreras = servDfCarreraServicio.listarCarrerasXFacultad(dfFacultadBuscar);
				dfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dfListaDocentes = null;
				listarDocentesPorFacultad();
				dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}else{
				dfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				dfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			dfListaDocentes = null;
			dfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			dfListaDocentes = servDfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(dfFacultadBuscar, dfPeriodoAcademicoBuscar);
			dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
		}
	}
	
	
	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorCarrera() {
		
		try {
			limpiarInfoDocente();
			dfListaDocentes = servDfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(dfCarreraBuscar, dfPeriodoAcademicoBuscar);
			dfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
		}
	}
	
	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarFunciones() {
 
			try {
				if(verificarRelacionLaboral()){
					dfListaFunciones = servDfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_DOCTORADOS_VALUE); 
					listarCargaHorariaDocente();
				}
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.listar.funciones.exception")));
			}
			
			
	}
		
 
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
			/**
			 * Calcula las horas en doctorados en base a las reglas y valores en base de datos
			 */
			public void calcularHoras(){
				 
				try {
					
					if(verificarDocente()){
						
						dfActivarTipoDoctorado = false;
						dfActivarTipoPreDoctorado = false;
						
						if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
							dfActivarTipoPreDoctorado = true;
							dfHorasSemanales = null;
							dfPreHorasSemanales = null;
						}
						
						if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
							dfActivarTipoDoctorado = true;
							dfHorasSemanales = null;
							dfPreHorasSemanales = null;
						}
						
						if(!dfActivarTipoDoctorado || !dfActivarTipoPreDoctorado){
							dfHorasSemanales = null;
							dfPreHorasSemanales = null;
						}
					 
						 
						
						if(dfActivarTipoDoctorado || dfActivarTipoPreDoctorado){
							
							if (dfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
								
								Boolean verificarFuncion = true;
								
								switch (dfDocente.getTmddId()) {
								case 3://Tiempo Completo
									if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
										dfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_TC_VALUE;
									}
									if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
										dfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_PREINVESTIGACION_TC_VALUE;
									} 
									break;
								case 2://Medio Tiempo
									if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
										dfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_MT_VALUE;
									}
									if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
										dfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_PREINVESTIGACION_MT_VALUE;
									}
									break;
								case 1://Tiempo Parcial
									if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
										dfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_TP_VALUE;
									}
									if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
										dfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_DOCTORADO_PREINVESTIGACION_TP_VALUE;
									}
									break;
								default:
									verificarFuncion = false;
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.calcular.horas.validacion")));
									break;
								}
								
								if(verificarFuncion){
									dfTipoFuncionCargaHorariaSeleccion = servDfTipoFuncionCargaHorariaServicio.buscarPorId(dfFuncionSeleccion);
									
									if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
//										dfHorasSemanales = dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
										dfListaHorasSemanales = new ArrayList<>();
										for(int i = dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
											dfListaHorasSemanales.add(i); 
										}
									}else{
										dfHorasSemanales = null;	
									}
									if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){
//										dfPreHorasSemanales = dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
										dfListaPreHorasSemanales= new ArrayList<>();
										for(int i = dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= dfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
											dfListaPreHorasSemanales.add(i); 
										}
									}else{
										dfPreHorasSemanales = null;
									}
									
								}else{
									dfPreHorasSemanales = null;
									dfHorasSemanales = null;	
								} 
							}else{
								dfPreHorasSemanales = null;
								dfHorasSemanales = null;
							}
						}
					}else{ 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.calcular.horas.docente.no.encontrado.exception")));
					}
					 
				} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.calcular.horas.no.encontrado.exception")));
				} catch (TipoFuncionCargaHorariaException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.calcular.horas.exception")));
				}
			}
			
			/**
			 * Verifica la seleccion del docente
			 * @return si se ha seleccionado el docente o no 
			 */
			public Boolean verificarDocente(){
				Boolean verificar = false;
				if(dfDocente!=null){
					verificar = true;
				}
				return verificar;
			}
			
			/**
			 * Busca si existe un periodo activo
			 * @return retorna la entidad PeriodoAcademico activo en pregrado
			 **/
			public PeriodoAcademico verificarPeriodoActivo(){
				
				try {
					return servDfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				} catch (PeriodoAcademicoNoEncontradoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.periodo.activo.validacion.exception")));
					return null;
				} catch (PeriodoAcademicoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.periodo.activo.exception")));
					return null;
				}
			}
			
			/**
			 * Busca si existe un periodo de cierre
			 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
			 **/
			public PeriodoAcademico verificarPeriodoCierre(){
				
				try {
					return servDfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				} catch (PeriodoAcademicoNoEncontradoException e) {
					return null;
				} catch (PeriodoAcademicoException e) {
					return null;
				}
			}
			
			/**
			 * Busca si existe un periodo activo
			 * @return retorna la entidad PeriodoAcademico activo en pregrado
			 **/
			public PlanificacionCronogramaDto verificarCronograma(){
				
				try {
					return servDfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
				} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.cronograma.no.encontrado.exception")));
					return null;
				} catch (PlanificacionCronogramaDtoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.cronograma.exception")));
					return null;
				}
			}
			
			/**
			 * Valida el cronograa para uso de la carga horaria
			 **/
			public Boolean validarCronograma(){
				
				Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
				Boolean verificar = false;
				dfPlanificacionCronograma = verificarCronograma();
				if(dfPlanificacionCronograma != null){
					if(dfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
						if(dfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){ 
							verificar = true;
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.validar.cronograma.no.iniciado.validacion.exception")));
						}
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.validar.cronograma.expirado.validacion.exception")));
					}
				}
				return verificar;
			}
			
			/**
			 * Valida campos al guardar o gaurdar cambios
			 **/
			public Boolean verificarGuardar(){
				 
				Boolean verificar = true;
				
				if((dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0) || (dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0)){
					
					if(dfFuncionInvestigacionSeleccion != null && dfFuncionInvestigacionSeleccion.size() > 0){
						verificar = false;
						if(dfUniversidad != null && dfUniversidad.replaceAll(" ", "").length()!=0){
							if(dfTitDoct != null && dfTitDoct.replaceAll(" ", "").length()!=0){
								if(dfFechaInicio.before(dfFechaFin)){
									if(dfHorasSemanales != null){  
										verificar = true; 
									} 
								}else{ 
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.fechas.incorrectas.validacion")));
								}
							}else{ 
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.titulo.doctorado.validacion")));
							}
						}else{ 
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.nombre.universidad.validacion")));
						}   
					} 
				  
					 if(verificar){
						if(dfFuncionPreinvestigacionSeleccion != null && dfFuncionPreinvestigacionSeleccion.size() > 0){ 
							verificar = false;
							if(dfPreUniversidad != null && dfPreUniversidad.replaceAll(" ", "").length()!=0){
								if(dfPreTitDoct != null && dfPreTitDoct.replaceAll(" ", "").length()!=0){
									if(dfPreFechaInicio.before(dfPreFechaFin)){
										if(dfPreHorasSemanales != null){ 
											verificar = true; 
										}
									}else{ 
										FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.prefechas.incorrectas.validacion")));
									}
								}else{ 
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.pretitulo.doctorado.validacion")));
								}
							}else{ 
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.nombre.preuniversidad.validacion")));
							}
						} 
					 }
					 
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.guardar.sin.funcion.validacion")));
					verificar = false;
				}
				
				if(dfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
						|| dfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
					verificar = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.relacion.laboral.validacion.exception")));
				}
				 
				return verificar;
			}
					
			/**
			 * Carga los periodos en la lista para mostrar.
			 */
			public void cagarPeriodos(){
				
				dfListaPeriodosAcademicos = new ArrayList<>();
				
				PeriodoAcademico pracActivo = verificarPeriodoActivo();
				if(pracActivo != null){
					dfListaPeriodosAcademicos.add(pracActivo);
					dfPeriodoAcademico = pracActivo;
					dfPeriodoAcademicoBuscar = pracActivo.getPracId();
				}
				
				PeriodoAcademico pracCierre = verificarPeriodoCierre();
				if(pracCierre != null){
					dfListaPeriodosAcademicos.add(pracCierre);
				}
			}
			
			public Boolean verificarRelacionLaboral(){
				Boolean verificar= true;
				if(dfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
						|| dfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
					verificar = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Doctorados.verificar.relacion.laboral.validacion.exception")));
				}
				return verificar;
			}
			
			
			public void generarReporteCargaHorariaDocente(){
				if (dfDocente != null) {
					cargarAsignacionesCargaHoraria(dfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(dfPeriodoAcademico.getPracId(),dfPeriodoAcademico.getPracDescripcion()), dfUsuario, dfUsuario.getUsrIdentificacion());
					generarReporteIndividualCargaHoraria();
					rchfActivarReporte = 1;
				}
			}
			
		// ****************************************************************/
		// *********************** GETTERS Y SETTERS **********************/
		// ****************************************************************/

		
		public List<Dependencia> getDfListaFacultades() {
				return dfListaFacultades;
		}

		public void setDfListaFacultades(List<Dependencia> dfListaFacultades) {
			this.dfListaFacultades = dfListaFacultades;
		}

		public List<Carrera> getDfListaCarreras() {
			return dfListaCarreras;
		}

		public void setDfListaCarreras(List<Carrera> dfListaCarreras) {
			this.dfListaCarreras = dfListaCarreras;
		}

		public List<PersonaDatosDto> getDfListaDocentes() {
			return dfListaDocentes;
		}

		public void setDfListaDocentes(List<PersonaDatosDto> dfListaDocentes) {
			this.dfListaDocentes = dfListaDocentes;
		}

		public List<TipoFuncionCargaHoraria> getDfListaFunciones() {
			return dfListaFunciones;
		}

		public void setDfListaFunciones(List<TipoFuncionCargaHoraria> dfListaFunciones) {
			this.dfListaFunciones = dfListaFunciones;
		}

		public PersonaDatosDto getDfDocente() {
			return dfDocente;
		}

		public void setDfDocente(PersonaDatosDto dfDocente) {
			this.dfDocente = dfDocente;
		}

		public Integer getDfFacultadBuscar() {
			return dfFacultadBuscar;
		}

		public void setDfFacultadBuscar(Integer dfFacultadBuscar) {
			this.dfFacultadBuscar = dfFacultadBuscar;
		}

		public Integer getDfCarreraBuscar() {
			return dfCarreraBuscar;
		}

		public void setDfCarreraBuscar(Integer dfCarreraBuscar) {
			this.dfCarreraBuscar = dfCarreraBuscar;
		}

		public Integer getDfDocenteBuscar() {
			return dfDocenteBuscar;
		}

		public void setDfDocenteBuscar(Integer dfDocenteBuscar) {
			this.dfDocenteBuscar = dfDocenteBuscar;
		}

		public String getDfIdentificacionBuscar() {
			return dfIdentificacionBuscar;
		}

		public void setDfIdentificacionBuscar(String dfIdentificacionBuscar) {
			this.dfIdentificacionBuscar = dfIdentificacionBuscar;
		}

		public String getDfUniversidad() {
			return dfUniversidad;
		}

		public void setDfUniversidad(String dfUniversidad) {
			this.dfUniversidad = dfUniversidad;
		}

		public String getDfTitDoct() {
			return dfTitDoct;
		}

		public void setDfTitDoct(String dfTitDoct) {
			this.dfTitDoct = dfTitDoct;
		}

		public Date getDfFechaInicio() {
			return dfFechaInicio;
		}

		public void setDfFechaInicio(Date dfFechaInicio) {
			this.dfFechaInicio = dfFechaInicio;
		}

		public Date getDfFechaFin() {
			return dfFechaFin;
		}

		public void setDfFechaFin(Date dfFechaFin) {
			this.dfFechaFin = dfFechaFin;
		}

		public Integer getDfHorasSemanales() {
			return dfHorasSemanales;
		}

		public void setDfHorasSemanales(Integer dfHorasSemanales) {
			this.dfHorasSemanales = dfHorasSemanales;
		}
	 
		public Boolean getDfCargaHorariaActiva() {
			return dfCargaHorariaActiva;
		}
	 
		public void setDfCargaHorariaActiva(Boolean dfCargaHorariaActiva) {
			this.dfCargaHorariaActiva = dfCargaHorariaActiva;
		}
	 
		public Integer getDfPeriodoAcademicoBuscar() {
			return dfPeriodoAcademicoBuscar;
		}
	 
		public void setDfPeriodoAcademicoBuscar(Integer dfPeriodoAcademicoBuscar) {
			this.dfPeriodoAcademicoBuscar = dfPeriodoAcademicoBuscar;
		}
	 
		public List<PeriodoAcademico> getDfListaPeriodosAcademicos() {
			return dfListaPeriodosAcademicos;
		}
	 
		public void setDfListaPeriodosAcademicos(List<PeriodoAcademico> dfListaPeriodosAcademicos) {
			this.dfListaPeriodosAcademicos = dfListaPeriodosAcademicos;
		}
  
		public String getDfCedulaDocenteBusquedaAvanzada() {
			return dfCedulaDocenteBusquedaAvanzada;
		}
 
		public void setDfCedulaDocenteBusquedaAvanzada(String dfCedulaDocenteBusquedaAvanzada) {
			this.dfCedulaDocenteBusquedaAvanzada = dfCedulaDocenteBusquedaAvanzada;
		}
 
		public String getDfApellidoDocenteBusquedaAvanzada() {
			return dfApellidoDocenteBusquedaAvanzada;
		}
 
		public void setDfApellidoDocenteBusquedaAvanzada(String dfApellidoDocenteBusquedaAvanzada) {
			this.dfApellidoDocenteBusquedaAvanzada = dfApellidoDocenteBusquedaAvanzada;
		}
 
		public String getDfMensajeBusquedaAvanzada() {
			return dfMensajeBusquedaAvanzada;
		}
 
		public void setDfMensajeBusquedaAvanzada(String dfMensajeBusquedaAvanzada) {
			this.dfMensajeBusquedaAvanzada = dfMensajeBusquedaAvanzada;
		}
 
		public List<PersonaDatosDto> getDfListDocentesBusquedaAvanzada() {
			return dfListDocentesBusquedaAvanzada;
		}
 
		public void setDfListDocentesBusquedaAvanzada(List<PersonaDatosDto> dfListDocentesBusquedaAvanzada) {
			this.dfListDocentesBusquedaAvanzada = dfListDocentesBusquedaAvanzada;
		}
 
		public String getDfPreUniversidad() {
			return dfPreUniversidad;
		}
 
		public void setDfPreUniversidad(String dfPreUniversidad) {
			this.dfPreUniversidad = dfPreUniversidad;
		}
 
		public String getDfPreTitDoct() {
			return dfPreTitDoct;
		}
 
		public void setDfPreTitDoct(String dfPreTitDoct) {
			this.dfPreTitDoct = dfPreTitDoct;
		}
 
		public Date getDfPreFechaInicio() {
			return dfPreFechaInicio;
		}
 
		public void setDfPreFechaInicio(Date dfPreFechaInicio) {
			this.dfPreFechaInicio = dfPreFechaInicio;
		}
 
		public Date getDfPreFechaFin() {
			return dfPreFechaFin;
		} 
		
		public void setDfPreFechaFin(Date dfPreFechaFin) {
			this.dfPreFechaFin = dfPreFechaFin;
		}
 
		public Integer getDfPreHorasSemanales() {
			return dfPreHorasSemanales;
		}
 
		public void setDfPreHorasSemanales(Integer dfPreHorasSemanales) {
			this.dfPreHorasSemanales = dfPreHorasSemanales;
		}
 
		public List<Integer> getDfFuncionInvestigacionSeleccion() {
			return dfFuncionInvestigacionSeleccion;
		}
 
		public void setDfFuncionInvestigacionSeleccion(List<Integer> dfFuncionInvestigacionSeleccion) {
			this.dfFuncionInvestigacionSeleccion = dfFuncionInvestigacionSeleccion;
		}
 
		public List<Integer> getDfFuncionPreinvestigacionSeleccion() {
			return dfFuncionPreinvestigacionSeleccion;
		}
 
		public void setDfFuncionPreinvestigacionSeleccion(List<Integer> dfFuncionPreinvestigacionSeleccion) {
			this.dfFuncionPreinvestigacionSeleccion = dfFuncionPreinvestigacionSeleccion;
		} 
		
		public Boolean getDfActivarTipoDoctorado() {
			return dfActivarTipoDoctorado;
		}
 
		public void setDfActivarTipoDoctorado(Boolean dfActivarTipoDoctorado) {
			this.dfActivarTipoDoctorado = dfActivarTipoDoctorado;
		}
 
		public Boolean getDfActivarTipoPreDoctorado() {
			return dfActivarTipoPreDoctorado;
		}
 
		public void setDfActivarTipoPreDoctorado(Boolean dfActivarTipoPreDoctorado) {
			this.dfActivarTipoPreDoctorado = dfActivarTipoPreDoctorado;
		}
 
		public String getDfLinkReporte() {
			return dfLinkReporte;
		}
 
		public void setDfLinkReporte(String dfLinkReporte) {
			this.dfLinkReporte = dfLinkReporte;
		}


		public List<Integer> getDfListaHorasSemanales() {
			return dfListaHorasSemanales;
		}


		public void setDfListaHorasSemanales(List<Integer> dfListaHorasSemanales) {
			this.dfListaHorasSemanales = dfListaHorasSemanales;
		}


		public List<Integer> getDfListaPreHorasSemanales() {
			return dfListaPreHorasSemanales;
		}


		public void setDfListaPreHorasSemanales(List<Integer> dfListaPreHorasSemanales) {
			this.dfListaPreHorasSemanales = dfListaPreHorasSemanales;
		}


		public Usuario getDfUsuario() {
			return dfUsuario;
		}


		public void setDfUsuario(Usuario dfUsuario) {
			this.dfUsuario = dfUsuario;
		}
		
		
	 
		
	}