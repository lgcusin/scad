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

 ARCHIVO:     CampoFormacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla CampoFormacion. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 07-Agosto-2017           Marcelo Quishpe 						Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.CampoFormacionNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.CampoFormacionServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.CampoFormacion;

/**
 * Clase (Bean)CampoFormacionServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class CampoFormacionServicioImpl implements CampoFormacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Lista todas los CampoFormacion existentes en la BD
	 * @return lista de todas los CampoFormacion existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<CampoFormacion> listarTodos() throws CampoFormacionNoEncontradoException{
		List<CampoFormacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select cmfr from CampoFormacion cmfr ");
		sbsql.append(" Order by cmfr.cmfrDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new CampoFormacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CampoFormacion.buscar.todos.no.result.exception")));
		}
		return retorno;

	}
	


}
