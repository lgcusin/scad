package servicios;

import java.util.List;

import javax.ejb.Local;

import model.Facultad;
import model.Parametro;

@Local
public interface SrvAdministrarParametroLocal {

	List<Parametro> listarParametro(Integer fctId);

	void guardarActualizarParametro(Parametro parametro);

	List<Facultad> listarFacultades();
}
