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

 ARCHIVO:     SolicitudTerceraMatriculaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla SolicitudTerceraMatriculaDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 07-02-2018			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaDtoNoEncontradoException;


/**
 * Interface SolicitudTerceraMatriculaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla SolicitudTerceraMatriculaDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface SolicitudTerceraMatriculaDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas las solicitudes que pertenecen a cada periodo, carrera, buscar por apellido, identificacion y estado de la solictud.
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitud(int pracId, int crrId, String personaApellido, String personaIdentificacion, int estadoSolicitud) throws SolicitudTerceraMatriculaDtoException;
	
	
	/**
	 * Realiza la busqueda de todas las solicitudes que pertenecen a cada periodo, carrera, buscar por apellido, identificacion y estado de la solicitud  para Estudiantes SAU.
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXPrimerApellidoXIdentidadXEstadoSolicitudSIIUSAU(int pracId, int crrId, String personaApellido, String personaIdentificacion, int estadoSltrmt) throws SolicitudTerceraMatriculaDtoException;


	/**SIIU
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud, 
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera, estado de la solicitud y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitud(int pracId, int crrId, int fcesId , int estadoSolicitud, int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException;
	
	
	
	/**SAU
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SAU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param estadoSolicitud- busca por el estado de la solicitud
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera, estado de la solicitud y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXFichaEstudianteXEstadoSolicitudSIIUSAU(int pracId, int crrId, int fcesId , int estadoSltrmt, int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException;
	
	public List<SolicitudTerceraMatriculaDto> listarxIdentificacionxCarrera(String identificacion, int crrId) throws SolicitudTerceraMatriculaDtoException;
	
	public List<SolicitudTerceraMatriculaDto> listarxIdentificacionxCarreraSolicitudApelacion(String identificacion, int crrId, int tipo) throws SolicitudTerceraMatriculaDtoException;

	
	
	/**SIIUSAU Reporte de materias aprobadas negadas
	 * Realiza la busqueda de los estudiantes con solicitudes por carrera, por apellido, identificacion y estado aprobadas-negadas .
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXPrimerApellidoXIdentidadXAprobasNegadas(int pracId, int crrId, String personaApellido, String personaIdentificacion) throws SolicitudTerceraMatriculaDtoException;
	
	
	/**Lista de materias para reimpresion de  reporte de solicitudes aprobadas y negadas .
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SAU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera y ficha estudiante tipo solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(int pracId, int crrId, int fcesId , int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException;
	
	
	/**LISTA de estudiantes para reimpresion de Reporte de apelaciones aprobadas negadas
	 * Realiza la busqueda de los estudiantes con solicitudes por carrera, por apellido, identificacion y estado aprobadas-negadas .
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param personaApellido - buscar por apellido
	 * @param personaIdentificacion - buscar por identificacion
	 * @return Lista de las solicitudes por periodo, carrera, apellido, identificacion y estado de la solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarApelacionesXPeriodoXCarreraXPrimerApellidoXIdentidadXAprobasNegadas(int pracId, int crrId, String personaApellido, String personaIdentificacion) throws SolicitudTerceraMatriculaDtoException;
	
	/**Lista de materias para reimpresion de  reporte de apelación aprobadas y negadas .
	 * Realiza la busqueda de todas las materias solicitadas tercera matricula de un periodo, carrera, fichaEstudiante, estado de la solictud estudiante SAU
	 * @param pracId - id del periodo
	 * @param crrId - id de la carrera
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las materias por periodo, carrera y ficha estudiante tipo solicitud
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarApelacionesXPeriodoXCarreraXFichaEstudianteXAprobadasNegadas(int pracId, int crrId, int fcesId , int tipoSolicitud) throws SolicitudTerceraMatriculaDtoException;
	
	
	/**
	 * Reimprimir solicitud, apelaciones tercera matricula , Ver solicitudes y apelaciones anteriores
	 */
	public List<SolicitudTerceraMatriculaDto> listarxIdentificacionxCarreraSolicitudApelacionAnteriores(String identificacion, int crrId, int tipo) throws SolicitudTerceraMatriculaDtoException;
	
	
	/**MQ
	 * Lista las solicitudes de tercera matrícula por materia y ficha estudiante
	 * Realiza la busqueda de todas las solicitadas tercera matricula por fichaEstudiante y materia
	 * @param mtrId - id del periodo
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las solicitudes de tercera matrícula por materia y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarSolicitudesxFcesIdXMtrId(int mtrId,  int fcesId) throws SolicitudTerceraMatriculaDtoException;
	
	
	/**MQ
	 * Lista las solicitudes de tercera matrícula por materia y ficha estudiante y estao del registro
	 * Realiza la busqueda de todas las solicitadas tercera matricula por fichaEstudiante y materia
	 * @param mtrId - id del periodo
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las solicitudes de tercera matrícula por materia y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws SolicitudTerceraMatriculaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<SolicitudTerceraMatriculaDto> listarSolicitudesxFcesIdXMtrIdxEstadoRegistro(int mtrId,  int fcesId, int estadoRegistro) throws SolicitudTerceraMatriculaDtoException;
	
	/**MQ
	 * Lista las solicitudes de tercera matrícula por ficha estudiante
	 * Realiza la busqueda de todas las solicitadas tercera matricula por fichaEstudiante 
	 * @param fcesId - id de la ficha del estudiante a buscar
	 * @return Lista de las solicitudes de tercera matrícula por materia y ficha estudiante
	 * @throws SolicitudTerceraMatriculaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<SolicitudTerceraMatriculaDto> listarSolicitudesxFcesId(int fcesId) throws SolicitudTerceraMatriculaDtoException;

}
