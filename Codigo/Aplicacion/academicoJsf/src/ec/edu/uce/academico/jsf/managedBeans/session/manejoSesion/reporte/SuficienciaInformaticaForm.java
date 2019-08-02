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
   
 ARCHIVO:     SuficienciaInformaticaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para los reportes de la
 suficiencia en informatica.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
29-ABR-2019				 FREDDY GUZMÁN						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
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
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteSuficienciaInformaticaForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;



/**
 * Clase (managed bean) SuficienciaInformaticaForm.
 * Managed Bean que maneja las peticiones para los reportes de la Suficiencia en Informatica.
 * @author fgguzman.
 * @version 1.0
 */

@ManagedBean(name="suficienciaInformaticaForm")
@SessionScoped
public class SuficienciaInformaticaForm implements Serializable{
	
	// ********************************************************************/
	// ******************* ATRIBUTOS DE LA CLASE **************************/
	// ********************************************************************/
	private static final long serialVersionUID = 132143102732766241L;
	
	private String sifTipoArchivo;
	private String sifNombreJasper;
	private String sifTokenServlet;
	
	private Integer sifActivarReporte;
	private Integer sifEstadoId;
	private Integer sifOpcionId;
	
	private Usuario sifUsuario;
	private Date sifFechaHoraId;
	private PeriodoAcademicoDto sifPeriodoAcademicoDto;
	private Carrera sifCarrera;
	private MateriaDto sifMateriaDto;
	
	private List<PersonaDto> sifListPersonaDto;
	private List<SelectItem> sifListEstadoMatricula;
	private List<SelectItem> sifListOpcionReporte;
	
	
	
	
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
	
	public String irReporteAsistencias(Usuario user){
		String retorno = null;
		
		try {
			sifUsuario = user;
			sifPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(PeriodoAcademicoConstantes.PRAC_PERIODO_SUFICIENCIA_INFORMATICA_EXONERACIONES_VALUE);
			sifCarrera = servCarrera.buscarPorId(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE);
			
			List<MateriaDto> materias = servJdbcMateriaDto.listarMateriasxCarreraFull(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
			if (materias!= null && materias.size()>0) {
				sifMateriaDto = materias.get(materias.size()-1);
			}
			
			sifFechaHoraId = null;
			sifEstadoId = GeneralesConstantes.APP_ID_BASE;
			sifListPersonaDto = new ArrayList<>();
			sifListEstadoMatricula = cargarEstadosMatriculaExoneraciones();
			sifListOpcionReporte = cargarOpcionesImpresion();
					
			retorno = "irReporteAsistencias";

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
		}
		
		return retorno;
	}
	
	private List<SelectItem> cargarOpcionesImpresion() {
		List<SelectItem> retorno = new ArrayList<>();
		retorno.add(new SelectItem(new Integer(1), new String("ASISTENCIAS")));
		retorno.add(new SelectItem(new Integer(2), new String("RESULTADOS")));
		return retorno;
	}
	
	private List<SelectItem> cargarEstadosMatriculaExoneraciones() {
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE, RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE, RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_APROBADO_VALUE, RecordEstudianteConstantes.ESTADO_APROBADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE, RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL));
		retorno.add(new SelectItem(RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE, RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL));
		return retorno;
	}
	
	public void cancelarGenerarReportes(){
		sifOpcionId = GeneralesConstantes.APP_ID_BASE;
		sifActivarReporte = GeneralesConstantes.APP_ID_BASE;
	}
	
	public void generarReportes(){
		if (!sifOpcionId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!sifListPersonaDto.isEmpty()) {
				sifTipoArchivo = "PDF";
				sifTokenServlet = "SUFICIENCIA_INFORMATICA";

				switch (sifOpcionId) {
				case 1:

					sifNombreJasper = "asistenciaExoneracion";
					PersonaDto director = cargarDirectorCarrera();
					ReporteSuficienciaInformaticaForm.generarEstudiantesExoneracion(sifUsuario, director, sifListPersonaDto);
					sifActivarReporte = 1;
					break;
				case 2:
					sifNombreJasper = "resultadosExoneracion";
					ReporteSuficienciaInformaticaForm.generarCalificacionEstudiantesExoneracion(sifUsuario, sifListPersonaDto);
					sifActivarReporte = 1;
					break;
				}
			}else{
				cancelarGenerarReportes();
				FacesUtil.mensajeError("No se encontró datos para mostrar, intente con otra fecha.");
			}

		}else {
			cancelarGenerarReportes();
			FacesUtil.mensajeInfo("Seleccione una opcion para generar el reporte");
		}
	}

	
	public String irReporteAprobados(Usuario user){
		String retorno = null;
		
		try {
			sifUsuario = user;
			sifPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(PeriodoAcademicoConstantes.PRAC_PERIODO_SUFICIENCIA_INFORMATICA_EXONERACIONES_VALUE);
			sifCarrera = servCarrera.buscarPorId(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE);
			
			List<MateriaDto> materias = servJdbcMateriaDto.listarMateriasxCarreraFull(CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE});
			if (materias!= null && materias.size()>0) {
				sifMateriaDto = materias.get(materias.size()-1);
			}
			
			sifFechaHoraId = null;
			
			retorno = "irReporteAprobados";

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
		}
		
		return retorno;
	}
	
	public String irInicio(){
		sifTipoArchivo = null;
		sifNombreJasper = null;
		sifTokenServlet = null;
		sifActivarReporte = null;
		
		sifUsuario = null;
		sifFechaHoraId = null;
		sifPeriodoAcademicoDto = null;
		sifCarrera = null;
		sifMateriaDto = null;
		
		sifListPersonaDto = null;
		
		return "irInicio";
	}
	
	// ********************************************************************/
	// *********************** METODOS GENERALES **************************/
	// ********************************************************************/
	
	
	
	public void buscarEstudiantesPorFecha(){
		if (sifFechaHoraId  != null) {
			sifOpcionId = GeneralesConstantes.APP_ID_BASE;
			sifListPersonaDto = cargarEstudiantesPorFecha(sifFechaHoraId);
			cancelarGenerarReportes();
		}else {
			FacesUtil.mensajeError("Seleccione una fecha para Continuar.");
		}
		
	}
	
	public void limpiarFormAsistentecias(){
		sifFechaHoraId = null;
		sifEstadoId = GeneralesConstantes.APP_ID_BASE;
		sifOpcionId = GeneralesConstantes.APP_ID_BASE;
		sifActivarReporte = GeneralesConstantes.APP_ID_BASE;
		sifListPersonaDto = new ArrayList<>();
	}
	
	/**
	 * Mètodo que permite cargar datos del director de carrera
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarDirectorCarrera(){
		
		PersonaDto director  = null;
		
		try {
			director = servJdbcPersonaDto.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE, CarreraConstantes.CARRERA_SUFICIENCIA_INFORMATICA_VALUE);
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró Director de Carrera.");
		}
		
		return director;
	}
	
	private List<PersonaDto> cargarEstudiantesPorFecha(Date fecha){
		List<PersonaDto> retorno = new ArrayList<>();

		try {
			retorno = servJdbcPersonaDto.buscarEstudiantesPorFechaExoneracion(GeneralesUtilidades.cambiarDateToStringFormatoFecha(fecha, "dd-MMM-yyyy"));
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	
	// ********************************************************************/
	// *********************** ENCAPSULAMIENTO ****************************/
	// ********************************************************************/
	public Date getSifFechaHoraId() {
		return sifFechaHoraId;
	}
	public void setSifFechaHoraId(Date sifFechaHoraId) {
		this.sifFechaHoraId = sifFechaHoraId;
	}
	public PeriodoAcademicoDto getSifPeriodoAcademicoDto() {
		return sifPeriodoAcademicoDto;
	}
	public void setSifPeriodoAcademicoDto(PeriodoAcademicoDto sifPeriodoAcademicoDto) {
		this.sifPeriodoAcademicoDto = sifPeriodoAcademicoDto;
	}
	public MateriaDto getSifMateriaDto() {
		return sifMateriaDto;
	}
	public void setSifMateriaDto(MateriaDto sifMateriaDto) {
		this.sifMateriaDto = sifMateriaDto;
	}
	public List<PersonaDto> getSifListPersonaDto() {
		return sifListPersonaDto;
	}
	public void setSifListPersonaDto(List<PersonaDto> sifListPersonaDto) {
		this.sifListPersonaDto = sifListPersonaDto;
	}

	public String getSifTipoArchivo() {
		return sifTipoArchivo;
	}

	public void setSifTipoArchivo(String sifTipoArchivo) {
		this.sifTipoArchivo = sifTipoArchivo;
	}

	public String getSifNombreJasper() {
		return sifNombreJasper;
	}

	public void setSifNombreJasper(String sifNombreJasper) {
		this.sifNombreJasper = sifNombreJasper;
	}

	public String getSifTokenServlet() {
		return sifTokenServlet;
	}

	public void setSifTokenServlet(String sifTokenServlet) {
		this.sifTokenServlet = sifTokenServlet;
	}

	public Integer getSifActivarReporte() {
		return sifActivarReporte;
	}

	public void setSifActivarReporte(Integer sifActivarReporte) {
		this.sifActivarReporte = sifActivarReporte;
	}

	public Usuario getSifUsuario() {
		return sifUsuario;
	}

	public void setSifUsuario(Usuario sifUsuario) {
		this.sifUsuario = sifUsuario;
	}

	public Carrera getSifCarrera() {
		return sifCarrera;
	}

	public void setSifCarrera(Carrera sifCarrera) {
		this.sifCarrera = sifCarrera;
	}

	public Integer getSifEstadoId() {
		return sifEstadoId;
	}

	public void setSifEstadoId(Integer sifEstadoId) {
		this.sifEstadoId = sifEstadoId;
	}

	public List<SelectItem> getSifListEstadoMatricula() {
		return sifListEstadoMatricula;
	}

	public void setSifListEstadoMatricula(List<SelectItem> sifListEstadoMatricula) {
		this.sifListEstadoMatricula = sifListEstadoMatricula;
	}

	public Integer getSifOpcionId() {
		return sifOpcionId;
	}

	public void setSifOpcionId(Integer sifOpcionId) {
		this.sifOpcionId = sifOpcionId;
	}

	public List<SelectItem> getSifListOpcionReporte() {
		return sifListOpcionReporte;
	}

	public void setSifListOpcionReporte(List<SelectItem> sifListOpcionReporte) {
		this.sifListOpcionReporte = sifListOpcionReporte;
	}

	
	
	
}