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

 ARCHIVO:     MallaPeriodoDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mallaPeriodo.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 31-AGOSTO-2017		Vinicio Rosales 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MallaPeriodoDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoNoEncontradoException;


/**
 * Interface MallaPeriodoDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla mallaPeriodo.
 * @author jvrosales
 * @version 1.0
 */
public interface MallaPeriodoDtoServicioJdbc {
	/**
	 * Realiza la busqueda de todas las mallas_periodo
	 * @return Lista todas las mallas_periodo por carrera de la aplicacion
	 * @throws MallaPeridoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaPeriodoDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 * @throws MallaCurricularDtoException 
	 */
	List<MallaPeriodoDto> listarMallasPeriodo() throws MallaPeriodoDtoException, MallaPeriodoDtoNoEncontradoException;
	
	List<MallaPeriodoDto> listarMallasPeriodoXPeriodo(int pracId) throws MallaPeriodoDtoException, MallaPeriodoDtoNoEncontradoException;
	
	/**
	 * Método que permite recuperar las mallas asociadas a un tipo de periodo academico.
	 * @author fgguzman
	 * @param pracTipo - tipo de periodo academico.
	 * @param mlprEstado - estado de las mallas periodo.
	 * @return mallas por periodo.
	 * @throws MallaPeriodoException
	 * @throws MallaPeriodoNoEncontradoException
	 */
	List<MallaPeriodoDto>  buscarMallasPorTipoPeriodo(int pracTipo, int mlprEstado) throws MallaPeriodoException, MallaPeriodoNoEncontradoException;
}
