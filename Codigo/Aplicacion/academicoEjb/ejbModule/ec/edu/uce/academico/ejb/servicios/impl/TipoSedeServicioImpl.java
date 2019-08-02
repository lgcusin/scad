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

 ARCHIVO:     TipoSedeServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoSede. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-03-2019           Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoSedeException;
import ec.edu.uce.academico.ejb.excepciones.TipoSedeNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoSedeServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSede;

/**
 * Clase (Bean)TipoSedeServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0b
 */

@Stateless
public class TipoSedeServicioImpl implements TipoSedeServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoSede por su id
	 * @param id - del TipoSede a buscar
	 * @return TipoSede con el id solicitado
	 * @throws TipoSedeNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoSede con el id solicitado
	 * @throws TipoSedeException - Excepcion general
	 */
	@Override
	public TipoSede buscarPorId(Integer id) throws TipoSedeNoEncontradoException, TipoSedeException {
		TipoSede retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoSede.class, id);
			} catch (NoResultException e) {
				throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSedeb.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new TipoSedeException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new TipoSedeException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoSede existentes en la BD
	 * @return lista de todas las entidades TipoSede existentes en la BD
	 * @throws TipoSedeNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoSede
	 * @throws TipoSedeException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoSede> listarTodos() throws TipoSedeNoEncontradoException, TipoSedeException {
		List<TipoSede> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tise from TipoSede tise ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new TipoSedeException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.listar.todos.non.unique.result.exception")));
		} catch (Exception e) {
			throw new TipoSedeException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.listar.todos.exception")));
		}
		
		return retorno;
	}
	

}
