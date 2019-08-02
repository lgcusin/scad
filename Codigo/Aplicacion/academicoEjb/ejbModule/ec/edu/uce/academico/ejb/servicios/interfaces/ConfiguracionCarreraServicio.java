/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     ConfiguracionCarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla ConfiguracionCarrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 19-05-2017            David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.ConfiguracionCarreraException;
import ec.edu.uce.academico.ejb.excepciones.ConfiguracionCarreraNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;

/**
 * Interface ConfiguracionCarreraServicio
 * Interfase que define las operaciones sobre la tabla ConfiguracionCarrera.
 * @author darellano
 * @version 1.0
 */
public interface ConfiguracionCarreraServicio {
//	/**
//	 * Busca una entidad Carrera por su id
//	 * @param id - de la Carrera a buscar
//	 * @return Carrera con el id solicitado
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
//	 * @throws CarreraException - Excepcion general
//	 */
//	public Carrera buscarPorId(Integer id) throws CarreraNoEncontradoException, CarreraException;
//
//	/**
//	 * Lista todas las entidades Carrera existentes en la BD
//	 * @return lista de todas las entidades Carrera existentes en la BD
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
//	 * @throws CarreraException - Excepcion general
//	 */
//	public List<Carrera> listarTodos() throws CarreraNoEncontradoException , CarreraException;
//	
//	/**
//	 * Lista todas carreras por facultad existentes en la BD
//	 * @return Lista todas carreras por facultad existentes en la BD
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
//	 * @throws CarreraException - Excepcion general
//	 */
//	public List<Carrera> listarCarrerasXFacultad(int facultadId) throws CarreraNoEncontradoException , CarreraException;
//	
//	/**
//	 * Lista todas carreras por facultad y carrera existentes en la BD
//	 * @return Lista todas carreras por facultad y carrera existentes en la BD
//	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad y carrera 
//	 * @throws CarreraException - Excepcion general
//	 */
//	public List<Carrera> listarCarrerasXFacultad(int facultadId, int carreraId) throws CarreraNoEncontradoException , CarreraException; 
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXcrr(int crrId) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException;
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXid(int cncrId) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException;
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXcrrIdXsexoTtl(int crrId, int sexoTtl) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException;
	
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarCncrXcrrIdXsexoTtl(int crrId, int sexoTtl) throws ConfiguracionCarreraNoEncontradoException , ConfiguracionCarreraException;

	public ConfiguracionCarrera buscarXcrrXSexoHombre(int crrId)
			throws ConfiguracionCarreraNoEncontradoException, ConfiguracionCarreraException;
	/**
	 * Busca la configuracion carrera por el tipo de formacion y por la carrera sin no encontado exception
	 * @param crrId- id de la carrera a buscar
	 * @return Configuracion carrera con los parámetros ingresados
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una configuracion carrera por tipo formacion y carrera 
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarXcrrIdNuevo(int crrId) throws ConfiguracionCarreraException;

}
