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

 ARCHIVO:     TpcnFuncionTpevServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TpcnFuncionTpevServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevException;
import ec.edu.uce.academico.ejb.excepciones.TpcnFuncionTpevNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TpcnFuncionTpevServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TpcnFuncionTpev;

/**
 * Clase (Bean)TpcnFuncionTpevServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class TpcnFuncionTpevServicioImpl implements TpcnFuncionTpevServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TpcnFuncionTpev por su id
	 * @param id - deL TpcnFuncionTpev a buscar
	 * @return TpcnFuncionTpev con el id solicitado
	 * @throws TpcnFuncionTpevNoEncontradoException - Excepcion lanzada cuando no se encuentra una TpcnFuncionTpev con el id solicitado
	 * @throws TpcnFuncionTpevException - Excepcion general
	 */
	@Override
	public TpcnFuncionTpev buscarPorId(Integer id) throws TpcnFuncionTpevNoEncontradoException, TpcnFuncionTpevException {
		TpcnFuncionTpev retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TpcnFuncionTpev.class, id);
			} catch (NoResultException e) {
				throw new TpcnFuncionTpevNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TpcnFuncionTpev.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TpcnFuncionTpevException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TpcnFuncionTpev.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TpcnFuncionTpevException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TpcnFuncionTpev.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * La entidad TpcnFuncionTpev existentes en la BD
	 * @param  idTipoContenido .- Id del tipo contenido
	 * @return Entidad TpcnFuncionTpev existentes en la BD
	 * @throws TpcnFuncionTpevNoEncontradoException - Excepcion lanzada cuando no se encuentra una TpcnFuncionTpev
	 * @throws TpcnFuncionTpevException - Excepcion general
	 */
	@Override
	public TpcnFuncionTpev buscarXTipoContenido(int idTipoContenido) throws TpcnFuncionTpevNoEncontradoException , TpcnFuncionTpevException {
		TpcnFuncionTpev retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select tpcnfntpev from TpcnFuncionTpev tpcnfntpev where tpcnfntpev.tpcnfntpevTipoContenido.tpcnId =:idTipoContenido order by 1 asc");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("idTipoContenido", idTipoContenido);
			retorno = (TpcnFuncionTpev) q.getSingleResult();
		} catch (NoResultException e) {
			throw new TpcnFuncionTpevNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TpcnFuncionTpev.buscar.tipo.contenido.no.result.exception")));
		} catch (Exception e) {
			throw new TpcnFuncionTpevException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TpcnFuncionTpev.buscar.tipo.contenido.exception")));
		}
		return retorno;
	}
}