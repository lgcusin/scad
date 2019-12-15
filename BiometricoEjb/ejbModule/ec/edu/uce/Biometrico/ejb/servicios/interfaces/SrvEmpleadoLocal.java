package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.biometrico.jpa.*;


@Local
public interface SrvEmpleadoLocal {

	/**
	 * Obtiene las carreras de una facultad a la que pertenece el docente
	 * @param fdId de la Facultad
	 * @return lista carreras
	 */
	List<Carrera> listarCarreras(Integer fdId);

	/**
	 * Lista de docentes por las careras
	 * @param crrId
	 * @return
	 */
	List<FichaDocente> listarDocentes(Integer crrId);

	/**
	 * Obtiene horarios academicos de la fecha actual del Docente selecionado en el rango de hora
	 * @param integer id del Docente
	 * @param arrayHora rango de hora
	 * @param dia fecha actual
	 * @param crrId id de la carrera
	 * @return Horarios
	 */
	List<HorarioAcademico> listarHorariosxDocentexFechaHora(Integer integer, Integer[] arrayHora, int dia,
			Integer crrId);
	
	/**
	 * Obtiene los docentes de la carrera a la que petenecen
	 * @param crrId id de carrera
	 * @return docentes
	 */
	List<FichaDocente> listarDocentesxCarrera(Integer crrId);

	/**
	 * Obtiene la carrera por facultad selecionada
	 * @param dpnId id de la facultad
	 * @return carreras
	 */
	List<Carrera> listarCarrerasxFacultad(Integer dpnId);

	/**
	 * Obtiene los contenidos del syllabo de la materia que dicta un docente 
	 * @param fcdcId id del docente
	 * @return contenidos de clase
	 */
	List<Seguimiento> listarSeguimientosxDocente(Integer fcdcId);

	/**
	 * Obtiene la Facultad a la que pertenece
	 * @param usrId id del Usuario
	 * @return Facultad
	 */
	Dependencia buscarOrigenCarrera(int usrId);

	/**
	 * Lista los periodos academicos existentes
	 * @return Lista de Periodos de tipo= 0
	 */
	List<PeriodoAcademico> listarPeriodos();

	/**
	 * Lista las carreras de la facultad en el periodo elejido
	 * @param dpnId Id de la facultad
	 * @param pracId Id del periodo acdemico
	 * @return
	 */
	List<Carrera> listarCarrerasxFacultadPeriodo(int dpnId, Integer pracId);

}
