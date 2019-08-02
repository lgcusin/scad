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

 ARCHIVO:     ParaleloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Paralelo.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 17-03-2017			David Arellano				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.Date;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;


/**
 * Interface ParaleloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Paralelo.
 * @author darellano
 * @version 1.0
 */
public interface ParaleloDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por período
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXmallaMateriaXperiodo(int mallaMateriaId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada carrera por período
	 * @param usuarioId - id del usuario
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera, ese período y usuario
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXusuarioXcarreraXperiodo(int usuarioId, int carreraId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * 
	 * Método que busca el paralelo por carrera, por nivel.
	 * @param idPeriodo - id del periodo académico actual o vigente
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @return Lista de paralelos que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws ParaleloDtoException - EstudianteDtoJdbcException excepción general
	 * @throws ParaleloDtoNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra paralelos con los parametros ingresados
	 */
	public List<ParaleloDto> listarParaleloXPeriodoCarreraNivel(int idPeriodo, int idCarrera, int idNivel) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * MQ
	 * Método que busca el paralelo por carrera, por nivel para postgrado sin exception no encontrado
	 * @param idPeriodo - id del periodo académico actual o vigente
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @return Lista de paralelos que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws ParaleloDtoException - EstudianteDtoJdbcException excepción general
	 * @throws ParaleloDtoNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra paralelos con los parametros ingresados
	 */
	public List<ParaleloDto> listarParaleloXPeriodoCarreraNivelPostgradoNuevo(int idPeriodo, int idCarrera, int idNivel) throws ParaleloDtoException;
	
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoXcarreraXnivelXdocente(int periodoId, int carreraId, int nivelId, int docente) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Método que busca los paralelos por periodo Activo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @param nivel - id del nivel a buscar
	 * @param docente - id del docente a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocente(int carreraId, int nivelId, int docente , int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteCompartida( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteNoComp( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Método que busca los paralelos por periodo, carrera , nivel, docente
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteNoCompModular( int carreraId, int nivelId, int docente, int materiaId, String paralelo) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada carrera por período y nivel
	 * @param usuarioId - id del usuario
	 * @param carreraId - id de la carrera
	 * @param periodoId - id del periodo a buscar
	 * * @param nivelId - id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera, ese período y usuario
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<ParaleloDto> listarXusuarioXcarreraXperiodoXNivel(int usuarioId, int carreraId, int periodoId, int nivelId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas los paralelos que pertenecen a cada materia por período
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param nivelId - nivelId id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXNivelCarreraXperiodo(int carreraId, int periodoId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas los paralelos de cada materia activos e inactivos para hacer la cuenta de paralelos
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param nivelId - nivelId id del nivel a buscar
	 * @return Lista todas los paralelos pertenecientes a esa carrera y ese período
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> listarXNivelCarreraXperiodoNumMaxParalelo(int carreraId, int periodoId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	

	ParaleloDto listarXMallaCurricularXCarreraXEstadoActivo(int carreraId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * 
	 * Método que busca el paralelo por carrera, por nivel para postgrado
	 * @param idPeriodo - id del periodo académico actual o vigente
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @return Lista de paralelos que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws ParaleloDtoException - EstudianteDtoJdbcException excepción general
	 * @throws ParaleloDtoNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra paralelos con los parametros ingresados
	 */
	public List<ParaleloDto> listarParaleloXPeriodoCarreraNivelPostgrado(int idPeriodo, int idCarrera, int idNivel) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda del paralelo que pertenecen homologacion de la carrera
	 * @param mallaMateriaId - id de la malla materia id
	 * @param periodoId - id del periodo a buscar
	 * @param descripcion- la descripcion del paralelo
	 * @return  paralelo  que cumple con la descripcion.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public ParaleloDto buscarXmallaMateriaXperiodoXDescripcion(int mallaMateriaId, int periodoId, String descripcion) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda del paralelo por Materia Id
	 * @param MateriaId - id de la materia 
	 * @return  paralelo  que cumple con la busqueda.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> ListarXMateriaId(int MateriaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda del paralelo por Materia Id
	 * @param MateriaId - id de la materia 
	 * @return  paralelo  que cumple con la busqueda.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> ListarXMateriaIdXDisponibilidadCupo(int MateriaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Metodo que permite buscar los paralelos que aun tienen cupo segun parametros.
	 * @param materiaId - id de la materia
	 * @param periodoId - id del periodo
	 * @return paralelos disponibles
	 * @throws ParaleloDtoException
	 * @throws ParaleloDtoNoEncontradoException
	 */
	public List<ParaleloDto> ListarXMateriaIdXDisponibilidadCupoXTipoPeriodo(int materiaId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	public List<ParaleloDto> listarMayorParaleloXNivelCarreraXperiodo(int carreraId, int periodoId, int nivelId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelPosgrado(int carreraId, int nivelId, int materiaId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda del paralelo por Materia Id y Periodo Id.
	 * @author ajvillafuerte v1
	 * @author fgguzman v2
	 * @param materiaId - id de la materia 
	 * @param periodoId - id del periodo
	 * @return  paralelos segun parametros ingresados.
	 * @throws ParaleloDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws ParaleloDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ParaleloDto> buscarParlelosPorMateriaPeriodo(int materiaId, int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	public List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocentePagados(int carreraId, int nivelId, int docente,
			int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	public List<ParaleloDto> listarXmallaMateriaXperiodoNivelacion(int mallaMateriaId, int periodoId, int crrId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	public List<ParaleloDto> listarXperiodoEnCierreXcarreraXnivelXdocenteNoComp(int carreraId, int nivelId, int docente,
			int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXperiodoEnCierreXcarreraXnivelXdocenteCompartida(int carreraId, int nivelId, int docente,
			int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> ListarXMateriaIdXDisponibilidadCupoEnCierre(int MateriaId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteCompartidaAnterior(int carreraId, int nivelId,
			int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	List<ParaleloDto> listarXperiodoActivoXcarreraXnivelXdocenteNoCompAnterior( int carreraId, int nivelId, int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Método que permite buscar los paralelos asignados a un docente según parámetros ingresados. Estados del periodo activo y en cierre.
	 * @author fgguzman
	 * @param carreraId
	 * @param materiaId
	 * @param identificacion
	 * @return paralelos asignados al docente.
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloException
	 */
	List<ParaleloDto> buscarParalelosPorDocente(int carreraId, int materiaId, String identificacion) throws ParaleloNoEncontradoException, ParaleloException;
	
	/**
	 * Método que permite recuperar los paralelos que un docente dicto en el transcurso del tiempo.
	 * @author fgguzman
	 * @param identificacion
	 * @param nivelAcademico
	 * @param periodo
	 * @param carrera
	 * @return paralelos a cargo
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloValidacionException
	 * @throws ParaleloException
	 */
	List<ParaleloDto> buscarParalelosPorDocenteNivelPeriodoCarrera(String identificacion, int nivelAcademico, int periodo, int carrera)throws ParaleloNoEncontradoException, ParaleloValidacionException, ParaleloException;
	
	/**
	 * Método que permite listar los paralelos segun parametros solicitados.
	 * @author fgguzman
	 * @param periodoId - id del periodo academico
	 * @param carreraId - id de la carrera.
	 * @param nivelId - id del nivel requerido
	 * @return paralelos encontrados.
	 * @throws ParaleloDtoException
	 * @throws ParaleloDtoNoEncontradoException
	 */
	List<ParaleloDto> buscarParalelos(int periodoId, int carreraId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

	/**
	 * Método que permite listar los paralelos segun parametros solicitados.
	 * @author fgguzman
	 * @param periodoId - id del periodo academico
	 * @param areaId - id del area
	 * @param carreraId - id de la carrera
	 * @param nivelId - id del nivel
	 * @return paralelos encontrados
	 * @throws ParaleloDtoException
	 * @throws ParaleloDtoNoEncontradoException
	 */
	List<ParaleloDto> buscarParalelosPorAreaCarrera(int periodoId,int areaId, int carreraId, int nivelId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

	/**
	 * Método que permite listar los paralelos segun parametros solicitados.
	 * @author fgguzman
	 * @param periodoId - id del periodo academico
	 * @param carreraId - id de la carrera.
	 * @param nivelId - id del nivel requerido
	 * @param modalidadId - id de la modalidad
	 * @return paralelos encontrados.
	 * @throws ParaleloDtoException
	 * @throws ParaleloDtoNoEncontradoException
	 */
	List<ParaleloDto> buscarParalelos(int periodoId, int carreraId, int nivelId, int modalidadId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

	/**
	 * Método que permite listar los paralelos segun parametros solicitados.
	 * @author fgguzman
	 * @param modalidad - id de la Modalidad
	 * @param materiaId - id de la Materia
	 * @return paralelos
	 * @throws ParaleloDtoException
	 * @throws ParaleloDtoNoEncontradoException
	 */
	List<ParaleloDto> buscarParalelosPorModalidad(int modalidad, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;
	
	/**
	 * Método que permite listar estudiantes matriculados o inscritos en un paralelo.
	 * @author fgguzman
	 * @param periodoId - id del paralelo
	 * @param carreraId - id de la carrera
	 * @param paraleloId - id del paralelo
	 * @param mlcrmtId - id de la malla curricular materia
	 * @return estudiantes matriculados o inscritos
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarMatriculados(int periodoId, int carreraId, int paraleloId, int mlcrmtId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException ;
	
	/**
	 * Método que permite listar los paralelos segun parametros solicitados.
	 * @author fgguzman
	 * @param periodoId - id del periodo academico
	 * @param fecha - fecha inicial para busqueda.
	 * @return paralelos encontrados.
	 * @throws ParaleloDtoException
	 * @throws ParaleloDtoNoEncontradoException
	 */
	List<ParaleloDto> buscarParalelos(int periodoId, Date fecha) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteXmateriaNoComp(int carreraId, int nivelId, int docente,
			int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;



	List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaComp(int carreraId, int nivelId,
			int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteXmateriaNoCompParaReporte(int carreraId, int nivelId,
			int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXperiodoEnCierreXcarreraXnivelXdocenteNoCompXCedula(int carreraId, int nivelId, int docente,
			int materiaId, String identificacion) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteXmateriaNoCompXPracId(int carreraId, int nivelId,
			int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteXMateriaCompXpracId(int carreraId, int nivelId,
			int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXusuarioXcarreraXperiodoXNivelXArea(int usuarioId, int carreraId, int periodoId,
			int nivelId, int areaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParalelosXProgramaXnivelXdocenteXmateriaNoComp(int carreraId, int nivelId, int docente,
			int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;



	List<ParaleloDto> listarXmallaMateriaXperiodoNivelacionParaEdicionFull(int materiaId, int periodoId, int crrId,
			int areaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXmallaMateriaXperiodoNivelacionParaEdicion(int mallaMateriaId, int periodoId, int crrId,
			int areaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;



	List<ParaleloDto> ListarXMateriaIdNivelacion(int MateriaId, int pracId, int crrId)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParalelosTotalesXcarreraXPeriodoNoComp(int carreraId, String identificacionDocente,
			int periodoId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParaleloReporteNotasDocenteXCrrXPracXIdentificacionComp(int carreraId, String identificacion,
			int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;



	List<ParaleloDto> listarParalelosXcarreraXnivelXdocenteInactivoXmateriaNoComp(int carreraId, int nivelId,
			int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarXperiodoActivoEnCierreXcarreraXnivelXdocenteInactivoXMateriaComp(int carreraId, int nivelId,
			int docente, int materiaId, int pracId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


	List<ParaleloDto> listarParalelosXProgramaXnivelXdocenteXmateriaNoCompEnLineaX350(int carreraId, int nivelId,
			int docente, int materiaId) throws ParaleloDtoException, ParaleloDtoNoEncontradoException;

}
