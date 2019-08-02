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

 ARCHIVO:     MallaCurricularNivelServicio.java      
 DESCRIPCIÓN: Interfaz que define las operaciones sobre la tabla MallaCurricularNivel.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 07-Ene-2019            Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNivelValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularNivel;

/**
 * Interface MallaCurricularNivelServicio
 * Interfase que define las operaciones sobre la tabla MallaCurricularNivel.
 * @author fgguzman
 * @version 1.0
 */
public interface MallaCurricularNivelServicio {
	
	/**
	 * Método que permite guardar y actualizar los registros de la tabla MallaCurricularNivel de todas las 
	 * Carreras de Pregrado.
	 * @author fgguzman 
	 * @throws MallaCurricularNivelValidacionException
	 * @throws MallaCurricularNivelException
	 */
	void registrarCreditosPorNivel() throws MallaCurricularNivelValidacionException, MallaCurricularNivelException;
	
	/**
	 * Método que pérmite actualizar la tabla MallaCurricularNivel.
	 * @author fgguzman
	 * @param crrId - id de la MallaCurricular
	 * @throws MallaCurricularNivelValidacionException
	 * @throws MallaCurricularNivelException
	 */
	void actualizarCreditosPorCarrera(int crrId) throws MallaCurricularNivelValidacionException, MallaCurricularNivelException;
	
	/**
	 * Método que retorno un registro tipo MallaCurricularNivel.
	 * @author fgguzman
	 * @param mlcrId - id de la MallaCurricular
	 * @param nvlId - id del Nivel
	 * @return objeto MallaCurricularNivel
	 * @throws MallaCurricularNivelNoEncontradoException
	 * @throws MallaCurricularNivelException
	 */
	MallaCurricularNivel  buscarPorMallaCurricularNivel(int mlcrId, int nvlId) throws MallaCurricularNivelNoEncontradoException, MallaCurricularNivelValidacionException, MallaCurricularNivelException;
	
	
	/**
	 * 
	 * @param carreId
	 * @return
	 * @throws MallaCurricularNivelNoEncontradoException
	 * @throws MallaCurricularNivelValidacionException
	 * @throws MallaCurricularNivelException
	 */
	List<MallaCurricularNivel> buscarCreditosPorNivel(int carreId) throws MallaCurricularNivelNoEncontradoException, MallaCurricularNivelValidacionException, MallaCurricularNivelException;
}
