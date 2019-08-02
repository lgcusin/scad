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
   
 ARCHIVO:      DireccionGestionAcademicaForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario Dieccion Academica y Gestion Academica. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 14-OCTUBRE-2018		 Freddy Guzman						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.FuncionCargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.TipoCargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.TipoFuncionCargaHorariaDto;
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
 * Clase (session bean) GestionAcademicaForm.java Bean de sesión que maneja
 * los atributos del formulario de Gestion Academica.
 * 
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "direccionGestionAcademicaForm")
@SessionScoped
public class DireccionGestionAcademicaForm extends ReporteCargaHorariaForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario gafUsuario;
	private List<Dependencia> gafListaFacultades;
	private List<Carrera> gafListaCarreras;
	private List<PersonaDatosDto> gafListaDocentes;
	private List<TipoFuncionCargaHoraria> gafListaFunciones;
	private List<Integer> gafListaHorasOtros;

	private PersonaDatosDto gafDocente;
	
	private Integer gafHorasOtrosSeleccion;
	private Integer gafFuncionSeleccion; 
	private TipoFuncionCargaHoraria gafTipoFuncionCargaHorariaSeleccion; 

	
	private Integer gafFacultadBuscar;
	private Integer gafCarreraBuscar;
	private Integer gafDocenteBuscar;
	private String gafIdentificacionBuscar;

	private String gafUnidadAcademica; 
	private Integer gafHorasSemanales;
	 
	private PeriodoAcademico gafPeriodoAcademico;
	private Integer gafPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> gafListaPeriodosAcademicos;
	
	private PlanificacionCronogramaDto gafPlanificacionCronograma;
	
	private List<CargaHoraria> gafListaCargasHorarias;
	private List<DetalleCargaHoraria> gafListaDetalleCargaHoraria; 
	
	private CargaHoraria gafCargaHorariaGestionActual;
	private DetalleCargaHoraria gafDetalleCargaHorariaGestionActual; 
	
	private Boolean gafCargaHorariaActiva;
	
	private String gafCedulaDocenteBusquedaAvanzada;
	private String gafApellidoDocenteBusquedaAvanzada;
	private String gafMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> gafListDocentesBusquedaAvanzada;
	
	private List<Integer> gafListaHorasGestion;
	
	//URL reporte
	private String gafLinkReporte;
	
	
	
	// Gestion academica
	private Boolean gafHabilitarFuncionesCaHr;
	
	// Miembros de Consejo
	private Integer gafHorasSemanalesMiembroConsejo;
	private Integer gafTipoFuncionCargaHorariaDtoAsignacionId;
	private TipoFuncionCargaHorariaDto gafTipoFuncionCargaHorariaDtoMiembroConsejo;
	private TipoFuncionCargaHoraria gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida;
	private List<TipoFuncionCargaHorariaDto> gafListTipoFuncionCargaHorariaDtoMiembroConsejo; // asignacion tipo y funcion
	
	// Miembros de Gestion
	private List<TipoFuncionCargaHorariaDto> gafListTipoFuncionCargaHorariaDtoMiembroGestion; // asignacion tipo y funcion
	
	// Coordinadores
	private List<TipoFuncionCargaHorariaDto> gafListTipoFuncionCargaHorariaDtoCoordinador; // asignacion tipo y funcion	
	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB private DependenciaServicio servGafDependenciaServicio;
	@EJB private CarreraServicio servGafCarreraServicio;
	@EJB private PersonaDatosDtoServicioJdbc servGafPersonaDatosServicioJdbc;
	@EJB private PersonaServicio servGafPersonaServicio;
	@EJB private TipoFuncionCargaHorariaServicio servGafTipoFuncionCargaHorariaServicio;
	@EJB private PeriodoAcademicoServicio servGafPeriodoAcademicoServicio;
	@EJB private PlanificacionCronogramaDtoServicioJdbc servGafPlanificacionCronogramaDtoServicioJdbc; 
	@EJB private CargaHorariaServicio servGafCargaHorariaServicio;
	@EJB private DetalleCargaHorariaServicio servGafDetalleCargaHorariaServicio;
	@EJB private DocenteDatosDtoServicioJdbc servGafDocenteDatosDtoServicioJdbc;
	@EJB private UsuarioRolServicio servGafUsuarioRolServicio;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/


	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * @return navegacion hacia el formulario de gestion academica.
	 */
	public String irFormularioGestionAcademica(Usuario usuario) {
		gafUsuario = usuario;
		iniciarParametros();
		return "irFormularioGestionAcademica";
	}

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irFormularioDireccionAcademica(Usuario usuario) {
		gafUsuario = usuario;
		iniciarParametros();
		return "irFormularioDireccionAcademica";
	}

	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		gafListaCarreras = null;
		gafListaDocentes = null;
		gafListaFunciones = null;
		gafListaHorasOtros = null;
		gafDocente = null;
		gafHorasOtrosSeleccion = null;
		gafFuncionSeleccion = null; 
		gafTipoFuncionCargaHorariaSeleccion = null;
		gafFacultadBuscar = null;
		gafCarreraBuscar = null;
		gafDocenteBuscar = null;
		gafIdentificacionBuscar = null;
		gafUnidadAcademica = null; 
		gafHorasSemanales = null; 
		gafCargaHorariaGestionActual = null;
		gafDetalleCargaHorariaGestionActual = null; 
		gafCargaHorariaActiva = false ;
		gafPeriodoAcademicoBuscar = null;
		gafListaPeriodosAcademicos = null;
		gafLinkReporte = null;
		gafListaHorasGestion = null;
		gafHabilitarFuncionesCaHr = null; 
		gafHorasSemanalesMiembroConsejo = null;
		gafTipoFuncionCargaHorariaDtoAsignacionId = null;
		gafTipoFuncionCargaHorariaDtoMiembroConsejo = null;
		gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida = null;
		gafListTipoFuncionCargaHorariaDtoMiembroConsejo = null;
		gafListTipoFuncionCargaHorariaDtoMiembroGestion = null;
		gafListTipoFuncionCargaHorariaDtoCoordinador = null;	
		
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Inicia los parametros de la funcionalidad
	 */
	@SuppressWarnings("rawtypes")
	private void iniciarParametros() { 
		try {	

			gafCargaHorariaActiva = false ;
			rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

			validarCronograma();
			cagarPeriodos();
			limpiarInfoDocente();
			
			gafListaFacultades = servGafDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			Iterator it = gafListaFacultades.iterator();
			while(it.hasNext()){
				Dependencia cad = (Dependencia) it.next();
				if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
					it.remove();
				}
			}

		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.iniciar.parametros.dependencia.no.encontrado.exception")));
		}


	}
		/**
		 * Setea y nulifica a los valores iniciales de cada parametro
		 */
		public void limpiar() {
			
			gafFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			gafCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			gafFuncionSeleccion = GeneralesConstantes.APP_ID_BASE;
			gafIdentificacionBuscar = null;
			gafHorasSemanales = null;
			gafListaFunciones = null;
			gafListaCarreras = null;
			gafListaFacultades = null;
			gafListaDocentes = null;
			gafFuncionSeleccion = null;
			gafTipoFuncionCargaHorariaSeleccion = null;
			gafDocente = null;
			gafListaHorasOtros = null; 
			gafHorasOtrosSeleccion = null;   
			gafUnidadAcademica = null; 
			gafCargaHorariaGestionActual = null;
			gafDetalleCargaHorariaGestionActual = null; 
			gafCargaHorariaActiva = false ;
			gafLinkReporte = null;
			gafListaHorasGestion = null;
			gafHabilitarFuncionesCaHr = null; 
			gafHorasSemanalesMiembroConsejo = null;
			gafTipoFuncionCargaHorariaDtoAsignacionId = null;
			gafTipoFuncionCargaHorariaDtoMiembroConsejo = null;
			gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida = null;
			gafListTipoFuncionCargaHorariaDtoMiembroConsejo = null;
			gafListTipoFuncionCargaHorariaDtoMiembroGestion = null;
			gafListTipoFuncionCargaHorariaDtoCoordinador = null;
			gafPeriodoAcademico = verificarPeriodoActivo();
			iniciarParametros();
		}
		
		/**
		 * Limpia la seleccion de campos en base al docente
		 */
		public void limpiarInfoDocente(){
			gafIdentificacionBuscar = null; 
			gafListaFunciones = null;
			gafFuncionSeleccion = null;
			gafTipoFuncionCargaHorariaSeleccion = null;
			gafDocente = null;
			gafHorasSemanales = null;
			gafListaHorasOtros = null; 
			gafHorasOtrosSeleccion = null;   
			gafUnidadAcademica = null; 
			gafCargaHorariaGestionActual = null;
			gafDetalleCargaHorariaGestionActual = null; 
			gafCargaHorariaActiva = false ;
			gafLinkReporte = null;
			gafListaHorasGestion = null;
			gafPeriodoAcademico = verificarPeriodoActivo();
			limpiarMiembrosConsejo();
			limpiarMiembrosGestion();
			limpiarCoordinadores();
		}
		
		
		public void generarReporteCargaHorariaDocente(){
			if (gafDocente != null) {
				cargarAsignacionesCargaHoraria(gafDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(gafPeriodoAcademico.getPracId(), gafPeriodoAcademico.getPracDescripcion()), gafUsuario, gafUsuario.getUsrIdentificacion());
				generarReporteIndividualCargaHoraria();
				rchfActivarReporte = 1;
			}
		}
		
		
		
		private void limpiarMiembrosConsejo(){
			gafHabilitarFuncionesCaHr = null; 
			gafHorasSemanalesMiembroConsejo = null;
			gafTipoFuncionCargaHorariaDtoAsignacionId = null;
			gafTipoFuncionCargaHorariaDtoMiembroConsejo = null;
			gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida = null;
			gafListTipoFuncionCargaHorariaDtoMiembroConsejo = null;
		}
		
		private void limpiarMiembrosGestion(){
			gafListTipoFuncionCargaHorariaDtoMiembroGestion = null;
		}
		
		private void limpiarCoordinadores(){
			gafListTipoFuncionCargaHorariaDtoCoordinador = null;			
		}
		
		/**
		 * Limpia parametros del cuadro de dialogo busqueda avanzada
		 */
		public void limpiarBusquedaAvanzada(){
			gafCedulaDocenteBusquedaAvanzada = null;
			gafApellidoDocenteBusquedaAvanzada = null;
			gafListDocentesBusquedaAvanzada = null;
			gafMensajeBusquedaAvanzada = null;
			gafListaHorasGestion = null;
			gafLinkReporte = null;
		}

		// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

		/**
		 * Busca la entidad Docente basado en los parametros de ingreso
		 */
		public void buscar() {
			
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017	
				try {
					limpiarInfoDocente();
					if (gafFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
						if (gafDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
							gafPeriodoAcademico = servGafPeriodoAcademicoServicio.buscarPorId(gafPeriodoAcademicoBuscar);
							gafDocente = servGafPersonaDatosServicioJdbc.buscarPorId(gafDocenteBuscar , gafPeriodoAcademicoBuscar);
							gafLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+gafDocente.getPrsIdentificacion()+"&prd="+gafPeriodoAcademico.getPracDescripcion();
							verificarAcceso(); 
						} else { 
							gafDocente = null;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.docente.validacion.exception")));
						}
					} else { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.facultad.validacion.exception")));
					}
	
				} catch (PersonaDatosDtoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.persona.datos.dto.no.encontrado.exception")));
				} catch (PersonaDatosDtoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.persona.datos.dto.exception")));
				} catch (PeriodoAcademicoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.periodo.academico.no.encontrado.exception")));
				} catch (PeriodoAcademicoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.periodo.academico.exception")));
				}
//			}
		}
		
		
		
			/**
			 * Busca la entidad Docente basado en los parametros de ingreso
			 */
			public void buscarGestion() {
				
//				if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017	
					try {
						limpiarInfoDocente();
						if (gafFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
							if (gafDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
								
								gafPeriodoAcademico = servGafPeriodoAcademicoServicio.buscarPorId(gafPeriodoAcademicoBuscar);
								gafDocente = servGafPersonaDatosServicioJdbc.buscarPorId(gafDocenteBuscar , gafPeriodoAcademicoBuscar);
								gafLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+gafDocente.getPrsIdentificacion()+"&prd="+gafPeriodoAcademico.getPracDescripcion();
								verificarAcceso();
								
								if (verificarDireccion()) {
									limpiar();
									FacesUtil.mensajeInfo("Usted posee carga horaria por Dirección Académica, no se puede asignar mas Funciones.");
								}
								
							} else { 
								gafDocente = null;
								FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.docente.validacion.exception")));
							}
						} else { 
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.facultad.validacion.exception")));
						}
		
					} catch (PersonaDatosDtoNoEncontradoException e) { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.persona.datos.dto.no.encontrado.exception")));
					} catch (PersonaDatosDtoException e) { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.persona.datos.dto.exception")));
					} catch (PeriodoAcademicoNoEncontradoException e) { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.periodo.academico.no.encontrado.exception")));
					} catch (PeriodoAcademicoException e) {
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.periodo.academico.exception")));
					}
//				}
			}		
		
		
		/**
		 * Carga informacion del docente seleccionado en el dialogo de busqueda avanzada
		 * @param item Item recibido con la infromacion del docente seleccionado
		 */
		public void asignarDocente( PersonaDatosDto item){
			try {
				limpiarInfoDocente();
				gafFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				gafCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				gafListaCarreras = null;
				gafListaDocentes = null;
				
					gafDocente = servGafPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , gafPeriodoAcademicoBuscar);
					verificarAcceso(); 
					limpiarBusquedaAvanzada(); 
					gafLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+gafPeriodoAcademico.getPracDescripcion();
					
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.asignar.docente.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.asignar.docente.exception")));
			} 
		}
		
		/**
		 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
		 */
		public void buscarDocentes(){
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				try {
					
					if(gafCedulaDocenteBusquedaAvanzada == null){
						gafCedulaDocenteBusquedaAvanzada = "";
					}
					if(gafApellidoDocenteBusquedaAvanzada == null){
						gafApellidoDocenteBusquedaAvanzada = "";
					}
					gafListDocentesBusquedaAvanzada = servGafPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(gafCedulaDocenteBusquedaAvanzada, gafApellidoDocenteBusquedaAvanzada);
					
				} catch (PersonaDatosDtoException e) {
					gafMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.docente.exception"));
				} catch (PersonaDatosDtoNoEncontradoException e) {
					gafMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.buscar.docente.no.encontrado.exception"));
				}
//			}
			
		}

	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------

	/**
	 * Guarda los parametros del formulario direccion academcia
	 **/
	public void guardar() {

		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
//			if(verificarCargoLibre()){ 
				if(verificarGuardar()){
					
					Boolean verificar = false;
					
					//Gestion academica 
						if (gafFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
							if (gafFuncionSeleccion == TipoFuncionCargaHorariaConstantes.FUNCION_OTROS_AUTORIZADOS_GESTION_ACADEMICA_VALUE) {
								verificar = cargarDetalleCargaHoraria(
										cargarCargaHoraria(gafDocente, 
												gafHorasSemanales, 
												gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
												gafPeriodoAcademico, 
												gafTipoFuncionCargaHorariaSeleccion)
										,gafUnidadAcademica, gafHorasSemanales);
							}else{
								verificar = cargarDetalleCargaHoraria(
										cargarCargaHoraria(gafDocente, 
												gafHorasSemanales, 
												gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
												gafPeriodoAcademico, 
												gafTipoFuncionCargaHorariaSeleccion)
										,gafUnidadAcademica, gafHorasSemanales); 
							} 
						}else{ 
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.guardar.sin.funcion.validacion")));
						}   
						if(verificar){ 
							limpiar();
						}
						
						boolean gestion = false;
						// desactivo todo lo que tenga de gestion
						if (gafListTipoFuncionCargaHorariaDtoMiembroConsejo != null && gafListTipoFuncionCargaHorariaDtoMiembroConsejo.size() > 0) {
							for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroConsejo) {
								try {
									List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
									if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
										gestion = true;
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

						

						if (gafListTipoFuncionCargaHorariaDtoMiembroGestion != null && gafListTipoFuncionCargaHorariaDtoMiembroGestion.size() > 0) {
							for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroGestion) {
								try {
									List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
									if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
										gestion = true;
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

						if (gafListTipoFuncionCargaHorariaDtoCoordinador != null && gafListTipoFuncionCargaHorariaDtoCoordinador.size() > 0) {
							for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoCoordinador) {
								try {
									List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
									if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
										gestion = true;
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
						
						if(gestion){ 
							limpiar();
							limpiarMiembrosConsejo();
							limpiarMiembrosGestion();
							limpiarCoordinadores();
							gafCargaHorariaActiva = false;
							FacesUtil.mensajeInfo("Se ha desactivado las funciones de Gestión Académica que se encontraba a su cargo.");
						}
						
				}
			}
//		}
	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambios() {
		 
		Boolean verificar = false;
		if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			if(verificarDocente()){  
					if(gafFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){ 
						if(verificarGuardar()){
							//Actualizar Gestion academica
							if(gafHorasSemanales != null){
								if(gafCargaHorariaGestionActual != null){
									gafCargaHorariaGestionActual.setCrhrTipoFuncionCargaHoraria(gafTipoFuncionCargaHorariaSeleccion);
									gafCargaHorariaGestionActual.setCrhrObservacion(gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
									gafCargaHorariaGestionActual.setCrhrNumHoras(gafHorasSemanales);
									
									if(actualizarCargaHoraria(gafCargaHorariaGestionActual)){
										gafDetalleCargaHorariaGestionActual.setDtcrhrUnidadAcademica(GeneralesUtilidades.eliminarEspaciosEnBlanco(gafUnidadAcademica).toUpperCase());
										gafDetalleCargaHorariaGestionActual.setDtcrhrFuncion(gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
										gafDetalleCargaHorariaGestionActual.setDtcrhrNumHoras(gafHorasSemanales);
										
										if(actualizarDetalleCargaHoraria(gafDetalleCargaHorariaGestionActual)){
											verificar = true;
										}
									}
									//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
								}else{
									if (gafFuncionSeleccion == TipoFuncionCargaHorariaConstantes.FUNCION_OTROS_AUTORIZADOS_GESTION_ACADEMICA_VALUE) {
										verificar = cargarDetalleCargaHoraria(
												cargarCargaHoraria(gafDocente, 
														gafHorasSemanales, 
														gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
														gafPeriodoAcademico, 
														gafTipoFuncionCargaHorariaSeleccion)
												,gafUnidadAcademica, gafHorasSemanales);
									}else{
										verificar = cargarDetalleCargaHoraria(
												cargarCargaHoraria(gafDocente, 
														gafHorasSemanales, 
														gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
														gafPeriodoAcademico, 
														gafTipoFuncionCargaHorariaSeleccion)
												,gafUnidadAcademica, gafHorasSemanales);
									} 
								}
							}   
							
							
							boolean gestion = false;
							// desactivo todo lo que tenga de gestion
							if (gafListTipoFuncionCargaHorariaDtoMiembroConsejo != null && gafListTipoFuncionCargaHorariaDtoMiembroConsejo.size() > 0) {
								for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroConsejo) {
									try {
										List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
										if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
											gestion = true;
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

							

							if (gafListTipoFuncionCargaHorariaDtoMiembroGestion != null && gafListTipoFuncionCargaHorariaDtoMiembroGestion.size() > 0) {
								for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroGestion) {
									try {
										List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
										if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
											gestion = true;
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

							if (gafListTipoFuncionCargaHorariaDtoCoordinador != null && gafListTipoFuncionCargaHorariaDtoCoordinador.size() > 0) {
								for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoCoordinador) {
									try {
										List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
										if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
											gestion = true;
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
							
							if(gestion){ 
								limpiar();
								limpiarMiembrosConsejo();
								limpiarMiembrosGestion();
								limpiarCoordinadores();
								gafCargaHorariaActiva = false;
								FacesUtil.mensajeInfo("Se ha desactivado las funciones de Gestión Académica que se encontraba a su cargo.");
							}
							
							
						//Si al actualizar se ha descartado el parametro ingresado inicialmente se elimina 
						}else{
							if(gafCargaHorariaGestionActual != null){
								gafCargaHorariaGestionActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(gafCargaHorariaGestionActual)){
									gafDetalleCargaHorariaGestionActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									if(actualizarDetalleCargaHoraria(gafDetalleCargaHorariaGestionActual)){
										verificar = true;
									}
								}
							}
						} 
					}
					
					//Verificacion de actualizacion
					if(verificar){
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.guardar.cambios.con.exito.validacion")));
						limpiar();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.guardar.cambios.exception")));
					} 
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.guardar.cambios.docente.validacion.exception")));
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
				
				if(gafFuncionSeleccion != GeneralesConstantes.APP_ID_BASE){ 
					
					//Eliminar Gestion academica
					if(gafHorasSemanales != null){
						gafCargaHorariaGestionActual.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);
						
						if(actualizarCargaHoraria(gafCargaHorariaGestionActual)){
							gafDetalleCargaHorariaGestionActual.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
							
							if(actualizarDetalleCargaHoraria(gafDetalleCargaHorariaGestionActual)){
								verificar = true;
							}
						} 
					}  
				}
				//Verificacion de Eliminacion
				if(verificar){
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.eliminar.con.exito.validacion")));
					limpiar();
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.eliminar.exception")));
				} 
				
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.eliminar.docente.validacion.exception")));
			} 
		}
	}
	
	/**
	 * Guarda los parametros del formulario
	 **/
	public void guardarGestion() {

		if (validarCronograma()) { // Cambio Solicitado por Eco. Reinoso 12/12/2017

			if (verificarGuardarGestion()) {

					// Para todas las listas
					if (gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida != null) {
							cargarCargaHoraria(
									gafDocente,
									gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas(),
									gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(),
									gafPeriodoAcademico,
									gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida);
					}

					if (gafListTipoFuncionCargaHorariaDtoMiembroGestion != null
							&& gafListTipoFuncionCargaHorariaDtoMiembroGestion.size() > 0) {
						for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroGestion) {
							if (item.getTifncrhrIsCheck()) {
								TipoFuncionCargaHoraria tpfncahr = new TipoFuncionCargaHoraria();
								tpfncahr.setTifncrhrId(item.getTifncrhrId());

								cargarCargaHoraria(
										gafDocente,
										item.getTifncrhrHoraSeleccionada(),
										item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion(),
										gafPeriodoAcademico,
										tpfncahr);
							}
						}
					}

					if (gafListTipoFuncionCargaHorariaDtoCoordinador != null
							&& gafListTipoFuncionCargaHorariaDtoCoordinador.size() > 0) {
						for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoCoordinador) {
							if (item.getTifncrhrIsCheck()) {
								TipoFuncionCargaHoraria tpfncahr = new TipoFuncionCargaHoraria();
								tpfncahr.setTifncrhrId(item.getTifncrhrId());
								cargarCargaHoraria(
										gafDocente,
										item.getTifncrhrHoraSeleccionada(),
										item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion(),
										gafPeriodoAcademico,
										tpfncahr);
							}
						}
					}

					limpiar();
			}
		}

	}

	/**
	 * Actualiza los parametros del formulario
	 **/
	public void guardarCambiosGestion() {

		if (validarCronograma()) { // Cambio Solicitado por Eco. Reinoso 12/12/2017

			if (verificarGuardarGestion()) {

				if (gafListTipoFuncionCargaHorariaDtoMiembroConsejo != null && gafListTipoFuncionCargaHorariaDtoMiembroConsejo.size() > 0) {
					for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroConsejo) {

						// busco carga horaria, segun dtps_id, tpfncahr_id y estados
						// si existe actualizo, 
						// caso contrario crear , si solo si IsCheck 

						if (item.getTifncrhrIsCheck()) { // si existe actualizo, caso contrario creo

							try {
								List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
								if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
									for (CargaHoraria itemCaHr : listCargaHoraria) {
										CargaHoraria cargaHoraria = itemCaHr;
										cargaHoraria.setCrhrNumHoras(item.getTifncrhrFuncionCargaHorariaDto().getFncrhrHorasAsignadas());
										cargaHoraria.setCrhrObservacion(item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion());
										actualizarCargaHoraria(cargaHoraria);
										FacesUtil.mensajeInfo("Actualización, horas " + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion() + " registrada con éxito.");
									}
								}else {
									TipoFuncionCargaHoraria tpfncahr = new TipoFuncionCargaHoraria();
									tpfncahr.setTifncrhrId(item.getTifncrhrId());
									cargarCargaHoraria(
											gafDocente,
											item.getTifncrhrFuncionCargaHorariaDto().getFncrhrHorasAsignadas(),
											item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion(),
											gafPeriodoAcademico,
											tpfncahr);		
									FacesUtil.mensajeInfo("Actualización, horas " + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion() + " registrada con éxito.");	
								}
							} catch (CargaHorariaNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage());
							} catch (CargaHorariaException e) {
								FacesUtil.mensajeError(e.getMessage());
							}	

						}else{ // busco y desactivo

							try {
								List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
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
				}

				

				if (gafListTipoFuncionCargaHorariaDtoMiembroGestion != null && gafListTipoFuncionCargaHorariaDtoMiembroGestion.size() > 0) {
					for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroGestion) {

						// busco carga horaria, segun dtps_id, tpfncahr_id y estados
						// si existe actualizo, 
						// caso contrario crear , si solo si IsCheck 

						if (item.getTifncrhrIsCheck()) { // si existe actualizo, caso contrario creo

							try {
								List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
								if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
									for (CargaHoraria itemCaHr : listCargaHoraria) {
										CargaHoraria cargaHoraria = itemCaHr;
										cargaHoraria.setCrhrNumHoras(item.getTifncrhrHoraSeleccionada());
										cargaHoraria.setCrhrObservacion(item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion());
										actualizarCargaHoraria(cargaHoraria);
										FacesUtil.mensajeInfo("Actualización, horas " + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion() + " registrada con éxito.");
									}
								}else {
									TipoFuncionCargaHoraria tpfncahr = new TipoFuncionCargaHoraria();
									tpfncahr.setTifncrhrId(item.getTifncrhrId());
									cargarCargaHoraria(
											gafDocente,
											item.getTifncrhrHoraSeleccionada(),
											item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion(),
											gafPeriodoAcademico,
											tpfncahr);		
									FacesUtil.mensajeInfo("Actualización, horas " + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion() + " registrada con éxito.");	
								}
							} catch (CargaHorariaNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage());
							} catch (CargaHorariaException e) {
								FacesUtil.mensajeError(e.getMessage());
							}	

						}else{ // busco y desactivo

							try {
								List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
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
				}

				if (gafListTipoFuncionCargaHorariaDtoCoordinador != null && gafListTipoFuncionCargaHorariaDtoCoordinador.size() > 0) {
					for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoCoordinador) {
						// busco carga horaria, segun dtps_id, tpfncahr_id y estados
						// si existe actualizo, 
						// caso contrario crear , si solo si IsCheck 

						if (item.getTifncrhrIsCheck()) { // si existe actualizo, caso contrario creo

							try {
								List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
								if (listCargaHoraria != null && listCargaHoraria.size() > 0) {
									for (CargaHoraria itemCaHr : listCargaHoraria) {
										CargaHoraria cargaHoraria = itemCaHr;
										cargaHoraria.setCrhrNumHoras(item.getTifncrhrHoraSeleccionada());
										cargaHoraria.setCrhrObservacion(item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion());
										actualizarCargaHoraria(cargaHoraria);
										FacesUtil.mensajeInfo("Actualización, horas " + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion() + " registrada con éxito.");													
									}
								}else {
									TipoFuncionCargaHoraria tpfncahr = new TipoFuncionCargaHoraria();
									tpfncahr.setTifncrhrId(item.getTifncrhrId());
									cargarCargaHoraria(
											gafDocente,
											item.getTifncrhrHoraSeleccionada(),
											item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion(),
											gafPeriodoAcademico,
											tpfncahr);										
									FacesUtil.mensajeInfo("Actualización, horas " + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrDescripcion() + " registrada con éxito.");	
								}
							} catch (CargaHorariaNoEncontradoException e) {
								FacesUtil.mensajeError(e.getMessage());
							} catch (CargaHorariaException e) {
								FacesUtil.mensajeError(e.getMessage());
							}	

						}else{ // busco y desactivo

							try {
								List<CargaHoraria> listCargaHoraria =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), item.getTifncrhrId(), gafPeriodoAcademico.getPracId());
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
				}
				
				limpiarMiembrosConsejo();
				limpiarMiembrosGestion();
				limpiarCoordinadores();
				gafCargaHorariaActiva = false;
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
			
			crhrResult = servGafCargaHorariaServicio.anadir(crhr);
	  
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.cargar.carga.horaria.con.exito.validacion", observacion)));
			
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.cargar.carga.horaria.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.cargar.carga.horaria.carga.horaria.exception")));
		}
		
		return crhrResult;
	}
	
	
	
	
	
	/**
	 * Carga el detalle de la carga horaia en la tabla DETALLE_CARGA_HORARIA
	 * @param cargaHoraria Entidad a insertar en el detalle
	 * @param unidadAcademica Unidad Academica a agregar
	 * @param horas Numero de horas por semana
	 **/
	public Boolean cargarDetalleCargaHoraria(CargaHoraria cargaHoraria, String unidadAcademica, Integer horas){
		
		Boolean verificar = false;
		try {
			
			DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
			
			dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
			dtcrhr.setDtcrhrUnidadAcademica(GeneralesUtilidades.eliminarEspaciosEnBlanco(unidadAcademica).toUpperCase());
			dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
			dtcrhr.setDtcrhrNumHoras(horas);
			
			servGafDetalleCargaHorariaServicio.anadir(dtcrhr);
			verificar = true;
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.cargar.carga.horaria.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.cargar.carga.horaria.detalle.carga.horaria.exception")));
		}
		return verificar;
	}
	
	/**
	 * Carga los campos de carga horaria del docente
	 */
	public void listarCargaHorariaDocente(){ 
		
		try {
			
			//Gestion Academica
			if(gafListaFunciones != null){
				for(TipoFuncionCargaHoraria itemFuncion: gafListaFunciones){
					gafListaCargasHorarias =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), itemFuncion.getTifncrhrId(), gafPeriodoAcademico.getPracId());
					if(gafListaCargasHorarias.size() > 0 ){
						gafCargaHorariaActiva = true;
						for(CargaHoraria item: gafListaCargasHorarias ){ 
							gafCargaHorariaGestionActual = item;
							gafListaDetalleCargaHoraria = servGafDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId());
							if(gafListaDetalleCargaHoraria.size() > 0){
								for(DetalleCargaHoraria itemDetalle: gafListaDetalleCargaHoraria){
									gafDetalleCargaHorariaGestionActual = itemDetalle;
									gafUnidadAcademica = itemDetalle.getDtcrhrUnidadAcademica();
									gafFuncionSeleccion = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
									gafHorasSemanales = item.getCrhrNumHoras();
									calcularHoras();

									if(item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId()==TipoFuncionCargaHorariaConstantes.FUNCION_OTROS_AUTORIZADOS_GESTION_ACADEMICA_VALUE){
										gafListaHorasOtros = new ArrayList<>();
										for(int i = gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
											gafListaHorasOtros.add(i); 
										} 
										gafHorasOtrosSeleccion = itemDetalle.getDtcrhrNumHoras();
										calcularOtrasHoras();
									}
									gafCargaHorariaActiva = true; 
								}
							}
						}
					}
				}
			}
			 
			
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.exception")));
		} catch (DetalleCargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.detalle.carga.horaria.no.encontrado.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.detalle.carga.horaria.exception")));
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
				verificar = servGafCargaHorariaServicio.editar(entidad); 
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.actualizar.carga.horaria.validacion.exception")));
		} catch (CargaHorariaException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.actualizar.carga.horaria.exception")));
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
			verificar = servGafDetalleCargaHorariaServicio.editar(entidad);
		} catch (DetalleCargaHorariaValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.actualizar.detalle.carga.horaria.validacion.exception")));
		} catch (DetalleCargaHorariaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.actualizar.detalle.carga.horaria.exception")));
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
		gafFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
		gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
		gafListaDocentes = null;
		gafCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
		gafListaCarreras = null; 
	}

		/**
		 * Lista de Entidades Carrera por facultad id
		 **/
		public void listarCarreras() {
			
			try {	
				limpiarInfoDocente();
				if(gafFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
					gafListaCarreras = servGafCarreraServicio.listarCarrerasXFacultad(gafFacultadBuscar);
					gafCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
					gafListaDocentes = null;
					listarDocentesPorFacultad();
					gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				}else{
					gafFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
					gafCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
					gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				} 
			} catch (CarreraNoEncontradoException e) { 
				gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				gafListaDocentes = null;
				gafCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				gafListaCarreras = null;
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carreras.no.encontrado.exception")));
			} catch (CarreraException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carreras.exception"))); 
			}
		}

		/**
		 * Lista de Entidades Docente por carrera
		 **/
		public void listarDocentesPorFacultad() {
			
			try {
				limpiarInfoDocente();
				gafListaDocentes = servGafPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(gafFacultadBuscar, gafPeriodoAcademicoBuscar);
				gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
				gafListaDocentes = servGafPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(gafCarreraBuscar, gafPeriodoAcademicoBuscar);
				gafDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			} catch (PersonaDatosDtoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
			}
		}
		
		
		/**
		 * Lista de Funciones que se pueden asignar al docente
		 **/
		private void listarFuncionesGestion() {
			   
				try { 
					gafListaFunciones = servGafTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_DIRECCION_VALUE); 
					listarCargaHorariaDocente();
				} catch (TipoFuncionCargaHorariaNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.funciones.no.encontrado.exception")));
				} catch (TipoFuncionCargaHorariaException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.funciones.exception")));
				}
				
		}
		
		private void listarFuncionesMiembrosConsejo() {
			gafTipoFuncionCargaHorariaDtoMiembroConsejo = new TipoFuncionCargaHorariaDto();
			gafListTipoFuncionCargaHorariaDtoMiembroConsejo = new ArrayList<>();

			List<TipoFuncionCargaHoraria> auxListTipoFuncionCargaHoraria = null;
			try {
				auxListTipoFuncionCargaHoraria = servGafTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_MIEMBRO_CONSEJO_VALUE);

				if (auxListTipoFuncionCargaHoraria != null && auxListTipoFuncionCargaHoraria.size() > 0) {
					for (TipoFuncionCargaHoraria item : auxListTipoFuncionCargaHoraria) {
						FuncionCargaHorariaDto funcion = new FuncionCargaHorariaDto();
						TipoCargaHorariaDto tipo = new TipoCargaHorariaDto();
						TipoFuncionCargaHorariaDto tipoFuncion = new TipoFuncionCargaHorariaDto();
						List<Integer> horas = new ArrayList<>();

						funcion.setFncrhrId(item.getTifncrhrId());
						funcion.setFncrhrDescripcion(item.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
						funcion.setFncrhrHorasAsignadas(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas());
						funcion.setFncrhrHorasMinimo(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo());
						funcion.setFncrhrHorasMaximo(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo());
						tipo.setTicrhrId(item.getTifncrhrTipoCargaHoraria().getTicrhrId());
						tipo.setTicrhrDescripcion(item.getTifncrhrTipoCargaHoraria().getTicrhrDescripcion());
						tipo.setTicrhrEstado(item.getTifncrhrTipoCargaHoraria().getTicrhrEstado());

						if (funcion.getFncrhrHorasAsignadas() == null) {
							for (int i = funcion.getFncrhrHorasMinimo(); i <= funcion.getFncrhrHorasMaximo(); i++) {
								horas.add(i);
							}
						} else {
							horas.add(funcion.getFncrhrHorasAsignadas());
						}
						
						tipoFuncion.setTifncrhrId(item.getTifncrhrId());
						tipoFuncion.setTifncrhrIsCheck(Boolean.FALSE);
						tipoFuncion.setTifncrhrFuncionCargaHorariaDto(funcion);
						tipoFuncion.setTifncrhrTipoCargaHorariaDto(tipo);
						tipoFuncion.setTifncrhrListHoras(horas);

						gafListTipoFuncionCargaHorariaDtoMiembroConsejo.add(tipoFuncion);
					}
				}
				
				
				try {
					if(gafListTipoFuncionCargaHorariaDtoMiembroConsejo != null){
						for(TipoFuncionCargaHorariaDto itemFuncion: gafListTipoFuncionCargaHorariaDtoMiembroConsejo){
							List <CargaHoraria> gafListaCargasHorarias =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), itemFuncion.getTifncrhrId(), gafPeriodoAcademico.getPracId());
							if(gafListaCargasHorarias.size() > 0 ){
								gafCargaHorariaActiva = true;
								for(CargaHoraria item: gafListaCargasHorarias ){
									if (item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId() == itemFuncion.getTifncrhrId()) {
//										itemFuncion.setTifncrhrIsCheck(Boolean.TRUE);
										gafTipoFuncionCargaHorariaDtoAsignacionId = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId();
										gafHorasSemanalesMiembroConsejo = item.getCrhrTipoFuncionCargaHoraria().getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
									}
//									else {
//										itemFuncion.setTifncrhrIsCheck(Boolean.FALSE);
//									}
									
								}
							}
						}
					}
					 
					
				} catch (CargaHorariaNoEncontradoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
				} catch (CargaHorariaException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.exception")));
				}

			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.exception")));
			}

		}
		
		
		private void listarFuncionesMiembrosGestion() {
			gafListTipoFuncionCargaHorariaDtoMiembroGestion = new ArrayList<>();

			try {
				List<TipoFuncionCargaHoraria> auxListTipoFuncionCargaHoraria = servGafTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_MIEMBRO_GESTION_VALUE);

				if (auxListTipoFuncionCargaHoraria != null && auxListTipoFuncionCargaHoraria.size() > 0) {
					for (TipoFuncionCargaHoraria item : auxListTipoFuncionCargaHoraria) {
						FuncionCargaHorariaDto funcion = new FuncionCargaHorariaDto();
						TipoCargaHorariaDto tipo = new TipoCargaHorariaDto();
						TipoFuncionCargaHorariaDto tipoFuncion = new TipoFuncionCargaHorariaDto();
						List<Integer> horas = new ArrayList<>();

						funcion.setFncrhrId(item.getTifncrhrId());
						funcion.setFncrhrDescripcion(item.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
						funcion.setFncrhrHorasAsignadas(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas());
						funcion.setFncrhrHorasMinimo(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo());
						funcion.setFncrhrHorasMaximo(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo());
						tipo.setTicrhrId(item.getTifncrhrTipoCargaHoraria().getTicrhrId());
						tipo.setTicrhrDescripcion(item.getTifncrhrTipoCargaHoraria().getTicrhrDescripcion());
						tipo.setTicrhrEstado(item.getTifncrhrTipoCargaHoraria().getTicrhrEstado());

						if (funcion.getFncrhrHorasAsignadas() == null) {
//							for (int i = funcion.getFncrhrHorasMinimo(); i <= funcion.getFncrhrHorasMaximo(); i++) {
								horas.add(funcion.getFncrhrHorasMinimo());
								horas.add(Integer.valueOf(10));
								horas.add(funcion.getFncrhrHorasMaximo());
//							}
						} else {
							horas.add(funcion.getFncrhrHorasAsignadas());
						}
						
						tipoFuncion.setTifncrhrId(item.getTifncrhrId());
						tipoFuncion.setTifncrhrIsCheck(Boolean.FALSE);
						tipoFuncion.setTifncrhrFuncionCargaHorariaDto(funcion);
						tipoFuncion.setTifncrhrTipoCargaHorariaDto(tipo);
						tipoFuncion.setTifncrhrListHoras(horas);
						tipoFuncion.setTifncrhrDisabledIsCheck(Boolean.TRUE);

						gafListTipoFuncionCargaHorariaDtoMiembroGestion.add(tipoFuncion);
					}
				}
				
				try {
					if(gafListTipoFuncionCargaHorariaDtoMiembroGestion != null){
						for(TipoFuncionCargaHorariaDto itemFuncion: gafListTipoFuncionCargaHorariaDtoMiembroGestion){
							List <CargaHoraria> gafListaCargasHorarias =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), itemFuncion.getTifncrhrId(), gafPeriodoAcademico.getPracId());
							if(gafListaCargasHorarias.size() > 0 ){
								gafCargaHorariaActiva = true;
								for(CargaHoraria item: gafListaCargasHorarias ){
									if (item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId() == itemFuncion.getTifncrhrId()) {
										itemFuncion.setTifncrhrIsCheck(Boolean.TRUE);
										itemFuncion.setTifncrhrDisabledIsCheck(Boolean.FALSE);
									}
									
								}
							}
						}
					}
					 
					
				} catch (CargaHorariaNoEncontradoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
				} catch (CargaHorariaException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.exception")));
				}

			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(
						new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades
						.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.exception")));
			}

		}

		private void listarFuncionesCoordinador() {
			gafListTipoFuncionCargaHorariaDtoCoordinador = new ArrayList<>();

			List<TipoFuncionCargaHoraria> auxListTipoFuncionCargaHoraria = null;

			try {
				auxListTipoFuncionCargaHoraria = servGafTipoFuncionCargaHorariaServicio.listarTodosActivosXTipo(TipoCargaHorariaConstantes.TIPO_COORDINADOR_VALUE);
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AseguramientoCalidad.listar.funciones.exception")));
			}

			if (auxListTipoFuncionCargaHoraria != null && auxListTipoFuncionCargaHoraria.size() > 0) {
				for (TipoFuncionCargaHoraria item : auxListTipoFuncionCargaHoraria) {
					FuncionCargaHorariaDto funcion = new FuncionCargaHorariaDto();
					TipoCargaHorariaDto tipo = new TipoCargaHorariaDto();
					TipoFuncionCargaHorariaDto tipoFuncion = new TipoFuncionCargaHorariaDto();
					List<Integer> horas = new ArrayList<>();

					funcion.setFncrhrId(item.getTifncrhrId());
					funcion.setFncrhrDescripcion(item.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
					funcion.setFncrhrHorasAsignadas(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas());
					funcion.setFncrhrHorasMinimo(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo());
					funcion.setFncrhrHorasMaximo(item.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo());
					tipo.setTicrhrId(item.getTifncrhrTipoCargaHoraria().getTicrhrId());
					tipo.setTicrhrDescripcion(item.getTifncrhrTipoCargaHoraria().getTicrhrDescripcion());
					tipo.setTicrhrEstado(item.getTifncrhrTipoCargaHoraria().getTicrhrEstado());

					if (funcion.getFncrhrHorasAsignadas() == null) {
						if (item.getTifncrhrFuncionCargaHoraria().getFncrhrId() == 45) {
							horas.add(6);
							horas.add(10);
						}else if (item.getTifncrhrFuncionCargaHoraria().getFncrhrId() == 79) {
							horas.add(4);
							horas.add(6);
						}else{
							for (int i = funcion.getFncrhrHorasMinimo(); i <= funcion.getFncrhrHorasMaximo(); i++) {
								horas.add(i);
							}
						}
					} else {
						horas.add(funcion.getFncrhrHorasAsignadas());
					}
					tipoFuncion.setTifncrhrId(item.getTifncrhrId());
					tipoFuncion.setTifncrhrIsCheck(Boolean.FALSE);
					tipoFuncion.setTifncrhrFuncionCargaHorariaDto(funcion);
					tipoFuncion.setTifncrhrTipoCargaHorariaDto(tipo);
					tipoFuncion.setTifncrhrListHoras(horas);
					tipoFuncion.setTifncrhrDisabledIsCheck(Boolean.TRUE);

					gafListTipoFuncionCargaHorariaDtoCoordinador.add(tipoFuncion);
				}
			}
			
			try {
				if(gafListTipoFuncionCargaHorariaDtoCoordinador != null){
					for(TipoFuncionCargaHorariaDto itemFuncion: gafListTipoFuncionCargaHorariaDtoCoordinador){
						List <CargaHoraria> gafListaCargasHorarias =  servGafCargaHorariaServicio.buscarPorDetallePuesto(gafDocente.getDtpsId(), itemFuncion.getTifncrhrId(), gafPeriodoAcademico.getPracId());
						if(gafListaCargasHorarias.size() > 0 ){
							gafCargaHorariaActiva = true;
							for(CargaHoraria item: gafListaCargasHorarias ){
								if (item.getCrhrTipoFuncionCargaHoraria().getTifncrhrId() == itemFuncion.getTifncrhrId()) {
									itemFuncion.setTifncrhrIsCheck(Boolean.TRUE);
									itemFuncion.setTifncrhrDisabledIsCheck(Boolean.FALSE);
								}
								
							}
						}
					}
				}
				 
				
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.carga.horaria.docente.carga.horaria.exception")));
			}

		}

		
		
		
		
		
		
	  
		
		// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
		
		/**
		 * Calcula las horas para de gestion academica en base a las reglas y valores en base de datos
		 */
		public void calcularHoras(){
			
			try {
				if (gafFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
					if (gafFuncionSeleccion != TipoFuncionCargaHorariaConstantes.FUNCION_OTROS_AUTORIZADOS_GESTION_ACADEMICA_VALUE) { 
						gafListaHorasOtros = null;
						gafHorasOtrosSeleccion = GeneralesConstantes.APP_ID_BASE;
						gafTipoFuncionCargaHorariaSeleccion = servGafTipoFuncionCargaHorariaServicio.buscarPorId(gafFuncionSeleccion);
//						gafHorasSemanales = gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
					
						gafListaHorasGestion = new ArrayList<>();
						for(int i = gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
							gafListaHorasGestion.add(i); 
						}
					
					} else {
						gafHorasSemanales = null;
						gafTipoFuncionCargaHorariaSeleccion = servGafTipoFuncionCargaHorariaServicio.buscarPorId(gafFuncionSeleccion);
						gafListaHorasOtros = new ArrayList<>();
						for(int i = gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= gafTipoFuncionCargaHorariaSeleccion.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
							gafListaHorasOtros.add(i); 
						} 
					}
				}else{
					gafHorasSemanales = null;
					gafHorasOtrosSeleccion = GeneralesConstantes.APP_ID_BASE;
					gafListaHorasOtros = null; 
				}

			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.calcular.horas.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.calcular.horas.exception")));
			}
		}
		
		/**
		 * Calcula otras horas para coordinadores en base a las reglas y valores en base de datos
		 */
		public void calcularOtrasHoras(){
			
			try {
				 
				if (gafHorasOtrosSeleccion != GeneralesConstantes.APP_ID_BASE) { 
					gafTipoFuncionCargaHorariaSeleccion = servGafTipoFuncionCargaHorariaServicio.buscarPorId(gafFuncionSeleccion);
					gafHorasSemanales = gafHorasOtrosSeleccion; 					
				} else {
					gafHorasSemanales = null;
				}

			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.calcular.otras.horas.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.calcular.otras.horas.exception")));
			}
		}
		
		
		/**
		 * Calcula las horas en aseguramiento de calidad en base a las reglas y
		 * valores en base de datos
		 */ 
		public void calcularHorasMiembrosConsejo() {
			
			try {
				if (gafTipoFuncionCargaHorariaDtoAsignacionId != GeneralesConstantes.APP_ID_BASE) {
					gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida = servGafTipoFuncionCargaHorariaServicio.buscarPorId(gafTipoFuncionCargaHorariaDtoAsignacionId);
					gafHorasSemanalesMiembroConsejo = gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
					
					gafTipoFuncionCargaHorariaDtoMiembroConsejo = new TipoFuncionCargaHorariaDto();
					gafTipoFuncionCargaHorariaDtoMiembroConsejo.setTifncrhrIsCheck(Boolean.TRUE);
					gafTipoFuncionCargaHorariaDtoMiembroConsejo.setTifncrhrEstado(0);
					gafTipoFuncionCargaHorariaDtoMiembroConsejo.setTifncrhrHoraSeleccionada(gafHorasSemanalesMiembroConsejo);
					FuncionCargaHorariaDto funcion = new FuncionCargaHorariaDto();
					funcion.setFncrhrHorasAsignadas(gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas());
					funcion.setFncrhrDescripcion(gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
					gafTipoFuncionCargaHorariaDtoMiembroConsejo.setTifncrhrFuncionCargaHorariaDto(funcion);
					
					for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroConsejo) {
						if (item.getTifncrhrId() == gafTipoFuncionCargaHorariaDtoAsignacionId) {
							item.setTifncrhrIsCheck(true);
						}else {
							item.setTifncrhrIsCheck(false);
						}
					}
					
				} else {
					gafHorasSemanalesMiembroConsejo = null;
					gafTipoFuncionCargaHorariaDtoMiembroConsejo = null;
					gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida = null;
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
				if(gafDocente!=null){
					verificar = true;
				}
				return verificar;
		}
		
		/**
		 * Verifica el acceso del docente a los diferentes tipos de carga horaria
		 * otorgando un estado true / false para activar las casillas
		 */
		public void verificarAcceso(){
			Boolean verificarFuncion = false;
			

			if(gafDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
					|| gafDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){ 
				verificarFuncion = false; 
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.acceso.relacion.laboral.validacion.exception")));
			}else{ 
				switch (gafDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE://Tiempo Completo
					verificarFuncion = true;
					gafHabilitarFuncionesCaHr = true;
				 	break;
				default: 
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.acceso.validacion")));
					break;
				}
			}
			
			if(verificarFuncion){
				// gestion academica
				listarFuncionesGestion();
				// direccion academica
				listarFuncionesMiembrosConsejo();
				listarFuncionesMiembrosGestion();
				listarFuncionesCoordinador();
				
			}
		}
		
		/**
		 * Método que permite verificar si un docente tiempo completo ya dispone funciones de Direccion
		 * @return true - si dispone alguna asignacion del tipo Direccion.
		 */
		private boolean verificarDireccion(){
			boolean retorno = false;

			if (gafCargaHorariaGestionActual != null) {
				if (gafCargaHorariaGestionActual.getCrhrTipoFuncionCargaHoraria().getTifncrhrTipoCargaHoraria().getTicrhrId() == TipoCargaHorariaConstantes.TIPO_DIRECCION_VALUE) {
					retorno = true;		
				}
			}
			
			return retorno;
		}
		
		/**
		 * Método que permite controlar los registros de la tabla.
		 * @param item - parametro para realizar cambios.
		 */
		public void cambiarEstadosTabla(TipoFuncionCargaHorariaDto item){
			if (item.getTifncrhrIsCheck()) {
				item.setTifncrhrDisabledIsCheck(Boolean.FALSE);
			}else {
				item.setTifncrhrDisabledIsCheck(Boolean.TRUE);
			}
		}
		
		
		/**
		 * Verifica campos, parametos antes de guardar las distintas cargas horarias
		 * @return Estado false por culquier error al ingresar, true - pasa las validaciones 
		 **/
		public Boolean verificarGuardar() {
			
			Boolean verificar = true;
			
			if(verificarDocente()){ 
				
				//Gestion academica 
					if (gafFuncionSeleccion != GeneralesConstantes.APP_ID_BASE) {
						if(gafUnidadAcademica != null && gafUnidadAcademica.replaceAll(" ", "").length()!=0){
							if (gafFuncionSeleccion == TipoFuncionCargaHorariaConstantes.FUNCION_OTROS_AUTORIZADOS_GESTION_ACADEMICA_VALUE) {
								if (gafHorasOtrosSeleccion == GeneralesConstantes.APP_ID_BASE) { 
									verificar = false;
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.guardar.otras.horas.validacion")));
								}
							}
							if(gafHorasSemanales == null){
								verificar = false;
							}
						}else{
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.guardar.unidad.academica.validacion")));
						}
					}
					
					
			}else{
				verificar = false;
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.guardar.docente.validacion.exception")));
			} 
			
			return verificar;
		}
		
		
		/**
		 * Verifica campos, parametos antes de guardar las distintas cargas horarias
		 * @return Estado false por culquier error al ingresar, true - pasa las validaciones
		 **/
		public Boolean verificarGuardarGestion() {

			Boolean verificar = true;

			if (verificarDocente()) {

					// si ya tiene algo en Direccion no puede Gestion
					// si esta por lo menos uno seleccionado
					// si solo si numero es <=20
					
						int count = 0;

//						if (gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida != null) {
//								count = count + gafTipoFuncionCargaHorariaMiembroConsejoAsignacionElejida.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
//						}
						if (gafListTipoFuncionCargaHorariaDtoMiembroConsejo != null	&& gafListTipoFuncionCargaHorariaDtoMiembroConsejo.size() > 0) {
							for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroConsejo) {
								if (item.getTifncrhrIsCheck()) {
									count  = count + item.getTifncrhrFuncionCargaHorariaDto().getFncrhrHorasAsignadas();
								}
							}
						}

						if (gafListTipoFuncionCargaHorariaDtoMiembroGestion != null	&& gafListTipoFuncionCargaHorariaDtoMiembroGestion.size() > 0) {
							for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoMiembroGestion) {
								if (item.getTifncrhrIsCheck()) {
									count  = count + item.getTifncrhrHoraSeleccionada();
								}
							}
						}

						if (gafListTipoFuncionCargaHorariaDtoCoordinador != null && gafListTipoFuncionCargaHorariaDtoCoordinador.size() > 0) {
							for (TipoFuncionCargaHorariaDto item : gafListTipoFuncionCargaHorariaDtoCoordinador) {
								if (item.getTifncrhrIsCheck()) {
									count = count + item.getTifncrhrHoraSeleccionada();
								}
							}
						}

						if (count <= 0) {
							verificar = false;
							FacesUtil.mensajeError("Seleccione al menos una opción para continuar.");
						}else if (count > 20) {
							verificar = false;
							FacesUtil.mensajeError("Verifique que su asignación semanal no exceda 20 horas.");
						}
					
			} else {
				verificar = false;
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.validacion")));
			}

			return verificar;
		}
		
		
		/**
		 * Busca si existe un periodo activo
		 * @return retorna la entidad PeriodoAcademico activo en pregrado
		 **/
		public PeriodoAcademico verificarPeriodoActivo(){
			
			try {
				return servGafPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.periodo.activo.validacion.exception")));
				return null;
			} catch (PeriodoAcademicoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.periodo.activo.exception")));
				return null;
			}
		}
		
		/**
		 * Busca si existe un periodo de cierre
		 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
		 **/
		public PeriodoAcademico verificarPeriodoCierre(){
			
			try {
				return servGafPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
				return servGafPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
			} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.cronograma.no.encontrado.exception")));
				return null;
			} catch (PlanificacionCronogramaDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.cronograma.exception")));
				return null;
			}
		}
		
		/**
		 * Valida el cronograa para uso de la carga horaria
		 **/
		public Boolean validarCronograma(){
			
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			Boolean verificar = false;
			gafPlanificacionCronograma = verificarCronograma();
			if(gafPlanificacionCronograma != null){
				if(gafPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
					if(gafPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){
						verificar = true;
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.validar.cronograma.no.iniciado.validacion.exception")));
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.validar.cronograma.expirado.validacion.exception")));
				}
			}
			return verificar;
		}
		
		/**
		 * Verifica la existencia de un registro en ese cargo para uso de la carga horaria
		 **/
		public Boolean verificarCargoLibre(){
			Boolean verificar = false;
			
			try {
				if(gafTipoFuncionCargaHorariaSeleccion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_RECTOR_VALUE
						|| gafTipoFuncionCargaHorariaSeleccion.getTifncrhrId() == TipoFuncionCargaHorariaConstantes.FUNCION_VICERRECTOR_VALUE ){
					List<CargaHoraria> listaCargaHoraria = servGafCargaHorariaServicio.buscarPorTipFncCrHr(gafTipoFuncionCargaHorariaSeleccion.getTifncrhrId(), gafPeriodoAcademico.getPracId());
					if(listaCargaHoraria != null && listaCargaHoraria.size() != 0){
						if(listaCargaHoraria != null && listaCargaHoraria.size() == 1){
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.cargo.validacion.exception")));
						} else if(listaCargaHoraria != null && listaCargaHoraria.size() > 1){
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.verificar.cargo.multiple.validacion.exception")));
						}
					}else{
						verificar = true;
					}
				}else{
					verificar = true;
				}
				
			} catch (CargaHorariaNoEncontradoException e) {
				verificar = true;
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
			return verificar;
		}
		
		/**
		 * Carga los periodos en la lista para mostrar.
		 */
		public void cagarPeriodos(){
			
			gafListaPeriodosAcademicos = new ArrayList<>();
			
			PeriodoAcademico pracActivo = verificarPeriodoActivo();
			if(pracActivo != null){
				gafListaPeriodosAcademicos.add(pracActivo);
				gafPeriodoAcademico = pracActivo;
				gafPeriodoAcademicoBuscar = pracActivo.getPracId();
			}
			
			PeriodoAcademico pracCierre = verificarPeriodoCierre();
			if(pracCierre != null){
				gafListaPeriodosAcademicos.add(pracCierre);
			}
		}
		

	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

		public List<Dependencia> getGafListaFacultades() {
			return gafListaFacultades;
		}

		public void setGafListaFacultades(List<Dependencia> gafListaFacultades) {
			this.gafListaFacultades = gafListaFacultades;
		}

		public List<Carrera> getGafListaCarreras() {
			return gafListaCarreras;
		}

		public void setGafListaCarreras(List<Carrera> gafListaCarreras) {
			this.gafListaCarreras = gafListaCarreras;
		}

		public List<PersonaDatosDto> getGafListaDocentes() {
			return gafListaDocentes;
		}

		public void setGafListaDocentes(List<PersonaDatosDto> gafListaDocentes) {
			this.gafListaDocentes = gafListaDocentes;
		}

		public List<TipoFuncionCargaHoraria> getGafListaFunciones() {
			return gafListaFunciones;
		}

		public void setGafListaFunciones(List<TipoFuncionCargaHoraria> gafListaFunciones) {
			this.gafListaFunciones = gafListaFunciones;
		}

		public List<Integer> getGafListaHorasOtros() {
			return gafListaHorasOtros;
		}

		public void setGafListaHorasOtros(List<Integer> gafListaHorasOtros) {
			this.gafListaHorasOtros = gafListaHorasOtros;
		}

		public PersonaDatosDto getGafDocente() {
			return gafDocente;
		}

		public void setGafDocente(PersonaDatosDto gafDocente) {
			this.gafDocente = gafDocente;
		}

		public Integer getGafHorasOtrosSeleccion() {
			return gafHorasOtrosSeleccion;
		}

		public void setGafHorasOtrosSeleccion(Integer gafHorasOtrosSeleccion) {
			this.gafHorasOtrosSeleccion = gafHorasOtrosSeleccion;
		}

		public Integer getGafFuncionSeleccion() {
			return gafFuncionSeleccion;
		}

		public void setGafFuncionSeleccion(Integer gafFuncionSeleccion) {
			this.gafFuncionSeleccion = gafFuncionSeleccion;
		}

		public Integer getGafFacultadBuscar() {
			return gafFacultadBuscar;
		}

		public void setGafFacultadBuscar(Integer gafFacultadBuscar) {
			this.gafFacultadBuscar = gafFacultadBuscar;
		}

		public Integer getGafCarreraBuscar() {
			return gafCarreraBuscar;
		}

		public void setGafCarreraBuscar(Integer gafCarreraBuscar) {
			this.gafCarreraBuscar = gafCarreraBuscar;
		}

		public Integer getGafDocenteBuscar() {
			return gafDocenteBuscar;
		}

		public void setGafDocenteBuscar(Integer gafDocenteBuscar) {
			this.gafDocenteBuscar = gafDocenteBuscar;
		}

		public String getGafIdentificacionBuscar() {
			return gafIdentificacionBuscar;
		}

		public void setGafIdentificacionBuscar(String gafIdentificacionBuscar) {
			this.gafIdentificacionBuscar = gafIdentificacionBuscar;
		}
 
		public Integer getGafHorasSemanales() {
			return gafHorasSemanales;
		}

		public void setGafHorasSemanales(Integer gafHorasSemanales) {
			this.gafHorasSemanales = gafHorasSemanales;
		}

		public String getGafUnidadAcademica() {
			return gafUnidadAcademica;
		}

		public void setGafUnidadAcademica(String gafUnidadAcademica) {
			this.gafUnidadAcademica = gafUnidadAcademica;
		}

		public Boolean getGafCargaHorariaActiva() {
			return gafCargaHorariaActiva;
		}

		public void setGafCargaHorariaActiva(Boolean gafCargaHorariaActiva) {
			this.gafCargaHorariaActiva = gafCargaHorariaActiva;
		}

		public Integer getGafPeriodoAcademicoBuscar() {
			return gafPeriodoAcademicoBuscar;
		}

		public void setGafPeriodoAcademicoBuscar(Integer gafPeriodoAcademicoBuscar) {
			this.gafPeriodoAcademicoBuscar = gafPeriodoAcademicoBuscar;
		}

		public List<PeriodoAcademico> getGafListaPeriodosAcademicos() {
			return gafListaPeriodosAcademicos;
		}

		public void setGafListaPeriodosAcademicos(List<PeriodoAcademico> gafListaPeriodosAcademicos) {
			this.gafListaPeriodosAcademicos = gafListaPeriodosAcademicos;
		}

		public String getGafCedulaDocenteBusquedaAvanzada() {
			return gafCedulaDocenteBusquedaAvanzada;
		}

		public void setGafCedulaDocenteBusquedaAvanzada(String gafCedulaDocenteBusquedaAvanzada) {
			this.gafCedulaDocenteBusquedaAvanzada = gafCedulaDocenteBusquedaAvanzada;
		}

		public String getGafApellidoDocenteBusquedaAvanzada() {
			return gafApellidoDocenteBusquedaAvanzada;
		}

		public void setGafApellidoDocenteBusquedaAvanzada(String gafApellidoDocenteBusquedaAvanzada) {
			this.gafApellidoDocenteBusquedaAvanzada = gafApellidoDocenteBusquedaAvanzada;
		}

		public String getGafMensajeBusquedaAvanzada() {
			return gafMensajeBusquedaAvanzada;
		}

		public void setGafMensajeBusquedaAvanzada(String gafMensajeBusquedaAvanzada) {
			this.gafMensajeBusquedaAvanzada = gafMensajeBusquedaAvanzada;
		}

		public List<PersonaDatosDto> getGafListDocentesBusquedaAvanzada() {
			return gafListDocentesBusquedaAvanzada;
		}

		public void setGafListDocentesBusquedaAvanzada(List<PersonaDatosDto> gafListDocentesBusquedaAvanzada) {
			this.gafListDocentesBusquedaAvanzada = gafListDocentesBusquedaAvanzada;
		}

		public String getGafLinkReporte() {
			return gafLinkReporte;
		}

		public void setGafLinkReporte(String gafLinkReporte) {
			this.gafLinkReporte = gafLinkReporte;
		}

		public List<Integer> getGafListaHorasGestion() {
			return gafListaHorasGestion;
		}

		public void setGafListaHorasGestion(List<Integer> gafListaHorasGestion) {
			this.gafListaHorasGestion = gafListaHorasGestion;
		}

		public Integer getGafHorasSemanalesMiembroConsejo() {
			return gafHorasSemanalesMiembroConsejo;
		}

		public void setGafHorasSemanalesMiembroConsejo(Integer gafHorasSemanalesMiembroConsejo) {
			this.gafHorasSemanalesMiembroConsejo = gafHorasSemanalesMiembroConsejo;
		}

		public Integer getGafTipoFuncionCargaHorariaDtoAsignacionId() {
			return gafTipoFuncionCargaHorariaDtoAsignacionId;
		}

		public void setGafTipoFuncionCargaHorariaDtoAsignacionId(Integer gafTipoFuncionCargaHorariaDtoAsignacionId) {
			this.gafTipoFuncionCargaHorariaDtoAsignacionId = gafTipoFuncionCargaHorariaDtoAsignacionId;
		}

		public TipoFuncionCargaHorariaDto getGafTipoFuncionCargaHorariaDtoMiembroConsejo() {
			return gafTipoFuncionCargaHorariaDtoMiembroConsejo;
		}

		public void setGafTipoFuncionCargaHorariaDtoMiembroConsejo(
				TipoFuncionCargaHorariaDto gafTipoFuncionCargaHorariaDtoMiembroConsejo) {
			this.gafTipoFuncionCargaHorariaDtoMiembroConsejo = gafTipoFuncionCargaHorariaDtoMiembroConsejo;
		}

		public List<TipoFuncionCargaHorariaDto> getGafListTipoFuncionCargaHorariaDtoMiembroConsejo() {
			return gafListTipoFuncionCargaHorariaDtoMiembroConsejo;
		}

		public void setGafListTipoFuncionCargaHorariaDtoMiembroConsejo(
				List<TipoFuncionCargaHorariaDto> gafListTipoFuncionCargaHorariaDtoMiembroConsejo) {
			this.gafListTipoFuncionCargaHorariaDtoMiembroConsejo = gafListTipoFuncionCargaHorariaDtoMiembroConsejo;
		}

		public Boolean getGafHabilitarFuncionesCaHr() {
			return gafHabilitarFuncionesCaHr;
		}

		public void setGafHabilitarFuncionesCaHr(Boolean gafHabilitarFuncionesCaHr) {
			this.gafHabilitarFuncionesCaHr = gafHabilitarFuncionesCaHr;
		}

		public List<TipoFuncionCargaHorariaDto> getGafListTipoFuncionCargaHorariaDtoMiembroGestion() {
			return gafListTipoFuncionCargaHorariaDtoMiembroGestion;
		}

		public void setGafListTipoFuncionCargaHorariaDtoMiembroGestion(
				List<TipoFuncionCargaHorariaDto> gafListTipoFuncionCargaHorariaDtoMiembroGestion) {
			this.gafListTipoFuncionCargaHorariaDtoMiembroGestion = gafListTipoFuncionCargaHorariaDtoMiembroGestion;
		}

		public List<TipoFuncionCargaHorariaDto> getGafListTipoFuncionCargaHorariaDtoCoordinador() {
			return gafListTipoFuncionCargaHorariaDtoCoordinador;
		}

		public void setGafListTipoFuncionCargaHorariaDtoCoordinador(
				List<TipoFuncionCargaHorariaDto> gafListTipoFuncionCargaHorariaDtoCoordinador) {
			this.gafListTipoFuncionCargaHorariaDtoCoordinador = gafListTipoFuncionCargaHorariaDtoCoordinador;
		}

		public Usuario getGafUsuario() {
			return gafUsuario;
		}

		public void setGafUsuario(Usuario gafUsuario) {
			this.gafUsuario = gafUsuario;
		}

		
		
}