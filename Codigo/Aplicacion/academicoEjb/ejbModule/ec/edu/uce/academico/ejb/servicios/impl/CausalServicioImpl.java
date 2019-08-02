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

 ARCHIVO:     CausalServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CausalServicio. 
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

import ec.edu.uce.academico.ejb.excepciones.CausalException;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;

/**
 * Clase (Bean) CausalServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class CausalServicioImpl implements CausalServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Causal por su id
	 * @param id - de la Causal a buscar
	 * @return Causal con el id solicitado
	 * @throws CausalNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CausalException - Excepcion general
	 */
	@Override
	public Causal buscarPorId(Integer id) throws CausalNoEncontradoException, CausalException {
		Causal retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Causal.class, id);
			} catch (NoResultException e) {
				throw new CausalNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Causal.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CausalException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Causal.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CausalException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Causal.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las Causale existentes en la BD
	 * @return lista de todas las causales existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<Causal> listarTodos() throws CausalNoEncontradoException{
		List<Causal> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select csl from Causal csl ");
		sbsql.append(" Order by csl.cslDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new CausalNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Causal.buscar.todos.no.result.exception")));
		}
		return retorno;

	}
	
	/**
	 * Lista todas las Causale existentes en la BD por tipo
	 * @return lista de todas las causales existentes en la BD pot tipo
	 */
	@SuppressWarnings("unchecked")
	public List<Causal> listarxTipo(int idTipo) throws CausalNoEncontradoException{
		List<Causal> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select csl from Causal csl ");
		sbsql.append(" where csl.cslTipoCausal.ticsId =:idTipo ");
		sbsql.append(" Order by csl.cslDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("idTipo", idTipo);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new CausalNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Causal.listar.por.tipo.no.result.exception")));
		}
		return retorno;

	}
	
	
	
	
	
}
