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
   
 ARCHIVO:     DirectorCarreraForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Directores de Carrera. 
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
 * Clase (session bean) DirectorCarreraForm.java Bean de sesión que maneja
 * los atributos del formulario de Directores de Carrera.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "directorCarreraForm")
@SessionScoped
public class DirectorCarreraForm  extends ReporteCargaHorariaForm  implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario dcfUsuario;
	
	private List<Dependencia> dcfListaFacultades;
	private List<Carrera> dcfListaCarreras;
	private List<PersonaDatosDto> dcfListaDocentes; 
	private List<TipoFuncionCargaHoraria> dcfListaFuncionesConsejoRed;
	
	private PersonaDatosDto dcfDocente; 
	
	private Integer dcfFacultadBuscar;
	private Integer dcfCarreraBuscar;
	private Integer dcfDocenteBuscar;
	private String dcfIdentificacionBuscar;
	
	private TipoFuncionCargaHoraria dcfCoordinadorAraSlc;
	private Integer dcfHorasCoordinadorAraSlc;
	private List<Integer> dcfFuncionCoordinadorAraSlc;
	private String dcfCoordinadorAra;
	
	private TipoFuncionCargaHoraria dcfCoordinadorSubAraSlc;
	private Integer dcfHorasCoordinadorSubAraSlc;
	private List<Integer> dcfFuncionCoordinadorSubAraSlc;
	private String dcfCoordinadorSubAra;
	
	private Integer dcfHorasSemanalesCoordinador;
	 
	private TipoFuncionCargaHoraria dcfCoordinadorTtlSlc;
	private Integer dcfHorasCoordinadorTtlSlc;  
	private List<Integer> dcfFuncionCoordinadorTtlSlc;  
	private String dcfCoordinadorTtl; 
	private Integer dcfNumEstudiantesTitulacion;
	
	private TipoFuncionCargaHoraria dcfClinicasSlc;
	private String dcfUnidadAcademica;
	private String dcfAsignatura;
	private Integer dcfNumEstudiantes;
	private Integer dcfHorasSemanalesCliPra;
	  
	private TipoFuncionCargaHoraria dcfExamenSlc;
	private List<Integer> dcfFuncionSeleccionExamen;
	private Integer dcfHorasSemanalesExaReac;
	
	private Boolean dcfEditarCoordinador;
	private Boolean dcfEditarClinicas;
	private Boolean dcfEditarOtrosAsignados;
	private Boolean dcfEditarExamen;
	
	private PlanificacionCronogramaDto dcfPlanificacionCronograma;
	
	private List<CargaHoraria> dcfListaCargasHorarias;
	private List<DetalleCargaHoraria> dcfListaDetalleCargaHoraria; 
	
	private Boolean dcfCargaHorariaActiva;
	
	//Coordinadores
		//--Area
	private CargaHoraria dcfCargaHorariaCooArea;
	private DetalleCargaHoraria dcfDetalleCargaHorariaCooArea;
		//--SubArea
	private CargaHoraria dcfCargaHorariaCooSubArea;
	private DetalleCargaHoraria dcfDetalleCargaHorariaCooSubArea;
		//--Titulacion
	private CargaHoraria dcfCargaHorariaCooTtl;
	private DetalleCargaHoraria dcfDetalleCargaHorariaCooTtl;
	
	//Miembros de Consejo
	private CargaHoraria dcfCargaHorariaConsejo;
	private DetalleCargaHoraria dcfDetalleCargaHorariaConsejo;

	//Clinicas Y Practicas
	private CargaHoraria dcfCargaHorariaClinicas;
	private DetalleCargaHoraria dcfDetalleCargaHorariaClinicas;
	
	//Examen Complexivo/Reactivos
	private CargaHoraria dcfCargaHorariaExamen;
	 
	
	private PeriodoAcademico dcfPeriodoAcademico;
	private Integer dcfPeriodoAcademicoBuscar;
	private List<PeriodoAcademico> dcfListaPeriodosAcademicos;
	
	private String dcfCedulaDocenteBusquedaAvanzada;
	private String dcfApellidoDocenteBusquedaAvanzada;
	private String dcfMensajeBusquedaAvanzada;
	private List<PersonaDatosDto> dcfListDocentesBusquedaAvanzada;
	
	//--v2
	//-Elaboracion de reactivos
	private TipoFuncionCargaHoraria dcfElaboracionReactivosSlc;
	private List<Integer> dcfFuncionSeleccionElaboracionReac;
	private Integer dcfHorasSemanalesElaboracionReac;
	private CargaHoraria dcfCargaHorariaElaboracionReac;
	
	private Integer dcfHorasSemanalesExamenCom;
	
	//-Lector de proyecto
//	private TipoFuncionCargaHoraria dcfLectorSlc; 
//	private Integer dcfHorasSemanalesLector;
//	private CargaHoraria dcfCargaHorariaLector;
//	private DetalleCargaHoraria dcfDetalleCargaHorariaLector;
//	private String dcfLector;
//	private List<Integer> dcfFuncionSeleccionLector;
	
	private Integer dcfHorasSemanalesOtrasAsig;
	
	//-Capacitacion Docente
	private TipoFuncionCargaHoraria dcfCapacitacionDocenteSlc;
	private List<Integer> dcfFuncionSeleccionCapacitacionDocente;
	private Integer dcfHorasSemanalesCapacitacionDocente;
	private CargaHoraria dcfCargaHorariaCapacitacionDocente;
	private DetalleCargaHoraria dcfDetalleCargaHorariaCapacitacionDocente;
	private String dcfCapacitacionDocente;
	
	//-Publicacion Academica
	private TipoFuncionCargaHoraria dcfPublicacionAcademicaSlc;
	private List<Integer> dcfFuncionSeleccionPublicacionAcademica;
	private Integer dcfHorasSemanalesPublicacionAcademica;
	private CargaHoraria dcfCargaHorariaPublicacionAcademica;
	private DetalleCargaHoraria dcfDetalleCargaHorariaPublicacionAcademica;
	private String dcfPublicacionAcademica;
	
	//-Coordinador diseno/rediseno
	private TipoFuncionCargaHoraria dcfCoordinadorDisenoSlc;
	private List<Integer> dcfFuncionSeleccionDiseno;
	private Integer dcfHorasCoordinadorDisenoSlc;
	private CargaHoraria dcfCargaHorariaCooDiseno;
	
	//-Banderas
	private Boolean dcfEditarLector;
	private Boolean dcfEditarCoordinadorDiseno;
	
	//-Clinicas y Practicas
	private List<Integer> dcfListaHorasClinicas;
	
	//Reactivos
	private Boolean dcfEditarReactvos;
	
	//--v3
	//URL reporte
	private String dcfLinkReporte;
	
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
		DependenciaServicio servDcfDependenciaServicio;
		@EJB
		CarreraServicio servDcfCarreraServicio;
		@EJB
		PersonaDatosDtoServicioJdbc servDcfPersonaDatosServicioJdbc;
		@EJB
		PersonaServicio servDcfPersonaServicio;
		@EJB
		TipoFuncionCargaHorariaServicio servDcfTipoFuncionCargaHorariaServicio;
		@EJB
		PeriodoAcademicoServicio servDcfPeriodoAcademicoServicio;
		@EJB
		PlanificacionCronogramaDtoServicioJdbc servDcfPlanificacionCronogramaDtoServicioJdbc;
		@EJB
		CargaHorariaServicio servDcfCargaHorariaServicio;
		@EJB
		DetalleCargaHorariaServicio servDcfDetalleCargaHorariaServicio;
		@EJB
		UsuarioRolServicio servDcfUsuarioRolServicio;
		@EJB
		DocenteDatosDtoServicioJdbc servDcfDocenteDatosDtoServicioJdbc;
		
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
				 
				rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
				dcfEditarClinicas = false;
				dcfEditarExamen = false;
				dcfEditarCoordinador = false;
				dcfEditarOtrosAsignados = false;
				dcfCargaHorariaActiva = false;
				dcfEditarLector = false;
				dcfEditarCoordinadorDiseno = false;
				
//				if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
					validarCronograma();
					cagarPeriodos();
					dcfListaFacultades = servDcfDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);

					Iterator it = dcfListaFacultades.iterator();
					while(it.hasNext()){
						Dependencia cad = (Dependencia) it.next();
						if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE){
							it.remove();
						}
					}
					
//				}			 
			} catch (DependenciaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.iniciar.parametros.dependencia.no.encontrado.exception")));
			}
		}

		// >>--------------------------------------------NAVEGACION----------------------------------------------

		/**
		 * Incicializa las variables para el inicio de la funcionalidad
		 * 
		 * @return navegacion hacia el formulario de proyectos semilla.
		 */
		public String irFormularioDirectorCarrera(Usuario usuario) {
			dcfUsuario = usuario;
			inicarParametros();
			return "irFormularioDirectorCarrera";
		}
		

		/**
		 * Setea a la entidad y a la lista de entidades a null y redirige a la
		 * pagina de inicio
		 * 
		 * @return Navegacion a la pagina de inicio.
		 */
		public String irInicio() {
			
			dcfListaFacultades = null;
			dcfListaCarreras = null;
			dcfListaDocentes = null; 
			dcfDocente = null;  
			dcfFacultadBuscar = null;
			dcfCarreraBuscar = null;
			dcfDocenteBuscar = null;
			dcfIdentificacionBuscar = null; 
			
			dcfEditarClinicas = false;
			dcfEditarExamen = false;
			dcfEditarCoordinador = false;
			dcfEditarOtrosAsignados = false;
			 
			dcfListaFuncionesConsejoRed = null;
			
			dcfDocente = null;  
			
			dcfFuncionCoordinadorAraSlc = null;
			dcfFuncionCoordinadorSubAraSlc = null;
			dcfFuncionCoordinadorTtlSlc = null;
			
			dcfCoordinadorAraSlc = null;
			dcfCoordinadorSubAraSlc = null;
			dcfCoordinadorTtlSlc = null;
			
			dcfHorasCoordinadorAraSlc = null;
			dcfHorasCoordinadorSubAraSlc = null;
			dcfHorasCoordinadorTtlSlc = null;
			
			dcfCoordinadorAra = null;
			dcfCoordinadorSubAra = null;
			dcfCoordinadorTtl = null;
			
			dcfNumEstudiantesTitulacion = null;
			dcfHorasSemanalesCoordinador = null;
			
			dcfClinicasSlc = null;
			dcfUnidadAcademica = null;
			dcfAsignatura = null;
			dcfNumEstudiantes = null;
			dcfHorasSemanalesCliPra = null;
			 
			dcfExamenSlc = null;
			dcfFuncionSeleccionExamen = null;
			dcfHorasSemanalesExaReac = null;
			
			dcfCargaHorariaActiva = false;
			
			dcfCargaHorariaCooArea = null;
			dcfDetalleCargaHorariaCooArea = null;
			dcfCargaHorariaCooSubArea = null;
			dcfDetalleCargaHorariaCooSubArea = null;
			dcfCargaHorariaCooTtl = null;
			dcfDetalleCargaHorariaCooTtl = null;
			dcfCargaHorariaConsejo = null;
			dcfDetalleCargaHorariaConsejo = null;
			dcfCargaHorariaClinicas = null;
			dcfDetalleCargaHorariaClinicas = null;
			dcfCargaHorariaExamen = null;

			dcfPeriodoAcademicoBuscar = null;
			dcfListaPeriodosAcademicos = null;
			
			dcfElaboracionReactivosSlc = null;
			dcfFuncionSeleccionElaboracionReac = null;
			dcfHorasSemanalesElaboracionReac = null;
			dcfHorasSemanalesExamenCom = null; 
			
			dcfCargaHorariaElaboracionReac = null;

//			dcfFuncionSeleccionLector = null;
//			dcfLectorSlc = null;
//			dcfHorasSemanalesLector = null;
//			dcfCargaHorariaLector = null;
//			dcfDetalleCargaHorariaLector = null;
//			dcfLector = null;
			
			dcfHorasSemanalesOtrasAsig = null;
			 
			dcfCapacitacionDocenteSlc = null;
			dcfFuncionSeleccionCapacitacionDocente = null;
			dcfHorasSemanalesCapacitacionDocente = null;
			dcfCargaHorariaCapacitacionDocente = null;
			dcfDetalleCargaHorariaCapacitacionDocente = null;
			dcfCapacitacionDocente = null;
			 
			dcfPublicacionAcademicaSlc = null;
			dcfFuncionSeleccionPublicacionAcademica = null;
			dcfHorasSemanalesPublicacionAcademica = null;
			dcfCargaHorariaPublicacionAcademica = null;
			dcfDetalleCargaHorariaPublicacionAcademica = null;
			dcfPublicacionAcademica = null;
			
			dcfCoordinadorDisenoSlc = null;
			dcfFuncionSeleccionDiseno = null;
			dcfHorasCoordinadorDisenoSlc = null;
			dcfCargaHorariaCooDiseno = null;

			dcfEditarLector = false;
			dcfEditarCoordinadorDiseno = false;
			
			dcfListaHorasClinicas = null;
			
			dcfEditarReactvos = false;
			
			dcfLinkReporte = null;
			
			return "irInicio";
		}

		// >>-------------------------------------------LIMPIEZA-------------------------------------------------

		/**
		 * Setea y nulifica a los valores iniciales de cada parametro
		 */
		public void limpiar() {
			dcfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			dcfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE; 
			
			dcfIdentificacionBuscar = null; 
			
			dcfListaCarreras = null;
			dcfListaFacultades = null;
			dcfListaDocentes = null;  
			
			dcfEditarClinicas = false;
			dcfEditarExamen = false;
			dcfEditarReactvos = false;
			dcfEditarCoordinador = false;
			dcfEditarOtrosAsignados = false;
			 
			dcfListaFuncionesConsejoRed = null;
			
			dcfDocente = null;  
			
			dcfFuncionCoordinadorAraSlc = null;
			dcfFuncionCoordinadorSubAraSlc = null;
			dcfFuncionCoordinadorTtlSlc = null;
			
			dcfCoordinadorAraSlc = null;
			dcfCoordinadorSubAraSlc = null;
			dcfCoordinadorTtlSlc = null;
			
			dcfHorasCoordinadorAraSlc = null;
			dcfHorasCoordinadorSubAraSlc = null;
			dcfHorasCoordinadorTtlSlc = null;
			
			dcfCoordinadorAra = null;
			dcfCoordinadorSubAra = null;
			dcfCoordinadorTtl = null;
			
			dcfNumEstudiantesTitulacion = null;
			dcfHorasSemanalesCoordinador = null;
			
			dcfClinicasSlc = null;
			dcfUnidadAcademica = null;
			dcfAsignatura = null;
			dcfNumEstudiantes = null;
			dcfHorasSemanalesCliPra = null;
			 
			dcfExamenSlc = null;
			dcfFuncionSeleccionExamen = null;
			dcfHorasSemanalesExaReac = null;
			
			dcfCargaHorariaActiva = false;
			
			dcfCargaHorariaCooArea = null;
			dcfDetalleCargaHorariaCooArea = null;
			dcfCargaHorariaCooSubArea = null;
			dcfDetalleCargaHorariaCooSubArea = null;
			dcfCargaHorariaCooTtl = null;
			dcfDetalleCargaHorariaCooTtl = null;
			dcfCargaHorariaConsejo = null;
			dcfDetalleCargaHorariaConsejo = null;
			dcfCargaHorariaClinicas = null;
			dcfDetalleCargaHorariaClinicas = null;
			dcfCargaHorariaExamen = null;
			
			dcfPeriodoAcademico = verificarPeriodoActivo();

			dcfElaboracionReactivosSlc = null;
			dcfFuncionSeleccionElaboracionReac = null;
			dcfHorasSemanalesElaboracionReac = null;
			dcfHorasSemanalesExamenCom = null; 
			
			dcfCargaHorariaElaboracionReac = null;

//			dcfFuncionSeleccionLector = null;
//			dcfLectorSlc = null;
//			dcfHorasSemanalesLector = null;
//			dcfCargaHorariaLector = null;
//			dcfDetalleCargaHorariaLector = null;
//			dcfLector = null;
			
			dcfHorasSemanalesOtrasAsig = null;
			 
			dcfCapacitacionDocenteSlc = null;
			dcfFuncionSeleccionCapacitacionDocente = null;
			dcfHorasSemanalesCapacitacionDocente = null;
			dcfCargaHorariaCapacitacionDocente = null;
			dcfDetalleCargaHorariaCapacitacionDocente = null;
			dcfCapacitacionDocente = null;
			 
			dcfPublicacionAcademicaSlc = null;
			dcfFuncionSeleccionPublicacionAcademica = null;
			dcfHorasSemanalesPublicacionAcademica = null;
			dcfCargaHorariaPublicacionAcademica = null;
			dcfDetalleCargaHorariaPublicacionAcademica = null;
			dcfPublicacionAcademica = null;
			
			dcfCoordinadorDisenoSlc = null;
			dcfFuncionSeleccionDiseno = null;
			dcfHorasCoordinadorDisenoSlc = null;
			dcfCargaHorariaCooDiseno = null;

			dcfEditarLector = false;
			dcfEditarCoordinadorDiseno = false;
			
			dcfListaHorasClinicas = null;
			dcfLinkReporte = null;
			
			inicarParametros();
		}
		
		/**
		 * Limpia la seleccion de campos en base al docente
		 */
		public void limpiarInfoDocente(){ 
			
			dcfEditarClinicas = false;
			dcfEditarExamen = false;
			dcfEditarReactvos = false;
			dcfEditarCoordinador = false;
			dcfEditarOtrosAsignados = false;
			
			dcfDocente = null;  
			 
			dcfNumEstudiantesTitulacion = null;
			dcfHorasSemanalesCoordinador = null;
			
			dcfCoordinadorAra = null;
			dcfCoordinadorSubAra = null;
			dcfCoordinadorTtl = null;
			dcfHorasCoordinadorAraSlc = null;
			dcfHorasCoordinadorSubAraSlc = null;
			dcfHorasCoordinadorTtlSlc = null;
			
			dcfClinicasSlc = null;
			dcfUnidadAcademica = null;
			dcfAsignatura = null;
			dcfNumEstudiantes = null;
			dcfHorasSemanalesCliPra = null;
			  
			dcfExamenSlc = null;
			dcfFuncionSeleccionExamen = null;
			dcfHorasSemanalesExaReac = null;
			
			dcfFuncionCoordinadorAraSlc = null;
			dcfFuncionCoordinadorSubAraSlc = null;
			dcfFuncionCoordinadorTtlSlc = null;
			 
			dcfListaFuncionesConsejoRed = null;
			
			dcfCargaHorariaActiva = false;
			
			dcfCargaHorariaCooArea = null;
			dcfDetalleCargaHorariaCooArea = null;
			dcfCargaHorariaCooSubArea = null;
			dcfDetalleCargaHorariaCooSubArea = null;
			dcfCargaHorariaCooTtl = null;
			dcfDetalleCargaHorariaCooTtl = null;
			dcfCargaHorariaConsejo = null;
			dcfDetalleCargaHorariaConsejo = null;
			dcfCargaHorariaClinicas = null;
			dcfDetalleCargaHorariaClinicas = null;
			dcfCargaHorariaExamen = null;
			
			dcfPeriodoAcademico = verificarPeriodoActivo();
			
			dcfElaboracionReactivosSlc = null;
			dcfFuncionSeleccionElaboracionReac = null;
			dcfHorasSemanalesElaboracionReac = null;
			dcfHorasSemanalesExamenCom = null; 
			
			dcfCargaHorariaElaboracionReac = null;
			
//			dcfFuncionSeleccionLector = null;
//			dcfLectorSlc = null; 
//			dcfHorasSemanalesLector = null;
//			dcfCargaHorariaLector = null;
//			dcfDetalleCargaHorariaLector = null;
//			dcfLector = null;
			
			dcfHorasSemanalesOtrasAsig = null;
			 
			dcfCapacitacionDocenteSlc = null;
			dcfFuncionSeleccionCapacitacionDocente = null;
			dcfHorasSemanalesCapacitacionDocente = null;
			dcfCargaHorariaCapacitacionDocente = null;
			dcfDetalleCargaHorariaCapacitacionDocente = null;
			dcfCapacitacionDocente = null;
			 
			dcfPublicacionAcademicaSlc = null;
			dcfFuncionSeleccionPublicacionAcademica = null;
			dcfHorasSemanalesPublicacionAcademica = null;
			dcfCargaHorariaPublicacionAcademica = null;
			dcfDetalleCargaHorariaPublicacionAcademica = null;
			dcfPublicacionAcademica = null;
			 
			dcfCoordinadorDisenoSlc = null;
			dcfFuncionSeleccionDiseno = null;
			dcfHorasCoordinadorDisenoSlc = null;
			dcfCargaHorariaCooDiseno = null;

			dcfEditarLector = false;
			dcfEditarCoordinadorDiseno = false;
			
			dcfListaHorasClinicas = null; 
			dcfLinkReporte = null;
			
		}
		
		/**
		 * Limpia parametros del cuadro de dialogo busqueda avanzada
		 */
		public void limpiarBusquedaAvanzada(){
			dcfCedulaDocenteBusquedaAvanzada = null;
			dcfApellidoDocenteBusquedaAvanzada = null;
			dcfListDocentesBusquedaAvanzada = null;
			dcfMensajeBusquedaAvanzada = null;
			dcfLinkReporte = null;
		}

		// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

		/**
		 * Busca la entidad Docente basado en los parametros de ingreso
		 */
		public void buscar() {
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso	
				try {
					limpiarInfoDocente();
					if (dcfFacultadBuscar != GeneralesConstantes.APP_ID_BASE) {
						if (dcfDocenteBuscar != GeneralesConstantes.APP_ID_BASE) {
							dcfPeriodoAcademico = servDcfPeriodoAcademicoServicio.buscarPorId(dcfPeriodoAcademicoBuscar);
							dcfDocente = servDcfPersonaDatosServicioJdbc.buscarPorId(dcfDocenteBuscar , dcfPeriodoAcademicoBuscar);
							dcfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+dcfDocente.getPrsIdentificacion()+"&prd="+dcfPeriodoAcademico.getPracDescripcion();
							listarFuncionesConsejo();
							verificarAcceso();
							
						} else {
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.docente.validacion.exception")));
						}
					} else { 
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.facultad.validacion.exception")));
					}
	
				} catch (PersonaDatosDtoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.persona.datos.dto.no.encontrado.exception")));
				} catch (PersonaDatosDtoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.persona.datos.dto.exception")));
				} catch (PeriodoAcademicoNoEncontradoException e) { 
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.periodo.academico.no.encontrado.exception")));
				} catch (PeriodoAcademicoException e) {
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.periodo.academico.exception")));
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
				dcfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
				dcfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				dcfListaCarreras = null;
				dcfListaDocentes = null;
				
					dcfDocente = servDcfPersonaDatosServicioJdbc.buscarPorId(item.getPrsId() , dcfPeriodoAcademicoBuscar);
					listarFuncionesConsejo();
					verificarAcceso();
					limpiarBusquedaAvanzada();
					dcfLinkReporte = "http://reportes.uce.edu.ec/Docentes/CargaHoraria.aspx?idn="+item.getPrsIdentificacion()+"&prd="+dcfPeriodoAcademico.getPracDescripcion();
					
			} catch (PersonaDatosDtoNoEncontradoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.asignar.docente.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.asignar.docente.exception")));
			}
			
		}
		
		/**
		 * Busca docentes por identificacion y primer apellido en dialogo en busqueda avanzada
		 */
		public void buscarDocentes(){
//			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				try {
					
					if(dcfCedulaDocenteBusquedaAvanzada == null){
						dcfCedulaDocenteBusquedaAvanzada = "";
					}
					if(dcfApellidoDocenteBusquedaAvanzada == null){
						dcfApellidoDocenteBusquedaAvanzada = "";
					}
					dcfListDocentesBusquedaAvanzada = servDcfPersonaDatosServicioJdbc.buscarDocenteXIdentificacionXApellido(dcfCedulaDocenteBusquedaAvanzada, dcfApellidoDocenteBusquedaAvanzada);
					
				} catch (PersonaDatosDtoException e) {
					dcfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.docente.exception"));
				} catch (PersonaDatosDtoNoEncontradoException e) {
					dcfMensajeBusquedaAvanzada = MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.buscar.docente.no.encontrado.exception"));
				}
//			}
			
		}

		// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------

		
		/**
		 * Guarda los parametros del formulario
		 **/
		public void guardar() {
			 
			Boolean verificar = true;
			
			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
			
				if(verificarGuardar()){
					
					//Coordinadores
					if(dcfEditarCoordinador){
						
						//Coordinador de Area
						if (dcfFuncionCoordinadorAraSlc != null && dcfFuncionCoordinadorAraSlc.size() != 0) {
							if(verificar){
								verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
										dcfHorasCoordinadorAraSlc, 
										dcfCoordinadorAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCoordinadorAraSlc)
										, dcfCoordinadorAra , 0, dcfHorasCoordinadorAraSlc);
								}
							}
						
		
						// Coordinador de SubArea
						if (dcfFuncionCoordinadorSubAraSlc != null && dcfFuncionCoordinadorSubAraSlc.size() != 0) {
							if(verificar){
							verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
									dcfHorasCoordinadorSubAraSlc, 
									dcfCoordinadorSubAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									dcfPeriodoAcademico, 
									dcfCoordinadorSubAraSlc)
									, dcfCoordinadorSubAra , 0, dcfHorasCoordinadorSubAraSlc);
							}
						}
		 
					}
					
					if(dcfEditarCoordinadorDiseno){
					// Coordinador de Titulacion
						if (dcfFuncionCoordinadorTtlSlc != null && dcfFuncionCoordinadorTtlSlc.size() != 0) {
							if(verificar){
							verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
									dcfHorasCoordinadorTtlSlc, 
									dcfCoordinadorTtlSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									dcfPeriodoAcademico, 
									dcfCoordinadorTtlSlc)
									, dcfCoordinadorTtl , dcfNumEstudiantesTitulacion, dcfHorasCoordinadorTtlSlc);
							} 
						}
					}
					
					//Clinicas y Practicas 
					if(dcfEditarClinicas){ 
						if(dcfUnidadAcademica != null && dcfUnidadAcademica.replaceAll(" ", "").length()!=0){
							if(verificar){
							verificar = cargarDetalleCargaHorariaCliPra(cargarCargaHoraria(dcfDocente, 
									dcfHorasSemanalesCliPra, 
									dcfClinicasSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									dcfPeriodoAcademico, 
									dcfClinicasSlc)
									, dcfUnidadAcademica, dcfAsignatura, dcfNumEstudiantes, dcfHorasSemanalesCliPra);
							}
						}
					} 
	 
					//Examen Complexivo y Reativos
					if(dcfEditarExamen){
						if(dcfFuncionSeleccionExamen != null && dcfFuncionSeleccionExamen.size()!=0){
							if(verificar){
								if(cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesExamenCom, 
										dcfExamenSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfExamenSlc)!= null){
									verificar = true;
								}
							}
						}
					}
					
					//--v2
					// Elaboracion de reactivos
					if(dcfEditarReactvos){
						if(dcfFuncionSeleccionElaboracionReac != null && dcfFuncionSeleccionElaboracionReac.size()!=0){
							if(verificar){
								if(cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesElaboracionReac, 
										dcfElaboracionReactivosSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfElaboracionReactivosSlc)!= null){
									verificar = true;
								}
							}
						}
					}
					
					// Lector de Proyecto
	//				if(dcfEditarLector){
	//					if (dcfFuncionSeleccionLector != null && dcfFuncionSeleccionLector.size() != 0) {
	//						if(verificar){
	//						verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
	//								dcfHorasSemanalesLector, 
	//								dcfLectorSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
	//								dcfPeriodoAcademico, 
	//								dcfLectorSlc)
	//								, dcfLector , 0, dcfHorasSemanalesLector);
	//						} 
	//					}
	//				}
					
					if(dcfEditarOtrosAsignados){
						// Capacitacion Docente
						if (dcfFuncionSeleccionCapacitacionDocente != null && dcfFuncionSeleccionCapacitacionDocente.size() != 0) {
							if(verificar){
							verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
									dcfHorasSemanalesCapacitacionDocente, 
									dcfCapacitacionDocenteSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									dcfPeriodoAcademico, 
									dcfCapacitacionDocenteSlc)
									, dcfCapacitacionDocente , 0, dcfHorasSemanalesCapacitacionDocente);
							} 
						}
						
						// Publicaciones Academicas
						if (dcfFuncionSeleccionPublicacionAcademica != null && dcfFuncionSeleccionPublicacionAcademica.size() != 0) {
							if(verificar){
							verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
									dcfHorasSemanalesPublicacionAcademica, 
									dcfPublicacionAcademicaSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
									dcfPeriodoAcademico, 
									dcfPublicacionAcademicaSlc)
									, dcfPublicacionAcademica , 0, dcfHorasSemanalesPublicacionAcademica);
							} 
						}
					}
					 
					// Coordinador diseno/rediseno
					if(dcfEditarCoordinadorDiseno){  
						if(dcfFuncionSeleccionDiseno != null && dcfFuncionSeleccionDiseno.size()!=0){
							if(verificar){
								if(cargarCargaHoraria(dcfDocente, 
										dcfHorasCoordinadorDisenoSlc, 
										dcfCoordinadorDisenoSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCoordinadorDisenoSlc)!= null){
									verificar = true;
								}
							}
						} 	
					}
					
					if(verificar){
						limpiar();
					}
				} 
			}
		}
		 
		/**
		 * Actualiza los parametros del formulario
		 **/
		public void guardarCambios() {
			 Boolean verificar = false;
			 if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				 
				 if(verificarGuardar()){
					
					//Coordinadores
					if(dcfEditarCoordinador){
						
						//--Coordinador de Area
						if (dcfFuncionCoordinadorAraSlc != null && dcfFuncionCoordinadorAraSlc.size() != 0) {
							verificar = false;
							if(dcfCargaHorariaCooArea != null){
								dcfCargaHorariaCooArea.setCrhrTipoFuncionCargaHoraria(dcfCoordinadorAraSlc);
								dcfCargaHorariaCooArea.setCrhrObservacion(dcfCoordinadorAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaCooArea.setCrhrNumHoras(dcfHorasCoordinadorAraSlc);
								
								if(actualizarCargaHoraria(dcfCargaHorariaCooArea)){
									dcfDetalleCargaHorariaCooArea.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfCoordinadorAra).toUpperCase());
									dcfDetalleCargaHorariaCooArea.setDtcrhrNumHoras(dcfHorasCoordinadorAraSlc);
									
									verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooArea);
								}
								
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
										dcfHorasCoordinadorAraSlc, 
										dcfCoordinadorAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCoordinadorAraSlc)
										, dcfCoordinadorAra , 0, dcfHorasCoordinadorAraSlc);
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaCooArea != null){
								dcfCargaHorariaCooArea.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dcfCargaHorariaCooArea)){
									dcfDetalleCargaHorariaCooArea.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooArea); 
								}
							}
						}
					
						//--Coordinador de SubArea
						if (dcfFuncionCoordinadorSubAraSlc != null && dcfFuncionCoordinadorSubAraSlc.size() != 0) {
							verificar = false;
							if(dcfCargaHorariaCooSubArea != null){
								dcfCargaHorariaCooSubArea.setCrhrTipoFuncionCargaHoraria(dcfCoordinadorSubAraSlc);
								dcfCargaHorariaCooSubArea.setCrhrObservacion(dcfCoordinadorSubAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaCooSubArea.setCrhrNumHoras(dcfHorasCoordinadorSubAraSlc);
								
								if(actualizarCargaHoraria(dcfCargaHorariaCooSubArea)){
									dcfDetalleCargaHorariaCooSubArea.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfCoordinadorSubAra).toUpperCase());
									dcfDetalleCargaHorariaCooSubArea.setDtcrhrNumHoras(dcfHorasCoordinadorSubAraSlc);
									
									verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooSubArea);
								}
								
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
										dcfHorasCoordinadorSubAraSlc, 
										dcfCoordinadorSubAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCoordinadorSubAraSlc)
										, dcfCoordinadorSubAra , 0, dcfHorasCoordinadorSubAraSlc);
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaCooSubArea != null){
								dcfCargaHorariaCooSubArea.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dcfCargaHorariaCooSubArea)){
									dcfDetalleCargaHorariaCooSubArea.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooSubArea); 
								}
							}
						}
							
					}	
					
					if(dcfEditarCoordinadorDiseno){
						//--Coordinador de Titulacion
						if (dcfFuncionCoordinadorTtlSlc != null && dcfFuncionCoordinadorTtlSlc.size() != 0) {
							verificar = false;
							if(dcfCargaHorariaCooTtl != null){
								dcfCargaHorariaCooTtl.setCrhrTipoFuncionCargaHoraria(dcfCoordinadorTtlSlc);
								dcfCargaHorariaCooTtl.setCrhrObservacion(dcfCoordinadorTtlSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaCooTtl.setCrhrNumHoras(dcfHorasCoordinadorTtlSlc);
								
								if(actualizarCargaHoraria(dcfCargaHorariaCooTtl)){
									dcfDetalleCargaHorariaCooTtl.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfCoordinadorTtl).toUpperCase());
									dcfDetalleCargaHorariaCooTtl.setDtcrhrNumHoras(dcfHorasCoordinadorTtlSlc);
									
									verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooTtl);
								}
								
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								verificar =cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
										dcfHorasCoordinadorTtlSlc, 
										dcfCoordinadorTtlSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCoordinadorTtlSlc)
										, dcfCoordinadorTtl , dcfNumEstudiantesTitulacion, dcfHorasCoordinadorTtlSlc);
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaCooTtl != null){
								dcfCargaHorariaCooTtl.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dcfCargaHorariaCooTtl)){
									dcfDetalleCargaHorariaCooTtl.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooTtl); 
								}
							}
						}
					}
					
					//Clinicas y Practicas 
					if(dcfEditarClinicas){
						if(dcfUnidadAcademica != null && dcfUnidadAcademica.replaceAll(" ", "").length()!=0){
	
							verificar = false;
							if(dcfCargaHorariaClinicas != null){
								dcfCargaHorariaClinicas.setCrhrTipoFuncionCargaHoraria(dcfClinicasSlc);
								dcfCargaHorariaClinicas.setCrhrObservacion(dcfClinicasSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaClinicas.setCrhrNumHoras(dcfHorasSemanalesCliPra);
								
								if(actualizarCargaHoraria(dcfCargaHorariaClinicas)){
									dcfDetalleCargaHorariaClinicas.setDtcrhrUnidadAcademica(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfUnidadAcademica).toUpperCase());
									dcfDetalleCargaHorariaClinicas.setDtcrhrAsignaturaClinicas(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfAsignatura).toUpperCase());
									dcfDetalleCargaHorariaClinicas.setDtcrhrNumHoras(dcfHorasSemanalesCliPra);
									dcfDetalleCargaHorariaClinicas.setDtcrhrNumAlumnosClinicas(dcfNumEstudiantes);
									verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaClinicas);
								}
								
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								verificar = cargarDetalleCargaHorariaCliPra(cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesCliPra, 
										dcfClinicasSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfClinicasSlc)
										, dcfUnidadAcademica, dcfAsignatura, dcfNumEstudiantes, dcfHorasSemanalesCliPra); 
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaClinicas != null){
								dcfCargaHorariaClinicas.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dcfCargaHorariaClinicas)){
									dcfDetalleCargaHorariaClinicas.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaClinicas); 
								}
							}
						}
					} 
	 
					//Examen Complexivo y Reativos
					if(dcfEditarExamen){
						if(dcfFuncionSeleccionExamen != null && dcfFuncionSeleccionExamen.size()!=0){
		
							verificar = false;
							if(dcfCargaHorariaExamen != null){
								dcfCargaHorariaExamen.setCrhrTipoFuncionCargaHoraria(dcfExamenSlc);
								dcfCargaHorariaExamen.setCrhrObservacion(dcfExamenSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaExamen.setCrhrNumHoras(dcfHorasSemanalesExamenCom);
								
								verificar = actualizarCargaHoraria(dcfCargaHorariaExamen);
									
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								
								CargaHoraria crhr = cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesExamenCom, 
										dcfExamenSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfExamenSlc);
								
								if(crhr != null){
									verificar = true;
								}else{
									verificar = false;
								}
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaExamen != null){
								dcfCargaHorariaExamen.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								verificar =actualizarCargaHoraria(dcfCargaHorariaExamen);
							}
						}
					} 
					
					//--v2
					//--Elaboracion de Reativos
					if(dcfEditarReactvos){
						if(dcfFuncionSeleccionElaboracionReac != null && dcfFuncionSeleccionElaboracionReac.size()!=0){
		
							verificar = false;
							if(dcfCargaHorariaElaboracionReac != null){
								dcfCargaHorariaElaboracionReac.setCrhrTipoFuncionCargaHoraria(dcfElaboracionReactivosSlc);
								dcfCargaHorariaElaboracionReac.setCrhrObservacion(dcfElaboracionReactivosSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaElaboracionReac.setCrhrNumHoras(dcfHorasSemanalesElaboracionReac);
								
								verificar = actualizarCargaHoraria(dcfCargaHorariaElaboracionReac);
									
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{ 
								CargaHoraria crhr = cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesElaboracionReac, 
										dcfElaboracionReactivosSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfElaboracionReactivosSlc);
								
								if(crhr != null){
									verificar = true;
								}else{
									verificar = false;
								}
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaElaboracionReac != null){
								dcfCargaHorariaElaboracionReac.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								verificar =actualizarCargaHoraria(dcfCargaHorariaElaboracionReac);
							}
						}
					} 
					
	//				if(dcfEditarLector){
	//					
	//					//--Lector de Proyecto
	//					if (dcfFuncionSeleccionLector != null && dcfFuncionSeleccionLector.size() != 0) {
	//						verificar = false;
	//						if(dcfCargaHorariaLector != null){
	//							dcfCargaHorariaLector.setCrhrTipoFuncionCargaHoraria(dcfLectorSlc);
	//							dcfCargaHorariaLector.setCrhrObservacion(dcfLectorSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
	//							dcfCargaHorariaLector.setCrhrNumHoras(dcfHorasSemanalesLector);
	//							
	//							if(actualizarCargaHoraria(dcfCargaHorariaLector)){
	//								dcfDetalleCargaHorariaLector.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfLector).toUpperCase());
	//								dcfDetalleCargaHorariaLector.setDtcrhrNumHoras(dcfHorasSemanalesLector);
	//								
	//								verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaLector);
	//							}
	//							
	//						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
	//						}else{
	//							verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
	//									dcfHorasSemanalesLector, 
	//									dcfLectorSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
	//									dcfPeriodoAcademico, 
	//									dcfLectorSlc)
	//									, dcfLector , 0, dcfHorasSemanalesLector);
	//						}
	//						
	//					//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
	//					}else{
	//						if(dcfCargaHorariaLector != null){
	//							dcfCargaHorariaLector.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
	//							if(actualizarCargaHoraria(dcfCargaHorariaLector)){
	//								dcfDetalleCargaHorariaLector.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
	//								 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaLector); 
	//							}
	//						}
	//					}
	//				}
					
					if(dcfEditarOtrosAsignados){
						 
						//--Capacitacion docente
						if (dcfFuncionSeleccionCapacitacionDocente != null && dcfFuncionSeleccionCapacitacionDocente.size() != 0) {
							verificar = false;
							if(dcfCargaHorariaCapacitacionDocente != null){
								dcfCargaHorariaCapacitacionDocente.setCrhrTipoFuncionCargaHoraria(dcfCapacitacionDocenteSlc);
								dcfCargaHorariaCapacitacionDocente.setCrhrObservacion(dcfCapacitacionDocenteSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaCapacitacionDocente.setCrhrNumHoras(dcfHorasSemanalesCapacitacionDocente);
								
								if(actualizarCargaHoraria(dcfCargaHorariaCapacitacionDocente)){
									dcfDetalleCargaHorariaCapacitacionDocente.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfCapacitacionDocente).toUpperCase());
									dcfDetalleCargaHorariaCapacitacionDocente.setDtcrhrNumHoras(dcfHorasSemanalesCapacitacionDocente);
									
									verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCapacitacionDocente);
								}
								
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesCapacitacionDocente, 
										dcfCapacitacionDocenteSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCapacitacionDocenteSlc)
										, dcfCapacitacionDocente , 0, dcfHorasSemanalesCapacitacionDocente);
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaCapacitacionDocente != null){
								dcfCargaHorariaCapacitacionDocente.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dcfCargaHorariaCapacitacionDocente)){
									dcfDetalleCargaHorariaCapacitacionDocente.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCapacitacionDocente); 
								}
							}
						}
					
						
						//--Publicaciones Academicas
						if (dcfFuncionSeleccionPublicacionAcademica != null && dcfFuncionSeleccionPublicacionAcademica.size() != 0) {
							verificar = false;
							if(dcfCargaHorariaPublicacionAcademica != null){
								dcfCargaHorariaPublicacionAcademica.setCrhrTipoFuncionCargaHoraria(dcfPublicacionAcademicaSlc);
								dcfCargaHorariaPublicacionAcademica.setCrhrObservacion(dcfPublicacionAcademicaSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaPublicacionAcademica.setCrhrNumHoras(dcfHorasSemanalesPublicacionAcademica);
								
								if(actualizarCargaHoraria(dcfCargaHorariaPublicacionAcademica)){
									dcfDetalleCargaHorariaPublicacionAcademica.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(dcfPublicacionAcademica).toUpperCase());
									dcfDetalleCargaHorariaPublicacionAcademica.setDtcrhrNumHoras(dcfHorasSemanalesPublicacionAcademica);
									
									verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaPublicacionAcademica);
								}
								
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{
								verificar = cargarDetalleCargaHorariaCoordinador(cargarCargaHoraria(dcfDocente, 
										dcfHorasSemanalesPublicacionAcademica, 
										dcfPublicacionAcademicaSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfPublicacionAcademicaSlc)
										, dcfPublicacionAcademica , 0, dcfHorasSemanalesPublicacionAcademica);
							}
							
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaPublicacionAcademica != null){
								dcfCargaHorariaPublicacionAcademica.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								if(actualizarCargaHoraria(dcfCargaHorariaPublicacionAcademica)){
									dcfDetalleCargaHorariaPublicacionAcademica.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
									 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaPublicacionAcademica); 
								}
							}
						} 
					}
					 
					// Coordinador diseno/rediseno
					if(dcfEditarCoordinadorDiseno){
						if(dcfFuncionSeleccionDiseno != null && dcfFuncionSeleccionDiseno.size()!=0){
							
							verificar = false;
							if(dcfCargaHorariaCooDiseno != null){
								dcfCargaHorariaCooDiseno.setCrhrTipoFuncionCargaHoraria(dcfCoordinadorDisenoSlc);
								dcfCargaHorariaCooDiseno.setCrhrObservacion(dcfCoordinadorDisenoSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion());
								dcfCargaHorariaCooDiseno.setCrhrNumHoras(dcfHorasCoordinadorDisenoSlc);
								
								verificar = actualizarCargaHoraria(dcfCargaHorariaCooDiseno);
									
							//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el nuevo parametro 
							}else{ 
								CargaHoraria crhr = cargarCargaHoraria(dcfDocente, 
										dcfHorasCoordinadorDisenoSlc, 
										dcfCoordinadorDisenoSlc.getTifncrhrFuncionCargaHoraria().getFncrhrDescripcion(), 
										dcfPeriodoAcademico, 
										dcfCoordinadorDisenoSlc);
								
								if(crhr != null){
									verificar = true;
								}else{
									verificar = false;
								}
							}
						//Si al actualzar existe el parametro no ingresado anteriormente se ingresa el parametro 
						}else{
							if(dcfCargaHorariaCooDiseno != null){
								dcfCargaHorariaCooDiseno.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
								verificar =actualizarCargaHoraria(dcfCargaHorariaCooDiseno);
							}
						} 
					}
					
					//Verificacion de actualizacion
					if(verificar){
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.guardar.cambios.con.exito.validacion")));
						limpiar();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.guardar.cambios.exception")));
					} 
				 } 
			 }
		}

		/**
		 * Elimina el registro
		 **/
		public void eliminar() {
 
			Boolean verificar = true;
			
			if(validarCronograma()){ // Cambio Solicitado por Eco. Reinoso 12/12/2017
				 
				if(verificarDocente()){
					
					//Eliminar area
					if(dcfCargaHorariaCooArea != null){
						dcfCargaHorariaCooArea.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaCooArea)){
							dcfDetalleCargaHorariaCooArea.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooArea); 
						}
					}
					 
					//Eliminar Sub Area
					if(dcfCargaHorariaCooSubArea != null){
						dcfCargaHorariaCooSubArea.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaCooSubArea)){
							dcfDetalleCargaHorariaCooSubArea.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooSubArea); 
						}
					}
					
					//Eliminar Titulacion
					if(dcfCargaHorariaCooTtl != null){
						dcfCargaHorariaCooTtl.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaCooTtl)){
							dcfDetalleCargaHorariaCooTtl.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCooTtl); 
						}
					}
					
					//Eliminar Consejo
					if(dcfCargaHorariaConsejo != null){
						dcfCargaHorariaConsejo.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaConsejo)){
							dcfDetalleCargaHorariaConsejo.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaConsejo); 
						}
					}
					
					//Eliminar Clinicas
					if(dcfCargaHorariaClinicas != null){
						dcfCargaHorariaClinicas.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaClinicas)){
							dcfDetalleCargaHorariaClinicas.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							 verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaClinicas); 
						}
					}
					
					//Eliminar Examen
					if(dcfCargaHorariaExamen != null){
						dcfCargaHorariaExamen.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						verificar =actualizarCargaHoraria(dcfCargaHorariaExamen);
					}
					
					//--v2
					//Eliminar Elaboracion de reactivos
					if(dcfCargaHorariaElaboracionReac != null){
						dcfCargaHorariaElaboracionReac.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						verificar =actualizarCargaHoraria(dcfCargaHorariaElaboracionReac);
					}
					
					//Eliminar Lector de Proyecto
	//				if(dcfCargaHorariaLector != null){
	//					dcfCargaHorariaLector.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
	//					if(actualizarCargaHoraria(dcfCargaHorariaLector)){
	//						dcfDetalleCargaHorariaLector.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
	//						verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaLector); 
	//					}
	//				}
					
					//Eliminar Capacitacion Docente
					if(dcfCargaHorariaCapacitacionDocente != null){
						dcfCargaHorariaCapacitacionDocente.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaCapacitacionDocente)){
							dcfDetalleCargaHorariaCapacitacionDocente.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaCapacitacionDocente); 
						}
					}
					
					//Eliminar Publicacion Academica
					if(dcfCargaHorariaPublicacionAcademica != null){
						dcfCargaHorariaPublicacionAcademica.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						if(actualizarCargaHoraria(dcfCargaHorariaPublicacionAcademica)){
							dcfDetalleCargaHorariaPublicacionAcademica.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE);  
							verificar = actualizarDetalleCargaHoraria(dcfDetalleCargaHorariaPublicacionAcademica); 
						}
					}
					
					// Eliminar Coordinador diseno/rediseno
					if(dcfCargaHorariaCooDiseno != null){
						dcfCargaHorariaCooDiseno.setCrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_SI_VALUE); 
						verificar =actualizarCargaHoraria(dcfCargaHorariaCooDiseno);
					}
					
					if(verificar){
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.eliminar.con.exito.validacion")));
						limpiar();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.eliminar.exception")));
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.eliminar.docente.validacion.exception")));
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
				
				crhrResult = servDcfCargaHorariaServicio.anadir(crhr);
				 
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.carga.horaria.con.exito.validacion", observacion)));
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.carga.horaria.validacion.exception")));
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.carga.horaria.exception")));
			}
			
			return crhrResult;
		}
		
		/**
		 * Carga el detalle de la carga horaia para Miembros de consejo en la tabla DETALLE_CARGA_HORARIA
		 * @param cargaHoraria Entidad a insertar en el detalle
		 * @param unidadAcademica Unidad Academica a agregar
		 * @param horas Numero de horas por semana
		 **/
		public Boolean cargarDetalleCargaHorariaMmbCons(CargaHoraria cargaHoraria, String funcion, Integer horas){
			
			Boolean verificar = false;
			try {
				
				DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
				
				dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
				dtcrhr.setDtcrhrFuncion(GeneralesUtilidades.eliminarEspaciosEnBlanco(funcion).toUpperCase());
				dtcrhr.setDtcrhrNumHoras(horas);
				dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
				
				servDcfDetalleCargaHorariaServicio.anadir(dtcrhr);
				verificar = true;
			} catch (DetalleCargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.detalle.carga.horaria.mmb.cons.validacion.exception")));
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.detalle.carga.horaria.mmb.cons.exception")));
			}
			return verificar;
		}
		
		/**
		 * Carga el detalle de la carga horaia para clinicas y practicas en la tabla DETALLE_CARGA_HORARIA
		 * @param cargaHoraria Entidad a insertar en el detalle
		 * @param unidadAcademica Unidad Academica a agregar
		 * @param horas Numero de horas por semana
		 **/
		public Boolean cargarDetalleCargaHorariaCliPra(CargaHoraria cargaHoraria, String unidadAcademica, String asignatura, Integer numAlumnos, Integer horas){
			 Boolean verificar = false;
			try {
				
				DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
				
				dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
				dtcrhr.setDtcrhrUnidadAcademica(GeneralesUtilidades.eliminarEspaciosEnBlanco(unidadAcademica).toUpperCase());
				dtcrhr.setDtcrhrAsignaturaClinicas(GeneralesUtilidades.eliminarEspaciosEnBlanco(asignatura).toUpperCase());
				dtcrhr.setDtcrhrNumAlumnosClinicas(numAlumnos);
				dtcrhr.setDtcrhrNumHoras(horas);
				dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
				
				servDcfDetalleCargaHorariaServicio.anadir(dtcrhr);
				
				 verificar = true;
				 
			} catch (DetalleCargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.detalle.carga.horaria.cli.pra.validacion.exception")));
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.detalle.carga.horaria.cli.pra.exception")));
			}
			
			return verificar;
		}
		
		/**
		 * Carga el detalle de la carga horaia para coordinadores en la tabla DETALLE_CARGA_HORARIA
		 * @param cargaHoraria Entidad a insertar en el detalle
		 * @param unidadAcademica Unidad Academica a agregar
		 * @param horas Numero de horas por semana
		 **/
		public Boolean cargarDetalleCargaHorariaCoordinador(CargaHoraria cargaHoraria, String areaSubarea, Integer numAlumnos, Integer horas){
			 Boolean verificar = false;
			try {
				
				DetalleCargaHoraria dtcrhr = new DetalleCargaHoraria();
				
				dtcrhr.setDtcrhrCargaHoraria(cargaHoraria);
				dtcrhr.setDtcrhrArea(GeneralesUtilidades.eliminarEspaciosEnBlanco(areaSubarea).toUpperCase());
				dtcrhr.setDtcrhrEstado(TipoCargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
				dtcrhr.setDtcrhrEstadoEliminacion(TipoCargaHorariaConstantes.ESTADO_ELIMINACION_ELIMINADO_NO_VALUE);
				if(numAlumnos != 0){
					dtcrhr.setDtcrhrNumAlumnosTitulacion(numAlumnos);
				}
				dtcrhr.setDtcrhrNumHoras(horas);
				
				servDcfDetalleCargaHorariaServicio.anadir(dtcrhr);
				verificar = true;
				
			} catch (DetalleCargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.detalle.carga.horaria.coordinador.validacion.exception")));
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.cargar.detalle.carga.horaria.coordinador.exception")));
			}
			return verificar;
		}
		
		/**
		 * Carga los campos de carga horaria del dcente
		 */
		public void listarCargaHorariaDocente(){ 
			try { 
				
				//Coordinador de Area 
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_AREA_VALUE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaCooArea = item;
						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(dcfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
								dcfDetalleCargaHorariaCooArea = itemDetalle;
								dcfFuncionCoordinadorAraSlc = new ArrayList<>();
								dcfFuncionCoordinadorAraSlc.add(101);
								dcfHorasCoordinadorAraSlc = itemDetalle.getDtcrhrNumHoras();
								dcfCoordinadorAra = itemDetalle.getDtcrhrArea(); 
								dcfCargaHorariaActiva = true;
								calcularHorasCoordinador();
							}
						}
					} 
				}
				
				//Coordinador de SubArea 
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_SUBAREA_VALUE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaCooSubArea = item;
						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(dcfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
								dcfDetalleCargaHorariaCooSubArea = itemDetalle;
								dcfFuncionCoordinadorSubAraSlc = new ArrayList<>();
								dcfFuncionCoordinadorSubAraSlc.add(101);
								dcfHorasCoordinadorSubAraSlc = itemDetalle.getDtcrhrNumHoras();
								dcfCoordinadorSubAra = itemDetalle.getDtcrhrArea(); 
								dcfCargaHorariaActiva = true;
								calcularHorasCoordinador();
							}
						}
					}
				}
					
				//Coordinador de Titulacion 
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_TITULACION_VALUE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaCooTtl = item;
						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(dcfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
								dcfDetalleCargaHorariaCooTtl = itemDetalle;
								dcfFuncionCoordinadorTtlSlc = new ArrayList<>();
								dcfFuncionCoordinadorTtlSlc.add(101);
								dcfHorasCoordinadorTtlSlc = itemDetalle.getDtcrhrNumHoras();
								dcfNumEstudiantesTitulacion = itemDetalle.getDtcrhrNumAlumnosTitulacion();
								dcfCoordinadorTtl = itemDetalle.getDtcrhrArea();
								dcfCargaHorariaActiva = true;
								calcularHorasCoordinador();
							}
						}
					}
				}
			  
				//Examen complexivo
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_EXAMEN_COMPLEXIVO_REACTIVOS_VALUE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaExamen = item;
						dcfFuncionSeleccionExamen = new ArrayList<>();
						dcfFuncionSeleccionExamen.add(101);
						dcfHorasSemanalesExamenCom = item.getCrhrNumHoras(); 
						dcfCargaHorariaActiva = true;
						calcularHorasExamen();
					}
				}
				 
				//Clinicas y Practicas
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_SUPERVISOR_CLINICAS_PRACTICAS_VALUE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaClinicas = item;
						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(dcfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
								dcfDetalleCargaHorariaClinicas = itemDetalle;
								dcfUnidadAcademica = itemDetalle.getDtcrhrUnidadAcademica();
								dcfAsignatura = itemDetalle.getDtcrhrAsignaturaClinicas();
								dcfNumEstudiantes = itemDetalle.getDtcrhrNumAlumnosClinicas();
								dcfHorasSemanalesCliPra = itemDetalle.getDtcrhrNumHoras();
								dcfCargaHorariaActiva = true;
								calcularHorasClinicas();
							}
						} 
					}
				}
				
				//--v2
				//Elaboracion de Reactivos
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_ELABORACION_REACTIVOS, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaElaboracionReac = item;
						dcfFuncionSeleccionElaboracionReac = new ArrayList<>();
						dcfFuncionSeleccionElaboracionReac.add(101);
						dcfHorasSemanalesElaboracionReac = item.getCrhrNumHoras(); 
						dcfCargaHorariaActiva = true;
						calcularHorasExamen();
					}
				}
				  
				//Lector de proyecto 
//				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_LECTOR_PROYECTO, dcfPeriodoAcademico.getPracId());
//				if(dcfListaCargasHorarias.size() > 0 ){
//					for(CargaHoraria item: dcfListaCargasHorarias ){ 
//						dcfCargaHorariaLector = item;
//						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
//						if(dcfListaDetalleCargaHoraria.size() > 0){
//							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
//								dcfDetalleCargaHorariaLector = itemDetalle;
//								dcfFuncionSeleccionLector = new ArrayList<>();
//								dcfFuncionSeleccionLector.add(101);
//								dcfHorasSemanalesLector = itemDetalle.getDtcrhrNumHoras();
//								dcfLector = itemDetalle.getDtcrhrArea(); 
//								dcfCargaHorariaActiva = true;
//								calcularHorasOtrosAsignados();
//							}
//						}
//					} 
//				}
				
				//Capacitacion docente
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_CAPACITACION_DOCENTE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaCapacitacionDocente = item;
						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(dcfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
								dcfDetalleCargaHorariaCapacitacionDocente = itemDetalle;
								dcfFuncionSeleccionCapacitacionDocente = new ArrayList<>();
								dcfFuncionSeleccionCapacitacionDocente.add(101);
								dcfHorasSemanalesCapacitacionDocente = itemDetalle.getDtcrhrNumHoras();
								dcfCapacitacionDocente = itemDetalle.getDtcrhrArea(); 
								dcfCargaHorariaActiva = true;
								calcularHorasOtrosAsignados();
							}
						}
					} 
				}
				
				//Publicacion academica
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_PUBLICACIONES_ACADEMICAS, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaPublicacionAcademica = item;
						dcfListaDetalleCargaHoraria = servDcfDetalleCargaHorariaServicio.listaPorCargaHoraria(item.getCrhrId()); 
						if(dcfListaDetalleCargaHoraria.size() > 0){
							for(DetalleCargaHoraria itemDetalle: dcfListaDetalleCargaHoraria){
								dcfDetalleCargaHorariaPublicacionAcademica = itemDetalle;
								dcfFuncionSeleccionPublicacionAcademica = new ArrayList<>();
								dcfFuncionSeleccionPublicacionAcademica.add(101);
								dcfHorasSemanalesPublicacionAcademica = itemDetalle.getDtcrhrNumHoras();
								dcfPublicacionAcademica = itemDetalle.getDtcrhrArea(); 
								dcfCargaHorariaActiva = true;
								calcularHorasOtrosAsignados();
							}
						}
					} 
				}
				
				//Coordinador diseno/rediseno
				dcfListaCargasHorarias =  servDcfCargaHorariaServicio.buscarPorDetallePuesto(dcfDocente.getDtpsId(),TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_DISENO_REDISENO_VALUE, dcfPeriodoAcademico.getPracId());
				if(dcfListaCargasHorarias.size() > 0 ){
					for(CargaHoraria item: dcfListaCargasHorarias ){ 
						dcfCargaHorariaCooDiseno = item;
						dcfFuncionSeleccionDiseno = new ArrayList<>();
						dcfFuncionSeleccionDiseno.add(101);
						dcfHorasCoordinadorDisenoSlc = item.getCrhrNumHoras(); 
						dcfCargaHorariaActiva = true;
						calcularHorasCoordinador();
					}
				}
				
				//Calculo de horas
				dcfHorasSemanalesCoordinador = convertirHoras(dcfHorasCoordinadorAraSlc)+convertirHoras(dcfHorasCoordinadorSubAraSlc)+convertirHoras(dcfHorasCoordinadorTtlSlc)+convertirHoras(dcfHorasCoordinadorDisenoSlc);
				
				if(dcfHorasSemanalesCoordinador == 0){
					dcfHorasSemanalesCoordinador = null;
				}
									
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.listar.carga.horaria.docente.carga.horaria.no.encontrado.exception")));
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.listar.carga.horaria.docente.carga.horaria.exception")));
			} catch (DetalleCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.listar.carga.horaria.docente.detalle.carga.horaria.no.encontrado.exception")));
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.listar.carga.horaria.docente.detalle.carga.horaria.exception")));
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
					verificar = servDcfCargaHorariaServicio.editar(entidad); 
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.actualizar.carga.horaria.validacion.exception")));
			} catch (CargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.actualizar.carga.horaria.exception")));
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
				verificar = servDcfDetalleCargaHorariaServicio.editar(entidad);
			} catch (DetalleCargaHorariaValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.actualizar.detalle.carga.horaria.validacion.exception")));
			} catch (DetalleCargaHorariaException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.actualizar.detalle.carga.horaria.exception")));
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
			dcfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
			dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			dcfListaDocentes = null;
			dcfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
			dcfListaCarreras = null; 
		}

		/**
		 * Lista de Entidades Carrera por facultad id
		 **/
		public void listarCarreras() {

			try {	 
				dcfListaDocentes = null;
				dcfListaCarreras = null; 
				limpiarInfoDocente();
				if(dcfFacultadBuscar != GeneralesConstantes.APP_ID_BASE){
					dcfListaCarreras = servDcfCarreraServicio.listarCarrerasXFacultad(dcfFacultadBuscar);
					dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
					dcfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
					dcfListaDocentes = null;
					listarDocentesPorFacultad();
				}else{
					dcfFacultadBuscar = GeneralesConstantes.APP_ID_BASE;
					dcfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
					dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				}
			} catch (CarreraNoEncontradoException e) { 
				dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
				dcfListaDocentes = null;
				dcfCarreraBuscar = GeneralesConstantes.APP_ID_BASE;
				dcfListaCarreras = null;
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.listar.carreras.no.encontrado.exception")));
			} catch (CarreraException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.listar.carreras.exception"))); 
			}
		}

		/**
		 * Lista de Entidades Docente por carrera
		 **/
		public void listarDocentesPorFacultad() {
			
			try {
				limpiarInfoDocente();
				dcfListaDocentes = servDcfPersonaDatosServicioJdbc.buscarDocentesPorFacultadPeriodo(dcfFacultadBuscar, dcfPeriodoAcademicoBuscar);
				dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
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
				dcfListaDocentes = servDcfPersonaDatosServicioJdbc.buscarDocentesPorCarreraPeriodo(dcfCarreraBuscar, dcfPeriodoAcademicoBuscar);
				dcfDocenteBuscar = GeneralesConstantes.APP_ID_BASE;
			} catch (PersonaDatosDtoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.no.encontrado.exception")));
			} catch (PersonaDatosDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("GestionAcademica.listar.docentes.exception")));
			}
		}
		
		  
		/**
		 * Lista de Entidades Docente por carrera
		 **/
		public void listarFuncionesConsejo() { 
			listarCargaHorariaDocente(); 
		}
		
		
		
	 
		// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
		
		/**
		 * Calcula las horas para coordinadores en base a las reglas y valores en base de datos
		 */
		public void calcularHorasCoordinador(){
			 
			dcfHorasCoordinadorAraSlc = null;
			dcfHorasCoordinadorSubAraSlc = null;
			dcfHorasCoordinadorTtlSlc = null;
			dcfHorasCoordinadorDisenoSlc = null;
			
			try { 
				
				//Coordinador de Area
				if(dcfFuncionCoordinadorAraSlc != null && dcfFuncionCoordinadorAraSlc.size() != 0 ){ 
					dcfCoordinadorAraSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_AREA_VALUE);
					dcfHorasCoordinadorAraSlc = dcfCoordinadorAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
				}
				
				//Coordinador de SubArea
				if(dcfFuncionCoordinadorSubAraSlc != null && dcfFuncionCoordinadorSubAraSlc.size()!=0){ 
					dcfCoordinadorSubAraSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_SUBAREA_VALUE);
					dcfHorasCoordinadorSubAraSlc = dcfCoordinadorSubAraSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
				}
				
				//Coordinador de Titulacion
				if(dcfFuncionCoordinadorTtlSlc != null && dcfFuncionCoordinadorTtlSlc.size()!=0){ 
					dcfCoordinadorTtlSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_TITULACION_VALUE);
					if(dcfNumEstudiantesTitulacion == null){
						
						FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.coordinador.titulacion.numero.estudiantes.validacion")));
						dcfHorasCoordinadorTtlSlc = null;
					} else if(dcfNumEstudiantesTitulacion != null && dcfNumEstudiantesTitulacion < 50){
						dcfHorasCoordinadorTtlSlc = dcfCoordinadorTtlSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo();
					}else{
						dcfHorasCoordinadorTtlSlc = dcfCoordinadorTtlSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo();
					}	
				}else{
					dcfNumEstudiantesTitulacion = null;
				}
				
				//--v2
				//Coordinador diseno/rediseno
				if(dcfFuncionSeleccionDiseno != null && dcfFuncionSeleccionDiseno.size()!=0){ 
					dcfCoordinadorDisenoSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_COORDINADOR_DISENO_REDISENO_VALUE);
					dcfHorasCoordinadorDisenoSlc = dcfCoordinadorDisenoSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
				}
				
				//Calculo de horas
				dcfHorasSemanalesCoordinador = convertirHoras(dcfHorasCoordinadorAraSlc)+convertirHoras(dcfHorasCoordinadorSubAraSlc)+convertirHoras(dcfHorasCoordinadorTtlSlc)+convertirHoras(dcfHorasCoordinadorDisenoSlc);
				
				if(dcfHorasSemanalesCoordinador == 0){
					dcfHorasSemanalesCoordinador = null;
				}
					
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.coordinador.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.coordinador.exception")));
			}
			 
		}
		
		/**
		 * Calcula las horas en miembros de consejo en base a las reglas y valores en base de datos
		 */
		public void calcularHorasOtrosAsignados(){
			 
			try {
				
				dcfHorasSemanalesCapacitacionDocente = 0;
				dcfHorasSemanalesPublicacionAcademica = 0;
				
//				dcfHorasSemanalesLector = 0;
//				if (dcfFuncionSeleccionLector != null && dcfFuncionSeleccionLector.size() > 0) {
//					dcfLectorSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_LECTOR_PROYECTO);
//					dcfHorasSemanalesLector = dcfLectorSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas(); 
//				}
				
				if (dcfFuncionSeleccionCapacitacionDocente != null && dcfFuncionSeleccionCapacitacionDocente.size() > 0) {
					dcfCapacitacionDocenteSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_CAPACITACION_DOCENTE);
					dcfHorasSemanalesCapacitacionDocente = dcfCapacitacionDocenteSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas(); 
				}
				
				if (dcfFuncionSeleccionPublicacionAcademica != null && dcfFuncionSeleccionPublicacionAcademica.size() > 0) {
					dcfPublicacionAcademicaSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_PUBLICACIONES_ACADEMICAS);
					dcfHorasSemanalesPublicacionAcademica = dcfPublicacionAcademicaSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
				}
				
//				dcfHorasSemanalesOtrasAsig = dcfHorasSemanalesLector+dcfHorasSemanalesCapacitacionDocente+dcfHorasSemanalesPublicacionAcademica;
				dcfHorasSemanalesOtrasAsig = dcfHorasSemanalesCapacitacionDocente+dcfHorasSemanalesPublicacionAcademica;
				
				if(dcfHorasSemanalesOtrasAsig == 0){
//					dcfHorasSemanalesLector = null;
					dcfHorasSemanalesOtrasAsig = null;
					dcfHorasSemanalesCapacitacionDocente = null;
					dcfHorasSemanalesPublicacionAcademica = null;
				}
				
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.consejo.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.consejo.exception")));
			}
		}

		/**
		 * Calcula las horas en examen complexivo/ reactivos en base a las reglas y valores en base de datos
		 */
		public void calcularHorasExamen(){
			try {
				if ((dcfFuncionSeleccionExamen != null && dcfFuncionSeleccionExamen.size()!=0) || (dcfFuncionSeleccionElaboracionReac != null && dcfFuncionSeleccionElaboracionReac.size()!=0)) {
					
					dcfHorasSemanalesExamenCom = 0;
					dcfHorasSemanalesElaboracionReac = 0;
					
					if(dcfFuncionSeleccionExamen != null &&  dcfFuncionSeleccionExamen.size()!=0){
					dcfExamenSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_EXAMEN_COMPLEXIVO_REACTIVOS_VALUE);
					dcfHorasSemanalesExamenCom = dcfExamenSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
					}
					
					if(dcfFuncionSeleccionElaboracionReac != null && dcfFuncionSeleccionElaboracionReac.size()!=0){
						dcfElaboracionReactivosSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_ELABORACION_REACTIVOS);
						dcfHorasSemanalesElaboracionReac = dcfElaboracionReactivosSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasAsignadas();
					}
					
					if(dcfHorasSemanalesExamenCom != 0 || dcfHorasSemanalesElaboracionReac != 0){
						dcfHorasSemanalesExaReac = dcfHorasSemanalesElaboracionReac + dcfHorasSemanalesExamenCom;
					}
					
				}else{
					dcfHorasSemanalesExaReac = null;
				}
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.examen.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.examen.exception")));
			}
		}
		
		/**
		 * Calcula las horas en examen complexivo/ reactivos en base a las reglas y valores en base de datos
		 */
		public void calcularHorasClinicas(){
			try {
					dcfClinicasSlc = servDcfTipoFuncionCargaHorariaServicio.buscarPorId(TipoFuncionCargaHorariaConstantes.FUNCION_SUPERVISOR_CLINICAS_PRACTICAS_VALUE);
					dcfListaHorasClinicas = new ArrayList<>();
					for(int i = dcfClinicasSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMinimo() ; i <= dcfClinicasSlc.getTifncrhrFuncionCargaHoraria().getFncrhrHorasMaximo(); i++){
						dcfListaHorasClinicas.add(i); 
					}  
					
			} catch (TipoFuncionCargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.clinicas.no.encontrado.exception")));
			} catch (TipoFuncionCargaHorariaException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.calcular.horas.clinicas.exception")));
			}
		}
		
		/**
		 * Convierte los nulos en ceros
		 * @param horas numero de horas a convertir
		 * @return numero de horas
		 */
		public Integer convertirHoras(Integer horas){
			if(horas == null){
				horas = 0;
			}
			return horas;
		}
		
		/**
		 * Verifica la seleccion del docente
		 * @return si se ha seleccionado el docente o no 
		 */
		public Boolean verificarDocente(){
			Boolean verificar = false;
			if(dcfDocente!=null){
				verificar = true;
			}
			return verificar;
		}
		
		/**
		 * Verifica el acceso del docente a los diferentes tipos de carga horaria
		 * otorgando un estado true / false para activar las casillas
		 */
		public void verificarAcceso(){
			if(verificarDocente()){
				
				dcfEditarCoordinador = false;
				dcfEditarCoordinadorDiseno = false;
				dcfEditarLector = false;
				dcfEditarOtrosAsignados = false;
				
				switch (dcfDocente.getTmddId()) {
				case TiempoDedicacionConstantes.TMDD_TIEMPO_COMPLETO_VALUE://Tiempo Completo
					if(dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_CIENCIAS_MEDICAS_VALUE || dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_MEDICINA_VETERINARIA_ZOOTECNIA_VALUE || dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_ODONTOLOGIA_VALUE){
						dcfEditarClinicas = true; 
					}
					  
					dcfEditarOtrosAsignados = true;
					dcfEditarExamen = true;
					dcfEditarReactvos = true;
					dcfEditarCoordinador = true;
					dcfEditarCoordinadorDiseno = true;
					dcfEditarLector = true;
					break;
				case TiempoDedicacionConstantes.TMDD_MEDIO_TIEMPO_VALUE://Medio Tiempo
					if(dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_CIENCIAS_MEDICAS_VALUE || dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_MEDICINA_VETERINARIA_ZOOTECNIA_VALUE || dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_ODONTOLOGIA_VALUE){
						dcfEditarClinicas = true;
					}
					
					dcfEditarCoordinador = true;
					dcfEditarOtrosAsignados = true;
					dcfEditarExamen = true;
					break;
				case TiempoDedicacionConstantes.TMDD_TIEMPO_PARCIAL_VALUE://Tiempo Parcial
					if(dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_CIENCIAS_MEDICAS_VALUE || dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_MEDICINA_VETERINARIA_ZOOTECNIA_VALUE || dcfFacultadBuscar==TipoCargaHorariaConstantes.DEPENDENCIA_ODONTOLOGIA_VALUE){
						dcfEditarClinicas = true;
					}
					
					dcfEditarExamen = true;
					break;
				default:
					dcfEditarCoordinador = false;
					dcfEditarCoordinadorDiseno = false;
					dcfEditarLector = false;
					dcfEditarOtrosAsignados = false;
					dcfEditarClinicas = false;
					dcfEditarExamen = false; 
					dcfEditarReactvos = false;
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.acceso.validacion")));
					break;
				}
				
				if(dcfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE
						|| dcfDocente.getRllbId() == RelacionLaboralConstantes.RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE){
					
					dcfEditarCoordinador = false;
					dcfEditarCoordinadorDiseno = false; 
				}
			}
			
			if(dcfEditarClinicas){
				calcularHorasClinicas();
			}
		}
		
		/**
		 * Verifica campos, parametos antes de guardar las distintas cargas horarias
		 * @return Estado false por culquier error al ingresar, true - pasa las validaciones 
		 **/
		public Boolean verificarGuardar() {
			
			
			Boolean verificar = true;
			
			if(verificarDocente()){
				
				if(dcfEditarCoordinador){
					//Coordinador de Area
					if (dcfFuncionCoordinadorAraSlc != null && dcfFuncionCoordinadorAraSlc.size() != 0) {
						if (dcfCoordinadorAra != null && dcfCoordinadorAra.replaceAll(" ", "").length() != 0) {
							if(dcfHorasSemanalesCoordinador == null){
								verificar = false;
							}
						} else {
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.coordinador.area.validacion")));
						}
					}
	
					// Coordinador de SubArea
					if (dcfFuncionCoordinadorSubAraSlc != null && dcfFuncionCoordinadorSubAraSlc.size() != 0) {
						if (dcfCoordinadorSubAra != null && dcfCoordinadorSubAra.replaceAll(" ", "").length() != 0) {
							if(dcfHorasSemanalesCoordinador == null){
								verificar = false;
							}
						} else {
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.coordinador.subarea.validacion")));
						}
					}
	
					// Coordinador de Titulacion
					if (dcfFuncionCoordinadorTtlSlc != null && dcfFuncionCoordinadorTtlSlc.size() != 0) {
						if (dcfCoordinadorTtl != null && dcfCoordinadorTtl.replaceAll(" ", "").length() != 0) { 
								if (dcfNumEstudiantesTitulacion != null) {
									if(dcfHorasSemanalesCoordinador == null){
										verificar = false; 
									}
								} else {
									verificar = false;
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.coordinador.titulacion.numero.estudiantes.validacion")));
								} 
						} else {
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.coordinador.titulacion.validacion")));
						}
					}
				}
				
				//Clinicas y Practicas 
				if(dcfEditarClinicas){
					if(dcfUnidadAcademica != null && dcfUnidadAcademica.replaceAll(" ", "").length()!=0){
						if(dcfAsignatura != null && dcfAsignatura.replaceAll(" ", "").length()!=0){ 
								if(dcfNumEstudiantes != null){
									if(dcfHorasSemanalesCliPra != null){
										if( dcfHorasSemanalesCliPra == null){
											verificar = false;
										}
									}else{
										verificar = false;
										FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.clinicas.practicas.numero.horas.validacion")));
									}
								}else{
									verificar = false;
									FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.clinicas.practicas.numero.estudiantes.validacion")));
								} 
						}else{
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.clinicas.practicas.asigantura.validacion"))); 
						}
					}
				} 
 
				//Examen Complexivo y Reativos
				if(dcfEditarExamen){
					if(dcfFuncionSeleccionExamen != null && dcfFuncionSeleccionExamen.size()!=0){
						if( dcfHorasSemanalesExaReac == null){
							verificar = false;
						}
					}
				}
				
				//--v2
				//Lector de Proyecto
//				if(dcfEditarLector){
//					if (dcfFuncionSeleccionLector != null && dcfFuncionSeleccionLector.size() != 0) {
//						if (dcfLector != null && dcfLector.replaceAll(" ", "").length() != 0) {
//							if(dcfHorasSemanalesLector == null){
//								verificar = false;
//							}
//						} else {
//							verificar = false;
//							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.lector.proyecto.validacion")));
//						}
//					}
//				}
				
				if(dcfEditarOtrosAsignados){	
					//Capacitacion Docente
					if (dcfFuncionSeleccionCapacitacionDocente != null && dcfFuncionSeleccionCapacitacionDocente.size() != 0) {
						if (dcfCapacitacionDocente != null && dcfCapacitacionDocente.replaceAll(" ", "").length() != 0) {
							if(dcfHorasSemanalesCapacitacionDocente == null){
								verificar = false;
							}
						} else {
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.capacitacion.docente.validacion")));
						}
					}
					
					//Publicacion Academica
					if (dcfFuncionSeleccionPublicacionAcademica != null && dcfFuncionSeleccionPublicacionAcademica.size() != 0) {
						if (dcfPublicacionAcademica != null && dcfPublicacionAcademica.replaceAll(" ", "").length() != 0) {
							if(dcfHorasSemanalesPublicacionAcademica == null){
								verificar = false;
							}
						} else {
							verificar = false;
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.guardar.publicacion.academica.validacion")));
						}
					}
				}
				
			}else{
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
				return servDcfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			} catch (PeriodoAcademicoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.periodo.activo.validacion.exception")));
				return null;
			} catch (PeriodoAcademicoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.periodo.activo.exception")));
				return null;
			}
		}
		
		/**
		 * Busca si existe un periodo de cierre
		 * @return retorna la entidad PeriodoAcademico de cierre en pregrado
		 **/
		public PeriodoAcademico verificarPeriodoCierre(){
			
			try {
				return servDcfPeriodoAcademicoServicio.buscarXestado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
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
				return servDcfPlanificacionCronogramaDtoServicioJdbc.buscarCronogramaXProcesoFlujo(TipoCargaHorariaConstantes.PROCESO_CARGA_HORARIA_VALUE);
			} catch (PlanificacionCronogramaDtoNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.cronograma.no.encontrado.exception")));
				return null;
			} catch (PlanificacionCronogramaDtoException e) { 
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.verificar.cronograma.exception")));
				return null;
			}
		}
		
		/**
		 * Valida el cronograa para uso de la carga horaria
		 **/
		public Boolean validarCronograma(){
			
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			Boolean verificar = false;
			dcfPlanificacionCronograma = verificarCronograma();
			if(dcfPlanificacionCronograma != null){
				if(dcfPlanificacionCronograma.getPlcrFechaFin().after(fechaActual)){
					if(dcfPlanificacionCronograma.getPlcrFechaInicio().before(fechaActual)){ 
						verificar = true;
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.validar.cronograma.no.iniciado.validacion.exception")));
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DirectorCarrera.validar.cronograma.expirado.validacion.exception")));
				}
			}
			return verificar;
		}
		
		/**
		 * Carga los periodos en la lista para mostrar.
		 */
		public void cagarPeriodos(){
			
			dcfListaPeriodosAcademicos = new ArrayList<>();
			
			PeriodoAcademico pracActivo = verificarPeriodoActivo();
			
			if(pracActivo != null){
				dcfListaPeriodosAcademicos.add(pracActivo);
				dcfPeriodoAcademico = pracActivo;
				dcfPeriodoAcademicoBuscar = pracActivo.getPracId();
			}
			
			PeriodoAcademico pracCierre = verificarPeriodoCierre();
			if(pracCierre != null){
				dcfListaPeriodosAcademicos.add(pracCierre);
			}
		}
		
		public void generarReporteCargaHorariaDocente(){
			if (dcfDocente != null) {
				cargarAsignacionesCargaHoraria(dcfDocente.getPrsIdentificacion(), new PeriodoAcademicoDto(dcfPeriodoAcademico.getPracId(),dcfPeriodoAcademico.getPracDescripcion()), dcfUsuario, dcfUsuario.getUsrIdentificacion());
				generarReporteIndividualCargaHoraria();
				rchfActivarReporte = 1;
			}
		}
		
		// ****************************************************************/
		// *********************** GETTERS Y SETTERS **********************/
		// ****************************************************************/

		public Usuario getDcfUsuario() {
			return dcfUsuario;
		}

		public void setDcfUsuario(Usuario dcfUsuario) {
			this.dcfUsuario = dcfUsuario;
		}

		public List<Dependencia> getDcfListaFacultades() {
			return dcfListaFacultades;
		}

		public void setDcfListaFacultades(List<Dependencia> dcfListaFacultades) {
			this.dcfListaFacultades = dcfListaFacultades;
		}

		public List<Carrera> getDcfListaCarreras() {
			return dcfListaCarreras;
		}

		public void setDcfListaCarreras(List<Carrera> dcfListaCarreras) {
			this.dcfListaCarreras = dcfListaCarreras;
		}

		public List<PersonaDatosDto> getDcfListaDocentes() {
			return dcfListaDocentes;
		}

		public void setDcfListaDocentes(List<PersonaDatosDto> dcfListaDocentes) {
			this.dcfListaDocentes = dcfListaDocentes;
		}

		public List<TipoFuncionCargaHoraria> getDcfListaFuncionesConsejoRed() {
			return dcfListaFuncionesConsejoRed;
		}

		public void setDcfListaFuncionesConsejoRed(List<TipoFuncionCargaHoraria> dcfListaFuncionesConsejoRed) {
			this.dcfListaFuncionesConsejoRed = dcfListaFuncionesConsejoRed;
		}

		public PersonaDatosDto getDcfDocente() {
			return dcfDocente;
		}

		public void setDcfDocente(PersonaDatosDto dcfDocente) {
			this.dcfDocente = dcfDocente;
		}

		public Integer getDcfFacultadBuscar() {
			return dcfFacultadBuscar;
		}

		public void setDcfFacultadBuscar(Integer dcfFacultadBuscar) {
			this.dcfFacultadBuscar = dcfFacultadBuscar;
		}

		public Integer getDcfCarreraBuscar() {
			return dcfCarreraBuscar;
		}

		public void setDcfCarreraBuscar(Integer dcfCarreraBuscar) {
			this.dcfCarreraBuscar = dcfCarreraBuscar;
		}

		public Integer getDcfDocenteBuscar() {
			return dcfDocenteBuscar;
		}

		public void setDcfDocenteBuscar(Integer dcfDocenteBuscar) {
			this.dcfDocenteBuscar = dcfDocenteBuscar;
		}

		public String getDcfIdentificacionBuscar() {
			return dcfIdentificacionBuscar;
		}

		public void setDcfIdentificacionBuscar(String dcfIdentificacionBuscar) {
			this.dcfIdentificacionBuscar = dcfIdentificacionBuscar;
		}

		public List<Integer> getDcfFuncionCoordinadorAraSlc() {
			return dcfFuncionCoordinadorAraSlc;
		}

		public void setDcfFuncionCoordinadorAraSlc(List<Integer> dcfFuncionCoordinadorAraSlc) {
			this.dcfFuncionCoordinadorAraSlc = dcfFuncionCoordinadorAraSlc;
		}

		public List<Integer> getDcfFuncionCoordinadorSubAraSlc() {
			return dcfFuncionCoordinadorSubAraSlc;
		}

		public void setDcfFuncionCoordinadorSubAraSlc(List<Integer> dcfFuncionCoordinadorSubAraSlc) {
			this.dcfFuncionCoordinadorSubAraSlc = dcfFuncionCoordinadorSubAraSlc;
		}

		public List<Integer> getDcfFuncionCoordinadorTtlSlc() {
			return dcfFuncionCoordinadorTtlSlc;
		}

		public void setDcfFuncionCoordinadorTtlSlc(List<Integer> dcfFuncionCoordinadorTtlSlc) {
			this.dcfFuncionCoordinadorTtlSlc = dcfFuncionCoordinadorTtlSlc;
		}

		public TipoFuncionCargaHoraria getDcfCoordinadorAraSlc() {
			return dcfCoordinadorAraSlc;
		}

		public void setDcfCoordinadorAraSlc(TipoFuncionCargaHoraria dcfCoordinadorAraSlc) {
			this.dcfCoordinadorAraSlc = dcfCoordinadorAraSlc;
		}

		public TipoFuncionCargaHoraria getDcfCoordinadorSubAraSlc() {
			return dcfCoordinadorSubAraSlc;
		}

		public void setDcfCoordinadorSubAraSlc(TipoFuncionCargaHoraria dcfCoordinadorSubAraSlc) {
			this.dcfCoordinadorSubAraSlc = dcfCoordinadorSubAraSlc;
		}

		public TipoFuncionCargaHoraria getDcfCoordinadorTtlSlc() {
			return dcfCoordinadorTtlSlc;
		}

		public void setDcfCoordinadorTtlSlc(TipoFuncionCargaHoraria dcfCoordinadorTtlSlc) {
			this.dcfCoordinadorTtlSlc = dcfCoordinadorTtlSlc;
		}

		public String getDcfCoordinadorAra() {
			return dcfCoordinadorAra;
		}

		public void setDcfCoordinadorAra(String dcfCoordinadorAra) {
			this.dcfCoordinadorAra = dcfCoordinadorAra;
		}

		public String getDcfCoordinadorSubAra() {
			return dcfCoordinadorSubAra;
		}

		public void setDcfCoordinadorSubAra(String dcfCoordinadorSubAra) {
			this.dcfCoordinadorSubAra = dcfCoordinadorSubAra;
		}

		public String getDcfCoordinadorTtl() {
			return dcfCoordinadorTtl;
		}

		public void setDcfCoordinadorTtl(String dcfCoordinadorTtl) {
			this.dcfCoordinadorTtl = dcfCoordinadorTtl;
		}

		public Integer getDcfNumEstudiantesTitulacion() {
			return dcfNumEstudiantesTitulacion;
		}

		public void setDcfNumEstudiantesTitulacion(Integer dcfNumEstudiantesTitulacion) {
			this.dcfNumEstudiantesTitulacion = dcfNumEstudiantesTitulacion;
		}

		public Integer getDcfHorasSemanalesCoordinador() {
			return dcfHorasSemanalesCoordinador;
		}

		public void setDcfHorasSemanalesCoordinador(Integer dcfHorasSemanalesCoordinador) {
			this.dcfHorasSemanalesCoordinador = dcfHorasSemanalesCoordinador;
		}

		public TipoFuncionCargaHoraria getDcfClinicasSlc() {
			return dcfClinicasSlc;
		}

		public void setDcfClinicasSlc(TipoFuncionCargaHoraria dcfClinicasSlc) {
			this.dcfClinicasSlc = dcfClinicasSlc;
		}

		public String getDcfUnidadAcademica() {
			return dcfUnidadAcademica;
		}

		public void setDcfUnidadAcademica(String dcfUnidadAcademica) {
			this.dcfUnidadAcademica = dcfUnidadAcademica;
		}

		public String getDcfAsignatura() {
			return dcfAsignatura;
		}

		public void setDcfAsignatura(String dcfAsignatura) {
			this.dcfAsignatura = dcfAsignatura;
		}

		public Integer getDcfNumEstudiantes() {
			return dcfNumEstudiantes;
		}

		public void setDcfNumEstudiantes(Integer dcfNumEstudiantes) {
			this.dcfNumEstudiantes = dcfNumEstudiantes;
		}

		public Integer getDcfHorasSemanalesCliPra() {
			return dcfHorasSemanalesCliPra;
		}

		public void setDcfHorasSemanalesCliPra(Integer dcfHorasSemanalesCliPra) {
			this.dcfHorasSemanalesCliPra = dcfHorasSemanalesCliPra;
		}
 
		public TipoFuncionCargaHoraria getDcfExamenSlc() {
			return dcfExamenSlc;
		}

		public void setDcfExamenSlc(TipoFuncionCargaHoraria dcfExamenSlc) {
			this.dcfExamenSlc = dcfExamenSlc;
		}

		public  List<Integer> getDcfFuncionSeleccionExamen() {
			return dcfFuncionSeleccionExamen;
		}

		public void setDcfFuncionSeleccionExamen( List<Integer> dcfFuncionSeleccionExamen) {
			this.dcfFuncionSeleccionExamen = dcfFuncionSeleccionExamen;
		}

		public Integer getDcfHorasSemanalesExaReac() {
			return dcfHorasSemanalesExaReac;
		}

		public void setDcfHorasSemanalesExaReac(Integer dcfHorasSemanalesExaReac) {
			this.dcfHorasSemanalesExaReac = dcfHorasSemanalesExaReac;
		}

		public Boolean getDcfEditarExamen() {
			return dcfEditarExamen;
		}

		public void setDcfEditarExamen(Boolean dcfEditarExamen) {
			this.dcfEditarExamen = dcfEditarExamen;
		}

		public Boolean getDcfEditarClinicas() {
			return dcfEditarClinicas;
		}

		public void setDcfEditarClinicas(Boolean dcfEditarClinicas) {
			this.dcfEditarClinicas = dcfEditarClinicas;
		}

		public Boolean getDcfEditarCoordinador() {
			return dcfEditarCoordinador;
		}

		public void setDcfEditarCoordinador(Boolean dcfEditarCoordinador) {
			this.dcfEditarCoordinador = dcfEditarCoordinador;
		}

		public Boolean getDcfEditarOtrosAsignados() {
			return dcfEditarOtrosAsignados;
		}

		public void setDcfEditarOtrosAsignados(Boolean dcfEditarConsejo) {
			this.dcfEditarOtrosAsignados = dcfEditarConsejo;
		}

		public Boolean getDcfCargaHorariaActiva() {
			return dcfCargaHorariaActiva;
		}

		public void setDcfCargaHorariaActiva(Boolean dcfCargaHorariaActiva) {
			this.dcfCargaHorariaActiva = dcfCargaHorariaActiva;
		}

		public Integer getDcfPeriodoAcademicoBuscar() {
			return dcfPeriodoAcademicoBuscar;
		}

		public void setDcfPeriodoAcademicoBuscar(Integer dcfPeriodoAcademicoBuscar) {
			this.dcfPeriodoAcademicoBuscar = dcfPeriodoAcademicoBuscar;
		}

		public List<PeriodoAcademico> getDcfListaPeriodosAcademicos() {
			return dcfListaPeriodosAcademicos;
		}

		public void setDcfListaPeriodosAcademicos(List<PeriodoAcademico> dcfListaPeriodosAcademicos) {
			this.dcfListaPeriodosAcademicos = dcfListaPeriodosAcademicos;
		}

		public String getDcfCedulaDocenteBusquedaAvanzada() {
			return dcfCedulaDocenteBusquedaAvanzada;
		}

		public void setDcfCedulaDocenteBusquedaAvanzada(String dcfCedulaDocenteBusquedaAvanzada) {
			this.dcfCedulaDocenteBusquedaAvanzada = dcfCedulaDocenteBusquedaAvanzada;
		}

		public String getDcfApellidoDocenteBusquedaAvanzada() {
			return dcfApellidoDocenteBusquedaAvanzada;
		}

		public void setDcfApellidoDocenteBusquedaAvanzada(String dcfApellidoDocenteBusquedaAvanzada) {
			this.dcfApellidoDocenteBusquedaAvanzada = dcfApellidoDocenteBusquedaAvanzada;
		}

		public String getDcfMensajeBusquedaAvanzada() {
			return dcfMensajeBusquedaAvanzada;
		}

		public void setDcfMensajeBusquedaAvanzada(String dcfMensajeBusquedaAvanzada) {
			this.dcfMensajeBusquedaAvanzada = dcfMensajeBusquedaAvanzada;
		}

		public List<PersonaDatosDto> getDcfListDocentesBusquedaAvanzada() {
			return dcfListDocentesBusquedaAvanzada;
		}

		public void setDcfListDocentesBusquedaAvanzada(List<PersonaDatosDto> dcfListDocentesBusquedaAvanzada) {
			this.dcfListDocentesBusquedaAvanzada = dcfListDocentesBusquedaAvanzada;
		}

		public List<Integer> getDcfFuncionSeleccionElaboracionReac() {
			return dcfFuncionSeleccionElaboracionReac;
		}

		public void setDcfFuncionSeleccionElaboracionReac(List<Integer> dcfFuncionSeleccionElaboracionReac) {
			this.dcfFuncionSeleccionElaboracionReac = dcfFuncionSeleccionElaboracionReac;
		}

//		public List<Integer> getDcfFuncionSeleccionLector() {
//			return dcfFuncionSeleccionLector;
//		}
//
//		public void setDcfFuncionSeleccionLector(List<Integer> dcfFuncionSeleccionLector) {
//			this.dcfFuncionSeleccionLector = dcfFuncionSeleccionLector;
//		}
//
//		public String getDcfLector() {
//			return dcfLector;
//		}
//
//		public void setDcfLector(String dcfLector) {
//			this.dcfLector = dcfLector;
//		}

		public Integer getDcfHorasSemanalesElaboracionReac() {
			return dcfHorasSemanalesElaboracionReac;
		}

		public void setDcfHorasSemanalesElaboracionReac(Integer dcfHorasSemanalesElaboracionReac) {
			this.dcfHorasSemanalesElaboracionReac = dcfHorasSemanalesElaboracionReac;
		}

		public Integer getDcfHorasSemanalesOtrasAsig() {
			return dcfHorasSemanalesOtrasAsig;
		}

		public void setDcfHorasSemanalesOtrasAsig(Integer dcfHorasSemanalesOtrasAsig) {
			this.dcfHorasSemanalesOtrasAsig = dcfHorasSemanalesOtrasAsig;
		}

		public List<Integer> getDcfFuncionSeleccionCapacitacionDocente() {
			return dcfFuncionSeleccionCapacitacionDocente;
		}

		public void setDcfFuncionSeleccionCapacitacionDocente(List<Integer> dcfFuncionSeleccionCapacitacionDocente) {
			this.dcfFuncionSeleccionCapacitacionDocente = dcfFuncionSeleccionCapacitacionDocente;
		}

		public String getDcfCapacitacionDocente() {
			return dcfCapacitacionDocente;
		}

		public void setDcfCapacitacionDocente(String dcfCapacitacionDocente) {
			this.dcfCapacitacionDocente = dcfCapacitacionDocente;
		}

		public List<Integer> getDcfFuncionSeleccionPublicacionAcademica() {
			return dcfFuncionSeleccionPublicacionAcademica;
		}

		public void setDcfFuncionSeleccionPublicacionAcademica(List<Integer> dcfFuncionSeleccionPublicacionAcademica) {
			this.dcfFuncionSeleccionPublicacionAcademica = dcfFuncionSeleccionPublicacionAcademica;
		}

		public String getDcfPublicacionAcademica() {
			return dcfPublicacionAcademica;
		}

		public void setDcfPublicacionAcademica(String dcfPublicacionAcademica) {
			this.dcfPublicacionAcademica = dcfPublicacionAcademica;
		}

		public List<Integer> getDcfFuncionSeleccionDiseno() {
			return dcfFuncionSeleccionDiseno;
		}

		public void setDcfFuncionSeleccionDiseno(List<Integer> dcfFuncionSeleccionDiseno) {
			this.dcfFuncionSeleccionDiseno = dcfFuncionSeleccionDiseno;
		}

		public Boolean getDcfEditarLector() {
			return dcfEditarLector;
		}

		public void setDcfEditarLector(Boolean dcfEditarLector) {
			this.dcfEditarLector = dcfEditarLector;
		}

		public Boolean getDcfEditarCoordinadorDiseno() {
			return dcfEditarCoordinadorDiseno;
		}

		public void setDcfEditarCoordinadorDiseno(Boolean dcfEditarCoordinadorDiseno) {
			this.dcfEditarCoordinadorDiseno = dcfEditarCoordinadorDiseno;
		}

		public List<Integer> getDcfListaHorasClinicas() {
			return dcfListaHorasClinicas;
		}

		public void setDcfListaHorasClinicas(List<Integer> dcfListaHorasClinicas) {
			this.dcfListaHorasClinicas = dcfListaHorasClinicas;
		}

		public Boolean getDcfEditarReactvos() {
			return dcfEditarReactvos;
		}

		public void setDcfEditarReactvos(Boolean dcfEditarReactvos) {
			this.dcfEditarReactvos = dcfEditarReactvos;
		}

		public String getDcfLinkReporte() {
			return dcfLinkReporte;
		}

		public void setDcfLinkReporte(String dcfLinkReporte) {
			this.dcfLinkReporte = dcfLinkReporte;
		}
 
		
		
  
	}