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

 ARCHIVO:     VigenciaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Vigencia. 
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

import ec.edu.uce.academico.ejb.excepciones.VigenciaException;
import ec.edu.uce.academico.ejb.excepciones.VigenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.VigenciaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Vigencia;

/**
 * Clase (Bean)VigenciaServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0b
 */

@Stateless
public class VigenciaServicioImpl implements VigenciaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Vigencia por su id
	 * @param id - del Vigencia a buscar
	 * @return Vigencia con el id solicitado
	 * @throws VigenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Vigencia con el id solicitado
	 * @throws VigenciaException - Excepcion general
	 */
	@Override
	public Vigencia buscarPorId(Integer id) throws VigenciaNoEncontradoException, VigenciaException {
		Vigencia retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Vigencia.class, id);
			} catch (NoResultException e) {
				throw new VigenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vigenciab.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new VigenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vigencia.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new VigenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vigencia.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Vigencia existentes en la BD
	 * @return lista de todas las entidades Vigencia existentes en la BD
	 * @throws VigenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Vigencia
	 * @throws VigenciaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Vigencia> listarTodos() throws VigenciaNoEncontradoException, VigenciaException {
		List<Vigencia> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select vgn from Vigencia vgn ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new VigenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vigencia.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new VigenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vigencia.listar.todos.non.unique.result.exception")));
		} catch (Exception e) {
			throw new VigenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Vigencia.listar.todos.exception")));
		}
		
		return retorno;
	}
	

}
