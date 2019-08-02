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

 ARCHIVO:     PeriodoAcademicoDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla periodo academico.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 22-MARZ-2017		Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoDtoJdbcNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;


/**
 * Interface PeriodoAcademicoDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla periodo academico.
 * @author dcollaguazo
 * @version 1.0
 */
public interface PeriodoAcademicoDtoServicioJdbc {

	/**
	 * Realiza la busqueda de un periodo academico por id
	 * @param periodoId - id del carrera
	 * @return Periodo academico con el id solicitado 
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	PeriodoAcademicoDto buscarXId(int periodoId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<PeriodoAcademicoDto> listarTodos() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion por tipo de periodo
	 * @return Lista todos los periodos academicos de la aplicacion por tipo de periodo
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<PeriodoAcademicoDto> listarTodosXTipo(int tipoPeriodo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por el estado solicitado
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<PeriodoAcademicoDto> listarXEstado(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<PeriodoAcademicoDto> listarXEstado(int estadoActivo, int estadoIniciado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de todos los periodos academicos por el estado
	 * @return Lista todos los periodos academicos de la aplicacion por los estados solicitados
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<PeriodoAcademicoDto> listarXEstadoXTipo(int estadoActivo, int estadoIniciado, int tipoPeriodo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	List<PeriodoAcademicoDto> listarXEstadoPosgrado(int estadoActivo, int estadoIniciado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarXEstadoPosgrado(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los periodos academicos por el usuario
	 * @return Lista todos los periodos academicos de la aplicacion por el estado solicitado
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarXUsuarioPosgrado(Integer idUsuario) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarTodosPosgrado() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	PeriodoAcademicoDto buscarXEstadoPregrado(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	PeriodoAcademicoDto buscarXIdPosgrado(int periodoId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarPeriodosMatriculadoConComprobantePago(String cedula) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarPeriodosMatriculadoConComprobantePagoPosgrado(String cedula) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	/**
	 * Método que permite buscar Periodos Academicos segun los estados y tipos de periodo academico.
	 * @author fgguzman
	 * @param estados - {0-Activo, 1-Inactivo, 2-En cierre}
	 * @param tipo - {0-PREGRADO,1-POSGRADO,2-IDIOMAS,3-SUFICIENCIA_CULTURA_FISICA,4-SUFICIENCIA_CULTURA_FISICA_INTENSIVO}
	 * @return periodos academicos segun parametros ingresados.
	 * @throws PeriodoAcademicoDtoJdbcException
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	 */
	List<PeriodoAcademicoDto> buscar(Integer[] estados, Integer[] tipos) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que permite buscar Periodos Academicos segun los estados y tipos de periodo academico.
	 * @author fgguzman
	 * @param estado - {0-Activo, 1-Inactivo, 2-En cierre}
	 * @param tipo - {0-PREGRADO,1-POSGRADO,2-IDIOMAS,3-SUFICIENCIA_CULTURA_FISICA,4-SUFICIENCIA_CULTURA_FISICA_INTENSIVO}
	 * @return periodos academicos segun parametros ingresados.
	 * @throws PeriodoAcademicoDtoJdbcException
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	 */
	PeriodoAcademicoDto buscar(int estado, int tipo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	/**
	 * Método que permite buscar Periodos Academicos para CargaHoraria SIIU.
	 * @author fgguzman
	 * @throws PeriodoAcademicoDtoJdbcException
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	 */
	 List<PeriodoAcademicoDto> buscarPeriodosCargaHoraria() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	 /**
	  * Método que permite cargar los periodos academicos segun nivel academico que impartio el docente.
	  * @author fgguzman
	  * @param identificacion - cedula o pasaporte.
	  * @param nivelAcademico - pregrado , posgrado, suficiencias, nivelacion
	  * @return periodos academicos.
	  * @throws PeriodoAcademicoDtoJdbcException
	  * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	  */
	 List<PeriodoAcademicoDto> buscarPeriodosPorDocenteNivelAcademico(String identificacion, int nivelAcademico) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException,  PeriodoAcademicoNoEncontradoException;

	 
	/**
	 * Método que permite buscar un periodo academico segun su pracId.
	 * @author fgguzman 
	 * @param pracId - Pk del PeriodoAcademico
	 * @return el periodo academico solicitado.
	 * @throws PeriodoAcademicoDtoJdbcException
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	 */
	PeriodoAcademicoDto buscar(int pracId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	/**
	 * Método que permite buscar los periodos academicos segun los parametros de entrada.
	 * @author fgguzman
	 * @param usrId - id del Usuario
	 * @param rolId - rol solicitado
	 * @param pracEstado - estado del periodo academico
	 * @return periodos academico
	 * @throws PeriodoAcademicoDtoJdbcException
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException
	 */
	List<PeriodoAcademicoDto> buscarPeriodos(int usrId, int rolId, int pracEstado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	PeriodoAcademicoDto buscarXEstadoIdiomas(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarTodosSAU() throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarTodosSAUxEscCodigoxEspeCodigo(int esccodigo, int especodigo) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	PeriodoAcademicoDto buscarXEstadoCulturaFisica(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	PeriodoAcademicoDto buscarXEstadoSuficienciaCulturaFisica(int estado) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarTodosXCarrera(List<Carrera> listaCarreras)
			throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	List<PeriodoAcademicoDto> listarTodosPosgradoPorDependencia(Integer dpnId)
			throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de todos los periodos academicos de la aplicacion asociados a la carrera
	 * @return Lista todos los periodos academicos de la aplicacion
	 * @throws PeriodoAcademicoDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PeriodoAcademicoDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PeriodoAcademicoDto> listarPeriodoPosgradoXCarrera(Integer crrId) throws PeriodoAcademicoDtoJdbcException, PeriodoAcademicoDtoJdbcNoEncontradoException;
	
	
	
}
