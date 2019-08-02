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

 ARCHIVO:     FichaFichaMatriculaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla FichaFichaMatricula. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-03-2017          David Arellano                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.FichaMatriculaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;

/**
 * Clase (Bean)FichaFichaMatriculaServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class FichaMatriculaServicioImpl implements FichaMatriculaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad FichaMatricula por su id
	 * @param id - de la FichaMatricula a buscar
	 * @return FichaMatricula con el id solicitado
	 * @throws FichaMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaMatricula con el id solicitado
	 * @throws FichaMatriculaException - Excepcion general
	 */
	@Override
	public FichaMatricula buscarPorId(Integer id) throws FichaMatriculaNoEncontradoException, FichaMatriculaException {
		FichaMatricula retorno = null;
		if (id != null) {
			try {
				retorno = em.find(FichaMatricula.class, id);
			} catch (NoResultException e) {
				throw new FichaMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades FichaMatricula existentes en la BD
	 * @return lista de todas las entidades FichaMatricula existentes en la BD
	 * @throws FichaMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaMatricula 
	 * @throws FichaMatriculaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FichaMatricula> listarTodos() throws FichaMatriculaNoEncontradoException , FichaMatriculaException {
		List<FichaMatricula> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcmt from FichaMatricula fcmt ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new FichaMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca Lista de  FichaMatricula por  id Ficha estudiante
	 * @param idFces - id del Estudiante a a buscar
	 * @return Lista de  FichaMatricula del idEstudiante. 
	 * 
	 */
     @SuppressWarnings("unchecked")
	@Override
	public List<FichaMatricula> ListarFichaMatriculaXfcesId(Integer fcesId) throws  FichaMatriculaException, FichaMatriculaNoEncontradoException  {
		List<FichaMatricula> retorno=null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcmt from FichaMatricula fcmt ");
			sbsql.append(" where fcmt.fcmtFichaEstudiante.fcesId =:fcesId ");
	  		Query q = em.createQuery(sbsql.toString());
	  		 q.setParameter("fcesId", fcesId);
			retorno = q.getResultList();
		} catch (Exception e) {
			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.listar.FichaMatricula.por.fcesId.exception")));
		}
		if (retorno.size() == Integer.valueOf(0))
			
				{
					throw new FichaMatriculaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.listar.FichaMatricula.por.fcesId.no.result.exception")));
				}
			
		return retorno;
	}
     
 	/**
 	 * Busca Lista de  FichaMatricula por  id Ficha estudiante
 	 * @param idFces - id del Estudiante a a buscar
 	 * @return Lista de  FichaMatricula del idEstudiante. 
 	 * 
 	 */
      @SuppressWarnings("unchecked")
 	@Override
 	public FichaMatricula buscarFichaMatriculaXPeriodoActivoXFcesId(Integer fcesId) throws  FichaMatriculaException, FichaMatriculaNoEncontradoException  {
 		FichaMatricula retorno=null;
 		try {
 			StringBuffer sbsql = new StringBuffer();
 			sbsql.append(" Select fcmt from FichaMatricula fcmt ");
 			sbsql.append(" where fcmt.fcmtFichaEstudiante.fcesId =:fcesId ");
 			sbsql.append(" where fcmt.fcmtFichaEstudiante.fcesId =:fcesId ");
 	  		Query q = em.createQuery(sbsql.toString());
 	  		 q.setParameter("fcesId", fcesId);
 			retorno = (FichaMatricula) q.getSingleResult();
 		} catch (Exception e) {
 			throw new FichaMatriculaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaMatricula.listar.FichaMatricula.por.fcesId.exception")));
 		}
 			
 		return retorno;
 	}
     
}
