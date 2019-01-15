package beans;

import javax.ejb.Local;

import model.FichaDocente;

@Local
public interface DocenteBeanLocal {

	public void guardar(FichaDocente fcdc);
}
