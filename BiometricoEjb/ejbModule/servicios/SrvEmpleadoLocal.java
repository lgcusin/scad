package servicios;

import java.util.List;

import javax.ejb.Local;

import model.Carrera;
import model.FichaDocente;
import model.UnidadCurricular;

@Local
public interface SrvEmpleadoLocal {

	List<Carrera> listarCarreras();

	List<FichaDocente> listarDocentes(Integer crrId);

	void create(UnidadCurricular uc);

}
