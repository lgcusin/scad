package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.uce.edu.biometrico.jpa.Aula;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.DiaSemana;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Nivel;
import ec.uce.edu.biometrico.jpa.Paralelo;
import ec.uce.edu.biometrico.jpa.TipoHorario;

@Local
public interface SrvHorarioLocal {

	List<Carrera> listarAllCrr(Integer fcId);

	List<Materia> listarAllMat();

	List<Nivel> listarAllSem();

	List<Materia> listarMatByCrr(Integer id);

	List<Materia> listarMatBySem(Integer idSemestre, Integer idCarrera);

	List<Paralelo> listarParalelosHorario(Integer idSemestre, Integer idMateria, Integer idCarrera);

	List<DiaSemana> listarAllDias();

	List<TipoHorario> listarTipoHorario();

	List<FichaDocente> listarDocentes();

	List<Aula> listarAula();

//	void guardarHorario(Horario horario);
//
//	List<Horario> listarHorarios(Integer idParalelo, Integer idMateria);
//
//	List<Paralelo> listarParalelosHorario(Integer mtrId);
//
//	List<ec.uce.edu.biometrico.jpa.Horario> listarHorarios(String prcdId);
//
//	void eliminarHorario(Horario horario);

}
