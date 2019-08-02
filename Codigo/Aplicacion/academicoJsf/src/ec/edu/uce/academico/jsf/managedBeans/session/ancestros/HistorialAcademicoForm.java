package ec.edu.uce.academico.jsf.managedBeans.session.ancestros;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.MateriaServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.GratuidadServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.HistorialAcademicoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.SuficienciaInformaticaServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MatriculaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.NivelConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SuficienciaInformaticaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jsf.managedBeans.request.generales.ListasCombosForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

@ManagedBean(name="historialAcademicoForm")
@SessionScoped
public class HistorialAcademicoForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB private CarreraServicio servCarrera;
	@EJB private MateriaServicio servMateria;
	@EJB private HistorialAcademicoServicioJdbc servJdbcHistorialAcademico;
	@EJB private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB private GratuidadServicioJdbc servJdbcGratuidad;
	@EJB private SuficienciaInformaticaServicioJdbc servJdbcSuficienciaInformatica;
	
	
	/**
	 * Método que permite obtener el periodo en el que aprobo la suficiencia en Idiomas.
	 * @author fgguzman
	 * @param historial en idiomas.
	 * @return RecordEstudianteDto
	 */
	public RecordEstudianteDto verificarEstadoSuficienciaIdiomas(List<RecordEstudianteDto> historial){
		RecordEstudianteDto retorno = null;
		
		if (!historial.isEmpty()) {
			for (RecordEstudianteDto item : historial) {
				if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU)
						&& (item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL) || item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL))
						&& (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_APROBACION_VALUE) || item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_CUARTO_VALUE) )) {
					retorno = item;
				}
			}
		}
		
		return retorno;
	}
	
	
	
	
	/**
	 * Método que permite obtener el periodo en el que aprobo la suficiencia en cultura física.
	 * @author fgguzman
	 * @param historial en actividad fisica.
	 * @return RecordEstudianteDto si aprobo.
	 */
	public RecordEstudianteDto verificarEstadoSuficienciaActividadFisica(List<RecordEstudianteDto> historial){
		RecordEstudianteDto retorno = null;
		
		if (!historial.isEmpty()) {
			retorno = verificacionEstadosActividadFisicaSIIU(historial);//-1,2
			if (retorno == null) {
				retorno = verificacionEstadosActividadFisicaSAU(historial);// AFR y 2 Deportes sin SIIU 	
				if (retorno == null) {
					retorno = verificacionEstadoActividadFisicaMigracionSIIU(historial);//si prac_id = 210 y un deporte cualquiera o AFR mas 2do SIIU. 	
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite verificar si aprobo la suficiencia en SAU / SIIU por migración.
	 * - Periodo 210 -> cualquier deporte en SAU mas el 2do en SIIU.
	 * - Periodo 210 reprobado y superiores -> AFR mas segundo en SIIU
	 * @author fgguzman
	 * @param historial
	 * @return RecordEstudianteDto
	 */
	private RecordEstudianteDto verificacionEstadoActividadFisicaMigracionSIIU(List<RecordEstudianteDto> historial) {
		RecordEstudianteDto retorno = null;

		boolean migradoAlSIIU = false;
		boolean matriculaEnMigracionSIIU = false;
		for (RecordEstudianteDto item : historial) {

			if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU)){
				migradoAlSIIU = true;
				if (item.getRcesPeriodoAcademicoDto().getPracId() == PeriodoAcademicoConstantes.PRAC_PERIODO_MIGRACION_SUFICIENCIA_CULTURA_FISICA_VALUE) {
					matriculaEnMigracionSIIU = true;
				}
			}

		}	

		int parteA = 0;
		if (migradoAlSIIU && matriculaEnMigracionSIIU) {
			for (RecordEstudianteDto item : historial) {

				if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SAU)
						&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
					parteA = parteA  + 1;
					if (retorno != null) {
						if (retorno.getRcesNivelDto().getNvlNumeral() < item.getRcesNivelDto().getNvlNumeral()) {
							retorno = item;
						}
					}else {
						retorno = item;
					}
				}
				
				if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU)
						&& item.getRcesPeriodoAcademicoDto().getPracId() == PeriodoAcademicoConstantes.PRAC_PERIODO_MIGRACION_SUFICIENCIA_CULTURA_FISICA_VALUE
						&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
					parteA = parteA  + 1;
					if (retorno != null) {
						if (retorno.getRcesNivelDto().getNvlNumeral() < item.getRcesNivelDto().getNvlNumeral()) {
							retorno = item;
						}
					}else {
						retorno = item;
					}
				}
				
			}
		}

		if (parteA < 2) {

			retorno = null;
			if (migradoAlSIIU) { 

				int aprobada = 0;
				for (RecordEstudianteDto item : historial) {

					if(item.getRcesCarreraDto().getCrrEspeCodigo().equals(SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA)
							&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {

						aprobada = aprobada + 1;

						if (retorno != null) {
							if (retorno.getRcesNivelDto().getNvlNumeral() < item.getRcesNivelDto().getNvlNumeral()) {
								retorno = item;
							}
						}else {
							retorno = item;
						}
					}

					if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU)
							&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {

						aprobada = aprobada + 1;

						if (retorno != null) {
							if (retorno.getRcesNivelDto().getNvlNumeral() < item.getRcesNivelDto().getNvlNumeral()) {
								retorno = item;
							}
						}else {
							retorno = item;
						}

					}

				}	

				if (aprobada < 2) {
					retorno = null;
				}
			}

		}

		return retorno;
	}
	
	
	/**
	 * Método que permite verificar si aprobo la suficiencia en el SAU.
	 * - Solo AFR en SAU
	 * - Solo deportes sin registro en SIIU
	 * @author fgguzman
	 * @param historial
	 * @return RecordEstudianteDto
	 */
	private RecordEstudianteDto verificacionEstadosActividadFisicaSAU(List<RecordEstudianteDto> historial) {
		RecordEstudianteDto retorno = null;

		int aprobada = 0;
		for (RecordEstudianteDto item : historial) {

			if(item.getRcesCarreraDto().getCrrEspeCodigo().equals(SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA)
						&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
					
					aprobada = aprobada + 1;
					
					if (retorno != null) {
						if (retorno.getRcesNivelDto().getNvlNumeral() < item.getRcesNivelDto().getNvlNumeral()) {
							retorno = item;
						}
					}else {
						retorno = item;
					}
					
			}

		}	
		
		boolean migradoAlSIIU = false;
		for (RecordEstudianteDto item : historial) {

			if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU)){
				migradoAlSIIU = true;
			}

		}	

		if (aprobada < 2 && !migradoAlSIIU) {

			retorno = null;
			aprobada = 0;
			
			for (RecordEstudianteDto item : historial) {

				if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SAU)
						&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
					
					aprobada = aprobada + 1;
					
					if (retorno != null) {
						if (retorno.getRcesNivelDto().getNvlNumeral() < item.getRcesNivelDto().getNvlNumeral()) {
							retorno = item;
						}
					}else {
						retorno = item;
					}
				}
				
			}	
			
			if (aprobada < 2) {
				retorno = null;
			}
		}
		
		
		
		return retorno;
	}
	
	
	
	/**
	 * Método que permite verificar si aprobo la suficiencia en el SIIU.
	 * @author fgguzman
	 * @param historial
	 * @return RecordEstudianteDto
	 */
	private RecordEstudianteDto verificacionEstadosActividadFisicaSIIU(List<RecordEstudianteDto> historial) {
		RecordEstudianteDto retorno = null;
		
		for (RecordEstudianteDto item : historial) {
			if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SIIU)
					&& (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_SEGUNDO_VALUE) || item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_APROBACION_VALUE)) 
					&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
				retorno = item;
				break;
			}
		}	
		
		return retorno;
	}



	/**
	 * Método que permite obtener la el record estudiante en el que aprobo la suficiencia en informatica.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte
	 * @param nivelEducacion - pregrado o posgrado
	 * @return RecordEstudianteDto si aprobo.
	 */
	public RecordEstudianteDto verificarEstadoSuficienciaInformatica(String identificacion, int nivelEducacion){
		RecordEstudianteDto retorno = null;
		
		List<RecordEstudianteDto> historial = new ArrayList<>();
		
		List<RecordEstudianteDto> historicoSIIU = cargarHistorialAcademicoSuficienciaInformaticaSIIU(identificacion);
		if (!historicoSIIU.isEmpty()) {
			for (RecordEstudianteDto item : historicoSIIU) {
				item.setRcesEstadoLabel(ListasCombosForm.getListaEstadoRecorAcademico(item.getRcesEstado()));
			}
			historial.addAll(historicoSIIU);			
		}
		
		List<RecordEstudianteDto> exonerados = cargarHistorialAcademicoSuficienciaInformaticaExoneracion(identificacion);
		if (!exonerados.isEmpty()) {
			historial.addAll(exonerados);			
		}
		
		List<RecordEstudianteDto> presenciales = cargarHistorialAcademicoSuficienciaInformaticaPresencial(identificacion);
		if (!presenciales.isEmpty()) {
			historial.addAll(presenciales);			
		}
		
		FacesUtil.limpiarMensaje();
		
		if (!historial.isEmpty()) {
			for (RecordEstudianteDto item : historial) {
				if (item.getRcesEstadoLabel().equals(SuficienciaInformaticaConstantes.RCES_EXONERACION_ESTADO_APROBADO_LABEL)
						|| item.getRcesEstadoLabel().equals(SuficienciaInformaticaConstantes.RCES_PRESENCIAL_ESTADO_APROBADO_LABEL)
						|| item.getRcesEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_APROBADO_LABEL)) {
					retorno = item;
					break;
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite obtener el historial académico suficiencia informáticas del siiu.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial suficiencia informatica.
	 */
	private List<RecordEstudianteDto> cargarHistorialAcademicoSuficienciaInformaticaSIIU(String identificacion){
		List<RecordEstudianteDto> retorno = new ArrayList<>();

		try {
			retorno = servJdbcSuficienciaInformatica.buscarHistorialMatriculasSuficiencia(identificacion);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite obtener el historial académico del app-suficiencia-informatica modalidad exoneraciones.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial exoneraciones
	 */
	private List<RecordEstudianteDto> cargarHistorialAcademicoSuficienciaInformaticaExoneracion(String identificacion){
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcSuficienciaInformatica.buscarHistorialMatriculasExoneracion(identificacion);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite obtener el historial académico del app-suficiencia-informatica modalidad presenciales.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial presenciales
	 */
	private List<RecordEstudianteDto> cargarHistorialAcademicoSuficienciaInformaticaPresencial(String identificacion){
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		
		try {
			retorno = servJdbcSuficienciaInformatica.buscarHistorialMatriculasPresencial(identificacion);
		} catch (RecordEstudianteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	

	/**
	 * Método que permite verificar si un estudiante tiene gratuidad o ya la perdió definitivamente.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte.
	 * @param historialAcademico - historial en carrera.
	 * @param carreraId - id de la carrera.
	 * @param totalCreditosHorasMalla - cantidad de creditos / horas de la malla 
	 * @return true si perdió gratuidad.
	 */
	public boolean verificarPerdidaDefinitivaGratuidad(String identificacion, List<RecordEstudianteDto> historialAcademico, int carreraId, int totalCreditosHorasMalla){
		boolean retorno = false;
		
		if (verificarSegundaCarreraPublica(identificacion)) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Ud registra segunda carrera por ello No Aplica Gratuidad.");
			retorno = true;
		}else if (verificarTreintaPorCienReprobadosEnCarrera(historialAcademico, carreraId, totalCreditosHorasMalla)) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("La cantidad de créditos reprobados es mayor o igual al 30% del total de la malla curricular por ello no aplica gratuidad.");
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método a verificar si el estudiante ha perdido la gratuidad en la carrera.
	 * @param historialAcademico - record acedemico por carrera.
	 * @return true - mayor o igual al 30% de creditos reprobados.
	 */
	private boolean verificarTreintaPorCienReprobadosEnCarrera(List<RecordEstudianteDto> historialAcademico, int carreraId, int totalCreditosHorasMalla) {
		boolean retorno = false;		

		if (!historialAcademico.isEmpty()) {

			int creditosReprobados = 0;
			for (RecordEstudianteDto materia : historialAcademico) {
				// si reprobo -> sumar creditos reprobados
				if (materia.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
					creditosReprobados = creditosReprobados + materia.getRcesMateriaDto().getMtrCreditos();
				}else {
					creditosReprobados = creditosReprobados +  establecerNumeroCreditos(materia, historialAcademico);
				}
			}

			// calcular el 30 % de creditos
			BigDecimal limitePorcentaje = calcularTreintaPorCientoMalla(Long.valueOf(totalCreditosHorasMalla));
			if (new BigDecimal(creditosReprobados).floatValue() >= limitePorcentaje.floatValue()) {
				retorno = true;
			}

		}

		return retorno;
	}
	
	
	
	/**
	 * Método que permite calcular el 30% de Creditos de la Malla Curricular
	 * @param totalHorasPorSemana - horas por semana total
	 * @return 30% de horas por semana
	 */
	private BigDecimal calcularTreintaPorCientoMalla(Long totalHorasPorSemana) {
		MathContext mc = new MathContext(6);
		BigDecimal divisor = new BigDecimal("100");
		BigDecimal constante = new BigDecimal("30");
		constante = constante.multiply(new BigDecimal(totalHorasPorSemana,mc));
		return establecerNumeroSinAproximacion(constante.divide(divisor,mc),2);
	}
	

	/**
	 * Método que permite establecer el numero de matricula de las asignaturas del SAU especialmente.
	 * Si registra un Aprobado con tercera o segunda verificar que se encuentren los dos registros anteriores.
	 * Verificar casos en que convalida con 2da o 3era sin record
	 */
	private int establecerNumeroCreditos(RecordEstudianteDto materia, List<RecordEstudianteDto> matriculas) {
		int retorno = 0;

		int repeticiones = 0;
		for (RecordEstudianteDto item : matriculas) {// busco las veces que reprobo
			if (item.getRcesMateriaDto().getMtrCodigo().equals(materia.getRcesMateriaDto().getMtrCodigo())
					&& item.getRcesMateriaDto().getMtrEstadoLabel().equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
				repeticiones = repeticiones + 1;
			}
		}

		if (!(repeticiones == (materia.getRcesMateriaDto().getNumMatricula()-1))) {
			retorno = materia.getRcesMateriaDto().getMtrCreditos() * (materia.getRcesMateriaDto().getNumMatricula()-1);
		}

		return retorno;
	}

	private boolean verificarSegundaCarreraPublica(String identificacion){
		boolean retorno = false;		

		try {
			retorno = servJdbcGratuidad.buscarSegundaCarrera(identificacion);
		} catch (GratuidadNoEncontradoException e) {
		} catch (GratuidadException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		
		return retorno;
	}
	
	
	/**
	 * Método que calcula  el porcentaje de aprobación de malla de un estudiante.
	 * @author fgguzman
	 * @param totalCreditos - total creditos por aprobar en la carrera
	 * @param creditosAprobados - cantidad de creditos aprobados en la carrera
	 * @return porcentaje aprobación.
	 */
	public BigDecimal calcularPorcentajeMallaAprobada(int totalCreditos, int creditosAprobados){
		BigDecimal aprobados =  new BigDecimal(String.valueOf(creditosAprobados)).multiply(new BigDecimal("100",new MathContext(6)));
		return establecerNumeroSinAproximacion(aprobados.divide(new BigDecimal(String.valueOf(totalCreditos)),new MathContext(6)),2);
	}
	

	/**
	 * Método que permite recuperar los datos de un estudiante de SAU / SIIU
	 * @author fgguzman
	 * @param param - identificacion / primer apellido
	 * @param carreras - lista tipo carrera
	 * @param tipoBusqueda -> 0- Por identificacion / 1- Por primer apellido
	 * @return estudiantes.
	 */
	public List<EstudianteJdbcDto> cargarEstudianteEgresadoSAIU(String param, List<String> carreras, int tipoBusqueda){
		List<EstudianteJdbcDto> retorno = new ArrayList<>();

		List<EstudianteJdbcDto>  estudiantes = new ArrayList<>();
		
		List<EstudianteJdbcDto> estudiantesSIIU = cargarEstudiantesSIIU(param, carreras, tipoBusqueda);
		if (estudiantesSIIU != null && estudiantesSIIU.size() > 0) {
			estudiantes.addAll(estudiantesSIIU);
		}else {
			List<EstudianteJdbcDto> estudiantesSAU = cargarEstudiantesSAU(param, carreras, tipoBusqueda);
			if (estudiantesSAU != null && estudiantesSAU.size()  > 0) {
				estudiantes.addAll(estudiantesSAU);
			}
		}

		if (!estudiantes.isEmpty()) {
			retorno = quitarDuplicados(estudiantes);
		}

		return retorno;
	}
	
	
	/**
	 * Método que permite recuperar los datos de un estudiante de SAU / SIIU
	 * @author fgguzman
	 * @param param - identificacion / primer apellido
	 * @param carreras - lista tipo carrera
	 * @param tipoBusqueda -> 0- Por identificacion / 1- Por primer apellido
	 * @return estudiantes.
	 */
	public List<EstudianteJdbcDto> cargarEstudianteSAIU(String param, List<Carrera> carreras, int tipoBusqueda){
		List<EstudianteJdbcDto> retorno = new ArrayList<>();

		List<EstudianteJdbcDto>  estudiantes = new ArrayList<>();
		
		List<EstudianteJdbcDto> estudiantesSIIU = cargarEstudiantesSIIU(param, establecerCarreraId(carreras, RecordEstudianteConstantes.RCES_ORIGEN_SIIU), tipoBusqueda);
		if (estudiantesSIIU != null && estudiantesSIIU.size() > 0) {
			estudiantes.addAll(estudiantesSIIU);
		}else {
			List<EstudianteJdbcDto> estudiantesSAU = cargarEstudiantesSAU(param, establecerCarreraId(carreras, RecordEstudianteConstantes.RCES_ORIGEN_SAU), tipoBusqueda);
			if (estudiantesSAU != null && estudiantesSAU.size()  > 0) {
				estudiantes.addAll(estudiantesSAU);
			}
		}

		if (!estudiantes.isEmpty()) {
			retorno = quitarDuplicados(estudiantes);
		}

		return retorno;
	}
	
	/**
	 * Método que permite buscar el historial académico de estudiantes SAU - SIIU.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial académico
	 */
	public List<RecordEstudianteDto> cargarHistorialAcademicoSAIUHomologado(String identificacion){
		List<RecordEstudianteDto> retorno = new ArrayList<>();

		List<RecordEstudianteDto> auxListSAU = buscarHistorialAcademicoSAUHomologado(identificacion,new Integer[]{SAUConstantes.MTR_ACTIVA_VALUE});
		if (!auxListSAU.isEmpty()) {
			// setear atributos propios del SIIU
			for (RecordEstudianteDto item : auxListSAU) {
				asignarEstadoAsignaturaEnRecord(item);
				asignarCarreraPorEspeCodigo(item);
				asignarNivelMatricula(item);
				asignarNivelMateria(item);
				asignarTipoMatriculacion(item);
				asignarMateriaIdPorCodigoMateria(item);
			}
			retorno.addAll(auxListSAU);
		}

		List<RecordEstudianteDto> auxListSIIU = buscarHistorialAcademicoSIIUHomologado(identificacion, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_INACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_EN_CIERRE_VALUE});
		if (!auxListSIIU.isEmpty()) {
			// setear atributos propios del SIIU
			for (RecordEstudianteDto item : auxListSIIU) {
				asignarEstadoAsignaturaEnRecord(item);
				asignarNivelMatricula(item);
				asignarTipoMatriculacion(item);
			}
			
			retorno.addAll(auxListSIIU);
		}

		return retorno;
	}
	
	
	
	/**
	 * Método que permite cargar el historial academico de posgrado
	 * @author daniel
	 * @param identificacion - identificación.
	 * @return List<RecordEstudianteDto>.
	 */
	public List<RecordEstudianteDto> cargarHistorialAcademicoPosgradoSIIU(String identificacion){
		List<RecordEstudianteDto> retorno = new ArrayList<>();

		List<RecordEstudianteDto> auxListSIIU = buscarHistorialAcademicoPosgradoSIIU(identificacion, new Integer[]{MateriaConstantes.ESTADO_MATERIA_ACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_INACTIVO_VALUE, MateriaConstantes.ESTADO_MATERIA_EN_CIERRE_VALUE});
		if (auxListSIIU != null && auxListSIIU.size() > 0) {
			for (RecordEstudianteDto it : auxListSIIU) {
				try {
					Carrera entidad = servCarrera.buscarPorId(it.getRcesCarreraDto().getCrrId());
					it.getRcesCarreraDto().setCrrId(entidad.getCrrId());
					it.getRcesCarreraDto().setCrrDescripcion(entidad.getCrrDescripcion());
					it.getRcesDependenciaDto().setDpnId(entidad.getCrrDependencia().getDpnId());
					it.getRcesDependenciaDto().setDpnDescripcion(entidad.getCrrDependencia().getDpnDescripcion());
				} catch (CarreraNoEncontradoException e) {
					it.getRcesCarreraDto().setCrrId(-1);
					it.getRcesCarreraDto().setCrrDescripcion("CARRERA NO REGISTRADA EN SIIU");
					it.getRcesCarreraDto().setDpnId(-1);
					it.getRcesCarreraDto().setDpnDescripcion("DEPENDENCIA NO REGISTRADA EN SIIU");
				} catch (CarreraException e) {
				}
			}
			retorno.addAll(auxListSIIU);
		}

		if (retorno.size() > 0) {
			return retorno;
		}else {
			return null;
		}
	}
	
	/**
	 * Método que permite cargar el historial de matriculas de un estudiante por su identificacion.
	 * @author fgguzman
	 * @param identificacion - cedula   o pasaporte del estudiente.
	 * @return historial matriculas SAIU.
	 */
	public List<RecordEstudianteDto> cargarHistorialMatriculasSAIU(String identificacion){
		List<RecordEstudianteDto> retorno = new ArrayList<>();
		
		List<RecordEstudianteDto> auxListSAU = buscarHistorialMatriculasSAU(identificacion);
		if (auxListSAU != null && auxListSAU.size() > 0) {
			
			for (RecordEstudianteDto it : auxListSAU) {
				try {
					Carrera entidad = servCarrera.buscarCarreraXEspeCodigo(establecerEspeCodigoSuficiencias(it.getRcesCarreraDto().getCrrId()));
					it.getRcesCarreraDto().setCrrId(entidad.getCrrId());
					it.getRcesCarreraDto().setCrrDescripcion(entidad.getCrrDescripcion());
					it.getRcesDependenciaDto().setDpnId(entidad.getCrrDependencia().getDpnId());
					it.getRcesDependenciaDto().setDpnDescripcion(entidad.getCrrDependencia().getDpnDescripcion());
				} catch (CarreraNoEncontradoException e) {
					it.getRcesCarreraDto().setCrrId(-1);
					it.getRcesCarreraDto().setCrrDescripcion("CARRERA NO REGISTRADA EN SIIU");
					it.getRcesCarreraDto().setDpnId(-1);
					it.getRcesCarreraDto().setDpnDescripcion("DEPENDENCIA NO REGISTRADA EN SIIU");
				} catch (CarreraException e) {
				}
			}
			
			retorno.addAll(auxListSAU);
		}

		List<RecordEstudianteDto> auxListSIIU = buscarHistorialMatriculaSIIU(identificacion);
		if (auxListSIIU != null && auxListSIIU.size() > 0) {
			retorno.addAll(auxListSIIU);
		}

		if (retorno.size() > 0) {
			return retorno;
		}else {
			return null;
		}
	}
	
	
	
	/**
	 * Método que permite consultar el historial academico con Homologacion de Etiquetas y ID´s registrado en el SAU correspondientes al SIIU.
	 * @author fgguzman
	 * @return historial academico SAU
	 */
	private List<RecordEstudianteDto> buscarHistorialAcademicoSAUHomologado(String identificacion, Integer[] mtrEstados) {
		try {
			return servJdbcHistorialAcademico.buscarHistorialAcademicoSAUHomologado(identificacion, mtrEstados);
		} catch (RecordEstudianteNoEncontradoException e) {
			return new ArrayList<>();
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return new ArrayList<>();
		}
	}
	
	/**
	 * Método que permite consultar el historial academico registrado en el SIIU.
	 * @author fgguzman
	 * @return historial academico SIIU
	 */
	private List<RecordEstudianteDto> buscarHistorialAcademicoSIIUHomologado(String identificacion, Integer[] mtrEstados) {
		try {
			return servJdbcHistorialAcademico.buscarHistorialAcademicoSIIUHomologado(identificacion, mtrEstados);
		} catch (RecordEstudianteNoEncontradoException e) {
			return new ArrayList<>();
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return new ArrayList<>();
		}
	}
	
	
	private List<RecordEstudianteDto> buscarHistorialAcademicoPosgradoSIIU(String identificacion, Integer[] mtrEstados) {
		try {
			return servJdbcHistorialAcademico.buscarHistorialAcademicoSIIUPosgrado(identificacion, mtrEstados);
		} catch (RecordEstudianteNoEncontradoException e) {
			e.printStackTrace();
			return null;
		} catch (RecordEstudianteException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Método que permite consultar el historial de matriculas registrado en el SAU.
	 * @author fgguzman
	 * @return historial matricula SAU
	 */
	private List<RecordEstudianteDto> buscarHistorialMatriculasSAU(String identificacion) {
		try {
			return servJdbcHistorialAcademico.buscarHistorialMatriculaSAU(identificacion);
		} catch (RecordEstudianteNoEncontradoException e) {
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}
	
	/**
	 * Método que permite consultar el historial de matriculas registrado en el SIIU.
	 * @author fgguzman
	 * @return historial matricula SIIU
	 */
	private List<RecordEstudianteDto> buscarHistorialMatriculaSIIU(String identificacion) {
		try {
			return servJdbcHistorialAcademico.buscarHistorialMatriculaSIIU(identificacion);
		} catch (RecordEstudianteNoEncontradoException e) {
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Método que permite traer los estudiantes del SIIU.
	 * @author fgguzman
	 * @param param - identificacion / apellido
	 * @param carreras - id carreras SIIU
	 * @param tipoBusqueda - identificacion / primer apellido
	 * @return estudiantes
	 */
	private List<EstudianteJdbcDto> cargarEstudiantesSIIU(String param, List<String> carreras, int tipoBusqueda){
		try {
			return servJdbcEstudianteDto.buscarEstudiantePorCarrerasSIIU(param, carreras, tipoBusqueda);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			return null;
		}
	}
	
	/**
	 * Método que permite traer los estudiantes del SAU.
	 * @author fgguzman
	 * @param param - identificacion / apellido
	 * @param carreras - id carreras SAU
	 * @param tipoBusqueda - identificacion / primer apellido
	 * @return estudiantes
	 */
	private List<EstudianteJdbcDto> cargarEstudiantesSAU(String param, List<String> carreras, int tipoBusqueda){
		try {
			return servJdbcEstudianteDto.buscarEstudiantePorCarrerasSAU(param, carreras, tipoBusqueda);
		} catch (EstudianteDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (EstudianteDtoJdbcNoEncontradoException e) {
			return null;
		}
	}
	
	/**
	 * Método que permite establecer las carreras para busqueda, SAIU.
	 * @author fgguzman
	 * @param carreras - SAU / SIIU
	 * @param origen - SAU / SIIU
	 * @return cadena de Id
	 */
	private List<String> establecerCarreraId(List<Carrera> carreras, int origen){
		List<String> retorno = new ArrayList<>();
		
		if (carreras != null) {
			if (origen == RecordEstudianteConstantes.RCES_ORIGEN_SIIU) {
				for (Carrera item : carreras) {
					retorno.add(String.valueOf(item.getCrrId()));
				}
			}else {
				for (Carrera item : carreras) {
					if (item.getCrrEspeCodigo() != null) {
						retorno.add(String.valueOf(item.getCrrEspeCodigo()));					
					}
				}
			}
		}else {
			retorno.add(String.valueOf(GeneralesConstantes.APP_ID_BASE));
		}

		return retorno;
	}
	
	/**
	 * Método que permite eliminar personas duplicadas.
	 * @author fgguzman
	 * @param personas - estudiantes encontrados SAIU
	 * @return estudiantes.
	 */
	private List<EstudianteJdbcDto> quitarDuplicados(List<EstudianteJdbcDto> personas){
		List<EstudianteJdbcDto> retorno = new ArrayList<>();
		if (personas != null && personas.size() > 0) {
			List<EstudianteJdbcDto> aux = new ArrayList<>();
			Map<String, EstudianteJdbcDto> mapCarreras =  new HashMap<String, EstudianteJdbcDto>();

			for (EstudianteJdbcDto it : personas) {
				mapCarreras.put(it.getPrsIdentificacion(),it);
			}
			for (Entry<String, EstudianteJdbcDto> it : mapCarreras.entrySet()) {
				aux.add(it.getValue());
			}
			
			aux.sort(Comparator.comparing(EstudianteJdbcDto::getPrsPrimerApellido).thenComparing(EstudianteJdbcDto:: getPrsSegundoApellido).thenComparing(EstudianteJdbcDto:: getPrsNombres));
			retorno = aux;
		}
		
		return retorno;
	}
	
	

	/**
	 * Método que permite setear dos cifras despues del punto decimal, sin incremetar al inmediato superior.
	 * @param numero - BigDEcimal mayor a 0.00
	 * @param digitos - cantidad de decimales requeridos.
	 * @return Bigdecimal con Formato ##.##
	 */
	public static BigDecimal establecerNumeroSinAproximacion(BigDecimal numero, int digitos){
		BigInteger entero = BigInteger.ZERO;BigInteger decimal = BigInteger.ZERO;
		MathContext mc = new MathContext(6);
		
		entero = numero.toBigInteger();
		BigDecimal decimas = numero.remainder(BigDecimal.ONE, mc);
		decimal = decimas.multiply(BigDecimal.TEN.pow(digitos)).toBigInteger();
		
		return new BigDecimal(entero+"."+setearMascara(decimal));
	}

	/**
	 * Método que permite dar mascara al decimal obtenido.
	 * @param number - entero tipo BigInteger
	 * @return entero con mascara ##
	 */
	private static String setearMascara(BigInteger number) {
		return String.format("%02d", number);
	}



	
	private void asignarTipoMatriculacion(RecordEstudianteDto item) {
		if (item.getRcesOrigen().equals(RecordEstudianteConstantes.RCES_ORIGEN_SAU)) {
			if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(1)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(2)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(3)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_LABEL);	
			}else {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel("DESCONOCIDO");
			}
		}else {
			if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_VALUE)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_VALUE)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_EXTRAORDINARIA_LABEL);	
			}else if (item.getRcesFichaMatriculaDto().getFcmtTipo().equals(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_VALUE)) {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel(MatriculaConstantes.TIPO_MATRICULA_ESPECIAL_LABEL);	
			}else {
				item.getRcesFichaMatriculaDto().setFcmtTipoMatriculaLabel("DESCONOCIDO");
			}
		}
	}
	
	private void asignarEstadoAsignaturaEnRecord(RecordEstudianteDto item) {
		item.setRcesEstadoLabel(cargarEstadoRecordEstudiantil(item.getRcesEstado(), item.getRcesOrigen()));
		item.getRcesMateriaDto().setMtrEstadoLabel(item.getRcesEstadoLabel());
	}
	
	private void asignarNivelMatricula(RecordEstudianteDto item) {
		String retorno = "";
		
		if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_APROBACION_VALUE)){
			retorno = NivelConstantes.NIVEL_APROBACION_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_NIVELACION_VALUE)) {
			retorno = NivelConstantes.NIVEL_NIVELACION_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_PRIMERO_VALUE)) {
			retorno = NivelConstantes.NIVEL_PRIMERO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_SEGUNDO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEGUNDO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_TERCER_VALUE)) {
			retorno = NivelConstantes.NIVEL_TERCER_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_CUARTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_CUARTO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_QUINTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_QUINTO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_SEXTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEXTO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_SEPTIMO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEPTIMO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_OCTAVO_VALUE)) {
			retorno = NivelConstantes.NIVEL_OCTAVO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_NOVENO_VALUE)) {
			retorno = NivelConstantes.NIVEL_NOVENO_LABEL;
		}else if (item.getRcesFichaMatriculaDto().getFcmtNivelUbicacion().equals(NivelConstantes.NIVEL_DECIMO_VALUE)) {
			retorno = NivelConstantes.NIVEL_DECIMO_LABEL;
		}
		
		item.getRcesFichaMatriculaDto().setNvlDescripcion(retorno);
	}
	
	private void asignarNivelMateria(RecordEstudianteDto item) {
		String retorno = "";
		
		if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_APROBACION_VALUE)){
			retorno = NivelConstantes.NIVEL_APROBACION_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_NIVELACION_VALUE)) {
			retorno = NivelConstantes.NIVEL_NIVELACION_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_PRIMERO_VALUE)) {
			retorno = NivelConstantes.NIVEL_PRIMERO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_SEGUNDO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEGUNDO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_TERCER_VALUE)) {
			retorno = NivelConstantes.NIVEL_TERCER_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_CUARTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_CUARTO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_QUINTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_QUINTO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_SEXTO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEXTO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_SEPTIMO_VALUE)) {
			retorno = NivelConstantes.NIVEL_SEPTIMO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_OCTAVO_VALUE)) {
			retorno = NivelConstantes.NIVEL_OCTAVO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_NOVENO_VALUE)) {
			retorno = NivelConstantes.NIVEL_NOVENO_LABEL;
		}else if (item.getRcesNivelDto().getNvlNumeral().equals(NivelConstantes.NIVEL_DECIMO_VALUE)) {
			retorno = NivelConstantes.NIVEL_DECIMO_LABEL;
		}
		
		item.getRcesNivelDto().setNvlDescripcion(retorno);
	}
	
	private String cargarEstadoRecordEstudiantil(int rcesEstado, int origen) {
		String retorno = "";
				
		if (RecordEstudianteConstantes.RCES_ORIGEN_SAU == origen) {
			switch (rcesEstado) {
			case SAUConstantes.RCES_MATERIA_ANULADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_ANULADO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_DESCONOCIDO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL;			
				break;
			case SAUConstantes.RCES_MATERIA_INSCRITO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_INSCRITO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_MATRICULDO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_MATRICULDO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_APROBADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_APROBADO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_REPROBADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_REPROBADO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_NINGUNA_NOTA_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_NINGUNA_NOTA_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_SUSPENSO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_SUSPENSO_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_PERDIDO_POR_ASISTENCIA_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_PERDIDO_POR_ASISTENCIA_LABEL;
				break;
			case SAUConstantes.RCES_MATERIA_CONVALIDADO_VALUE:
				retorno = SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL;
				break;
			}
		}else {
			
			if(RecordEstudianteConstantes.ESTADO_MATRICULADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_MATRICULADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_APROBADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_APROBADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_REPROBADO_VALUE.equals(rcesEstado)){
				retorno = RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_RECUPERACION_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_RECUPERACION_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_HOMOLOGADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_RETIRADO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_RETIRADO_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_LABEL;
			}else if (RecordEstudianteConstantes.ESTADO_INSCRITO_VALUE.equals(rcesEstado)){
				retorno =  RecordEstudianteConstantes.ESTADO_INSCRITO_LABEL;
			}else{
				retorno = SAUConstantes.RCES_MATERIA_DESCONOCIDO_LABEL;			
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Método que permite establecer el EspeCodigo de las Suficiencias.
	 * @author fgguzman
	 * @param espeCodigo - id de la Carrera del SAU.
	 * @return espeCodigo registrado en SIIU.
	 */
	private int establecerEspeCodigoSuficiencias(int espeCodigo) {

		if (espeCodigo == SAUConstantes.INGLES_APROBACION_NIVELES 
				|| espeCodigo == SAUConstantes.INGLES_APROBACION_CERTIFICADO
				|| espeCodigo == SAUConstantes.INGLES_APROBACION_IDIOMA_ADICIONAL
				|| espeCodigo == SAUConstantes.INGLES_APROBACION_SUFICIENCIA 
				|| espeCodigo == SAUConstantes.INGLES_APROBACION_A1
				|| espeCodigo == SAUConstantes.INGLES_APROBACION_A2
				|| espeCodigo ==  SAUConstantes.INGLES_APROBACION_INTENSIVO1
				|| espeCodigo ==  SAUConstantes.INGLES_APROBACION_INTENSIVO2
				|| espeCodigo == SAUConstantes.INGLES_APROBACION_ONLINE){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}else if (espeCodigo == SAUConstantes.FRANCES_APROBACION_SUFICIENCIA
				|| espeCodigo == SAUConstantes.FRANCES_APROBACION_A1
				|| espeCodigo == SAUConstantes.FRANCES_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_FRANCES;
		}else if (espeCodigo == SAUConstantes.ITALIANO_APROBACION_SUFICIENCIA
				|| espeCodigo == SAUConstantes.ITALIANO_APROBACION_A1
				|| espeCodigo == SAUConstantes.ITALIANO_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_ITALIANO;
		}else if (espeCodigo == SAUConstantes.COREANO_APROBACION_SUFICIENCIA
				|| espeCodigo == SAUConstantes.COREANO_APROBACION_A1
				|| espeCodigo == SAUConstantes.COREANO_APROBACION_A2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_COREANO;
		}else if (espeCodigo == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA1
				|| espeCodigo == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA2){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_KICHWA;
		}else if (espeCodigo == SAUConstantes.AFR_DEFENSA_PERSONAL
				|| espeCodigo == SAUConstantes.AFR_ACOND_FISICO
				|| espeCodigo == SAUConstantes.AFR_FUTBOL
				|| espeCodigo == SAUConstantes.AFR_AEREOBICOS
				|| espeCodigo == SAUConstantes.AFR_GIMNASIA_PESAS
				|| espeCodigo == SAUConstantes.AFR_BASQUETBALL
				|| espeCodigo == SAUConstantes.AFR_VOLEYBALL
				|| espeCodigo == SAUConstantes.AFR_TENIS
				|| espeCodigo == SAUConstantes.AFR_BAILE
				|| espeCodigo == SAUConstantes.AFR_BAILE_TROPICAL
				|| espeCodigo == SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA){
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_CULTURA_FISICA;
			// no se sabe en que idioma cayeron
		}else if (espeCodigo == SAUConstantes.CHINO_MANDARIN_APROBACION
				|| espeCodigo == SAUConstantes.APROBADO_A1A2
				|| espeCodigo == SAUConstantes.APROBADO_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}

		return espeCodigo;
	}
	
	private void asignarCarreraPorEspeCodigo(RecordEstudianteDto item){
		Carrera carrera = null;

		try {
			carrera = servCarrera.buscarCarreraXEspeCodigo(establecerEspeCodigoSuficiencias(item.getRcesCarreraDto().getCrrId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (carrera != null) {
			item.getRcesCarreraDto().setCrrEspeCodigo(item.getRcesCarreraDto().getCrrId());
			item.getRcesCarreraDto().setCrrId(carrera.getCrrId());
			item.getRcesCarreraDto().setCrrDescripcion(carrera.getCrrDescripcion());
			item.getRcesCarreraDto().setCrrTipo(carrera.getCrrTipo());
			item.getRcesCarreraDto().setCrrProceso(carrera.getCrrProceso());
			item.getRcesDependenciaDto().setDpnId(carrera.getCrrDependencia().getDpnId());
			item.getRcesDependenciaDto().setDpnDescripcion(carrera.getCrrDependencia().getDpnDescripcion());
		}else{
			item.getRcesCarreraDto().setCrrId(GeneralesConstantes.APP_ID_BASE);
			item.getRcesCarreraDto().setCrrDescripcion("CARRERA NO REGISTRADA EN SIIU");
		}
	}
	
	

	private void asignarMateriaIdPorCodigoMateria(RecordEstudianteDto item) {
		
		Materia materia = null;
		
		try {
			materia = servMateria.buscarXCodigoXCarrera(GeneralesUtilidades.quitarEspaciosEnBlanco(item.getRcesMateriaDto().getMtrCodigo()), item.getRcesCarreraDto().getCrrId());
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		if (materia != null) {
			item.getRcesMateriaDto().setMtrId(materia.getMtrId());
			item.setMtrId(materia.getMtrId());	
		}
		
	}

}
