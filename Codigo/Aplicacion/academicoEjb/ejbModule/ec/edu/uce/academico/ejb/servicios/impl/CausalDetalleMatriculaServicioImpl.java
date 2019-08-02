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

 ARCHIVO:     CausalDetalleMatriculaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CausalDetalleMatricula. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 24-ENE-2019           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.CausalDetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.CausalDetalleMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CausalException;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CausalDetalleMatriculaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CausalDetalleMatricula;

/**
 * Clase (Bean) CausalServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class CausalDetalleMatriculaServicioImpl implements CausalDetalleMatriculaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad CausalDetalleMatricula por su id
	 * @param id - de la CausalDetalleMatricula a buscar
	 * @return CausalDetalleMatricula con el id solicitado
	 * @throws CausalNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CausalException - Excepcion general
	 */
	@Override
	public CausalDetalleMatricula buscarPorId(Integer id) throws CausalDetalleMatriculaNoEncontradoException, CausalDetalleMatriculaException {
		CausalDetalleMatricula retorno = null;
		if (id != null) {
			try {
				retorno = em.find(CausalDetalleMatricula.class, id);
			} catch (NoResultException e) {
				throw new CausalDetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDetalleMatricula.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CausalDetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDetalleMatricula.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CausalDetalleMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDetalleMatricula.buscar.por.id.exception")));
			}
		}
		return retorno;
	}


	/**
	 * Lista todas las Causale existentes en la BD por tipo
	 * @return lista de todas las causales existentes en la BD pot tipo
	 */
	@SuppressWarnings("unchecked")
	public List<CausalDetalleMatricula> listarxdtmtId(int dtmtId) {
		List<CausalDetalleMatricula> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select csdtmt from CausalDetalleMatricula csdtmt ");
		sbsql.append(" where csdtmt.csdtmtDetalleMatricula.dtmtId =:dtmtId ");
		sbsql.append(" Order by csdtmt.csdtmtCausal.cslId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("dtmtId", dtmtId);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//throw new CausalDetalleMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CausalDetalleMatricula.listar.por.dtmtId.no.result.exception")));
		
			retorno = new ArrayList<CausalDetalleMatricula>();
		}
		return retorno;

	}
	
	
	
	
	
}
