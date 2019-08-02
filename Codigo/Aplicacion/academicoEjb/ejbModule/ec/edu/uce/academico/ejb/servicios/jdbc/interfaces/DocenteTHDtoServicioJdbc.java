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

 ARCHIVO:     DocenteTHDtoServicioJdbc.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la base de talento humano, docente.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 28/05/2019            Dennis Collaguazo	                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import ec.edu.uce.academico.ejb.dtos.DocenteTHJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.DocenteTHDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteTHDtoNoEncontradoException;

/**
 * Interface DocenteTHDtoServicioJdbc
 * Interfase que define las operaciones sobre la base de talento humano, docente.
 * @author dcollaguazo
 * @version 1.0
 */
public interface DocenteTHDtoServicioJdbc {
	
	/**
	 * Método que busca el docente por cédula en la base de talento humano
	 * @param cedula - cedula del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado en la base de talento humano
	 * @throws DocenteTHDtoNoEncontradoException - DocenteTHDtoNoEncontradoException lanzada cuando no se encuentra docentes
	 * @throws DocenteTHDtoException - DocenteTHDtoException excepción general lanzada 
	 */
	public DocenteTHJdbcDto buscarDocenteTHXCedula(String cedula) throws DocenteTHDtoNoEncontradoException, DocenteTHDtoException;
	
}
