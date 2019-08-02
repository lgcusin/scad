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
   
 ARCHIVO:     AsegurmientoCalidadForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Postgrado de la calidad. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Arturo Villafuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFuncionCargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCargaHorariaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) AsegurmientoCalidadForm.java Bean de sesión que maneja
 * los atributos del formulario de Postgrado de la calidad.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "postgradoForm")
@SessionScoped
public class PostgradoForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private Usuario pfUsuario;

	private List<Dependencia> pfListaFacultades;
	private List<Carrera> pfListaCarreras;
	private List<PersonaDatosDto> pfListaDocentes;
	private List<TipoFuncionCargaHoraria> pfListaFunciones;

	private PersonaDatosDto pfDocente;

	private Integer pfFacultadBuscar;
	private Integer pfCarreraBuscar;
	private Integer pfDocenteBuscar;
	private String pfIdentificacionBuscar;

	private TipoFuncionCargaHoraria pfTipoFuncionCargaHorariaSeleccion;
	private Integer pfFuncionSeleccion;
	private Integer pfHorasSemanales;
	private Boolean pfEditarAsgCld;
	
	private PeriodoAcademico pfPeriodoAcademico;
	private Integer pfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> pfListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto pfPlanificacionCronograma;
	
	private List<CargaHoraria> pfListaCargasHorarias;
	
	private CargaHoraria pfCargaHorariaActual;
	
	private Boolean pfCargaHorariaActiva;
	 
	private String pfCedulaDocenteBusquedaAvanzada;
	private String pfApellidoDocenteBusquedaAvanzada;
	private String pfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> pfListDocentesBusquedaAvanzada;
	
	//--v2
	//URL reporte
	private String pfLinkReporte;
	
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
	DependenciaServicio servPfDependenciaServicio;
	@EJB
	CarreraServicio servPfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servPfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servPfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servPfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servPfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servPfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	CargaHorariaServicio servPfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servPfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servPfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servPfUsuarioRolServicio;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/**
	 * Inicia los parametros de la funcionalidad
	 */
	@SuppressWarnings("rawtypes")
	private void inicarParametros() {
		try { 
			pfCargaHorariaActiva = false;
			rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				validarCronograma();
				cagarPeriodos();
				pfListaFacultades = servPfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = pfListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			}
		} catch (DependenciaNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de postgrado de calidad.
	 */
	public String irFormularioPostgrado(Usuario usuario) {
		pfUsuario = usuario;
		inicarParametros();
		return "irFormularioPostgrado";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		pfListaFacultades = null;
		pfListaCarreras = null;
		pfListaDocentes = null;
		pfListaFunciones = null;
		pfDocente = null;
		pfFacultadBuscar = null;
		pfCarreraBuscar = null;
		pfDocenteBuscar = null;
		pfIdentificacionBuscar = null;
		pfTipoFuncionCargaHorariaSeleccion = null;
		pfFuncionSeleccion = null;
		pfHorasSemanales = null;
		pfCargaHorariaActual = null;
		pfPeriodoAcademico = null;
		pfPlanificacionCronograma = null;
		pfPeriodoAcademicoBuscar = null;
		pfListaPeriodosAcademicos = null;
		pfLinkReporte = null;
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		pfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		pfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		pfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
		pfIdentificacionBuscar = null;
		pfHorasSemanales = null;
		pfListaFunciones = null;
		pfListaCarreras = null;
		pfListaFacultades = null;
		pfListaDocentes = null;
		pfTipoFuncionCargaHorariaSeleccion = null;
		pfDocente = null;
		pfCargaHorariaActiva = false;
		pfCargaHorariaActual = null;
		pfLinkReporte = null;
		pfPeriodoAcademico = verificarPeriodoActivo();
		limpiarBusquedaAvanzada();
		inicarParametros();
	}
	
	/**
	 * Limpia la seleccion de campos en base al docente
	 */
	public void limpiarInfoDocente(){

		pfListaFunciones = null;
		pfFuncionSeleccion = null;
		pfTipoFuncionCargaHorariaSeleccion = null;
		pfDocente = null;
		pfHorasSemanales = null;
		pfCargaHorariaActiva=false;
		pfCargaHorariaActual = null;
		pfLinkReporte = null;
		pfPeriodoAcademico = verificarPeriodoActivo();
		limpiarBusquedaAvanzada();
	}
	
	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		pfCedulaDocenteBusquedaAvanzada = null;
		pfApellidoDocenteBusquedaAvanzada = null;
		pfListDocentesBusquedaAvanzada = null;
		pfMensajeBusquedaAvanzada = null;
		pfLinkReporte = null;
	}
	
	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			pfCargaHorariaActiva=true;
			try {
				limpiarInfoDocente();
				if (pfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
					if (pfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
						pfPeriodoAcademico = servPfPeriodoAcademicoServicio.buscarPorId(pfPeriodoAcademicoBuscar);
						pfDocente = servPfPersonaDatosServicioJdbc.buscarPorId(pfDocenteBuscar , pfPeriodoAcademicoBuscar);
						pfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+pfDocente.getPrsIdentificacion()+"&prd="+pfPeriodoAcademico.getPracDescripcion();
						listarFunciones();
					} else { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.docente.validacion.exception")));
					}
				} else { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.facultad.validacion.exception")));
				}
	
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.persona.datos.dto.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.persona.datos.dto.exception")));
			} catch (PeriodoAcademicoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.periodo.academico.no.encontrado.exception")));
			} catch (PeriodoAcademicoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.periodo.academico.exception")));
			}
//		}
	}

	/**
	 * Carga informacion del docente seleccionado en el dialogo de busqueda avanzada
	 * @param item Item recibido con la infromacion del docente seleccionado
	 */
	public void asignarDocente( PersonaDatosDto item){
		try {
			
			limpiarInfoDocente();
			pfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			pfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			pfListaCarreras = null;
			pfListaDocentes = null;
			
				pfDocente = servPfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , pfPeriodoAcademicoBuscar);
				listarFunciones();
				limpiarBusquedaAvanzada();
				pfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+pfPeriodoAcademico.getPracDescripcion();
				
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.asignar.docente.exception")));
		}
		
	}
	
	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			try {
				
				if(pfCedulaDocenteBusquedaAvanzada == null){
					pfCedulaDocenteBusquedaAvanzada = "";
				}
				if(pfApellidoDocenteBusquedaAvanzada == null){
					pfApellidoDocenteBusquedaAvanzada = "";
				}
				pfListDocentesBusquedaAvanzada = servPfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(pfCedulaDocenteBusquedaAvanzada, pfApellidoDocenteBusquedaAvanzada);
				
			} catch (PersonaDatosDtoException e) {
				pfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.docente.exception"));
			} catch (PersonaDatosDtoNoEncontradoException e) {
				pfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.buscar.docente.no.encontrado.exception"));
			}
//		}
		
	}
	
	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------

	/**
	 * Guarda los parametros del formulario
	 **/
	public void guardar() {
		
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(pfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(pfHorasSemanales != null){
						cargarCargaHoraria(pfDocente, 
								pfHorasSemanales, 
								pfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								pfPeriodoAcademico, 
								pfTipoFuncionCargaHorariaSeleccion); 
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.guardar.funcion.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.guardar.docente.validacion.exception")));
			}
		}
	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {
		
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(pfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(pfHorasSemanales != null){
						
						pfCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(pfTipoFuncionCargaHorariaSeleccion);
						pfCargaHorariaActual.setCrhrObservacion(pfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
						pfCargaHorariaActual.setCrhrNumHoras(pfHorasSemanales);
						 
						if(actualizarCargaHoraria(pfCargaHorariaActual)){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.guardar.cambios.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.guardar.cambios.exception")));
						}
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.guardar.cambios.docente.validacion.exception")));
			} 
		}
	}

	/**
	 * Elimina el registro
	 **/
	public void eliminar() { 
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(pfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(pfHorasSemanales != null){
						 
						pfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(pfCargaHorariaActual)){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.eliminar.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.eliminar.exception")));
						}
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.eliminar.docente.validacion.exception")));
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
			
			crhr = servPfCargaHorariaServicio.anadir(crhr);
			
			verificar = true;
			 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.cargar.carga.horaria.con.exito.validacion", observacion)));
			limpiar();
			
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.cargar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.cargar.carga.horaria.exception")));
		}
		
		return verificar;
	}
	
	/**
	 * Carga los campos de carga horaria del dcente
	 */
	public void listarCargaHorariaDocente(){ 
		
		Boolean verificar = false;
		try { 
			if(pfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: pfListaFunciones){
					pfListaCargasHorarias =  servPfCargaHorariaServicio.buscarPorDetallePuesto(pfDocente.getDtpsId(),itemFuncion.getTifncrhrId(), pfPeriodoAcademico.getPracId());
					if(pfListaCargasHorarias.size() > 0 ){
						
						for(CargaHoraria item: pfListaCargasHorarias ){ 
							pfCargaHorariaActual = item;
							pfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
							pfHorasSemanales = item.getCrhrNumHoras();
							verificar = true;
							calcularHoras();
						}
					}
				}
			}
			pfCargaHorariaActiva = verificar;

		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.carga.horaria.docente.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.carga.horaria.docente.exception")));
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
				verificar = servPfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.actualizar.carga.horaria.exception")));
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
		pfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		pfListaDocentes = null;
		pfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		pfListaCarreras = null;
		
	}
	
	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {
		
		try { 
			limpiarInfoDocente(); 
			if(pfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				pfListaCarreras = servPfCarreraServicio.listarCarrerasXFacultad(pfFacultadBuscar);
				pfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				pfListaDocentes = null;
				listarDocentesPorFacultad();
				pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}else{
				pfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				pfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}  
		} catch (CarreraNoEncontradoException e) { 
			pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			pfListaDocentes = null;
			pfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			pfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			pfListaDocentes = servPfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(pfFacultadBuscar, pfPeriodoAcademicoBuscar);
			pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			pfListaDocentes = servPfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(pfCarreraBuscar, pfPeriodoAcademicoBuscar);
			pfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
				
				
				 
				if(pfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
						|| pfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
					pfEditarAsgCld = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.funciones.relacion.laboral.validacion.exception")));
				}else{
					switch (pfDocente.getTmddId()) {
					case 3 ://Tiempo Completo
						pfEditarAsgCld = true;
						break;
					default:
						pfEditarAsgCld = false;
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.funciones.validacion.exception")));
						break;
					}
				}
				
				if(pfEditarAsgCld){
					pfListaFunciones = servPfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_POSTGRADO_VALUE);
					listarCargaHorariaDocente();
				}else{
					pfListaFunciones = null;
					pfHorasSemanales = null;
					pfIdentificacionBuscar = null;
					pfFuncionSeleccion = null;
				}
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.listar.funciones.exception")));
			}
			
	}
	 
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	/**
	 * Calcula las horas en postgrado de calidad en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){
		try {
			if (pfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
				pfTipoFuncionCargaHorariaSeleccion = servPfTipoFuncionCargaHorariaServicio.buscarPorId(pfFuncionSeleccion);
				pfHorasSemanales = pfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
			}else{
				pfHorasSemanales = null;
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.calcular.horas.exception")));
		}
	}
	
	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(pfDocente!=null){
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
			return servPfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.verificar.periodo.activo.validacion.exception")));
			return null;
		} catch (PeriodoAcademicoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.verificar.periodo.activo.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){
		
		try {
			return servPfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
			return servPfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.verificar.cronograma.exception")));
			return null;
		}
	}
	
	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		pfPlanificacionCronograma = verificarCronograma();
		if(pfPlanificacionCronograma != null){
			if(pfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(pfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postgrado.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}
	
	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		
		pfListaPeriodosAcademicos = new ArrayList<>();
		
		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			pfListaPeriodosAcademicos.add(pracActivo);
			pfPeriodoAcademico = pracActivo;
			pfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}
		
		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			pfListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	
	public void generarReporteCargaHorariaDocente(){
		if (pfDocente != null) {
			cargarAsignacionesCargaHoraria(pfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(pfPeriodoAcademico.getPracId(),pfPeriodoAcademico.getPracDescripcion()), pfUsuario, pfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}

	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	

	public Integer getPfFacultadBuscar() {
		return pfFacultadBuscar;
	}

	public List<Dependencia> getPfListaFacultades() {
		return pfListaFacultades;
	}

	public void setPfListaFacultades(List<Dependencia> pfListaFacultades) {
		this.pfListaFacultades = pfListaFacultades;
	}

	public List<Carrera> getPfListaCarreras() {
		return pfListaCarreras;
	}

	public void setPfListaCarreras(List<Carrera> pfListaCarreras) {
		this.pfListaCarreras = pfListaCarreras;
	}

	public List<PersonaDatosDto> getPfListaDocentes() {
		return pfListaDocentes;
	}

	public void setPfListaDocentes(List<PersonaDatosDto> pfListaDocentes) {
		this.pfListaDocentes = pfListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getPfListaFunciones() {
		return pfListaFunciones;
	}

	public void setPfListaFunciones(List<TipoFuncionCargaHoraria> pfListaFunciones) {
		this.pfListaFunciones = pfListaFunciones;
	}

	public PersonaDatosDto getPfDocente() {
		return pfDocente;
	}

	public void setPfDocente(PersonaDatosDto pfDocente) {
		this.pfDocente = pfDocente;
	}

	public void setPfFacultadBuscar(Integer pfFacultadBuscar) {
		this.pfFacultadBuscar = pfFacultadBuscar;
	}

	public Integer getPfCarreraBuscar() {
		return pfCarreraBuscar;
	}

	public void setPfCarreraBuscar(Integer pfCarreraBuscar) {
		this.pfCarreraBuscar = pfCarreraBuscar;
	}

	public Integer getPfDocenteBuscar() {
		return pfDocenteBuscar;
	}

	public void setPfDocenteBuscar(Integer pfDocenteBuscar) {
		this.pfDocenteBuscar = pfDocenteBuscar;
	}

	public String getPfIdentificacionBuscar() {
		return pfIdentificacionBuscar;
	}

	public void setPfIdentificacionBuscar(String pfIdentificacionBuscar) {
		this.pfIdentificacionBuscar = pfIdentificacionBuscar;
	}

	public Integer getPfFuncionSeleccion() {
		return pfFuncionSeleccion;
	}

	public void setPfFuncionSeleccion(Integer pfFuncionSeleccion) {
		this.pfFuncionSeleccion = pfFuncionSeleccion;
	}

	public Integer getPfHorasSemanales() {
		return pfHorasSemanales;
	}

	public void setPfHorasSemanales(Integer pfHorasSemanales) {
		this.pfHorasSemanales = pfHorasSemanales;
	}

	public Boolean getPfCargaHorariaActiva() {
		return pfCargaHorariaActiva;
	}

	public void setPfCargaHorariaActiva(Boolean pfCargaHorariaActiva) {
		this.pfCargaHorariaActiva = pfCargaHorariaActiva;
	}

	public Integer getPfPeriodoAcademicoBuscar() {
		return pfPeriodoAcademicoBuscar;
	}

	public void setPfPeriodoAcademicoBuscar(Integer pfPeriodoAcademicoBuscar) {
		this.pfPeriodoAcademicoBuscar = pfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getPfListaPeriodosAcademicos() {
		return pfListaPeriodosAcademicos;
	}

	public void setPfListaPeriodosAcademicos(List<PeriodoAcademico> pfListaPeriodosAcademicos) {
		this.pfListaPeriodosAcademicos = pfListaPeriodosAcademicos;
	}
  
	public String getPfCedulaDocenteBusquedaAvanzada() {
		return pfCedulaDocenteBusquedaAvanzada;
	}

	public void setPfCedulaDocenteBusquedaAvanzada(String pfCedulaDocenteBusquedaAvanzada) {
		this.pfCedulaDocenteBusquedaAvanzada = pfCedulaDocenteBusquedaAvanzada;
	}

	public String getPfApellidoDocenteBusquedaAvanzada() {
		return pfApellidoDocenteBusquedaAvanzada;
	}

	public void setPfApellidoDocenteBusquedaAvanzada(String pfApellidoDocenteBusquedaAvanzada) {
		this.pfApellidoDocenteBusquedaAvanzada = pfApellidoDocenteBusquedaAvanzada;
	}

	public String getPfMensajeBusquedaAvanzada() {
		return pfMensajeBusquedaAvanzada;
	}

	public void setPfMensajeBusquedaAvanzada(String pfMensajeBusquedaAvanzada) {
		this.pfMensajeBusquedaAvanzada = pfMensajeBusquedaAvanzada;
	}

	public List<PersonaDatosDto> getPfListDocentesBusquedaAvanzada() {
		return pfListDocentesBusquedaAvanzada;
	}

	public void setPfListDocentesBusquedaAvanzada(List<PersonaDatosDto> pfListDocentesBusquedaAvanzada) {
		this.pfListDocentesBusquedaAvanzada = pfListDocentesBusquedaAvanzada;
	}

	public String getPfLinkReporte() {
		return pfLinkReporte;
	}

	public void setPfLinkReporte(String pfLinkReporte) {
		this.pfLinkReporte = pfLinkReporte;
	}

	public Usuario getPfUsuario() {
		return pfUsuario;
	}

	public void setPfUsuario(Usuario pfUsuario) {
		this.pfUsuario = pfUsuario;
	}

	
 
}