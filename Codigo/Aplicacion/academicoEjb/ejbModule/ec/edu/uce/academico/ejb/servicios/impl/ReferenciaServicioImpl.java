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

 ARCHIVO:     ReferenciaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Referencia. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 11-12-2018           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.ReferenciaException;
import ec.edu.uce.academico.ejb.excepciones.ReferenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ReferenciaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.ReferenciaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Referencia;

/**
 * Clase (Bean)ReferenciaServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class ReferenciaServicioImpl implements ReferenciaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Referencia por su id
	 * @param id - del Referencia a buscar
	 * @return Referencia con el id solicitado
	 * @throws ReferenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Referencia con el id solicitado
	 * @throws ReferenciaException - Excepcion general
	 */
	@Override
	public Referencia buscarPorId(Integer id) throws ReferenciaNoEncontradoException, ReferenciaException {
		Referencia retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Referencia.class, id);
			} catch (NoResultException e) {
				throw new ReferenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ReferenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ReferenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Referencia existentes en la BD
	 * @return lista de todas las entidades Referencia existentes en la BD
	 * @throws ReferenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Referencia 
	 * @throws ReferenciaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Referencia> listarTodos() throws ReferenciaNoEncontradoException , ReferenciaException {
		List<Referencia> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rfr from Referencia rfr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ReferenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new ReferenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.listar.todos.exception")));
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Referencia existentes en la BD por empleado
	 * @return lista de todas las entidades Referencia existentes en la BD por empleado
	 * @throws ReferenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Referencia 
	 * @throws ReferenciaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Referencia> buscarXPersona(Integer prsId) throws ReferenciaNoEncontradoException , ReferenciaException {
		List<Referencia> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rfr from Referencia rfr where ");
			sbsql.append(" rfr.rfrEstado = :estado ");
			sbsql.append(" and rfr.rfrPersona.prsId = :prsId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", ReferenciaConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("prsId", prsId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ReferenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new ReferenciaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Referencia.listar.todos.exception")));
		}
		return retorno;
	}






}
