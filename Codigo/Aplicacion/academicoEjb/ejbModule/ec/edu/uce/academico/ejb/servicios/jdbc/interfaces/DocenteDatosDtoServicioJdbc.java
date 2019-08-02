/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 ************************************************************************* 

 ARCHIVO:     DocenteDatosDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc del DTO DocenteDatosDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 30-AGOSTO-2017			Arturo Villafuerte				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;
 

import ec.edu.uce.academico.ejb.dtos.DocenteDatosDto;
import ec.edu.uce.academico.ejb.excepciones.DocenteDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDatosDtoNoEncontradoException;

/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc del DTO DocenteDatosDto.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface DocenteDatosDtoServicioJdbc {

	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param dcnId ide del docente a buscar
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws DocenteDatosDtoNoEncontradoException DocenteDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws DocenteDatosDtoException DocenteDatosDtoException excepción general lanzada
	 */
	public DocenteDatosDto buscarPdcnIdProlId(int dcnId, int rolId) throws DocenteDatosDtoNoEncontradoException, DocenteDatosDtoException;
	
	 
}
