package servicios;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import model.Actividad;
import model.Carrera;
import model.Contenido;
import model.Herramienta;
import model.Horario;
import model.Materia;
import model.Syllabo;
import model.UnidadCurricular;

@Local
public interface SrvSeguimientoLocal {

	List<Carrera> listarAllCrr();

	List<Materia> listarAllMat();

	List<Materia> listarMatByCrr(Integer id);

	Carrera getCarrera(Integer crrId);

	Syllabo getSyllabus(Integer mtrId);

	List<UnidadCurricular> listarUnidadCurricular(Integer mtrId);

	List<Contenido> listarContenidos(Integer uncrId);

	List<Actividad> listarActividades(Integer cntId);

	List<Herramienta> listarHerramientas(Integer cntId);
	
	Horario verificarHorario(Date fecha, Integer fcdcId);

	Materia getMateria(Integer hrrId);

}
