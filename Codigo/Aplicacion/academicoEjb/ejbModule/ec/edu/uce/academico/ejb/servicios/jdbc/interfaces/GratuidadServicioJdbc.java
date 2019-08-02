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

 ARCHIVO:     GratuidadServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la Gratuidad del SAIU.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 23Jul2018			 Freddy Guzmán				          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.RecordEstudianteSAUDto;
import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;

/**
 * Interface GratuidadServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la Gratuidad del SAIU.
 * @author fgguzman
 * @version 1.0
 */
public interface GratuidadServicioJdbc {


	/**
	 * Método que permite recuperar los Estados de gratuidad de los periodos academicos que el estudiante se ha matriculado. 
	 * @param prsIdentificacion - cedula o pasaporte del estudiante.
	 * @return record estudiantil con estados de gratuidad.
	 * @throws GratuidadNoEncontradoException
	 * @throws GratuidadException
	 */
	List<RecordEstudianteSAUDto> buscarRecordSAU(String prsIdentificacion) throws GratuidadNoEncontradoException, GratuidadException;
	
	/**
	 * Método que permite consultar si un estudiante ya registra una segunda carrera  despues de haber obtenido un titulo en una universidad publica en el SIIU.
	 * @param prsIdentificacion - cedula o pasaporte del estudiante.
	 * @return true - si pierde la gratuidad por segunda carrera despues de titulado.
	 * @throws GratuidadNoEncontradoException
	 * @throws GratuidadException
	 */
	boolean buscarSegundaCarrera(String prsIdentificacion) throws GratuidadNoEncontradoException, GratuidadException;
	 
}

