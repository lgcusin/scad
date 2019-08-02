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

 ARCHIVO:     SistemaCalificacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla SistemaCalificacion. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 25-MARZO-2017           Gabriel Mafla                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.SistemaCalificacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.SistemaCalificacion;

/**
 * Clase (Bean)CarreraServicioImpl.
 * Bean declarado como Stateless.
 * @author ghmafla
 * @version 1.0
 */

@Stateless
public class SistemaCalificacionServicioImpl implements SistemaCalificacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad SistemaCalificacion por su id
	 * @param id - de la SistemaCalificacion a buscar
	 * @return SistemaCalificacion con el id solicitado
	 * @throws SistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una SistemaCalificacion con el id solicitado
	 * @throws SistemaCalificacionException - Excepcion general
	 */
	public SistemaCalificacion buscarPorId(Integer id) throws SistemaCalificacionNoEncontradoException, SistemaCalificacionException {
		SistemaCalificacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(SistemaCalificacion.class, id);
			} catch (NoResultException e) {
				throw new SistemaCalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.no.result.exception",id)));
				//TODO GABRIEL T FALTAN LOS MENSAJES
			}catch (NonUniqueResultException e) {
				//TODO GABRIEL T FALTAN LOS MENSAJES
				throw new SistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				//TODO GABRIEL T FALTAN LOS MENSAJES
				throw new SistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades SistemaCalificacion existentes en la BD
	 * @return lista de todas las entidades SistemaCalificacion existentes en la BD
	 * @throws SistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una SistemaCalificacion 
	 * @throws SistemaCalificacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<SistemaCalificacion> listarTodos() throws SistemaCalificacionNoEncontradoException , SistemaCalificacionException {
		List<SistemaCalificacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select sscl from SistemaCalificacion sscl ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			//TODO GABRIEL T FALTAN LOS MENSAJES
			throw new SistemaCalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.no.result.exception")));
		} catch (Exception e) {
			//TODO GABRIEL T FALTAN LOS MENSAJES
			throw new SistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 *  SistemaCalificacion por pracId, tissclId y estado existentes en la BD
	 * @return Entidad SistemaCalificacion existentes en la BD
	 * @throws SistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una SistemaCalificacion por pracId, tissclId y estado  
	 * @throws SistemaCalificacionException - Excepcion general
	 */
	//TODO: CAMBIAR EL COMENARIO YA QUE SOLO RETORNA UNA ENTIDAD Y NO UNA LISTA
	public SistemaCalificacion listarSistemaCalificacionXPracXtissclXEstado(int pracId, int tissclId, int estado ) throws SistemaCalificacionNoEncontradoException , SistemaCalificacionException {
		SistemaCalificacion retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select sscl from SistemaCalificacion sscl , PeriodoAcademico prac , TipoSistemaCalificacion tisscl");
			sbsql.append(" Where sscl.periodoAcademico.pracId = prac.pracId ");
			sbsql.append(" and sscl.tipoSistemaCalificacion.tissclId = tisscl.tissclId ");
			sbsql.append(" and  sscl.periodoAcademico.pracId =:pracId ");
			sbsql.append(" and  sscl.tipoSistemaCalificacion.tissclId =:tissclId ");
			sbsql.append(" and  sscl.ssclEstado  =:estado ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("pracId", pracId);
			q.setParameter("tissclId", tissclId);
			q.setParameter("estado", estado);
			retorno = (SistemaCalificacion)q.getSingleResult();
		} catch (NoResultException e) {
			throw new SistemaCalificacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Sistema.calificacion.listar.por.periodo.academico.tipo.sistema.academico.estado.sistema.calificacion.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new SistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Sistema.calificacion.listar.por.periodo.academico.tipo.sistema.academico.estado.sistema.calificacion.non.unique.result.exception")));
		} catch (Exception e) {
			throw new SistemaCalificacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Sistema.calificacion.listar.por.periodo.academico.tipo.sistema.academico.estado.sistema.calificacion.exception")));
		}
		return retorno;
	}
	
	
}
