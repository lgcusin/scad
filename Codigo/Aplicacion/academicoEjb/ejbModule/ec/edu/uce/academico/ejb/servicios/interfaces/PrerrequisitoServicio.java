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

 ARCHIVO:     PrerrequisitoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el proceso de Prerrequisito.  
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
import ec.edu.uce.academico.ejb.excepciones.PrerequisitoException;

/**
 * Interface PrerrequisitoServicio
 * Interfase que define las operaciones sobre el proceso de Prerrequisito.
 * @author lmquishpei
 * @version 1.0
 */
public interface PrerrequisitoServicio {
	
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	//public boolean editar(MallaCurricularMateria entidad) throws MallaCurricularMateriaException, Exception;
	
	/**
	 * Método que sirve para agregar un nuevos prerequisito a una materia
	 * @param materiaPrincipal - materiaPrincipal, entidad materia ha ser agregados los prerrequisitos
	 * @param lisPrerrequisitos - lisPrerrequisitos, Lista de prerrequisitos a ser agregados.
	 * @return, retorna verdadero si se ejecuto la transacción añadir.
	 * @throws Exception - Exception excepción general
	 * @throws PrerequisitoException - PrerequisitoException, excepción general.
	 */
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean anadir(MateriaDto materiaPrincipal, List<MateriaDto> listPrerrequisitos) throws PrerequisitoException, Exception;
	
	
	/**
	 * Método que sirve para eliminar prerrequsitos de una materia
	*  @param materiaPrincipal - materiaPrincipal, entidad materia ha ser eliminados los prerrequisitos
	 * @param lisPrerrequisitosEliminar - lisPrerrequisitos, Lista de prerrequisitos a ser eliminados.
	 * @return, retorna verdadero si se ejecuto la transacción eliminar.
	 * @throws Exception - Exception excepción general
	 * @throws PrerequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean eliminar(List<MateriaDto> listPrerrequisitosEliminar) throws PrerequisitoException, Exception;
	
	
	/**
	 * Método que sirve para guardar o editar campo estado(activo-inactivo) prerrequisitos de una materia
	*  @param materiaPrerrequisito -  entidad materia ha ser creada o editada 
	 * @return Prerrequisito agregado o editado
	 * @throws PrerequisitoException - PrerequisitoException, excepción general.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean guardar(List<MateriaDto> listPrerrequisitos) throws PrerequisitoException, Exception;
}
