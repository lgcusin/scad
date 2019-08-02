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

 ARCHIVO:     CarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DependenciaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;

/**
 * Interface CarreraServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author dcollaguazo
 * @version 1.0
 */
public interface CarreraServicio {
	/**
	 * Busca una entidad Carrera por su id
	 * @param id - de la Carrera a buscar
	 * @return Carrera con el id solicitado
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CarreraException - Excepcion general
	 */
	public Carrera buscarPorId(Integer id) throws CarreraNoEncontradoException, CarreraException;

	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws CarreraException - Excepcion general
	 */
	public List<Carrera> listarTodos() throws CarreraNoEncontradoException , CarreraException;
	
	/**
	 * Lista todas carreras por facultad existentes en la BD
	 * @return Lista todas carreras por facultad existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
	 * @throws CarreraException - Excepcion general
	 */
	public List<Carrera> listarCarrerasXFacultad(int facultadId) throws CarreraNoEncontradoException , CarreraException;
	
	/**
	 * Lista todas carreras por facultad existentes en la BD
	 * @return Lista todas carreras por facultad existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad 
	 * @throws CarreraException - Excepcion general
	 */
	public List<Carrera> listarCarrerasYSuficienciaXFacultad(int facultadId) throws CarreraNoEncontradoException , CarreraException ;
	
	/**
	 * Lista todas carreras por facultad y carrera existentes en la BD
	 * @return Lista todas carreras por facultad y carrera existentes en la BD
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una carreras por facultad y carrera 
	 * @throws CarreraException - Excepcion general
	 */
	public List<Carrera> listarCarrerasXFacultad(int facultadId, int carreraId) throws CarreraNoEncontradoException , CarreraException;

	public List<Carrera> listarCarrerasXFacultadPosgrado(int facultadId, int carreraId)
			throws CarreraNoEncontradoException, CarreraException;

	public List<Carrera> listarCarrerasXFacultadPosgrado(int facultadId) throws CarreraNoEncontradoException, CarreraException;

	public List<Carrera> listarTodosPosgrado() throws CarreraNoEncontradoException, CarreraException;

	public List<Carrera> listarCarrerasXFacultadNivelacion(int facultadId, int carreraId)
			throws CarreraNoEncontradoException, CarreraException;

	public Carrera buscarFacultadXCarrera(int crrId) throws DependenciaNoEncontradoException;
	
	/**MQ
	 * Busca una entidad Carrera por su id sin excepciones
	 * @param id - de la Carrera a buscar
	 * @return Carrera con el id solicitado
	 */
	public Carrera buscarPorIdSinException(Integer id);

	/**
	 * Carrera por espeCodigo
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @return Carrera por espeCodigo
	 */
	Carrera buscarCarreraXEspeCodigo(int espeCodigo) throws CarreraNoEncontradoException, CarreraException;
	
	/**
	 * Método que permite buscar todas las carreras segun el tipo.
	 * @author fgguzman
	 * @param facultadId - id de la Dependencia
	 * @param crrTipo - nivelacion, pregrado, posgrado, suficiencias
	 * @return carreras asociadas al tipo
	 * @throws CarreraNoEncontradoException
	 * @throws CarreraException
	 */
	List<Carrera> buscarCarreras(int facultadId, int crrTipo) throws CarreraNoEncontradoException, CarreraException;

	/**
	 * Método que permite recuperar la carrera por el id del grupo
	 * @author fgguzman
	 * @param grupoId - id del Grupo
	 * @return Carrera
	 * @throws CarreraNoEncontradoException
	 * @throws CarreraException
	 */
	Carrera buscarCarreraPorGrupoId(int grupoId) throws CarreraNoEncontradoException, CarreraException;

	/**
	 * Método que permite listar las carreras a cargo de un usuario segun el rol y estado del usro.
	 * @author fgguzman
	 * @param usuarioId - id del usuario
	 * @param rolId - id del Rol
	 * @param usroEstado - estado del usuario rol
	 * @return carrerras segun parametros.
	 */
	List<Carrera> buscarCarrerasPorUsuarioRol(int usuarioId, int rolId, int usroEstado) throws CarreraNoEncontradoException, CarreraException;
	
	/**
	 * Lista todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 * @return lista de todas las dependencias de jerarquia facultades existentes en la BD dependiendo el tipo de carrera
	 */
	public List<Carrera> listarCarrerasActivasEvaluacionXTipoCarrera(int dependenciaId, int tipoCarrera, int periodoId) throws CarreraNoEncontradoException;

	/**
	 * Método que permite listar las carreras a cargo de un usuario segun el rol y estado del usro.
	 * @author fgguzman
	 * @param usuarioId - id del usuario
	 * @param rolId - id del Rol
	 * @param usroEstado - estado del usuario rol
	 * @return carrerras segun parametros.
	 */
	public Carrera buscarAreaXCarrera(Integer crrId) throws CarreraNoEncontradoException, CarreraException;
	
}
