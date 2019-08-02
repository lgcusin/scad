/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     ModalidadServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Modalidad. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-03-2019           Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.ModalidadException;
import ec.edu.uce.academico.ejb.excepciones.ModalidadNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ModalidadServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Modalidad;

/**
 * Clase (Bean)ModalidadServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0b
 */

@Stateless
public class ModalidadServicioImpl implements ModalidadServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Modalidad por su id
	 * @param id - del Modalidad a buscar
	 * @return Modalidad con el id solicitado
	 * @throws ModalidadNoEncontradoException - Excepcion lanzada cuando no se encuentra un Modalidad con el id solicitado
	 * @throws ModalidadException - Excepcion general
	 */
	@Override
	public Modalidad buscarPorId(Integer id) throws ModalidadNoEncontradoException, ModalidadException {
		Modalidad retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Modalidad.class, id);
			} catch (NoResultException e) {
				throw new ModalidadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Modalidadb.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new ModalidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Modalidad.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new ModalidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Modalidad.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Modalidad existentes en la BD
	 * @return lista de todas las entidades Modalidad existentes en la BD
	 * @throws ModalidadNoEncontradoException - Excepcion lanzada cuando no se encuentra un Modalidad
	 * @throws ModalidadException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Modalidad> listarTodos() throws ModalidadNoEncontradoException, ModalidadException {
		List<Modalidad> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mdl from Modalidad mdl ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new ModalidadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Modalidad.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new ModalidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Modalidad.listar.todos.non.unique.result.exception")));
		} catch (Exception e) {
			throw new ModalidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Modalidad.listar.todos.exception")));
		}
		
		return retorno;
	}
	

}
