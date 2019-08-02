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
   
 ARCHIVO:     EvaluacionEstudianteForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos de la evaluacion de estudiantes. 
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
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AsignacionEvaluadorServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
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
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.FichaMatriculaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoContenidoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.AsignacionEvaluador;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.EvaluadorEvaluado;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) EvaluacionEstudianteForm.java Bean de sesión que maneja
 * los atributos del formulario de Evaluacion del Estudiante.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */

@ManagedBean(name = "evaluacionEstudianteForm")
@SessionScoped
public class EvaluacionEstudianteForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
		// ************************* ATRIBUTOS ****************************/
		// ****************************************************************/

		private Usuario eefUsuario;

		private PersonaDatosDto eefDocente; 
 
		private Evaluacion eefEvaluacionActiva; 

		private List<Carrera> eefListaCarrerasEstudiante; 
		private List<FichaMatriculaDto> eefListaMatriculasEstudiante;
		private FichaMatriculaDto eefMatriculaEstudiante;
		private List<MateriaDto> eefListMateriaEstudiante;
		
		private MateriaDto eefMateriaDtoSelecion;
		
		private List<FuncionTipoEvaluacion> eefListaFuncionTipoEvaluacion;
		
		private Integer[] eefRadioSeleccion;
		private List<Contenido> eefListaSeleccionContenido;
		private List<TipoContenido> eefListaTipoContenido;
		
		private CargaHoraria eefCrhrSeleccion;
		private PersonaDatosDto eefPrsDtoSeleccion;
		private UsuarioRol eefUsroEvaluador;
		private UsuarioRol eefUsroEvaluado;

		//--v3
		//URL reporte
		private String eefLinkReporte;

		// ****************************************************************/
		// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
		// ****************************************************************/

		@PostConstruct
		public void inicializar() {
		}

		// ****************************************************************/
		// ********************* SERVICIOS GENERALES **********************/
		// ****************************************************************/

		@EJB private CarreraServicio servEefCarreraServicio;
		@EJB private PersonaDatosDtoServicioJdbc servEefPersonaDatosServicioJdbc;
		@EJB private PersonaServicio servEefPersonaServicio;
		@EJB private PlanificacionCronogramaDtoServicioJdbc servEefPlanificacionCronogramaDtoServicioJdbc;
		@EJB private UsuarioRolServicio servEefUsuarioRolServicio;
		@EJB private RolFlujoCarreraServicio servEefRolFlujoCarreraServicio;
		@EJB private DocenteDatosDtoServicioJdbc servEefDocenteDatosDtoServicioJdbc;
		@EJB private DocenteDtoServicioJdbc servEefDocenteDtoServicioJdbc;
		@EJB private EvaluacionServicio servEefEvaluacionServicio;
		@EJB private FichaMatriculaDtoServicioJdbc servEefFichaMatriculaDtoServicioJdbc;
		@EJB private MateriaDtoServicioJdbc servEefMateriaDtoServicioJdbc;
		@EJB private FuncionTipoEvaluacionServicio servEefFuncionTipoEvaluacionServicio;
		@EJB private TipoContenidoServicio servEefTipoContenidoServicio;
		@EJB private ContenidoServicio servEefContenidoServicio;
		@EJB private EvaluadorEvaluadoServicio servEefEvaluadorEvaluadoServicio; 
		@EJB private AsignacionEvaluadorServicio servEefAsignacionEvaluadorServicio;
		@EJB private TpcnFuncionTpevServicio servEefTpcnFuncionTpevServicio;
		@EJB private CargaHorariaServicio servEefCargaHorariaServicio; 
		@EJB private ContenidoEvaluacionDtoServicioJdbc servEefContenidoEvaluacionDtoJdbcServicio;
		@EJB private CronogramaDtoServicioJdbc servJdbcCronogramaDto;
		@EJB private HorarioAcademicoDtoServicioJdbc servJdbcHorarioAcademicoDto;

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
		public String irEvaluacionEstudianteDocente(Usuario usuario) {

			try {
				eefUsuario = usuario; 
				eefUsroEvaluador = servEefUsuarioRolServicio.buscarXUsuarioXrol(eefUsuario.getUsrId(), RolConstantes.ROL_ESTUD_PREGRADO_VALUE);
			} catch (UsuarioRolException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
			inicarParametros();
			 
			return "irEvaluacionEstudianteDocente";
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
		public String irEvaluarDirectivo(MateriaDto materia) {

			try {
//				if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, eefMatriculaEstudiante.getFcmtNivelUbicacion(), ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE, eefMatriculaEstudiante.getCrrId(), true)){

				
				eefCrhrSeleccion = servEefCargaHorariaServicio.buscarPorMlCrMtXPeriodoXParalelo(materia.getMlcrmtId(), eefEvaluacionActiva.getEvPeriodoAcademico().getPracId(), materia.getPrlId());
				eefPrsDtoSeleccion = servEefPersonaDatosServicioJdbc.buscarXDetallePuesto(eefCrhrSeleccion.getCrhrDetallePuesto().getDtpsId());
				eefUsroEvaluado = servEefUsuarioRolServicio.buscarXPersonaXrol(eefPrsDtoSeleccion.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
				
				if(validarCronograma()){
					if(verificarEvaluacion(eefEvaluacionActiva, materia,  0)){

						eefMateriaDtoSelecion = materia;

						Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

						eefListaTipoContenido = new ArrayList<>();
						eefListaSeleccionContenido = new ArrayList<>(); 
						eefListaFuncionTipoEvaluacion = servEefFuncionTipoEvaluacionServicio.listarActivoXTipoEvaluacion(eefEvaluacionActiva.getEvTipoEvaluacion().getTpevId());

						for(FuncionTipoEvaluacion it: eefListaFuncionTipoEvaluacion){

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

									eefListaTipoContenido.add(item);

									Contenido cnt = new Contenido();
									cnt.setCntEvaluacion(eefEvaluacionActiva);
									cnt.setCntTpcnFuncionTpev(servEefTpcnFuncionTpevServicio.buscarXTipoContenido(item.getTpcnId()));
									cnt.setCntFecha(fechaActual);
									cnt.setCntUsuario(eefUsuario.getUsrNick());
									cnt.setCntDescripcion(item.getTpcnDescripcion());

									eefListaSeleccionContenido.add(cnt);
								}
							}
						} 

						eefRadioSeleccion = new Integer[eefListaTipoContenido.size()];
					}
				}

//				}else {
//					return null;
//				}
				
			} catch (FuncionTipoEvaluacionNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (FuncionTipoEvaluacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (TpcnFuncionTpevNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());  
			} catch (TpcnFuncionTpevException e) {
				FacesUtil.mensajeError(e.getMessage()); 
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage()); 
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(e.getMessage()); 
			} catch (PersonaDatosDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage()); 
			} catch (PersonaDatosDtoException e) {
				FacesUtil.mensajeError(e.getMessage()); 
			} catch (UsuarioRolException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} 
			return "irEvaluarEstudiante";
		}
		
		/**
		 * limpia y regresa a la ventana anterior
		 * @return Navegacion a la pagina anterior.
		 */
		public String irRegresar() {
			limpiarEvaluar();
			inicarParametros();
			return "irEvaluacionEstudiante";
		}

		// >>-------------------------------------------LIMPIEZA-------------------------------------------------

		/**
		 * Setea y nulifica a los valores iniciales de cada parametro
		 */
		public void limpiar() {
			eefDocente = null;   
			eefLinkReporte = null;
			eefUsuario = null; 
			eefEvaluacionActiva = null;
			eefListaCarrerasEstudiante = null; 
			eefListaFuncionTipoEvaluacion = null;
			eefRadioSeleccion = null;
			eefListaSeleccionContenido = null;
			eefListaTipoContenido = null;
			eefMateriaDtoSelecion = null;
			eefListaMatriculasEstudiante = null;
			eefMatriculaEstudiante = null;
			eefListMateriaEstudiante = null;
		}
		
		/**
		 * Setea y nulifica a los valores iniciales de cada parametro
		 */
		public void limpiarEvaluar() {

			eefListaFuncionTipoEvaluacion = null;
			eefRadioSeleccion = null;
			eefListaSeleccionContenido = null;
			eefListaTipoContenido = null;
			eefMateriaDtoSelecion = null;
			eefCrhrSeleccion = null;
			eefPrsDtoSeleccion = null;

		}


		// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------

		/**
		 * Busca la entidad Docente basado en los parametros de ingreso
		 */
		public void buscar() {
			cambiarFicha(eefMatriculaEstudiante.getFcmtId());
			if(validarCronograma()){
				cargarMaterias();
			}
		}
		
		public void cambiarFicha(int ficha){
			try {
				eefMatriculaEstudiante = servEefFichaMatriculaDtoServicioJdbc.buscarFichaMatriculaDto(ficha);
			} catch (FichaMatriculaException e) {
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
				
				try{

					if(verificarGuardar()){
  
						EvaluadorEvaluado evalrEval = new EvaluadorEvaluado();
						evalrEval.setEvevEvaluador(eefUsroEvaluador.getUsroId());
						evalrEval.setEvevUsuarioRol(eefUsroEvaluado);
						evalrEval = servEefEvaluadorEvaluadoServicio.anadir(evalrEval);

						AsignacionEvaluador asigEval= new AsignacionEvaluador();
						asigEval.setAsevEvaluadorEvaluado(evalrEval);
						asigEval.setAsevCargaHoraria(eefCrhrSeleccion);
						asigEval.setAsevEstado(0);
						asigEval.setAsevUsuario(eefUsuario.getUsrNick());
						asigEval.setAsevFecha(fechaActual);
						asigEval = servEefAsignacionEvaluadorServicio.anadir(asigEval);
						

						for(Contenido item: eefListaSeleccionContenido){
							item.setCntAsignacionEvaluador(asigEval);
							Contenido contenido = servEefContenidoServicio.anadir(item);
							if(contenido == null){
								verificar = false;
							}
						}

						if(verificar){
							direccion = irRegresar();
							FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.guardar.exito")));
						}else{
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.guardar.no.exito")));
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

			return direccion;
		}

		// ****************************************************************/
		// *********************** METODOS AUXILIARES *********************/
		// ****************************************************************/

		// >>---------------------------------------CARGA_DE_DATOS-----------------------------------------------
		@SuppressWarnings("rawtypes")
		public void cargarMaterias(){
			try { 
//				if(verificarCronogramaSemestreCarrera(TipoAperturaConstantes.TPAP_POR_CARRERA_VALUE, eefMatriculaEstudiante.getFcmtNivelUbicacion(), ProcesoFlujoConstantes.PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE, eefMatriculaEstudiante.getCrrId(), true)){
					if (eefMatriculaEstudiante.getFcmtNivelUbicacion().intValue() != NivelConstantes.NIVEL_NIVELACION_VALUE) {
						List<MateriaDto> auxListMateriaDto = servEefMateriaDtoServicioJdbc.listarXestudianteXmatriculaXperiodoXcarrera(eefUsuario.getUsrPersona().getPrsId(), eefMatriculaEstudiante.getFcmtId(), eefMatriculaEstudiante.getPracId());
						List<MateriaDto> materias =  new ArrayList<>();
						
						if (auxListMateriaDto != null && auxListMateriaDto.size()> 0) {
							for (MateriaDto item : auxListMateriaDto) {
								if (item.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
									materias.addAll(cargarModulos(item));
								}
							}	
						}
						
						/// quitar modulares
						auxListMateriaDto.addAll(materias);
						Iterator iter = auxListMateriaDto.iterator();
						while(iter.hasNext()){
							final MateriaDto item = (MateriaDto) iter.next();
							if (item.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
								iter.remove();
							}
						}
						/// buscar si es compartida para actualizar los id
						for (MateriaDto item : auxListMateriaDto) {
							List<HorarioAcademicoDto> horarios = cargarParaleloCompartido(item.getMlcrprId());
							if (horarios != null && horarios.size() >0) {
								List<HorarioAcademicoDto> horariosPadre = cargarParaleloCompartido(horarios.get(0).getHracMlcrprIdComp());
								if (horariosPadre != null && horariosPadre.size() > 0) {
									item.setMlcrprId(horariosPadre.get(0).getMlcrprId());
									item.setMlcrmtId(horariosPadre.get(0).getMlcrmtId());
									item.setPrlId(horariosPadre.get(0).getPrlId());
								}
							}
						}

						auxListMateriaDto.sort(Comparator.comparing(MateriaDto::getMtrDescripcion));
						eefListMateriaEstudiante = auxListMateriaDto;
					}else {
						eefListMateriaEstudiante = null;	
						FacesUtil.mensajeError("No aplica a estudiantes con materias en nivelación.");
					}
//				}else {
//					eefListMateriaEstudiante = null;	
//				}
			} catch (MateriaDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (MateriaDtoNoEncontradoException e) {
				eefListaMatriculasEstudiante = null;
			}
		}

		// >>----------------------------------VERIFICACION_DE_PARAMETROS----------------------------------------
 
		private List<HorarioAcademicoDto> cargarParaleloCompartido(int mlcrprId){
			List<HorarioAcademicoDto> retorno = null;

			try {
				retorno = servJdbcHorarioAcademicoDto.buscarParaleloCompartido(mlcrprId);
			} catch (HorarioAcademicoDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (HorarioAcademicoDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

			FacesUtil.limpiarMensaje();	

			return retorno;

		}
		
		private List<MateriaDto> cargarModulos(MateriaDto recordId) {
			try {
				List<MateriaDto> retorno = servEefMateriaDtoServicioJdbc.listarXrecordXmatriculaXperiodo(recordId.getRcesId(), eefMatriculaEstudiante.getFcmtId(), eefMatriculaEstudiante.getPracId());
				if (retorno != null && retorno.size() > 0) {
					for (MateriaDto item : retorno) {
						item.setMtrDescripcion(recordId.getMtrDescripcion() + " - " + item.getMtrDescripcion());
					}
				}

				return retorno;
			} catch (MateriaDtoException e) {
				return null;
			} catch (MateriaDtoNoEncontradoException e) {
				return null;
			}
		}

		/**
		 * Verifica el acceso del docente a la autoevaluacion
		 * otorgando un estado true / false para activar la evaluacion
		 */
		public void verificarAcceso(){

			try{ 
				eefEvaluacionActiva = servEefEvaluacionServicio.buscarActivoXTipo(TipoEvaluacionConstantes.EVALUACION_ESTUDIANTE_VALUE);
				if(validarCronograma()){
					eefListaMatriculasEstudiante = servEefFichaMatriculaDtoServicioJdbc.ListarXPeriodoXidentificacionXcarreraXEstado(eefEvaluacionActiva.getEvPeriodoAcademico().getPracId(), eefUsuario.getUsrPersona().getPrsIdentificacion(), GeneralesConstantes.APP_ID_BASE, DetalleMatriculaConstantes.ESTADO_ACTIVO_VALUE);
					if(eefListaMatriculasEstudiante != null && eefListaMatriculasEstudiante.size() > 0){
						eefMatriculaEstudiante = eefListaMatriculasEstudiante.get(eefListaMatriculasEstudiante.size()-1);
							cargarMaterias();
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.verificar.acceso.sin.matriculas")));
					}
				}
				
			} catch (EvaluacionNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.verificar.acceso.evaluacion.no.activa.validacion.exception")));
			} catch (EvaluacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (FichaMatriculaException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EvaluacionValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			}       
		}
		
		
//		public Boolean verificarCronogramaSemestreCarrera(int tipoApertura, int numeral, int procesoFlujo, int crrId, boolean mensaje){
//			Boolean verificar = false;
//			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
//			
//			try {
//				CronogramaDto cronograma = servJdbcCronogramaDto.buscarCronogramaXTipoAperturaXNivelAperturaXprocesoFlujo(procesoFlujo, tipoApertura, numeral, crrId);
//				  
//				if(cronograma != null){
//					if(cronograma.getPlcrFechaFin() != null && cronograma.getPlcrFechaInicio() != null){
//						
//						if(cronograma.getPlcrFechaFin().after(fechaActual)){
//							if(cronograma.getPlcrFechaInicio().before(fechaActual)){ 
//								verificar = true;
//							}else{ 
//								FacesUtil.mensajeError("El cronograma para "+cronograma.getCrnDescripcion()+" no ha empezado."); 
//							}
//						}else{ 
//							FacesUtil.mensajeError("El cronograma para "+cronograma.getCrnDescripcion()+" ha expirado."); 
//						}
//					}else{
//						FacesUtil.mensajeError("No se ha asignado cronograma para "+cronograma.getCrnDescripcion()+".");
//					}
//				}else{
//					FacesUtil.limpiarMensaje();
//					FacesUtil.mensajeError("No se ha asignado cronograma para evaluación al docente.");
//				}
//				
//				if(!mensaje){
//					FacesUtil.limpiarMensaje();
//				}
//				
//			} catch (CronogramaDtoJdbcException e) {
//				FacesUtil.mensajeError(e.getMessage());
//			} catch (CronogramaDtoJdbcNoEncontradoException e) {
//				FacesUtil.limpiarMensaje();
////				FacesUtil.mensajeError("No se ha asignado cronograma para evaluación al docente.");
//				FacesUtil.mensajeError("No aplica a estudiantes con materias en nivelación.");
//			}
//		 
//			return verificar;
//		}

		  
		/**
		 * Verifica campos, parametos antes de guardar las distintas cargas horarias
		 * @return Estado false por culquier error al ingresar, true - pasa las validaciones 
		 **/
		public Boolean verificarGuardar() {
			Boolean verificar = true;
			if(validarCronograma()){
				Integer sinRespuesta = null;
				int count = 1;
				for(Contenido cnt: eefListaSeleccionContenido){
					if(cnt.getCntSeleccion() == null){
						if(sinRespuesta == null){
							sinRespuesta = count;
						}
						verificar = false;
					}
					count = count+1;
				}
				if(!verificar){
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.verificar.guardar.sin.respuesta",sinRespuesta)));
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
			
			if(eefEvaluacionActiva != null){
				if(eefEvaluacionActiva.getEvaCronogramaFin().after(fechaActual) || eefEvaluacionActiva.getEvaCronogramaFin().equals(fechaActual)){
					if(eefEvaluacionActiva.getEvaCronogramaInicio().before(fechaActual) || eefEvaluacionActiva.getEvaCronogramaInicio().equals(fechaActual)){ 
						verificar = true;
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.validar.cronograma.no.iniciado.validacion.exception")));
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.validar.cronograma.expirado.validacion.exception")));
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

			for(FuncionTipoEvaluacion it: eefListaFuncionTipoEvaluacion){
				for(TpcnFuncionTpev ite: it.getFntpevListTpcnFuncionTpev()){
					if(ite.getTpcnfntpevFuncionTipoEvaluacion().getFnctpevId() == idFnctpev){
						for(TipoContenido item: ite.getTpcnfntpevTipoContenido().getTpcnLisTipoContenido()){
							if( item.getTpcnId() == idTpcn){
								for(int i = 0; i<eefListaTipoContenido.size(); i++){
									if(eefListaTipoContenido.get(i).getTpcnId() == idTpcn){
										eefListaSeleccionContenido.get(i).setCntSeleccion(eefRadioSeleccion[i]);
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
		public Boolean verificarEvaluacion(Evaluacion evaluacion,  MateriaDto materia, int mensaje){
			Boolean verificar = false;
			try {
				
				CargaHoraria crhrSeleccion = servEefCargaHorariaServicio.buscarPorMlCrMtXPeriodoXParalelo(materia.getMlcrmtId(), eefEvaluacionActiva.getEvPeriodoAcademico().getPracId(), materia.getPrlId());
				PersonaDatosDto prsDtoSeleccion = servEefPersonaDatosServicioJdbc.buscarXDetallePuesto(crhrSeleccion.getCrhrDetallePuesto().getDtpsId());
				UsuarioRol usroEvaluado = servEefUsuarioRolServicio.buscarXPersonaXrol(prsDtoSeleccion.getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
				
				List<ContenidoEvaluacionDto> contenido = servEefContenidoEvaluacionDtoJdbcServicio.listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(evaluacion.getEvTipoEvaluacion().getTpevId(), evaluacion.getEvaId(), crhrSeleccion.getCrhrId(), eefUsroEvaluador.getUsroId(), usroEvaluado.getUsroId(), GeneralesConstantes.APP_ID_BASE );
				if(contenido == null || contenido.size() == 0){
					verificar = true;
				}
				
			} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
				verificar = true;
			} catch (ContenidoEvaluacionDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (UsuarioRolException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError("No hay docente asignado en la materia " + materia.getMtrDescripcion());
			} catch (CargaHorariaException e) {
				 FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDatosDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDatosDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
			if(!verificar && mensaje == 0){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluacionEstudiante.verificar.evaluacion.existente"))); 
			}
			return verificar;
		}
		

		// ****************************************************************/
		// *********************** GETTERS Y SETTERS **********************/
		// ****************************************************************/

		public Usuario getEefUsuario() {
			return eefUsuario;
		}

		public void setEefUsuario(Usuario eefUsuario) {
			this.eefUsuario = eefUsuario;
		}

		public PersonaDatosDto getEefDocente() {
			return eefDocente;
		}

		public void setEefDocente(PersonaDatosDto eefDocente) {
			this.eefDocente = eefDocente;
		} 
 
		public String getEefLinkReporte() {
			return eefLinkReporte;
		}

		public void setEefLinkReporte(String eefLinkReporte) {
			this.eefLinkReporte = eefLinkReporte;
		}

		public List<Carrera> getEefListaCarrerasDocente() {
			return eefListaCarrerasEstudiante;
		}

		public void setEefListaCarrerasDocente(List<Carrera> eefListaCarrerasEstudiante) {
			this.eefListaCarrerasEstudiante = eefListaCarrerasEstudiante;
		}

		public List<FichaMatriculaDto> getEefListaMatriculasEstudiante() {
			return eefListaMatriculasEstudiante;
		}

		public void setEefListaMatriculasEstudiante(List<FichaMatriculaDto> eefListaMatriculasEstudiante) {
			this.eefListaMatriculasEstudiante = eefListaMatriculasEstudiante;
		}

		public List<MateriaDto> getEefListMateriaEstudiante() {
			return eefListMateriaEstudiante;
		}

		public void setEefListMateriaEstudiante(List<MateriaDto> eefListMateriaEstudiante) {
			this.eefListMateriaEstudiante = eefListMateriaEstudiante;
		}

		public FichaMatriculaDto getEefMatriculaEstudiante() {
			return eefMatriculaEstudiante;
		}

		public void setEefMatriculaEstudiante(FichaMatriculaDto eefMatriculaEstudiante) {
			this.eefMatriculaEstudiante = eefMatriculaEstudiante;
		}

		public Evaluacion getEefEvaluacionActiva() {
			return eefEvaluacionActiva;
		}

		public void setEefEvaluacionActiva(Evaluacion eefEvaluacionActiva) {
			this.eefEvaluacionActiva = eefEvaluacionActiva;
		}

		public List<Carrera> getEefListaCarrerasEstudiante() {
			return eefListaCarrerasEstudiante;
		}

		public void setEefListaCarrerasEstudiante(List<Carrera> eefListaCarrerasEstudiante) {
			this.eefListaCarrerasEstudiante = eefListaCarrerasEstudiante;
		}

		public List<FuncionTipoEvaluacion> getEefListaFuncionTipoEvaluacion() {
			return eefListaFuncionTipoEvaluacion;
		}

		public void setEefListaFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> eefListaFuncionTipoEvaluacion) {
			this.eefListaFuncionTipoEvaluacion = eefListaFuncionTipoEvaluacion;
		}

		public Integer[] getEefRadioSeleccion() {
			return eefRadioSeleccion;
		}

		public void setEefRadioSeleccion(Integer[] eefRadioSeleccion) {
			this.eefRadioSeleccion = eefRadioSeleccion;
		}

		public List<Contenido> getEefListaSeleccionContenido() {
			return eefListaSeleccionContenido;
		}

		public void setEefListaSeleccionContenido(List<Contenido> eefListaSeleccionContenido) {
			this.eefListaSeleccionContenido = eefListaSeleccionContenido;
		}

		public List<TipoContenido> getEefListaTipoContenido() {
			return eefListaTipoContenido;
		}

		public void setEefListaTipoContenido(List<TipoContenido> eefListaTipoContenido) {
			this.eefListaTipoContenido = eefListaTipoContenido;
		}

		public MateriaDto getEefMateriaDtoSelecion() {
			return eefMateriaDtoSelecion;
		}

		public void setEefMateriaDtoSelecion(MateriaDto eefMateriaDtoSelecion) {
			this.eefMateriaDtoSelecion = eefMateriaDtoSelecion;
		}

		public PersonaDatosDto getEefPrsDtoSeleccion() {
			return eefPrsDtoSeleccion;
		}

		public void setEefPrsDtoSeleccion(PersonaDatosDto eefPrsDtoSeleccion) {
			this.eefPrsDtoSeleccion = eefPrsDtoSeleccion;
		}
	  
	}