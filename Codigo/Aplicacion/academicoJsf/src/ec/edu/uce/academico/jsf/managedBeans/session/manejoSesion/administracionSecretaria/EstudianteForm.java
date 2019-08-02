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
   
 ARCHIVO:     EstudianteForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 09-MARZ-2017 			Dennis Collaguazo                     Emisión Inicial
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
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
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
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSecretariaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteForm.
 * Managed Bean que administra los estudiantes para la visualización de matriculados.
 * @author dcollaguazo.
 * @version 1.0
 */


@ManagedBean(name="estudianteForm")
@SessionScoped
public class EstudianteForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario efUsuario;
	private String efTipoCarrera;
	private Integer efTipoUsuario;
	
	//PARA BUSQUEDA
	private EstudianteJdbcDto efEstudianteBuscar;
	private List<CarreraDto> efListCarreraBusq;
	private List<EstudianteJdbcDto> efListEstudianteBusq;
	private List<NivelDto> efListNivelBusq;
	private List<PeriodoAcademicoDto> efListPeriodoAcademicoBusq;
	private List<ParaleloDto> efListParaleloBusq;
	private NivelDto efNivelDtoBusq;
	private ParaleloDto efParaleloDtoBusq;
	
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto efEstudianteVer;
	private UbicacionDto efCantonResidenciaVer;
	private UbicacionDto efPaisResidenciaVer;
	private UbicacionDto efProvinciaResidenciaVer;
	private String efActivacion;
	private int ecfActivadorReporte;
	
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
	private CarreraDtoServicioJdbc servEfCarreraDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servEfEstudianteDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servEfNivelDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servEfUbicacionDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servEfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servEfParaleloDtoServicioJdbc;
	@EJB
	private UsuarioRolServicio servEfUsuarioRolServicio;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudianteLibro(Usuario usuario){
		efUsuario = usuario;
		ecfActivadorReporte = 0;
		String retorno = null;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			try {
				List<UsuarioRol> usro = servEfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
				for (UsuarioRol item : usro) {
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						efTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
						efTipoCarrera="carreras";
					}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
						efTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
						efTipoCarrera="programas";
					}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
						efTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
						efTipoCarrera="suficiencias";
					}
				}
			} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			}
			//LISTO LOS PERIODOS ACADEMICOS
			if(efTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				efListPeriodoAcademicoBusq = servEfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			}else if (efTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				efListPeriodoAcademicoBusq = servEfPeriodoAcademicoDtoServicioJdbc.listarTodosPosgrado();		
			}else if(efTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				efListPeriodoAcademicoBusq = servEfPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
			}

			//LISTO LAS CARRERAS ASIGNADAS AL ROL QUE ENTRO A LA FUNCIONALIDAD
			//efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFl(usuario.getUsrId(), RolConstantes.ROL_BD_SECRECARRERA, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			//LISTO TODOS LOS NIVELES DE LAS MALLAS CURRICULARES
			//efListNivelBusq = servEfNivelDtoServicioJdbc.listarTodos();
			retorno = "irListarEstudianteLibro";
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		efEstudianteBuscar = new EstudianteJdbcDto();
		efListCarreraBusq = null;
		efListEstudianteBusq = null;
		efListNivelBusq = null;
		return "irInicio";
	}
	
	public void nada(){
		efActivacion = "true";
		ecfActivadorReporte = 1;
		try {
			ReporteSecretariaForm.generarReporteSecretariaLibro(efListEstudianteBusq, efEstudianteBuscar.getPrlId());
			
		} catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		efEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		efListEstudianteBusq = null;
		efActivacion = "true";
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

			if(efEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.periodo.validacion.exception")));
			}else if(efEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.carrera.validacion.exception")));
			}else if(efEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.nivel.validacion.exception")));
			}else{
				//				efActivacion = "false";
				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
				efListEstudianteBusq = servEfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXCarreraXNivel(efEstudianteBuscar.getPracId(), efEstudianteBuscar.getCrrId(),efEstudianteBuscar.getNvlId());
				//HABILITO BOTON IMPRIMIR
				if(efListEstudianteBusq.size() > 0 ){ //si existe por lo menos uno se activa
					efActivacion = "false";
				}

			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.no.encontrado.exception")));
		}
	}
	
	/**
	 * Redirecciona a la pagina de ver datos completos del estudiante
	 * @return Navegacion a la página de visualización de datos del estudiante.
	 */
	public String verDatosEstudiante(EstudianteJdbcDto estudiante) {
		String retorno = null;
		try {
			//DATOS DE ESTUDIANTE
			efEstudianteVer = servEfEstudianteDtoServicioJdbc.buscarEstudianteXId(estudiante.getPrsId());
			if(efEstudianteVer != null){
				efCantonResidenciaVer = servEfUbicacionDtoServicioJdbc.buscarXId(efEstudianteVer.getUbcCantonId());
				UbicacionDto ubicacionAux =  null;
				try {
					ubicacionAux =  servEfUbicacionDtoServicioJdbc.buscarPadreXId(efEstudianteVer.getUbcCantonId());
				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					ubicacionAux =  null;
				}
				//verfifica si es pais o canton
				if(ubicacionAux == null){//No es ecuador
					efPaisResidenciaVer = efCantonResidenciaVer;
					efProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					efProvinciaResidenciaVer.setUbcDescripcion("N/A");
					efCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					efCantonResidenciaVer.setUbcDescripcion("N/A");
				}else{
					efProvinciaResidenciaVer = servEfUbicacionDtoServicioJdbc.buscarPadreXId(efCantonResidenciaVer.getUbcId());
					efPaisResidenciaVer = servEfUbicacionDtoServicioJdbc.buscarPadreXId(efProvinciaResidenciaVer.getUbcId());
				}
			}else{
				FacesUtil.mensajeError("No se encontró la información del estudiante");
			}
			retorno = "irVerDatosEstudiante";
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.no.encontrado.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.ubicacion.no.encontrado.exception")));
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.ubicacion.exception")));
		}
		return retorno;
	}
	
	/**
	 * Método que permite buscar la carrera por el periódo academico
	 * @param idPeriodo -  idPeriodo seleccionado para la busqueda
	 */
	public void llenarCarrera(int idPeriodo){
		idPeriodo = efEstudianteBuscar.getPracId();
		efEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		efEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		efEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		efListCarreraBusq = null;
		efListNivelBusq = null;
		efListParaleloBusq = null;
		efListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				if(efTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(efUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (efTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(efUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				} else if(efTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(efUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}
				
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
		idCarrera = efEstudianteBuscar.getCrrId();
		efEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		efEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		efListNivelBusq = null;
		efListParaleloBusq = null;
		efListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS NIVELES
				if(efTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					efListNivelBusq = servEfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if (efTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					efListNivelBusq = servEfNivelDtoServicioJdbc.listarNivelXCarreraPosgrado(idCarrera);
				}else if(efTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					efListNivelBusq = servEfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}
				
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
		idPeriodo = efEstudianteBuscar.getPracId();
		idCarrera = efEstudianteBuscar.getCrrId();
		idNivel = efEstudianteBuscar.getNvlId();
		efEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		efListParaleloBusq = null;
		efListEstudianteBusq = null;
		
//		efParaleloDtoBusq = new ParaleloDto();
//		efParaleloDtoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		efListParaleloBusq = null;
		
		try{
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				//Lista de paralelos
				efListParaleloBusq = servEfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(idPeriodo, idCarrera, idNivel);
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.paralelo.id.periodo.carrera.nivel.validacion.exception")));
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.paralelo.id.periodo.carrera.nivel.validacion.no.encontrado.exception")));
		}
		
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		//ANULO LA LISTA DE ESTUDIANTES
		efListEstudianteBusq = null;
		//ANULO LA LISTA DE NIVEL
		efListNivelBusq = null;
		//ANULO LA LISTA DE CARRERAS
		efListCarreraBusq = null;
		//ANULO LA LISTA DE PARALELOS
		efListParaleloBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		efEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO
		efEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		efEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL
		efEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO
		efEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL DTO DE NIVEL BUSCAR
		efNivelDtoBusq = new NivelDto();
		//INICIALIZO EL NIVEL ID
		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		efEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		efEstudianteBuscar.setPrsPrimerApellido("");
		efActivacion = "true";
	}
	
	
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	public Usuario getEfUsuario() {
		return efUsuario;
	}
	public void setEfUsuario(Usuario efUsuario) {
		this.efUsuario = efUsuario;
	}
	public EstudianteJdbcDto getEfEstudianteBuscar() {
		return efEstudianteBuscar;
	}
	public void setEfEstudianteBuscar(EstudianteJdbcDto efEstudianteBuscar) {
		this.efEstudianteBuscar = efEstudianteBuscar;
	}
	public List<CarreraDto> getEfListCarreraBusq() {
		efListCarreraBusq = efListCarreraBusq==null?(new ArrayList<CarreraDto>()):efListCarreraBusq;
		return efListCarreraBusq;
	}
	public void setEfListCarreraBusq(List<CarreraDto> efListCarreraBusq) {
		this.efListCarreraBusq = efListCarreraBusq;
	}
	public List<EstudianteJdbcDto> getEfListEstudianteBusq() {
		efListEstudianteBusq = efListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):efListEstudianteBusq;
		return efListEstudianteBusq;
	}
	public void setEfListEstudianteBusq(List<EstudianteJdbcDto> efListEstudianteBusq) {
		this.efListEstudianteBusq = efListEstudianteBusq;
	}
	public List<NivelDto> getEfListNivelBusq() {
		efListNivelBusq = efListNivelBusq==null?(new ArrayList<NivelDto>()):efListNivelBusq;
		return efListNivelBusq;
	}
	public void setEfListNivelBusq(List<NivelDto> efListNivelBusq) {
		this.efListNivelBusq = efListNivelBusq;
	}
	public List<PeriodoAcademicoDto> getEfListPeriodoAcademicoBusq() {
		efListPeriodoAcademicoBusq = efListPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademicoDto>()):efListPeriodoAcademicoBusq;
		return efListPeriodoAcademicoBusq;
	}
	public void setEfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> efListPeriodoAcademicoBusq) {
		this.efListPeriodoAcademicoBusq = efListPeriodoAcademicoBusq;
	}
	public NivelDto getEfNivelDtoBusq() {
		return efNivelDtoBusq;
	}
	public void setEfNivelDtoBusq(NivelDto efNivelDtoBusq) {
		this.efNivelDtoBusq = efNivelDtoBusq;
	}
	public EstudianteJdbcDto getEfEstudianteVer() {
		return efEstudianteVer;
	}
	public void setEfEstudianteVer(EstudianteJdbcDto efEstudianteVer) {
		this.efEstudianteVer = efEstudianteVer;
	}
	public UbicacionDto getEfCantonResidenciaVer() {
		return efCantonResidenciaVer;
	}
	public void setEfCantonResidenciaVer(UbicacionDto efCantonResidenciaVer) {
		this.efCantonResidenciaVer = efCantonResidenciaVer;
	}
	public UbicacionDto getEfPaisResidenciaVer() {
		return efPaisResidenciaVer;
	}
	public void setEfPaisResidenciaVer(UbicacionDto efPaisResidenciaVer) {
		this.efPaisResidenciaVer = efPaisResidenciaVer;
	}
	public UbicacionDto getEfProvinciaResidenciaVer() {
		return efProvinciaResidenciaVer;
	}
	public void setEfProvinciaResidenciaVer(UbicacionDto efProvinciaResidenciaVer) {
		this.efProvinciaResidenciaVer = efProvinciaResidenciaVer;
	}
	public String getEfActivacion() {
		return efActivacion;
	}
	public void setEfActivacion(String efActivacion) {
		this.efActivacion = efActivacion;
	}
	public List<ParaleloDto> getEfListParaleloBusq() {
		efListParaleloBusq = efListParaleloBusq==null?(new ArrayList<ParaleloDto>()):efListParaleloBusq;
		return efListParaleloBusq;
	}
	public void setEfListParaleloBusq(List<ParaleloDto> efListParaleloBusq) {
		this.efListParaleloBusq = efListParaleloBusq;
	}
	public ParaleloDto getEfParaleloDtoBusq() {
		return efParaleloDtoBusq;
	}
	public void setEfParaleloDtoBusq(ParaleloDto efParaleloDtoBusq) {
		this.efParaleloDtoBusq = efParaleloDtoBusq;
	}
	public String getEfTipoCarrera() {
		return efTipoCarrera;
	}
	public void setEfTipoCarrera(String efTipoCarrera) {
		this.efTipoCarrera = efTipoCarrera;
	}
	public Integer getEfTipoUsuario() {
		return efTipoUsuario;
	}
	public void setEfTipoUsuario(Integer efTipoUsuario) {
		this.efTipoUsuario = efTipoUsuario;
	}
	public int getEcfActivadorReporte() {
		return ecfActivadorReporte;
	}
	public void setEcfActivadorReporte(int ecfActivadorReporte) {
		this.ecfActivadorReporte = ecfActivadorReporte;
	}
	
}
