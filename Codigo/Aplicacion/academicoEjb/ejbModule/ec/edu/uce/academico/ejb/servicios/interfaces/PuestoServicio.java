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
20-10-2017            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.PuestoException;
import ec.edu.uce.academico.ejb.excepciones.PuestoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;

/**
 * Interface PuestoServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface PuestoServicio {

	/**
	 * Busca una entidad Puesto por su id
	 * @param id - deL Puesto a buscar
	 * @return Puesto con el id solicitado
	 * @throws PuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Puesto con el id solicitado
	 * @throws PuestoException - Excepcion general
	 */
	Puesto buscarPorId(Integer id) throws PuestoNoEncontradoException, PuestoException;

	/**
	 * Lista todas las entidades Puesto existentes en la BD
	 * @return lista de todas las entidades Puesto existentes en la BD
	 * @throws PuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws PuestoException - Excepcion general
	 */
	List<Puesto> listarTodos() throws PuestoNoEncontradoException , PuestoException;
	
	/**
	 * Busca la entidad Puesto existente en la BD
	 * @return entidad Puesto existente en la BD
	 * @throws PuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws PuestoException - Excepcion general
	 */
	Puesto buscarPuesto(int dedicacion, int categoria, int rango) throws PuestoNoEncontradoException , PuestoException;
	
	/**
	 * Método que permite recuperar las categorias disponibles.
	 * @author fgguzman
	 * @param tmddId - id del Tiempo de Dedicacion
	 * @return Categorias
	 * @throws PuestoNoEncontradoException
	 * @throws PuestoException
	 */
	List<Puesto> buscarPorTiempoDedicacion(int tmddId) throws PuestoNoEncontradoException , PuestoException;
	
	/**
	 * Método que permite recuperar los niveles del rango gradual.
	 * @author fgguzman
	 * @param tmddId - id del tiempo de dedicacion.
	 * @param categoriaId - id de la categoria
	 * @return niveles disponibles.
	 * @throws PuestoNoEncontradoException
	 * @throws PuestoException
	 */
	List<Puesto> buscarPorTiempoDedicacionCategoria(int tmddId, int categoriaId) throws PuestoNoEncontradoException , PuestoException;
}
