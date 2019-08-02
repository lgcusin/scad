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

 ARCHIVO:     DocenteDtoServicioJdbc.java	  
 DESCRIPCION: Interface para los servicios de DTO del docente. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 27-ABRI-2017 		  Dennis Collaguazo			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoException;
import ec.edu.uce.academico.ejb.excepciones.DocenteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface DocenteDtoServicioJdbc.
 * Para los servicios del DTO de docente.
 * @author dcollaguazo.
 * @version 1.0
 */
public interface DocenteDtoServicioJdbc {

	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPeriodo(int idUsuario, int idCarrera, int docente, int idPeriodoAcademico) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPeriodoActivo(int idUsuario, int idCarrera, int docente) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocente(String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca los niveles del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesDocente(int crrId, String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las masterias del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param crrId - id de la carrera del docente da clase a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarMateriasDocente(int crrId, int nvlId,String identificacionId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo y materia
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<DocenteJdbcDto> listarHorarioPpracIdPprlIdPmtrId(int pracId, int prlId, int mtrId) throws DocenteDtoException  ;
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo , materia y malla curricular paralelo
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @param mlcrprId - mlcrprId id de la malla curricular paralelo
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<DocenteJdbcDto> listarHorarioXpracIdXprlIdXmtrIdDocente(int pracId, int prlId, int mtrId, int mlcrprId) throws DocenteDtoException;
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo , materia y malla curricular paralelo
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @param mlcrprId - mlcrprId id de la malla curricular paralelo
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public List<DocenteJdbcDto> listarHorarioXpracIdXprlIdXmtrIdDocenteCompratida(int pracId, int prlId, int mtrId, int mlcrprId) throws DocenteDtoException  ;
		
	
	/**
	 * Realiza la busqueda del docente por periodo paralelo y materia
	 * @param pracId - pracId id del periodo academico 
	 * @param prlId - prlId , id del paralelo
	 * @param mtrId - mtrId id de la materia
	 * @return Lista el docente por los parametros buscado
	 * @throws DocenteDtoException Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws DocenteDtoNoEncontradoException  Lanzada cuando la busqueda no retorna resultados 
	 */
	public DocenteJdbcDto docenteXpracIdXprlIdXmtrId(int pracId, int prlId, int mtrId) throws DocenteDtoException ;
	
	/**
	 * Método que lista los docentes por carrera por apellido paterno o por identificación
	 * @param crrId - crrId id de la carrera para buscar
	 * @param prsPrimerApellido - prsPrimerApellido apellido paterno a buscar
	 * @param prsIdentificacion - prsIdentificacion número de identificación a buscar
	 * @param rolTipo - rolTipo tipo de rol que realiza la búsqueda entre el rol de secretarias
	 * @return - retorna la lista de docentes
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> listarPcrrIdPprsPrimerApellidoPprsIdentificacionPTipoRol(int crrId, String prsPrimerApellido, String prsIdentificacion, Integer rolTipo) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @param pracId - idPeriodoAcademico  enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPrac(String identificacionId , int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracComp(String identificacionId , int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPracRep(String identificacionId , int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPraXListMlcrprId(List<DocenteJdbcDto> listaMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPraXListMlcrprIdPrinci(List<DocenteJdbcDto> listaMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
		
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDocenteXPraXListMlcrprIdPrinciNotas(List<DocenteJdbcDto> listaMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarCarrerasXMlcrprId(int idMlcrpr) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
		
	/**
	 * Método que busca las carreras del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesMlcrprId(int mlcrprId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
		
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPrac(int idUsuario, int idCarrera, int docente,int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca el docente por usuario, carrera, periodo academico
	 * @param idUsuario - idUsuario del usuario enviado para buscar
	 * @param idCarrera - idCarrera de la carrera a buscar
	 * @param docente - docente tipo de docente enviado a buscar
	 * @param idPeriodoAcademico - idPeriodoAcademico del periodo academico a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPracXTipoCarrera(int idUsuario, int idCarrera, int docente,int pracId, int tipoCarrera) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	
	/**
	 * Método que busca los niveles del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesDocenteXPrac(int crrId, String identificacionId, int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca los niveles del docente por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesDocenteXPracComp(List<DocenteJdbcDto> listCarreras, String identificacionId, int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
		
	/**
	 * Método que busca las carreras del docente por perido academico activo no cierre
	 * @param pracId - periodo academico
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public List<DocenteJdbcDto> buscarCarrerasXIdentificacionXPeriodoActivo(String identificacionId, int pracId) throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;

	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXcarreraXnivelXdocente(int periodoId, int carreraId, int nivelId, int docente) throws DetallePuestoDtoJdbcException ;
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXcarreraXnivelXdocenteComp(int periodoId, List<DocenteJdbcDto> listCarreras, List<DocenteJdbcDto> listNiveles, int docente) throws DetallePuestoDtoJdbcException ;
		
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXcarreraXnivelXparaleloXdocente(int periodoId, int carreraId, int nivelId, int paraleloId, int docenteId) throws DetallePuestoDtoJdbcException;
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param carreraId - carreraId id de la carrera seleccionada para la busqueda
	 * @param nivelId - nivelId id del nivel seleccionado para la busqueda
	 * @param paraleloId - paraleloId id del paralelo seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXPeriodoXcarreraXnivelXdocente(int periodoId, int carreraId, int nivelId, int docenteId) throws DetallePuestoDtoJdbcException;
	
	/**
	 * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	 * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	 * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto> listarXperiodoXdocente(int periodoId, int docenteId) throws DetallePuestoDtoJdbcException ;
	
	/**
	 * Método que busca las carreras del director de posgrado por perido academico activo
	 * @param identificacionId - idUsuario del usuario enviado para buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarCarrerasDirectorPosgrado(String identificacionId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;

	/**
	 * Método que busca los niveles del programa de posgrado por perído activo
	 * @param crrId - id de la carrera a buscar
	 * @return - retorna el dto del docente a ser buscado
	 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
	 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
	 */
	public List<DocenteJdbcDto> buscarNivelesProgramaPosgrado(int crrId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;


	

/**
 * Método que busca docentes por materias
 * @param idPeriodo -  periodo academico seleccionado para la busqueda
 * @param idDependencia - dependencia seleccionado para la busqueda
 * @param idCarrera -carrera seleccionada para la busqueda
 * @param identificacion -docente
 * @return - retorna el dto del docente a ser buscado
 * @throws DetallePuestoDtoJdbcException - DetallePuestoDtoJdbcException excepción general lanzada 
 * @throws DetallePuestoDtoJdbcNoEncontradoException - DetallePuestoDtoJdbcNoEncontradoException lanzada cuando no se encuentra docentes
 */
		
	 List<DocenteJdbcDto> listarXDocentesXMateria(int pracId, String prsIdentificacion, Usuario usuario) throws DetallePuestoDtoJdbcException;

	
	 /**
	  * MÃ©todo que realiza la busqueda por periodo academico por carrera por nivel y por docente para listar las materias
	  * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
	  * @param docenteId - docenteId id del detalle puesto del docente seleccionado para la busqueda
	  * @return - retorna la lista de materias por los parÃ¡metros seleccionados
	  * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	  * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	  */
	 public List<DocenteJdbcDto>  listarXDocentesXMateriaXCarreraXMateria(int pracId, int dpnId, int crrId, Usuario usuario, int tipoUsuario ) throws DetallePuestoDtoJdbcException ;
	 





DocenteJdbcDto buscarDocenteXUsuarioXCarreraXPracAnterior(int idUsuario, int idCarrera, int docente, int pracId)
		throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;
	
/**
 * Método que busca docentes de autoevaluación
 * @param periodoId - periodoId id del periodo academico seleccionado para la busqueda
 * @return - retorna la lista de docentes autoevaluados
 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
 */
	public List<DocenteJdbcDto>  listarXDocentesAutoevaluacionXPeriodo(int pracId) throws DetallePuestoDtoJdbcException ;
	
	/**
	 * Método que busca docentes con materia y núero de estudiantes por paralelo
	 * @param pracId - pracId id del periodo academico seleccionado para la busqueda
	 * @param dpnId - dpnId id de la facultad seleccionado para la busqueda
	 * @param crrId - crrId id de la carrera seleccionado para la busqueda
	 * @return - retorna la lista de docentes con materia y núero de estudiantes por paralelo
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto>  listarDocentesMateriaNumMatriculadosXPeriodoXFacultadXCarrera(int pracId, int dpnId, int crrId) throws DocenteDtoException ;
	
	/**
	 * Método que busca docentes con el detalle puesto (categoria tiemo de dedicación y carrera)
	 * @param prsId - prsId id de la persona seleccionado para la busqueda
	 * @return - retorna la lista de docentes con materia y núero de estudiantes por paralelo
	 * @throws DocenteDtoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<DocenteJdbcDto>  listarDocentesConDetallePuestoXPersona(int prsId) throws DocenteDtoException ;


	List<DocenteJdbcDto> buscarCarrerasDocentePeriodosInactivos(String identificacionId, Integer pracId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarCarrerasDocentePeriodosAnteriores(String identificacionId, Integer pracId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;


	

	List<DocenteJdbcDto> listarXPeriodoAcademicoXDependenciaXCarrera(int pracId, int dpnId, int crrId, Usuario usuario,
			int tipoUsuario) throws DetallePuestoDtoJdbcException;


	List<DocenteJdbcDto> buscarCarrerasDocenteXPracActivoCierre(String identificacionId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;



	List<DocenteJdbcDto> buscarCarrerasDocenteXPracRepActivoCierre(String identificacionId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaComp(int carreraId, int nivelId,
			int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<DocenteJdbcDto> buscarCarrerasDocenteXPracEstado(Integer identificacionId, Integer pracEstado)
			throws  CarreraDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarCarrerasDocenteXPracEstadoActivoCierre(Integer identificacionId)
			throws CarreraDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarNivelesDocenteXCarrera(Integer identificacionId, Integer crrId)
			throws NivelDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarMateriasDocenteXCarreraXNivel(Integer identificacionId, Integer crrId, Integer nvlId)
			throws MateriaDtoNoEncontradoException;


	List<DocenteJdbcDto> buscarCarrerasDocenteXPracEstadoActivoCierreParaReporte(Integer identificacionId)
			throws CarreraDtoJdbcNoEncontradoException;


	DocenteJdbcDto buscarDatosDocenteXUsuario(int idUsuario)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;


//List<DocenteJdbcDto> listarXDocentesXMateria(int pracId, int dpnId, int crrId, String usrIdentificacion,
//		Usuario usuario) throws DetallePuestoDtoJdbcException;
	
	public List<DocenteJdbcDto>  listarDocenciaXParalelo(int pracId, int dpnId, int crrId, Usuario usuario, int tipoUsuario ) throws DetallePuestoDtoJdbcException;


	List<DocenteJdbcDto> buscarCarrerasDocenteXPracId(Integer identificacionId, Integer pracId)
			throws CarreraDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarNivelesDocenteXCarreraXPracId(Integer identificacionId, Integer crrId, Integer pracId)
			throws NivelDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarMateriasDocenteXCarreraXNivelXPracId(Integer identificacionId, Integer crrId,
			Integer nvlId, Integer pracId) throws MateriaDtoNoEncontradoException;


	List<DocenteJdbcDto> buscarProgramasDocente(Integer identificacionId) throws CarreraDtoJdbcNoEncontradoException;


	DocenteJdbcDto buscarDocenteInactivoXUsuarioXCarreraXPrac(int idUsuario, int idCarrera, int docente, int pracId)
			throws DetallePuestoDtoJdbcException, DetallePuestoDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarNivelesDocenteInactivoXCarreraXPracId(Integer identificacionId, Integer crrId,
			Integer pracId) throws NivelDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarNivelesDocenteInactivoXCarrera(Integer identificacionId, Integer crrId)
			throws NivelDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarMateriasDocenteInactivoXCarreraXNivel(Integer identificacionId, Integer crrId,
			Integer nvlId) throws MateriaDtoNoEncontradoException;


	List<DocenteJdbcDto> buscarCarrerasDocenteXPracIdMedicinaAnterior(Integer identificacionId, Integer pracId)
			throws CarreraDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarCarrerasDocenteInactivoXPracId(Integer identificacionId, Integer pracId)
			throws CarreraDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarMateriasDocenteInactivoXCarreraXNivelXPracId(Integer identificacionId, Integer crrId,
			Integer nvlId, Integer pracId) throws MateriaDtoNoEncontradoException;



	List<DocenteJdbcDto> buscarCarrerasEnLineaDocente2018_2019(Integer identificacionId)
			throws CarreraDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarNivelesDocenteEnLineaX350(Integer identificacionId, Integer crrId)
			throws NivelDtoJdbcNoEncontradoException;


	List<DocenteJdbcDto> buscarMateriasDocenteXCarreraXNivelEnLineaX350(Integer identificacionId, Integer crrId,
			Integer nvlId) throws MateriaDtoNoEncontradoException;


}
