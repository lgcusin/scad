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

 ARCHIVO:     RelacionLaboralServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla RelacionLaboralServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 03-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralException;
import ec.edu.uce.academico.ejb.excepciones.RelacionLaboralNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.RelacionLaboralServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes; 
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.RelacionLaboral;

/**
 * Clase (Bean)RelacionLaboralServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class RelacionLaboralServicioImpl implements RelacionLaboralServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad RelacionLaboral por su id
	 * @param id - deL RelacionLaboral a buscar
	 * @return RelacionLaboral con el id solicitado
	 * @throws RelacionLaboralNoEncontradoException - Excepcion lanzada cuando no se encuentra una RelacionLaboral con el id solicitado
	 * @throws RelacionLaboralException - Excepcion general
	 */
	@Override
	public RelacionLaboral buscarPorId(Integer id) throws RelacionLaboralNoEncontradoException, RelacionLaboralException {
		RelacionLaboral retorno = null;
		if (id != null) {
			try {
				retorno = em.find(RelacionLaboral.class, id);
			} catch (NoResultException e) {
				throw new RelacionLaboralNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RelacionLaboral.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RelacionLaboralException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RelacionLaboral.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RelacionLaboralException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RelacionLaboral.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades RelacionLaboral existentes en la BD
	 * @return lista de todas las entidades RelacionLaboral existentes en la BD
	 * @throws RelacionLaboralNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws RelacionLaboralException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RelacionLaboral> listarTodos() throws RelacionLaboralNoEncontradoException , RelacionLaboralException {
		List<RelacionLaboral> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rllb from RelacionLaboral rllb order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new RelacionLaboralNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RelacionLaboral.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new RelacionLaboralException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RelacionLaboral.listar.todos.exception")));
		}
		return retorno;
	}
	
		
}