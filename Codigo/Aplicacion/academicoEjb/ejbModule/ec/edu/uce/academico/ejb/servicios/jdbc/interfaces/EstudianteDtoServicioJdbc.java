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

 ARCHIVO:     EstudianteDtoServicioJdbc.java	  
 DESCRIPCION: Interface para los servicios de DTO del estudiante. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 13-MARZ-2017 		  Dennis Collaguazo			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;

/**
 * Interface EstudianteDtoServicioJdbc. Para los servicios del DTO de
 * estudiante.
 * 
 * @author dcollaguazo.
 * @version 1.0
 */
public interface EstudianteDtoServicioJdbc {

	/**
	 * Método que busca un estudiante por carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras
	 *            - lista de carreras en caso de que haya seleccionado todas las
	 *            carreras disponibles para el ususario.
	 * @param apellidoPaterno
	 *            - apellido del estudiante que se requiere buscar.
	 * @param identificacion
	 *            -identificacion del estudiante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodocarreraNivelTituloApPaternoIndetificacion(int idPeriodo,
			int idCarrera, List<CarreraDto> listCarreras, int idNivel, String apellidoPaterno, String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca una lista de estudiantes por carrera nivel materia
	 * apellidos o identificación
	 * 
	 * @param idPeriodo
	 *            - idPeriodo id del periodo academico a buscar
	 * @param idCarrera
	 *            - idCarrera id de la carrera a buscar
	 * @param idNivel
	 *            - idNivel id del nivel a buscar
	 * @param idParalelo
	 *            - idParalelo id del paralelo a buscar
	 * @param idMateria
	 *            - idMateria id de la materia a buscar
	 * @param docente
	 *            - docente id del detalle puesto para identificar el docente
	 *            que busca estudiantes
	 * @param apellidoPaterno
	 *            - apellidoPaterno a buscar
	 * @param identificacion
	 *            - identificación número de cédula o pasaporte a buscar
	 * @return - retorna una lista de estudiantes con los parametros ingresados
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacion(int idPeriodo,
			int idCarrera, int idNivel, int idParalelo, int idMateria, int docente, String apellidoPaterno,
			String identificacion) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante.
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @return Dto estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public EstudianteJdbcDto buscarEstudianteXId(int idEstudiante)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante y por id de
	 * matrícula.
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatricula(int idEstudiante, int idMatricula, int idPrac)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante y por id de
	 * matrícula.
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatriculaRep(int idEstudiante, int idMatricula,
			int idPrac) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante y por id de
	 * matrícula y por periodo academico para listar con todos los parametros de
	 * busqueda.
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac
	 *            - id del periodo academcico seleccionado para visualizar los
	 *            datos.
	 * @param estadoDetalleMatricula
	 *            - estadoDetalleMatricula estado del destalle matrícula activo
	 *            o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstado(int idEstudiante,
			int idMatricula, int idPrac, int estadoDetalleMatricula, int estadoCambio)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar con todos los parametros de busqueda.
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteMateriaRetiradaXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstado(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt, int estadoCambio) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar con todos los parametros de busqueda.
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstadoRetiroVer(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt, int estadoCambio) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	
	/**
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar con todos los parametros de busqueda.
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatriculaXidPeriodoAcademicoParaEstadoRetiro(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt, int estadoCambio) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	
	/**
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar estudiantes con matriculas no retiradas
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	
	/**ok
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar estudiantes con matriculas no retiradas y con control de tercera matrícula
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstadoCtrlTerceraMatricula(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar estudiantes con matriculas no retiradas
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteValidarRetiroMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	

	/**
	 * Método que busca un estudiante por carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodocarreraNivel(int idPeriodo, int idCarrera, int idNivel,
			int idParalelo, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarXPeriodocarreraNivelMatriculado(int idPeriodo, int idCarrera, int idNivel,
			String identificacion) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante y por id de
	 * matrícula para cer las materias de una matricula
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatriculaNuevo(int idEstudiante, int idMatricula)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id de matrícula.
	 * 
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdMatricula(int idMatricula)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	List<EstudianteJdbcDto> buscarEstudianteXIdPersonaXIdMatriculaPosgrado(int idEstudiante, int idMatricula,
			int idPrac) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante y por id de
	 * matrícula y por periodo academico.
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac
	 *            - id del periodo academico seleccionado para visualizar los
	 *            datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXIdentificacionXNotas(String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXIdentificacionXProemdio(String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id de matrícula.
	 * 
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteNivelacion()
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteNivelacionMigrarSIAC()
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> listaMateriasXfcmtXidentificacion(String idEstudiante, int idMatricula)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante y por id de
	 * matrícula.
	 * 
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula
	 *            - id de la matrícula seleccionada para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> listaMateriasXfcmtXidentificacionFull(String idEstudiante, int idMatricula)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por nivel, periodo, carrera,
	 * identificacion y presentar materias con notas.
	 * 
	 * @param identificacion
	 *            - identificacion del estudiantes.
	 * @param matriculaId
	 *            - id de la mtricula seleccionada para visualizar los datos.
	 * @param periodoId
	 *            - id de periodo academico seleccionado para visualizar los
	 *            datos.
	 * @param idCarrera
	 *            - id de carrera seleccionado para visualizar los datos.
	 * @return lista de materias con notas.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteNotasXIdentificacionXMatriculaXPeriodoXCarrera(String identificacion,
			int matriculaId, int periodoId, int carreraId)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarXPeriodocarreraNivelMatriculadoLista(int idPeriodo, int idCarrera, int idNivel,
			String identificacion) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por periodo, carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param identificacion
	 *            -identificacion del estudiante a buscar.
	 * @param apellidoPaterno
	 *            - apellido paterno del estudiante a buscar
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXIdentificacionxApellido(int idPeriodo,
			int idCarrera, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por periodo, carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param identificacion
	 *            -identificacion del estudiante a buscar.
	 * @param apellidoPaterno
	 *            - apellido paterno del estudiante a buscar
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXIdentificacionxApellidoAux(int idPeriodo,
			int idCarrera, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXNivel(int idPeriodo, int idCarrera, int idNivel)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por carrera, apellido paterno,
	 * identificacion.
	 * 
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXNivelXIdentificacionXApellido(int idPeriodo,
			int idCarrera, int idNivel, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * MQ:Método que busca un estudiante por periodo, carrera, nivel, paralelo,
	 * identificacion, apellidoPaterno
	 * 
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @param idParalelo
	 *            -paralelo en que se requiere buscar.
	 * @param identifiacion
	 *            -identificacion del estudiante a buscar.
	 * @param apellidoPaterno
	 *            - apellidoPaterno del estudiante a buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodocarreraXNivelxParaleloxIdentificacionXApellido(int idPeriodo,
			int idCarrera, int idNivel, int idParalelo, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por periodo, carrera, nivel, paralelo,
	 * identificacion, apellidoPaterno
	 * 
	 * @param idPerido
	 *            - periodo en el que se desea buscar.
	 * @param idCarrera
	 *            - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel
	 *            -nivel en el que se requiere buscar.
	 * @param idParalelo
	 *            -paralelo en que se requiere buscar.
	 * @param identifiacion
	 *            -identificacion del estudiante a buscar.
	 * @param apellidoPaterno
	 *            - apellidoPaterno del estudiante a buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros
	 *         ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXcarreraXNivelXParaleloXAsignaturaXIdentificacionXApellido(
			int idPeriodo, int idCarrera, int idNivel, int idParalelo, int idMateria, String identificacion,
			String apellidoPaterno) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca un estudiante por periodo, carrera, nivel, paralelo, identificacion, apellidoPaterno
	 * @param idPerido - periodo en el que se desea buscar.
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param idNivel -nivel en el que se requiere buscar.
	 * @param idParalelo -paralelo en que se requiere buscar.
	 * @param identifiacion -identificacion  del estudiante a buscar.
	 * @param apellidoPaterno - apellidoPaterno del estudiante a buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXcarreraXNivelXParaleloXAsignaturaXIdentificacionXApellidoReporte(int idPeriodo, int idCarrera, int idNivel, int idParalelo, int idMateria, String identificacion, String apellidoPaterno, int rcesEstado, List<CarreraDto> listCarrera) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante.
	 * 
	 * @param idEstudiante
	 *            - id del estudiante seleccionano para visualizar los datos.
	 * @return Dto estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public EstudianteJdbcDto buscarEstudianteXIdFichaEstudiante(int idFichaEstudiante)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante.
	 * 
	 * @param idPersona
	 *            - id de persona seleccionado para visualizar los datos.
	 * @return Dto estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public EstudianteJdbcDto buscarEstudianteXIdPersona(int idPersona)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteReingresoxIdentificacionXApellido(String identificacion,
			String apellidoPaterno, List<RolFlujoCarrera> listaCrr)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXNivelXIdentificacionXApellidoPagados(int idPeriodo,
			int idCarrera, int idNivel, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**
	 * Método que busca un estudiante por id del estudiante.
	 * 
	 * @param idPersona
	 *            - id de persona seleccionado para visualizar los datos.
	 * @return Dto estudiante.
	 * @throws EstudianteDtoJdbcException
	 *             - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 *             - EstudianteDtoJdbcNoEncontradoException excepción cuando no
	 *             encuentra estudiantes con los parametros ingresados
	 */
	public EstudianteJdbcDto buscarEstudianteXPeriodo(int idPersona, int idPeriodo)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXIdentificacionxApellidoPagados(int idPeriodo,
			int idCarrera, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXIdentificacionxApellidoAuxPagados(int idPeriodo,
			int idCarrera, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacionPagados(
			int idPeriodo, int idCarrera, int idNivel, int idParalelo, int idMateria, int docente,
			String apellidoPaterno, String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacionPagadosAlterno(
			int idPeriodo, int idCarrera, int idNivel, int idParalelo, int idMateria, int docente,
			String apellidoPaterno, String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	public List<EstudianteJdbcDto> buscarEstudianteXperiodoXcarreraNivelMateriaApPaternoIndetificacionPagadosNoRetirados(int idPeriodo, int idCarrera, int idNivel, int idParalelo, int idMateria, int docente, String apellidoPaterno, String identificacion) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
		
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXProgramaPosgrado(int idPeriodo,
			int idCarrera)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Mètodo que permite listar estudiantes por caarrera.
	 * @author fgguzman
	 * @param parametro - cedula, pasaporte o apellido paterno.
	 * @param carreras - id carreras a buscar.
	 * @param tipoBusqueda 0-cedula o pasaporte / 1-apellido paterno.
	 * @return estudiantes.
	 * @throws EstudianteDtoJdbcException
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 */
	List<EstudianteJdbcDto> buscarEstudiantePorCarrerasSIIU(String parametro, List<String> carreras, int tipoBusqueda) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Mètodo que permite listar estudiantes por carrera.
	 * @author fgguzman
	 * @param parametro - cedula, pasaporte o apellido paterno.
	 * @param carreras - id carreras a buscar.
	 * @param tipoBusqueda 0-cedula o pasaporte / 1-apellido paterno.
	 * @return estudiantes.
	 * @throws EstudianteDtoJdbcException
	 * @throws EstudianteDtoJdbcNoEncontradoException
	 */
	List<EstudianteJdbcDto> buscarEstudiantePorCarrerasSAU(String parametro, List<String> carreras, int tipoBusqueda) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**MQ
	 * Método que busca un estudiante por identificación y carrera.
	 * @param idPersona - id de persona seleccionado para visualizar los datos.
	 * @param idCarrera - id de carrera seleccionado para visualizar los datos.
	 * @return Dto estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */

	public List<EstudianteJdbcDto> buscarEstudianteXIdentificacionXCarrera(String identificacion, int idCarrera) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;


	/**
	 * Método que busca total de estudiantes por carrera y nivel.
	 * @param idPeriodo - id de persona seleccionado para visualizar los datos.
	 * @param idDependencia - id de carrera seleccionado para visualizar los datos.
	 * @param idCarrera}
	 * @param idNivel
	 * @return Dto estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	
	public List<EstudianteJdbcDto> buscarEstudianteXPeriodoXFacultadXCarreraXNivel(int pracId, int dpnId,int crrId, int nvlId) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	
	/**
	 * MQ:
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar 
	 *  asignaturas sin nota  o asistencia (segundo hemisemestre) en la matricula actual , en las cuales puede realizar un retiro  por Casos fortuitos.
	 * 
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista de asignaturas del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRetiroFortuitoMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(int idPersona, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	
	
	
	
	/**
	 * MQ:
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar 
	 *  asignaturas sin nota  o asistencia (segundo hemisemestre) en la matricula actual , en las cuales puede realizar un retiro  por Casos fortuitos.
	 * 
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista de asignaturas del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRetiroFortuitoMatriculaXprsIdXmtrIdXpracIdXdtmtParaEstado(int idPersona, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;


	List<EstudianteJdbcDto> buscarEstudianteXPeriodoXCarreraXIdentificacionXApellidoPosgrado(int idPeriodo,
			int idCarrera, String identificacion, String apellidoPaterno)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * MQ:  12 dic 2018
	 * Método que busca las asignaturas de una matricula por id del estudiante y por id de matrícula y por periodo academico 
	 * con solicitudes de retiro fortuito realizado
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarAsignaturasValidarRetiroFortuitoXprsIdXmtrIdXpracIdXdtmtEstado(int idPersona, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	/**
	 * MQ:  19 dic 2018
	 * Método que busca las asignaturas de una matricula por id del estudiante y por id de matrícula y por periodo academico 
	 * con solicitudes de retiro fortuito realizado
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarAsignaturasAprobarRetiroFortuitoXprsIdXmtrIdXpracIdXdtmtEstado(int idPersona, int idMatricula, int idPrac, int estadoDtmt) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/** 
	 * MQ:7 ene 2018
	 * Método que busca un estudiante por id del estudiante y por id de matrícula y por periodo academico para listar con todos los parametros de busqueda.
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDtmt - estadoDtmt estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @param estadoCambio - estadoCambio estado solicitud retiro del destalle matrícula aprobado o negado seleccionado para visualizar los datos.
	 * @return lista del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteMateriaInactivasRetiroAprobadoXIdEstudianteXIdMatriculaXidPeriodoxEstados(int idEstudiante, int idMatricula, int idPrac, int estadoDtmt, int estadoCambio) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	/**MQ:  14 ene 2019
	 * Método que busca un estudiante por identificación y carrera.
	 * @param identificacion - id de persona seleccionado para visualizar los datos.
	 * @param idCarrera - id de carrera seleccionado para visualizar los datos.
	 * @param idPeriodo - id de periodo seleccionado para visualizar los datos.
	 * @param listCarrera - lista de carreras para visualizar los datos.
	 * @param listPeriodo - lista de periodos para visualizar los datos.
	 * @return Lista EstudianteJdbcDto.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */

	public List<EstudianteJdbcDto> buscarEstudianteXIdentificacionXCarreraXPeriodo(String identificacion, int idCarrera, int idPeriodo, List<CarreraDto> listCarrera, List<PeriodoAcademico> listPeriodo) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
     * MQ: 14 ene 2019
     * Método que busca  las asignaturas de la matricula un estudiante por id del estudiante y por id de matrícula y por periodo academico para anular matricula
	 * @param idEstudiante - id del estudiante seleccionano para visualizar los datos.
	 * @param idMatrícula - id de la matrícula seleccionada para visualizar los datos.
	 * @param idPrac - id del periodo academcico seleccionado para visualizar los datos.
	 * @param estadoDetalleMatricula - estadoDetalleMatricula estado del destalle matrícula activo o inactivo seleccionado para visualizar los datos.
	 * @return lista de asignaturas del estudiante.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarAsignaturasMatriculaXprsIdXmtrIdXpracIdXdtmtEstado(int idPersona, int idMatricula, int idPrac) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca un estudiantes retirados por
	 * @param idPeriodo - id de periodo seleccionado para visualizar los datos.
	 * @param idCarrera - id de carrera seleccionado para visualizar los datos.
	 * @param apellido - apellido del estudiante
	 * @param identificacion - identificacion del estudiante
	 * @return Lista EstudianteJdbcDto, lista de retirados.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacion( int idPeriodo, int idCarrera, String apellido, String identificacion, List<CarreraDto> listCarrera, Integer tipoCarrera, Integer idFacultad) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	List<EstudianteJdbcDto> buscarEstudianteXPeriodoXProgramaPosgradoDireccionPosgrado(int idPeriodo, int idCarrera)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;


	EstudianteJdbcDto buscarEstudianteMatriculaPosgrado(EstudianteJdbcDto estudiante, int idPeriodo, int idCarrera)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	List<EstudianteJdbcDto> buscarEstudianteNotasXIdentificacionXMatriculaXPeriodoXCarreraPosgrado(
			String identificacion, int matriculaId, int periodoId, int carreraId)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca  estudiantes con solicitudes de terceras matriculas
	 * @param idPeriodo - id de periodo seleccionado para visualizar los datos.
	 * @param idCarrera - id de carrera seleccionado para visualizar los datos.
	 * @param apellido - apellido del estudiante
	 * @param identificacion - identificacion del estudiante
	 * @param tipoCarrera - tipo de carrera.
	 * @param listCarrera - lista de carreras.
	 * @return Lista EstudianteJdbcDto, lista de retirados.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteSltrmtXperiodoXcarreraXapellidoXidentificacion( int idPeriodo, int idCarrera, String apellido, String identificacion, List<CarreraDto> listCarrera, Integer tipoCarrera, Integer idFacultad) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;


	List<EstudianteJdbcDto> buscarEstudianteNivelacion1(String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	List<EstudianteJdbcDto> buscarEstudianteReprobadoNivelacion(String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;

	List<EstudianteJdbcDto> buscarXPeriodoNivelacionMatriculadoLista(int idPeriodo, String identificacion)
			throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;	
	
	
	/**
	 * MQ: 5 jun 2019
	 * Método que busca un estudiantes retirados por
	 * @param idPeriodo - id de periodo seleccionado para visualizar los datos.
	 * @param idCarrera - id de carrera seleccionado para visualizar los datos.
	 * @param apellido - apellido del estudiante
	 * @param identificacion - identificacion del estudiante
	* @param tipo - tipo de retiro o anulacion
	 * @return Lista EstudianteJdbcDto, lista de retirados.
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRetiradoXperiodoXcarreraXapellidoXidentificacionXtipo( int idPeriodo, int idCarrera, String apellido, String identificacion, List<CarreraDto> listCarrera, Integer tipoCarrera, Integer idFacultad, Integer tipo) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
}
