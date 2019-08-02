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

 ARCHIVO:     FichaInscripcionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla FichaInscripcion.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 24-03-2017			David Arellano					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;


/**
 * Interface FichaInscripcionDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla FichaInscripcion.
 * @author darellano
 * @version 1.0
 */

public interface FichaInscripcionDtoServicioJdbc {

	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarXidentificacionXestado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarFcinPregradoXidentificacionXestado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFcinXidentificacionXestado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	

	/**
	 * Realiza la busqueda de un FichaMatriculaDto por la identifiacion de la persona , el estado de la ficha inscripcion y el rol de estudiante posgrado
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @param rolId - id del rol de posgrado
	 * @return Lista de FichaMatriculaDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> buscarEstudiantePosgradoPidentificacionPfcinEstado(String prsIdentificacion, int fcinEstado) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;

	public List<FichaInscripcionDto> buscarXidentificacionXestadoXMatriculado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public FichaInscripcionDto buscarFcinXidentificacionXcarrera(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;

	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXidentificacionXcarrera(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException;
	
	/**
	 * Método que realiza la búsqueda de una Ficha Estudiante por la  carrea e identifiacion de la persona.
	 * @author ajvillafuerte v1
	 * @author fgguzman v2
	 * @param prsIdentificacion - cedula o pasaporte del estudiante
	 * @param carreraId - id de la carrera 
	 * @return fichas asociadas
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException 
	 */
	public List<FichaInscripcionDto> buscarFichaInscripcionPorCarrera(String prsIdentificacion, int carreraId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las Fichas Inscripcion que dispone un estudiante.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @param estados - constantes FCIN_ESTADO 
	 * @return fichas inscripcion encontradas
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException
	 */
	public List<FichaInscripcionDto> buscarFichasInscripcion(String identificacion, Integer[] estados) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las Fichas Inscripcion que dispone un estudiante por tipo ingreso.
	 * @author fgguzman
	 * @param estados - constantes FCIN_ESTADO
	 * @param tiposIngreso - constantes FCIN_TIPO_INGRESO  
	 * @param pracId - periodo academico
	 * @return fichas inscripcion encontradas
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException
	 */
	public List<FichaInscripcionDto> buscarFichasInscripcion(Integer[] estados, Integer[] tiposIngreso, int pracId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de las Fichas Inscripcion que dispone un estudiante segun el Tipo de Carrera.
	 * @author fgguzman
	 * @param prsIdentificacion - cedula o pasaporte del estudiante.
	 * @param crrTipo - tipo de Carrera. 
	 * @return fichas inscripcion en cualquier estado.
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException
	 */
	public List<FichaInscripcionDto> buscarFichasInscripcion(String prsIdentificacion, int crrTipo) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las Fichas Inscripcion que dispone un estudiante por tipo ingreso.
	 * @author fgguzman
	 * @param param - identificacion - primer apellido.
	 * @param estados - constantes FCIN_ESTADO
	 * @param tiposIngreso - constantes FCIN_TIPO_INGRESO
	 * @param tipoBusqueda - 0=cedula - 1=primer apellido
	 * @param pracId - periodo academico    
	 * @return fichas inscripcion encontradas
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException
	 */
	public List<FichaInscripcionDto> buscarFichasInscripcion(String param, Integer[] estados, Integer[] tiposIngreso, int tipoBusqueda, int pracId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoId - id del estado de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaInscripcionDto> listarFcinXIdentificacionXEstado(String personaIdentificacion, int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;

	
	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXidentificacionXcarreraNueva(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException;
	
	/**
	 * Realiza la busqueda de una Lista de FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
     * @param estadoId - id estao  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaInscripcionDto> buscarXidentificacionXcarreraXEstado(String personaIdentificacion, int crrId, int estadoId) throws FichaInscripcionDtoException;
	
	
	/**
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y el estado de la ficha inscripcion
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXidentificacionXcarreraxPeriodo(String personaIdentificacion, int crrId, int pracId) throws FichaInscripcionDtoException;
	
	/**MQ
	 * Realiza la busqueda de un FichaInscripcionDto por la identifiacion de la persona , la carrera y FcinId Mayor
	 * @param personaIdentificacion - String de la indentificacion de la persona a buscar
	 * @param carreraId - id de la carrera  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la persona buscada, por carrera y al estado de la ficha inscripcion
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaInscripcionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FichaInscripcionDto buscarFichaInscripcionXIdentificacionXCarreraXIdMayor(String personaIdentificacion, int crrId) throws FichaInscripcionDtoException;

	List<FichaInscripcionDto> listarFcinXRetornoNivelacion() throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;

	List<FichaInscripcionDto> listarFcinParaCorrecionCncr() throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;


	void corregirCncrEnFcin(FichaInscripcionDto item);
	
	/**
	 * Realiza la busqueda de las Fichas Inscripcion que pertenescan a un rol de estudiante nivelación y tipo nivelación.
	 * @author Daniel Albuja
	 * @param  UsuarioRol - id de usuarioRol.
	 * @param  nivelacion - Tipo Nivelación
	 * @return fichas inscripcion encontradas
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException
	 */
	public List<FichaInscripcionDto> buscarFichasInscripcionRolEstudianteTipoNivelacion(Integer UsuarioRol, Integer nivelacion) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;

	void correcionFichaErrorCargaNivelacion() throws FichaInscripcionDtoException;

	FichaInscripcionDto buscarFichaInscripcionNivelacionXidentificacion(String personaIdentificacion)
			throws FichaInscripcionDtoException;

	void actualizarCarreraAreaXFcinId(Integer fcinId, Integer crrId, Integer areaId)
			throws FichaInscripcionDtoException;

	List<FichaInscripcionDto> buscarXidentificacionXestadoXMatriculadoPosgrado(String personaIdentificacion,
			int estadoId) throws FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;
	
	
	/**MQ
	 * Realiza la busqueda de una Lista de FichaInscripcionDto por nota de corteId
     * @param notaCorteID - id nota de corte  de la fichaInscripcion a buscar
	 * @return Lista de FichaInscripcionDto que corresponden a la nota de corte
	 * @throws FichaInscripcionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaInscripcionDto> listarXnotaCorte(int notaCorteId) throws FichaInscripcionDtoException;
	
}
