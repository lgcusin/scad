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

 ARCHIVO:     FichaDocenteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla FichaDocente. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 12/04/2018           Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.academico.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;

/**
 * Clase (Bean)FichaDocenteServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class FichaDocenteServicioImpl implements FichaDocenteServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad FichaDocente por su Prsid
	 * @param id - de la FichaDocente a buscar
	 * @return FichaDocente con el id solicitado
	 * @throws FichaDocenteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaDocente con el id solicitado
	 * @throws FichaDocenteException - Excepcion general
	 */
	@Override
	public FichaDocente buscarPorPrsId(Integer prsId) throws FichaDocenteNoEncontradoException, FichaDocenteException {
		FichaDocente retorno = new FichaDocente();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcdc from FichaDocente fcdc ");
			sbsql.append(" Where fcdc.fcdcPersona.prsId = :prsId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("prsId", prsId);
			retorno = (FichaDocente) q.getSingleResult();
		} catch (NoResultException e) {
			throw new FichaDocenteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaDocente.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaDocente.listar.todos.exception")));
		}
		return retorno;
	}

	@Override
	public FichaDocente buscarPorPrsIdentificacion(String prsIdentificacion) throws FichaDocenteNoEncontradoException, FichaDocenteException {
		FichaDocente retorno = new FichaDocente();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcdc from FichaDocente fcdc ");
			sbsql.append(" Where fcdc.fcdcPersona.prsIdentificacion = :prsIdentificacion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("prsIdentificacion", prsIdentificacion);
			retorno = (FichaDocente) q.getSingleResult();
		} catch (NoResultException e) {
			throw new FichaDocenteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaDocente.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaDocente.listar.todos.exception")));
		}
		return retorno;
	}
	
}
