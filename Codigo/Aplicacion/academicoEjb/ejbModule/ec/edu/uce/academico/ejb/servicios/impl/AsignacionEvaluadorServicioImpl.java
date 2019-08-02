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

 ARCHIVO:     AsignacionEvaluadorServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla AsignacionEvaluadorServicioImpl. 
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

import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.AsignacionEvaluadorServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.AsignacionEvaluador;

/**
 * Clase (Bean)AsignacionEvaluadorServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class AsignacionEvaluadorServicioImpl implements AsignacionEvaluadorServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad AsignacionEvaluador por su id
	 * @param id - deL AsignacionEvaluador a buscar
	 * @return AsignacionEvaluador con el id solicitado
	 * @throws AsignacionEvaluadorNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionEvaluador con el id solicitado
	 * @throws AsignacionEvaluadorException - Excepcion general
	 */
	@Override
	public AsignacionEvaluador buscarPorId(Integer id) throws AsignacionEvaluadorNoEncontradoException, AsignacionEvaluadorException {
		AsignacionEvaluador retorno = null;
		if (id != null) {
			try {
				retorno = em.find(AsignacionEvaluador.class, id);
			} catch (NoResultException e) {
				throw new AsignacionEvaluadorNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new AsignacionEvaluadorException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new AsignacionEvaluadorException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades AsignacionEvaluador existentes en la BD
	 * @return lista de todas las entidades AsignacionEvaluador existentes en la BD
	 * @throws AsignacionEvaluadorNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionEvaluador
	 * @throws AsignacionEvaluadorException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AsignacionEvaluador> listarTodos() throws AsignacionEvaluadorNoEncontradoException , AsignacionEvaluadorException {
		List<AsignacionEvaluador> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from AsignacionEvaluador evl order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new AsignacionEvaluadorNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new AsignacionEvaluadorException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.listar.todos.exception")));
		}
		return retorno;
	}
 
	/**
	 * Añade una AsignacionEvaluador en la BD
	 * @return Si se añadio o no la AsignacionEvaluador
	 * @throws AsignacionEvaluadorValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws AsignacionEvaluadorException - Excepción general
	 */
	@Override
	public AsignacionEvaluador anadir(AsignacionEvaluador entidad) throws AsignacionEvaluadorValidacionException, AsignacionEvaluadorException {
		AsignacionEvaluador retorno = null;
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new AsignacionEvaluadorException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.anadir.exception")));
			}
		} else {
			throw new AsignacionEvaluadorException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionEvaluador.anadir.null.exception")));
		}
		return retorno;
	}

	 


}