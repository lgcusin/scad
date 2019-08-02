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

 ARCHIVO:     TipoGratuidadServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoGratuidadServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadException;
import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoGratuidadServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;

/**
 * Clase (Bean)TipoGratuidadServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class TipoGratuidadServicioImpl implements TipoGratuidadServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoGratuidad por su id
	 * @param id - deL TipoGratuidad a buscar
	 * @return TipoGratuidad con el id solicitado
	 * @throws TipoGratuidadNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoGratuidad con el id solicitado
	 * @throws TipoGratuidadException - Excepcion general
	 */
	@Override
	public TipoGratuidad buscarPorId(Integer id) throws TipoGratuidadNoEncontradoException, TipoGratuidadException {
		TipoGratuidad retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoGratuidad.class, id);
			} catch (NoResultException e) {
				throw new TipoGratuidadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoGratuidad.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TipoGratuidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoGratuidad.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TipoGratuidadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoGratuidad.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

}