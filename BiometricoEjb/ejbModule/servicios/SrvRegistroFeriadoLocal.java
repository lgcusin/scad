package servicios;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import model.Facultad;
import model.Feriado;

@Local
public interface SrvRegistroFeriadoLocal {

	/**
	 * Metodo definido para consultar los feriados segun los filtros ingresados.
	 * 
	 * @param fclId
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	List<Feriado> listarFeriados(Integer fclId, Date fechaInicio, Date fechaFin);

	/**
	 * Metodo definido para guardar o actualizar un registro de feriado.
	 * 
	 * @param feriado
	 */
	void guardarActualizarFeriado(Feriado feriado);

	/**
	 * Metodo definido para eliminar un registro de feriado.
	 * 
	 * @param feriado
	 */
	void eliminarFeriado(Feriado feriado);

	/**
	 * Metodo definido para listar las facultades disponibles.
	 * 
	 * @return
	 */
	List<Facultad> listarFacultades();
}
