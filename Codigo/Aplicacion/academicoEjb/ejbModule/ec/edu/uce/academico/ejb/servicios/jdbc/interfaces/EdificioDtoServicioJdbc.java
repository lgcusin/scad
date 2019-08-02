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

 ARCHIVO:     AulaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Aula.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 15-05-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.dtos.EdificioDto;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoException;
import ec.edu.uce.academico.ejb.excepciones.EdificioDtoNoEncontradoException;




/**
 * Interface AulaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Aula.
 * @author lmquishpei
 * @version 1.0
 */
public interface EdificioDtoServicioJdbc {

	/**
	 * Realiza la busqueda de los edificios por la dependencia
	 * @param usuarioId - id de la dependencia
	 * @return Lista de Edificios  por dependencia 
	 * @throws EdificioDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw EdificioDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<EdificioDto> listarXDependencia(int dependenciaId, List<DependenciaDto> listDependencia) throws EdificioDtoException, EdificioDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de los edificios por la dependencia Id
	 * @param dependenciaId - id de la dependencia
	 * @throws EdificioDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw EdificioDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<EdificioDto> listarEdificiosPdpnId(int dependenciaId) throws EdificioDtoException, EdificioDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de los edificios por la dependencia
	 * @param usuarioId - id de la dependencia
	 * @param listDependencia - listDependencia de dependencias
	 * @return Lista de Edificios  por dependencia 
	 * @throws EdificioDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw EdificioDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<EdificioDto> listarXDependenciaXEstadoActivo(int dependenciaId, List<DependenciaDto> listDependencia) throws EdificioDtoException, EdificioDtoNoEncontradoException;
}
