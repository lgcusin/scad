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
   
 ARCHIVO:     ReporteTercerasMatriculasCompletoForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones del reporte  de tercerasMatriculas.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
19-MARZO-2019			Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SolicitudTerceraMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) ReporteTercerasMatriculasCompletoForm
 * Managed Bean que maneja las peticiones del reporte de tercerasMatriculas.
 * @author lmquishpei.
 * @version 1.0
 */

@ManagedBean(name="reporteTercerasMatriculasCompletoForm")
@SessionScoped
public class ReporteTercerasMatriculasCompletoForm implements Serializable{

	private static final long serialVersionUID = -5044772886269795627L;
	
	//********************************************************************/
	//******************* ATRIBUTOS DE LA CLASE **************************/
	//********************************************************************/
	//PARA VER
	private Usuario rtmfUsuario;
	private PeriodoAcademico rtmfPeriodoAcademico;
	private CarreraDto rtmfCarreraDto ;
	private String rtmfCedula;
	private String rtmfApellido;
	private String rtmfTipoCarrera;
	private Integer rtmfTipoUsuario;
	private List<PeriodoAcademico> rtmfListPeriodoAcademico;
	private List<CarreraDto> rtmfListCarreraDto;
	private List<EstudianteJdbcDto> rtmfListEstudianteTercerasMatriculas;
	private List<Dependencia> rtmfListDependencia;
	private boolean rtmfSoporte;
	
	//PARA DESCARGAR ARCHIVO
	private String rtmfArchivoSelSt;

	//PARA IMPRIMIR
	private Integer rtmfValidadorClick; 
	
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
		
	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB
	UsuarioRolServicio servRtmfUsuarioRolServicio;	
	@EJB
	PeriodoAcademicoServicio servRtmfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servRtmfCarreraDtoServicioJdbc;
	@EJB
	EstudianteDtoServicioJdbc servRtmfEstudianteDtoServicioJdbc;
	@EJB
	DependenciaServicio servRtmfDependenciaServicio;

	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	/**
	 * Método que permite iniciar el reporte de solicitudes  de tercera matricula
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de listar retiro.
	 */
	public String irReporteTercerasMatriculas(Usuario usuario) { 
		rtmfUsuario= usuario;
		iniciarParametros();
		try {
			List<UsuarioRol> usro = servRtmfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() 
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() 
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE){
					
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rtmfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rtmfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rtmfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
						rtmfSoporte = true;
					}
					
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
						rtmfTipoUsuario = RolConstantes.ROL_SECREABOGADO_VALUE.intValue();
						
					}
					rtmfTipoCarrera="carreras";
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rtmfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					rtmfTipoCarrera="programas";
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
					rtmfTipoUsuario = RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue();
					rtmfTipoCarrera="nivelacion";
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rtmfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
					rtmfTipoCarrera="suficiencia";
				}
			}
			
			rtmfListPeriodoAcademico = servRtmfPeriodoAcademico.listarTodosConTercerasMatriculas(); //Solo periodos de pregrado con terceras matriculas
			
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		}
		return "irListarReporteTercerasMatriculas";
	}
	
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		iniciarParametros();
		return "irInicio";
	}
	
	/**
	 * Método que busca las carreras de acuerdo al período cambiado
	 */
	public void cambiarPeriodo(){
		try {
			rtmfListCarreraDto = null;
			rtmfListDependencia = null;
			rtmfListEstudianteTercerasMatriculas = null;
			rtmfCarreraDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
			rtmfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//INICIALIZO IDENTIFICACION
			rtmfCedula = "";
			//INICIALIZO APELLIDOS
			rtmfApellido = "";
			
			if(rtmfPeriodoAcademico.getPracId() != GeneralesConstantes.APP_ID_BASE){
				if(rtmfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() 
						|| rtmfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() 
						|| rtmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()
						|| rtmfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					
					if(rtmfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rtmfListCarreraDto = servRtmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rtmfUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rtmfPeriodoAcademico.getPracId());
					}
					if(rtmfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rtmfListCarreraDto = servRtmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rtmfUsuario.getUsrId(),  RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rtmfPeriodoAcademico.getPracId());
					}
					if(rtmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rtmfListDependencia=servRtmfDependenciaServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
						rtmfSoporte = true;
					}
					
					if(rtmfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
						rtmfListCarreraDto = servRtmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rtmfUsuario.getUsrId(),  RolConstantes.ROL_SECREABOGADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rtmfPeriodoAcademico.getPracId());
					
					}
					
					
				}else if(rtmfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rtmfListCarreraDto = servRtmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(rtmfUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rtmfPeriodoAcademico.getPracId());
				}else if(rtmfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rtmfListCarreraDto = servRtmfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rtmfUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rtmfPeriodoAcademico.getPracId());
				}
			}else{
				
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.cambiarPeriodo.periodo.validacion.exception")));
				
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca las facultades de acuerdo al cambio de periodo academico
	 */
	public void cambiarFacultad(){
		try {
			rtmfListCarreraDto = null;
			rtmfListEstudianteTercerasMatriculas = null;
			//INICIALIZO IDENTIFICACION
			rtmfCedula = "";
			//INICIALIZO APELLIDOS
			rtmfApellido = "";
			rtmfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
			if(rtmfCarreraDto.getDpnId() != GeneralesConstantes.APP_ID_BASE){
				if(rtmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rtmfListCarreraDto = servRtmfCarreraDtoServicioJdbc.buscarCarrerasPorTipoFacultad(rtmfCarreraDto.getDpnId(), CarreraConstantes.TIPO_PREGRADO_VALUE);
				}
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que limpia la lista al momento de hacer el cambio de carrera
	 */
	public void cambiarCarrera(){
		rtmfListEstudianteTercerasMatriculas = null;
		//INICIALIZO IDENTIFICACION
		rtmfCedula = "";
		//INICIALIZO APELLIDOS
		rtmfApellido = "";
	}
	
	/**
	 * Método que busca las solicitudes y apelaciones de tercera matricula de estudiantes de acuerdo a los parámetros ingresados
	 */
	public void buscar(){
		try {
			rtmfListEstudianteTercerasMatriculas = new ArrayList<>();
			if(rtmfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}else{
				if(rtmfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() 
						|| rtmfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() 
						|| rtmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()
						|| rtmfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					
					if(rtmfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						//lista de estudiantes que han generado sltrmt con registro activo
						rtmfListEstudianteTercerasMatriculas = servRtmfEstudianteDtoServicioJdbc.buscarEstudianteSltrmtXperiodoXcarreraXapellidoXidentificacion(rtmfPeriodoAcademico.getPracId(), rtmfCarreraDto.getCrrId(), rtmfApellido, rtmfCedula, rtmfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);

					}
					if(rtmfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						//lista de estudiantes que han generado sltrmt con registro activo
						rtmfListEstudianteTercerasMatriculas = servRtmfEstudianteDtoServicioJdbc.buscarEstudianteSltrmtXperiodoXcarreraXapellidoXidentificacion(rtmfPeriodoAcademico.getPracId(), rtmfCarreraDto.getCrrId(), rtmfApellido, rtmfCedula, rtmfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);

					}
					if(rtmfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
						//lista de estudiantes que han generado sltrmt con registro activo
						rtmfListEstudianteTercerasMatriculas = servRtmfEstudianteDtoServicioJdbc.buscarEstudianteSltrmtXperiodoXcarreraXapellidoXidentificacion(rtmfPeriodoAcademico.getPracId(), rtmfCarreraDto.getCrrId(), rtmfApellido, rtmfCedula, rtmfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);

					}
					
					if(rtmfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						Integer valor = 0;
						if(rtmfCarreraDto.getDpnId() != GeneralesConstantes.APP_ID_BASE){
							valor = rtmfCarreraDto.getDpnId();
						}
						//lista de estudiantes que han generado sltrmt con registro activo
						rtmfListEstudianteTercerasMatriculas = servRtmfEstudianteDtoServicioJdbc.buscarEstudianteSltrmtXperiodoXcarreraXapellidoXidentificacion(rtmfPeriodoAcademico.getPracId(), rtmfCarreraDto.getCrrId(), rtmfApellido, rtmfCedula, rtmfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, valor);
					
					}
				}else if(rtmfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					//rtmfListEstudianteTercerasMatriculas = servRtmfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rtmfPeriodoAcademico.getPracId(), rtmfCarreraDto.getCrrId(), rtmfApellido, rtmfCedula, rtmfListCarreraDto, CarreraConstantes.TIPO_POSGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);
			
				}else if(rtmfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				
					//rtmfListEstudianteTercerasMatriculas = servRtmfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rtmfPeriodoAcademico.getPracId(), rtmfCarreraDto.getCrrId(), rtmfApellido, rtmfCedula, rtmfListCarreraDto, CarreraConstantes.TIPO_SUFICIENCIA_VALUE, GeneralesConstantes.APP_ID_BASE);
				}
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	/**
	 * Método que limpia los parámetros de búsqueda
	 */
	public void limpiar(){
		try {
			iniciarParametros();
			rtmfListPeriodoAcademico = servRtmfPeriodoAcademico.listarTodosConRetiros();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	/**
	 * Método para descargar el archivo de evidencia
	 * @param materiaSeleccionada - item seleccionado para descargar evidencia
	 * @return 
	 */
	public StreamedContent descargaArchivo(EstudianteJdbcDto materiaSeleccionada){
		String archivo = null;
			 archivo = materiaSeleccionada.getSltrmtDocumentoSolicitud();
		if(archivo != null){
					rtmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_SOLICITUD_TERCERA_MATRICULA+archivo;
						
			if(rtmfArchivoSelSt != null){
				
				String nombre = null;
				if(materiaSeleccionada.getSltrmtTipo()==SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE){
				 nombre = SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_LABEL+"-"+materiaSeleccionada.getPrsIdentificacion()+"-"+materiaSeleccionada.getMtrDescripcion();
				}else if (materiaSeleccionada.getSltrmtTipo()==SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_VALUE){
					nombre = SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_LABEL+"-"+materiaSeleccionada.getPrsIdentificacion()+"-"+materiaSeleccionada.getMtrDescripcion();
				}
				
				
				try{
					URL oracle = new URL("file:"+rtmfArchivoSelSt);
					URLConnection urlConnection = oracle.openConnection();
					InputStream inputStream = urlConnection.getInputStream();
					return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(rtmfArchivoSelSt),nombre);

				}catch(FileNotFoundException fnfe){
					fnfe.printStackTrace();
					rtmfArchivoSelSt = null;
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarArchivo.archivo.no.encontrado.exception")));
					return null;
				} catch (Exception e) {
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarArchivo.ubicacion.exception")));
				return null;
			}
		}else {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarArchivo.archivo.no.registrado.exception")));

		}
		return null;
	}
	
	/**
	 * Método para descargar el archivo de resolución si se resolvió la apelación
	 * @param materiaSeleccionada - item seleccionado para descargar resolución
	 * @return 
	 */
	public StreamedContent descargaResolucion(EstudianteJdbcDto materiaSeleccionada){
		String archivo = null;
		if(materiaSeleccionada.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE
				|| materiaSeleccionada.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE){
			archivo = materiaSeleccionada.getSltrmtDocumentoResolucion(); //Si se aprobo la apelacion, permito descargar la  resolucion del Secre Abogado
		}  else if(materiaSeleccionada.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_TERCERA_MATRICULA_VALUE
								|| materiaSeleccionada.getSltrmtEstado() == SolicitudTerceraMatriculaConstantes.ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE) {
			FacesUtil.mensajeError("La apelación de tercera matrícula no está resuelta");
			return null;			
			
		} 
		
		if(archivo != null){
			 rtmfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_RESOLUCION_APELACION_TERCERA_MATRICULA+archivo;
			
			if(rtmfArchivoSelSt != null){
				
				String nombre = SolicitudTerceraMatriculaConstantes.RESOLUCION_APELACION_TERCERA_MATRICULA_LABEL+"-"+materiaSeleccionada.getPrsIdentificacion()+"-"+materiaSeleccionada.getMtrDescripcion();
				try{
					URL oracle = new URL("file:"+rtmfArchivoSelSt);
					URLConnection urlConnection = oracle.openConnection();
					InputStream inputStream = urlConnection.getInputStream();
					return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(rtmfArchivoSelSt),nombre);

				}catch(FileNotFoundException fnfe){
					fnfe.printStackTrace();
					rtmfArchivoSelSt = null;
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarResolucion.archivo.no.encontrado.exception")));
					return null;
				} catch (Exception e) {
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarResolucion.ubicacion.exception")));
				
				return null;
			}
		}else {
			if(materiaSeleccionada.getSltrmtTipo()==SolicitudTerceraMatriculaConstantes.TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarResolucion.tipoSolicitud.validacion.exception")));
				return null;
			}else  if(materiaSeleccionada.getSltrmtTipo()==SolicitudTerceraMatriculaConstantes.TIPO_APELACION_TERCERA_MATRICULA_VALUE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarResolucion.tipoApelacion.validacion.exception")));
				
				return null;
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReporteTercerasMatriculasCompleto.descargarResolucion.tipo.validacion.exception")));
				return null;
				
			}
			
		}
		return null;
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		//INSTANCIO PERIODO ACADEMICO BUSQ
		rtmfPeriodoAcademico = new PeriodoAcademico();
		//INSTANCIO CARRERA
		rtmfCarreraDto = new CarreraDto();
		//INSTANCIO LA LISTA DE PERIODOS
		rtmfListPeriodoAcademico = new ArrayList<>();
		//ANULO LA LISTA DE PERIODOS
		rtmfListPeriodoAcademico = null;
		//ANULO LA LISTA DE CARRERAS 
		rtmfListCarreraDto = null;
		//ANULO LA LISTA DE DEPENDENCIAS
		rtmfListDependencia = null;
		//ANULO LA LISTA DE ESTUDIANTES RETIRO
		rtmfListEstudianteTercerasMatriculas = null;
		//SETEO PERIODO ACADEMICO
		rtmfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA DEPENDENCIA
		rtmfCarreraDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA
		rtmfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO IDENTIFICACION
		rtmfCedula = "";
		//INICIALIZO APELLIDOS
		rtmfApellido = "";
	    //ASIGNO VALOR INICIAL PARA EJECUCION DE MODALES
		bloqueaModal();
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas de la evidencia
	 * @param materiaDeshabilitar - materiaDeshabilitar entidad seleccionada para desabhilitar el link
	 */
	public void deshabilitar(EstudianteJdbcDto materiaDeshabilitar){
		for (EstudianteJdbcDto item : rtmfListEstudianteTercerasMatriculas) {
			if(materiaDeshabilitar.getSltrmtId() == item.getSltrmtId()){
				item.setVisualizador(true);
				break;
			}
		}
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas de la resolución
	 * @param materiaDeshabilitar - materiaDeshabilitar entidad seleccionada para desabhilitar el link
	 */
	public void deshabilitarResolucion(EstudianteJdbcDto materiaDeshabilitar){
		for (EstudianteJdbcDto item : rtmfListEstudianteTercerasMatriculas) {
			if(materiaDeshabilitar.getSltrmtId() == item.getSltrmtId()){
				item.setVisualizador2(true);
				break;
			}
		}
	}
	
	//HABILITA EL MODAL IMPRIMIR
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del xls
	 */
	public void verificarClickImprimir2(){
		if(rtmfListEstudianteTercerasMatriculas.size() > 0 && !rtmfListEstudianteTercerasMatriculas.equals(null)){
			generarReporteSltrmt(rtmfListEstudianteTercerasMatriculas);
			habilitaModalImpresion2();
		}else{
			bloqueaModal();
			FacesUtil.mensajeWarn("No existen estudiantes para generar el archivo");
		}
		
	}

	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		rtmfValidadorClick = 0;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion2(){
		rtmfValidadorClick = 2;
	}
	
	/**
	 * Método que genera el reporte
	 */
	public void generarReporteSltrmt(List<EstudianteJdbcDto> listaEstudiantesRetiro){
		try {

			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE RETIROS ********************//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "reporteTercerasMatriculas";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			frmRrmParametros.put("nick", rtmfUsuario.getUsrNick());

			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoSltrmt = null;
			ListasCombosForm listaCombo = new ListasCombosForm();
			for (EstudianteJdbcDto item : listaEstudiantesRetiro) {
				datoSltrmt = new HashMap<String, Object>();
				datoSltrmt.put("periodo", item.getPracDescripcion());
				datoSltrmt.put("facultad", item.getDpnDescripcion());
				datoSltrmt.put("carrera", item.getCrrDetalle());
				datoSltrmt.put("asignatura", item.getMtrDescripcion());
				if(item.getSltrmtTipo() != null){
					datoSltrmt.put("tiposolicitud", listaCombo.getLabelTipoSolicitud(item.getSltrmtTipo()));
					
				}else{
					datoSltrmt.put("tiposolicitud", "NO HAY");
				}
				
				datoSltrmt.put("fechasolicitud", item.getSltrmtFechaSolicitud() == null || item.getSltrmtFechaSolicitud().toString() == "" ?  " "  : GeneralesUtilidades.fechaFormatoTimeStamp(item.getSltrmtFechaSolicitud()));
				datoSltrmt.put("fechaverificacion", item.getSltrmtFechaVerificacion() == null || item.getSltrmtFechaVerificacion().toString() == "" ?  " "  : GeneralesUtilidades.fechaFormatoTimeStamp(item.getSltrmtFechaVerificacion()));
				datoSltrmt.put("fecharespuesta", item.getSltrmtFechaRespSolicitud() == null || item.getSltrmtFechaRespSolicitud().toString() == "" ?  " "  : GeneralesUtilidades.fechaFormatoTimeStamp(item.getSltrmtFechaRespSolicitud()));
				datoSltrmt.put("estado", listaCombo.getLabelListaEstadoSolicitudTerceraMatriSltrmt(item.getSltrmtEstado()));
				datoSltrmt.put("observacion", item.getSltrmtObservacionFinal() == null ||  item.getSltrmtObservacionFinal() == "" ? " " :  item.getSltrmtObservacionFinal());
				datoSltrmt.put("identificacion", item.getPrsIdentificacion());
				datoSltrmt.put("nombres", item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres());
				frmRrmCampos.add(datoSltrmt);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteCompletoTercerasMatriculas");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");
			

			// Establecemos en el atributo de la sesión la lista de mapas de
			// datos frmCrpCampos y parámetros frmRrmParametros
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
			httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
			httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
			// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getRtmfUsuario() {
		return rtmfUsuario;
	}
	public void setRtmfUsuario(Usuario rtmfUsuario) {
		this.rtmfUsuario = rtmfUsuario;
	}
	public PeriodoAcademico getRtmfPeriodoAcademico() {
		return rtmfPeriodoAcademico;
	}
	public void setRtmfPeriodoAcademico(PeriodoAcademico rtmfPeriodoAcademico) {
		this.rtmfPeriodoAcademico = rtmfPeriodoAcademico;
	}
	public CarreraDto getRtmfCarreraDto() {
		return rtmfCarreraDto;
	}
	public void setRtmfCarreraDto(CarreraDto rtmfCarreraDto) {
		this.rtmfCarreraDto = rtmfCarreraDto;
	}
	public String getRtmfCedula() {
		return rtmfCedula;
	}
	public void setRtmfCedula(String rtmfCedula) {
		this.rtmfCedula = rtmfCedula;
	}
	public String getRtmfApellido() {
		return rtmfApellido;
	}
	public void setRtmfApellido(String rtmfApellido) {
		this.rtmfApellido = rtmfApellido;
	}
	public String getRtmfTipoCarrera() {
		return rtmfTipoCarrera;
	}
	public void setRtmfTipoCarrera(String rtmfTipoCarrera) {
		this.rtmfTipoCarrera = rtmfTipoCarrera;
	}

	public Integer getRtmfTipoUsuario() {
		return rtmfTipoUsuario;
	}
	public void setRtmfTipoUsuario(Integer rtmfTipoUsuario) {
		this.rtmfTipoUsuario = rtmfTipoUsuario;
	}
	public List<PeriodoAcademico> getRtmfListPeriodoAcademico() {
		rtmfListPeriodoAcademico = rtmfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):rtmfListPeriodoAcademico;
		return rtmfListPeriodoAcademico;
	}
	public void setRtmfListPeriodoAcademico(List<PeriodoAcademico> rtmfListPeriodoAcademico) {
		this.rtmfListPeriodoAcademico = rtmfListPeriodoAcademico;
	}
	public List<CarreraDto> getRtmfListCarreraDto() {
		rtmfListCarreraDto = rtmfListCarreraDto==null?(new ArrayList<CarreraDto>()):rtmfListCarreraDto;
		return rtmfListCarreraDto;
	}
	public void setRtmfListCarreraDto(List<CarreraDto> rtmfListCarreraDto) {
		this.rtmfListCarreraDto = rtmfListCarreraDto;
	}
	public List<EstudianteJdbcDto> getRtmfListEstudianteTercerasMatriculas() {
		rtmfListEstudianteTercerasMatriculas = rtmfListEstudianteTercerasMatriculas==null?(new ArrayList<EstudianteJdbcDto>()):rtmfListEstudianteTercerasMatriculas;
		return rtmfListEstudianteTercerasMatriculas;
	}
	public void setRtmfListEstudianteTercerasMatriculas(List<EstudianteJdbcDto> rtmfListEstudianteTercerasMatriculas) {
		this.rtmfListEstudianteTercerasMatriculas = rtmfListEstudianteTercerasMatriculas;
	}
	public String getRtmfArchivoSelSt() {
		return rtmfArchivoSelSt;
	}
	public void setRtmfArchivoSelSt(String rtmfArchivoSelSt) {
		this.rtmfArchivoSelSt = rtmfArchivoSelSt;
	}
	
	public Integer getRtmfValidadorClick() {
		return rtmfValidadorClick;
	}
	public void setRtmfValidadorClick(Integer rtmfValidadorClick) {
		this.rtmfValidadorClick = rtmfValidadorClick;
	}
	public List<Dependencia> getRtmfListDependencia() {
		rtmfListDependencia = rtmfListDependencia==null?(new ArrayList<Dependencia>()):rtmfListDependencia;
		return rtmfListDependencia;
	}
	public void setRtmfListDependencia(List<Dependencia> rtmfListDependencia) {
		this.rtmfListDependencia = rtmfListDependencia;
	}
	public boolean isRtmfSoporte() {
		return rtmfSoporte;
	}
	public void setRtmfSoporte(boolean rtmfSoporte) {
		this.rtmfSoporte = rtmfSoporte;
	}
	
}