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

 ARCHIVO:     EvaluadorEvaluadoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla EvaluadorEvaluadoServicioImpl. 
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

import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluadorEvaluadoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.EvaluadorEvaluado;

/**
 * Clase (Bean)EvaluadorEvaluadoServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class EvaluadorEvaluadoServicioImpl implements EvaluadorEvaluadoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad EvaluadorEvaluado por su id
	 * @param id - deL EvaluadorEvaluado a buscar
	 * @return EvaluadorEvaluado con el id solicitado
	 * @throws EvaluadorEvaluadoNoEncontradoException - Excepcion lanzada cuando no se encuentra una EvaluadorEvaluado con el id solicitado
	 * @throws EvaluadorEvaluadoException - Excepcion general
	 */
	@Override
	public EvaluadorEvaluado buscarPorId(Integer id) throws EvaluadorEvaluadoNoEncontradoException, EvaluadorEvaluadoException {
		EvaluadorEvaluado retorno = null;
		if (id != null) {
			try {
				retorno = em.find(EvaluadorEvaluado.class, id);
			} catch (NoResultException e) {
				throw new EvaluadorEvaluadoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new EvaluadorEvaluadoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new EvaluadorEvaluadoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades EvaluadorEvaluado existentes en la BD
	 * @return lista de todas las entidades EvaluadorEvaluado existentes en la BD
	 * @throws EvaluadorEvaluadoNoEncontradoException - Excepcion lanzada cuando no se encuentra una EvaluadorEvaluado
	 * @throws EvaluadorEvaluadoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EvaluadorEvaluado> listarTodos() throws EvaluadorEvaluadoNoEncontradoException , EvaluadorEvaluadoException {
		List<EvaluadorEvaluado> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from EvaluadorEvaluado evl order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EvaluadorEvaluadoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluadorEvaluadoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.listar.todos.exception")));
		}
		return retorno;
	}
 
	/**
	 * Añade una EvaluadorEvaluado en la BD
	 * @return Si se añadio o no la EvaluadorEvaluado
	 * @throws EvaluadorEvaluadoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws EvaluadorEvaluadoException - Excepción general
	 */
	@Override
	public EvaluadorEvaluado anadir(EvaluadorEvaluado entidad) throws EvaluadorEvaluadoValidacionException, EvaluadorEvaluadoException {
		EvaluadorEvaluado retorno = null;
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new EvaluadorEvaluadoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.anadir.exception")));
			}
		} else {
			throw new EvaluadorEvaluadoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EvaluadorEvaluado.anadir.null.exception")));
		}
		return retorno;
	}

	 


}