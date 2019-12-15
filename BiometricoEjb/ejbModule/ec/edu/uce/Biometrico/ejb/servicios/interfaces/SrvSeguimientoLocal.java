package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.biometrico.jpa.*;


@Local
public interface SrvSeguimientoLocal {

	/**
	 * lista todos las carreras del docente
	 * @param fdcId id del docente
	 * @return carreras
	 */
	List<Carrera> listarAllCrrByFcdc(Integer fdcId);

	/**
	 * lista todas las materias del docente
	 * @param fdcID id del docente
	 * @return materias
	 */
	List<Materia> listarAllMatByFcdc(Integer fdcID);

	/**
	 * Obtiene un lista de materias por la carrera seleccionada - agregar sylabus
	 * @param id de la carrera
	 * @return materias
	 */
	List<Materia> listarMatByCrr(Integer id);

	/**
	 * obtiene la carrera para informacion del syllabus- agregar syllabus
	 * @param crrId id de la materia
	 * @return carrera
	 */
	Carrera obtenerCarreraSyllabus(Integer crrId);

	/**
	 * Obtiene el syllabus de la materia selecionada- agregar syllabus
	 * @param mtrId id de la materia
	 * @return syllabus
	 */
	Syllabo obtenerSyllabusxMateria(Integer mtrId);

	/**
	 * Lista las unidades curriculares de la materia seleccionada - agregar syllabus 
	 * @param mtrId id de la materia
	 * @return unidades curriculares
	 */
	List<UnidadCurricular> listarUnidadCurricular(Integer mtrId);

	/**
	 * Lista los cotenidos curriculares de la unidad curricular de la materia selecionada - agregar syllabus
	 * @param uncrId id de la unidad curricular
	 * @return contenidos curriculares
	 */
	List<ContenidoCurricular> listarContenidosCurriculares(Integer uncrId);

	/**
	 * Lista las actividades del contenido curricular de la materia seleccionada - agregar syllabus 
	 * @param cntId id del contenido
	 * @return contenidos
	 */
	List<Actividad> listarActividades(Integer cntId);

	/**
	 * Lista las herrammientas utilixadas para los contenidos curriculares de la materia seleccionada - agregar syllabus
	 * @param cntId in del contenido
	 * @return herramientas
	 */
	List<Herramienta> listarHerramientas(Integer cntId);

	/**
	 * Obtiene un horario academico del periodo academico establecido y del docente - control
	 * @param fecha fecha actual
	 * @param fcdcId id del docente
	 * @param b tipo de horario inicio o fin
	 * @param fclId id de la facutal
	 * @param tipo id de tipo entrada o atraso
	 * @return
	 */
	HorarioAcademico verificarHorario(Date fecha, Integer fcdcId, boolean b, Integer fclId, Integer tipo);

	Materia getMateria(Integer hrrId);

	Aula getAula(Integer hrrId);

	/**
	 * Obtiene los contenidos curriculares del syllabus para visualizar por el docente a registrar- control
	 * @param mtrId id de la amteria
	 * @return contenidos curriculares
	 */
	List<ContenidoCurricular> buscarContenidosCurriculares(Integer mtrId);

	/**
	 * Obtiene las asistencias generadas de la fecha actualdel docente y la materia a registrar 
	 * @param fecha fecha actual
	 * @param fcdcId id de docente
	 * @return asistencias
	 */
	List<Asistencia> obtenerAsistenciasxDocente(Date fecha, Integer fcdcId);

	/**
	 * Guarda las asistencias encontradascon la informacion de entrada - control
	 * @param regAss asistencia a guardar
	 */
	void guardarRegistro(Asistencia regAss);

	/**
	 * Metodo de autogeneracion de asistencias de los docenetes en la fecha actual de la facultad - control
	 * @param ahora fecha y hora actual
	 * @param fclId id del docente
	 * @return true- generado, false-no generado
	 */
	boolean generarAsistecniasxFacultad(Date ahora, Integer fclId);

	/**
	 * Guarda un registro de seguimiento con la tarea seleccionda en la asistencia inicio o fin
	 * @param seguimiento seguimiento a guardar
	 */
	void guardarActualizarSeguimiento(Seguimiento seguimiento);

	/**
	 * Guarda el contenido curricular ingresado en el syllabus- agregar syllabus
	 * @param cnt in del contenido curricular
	 * @return 
	 */
	ContenidoCurricular guardarActualizarContenido(ContenidoCurricular cnt);

	Syllabo setSyllabus(MallaCurricularMateria mllCrrMateria);

	/**
	 * Guarda la lista de unidad curricular ingresado en el syllabus - agregar syllabus
	 * @param uncr id de la unidad curricular
	 * @return 
	 */
	UnidadCurricular guardarActualizarUnidad(UnidadCurricular uncr);

	/**
	 * Obtiene la malla curricular materia a la que pertenece la materia - agregar syllabus
	 * @param mtrId id de la materia
	 * @return malla curricular
	 */
	MallaCurricularMateria getMallaCurricularMateria(Integer mtrId);

	/**
	 * Guarda el syllabu de la materia- agregar syllabus
	 * @param sylllabus a guardar
	 */
	void guardarActualizarSyllabus(Syllabo syl);

	/**
	 * Guarda la actividad del contenido ingresado en el syllabus - agregar syllabus
	 * @param actividad
	 */
	void guardarActualizarActividad(Actividad actividad);

	/**
	 * Guarda la herrammienta del contenido ingresado en el syllabus - agregar syllabus
	 * @param herramienta a guardar
	 */
	void guardarActualizarHerramienta(Herramienta herramienta);

	/**
	 * Guarda la metodologia de la unidad curricular ingresada en el syllabus - agregar syllabus
	 * @param metodologia a guardar
	 */
	void guardarActualizarMetodologia(Metodologia metodologia);

	/**
	 * Guara la bibliografia de la unidad surricular ingresada en el syllabus - agregar syllabus
	 * @param bibliografia a guardar
	 */
	void guardarActualizarBibliografia(Bibliografia bibliografia);

	/**
	 * Lista las metodologias de la unidad curricualar - agregar syllabus
	 * @param uncrId id de la unidad curricular
	 * @return metodologias
	 */
	List<Metodologia> listarMetodologias(Integer uncrId);

	/**
	 * Lista las bibliografias de la unidad curricular - agregar syllabus
	 * @param uncrId id de la unidad curricular
	 * @return bibliografias
	 */
	List<Bibliografia> listarBibliografias(Integer uncrId);

	/**
	 * Elimina una unidad curricular ingresada en el syllabus - agregar syllabus
	 * @param uc unidad a guardar
	 */
	void eliminarUnidad(UnidadCurricular uc);

	/**
	 * Obtiene  los seguimientos guardados de cada asistencia de la materia - control
	 * @param mtrId id de la materia
	 * @param fcdcId id del docente
	 * @return seguimientos
	 */
	List<Seguimiento> obtenerSeguimientosxMateria(Integer mtrId, Integer fcdcId);

	/**
	 * Guardar una asistencia con el horario academico y el docente en la fecha actual - generar registros
	 * @param hrac horario academico
	 * @param docente docente a guardar
	 * @param date fecha actual
	 */
	void generarAsistencias(HorarioAcademico hrac, FichaDocente docente, Date date);

	/**
	 * Obtiene una hora de inicio y de fin del horario acadmeico para el registro- control
	 * @param hrrI horario
	 * @return hora inicio y fin
	 */
	String obtenerHoraClasexHorario(HorarioAcademico hrrI);

	/**
	 * Obtiene los recursos didacticos de la Unidad Curricular selecciona
	 * @param uncrId id de LaUnidad Curricular
	 * @return Lista de recursos
	 */
	List<RecursoDidactico> listarRecursosDidacticos(int uncrId);

	/**
	 * Actualiza o guardar el recurso didactico ingresado
	 * @param recurso Objeto a guardar
	 */
	void guardarActualizarRecursoDidactico(RecursoDidactico recurso);

	/**
	 * Lista el periodo academico activo del docente
	 * @param i tipo
	 * @param j estado
	 * @return
	 */
	List<PeriodoAcademico> listarPeriodos(int i, int j);
	
	/**
	 * Busca todos los contenidos del sylabus
	 * @param mlcrmtId id del syllabus
	 * @return
	 */
	List<ContenidoCurricular> buscarAllContenidosCurriculares(int mlcrmtId);
	/**
	 * Busca la unidades curriculares del sylabus al que pertence la materia del horario actua
	 * @param mlcrmtId Id del Syllabus
	 * @return Lista de Unidades Curriculares
	 */
	List<UnidadCurricular> buscarAllUnidadesCurriculares(int mlcrmtId);

	/**
	 * Obtiene los syllabus regostrados en periodos anteriores
	 * @param mllCrrMateria Materia seleciona a para asignar un syllabo
	 * @return Lista de syllabos
	 */
	List<Syllabo> buscarAllSyllaboxPeriodo(MallaCurricularMateria mllCrrMateria);

	/**
	 * Obtiene el numero de horas restantes del horaio actula de registro
	 * @param mtrId
	 * @param fcdcId
	 * @return
	 */
	//Integer obtenerHoraSyllabusRestante(int mtrId, int fcdcId);

}
