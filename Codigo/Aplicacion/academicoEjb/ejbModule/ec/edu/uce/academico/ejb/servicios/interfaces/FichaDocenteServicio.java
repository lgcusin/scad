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

 ARCHIVO:     FichaEstudianteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla FichaDocente.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 12/04/2018            Daniel Albuja                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.academico.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;

/**
 * Interface FichaEstudianteServicio
 * Interfase que define las operaciones sobre la tabla FichaDocente.
 * @author dalbuja
 * @version 1.0
 */
public interface FichaDocenteServicio {
	/**
	 * Busca una entidad FichaDocente por su Prsid
	 * @param id - de la FichaDocente a buscar
	 * @return FichaDocente con el id solicitado
	 * @throws FichaDocenteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaDocente con el id solicitado
	 * @throws FichaDocenteException - Excepcion general
	 */
	public FichaDocente buscarPorPrsId(Integer prsId) throws FichaDocenteNoEncontradoException, FichaDocenteException;

	FichaDocente buscarPorPrsIdentificacion(String prsIdentificacion)
			throws FichaDocenteNoEncontradoException, FichaDocenteException;
	
}
