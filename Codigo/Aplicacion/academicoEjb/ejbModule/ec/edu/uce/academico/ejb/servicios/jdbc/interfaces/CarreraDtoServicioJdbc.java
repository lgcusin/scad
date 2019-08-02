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

 ARCHIVO:     CarreraDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla carrera.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 11-MARZ-2017		Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DependenciaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Dependencia;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;


/**
 * Interface CarreraDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla carrera.
 * @author dcollaguazo
 * @version 1.0
 */
public interface CarreraDtoServicioJdbc {

	/**
	  * Método que permite cargar las carreras segun nivel academico y periodo academico que impartio el docente.
	  * @author fgguzman
	  * @param identificacion - cedula o pasaporte.
	  * @param nivelAcademico - pregrado , posgrado, suficiencias, nivelacion
	  * @param periodo - id del periodo academico
	  * @return periodos academicos.
	  * @throws PeriodoAcademicoDtoJdbcException
	  * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	  */
	List<CarreraDto> buscarCarrerasPorDocenteNivelAcademicoPeriodoAcademico(String identificacion, int nivelAcademico, int periodo) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException,  PeriodoAcademicoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public CarreraDto buscarXId(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	


	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarTodos() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion, sin no encontrado exception
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarCarrerasTodas() throws CarreraDtoJdbcException;
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion por la facultad
	 * @return Lista todas las carreras de la aplicacion por la facultad solicitada
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXfacultad(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	

	public List<CarreraDto> listarXfacultades(List<Dependencia> listaDependencia) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	/**
	 * Realiza la busqueda de facultades id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las facultades por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFl(int idUsuario, String descRol, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXDependencia(int idUsuario, int descRol, int estadoRolFlujo, int idDependencia) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las carreras a las que el estudiante esta matriculado
	 * @return Lista todas las carreras a las que el estudiante se inscribió
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXRolFlujoCarrera(int usuarioId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @param idPeriodoAcademico - id del periodo academico
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademico(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoEnCierre(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	

	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoXFacultad(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico, List<DependenciaDto> listDependencia) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
		
	/**
	 * Realiza la busqueda de las carreras por id de usuario y el periodo academico
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @return Lista de las carreras por id de usuario, y el periodo academico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	
	public List<CarreraDto> listarXIdUsuarioXPeriodoAcademico(int idUsuario,  int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo y el periodo academico activo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivo(int idUsuario, String descRol, int estadoRolFlujo, List<RolFlujoCarrera> listCarreras) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	
	
	
	/**
	 * Realiza la busqueda de las carreras a las cuales tiene acceso el Usuario y por la Facultad
	 * @param usuarioId - id del usuario , id de la facultad
	 * @return Lista de Carreras  por el usuario que accede y la dependencia seleccionada 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<CarreraDto> listarXUsuarioXDependencia(int usuarioId, int dependenciaId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;


	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrr(int idUsuario, int rolId, int estadoRolFlujo)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;


	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrXTipo(int idUsuario, int rolId, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, id de Rol, Estado rolFlujo
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, id de Rol, Estado rolFlujo 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrXDependencia(int idUsuario, int rolId, int estadoRolFlujo, int dpnId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	CarreraDto buscarXIdPosgrado(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarTodosPosgrado() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXfacultadPosgrado(int facultadId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlPosgrado(int idUsuario, String descRol, int estadoRolFlujo)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXRolFlujoCarreraPosgrado(int usuarioRolId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXPeriodoAcademicoPosgrado(int idUsuario, int idPeriodoAcademico)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPosgrado(int idUsuario, String descRol,
			int estadoRolFlujo, int idPeriodoAcademico)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoPosgrado(int idUsuario, String descRol,
			int estadoRolFlujo, List<RolFlujoCarrera> listCarreras)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXUsuarioXDependenciaPosgrado(int usuarioId, int dependenciaId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrPosgrado(int idUsuario, int rolId, int estadoRolFlujo)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de las materias por perido_academico, tipo de carrera, dependencia
	 * @param pracId - id del perido_academico
	 * @param tipoCrr - tipo de carrera 0 grado 1 posgrado
	 * @param dpnId -  id de la facultad
	 * @return Lista de materias
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<CarreraDto> listarMateriasXPracIdXtipoCrrXDpnId(Integer pracId,Integer tipoCrr,Integer dpnId, Integer crrId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las materias por perido_academico, tipo de carrera, dependencia
	 * @param pracId - id del perido_academico
	 * @param tipoCrr - tipo de carrera 0 grado 1 posgrado
	 * @param dpnId -  id de la facultad
	 * @return Lista de materias
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throw CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<CarreraDto> listarMateriasCarrera() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	/**
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico en Pregrado
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoPregrado(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	CarreraDto buscarXIdNivelacion(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarTodosNivelacion() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXfacultadNivelacion(int facultadId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlNivelacion(int idUsuario, String descRol, int estadoRolFlujo)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXRolFlujoCarreraNivelacion(int usuarioRolId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXPeriodoAcademicoNivelacion(int idUsuario, int idPeriodoAcademico)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoNivelacion(int idUsuario, String descRol,
			int estadoRolFlujo, int idPeriodoAcademico)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPeriodoAcademicoActivoNivelacion(int idUsuario,
			String descRol, int estadoRolFlujo, List<RolFlujoCarrera> listCarreras)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXUsuarioXDependenciaNivelacion(int usuarioId, int dependenciaId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrNivelacion(int idUsuario, int rolId, int estadoRolFlujo)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un estudiante por su identificacion y su carrera por id
	 * @param carreraId - id del carrera
	 * @param identificacion - id del identificaion de la persona
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public CarreraDto buscarCarreraXIdentificacionXcrrId(String identificacion,int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	
	/**MQ:
	 * Realiza la busqueda de todas las carreras de la aplicacion por carrera Proceso
	 * @return Lista todas las carreras de la aplicacion por carrera Proceso
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarTodosXCrrProceso(int crrProceso) throws CarreraDtoJdbcException;



	List<CarreraDto> listarAreasXCrrId(Integer crrId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Método que permite recuperar las carreras asignadas a un area.
	 * @author fgguzman
	 * @param areaId - id del area (carreraId)
	 * @return carreras asociadas al area
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	List<CarreraDto> buscarCarrerasPorArea(int areaId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**MQ
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y el periodo academico Pregrado y Posgrado
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param idPeriodoAcademico - id del periodo academico
	 * @param idPeriodoAcademico - descripcion del rol
	 * @param idPeriodoAcademico - estado del rol flujo
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y perido académico
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXPrac(int idUsuario, Integer rolId, int estadoRolFlujo, int idPeriodoAcademico) throws CarreraDtoJdbcException;
	
	/**MQ
	 * Realiza la busqueda de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y lista de periodos Posgrado
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param rolId - id del rol
	 * @param estadoRolFlujo - estado del rol flujo
	 * @param listaPeriodosActivos - lista de periodos activos de posgrado
	 * @return Lista de las carreras por id de usuario, Descripcio Rol, Estado rolFlujo y lista de periodos activos
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFlXListaPrac(int idUsuario, Integer rolId, int estadoRolFlujo, List<PeriodoAcademico> listaPeriodosActivos) throws CarreraDtoJdbcException;

	/**
	 * Realiza la busqueda de todas las carreras con sus dependencias en funcion al tipo de carrera.
	 * @author fgguzman
	 * @param crrTipo - {0-PREGRADO,1-POSGRADO,2-NIVELACIÓN,3-SUFICIENCIA}
	 * @return carreras que pertenencen al tipo. 
	 * @throws CarreraDtoJdbcException 
	 * @throws CarreraDtoJdbcNoEncontradoException 
	 */
	List<CarreraDto> buscarCarreraDependencia(int crrTipo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	List<CarreraDto> buscarXPracIdPosgrado(int pracId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Método que permite buscar carreras por usuario rol ID.
	 * @author fgguzman
	 * @param usroId - id del usuario rol
	 * @return carreras.
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	List<CarreraDto> buscarCarreras(int usroId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	/**
	 * Método que permite listar las carreras de una facultad segun el tipo de carreras.
	 * @param facultadId - id de la Dependencia.
	 * @param carreraTipo - tipo de Carreras {Nivelacion, Pregrado, Postgrado}
	 * @return carreras vinculadas a la dependencia.
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	List<CarreraDto> buscarCarrerasPorTipoFacultad(int facultadId, int carreraTipo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	
	/**
	 * Método que busca las carreras de docentes asignados carga horaria
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @return - retorna las carreras de docentes asignados carga horaria
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto>  listarXDocentesHorasClaseXPeriodo(int pracId) throws DetallePuestoDtoJdbcException ;
	
	/**
	 * Método que busca las carreras de docentes asignados carga horaria
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @return - retorna las carreras de docentes asignados carga horaria
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto>  listarXDocentesHorasClaseXPeriodoXDependencia(int pracId, int dpnId) throws DetallePuestoDtoJdbcException ;

	List<CarreraDto> listarXIdUsuarioXIdRolXEstadoRolFlCrrNivelacionRediseno(int idUsuario, int rolId,
			int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> listarTodosRediseno() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;



	List<CarreraDto> buscarTodasPosgrado() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	
	/**
	 * MQ: 11 jul 2019
	 * Realiza la busqueda de todas las carreras activas de rediseño
	 * @return Lista todas las carreras activas de rediseño
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarCarreraActivasRediseno() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * MQ: 17 jul 2019
	 * Realiza la busqueda de todas las carreras de pregrado
	 * @return Lista todas las carreras activas de rediseño
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarCarrerasPregrado() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
}
