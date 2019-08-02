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

 ARCHIVO:     MatriculaIntercambioServicioJdbc.java      
 DESCRIPCIÓN: Interfaz que define las operaciones para el proceso de matriculas de intercambio.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 05-OCT-2018            Freddy Guzman   	                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;


import java.util.List;

import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioNoEncontradoException;


/**
 * Interfaz MatriculaIntercambioServicioJdbc
 * Interfaz que define las operaciones del proceso matriculas de intercambio.
 * @author fgguzman
 * @version 1.0
 */
public interface MatriculaIntercambioServicioJdbc {

	/**
	 * Método que permite recuperar las materias en las que Relaciones Internacionales inscribio al estudiante.
	 * @param fcinId - id de la ficha del estudiante de intercambio
	 * @return Materias y Carreras inscrito.
	 * @throws CarreraIntercambioNoEncontradoException
	 * @throws CarreraIntercambioException
	 */
	List<MateriaDto> buscarMateriasIscripcion(List<FichaInscripcionDto> fichasInscripcion) throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException;

}
