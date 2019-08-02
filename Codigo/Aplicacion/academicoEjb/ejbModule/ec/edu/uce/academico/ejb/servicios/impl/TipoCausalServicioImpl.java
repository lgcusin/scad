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

 ARCHIVO:     TipoCausalServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoCausalServicio. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 19-ENE-2018           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoCausalException;
import ec.edu.uce.academico.ejb.excepciones.TipoCausalNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoCausalServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoCausal;

/**
 * Clase (Bean)TipoCausalServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class TipoCausalServicioImpl implements TipoCausalServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Tipo Causal por su id
	 * @param id - del Tipo causal a buscar
	 * @return Tipo Causal con el id solicitado
	 * @throws TipoCausalNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws TipoCausalException - Excepcion general
	 */
	@Override
	public TipoCausal buscarPorId(Integer id) throws TipoCausalNoEncontradoException, TipoCausalException {
		TipoCausal retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoCausal.class, id);
			} catch (NoResultException e) {
				//TODO: falta todo
				throw new TipoCausalNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				//TODO: falta todo
				throw new TipoCausalException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				//TODO: falta todo
				throw new TipoCausalException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas los TipoCausal existentes en la BD
	 * @return lista de todas los TipoCausal existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<TipoCausal> listarTodos() throws TipoCausalNoEncontradoException{
		List<TipoCausal> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select tics from TipoCausal tics ");
		sbsql.append(" Order by tics.ticsDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//TODO:   falta todo
			throw new TipoCausalNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoMateria.buscar.todos.no.result.exception")));
		}
		return retorno;

	}
	
	
	
	
}
