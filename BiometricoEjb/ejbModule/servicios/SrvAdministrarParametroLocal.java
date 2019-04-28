package servicios;

import java.util.List;

import javax.ejb.Local;

import model.Facultad;
import model.Parametro;

@Local
public interface SrvAdministrarParametroLocal {

	/**
	 * Metodo definido para buscar los parametros por facultad.
	 */
	List<Parametro> listarParametro(Integer fctId);

	/**
	 * Metodo definido para guardar o actualizar un parametro.
	 */
	void guardarActualizarParametro(Parametro parametro);

	/**
	 * Metodo definido para buscar las facultades.
	 */
	List<Facultad> listarFacultades();
}
