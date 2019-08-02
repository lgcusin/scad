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
   
 ARCHIVO:     ComprobanteCSVDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-04-2017		Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ComprobanteCSVDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobanteCSVDtoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobanteCSVDtoNoEncontradoException;

/**
 * Interface ComprobanteCSVDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 * @author dalbuja
 * @version 1.0
 */
public interface ComprobanteCSVDtoServicioJdbc {

	public List<ComprobanteCSVDto> buscarEmitidosGeneracionCSV()
			throws ComprobanteCSVDtoNoEncontradoException, ComprobanteCSVDtoException;

	public ComprobanteCSVDto buscarComprobantePagoParaEditarReimpresion(String numComprobante)
			throws ComprobanteCSVDtoNoEncontradoException, ComprobanteCSVDtoException;

	public List<ComprobanteCSVDto> buscarComprobantesPagoParaEditarReimpresion(String numComprobante)
			throws ComprobanteCSVDtoNoEncontradoException, ComprobanteCSVDtoException;
	
	
}
