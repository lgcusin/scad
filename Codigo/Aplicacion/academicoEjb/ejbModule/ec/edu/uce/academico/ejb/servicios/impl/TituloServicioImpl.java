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

 ARCHIVO:     RolServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Titulo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017           Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Titulo;

/**
 * Clase (Bean)RolServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class TituloServicioImpl implements TituloServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	
	
	/**
	 * Busca el titulo por descripcion
	 * @return titulo por descripcion
	 */
	@Override
	public Titulo buscarTituloXDescripcion(String ttlDescripcion) {
		Titulo retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select t from Titulo t where ");
			sbsql.append(" UPPER(t.ttlDescripcion) =:ttlDescripcion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("ttlDescripcion", ttlDescripcion.toUpperCase());
			retorno = (Titulo)q.getSingleResult();
		} catch (Exception e) {
		}
		return retorno;	
	}

	/**
	 * Busca el titulo por id
	 * @return titulo por id
	 */
	@Override
	public Titulo buscarTituloXId(Integer ttlId) {
		Titulo retorno = null;
		try {
			retorno = em.find(Titulo.class, ttlId);
		} catch (Exception e) {
		}
		return retorno;	
	}
	
	
}
