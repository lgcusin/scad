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

 ARCHIVO:     AsignacionEvaluadorServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
07-02-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;

/**
 * Interface AsignacionEvaluadorServicio
 * Interfase que define las operaciones sobre la tabla AsignacionEvaluador.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface GrupoServicio {

	/**
	 * Busca una entidad Grupo por su id
	 * @param id - de la Grupo a buscar
	 * @return Grupo con el id solicitado
	 * @throws GrupoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Grupo con el id solicitado
	 * @throws GrupoException - Excepcion general
	 */
	Grupo buscarPorId(Integer id) throws GrupoNoEncontradoException, GrupoException;

	/**
	 * Lista todas las entidades Grupo existentes en la BD activas
	 * @return lista de todas las entidades Grupo existentes en la BD activas
	 * @throws GrupoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Grupo 
	 * @throws GrupoException - Excepcion general
	 */
	List<Grupo> listarTodosActivos() throws GrupoNoEncontradoException , GrupoException;
	
	/**
	 * Método que permite buscar los grupos asociados a una carrera.
	 * @author fgguzman
	 * @param carreraId - id de la carrera.
	 * @return grupos asociados a la carrera solicitada
	 * @throws GrupoNoEncontradoException
	 * @throws GrupoException
	 */
	List<Grupo> buscarPorCarrera(int carreraId) throws GrupoNoEncontradoException , GrupoException;
	
	/**
	 * Método que permite crear un nuevo registro en la tabla Grupo.
	 * @author fgguzman
	 * @param entidad - Grupo.class
	 * @return true si se efectua el registro.
	 * @throws GrupoException
	 */
	boolean crear(Grupo entidad) throws GrupoException;

	/**
	 * Método que permite actualizar un registro en la tabla Grupo.
	 * @author fgguzman
	 * @param entidad - Grupo.class
	 * @return true si se efectua el registro.
	 * @throws GrupoException
	 */
	boolean editar(Grupo entidad) throws GrupoException;
	
}
