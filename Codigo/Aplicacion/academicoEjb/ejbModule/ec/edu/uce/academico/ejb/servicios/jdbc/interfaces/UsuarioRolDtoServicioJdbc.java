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
   
 ARCHIVO:     UsuarioRolDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Usuario_Rol
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-FEBRERO-2016		Daniel Albuja 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;


/**
 * Interface UsuarioRolDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Usuario_Rol.
 * @author dalbuja
 * @version 1.0
 */
public interface UsuarioRolDtoServicioJdbc {
	/**
	 * Realiza la busqueda de una lista de usuario_rol por cédula, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarrea(String identificacion, int rolId, String primerApellido) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;
	
	public List<UsuarioRolJdbcDto> buscarXIdentificacion(String identificacion)throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException ;
		
	
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarreaAlterno(String identificacion,  int rolId, String primerApellido) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;
		
		
	/**
	 * Realiza la busqueda de una lista de usuario_rol por cédula, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarCarrerasXIdentificacion(String identificacion) throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraXRol(
			int rolId, int facultadId, int carreraId)throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UsuarioRolJdbcDto buscarUsuarioXCarreraXRol(int rolId, int carreraId) throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de una lista de usuario por identificacion
	 * @param String identificacion - cadena de identificacion
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UsuarioRolJdbcDto buscarUsuariosXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException ;

	public UsuarioRolJdbcDto buscarUsuariosXIdentificacionAgregarCarrera(String identificacion)
			throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;

	public List<UsuarioRolJdbcDto> buscarUsuariosActivosxCarreraXIdentificacion(String identificacion, int rolId, int crrId)
			throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;

	public List<UsuarioRolJdbcDto> buscarCarrerasXIdentificacionEstudiantes(String identificacion)
			throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;

	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraTodas(String identificacion ,int rolId, int facultadId, int carreraId)
			throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;

	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraXRolXEstado(int facultadId, int carreraId, int postgradoId, int rolId, int estadoRoflcr) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda del rol que no tienen rol flujo carrera
	 * @param rolId - rolId id del rol
	 * @return datos generales de usuario con el rol consultado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarUsuarioXrolIdXestado(int rolId, int estadoUsro) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException ;
		
	public List<UsuarioRolJdbcDto> buscarRolesXIdentificacion(String identificacion)
			throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;

	List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarreaConRol(String identificacion, int rolId,
			String primerApellido) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarRolesUsuarioTodosActivos(String identificacion) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException ;

	public List<UsuarioRolJdbcDto> buscarRolesEvaluacionDocenteXIdentificacion(String identificacion,int tipoEvalId)throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;
	
	public List<UsuarioRolJdbcDto> buscarRolesEvaluacionDocenteEvaluadoXIdentificacion(String identificacion,int rolEvaluador)throws UsuarioRolJdbcDtoException,
	UsuarioRolJdbcDtoNoEncontradoException;
	
	}
