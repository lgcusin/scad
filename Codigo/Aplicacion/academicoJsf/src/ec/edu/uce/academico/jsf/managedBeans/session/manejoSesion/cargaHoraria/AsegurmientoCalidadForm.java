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
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Aseguramiento de la calidad. 
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
 * los atributos del formulario de Aseguramiento de la calidad.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "aseguramientoCalidadForm")
@SessionScoped
public class AsegurmientoCalidadForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario acfUsuario;
	private List<Dependencia> acfListaFacultades;
	private List<Carrera> acfListaCarreras;
	private List<PersonaDatosDto> acfListaDocentes;
	private List<TipoFuncionCargaHoraria> acfListaFunciones;

	private PersonaDatosDto acfDocente;

	private Integer acfFacultadBuscar;
	private Integer acfCarreraBuscar;
	private Integer acfDocenteBuscar;
	private String acfIdentificacionBuscar;

	private TipoFuncionCargaHoraria acfTipoFuncionCargaHorariaSeleccion;
	private Integer acfFuncionSeleccion;
	private Integer acfHorasSemanales;
	private Boolean acfEditarAsgCld;
	
	private PeriodoAcademico acfPeriodoAcademico;
	private Integer acfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> acfListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto acfPlanificacionCronograma;
	
	private List<CargaHoraria> acfListaCargasHorarias;
	
	private CargaHoraria acfCargaHorariaActual;
	
	private Boolean acfCargaHorariaActiva;
	  
	private String acfCedulaDocenteBusquedaAvanzada;
	private String acfApellidoDocenteBusquedaAvanzada;
	private String acfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> acfListDocentesBusquedaAvanzada;
	
	//--v3
	//URL reporte
	private String acfLinkReporte;
	
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
	DependenciaServicio servAcfDependenciaServicio;
	@EJB
	CarreraServicio servAcfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servAcfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servAcfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servAcfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servAcfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servAcfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	CargaHorariaServicio servAcfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servAcfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servAcfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servAcfUsuarioRolServicio;
	
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
			acfCargaHorariaActiva = false;
			rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				validarCronograma();
				cagarPeriodos();
				acfListaFacultades = servAcfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = acfListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			}
		} catch (DependenciaNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de aseguramiento de calidad.
	 */
	public String irFormularioAsegurmientoCalidad(Usuario usuario) {
		acfUsuario = usuario;
		inicarParametros();
		return "irFormularioAseguramientoCalidad";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		acfListaFacultades = null;
		acfListaCarreras = null;
		acfListaDocentes = null;
		acfListaFunciones = null;
		acfDocente = null;
		acfFacultadBuscar = null;
		acfCarreraBuscar = null;
		acfDocenteBuscar = null;
		acfIdentificacionBuscar = null;
		acfTipoFuncionCargaHorariaSeleccion = null;
		acfFuncionSeleccion = null;
		acfHorasSemanales = null;
		acfCargaHorariaActual = null;
		acfPeriodoAcademico = null;
		acfPlanificacionCronograma = null;
		acfPeriodoAcademicoBuscar = null;
		acfListaPeriodosAcademicos = null;
		acfLinkReporte = null;
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		acfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		acfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		acfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
		acfIdentificacionBuscar = null;
		acfHorasSemanales = null;
		acfListaFunciones = null;
		acfListaCarreras = null;
		acfListaFacultades = null;
		acfListaDocentes = null;
		acfTipoFuncionCargaHorariaSeleccion = null;
		acfDocente = null;
		acfCargaHorariaActiva = false;
		acfCargaHorariaActual = null;
		acfPeriodoAcademico = verificarPeriodoActivo();
		acfLinkReporte = null;
		limpiarBusquedaAvanzada();
		inicarParametros();
	}
	
	/**
	 * Limpia la seleccion de campos en base al docente
	 */
	public void limpiarInfoDocente(){

		acfListaFunciones = null;
		acfFuncionSeleccion = null;
		acfTipoFuncionCargaHorariaSeleccion = null;
		acfDocente = null;
		acfHorasSemanales = null;
		acfCargaHorariaActiva=false;
		acfCargaHorariaActual = null;
		acfPeriodoAcademico = verificarPeriodoActivo();
		acfLinkReporte = null;
		limpiarBusquedaAvanzada();
	}
	
	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		acfCedulaDocenteBusquedaAvanzada = null;
		acfApellidoDocenteBusquedaAvanzada = null;
		acfListDocentesBusquedaAvanzada = null;
		acfMensajeBusquedaAvanzada = null;
		acfLinkReporte = null;
	}
	
	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			acfCargaHorariaActiva=true;
			try {
				limpiarInfoDocente();
				if (acfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
					if (acfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
						acfPeriodoAcademico = servAcfPeriodoAcademicoServicio.buscarPorId(acfPeriodoAcademicoBuscar);
						acfDocente = servAcfPersonaDatosServicioJdbc.buscarPorId(acfDocenteBuscar, acfPeriodoAcademicoBuscar);
						acfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+acfDocente.getPrsIdentificacion()+"&prd="+acfPeriodoAcademico.getPracDescripcion();
//						acfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHorariaDocInd.aspx?idn="+acfDocente.getPrsIdentificacion()+"&prd="+acfPeriodoAcademico.getPracDescripcion();
						listarFunciones();
					} else { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.docente.validacion.exception")));
					}
				} else { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.facultad.validacion.exception")));
				}
	
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.persona.datos.dto.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.persona.datos.dto.exception")));
			} catch (PeriodoAcademicoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.periodo.academico.no.encontrado.exception")));
			} catch (PeriodoAcademicoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.periodo.academico.exception")));
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
			acfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			acfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			acfListaCarreras = null;
			acfListaDocentes = null;
			
			acfDocente = servAcfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId(), acfPeriodoAcademicoBuscar);				
			listarFunciones();
			limpiarBusquedaAvanzada();
			acfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+acfDocente.getPrsIdentificacion()+"&prd="+acfPeriodoAcademico.getPracDescripcion();
			
			
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.asignar.docente.exception")));
		}
		
	}
	
	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
		
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			try {
				
				if(acfCedulaDocenteBusquedaAvanzada == null){
					acfCedulaDocenteBusquedaAvanzada = "";
				}
				if(acfApellidoDocenteBusquedaAvanzada == null){
					acfApellidoDocenteBusquedaAvanzada = "";
				}
				acfListDocentesBusquedaAvanzada = servAcfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(acfCedulaDocenteBusquedaAvanzada, acfApellidoDocenteBusquedaAvanzada);
				
			} catch (PersonaDatosDtoException e) {
				acfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.docente.exception"));
			} catch (PersonaDatosDtoNoEncontradoException e) {
				acfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.buscar.docente.no.encontrado.exception"));
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
				if(acfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(acfHorasSemanales != null){
						cargarCargaHoraria(acfDocente, 
								acfHorasSemanales, 
								acfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								acfPeriodoAcademico, 
								acfTipoFuncionCargaHorariaSeleccion); 
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.guardar.funcion.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.guardar.docente.validacion.exception")));
			}
		}
	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(acfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(acfHorasSemanales != null){
						
						acfCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(acfTipoFuncionCargaHorariaSeleccion);
						acfCargaHorariaActual.setCrhrObservacion(acfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
						acfCargaHorariaActual.setCrhrNumHoras(acfHorasSemanales);
						 
						if(actualizarCargaHoraria(acfCargaHorariaActual)){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.guardar.cambios.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.guardar.cambios.exception")));
						}
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.guardar.cambios.docente.validacion.exception")));
			} 
		}
	}

	/**
	 * Elimina el registro
	 **/
	public void eliminar() { 
		
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(acfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(acfHorasSemanales != null){
						 
						acfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(acfCargaHorariaActual)){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.eliminar.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.eliminar.exception")));
						}
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.eliminar.docente.validacion.exception")));
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
			
			crhr = servAcfCargaHorariaServicio.anadir(crhr);
			
			verificar = true;
			 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.cargar.carga.horaria.con.exito.validacion", observacion)));
			limpiar();
			
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.cargar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.cargar.carga.horaria.exception")));
		}
		
		return verificar;
	}
	
	/**
	 * Carga los campos de carga horaria del dcente
	 */
	public void listarCargaHorariaDocente(){ 
		
		Boolean verificar = false;
		try { 
			if(acfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: acfListaFunciones){
					acfListaCargasHorarias =  servAcfCargaHorariaServicio.buscarPorDetallePuesto(acfDocente.getDtpsId(),itemFuncion.getTifncrhrId(), acfPeriodoAcademico.getPracId());
					if(acfListaCargasHorarias.size() > 0 ){
						
						for(CargaHoraria item: acfListaCargasHorarias ){ 
							acfCargaHorariaActual = item;
							acfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
							acfHorasSemanales = item.getCrhrNumHoras();
							verificar = true;
							calcularHoras();
						}
					}
				}
			}
			acfCargaHorariaActiva = verificar;

		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.carga.horaria.docente.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.carga.horaria.docente.exception")));
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
				verificar = servAcfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.actualizar.carga.horaria.exception")));
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
		acfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		acfListaDocentes = null;
		acfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		acfListaCarreras = null;
		
	}
	
	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {
		
		try { 
			limpiarInfoDocente(); 
			if(acfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				acfListaCarreras = servAcfCarreraServicio.listarCarrerasXFacultad(acfFacultadBuscar);
				acfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				acfListaDocentes = null;
				listarDocentesPorFacultad();
				acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}else{
				acfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				acfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}  
		} catch (CarreraNoEncontradoException e) { 
			acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			acfListaDocentes = null;
			acfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			acfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			acfListaDocentes = servAcfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(acfFacultadBuscar, acfPeriodoAcademicoBuscar);
			acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			acfListaDocentes = servAcfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(acfCarreraBuscar, acfPeriodoAcademicoBuscar);
			acfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
				
				
				
				if(acfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
						|| acfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
					acfEditarAsgCld = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.relacion.laboral.validacion.exception")));
				}else{
					switch (acfDocente.getTmddId()) {
					case 3 ://Tiempo Completo
						acfEditarAsgCld = true;
						break;
					default:
						acfEditarAsgCld = false;
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.validacion.exception")));
						break;
					}
				}
				
				if(acfEditarAsgCld){
					acfListaFunciones = servAcfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_ASEGURAMIENTO_CALIDAD_VALUE);
					listarCargaHorariaDocente();
				}else{
					acfListaFunciones = null;
					acfHorasSemanales = null;
					acfIdentificacionBuscar = null;
					acfFuncionSeleccion = null;
				}
				 
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.exception")));
			}
			
	}
	 
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	/**
	 * Calcula las horas en aseguramiento de calidad en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){
		try {
			if (acfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
				acfTipoFuncionCargaHorariaSeleccion = servAcfTipoFuncionCargaHorariaServicio.buscarPorId(acfFuncionSeleccion);
				acfHorasSemanales = acfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
			}else{
				acfHorasSemanales = null;
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.calcular.horas.exception")));
		}
	}
	
	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(acfDocente!=null){
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
			return servAcfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.verificar.periodo.activo.validacion.exception")));
			return null;
		} catch (PeriodoAcademicoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.verificar.periodo.activo.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){
		
		try {
			return servAcfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
			return servAcfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.verificar.cronograma.exception")));
			return null;
		}
	}
	
	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		acfPlanificacionCronograma = verificarCronograma();
		if(acfPlanificacionCronograma != null){
			if(acfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(acfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}
	
	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		
		acfListaPeriodosAcademicos = new ArrayList<>();
		
		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			acfListaPeriodosAcademicos.add(pracActivo);
			acfPeriodoAcademico = pracActivo;
			acfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}
		
		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			acfListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	public void generarReporteCargaHorariaDocente(){
		if (acfDocente != null) {
			cargarAsignacionesCargaHoraria(acfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(acfPeriodoAcademico.getPracId(), acfPeriodoAcademico.getPracDescripcion()), acfUsuario, acfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	

	public Integer getAcfFacultadBuscar() {
		return acfFacultadBuscar;
	}

	public List<Dependencia> getAcfListaFacultades() {
		return acfListaFacultades;
	}

	public void setAcfListaFacultades(List<Dependencia> acfListaFacultades) {
		this.acfListaFacultades = acfListaFacultades;
	}

	public List<Carrera> getAcfListaCarreras() {
		return acfListaCarreras;
	}

	public void setAcfListaCarreras(List<Carrera> acfListaCarreras) {
		this.acfListaCarreras = acfListaCarreras;
	}

	public List<PersonaDatosDto> getAcfListaDocentes() {
		return acfListaDocentes;
	}

	public void setAcfListaDocentes(List<PersonaDatosDto> acfListaDocentes) {
		this.acfListaDocentes = acfListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getAcfListaFunciones() {
		return acfListaFunciones;
	}

	public void setAcfListaFunciones(List<TipoFuncionCargaHoraria> acfListaFunciones) {
		this.acfListaFunciones = acfListaFunciones;
	}

	public PersonaDatosDto getAcfDocente() {
		return acfDocente;
	}

	public void setAcfDocente(PersonaDatosDto acfDocente) {
		this.acfDocente = acfDocente;
	}

	public void setAcfFacultadBuscar(Integer acfFacultadBuscar) {
		this.acfFacultadBuscar = acfFacultadBuscar;
	}

	public Integer getAcfCarreraBuscar() {
		return acfCarreraBuscar;
	}

	public void setAcfCarreraBuscar(Integer acfCarreraBuscar) {
		this.acfCarreraBuscar = acfCarreraBuscar;
	}

	public Integer getAcfDocenteBuscar() {
		return acfDocenteBuscar;
	}

	public void setAcfDocenteBuscar(Integer acfDocenteBuscar) {
		this.acfDocenteBuscar = acfDocenteBuscar;
	}

	public String getAcfIdentificacionBuscar() {
		return acfIdentificacionBuscar;
	}

	public void setAcfIdentificacionBuscar(String acfIdentificacionBuscar) {
		this.acfIdentificacionBuscar = acfIdentificacionBuscar;
	}

	public Integer getAcfFuncionSeleccion() {
		return acfFuncionSeleccion;
	}

	public void setAcfFuncionSeleccion(Integer acfFuncionSeleccion) {
		this.acfFuncionSeleccion = acfFuncionSeleccion;
	}

	public Integer getAcfHorasSemanales() {
		return acfHorasSemanales;
	}

	public void setAcfHorasSemanales(Integer acfHorasSemanales) {
		this.acfHorasSemanales = acfHorasSemanales;
	}

	public Boolean getAcfCargaHorariaActiva() {
		return acfCargaHorariaActiva;
	}

	public void setAcfCargaHorariaActiva(Boolean acfCargaHorariaActiva) {
		this.acfCargaHorariaActiva = acfCargaHorariaActiva;
	}

	public Integer getAcfPeriodoAcademicoBuscar() {
		return acfPeriodoAcademicoBuscar;
	}

	public void setAcfPeriodoAcademicoBuscar(Integer acfPeriodoAcademicoBuscar) {
		this.acfPeriodoAcademicoBuscar = acfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getAcfListaPeriodosAcademicos() {
		return acfListaPeriodosAcademicos;
	}

	public void setAcfListaPeriodosAcademicos(List<PeriodoAcademico> acfListaPeriodosAcademicos) {
		this.acfListaPeriodosAcademicos = acfListaPeriodosAcademicos;
	}
  
	public String getAcfCedulaDocenteBusquedaAvanzada() {
		return acfCedulaDocenteBusquedaAvanzada;
	}

	public void setAcfCedulaDocenteBusquedaAvanzada(String acfCedulaDocenteBusquedaAvanzada) {
		this.acfCedulaDocenteBusquedaAvanzada = acfCedulaDocenteBusquedaAvanzada;
	}

	public String getAcfApellidoDocenteBusquedaAvanzada() {
		return acfApellidoDocenteBusquedaAvanzada;
	}

	public void setAcfApellidoDocenteBusquedaAvanzada(String acfApellidoDocenteBusquedaAvanzada) {
		this.acfApellidoDocenteBusquedaAvanzada = acfApellidoDocenteBusquedaAvanzada;
	}

	public String getAcfMensajeBusquedaAvanzada() {
		return acfMensajeBusquedaAvanzada;
	}

	public void setAcfMensajeBusquedaAvanzada(String acfMensajeBusquedaAvanzada) {
		this.acfMensajeBusquedaAvanzada = acfMensajeBusquedaAvanzada;
	}

	public List<PersonaDatosDto> getAcfListDocentesBusquedaAvanzada() {
		return acfListDocentesBusquedaAvanzada;
	}

	public void setAcfListDocentesBusquedaAvanzada(List<PersonaDatosDto> acfListDocentesBusquedaAvanzada) {
		this.acfListDocentesBusquedaAvanzada = acfListDocentesBusquedaAvanzada;
	}

	public String getAcfLinkReporte() {
		return acfLinkReporte;
	}

	public void setAcfLinkReporte(String acfLinkReporte) {
		this.acfLinkReporte = acfLinkReporte;
	}

	public Usuario getAcfUsuario() {
		return acfUsuario;
	}

	public void setAcfUsuario(Usuario acfUsuario) {
		this.acfUsuario = acfUsuario;
	}

	
 
}