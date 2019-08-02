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
   
 ARCHIVO:     EstudianteCertificadoNotasForm.java	  
 DESCRIPCION: Bean de sesion que maneja los estudiantes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-SEPT-2017 			Vinicio Rosales                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.certificado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularMateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCertificadoNotasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteCertificadoNotasForm.
 * Managed Bean que administra los estudiantes para la visualización de los certificados de notas.
 * @author jvrosales.
 * @version 1.0
 */


@ManagedBean(name="estudianteCertificadoNotasForm")
@SessionScoped
public class EstudianteCertificadoNotasForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	//GENERAL
	private Usuario ecnfUsuario;
	
	//PARA BUSQUEDA
	private EstudianteJdbcDto ecnfEstudianteBuscar;
	private List<CarreraDto> ecnfListCarreraBusq;
	private List<EstudianteJdbcDto> ecnfListEstudianteBusq;
	private List<EstudianteJdbcDto> ecnflistEstudianteReporteBusq;
	private List<NivelDto> ecnfListNivelBusq;
	private List<PeriodoAcademicoDto> ecnfListPeriodoAcademicoBusq;
	private List<ParaleloDto> ecnfListParaleloBusq;
	private List<Dependencia> ecnfListDependenciaBusq;
	private NivelDto ecnfNivelDtoBusq;
	private ParaleloDto ecnfParaleloDtoBusq;
	private String ecnfTipoCarrera;
	private Integer ecnfTipoUsuario;
	//PARA LA VISUALIZACION DE INFORMACIÓN DEL ESTUDIANTE
	private EstudianteJdbcDto ecnfEstudianteVer;
	private UbicacionDto ecnfCantonResidenciaVer;
	private UbicacionDto ecnfPaisResidenciaVer;
	private UbicacionDto ecnfProvinciaResidenciaVer;
	private String ecnfActivacion;
	private int ecnfActivarReporte;
//	private List<MateriaDto> ecnflistMateriasNotas;
	private Carrera ecnfCarreraBuscar;
	private PersonaDto ecnfSecretarioBuscar;
	private String ecnfUsuarioDescripcion;
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
	private CarreraDtoServicioJdbc servEcnfCarreraDtoServicioJdbc;
	@EJB
	private EstudianteDtoServicioJdbc servEcnfEstudianteDtoServicioJdbc;
	@EJB
	private NivelDtoServicioJdbc servEcnfNivelDtoServicioJdbc;
	@EJB
	private UbicacionDtoServicioJdbc servEcnfUbicacionDtoServicioJdbc;
	@EJB
	private PeriodoAcademicoDtoServicioJdbc servEcnfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	private ParaleloDtoServicioJdbc servEcnfParaleloDtoServicioJdbc;
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB 
	private MateriaDtoServicioJdbc  servEcnfMateriaDto;
	@EJB
	private CarreraServicio servEcnfCarreraServicio;
	@EJB
	private PersonaDtoServicioJdbc servEcnfPersonaServicio;
	@EJB
	private DependenciaDtoServicioJdbc servEcnfDependenciaDtoServicioJdbc;
	@EJB 
	private MateriaServicio  servEcnfMateria;
	@EJB
	private MallaCurricularMateriaServicio servMallaCurricularMateriaServicio;
	@EJB
	private MallaCurricularParaleloServicio servMallaCurricularParaleloServicio;
	@EJB
	private RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	@EJB
	private DependenciaServicio servDependenciaServicio;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//PARA IR AL ESTUDIANTE
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiante(Usuario usuario){
		ecnfUsuario = usuario;
		ecnfActivarReporte = 0;
		String retorno = null;
		try {
			try {
				List<UsuarioRol> usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
				for (UsuarioRol item : usro) {
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						ecnfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
						ecnfTipoCarrera="carreras";
					}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
						ecnfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
						ecnfTipoCarrera="programas";
					}
				}
			} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			}
			
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros();
			//LISTO LOS PERIODOS ACADEMICOS
				if(ecnfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ecnfListPeriodoAcademicoBusq = servEcnfPeriodoAcademicoDtoServicioJdbc.listarTodos();
				}else if (ecnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ecnfListPeriodoAcademicoBusq = servEcnfPeriodoAcademicoDtoServicioJdbc.listarTodosPosgrado();
				}
			//LISTO LAS CARRERAS ASIGNADAS AL ROL QUE ENTRO A LA FUNCIONALIDAD
			//efListCarreraBusq = servEfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFl(usuario.getUsrId(), RolConstantes.ROL_BD_SECRECARRERA, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE);
			//LISTO TODOS LOS NIVELES DE LAS MALLAS CURRICULARES
			//efListNivelBusq = servEfNivelDtoServicioJdbc.listarTodos();
				
				
			retorno = "irListarEstudianteCertificadoNotas";
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiantePosgrado(Usuario usuario){
		ecnfUsuario = usuario;
		ecnfActivarReporte = 0;
		String retorno = null;
			try {
				ecnfTipoUsuario = GeneralesConstantes.APP_ID_BASE;
				List<UsuarioRol> usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
				for (UsuarioRol item : usro) {
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
						ecnfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
						ecnfUsuarioDescripcion="Secretaría ";
					}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
						ecnfTipoUsuario = RolConstantes.ROL_ADMINDPP_VALUE.intValue();
						ecnfUsuarioDescripcion="Director de Posgrado ";
					}
				}
				
			} catch (UsuarioRolNoEncontradoException | UsuarioRolException e) {
			}
		if(ecnfTipoUsuario == GeneralesConstantes.APP_ID_BASE){
			FacesUtil.mensajeError("Usuario de posgrado no encontrado o inactivo.");
			return null;
		}
		ecnfListDependenciaBusq = new ArrayList<>();
		ecnfTipoCarrera = "programas";

		// INICIO PARAMETROS DE BUSQUEDA
		iniciarParametros();
		// LISTO LAS FACULTADES
		try {
			if(ecnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				ecnfListDependenciaBusq = servDependenciaServicio.listarFacultadesxUsuario(ecnfUsuario.getUsrId());
			}else{
				ecnfListDependenciaBusq = servDependenciaServicio.listarFacultadesActivasPosgrado(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			}
			return "irListarEstudianteCertificadoNotasPosgrado";
		}catch (Exception e) {
		}
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeError("Error al listar los programas, intente más tarde.");
		return retorno;
	}
	
	public void llenarCohortes(){
		try {
			List<Carrera> listaProgramas = new ArrayList<Carrera>();
			if(ecnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				listaProgramas = servRolFlujoCarreraServicio.buscarCarrerasXUsuarioPosgrado(ecnfUsuario.getUsrId());
				ecnfListPeriodoAcademicoBusq = servEcnfPeriodoAcademicoDtoServicioJdbc.listarTodosXCarrera(listaProgramas);	
			}else{
				listaProgramas = servEcnfCarreraServicio.listarTodosPosgrado();
				ecnfListPeriodoAcademicoBusq = servEcnfPeriodoAcademicoDtoServicioJdbc.listarTodosPosgradoPorDependencia(ecnfEstudianteBuscar.getDpnId());
			}
		} catch (PeriodoAcademicoDtoJdbcException e) {
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
		} catch (CarreraNoEncontradoException e) {
		} catch (CarreraException e) {
		}catch (Exception e) {
		}
		
	}
	
	/**
	 * Setea a la entidad y a la lista de entidades a null y redirige a la pagina de inicio
	 * @return  Navegacion a la pagina de inicio.
	 */
	public String irInicio(){
		ecnfEstudianteBuscar = new EstudianteJdbcDto();
		ecnfListCarreraBusq = null;
		ecnfListEstudianteBusq = null;
		ecnflistEstudianteReporteBusq = null;
		ecnfListNivelBusq = null;
		return "irInicio";
	}
	
	public void nada(EstudianteJdbcDto estudiante){
		
			ecnflistEstudianteReporteBusq=null;
			//ecnflistEstudianteReporteBusq = servEcnfEstudianteDtoServicioJdbc.buscarEstudianteXIdentificacionXNotas(estudiante.getPrsIdentificacion());
			
			try {
				ecnflistEstudianteReporteBusq=servEcnfEstudianteDtoServicioJdbc.buscarEstudianteNotasXIdentificacionXMatriculaXPeriodoXCarrera(estudiante.getPrsIdentificacion(), estudiante.getFcmtId(), estudiante.getPracId(), estudiante.getCrrId());
				
				List<EstudianteJdbcDto> listaModulosAgregar = new ArrayList<EstudianteJdbcDto>(); 
				for (EstudianteJdbcDto item : ecnflistEstudianteReporteBusq) {
					List<Materia> listaMateriasModulos = null;
					try {
						listaMateriasModulos = new ArrayList<Materia>();
						listaMateriasModulos = servEcnfMateria.listarTodosModulos(item.getMtrId());
						int i = 1;
						BigDecimal sumatoriaNotas = BigDecimal.ZERO;
						BigDecimal sumatoriaAsistencia = BigDecimal.ZERO;
						BigDecimal sumatoriaAsistenciaDocente = BigDecimal.ZERO;
						for (Materia materia : listaMateriasModulos) {
//							System.out.println(i);
							try {
								MallaCurricularMateria mlcrmtAux = servMallaCurricularMateriaServicio.buscarPorMtrIdPorEstado(materia.getMtrId(), MallaCurricularMateriaConstantes.ESTADO_MALLA_MATERIA_ACTIVO_VALUE);
								MallaCurricularParalelo mlcrprAux = servMallaCurricularParaleloServicio.buscarPorMlcrmtPorPrlId(mlcrmtAux.getMlcrmtId(), item.getPrlId());
								List<EstudianteJdbcDto> listaModulos = servEcnfMateriaDto.listarNotasXmallaCurricularParalelo(mlcrprAux.getMlcrprId(),estudiante.getPrsId());
								listaModulos.get(0).setMtrId(materia.getMtrId());
								listaModulos.get(0).setMtrDescripcion(listaModulos.get(0).getMtrDescripcion()+" - "+materia.getMtrDescripcion());
								listaModulosAgregar.addAll(listaModulos);
								sumatoriaNotas = sumatoriaNotas.add((listaModulos.get(0).getClfNota1()).setScale(2, RoundingMode.CEILING));
								sumatoriaAsistencia = sumatoriaAsistencia.add(new BigDecimal(listaModulos.get(0).getClfAsistenciaEstudiante1()).setScale(2, RoundingMode.HALF_UP));
								sumatoriaAsistenciaDocente = sumatoriaAsistenciaDocente.add(new BigDecimal(listaModulos.get(0).getClfAsistenciaDocente1()).setScale(0, RoundingMode.CEILING));
								i++;
							} catch (Exception e) {
								e.printStackTrace();
								List<EstudianteJdbcDto> listaModulos = new ArrayList<EstudianteJdbcDto>();
								listaModulos.add(new EstudianteJdbcDto());
								listaModulos.get(0).setMtrId(materia.getMtrId());
								listaModulos.get(0).setMtrDescripcion(item.getMtrDescripcion()+" - "+materia.getMtrDescripcion());
								listaModulos.get(0).setPrlDescripcion(item.getPrlDescripcion());
								listaModulos.get(0).setDtmtNumero(item.getDtmtNumero());
								listaModulos.get(0).setRcesEstado(item.getRcesEstado());
								listaModulosAgregar.addAll(listaModulos);
							}
							if(i==listaMateriasModulos.size()+1){
								item.setClfAsistenciaEstudiante1((sumatoriaAsistencia.divide(new BigDecimal(i-1),0,RoundingMode.CEILING)).setScale(2, RoundingMode.HALF_UP).intValue());
								item.setClfNota1(sumatoriaNotas.divide(new BigDecimal(i-1),2,RoundingMode.CEILING));
								item.setClfAsistenciaDocente1((sumatoriaAsistenciaDocente.divide(new BigDecimal(i-1),0,RoundingMode.CEILING)).setScale(0, RoundingMode.CEILING).intValue());
//								System.out.println(item.getRcesId());
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				if(listaModulosAgregar.size()!=0){
					ecnflistEstudianteReporteBusq.addAll(listaModulosAgregar);
				}
				

				
				ecnfCarreraBuscar = servEcnfCarreraServicio.buscarPorId(estudiante.getCrrId());

				if(ecnfEstudianteBuscar.getNvlId() != NivelConstantes.NIVEL_NIVELACION_VALUE){
					ecnfSecretarioBuscar = servEcnfPersonaServicio.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE, ecnfCarreraBuscar.getCrrDependencia().getDpnId());
				}

				ReporteCertificadoNotasForm.generarReporteCertificadoNotas(ecnflistEstudianteReporteBusq, ecnfUsuario.getUsrNick(), ecnfCarreraBuscar, ecnfSecretarioBuscar);
				ecnfActivarReporte = 1;
			} catch (EstudianteDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (EstudianteDtoJdbcNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.notas.validacion.no.encontrado.exception")));
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PersonaDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} 
	}
	
	
	public void generarNotasPosgrado(EstudianteJdbcDto estudiante){
		
		ecnflistEstudianteReporteBusq=null;
		//ecnflistEstudianteReporteBusq = servEcnfEstudianteDtoServicioJdbc.buscarEstudianteXIdentificacionXNotas(estudiante.getPrsIdentificacion());
		
		try {
			ecnflistEstudianteReporteBusq=servEcnfEstudianteDtoServicioJdbc.buscarEstudianteNotasXIdentificacionXMatriculaXPeriodoXCarreraPosgrado(estudiante.getPrsIdentificacion(), estudiante.getFcmtId(), estudiante.getPracId(), estudiante.getCrrId());
			
			

			
			ecnfCarreraBuscar = servEcnfCarreraServicio.buscarPorId(estudiante.getCrrId());

			if(ecnfEstudianteBuscar.getNvlId() != NivelConstantes.NIVEL_NIVELACION_VALUE){
				ecnfSecretarioBuscar = servEcnfPersonaServicio.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE, ecnfCarreraBuscar.getCrrDependencia().getDpnId());
			}

			ReporteCertificadoNotasForm.generarReporteCertificadoNotasPosgrado(ecnflistEstudianteReporteBusq, ecnfUsuario.getUsrNick(), ecnfCarreraBuscar, ecnfSecretarioBuscar);
			ecnfActivarReporte = 1;
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.notas.validacion.no.encontrado.exception")));
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} 
}
	
	public void generarReporte() {
	}
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
		ecnfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
		ecnfListEstudianteBusq = null;
		ecnflistEstudianteReporteBusq = null;
		ecnfActivacion = "true";
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
			
			if(ecnfEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.periodo.validacion.exception")));
			}else if(ecnfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.carrera.validacion.exception")));
			}else if(ecnfEstudianteBuscar.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.nivel.validacion.exception")));
			}else{
//				ecnfActivacion = "false";
				//BUSCO LA LISTA DE ESTUDIANTES POR PERIODO, CARRERA, NIVEL, PARALELO, MATERIA, APELLIDO PATERNO , IDENTIFICACIÓN
//				ecnfListEstudianteBusq = servEcnfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodocarreraNivel(ecnfEstudianteBuscar.getPracId(), ecnfEstudianteBuscar.getCrrId(),ecnfEstudianteBuscar.getNvlId(),
//						ecnfEstudianteBuscar.getPrlId(),ecnfEstudianteBuscar.getPrsIdentificacion(),ecnfEstudianteBuscar.getPrsPrimerApellido());
				
				ecnfListEstudianteBusq = servEcnfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXCarreraXNivelXIdentificacionXApellidoPagados(ecnfEstudianteBuscar.getPracId(), ecnfEstudianteBuscar.getCrrId(),ecnfEstudianteBuscar.getNvlId()
						,ecnfEstudianteBuscar.getPrsIdentificacion(),ecnfEstudianteBuscar.getPrsPrimerApellido());
				
				
				
				
				
				//HABILITO BOTON IMPRIMIR
				if(ecnfListEstudianteBusq.size() > 0 ){ //si existe por lo menos uno se activa
					ecnfActivacion = "false";
				}
				
			}
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.exception")));
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.buscar.estudiante.no.encontrado.exception")));
		}
	}
	
	public void buscarPosgrado(){
		//anulo la lista de estudiantes
		ecnfListEstudianteBusq = null;
		ecnflistEstudianteReporteBusq = null;
		ecnfActivacion = "true";
		try {
			
			if(ecnfEstudianteBuscar.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.periodo.validacion.exception")));
			}else if(ecnfEstudianteBuscar.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Estudiante.secretaria.buscar.carrera.validacion.exception")));
			}else{
				
				ecnfListEstudianteBusq = servEcnfEstudianteDtoServicioJdbc.buscarEstudianteXPeriodoXCarreraXIdentificacionXApellidoPosgrado(ecnfEstudianteBuscar.getPracId(), ecnfEstudianteBuscar.getCrrId()
						,ecnfEstudianteBuscar.getPrsIdentificacion(),ecnfEstudianteBuscar.getPrsPrimerApellido());
				
				
				
				
				
				//HABILITO BOTON IMPRIMIR
				if(ecnfListEstudianteBusq.size() > 0 ){ //si existe por lo menos uno se activa
					ecnfActivacion = "false";
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
			ecnfEstudianteVer = servEcnfEstudianteDtoServicioJdbc.buscarEstudianteXId(estudiante.getPrsId());
			if(ecnfEstudianteVer != null){
				ecnfCantonResidenciaVer = servEcnfUbicacionDtoServicioJdbc.buscarXId(ecnfEstudianteVer.getUbcCantonId());
				UbicacionDto ubicacionAux =  null;
				try {
					ubicacionAux =  servEcnfUbicacionDtoServicioJdbc.buscarPadreXId(ecnfEstudianteVer.getUbcCantonId());
				} catch (UbicacionDtoJdbcNoEncontradoException e) {
					ubicacionAux =  null;
				}
				//verfifica si es pais o canton
				if(ubicacionAux == null){//No es ecuador
					ecnfPaisResidenciaVer = ecnfCantonResidenciaVer;
					ecnfProvinciaResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					ecnfProvinciaResidenciaVer.setUbcDescripcion("N/A");
					ecnfCantonResidenciaVer = new UbicacionDto(GeneralesConstantes.APP_ID_BASE);
					ecnfCantonResidenciaVer.setUbcDescripcion("N/A");
				}else{
					ecnfProvinciaResidenciaVer = servEcnfUbicacionDtoServicioJdbc.buscarPadreXId(ecnfCantonResidenciaVer.getUbcId());
					ecnfPaisResidenciaVer = servEcnfUbicacionDtoServicioJdbc.buscarPadreXId(ecnfProvinciaResidenciaVer.getUbcId());
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
		idPeriodo = ecnfEstudianteBuscar.getPracId();
		ecnfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		ecnfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		ecnfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		ecnfListCarreraBusq = null;
		ecnfListNivelBusq = null;
		ecnfListParaleloBusq = null;
		ecnfListEstudianteBusq = null;
		ecnflistEstudianteReporteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		try {
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE){
				//LISTO LAS CARRERAS
				if(ecnfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ecnfListCarreraBusq = servEcnfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(ecnfUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (ecnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ecnfListCarreraBusq = servEcnfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(ecnfUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, idPeriodo);
				}else if (ecnfTipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
					ecnfListCarreraBusq = servEcnfCarreraDtoServicioJdbc.buscarXPracIdPosgrado(idPeriodo);
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
		idCarrera = ecnfEstudianteBuscar.getCrrId();
		ecnfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		ecnfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		ecnfListNivelBusq = null;
		ecnfListParaleloBusq = null;
		ecnfListEstudianteBusq = null;
		ecnflistEstudianteReporteBusq = null;
		
//		efNivelDtoBusq = new NivelDto();
//		efNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
//		efListNivelBusq = null;
		
		try {
			if(idCarrera != GeneralesConstantes.APP_ID_BASE){
				//LISTO LOS NIVELES
				if(ecnfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
					ecnfListNivelBusq = servEcnfNivelDtoServicioJdbc.listarNivelXCarrera(idCarrera);
				}else if (ecnfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					ecnfListNivelBusq = servEcnfNivelDtoServicioJdbc.listarNivelXCarreraPosgrado(idCarrera);
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
		idPeriodo = ecnfEstudianteBuscar.getPracId();
		idCarrera = ecnfEstudianteBuscar.getCrrId();
		idNivel = ecnfEstudianteBuscar.getNvlId();
		ecnfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		ecnfListParaleloBusq = null;
		ecnfListEstudianteBusq = null;
		ecnflistEstudianteReporteBusq = null;
		
//		efParaleloDtoBusq = new ParaleloDto();
//		efParaleloDtoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
//		efListParaleloBusq = null;
		
		try{
			if(idPeriodo != GeneralesConstantes.APP_ID_BASE && idCarrera != GeneralesConstantes.APP_ID_BASE && idNivel != GeneralesConstantes.APP_ID_BASE){
				//Lista de paralelos
				ecnfListParaleloBusq = servEcnfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(idPeriodo, idCarrera, idNivel);
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
		ecnfListEstudianteBusq = null;
		//ANULO LA LISTA DE ESTUDIANTE PARA EL REPORTE
		ecnflistEstudianteReporteBusq = null;
		//ANULO LA LISTA DE NIVEL
		ecnfListNivelBusq = null;
		//ANULO LA LISTA DE CARRERAS
		ecnfListCarreraBusq = null;
		//ANULO LA LISTA DE PARALELOS
		ecnfListParaleloBusq = null;
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		ecnfEstudianteBuscar = new EstudianteJdbcDto();
		//INICIALIZO EL PERIODO ACADEMICO
		ecnfEstudianteBuscar.setPracId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA CARRERA ID
		ecnfEstudianteBuscar.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL NIVEL
		ecnfEstudianteBuscar.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL PARALELO
		ecnfEstudianteBuscar.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO EL DTO DE NIVEL BUSCAR
		ecnfNivelDtoBusq = new NivelDto();
		//INICIALIZO EL NIVEL ID
		ecnfNivelDtoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//INICIALIZO LA IDENTIFICACION
		ecnfEstudianteBuscar.setPrsIdentificacion("");
		//INICIALIZO EL APELLIDO PATERNO
		ecnfEstudianteBuscar.setPrsPrimerApellido("");
		
		ecnfSecretarioBuscar = new PersonaDto();
		ecnfActivacion = "true";
	}

	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/
	public Usuario getEcnfUsuario() {
		return ecnfUsuario;
	}

	public void setEcnfUsuario(Usuario ecnfUsuario) {
		this.ecnfUsuario = ecnfUsuario;
	}

	public EstudianteJdbcDto getEcnfEstudianteBuscar() {
		return ecnfEstudianteBuscar;
	}

	public void setEcnfEstudianteBuscar(EstudianteJdbcDto ecnfEstudianteBuscar) {
		this.ecnfEstudianteBuscar = ecnfEstudianteBuscar;
	}

	public List<CarreraDto> getEcnfListCarreraBusq() {
		return ecnfListCarreraBusq;
	}

	public void setEcnfListCarreraBusq(List<CarreraDto> ecnfListCarreraBusq) {
		this.ecnfListCarreraBusq = ecnfListCarreraBusq;
	}

	public List<EstudianteJdbcDto> getEcnfListEstudianteBusq() {
		return ecnfListEstudianteBusq;
	}

	public void setEcnfListEstudianteBusq(List<EstudianteJdbcDto> ecnfListEstudianteBusq) {
		this.ecnfListEstudianteBusq = ecnfListEstudianteBusq;
	}

	public List<EstudianteJdbcDto> getEcnflistEstudianteReporteBusq() {
		return ecnflistEstudianteReporteBusq;
	}

	public void setEcnflistEstudianteReporteBusq(List<EstudianteJdbcDto> ecnflistEstudianteReporteBusq) {
		this.ecnflistEstudianteReporteBusq = ecnflistEstudianteReporteBusq;
	}

	public List<NivelDto> getEcnfListNivelBusq() {
		return ecnfListNivelBusq;
	}

	public void setEcnfListNivelBusq(List<NivelDto> ecnfListNivelBusq) {
		this.ecnfListNivelBusq = ecnfListNivelBusq;
	}

	public List<PeriodoAcademicoDto> getEcnfListPeriodoAcademicoBusq() {
		return ecnfListPeriodoAcademicoBusq;
	}

	public void setEcnfListPeriodoAcademicoBusq(List<PeriodoAcademicoDto> ecnfListPeriodoAcademicoBusq) {
		this.ecnfListPeriodoAcademicoBusq = ecnfListPeriodoAcademicoBusq;
	}

	public List<ParaleloDto> getEcnfListParaleloBusq() {
		return ecnfListParaleloBusq;
	}

	public void setEcnfListParaleloBusq(List<ParaleloDto> ecnfListParaleloBusq) {
		this.ecnfListParaleloBusq = ecnfListParaleloBusq;
	}

	public NivelDto getEcnfNivelDtoBusq() {
		return ecnfNivelDtoBusq;
	}

	public void setEcnfNivelDtoBusq(NivelDto ecnfNivelDtoBusq) {
		this.ecnfNivelDtoBusq = ecnfNivelDtoBusq;
	}

	public ParaleloDto getEcnfParaleloDtoBusq() {
		return ecnfParaleloDtoBusq;
	}

	public void setEcnfParaleloDtoBusq(ParaleloDto ecnfParaleloDtoBusq) {
		this.ecnfParaleloDtoBusq = ecnfParaleloDtoBusq;
	}

	public String getEcnfTipoCarrera() {
		return ecnfTipoCarrera;
	}

	public void setEcnfTipoCarrera(String ecnfTipoCarrera) {
		this.ecnfTipoCarrera = ecnfTipoCarrera;
	}

	public Integer getEcnfTipoUsuario() {
		return ecnfTipoUsuario;
	}

	public void setEcnfTipoUsuario(Integer ecnfTipoUsuario) {
		this.ecnfTipoUsuario = ecnfTipoUsuario;
	}

	public EstudianteJdbcDto getEcnfEstudianteVer() {
		return ecnfEstudianteVer;
	}

	public void setEcnfEstudianteVer(EstudianteJdbcDto ecnfEstudianteVer) {
		this.ecnfEstudianteVer = ecnfEstudianteVer;
	}

	public UbicacionDto getEcnfCantonResidenciaVer() {
		return ecnfCantonResidenciaVer;
	}

	public void setEcnfCantonResidenciaVer(UbicacionDto ecnfCantonResidenciaVer) {
		this.ecnfCantonResidenciaVer = ecnfCantonResidenciaVer;
	}

	public UbicacionDto getEcnfPaisResidenciaVer() {
		return ecnfPaisResidenciaVer;
	}

	public void setEcnfPaisResidenciaVer(UbicacionDto ecnfPaisResidenciaVer) {
		this.ecnfPaisResidenciaVer = ecnfPaisResidenciaVer;
	}

	public UbicacionDto getEcnfProvinciaResidenciaVer() {
		return ecnfProvinciaResidenciaVer;
	}

	public void setEcnfProvinciaResidenciaVer(UbicacionDto ecnfProvinciaResidenciaVer) {
		this.ecnfProvinciaResidenciaVer = ecnfProvinciaResidenciaVer;
	}

	public String getEcnfActivacion() {
		return ecnfActivacion;
	}

	public void setEcnfActivacion(String ecnfActivacion) {
		this.ecnfActivacion = ecnfActivacion;
	}

	public int getEcnfActivarReporte() {
		return ecnfActivarReporte;
	}

	public void setEcnfActivarReporte(int ecnfActivarReporte) {
		this.ecnfActivarReporte = ecnfActivarReporte;
	}

	public Carrera getEcnfCarreraBuscar() {
		return ecnfCarreraBuscar;
	}

	public void setEcnfCarreraBuscar(Carrera ecnfCarreraBuscar) {
		this.ecnfCarreraBuscar = ecnfCarreraBuscar;
	}

	public PersonaDto getEcnfSecretarioBuscar() {
		return ecnfSecretarioBuscar;
	}

	public void setEcnfSecretarioBuscar(PersonaDto ecnfSecretarioBuscar) {
		this.ecnfSecretarioBuscar = ecnfSecretarioBuscar;
	}

	public String getEcnfUsuarioDescripcion() {
		return ecnfUsuarioDescripcion;
	}

	public void setEcnfUsuarioDescripcion(String ecnfUsuarioDescripcion) {
		this.ecnfUsuarioDescripcion = ecnfUsuarioDescripcion;
	}

	public List<Dependencia> getEcnfListDependenciaBusq() {
		return ecnfListDependenciaBusq;
	}

	public void setEcnfListDependenciaBusq(List<Dependencia> ecnfListDependenciaBusq) {
		this.ecnfListDependenciaBusq = ecnfListDependenciaBusq;
	}
	
	
	
}
