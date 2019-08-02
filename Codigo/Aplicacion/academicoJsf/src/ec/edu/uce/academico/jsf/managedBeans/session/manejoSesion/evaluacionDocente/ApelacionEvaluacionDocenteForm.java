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
24-AGOSTO-2017		 Arturo Villafuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionDocente;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AsignacionEvaluadorServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ContenidoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluadorEvaluadoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.FuncionTipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TpcnFuncionTpevServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.EvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;
import ec.edu.uce.academico.jpa.entidades.publico.TipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionDirectivoForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del Directivo.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "apelacionEvaluacionDocenteForm")
@SessionScoped
public class ApelacionEvaluacionDocenteForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario edfUsuario;
	private PersonaDatosDto edafPersonaEvaluador;
	private PersonaDatosDto edafPersonaEvaluado;

	private Integer edfPeriodoAcademicoBuscar;
	private PeriodoAcademico edfPeriodoAcademico;
	private List<PeriodoAcademico> edfListaPeriodosAcademicos;
	private List<PeriodoAcademico> edfListaTodosPeriodosAcademicos;

	private Evaluacion edfEvaluacionBuscar;
	private List<Evaluacion> edfListaEvaluaciones;
	
	private Contenido edafContenido;
	private Evaluacion edfEvaluacionActiva; 
	private List<ContenidoEvaluacionDto> edafListaContenido;
	private ContenidoEvaluacionDto edafContenidoDto;
	private List<Contenido> edfListaSeleccionContenido;
	private List<ContenidoEvaluacionDto> edfListaSeleccionContenidoAux;
	private List<TipoContenido> edfListaTipoContenido;
	private List<FuncionTipoEvaluacion> edfListaFuncionTipoEvaluacion;

	private String edfDescripcionEvaluacion;
	private Date edfFechaInicioEvaluacion;
	private Date edfFechaFinEvaluacion;
	private Integer edfEstadoEvaluacion;

	private Integer edfTipoEvaluacionBuscar;
	private Evaluacion edfTipoEvaluacion;
	private List<TipoEvaluacion> edfListaTipoEvaluaciones;
	
	private UsuarioRol edfUsroEvaluador;
	private List<UsuarioRolJdbcDto>edafListUsroDtoEvaluador;
	private UsuarioRolJdbcDto edafUsuarioDtoEvaluador;
	private List<UsuarioRolJdbcDto>edafListUsroDtoEvaluado;
	private UsuarioRolJdbcDto edafUsuarioDtoEvaluado;
	private UsuarioRol edfUsroEvaluado;
	private Integer edfCarreraBuscar; 
	private List<Carrera> edfListaCarrerasDocente; 
	private Integer[] edfRadioSeleccion;
 
	//validaciones modal
	private Integer edafVerificadorActivaModal;
	private Integer edafVerificarActivarModalEvaluador;
	private Integer edafVerificarActivarModalEvaluado;
	private List<Integer> edafListaRadio;
	private String edafEtiquetaRadio;	
	
	private Integer seleccionAux;

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
	CarreraServicio servEdfCarreraServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servEdfPersonaDatosServicioJdbc;
	@EJB
	PersonaServicio servEdfPersonaServicio;
	@EJB
	PeriodoAcademicoServicio servEdfPeriodoAcademicoServicio;
	@EJB
	PlanificacionCronogramaDtoServicioJdbc servEdfPlanificacionCronogramaDtoServicioJdbc;
	@EJB
	UsuarioRolServicio servEdfUsuarioRolServicio;
	@EJB
	RolFlujoCarreraServicio servEdfRolFlujoCarreraServicio;
	@EJB
	DocenteDatosDtoServicioJdbc servEdfDocenteDatosDtoServicioJdbc;
	@EJB
	DocenteDtoServicioJdbc servEdfDocenteDtoServicioJdbc;
	@EJB
	EvaluacionServicio servEdfEvaluacionServicio;
	@EJB
	TipoEvaluacionServicio servEdfTipoEvaluacionServicio;
	@EJB ContenidoEvaluacionDtoServicioJdbc servEdfContenidoEvaluacionDtoJdbcServicio;
	@EJB FuncionTipoEvaluacionServicio servEdfFuncionTipoEvaluacionServicio;
	@EJB TpcnFuncionTpevServicio servEdfTpcnFuncionTpevServicio;
	@EJB EvaluadorEvaluadoServicio servEdfEvaluadorEvaluadoServicio; 
	 @EJB AsignacionEvaluadorServicio servEdfAsignacionEvaluadorServicio;
	 @EJB ContenidoServicio servEdfContenidoServicio;
	 @EJB UsuarioRolDtoServicioJdbc servEdafUsuarioRolDtoServicio;

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

	/**
	 * Incicializa las variables para el inicio de la funcionalidad
	 * 
	 * @return navegacion hacia el formulario de proyectos semilla.
	 */
	public String irApelacionEvaluacionDocente(Usuario usuario) {
		
		Boolean verificar = false;
		
		try {
			
			
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
		
		return "irApelacionEvaluacionDocente";
	}
	// >>--------------------------------------------NAVEGACION----------------------------------------------


	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la
	 * pagina de inicio
	 * 
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		
		edfUsuario = null; 

		edfPeriodoAcademicoBuscar = null;
		edfPeriodoAcademico = null;
		edfListaPeriodosAcademicos = null;

		edfEvaluacionBuscar = null;
		edfListaEvaluaciones = null;

		edfDescripcionEvaluacion = null;
		edfFechaInicioEvaluacion = null;
		edfFechaFinEvaluacion = null;
		edfEstadoEvaluacion = null;

		edfTipoEvaluacionBuscar = null;
		edfTipoEvaluacion = null;
		edfListaTipoEvaluaciones = null;
		edafListaContenido=null;
		

		edfListaTodosPeriodosAcademicos = null;
		
		return "irInicio";
	}


	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		
		inicarParametros();
	}

	
	/**
	 * Limpia los parametros de edicion/nuevo
	 */
	public void limpiarEdicion(){
		edfPeriodoAcademico = null;
		edfPeriodoAcademicoBuscar = null;
		edfDescripcionEvaluacion = null;
		edfEstadoEvaluacion = null;
		edfFechaInicioEvaluacion = null;
		edfFechaFinEvaluacion = null;
		edfTipoEvaluacionBuscar = null;
		edfTipoEvaluacion = null;
		edfListaTipoEvaluaciones = null;
		edfEvaluacionBuscar = null;
	}

	public void limpiarEvaluador(){
		edafListUsroDtoEvaluador=null;
		edafPersonaEvaluador=new PersonaDatosDto();
		
	}
	
	public void limpiarEvaluado(){
		edafListUsroDtoEvaluado=null;
		edafPersonaEvaluado=new PersonaDatosDto();
	}

	/**
	 * Método para activar el modal de carga de causal y solicitud
	 * @param materia - materia que se selecciona de la lista para cargar causal y la solicitud
	 */
	public void activaModalRegistrarApelacion(ContenidoEvaluacionDto contenido) {
		
		edafVerificadorActivaModal = 1;	
		edafContenidoDto=contenido;
		seleccionAux=contenido.getCntSeleccion();
	
	}
	
	/**
	 * Método para activar el modal de carga de causal y solicitud
	 * @param materia - materia que se selecciona de la lista para cargar causal y la solicitud
	 */
	public void activaModalEvaluador() {
		
		edafVerificarActivarModalEvaluador = 1;
		limpiarEvaluador();
		
		
	}
	
	public void activarModalEvaluado() {
		edafVerificarActivarModalEvaluado = 1;
		limpiarEvaluado();
		
	}
	
	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	public void buscarRolesEvaluador() {
		try {
			
				if (!edafPersonaEvaluador.getPrsIdEvaluador().isEmpty()) {
					edafListUsroDtoEvaluador = servEdafUsuarioRolDtoServicio.buscarRolesEvaluacionDocenteXIdentificacion(edafPersonaEvaluador.getPrsIdEvaluador(),edfTipoEvaluacionBuscar);
				}
			
			
		} catch (Exception e) {

		}
	}

	public void buscarRolesEvaluado() {
		try {
			
				if (!edafPersonaEvaluado.getPrsIdentificacion().isEmpty()) {
					edafListUsroDtoEvaluado = servEdafUsuarioRolDtoServicio.buscarRolesEvaluacionDocenteEvaluadoXIdentificacion(edafPersonaEvaluado.getPrsIdentificacion(),edafUsuarioDtoEvaluador.getRolId());
				}
			
			
		} catch (Exception e) {

		}
	}
	/**
	 * Metodo para asignar el evaluador
	 * @param evaluador
	 */
	public void seleccionarEvaluador(UsuarioRolJdbcDto evaluador){
		edafUsuarioDtoEvaluador=evaluador;
		edafPersonaEvaluador.setPrsNomApEvaluador(evaluador.nombresCompletos());
		edafVerificarActivarModalEvaluador=0;
	}
	/**
	 * Metodo para seleccionar el evaluado
	 * @param evaluador
	 */
	public void seleccionarEvaluado(UsuarioRolJdbcDto evaluado){
		edafUsuarioDtoEvaluado=evaluado;
		edafPersonaEvaluado.setPrsNomApEvaluado(evaluado.nombresCompletos());
		edafVerificarActivarModalEvaluado=0;
	}
	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		try {
			edafVerificadorActivaModal=0;
			edfEvaluacionActiva = servEdfEvaluacionServicio.buscarActivoXTipo(edfTipoEvaluacionBuscar);
			if (validarCronograma()) {
				if (edfPeriodoAcademicoBuscar != 0 && edfTipoEvaluacionBuscar != 0
						&& edafUsuarioDtoEvaluador.getUsroId()!=null
						&& edafUsuarioDtoEvaluado.getUsroId()!=null ) {
					edafListaContenido = servEdfContenidoEvaluacionDtoJdbcServicio
							.listarXPeriodoXTipoEvaluacionXEvaluadorXEvaluado(edfPeriodoAcademicoBuscar,
									edfTipoEvaluacionBuscar,edafUsuarioDtoEvaluador.getUsroId(), edafUsuarioDtoEvaluado.getUsroId(),edfUsuario.getUsrId());
				cargarSeleccionInicial();

				edfRadioSeleccion = new Integer[edafListaContenido.size()];

				for (ContenidoEvaluacionDto item : edafListaContenido) {
					System.out.println(item.getTpcnNumeral().toString());
					System.out.println("indicador" + item.getTpcnDescripcion().toString());
					System.out.println("valoracion"
							+ calcularComponenteEtiqueta(item.getCntSeleccion(), item.getTpcnTipoSeleccion()));
					System.out
							.println("valor" + calcularComponente(item.getCntSeleccion(), item.getTpcnTipoSeleccion()));
				}

				} else {
					FacesUtil.mensajeError("Debe ingresar todos los filtros de busqueda");
				}
			}

		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

	}

	public void cargarSeleccionInicial(){

		for (ContenidoEvaluacionDto val : edafListaContenido) {			
			val.setCntSeleccionAux(calcularComponenteEtiqueta(val.getCntSeleccion(), val.getTpcnTipoSeleccion()) );
			
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
	public void agregarSeleccion() {
		
			
			edafContenidoDto.setCntSeleccion(edafContenidoDto.getCntSeleccionApelacion());
		

	}

	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------
	
	
	/**
	 * Método para editar una partida
	 * @return navagación al xhtml listarPartida
	 * @throws DetallePuestoException 
	 * @throws DetallePuestoNoEncontradoException 
	 */
	public void guardarApelacion()   
	{
			try {
				
				if(!edafContenidoDto.getCntOficioApelacion().isEmpty()){
				servEdfContenidoServicio.anadirApelacion(edafContenidoDto,edfUsuario,seleccionAux);	
				FacesUtil.mensajeInfo("Registro de apelación ingresado Exitosamente");
				}
				else{
					FacesUtil.mensajeError("Debe ingresar información para poder generar la apelación");
				}
			} catch (ContenidoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
			
			cancelar();
			
		
	}
	
		
	/**
	 * Valida el cronograa para uso de la carga horaria
	 **/
	public Boolean validarCronograma(){
		Boolean verificar = false;
		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		if(edfEvaluacionActiva != null){
			if(edfEvaluacionActiva.getEvaEstado().equals(EvaluacionConstantes.ESTADO_ACTIVO_VALUE)){
				if(edfEvaluacionActiva.getEvaCronogramaFin().after(fechaActual) || edfEvaluacionActiva.getEvaCronogramaFin().equals(fechaActual)){
					verificar = true;
				}else{
					FacesUtil.mensajeError("Es necesario que la Evaluación que quiere realizar la Apelación se encuentre Activa.");
				}
				
			}else{
				FacesUtil.mensajeError("Es necesario que la Evaluación que quiere realizar la Apelación se encuentre Activa.");
			}
		}
		
		return verificar;
	}
	

	// ****************************************************************/
	// *********************** METODOS AUXILIARES *********************/
	// ****************************************************************/



	// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------

	/**
	 * Verifica el acceso del docente a la autoevaluacion
	 * otorgando un estado true / false para activar la evaluacion
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void verificarAcceso(){

		try { 
			
			edafVerificadorActivaModal=0;
			edafVerificarActivarModalEvaluador=0;
			edafVerificarActivarModalEvaluado=0;
			seleccionAux=0;
			edafListaContenido=null;
			edafListUsroDtoEvaluador=null;
			edafUsuarioDtoEvaluador=new UsuarioRolJdbcDto();
			edafListUsroDtoEvaluado=null;
			edafUsuarioDtoEvaluado=new UsuarioRolJdbcDto();
			edafPersonaEvaluador=new PersonaDatosDto();
			edafPersonaEvaluado =new PersonaDatosDto();
			edafContenidoDto=new ContenidoEvaluacionDto();
			edafContenidoDto.setTpcnNumMax(1);
			edafContenidoDto.setTpcnTipoSeleccion(1);
			edfEvaluacionActiva=null;
			edfListaEvaluaciones = servEdfEvaluacionServicio.listarActivo();
			edfListaPeriodosAcademicos = new ArrayList<>();
			for(Evaluacion item: edfListaEvaluaciones){
				edfListaPeriodosAcademicos.add(item.getEvPeriodoAcademico());
			}  
			
			HashSet hs = new HashSet();   
		    hs.addAll(edfListaPeriodosAcademicos);	     
		    edfListaPeriodosAcademicos.clear();
		    edfListaPeriodosAcademicos.addAll(hs);
		 
			edfListaTipoEvaluaciones = servEdfTipoEvaluacionServicio.listarXApelacion(); 
			
			
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoEvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (TipoEvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

	}

	public void cancelar(){
		edafVerificadorActivaModal=0;
		for (ContenidoEvaluacionDto val : edafListaContenido) {			
			val.setCntSeleccionApelacion(null);
			
		}
		
	}
	
	public void cancelarEvaluador(){
		edafVerificarActivarModalEvaluador=0;
		limpiarEvaluador();
	}
	public void cancelarEvaluado(){
		edafVerificarActivarModalEvaluado=0;
		edafListUsroDtoEvaluado=null;
		//limpiarEvaluado();
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

	public Integer getEdfPeriodoAcademicoBuscar() {
		return edfPeriodoAcademicoBuscar;
	}

	public void setEdfPeriodoAcademicoBuscar(Integer edfPeriodoAcademicoBuscar) {
		this.edfPeriodoAcademicoBuscar = edfPeriodoAcademicoBuscar;
	}

	public List<PeriodoAcademico> getEdfListaPeriodosAcademicos() {
		return edfListaPeriodosAcademicos;
	}

	public void setEdfListaPeriodosAcademicos(List<PeriodoAcademico> edfListaPeriodosAcademicos) {
		this.edfListaPeriodosAcademicos = edfListaPeriodosAcademicos;
	} 

	

	public PeriodoAcademico getEdfPeriodoAcademico() {
		return edfPeriodoAcademico;
	}

	public void setEdfPeriodoAcademico(PeriodoAcademico edfPeriodoAcademico) {
		this.edfPeriodoAcademico = edfPeriodoAcademico;
	}

	public List<Evaluacion> getEdfListaEvaluaciones() {
		return edfListaEvaluaciones;
	}

	public void setEdfListaEvaluaciones(List<Evaluacion> edfListaEvaluaciones) {
		this.edfListaEvaluaciones = edfListaEvaluaciones;
	}

	public String getEdfDescripcionEvaluacion() {
		return edfDescripcionEvaluacion;
	}

	public void setEdfDescripcionEvaluacion(String edfDescripcionEvaluacion) {
		this.edfDescripcionEvaluacion = edfDescripcionEvaluacion;
	}

	public Date getEdfFechaInicioEvaluacion() {
		return edfFechaInicioEvaluacion;
	}

	public void setEdfFechaInicioEvaluacion(Date edfFechaInicioEvaluacion) {
		this.edfFechaInicioEvaluacion = edfFechaInicioEvaluacion;
	}

	public Date getEdfFechaFinEvaluacion() {
		return edfFechaFinEvaluacion;
	}

	public void setEdfFechaFinEvaluacion(Date edfFechaFinEvaluacion) {
		this.edfFechaFinEvaluacion = edfFechaFinEvaluacion;
	}

	public Integer getEdfEstadoEvaluacion() {
		return edfEstadoEvaluacion;
	}

	public void setEdfEstadoEvaluacion(Integer edfEstadoEvaluacion) {
		this.edfEstadoEvaluacion = edfEstadoEvaluacion;
	}

	public Evaluacion getEdfEvaluacionBuscar() {
		return edfEvaluacionBuscar;
	}

	public void setEdfEvaluacionBuscar(Evaluacion edfEvaluacionBuscar) {
		this.edfEvaluacionBuscar = edfEvaluacionBuscar;
	}

	public Integer getEdfTipoEvaluacionBuscar() {
		return edfTipoEvaluacionBuscar;
	}

	public void setEdfTipoEvaluacionBuscar(Integer edfTipoEvaluacionBuscar) {
		this.edfTipoEvaluacionBuscar = edfTipoEvaluacionBuscar;
	}

	public Evaluacion getEdfTipoEvaluacion() {
		return edfTipoEvaluacion;
	}

	public void setEdfTipoEvaluacion(Evaluacion edfTipoEvaluacion) {
		this.edfTipoEvaluacion = edfTipoEvaluacion;
	}

	public List<TipoEvaluacion> getEdfListaTipoEvaluaciones() {
		return edfListaTipoEvaluaciones;
	}

	public void setEdfListaTipoEvaluaciones(List<TipoEvaluacion> edfListaTipoEvaluaciones) {
		this.edfListaTipoEvaluaciones = edfListaTipoEvaluaciones;
	}

	public List<PeriodoAcademico> getEdfListaTodosPeriodosAcademicos() {
		return edfListaTodosPeriodosAcademicos;
	}

	public void setEdfListaTodosPeriodosAcademicos(List<PeriodoAcademico> edfListaTodosPeriodosAcademicos) {
		this.edfListaTodosPeriodosAcademicos = edfListaTodosPeriodosAcademicos;
	}

	public Contenido getEdafContenido() {
		return edafContenido;
	}

	public void setEdafContenido(Contenido edafContenido) {
		this.edafContenido = edafContenido;
	}

	public List<ContenidoEvaluacionDto> getEdafListaContenido() {
		return edafListaContenido;
	}

	public void setEdafListaContenido(List<ContenidoEvaluacionDto> edafListaContenido) {
		this.edafListaContenido = edafListaContenido;
	}

	public Evaluacion getEdfEvaluacionActiva() {
		return edfEvaluacionActiva;
	}

	public void setEdfEvaluacionActiva(Evaluacion edfEvaluacionActiva) {
		this.edfEvaluacionActiva = edfEvaluacionActiva;
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

	public List<FuncionTipoEvaluacion> getEdfListaFuncionTipoEvaluacion() {
		return edfListaFuncionTipoEvaluacion;
	}

	public void setEdfListaFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> edfListaFuncionTipoEvaluacion) {
		this.edfListaFuncionTipoEvaluacion = edfListaFuncionTipoEvaluacion;
	}

	public UsuarioRol getEdfUsroEvaluador() {
		return edfUsroEvaluador;
	}

	public void setEdfUsroEvaluador(UsuarioRol edfUsroEvaluador) {
		this.edfUsroEvaluador = edfUsroEvaluador;
	}

	public Integer[] getEdfRadioSeleccion() {
		return edfRadioSeleccion;
	}

	public void setEdfRadioSeleccion(Integer[] edfRadioSeleccion) {
		this.edfRadioSeleccion = edfRadioSeleccion;
	}
	public UsuarioRol getEdfUsroEvaluado() {
		return edfUsroEvaluado;
	}

	public void setEdfUsroEvaluado(UsuarioRol edfUsroEvaluado) {
		this.edfUsroEvaluado = edfUsroEvaluado;
	}

	public List<ContenidoEvaluacionDto> getEdfListaSeleccionContenidoAux() {
		return edfListaSeleccionContenidoAux;
	}

	public void setEdfListaSeleccionContenidoAux(List<ContenidoEvaluacionDto> edfListaSeleccionContenidoAux) {
		this.edfListaSeleccionContenidoAux = edfListaSeleccionContenidoAux;
	}

	public ContenidoEvaluacionDto getEdafContenidoDto() {
		return edafContenidoDto;
	}

	public void setEdafContenidoDto(ContenidoEvaluacionDto edafContenidoDto) {
		this.edafContenidoDto = edafContenidoDto;
	}

	public Integer getEdafVerificadorActivaModal() {
		return edafVerificadorActivaModal;
	}

	public void setEdafVerificadorActivaModal(Integer edafVerificadorActivaModal) {
		this.edafVerificadorActivaModal = edafVerificadorActivaModal;
	}

	public List<Integer> getEdafListaRadio() {
		return edafListaRadio;
	}

	public void setEdafListaRadio(List<Integer> edafListaRadio) {
		this.edafListaRadio = edafListaRadio;
	}

	public String getEdafEtiquetaRadio() {
		return edafEtiquetaRadio;
	}

	public void setEdafEtiquetaRadio(String edafEtiquetaRadio) {
		this.edafEtiquetaRadio = edafEtiquetaRadio;
	}

	public Integer getSeleccionAux() {
		return seleccionAux;
	}

	public void setSeleccionAux(Integer seleccionAux) {
		this.seleccionAux = seleccionAux;
	}

	public Integer getEdafVerificarActivarModalEvaluador() {
		return edafVerificarActivarModalEvaluador;
	}

	public void setEdafVerificarActivarModalEvaluador(Integer edafVerificarActivarModalEvaluador) {
		this.edafVerificarActivarModalEvaluador = edafVerificarActivarModalEvaluador;
	}

	public Integer getEdafVerificarActivarModalEvaluado() {
		return edafVerificarActivarModalEvaluado;
	}

	public void setEdafVerificarActivarModalEvaluado(Integer edafVerificarActivarModalEvaluado) {
		this.edafVerificarActivarModalEvaluado = edafVerificarActivarModalEvaluado;
	}

	public List<UsuarioRolJdbcDto> getEdafListUsroDtoEvaluador() {
		return edafListUsroDtoEvaluador;
	}

	public void setEdafListUsroDtoEvaluador(List<UsuarioRolJdbcDto> edafListUsroDtoEvaluador) {
		this.edafListUsroDtoEvaluador = edafListUsroDtoEvaluador;
	}

	public UsuarioRolJdbcDto getEdafUsuarioDtoEvaluador() {
		return edafUsuarioDtoEvaluador;
	}

	public void setEdafUsuarioDtoEvaluador(UsuarioRolJdbcDto edafUsuarioDtoEvaluador) {
		this.edafUsuarioDtoEvaluador = edafUsuarioDtoEvaluador;
	}

	public List<UsuarioRolJdbcDto> getEdafListUsroDtoEvaluado() {
		return edafListUsroDtoEvaluado;
	}

	public void setEdafListUsroDtoEvaluado(List<UsuarioRolJdbcDto> edafListUsroDtoEvaluado) {
		this.edafListUsroDtoEvaluado = edafListUsroDtoEvaluado;
	}

	public UsuarioRolJdbcDto getEdafUsuarioDtoEvaluado() {
		return edafUsuarioDtoEvaluado;
	}

	public void setEdafUsuarioDtoEvaluado(UsuarioRolJdbcDto edafUsuarioDtoEvaluado) {
		this.edafUsuarioDtoEvaluado = edafUsuarioDtoEvaluado;
	}

	public PersonaDatosDto getEdafPersonaEvaluador() {
		return edafPersonaEvaluador;
	}

	public void setEdafPersonaEvaluador(PersonaDatosDto edafPersonaEvaluador) {
		this.edafPersonaEvaluador = edafPersonaEvaluador;
	}

	public PersonaDatosDto getEdafPersonaEvaluado() {
		return edafPersonaEvaluado;
	}

	public void setEdafPersonaEvaluado(PersonaDatosDto edafPersonaEvaluado) {
		this.edafPersonaEvaluado = edafPersonaEvaluado;
	}


	

}