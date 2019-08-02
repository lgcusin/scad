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

 ARCHIVO:     MallaPeriodoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla MallaPeriodo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 18-JUL-2017           Vinicio Rosales                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.MallaPeriodoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.MallaPeriodoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;

/**
 * Clase (Bean)MallaParaleloServicioImpl.
 * Bean declarado como Stateless.
 * @author jvrosales
 * @version 1.0
 */

@Stateless
public class MallaPeriodoServicioImpl implements MallaPeriodoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad MallaPeriodo por su id
	 * @param id - de la MallaPeriodo a buscar
	 * @return MallaPeriodo con el id solicitado
	 * @throws MallaPeriodoNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaPeriodo con el id solicitado
	 * @throws MallaPeriodoException - Excepcion general
	 */
	@Override
	public MallaPeriodo buscarPorId(Integer id) throws MallaPeriodoNoEncontradoException, MallaPeriodoException {
		MallaPeriodo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(MallaPeriodo.class, id);
			} catch (NoResultException e) {
				throw new MallaPeriodoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new MallaPeriodoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new MallaPeriodoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades MallaPeriodo existentes en la BD
	 * @return lista de todas las entidades MallaPeriodo existentes en la BD
	 * @throws MallaPeriodoNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaPeriodo 
	 * @throws MallaPeriodoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MallaPeriodo> listarTodos() throws MallaPeriodoNoEncontradoException , MallaPeriodoException {
		List<MallaPeriodo> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select mlpr from MallaPeriodo mlpr ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new MallaPeriodoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new MallaPeriodoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Añade una AreaConocimiento en la BD
	 * @return lista de todas las entidades AreaConocimiento existentes en la BD
	 * @throws MallaCurricularException 
	 * @throws AreaConocimientoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws AreaConocimientoException - Excepción general
	 */
	@Override
	public MallaPeriodo anadir(MallaPeriodo entidad) throws MallaPeriodoValidacionException, MallaPeriodoException {
		MallaPeriodo retorno = null;
		try {
			if(entidad != null){
				em.persist(entidad);
				em.flush();
				retorno = entidad;
			}else{
				throw new MallaPeriodoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.validar.entidad.null")));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallaPeriodoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.anadir.exception")));
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws MallaPeriodoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MallaPeriodoNoEncontradoException - Excepción lanzada si no se encontró la entidad a editar
	 * @throws MallaPeriodoException - Excepción general
	*/
	@Override
	public MallaPeriodo editar(MallaPeriodo entidad) throws MallaPeriodoNoEncontradoException, MallaPeriodoException {
		MallaPeriodo retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getMlprId());
				if (retorno != null) {
					retorno.setMlprEstado(entidad.getMlprEstado());
					
				}
			}
		} catch (MallaPeriodoNoEncontradoException e) {
			throw new MallaPeriodoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.editar.no.result.exception")));
		} catch (MallaPeriodoException e) {
			throw new MallaPeriodoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MallaPeriodo.editar.exception")));
		}
		return retorno;
	}
	
	/**
	 * Retorna la entidad buscada con los parametros idMalla, idPeriodo
	 * @param idMalla id de la malla a buscar
	 * @param idPeriodo id del periodo academico a buscar
	 * @return la entidad con los parametros ingresados
	 * @throws MallaPeriodoNoEncontradoException Excepción lanzada si no se encontró la entidad
	 * @throws MallaPeriodoException Excepción general
	 */
	public MallaPeriodo buscarxPeriodoxMallaCurricular(int idMalla, int idPeriodo) throws MallaPeriodoNoEncontradoException , MallaPeriodoException {
		MallaPeriodo retorno = null;
		try{
			if (idMalla != 0 && idPeriodo != 0) {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select distinct mlpr from MallaPeriodo mlpr ");
				sbsql.append(" where mlpr.mlprMallaCurricular.mlcrId =:idMalla and ");
				sbsql.append(" mlpr.mlprPeriodoAcademico.pracId =:idPeriodo ");
				Query q = em.createQuery(sbsql.toString());
				q.setMaxResults(1);
				q.setParameter("idMalla", idMalla);
				q.setParameter("idPeriodo", idPeriodo);
				retorno = (MallaPeriodo) q.getSingleResult();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
			
			
		
		return retorno;
	}
	
	/**
	 * Retorna la entidad buscada con los parametros idMalla
	 * @param idMalla id de la malla a buscar
	 * @return la entidad con los parametros ingresados
	 * @throws MallaPeriodoNoEncontradoException Excepción lanzada si no se encontró la entidad
	 * @throws MallaPeriodoException Excepción general
	 */
	public MallaPeriodo buscarxMallaCurricular(int idMalla) throws MallaPeriodoNoEncontradoException , MallaPeriodoException {
		MallaPeriodo retorno = null;
		try{
			if (idMalla != 0) {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select mlpr from MallaPeriodo mlpr ");
				sbsql.append(" where mlpr.mlprMallaCurricular.mlcrId =:idMalla ");
				Query q = em.createQuery(sbsql.toString());
				q.setMaxResults(1);
				q.setParameter("idMalla", idMalla);
				retorno = (MallaPeriodo) q.getSingleResult();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
			
			
		
		return retorno;
	}
	
	/**
	 * Actualiza la entidad buscada con los parametros pracId
	 * @param pracId id de la malla a buscar
	 * @return la entidad con los parametros ingresados
	 * @throws MallaPeriodoNoEncontradoException Excepción lanzada si no se encontró la entidad
	 * @throws MallaPeriodoException Excepción general
	 */
	public void actualizaMallaPeriodo(int pracId) throws MallaPeriodoNoEncontradoException , MallaPeriodoException {
		try{
			if (pracId != 0) {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Update MallaPeriodo  ");
				sbsql.append(" set mlprEstado = ");sbsql.append(MallaPeriodoConstantes.ESTADO_MALLA_PERIODO_INACTIVO_VALUE);
				sbsql.append(" where mlprPeriodoAcademico.pracId = ");sbsql.append(pracId);
				Query q1 = em.createQuery(sbsql.toString());
				q1.executeUpdate();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
//	
}
