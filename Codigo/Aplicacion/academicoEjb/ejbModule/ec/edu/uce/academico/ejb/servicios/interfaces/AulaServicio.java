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

 ARCHIVO:     AulaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Aula.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
26-06-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.excepciones.AulaException;
import ec.edu.uce.academico.ejb.excepciones.AulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AulaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.Edificio;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;

/**
 * Interface AulaServicio
 * Interfase que define las operaciones sobre la tabla Aula.
 * @author lmquishpei
 * @version 1.0
 */
public interface AulaServicio {
	/**
	 * Busca una entidad Aula por su id
	 * @param id - del Aula a a buscar
	 * @return Aula con el id solicitado
	 * @throws AulaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Aula con el id solicitado
	 * @throws AulaException - Excepcion general
	 */
	public Aula buscarPorId(Integer id) throws AulaNoEncontradoException, AulaException;

	/**
	 * Lista todas las entidades Aula existentes en la BD
	 * @return lista de todas las entidades Aula existentes en la BD
	 * @throws AulaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Aula
	 * @throws AulaException - Excepcion general
	 */
	//public List<Aula> listarTodos() throws AulaoNoEncontradoException , AulaException;
	
	
	/**
	 * Añade un Aula en la BD
	 * @return Si se añadio o no el Aula
	 * @throws AulaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws AulaException - Excepción general
	 */
	public Aula anadir(AulaDto entidad , Edificio edificio , List<HoraClase> listaHorasClase ) throws AulaValidacionException, AulaException ;
	
	/**
	 * Permite editar la entidad Aula
	 * @param entidad AulaDto a editar
	 * @return True o False si fue editada la entidad Aula existente en la BD
	 * @throws AulaException 
	 * @throws AulaValidacionException 
	 */

	public Boolean editar(AulaDto entidad, List<HoraClaseAula> listHoraClaseAula) throws  AulaValidacionException, AulaException;
	


	/**
	 * Elimina un Aula
	 * @param idAula - id del aula que se quiere eliminar
	 * @return true en caso de que se complete la operacion, false caso contrario
	 * @throws AulaValidacionException - lanzada cuando el id del aula es nulo
	 * @throws AulaNoEncontradoException - lanzada cuando no se encuentra el aula con el id enviado
	 * @throws AulaException - lanzada cuando se deconoce el error al buscar el aula
	 */
	
	public boolean eliminarAula(Integer idAula, List<HoraClaseAula> listaHoraClaseAula) throws AulaValidacionException, AulaNoEncontradoException, AulaException;

	
	
	
	
	
}
