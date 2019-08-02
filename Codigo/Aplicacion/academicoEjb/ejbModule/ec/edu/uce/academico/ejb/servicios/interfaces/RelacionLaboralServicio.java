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
03-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralException;
import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.RelacionLaboral;

/**
 * Interface RelacionLaboralServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface RelacionLaboralServicio {

	/**
	 * Busca una entidad RelacionLaboral por su id
	 * @param id - deL RelacionLaboral a buscar
	 * @return RelacionLaboral con el id solicitado
	 * @throws RelacionLaboralNoEncontradoException - Excepcion lanzada cuando no se encuentra una RelacionLaboral con el id solicitado
	 * @throws RelacionLaboralException - Excepcion general
	 */
	public RelacionLaboral buscarPorId(Integer id) throws RelacionLaboralNoEncontradoException, RelacionLaboralException;

	/**
	 * Lista todas las entidades RelacionLaboral existentes en la BD
	 * @return lista de todas las entidades RelacionLaboral existentes en la BD
	 * @throws RelacionLaboralNoEncontradoException - Excepcion lanzada cuando no se encuentra una RelacionLaboral 
	 * @throws RelacionLaboralException - Excepcion general
	 */
	public List<RelacionLaboral> listarTodos() throws RelacionLaboralNoEncontradoException , RelacionLaboralException;
	 
}
