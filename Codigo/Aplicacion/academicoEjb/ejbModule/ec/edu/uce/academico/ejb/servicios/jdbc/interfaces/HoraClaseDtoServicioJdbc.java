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

 ARCHIVO:     HoraClaseDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla HoraClaseDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 23-SEPT-2017			Dennis Collaguazo			       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.HoraClaseDtoValidacionException;

/**
 * Interface HoraClaseDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla HoraClaseDto.
 * @author dcollaguazo
 * @version 1.0
 */
public interface HoraClaseDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas las hora clase disponible por aula y por día
	 * @param alaId - alaId id del aula 
	 * @param hracDia - hracDia dia que se quiere buscar
	 * @param pracId - pracId id del periodo academico a buscar la disponibilidad
	 * @param listaHorasND - listaHorasND lista de horas clase que estan utilizadas
	 * @return Lista de todas las hora clase que se necesita que esten disponibles para la asignación 
	 * @throws HoraClaseDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HoraClaseDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<HoraClaseDto> listarPalaIdPhracDiaPpracIdPlistHocl(int alaId, int hracDia, int pracId, List<HorarioAcademicoDto> listaHorasND) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	
	public List<HoraClaseDto> listarPalaIdPhracDiaPpracIdPlistHoclNuevo(int alaId, int hracDia, int pracId, List<HorarioAcademicoDto> listaHorasND) throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de todas las hora clase 
	 * @return Lista de todas las hora clase 
	 * @throws HoraClaseDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws HoraClaseDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<HoraClaseDto> listarTodos() throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	/**
	 * Método que permite obtener una lista con las horas de clase disponibles para todas las aulas durante un dia.
	 * Intervalos de una hora.
	 * @author fgguzman
	 * @return plantilla horarios de clase.
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> listarTemplateHorarioClases() throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;

	/**
	 * Método que permite buscar el horario academico de una materia en especifico segun su malla curricular paralelo id.
	 * @author fgguzman
	 * @param mlcrprId - id de la malla curricular paralelo
	 * @return horarios asociados 
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarHorarioAcademico(int mlcrprId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	
	/**
	 * Método que permite buscar el horario funcines de un docente.
	 * @author fgguzman
	 * @param docente - PersonatDto
	 * @return horarios asociados 
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarHorarioFunciones(PersonaDto docente, int periodoId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	
	/**
	 * Método que permite buscar el horario academico de una materia en especifico segun su malla curricular paralelo id.
	 * @author fgguzman
	 * @param docente - PersonatDto
	 * @return horarios asociados 
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarHorarioAcademico(PersonaDto docente, int periodoId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	/**
	 * Método que permite traer la carga horaria de un docente.
	 * @author fgguzman
	 * @param docenteId - id de la persona.
	 * @param periodoId - id del periodo.
	 * @return carga horaria activa
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarCargaHoraria(int docenteId, int periodoId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	
	/**
	 * Método que permite buscar la carga horaria de un docente.
	 * @author fgguzman
	 * @param periodoId - id del periodo.
	 * @param paraleloId - id del paralelo.
	 * @param materiaId - id del materia.
	 * @return carga horaria activa
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarCargaHoraria(int periodoId, int paraleloId, int materiaId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	/**
	 * Método que permite buscar la disponibilidad de un aula en  periodos academicos activos.
	 * @author fgguzman
	 * @param aulaId - id del aula
	 * @param diaId - id del dia
	 * @param horaId - id de la Hora
	 * @param pracEstado - estado de los periodos academicos.
	 * @return horas asignadas
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarDisponibilidad(int aulaId, int diaId, String horaId, int pracEstado)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	/**
	 * Método que permite buscar la disponibilidad de un aula en  periodos academicos activos.
	 * @author fgguzman
	 * @param aulaId - id del aula
	 * @param diaId - feha y hora
	 * @param horaId - id de la Hora
	 * @param pracEstado - estado de los periodos academicos.
	 * @return horas asignadas
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarDisponibilidad(int aulaId, String diaId, String horaId, int pracEstado)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	/**
	 * Método que permite encontrar un objeto HorarioClaseDto segun el Aula y la Hora Inicial.
	 * @author fgguzman
	 * @param aulaId - id
	 * @param horaInicio - hora inicio de clases
	 * @return HorarioClaseDto
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	HoraClaseDto buscarHoraClaseDto(int aulaId, String horaInicio)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException,HoraClaseDtoValidacionException;

	/**
	 * Método que permite buscar si a una asignatura ya se le asigno Horas Clase. 
	 * @author fgguzman
	 * @return horas clase por semana.
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarHorarioAcademicoPorPeriodoParaleloAsignatura(int periodoId, int paraleloId, int materiaId)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException;

	/**
	 * Método que permite traer la carga horaria de un docente en todos los periodos activos.
	 * @author fgguzman
	 * @param docenteId - id de la persona.
	 * @param pracEstado - estado de los periodos.
	 * @return carga horaria activa
	 * @throws HoraClaseDtoException
	 * @throws HoraClaseDtoNoEncontradoException
	 */
	List<HoraClaseDto> buscarCargaHorariaPeriodosActivos(int docenteId, int pracEstado)throws HoraClaseDtoException, HoraClaseDtoNoEncontradoException ;
	
	
}
