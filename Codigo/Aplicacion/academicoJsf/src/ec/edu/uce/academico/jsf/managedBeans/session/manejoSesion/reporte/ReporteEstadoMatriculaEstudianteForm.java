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
   
 ARCHIVO:     ReporteEstadoMatriculaEstudianteForm.java	  
 DESCRIPCION: Bean de sesion que maneja el reporte estado matrícula estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02-ABRL-2019 			Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EtniaException;
import ec.edu.uce.academico.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Etnia;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSecretariaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReporteEstadoMatriculaEstudianteForm.
 * Managed Bean que administra maneja el reporte estado matrícula estudiantes.
 * @author dcollaguazo.
 * @version 1.0
 */

@ManagedBean(name="reporteEstadoMatriculaEstudianteForm")
@SessionScoped
public class ReporteEstadoMatriculaEstudianteForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario remefUsuario;
	private String remefTipoCarrera;
	private Integer remefTipoUsuario;
	
	
	//PARA BUSQUEDA
	private EstudianteJdbcDto remefEstudianteBuscar;
	private List<CarreraDto> remefListCarreraBusq;
	private List<EstudianteJdbcDto> remefListEstudianteBusq;
	private List<NivelDto> remefListNivelBusq;
	private List<PeriodoAcademicoDto> remefListPeriodoAcademicoBusq;
	private List<ParaleloDto> remefListParaleloBusq;
	private NivelDto remefNivelDtoBusq;
	private ParaleloDto remefParaleloDtoBusq;
	private List<MateriaDto> remefListMateriaBusq;
	
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto remefEstudianteVer;
	private String remefActivacion;
	private int remefActivadorReporte;
	//PARA VISUALIZAR LA RESIDENCIA DEL ESTUDIANTE
	private UbicacionDto remefPaisResidenciaVer;
	private UbicacionDto remefProvinciaResidenciaVer;
	private UbicacionDto remefCantonResidenciaVer;
	private UbicacionDto remefParroquiaResidenciaVer;
	
	//PARA VISUALIZAR LA NACIONALIDAD DEL ESTUDIANTE
	private UbicacionDto remefPaisNacimientoVer;
	private UbicacionDto remefProvinciaNacimientoVer;
	private UbicacionDto remefCantonNacimientoVer;
	
	//PARA VISUALIZAR LA UBICACION DEL COLEGIO DEL ESTUDIANTE
	private UbicacionDto remefPaisInacVer;
	private UbicacionDto remefProvinciaInacVer;
	private UbicacionDto remefCantonInacVer;

	//PARA EDITAR DATOS DEL ESTUDIANTE
	private EstudianteJdbcDto remefEstudianteEditar;
	// PARA EDITAR LA RESIDENCIA DEL ESTUDIANTE
	private UbicacionDto remefPaisResidenciaEditar;
	private UbicacionDto remefProvinciaResidenciaEditar;
	private UbicacionDto remefCantonResidenciaEditar;
	private UbicacionDto remefParroquiaResidenciaEditar;

	// PARA EDITAR LA NACIONALIDAD DEL ESTUDIANTE
	private UbicacionDto remefPaisNacimientoEditar;
	private UbicacionDto remefProvinciaNacimientoEditar;
	private UbicacionDto remefCantonNacimientoEditar;

	// PARA EDITAR LA UBICACION DEL COLEGIO DEL ESTUDIANTE
	private UbicacionDto remefPaisInacEditar;
	private UbicacionDto remefProvinciaInacEditar;
	private UbicacionDto remefCantonInacEditar;

	// LISTAS DE PAIS, PROVINCIA, CANTON DE NACIMIENTO
	private List<UbicacionDto> remefListaPaisesNac;
	private List<UbicacionDto> remefListaProvinciasNac;
	private List<UbicacionDto> remefListaCantonesNac;
	
	// LISTAS DE PAIS, PROVINCIA, CANTON DE RESIDENCIA
	private List<UbicacionDto> remefListaPaisesRes;
	private List<UbicacionDto> remefListaProvinciasRes;
	private List<UbicacionDto> remefListaCantonesRes;
	private List<UbicacionDto> remefListaParroquiasRes;

	// LISTAS DE PAIS, PROVINCIA, CANTON DE INSTITUCION ACADEMICA.
	private List<UbicacionDto> remefListaPaisesInac;
	private List<UbicacionDto> remefListaProvinciasInac;
	private List<UbicacionDto> remefListaCantonesInac;
	
	//LISTA DE INSTITUCIONES ACADEMICAS
	
	private List<InstitucionAcademicaDto> remefListaInstitucionesAcademicasDto;
	private List<TituloDto> remefListaTitulosDto;
   //LISTA DE ETNIAS
	private List<Etnia> remefListaEtnias;
	
	//AUXILIARES
	
	private Boolean remefDesactivarUbicacionInac;
	private Boolean remefDesactivarDiscapacidad;
	private Boolean remefDesactivarCarnetDiscap;
	private int remefClickModalEditarEstudiante;
	private Boolean remefDesactivarUbicacionRes;
	private Boolean remefDesactivarUbicacionNac;
	private String remefNombreReporte;
		
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
	private CarreraDtoServicioJdbc servRemefCarreraDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servRemefEstudianteDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servRemefNivelDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servRemefUbicacionDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servRemefPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servRemefParaleloDtoServicioJdbc;
	@EJB
	private UsuarioRolServicio servRemefUsuarioRolServicio;
	@EJB
	private InstitucionAcademicaDtoServicioJdbc servRemefInstitucionAcademiacaDtoServicioJdbc;
	@EJB
	private TituloDtoServicioJdbc servRemefTituloDtoServicioJdbc;
	@EJB
	private EtniaServicio servRemefEtniaServicio;
	@EJB
	private PersonaServicio servRemefPersonaServicio;
	@EJB
	private MateriaDtoServicioJdbc servRemefMateriaDtoServicioJdbc;
	@EJB
	private MateriaServicio servRemefMateriaServicio;
	
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR AL ESTUDIANTE
		/**
		 * Dirige la navegacion hacia la pagina de listarEstudiante
		 */
		public String irAlistarEstudiante(Usuario usuario){
			remefUsuario = usuario;
			remefActivadorReporte = 0;
			String retorno = null;
			try {
				try {
					List<UsuarioRol> usro = servRemefUsuarioRolServicio.buscarRolesActivoXUsuario(usuario.getUsrId());
					for (UsuarioRol item : usro) {
						if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
							remefTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
							remefTipoCarrera="carreras";
						}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
							remefTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
							remefTipoCarrera="programas";
						}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
							remefTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
							remefTipoCarrera="suficiencias";
						}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
							remefTipoUsuario = RolConstantes.ROL_SECRENIVELACION_VALUE.intValue();
							remefTipoCarrera="areas";
						}
					}
				} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
				}
				//INICIO PARAMETROS DE BUSQUEDA
				iniciarParametros();
				//LISTO LOS PERIODOS ACADEMICOS
				if(remefTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
//					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				}else if (remefTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarXEstadoPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);		
				}else if(remefTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					//idiomas
//					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarXEstadoXTipo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				}else if (remefTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
//					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
					remefListPeriodoAcademicoBusq = servRemefPeriodoAcademicoDtoServicioJdbc.listarTodosXTipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				}
				
				//LISTO LAS CARRERAS ASIGNADAS AL ROL QUE ENTRO A LA FUNCIONALIDAD
				//efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFl(usuario.getUsrId(), RolConstantes.ROL_BD_SECRECARRERA, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				//LISTO TODOS LOS NIVELES DE LAS MALLAS CURRICULARES
				//efListNivelBusq = servEfNivelDtoServicioJdbc.listarTodos();
				retorno = "irReporteEstadoMatriculaEstudiante";
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
		remefEstudianteBuscar = new EstudianteJdbcDto();
		remefListCarreraBusq = null;
		remefListEstudianteBusq = null;
		remefListNivelBusq = null;
		return "irInicio";
	}
	
	public void nada(){
		remefActivacion = "true";
		remefActivadorReporte = 1;
		try {
			Materia mat = new Materia();
			if(remefEstudianteBuscar.getMtrId() != GeneralesConstantes.APP_ID_BASE){
				mat = servRemefMateriaServicio.buscarPorId(remefEstudianteBuscar.getMtrId());
			}
			
			
			if(remefEstudianteBuscar.getNvlId() == NivelConstantes.NIVEL_NIVELACION_VALUE){
				ReporteSecretariaForm.generarReporteEstadoMatricula(remefListEstudianteBusq, remefEstudianteBuscar.getPrlId(), remefEstudianteBuscar.getMtrId(), mat, NivelConstantes.NIVEL_NIVELACION_VALUE);
				remefNombreReporte = "'XLS','reporteSecretariaNivelacion','ESTADO_MATRICULA'";
			}else{
				ReporteSecretariaForm.generarReporteEstadoMatricula(remefListEstudianteBusq, remefEstudianteBuscar.getPrlId(), remefEstudianteBuscar.getMtrId(), mat, GeneralesConstantes.APP_ID_BASE);
				remefNombreReporte = "'XLS','reporteSecretaria','ESTADO_MATRICULA'";
			}
			
		} catch (Exception e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		remefEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		remefListEstudianteBusq = null;
		remefActivacion = "true";
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
			
			if(remefEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.periodo.validacion.exception")));
			}else {
				
				
//				if(remefEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.carrera.validacion.exception")));
//			}else  
				
				
//				if(remefEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.nivel.validacion.exception")));
//			}else{
				
				
				
//				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				remefListEstudianteBusq = servRemefEstudianteDtoServicioJdbc.buscarEstudianteXPeriodocarreraXNivelxParaleloxIdentificacionXApellido(remefEstudianteBuscar.getPracId(), remefEstudianteBuscar.getCrrId(),remefEstudianteBuscar.getNvlId(), 
//						remefEstudianteBuscar.getPrlId(),remefEstudianteBuscar.getPrsIdentificacion(),remefEstudianteBuscar.getPrsPrimerApellido());
				
//				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				remefListEstudianteBusq = servRemefEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXcarreraXNivelXParaleloXAsignaturaXIdentificacionXApellido(remefEstudianteBuscar.getPracId(), remefEstudianteBuscar.getCrrId(),remefEstudianteBuscar.getNvlId(), 
//						remefEstudianteBuscar.getPrlId(), remefEstudianteBuscar.getMtrId(),remefEstudianteBuscar.getPrsIdentificacion(),remefEstudianteBuscar.getPrsPrimerApellido());
				
				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
				remefListEstudianteBusq = servRemefEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXcarreraXNivelXParaleloXAsignaturaXIdentificacionXApellidoReporte(remefEstudianteBuscar.getPracId(), remefEstudianteBuscar.getCrrId(),remefEstudianteBuscar.getNvlId(), 
						remefEstudianteBuscar.getPrlId(), remefEstudianteBuscar.getMtrId(),remefEstudianteBuscar.getPrsIdentificacion(),remefEstudianteBuscar.getPrsPrimerApellido(), remefEstudianteBuscar.getRcesEstado(), remefListCarreraBusq);
				
				//HABILITO BOTON IMPRIMIR
				if(remefListEstudianteBusq.size() > 0 ){ //si existe por lo menos uno se activa
					remefActivacion = "false";
				}
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.no.encontrado.exception")));
		}
	}
	
	
	
	/**
	 * Método que permite buscar la carrera por el periódo academico
	 * @param idPeriodo -  idPeriodo seleccionado para la busqueda
	 */
	public void llenarCarrera(int idPeriodo){
		idPeriodo = remefEstudianteBuscar.getPracId();
		remefEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setRcesEstado(GeneralesConstantes.APP_ID_BASE);
		remefListCarreraBusq = null;
		remefListNivelBusq = null;
		remefListParaleloBusq = null;
		remefListMateriaBusq = null;
		remefListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				if(remefTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					remefListCarreraBusq = servRemefCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(remefUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (remefTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					remefListCarreraBusq = servRemefCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(remefUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if(remefTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					remefListCarreraBusq = servRemefCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(remefUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (remefTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
					remefListCarreraBusq = servRemefCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(remefUsuario.getUsrId(),  RolConstantes.ROL_SECRENIVELACION_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
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
		idCarrera = remefEstudianteBuscar.getCrrId();
		remefEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setRcesEstado(GeneralesConstantes.APP_ID_BASE);
		remefListNivelBusq = null;
		remefListParaleloBusq = null;
		remefListMateriaBusq = null;
		remefListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
//			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS NIVELES
				if(remefTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						remefListNivelBusq = servRemefNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
					}
				}else if (remefTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						remefListNivelBusq = servRemefNivelDtoServicioJdbc.listarNivelXCarreraPosgrado(idCarrera);
					}
				}else if(remefTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						remefListNivelBusq = servRemefNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
					}
				}else if (remefTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
					if(idCarrera != GeneralesConstantes.APP_ID_BASE){
						remefListNivelBusq = servRemefNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
					}
				}
//			}else{
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.nivel.id.carrera.validacion.exception")));
//			}
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
		idPeriodo = remefEstudianteBuscar.getPracId();
		idCarrera = remefEstudianteBuscar.getCrrId();
		idNivel = remefEstudianteBuscar.getNvlId();
		remefEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setRcesEstado(GeneralesConstantes.APP_ID_BASE);
		remefListParaleloBusq = null;
		remefListMateriaBusq = null;
		remefListEstudianteBusq = null;
		
//		efParaleloDtoBusq = new ParaleloDto();
//		efParaleloDtoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		efListParaleloBusq = null;
		
		try{
//			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				//Lista de paralelos
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				remefListParaleloBusq = servRemefParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(idPeriodo, idCarrera, idNivel);
			}
//			}else{
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.paralelo.id.periodo.carrera.nivel.validacion.exception")));
//			}
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.llenar.paralelo.id.periodo.carrera.nivel.validacion.no.encontrado.exception")));
		}
		
	}
	
	/**
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idParalelo){
		idParalelo = remefEstudianteBuscar.getPrlId();
		remefEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteBuscar.setRcesEstado(GeneralesConstantes.APP_ID_BASE);
		remefListMateriaBusq = null;
		try {
			if(idParalelo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				remefListMateriaBusq = servRemefMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlId(remefEstudianteBuscar.getPracId(), remefEstudianteBuscar.getCrrId(), remefEstudianteBuscar.getNvlId(), remefEstudianteBuscar.getPrlId());
			}
//			else {
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
//			}
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que limpia la lista de resultados de acuerdo al cambio de asignatura
	 */
	public void cambiarAsignatura(){
		remefEstudianteBuscar.setRcesEstado(GeneralesConstantes.APP_ID_BASE);
		remefListEstudianteBusq = null;
	}
	
	/**
	 * Método que limpia la lista de resultados de acuerdo al cambio de estado matricula
	 */
	public void cambiarEstadoMatricula(){
		remefListEstudianteBusq = null;
	}
	
	
	/**
	 * Redirecciona a la pagina de ver datos completos del estudiante
	 * @return Navegacion a la página de visualización de datos del estudiante.
	 */
	public String verDatosEstudiante(EstudianteJdbcDto estudiante) {
		String retorno = null;
		 iniciarParametrosVerEstudiante();
		try {
			//DATOS DE ESTUDIANTE 
			remefEstudianteVer = servRemefEstudianteDtoServicioJdbc.buscarEstudianteXIdFichaEstudiante(estudiante.getFcesId());
			
			if(remefEstudianteVer != null){
				//Pais, provincia, cantón de nacimiento
				try {
					remefCantonNacimientoVer = servRemefUbicacionDtoServicioJdbc.buscarXId(remefEstudianteVer.getUbcPaisId());
				
					//Verifico que el dato almacenado sea de una cantón
					if (remefCantonNacimientoVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
						try {
							remefProvinciaNacimientoVer = servRemefUbicacionDtoServicioJdbc
									.buscarPadreXId(remefCantonNacimientoVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefProvinciaNacimientoVer.setUbcDescripcion("N/A");
						}

						try {
							remefPaisNacimientoVer = servRemefUbicacionDtoServicioJdbc
									.buscarPadreXId(remefProvinciaNacimientoVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefPaisNacimientoVer.setUbcDescripcion("N/A");
							
						}

					} else { //Si el dato guardado NO es un cantón, coloco N/A  provincia y canton, coloco el Id en pais.
						remefPaisNacimientoVer=remefCantonNacimientoVer;
						remefCantonNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefCantonNacimientoVer.setUbcDescripcion("N/A");
						remefProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefProvinciaNacimientoVer.setUbcDescripcion("N/A");
//						remefPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
//						remefPaisNacimientoVer.setUbcDescripcion("N/A");
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					remefCantonNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefCantonNacimientoVer.setUbcDescripcion("N/A");
					remefProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefProvinciaNacimientoVer.setUbcDescripcion("N/A");
					remefPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefPaisNacimientoVer.setUbcDescripcion("N/A");
				}

				// Pais, provincia, cantón de residencia
				try {

					remefParroquiaResidenciaVer = servRemefUbicacionDtoServicioJdbc.buscarXId(remefEstudianteVer.getUbcParroquiaId());
					//Verifico que el dato almacenado sea de una parroquia
					if (remefParroquiaResidenciaVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE) {
						try {// busco el Canton con la parroquia de residencia
							remefCantonResidenciaVer = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefParroquiaResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefCantonResidenciaVer.setUbcDescripcion("N/A");
						}

						try { //Busco la provincia con el canton de residencia
							remefProvinciaResidenciaVer = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefCantonResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefProvinciaResidenciaVer.setUbcDescripcion("N/A");
						}

						try {//Busco el pais con la provincia de residencia
							remefPaisResidenciaVer = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefProvinciaResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {

							remefPaisResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefPaisResidenciaVer.setUbcDescripcion("N/A");
						}
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia, canton y parroquia
						remefPaisResidenciaVer=remefParroquiaResidenciaVer;
						remefProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefProvinciaResidenciaVer.setUbcDescripcion("N/A");
						remefCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefCantonResidenciaVer.setUbcDescripcion("N/A");
						remefParroquiaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefParroquiaResidenciaVer.setUbcDescripcion("N/A");
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					remefPaisResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefPaisResidenciaVer.setUbcDescripcion("N/A");
					remefProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefProvinciaResidenciaVer.setUbcDescripcion("N/A");
					remefCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefCantonResidenciaVer.setUbcDescripcion("N/A");
					remefParroquiaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefParroquiaResidenciaVer.setUbcDescripcion("N/A");
					
				}
				  //PAIS, PROVINCIA Y CANTON DEL COLEGIO DEL ESTUDIANTE.
				try {
					remefCantonInacVer = servRemefUbicacionDtoServicioJdbc.buscarXId(remefEstudianteVer.getFcesUbcColegio());
                      //Verifico que el dato almacenado sea de una cantón
					if (remefCantonInacVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {

						try { //Busco la provincia por el canton del colegio.
							remefProvinciaInacVer = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefCantonInacVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefProvinciaInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefProvinciaInacVer.setUbcDescripcion("N/A");
						}

						try {//Busco el país por la provincia del colegio.
							remefPaisInacVer = servRemefUbicacionDtoServicioJdbc
									.buscarPadreXId(remefProvinciaInacVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefPaisInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefPaisInacVer.setUbcDescripcion("N/A");
						}

					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						
						remefPaisInacVer= remefCantonInacVer;
						remefCantonInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefCantonInacVer.setUbcDescripcion("N/A");
						remefProvinciaInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefProvinciaInacVer.setUbcDescripcion("N/A");
						remefEstudianteVer.setTtlDescripcion("N/A");
						remefEstudianteVer.setInacDescripcion("N/A");
					
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					remefCantonInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefCantonInacVer.setUbcDescripcion("N/A");
					remefProvinciaInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefProvinciaInacVer.setUbcDescripcion("N/A");
					remefPaisInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefPaisInacVer.setUbcDescripcion("N/A");
					remefEstudianteVer.setTtlDescripcion("N/A");
					remefEstudianteVer.setInacDescripcion("N/A");

				}
				    
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.validacion.exception")));
			}
			retorno = "irVerDatosEstudiante";
	   } catch (EstudianteDtoJdbcException e) {
		   FacesUtil.limpiarMensaje();
		   FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.estudiante.no.encontrado.exception")));
		}  catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.ver.datos.ubicacion.exception")));
		}
		return retorno;
	}
	
	/**
	 * Redirecciona a la pagina de ver datos completos del estudiante
	 * @return Navegacion a la página de visualización de datos del estudiante.
	 */
	public String editarDatosEstudiante(EstudianteJdbcDto estudiante) {
		String retorno = null;
		iniciarParametrosEditarEstudiante();
		
		//Lista de etnias en BDD
		   try {
			remefListaEtnias=servRemefEtniaServicio.listarTodos();
		} catch (EtniaNoEncontradoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		} catch (EtniaException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		}
		
		   //Listo los paises para el combo de pais de nacimiento
			try {
				remefListaPaisesNac= servRemefUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			} catch (UbicacionDtoJdbcException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.nacimiento.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.nacimiento.no.encontrado.exception")));
			}
			//Listo los paises para el combo de pais de residencia
			try {
				remefListaPaisesRes= servRemefUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			} catch (UbicacionDtoJdbcException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.residencia.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.residencia.no.encontrado.exception")));
			}
		    //Listo los paises para el combo de pais del colegio
			try {
				remefListaPaisesInac= servRemefUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			} catch (UbicacionDtoJdbcException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.colegio.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.colegio.no.encontrado.exception")));
				
			}
			
		try {
			//DATOS DE ESTUDIANTE 
			remefEstudianteEditar = servRemefEstudianteDtoServicioJdbc.buscarEstudianteXIdFichaEstudiante(estudiante.getFcesId());
			if(remefEstudianteEditar != null){
				//PAIS, PROVINCIA, CANTON DE NACIMIENTO
				try {
					remefCantonNacimientoEditar = servRemefUbicacionDtoServicioJdbc.buscarXId(remefEstudianteEditar.getUbcPaisId());
					//Verifico que el dato almacenado sea de una cantón
					if (remefCantonNacimientoEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
						try {//Busco la provincia por el canton de nacimiento
							remefProvinciaNacimientoEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefCantonNacimientoEditar.getUbcId());
							 //Creo la lista de cantones por la provincia guardada en la BDD
							remefListaCantonesNac=   servRemefUbicacionDtoServicioJdbc.listaCatonXProvincia(remefProvinciaNacimientoEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefProvinciaNacimientoEditar.setUbcDescripcion("N/A");
							remefListaCantonesNac= null;
						}

						try {//Busco el pais por la provincia de nacimiento
							remefPaisNacimientoEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefProvinciaNacimientoEditar.getUbcId());
							//Creo la lista de Provincias por el pais guardado en la BDD.
							remefListaProvinciasNac= servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(remefPaisNacimientoEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefPaisNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefPaisNacimientoEditar.setUbcDescripcion("N/A");
							remefListaProvinciasNac=null;
						}

					} else { //Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						
						remefPaisNacimientoEditar=remefCantonNacimientoEditar;
						remefCantonNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefCantonNacimientoEditar.setUbcDescripcion("N/A");
						remefProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefProvinciaNacimientoEditar.setUbcDescripcion("N/A");
						remefListaCantonesNac= null;
						//Busco la lista de Provincias si el pais es ecuador
						if(remefPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
						remefListaProvinciasNac=servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(remefPaisNacimientoEditar.getUbcId());
						}else{
							remefListaProvinciasNac= null;
						}
							
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					remefCantonNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefCantonNacimientoEditar.setUbcDescripcion("N/A");
					remefProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefProvinciaNacimientoEditar.setUbcDescripcion("N/A");
					remefPaisNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefPaisNacimientoEditar.setUbcDescripcion("N/A");
					remefListaCantonesNac= null;
					remefListaProvinciasNac=null;
					
				}

				// PAIS, PROVINCIA, CANTON Y PARROQUIA DE RESIDENCIA
				try {

					remefParroquiaResidenciaEditar = servRemefUbicacionDtoServicioJdbc.buscarXId(remefEstudianteEditar.getUbcParroquiaId());
					
					//Verifico que el dato almacenado sea de una parroquia
					if (remefParroquiaResidenciaEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE) {
						try {//Busco el canton por la parroquia de residencia
							remefCantonResidenciaEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefParroquiaResidenciaEditar.getUbcId());
							//Creo la Lista de Parroquias por el canton guardado en la BDD.
					         remefListaParroquiasRes=servRemefUbicacionDtoServicioJdbc.listaParroquiasXCanton(remefCantonResidenciaEditar.getUbcId());	
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefCantonResidenciaEditar.setUbcDescripcion("N/A");
							remefListaParroquiasRes= null;
						}

						try {//Busco la provincia por el canton de residencia
							remefProvinciaResidenciaEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefCantonResidenciaEditar.getUbcId());
							//Creo la Lista de Cantones por la provincia guardada en la BDD
							remefListaCantonesRes=servRemefUbicacionDtoServicioJdbc.listaCatonXProvincia(remefProvinciaResidenciaEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefProvinciaResidenciaEditar.setUbcDescripcion("N/A");
							remefListaCantonesRes= null;
						}

						try {//Busco el pais por la provincia de residencia
							remefPaisResidenciaEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefProvinciaResidenciaEditar.getUbcId());
							//Creo la Lista de Provincias por Pais guardado en la BDD
							remefListaProvinciasRes= servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(remefPaisResidenciaEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {

							remefPaisResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefPaisResidenciaEditar.setUbcDescripcion("N/A");
						}
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia, canton y parroquia

						remefPaisResidenciaEditar=remefParroquiaResidenciaEditar;
						remefProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefProvinciaResidenciaEditar.setUbcDescripcion("N/A");
						remefCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefCantonResidenciaEditar.setUbcDescripcion("N/A");
						remefParroquiaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefParroquiaResidenciaEditar.setUbcDescripcion("N/A");
						remefListaCantonesRes= null;
						remefListaParroquiasRes= null;
						//Si el dato guardado  es Ecuador busco la lista de provincias.
						if(remefPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
							remefListaProvinciasRes=servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(remefPaisResidenciaEditar.getUbcId());;
							}else{
								remefListaProvinciasRes= null;
							}
						
						
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					remefPaisResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefPaisResidenciaEditar.setUbcDescripcion("N/A");
					remefProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefProvinciaResidenciaEditar.setUbcDescripcion("N/A");
					remefCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefCantonResidenciaEditar.setUbcDescripcion("N/A");
					remefParroquiaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefParroquiaResidenciaEditar.setUbcDescripcion("N/A");
					remefListaProvinciasRes= null;
					remefListaCantonesRes= null;
					remefListaParroquiasRes= null;
				}
				    
				  //PAIS,PROVINCIA, CANTON DEL COLEGIO
				try {
					remefCantonInacEditar = servRemefUbicacionDtoServicioJdbc.buscarXId(remefEstudianteEditar.getFcesUbcColegio());
                      //Verifico que el dato almacenado sea de una cantón
					if (remefCantonInacEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
                         
						try {//Busco la provinci del por el cantón guardado en la BDD 
							remefProvinciaInacEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefCantonInacEditar.getUbcId());
							//Creo la Lista de los cantones por la provincia guardada en la BDD
							remefListaCantonesInac= servRemefUbicacionDtoServicioJdbc.listaCatonXProvincia(remefProvinciaInacEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefProvinciaInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefProvinciaInacEditar.setUbcDescripcion("N/A");
							remefListaCantonesInac= null;
						}

						try {//Busco el pais por la provincia guardada en la BDD.
							remefPaisInacEditar = servRemefUbicacionDtoServicioJdbc.buscarPadreXId(remefProvinciaInacEditar.getUbcId());
							//Creo la lista de Provincias por el pais guardado en la BDD.
							remefListaProvinciasInac= servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(remefPaisInacEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							remefPaisInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							remefPaisInacEditar.setUbcDescripcion("N/A");
							remefListaProvinciasInac= null;
						}
						
						
						try {//Lista las instituciones academicas por el cantón y el tipo de colegio seleccionado.
							remefListaInstitucionesAcademicasDto=servRemefInstitucionAcademiacaDtoServicioJdbc.listarXCantonXTipo(remefCantonInacEditar.getUbcId(), remefEstudianteEditar.getInacTipo());
						} catch (InstitucionAcademicaDtoException e) {
							remefListaInstitucionesAcademicasDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(e.getMessage());
							//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.exception")));
						} catch (InstitucionAcademicaDtoNoEncontradoException e) {
							remefListaInstitucionesAcademicasDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(e.getMessage());
						//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.no.encontrado.exception")));
							
						}

						
						
							try {// Listo los titulos de bachiller.
								remefListaTitulosDto=servRemefTituloDtoServicioJdbc.listarXTipo(TituloConstantes.TIPO_BACHILLER_VALUE);
							} catch (TituloDtoException e) {
								remefListaTitulosDto= null;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(e.getMessage());
								//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.exception")));
							} catch (TituloDtoNoEncontradoException e) {
								remefListaTitulosDto= null;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(e.getMessage());
							//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.no.encontrado.exception")));
							}
												
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						
						remefPaisInacEditar=remefCantonInacEditar;
						remefCantonInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefCantonInacEditar.setUbcDescripcion("N/A");
						remefProvinciaInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						remefProvinciaInacEditar.setUbcDescripcion("N/A");
						remefEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);//seteo el Id de Titulo
						remefListaTitulosDto= null; //vacio  la lista de titulos
						remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
						remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
						remefListaCantonesInac= null; //Vacío la lista de Cantones
						remefListaInstitucionesAcademicasDto=null; //Vacío la lista de Instituciones
						//Si el dato guardado  es Ecuador busco la lista de provincias.
						if(remefPaisInacEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
							remefListaProvinciasInac=servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(remefPaisInacEditar.getUbcId());;
							}else{
								remefListaProvinciasInac= null;
							}
						
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					remefCantonInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefCantonInacEditar.setUbcDescripcion("N/A");
					remefProvinciaInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefProvinciaInacEditar.setUbcDescripcion("N/A");
					remefPaisInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					remefPaisInacEditar.setUbcDescripcion("N/A");
					remefEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);//seteo el Id de Titulo
					remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
					remefListaTitulosDto= null; //vacio  la lista de titulos
					remefListaCantonesInac= null;
					remefListaProvinciasInac= null;
					remefListaInstitucionesAcademicasDto=null;
					
				}
				
				//CONTROL DE BLOQUEO DE VISTA DE ELEMENTOS, ANTES DE PRESENTAR LOS DATOS
				
				if (remefPaisNacimientoEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {
					remefDesactivarUbicacionNac = Boolean.TRUE;
				}

				if (remefEstudianteEditar.getPrsDiscapacidad() == PersonaConstantes.DISCAPACIDAD_NO_VALUE) {
					remefDesactivarDiscapacidad = Boolean.TRUE;
				}

				if (remefEstudianteEditar.getPrsCarnetConadis() == PersonaConstantes.CARNET_CONADIS_NO_VALUE) {
					remefDesactivarDiscapacidad = Boolean.TRUE;
				}

				if (remefPaisResidenciaEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {

					remefDesactivarUbicacionRes = Boolean.TRUE;
				}

				if (remefPaisInacEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {
					remefDesactivarUbicacionInac = Boolean.TRUE;
				}
				
				// FIN CONTROL
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.validacion.exception")));
			}
			retorno = "irEditarDatosEstudiante";
			
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.no.encontrado.exception")));
		}  catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.ubicacion.exception")));
		}
		return retorno;
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	public void iniciarParametros(){
		//ANULO LA LISTA DE ESTUDIANTES
		remefListEstudianteBusq = null;
		//ANULO LA LISTA DE NIVEL
		remefListNivelBusq = null;
		//ANULO LA LISTA DE CARRERAS
		remefListCarreraBusq = null;
		//ANULO LA LISTA DE PARALELOS
		remefListParaleloBusq = null;
		//ANULO LA LISTA DE MATERIAS
		remefListMateriaBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		remefEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO
		remefEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		remefEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL
		remefEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO
		remefEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL DTO DE NIVEL BUSCAR
		remefNivelDtoBusq = new NivelDto();
		//INICIALIZO EL NIVEL ID
		remefNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		remefEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		remefEstudianteBuscar.setPrsPrimerApellido("");
		remefActivacion = "true";
		ClickCerrarModalEditarEstudiante();
	}
	
	//INICIAR PARÁMETROS DE VER ESTUDIANTE
	public void iniciarParametrosVerEstudiante(){
	remefEstudianteVer= null;
	remefPaisResidenciaVer= null;
	remefProvinciaResidenciaVer= null;
	remefCantonResidenciaVer= null;
	remefParroquiaResidenciaVer= null;
	//PARA VISUALIZAR LA NACIONALIDAD DEL ESTUDIANTE
	remefPaisNacimientoVer= null;
	remefProvinciaNacimientoVer= null;
	remefCantonNacimientoVer= null;
	//PARA VISUALIZAR LA UBICACION DEL COLEGIO DEL ESTUDIANTE
	remefPaisInacVer= null;
	remefProvinciaInacVer= null;
	remefCantonInacVer= null;
	}
	
	//INICIAR PARÁMETROS DE EDITAR ESTUDIANTE
	public void iniciarParametrosEditarEstudiante() {
		remefListaPaisesNac = new ArrayList<UbicacionDto>();
		remefListaPaisesRes = new ArrayList<UbicacionDto>();
		remefListaCantonesNac = new ArrayList<UbicacionDto>();
		remefListaProvinciasNac = new ArrayList<UbicacionDto>();
		remefListaParroquiasRes = new ArrayList<UbicacionDto>();
		remefListaCantonesRes = new ArrayList<UbicacionDto>();
		remefListaProvinciasRes = new ArrayList<UbicacionDto>();
		remefListaEtnias= new ArrayList<>();
		remefEstudianteEditar = null;
		remefCantonNacimientoEditar = null;
		remefProvinciaNacimientoEditar = null;
		remefPaisNacimientoEditar = null;
		remefParroquiaResidenciaEditar = null;
		remefCantonResidenciaEditar = null;
		remefProvinciaResidenciaEditar = null;
		remefPaisResidenciaEditar = null;
		remefDesactivarUbicacionInac= Boolean.FALSE;
		remefDesactivarDiscapacidad= Boolean.FALSE;
		remefDesactivarCarnetDiscap= Boolean.FALSE;
	    remefDesactivarUbicacionRes= Boolean.FALSE;
		remefDesactivarUbicacionNac= Boolean.FALSE;

	}
	
	/**
	 * Método que permite buscar la lista estudiantes sin limpiar los filtros 
	 *
	 */
	public String regresaAlistarEstudiante(Usuario usuario){
	String retorno;
	
	remefUsuario=usuario;
	iniciarParametrosVerEstudiante();
	iniciarParametrosEditarEstudiante();
	ClickCerrarModalEditarEstudiante();
	retorno="irListarEstudiante";
	
	return retorno;
	}
	/**
	 * Método que permite buscar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarProvinciasNacEditar(int idPais ){
		remefListaProvinciasNac=null;
		remefProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//remefListaCantonesNac=null;
		remefCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
	    //Actualizar el valor del gentilicio, de acuerdo al pais seleccionado
		UbicacionDto paisSeleccionadAux;
		try {
			paisSeleccionadAux = servRemefUbicacionDtoServicioJdbc.buscarXId(idPais);
			remefPaisNacimientoEditar=paisSeleccionadAux;
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.pais.exception")));
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.pais.no.encontrado.exception")));
		}
		
		
		//LLenar la lista de Provincias.
		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
			try {
				remefListaProvinciasNac = servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
				remefDesactivarUbicacionNac=Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				
				remefListaProvinciasNac=null;
				remefListaCantonesNac=null;
				remefProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefDesactivarUbicacionNac=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.provincia.exception")));
				
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				
				remefListaProvinciasNac=null;
				remefListaCantonesNac=null;
				remefProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefDesactivarUbicacionNac=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.provincia.no.encotrado.exception")));
				
			}
		} else {
			remefListaProvinciasNac=null;
			remefListaCantonesNac=null;
			remefProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			remefCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			remefDesactivarUbicacionNac=Boolean.TRUE;
			
		}
	}
	
	/**
	 * Método que permite buscar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarCantonesNacEditar(int idProvincia){
		remefListaCantonesNac=null;
		remefCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//Llena la lista de Cantones de nacimiento
		try {
			remefListaCantonesNac=servRemefUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.nac.editar.ubicacion.exception")));
			
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.nac.editar.ubicacion.no.encontrado.exception")));
		}
	
	}
	
	/**
	 * Método que permite buscsar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarProvinciasResEditar(int idPais ){
		remefListaProvinciasRes=null;
		remefProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//remefListaCantonesRes=null;
		remefCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//remefListaParroquiasRes=null;
		remefParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
	   		
		//LLenar la lista de Provincias.
		
		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
			try {
				remefListaProvinciasRes = servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
                remefDesactivarUbicacionRes=Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				remefListaProvinciasRes=null;
				remefListaCantonesRes=null;
				remefListaParroquiasRes=null;
				remefProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefDesactivarUbicacionRes=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.res.editar.ubicacion.provincia.exception")));
			
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				remefListaProvinciasRes=null;
				remefListaCantonesRes=null;
				remefListaParroquiasRes=null;
				remefProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				remefDesactivarUbicacionRes=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.res.editar.ubicacion.provincia.no.encontrado.exception")));
			}
		} else {
			remefListaProvinciasRes=null;
			remefListaCantonesRes=null;
			remefListaParroquiasRes=null;
			remefProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			remefCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			remefParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			remefDesactivarUbicacionRes=Boolean.TRUE;
			
		}
			
		
		
		
	}
	
	/**
	 * Método que permite buscsar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarCantonesResEditar(int idProvincia){
		remefListaCantonesRes=null;
		remefCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		remefListaParroquiasRes=null;
		remefParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		
		//Llena la lista de Cantones de nacimiento
		try {
			remefListaCantonesRes=servRemefUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.res.editar.ubicacion.canton.exception")));
	
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.res.editar.ubicacion.canton.no.encontrado.exception")));
		}
	
	
	}
	/**
	 * Método que permite buscsar la lista de parroquias por el id del canton
	 * @param idParroquia - idParroquia seleccionada para la búsqueda
	 */
	public void	llenarParroquiasResEditar(int idCanton){
		remefListaParroquiasRes=null;
		remefParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				
		try {
			remefListaParroquiasRes=servRemefUbicacionDtoServicioJdbc.listaParroquiasXCanton(idCanton);
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.parroquias.res.editar.ubicacion.parroquia.exception")));
	
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.parroquias.res.editar.ubicacion.parroquia.no.encontrado.exception")));
	
		}
		
	}
	
	/**
	 * Método que permite buscar la lista de titulos de instituciones Academicas tipo de institución
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	LlenartitulosInac(){
	    
		remefListaTitulosDto=null;
		remefEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
		remefProvinciaInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		remefCantonInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		remefListaInstitucionesAcademicasDto=null;
		remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
		//Llena la lista de Titulos
						try {
							remefListaTitulosDto=servRemefTituloDtoServicioJdbc.listarXTipo(TituloConstantes.TIPO_BACHILLER_VALUE);
						} catch (TituloDtoException e) {
							remefListaTitulosDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.exception")));
						} catch (TituloDtoNoEncontradoException e) {
							remefListaTitulosDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.no.encontrado.exception")));
						}
	
	}
	
	/**
	 * Método que permite buscsar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarProvinciasInacEditar(int idPais ){
		remefListaProvinciasInac=null;
		remefProvinciaInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//remefListaCantonesInac=null;
		remefCantonInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//remefListaInstitucionesAcademicasDto=null;
		remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
	   
		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
			// LLenar la lista de Provincias.
			try {
				remefListaProvinciasInac = servRemefUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
				LlenartitulosInac();
				remefDesactivarUbicacionInac= Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				remefListaProvinciasInac=null;
				remefListaCantonesInac=null;
				remefListaInstitucionesAcademicasDto=null;
				remefListaTitulosDto=null;
				remefEstudianteEditar.setFcesUbcColegio(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setFcesNotaGradoSecundaria(null);
				remefDesactivarUbicacionInac= Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.inac.editar.ubicacion.provincia.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				remefListaProvinciasInac=null;
				remefListaCantonesInac=null;
				remefListaInstitucionesAcademicasDto=null;
				remefListaTitulosDto=null;
				remefEstudianteEditar.setFcesUbcColegio(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
				remefEstudianteEditar.setFcesNotaGradoSecundaria(null);
				remefDesactivarUbicacionInac= Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.inac.editar.ubicacion.provincia.no.encontrado.exception")));
			}
		}else{
			remefListaProvinciasInac=null;
			remefListaCantonesInac=null;
			remefListaInstitucionesAcademicasDto=null;
			remefListaTitulosDto=null;
			remefEstudianteEditar.setFcesUbcColegio(GeneralesConstantes.APP_ID_BASE);
			remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
			remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
			remefEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
			//remefEstudianteEditar.setFcesNotaGradoSecundaria(null);
			remefDesactivarUbicacionInac= Boolean.TRUE;
		
		
		}
		
	}
	
	/**
	 * Método que permite buscar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarCantonesInacEditar(int idProvincia){
		remefListaCantonesInac=null;
		//remefListaInstitucionesAcademicasDto=null;
		remefCantonInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
		
		//Llena la lista de Cantones de nacimiento
		try {
			remefListaCantonesInac=servRemefUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
		} catch (UbicacionDtoJdbcException e) {
			remefListaInstitucionesAcademicasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.inac.editar.ubicacion.canton.exception")));
	
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			remefListaInstitucionesAcademicasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.inac.editar.ubicacion.canton.no.encontrado.exception")));
		}
	
	
	}
	
	
	/**
	 * Método que permite limpiar las listas de tipo y nombre institución académica al cambiar el cantón.
	*/
	public void	limpiarTipoNombreInacEditar(){
		remefListaInstitucionesAcademicasDto=null;
		remefEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
				
	}
	
	/**
	 * Método que permite buscar la lista de instituciones Academicas por el id de canton y tipo de institución
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarListaInacEditar(int idCanton, int idTipo){
	
		remefListaInstitucionesAcademicasDto=null;
		remefEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
		//Llena la lista de Cantones de nacimiento
			try {
				remefListaInstitucionesAcademicasDto=servRemefInstitucionAcademiacaDtoServicioJdbc.listarXCantonXTipo(idCanton, idTipo );
			} catch (InstitucionAcademicaDtoException e) {
				remefListaInstitucionesAcademicasDto= null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.exception")));
	
			} catch (InstitucionAcademicaDtoNoEncontradoException e) {
				remefListaInstitucionesAcademicasDto= null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.no.encontrado.exception")));
				
			}
	
	}
	
	
	/**
	 * Método que permite desactivar los campos de discapacidad
	 * @param Estado - de la discapacidad 
	 */
	public void	desactivarDiscapacidad(int  estado){
	
		if(estado==PersonaConstantes.DISCAPACIDAD_NO_VALUE){
			remefEstudianteEditar.setPrsCarnetConadis(GeneralesConstantes.APP_ID_BASE);
			remefEstudianteEditar.setPrsNumCarnetConadis(null);
			remefEstudianteEditar.setPrsPorceDiscapacidad(null);
			remefEstudianteEditar.setPrsTipoDiscapacidad(GeneralesConstantes.APP_ID_BASE);
			remefDesactivarDiscapacidad= Boolean.TRUE;
			remefDesactivarCarnetDiscap= Boolean.TRUE;
			
		}else{
			
			remefDesactivarDiscapacidad= Boolean.FALSE;
			remefDesactivarCarnetDiscap= Boolean.FALSE;
		}
		
	
	
	}

	
	/**
	 * Método que permite desactivar los campos de discapacidad
	 * @param Estado - de la discapacidad 
	 */
	public void	desactivarDiscapacidadSinCarnet(int  estado){
	
		if(estado==PersonaConstantes.CARNET_CONADIS_NO_VALUE){
			
			remefEstudianteEditar.setPrsTipoDiscapacidad(GeneralesConstantes.APP_ID_BASE);
			remefEstudianteEditar.setPrsNumCarnetConadis(null);
			remefEstudianteEditar.setPrsPorceDiscapacidad(null);
			remefDesactivarDiscapacidad= Boolean.TRUE;
			
		}else{
			
			remefDesactivarDiscapacidad= Boolean.FALSE;
		}
	
	}
	
	
	
	/**
	 * Método para verificar campos llenos antes de activar modal editar estudiante
	 */
	public void VerificaModalEditarEstudiante() {
		
	    if (remefEstudianteEditar.getEtnId() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.estado.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la autodefinición étnica del estudiante");
	    } else if (remefEstudianteEditar.getPrsSexo() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.tipo.materia.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el sexo del estudiante");
	    } else if (remefEstudianteEditar.getPrsEstadoCivil() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.campo.formacion.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el estado civil del estudiante");
	    } else if (remefPaisNacimientoEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE){
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.unidad.medida.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el pais de nacimiento del estudiante");
	    }else if((remefProvinciaNacimientoEditar.getUbcId()==GeneralesConstantes.APP_ID_BASE)&&(remefPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	FacesUtil.mensajeError("Debe seleccionar la provincia de nacimiento del estudiante");
	    } else if ((remefCantonNacimientoEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (remefPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.nucleo.problemico.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el cantón de nacimiento del estudiante");
	    } else if (remefEstudianteEditar.getPrsDiscapacidad() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.relacion.trabajo.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar si el estudiante presenta discapacidad");
	    } else if((remefEstudianteEditar.getPrsCarnetConadis()==GeneralesConstantes.APP_ID_BASE)&&(remefEstudianteEditar.getPrsDiscapacidad()==PersonaConstantes.DISCAPACIDAD_SI_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar si el estudiante tiene carnet de discapacidad");
	    }else if((remefEstudianteEditar.getPrsTipoDiscapacidad()==GeneralesConstantes.APP_ID_BASE)&&(remefEstudianteEditar.getPrsCarnetConadis()== PersonaConstantes.CARNET_CONADIS_SI_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el tipo de discapacidad del estudiante");
	    } else if((remefEstudianteEditar.getPrsPorceDiscapacidad()== null)&&(remefEstudianteEditar.getPrsCarnetConadis()==PersonaConstantes.CARNET_CONADIS_SI_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el porcentaje de discapacidad del estudiante");
	    } else if (remefPaisResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el pais de residencia del estudiante");
		} else if((remefProvinciaResidenciaEditar.getUbcId()==GeneralesConstantes.APP_ID_BASE)&&(remefPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError("Las horas semanales no pueden ser mayor o igual a las horas semestrales");
			FacesUtil.mensajeError("Debe seleccionar la provincia de residencia del estudiante");
		} else if ((remefCantonResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (remefPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el cantón de residencia del estudiante");
		} else if ((remefParroquiaResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (remefPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la parroquia de residencia del estudiante");
		} else if (remefPaisInacEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el país del colegio del estudiante");	
		} else if ((remefProvinciaInacEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&&(remefPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la provincia del colegio del estudiante");	
		} else if ((remefCantonInacEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&&(remefPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el cantón del colegio del estudiante");	
		} else if ((remefEstudianteEditar.getInacTipo() == GeneralesConstantes.APP_ID_BASE)&&(remefPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el tipo del colegio del estudiante");	
		} else if ((remefEstudianteEditar.getFcesColegioId() == GeneralesConstantes.APP_ID_BASE)&&(remefPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el colegio del estudiante");	
		} else if ((remefEstudianteEditar.getTtlId() == GeneralesConstantes.APP_ID_BASE)&&(remefPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el título de bachiller del estudiante");	
		} else {
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
	  //Si el valor del pais seleccionado es diferente de ecuador, guardo el valor del pais en el campo de canton de nacimiento en la BDD
		if(remefPaisNacimientoEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			remefCantonNacimientoEditar= remefPaisNacimientoEditar;
		}
	
	 //Si el valor del	pais seleccionado como residencia es diferente de ecuador, guardo el valor  del paisen el campo de Parroquia de residencia de la BDD
		if(remefPaisResidenciaEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			remefParroquiaResidenciaEditar=remefPaisResidenciaEditar;
		}
	//Si el valor del pais de colegio es diferente de ecuador, guardo el valor del id_pais en el campo UbcColegio de la BDD	
		if(remefPaisInacEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			remefEstudianteEditar.setFcesUbcColegio(remefPaisInacEditar.getUbcId());
		}else { //Caso contrario guardamos el valor del Canton del Colegio en la BDD
			remefEstudianteEditar.setFcesUbcColegio(remefCantonInacEditar.getUbcId());
		}
		
		//LLama al método actualizar estudiante, con los parametros 	
		
			try {
				if(servRemefPersonaServicio.actualizarEstudianteXSecretaria(remefEstudianteEditar, remefCantonNacimientoEditar, remefParroquiaResidenciaEditar)){
					ClickCerrarModalEditarEstudiante();
					FacesUtil.limpiarMensaje();
                   FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.guardar.estudiante.editado.validacion.exitoso")));
                   
				}else{ // caso que no se ejecute la actualización
					ClickCerrarModalEditarEstudiante();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.guardar.estudiante.editado.validacion.sin.exitoso")));
					
				}
				retorno="irListarEstudiante";
			} catch (PersonaException e) {
				ClickCerrarModalEditarEstudiante();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			
			}
				
		return retorno;
	}

	/**
	 * Método para activar Modal modificar estudiante
	 */
	public void ClickModalEditarEstudiante() {
		remefClickModalEditarEstudiante = 1; // habilita modal modificar
	}

	/**
	 * Método para desactivar Modal modificar estudiante
	 */
	public void ClickCerrarModalEditarEstudiante() {
		remefClickModalEditarEstudiante = 0; // cerrer modal modificar
	}
	
//remefListaInstitucionesAcademicasDto = remefListaInstitucionesAcademicasDto == null ? (new ArrayList<InstitucionAcademicaDto>()) : remefListaInstitucionesAcademicasDto;
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getRemefUsuario() {
		return remefUsuario;
	}

	public void setRemefUsuario(Usuario remefUsuario) {
		this.remefUsuario = remefUsuario;
	}

	public String getRemefTipoCarrera() {
		return remefTipoCarrera;
	}

	public void setRemefTipoCarrera(String remefTipoCarrera) {
		this.remefTipoCarrera = remefTipoCarrera;
	}

	public Integer getRemefTipoUsuario() {
		return remefTipoUsuario;
	}

	public void setRemefTipoUsuario(Integer remefTipoUsuario) {
		this.remefTipoUsuario = remefTipoUsuario;
	}

	public EstudianteJdbcDto getRemefEstudianteBuscar() {
		return remefEstudianteBuscar;
	}

	public void setRemefEstudianteBuscar(EstudianteJdbcDto remefEstudianteBuscar) {
		this.remefEstudianteBuscar = remefEstudianteBuscar;
	}

	public List<CarreraDto> getRemefListCarreraBusq() {
		return remefListCarreraBusq;
	}

	public void setRemefListCarreraBusq(List<CarreraDto> remefListCarreraBusq) {
		this.remefListCarreraBusq = remefListCarreraBusq;
	}

	public List<EstudianteJdbcDto> getRemefListEstudianteBusq() {
		return remefListEstudianteBusq;
	}

	public void setRemefListEstudianteBusq(List<EstudianteJdbcDto> remefListEstudianteBusq) {
		this.remefListEstudianteBusq = remefListEstudianteBusq;
	}

	public List<NivelDto> getRemefListNivelBusq() {
		return remefListNivelBusq;
	}

	public void setRemefListNivelBusq(List<NivelDto> remefListNivelBusq) {
		this.remefListNivelBusq = remefListNivelBusq;
	}

	public List<PeriodoAcademicoDto> getRemefListPeriodoAcademicoBusq() {
		return remefListPeriodoAcademicoBusq;
	}

	public void setRemefListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> remefListPeriodoAcademicoBusq) {
		this.remefListPeriodoAcademicoBusq = remefListPeriodoAcademicoBusq;
	}

	public List<ParaleloDto> getRemefListParaleloBusq() {
		return remefListParaleloBusq;
	}

	public void setRemefListParaleloBusq(List<ParaleloDto> remefListParaleloBusq) {
		this.remefListParaleloBusq = remefListParaleloBusq;
	}

	public NivelDto getRemefNivelDtoBusq() {
		return remefNivelDtoBusq;
	}

	public void setRemefNivelDtoBusq(NivelDto remefNivelDtoBusq) {
		this.remefNivelDtoBusq = remefNivelDtoBusq;
	}

	public ParaleloDto getRemefParaleloDtoBusq() {
		return remefParaleloDtoBusq;
	}

	public void setRemefParaleloDtoBusq(ParaleloDto remefParaleloDtoBusq) {
		this.remefParaleloDtoBusq = remefParaleloDtoBusq;
	}

	public List<MateriaDto> getRemefListMateriaBusq() {
		return remefListMateriaBusq;
	}

	public void setRemefListMateriaBusq(List<MateriaDto> remefListMateriaBusq) {
		this.remefListMateriaBusq = remefListMateriaBusq;
	}

	public EstudianteJdbcDto getRemefEstudianteVer() {
		return remefEstudianteVer;
	}

	public void setRemefEstudianteVer(EstudianteJdbcDto remefEstudianteVer) {
		this.remefEstudianteVer = remefEstudianteVer;
	}

	public String getRemefActivacion() {
		return remefActivacion;
	}

	public void setRemefActivacion(String remefActivacion) {
		this.remefActivacion = remefActivacion;
	}

	public int getRemefActivadorReporte() {
		return remefActivadorReporte;
	}

	public void setRemefActivadorReporte(int remefActivadorReporte) {
		this.remefActivadorReporte = remefActivadorReporte;
	}

	public UbicacionDto getRemefPaisResidenciaVer() {
		return remefPaisResidenciaVer;
	}

	public void setRemefPaisResidenciaVer(UbicacionDto remefPaisResidenciaVer) {
		this.remefPaisResidenciaVer = remefPaisResidenciaVer;
	}

	public UbicacionDto getRemefProvinciaResidenciaVer() {
		return remefProvinciaResidenciaVer;
	}

	public void setRemefProvinciaResidenciaVer(UbicacionDto remefProvinciaResidenciaVer) {
		this.remefProvinciaResidenciaVer = remefProvinciaResidenciaVer;
	}

	public UbicacionDto getRemefCantonResidenciaVer() {
		return remefCantonResidenciaVer;
	}

	public void setRemefCantonResidenciaVer(UbicacionDto remefCantonResidenciaVer) {
		this.remefCantonResidenciaVer = remefCantonResidenciaVer;
	}

	public UbicacionDto getRemefParroquiaResidenciaVer() {
		return remefParroquiaResidenciaVer;
	}

	public void setRemefParroquiaResidenciaVer(UbicacionDto remefParroquiaResidenciaVer) {
		this.remefParroquiaResidenciaVer = remefParroquiaResidenciaVer;
	}

	public UbicacionDto getRemefPaisNacimientoVer() {
		return remefPaisNacimientoVer;
	}

	public void setRemefPaisNacimientoVer(UbicacionDto remefPaisNacimientoVer) {
		this.remefPaisNacimientoVer = remefPaisNacimientoVer;
	}

	public UbicacionDto getRemefProvinciaNacimientoVer() {
		return remefProvinciaNacimientoVer;
	}

	public void setRemefProvinciaNacimientoVer(UbicacionDto remefProvinciaNacimientoVer) {
		this.remefProvinciaNacimientoVer = remefProvinciaNacimientoVer;
	}

	public UbicacionDto getRemefCantonNacimientoVer() {
		return remefCantonNacimientoVer;
	}

	public void setRemefCantonNacimientoVer(UbicacionDto remefCantonNacimientoVer) {
		this.remefCantonNacimientoVer = remefCantonNacimientoVer;
	}

	public UbicacionDto getRemefPaisInacVer() {
		return remefPaisInacVer;
	}

	public void setRemefPaisInacVer(UbicacionDto remefPaisInacVer) {
		this.remefPaisInacVer = remefPaisInacVer;
	}

	public UbicacionDto getRemefProvinciaInacVer() {
		return remefProvinciaInacVer;
	}

	public void setRemefProvinciaInacVer(UbicacionDto remefProvinciaInacVer) {
		this.remefProvinciaInacVer = remefProvinciaInacVer;
	}

	public UbicacionDto getRemefCantonInacVer() {
		return remefCantonInacVer;
	}

	public void setRemefCantonInacVer(UbicacionDto remefCantonInacVer) {
		this.remefCantonInacVer = remefCantonInacVer;
	}

	public EstudianteJdbcDto getRemefEstudianteEditar() {
		return remefEstudianteEditar;
	}

	public void setRemefEstudianteEditar(EstudianteJdbcDto remefEstudianteEditar) {
		this.remefEstudianteEditar = remefEstudianteEditar;
	}

	public UbicacionDto getRemefPaisResidenciaEditar() {
		return remefPaisResidenciaEditar;
	}

	public void setRemefPaisResidenciaEditar(UbicacionDto remefPaisResidenciaEditar) {
		this.remefPaisResidenciaEditar = remefPaisResidenciaEditar;
	}

	public UbicacionDto getRemefProvinciaResidenciaEditar() {
		return remefProvinciaResidenciaEditar;
	}

	public void setRemefProvinciaResidenciaEditar(UbicacionDto remefProvinciaResidenciaEditar) {
		this.remefProvinciaResidenciaEditar = remefProvinciaResidenciaEditar;
	}

	public UbicacionDto getRemefCantonResidenciaEditar() {
		return remefCantonResidenciaEditar;
	}

	public void setRemefCantonResidenciaEditar(UbicacionDto remefCantonResidenciaEditar) {
		this.remefCantonResidenciaEditar = remefCantonResidenciaEditar;
	}

	public UbicacionDto getRemefParroquiaResidenciaEditar() {
		return remefParroquiaResidenciaEditar;
	}

	public void setRemefParroquiaResidenciaEditar(UbicacionDto remefParroquiaResidenciaEditar) {
		this.remefParroquiaResidenciaEditar = remefParroquiaResidenciaEditar;
	}

	public UbicacionDto getRemefPaisNacimientoEditar() {
		return remefPaisNacimientoEditar;
	}

	public void setRemefPaisNacimientoEditar(UbicacionDto remefPaisNacimientoEditar) {
		this.remefPaisNacimientoEditar = remefPaisNacimientoEditar;
	}

	public UbicacionDto getRemefProvinciaNacimientoEditar() {
		return remefProvinciaNacimientoEditar;
	}

	public void setRemefProvinciaNacimientoEditar(UbicacionDto remefProvinciaNacimientoEditar) {
		this.remefProvinciaNacimientoEditar = remefProvinciaNacimientoEditar;
	}

	public UbicacionDto getRemefCantonNacimientoEditar() {
		return remefCantonNacimientoEditar;
	}

	public void setRemefCantonNacimientoEditar(UbicacionDto remefCantonNacimientoEditar) {
		this.remefCantonNacimientoEditar = remefCantonNacimientoEditar;
	}

	public UbicacionDto getRemefPaisInacEditar() {
		return remefPaisInacEditar;
	}

	public void setRemefPaisInacEditar(UbicacionDto remefPaisInacEditar) {
		this.remefPaisInacEditar = remefPaisInacEditar;
	}

	public UbicacionDto getRemefProvinciaInacEditar() {
		return remefProvinciaInacEditar;
	}

	public void setRemefProvinciaInacEditar(UbicacionDto remefProvinciaInacEditar) {
		this.remefProvinciaInacEditar = remefProvinciaInacEditar;
	}

	public UbicacionDto getRemefCantonInacEditar() {
		return remefCantonInacEditar;
	}

	public void setRemefCantonInacEditar(UbicacionDto remefCantonInacEditar) {
		this.remefCantonInacEditar = remefCantonInacEditar;
	}

	public List<UbicacionDto> getRemefListaPaisesNac() {
		return remefListaPaisesNac;
	}

	public void setRemefListaPaisesNac(List<UbicacionDto> remefListaPaisesNac) {
		this.remefListaPaisesNac = remefListaPaisesNac;
	}

	public List<UbicacionDto> getRemefListaProvinciasNac() {
		return remefListaProvinciasNac;
	}

	public void setRemefListaProvinciasNac(List<UbicacionDto> remefListaProvinciasNac) {
		this.remefListaProvinciasNac = remefListaProvinciasNac;
	}

	public List<UbicacionDto> getRemefListaCantonesNac() {
		return remefListaCantonesNac;
	}

	public void setRemefListaCantonesNac(List<UbicacionDto> remefListaCantonesNac) {
		this.remefListaCantonesNac = remefListaCantonesNac;
	}

	public List<UbicacionDto> getRemefListaPaisesRes() {
		return remefListaPaisesRes;
	}

	public void setRemefListaPaisesRes(List<UbicacionDto> remefListaPaisesRes) {
		this.remefListaPaisesRes = remefListaPaisesRes;
	}

	public List<UbicacionDto> getRemefListaProvinciasRes() {
		return remefListaProvinciasRes;
	}

	public void setRemefListaProvinciasRes(List<UbicacionDto> remefListaProvinciasRes) {
		this.remefListaProvinciasRes = remefListaProvinciasRes;
	}

	public List<UbicacionDto> getRemefListaCantonesRes() {
		return remefListaCantonesRes;
	}

	public void setRemefListaCantonesRes(List<UbicacionDto> remefListaCantonesRes) {
		this.remefListaCantonesRes = remefListaCantonesRes;
	}

	public List<UbicacionDto> getRemefListaParroquiasRes() {
		return remefListaParroquiasRes;
	}

	public void setRemefListaParroquiasRes(List<UbicacionDto> remefListaParroquiasRes) {
		this.remefListaParroquiasRes = remefListaParroquiasRes;
	}

	public List<UbicacionDto> getRemefListaPaisesInac() {
		return remefListaPaisesInac;
	}

	public void setRemefListaPaisesInac(List<UbicacionDto> remefListaPaisesInac) {
		this.remefListaPaisesInac = remefListaPaisesInac;
	}

	public List<UbicacionDto> getRemefListaProvinciasInac() {
		return remefListaProvinciasInac;
	}

	public void setRemefListaProvinciasInac(List<UbicacionDto> remefListaProvinciasInac) {
		this.remefListaProvinciasInac = remefListaProvinciasInac;
	}

	public List<UbicacionDto> getRemefListaCantonesInac() {
		return remefListaCantonesInac;
	}

	public void setRemefListaCantonesInac(List<UbicacionDto> remefListaCantonesInac) {
		this.remefListaCantonesInac = remefListaCantonesInac;
	}

	public List<InstitucionAcademicaDto> getRemefListaInstitucionesAcademicasDto() {
		return remefListaInstitucionesAcademicasDto;
	}

	public void setRemefListaInstitucionesAcademicasDto(
			List<InstitucionAcademicaDto> remefListaInstitucionesAcademicasDto) {
		this.remefListaInstitucionesAcademicasDto = remefListaInstitucionesAcademicasDto;
	}

	public List<TituloDto> getRemefListaTitulosDto() {
		return remefListaTitulosDto;
	}

	public void setRemefListaTitulosDto(List<TituloDto> remefListaTitulosDto) {
		this.remefListaTitulosDto = remefListaTitulosDto;
	}

	public List<Etnia> getRemefListaEtnias() {
		return remefListaEtnias;
	}

	public void setRemefListaEtnias(List<Etnia> remefListaEtnias) {
		this.remefListaEtnias = remefListaEtnias;
	}

	public Boolean getRemefDesactivarUbicacionInac() {
		return remefDesactivarUbicacionInac;
	}

	public void setRemefDesactivarUbicacionInac(Boolean remefDesactivarUbicacionInac) {
		this.remefDesactivarUbicacionInac = remefDesactivarUbicacionInac;
	}

	public Boolean getRemefDesactivarDiscapacidad() {
		return remefDesactivarDiscapacidad;
	}

	public void setRemefDesactivarDiscapacidad(Boolean remefDesactivarDiscapacidad) {
		this.remefDesactivarDiscapacidad = remefDesactivarDiscapacidad;
	}

	public Boolean getRemefDesactivarCarnetDiscap() {
		return remefDesactivarCarnetDiscap;
	}

	public void setRemefDesactivarCarnetDiscap(Boolean remefDesactivarCarnetDiscap) {
		this.remefDesactivarCarnetDiscap = remefDesactivarCarnetDiscap;
	}

	public int getRemefClickModalEditarEstudiante() {
		return remefClickModalEditarEstudiante;
	}

	public void setRemefClickModalEditarEstudiante(int remefClickModalEditarEstudiante) {
		this.remefClickModalEditarEstudiante = remefClickModalEditarEstudiante;
	}

	public Boolean getRemefDesactivarUbicacionRes() {
		return remefDesactivarUbicacionRes;
	}

	public void setRemefDesactivarUbicacionRes(Boolean remefDesactivarUbicacionRes) {
		this.remefDesactivarUbicacionRes = remefDesactivarUbicacionRes;
	}

	public Boolean getRemefDesactivarUbicacionNac() {
		return remefDesactivarUbicacionNac;
	}

	public void setRemefDesactivarUbicacionNac(Boolean remefDesactivarUbicacionNac) {
		this.remefDesactivarUbicacionNac = remefDesactivarUbicacionNac;
	}

	public String getRemefNombreReporte() {
		return remefNombreReporte;
	}

	public void setRemefNombreReporte(String remefNombreReporte) {
		this.remefNombreReporte = remefNombreReporte;
	}
	
}
