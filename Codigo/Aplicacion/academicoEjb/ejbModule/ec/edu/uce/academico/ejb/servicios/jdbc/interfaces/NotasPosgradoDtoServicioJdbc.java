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

 ARCHIVO:     NotasPregradoDtoServicioJdbc.java	  
 DESCRIPCION: Interface para los servicios de DTO del estudiante. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 9-JUNIO-2017 		  Marcelo Quishpe			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.EstudianteDtoJdbcNoEncontradoException;

/**
 * Interface NotasPregradoDtoServicioJdbc.
 * Para los servicios del DTO de estudiante.
 * @author lmquishpei.
 * @version 1.0
 */
public interface NotasPosgradoDtoServicioJdbc {


	/**
	 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
	 * @param idPeriodo - idPeriodo id del periodo academico a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param idNivel - idNivel id del nivel a buscar
	 * @param idParalelo - idParalelo id del paralelo a buscar
	 * @param idMateria - idMateria id de la materia a buscar
	 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
	 * @return - retorna una lista de estudiantes con los parametros ingresados
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteXperiodoXcarreraXnivelXparaleloXmateriaXdocente( int idCarrera, int idNivel, int idParalelo, int idMateria,  int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
	 * @param idPeriodo - idPeriodo id del periodo academico a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param idNivel - idNivel id del nivel a buscar
	 * @param idParalelo - idParalelo id del paralelo a buscar
	 * @param idMateria - idMateria id de la materia a buscar
	 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
	 * @return - retorna una lista de estudiantes con los parametros ingresados
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRecuperacionXperiodoXcarreraXnivelXparaleloXmateriaXdocente( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
	 * @param idPeriodo - idPeriodo id del periodo academico a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param idNivel - idNivel id del nivel a buscar
	 * @param idParalelo - idParalelo id del paralelo a buscar
	 * @param idMateria - idMateria id de la materia a buscar
	 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
	 * @return - retorna una lista de estudiantes con los parametros ingresados
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRecuperacionReporte( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
	 * @param idPeriodo - idPeriodo id del periodo academico a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param idNivel - idNivel id del nivel a buscar
	 * @param idParalelo - idParalelo id del paralelo a buscar
	 * @param idMateria - idMateria id de la materia a buscar
	 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
	 * @return - retorna una lista de estudiantes con los parametros ingresados
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRectificacion( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
	
	/**
	 * Método que busca una lista de estudiantes por periodo carrera nivel paralelo materia docente
	 * @param idPeriodo - idPeriodo id del periodo academico a buscar
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param idNivel - idNivel id del nivel a buscar
	 * @param idParalelo - idParalelo id del paralelo a buscar
	 * @param idMateria - idMateria id de la materia a buscar
	 * @param idDocente - idDocente id de Ficha Docente para identificar el docente que busca estudiantes
	 * @return - retorna una lista de estudiantes con los parametros ingresados
	 * @throws EstudianteDtoJdbcException - EstudianteDtoJdbcException excepción general
	 * @throws EstudianteDtoJdbcNoEncontradoException - EstudianteDtoJdbcNoEncontradoException excepción cuando no encuentra estudiantes con los parametros ingresados
	 */
	public List<EstudianteJdbcDto> buscarEstudianteRectificacionRecuperacion( int idCarrera, int idNivel, int idParalelo, int idMateria, int idDocente , int idRcesMlcrpr) throws EstudianteDtoJdbcException, EstudianteDtoJdbcNoEncontradoException;
}
