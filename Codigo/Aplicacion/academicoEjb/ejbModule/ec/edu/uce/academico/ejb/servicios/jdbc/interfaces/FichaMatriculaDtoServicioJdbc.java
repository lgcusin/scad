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

 ARCHIVO:     FichaMatriculaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla matricula.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 04-03-2017		David Arellano					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Interface FichaMatriculaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla matricula.
 * @author darellano
 * @version 1.0
 */

public interface FichaMatriculaDtoServicioJdbc {
	
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraXsuficiencia(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;

	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarrera(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**MQ:  7 feb 2019
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraParaTercerasMatriculas(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraTodos(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**
	 * Realiza la busqueda de las matriculas de estudiantes en pregrado y suficiencia en informática 
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarMatriculaPregradoYSufInformaticaXPeriodoXidentificacionXcarrera(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;

	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraNivelacion(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;

	
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico activo, identifiacion de la persona, por apellido, por carrera
	 * @param periodoId - periodoId  String del período académico a buscar
	 * @param listaCarreras - listaCarreras lista de carreras a buscar, si se selecciona todas
	 * @param carreraId - carreraId id de la carrera a buscar
	 * @param personaIdentificacion - personaIdentificacion número de identificación de la persona a buscar
	 * @param personaApellido - personaApellido apellido paterno de la persona a buscar
	 * @return retorna la lista de ficha matriculas - lista de estudiantes matriculados
	 * @throws FichaMatriculaException - FichaMatriculaException excepción general lanzada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXapellidoXidentificacionXcarrera(int periodoId, List<CarreraDto> listaCarreras, int carreraId, String personaIdentificacion, String personaApellido) throws FichaMatriculaException;
	
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico activo, identifiacion de la persona, por apellido, por carrera
	 * @param periodoId - periodoId  String del período académico a buscar
	 * @param listaCarreras - listaCarreras lista de carreras a buscar, si se selecciona todas
	 * @param carreraId - carreraId id de la carrera a buscar
	 * @param personaIdentificacion - personaIdentificacion número de identificación de la persona a buscar
	 * @param personaApellido - personaApellido apellido paterno de la persona a buscar
	 * @return retorna la lista de ficha matriculas - lista de estudiantes matriculados
	 * @throws FichaMatriculaException - FichaMatriculaException excepción general lanzada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoActivoEnCierreXapellidoXidentificacionXcarrera(int periodoId, List<CarreraDto> listaCarreras, int carreraId, String personaIdentificacion, String personaApellido) throws FichaMatriculaException;

	
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona para posgrado
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraPosgrado(int periodoId, String personaIdentificacion,
			int carreraId) throws FichaMatriculaException;	
	
	/**
	 * Realiza la busqueda de un FichaMatricula del estudiante  del periodo activo, carreras de pregrado, periodo de tipo pregrado por usuarioId y por la carrera 
	 * @param personaId - id de la persona
	 * @param carreraId - id de la carrera 
	 * @param periodoId - id del periodo
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXidPersonaXcarrera(int personaId, int carreraId, int periodoId) throws FichaMatriculaException;
	
	
	/**
	 * Realiza la busqueda de un FichaMatricula del estudiante  del periodo activo, carreras de pregrado, periodo de tipo pregrado por usuarioId y por la carrera 
	 * @param usuarioId - Usuario a buscar
	 * @param carreraID - Carrera a buscar
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXidPersonaXcarreraXtipo(int personaId, int carreraId, int tipoPeriodo) throws FichaMatriculaException;
	
	
	/**MQ
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarrera(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**MQ
	 * Realiza la busqueda de un FichaMatricula de posgrado por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarreraPosgrado(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona,OJO: SIN TOMAR CARRERA DE FICHA INSCRIPCION
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @param estado - estado del detalle matricula
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	List<FichaMatriculaDto> ListarXPeriodoXidentificacionXcarreraXEstado(int periodoId, String personaIdentificacion, int carreraId, int estado) throws FichaMatriculaException;

	/**
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @author Arturo Villafuerte - ajvillafuerte 
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraFull(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**
	 * Realiza la busqueda de las FichasMatricula del estudiante , carreras de pregrado, periodo de tipo pregrado por personaId y por la carrera 
	 * @param personaId - Usuario a buscar
	 * @param carreraID - Carrera a buscar
	 * @return Lista de MatriculaDto que corresponden a la persona y carrera
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	List<FichaMatriculaDto> ListarTodasXidPersonaXcarrera(int personaId, int carreraId) throws FichaMatriculaException;

	List<FichaMatriculaDto> ListarXidPersonaXcarreraXPracEnCierre(int personaId, int carreraId) throws FichaMatriculaException;
	
	/**
	 * Método que permite recuperar los atributos de la matricula de un estudiante.
	 * @author fgguzman
	 * @param fcmtId - id de la ficha matricula
	 * @return ficha requerida.
	 * @throws FichaMatriculaException
	 */
	FichaMatriculaDto buscarFichaMatriculaDto(int fcmtId) throws FichaMatriculaException;

	/**
	 * Método que permite buscar las fichas matricula que tenga el estudiante en un periodo academico.
	 * @author fgguzman
	 * @param prsIdentificacion - cedula o pasaporte
	 * @param periodoId - id del periodo academico
	 * @return fichas matricula , nivel de ubicacion.
	 * @throws FichaMatriculaNoEncontradoException
	 * @throws FichaMatriculaException
	 */
	List<FichaMatriculaDto> buscarFichasMatricula(String prsIdentificacion, int periodoId) throws FichaMatriculaNoEncontradoException, FichaMatriculaException;
	
	/**MQ
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona, devuelve null sino existe
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> listarXPeriodoXidentificacionXcarreraNull(int periodoId, String personaIdentificacion, int carreraId) throws FichaMatriculaException;
	
	/**
	 * MQ: 12 dic 2008
	 *  Realiza la busqueda de un FichaMatricula por período académico activo, identifiacion de la persona, por apellido, por carrera de los estudiantes 
	 * que han solicitado retiro fortuito.
	 * @param periodoId - periodoId  String del período académico a buscar
	 * @param listaCarreras - listaCarreras lista de carreras a buscar, si se selecciona todas
	 * @param carreraId - carreraId id de la carrera a buscar
	 * @param personaIdentificacion - personaIdentificacion número de identificación de la persona a buscar
	 * @param personaApellido - personaApellido apellido paterno de la persona a buscar
	 * @return retorna la lista de ficha matriculas - lista de estudiantes matriculados
	 * @throws FichaMatriculaException - FichaMatriculaException excepción general lanzada
	 */
	public List<FichaMatriculaDto> buscarSolicitudRetiroFortuitoXPeriodoXapellidoXidentificacionXcarrera(int periodoId, List<CarreraDto> listaCarreras, int carreraId, String personaIdentificacion, String personaApellido, Integer estadoSolicitud) throws FichaMatriculaException;

	/**MQ: 17 ene 2019
	 * Realiza la busqueda de un FichaMatricula por período académico y por la identifiacion de la persona
	 * @param periodo - String del período académico a buscar
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - id de la carrera a buscar
	 * @return Lista de MatriculaDto que corresponden al período y a la persona buscada
	 * @throws FichaMatriculaException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraTodosPregrado(int periodoId, String personaIdentificacion, int carreraId, List<PeriodoAcademico> listaPeriodo) throws FichaMatriculaException;

	List<FichaMatriculaDto> buscarXPeriodoXidentificacionXcarreraFullNivelacion(int periodoId,
			String personaIdentificacion, int carreraId) throws FichaMatriculaException;


	List<FichaMatriculaDto> buscarFichaMatriculaXPeriodoXIdentificacion(int periodoId, String personaIdentificacion)
			throws FichaMatriculaException;



}
