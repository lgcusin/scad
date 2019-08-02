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

 ARCHIVO:     TipoContenidoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoContenidoServicioImpl. 
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

import ec.edu.uce.academico.ejb.excepciones.TipoContenidoException;
import ec.edu.uce.academico.ejb.excepciones.TipoContenidoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoContenidoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoContenidoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;

/**
 * Clase (Bean)TipoContenidoServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class TipoContenidoServicioImpl implements TipoContenidoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoContenido por su id
	 * @param id - deL TipoContenido a buscar
	 * @return TipoContenido con el id solicitado
	 * @throws TipoContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoContenido con el id solicitado
	 * @throws TipoContenidoException - Excepcion general
	 */
	@Override
	public TipoContenido buscarPorId(Integer id) throws TipoContenidoNoEncontradoException, TipoContenidoException {
		TipoContenido retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoContenido.class, id);
			} catch (NoResultException e) {
				throw new TipoContenidoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TipoContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TipoContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoContenido existentes en la BD
	 * @return lista de todas las entidades TipoContenido existentes en la BD
	 * @throws TipoContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoContenido
	 * @throws TipoContenidoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoContenido> listarTodos() throws TipoContenidoNoEncontradoException , TipoContenidoException {
		List<TipoContenido> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpcn from TipoContenido tpcn order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new TipoContenidoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new TipoContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.listar.todos.exception")));
		}
		return retorno;
	}

  	/**
	 * Lista de entidades TipoContenido existentes en la BD por tipo de evalucion
	 * @param idTipoTipoContenido .- id del tipo de TipoContenido
	 * @return Lista de entidades TipoContenido existentes en la BD
	 * @throws TipoContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoContenido
	 * @throws TipoContenidoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoContenido> listarTodosXTipo(int idTipoTipoContenido) throws TipoContenidoNoEncontradoException , TipoContenidoException {
		List<TipoContenido> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpcn from TipoContenido tpcn where ");
			sbsql.append(" tpcn.tpcnTipo =:idTipoTipoContenido ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("idTipoTipoContenido", idTipoTipoContenido);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new TipoContenidoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.listar.todos.x.tipo.no.result.exception")));
		} catch (Exception e) {
			throw new TipoContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.listar.todos.x.tipo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Añade una TipoContenido en la BD
	 * @return Si se añadio o no la TipoContenido
	 * @throws TipoContenidoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws TipoContenidoException - Excepción general
	 */
	@Override
	public TipoContenido anadir(TipoContenido entidad) throws TipoContenidoValidacionException, TipoContenidoException {
		TipoContenido retorno = null;
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new TipoContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.anadir.exception")));
			}
		} else {
			throw new TipoContenidoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoContenido.anadir.null.exception")));
		}
		return retorno;
	} 

}