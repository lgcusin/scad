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
   
 ARCHIVO:     AdministracionMallaCurricular.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración de malla curricular .
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
06-04-2017			Gabriel Mafla						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionAcademico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularNivelPosgradoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoFormacionMallaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularNivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionMallaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CorequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularNivelServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PrerequisitoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaCurricularConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoFormacionMallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularNivel;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacionMalla;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteMallaCurricularForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionMallaCurricular.
 * Managed Bean que maneja las peticiones para la administración de la tabla MallaCurricular.
 * @author ghmafla, fgguzman.
 * @version 1.0, 2.0
 */
@ManagedBean(name="administracionMallaCurricular")
@SessionScoped
public class AdministracionMallaCurricular implements Serializable{
	private static final long serialVersionUID = -1645145272257648479L;
	
	//********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	private Integer amcfDependenciaId;
	private Integer amcfCarreraId;
	private Integer amcfActivarReporte;
	private String amcfNombreArchivo;
	private String amcfNombreReporte;
	private String amcfTipoArchivo;
	private Boolean amcfAgregarMalla;
	private Integer amcfTipoUsuario;
	
	private Usuario amcfUsuario;
	private MallaCurricularDto amcfMallaCurricularDto;
	private PeriodoAcademicoDto amcfPeriodoAcademicoDto;

	
	private List<Dependencia> amcfListDependencia;
	private List<CarreraDto> amcfListCarreraDto;
	private List<MallaCurricularDto> amcfListMallaCurricularDto;
	private List<MallaCurricularNivelDto> amcfListMallaCurricularNivelDto;
	private List<MallaCurricularNivelPosgradoDto> amcfListMallaCurricularNivelPosgradoDto;
	private List<TipoFormacionMalla> amcfListTipoFormacionMalla;
	private List<PeriodoAcademico> amcfListPeriodoAcademico;


	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	
	@EJB	private UsuarioRolServicio servUsuarioRol;
	@EJB	private DependenciaServicio servDependencia;
	@EJB	private TipoFormacionMallaServicio servTipoFormacionMalla;
	@EJB	private MallaCurricularServicio servMallaCurricular;
	@EJB	private MallaCurricularNivelServicio servMallaCurricularNivel;
	@EJB	private PeriodoAcademicoServicio servPeriodoAcademico;
	
	@EJB	private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB	private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB	private MallaCurricularDtoServicioJdbc servJdbcMallaCurricularDto;
	@EJB	private PrerequisitoDtoServicioJdbc servJdbcPrerequisitoDto;
	@EJB	private CorequisitoDtoServicioJdbc servJdbcCorequisitoDto;
	@EJB	private MallaCurricularNivelServicioJdbc servJdbcMallaCurricularNivel;
	
	
	// ********************************************************************/
	// ************************** NAVEGACION ******************************/
	// ********************************************************************/
	
	public String irMallaCurricular(Usuario usuario){
		
		try {
			
			amcfUsuario = usuario;
			amcfTipoUsuario = 0;
			List<UsuarioRol> usroAux = servUsuarioRol.buscarXUsuario(usuario.getUsrId());
			for (UsuarioRol item : usroAux) {
				if(item.getUsroRol().getRolId()==RolConstantes.ROL_ADMINDPP_VALUE){
					amcfTipoUsuario=RolConstantes.ROL_ADMINDPP_VALUE;
				}
			}
			amcfPeriodoAcademicoDto = cargarPeriodoAcademicoActivo();
			amcfListDependencia = servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
			if(amcfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
				amcfListDependencia = servDependencia.listarFacultadesActivasPosgrado(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				amcfListPeriodoAcademico = servPeriodoAcademico.listarTodosPosgradoDesc();
			}
			iniciarFormListarMallaCurricular();
			
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("malla.curricular.irMallaCurricular.Dependencia.No.Encontrado.Exception")));
			return  null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("malla.curricular.irMallaCurricular.UsuarioRol.exception")));
			return  null;
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("malla.curricular.irMallaCurricular.UsuarioRol.no.encontrado.exception")));
			return  null;
		}
		if(amcfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
			return "irMallaCurricularPosgrado";
		}
		return "irMallaCurricular";
	}
	
	
	public String irInicio(){
		vaciarFormListarMallaCurricular();
		return "irInicio";
	}
	
	public String irVerMallas(MallaCurricularDto mallaCurricular){
		amcfMallaCurricularDto = mallaCurricular;
		
		try {
			if(amcfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
				List<MallaCurricularNivelPosgradoDto> mallaPorNiveles= servJdbcMallaCurricularNivel.buscarPosgrado(mallaCurricular.getCrrId(),amcfPeriodoAcademicoDto.getPracId());
				amcfListMallaCurricularNivelPosgradoDto = mallaPorNiveles;
			}else{
				List<MallaCurricularNivelDto> mallaPorNiveles= servJdbcMallaCurricularNivel.buscar(mallaCurricular.getCrrId());	
				if (!mallaPorNiveles.isEmpty()) {
					for (MallaCurricularNivelDto item : mallaPorNiveles) {
						cargarHorasEnTabla(item);
					}
					amcfListMallaCurricularNivelDto = mallaPorNiveles;
				}
			}
		} catch (MallaCurricularNivelException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularNivelNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irVerMallas";
	}


	private void cargarHorasEnTabla(MallaCurricularNivelDto item) {

		try {
			MallaCurricularNivel malla = servMallaCurricularNivel.buscarPorMallaCurricularNivel(item.getMlcrnvMlcrId(), item.getMlcrnvNvlId());
			item.setMlcrnvHorasSql(malla.getMlcrnvCreditos());
			item.setMlcrnvHorasAcumuladoSql(malla.getMlcrnvCreditosAcumulado());
		} catch (MallaCurricularNivelNoEncontradoException e) {
		} catch (MallaCurricularNivelValidacionException e) {
		} catch (MallaCurricularNivelException e) {
		}

	}	

	public String irEditarMalla(){
		iniciarFormEditarMallaCurricular();
		return "irEditarMalla";
	}

	public String irCancelarEditar(){

		return "irMallaCurricular";
	}
	
	public String irCancelarVer(){
		amcfMallaCurricularDto = null;
		amcfListMallaCurricularNivelDto = null;
		amcfActivarReporte = null;
		return "irCancelarVer";
	}
	
	public String irListarMallaCurricular(){
		vaciarFormNuevaMallaCurricular();
		return "irListarMallaCurricular";
	}
	
	

	public String irRegistrarNuevaMalla(){
		try {
			if (verificarCamposObligatorios()) {
				List<MallaCurricularDto> mallaCurricular = cargarMallaCurricularPorCarrera(amcfMallaCurricularDto.getCrrId());
				if (mallaCurricular != null && mallaCurricular.size() >0) {
					FacesUtil.mensajeError("La Carrera seleccionada ya tiene una Malla Curricular activa, intente con otra Carrera.");
					return null;
				}else {
					servMallaCurricular.anadir(amcfMallaCurricularDto);
					amcfListMallaCurricularDto =  cargarMallaCurricularPorCarrera(amcfMallaCurricularDto.getCrrId());	
				}
			}else {
				return null;
			}
			
		} catch (MallaCurricularException e) {
			FacesUtil.mensajeError("Error al intentar registrar la nueva Malla Curricular.");
			return null;
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.mensajeError("Error al intentar registrar la nueva Malla Curricular.");
			return null;
		}
		
		return "irListarMallaCurricular";
		
	}
	

	public String irCrearNuevaMalla(){
		
		try {
			amcfPeriodoAcademicoDto =  servJdbcPeriodoAcademicoDto.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			iniciarFormNuevaMallaCurricular();
			
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró un Período Académico activo.");
			return null;
		}

		return "irCrearNuevo";
	}
	
	public String irRegistrarEdicionMallaCurricular(){
		try {
			if (verificarCamposObligatorios()) {
				servMallaCurricular.editar(amcfMallaCurricularDto);
				amcfListMallaCurricularDto =  cargarMallaCurricularPorCarrera(amcfMallaCurricularDto.getCrrId());
				
				try {
					servMallaCurricularNivel.actualizarCreditosPorCarrera(amcfMallaCurricularDto.getCrrId());
				} catch (MallaCurricularNivelValidacionException e) {
				} catch (MallaCurricularNivelException e) {
				}
				
			}else {
				return null;
			}
			
		} catch (MallaCurricularException e) {
			FacesUtil.mensajeError("Error al intentar actualizar la Malla Curricular.");
			return null;
		} catch (MallaCurricularNoEncontradoException e) {
			FacesUtil.mensajeError("Error al intentar actualizar la Malla Curricular.");
			return null;
		}
		return "irVerMallas";
	}
	// ********************************************************************/
	// **************************** AUXILIARES   **************************/
	// ********************************************************************/
	
	private void iniciarFormListarMallaCurricular(){
		amcfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		amcfCarreraId = GeneralesConstantes.APP_ID_BASE;
		amcfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		amcfTipoArchivo = null;
		amcfNombreArchivo = null;
		amcfNombreReporte = null;
		amcfListCarreraDto = null;
		amcfListMallaCurricularDto = null;
		
	}
	
	private void iniciarFormNuevaMallaCurricular(){
		amcfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		amcfCarreraId = GeneralesConstantes.APP_ID_BASE;
		
		amcfMallaCurricularDto = new MallaCurricularDto();
		amcfListDependencia = cargarDependeciasPregrado();
		amcfListTipoFormacionMalla = cargarTipoFormacionMalla();
		
		amcfMallaCurricularDto.setCrrId(GeneralesConstantes.APP_ID_BASE);
		amcfMallaCurricularDto.setTimtId(GeneralesConstantes.APP_ID_BASE);
		amcfMallaCurricularDto.setMlcrTipoAprobacion(GeneralesConstantes.APP_ID_BASE);
		amcfMallaCurricularDto.setMlcrTipoOrgAprendizaje(GeneralesConstantes.APP_ID_BASE);
		amcfMallaCurricularDto.setMlcrVigencia(GeneralesConstantes.APP_ID_BASE);
		amcfMallaCurricularDto.setPracId(amcfPeriodoAcademicoDto.getPracId());
		
		amcfListCarreraDto = null;
	}
	
	private void iniciarFormEditarMallaCurricular(){
		amcfListTipoFormacionMalla = cargarTipoFormacionMalla();
		amcfMallaCurricularDto.setCrrId(amcfMallaCurricularDto.getCrrId());
		amcfMallaCurricularDto.setTimtId(amcfMallaCurricularDto.getTimtId());
		amcfMallaCurricularDto.setMlcrTipoAprobacion(amcfMallaCurricularDto.getMlcrTipoAprobacion());
		amcfMallaCurricularDto.setMlcrTipoOrgAprendizaje(amcfMallaCurricularDto.getMlcrTipoOrgAprendizaje());
		amcfMallaCurricularDto.setMlcrVigencia(amcfMallaCurricularDto.getMlcrVigencia());
		amcfMallaCurricularDto.setPracId(amcfMallaCurricularDto.getPracId());
	}
	
	private void vaciarFormNuevaMallaCurricular() {
		amcfMallaCurricularDto = null;
		amcfListDependencia = cargarDependeciasPregrado();
		amcfListCarreraDto = null;
		amcfListTipoFormacionMalla = null;
	}
	
	private void vaciarFormListarMallaCurricular(){
		amcfDependenciaId = null;
		amcfCarreraId = null;
		amcfListCarreraDto = null;
		amcfListDependencia = null;
		amcfListMallaCurricularDto = null;
		amcfTipoArchivo = null;
		amcfNombreArchivo = null;
		amcfNombreReporte = null;
		amcfActivarReporte = null;
		amcfAgregarMalla = null;
	}
	
	public void limpiarFormListarMallaCurricular(){
		iniciarFormListarMallaCurricular();
	}
	
	
	
	private List<MallaCurricularDto> cargarMallaCurricularPorCarrera(int crrId) {
		List<MallaCurricularDto> retorno = new ArrayList<>();
		try {
			retorno = servJdbcMallaCurricularDto.listarMallasXCarrera(amcfMallaCurricularDto.getCrrId());
		} catch (MallaCurricularDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró ninguna Malla Curricular, continue con el Ingreso de Datos.");
		}
		return retorno;
	}

	private boolean verificarCamposObligatorios() {
		if (amcfMallaCurricularDto.getCrrId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			return false;
		}
		
		if (GeneralesUtilidades.quitarEspaciosEnBlanco(amcfMallaCurricularDto.getMlcrCodigo()).length() <= 0) {
			FacesUtil.mensajeError("Ingrese el código de malla para continuar.");
			return false;
		}
		
		if (GeneralesUtilidades.quitarEspaciosEnBlanco(amcfMallaCurricularDto.getMlcrDescripcion()).length() <= 0) {
			FacesUtil.mensajeError("Ingrese la descripción de malla para continuar.");
			return false;
		}
		
		if (GeneralesUtilidades.quitarEspaciosEnBlanco(amcfMallaCurricularDto.getMlcrDescripcion()).length() <= 0) {
			FacesUtil.mensajeError("Ingrese la descripción de malla para continuar.");
			return false;
		}
		
		if (amcfMallaCurricularDto.getMlcrTotalMaterias() == null || amcfMallaCurricularDto.getMlcrTotalMaterias() < 1) {
			FacesUtil.mensajeError("Ingrese el número Total de Asignaturas que conforman la Malla Curricular para continuar.");
			return false;
		}
		
		if (amcfMallaCurricularDto.getMlcrTotalCreditos() == null || amcfMallaCurricularDto.getMlcrTotalCreditos() < 1) {
			FacesUtil.mensajeError("Ingrese el número Total de Créditos que conforman la Malla Curricular para continuar.");
			return false;
		}
		
		if (amcfMallaCurricularDto.getTpfrmlId() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError("Seleccione una opción en Tipo de Malla para continuar.");
			return false;
		}
		
		if (amcfMallaCurricularDto.getMlcrTipoAprobacion().intValue() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError("Seleccione una opción en Tipo de Aprobación para continuar.");
			return false;
		}
		
		if (amcfMallaCurricularDto.getMlcrTipoOrgAprendizaje().intValue() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError("Seleccione una opción en Organización de Aprendizaje para continuar.");
			return false;
		}
		
		if (amcfMallaCurricularDto.getMlcrVigencia().intValue() == GeneralesConstantes.APP_ID_BASE) {
			FacesUtil.mensajeError("Seleccione una opción en Vigencia para continuar.");
			return false;
		}
		
		return true;
	}

	public void buscarMallaCurricularPorCarrera() {

		if (!amcfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (amcfMallaCurricularDto.getCrrId() != GeneralesConstantes.APP_ID_BASE) {
				List<MallaCurricularDto> mallaCurricular = cargarMallaCurricularPorCarrera(amcfMallaCurricularDto.getCrrId());
				if (mallaCurricular != null && mallaCurricular.size() >0) {
					FacesUtil.mensajeError("La Carrera seleccionada ya tiene una Malla Curricular activa, intente con otra Carrera.");
				}
			}
		}

	}
	
	
	public void buscarCarreras() {
		
		if (!amcfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			try {
				amcfListCarreraDto = null;
				amcfListMallaCurricularDto = null;
				amcfCarreraId = GeneralesConstantes.APP_ID_BASE;
				amcfListCarreraDto = servJdbcCarreraDto.buscarCarrerasPorTipoFacultad(amcfDependenciaId, CarreraConstantes.TIPO_PREGRADO_VALUE);
			} catch (CarreraDtoJdbcException e) {
			} catch (CarreraDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeInfo("No se encontró carreras vinculadas a la Dependencia seleccionada.");
			}
		}else {
			limpiarFormListarMallaCurricular();
			FacesUtil.mensajeInfo("Seleccione una Facultad para continuar con la búsqueda.");
		}
		
	}
	
	
	public void buscarProgramas() {
		if(amcfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
			amcfListMallaCurricularDto = servJdbcMallaCurricularDto.buscarCarreraDependenciaPorPeriodoAcademico(amcfPeriodoAcademicoDto.getPracId());
			amcfDependenciaId=amcfListMallaCurricularDto.get(0).getDpnId();
			amcfCarreraId=amcfListMallaCurricularDto.get(0).getCrrId();
		}
//		System.out.println(amcfCarreraId);
//		System.out.println(amcfDependenciaId);
//		if (!amcfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
//			try {
//				amcfListCarreraDto = null;
//				amcfListMallaCurricularDto = null;
//				amcfCarreraId = GeneralesConstantes.APP_ID_BASE;
//				amcfListCarreraDto = servJdbcCarreraDto.buscarCarrerasPorTipoFacultad(amcfDependenciaId, CarreraConstantes.TIPO_POSGRADO_VALUE);
//			} catch (CarreraDtoJdbcException e) {
//			} catch (CarreraDtoJdbcNoEncontradoException e) {
//				FacesUtil.mensajeInfo("No se encontró programas de posgrado vinculados a la dependencia seleccionada.");
//			}
//		}else {
//			limpiarFormListarMallaCurricular();
//			FacesUtil.mensajeInfo("Seleccione una Facultad para continuar con la búsqueda.");
//		}
		
	}
	
	public void buscarMallas(){
		
//		TODO: ACTIVAR PRIMERA CARGA O SOLO PARA ACTUALIZACION
//		cargarMallaCurricularPorNivelGeneral();
		
		if (!amcfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			try {
				if(amcfTipoUsuario==RolConstantes.ROL_ADMINDPP_VALUE){
					amcfListMallaCurricularDto =  servJdbcMallaCurricularDto.buscarMallaCurricularPosgrado(amcfDependenciaId, amcfCarreraId, CarreraConstantes.TIPO_POSGRADO_VALUE, MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE);
					amcfAgregarMalla=false;
				}else{
					amcfListMallaCurricularDto =  servJdbcMallaCurricularDto.buscarMallaCurricular(amcfDependenciaId, amcfCarreraId, new Integer[]{CarreraConstantes.TIPO_PREGRADO_VALUE}, new Integer[]{MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE});
					amcfAgregarMalla = Boolean.TRUE;
				}
				
			} catch (MallaCurricularDtoException e) {
				FacesUtil.mensajeInfo(e.getMessage());
			} catch (MallaCurricularDtoNoEncontradoException e) {
				amcfAgregarMalla = Boolean.TRUE;
				iniciarFormListarMallaCurricular();
				FacesUtil.mensajeInfo("No se encontró Mallas vinculadas a la Carrera seleccionada.");
			}
			
		}else {
			iniciarFormListarMallaCurricular();
			FacesUtil.mensajeInfo("Seleccione una Facultad para continuar con la búsqueda.");
		}
		
	}
	
//	private void cargarMallaCurricularPorNivelGeneral() {
//
//		try {
//			servMallaCurricularNivel.registrarCreditosPorNivel();
//		} catch (MallaCurricularNivelValidacionException e) {
//		} catch (MallaCurricularNivelException e) {
//		}
//		
//	}

	public void imprimirMallaCurricular(MallaCurricularDto malla){
		CarreraDto carrera = new CarreraDto();
		carrera.setCrrDescripcion(malla.getCrrDescripcion());
		carrera.setDpnDescripcion(malla.getDpnDescripcion());
		malla.setPracDescripcion(amcfPeriodoAcademicoDto.getPracDescripcion());
		
		List<MateriaDto> materias = cargarMallaCurricularPorMaterias(malla.getMlcrId());
		if (!materias.isEmpty()) {
			amcfTipoArchivo = "PDF";
			if (malla.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
				amcfNombreArchivo = "mallaCurricularPorMateriasRediseno";
			}else {
				amcfNombreArchivo = "mallaCurricularPorMaterias";
			}
			amcfNombreReporte = "MALLA_CURRICULAR_MATERIAS";
			for (MateriaDto item : materias) {
				item.setMtrPrerequisito(cargarPrerequisitosPorMateria(item.getMtrId()));
				item.setMtrCorrequisito(cargarCorrequisitosPorMateria(item.getMtrId()));
			}
			
			ReporteMallaCurricularForm.generarReporteCertificadoNotas(amcfUsuario, carrera, malla, materias);
			amcfActivarReporte = 1;
		}else {
			amcfActivarReporte = null;
			amcfTipoArchivo = null;
			amcfNombreArchivo = null;
			amcfNombreReporte = null;
		}
		
	}
	
	
	private String cargarCorrequisitosPorMateria(int mtrId) {
		String retorno = "";
		try {
			List<MateriaDto> correquisitos = servJdbcCorequisitoDto.listarXidMateria(mtrId);
			if (correquisitos != null && correquisitos.size() >0) {
				for (MateriaDto item : correquisitos) {
					retorno = retorno + item.getMtrCodigo() + " ";
				}
			}
		} catch (MateriaDtoException e) {
		} catch (MateriaDtoNoEncontradoException e) {
		}
		return retorno;
	}

	private String cargarPrerequisitosPorMateria(int mtrId) {
		String retorno = "";
			try {
				List<MateriaDto> prerequisitos = servJdbcPrerequisitoDto.listarXidMateriaRequisitos(mtrId);
				if (prerequisitos != null && prerequisitos.size() >0) {
					for (MateriaDto item : prerequisitos) {
						retorno = retorno + item.getMtrCodigo() + " ";	
					}
				}
			} catch (MateriaDtoException e) {
			} catch (MateriaDtoNoEncontradoException e) {
			}
		return retorno;
	}

	
	
	

	public String establecerEstadoMallaCurricular(int param){
		String retorno = "";
		switch (param) {
		case MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_VALUE:
			retorno = MallaCurricularConstantes.ESTADO_MALLA_ACTIVO_LABEL;
			break;
		case MallaCurricularConstantes.ESTADO_MALLA_INACTIVO_VALUE:
			retorno = MallaCurricularConstantes.ESTADO_MALLA_INACTIVO_LABEL;
			break;
		}
		return retorno;
	}
	
	
	
	/**
	 * Retorna el label del valor representativo del vigencia de la malla curricular
	 * @param param el valor de la constantes LABEL del estado de la malla curricular
	 * @return el valor de la constantes de vigencia malla curricular: 0.- SI 1.-NO
	 */
	public String establecerVigenciaMallaCurricular(int param){
		String retorno = "";
		switch (param) {
		case MallaCurricularConstantes.VIGENTE_MALLA_SI_VALUE:
			retorno = MallaCurricularConstantes.VIGENTE_MALLA_SI_LABEL;
			break;
		case MallaCurricularConstantes.VIGENTE_MALLA_NO_VALUE:
			retorno = MallaCurricularConstantes.VIGENTE_MALLA_NO_LABEL;
			break;
		}
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo del COD APRENDIZAJE de la malla curricular
	 * @param vigenciaMallaCurricular el valor de la constantes LABEL del COD APRENDIZAJE de la malla curricular
	 * @return el valor de la constantes de COD APRENDIZAJE malla curricular: 0.- SI 1.-NO
	 */
	public String establecerOrgAprendizajeMallaCurricular(int param){
		String retorno = "";
		switch (param) {
		case MallaCurricularConstantes.TIPO_ORG_APREN_1_VALUE:
			retorno = MallaCurricularConstantes.TIPO_ORG_APREN_1_LABEL;
			break;
		case MallaCurricularConstantes.TIPO_ORG_APREN_2_VALUE:
			retorno = MallaCurricularConstantes.TIPO_ORG_APREN_2_LABEL;
			break;
		case MallaCurricularConstantes.TIPO_ORG_APREN_3_VALUE:
			retorno = MallaCurricularConstantes.TIPO_ORG_APREN_3_LABEL;
			break;
		}
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo del APROBACION de la malla curricular
	 * @param vigenciaMallaCurricular el valor de la constantes LABEL del APROBACION de la malla curricular
	 * @return el valor de la constantes de APROBACION malla curricular: 0.- SI 1.-NO
	 */
	public String establecerAprobacionMallaCurricular(int param){
		String retorno = "";
		switch (param) {
		case MallaCurricularConstantes.POR_NIVEL_VALUE:
			retorno = MallaCurricularConstantes.POR_NIVEL_LABEL;
			break;
		case MallaCurricularConstantes.POR_MATERIA_VALUE:
			retorno = MallaCurricularConstantes.POR_MATERIA_LABEL;
			break;
		}
		return retorno;
	}

	/**
	 * Retorna el label del valor representativo del APROBACION de la malla curricular
	 * @param vigenciaMallaCurricular el valor de la constantes LABEL del APROBACION de la malla curricular
	 * @return el valor de la constantes de APROBACION malla curricular: 0.- SI 1.-NO
	 */
	public String establecerTipoFormacionMallaCurricular(int param){
		String retorno = "";
		switch (param) {
		case TipoFormacionMallaConstantes.TIFRML_EDUCACION_VALUE:
			retorno = TipoFormacionMallaConstantes.TIFRML_EDUCACION_LABEL;
			break;
		case TipoFormacionMallaConstantes.TIFRML_INGENIERIA_VALUE:
			retorno = TipoFormacionMallaConstantes.TIFRML_INGENIERIA_LABEL;
			break;
		case TipoFormacionMallaConstantes.TIFRML_NIVELACION_VALUE:
			retorno = TipoFormacionMallaConstantes.TIFRML_NIVELACION_LABEL;
			break;
		case TipoFormacionMallaConstantes.TIFRML_POSGRADO_VALUE:
			retorno = TipoFormacionMallaConstantes.TIFRML_POSGRADO_LABEL;
			break;
		case TipoFormacionMallaConstantes.TIFRML_MIGRACION_SAU_VALUE:
			retorno = TipoFormacionMallaConstantes.TIFRML_MIGRACION_SAU_LABEL;
			break;
		case TipoFormacionMallaConstantes.TIFRML_SUFICIENCIA_VALUE:
			retorno = TipoFormacionMallaConstantes.TIFRML_SUFICIENCIA_LABEL;
			break;
		}
		return retorno;
	}
	
	
	/**
	 * Método que permite listar las mallas curriculares segun la carrera seleccionada.
	 */
	private List<Dependencia> cargarDependeciasPregrado(){
		List<Dependencia> retorno = new ArrayList<>();

		try {
			retorno = servDependencia.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	
	/**
	 * Método que permite listar las mallas curriculares segun la carrera seleccionada.
	 */
	private List<MateriaDto> cargarMallaCurricularPorMaterias(int mlcrId){
		List<MateriaDto> retorno = new ArrayList<>();
		
		try {
			retorno =  servJdbcMallaCurricularDto.buscarMaterias(mlcrId);
		} catch (MallaCurricularDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró asignaturas vinculadas a la malla curricular seleccionada.");
		}

		return retorno;
	}

	private PeriodoAcademicoDto cargarPeriodoAcademicoActivo(){
		PeriodoAcademicoDto retorno = null;
		try {
			retorno = servJdbcPeriodoAcademicoDto.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró un Período Académico activo.");
		}
		return retorno;
	}
	
	
	private List<TipoFormacionMalla> cargarTipoFormacionMalla(){
		List<TipoFormacionMalla> retorno = null;
		try {
			retorno = servTipoFormacionMalla.listarTodosActivas();
		} catch (TipoFormacionMallaNoEncontradoException e) {
			FacesUtil.mensajeError("Error al buscar Tipo de Formación Malla.");
		}
		return retorno;
	}
	

	// ********************************************************************/
	// *********************** ENCAPSULAMIENTO   **************************/
	// ********************************************************************/

	public Integer getAmcfDependenciaId() {
		return amcfDependenciaId;
	}


	public void setAmcfDependenciaId(Integer amcfDependenciaId) {
		this.amcfDependenciaId = amcfDependenciaId;
	}


	public Integer getAmcfCarreraId() {
		return amcfCarreraId;
	}


	public void setAmcfCarreraId(Integer amcfCarreraId) {
		this.amcfCarreraId = amcfCarreraId;
	}

	public List<CarreraDto> getAmcfListCarreraDto() {
		return amcfListCarreraDto;
	}


	public void setAmcfListCarreraDto(List<CarreraDto> amcfListCarreraDto) {
		this.amcfListCarreraDto = amcfListCarreraDto;
	}


	public List<MallaCurricularDto> getAmcfListMallaCurricularDto() {
		return amcfListMallaCurricularDto;
	}


	public void setAmcfListMallaCurricularDto(List<MallaCurricularDto> amcfListMallaCurricularDto) {
		this.amcfListMallaCurricularDto = amcfListMallaCurricularDto;
	}


	public List<Dependencia> getAmcfListDependencia() {
		return amcfListDependencia;
	}


	public void setAmcfListDependencia(List<Dependencia> amcfListDependencia) {
		this.amcfListDependencia = amcfListDependencia;
	}

	public Usuario getAmcfUsuario() {
		return amcfUsuario;
	}

	public void setAmcfUsuario(Usuario amcfUsuario) {
		this.amcfUsuario = amcfUsuario;
	}

	public MallaCurricularDto getAmcfMallaCurricularDto() {
		return amcfMallaCurricularDto;
	}

	public void setAmcfMallaCurricularDto(MallaCurricularDto amcfMallaCurricularDto) {
		this.amcfMallaCurricularDto = amcfMallaCurricularDto;
	}

	public List<MallaCurricularNivelDto> getAmcfListMallaCurricularNivelDto() {
		return amcfListMallaCurricularNivelDto;
	}

	public void setAmcfListMallaCurricularNivelDto(List<MallaCurricularNivelDto> amcfListMallaCurricularNivelDto) {
		this.amcfListMallaCurricularNivelDto = amcfListMallaCurricularNivelDto;
	}

	public Integer getAmcfActivarReporte() {
		return amcfActivarReporte;
	}

	public void setAmcfActivarReporte(Integer amcfActivarReporte) {
		this.amcfActivarReporte = amcfActivarReporte;
	}

	public String getAmcfNombreArchivo() {
		return amcfNombreArchivo;
	}

	public void setAmcfNombreArchivo(String amcfNombreArchivo) {
		this.amcfNombreArchivo = amcfNombreArchivo;
	}

	public String getAmcfNombreReporte() {
		return amcfNombreReporte;
	}

	public void setAmcfNombreReporte(String amcfNombreReporte) {
		this.amcfNombreReporte = amcfNombreReporte;
	}

	public String getAmcfTipoArchivo() {
		return amcfTipoArchivo;
	}

	public void setAmcfTipoArchivo(String amcfTipoArchivo) {
		this.amcfTipoArchivo = amcfTipoArchivo;
	}

	public Boolean getAmcfAgregarMalla() {
		return amcfAgregarMalla;
	}

	public void setAmcfAgregarMalla(Boolean amcfAgregarMalla) {
		this.amcfAgregarMalla = amcfAgregarMalla;
	}

	public List<TipoFormacionMalla> getAmcfListTipoFormacionMalla() {
		return amcfListTipoFormacionMalla;
	}

	public void setAmcfListTipoFormacionMalla(List<TipoFormacionMalla> amcfListTipoFormacionMalla) {
		this.amcfListTipoFormacionMalla = amcfListTipoFormacionMalla;
	}

	public PeriodoAcademicoDto getAmcfPeriodoAcademicoDto() {
		return amcfPeriodoAcademicoDto;
	}

	public void setAmcfPeriodoAcademicoDto(PeriodoAcademicoDto amcfPeriodoAcademicoDto) {
		this.amcfPeriodoAcademicoDto = amcfPeriodoAcademicoDto;
	}


	public List<PeriodoAcademico> getAmcfListPeriodoAcademico() {
		return amcfListPeriodoAcademico;
	}


	public void setAmcfListPeriodoAcademico(List<PeriodoAcademico> amcfListPeriodoAcademico) {
		this.amcfListPeriodoAcademico = amcfListPeriodoAcademico;
	}


	public List<MallaCurricularNivelPosgradoDto> getAmcfListMallaCurricularNivelPosgradoDto() {
		return amcfListMallaCurricularNivelPosgradoDto;
	}


	public void setAmcfListMallaCurricularNivelPosgradoDto(
			List<MallaCurricularNivelPosgradoDto> amcfListMallaCurricularNivelPosgradoDto) {
		this.amcfListMallaCurricularNivelPosgradoDto = amcfListMallaCurricularNivelPosgradoDto;
	}
	



	
	
}
