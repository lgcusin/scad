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

 ARCHIVO:     EdificioServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Edificio.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-JUN-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.excepciones.EdificioException;
import ec.edu.uce.academico.ejb.excepciones.EdificioNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Edificio;

/**
 * Interface EdificioServicio
 * Interfase que define las operaciones sobre la tabla Edificio.
 * @author lmquishpei
 * @version 1.0
 */
public interface EdificioServicio {
	
	/**
	 * Busca una entidad Edificio por su id
	 * @param id - del Edificio a buscar
	 * @return Edificio con el id solicitado
	 * @throws EdificioNoEncontradoException - Excepcion lanzada cuando no se encuentra un Edificio con el id solicitado
	 * @throws EdificioException - Excepcion general
	 */
	public Edificio buscarPorId(Integer id) throws EdificioNoEncontradoException, EdificioException;
	
	/**
	 * Método que sirve para editar edificio
	 * @param entidad - entidad, entidad edificio ha ser editada
	 * @return, retorna verdadero si se ejecuto la edición
	 * @throws Exception - Exception excepción general
	 * @throws EdificioException - EdificioException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean editar(Edificio entidad) throws EdificioException, Exception;
	
	/**
	 * Método que sirve para agregar un nuevo edificio
	 * @param entidad - entidad entidad, que tiene la informacion de todo el edificio
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws EdificioException - EdificioException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean nuevo(Edificio entidad) throws EdificioException, Exception;
	
	/**
	 * Método que sirve para eliminar un edificio
	 * @param idEdificio - idEdificio, id del edificio eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws EdificioException - EdificioException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int idEdificio) throws EdificioException, Exception;
	
//	/**
//	 * Lista todas los Edificios existentes en la BD
//	 * @return lista de todas los edificios existentes en la BD
//	 */
//	public List<Edificio> listarTodos() throws EdificioNoEncontradoException;
	
	
	
}
