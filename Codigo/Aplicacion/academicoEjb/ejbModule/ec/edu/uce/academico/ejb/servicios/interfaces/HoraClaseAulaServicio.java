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

 ARCHIVO:     HoraClaseAulaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla HoraClaseAula.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
05-07-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClaseAula;

/**
 * Interface HoraClaseAulaServicio
 * Interfase que define las operaciones sobre la tabla HoraClaseAula.
 * @author lmquishpei
 * @version 1.0
 */
public interface HoraClaseAulaServicio {
	/**
	 * Busca una entidad HoraClaseAula por su id
	 * @param id - del HoraClaseAula a a buscar
	 * @return HoraClaseAula con el id solicitado
	 * @throws HoraClaseAulaNoEncontradoException - Excepcion lanzada cuando no se encuentra una HoraClaseAula con el id solicitado
	 * @throws HoraClaseAulaException - Excepcion general
	 */
//	public HoraClaseAula buscarPorId(Integer id) throws HoraClaseAulaNoEncontradoException, HoraClaseAulaException;

	
	/**
	 * Busca una entidad HoraClaseAula por  id del Aula para eliminar sino existe la relacion
	 * @param id - del HoraClaseAula a a buscar
	 * @return true si encuentra por lo menos un HoraClaseAula con idAula o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	public boolean buscarPorIdAula(Integer idAula);
	

	/**
	 * Añade  horaClase por cada aula en la BD
	 * @return Si se añadio o no correctamente toda la lista de horas clase a el aula
	 * @throws HoraClaseAulaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws HoraClaseAulaException - Excepción general
	 */
	public boolean grabar(Aula aula , List<HoraClase> listaHorasClase  ) throws  HoraClaseAulaException;
	
	
	/**
	 * Busca Lista de  HoraClaseAula por  id del Aula 
	 * @param idAula - id del Aula a a buscar
	 * @return Lista de  HoraClaseAula con idAula. 
	 * 
	 */	
	public List<HoraClaseAula> ListarHoraClaseAulaPIdAla(Integer idAula) throws  HoraClaseAulaException, HoraClaseAulaNoEncontradoException;
	
	/**
	 * Busca si existe al meos una  entidad HoraClaseAula por  id del Aula y el id de hora clase 
	 * @param alaId - alaId id del aula a buscar
	 * @param hoclId - hoclId id del hora clase a buscar
	 * @return retorna la entidad hora clase aula buscada por los parametros ingresados. 
	 * @throws HoraClaseAulaNoEncontradoException  - excepción lanzada cuando no se encuentra hora clase aula
	 * @throws HoraClaseAulaValidacionException - cuando existen mas de un registro
	 * @throws HoraClaseAulaException - excepción generan lanzada
	 */
	public HoraClaseAula buscarPalaIdPhoclId(Integer alaId, Integer hoclId) throws HoraClaseAulaNoEncontradoException, HoraClaseAulaValidacionException, HoraClaseAulaException ;
	
}
