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

 ARCHIVO:     NivelServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Nivel. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.NivelServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;

/**
 * Clase (Bean)NivelServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class NivelServicioImpl implements NivelServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Nivel por su id
	 * @param id - del Nivel a buscar
	 * @return Nivel con el id solicitado
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel con el id solicitado
	 * @throws NivelException - Excepcion general
	 */
	@Override
	public Nivel buscarPorId(Integer id) throws NivelNoEncontradoException, NivelException {
		Nivel retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Nivel.class, id);
			} catch (NoResultException e) {
				throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Nivel> listarTodos() throws NivelNoEncontradoException, NivelException {
		List<Nivel> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select nvl from Nivel nvl ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.todos.non.unique.result.exception")));
		} catch (Exception e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.todos.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Nivel> listarTodosPosgrado() throws NivelNoEncontradoException, NivelException {
		List<Nivel> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select nvl from Nivel nvl ");
			sbsql.append(" where rownum < 5 ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.todos.non.unique.result.exception")));
		} catch (Exception e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.todos.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Nivel> listarNivelacion() throws NivelNoEncontradoException, NivelException {
		List<Nivel> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select nvl from Nivel nvl where nvl.nvlNumeral = 0 ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.non.unique.result.exception")));
		} catch (Exception e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Nivel por numeral
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param numeral .- numeral del nivel
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	@Override
	public Nivel listarNivelXNumeral(int numeral) throws NivelNoEncontradoException, NivelException {
		Nivel retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select nvl from Nivel nvl where nvl.nvlNumeral =:numeral ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("numeral", numeral);
			retorno = (Nivel) q.getSingleResult();
			
		}  catch (NoResultException e) {
			throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.non.unique.result.exception")));
		} catch (Exception e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Nivel> listarSuficienciaCulturaFisica() throws NivelNoEncontradoException, NivelException {
		List<Nivel> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select nvl from Nivel nvl where nvl.nvlNumeral in (1,2,-1) ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.non.unique.result.exception")));
		} catch (Exception e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Nivel> listarSuficienciaIdiomas() throws NivelNoEncontradoException, NivelException {
		List<Nivel> retorno = null;
		try {
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select nvl from Nivel nvl where nvl.nvlNumeral in (-1,1,2,3,4) ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			
		}  catch (NoResultException e) {
			throw new NivelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.non.unique.result.exception")));
		} catch (Exception e) {
			throw new NivelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Nivel.listar.nivelacion.exception")));
		}
		
		return retorno;
	}

}
