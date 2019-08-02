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

 ARCHIVO:     TipoFuncionCargaHorariaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoFuncionCargaHoraria. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
31-08-2017            Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoFuncionCargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.TipoFuncionCargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoFuncionCargaHorariaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFuncionCargaHoraria;

/**
 * Clase (Bean)TipoFuncionCargaHorariaServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class TipoFuncionCargaHorariaServicioImpl implements TipoFuncionCargaHorariaServicio {

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TipoFuncionCargaHoraria por su id
	 * @param id - del TipoFuncionCargaHoraria a buscar
	 * @return TipoFuncionCargaHoraria con el id solicitado
	 * @throws TipoFuncionCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoFuncionCargaHoraria con el id solicitado
	 * @throws TipoFuncionCargaHorariaException - Excepcion general
	 */ 
	public TipoFuncionCargaHoraria buscarPorId(Integer id) throws TipoFuncionCargaHorariaNoEncontradoException, TipoFuncionCargaHorariaException {
		TipoFuncionCargaHoraria retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TipoFuncionCargaHoraria.class, id);
			} catch (NoResultException e) { 
				throw new TipoFuncionCargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFuncionCargaHoraria.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) { 
				throw new TipoFuncionCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFuncionCargaHoraria.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) { 
				throw new TipoFuncionCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFuncionCargaHoraria.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades TipoFuncionCargaHoraria existentes en la BD
	 * @return lista de todas las entidades TipoFuncionCargaHoraria existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFuncionCargaHoraria> listarTodosActivosXTipo(int idTipoCargaHoraria) throws TipoFuncionCargaHorariaNoEncontradoException, TipoFuncionCargaHorariaException{
		
		List<TipoFuncionCargaHoraria> retorno = null;
		
		try{
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select tifncrhr from TipoFuncionCargaHoraria tifncrhr where tifncrhrTipoCargaHoraria.ticrhrId =:idTipoCargaHoraria");
		sbsql.append(" and tifncrhr.tifncrhrEstado = 0");

		Query q = em.createQuery(sbsql.toString());
		q.setParameter("idTipoCargaHoraria", idTipoCargaHoraria);
		retorno = q.getResultList();
		
	} catch (NoResultException e) {
		throw new TipoFuncionCargaHorariaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFuncionCargaHoraria.listar.todos.activos.tipo.no.result.exception")));
	} catch (Exception e) {
		throw new TipoFuncionCargaHorariaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoFuncionCargaHoraria.listar.todos.activos.exception")));
	}
		
		return retorno;
	}
	
}
