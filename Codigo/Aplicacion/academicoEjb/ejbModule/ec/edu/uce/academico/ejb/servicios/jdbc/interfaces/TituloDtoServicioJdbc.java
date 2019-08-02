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

 ARCHIVO:     TituloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla TituloDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 29-NOV-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.TituloDto;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoException;
import ec.edu.uce.academico.ejb.excepciones.TituloDtoNoEncontradoException;
/**
 * Interface TituloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla TituloDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface TituloDtoServicioJdbc {

	/**
	 * Realiza la busqueda de Titulos académicos  tipo (secundario, 3erNivel, 4to Nivel)
	 * @param tipoId - id del tipo  a buscar
	 * @return Lista de todas los instituciones académicas por cantón y tipo
	 * @throws TituloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoNoEncontradoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<TituloDto> listarXTipo(int tipoId) throws TituloDtoException, TituloDtoNoEncontradoException;	
}
