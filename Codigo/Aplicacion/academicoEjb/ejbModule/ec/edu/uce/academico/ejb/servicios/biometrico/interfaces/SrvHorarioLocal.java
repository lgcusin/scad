package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.Asistencia;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademicoExamen;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

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

	void actualizarGuardarAsistencia(Asistencia asis);

	List<Asistencia> listarAsistenciasByHorario(Integer fcdcId, List<Integer> idHorarios, Date date);

	String obtenerHoraClasexHorario(HorarioAcademico h, int i);

	List<PeriodoAcademico> listarPeriodos(String string);

}
