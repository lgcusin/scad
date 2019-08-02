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
   
 ARCHIVO:     EstudianteListaForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-NOV-2017 			Vinicio Rosales                     Emisión Inicial
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
 * Clase (managed bean) EstudianteListaForm.
 * Managed Bean que administra los estudiantes para la visualización de matriculados.
 * @author jvrosales.
 * @version 1.0
 */

@ManagedBean(name="estudianteListaForm")
@SessionScoped
public class EstudianteListaForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario elfUsuario;
	private String elfTipoCarrera;
	private Integer elfTipoUsuario;
	
	
	//PARA BUSQUEDA
	private EstudianteJdbcDto elfEstudianteBuscar;
	private List<CarreraDto> elfListCarreraBusq;
	private List<EstudianteJdbcDto> elfListEstudianteBusq;
	private List<NivelDto> elfListNivelBusq;
	private List<PeriodoAcademicoDto> elfListPeriodoAcademicoBusq;
	private List<ParaleloDto> elfListParaleloBusq;
	private NivelDto elfNivelDtoBusq;
	private ParaleloDto elfParaleloDtoBusq;
	private List<MateriaDto> elfListMateriaBusq;
	
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto elfEstudianteVer;
	private String elfActivacion;
	private int ecfActivadorReporte;
	//PARA VISUALIZAR LA RESIDENCIA DEL ESTUDIANTE
	private UbicacionDto elfPaisResidenciaVer;
	private UbicacionDto elfProvinciaResidenciaVer;
	private UbicacionDto elfCantonResidenciaVer;
	private UbicacionDto elfParroquiaResidenciaVer;
	
	//PARA VISUALIZAR LA NACIONALIDAD DEL ESTUDIANTE
	private UbicacionDto elfPaisNacimientoVer;
	private UbicacionDto elfProvinciaNacimientoVer;
	private UbicacionDto elfCantonNacimientoVer;
	
	//PARA VISUALIZAR LA UBICACION DEL COLEGIO DEL ESTUDIANTE
	private UbicacionDto elfPaisInacVer;
	private UbicacionDto elfProvinciaInacVer;
	private UbicacionDto elfCantonInacVer;

	//PARA EDITAR DATOS DEL ESTUDIANTE
	private EstudianteJdbcDto elfEstudianteEditar;
	// PARA EDITAR LA RESIDENCIA DEL ESTUDIANTE
	private UbicacionDto elfPaisResidenciaEditar;
	private UbicacionDto elfProvinciaResidenciaEditar;
	private UbicacionDto elfCantonResidenciaEditar;
	private UbicacionDto elfParroquiaResidenciaEditar;

	// PARA EDITAR LA NACIONALIDAD DEL ESTUDIANTE
	private UbicacionDto elfPaisNacimientoEditar;
	private UbicacionDto elfProvinciaNacimientoEditar;
	private UbicacionDto elfCantonNacimientoEditar;

	// PARA EDITAR LA UBICACION DEL COLEGIO DEL ESTUDIANTE
	private UbicacionDto elfPaisInacEditar;
	private UbicacionDto elfProvinciaInacEditar;
	private UbicacionDto elfCantonInacEditar;

	// LISTAS DE PAIS, PROVINCIA, CANTON DE NACIMIENTO
	private List<UbicacionDto> elfListaPaisesNac;
	private List<UbicacionDto> elfListaProvinciasNac;
	private List<UbicacionDto> elfListaCantonesNac;
	
	// LISTAS DE PAIS, PROVINCIA, CANTON DE RESIDENCIA
	private List<UbicacionDto> elfListaPaisesRes;
	private List<UbicacionDto> elfListaProvinciasRes;
	private List<UbicacionDto> elfListaCantonesRes;
	private List<UbicacionDto> elfListaParroquiasRes;

	// LISTAS DE PAIS, PROVINCIA, CANTON DE INSTITUCION ACADEMICA.
	private List<UbicacionDto> elfListaPaisesInac;
	private List<UbicacionDto> elfListaProvinciasInac;
	private List<UbicacionDto> elfListaCantonesInac;
	
	//LISTA DE INSTITUCIONES ACADEMICAS
	
	private List<InstitucionAcademicaDto> elfListaInstitucionesAcademicasDto;
	private List<TituloDto> elfListaTitulosDto;
   //LISTA DE ETNIAS
	private List<Etnia> elfListaEtnias;
	
	//AUXILIARES
	
	private Boolean elfDesactivarUbicacionInac;
	private Boolean elfDesactivarDiscapacidad;
	private Boolean elfDesactivarCarnetDiscap;
	private int elfClickModalEditarEstudiante;
	private Boolean elfDesactivarUbicacionRes;
	private Boolean elfDesactivarUbicacionNac;
	private String elfNombreReporte;
		
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
	private CarreraDtoServicioJdbc servElfCarreraDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servElfEstudianteDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servElfNivelDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servElfUbicacionDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servElfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servElfParaleloDtoServicioJdbc;
	@EJB
	private UsuarioRolServicio servElfUsuarioRolServicio;
	@EJB
	private InstitucionAcademicaDtoServicioJdbc servElfInstitucionAcademiacaDtoServicioJdbc;
	@EJB
	private TituloDtoServicioJdbc servElfTituloDtoServicioJdbc;
	@EJB
	private EtniaServicio servElfEtniaServicio;
	@EJB
	private PersonaServicio servElfPersonaServicio;
	@EJB
	private MateriaDtoServicioJdbc servElfMateriaDtoServicioJdbc;
	@EJB
	private MateriaServicio servMateriaServicio;
	
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR AL ESTUDIANTE
		/**
		 * Dirige la navegacion hacia la pagina de listarEstudiante
		 */
		public String irAlistarEstudiante(Usuario usuario){
			elfUsuario = usuario;
			ecfActivadorReporte = 0;
			String retorno = null;
			try {
				try {
					List<UsuarioRol> usro = servElfUsuarioRolServicio.buscarRolesActivoXUsuario(usuario.getUsrId());
					for (UsuarioRol item : usro) {
						if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
							elfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
							elfTipoCarrera="carreras";
						}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
							elfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
							elfTipoCarrera="programas";
						}else if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
							elfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
							elfTipoCarrera="suficiencias";
						}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
							elfTipoUsuario = RolConstantes.ROL_SECRENIVELACION_VALUE.intValue();
							elfTipoCarrera="areas";
						}
					}
				} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
				}
				//INICIO PARAMETROS DE BUSQUEDA
				iniciarParametros();
				//LISTO LOS PERIODOS ACADEMICOS
				if(elfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					elfListPeriodoAcademicoBusq = servElfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				}else if (elfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					elfListPeriodoAcademicoBusq = servElfPeriodoAcademicoDtoServicioJdbc.listarXEstadoPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);		
				}else if(elfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					//idiomas
					elfListPeriodoAcademicoBusq = servElfPeriodoAcademicoDtoServicioJdbc.listarXEstadoXTipo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				}else if (elfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
					elfListPeriodoAcademicoBusq = servElfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				}
				
				//LISTO LAS CARRERAS ASIGNADAS AL ROL QUE ENTRO A LA FUNCIONALIDAD
				//efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFl(usuario.getUsrId(), RolConstantes.ROL_BD_SECRECARRERA, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
				//LISTO TODOS LOS NIVELES DE LAS MALLAS CURRICULARES
				//efListNivelBusq = servEfNivelDtoServicioJdbc.listarTodos();
				retorno = "irListarEstudiante";
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
		elfEstudianteBuscar = new EstudianteJdbcDto();
		elfListCarreraBusq = null;
		elfListEstudianteBusq = null;
		elfListNivelBusq = null;
		return "irInicio";
	}
	
	public void nada(){
		elfActivacion = "true";
		ecfActivadorReporte = 1;
		try {
			Materia mat = new Materia();
			if(elfEstudianteBuscar.getMtrId() != GeneralesConstantes.APP_ID_BASE){
				mat = servMateriaServicio.buscarPorId(elfEstudianteBuscar.getMtrId());
			}
			
			
			if(elfEstudianteBuscar.getNvlId() == NivelConstantes.NIVEL_NIVELACION_VALUE){
				ReporteSecretariaForm.generarReporteSecretariaEstudiante(elfListEstudianteBusq, elfEstudianteBuscar.getPrlId(), elfEstudianteBuscar.getMtrId(), mat, NivelConstantes.NIVEL_NIVELACION_VALUE);
				elfNombreReporte = "'XLS','reporteSecretariaNivelacion','SECRETARIA'";
			}else{
				ReporteSecretariaForm.generarReporteSecretariaEstudiante(elfListEstudianteBusq, elfEstudianteBuscar.getPrlId(), elfEstudianteBuscar.getMtrId(), mat, GeneralesConstantes.APP_ID_BASE);
				elfNombreReporte = "'XLS','reporteSecretaria','SECRETARIA'";
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
		elfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		elfListEstudianteBusq = null;
		elfActivacion = "true";
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
			
			if(elfEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.periodo.validacion.exception")));
			}else if(elfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.carrera.validacion.exception")));
			}else if(elfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.nivel.validacion.exception")));
			}else{
//				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				elfListEstudianteBusq = servElfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodocarreraXNivelxParaleloxIdentificacionXApellido(elfEstudianteBuscar.getPracId(), elfEstudianteBuscar.getCrrId(),elfEstudianteBuscar.getNvlId(), 
//						elfEstudianteBuscar.getPrlId(),elfEstudianteBuscar.getPrsIdentificacion(),elfEstudianteBuscar.getPrsPrimerApellido());
				
				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
				elfListEstudianteBusq = servElfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXcarreraXNivelXParaleloXAsignaturaXIdentificacionXApellido(elfEstudianteBuscar.getPracId(), elfEstudianteBuscar.getCrrId(),elfEstudianteBuscar.getNvlId(), 
						elfEstudianteBuscar.getPrlId(), elfEstudianteBuscar.getMtrId(),elfEstudianteBuscar.getPrsIdentificacion(),elfEstudianteBuscar.getPrsPrimerApellido());
				
				//HABILITO BOTON IMPRIMIR
				if(elfListEstudianteBusq.size() > 0 ){ //si existe por lo menos uno se activa
					elfActivacion = "false";
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
		idPeriodo = elfEstudianteBuscar.getPracId();
		elfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		elfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		elfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		elfListCarreraBusq = null;
		elfListNivelBusq = null;
		elfListParaleloBusq = null;
		elfListMateriaBusq = null;
		elfListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				if(elfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					elfListCarreraBusq = servElfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(elfUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (elfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					elfListCarreraBusq = servElfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(elfUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if(elfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					elfListCarreraBusq = servElfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(elfUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (elfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
					elfListCarreraBusq = servElfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(elfUsuario.getUsrId(),  RolConstantes.ROL_SECRENIVELACION_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
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
		idCarrera = elfEstudianteBuscar.getCrrId();
		elfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		elfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		elfListNivelBusq = null;
		elfListParaleloBusq = null;
		elfListMateriaBusq = null;
		elfListEstudianteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS NIVELES
				if(elfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					elfListNivelBusq = servElfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if (elfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					elfListNivelBusq = servElfNivelDtoServicioJdbc.listarNivelXCarreraPosgrado(idCarrera);
				}else if(elfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					elfListNivelBusq = servElfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if (elfTipoUsuario == RolConstantes.ROL_SECRENIVELACION_VALUE.intValue()) {
					elfListNivelBusq = servElfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
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
		idPeriodo = elfEstudianteBuscar.getPracId();
		idCarrera = elfEstudianteBuscar.getCrrId();
		idNivel = elfEstudianteBuscar.getNvlId();
		elfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		elfListParaleloBusq = null;
		elfListMateriaBusq = null;
		elfListEstudianteBusq = null;
		
//		efParaleloDtoBusq = new ParaleloDto();
//		efParaleloDtoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		efListParaleloBusq = null;
		
		try{
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				//Lista de paralelos
				elfListParaleloBusq = servElfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(idPeriodo, idCarrera, idNivel);
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
	 * Método que permite buscar la lista de materias por el por el id de paralelo
	 * @param idParalelo - idParalelo seleccionado para la busqueda
	 */
	public void llenarMateria(int idParalelo){
		idParalelo = elfEstudianteBuscar.getPrlId();
		elfEstudianteBuscar.setMtrId(GeneralesConstantes.APP_ID_BASE);
		elfListMateriaBusq = null;
		try {
			if(idParalelo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS MATERIAS
				elfListMateriaBusq = servElfMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlId(elfEstudianteBuscar.getPracId(), elfEstudianteBuscar.getCrrId(), elfEstudianteBuscar.getNvlId(), elfEstudianteBuscar.getPrlId());
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.Docente.llenar.paralelo.id.nivel.validacion.exception")));
			}
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
		elfListEstudianteBusq = null;
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
			elfEstudianteVer = servElfEstudianteDtoServicioJdbc.buscarEstudianteXIdFichaEstudiante(estudiante.getFcesId());
			
			if(elfEstudianteVer != null){
				//Pais, provincia, cantón de nacimiento
				try {
					elfCantonNacimientoVer = servElfUbicacionDtoServicioJdbc.buscarXId(elfEstudianteVer.getUbcPaisId());
				
					//Verifico que el dato almacenado sea de una cantón
					if (elfCantonNacimientoVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
						try {
							elfProvinciaNacimientoVer = servElfUbicacionDtoServicioJdbc
									.buscarPadreXId(elfCantonNacimientoVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfProvinciaNacimientoVer.setUbcDescripcion("N/A");
						}

						try {
							elfPaisNacimientoVer = servElfUbicacionDtoServicioJdbc
									.buscarPadreXId(elfProvinciaNacimientoVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfPaisNacimientoVer.setUbcDescripcion("N/A");
							
						}

					} else { //Si el dato guardado NO es un cantón, coloco N/A  provincia y canton, coloco el Id en pais.
						elfPaisNacimientoVer=elfCantonNacimientoVer;
						elfCantonNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfCantonNacimientoVer.setUbcDescripcion("N/A");
						elfProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfProvinciaNacimientoVer.setUbcDescripcion("N/A");
//						elfPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
//						elfPaisNacimientoVer.setUbcDescripcion("N/A");
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					elfCantonNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfCantonNacimientoVer.setUbcDescripcion("N/A");
					elfProvinciaNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfProvinciaNacimientoVer.setUbcDescripcion("N/A");
					elfPaisNacimientoVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfPaisNacimientoVer.setUbcDescripcion("N/A");
				}

				// Pais, provincia, cantón de residencia
				try {

					elfParroquiaResidenciaVer = servElfUbicacionDtoServicioJdbc.buscarXId(elfEstudianteVer.getUbcParroquiaId());
					//Verifico que el dato almacenado sea de una parroquia
					if (elfParroquiaResidenciaVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE) {
						try {// busco el Canton con la parroquia de residencia
							elfCantonResidenciaVer = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfParroquiaResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfCantonResidenciaVer.setUbcDescripcion("N/A");
						}

						try { //Busco la provincia con el canton de residencia
							elfProvinciaResidenciaVer = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfCantonResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfProvinciaResidenciaVer.setUbcDescripcion("N/A");
						}

						try {//Busco el pais con la provincia de residencia
							elfPaisResidenciaVer = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfProvinciaResidenciaVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {

							elfPaisResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfPaisResidenciaVer.setUbcDescripcion("N/A");
						}
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia, canton y parroquia
						elfPaisResidenciaVer=elfParroquiaResidenciaVer;
						elfProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfProvinciaResidenciaVer.setUbcDescripcion("N/A");
						elfCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfCantonResidenciaVer.setUbcDescripcion("N/A");
						elfParroquiaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfParroquiaResidenciaVer.setUbcDescripcion("N/A");
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					elfPaisResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfPaisResidenciaVer.setUbcDescripcion("N/A");
					elfProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfProvinciaResidenciaVer.setUbcDescripcion("N/A");
					elfCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfCantonResidenciaVer.setUbcDescripcion("N/A");
					elfParroquiaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfParroquiaResidenciaVer.setUbcDescripcion("N/A");
					
				}
				  //PAIS, PROVINCIA Y CANTON DEL COLEGIO DEL ESTUDIANTE.
				try {
					elfCantonInacVer = servElfUbicacionDtoServicioJdbc.buscarXId(elfEstudianteVer.getFcesUbcColegio());
                      //Verifico que el dato almacenado sea de una cantón
					if (elfCantonInacVer.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {

						try { //Busco la provincia por el canton del colegio.
							elfProvinciaInacVer = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfCantonInacVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfProvinciaInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfProvinciaInacVer.setUbcDescripcion("N/A");
						}

						try {//Busco el país por la provincia del colegio.
							elfPaisInacVer = servElfUbicacionDtoServicioJdbc
									.buscarPadreXId(elfProvinciaInacVer.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfPaisInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfPaisInacVer.setUbcDescripcion("N/A");
						}

					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						
						elfPaisInacVer= elfCantonInacVer;
						elfCantonInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfCantonInacVer.setUbcDescripcion("N/A");
						elfProvinciaInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfProvinciaInacVer.setUbcDescripcion("N/A");
						elfEstudianteVer.setTtlDescripcion("N/A");
						elfEstudianteVer.setInacDescripcion("N/A");
					
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					elfCantonInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfCantonInacVer.setUbcDescripcion("N/A");
					elfProvinciaInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfProvinciaInacVer.setUbcDescripcion("N/A");
					elfPaisInacVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfPaisInacVer.setUbcDescripcion("N/A");
					elfEstudianteVer.setTtlDescripcion("N/A");
					elfEstudianteVer.setInacDescripcion("N/A");

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
			elfListaEtnias=servElfEtniaServicio.listarTodos();
		} catch (EtniaNoEncontradoException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		} catch (EtniaException e2) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e2.getMessage());
		}
		
		   //Listo los paises para el combo de pais de nacimiento
			try {
				elfListaPaisesNac= servElfUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			} catch (UbicacionDtoJdbcException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.nacimiento.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.nacimiento.no.encontrado.exception")));
			}
			//Listo los paises para el combo de pais de residencia
			try {
				elfListaPaisesRes= servElfUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			} catch (UbicacionDtoJdbcException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.residencia.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.residencia.no.encontrado.exception")));
			}
		    //Listo los paises para el combo de pais del colegio
			try {
				elfListaPaisesInac= servElfUbicacionDtoServicioJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			} catch (UbicacionDtoJdbcException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.colegio.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.pais.colegio.no.encontrado.exception")));
				
			}
			
		try {
			//DATOS DE ESTUDIANTE 
			elfEstudianteEditar = servElfEstudianteDtoServicioJdbc.buscarEstudianteXIdFichaEstudiante(estudiante.getFcesId());
			if(elfEstudianteEditar != null){
				//PAIS, PROVINCIA, CANTON DE NACIMIENTO
				try {
					elfCantonNacimientoEditar = servElfUbicacionDtoServicioJdbc.buscarXId(elfEstudianteEditar.getUbcPaisId());
					//Verifico que el dato almacenado sea de una cantón
					if (elfCantonNacimientoEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
						try {//Busco la provincia por el canton de nacimiento
							elfProvinciaNacimientoEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfCantonNacimientoEditar.getUbcId());
							 //Creo la lista de cantones por la provincia guardada en la BDD
							elfListaCantonesNac=   servElfUbicacionDtoServicioJdbc.listaCatonXProvincia(elfProvinciaNacimientoEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfProvinciaNacimientoEditar.setUbcDescripcion("N/A");
							elfListaCantonesNac= null;
						}

						try {//Busco el pais por la provincia de nacimiento
							elfPaisNacimientoEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfProvinciaNacimientoEditar.getUbcId());
							//Creo la lista de Provincias por el pais guardado en la BDD.
							elfListaProvinciasNac= servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(elfPaisNacimientoEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfPaisNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfPaisNacimientoEditar.setUbcDescripcion("N/A");
							elfListaProvinciasNac=null;
						}

					} else { //Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						
						elfPaisNacimientoEditar=elfCantonNacimientoEditar;
						elfCantonNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfCantonNacimientoEditar.setUbcDescripcion("N/A");
						elfProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfProvinciaNacimientoEditar.setUbcDescripcion("N/A");
						elfListaCantonesNac= null;
						//Busco la lista de Provincias si el pais es ecuador
						if(elfPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
						elfListaProvinciasNac=servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(elfPaisNacimientoEditar.getUbcId());
						}else{
							elfListaProvinciasNac= null;
						}
							
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					elfCantonNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfCantonNacimientoEditar.setUbcDescripcion("N/A");
					elfProvinciaNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfProvinciaNacimientoEditar.setUbcDescripcion("N/A");
					elfPaisNacimientoEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfPaisNacimientoEditar.setUbcDescripcion("N/A");
					elfListaCantonesNac= null;
					elfListaProvinciasNac=null;
					
				}

				// PAIS, PROVINCIA, CANTON Y PARROQUIA DE RESIDENCIA
				try {

					elfParroquiaResidenciaEditar = servElfUbicacionDtoServicioJdbc.buscarXId(elfEstudianteEditar.getUbcParroquiaId());
					
					//Verifico que el dato almacenado sea de una parroquia
					if (elfParroquiaResidenciaEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_PARROQUIA_VALUE) {
						try {//Busco el canton por la parroquia de residencia
							elfCantonResidenciaEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfParroquiaResidenciaEditar.getUbcId());
							//Creo la Lista de Parroquias por el canton guardado en la BDD.
					         elfListaParroquiasRes=servElfUbicacionDtoServicioJdbc.listaParroquiasXCanton(elfCantonResidenciaEditar.getUbcId());	
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfCantonResidenciaEditar.setUbcDescripcion("N/A");
							elfListaParroquiasRes= null;
						}

						try {//Busco la provincia por el canton de residencia
							elfProvinciaResidenciaEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfCantonResidenciaEditar.getUbcId());
							//Creo la Lista de Cantones por la provincia guardada en la BDD
							elfListaCantonesRes=servElfUbicacionDtoServicioJdbc.listaCatonXProvincia(elfProvinciaResidenciaEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfProvinciaResidenciaEditar.setUbcDescripcion("N/A");
							elfListaCantonesRes= null;
						}

						try {//Busco el pais por la provincia de residencia
							elfPaisResidenciaEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfProvinciaResidenciaEditar.getUbcId());
							//Creo la Lista de Provincias por Pais guardado en la BDD
							elfListaProvinciasRes= servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(elfPaisResidenciaEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {

							elfPaisResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfPaisResidenciaEditar.setUbcDescripcion("N/A");
						}
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia, canton y parroquia

						elfPaisResidenciaEditar=elfParroquiaResidenciaEditar;
						elfProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfProvinciaResidenciaEditar.setUbcDescripcion("N/A");
						elfCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfCantonResidenciaEditar.setUbcDescripcion("N/A");
						elfParroquiaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfParroquiaResidenciaEditar.setUbcDescripcion("N/A");
						elfListaCantonesRes= null;
						elfListaParroquiasRes= null;
						//Si el dato guardado  es Ecuador busco la lista de provincias.
						if(elfPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
							elfListaProvinciasRes=servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(elfPaisResidenciaEditar.getUbcId());;
							}else{
								elfListaProvinciasRes= null;
							}
						
						
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					elfPaisResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfPaisResidenciaEditar.setUbcDescripcion("N/A");
					elfProvinciaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfProvinciaResidenciaEditar.setUbcDescripcion("N/A");
					elfCantonResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfCantonResidenciaEditar.setUbcDescripcion("N/A");
					elfParroquiaResidenciaEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfParroquiaResidenciaEditar.setUbcDescripcion("N/A");
					elfListaProvinciasRes= null;
					elfListaCantonesRes= null;
					elfListaParroquiasRes= null;
				}
				    
				  //PAIS,PROVINCIA, CANTON DEL COLEGIO
				try {
					elfCantonInacEditar = servElfUbicacionDtoServicioJdbc.buscarXId(elfEstudianteEditar.getFcesUbcColegio());
                      //Verifico que el dato almacenado sea de una cantón
					if (elfCantonInacEditar.getUbcJerarquia() == UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE) {
                         
						try {//Busco la provinci del por el cantón guardado en la BDD 
							elfProvinciaInacEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfCantonInacEditar.getUbcId());
							//Creo la Lista de los cantones por la provincia guardada en la BDD
							elfListaCantonesInac= servElfUbicacionDtoServicioJdbc.listaCatonXProvincia(elfProvinciaInacEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfProvinciaInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfProvinciaInacEditar.setUbcDescripcion("N/A");
							elfListaCantonesInac= null;
						}

						try {//Busco el pais por la provincia guardada en la BDD.
							elfPaisInacEditar = servElfUbicacionDtoServicioJdbc.buscarPadreXId(elfProvinciaInacEditar.getUbcId());
							//Creo la lista de Provincias por el pais guardado en la BDD.
							elfListaProvinciasInac= servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(elfPaisInacEditar.getUbcId());
						} catch (UbicacionDtoJdbcNoEncontradoException e) {
							elfPaisInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
							elfPaisInacEditar.setUbcDescripcion("N/A");
							elfListaProvinciasInac= null;
						}
						
						
						try {//Lista las instituciones academicas por el cantón y el tipo de colegio seleccionado.
							elfListaInstitucionesAcademicasDto=servElfInstitucionAcademiacaDtoServicioJdbc.listarXCantonXTipo(elfCantonInacEditar.getUbcId(), elfEstudianteEditar.getInacTipo());
						} catch (InstitucionAcademicaDtoException e) {
							elfListaInstitucionesAcademicasDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(e.getMessage());
							//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.exception")));
						} catch (InstitucionAcademicaDtoNoEncontradoException e) {
							elfListaInstitucionesAcademicasDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(e.getMessage());
						//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.no.encontrado.exception")));
							
						}

						
						
							try {// Listo los titulos de bachiller.
								elfListaTitulosDto=servElfTituloDtoServicioJdbc.listarXTipo(TituloConstantes.TIPO_BACHILLER_VALUE);
							} catch (TituloDtoException e) {
								elfListaTitulosDto= null;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(e.getMessage());
								//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.exception")));
							} catch (TituloDtoNoEncontradoException e) {
								elfListaTitulosDto= null;
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeError(e.getMessage());
							//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.no.encontrado.exception")));
							}
												
					} else {//Si el dato guardado NO es un cantón, coloco N/A  en Pais, provincia y canton
						
						elfPaisInacEditar=elfCantonInacEditar;
						elfCantonInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfCantonInacEditar.setUbcDescripcion("N/A");
						elfProvinciaInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
						elfProvinciaInacEditar.setUbcDescripcion("N/A");
						elfEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);//seteo el Id de Titulo
						elfListaTitulosDto= null; //vacio  la lista de titulos
						elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
						elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
						elfListaCantonesInac= null; //Vacío la lista de Cantones
						elfListaInstitucionesAcademicasDto=null; //Vacío la lista de Instituciones
						//Si el dato guardado  es Ecuador busco la lista de provincias.
						if(elfPaisInacEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE){
							elfListaProvinciasInac=servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(elfPaisInacEditar.getUbcId());;
							}else{
								elfListaProvinciasInac= null;
							}
						
					}

				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					elfCantonInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfCantonInacEditar.setUbcDescripcion("N/A");
					elfProvinciaInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfProvinciaInacEditar.setUbcDescripcion("N/A");
					elfPaisInacEditar = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					elfPaisInacEditar.setUbcDescripcion("N/A");
					elfEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);//seteo el Id de Titulo
					elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
					elfListaTitulosDto= null; //vacio  la lista de titulos
					elfListaCantonesInac= null;
					elfListaProvinciasInac= null;
					elfListaInstitucionesAcademicasDto=null;
					
				}
				
				//CONTROL DE BLOQUEO DE VISTA DE ELEMENTOS, ANTES DE PRESENTAR LOS DATOS
				
				if (elfPaisNacimientoEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {
					elfDesactivarUbicacionNac = Boolean.TRUE;
				}

				if (elfEstudianteEditar.getPrsDiscapacidad() == PersonaConstantes.DISCAPACIDAD_NO_VALUE) {
					elfDesactivarDiscapacidad = Boolean.TRUE;
				}

				if (elfEstudianteEditar.getPrsCarnetConadis() == PersonaConstantes.CARNET_CONADIS_NO_VALUE) {
					elfDesactivarDiscapacidad = Boolean.TRUE;
				}

				if (elfPaisResidenciaEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {

					elfDesactivarUbicacionRes = Boolean.TRUE;
				}

				if (elfPaisInacEditar.getUbcId() != UbicacionConstantes.ECUADOR_VALUE) {
					elfDesactivarUbicacionInac = Boolean.TRUE;
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
		elfListEstudianteBusq = null;
		//ANULO LA LISTA DE NIVEL
		elfListNivelBusq = null;
		//ANULO LA LISTA DE CARRERAS
		elfListCarreraBusq = null;
		//ANULO LA LISTA DE PARALELOS
		elfListParaleloBusq = null;
		//ANULO LA LISTA DE MATERIAS
		elfListMateriaBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		elfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO
		elfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		elfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL
		elfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO
		elfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL DTO DE NIVEL BUSCAR
		elfNivelDtoBusq = new NivelDto();
		//INICIALIZO EL NIVEL ID
		elfNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		elfEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		elfEstudianteBuscar.setPrsPrimerApellido("");
		elfActivacion = "true";
		ClickCerrarModalEditarEstudiante();
	}
	
	//INICIAR PARÁMETROS DE VER ESTUDIANTE
	public void iniciarParametrosVerEstudiante(){
	elfEstudianteVer= null;
	elfPaisResidenciaVer= null;
	elfProvinciaResidenciaVer= null;
	elfCantonResidenciaVer= null;
	elfParroquiaResidenciaVer= null;
	//PARA VISUALIZAR LA NACIONALIDAD DEL ESTUDIANTE
	elfPaisNacimientoVer= null;
	elfProvinciaNacimientoVer= null;
	elfCantonNacimientoVer= null;
	//PARA VISUALIZAR LA UBICACION DEL COLEGIO DEL ESTUDIANTE
	elfPaisInacVer= null;
	elfProvinciaInacVer= null;
	elfCantonInacVer= null;
	}
	
	//INICIAR PARÁMETROS DE EDITAR ESTUDIANTE
	public void iniciarParametrosEditarEstudiante() {
		elfListaPaisesNac = new ArrayList<UbicacionDto>();
		elfListaPaisesRes = new ArrayList<UbicacionDto>();
		elfListaCantonesNac = new ArrayList<UbicacionDto>();
		elfListaProvinciasNac = new ArrayList<UbicacionDto>();
		elfListaParroquiasRes = new ArrayList<UbicacionDto>();
		elfListaCantonesRes = new ArrayList<UbicacionDto>();
		elfListaProvinciasRes = new ArrayList<UbicacionDto>();
		elfListaEtnias= new ArrayList<>();
		elfEstudianteEditar = null;
		elfCantonNacimientoEditar = null;
		elfProvinciaNacimientoEditar = null;
		elfPaisNacimientoEditar = null;
		elfParroquiaResidenciaEditar = null;
		elfCantonResidenciaEditar = null;
		elfProvinciaResidenciaEditar = null;
		elfPaisResidenciaEditar = null;
		elfDesactivarUbicacionInac= Boolean.FALSE;
		elfDesactivarDiscapacidad= Boolean.FALSE;
		elfDesactivarCarnetDiscap= Boolean.FALSE;
	    elfDesactivarUbicacionRes= Boolean.FALSE;
		elfDesactivarUbicacionNac= Boolean.FALSE;

	}
	
	/**
	 * Método que permite buscar la lista estudiantes sin limpiar los filtros 
	 *
	 */
	public String regresaAlistarEstudiante(Usuario usuario){
	String retorno;
	
	elfUsuario=usuario;
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
		elfListaProvinciasNac=null;
		elfProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//elfListaCantonesNac=null;
		elfCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
	    //Actualizar el valor del gentilicio, de acuerdo al pais seleccionado
		UbicacionDto paisSeleccionadAux;
		try {
			paisSeleccionadAux = servElfUbicacionDtoServicioJdbc.buscarXId(idPais);
			elfPaisNacimientoEditar=paisSeleccionadAux;
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
				elfListaProvinciasNac = servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
				elfDesactivarUbicacionNac=Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				
				elfListaProvinciasNac=null;
				elfListaCantonesNac=null;
				elfProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfDesactivarUbicacionNac=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.provincia.exception")));
				
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				
				elfListaProvinciasNac=null;
				elfListaCantonesNac=null;
				elfProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfDesactivarUbicacionNac=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.nac.editar.ubicacion.provincia.no.encotrado.exception")));
				
			}
		} else {
			elfListaProvinciasNac=null;
			elfListaCantonesNac=null;
			elfProvinciaNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			elfCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			elfDesactivarUbicacionNac=Boolean.TRUE;
			
		}
	}
	
	/**
	 * Método que permite buscar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarCantonesNacEditar(int idProvincia){
		elfListaCantonesNac=null;
		elfCantonNacimientoEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//Llena la lista de Cantones de nacimiento
		try {
			elfListaCantonesNac=servElfUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
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
		elfListaProvinciasRes=null;
		elfProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//elfListaCantonesRes=null;
		elfCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//elfListaParroquiasRes=null;
		elfParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
	   		
		//LLenar la lista de Provincias.
		
		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
			try {
				elfListaProvinciasRes = servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
                elfDesactivarUbicacionRes=Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				elfListaProvinciasRes=null;
				elfListaCantonesRes=null;
				elfListaParroquiasRes=null;
				elfProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfDesactivarUbicacionRes=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.res.editar.ubicacion.provincia.exception")));
			
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				elfListaProvinciasRes=null;
				elfListaCantonesRes=null;
				elfListaParroquiasRes=null;
				elfProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				elfDesactivarUbicacionRes=Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.res.editar.ubicacion.provincia.no.encontrado.exception")));
			}
		} else {
			elfListaProvinciasRes=null;
			elfListaCantonesRes=null;
			elfListaParroquiasRes=null;
			elfProvinciaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			elfCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			elfParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
			elfDesactivarUbicacionRes=Boolean.TRUE;
			
		}
			
		
		
		
	}
	
	/**
	 * Método que permite buscsar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarCantonesResEditar(int idProvincia){
		elfListaCantonesRes=null;
		elfCantonResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		elfListaParroquiasRes=null;
		elfParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		
		//Llena la lista de Cantones de nacimiento
		try {
			elfListaCantonesRes=servElfUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
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
		elfListaParroquiasRes=null;
		elfParroquiaResidenciaEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
				
		try {
			elfListaParroquiasRes=servElfUbicacionDtoServicioJdbc.listaParroquiasXCanton(idCanton);
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
	    
		elfListaTitulosDto=null;
		elfEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
		elfProvinciaInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		elfCantonInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		elfListaInstitucionesAcademicasDto=null;
		elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
		//Llena la lista de Titulos
						try {
							elfListaTitulosDto=servElfTituloDtoServicioJdbc.listarXTipo(TituloConstantes.TIPO_BACHILLER_VALUE);
						} catch (TituloDtoException e) {
							elfListaTitulosDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.exception")));
						} catch (TituloDtoNoEncontradoException e) {
							elfListaTitulosDto= null;
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.titulo.no.encontrado.exception")));
						}
	
	}
	
	/**
	 * Método que permite buscsar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarProvinciasInacEditar(int idPais ){
		elfListaProvinciasInac=null;
		elfProvinciaInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//elfListaCantonesInac=null;
		elfCantonInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		//elfListaInstitucionesAcademicasDto=null;
		elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
	   
		if (idPais == UbicacionConstantes.ECUADOR_VALUE) {
			// LLenar la lista de Provincias.
			try {
				elfListaProvinciasInac = servElfUbicacionDtoServicioJdbc.listaProvinciaXPais(idPais);
				LlenartitulosInac();
				elfDesactivarUbicacionInac= Boolean.FALSE;
			} catch (UbicacionDtoJdbcException e) {
				elfListaProvinciasInac=null;
				elfListaCantonesInac=null;
				elfListaInstitucionesAcademicasDto=null;
				elfListaTitulosDto=null;
				elfEstudianteEditar.setFcesUbcColegio(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setFcesNotaGradoSecundaria(null);
				elfDesactivarUbicacionInac= Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.inac.editar.ubicacion.provincia.exception")));
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				elfListaProvinciasInac=null;
				elfListaCantonesInac=null;
				elfListaInstitucionesAcademicasDto=null;
				elfListaTitulosDto=null;
				elfEstudianteEditar.setFcesUbcColegio(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
				elfEstudianteEditar.setFcesNotaGradoSecundaria(null);
				elfDesactivarUbicacionInac= Boolean.TRUE;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.provincia.inac.editar.ubicacion.provincia.no.encontrado.exception")));
			}
		}else{
			elfListaProvinciasInac=null;
			elfListaCantonesInac=null;
			elfListaInstitucionesAcademicasDto=null;
			elfListaTitulosDto=null;
			elfEstudianteEditar.setFcesUbcColegio(GeneralesConstantes.APP_ID_BASE);
			elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE);
			elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
			elfEstudianteEditar.setTtlId(GeneralesConstantes.APP_ID_BASE);
			//elfEstudianteEditar.setFcesNotaGradoSecundaria(null);
			elfDesactivarUbicacionInac= Boolean.TRUE;
		
		
		}
		
	}
	
	/**
	 * Método que permite buscar la lista de provinvias por el id del pais
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarCantonesInacEditar(int idProvincia){
		elfListaCantonesInac=null;
		//elfListaInstitucionesAcademicasDto=null;
		elfCantonInacEditar.setUbcId(GeneralesConstantes.APP_ID_BASE);
		elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
		
		//Llena la lista de Cantones de nacimiento
		try {
			elfListaCantonesInac=servElfUbicacionDtoServicioJdbc.listaCatonXProvincia(idProvincia);
		} catch (UbicacionDtoJdbcException e) {
			elfListaInstitucionesAcademicasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.inac.editar.ubicacion.canton.exception")));
	
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			elfListaInstitucionesAcademicasDto=null;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.llenar.cantones.inac.editar.ubicacion.canton.no.encontrado.exception")));
		}
	
	
	}
	
	
	/**
	 * Método que permite limpiar las listas de tipo y nombre institución académica al cambiar el cantón.
	*/
	public void	limpiarTipoNombreInacEditar(){
		elfListaInstitucionesAcademicasDto=null;
		elfEstudianteEditar.setInacTipo(GeneralesConstantes.APP_ID_BASE); //inicializó el tipo  de Institución Académica.
		elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
				
	}
	
	/**
	 * Método que permite buscar la lista de instituciones Academicas por el id de canton y tipo de institución
	 * @param idPais - idPais seleccionada para la búsqueda
	 */
	public void	llenarListaInacEditar(int idCanton, int idTipo){
	
		elfListaInstitucionesAcademicasDto=null;
		elfEstudianteEditar.setFcesColegioId(GeneralesConstantes.APP_ID_BASE);
		//Llena la lista de Cantones de nacimiento
			try {
				elfListaInstitucionesAcademicasDto=servElfInstitucionAcademiacaDtoServicioJdbc.listarXCantonXTipo(idCanton, idTipo );
			} catch (InstitucionAcademicaDtoException e) {
				elfListaInstitucionesAcademicasDto= null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.editar.datos.estudiante.instituciones.academicas.exception")));
	
			} catch (InstitucionAcademicaDtoNoEncontradoException e) {
				elfListaInstitucionesAcademicasDto= null;
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
			elfEstudianteEditar.setPrsCarnetConadis(GeneralesConstantes.APP_ID_BASE);
			elfEstudianteEditar.setPrsNumCarnetConadis(null);
			elfEstudianteEditar.setPrsPorceDiscapacidad(null);
			elfEstudianteEditar.setPrsTipoDiscapacidad(GeneralesConstantes.APP_ID_BASE);
			elfDesactivarDiscapacidad= Boolean.TRUE;
			elfDesactivarCarnetDiscap= Boolean.TRUE;
			
		}else{
			
			elfDesactivarDiscapacidad= Boolean.FALSE;
			elfDesactivarCarnetDiscap= Boolean.FALSE;
		}
		
	
	
	}

	
	/**
	 * Método que permite desactivar los campos de discapacidad
	 * @param Estado - de la discapacidad 
	 */
	public void	desactivarDiscapacidadSinCarnet(int  estado){
	
		if(estado==PersonaConstantes.CARNET_CONADIS_NO_VALUE){
			
			elfEstudianteEditar.setPrsTipoDiscapacidad(GeneralesConstantes.APP_ID_BASE);
			elfEstudianteEditar.setPrsNumCarnetConadis(null);
			elfEstudianteEditar.setPrsPorceDiscapacidad(null);
			elfDesactivarDiscapacidad= Boolean.TRUE;
			
		}else{
			
			elfDesactivarDiscapacidad= Boolean.FALSE;
		}
	
	}
	
	
	
	/**
	 * Método para verificar campos llenos antes de activar modal editar estudiante
	 */
	public void VerificaModalEditarEstudiante() {
		
	    if (elfEstudianteEditar.getEtnId() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.estado.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la autodefinición étnica del estudiante");
	    } else if (elfEstudianteEditar.getPrsSexo() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.tipo.materia.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el sexo del estudiante");
	    } else if (elfEstudianteEditar.getPrsEstadoCivil() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.campo.formacion.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el estado civil del estudiante");
	    } else if (elfPaisNacimientoEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE){
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.unidad.medida.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el pais de nacimiento del estudiante");
	    }else if((elfProvinciaNacimientoEditar.getUbcId()==GeneralesConstantes.APP_ID_BASE)&&(elfPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	FacesUtil.mensajeError("Debe seleccionar la provincia de nacimiento del estudiante");
	    } else if ((elfCantonNacimientoEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (elfPaisNacimientoEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.nucleo.problemico.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el cantón de nacimiento del estudiante");
	    } else if (elfEstudianteEditar.getPrsDiscapacidad() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//	FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.relacion.trabajo.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar si el estudiante presenta discapacidad");
	    } else if((elfEstudianteEditar.getPrsCarnetConadis()==GeneralesConstantes.APP_ID_BASE)&&(elfEstudianteEditar.getPrsDiscapacidad()==PersonaConstantes.DISCAPACIDAD_SI_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar si el estudiante tiene carnet de discapacidad");
	    }else if((elfEstudianteEditar.getPrsTipoDiscapacidad()==GeneralesConstantes.APP_ID_BASE)&&(elfEstudianteEditar.getPrsCarnetConadis()== PersonaConstantes.CARNET_CONADIS_SI_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el tipo de discapacidad del estudiante");
	    } else if((elfEstudianteEditar.getPrsPorceDiscapacidad()== null)&&(elfEstudianteEditar.getPrsCarnetConadis()==PersonaConstantes.CARNET_CONADIS_SI_VALUE)){
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el porcentaje de discapacidad del estudiante");
	    } else if (elfPaisResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE) {
	    	FacesUtil.limpiarMensaje();
	    	//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el pais de residencia del estudiante");
		} else if((elfProvinciaResidenciaEditar.getUbcId()==GeneralesConstantes.APP_ID_BASE)&&(elfPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)){
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError("Las horas semanales no pueden ser mayor o igual a las horas semestrales");
			FacesUtil.mensajeError("Debe seleccionar la provincia de residencia del estudiante");
		} else if ((elfCantonResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (elfPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el cantón de residencia del estudiante");
		} else if ((elfParroquiaResidenciaEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&& (elfPaisResidenciaEditar.getUbcId()==UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la parroquia de residencia del estudiante");
		} else if (elfPaisInacEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el país del colegio del estudiante");	
		} else if ((elfProvinciaInacEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&&(elfPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar la provincia del colegio del estudiante");	
		} else if ((elfCantonInacEditar.getUbcId() == GeneralesConstantes.APP_ID_BASE)&&(elfPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el cantón del colegio del estudiante");	
		} else if ((elfEstudianteEditar.getInacTipo() == GeneralesConstantes.APP_ID_BASE)&&(elfPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el tipo del colegio del estudiante");	
		} else if ((elfEstudianteEditar.getFcesColegioId() == GeneralesConstantes.APP_ID_BASE)&&(elfPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
			FacesUtil.limpiarMensaje();
			//FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AdministracionMateria.verificar.hora.y.credito.cero.modal.agregar.materia.validacion.exception")));
	    	FacesUtil.mensajeError("Debe seleccionar el colegio del estudiante");	
		} else if ((elfEstudianteEditar.getTtlId() == GeneralesConstantes.APP_ID_BASE)&&(elfPaisInacEditar.getUbcId() == UbicacionConstantes.ECUADOR_VALUE)) {
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
		if(elfPaisNacimientoEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			elfCantonNacimientoEditar= elfPaisNacimientoEditar;
		}
	
	 //Si el valor del	pais seleccionado como residencia es diferente de ecuador, guardo el valor  del paisen el campo de Parroquia de residencia de la BDD
		if(elfPaisResidenciaEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			elfParroquiaResidenciaEditar=elfPaisResidenciaEditar;
		}
	//Si el valor del pais de colegio es diferente de ecuador, guardo el valor del id_pais en el campo UbcColegio de la BDD	
		if(elfPaisInacEditar.getUbcId()!=UbicacionConstantes.ECUADOR_VALUE){
			elfEstudianteEditar.setFcesUbcColegio(elfPaisInacEditar.getUbcId());
		}else { //Caso contrario guardamos el valor del Canton del Colegio en la BDD
			elfEstudianteEditar.setFcesUbcColegio(elfCantonInacEditar.getUbcId());
		}
		
		//LLama al método actualizar estudiante, con los parametros 	
		
			try {
				if(servElfPersonaServicio.actualizarEstudianteXSecretaria(elfEstudianteEditar, elfCantonNacimientoEditar, elfParroquiaResidenciaEditar)){
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
		elfClickModalEditarEstudiante = 1; // habilita modal modificar
	}

	/**
	 * Método para desactivar Modal modificar estudiante
	 */
	public void ClickCerrarModalEditarEstudiante() {
		elfClickModalEditarEstudiante = 0; // cerrer modal modificar
	}

	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	
	

	public int getEcfActivadorReporte() {
		return ecfActivadorReporte;
	}

	public Usuario getElfUsuario() {
		return elfUsuario;
	}

	public void setElfUsuario(Usuario elfUsuario) {
		this.elfUsuario = elfUsuario;
	}

	public String getElfTipoCarrera() {
		return elfTipoCarrera;
	}

	public void setElfTipoCarrera(String elfTipoCarrera) {
		this.elfTipoCarrera = elfTipoCarrera;
	}

	public Integer getElfTipoUsuario() {
		return elfTipoUsuario;
	}

	public void setElfTipoUsuario(Integer elfTipoUsuario) {
		this.elfTipoUsuario = elfTipoUsuario;
	}

	public EstudianteJdbcDto getElfEstudianteBuscar() {
		return elfEstudianteBuscar;
	}

	public void setElfEstudianteBuscar(EstudianteJdbcDto elfEstudianteBuscar) {
		this.elfEstudianteBuscar = elfEstudianteBuscar;
	}

	public List<CarreraDto> getElfListCarreraBusq() {
		elfListCarreraBusq = elfListCarreraBusq == null ? (new ArrayList<CarreraDto>()) : elfListCarreraBusq;
		return elfListCarreraBusq;
	}

	public void setElfListCarreraBusq(List<CarreraDto> elfListCarreraBusq) {
		this.elfListCarreraBusq = elfListCarreraBusq;
	}

	public List<EstudianteJdbcDto> getElfListEstudianteBusq() {
		elfListEstudianteBusq = elfListEstudianteBusq == null ? (new ArrayList<EstudianteJdbcDto>()) : elfListEstudianteBusq;
		return elfListEstudianteBusq;
	}

	public void setElfListEstudianteBusq(List<EstudianteJdbcDto> elfListEstudianteBusq) {
		this.elfListEstudianteBusq = elfListEstudianteBusq;
	}

	public List<NivelDto> getElfListNivelBusq() {
		elfListNivelBusq = elfListNivelBusq == null ? (new ArrayList<NivelDto>()) : elfListNivelBusq;
		return elfListNivelBusq;
	}

	public void setElfListNivelBusq(List<NivelDto> elfListNivelBusq) {
		this.elfListNivelBusq = elfListNivelBusq;
	}

	public List<PeriodoAcademicoDto> getElfListPeriodoAcademicoBusq() {
		elfListPeriodoAcademicoBusq = elfListPeriodoAcademicoBusq == null ? (new ArrayList<PeriodoAcademicoDto>()) : elfListPeriodoAcademicoBusq;
		return elfListPeriodoAcademicoBusq;
	}

	public void setElfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> elfListPeriodoAcademicoBusq) {
		this.elfListPeriodoAcademicoBusq = elfListPeriodoAcademicoBusq;
	}

	public List<ParaleloDto> getElfListParaleloBusq() {
		elfListParaleloBusq = elfListParaleloBusq == null ? (new ArrayList<ParaleloDto>()) : elfListParaleloBusq;
		return elfListParaleloBusq;
	}

	public void setElfListParaleloBusq(List<ParaleloDto> elfListParaleloBusq) {
		this.elfListParaleloBusq = elfListParaleloBusq;
	}

	public NivelDto getElfNivelDtoBusq() {
		return elfNivelDtoBusq;
	}

	public void setElfNivelDtoBusq(NivelDto elfNivelDtoBusq) {
		this.elfNivelDtoBusq = elfNivelDtoBusq;
	}

	public ParaleloDto getElfParaleloDtoBusq() {
		return elfParaleloDtoBusq;
	}

	public void setElfParaleloDtoBusq(ParaleloDto elfParaleloDtoBusq) {
		this.elfParaleloDtoBusq = elfParaleloDtoBusq;
	}

	public List<MateriaDto> getElfListMateriaBusq() {
		elfListMateriaBusq = elfListMateriaBusq == null ? (new ArrayList<MateriaDto>()) : elfListMateriaBusq;
		return elfListMateriaBusq;
	}

	public void setElfListMateriaBusq(List<MateriaDto> elfListMateriaBusq) {
		this.elfListMateriaBusq = elfListMateriaBusq;
	}

	public EstudianteJdbcDto getElfEstudianteVer() {
		return elfEstudianteVer;
	}

	public void setElfEstudianteVer(EstudianteJdbcDto elfEstudianteVer) {
		this.elfEstudianteVer = elfEstudianteVer;
	}

	public String getElfActivacion() {
		return elfActivacion;
	}

	public void setElfActivacion(String elfActivacion) {
		this.elfActivacion = elfActivacion;
	}

	public void setEcfActivadorReporte(int ecfActivadorReporte) {
		this.ecfActivadorReporte = ecfActivadorReporte;
	}

	public UbicacionDto getElfPaisResidenciaVer() {
		return elfPaisResidenciaVer;
	}

	public void setElfPaisResidenciaVer(UbicacionDto elfPaisResidenciaVer) {
		this.elfPaisResidenciaVer = elfPaisResidenciaVer;
	}

	public UbicacionDto getElfCantonResidenciaVer() {
		return elfCantonResidenciaVer;
	}

	public void setElfCantonResidenciaVer(UbicacionDto elfCantonResidenciaVer) {
		this.elfCantonResidenciaVer = elfCantonResidenciaVer;
	}

	public UbicacionDto getElfProvinciaResidenciaVer() {
		return elfProvinciaResidenciaVer;
	}

	public void setElfProvinciaResidenciaVer(UbicacionDto elfProvinciaResidenciaVer) {
		this.elfProvinciaResidenciaVer = elfProvinciaResidenciaVer;
	}

	public UbicacionDto getElfPaisNacimientoVer() {
		return elfPaisNacimientoVer;
	}

	public void setElfPaisNacimientoVer(UbicacionDto elfPaisNacimientoVer) {
		this.elfPaisNacimientoVer = elfPaisNacimientoVer;
	}

	public UbicacionDto getElfProvinciaNacimientoVer() {
		return elfProvinciaNacimientoVer;
	}

	public void setElfProvinciaNacimientoVer(UbicacionDto elfProvinciaNacimientoVer) {
		this.elfProvinciaNacimientoVer = elfProvinciaNacimientoVer;
	}

	public UbicacionDto getElfCantonNacimientoVer() {
		return elfCantonNacimientoVer;
	}

	public void setElfCantonNacimientoVer(UbicacionDto elfCantonNacimientoVer) {
		this.elfCantonNacimientoVer = elfCantonNacimientoVer;
	}

	public UbicacionDto getElfPaisInacVer() {
		return elfPaisInacVer;
	}

	public void setElfPaisInacVer(UbicacionDto elfPaisInacVer) {
		this.elfPaisInacVer = elfPaisInacVer;
	}

	public UbicacionDto getElfProvinciaInacVer() {
		return elfProvinciaInacVer;
	}

	public void setElfProvinciaInacVer(UbicacionDto elfProvinciaInacVer) {
		this.elfProvinciaInacVer = elfProvinciaInacVer;
	}

	public UbicacionDto getElfCantonInacVer() {
		return elfCantonInacVer;
	}

	public void setElfCantonInacVer(UbicacionDto elfCantonInacVer) {
		this.elfCantonInacVer = elfCantonInacVer;
	}

	public EstudianteJdbcDto getElfEstudianteEditar() {
		return elfEstudianteEditar;
	}

	public void setElfEstudianteEditar(EstudianteJdbcDto elfEstudianteEditar) {
		this.elfEstudianteEditar = elfEstudianteEditar;
	}

	public UbicacionDto getElfPaisResidenciaEditar() {
		return elfPaisResidenciaEditar;
	}

	public void setElfPaisResidenciaEditar(UbicacionDto elfPaisResidenciaEditar) {
		this.elfPaisResidenciaEditar = elfPaisResidenciaEditar;
	}

	public UbicacionDto getElfCantonResidenciaEditar() {
		return elfCantonResidenciaEditar;
	}

	public void setElfCantonResidenciaEditar(UbicacionDto elfCantonResidenciaEditar) {
		this.elfCantonResidenciaEditar = elfCantonResidenciaEditar;
	}

	public UbicacionDto getElfProvinciaResidenciaEditar() {
		return elfProvinciaResidenciaEditar;
	}

	public void setElfProvinciaResidenciaEditar(UbicacionDto elfProvinciaResidenciaEditar) {
		this.elfProvinciaResidenciaEditar = elfProvinciaResidenciaEditar;
	}

	public UbicacionDto getElfPaisNacimientoEditar() {
		return elfPaisNacimientoEditar;
	}

	public void setElfPaisNacimientoEditar(UbicacionDto elfPaisNacimientoEditar) {
		this.elfPaisNacimientoEditar = elfPaisNacimientoEditar;
	}

	public UbicacionDto getElfProvinciaNacimientoEditar() {
		return elfProvinciaNacimientoEditar;
	}

	public void setElfProvinciaNacimientoEditar(UbicacionDto elfProvinciaNacimientoEditar) {
		this.elfProvinciaNacimientoEditar = elfProvinciaNacimientoEditar;
	}

	public UbicacionDto getElfCantonNacimientoEditar() {
		return elfCantonNacimientoEditar;
	}

	public void setElfCantonNacimientoEditar(UbicacionDto elfCantonNacimientoEditar) {
		this.elfCantonNacimientoEditar = elfCantonNacimientoEditar;
	}

	public UbicacionDto getElfPaisInacEditar() {
		return elfPaisInacEditar;
	}

	public void setElfPaisInacEditar(UbicacionDto elfPaisInacEditar) {
		this.elfPaisInacEditar = elfPaisInacEditar;
	}

	public UbicacionDto getElfProvinciaInacEditar() {
		return elfProvinciaInacEditar;
	}

	public void setElfProvinciaInacEditar(UbicacionDto elfProvinciaInacEditar) {
		this.elfProvinciaInacEditar = elfProvinciaInacEditar;
	}

	public UbicacionDto getElfCantonInacEditar() {
		return elfCantonInacEditar;
	}

	public void setElfCantonInacEditar(UbicacionDto elfCantonInacEditar) {
		this.elfCantonInacEditar = elfCantonInacEditar;
	}

	
	public List<UbicacionDto> getElfListaPaisesNac() {
		elfListaPaisesNac = elfListaPaisesNac == null ? (new ArrayList<UbicacionDto>()) : elfListaPaisesNac;
		return elfListaPaisesNac;
	}

	public void setElfListaPaisesNac(List<UbicacionDto> elfListaPaisesNac) {
		this.elfListaPaisesNac = elfListaPaisesNac;
	}

	public List<UbicacionDto> getElfListaProvinciasNac() {
		elfListaProvinciasNac = elfListaProvinciasNac == null ? (new ArrayList<UbicacionDto>()) : elfListaProvinciasNac;
		return elfListaProvinciasNac;
	}

	public void setElfListaProvinciasNac(List<UbicacionDto> elfListaProvinciasNac) {
		this.elfListaProvinciasNac = elfListaProvinciasNac;
	}

	public List<UbicacionDto> getElfListaCantonesNac() {
		elfListaCantonesNac = elfListaCantonesNac == null ? (new ArrayList<UbicacionDto>()) : elfListaCantonesNac;
		return elfListaCantonesNac;
	}

	public void setElfListaCantonesNac(List<UbicacionDto> elfListaCantonesNac) {
		this.elfListaCantonesNac = elfListaCantonesNac;
	}

	public List<UbicacionDto> getElfListaPaisesRes() {
		elfListaPaisesRes = elfListaPaisesRes == null ? (new ArrayList<UbicacionDto>()) : elfListaPaisesRes;
		return elfListaPaisesRes;
	}

	public void setElfListaPaisesRes(List<UbicacionDto> elfListaPaisesRes) {
		this.elfListaPaisesRes = elfListaPaisesRes;
	}

	public List<UbicacionDto> getElfListaProvinciasRes() {
		elfListaProvinciasRes = elfListaProvinciasRes == null ? (new ArrayList<UbicacionDto>()) : elfListaProvinciasRes;
		return elfListaProvinciasRes;
	}

	public void setElfListaProvinciasRes(List<UbicacionDto> elfListaProvinciasRes) {
		this.elfListaProvinciasRes = elfListaProvinciasRes;
	}

	public List<UbicacionDto> getElfListaCantonesRes() {
		elfListaCantonesRes = elfListaCantonesRes == null ? (new ArrayList<UbicacionDto>()) : elfListaCantonesRes;
		return elfListaCantonesRes;
	}

	public void setElfListaCantonesRes(List<UbicacionDto> elfListaCantonesRes) {
		this.elfListaCantonesRes = elfListaCantonesRes;
	}

	public List<UbicacionDto> getElfListaParroquiasRes() {
		elfListaParroquiasRes = elfListaParroquiasRes == null ? (new ArrayList<UbicacionDto>()) : elfListaParroquiasRes;
		return elfListaParroquiasRes;
	}

	public void setElfListaParroquiasRes(List<UbicacionDto> elfListaParroquiasRes) {
		this.elfListaParroquiasRes = elfListaParroquiasRes;
	}

	public List<UbicacionDto> getElfListaPaisesInac() {
		elfListaPaisesInac = elfListaPaisesInac == null ? (new ArrayList<UbicacionDto>()) : elfListaPaisesInac;
		return elfListaPaisesInac;
	}

	public void setElfListaPaisesInac(List<UbicacionDto> elfListaPaisesInac) {
		this.elfListaPaisesInac = elfListaPaisesInac;
	}

	public List<UbicacionDto> getElfListaProvinciasInac() {
		elfListaProvinciasInac = elfListaProvinciasInac == null ? (new ArrayList<UbicacionDto>()) : elfListaProvinciasInac;
		return elfListaProvinciasInac;
	}

	public void setElfListaProvinciasInac(List<UbicacionDto> elfListaProvinciasInac) {
		this.elfListaProvinciasInac = elfListaProvinciasInac;
	}

	public List<UbicacionDto> getElfListaCantonesInac() {
		elfListaCantonesInac = elfListaCantonesInac == null ? (new ArrayList<UbicacionDto>()) : elfListaCantonesInac;
		return elfListaCantonesInac;
	}

	public void setElfListaCantonesInac(List<UbicacionDto> elfListaCantonesInac) {
		this.elfListaCantonesInac = elfListaCantonesInac;
	}

	public UbicacionDto getElfParroquiaResidenciaVer() {
		return elfParroquiaResidenciaVer;
	}

	public void setElfParroquiaResidenciaVer(UbicacionDto elfParroquiaResidenciaVer) {
		this.elfParroquiaResidenciaVer = elfParroquiaResidenciaVer;
	}

	public UbicacionDto getElfParroquiaResidenciaEditar() {
		return elfParroquiaResidenciaEditar;
	}

	public void setElfParroquiaResidenciaEditar(UbicacionDto elfParroquiaResidenciaEditar) {
		this.elfParroquiaResidenciaEditar = elfParroquiaResidenciaEditar;
	}

	public List<InstitucionAcademicaDto> getElfListaInstitucionesAcademicasDto() {
		elfListaInstitucionesAcademicasDto = elfListaInstitucionesAcademicasDto == null ? (new ArrayList<InstitucionAcademicaDto>()) : elfListaInstitucionesAcademicasDto;
		return elfListaInstitucionesAcademicasDto;
	}

	public void setElfListaInstitucionesAcademicasDto(List<InstitucionAcademicaDto> elfListaInstitucionesAcademicasDto) {
		this.elfListaInstitucionesAcademicasDto = elfListaInstitucionesAcademicasDto;
	}

	public Boolean getElfDesactivarUbicacionInac() {
		return elfDesactivarUbicacionInac;
	}

	public void setElfDesactivarUbicacionInac(Boolean elfDesactivarUbicacionInac) {
		this.elfDesactivarUbicacionInac = elfDesactivarUbicacionInac;
	}

	public List<TituloDto> getElfListaTitulosDto() {
		elfListaTitulosDto = elfListaTitulosDto == null ? (new ArrayList<TituloDto>()) : elfListaTitulosDto;
		return elfListaTitulosDto;
	}

	public void setElfListaTitulosDto(List<TituloDto> elfListaTitulosDto) {
		this.elfListaTitulosDto = elfListaTitulosDto;
	}

	public Boolean getElfDesactivarDiscapacidad() {
		return elfDesactivarDiscapacidad;
	}

	public void setElfDesactivarDiscapacidad(Boolean elfDesactivarDiscapacidad) {
		this.elfDesactivarDiscapacidad = elfDesactivarDiscapacidad;
	}

		
	public Boolean getElfDesactivarCarnetDiscap() {
		return elfDesactivarCarnetDiscap;
	}

	public void setElfDesactivarCarnetDiscap(Boolean elfDesactivarCarnetDiscap) {
		this.elfDesactivarCarnetDiscap = elfDesactivarCarnetDiscap;
	}

	public int getElfClickModalEditarEstudiante() {
		return elfClickModalEditarEstudiante;
	}

	public void setElfClickModalEditarEstudiante(int elfClickModalEditarEstudiante) {
		this.elfClickModalEditarEstudiante = elfClickModalEditarEstudiante;
	}

	public Boolean getElfDesactivarUbicacionRes() {
		return elfDesactivarUbicacionRes;
	}

	public void setElfDesactivarUbicacionRes(Boolean elfDesactivarUbicacionRes) {
		this.elfDesactivarUbicacionRes = elfDesactivarUbicacionRes;
	}

	public Boolean getElfDesactivarUbicacionNac() {
		return elfDesactivarUbicacionNac;
	}

	public void setElfDesactivarUbicacionNac(Boolean elfDesactivarUbicacionNac) {
		this.elfDesactivarUbicacionNac = elfDesactivarUbicacionNac;
	}

	public List<Etnia> getElfListaEtnias() {
		elfListaEtnias = elfListaEtnias == null ? (new ArrayList<Etnia>()) : elfListaEtnias;
		return elfListaEtnias;
	}

	public void setElfListaEtnias(List<Etnia> elfListaEtnias) {
		this.elfListaEtnias = elfListaEtnias;
	}

	public String getElfNombreReporte() {
		return elfNombreReporte;
	}

	public void setElfNombreReporte(String elfNombreReporte) {
		this.elfNombreReporte = elfNombreReporte;
	}
	
}
