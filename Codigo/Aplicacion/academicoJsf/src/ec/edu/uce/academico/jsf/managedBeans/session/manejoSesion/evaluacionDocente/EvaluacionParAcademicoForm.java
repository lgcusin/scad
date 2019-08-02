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
   
 ARCHIVO:     EvaluacionParAcademicoForm.java	  
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
import java.util.Collections;
import java.util.Comparator;
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
import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoGrupoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoGrupoNoEncontradoException;
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
import ec.edu.uce.academico.ejb.servicios.interfaces.GrupoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoGrupoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TpcnFuncionTpevServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
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
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoGrupo;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionParAcademicoForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del ParAcademico.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "evaluacionParAcademicoForm")
@SessionScoped
public class EvaluacionParAcademicoForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario epafUsuario; 
	private PersonaDatosDto epafDocente; 

	private Integer epafAreaBuscar;  
  
	private Evaluacion epafEvaluacionActiva; 

	private List<Grupo> epafListaAreasDocente; 
	private List<PersonaDatosDto> epafListaDocentesArea;
	private PersonaDatosDto epafPersonaDtoSeleccion;
	
	private List<FuncionTipoEvaluacion> epafListaFuncionTipoEvaluacion;
	
	private Integer[] epafRadioSeleccion;
	private List<Contenido> epafListaSeleccionContenido;
	private List<TipoContenido> epafListaTipoContenido;
	
	private UsuarioRol epafUsroEvaluador;
	private UsuarioRol epafUsroEvaluado;
	
	private Integer epafActivarReporte;

	//--v3
	//URL reporte
	private String epafLinkReporte;

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
	CarreraServicio servEpafCarreraServicio;
	@EJB
	GrupoServicio servEpafAreaServicio;
	@EJB
	PersonaDatosDtoServicioJdbc servEpafPersonaDatosServicioJdbc; 
	@EJB
	UsuarioRolServicio servEpafUsuarioRolServicio; 
	@EJB
	RolFlujoCarreraServicio servEpafRolFlujoCarreraServicio;  
	@EJB
	EvaluacionServicio servEpafEvaluacionServicio;
	@EJB
	FuncionTipoEvaluacionServicio servEpafFuncionTipoEvaluacionServicio; 
	@EJB
	ContenidoServicio servEpafContenidoServicio;
	@EJB
	EvaluadorEvaluadoServicio servEpafEvaluadorEvaluadoServicio; 
	@EJB
	AsignacionEvaluadorServicio servEpafAsignacionEvaluadorServicio;
	@EJB
	TpcnFuncionTpevServicio servEpafTpcnFuncionTpevServicio;
	@EJB
	ContenidoEvaluacionDtoServicioJdbc servEpafContenidoEvaluacionDtoJdbcServicio;
	@EJB
	RolFlujoGrupoServicio servEpafRolFlujoAreaServicio;
	
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
	public String irEvaluacionParAcademicoDocente(Usuario usuario) {
		
		Boolean verificar = false;
		
		
		try {
			epafActivarReporte = 0;
			epafUsuario = usuario;
			epafUsroEvaluador = servEpafUsuarioRolServicio.buscarXUsuarioXrol(epafUsuario.getUsrId(), RolConstantes.ROL_COORDINADORAREA_VALUE);
			epafListaAreasDocente = new ArrayList<>();
			 
			List<RolFlujoGrupo> rolflujogrupo = servEpafRolFlujoAreaServicio.buscarXRolXUsuarioId(usuario.getUsrId(), RolConstantes.ROL_COORDINADORAREA_VALUE);
			
			for(RolFlujoGrupo rlflrr: rolflujogrupo){ 
				epafListaAreasDocente.add(servEpafAreaServicio.buscarPorId(rlflrr.getRoflgrGrupo().getGrpId()));
			}

			if(rolflujogrupo != null && rolflujogrupo.size()>0){
				epafAreaBuscar = epafListaAreasDocente.get(epafListaAreasDocente.size()-1).getGrpId();
				verificar = true;
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.ir.evaluacion.directivo.docente.sin.areas")));
			}
			
		} catch (RolFlujoGrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoGrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
		
		if(verificar){
			inicarParametros();
		}
		return "irEvaluacionParAcademicoDocente";
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
	public String irEvaluarParAcademico(PersonaDatosDto docente) {

		try {
			epafUsroEvaluado = servEpafUsuarioRolServicio.buscarXPersonaXrol(docente.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);

			if(validarCronograma()){
				if(verificarEvaluacion(epafEvaluacionActiva, docente,  0)){

					epafPersonaDtoSeleccion = docente;

					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

					epafListaTipoContenido = new ArrayList<>();
					epafListaSeleccionContenido = new ArrayList<>();

					epafListaFuncionTipoEvaluacion = servEpafFuncionTipoEvaluacionServicio.listarActivoXTipoEvaluacion(epafEvaluacionActiva.getEvTipoEvaluacion().getTpevId());

					for(FuncionTipoEvaluacion it: epafListaFuncionTipoEvaluacion){

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

								epafListaTipoContenido.add(item);

								Contenido cnt = new Contenido();
								cnt.setCntEvaluacion(epafEvaluacionActiva);
								cnt.setCntTpcnFuncionTpev(servEpafTpcnFuncionTpevServicio.buscarXTipoContenido(item.getTpcnId()));
								cnt.setCntFecha(fechaActual);
								cnt.setCntUsuario(epafUsuario.getUsrNick());
								cnt.setCntDescripcion(item.getTpcnDescripcion()); 

								epafListaSeleccionContenido.add(cnt);
							}
						}
					}  
					epafRadioSeleccion = new Integer[epafListaTipoContenido.size()]; 
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
		
		return "irEvaluarParAcademico";
	}

	/**
	 * limpia y regresa a la ventana anterior
	 * @return Navegacion a la pagina anterior.
	 */
	public String irRegresar() {
		limpiarEvaluar();
		inicarParametros();
		epafActivarReporte = GeneralesConstantes.APP_ID_BASE;
		return "irEvaluacionParAcademicoDocente";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {
		epafDocente = null;   
		epafLinkReporte = null;
		epafUsuario = null; 
		epafEvaluacionActiva = null;
		epafListaAreasDocente = null; 
		epafListaFuncionTipoEvaluacion = null;
		epafRadioSeleccion = null;
		epafListaSeleccionContenido = null;
		epafListaTipoContenido = null;
		epafAreaBuscar = null;
		epafListaDocentesArea = null;
		epafPersonaDtoSeleccion = null;   
	}
	
	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiarEvaluar() {

		epafListaFuncionTipoEvaluacion = null;
		epafRadioSeleccion = null;
		epafListaSeleccionContenido = null;
		epafListaTipoContenido = null;

	}


	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

	/**
	 * Busca la entidad Docente basado en los parametros de ingreso
	 */
	public void buscar() {
		if(validarCronograma()){
			cargarDocentesArea();
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
			if(verificarEvaluacion(epafEvaluacionActiva, epafPersonaDtoSeleccion,  0)){
				try{

					if(verificarGuardar()){

						EvaluadorEvaluado evalrEval = new EvaluadorEvaluado();
						evalrEval.setEvevEvaluador(epafUsroEvaluador.getUsroId());
						evalrEval.setEvevUsuarioRol(epafUsroEvaluado);
						evalrEval = servEpafEvaluadorEvaluadoServicio.anadir(evalrEval);

						AsignacionEvaluador asigEval= new AsignacionEvaluador();
						asigEval.setAsevEvaluadorEvaluado(evalrEval);
						asigEval.setAsevEvaluadorCrrId(epafAreaBuscar);
						asigEval.setAsevEstado(0);
						asigEval.setAsevUsuario(epafUsuario.getUsrNick());
						asigEval.setAsevFecha(fechaActual);
						asigEval = servEpafAsignacionEvaluadorServicio.anadir(asigEval);


						for(Contenido item: epafListaSeleccionContenido){
							item.setCntAsignacionEvaluador(asigEval);
							Contenido contenido = servEpafContenidoServicio.anadir(item);
							if(contenido == null){
								verificar = false;
							}
						}

						if(verificar){
							direccion = irRegresar();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.guardar.exito")));
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.guardar.no.exito")));
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
	public void cargarDocentesArea(){
		try { 
			List<PersonaDatosDto> auxListPersonaDatosDto = servEpafPersonaDatosServicioJdbc.listarXAreaXPeriodo(epafAreaBuscar, epafEvaluacionActiva.getEvPeriodoAcademico().getPracId());
			if (auxListPersonaDatosDto != null && auxListPersonaDatosDto.size() >0) {
				Iterator itera = auxListPersonaDatosDto.iterator();
				while(itera.hasNext()){
					PersonaDatosDto cad = (PersonaDatosDto) itera.next();
					if(cad.getPrsId() == epafDocente.getPrsId()){
						itera.remove();
					}
				}
				
				auxListPersonaDatosDto.sort(Comparator.comparing(PersonaDatosDto::getPrsPrimerApellido));
				epafListaDocentesArea = auxListPersonaDatosDto;
			}
			
		} catch (PersonaDatosDtoNoEncontradoException e) {
			epafListaDocentesArea = null;
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
			epafEvaluacionActiva = servEpafEvaluacionServicio.buscarActivoXTipo(TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_VALUE);
			if(validarCronograma()){ 
				epafDocente = servEpafPersonaDatosServicioJdbc.buscarPorId(epafUsuario.getUsrPersona().getPrsId(), epafEvaluacionActiva.getEvPeriodoAcademico().getPracId());
				cargarDocentesArea();
			}
			
		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.verificar.acceso.evaluacion.no.activa.validacion.exception")));
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
			for(Contenido cnt: epafListaSeleccionContenido){
				if(cnt.getCntSeleccion() == null){
					if(sinRespuesta == null){
						sinRespuesta = count;
					}
					verificar = false;
				}
				count = count+1;
			}
			if(!verificar){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.verificar.guardar.sin.respuesta",sinRespuesta)));
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
		
		if(epafEvaluacionActiva != null){
			if(epafEvaluacionActiva.getEvaCronogramaFin().after(fechaActual)){
				if(epafEvaluacionActiva.getEvaCronogramaInicio().before(fechaActual)){ 
					verificar = true;
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.validar.cronograma.no.iniciado.validacion.exception")));
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.validar.cronograma.expirado.validacion.exception")));
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

		for(FuncionTipoEvaluacion it: epafListaFuncionTipoEvaluacion){
			for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){
				if(ite.getTpcnfntpevFuncionTipoEvaluacion().getFnctpevId() == idFnctpev){
					for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){
						if( item.getTpcnId() == idTpcn){
							for(int i = 0; i<epafListaTipoContenido.size(); i++){
								if(epafListaTipoContenido.get(i).getTpcnId() == idTpcn){
									epafListaSeleccionContenido.get(i).setCntSeleccion(epafRadioSeleccion[i]);
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
	public Boolean verificarEvaluacion(Evaluacion evaluacion, PersonaDatosDto persona, int mensaje){
		Boolean verificar = false;
		try {
			UsuarioRol usroEvaluado = servEpafUsuarioRolServicio.buscarXPersonaXrol(persona.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			List<ContenidoEvaluacionDto> contenido = servEpafContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), GeneralesConstantes.APP_ID_BASE, epafUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), epafAreaBuscar);
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
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionParAcademico.verificar.evaluacion.existente")));
		}
		return verificar;
	}
	
	/**
	 * Genera el reporte del directivo
	 * @param docente - docente a incluir la informacion
	 **/
	public void generarReporte(PersonaDatosDto docente){ 
		
		try {
			
			UsuarioRol usroEvaluado = servEpafUsuarioRolServicio.buscarXPersonaXrol(docente.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
			Grupo area = servEpafAreaServicio.buscarPorId(epafAreaBuscar);
			List<ContenidoEvaluacionDto> contenido = servEpafContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(epafEvaluacionActiva.getEvTipoEvaluacion().getTpevId(), epafEvaluacionActiva.getEvaId(), GeneralesConstantes.APP_ID_BASE, epafUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), epafAreaBuscar);
			ReporteEvaluacionDocenteForm.generarReporteParAcademico(contenido, docente, epafEvaluacionActiva, area);
			epafActivarReporte = 1;
			
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
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		}
		
	}
	
  
	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getEpafUsuario() {
		return epafUsuario;
	}

	public void setEpafUsuario(Usuario epafUsuario) {
		this.epafUsuario = epafUsuario;
	}

	public PersonaDatosDto getEpafDocente() {
		return epafDocente;
	}

	public void setEpafDocente(PersonaDatosDto epafDocente) {
		this.epafDocente = epafDocente;
	} 

	public Integer getEpafAreaBuscar() {
		return epafAreaBuscar;
	}

	public void setEpafAreaBuscar(Integer epafAreaBuscar) {
		this.epafAreaBuscar = epafAreaBuscar;
	} 
 
	public String getEpafLinkReporte() {
		return epafLinkReporte;
	}

	public void setEpafLinkReporte(String epafLinkReporte) {
		this.epafLinkReporte = epafLinkReporte;
	}

	public List<Grupo> getEpafListaAreasDocente() {
		return epafListaAreasDocente;
	}

	public void setEpafListaAreasDocente(List<Grupo> epafListaAreasDocente) {
		this.epafListaAreasDocente = epafListaAreasDocente;
	}
 
	public List<PersonaDatosDto> getEpafListaDocentesArea() {
		return epafListaDocentesArea;
	}

	public void setEpafListaDocentesArea(List<PersonaDatosDto> epafListaDocentesArea) {
		this.epafListaDocentesArea = epafListaDocentesArea;
	}

	public List<FuncionTipoEvaluacion> getEpafListaFuncionTipoEvaluacion() {
		return epafListaFuncionTipoEvaluacion;
	}

	public void setEpafListaFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> epafListaFuncionTipoEvaluacion) {
		this.epafListaFuncionTipoEvaluacion = epafListaFuncionTipoEvaluacion;
	}

	public Evaluacion getEpafEvaluacionActiva() {
		return epafEvaluacionActiva;
	}

	public void setEpafEvaluacionActiva(Evaluacion epafEvaluacionActiva) {
		this.epafEvaluacionActiva = epafEvaluacionActiva;
	}

	public Integer[] getEpafRadioSeleccion() {
		return epafRadioSeleccion;
	}

	public void setEpafRadioSeleccion(Integer[] epafRadioSeleccion) {
		this.epafRadioSeleccion = epafRadioSeleccion;
	}

	public List<Contenido> getEpafListaSeleccionContenido() {
		return epafListaSeleccionContenido;
	}

	public void setEpafListaSeleccionContenido(List<Contenido> epafListaSeleccionContenido) {
		this.epafListaSeleccionContenido = epafListaSeleccionContenido;
	}

	public List<TipoContenido> getEpafListaTipoContenido() {
		return epafListaTipoContenido;
	}

	public void setEpafListaTipoContenido(List<TipoContenido> epafListaTipoContenido) {
		this.epafListaTipoContenido = epafListaTipoContenido;
	}

	public PersonaDatosDto getEpafPersonaDtoSeleccion() {
		return epafPersonaDtoSeleccion;
	}

	public void setEpafPersonaDtoSeleccion(PersonaDatosDto epafPersonaDtoSeleccion) {
		this.epafPersonaDtoSeleccion = epafPersonaDtoSeleccion;
	}

	public Integer getEpafActivarReporte() {
		return epafActivarReporte;
	}

	public void setEpafActivarReporte(Integer epafActivarReporte) {
		this.epafActivarReporte = epafActivarReporte;
	}

}