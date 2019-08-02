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

 ARCHIVO:     TpcnFuncionTpevServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;

/**
 * Interface TpcnFuncionTpevServicio
 * Interfase que define las operaciones sobre la tabla TpcnFuncionTpev.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface TpcnFuncionTpevServicio {

	/**
	 * Busca una entidad TpcnFuncionTpev por su id
	 * @param id - deL TpcnFuncionTpev a buscar
	 * @return TpcnFuncionTpev con el id solicitado
	 * @throws TpcnFuncionTpevNoEncontradoException - Excepcion lanzada cuando no se encuentra una TpcnFuncionTpev con el id solicitado
	 * @throws TpcnFuncionTpevException - Excepcion general
	 */
	public TpcnFuncionTpev buscarPorId(Integer id) throws TpcnFuncionTpevNoEncontradoException, TpcnFuncionTpevException;

	/**
	 * La entidad TpcnFuncionTpev existentes en la BD
	 * @param  idTipoContenido .- Id del tipo contenido
	 * @return Entidad TpcnFuncionTpev existentes en la BD
	 * @throws TpcnFuncionTpevNoEncontradoException - Excepcion lanzada cuando no se encuentra una TpcnFuncionTpev
	 * @throws TpcnFuncionTpevException - Excepcion general
	 */
	public TpcnFuncionTpev buscarXTipoContenido(int idTipoContenido) throws TpcnFuncionTpevNoEncontradoException , TpcnFuncionTpevException;
}
