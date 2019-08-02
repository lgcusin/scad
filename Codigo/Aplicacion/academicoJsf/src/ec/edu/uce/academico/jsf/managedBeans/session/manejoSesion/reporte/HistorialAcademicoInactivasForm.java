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
   
 ARCHIVO:     HistorialAcademicoInactivasForm.java	  
 DESCRIPCION: Bean de sesion que maneja los records academicos con mallas y materias inactivas. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-JUL-2018 			Freddy Guzmán						Integracion SAU
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.session.manejoSesion.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.EstudianteDtoServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.MatriculaServicioJdbc;
import ec.edu.uce.academico.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MateriaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RecordEstudianteConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.SAUConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.academico.jsf.managedBeans.session.ancestros.HistorialAcademicoForm;
import ec.edu.uce.academico.jsf.managedBeans.reportes.ReporteHistorialAcademicoInactivasForm;
import ec.edu.uce.academico.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) HistorialAcademicoInactivasForm. Managed Bean que
 * muestra el record academico de materias ACTIVAS E INACTIVAS MALLAS SAU Y SIIU
 * 
 * @author fgguzman
 * @version 1.0
 */

@ManagedBean(name = "historialAcademicoInactivasForm")
@SessionScoped
public class HistorialAcademicoInactivasForm extends HistorialAcademicoForm implements Serializable {
	private static final long serialVersionUID = 4816707704524277983L;
	// ****************************************************************/
	// *********************** VARIABLES ******************************/
	// ****************************************************************/
	private Usuario haifUsuario;
	private String haifPrimerApellido;
	private String haifIdentificacion;
	private Integer haifCrrId;
	private Integer haifTipoUsuario;
	private Integer haifTipoBusqueda;
	private BigDecimal haifPromedioGeneral;
	private BigDecimal haifPromedioInternado;
	private Boolean haifAcceso;
	private Boolean haifAccesoCertificado;
	private EstudianteJdbcDto haifPersona;
	private List<EstudianteJdbcDto> haifListPersona;
	private List<CarreraDto> haifListCarreraDto;
	private List<CarreraDto> haifListCarreraDtoSecretaria;
	private List<RecordEstudianteDto> haifListRecordEstudianteDto;
	private List<RecordEstudianteDto> haifListRecordEstudianteDtoSeleccion;
	private List<RecordEstudianteDto> haifListRecordEstudianteDtoPeriodoDto;
	private List<RecordEstudianteDto> haifListRecordEstudianteDtoMateriaDto;
	private List<PeriodoAcademicoDto> haifListPeriodoAcademicoDto;
	private int haifActivarReporte;
	private String haifNombreReporte;
	private String haifNombreArchivo;
	// ****************************************************************/
	// ********************* SERVICIOS GENERALES **********************/
	// ****************************************************************/
	@EJB	private MatriculaServicioJdbc servJdbcErafMatricula;
	@EJB	private PersonaDtoServicioJdbc servJdbcPersonaDto;
	@EJB	private CarreraDtoServicioJdbc servJdbcCarreraDto;
	@EJB	private PersonaServicio servPersona;
	@EJB	private EstudianteDtoServicioJdbc servJdbcEstudianteDto;
	@EJB	private CarreraServicio servCarrera;
	@EJB	private UsuarioRolServicio servUsuarioRolServicio;

	// ****************************************************************/
	// **************** METODOS GENERALES DE LA CLASE *****************/
	// ****************************************************************/

	/**
	 * Metodo que permite retornar a la pagina principal.
	 * @return pagina de inicio.
	 */
	public String irInicio() {
		limpiarFormEstudiantes();
		limpiarFormRecordAcademico();
		haifAcceso = Boolean.FALSE;
		haifAccesoCertificado = Boolean.FALSE;
		haifActivarReporte = 0;
		haifUsuario = null;
		haifPrimerApellido = null;
		haifIdentificacion = null;
		haifCrrId = null;
		haifTipoUsuario = null;
		haifTipoBusqueda = null;
		haifPromedioGeneral = null;
		haifAcceso = null;
		haifPersona = null;
		haifListPersona = null;
		haifListCarreraDto = null;
		haifListCarreraDtoSecretaria = null;
		haifListRecordEstudianteDto = null;
		haifListRecordEstudianteDtoSeleccion = null;
		haifListRecordEstudianteDtoPeriodoDto = null;
		haifListRecordEstudianteDtoMateriaDto = null;
		haifListPeriodoAcademicoDto = null;
		return "irInicio";
	}



	/**
	 * Dirige la navegacion hacia la pagina de listarEstudiante
	 */
	public String irAlistarEstudiante(Usuario usuario) {

		haifUsuario = usuario;
		haifActivarReporte = 0;
		haifPrimerApellido = new String();
		haifIdentificacion = new String();
		haifTipoBusqueda = GeneralesConstantes.APP_ID_BASE;
		
		List<UsuarioRol> usro = null;

		try {
			usro = servUsuarioRolServicio.buscarXUsuario(usuario.getUsrId());
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

		if (usro != null && usro.size() > 0) {
			for (UsuarioRol item : usro) {
				if (item.getUsroRol().getRolId() == RolConstantes.ROL_SECRECARRERA_VALUE.intValue()
						|| item.getUsroRol().getRolId() == RolConstantes.ROL_SECREABOGADO_VALUE.intValue()) {
					haifTipoUsuario = RolConstantes.ROL_SECRECARRERA_VALUE;
					haifAcceso = Boolean.TRUE;
					iniciarFormEstudiantesSecretariaCarreras(item);
					return "irListarEstudiantesRHI";
				} 
			}
		}

		return null;
	}
	
	public String irCarrerasEstudiante(EstudianteJdbcDto estudiante) {
		haifPersona = estudiante;
		haifListCarreraDto = null;
		haifListRecordEstudianteDto = null;

		List<RecordEstudianteDto> record = buscarRecordAcademico();
		if (record != null && record.size() > 0) {
			haifListRecordEstudianteDto = record;

			List<RecordEstudianteDto> carreras = new ArrayList<>();
			Map<Integer, RecordEstudianteDto> mapCarreras = new HashMap<Integer, RecordEstudianteDto>();

			for (RecordEstudianteDto it : record) {
				mapCarreras.put(it.getRcesCarreraDto().getCrrId(), it);
			}
			for (Entry<Integer, RecordEstudianteDto> carrera : mapCarreras.entrySet()) {
				carreras.add(carrera.getValue());
			}

			if (carreras.size() > 0) {
				haifListCarreraDto = permisosVisualizacionRecord(carreras);
			}

		} else {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("El estudiante con identificación: " + haifPersona.getPrsIdentificacion() + " aún no dispone de Carreras con Récord Académico activo.");
			return null;
		}

		return "irCarrerasEstudiantesRHI";
	}
	
	
	public String irEstudiantes(){
		
		return "irEstudiantesRHI";
	}
	
	public String irRecordAcademicoCarreras() {
		haifActivarReporte = 0;
		return "irCarrerasRHI";
	}

	
	private void iniciarFormEstudiantesSecretariaCarreras(UsuarioRol usro) {

		try {
			List<CarreraDto> carreras = servJdbcCarreraDto.buscarCarreras(usro.getUsroId());
			if (carreras != null && carreras.size() > 0) {
				haifListCarreraDtoSecretaria = null;
				haifListCarreraDtoSecretaria = carreras;
			}
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError("Usted no tiene asignadas carreras activas en este momento.");
		}

	}

	

	private List<CarreraDto> permisosVisualizacionRecord(List<RecordEstudianteDto> record) {
		List<CarreraDto> carreras = new ArrayList<>();
		for (RecordEstudianteDto it : record) {
			carreras.add(it.getRcesCarreraDto());
		}

		carreras.sort(Comparator.comparing(CarreraDto::getCrrDescripcion));

		if (haifTipoUsuario.equals(RolConstantes.ROL_SECRECARRERA_VALUE)) {
			haifAcceso = Boolean.TRUE;

			for (CarreraDto itC : haifListCarreraDtoSecretaria) {
				for (CarreraDto itR : carreras) {
					if (itC.getCrrId() == itR.getCrrId()) {
						itR.setCrrAcceso(true);
					}
				}
			}

		}


		return carreras;
	}

	/**
	 * Método que permite direccionar al historial academico.
	 * @param estudiante   - estudiante de pregrado
	 * @return historial academico.
	 */
	public String irHistorialAcademico(CarreraDto carrera) {

		if (haifListRecordEstudianteDto != null && haifListRecordEstudianteDto.size() > 0) {
			haifCrrId = carrera.getCrrId();
			List<RecordEstudianteDto> record = haifListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
					if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
							|| it.getRcesMateriaDto().getMtrEstadoLabel()
									.equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL)
							|| it.getRcesMateriaDto().getMtrEstadoLabel()
									.equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)
							|| it.getRcesMateriaDto().getMtrEstadoLabel()
									.equals(RecordEstudianteConstantes.ESTADO_REPROBADO_LABEL)) {
						recordSeleccion.add(it);
					}
				}
			}

			if (recordSeleccion.size() > 0) {
				List<BigDecimal> aprobadas = new ArrayList<>();

				try {
					for (RecordEstudianteDto it : recordSeleccion) {
						if (it.getRcesMateriaDto().getMtrEstadoLabel().equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
								|| it.getRcesMateriaDto().getMtrEstadoLabel()
										.equals(SAUConstantes.RCES_MATERIA_CONVALIDADO_LABEL)
								|| it.getRcesMateriaDto().getMtrEstadoLabel()
										.equals(RecordEstudianteConstantes.ESTADO_HOMOLOGADO_LABEL)) {

							if (it.getRcesMateriaDto().getMtrEstadoLabel()
									.equals(SAUConstantes.RCES_MATERIA_APROBADO_LABEL)
									&& it.getRcesCalificacionDto().getClfNotaFinalSemestre().floatValue() < BigDecimal
											.valueOf(10).floatValue()) {
								aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre()
										.multiply(BigDecimal.valueOf(4)));
							} else {
								aprobadas.add(it.getRcesCalificacionDto().getClfNotaFinalSemestre());
							}

						}
					}

					BigDecimal suma = BigDecimal.ZERO;
					for (BigDecimal it : aprobadas) {
						suma = suma.add(it);
					}
					haifPromedioGeneral = BigDecimal.ZERO;
					suma = suma.divide(BigDecimal.valueOf(aprobadas.size()), 10, RoundingMode.CEILING);
					haifPromedioGeneral = suma.divide(BigDecimal.valueOf(2), 10, RoundingMode.CEILING);
				} catch (Exception e) {
					haifPromedioGeneral = BigDecimal.ZERO;
					FacesUtil.mensajeError("Revisar las notas para poder realizar el promedio general.");
				}

			}
			recordSeleccion.sort(Comparator.comparing(RecordEstudianteDto::getRcesPracDescripcion));
			haifListRecordEstudianteDtoSeleccion = recordSeleccion;
			return "irRecordAcademicoRHI";
		} else {
			FacesUtil.mensajeError("Error, no existe Récord académico cargado.");
			return null;
		}

	}

	public String irMatriculasEstudiante(CarreraDto carrera) {
		if (haifListRecordEstudianteDto != null && haifListRecordEstudianteDto.size() > 0) {
			haifCrrId = null;
			haifCrrId = carrera.getCrrId();
			List<RecordEstudianteDto> record = haifListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
					recordSeleccion.add(it);
				}
			}

			if (recordSeleccion.size() > 0) {
				haifListPeriodoAcademicoDto = null;
				haifListPeriodoAcademicoDto = llenarHistorialMatricula(recordSeleccion);
			} else {
				FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
				return null;
			}

			return "irMatriculasEstudiante";
		} else {
			FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
			return null;
		}
	}

	public String irPeriodosAcademicosEstudiante(CarreraDto carrera) {
		if (haifListRecordEstudianteDto != null && haifListRecordEstudianteDto.size() > 0) {
			haifCrrId = null;
			haifCrrId = carrera.getCrrId();
			List<RecordEstudianteDto> record = haifListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == carrera.getCrrId()) {
					recordSeleccion.add(it);
				}
			}

			if (recordSeleccion.size() > 0) {
				haifListRecordEstudianteDtoPeriodoDto = null;
				haifListRecordEstudianteDtoPeriodoDto = llenarPeriodosAcademicos(recordSeleccion);
			} else {
				FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
				return null;
			}

			return "irPeriodosEstudiante";
		} else {
			FacesUtil.mensajeError("Error, no existen registros de matrícula para este estudiante.");
			return null;
		}
	}

	public String irNotasEstudiante(RecordEstudianteDto periodo) {

		if (haifListRecordEstudianteDto != null && haifListRecordEstudianteDto.size() > 0) {

			List<RecordEstudianteDto> record = haifListRecordEstudianteDto;
			List<RecordEstudianteDto> recordSeleccion = new ArrayList<>();

			for (RecordEstudianteDto it : record) {
				if (it.getRcesCarreraDto().getCrrId() == haifCrrId && it.getRcesPeriodoAcademicoDto()
						.getPracDescripcion().equals(periodo.getRcesPeriodoAcademicoDto().getPracDescripcion())) {
					recordSeleccion.add(it);
				}
			}

			if (recordSeleccion.size() > 0) {
				haifListRecordEstudianteDtoMateriaDto = null;
				haifListRecordEstudianteDtoMateriaDto = recordSeleccion;

			} else {
				FacesUtil.mensajeError("Error, no existen registros de notas para este período.");
				return null;
			}

			return "irNotasEstudiante";

		} else {
			FacesUtil.mensajeError("Error, no existen registros de notas para este período.");
			return null;
		}

	}

	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiarFormEstudiantes() {

		haifIdentificacion = new String();
		haifPrimerApellido = new String();
		haifListPersona = null;

	}

	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiarFormRecordAcademico() {
		haifListRecordEstudianteDto = null;
		haifCrrId = GeneralesConstantes.APP_ID_BASE;
	}

	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de
	 * búsqueda
	 */
	public void buscarEstudiantesSIIU() {


		if (haifTipoBusqueda.intValue() != GeneralesConstantes.APP_ID_BASE) {

			String param = null;
			if (haifTipoBusqueda == GeneralesConstantes.APP_FIND_BY_IDENTIFICACION) {
				param = haifIdentificacion;
			}else {
				param = haifPrimerApellido;
			}

			List<EstudianteJdbcDto>  estudiantes = cargarEstudianteSAIU(param, null, haifTipoBusqueda);
			if (estudiantes != null && estudiantes.size() > 0) {
				haifListPersona = estudiantes;
			}else {
				haifListPersona = null;
				FacesUtil.mensajeInfo("No se encontró resultados con los parámetros ingresados.");
			}

		}else {
			FacesUtil.mensajeInfo("Ingrese la identificación o el primer apellido del estudiante para continuar con la búsqueda.");
		}

	}

	public void busquedaPorIdentificacion() {

		if (haifIdentificacion.length() > 0) {
			haifPrimerApellido = new String();
			haifTipoBusqueda = Integer.valueOf(0);
		}

	}

	public void busquedaPorApellido() {

		if (haifPrimerApellido.length() > 0) {
			haifIdentificacion = new String();
			haifTipoBusqueda = Integer.valueOf(1);
		}

	}

	/**
	 * Método que permite unir los records academicos del estudiante.
	 * 
	 * @return record academico consolidado.
	 */
	private List<RecordEstudianteDto> buscarRecordAcademico() {
		List<RecordEstudianteDto> record = new ArrayList<>();

		List<RecordEstudianteDto> recordSAU = cargarRecordSAU();
		if (recordSAU != null && recordSAU.size() > 0) {

			for (RecordEstudianteDto it : recordSAU) {
				try {
					Carrera entidad = servCarrera
							.buscarCarreraXEspeCodigo(setearCarreraId(it.getRcesCarreraDto().getCrrId()));
					it.getRcesCarreraDto().setCrrId(entidad.getCrrId());
					it.getRcesCarreraDto().setCrrDescripcion(entidad.getCrrDescripcion());
					it.getRcesCarreraDto().setDpnId(entidad.getCrrDependencia().getDpnId());
				} catch (CarreraNoEncontradoException e) {
					it.getRcesCarreraDto().setCrrId(-1);
					it.getRcesCarreraDto().setCrrDescripcion("CARRERA NO REGISTRADA EN SIIU");
				} catch (CarreraException e) {
				}
			}

			record.addAll(recordSAU);
		}

		List<RecordEstudianteDto> recordSIIU = cargarRecordSIIU();
		if (recordSIIU != null && recordSIIU.size() > 0) {
			record.addAll(recordSIIU);
		}

		if (record.size() > 0) {
			return record;
		} else {
			return null;
		}
	}

	private int setearCarreraId(int carreraId) {

		if (carreraId == SAUConstantes.INGLES_APROBACION_NIVELES
				|| carreraId == SAUConstantes.INGLES_APROBACION_CERTIFICADO
				|| carreraId == SAUConstantes.INGLES_APROBACION_IDIOMA_ADICIONAL
				|| carreraId == SAUConstantes.INGLES_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.INGLES_APROBACION_A1 || carreraId == SAUConstantes.INGLES_APROBACION_A2
				|| carreraId == SAUConstantes.INGLES_APROBACION_INTENSIVO1
				|| carreraId == SAUConstantes.INGLES_APROBACION_INTENSIVO2
				|| carreraId == SAUConstantes.INGLES_APROBACION_ONLINE) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		} else if (carreraId == SAUConstantes.FRANCES_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.FRANCES_APROBACION_A1
				|| carreraId == SAUConstantes.FRANCES_APROBACION_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_FRANCES;
		} else if (carreraId == SAUConstantes.ITALIANO_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.ITALIANO_APROBACION_A1
				|| carreraId == SAUConstantes.ITALIANO_APROBACION_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_ITALIANO;
		} else if (carreraId == SAUConstantes.COREANO_APROBACION_SUFICIENCIA
				|| carreraId == SAUConstantes.COREANO_APROBACION_A1
				|| carreraId == SAUConstantes.COREANO_APROBACION_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_COREANO;
		} else if (carreraId == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA1
				|| carreraId == SAUConstantes.KICHWA_APROBACION_SUFICIENCIA2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_KICHWA;
		} else if (carreraId == SAUConstantes.AFR_DEFENSA_PERSONAL || carreraId == SAUConstantes.AFR_ACOND_FISICO
				|| carreraId == SAUConstantes.AFR_FUTBOL || carreraId == SAUConstantes.AFR_AEREOBICOS
				|| carreraId == SAUConstantes.AFR_GIMNASIA_PESAS || carreraId == SAUConstantes.AFR_BASQUETBALL
				|| carreraId == SAUConstantes.AFR_VOLEYBALL || carreraId == SAUConstantes.AFR_TENIS
				|| carreraId == SAUConstantes.AFR_BAILE || carreraId == SAUConstantes.AFR_BAILE_TROPICAL
				|| carreraId == SAUConstantes.AFR_ACTIVIDAD_FISICA_RECREATIVA) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_CULTURA_FISICA;
			// no se sabe en que idoma cayeron
		} else if (carreraId == SAUConstantes.CHINO_MANDARIN_APROBACION || carreraId == SAUConstantes.APROBADO_A1A2
				|| carreraId == SAUConstantes.APROBADO_A2) {
			return CarreraConstantes.CRR_ESPE_CODIGO_SUFICIENCIA_INGLES;
		}

		return carreraId;
	}

	/**
	 * Método que permite consultar el record academico del SIIU.
	 * 
	 * @return record academico
	 */
	private List<RecordEstudianteDto> cargarRecordSAU() {
		try {
			return servJdbcErafMatricula.buscarRecordAcademicoSAU(haifPersona.getPrsIdentificacion(), new Integer[]{SAUConstantes.MTR_INACTIVA_VALUE});
		} catch (RecordEstudianteNoEncontradoException e) {
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}

	}

	/**
	 * Método que permite consultar el record academico del SIIU.
	 * 
	 * @return record academico
	 */
	private List<RecordEstudianteDto> cargarRecordSIIU() {
		try {
			return servJdbcErafMatricula.buscarRecordEstudianteSIIU(haifPersona.getPrsIdentificacion(), new Integer[]{MateriaConstantes.ESTADO_MATERIA_INACTIVO_VALUE});
		} catch (RecordEstudianteNoEncontradoException e) {
			return null;
		} catch (RecordEstudianteException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
	}

	public void generarReporte() {
		haifNombreArchivo = "reporteRecordAcademicoInactivo";
		haifNombreReporte = "CERTIFICADO_RECORD_ACADEMICO_INACTIVAS";

		if (haifListRecordEstudianteDtoSeleccion != null && haifListRecordEstudianteDtoSeleccion.size() > 0) {
			ReporteHistorialAcademicoInactivasForm.generarReporteSuficienciaCulturaFisica(haifUsuario, haifPersona, haifPromedioGeneral, cargarDirectorCarrera(), cargarSecretarioAbogado(), haifListRecordEstudianteDtoSeleccion);
			haifActivarReporte = 1;
		} else {
			haifActivarReporte = 0;
			FacesUtil.mensajeInfo("No existen datos en lista para mostrar");
		}
	}


	/**
	 * Mètodo que permite cargar datos del director de carrera
	 * 
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarDirectorCarrera() {

		PersonaDto director = null;

		try {
			director = servJdbcPersonaDto.buscarPersonaProlDirCarreraPcrrId(RolConstantes.ROL_DIRCARRERA_VALUE,
					haifCrrId);
		} catch (PersonaDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaDtoNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró Director de Carrera.");
		}

		return director;
	}

	/**
	 * Mètodo que permite cargar datos del secretario abogado de la facultada
	 * 
	 * @return entidad PersonaDto.
	 */
	private PersonaDto cargarSecretarioAbogado() {

		PersonaDto secretario = null;

		if (haifCrrId != GeneralesConstantes.APP_ID_BASE) {
			CarreraDto carrera = null;

			try {
				carrera = servJdbcCarreraDto.buscarXId(haifCrrId);
			} catch (CarreraDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (CarreraDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeInfo("No se encontró la Carrera solicitada.");
			}

			if (carrera != null) {
				try {
					secretario = servJdbcPersonaDto.buscarPersonaXRolIdXFclId(RolConstantes.ROL_SECREABOGADO_VALUE,
							carrera.getCrrDpnId());
				} catch (PersonaDtoException e) {
					FacesUtil.mensajeError(e.getMessage());
				} catch (PersonaDtoNoEncontradoException e) {
					FacesUtil.mensajeInfo("No se encontró Secretario Abogado.");
				}
			}

		}

		return secretario;
	}

	private List<PeriodoAcademicoDto> llenarHistorialMatricula(List<RecordEstudianteDto> recordSeleccion) {
		List<PeriodoAcademicoDto> periodos = new ArrayList<>();

		Map<PeriodoAcademicoDto, List<RecordEstudianteDto>> mapMaterias = recordSeleccion.stream()
				.collect(Collectors.groupingBy(RecordEstudianteDto::getRcesPeriodoAcademicoDto));
		mapMaterias.forEach((k, v) -> {
			periodos.add(k);
			List<RecordEstudianteDto> materias = new ArrayList<>();
			k.setPracListRecordEstudianteDto(materias);
			v.forEach(it -> {
				materias.add(it);
			});
		});

		periodos.sort(Comparator.comparing(PeriodoAcademicoDto::getPracDescripcion));

		return periodos;
	}

	private List<RecordEstudianteDto> llenarPeriodosAcademicos(List<RecordEstudianteDto> recordSeleccion) {

		List<RecordEstudianteDto> periodos = new ArrayList<>();
		Map<String, RecordEstudianteDto> mapHorarios = new HashMap<String, RecordEstudianteDto>();
		for (RecordEstudianteDto periodo : recordSeleccion) {
			mapHorarios.put(periodo.getRcesPeriodoAcademicoDto().getPracDescripcion(), periodo);
		}
		for (Entry<String, RecordEstudianteDto> medico : mapHorarios.entrySet()) {
			periodos.add(medico.getValue());
		}
		periodos.sort(Comparator.comparing(RecordEstudianteDto::getRcesPeriodoAcademicoDto));

		return periodos;
	}
	// ****************************************************************/
	// ******************* GETTERS Y SETTERS *************************/
	// ****************************************************************/

	public Usuario getHaifUsuario() {
		return haifUsuario;
	}

	public void setHaifUsuario(Usuario haifUsuario) {
		this.haifUsuario = haifUsuario;
	}

	public String getHaifPrimerApellido() {
		return haifPrimerApellido;
	}

	public void setHaifPrimerApellido(String haifPrimerApellido) {
		this.haifPrimerApellido = haifPrimerApellido;
	}

	public String getHaifIdentificacion() {
		return haifIdentificacion;
	}

	public void setHaifIdentificacion(String haifIdentificacion) {
		this.haifIdentificacion = haifIdentificacion;
	}

	public Integer getHaifCrrId() {
		return haifCrrId;
	}

	public void setHaifCrrId(Integer haifCrrId) {
		this.haifCrrId = haifCrrId;
	}

	public Integer getHaifTipoUsuario() {
		return haifTipoUsuario;
	}

	public void setHaifTipoUsuario(Integer haifTipoUsuario) {
		this.haifTipoUsuario = haifTipoUsuario;
	}

	public Integer getHaifTipoBusqueda() {
		return haifTipoBusqueda;
	}

	public void setHaifTipoBusqueda(Integer haifTipoBusqueda) {
		this.haifTipoBusqueda = haifTipoBusqueda;
	}

	public BigDecimal getHaifPromedioGeneral() {
		return haifPromedioGeneral;
	}

	public void setHaifPromedioGeneral(BigDecimal haifPromedioGeneral) {
		this.haifPromedioGeneral = haifPromedioGeneral;
	}

	public BigDecimal getHaifPromedioInternado() {
		return haifPromedioInternado;
	}

	public void setHaifPromedioInternado(BigDecimal haifPromedioInternado) {
		this.haifPromedioInternado = haifPromedioInternado;
	}

	public Boolean getHaifAcceso() {
		return haifAcceso;
	}

	public void setHaifAcceso(Boolean haifAcceso) {
		this.haifAcceso = haifAcceso;
	}

	public Boolean getHaifAccesoCertificado() {
		return haifAccesoCertificado;
	}

	public void setHaifAccesoCertificado(Boolean haifAccesoCertificado) {
		this.haifAccesoCertificado = haifAccesoCertificado;
	}

	public EstudianteJdbcDto getHaifPersona() {
		return haifPersona;
	}

	public void setHaifPersona(EstudianteJdbcDto haifPersona) {
		this.haifPersona = haifPersona;
	}

	public List<EstudianteJdbcDto> getHaifListPersona() {
		return haifListPersona;
	}

	public void setHaifListPersona(List<EstudianteJdbcDto> haifListPersona) {
		this.haifListPersona = haifListPersona;
	}

	public List<CarreraDto> getHaifListCarreraDto() {
		return haifListCarreraDto;
	}

	public void setHaifListCarreraDto(List<CarreraDto> haifListCarreraDto) {
		this.haifListCarreraDto = haifListCarreraDto;
	}

	public List<CarreraDto> getHaifListCarreraDtoSecretaria() {
		return haifListCarreraDtoSecretaria;
	}

	public void setHaifListCarreraDtoSecretaria(List<CarreraDto> haifListCarreraDtoSecretaria) {
		this.haifListCarreraDtoSecretaria = haifListCarreraDtoSecretaria;
	}

	public List<RecordEstudianteDto> getHaifListRecordEstudianteDto() {
		return haifListRecordEstudianteDto;
	}

	public void setHaifListRecordEstudianteDto(List<RecordEstudianteDto> haifListRecordEstudianteDto) {
		this.haifListRecordEstudianteDto = haifListRecordEstudianteDto;
	}

	public List<RecordEstudianteDto> getHaifListRecordEstudianteDtoSeleccion() {
		return haifListRecordEstudianteDtoSeleccion;
	}

	public void setHaifListRecordEstudianteDtoSeleccion(
			List<RecordEstudianteDto> haifListRecordEstudianteDtoSeleccion) {
		this.haifListRecordEstudianteDtoSeleccion = haifListRecordEstudianteDtoSeleccion;
	}

	public List<RecordEstudianteDto> getHaifListRecordEstudianteDtoPeriodoDto() {
		return haifListRecordEstudianteDtoPeriodoDto;
	}

	public void setHaifListRecordEstudianteDtoPeriodoDto(
			List<RecordEstudianteDto> haifListRecordEstudianteDtoPeriodoDto) {
		this.haifListRecordEstudianteDtoPeriodoDto = haifListRecordEstudianteDtoPeriodoDto;
	}

	public List<RecordEstudianteDto> getHaifListRecordEstudianteDtoMateriaDto() {
		return haifListRecordEstudianteDtoMateriaDto;
	}

	public void setHaifListRecordEstudianteDtoMateriaDto(
			List<RecordEstudianteDto> haifListRecordEstudianteDtoMateriaDto) {
		this.haifListRecordEstudianteDtoMateriaDto = haifListRecordEstudianteDtoMateriaDto;
	}

	public List<PeriodoAcademicoDto> getHaifListPeriodoAcademicoDto() {
		return haifListPeriodoAcademicoDto;
	}

	public void setHaifListPeriodoAcademicoDto(List<PeriodoAcademicoDto> haifListPeriodoAcademicoDto) {
		this.haifListPeriodoAcademicoDto = haifListPeriodoAcademicoDto;
	}

	public int getHaifActivarReporte() {
		return haifActivarReporte;
	}

	public void setHaifActivarReporte(int haifActivarReporte) {
		this.haifActivarReporte = haifActivarReporte;
	}

	public String getHaifNombreReporte() {
		return haifNombreReporte;
	}

	public void setHaifNombreReporte(String haifNombreReporte) {
		this.haifNombreReporte = haifNombreReporte;
	}

	public String getHaifNombreArchivo() {
		return haifNombreArchivo;
	}

	public void setHaifNombreArchivo(String haifNombreArchivo) {
		this.haifNombreArchivo = haifNombreArchivo;
	}

}
