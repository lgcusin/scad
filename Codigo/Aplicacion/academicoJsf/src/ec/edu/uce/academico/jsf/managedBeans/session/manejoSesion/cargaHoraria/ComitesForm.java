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

 ARCHIVO:     ComitesForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Comites. 
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
import ec.edu.uce.academico.ejb.utilidades.constantes.TiempoDedicacionConstantes;
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
 * Clase (session bean) ComitesForm.java Bean de sesión que maneja
 * los atributos del formulario de Comites.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "comitesForm")
@SessionScoped
public class ComitesForm  extends ReporteCargaHorariaForm  implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario cfUsuario;

	private List<Dependencia> cfListaFacultades;
	private List<Carrera> cfListaCarreras;
	private List<PersonaDatosDto> cfListaDocentes;
	private List<TipoFuncionCargaHoraria> cfListaFunciones;

	private PersonaDatosDto cfDocente;

	private TipoFuncionCargaHoraria cfTipoFuncionCargaHorariaSeleccion;

	private Integer cfFacultadBuscar;
	private Integer cfCarreraBuscar;
	private Integer cfDocenteBuscar;
	private String cfIdentificacionBuscar;

	private Integer cfDocenteHorasSemanales;

	private Integer cfFuncionSeleccion;
	private Integer cfHorasSemanales;

	private PeriodoAcademico cfPeriodoAcademico;
	private Integer cfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> cfListaPeriodosAcademicos;

	private PlanificacionCronogramaDto cfPlanificacionCronograma;

	private List<CargaHoraria> cfListaCargasHorarias;

	private CargaHoraria cfCargaHorariaActual;
	private Boolean cfCargaHorariaActiva;

	private String cfCedulaDocenteBusquedaAvanzada;
	private String cfApellidoDocenteBusquedaAvanzada;
	private String cfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> cfListDocentesBusquedaAvanzada;
	private List<Integer> cfListaHorasInvestigacion;

	//--v2
	//URL reporte
	private String cfLinkReporte;

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
	DependenciaServicio servCfDependenciaServicio;
	@EJB
	CarreraServicio servCfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servCfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servCfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servCfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servCfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servCfPlanificacionCronogramaDtoServicioJdbc; 
	@EJB
	CargaHorariaServicio servCfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servCfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servCfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servCfUsuarioRolServicio;


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

			cfCargaHorariaActiva = false;
			rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

			//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			validarCronograma();
			cagarPeriodos();
			cfListaFacultades = servCfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			Iterator it = cfListaFacultades.iterator();
			while(it.hasNext()){
				Dependencia cad = (Dependencia) it.next();
				if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					it.remove();
				}
			}
			//			}
		} catch (DependenciaNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad 
	 * @return navegacion hacia el formulario de comites y subcomites.
	 */
	public String irFormularioComites(Usuario usuario) {
		cfUsuario = usuario;
		inicarParametros();
		return "irFormularioComites";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {

		cfListaFacultades = null;
		cfListaCarreras = null;
		cfListaDocentes = null;
		cfListaFunciones = null;
		cfDocente = null;
		cfTipoFuncionCargaHorariaSeleccion = null;
		cfFacultadBuscar = null;
		cfCarreraBuscar = null;
		cfDocenteBuscar = null;
		cfIdentificacionBuscar = null;
		cfDocenteHorasSemanales = null;
		cfFuncionSeleccion = null;
		cfHorasSemanales = null;
		cfCargaHorariaActual = null;
		cfCargaHorariaActiva = false;
		cfPeriodoAcademico = null;
		cfPlanificacionCronograma = null;
		cfPeriodoAcademicoBuscar = null;
		cfListaPeriodosAcademicos = null;
		cfLinkReporte = null;
		cfListaHorasInvestigacion = null;

		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		cfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		cfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		cfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
		cfIdentificacionBuscar = null;
		cfHorasSemanales = null;
		cfListaFunciones = null;
		cfListaCarreras = null;
		cfListaFacultades = null;
		cfListaDocentes = null;
		cfTipoFuncionCargaHorariaSeleccion = null;
		cfDocente = null;
		cfCargaHorariaActual = null;
		cfCargaHorariaActiva = false;
		cfLinkReporte = null;
		cfPeriodoAcademico = verificarPeriodoActivo();
		cfListaHorasInvestigacion = null;
		inicarParametros();
		limpiarBusquedaAvanzada();
	}

	/**
	 * Limpia la seleccion de campos en base al docente
	 */
	public void limpiarInfoDocente(){

		cfListaFunciones = null;
		cfFuncionSeleccion = null;
		cfTipoFuncionCargaHorariaSeleccion = null;
		cfDocente = null;
		cfHorasSemanales = null;
		cfCargaHorariaActual = null;
		cfCargaHorariaActiva = false;
		cfLinkReporte = null;
		cfPeriodoAcademico = verificarPeriodoActivo();
		cfListaHorasInvestigacion = null;
		limpiarBusquedaAvanzada();
	}

	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		cfCedulaDocenteBusquedaAvanzada = null;
		cfApellidoDocenteBusquedaAvanzada = null;
		cfListDocentesBusquedaAvanzada = null;
		cfMensajeBusquedaAvanzada = null;
		cfListaHorasInvestigacion = null;
		cfLinkReporte = null;
	}

	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		//			if(validarCronograma()){ Cambio Solicitado por Eco. Reinoso 12/12/2017
		try {
			limpiarInfoDocente();
			if (cfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
				if (cfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
					cfPeriodoAcademico = servCfPeriodoAcademicoServicio.buscarPorId(cfPeriodoAcademicoBuscar);
					cfDocente = servCfPersonaDatosServicioJdbc.buscarPorId(cfDocenteBuscar , cfPeriodoAcademicoBuscar);
					cfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+cfDocente.getPrsIdentificacion()+"&prd="+cfPeriodoAcademico.getPracDescripcion();
					listarFunciones();

				} else { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.docente.validacion.exception")));
				}
			} else { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.facultad.validacion.exception")));
			}

		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.persona.datos.dto.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.persona.datos.dto.exception")));
		} catch (PeriodoAcademicoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.periodo.academico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.periodo.academico.exception")));
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
			cfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			cfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			cfListaCarreras = null;
			cfListaDocentes = null;

			cfDocente = servCfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , cfPeriodoAcademicoBuscar);
			listarFunciones();
			limpiarBusquedaAvanzada();
			cfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+cfDocente.getPrsIdentificacion()+"&prd="+cfPeriodoAcademico.getPracDescripcion();

		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.asignar.docente.exception")));
		}

	}

	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
		//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
		try {

			if(cfCedulaDocenteBusquedaAvanzada == null){
				cfCedulaDocenteBusquedaAvanzada = "";
			}
			if(cfApellidoDocenteBusquedaAvanzada == null){
				cfApellidoDocenteBusquedaAvanzada = "";
			}
			cfListDocentesBusquedaAvanzada = servCfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(cfCedulaDocenteBusquedaAvanzada, cfApellidoDocenteBusquedaAvanzada);

		} catch (PersonaDatosDtoException e) {
			cfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.docente.exception"));
		} catch (PersonaDatosDtoNoEncontradoException e) {
			cfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.buscar.docente.no.encontrado.exception"));
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
				if (cfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
					cargarCargaHoraria(cfDocente, 
							cfHorasSemanales, 
							cfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
							cfPeriodoAcademico, 
							cfTipoFuncionCargaHorariaSeleccion);
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.guardar.funcion.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.guardar.docente.validacion.exception")));
			}
		}
	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {

		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(cfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(cfHorasSemanales != null){ 
						cfCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(cfTipoFuncionCargaHorariaSeleccion);
						cfCargaHorariaActual.setCrhrObservacion(cfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
						cfCargaHorariaActual.setCrhrNumHoras(cfHorasSemanales);

						if(actualizarCargaHoraria(cfCargaHorariaActual)){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.guardar.cambios.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.guardar.cambios.exception")));
						}
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.guardar.cambios.docente.validacion.exception")));
			} 
		}
	}

	/**
	 * Elimina el registro
	 **/
	public void eliminar() {

		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(cfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
					if(cfHorasSemanales != null){


						cfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
						if(actualizarCargaHoraria(cfCargaHorariaActual)){
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.eliminar.con.exito.validacion")));
							limpiar();
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.eliminar.exception")));
						}
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.eliminar.docente.validacion.exception")));
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

			crhr = servCfCargaHorariaServicio.anadir(crhr);

			verificar = true;

			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.cargar.carga.horaria.con.exito.validacion", observacion)));
			limpiar();

		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.cargar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.cargar.carga.horaria.exception")));
		}

		return verificar;
	}

	/**
	 * Carga los campos de carga horaria del dcente
	 */
	public void listarCargaHorariaDocente(){ 

		cfCargaHorariaActiva = false;
		try {  
			if(cfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: cfListaFunciones){
					cfListaCargasHorarias =  servCfCargaHorariaServicio.buscarPorDetallePuesto(cfDocente.getDtpsId(), itemFuncion.getTifncrhrId(), cfPeriodoAcademico.getPracId());
					if(cfListaCargasHorarias.size() > 0 ){
						for(CargaHoraria item: cfListaCargasHorarias ){ 
							cfCargaHorariaActual = item;
							cfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
							cfHorasSemanales = item.getCrhrNumHoras();
							cfCargaHorariaActiva = true;
							calcularHoras();
						}
					}
				}
			}
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.carga.horaria.docente.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.carga.horaria.docente.exception")));
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
			verificar = servCfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.actualizar.carga.horaria.exception")));
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
		cfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		cfListaDocentes = null;
		cfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		cfListaCarreras = null;
	}

	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {

		try {	
			limpiarInfoDocente();
			if(cfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				cfListaCarreras = servCfCarreraServicio.listarCarrerasXFacultad(cfFacultadBuscar);
				cfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				cfListaDocentes = null;
				listarDocentesPorFacultad();
				cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}else{
				cfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				cfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			cfListaDocentes = null;
			cfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			cfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			cfListaDocentes = servCfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(cfFacultadBuscar, cfPeriodoAcademicoBuscar);
			cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			cfListaDocentes = servCfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(cfCarreraBuscar, cfPeriodoAcademicoBuscar);
			cfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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

			Boolean verificarFuncion = true;

			if(cfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
					|| cfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
				verificarFuncion = false; 
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.funciones.relacion.laboral.validacion.exception")));
			}else{
				switch (cfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE://Tiempo Completo
					verificarFuncion = true;
					break;
				default:
					verificarFuncion = false; 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.funciones.validacion")));
					break;
				}
			}

			if(verificarFuncion){
				cfListaFunciones = servCfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_COMITES_SUBCOMITES_VALUE);
				listarCargaHorariaDocente();
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.funciones.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.listar.funciones.exception")));
		}
	}


	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------

	/**
	 * Calcula las horas para comites y subcomites en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){
		try {
			if (cfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
//				cfTipoFuncionCargaHorariaSeleccion = servCfTipoFuncionCargaHorariaServicio.buscarPorId(cfFuncionSeleccion);
//				cfHorasSemanales = cfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
				cfTipoFuncionCargaHorariaSeleccion = servCfTipoFuncionCargaHorariaServicio.buscarPorId(cfFuncionSeleccion);
				cfListaHorasInvestigacion = new ArrayList<>();
				for(int i = cfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= cfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
					cfListaHorasInvestigacion.add(i); 
				}
			}else{
				cfHorasSemanales = null;
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.calcular.horas.exception")));
		}
	}

	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(cfDocente!=null){
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
			return servCfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.verificar.periodo.activo.validacion.exception")));
			return null;
		} catch (PeriodoAcademicoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.verificar.periodo.activo.exception")));
			return null;
		}
	}

	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){

		try {
			return servCfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
			return servCfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.verificar.cronograma.exception")));
			return null;
		}
	} 

	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){

		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		cfPlanificacionCronograma = verificarCronograma();
		if(cfPlanificacionCronograma != null){
			if(cfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(cfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Comites.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}

	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		cfListaPeriodosAcademicos = new ArrayList<>();

		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			cfListaPeriodosAcademicos.add(pracActivo);
			cfPeriodoAcademico = pracActivo;
			cfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}

		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			cfListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	public void generarReporteCargaHorariaDocente(){
		if (cfDocente != null) {
			cargarAsignacionesCargaHoraria(cfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(cfPeriodoAcademico.getPracId(), cfPeriodoAcademico.getPracDescripcion()), cfUsuario, cfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}

	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/



	public Integer getCfFacultadBuscar() {
		return cfFacultadBuscar;
	}

	public void setCfFacultadBuscar(Integer cfFacultadBuscar) {
		this.cfFacultadBuscar = cfFacultadBuscar;
	}

	public Integer getCfCarreraBuscar() {
		return cfCarreraBuscar;
	}

	public void setCfCarreraBuscar(Integer cfCarreraBuscar) {
		this.cfCarreraBuscar = cfCarreraBuscar;
	}

	public Integer getCfDocenteBuscar() {
		return cfDocenteBuscar;
	}

	public void setCfDocenteBuscar(Integer cfDocenteBuscar) {
		this.cfDocenteBuscar = cfDocenteBuscar;
	}

	public String getCfIdentificacionBuscar() {
		return cfIdentificacionBuscar;
	}

	public void setCfIdentificacionBuscar(String cfIdentificacionBuscar) {
		this.cfIdentificacionBuscar = cfIdentificacionBuscar;
	}

	public Integer getCfDocenteHorasSemanales() {
		return cfDocenteHorasSemanales;
	}

	public void setCfDocenteHorasSemanales(Integer cfDocenteHorasSemanales) {
		this.cfDocenteHorasSemanales = cfDocenteHorasSemanales;
	}

	public List<Dependencia> getCfListaFacultades() {
		return cfListaFacultades;
	}

	public void setCfListaFacultades(List<Dependencia> cfListaFacultades) {
		this.cfListaFacultades = cfListaFacultades;
	}

	public List<Carrera> getCfListaCarreras() {
		return cfListaCarreras;
	}

	public void setCfListaCarreras(List<Carrera> cfListaCarreras) {
		this.cfListaCarreras = cfListaCarreras;
	}

	public List<PersonaDatosDto> getCfListaDocentes() {
		return cfListaDocentes;
	}

	public void setCfListaDocentes(List<PersonaDatosDto> cfListaDocentes) {
		this.cfListaDocentes = cfListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getCfListaFunciones() {
		return cfListaFunciones;
	}

	public void setCfListaFunciones(List<TipoFuncionCargaHoraria> cfListaFunciones) {
		this.cfListaFunciones = cfListaFunciones;
	}

	public PersonaDatosDto getCfDocente() {
		return cfDocente;
	}

	public void setCfDocente(PersonaDatosDto cfDocente) {
		this.cfDocente = cfDocente;
	}

	public Integer getCfFuncionSeleccion() {
		return cfFuncionSeleccion;
	}

	public void setCfFuncionSeleccion(Integer cfFuncionSeleccion) {
		this.cfFuncionSeleccion = cfFuncionSeleccion;
	}

	public Integer getCfHorasSemanales() {
		return cfHorasSemanales;
	}

	public void setCfHorasSemanales(Integer cfHorasSemanales) {
		this.cfHorasSemanales = cfHorasSemanales;
	}

	public Boolean getCfCargaHorariaActiva() {
		return cfCargaHorariaActiva;
	}

	public void setCfCargaHorariaActiva(Boolean cfCargaHorariaActiva) {
		this.cfCargaHorariaActiva = cfCargaHorariaActiva;
	}

	public Integer getCfPeriodoAcademicoBuscar() {
		return cfPeriodoAcademicoBuscar;
	}

	public void setCfPeriodoAcademicoBuscar(Integer cfPeriodoAcademicoBuscar) {
		this.cfPeriodoAcademicoBuscar = cfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getCfListaPeriodosAcademicos() {
		return cfListaPeriodosAcademicos;
	}

	public void setCfListaPeriodosAcademicos(List<PeriodoAcademico> cfListaPeriodosAcademicos) {
		this.cfListaPeriodosAcademicos = cfListaPeriodosAcademicos;
	}

	public String getCfCedulaDocenteBusquedaAvanzada() {
		return cfCedulaDocenteBusquedaAvanzada;
	}

	public void setCfCedulaDocenteBusquedaAvanzada(String cfCedulaDocenteBusquedaAvanzada) {
		this.cfCedulaDocenteBusquedaAvanzada = cfCedulaDocenteBusquedaAvanzada;
	}

	public String getCfApellidoDocenteBusquedaAvanzada() {
		return cfApellidoDocenteBusquedaAvanzada;
	}

	public void setCfApellidoDocenteBusquedaAvanzada(String cfApellidoDocenteBusquedaAvanzada) {
		this.cfApellidoDocenteBusquedaAvanzada = cfApellidoDocenteBusquedaAvanzada;
	}

	public String getCfMensajeBusquedaAvanzada() {
		return cfMensajeBusquedaAvanzada;
	}

	public void setCfMensajeBusquedaAvanzada(String cfMensajeBusquedaAvanzada) {
		this.cfMensajeBusquedaAvanzada = cfMensajeBusquedaAvanzada;
	}

	public List<PersonaDatosDto> getCfListDocentesBusquedaAvanzada() {
		return cfListDocentesBusquedaAvanzada;
	}

	public void setCfListDocentesBusquedaAvanzada(List<PersonaDatosDto> cfListDocentesBusquedaAvanzada) {
		this.cfListDocentesBusquedaAvanzada = cfListDocentesBusquedaAvanzada;
	}

	public String getCfLinkReporte() {
		return cfLinkReporte;
	}

	public void setCfLinkReporte(String cfLinkReporte) {
		this.cfLinkReporte = cfLinkReporte;
	}

	public List<Integer> getCfListaHorasInvestigacion() {
		return cfListaHorasInvestigacion;
	}

	public void setCfListaHorasInvestigacion(List<Integer> cfListaHorasInvestigacion) {
		this.cfListaHorasInvestigacion = cfListaHorasInvestigacion;
	}

	public Usuario getCfUsuario() {
		return cfUsuario;
	}

	public void setCfUsuario(Usuario cfUsuario) {
		this.cfUsuario = cfUsuario;
	}

	


}