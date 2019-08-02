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

 ARCHIVO:     ArancelServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ArancelServicioImpl. 
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

import ec.edu.uce.academico.ejb.excepciones.ArancelException;
import ec.edu.uce.academico.ejb.excepciones.ArancelNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ArancelValidacionException;
import ec.edu.uce.academico.ejb.servicios.interfaces.ArancelServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Arancel;

/**
 * Clase (Bean)ArancelServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class ArancelServicioImpl implements ArancelServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Arancel por su id
	 * @param id - deL Arancel a buscar
	 * @return Arancel con el id solicitado
	 * @throws ArancelNoEncontradoException - Excepcion lanzada cuando no se encuentra una Arancel con el id solicitado
	 * @throws ArancelException - Excepcion general
	 */
	@Override
	public Arancel buscarPorId(Integer id) throws ArancelNoEncontradoException, ArancelException {
		Arancel retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Arancel.class, id);
			} catch (NoResultException e) {
				throw new ArancelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ArancelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ArancelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Arancel existentes en la BD
	 * @return lista de todas las entidades Arancel existentes en la BD
	 * @throws ArancelNoEncontradoException - Excepcion lanzada cuando no se encuentra una Arancel
	 * @throws ArancelException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Arancel> listarXGratuidadXTipoMatriculaXModalidadXTipoArancel(int tipoGratuidadId, int tipoMatricula, int modalidadId, int tipoArancel) throws ArancelNoEncontradoException , ArancelException {
		List<Arancel> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select arn from Arancel arn where ");
			sbsql.append(" arn.arnTipoGratuidad.tigrId = :tipoGratuidadId ");
			sbsql.append(" and arn.arnTipoMatricula = :tipoMatricula ");
			sbsql.append(" and arn.arnModalidad.mdlId = :modalidadId ");
			sbsql.append(" and arn.arnTipo = :tipoArancel ");
			sbsql.append(" order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("tipoGratuidadId", tipoGratuidadId);
			q.setParameter("tipoMatricula", tipoMatricula);
			q.setParameter("modalidadId", modalidadId);
			q.setParameter("tipoArancel", tipoArancel);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ArancelNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.listar.todos.no.result.exception")));
		} catch (Exception e) {
			throw new ArancelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.listar.todos.exception")));
		}
		return retorno;
	}
 
	/**
	 * Añade una Arancel en la BD
	 * @return Si se añadio o no la Arancel
	 * @throws ArancelValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws ArancelException - Excepción general
	 */
	@Override
	public Arancel anadir(Arancel entidad) throws ArancelValidacionException, ArancelException {
		Arancel retorno = null;
		if (entidad != null) {
			try {
				em.persist(entidad);
				retorno = entidad;
			} catch (Exception e) { 
				throw new ArancelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.anadir.exception")));
			}
		} else {
			throw new ArancelException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Arancel.anadir.null.exception")));
		}
		return retorno;
	}

	 


}