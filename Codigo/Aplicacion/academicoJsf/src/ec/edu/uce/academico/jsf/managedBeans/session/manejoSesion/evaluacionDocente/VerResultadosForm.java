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

 ARCHIVO:     VerResultadosForm.java	  
 DESCRIPCION:  Bean de sesión que maneja los atributos del formulario de Resultados. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
01-FEBRERO-2017		 Arturo Villafuerte 			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.evaluacionDocente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDatosDto;
import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.GrupoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ContenidoEvaluacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteEvaluacionDocenteForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (session bean) VerResultadosForm.java Bean de sesión que maneja
 * los atributos del formulario de Resultados.
 * 
 * @author ajvillvrfuerte.
 * @version 1.0
 */

@ManagedBean(name = "verResultadosForm")
@SessionScoped
public class VerResultadosForm implements Serializable {

	private static final long serialVersionUID = 1881903750536586483L;

	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/

	private Usuario vrfUsuario; 
	private PersonaDatosDto vrfDocente; 

	private Integer vrfPeriodoBuscar;  

	private PersonaDatosDto vrfPersonaDtoSeleccion;

	private List<ContenidoEvaluacionDatosDto> vrfListaEvaluacionesDto;  
	private List<PeriodoAcademico> vrfListaPeriodos;

	private UsuarioRol vrfUsroEvaluado;
	private UsuarioRol vrfUsroEvaluadoCoord;
	
	private Integer vrfActivarReporte;
	
	private String vrfNombreReporte;
	private String vrfNombreJasper;

	//--v3
	//URL reporte
	private String vrfLinkReporte;

	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/

	@EJB private 	CarreraServicio servVrfCarreraServicio;
	@EJB private 	PersonaDatosDtoServicioJdbc servVrfPersonaDatosServicioJdbc;  
	@EJB private 	UsuarioRolServicio servVrfUsuarioRolServicio;    
	@EJB private 	EvaluacionServicio servVrfEvaluacionServicio;      
	@EJB private 	ContenidoEvaluacionDtoServicioJdbc servVrfContenidoEvaluacionDtoJdbcServicio;
	@EJB private 	PeriodoAcademicoServicio servVrfPeriodoAcademicoServicio; 
	@EJB private 	GrupoServicio servVrfAreaServicio;

	

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
	public String irVerResultados(Usuario usuario) {


		try {
			vrfActivarReporte = 0;
			vrfUsuario = usuario; 
			vrfUsroEvaluado = servVrfUsuarioRolServicio.buscarXPersonaXrol(usuario.getUsrPersona().getPrsId(), RolConstantes.ROL_DOCENTE_VALUE);
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		try {
			vrfUsroEvaluadoCoord = servVrfUsuarioRolServicio.buscarXPersonaXrol(usuario.getUsrPersona().getPrsId(), RolConstantes.ROL_COORDINADORAREA_VALUE);
		} catch (UsuarioRolException e) {
		} catch (UsuarioRolNoEncontradoException e) {
		}

		
		inicarParametros(); 

		return "irVerResultados";
	}


	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return Navegacion a la pagina de inicio.
	 */
	public String irInicio() {
		 
		limpiar();
		return "irInicio";
	}

	// >>-------------------------------------------LIMPIEZA-------------------------------------------------

	/**
	 * Setea y nulifica a los valores iniciales de cada parametro
	 */
	public void limpiar() {     
		
		vrfUsuario = null; 
		vrfDocente = null; 
		vrfPeriodoBuscar = null;  
		vrfPersonaDtoSeleccion = null;
		vrfListaEvaluacionesDto = null;  
		vrfListaPeriodos = null;
		vrfUsroEvaluado = null;
		vrfUsroEvaluadoCoord = null;
		vrfActivarReporte = null;
		vrfNombreReporte = null;
		vrfNombreJasper = null;
	}



	// >>--------------------------------------METOOS_DE_BUSQUEDA--------------------------------------------}
	public void buscar(){
		cargarEvaluaciones(1);
	}

	// >>-----------------------------------METOOS_DE_FUNCIONALIDAD------------------------------------------


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
			vrfPeriodoBuscar = GeneralesConstantes.APP_ID_BASE;
			vrfDocente = servVrfPersonaDatosServicioJdbc.buscarPorId(vrfUsuario.getUsrPersona().getPrsId(), vrfPeriodoBuscar);
			
			cargarEvaluaciones(0);

		} catch (PersonaDatosDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDatosDtoException e) { 
			FacesUtil.mensajeError(e.getMessage());
		}    
	}

	/**
	 * Verifica la existencia de una evaluacion.
	 * @param listar .- estado de reenlistado 0 reenlistar / 1 no reenlistar
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cargarEvaluaciones(int listar){
		List<ContenidoEvaluacionDto> contenido = new ArrayList<>();

		if (vrfUsroEvaluadoCoord != null && vrfUsroEvaluadoCoord.getUsroId() != 0) {
			try {
				List<ContenidoEvaluacionDto> contenidoCoord = servVrfContenidoEvaluacionDtoJdbcServicio.listarEvaluacionIdXEvaluadoXPeriodo(vrfUsroEvaluadoCoord.getUsroId(), vrfPeriodoBuscar);
				contenido.addAll(contenidoCoord);
			} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			} catch (ContenidoEvaluacionDtoException e) {
			}
		}
		
		try {
			List<ContenidoEvaluacionDto> contenidoDoc = servVrfContenidoEvaluacionDtoJdbcServicio.listarEvaluacionIdXEvaluadoXPeriodo(vrfUsroEvaluado.getUsroId(), vrfPeriodoBuscar);
			contenido.addAll(contenidoDoc);
			
			if(contenido != null && contenido.size() > 0){
				vrfListaEvaluacionesDto = new ArrayList<>();
				  
				for(ContenidoEvaluacionDto item: contenido){ 

					Evaluacion eval = servVrfEvaluacionServicio.buscarPorId(item.getEvaId());
					
//					if(fechaActual.after(eval.getEvaCronogramaFin())){

						if(eval.getEvTipoEvaluacion().getTpevId() != TipoEvaluacionConstantes.EVALUACION_ESTUDIANTE_VALUE){
							
							ContenidoEvaluacionDatosDto cntEvlDto = new ContenidoEvaluacionDatosDto(); 
							cntEvlDto.setEvaEstado(eval.getEvaEstado());
							cntEvlDto.setEvaId(eval.getEvaId());
							cntEvlDto.setEvaDescripcion(eval.getEvaDescripcion());

							PeriodoAcademico periodoAcademico = eval.getEvPeriodoAcademico();
							cntEvlDto.setPracId(periodoAcademico.getPracId());
							cntEvlDto.setPracDescripcion(periodoAcademico.getPracDescripcion());

							if(item.getAsevCrr() != null && item.getAsevCrr() != 0 && eval.getEvTipoEvaluacion().getTpevId() == TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_VALUE){
								Carrera carrera = servVrfCarreraServicio.buscarPorId(item.getAsevCrr());
								if (carrera != null) {
									cntEvlDto.setCrrDescripcion(carrera.getCrrDescripcion());
									cntEvlDto.setCrrId(carrera.getCrrId());
									cntEvlDto.setTpevId(eval.getEvTipoEvaluacion().getTpevId());
									cntEvlDto.setTpevDescripcion(eval.getEvTipoEvaluacion().getTpevDescripcion());

									cntEvlDto.setAsevId(item.getAsevId());

									vrfListaEvaluacionesDto.add(cntEvlDto);
								}
								
							} 
							
							if(item.getAsevCrr() != null && item.getAsevCrr() != 0 && eval.getEvTipoEvaluacion().getTpevId() == TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_VALUE){
								Grupo grupo = servVrfAreaServicio.buscarPorId(item.getAsevCrr());
								if (grupo != null) {
									cntEvlDto.setCrrDescripcion(grupo.getGrpDescripcion());
									cntEvlDto.setCrrId(grupo.getGrpId());
									cntEvlDto.setTpevId(eval.getEvTipoEvaluacion().getTpevId());
									cntEvlDto.setTpevDescripcion(eval.getEvTipoEvaluacion().getTpevDescripcion());

									cntEvlDto.setAsevId(item.getAsevId());

									vrfListaEvaluacionesDto.add(cntEvlDto);
								}
								
							} 

							
						}
					}
				}

				if(listar == 0){
					vrfListaPeriodos = new ArrayList<>();
					
					List<Integer> idPrac = new ArrayList<>();
					for(ContenidoEvaluacionDatosDto item: vrfListaEvaluacionesDto){
						idPrac.add(item.getPracId());
					}

					HashSet ht = new HashSet();   
					ht.addAll(idPrac);	     
					idPrac.clear();
					idPrac.addAll(ht);

					for(Integer item: idPrac){
						vrfListaPeriodos.add(servVrfPeriodoAcademicoServicio.buscarPorId(item));
					}
				}
				vrfListaEvaluacionesDto.sort(Comparator.comparing(ContenidoEvaluacionDatosDto::getPracId).thenComparing(ContenidoEvaluacionDatosDto::getTpevId).reversed());
//			}

		} catch (ContenidoEvaluacionDtoNoEncontradoException e) { 
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("VerResultados.cargar.evaluaciones.sin.evaluaciones")));
			vrfListaEvaluacionesDto = null;
			vrfListaPeriodos = null; 
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			e.printStackTrace();
		} catch (CarreraException e) {
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

	public void validarEvaluacionDirectivo(Evaluacion evaluacion){

	}
	
	 
	/**
	 * Genera el reporte del directivo
	 * @param evaluacion - evaluacion a incluir la informacion
	 **/
	public void generarReporte(ContenidoEvaluacionDatosDto evaluacionDto){ 
		
		try {
			Evaluacion evaluacion = servVrfEvaluacionServicio.buscarPorId(evaluacionDto.getEvaId());
			List<ContenidoEvaluacionDto> contenido = servVrfContenidoEvaluacionDtoJdbcServicio.listarXAsignacionEvaluador(evaluacionDto.getAsevId()); 
			vrfActivarReporte = 1;
			
			switch (evaluacion.getEvTipoEvaluacion().getTpevId()) {
			case 1:
				vrfNombreReporte = TipoEvaluacionConstantes.AUTOEVALUACION_LABEL;
				vrfNombreJasper = TipoEvaluacionConstantes.AUTOEVALUACION_JASPER;
				ReporteEvaluacionDocenteForm.generarReporteAutoevaluacion(contenido, vrfDocente, evaluacion);
				break;
			case 3:
				Carrera carrera = servVrfCarreraServicio.buscarPorId(contenido.get(contenido.size()-1).getAsevCrr());
				vrfNombreReporte = TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_LABEL;
				vrfNombreJasper = TipoEvaluacionConstantes.EVALUACION_DIRECTIVO_JASPER;
				ReporteEvaluacionDocenteForm.generarReporteDirectivo(contenido, vrfDocente, evaluacion, carrera);
				break;
			case 4:
				Grupo area = servVrfAreaServicio.buscarPorId(contenido.get(contenido.size()-1).getAsevCrr());
				vrfNombreReporte = TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_LABEL;
				vrfNombreJasper = TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_JASPER;
				ReporteEvaluacionDocenteForm.generarReporteParAcademico(contenido, vrfDocente, evaluacion, area);
				break;
			default:
				break;
			}
			
			
		} catch (ContenidoEvaluacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (ContenidoEvaluacionDtoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());  
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());  
		} catch (EvaluacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());  
		} catch (EvaluacionException e) {
			FacesUtil.mensajeError(e.getMessage());  
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		}
		
	}

	public String etiquetaCarreraArea(int idTipoEvaluacion, int idCarreraArea, String crrDescripcion){
		String retorno = null;
		try {
			if(idTipoEvaluacion == TipoEvaluacionConstantes.EVALUACION_PAR_ACADEMICO_VALUE){

				retorno = servVrfAreaServicio.buscarPorId(idCarreraArea).getGrpDescripcion();

			}else{
				retorno = crrDescripcion;
			}
		} catch (GrupoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		} catch (GrupoException e) {
			FacesUtil.mensajeError(e.getMessage()); 
			e.printStackTrace();
		}
		return retorno;
	}

	// ****************************************************************/
	// *********************** GETTERS Y SETTERS **********************/
	// ****************************************************************/

	public Usuario getVrfUsuario() {
		return vrfUsuario;
	}

	public void setVrfUsuario(Usuario vrfUsuario) {
		this.vrfUsuario = vrfUsuario;
	}

	public PersonaDatosDto getVrfDocente() {
		return vrfDocente;
	}

	public void setVrfDocente(PersonaDatosDto vrfDocente) {
		this.vrfDocente = vrfDocente;
	} 

	public Integer getVrfPeriodoBuscar() {
		return vrfPeriodoBuscar;
	}

	public void setVrfPeriodoBuscar(Integer vrfPeriodoBuscar) {
		this.vrfPeriodoBuscar = vrfPeriodoBuscar;
	} 

	public String getVrfLinkReporte() {
		return vrfLinkReporte;
	}

	public void setVrfLinkReporte(String vrfLinkReporte) {
		this.vrfLinkReporte = vrfLinkReporte;
	}

	public PersonaDatosDto getVrfPersonaDtoSeleccion() {
		return vrfPersonaDtoSeleccion;
	}

	public void setVrfPersonaDtoSeleccion(PersonaDatosDto vrfPersonaDtoSeleccion) {
		this.vrfPersonaDtoSeleccion = vrfPersonaDtoSeleccion;
	}

	public List<ContenidoEvaluacionDatosDto> getVrfListaEvaluacionesDto() {
		return vrfListaEvaluacionesDto;
	}

	public void setVrfListaEvaluacionesDto(List<ContenidoEvaluacionDatosDto> vrfListaEvaluacionesDto) {
		this.vrfListaEvaluacionesDto = vrfListaEvaluacionesDto;
	}

	public List<PeriodoAcademico> getVrfListaPeriodos() {
		return vrfListaPeriodos;
	}

	public void setVrfListaPeriodos(List<PeriodoAcademico> vrfListaPeriodos) {
		this.vrfListaPeriodos = vrfListaPeriodos;
	}

	public Integer getVrfActivarReporte() {
		return vrfActivarReporte;
	}

	public void setVrfActivarReporte(Integer vrfActivarReporte) {
		this.vrfActivarReporte = vrfActivarReporte;
	}

	public String getVrfNombreReporte() {
		return vrfNombreReporte;
	}

	public void setVrfNombreReporte(String vrfNombreReporte) {
		this.vrfNombreReporte = vrfNombreReporte;
	}

	public String getVrfNombreJasper() {
		return vrfNombreJasper;
	}

	public void setVrfNombreJasper(String vrfNombreJasper) {
		this.vrfNombreJasper = vrfNombreJasper;
	}

	public UsuarioRol getVrfUsroEvaluadoCoord() {
		return vrfUsroEvaluadoCoord;
	}

	public void setVrfUsroEvaluadoCoord(UsuarioRol vrfUsroEvaluadoCoord) {
		this.vrfUsroEvaluadoCoord = vrfUsroEvaluadoCoord;
	}
	
	

}