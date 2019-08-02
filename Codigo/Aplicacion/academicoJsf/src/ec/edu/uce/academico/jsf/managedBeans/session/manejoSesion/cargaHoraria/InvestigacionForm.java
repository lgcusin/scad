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
   
 ARCHIVO:     InvestigacionForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Investigacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
04-SEPTIEMBRE-2017		 Arturo Villafuerte 			       Emisión Inicial
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
import ec.edu.uce.academico.ejb.utilidades.constantes.TiempoDedicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoCargaHorariaConstantes;
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
 * Clase (session bean) InvestigacionForm.java Bean de sesión que maneja
 * los atributos del formulario de Investigacion.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "investigacionForm")
@SessionScoped
public class InvestigacionForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private Usuario ifUsuario;

	private List<Dependencia> ifListaFacultades;
	private List<Carrera> ifListaCarreras;
	private List<PersonaDatosDto> ifListaDocentes;
	private List<TipoFuncionCargaHoraria> ifListaFunciones;

	private PersonaDatosDto ifDocente;

	private Integer ifFacultadBuscar;
	private Integer ifCarreraBuscar;
	private Integer ifDocenteBuscar;
	private String ifIdentificacionBuscar;

	private TipoFuncionCargaHoraria ifTipoFuncionCargaHorariaSeleccion;
	private Integer ifFuncionSeleccion; 
	
	private String ifNombreProyecto;
	private Date ifFechaInicio;
	private Date ifFechaFin;
	private Integer ifHorasSemanales;
	
	private PeriodoAcademico ifPeriodoAcademico;
	private Integer ifPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> ifListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto ifPlanificacionCronograma;
	
	private List<CargaHoraria> ifListaCargasHorarias;
	private List<DetalleCargaHoraria> ifListaDetalleCargaHoraria;
	
	private CargaHoraria ifCargaHorariaActual;
	private DetalleCargaHoraria ifDetalleCargaHorariaActual;
	
	private Boolean ifCargaHorariaActiva;
	  
	private String ifCedulaDocenteBusquedaAvanzada;
	private String ifApellidoDocenteBusquedaAvanzada;
	private String ifMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> ifListDocentesBusquedaAvanzada;
	
	//--v2
	//URL reporte
	private String ifLinkReporte;
	
	//--v3
	private List<Integer> ifListaHorasInvestigacion;

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
	DependenciaServicio servIfDependenciaServicio;
	@EJB
	CarreraServicio servIfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servIfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servIfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servIfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servIfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servIfPlanificacionCronogramaDtoServicioJdbc; 
	@EJB
	CargaHorariaServicio servIfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servIfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servIfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servIfUsuarioRolServicio;
	
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
		ifFechaInicio = date;
		ifFechaFin = date;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

		
		try {	
			 
			ifCargaHorariaActiva= false;
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				validarCronograma();
				cagarPeriodos();
				ifListaFacultades = servIfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = ifListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			}		 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de aseguramiento de calidad.
	 */
	public String irFormularioInvestigacion(Usuario usuario) {
		ifUsuario = usuario;
		inicarParametros();
		return "irFormularioInvestigacion";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		ifListaFacultades = null;
		ifListaCarreras = null;
		ifListaDocentes = null;
		ifListaFunciones = null;
		ifDocente = null;
		ifFacultadBuscar = null;
		ifCarreraBuscar = null;
		ifDocenteBuscar = null;
		ifIdentificacionBuscar = null;
		ifTipoFuncionCargaHorariaSeleccion = null;
		ifFuncionSeleccion = null;  
		ifNombreProyecto = null;
		ifFechaInicio = null;
		ifFechaFin = null;
		ifHorasSemanales = null;
		ifCargaHorariaActual = null;
		ifDetalleCargaHorariaActual = null;
		ifCargaHorariaActiva= false;
		ifPeriodoAcademicoBuscar = null;
		ifListaPeriodosAcademicos = null;
		ifLinkReporte = null;
		ifListaHorasInvestigacion = null;
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

		/**
		 * Setea y nulifica a los valores iniciales de cada parametro
		 */
		public void limpiar() {
			ifFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			ifCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE; 
			ifIdentificacionBuscar = null;
			ifTipoFuncionCargaHorariaSeleccion = null;
			ifFuncionSeleccion = null;
			ifHorasSemanales = null;
			ifListaFunciones = null;
			ifListaCarreras = null;
			ifListaFacultades = null;
			ifListaDocentes = null; 
			ifDocente = null; 
			ifNombreProyecto = null;
			ifFechaInicio = null;
			ifFechaFin = null;
			ifCargaHorariaActual = null;
			ifDetalleCargaHorariaActual = null;
			ifCargaHorariaActiva= false;
			ifLinkReporte = null;
			ifPeriodoAcademico = verificarPeriodoActivo();
			ifListaHorasInvestigacion = null;
			inicarParametros();
		}

		/**
		 * Limpia la seleccion de campos en base al docente
		 */
		public void limpiarInfoDocente(){

			ifListaFunciones = null;
			ifFuncionSeleccion = null;
			ifTipoFuncionCargaHorariaSeleccion = null;
			ifDocente = null;
			ifHorasSemanales = null; 
			ifNombreProyecto = null;
			ifFechaInicio = null;
			ifFechaFin = null;
			ifCargaHorariaActual = null;
			ifDetalleCargaHorariaActual = null;
			ifCargaHorariaActiva= false;
			ifLinkReporte = null;
			ifPeriodoAcademico = verificarPeriodoActivo();
			ifListaHorasInvestigacion = null;
			
			Date date = new Date();
			ifFechaInicio = date;
			ifFechaFin = date;
		}
		
		/**
		 * Limpia parametros del cuadro de dialogo busqueda avanzada
		 */
		public void limpiarBusquedaAvanzada(){
			ifCedulaDocenteBusquedaAvanzada = null;
			ifApellidoDocenteBusquedaAvanzada = null;
			ifListDocentesBusquedaAvanzada = null;
			ifMensajeBusquedaAvanzada = null;
			ifLinkReporte = null;
			ifListaHorasInvestigacion = null;
		}
		
		// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

		/**
		 * Busca la entidad Docente basado en los parametros de ingreso
		 */
		public void buscar() {
			
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				try {
					limpiarInfoDocente();
					if (ifFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
						if (ifDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
							ifPeriodoAcademico = servIfPeriodoAcademicoServicio.buscarPorId(ifPeriodoAcademicoBuscar);
							ifDocente = servIfPersonaDatosServicioJdbc.buscarPorId(ifDocenteBuscar , ifPeriodoAcademicoBuscar); 
							ifLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+ifDocente.getPrsIdentificacion()+"&prd="+ifPeriodoAcademico.getPracDescripcion();
							listarFunciones(); 
						} else {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.docente.validacion.exception")));
						}
					} else { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.facultad.validacion.exception")));
					}
	
				} catch (PersonaDatosDtoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.persona.datos.dto.no.encontrado.exception")));
				} catch (PersonaDatosDtoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.persona.datos.dto.exception")));
				} catch (PeriodoAcademicoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.periodo.academico.no.encontrado.exception")));
				} catch (PeriodoAcademicoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.periodo.academico.exception")));
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
				ifFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				ifCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				ifListaCarreras = null;
				ifListaDocentes = null;
				
					ifDocente = servIfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId(), ifPeriodoAcademicoBuscar);
					listarFunciones(); 
					limpiarBusquedaAvanzada();
					ifLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+ifPeriodoAcademico.getPracDescripcion();

					
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.asignar.docente.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.asignar.docente.exception")));
			}
			
		}
		
		/**
		 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
		 */
		public void buscarDocentes(){
			
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				try {
					
					if(ifCedulaDocenteBusquedaAvanzada == null){
						ifCedulaDocenteBusquedaAvanzada = "";
					}
					if(ifApellidoDocenteBusquedaAvanzada == null){
						ifApellidoDocenteBusquedaAvanzada = "";
					}
					ifListDocentesBusquedaAvanzada = servIfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(ifCedulaDocenteBusquedaAvanzada, ifApellidoDocenteBusquedaAvanzada);
					
				} catch (PersonaDatosDtoException e) {
					ifMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.docente.exception"));
				} catch (PersonaDatosDtoNoEncontradoException e) {
					ifMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.buscar.docente.no.encontrado.exception"));
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
					cargarCargaHoraria(ifDocente, 
							ifHorasSemanales, 
							ifTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
							ifPeriodoAcademico, 
							ifTipoFuncionCargaHorariaSeleccion);			
						
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.guardar.docente.validacion")));
			}
		}
	}
	 
	  
	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {

		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				 if(verificarGuardar()){
						
						ifCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(ifTipoFuncionCargaHorariaSeleccion);
						ifCargaHorariaActual.setCrhrObservacion(ifTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
						ifCargaHorariaActual.setCrhrNumHoras(ifHorasSemanales);
						
						if(actualizarCargaHoraria(ifCargaHorariaActual)){
							 
							ifDetalleCargaHorariaActual.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(ifNombreProyecto).toUpperCase());
							ifDetalleCargaHorariaActual.setDtcrhrFechaInicio(ifFechaInicio);
							ifDetalleCargaHorariaActual.setDtcrhrFechaFin(ifFechaFin);
							ifDetalleCargaHorariaActual.setDtcrhrNumHoras(ifHorasSemanales);
							
							if(actualizarDetalleCargaHoraria(ifDetalleCargaHorariaActual)){
								FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.guardar.cambios.con.exito.validacion")));
								limpiar();
							} 
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.guardar.cambios.exception")));
						}
					 
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.guardar.cambios.docente.validacion.exception")));
			}
		}
	}

	/**
	 * Elimina el registro
	 **/
	public void eliminar() {
		 
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(ifCargaHorariaActual != null){ 
						ifCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(ifCargaHorariaActual)){ 
							ifDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
							
							if(actualizarDetalleCargaHoraria(ifDetalleCargaHorariaActual)){
								FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.eliminar.con.exito.validacion")));
								limpiar();
							} 
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.eliminar.exception")));
						} 
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.eliminar.docente.validacion.exception")));
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
	public CargaHoraria cargarCargaHoraria(PersonaDatosDto docente, Integer horasSemanales, String observacion, PeriodoAcademico periodo, TipoFuncionCargaHoraria tipo){
		
		CargaHoraria crhrResult = null;
		
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
			
			crhrResult = servIfCargaHorariaServicio.anadir(crhr);
			
			DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
			
			dtcrhr.setDtcrhrCargaHoraria(crhrResult);
			dtcrhr.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(ifNombreProyecto).toUpperCase());
			dtcrhr.setDtcrhrFechaInicio(ifFechaInicio);
			dtcrhr.setDtcrhrFechaFin(ifFechaFin);
			dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
			dtcrhr.setDtcrhrNumHoras(ifHorasSemanales);
			
			servIfDetalleCargaHorariaServicio.anadir(dtcrhr);
			
			 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.cargar.carga.horaria.con.exito.validacion", observacion)));
			limpiar();
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.cargar.carga.horaria.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.cargar.carga.horaria.detalle.carga.horaria.exception")));
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.cargar.carga.horaria.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.cargar.carga.horaria.carga.horaria.exception")));
		}
		
		return crhrResult;
	}

	/**
	 * Carga los campos de carga horaria del dcente
	 */
	public void listarCargaHorariaDocente(){ 
		try { 
			if(ifListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: ifListaFunciones){
					ifListaCargasHorarias =  servIfCargaHorariaServicio.buscarPorDetallePuesto(ifDocente.getDtpsId(), itemFuncion.getTifncrhrId(), ifPeriodoAcademico.getPracId());
					if(ifListaCargasHorarias.size() > 0 ){
						
						for(CargaHoraria item: ifListaCargasHorarias ){ 
							ifCargaHorariaActual = item;
							ifListaDetalleCargaHoraria = servIfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
							
							if(ifListaDetalleCargaHoraria.size() > 0){
								for(DetalleCargaHoraria itemDetalle: ifListaDetalleCargaHoraria){
									ifDetalleCargaHorariaActual = itemDetalle;
									ifFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
									ifNombreProyecto = itemDetalle.getDtcrhrNombreProyecto();
									ifFechaInicio = itemDetalle.getDtcrhrFechaInicio();
									ifFechaFin = itemDetalle.getDtcrhrFechaFin();
									ifHorasSemanales = itemDetalle.getDtcrhrNumHoras();
									calcularHoras();
									ifCargaHorariaActiva= true;
								}
							}	
						} 
					}
				}
			}
								
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.carga.horaria.docente.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.carga.horaria.docente.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.carga.horaria.docente.detalle.carga.horaria.exception")));
		}
	}
	
	
	
	
	/**
	 *Actualiza los atributos de la entidad carga horaria
	 *@param entidad Entidad Carga Horaria actualizada
	 *@return Si
	 * si se actualiza No si mp se actualiza
	 */
	public Boolean actualizarCargaHoraria(CargaHoraria entidad){
		
		Boolean verificar = false; 
		try { 
				verificar = servIfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.actualizar.carga.horaria.exception")));
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
			verificar = servIfDetalleCargaHorariaServicio.editar(entidad);
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.actualizar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.actualizar.detalle.carga.horaria.exception")));
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
		ifFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		ifListaDocentes = null;
		ifCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		ifListaCarreras = null; 
	}
	
	
	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {
		
		try {	
			limpiarInfoDocente();
			if(ifFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				ifListaCarreras = servIfCarreraServicio.listarCarrerasXFacultad(ifFacultadBuscar);
				ifCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				ifListaDocentes = null;
				listarDocentesPorFacultad();
				ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}else{
				ifFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				ifCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			ifListaDocentes = null;
			ifCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			ifListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			ifListaDocentes = servIfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(ifFacultadBuscar, ifPeriodoAcademicoBuscar);
			ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			ifListaDocentes = servIfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(ifCarreraBuscar, ifPeriodoAcademicoBuscar);
			ifDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
		}
	}
	/**
	 * Lista de Entidades Docente por carrera
	 **/
	@SuppressWarnings("rawtypes")
	public void listarFunciones() {

			try {
				
				Boolean verificarFuncion = true;
				List<TipoFuncionCargaHoraria> funciones = null;
				
				if(ifDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
						|| ifDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
					verificarFuncion = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.funciones.relacion.laboral.validacion.exception")));
				}else{
					switch (ifDocente.getTmddId()) {
					case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
						verificarFuncion = true;
						funciones = servIfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_INVESTIGACION_VALUE);
						if (funciones != null && funciones.size() >0) {
							Iterator iter = funciones.iterator();
							while(iter.hasNext()){
								TipoFuncionCargaHoraria cad = (TipoFuncionCargaHoraria) iter.next();
								if(!cad.getTifncrhrFuncionCargaHoraria().getFncrhrTiempoDedicacion().equals(TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE)){
									iter.remove();
								}
							}
						}
						break;
					case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
						verificarFuncion = true;
						funciones = servIfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_INVESTIGACION_VALUE);
						if (funciones != null && funciones.size() >0) {
							Iterator iter = funciones.iterator();
							while(iter.hasNext()){
								TipoFuncionCargaHoraria cad = (TipoFuncionCargaHoraria) iter.next();
								if(!cad.getTifncrhrFuncionCargaHoraria().getFncrhrTiempoDedicacion().equals(TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE)){
									iter.remove();
								}
							}
						}
						break;
					case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
						verificarFuncion = true;
						funciones = servIfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_INVESTIGACION_VALUE);
						if (funciones != null && funciones.size() >0) {
							Iterator iter = funciones.iterator();
							while(iter.hasNext()){
								TipoFuncionCargaHoraria cad = (TipoFuncionCargaHoraria) iter.next();
								if(!cad.getTifncrhrFuncionCargaHoraria().getFncrhrTiempoDedicacion().equals(TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE)){
									iter.remove();
								}
							}
						}
						break;
					default:
						verificarFuncion = false;
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.funciones.validacion")));
						break;
					}
					
					ifListaFunciones = funciones;
				}
				
				if(verificarFuncion){
//					ifListaFunciones = servIfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_INVESTIGACION_VALUE);
					listarCargaHorariaDocente();
				}
				
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.listar.funciones.exception")));
			}
			
	}

	
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	/**
	 * Calcula las horas para investigacion en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){
		
		try {
			if (ifFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
				
				ifTipoFuncionCargaHorariaSeleccion = servIfTipoFuncionCargaHorariaServicio.buscarPorId(ifFuncionSeleccion);
				 
				ifListaHorasInvestigacion = new ArrayList<>();
				for(int i = ifTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= ifTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
					ifListaHorasInvestigacion.add(i); 
				}
				
//				ifHorasSemanales = ifTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();				
			}else{
				ifHorasSemanales = null;
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.calcular.horas.exception")));
		}
	}
	
	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(ifDocente!=null){
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
			return servIfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.periodo.activo.validacion.exception")));
			return null;
		} catch (PeriodoAcademicoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.periodo.activo.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){
		
		try {
			return servIfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
			return servIfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.cronograma.exception")));
			return null;
		}
	}
	
	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		ifPlanificacionCronograma = verificarCronograma();
		if(ifPlanificacionCronograma != null){
			if(ifPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(ifPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}
	
	/**
	 * Valida campos al guardar o gaurdar cambios
	 **/
	public Boolean verificarGuardar(){
		 
		Boolean verificar = false;
		
		if(ifFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
			if(ifNombreProyecto != null && ifNombreProyecto.replaceAll(" ", "").length()!=0){
				if(ifFechaInicio.before(ifFechaFin)){
					if(ifHorasSemanales != null){
						verificar = true;
					}
				}else{ 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.guardar.fechas.incorrectas.validacion")));
				}
			}else{ 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.guardar.nombre.proyecto.validacion")));
			}
		}else{ 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Investigacion.verificar.guardar.funcion.validacion")));
		}
		 
		return verificar;
	}
	
	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		ifListaPeriodosAcademicos = new ArrayList<>();
		
		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			ifListaPeriodosAcademicos.add(pracActivo);
			ifPeriodoAcademico = pracActivo;
			ifPeriodoAcademicoBuscar = pracActivo.getPracId();
		}
		
		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			ifListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	public void generarReporteCargaHorariaDocente(){
		if (ifDocente != null) {
			cargarAsignacionesCargaHoraria(ifDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(ifPeriodoAcademico.getPracId(),ifPeriodoAcademico.getPracDescripcion()), ifUsuario, ifUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}

// ****************************************************************/
// *********************** GETTERS Y SETTERS **********************/
// ****************************************************************/

	public List<Dependencia> getIfListaFacultades() {
		return ifListaFacultades;
	}

	public void setIfListaFacultades(List<Dependencia> ifListaFacultades) {
		this.ifListaFacultades = ifListaFacultades;
	}

	public List<Carrera> getIfListaCarreras() {
		return ifListaCarreras;
	}

	public void setIfListaCarreras(List<Carrera> ifListaCarreras) {
		this.ifListaCarreras = ifListaCarreras;
	}

	public List<PersonaDatosDto> getIfListaDocentes() {
		return ifListaDocentes;
	}

	public void setIfListaDocentes(List<PersonaDatosDto> ifListaDocentes) {
		this.ifListaDocentes = ifListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getIfListaFunciones() {
		return ifListaFunciones;
	}

	public void setIfListaFunciones(List<TipoFuncionCargaHoraria> ifListaFunciones) {
		this.ifListaFunciones = ifListaFunciones;
	}

	public PersonaDatosDto getIfDocente() {
		return ifDocente;
	}

	public void setIfDocente(PersonaDatosDto ifDocente) {
		this.ifDocente = ifDocente;
	}

	public Integer getIfFacultadBuscar() {
		return ifFacultadBuscar;
	}

	public void setIfFacultadBuscar(Integer ifFacultadBuscar) {
		this.ifFacultadBuscar = ifFacultadBuscar;
	}

	public Integer getIfCarreraBuscar() {
		return ifCarreraBuscar;
	}

	public void setIfCarreraBuscar(Integer ifCarreraBuscar) {
		this.ifCarreraBuscar = ifCarreraBuscar;
	}

	public Integer getIfDocenteBuscar() {
		return ifDocenteBuscar;
	}

	public void setIfDocenteBuscar(Integer ifDocenteBuscar) {
		this.ifDocenteBuscar = ifDocenteBuscar;
	}

	public String getIfIdentificacionBuscar() {
		return ifIdentificacionBuscar;
	}

	public void setIfIdentificacionBuscar(String ifIdentificacionBuscar) {
		this.ifIdentificacionBuscar = ifIdentificacionBuscar;
	}

	public Integer getIfFuncionSeleccion() {
		return ifFuncionSeleccion;
	}

	public void setIfFuncionSeleccion(Integer ifFuncionSeleccion) {
		this.ifFuncionSeleccion = ifFuncionSeleccion;
	}

	public Date getIfFechaInicio() {
		return ifFechaInicio;
	}

	public void setIfFechaInicio(Date ifFechaInicio) {
		this.ifFechaInicio = ifFechaInicio;
	}

	public Date getIfFechaFin() {
		return ifFechaFin;
	}

	public void setIfFechaFin(Date ifFechaFin) {
		this.ifFechaFin = ifFechaFin;
	}

	public Integer getIfHorasSemanales() {
		return ifHorasSemanales;
	}

	public void setIfHorasSemanales(Integer ifHorasSemanales) {
		this.ifHorasSemanales = ifHorasSemanales;
	}

	public String getIfNombreProyecto() {
		return ifNombreProyecto;
	}

	public void setIfNombreProyecto(String ifNombreProyecto) {
		this.ifNombreProyecto = ifNombreProyecto;
	}

	public Boolean getIfCargaHorariaActiva() {
		return ifCargaHorariaActiva;
	}

	public void setIfCargaHorariaActiva(Boolean ifCargaHorariaActiva) {
		this.ifCargaHorariaActiva = ifCargaHorariaActiva;
	}

	public Integer getIfPeriodoAcademicoBuscar() {
		return ifPeriodoAcademicoBuscar;
	}

	public void setIfPeriodoAcademicoBuscar(Integer ifPeriodoAcademicoBuscar) {
		this.ifPeriodoAcademicoBuscar = ifPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getIfListaPeriodosAcademicos() {
		return ifListaPeriodosAcademicos;
	}

	public void setIfListaPeriodosAcademicos(List<PeriodoAcademico> ifListaPeriodosAcademicos) {
		this.ifListaPeriodosAcademicos = ifListaPeriodosAcademicos;
	}

	public String getIfCedulaDocenteBusquedaAvanzada() {
		return ifCedulaDocenteBusquedaAvanzada;
	}

	public void setIfCedulaDocenteBusquedaAvanzada(String ifCedulaDocenteBusquedaAvanzada) {
		this.ifCedulaDocenteBusquedaAvanzada = ifCedulaDocenteBusquedaAvanzada;
	}

	public String getIfApellidoDocenteBusquedaAvanzada() {
		return ifApellidoDocenteBusquedaAvanzada;
	}

	public void setIfApellidoDocenteBusquedaAvanzada(String ifApellidoDocenteBusquedaAvanzada) {
		this.ifApellidoDocenteBusquedaAvanzada = ifApellidoDocenteBusquedaAvanzada;
	}

	public String getIfMensajeBusquedaAvanzada() {
		return ifMensajeBusquedaAvanzada;
	}

	public void setIfMensajeBusquedaAvanzada(String ifMensajeBusquedaAvanzada) {
		this.ifMensajeBusquedaAvanzada = ifMensajeBusquedaAvanzada;
	}

	public List<PersonaDatosDto> getIfListDocentesBusquedaAvanzada() {
		return ifListDocentesBusquedaAvanzada;
	}

	public void setIfListDocentesBusquedaAvanzada(List<PersonaDatosDto> ifListDocentesBusquedaAvanzada) {
		this.ifListDocentesBusquedaAvanzada = ifListDocentesBusquedaAvanzada;
	}

	public String getIfLinkReporte() {
		return ifLinkReporte;
	}

	public void setIfLinkReporte(String ifLinkReporte) {
		this.ifLinkReporte = ifLinkReporte;
	}

	public List<Integer> getIfListaHorasInvestigacion() {
		return ifListaHorasInvestigacion;
	}

	public void setIfListaHorasInvestigacion(List<Integer> ifListaHorasInvestigacion) {
		this.ifListaHorasInvestigacion = ifListaHorasInvestigacion;
	}

	public Usuario getIfUsuario() {
		return ifUsuario;
	}

	public void setIfUsuario(Usuario ifUsuario) {
		this.ifUsuario = ifUsuario;
	} 

	
}