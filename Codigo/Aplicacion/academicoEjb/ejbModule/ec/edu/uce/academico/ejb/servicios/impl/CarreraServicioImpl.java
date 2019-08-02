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

 ARCHIVO:     CarreraServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Carrera. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
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

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;

/**
 * Clase (Bean)CarreraServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class CarreraServicioImpl implements CarreraServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Carrera por su id
	 * @param id - de la Carrera a buscar
	 * @return Carrera con el id solicitado
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CarreraException - Excepcion general
	 */
	@Override
	public Carrera buscarPorId(Integer id) throws CarreraNoEncontradoException, CarreraException {
		Carrera retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Carrera.class, id);
			} catch (NoResultException e) {
				throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**MQ
	 * Busca una entidad Carrera por su id sin excepciones
	 * @param id - de la Carrera a buscar
	 * @return Carrera con el id solicitado
	 
	 */
	@Override
	public Carrera buscarPorIdSinException(Integer id){
		Carrera retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Carrera.class, id);
			} catch (NoResultException e) {
				//throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.no.result.exception",id)));
				return retorno;
			}catch (NonUniqueResultException e) {
				//throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.non.unique.result.exception",id)));
				return retorno;
			} catch (Exception e) {
				//throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.por.id.exception")));
				return retorno;
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> listarTodos() throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" where crr.crrTipo =:tipoCarrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_PREGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas carreras por facultad existentes en la BD
	 * @return Lista todas carreras por facultad existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<Carrera> listarCarrerasXFacultad(int facultadId) throws CarreraNoEncontradoException , CarreraException {
		
		List<Carrera> retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where crr.crrDependencia.dpnId =:facultadId ");
			sbsql.append(" and crr.crrTipo =:tipoCarrera ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_PREGRADO_VALUE);
			
			retorno = q.getResultList();
			
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		
		if (retorno.size() == Integer.valueOf(0)) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select crr from Carrera crr ");
				sbsql.append(" Where crr.crrDependencia.dpnId =:facultadId ");
				sbsql.append(" and crr.crrTipo =:tipoCarrera ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("facultadId", facultadId);
				q.setParameter("tipoCarrera", CarreraConstantes.TIPO_NIVELEACION_VALUE);
				
				retorno = q.getResultList();
				
			} catch (Exception e) {
				throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
			}
			
		}
		if (retorno.size() == Integer.valueOf(0)) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select crr from Carrera crr ");
				sbsql.append(" Where crr.crrDependencia.dpnId =:facultadId ");
				sbsql.append(" and crr.crrTipo =:tipoCarrera ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("facultadId", facultadId);
				q.setParameter("tipoCarrera", CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
				
				retorno = q.getResultList();
				
			} catch (Exception e) {
				throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
			}
		}
		if (retorno.size() == Integer.valueOf(0)) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas carreras por facultad existentes en la BD
	 * @return Lista todas carreras por facultad existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<Carrera> listarCarrerasYSuficienciaXFacultad(int facultadId) throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where crr.crrDependencia.dpnId =:facultadId ");
			sbsql.append(" and ( crr.crrTipo =:tipoCarreraPre ");
			sbsql.append(" or crr.crrTipo =:tipoCarreraSuf ) ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("tipoCarreraPre", CarreraConstantes.TIPO_PREGRADO_VALUE);
			q.setParameter("tipoCarreraSuf", CarreraConstantes.TIPO_SUFICIENCIA_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas carreras por facultad y carrera existentes en la BD
	 * @return Lista todas carreras por facultad y carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad y carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<Carrera> listarCarrerasXFacultad(int facultadId, int carreraId) throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where ");
			if(facultadId == GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" crr.crrDependencia.dpnId >:facultadId ");
			}else{
				sbsql.append(" crr.crrDependencia.dpnId =:facultadId ");
			}
			
			if(carreraId == GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" and crr.crrId >:carreraId ");
			}else{
				sbsql.append(" and crr.crrId =:carreraId ");
			}
			sbsql.append(" and crr.crrTipo =:tipoCarrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("carreraId", carreraId);
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_PREGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> listarTodosPosgrado() throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" where crr.crrTipo =:tipoCarrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_POSGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas carreras por facultad existentes en la BD
	 * @return Lista todas carreras por facultad existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> listarCarrerasXFacultadPosgrado(int facultadId) throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where crr.crrDependencia.dpnId =:facultadId ");
			sbsql.append(" and crr.crrTipo =:tipoCarrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_POSGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista todas carreras por facultad y carrera existentes en la BD
	 * @return Lista todas carreras por facultad y carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad y carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> listarCarrerasXFacultadPosgrado(int facultadId, int carreraId) throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where ");
			if(facultadId == GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" crr.crrDependencia.dpnId >:facultadId ");
			}else{
				sbsql.append(" crr.crrDependencia.dpnId =:facultadId ");
			}
			
			if(carreraId == GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" and crr.crrId >:carreraId ");
			}else{
				sbsql.append(" and crr.crrId =:carreraId ");
			}
			sbsql.append(" and crr.crrTipo =:tipoCarrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("carreraId", carreraId);
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_POSGRADO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas carreras por facultad y carrera existentes en la BD
	 * @return Lista todas carreras por facultad y carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad y carrera 
	 * @throws CarreraException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> listarCarrerasXFacultadNivelacion(int facultadId, int carreraId) throws CarreraNoEncontradoException , CarreraException {
		List<Carrera> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where ");
			if(facultadId == GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" crr.crrDependencia.dpnId >:facultadId ");
			}else{
				sbsql.append(" crr.crrDependencia.dpnId =:facultadId ");
			}
			
			if(carreraId == GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" and crr.crrId >:carreraId ");
			}else{
				sbsql.append(" and crr.crrId =:carreraId ");
			}
			sbsql.append(" and crr.crrTipo =:tipoCarrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("carreraId", carreraId);
			q.setParameter("tipoCarrera", CarreraConstantes.TIPO_NIVELEACION_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.no.result.exception")));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.carreras.por.facultad.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Lista la facultad de acuerdo a la carrera enviada
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD
	 */
	@Override
	public Carrera buscarFacultadXCarrera(int crrId) throws DependenciaNoEncontradoException{
		Carrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where crr.crrId =:crrId");
//			sbsql.append(" and  crr.crrEstado =:activo");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
//			q.setParameter("activo", DependenciaConstantes.ESTADO_ACTIVO_VALUE);
			retorno = (Carrera) q.getSingleResult();	
		} catch (Exception e) {
			throw new DependenciaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.todos.no.result.exception")));
		}
		
		return retorno;

	}

	public Carrera buscarCarreraXEspeCodigo(int espeCodigo) throws CarreraNoEncontradoException, CarreraException{
		Carrera retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select crr from Carrera crr ");
			sbsql.append(" Where crr.crrEspeCodigo =:espeCodigo");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("espeCodigo", espeCodigo);
			retorno = (Carrera) q.getSingleResult();	
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException("Error, no se encontró la carrera con espeCodigo "+espeCodigo);
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Dependencia.buscar.todos.no.result.exception")));
		}
		
		return retorno;

	}
	

	@SuppressWarnings("unchecked")
	public List<Carrera> buscarCarreras(int facultadId, int crrTipo) throws CarreraNoEncontradoException, CarreraException{
		List<Carrera> retorno = null;

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select crr from Carrera crr ");
		sbsql.append(" Where crr.crrDependencia.dpnId = :facultadId ");
		sbsql.append(" and crr.crrTipo = :crrTipo ");
		
		try {
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("facultadId", facultadId);
			q.setParameter("crrTipo", crrTipo);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException("Error, no se encontró carreras con los parámetros solicitados.");
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
		}
		
		return retorno;

	}
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 */
	@SuppressWarnings("unchecked")
	public List<Carrera> listarCarrerasActivasEvaluacionXTipoCarrera(int dependenciaId, int tipoCarrera, int periodoId) throws CarreraNoEncontradoException{
		List<Carrera> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct crr from Carrera crr, Paralelo prl, MallaCurricularParalelo mlcrpr, CargaHoraria crhr ");
		sbsql.append(" Where crr.crrId = prl.prlCarrera.crrId ");
		sbsql.append(" and prl.prlId = mlcrpr.mlcrprParalelo.prlId ");
		sbsql.append(" and mlcrpr.mlcrprId = crhr.crhrMallaCurricularParalelo.mlcrprId");
		sbsql.append(" and crr.crrDependencia.dpnId =:dependenciaId");
		sbsql.append(" and  crr.crrTipo =:tipoCarrera");
		sbsql.append(" and  prl.prlPeriodoAcademico.pracId =:periodoId");
		sbsql.append(" order by crr.crrDescripcion");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("dependenciaId", dependenciaId);
		q.setParameter("tipoCarrera", tipoCarrera);
		q.setParameter("periodoId", periodoId);
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.facultades.activas.no.result.exception")));
		}
		return retorno;

	}

	public Carrera buscarAreaXCarrera(Integer crrId) throws CarreraNoEncontradoException{
		Carrera retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select distinct area from Carrera crr, CarreraArea crar, Carrera area ");
		sbsql.append(" Where area.crrId = crar.crarArea.crrId ");
		sbsql.append(" and crar.crarCarrera.crrId = crr.crrId ");
		sbsql.append(" and  crr.crrId =:crrId");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("crrId", crrId);
		retorno = (Carrera) q.getSingleResult();
		return retorno;

	}
	
	
	public Carrera buscarCarreraPorGrupoId(int grupoId) throws CarreraNoEncontradoException, CarreraException{
		Carrera retorno = null;

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT g.grpCarrera FROM Grupo g");
		sbsql.append(" WHERE g.grpId = :grupoId ");
		
		try {
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("grupoId", grupoId);
			retorno = (Carrera)q.getSingleResult();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException("Error, no se encontró carreras con los parámetros solicitados.");
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.listar.todos.exception")));
		}
		
		return retorno;

	}
	
	@SuppressWarnings("unchecked")
	public List<Carrera> buscarCarrerasPorUsuarioRol(int usuarioId, int rolId, int usroEstado) throws CarreraNoEncontradoException, CarreraException{
		List<Carrera> retorno = new ArrayList<>();

		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" Select crr from Carrera crr, Usuario usr, UsuarioRol usro, RolFlujoCarrera roflcr where ");
		sbSql.append(" usr.usrId = usro.usroUsuario.usrId ");
		sbSql.append(" and usro.usroId = roflcr.roflcrUsuarioRol.usroId ");
		sbSql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
		sbSql.append(" and usr.usrId = :usuarioId ");
		sbSql.append(" and usro.usroRol.rolId = :rolId ");
		sbSql.append(" and usro.usroEstado = :usroEstado ");
		
		try{
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("usuarioId",usuarioId);
			q.setParameter("rolId",rolId);
			q.setParameter("usroEstado",usroEstado);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new CarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.no.result.exception",usuarioId)));
		} catch (Exception e) {
			throw new CarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.rol.usuarioid.exception")));
		}

		return retorno;
	}
}
