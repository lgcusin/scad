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

 ARCHIVO:     HoraClaseServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla HoraClase.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 14-SEP-2017            MARCELO QUISHPE                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseException;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;

/**
 * Interface HoraClaseServicio
 * Interfase que define las operaciones sobre la tabla PeriodoAcademico.
 * @author lmquishpei
 * @version 1.0
 */
public interface HoraClaseServicio {
	
	/**
	 * Lista todas las entidades Horas Clase existentes en la BD
	 * @return lista de todas las entidades Horas Clase existentes en la BD
	 * @throws HoraClaseAulaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws HoraClaseException - Excepcion general
	 */
	public List<HoraClase> listarTodos() throws HoraClaseAulaNoEncontradoException, HoraClaseException;
	
	
}
