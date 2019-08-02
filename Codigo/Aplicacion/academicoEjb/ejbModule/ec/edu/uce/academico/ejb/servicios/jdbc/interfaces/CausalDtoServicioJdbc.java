/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 ************************************************************************* 

 ARCHIVO:     CausalDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla CausalDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
21-01-2019			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CausalDto;
import ec.edu.uce.academico.ejb.excepciones.CausalDtoException;
import ec.edu.uce.academico.ejb.excepciones.CausalDtoNoEncontradoException;


/**
 * Interface CausalDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla CausalDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface CausalDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas las causales por tipo de causal
	 * @param causalId - id del usuario 
	 * @return Lista de todas las causales por tipo de causal
	 * @throws CausalDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CausalDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CausalDto> listarXidTipoCausal(int tipoCausalId) throws CausalDtoException, CausalDtoNoEncontradoException;
	
	
	
}
