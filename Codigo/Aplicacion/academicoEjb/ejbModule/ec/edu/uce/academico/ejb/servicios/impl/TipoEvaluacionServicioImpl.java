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

 ARCHIVO:     TipoEvaluacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoEvaluacionServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoEvaluacion;

/**
 * Clase (Bean)TipoEvaluacionServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class TipoEvaluacionServicioImpl implements TipoEvaluacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoEvaluacion por su id
	 * @param id - deL TipoEvaluacion a buscar
	 * @return TipoEvaluacion con el id solicitado
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion con el id solicitado
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	@Override
	public TipoEvaluacion buscarPorId(Integer id) throws TipoEvaluacionNoEncontradoException, TipoEvaluacionException {
		TipoEvaluacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoEvaluacion.class, id);
			} catch (NoResultException e) {
				throw new TipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoEvaluacion existentes en la BD
	 * @return lista de todas las entidades TipoEvaluacion existentes en la BD
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoEvaluacion> listarTodos() throws TipoEvaluacionNoEncontradoException , TipoEvaluacionException {
		List<TipoEvaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpev from TipoEvaluacion tpev order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new TipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new TipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.listar.todos.exception")));
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoEvaluacion existentes en la BD con estado activo
	 * @return lista de todas las entidades TipoEvaluacion existentes en la BD
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoEvaluacion> listarActivos() throws TipoEvaluacionNoEncontradoException , TipoEvaluacionException {
		List<TipoEvaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpev from TipoEvaluacion tpev where tpev.tpevEstado =:estado ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", TipoEvaluacionConstantes.ESTADO_ACTIVO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new TipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.listar.activos.no.result.exception")));
		} catch (Exception e) {
			throw new TipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.listar.activos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades TipoEvaluacion existentes en la BD con estado activo
	 * @return lista de todas las entidades TipoEvaluacion existentes en la BD
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoEvaluacion> listarXApelacion() throws TipoEvaluacionNoEncontradoException , TipoEvaluacionException {
		List<TipoEvaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpev from TipoEvaluacion tpev where tpev.tpevEstado =:estado ");
			sbsql.append(" and tpev.tpevId in (3,4) ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", TipoEvaluacionConstantes.ESTADO_ACTIVO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new TipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.listar.activos.no.result.exception")));
		} catch (Exception e) {
			throw new TipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoEvaluacion.listar.activos.exception")));
		}
		return retorno;
	}

}