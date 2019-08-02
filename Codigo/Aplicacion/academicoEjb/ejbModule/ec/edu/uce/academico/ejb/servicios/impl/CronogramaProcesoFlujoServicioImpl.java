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

 ARCHIVO:     CronogramaProcesoFlujoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CronogramaProcesoFlujoServicio. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-03-2017           David Arellano                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaProcesoFlujoException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaProcesoFlujoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CronogramaProcesoFlujo;

/**
 * Clase (Bean)CronogramaProcesoFlujoServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class CronogramaProcesoFlujoServicioImpl implements CronogramaProcesoFlujoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad CronogramaProcesoFlujo por su id
	 * @param id - deL CronogramaProcesoFlujo a buscar
	 * @return CronogramaProcesoFlujo con el id solicitado
	 * @throws CronogramaProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una CronogramaProcesoFlujo con el id solicitado
	 * @throws CronogramaProcesoFlujoException - Excepcion general
	 */
	@Override
	public CronogramaProcesoFlujo buscarPorId(Integer id) throws CronogramaProcesoFlujoNoEncontradoException, CronogramaProcesoFlujoException {
		CronogramaProcesoFlujo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(CronogramaProcesoFlujo.class, id);
			} catch (NoResultException e) {
				throw new CronogramaProcesoFlujoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CronogramaProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CronogramaProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CronogramaProcesoFlujo> listarTodos() throws CronogramaProcesoFlujoNoEncontradoException , CronogramaProcesoFlujoException {
		List<CronogramaProcesoFlujo> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crprfl from CronogramaProcesoFlujo crprfl ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CronogramaProcesoFlujoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CronogramaProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@Override
	public CronogramaProcesoFlujo buscarXcronogramaXprocesoFlujo(int cronogramaId, int procesoFlujoId) throws CronogramaProcesoFlujoNoEncontradoException , CronogramaProcesoFlujoException {
		CronogramaProcesoFlujo retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crprfl from CronogramaProcesoFlujo crprfl ");
			sbsql.append(" Where crprfl.crprProcesoFlujo.prflId =:procesoFlujoId");
			sbsql.append(" And crprfl.crprCronograma.crnId =:cronogramaId");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("procesoFlujoId", procesoFlujoId);
			q.setParameter("cronogramaId", cronogramaId);
			retorno = (CronogramaProcesoFlujo)q.getSingleResult();
		} catch (NoResultException e) {
			throw new CronogramaProcesoFlujoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.buscar.por.cronograma.por.procesoFlujo.no.result.exception")));
		} catch (Exception e) {
			throw new CronogramaProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CronogramaProcesoFlujo.buscar.por.cronograma.por.procesoFlujo.exception")));
		}
		return retorno;
	}
	
}
