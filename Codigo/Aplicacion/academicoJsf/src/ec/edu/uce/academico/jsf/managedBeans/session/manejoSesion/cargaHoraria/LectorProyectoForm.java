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
   
 ARCHIVO:     LectorProyectoForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de proyectos LectorProyecto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Carlos Roca 			              Emisión Inicial
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
 * Clase (session bean) LectorProyectoForm.java Bean de sesión que maneja
 * los atributos de proyectos LectorProyecto.
 * 
 * @author caroca.
 * @version 1.0
 */

@ManagedBean(name = "lectorProyectoForm")
@SessionScoped
public class LectorProyectoForm  extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	private Usuario lpfUsuario;

	private List<Dependencia> lpfListaFacultades;
	private List<Carrera> lpfListaCarreras;
	private List<PersonaDatosDto> lpfListaDocentes;
	private List<TipoFuncionCargaHoraria> lpfListaFunciones;
	private List<Integer> lpfListaHorasSemana;
	
	
	private List<Integer> lpfListaDocenteNivel ;
	private List<String> lpfListaDocenteProyecto ;
	private List<Integer> lpfListaDocenteHorasSemana ;
	private List<List<String>> lpfListaDetalleLectorProyecto ;
	private List<List<String>> lpfListaDetalleLectorProyectoEditar ;
	private List<CargaHoraria> lpfListaCargasHorarias;
	private List<DetalleCargaHoraria> lpfListaDetalleCargaHoraria; 
	
	private PersonaDatosDto lpfDocente;
	
	private DetalleCargaHoraria lpfDetalleCargaHorariaEditar;
	private DetalleCargaHoraria lpfDetalleCargaHorariaBuscar;
	private CargaHoraria lpfCargaHorariaBuscar;
	
	private TipoFuncionCargaHoraria lpfTipoFuncionCargaHorariaSeleccion;
	  
	private Integer lpfFacultadBuscar;
	private Integer lpfCarreraBuscar;
	private Integer lpfDocenteBuscar;
	private String lpfIdentificacionBuscar;
	
	private Integer lpfFuncionSeleccion;
	
	private Integer lpfNivel;
	private String lpfNombreProyecto;
	private Integer lpfHorasSemanaProyecto;
	
	private Integer lpfHorasSemanales;
	private Integer lpfTipo; // 0.- Editar , 1.-Nuevo
	private Integer lpfNumDetalle;
	private Boolean lpfBuscar; // 0.- Editar , 1.-Nuevo
	
	private PeriodoAcademico lpfPeriodoAcademico;
	private Integer lpfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> lpfListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto lpfPlanificacionCronograma;
	 
	private String lpfCedulaDocenteBusquedaAvanzada;
	private String lpfApellidoDocenteBusquedaAvanzada;
	private String lpfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> lpfListDocentesBusquedaAvanzada;
	
	//--v2
	//URL reporte
	private String lpfLinkReporte;
	
	//--v3
	private Integer lpfNumeroMaximo;
	
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
	DependenciaServicio servLpfDependenciaServicio;
	@EJB
	CarreraServicio servLpfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servLpfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servLpfPersonaServicio;
	@EJB
	TipoFuncionCargaHorariaServicio servLpfTipoFuncionCargaHorariaServicio;
	@EJB
	PeriodoAcademicoServicio servLpfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servLpfPlanificacionCronogramaDtoServicioJdbc; 
	@EJB
	CargaHorariaServicio servLpfCargaHorariaServicio;
	@EJB
	DetalleCargaHorariaServicio servLpfDetalleCargaHorariaServicio;
	@EJB
	UsuarioRolServicio servLpfUsuarioRolServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servLpfDocenteDatosDtoServicioJdbc;

	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/**
	 * Inicia los parametros de la funcionalidad
	 */
	@SuppressWarnings("rawtypes")
	private void inicarParametros() {
		lpfBuscar= false;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

		try {
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				validarCronograma();
				cagarPeriodos();
				lpfListaFacultades = servLpfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = lpfListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			}
			
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}


	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos LectorProyecto.
	 */
	public String irFormularioLectorProyecto(Usuario usuario) {
		lpfUsuario = usuario;
		lpfNumeroMaximo = 5;
		inicarParametros();
		return "irFormularioLectorProyecto";
	}
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos LectorProyecto.
	 */
	public String irAtras() {
		lpfNivel=null;
		lpfNombreProyecto=null;
		lpfHorasSemanaProyecto=null; 
		return "irFormularioLectorProyecto";
	}
	
	/**
	 * Editar los atributos de un DetalleCargaHoraria
	 * @param rol.- Entidad DetalleCargaHoraria
	 * @return Navegación a la pagina Editar
	 */
	public String irEditar(List<String> lista) {
		 
		lpfListaDetalleLectorProyectoEditar = new ArrayList<List<String>>();
		lpfListaDetalleLectorProyectoEditar.add(lista);
		lpfNivel = Integer.parseInt(lista.get(0));
		lpfNombreProyecto =  lista.get(1);;
		lpfHorasSemanaProyecto = Integer.parseInt(lista.get(2));;
		lpfTipo = new Integer(0);
		calcularHorasMaxMin();
		return "irEditarLectorProyecto";
	}
	
	/**
	 * Ir a crear un nuevo DetalleCargaHoraria
	 * @return navegacion a la ventana editar con atributos para crear nuevo
	 */
	public String irNuevo() {
		if(verificarDocente()){
			if(lpfNumDetalle<lpfNumeroMaximo){	
			lpfTipo = new Integer(1);
			calcularHorasMaxMin();
			return "irNuevoLectorProyecto";
			} else {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.ir.nuevo.numero.maximo.exception", lpfNumeroMaximo)));
				return null;
			}
		} else {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.ir.nuevo.docente.exception")));	
			return null;
		}
		
	}

	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		lpfListaFacultades = null;
		lpfListaCarreras = null;
		lpfListaDocentes = null;
		lpfListaFunciones = null;
		lpfDocente = null;
		lpfTipoFuncionCargaHorariaSeleccion = null;  
		lpfFacultadBuscar = null;
		lpfCarreraBuscar = null;
		lpfDocenteBuscar = null;
		lpfIdentificacionBuscar = null; 
		lpfFuncionSeleccion = null;
		lpfHorasSemanales = null; 
		lpfNumDetalle=null;
		lpfListaDetalleLectorProyecto = null;
		lpfBuscar= false;
		lpfPeriodoAcademicoBuscar = null;
		lpfListaPeriodosAcademicos = null;
		lpfLinkReporte = null;
		
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		lpfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		lpfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		lpfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
		lpfIdentificacionBuscar = null;
		lpfHorasSemanales = null;
		lpfListaFunciones = null;
		lpfListaCarreras = null;
		lpfListaFacultades = null;
		lpfListaDocentes = null; 
		lpfTipoFuncionCargaHorariaSeleccion = null;
		lpfDocente = null; 
		lpfListaDetalleLectorProyecto = null;
		lpfNumDetalle=0;
		lpfBuscar= false;
		lpfDetalleCargaHorariaBuscar = null;
		lpfDetalleCargaHorariaEditar = null;
		lpfCargaHorariaBuscar = null;
		lpfListaDetalleLectorProyecto = null;
		lpfLinkReporte = null;
		lpfPeriodoAcademico = verificarPeriodoActivo();
		inicarParametros();
		limpiarBusquedaAvanzada();
	}
	
	/**
	 * Setea y nulifica a los valores iniciales de la Información Docente
	 */
	public void limpiarInfoDocente(){
		
		lpfListaFunciones = null;
		lpfFuncionSeleccion = null;
		lpfTipoFuncionCargaHorariaSeleccion = null;
		lpfDocente = null; 
		lpfHorasSemanales = null;
		lpfBuscar= false;
		lpfLinkReporte = null;
		limpiarBusquedaAvanzada();
	}
	
	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		lpfCedulaDocenteBusquedaAvanzada = null;
		lpfApellidoDocenteBusquedaAvanzada = null;
		lpfListDocentesBusquedaAvanzada = null;
		lpfMensajeBusquedaAvanzada = null;
		lpfLinkReporte = null;
	}

	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			lpfNumDetalle=0;
			lpfHorasSemanales=0;
			lpfListaDetalleLectorProyecto = new ArrayList<List<String>>();
			lpfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
			
			try {
				limpiarInfoDocente();
				if (lpfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
					if (lpfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
						lpfPeriodoAcademico = servLpfPeriodoAcademicoServicio.buscarPorId(lpfPeriodoAcademicoBuscar);
						lpfDocente = servLpfPersonaDatosServicioJdbc.buscarPorId(lpfDocenteBuscar, lpfPeriodoAcademicoBuscar);
						lpfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+lpfDocente.getPrsIdentificacion()+"&prd="+lpfPeriodoAcademico.getPracDescripcion();
						listarFunciones();
						listarCargaHorariaDocente();
					} else {
						lpfDocente = null;
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.docente.validacion.exception")));
					} 
				} else {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.facultad.validacion.exception")));
				}
	
			} catch (DetalleCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.detalle.carga.horaria.no.encontrada.exception")));	
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar..detalle.carga.horaria.exception")));
			}catch (PersonaDatosDtoNoEncontradoException e) {
				 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.persona.datos.dto.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.persona.datos.dto.exception")));
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.periodo.academico.no.encontrado.exception")));
			} catch (PeriodoAcademicoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.periodo.academico.exception")));
			}
//		}
	}
	/**
	 * Carga informacion del docente seleccionado en el dialogo de busqueda avanzada
	 * @param item Item recibido con la infromacion del docente seleccionado
	 */
	public void asignarDocente( PersonaDatosDto item){
		
		lpfNumDetalle=0;
		lpfHorasSemanales=0;
		lpfListaDetalleLectorProyecto = new ArrayList<List<String>>();
		lpfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
		
		try {
			limpiarInfoDocente();
			
			lpfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfListaCarreras = null;
			lpfListaDocentes = null;
			
				lpfDocente = servLpfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , lpfPeriodoAcademicoBuscar);
				listarFunciones();
				listarCargaHorariaDocente();
				limpiarBusquedaAvanzada();
				lpfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+lpfPeriodoAcademico.getPracDescripcion();
				
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.asignar.docente.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.asignar.docente.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.asignar.docente.detalle.carga.horaria.exception")));
		}
		
	}
	
	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
		
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			lpfNumDetalle=0;
			lpfHorasSemanales=0;
			lpfListaDetalleLectorProyecto = new ArrayList<List<String>>();
			lpfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
			try {
				
				if(lpfCedulaDocenteBusquedaAvanzada == null){
					lpfCedulaDocenteBusquedaAvanzada = "";
				}
				if(lpfApellidoDocenteBusquedaAvanzada == null){
					lpfApellidoDocenteBusquedaAvanzada = "";
				}
				lpfListDocentesBusquedaAvanzada = servLpfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(lpfCedulaDocenteBusquedaAvanzada, lpfApellidoDocenteBusquedaAvanzada);
				
			} catch (PersonaDatosDtoException e) {
				lpfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.docente.exception"));
			} catch (PersonaDatosDtoNoEncontradoException e) {
				lpfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.buscar.docente.no.encontrado.exception"));
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
				if(lpfDetalleCargaHorariaEditar.getDtcrhrProyecto1() != null){
					cargarListaDetalleCargaHoraria();
					lpfDetalleCargaHorariaEditar.setDtcrhrEstadoEliminacion(1);
					cargarDetalleCargaHoraria(cargarCargaHoraria(lpfDocente, 
							lpfHorasSemanales, 
							lpfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
							lpfPeriodoAcademico, 
							lpfTipoFuncionCargaHorariaSeleccion), lpfDetalleCargaHorariaEditar, lpfHorasSemanales);
					limpiar();
				}
				else {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.guardar.null.pointer.exception")));
				}
			} else {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.guardar.docente.exception")));
			}
		}
	}
	
	/**
	 * nulifica la lista DetalleCargaHorariaEditar
	 **/
	public void limpiarLista(){
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto1(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto1(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto1Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto2(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto2(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto2Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto3(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto3(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto3Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto4(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto4(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto4Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto5(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto5(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto5Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto6(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto6(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto6Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto7(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto7(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto7Horas(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto8(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto8(null);
		lpfDetalleCargaHorariaEditar.setDtcrhrProyecto8Horas(null);
	}
	
	/**
	 * Guarda los cambios de los parametros del formulario
	 **/
	public void guardarCambios() {
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			limpiarLista();
			cargarListaDetalleCargaHoraria();
			
			if(lpfCargaHorariaBuscar != null && lpfDetalleCargaHorariaEditar != null){
				if(lpfDetalleCargaHorariaEditar.getDtcrhrProyecto1() != null){
					 actualizarDetalleCargaHoraria(lpfCargaHorariaBuscar,lpfDetalleCargaHorariaEditar, lpfHorasSemanales);	
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.guardar.cambios.con.exito.validacion")));
					limpiar();
				} else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.guardar.cambios.null.pointer.exception")));
				}
			} else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.guardar.cambios.exception")));
			}
		}
	}
	
	/**
	 * Elimina el registro de la base de datos
	 **/
	public void eliminar() {
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(lpfCargaHorariaBuscar != null && lpfDetalleCargaHorariaEditar != null){
				lpfDetalleCargaHorariaEditar.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
				lpfCargaHorariaBuscar.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
				actualizarDetalleCargaHoraria(lpfCargaHorariaBuscar,lpfDetalleCargaHorariaEditar, lpfHorasSemanales);
				limpiar();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.eliminar.con.exito.validacion")));
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.eliminar.exception")));
			}
		}
	}
	
	
	/**
	 * agrega los parametros del formulario
	 **/
	public String agregarALista() {
		
		if(lpfNivel!= null && lpfNombreProyecto!= null && lpfHorasSemanaProyecto != null  ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(lpfNumDetalle).add(String.valueOf(lpfNivel));
			lpfListaDetalleLectorProyecto.get(lpfNumDetalle).add(GeneralesUtilidades.eliminarEspaciosEnBlanco(lpfNombreProyecto).toUpperCase());
			lpfListaDetalleLectorProyecto.get(lpfNumDetalle).add(String.valueOf(lpfHorasSemanaProyecto));
			lpfNumDetalle++;
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.agregar.a.lista.con.exito.validacion")));
			cargarListaDetalleCargaHoraria();
			return irAtras();
		} else{
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.agregar.a.lista.exception")));
			return null;
		}
	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public String editarALista() {
		String retornar=null;
		for(int i=0;i<lpfListaDetalleLectorProyecto.size();i++){
			if(lpfListaDetalleLectorProyecto.get(i).get(0).equals(lpfListaDetalleLectorProyectoEditar.get(0).get(0)) && lpfListaDetalleLectorProyecto.get(i).get(1).equals(lpfListaDetalleLectorProyectoEditar.get(0).get(1)) && lpfListaDetalleLectorProyecto.get(i).get(2).equals(lpfListaDetalleLectorProyectoEditar.get(0).get(2))){
				lpfListaDetalleLectorProyecto.get(i).set(0, String.valueOf(lpfNivel));
				lpfListaDetalleLectorProyecto.get(i).set(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(lpfNombreProyecto).toUpperCase());
				lpfListaDetalleLectorProyecto.get(i).set(2, String.valueOf(lpfHorasSemanaProyecto));
				retornar = irAtras();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.editar.a.lista.con.exito.validacion")));
				cargarListaDetalleCargaHoraria();
			}
		}
		return retornar;
	}

	/**
	 * Elimina el registro de la lista
	 **/
	public void eliminarDeLista(ArrayList<List<String>> lista) {
		for(int i=0;i<lpfListaDetalleLectorProyecto.size();i++){
			if(lpfListaDetalleLectorProyecto.get(i).get(0).equals(lista.get(0)) && lpfListaDetalleLectorProyecto.get(i).get(1).equals(lista.get(1)) && lpfListaDetalleLectorProyecto.get(i).get(2).equals(lista.get(2))){
				lpfListaDetalleLectorProyecto.remove(i);
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.eliminar.de.lista.con.exito.validacion")));
			}
		}
		lpfNumDetalle--;
		cargarListaDetalleCargaHoraria();
	}
	
	
	
	/**
	 * Calcula las horas en examen complexivo/ reactivos en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){
		try {
			lpfTipoFuncionCargaHorariaSeleccion = servLpfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_DIRECCION_TUTORIA_TITULACION_VALUE);
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.calcular.horas.no.encontrado.exception")));
			
		} catch (TipoFuncionCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.calcular.horas.exception")));
			
		}
	}
	
	/**
	 * Carga los datos y atributos de CARGA_HORARIA sobre el docente
	 * @param docente Docente al que se agrega carga horaria
	 * @param horasSemanales Numero de horas semanales asignadas al docente
	 * @param observacion Observacion descripcion de la carga asignada
	 * @param periodo Periodo Academico actual 
	 * @param tipo tipo funcion carga horaria a asignar
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
			
			crhrResult = servLpfCargaHorariaServicio.anadir(crhr);
			 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.cargar.carga.horaria.con.exito.validacion", observacion)));
			
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.cargar.carga.horaria.validacion.exception")));
			
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.cargar.carga.horaria.exception")));
		}
		
		return crhrResult;
	}
	
	/**
	 * Carga los campos de carga horaria del dcente
	 * @throws DetalleCargaHorariaException 
	 * @throws DetalleCargaHorariaNoEncontradoException 
	 */
	public void listarCargaHorariaDocente() throws DetalleCargaHorariaNoEncontradoException, DetalleCargaHorariaException{ 
		try { 
			//Coordinador de Titulacion
			lpfListaCargasHorarias =  servLpfCargaHorariaServicio.buscarPorDetallePuesto(lpfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_LECTOR_PROYECTO, lpfPeriodoAcademico.getPracId());
			if(lpfListaCargasHorarias.size() > 0 ){
				for(CargaHoraria item: lpfListaCargasHorarias ){
					lpfCargaHorariaBuscar = item;
						lpfListaDetalleCargaHoraria = servLpfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(lpfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: lpfListaDetalleCargaHoraria){
								 lpfDetalleCargaHorariaBuscar = itemDetalle ;
								 lpfDetalleCargaHorariaEditar = itemDetalle;
								 buscarDetalleCargaHoraria();
							}
							lpfBuscar = true;
						}
				}
			}
			 				
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.listar.carga.horaria.docente.no.encontrado.exception")));
			
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.listar.carga.horaria.docente.exception")));
		}
	}
	
	/**
	 * Carga el detalle de la carga horaia para Dirección de Titulación en la tabla DETALLE_CARGA_HORARIA
	 * @param cargaHoraria Entidad a insertar en el detalle
	 * @param detalleCargaHoraria Entidad a insertar en el detalle
	 * @param horas número de horas semanales
	 **/
	public void actualizarDetalleCargaHoraria(CargaHoraria cargaHoraria, DetalleCargaHoraria detalleCargaHoraria, Integer horas){
		
		try {		
			
			cargaHoraria.setCrhrNumHoras(horas);
			servLpfCargaHorariaServicio.editar(cargaHoraria);
			
			DetalleCargaHoraria dtcrhr = detalleCargaHoraria;
			dtcrhr.setDtcrhrNumHoras(horas);
			dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
			servLpfDetalleCargaHorariaServicio.editar(dtcrhr);
			
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.actualizar.carga.horaria.validacion.exception")));
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.actualizar.carga.horaria.exception")));
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.actualizar.detalle.carga.horaria.validacion.exception")));	
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.actualizar.detalle.carga.horaria.exception")));
		} 
	}
	
	
	/**
	 * Carga el detalle de la carga horaia para clinicas y practicas en la tabla DETALLE_CARGA_HORARIA
	 * @param cargaHoraria Entidad a insertar en el detalle
	 * @param detalleCargaHoraria Entidad a insertar en el detalle
	 **/
	public void cargarDetalleCargaHoraria(CargaHoraria cargaHoraria, DetalleCargaHoraria detalleCargaHoraria, Integer horas){
		
		try {
			
			DetalleCargaHoraria dtcrhr = detalleCargaHoraria;
			dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
			dtcrhr.setDtcrhrNumHoras(horas);
			dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			
			servLpfDetalleCargaHorariaServicio.anadir(dtcrhr);
			
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.cargar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.cargar.detalle.carga.horaria.exception")));
		}
	}

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------


	/**
	 * Lista de Entidades Carrera por facultad id
	 **/
	public void listarCarreras() {
		
		try {	
			limpiarInfoDocente();
			if(lpfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				lpfListaCarreras = servLpfCarreraServicio.listarCarrerasXFacultad(lpfFacultadBuscar);
				lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				lpfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				lpfListaDocentes = null;
				listarDocentesPorFacultad();
			}else{
				lpfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				lpfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfListaDocentes = null;
			lpfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			lpfListaDocentes = servLpfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(lpfFacultadBuscar, lpfPeriodoAcademicoBuscar);
			lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			lpfListaDocentes = servLpfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(lpfCarreraBuscar, lpfPeriodoAcademicoBuscar);
			lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
		}
	}
	
	/**
	 * Lista de funciones
	 **/
	public void listarFunciones() {
 
			try {
				lpfListaFunciones = servLpfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_OTRAS_ASIGNACIONES_VALUE);
				calcularHorasMaxMin();
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.listar.funciones.exception")));
			}
			
	}
	
 
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	/**
	 * Calcula la hora max y min para la carga horaria
	 **/
	public Boolean calcularHorasMaxMin(){
		
		Boolean verificarFuncion = false;
		
		try {
			if(lpfDocente != null){
				switch (lpfDocente.getTmddId()) {
				case 3://Tiempo Completo
					verificarFuncion = true;
					break;
				case 2://Medio Tiempo
					verificarFuncion = true;
					break;
				default:
					verificarFuncion = false;
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.calcular.horas.max.min.complementaria.exception")));
					break;
				}
				
				if(verificarFuncion){
					lpfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_LECTOR_PROYECTO;
					lpfTipoFuncionCargaHorariaSeleccion = servLpfTipoFuncionCargaHorariaServicio.buscarPorId(lpfFuncionSeleccion);
					lpfListaHorasSemana = new ArrayList<Integer>();
					lpfListaHorasSemana.add(lpfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas());
					lpfHorasSemanaProyecto = lpfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
				}
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.calcular.horas.max.min.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.calcular.horas.max.min.exception")));
		}
		
		return verificarFuncion;
	}
	
	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(lpfDocente!=null){
			verificar = true;
		}
		return verificar;
	}
	
	/**
	 * carga la entidad DetalleCargaHoraria
	 */
	public void cargarListaDetalleCargaHoraria(){
		
		lpfHorasSemanales=0;
		
		if(1<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto1(Integer.parseInt(lpfListaDetalleLectorProyecto.get(0).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto1(lpfListaDetalleLectorProyecto.get(0).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto1Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(0).get(2)));
			lpfHorasSemanales= Integer.parseInt(lpfListaDetalleLectorProyecto.get(0).get(2)) + lpfHorasSemanales;
		}
		if(2<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto2(Integer.parseInt(lpfListaDetalleLectorProyecto.get(1).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto2(lpfListaDetalleLectorProyecto.get(1).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto2Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(1).get(2)));
			lpfHorasSemanales= Integer.parseInt(lpfListaDetalleLectorProyecto.get(1).get(2)) + lpfHorasSemanales;
		}
		if(3<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto3(Integer.parseInt(lpfListaDetalleLectorProyecto.get(2).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto3(lpfListaDetalleLectorProyecto.get(2).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto3Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(2).get(2)));
			lpfHorasSemanales=Integer.parseInt(lpfListaDetalleLectorProyecto.get(2).get(2)) + lpfHorasSemanales;
		}
		if(4<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto4(Integer.parseInt(lpfListaDetalleLectorProyecto.get(3).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto4(lpfListaDetalleLectorProyecto.get(3).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto4Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(3).get(2)));
			lpfHorasSemanales=Integer.parseInt(lpfListaDetalleLectorProyecto.get(3).get(2)) + lpfHorasSemanales;
		}
		if(5<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto5(Integer.parseInt(lpfListaDetalleLectorProyecto.get(4).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto5(lpfListaDetalleLectorProyecto.get(4).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto5Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(4).get(2)));
			lpfHorasSemanales=Integer.parseInt(lpfListaDetalleLectorProyecto.get(4).get(2)) + lpfHorasSemanales;
		}
		if(6<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto6(Integer.parseInt(lpfListaDetalleLectorProyecto.get(5).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto6(lpfListaDetalleLectorProyecto.get(5).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto6Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(5).get(2)));
			lpfHorasSemanales=Integer.parseInt(lpfListaDetalleLectorProyecto.get(5).get(2)) + lpfHorasSemanales;
		}
		if(7<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto7(Integer.parseInt(lpfListaDetalleLectorProyecto.get(6).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto7(lpfListaDetalleLectorProyecto.get(6).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto7Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(6).get(2)));
			lpfHorasSemanales=Integer.parseInt(lpfListaDetalleLectorProyecto.get(6).get(2)) + lpfHorasSemanales;
		}
		if(8<=lpfListaDetalleLectorProyecto.size()){
			lpfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto8(Integer.parseInt(lpfListaDetalleLectorProyecto.get(7).get(0))); 
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto8(lpfListaDetalleLectorProyecto.get(7).get(1).toUpperCase());
			lpfDetalleCargaHorariaEditar.setDtcrhrProyecto8Horas(Integer.parseInt(lpfListaDetalleLectorProyecto.get(7).get(2)));
			lpfHorasSemanales=Integer.parseInt(lpfListaDetalleLectorProyecto.get(7).get(2)) + lpfHorasSemanales;
		}
		
		if(lpfHorasSemanales>14){
			lpfHorasSemanales=14;
		}
	}
	
	/**
	 * busca la entidad DetalleCargaHoraria
	 */
	public void buscarDetalleCargaHoraria(){
		
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto1()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto1()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto1Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(0).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto1()));
			lpfListaDetalleLectorProyecto.get(0).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto1());
			lpfListaDetalleLectorProyecto.get(0).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto1Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto2()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto2()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto2Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(1).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto2()));
			lpfListaDetalleLectorProyecto.get(1).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto2());
			lpfListaDetalleLectorProyecto.get(1).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto2Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto3()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto3()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto3Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(2).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto3()));
			lpfListaDetalleLectorProyecto.get(2).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto3());
			lpfListaDetalleLectorProyecto.get(2).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto3Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto4()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto4()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto4Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(3).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto4()));
			lpfListaDetalleLectorProyecto.get(3).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto4());
			lpfListaDetalleLectorProyecto.get(3).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto4Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto5()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto5()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto5Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(4).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto5()));
			lpfListaDetalleLectorProyecto.get(4).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto5());
			lpfListaDetalleLectorProyecto.get(4).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto5Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto6()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto6()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto6Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(5).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto6()));
			lpfListaDetalleLectorProyecto.get(5).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto6());
			lpfListaDetalleLectorProyecto.get(5).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto6Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto7()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto7()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto7Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(6).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto7()));
			lpfListaDetalleLectorProyecto.get(6).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto7());
			lpfListaDetalleLectorProyecto.get(6).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto7Horas()));
			lpfNumDetalle++;
		}
		if(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto8()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto8()!=null && lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto8Horas()!=null ){
			lpfListaDetalleLectorProyecto.add(new ArrayList<String>());
			lpfListaDetalleLectorProyecto.get(7).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto8()));
			lpfListaDetalleLectorProyecto.get(7).add(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto8());
			lpfListaDetalleLectorProyecto.get(7).add(String.valueOf(lpfDetalleCargaHorariaBuscar.getDtcrhrProyecto8Horas()));
			lpfNumDetalle++;
		}
		cargarListaDetalleCargaHoraria();
	}	


	/**
	 * Busca si existe un periodo activo
	 * @return retorna la entidad PeriodoAcademico activo en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoActivo(){
		
		try {
			return servLpfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.verificar.periodo.activo.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.verificar.periodo.activo.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo activo
	 * @return retorna la entidad PeriodoAcademico activo en pregrado
	 **/
	public PlanificacionCronogramaDto verificarCronograma(){
		
		try {
			return servLpfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.verificar.cronograma.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){

		try {
		return servLpfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
		return null;
		} catch (PeriodoAcademicoException e) {
		return null;
		}
	} 
	
	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		lpfPlanificacionCronograma = verificarCronograma();
		if(lpfPlanificacionCronograma != null){
			if(lpfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(lpfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("LectorProyecto.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}
	
	
	/**
	 * Lista de Entidades Dependencia al seleccionar un peridoso
	 **/
	public void seleccionarPeriodo() {
			limpiarInfoDocente(); 
			lpfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfListaDocentes = null;
			lpfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			lpfListaCarreras = null; 
	}
	
	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		lpfListaPeriodosAcademicos = new ArrayList<>();

		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
			lpfListaPeriodosAcademicos.add(pracActivo);
			lpfPeriodoAcademico = pracActivo;
			lpfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}

		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
			lpfListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	public void generarReporteCargaHorariaDocente(){
		if (lpfDocente != null) {
			cargarAsignacionesCargaHoraria(lpfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(lpfPeriodoAcademico.getPracId(),lpfPeriodoAcademico.getPracDescripcion()), lpfUsuario, lpfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}

	
	
	
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public List<Dependencia> getLpfListaFacultades() {
		return lpfListaFacultades;
	}

	public void setLpfListaFacultades(List<Dependencia> lpfListaFacultades) {
		this.lpfListaFacultades = lpfListaFacultades;
	}

	public List<Carrera> getLpfListaCarreras() {
		return lpfListaCarreras;
	}

	public void setLpfListaCarreras(List<Carrera> lpfListaCarreras) {
		this.lpfListaCarreras = lpfListaCarreras;
	}

	public List<PersonaDatosDto> getLpfListaDocentes() {
		return lpfListaDocentes;
	}

	public void setLpfListaDocentes(List<PersonaDatosDto> lpfListaDocentes) {
		this.lpfListaDocentes = lpfListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getLpfListaFunciones() {
		return lpfListaFunciones;
	}

	public void setLpfListaFunciones(List<TipoFuncionCargaHoraria> lpfListaFunciones) {
		this.lpfListaFunciones = lpfListaFunciones;
	}

	public PersonaDatosDto getLpfDocente() {
		return lpfDocente;
	}

	public void setLpfDocente(PersonaDatosDto lpfDocente) {
		this.lpfDocente = lpfDocente;
	}

	public Integer getLpfFacultadBuscar() {
		return lpfFacultadBuscar;
	}

	public void setLpfFacultadBuscar(Integer lpfFacultadBuscar) {
		this.lpfFacultadBuscar = lpfFacultadBuscar;
	}

	public Integer getLpfCarreraBuscar() {
		return lpfCarreraBuscar;
	}

	public void setLpfCarreraBuscar(Integer lpfCarreraBuscar) {
		this.lpfCarreraBuscar = lpfCarreraBuscar;
	}

	public Integer getLpfDocenteBuscar() {
		return lpfDocenteBuscar;
	}

	public void setLpfDocenteBuscar(Integer lpfDocenteBuscar) {
		this.lpfDocenteBuscar = lpfDocenteBuscar;
	}

	public String getLpfIdentificacionBuscar() {
		return lpfIdentificacionBuscar;
	}

	public void setLpfIdentificacionBuscar(String lpfIdentificacionBuscar) {
		this.lpfIdentificacionBuscar = lpfIdentificacionBuscar;
	}

	public Integer getLpfFuncionSeleccion() {
		return lpfFuncionSeleccion;
	}

	public void setLpfFuncionSeleccion(Integer lpfFuncionSeleccion) {
		this.lpfFuncionSeleccion = lpfFuncionSeleccion;
	}

	public Integer getLpfHorasSemanales() {
		return lpfHorasSemanales;
	}

	public void setLpfHorasSemanales(Integer lpfHorasSemanales) {
		this.lpfHorasSemanales = lpfHorasSemanales;
	}

	public DetalleCargaHoraria getLpfDetalleCargaHorariaEditar() {
		return lpfDetalleCargaHorariaEditar;
	}

	public void setLpfDetalleCargaHorariaEditar(DetalleCargaHoraria lpfDetalleCargaHorariaEditar) {
		this.lpfDetalleCargaHorariaEditar = lpfDetalleCargaHorariaEditar;
	}

	public Integer getLpfTipo() {
		return lpfTipo;
	}

	public void setLpfTipo(Integer lpfTipo) {
		this.lpfTipo = lpfTipo;
	}

	public List<Integer> getLpfListaHorasSemana() {
		return lpfListaHorasSemana;
	}

	public void setLpfListaHorasSemana(List<Integer> lpfListaHorasSemana) {
		this.lpfListaHorasSemana = lpfListaHorasSemana;
	}

	public Integer getLpfNivel() {
		return lpfNivel;
	}

	public void setLpfNivel(Integer lpfNivel) {
		this.lpfNivel = lpfNivel;
	}

	public String getLpfNombreProyecto() {
		return lpfNombreProyecto;
	}

	public void setLpfNombreProyecto(String lpfNombreProyecto) {
		this.lpfNombreProyecto = lpfNombreProyecto;
	}

	public Integer getLpfHorasSemanaProyecto() {
		return lpfHorasSemanaProyecto;
	}

	public void setLpfHorasSemanaProyecto(Integer lpfHorasSemanaProyecto) {
		this.lpfHorasSemanaProyecto = lpfHorasSemanaProyecto;
	}

	public List<Integer> getLpfListaDocenteNivel() {
		return lpfListaDocenteNivel;
	}

	public void setLpfListaDocenteNivel(List<Integer> lpfListaDocenteNivel) {
		this.lpfListaDocenteNivel = lpfListaDocenteNivel;
	}

	public List<String> getLpfListaDocenteProyecto() {
		return lpfListaDocenteProyecto;
	}

	public void setLpfListaDocenteProyecto(List<String> lpfListaDocenteProyecto) {
		this.lpfListaDocenteProyecto = lpfListaDocenteProyecto;
	}

	public List<Integer> getLpfListaDocenteHorasSemana() {
		return lpfListaDocenteHorasSemana;
	}

	public void setLpfListaDocenteHorasSemana(List<Integer> lpfListaDocenteHorasSemana) {
		this.lpfListaDocenteHorasSemana = lpfListaDocenteHorasSemana;
	}

	public List<List<String>> getLpfListaDetalleLectorProyecto() {
		return lpfListaDetalleLectorProyecto;
	}

	public void setLpfListaDetalleLectorProyecto(List<List<String>> lpfListaDetalleLectorProyecto) {
		this.lpfListaDetalleLectorProyecto = lpfListaDetalleLectorProyecto;
	}

	public List<List<String>> getLpfListaDetalleLectorProyectoEditar() {
		return lpfListaDetalleLectorProyectoEditar;
	}

	public void setLpfListaDetalleLectorProyectoEditar(List<List<String>> lpfListaDetalleLectorProyectoEditar) {
		this.lpfListaDetalleLectorProyectoEditar = lpfListaDetalleLectorProyectoEditar;
	}


	public Boolean getLpfBuscar() {
		return lpfBuscar;
	}

	public void setLpfBuscar(Boolean lpfBuscar) {
		this.lpfBuscar = lpfBuscar;
	}


	public Integer getLpfPeriodoAcademicoBuscar() {
		return lpfPeriodoAcademicoBuscar;
	}


	public void setLpfPeriodoAcademicoBuscar(Integer lpfPeriodoAcademicoBuscar) {
		this.lpfPeriodoAcademicoBuscar = lpfPeriodoAcademicoBuscar;
	}


	public List<PeriodoAcademico> getLpfListaPeriodosAcademicos() {
		return lpfListaPeriodosAcademicos;
	}


	public void setLpfListaPeriodosAcademicos(List<PeriodoAcademico> lpfListaPeriodosAcademicos) {
		this.lpfListaPeriodosAcademicos = lpfListaPeriodosAcademicos;
	}


	public String getLpfCedulaDocenteBusquedaAvanzada() {
		return lpfCedulaDocenteBusquedaAvanzada;
	}


	public void setLpfCedulaDocenteBusquedaAvanzada(String lpfCedulaDocenteBusquedaAvanzada) {
		this.lpfCedulaDocenteBusquedaAvanzada = lpfCedulaDocenteBusquedaAvanzada;
	}


	public String getLpfApellidoDocenteBusquedaAvanzada() {
		return lpfApellidoDocenteBusquedaAvanzada;
	}


	public void setLpfApellidoDocenteBusquedaAvanzada(String lpfApellidoDocenteBusquedaAvanzada) {
		this.lpfApellidoDocenteBusquedaAvanzada = lpfApellidoDocenteBusquedaAvanzada;
	}


	public String getLpfMensajeBusquedaAvanzada() {
		return lpfMensajeBusquedaAvanzada;
	}


	public void setLpfMensajeBusquedaAvanzada(String lpfMensajeBusquedaAvanzada) {
		this.lpfMensajeBusquedaAvanzada = lpfMensajeBusquedaAvanzada;
	}


	public List<PersonaDatosDto> getLpfListDocentesBusquedaAvanzada() {
		return lpfListDocentesBusquedaAvanzada;
	}
 
	public void setLpfListDocentesBusquedaAvanzada(List<PersonaDatosDto> lpfListDocentesBusquedaAvanzada) {
		this.lpfListDocentesBusquedaAvanzada = lpfListDocentesBusquedaAvanzada;
	}
 
	public String getLpfLinkReporte() {
		return lpfLinkReporte;
	}
  
	public void setLpfLinkReporte(String lpfLinkReporte) {
		this.lpfLinkReporte = lpfLinkReporte;
	}


	public Usuario getLpfUsuario() {
		return lpfUsuario;
	}


	public void setLpfUsuario(Usuario lpfUsuario) {
		this.lpfUsuario = lpfUsuario;
	}
 
	
}