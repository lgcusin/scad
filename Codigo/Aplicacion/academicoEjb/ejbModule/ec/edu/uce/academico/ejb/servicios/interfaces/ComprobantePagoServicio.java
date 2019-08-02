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
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ComprobanteCSVDto;
import ec.edu.uce.academico.ejb.dtos.ComprobantePagoDto;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ComprobantePagoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.ComprobantePago;

/**
 * Interface ComprobanteCSVDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla comprobante.
 * @author dalbuja
 * @version 1.0
 */
public interface ComprobantePagoServicio {
	
	public ComprobantePago editarEstadoMigrado(ComprobanteCSVDto entidad) throws ComprobantePagoValidacionException , ComprobantePagoNoEncontradoException , ComprobantePagoException;
	
	
	public ComprobantePago buscarXId(Integer id) throws ComprobantePagoNoEncontradoException , ComprobantePagoException;


	public ComprobantePagoDto buscarXNumComprobantePago(String numComprobantePago)
			throws ComprobantePagoNoEncontradoException, ComprobantePagoException;


	boolean editarMontoPagarXid(Integer cmpaId, Integer tipoFacturacion)
			throws ComprobantePagoNoEncontradoException, ComprobantePagoException;


	public ComprobantePago generarComprobanteMedicina(Integer id)
			throws ComprobantePagoNoEncontradoException, ComprobantePagoException;


	public String editarNumComprobanteXCmpaId(Integer cmpaId)
			throws ComprobantePagoNoEncontradoException, ComprobantePagoException;


	public String editarNumComprobanteXCmpaNumComprobante(String cmpaNumComprobante, Integer cmpaId)
			throws ComprobantePagoNoEncontradoException, ComprobantePagoException;
	
	/**MQ
	 * Busca Lista de  comprobantes por Id De FichaMatricula
	 * @param fcmtId - id de la ficha matricula a buscar
	 * @return Lista de  Comprobantes de pago. 
	 * 
	 */
	public List<ComprobantePago> ListarPorFcmt(Integer fcmtId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException;

	/**MQ
	 * Busca un comprobante por Id De FichaMatricula
	 * @param fcmtId - id de la ficha matricula a buscar
	 * @return Comprobante de pago. 
	 * 
	 */
	public ComprobantePago buscarPorFcmt(Integer fcmtId) throws ComprobantePagoNoEncontradoException, ComprobantePagoException;
	
	
	/**@author Daniel
	 * Busca Lista de comprobantes para el cambio de estado en record_estudiante si están pagados
	 * @return Lista de  Comprobantes de pago. 
	 * 
	 */
	public List<ComprobantePago> buscarCmpaPendientePagoNivelacionPosgrado() throws ComprobantePagoNoEncontradoException, ComprobantePagoException;


	Integer buscarMaxId() throws ComprobantePagoNoEncontradoException, ComprobantePagoException;
	
	/**
	 * Método que permite actualizar el comprobante de pago pagado.
	 * @param entidad - CompronbantePagoDto
	 * @return true si es exito.
	 * @throws ComprobantePagoValidacionException
	 * @throws ComprobantePagoNoEncontradoException
	 */
	boolean actualizarComprobantePago(ComprobantePagoDto entidad)throws ComprobantePagoValidacionException, ComprobantePagoNoEncontradoException;
	
}
