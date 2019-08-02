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

 ARCHIVO:     GratuidadServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla GratuidadServicioImpl. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 17-01-2018           Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.GratuidadServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;

/**
 * Clase (Bean)GratuidadServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class GratuidadServicioImpl implements GratuidadServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	
	/**
	 * Buscar Gratuidad existentes en la BD
	 * @return Buscar la entidade Gratuidad existente en la BD
	 * @throws GratuidadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Gratuidad
	 * @throws GratuidadException - Excepcion general
	 */
	@Override
	public Gratuidad buscarGratuidadXFichaMatricula(int fcmtId) throws GratuidadNoEncontradoException , GratuidadException {
		Gratuidad retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select grt from Gratuidad grt where ");
			sbsql.append(" grt.grtFichaMatricula.fcmtId = :fcmtId ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fcmtId", fcmtId);
			retorno = (Gratuidad) q.getSingleResult();
		} catch (NoResultException e) {
			throw new GratuidadNoEncontradoException("No se encontro gratuidad en la matrícula seleccionada.");
		} catch (Exception e) {
			throw new GratuidadException("Error al buscar gratuidad en la matrícula seleccionada.");
		}
		return retorno;
	}
 
}