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
   
 ARCHIVO:     EstudianteReingresoForm.java	  
 DESCRIPCION: Bean de sesion que maneja los reingresos de los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11/03/2018 			Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSecretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSecretariaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteReingresoForm.
 * Managed Bean de sesion que maneja los reingresos de los estudiantes. 
 * @author dalbuja.
 * @version 1.0
 */


@ManagedBean(name="estudianteReingresoForm")
@SessionScoped
public class EstudianteReingresoForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario erfUsuario;
	
	
	//PARA BUSQUEDA
	private EstudianteJdbcDto erfEstudianteBuscar;
	private List<CarreraDto> erfListCarreraBusq;
	private List<EstudianteJdbcDto> erfListEstudianteBusq;
	private List<NivelDto> erfListNivelBusq;
	private List<PeriodoAcademicoDto> erfListPeriodoAcademicoBusq;
	private List<ParaleloDto> erfListParaleloBusq;
	private NivelDto erfNivelDtoBusq;
	private ParaleloDto erfParaleloDtoBusq;
	
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto erfEstudianteVer;
	private String erfActivacion;
	private int erfActivadorReporte;

	//PARA EDITAR DATOS DEL ESTUDIANTE
	private EstudianteJdbcDto erfEstudianteEditar;

	//AUXILIARES
	private int erfClickModalEditarEstudiante;
	private boolean erfHomologacionSi;
	private boolean erfReingresoSi;
	
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
			
	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	private CarreraDtoServicioJdbc servErfCarreraDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servErfEstudianteDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servErfNivelDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servErfUbicacionDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servErfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servErfParaleloDtoServicioJdbc;
	@EJB
	private UsuarioRolServicio servErfUsuarioRolServicio;
	@EJB
	private InstitucionAcademicaDtoServicioJdbc servErfInstitucionAcademiacaDtoServicioJdbc;
	@EJB
	private TituloDtoServicioJdbc servErfTituloDtoServicioJdbc;
	@EJB
	private EtniaServicio servErfEtniaServicio;
	@EJB
	private PersonaServicio servErfPersonaServicio;
	@EJB
	private RolFlujoCarreraServicio servErfRolFlujoCarreraServicio;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR AL ESTUDIANTE
		/**
		 * Dirige la navegacion hacia la pagina de listarEstudiante
		 */
		public String irAlistarEstudianteReingreso(Usuario usuario){
			erfUsuario = usuario;
			erfActivadorReporte = 0;
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			try {
				erfListPeriodoAcademicoBusq = servErfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			} catch (PeriodoAcademicoDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
				return null;
			} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
				return null;
			}
			return "irListarEstudianteReingreso";
		}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		erfEstudianteBuscar = new EstudianteJdbcDto();
		erfListCarreraBusq = null;
		erfListEstudianteBusq = null;
		erfListNivelBusq = null;
		return "irInicio";
	}
	
	public void nada(){
		erfActivacion = "true";
		erfActivadorReporte = 1;
		try {
			ReporteSecretariaForm.generarReporteSecretaria(erfListEstudianteBusq, erfEstudianteBuscar.getPrlId());
			
		} catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		erfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		erfListEstudianteBusq = null;
		erfActivacion = "true";
		try {
//			if(efEstudianteBuscar.getPracId() != GeneralesConstantes.APP_ID_BASE 
//					&& efEstudianteBuscar.getCrrId() != GeneralesConstantes.APP_ID_BASE 
//					&& efEstudianteBuscar.getNvlId() != GeneralesConstantes.APP_ID_BASE){
//				efActivacion = "false";
//				efListEstudianteBusq = servEfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodocarreraNivel(efEstudianteBuscar.getPracId(), efEstudianteBuscar.getCrrId(),efEstudianteBuscar.getNvlId(),
//						efEstudianteBuscar.getPrlId(),efEstudianteBuscar.getPrsIdentificacion(),efEstudianteBuscar.getPrsPrimerApellido());
//				try {
//					reporte.generarReporteSecretaria(efListEstudianteBusq, efEstudianteBuscar.getPrlId());
//				} catch (Exception e) {
//					FacesUtil.mensajeError(e.getMessage());
//				}
////				efListEstudianteBusq =  servEfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodocarreraNivelTituloApPaternoIndetificacionAlterno(efEstudianteBuscar.getPracId(), efEstudianteBuscar.getCrrId(),efEstudianteBuscar.getNvlId(), efEstudianteBuscar.getPrsPrimerApellido(),efEstudianteBuscar.getPrsIdentificacion());
//			}else{
//				iniciarParametros();
//				FacesUtil.mensajeError("Debe seleccionar los párametros de búsqueda");
//			}
			List<RolFlujoCarrera> listaCarreras = servErfRolFlujoCarreraServicio.buscarPorIdUsuario(erfUsuario);
				//BUSCO LA LISTA DE ESTUDIANTES POR APELLIDO PATERNO , IDENTIFICACIÓN
				erfListEstudianteBusq = servErfEstudianteDtoServicioJdbc.buscarEstudianteReingresoxIdentificacionXApellido( 
						erfEstudianteBuscar.getPrsIdentificacion(),erfEstudianteBuscar.getPrsPrimerApellido(),listaCarreras);
				//HABILITO BOTON IMPRIMIR
				if(erfListEstudianteBusq.size() > 0 ){ //si existe por lo menos uno se activa
					erfActivacion = "false";
				}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.no.encontrado.exception")));
		} catch (RolFlujoCarreraNoEncontradoException e) {
		} catch (RolFlujoCarreraException e) {
		}
	}
	
	
	
	/**
	 * Método que permite buscar la carrera por el periódo academico
	 * @param idPeriodo -  idPeriodo seleccionado para la busqueda
	 */
	public void llenarCarrera(int idPeriodo){
		idPeriodo = erfEstudianteBuscar.getPracId();
		erfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		erfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		erfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		erfListCarreraBusq = null;
		erfListNivelBusq = null;
		erfListParaleloBusq = null;
		erfListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
					erfListCarreraBusq = servErfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(erfUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.carrera.id.periodo.validacion.exception")));
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.carrera.id.periodo.validacion.no.encontrado.exception")));
		}
		
	}
	
	/**
	 * Método que permite buscar la lista de niveles por el id de la carrera
	 * @param idCarrera - idCarrera seleccionado para la búsqueda
	 */
	public void llenarNivel(int idCarrera){
		idCarrera = erfEstudianteBuscar.getCrrId();
		erfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		erfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		erfListNivelBusq = null;
		erfListParaleloBusq = null;
		erfListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS NIVELES
					erfListNivelBusq = servErfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.nivel.id.carrera.validacion.exception")));
			}
		} catch (NivelDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.nivel.id.carrera.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que permite buscsar la lista de paralelos por el id de carrera y el id de nivel
	 * @param idCarrera - idCarrera seleccionada para la búsqueda
	 * @param idNivel - idNivel seleccionada para la búsqueda
	 */
	public void llenarParalelo(int idPeriodo, int idCarrera, int idNivel){
		idPeriodo = erfEstudianteBuscar.getPracId();
		idCarrera = erfEstudianteBuscar.getCrrId();
		idNivel = erfEstudianteBuscar.getNvlId();
		erfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		erfListParaleloBusq = null;
		erfListEstudianteBusq = null;
		
//		efParaleloDtoBusq = new ParaleloDto();
//		efParaleloDtoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		efListParaleloBusq = null;
		
		try{
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				//Lista de paralelos
				erfListParaleloBusq = servErfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(idPeriodo, idCarrera, idNivel);
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.paralelo.id.periodo.carrera.nivel.validacion.exception")));
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.paralelo.id.periodo.carrera.nivel.validacion.no.encontrado.exception")));
		}
		
	}
	
	
	/**
	 * Redirecciona a la pagina de ver datos completos del estudiante
	 * @return Navegacion a la página de visualización de datos del estudiante.
	 */
	public String verDatosEstudiante(EstudianteJdbcDto estudiante) {
		String retorno = null;
		 iniciarParametrosVerEstudiante();
			//DATOS DE ESTUDIANTE 
			erfEstudianteVer = estudiante;
			if(erfEstudianteVer != null){
				erfHomologacionSi=false;
				erfReingresoSi=false;
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.validacion.exception")));
			}
			retorno = "irVerDatosEstudiante";
		return retorno;
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		//ANULO LA LISTA DE ESTUDIANTES
		erfListEstudianteBusq = null;
		//ANULO LA LISTA DE NIVEL
		erfListNivelBusq = null;
		//ANULO LA LISTA DE CARRERAS
		erfListCarreraBusq = null;
		//ANULO LA LISTA DE PARALELOS
		erfListParaleloBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		erfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO
		erfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		erfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL
		erfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO
		erfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL DTO DE NIVEL BUSCAR
		erfNivelDtoBusq = new NivelDto();
		//INICIALIZO EL NIVEL ID
		erfNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		erfEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		erfEstudianteBuscar.setPrsPrimerApellido("");
		erfActivacion = "true";
		ClickCerrarModalEditarEstudiante();
	}
	
	//INICIAR PARÁMETROS DE VER ESTUDIANTE
	public void iniciarParametrosVerEstudiante(){
	erfEstudianteVer= null;
	}
	
	//INICIAR PARÁMETROS DE EDITAR ESTUDIANTE
	public void iniciarParametrosEditarEstudiante() {
		erfEstudianteEditar = null;
	}
	
	/**
	 * Método que permite buscar la lista estudiantes sin limpiar los filtros 
	 *
	 */
	public String regresaAlistarEstudiante(Usuario usuario) {
		String retorno;
		erfHomologacionSi = false;
		erfReingresoSi = false;
		erfUsuario = usuario;
		iniciarParametrosVerEstudiante();
		iniciarParametrosEditarEstudiante();
		ClickCerrarModalEditarEstudiante();
		retorno = "irListarEstudiante";

		return retorno;
	}
	
	
	
	/**
	 * Método para verificar campos llenos antes de activar modal editar estudiante
	 */
	public void VerificaModalEditarEstudiante() {
		
	    if (erfEstudianteEditar.getEtnId() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.estado.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la autodefinición étnica del estudiante");
	    } else if (erfEstudianteEditar.getPrsSexo() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.tipo.materia.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el sexo del estudiante");
	    } else if (erfEstudianteEditar.getPrsEstadoCivil() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.campo.formacion.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el estado civil del estudiante");
	    }else {
			// habilita Modal Modificar
			ClickModalEditarEstudiante();
	}
	}
	
	/**
	 * Método para actualizar la información del estudiante en la BDD
	 * @return navegación al xhtml listarEstudiante
	 */
	public String guardarEstudianteEditado() {
		String retorno = null;
	
				
		return retorno;
	}

	/**
	 * Método para activar Modal modificar estudiante
	 */
	public void ClickModalEditarEstudiante() {
		erfClickModalEditarEstudiante = 1; // habilita modal modificar
	}

	/**
	 * Método para desactivar Modal modificar estudiante
	 */
	public void ClickCerrarModalEditarEstudiante() {
		erfClickModalEditarEstudiante = 0; // cerrer modal modificar
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	

	public Usuario getErfUsuario() {
		return erfUsuario;
	}

	public void setErfUsuario(Usuario erfUsuario) {
		this.erfUsuario = erfUsuario;
	}

	public EstudianteJdbcDto getErfEstudianteBuscar() {
		return erfEstudianteBuscar;
	}

	public void setErfEstudianteBuscar(EstudianteJdbcDto erfEstudianteBuscar) {
		this.erfEstudianteBuscar = erfEstudianteBuscar;
	}

	public List<CarreraDto> getErfListCarreraBusq() {
		erfListCarreraBusq = erfListCarreraBusq == null ? (new ArrayList<CarreraDto>()) : erfListCarreraBusq;
		return erfListCarreraBusq;
	}

	public void setErfListCarreraBusq(List<CarreraDto> erfListCarreraBusq) {
		this.erfListCarreraBusq = erfListCarreraBusq;
	}

	public List<EstudianteJdbcDto> getErfListEstudianteBusq() {
		erfListEstudianteBusq = erfListEstudianteBusq == null ? (new ArrayList<EstudianteJdbcDto>()) : erfListEstudianteBusq;
		return erfListEstudianteBusq;
	}

	public void setErfListEstudianteBusq(List<EstudianteJdbcDto> erfListEstudianteBusq) {
		this.erfListEstudianteBusq = erfListEstudianteBusq;
	}

	public List<NivelDto> getErfListNivelBusq() {
		erfListNivelBusq = erfListNivelBusq == null ? (new ArrayList<NivelDto>()) : erfListNivelBusq;
		return erfListNivelBusq;
	}

	public void setErfListNivelBusq(List<NivelDto> erfListNivelBusq) {
		this.erfListNivelBusq = erfListNivelBusq;
	}

	public List<PeriodoAcademicoDto> getErfListPeriodoAcademicoBusq() {
		erfListPeriodoAcademicoBusq = erfListPeriodoAcademicoBusq == null ? (new ArrayList<PeriodoAcademicoDto>()) : erfListPeriodoAcademicoBusq;
		return erfListPeriodoAcademicoBusq;
	}

	public void setErfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> erfListPeriodoAcademicoBusq) {
		this.erfListPeriodoAcademicoBusq = erfListPeriodoAcademicoBusq;
	}

	public List<ParaleloDto> getErfListParaleloBusq() {
		erfListParaleloBusq = erfListParaleloBusq == null ? (new ArrayList<ParaleloDto>()) : erfListParaleloBusq;
		return erfListParaleloBusq;
	}

	public void setErfListParaleloBusq(List<ParaleloDto> erfListParaleloBusq) {
		this.erfListParaleloBusq = erfListParaleloBusq;
	}

	public NivelDto getErfNivelDtoBusq() {
		
		return erfNivelDtoBusq;
	}

	public void setErfNivelDtoBusq(NivelDto erfNivelDtoBusq) {
		this.erfNivelDtoBusq = erfNivelDtoBusq;
	}

	public ParaleloDto getErfParaleloDtoBusq() {
		return erfParaleloDtoBusq;
	}

	public void setErfParaleloDtoBusq(ParaleloDto erfParaleloDtoBusq) {
		this.erfParaleloDtoBusq = erfParaleloDtoBusq;
	}

	public EstudianteJdbcDto getErfEstudianteVer() {
		return erfEstudianteVer;
	}

	public void setErfEstudianteVer(EstudianteJdbcDto erfEstudianteVer) {
		this.erfEstudianteVer = erfEstudianteVer;
	}

	public String getErfActivacion() {
		return erfActivacion;
	}

	public void setErfActivacion(String erfActivacion) {
		this.erfActivacion = erfActivacion;
	}

	public int getErfActivadorReporte() {
		return erfActivadorReporte;
	}

	public void setErfActivadorReporte(int erfActivadorReporte) {
		this.erfActivadorReporte = erfActivadorReporte;
	}

	public EstudianteJdbcDto getErfEstudianteEditar() {
		return erfEstudianteEditar;
	}

	public void setErfEstudianteEditar(EstudianteJdbcDto erfEstudianteEditar) {
		this.erfEstudianteEditar = erfEstudianteEditar;
	}

	public int getErfClickModalEditarEstudiante() {
		return erfClickModalEditarEstudiante;
	}

	public void setErfClickModalEditarEstudiante(int erfClickModalEditarEstudiante) {
		this.erfClickModalEditarEstudiante = erfClickModalEditarEstudiante;
	}

	public boolean isErfHomologacionSi() {
		return erfHomologacionSi;
	}

	public void setErfHomologacionSi(boolean erfHomologacionSi) {
		this.erfHomologacionSi = erfHomologacionSi;
	}

	public boolean isErfReingresoSi() {
		return erfReingresoSi;
	}

	public void setErfReingresoSi(boolean erfReingresoSi) {
		this.erfReingresoSi = erfReingresoSi;
	}

	
	
}
