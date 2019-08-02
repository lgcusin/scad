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

 ARCHIVO:     CampoFormacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla CampoFormacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-Agosto-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CampoFormacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.CampoFormacion;

/**
 * Interface CampoFormacionServicio
 * Interfase que define las operaciones sobre la tabla CampoFormacion.
 * @author lmquishpei
 * @version 1.0
 */
public interface CampoFormacionServicio {
	
	/**
	 * Busca una entidad CampoFormacion por su id
	 * @param id - del CampoFormacion a buscar
	 * @return CampoFormacion con el id solicitado
	 * @throws CampoFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra un Dependencia con el id solicitado
	 * @throws CampoFormacionException - Excepcion general
	 */
//	public TipoMateria buscarPorId(Integer id) throws TipoMateriaNoEncontradoException, TipoMateriaException;
	
	/**
	 * Lista todas los CampoFormacion existentes en la BD
	 * @return lista de todas los CampoFormacion existentes en la BD
	 */
	
	public List<CampoFormacion> listarTodos() throws CampoFormacionNoEncontradoException;

	
	
	
}
