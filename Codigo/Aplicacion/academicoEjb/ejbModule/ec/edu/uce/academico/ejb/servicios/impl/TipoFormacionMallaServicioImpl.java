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

 ARCHIVO:     TipoFormacionMallaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoFormacionMalla. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 22-Abril-2017           Gabriel Mafla                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoFormacionMallaException;
import ec.edu.uce.academico.ejb.excepciones.TipoFormacionMallaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionMallaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.TipoFormacionMallaConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacionMalla;

/**
 * Clase (Bean)TipoFormacionMallaServicioImpl.
 * Bean declarado como Stateless.
 * @author ghmafla
 * @version 1.0
 */

@Stateless
public class TipoFormacionMallaServicioImpl implements TipoFormacionMallaServicio {

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoFormacionMalla por su id
	 * @param id - del TipoFormacionMalla a buscar
	 * @return TipoFormacionMalla con el id solicitado
	 * @throws TipoFormacionMallaNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoFormacionMalla con el id solicitado
	 * @throws TipoFormacionMallaException - Excepcion general
	 */
	public TipoFormacionMalla buscarPorId(Integer id) throws TipoFormacionMallaNoEncontradoException, TipoFormacionMallaException {
		TipoFormacionMalla retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoFormacionMalla.class, id);
			} catch (NoResultException e) {
				throw new TipoFormacionMallaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacionMalla.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TipoFormacionMallaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacionMalla.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TipoFormacionMallaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacionMalla.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoFormacionMalla existentes en la BD
	 * @return lista de todas las entidades TipoFormacionMalla existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFormacionMalla> listarTodos() throws TipoFormacionMallaNoEncontradoException{
		List<TipoFormacionMalla> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select tifrml from TipoFormacionMalla tifrml ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new TipoFormacionMallaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacionMalla.buscar.todos.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Activas de TipoFormacionMalla existentes en la BD
	 * @return lista de todas las entidades Activas de TipoFormacionMalla existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFormacionMalla> listarTodosActivas() throws TipoFormacionMallaNoEncontradoException{
		List<TipoFormacionMalla> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select tifrml from TipoFormacionMalla tifrml ");
		sbsql.append(" Where tifrml.tifrmlEstado =:tifrmlEstado ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tifrmlEstado", TipoFormacionMallaConstantes.ESTADO_TIPO_FORMACION_MALLA_ACTIVO_VALUE);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new TipoFormacionMallaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacionMalla.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;
	}

}
