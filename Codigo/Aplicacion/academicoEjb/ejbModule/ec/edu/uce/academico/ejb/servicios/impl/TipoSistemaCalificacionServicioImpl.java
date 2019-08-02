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

 ARCHIVO:     TipoSistemaCalificacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoSistemaCalificacion. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-05-2017           David Arellano                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoSistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSistemaCalificacion;

/**
 * Clase (Bean)TipoSistemaCalificacionServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class TipoSistemaCalificacionServicioImpl implements TipoSistemaCalificacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/**
	 * Busca la entidad por la descripcion
	 * @param descripcion - String de la descripcion que se requiere buscar
	 * @return Entidad TipoSistemaCalificacion por la descripcion buscada
	 * @throws TipoSistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoSistemaCalificacion  por descripcion 
	 * @throws TipoSistemaCalificacionException - Excepcion general
	 */
	public TipoSistemaCalificacion buscarXDescripcion(String descripcion) throws TipoSistemaCalificacionNoEncontradoException , TipoSistemaCalificacionException {
		TipoSistemaCalificacion retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tisscl from TipoSistemaCalificacion tisscl ");
			sbsql.append(" Where tisscl.tissclDescripcion =:descripcion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("descripcion", descripcion);
			retorno = (TipoSistemaCalificacion)q.getSingleResult();
		} catch (NoResultException e) {
			throw new TipoSistemaCalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Tipo.sistema.calificacion.buscar.por.descripcion.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new TipoSistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Tipo.sistema.calificacion.buscar.por.descripcion.non.unique.result.exception")));
		} catch (Exception e) {
			throw new TipoSistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Tipo.sistema.calificacion.buscar.por.descripcion.exception")));
		}
		return retorno;
	}
	
}
