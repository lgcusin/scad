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

 ARCHIVO:     TipoProcesoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoProceso. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 14-05-2018           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoProcesoException;
import ec.edu.uce.academico.ejb.excepciones.TipoProcesoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoProcesoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoProceso;

/**
 * Clase (Bean)TipoProcesoServicioImpl.
 * Bean declarado como Stateless.
 * @author lmqusihpei
 * @version 1.0
 */

@Stateless
public class TipoProcesoServicioImpl implements TipoProcesoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoProceso por su id
	 * @param id - del TipoProceso a buscar
	 * @return TipoProceso con el id solicitado
	 * @throws TipoProcesoNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoProceso con el id solicitado
	 * @throws TipoProcesoException - Excepcion general
	 */
	public TipoProceso buscarPorId(Integer id) throws TipoProcesoNoEncontradoException, TipoProcesoException {
		TipoProceso retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoProceso.class, id);
			} catch (NoResultException e) {
				throw new TipoProcesoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoProceso.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TipoProcesoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoProceso.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TipoProcesoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoProceso.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Busca una entidad Tipo Proceso por su descripcion
	 * @param descripcion - del Tipo Proceso a buscar
	 * @return Tipo Proceso por su descripcion
	 * @throws TipoProcesoNoEncontradoException - Excepcion lanzada cuando no se encuentra un Tipo de proceso 
	 * @throws TipoProcesoException - Excepcion general
	 */
	public TipoProceso buscarXDescripcion(String descripcion) throws TipoProcesoNoEncontradoException, TipoProcesoException{
		TipoProceso retorno = null;
		if (descripcion != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select tipr from TipoProceso tipr ");
				sbsql.append(" Where tipr.tiprDescripcion =:descripcion ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("descripcion", descripcion);
				retorno = (TipoProceso)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new TipoProcesoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoProceso.buscar.por.descripcion.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new TipoProcesoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoProceso.buscar.por.descripcion.non.unique.result.exception")));
			} catch (Exception e) {
				throw new TipoProcesoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoProceso.buscar.por.descripcion.exception")));
			}
		}
		return retorno;
	}
	
	

}
