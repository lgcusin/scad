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
   
 ARCHIVO:     ReporteHorarioEstudianteForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones para el reporte de horario por estudiante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
10-DIC-2018		    	Freddy Guzmán 			      		Carga Horaria
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.academico.ejb.dtos.CargaHorariaDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.DependenciaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CargaHorariaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MallaCurricularMateriaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PeriodoAcademicoDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDatosDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.DependenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoMateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial.CargaHoraria20182018;
import ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial.CargaHoraria20182019;
import ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial.CargaHoraria20192019;
import ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.cargaHoraria.historial.CargaHorariaTemplate;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Clase (managed bean) ReporteCargaHorariaForm.
 * Managed Bean que permite generar los reportes de la carga horaria de los docentes.
 * @author fgguzman
 * @version 1.0
 */

@SessionScoped
@ManagedBean(name="reporteCargaHorariaForm")
public class ReporteCargaHorariaForm extends ReporteTemplateForm implements Serializable {
	private static final long serialVersionUID = -5422984994284974811L;
	
	// ****************************************************************/
	// ************************* ATRIBUTOS ****************************/
	// ****************************************************************/
	
	protected Integer rchfActivarReporte;
	private String rchfNombreArchivo ;
	private String rchfNombreReporte;
	private String rchfTipoArchivo;
	
	private Integer rchfPeriodoId;
	private Integer rchfDependenciaId;
	
	private Integer[] rchfImparticionClases;
	private Integer[] rchfAsignacionFunciones;
	
	private Usuario rchfUsuario;
	private PersonaDto rchfDocenteDto;
	private PersonaDto rchfDirectorDto;
	private DependenciaDto rchfDependenciaDto;

	
	private PeriodoAcademicoDto rchfPeriodoAcademicoDto;
	private List<PeriodoAcademicoDto> rchfListPeriodoAcademicoDto;
	private List<CargaHorariaDto> rchfListFuncionesPeriodoCargaHorariaDto;
	private List<CargaHorariaDto> rchfListFuncionesDocenteCargaHorariaDto;
	private List<CargaHorariaDto> rchfListImparticionClasesCargaHorariaDto;
	private List<CargaHorariaDto> rchfListCargaHorariaDto;
	private List<PersonaDatosDto> rchfListPersonaDatosDto;
	private List<Dependencia> rchfListDependencia;
	
	private List<Map<String, Object>>  rchfMapReporteGeneral ;

	
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB private PersonaDatosDtoServicioJdbc servJdbcPersonaDatosDto;
	@EJB private PeriodoAcademicoDtoServicioJdbc servJdbcPeriodoAcademicoDto;
	@EJB private CargaHorariaServicioJdbc servJdbcCargaHorariaDto;
	@EJB private MallaCurricularMateriaDtoServicioJdbc servJdbcMallaCurriculaMateriaDto;
	@EJB private DependenciaServicio servDependenciaServicio;
	@EJB private PersonaDtoServicioJdbc servJdbcPersonaDto;

	
	// ****************************************************************/
	// ****************** METODOS DE NAVEGACIÓN  **********************/
	// ****************************************************************/
	public String irInicio(){
		rchfPeriodoId = null;
		rchfActivarReporte= null;
		rchfNombreArchivo = null;
		rchfNombreReporte= null;
		rchfTipoArchivo= null;
		
		rchfImparticionClases= null;
		rchfAsignacionFunciones= null;
		
		rchfDocenteDto= null;
		rchfDependenciaDto =  null;
		
		rchfPeriodoAcademicoDto= null;
		rchfListPeriodoAcademicoDto= null;
		rchfListFuncionesPeriodoCargaHorariaDto= null;
		rchfListFuncionesDocenteCargaHorariaDto= null;
		rchfListImparticionClasesCargaHorariaDto= null;
		rchfListCargaHorariaDto= null;
		return "irInicio";
	}
	
	
	public String irVerCargaHorariaGeneral(Usuario usuario){
		String retorno = null;
		
		 try {
			 
			List<PeriodoAcademicoDto> periodos = servJdbcPeriodoAcademicoDto.buscarPeriodosCargaHoraria();
			 if (!periodos.isEmpty()) {
				iniciarFormCargaHorariaPorFacultad(usuario);
				Iterator<PeriodoAcademicoDto> iter = periodos.iterator();
				while(iter.hasNext()){
					PeriodoAcademicoDto cad = (PeriodoAcademicoDto) iter.next();
					if(cad.getPracId() < 150 ){
						iter.remove();
					}
				}
				rchfListPeriodoAcademicoDto = periodos;
				rchfListPeriodoAcademicoDto.sort(Comparator.comparing(PeriodoAcademicoDto::getPracDescripcion));
					
				List<Dependencia> dependencias = servDependenciaServicio.listarFacultadesActivas(DependenciaConstantes.ESTADO_JERARQUIA_FACULTADES_VALUE);
				Iterator<Dependencia> it = dependencias.iterator();
				while(it.hasNext()){
					Dependencia cad = (Dependencia) it.next();
					if(cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_NIVELACION_VALUE ||
							cad.getDpnId() == DependenciaConstantes.DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE ){
						it.remove();
					}
				}
				
				rchfListDependencia = dependencias;
				rchfListDependencia.sort(Comparator.comparing(Dependencia::getDpnDescripcion));

				retorno = "ircargaHorariaPorFacultad";
			}
			
		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (DependenciaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}

	public String irVerCargaHoraria(Usuario usuario){

		try {
			
			PeriodoAcademicoDto periodo = servJdbcPeriodoAcademicoDto.buscarXEstadoPregrado(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);	
			if (periodo != null) {
				
				rchfUsuario = usuario;
				rchfPeriodoAcademicoDto = periodo;
				rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
				
				rchfPeriodoId = periodo.getPracId();
				
				rchfDocenteDto = cargarInformacionPersonal(usuario.getUsrIdentificacion());
				rchfDocenteDto.setPstDenominacion(CargaHorariaTemplate.establecerCategoria(rchfDocenteDto.getPstCategoria(), rchfDocenteDto.getPstNivelRangoGradual()));
				
				rchfDirectorDto = cargarInformacionPersonal(null);
				
				List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(usuario.getUsrIdentificacion(), rchfPeriodoId);
				if (!listImparticionClases.isEmpty()) {
					rchfListImparticionClasesCargaHorariaDto = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);
				}
				
			}

		} catch (PeriodoAcademicoDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PeriodoAcademicoDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("No se encontró un período académico activo.");
		}

		return "irVerCargaHoraria";

	}
	
	
	/**
	 * Método que permite consolidar la informacion para mostrar en el reporte.
	 * - Si es modulo -> contabilizar los alumnos matriculados en la modular.
	 * - Si es paralelo compartido (materia+docente) -> etiquetar cual es el principal.
	 * - Si es paralelo compartido (materia+docente) -> cuantificar los alumnos matriculados al principal. 
	 */
	private List<CargaHorariaDto> cargarMatriculadosPorModularEtiquetarHorarioPrincipal(List<CargaHorariaDto> listImparticionClases) {
		
		if (!listImparticionClases.isEmpty()) {
			
			for (CargaHorariaDto item : listImparticionClases) {

				if (item.getCahrMateriaDto().getTpmtId() == TipoMateriaConstantes.TIPO_MODULO_VALUE) {
					item.getCahrParaleloDto().setMlcrprMatriculados(contarMatriculadosEnModular(cargarMlcrprIdPorPeriodo(item.getCahrMateriaDto().getMtrSubId(), item.getCahrParaleloDto().getPrlId())));
				}
				
				item.setCahrObservacion(etiquetarParaleloPrincipal(item.getCahrMateriaDto().getHracMlcrprIdComp(), listImparticionClases));
			}
			
		}
		
		return listImparticionClases;
	}
	
	
	private Integer cargarMlcrprIdPorPeriodo(int mtrPrincipalId, int paraleloId) {
		MateriaDto retorno = new MateriaDto(); 
		
		try {
			retorno = servJdbcMallaCurriculaMateriaDto.buscarModularPorParalelo(mtrPrincipalId, paraleloId);
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno.getMlcrprId();
	}
	
	
	private Integer contarMatriculadosEnModular(int mlcrprIdModular) {
		int retorno = 0;
		
		try {
			retorno = servJdbcMallaCurriculaMateriaDto.contarEstudiantesMatriculados(mlcrprIdModular);
		} catch (MallaCurricularMateriaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (MallaCurricularMateriaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	private String etiquetarParaleloPrincipal(Integer mlcrprPrincipalId, List<CargaHorariaDto> imparitcionClases) {
		
		if (mlcrprPrincipalId != null) {
			for (CargaHorariaDto cargaHoraria : imparitcionClases) {
				if (cargaHoraria.getCahrMateriaDto().getHracMlcrprId().equals(mlcrprPrincipalId)) {
					return "Principal (" + cargaHoraria.getCahrParaleloDto().getPrlDescripcion() + ")";
				}
			}
		}
		
		return "";
	}
	
	private void iniciarFormCargaHorariaPorFacultad(Usuario usuario) {
		rchfUsuario = usuario;
		
		rchfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		rchfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		rchfNombreArchivo = new String();
		rchfNombreReporte = new String();
		rchfTipoArchivo = new String();
		
		rchfListPersonaDatosDto = new ArrayList<>();
		rchfMapReporteGeneral = new ArrayList<>();
	}
	
	public void limpiarFormCargaHorariaPorFacultades(){
		rchfListPersonaDatosDto = new ArrayList<>();
		rchfMapReporteGeneral = new ArrayList<>();
		
		rchfPeriodoId = GeneralesConstantes.APP_ID_BASE;
		rchfDependenciaId = GeneralesConstantes.APP_ID_BASE;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;

		rchfNombreArchivo = new String();
		rchfNombreReporte = new String();
		rchfTipoArchivo = new String();
	}
	
	// ****************************************************************/
	// *********** METODOS GENERALES DE LA CLASE **********************/
	// ****************************************************************/
	public void buscarDocentesPorFacultadPeriodo(){
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		rchfListPersonaDatosDto = new ArrayList<>();
		rchfMapReporteGeneral = new ArrayList<>();

		if (!rchfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			if (!rchfDependenciaId.equals(GeneralesConstantes.APP_ID_BASE)) {
				
				try {
					
					rchfListPersonaDatosDto = servJdbcPersonaDatosDto.buscarDocentesPorFacultadPeriodo(rchfDependenciaId,  rchfPeriodoId);
					if (!rchfListPersonaDatosDto.isEmpty()) {
						List<Map<String, Object>>  registros  = new ArrayList<Map<String, Object>>();
						
						switch (rchfPeriodoId) {
						case 1:
							break;
						case 2:
							break;
						case 46:
							break;
						case 47:
							break;
						case 48:
							break;
						case 150:
							registros  = cargarDatosReporteGeneralCargaHoraria20182018();
							if (!registros.isEmpty()) {
								FacesUtil.limpiarMensaje();
								rchfMapReporteGeneral = registros;
							}else {
								FacesUtil.mensajeError("No se encontró resultados en el período acdémico seleccionado.");
							}
							break;
						case 350:
							registros  = cargarDatosReporteGeneralCargaHoraria20182019();
							if (!registros.isEmpty()) {
								FacesUtil.limpiarMensaje();
								rchfMapReporteGeneral = registros;
							}else {
								FacesUtil.mensajeError("No se encontró resultados en el período acdémico seleccionado.");
							}
							break;
						case 630:
							registros  = cargarDatosReporteGeneralCargaHoraria20192019();
							if (!registros.isEmpty()) {
								FacesUtil.limpiarMensaje();
								rchfMapReporteGeneral = registros;
							}else {
								FacesUtil.mensajeError("No se encontró resultados en el período acdémico seleccionado.");
							}
							break;
						}
						
					}
					
				
				} catch (PersonaDatosDtoNoEncontradoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (PersonaDatosDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				}
				
			}else {
				FacesUtil.mensajeError("Seleccione una facultad para continuar con la búsqueda.");
			}
		}else {
			FacesUtil.mensajeError("Seleccione un período académico para continuar con la búsqueda.");
		}
		
	}
	
	public void generarReporteIndividualPorDocente(PersonaDatosDto docente){
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		if (docente != null) {
			cargarAsignacionesCargaHoraria(docente.getPrsIdentificacion(), docente.getPrsPeriodoAcademicoDto(), rchfUsuario, rchfUsuario.getUsrIdentificacion());
			generarReporteIndividualCargaHoraria();
			rchfActivarReporte = 1;
		}
		
	}
	

	
	public void generarReporteIndividualCargaHoraria(){
		
//		if (rchfPeriodoId.equals(GeneralesConstantes.APP_ID_BASE)) {
			rchfTipoArchivo = "PDF_TABLE";
			
			switch (rchfPeriodoId) {
			case 1:
				break;
			case 2:
				break;
			case 46:
				break;
			case 47:
				break;
			case 48:
				break;
			case 150:// primer instructivo en siiu 
				activarReporteIndividual20182018();
				break;
			case 350:// instructivo agosto2018
				activarReporteIndividual20182019();
				break;
			case 630:// instructivo agosto2018 - inclusion de PAE
				activarReporteIndividual20192019();
				break;

			default:
				FacesUtil.mensajeInfo("Seleccione un período académico para continuar.");
				rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;	
				break;
			}
			
//		}else {
//			FacesUtil.mensajeInfo("Seleccione un período académico para continuar.");
//		}
			
	}
	
	
	private void activarReporteIndividual20182018() {
		CargaHoraria20182018 cargaPeriodo = new CargaHoraria20182018();

		List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(rchfDocenteDto.getPrsIdentificacion(), rchfPeriodoId);
		List<CargaHorariaDto> listFuncionesCargaHoraria = cargarFuncionesCargaHoraria(rchfDocenteDto.getPrsIdentificacion(), rchfPeriodoId);

		List<CargaHorariaDto> listHorarioGeneral = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);

		List<CargaHorariaDto> listHorarioConsolidado = cargaPeriodo.consolidarAsignaturasPrincipales(listHorarioGeneral);

		DependenciaDto dependencia = cargaPeriodo.establecerDependencia(listHorarioConsolidado);

		Integer [] imparticionClases = cargaPeriodo.cargarDatosImparticionClases(cargarInformacionPersonal(rchfDocenteDto.getPrsIdentificacion()), listHorarioConsolidado);
		Integer [] asignacionFunciones = cargaPeriodo.cargarDatosFuncionesCargaHoraria(listFuncionesCargaHoraria);

		cargarReporteIndividualCargaHoraria20182018(rchfUsuario, rchfDocenteDto, rchfDirectorDto, dependencia, rchfPeriodoAcademicoDto, imparticionClases, asignacionFunciones, listHorarioConsolidado);

		rchfNombreArchivo = "cargaHorariaDocente2018_2018";
		rchfNombreReporte = "CARGA_HORARIA_DOCENTE";
		rchfActivarReporte = 1;	
	}


	private void activarReporteIndividual20182019() {
		CargaHoraria20182019 cargaPeriodo = new CargaHoraria20182019();

		List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(rchfDocenteDto.getPrsIdentificacion(), rchfPeriodoId);
		List<CargaHorariaDto> listFuncionesCargaHoraria = cargarFuncionesCargaHoraria(rchfDocenteDto.getPrsIdentificacion(), rchfPeriodoId);

		List<CargaHorariaDto> listHorarioGeneral = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);

		List<CargaHorariaDto> listHorarioConsolidado = cargaPeriodo.consolidarAsignaturasPrincipales(listHorarioGeneral);

		DependenciaDto dependencia = cargaPeriodo.establecerDependencia(listHorarioConsolidado);

		Integer [] imparticionClases = cargaPeriodo.cargarDatosImparticionClases(cargarInformacionPersonal(rchfDocenteDto.getPrsIdentificacion()), listHorarioConsolidado);
		Integer [] asignacionFunciones = cargaPeriodo.cargarDatosFuncionesCargaHoraria(listFuncionesCargaHoraria);

		cargarReporteIndividualCargaHoraria20192019(rchfUsuario, rchfDocenteDto, rchfDirectorDto, dependencia, rchfPeriodoAcademicoDto, imparticionClases, asignacionFunciones, listHorarioConsolidado);

		rchfNombreArchivo = "cargaHorariaDocente2019_2019";
		rchfNombreReporte = "CARGA_HORARIA_DOCENTE";
		rchfActivarReporte = 1;	
		
	}


	private void activarReporteIndividual20192019() {
		CargaHoraria20192019 cargaPeriodo = new CargaHoraria20192019();

		List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(rchfDocenteDto.getPrsIdentificacion(), rchfPeriodoId);
		List<CargaHorariaDto> listFuncionesCargaHoraria = cargarFuncionesCargaHoraria(rchfDocenteDto.getPrsIdentificacion(), rchfPeriodoId);

		List<CargaHorariaDto> listHorarioGeneral = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);

		List<CargaHorariaDto> listHorarioConsolidado = cargaPeriodo.consolidarAsignaturasPrincipales(listHorarioGeneral);

		DependenciaDto dependencia = cargaPeriodo.establecerDependencia(listHorarioConsolidado);

		Integer [] imparticionClases = cargaPeriodo.cargarDatosImparticionClases(cargarInformacionPersonal(rchfDocenteDto.getPrsIdentificacion()), listHorarioConsolidado);
		Integer [] asignacionFunciones = cargaPeriodo.cargarDatosFuncionesCargaHoraria(listFuncionesCargaHoraria);

		cargarReporteIndividualCargaHoraria20192019(rchfUsuario, rchfDocenteDto, rchfDirectorDto, dependencia, rchfPeriodoAcademicoDto, imparticionClases, asignacionFunciones, listHorarioConsolidado);

		rchfNombreArchivo = "cargaHorariaDocente2019_2019";
		rchfNombreReporte = "CARGA_HORARIA_DOCENTE";
		rchfActivarReporte = 1;	
	}


	public void generarReporteGeneralCargaHoraria(){
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;	
		
		switch (rchfPeriodoId) {
		case 1:
			break;
		case 2:
			break;
		case 46:
			break;
		case 47:
			break;
		case 48:
			break;
		case 150:
			if (!rchfMapReporteGeneral.isEmpty()) {
				rchfTipoArchivo = "XLSX";
				rchfNombreArchivo = "generalCargaHorariaXLS";
				rchfNombreReporte = "CARGA_HORARIA_GENERAL";
				
				cargarReporteGeneral20182018(rchfMapReporteGeneral);
				
				rchfActivarReporte = 1;
			}
			break;
		case 350:// instructivo agosto2018
			if (!rchfMapReporteGeneral.isEmpty()) {
				rchfTipoArchivo = "XLSX";
				rchfNombreArchivo = "generalCargaHorariaXLS";
				rchfNombreReporte = "CARGA_HORARIA_GENERAL";
				
				cargarReporteGeneral20182019(rchfMapReporteGeneral);
				
				rchfActivarReporte = 1;
			}
			break;
		case 630:// instructivo agosto2018 - inclusion de PAE
			if (!rchfMapReporteGeneral.isEmpty()) {
				rchfTipoArchivo = "XLSX";
				rchfNombreArchivo = "generalCargaHorariaXLS";
				rchfNombreReporte = "CARGA_HORARIA_GENERAL";
				
				cargarReporteGeneral20182019(rchfMapReporteGeneral);
				
				rchfActivarReporte = 1;
			}
			break;

		default:
			FacesUtil.mensajeInfo("Seleccione un período académico para continuar.");
			rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;	
			break;
		}
		
		
	}
	
	
	
	
	
	private List<Map<String, Object>> cargarDatosReporteGeneralCargaHoraria20182018() {
		List<Map<String, Object>> campos = new ArrayList<Map<String, Object>>();
		CargaHoraria20182018 cargaPeriodo = new CargaHoraria20182018();

		
		for (PersonaDatosDto item: rchfListPersonaDatosDto) {
			Map<String, Object> dato = new HashMap<String, Object>();

			List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(item.getPrsIdentificacion(), rchfPeriodoId);
			List<CargaHorariaDto> listFuncionesCargaHoraria = cargarFuncionesCargaHoraria(item.getPrsIdentificacion(), rchfPeriodoId);

			List<CargaHorariaDto> listHorarioGeneral = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);

			List<CargaHorariaDto> listHorarioConsolidado = cargaPeriodo.consolidarAsignaturasPrincipales(listHorarioGeneral);

			DependenciaDto dependencia = cargaPeriodo.establecerDependencia(listHorarioConsolidado);

			PersonaDto docente = new PersonaDto();
			docente.setRllbId(item.getRllbId());
			docente.setTmddId(item.getTmddId());
			docente.setPrsIdentificacion(item.getPrsIdentificacion());
			
			Integer [] imparticionClases = cargaPeriodo.cargarDatosImparticionClases(docente, listHorarioConsolidado);
			Integer [] asignacionFunciones = cargaPeriodo.cargarDatosFuncionesCargaHoraria(listFuncionesCargaHoraria);

			int a = Arrays.stream(imparticionClases).mapToInt(Integer::intValue).sum();
			int b = Arrays.stream(asignacionFunciones).mapToInt(Integer::intValue).sum();

			dato.put("dependencia", dependencia.getDpnDescripcion());
			dato.put("carrera", item.getDtpsCrrDescripcion());
			dato.put("identificacion",  item.getPrsIdentificacion());
			dato.put("docente", item.getPrsPrimerApellido()+" "+ item.getPrsSegundoApellido() + " "+ item.getPrsNombres());
			dato.put("relacion_laboral",  item.getRllbDescripcion());
			dato.put("tiempo_dedicacion", item.getTmddDescripcion());
			dato.put("categoria", CargaHorariaTemplate.establecerCategoria(item.getPstCategoria(), item.getPstNivelRangoGradual()));

			dato.put("total_horas", (a + b - imparticionClases[4] - imparticionClases[5]));
			dato.put("total_paralelos", imparticionClases[4]);
			dato.put("total_matriculados", imparticionClases[5]);
			
			dato.put("param01", imparticionClases[0]);//imparticion_clase
			dato.put("param02", imparticionClases[1]);//preparacion_clase
			dato.put("param03", imparticionClases[2]);//preparacion_examen
			dato.put("param04", imparticionClases[3]);//tutoria_academica
			
			dato.put("param05", asignacionFunciones[0]);//clinica_pasantia
			dato.put("param06", asignacionFunciones[1]);//investigacion_avanzada
			dato.put("param07", asignacionFunciones[2]);//proyecto_semilla
			dato.put("param08", asignacionFunciones[3]);//vinculacion
			dato.put("param09", asignacionFunciones[4]);//titulacion
			dato.put("param10", asignacionFunciones[5]);//examen_complexivo
			dato.put("param11", asignacionFunciones[6]);//coordinador
			dato.put("param12", asignacionFunciones[7]);//doctorados
			dato.put("param13", asignacionFunciones[8]);//direccion_gestion
			dato.put("param14", asignacionFunciones[9]);//comite_subcomite
			dato.put("param15", asignacionFunciones[10]);//aseguramient_calidad
			dato.put("param16", asignacionFunciones[11]);//redes_academicas
			dato.put("param17", asignacionFunciones[12]);//lector_proyecto
			dato.put("param18", asignacionFunciones[13]);//capacitacion_docente
			dato.put("param19", asignacionFunciones[14]);//publicacion_academica
			dato.put("param20", asignacionFunciones[15]);//posgrado

			
			campos.add(dato);
		}

		return campos;
	}
	
	
	
	private List<Map<String, Object>> cargarDatosReporteGeneralCargaHoraria20182019() {
		List<Map<String, Object>> campos = new ArrayList<Map<String, Object>>();
		CargaHoraria20182019 cargaPeriodo = new CargaHoraria20182019();

		
		for (PersonaDatosDto item: rchfListPersonaDatosDto) {
			Map<String, Object> dato = new HashMap<String, Object>();

			List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(item.getPrsIdentificacion(), rchfPeriodoId);
			List<CargaHorariaDto> listFuncionesCargaHoraria = cargarFuncionesCargaHoraria(item.getPrsIdentificacion(), rchfPeriodoId);

			List<CargaHorariaDto> listHorarioGeneral = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);

			List<CargaHorariaDto> listHorarioConsolidado = cargaPeriodo.consolidarAsignaturasPrincipales(listHorarioGeneral);

			DependenciaDto dependencia = cargaPeriodo.establecerDependencia(listHorarioConsolidado);

			PersonaDto docente = new PersonaDto();
			docente.setRllbId(item.getRllbId());
			docente.setTmddId(item.getTmddId());
			docente.setPrsIdentificacion(item.getPrsIdentificacion());
			
			Integer [] imparticionClases = cargaPeriodo.cargarDatosImparticionClases(docente, listHorarioConsolidado);
			Integer [] asignacionFunciones = cargaPeriodo.cargarDatosFuncionesCargaHoraria(listFuncionesCargaHoraria);

			int a = Arrays.stream(imparticionClases).mapToInt(Integer::intValue).sum();
			int b = Arrays.stream(asignacionFunciones).mapToInt(Integer::intValue).sum();

			dato.put("dependencia", dependencia.getDpnDescripcion());
			dato.put("carrera", item.getDtpsCrrDescripcion());
			dato.put("identificacion",  item.getPrsIdentificacion());
			dato.put("docente", item.getPrsPrimerApellido()+" "+ item.getPrsSegundoApellido() + " "+ item.getPrsNombres());
			dato.put("relacion_laboral",  item.getRllbDescripcion());
			dato.put("tiempo_dedicacion", item.getTmddDescripcion());
			dato.put("categoria", CargaHorariaTemplate.establecerCategoria(item.getPstCategoria(), item.getPstNivelRangoGradual()));

			dato.put("total_horas", (a + b - imparticionClases[6] - imparticionClases[5]));
			dato.put("total_paralelos", imparticionClases[5]);
			dato.put("total_matriculados", imparticionClases[6]);
			
			dato.put("param01", imparticionClases[0]);//imparticion_clase
			dato.put("param02", imparticionClases[1]);//preparacion_clase
			dato.put("param03", imparticionClases[2]);//preparacion_examen
			dato.put("param04", imparticionClases[3]);//tutoria_academica
			dato.put("param05", imparticionClases[4]);//practica_experimentacion

			dato.put("param06", asignacionFunciones[0]);//clinica_pasantia
			dato.put("param07", asignacionFunciones[1]);//vinculacion
			dato.put("param08", asignacionFunciones[2]);//titulacion
			dato.put("param09", asignacionFunciones[3]);//investigacion_avanzada
			dato.put("param10", asignacionFunciones[4]);//proyecto_semilla
			dato.put("param11", asignacionFunciones[5]);//otra_actividad_investigacion
			dato.put("param12", asignacionFunciones[6]);//otro_proyecto_investigacion
			dato.put("param13", asignacionFunciones[7]);//direccion
			dato.put("param14", asignacionFunciones[9]);//representacion
			dato.put("param15", asignacionFunciones[10]);//otra_actividad_vap
			dato.put("param16", asignacionFunciones[11]);//coordinador

			campos.add(dato);
		}

		return campos;
	}
	
	
	

	private List<Map<String, Object>> cargarDatosReporteGeneralCargaHoraria20192019() {
		List<Map<String, Object>> campos = new ArrayList<Map<String, Object>>();
		
		CargaHoraria20192019 cargaPeriodo = new CargaHoraria20192019();
		
		for (PersonaDatosDto item: rchfListPersonaDatosDto) {
			Map<String, Object> dato = new HashMap<String, Object>();

			List<CargaHorariaDto> listImparticionClases = cargarImparticionClases(item.getPrsIdentificacion(), rchfPeriodoId);
			List<CargaHorariaDto> listFuncionesCargaHoraria = cargarFuncionesCargaHoraria(item.getPrsIdentificacion(), rchfPeriodoId);
			
			List<CargaHorariaDto> listHorarioGeneral = cargarMatriculadosPorModularEtiquetarHorarioPrincipal(listImparticionClases);
			
			List<CargaHorariaDto> listHorarioConsolidado = cargaPeriodo.consolidarAsignaturasPrincipales(listHorarioGeneral);
			
			DependenciaDto dependencia = cargaPeriodo.establecerDependencia(listHorarioConsolidado);
			
			Integer [] imparticionClases = cargaPeriodo.cargarDatosImparticionClases(cargarInformacionPersonal(item.getPrsIdentificacion()), listHorarioConsolidado);
			Integer [] asignacionFunciones = cargaPeriodo.cargarDatosFuncionesCargaHoraria(listFuncionesCargaHoraria);
		
			int a = Arrays.stream(imparticionClases).mapToInt(Integer::intValue).sum();
			int b = Arrays.stream(asignacionFunciones).mapToInt(Integer::intValue).sum();
			
			dato.put("dependencia", dependencia.getDpnDescripcion());
			dato.put("carrera", item.getDtpsCrrDescripcion());
			dato.put("identificacion",  item.getPrsIdentificacion());
			dato.put("docente", item.getPrsPrimerApellido()+" "+ item.getPrsSegundoApellido() + " "+ item.getPrsNombres());
			dato.put("relacion_laboral",  item.getRllbDescripcion());
			dato.put("tiempo_dedicacion", item.getTmddDescripcion());
			dato.put("categoria", CargaHorariaTemplate.establecerCategoria(item.getPstCategoria(), item.getPstNivelRangoGradual()));
			
			dato.put("total_horas", (a + b - imparticionClases[6] - imparticionClases[5]));
			dato.put("total_paralelos", imparticionClases[5]);
			dato.put("total_matriculados", imparticionClases[6]);
			
			dato.put("param01", imparticionClases[0]);//imparticion_clase
			dato.put("param02", imparticionClases[1]);//preparacion_clase
			dato.put("param03", imparticionClases[2]);//preparacion_examen
			dato.put("param04", imparticionClases[3]);//tutoria_academica
			dato.put("param05", imparticionClases[4]);//practica_experimentacion

			dato.put("param06", asignacionFunciones[0]);//clinica_pasantia
			dato.put("param07", asignacionFunciones[1]);//vinculacion
			dato.put("param08", asignacionFunciones[2]);//titulacion
			dato.put("param09", asignacionFunciones[3]);//investigacion_avanzada
			dato.put("param10", asignacionFunciones[4]);//proyecto_semilla
			dato.put("param11", asignacionFunciones[5]);//otra_actividad_investigacion
			dato.put("param12", asignacionFunciones[6]);//otro_proyecto_investigacion
			dato.put("param13", asignacionFunciones[7]);//direccion
			dato.put("param14", asignacionFunciones[9]);//representacion
			dato.put("param15", asignacionFunciones[10]);//otra_actividad_vap
			dato.put("param16", asignacionFunciones[11]);//coordinador
			
			campos.add(dato);
		}
		
		return campos;
	}

	
	private void cargarReporteIndividualCargaHoraria20182018(Usuario usuario, PersonaDto docente,  PersonaDto  director, DependenciaDto dependencia, PeriodoAcademicoDto periodo, Integer[] imparticionClases , Integer[] asignacionFunciones, List<CargaHorariaDto> horarioClases){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CARGA HORARIA";

		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO "+ periodo.getPracDescripcion());
		frmRrmParametros.put("docente", docente.getPrsNombres() + " "+ docente.getPrsPrimerApellido() + " "+ docente.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  docente.getPrsIdentificacion());
		frmRrmParametros.put("facultad", dependencia != null ? dependencia.getDpnDescripcion():"");
		frmRrmParametros.put("relacion_laboral",  docente.getRllbDescripcion());
		frmRrmParametros.put("categoria", CargaHorariaTemplate.establecerCategoria(docente.getPstCategoria(), docente.getPstNivelRangoGradual()));
		frmRrmParametros.put("tiempo_dedicacion", docente.getTmddDescripcion());

		frmRrmParametros.put("param01", imparticionClases[0]);//imparticion_clase
		frmRrmParametros.put("param02", imparticionClases[1]);//preparacion_clase
		frmRrmParametros.put("param03", imparticionClases[2]);//preparacion_examen
		frmRrmParametros.put("param04", imparticionClases[3]);//tutoria_academica
		
		frmRrmParametros.put("param05", asignacionFunciones[0]);//clinica_pasantia
		frmRrmParametros.put("param06", asignacionFunciones[1]);//investigacion_avanzada
		frmRrmParametros.put("param07", asignacionFunciones[2]);//proyecto_semilla
		frmRrmParametros.put("param08", asignacionFunciones[3]);//vinculacion
		frmRrmParametros.put("param09", asignacionFunciones[4]);//titulacion
		frmRrmParametros.put("param10", asignacionFunciones[5]);//examen_complexivo
		frmRrmParametros.put("param11", asignacionFunciones[6]);//coordinador
		frmRrmParametros.put("param12", asignacionFunciones[7]);//doctorados
		frmRrmParametros.put("param13", asignacionFunciones[8]);//direccion_gestion
		frmRrmParametros.put("param14", asignacionFunciones[9]);//comite_subcomite
		frmRrmParametros.put("param15", asignacionFunciones[10]);//aseguramient_calidad
		frmRrmParametros.put("param16", asignacionFunciones[11]);//redes_academicas
		frmRrmParametros.put("param17", asignacionFunciones[12]);//lector_proyecto
		frmRrmParametros.put("param18", asignacionFunciones[13]);//capacitacion_docente
		frmRrmParametros.put("param19", asignacionFunciones[14]);//publicacion_academica
		frmRrmParametros.put("param20", asignacionFunciones[15]);//posgrado
		
		int a = Arrays.stream(imparticionClases).mapToInt(Integer::intValue).sum();
		int b = Arrays.stream(asignacionFunciones).mapToInt(Integer::intValue).sum();
		
		frmRrmParametros.put("sumatoria", (a + b - imparticionClases[4] - imparticionClases[5]));

		
		frmRrmParametros.put("cargo_label_1", "DOCENTE");
		frmRrmParametros.put("cargo_value_1", "");
		if (docente != null) {
			frmRrmParametros.put("cargo_value_1", docente.getPrsApellidosNombres());
		}
		
		frmRrmParametros.put("cargo_label_2", "DIRECTOR(A) DE CARRERA");
		frmRrmParametros.put("cargo_value_2", "");
		if (director != null) {
			frmRrmParametros.put("cargo_value_2", director.getPrsApellidosNombres());
		}
		
		frmRrmParametros.put("ItemDataSource", new JRBeanCollectionDataSource(horarioClases));
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = new HashMap<String, Object>();
		frmRrmCampos.add(datoHorario);
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte +"_"+ docente.getPrsIdentificacion());
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	}
	
	

	private void cargarReporteIndividualCargaHoraria20192019(Usuario usuario, PersonaDto docente,  PersonaDto  director, DependenciaDto dependencia, PeriodoAcademicoDto periodo, Integer[] imparticionClases , Integer[] asignacionFunciones, List<CargaHorariaDto> horarioClases){

		Map<String, Object> frmRrmParametros = null;
		List<Map<String, Object>> frmRrmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CARGA HORARIA";

		frmRrmParametros = new HashMap<String, Object>();
		frmRrmParametros.put("imagen_cabecera", pathGeneralReportes  + PATH_GENERAL_IMG_CABECERA);
		frmRrmParametros.put("imagen_pie", pathGeneralReportes + PATH_GENERAL_IMG_PIE);
		frmRrmParametros.put("encabezado_institucion", GENERAL_NOMBRE_INSTITUCION);
		frmRrmParametros.put("encabezado_reporte", encabezadoReporte);
		frmRrmParametros.put("pie_pagina", GENERAL_PIE_PAGINA);
		frmRrmParametros.put("usuario", usuario.getUsrNick());
		frmRrmParametros.put("leyenda", GENERAL_DOC_AUTOGENERADO + GeneralesUtilidades.cambiarDateToStringFormatoFecha(GeneralesUtilidades.getFechaActualSistema(), "dd/MM/yyyy HH:mm:ss"));
		
		frmRrmParametros.put("periodo", "PERÍODO ACADÉMICO "+ periodo.getPracDescripcion());
		frmRrmParametros.put("docente", docente.getPrsNombres() + " "+ docente.getPrsPrimerApellido() + " "+ docente.getPrsSegundoApellido());
		frmRrmParametros.put("identificacion",  docente.getPrsIdentificacion());
		frmRrmParametros.put("facultad", dependencia != null ? dependencia.getDpnDescripcion():"");
		frmRrmParametros.put("relacion_laboral",  docente.getRllbDescripcion());
		frmRrmParametros.put("categoria", CargaHorariaTemplate.establecerCategoria(docente.getPstCategoria(), docente.getPstNivelRangoGradual()));
		frmRrmParametros.put("tiempo_dedicacion", docente.getTmddDescripcion());

		frmRrmParametros.put("param01", imparticionClases[0]);
		frmRrmParametros.put("param02", imparticionClases[1]);
		frmRrmParametros.put("param03", imparticionClases[2]);
		frmRrmParametros.put("param04", imparticionClases[3]);
		frmRrmParametros.put("param05", imparticionClases[4]);
		frmRrmParametros.put("param06", asignacionFunciones[0]);
		frmRrmParametros.put("param07", asignacionFunciones[1]);
		frmRrmParametros.put("param08", asignacionFunciones[2]);
		frmRrmParametros.put("param09", asignacionFunciones[3]);
		frmRrmParametros.put("param10", asignacionFunciones[4]);
		frmRrmParametros.put("param11", asignacionFunciones[5]);
		frmRrmParametros.put("param12", asignacionFunciones[6]);
		frmRrmParametros.put("param13", asignacionFunciones[7]);
		frmRrmParametros.put("param14", asignacionFunciones[8]);
		frmRrmParametros.put("param15", asignacionFunciones[9]);
		frmRrmParametros.put("param16", asignacionFunciones[10]);
		frmRrmParametros.put("param17", asignacionFunciones[11]);

		int a = Arrays.stream(imparticionClases).mapToInt(Integer::intValue).sum();
		int b = Arrays.stream(asignacionFunciones).mapToInt(Integer::intValue).sum();
		
		int suma_docencia = a + asignacionFunciones[0] + asignacionFunciones[1] + asignacionFunciones[2] - imparticionClases[6] - imparticionClases[5];
		int suma_investigacion = asignacionFunciones[3]+asignacionFunciones[4]+asignacionFunciones[5]+asignacionFunciones[6];
		int suma_direccion_gestion = asignacionFunciones[7] + asignacionFunciones[8] + asignacionFunciones[9] + asignacionFunciones[10] + asignacionFunciones[11];
		
		frmRrmParametros.put("sumatoria", (a + b - imparticionClases[6] - imparticionClases[5]));
		frmRrmParametros.put("total_docencia", suma_docencia);
		frmRrmParametros.put("total_investigacion", suma_investigacion);
		frmRrmParametros.put("total_direccion_gestion", suma_direccion_gestion);
		
		frmRrmParametros.put("cargo_label_1", "DOCENTE");
		frmRrmParametros.put("cargo_value_1", "");
		if (docente != null) {
			frmRrmParametros.put("cargo_value_1", docente.getPrsApellidosNombres());
		}
		
		frmRrmParametros.put("cargo_label_2", "DIRECTOR(A) DE CARRERA");
		frmRrmParametros.put("cargo_value_2", "");
		if (director != null) {
			frmRrmParametros.put("cargo_value_2", director.getPrsApellidosNombres());
		}
		
		frmRrmParametros.put("ItemDataSource", new JRBeanCollectionDataSource(horarioClases));
		
		frmRrmCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> datoHorario = new HashMap<String, Object>();
		frmRrmCampos.add(datoHorario);
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmRrmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte +"_"+ docente.getPrsIdentificacion());
		httpSession.setAttribute("frmCargaParametros",frmRrmParametros);
	}
	
	private void cargarReporteGeneral20182018(List<Map<String, Object>> campos){

		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CARGA HORARIA GENERAL";

		frmParametros = new HashMap<String, Object>();
		frmParametros.put("titulo", encabezadoReporte);
		frmParametros.put("fecha", GeneralesUtilidades.getFechaActualSistema().toString());
		
		frmParametros.put("param01_label", "IMPARTICIÓN CLASES");
		frmParametros.put("param02_label", "PREPARACIÓN CLASES");
		frmParametros.put("param03_label", "PREPARACIÓN EXAMENES");
		frmParametros.put("param04_label", "TUTORÍAS ACADÉMICAS");
		frmParametros.put("param05_label", "CLÍNICAS Y PASANTÍAS");
		frmParametros.put("param06_label", "INVESTIGACION AVANZADA");
		frmParametros.put("param07_label", "PROYECTOS SEMILLA");
		frmParametros.put("param08_label", "VINCULACIÓN");
		frmParametros.put("param09_label", "TITULACIÓN");
		frmParametros.put("param10_label", "EXÁMENES COMPLEXIVOS");
		frmParametros.put("param11_label", "COORDINADOR");
		frmParametros.put("param12_label", "DOCTORADOS");
		frmParametros.put("param13_label", "DIRECCIÓN Y GESTIÓN");
		frmParametros.put("param14_label", "COMITÉS Y SUBCOMITÉS");
		frmParametros.put("param15_label", "ASEGURAMIENTO DE LA CALIDAD");
		frmParametros.put("param16_label", "REDES ACADÉMICAS");
		frmParametros.put("param17_label", "LECOR DE PROYECTO");
		frmParametros.put("param18_label", "CAPACITACIÓN DOCENTE");
		frmParametros.put("param19_label", "PUBLICACIÓN ACADÉMICA");
		frmParametros.put("param20_label", "POSGRADO");

		frmCampos = campos;
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	}
	
	private void cargarReporteGeneral20182019(List<Map<String, Object>> campos){

		Map<String, Object> frmParametros = null;
		List<Map<String, Object>> frmCampos = null;
		
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		String encabezadoReporte = "CARGA HORARIA GENERAL";

		frmParametros = new HashMap<String, Object>();
		frmParametros.put("titulo", encabezadoReporte);
		frmParametros.put("fecha", GeneralesUtilidades.getFechaActualSistema().toString());
		
		frmParametros.put("param01_label", "IMPARTICIÓN CLASES");
		frmParametros.put("param02_label", "PREPARACIÓN CLASES");
		frmParametros.put("param03_label", "PREPARACIÓN EXAMENES");
		frmParametros.put("param04_label", "TUTORÍAS ACADÉMICAS");
		frmParametros.put("param05_label", "PRÁCTICAS DE EXPERIMENACIÓN");
		frmParametros.put("param06_label", "CLÍNICAS Y PASANTÍAS");
		frmParametros.put("param07_label", "VINCULACIÓN");
		frmParametros.put("param08_label", "TITULACIÓN");
		frmParametros.put("param09_label", "INVESTIGACION AVANZADA");
		frmParametros.put("param10_label", "PROYECTOS SEMILLA");
		frmParametros.put("param11_label", "OTRAS ACTIVIDADES INVESTIGACIÓN");
		frmParametros.put("param12_label", "OTROS PROYECTOS INVESTIGACIÓN");
		frmParametros.put("param13_label", "DIRECCIÓN");
		frmParametros.put("param14_label", "REPRESENTACIONES");
		frmParametros.put("param15_label", "OTRAS ACTIVIDADES VAP");
		frmParametros.put("param16_label", "COORDINADOR");

		frmCampos = campos;
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCampos);
		httpSession.setAttribute("frmCargaNombreReporte", encabezadoReporte);
		httpSession.setAttribute("frmCargaParametros",frmParametros);
	}
	
	
	private List<CargaHorariaDto> cargarFuncionesCargaHoraria(String docenteIdentificacion, Integer periodoId) {
		List<CargaHorariaDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcCargaHorariaDto.buscarFuncionesPorDocente(docenteIdentificacion, periodoId);
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError("El docente no dispone Carga Horaria - Asignación de Funciones.");
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}

	private List<CargaHorariaDto> cargarImparticionClases(String docenteIdentificacion, int periodoId) {
		List<CargaHorariaDto> retorno = new ArrayList<>();

		try{
			retorno = servJdbcCargaHorariaDto.buscarImparticionClasesPorDocente(docenteIdentificacion, periodoId);
		} catch (CargaHorariaNoEncontradoException e) {
			FacesUtil.mensajeError("El docente no dispone Carga Horaria - Impartición Clases.");
		} catch (CargaHorariaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}

		return retorno;
	}
	

	public void cargarAsignacionesCargaHoraria(String identificacionDocente, PeriodoAcademicoDto periodo, Usuario usuarioLogin, String identificacionDirector) {

		rchfUsuario = usuarioLogin;
		rchfPeriodoAcademicoDto = periodo;
		rchfActivarReporte = GeneralesConstantes.APP_ID_BASE;
		
		rchfPeriodoId = periodo.getPracId();
		
		rchfDocenteDto = cargarInformacionPersonal(identificacionDocente);
		rchfDirectorDto = cargarDirectorCarrera(identificacionDirector);
		

	}

	
	private PersonaDto cargarDirectorCarrera(String identificacion){
		
		PersonaDto director  = null;
		try {
			director = servJdbcPersonaDto.buscarPersonaPorIdentificacionRol(RolConstantes.ROL_DIRCARRERA_VALUE, identificacion);
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("Su usuario no dispone un rol de Director de Carrera.");
		} catch (PersonaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return director;
	}

	private PersonaDto cargarInformacionPersonal(String identificacion) {
		PersonaDto retorno = null;
		
		try{
			if (identificacion != null) {
				retorno = servJdbcCargaHorariaDto.buscarInformacionDocente(identificacion);
			}
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError("Error al buscar al Docente.");
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	// ****************************************************************/
	// ************************* ENCAPSULAMIENTO **********************/
	// ****************************************************************/

	
	public PeriodoAcademicoDto getRchfPeriodoAcademicoDto() {
		return rchfPeriodoAcademicoDto;
	}

	public void setRchfPeriodoAcademicoDto(PeriodoAcademicoDto rchfPeriodoAcademicoDto) {
		this.rchfPeriodoAcademicoDto = rchfPeriodoAcademicoDto;
	}

	public Integer getRchfPeriodoId() {
		return rchfPeriodoId;
	}

	public void setRchfPeriodoId(Integer rchfPeriodoId) {
		this.rchfPeriodoId = rchfPeriodoId;
	}

	public String getRchfNombreArchivo() {
		return rchfNombreArchivo;
	}

	public void setRchfNombreArchivo(String rchfNombreArchivo) {
		this.rchfNombreArchivo = rchfNombreArchivo;
	}

	public String getRchfNombreReporte() {
		return rchfNombreReporte;
	}

	public void setRchfNombreReporte(String rchfNombreReporte) {
		this.rchfNombreReporte = rchfNombreReporte;
	}

	public String getRchfTipoArchivo() {
		return rchfTipoArchivo;
	}

	public void setRchfTipoArchivo(String rchfTipoArchivo) {
		this.rchfTipoArchivo = rchfTipoArchivo;
	}


	public List<PeriodoAcademicoDto> getRchfListPeriodoAcademicoDto() {
		return rchfListPeriodoAcademicoDto;
	}

	public void setRchfListPeriodoAcademicoDto(List<PeriodoAcademicoDto> rchfListPeriodoAcademicoDto) {
		this.rchfListPeriodoAcademicoDto = rchfListPeriodoAcademicoDto;
	}

	public List<CargaHorariaDto> getRchfListFuncionesPeriodoCargaHorariaDto() {
		return rchfListFuncionesPeriodoCargaHorariaDto;
	}

	public void setRchfListFuncionesPeriodoCargaHorariaDto(List<CargaHorariaDto> rchfListFuncionesPeriodoCargaHorariaDto) {
		this.rchfListFuncionesPeriodoCargaHorariaDto = rchfListFuncionesPeriodoCargaHorariaDto;
	}

	public List<CargaHorariaDto> getRchfListFuncionesDocenteCargaHorariaDto() {
		return rchfListFuncionesDocenteCargaHorariaDto;
	}

	public void setRchfListFuncionesDocenteCargaHorariaDto(List<CargaHorariaDto> rchfListFuncionesDocenteCargaHorariaDto) {
		this.rchfListFuncionesDocenteCargaHorariaDto = rchfListFuncionesDocenteCargaHorariaDto;
	}

	public List<CargaHorariaDto> getRchfListImparticionClasesCargaHorariaDto() {
		return rchfListImparticionClasesCargaHorariaDto;
	}

	public void setRchfListImparticionClasesCargaHorariaDto(
			List<CargaHorariaDto> rchfListImparticionClasesCargaHorariaDto) {
		this.rchfListImparticionClasesCargaHorariaDto = rchfListImparticionClasesCargaHorariaDto;
	}

	public List<CargaHorariaDto> getRchfListCargaHorariaDto() {
		return rchfListCargaHorariaDto;
	}

	public void setRchfListCargaHorariaDto(List<CargaHorariaDto> rchfListCargaHorariaDto) {
		this.rchfListCargaHorariaDto = rchfListCargaHorariaDto;
	}

	public Integer getRchfActivarReporte() {
		return rchfActivarReporte;
	}

	public void setRchfActivarReporte(Integer rchfActivarReporte) {
		this.rchfActivarReporte = rchfActivarReporte;
	}

	public Usuario getRchfUsuario() {
		return rchfUsuario;
	}

	public void setRchfUsuario(Usuario rchfUsuario) {
		this.rchfUsuario = rchfUsuario;
	}



	public Integer[] getRchfImparticionClases() {
		return rchfImparticionClases;
	}

	public void setRchfImparticionClases(Integer[] rchfImparticionClases) {
		this.rchfImparticionClases = rchfImparticionClases;
	}

	public Integer[] getRchfAsignacionFunciones() {
		return rchfAsignacionFunciones;
	}

	public void setRchfAsignacionFunciones(Integer[] rchfAsignacionFunciones) {
		this.rchfAsignacionFunciones = rchfAsignacionFunciones;
	}


	public DependenciaDto getRchfDependenciaDto() {
		return rchfDependenciaDto;
	}


	public void setRchfDependenciaDto(DependenciaDto rchfDependenciaDto) {
		this.rchfDependenciaDto = rchfDependenciaDto;
	}


	public PersonaDto getRchfDirectorDto() {
		return rchfDirectorDto;
	}


	public void setRchfDirectorDto(PersonaDto rchfDirectorDto) {
		this.rchfDirectorDto = rchfDirectorDto;
	}


	public Integer getRchfDependenciaId() {
		return rchfDependenciaId;
	}

	public void setRchfDependenciaId(Integer rchfDependenciaId) {
		this.rchfDependenciaId = rchfDependenciaId;
	}


	public PersonaDatosDtoServicioJdbc getServJdbcPersonaDatosDto() {
		return servJdbcPersonaDatosDto;
	}

	public void setServJdbcPersonaDatosDto(PersonaDatosDtoServicioJdbc servJdbcPersonaDatosDto) {
		this.servJdbcPersonaDatosDto = servJdbcPersonaDatosDto;
	}

	public List<PersonaDatosDto> getRchfListPersonaDatosDto() {
		return rchfListPersonaDatosDto;
	}

	public void setRchfListPersonaDatosDto(List<PersonaDatosDto> rchfListPersonaDatosDto) {
		this.rchfListPersonaDatosDto = rchfListPersonaDatosDto;
	}

	public List<Dependencia> getRchfListDependencia() {
		return rchfListDependencia;
	}

	public void setRchfListDependencia(List<Dependencia> rchfListDependencia) {
		this.rchfListDependencia = rchfListDependencia;
	}

	public PersonaDto getRchfDocenteDto() {
		return rchfDocenteDto;
	}

	public void setRchfDocenteDto(PersonaDto rchfDocenteDto) {
		this.rchfDocenteDto = rchfDocenteDto;
	}


	public List<Map<String, Object>> getRchfMapReporteGeneral() {
		return rchfMapReporteGeneral;
	}


	public void setRchfMapReporteGeneral(List<Map<String, Object>> rchfMapReporteGeneral) {
		this.rchfMapReporteGeneral = rchfMapReporteGeneral;
	}

	
	
}
