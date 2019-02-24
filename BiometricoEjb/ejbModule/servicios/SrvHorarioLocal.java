package servicios;

import java.util.List;

import javax.ejb.Local;

import model.Aula;
import model.Carrera;
import model.DiaSemana;
import model.FichaDocente;
import model.Horario;
import model.Materia;
import model.Paralelo;
import model.Semestre;
import model.TipoHorario;

@Local
public interface SrvHorarioLocal {

	List<Carrera> listarAllCrr();

	List<Materia> listarAllMat();

	List<Semestre> listarAllSem();

	List<Materia> listarMatByCrr(Integer id);

	List<Materia> listarMatBySem(Integer idSemestre, Integer idCarrera);

	List<Paralelo> listarParalelosHorario(Integer idSemestre, Integer idMateria, Integer idCarrera);

	List<DiaSemana> listarAllDias();

	List<TipoHorario> listarTipoHorario();

	List<FichaDocente> listarDocentes();

	List<Aula> listarAula();

	void guardarHorario(Horario horario);

	List<Horario> listarHorarios(Integer idParalelo, Integer idMateria);

	List<Paralelo> listarParalelosHorario(Integer mtrId);

	List<model.Horario> listarHorarios(String prcdId);

}