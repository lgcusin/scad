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

 ARCHIVO:     PuestoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla PuestoServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 20-10-2017           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.PuestoException;
import ec.edu.uce.academico.ejb.excepciones.PuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.PuestoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;

/**
 * Clase (Bean)PuestoServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class PuestoServicioImpl implements PuestoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Puesto por su id
	 * @param id - deL Puesto a buscar
	 * @return Puesto con el id solicitado
	 * @throws PuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Puesto con el id solicitado
	 * @throws PuestoException - Excepcion general
	 */
	@Override
	public Puesto buscarPorId(Integer id) throws PuestoNoEncontradoException, PuestoException {
		Puesto retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Puesto.class, id);
			} catch (NoResultException e) {
				throw new PuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new PuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new PuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Puesto existentes en la BD
	 * @return lista de todas las entidades Puesto existentes en la BD
	 * @throws PuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws PuestoException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Puesto> listarTodos() throws PuestoNoEncontradoException , PuestoException {
		List<Puesto> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select pst from Puesto pst order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new PuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca la entidad Puesto existente en la BD
	 * @return entidad Puesto existente en la BD
	 * @throws PuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws PuestoException - Excepcion general
	 */
	public Puesto buscarPuesto(int dedicacion, int categoria, int rango) throws PuestoNoEncontradoException , PuestoException {
		Puesto retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			
			sbsql.append(" Select pst from Puesto pst, TiempoDedicacion tpdd ");
			sbsql.append(" Where tpdd.tmddId = pst.pstTiempoDedicacion.tmddId");
			sbsql.append(" and tpdd.tmddId = :dedicacion");
			sbsql.append(" and pst.pstCategoriaDocente = :categoria");
			
			if(rango != GeneralesConstantes.APP_ID_BASE){
				sbsql.append(" and pst.pstNivelRangoGradual = :rango"); 
			} 
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("dedicacion", dedicacion);
			q.setParameter("categoria", categoria);
			
			if(rango != GeneralesConstantes.APP_ID_BASE){
				q.setParameter("rango", rango);
			}
			
			retorno = (Puesto)q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new PuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.buscar.por.denominacion.por.rango.por.dedicacion.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new PuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.buscar.por.denominacion.por.rango.por.dedicacion.exception")));
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Puesto> buscarPorTiempoDedicacion(int tmddId) throws PuestoNoEncontradoException , PuestoException {
		List<Puesto> retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT distinct pst.pstCategoriaDocente FROM Puesto pst WHERE pst.pstTiempoDedicacion.tmddId = :dedicacion");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("dedicacion", tmddId);

			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new PuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.listar.todos.exception")));
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Puesto> buscarPorTiempoDedicacionCategoria(int tmddId, int categoriaId) throws PuestoNoEncontradoException , PuestoException {
		List<Puesto> retorno = null;
		
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT distinct pst.pstNivelRangoGradual FROM Puesto pst WHERE pst.pstTiempoDedicacion.tmddId = :dedicacion and pst.pstCategoriaDocente = :categoria and pst.pstNivelRangoGradual is not null");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("dedicacion", tmddId);
			q.setParameter("categoria", categoriaId);

			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PuestoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new PuestoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Puesto.listar.todos.exception")));
		}
		
		return retorno;
	}
	
}