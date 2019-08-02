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

 ARCHIVO:     TipoFuncionCargaHorariaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoFuncionCargaHoraria.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 31-08-2017            Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TipoFuncionCargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.TipoFuncionCargaHorariaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFuncionCargaHoraria;

/**
 * Interface TipoFuncionCargaHorariaServicio
 * Interfase que define las operaciones sobre la tabla TipoFuncionCargaHoraria.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface TipoFuncionCargaHorariaServicio {
	/**
	 * Busca una entidad TipoFuncionCargaHoraria por su id
	 * @param id - del TipoFuncionCargaHoraria a buscar
	 * @return TipoFuncionCargaHoraria con el id solicitado
	 * @throws TipoFuncionCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoFuncionCargaHoraria con el id solicitado
	 * @throws TipoFuncionCargaHorariaException - Excepcion general
	 */
	public TipoFuncionCargaHoraria buscarPorId(Integer id) throws TipoFuncionCargaHorariaNoEncontradoException, TipoFuncionCargaHorariaException;

	/**
	 * Lista todas las entidades TipoFuncionCargaHoraria existentes en la BD
	 * @param idTipoCrHr - del tipoCargaHoraria a buscar
	 * @return lista de todas las entidades TipoFuncionCargaHoraria existentes en la BD
	 */
	public List<TipoFuncionCargaHoraria> listarTodosActivosXTipo(int idTipoCargaHoraria) throws TipoFuncionCargaHorariaNoEncontradoException, TipoFuncionCargaHorariaException;
	
}
