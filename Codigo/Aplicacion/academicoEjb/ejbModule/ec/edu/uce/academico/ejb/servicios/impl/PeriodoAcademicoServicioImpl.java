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

 ARCHIVO:     PeriodoAcademicoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla PeriodoAcademico. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 06-03-2017           David Arlellano                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PeriodoAcademicoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.EvaluacionConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PeriodoAcademicoConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.PlanificacionCronogramaConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Cronograma;
import ec.edu.uce.academico.jpa.entidades.publico.CronogramaProcesoFlujo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;

/**
 * Clase (Bean)PeriodoAcademicoServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class PeriodoAcademicoServicioImpl implements PeriodoAcademicoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad PeriodoAcademico por su id
	 * @param id - del PeriodoAcademico a buscar
	 * @return PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoNoEncontradoException - Excepcion lanzada cuando no se encuentra un PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoException - Excepcion general
	 */
	public PeriodoAcademico buscarPorId(Integer id) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException {
		PeriodoAcademico retorno = null;
		if (id != null) {
			try {
				retorno = em.find(PeriodoAcademico.class, id);
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodos(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPregrado ");
		sbsql.append(" order by prac.pracDescripcion desc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * MQ: 14ene2019
	 * Lista todas las entidades PeriodoAcademico existentes en la BD en orden descendente por Id
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD en orden descendente por Id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosPreGradoOrdenadosXId(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPregrado ");
		sbsql.append(" order by prac.pracId desc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosActivo(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPregrado ");
		sbsql.append(" and prac.pracEstado =:estado ");
		sbsql.append(" order by prac.pracId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
		q.setParameter("estado", PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}

	
	@SuppressWarnings("unchecked")
	public List<PeriodoAcademico> buscarPeriodos(int tipoPeriodo, List<String> estadoPeriodo) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo = :tipoPeriodo ");
		sbsql.append(" and prac.pracEstado in ");sbsql.append(estadoPeriodo.toString().replace("[", "(").replace("]",")"));
		sbsql.append(" order by prac.pracId ");
		
		try{
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoPeriodo", tipoPeriodo);
			retorno = q.getResultList();	
		} catch (NoResultException e) {
			throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
		}
		
		return retorno;

	}
	
	

	public PeriodoAcademico buscarPeriodo(int pracTipo, int pracEstado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoValidacionException, PeriodoAcademicoException{
		PeriodoAcademico retorno = null;

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" Where prac.pracEstado = :estado ");
		sbsql.append(" and prac.pracTipo = :tipo ");
		sbsql.append(" order by prac.pracId ");
		
		try {
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipo", pracTipo);
			q.setParameter("estado", pracEstado);
			retorno = (PeriodoAcademico) q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
		}
		return retorno;
	}
	
	public PeriodoAcademico buscarPeriodoXTipoXEstado(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoValidacionException, PeriodoAcademicoException{
		PeriodoAcademico retorno = null;
		if (tipoPrac != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estadoPrac ");
				sbsql.append(" and prac.pracTipo =:tipoPrac ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estadoPrac", estadoPrac);
				q.setParameter("tipoPrac", tipoPrac);
				retorno = (PeriodoAcademico) q.getSingleResult();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",tipoPrac)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",tipoPrac)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;
	}
	
	
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosActivoXtipoPeriodo(int tipoPeriodo){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPeriodo ");
		sbsql.append(" and prac.pracEstado =:estado ");
		sbsql.append(" order by prac.pracId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPeriodo", tipoPeriodo);
		q.setParameter("estado", PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	@Override
	public PeriodoAcademico buscarXestado(Integer estado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		PeriodoAcademico retorno = null;
		if (estado != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPregrado ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estado);
				q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				retorno = (PeriodoAcademico)q.getSingleResult();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",estado)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",estado)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	/**
	 * @author Daniel
	 * Buscar el PeriodoAcademico que cumpla con el estado buscado
	 * @return entidad PeriodoAcademico 
	 */
	@Override
	public PeriodoAcademico buscarXestadoSinExcepcion(Integer estado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		PeriodoAcademico retorno = null;
		if (estado != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPregrado ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estado);
				q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				retorno = (PeriodoAcademico)q.getSingleResult();
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	@Override
	public PeriodoAcademico buscarXestadoXtipoPeriodo(Integer estado, int tipoPeriodo) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		PeriodoAcademico retorno = null;
		if (estado != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPeriodo ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estado);
				q.setParameter("tipoPeriodo", tipoPeriodo);
				retorno = (PeriodoAcademico)q.getSingleResult();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",estado)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",estado)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarXestadoXtipoPeriodoActivoEnCierre(int tipoPeriodo) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (tipoPeriodo != GeneralesConstantes.APP_ID_BASE) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where (prac.pracEstado =:estadoActivo ");
				sbsql.append(" or prac.pracEstado =:estadoEnCierre ) ");
				sbsql.append(" and prac.pracTipo =:tipoPeriodo ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estadoActivo", PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
				q.setParameter("estadoEnCierre", PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
				q.setParameter("tipoPeriodo", tipoPeriodo);
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception")));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> buscarXestadolist(Integer estado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (estado != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPregrado ");
				sbsql.append(" order by prac.pracDescripcion ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estado);
				q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",estado)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",estado)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	
	
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	
	@SuppressWarnings("unchecked")
	public List<PeriodoAcademico> buscarXestadoPracXtipoPrac(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (tipoPrac != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPrac ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estadoPrac);
				q.setParameter("tipoPrac", tipoPrac);
				
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",tipoPrac)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",tipoPrac)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	@SuppressWarnings("unchecked")
	public List<PeriodoAcademico> buscarXestadoPracXtipoPracTodos(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (tipoPrac != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				if(tipoPrac != GeneralesConstantes.APP_ID_BASE){
					sbsql.append(" and prac.pracTipo =:tipoPrac ");
				}else{
					sbsql.append(" and (prac.pracTipo =:tipoPracPre ");
					sbsql.append(" or prac.pracTipo =:tipoPracSuf )");
				}
				
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estadoPrac);
				if(tipoPrac != GeneralesConstantes.APP_ID_BASE){
					q.setParameter("tipoPrac", tipoPrac);
				}else{
					q.setParameter("tipoPracPre", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
					q.setParameter("tipoPracSuf", PeriodoAcademicoConstantes.PRAC_IDIOMAS_VALUE);
				}
				
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",tipoPrac)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",tipoPrac)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Añade un periodo academico en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 * @throws PeriodoAcademicoException - Excepción general
	 * @throws PeriodoAcademicoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * 
	 */
	public boolean anadir(PeriodoAcademico entidad, Integer tipoUsuario, List<PeriodoAcademicoDto> entidades
			, List<PeriodoAcademicoDto> entidadesNivelacion) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException{
		Boolean retorno = false;
		if(entidad != null){
			try{
				if(tipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()){
					if(verificarDesccripcion(entidad, GeneralesConstantes.APP_NUEVO)){
						
						//Creamos el cronograma
						Cronograma crnAux = new Cronograma();
						Cronograma crnAuxNivelacion = new Cronograma();
						crnAux.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						crnAuxNivelacion.setCrnEstado(CronogramaConstantes.ESTADO_ACTIVO_VALUE);
						if(tipoUsuario == RolConstantes.ROL_ADMINDGA_VALUE.intValue()){
							// Buscamos el período académico que está en curso de pregrado
							PeriodoAcademico pracAux = new PeriodoAcademico();
							StringBuffer sbsql = new StringBuffer();
							sbsql.append(" select prac from PeriodoAcademico prac ");
							sbsql.append(" where pracEstado = ");sbsql.append(PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
							sbsql.append(" and pracTipo = ");sbsql.append(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
							Query q = em.createQuery(sbsql.toString());
							pracAux = (PeriodoAcademico) q.getSingleResult();
							//Modificamos su estado a EN CIERRE
							sbsql = new StringBuffer();
							sbsql.append(" update PeriodoAcademico set pracEstado = ");
							sbsql.append(PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
							sbsql.append(" where pracId = :pracId");
							Query q1 = em.createQuery(sbsql.toString());
							q1.setParameter("pracId", pracAux.getPracId());
							q1.executeUpdate();
							//Guardamos el nuevo período académico
							em.persist(entidad);
							em.flush();
							crnAux.setCrnTipo(CronogramaConstantes.TIPO_ACADEMICO_VALUE);
							crnAuxNivelacion.setCrnTipo(CronogramaConstantes.TIPO_NIVELACION_VALUE);
							crnAux.setCrnDescripcion("ACADEMICO "+entidad.getPracDescripcion());
							crnAuxNivelacion.setCrnDescripcion("NIVELACIÓN "+entidad.getPracDescripcion());
							crnAux.setCrnPeriodoAcademico(entidad);
							crnAuxNivelacion.setCrnPeriodoAcademico(entidad);
							em.persist(crnAux);
							em.flush();
							em.persist(crnAuxNivelacion);
							em.flush();
							for (int i = 0;i<entidades.size();i++){
								//Creamos el CronogramaProcesoFlujo
								CronogramaProcesoFlujo crprflAux = new CronogramaProcesoFlujo();
								CronogramaProcesoFlujo crprflAuxNivelacion = new CronogramaProcesoFlujo();
								crprflAux.setCrprCronograma(crnAux);
								crprflAuxNivelacion.setCrprCronograma(crnAuxNivelacion);
								ProcesoFlujo prflAux = em.find(ProcesoFlujo.class, entidades.get(i).getPrflId());
								crprflAux.setCrprProcesoFlujo(prflAux);
								crprflAuxNivelacion.setCrprProcesoFlujo(prflAux);
								em.persist(crprflAux);
								em.flush();
								em.persist(crprflAuxNivelacion);
								em.flush();
//								em.flush();
								PlanificacionCronograma plcrAux = new PlanificacionCronograma();
								plcrAux.setPlcrCronogramaProcesoFlujo(crprflAux);
								plcrAux.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
								Calendar cal = Calendar.getInstance();
								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							        try {
							            Date date = formatter.parse(entidades.get(i).getPlcrFechaInicio());
							            cal.setTime(date);
							        }catch (Exception e) {
									}
								cal.set(Calendar.MILLISECOND, 0);
								plcrAux.setPlcrFechaInicio(new java.sql.Timestamp(cal.getTimeInMillis()));
								cal = Calendar.getInstance();
								try {
						            Date date = formatter.parse(entidades.get(i).getPlcrFechaFin());
						            cal.setTime(date);
						        }catch (Exception e) {
								}
								cal.set(Calendar.MILLISECOND, 0);
								plcrAux.setPlcrFechaFin(new java.sql.Timestamp(cal.getTimeInMillis()));
								
								PlanificacionCronograma plcrAuxNivelacion = new PlanificacionCronograma();
								plcrAuxNivelacion.setPlcrCronogramaProcesoFlujo(crprflAuxNivelacion);
								plcrAuxNivelacion.setPlcrEstado(PlanificacionCronogramaConstantes.ESTADO_ACTIVO_VALUE);
							        try {
							            Date date = formatter.parse(entidadesNivelacion.get(i).getPlcrFechaInicio());
							            cal.setTime(date);
							        }catch (Exception e) {
									}
								cal.set(Calendar.MILLISECOND, 0);
								plcrAuxNivelacion.setPlcrFechaInicio(new java.sql.Timestamp(cal.getTimeInMillis()));
								cal = Calendar.getInstance();
								try {
						            Date date = formatter.parse(entidadesNivelacion.get(i).getPlcrFechaFin());
						            cal.setTime(date);
						        }catch (Exception e) {
								}
								cal.set(Calendar.MILLISECOND, 0);
								plcrAuxNivelacion.setPlcrFechaFin(new java.sql.Timestamp(cal.getTimeInMillis()));
								em.persist(plcrAuxNivelacion);
								em.flush();
								em.persist(plcrAux);
								em.flush();
							}
							
						}else{
							crnAux.setCrnTipo(CronogramaConstantes.TIPO_POSGRADO_VALUE);
							crnAux.setCrnDescripcion("POSGRADO "+entidad.getPracDescripcion());
							//Guardamos el nuevo período académico
							em.persist(entidad);
							em.flush();
						}
						
						
						retorno = true;
					}else{
						throw new PeriodoAcademicoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.añadir.duplicado.error")));
					}
				}else  if(tipoUsuario == RolConstantes.ROL_ADMINDPP_VALUE.intValue()){
					
					em.persist(entidad);
//					em.flush();
					retorno = true;
				}
				
			}catch(PeriodoAcademicoValidacionException e){
				throw new PeriodoAcademicoValidacionException(e.getMessage());
			}catch(Exception e){
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.añadir.exception")));
			}
		}else{
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.añadir.null.exception")));
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PeriodoAcademicoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PeriodoAcademicoException - Excepción general
	 */
	public Boolean editar(PeriodoAcademico entidad) throws PeriodoAcademicoValidacionException, PeriodoAcademicoException{
		Boolean retorno = false;
		if(entidad != null){
						
			try{
				if(verificarDesccripcion(entidad, GeneralesConstantes.APP_EDITAR)){
					PeriodoAcademico pracAux = em.find(PeriodoAcademico.class, entidad.getPracId());
					pracAux.setPracDescripcion(entidad.getPracDescripcion());
					pracAux.setPracEstado(entidad.getPracEstado());
					pracAux.setPracFechaIncio(entidad.getPracFechaIncio());
					pracAux.setPracFechaFin(entidad.getPracFechaFin());
					pracAux.setPracTipo(PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
					retorno = true;
				}else{
					throw new PeriodoAcademicoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.editar.duplicado.error")));
				}
					
				
			}catch(Exception e){
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.editar.exception")));
			}
		}else{
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.editar.null.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Realiza verificaciones para determinar si existe una entidad con la misma descripcion
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @throws PeriodoAcademicoValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PeriodoAcademicoException  - Excepcion lanzada no se controla el error
	 */
	private Boolean verificarDesccripcion(PeriodoAcademico entidad, int tipo) throws PeriodoAcademicoValidacionException{
		Boolean retorno = false;
		try{
			//verifico que no exista otra entidad con el mismo periodo academico
			StringBuffer sbSql = new StringBuffer();
			sbSql.append(" Select prac from PeriodoAcademico prac ");
			sbSql.append(" where prac.pracDescripcion =:descripcion ");
			if(tipo == GeneralesConstantes.APP_EDITAR){
				sbSql.append(" and prac.pracId <>:identificador ");
			}
			sbSql.append(" order by prac.pracDescripcion ");
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("descripcion", entidad.getPracDescripcion());
			if(tipo == GeneralesConstantes.APP_EDITAR){
				q.setParameter("identificador", entidad.getPracId());
			}
			PeriodoAcademico periodoAcademico = (PeriodoAcademico)q.getSingleResult();
			if(periodoAcademico == null){
				retorno = false;
			}
			
		} catch(NoResultException e){
			retorno = true;
		} catch(Exception e){
			throw new PeriodoAcademicoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.verificar.descripcion.validacion.exception")));
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosPosgrado(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPosgrado ");
		sbsql.append(" order by prac.pracDescripcion asc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosPosgradoDesc(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPosgrado ");
		sbsql.append(" order by prac.pracDescripcion desc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosPosgradoActivo(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac ");
		sbsql.append(" where prac.pracTipo =:tipoPosgrado ");
		sbsql.append(" and prac.pracEstado =:estado ");
		sbsql.append(" order by prac.pracId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
		q.setParameter("estado", PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD de acuerdo al usuario y postgrado
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD de acuerdo al usuario y postgrado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarXUsuarioXPosgradoActivo(Integer crrId){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prac from PeriodoAcademico prac, MallaPeriodo mlpr, MallaCurricular mlcr, Carrera crr, RolFlujoCarrera roflcr ");
		sbsql.append(" where prac.pracId =  mlpr.mlprPeriodoAcademico.pracId");
		sbsql.append(" and mlcr.mlcrId =  mlpr.mlprMallaCurricular.mlcrId");
		sbsql.append(" and crr.crrId =  mlcr.mlcrCarrera.crrId");
		sbsql.append(" and crr.crrId =  roflcr.roflcrCarrera.crrId");
		sbsql.append(" and crr.crrId =:crrId ");
		sbsql.append(" and prac.pracTipo =:tipoPosgrado ");
		sbsql.append(" and prac.pracEstado =:estado ");
		sbsql.append(" order by prac.pracId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("crrId", crrId);
		q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
		q.setParameter("estado", PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	
	@Override
	public PeriodoAcademico buscarXestadoPosgrado(Integer crrId,Integer pracEstado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		PeriodoAcademico retorno = null;
		if (pracEstado != null && crrId!= null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac , MallaPeriodo mlpr , MallaCurricular mlcr ");
				sbsql.append(" Where ");
				sbsql.append(" prac.pracId = mlpr.mlprPeriodoAcademico.pracId ");
				sbsql.append(" AND mlpr.mlprMallaCurricular.mlcrId = mlcr.mlcrId ");
				sbsql.append(" AND mlcr.mlcrCarrera.crrId = :crrId ");
				sbsql.append(" AND prac.pracEstado =:pracEstado ");
				sbsql.append(" and prac.pracTipo =:tipoPosgrado ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("crrId", crrId);
				q.setParameter("pracEstado", pracEstado);
				q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
				retorno = (PeriodoAcademico)q.getSingleResult();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",pracEstado)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",pracEstado)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	
	/**MQ
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarXestadoPosgrado(Integer crrId,Integer pracEstado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (pracEstado != null && crrId!= null) {
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac , MallaPeriodo mlpr , MallaCurricular mlcr ");
				sbsql.append(" Where ");
				sbsql.append(" prac.pracId = mlpr.mlprPeriodoAcademico.pracId ");
				sbsql.append(" AND mlpr.mlprMallaCurricular.mlcrId = mlcr.mlcrId ");
				sbsql.append(" AND mlcr.mlcrCarrera.crrId = :crrId ");
				sbsql.append(" AND prac.pracEstado =:pracEstado ");
				sbsql.append(" and prac.pracTipo =:tipoPosgrado ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("crrId", crrId);
				q.setParameter("pracEstado", pracEstado);
				q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",pracEstado)));
			} catch (Exception e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}	
			
		}
		return retorno;

	}
	
	
	/**
	 * Buscar las entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> buscarXestadolistPosgrado(Integer estado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (estado != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPosgrado ");
				sbsql.append(" order by prac.pracDescripcion ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estado);
				q.setParameter("tipoPosgrado", PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",estado)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;

	}
	
	/**
	 * Añade un periodo academico en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 * @throws PeriodoAcademicoException - Excepción general
	 * @throws PeriodoAcademicoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * 
	 */
	public boolean anadirPosgrado(PeriodoAcademico entidad) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException{
		Boolean retorno = false;
		if(entidad != null){
			try{
					em.persist(entidad);
//					em.flush();
					retorno = true;
			}catch(Exception e){
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.añadir.exception")));
			}
		}else{
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.añadir.null.exception")));
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PeriodoAcademicoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PeriodoAcademicoException - Excepción general
	 */
	public Boolean editarPosgrado(PeriodoAcademico entidad) throws PeriodoAcademicoValidacionException, PeriodoAcademicoException{
		Boolean retorno = false;
		if(entidad != null){
						
			try{
					PeriodoAcademico pracAux = em.find(PeriodoAcademico.class, entidad.getPracId());
					pracAux.setPracDescripcion(entidad.getPracDescripcion().toUpperCase());
					pracAux.setPracEstado(entidad.getPracEstado());
					pracAux.setPracFechaIncio(entidad.getPracFechaIncio());
					pracAux.setPracFechaFin(entidad.getPracFechaFin());
					pracAux.setPracTipo(PeriodoAcademicoConstantes.PRAC_POSGRADO_VALUE);
					retorno = true;
				
			}catch(Exception e){
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.editar.exception")));
			}
		}else{
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.editar.null.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Buscar la entidad PeriodoAcademico que se encuentre en cierre
	 * @throws PeriodoAcademicoNoEncontradoException  - Excepcion lanzada cuando no hay datos
	 */
	@Override
	public PeriodoAcademico buscarPeriodoEnCierre() throws PeriodoAcademicoNoEncontradoException{
		PeriodoAcademico retorno = null;
		try{
			StringBuffer sbSql = new StringBuffer();
			sbSql.append(" Select prac from PeriodoAcademico prac ");
			sbSql.append(" where prac.pracEstado =:estado ");
			sbSql.append(" and prac.pracTipo =:tipo ");
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("estado", PeriodoAcademicoConstantes.ESTADO_EN_CIERRE_VALUE);
			q.setParameter("tipo", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			retorno = (PeriodoAcademico)q.getSingleResult();
			if(retorno == null){
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.verificar.descripcion.validacion.exception")));
			}
			
		} catch(NoResultException e){
			retorno = null;
		} 
		return retorno;
	}
	
	/**
	 * Buscar la entidad PeriodoAcademico que se encuentre en cierre
	 * @throws PeriodoAcademicoNoEncontradoException  - Excepcion lanzada cuando no hay datos
	 */
	@Override
	public boolean desactivarPeriodoEnCierre(PeriodoAcademico pracAux) throws PeriodoAcademicoNoEncontradoException{
		boolean retorno = false;
		try{
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" update PeriodoAcademico set pracEstado = ");
			sbsql.append(PeriodoAcademicoConstantes.ESTADO_INACTIVO_VALUE);
			sbsql.append(" where pracId = :pracId");
			Query q1 = em.createQuery(sbsql.toString());
			q1.setParameter("pracId", pracAux.getPracId());
			q1.executeUpdate();
			retorno = true;
		} catch(Exception e){
			retorno = false;
		} 
		return retorno;
	}
	
	
	/**
	 * Busca una entidad PeriodoAcademico por su descripcion
	 * @param descripcion - del PeriodoAcademico a buscar
	 * @return PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoNoEncontradoException - Excepcion lanzada cuando no se encuentra un PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoException - Excepcion general
	 */
	public PeriodoAcademico buscarPDescripcion(String descripcion) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException {
		PeriodoAcademico retorno = null;
		if (descripcion != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracDescripcion =:descripcion ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("descripcion", descripcion);
				retorno = (PeriodoAcademico)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.descripcion.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.descripcion.non.unique.result.exception")));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.descripcion.exception")));
			}
		}
		return retorno;
	}
	
	
	/**MQ
	 * Buscar las entidades PeriodoAcademico que tengan un estado determinado y un tipo
	 * @return PeriodoAcademico que tengan un estado determinado.
	 */
	
	public PeriodoAcademico buscarPeriodoXestadoXtipo(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
	PeriodoAcademico retorno = null;
		if (tipoPrac != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prac from PeriodoAcademico prac ");
				sbsql.append(" Where prac.pracEstado =:estado ");
				sbsql.append(" and prac.pracTipo =:tipoPrac ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("estado", estadoPrac);
				q.setParameter("tipoPrac", tipoPrac);
				
				retorno = (PeriodoAcademico) q.getSingleResult();
			} catch (NoResultException e) {
			//	throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",tipoPrac)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",tipoPrac)));
			} catch (Exception e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	@SuppressWarnings("unchecked")
	public List<PeriodoAcademico> buscarPracEstadoEvaluacionXestadoPracXtipoPrac(Integer tipoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		if (tipoPrac != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select distinct prac from PeriodoAcademico prac, Evaluacion eva ");
				sbsql.append(" Where prac.pracId = eva.evPeriodoAcademico.pracId ");
				sbsql.append(" and prac.pracTipo =:tipoPrac ");
				sbsql.append(" and eva.evaEstado =:estadoEva ");
				sbsql.append(" order by prac.pracId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("tipoPrac", tipoPrac);
				q.setParameter("estadoEva", EvaluacionConstantes.ESTADO_ACTIVO_VALUE);
				
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception",tipoPrac)));
			}catch (NonUniqueResultException e) {
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception",tipoPrac)));
			} catch (Exception e) {
				e.printStackTrace();
				throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
			}
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<PeriodoAcademico> buscarPeriodosEvaluacionDesempeño() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		
		try {

			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select distinct prac from PeriodoAcademico prac, Evaluacion eva ");
			sbsql.append(" Where prac.pracId = eva.evPeriodoAcademico.pracId ");
			sbsql.append(" order by prac.pracId ");
			Query q = em.createQuery(sbsql.toString());

			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD con carga horaria
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD con carga horaria
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosConCargaHoraria(){
		List<PeriodoAcademico> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct prac from PeriodoAcademico prac, CargaHoraria crhr ");
		sbsql.append(" where prac.pracId = crhr.crhrPeriodoAcademico.pracId");
		sbsql.append(" and prac.pracTipo =:tipoPregrado ");
		sbsql.append(" order by prac.pracDescripcion desc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD con retiros
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD con retiros
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosConRetiros() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select distinct prac from PeriodoAcademico prac, DetalleMatricula dtmt, ComprobantePago cmpa, FichaMatricula fcmt ");
			sbsql.append(" , FichaEstudiante fces, RecordEstudiante rces, MallaCurricularParalelo mlcrpr ");
			sbsql.append(" where prac.pracId = fcmt.fcmtPracId");
			sbsql.append(" and fcmt.fcmtId = cmpa.cmpaFichaMatricula.fcmtId");
			sbsql.append(" and cmpa.cmpaId = dtmt.dtmtComprobantePago.cmpaId");
			sbsql.append(" and mlcrpr.mlcrprId = dtmt.dtmtMallaCurricularParalelo.mlcrprId");
			sbsql.append(" and mlcrpr.mlcrprId = rces.rcesMallaCurricularParalelo.mlcrprId");
			sbsql.append(" and fces.fcesId = rces.rcesFichaEstudiante.fcesId");
			sbsql.append(" and fces.fcesId = fcmt.fcmtFichaEstudiante.fcesId");
			sbsql.append(" and (dtmt.dtmtEstadoRespuesta is not null");
			sbsql.append(" or rces.rcesEstado in (3,10,12))");
			sbsql.append(" and prac.pracTipo =:tipoPregrado ");
			sbsql.append(" order by prac.pracDescripcion desc ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
		}
		return retorno;

	}
	
	
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD con solicitudes y apelaciones de tercera matricula
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD con solicitudes y apelaciones de tercera matricula
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoAcademico> listarTodosConTercerasMatriculas() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException{
		List<PeriodoAcademico> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select distinct prac from PeriodoAcademico prac,  SolicitudTerceraMatricula sltrmt ");
			sbsql.append(" where prac.pracId = sltrmt.sltrmtPeriodoAcademico.pracId");
			sbsql.append(" and prac.pracTipo =:tipoPregrado ");
			sbsql.append(" order by prac.pracDescripcion desc ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoPregrado", PeriodoAcademicoConstantes.PRAC_PREGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
		}
		return retorno;

	}

	@SuppressWarnings("unchecked")
	public List<PeriodoAcademico> buscarPeriodosInformatica() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException {
		List<PeriodoAcademico> retorno = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" Select prac from PeriodoAcademico prac ");
		sql.append(" Where prac.pracTipo in ("+PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE+","+PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_VALUE+","+PeriodoAcademicoConstantes.PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE + ")");
		sql.append(" and prac.pracEstado = " + PeriodoAcademicoConstantes.ESTADO_ACTIVO_VALUE);
		sql.append(" order by prac.pracDescripcion ");
		
		try {
			Query q = em.createQuery(sql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PeriodoAcademicoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.non.unique.result.exception")));
		} catch (Exception e) {
			throw new PeriodoAcademicoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PeriodoAcademico.buscar.por.estado.exception")));
		}
		
		return retorno;
	}
	
}
