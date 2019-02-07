package servicios;

import java.util.List;

import javax.ejb.Local;

import model.Carrera;
import model.Materia;
import model.Syllabo;

@Local
public interface SrvSeguimientoLocal {

	List<Carrera> listarAllCrr();

	List<Materia> listarAllMat();
	
	List<Materia> listarMatByCrr(Integer id);

}
