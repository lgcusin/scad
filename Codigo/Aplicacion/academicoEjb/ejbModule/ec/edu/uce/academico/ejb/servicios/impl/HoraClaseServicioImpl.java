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

 ARCHIVO:     HoraClaseServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla HoraClase. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 14-SEP-2017           MARCELO QUISHPE                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.HoraClaseAulaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseException;
import ec.edu.uce.academico.ejb.servicios.interfaces.HoraClaseServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.HoraClase;

/**
 * Clase (Bean)HoraClaseServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class HoraClaseServicioImpl implements HoraClaseServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Lista todas las entidades Horas Clase existentes en la BD
	 * @return lista de todas las entidades Horas Clase existentes en la BD
	 * @throws HoraClaseAulaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws HoraClaseException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HoraClase> listarTodos() throws HoraClaseAulaNoEncontradoException, HoraClaseException {
		List<HoraClase> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select hocl from HoraClase hocl ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
		}  catch (NoResultException e) {
			throw new HoraClaseAulaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClase.listar.todos.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new HoraClaseException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClase.listar.todos.non.unique.result.exception")));
		} catch (Exception e) {
			throw new HoraClaseException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HoraClase.listar.todos.exception")));
		}
		return retorno;
	}
	
	
	

	
	

	


	
	
}
