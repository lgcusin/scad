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

 ARCHIVO:     EtniaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Etnia. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-02-2018           Marcelo Quishpe                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.EtniaException;
import ec.edu.uce.academico.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Etnia;

/**
 * Clase (Bean)EtniaServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class EtniaServicioImpl implements EtniaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Etnia por su id
	 * @param id - del Etnia a buscarEtnia.buscar.por.id.no.result.exception
	 * @return Etnia con el id solicitado
	 * @throws EtniaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Etnia con el id solicitado
	 * @throws EtniaException - Excepcion general
	 */
	@Override
	public Etnia buscarPorId(Integer id) throws EtniaNoEncontradoException, EtniaException {
		Etnia retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Etnia.class, id);
			} catch (NoResultException e) {
				throw new EtniaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new EtniaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new EtniaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Etnia existentes en la BD
	 * @return lista de todas las entidades Etnia existentes en la BD
	 * @throws EtniaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Etnia 
	 * @throws EtniaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Etnia> listarTodos() throws EtniaNoEncontradoException , EtniaException {
		List<Etnia> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select etn from Etnia etn order by etnDescripcion asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new EtniaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new EtniaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	
	
	
	
	
	
}
