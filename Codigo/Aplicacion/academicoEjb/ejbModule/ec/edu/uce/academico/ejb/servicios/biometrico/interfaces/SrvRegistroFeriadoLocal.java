package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.Feriado;

@Local
public interface SrvRegistroFeriadoLocal {

	/**
	 * Metodo definido para consultar los feriados segun los filtros ingresados.
	 * 
	 * @param fclId
	 * @param integer
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	List<Feriado> listarFeriados(boolean b, Integer crrId, Date inicio, Date fin);

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
	List<Dependencia> listarFacultades();

}
