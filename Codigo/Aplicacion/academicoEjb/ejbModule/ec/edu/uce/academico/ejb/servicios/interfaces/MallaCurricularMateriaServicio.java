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

 ARCHIVO:     MallaCurricularMateriaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el proceso de MallaCurricularMateria.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 08-AGOS-2017          Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;

/**
 * Interface MallaCurricularMateriaServicio
 * Interfase que define las operaciones sobre el proceso de MallaCurricularMateria.
 * @author dcollaguazo
 * @version 1.0
 */
public interface MallaCurricularMateriaServicio {
	
	/**
	 * Método que sirve para editar la malla curricular materia
	 * @param entidad - entidad, entidad malla curricular materia ha ser editada
	 * @return, retorna verdadero si se ejecuto la edición
	 * @throws Exception - Exception excepción general
	 * @throws MallaCurricularMateriaException - MallaCurricularMateriaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean editar(MallaCurricularMateria entidad) throws MallaCurricularMateriaException, Exception;
	
	/**
	 * Método que sirve para agregar una nueva malla curricular materia
	 * @param idUnidadFormacion - idUnidadFormacion, entidad malla curricular materia ha ser editada
	 * @param idNivel - idNivel entidad, entidad malla curricular materia ha ser editada
	 * @param idMallaCurricular - idMallaCurricular entidad, entidad malla curricular materia ha ser editada
	 * @param listMateria - listMateria entidad, entidad malla curricular materia ha ser editada
	 * @param estado - estado entidad, entidad malla curricular materia ha ser editada
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws MallaCurricularMateriaException - MallaCurricularMateriaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean nuevo(int idUnidadFormacion, int idNivel, int idMallaCurricular, List<MateriaDto> listMateria, int estado) throws MallaCurricularMateriaException, Exception;
	
	/**
	 * Método que sirve para eliminar una malla curricular materia
	 * @param idMallaMateria - idMallaMateria, id de la malla curricular materia a eliminar
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws MallaCurricularMateriaException - MallaCurricularMateriaException, excepción genearal.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(int idMallaMateria) throws MallaCurricularMateriaException, Exception;

	MallaCurricularMateria buscarPorId(Integer prlId)
			throws MallaCurricularMateriaNoEncontradoException, MallaCurricularMateriaException;

	MallaCurricularMateria buscarPorMtrIdPorEstado(Integer mtrId, Integer estado)
			throws MallaCurricularMateriaNoEncontradoException, MallaCurricularMateriaException;
}
