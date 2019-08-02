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

 ARCHIVO:     VinculacionForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Vinculacion. 
 *************************************************************************
                           	MODvfICACIONES

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
 * Clase (session bean) VinculacionForm.java Bean de sesión que maneja
 * los atributos del formulario de Vinculacion.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "vinculacionForm")
@SessionScoped
public class VinculacionForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private Usuario vfUsuario;

	private List<Dependencia> vfListaFacultades;
	private List<Carrera> vfListaCarreras;
	private List<PersonaDatosDto> vfListaDocentes;
	private List<TipoFuncionCargaHoraria> vfListaFunciones;

	private PersonaDatosDto vfDocente;

	private Integer vfFacultadBuscar;
	private Integer vfCarreraBuscar;
	private Integer vfDocenteBuscar;
	private String vfIdentificacionBuscar;

	private TipoFuncionCargaHoraria vfTipoFuncionCargaHorariaSeleccion;
	private Integer vfFuncionSeleccion; 
//	private Integer vfHorasSemanalesFuncion;

//	private TipoFuncionCargaHoraria vfTipoFuncionCargaHorariaXTmdd;
	private Integer vfFuncionXTmdd;
//	private Integer vfHorasSemanalesXTmdd;

	private String vfNombreProyecto;
	private Date vfFechaInicio;
	private Date vfFechaFin;
	private Integer vfHorasSemanales;

	private PeriodoAcademico vfPeriodoAcademico;
	private Integer vfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> vfListaPeriodosAcademicos;

	private PlanificacionCronogramaDto vfPlanificacionCronograma;

	private List<CargaHoraria> vfListaCargasHorarias;
	private List<DetalleCargaHoraria> vfListaDetalleCargaHoraria;

//	private CargaHoraria vfCargaHorariaInvestigadorActual;
//	private DetalleCargaHoraria vfDetalleCargaHorariaInvestigadorActual;
	private CargaHoraria vfCargaHorariaFuncionActual;
	private DetalleCargaHoraria vfDetalleCargaHorariaFuncionActual;

	private Boolean vfCargaHorariaActiva;

	private String vfCedulaDocenteBusquedaAvanzada;
	private String vfApellidoDocenteBusquedaAvanzada;
	private String vfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> vfListDocentesBusquedaAvanzada;
	
	
	private List<Integer> vfListaHorasProyecto;

	//--v2
	//URL reporte
	private String vfLinkReporte;

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
	DependenciaServicio servVfDependenciaServicio;
	@EJB
	CarreraServicio servVfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servVfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servVfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servVfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servVfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servVfPlanificacionCronogramaDtoServicioJdbc; 
	@EJB
	CargaHorariaServicio servVfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servVfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servVfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servVfUsuarioRolServicio;


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
		vfFechaInicio = date;
		vfFechaFin = date;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;


		try {	

			vfCargaHorariaActiva = false; 
			//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			validarCronograma();
			cagarPeriodos();
			vfListaFacultades = servVfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			Iterator it = vfListaFacultades.iterator();
			while(it.hasNext()){
				Dependencia cad = (Dependencia) it.next();
				if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					it.remove();
				}
			}
			//			}		 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * @return navegacion hacia el formulario de aseguramiento de calidad.
	 */
	public String irFormularioVinculacion(Usuario usuario) {
		vfUsuario = usuario;
		inicarParametros();
		return "irFormularioVinculacion";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {

		vfListaFacultades = null;
		vfListaCarreras = null;
		vfListaDocentes = null;
		vfListaFunciones = null;
		vfDocente = null;
		vfFacultadBuscar = null;
		vfCarreraBuscar = null;
		vfDocenteBuscar = null;
		vfIdentificacionBuscar = null;
		vfTipoFuncionCargaHorariaSeleccion = null;
		vfFuncionSeleccion = null;  
		vfNombreProyecto = null;
		vfFechaInicio = null;
		vfFechaFin = null;
		vfHorasSemanales = null;
//		vfCargaHorariaInvestigadorActual = null;
//		vfDetalleCargaHorariaInvestigadorActual = null;
		vfCargaHorariaFuncionActual = null;
		vfDetalleCargaHorariaFuncionActual = null; 
		vfCargaHorariaActiva = false;
		vfPeriodoAcademicoBuscar = null;
		vfListaPeriodosAcademicos = null;
		vfLinkReporte = null;
		limpiar();
		limpiarBusquedaAvanzada();
		limpiarInfoDocente();
		vfListaHorasProyecto = null;

		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulvfica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		vfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		vfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE; 
		vfIdentificacionBuscar = null;
		vfTipoFuncionCargaHorariaSeleccion = null;
		vfFuncionSeleccion = null;
		vfHorasSemanales = null;
		vfListaFunciones = null;
		vfListaCarreras = null;
		vfListaFacultades = null;
		vfListaDocentes = null; 
		vfDocente = null; 
		vfNombreProyecto = null;
		vfFechaInicio = null;
		vfFechaFin = null;
//		vfHorasSemanalesXTmdd = null;
//		vfTipoFuncionCargaHorariaXTmdd = null;
//		vfCargaHorariaInvestigadorActual = null;
//		vfDetalleCargaHorariaInvestigadorActual = null;
		vfCargaHorariaFuncionActual = null;
		vfDetalleCargaHorariaFuncionActual = null; 
		vfCargaHorariaActiva = false;
		vfLinkReporte = null;
		vfListaHorasProyecto = null;
		vfPeriodoAcademico = verificarPeriodoActivo();
		inicarParametros();
	}

	/**
	 * Limpia la seleccion de campos en base al docente
	 */
	public void limpiarInfoDocente(){
		vfListaFunciones = null;
		vfFuncionSeleccion = null;
		vfTipoFuncionCargaHorariaSeleccion = null;
		vfDocente = null;
		vfHorasSemanales = null; 
		vfNombreProyecto = null;
		vfFechaInicio = null;
		vfFechaFin = null;
//		vfHorasSemanalesXTmdd = null;
//		vfTipoFuncionCargaHorariaXTmdd = null;
//		vfCargaHorariaInvestigadorActual = null;
//		vfDetalleCargaHorariaInvestigadorActual = null;
		vfCargaHorariaFuncionActual = null;
		vfDetalleCargaHorariaFuncionActual = null; 
		vfCargaHorariaActiva = false;
		vfLinkReporte = null;
		vfPeriodoAcademico = verificarPeriodoActivo();
		vfListaHorasProyecto = null;

		Date date = new Date();
		vfFechaInicio = date;
		vfFechaFin = date;
	}

	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		vfCedulaDocenteBusquedaAvanzada = null;
		vfApellidoDocenteBusquedaAvanzada = null;
		vfListDocentesBusquedaAvanzada = null;
		vfMensajeBusquedaAvanzada = null;
		vfLinkReporte = null;
		vfListaHorasProyecto = null;

	}

	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {

		//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
		try {
			limpiarInfoDocente();
			if (vfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
				if (vfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
					vfPeriodoAcademico = servVfPeriodoAcademicoServicio.buscarPorId(vfPeriodoAcademicoBuscar);
					vfDocente = servVfPersonaDatosServicioJdbc.buscarPorId(vfDocenteBuscar , vfPeriodoAcademicoBuscar); 
					vfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+vfDocente.getPrsIdentificacion()+"&prd="+vfPeriodoAcademico.getPracDescripcion();
					listarFunciones();
				} else {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.docente.validacion.exception")));
				}
			} else { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.facultad.validacion.exception")));
			}

		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.persona.datos.dto.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.persona.datos.dto.exception")));
		} catch (PeriodoAcademicoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.periodo.academico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.periodo.academico.exception")));
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
			vfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			vfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			vfListaCarreras = null;
			vfListaDocentes = null;

			vfDocente = servVfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , vfPeriodoAcademicoBuscar);
			listarFunciones();
			limpiarBusquedaAvanzada();
			vfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+vfPeriodoAcademico.getPracDescripcion();

		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.asignar.docente.exception")));
		}

	}

	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
		//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
		try {

			if(vfCedulaDocenteBusquedaAvanzada == null){
				vfCedulaDocenteBusquedaAvanzada = "";
			}
			if(vfApellidoDocenteBusquedaAvanzada == null){
				vfApellidoDocenteBusquedaAvanzada = "";
			}
			vfListDocentesBusquedaAvanzada = servVfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(vfCedulaDocenteBusquedaAvanzada, vfApellidoDocenteBusquedaAvanzada);

		} catch (PersonaDatosDtoException e) {
			vfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.docente.exception"));
		} catch (PersonaDatosDtoNoEncontradoException e) {
			vfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.buscar.docente.no.encontrado.exception"));
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
					if(vfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
						cargarCargaHoraria(vfDocente, 
								vfHorasSemanales, 
								vfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								vfPeriodoAcademico, 
								vfTipoFuncionCargaHorariaSeleccion);
					}
					limpiar();			
				}
			}else{
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.guardar.docente.validacion")));
			}
		}
	}

	/**
	 * Valida campos al guardar o gaurdar cambios
	 **/
	public Boolean verificarGuardar(){

		Boolean verificar = false; 
		if(vfNombreProyecto != null && vfNombreProyecto.replaceAll(" ", "").length()!=0){

			if(vfFechaInicio.before(vfFechaFin)){
				if(vfHorasSemanales != null){ 
					verificar = true; 
				}else {
					FacesUtil.mensajeInfo("Debe seleccionar una Función para continuar.");
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.verificar.guardar.fechas.incorrectas.validacion")));
			}
		}else{
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.verificar.guardar.nombre.proyecto.validacion")));
		}
		return verificar;
	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {

		Boolean verificar = true;
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				if(verificarDocente()){
					if(verificarGuardar()){

						if (vfFuncionSeleccion != null && vfFuncionSeleccion.intValue() != GeneralesConstantes.APP_ID_BASE) {
							// desactivo todo lo que tenga de gestion
							if (vfListaFunciones != null && vfListaFunciones.size() > 0) {
								for (TipoFuncionCargaHoraria item : vfListaFunciones) {
									try {
										List<CargaHoraria> listCargaHoraria =  servVfCargaHorariaServicio.buscarPorDetallePuesto(vfDocente.getDtpsId(), item.getTifncrhrId(), vfPeriodoAcademico.getPracId());
										if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
											for (CargaHoraria itemCaHr : listCargaHoraria) {
												CargaHoraria cargaHoraria = itemCaHr;
												cargaHoraria.setCrhrEstado(TipoCargaHorariaConstantes.ESTADO_INACTIVO_VALUE);
												cargaHoraria.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
												actualizarCargaHoraria(cargaHoraria);	
											}
										}

									} catch (CargaHorariaNoEncontradoException e) {
									} catch (CargaHorariaException e) {
									}	
								}
							}

							cargarCargaHoraria(vfDocente, 
									vfHorasSemanales, 
									vfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									vfPeriodoAcademico, 
									vfTipoFuncionCargaHorariaSeleccion);

							
						}else{
							FacesUtil.mensajeInfo("Seleccione una Función para continuar con la actualización.");
						}
						
						
						
						
						
						
						
						
//						//Tiempo de investigacion 
//						if(vfHorasSemanalesXTmdd != null){
//							if(vfCargaHorariaInvestigadorActual != null){
//								verificar = false;
//								vfCargaHorariaInvestigadorActual.setCrhrTipoFuncionCargaHoraria(vfTipoFuncionCargaHorariaXTmdd);
//								vfCargaHorariaInvestigadorActual.setCrhrObservacion(vfTipoFuncionCargaHorariaXTmdd.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
//								vfCargaHorariaInvestigadorActual.setCrhrNumHoras(vfHorasSemanalesXTmdd);
//
//								if(actualizarCargaHoraria(vfCargaHorariaInvestigadorActual)){
//									vfDetalleCargaHorariaInvestigadorActual.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(vfNombreProyecto).toUpperCase());
//									vfDetalleCargaHorariaInvestigadorActual.setDtcrhrFechaInicio(vfFechaInicio);
//									vfDetalleCargaHorariaInvestigadorActual.setDtcrhrFechaFin(vfFechaFin);
//									vfDetalleCargaHorariaInvestigadorActual.setDtcrhrNumHoras(vfHorasSemanalesXTmdd);
//
//									if(actualizarDetalleCargaHoraria(vfDetalleCargaHorariaInvestigadorActual)){
//										verificar = true;
//									}
//								} 
//							}
//						} 

//						if(verificar){
//							//Actualizar funcion doctorado
//							if(vfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){ 
//
//								if(vfCargaHorariaFuncionActual != null){
//									if(vfHorasSemanales != null){
//										verificar = false;
//										vfCargaHorariaFuncionActual.setCrhrTipoFuncionCargaHoraria(vfTipoFuncionCargaHorariaSeleccion);
//										vfCargaHorariaFuncionActual.setCrhrObservacion(vfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
//										vfCargaHorariaFuncionActual.setCrhrNumHoras(vfHorasSemanales);
//
//										if(actualizarCargaHoraria(vfCargaHorariaFuncionActual)){
//											vfDetalleCargaHorariaFuncionActual.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(vfNombreProyecto).toUpperCase());
//											vfDetalleCargaHorariaFuncionActual.setDtcrhrFechaInicio(vfFechaInicio);
//											vfDetalleCargaHorariaFuncionActual.setDtcrhrFechaFin(vfFechaFin);
//											vfDetalleCargaHorariaFuncionActual.setDtcrhrNumHoras(vfHorasSemanalesFuncion);
//
//											if(actualizarDetalleCargaHoraria(vfDetalleCargaHorariaFuncionActual)){
//												verificar = true;
//											}
//										}
//									}
//									//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
//								}else{
//
//									if(cargarCargaHoraria(vfDocente, 
//											vfHorasSemanalesFuncion, 
//											vfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
//											vfPeriodoAcademico, 
//											vfTipoFuncionCargaHorariaSeleccion) != null){
//										verificar = true;
//									}
//
//								}  
//								//Si al actualizar se ha descartado el parametro ingresado inicialmente se elimina 
//							}else{
//								if(vfCargaHorariaFuncionActual != null){
//									verificar = false;
//									vfCargaHorariaFuncionActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
//									if(actualizarCargaHoraria(vfCargaHorariaFuncionActual)){
//										vfDetalleCargaHorariaFuncionActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
//										if(actualizarDetalleCargaHoraria(vfDetalleCargaHorariaFuncionActual)){
//											verificar = true;
//										}
//									} 
//								}
//							}
//						}


						//Verificacion de actualizacion
						if(verificar){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.guardar.cambios.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.guardar.cambios.exception")));
						}	
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.guardar.cambios.docente.validacion.exception")));
				} 
		}
	}

	/**
	 * Elimina el registro
	 **/
	public void eliminar() {

		Boolean verificar = false;
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				//Eliminar Gestion academica
					if(vfCargaHorariaFuncionActual != null){
						vfCargaHorariaFuncionActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);

						if(actualizarCargaHoraria(vfCargaHorariaFuncionActual)){
							vfDetalleCargaHorariaFuncionActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
							if(actualizarDetalleCargaHoraria(vfDetalleCargaHorariaFuncionActual)){
								verificar = true;
							}
						} 
					}
				//Eliminar Redes academicas
//				if(vfHorasSemanalesXTmdd != null){
//					verificar = false;
//					vfCargaHorariaInvestigadorActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
//
//					if(actualizarCargaHoraria(vfCargaHorariaInvestigadorActual)){
//						vfDetalleCargaHorariaInvestigadorActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
//
//						if(actualizarDetalleCargaHoraria(vfDetalleCargaHorariaInvestigadorActual)){
//							verificar = true;
//						}
//					} 
//				} 

				//Verificacion de Eliminacion
				if(verificar){
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.eliminar.con.exito.validacion")));
					limpiar();
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.eliminar.exception")));
				}  
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.eliminar.docente.validacion.exception")));
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

			crhrResult = servVfCargaHorariaServicio.anadir(crhr);

			DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();

			dtcrhr.setDtcrhrCargaHoraria(crhrResult);
			dtcrhr.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(vfNombreProyecto).toUpperCase());
			dtcrhr.setDtcrhrFechaInicio(vfFechaInicio);
			dtcrhr.setDtcrhrFechaFin(vfFechaFin);
			dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
			dtcrhr.setDtcrhrNumHoras(horasSemanales);

			servVfDetalleCargaHorariaServicio.anadir(dtcrhr);

			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.cargar.carga.horaria.con.exito.validacion", observacion)));

		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.cargar.carga.horaria.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.cargar.carga.horaria.detalle.carga.horaria.exception")));
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.cargar.carga.horaria.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.cargar.carga.horaria.carga.horaria.exception")));
		}

		return crhrResult;
	}

	/**
	 * Carga los campos de carga horaria del dcente incluida una funcion
	 */
	public void listarCargaHorariaDocenteConFuncion(){ 
		Boolean verificar = false;

		try { 

			if(vfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: vfListaFunciones){
					vfListaCargasHorarias =  servVfCargaHorariaServicio.buscarPorDetallePuesto(vfDocente.getDtpsId(), itemFuncion.getTifncrhrId(), vfPeriodoAcademico.getPracId());
					if(vfListaCargasHorarias.size() > 0 ){
						for(CargaHoraria item: vfListaCargasHorarias ){ 
							vfCargaHorariaFuncionActual = item;
							vfListaDetalleCargaHoraria = servVfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
							if(vfListaDetalleCargaHoraria.size() > 0){
								for(DetalleCargaHoraria itemDetalle: vfListaDetalleCargaHoraria){
									vfDetalleCargaHorariaFuncionActual = itemDetalle;
									if(item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId() != 0){
										vfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
									}
									vfNombreProyecto = itemDetalle.getDtcrhrNombreProyecto();
									vfFechaInicio = itemDetalle.getDtcrhrFechaInicio();
									vfFechaFin = itemDetalle.getDtcrhrFechaFin();
									vfHorasSemanales = item.getCrhrNumHoras();
									verificar = true;
									vfCargaHorariaActiva = true;
									calcularHoras();
								}
							}	
						} 
					}
				}
			}

			if(!verificar){
				listarCargaHorariaDocenteSinFuncion();
			}

		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.con.funcion.carga.horaria.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.con.funcion.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.con.funcion.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.con.funcion.detalle.carga.horaria.exception")));
		}
	}

	/**
	 * Carga los campos de carga horaria del docente incluida una funcion
	 */
	public void listarCargaHorariaDocenteSinFuncion(){ 
		try { 

			vfListaCargasHorarias =  servVfCargaHorariaServicio.buscarPorDetallePuesto(vfDocente.getDtpsId(), vfFuncionXTmdd, vfPeriodoAcademico.getPracId());
			if(vfListaCargasHorarias.size() > 0 ){
				for(CargaHoraria item: vfListaCargasHorarias ){ 
//					vfCargaHorariaInvestigadorActual = item;
					vfListaDetalleCargaHoraria = servVfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
					if(vfListaDetalleCargaHoraria.size() > 0){
						for(DetalleCargaHoraria itemDetalle: vfListaDetalleCargaHoraria){
//							vfDetalleCargaHorariaInvestigadorActual = itemDetalle;
							vfNombreProyecto = itemDetalle.getDtcrhrNombreProyecto();
							vfFechaInicio = itemDetalle.getDtcrhrFechaInicio();
							vfFechaFin = itemDetalle.getDtcrhrFechaFin();
							vfHorasSemanales = itemDetalle.getDtcrhrNumHoras();
							calcularHoras();
							vfCargaHorariaActiva = true;
						}
					}	
				} 
			}
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.sin.funcion.carga.horaria.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.sin.funcion.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.sin.funcion.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carga.horaria.docente.sin.funcion.detalle.carga.horaria.exception")));
		}
	}
	
	/**
	 * Carga los campos de carga horaria del dcente
	 * @throws DetalleCargaHorariaException 
	 * @throws DetalleCargaHorariaNoEncontradoException 
	 */
	public void listarCargaHorariaDocente(){ 
		try { 
			if(vfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: vfListaFunciones){
					vfListaCargasHorarias =  servVfCargaHorariaServicio.buscarPorDetallePuesto(vfDocente.getDtpsId(),itemFuncion.getTifncrhrId(), vfPeriodoAcademico.getPracId());
					if(vfListaCargasHorarias.size() > 0 ){
						vfCargaHorariaActiva = Boolean.TRUE;
						for(CargaHoraria item: vfListaCargasHorarias ){
							vfCargaHorariaFuncionActual = item;
							vfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
							vfHorasSemanales = item.getCrhrNumHoras();
							calcularHoras();

							try {
								vfListaDetalleCargaHoraria = servVfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId());
								if(vfListaDetalleCargaHoraria.size() > 0){
									for(DetalleCargaHoraria itemDetalle: vfListaDetalleCargaHoraria){
										vfDetalleCargaHorariaFuncionActual = itemDetalle;
										vfNombreProyecto = itemDetalle.getDtcrhrNombreProyecto();
										vfFechaInicio = itemDetalle.getDtcrhrFechaInicio();
										vfFechaFin = itemDetalle.getDtcrhrFechaFin();
									}
								}
							} catch (DetalleCargaHorariaNoEncontradoException e) {
							} catch (DetalleCargaHorariaException e) {
							} 

						}
					}
				}
			}	

		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.carga.horaria.docente.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.carga.horaria.docente.exception")));
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
			verificar = servVfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.actualizar.carga.horaria.exception")));
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
			verificar = servVfDetalleCargaHorariaServicio.editar(entidad);
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.actualizar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.actualizar.detalle.carga.horaria.exception")));
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
		vfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		vfListaDocentes = null;
		vfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		vfListaCarreras = null; 
	}

	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {

		try {	
			limpiarInfoDocente();
			if(vfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				vfListaCarreras = servVfCarreraServicio.listarCarrerasXFacultad(vfFacultadBuscar);
				vfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				vfListaDocentes = null;
				listarDocentesPorFacultad();
				vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}else{
				vfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				vfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			vfListaDocentes = null;
			vfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			vfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			vfListaDocentes = servVfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(vfFacultadBuscar, vfPeriodoAcademicoBuscar);
			vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			vfListaDocentes = servVfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(vfCarreraBuscar, vfPeriodoAcademicoBuscar);
			vfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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

			if(vfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE || vfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
				switch (vfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
					verificarFuncion = true;
					funciones = servVfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_VINCULACION_CON_SOCIEDAD_VALUE);
					if (funciones != null && funciones.size() >0) {
						Iterator iter = funciones.iterator();
						while(iter.hasNext()){
							TipoFuncionCargaHoraria cad = (TipoFuncionCargaHoraria) iter.next();
							if(!cad.getTifncrhrFuncionCargaHoraria().getFncrhrTiempoDedicacion().equals(TiempoDedicacionConstantes.TMDD_CONTRATO_TIEMPO_COMPLETO_VALUE)){
								iter.remove();
							}
						}
					}
					break;
				case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
					verificarFuncion = true;
					funciones = servVfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_VINCULACION_CON_SOCIEDAD_VALUE);
					if (funciones != null && funciones.size() >0) {
						Iterator iter = funciones.iterator();
						while(iter.hasNext()){
							TipoFuncionCargaHoraria cad = (TipoFuncionCargaHoraria) iter.next();
							if(!cad.getTifncrhrFuncionCargaHoraria().getFncrhrTiempoDedicacion().equals(TiempoDedicacionConstantes.TMDD_CONTRATO_MEDIO_TIEMPO_VALUE)){
								iter.remove();
							}
						}
					}
					break;
				default:
					verificarFuncion = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.funciones.relacion.laboral.validacion.exception")));
					break;
				}

				vfListaFunciones = funciones;

			}else{
				switch (vfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
					verificarFuncion = true;
					funciones = servVfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_VINCULACION_CON_SOCIEDAD_VALUE);
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
					funciones = servVfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_VINCULACION_CON_SOCIEDAD_VALUE);
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
				default:
					verificarFuncion = false;
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.funciones.validar")));
					break;
				}

				vfListaFunciones = funciones;
			}

			vfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
//			calcularHoras();

			if(verificarFuncion){
				listarCargaHorariaDocente();
			}  
			
//			listarCargaHorariaDocenteSinFuncion();  
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.funciones.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.funciones.exception")));
		}
	}

	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------

	/**
	 * Calcula las horas para vinculacion en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){

		try {

			if (vfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
				vfTipoFuncionCargaHorariaSeleccion = servVfTipoFuncionCargaHorariaServicio.buscarPorId(vfFuncionSeleccion);

				vfListaHorasProyecto = new ArrayList<>();
				for(int i = vfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= vfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
					vfListaHorasProyecto.add(i); 
				}

			}else{
				vfHorasSemanales = null;
			}


		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.calcular.horas.exception")));
		}
	}

	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(vfDocente!=null){
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
			return servVfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.verificar.periodo.activo.validacion.exception")));
			return null;
		} catch (PeriodoAcademicoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.verificar.periodo.activo.exception")));
			return null;
		}
	}

	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){

		try {
			return servVfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
			return servVfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.verificar.cronograma.exception")));
			return null;
		}
	}

	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){

		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		vfPlanificacionCronograma = verificarCronograma();
		if(vfPlanificacionCronograma != null){
			if(vfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(vfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}

	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){

		vfListaPeriodosAcademicos = new ArrayList<>();

		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			vfListaPeriodosAcademicos.add(pracActivo);
			vfPeriodoAcademico = pracActivo;
			vfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}

		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			vfListaPeriodosAcademicos.add(pracCierre);
		}
	}

	public void generarReporteCargaHorariaDocente(){
		if (vfDocente != null) {
			cargarAsignacionesCargaHoraria(vfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(vfPeriodoAcademico.getPracId(),vfPeriodoAcademico.getPracDescripcion()), vfUsuario, vfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}

	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public List<Dependencia> getVfListaFacultades() {
		return vfListaFacultades;
	}

	public void setVfListaFacultades(List<Dependencia> vfListaFacultades) {
		this.vfListaFacultades = vfListaFacultades;
	}

	public List<Carrera> getVfListaCarreras() {
		return vfListaCarreras;
	}

	public void setVfListaCarreras(List<Carrera> vfListaCarreras) {
		this.vfListaCarreras = vfListaCarreras;
	}

	public List<PersonaDatosDto> getVfListaDocentes() {
		return vfListaDocentes;
	}

	public void setVfListaDocentes(List<PersonaDatosDto> vfListaDocentes) {
		this.vfListaDocentes = vfListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getVfListaFunciones() {
		return vfListaFunciones;
	}

	public void setVfListaFunciones(List<TipoFuncionCargaHoraria> vfListaFunciones) {
		this.vfListaFunciones = vfListaFunciones;
	}

	public PersonaDatosDto getVfDocente() {
		return vfDocente;
	}

	public void setVfDocente(PersonaDatosDto vfDocente) {
		this.vfDocente = vfDocente;
	}

	public Integer getVfFacultadBuscar() {
		return vfFacultadBuscar;
	}

	public void setVfFacultadBuscar(Integer vfFacultadBuscar) {
		this.vfFacultadBuscar = vfFacultadBuscar;
	}

	public Integer getVfCarreraBuscar() {
		return vfCarreraBuscar;
	}

	public void setVfCarreraBuscar(Integer vfCarreraBuscar) {
		this.vfCarreraBuscar = vfCarreraBuscar;
	}

	public Integer getVfDocenteBuscar() {
		return vfDocenteBuscar;
	}

	public void setVfDocenteBuscar(Integer vfDocenteBuscar) {
		this.vfDocenteBuscar = vfDocenteBuscar;
	}

	public String getVfIdentificacionBuscar() {
		return vfIdentificacionBuscar;
	}

	public void setVfIdentificacionBuscar(String vfIdentificacionBuscar) {
		this.vfIdentificacionBuscar = vfIdentificacionBuscar;
	}

	public Integer getVfFuncionSeleccion() {
		return vfFuncionSeleccion;
	}

	public void setVfFuncionSeleccion(Integer vfFuncionSeleccion) {
		this.vfFuncionSeleccion = vfFuncionSeleccion;
	}

	public String getVfNombreProyecto() {
		return vfNombreProyecto;
	}

	public void setVfNombreProyecto(String vfNombreProyecto) {
		this.vfNombreProyecto = vfNombreProyecto;
	}

	public Date getVfFechaInicio() {
		return vfFechaInicio;
	}

	public void setVfFechaInicio(Date vfFechaInicio) {
		this.vfFechaInicio = vfFechaInicio;
	}

	public Date getVfFechaFin() {
		return vfFechaFin;
	}

	public void setVfFechaFin(Date vfFechaFin) {
		this.vfFechaFin = vfFechaFin;
	}

	public Integer getVfHorasSemanales() {
		return vfHorasSemanales;
	}

	public void setVfHorasSemanales(Integer vfHorasSemanales) {
		this.vfHorasSemanales = vfHorasSemanales;
	}

	public Boolean getVfCargaHorariaActiva() {
		return vfCargaHorariaActiva;
	}

	public void setVfCargaHorariaActiva(Boolean vfCargaHorariaActiva) {
		this.vfCargaHorariaActiva = vfCargaHorariaActiva;
	}

	public Integer getVfPeriodoAcademicoBuscar() {
		return vfPeriodoAcademicoBuscar;
	}

	public void setVfPeriodoAcademicoBuscar(Integer vfPeriodoAcademicoBuscar) {
		this.vfPeriodoAcademicoBuscar = vfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getVfListaPeriodosAcademicos() {
		return vfListaPeriodosAcademicos;
	}

	public void setVfListaPeriodosAcademicos(List<PeriodoAcademico> vfListaPeriodosAcademicos) {
		this.vfListaPeriodosAcademicos = vfListaPeriodosAcademicos;
	}

	public String getVfCedulaDocenteBusquedaAvanzada() {
		return vfCedulaDocenteBusquedaAvanzada;
	}

	public void setVfCedulaDocenteBusquedaAvanzada(String vfCedulaDocenteBusquedaAvanzada) {
		this.vfCedulaDocenteBusquedaAvanzada = vfCedulaDocenteBusquedaAvanzada;
	}

	public String getVfApellidoDocenteBusquedaAvanzada() {
		return vfApellidoDocenteBusquedaAvanzada;
	}

	public void setVfApellidoDocenteBusquedaAvanzada(String vfApellidoDocenteBusquedaAvanzada) {
		this.vfApellidoDocenteBusquedaAvanzada = vfApellidoDocenteBusquedaAvanzada;
	}

	public String getVfMensajeBusquedaAvanzada() {
		return vfMensajeBusquedaAvanzada;
	}

	public void setVfMensajeBusquedaAvanzada(String vfMensajeBusquedaAvanzada) {
		this.vfMensajeBusquedaAvanzada = vfMensajeBusquedaAvanzada;
	}

	public List<PersonaDatosDto> getVfListDocentesBusquedaAvanzada() {
		return vfListDocentesBusquedaAvanzada;
	}

	public void setVfListDocentesBusquedaAvanzada(List<PersonaDatosDto> vfListDocentesBusquedaAvanzada) {
		this.vfListDocentesBusquedaAvanzada = vfListDocentesBusquedaAvanzada;
	}

	public String getVfLinkReporte() {
		return vfLinkReporte;
	}

	public void setVfLinkReporte(String vfLinkReporte) {
		this.vfLinkReporte = vfLinkReporte;
	}

	public List<Integer> getVfListaHorasProyecto() {
		return vfListaHorasProyecto;
	}

	public void setVfListaHorasProyecto(List<Integer> vfListaHorasProyecto) {
		this.vfListaHorasProyecto = vfListaHorasProyecto;
	}

	public Usuario getVfUsuario() {
		return vfUsuario;
	}

	public void setVfUsuario(Usuario vfUsuario) {
		this.vfUsuario = vfUsuario;
	}

	


}