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

 ARCHIVO:     GrupoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Grupo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 06-FEBRERO-2018          Arturo Villafuerte                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.GrupoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.GrupoConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Grupo;

/**
 * Clase (Bean)GrupoServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class GrupoServicioImpl implements GrupoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Grupo por su id
	 * @param id - de la Grupo a buscar
	 * @return Grupo con el id solicitado
	 * @throws GrupoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Grupo con el id solicitado
	 * @throws GrupoException - Excepcion general
	 */
	@Override
	public Grupo buscarPorId(Integer id) throws GrupoNoEncontradoException, GrupoException {
		Grupo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Grupo.class, id);
			} catch (NoResultException e) {
				throw new GrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new GrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new GrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Grupo existentes en la BD activas
	 * @return lista de todas las entidades Grupo existentes en la BD activas
	 * @throws GrupoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Grupo 
	 * @throws GrupoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> listarTodosActivos() throws GrupoNoEncontradoException , GrupoException {
	List<Grupo> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select grp from Grupo grp ");
			sbsql.append(" where grp.grpEstado =:estado ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("estado", GrupoConstantes.ESTADO_GRUPO_ACTIVO_VALUE);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new GrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.listar.todos.activos.no.result.exception")));
		} catch (Exception e) {
			throw new GrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.listar.todos.activos.exception")));
		}
		return retorno;
	}
	

	@SuppressWarnings("unchecked")
	public List<Grupo> buscarPorCarrera(int carreraId) throws GrupoNoEncontradoException , GrupoException{
		List<Grupo> retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select grp from Grupo grp ");
			sbsql.append(" where grp.grpCarrera.crrId = :carrera ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("carrera", carreraId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new GrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.listar.todos.activos.no.result.exception")));
		} catch (Exception e) {
			throw new GrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Grupo.listar.todos.activos.exception")));
		}
		
		return retorno;
	}
	
	public boolean crear(Grupo entidad) throws GrupoException {
		Boolean retorno = false;

		try {

			Grupo grupo = new Grupo();
			grupo.setGrpDescripcion(entidad.getGrpDescripcion());
			grupo.setGrpEstado(entidad.getGrpEstado());
			grupo.setGrpCarrera(new Carrera(entidad.getGrpCarrera().getCrrId()));
			em.persist(grupo);
			em.flush();

			retorno = true;

		} catch (Exception e) {
			throw new GrupoException("Error desconocido al añadir Grupo.");
		}

		return retorno;
	}

	public boolean editar(Grupo entidad) throws GrupoException {
		Boolean retorno = false;

		try {

			Grupo grupo = new Grupo();
			grupo = em.find(Grupo.class, entidad.getGrpId());
			grupo.setGrpDescripcion(entidad.getGrpDescripcion());
			grupo.setGrpEstado(entidad.getGrpEstado());
			em.merge(grupo);
			em.flush();
			retorno = true;

		} catch (Exception e) {
			throw new GrupoException("Error desconocido al añadir Grupo.");
		}

		return retorno;
	}
	
}
