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
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla AulaDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 15-05-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.AulaDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;

/**
 * Interface AulaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla AulaDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface AulaDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<AulaDto> listarXUsuarioXDependenciaXEdificio(int usuarioId, int dependenciaId, int edificioId) throws AulaDtoException, AulaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<AulaDto> listarXUsuarioXDependenciaXEdificioXHoraXDia(int usuarioId, int dependenciaId, int edificioId, int horaId, int diaId) throws AulaDtoException, AulaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de  aulas que pertenecen a  edificio 
	 * @param edificioId - id del edificio  a buscar
	 * @return verdero si tiene falso caso contrario
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	boolean buscarPIdEdificio(int edificioId) throws AulaDtoException, AulaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<AulaDto> listarPusrIdPdpnIdP(int usrId, int dpnId) throws AulaDtoException, AulaDtoNoEncontradoException;
	
	/**
	 * Método que permite recuperar las aulas en las que se dicta catedra de la materia solicitada.
	 * @author fgguzman
	 * @param prlId - id paralelo que pertenece la materia.
	 * @param mlcrmtId - id de la materia que busca.
	 * @return aulas asociadas a esa materia.
	 * @throws AulaDtoException
	 * @throws AulaDtoNoEncontradoException
	 */
	List<AulaDto> buscarAulasPorMateria(int prlId, int mlcrmtId) throws AulaDtoException, AulaDtoNoEncontradoException;
	
	
}
