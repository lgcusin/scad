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

 ARCHIVO:     TipoMateriaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TipoMateria. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-Agosto-2017           Marcelo Quishpe 						Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.TipoMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.TipoMateriaServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.TipoMateria;

/**
 * Clase (Bean)TipoMateriaServicioImpl.
 * Bean declarado como Stateless.
 * @author lmquishpei
 * @version 1.0
 */

@Stateless
public class TipoMateriaServicioImpl implements TipoMateriaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;


	/**
	 * Lista todas los TipoMateria existentes en la BD
	 * @return lista de todas los TipoMateria existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<TipoMateria> listarTodos() throws TipoMateriaNoEncontradoException{
		List<TipoMateria> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select timt from TipoMateria timt ");
		sbsql.append(" Order by timt.timtDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			throw new TipoMateriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoMateria.buscar.todos.no.result.exception")));
		}
		return retorno;

	}
	


}
