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

 ARCHIVO:     TipoFormacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoFormacion. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 22-05-2017           David Arellano                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoFormacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoFormacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFormacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacion;

/**
 * Clase (Bean)TipoFormacionServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class TipoFormacionServicioImpl implements TipoFormacionServicio {

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoFormacion por su id
	 * @param id - del TipoFormacion a buscar
	 * @return TipoFormacion con el id solicitado
	 * @throws TipoFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoFormacion con el id solicitado
	 * @throws TipoFormacionException - Excepcion general
	 */
	//TODO: MENSAJES DAVID
	public TipoFormacion buscarPorId(Integer id) throws TipoFormacionNoEncontradoException, TipoFormacionException {
		TipoFormacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoFormacion.class, id);
			} catch (NoResultException e) {
				throw new TipoFormacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TipoFormacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TipoFormacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoFormacion existentes en la BD
	 * @return lista de todas las entidades TipoFormacion existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFormacion> listarTodos() throws TipoFormacionNoEncontradoException{
		List<TipoFormacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select tifr from TipoFormacion tifr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new TipoFormacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFormacion.buscar.todos.no.result.exception")));
		}
		return retorno;
	}
	
}
