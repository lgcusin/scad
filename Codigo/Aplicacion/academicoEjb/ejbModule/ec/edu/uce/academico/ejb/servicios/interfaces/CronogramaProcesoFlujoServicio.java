/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     CronogramaProcesoFlujoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla CronogramaProcesoFlujo.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-03-2017            David Arellano                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaProcesoFlujoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaProcesoFlujoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.CronogramaProcesoFlujo;

/**
 * Interface CronogramaProcesoFlujoServicio
 * Interfase que define las operaciones sobre la tabla CronogramaProcesoFlujo.
 * @author darellano
 * @version 1.0
 */
public interface CronogramaProcesoFlujoServicio {
	/**
	 * Busca una entidad CronogramaProcesoFlujo por su id
	 * @param id - de la CronogramaProcesoFlujo a buscar
	 * @return CronogramaProcesoFlujo con el id solicitado
	 * @throws CronogramaProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una CronogramaProcesoFlujo con el id solicitado
	 * @throws CronogramaProcesoFlujoException - Excepcion general
	 */
	public CronogramaProcesoFlujo buscarPorId(Integer id) throws CronogramaProcesoFlujoNoEncontradoException, CronogramaProcesoFlujoException;

	/**
	 * Lista todas las entidades CronogramaProcesoFlujo existentes en la BD
	 * @return lista de todas las entidades CronogramaProcesoFlujo existentes en la BD
	 * @throws CronogramaProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una CronogramaProcesoFlujo 
	 * @throws CronogramaProcesoFlujoException - Excepcion general
	 */
	public List<CronogramaProcesoFlujo> listarTodos() throws CronogramaProcesoFlujoNoEncontradoException , CronogramaProcesoFlujoException;
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @param cronogramaId - id del cronograma a buscar
	 * @param procesoFlujoId - id del proceso flujo a buscar
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	public CronogramaProcesoFlujo buscarXcronogramaXprocesoFlujo(int cronogramaId, int procesoFlujoId) throws CronogramaProcesoFlujoNoEncontradoException , CronogramaProcesoFlujoException;
	

}
