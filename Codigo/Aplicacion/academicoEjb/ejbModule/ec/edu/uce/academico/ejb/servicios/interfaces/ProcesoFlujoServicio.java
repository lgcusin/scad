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

 ARCHIVO:     ProcesoFlujoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla ProcesoFlujo.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-03-2017            David Arellano                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;

/**
 * Interface ProcesoFlujoServicio
 * Interfase que define las operaciones sobre la tabla ProcesoFlujo.
 * @author darellano
 * @version 1.0
 */
public interface ProcesoFlujoServicio {
	/**
	 * Busca una entidad ProcesoFlujo por su id
	 * @param id - de la ProcesoFlujo a buscar
	 * @return ProcesoFlujo con el id solicitado
	 * @throws ProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una ProcesoFlujo con el id solicitado
	 * @throws ProcesoFlujoException - Excepcion general
	 */
	public ProcesoFlujo buscarPorId(Integer id) throws ProcesoFlujoNoEncontradoException, ProcesoFlujoException;

	/**
	 * Lista todas las entidades ProcesoFlujo existentes en la BD
	 * @return lista de todas las entidades ProcesoFlujo existentes en la BD
	 * @throws ProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una ProcesoFlujo 
	 * @throws ProcesoFlujoException - Excepcion general
	 */
	public List<ProcesoFlujo> listarTodos() throws ProcesoFlujoNoEncontradoException , ProcesoFlujoException;
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @param descripcion - descripcion del proceso flujo a buscar
	 * @param estado - estado del proceso flujo a buscar
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws ProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws ProcesoFlujoException - Excepcion general
	 */
	public ProcesoFlujo buscarXdescripcionXestado(String descripcion, int estado) throws ProcesoFlujoNoEncontradoException , ProcesoFlujoException;

}
