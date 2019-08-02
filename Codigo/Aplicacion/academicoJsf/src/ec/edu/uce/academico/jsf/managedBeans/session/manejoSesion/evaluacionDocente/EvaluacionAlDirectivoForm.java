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

 ARCHIVO:     EvaluacionAlDirectivoForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Directores de Carrera. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
24-AGOSTO-2017		 Arturo Villeadfuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionDocente;

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
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.AsignacionEvaluador;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.EvaluadorEvaluado;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionAlDirectivoForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del Directivo.
 * 
 * @author ajvilleadfuerte.
 * @version 1.0
 */

@ManagedBean(name = "evaluacionAlDirectivoForm")
@SessionScoped
public class EvaluacionAlDirectivoForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario eadfUsuario; 
	private PersonaDatosDto eadfDocente; 

	private Integer eadfCarreraBuscar;  
  
	private Evaluacion eadfEvaluacionActiva; 

	private List<Carrera> eadfListaCarrerasDocente; 
	private List<PersonaDatosDto> eadfListaDocentesCarrera;
	private PersonaDatosDto eadfPersonaDtoSeleccion;
	
	private List<FuncionTipoEvaluacion> eadfListaFuncionTipoEvaluacion;
	
	private Integer[] eadfRadioSeleccion;
	private List<Contenido> eadfListaSeleccionContenido;
	private List<TipoContenido> eadfListaTipoContenido;
	
	private UsuarioRol eadfUsroEvaluador;
	private UsuarioRol eadfUsroEvaluado;
	
	private Integer eadfActivarReporte;

	//--v3
	//URL reporte
	private String eadfLinkReporte;

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
	CarreraServicio servEadfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servEadfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servEadfPersonaServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servEadfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servEadfUsuarioRolServicio; 
	@EJB
	RolFlujoCarreraServicio servEadfRolFlujoCarreraServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servEadfDocenteDatosDtoServicioJdbc;
	@EJB
	DocenteDtoServicioJdbc servEadfDocenteDtoServicioJdbc;
	@EJB
	EvaluacionServicio servEadfEvaluacionServicio;
	@EJB
	FuncionTipoEvaluacionServicio servEadfFuncionTipoEvaluacionServicio;
	@EJB
	TipoContenidoServicio servEadfTipoContenidoServicio;
	@EJB
	ContenidoServicio servEadfContenidoServicio;
	@EJB
	EvaluadorEvaluadoServicio servEadfEvaluadorEvaluadoServicio; 
	@EJB
	AsignacionEvaluadorServicio servEadfAsignacionEvaluadorServicio;
	@EJB
	TpcnFuncionTpevServicio servEadfTpcnFuncionTpevServicio;
	@EJB
	ContenidoEvaluacionDtoServicioJdbc servEadfContenidoEvaluacionDtoJdbcServicio;
	
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
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irEvaluacionAlDirectivoDocente(Usuario usuario) {
		
		Boolean verificar = false;
		
		try {
			eadfActivarReporte = 0;
			eadfUsuario = usuario;
			eadfUsroEvaluador = servEadfUsuarioRolServicio.buscarXUsuarioXrol(eadfUsuario.getUsrId(), RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);
			
			eadfListaCarrerasDocente = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servEadfRolFlujoCarreraServicio.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);
			
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				eadfListaCarrerasDocente.add(servEadfCarreraServicio.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}

			if(rolflujocarrera != null && rolflujocarrera.size()>0){
				eadfCarreraBuscar = eadfListaCarrerasDocente.get(eadfListaCarrerasDocente.size()-1).getCrrId();
				verificar = true;
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.ir.evaluacion.directivo.docente.sin.carreras")));
			}
			
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		if(verificar){
			inicarParametros();
		}
		return "irEvaluacionAlDirectivoDocente";
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
	public String irEvaluarAlParAcademico(PersonaDatosDto docente) {

		try {
			eadfUsroEvaluado = servEadfUsuarioRolServicio.buscarXPersonaXrol(docente.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);

			if(validarCronograma()){
				if(verificarEvaluacion(eadfEvaluacionActiva, docente,  0)){

					eadfPersonaDtoSeleccion = docente;

					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

					eadfListaTipoContenido = new ArrayList<>();
					eadfListaSeleccionContenido = new ArrayList<>();

					eadfListaFuncionTipoEvaluacion = servEadfFuncionTipoEvaluacionServicio.listarActivoXTipoEvaluacion(eadfEvaluacionActiva.getEvTipoEvaluacion().getTpevId());

					for(FuncionTipoEvaluacion it: eadfListaFuncionTipoEvaluacion){

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

								eadfListaTipoContenido.add(item);

								Contenido cnt = new Contenido();
								cnt.setCntEvaluacion(eadfEvaluacionActiva);
								cnt.setCntTpcnFuncionTpev(servEadfTpcnFuncionTpevServicio.buscarXTipoContenido(item.getTpcnId()));
								cnt.setCntFecha(fechaActual);
								cnt.setCntUsuario(eadfUsuario.getUsrNick());
								cnt.setCntDescripcion(item.getTpcnDescripcion()); 

								eadfListaSeleccionContenido.add(cnt);
							}
						}
					}
					eadfRadioSeleccion = new Integer[eadfListaTipoContenido.size()]; 
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
		
		return "irEvaluarAlDirectivo";
	}

	/**
	 * limpia y regresa a la ventana anterior
	 * @return Navegacion a la pagina anterior.
	 */
	public String irRegresar() {
		limpiarEvaluar();
		inicarParametros();
		return "irEvaluacionAlDirectivoDocente";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		eadfDocente = null;   
		eadfLinkReporte = null;
		eadfUsuario = null; 
		eadfEvaluacionActiva = null;
		eadfListaCarrerasDocente = null; 
		eadfListaFuncionTipoEvaluacion = null;
		eadfRadioSeleccion = null;
		eadfListaSeleccionContenido = null;
		eadfListaTipoContenido = null;
		eadfCarreraBuscar = null;
		eadfListaDocentesCarrera = null;
		eadfPersonaDtoSeleccion = null;   
	}
	
	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiarEvaluar() {

		eadfListaFuncionTipoEvaluacion = null;
		eadfRadioSeleccion = null;
		eadfListaSeleccionContenido = null;
		eadfListaTipoContenido = null;

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
			if(verificarEvaluacion(eadfEvaluacionActiva, eadfPersonaDtoSeleccion,  0)){
				try{

					if(verificarGuardar()){

						EvaluadorEvaluado evalrEval = new EvaluadorEvaluado();
						evalrEval.setEvevEvaluador(eadfUsroEvaluador.getUsroId());
						evalrEval.setEvevUsuarioRol(eadfUsroEvaluado);
						evalrEval = servEadfEvaluadorEvaluadoServicio.anadir(evalrEval);

						AsignacionEvaluador asigEval= new AsignacionEvaluador();
						asigEval.setAsevEvaluadorEvaluado(evalrEval);
						asigEval.setAsevEvaluadorCrrId(eadfCarreraBuscar);
						asigEval.setAsevEstado(0);
						asigEval.setAsevUsuario(eadfUsuario.getUsrNick());
						asigEval.setAsevFecha(fechaActual);
						asigEval = servEadfAsignacionEvaluadorServicio.anadir(asigEval);


						for(Contenido item: eadfListaSeleccionContenido){
							item.setCntAsignacionEvaluador(asigEval);
							Contenido contenido = servEadfContenidoServicio.anadir(item);
							if(contenido == null){
								verificar = false;
							}
						}

						if(verificar){
							direccion = irRegresar();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.guardar.exito")));
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.guardar.no.exito")));
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
			eadfListaDocentesCarrera = servEadfPersonaDatosServicioJdbc.listarXCarreraConCargaAcademicaDirectivo(eadfCarreraBuscar, eadfEvaluacionActiva.getEvPeriodoAcademico().getPracId(), RolConstantes.ROL_DIRCARRERA_VALUE);
			Iterator itera = eadfListaDocentesCarrera.iterator();
			while(itera.hasNext()){
				PersonaDatosDto cad = (PersonaDatosDto) itera.next();
				if(cad.getPrsId() == eadfDocente.getPrsId()){
					itera.remove();
				}
			}
		} catch (PersonaDatosDtoNoEncontradoException e) {
			eadfListaDocentesCarrera = null;
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
			
			eadfEvaluacionActiva = servEadfEvaluacionServicio.buscarActivoXTipo(TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE);
			if(validarCronograma()){ 
				eadfDocente = servEadfPersonaDatosServicioJdbc.buscarPorId(eadfUsuario.getUsrPersona().getPrsId(), eadfEvaluacionActiva.getEvPeriodoAcademico().getPracId());
				cargarDocentesCarrera();
			}
			
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.verificar.acceso.evaluacion.no.activa.validacion.exception")));
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}    
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
			for(Contenido cnt: eadfListaSeleccionContenido){
				if(cnt.getCntSeleccion() == null){
					if(sinRespuesta == null){
						sinRespuesta = count;
					}
					verificar = false;
				}
				count = count+1;
			}
			if(!verificar){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.verificar.guardar.sin.respuesta",sinRespuesta)));
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

		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		Boolean verificar = false;
		
		if(eadfEvaluacionActiva != null){
			if(eadfEvaluacionActiva.getEvaCronogramaFin().after(fechaActual)){
				if(eadfEvaluacionActiva.getEvaCronogramaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.validar.cronograma.expirado.validacion.exception")));
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
			lista.add(TipoContenidoConstantes.VALORACION_NUNCA_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_A_VECES_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_CASI_SIEMPRE_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_SIEMPRE_VALUE);
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

		for(FuncionTipoEvaluacion it: eadfListaFuncionTipoEvaluacion){
			for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){
				if(ite.getTpcnfntpevFuncionTipoEvaluacion().getFnctpevId() == idFnctpev){
					for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){
						if( item.getTpcnId() == idTpcn){
							for(int i = 0; i<eadfListaTipoContenido.size(); i++){
								if(eadfListaTipoContenido.get(i).getTpcnId() == idTpcn){
									eadfListaSeleccionContenido.get(i).setCntSeleccion(eadfRadioSeleccion[i]);
								}
							}							
						}
					}
				}
			}
		} 
	}
	
	/**
	 * Verifica la existencia de una evaluacion.
	 * @param evaluacion - evaluacion a verificar  
	 * @param persona - persona a verificar
	 * @param mensaje - 0 muestra mensaje / 1 no muestra mensaje
	 **/
	public Boolean verificarEvaluacion(Evaluacion evaluacion, PersonaDatosDto persona, int mensaje){
		Boolean verificar = false;
		try {
			UsuarioRol usroEvaluado = servEadfUsuarioRolServicio.buscarXPersonaXrol(persona.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			List<ContenidoEvaluacionDto> contenido = servEadfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), GeneralesConstantes.APP_ID_BASE, eadfUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), eadfCarreraBuscar);
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
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionAlDirectivo.verificar.evaluacion.existente")));
		}
		return verificar;
	}
	
	/**
	 * Genera el reporte del directivo
	 * @param docente - docente a incluir la informacion
	 **/
	public void generarReporteDirectivo(PersonaDatosDto docente){ 
		
		try {
			
			UsuarioRol usroEvaluado = servEadfUsuarioRolServicio.buscarXPersonaXrol(docente.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			Carrera carrera = servEadfCarreraServicio.buscarPorId(eadfCarreraBuscar);
			List<ContenidoEvaluacionDto> contenido = servEadfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(eadfEvaluacionActiva.getEvTipoEvaluacion().getTpevId(), eadfEvaluacionActiva.getEvaId(), GeneralesConstantes.APP_ID_BASE, eadfUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), eadfCarreraBuscar);
			ReporteEvaluacionDocenteForm.generarReporteDirectivo(contenido, docente, eadfEvaluacionActiva, carrera);
			eadfActivarReporte = 1;
			
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

	public Usuario getEadfUsuario() {
		return eadfUsuario;
	}

	public void setEadfUsuario(Usuario eadfUsuario) {
		this.eadfUsuario = eadfUsuario;
	}

	public PersonaDatosDto getEadfDocente() {
		return eadfDocente;
	}

	public void setEadfDocente(PersonaDatosDto eadfDocente) {
		this.eadfDocente = eadfDocente;
	} 

	public Integer getEadfCarreraBuscar() {
		return eadfCarreraBuscar;
	}

	public void setEadfCarreraBuscar(Integer eadfCarreraBuscar) {
		this.eadfCarreraBuscar = eadfCarreraBuscar;
	} 
 
	public String getEadfLinkReporte() {
		return eadfLinkReporte;
	}

	public void setEadfLinkReporte(String eadfLinkReporte) {
		this.eadfLinkReporte = eadfLinkReporte;
	}

	public List<Carrera> getEadfListaCarrerasDocente() {
		return eadfListaCarrerasDocente;
	}

	public void setEadfListaCarrerasDocente(List<Carrera> eadfListaCarrerasDocente) {
		this.eadfListaCarrerasDocente = eadfListaCarrerasDocente;
	}
 
	public List<PersonaDatosDto> getEadfListaDocentesCarrera() {
		return eadfListaDocentesCarrera;
	}

	public void setEadfListaDocentesCarrera(List<PersonaDatosDto> eadfListaDocentesCarrera) {
		this.eadfListaDocentesCarrera = eadfListaDocentesCarrera;
	}

	public List<FuncionTipoEvaluacion> getEadfListaFuncionTipoEvaluacion() {
		return eadfListaFuncionTipoEvaluacion;
	}

	public void setEadfListaFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> eadfListaFuncionTipoEvaluacion) {
		this.eadfListaFuncionTipoEvaluacion = eadfListaFuncionTipoEvaluacion;
	}

	public Evaluacion getEadfEvaluacionActiva() {
		return eadfEvaluacionActiva;
	}

	public void setEadfEvaluacionActiva(Evaluacion eadfEvaluacionActiva) {
		this.eadfEvaluacionActiva = eadfEvaluacionActiva;
	}

	public Integer[] getEadfRadioSeleccion() {
		return eadfRadioSeleccion;
	}

	public void setEadfRadioSeleccion(Integer[] eadfRadioSeleccion) {
		this.eadfRadioSeleccion = eadfRadioSeleccion;
	}

	public List<Contenido> getEadfListaSeleccionContenido() {
		return eadfListaSeleccionContenido;
	}

	public void setEadfListaSeleccionContenido(List<Contenido> eadfListaSeleccionContenido) {
		this.eadfListaSeleccionContenido = eadfListaSeleccionContenido;
	}

	public List<TipoContenido> getEadfListaTipoContenido() {
		return eadfListaTipoContenido;
	}

	public void setEadfListaTipoContenido(List<TipoContenido> eadfListaTipoContenido) {
		this.eadfListaTipoContenido = eadfListaTipoContenido;
	}

	public PersonaDatosDto getEadfPersonaDtoSeleccion() {
		return eadfPersonaDtoSeleccion;
	}

	public void setEadfPersonaDtoSeleccion(PersonaDatosDto eadfPersonaDtoSeleccion) {
		this.eadfPersonaDtoSeleccion = eadfPersonaDtoSeleccion;
	}

	public Integer getEadfActivarReporte() {
		return eadfActivarReporte;
	}

	public void setEadfActivarReporte(Integer eadfActivarReporte) {
		this.eadfActivarReporte = eadfActivarReporte;
	}

}