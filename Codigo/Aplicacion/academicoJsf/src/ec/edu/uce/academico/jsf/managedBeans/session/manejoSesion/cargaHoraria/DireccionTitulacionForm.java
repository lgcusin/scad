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
   
 ARCHIVO:     DireccionTitulacionForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de proyectos DireccionTitulacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Carlos Roca 			              Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

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
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
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
 * Clase (session bean) DireccionTitulacionForm.java Bean de sesión que maneja
 * los atributos de proyectos DireccionTitulacion.
 * 
 * @author caroca.
 * @version 1.0
 */

@ManagedBean(name = "direccionTitulacionForm")
@SessionScoped
public class DireccionTitulacionForm  extends ReporteCargaHorariaForm  implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
 
	private Usuario dtfUsuario;

	private List<Dependencia> dtfListaFacultades;
	private List<Carrera> dtfListaCarreras;
	private List<PersonaDatosDto> dtfListaDocentes;
	private List<TipoFuncionCargaHoraria> dtfListaFunciones;
	private List<Integer> dtfListaHorasSemana;
	
	
	private List<Integer> dtfListaDocenteNivel ;
	private List<String> dtfListaDocenteProyecto ;
	private List<Integer> dtfListaDocenteHorasSemana ;
	private List<List<String>> dtfListaDetalleDireccionTitulacion ;
	private List<List<String>> dtfListaDetalleDireccionTitulacionEditar ;
	private List<CargaHoraria> dtfListaCargasHorarias;
	private List<DetalleCargaHoraria> dtfListaDetalleCargaHoraria; 
	private List<SelectItem> dtfListTipoProyecto;
	
	
	private PersonaDatosDto dtfDocente;
	
	private DetalleCargaHoraria dtfDetalleCargaHorariaEditar;
	private DetalleCargaHoraria dtfDetalleCargaHorariaBuscar;
	private CargaHoraria dtfCargaHorariaBuscar;
	
	private TipoFuncionCargaHoraria dtfTipoFuncionCargaHorariaSeleccion;
	  
	private Integer dtfFacultadBuscar;
	private Integer dtfCarreraBuscar;
	private Integer dtfDocenteBuscar;
	private String dtfIdentificacionBuscar;
	
	private Integer dtfFuncionSeleccion;
	private Integer dtfHorasMax;
	private Integer dtfHorasMin;
	
	private Integer dtfTipoProyecto;
	private Integer dtfNivel;
	private String dtfNombreProyecto;
	private Integer dtfHorasPorProyecto;
	private Integer dtfHorasSemanaProyecto;
	
	private Integer dtfHorasSemanales;
	private Integer dtfTipo; // 0.- Editar , 1.-Nuevo
	private Integer dtfNumDetalle;
	private Boolean dtfBuscar; // 0.- Editar , 1.-Nuevo
	
	private PeriodoAcademico dtfPeriodoAcademico;
	private Integer dtfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> dtfListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto dtfPlanificacionCronograma;
	 
	private String dtfCedulaDocenteBusquedaAvanzada;
	private String dtfApellidoDocenteBusquedaAvanzada;
	private String dtfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> dtfListDocentesBusquedaAvanzada;
	
	//--v2
	//URL reporte
	private String dtfLinkReporte;

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB	DependenciaServicio servDtfDependenciaServicio;
	@EJB	CarreraServicio servDtfCarreraServicio;
	@EJB	PersonaDatosDtoServicioJdbc servDtfPersonaDatosServicioJdbc;
	@EJB	PersonaServicio servDtfPersonaServicio;
	@EJB	TipoFuncionCargaHorariaServicio servDtfTipoFuncionCargaHorariaServicio;
	@EJB	PeriodoAcademicoServicio servDtfPeriodoAcademicoServicio;
	@EJB	PlanificacionCronogramaDtoServicioJdbc servDtfPlanificacionCronogramaDtoServicioJdbc; 
	@EJB	CargaHorariaServicio servDtfCargaHorariaServicio;
	@EJB	DetalleCargaHorariaServicio servDtfDetalleCargaHorariaServicio;
	@EJB	UsuarioRolServicio servDtfUsuarioRolServicio;
	@EJB	DocenteDatosDtoServicioJdbc servDtfDocenteDatosDtoServicioJdbc;

	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/**
	 * Inicia los parametros de la funcionalidad
	 */
	@SuppressWarnings("rawtypes")
	private void inicarParametros() {
		dtfBuscar= false;
		dtfListaDetalleCargaHoraria = null;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		try {
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso
				validarCronograma();
				cagarPeriodos();
				dtfListaFacultades = servDtfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator it = dtfListaFacultades.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
						it.remove();
					}
				}
//			} 
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.iniciar.parametros.dependencia.no.encontrado.exception")));
		}
	}


	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos DireccionTitulacion.
	 */
	public String irFormularioDireccionTitulacion(Usuario usuario) {
		dtfUsuario = usuario;
		inicarParametros();
		return "irFormularioDireccionTitulacion";
	}
	
	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos DireccionTitulacion.
	 */
	public String irAtras() {
		dtfNivel=null;
		dtfNombreProyecto=null;
		dtfHorasPorProyecto = null;
		return "irFormularioDireccionTitulacion";
	}
	
	/**
	 * Editar los atributos de un DetalleCargaHoraria
	 * @param rol.- Entidad DetalleCargaHoraria
	 * @return Navegación a la pagina Editar
	 */
	public String irEditar(List<String> lista) {
		 
		dtfListaDetalleDireccionTitulacionEditar = new ArrayList<List<String>>();
		dtfListaDetalleDireccionTitulacionEditar.add(lista);
		dtfNivel = Integer.parseInt(lista.get(0));
		dtfNombreProyecto =  lista.get(1);;
		dtfHorasSemanaProyecto = Integer.parseInt(lista.get(2));;
		dtfTipo = new Integer(0);
		calcularHorasMaxMin();
		return "irEditarDireccionTitulacion";
	}
	
	public String irAsignarProyectos() {
		dtfNivel = Integer.valueOf(0);
		dtfNombreProyecto = String.valueOf("");
		dtfHorasPorProyecto = Integer.valueOf(0);
		return "irEditarDireccionTitulacion";
	}
	
	/**
	 * Ir a crear un nuevo DetalleCargaHoraria
	 * @return navegacion a la ventana editar con atributos para crear nuevo
	 */
	public String irNuevo() {
		if(verificarDocente()){
			if(dtfNumDetalle<8){	
			dtfTipo = new Integer(1);
			calcularHorasMaxMin();
			return "irNuevoDireccionTitulacion";
			} else {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.ir.nuevo.numero.maximo.exception")));
				return null;
			}
		} else {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.ir.nuevo.docente.exception")));	
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
		
		dtfListaFacultades = null;
		dtfListaCarreras = null;
		dtfListaDocentes = null;
		dtfListaFunciones = null;
		dtfDocente = null;
		dtfTipoFuncionCargaHorariaSeleccion = null;  
		dtfFacultadBuscar = null;
		dtfCarreraBuscar = null;
		dtfDocenteBuscar = null;
		dtfIdentificacionBuscar = null; 
		dtfFuncionSeleccion = null;
		dtfHorasSemanales = null; 
		dtfNumDetalle=null;
		dtfListaDetalleDireccionTitulacion = null;
		dtfBuscar= false;
		dtfPeriodoAcademicoBuscar = null;
		dtfListaPeriodosAcademicos = null;
		dtfLinkReporte = null;
		dtfListaHorasSemana = null;
		
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		dtfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		dtfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		dtfFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
		dtfIdentificacionBuscar = null;
		dtfHorasSemanales = null;
		dtfListaFunciones = null;
		dtfListaCarreras = null;
		dtfListaFacultades = null;
		dtfListaDocentes = null; 
		dtfTipoFuncionCargaHorariaSeleccion = null;
		dtfDocente = null; 
		dtfListaDetalleDireccionTitulacion = null;
		dtfNumDetalle=0;
		dtfBuscar= false;
		dtfDetalleCargaHorariaBuscar = null;
		dtfDetalleCargaHorariaEditar = null;
		dtfCargaHorariaBuscar = null;
		dtfListaDetalleDireccionTitulacion = null;
		dtfLinkReporte = null;
		dtfPeriodoAcademico = verificarPeriodoActivo();
		inicarParametros();
		limpiarBusquedaAvanzada();
		dtfListaDetalleCargaHoraria = null;
		
		dtfListaHorasSemana = null;
		dtfNombreProyecto = null;
	}
	
	/**
	 * Setea y nulifica a los valores iniciales de la Información Docente
	 */
	public void limpiarInfoDocente(){
		
		dtfListaFunciones = null;
		dtfListaHorasSemana = null;
		dtfNombreProyecto = new String();
		dtfTipoFuncionCargaHorariaSeleccion = null;
		dtfDocente = null; 
		dtfHorasSemanales = null;
		dtfListaHorasSemana = null;
		dtfListaFunciones = null;
		dtfBuscar= false;
		dtfLinkReporte = null;
		dtfHorasSemanaProyecto = null;
		dtfListaDetalleCargaHoraria = null;
		limpiarBusquedaAvanzada();
	}
	
	/**
	 * Limpia parametros del cuadro de dialogo busqueda avanzada
	 */
	public void limpiarBusquedaAvanzada(){
		dtfCedulaDocenteBusquedaAvanzada = null;
		dtfApellidoDocenteBusquedaAvanzada = null;
		dtfListDocentesBusquedaAvanzada = null;
		dtfMensajeBusquedaAvanzada = null;
		dtfLinkReporte = null;
	}

	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		
		try {
			limpiarInfoDocente();
			if (dtfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
				if (dtfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
					dtfPeriodoAcademico = servDtfPeriodoAcademicoServicio.buscarPorId(dtfPeriodoAcademicoBuscar);
					dtfDocente = servDtfPersonaDatosServicioJdbc.buscarPorId(dtfDocenteBuscar , dtfPeriodoAcademicoBuscar);
					
					//GENERAR REPORTE DESDE EL USAURIO ADMINISTRADOR DIRECTOR DE CARRERA
					dtfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+dtfDocente.getPrsIdentificacion()+"&prd="+dtfPeriodoAcademico.getPracDescripcion();
					
					//CARGA FUNCIONES QUE TIENE ACCESO EL DOCENTE
					verificarAcceso();
					
					//CARGAR LA CAHR DEL DOCENTE
					listarCargaHorariaDocente();
					
					dtfListTipoProyecto = new ArrayList<>();
					dtfListTipoProyecto.add(new SelectItem(1, "TUTOR"));
					dtfListTipoProyecto.add(new SelectItem(2, "LECTOR"));
				} else {
					dtfDocente = null;
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.docente.validacion.exception")));
				} 
			} else {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.facultad.validacion.exception")));
			}

		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.detalle.carga.horaria.no.encontrada.exception")));	
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar..detalle.carga.horaria.exception")));
		}catch (PersonaDatosDtoNoEncontradoException e) {
			 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.persona.datos.dto.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.persona.datos.dto.exception")));
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.periodo.academico.no.encontrado.exception")));
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.periodo.academico.exception")));
		}
		
		
		
		
		
		
		
		
		
		
		
		
		/**
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017	
		
			dtfNumDetalle=0;
			dtfHorasSemanales=0;
			dtfListaDetalleDireccionTitulacion = new ArrayList<List<String>>();
			dtfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
			
			try {
				limpiarInfoDocente();
				if (dtfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
					if (dtfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
						dtfPeriodoAcademico = servDtfPeriodoAcademicoServicio.buscarPorId(dtfPeriodoAcademicoBuscar);
						dtfDocente = servDtfPersonaDatosServicioJdbc.buscarPorId(dtfDocenteBuscar);
						dtfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+dtfDocente.getPrsIdentificacion()+"&prd="+dtfPeriodoAcademico.getPracDescripcion();
						verificarAcceso();
						listarCargaHorariaDocente();		
					} else {
						dtfDocente = null;
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.docente.validacion.exception")));
					} 
				} else {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.facultad.validacion.exception")));
				}
	
			} catch (DetalleCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.detalle.carga.horaria.no.encontrada.exception")));	
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar..detalle.carga.horaria.exception")));
			}catch (PersonaDatosDtoNoEncontradoException e) {
				 FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.persona.datos.dto.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.persona.datos.dto.exception")));
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.periodo.academico.no.encontrado.exception")));
			} catch (PeriodoAcademicoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.periodo.academico.exception")));
			}
		}*/
	}
	
	/**
	 * Carga informacion del docente seleccionado en el dialogo de busqueda avanzada
	 * @param item Item recibido con la infromacion del docente seleccionado
	 */
	public void asignarDocente(PersonaDatosDto item){
		
		dtfNumDetalle=0;
		dtfHorasSemanales=0;
		dtfListaDetalleDireccionTitulacion = new ArrayList<List<String>>();
		dtfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
		
		try {
			limpiarInfoDocente();
			
			dtfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfListaCarreras = null;
			dtfListaDocentes = null;
			
				dtfDocente = servDtfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , dtfPeriodoAcademicoBuscar);
//				listarFunciones();
				listarCargaHorariaDocente();
				limpiarBusquedaAvanzada();
				dtfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+dtfPeriodoAcademico.getPracDescripcion();
				
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.asignar.docente.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.asignar.docente.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.asignar.docente.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.asignar.docente.detalle.carga.horaria.exception")));
		}
		
	}
	
	/**
	 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
	 */
	public void buscarDocentes(){
		
//		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			dtfNumDetalle=0;
			dtfHorasSemanales=0;
			dtfListaDetalleDireccionTitulacion = new ArrayList<List<String>>();
			dtfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
			try {
				
				if(dtfCedulaDocenteBusquedaAvanzada == null){
					dtfCedulaDocenteBusquedaAvanzada = "";
				}
				if(dtfApellidoDocenteBusquedaAvanzada == null){
					dtfApellidoDocenteBusquedaAvanzada = "";
				}
				dtfListDocentesBusquedaAvanzada = servDtfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(dtfCedulaDocenteBusquedaAvanzada, dtfApellidoDocenteBusquedaAvanzada);
				
			} catch (PersonaDatosDtoException e) {
				dtfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.docente.exception"));
			} catch (PersonaDatosDtoNoEncontradoException e) {
				dtfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.buscar.docente.no.encontrado.exception"));
			}
//		}
		
	}


	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Guarda los parametros del formulario
	 **/
	public void guardar() {
		// Cambio Solicitado por Eco. Reinoso 12/12/2017
		if(validarCronograma()){ 
			if(verificarDocente()){
					dtfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
					dtfDetalleCargaHorariaEditar.setDtcrhrNombreProyecto(dtfNombreProyecto);
					dtfDetalleCargaHorariaEditar.setDtcrhrEstadoEliminacion(1);
//					cargarDetalleCargaHoraria(
							cargarCargaHoraria(dtfDocente, 
								dtfHorasSemanales, 
								dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
								dtfPeriodoAcademico, 
								dtfTipoFuncionCargaHorariaSeleccion);
//					dtfDetalleCargaHorariaEditar, dtfHorasSemanales);
					limpiar();
			} else {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.guardar.docente.exception")));
			}
		}
	}
	
	/**
	 * nulifica la lista DetalleCargaHorariaEditar
	 **/
	public void limpiarLista(){
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto1(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto1(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto1Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto2(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto2(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto2Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto3(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto3(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto3Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto4(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto4(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto4Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto5(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto5(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto5Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto6(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto6(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto6Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto7(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto7(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto7Horas(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto8(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto8(null);
		dtfDetalleCargaHorariaEditar.setDtcrhrProyecto8Horas(null);
	}
	
	/**
	 * Guarda los cambios de los parametros del formulario
	 **/
	public void guardarCambios() {
		
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(dtfCargaHorariaBuscar.getCrhrTipoFuncionCargaHoraria().getTifncrhrId()==dtfFuncionSeleccion){
					dtfDetalleCargaHorariaEditar = new DetalleCargaHoraria();
					dtfDetalleCargaHorariaEditar.setDtcrhrEstadoEliminacion(1);
					actualizarDetalleCargaHoraria(dtfCargaHorariaBuscar,dtfDetalleCargaHorariaEditar, dtfHorasSemanales);	
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.guardar.cambios.con.exito.validacion")));
					limpiar();
			} else{
				FacesUtil.mensajeError("Para realizar el cambio de Función debe eliminar la vigente.");
			}
		}
	}
	
	/**
	 * Elimina el registro de la base de datos
	 **/
	public void eliminar() {
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(dtfCargaHorariaBuscar != null){
//				dtfDetalleCargaHorariaEditar.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
				dtfCargaHorariaBuscar.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
				actualizarDetalleCargaHoraria(dtfCargaHorariaBuscar,dtfDetalleCargaHorariaEditar, dtfHorasSemanales);
				limpiar();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.eliminar.con.exito.validacion")));
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.eliminar.exception")));
			}
		}
	}
	
	
	/**
	 * agrega los parametros del formulario
	 * @throws DetalleCargaHorariaException 
	 * @throws DetalleCargaHorariaNoEncontradoException 
	 **/
	public String agregarALista() throws DetalleCargaHorariaNoEncontradoException, DetalleCargaHorariaException {
		
		if(dtfTipoProyecto.intValue() != GeneralesConstantes.APP_ID_BASE && dtfNombreProyecto!= null && dtfHorasPorProyecto != null && GeneralesUtilidades.quitarEspaciosEnBlanco(dtfNombreProyecto).length()>0){
			
			if (dtfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
				try {
					TipoFuncionCargaHoraria tpfncahr = servDtfTipoFuncionCargaHorariaServicio.buscarPorId(dtfFuncionSeleccion);
					if (verificarHorasPorFuncion(tpfncahr.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(), tpfncahr.getTifncrhrId())) {
						
						CargaHoraria carga  = busarCargaHorariaDocente(tpfncahr);
						if (carga == null) {// registro la CAHR
							carga  = cargarCargaHoraria(dtfDocente, 
									calcularHorasPorFuncion(dtfListaDetalleCargaHoraria,tpfncahr.getTifncrhrId()), 
									tpfncahr.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									dtfPeriodoAcademico, 
									tpfncahr);
						}
						
						// agrego el DTCAHR
						DetalleCargaHoraria detalle = new DetalleCargaHoraria();
						detalle.setDtcrhrNivelProyecto1(asignarTipoProyecto(dtfFuncionSeleccion));
						detalle.setDtcrhrNivelProyecto2(dtfTipoProyecto);
						detalle.setDtcrhrProyecto1(dtfNombreProyecto);
						detalle.setDtcrhrNumHoras(dtfHorasPorProyecto);
						
						guardarDetalleCargaHoraria(carga, detalle);
						listarCargaHorariaDocente();

						try {
							carga.setCrhrNumHoras(calcularHorasPorFuncion(dtfListaDetalleCargaHoraria,tpfncahr.getTifncrhrId()));
							servDtfCargaHorariaServicio.editar(carga);
						} catch (CargaHorariaValidacionException e) {
						} catch (CargaHorariaException e) {
						}
						dtfTipoProyecto = GeneralesConstantes.APP_ID_BASE;
						dtfNombreProyecto = new String("");
						dtfHorasPorProyecto = Integer.valueOf(0);
					}else{
						dtfTipoProyecto = GeneralesConstantes.APP_ID_BASE;
						dtfNombreProyecto = new String("");
						dtfHorasPorProyecto = Integer.valueOf(0);
						FacesUtil.mensajeError("El Docente excede el máximo de horas permitidas en la Función seleccionada.");
					}
					
				} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				} catch (TipoFuncionCargaHorariaException e) {
				}
			}
			
			
			
			/**
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(dtfNumDetalle).add(String.valueOf(dtfNivel));
			dtfListaDetalleDireccionTitulacion.get(dtfNumDetalle).add(GeneralesUtilidades.eliminarEspaciosEnBlanco(dtfNombreProyecto).toUpperCase());
			dtfListaDetalleDireccionTitulacion.get(dtfNumDetalle).add(String.valueOf(dtfHorasSemanaProyecto));
			dtfNumDetalle++;
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.agregar.a.lista.con.exito.validacion")));
			cargarListaDetalleCargaHoraria();
			**/
			return null;
		} else{
			FacesUtil.mensajeInfo("Ingrese un nombre al Proyecto.");
			return null;
		}
	}

	private Integer calcularHorasPorFuncion(List<DetalleCargaHoraria> proyectos, int tpfnchId) {
		int retorno = 0;
		
		if (!dtfListaDetalleCargaHoraria.isEmpty()) {
			for (DetalleCargaHoraria item : dtfListaDetalleCargaHoraria) {
				if (item.getDtcrhrCargaHoraria().getCrhrTipoFuncionCargaHoraria().getTifncrhrId() == tpfnchId) {
					retorno = retorno + item.getDtcrhrNumHoras();
				}
			}
		}
		
		return retorno;
	}


	private boolean verificarHorasPorFuncion(int horasMaxima, int tpfnchId) {
		boolean retorno = false;
		int sumatoria = 0;
		if (!dtfListaDetalleCargaHoraria.isEmpty()) {
			for (DetalleCargaHoraria item : dtfListaDetalleCargaHoraria) {
				if (item.getDtcrhrCargaHoraria().getCrhrTipoFuncionCargaHoraria().getTifncrhrId() == tpfnchId) {
					sumatoria = sumatoria + item.getDtcrhrNumHoras();
				}
			}
		}
		
		if ((sumatoria + dtfHorasPorProyecto.intValue()) <= horasMaxima) {
			retorno = true;
		}
		
		return retorno;
	}


	private Integer asignarTipoProyecto(Integer funcion) {
		int retorno = GeneralesConstantes.APP_ID_BASE;
		
		if (funcion != GeneralesConstantes.APP_ID_BASE) {
			try {
				TipoFuncionCargaHoraria tpfncahr = servDtfTipoFuncionCargaHorariaServicio.buscarPorId(funcion);
				if (tpfncahr.getTifncrhrFuncionCargaHoraria().getFncrhrId() == 108){ 
					retorno = 1;
				}else{
					retorno = 0;
				}
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			} catch (TipoFuncionCargaHorariaException e) {
			}
		}
		return retorno;
	}


	/**
	 * Actualiza los parametros del formulario
	 **/
	public String editarALista() {
		String retornar=null;
		for(int i=0;i<dtfListaDetalleDireccionTitulacion.size();i++){
			if(dtfListaDetalleDireccionTitulacion.get(i).get(0).equals(dtfListaDetalleDireccionTitulacionEditar.get(0).get(0)) && dtfListaDetalleDireccionTitulacion.get(i).get(1).equals(dtfListaDetalleDireccionTitulacionEditar.get(0).get(1)) && dtfListaDetalleDireccionTitulacion.get(i).get(2).equals(dtfListaDetalleDireccionTitulacionEditar.get(0).get(2))){
				dtfListaDetalleDireccionTitulacion.get(i).set(0, String.valueOf(dtfNivel));
				dtfListaDetalleDireccionTitulacion.get(i).set(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(dtfNombreProyecto).toUpperCase());
				dtfListaDetalleDireccionTitulacion.get(i).set(2, String.valueOf(dtfHorasSemanaProyecto));
				retornar = irAtras();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.editar.a.lista.con.exito.validacion")));
				cargarListaDetalleCargaHoraria();
			}
		}
		return retornar;
	}

	/**
	 * Elimina el registro de la lista
	 **/
	public void eliminarDeLista(ArrayList<List<String>> lista) {
		for(int i=0;i<dtfListaDetalleDireccionTitulacion.size();i++){
			if(dtfListaDetalleDireccionTitulacion.get(i).get(0).equals(lista.get(0)) && dtfListaDetalleDireccionTitulacion.get(i).get(1).equals(lista.get(1)) && dtfListaDetalleDireccionTitulacion.get(i).get(2).equals(lista.get(2))){
				dtfListaDetalleDireccionTitulacion.remove(i);
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.eliminar.de.lista.con.exito.validacion")));
			}
		}
		dtfNumDetalle--;
		cargarListaDetalleCargaHoraria();
	}
	
	
	
	/**
	 * Calcula las horas en examen complexivo/ reactivos en base a las reglas y valores en base de datos
	 */
	public void calcularHoras(){
		try {
			
			if (dtfFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
				dtfTipoFuncionCargaHorariaSeleccion = servDtfTipoFuncionCargaHorariaServicio.buscarPorId(dtfFuncionSeleccion);
				
				dtfListaHorasSemana = new ArrayList<>();
				for(int i = dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
					dtfListaHorasSemana.add(i); 
				}
				dtfHorasSemanales = dtfListaHorasSemana.get(dtfListaHorasSemana.size()-1);
				dtfListaHorasSemana.sort(Comparator.comparing(Integer::intValue).reversed());
			}else{
				dtfHorasSemanales = GeneralesConstantes.APP_ID_BASE;
			}
			
			dtfTipoProyecto = GeneralesConstantes.APP_ID_BASE;
			dtfNombreProyecto = new String("");
			dtfHorasPorProyecto = new Integer(0);

		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.exception")));

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
			
			crhrResult = servDtfCargaHorariaServicio.anadir(crhr);
			 
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.carga.horaria.con.exito.validacion", observacion)));
			
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.carga.horaria.validacion.exception")));
			
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.carga.horaria.exception")));
		}
		
		return crhrResult;
	}
	
	/**
	 * Carga los campos de carga horaria del docente
	 * @throws DetalleCargaHorariaException 
	 * @throws DetalleCargaHorariaNoEncontradoException 
	 */
	public void listarCargaHorariaDocente() throws DetalleCargaHorariaNoEncontradoException, DetalleCargaHorariaException{ 
		try { 
			if(dtfListaFunciones != null){
				dtfListaDetalleCargaHoraria = new ArrayList<>();
				
				for(TipoFuncionCargaHoraria itemFuncion: dtfListaFunciones){
					dtfListaCargasHorarias =  servDtfCargaHorariaServicio.buscarPorDetallePuesto(dtfDocente.getDtpsId(),itemFuncion.getTifncrhrId(), dtfPeriodoAcademico.getPracId());
					if(dtfListaCargasHorarias.size() > 0 ){
						
						for(CargaHoraria item: dtfListaCargasHorarias ){
							dtfListaDetalleCargaHoraria.addAll(servDtfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId())); 
						}
						
						if (dtfListaDetalleCargaHoraria.isEmpty()) {
							CargaHoraria cargaHorariaAux = new CargaHoraria();
							cargaHorariaAux.setCrhrId(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrId());
							cargaHorariaAux.setCrhrDetallePuesto(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrDetallePuesto());
							cargaHorariaAux.setCrhrEstado(CargaHorariaConstantes.ESTADO_INACTIVO_VALUE);
							cargaHorariaAux.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_ACTIVO_VALUE);
							cargaHorariaAux.setCrhrMallaCurricularParalelo(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrMallaCurricularParalelo());
							cargaHorariaAux.setCrhrNumHoras(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrNumHoras());
							cargaHorariaAux.setCrhrObservacion(GeneralesUtilidades.eliminarEspaciosEnBlanco(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrObservacion()).toUpperCase());
							cargaHorariaAux.setCrhrPeriodoAcademico(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrPeriodoAcademico());
							cargaHorariaAux.setCrhrTipoFuncionCargaHoraria(dtfListaCargasHorarias.get(dtfListaCargasHorarias.size()-1).getCrhrTipoFuncionCargaHoraria());
							try {
								servDtfCargaHorariaServicio.editar(cargaHorariaAux);
								FacesUtil.mensajeInfo("Carga horaria descativada con éxito.");
							} catch (CargaHorariaValidacionException e) {
								FacesUtil.mensajeError(e.getMessage());
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
		
		/**
		try { 
			if(dtfListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: dtfListaFunciones){
					dtfListaCargasHorarias =  servDtfCargaHorariaServicio.buscarPorDetallePuesto(dtfDocente.getDtpsId(),itemFuncion.getTifncrhrId(), dtfPeriodoAcademico.getPracId());
					if(dtfListaCargasHorarias.size() > 0 ){
						dtfBuscar = true;
						for(CargaHoraria item: dtfListaCargasHorarias ){
							dtfCargaHorariaBuscar = item;
							dtfFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
							dtfHorasSemanales = item.getCrhrNumHoras();
							calcularHoras();

							dtfListaDetalleCargaHoraria = servDtfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
							if(dtfListaDetalleCargaHoraria.size() > 0){
								for(DetalleCargaHoraria itemDetalle: dtfListaDetalleCargaHoraria){
									dtfNombreProyecto = itemDetalle.getDtcrhrNombreProyecto();
									dtfDetalleCargaHorariaBuscar = itemDetalle ;
									dtfDetalleCargaHorariaEditar = itemDetalle;
									buscarDetalleCargaHoraria();
								}
								dtfBuscar = true;
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
		*/
	}
	
	
	public CargaHoraria busarCargaHorariaDocente(TipoFuncionCargaHoraria tpfncahr) { 

		CargaHoraria retorno = null;
		try { 
			retorno =  servDtfCargaHorariaServicio.buscarCahrPorDetallePuesto(dtfDocente.getDtpsId(),tpfncahr.getTifncrhrId(), dtfPeriodoAcademico.getPracId());
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.carga.horaria.docente.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.carga.horaria.docente.exception")));
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError("Doble asignación");
		}
		
		return retorno;
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
			servDtfCargaHorariaServicio.editar(cargaHoraria);

//			DetalleCargaHoraria dtcrhr = detalleCargaHoraria;
			List<DetalleCargaHoraria> dtcrhrList = servDtfDetalleCargaHorariaServicio.listaPorCargaHoraria(cargaHoraria.getCrhrId());
			for (DetalleCargaHoraria dtcrhr : dtcrhrList) {
				dtcrhr.setDtcrhrNumHoras(horas);
				dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
				servDtfDetalleCargaHorariaServicio.editar(dtcrhr);
			}

		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.actualizar.carga.horaria.exception")));
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.actualizar.detalle.carga.horaria.validacion.exception")));	
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.actualizar.detalle.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
		} 
	}
	
	
	public String etiquetarNivelPreparacion(int nivel)	{
		String retorno = "";
		switch (nivel) {
		case 0:
			retorno = PeriodoAcademicoConstantes.PRAC_PREGRADO_LABEL;
			break;
		case 1:
			retorno = PeriodoAcademicoConstantes.PRAC_POSGRADO_LABEL;
			break;
		}
		return retorno;
	}
	
	public String etiquetarTipoAsignacion(int nivel)	{
		String retorno = "";
		switch (nivel) {
		case 1:
			retorno = "TUTOR";
			break;
		case 2:
			retorno = "LECTOR";
			break;
		}
		return retorno;
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
			
			servDtfDetalleCargaHorariaServicio.anadir(dtcrhr);
			
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.detalle.carga.horaria.exception")));
		}
	}
	
	private List<DetalleCargaHoraria> guardarDetalleCargaHoraria(CargaHoraria cargaHoraria, DetalleCargaHoraria detalleCaHr){
		List<DetalleCargaHoraria> retorno = new ArrayList<>();
		try {
			
			detalleCaHr.setDtcrhrCargaHoraria(cargaHoraria);
			detalleCaHr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			DetalleCargaHoraria dtcahr = servDtfDetalleCargaHorariaServicio.anadir(detalleCaHr);
			if (dtcahr != null) {
				try {
					retorno = servDtfDetalleCargaHorariaServicio.listaPorCargaHoraria(cargaHoraria.getCrhrId());
				} catch (DetalleCargaHorariaNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}	
			}
			
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.cargar.detalle.carga.horaria.exception")));
		}
		
		return retorno;
	}
	
	public void eliminarDetalleCargaHoraria(DetalleCargaHoraria item) throws DetalleCargaHorariaNoEncontradoException{
		try {
			boolean eliminar = servDtfDetalleCargaHorariaServicio.eliminar(item);
			if (eliminar) {
				listarCargaHorariaDocente();
			}
		} catch (DetalleCargaHorariaValidacionException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		} catch (DetalleCargaHorariaException e1) {
			FacesUtil.mensajeError(e1.getMessage());

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
			if(dtfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
				dtfListaCarreras = servDtfCarreraServicio.listarCarrerasXFacultad(dtfFacultadBuscar);
				dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				dtfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dtfListaDocentes = null;
				listarDocentesPorFacultad();
			}else{
				dtfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				dtfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			}
		} catch (CarreraNoEncontradoException e) { 
			dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfListaDocentes = null;
			dtfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfListaCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.carreras.no.encontrado.exception")));
		} catch (CarreraException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.carreras.exception"))); 
		}
	}

	/**
	 * Lista de Entidades Docente por carrera
	 **/
	public void listarDocentesPorFacultad() {
		
		try {
			limpiarInfoDocente();
			dtfListaDocentes = servDtfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(dtfFacultadBuscar, dtfPeriodoAcademicoBuscar);
			dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
			dtfListaDocentes = servDtfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(dtfCarreraBuscar, dtfPeriodoAcademicoBuscar);
			dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
		}
	}
	/**
	 * Verifica el acceso del docente a los diferentes tipos de carga horaria
	 * otorgando un estado true / false para activar las casillas
	 */
	@SuppressWarnings("rawtypes")
	public void verificarAcceso(){

		try{

			List<TipoFuncionCargaHoraria> funciones = servDtfTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_DIRECCION_PROYECTOS_TITULACION_VALUE);

			if(dtfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE 
					|| dtfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 

				switch (dtfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
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
					funciones = null;
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.funciones.relacion.laboral.validacion.exception")));
					break;
				}

				dtfListaFunciones = funciones;

			}else{
				switch (dtfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
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
					funciones = null;
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vinculacion.listar.funciones.validar")));
					break;
				}

				dtfListaFunciones = funciones;
			}
			
//			if (dtfListaFunciones != null && dtfListaFunciones.size()>0) {
//				try {
//					
//					dtfFuncionSeleccion = dtfListaFunciones.get(dtfListaFunciones.size()-1).getTifncrhrId();
//					dtfTipoFuncionCargaHorariaSeleccion = servDtfTipoFuncionCargaHorariaServicio.buscarPorId(dtfFuncionSeleccion);
//						
//						dtfListaHorasSemana = new ArrayList<>();
//						for(int i = dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
//							dtfListaHorasSemana.add(i); 
//						}
//						dtfListaHorasSemana.sort(Comparator.comparing(Integer::intValue).reversed());
//
//				} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.no.encontrado.exception")));
//				} catch (TipoFuncionCargaHorariaException e) {
//					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.exception")));
//
//				}
//			}

		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.funciones.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.listar.funciones.exception")));
		}

	}
	
	
 
	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
	
	/**
	 * Calcula la hora max y min para la carga horaria
	 **/
	public Boolean calcularHorasMaxMin(){
		
		Boolean verificarFuncion = false;
		
		try {
			if(dtfDocente != null){
				
				switch (dtfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE:
					verificarFuncion = true;
					break;
				case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE:
					verificarFuncion = true;
					break;
				case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE:
					verificarFuncion = true;
					break;
				default:
					verificarFuncion = false;
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.max.min.complementaria.exception")));
					break;
				}
				
				if(verificarFuncion){
					
					dtfFuncionSeleccion = TipoFuncionCargaHorariaConstantes.FUNCION_CAPACITACION_DOCENTE;
					dtfTipoFuncionCargaHorariaSeleccion = servDtfTipoFuncionCargaHorariaServicio.buscarPorId(dtfFuncionSeleccion);
					dtfHorasMax = dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo();
					dtfHorasMin = dtfTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo();
					dtfListaHorasSemana = new ArrayList<Integer>(); 
					for(int i = dtfHorasMin; i<= dtfHorasMax; i++){
						dtfListaHorasSemana.add(i);
					}
					
				}else{
					dtfHorasMax = null;
					dtfHorasMin = null;
				}
			}
		} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.max.min.no.encontrado.exception")));
		} catch (TipoFuncionCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.calcular.horas.max.min.exception")));
		}
		
		return verificarFuncion;
	}
	
	/**
	 * Verifica la seleccion del docente
	 * @return si se ha seleccionado el docente o no 
	 */
	public Boolean verificarDocente(){
		Boolean verificar = false;
		if(dtfDocente!=null){
			verificar = true;
		}
		return verificar;
	}
	
	/**
	 * carga la entidad DetalleCargaHoraria
	 */
	public void cargarListaDetalleCargaHoraria(){
		
		dtfHorasSemanales=0;
		
		if(1<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto1(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(0).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto1(dtfListaDetalleDireccionTitulacion.get(0).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto1Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(0).get(2)));
			dtfHorasSemanales= Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(0).get(2)) + dtfHorasSemanales;
		}
		if(2<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto2(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(1).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto2(dtfListaDetalleDireccionTitulacion.get(1).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto2Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(1).get(2)));
			dtfHorasSemanales= Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(1).get(2)) + dtfHorasSemanales;
		}
		if(3<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto3(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(2).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto3(dtfListaDetalleDireccionTitulacion.get(2).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto3Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(2).get(2)));
			dtfHorasSemanales=Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(2).get(2)) + dtfHorasSemanales;
		}
		if(4<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto4(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(3).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto4(dtfListaDetalleDireccionTitulacion.get(3).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto4Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(3).get(2)));
			dtfHorasSemanales=Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(3).get(2)) + dtfHorasSemanales;
		}
		if(5<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto5(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(4).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto5(dtfListaDetalleDireccionTitulacion.get(4).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto5Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(4).get(2)));
			dtfHorasSemanales=Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(4).get(2)) + dtfHorasSemanales;
		}
		if(6<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto6(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(5).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto6(dtfListaDetalleDireccionTitulacion.get(5).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto6Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(5).get(2)));
			dtfHorasSemanales=Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(5).get(2)) + dtfHorasSemanales;
		}
		if(7<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto7(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(6).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto7(dtfListaDetalleDireccionTitulacion.get(6).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto7Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(6).get(2)));
			dtfHorasSemanales=Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(6).get(2)) + dtfHorasSemanales;
		}
		if(8<=dtfListaDetalleDireccionTitulacion.size()){
			dtfDetalleCargaHorariaEditar.setDtcrhrNivelProyecto8(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(7).get(0))); 
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto8(dtfListaDetalleDireccionTitulacion.get(7).get(1).toUpperCase());
			dtfDetalleCargaHorariaEditar.setDtcrhrProyecto8Horas(Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(7).get(2)));
			dtfHorasSemanales=Integer.parseInt(dtfListaDetalleDireccionTitulacion.get(7).get(2)) + dtfHorasSemanales;
		}
		
		if(dtfHorasSemanales>14){
			dtfHorasSemanales=14;
		}
	}
	
	/**
	 * busca la entidad DetalleCargaHoraria
	 */
	public void buscarDetalleCargaHoraria(){
		
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto1()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto1()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto1Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(0).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto1()));
			dtfListaDetalleDireccionTitulacion.get(0).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto1());
			dtfListaDetalleDireccionTitulacion.get(0).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto1Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto2()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto2()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto2Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(1).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto2()));
			dtfListaDetalleDireccionTitulacion.get(1).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto2());
			dtfListaDetalleDireccionTitulacion.get(1).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto2Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto3()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto3()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto3Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(2).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto3()));
			dtfListaDetalleDireccionTitulacion.get(2).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto3());
			dtfListaDetalleDireccionTitulacion.get(2).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto3Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto4()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto4()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto4Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(3).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto4()));
			dtfListaDetalleDireccionTitulacion.get(3).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto4());
			dtfListaDetalleDireccionTitulacion.get(3).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto4Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto5()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto5()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto5Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(4).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto5()));
			dtfListaDetalleDireccionTitulacion.get(4).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto5());
			dtfListaDetalleDireccionTitulacion.get(4).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto5Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto6()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto6()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto6Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(5).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto6()));
			dtfListaDetalleDireccionTitulacion.get(5).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto6());
			dtfListaDetalleDireccionTitulacion.get(5).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto6Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto7()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto7()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto7Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(6).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto7()));
			dtfListaDetalleDireccionTitulacion.get(6).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto7());
			dtfListaDetalleDireccionTitulacion.get(6).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto7Horas()));
			dtfNumDetalle++;
		}
		if(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto8()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto8()!=null && dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto8Horas()!=null ){
			dtfListaDetalleDireccionTitulacion.add(new ArrayList<String>());
			dtfListaDetalleDireccionTitulacion.get(7).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrNivelProyecto8()));
			dtfListaDetalleDireccionTitulacion.get(7).add(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto8());
			dtfListaDetalleDireccionTitulacion.get(7).add(String.valueOf(dtfDetalleCargaHorariaBuscar.getDtcrhrProyecto8Horas()));
			dtfNumDetalle++;
		}
		cargarListaDetalleCargaHoraria();
	}	


	/**
	 * Busca si existe un periodo activo
	 * @return retorna la entidad PeriodoAcademico activo en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoActivo(){
		
		try {
			return servDtfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.verificar.periodo.activo.no.encontrado.exception")));
			return null;
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.verificar.periodo.activo.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo activo
	 * @return retorna la entidad PeriodoAcademico activo en pregrado
	 **/
	public PlanificacionCronogramaDto verificarCronograma(){
		
		try {
			return servDtfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
		} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.verificar.cronograma.no.encontrado.exception")));
			return null;
		} catch (PlanificacionCronogramaDtoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.verificar.cronograma.exception")));
			return null;
		}
	}
	
	/**
	 * Busca si existe un periodo de cierre
	 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
	 **/
	public PeriodoAcademico verificarPeriodoCierre(){

		try {
		return servDtfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
		dtfPlanificacionCronograma = verificarCronograma();
		if(dtfPlanificacionCronograma != null){
			if(dtfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
				if(dtfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DireccionTitulacion.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}
	
	
	/**
	 * Lista de Entidades Dependencia al seleccionar un peridoso
	 **/
	public void seleccionarPeriodo() {
			limpiarInfoDocente(); 
			dtfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfListaDocentes = null;
			dtfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dtfListaCarreras = null; 
	}
	
	/**
	 * Carga los periodos en la lista para mostrar.
	 */
	public void cagarPeriodos(){
		
		dtfListaPeriodosAcademicos = new ArrayList<>();

		PeriodoAcademico pracActivo = verificarPeriodoActivo();
		if(pracActivo != null){
		dtfListaPeriodosAcademicos.add(pracActivo);
		dtfPeriodoAcademico = pracActivo;
		dtfPeriodoAcademicoBuscar = pracActivo.getPracId();
		}

		PeriodoAcademico pracCierre = verificarPeriodoCierre();
		if(pracCierre != null){
		dtfListaPeriodosAcademicos.add(pracCierre);
		}
	}
	
	public void generarReporteCargaHorariaDocente(){
		if (dtfDocente != null) {
			cargarAsignacionesCargaHoraria(dtfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(dtfPeriodoAcademico.getPracId(), dtfPeriodoAcademico.getPracDescripcion()), dtfUsuario, dtfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
	}
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public List<Dependencia> getDtfListaFacultades() {
		return dtfListaFacultades;
	}

	public void setDtfListaFacultades(List<Dependencia> dtfListaFacultades) {
		this.dtfListaFacultades = dtfListaFacultades;
	}

	public List<Carrera> getDtfListaCarreras() {
		return dtfListaCarreras;
	}

	public void setDtfListaCarreras(List<Carrera> dtfListaCarreras) {
		this.dtfListaCarreras = dtfListaCarreras;
	}

	public List<PersonaDatosDto> getDtfListaDocentes() {
		return dtfListaDocentes;
	}

	public void setDtfListaDocentes(List<PersonaDatosDto> dtfListaDocentes) {
		this.dtfListaDocentes = dtfListaDocentes;
	}

	public List<TipoFuncionCargaHoraria> getDtfListaFunciones() {
		return dtfListaFunciones;
	}

	public void setDtfListaFunciones(List<TipoFuncionCargaHoraria> dtfListaFunciones) {
		this.dtfListaFunciones = dtfListaFunciones;
	}

	public PersonaDatosDto getDtfDocente() {
		return dtfDocente;
	}

	public void setDtfDocente(PersonaDatosDto dtfDocente) {
		this.dtfDocente = dtfDocente;
	}

	public Integer getDtfFacultadBuscar() {
		return dtfFacultadBuscar;
	}

	public void setDtfFacultadBuscar(Integer dtfFacultadBuscar) {
		this.dtfFacultadBuscar = dtfFacultadBuscar;
	}

	public Integer getDtfCarreraBuscar() {
		return dtfCarreraBuscar;
	}

	public void setDtfCarreraBuscar(Integer dtfCarreraBuscar) {
		this.dtfCarreraBuscar = dtfCarreraBuscar;
	}

	public Integer getDtfDocenteBuscar() {
		return dtfDocenteBuscar;
	}

	public void setDtfDocenteBuscar(Integer dtfDocenteBuscar) {
		this.dtfDocenteBuscar = dtfDocenteBuscar;
	}

	public String getDtfIdentificacionBuscar() {
		return dtfIdentificacionBuscar;
	}

	public void setDtfIdentificacionBuscar(String dtfIdentificacionBuscar) {
		this.dtfIdentificacionBuscar = dtfIdentificacionBuscar;
	}

	public Integer getDtfFuncionSeleccion() {
		return dtfFuncionSeleccion;
	}

	public void setDtfFuncionSeleccion(Integer dtfFuncionSeleccion) {
		this.dtfFuncionSeleccion = dtfFuncionSeleccion;
	}

	public Integer getDtfHorasSemanales() {
		return dtfHorasSemanales;
	}

	public void setDtfHorasSemanales(Integer dtfHorasSemanales) {
		this.dtfHorasSemanales = dtfHorasSemanales;
	}

	public DetalleCargaHoraria getDtfDetalleCargaHorariaEditar() {
		return dtfDetalleCargaHorariaEditar;
	}

	public void setDtfDetalleCargaHorariaEditar(DetalleCargaHoraria dtfDetalleCargaHorariaEditar) {
		this.dtfDetalleCargaHorariaEditar = dtfDetalleCargaHorariaEditar;
	}

	public Integer getDtfTipo() {
		return dtfTipo;
	}

	public void setDtfTipo(Integer dtfTipo) {
		this.dtfTipo = dtfTipo;
	}

	public List<Integer> getDtfListaHorasSemana() {
		return dtfListaHorasSemana;
	}

	public void setDtfListaHorasSemana(List<Integer> dtfListaHorasSemana) {
		this.dtfListaHorasSemana = dtfListaHorasSemana;
	}

	public Integer getDtfNivel() {
		return dtfNivel;
	}

	public void setDtfNivel(Integer dtfNivel) {
		this.dtfNivel = dtfNivel;
	}

	public String getDtfNombreProyecto() {
		return dtfNombreProyecto;
	}

	public void setDtfNombreProyecto(String dtfNombreProyecto) {
		this.dtfNombreProyecto = dtfNombreProyecto;
	}

	public Integer getDtfHorasSemanaProyecto() {
		return dtfHorasSemanaProyecto;
	}

	public void setDtfHorasSemanaProyecto(Integer dtfHorasSemanaProyecto) {
		this.dtfHorasSemanaProyecto = dtfHorasSemanaProyecto;
	}

	public List<Integer> getDtfListaDocenteNivel() {
		return dtfListaDocenteNivel;
	}

	public void setDtfListaDocenteNivel(List<Integer> dtfListaDocenteNivel) {
		this.dtfListaDocenteNivel = dtfListaDocenteNivel;
	}

	public List<String> getDtfListaDocenteProyecto() {
		return dtfListaDocenteProyecto;
	}

	public void setDtfListaDocenteProyecto(List<String> dtfListaDocenteProyecto) {
		this.dtfListaDocenteProyecto = dtfListaDocenteProyecto;
	}

	public List<Integer> getDtfListaDocenteHorasSemana() {
		return dtfListaDocenteHorasSemana;
	}

	public void setDtfListaDocenteHorasSemana(List<Integer> dtfListaDocenteHorasSemana) {
		this.dtfListaDocenteHorasSemana = dtfListaDocenteHorasSemana;
	}

	public List<List<String>> getDtfListaDetalleDireccionTitulacion() {
		return dtfListaDetalleDireccionTitulacion;
	}

	public void setDtfListaDetalleDireccionTitulacion(List<List<String>> dtfListaDetalleDireccionTitulacion) {
		this.dtfListaDetalleDireccionTitulacion = dtfListaDetalleDireccionTitulacion;
	}

	public List<List<String>> getDtfListaDetalleDireccionTitulacionEditar() {
		return dtfListaDetalleDireccionTitulacionEditar;
	}

	public void setDtfListaDetalleDireccionTitulacionEditar(List<List<String>> dtfListaDetalleDireccionTitulacionEditar) {
		this.dtfListaDetalleDireccionTitulacionEditar = dtfListaDetalleDireccionTitulacionEditar;
	}


	public Boolean getDtfBuscar() {
		return dtfBuscar;
	}

	public void setDtfBuscar(Boolean dtfBuscar) {
		this.dtfBuscar = dtfBuscar;
	}


	public Integer getDtfPeriodoAcademicoBuscar() {
		return dtfPeriodoAcademicoBuscar;
	}


	public void setDtfPeriodoAcademicoBuscar(Integer dtfPeriodoAcademicoBuscar) {
		this.dtfPeriodoAcademicoBuscar = dtfPeriodoAcademicoBuscar;
	}


	public List<PeriodoAcademico> getDtfListaPeriodosAcademicos() {
		return dtfListaPeriodosAcademicos;
	}


	public void setDtfListaPeriodosAcademicos(List<PeriodoAcademico> dtfListaPeriodosAcademicos) {
		this.dtfListaPeriodosAcademicos = dtfListaPeriodosAcademicos;
	}


	public String getDtfCedulaDocenteBusquedaAvanzada() {
		return dtfCedulaDocenteBusquedaAvanzada;
	}


	public void setDtfCedulaDocenteBusquedaAvanzada(String dtfCedulaDocenteBusquedaAvanzada) {
		this.dtfCedulaDocenteBusquedaAvanzada = dtfCedulaDocenteBusquedaAvanzada;
	}


	public String getDtfApellidoDocenteBusquedaAvanzada() {
		return dtfApellidoDocenteBusquedaAvanzada;
	}


	public void setDtfApellidoDocenteBusquedaAvanzada(String dtfApellidoDocenteBusquedaAvanzada) {
		this.dtfApellidoDocenteBusquedaAvanzada = dtfApellidoDocenteBusquedaAvanzada;
	}


	public String getDtfMensajeBusquedaAvanzada() {
		return dtfMensajeBusquedaAvanzada;
	}


	public void setDtfMensajeBusquedaAvanzada(String dtfMensajeBusquedaAvanzada) {
		this.dtfMensajeBusquedaAvanzada = dtfMensajeBusquedaAvanzada;
	}


	public List<PersonaDatosDto> getDtfListDocentesBusquedaAvanzada() {
		return dtfListDocentesBusquedaAvanzada;
	}
 
	public void setDtfListDocentesBusquedaAvanzada(List<PersonaDatosDto> dtfListDocentesBusquedaAvanzada) {
		this.dtfListDocentesBusquedaAvanzada = dtfListDocentesBusquedaAvanzada;
	}
 
	public String getDtfLinkReporte() {
		return dtfLinkReporte;
	}
  
	public void setDtfLinkReporte(String dtfLinkReporte) {
		this.dtfLinkReporte = dtfLinkReporte;
	}


	public List<DetalleCargaHoraria> getDtfListaDetalleCargaHoraria() {
		return dtfListaDetalleCargaHoraria;
	}


	public void setDtfListaDetalleCargaHoraria(List<DetalleCargaHoraria> dtfListaDetalleCargaHoraria) {
		this.dtfListaDetalleCargaHoraria = dtfListaDetalleCargaHoraria;
	}


	public Integer getDtfHorasPorProyecto() {
		return dtfHorasPorProyecto;
	}


	public void setDtfHorasPorProyecto(Integer dtfHorasPorProyecto) {
		this.dtfHorasPorProyecto = dtfHorasPorProyecto;
	}


	public List<SelectItem> getDtfListTipoProyecto() {
		return dtfListTipoProyecto;
	}


	public void setDtfListTipoProyecto(List<SelectItem> dtfListTipoProyecto) {
		this.dtfListTipoProyecto = dtfListTipoProyecto;
	}


	public Integer getDtfTipoProyecto() {
		return dtfTipoProyecto;
	}


	public void setDtfTipoProyecto(Integer dtfTipoProyecto) {
		this.dtfTipoProyecto = dtfTipoProyecto;
	}


	public Usuario getDtfUsuario() {
		return dtfUsuario;
	}


	public void setDtfUsuario(Usuario dtfUsuario) {
		this.dtfUsuario = dtfUsuario;
	}
	
	
	
 
	
	
}