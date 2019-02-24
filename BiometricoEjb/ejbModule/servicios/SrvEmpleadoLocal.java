package servicios;

import java.util.List;

import javax.ejb.Local;

import model.Carrera;
import model.FichaDocente;

@Local
public interface SrvEmpleadoLocal {

	List<Carrera> listarCarreras();

	List<FichaDocente> listarDocentes(Integer crrId);

}