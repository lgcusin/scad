package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Parametro;

@Local
public interface SrvAdministrarParametroLocal {

	/**
	 * Metodo definido para buscar los parametros por facultad.
	 */
	List<Parametro> listarParametroxFacultad(Integer fctId);

	/**
	 * Metodo definido para guardar o actualizar un parametro.
	 */
	void guardarActualizarParametro(Parametro parametro);

	/**
	 * Metodo definido para buscar las facultades.
	 */
	List<Dependencia> listarFacultades();
}
