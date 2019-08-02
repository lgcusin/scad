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

 ARCHIVO:     ProcesoFlujoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ProcesoFlujo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-03-2017           David Arellano                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoException;
import ec.edu.uce.academico.ejb.excepciones.ProcesoFlujoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ProcesoFlujoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.ProcesoFlujo;

/**
 * Clase (Bean)ProcesoFlujoServicioImpl.
 * Bean declarado como Stateless.
 * @author darellano
 * @version 1.0
 */

@Stateless
public class ProcesoFlujoServicioImpl implements ProcesoFlujoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad ProcesoFlujo por su id
	 * @param id - deL ProcesoFlujo a buscar
	 * @return ProcesoFlujo con el id solicitado
	 * @throws ProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una ProcesoFlujo con el id solicitado
	 * @throws ProcesoFlujoException - Excepcion general
	 */
	@Override
	public ProcesoFlujo buscarPorId(Integer id) throws ProcesoFlujoNoEncontradoException, ProcesoFlujoException {
		ProcesoFlujo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(ProcesoFlujo.class, id);
			} catch (NoResultException e) {
				throw new ProcesoFlujoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.buscar.por.id.exception")));
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
	public List<ProcesoFlujo> listarTodos() throws ProcesoFlujoNoEncontradoException , ProcesoFlujoException {
		List<ProcesoFlujo> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prfl from ProcesoFlujo prfl order by prfl.prflId");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ProcesoFlujoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new ProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.listar.todos.exception")));
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @param descripcion - descripcion del proceso flujo a buscar
	 * @param estado - estado del proceso flujo a buscar
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws ProcesoFlujoNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws ProcesoFlujoException - Excepcion general
	 */
	@Override
	public ProcesoFlujo buscarXdescripcionXestado(String descripcion, int estado) throws ProcesoFlujoNoEncontradoException , ProcesoFlujoException {
		ProcesoFlujo retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prfl from ProcesoFlujo prfl ");
			sbsql.append(" Where prfl.prflDescripcion =:descripcion");
			sbsql.append(" And prfl.prflEstado =:estado");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("descripcion", descripcion);
			q.setParameter("estado", estado);
			retorno = (ProcesoFlujo)q.getSingleResult();
		} catch (NoResultException e) {
			throw new ProcesoFlujoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.buscar.por.descripcion.por.estado.no.result.exception")));
		} catch (Exception e) {
			throw new ProcesoFlujoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoFlujo.buscar.por.descripcion.por.estado.exception")));
		}
		return retorno;
	}
	
}
