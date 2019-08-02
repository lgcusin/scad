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

 ARCHIVO:     UnidadFormacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla UnidadFormacion. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 07-AGOS-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.UnidadFormacionException;
import ec.edu.uce.academico.ejb.excepciones.UnidadFormacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.UnidadFormacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.UnidadFormacion;

/**
 * Clase (Bean)UnidadFormacionServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class UnidadFormacionServicioImpl implements UnidadFormacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad UnidadFormacion por su id
	 * @param id - de la UnidadFormacion a buscar
	 * @return UnidadFormacion con el id solicitado
	 * @throws UnidadFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una UnidadFormacion con el id solicitado
	 * @throws UnidadFormacionException - Excepcion general
	 */
	@Override
	public UnidadFormacion buscarPorId(Integer id) throws UnidadFormacionNoEncontradoException, UnidadFormacionException {
		UnidadFormacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(UnidadFormacion.class, id);
			} catch (NoResultException e) {
				throw new UnidadFormacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UnidadFormacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new UnidadFormacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UnidadFormacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new UnidadFormacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UnidadFormacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades UnidadFormacion existentes en la BD
	 * @return lista de todas las entidades UnidadFormacion existentes en la BD
	 * @throws UnidadFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una UnidadFormacion 
	 * @throws UnidadFormacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UnidadFormacion> listarTodos() throws UnidadFormacionNoEncontradoException , UnidadFormacionException {
		List<UnidadFormacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select uf from UnidadFormacion uf ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new UnidadFormacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UnidadFormacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new UnidadFormacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UnidadFormacion.listar.todos.exception")));
		}
		return retorno;
	}
	
}
