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
   
 ARCHIVO:     EstudianteSuficienciaForm.java	  
 DESCRIPCION: Bean de sesion que maneja las aprobaciones de la suficiencias. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 18-ABRIL-2019 			 Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.administracionSecretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaIdiomasServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteCertificadoSuficienciasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EstudianteSuficienciaForm.
 * Managed Bean que administra los estudiantes para la visualización de matriculados.
 * @author fgguzman.
 * @version 1.0
 */


@ManagedBean(name="estudianteSuficienciaForm")
@SessionScoped
public class EstudianteSuficienciaForm implements Serializable{
	private static final long serialVersionUID = 4816707704524277983L;
	private final int FIND_BY_IDENTIFICACION = 1;
	private final int FIND_BY_COMBOS = 2;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	private Usuario esfUsuario;
	
	private Integer esfActivarReporte;
	private Integer esfPeriodoId;
	private Integer esfDependenciaId;
	private Integer esfCarreraId;
	private Integer esfTipoBusqueda;
	private String esfIdentificacion;
	
	private List<Carrera> esfListCarrera;
	private List<PersonaDto> esfListEstudianteAprobado;
	private List<PeriodoAcademicoDto> esfListPeriodoAcademicoDto;
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	
	@EJB private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB private SuficienciaIdiomasServicioJdbc servJdbcSuficienciaIdiomas;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB private UsuarioRolServicio servUsuarioRol;
	@EJB private DependenciaServicio servDependencia;
	@EJB private CarreraServicio servCarrera;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	public String irBuscarEstudiantesAprobadosSuficiencia(Usuario usuario, int rolId){
		String retorno = null;
		
		try {
			esfListPeriodoAcademicoDto = servJdbcPeriodoAcademicoDto.buscar(new Integer[]{PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE, PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE}, new Integer[]{PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE});
			PeriodoAcademicoDto homologacion = new PeriodoAcademicoDto();
			homologacion.setPracId(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_VALUE);
			homologacion.setPracDescripcion(PeriodoAcademicoConstantes.PRAC_HOMOLOGACION_LABEL);
			esfListPeriodoAcademicoDto.add(homologacion);
			esfListPeriodoAcademicoDto.sort(Comparator.comparing(PeriodoAcademicoDto::getPracDescripcion));
			
			if (rolId == RolConstantes.ROL_SECRESUFICIENCIAS_VALUE) {
				esfListCarrera = servCarrera.listarTodos();
			}else {
				esfListCarrera = servCarrera.buscarCarrerasPorUsuarioRol(usuario.getUsrId(), rolId, UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			}
			
			esfListCarrera.sort(Comparator.comparing(Carrera::getCrrDescripcion));
			
			iniciarFormEstudiantes();
			retorno = "irListarAprobadosSuficiencias";
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	

	private void iniciarFormEstudiantes(){
		esfListEstudianteAprobado = new ArrayList<>();
		esfIdentificacion = new String();
		esfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		esfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		esfCarreraId = GeneralesConstantes.APP_ID_BASE;
		esfTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		
	}
	
	public String irInicio(){
		iniciarFormEstudiantes();
		return "irInicio";
	}
	
	public String irVerRecordEstudianteIdioma(){
	
		return "irVerRecordEstudianteIdioma";
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	public void verificarClickExportarExcel(){
		if (!esfListEstudianteAprobado.isEmpty()) {
			esfActivarReporte = Integer.valueOf(1);
			ReporteCertificadoSuficienciasForm.generarReporteEstudiantesAprodados(esfListEstudianteAprobado);
		}else {
			esfActivarReporte = GeneralesConstantes.APP_ID_BASE;
			FacesUtil.mensajeError("No existen estudiantes aprobados para exportar a Excel.");
		}
	}
	
	public void buscarCarreras(){

		establecerBusquedaPorCombos();

		if (esfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			FacesUtil.mensajeError("Seleccione un período académico para continuar.");	
		}
		
	}
	
	public void establecerBusquedaPorCombos(){
		esfIdentificacion = new String();
		esfTipoBusqueda = FIND_BY_COMBOS;
		esfListEstudianteAprobado = new ArrayList<>();
		
		if (esfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {
			FacesUtil.mensajeError("Seleccione una carrera para continuar.");
		}
	}
	
	public void establecerBusquedaPorIdentificacion(){

		if (GeneralesUtilidades.quitarEspaciosEnBlanco(esfIdentificacion).length() > 0) {
			esfTipoBusqueda = FIND_BY_IDENTIFICACION;
			esfPeriodoId = GeneralesConstantes.APP_ID_BASE;
			esfCarreraId = GeneralesConstantes.APP_ID_BASE;
			esfListEstudianteAprobado = new ArrayList<>();
		}

	}
	
	public void buscarEstudiantesAprobados(){
		List<PersonaDto> estudiantes = new ArrayList<>();

		if (esfTipoBusqueda.equals(FIND_BY_IDENTIFICACION)) {
			estudiantes = verificarSiAproboAlgunIdioma(esfIdentificacion);
		}else if (esfTipoBusqueda.equals(FIND_BY_COMBOS)) {

			if (!esfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {

				if (!esfCarreraId.equals(GeneralesConstantes.APP_ID_BASE)) {

					estudiantes = cargarEstudiantesAprobadosIdiomas(esfPeriodoId, esfCarreraId);
					
				}else {
					FacesUtil.mensajeInfo("Seleccione una carrera para continuar.");
				}

			}else {
				FacesUtil.mensajeInfo("Seleccione un período académico para continuar.");
			}
		}else {
			FacesUtil.mensajeInfo("Es necesario seleccionar los campos o  ingresar la identificación del estudiante para continuar.");
		}
		
		if (!estudiantes.isEmpty()) {
				esfListEstudianteAprobado = estudiantes;
				esfListEstudianteAprobado.sort(Comparator.comparing(PersonaDto::getCrrDescripcion).thenComparing(Comparator.comparing(PersonaDto::getCrrDetalle).thenComparing(Comparator.comparing(PersonaDto::getPrsPrimerApellido))));
				FacesUtil.limpiarMensaje();
		}else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
		}
	}
	
	private List<PersonaDto> verificarSiAproboAlgunIdioma(String identificacion) {
		List<PersonaDto> retorno = new ArrayList<>();

		try {
			List<RecordEstudianteDto> aprobados = servJdbcSuficienciaIdiomas.buscarAprobacionIdiomasSIIU(identificacion);
			if (!aprobados.isEmpty()) {
				for (RecordEstudianteDto item : aprobados) {
					PersonaDto estudiante = new PersonaDto();
					estudiante.setCrrDetalle(item.getRcesCarreraDto().getCrrDescripcion());
					estudiante.setPrsIdentificacion(item.getRcesEstudianteDto().getPrsIdentificacion());
					estudiante.setPrsPrimerApellido(item.getRcesEstudianteDto().getPrsPrimerApellido());
					estudiante.setPrsSegundoApellido(item.getRcesEstudianteDto().getPrsSegundoApellido());
					estudiante.setPrsNombres(item.getRcesEstudianteDto().getPrsNombres());
					estudiante.setFcinObservacion(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL);
					
					try {
						List<PersonaDto> fichas = servJdbcPersonaDto.buscarEstudiantePorIdentificacion(item.getRcesEstudianteDto().getPrsIdentificacion());
						estudiante.setCrrDescripcion(fichas.get(fichas.size()-1).getCrrDescripcion());
						estudiante.setPrsMailInstitucional(fichas.get(fichas.size()-1).getPrsMailInstitucional());
					} catch (Exception e1) {
						estudiante.setCrrDescripcion("EGRESADO");
						estudiante.setPrsMailInstitucional("");
					}
					
					retorno.add(estudiante);
					
				}
			}
			
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}


	private List<PersonaDto> cargarEstudiantesAprobadosIdiomas(Integer periodoId, Integer carreraId) {
		List<PersonaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcSuficienciaIdiomas.cargarEstudiantesAprobadosIdiomasPorCarrera(periodoId, carreraId);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}


	public void limpiarFormListarEstudiantes(){
		iniciarFormEstudiantes();
	}
	
	
	//****************************************************************/
	//******************* GETTERS Y SETTERS *************************/
	//****************************************************************/

	public Usuario getEsfUsuario() {
		return esfUsuario;
	}

	public void setEsfUsuario(Usuario esfUsuario) {
		this.esfUsuario = esfUsuario;
	}

	public Integer getEsfDependenciaId() {
		return esfDependenciaId;
	}

	public void setEsfDependenciaId(Integer esfDependenciaId) {
		this.esfDependenciaId = esfDependenciaId;
	}

	public Integer getEsfCarreraId() {
		return esfCarreraId;
	}

	public void setEsfCarreraId(Integer esfCarreraId) {
		this.esfCarreraId = esfCarreraId;
	}

	public List<Carrera> getEsfListCarrera() {
		return esfListCarrera;
	}

	public void setEsfListCarrera(List<Carrera> esfListCarrera) {
		this.esfListCarrera = esfListCarrera;
	}

	public String getEsfIdentificacion() {
		return esfIdentificacion;
	}

	public void setEsfIdentificacion(String esfIdentificacion) {
		this.esfIdentificacion = esfIdentificacion;
	}


	public Integer getEsfTipoBusqueda() {
		return esfTipoBusqueda;
	}


	public void setEsfTipoBusqueda(Integer esfTipoBusqueda) {
		this.esfTipoBusqueda = esfTipoBusqueda;
	}


	public Integer getEsfActivarReporte() {
		return esfActivarReporte;
	}


	public void setEsfActivarReporte(Integer esfActivarReporte) {
		this.esfActivarReporte = esfActivarReporte;
	}

	public Integer getEsfPeriodoId() {
		return esfPeriodoId;
	}

	public void setEsfPeriodoId(Integer esfPeriodoId) {
		this.esfPeriodoId = esfPeriodoId;
	}

	public List<PeriodoAcademicoDto> getEsfListPeriodoAcademicoDto() {
		return esfListPeriodoAcademicoDto;
	}

	public void setEsfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> esfListPeriodoAcademicoDto) {
		this.esfListPeriodoAcademicoDto = esfListPeriodoAcademicoDto;
	}


	public List<PersonaDto> getEsfListEstudianteAprobado() {
		return esfListEstudianteAprobado;
	}


	public void setEsfListEstudianteAprobado(List<PersonaDto> esfListEstudianteAprobado) {
		this.esfListEstudianteAprobado = esfListEstudianteAprobado;
	}




	

	
}
