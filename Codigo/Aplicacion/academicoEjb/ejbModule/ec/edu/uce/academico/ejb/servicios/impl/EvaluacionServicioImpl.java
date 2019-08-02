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

 ARCHIVO:     EvaluacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla EvaluacionServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EvaluacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.EvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionEvaluacion;

/**
 * Clase (Bean)EvaluacionServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class EvaluacionServicioImpl implements EvaluacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Evaluacion por su id
	 * @param id - deL Evaluacion a buscar
	 * @return Evaluacion con el id solicitado
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion con el id solicitado
	 * @throws EvaluacionException - Excepcion general
	 */
	@Override
	public Evaluacion buscarPorId(Integer id) throws EvaluacionNoEncontradoException, EvaluacionException {
		Evaluacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Evaluacion.class, id);
			} catch (NoResultException e) {
				throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Evaluacion existentes en la BD
	 * @return lista de todas las entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Evaluacion> listarTodos() throws EvaluacionNoEncontradoException , EvaluacionException {
		List<Evaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Evaluacion existentes en la BD
	 * @return lista de todas las entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Evaluacion> listarActivo() throws EvaluacionNoEncontradoException , EvaluacionException {
		List<Evaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl where evl.evaEstado =:estado order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", EvaluacionConstantes.ESTADO_ACTIVO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.exception")));
		}
		return retorno;
	}


	/**
	 * Entidad Evaluacion existentes en la BD con estado activo y por tipo de evalucion
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Entidade Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@Override
	public Evaluacion buscarActivoXTipo(int idTipoEvaluacion) throws EvaluacionValidacionException, EvaluacionNoEncontradoException , EvaluacionException {
		Evaluacion retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl where evl.evaEstado =:estado ");
			sbsql.append(" and evl.evTipoEvaluacion.tpevId =:idTipoEvaluacion ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", EvaluacionConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idTipoEvaluacion", idTipoEvaluacion);
			retorno = (Evaluacion) q.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new EvaluacionValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.no.unique.result.exception")));
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.exception")));
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Evaluacion existentes en la BD por periodo
	 * @param idPeriodo .- Id del periodo a buscar
	 * @return lista de todas las entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Evaluacion> listarXPeriodo(int idPeriodo) throws EvaluacionNoEncontradoException , EvaluacionException {
		List<Evaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl where evl.evPeriodoAcademico.pracId =:idPeriodo");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("idPeriodo", idPeriodo);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.x.periodo.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.x.periodo.exception")));
		}
		return retorno;
	}

	/**
	 * Lista de entidades Evaluacion existentes en la BD por tipo de evalucion
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Lista de entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Evaluacion> listarTodosXTipo(int idTipoEvaluacion) throws EvaluacionNoEncontradoException , EvaluacionException {
		List<Evaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl where ");
			sbsql.append(" evl.evTipoEvaluacion.tpevId =:idTipoEvaluacion ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("idTipoEvaluacion", idTipoEvaluacion);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.x.tipo.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.x.tipo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista de entidades Evaluacion existentes en la BD por tipo de evalucion activas
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Lista de entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Evaluacion> listarTodosXTipoActivo(int idTipoEvaluacion) throws EvaluacionNoEncontradoException , EvaluacionException {
		List<Evaluacion> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl where ");
			sbsql.append(" evl.evTipoEvaluacion.tpevId =:idTipoEvaluacion ");
			sbsql.append(" and evl.evaEstado =:estado ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString()); 
			q.setParameter("idTipoEvaluacion", idTipoEvaluacion);
			q.setParameter("estado", EvaluacionConstantes.ESTADO_ACTIVO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.x.tipo.activo.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.listar.todos.x.tipo.activo.exception")));
		}
		return retorno;
	}
	
	/**
	 * Añade una Evaluacion en la BD
	 * @return Si se añadio o no la Evaluacion
	 * @throws EvaluacionValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws EvaluacionException - Excepción general
	 */
	@Override
	public Evaluacion anadir(Evaluacion entidad) throws EvaluacionValidacionException, EvaluacionException {
		Evaluacion retorno = null;
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.anadir.exception")));
			}
		} else {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.anadir.null.exception")));
		}
		return retorno;
	}

	/**
	 * Edita una Evaluacion en la BD
	 * @return Si se Edito o no la Evaluacion
	 * @throws EvaluacionValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws EvaluacionException - Excepción general
	 */
	@Override
	public Boolean editar(Evaluacion entidad) throws EvaluacionValidacionException, EvaluacionException {
		Boolean verificar = false;

		Evaluacion retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getEvaId());
				if (retorno != null) {

					retorno.setEvaDescripcion(entidad.getEvaDescripcion());
					retorno.setEvaEstado(entidad.getEvaEstado());
					retorno.setEvaFecha(entidad.getEvaFecha());
					retorno.setEvaUsuario(entidad.getEvaUsuario());
					retorno.setEvPeriodoAcademico(entidad.getEvPeriodoAcademico());
					retorno.setEvTipoEvaluacion(entidad.getEvTipoEvaluacion());
					retorno.setEvaCronogramaInicio(entidad.getEvaCronogramaInicio());
					retorno.setEvaCronogramaFin(entidad.getEvaCronogramaFin());
					
					verificar= true;
				}
			}
		}catch (Exception e) { 
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.editar.exception")));
		}

		return verificar;
	}
	
	/**
	 * Entidad Evaluacion existentes en la BD con estado activo y por tipo de evalucion
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Entidade Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	@Override
	public Evaluacion buscarEvaluacion(int periodo, int tipoEvl) throws EvaluacionNoEncontradoException , EvaluacionException {
		Evaluacion retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select evl from Evaluacion evl ");
			sbsql.append(" where evl.evTipoEvaluacion.tpevId = :tipoEvl ");
			sbsql.append(" and evl.evPeriodoAcademico.pracId = :periodo ");

			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoEvl", tipoEvl);
			q.setParameter("periodo", periodo);
			
			retorno = (Evaluacion) q.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.no.unique.result.exception")));
		} catch (NoResultException e) {
			throw new EvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.no.result.exception")));
		} catch (Exception e) {
			throw new EvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.exception")));
		}
		return retorno;
	}

	public PlanificacionEvaluacion buscarPlanificacionEvaluacion(int evaluacionId, int procesoId, int plevEstado) throws PlanificacionEvaluacionNoEncontradoException, PlanificacionEvaluacionValidacionException, PlanificacionEvaluacionException{
		PlanificacionEvaluacion retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT pe FROM PlanificacionEvaluacion pe ");
			sbsql.append(" WHERE pe.plevEvaluacion.evaId = :evaId ");
			sbsql.append(" AND pe.plevProcesoFlujo.prflId = :prflId ");
			sbsql.append(" AND pe.plevEstado = :plevEstado ");

			Query q = em.createQuery(sbsql.toString());
			q.setParameter("evaId", evaluacionId);
			q.setParameter("prflId", procesoId);
			q.setParameter("plevEstado", plevEstado);
			
			retorno = (PlanificacionEvaluacion) q.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new PlanificacionEvaluacionValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.no.unique.result.exception")));
		} catch (NoResultException e) {
			throw new PlanificacionEvaluacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.no.result.exception")));
		} catch (Exception e) {
			throw new PlanificacionEvaluacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Evaluacion.buscar.activo.x.tipo.exception")));
		}
		
		return retorno;
	}
}