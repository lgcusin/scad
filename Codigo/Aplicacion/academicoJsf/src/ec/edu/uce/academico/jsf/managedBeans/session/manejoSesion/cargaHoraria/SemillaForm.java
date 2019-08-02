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
   
 ARCHIVO:     SemillaForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de proyectos semilla. 
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
import ec.edu.uce.academico.ejb.utilidades.constantes.TiempoDedicacionConstantes;
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
 * Clase (session bean) SemillaForm.java Bean de sesión que maneja
 * los atributos de proyectos semilla.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "semillaForm")
@SessionScoped
public class SemillaForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	private Usuario sfUsuario;
 
	private List<Dependencia> sfListaFacultades;
	private List<Carrera> sfListaCarreras;
	private List<PersonaDatosDto> sfListaDocentes;
	private List<TipoFuncionCargaHoraria> sfListaFunciones;

	private PersonaDatosDto sfDocente;
	
	private TipoFuncionCargaHoraria sfTipoFuncionCargaHorariaSeleccion;
	  
	private Integer sfFacultadBuscar;
	private Integer sfCarreraBuscar;
	private Integer sfDocenteBuscar;
	private String sfIdentificacionBuscar;
  
	private String sfNombreProyecto;
	private Integer sfFuncionSeleccion; 
	private Integer sfHorasProyecto;
	private Date sfFechaInicio;
	private Date sfFechaFin;
	
	private Integer sfHorasSemanales;
	 
	private PeriodoAcademico sfPeriodoAcademico;
	private Integer sfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> sfListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto sfPlanificacionCronograma;

	private List<CargaHoraria> sfListaCargasHorarias;
	private List<DetalleCargaHoraria> sfListaDetalleCargaHoraria;
	
	private CargaHoraria sfCargaHorariaActual;
	private DetalleCargaHoraria sfDetalleCargaHorariaActual;
	  
	private Boolean sfCargaHorariaActiva;  
	
	private String sfCedulaDocenteBusquedaAvanzada;
	private String sfApellidoDocenteBusquedaAvanzada;
	private String sfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> sfListDocentesBusquedaAvanzada;
	
	private List<Integer> sfFuncionProyecto;
	
	private List<Integer> sfFuncionEstructuras;
	private TipoFuncionCargaHoraria sfTipoFuncionCargaHorariaEstructurasSeleccion;
	private String sfNombrePaper;
	private Integer sfHorasEstructuras;
	
	private List<CargaHoraria> sfListaCargasHorariasEstructura;
	private List<DetalleCargaHoraria> sfListaDetalleCargaHorariaEstructura;
	
	private CargaHoraria sfCargaHorariaActualEstructura;
	private DetalleCargaHoraria sfDetalleCargaHorariaActualEstructura;
	 
	//--v2
	//URL reporte
	private String sfLinkReporte;
	
	//--v3
	private List<Integer> sfListaHorasProyecto;
	
	
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
	DependenciaServicio servSfDependenciaServicio;
	@EJB
	CarreraServicio servSfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servSfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servSfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servSfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servSfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servSfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	CargaHorariaServicio servSfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servSfDetalleCargaHorariaServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servSfDocenteDatosDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servSfUsuarioRolServicio;
	
	
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
		sfFechaInicio = date;
		sfFechaFin = date;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

		try {	
			
			sfCargaHorariaActiva= false;
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				validarCronograma();
				cagarPeriodos();
				sfListaFacultades = servSfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = sfListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			}
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irFormularioSemilla(Usuario usuario) {
		sfUsuario = usuario;
		inicarParametros();
		return "irFormularioSemilla";
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		sfListaFacultades = null;
		sfListaCarreras = null;
		sfListaDocentes = null;
		sfListaFunciones = null;
		sfDocente = null;
		sfTipoFuncionCargaHorariaSeleccion = null;  
		sfFacultadBuscar = null;
		sfCarreraBuscar = null;
		sfDocenteBuscar = null;
		sfIdentificacionBuscar = null;
		sfNombreProyecto = null;
		sfFuncionSeleccion = null;
		sfHorasSemanales = null;
		sfFechaInicio = null;
		sfFechaFin = null;
		sfCargaHorariaActual = null;
		sfDetalleCargaHorariaActual = null;
		sfCargaHorariaActiva= false;
		sfPeriodoAcademicoBuscar = null;
		sfListaPeriodosAcademicos = null;
		sfFuncionEstructuras = null;
		sfTipoFuncionCargaHorariaEstructurasSeleccion = null;
		sfHorasEstructuras = null;
		sfHorasProyecto = null; 
		sfFuncionProyecto = null;
		sfFuncionEstructuras = null;
		sfTipoFuncionCargaHorariaEstructurasSeleccion = null;
		sfNombrePaper = null;
		sfHorasEstructuras = null;
		sfListaCargasHorariasEstructura = null;
		sfListaDetalleCargaHorariaEstructura = null;
		sfCargaHorariaActualEstructura = null;
		sfDetalleCargaHorariaActualEstructura = null; 
		sfLinkReporte = null;
		sfListaHorasProyecto = null;
		
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		sfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		sfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		sfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
		sfIdentificacionBuscar = null;
		sfHorasSemanales = null;
		sfListaFunciones = null;
		sfListaCarreras = null;
		sfListaFacultades = null;
		sfListaDocentes = null;
		sfNombreProyecto = null;
		sfTipoFuncionCargaHorariaSeleccion = null;
		sfDocente = null;
		sfFechaInicio = null;
		sfFechaFin = null;
		sfCargaHorariaActual = null;
		sfDetalleCargaHorariaActual = null;
		sfCargaHorariaActiva= false;
		sfPeriodoAcademico = verificarPeriodoActivo();
		sfFuncionEstructuras = null;
		sfTipoFuncionCargaHorariaEstructurasSeleccion = null;
		sfHorasEstructuras = null;
		sfHorasProyecto = null;
		sfFuncionProyecto = null;
		sfFuncionEstructuras = null;
		sfTipoFuncionCargaHorariaEstructurasSeleccion = null;
		sfNombrePaper = null;
		sfHorasEstructuras = null;
		sfListaCargasHorariasEstructura = null;
		sfListaDetalleCargaHorariaEstructura = null;
		sfCargaHorariaActualEstructura = null;
		sfDetalleCargaHorariaActualEstructura = null; 
		sfLinkReporte = null;
		sfListaHorasProyecto = null;
		inicarParametros();
		
	}
	
	/**
	 * Limpia la seleccion de campos en base al docente
	 */
	public void limpiarInfoDocente(){
		sfListaFunciones = null;
		sfFuncionSeleccion = null;
		sfTipoFuncionCargaHorariaSeleccion = null;
		sfDocente = null;
		sfNombreProyecto = null;
		sfHorasSemanales = null;
		sfCargaHorariaActual = null;
		sfDetalleCargaHorariaActual = null;
		sfCargaHorariaActiva= false;
		sfPeriodoAcademico = verificarPeriodoActivo();
		sfFuncionEstructuras = null;
		sfTipoFuncionCargaHorariaEstructurasSeleccion = null;
		sfHorasEstructuras = null;
		sfHorasProyecto = null;
		sfFuncionProyecto = null;
		sfNombrePaper = null; 
		sfListaCargasHorariasEstructura = null;
		sfListaDetalleCargaHorariaEstructura = null;
		sfCargaHorariaActualEstructura = null;
		sfDetalleCargaHorariaActualEstructura = null; 
		sfLinkReporte = null;
		sfListaHorasProyecto = null;
		
		Date date = new Date();
		sfFechaInicio = date;
		sfFechaFin = date;
	}
	
	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		sfCedulaDocenteBusquedaAvanzada = null;
		sfApellidoDocenteBusquedaAvanzada = null;
		sfListDocentesBusquedaAvanzada = null;
		sfMensajeBusquedaAvanzada = null;
		sfLinkReporte = null;
		sfListaHorasProyecto = null;
	}

	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017	
			try {
				limpiarInfoDocente();
				if (sfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
					if (sfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
						sfPeriodoAcademico = servSfPeriodoAcademicoServicio.buscarPorId(sfPeriodoAcademicoBuscar);
						sfDocente = servSfPersonaDatosServicioJdbc.buscarPorId(sfDocenteBuscar , sfPeriodoAcademicoBuscar);
						sfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+sfDocente.getPrsIdentificacion()+"&prd="+sfPeriodoAcademico.getPracDescripcion();
						listarFunciones();
						
					} else {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.docente.validacion.exception")));
					}
				} else { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.facultad.validacion.exception")));
				}
	
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.persona.datos.dto.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.persona.datos.dto.exception")));
			} catch (PeriodoAcademicoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.periodo.academico.no.encontrado.exception")));
			} catch (PeriodoAcademicoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.periodo.academico.exception")));
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
			
			sfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			sfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			sfListaCarreras = null;
			sfListaDocentes = null;
			
				sfDocente = servSfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , sfPeriodoAcademicoBuscar);
				listarFunciones();
				limpiarBusquedaAvanzada();
				sfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+sfPeriodoAcademico.getPracDescripcion();
				
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.asignar.docente.exception")));
		}
		
	}
	
	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			try {
				
				if(sfCedulaDocenteBusquedaAvanzada == null){
					sfCedulaDocenteBusquedaAvanzada = "";
				}
				if(sfApellidoDocenteBusquedaAvanzada == null){
					sfApellidoDocenteBusquedaAvanzada = "";
				}
				sfListDocentesBusquedaAvanzada = servSfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(sfCedulaDocenteBusquedaAvanzada, sfApellidoDocenteBusquedaAvanzada);
				
			} catch (PersonaDatosDtoException e) {
				sfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.docente.exception"));
			} catch (PersonaDatosDtoNoEncontradoException e) {
				sfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.buscar.docente.no.encontrado.exception"));
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
				Boolean verificar = true;
				if(verificarGuardar()){
					if(sfFuncionProyecto != null &&  sfFuncionProyecto.size() > 0){
						verificar = false;
						if(cargarCargaHoraria(sfDocente, 
								sfHorasProyecto, 
								sfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								sfPeriodoAcademico, 
								sfTipoFuncionCargaHorariaSeleccion) != null){
							verificar = true;  
						}
										
					}
					
					if(verificar){
						if(sfFuncionEstructuras != null &&  sfFuncionEstructuras.size() > 0){
							verificar = false;
							if(cargarCargaHoraria(sfDocente, 
									sfHorasEstructuras, 
									sfTipoFuncionCargaHorariaEstructurasSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									sfPeriodoAcademico, 
									sfTipoFuncionCargaHorariaEstructurasSeleccion) != null){
								verificar = true;
							}
						}	 
					}
					
					if(verificar){
						limpiar(); 
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.guardar.docente.validacion")));
			} 
		}
	}
 
	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {
		
		Boolean verificar = false; 
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){
				if(verificarGuardar()){
					if(sfFuncionProyecto != null &&  sfFuncionProyecto.size() > 0){
						if(sfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){
							verificar = false;
							if(sfCargaHorariaActual != null){
								sfCargaHorariaActual.setCrhrTipoFuncionCargaHoraria(sfTipoFuncionCargaHorariaSeleccion);
								sfCargaHorariaActual.setCrhrObservacion(sfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								sfCargaHorariaActual.setCrhrNumHoras(sfHorasProyecto);
								if(actualizarCargaHoraria(sfCargaHorariaActual)){
									sfDetalleCargaHorariaActual.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(sfNombreProyecto).toUpperCase());
									sfDetalleCargaHorariaActual.setDtcrhrFechaInicio(sfFechaInicio);
									sfDetalleCargaHorariaActual.setDtcrhrFechaFin(sfFechaFin);
									sfDetalleCargaHorariaActual.setDtcrhrNumHoras(sfHorasProyecto);
									verificar = actualizarDetalleCargaHoraria(sfDetalleCargaHorariaActual); 	
								}
							}else{
								cargarCargaHoraria(sfDocente, 
										sfHorasProyecto, 
										sfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										sfPeriodoAcademico, 
										sfTipoFuncionCargaHorariaSeleccion);
								verificar = true;
							}
						}
					//Si al actualizar se ha descartado el parametro ingresado inicialmente se elimina 
					}else{
						if(sfCargaHorariaActual != null){
							verificar = false;
							sfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
							if(actualizarCargaHoraria(sfCargaHorariaActual)){
								sfDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
								if(actualizarDetalleCargaHoraria(sfDetalleCargaHorariaActual)){
									verificar = true;
								}
							} 
						}
					}
					
					if(sfFuncionEstructuras != null && sfFuncionEstructuras.size() > 0){
						verificar = false;
						if(sfCargaHorariaActualEstructura != null){
							sfCargaHorariaActualEstructura.setCrhrTipoFuncionCargaHoraria(sfTipoFuncionCargaHorariaEstructurasSeleccion);
							sfCargaHorariaActualEstructura.setCrhrObservacion(sfTipoFuncionCargaHorariaEstructurasSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
							sfCargaHorariaActualEstructura.setCrhrNumHoras(sfHorasEstructuras);
							if(actualizarCargaHoraria(sfCargaHorariaActualEstructura)){
								sfDetalleCargaHorariaActualEstructura.setDtcrhrFuncion(GeneralesUtilidades.eliminarEspaciosEnBlanco(sfNombrePaper).toUpperCase());
								sfDetalleCargaHorariaActualEstructura.setDtcrhrNumHoras(sfHorasEstructuras);
								 verificar = actualizarDetalleCargaHoraria(sfDetalleCargaHorariaActualEstructura);
								  
							}
						}else{
							cargarCargaHoraria(sfDocente, 
									sfHorasEstructuras, 
									sfTipoFuncionCargaHorariaEstructurasSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									sfPeriodoAcademico, 
									sfTipoFuncionCargaHorariaEstructurasSeleccion);
							verificar = true;
						} 
					//Si al actualizar se ha descartado el parametro ingresado inicialmente se elimina 
					}else{
						if(sfCargaHorariaActualEstructura != null){
							verificar = false; 
							sfCargaHorariaActualEstructura.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
							if(actualizarCargaHoraria(sfCargaHorariaActualEstructura)){
								sfDetalleCargaHorariaActualEstructura.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
								verificar = actualizarDetalleCargaHoraria(sfDetalleCargaHorariaActualEstructura);
							} 
						}
					}
					
					if(verificar){
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.guardar.cambios.con.exito.validacion")));
						limpiar();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.guardar.cambios.exception")));
					}
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.guardar.cambios.docente.validacion.exception")));
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
				
				if(sfCargaHorariaActual != null){
					verificar = false;
					sfCargaHorariaActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
					if(actualizarCargaHoraria(sfCargaHorariaActual)){
						sfDetalleCargaHorariaActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
						verificar = actualizarDetalleCargaHoraria(sfDetalleCargaHorariaActual);
					} 
				}
					
				if(sfCargaHorariaActualEstructura != null){
					verificar = false;
					sfCargaHorariaActualEstructura.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
					if(actualizarCargaHoraria(sfCargaHorariaActualEstructura)){
						sfDetalleCargaHorariaActualEstructura.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
						verificar = actualizarDetalleCargaHoraria(sfDetalleCargaHorariaActualEstructura);
					} 
				}
				
				if(verificar){
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.eliminar.con.exito.validacion")));
					limpiar();
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.eliminar.exception")));
				} 		 
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.eliminar.docente.validacion.exception")));
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
			crhr.setCrhrObservacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(observacion).toUpperCase());
			crhr.setCrhrPeriodoAcademico(periodo);
			crhr.setCrhrTipoFuncionCargaHoraria(tipo);
			crhr.setCrhrNumHoras(horasSemanales);
			
			crhrResult = servSfCargaHorariaServicio.anadir(crhr);
			
			DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
			
			dtcrhr.setDtcrhrCargaHoraria(crhrResult);
			dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
			
			if(tipo.getTifncrhrId() == 44){ 
				dtcrhr.setDtcrhrFuncion(GeneralesUtilidades.eliminarEspaciosEnBlanco(sfNombrePaper).toUpperCase());
				dtcrhr.setDtcrhrNumHoras(sfHorasEstructuras); 
			}
			
			if(tipo.getTifncrhrId() == 89 || tipo.getTifncrhrId() == 90 || tipo.getTifncrhrId() == 91){
				dtcrhr.setDtcrhrNombreProyecto(GeneralesUtilidades.eliminarEspaciosEnBlanco(sfNombreProyecto).toUpperCase());
				dtcrhr.setDtcrhrFechaInicio(sfFechaInicio);
				dtcrhr.setDtcrhrFechaFin(sfFechaFin); 
				dtcrhr.setDtcrhrNumHoras(sfHorasProyecto);
			}
			
			servSfDetalleCargaHorariaServicio.anadir(dtcrhr);
			 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.cargar.carga.horaria.con.exito.validacion", observacion)));

		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.cargar.carga.horaria.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.cargar.carga.horaria.detalle.carga.horaria.exception")));
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.cargar.carga.horaria.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.cargar.carga.horaria.carga.horaria.exception")));
		}
		
		return crhrResult;
	}
	
	/**
	 * Carga los campos de carga horaria del dcente
	 */
	public void listarCargaHorariaDocente(){
		try { 
			if(sfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: sfListaFunciones){
					if(itemFuncion.getTifncrhrId() == 89 || itemFuncion.getTifncrhrId() == 90 || itemFuncion.getTifncrhrId() == 91 ){
						sfListaCargasHorarias =  servSfCargaHorariaServicio.buscarPorDetallePuesto(sfDocente.getDtpsId(), itemFuncion.getTifncrhrId(), sfPeriodoAcademico.getPracId());
						if(sfListaCargasHorarias.size() > 0 ){
							for(CargaHoraria item: sfListaCargasHorarias ){ 
								sfCargaHorariaActual = item;
								sfListaDetalleCargaHoraria = servSfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
								if(sfListaDetalleCargaHoraria.size() > 0){
									for(DetalleCargaHoraria itemDetalle: sfListaDetalleCargaHoraria){
										sfDetalleCargaHorariaActual = itemDetalle;
										sfFuncionProyecto = new ArrayList<>();
										sfFuncionProyecto.add(101);
										sfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
										sfNombreProyecto = itemDetalle.getDtcrhrNombreProyecto();
										sfFechaInicio = itemDetalle.getDtcrhrFechaInicio();
										sfFechaFin = itemDetalle.getDtcrhrFechaFin();
										sfHorasProyecto = item.getCrhrNumHoras();
										sfCargaHorariaActiva= true; 
										calcularHoras();
									}
								} 
							} 
						}
					}
					
					if(itemFuncion.getTifncrhrId() == 44){
						sfListaCargasHorariasEstructura =  servSfCargaHorariaServicio.buscarPorDetallePuesto(sfDocente.getDtpsId(), itemFuncion.getTifncrhrId(), sfPeriodoAcademico.getPracId());
						if(sfListaCargasHorariasEstructura.size() > 0 ){
							for(CargaHoraria item: sfListaCargasHorariasEstructura ){ 
								sfCargaHorariaActualEstructura = item;
								sfListaDetalleCargaHorariaEstructura = servSfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
								if(sfListaDetalleCargaHorariaEstructura.size() > 0){
									for(DetalleCargaHoraria itemDetalle: sfListaDetalleCargaHorariaEstructura){
										sfFuncionEstructuras = new ArrayList<>();
										sfFuncionEstructuras.add(101);
										sfDetalleCargaHorariaActualEstructura = itemDetalle;
										sfNombrePaper = itemDetalle.getDtcrhrFuncion();
										sfHorasEstructuras = item.getCrhrNumHoras();
										sfCargaHorariaActiva= true; 
										calcularHoras();
									} 
								} 
							} 
						}
					} 
				}
			}
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.carga.horaria.docente.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.carga.horaria.docente.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.carga.horaria.docente.detalle.carga.horaria.exception")));
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
				verificar = servSfCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.actualizar.carga.horaria.exception")));
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
			verificar = servSfDetalleCargaHorariaServicio.editar(entidad);
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.actualizar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.actualizar.detalle.carga.horaria.exception")));
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
		sfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		sfListaDocentes = null;
		sfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		sfListaCarreras = null; 
	}
	
	
	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {
		
		try {	
			limpiarInfoDocente();
			if(sfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				sfListaCarreras = servSfCarreraServicio.listarCarrerasXFacultad(sfFacultadBuscar);
				sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				sfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				sfListaDocentes = null;
				listarDocentesPorFacultad();
			}else{
				sfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				sfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			sfListaDocentes = null;
			sfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			sfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			sfListaDocentes = servSfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(sfFacultadBuscar, sfPeriodoAcademicoBuscar);
			sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			sfListaDocentes = servSfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(sfCarreraBuscar, sfPeriodoAcademicoBuscar);
			sfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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

		Boolean verificarFuncion = true;
		List<TipoFuncionCargaHoraria> funciones = null;

		try {


			if(sfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE || sfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
				verificarFuncion = false; 
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.funciones.relacion.laboral.validacion.exception")));
			}else{

				switch (sfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
					verificarFuncion = true;
					funciones = servSfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_PROYECTOS_SEMILLA_VALUE);
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
					funciones = servSfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_PROYECTOS_SEMILLA_VALUE);
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
					funciones = servSfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_PROYECTOS_SEMILLA_VALUE);
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

				sfListaFunciones = funciones;
			}

			if(verificarFuncion){
				//					sfListaFunciones = servSfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_PROYECTOS_SEMILLA_VALUE);
				listarCargaHorariaDocente();
			}

		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.funciones.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.funciones.exception")));
		}
	}
	
 
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	public void calcularHoras(){
		try {
			
			Boolean verificarFuncion = true;
			
			if(verificarDocente()){
			
				switch (sfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
					sfFuncionSeleccion = 89;//TipoFuncionCargaHorariaConstantes.FUNCION_INVESTIGADOR_PROYECTO_SEMILLA_TC_VALUE;
					break;
				case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
					sfFuncionSeleccion = 90;//TipoFuncionCargaHorariaConstantes.FUNCION_INVESTIGADOR_PROYECTO_SEMILLA_MT_VALUE;
					break;
				case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE://Tiempo Parcial
					sfFuncionSeleccion = 91;//TipoFuncionCargaHorariaConstantes.FUNCION_INVESTIGADOR_PROYECTO_SEMILLA_TP_VALUE;
					break;
				default:
					verificarFuncion = false;
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.calcular.horas.validacion")));
					break;
				}
				
				if(verificarFuncion){
					
					sfHorasEstructuras = 0;
					
					if(sfFuncionProyecto != null && sfFuncionProyecto.size() > 0){
						sfTipoFuncionCargaHorariaSeleccion = servSfTipoFuncionCargaHorariaServicio.buscarPorId(sfFuncionSeleccion);
						
						sfListaHorasProyecto = new ArrayList<>();
						for(int i = sfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= sfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
							sfListaHorasProyecto.add(i); 
						}
						
						if (sfListaHorasProyecto.size()==1) {
							sfHorasProyecto=sfListaHorasProyecto.get(0).intValue();
						}
//						sfHorasProyecto = sfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
					}else{
						sfListaHorasProyecto = null;
						sfHorasProyecto = null;
					}
					
					if(sfFuncionEstructuras != null && sfFuncionEstructuras.size() > 0){
						sfTipoFuncionCargaHorariaEstructurasSeleccion = servSfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_ESTRUCTURAS_PAPERS_ARTICULOS_CIENTIFICOS);
						sfHorasEstructuras = sfTipoFuncionCargaHorariaEstructurasSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
					}
					
					Integer sfHorasProyectoAux = 0;
					if(sfHorasProyecto != null && sfHorasProyecto != 0){
						sfHorasProyectoAux = sfHorasProyecto;
					}
					
					sfHorasSemanales = sfHorasEstructuras+sfHorasProyectoAux;
				}
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.calcular.horas.exception")));
		}
	}
	
	
	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(sfDocente!=null){
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
			return servSfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.periodo.activo.validacion.exception")));
			return null;
		} catch (PeriodoAcademicoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.periodo.activo.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){
		
		try {
			return servSfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
			return servSfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.cronograma.exception")));
			return null;
		}
	}

	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		sfPlanificacionCronograma = verificarCronograma();
		if(sfPlanificacionCronograma != null){
			if(sfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(sfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}
	
	/**
	 * Valida campos al guardar o gaurdar cambios
	 **/
	public Boolean verificarGuardar(){
		 
		Boolean verificar = true; 
		
		if((sfFuncionProyecto != null &&  sfFuncionProyecto.size() > 0 ) || (sfFuncionEstructuras != null && sfFuncionEstructuras.size() > 0)){
		
			if(sfFuncionProyecto != null &&  sfFuncionProyecto.size() > 0){
				verificar = false;
				if(sfNombreProyecto != null && sfNombreProyecto.replaceAll(" ", "").length()!=0){
					if(sfFechaInicio.before(sfFechaFin)){
						if(sfHorasSemanales != null){  
							verificar = true; 
						} 
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.guardar.fechas.incorrectas.validacion")));
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.guardar.nombre.proyecto.validacion")));
				} 
			}
			
			if(verificar){
				if(sfFuncionEstructuras != null &&  sfFuncionEstructuras.size() > 0){
					verificar = false;
					if(sfNombrePaper != null && sfNombrePaper.replaceAll(" ", "").length()!=0){
						if(sfHorasEstructuras != null){
							if(sfFuncionEstructuras.size() > 0){ 
								verificar = true;
							}	
						}  
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.guardar.nombre.paper.validacion")));
					} 
				} 
			}
		}else{ 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.verificar.guardar.validacion.exception")));
			verificar = false;
		}
		
		if(sfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
				|| sfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
			verificar = false; 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Semilla.listar.funciones.relacion.laboral.validacion.exception")));
		}
		 
		return verificar;
	}
	
	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		
		sfListaPeriodosAcademicos = new ArrayList<>();
		
		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			sfListaPeriodosAcademicos.add(pracActivo);
			sfPeriodoAcademico = pracActivo;
			sfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}
		
		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			sfListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	public void generarReporteCargaHorariaDocente(){
		if (sfDocente != null) {
			cargarAsignacionesCargaHoraria(sfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(sfPeriodoAcademico.getPracId(),sfPeriodoAcademico.getPracDescripcion()), sfUsuario, sfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}

	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/


	public List<Dependencia> getSfListaFacultades() {
		return sfListaFacultades;
	}

	public void setSfListaFacultades(List<Dependencia> sfListaFacultades) {
		this.sfListaFacultades = sfListaFacultades;
	}

	public List<Carrera> getSfListaCarreras() {
		return sfListaCarreras;
	}

	public void setSfListaCarreras(List<Carrera> sfListaCarreras) {
		this.sfListaCarreras = sfListaCarreras;
	}

	public List<PersonaDatosDto> getSfListaDocentes() {
		return sfListaDocentes;
	}

	public void setSfListaDocentes(List<PersonaDatosDto> sfListaDocentes) {
		this.sfListaDocentes = sfListaDocentes;
	}
 
	public PersonaDatosDto getSfDocente() {
		return sfDocente;
	}

	public void setSfDocente(PersonaDatosDto sfDocente) {
		this.sfDocente = sfDocente;
	}

	public Integer getSfFacultadBuscar() {
		return sfFacultadBuscar;
	}

	public void setSfFacultadBuscar(Integer sfFacultadBuscar) {
		this.sfFacultadBuscar = sfFacultadBuscar;
	}

	public Integer getSfCarreraBuscar() {
		return sfCarreraBuscar;
	}

	public void setSfCarreraBuscar(Integer sfCarreraBuscar) {
		this.sfCarreraBuscar = sfCarreraBuscar;
	}

	public Integer getSfDocenteBuscar() {
		return sfDocenteBuscar;
	}

	public void setSfDocenteBuscar(Integer sfDocenteBuscar) {
		this.sfDocenteBuscar = sfDocenteBuscar;
	}

	public String getSfIdentificacionBuscar() {
		return sfIdentificacionBuscar;
	}

	public void setSfIdentificacionBuscar(String sfIdentificacionBuscar) {
		this.sfIdentificacionBuscar = sfIdentificacionBuscar;
	}

	public String getSfNombreProyecto() {
		return sfNombreProyecto;
	}

	public void setSfNombreProyecto(String sfNombreProyecto) {
		this.sfNombreProyecto = sfNombreProyecto;
	}
 
	public Integer getSfHorasSemanales() {
		return sfHorasSemanales;
	}

	public void setSfHorasSemanales(Integer sfHorasSemanales) {
		this.sfHorasSemanales = sfHorasSemanales;
	}

	public Date getSfFechaInicio() {
		return sfFechaInicio;
	}

	public void setSfFechaInicio(Date sfFechaInicio) {
		this.sfFechaInicio = sfFechaInicio;
	}

	public Date getSfFechaFin() {
		return sfFechaFin;
	}

	public void setSfFechaFin(Date sfFechaFin) {
		this.sfFechaFin = sfFechaFin;
	}

	public List<TipoFuncionCargaHoraria> getSfListaFunciones() {
		return sfListaFunciones;
	}

	public void setSfListaFunciones(List<TipoFuncionCargaHoraria> sfListaFunciones) {
		this.sfListaFunciones = sfListaFunciones;
	}

	public Integer getSfFuncionSeleccion() {
		return sfFuncionSeleccion;
	}

	public void setSfFuncionSeleccion(Integer sfFuncionSeleccion) {
		this.sfFuncionSeleccion = sfFuncionSeleccion;
	}

	public Boolean getSfCargaHorariaActiva() {
		return sfCargaHorariaActiva;
	}

	public void setSfCargaHorariaActiva(Boolean sfCargaHorariaActiva) {
		this.sfCargaHorariaActiva = sfCargaHorariaActiva;
	}

	public Integer getSfPeriodoAcademicoBuscar() {
		return sfPeriodoAcademicoBuscar;
	}

	public void setSfPeriodoAcademicoBuscar(Integer sfPeriodoAcademicoBuscar) {
		this.sfPeriodoAcademicoBuscar = sfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getSfListaPeriodosAcademicos() {
		return sfListaPeriodosAcademicos;
	}

	public void setSfListaPeriodosAcademicos(List<PeriodoAcademico> sfListaPeriodosAcademicos) {
		this.sfListaPeriodosAcademicos = sfListaPeriodosAcademicos;
	}

	public String getSfCedulaDocenteBusquedaAvanzada() {
		return sfCedulaDocenteBusquedaAvanzada;
	}

	public void setSfCedulaDocenteBusquedaAvanzada(String sfCedulaDocenteBusquedaAvanzada) {
		this.sfCedulaDocenteBusquedaAvanzada = sfCedulaDocenteBusquedaAvanzada;
	}

	public String getSfApellidoDocenteBusquedaAvanzada() {
		return sfApellidoDocenteBusquedaAvanzada;
	}

	public void setSfApellidoDocenteBusquedaAvanzada(String sfApellidoDocenteBusquedaAvanzada) {
		this.sfApellidoDocenteBusquedaAvanzada = sfApellidoDocenteBusquedaAvanzada;
	}

	public String getSfMensajeBusquedaAvanzada() {
		return sfMensajeBusquedaAvanzada;
	}

	public void setSfMensajeBusquedaAvanzada(String sfMensajeBusquedaAvanzada) {
		this.sfMensajeBusquedaAvanzada = sfMensajeBusquedaAvanzada;
	}

	public List<PersonaDatosDto> getSfListDocentesBusquedaAvanzada() {
		return sfListDocentesBusquedaAvanzada;
	}

	public void setSfListDocentesBusquedaAvanzada(List<PersonaDatosDto> sfListDocentesBusquedaAvanzada) {
		this.sfListDocentesBusquedaAvanzada = sfListDocentesBusquedaAvanzada;
	}

	public List<Integer> getSfFuncionEstructuras() {
		return sfFuncionEstructuras;
	}

	public void setSfFuncionEstructuras(List<Integer> sfFuncionEstructuras) {
		this.sfFuncionEstructuras = sfFuncionEstructuras;
	}

	public String getSfNombrePaper() {
		return sfNombrePaper;
	}

	public void setSfNombrePaper(String sfNombrePaper) {
		this.sfNombrePaper = sfNombrePaper;
	}

	public List<Integer> getSfFuncionProyecto() {
		return sfFuncionProyecto;
	}

	public void setSfFuncionProyecto(List<Integer> sfFuncionProyecto) {
		this.sfFuncionProyecto = sfFuncionProyecto;
	}

	public String getSfLinkReporte() {
		return sfLinkReporte;
	}

	public void setSfLinkReporte(String sfLinkReporte) {
		this.sfLinkReporte = sfLinkReporte;
	}

	public Integer getSfHorasProyecto() {
		return sfHorasProyecto;
	}

	public void setSfHorasProyecto(Integer sfHorasProyecto) {
		this.sfHorasProyecto = sfHorasProyecto;
	}

	public List<Integer> getSfListaHorasProyecto() {
		return sfListaHorasProyecto;
	}

	public void setSfListaHorasProyecto(List<Integer> sfListaHorasProyecto) {
		this.sfListaHorasProyecto = sfListaHorasProyecto;
	}

	public Usuario getSfUsuario() {
		return sfUsuario;
	}

	public void setSfUsuario(Usuario sfUsuario) {
		this.sfUsuario = sfUsuario;
	} 

	
	
}