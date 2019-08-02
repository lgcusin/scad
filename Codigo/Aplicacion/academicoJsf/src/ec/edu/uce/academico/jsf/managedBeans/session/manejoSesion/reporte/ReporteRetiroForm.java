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
   
 ARCHIVO:     ReporteRetiroForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones del reporte de retiro.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
15-ENERO-2018			Dennis Collaguazo                  Emisión Inicial
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
import ec.edu.uce.academico.ejb.utilidades.constantes.DetalleMatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) ReporteRetiroForm
 * Managed Bean que maneja las peticiones del reporte de retiro.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="reporteRetiroForm")
@SessionScoped
public class ReporteRetiroForm implements Serializable{

	private static final long serialVersionUID = -5044772886269795627L;
	
	//********************************************************************/
	//******************* ATRIBUTOS DE LA CLASE **************************/
	//********************************************************************/
	//PARA VER
	private Usuario rrfUsuario;
	private PeriodoAcademico rrfPeriodoAcademico;
	private CarreraDto rrfCarreraDto ;
	private String rrfCedula;
	private String rrfApellido;
	private String rrfTipoCarrera;
	private Integer rrfTipoRetiro;
	private Integer rrfTipoUsuario;
	private List<PeriodoAcademico> rrfListPeriodoAcademico;
	private List<CarreraDto> rrfListCarreraDto;
	private List<EstudianteJdbcDto> rrfListEstudianteRetiro;
	private List<Dependencia> rrfListDependencia;
	private Integer rrfTipoRetiroAnulacion;
	private boolean rrfRenderTipoRetiroAnulacion;
	private boolean rrfSoporte;
	
	//PARA DESCARGAR ARCHIVO
	private String rrfArchivoSelSt;
	private Integer rrfVisualizadorLink;
	
	
	
	//PARA IMPRIMIR
	private Integer rrfValidadorClick; 
	
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
	UsuarioRolServicio servRrfUsuarioRolServicio;	
	@EJB
	PeriodoAcademicoServicio servRpfPeriodoAcademico;
	@EJB
	CarreraDtoServicioJdbc servRrfCarreraDtoServicioJdbc;
	@EJB
	EstudianteDtoServicioJdbc servRrfEstudianteDtoServicioJdbc;
	@EJB
	DependenciaServicio servRrfDependenciaServicio;

	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	/**
	 * Método que permite iniciar el reporte de retiro
	 * @param usuario - el usuario que ingresa 
	  * @return  Navegacion a la pagina xhtml de listar retiro.
	 */
	public String irReporteRetiro(Usuario usuario) { 
		rrfUsuario= usuario;
		iniciarParametros();
		try {
			List<UsuarioRol> usro = servRrfUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usro) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() 
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() 
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rrfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rrfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rrfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
						rrfSoporte = true;
					}
					
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
						rrfTipoUsuario = RolConstantes.ROL_SECREABOGADO_VALUE.intValue();
						}
					
					rrfTipoCarrera="carreras";
					rrfRenderTipoRetiroAnulacion=true; 
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rrfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					rrfTipoCarrera="programas";
					rrfRenderTipoRetiroAnulacion=false;  //No se muestra combo de tipos de retiro
					
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue()){
					rrfTipoUsuario = RolConstantes.ROL_USUARIONIVELACION_VALUE.intValue();
					rrfTipoCarrera="nivelacion";
					rrfRenderTipoRetiroAnulacion=false;  //No se muestra combo de tipos de retiro
				}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rrfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
					rrfTipoCarrera="suficiencia";
					rrfRenderTipoRetiroAnulacion=false;  //No se muestra combo de tipos de retiro
				}
			}
			rrfListPeriodoAcademico = servRpfPeriodoAcademico.listarTodosConRetiros();
		} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		}
		return "irReporteRetiro";
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
			rrfListCarreraDto = null;
			rrfListDependencia = null;
			rrfListEstudianteRetiro = null;
			rrfCarreraDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
			rrfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
			rrfTipoRetiroAnulacion = GeneralesConstantes.APP_ID_BASE;
			if(rrfPeriodoAcademico.getPracId() != GeneralesConstantes.APP_ID_BASE){
				if(rrfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() 
						|| rrfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() 
						|| rrfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()
						|| rrfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					
					if(rrfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rrfListCarreraDto = servRrfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rrfUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rrfPeriodoAcademico.getPracId());
					}
					if(rrfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rrfListCarreraDto = servRrfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rrfUsuario.getUsrId(),  RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rrfPeriodoAcademico.getPracId());
					}
					if(rrfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rrfListDependencia=servRrfDependenciaServicio.listarFacultadesActivasXTipoCarrera(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE, CarreraConstantes.TIPO_PREGRADO_VALUE);
						rrfSoporte = true;
					}
					
					if(rrfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
						rrfListCarreraDto = servRrfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rrfUsuario.getUsrId(),  RolConstantes.ROL_SECREABOGADO_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rrfPeriodoAcademico.getPracId());
					}
					
				}else if(rrfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rrfListCarreraDto = servRrfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(rrfUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rrfPeriodoAcademico.getPracId());
	
				}else if(rrfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rrfListCarreraDto = servRrfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rrfUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, rrfPeriodoAcademico.getPracId());
			
				}
			}else{
				FacesUtil.mensajeError("Debe seleccionar el período académico");
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
			rrfListCarreraDto = null;
			rrfListEstudianteRetiro = null;
			rrfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
			rrfTipoRetiroAnulacion = GeneralesConstantes.APP_ID_BASE;
			if(rrfCarreraDto.getDpnId() != GeneralesConstantes.APP_ID_BASE){
				if(rrfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					rrfListCarreraDto = servRrfCarreraDtoServicioJdbc.buscarCarrerasPorTipoFacultad(rrfCarreraDto.getDpnId(), CarreraConstantes.TIPO_PREGRADO_VALUE);
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
		rrfListEstudianteRetiro = null;
		rrfTipoRetiroAnulacion = GeneralesConstantes.APP_ID_BASE;
	}
	
	
	/**
	 * Método que limpia la lista al momento de hacer el cambio de carrera
	 */
	public void cambiarTipoRetiroAnulacion(){
		rrfListEstudianteRetiro = null;
	}
	
	/**
	 * Método que busca los retiros de estudiantes de acuerdo a los parámetros ingresados
	 */
	public void buscar(){
		try {
			rrfListEstudianteRetiro = new ArrayList<>();
			if(rrfPeriodoAcademico.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError("Debe seleccionar el período académico");
			}else{
				if(rrfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() 
						|| rrfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() 
						|| rrfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()
						|| rrfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					
					if(rrfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						//rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);
						rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacionXtipo(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE, rrfTipoRetiroAnulacion);
	
					}
					if(rrfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
					 //	rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);
						rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacionXtipo(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE, rrfTipoRetiroAnulacion);

					}
					
					if(rrfTipoUsuario == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()){
					//	rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);
					
					rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacionXtipo(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, GeneralesConstantes.APP_ID_BASE, rrfTipoRetiroAnulacion);

					}
					
					
					if(rrfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						Integer valor = 0;
						if(rrfCarreraDto.getDpnId() != GeneralesConstantes.APP_ID_BASE){
							valor = rrfCarreraDto.getDpnId();
						}
						//rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE, valor);
						rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacionXtipo(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_PREGRADO_VALUE,valor , rrfTipoRetiroAnulacion);

					}
				
				}else if(rrfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_POSGRADO_VALUE, GeneralesConstantes.APP_ID_BASE);
				}else if(rrfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rrfListEstudianteRetiro = servRrfEstudianteDtoServicioJdbc.buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion(rrfPeriodoAcademico.getPracId(), rrfCarreraDto.getCrrId(), rrfApellido, rrfCedula, rrfListCarreraDto, CarreraConstantes.TIPO_SUFICIENCIA_VALUE, GeneralesConstantes.APP_ID_BASE);
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
			rrfListPeriodoAcademico = servRpfPeriodoAcademico.listarTodosConRetiros();
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());	
		}
	}
	
	/**
	 * Método para descargar el archivo
	 * @param rutaNombre - rutaNombre nombre p path del archivo que se encuentra registrado
	 * @return 
	 */
	public StreamedContent descargaArchivo(EstudianteJdbcDto materiaSeleccionada){
		String archivo = null;
		if((materiaSeleccionada.getDtmtEstadoRespuesta() == DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE 
				|| materiaSeleccionada.getDtmtEstadoRespuesta() == DetalleMatriculaConstantes.DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE)
				&& (materiaSeleccionada.getDtmtEstadoSolicitud() == DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE
				|| materiaSeleccionada.getDtmtEstadoSolicitud() == DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE)){
			archivo = materiaSeleccionada.getDtmtArchivoRespuesta();
		} else if(materiaSeleccionada.getDtmtEstadoSolicitud() == DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE
				|| materiaSeleccionada.getDtmtEstadoSolicitud() == DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE) {
			archivo = null;
		} else {
			archivo = materiaSeleccionada.getDtmtArchivoEstudiantes();
		}
		if(archivo != null){
			rrfArchivoSelSt = GeneralesConstantes.APP_PATH_ARCHIVOS+GeneralesConstantes.APP_PATH_ARCHIVOS_RETIRO_MATRICULA_SNIESE+archivo;
			if(rrfArchivoSelSt != null){
				String nombre = materiaSeleccionada.getMtrDescripcion();
				try{
					URL oracle = new URL("file:"+rrfArchivoSelSt);
					URLConnection urlConnection = oracle.openConnection();
					InputStream inputStream = urlConnection.getInputStream();
					return new DefaultStreamedContent(inputStream,GeneralesUtilidades.obtenerContentType(rrfArchivoSelSt),nombre);

				}catch(FileNotFoundException fnfe){
					fnfe.printStackTrace();
					rrfArchivoSelSt = null;
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.StreamedContent.descargar.archivo.exception")));
					return null;
				} catch (Exception e) {
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aprobacion.retiro.matricula.StreamedContent.descargar.archivo.no.encontrado.exception")));
				return null;
			}
		}else if(archivo == null && (materiaSeleccionada.getDtmtEstadoSolicitud() == DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE
				|| materiaSeleccionada.getDtmtEstadoSolicitud() == DetalleMatriculaConstantes.ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE)){
			FacesUtil.mensajeError("No tiene el documento de validación generado por el Director de Carrera");
		}else{
			FacesUtil.mensajeError("No se encuentra el documento de solicitud del estudiante");
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
		rrfPeriodoAcademico = new PeriodoAcademico();
		//INSTANCIO CARRERA
		rrfCarreraDto = new CarreraDto();
		//INSTANCIO LA LISTA DE PERIODOS
		rrfListPeriodoAcademico = new ArrayList<>();
		//ANULO LA LISTA DE PERIODOS
		rrfListPeriodoAcademico = null;
		//ANULO LA LISTA DE CARRERAS 
		rrfListCarreraDto = null;
		//ANULO LA LISTA DE DEPENDENCIAS
		rrfListDependencia = null;
		//ANULO LA LISTA DE ESTUDIANTES RETIRO
		rrfListEstudianteRetiro = null;
		//SETEO PERIODO ACADEMICO
		rrfPeriodoAcademico.setPracId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA DEPENDENCIA
		rrfCarreraDto.setDpnId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA
		rrfCarreraDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO IDENTIFICACION
		rrfCedula = "";
		//INICIALIZO APELLIDOS
		rrfApellido = "";
		//INICIALIZO TIPO RETIRO
		rrfTipoRetiro = GeneralesConstantes.APP_ID_BASE;
		//INICIALIZO TIPO RETIRO
		rrfTipoRetiroAnulacion = GeneralesConstantes.APP_ID_BASE;
		rrfRenderTipoRetiroAnulacion=false;
		//ASIGNO VALOR INICIAL PARA EJECUCION DE MODALES
		bloqueaModal();
	}
	
	/**
	 * Método que permite deshabilitar el link para varias descargas 
	 * @param materiaDeshabilitar - materiaDeshabilitar entidad seleccionada para desabhilitar el link
	 */
	public void deshabilitar(EstudianteJdbcDto materiaDeshabilitar){
		for (EstudianteJdbcDto item : rrfListEstudianteRetiro) {
			if(materiaDeshabilitar.getDtmtId() == item.getDtmtId()){
				item.setVisualizador(true);
				break;
			}
		}
	}
	
	//HABILITA EL MODAL IMPRIMIR
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del xls
	 */
	public void verificarClickImprimir2(){
		if(rrfListEstudianteRetiro.size() > 0 && !rrfListEstudianteRetiro.equals(null)){
			generarReporteRetiro(rrfListEstudianteRetiro);
			habilitaModalImpresion2();
		}else{
			bloqueaModal();
			FacesUtil.mensajeWarn("No existen estudiantes para generar el archivo");
		}
		//			rpfHabilitarBoton = 0;
	}

	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		rrfValidadorClick = 0;
	}

	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion2(){
		rrfValidadorClick = 2;
	}
	
	/**
	 * Método que genera el reporte
	 */
	public void generarReporteRetiro(List<EstudianteJdbcDto> listaEstudiantesRetiro){
		try {

			List<Map<String, Object>> frmRrmCampos = null;
			Map<String, Object> frmRrmParametros = null;
			String frmRrmNombreReporte = null;
			// ****************************************************************//
			// ********* GENERACION DEL REPORTE DE RETIROS ********************//
			// ****************************************************************//
			// ****************************************************************//
			frmRrmNombreReporte = "reporteParalelos";
			frmRrmParametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
			String fecha = formato.format(new Date());
			frmRrmParametros.put("fecha",fecha);
			frmRrmParametros.put("nick", rrfUsuario.getUsrNick());

			frmRrmCampos = new ArrayList<Map<String, Object>>();
			Map<String, Object> datoRetiro = null;
			ListasCombosForm listaCombo = new ListasCombosForm();
			for (EstudianteJdbcDto item : listaEstudiantesRetiro) {
				datoRetiro = new HashMap<String, Object>();
				datoRetiro.put("periodo", item.getPracDescripcion());
				datoRetiro.put("facultad", item.getDpnDescripcion());
				datoRetiro.put("carrera", item.getCrrDescripcion());
				datoRetiro.put("asignatura", item.getMtrDescripcion());
				datoRetiro.put("paralelo", item.getPrlDescripcion());
				datoRetiro.put("solicitud", listaCombo.getLabelEstadoDetalleMatricula(item.getDtmtEstadoSolicitud()));
				datoRetiro.put("fechasolicitud", item.getDtmtFechaSolicitud() == null || item.getDtmtFechaSolicitud().toString() == "" ? (item.getDtmtFechaHistorico() == null ? " " : GeneralesUtilidades.fechaFormatoTimeStamp(item.getDtmtFechaHistorico()) ) : GeneralesUtilidades.fechaFormatoTimeStamp(item.getDtmtFechaSolicitud()));
				datoRetiro.put("validacion", item.getDtmtEstadoCambio() == 1 || item.getDtmtEstadoCambio() == 5 || item.getDtmtEstadoCambio() == 4 ? 
											listaCombo.getLabelEstadoDetalleMatricula(item.getDtmtEstadoCambio()) : 
												item.getDtmtEstadoRespuesta() == 1 || item.getDtmtEstadoRespuesta() == 5 || item.getDtmtEstadoRespuesta() == 4  ? listaCombo.getLabelEstadoDetalleMatricula(item.getDtmtEstadoRespuesta()) : "NO");
				datoRetiro.put("fechavalidacion", item.getDtmtEstadoCambio() == 1 || item.getDtmtEstadoCambio() == 5 || item.getDtmtEstadoCambio() == 4 || item.getDtmtEstadoCambio() == 6? 
						item.getDtmtFechaCambio() == null  ? " " : GeneralesUtilidades.fechaFormatoTimeStamp(item.getDtmtFechaCambio()) : 
							item.getDtmtEstadoRespuesta() == 1 || item.getDtmtEstadoRespuesta() == 5 || item.getDtmtEstadoRespuesta() == 4  ? 
									item.getDtmtFechaRespuesta() == null ? " " : GeneralesUtilidades.fechaFormatoTimeStamp(item.getDtmtFechaRespuesta()) : "NO");
				datoRetiro.put("observacion", item.getDtmtObservacionCambio() == null ||  item.getDtmtObservacionCambio() == "" ? " " :  item.getDtmtObservacionCambio());
				datoRetiro.put("identificacion", item.getPrsIdentificacion());
				datoRetiro.put("nombres", item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido()+" "+item.getPrsNombres());
				frmRrmCampos.add(datoRetiro);
			}

			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/academico/reportes/archivosJasper/reporteRetiro");
			frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
			frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");

			//			frmRrmCampos.add(datoRetiro);
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

	public Usuario getRrfUsuario() {
		return rrfUsuario;
	}
	public void setRrfUsuario(Usuario rrfUsuario) {
		this.rrfUsuario = rrfUsuario;
	}
	public PeriodoAcademico getRrfPeriodoAcademico() {
		return rrfPeriodoAcademico;
	}
	public void setRrfPeriodoAcademico(PeriodoAcademico rrfPeriodoAcademico) {
		this.rrfPeriodoAcademico = rrfPeriodoAcademico;
	}
	public CarreraDto getRrfCarreraDto() {
		return rrfCarreraDto;
	}
	public void setRrfCarreraDto(CarreraDto rrfCarreraDto) {
		this.rrfCarreraDto = rrfCarreraDto;
	}
	public String getRrfCedula() {
		return rrfCedula;
	}
	public void setRrfCedula(String rrfCedula) {
		this.rrfCedula = rrfCedula;
	}
	public String getRrfApellido() {
		return rrfApellido;
	}
	public void setRrfApellido(String rrfApellido) {
		this.rrfApellido = rrfApellido;
	}
	public String getRrfTipoCarrera() {
		return rrfTipoCarrera;
	}
	public void setRrfTipoCarrera(String rrfTipoCarrera) {
		this.rrfTipoCarrera = rrfTipoCarrera;
	}
	public Integer getRrfTipoRetiro() {
		return rrfTipoRetiro;
	}
	public void setRrfTipoRetiro(Integer rrfTipoRetiro) {
		this.rrfTipoRetiro = rrfTipoRetiro;
	}
	public Integer getRrfTipoUsuario() {
		return rrfTipoUsuario;
	}
	public void setRrfTipoUsuario(Integer rrfTipoUsuario) {
		this.rrfTipoUsuario = rrfTipoUsuario;
	}
	public List<PeriodoAcademico> getRrfListPeriodoAcademico() {
		rrfListPeriodoAcademico = rrfListPeriodoAcademico==null?(new ArrayList<PeriodoAcademico>()):rrfListPeriodoAcademico;
		return rrfListPeriodoAcademico;
	}
	public void setRrfListPeriodoAcademico(List<PeriodoAcademico> rrfListPeriodoAcademico) {
		this.rrfListPeriodoAcademico = rrfListPeriodoAcademico;
	}
	public List<CarreraDto> getRrfListCarreraDto() {
		rrfListCarreraDto = rrfListCarreraDto==null?(new ArrayList<CarreraDto>()):rrfListCarreraDto;
		return rrfListCarreraDto;
	}
	public void setRrfListCarreraDto(List<CarreraDto> rrfListCarreraDto) {
		this.rrfListCarreraDto = rrfListCarreraDto;
	}
	public List<EstudianteJdbcDto> getRrfListEstudianteRetiro() {
		rrfListEstudianteRetiro = rrfListEstudianteRetiro==null?(new ArrayList<EstudianteJdbcDto>()):rrfListEstudianteRetiro;
		return rrfListEstudianteRetiro;
	}
	public void setRrfListEstudianteRetiro(List<EstudianteJdbcDto> rrfListEstudianteRetiro) {
		this.rrfListEstudianteRetiro = rrfListEstudianteRetiro;
	}
	public String getRrfArchivoSelSt() {
		return rrfArchivoSelSt;
	}
	public void setRrfArchivoSelSt(String rrfArchivoSelSt) {
		this.rrfArchivoSelSt = rrfArchivoSelSt;
	}
	public Integer getRrfVisualizadorLink() {
		return rrfVisualizadorLink;
	}
	public void setRrfVisualizadorLink(Integer rrfVisualizadorLink) {
		this.rrfVisualizadorLink = rrfVisualizadorLink;
	}
	public Integer getRrfValidadorClick() {
		return rrfValidadorClick;
	}
	public void setRrfValidadorClick(Integer rrfValidadorClick) {
		this.rrfValidadorClick = rrfValidadorClick;
	}
	public List<Dependencia> getRrfListDependencia() {
		rrfListDependencia = rrfListDependencia==null?(new ArrayList<Dependencia>()):rrfListDependencia;
		return rrfListDependencia;
	}
	public void setRrfListDependencia(List<Dependencia> rrfListDependencia) {
		this.rrfListDependencia = rrfListDependencia;
	}
	public boolean isRrfSoporte() {
		return rrfSoporte;
	}
	public void setRrfSoporte(boolean rrfSoporte) {
		this.rrfSoporte = rrfSoporte;
	}


	public Integer getRrfTipoRetiroAnulacion() {
		return rrfTipoRetiroAnulacion;
	}


	public void setRrfTipoRetiroAnulacion(Integer rrfTipoRetiroAnulacion) {
		this.rrfTipoRetiroAnulacion = rrfTipoRetiroAnulacion;
	}


	public boolean isRrfRenderTipoRetiroAnulacion() {
		return rrfRenderTipoRetiroAnulacion;
	}


	public void setRrfRenderTipoRetiroAnulacion(boolean rrfRenderTipoRetiroAnulacion) {
		this.rrfRenderTipoRetiroAnulacion = rrfRenderTipoRetiroAnulacion;
	}


	
	
	
	
}