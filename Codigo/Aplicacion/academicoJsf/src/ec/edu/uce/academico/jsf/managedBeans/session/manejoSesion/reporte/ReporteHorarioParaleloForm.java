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
   
 ARCHIVO:     ReporteHorarioParaleloForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para el reporte de horario por paralelo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
02-ABRIL-2018			Dennis Collaguazo 			      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
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

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseAulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HorarioAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReporteHorarioParaleloForm.
 * Managed Bean que maneja las peticiones para el reporte de horario por paralelo.
 * @author dcollaguazo.
 * @version 1.0
 */
@ManagedBean(name="reporteHorarioParaleloForm")
@SessionScoped
public class ReporteHorarioParaleloForm implements Serializable{

	private static final long serialVersionUID = -7351056909580145148L;
	
	//*******************************************************************/
	//******************* ATRIBUTOS DE LA CLASE *************************/
	//*******************************************************************/

	//GENERAL
	private Usuario rhpfUsuario;
	private Integer rhpfTipoUsuario;
	private String rhpfTipoCarrera;
	
	//PARA BUSQUEDA DE HORARIO ACADEMICO
	private HorarioAcademicoDto rhpfHorarioAcademicoBusq;
	private List<PeriodoAcademicoDto> rhpfListPeriodoAcademicoDtoBusq;
	private List<CarreraDto> rhpfListCarreraDtoBusq;
	private List<Nivel> rhpfListNivelBusq;
	private List<ParaleloDto> rhpfListParaleloDtoBusq;
	private List<MateriaDto> rhpfListMateriaDtoBusq;
	private List<DocenteJdbcDto> rhpfListDocenteJdbcDto;
	
	//PARA VER
	private List<HorarioAcademicoDto> rhpfListHorarioAcademicoBusq;
	private MateriaDto rhpfMateriaVer;
	
	//PARA HABILITAR
	private Integer rhpfValidadorClic;
	private Integer rhpfModoAsignatura;
	private boolean rhpfDeshabilitaDia;
	private Integer rhpfHabilitaBoton;
	

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
	UsuarioRolServicio servRhpfUsuarioRolServicio;
	@EJB
	PeriodoAcademicoDtoServicioJdbc servRhpfPeriodoAcademicoDtoServicioJdbc;
	@EJB
	CarreraDtoServicioJdbc servRhpfCarreraDtoServicioJdbc;
	@EJB
	NivelServicio servRhpfNivelServicio;
	@EJB
	ParaleloDtoServicioJdbc servRhpfParaleloDtoServicioJdbc;
	@EJB
	MateriaDtoServicioJdbc servRhpfMateriaDtoServicioJdbc;
	@EJB
	HorarioAcademicoDtoServicioJdbc servRhpfHorarioAcademicoDtoServicioJdbc;
	@EJB
	PeriodoAcademicoServicio servRhpfPeriodoAcademicoServicio;
	@EJB
	AulaDtoServicioJdbc servRhpfAulaDtoServicioJdbc;
	@EJB
	HoraClaseDtoServicioJdbc servRhpfHoraClaseDtoServicioJdbc;
	@EJB
	HoraClaseAulaServicio servRhpfHoraClaseAulaServicio;
	@EJB
	MallaCurricularParaleloServicio servRhpfMallaCurricularParaleloServicio;
	@EJB
	HorarioAcademicoServicio servRhpfHorarioAcademicoServicio;
	@EJB
	DocenteDtoServicioJdbc servRhpfDocenteDtoServicioJdbc;
	@EJB
	DependenciaServicio servRhpfDependenciaServicio;
	@EJB
	CarreraServicio servRhpfCarreraServicio;
	@EJB
	DetallePuestoServicio servRhpfDetallePuestoServicio;
	@EJB
	CargaHorariaServicio servRhpfCargaHorariaServicio;
	@EJB
	DependenciaDtoServicioJdbc servRhpfDependenciaDtoServicioJdbc;
	@EJB 
	EdificioDtoServicioJdbc servRhpfEdificioDtoServicioJdbc;
	@EJB
	AulaServicio servRhpfAulaServicio;
	@EJB
	MallaCurricularParaleloDtoServicioJdbc servRhpfMallaCurricularParaleloDtoServicioJdbc;
	@EJB
	ParaleloServicio servRhpfParaleloServicio;
	
	// ********************************************************************/
	// ************* METODOS GENERALES DE LA CLASE ************************/
	// ********************************************************************/
	/**
	 * Dirige la navegación hacia la página de listar
	 * @param usuario - usuario objeto que ingresa al método
	 * @return navegacion al listar 
	 */
	public String irHorarioAcademico(Usuario usuario){
		rhpfUsuario = usuario;
		try {
			//INICIO PARAMETROS DE BUSQUEDA
			iniciarParametros(); //inicializa los parametros
			List<UsuarioRol> usroLista = servRhpfUsuarioRolServicio.buscarXUsuario(rhpfUsuario.getUsrId());
			for (UsuarioRol item : usroLista) {
				if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rhpfTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rhpfTipoUsuario = RolConstantes.ROL_DIRCARRERA_VALUE.intValue();
					}
					if(item.getUsroRol().getRolId() == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rhpfTipoUsuario = RolConstantes.ROL_SOPORTE_VALUE.intValue();
					}
					rhpfTipoCarrera="carreras";
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rhpfTipoUsuario = RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue();
					rhpfTipoCarrera="programas";
				}else if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rhpfTipoUsuario = RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue();
					rhpfTipoCarrera="suficiencias";
				}
			}
			//LISTO LOS PERIODOS ACADEMICOS
			if(rhpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
				rhpfListPeriodoAcademicoDtoBusq = servRhpfPeriodoAcademicoDtoServicioJdbc.listarXEstado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			}else if (rhpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				rhpfListPeriodoAcademicoDtoBusq = servRhpfPeriodoAcademicoDtoServicioJdbc.listarXEstadoPosgrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);	
			}else if(rhpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				//para idiomas
				rhpfListPeriodoAcademicoDtoBusq = servRhpfPeriodoAcademicoDtoServicioJdbc.listarXEstadoXTipo(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE, PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
			}
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} 
		return "irReporteHorarioParalelo";
	}
	
	/**
	 * Método que busca las carreras por usuario de acuerdo al periodo
	 * @param pracId - pracId id de la del periodo academico a buscar
	 */
	public void cambiarCarrera(int pracId){
		try {
			//ASIGNO EL PERIODO ACADEMICO EN EL QUE SE ESTA TRABAJANDO
//			pracId = rhpfHorarioAcademicoBusq.getPracId();
			//SETEO LA CARRERA
			rhpfHorarioAcademicoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL NIVEL
			rhpfHorarioAcademicoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL PARALELO
			rhpfHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			rhpfHorarioAcademicoBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			//ANULO LA LISTA DE CARRERA
			rhpfListCarreraDtoBusq = null;
			//ANULO LA LISTA DE NIVEL
			rhpfListNivelBusq = null;
			//ANULO LA LISTA DE PARALELO
			rhpfListParaleloDtoBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			rhpfListMateriaDtoBusq = null;
			
			//LISTO LAS CARRERAS POR PERIODO ACADEMICO
			if(pracId != GeneralesConstantes.APP_ID_BASE){
				if(rhpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
					if(rhpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()){
						rhpfListCarreraDtoBusq = servRhpfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rhpfUsuario.getUsrId(),  RolConstantes.ROL_SECRECARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
					}
					if(rhpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue()){
						rhpfListCarreraDtoBusq = servRhpfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rhpfUsuario.getUsrId(),  RolConstantes.ROL_DIRCARRERA_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
					}
					if(rhpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue()){
						rhpfListCarreraDtoBusq=servRhpfCarreraDtoServicioJdbc.listarTodos();
					}
				}else if(rhpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
					rhpfListCarreraDtoBusq = servRhpfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(rhpfUsuario.getUsrId(), RolConstantes.ROL_BD_SECREPOSGRADO, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
				}else if(rhpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
					rhpfListCarreraDtoBusq = servRhpfCarreraDtoServicioJdbc.listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(rhpfUsuario.getUsrId(),  RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE, pracId);
				}
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.carrera.no.encontrado.exception")));
		}
	}
	
	/**
	 * Método que busca el nivel por carrera
	 */
	public void cambiarNivel(){
		try {
			//SETEO EL NIVEL
			rhpfHorarioAcademicoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO EL PARALELO
			rhpfHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			rhpfHorarioAcademicoBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);
			
			//ANULO LA LISTA DE NIVEL
			rhpfListNivelBusq = null;
			//ANULO LA LISTA DE PARALELO
			rhpfListParaleloDtoBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			rhpfListMateriaDtoBusq = null;
			//LISTO LOS NIVELES
			if(rhpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				int contador = 0;
				for (CarreraDto itemCarr : rhpfListCarreraDtoBusq) {
					if(itemCarr.getCrrTipo() == CarreraConstantes.TIPO_NIVELEACION_VALUE){
						contador = contador + 1;
					}
				}
				if(contador >= 1){
					rhpfListNivelBusq = servRhpfNivelServicio.listarNivelacion();
				}else{
					rhpfListNivelBusq = servRhpfNivelServicio.listarTodos();
				}
			}else if(rhpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				rhpfListNivelBusq = servRhpfNivelServicio.listarTodosPosgrado();
			}
		} catch (NivelNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.nivel.no.encontrado.exception")));
		} catch (NivelException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	/**
	 * Método que busca los paralelos 
	 */
	public void cambiarParalelo(){
		try {
			//SETEO EL PARALELO
			rhpfHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
			//SETEO LA MATERIA
			rhpfHorarioAcademicoBusq.setMtrId(GeneralesConstantes.APP_ID_BASE);

			//ANULO LA LISTA DE PARALELO
			rhpfListParaleloDtoBusq = null;
			//ANULO LA LISTA DE RESULTADOS
			rhpfListMateriaDtoBusq = null;
			//LISTO LOS PARALELOS
			if(rhpfTipoUsuario == RolConstantes.ROL_SECRECARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_DIRCARRERA_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_SOPORTE_VALUE.intValue() || rhpfTipoUsuario == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE.intValue()){
				rhpfListParaleloDtoBusq = servRhpfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivel(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getCrrId(), rhpfHorarioAcademicoBusq.getNvlId());
			}else if(rhpfTipoUsuario == RolConstantes.ROL_SECREPOSGRADO_VALUE.intValue()){
				rhpfListParaleloDtoBusq = servRhpfParaleloDtoServicioJdbc.listarParaleloXPeriodoCarreraNivelPostgrado(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getCrrId(), rhpfHorarioAcademicoBusq.getNvlId());
			}
		} catch (ParaleloDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.cambiar.paralelo.no.encontrado.exception")));
		}
	}
	
	
	/**
	 * Método que busca materias del periodo , carrera , nivel y paralelo
	 */
	public void buscar(){
		try {
			//ANULO LA LISTA DE RESULTADOS
			rhpfListMateriaDtoBusq = null;
			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
			if(rhpfHorarioAcademicoBusq.getPracId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.academico.no.seleccionado")));
			}else if(rhpfHorarioAcademicoBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.carrera.no.seleccionado")));
			}else if(rhpfHorarioAcademicoBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.nivel.no.seleccionado")));
			}else if(rhpfHorarioAcademicoBusq.getPrlId() == GeneralesConstantes.APP_ID_BASE){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
			}else {
				//BUSCO LAS MATERIAS POR LOS PARAMETROS INGRESADOS
				// sin proceso de modulares
//				rhpfListMateriaDtoBusq = servRhpfMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlId(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getCrrId(), rhpfHorarioAcademicoBusq.getNvlId(), rhpfHorarioAcademicoBusq.getPrlId());
				//***********************************************************************************//
				//**************** busca materias con sub materias sin materias modulares ***********//
				//***********************************************************************************//
				rhpfListMateriaDtoBusq = servRhpfMateriaDtoServicioJdbc.listarPpracIdPcrrIdPnvlIdPprlIdModular(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getCrrId(), rhpfHorarioAcademicoBusq.getNvlId(), rhpfHorarioAcademicoBusq.getPrlId());
				//***********************************************************************************//
//				int salto = 0;
//				for (MateriaDto item : rhpfListMateriaDtoBusq) {
//					if(item.getMtrHoras() == null && item.getMtrHoras() == 0 && item.getMtrCreditos() == null && item.getMtrCreditos() == 0 ){
//						salto = 1;
//						break;
//					}
//				}
//				if(salto == 1){
//					
//				}
				if(rhpfListMateriaDtoBusq.size() > 0){
					rhpfHabilitaBoton = GeneralesConstantes.APP_ID_BASE;
				}
			}
		} catch (MateriaDtoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			iniciarParametros();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.carrera.nivel.paralelo.no.encontrado.exception")));
			
		}
	}
	
//	/**
//	 * Método que busca materias del periodo , carrera , nivel , paralelo y  materia
//	 */
//	public void consultaHorario(){
//		try {
//			//ANULO LA LISTA DE HORARIOS PARA BUSCAR SI TIENE ASIGNADOS
//			rhpfListHorarioAcademicoCompBusq = null;
//			//VERIFICO QUE SE HA SELECCIONADO TODOS LOS CAMPOS REQUERIDOS
//			if(rhpfHorarioAcademicoCompBusq.getPracId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.periodo.academico.no.seleccionado")));
//			}else if(rhpfHorarioAcademicoCompBusq.getCrrId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.carrera.no.seleccionado")));
//			}else if(rhpfHorarioAcademicoCompBusq.getNvlId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.nivel.no.seleccionado")));
//			}else if(rhpfHorarioAcademicoCompBusq.getPrlId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
//			}else if(rhpfHorarioAcademicoCompBusq.getMtrId() == GeneralesConstantes.APP_ID_BASE){
//				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.buscar.paralelo.no.seleccionado")));
//			}else {
//				//BUSCO LOS HORARIOS ACADEMICOS DE LA MATERIA
//				rhpfListHorarioAcademicoCompBusq = servRhpfHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(rhpfHorarioAcademicoCompBusq.getPracId(), rhpfHorarioAcademicoCompBusq.getPrlId(), rhpfHorarioAcademicoCompBusq.getMtrId());
//				if(rhpfListHorarioAcademicoCompBusq.size() <= 0 || rhpfListHorarioAcademicoCompBusq == null){
//					bandera = 3;
//				}else{
//					bandera = 0;
//					FacesUtil.mensajeError("No puede compartir este horario, ya tiene un horario académico definido");
//				}
//			}
//		} catch (HorarioAcademicoDtoException e) {
//			iniciarParametros();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		} catch (HorarioAcademicoDtoNoEncontradoException e) {
//			iniciarParametros();
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeError(e.getMessage());
//		}
//	}
//	
	/**
	 *Método que limpia los parametros de busqueda
	 */
	public void limpiar(){
		iniciarParametros(); //inicaliza los parametros
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
	 * Navega hacia la página de ver horario academico
	 * @param materiaDto - materiaDto entidad dto de materia
	 * @return  Navegacion a la pagina de ver horario academico.
	 */
	public String irVerHorario(MateriaDto materiaDto){
		String retorno = null;
		rhpfMateriaVer = materiaDto;
		try {
			rhpfDeshabilitaDia = false;
			
			//busca los horarios academicos por periodo paralelo y materia
			rhpfListHorarioAcademicoBusq = servRhpfHorarioAcademicoDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getPrlId(), rhpfMateriaVer.getMtrId());
			
			Integer HracDtpsId = GeneralesConstantes.APP_ID_BASE;
			if(rhpfListHorarioAcademicoBusq.size() > 0){ // si existe al menos un registro
				for (HorarioAcademicoDto item : rhpfListHorarioAcademicoBusq) {
					HracDtpsId = item.getHracMlcrprIdComp();
					if(item.getPrsId() != null){
						rhpfMateriaVer.setPrsNombres(item.getPrsNombres());
						rhpfMateriaVer.setPrsPrimerApellido(item.getPrsPrimerApellido());
						rhpfMateriaVer.setPrsSegundoApellido(item.getPrsSegundoApellido());
						HracDtpsId = item.getHracMlcrprIdComp();
						break;
					}
				}
			}else{ // no existe registros
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.ver.horario.horario.academico.no.existe")));
			}
			
			//busca el docente de la materia
//			rhpfListDocenteJdbcDto = servRhpfDocenteDtoServicioJdbc.listarHorarioPpracIdPprlIdPmtrId(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getPrlId(), rhpfMateriaVer.getMtrId());
			rhpfListDocenteJdbcDto = servRhpfDocenteDtoServicioJdbc.listarHorarioXpracIdXprlIdXmtrIdDocente(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getPrlId(), rhpfMateriaVer.getMtrId(), HracDtpsId.intValue());
			
			if(rhpfListDocenteJdbcDto.size() <= 0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Administracion.horario.academico.ir.ver.horario.docente.no.existe")));
			}
			
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DocenteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}  
		retorno = "irVerHorario";
		return retorno;
	}
	
	/**
	 *Navega hacia la pagina del listar las materias del paralelo
	 *@return  Navegacion a la pagina del listar las materias del paralelo.
	 */
	public String irListarHorarioAcademico(){
		String retorno = null;
		retorno = "irHorarioAcademico";
		return retorno;
	}
	
	/**
	 * Método que genera el reporte
	 */
	public void generarReporteHorarioParalelo(List<HorarioAcademicoDto> listaHorarioParalelo){
		try {
		List<Map<String, Object>> frmRrmCampos = null;
		Map<String, Object> frmRrmParametros = null;
		String frmRrmNombreReporte = null;
		// ****************************************************************//
		// ********* GENERACION DEL REPORTE DE HORARIO *********//
		// ****************************************************************//
		// ****************************************************************//
		frmRrmNombreReporte = "reporteHorarioParalelo";
		frmRrmParametros = new HashMap<String, Object>();
		SimpleDateFormat formato = new SimpleDateFormat("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
		String fecha = formato.format(new Date());
		frmRrmParametros.put("fecha",fecha);
		Nivel nivel = new Nivel();
		nivel = servRhpfNivelServicio.buscarPorId(rhpfHorarioAcademicoBusq.getNvlId());
		frmRrmParametros.put("curso", nivel.getNvlDescripcion());
		PeriodoAcademico periodoAcademico = new PeriodoAcademico();
		periodoAcademico = servRhpfPeriodoAcademicoServicio.buscarPorId(rhpfHorarioAcademicoBusq.getPracId());
		frmRrmParametros.put("periodo", periodoAcademico.getPracDescripcion());
		Dependencia dependencia = new Dependencia();
		Carrera carr = new Carrera();
		dependencia = servRhpfDependenciaServicio.buscarFacultadXcrrId(rhpfHorarioAcademicoBusq.getCrrId());
		carr = servRhpfCarreraServicio.buscarPorId(rhpfHorarioAcademicoBusq.getCrrId());
		Paralelo parale = new Paralelo();
		parale = servRhpfParaleloServicio.buscarPorId(rhpfHorarioAcademicoBusq.getPrlId());
		frmRrmParametros.put("paralelo", parale.getPrlDescripcion());
		frmRrmParametros.put("facultad", dependencia.getDpnDescripcion());
		frmRrmParametros.put("carrera", carr.getCrrDescripcion());
		frmRrmParametros.put("nick", rhpfUsuario.getUsrNick());
		
		
//		StringBuilder sbCodigo = new StringBuilder();
//		StringBuilder sbAsignatura = new StringBuilder();
//		StringBuilder sbHoras = new StringBuilder();
//		StringBuilder sbAula = new StringBuilder();
//		StringBuilder sbCapacidad = new StringBuilder();
//		StringBuilder sbInscritos = new StringBuilder();
//		StringBuilder sbDia = new StringBuilder();
//		StringBuilder sbInicioFin = new StringBuilder();
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = null;
		
		
		for (HorarioAcademicoDto item : listaHorarioParalelo) {
			datoHorario = new HashMap<String, Object>();
			
			datoHorario.put("codigo", item.getMtrCodigo());
			datoHorario.put("asignatura", item.getMtrDescripcion());
			
			Integer horaAux = null;
			horaAux = modalidadCreditosHoras(item.getMtrCreditos(), item.getMtrHoras());
			if(horaAux == 0){
				datoHorario.put("horas", item.getMtrCreditos().toString());
			}
			if(horaAux == 1){
				datoHorario.put("horas", item.getMtrHoras().toString());
			}
			if(horaAux == GeneralesConstantes.APP_ID_BASE){
				datoHorario.put("horas", "");
			}
			
			datoHorario.put("aula", item.getAlaDescripcion());
			datoHorario.put("capacidad", item.getAlaCapacidad());
			datoHorario.put("inscritos", item.getMlcrprInscritos().toString());
			datoHorario.put("dia", HorarioAcademicoConstantes.traerDiaHorarioAcademico(item.getHracDia()));
			datoHorario.put("iniciofin", item.getHoclDescripcion());
			
			
//			sbCodigo.append(item.getMtrCodigo());
//			sbAsignatura.append(item.getMtrDescripcion());
//			if(item.getMtrHoras().intValue() == 0 || item.getMtrHoras() == null){
//				sbHoras.append(item.getMtrCreditos());
//			}else if(item.getMtrCreditos().intValue() == 0 || item.getMtrCreditos() == null){
//				sbHoras.append(item.getMtrHoras());
//			} else {
//				sbHoras.append("");
//			}
//			sbAula.append(item.getAlaDescripcion());
//			sbCapacidad.append(item.getAlaCapacidad().toString());
//			sbInscritos.append(item.getPrlCupo());
//			sbDia.append(item.getHracDia());
//			sbInicioFin.append(item.getHoclDescripcion());
			frmRrmCampos.add(datoHorario);
		}
		
//		frmRrmParametros.put("codigo", sbCodigo.toString());
//		frmRrmParametros.put("asignatura", sbAsignatura.toString());
//		frmRrmParametros.put("horas", sbHoras.toString());
//		frmRrmParametros.put("aula", sbAula.toString());
//		frmRrmParametros.put("capacidad", sbCapacidad.toString());
//		frmRrmParametros.put("inscritos", sbInscritos.toString());
//		frmRrmParametros.put("dia", sbDia.toString());
//		frmRrmParametros.put("iniciofin", sbInicioFin.toString());
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/academico/reportes/archivosJasper/horario/paralelo");
		frmRrmParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraMatricula.png");
		frmRrmParametros.put("imagenPie", pathGeneralReportes+"/pieMatricula.png");
		
		
//		frmRrmCampos.add(datoHorario);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmRrmParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmRrmNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		} catch (NivelNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	
	//INICIAR PARÁMETROS DE BUSQUEDA
	/**
	 * Método que inicializa parametros de búsqueda
	 */
	public void iniciarParametros(){
		//INSTANCIO HORARIO ACADEMICO BUSQ
		rhpfHorarioAcademicoBusq = new HorarioAcademicoDto();
		//INSTANCIO MATERIA
		rhpfMateriaVer = new MateriaDto();
		//ANULO LA LISTA DE CARRERAS 
		rhpfListCarreraDtoBusq = null;
		//ANULO LA LISTA DE NIVELES
		rhpfListNivelBusq = null;
		//ANULO LA LISTA DE PARALELOS
		rhpfListParaleloDtoBusq = null;
		//ANULO LA LISTA DE MATERIAS
		rhpfListMateriaDtoBusq = null;
		//ANULO LA LISTA DE HORARIO ACADEMICO BUSQ
		rhpfListHorarioAcademicoBusq = null;
		//ANULO LA LISTA DE DOCENTE
		rhpfListDocenteJdbcDto = null;
		//ANULO LA LISTA DE CARRERAS BUSQ DOC
//		rhpfListCarreraBusq = null;
		//ASIGNO EL VALOR -99 A LA VALIDACIÓN
		rhpfModoAsignatura = GeneralesConstantes.APP_ID_BASE;
		//SETEO HABILITA DIA - EDITA - NUEVO
		rhpfDeshabilitaDia = false;
		//SETEO PERIODO ACADEMICO
		rhpfHorarioAcademicoBusq.setPracId(GeneralesConstantes.APP_ID_BASE);
		//SETEO LA CARRERA
		rhpfHorarioAcademicoBusq.setCrrId(GeneralesConstantes.APP_ID_BASE);
		//SETEO EL NIVEL
		rhpfHorarioAcademicoBusq.setNvlId(GeneralesConstantes.APP_ID_BASE);
		//SETEO PARALELO
		rhpfHorarioAcademicoBusq.setPrlId(GeneralesConstantes.APP_ID_BASE);
		//ASIGNO VALOR INICIAL PARA EJECUCION DE MODALES
		bloqueaModal();
		//DESABILITA BOTON;
		rhpfHabilitaBoton = 0;
	}
	
	//HABILITA EL MODAL GUARDAR EDICION
	/**
	 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
	 */
	public void verificarClickImprimir(){
		try {
			//busca los horarios academicos por periodo paralelo
			rhpfListHorarioAcademicoBusq = servRhpfHorarioAcademicoDtoServicioJdbc.listarHorarioXpracIdXprlId(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getPrlId());
			if(rhpfListHorarioAcademicoBusq.size() > 0){
				generarReporteHorarioParalelo(rhpfListHorarioAcademicoBusq);
				habilitaModalImpresion();
			}else{
				bloqueaModal();
			}
			rhpfHabilitaBoton = 0;
		} catch (HorarioAcademicoDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
		
	}
	
	//HABILITA EL MODAL GUARDAR EDICION
		/**
		 * Metodo que genera el reporte y habilita el modal para impresion en pantalla del pdf
		 */
		public void verificarClickImprimir2(){
			try {
				//busca los horarios academicos por periodo paralelo
				rhpfListHorarioAcademicoBusq = servRhpfHorarioAcademicoDtoServicioJdbc.listarHorarioXpracIdXprlId(rhpfHorarioAcademicoBusq.getPracId(), rhpfHorarioAcademicoBusq.getPrlId());
				if(rhpfListHorarioAcademicoBusq.size() > 0){
					generarReporteHorarioParalelo(rhpfListHorarioAcademicoBusq);
					habilitaModalImpresionXls();
				}else{
					bloqueaModal();
				}
				rhpfHabilitaBoton = 0;
			} catch (HorarioAcademicoDtoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (HorarioAcademicoDtoNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}
	
	//BLOQUEA EL MODAL
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void bloqueaModal(){
		rhpfValidadorClic = 0;
	}
	
	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresion(){
		rhpfValidadorClic = 1;
	}
	
	//HABILITA MODAL IMPRESION DE PDF
	/**
	 * Método que bloquea el modal para que se despliegue en pantalla
	 */
	public void habilitaModalImpresionXls(){
		rhpfValidadorClic = 2;
	}
	
	//HABILITA SEGÚN SEA EL CASO EN CABECERA DEL XHTML DE HORAS O CREDITOS
	/**
	 * Cabecera de la columna que varia entre creditos y horas 
	 */
	public String cabeceraCreditosHoras(Integer creditos, Integer horas){
		if(creditos != null && creditos != 0){
			return "Créditos";
		}
		if(horas != null && horas != 0){
			return "Horas";
		}
		return "Horas - Créditos N/D";
	}
	
	//HABILITA EL TIPO DE ASIGNATURA - CREDITOS U HORAS
	/**
	 * Modalidad de la asignatura entre creditos y horas 
	 */
	public Integer modalidadCreditosHoras(Integer creditos, Integer horas){
		if(creditos != null && creditos != 0){
			return 0;
		}
		if(horas != null && horas != 0){
			return 1;
		}
		return GeneralesConstantes.APP_ID_BASE;
	}

	// ********************************************************************/
	// *********************** METODOS GET Y SET **************************/
	// ********************************************************************/
	public Usuario getRhpfUsuario() {
		return rhpfUsuario;
	}
	public void setRhpfUsuario(Usuario rhpfUsuario) {
		this.rhpfUsuario = rhpfUsuario;
	}
	public Integer getRhpfTipoUsuario() {
		return rhpfTipoUsuario;
	}
	public void setRhpfTipoUsuario(Integer rhpfTipoUsuario) {
		this.rhpfTipoUsuario = rhpfTipoUsuario;
	}
	public String getRhpfTipoCarrera() {
		return rhpfTipoCarrera;
	}
	public void setRhpfTipoCarrera(String rhpfTipoCarrera) {
		this.rhpfTipoCarrera = rhpfTipoCarrera;
	}
	public HorarioAcademicoDto getRhpfHorarioAcademicoBusq() {
		return rhpfHorarioAcademicoBusq;
	}
	public void setRhpfHorarioAcademicoBusq(HorarioAcademicoDto rhpfHorarioAcademicoBusq) {
		this.rhpfHorarioAcademicoBusq = rhpfHorarioAcademicoBusq;
	}
	public List<PeriodoAcademicoDto> getRhpfListPeriodoAcademicoDtoBusq() {
		rhpfListPeriodoAcademicoDtoBusq = rhpfListPeriodoAcademicoDtoBusq==null?(new ArrayList<PeriodoAcademicoDto>()):rhpfListPeriodoAcademicoDtoBusq;
		return rhpfListPeriodoAcademicoDtoBusq;
	}
	public void setRhpfListPeriodoAcademicoDtoBusq(List<PeriodoAcademicoDto> rhpfListPeriodoAcademicoDtoBusq) {
		this.rhpfListPeriodoAcademicoDtoBusq = rhpfListPeriodoAcademicoDtoBusq;
	}
	public List<CarreraDto> getRhpfListCarreraDtoBusq() {
		rhpfListCarreraDtoBusq = rhpfListCarreraDtoBusq==null?(new ArrayList<CarreraDto>()):rhpfListCarreraDtoBusq;
		return rhpfListCarreraDtoBusq;
	}
	public void setRhpfListCarreraDtoBusq(List<CarreraDto> rhpfListCarreraDtoBusq) {
		this.rhpfListCarreraDtoBusq = rhpfListCarreraDtoBusq;
	}
	public List<Nivel> getRhpfListNivelBusq() {
		rhpfListNivelBusq = rhpfListNivelBusq==null?(new ArrayList<Nivel>()):rhpfListNivelBusq;
		return rhpfListNivelBusq;
	}
	public void setRhpfListNivelBusq(List<Nivel> rhpfListNivelBusq) {
		this.rhpfListNivelBusq = rhpfListNivelBusq;
	}
	public List<ParaleloDto> getRhpfListParaleloDtoBusq() {
		rhpfListParaleloDtoBusq = rhpfListParaleloDtoBusq==null?(new ArrayList<ParaleloDto>()):rhpfListParaleloDtoBusq;
		return rhpfListParaleloDtoBusq;
	}
	public void setRhpfListParaleloDtoBusq(List<ParaleloDto> rhpfListParaleloDtoBusq) {
		this.rhpfListParaleloDtoBusq = rhpfListParaleloDtoBusq;
	}
	public List<MateriaDto> getRhpfListMateriaDtoBusq() {
		rhpfListMateriaDtoBusq = rhpfListMateriaDtoBusq==null?(new ArrayList<MateriaDto>()):rhpfListMateriaDtoBusq;
		return rhpfListMateriaDtoBusq;
	}
	public void setRhpfListMateriaDtoBusq(List<MateriaDto> rhpfListMateriaDtoBusq) {
		this.rhpfListMateriaDtoBusq = rhpfListMateriaDtoBusq;
	}
	public List<DocenteJdbcDto> getRhpfListDocenteJdbcDto() {
		rhpfListDocenteJdbcDto = rhpfListDocenteJdbcDto==null?(new ArrayList<DocenteJdbcDto>()):rhpfListDocenteJdbcDto;
		return rhpfListDocenteJdbcDto;
	}
	public void setRhpfListDocenteJdbcDto(List<DocenteJdbcDto> rhpfListDocenteJdbcDto) {
		this.rhpfListDocenteJdbcDto = rhpfListDocenteJdbcDto;
	}
	public List<HorarioAcademicoDto> getRhpfListHorarioAcademicoBusq() {
		rhpfListHorarioAcademicoBusq = rhpfListHorarioAcademicoBusq==null?(new ArrayList<HorarioAcademicoDto>()):rhpfListHorarioAcademicoBusq;
		return rhpfListHorarioAcademicoBusq;
	}
	public void setRhpfListHorarioAcademicoBusq(List<HorarioAcademicoDto> rhpfListHorarioAcademicoBusq) {
		this.rhpfListHorarioAcademicoBusq = rhpfListHorarioAcademicoBusq;
	}
	public MateriaDto getRhpfMateriaVer() {
		return rhpfMateriaVer;
	}
	public void setRhpfMateriaVer(MateriaDto rhpfMateriaVer) {
		this.rhpfMateriaVer = rhpfMateriaVer;
	}
	public Integer getRhpfValidadorClic() {
		return rhpfValidadorClic;
	}
	public void setRhpfValidadorClic(Integer rhpfValidadorClic) {
		this.rhpfValidadorClic = rhpfValidadorClic;
	}
	public Integer getRhpfModoAsignatura() {
		return rhpfModoAsignatura;
	}
	public void setRhpfModoAsignatura(Integer rhpfModoAsignatura) {
		this.rhpfModoAsignatura = rhpfModoAsignatura;
	}
	public boolean isRhpfDeshabilitaDia() {
		return rhpfDeshabilitaDia;
	}
	public void setRhpfDeshabilitaDia(boolean rhpfDeshabilitaDia) {
		this.rhpfDeshabilitaDia = rhpfDeshabilitaDia;
	}
	public Integer getRhpfHabilitaBoton() {
		return rhpfHabilitaBoton;
	}
	public void setRhpfHabilitaBoton(Integer rhpfHabilitaBoton) {
		this.rhpfHabilitaBoton = rhpfHabilitaBoton;
	}
}
