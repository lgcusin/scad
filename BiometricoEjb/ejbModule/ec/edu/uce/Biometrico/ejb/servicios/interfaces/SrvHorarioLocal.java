package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.biometrico.jpa.*;


@Local
public interface SrvHorarioLocal {

	List<Carrera> listarAllCrr(Integer fcId);

	List<Materia> listarAllMat();

	/**
	 * Lista los semestres  de la carrera al que pertenece el docente
	 * @param fdId id del docente
	 * @param crrId id de la carrera
	 * @return semestres
	 */
	List<Nivel> listarSemestrexCarrera(Integer fdId, Integer crrId);

	List<Materia> listarMatByCrr(Integer id);

	/**
	 * Obtiene las materias por el semestre al que pertenece el docente 
	 * @param fdId id del docente
	 * @param idSemestre id del semtestre
	 * @param idCarrera id de la carrera
	 * @return materias
	 */
	List<Materia> listarMatBySem(Integer fdId, Integer idSemestre, Integer idCarrera);

	/**
	 * Obtiene los paralelos del semestres en que se encuentra la materia que dicta el docente
	 * @param fdId id del docente
	 * @param idSemestre id del semestre
	 * @param idMateria id de la materia
	 * @param idCarrera id de la carrera
	 * @return paralelos
	 */
	List<Paralelo> listarParalelosHorario(Integer fdId, Integer idSemestre, Integer idMateria, Integer idCarrera);

	List<Aula> listarAula();

	/**
	 * obtiene el horario academico de la materia en el paralelo,semestre y carrera a la que pertenece y dicta el docente
	 * @param integer id del docente
	 * @param prlId id del paralelo
	 * @param mtrId id de la materia
	 * @return horarios academicos
	 */
	List<HorarioAcademico> listarHorarios(Integer integer, Integer prlId, Integer mtrId);

	/**
	 * Guarda los horarios de examen que dicta el docente
	 * @param hor Horario a guardar
	 */
	void guardarHorarioExamen(HorarioAcademicoExamen hor);

	/**
	 * Guarda las asistencias de la semana de examenes
	 * @param asis asistencia para guardar
	 */
	void actualizarGuardarAsistencia(Asistencia asis);

	/**
	 * Comprueba si exiten asistencias de los horarios academicos
	 * @param fcdcId id del docente
	 * @param idHorarios key id de hoarios obtenidos de la materia
	 * @param date fecha actual
	 * @return asistencias
	 */
	Asistencia listarAsistenciasByHorario(Integer fcdcId, List<Integer> idHorarios, Date date);

	/**
	 * Obtiene la hora del horario academico y lo tranforma a un formato estandar
	 * @param h horario academico
	 * @param i indice del Horario
	 * @return hora transformada
	 */
	String obtenerHoraClasexHorario(HorarioAcademico h, int i);

	/**
	 * Obtiene fechas de periodo acdemico al que pertenece horaios de materias del docente para examenes
	 * @param string Fecha actualizada
	 * @return periodos acdemicos
	 */
	List<PeriodoAcademico> listarPeriodos(String string);

	/**
	 * Obtiene fechas de periodo acdemico al que pertenece horaios de materias del docente para examenes
	 * @param string Fecha actualizada
	 * @return periodos acdemicos
	 */
	List<PlanificacionCronograma> listarPlannificacion(String string);

	/**
	 * Elimina el regitro modificado
	 * @param asis Asisencia a eliminar
	 */
	void removeAsistencia(Asistencia asis);

	/**
	 * Elimina el horario de examen de la asistencia a modificar
	 * @param hracex Horario a eliminar
	 */
	void removerHorarioExamen(HorarioAcademicoExamen hracex);

}
