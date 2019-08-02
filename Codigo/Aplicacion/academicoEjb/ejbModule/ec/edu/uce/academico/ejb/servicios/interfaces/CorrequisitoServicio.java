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

 ARCHIVO:     CorrequisitoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el proceso de Correquisito.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-AGOS-2017         Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CorequisitoException;

/**
 * Interface CorrequisitoServicio
 * Interfase que define las operaciones sobre el proceso de Correquisito.
 * @author lmquishpei
 * @version 1.0
 */
public interface CorrequisitoServicio {
	
	
	/**
	 * Método que sirve para agregar un nuevos Correquisito a una materia
	 * @param materiaPrincipal - materiaPrincipal, entidad materia ha ser agregados los Correquisitos
	 * @param lisCorrequisitos - lisCorrequisitos, Lista de prerrequisitos a ser agregados.
	 * @return retorna verdadero si se ejecuto la transacción añadir.
	 * @throws Exception - Exception excepción general
	 * @throws CorequisitoException - CorequisitoException, excepción general.
	 */
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean anadir(MateriaDto materiaPrincipal, List<MateriaDto> listCorrequisitos) throws CorequisitoException, Exception;
	
	
	/**
	 * Método que sirve para eliminar Correqusitos de una materia
	 * @param lisPrerrequisitosEliminar - lisPrerrequisitos, Lista de prerrequisitos a ser eliminados.
	 * @return retorna verdadero si se ejecuto la transacción eliminar.
	 * @throws Exception - Exception excepción general
	 * @throws CorequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(List<MateriaDto> listCorrequisitosEliminar) throws CorequisitoException, Exception;
	
	/**
	 * Método que sirve para guardar correquisitos de una materia
	 * @param materiaCorequisito -  entidad materia ha ser creada o editada 
	 * @return Correquisito agregado o editado
	 * @throws Exception - Exception excepción general
	 * @throws CorequisitoException - CorequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean guardar(List<MateriaDto> listCorrequisitos) throws CorequisitoException, Exception;
}
