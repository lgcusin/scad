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

 ARCHIVO:     TiempoDedicacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TiempoDedicacionServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 20-10-2017           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
 
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionException;
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TiempoDedicacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TiempoDedicacion;

/**
 * Clase (Bean)TiempoDedicacionServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class TiempoDedicacionServicioImpl implements TiempoDedicacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TiempoDedicacion por su id
	 * @param id - deL TiempoDedicacion a buscar
	 * @return TiempoDedicacion con el id solicitado
	 * @throws TiempoDedicacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TiempoDedicacion con el id solicitado
	 * @throws TiempoDedicacionException - Excepcion general
	 */
	@Override
	public TiempoDedicacion buscarPorId(Integer id) throws TiempoDedicacionNoEncontradoException, TiempoDedicacionException {
		TiempoDedicacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TiempoDedicacion.class, id);
			} catch (NoResultException e) {
				throw new TiempoDedicacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TiempoDedicacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TiempoDedicacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TiempoDedicacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TiempoDedicacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TiempoDedicacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TiempoDedicacion existentes en la BD
	 * @return lista de todas las entidades TiempoDedicacion existentes en la BD
	 * @throws TiempoDedicacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws TiempoDedicacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TiempoDedicacion> listarTodos() throws TiempoDedicacionNoEncontradoException , TiempoDedicacionException {
		List<TiempoDedicacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpdd from TiempoDedicacion tpdd ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new TiempoDedicacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TiempoDedicacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new TiempoDedicacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TiempoDedicacion.listar.todos.exception")));
		}
		return retorno;
	}
	
	 
}
