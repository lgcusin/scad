package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Aula;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.HorarioAcademicoExamen;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Nivel;
import ec.uce.edu.biometrico.jpa.Paralelo;

@Local
public interface SrvHorarioLocal {

	List<Carrera> listarAllCrr(Integer fcId);

	List<Materia> listarAllMat();

	List<Nivel> listarSemestrexCarrera(Integer fdId, Integer crrId);

	List<Materia> listarMatByCrr(Integer id);

	List<Materia> listarMatBySem(Integer fdId, Integer idSemestre, Integer idCarrera);

	List<Paralelo> listarParalelosHorario(Integer fdId, Integer idSemestre, Integer idMateria, Integer idCarrera);

	List<Aula> listarAula();

	List<HorarioAcademico> listarHorarios(Integer integer, Integer prlId, Integer mtrId);

	void guardarHorarioExamen(HorarioAcademicoExamen hor);

	void eliminarHorario(Asistencia asis);

	void actualizarGuardarAsistencia(Asistencia asis);

	List<Asistencia> listarAsistenciasByHorario(Integer fcdcId, List<Integer> idHorarios, Date date);

}
