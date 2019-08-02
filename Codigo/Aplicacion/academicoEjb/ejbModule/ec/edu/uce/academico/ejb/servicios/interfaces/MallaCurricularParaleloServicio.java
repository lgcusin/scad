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

 ARCHIVO:     MallaCurricularParaleloServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla MallaCurricularParalelo.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 14-AGOS-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;

/**
 * Interface MallaCurricularServicio
 * Interfase que define las operaciones sobre la tabla MallaCurricularParalelo.
 * @author dcollaguazo
 * @version 1.0
 */
public interface MallaCurricularParaleloServicio {
	
	/**
	 * Busca la entidades MallaCurricularParalelo por id de la malla curricular materia
	 * @param idMallaMateria - idMallaMateria id de la malla curricular materia
	 * @return verdadero si encuentra falso caso contrario
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricularParalelo 
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	public boolean buscarXMallaMateria(int idMallaMateria) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloException;

	boolean eliminarPmlcrprId(int idMallaCurricularParalelo) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	List<MallaCurricularParalelo> buscarXParaleloId(int idParalelo) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	/**
	 * Busca una entidad MallaCurricularParalelo por su id
	 * @param id - de la malla curricular paralelo a buscar
	 * @return MallaCurricularParalelo con el id solicitado
	 * @throws MallaCurricularParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una malla curricular paralelo con el id solicitado
	 * @throws MallaCurricularParaleloException - Excepcion general
	 */
	public MallaCurricularParalelo buscarPorId(Integer prlId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException ;
	
	public MallaCurricularParalelo buscarPorMlcrmtPorPrlId(Integer mlcrmt,Integer prlId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException ;

	public void editarCupoPorMlcrprId(Integer mlcrprId,Integer nuevoCupo) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException ;
	
	public void editarCupoPorMlcrprIdReserva(Integer mlcrprId, Integer nuevoCupo) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException ;

	public MallaCurricularParalelo buscarPorMtrIdPrsIdentificacion(Integer materiaId, String cedula, Integer nivelId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	MallaCurricularParalelo buscarPorMlcrprIdPorMtrId(Integer prlId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	MallaCurricularParalelo buscarXParaleloIdXMateriaId(Integer mtrId, Integer prlPracId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	MallaCurricularParalelo actualizarParaleloCupo(MallaCurricularParalelo mlcrpr) throws MallaCurricularParaleloValidacionException, MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException ;

	/**
	 * Método que permite recuperar un objeto tipo MallaCurricularParalelo segun el id del Paralelo.
	 * @author fgguzman
	 * @param paraleloId 
	 * @return MallaCurricularParalelo.
	 * @throws MallaCurricularParaleloNoEncontradoException
	 * @throws MallaCurricularParaleloValidacionException
	 * @throws MallaCurricularParaleloException
	 */
	MallaCurricularParalelo buscarPorParalelo(int paraleloId) throws MallaCurricularParaleloNoEncontradoException , MallaCurricularParaleloValidacionException, MallaCurricularParaleloException ;
}
