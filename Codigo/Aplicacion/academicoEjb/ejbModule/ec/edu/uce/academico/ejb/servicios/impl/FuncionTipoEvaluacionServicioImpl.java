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

 ARCHIVO:     FuncionTipoEvaluacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla FuncionTipoEvaluacionServicioImpl. 
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

import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.FuncionTipoEvaluacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.FuncionTipoEvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;

/**
 * Clase (Bean)FuncionTipoEvaluacionServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class FuncionTipoEvaluacionServicioImpl implements FuncionTipoEvaluacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad FuncionTipoEvaluacion por su id
	 * @param id - deL FuncionTipoEvaluacion a buscar
	 * @return FuncionTipoEvaluacion con el id solicitado
	 * @throws FuncionTipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FuncionTipoEvaluacion con el id solicitado
	 * @throws FuncionTipoEvaluacionException - Excepcion general
	 */
	@Override
	public FuncionTipoEvaluacion buscarPorId(Integer id) throws FuncionTipoEvaluacionNoEncontradoException, FuncionTipoEvaluacionException {
		FuncionTipoEvaluacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(FuncionTipoEvaluacion.class, id);
			} catch (NoResultException e) {
				throw new FuncionTipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FuncionTipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FuncionTipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades FuncionTipoEvaluacion existentes en la BD
	 * @return lista de todas las entidades FuncionTipoEvaluacion existentes en la BD
	 * @throws FuncionTipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FuncionTipoEvaluacion
	 * @throws FuncionTipoEvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FuncionTipoEvaluacion> listarTodos() throws FuncionTipoEvaluacionNoEncontradoException , FuncionTipoEvaluacionException {
		List<FuncionTipoEvaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fntpev from FuncionTipoEvaluacion fntpev order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new FuncionTipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new FuncionTipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.listar.todos.exception")));
		}
		return retorno;
	}

	/**
	 * Entidad FuncionTipoEvaluacion existentes en la BD con estado activo y por tipo de FuncionTipoEvaluacion
	 * @param idFuncionTipoEvaluacion .- id del tipo de FuncionTipoEvaluacion
	 * @return Entidade FuncionTipoEvaluacion existentes en la BD
	 * @throws FuncionTipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FuncionTipoEvaluacion
	 * @throws FuncionTipoEvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FuncionTipoEvaluacion> listarActivoXTipoEvaluacion(int idFuncionTipoEvaluacion) throws FuncionTipoEvaluacionNoEncontradoException , FuncionTipoEvaluacionException {
		List<FuncionTipoEvaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fntpev from FuncionTipoEvaluacion fntpev where fntpev.fnctpevEstado =:estado ");
			sbsql.append(" and fntpev.tpevTipoEvaluacion.tpevId =:idFuncionTipoEvaluacion ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", FuncionTipoEvaluacionConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idFuncionTipoEvaluacion", idFuncionTipoEvaluacion);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new FuncionTipoEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.listar.activo.x.tipo.evaluacion.no.result.exception")));
		} catch (Exception e) {
			throw new FuncionTipoEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FuncionTipoEvaluacion.listar.activo.x.tipo.evaluacion.exception")));
		}
		return retorno;
	} 
	 
	
	
}