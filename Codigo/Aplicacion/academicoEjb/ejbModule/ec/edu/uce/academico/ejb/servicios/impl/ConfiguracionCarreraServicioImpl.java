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

 ARCHIVO:     ConfiguracionCarreraServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ConfiguracionCarrera. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 19-05-2017           David Arellano                 Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.ConfiguracionCarreraException;
import ec.edu.uce.academico.ejb.excepciones.ConfiguracionCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;

/**
 * Clase (Bean)ConfiguracionCarreraServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class ConfiguracionCarreraServicioImpl implements ConfiguracionCarreraServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

//	/**
//	 * Busca una entidad Carrera por su id
//	 * @param id - de la Carrera a buscar
//	 * @return Carrera con el id solicitado
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
//	 * @throws CarreraException - Excepcion general
//	 */
//	@Override
//	public Carrera buscarPorId(Integer id) throws CarreraNoEncontradoException, CarreraException {
//		Carrera retorno = null;
//		if (id != null) {
//			try {
//				retorno = em.find(Carrera.class, id);
//			} catch (NoResultException e) {
//				throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.no.result.exception",id)));
//			}catch (NonUniqueResultException e) {
//				throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.non.unique.result.exception",id)));
//			} catch (Exception e) {
//				throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.exception")));
//			}
//		}
//		return retorno;
//	}
//
//	/**
//	 * Lista todas las entidades Carrera existentes en la BD
//	 * @return lista de todas las entidades Carrera existentes en la BD
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
//	 * @throws CarreraException - Excepcion general
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Carrera> listarTodos() throws CarreraNoEncontradoException , CarreraException {
//		List<Carrera> retorno = null;
//		try {
//			StringBuffer sbsql = new StringBuffer();
//			sbsql.append(" Select crr from Carrera crr ");
//			Query q = em.createQuery(sbsql.toString());
//			retorno = q.getResultList();
//		} catch (NoResultException e) {
//			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.no.result.exception")));
//		} catch (Exception e) {
//			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
//		}
//		return retorno;
//	}
//	
//	
//	/**
//	 * Lista todas carreras por facultad existentes en la BD
//	 * @return Lista todas carreras por facultad existentes en la BD
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
//	 * @throws CarreraException - Excepcion general
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Carrera> listarCarrerasXFacultad(int facultadId) throws CarreraNoEncontradoException , CarreraException {
//		List<Carrera> retorno = null;
//		try {
//			StringBuffer sbsql = new StringBuffer();
//			sbsql.append(" Select crr from Carrera crr ");
//			sbsql.append(" Where crr.crrDependencia.dpnId =:facultadId ");
//			Query q = em.createQuery(sbsql.toString());
//			q.setParameter("facultadId", facultadId);
//			retorno = q.getResultList();
//		} catch (NoResultException e) {
//			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
//		} catch (Exception e) {
//			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
//		}
//		return retorno;
//	}
//	
//	
//	/**
//	 * Lista todas carreras por facultad y carrera existentes en la BD
//	 * @return Lista todas carreras por facultad y carrera existentes en la BD
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad y carrera 
//	 * @throws CarreraException - Excepcion general
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Carrera> listarCarrerasXFacultad(int facultadId, int carreraId) throws CarreraNoEncontradoException , CarreraException {
//		List<Carrera> retorno = null;
//		try {
//			StringBuffer sbsql = new StringBuffer();
//			sbsql.append(" Select crr from Carrera crr ");
//			sbsql.append(" Where ");
//			if(facultadId == GeneralesConstantes.APP_ID_BASE){
//				sbsql.append(" crr.crrDependencia.dpnId >:facultadId ");
//			}else{
//				sbsql.append(" crr.crrDependencia.dpnId =:facultadId ");
//			}
//			
//			if(carreraId == GeneralesConstantes.APP_ID_BASE){
//				sbsql.append(" and crr.crrId >:carreraId ");
//			}else{
//				sbsql.append(" and crr.crrId =:carreraId ");
//			}
//			Query q = em.createQuery(sbsql.toString());
//			q.setParameter("facultadId", facultadId);
//			q.setParameter("carreraId", carreraId);
//			retorno = q.getResultList();
//		} catch (NoResultException e) {
//			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
//		} catch (Exception e) {
//			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
//		}
//		return retorno;
//	}
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXcrr(int crrId) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException{
		ConfiguracionCarrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
			sbsql.append(" Where cncr.cncrCarrera.crrId = :crrId ");
			Query q = em.createQuery(sbsql.toString()).setMaxResults(1);
			q.setParameter("crrId", crrId);
			retorno =(ConfiguracionCarrera) q.getSingleResult();
		} catch (NoResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (NonUniqueResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		} catch (Exception e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera sin no encontrado exception
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXcrrIdNuevo(int crrId) throws ConfiguracionCarreraException{
		ConfiguracionCarrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
			sbsql.append(" Where cncr.cncrCarrera.crrId = :crrId ");
			Query q = em.createQuery(sbsql.toString()).setMaxResults(1);
			q.setParameter("crrId", crrId);
			retorno =(ConfiguracionCarrera) q.getSingleResult();
		} catch (NoResultException e) {
			
		//	throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (NonUniqueResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Configuracion.carrera.buscar.por.carrera.no.unico.exception")));
		} catch (Exception e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Configuracion.carrera.buscar.por.carrera.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	@Override
	public ConfiguracionCarrera buscarXcrrXSexoHombre(int crrId) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException{
		ConfiguracionCarrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
			sbsql.append(" Where cncr.cncrCarrera.crrId = :crrId ");
			sbsql.append(" and ROWNUM <2 ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			retorno =(ConfiguracionCarrera) q.getSingleResult();
		} catch (NoResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (NonUniqueResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		} catch (Exception e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXid(int cncrId) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException{
		ConfiguracionCarrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
			sbsql.append(" Where cncr.cncrId = :cncrId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("cncrId", cncrId);
			retorno =(ConfiguracionCarrera) q.getSingleResult();
		} catch (NoResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (NonUniqueResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		} catch (Exception e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXcrrIdXsexoTtl(int crrId, int sexoTtl) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException{
		ConfiguracionCarrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
			sbsql.append(" Where cncr.cncrCarrera.crrId =:crrId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			retorno =(ConfiguracionCarrera) q.setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e) {
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarCncrXcrrIdXsexoTtl(int crrId, int sexoTtl) throws  ConfiguracionCarreraException{
		ConfiguracionCarrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
			sbsql.append(" Where cncr.cncrCarrera.crrId =:crrId ");
			sbsql.append(" and  cncr.cncrTitulo.ttlSexo =:sexoTtl ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("sexoTtl", sexoTtl);
			retorno =(ConfiguracionCarrera) q.getSingleResult();
		} catch (NoResultException e) {
			//TODO: HACER MENSAJE DAVID
			retorno=null;
//			throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (NonUniqueResultException e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		} catch (Exception e) {
			//TODO: HACER MENSAJE DAVID
			throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
}
