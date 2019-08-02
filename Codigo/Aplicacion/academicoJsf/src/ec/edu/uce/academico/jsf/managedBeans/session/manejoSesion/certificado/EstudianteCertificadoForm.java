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
   
 ARCHIVO:     EstudianteCertificadoForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-JUN-2017 			Vinicio Rosales                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.certificado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCertificadoMatriculaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteCertificadoForm.
 * Managed Bean que administra los estudiantes para la visualización de los certificados de matricula.
 * @author jvrosales.
 * @version 1.0
 */


@ManagedBean(name="estudianteCertificadoForm")
@SessionScoped
public class EstudianteCertificadoForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario ecfUsuario;
	
	//PARA BUSQUEDA
	private EstudianteJdbcDto ecfEstudianteBuscar;
	private List<CarreraDto> ecfListCarreraBusq;
	private List<EstudianteJdbcDto> ecfListEstudianteBusq;
	private List<EstudianteJdbcDto> ecflistEstudianteReporteBusq;
	private List<NivelDto> ecfListNivelBusq;
	private List<PeriodoAcademicoDto> ecfListPeriodoAcademicoBusq;
	private List<ParaleloDto> ecfListParaleloBusq;
	private NivelDto ecfNivelDtoBusq;
	private ParaleloDto ecfParaleloDtoBusq;
	private String ecfTipoCarrera;
	private Integer ecfTipoUsuario;
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto ecfEstudianteVer;
	private UbicacionDto ecfCantonResidenciaVer;
	private UbicacionDto ecfPaisResidenciaVer;
	private UbicacionDto ecfProvinciaResidenciaVer;
	private String ecfActivacion;
	private int ecfActivadorReporte;
	//AUMENTO JVROSALES pero debemos modificarle es solo por hacer rapido
	private Carrera ecfDependenciaBuscar;
	private PersonaDto ecfSecretarioBuscar;
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB	private CarreraDtoServicioJdbc servEcfCarreraDtoServicioJdbc;
	@EJB	private EstudianteDtoServicioJdbc servEcfEstudianteDtoServicioJdbc;
	@EJB	private NivelDtoServicioJdbc servEcfNivelDtoServicioJdbc;
	@EJB	private UbicacionDtoServicioJdbc servEcfUbicacionDtoServicioJdbc;
	@EJB	private PeriodoAcademicoDtoServicioJdbc servEcfPeriodoAcademicoDtoServicioJdbc;
	@EJB	private ParaleloDtoServicioJdbc servEcfParaleloDtoServicioJdbc;
	@EJB	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB	private CarreraServicio servCarreraServicio;
	@EJB	private PersonaDtoServicioJdbc servPersonaDto;
	@EJB    private CarreraDtoServicioJdbc servJdbcCarreraDto;

	
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiante(Usuario usuario){
		ecfUsuario = usuario;
		ecfActivadorReporte = 0;
		String retorno = null;
		List<UsuarioRol> usro  = null;

		try {
			usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ecfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					ecfTipoCarrera="carreras";
					break;
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ecfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					ecfTipoCarrera="programas";
					break;
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					ecfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
					ecfTipoCarrera="suficiencias";
					break;
				}
			}

			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			//LISTO LOS PERIODOS ACADEMICOS
			if(ecfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
				ecfListPeriodoAcademicoBusq = servEcfPeriodoAcademicoDtoServicioJdbc.listarTodos();
			}else if (ecfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ecfListPeriodoAcademicoBusq = servEcfPeriodoAcademicoDtoServicioJdbc.listarTodosPosgrado();
			}else if(ecfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				if (usro != null && usro.size() > 0) {
					for (UsuarioRol item : usro) {
						cargarCarrerasAsignadasASecretaria(item);
					}
				}
				
				if (ecfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					ecfListPeriodoAcademicoBusq = servEcfPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE);
				}else if (ecfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
					ecfListPeriodoAcademicoBusq = servEcfPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				}else if (ecfTipoUsuario == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
					ecfListPeriodoAcademicoBusq = servEcfPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE);
				}

			}
			
			retorno = "irListarEstudianteCertificado";

		} catch (UsuarioRolNoEncontradoException  e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch ( UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
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
		ecfEstudianteBuscar = new EstudianteJdbcDto();
		ecfListCarreraBusq = null;
		ecfListEstudianteBusq = null;
		ecflistEstudianteReporteBusq = null;
		ecfListNivelBusq = null;
		return "irInicio";
	}
	
	public void nada(EstudianteJdbcDto estudiante){
		try {
		//GENERA EL REPORTE
		
//			ecflistEstudianteReporteBusq = servEcfEstudianteDtoServicioJdbc.buscarEstudianteXIdMatricula(estudiante.getFcmtId());
			
			ecflistEstudianteReporteBusq = servEcfEstudianteDtoServicioJdbc.listaMateriasXfcmtXidentificacion(estudiante.getPrsIdentificacion(),estudiante.getFcmtId());
			
			
			String usrNick = ecfUsuario.getUsrNick();
//			for (EstudianteJdbcDto item : ecflistEstudianteReporteBusq) {
//				
//			}
			ecfDependenciaBuscar = servCarreraServicio.buscarPorId(estudiante.getCrrId());
			
			ecfSecretarioBuscar = servPersonaDto.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE, ecfDependenciaBuscar.getCrrDependencia().getDpnId());
			ReporteCertificadoMatriculaForm.generarReporteCertificadoMatricula(ecflistEstudianteReporteBusq, usrNick, estudiante, ecfDependenciaBuscar, ecfSecretarioBuscar);
			ecfActivadorReporte = 1;
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		}catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.notas.validacion.no.encontrado.exception")));
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encuentra asigando el Secretario Abogado de la Facultad, comuniquese con el administrador del sistema");
		}
		
		
		
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		ecfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		ecfListEstudianteBusq = null;
		ecflistEstudianteReporteBusq = null;
		
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
			
			if(ecfEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.periodo.validacion.exception")));
			}else if(ecfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.carrera.validacion.exception")));
			}else if(ecfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.nivel.validacion.exception")));
			}else{
				ecfActivacion = "false";
				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				ecfListEstudianteBusq = servEcfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodocarreraNivel(ecfEstudianteBuscar.getPracId(), ecfEstudianteBuscar.getCrrId(),ecfEstudianteBuscar.getNvlId(),
//						ecfEstudianteBuscar.getPrlId(),ecfEstudianteBuscar.getPrsIdentificacion(),ecfEstudianteBuscar.getPrsPrimerApellido());
				
				ecfListEstudianteBusq = servEcfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXCarreraXNivelXIdentificacionXApellidoPagados(ecfEstudianteBuscar.getPracId(), ecfEstudianteBuscar.getCrrId(),ecfEstudianteBuscar.getNvlId(),
						ecfEstudianteBuscar.getPrsIdentificacion(),ecfEstudianteBuscar.getPrsPrimerApellido());
				
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
			ecfEstudianteVer = servEcfEstudianteDtoServicioJdbc.buscarEstudianteXId(estudiante.getPrsId());
			if(ecfEstudianteVer != null){
				ecfCantonResidenciaVer = servEcfUbicacionDtoServicioJdbc.buscarXId(ecfEstudianteVer.getUbcCantonId());
				UbicacionDto ubicacionAux =  null;
				try {
					ubicacionAux =  servEcfUbicacionDtoServicioJdbc.buscarPadreXId(ecfEstudianteVer.getUbcCantonId());
				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					ubicacionAux =  null;
				}
				//verfifica si es pais o canton
				if(ubicacionAux == null){//No es ecuador
					ecfPaisResidenciaVer = ecfCantonResidenciaVer;
					ecfProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					ecfProvinciaResidenciaVer.setUbcDescripcion("N/A");
					ecfCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					ecfCantonResidenciaVer.setUbcDescripcion("N/A");
				}else{
					ecfProvinciaResidenciaVer = servEcfUbicacionDtoServicioJdbc.buscarPadreXId(ecfCantonResidenciaVer.getUbcId());
					ecfPaisResidenciaVer = servEcfUbicacionDtoServicioJdbc.buscarPadreXId(ecfProvinciaResidenciaVer.getUbcId());
				}
			}else{
				FacesUtil.mensajeError("No se encontró la información del estudiante");
			}
			retorno = "irVerDatosEstudianteCertificado";
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
		idPeriodo = ecfEstudianteBuscar.getPracId();
		ecfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		ecfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		ecfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		ecfListCarreraBusq = null;
		ecfListNivelBusq = null;
		ecfListParaleloBusq = null;
		ecfListEstudianteBusq = null;
		ecflistEstudianteReporteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				if(ecfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ecfListCarreraBusq = servEcfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ecfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (ecfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ecfListCarreraBusq = servEcfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ecfUsuario.getUsrId(), RolConstantes.ROL_SECREPOSGRADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if(ecfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE)){
					ecfListCarreraBusq = servEcfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ecfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if(ecfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE)){
					ecfListCarreraBusq = servEcfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ecfUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if(ecfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE)){
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
		idCarrera = ecfEstudianteBuscar.getCrrId();
		ecfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		ecfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		ecfListNivelBusq = null;
		ecfListParaleloBusq = null;
		ecfListEstudianteBusq = null;
		ecflistEstudianteReporteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS NIVELES
				if(ecfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ecfListNivelBusq = servEcfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if (ecfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ecfListNivelBusq = servEcfNivelDtoServicioJdbc.listarNivelXCarreraPosgrado(idCarrera);
				}else if(ecfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE)){
					ecfListNivelBusq = servEcfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if(ecfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE)){
					ecfListNivelBusq = servEcfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if(ecfTipoUsuario.equals(DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE)){
					ecfListNivelBusq = servEcfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
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
		idPeriodo = ecfEstudianteBuscar.getPracId();
		idCarrera = ecfEstudianteBuscar.getCrrId();
		idNivel = ecfEstudianteBuscar.getNvlId();
		ecfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		ecfListParaleloBusq = null;
		ecfListEstudianteBusq = null;
		ecflistEstudianteReporteBusq = null;
		
//		efParaleloDtoBusq = new ParaleloDto();
//		efParaleloDtoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		efListParaleloBusq = null;
		
		try{
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				//Lista de paralelos
				ecfListParaleloBusq = servEcfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(idPeriodo, idCarrera, idNivel);
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
		ecfListEstudianteBusq = null;
		//ANULO LA LISTA DE ESTUDIANTE PARA EL REPORTE
		ecflistEstudianteReporteBusq = null;
		//ANULO LA LISTA DE NIVEL
		ecfListNivelBusq = null;
		//ANULO LA LISTA DE CARRERAS
		ecfListCarreraBusq = null;
		//ANULO LA LISTA DE PARALELOS
		ecfListParaleloBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		ecfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO
		ecfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		ecfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL
		ecfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO
		ecfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL DTO DE NIVEL BUSCAR
		ecfNivelDtoBusq = new NivelDto();
		//INICIALIZO EL NIVEL ID
		ecfNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		ecfEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		ecfEstudianteBuscar.setPrsPrimerApellido("");
		ecfSecretarioBuscar = new PersonaDto();
		ecfActivacion = "true";
	}
	
	
private void cargarCarrerasAsignadasASecretaria(UsuarioRol usro) {
		
		try {
			List<CarreraDto> carreras = servJdbcCarreraDto.buscarCarreras(usro.getUsroId());
			if (carreras != null && carreras.size() > 0) {

				for (CarreraDto it : carreras) {
					if (it.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE) {
						ecfTipoUsuario = DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE;
						break;
					}else if (it.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE) {
						ecfTipoUsuario = DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE;
						break;
					}else if (it.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE) {
						ecfTipoUsuario =  DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE;
						break;
					}
				}
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
		}
		
	}
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	public Usuario getEcfUsuario() {
		return ecfUsuario;
	}
	public void setEcfUsuario(Usuario ecfUsuario) {
		this.ecfUsuario = ecfUsuario;
	}
	public EstudianteJdbcDto getEcfEstudianteBuscar() {
		return ecfEstudianteBuscar;
	}
	public void setEcfEstudianteBuscar(EstudianteJdbcDto ecfEstudianteBuscar) {
		this.ecfEstudianteBuscar = ecfEstudianteBuscar;
	}
	public List<CarreraDto> getEcfListCarreraBusq() {
		ecfListCarreraBusq = ecfListCarreraBusq==null?(new ArrayList<CarreraDto>()):ecfListCarreraBusq;
		return ecfListCarreraBusq;
	}
	public void setEcfListCarreraBusq(List<CarreraDto> ecfListCarreraBusq) {
		this.ecfListCarreraBusq = ecfListCarreraBusq;
	}
	public List<EstudianteJdbcDto> getEcfListEstudianteBusq() {
		ecfListEstudianteBusq = ecfListEstudianteBusq==null?(new ArrayList<EstudianteJdbcDto>()):ecfListEstudianteBusq;
		return ecfListEstudianteBusq;
	}
	public void setEcfListEstudianteBusq(List<EstudianteJdbcDto> ecfListEstudianteBusq) {
		this.ecfListEstudianteBusq = ecfListEstudianteBusq;
	}
	public List<NivelDto> getEcfListNivelBusq() {
		ecfListNivelBusq = ecfListNivelBusq==null?(new ArrayList<NivelDto>()):ecfListNivelBusq;
		return ecfListNivelBusq;
	}
	public void setEcfListNivelBusq(List<NivelDto> ecfListNivelBusq) {
		this.ecfListNivelBusq = ecfListNivelBusq;
	}
	public List<PeriodoAcademicoDto> getEcfListPeriodoAcademicoBusq() {
		ecfListPeriodoAcademicoBusq = ecfListPeriodoAcademicoBusq==null?(new ArrayList<PeriodoAcademicoDto>()):ecfListPeriodoAcademicoBusq;
		return ecfListPeriodoAcademicoBusq;
	}
	public void setEcfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> ecfListPeriodoAcademicoBusq) {
		this.ecfListPeriodoAcademicoBusq = ecfListPeriodoAcademicoBusq;
	}
	public NivelDto getEfNivelDtoBusq() {
		return ecfNivelDtoBusq;
	}
	public void setEcfNivelDtoBusq(NivelDto ecfNivelDtoBusq) {
		this.ecfNivelDtoBusq = ecfNivelDtoBusq;
	}
	public EstudianteJdbcDto getEcfEstudianteVer() {
		return ecfEstudianteVer;
	}
	public void setEcfEstudianteVer(EstudianteJdbcDto ecfEstudianteVer) {
		this.ecfEstudianteVer = ecfEstudianteVer;
	}
	public UbicacionDto getEcfCantonResidenciaVer() {
		return ecfCantonResidenciaVer;
	}
	public void setEcfCantonResidenciaVer(UbicacionDto ecfCantonResidenciaVer) {
		this.ecfCantonResidenciaVer = ecfCantonResidenciaVer;
	}
	public UbicacionDto getEcfPaisResidenciaVer() {
		return ecfPaisResidenciaVer;
	}
	public void setEcfPaisResidenciaVer(UbicacionDto ecfPaisResidenciaVer) {
		this.ecfPaisResidenciaVer = ecfPaisResidenciaVer;
	}
	public UbicacionDto getEcfProvinciaResidenciaVer() {
		return ecfProvinciaResidenciaVer;
	}
	public void setEcfProvinciaResidenciaVer(UbicacionDto ecfProvinciaResidenciaVer) {
		this.ecfProvinciaResidenciaVer = ecfProvinciaResidenciaVer;
	}
	public String getEcfActivacion() {
		return ecfActivacion;
	}
	public void setEcfActivacion(String ecfActivacion) {
		this.ecfActivacion = ecfActivacion;
	}
	public List<ParaleloDto> getEcfListParaleloBusq() {
		ecfListParaleloBusq = ecfListParaleloBusq==null?(new ArrayList<ParaleloDto>()):ecfListParaleloBusq;
		return ecfListParaleloBusq;
	}
	public void setEcfListParaleloBusq(List<ParaleloDto> ecfListParaleloBusq) {
		this.ecfListParaleloBusq = ecfListParaleloBusq;
	}
	public ParaleloDto getEcfParaleloDtoBusq() {
		return ecfParaleloDtoBusq;
	}
	public void setEcfParaleloDtoBusq(ParaleloDto ecfParaleloDtoBusq) {
		this.ecfParaleloDtoBusq = ecfParaleloDtoBusq;
	}
	public List<EstudianteJdbcDto> getEcflistEstudianteReporteBusq() {
		return ecflistEstudianteReporteBusq;
	}
	public void setEcflistEstudianteReporteBusq(List<EstudianteJdbcDto> ecflistEstudianteReporteBusq) {
		this.ecflistEstudianteReporteBusq = ecflistEstudianteReporteBusq;
	}
	public String getEcfTipoCarrera() {
		return ecfTipoCarrera;
	}
	public void setEcfTipoCarrera(String ecfTipoCarrera) {
		this.ecfTipoCarrera = ecfTipoCarrera;
	}
	public Integer getEcfTipoUsuario() {
		return ecfTipoUsuario;
	}
	public void setEcfTipoUsuario(Integer ecfTipoUsuario) {
		this.ecfTipoUsuario = ecfTipoUsuario;
	}
	public int getEcfActivadorReporte() {
		return ecfActivadorReporte;
	}
	public void setEcfActivadorReporte(int ecfActivadorReporte) {
		this.ecfActivadorReporte = ecfActivadorReporte;
	}
	public Carrera getEcfDependenciaBuscar() {
		return ecfDependenciaBuscar;
	}
	public void setEcfDependenciaBuscar(Carrera ecfDependenciaBuscar) {
		this.ecfDependenciaBuscar = ecfDependenciaBuscar;
	}
	public PersonaDto getEcfSecretarioBuscar() {
		return ecfSecretarioBuscar;
	}
	public void setEcfSecretarioBuscar(PersonaDto ecfSecretarioBuscar) {
		this.ecfSecretarioBuscar = ecfSecretarioBuscar;
	}
	
}
