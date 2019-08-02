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

 ARCHIVO:     CarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Cronograma;

/**
 * Interface CarreraServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author dcollaguazo
 * @version 1.0
 */
public interface CronogramaServicio {

	/**
	 * Busca una entidad Cronograma por su id
	 * @param id - deL Cronograma a buscar
	 * @return Cronograma con el id solicitado
	 * @throws CronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Cronograma con el id solicitado
	 * @throws CronogramaException - Excepcion general
	 */
	public Cronograma buscarPorId(Integer id) throws CronogramaNoEncontradoException, CronogramaException;

	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	public List<Cronograma> listarTodos() throws CronogramaNoEncontradoException , CronogramaException;
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @param periodoAcademicoId - id del período academico a buscar
	 * @param estado - del cronograma a buscar 
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CronogramaException - Excepcion general
	 */
	public Cronograma buscarXperiodoAcademicoXestado(int periodoAcademicoId, int estado) throws CronogramaNoEncontradoException , CronogramaException;

}
