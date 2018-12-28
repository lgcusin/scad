package servicios;

import java.util.List;

import model.FichaDocente;

public interface DocenteBeanIn {
	public List<FichaDocente> listar();

	public void crear(FichaDocente fcdc);

}
