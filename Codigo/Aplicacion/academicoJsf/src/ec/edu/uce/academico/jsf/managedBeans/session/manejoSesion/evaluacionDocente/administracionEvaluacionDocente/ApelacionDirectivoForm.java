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

 ARCHIVO:     EvaluacionDirectivoForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Directores de Carrera. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Arturo Villedfuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionDocente.administracionEvaluacionDocente;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AsignacionEvaluadorServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ContenidoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluadorEvaluadoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FuncionTipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoContenidoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TpcnFuncionTpevServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.EvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.AsignacionEvaluador;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.EvaluadorEvaluado;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionDirectivoForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del Directivo.
 * 
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name = "apelacionDirectivoForm")
@SessionScoped
public class ApelacionDirectivoForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario edfUsuario; 
	private PersonaDatosDto edfDocente; 

	private Integer edfCarreraBuscar;  
  
	private Evaluacion edfEvaluacionActiva; 

	private List<Carrera> edfListaCarrerasDocente; 
	private List<PersonaDatosDto> edfListaDocentesCarrera;
	private PersonaDatosDto edfPersonaDtoSeleccion;
	
	private List<FuncionTipoEvaluacion> edfListaFuncionTipoEvaluacion;
	
	private Integer[] edfRadioSeleccion;
	private List<Contenido> edfListaSeleccionContenido;
	private List<TipoContenido> edfListaTipoContenido;
	
	private UsuarioRol edfUsroEvaluador;
	private UsuarioRol edfUsroEvaluado;
	
	private Integer edfActivarReporte;

	//--v3
	//URL reporte
	private String edfLinkReporte;

	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/

	@PostConstruct
	public void inicializar() {
	}

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	private @EJB CarreraServicio servEdfCarreraServicio;
	private @EJB PersonaDatosDtoServicioJdbc servEdfPersonaDatosServicioJdbc;
	private @EJB PersonaServicio servEdfPersonaServicio;
	private @EJB PlanificacionCronogramaDtoServicioJdbc servEdfPlanificacionCronogramaDtoServicioJdbc;
	private @EJB UsuarioRolServicio servEdfUsuarioRolServicio; 
	private @EJB RolFlujoCarreraServicio servEdfRolFlujoCarreraServicio;
	private @EJB DocenteDatosDtoServicioJdbc servEdfDocenteDatosDtoServicioJdbc;
	private @EJB DocenteDtoServicioJdbc servEdfDocenteDtoServicioJdbc;
	private @EJB EvaluacionServicio servEdfEvaluacionServicio;
	private @EJB FuncionTipoEvaluacionServicio servEdfFuncionTipoEvaluacionServicio;
	private @EJB TipoContenidoServicio servEdfTipoContenidoServicio;
	private @EJB ContenidoServicio servEdfContenidoServicio;
	private @EJB EvaluadorEvaluadoServicio servEdfEvaluadorEvaluadoServicio; 
	private @EJB AsignacionEvaluadorServicio servEdfAsignacionEvaluadorServicio;
	private @EJB TpcnFuncionTpevServicio servEdfTpcnFuncionTpevServicio;
	private @EJB ContenidoEvaluacionDtoServicioJdbc servEdfContenidoEvaluacionDtoJdbcServicio;
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/** 
	 * Inicia los parametros de la funcionalidad
	 */
	private void inicarParametros() { 
		verificarAcceso(); 
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irEvaluacionDirectivoDocente(Usuario usuario) {
		
		Boolean verificar = false;
		
		try {
			
			edfActivarReporte = 0;
			edfUsuario = usuario;
			edfUsroEvaluador = servEdfUsuarioRolServicio.buscarXUsuarioXrol(edfUsuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE);
			edfListaCarrerasDocente = new ArrayList<>();
			 
			List<RolFlujoCarrera> rolflujocarrera = servEdfRolFlujoCarreraServicio.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_DIRCARRERA_VALUE);
			
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				edfListaCarrerasDocente.add(servEdfCarreraServicio.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}

			if(rolflujocarrera != null && rolflujocarrera.size()>0){
				edfCarreraBuscar = edfListaCarrerasDocente.get(edfListaCarrerasDocente.size()-1).getCrrId();
				verificar = true;
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionDirectivo.ir.evaluacion.directivo.docente.sin.carreras")));
			}
			
			if(verificar){
				inicarParametros();
			}
			
			
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		
		return "irApelacionDirectivoDocente";
	}


	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		limpiar();
		return "irInicio";
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de evaluar
	 * @return Navegacion a la pagina de evaluar.
	 */
	@SuppressWarnings("rawtypes")
	public String irEvaluarDirectivo(PersonaDatosDto docente) {

		try {
			
			edfUsroEvaluado = servEdfUsuarioRolServicio.buscarXPersonaXrol(docente.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);

			if(validarCronograma()){
//				if(!verificarEvaluacion(edfEvaluacionActiva, docente,  0)){

					edfPersonaDtoSeleccion = docente;

					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

					edfListaTipoContenido = new ArrayList<>();
					edfListaSeleccionContenido = new ArrayList<>();

					edfListaFuncionTipoEvaluacion = servEdfFuncionTipoEvaluacionServicio.listarActivoXTipoEvaluacion(edfEvaluacionActiva.getEvTipoEvaluacion().getTpevId());

					for(FuncionTipoEvaluacion it: edfListaFuncionTipoEvaluacion){

						Boolean verificar = false;

						Iterator iter = it.getFntpevListTpcnFuncionTpev().iterator();
						while(iter.hasNext()){
							verificar = false;
							TpcnFuncionTpev cad = (TpcnFuncionTpev) iter.next();

							if(cad.getTpcnfntpevTipoContenido().getTpcnTipo().intValue() == 1){
								iter.remove();
								verificar = true;
							}

							if(!verificar && cad.getTpcnfntpevTipoContenido().getTpcnEstado().intValue() == 1){
								iter.remove();
								verificar = true;
							}

							if(!verificar && cad.getTpcnfntpevEstado().intValue() == 1){
								iter.remove();
								verificar = true;
							}
						}

						Collections.sort(it.getFntpevListTpcnFuncionTpev());

						for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){

							Iterator itera = ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido().iterator();
							while(iter.hasNext()){
								TipoContenido cad = (TipoContenido) iter.next();
								if(cad.getTpcnEstado().intValue() == 1){
									itera.remove();
								}
							}
							
							Collections.sort(ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido());

							for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){

								edfListaTipoContenido.add(item);

								Contenido cnt = new Contenido();
								cnt.setCntEvaluacion(edfEvaluacionActiva);
								cnt.setCntTpcnFuncionTpev(servEdfTpcnFuncionTpevServicio.buscarXTipoContenido(item.getTpcnId()));
								cnt.setCntFecha(fechaActual);
								cnt.setCntUsuario(edfUsuario.getUsrNick());
								cnt.setCntDescripcion(item.getTpcnDescripcion()); 

								edfListaSeleccionContenido.add(cnt);
							}
						}
						
					// cargar lo encuestado	
//					} 

					edfRadioSeleccion = new Integer[edfListaTipoContenido.size()];

					List<ContenidoEvaluacionDto> listaContenidos = cargarEvaluacion(edfEvaluacionActiva, docente,  0);
					
					for (ContenidoEvaluacionDto item : listaContenidos) {
						System.out.println(item.getTpcnNumeral().toString());
						System.out.println("indicador"+ item.getTpcnDescripcion().toString());
						System.out.println("valoracion"+ calcularComponenteEtiqueta(item.getCntSeleccion(), item.getTpcnTipoSeleccion()) );
					}
				}
			}

		} catch (FuncionTipoEvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FuncionTipoEvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TpcnFuncionTpevNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());  
		} catch (TpcnFuncionTpevException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} 
		
		return "irApelarDirectivo";
	}

	/**
	 * limpia y regresa a la ventana anterior
	 * @return Navegacion a la pagina anterior.
	 */
	public String irRegresar() {
		limpiarEvaluar();
		inicarParametros();
		edfActivarReporte = 0;
		return "irApelacionDirectivoDocente";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		edfDocente = null;   
		edfLinkReporte = null;
		edfUsuario = null; 
		edfEvaluacionActiva = null;
		edfListaCarrerasDocente = null; 
		edfListaFuncionTipoEvaluacion = null;
		edfRadioSeleccion = null;
		edfListaSeleccionContenido = null;
		edfListaTipoContenido = null;
		edfCarreraBuscar = null;
		edfListaDocentesCarrera = null;
		edfPersonaDtoSeleccion = null;   
	}
	
	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiarEvaluar() {

		edfListaFuncionTipoEvaluacion = null;
		edfRadioSeleccion = null;
		edfListaSeleccionContenido = null;
		edfListaTipoContenido = null;

	}


	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		if(validarCronograma()){
			cargarDocentesCarrera();
		}
	}

	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------


	/**
	 * Guarda los parametros del formulario
	 **/
	public String guardar() {

		String direccion = null;
		Boolean verificar = true;
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

		if(validarCronograma()){
			if(verificarEvaluacion(edfEvaluacionActiva, edfPersonaDtoSeleccion,  0)){
				try{

					if(verificarGuardar()){

						EvaluadorEvaluado evalrEval = new EvaluadorEvaluado();
						evalrEval.setEvevEvaluador(edfUsroEvaluador.getUsroId());
						evalrEval.setEvevUsuarioRol(edfUsroEvaluado);
						evalrEval = servEdfEvaluadorEvaluadoServicio.anadir(evalrEval);

						AsignacionEvaluador asigEval= new AsignacionEvaluador();
						asigEval.setAsevEvaluadorEvaluado(evalrEval);
						asigEval.setAsevEvaluadorCrrId(edfCarreraBuscar);
						asigEval.setAsevEstado(0);
						asigEval.setAsevUsuario(edfUsuario.getUsrNick());
						asigEval.setAsevFecha(fechaActual);
						asigEval = servEdfAsignacionEvaluadorServicio.anadir(asigEval);


						for(Contenido item: edfListaSeleccionContenido){
							item.setCntAsignacionEvaluador(asigEval);
							Contenido contenido = servEdfContenidoServicio.anadir(item);
							if(contenido == null){
								verificar = false;
							}
						}

						if(verificar){
							direccion = irRegresar();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionDirectivo.guardar.exito")));
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionDirectivo.guardar.no.exito")));
						}
					}
				} catch (ContenidoValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
					e.printStackTrace();
				} catch (ContenidoException e) {
					FacesUtil.mensajeError(e.getMessage());
					e.printStackTrace();
				} catch (AsignacionEvaluadorValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
					e.printStackTrace();
				} catch (AsignacionEvaluadorException e) {
					FacesUtil.mensajeError(e.getMessage());
					e.printStackTrace();
				} catch (EvaluadorEvaluadoValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
					e.printStackTrace();
				} catch (EvaluadorEvaluadoException e) {
					FacesUtil.mensajeError(e.getMessage());
					e.printStackTrace();
				} 
			}else {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Actualice!");
			}
		}

		return direccion;
	}

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------
 
	@SuppressWarnings("rawtypes")
	public void cargarDocentesCarrera(){
		try { 
			edfListaDocentesCarrera = servEdfPersonaDatosServicioJdbc.listarXCarreraConCargaAcademica(edfCarreraBuscar, edfEvaluacionActiva.getEvPeriodoAcademico().getPracId(), RolConstantes.ROL_DOCENTE_VALUE);
			Iterator itera = edfListaDocentesCarrera.iterator();
			while(itera.hasNext()){
				PersonaDatosDto cad = (PersonaDatosDto) itera.next();
				if(cad.getPrsId() == edfDocente.getPrsId()){
					itera.remove();
				}
			}
		} catch (PersonaDatosDtoNoEncontradoException e) {
			edfListaDocentesCarrera = null;
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------

	 

	/**
	 * Verifica el acceso del docente a la autoevaluacion
	 * otorgando un estado true / false para activar la evaluacion
	 */
	public void verificarAcceso(){

		
		try{ 
			
			edfEvaluacionActiva = servEdfEvaluacionServicio.buscarActivoXTipo(TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE);
			// Cronograma para Evaluaciones
			if(validarCronograma()){ 
				PlanificacionEvaluacion apelacion = servEdfEvaluacionServicio.buscarPlanificacionEvaluacion(edfEvaluacionActiva.getEvaId(), ProcesoFlujoConstantes.PRFL_EVALUACION_APELACION_DIRECTIVO_VALUE, PlanificacionEvaluacionConstantes.PLEV_ESTADO_ACTIVO_VALUE);
				if (verificarCronogramaApelacion(apelacion)) {
					edfDocente = servEdfPersonaDatosServicioJdbc.buscarPorId(edfUsuario.getUsrPersona().getPrsId() , edfEvaluacionActiva.getEvPeriodoAcademico().getPracId());
					cargarDocentesCarrera();
				}	
			}
			
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionDirectivo.verificar.acceso.evaluacion.no.activa.validacion.exception")));
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PlanificacionEvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró cronograma para apelación de directivos.");
		} catch (PlanificacionEvaluacionValidacionException e) {
			FacesUtil.mensajeError("Se encontró mas de un cronograma activo para la misma evaluación.");
		} catch (PlanificacionEvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	private boolean verificarCronogramaApelacion(PlanificacionEvaluacion cronograma) {

		Boolean verificar = false;
		Timestamp fechaActual = GeneralesUtilidades.getFechaActualSistemaTimestamp();

		if(cronograma != null){

			if(cronograma.getPlevFechaInicio() != null && cronograma.getPlevFechaFin() != null){

				if(cronograma.getPlevFechaFin().after(fechaActual)){
					if(cronograma.getPlevFechaInicio().before(fechaActual)){ 
						verificar = true;
					}else{ 
						FacesUtil.mensajeError("El cronograma para apelación, evaluación al directivo no ha empezado."); 
					}
				}else{ 
					FacesUtil.mensajeError("El cronograma para apelación, evaluación al directivo ha expirado."); 
				}
			}else{
				FacesUtil.mensajeError("No se ha asignado cronograma para apelación, evaluación al directivo.");
			}
		}else{
			FacesUtil.mensajeError("No se ha asignado cronograma para apelación, evaluación al directivo.");
		}


		return verificar;
	}

	/**
	 * Verifica campos, parametos antes de guardar las distintas cargas horarias
	 * @return Estado false por culquier error al ingresar, true - pasa las validaciones 
	 **/
	public Boolean verificarGuardar() {
		Boolean verificar = true;
		if(validarCronograma()){
			Integer sinRespuesta = null;
			int count = 1;
			for(Contenido cnt: edfListaSeleccionContenido){
				if(cnt.getCntSeleccion() == null){
					if(sinRespuesta == null){
						sinRespuesta = count;
					}
					verificar = false;
				}
				count = count+1;
			}
			if(!verificar){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionDirectivo.verificar.guardar.sin.respuesta",sinRespuesta)));
				sinRespuesta = null;
			}
		}else{
			verificar = false;
		}
		return verificar;
	}
  
	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		Boolean verificar = false;
		
		if(edfEvaluacionActiva != null){
			if(edfEvaluacionActiva.getEvaEstado().equals(EvaluacionConstantes.ESTADO_ACTIVO_VALUE)){
				verificar = true;
			}else{
				FacesUtil.mensajeError("Es necesario que la Evaluación que quiere realizar la Apelación se encuentre Activa.");
			}
		}
		
		return verificar;
	}
	
	/**
	 * Transforma el id de estado a descripcion
	 **/
	public List<Integer> calcularComponente(Integer num, Integer tipoSeleccion){
		List<Integer> lista = new ArrayList<>();
		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_UNICO_VALUE){
			lista.add(TipoContenidoConstantes.VALORACION_SI_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_NO_VALUE);
		}else{
			lista.add(TipoContenidoConstantes.VALORACION_SIEMPRE_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_CASI_SIEMPRE_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_A_VECES_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_NUNCA_VALUE);
		}
		return lista;
	}

	/**
	 * Transforma el id de estado a descripcion
	 **/
	public String calcularComponenteEtiqueta(Integer num, Integer tipoSeleccion){
		String lista = null;

		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_UNICO_VALUE){
			switch (num) {
			case 0:
				lista = TipoContenidoConstantes.VALORACION_SI_LABEL;
				break;
			case 1:
				lista = TipoContenidoConstantes.VALORACION_NO_LABEL;
				break;
			default:
				lista = TipoContenidoConstantes.VALORACION_SIN_ETIQUETA_LABEL;
				break;
			}
		}else{

			switch (num) {
			case 0:
				lista = TipoContenidoConstantes.VALORACION_NUNCA_LABEL;
				break;
			case 1:
				lista = TipoContenidoConstantes.VALORACION_A_VECES_LABEL;
				break;
			case 2:
				lista = TipoContenidoConstantes.VALORACION_CASI_SIEMPRE_LABEL;
				break;
			case 3:
				lista = TipoContenidoConstantes.VALORACION_SIEMPRE_LABEL;
				break;
			default:
				lista = TipoContenidoConstantes.VALORACION_SIN_ETIQUETA_LABEL;
				break;
			}
		}
		return lista;
	}

	/**
	 * Agrega la respuesta de seleccion en el tipo de contenido correspondiente
	 **/
	public void agregarSeleccion(int idFnctpev, int idTpcn){

		for(FuncionTipoEvaluacion it: edfListaFuncionTipoEvaluacion){
			for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){
				if(ite.getTpcnfntpevFuncionTipoEvaluacion().getFnctpevId() == idFnctpev){
					for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){
						if( item.getTpcnId() == idTpcn){
							for(int i = 0; i<edfListaTipoContenido.size(); i++){
								if(edfListaTipoContenido.get(i).getTpcnId() == idTpcn){
									edfListaSeleccionContenido.get(i).setCntSeleccion(edfRadioSeleccion[i]);
								}
							}							
						}
					}
				}
			}
		} 
	}
	
	private List<ContenidoEvaluacionDto> cargarEvaluacion(Evaluacion evaluacion, PersonaDatosDto persona, int mensaje){
		List<ContenidoEvaluacionDto> contenido = new ArrayList<>();
		try {
			UsuarioRol usroEvaluado = servEdfUsuarioRolServicio.buscarXPersonaXrol(persona.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			contenido = servEdfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), GeneralesConstantes.APP_ID_BASE, edfUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), edfCarreraBuscar);
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return contenido;
	}
	
	
	
	/**
	 * Verifica la existencia de una evaluacion.
	 **/
	public Boolean verificarEvaluacion(Evaluacion evaluacion, PersonaDatosDto persona, int mensaje){
		Boolean verificar = false;
		try {
			UsuarioRol usroEvaluado = servEdfUsuarioRolServicio.buscarXPersonaXrol(persona.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			List<ContenidoEvaluacionDto> contenido = servEdfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), GeneralesConstantes.APP_ID_BASE, edfUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), edfCarreraBuscar);
			if(contenido == null || contenido.size() == 0){
				verificar = true;
			}
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			verificar = true;
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		}
		
		if(!verificar && mensaje == 0){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionDirectivo.verificar.evaluacion.existente")));
		}
		return verificar;
	}
	
	/**
	 * Genera el reporte del directivo
	 * @param docente - docente a incluir la informacion
	 **/
	public void generarReporteDirectivo(PersonaDatosDto docente){ 
		
		try {
			
			UsuarioRol usroEvaluado = servEdfUsuarioRolServicio.buscarXPersonaXrol(docente.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			Carrera carrera = servEdfCarreraServicio.buscarPorId(edfCarreraBuscar);
			List<ContenidoEvaluacionDto> contenido = servEdfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(edfEvaluacionActiva.getEvTipoEvaluacion().getTpevId(), edfEvaluacionActiva.getEvaId(), GeneralesConstantes.APP_ID_BASE, edfUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), edfCarreraBuscar);
			ReporteEvaluacionDocenteForm.generarReporteDirectivo(contenido, docente, edfEvaluacionActiva, carrera);
			edfActivarReporte = 1;
			
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		}
		
	}
	
  
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getEdfUsuario() {
		return edfUsuario;
	}

	public void setEdfUsuario(Usuario edfUsuario) {
		this.edfUsuario = edfUsuario;
	}

	public PersonaDatosDto getEdfDocente() {
		return edfDocente;
	}

	public void setEdfDocente(PersonaDatosDto edfDocente) {
		this.edfDocente = edfDocente;
	} 

	public Integer getEdfCarreraBuscar() {
		return edfCarreraBuscar;
	}

	public void setEdfCarreraBuscar(Integer edfCarreraBuscar) {
		this.edfCarreraBuscar = edfCarreraBuscar;
	} 
 
	public String getEdfLinkReporte() {
		return edfLinkReporte;
	}

	public void setEdfLinkReporte(String edfLinkReporte) {
		this.edfLinkReporte = edfLinkReporte;
	}

	public List<Carrera> getEdfListaCarrerasDocente() {
		return edfListaCarrerasDocente;
	}

	public void setEdfListaCarrerasDocente(List<Carrera> edfListaCarrerasDocente) {
		this.edfListaCarrerasDocente = edfListaCarrerasDocente;
	}
 
	public List<PersonaDatosDto> getEdfListaDocentesCarrera() {
		return edfListaDocentesCarrera;
	}

	public void setEdfListaDocentesCarrera(List<PersonaDatosDto> edfListaDocentesCarrera) {
		this.edfListaDocentesCarrera = edfListaDocentesCarrera;
	}

	public List<FuncionTipoEvaluacion> getEdfListaFuncionTipoEvaluacion() {
		return edfListaFuncionTipoEvaluacion;
	}

	public void setEdfListaFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> edfListaFuncionTipoEvaluacion) {
		this.edfListaFuncionTipoEvaluacion = edfListaFuncionTipoEvaluacion;
	}

	public Evaluacion getEdfEvaluacionActiva() {
		return edfEvaluacionActiva;
	}

	public void setEdfEvaluacionActiva(Evaluacion edfEvaluacionActiva) {
		this.edfEvaluacionActiva = edfEvaluacionActiva;
	}

	public Integer[] getEdfRadioSeleccion() {
		return edfRadioSeleccion;
	}

	public void setEdfRadioSeleccion(Integer[] edfRadioSeleccion) {
		this.edfRadioSeleccion = edfRadioSeleccion;
	}

	public List<Contenido> getEdfListaSeleccionContenido() {
		return edfListaSeleccionContenido;
	}

	public void setEdfListaSeleccionContenido(List<Contenido> edfListaSeleccionContenido) {
		this.edfListaSeleccionContenido = edfListaSeleccionContenido;
	}

	public List<TipoContenido> getEdfListaTipoContenido() {
		return edfListaTipoContenido;
	}

	public void setEdfListaTipoContenido(List<TipoContenido> edfListaTipoContenido) {
		this.edfListaTipoContenido = edfListaTipoContenido;
	}

	public PersonaDatosDto getEdfPersonaDtoSeleccion() {
		return edfPersonaDtoSeleccion;
	}

	public void setEdfPersonaDtoSeleccion(PersonaDatosDto edfPersonaDtoSeleccion) {
		this.edfPersonaDtoSeleccion = edfPersonaDtoSeleccion;
	}

	public Integer getEdfActivarReporte() {
		return edfActivarReporte;
	}

	public void setEdfActivarReporte(Integer edfActivarReporte) {
		this.edfActivarReporte = edfActivarReporte;
	}

}