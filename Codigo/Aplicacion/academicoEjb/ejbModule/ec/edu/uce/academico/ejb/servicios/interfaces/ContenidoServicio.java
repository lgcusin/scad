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

 ARCHIVO:     ContenidoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.excepciones.ContenidoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Contenido;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface ContenidoServicio
 * Interfase que define las operaciones sobre la tabla Contenido.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface ContenidoServicio {

	/**
	 * Busca una entidad Contenido por su id
	 * @param id - deL Contenido a buscar
	 * @return Contenido con el id solicitado
	 * @throws ContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Contenido con el id solicitado
	 * @throws ContenidoException - Excepcion general
	 */
	public Contenido buscarPorId(Integer id) throws ContenidoNoEncontradoException, ContenidoException;

	/**
	 * Lista todas las entidades Contenido existentes en la BD
	 * @return lista de todas las entidades Contenido existentes en la BD
	 * @throws ContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Contenido 
	 * @throws ContenidoException - Excepcion general
	 */
	public List<Contenido> listarTodos() throws ContenidoNoEncontradoException , ContenidoException;
	
	/**
	 * Añade una Contenido en la BD
	 * @param entidad .- Entidad a ingresar
	 * @return Si se añadio o no la Contenido
	 * @throws ContenidoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws ContenidoException - Excepción general
	 */
	public Contenido anadir(Contenido entidad) throws ContenidoValidacionException, ContenidoException;
	
	
	/**
	 * Lista de entidades Contenido existentes en la BD por tipo de evalucion
	 * @param idContenido .- id del tipo de Contenido
	 * @return Lista de entidades Contenido existentes en la BD
	 * @throws ContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Contenido
	 * @throws ContenidoException - Excepcion general
	 */
	public List<Contenido> listarTodosXTipo(int idContenido) throws ContenidoNoEncontradoException , ContenidoException;
	
	
	public Boolean anadirApelacion(ContenidoEvaluacionDto entidad,Usuario usuario,Integer seleccionAux) throws ContenidoException;
}
