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
   
 ARCHIVO:     UbicacionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla ubicacion.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-MARZ-2017		Dennis Collaguazo					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;


/**
 * Interface UbicacionDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla ubicacion.
 * @author dcollaguazo
 * @version 1.0
 */
public interface UbicacionDtoServicioJdbc {

	/**
	 * Realiza la busqueda de una ubicacion por id
	 * @param ubicacionId - id de la ubicacion
	 * @return Ubicacion con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las ubicaciones por carrera de la aplicacion
	 * @return Lista todas las ubicaciones de la aplicacion
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarUbicacionesXCarrera(int carreraId, List<CarreraDto> lstCarreras) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de todas las ubicaciones de la aplicacion
	 * @return Lista todas las ubicaciones de la aplicacion
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarTodos() throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de un ubicacion de pais por id
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarPaisXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una ubicacion de provincia por id de pais
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaProvinciaXPais(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una ubicacion de canton por id de provincia 
	 * @param ubicacionId - id del ubicacion de provincia 
	 * @return Ubicacion canton con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaCatonXProvincia(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las ubicaciones por carrera, vigencia, modalidad, titulo, tipo sede, tipo formacion
	 * @param tiseId - id de tipo sede
	 * @param tifrId - id de tipo de formacion 
	 * @param crrId - id de carrera
	 * @param vgnId - id de vigencia
	 * @param mdlId - id de modalidad
	 * @param ttlId - id de titulo
	 * @return Ubicaciones con los id solicitados 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaUbicacionXTiseXTifrXCrrXVgnXMdlXXttl(int tiseId, int tifrId, int crrId, int vgnId, int mdlId, int ttlId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un ubicacion de pais por id
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarPadreXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las ubicaciones por jerarquia
	 * @return Lista todos las ubicaciones de la aplicacion por jerarquia
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarXjerarquia(int idJerarquia) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de una ubicacion de parroquia por id del canton
	 * @param ubicacionId - id del ubicacion del canton
	 * @return Ubicacion parroquia con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaParroquiasXCanton(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException;
	
}
