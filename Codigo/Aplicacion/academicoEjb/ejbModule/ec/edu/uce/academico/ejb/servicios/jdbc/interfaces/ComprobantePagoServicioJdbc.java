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
   
 ARCHIVO:     ComprobantePagoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-08-2018			Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.math.BigDecimal;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteValidacionException;

/**
 * Interface ComprobantePagoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 * @author fgguzman
 * @version 1.0
 */
public interface ComprobantePagoServicioJdbc {

	/**
	 * Método que permite buscar comprobantes de pago registrados en SYSREC.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte de la persona.
	 * @param fechaPartida - fecha inicial para búsqueda formato DD/MM/YYYY
	 * @return comprobantes de pago.
	 * @throws ComprobanteNoEncontradoException
	 * @throws ComprobanteException
	 */
	List<ComprobantePagoDto> buscarComprobantesDePago(String identificacion, String fechaPartida) throws ComprobantePagoException, ComprobantePagoNoEncontradoException;
	
	/**
	 * Método que permite traer los comprobantes de pago ya conciliados del SIIU desde el SYSREC 
	 * @param fecha - formato yyyy-MM-dd de busqueda
	 * @return comrobantes encontrados
	 * @throws ComprobantePagoException
	 * @throws ComprobantePagoNoEncontradoException
	 */
	List<ComprobantePagoDto> buscarComprobantesDePagoFecha(String fecha) throws ComprobantePagoException, ComprobantePagoNoEncontradoException;
	
	/**
	 * Método que permite verificar si el comprobante de pago a sido actualizado en base SIIU
	 * @author fgguzman
	 * @param numero - numero de comprobante
	 * @return null si no esta actualizado a pagado.
	 * @throws ComprobantePagoException
	 * @throws ComprobantePagoNoEncontradoException
	 */
	ComprobantePagoDto buscarComprobante (String numero) throws ComprobantePagoException, ComprobantePagoNoEncontradoException, ComprobantePagoValidacionException;
	
	
    /**
     * Método que permite buscar el valor a pagar por la matricula del estudiante.
     * @author fgguzman
     * @param identificacion - cedula o pasaporte
     * @param carreraId - id de la carrera
     * @param periodoId - id del periodo
     * @throws ComprobantePagoNoEncontradoException
     * @throws ComprobantePagoException
     */
	BigDecimal buscarValorAPagarMatricula(String identificacion, int carreraId, int periodoId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException;
	

	 /**
     * Método que permite buscar la matricula del estudiante.
     * @author fgguzman
     * @param identificacion - cedula o pasaporte
     * @param carreraId - id de la carrera
     * @param periodoId - id del periodo
     * @throws ComprobantePagoNoEncontradoException
     * @throws ComprobantePagoException
     */
	List<RecordEstudianteDto> buscarMatricula(String identificacion, int carreraId, int periodoId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite legalizar la matricula del estudiante.
	 * @param identificacion
	 * @param carreraId
	 * @param periodoId
	 * @return registros actualizados
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	Integer legalizarMatricula(String identificacion, int carreraId, int periodoId) throws RecordEstudianteValidacionException, RecordEstudianteException;

	
	/**
	 * Método que permite legalizar la matricula del estudiante.
	 * @param identificacion
	 * @param numeroComprobane
	 * @return registros actualizados
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	Integer legalizarMatricula(String identificacion, String numeroComprobante) throws RecordEstudianteValidacionException, RecordEstudianteException;

	
	/**
	 * Método que permite legalizar la matricula del estudiante.
	 * @param identificacion
	 * @param comprobane
	 * @return registros actualizados
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	boolean actualizarComprobateMatricula(String identificacion, ComprobantePagoDto comprobante) throws RecordEstudianteValidacionException, RecordEstudianteException;

	/**
	 * Método que permite recuperar comprobantes de pago solo de informatica.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte
	 * @param fechaInicioBusqueda - fecha de matricula
	 * @return comprobangtes generados
	 * @throws ComprobantePagoException
	 * @throws ComprobantePagoNoEncontradoException
	 */
	List<ComprobantePagoDto> buscarComprobantesDePagoInformatica(String identificacion, String fechaInicioBusqueda) throws ComprobantePagoException, ComprobantePagoNoEncontradoException ;
	
	List<ComprobantePagoDto> buscarComprobantesDePagoPagados(String num_comprobante) throws ComprobantePagoException, ComprobantePagoNoEncontradoException;

}
