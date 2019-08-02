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

 ARCHIVO:     CronogramaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CronogramaServicioImpl. 
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
import ec.edu.uce.academico.ejb.excepciones.CronogramaException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CronogramaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Cronograma;

/**
 * Clase (Bean)CronogramaServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class CronogramaServicioImpl implements CronogramaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Cronograma por su id
	 * @param id - deL Cronograma a buscar
	 * @return Cronograma con el id solicitado
	 * @throws CronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Cronograma con el id solicitado
	 * @throws CronogramaException - Excepcion general
	 */
	@Override
	public Cronograma buscarPorId(Integer id) throws CronogramaNoEncontradoException, CronogramaException {
		Cronograma retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Cronograma.class, id);
			} catch (NoResultException e) {
				throw new CronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.buscar.por.id.exception")));
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
	public List<Cronograma> listarTodos() throws CronogramaNoEncontradoException , CronogramaException {
		List<Cronograma> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crn from Cronograma crn ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @param periodoAcademicoId - id del período academico a buscar
	 * @param estado - del cronograma a buscar 
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CronogramaException - Excepcion general
	 */
	@Override
	public Cronograma buscarXperiodoAcademicoXestado(int periodoAcademicoId, int estado) throws CronogramaNoEncontradoException , CronogramaException {
		Cronograma retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crn from Cronograma crn ");
			sbsql.append(" Where crn.crnPeriodoAcademico.pracId =:periodoAcademicoId");
			sbsql.append(" And crn.crnEstado =:estado");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("periodoAcademicoId", periodoAcademicoId);
			q.setParameter("estado", estado);
			retorno = (Cronograma)q.getSingleResult();
		} catch (NoResultException e) {
			throw new CronogramaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.buscar.por.periodoAcademico.por.estado.no.result.exception")));
		} catch (Exception e) {
			throw new CronogramaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cronograma.buscar.por.periodoAcademico.por.estado.exception")));
		}
		return retorno;
	}
	
	
	
}
