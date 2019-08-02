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
   
 ARCHIVO:     AdministracionSuficienciaExoneracionForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para la administración  del proceso Planificacion Cronogramas de las Suficiencias.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
22-FEB-2018				 FREDDY GUZMÁN						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSuficiencia.informatica;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
import ec.edu.uce.academico.ejb.dtos.ModalidadDto;
import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AulaException;
import ec.edu.uce.academico.ejb.excepciones.AulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AulaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CargaHorariaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.HorarioAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaCurricularParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.ParaleloServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.AulaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.DependenciaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EdificioDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HoraClaseDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.NivelDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.ParaleloDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.AulaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.HorarioAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ModalidadConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ParaleloConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) AdministracionSuficienciaExoneracionForm.
 * Managed Bean que maneja las peticiones para la administración de Exoneraciones.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name="administracionSuficienciaExoneracionForm")
@SessionScoped
public class AdministracionSuficienciaExoneracionForm implements Serializable{

	private static final int HORA_CLASE_INICIO_JORNADA_VALUE = 7;
	private static final int CUPO_MINIMO_VALUE = 1;
	private static final long serialVersionUID = -3934424476360393698L;
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	
	private String asefAulaLabel;
	private String asefDocenteLabel;
	private String asefIdentificacion;
	private String asefPrimerApellido;
	
	private Integer asefPeriodoId;
	private Integer asefCarreraId;
	private Integer asefNivelId;
	private Integer asefHoraId;
	private Integer asefDependenciaId;
	private Integer asefEdificioId;
	private Integer asefAulaId;
	private Integer asefEstadoId;
	private Integer asefActivarModal;
	private Integer asefCupo;
	private Integer asefTipoBusqueda;
	
	private Boolean asefDisabledModalidad;
	private Boolean asefDocenteAsignado;
	private Boolean asefAulaAsignada;
	
	private Date asefFechaHoraParalelo;
	private String asefCodigoParalelo;

	private Usuario asefUsuario;
	private Carrera asefCarrera;
	private CargaHoraria asefCargaHoraria;
	private LocalDateTime asefFechaParalelo;
	
	private PeriodoAcademicoDto asefPeriodoAcademicoDto;	
	private NivelDto asefNivelDto;
	private AulaDto asefAulaDto;
	private ParaleloDto asefParaleloDto;
	private MateriaDto asefMateriaDto;
	private PersonaDto asefDocentePersonaDto;
	
	
	private List<PeriodoAcademicoDto> asefListPeriodoAcademicoDto;
	private List<CarreraDto> asefListCarreraDto;
	private List<NivelDto> asefListNivelDto;
	private List<ModalidadDto> asefListModalidadDto;
	private List<ParaleloDto> asefListParaleloDto;
	private List<MateriaDto> asefListMateriaDto;
	private List<AulaDto> asefListAulaDto;
	private List<CarreraDto> asefListAreaDto;
	private List<HoraClaseDto> asefListHoraClaseDto;
	private List<DependenciaDto> asefListDependenciaDto;
	private List<EdificioDto> asefListEdificioDto;
	private List<PersonaDto> asefListPersonaDto;
	private List<RecordEstudianteDto> asefListRecordEstudianteDto;
	
	private List<Carrera> asefListCarrera;
	private List<SelectItem> asefListEstados;
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarrera;
	@EJB private CarreraServicio servCarrera;
	@EJB private ParaleloServicio servParalelo;
	@EJB private DependenciaServicio servDependencia;
	@EJB private PeriodoAcademicoServicio servPeriodoAcademico;
	@EJB private MallaCurricularParaleloServicio servMallaCurricularParalelo;
	@EJB private NivelServicio servNivel;
	@EJB private HorarioAcademicoServicio servHorarioAcademico;	
	@EJB private CargaHorariaServicio servCargaHoraria;
	@EJB private MateriaServicio servMateria;
	@EJB private AulaServicio servAula;
	
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
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurricularMateriaDto;
	@EJB private MatriculaServicioJdbc servJdbcMatricula;

	
	// ********************************************************************/
	// ******************* METODOS DE NAVEGACION **************************/
	// ********************************************************************/
	
	
	public String irInicio(){
		vaciarModalAulas();
		vaciarModalDocentes();
		return "irInicio"; 	
	}
	
	
	public String irParalelosExoneraciones(Usuario usuario) {
		String retorno = null;

		try {
			asefUsuario = usuario;
			asefPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(PeriodoAcademicoConstantes.PRAC_PERIODO_SUFICIENCIA_INFORMATICA_EXONERACIONES_VALUE);
			asefCarrera = servCarrera.buscarPorId(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE);
			asefNivelDto = servJdbcNivelDto.buscarXId(NivelConstantes.NIVEL_PRIMERO_VALUE);
			
			List<MateriaDto> materias = servJdbcMateriaDto.listarMateriasxCarreraFull(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
			if (materias!= null && materias.size()>0) {
				asefMateriaDto = materias.get(materias.size()-1);
			}

			iniciarFormParalelos();
			retorno = "irParalelosExoneraciones";

		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MateriaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (NivelDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	
	
	public String irFormNuevoParalelo() {
		
		iniciarFormNuevoParalelo();
		iniciarModalAulas();
		iniciarModalAsignarDocente();
		
		return "irNuevoParaleloExoneraciones";
	}
	
	
	public String irParalelosExoneraciones(){
		vaciarFormNuevoParalelo();
		return "irParalelosExoneraciones";
	}
	
	
	public String irVerParaleloExoneraciones(ParaleloDto paralelo){
		asefParaleloDto = paralelo;
		
		try {
			asefListRecordEstudianteDto =	servJdbcMatricula.buscarEstudiantesMatriculados(paralelo.getPrlMallaCurricularParaleloDto().getMlcrprId());
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError("El paralelo seleccionado aún no dispone de Estudiantes matrículados.");
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return "irVerParaleloExoneraciones";
	}
	
	public String irEditarParaleloExoneraciones(){
		asefEstadoId = asefParaleloDto.getPrlEstado();
		asefListEstados = cargarEstadoParalelos();
		asefCupo = null;
		return "irEditarParaleloExoneraciones";
	}
	
	public String irVerParalelo(){

		return "irVerParaleloExoneraciones";
	}
	
	public void guardarNuevoEstadoParaleloExoneraciones(){
		try {
			asefParaleloDto.setPrlEstado(asefEstadoId);
			if (servParalelo.editarParalelo(asefParaleloDto)) {
				FacesUtil.mensajeInfo("El estado del Paralelo fue actualizado con éxito.");
			}
		} catch (ParaleloNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró el Paralelo para realizar cambio de estado.");
		} catch (ParaleloException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public void guardarNuevoCupoParaleloExoneraciones(){
		if (asefCupo != null && asefCupo >= CUPO_MINIMO_VALUE) {
			try {
				servMallaCurricularParalelo.editarCupoPorMlcrprId(asefParaleloDto.getPrlMallaCurricularParaleloDto().getMlcrprId(), asefCupo);
				asefParaleloDto.getPrlMallaCurricularParaleloDto().setMlcrprCupo(asefCupo);
				FacesUtil.mensajeInfo("Cupo actualizado con éxito.");
			} catch (MallaCurricularParaleloNoEncontradoException e) {
				FacesUtil.mensajeError("Error al actualizar el Nuevo Cupo.");
			} catch (MallaCurricularParaleloException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
		}else{
			FacesUtil.mensajeError("Debe ingresar un Cupo mínimo de Uno.");
		}
	}
	
	
	
	private  void iniciarFormParalelos(){
		
		asefListPeriodoAcademicoDto = new ArrayList<>();
		asefListPeriodoAcademicoDto.add(asefPeriodoAcademicoDto);
		
		asefListCarrera = new ArrayList<>();
		asefListCarrera.add(asefCarrera);
		
		asefListNivelDto = new ArrayList<>();
		asefListNivelDto.add(asefNivelDto);
		
		asefPeriodoId = asefPeriodoAcademicoDto.getPracId();
		asefCarreraId = asefCarrera.getCrrId();
		asefNivelId = asefNivelDto.getNvlNumeral();
		
		asefListParaleloDto = null;
		asefFechaParalelo = null;
		asefFechaHoraParalelo = null;
	}
	
	private  void iniciarFormNuevoParalelo(){
		
		asefDocenteAsignado = Boolean.FALSE;
		asefAulaAsignada = Boolean.FALSE;
		
		asefParaleloDto = null;
		asefDocentePersonaDto = null;
		
		asefFechaParalelo = null;
		asefFechaHoraParalelo = null;
		asefCupo = null;
		asefCodigoParalelo = null;
		asefAulaLabel = null;
		asefDocenteLabel = null;
		asefListHoraClaseDto = new ArrayList<>();
		
		desactivarModalAsignacionAula();
		
	}
	
	
	private void iniciarModalAulas(){
		asefListDependenciaDto = cargarDependencias(asefUsuario);
		asefListEdificioDto = null;
		asefListAulaDto = null;
		
		asefDependenciaId = GeneralesConstantes.APP_ID_BASE;
		asefEdificioId = GeneralesConstantes.APP_ID_BASE;
		asefAulaId = GeneralesConstantes.APP_ID_BASE;
		desactivarModalAsignacionAula();
	}
	
	private void iniciarModalAsignarDocente(){
		asefIdentificacion = new String();
		asefPrimerApellido = new String();
		asefTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		asefListPersonaDto = null; 
	}
	
	
	
	private void vaciarModalAulas() {
		asefListDependenciaDto = null;
		asefListEdificioDto = null;
		asefListAulaDto = null;
		
		asefDependenciaId = null;
		asefEdificioId = null;
		asefAulaId = null;
	}
	
	private void vaciarModalDocentes() {
		asefListPersonaDto = null;
		asefTipoBusqueda = null;
		asefPrimerApellido = null;
		asefIdentificacion = null;
		
	}
	
	private  void vaciarFormNuevoParalelo(){
		
		asefDocenteAsignado = null;
		asefAulaAsignada = null;
		
		asefParaleloDto = null;
		asefDocentePersonaDto = null;
		
		asefFechaParalelo = null;
		asefFechaHoraParalelo = null;
		asefCupo = null;
		asefCodigoParalelo = null;
		asefAulaLabel = null;
		asefDocenteLabel = null;
		asefDocenteAsignado = null;
		asefListHoraClaseDto = new ArrayList<>();
		asefListRecordEstudianteDto = null;
		desactivarModalAsignacionAula();
		
	}
	
	
	

	public void limpiarFormParalelos() {
		asefFechaHoraParalelo = null;
		asefListParaleloDto = null;
	}
	
	private void limpiarCombosModalAulasDependencia() {
		asefListEdificioDto = null;
		asefListAulaDto = null;
		
		asefEdificioId = GeneralesConstantes.APP_ID_BASE;
		asefAulaId = GeneralesConstantes.APP_ID_BASE;
	}
	
	private void limpiarCombosModalAulasEdificio() {
		asefListAulaDto = null;
		asefAulaId = GeneralesConstantes.APP_ID_BASE;
	}
	
	private void limpiarCombosModalAulasAula() {
	}
	
	
	
	
	
	
	
	
	private List<DependenciaDto> cargarDependencias(Usuario usuario){
		List<DependenciaDto>  retorno = new ArrayList<>();
	
		try {
			retorno = servJdbcDependenciaDto.listarXUsuario(usuario.getUsrId());
		} catch (DependenciaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
		
	}
	
	
	private List<SelectItem> cargarEstadoParalelos(){
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(ParaleloConstantes.ESTADO_ACTIVO_VALUE, ParaleloConstantes.ESTADO_ACTIVO_LABEL));
		retorno.add(new SelectItem(ParaleloConstantes.ESTADO_INACTIVO_VALUE, ParaleloConstantes.ESTADO_INACTIVO_LABEL));
		return retorno;
	}
	
	
	
	
	
	
	public void buscarParalelosExoneracion() {
		if (asefPeriodoId.intValue() != GeneralesConstantes.APP_ID_BASE) {
			if (asefCarreraId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				if (asefNivelId.intValue() != GeneralesConstantes.APP_ID_BASE) {
					if (asefFechaHoraParalelo != null) {
						List<ParaleloDto> auxListParaleloDto = cargarParalelosPorFechaInicial();
						auxListParaleloDto.sort(Comparator.comparing(ParaleloDto::getPrlFecha));
						asefListParaleloDto = auxListParaleloDto;						
					}else {
						FacesUtil.mensajeError("Seleccione una fecha para continuar.");	
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
	
	private List<ParaleloDto> cargarParalelosPorFechaInicial() {
		List<ParaleloDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcParaleloDto.buscarParalelos(asefPeriodoId, asefFechaHoraParalelo);
		} catch (ParaleloDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (ParaleloDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	public void buscarEdificios(){
		
		if (!asefDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			limpiarCombosModalAulasDependencia();
			
			try {
				asefListEdificioDto = servJdbcEdificioDto.listarEdificiosPdpnId(asefDependenciaId);
			} catch (EdificioDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (EdificioDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			}
			
		}else {
			limpiarCombosModalAulasDependencia();
		}
		
	}
	
	public void buscarAulas(){

		if (!asefDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!asefEdificioId.equals(GeneralesConstantes.APP_ID_BASE)) {
				limpiarCombosModalAulasEdificio();
				try {
					asefListAulaDto = servJdbcAulaDto.listarXUsuarioXDependenciaXEdificio(asefUsuario.getUsrId(), asefDependenciaId, asefEdificioId);
				} catch (AulaDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (AulaDtoNoEncontradoException e) {
					limpiarCombosModalAulasEdificio();
					FacesUtil.mensajeError(e.getMessage());
				}
			}else {
				limpiarCombosModalAulasEdificio();
				FacesUtil.mensajeError("Seleccione un Edificio para continuar con la búsqueda.");
			}
		}else {
			limpiarCombosModalAulasDependencia();
			FacesUtil.mensajeError("Seleccione una Dependencia para continuar con la búsqueda.");
		}

	}
	
	public void buscarDisponibilidad(){
		if (!asefDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!asefEdificioId.equals(GeneralesConstantes.APP_ID_BASE)) {
				if (!asefAulaId.equals(GeneralesConstantes.APP_ID_BASE)) {

					limpiarCombosModalAulasAula();

					asefListHoraClaseDto = new ArrayList<>();
					asefListHoraClaseDto.add(cargarHoraClaseDto(asefAulaId, String.valueOf(asefFechaParalelo.getHour())));
					asefListHoraClaseDto.add(cargarHoraClaseDto(asefAulaId, String.valueOf(asefFechaParalelo.getHour()+1)));

					boolean isCorrecto = true;
					for (HoraClaseDto item : asefListHoraClaseDto) {
						if (isCorrecto) {
							item.setHoclDiaValue((asefFechaParalelo.getDayOfWeek().minus(1)).getValue());
							
							if (!verificarDisponibilidadAulaPorHora(item.getAlaId(), GeneralesUtilidades.cambiarDateToStringFormatoFecha(asefFechaHoraParalelo, "dd-MMM-yyyy HH").toUpperCase()+ "H00", String.valueOf(item.getHoclHoraInicio()), PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE)) {
								isCorrecto = false;
							}							
						}else {
							iniciarModalAulas();
							asefListHoraClaseDto = new ArrayList<>();
						}
					}
					
					if (isCorrecto) {
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo("Aula disponible, haga click en Guardar para continuar.");
					}

				}else {
					limpiarCombosModalAulasAula();
					FacesUtil.mensajeError("Seleccione una Aula para continuar con la búsqueda.");
				}
			}else {
				limpiarCombosModalAulasEdificio();
				FacesUtil.mensajeError("Seleccione un Edificio para continuar con la búsqueda.");
			}
		}else {
			limpiarCombosModalAulasDependencia();
			FacesUtil.mensajeError("Seleccione una Dependencia para continuar con la búsqueda.");
		}
	}
	
	private boolean verificarDisponibilidadAulaPorHora(int aulaId, String dia, String horaInicio, int pracEstado) {
		boolean retorno = true;

		try {
			List<HoraClaseDto> horasClaseAula = servHoraClaseDto.buscarDisponibilidad(aulaId, dia, horaInicio, pracEstado);
			if (!horasClaseAula.isEmpty()) {
				retorno = false;
				FacesUtil.mensajeError("El aula "+horasClaseAula.get(horasClaseAula.size()-1).getAlaDescripcion() + " se encuentra asignada al paralelo "+horasClaseAula.get(horasClaseAula.size()-1).getHracParaleloDto().getPrlCodigo()+".");
			}
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError("Error al buscar la disponibilidad del Aula seleccionada.");
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("El aula seleccionada se encuentra disponible.");
		}	

		return retorno;
	}
	
//	private boolean verificarDisponibilidadAulaPorHora(int aulaId, int dia, String horaInicio, int pracEstado) {
//		boolean retorno = true;
//
//		try {
//			List<HoraClaseDto> horasClaseAula = servHoraClaseDto.buscarDisponibilidad(aulaId, dia, horaInicio, pracEstado);
//			if (!horasClaseAula.isEmpty()) {
//				retorno = false;
//				FacesUtil.mensajeError("El aula "+horasClaseAula.get(horasClaseAula.size()-1).getAlaDescripcion() + " se encuentra asignada al paralelo "+horasClaseAula.get(horasClaseAula.size()-1).getHracParaleloDto().getPrlCodigo()+".");
//			}
//		} catch (HoraClaseDtoException e) {
//			FacesUtil.mensajeError("Error al buscar la disponibilidad del Aula seleccionada.");
//		} catch (HoraClaseDtoNoEncontradoException e) {
//			FacesUtil.mensajeInfo("El aula seleccionada se encuentra disponible.");
//		}	
//
//		return retorno;
//	}
	
	
	public void verificarActivacionModalAsignarAula(){
		if (asefFechaParalelo != null && asefFechaParalelo.getHour()>= HORA_CLASE_INICIO_JORNADA_VALUE) {
			iniciarModalAulas();
			activarModalAsignacionAula();
		}else{
			desactivarModalAsignacionAula();
			FacesUtil.mensajeInfo("Seleccione una fecha y hora mayor a " + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd-MMM-yyyy")+" 7H00");
		}
	}
	
	public void quitarAulaAsiganda(){
		desactivarModalAsignacionAula();
		asefAulaLabel = null;
		asefListHoraClaseDto = new ArrayList<>();
		asefAulaAsignada = Boolean.FALSE;
	}

	public void asignarAulaSeleccionada(){
		if (!asefListHoraClaseDto.isEmpty()) {
			try {
				Aula aula = servAula.buscarPorId(asefAulaId);
				asefAulaLabel = aula.getAlaEdificio().getEdfDescripcion() + " : " + aula.getAlaCodigo();
				asefAulaAsignada = Boolean.TRUE;
			} catch (AulaNoEncontradoException e1) {
			} catch (AulaException e1) {
			}
		}else {
			asefAulaAsignada = Boolean.FALSE;
			asefAulaLabel = null;
		}
		
		desactivarModalAsignacionAula();
	}
	
	public void vaciarModalBusquedaDocente(){
		asefIdentificacion = new String();
		asefPrimerApellido = new String();
		asefTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		asefListPersonaDto = null; 
		asefActivarModal = GeneralesConstantes.APP_ID_BASE;
	}
	
	public void limpiarModalBusquedaDocente(){
		asefIdentificacion = new String();
		asefPrimerApellido = new String();
		asefTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		asefListPersonaDto = null; 
	}
	
	
	public void asignarDocente(){
		iniciarModalAsignarDocente();
	}
	
	public void quitarDocenteAsigando(){
		desactivarModalAsignacionAula();
		asefDocenteLabel = null;
		asefDocenteAsignado = Boolean.FALSE;
		asefDocentePersonaDto = null;
		iniciarModalAsignarDocente();
	}
	
	
	public void asignarDocenteSeleccionado(PersonaDto item){
		boolean disponibilidad = false;
		
		List<HoraClaseDto> horasClaseDocente = cargarCargaHorariaPorDocente(item.getPrsId());
		if (!horasClaseDocente.isEmpty()) {
			disponibilidad = verificarCruceHorarioClases(horasClaseDocente);
			if (disponibilidad) {
				FacesUtil.limpiarMensaje();
				asefDocenteLabel = item.getPrsPrimerApellido() + " "+ item.getPrsSegundoApellido() +" "+item.getPrsNombres();
				asefDocentePersonaDto = item;
				asefDocenteAsignado = Boolean.TRUE; 
				iniciarModalAsignarDocente();
			}
		}else {
			FacesUtil.limpiarMensaje();
			asefDocenteLabel = item.getPrsPrimerApellido() + " "+ item.getPrsSegundoApellido() +" "+item.getPrsNombres();
			asefDocentePersonaDto = item;
			asefDocenteAsignado = Boolean.TRUE;
			iniciarModalAsignarDocente();
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
	
	
	public void vaciarModalAsignarAulas(){
		asefDependenciaId = GeneralesConstantes.APP_ID_BASE;
		limpiarCombosModalAulasDependencia();
		desactivarModalAsignacionAula();
	}
	
	
	public void busquedaPorIdentificacion(){
		
		if (asefIdentificacion.length() > 0) {
			asefPrimerApellido = new String();
			asefTipoBusqueda = GeneralesConstantes.APP_FIND_BY_IDENTIFICACION;
		}
		
	}

	public void busquedaPorApellido() {
		
		if (asefPrimerApellido.length() > 0) {
			asefIdentificacion = new String();
			asefTipoBusqueda = GeneralesConstantes.APP_FIND_BY_PRIMER_APELLIDO;
		}
		
	}
	
	public void buscarDocentes() {
		String param = null;
		
		if (!asefTipoBusqueda.equals(GeneralesConstantes.APP_ID_BASE)) {
			
			if (asefTipoBusqueda.intValue() == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = asefIdentificacion;
			}else {
				param = asefPrimerApellido;
			}

			
			List<PersonaDto>  docentes = cargarDocentesPorCarreraTipo(param, cargarTipoCarrera(asefCarrera .getCrrId()), asefTipoBusqueda);
			if (docentes != null && docentes.size() > 0) {
				asefListPersonaDto = docentes;
			}else {
				asefListPersonaDto = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}
			
		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del docente para continuar con la búsqueda.");
		}
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
	
	
	
	private void desactivarModalAsignacionAula() {
		asefActivarModal = 0;
	}

	private void activarModalAsignacionAula() {
		asefActivarModal = 1;
	}
	
	private boolean verificarCruceHorarioClases(List<HoraClaseDto> horario) {
		
		for (HoraClaseDto item: horario) {
			switch (item.getHracDia().intValue()) {
			case HorarioAcademicoConstantes.DIA_LUNES_VALUE:
				for (HoraClaseDto item1 : asefListHoraClaseDto) {

					if (item1.getHoclLunesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclLunesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_LUNES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_MARTES_VALUE:
				for (HoraClaseDto item1 : asefListHoraClaseDto) {
					if (item1.getHoclMartesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclMartesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_MARTES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}				
				break;
			case HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE:
				for (HoraClaseDto item1 : asefListHoraClaseDto) {
					if (item1.getHoclMiercolesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclMiercolesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_JUEVES_VALUE:
				for (HoraClaseDto item1 : asefListHoraClaseDto) {
					if (item1.getHoclJuevesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclJuevesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_JUEVES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_VIERNES_VALUE:
				for (HoraClaseDto item1 : asefListHoraClaseDto) {
					if (item1.getHoclViernesHoraClaseDto().getHoclCheckBox()){
						if (item.getHoclHoraInicio().equals(item1.getHoclViernesHoraClaseDto().getHoclHoraInicio())) {
							FacesUtil.mensajeInfo("No se puede asignar al Docente. Cruce de horario:  " + HorarioAcademicoConstantes.DIA_VIERNES_LABEL + " : " +item.getHoclDescripcion() + " Paralelo : "+ item.getHracParaleloDto().getPrlCodigo() + ".");
							return false;
						}
					}
				}
				break;
			case HorarioAcademicoConstantes.DIA_SABADO_VALUE:
				for (HoraClaseDto item1 : asefListHoraClaseDto) {
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
	
	
	
	private List<HoraClaseDto> cargarCargaHorariaPorDocente(int prsId){
		List<HoraClaseDto> retorno = new ArrayList<>();
		
		try {
			retorno = servHoraClaseDto.buscarCargaHoraria(prsId, PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		} catch (HoraClaseDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (HoraClaseDtoNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró Carga Horaria asignada al Docente seleccionado.");
		}
		
		return retorno;
	}
	
	
	public void generarCodigoParalelo() {
		if (asefFechaHoraParalelo.after(GeneralesUtilidades.getFechaActualSistema())) {
			asefFechaParalelo = Instant.ofEpochMilli(asefFechaHoraParalelo.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime(); 
			if (asefFechaParalelo.getHour() >= HORA_CLASE_INICIO_JORNADA_VALUE) {
				asefCodigoParalelo = GeneralesUtilidades.cambiarDateToStringFormatoFecha(asefFechaHoraParalelo, "dd-MMM-yyyy HH").toUpperCase()+ "H00";
			}else {
				FacesUtil.mensajeInfo("Seleccione una hora mayor o igual a las 7H00 para continuar.");
			}
			
		}else {
			asefCodigoParalelo = null;
			asefFechaHoraParalelo = null;
			asefFechaParalelo = null;
			FacesUtil.mensajeInfo("Seleccione una fecha superior a la actual para continuar.");
		}
	}
	
	
	
	public String registrarParaleloExoneracion(){
		String retorno = null;
		
		if (asefAulaLabel != null && asefAulaLabel.length() > GeneralesConstantes.APP_ID_BASE) {
			if (asefDocenteLabel != null && asefDocenteLabel.length() > GeneralesConstantes.APP_ID_BASE) {
				if (asefFechaHoraParalelo.after(GeneralesUtilidades.getFechaActualSistema())) {
					if (asefCodigoParalelo != null && asefCodigoParalelo.length() > GeneralesConstantes.APP_ID_BASE) {
						if (asefCupo != null && asefCupo > GeneralesConstantes.APP_ID_BASE) {
							
							asefParaleloDto = new ParaleloDto();
							asefParaleloDto.setPrlCodigo(asefCodigoParalelo);
							asefParaleloDto.setPrlDescripcion(asefCodigoParalelo);
							asefParaleloDto.setPrlCupo(asefCupo);
							asefParaleloDto.setPrlEstado(ParaleloConstantes.ESTADO_ACTIVO_VALUE);
							asefParaleloDto.setPracId(asefPeriodoAcademicoDto.getPracId());
							asefParaleloDto.setCrrId(asefCarrera.getCrrId());
							asefParaleloDto.setMlcrprModalidad(ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE);
							asefParaleloDto.setPrlFecha(Timestamp.valueOf(asefFechaParalelo));
							
								try {
									if (servParalelo.crearParaleloExoneracion(asefParaleloDto, asefMateriaDto, asefListHoraClaseDto, asefDocentePersonaDto)) {
										iniciarFormParalelos();
										FacesUtil.limpiarMensaje();
										FacesUtil.mensajeInfo("El Paralelo fue creado con éxito.");
										vaciarFormNuevoParalelo();
										return "irParalelosExoneraciones";
									}
								} catch (ParaleloException e) {
									FacesUtil.mensajeError(e.getMessage());
								}
								
							
						}else {
							FacesUtil.mensajeInfo("Asigne el Cupo para continuar con el registro del nuevo Paralelo.");
						}
					}else {
						FacesUtil.mensajeInfo("Seleccione una fecha superior a la actual para continuar con el registro del nuevo Paralelo.");
					}
				}else {
					FacesUtil.mensajeInfo("Seleccione una fecha superior a la actual para continuar con el registro del nuevo Paralelo.");
				}
			}else {
				FacesUtil.mensajeInfo("Asigne un Docente para continuar con el registro del nuevo Paralelo.");
			}
		}else {
			FacesUtil.mensajeInfo("Asigne un Aula para continuar con el registro del nuevo Paralelo.");
		}
		
		return retorno;
	}
	
	
	
	
	
	
	

	
	
	
	

	
	
	
	
	



	

	
	// ********************************************************************/
	// *********************** METODOS AUXILIARES *************************/
	// ********************************************************************/
	
	
	public void resetComboMateria(MateriaDto item){
		
	}
	
	
	
	
	public void crearParalelos(MateriaDto item){
		
	}
	
	public void actualizarCupoAula(){
		
	}
	
	
	
	public String etiquetarModalidadParalelo(int modalidad){
		String retorno = "";
		switch (modalidad) {
		case ModalidadConstantes.MDL_PRESENCIAL_REGULAR_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_INTENSIVO_LABEL;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_ON_LINE_LABEL;
			break;
		case ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_VALUE:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_EXONERACION_LABEL;
			break;
		default:
			retorno = ModalidadConstantes.MDL_PRESENCIAL_REGULAR_LABEL;
			break;
		}
		return retorno;
	}
	
	public String etiquetarTipoMateria(int timtId){
		String retorno = "";
		
		switch (timtId) {
		case TipoMateriaConstantes.TIPO_UNIDAD_ANALISIS_VALUE:
			retorno = TipoMateriaConstantes.TIPO_UNIDAD_ANALISIS_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_VALUE:
			retorno = TipoMateriaConstantes.TIPO_SUB_UNIDAD_ANALISIS_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_MODULAR_VALUE:
			retorno = TipoMateriaConstantes.TIPO_MODULAR_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_MODULO_VALUE:
			retorno = TipoMateriaConstantes.TIPO_MODULO_LABEL;
			break;
		case TipoMateriaConstantes.TIPO_ASIGNATURA_VALUE:
			retorno = TipoMateriaConstantes.TIPO_ASIGNATURA_LABEL;
			break;
		}
		
		return retorno;
	}
	
	public String etiquetarEstadoParalelo(int estado){
		String retorno = "";
		switch (estado) {
		case ParaleloConstantes.ESTADO_ACTIVO_VALUE:
			retorno = ParaleloConstantes.ESTADO_ACTIVO_LABEL;
			break;
		case ParaleloConstantes.ESTADO_INACTIVO_VALUE:
			retorno = ParaleloConstantes.ESTADO_INACTIVO_LABEL;
			break;
		}
		return retorno;
		
	}
	
	
	public String etiquetarEstadoAula(int estado){
		String retorno = "";
		switch (estado) {
		case 0:
			retorno = AulaConstantes.ESTADO_ACTIVO_LABEL;
			break;
		case 1:
			retorno = AulaConstantes.ESTADO_INACTIVO_LABEL;
			break;
		}
		return retorno;
		
	}
	
	
	public String etiquetarTipoAula(int estado){
		String retorno = "";
		switch (estado) {
		case 0:
			retorno = AulaConstantes.TIPO_AULA_LABEL;
			break;
		case 1:
			retorno = AulaConstantes.TIPO_LABORATORIO_LABEL;
			break;
		}
		return retorno;
		
	}
	
	
	// ********************************************************************/
	// *********************** METODOS ENCAPSULACION **********************/
	// ********************************************************************/

	public Integer getAsefDependenciaId() {
		return asefDependenciaId;
	}


	public void setAsefDependenciaId(Integer asefDependenciaId) {
		this.asefDependenciaId = asefDependenciaId;
	}




	public Integer getAsefActivarModal() {
		return asefActivarModal;
	}


	public void setAsefActivarModal(Integer asefActivarModal) {
		this.asefActivarModal = asefActivarModal;
	}


	public Boolean getAsefDisabledModalidad() {
		return asefDisabledModalidad;
	}


	public void setAsefDisabledModalidad(Boolean asefDisabledModalidad) {
		this.asefDisabledModalidad = asefDisabledModalidad;
	}


	public Usuario getAsefUsuario() {
		return asefUsuario;
	}


	public void setAsefUsuario(Usuario asefUsuario) {
		this.asefUsuario = asefUsuario;
	}


	public AulaDto getAsefAulaDto() {
		return asefAulaDto;
	}


	public void setAsefAulaDto(AulaDto asefAulaDto) {
		this.asefAulaDto = asefAulaDto;
	}


	public ParaleloDto getAsefParaleloDto() {
		return asefParaleloDto;
	}


	public void setAsefParaleloDto(ParaleloDto asefParaleloDto) {
		this.asefParaleloDto = asefParaleloDto;
	}


	public MateriaDto getAsefMateriaDto() {
		return asefMateriaDto;
	}


	public void setAsefMateriaDto(MateriaDto asefMateriaDto) {
		this.asefMateriaDto = asefMateriaDto;
	}


	public List<PeriodoAcademicoDto> getAsefListPeriodoAcademicoDto() {
		return asefListPeriodoAcademicoDto;
	}


	public void setAsefListPeriodoAcademicoDto(List<PeriodoAcademicoDto> asefListPeriodoAcademicoDto) {
		this.asefListPeriodoAcademicoDto = asefListPeriodoAcademicoDto;
	}


	public List<CarreraDto> getAsefListCarreraDto() {
		return asefListCarreraDto;
	}


	public void setAsefListCarreraDto(List<CarreraDto> asefListCarreraDto) {
		this.asefListCarreraDto = asefListCarreraDto;
	}


	public List<NivelDto> getAsefListNivelDto() {
		return asefListNivelDto;
	}


	public void setAsefListNivelDto(List<NivelDto> asefListNivelDto) {
		this.asefListNivelDto = asefListNivelDto;
	}


	public List<ModalidadDto> getAsefListModalidadDto() {
		return asefListModalidadDto;
	}


	public void setAsefListModalidadDto(List<ModalidadDto> asefListModalidadDto) {
		this.asefListModalidadDto = asefListModalidadDto;
	}


	public List<ParaleloDto> getAsefListParaleloDto() {
		return asefListParaleloDto;
	}


	public void setAsefListParaleloDto(List<ParaleloDto> asefListParaleloDto) {
		this.asefListParaleloDto = asefListParaleloDto;
	}


	public List<MateriaDto> getAsefListMateriaDto() {
		return asefListMateriaDto;
	}


	public void setAsefListMateriaDto(List<MateriaDto> asefListMateriaDto) {
		this.asefListMateriaDto = asefListMateriaDto;
	}


	public List<AulaDto> getAsefListAulaDto() {
		return asefListAulaDto;
	}


	public void setAsefListAulaDto(List<AulaDto> asefListAulaDto) {
		this.asefListAulaDto = asefListAulaDto;
	}


	public List<CarreraDto> getAsefListAreaDto() {
		return asefListAreaDto;
	}


	public void setAsefListAreaDto(List<CarreraDto> asefListAreaDto) {
		this.asefListAreaDto = asefListAreaDto;
	}


	public List<Carrera> getAsefListCarrera() {
		return asefListCarrera;
	}


	public void setAsefListCarrera(List<Carrera> asefListCarrera) {
		this.asefListCarrera = asefListCarrera;
	}


	public List<SelectItem> getAsefListEstados() {
		return asefListEstados;
	}


	public void setAsefListEstados(List<SelectItem> asefListEstados) {
		this.asefListEstados = asefListEstados;
	}


	public String getAsefAulaLabel() {
		return asefAulaLabel;
	}


	public void setAsefAulaLabel(String asefAulaLabel) {
		this.asefAulaLabel = asefAulaLabel;
	}


	public String getAsefDocenteLabel() {
		return asefDocenteLabel;
	}


	public void setAsefDocenteLabel(String asefDocenteLabel) {
		this.asefDocenteLabel = asefDocenteLabel;
	}


	public List<DependenciaDto> getAsefListDependenciaDto() {
		return asefListDependenciaDto;
	}


	public void setAsefListDependenciaDto(List<DependenciaDto> asefListDependenciaDto) {
		this.asefListDependenciaDto = asefListDependenciaDto;
	}



	public Integer getAsefEdificioId() {
		return asefEdificioId;
	}


	public void setAsefEdificioId(Integer asefEdificioId) {
		this.asefEdificioId = asefEdificioId;
	}


	public Integer getAsefAulaId() {
		return asefAulaId;
	}


	public void setAsefAulaId(Integer asefAulaId) {
		this.asefAulaId = asefAulaId;
	}



	public List<EdificioDto> getAsefListEdificioDto() {
		return asefListEdificioDto;
	}


	public void setAsefListEdificioDto(List<EdificioDto> asefListEdificioDto) {
		this.asefListEdificioDto = asefListEdificioDto;
	}


	public Integer getAsefCupo() {
		return asefCupo;
	}


	public void setAsefCupo(Integer asefCupo) {
		this.asefCupo = asefCupo;
	}


	public String getAsefCodigoParalelo() {
		return asefCodigoParalelo;
	}


	public void setAsefCodigoParalelo(String asefCodigoParalelo) {
		this.asefCodigoParalelo = asefCodigoParalelo;
	}


	public String getAsefIdentificacion() {
		return asefIdentificacion;
	}


	public void setAsefIdentificacion(String asefIdentificacion) {
		this.asefIdentificacion = asefIdentificacion;
	}


	public String getAsefPrimerApellido() {
		return asefPrimerApellido;
	}


	public void setAsefPrimerApellido(String asefPrimerApellido) {
		this.asefPrimerApellido = asefPrimerApellido;
	}


	public Integer getAsefTipoBusqueda() {
		return asefTipoBusqueda;
	}


	public void setAsefTipoBusqueda(Integer asefTipoBusqueda) {
		this.asefTipoBusqueda = asefTipoBusqueda;
	}


	public List<PersonaDto> getAsefListPersonaDto() {
		return asefListPersonaDto;
	}


	public void setAsefListPersonaDto(List<PersonaDto> asefListPersonaDto) {
		this.asefListPersonaDto = asefListPersonaDto;
	}


	public Carrera getAsefCarrera() {
		return asefCarrera;
	}


	public void setAsefCarrera(Carrera asefCarrera) {
		this.asefCarrera = asefCarrera;
	}


	public PeriodoAcademicoDto getAsefPeriodoAcademicoDto() {
		return asefPeriodoAcademicoDto;
	}


	public void setAsefPeriodoAcademicoDto(PeriodoAcademicoDto asefPeriodoAcademicoDto) {
		this.asefPeriodoAcademicoDto = asefPeriodoAcademicoDto;
	}

	public Boolean getAsefDocenteAsignado() {
		return asefDocenteAsignado;
	}

	public void setAsefDocenteAsignado(Boolean asefDocenteAsignado) {
		this.asefDocenteAsignado = asefDocenteAsignado;
	}


	public CargaHoraria getAsefCargaHoraria() {
		return asefCargaHoraria;
	}


	public void setAsefCargaHoraria(CargaHoraria asefCargaHoraria) {
		this.asefCargaHoraria = asefCargaHoraria;
	}


	public PersonaDto getAsefDocentePersonaDto() {
		return asefDocentePersonaDto;
	}


	public void setAsefDocentePersonaDto(PersonaDto asefDocentePersonaDto) {
		this.asefDocentePersonaDto = asefDocentePersonaDto;
	}


	public List<HoraClaseDto> getAsefListHoraClaseDto() {
		return asefListHoraClaseDto;
	}


	public void setAsefListHoraClaseDto(List<HoraClaseDto> asefListHoraClaseDto) {
		this.asefListHoraClaseDto = asefListHoraClaseDto;
	}


	public Integer getAsefPeriodoId() {
		return asefPeriodoId;
	}


	public void setAsefPeriodoId(Integer asefPeriodoId) {
		this.asefPeriodoId = asefPeriodoId;
	}


	public Integer getAsefCarreraId() {
		return asefCarreraId;
	}


	public void setAsefCarreraId(Integer asefCarreraId) {
		this.asefCarreraId = asefCarreraId;
	}


	public Integer getAsefNivelId() {
		return asefNivelId;
	}


	public void setAsefNivelId(Integer asefNivelId) {
		this.asefNivelId = asefNivelId;
	}


	public Boolean getAsefAulaAsignada() {
		return asefAulaAsignada;
	}


	public void setAsefAulaAsignada(Boolean asefAulaAsignada) {
		this.asefAulaAsignada = asefAulaAsignada;
	}


	public NivelDto getAsefNivelDto() {
		return asefNivelDto;
	}


	public void setAsefNivelDto(NivelDto asefNivelDto) {
		this.asefNivelDto = asefNivelDto;
	}


	public Integer getAsefHoraId() {
		return asefHoraId;
	}


	public void setAsefHoraId(Integer asefHoraId) {
		this.asefHoraId = asefHoraId;
	}


	public Date getAsefFechaHoraParalelo() {
		return asefFechaHoraParalelo;
	}


	public void setAsefFechaHoraParalelo(Date asefFechaHoraParalelo) {
		this.asefFechaHoraParalelo = asefFechaHoraParalelo;
	}


	public LocalDateTime getAsefFechaParalelo() {
		return asefFechaParalelo;
	}


	public void setAsefFechaParalelo(LocalDateTime asefFechaParalelo) {
		this.asefFechaParalelo = asefFechaParalelo;
	}


	public Integer getAsefEstadoId() {
		return asefEstadoId;
	}


	public void setAsefEstadoId(Integer asefEstadoId) {
		this.asefEstadoId = asefEstadoId;
	}


	public List<RecordEstudianteDto> getAsefListRecordEstudianteDto() {
		return asefListRecordEstudianteDto;
	}


	public void setAsefListRecordEstudianteDto(List<RecordEstudianteDto> asefListRecordEstudianteDto) {
		this.asefListRecordEstudianteDto = asefListRecordEstudianteDto;
	}

	
	
 
	


}

