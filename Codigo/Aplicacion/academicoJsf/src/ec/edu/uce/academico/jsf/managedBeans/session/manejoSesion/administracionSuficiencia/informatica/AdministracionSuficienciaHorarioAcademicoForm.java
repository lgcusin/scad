/**************************************************************************
. *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     AdministracionPlanificacionCronogramaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración  del proceso Planificacion Cronogramas de las Suficiencias.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
22-OCT-2018				 FREDDY GUZMÁN						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSuficiencia.informatica;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EdificioDto;
import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HorarioAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DetallePuestoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PlanificacionCronogramaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CargaHorariaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.DetallePuestoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ProcesoFlujoEnum;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) AdministracionPlanificacionCronogramaForm.
 * Managed Bean que maneja las peticiones para la administración del proceso Planificacion Cronogramas de las Suficiencias.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name="administracionSuficienciaHorarioAcademicoForm")
@SessionScoped
public class AdministracionSuficienciaHorarioAcademicoForm implements Serializable{
	private static final long serialVersionUID = -3934424476360393698L;
	private int HOAC_TIPO_HORA_DOCENCIA_VALUE = 1;
	private int HOAC_TIPO_HORA_PRACTICA_VALUE = 2;
	
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	private Integer ashafPeriodoId;
	private Integer ashafCarreraId;
	private Integer ashafCarreraCompartidaId;
	private Integer ashafNivelId;
	private Integer ashafNivelCompartidaId;
	private Integer ashafParaleloId;
	private Integer ashafParaleloCompartidaId;
	private Integer ashafMateriaCompartidaId;
	private Integer ashafTipoBusqueda;
	private Integer ashafDependenciaId;
	private Integer ashafDependenciaCompartidaId;
	private Integer ashafEdificioId;
	private Integer ashafAulaId;
	private Integer ashafHorasDocenciaPorSemanaMateria;
	private Integer ashafHorasPracticaPorSemanaMateria;
	private Integer ashafHorasPorSemanaMateria;
	private Integer ashafActivarModal;
	private Integer ashafDiaSeleccionado;
	private Integer ashafTipoHoraClaseId;
	private Integer ashafAreaId;

	private String ashafHoraSeleccionada;
	private String ashafIdentificacion;
	private String  ashafPrimerApellido;
	private String  ashafDiaLabel;
	private Boolean ashafDocenteAsignado;
	private Boolean  ashafRenderTipoHoraClase;
	private Boolean  ashafRenderCompartirHorario;
	private Boolean  ashafRenderGuardarCompartirHorario;
	private Boolean  ashafRenderGuardarAula;
	private Boolean ashafEsHorarioCompartido;
	private Usuario ashafUsuario;
	private Materia ashafMateriaAcompartir;
	private MateriaDto ashafMateriaDto;
	private PersonaDto ashafPersonaDto;
	private HoraClaseDto ashafHoraClaseDto;
	private CargaHoraria ashafCargaHoraria;

	
	private List<PeriodoAcademicoDto> ashafListPeriodoAcademicoDto;
	private List<Carrera> ashafListArea;
	private List<Carrera> ashafListCarrera;
	private List<Carrera> ashafListCompartidaCarrera;
	private List<NivelDto> ashafListNivelDto;
	private List<NivelDto> ashafListCompartidaNivelDto;
	private List<ParaleloDto> ashafListParaleloDto;
	private List<ParaleloDto> ashafListCompartidaParaleloDto;
	private List<MateriaDto> ashafListMateriaDto;
	private List<MateriaDto> ashafListCompartidaMateriaDto;
	private List<PersonaDto> ashafListPersonaDto;
	private List<DependenciaDto> ashafListDependenciaDto;
	private List<DependenciaDto> ashafListCompartidaDependenciaDto;
	private List<EdificioDto> ashafListEdificioDto;
	private List<AulaDto> ashafListAulaDto;
	private List<HoraClaseDto> ashafListHoraClaseDto;
	private List<HoraClaseDto> ashafListDisponibilidadHoraClaseDto;//Disponibilidad Aula
	private List<HoraClaseDto> ashafListDocenteHoraClaseDto;//Disponibilidad Docente
	private List<CarreraDto> ashafListCarreraDto;
	private List<SelectItem> ashafListTipoHoraClase;
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/

	@EJB private HorarioAcademicoServicio servHorarioAcademico;	
	@EJB private CarreraServicio servCarrera;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private CargaHorariaServicio servCargaHoraria;
	@EJB private MallaCurricularParaleloServicio servMallaCurricularParalelo;
	@EJB private MateriaServicio servMateria;
	@EJB private DetallePuestoServicio servDetallePuesto;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	
	@EJB private HoraClaseDtoServicioJdbc servHoraClaseDto; 
	@EJB private MateriaDtoServicioJdbc servJdbcMateriaDto;
	@EJB private ParaleloDtoServicioJdbc servJdbcParaleloDto;
	@EJB private NivelDtoServicioJdbc servJdbcNivelDto;	
	@EJB private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private DependenciaDtoServicioJdbc servJdbcDependenciaDto;
	@EJB private EdificioDtoServicioJdbc servJdbcEdificioDto;
	@EJB private AulaDtoServicioJdbc servJdbcAulaDto;
	@EJB private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB private PlanificacionCronogramaDtoServicioJdbc servJdbcPlanificacionCronogramaDto;
	
	// ********************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **************************/
	// ********************************************************************/
	
	public String irInicio(){
		limpiarFormMateriasPorParalelo();
		vaciarFormMateriasPorParalelo();
		return "irInicio"; 	
	}

	
	public void irModalCompartirHorario(){
		iniciarModalCompartirHorario();
	}
	
	public String irVerHorarioClasesNivelacion(MateriaDto materia){
		ashafMateriaDto = materia;
		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		establecerLimiteHorasPorSemanaPregrado(materia);
		desactivarModalAsignacionAula();
		
		List<HoraClaseDto> plantilla = cargarHorarioClasesTemplatePorAsignatura();
		if (!plantilla.isEmpty()) {
			List<HoraClaseDto> horario = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());
			if (!horario.isEmpty()) {
				ashafListHoraClaseDto = establecerCheckBox(plantilla, horario);
			}else {
				ashafListHoraClaseDto = plantilla;
			}
			FacesUtil.limpiarMensaje();
		}
		
		ashafPersonaDto = new PersonaDto();
		if (materia.getPrsId()!= 0) {
			ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(materia.getPrsId(), ashafPeriodoId);
			ashafDocenteAsignado = Boolean.TRUE;
			ashafPersonaDto.setPrsId(materia.getPrsId());
			
			try {
			    ashafCargaHoraria = servCargaHoraria.buscarPorMallaCurricularParalelo(materia.getMlcrprId(), CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError("No se encontró Carga Horaria Activa.");
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError("Se encontró mas de una Carga Horaria Activa.");
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError("Error de conexión, comuniquése con el Administrador del Sistema.");
			}
		}else {
			ashafListDocenteHoraClaseDto = new ArrayList<>();
			ashafDocenteAsignado = Boolean.FALSE;
		}
		
		establecerSiEsHorarioCompartido(materia);
		establecerSiPuedeSerCompartidoHorario();
		cargarDependenciasParaCompartirHorarioPorUsuarioNivelacion();
//		ashafMateriaDto = materia;
//		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
//		ashafEsHorarioCompartido = Boolean.FALSE;
//		establecerLimiteHorasPorSemana(materia);
//		desactivarModalAsignacionAula();
//		
//		List<HoraClaseDto> plantilla = cargarHorarioClasesTemplatePorAsignatura();
//		if (!plantilla.isEmpty()) {
//			List<HoraClaseDto> horario = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());
//			if (!horario.isEmpty()) {
//				ashafListHoraClaseDto = establecerCheckBox(plantilla, horario);
//			}else {
//				ashafListHoraClaseDto = plantilla;
//			}
//			FacesUtil.limpiarMensaje();
//		}
//		
//		ashafPersonaDto = new PersonaDto();
//		if (materia.getPrsId()!= 0) {
//			ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(materia.getPrsId(), ashafPeriodoId);
//			ashafDocenteAsignado = Boolean.TRUE;
//			ashafPersonaDto.setPrsId(materia.getPrsId());
//			ashafCargaHoraria = cargarCargaHorariaPorDocente(materia.getMlcrprId());
//		}else {
//			ashafListDocenteHoraClaseDto = new ArrayList<>();
//			ashafDocenteAsignado = Boolean.FALSE;
//		}
		 
		
		return "irNuevoHorarioNivelacion";
	}
	

	public String irVerHorarioClases(MateriaDto materia){
		ashafMateriaDto = materia;
		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		ashafEsHorarioCompartido = Boolean.FALSE;
		establecerLimiteHorasPorSemana(materia);
		desactivarModalAsignacionAula();
		
		List<HoraClaseDto> plantilla = cargarHorarioClasesTemplatePorAsignatura();
		if (!plantilla.isEmpty()) {
			List<HoraClaseDto> horario = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());
			if (!horario.isEmpty()) {
				ashafListHoraClaseDto = establecerCheckBox(plantilla, horario);
			}else {
				ashafListHoraClaseDto = plantilla;
			}
			FacesUtil.limpiarMensaje();
		}
		
		ashafPersonaDto = new PersonaDto();
		if (materia.getPrsId()!= 0) {
			ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(materia.getPrsId(), ashafPeriodoId);
			ashafDocenteAsignado = Boolean.TRUE;
			ashafPersonaDto.setPrsId(materia.getPrsId());
			ashafCargaHoraria = cargarCargaHorariaPorDocente(materia.getMlcrprId());
		}else {
			ashafListDocenteHoraClaseDto = new ArrayList<>();
			ashafDocenteAsignado = Boolean.FALSE;
		}
		 
		
		return "irNuevoHorarioSuficiencias";
	}
	
	
	private CargaHoraria cargarCargaHorariaPorDocente(int mlcrprId){
		CargaHoraria  retorno = null;
		try {
			retorno = servCargaHoraria.buscarPorMallaCurricularParalelo(mlcrprId, CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Carga Horaria Activa.");
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError("Se encontró mas de una Carga Horaria Activa.");
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError("Error de conexión, comuniquése con el Administrador del Sistema.");
		}
		
		return retorno;
	}
	
	
	
	public String irVerHorarioClasesPregrado(MateriaDto materia){
		ashafMateriaDto = materia;
		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		establecerLimiteHorasPorSemanaPregrado(materia);
		desactivarModalAsignacionAula();
		
		List<HoraClaseDto> plantilla = cargarHorarioClasesTemplatePorAsignatura();
		if (!plantilla.isEmpty()) {
			List<HoraClaseDto> horario = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());
			if (!horario.isEmpty()) {
				ashafListHoraClaseDto = establecerCheckBox(plantilla, horario);
			}else {
				ashafListHoraClaseDto = plantilla;
			}
			FacesUtil.limpiarMensaje();
		}
		
		ashafPersonaDto = new PersonaDto();
		if (materia.getPrsId()!= 0) {
			ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(materia.getPrsId(), ashafPeriodoId);
			ashafDocenteAsignado = Boolean.TRUE;
			ashafPersonaDto.setPrsId(materia.getPrsId());
			
			try {
			    ashafCargaHoraria = servCargaHoraria.buscarPorMallaCurricularParalelo(materia.getMlcrprId(), CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError("No se encontró Carga Horaria Activa.");
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError("Se encontró mas de una Carga Horaria Activa.");
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError("Error de conexión, comuniquése con el Administrador del Sistema.");
			}
		}else {
			ashafListDocenteHoraClaseDto = new ArrayList<>();
			ashafDocenteAsignado = Boolean.FALSE;
		}
		
		establecerSiEsHorarioCompartido(materia);
		establecerSiPuedeSerCompartidoHorario();
		cargarDependenciasParaCompartirHorarioPorUsuario();
		return "irNuevoHorarioPregrado";
	}
	
	

	
	private void establecerSiPuedeSerCompartidoHorario() {
		if (!ashafEsHorarioCompartido) {
			if (ashafDocenteAsignado && !verificarLimiteHorasPorSemanaPregrado() ) {
				ashafRenderCompartirHorario = Boolean.TRUE;
			}else {
				ashafRenderCompartirHorario = Boolean.FALSE;
			}
		}else {
			ashafRenderCompartirHorario = Boolean.FALSE;
		}		
	}

	
	private void establecerSiEsHorarioCompartido(MateriaDto materia) {
		
		if (materia.getCahrMlcrprIdPrincipal() != null && !materia.getCahrMlcrprIdPrincipal().equals(0)) {
			ashafEsHorarioCompartido = Boolean.TRUE;
		}else {
			ashafEsHorarioCompartido = Boolean.FALSE;
		}
		
	}

	//TODO: CARGAR HORARIO COMPLETO
	public String irHorarioClasesPregrado(){
		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		desactivarModalAsignacionAula();
		
		if (!ashafListMateriaDto.isEmpty()) {
			ashafMateriaDto = new MateriaDto();
			
			List<HoraClaseDto> plantilla = cargarHorarioClasesTemplatePorAsignatura();
			if (!plantilla.isEmpty()) {
				for (MateriaDto materia : ashafListMateriaDto) {
					List<HoraClaseDto> horario = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());
					if (!horario.isEmpty()) {
						establecerCheckBox(plantilla, horario);
					}
					FacesUtil.limpiarMensaje();	
				}
			}

			ashafListHoraClaseDto = plantilla;
			
		}else{
			FacesUtil.mensajeError("Seleccione las opciones de búsqueda para continuar.");
		}
		
		return "irNuevoHorarioPregrado";
	}
	
	
	
	private void establecerLimiteHorasPorSemana(MateriaDto materia){
		if (materia.getMtrHoras()!= null && materia.getMtrHoras().intValue() != 0) {
			ashafHorasDocenciaPorSemanaMateria = materia.getMtrHoras();
			ashafHorasPorSemanaMateria = materia.getMtrHoras();
		}else if (materia.getMtrCreditos() != null && materia.getMtrCreditos().intValue() != 0) {
			ashafHorasDocenciaPorSemanaMateria = materia.getMtrCreditos();
			ashafHorasPorSemanaMateria = materia.getMtrCreditos();
		}else {
			ashafHorasDocenciaPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
			FacesUtil.mensajeError("Comuníquese con el Administrador del Sistema, para actualizar las Horas por Semana de la Asignatura.");
		}
	}
	
	private void establecerLimiteHorasPorSemanaPregrado(MateriaDto materia){
		
		if (materia.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
			if (materia.getMtrHoras()!= null && !materia.getMtrHoras().equals(0) && materia.getMtrHorasPracticas() != null && !materia.getMtrHorasPracticas().equals(0)) {
				ashafHorasDocenciaPorSemanaMateria = materia.getMtrHoras();
				ashafHorasPracticaPorSemanaMateria = materia.getMtrHorasPracticas();
				ashafHorasPorSemanaMateria = ashafHorasDocenciaPorSemanaMateria + ashafHorasPracticaPorSemanaMateria;
			}else {
				ashafHorasDocenciaPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
				ashafHorasPracticaPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
				ashafHorasPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
				FacesUtil.mensajeError("Comuníquese con el Administrador del Sistema, para actualizar las Horas por Semana de la Asignatura.");
			}
			
			ashafRenderTipoHoraClase = Boolean.TRUE;
		}else {
			ashafRenderTipoHoraClase = Boolean.FALSE;
			
			if (materia.getMtrCreditos() != null && !materia.getMtrCreditos().equals(0)) {
				ashafHorasDocenciaPorSemanaMateria = materia.getMtrCreditos();
				ashafHorasPracticaPorSemanaMateria = Integer.valueOf(0);
				ashafHorasPorSemanaMateria = ashafHorasDocenciaPorSemanaMateria + ashafHorasPracticaPorSemanaMateria;
			}else {
				ashafHorasDocenciaPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
				ashafHorasPracticaPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
				ashafHorasPorSemanaMateria = GeneralesConstantes.APP_ID_BASE;
				FacesUtil.mensajeError("Comuníquese con el Administrador del Sistema, para actualizar las Horas por Semana de la Asignatura.");
			}
		}
		
	}
	
	private int establecerLimiteHorasPorSemanaPregrado(Materia materia){
		
		int retorno = 0;
		
		if (materia.getMtrCarrera().getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
			if (materia.getMtrHoras()!= null && !materia.getMtrHoras().equals(0) && materia.getMtrHorasPracticas() != null && !materia.getMtrHorasPracticas().equals(0)) {
				retorno = materia.getMtrHoras() + materia.getMtrHorasPracSema();
			}else {
				FacesUtil.mensajeError("Comuníquese con el Administrador del Sistema, para actualizar las Horas por Semana de la Asignatura.");
			}
		}else {
			if (materia.getMtrCreditos() != null && !materia.getMtrCreditos().equals(0)) {
				retorno = materia.getMtrCreditos().intValue();
			}else {
				FacesUtil.mensajeError("Comuníquese con el Administrador del Sistema, para actualizar las Horas por Semana de la Asignatura.");
			}
		}
		
		return retorno;
	}
	
	
	private List<HoraClaseDto> establecerCheckBox(List<HoraClaseDto> plantilla, List<HoraClaseDto> horario ) {

		for (HoraClaseDto item: horario) {
			switch (item.getHracDia().intValue()) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclLunesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclLunesHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclLunesHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclLunesHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());
						item1.getHoclLunesHoraClaseDto().setHracId(item.getHracId());
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclMartesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclMartesHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclMartesHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclMartesHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());	
						item1.getHoclMartesHoraClaseDto().setHracId(item.getHracId());
					}
				}				
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclMiercolesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclMiercolesHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclMiercolesHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclMiercolesHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());
						item1.getHoclMiercolesHoraClaseDto().setHracId(item.getHracId());
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclJuevesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclJuevesHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclJuevesHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclJuevesHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());	
						item1.getHoclJuevesHoraClaseDto().setHracId(item.getHracId());
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclViernesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclViernesHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclViernesHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclViernesHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());
						item1.getHoclViernesHoraClaseDto().setHracId(item.getHracId());
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclSabadoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclSabadoHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclSabadoHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclSabadoHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());
						item1.getHoclSabadoHoraClaseDto().setHracId(item.getHracId());
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
				for (HoraClaseDto item1 : plantilla) {
					if (item.getHoclHoraInicio().equals(item1.getHoclHoraInicio())) {
						item1.getHoclDomingoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						item1.getHoclDomingoHoraClaseDto().setHoclDisableCheck(Boolean.TRUE);
						item1.getHoclDomingoHoraClaseDto().setAlaCodigo(item.getAlaCodigo());
						item1.getHoclDomingoHoraClaseDto().setAlaDescripcion(item.getAlaDescripcion());
						item1.getHoclDomingoHoraClaseDto().setHracId(item.getHracId());
					}
				}
				break;
			}
			
		}
		
		return plantilla;
	}

	private List<HoraClaseDto> cargarHorarioClasesTemplatePorAsignatura(){
		
		List<HoraClaseDto>  retorno = new ArrayList<>();
		
		 try {
			retorno = servHoraClaseDto.listarTemplateHorarioClases();
			if (!retorno.isEmpty()) {
				for (int i = 0; i < HorarioAcademicoConstantes.DIA_DOMINGO_VALUE+1; i++) {
					for (int j = 0; j < retorno.size(); j++) {
						switch (i) {
						case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
							HoraClaseDto horarioL = new HoraClaseDto(i,j, 0);
							horarioL.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioL.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioL.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioL.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclLunesHoraClaseDto(horarioL);
							break;
						case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
							HoraClaseDto horarioM = new HoraClaseDto(i,j, 0);
							horarioM.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioM.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioM.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioM.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclMartesHoraClaseDto(horarioM);
							break;
						case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
							HoraClaseDto horarioX = new HoraClaseDto(i,j, 0);
							horarioX.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioX.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioX.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioX.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclMiercolesHoraClaseDto(horarioX);
							break;
						case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
							HoraClaseDto horarioJ = new HoraClaseDto(i,j, 0);
							horarioJ.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioJ.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioJ.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioJ.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclJuevesHoraClaseDto(horarioJ);
							break;
						case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
							HoraClaseDto horarioV = new HoraClaseDto(i,j, 0);
							horarioV.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioV.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioV.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioV.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclViernesHoraClaseDto(horarioV);
							break;
						case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
							HoraClaseDto horarioS = new HoraClaseDto(i,j, 0);
							horarioS.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioS.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioS.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioS.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclSabadoHoraClaseDto(horarioS);
							break;
						case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
							HoraClaseDto horarioD = new HoraClaseDto(i,j, 0);
							horarioD.setHoclDescripcion(retorno.get(j).getHoclDescripcion());
							horarioD.setHoclHoraInicio(retorno.get(j).getHoclHoraInicio());
							horarioD.setHoclHoraFin(retorno.get(j).getHoclHoraFin());
							horarioD.setHoclCheckBox(Boolean.FALSE);
							retorno.get(j).setHoclDomingoHoraClaseDto(horarioD);
							break;
						}
					}
				}
			}
			
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError("Error de conexión, comuníquese con el Administrador del Sistema.");
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Error de conexión, comuníquese con el Administrador del Sistema.");
		}

		return retorno;
	}
	
	public static String establecerDiaPorCodigo(int param){
		String retorno = "";
		
		switch (param) {
		case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_LUNES_LABEL;
			break;
		case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_MARTES_LABEL;
			break;
		case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL;
			break;
		case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_JUEVES_LABEL;
			break;
		case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_VIERNES_LABEL;
			break;
		case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_SABADO_LABEL;
			break;
		case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
			retorno = HorarioAcademicoConstantes.DIA_DOMINGO_LABEL;
			break;
		}
		
		return retorno;
	}
	
	public void buscarAula(HoraClaseDto horario, int dia, String hora){
		ashafDiaSeleccionado = dia;
		ashafHoraSeleccionada = hora;
//		ashafHoraClaseDto = horario;
		
		try {
			boolean limiteHoras = false;
			ashafListDependenciaDto = servJdbcDependenciaDto.listarXUsuario(ashafUsuario.getUsrId());
			
			switch (dia) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_LUNES_LABEL;
				System.out.println(horario.getHoclLunesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclLunesHoraClaseDto().getHoclPosicionY());

				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();	
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_MARTES_LABEL;
				System.out.println(horario.getHoclMartesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclMartesHoraClaseDto().getHoclPosicionY());
				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL;
				System.out.println(horario.getHoclMiercolesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclMiercolesHoraClaseDto().getHoclPosicionY());
				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();	
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_JUEVES_LABEL;
				System.out.println(horario.getHoclJuevesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclJuevesHoraClaseDto().getHoclPosicionY());
				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_VIERNES_LABEL;
				System.out.println(horario.getHoclViernesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclViernesHoraClaseDto().getHoclPosicionY());
				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();	
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_SABADO_LABEL;
				System.out.println(horario.getHoclSabadoHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclSabadoHoraClaseDto().getHoclPosicionY());
				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_DOMINGO_LABEL;
				System.out.println(horario.getHoclDomingoHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclDomingoHoraClaseDto().getHoclPosicionY());
				limiteHoras = verificarLimiteHorasPorSemana();
				if (limiteHoras) {
					activarModalAsignacionAula();	
					ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
					ashafListAulaDto = null;
					ashafListDisponibilidadHoraClaseDto = null;
				}else {
					desactivarModalAsignacionAula();
					FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasDocenciaPorSemanaMateria + " horas por semana de la Asignatura.");
				}
				break;
			}
			
		} catch (DependenciaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Registre carreras a su Rol, para poder buscar Aulas.");
		}
		
		
	}
	
	public void buscarAulaNivelacion(HoraClaseDto horario, int dia, String hora){
		ashafDiaSeleccionado = dia;
		ashafHoraSeleccionada = hora;

		try {
			limpiarModalAsignarAulas();
			ashafListDependenciaDto = servJdbcDependenciaDto.listarXUsuario(ashafUsuario.getUsrId());
			ashafListTipoHoraClase = cargarTiposHoraClasePorMateria();

			if (ashafRenderTipoHoraClase) {
				ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
			}else {
				ashafTipoHoraClaseId = HOAC_TIPO_HORA_DOCENCIA_VALUE;
			}

			switch (dia) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_LUNES_LABEL;
				activarModalAsignacionAula();	
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_MARTES_LABEL;
				activarModalAsignacionAula();
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL;
				activarModalAsignacionAula();	
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_JUEVES_LABEL;
				activarModalAsignacionAula();
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_VIERNES_LABEL;
				activarModalAsignacionAula();	
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_SABADO_LABEL;
				activarModalAsignacionAula();
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
				ashafDiaLabel = HorarioAcademicoConstantes.DIA_DOMINGO_LABEL;
				activarModalAsignacionAula();	
				ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
				ashafListAulaDto = null;
				ashafListDisponibilidadHoraClaseDto = null;
				break;
			}

		} catch (DependenciaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Registre carreras a su Rol, para poder buscar Aulas.");
		}

	}
	
	
	public void buscarAulaPregrado(HoraClaseDto horario, int dia, String hora){
		if (!ashafMateriaDto.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
			if (!ashafEsHorarioCompartido) {
				ashafDiaSeleccionado = dia;
				ashafHoraSeleccionada = hora;

				try {
					boolean limiteHoras = false;
					limpiarModalAsignarAulas();
					ashafListDependenciaDto = servJdbcDependenciaDto.listarXUsuario(ashafUsuario.getUsrId());
					ashafListTipoHoraClase = cargarTiposHoraClasePorMateria();

					if (ashafRenderTipoHoraClase) {
						ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
					}else {
						ashafTipoHoraClaseId = HOAC_TIPO_HORA_DOCENCIA_VALUE;
					}

					switch (dia) {
					case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_LUNES_LABEL;
//						System.out.println(horario.getHoclLunesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclLunesHoraClaseDto().getHoclPosicionY());

						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();	
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_MARTES_LABEL;
//						System.out.println(horario.getHoclMartesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclMartesHoraClaseDto().getHoclPosicionY());
						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL;
//						System.out.println(horario.getHoclMiercolesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclMiercolesHoraClaseDto().getHoclPosicionY());
						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();	
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_JUEVES_LABEL;
//						System.out.println(horario.getHoclJuevesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclJuevesHoraClaseDto().getHoclPosicionY());
						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_VIERNES_LABEL;
//						System.out.println(horario.getHoclViernesHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclViernesHoraClaseDto().getHoclPosicionY());
						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();	
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_SABADO_LABEL;
//						System.out.println(horario.getHoclSabadoHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclSabadoHoraClaseDto().getHoclPosicionY());
						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
						ashafDiaLabel = HorarioAcademicoConstantes.DIA_DOMINGO_LABEL;
//						System.out.println(horario.getHoclDomingoHoraClaseDto().getHoclPosicionX()+" "+ horario.getHoclDomingoHoraClaseDto().getHoclPosicionY());
						limiteHoras = verificarLimiteHorasPorSemanaPregrado();
						if (limiteHoras) {
							activarModalAsignacionAula();	
							ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
							ashafListAulaDto = null;
							ashafListDisponibilidadHoraClaseDto = null;
						}else {
							desactivarModalAsignacionAula();
							FacesUtil.mensajeInfo("No se puede asignar más horas al Horario de Clases ya que excedería las " + ashafHorasPorSemanaMateria + " horas por semana de la Asignatura.");
						}
						break;
					}

				} catch (DependenciaDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (DependenciaDtoNoEncontradoException e) {
					FacesUtil.mensajeError("Registre carreras a su Rol, para poder buscar Aulas.");
				}
			}else {
				FacesUtil.mensajeError("El Horario de Clases es Compartido por ello no puede asignar Aulas.");
			}
		}else {
			desactivarModalAsignacionAula();
			FacesUtil.mensajeError("La Asignatura que intenta asignar aula es Modular, por favor registre el Horario de Clases en los Módulos.");;
		}

	}



	private List<SelectItem> cargarTiposHoraClasePorMateria() {
		List<SelectItem> retorno = new ArrayList<>();
		
		if (ashafMateriaDto.getCrrProceso().equals(CarreraConstantes.PROCESO_PREGRADO_REDISENO_VALUE)) {
			retorno.add(new SelectItem(Integer.valueOf(1),String.valueOf("DOCENCIA")));
			retorno.add(new SelectItem(Integer.valueOf(2),String.valueOf("PRÁCTICA")));
		}
		
		return retorno;
	}



	private boolean verificarCruceHorarioDocente(HoraClaseDto horario) {
		boolean retorno = false;

		if (ashafPersonaDto.getPrsId()!= 0) {
			ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(ashafPersonaDto.getPrsId(), ashafPeriodoId);
		}else {
			ashafListDocenteHoraClaseDto = new ArrayList<>();
		}		

		if (!ashafListDocenteHoraClaseDto.isEmpty()) {
			for (HoraClaseDto item : ashafListDocenteHoraClaseDto) {
				if (item.getHracDia().equals(horario.getHracDia()) && item.getHoclHoraInicio().equals(horario.getHoclHoraInicio())) {
					retorno = true;
					FacesUtil.mensajeInfo("No se puede asignar el Aula seleccionada porque genera Cruce con el Horario del Docente en el Paralelo " + item.getHracParaleloDto().getPrlCodigo() + ".");
					break;
				}
			}
		}

		return retorno;
	}
	
	private boolean verificarCruceHorarioMateriasMismoParalelo() {
		boolean retorno = false;

		List<HoraClaseDto> horarioMateriaSeleccionada = cargarHorarioAsignadoPorAsignatura(ashafMateriaDto.getMlcrprId());
		if (!horarioMateriaSeleccionada.isEmpty()) {

			if (!retorno) {
				for (MateriaDto materia : ashafListMateriaDto) {

					if (ashafMateriaDto.getMlcrprId() != materia.getMlcrprId()) {
						List<HoraClaseDto> horariosPorMateria = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());

						if (!horariosPorMateria.isEmpty()) {
							if (!retorno) {
								for (HoraClaseDto item : horariosPorMateria) {

									if (!retorno) {
										for (HoraClaseDto itemActivo : horarioMateriaSeleccionada) {
											if (item.getHracDia().equals(itemActivo.getHracDia()) && item.getHoclHoraInicio().equals(itemActivo.getHoclHoraInicio())) {
												retorno = true;
												break;
											}else if (item.getHracDia().equals(ashafDiaSeleccionado) && item.getHoclHoraInicio().equals(ashafHoraSeleccionada)) {
												retorno = true;
												break;
											}
										}
									}


								}
							}
						}
					}
				}
			}
		}else {
			if (!retorno) {
				for (MateriaDto materia : ashafListMateriaDto) {

					if (ashafMateriaDto.getMlcrprId() != materia.getMlcrprId()) {
						List<HoraClaseDto> horariosPorMateria = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());

						if (!horariosPorMateria.isEmpty()) {

							if (!retorno) {
								for (HoraClaseDto item : horariosPorMateria) {

									if (!retorno) {
										if (item.getHracDia().equals(ashafDiaSeleccionado) && item.getHoclHoraInicio().equals(ashafHoraSeleccionada)) {
											retorno = true;
											break;
										}
									}


								}
							}
						}
					}
				}
			}

		}

		if (retorno) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se puede asignar el Aula porque genera Cruce con una Asignatura del mismo Paralelo.");
		}

		return retorno;
	}
	
	

	/**
	 * Método que permite activar el modal si aun se puede asignar horas al Horario de Calses.
	 * true - dentro del limite 
	 * false - ya no se puede agregar
	 */
	private boolean verificarLimiteHorasPorSemana() {
		boolean retorno = false;

		int horasPorSemana = 0;
		for (HoraClaseDto item : ashafListHoraClaseDto) {
			if (item.getHoclLunesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclMartesHoraClaseDto().getHoclCheckBox()) {
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclMiercolesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclJuevesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclViernesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclSabadoHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
		}

		if (horasPorSemana < ashafHorasDocenciaPorSemanaMateria) {
			retorno = true;
		}

		return retorno;
	}

	private boolean verificarLimiteHorasPorSemanaPregrado() {
		boolean retorno = false;

		int horasPorSemana = 0;
		for (HoraClaseDto item : ashafListHoraClaseDto) {
			if (item.getHoclLunesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclMartesHoraClaseDto().getHoclCheckBox()) {
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclMiercolesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclJuevesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclViernesHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclSabadoHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
			if (item.getHoclDomingoHoraClaseDto().getHoclCheckBox()){
				horasPorSemana = horasPorSemana + 1; 
			}
		}

		if (horasPorSemana < ashafHorasPorSemanaMateria) {
			retorno = true;
		} 

		return retorno;
	}
	
	
	
	
	/**
	 * @param tipo -> 1 Docencia / 2 Practica
	 */
	private boolean verificarLimiteHorasPorSemanaPregradoPorTipoHora() {
		boolean retorno = false;

		int horasPorSemana = 0;
		List<HoraClaseDto> horasClasePorMateria = cargarHorarioAsignadoPorAsignatura(ashafMateriaDto.getMlcrprId());
		if (!horasClasePorMateria.isEmpty()) {
			for (HoraClaseDto item : horasClasePorMateria) {
				if (item.getHracHoraTipo().equals(ashafTipoHoraClaseId)){
					horasPorSemana = horasPorSemana + 1;
				}
			}
		}
		
		if (ashafTipoHoraClaseId.equals(HOAC_TIPO_HORA_DOCENCIA_VALUE)) {
			if (horasPorSemana >= ashafHorasDocenciaPorSemanaMateria) {
				retorno = true;
			} 
		}else if(ashafTipoHoraClaseId.equals(HOAC_TIPO_HORA_PRACTICA_VALUE)){
			if (horasPorSemana >= ashafHorasPracticaPorSemanaMateria) {
				retorno = true;
			} 
		}
		
		return retorno;
	}

	
	public void verificarLimiteHorasPorSemanaPorTipo(){
		if (!ashafTipoHoraClaseId.equals(GeneralesConstantes.APP_ID_BASE)) {
			boolean retorno = verificarLimiteHorasPorSemanaPregradoPorTipoHora();
			if (retorno) {
				ashafRenderGuardarAula = Boolean.TRUE;
				FacesUtil.mensajeError("Seleccione otro Tipo de Hora, el seleccionado ya completo el total de horas por semana. ");
			}else {
				ashafRenderGuardarAula = Boolean.FALSE;
				FacesUtil.limpiarMensaje();
			}
		}else {
			ashafRenderGuardarAula = Boolean.FALSE;
			FacesUtil.mensajeError("Seleccione un Tipo de Hora para poder asignar el Aula.");
		}
		
		
	}
	
	public void activarModalAsignacionDocente(){
		if (ashafMateriaDto.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULAR_VALUE)) {
			ashafActivarModal = 0;
			FacesUtil.mensajeError("La Asignatura que intenta asignar docente es Modular, por favor realice la asignación en los Módulos.");
		}else {
			ashafActivarModal = 2;
		}
	}
	
	private void desactivarModalAsignacionAula() {
		ashafActivarModal = 0;
	}

	private void activarModalAsignacionAula() {
		ashafActivarModal = 1;
	}

	@SuppressWarnings("rawtypes")
	public void asignarAula(HoraClaseDto item){
		boolean cruceDocente = false;
		desactivarModalAsignacionAula();
		limpiarModalAsignarAulas();
		ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;

		cruceDocente = verificarCruceHorarioDocente(item);
		if (!cruceDocente) {
			Iterator it = ashafListHoraClaseDto.iterator();
			switch (item.getHracDia()) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclLunesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclLunesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclLunesHoraClaseDto().setHoclauId(item.getHoclauId());
						
						if (registrarHorarioAcademico(hocl.getHoclLunesHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclMartesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclMartesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclMartesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademico(hocl.getHoclMartesHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclMiercolesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclMiercolesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclMiercolesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademico(hocl.getHoclMiercolesHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclJuevesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclJuevesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclJuevesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademico(hocl.getHoclJuevesHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclViernesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclViernesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclViernesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademico(hocl.getHoclViernesHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}			
				}break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclSabadoHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclSabadoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclSabadoHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademico(hocl.getHoclSabadoHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclDomingoHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclDomingoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclDomingoHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademico(hocl.getHoclDomingoHoraClaseDto())) {
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}			
				}break;
			}

		}
	
	}
	
	public void asignarAulaNivelacion(){

		HoraClaseDto item = ashafHoraClaseDto;

//		boolean cruceDocente = false;
//		boolean cruceMismoParalelo = false;
		desactivarModalAsignacionAula();
		limpiarModalAsignarAulas();

//		cruceDocente = verificarCruceHorarioDocente(item);
//		cruceMismoParalelo = verificarCruceHorarioMateriasMismoParalelo();
//		if (!cruceDocente && !cruceMismoParalelo) {
			Iterator<HoraClaseDto> it = ashafListHoraClaseDto.iterator();
			switch (item.getHracDia()) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclLunesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclLunesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclLunesHoraClaseDto().setHoclauId(item.getHoclauId());

						if (registrarHorarioAcademicoPregrado(hocl.getHoclLunesHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclMartesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclMartesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclMartesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademicoPregrado(hocl.getHoclMartesHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclMiercolesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclMiercolesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclMiercolesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademicoPregrado(hocl.getHoclMiercolesHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclJuevesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclJuevesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclJuevesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademicoPregrado(hocl.getHoclJuevesHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclViernesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclViernesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclViernesHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademicoPregrado(hocl.getHoclViernesHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}			
				}break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclSabadoHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclSabadoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclSabadoHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademicoPregrado(hocl.getHoclSabadoHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
				while(it.hasNext()){
					HoraClaseDto hocl = (HoraClaseDto) it.next();
					if(hocl.getHoclDomingoHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
						hocl.getHoclDomingoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
						hocl.getHoclDomingoHoraClaseDto().setHoclauId(item.getHoclauId());
						if (registrarHorarioAcademicoPregrado(hocl.getHoclDomingoHoraClaseDto())) {
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							establecerSiPuedeSerCompartidoHorario();
							limpiarModalAsignarAulas();
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
						}
					}			
				}
				break;
			}

//		}

	}
	
	public void asignarAulaPregrado(){
		
		HoraClaseDto item = ashafHoraClaseDto;
		boolean continuar = false;
		if (ashafRenderTipoHoraClase) {
			
			boolean limiteHorasPorTipo = false;
			if (!ashafTipoHoraClaseId.equals(GeneralesConstantes.APP_ID_BASE)) {
				// verificar limite de horas por tipo
				limiteHorasPorTipo = verificarLimiteHorasPorSemanaPregradoPorTipoHora();
				if (!limiteHorasPorTipo) {
					continuar = true;
				}else {
					FacesUtil.mensajeError("Seleccione un Tipo de Hora diferente para poder asignar el Aula.");
				}
				
			}else {
				FacesUtil.mensajeError("Seleccione el tipo de hora para continuar.");
			}
			
		}else {
			continuar = true;
		}


		if (continuar) {
			boolean cruceDocente = false;
			boolean cruceMismoParalelo = false;
			desactivarModalAsignacionAula();
			limpiarModalAsignarAulas();

			cruceDocente = verificarCruceHorarioDocente(item);
			cruceMismoParalelo = verificarCruceHorarioMateriasMismoParalelo();
			if (!cruceDocente && !cruceMismoParalelo) {
				Iterator<HoraClaseDto> it = ashafListHoraClaseDto.iterator();
				switch (item.getHracDia()) {
				case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclLunesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclLunesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclLunesHoraClaseDto().setHoclauId(item.getHoclauId());

							if (registrarHorarioAcademicoPregrado(hocl.getHoclLunesHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}
					}
					break;
				case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclMartesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclMartesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclMartesHoraClaseDto().setHoclauId(item.getHoclauId());
							if (registrarHorarioAcademicoPregrado(hocl.getHoclMartesHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}
					}
					break;
				case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclMiercolesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclMiercolesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclMiercolesHoraClaseDto().setHoclauId(item.getHoclauId());
							if (registrarHorarioAcademicoPregrado(hocl.getHoclMiercolesHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}
					}
					break;
				case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclJuevesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclJuevesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclJuevesHoraClaseDto().setHoclauId(item.getHoclauId());
							if (registrarHorarioAcademicoPregrado(hocl.getHoclJuevesHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}
					}
					break;
				case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclViernesHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclViernesHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclViernesHoraClaseDto().setHoclauId(item.getHoclauId());
							if (registrarHorarioAcademicoPregrado(hocl.getHoclViernesHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}			
					}break;
				case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclSabadoHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclSabadoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclSabadoHoraClaseDto().setHoclauId(item.getHoclauId());
							if (registrarHorarioAcademicoPregrado(hocl.getHoclSabadoHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}
					}
					break;
				case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
					while(it.hasNext()){
						HoraClaseDto hocl = (HoraClaseDto) it.next();
						if(hocl.getHoclDomingoHoraClaseDto().getHoclHoraInicio().equals(item.getHoclHoraInicio())){
							hocl.getHoclDomingoHoraClaseDto().setHoclCheckBox(Boolean.TRUE);
							hocl.getHoclDomingoHoraClaseDto().setHoclauId(item.getHoclauId());
							if (registrarHorarioAcademicoPregrado(hocl.getHoclDomingoHoraClaseDto())) {
								ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
								establecerSiPuedeSerCompartidoHorario();
								limpiarModalAsignarAulas();
								FacesUtil.limpiarMensaje();
								FacesUtil.mensajeInfo("Horario Académico registrado con éxito.");
							}
						}			
					}
					break;
				}

			}
		}

	}
	
	
	private void cargarDependenciasParaCompartirHorarioPorUsuario(){
		
		try {
			List<DependenciaDto> dependencias = servJdbcDependenciaDto.buscarDependencias(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE);
			if (!dependencias.isEmpty()) {
				dependencias.sort(Comparator.comparing(DependenciaDto::getDpnDescripcion));
				ashafListCompartidaDependenciaDto = dependencias;
				iniciarModalCompartirHorario();
			}
		} catch (DependenciaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
	}
	
	private void cargarDependenciasParaCompartirHorarioPorUsuarioNivelacion(){
		
		try {
			List<DependenciaDto> dependencias = servJdbcDependenciaDto.buscarDependencias(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRENIVELACION_VALUE);
			if (!dependencias.isEmpty()) {
				dependencias.sort(Comparator.comparing(DependenciaDto::getDpnDescripcion));
				ashafListCompartidaDependenciaDto = dependencias;
				iniciarModalCompartirHorario();
			}
		} catch (DependenciaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void iniciarModalCompartirHorario(){
		ashafDependenciaCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafCarreraCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafMateriaCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafRenderGuardarCompartirHorario = Boolean.TRUE;
		
		ashafListCompartidaCarrera = null;
		ashafListCompartidaNivelDto = null;
		ashafListCompartidaParaleloDto = null;
		ashafListCompartidaMateriaDto = null;
	}
	
	private void limpiarModalCompartirHorarioDependendencia(){
		ashafCarreraCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafMateriaCompartidaId = GeneralesConstantes.APP_ID_BASE;
		
		ashafListCompartidaCarrera = null;
		ashafListCompartidaNivelDto = null;
		ashafListCompartidaParaleloDto = null;
		ashafListCompartidaMateriaDto = null;
	}
	
	private void limpiarModalCompartirHorarioCarrera(){
		ashafNivelCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafMateriaCompartidaId = GeneralesConstantes.APP_ID_BASE;
		
		ashafListCompartidaNivelDto = null;
		ashafListCompartidaParaleloDto = null;
		ashafListCompartidaMateriaDto = null;
	}
	
	
	private void limpiarModalCompartirHorarioNivel(){
		ashafParaleloCompartidaId = GeneralesConstantes.APP_ID_BASE;
		ashafMateriaCompartidaId = GeneralesConstantes.APP_ID_BASE;
		
		ashafListCompartidaParaleloDto = null;
		ashafListCompartidaMateriaDto = null;
	}
	
	private void limpiarModalCompartirHorarioParalelo(){
		ashafMateriaCompartidaId = GeneralesConstantes.APP_ID_BASE;
		
		ashafListCompartidaMateriaDto = null;
	}
	
	
	
	private void limpiarModalAsignarAulas() {
		ashafListEdificioDto = null;
		ashafListAulaDto = null;
		ashafListDisponibilidadHoraClaseDto = null;
		
		ashafDependenciaId = GeneralesConstantes.APP_ID_BASE;
		ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
		ashafAulaId = GeneralesConstantes.APP_ID_BASE;
		
		ashafRenderGuardarAula = Boolean.TRUE;
	}
	
	private void vaciarCombosDependenciaModalAulas() {
		ashafListEdificioDto = null;
		ashafListAulaDto = null;
		ashafListDisponibilidadHoraClaseDto = null;
		
		ashafEdificioId = GeneralesConstantes.APP_ID_BASE;
		ashafAulaId = GeneralesConstantes.APP_ID_BASE;
		ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
		
		ashafRenderGuardarAula = Boolean.TRUE;

	}
	
	private void vaciarCombosEdificioModalAulas() {
		ashafListAulaDto = null;
		ashafListDisponibilidadHoraClaseDto = null;
		
		ashafAulaId = GeneralesConstantes.APP_ID_BASE;
		ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
		
		ashafRenderGuardarAula = Boolean.TRUE;
	}
	
	private void vaciarCombosAulaModalAulas() {
		ashafListDisponibilidadHoraClaseDto = null;
		ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
		ashafRenderGuardarAula = Boolean.TRUE;
	}
	
	
	private void vaciarModalAsignarAulas() {
		ashafListDependenciaDto = null;
		ashafListEdificioDto = null;
		ashafListAulaDto = null;
		ashafListDisponibilidadHoraClaseDto = null;
		
		ashafDependenciaId = null;
		ashafEdificioId = null;
		ashafAulaId = null;
		ashafRenderGuardarAula = null;
	}
	
	
	
	
	
	
	
	private void limpiarFormMateriasPorParalelo() {
		ashafListMateriaDto = null;
		ashafListCarreraDto = null;
		ashafListNivelDto = null;
		ashafListParaleloDto = null;
		
		ashafPeriodoId = GeneralesConstantes.APP_ID_BASE;
		ashafAreaId = GeneralesConstantes.APP_ID_BASE;
		ashafCarreraId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloId = GeneralesConstantes.APP_ID_BASE;
	}
	
	private void iniciarFormListarMateriasPorParalelo(){
		ashafListMateriaDto = null;
		ashafListNivelDto = null;
		ashafListParaleloDto = null;
		ashafListCarreraDto = null;
		
		ashafPeriodoId = GeneralesConstantes.APP_ID_BASE;
		ashafAreaId = GeneralesConstantes.APP_ID_BASE;
		ashafCarreraId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloId = GeneralesConstantes.APP_ID_BASE;
	}

	private void vaciarFormMateriasPorParalelo() {
		ashafPeriodoId = null;
		ashafCarreraId= null;
		ashafNivelId= null;
		ashafParaleloId= null;
		ashafTipoBusqueda= null;
		ashafDependenciaId= null;
		ashafEdificioId= null;
		ashafAulaId= null;
		ashafHorasDocenciaPorSemanaMateria= null;
		ashafActivarModal= null;
		ashafDiaSeleccionado= null;
		ashafHoraSeleccionada= null;
		ashafIdentificacion= null;
		ashafPrimerApellido= null;
		ashafDiaLabel= null;
		
		ashafDocenteAsignado= null;
		ashafUsuario= null;
		ashafMateriaDto= null;
		ashafPersonaDto= null;
		ashafHoraClaseDto= null;
		ashafCargaHoraria= null;

		
		ashafListPeriodoAcademicoDto= null;
		ashafListCarrera= null;
		ashafListNivelDto= null;
		ashafListParaleloDto= null;
		ashafListMateriaDto= null;
		ashafListPersonaDto= null;
		ashafListDependenciaDto= null;
		ashafListEdificioDto= null;
		ashafListAulaDto= null;
		ashafListHoraClaseDto= null;
		ashafListDisponibilidadHoraClaseDto= null;
		ashafListDocenteHoraClaseDto= null;
		
	}
	
	
	
	private boolean registrarHorarioAcademico(HoraClaseDto hocl) {
		
		HorarioAcademico horario = new HorarioAcademico();
		horario.setHracMallaCurricularParalelo(new MallaCurricularParalelo(ashafMateriaDto.getMlcrprId()));
		horario.setHracHoraClaseAula(new HoraClaseAula(hocl.getHoclauId()));
		horario.setHracDia(ashafDiaSeleccionado);
		horario.setHracHoraInicio(Integer.valueOf(hocl.getHoclHoraInicio()));
		horario.setHracHoraFin(Integer.valueOf(hocl.getHoclHoraFin()));
		horario.setHracDescripcion(hocl.getHoclDescripcion());
		horario.setHracEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		
		boolean retorno = false;

		try {
			HorarioAcademico hrac = servHorarioAcademico.guardar(horario);
			if (hrac!=null && hrac.getHracId() != 0) {
				hocl.setHracId(hrac.getHracId());
				retorno = true;
				
				MallaCurricularParalelo malla = new MallaCurricularParalelo();
				malla.setMlcrprId(ashafMateriaDto.getMlcrprId());
				malla.setMlcrprCupo(calcularCupoMaximoPorAsignatura());
				try {
					servMallaCurricularParalelo.actualizarParaleloCupo(malla);
				} catch (MallaCurricularParaleloValidacionException e) {
				} catch (MallaCurricularParaleloNoEncontradoException e) {
				} catch (MallaCurricularParaleloException e) {
				}
			}
		} catch (HorarioAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	private boolean registrarHorarioAcademicoPregrado(HoraClaseDto hocl) {
		
		HorarioAcademico horario = new HorarioAcademico();
		horario.setHracMallaCurricularParalelo(new MallaCurricularParalelo(ashafMateriaDto.getMlcrprId()));
		horario.setHracHoraClaseAula(new HoraClaseAula(hocl.getHoclauId()));
		horario.setHracDia(ashafDiaSeleccionado);
		horario.setHracHoraInicio(Integer.valueOf(hocl.getHoclHoraInicio()));
		horario.setHracHoraFin(Integer.valueOf(hocl.getHoclHoraFin()));
		horario.setHracDescripcion(hocl.getHoclDescripcion());
		horario.setHracEstado(HorarioAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		horario.setHracHoraTipo(ashafTipoHoraClaseId);
		boolean retorno = false;

		try {
			HorarioAcademico hrac = servHorarioAcademico.guardar(horario);
			if (hrac!=null && hrac.getHracId() != 0) {
				hocl.setHracId(hrac.getHracId());
				retorno = true;
				
				MallaCurricularParalelo malla = new MallaCurricularParalelo();
				malla.setMlcrprId(ashafMateriaDto.getMlcrprId());
				malla.setMlcrprCupo(calcularCupoMaximoPorAsignatura());
				try {
					servMallaCurricularParalelo.actualizarParaleloCupo(malla);
				} catch (MallaCurricularParaleloValidacionException e) {
				} catch (MallaCurricularParaleloNoEncontradoException e) {
				} catch (MallaCurricularParaleloException e) {
				}
			}
		} catch (HorarioAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	private Integer calcularCupoMaximoPorAsignatura() {
		Integer retorno = Integer.valueOf(0);
		
		try {
			List<AulaDto> aulas =  servJdbcAulaDto.buscarAulasPorMateria(ashafParaleloId, ashafMateriaDto.getMlcrmtId());
			if (aulas != null && aulas.size() >0) {
				AulaDto aula = aulas.stream().min(Comparator.comparing(AulaDto::getAlaCapacidad)).get();
				retorno = aula.getAlaCapacidad();
			}
			
		} catch (AulaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (AulaDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Es necesario asignar horario a la materia para poder establecer el Cupo.");
		}
		return retorno;
	}



	private boolean eliminarHorarioAcademico(HoraClaseDto hocl) {
		boolean retorno = false;

		try {
			boolean eliminado = servHorarioAcademico.eliminar(hocl.getHracId());
			
			if (eliminado) {
				retorno = true;
				ashafRenderCompartirHorario = Boolean.FALSE;
				
				MallaCurricularParalelo malla = new MallaCurricularParalelo();
				malla.setMlcrprId(ashafMateriaDto.getMlcrprId());
				malla.setMlcrprCupo(calcularCupoMaximoPorAsignatura());
				try {
					servMallaCurricularParalelo.actualizarParaleloCupo(malla);
				} catch (MallaCurricularParaleloValidacionException e) {
				} catch (MallaCurricularParaleloNoEncontradoException e) {
				} catch (MallaCurricularParaleloException e) {
				}
			}
			
		} catch (HorarioAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	public void quitarAula(HoraClaseDto horario, int dia, String hora){
		desactivarModalAsignacionAula();
		
		if (verificarCronogramaRejusteHorario()) {
			if (!verificarSiExistenEstudiantesMatriculados()) {
				//quitar hora clase
				if (ashafEsHorarioCompartido) {
					// quitar principal y compartidos
					FacesUtil.mensajeError("El Horario es Compartido por ello no puede eliminar la Hora de Clase.");
				}else {
					// quitar hora clase
					if (eliminarHorarioAcademico(horario)) {
						horario.setHoclDescripcion("");
						horario.setHoclCheckBox(Boolean.FALSE);
						//						recargarTemplate(ahfPersonaDto);
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Horario modificado con éxito.");
					}
				}
			}else{
				if (cargarHorarioAsignadoPorAsignatura(ashafMateriaDto.getMlcrprId()).size() > 1) {
					// quitar hora clase
					if (ashafEsHorarioCompartido) {
						// quitar principal y compartidos
						FacesUtil.mensajeError("El Horario es Compartido por ello no puede eliminar la Hora de Clase.");
					}else {
						// quitar hora clase
						if (eliminarHorarioAcademico(horario)) {
							horario.setHoclDescripcion("");
							horario.setHoclCheckBox(Boolean.FALSE);
							//						recargarTemplate(ahfPersonaDto);
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario modificado con éxito.");
						}
					}
				}else {
					FacesUtil.mensajeError("No se puede eliminar todas las Horas de Clase, ya que existen estudiantes matriculados.");
				}
			}

		}
		
	}
	
	public void quitarAulaNivelacion(HoraClaseDto horario){
		desactivarModalAsignacionAula();
		
//		if (verificarCronogramaRejusteHorario()) {
			if (!verificarSiExistenEstudiantesMatriculados()) {
				//quitar hora clase
				if (ashafEsHorarioCompartido) {
					// quitar principal y compartidos
					FacesUtil.mensajeError("El Horario es Compartido por ello no puede eliminar la Hora de Clase.");
				}else {
					// quitar hora clase
					if (eliminarHorarioAcademico(horario)) {
						horario.setHoclDescripcion("");
						horario.setHoclCheckBox(Boolean.FALSE);
						//						recargarTemplate(ahfPersonaDto);
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Horario modificado con éxito.");
					}
				}
			}else{
				if (cargarHorarioAsignadoPorAsignatura(ashafMateriaDto.getMlcrprId()).size() > 1) {
					// quitar hora clase
					if (ashafEsHorarioCompartido) {
						// quitar principal y compartidos
						FacesUtil.mensajeError("El Horario es Compartido por ello no puede eliminar la Hora de Clase.");
					}else {
						// quitar hora clase
						if (eliminarHorarioAcademico(horario)) {
							horario.setHoclDescripcion("");
							horario.setHoclCheckBox(Boolean.FALSE);
							//						recargarTemplate(ahfPersonaDto);
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Horario modificado con éxito.");
						}
					}
				}else {
					FacesUtil.mensajeError("No se puede eliminar todas las Horas de Clase, ya que existen estudiantes matriculados.");
				}
			}

//		}
		
	}
	
	
	private boolean verificarCronogramaRejusteHorario() {
		boolean retorno = false;

		Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
		
		
		if (cargarTipoCarrera(ashafCarreraId).equals(CarreraConstantes.TIPO_PREGRADO_VALUE)) {
		
			try {
				PlanificacionCronogramaDto cronograma = servJdbcPlanificacionCronogramaDto.buscarPlanificacion(ashafPeriodoId, CronogramaConstantes.TIPO_ACADEMICO_VALUE, ProcesoFlujoEnum.REAJUSTE_HORARIO_CLASES.getValue());
				if(cronograma != null){
					if(cronograma.getPlcrFechaFin() != null && cronograma.getPlcrFechaInicio() != null){
						
						if(cronograma.getPlcrFechaFin().after(fechaActual)){
							if(cronograma.getPlcrFechaInicio().before(fechaActual)){ 
								retorno = true;
							}else{ 
								FacesUtil.mensajeError("El cronograma para reajuste de horarios no ha empezado."); 
							}
						}else{ 
							FacesUtil.mensajeError("El cronograma para reajuste de horarios ha expirado."); 
						}
					}else{
						FacesUtil.mensajeError("No se ha asignado cronograma para reajuste de horarios.");
					}
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError("No se ha asignado cronograma para reajuste de horarios.");
				}
			
			} catch (PlanificacionCronogramaValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PlanificacionCronogramaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (PlanificacionCronogramaException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else {
			retorno = true;
		}

		return retorno;
	}


	public String irNuevoHorarioSuficiencias() {
		return "irNuevoHorarioSuficiencias";
	}

	public String irHorariosSuficiencias(Usuario usuario) {
		try {
			ashafUsuario = usuario;
			
			ashafListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscarPeriodos(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			ashafListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRESUFICIENCIAS_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				ashafListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			ashafListCarrera.sort(Comparator.comparing(Carrera::getCrrDescripcion));
			
			iniciarFormListarMateriasPorParalelo();
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return "irHorarioMateriasSuficiencias";
	}
	
	public String irHorariosPregrado(Usuario usuario) {
		try {
			ashafUsuario = usuario;
			
//			ashafListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscarPeriodos(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			ashafListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,  PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE});
			PeriodoAcademicoDto retorno = new PeriodoAcademicoDto();
			retorno.setPracId(350);
			retorno.setPracDescripcion("2018-2019");
			retorno.setPracEstado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			retorno.setPracFechaIncio(null);
			retorno.setPracFechaFin(null);
			retorno.setPracTipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			ashafListPeriodoAcademicoDto.add(retorno);
			
			ashafListCarrera = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				ashafListCarrera.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			ashafListCarrera.sort(Comparator.comparing(Carrera::getCrrDescripcion));

			iniciarFormListarMateriasPorParalelo();
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return "irHorarioMateriasPregrado";
	}
	
	public String irHorariosNivelacion(Usuario usuario) {
		try {
			ashafUsuario = usuario;
			
//			ashafListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscarPeriodos(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRECARRERA_VALUE, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
			
			ashafListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE,  PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE});
			PeriodoAcademicoDto retorno = new PeriodoAcademicoDto();
			retorno.setPracId(350);
			retorno.setPracDescripcion("2018-2019");
			retorno.setPracEstado(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			retorno.setPracFechaIncio(null);
			retorno.setPracFechaFin(null);
			retorno.setPracTipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			ashafListPeriodoAcademicoDto.add(retorno);
			
			
			ashafListArea = new ArrayList<>();
			List<RolFlujoCarrera> rolflujocarrera = servRolFlujoCarrera.buscarXRolXUsuarioId(ashafUsuario.getUsrId(), RolConstantes.ROL_SECRENIVELACION_VALUE);
			for(RolFlujoCarrera rlflcr: rolflujocarrera){ 
				ashafListArea.add(servCarrera.buscarPorId(rlflcr.getRoflcrCarrera().getCrrId()));
			}
			
			iniciarFormListarMateriasPorParalelo();
		} catch (RolFlujoCarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return "irHorarioMateriasNivelacion";
	}
	
	
	
	public String irMateriasSuficiencias(){
		vaciarModalAsignarAulas();
		vaciarFormNuevoHorario();
//		limpiarFormMateriasPorParalelo();
		return "irHorariosSuficiencias";
	}
	
	public String irMateriasPregrado(){
		vaciarModalAsignarAulas();
		vaciarFormNuevoHorario();
		return "irHorariosPregrado";
	}
	
	public String irMateriasNivelacion(){
		vaciarModalAsignarAulas();
		vaciarFormNuevoHorario();
//		limpiarFormMateriasPorParalelo();
		return "irHorariosNivelacion";
	}
	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/

	private boolean verificarSiExistenEstudiantesMatriculados(){
		boolean retorno = false;
		try {
			List<RecordEstudianteDto> estudiantes = servJdbcParaleloDto.buscarMatriculados(ashafPeriodoId, ashafCarreraId, ashafParaleloId, ashafMateriaDto.getMlcrmtId());
			if (!estudiantes.isEmpty()) {
				retorno = true;
			}
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError("Aún no se encuentran estudiantes matriculados, el Horario de Clases puede ser modificado.");
			FacesUtil.limpiarMensaje();
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return retorno;
	}
	
	
	private void vaciarFormNuevoHorario() {
		ashafMateriaDto = null;
		ashafDocenteAsignado = null;
		ashafPersonaDto = null;
		ashafHoraClaseDto = null;
		
		ashafDiaSeleccionado = null;
		ashafHoraSeleccionada = null;
		ashafActivarModal = null;		
		
		ashafListHoraClaseDto = null;
		ashafListDisponibilidadHoraClaseDto = null;
		ashafListDocenteHoraClaseDto = null;
	}

	public void limpiarModalBusquedaDocente(){
		ashafIdentificacion = new String();
		ashafPrimerApellido = new String();
		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		ashafListPersonaDto = null; 
	}
	
	public void vaciarModalBusquedaDocente(){
		ashafIdentificacion = new String();
		ashafPrimerApellido = new String();
		ashafTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		ashafListPersonaDto = null; 
		ashafActivarModal = GeneralesConstantes.APP_ID_BASE;
	}
	
	public void vaciarModalCompartirHorario(){
		iniciarModalCompartirHorario();
	}
	
	public void vaciarModalBusquedaAula(){
		desactivarModalAsignacionAula();
		limpiarModalAsignarAulas();
	}
	
	public void buscarEdificios(){
		
		if (!ashafDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			try {
				vaciarCombosDependenciaModalAulas();
				ashafListEdificioDto = servJdbcEdificioDto.listarEdificiosPdpnId(ashafDependenciaId);
			} catch (EdificioDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EdificioDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}	
		}else {
			vaciarCombosDependenciaModalAulas();
			ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
		}
		
	}
	
	public void buscarAulas(){

		if (!ashafDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafEdificioId.equals(GeneralesConstantes.APP_ID_BASE)) {
				try {
					vaciarCombosEdificioModalAulas();
					ashafListAulaDto = servJdbcAulaDto.listarXUsuarioXDependenciaXEdificio(ashafUsuario.getUsrId(), ashafDependenciaId, ashafEdificioId);
				} catch (AulaDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (AulaDtoNoEncontradoException e) {
					vaciarCombosEdificioModalAulas();
					FacesUtil.mensajeError(e.getMessage());
				}
			}else {
				vaciarCombosEdificioModalAulas();
				FacesUtil.mensajeError("Seleccione un Edificio para continuar con la búsqueda.");
			}
		}else {
			vaciarCombosDependenciaModalAulas();
			ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
			FacesUtil.mensajeError("Seleccione una Dependencia para continuar con la búsqueda.");
		}

	}

	public void buscarDisponibilidad(){
		if (!ashafDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafEdificioId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!ashafAulaId.equals(GeneralesConstantes.APP_ID_BASE)) {
					try {
						vaciarCombosAulaModalAulas();
						List<HoraClaseDto> retorno = servHoraClaseDto.buscarDisponibilidad(ashafAulaId, ashafDiaSeleccionado, ashafHoraSeleccionada, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						if (!retorno.isEmpty()) {
							ashafListDisponibilidadHoraClaseDto = null;
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
							FacesUtil.mensajeError("El aula "+retorno.get(retorno.size()-1).getAlaDescripcion() + " se encuentra asignada al paralelo "+retorno.get(retorno.size()-1).getHracParaleloDto().getPrlCodigo()+".");
						}
					} catch (HoraClaseDtoException e) {
						FacesUtil.mensajeError("Error al buscar la disponibilidad del aula seleccionada.");
					} catch (HoraClaseDtoNoEncontradoException e) {
						ashafHoraClaseDto = cargarHoraClaseDto(ashafAulaId, ashafHoraSeleccionada);
						if (ashafHoraClaseDto != null) {
							ashafListDisponibilidadHoraClaseDto = new ArrayList<>();
							ashafHoraClaseDto.setHracDia(ashafDiaSeleccionado);
							ashafListDisponibilidadHoraClaseDto.add(ashafHoraClaseDto);
							ashafRenderGuardarAula = Boolean.FALSE;
						}
					}	
				}else {
					vaciarCombosAulaModalAulas();
					FacesUtil.mensajeError("Seleccione una Aula para continuar con la búsqueda.");
				}
			}else {
				vaciarCombosEdificioModalAulas();
				FacesUtil.mensajeError("Seleccione un Edificio para continuar con la búsqueda.");
			}
		}else {
			vaciarCombosDependenciaModalAulas();
			FacesUtil.mensajeError("Seleccione una Dependencia para continuar con la búsqueda.");
		}
	}
	
	
	/**
	 * Sin control de cruces.
	 */
	public void buscarDisponibilidadNivelacion(){
		if (!ashafDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafEdificioId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!ashafAulaId.equals(GeneralesConstantes.APP_ID_BASE)) {
					boolean aplica= true;
					
					try {
						vaciarCombosAulaModalAulas();
						servHoraClaseDto.buscarDisponibilidad(ashafAulaId, ashafDiaSeleccionado, ashafHoraSeleccionada, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
						ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
					} catch (HoraClaseDtoException e) {
						aplica = false;
						FacesUtil.mensajeError("Error al buscar la disponibilidad del aula seleccionada.");
					} catch (HoraClaseDtoNoEncontradoException e) {
					}	
					
					if (aplica) {
						ashafHoraClaseDto = cargarHoraClaseDto(ashafAulaId, ashafHoraSeleccionada);
						if (ashafHoraClaseDto != null) {
							ashafListDisponibilidadHoraClaseDto = new ArrayList<>();
							ashafHoraClaseDto.setHracDia(ashafDiaSeleccionado);
							ashafListDisponibilidadHoraClaseDto.add(ashafHoraClaseDto);
							ashafRenderGuardarAula = Boolean.FALSE;
							ashafTipoHoraClaseId = GeneralesConstantes.APP_ID_BASE;
						}	
					}
					
				}else {
					vaciarCombosAulaModalAulas();
					FacesUtil.mensajeError("Seleccione una Aula para continuar con la búsqueda.");
				}
			}else {
				vaciarCombosEdificioModalAulas();
				FacesUtil.mensajeError("Seleccione un Edificio para continuar con la búsqueda.");
			}
		}else {
			vaciarCombosDependenciaModalAulas();
			FacesUtil.mensajeError("Seleccione una Dependencia para continuar con la búsqueda.");
		}
	}
	
	
	private HoraClaseDto cargarHoraClaseDto(Integer aulaId, String horaInicio) {
		HoraClaseDto horario = null;
		try {
			horario =  servHoraClaseDto.buscarHoraClaseDto(aulaId, horaInicio);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError("Verifique que el Aula se encuentre en estado Activo.");
		} catch (HoraClaseDtoValidacionException e) {
			FacesUtil.mensajeError("Se encontró mas de un resultado con lo parámetros ingresados, comuníquese con el Administrador.");
		}
		return horario;
	}

	public void buscarDocentes() {
		String param = null;
		
		if (!ashafTipoBusqueda.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			if (ashafTipoBusqueda.intValue() == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = ashafIdentificacion;
			}else {
				param = ashafPrimerApellido;
			}

			
			List<PersonaDto>  docentes = cargarDocentesPorCarreraTipo(param, cargarTipoCarrera(ashafCarreraId), ashafTipoBusqueda);
			if (docentes != null && docentes.size() > 0) {
				ashafListPersonaDto = docentes;
			}else {
				ashafListPersonaDto = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del docente para continuar con la búsqueda.");
		}
	}
	
	public void buscarDocentesNivelacion() {
		String param = null;
		
		if (!ashafTipoBusqueda.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			if (ashafTipoBusqueda.intValue() == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = ashafIdentificacion;
			}else {
				param = ashafPrimerApellido;
			}

			
			List<PersonaDto>  docentes = cargarDocentesPorCarreraTipo(param, cargarTipoCarrera(ashafAreaId), ashafTipoBusqueda);
			if (docentes != null && docentes.size() > 0) {
				ashafListPersonaDto = docentes;
			}else {
				ashafListPersonaDto = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del docente para continuar con la búsqueda.");
		}
	}
	
	
	private Integer cargarTipoCarrera(Integer carreraId) {
		Carrera carrera = null;
		try {
			carrera = servCarrera.buscarPorId(carreraId);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return carrera.getCrrTipo()!= null ? carrera.getCrrTipo() : GeneralesConstantes.APP_ID_BASE;
	}


	public void buscarAreasNivelacion(){

		limpiarCombosPeriodo();
		
		if (!ashafPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			FacesUtil.limpiarMensaje();
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}

	private void limpiarCombosPeriodo(){
		ashafListMateriaDto = null;
		ashafListCarreraDto = null;
		ashafListNivelDto = null;
		ashafListParaleloDto = null;
		
		ashafAreaId = GeneralesConstantes.APP_ID_BASE;
		ashafCarreraId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloId = GeneralesConstantes.APP_ID_BASE;
	}
	
	public void buscarCarrerasPorArea(){

		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			limpiarCombosArea();
			
			if (ashafAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				
				try {
					 ashafListCarreraDto = servJdbcCarreraDto.buscarCarrerasPorArea(ashafAreaId);
				} catch (CarreraDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());	
				} catch (CarreraDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}

			}else {
				FacesUtil.mensajeError("Seleccione un área para continuar.");
			}
			
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
			
	}
	
	private void limpiarCombosArea(){
		ashafCarreraId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		ashafListNivelDto = null;
		ashafListParaleloDto = null;
		ashafListMateriaDto = null;
		ashafListCarreraDto = null;
	}
	

	public void buscarNiveles(){
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				try {
					vaciarCombosNivel();
					ashafListNivelDto = servJdbcNivelDto.listarNivelXCarrera(ashafCarreraId);
				} catch (NivelDtoJdbcException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (NivelDtoJdbcNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
			}else {
				vaciarCombosNivel();
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	
	public void buscarNivelesPorArea(){
		
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			if (ashafAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				
				limpiarCombosCarrera();
				
				if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					
					try {
						ashafListNivelDto = servJdbcNivelDto.listarNivelXCarrera(ashafAreaId);
					} catch (NivelDtoJdbcException e) {
						FacesUtil.mensajeError(e.getMessage());
					} catch (NivelDtoJdbcNoEncontradoException e) {
						FacesUtil.mensajeError(e.getMessage());
					}
					
				}else {
					FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				}
				
			}else {
				limpiarCombosArea();
				FacesUtil.mensajeError("Seleccione un área para continuar.");
			}
			
		}else {
			limpiarCombosPeriodo();
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
		
	}
	
	
	private void limpiarCombosCarrera() {

		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		
		ashafListNivelDto = null;
		ashafListParaleloDto = null;
		ashafListMateriaDto = null;
	}


	private void vaciarCombosNivel() {
		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		ashafParaleloId = GeneralesConstantes.APP_ID_BASE;
		ashafListMateriaDto = null;
		ashafListNivelDto = null;
		ashafListParaleloDto = null;
	}



	public void buscarParalelos(){
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (ashafNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					vaciarCombosParalelo();
						List<ParaleloDto> paralelos = cargarParalelosPorPeriodoCarreraNivel();
						if (!paralelos.isEmpty()) {
							paralelos.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
							ashafListParaleloDto = paralelos;
						}
				} else {
					vaciarCombosParalelo();
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	public void buscarParalelosNivelacion(){
		
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			if (ashafAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				
				if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					
					limpiarCombosNivel();
					if (ashafNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						
						List<ParaleloDto> paralelos = cargarParalelosPorPeriodoCarreraNivelNivelacion();
						if (!paralelos.isEmpty()) {
							paralelos.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
							ashafListParaleloDto = paralelos;
						}
					} else {
						FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					}
					
				}else {
					limpiarCombosCarrera();
					FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				}
			}else {
				limpiarCombosArea();
				FacesUtil.mensajeError("Seleccione un área para continuar.");
			}
		}else {
			limpiarCombosPeriodo();
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	

	private void vaciarCombosParalelo() {
		ashafParaleloId = GeneralesConstantes.APP_ID_BASE;
		ashafListMateriaDto = null;
		ashafListParaleloDto = null;
	}



	public void buscarMaterias(){
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (ashafNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (ashafParaleloId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						ashafListMateriaDto = cargarMateriasPorParalelo(ashafParaleloId);
					}else {
						FacesUtil.mensajeError("Seleccione un paralelo para continuar.");
					}
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	public void buscarMateriasPregrado(){
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (ashafNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (ashafParaleloId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						List<MateriaDto> materias = cargarMateriasPorParalelo(ashafParaleloId);
						if (!materias.isEmpty()) {
							Iterator<MateriaDto> iter = materias.iterator();
							while (iter.hasNext()) {
								MateriaDto modulo = (MateriaDto) iter.next();
								if (modulo.getMtrTpmtId().equals(TipoMateriaConstantes.TIPO_MODULO_VALUE)) {
									modulo.setMtrDescripcion(establecerCodigoModular(modulo.getMtrSubId(), materias, modulo));
								}
							}
							materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo).thenComparing(Comparator.comparing(MateriaDto::getMtrTpmtId).thenComparing(Comparator.comparing(MateriaDto::getMtrDescripcion))));
							ashafListMateriaDto = materias;
						}
						
					}else {
						FacesUtil.mensajeError("Seleccione un paralelo para continuar.");
					}
				} else {
					FacesUtil.mensajeError("Seleccione un nivel para continuar.");
				}
			}else {
				FacesUtil.mensajeError("Seleccione una carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	public void buscarMateriasNivelacion(){
		
		if (ashafPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			
			if (ashafAreaId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				
				if (ashafCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					
					if (ashafNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
						
						limpiarCombosParalelo();
						
						if (ashafParaleloId.intValue() != GeneralesConstantes.APP_ID_BASE) {
							
							List<MateriaDto> materias = cargarMateriasPorParalelo(ashafParaleloId);
							if (!materias.isEmpty()) {
								materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo).thenComparing(Comparator.comparing(MateriaDto::getMtrTpmtId).thenComparing(Comparator.comparing(MateriaDto::getMtrDescripcion))));
								ashafListMateriaDto = materias;
							}

						}else {
							FacesUtil.mensajeError("Seleccione un paralelo para continuar.");
						}
						
					} else {
						limpiarCombosNivel();
						FacesUtil.mensajeError("Seleccione un nivel para continuar.");
					}
				}else {
					limpiarCombosCarrera();
					FacesUtil.mensajeError("Seleccione una carrera para continuar.");
				}
			}else {
				limpiarCombosArea();
				FacesUtil.mensajeError("Seleccione un área para continuar.");
			}
		}else {
			limpiarCombosPeriodo();
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");
		}
	}
	
	
	
	
	private void limpiarCombosParalelo() {
		ashafListMateriaDto = null;
	}


	private void limpiarCombosNivel() {
		ashafParaleloId = GeneralesConstantes.APP_ID_BASE;
		ashafListParaleloDto = null;
		ashafListMateriaDto = null;
	}


	private String establecerCodigoModular(int mtrPrincipal, List<MateriaDto> materias, MateriaDto modulo) {
		String retorno = "";
		
		Iterator<MateriaDto> iter = materias.iterator();
		while (iter.hasNext()) {
			MateriaDto materia = (MateriaDto) iter.next();
			if (materia.getMtrId() == mtrPrincipal) {
				modulo.setMtrCodigo(materia.getMtrCodigo());
				retorno = materia.getMtrDescripcion() + " / "+ modulo.getMtrDescripcion();
				break;
			}
		}
		
		return retorno;
	}



	public void limpiarFormMaterias(){
		ashafPeriodoId = GeneralesConstantes.APP_ID_BASE;
		ashafAreaId = GeneralesConstantes.APP_ID_BASE;
		ashafCarreraId = GeneralesConstantes.APP_ID_BASE;
		ashafNivelId = GeneralesConstantes.APP_ID_BASE;
		ashafListCarrera = null;
		ashafListParaleloDto = null;
		ashafListNivelDto = null;
		ashafListMateriaDto = null;
	}
	
	private List<ParaleloDto> cargarParalelosPorPeriodoCarreraNivel(){
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelos(ashafPeriodoId,ashafCarreraId,ashafNivelId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<ParaleloDto> cargarParalelosPorPeriodoCarreraNivelNivelacion(){
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelosPorAreaCarrera(ashafPeriodoId,ashafAreaId, ashafCarreraId, ashafNivelId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<ParaleloDto> cargarParalelosAcompartirPorPeriodoCarreraNivel(){
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelos(ashafPeriodoId,ashafCarreraCompartidaId,ashafNivelCompartidaId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private List<MateriaDto> cargarMateriasPorParalelo(int paraleloId){
		List<MateriaDto> retorno =  new ArrayList<>();
		
		try {
			retorno = servJdbcMateriaDto.buscarMateriasDocentePorParalelo(paraleloId);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Materias vinculadas al Paralelo seleccionado.");
		}
		
		return retorno;
	}
	
	private List<HoraClaseDto> cargarHorarioAsignadoPorAsignatura(int mlcrprId){
		List<HoraClaseDto>  retorno = new ArrayList<>();
			try {
				retorno = servHoraClaseDto.buscarHorarioAcademico(mlcrprId);
			} catch (HoraClaseDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (HoraClaseDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		return retorno;
	}
	
	private List<PersonaDto> cargarDocentesPorCarreraTipo(String param, Integer crrTipo, Integer tipoBusqueda) {
		List<PersonaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcPersonaDto.buscarDocentesPorCarrera(crrTipo, tipoBusqueda, param);
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Docentes vinculados a la Carrera del paralelo seleccionado.");
		}
		
		return retorno;
	}
	

	public void asignarDocente(PersonaDto item){
		boolean disponibilidad = false;
		
		ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(item.getPrsId(), ashafPeriodoId);
		if (!ashafListDocenteHoraClaseDto.isEmpty()) {
			disponibilidad = verificarCruceHorarioClases(ashafListDocenteHoraClaseDto);
			if (disponibilidad) {
				ashafPersonaDto = item;
				ashafMateriaDto.setPrsId(item.getPrsId());
				ashafMateriaDto.setPrsIdentificacion(item.getPrsIdentificacion());
				ashafMateriaDto.setPrsPrimerApellido(item.getPrsPrimerApellido());
				ashafMateriaDto.setPrsSegundoApellido(item.getPrsSegundoApellido());
				ashafMateriaDto.setPrsNombres(item.getPrsNombres());
				boolean registro = registrarCargaHoraria(item);
				if (registro) {
					ashafDocenteAsignado = Boolean.TRUE;
					ashafCargaHoraria = cargarCargaHorariaPorDocente(ashafMateriaDto.getMlcrprId());
					establecerSiPuedeSerCompartidoHorario();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Carga Horaria asignada con éxito.");
				}
			}else {
				item = null;
			}
		}else {
			ashafPersonaDto = item;
			ashafMateriaDto.setPrsId(item.getPrsId());
			ashafMateriaDto.setPrsIdentificacion(item.getPrsIdentificacion());
			ashafMateriaDto.setPrsPrimerApellido(item.getPrsPrimerApellido());
			ashafMateriaDto.setPrsSegundoApellido(item.getPrsSegundoApellido());
			ashafMateriaDto.setPrsNombres(item.getPrsNombres());
			boolean registro = registrarCargaHoraria(item);
			if (registro) {
				ashafDocenteAsignado = Boolean.TRUE;
				ashafCargaHoraria = cargarCargaHorariaPorDocente(ashafMateriaDto.getMlcrprId());
				establecerSiPuedeSerCompartidoHorario();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Carga Horaria asignada con éxito.");
			}
		}
		
		ashafActivarModal = GeneralesConstantes.APP_ID_BASE;
		
	}
	
	public void asignarDocenteSuficiencias(PersonaDto item){
		boolean disponibilidad = false;
		
		ashafListDocenteHoraClaseDto = cargarCargaHorariaPorDocente(item.getPrsId(), ashafPeriodoId);
		if (!ashafListDocenteHoraClaseDto.isEmpty()) {
			disponibilidad = verificarCruceHorarioClases(ashafListDocenteHoraClaseDto);
			if (disponibilidad) {
				ashafPersonaDto = item;
				ashafMateriaDto.setPrsId(item.getPrsId());
				ashafMateriaDto.setPrsIdentificacion(item.getPrsIdentificacion());
				ashafMateriaDto.setPrsPrimerApellido(item.getPrsPrimerApellido());
				ashafMateriaDto.setPrsSegundoApellido(item.getPrsSegundoApellido());
				ashafMateriaDto.setPrsNombres(item.getPrsNombres());
				boolean registro = registrarCargaHoraria(item);
				if (registro) {
					ashafDocenteAsignado = Boolean.TRUE;
					ashafCargaHoraria = cargarCargaHorariaPorDocente(ashafMateriaDto.getMlcrprId());
					establecerSiPuedeSerCompartidoHorario();
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Carga Horaria asignada con éxito.");
				}
			}else {
				item = null;
			}
		}else {
			ashafPersonaDto = item;
			ashafMateriaDto.setPrsId(item.getPrsId());
			ashafMateriaDto.setPrsIdentificacion(item.getPrsIdentificacion());
			ashafMateriaDto.setPrsPrimerApellido(item.getPrsPrimerApellido());
			ashafMateriaDto.setPrsSegundoApellido(item.getPrsSegundoApellido());
			ashafMateriaDto.setPrsNombres(item.getPrsNombres());
			boolean registro = registrarCargaHoraria(item);
			if (registro) {
				ashafDocenteAsignado = Boolean.TRUE;
				ashafCargaHoraria = cargarCargaHorariaPorDocente(ashafMateriaDto.getMlcrprId());
				establecerSiPuedeSerCompartidoHorario();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Carga Horaria asignada con éxito.");
			}
		}
		
		ashafActivarModal = GeneralesConstantes.APP_ID_BASE;
		
	}

	
	public void asignarDocenteNivelacion(PersonaDto item){

		ashafPersonaDto = item;
		ashafMateriaDto.setPrsId(item.getPrsId());
		ashafMateriaDto.setPrsIdentificacion(item.getPrsIdentificacion());
		ashafMateriaDto.setPrsPrimerApellido(item.getPrsPrimerApellido());
		ashafMateriaDto.setPrsSegundoApellido(item.getPrsSegundoApellido());
		ashafMateriaDto.setPrsNombres(item.getPrsNombres());
		
		boolean registro = registrarCargaHoraria(item);
		if (registro) {
			ashafDocenteAsignado = Boolean.TRUE;
			ashafCargaHoraria = cargarCargaHorariaPorDocente(ashafMateriaDto.getMlcrprId());
			establecerSiPuedeSerCompartidoHorario();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Carga Horaria asignada con éxito.");
		}

		ashafActivarModal = GeneralesConstantes.APP_ID_BASE;

	}

	public void desactivarCargaHoraria(){
		
		if (!ashafEsHorarioCompartido) {
			try {

				ashafCargaHoraria.setCrhrEstado(DetallePuestoConstantes.ESTADO_INACTIVO_VALUE);
				ashafCargaHoraria.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_ACTIVO_VALUE);
				
				if (servCargaHoraria.editarPorHorarioAcademico(ashafCargaHoraria)) {
					ashafMateriaDto.setPrsId(0);
					ashafMateriaDto.setPrsIdentificacion("");
					ashafMateriaDto.setPrsPrimerApellido("");
					ashafMateriaDto.setPrsSegundoApellido("");
					ashafMateriaDto.setPrsNombres("");
					FacesUtil.mensajeInfo("Carga Horaria desactivada con éxito.");
					ashafDocenteAsignado = Boolean.FALSE;
					establecerSiPuedeSerCompartidoHorario();
				}
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CargaHorariaNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}

		}else {
			FacesUtil.mensajeError("El Horario de Clases es Compartido por ello no puede eliminar al Docente.");
		}
		
	}
	
	
	/**
	 * DetallePuesto :
	 *  - buscar detalle puesto activo
	 *  - si se encuentra en este periodo -> guardar cahr
	 *  - caso contrario -> crear dtps 
	 *  		- si es periodo en cierre -> crear dtps inactivo
	 *  		- si es periodo activo -> crear dtps activo y desactivar los demas dtps
	 *  
	 */
	private boolean registrarCargaHoraria(PersonaDto item) {
		boolean retorno = false;
		
		CargaHoraria carga = new CargaHoraria();
		MallaCurricularParalelo mlcrpr = new MallaCurricularParalelo(ashafMateriaDto.getMlcrprId());
		PeriodoAcademico periodo = new PeriodoAcademico(ashafPeriodoId);
		
		carga.setCrhrObservacion("HORAS CLASE");
		carga.setCrhrNumHoras(ashafHorasDocenciaPorSemanaMateria);
		carga.setCrhrEstado(CargaHorariaConstantes.ESTADO_ACTIVO_VALUE);
		carga.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_INACTIVO_VALUE);
		carga.setCrhrMallaCurricularParalelo(mlcrpr);
		carga.setCrhrPeriodoAcademico(periodo);
		
		try {
			
			DetallePuesto dtpsActivo = cargarDetallePuestoActivo(item.getDtpsId());
			if (dtpsActivo.getDtpsPracId().equals(ashafPeriodoId)) {
				
				carga.setCrhrDetallePuesto(dtpsActivo);
				if (servCargaHoraria.anadir(carga).getCrhrId() != 0) {
					retorno = true;	
				}
				
			}else {
				
				DetallePuesto dtpsPeriodoSolicitado = null;
				
				try {
					
					dtpsPeriodoSolicitado = servDetallePuesto.buscarDetallePuesto(ashafPersonaDto.getPrsId(), ashafPeriodoId);
					
					carga.setCrhrDetallePuesto(dtpsPeriodoSolicitado);
					if (servCargaHoraria.anadir(carga).getCrhrId() != 0) {
						retorno = true;	
					}
					
				} catch (DetallePuestoNoEncontradoException e) {
					// si no encuentra resultados crear el detalle puesto en ese periodo.
					
					try {
						DetallePuesto dtpsNuevo = new DetallePuesto();
						
						dtpsNuevo.setDtpsFichaDocente(dtpsActivo.getDtpsFichaDocente());
						dtpsNuevo.setDtpsPuesto(dtpsActivo.getDtpsPuesto());
						dtpsNuevo.setDtpsRelacionLaboral(dtpsActivo.getDtpsRelacionLaboral());
						dtpsNuevo.setDtpsCarrera(dtpsActivo.getDtpsCarrera()); 
						dtpsNuevo.setDtpsPracId(ashafPeriodoId);
						
						dtpsNuevo.setDtpsTipoCarrera(cargarTipoCarrera(ashafCarreraId));
						dtpsNuevo.setDtpsFechaRegistro(Timestamp.from(Instant.now()));
						dtpsNuevo.setDtpsUsuario(ashafUsuario.getUsrNick());
						dtpsNuevo.setDtpsProcesoRegistro(DetallePuestoConstantes.TIPO_PROCESO_REGISTRO_HORARIO_VALUE);
						
						if (verificarSiPeriodoAcademicoActivo()) {
							dtpsNuevo.setDtpsEstado(DetallePuestoConstantes.ESTADO_ACTIVO_VALUE);
							desactivarDetallePuestoPeriodosAnteriores(dtpsActivo.getDtpsFichaDocente().getFcdcId(), dtpsNuevo.getDtpsTipoCarrera());
						}else {
							dtpsNuevo.setDtpsEstado(DetallePuestoConstantes.ESTADO_INACTIVO_VALUE);
						}
						
						dtpsPeriodoSolicitado = servDetallePuesto.guardar(dtpsNuevo);
						
						carga.setCrhrDetallePuesto(dtpsPeriodoSolicitado);
						if (servCargaHoraria.anadir(carga).getCrhrId() != 0) {
							retorno = true;	
						}
						
					} catch (DetallePuestoValidacionException e1) {
						FacesUtil.mensajeError(e1.getMessage());
					} catch (DetallePuestoException e1) {
						FacesUtil.mensajeError(e1.getMessage());
					}
					
				} catch (DetallePuestoValidacionException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (DetallePuestoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
			}
			
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	private void desactivarDetallePuestoPeriodosAnteriores(int fcdcId, Integer dtpsTipoCarrera) {
		try {
			servDetallePuesto.desactivarDetallePuesto(fcdcId, dtpsTipoCarrera);
		} catch (DetallePuestoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}		
	}


	private boolean verificarSiPeriodoAcademicoActivo() {
		boolean retorno = false;
		
		try {
			PeriodoAcademico periodo =servPeriodoAcademico.buscarPorId(ashafPeriodoId); 
			if (periodo != null) {
				if (periodo.getPracEstado().equals(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE)) {
					retorno = true;
				}
			}
		} catch (PeriodoAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}


	private DetallePuesto cargarDetallePuestoActivo(int dtpsId) {
		DetallePuesto retorno = null;
		
		try {
			retorno = servDetallePuesto.buscarPorId(dtpsId);
		} catch (DetallePuestoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DetallePuestoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	private boolean verificarCruceHorarioClases(List<HoraClaseDto> horario) {
		
		for (HoraClaseDto item: horario) {
			switch (item.getHracDia().intValue()) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				for (HoraClaseDto item1 : ashafListHoraClaseDto) {
					System.out.println(item1.getHoclLunesHoraClaseDto().getHoclPosicionX()+" "+ item1.getHoclLunesHoraClaseDto().getHoclPosicionY());

					if (item1.getHoclLunesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclLunesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_LUNES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				for (HoraClaseDto item1 : ashafListHoraClaseDto) {
					if (item1.getHoclMartesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclMartesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_MARTES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}				
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				for (HoraClaseDto item1 : ashafListHoraClaseDto) {
					if (item1.getHoclMiercolesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclMiercolesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				for (HoraClaseDto item1 : ashafListHoraClaseDto) {
					if (item1.getHoclJuevesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclJuevesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_JUEVES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				for (HoraClaseDto item1 : ashafListHoraClaseDto) {
					if (item1.getHoclViernesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclViernesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_VIERNES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				for (HoraClaseDto item1 : ashafListHoraClaseDto) {
					if (item1.getHoclSabadoHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclSabadoHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_SABADO_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			}
		}
		
		return true;
	}

	private List<HoraClaseDto> cargarCargaHorariaPorDocente(int prsId, int periodoId){
		List<HoraClaseDto> retorno = new ArrayList<>();
		
		try {
			retorno = servHoraClaseDto.buscarCargaHoraria(prsId, periodoId);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Carga Horaria asignada al Docente seleccionado.");
		}
		
		return retorno;
	}
	

	
	public void busquedaPorIdentificacion(){
		
		if (ashafIdentificacion.length() > 0) {
			ashafPrimerApellido = new String();
			ashafTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (ashafPrimerApellido.length() > 0) {
			ashafIdentificacion = new String();
			ashafTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	
	//	COMPARTIDA
	
	public void buscarCarrerasAcompartir() {
		
		if (!ashafDependenciaCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			limpiarModalCompartirHorarioDependendencia();
			List<Carrera> carreras = cargarCarrerasPorDependencia();	
			if (!carreras.isEmpty()) {
				carreras.sort(Comparator.comparing(Carrera::getCrrDescripcion));
				ashafListCompartidaCarrera = carreras;
			}
		}else {
			limpiarModalCompartirHorarioDependendencia();
			FacesUtil.mensajeError("Seleccione una Facultad para continuar.");
		}
	}
	
	
	private List<Carrera> cargarCarrerasPorDependencia() {
		List<Carrera> retorno = new ArrayList<>();

		try {
			retorno = servCarrera.buscarCarreras(ashafDependenciaCompartidaId, CarreraConstantes.TIPO_PREGRADO_VALUE);
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}



	public void buscarNivelesAcompartir() {
		if (!ashafDependenciaCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafCarreraCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				limpiarModalCompartirHorarioCarrera();;
				List<NivelDto> niveles = cargarNivelesPorCarrera();
				if (!niveles.isEmpty()) {
					niveles.sort(Comparator.comparing(NivelDto::getNvlNumeral));
					ashafListCompartidaNivelDto = niveles; 
				}
			}else {
				limpiarModalCompartirHorarioCarrera();;
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar.");
		}
	}
	
	private List<NivelDto> cargarNivelesPorCarrera() {
		List<NivelDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcNivelDto.listarNivelXCarrera(ashafCarreraCompartidaId);
		} catch (NivelDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	
	public void buscarParalelosAcompartir() {
		if (!ashafDependenciaCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafCarreraCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!ashafNivelCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
					limpiarModalCompartirHorarioNivel();
					List<ParaleloDto> paralelos = cargarParalelosAcompartirPorPeriodoCarreraNivel();
					if (!paralelos.isEmpty()) {
						paralelos.sort(Comparator.comparing(ParaleloDto::getPrlCodigo));
						ashafListCompartidaParaleloDto = paralelos; 
					}
				}else {
					limpiarModalCompartirHorarioNivel();
					FacesUtil.mensajeError("Seleccione un Nivel para continuar.");	
				}
			}else {
				limpiarModalCompartirHorarioCarrera();;
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar.");
		}
	}


	public void buscarMateriaAcompartir() {
		if (!ashafDependenciaCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafCarreraCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!ashafNivelCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
					if (!ashafParaleloCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
						List<MateriaDto> materias= cargarMateriasPorParalelo(ashafParaleloCompartidaId);
						if (!materias.isEmpty()) {
							materias.sort(Comparator.comparing(MateriaDto::getMtrCodigo));
							ashafListCompartidaMateriaDto = materias; 
						}
					}else {
						limpiarModalCompartirHorarioParalelo();
						FacesUtil.mensajeError("Seleccione un Paralelo para continuar.");
					}
				}else {
					limpiarModalCompartirHorarioNivel();
					FacesUtil.mensajeError("Seleccione un Nivel para continuar.");	
				}
			}else {
				limpiarModalCompartirHorarioCarrera();;
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar.");
		}

	}
	
	public void verificarCompatibilidad() {

		if (!ashafDependenciaCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!ashafCarreraCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!ashafNivelCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
					if (!ashafParaleloCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
						if (!ashafMateriaCompartidaId.equals(GeneralesConstantes.APP_ID_BASE)) {
							List<HoraClaseDto> horas = cargarHorarioAsignadoPorPeriodoParaleloAsignatura(ashafPeriodoId, ashafParaleloCompartidaId, ashafMateriaCompartidaId);
							if (horas.isEmpty()) {
								// buscar Carga Horaria 
								List<HoraClaseDto> cargaHoraria = cargarCargaHorariaPorPeriodoParaleloMateria(ashafPeriodoId, ashafParaleloCompartidaId, ashafMateriaCompartidaId);
								if (cargaHoraria.isEmpty()) {
									try {
										Materia materia  = servMateria.buscarPorId(ashafMateriaCompartidaId);
										if (ashafHorasPorSemanaMateria.equals(establecerLimiteHorasPorSemanaPregrado(materia))) {
											ashafMateriaAcompartir = materia;
											if (!verificarCruceHorarioMateriasParaleloAcompartir()) {
												ashafRenderGuardarCompartirHorario = Boolean.FALSE;
												FacesUtil.limpiarMensaje();
												FacesUtil.mensajeInfo("El Horario de Clases puede ser compartido a la Asignatura selecciona...");	
											}
										}else {
											ashafRenderGuardarCompartirHorario = Boolean.TRUE;
											FacesUtil.limpiarMensaje();
											FacesUtil.mensajeInfo("No es posible Compartir el Horario a la Asignatura seleccionada porque el total de horas/créditos semanales no son iguales.");
										}
									} catch (MateriaNoEncontradoException e) {
										FacesUtil.mensajeError(e.getMessage());
									} catch (MateriaException e) {
										FacesUtil.mensajeError(e.getMessage());
									}
								}else {
									FacesUtil.mensajeError("No es posible Compartir el Horario a la Asignatura seleccionada porque ya tiene Docente asiganado.");	
								}
							}else {
								FacesUtil.mensajeError("No es posible Compartir el Horario a la Asignatura seleccionada porque ya tiene Aulas asiganadas al Horario de Clases.");	
							}
						} else {
							ashafRenderGuardarCompartirHorario = Boolean.TRUE;
							FacesUtil.mensajeError("Seleccione una Asignatura para continuar.");
						}
					}else {
						limpiarModalCompartirHorarioParalelo();
						FacesUtil.mensajeError("Seleccione un Paralelo para continuar.");
					}
				}else {
					limpiarModalCompartirHorarioNivel();
					FacesUtil.mensajeError("Seleccione un Nivel para continuar.");	
				}
			}else {
				limpiarModalCompartirHorarioCarrera();;
				FacesUtil.mensajeError("Seleccione una Carrera para continuar.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione una Facultad para continuar.");
		}

	}
	
	private List<HoraClaseDto> cargarCargaHorariaPorPeriodoParaleloMateria(Integer periodoId, Integer paraleloId, Integer materiaId) {
		List<HoraClaseDto> retorno = new ArrayList<>();
		
		try {
			retorno = servHoraClaseDto.buscarCargaHoraria(periodoId, paraleloId, materiaId);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	private List<HoraClaseDto> cargarHorarioAsignadoPorPeriodoParaleloAsignatura(int periodoId,	int paraleloId, int materiaId) {
		List<HoraClaseDto> retorno = new ArrayList<>();
		
		try {
			retorno = servHoraClaseDto.buscarHorarioAcademicoPorPeriodoParaleloAsignatura(periodoId, paraleloId, materiaId);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		
		return retorno;
	}

	
	private boolean verificarCruceHorarioMateriasParaleloAcompartir() {
		boolean retorno = false;

		List<HoraClaseDto> horarioMateriaPrincipal = cargarHorarioAsignadoPorAsignatura(ashafMateriaDto.getMlcrprId());
		if (!horarioMateriaPrincipal.isEmpty()) {

			if (!retorno) {

				List<MateriaDto> materiasParaleloAcompartir = cargarMateriasPorParalelo(ashafParaleloCompartidaId);
				for (MateriaDto materia : materiasParaleloAcompartir) {

					List<HoraClaseDto> horariosPorMateria = cargarHorarioAsignadoPorAsignatura(materia.getMlcrprId());

					if (!horariosPorMateria.isEmpty()) {
						if (!retorno) {
							for (HoraClaseDto item : horariosPorMateria) {
								if (!retorno) {
									for (HoraClaseDto itemAcompartir : horarioMateriaPrincipal) {
										if (item.getHracDia().equals(itemAcompartir.getHracDia()) && item.getHoclHoraInicio().equals(itemAcompartir.getHoclHoraInicio())) {
											retorno = true;
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (retorno) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se puede compartir porque genera Cruce con una Asignatura del Paralelo al que se quiere Compartir.");
		}

		return retorno;
	}


	public void asignarDocenteAulasCompartidas() {
		try {
			MateriaDto materia = servJdbcMateriaDto.buscarMateriaPorParaleloMateria(ashafParaleloCompartidaId, ashafMateriaCompartidaId);
			boolean retorno = servHorarioAcademico.compartir(ashafMateriaDto.getMlcrprId(),materia.getMlcrprId());
			if (retorno) {
				iniciarModalCompartirHorario();
				FacesUtil.mensajeInfo("El Horario de Clases y Docente fue compartido con éxito.");
			}
		} catch (HorarioAcademicoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HorarioAcademicoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CargaHorariaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public void quitarDocenteAulasCompartidas(){
		
		if (!verificarSiExistenEstudiantesMatriculados()) {
			try {

				ashafCargaHoraria.setCrhrEstado(DetallePuestoConstantes.ESTADO_INACTIVO_VALUE);
				ashafCargaHoraria.setCrhrEstadoEliminacion(CargaHorariaConstantes.ESTADO_ELIMINADO_ACTIVO_VALUE);
				
				if (servCargaHoraria.editar(ashafCargaHoraria)) {
					ashafMateriaDto.setPrsId(0);
					ashafMateriaDto.setPrsIdentificacion("");
					ashafMateriaDto.setPrsPrimerApellido("");
					ashafMateriaDto.setPrsSegundoApellido("");
					ashafMateriaDto.setPrsNombres("");
					ashafMateriaDto.setCahrMlcrprIdPrincipal(0);
					FacesUtil.mensajeInfo("Carga Horaria desactivada con éxito.");
					
					ashafDocenteAsignado = Boolean.FALSE;
					ashafRenderCompartirHorario = Boolean.FALSE;
					ashafEsHorarioCompartido = Boolean.FALSE;
				}
				
				List<HoraClaseDto> horarios = cargarHorarioAsignadoPorAsignatura(ashafMateriaDto.getMlcrprId());
				if (!horarios.isEmpty()) {
					for (HoraClaseDto item : horarios) {
						quitarAulaCompartida(item, item.getHracDia(), item.getHoclHoraInicio());
						FacesUtil.limpiarMensaje();
					}
				}
				
			} catch (CargaHorariaValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CargaHorariaException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else {
			FacesUtil.mensajeError("No se puede eliminar el Horario Compartido porque existen estudiantes matriculados.");
		}
		
	}
	
	public void quitarAulaCompartida(HoraClaseDto horario, int dia, String hora){

		Iterator<HoraClaseDto> it = ashafListHoraClaseDto.iterator();
		switch (dia) {
		case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclLunesHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclLunesHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclLunesHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclMartesHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclMartesHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclMartesHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclMiercolesHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclMiercolesHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclMiercolesHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclJuevesHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclJuevesHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclJuevesHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclViernesHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclViernesHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclViernesHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}			
			}break;
		case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclSabadoHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclSabadoHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclSabadoHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}
			}
			break;
		case HorarioAcademicoConstantes.DIA_DOMINGO_VALUE:
			while(it.hasNext()){
				HoraClaseDto hocl = (HoraClaseDto) it.next();
				if(hocl.getHoclDomingoHoraClaseDto().getHoclHoraInicio().equals(hora)){
					hocl.getHoclDomingoHoraClaseDto().setHoclCheckBox(Boolean.FALSE);
					if (eliminarHorarioAcademico(hocl.getHoclDomingoHoraClaseDto())) {
						FacesUtil.mensajeInfo("Horario Académico eliminado con éxito.");
					}
				}
			}
			break;
		}
	}
	

	// ********************************************************************/
	// *********************** METODOS ENCAPSULACION **********************/
	// ********************************************************************/

	public Usuario getAshafUsuario() {
		return ashafUsuario;
	}

	public void setAshafUsuario(Usuario ashafUsuario) {
		this.ashafUsuario = ashafUsuario;
	}

	public Integer getAshafPeriodoId() {
		return ashafPeriodoId;
	}

	public void setAshafPeriodoId(Integer ashafPeriodoId) {
		this.ashafPeriodoId = ashafPeriodoId;
	}

	public Integer getAshafCarreraId() {
		return ashafCarreraId;
	}

	public void setAshafCarreraId(Integer ashafCarreraId) {
		this.ashafCarreraId = ashafCarreraId;
	}

	public Integer getAshafNivelId() {
		return ashafNivelId;
	}

	public void setAshafNivelId(Integer ashafNivelId) {
		this.ashafNivelId = ashafNivelId;
	}

	public Integer getAshafParaleloId() {
		return ashafParaleloId;
	}

	public void setAshafParaleloId(Integer ashafParaleloId) {
		this.ashafParaleloId = ashafParaleloId;
	}

	public List<PeriodoAcademicoDto> getAshafListPeriodoAcademicoDto() {
		return ashafListPeriodoAcademicoDto;
	}

	public void setAshafListPeriodoAcademicoDto(List<PeriodoAcademicoDto> ashafListPeriodoAcademicoDto) {
		this.ashafListPeriodoAcademicoDto = ashafListPeriodoAcademicoDto;
	}

	public List<NivelDto> getAshafListNivelDto() {
		return ashafListNivelDto;
	}

	public void setAshafListNivelDto(List<NivelDto> ashafListNivelDto) {
		this.ashafListNivelDto = ashafListNivelDto;
	}

	public List<ParaleloDto> getAshafListParaleloDto() {
		return ashafListParaleloDto;
	}

	public void setAshafListParaleloDto(List<ParaleloDto> ashafListParaleloDto) {
		this.ashafListParaleloDto = ashafListParaleloDto;
	}

	public List<MateriaDto> getAshafListMateriaDto() {
		return ashafListMateriaDto;
	}

	public void setAshafListMateriaDto(List<MateriaDto> ashafListMateriaDto) {
		this.ashafListMateriaDto = ashafListMateriaDto;
	}

	public List<Carrera> getAshafListCarrera() {
		return ashafListCarrera;
	}

	public void setAshafListCarrera(List<Carrera> ashafListCarrera) {
		this.ashafListCarrera = ashafListCarrera;
	}

	public MateriaDto getAshafMateriaDto() {
		return ashafMateriaDto;
	}

	public void setAshafMateriaDto(MateriaDto ashafMateriaDto) {
		this.ashafMateriaDto = ashafMateriaDto;
	}

	public List<HoraClaseDto> getAshafListHoraClaseDto() {
		return ashafListHoraClaseDto;
	}

	public void setAshafListHoraClaseDto(List<HoraClaseDto> ashafListHoraClaseDto) {
		this.ashafListHoraClaseDto = ashafListHoraClaseDto;
	}

	public Integer getAshafTipoBusqueda() {
		return ashafTipoBusqueda;
	}

	public void setAshafTipoBusqueda(Integer ashafTipoBusqueda) {
		this.ashafTipoBusqueda = ashafTipoBusqueda;
	}

	public String getAshafIdentificacion() {
		return ashafIdentificacion;
	}

	public void setAshafIdentificacion(String ashafIdentificacion) {
		this.ashafIdentificacion = ashafIdentificacion;
	}

	public String getAshafPrimerApellido() {
		return ashafPrimerApellido;
	}

	public void setAshafPrimerApellido(String ashafPrimerApellido) {
		this.ashafPrimerApellido = ashafPrimerApellido;
	}

	public List<PersonaDto> getAshafListPersonaDto() {
		return ashafListPersonaDto;
	}

	public void setAshafListPersonaDto(List<PersonaDto> ashafListPersonaDto) {
		this.ashafListPersonaDto = ashafListPersonaDto;
	}

	public PersonaDto getAshafPersonaDto() {
		return ashafPersonaDto;
	}

	public void setAshafPersonaDto(PersonaDto ashafPersonaDto) {
		this.ashafPersonaDto = ashafPersonaDto;
	}

	public Integer getAshafDependenciaId() {
		return ashafDependenciaId;
	}

	public void setAshafDependenciaId(Integer ashafDependenciaId) {
		this.ashafDependenciaId = ashafDependenciaId;
	}

	public List<DependenciaDto> getAshafListDependenciaDto() {
		return ashafListDependenciaDto;
	}

	public void setAshafListDependenciaDto(List<DependenciaDto> ashafListDependenciaDto) {
		this.ashafListDependenciaDto = ashafListDependenciaDto;
	}

	public Integer getAshafEdificioId() {
		return ashafEdificioId;
	}

	public void setAshafEdificioId(Integer ashafEdificioId) {
		this.ashafEdificioId = ashafEdificioId;
	}

	public List<EdificioDto> getAshafListEdificioDto() {
		return ashafListEdificioDto;
	}

	public void setAshafListEdificioDto(List<EdificioDto> ashafListEdificioDto) {
		this.ashafListEdificioDto = ashafListEdificioDto;
	}

	public List<AulaDto> getAshafListAulaDto() {
		return ashafListAulaDto;
	}

	public void setAshafListAulaDto(List<AulaDto> ashafListAulaDto) {
		this.ashafListAulaDto = ashafListAulaDto;
	}

	public String getAshafDiaLabel() {
		return ashafDiaLabel;
	}

	public void setAshafDiaLabel(String ashafDiaLabel) {
		this.ashafDiaLabel = ashafDiaLabel;
	}

	public Integer getAshafAulaId() {
		return ashafAulaId;
	}

	public void setAshafAulaId(Integer ashafAulaId) {
		this.ashafAulaId = ashafAulaId;
	}

	public List<HoraClaseDto> getAshafListDisponibilidadHoraClaseDto() {
		return ashafListDisponibilidadHoraClaseDto;
	}

	public void setAshafListDisponibilidadHoraClaseDto(List<HoraClaseDto> ashafListDisponibilidadHoraClaseDto) {
		this.ashafListDisponibilidadHoraClaseDto = ashafListDisponibilidadHoraClaseDto;
	}


	public Integer getAshafActivarModal() {
		return ashafActivarModal;
	}

	public void setAshafActivarModal(Integer ashafActivarModal) {
		this.ashafActivarModal = ashafActivarModal;
	}

	public Integer getAshafDiaSeleccionado() {
		return ashafDiaSeleccionado;
	}

	public void setAshafDiaSeleccionado(Integer ashafDiaSeleccionado) {
		this.ashafDiaSeleccionado = ashafDiaSeleccionado;
	}

	public String getAshafHoraSeleccionada() {
		return ashafHoraSeleccionada;
	}

	public void setAshafHoraSeleccionada(String ashafHoraSeleccionada) {
		this.ashafHoraSeleccionada = ashafHoraSeleccionada;
	}

	public List<HoraClaseDto> getAshafListDocenteHoraClaseDto() {
		return ashafListDocenteHoraClaseDto;
	}

	public void setAshafListDocenteHoraClaseDto(List<HoraClaseDto> ashafListDocenteHoraClaseDto) {
		this.ashafListDocenteHoraClaseDto = ashafListDocenteHoraClaseDto;
	}

	public HoraClaseDto getAshafHoraClaseDto() {
		return ashafHoraClaseDto;
	}

	public void setAshafHoraClaseDto(HoraClaseDto ashafHoraClaseDto) {
		this.ashafHoraClaseDto = ashafHoraClaseDto;
	}

	public Boolean getAshafDocenteAsignado() {
		return ashafDocenteAsignado;
	}

	public void setAshafDocenteAsignado(Boolean ashafDocenteAsignado) {
		this.ashafDocenteAsignado = ashafDocenteAsignado;
	}

	public Integer getAshafTipoHoraClaseId() {
		return ashafTipoHoraClaseId;
	}

	public void setAshafTipoHoraClaseId(Integer ashafTipoHoraClaseId) {
		this.ashafTipoHoraClaseId = ashafTipoHoraClaseId;
	}



	public Integer getAshafHorasDocenciaPorSemanaMateria() {
		return ashafHorasDocenciaPorSemanaMateria;
	}



	public void setAshafHorasDocenciaPorSemanaMateria(Integer ashafHorasDocenciaPorSemanaMateria) {
		this.ashafHorasDocenciaPorSemanaMateria = ashafHorasDocenciaPorSemanaMateria;
	}



	public Integer getAshafHorasPracticaPorSemanaMateria() {
		return ashafHorasPracticaPorSemanaMateria;
	}



	public void setAshafHorasPracticaPorSemanaMateria(Integer ashafHorasPracticaPorSemanaMateria) {
		this.ashafHorasPracticaPorSemanaMateria = ashafHorasPracticaPorSemanaMateria;
	}



	public Integer getAshafHorasPorSemanaMateria() {
		return ashafHorasPorSemanaMateria;
	}

	public void setAshafHorasPorSemanaMateria(Integer ashafHorasPorSemanaMateria) {
		this.ashafHorasPorSemanaMateria = ashafHorasPorSemanaMateria;
	}

	public CargaHoraria getAshafCargaHoraria() {
		return ashafCargaHoraria;
	}

	public void setAshafCargaHoraria(CargaHoraria ashafCargaHoraria) {
		this.ashafCargaHoraria = ashafCargaHoraria;
	}

	public List<SelectItem> getAshafListTipoHoraClase() {
		return ashafListTipoHoraClase;
	}

	public void setAshafListTipoHoraClase(List<SelectItem> ashafListTipoHoraClase) {
		this.ashafListTipoHoraClase = ashafListTipoHoraClase;
	}

	public Boolean getAshafRenderTipoHoraClase() {
		return ashafRenderTipoHoraClase;
	}

	public void setAshafRenderTipoHoraClase(Boolean ashafRenderTipoHoraClase) {
		this.ashafRenderTipoHoraClase = ashafRenderTipoHoraClase;
	}



	public Integer getAshafCarreraCompartidaId() {
		return ashafCarreraCompartidaId;
	}



	public void setAshafCarreraCompartidaId(Integer ashafCarreraCompartidaId) {
		this.ashafCarreraCompartidaId = ashafCarreraCompartidaId;
	}



	public Integer getAshafNivelCompartidaId() {
		return ashafNivelCompartidaId;
	}



	public void setAshafNivelCompartidaId(Integer ashafNivelCompartidaId) {
		this.ashafNivelCompartidaId = ashafNivelCompartidaId;
	}


	public Integer getAshafMateriaCompartidaId() {
		return ashafMateriaCompartidaId;
	}



	public void setAshafMateriaCompartidaId(Integer ashafMateriaCompartidaId) {
		this.ashafMateriaCompartidaId = ashafMateriaCompartidaId;
	}



	public Integer getAshafDependenciaCompartidaId() {
		return ashafDependenciaCompartidaId;
	}



	public void setAshafDependenciaCompartidaId(Integer ashafDependenciaCompartidaId) {
		this.ashafDependenciaCompartidaId = ashafDependenciaCompartidaId;
	}



	public List<Carrera> getAshafListCompartidaCarrera() {
		return ashafListCompartidaCarrera;
	}



	public void setAshafListCompartidaCarrera(List<Carrera> ashafListCompartidaCarrera) {
		this.ashafListCompartidaCarrera = ashafListCompartidaCarrera;
	}





	public List<ParaleloDto> getAshafListCompartidaParaleloDto() {
		return ashafListCompartidaParaleloDto;
	}



	public void setAshafListCompartidaParaleloDto(List<ParaleloDto> ashafListCompartidaParaleloDto) {
		this.ashafListCompartidaParaleloDto = ashafListCompartidaParaleloDto;
	}



	public List<MateriaDto> getAshafListCompartidaMateriaDto() {
		return ashafListCompartidaMateriaDto;
	}



	public void setAshafListCompartidaMateriaDto(List<MateriaDto> ashafListCompartidaMateriaDto) {
		this.ashafListCompartidaMateriaDto = ashafListCompartidaMateriaDto;
	}



	public List<DependenciaDto> getAshafListCompartidaDependenciaDto() {
		return ashafListCompartidaDependenciaDto;
	}



	public void setAshafListCompartidaDependenciaDto(List<DependenciaDto> ashafListCompartidaDependenciaDto) {
		this.ashafListCompartidaDependenciaDto = ashafListCompartidaDependenciaDto;
	}



	public Integer getAshafParaleloCompartidaId() {
		return ashafParaleloCompartidaId;
	}



	public void setAshafParaleloCompartidaId(Integer ashafParaleloCompartidaId) {
		this.ashafParaleloCompartidaId = ashafParaleloCompartidaId;
	}



	public List<NivelDto> getAshafListCompartidaNivelDto() {
		return ashafListCompartidaNivelDto;
	}



	public void setAshafListCompartidaNivelDto(List<NivelDto> ashafListCompartidaNivelDto) {
		this.ashafListCompartidaNivelDto = ashafListCompartidaNivelDto;
	}



	public Boolean getAshafRenderCompartirHorario() {
		return ashafRenderCompartirHorario;
	}



	public void setAshafRenderCompartirHorario(Boolean ashafRenderCompartirHorario) {
		this.ashafRenderCompartirHorario = ashafRenderCompartirHorario;
	}



	public Materia getAshafMateriaAcompartir() {
		return ashafMateriaAcompartir;
	}



	public void setAshafMateriaAcompartir(Materia ashafMateriaAcompartir) {
		this.ashafMateriaAcompartir = ashafMateriaAcompartir;
	}



	public Boolean getAshafEsHorarioCompartido() {
		return ashafEsHorarioCompartido;
	}



	public void setAshafEsHorarioCompartido(Boolean ashafEsHorarioCompartido) {
		this.ashafEsHorarioCompartido = ashafEsHorarioCompartido;
	}


	public Boolean getAshafRenderGuardarCompartirHorario() {
		return ashafRenderGuardarCompartirHorario;
	}


	public void setAshafRenderGuardarCompartirHorario(Boolean ashafRenderGuardarCompartirHorario) {
		this.ashafRenderGuardarCompartirHorario = ashafRenderGuardarCompartirHorario;
	}


	public Boolean getAshafRenderGuardarAula() {
		return ashafRenderGuardarAula;
	}


	public void setAshafRenderGuardarAula(Boolean ashafRenderGuardarAula) {
		this.ashafRenderGuardarAula = ashafRenderGuardarAula;
	}


	public Integer getAshafAreaId() {
		return ashafAreaId;
	}


	public void setAshafAreaId(Integer ashafAreaId) {
		this.ashafAreaId = ashafAreaId;
	}


	public List<Carrera> getAshafListArea() {
		return ashafListArea;
	}


	public void setAshafListArea(List<Carrera> ashafListArea) {
		this.ashafListArea = ashafListArea;
	}


	public List<CarreraDto> getAshafListCarreraDto() {
		return ashafListCarreraDto;
	}


	public void setAshafListCarreraDto(List<CarreraDto> ashafListCarreraDto) {
		this.ashafListCarreraDto = ashafListCarreraDto;
	}

	
	
	

}

