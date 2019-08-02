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

 ARCHIVO:     AutoevaluacionForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Directores de Carrera. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
15-ENERO-2018		 Arturo Villafuerte 			       Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorValidacionException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AsignacionEvaluadorServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ContenidoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluadorEvaluadoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FuncionTipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.AsignacionEvaluador;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.EvaluadorEvaluado;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) AutoevaluacionForm.java Bean de sesión que maneja
 * los atributos del formulario de Autoevaluacion.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "autoevaluacionForm")
@SessionScoped
public class AutoevaluacionForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario afUsuario;
	private PersonaDatosDto afDocente; 

	private Evaluacion afEvaluacionSelecion;
	private List<Evaluacion> afListaEvaluaciones;

	private List<DocenteJdbcDto> afListaCarrerasDocente; 

	private List<FuncionTipoEvaluacion> afListaFuncionTipoEvaluacion;

	private Integer[] afRadioSeleccion;
	private List<Contenido> afListaSeleccionContenido;
	private List<TipoContenido> afListaTipoContenido;
	
	private UsuarioRol afUsroDocenteActual; 
	
	//URL reporte
	private String afLinkReporte;
	private Integer afActivarReporte;

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
	PersonaDatosDtoServicioJdbc servAfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servAfPersonaServicio; 
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servAfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servAfUsuarioRolServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servAfDocenteDatosDtoServicioJdbc;
	@EJB
	DocenteDtoServicioJdbc servAfDocenteDtoServicioJdbc;
	@EJB
	EvaluacionServicio servAfEvaluacionServicio;
	@EJB
	FuncionTipoEvaluacionServicio servAfFuncionTipoEvaluacionServicio;
	@EJB
	TipoContenidoServicio servAfTipoContenidoServicio;
	@EJB
	ContenidoServicio servAfContenidoServicio;
	@EJB
	EvaluadorEvaluadoServicio servAfEvaluadorEvaluadoServicio; 
	@EJB
	AsignacionEvaluadorServicio servAfAsignacionEvaluadorServicio;
	@EJB
	TpcnFuncionTpevServicio servAfTpcnFuncionTpevServicio;
	@EJB
	ContenidoEvaluacionDtoServicioJdbc servAfContenidoEvaluacionDtoJdbcServicio;

	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/

	// >>-----------------------------------INICIALIZACION_DE_PARAMETROS-------------------------------------

	/**
	 * Inicia los parametros de la funcionalidad
	 */
	private void inicarParametros() { 

		if(verificarDocenteClases()){ 
			verificarAcceso();
		}
 
	}

	// >>--------------------------------------------NAVEGACION----------------------------------------------

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irAutoevaluacionDocente(Usuario usuario) {
		try {
			afUsuario = usuario;
			afUsroDocenteActual = servAfUsuarioRolServicio.buscarXUsuarioXrol(afUsuario.getUsrId(), RolConstantes.ROL_DOCENTE_VALUE);
			afActivarReporte = 0;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		inicarParametros(); 
		return "irAutoevaluacionDocente";
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
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return Navegacion a la pagina de inicio.
	 */
	@SuppressWarnings("rawtypes")
	public String irEvaluarAutoevaluacion(Evaluacion evaluacion) {
		
		
		afEvaluacionSelecion = evaluacion;
		
		if(validarCronograma()){
			if(verificarEvaluacion(evaluacion, 0)){
				
				try {

					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

					afListaTipoContenido = new ArrayList<>();
					afListaSeleccionContenido = new ArrayList<>();

					afListaFuncionTipoEvaluacion = servAfFuncionTipoEvaluacionServicio.listarActivoXTipoEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId());

					for(FuncionTipoEvaluacion it: afListaFuncionTipoEvaluacion){

						Boolean verificar = false;
						
						Iterator iter = it.getFntpevListTpcnFuncionTpev().iterator();
						while(iter.hasNext()){
							verificar = false;
							TpcnFuncionTpev cad = (TpcnFuncionTpev) iter.next();

							if(cad.getTpcnfntpevTipoContenido().getTpcnTipo().intValue() == TipoContenidoConstantes.TIPO_PREGUNTA_VALUE){
								iter.remove();
								verificar = true;
							}

							if(!verificar && cad.getTpcnfntpevTipoContenido().getTpcnEstado().intValue() == TipoContenidoConstantes.ESTADO_INACTIVO_VALUE){
								iter.remove();
								verificar = true;
							}

							if(!verificar && cad.getTpcnfntpevEstado().intValue() == TipoContenidoConstantes.ESTADO_INACTIVO_VALUE){
								iter.remove();
								verificar = true;
							}
						}
						
						
						Collections.sort(it.getFntpevListTpcnFuncionTpev());
 
						for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){
							
							Iterator itera = ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido().iterator();
							while(itera.hasNext()){
								TipoContenido cad = (TipoContenido) itera.next();
								if(cad.getTpcnEstado().intValue() == 1){
									itera.remove();
								} 
							}
							 
							Collections.sort(ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido());
							
							for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){

								afListaTipoContenido.add(item);

								Contenido cnt = new Contenido();
								cnt.setCntEvaluacion(afEvaluacionSelecion);
								cnt.setCntTpcnFuncionTpev(servAfTpcnFuncionTpevServicio.buscarXTipoContenido(item.getTpcnId()));
								cnt.setCntFecha(fechaActual);
								cnt.setCntUsuario(afUsuario.getUsrNick());
								cnt.setCntDescripcion(item.getTpcnDescripcion()); 

								afListaSeleccionContenido.add(cnt);
							}
						}
					} 

					afRadioSeleccion = new Integer[afListaTipoContenido.size()]; 

				} catch (FuncionTipoEvaluacionNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (FuncionTipoEvaluacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (TpcnFuncionTpevNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());  
				} catch (TpcnFuncionTpevException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				} 
			}
			
		}
		
		return "irEvaluarAutoevaluacion";
	}

	/**
	 * limpia y regresa a la ventana anterior
	 * @return Navegacion a la pagina anterior.
	 */
	public String irRegresar() {
		limpiarEvaluar();
		inicarParametros();
		return "irAutoevaluacionDocente";
	}


	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {

		afDocente = null;   
		afLinkReporte = null;
		afUsuario = null; 
		afEvaluacionSelecion = null;
		afListaEvaluaciones = null;
		afListaCarrerasDocente = null; 
		afListaFuncionTipoEvaluacion = null;
		afRadioSeleccion = null;
		afListaSeleccionContenido = null;
		afListaTipoContenido = null;

	}

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiarEvaluar() {

		afListaFuncionTipoEvaluacion = null;
		afRadioSeleccion = null;
		afListaSeleccionContenido = null;
		afListaTipoContenido = null;

	}

	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------



	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------

	/**
	 * Guarda los parametros del formulario
	 **/
	public String guardar() {

		String direccion = null;
		Boolean verificar = true;
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		
		if(validarCronograma()){
			if(verificarEvaluacion(afEvaluacionSelecion, 0)){
				
				try{

					if(verificarGuardar()){

						EvaluadorEvaluado evalrEval = new EvaluadorEvaluado();
						evalrEval.setEvevEvaluador(afUsroDocenteActual.getUsroId());
						evalrEval.setEvevUsuarioRol(afUsroDocenteActual);
						evalrEval = servAfEvaluadorEvaluadoServicio.anadir(evalrEval);

						AsignacionEvaluador asigEval= new AsignacionEvaluador();
						asigEval.setAsevEvaluadorEvaluado(evalrEval);
						asigEval.setAsevEstado(0);
						asigEval.setAsevUsuario(afUsuario.getUsrNick());
						asigEval.setAsevFecha(fechaActual);
						asigEval = servAfAsignacionEvaluadorServicio.anadir(asigEval);

						for(Contenido item: afListaSeleccionContenido){
							item.setCntAsignacionEvaluador(asigEval);
							Contenido contenido = servAfContenidoServicio.anadir(item);
							if(contenido == null){
								verificar = false;
							}
						}

						if(verificar){
							direccion = irRegresar();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.guardar.exito")));
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.guardar.no.exito")));
						}
					}
				} catch (ContenidoValidacionException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				} catch (ContenidoException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				} catch (AsignacionEvaluadorValidacionException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				} catch (AsignacionEvaluadorException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				} catch (EvaluadorEvaluadoValidacionException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				} catch (EvaluadorEvaluadoException e) {
					FacesUtil.mensajeError(e.getMessage()); 
				}
			}
		}

		return direccion;
	}

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/

	// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------



	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
  
	/**
	 * Verifica el acceso del docente a la autoevaluacion
	 * otorgando un estado true / false para activar la evaluacion
	 */
	public void verificarAcceso(){

		try{
			afListaEvaluaciones = servAfEvaluacionServicio.listarTodosXTipoActivo(TipoEvaluacionConstantes.AUTOEVALUACION_VALUE); 
		}  catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionException e) {
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
			for(Contenido cnt: afListaSeleccionContenido){
				if(cnt.getCntSeleccion() == null){
					if(sinRespuesta == null){
						sinRespuesta = count;
					}
					verificar = false;
				}
				count = count+1;
			}
			if(!verificar){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.verificar.guardar.sin.respuesta",sinRespuesta)));
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
		
		if(afEvaluacionSelecion != null){
			if(afEvaluacionSelecion.getEvaCronogramaFin().after(fechaActual)){
				if(afEvaluacionSelecion.getEvaCronogramaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.validar.cronograma.expirado.validacion.exception")));
			}
		}
		return verificar;
	}

	/**
	 * Verifica si el docenes da clases actualmente
	 */
	public Boolean verificarDocenteClases(){
		Boolean verificar = false;
		try {
			verificarAcceso();
			afListaCarrerasDocente = servAfDocenteDtoServicioJdbc.buscarCarrerasXIdentificacionXPeriodoActivo(afUsuario.getUsrPersona().getPrsIdentificacion(),afListaEvaluaciones.get(0).getEvPeriodoAcademico().getPracId());
			if (afListaCarrerasDocente != null && afListaCarrerasDocente.size() > 0) {
				afDocente = servAfPersonaDatosServicioJdbc.buscarPorId(afUsuario.getUsrPersona().getPrsId(), afListaEvaluaciones.get(0).getEvPeriodoAcademico().getPracId());
				verificar = true;
			}
			

		} catch (DetallePuestoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.verificar.docente.clases.sin.carga"))+ " " + afListaEvaluaciones.get(0).getEvPeriodoAcademico().getPracDescripcion());
			afListaEvaluaciones = null;
		} catch (PersonaDatosDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDatosDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return verificar;
	}

	/**
	 * Transforma el id de estado a descripcion
	 **/
	public String mostrarEstado(Integer estado){
		if(estado == 0){
			return EvaluacionConstantes.ESTADO_ACTIVO_LABEL;
		}else{
			return EvaluacionConstantes.ESTADO_INACTIVO_LABEL;
		} 
	}

	/**
	 * Transforma el id de estado a descripcion
	 **/
	public List<Integer> calcularComponente(Integer num, Integer tipoSeleccion){
		List<Integer> lista = new ArrayList<>();
		
		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_UNICO_VALUE){
			lista.add(TipoContenidoConstantes.VALORACION_SI_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_NO_VALUE);
		}
		
		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_VALUE){
			lista.add(TipoContenidoConstantes.VALORACION_SIEMPRE_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_CASI_SIEMPRE_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_A_VECES_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_NUNCA_VALUE);
		}
		
		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_TRES_VALUE){
			lista.add(TipoContenidoConstantes.VALORACION_SI_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_NO_VALUE);
			lista.add(TipoContenidoConstantes.VALORACION_NO_APLICA_VALUE);
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
		}
		
		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_VALUE){

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
		
		if(tipoSeleccion == TipoContenidoConstantes.TIPO_SELECCION_MULTIPLE_TRES_VALUE){
			switch (num) {
			case 0:
				lista = TipoContenidoConstantes.VALORACION_SI_LABEL;
				break;
			case 1:
				lista = TipoContenidoConstantes.VALORACION_NO_LABEL;
				break;
			case 3:
				lista = TipoContenidoConstantes.VALORACION_NO_APLICA_LABEL;
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

		for(FuncionTipoEvaluacion it: afListaFuncionTipoEvaluacion){
			for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){
				if(ite.getTpcnfntpevFuncionTipoEvaluacion().getFnctpevId() == idFnctpev){
					for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){
						if( item.getTpcnId() == idTpcn){
							for(int i = 0; i<afListaTipoContenido.size(); i++){
								if(afListaTipoContenido.get(i).getTpcnId() == idTpcn){
									afListaSeleccionContenido.get(i).setCntSeleccion(afRadioSeleccion[i]);
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
	 **/
	public Boolean verificarEvaluacion(Evaluacion evaluacion, int mensaje){
		Boolean verificar = false;
		List<ContenidoEvaluacionDto> contenido = null;
		
		try {
			contenido = servAfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), GeneralesConstantes.APP_ID_BASE, afUsroDocenteActual.getUsroId(), afUsroDocenteActual.getUsroId(), GeneralesConstantes.APP_ID_BASE);
			if(contenido == null || contenido.size() == 0){
				verificar = true;
			}
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			verificar = true;
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		}
		
		if(!verificar && mensaje == 0){
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoevaluacion.verificar.evaluacion.existente")));
		}
		
		return verificar;
	}
	
	
	public void generarReporteAutoevaluacion(Evaluacion evaluacion){ 
		
		try {
			List<ContenidoEvaluacionDto> contenido = servAfContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), GeneralesConstantes.APP_ID_BASE, afUsroDocenteActual.getUsroId(), afUsroDocenteActual.getUsroId(), GeneralesConstantes.APP_ID_BASE);
			ReporteEvaluacionDocenteForm.generarReporteAutoevaluacion(contenido, afDocente, evaluacion);
			afActivarReporte = 1;
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		}
		
	}
 
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getAfUsuario() {
		return afUsuario;
	}

	public void setAfUsuario(Usuario afUsuario) {
		this.afUsuario = afUsuario;
	}

	public PersonaDatosDto getAfDocente() {
		return afDocente;
	}

	public void setAfDocente(PersonaDatosDto afDocente) {
		this.afDocente = afDocente;
	}

	public String getAfLinkReporte() {
		return afLinkReporte;
	}

	public void setAfLinkReporte(String afLinkReporte) {
		this.afLinkReporte = afLinkReporte;
	}

	public List<DocenteJdbcDto> getAfListaCarrerasDocente() {
		return afListaCarrerasDocente;
	}

	public void setAfListaCarrerasDocente(List<DocenteJdbcDto> afListaCarrerasDocente) {
		this.afListaCarrerasDocente = afListaCarrerasDocente;
	}

	public List<Evaluacion> getAfListaEvaluaciones() {
		return afListaEvaluaciones;
	}

	public void setAfListaEvaluaciones(List<Evaluacion> afListaEvaluaciones) {
		this.afListaEvaluaciones = afListaEvaluaciones;
	}

	public List<FuncionTipoEvaluacion> getAfListaFuncionTipoEvaluacion() {
		return afListaFuncionTipoEvaluacion;
	}

	public void setAfListaFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> afListaFuncionTipoEvaluacion) {
		this.afListaFuncionTipoEvaluacion = afListaFuncionTipoEvaluacion;
	}

	public Evaluacion getAfEvaluacionSelecion() {
		return afEvaluacionSelecion;
	}

	public void setAfEvaluacionSelecion(Evaluacion afEvaluacionSelecion) {
		this.afEvaluacionSelecion = afEvaluacionSelecion;
	}

	public List<TipoContenido> getAfListaTipoContenido() {
		return afListaTipoContenido;
	}

	public void setAfListaTipoContenido(List<TipoContenido> afListaTipoContenido) {
		this.afListaTipoContenido = afListaTipoContenido;
	}

	public Integer[] getAfRadioSeleccion() {
		return afRadioSeleccion;
	}

	public void setAfRadioSeleccion(Integer[] afRadioSeleccion) {
		this.afRadioSeleccion = afRadioSeleccion;
	}

	public Integer getAfActivarReporte() {
		return afActivarReporte;
	}

	public void setAfActivarReporte(Integer afActivarReporte) {
		this.afActivarReporte = afActivarReporte;
	}

	
}