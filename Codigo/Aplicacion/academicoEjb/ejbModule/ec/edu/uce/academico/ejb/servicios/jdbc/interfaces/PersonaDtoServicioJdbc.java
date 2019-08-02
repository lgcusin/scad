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
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla ParaleloDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 25-05-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;

/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Persona.
 * @author lmquishpei
 * @version 1.0
 */
public interface PersonaDtoServicioJdbc {

	/**
	 * Método que permite listar estudiantes que registraron matricula en un periodo academico.
	 * @author fgguzman
	 * @param periodoId - id del periodo.
	 * @param dependenciaId - id de la ependencia.
	 * @return estudiantes por facultad carrera.
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	List<PersonaDto> buscarEstudiantesRegistrados(int periodoId, int dependenciaId) throws PersonaNoEncontradoException, PersonaException;
	
	/**
	 * Método que permite listar estudiantes con sus calificaciones en modulos.
	 * @param mallaCurricularParaleloId - id de la mlcrpr del modulo.
	 * @return estudiantes con notas.
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarEstudiantesPorModuloParalelo(int mallaCurricularParaleloId) throws PersonaException, PersonaNoEncontradoException;
	
	/**
	 * Método que busca a la persona con el rol e identificacion requerida.
	 * @author fgguzman
	 * @param rolId
	 * @param identificacion
	 * @return
	 * @throws PersonaDtoException
	 * @throws PersonaDtoNoEncontradoException
	 */
	PersonaDto buscarPersonaPorIdentificacionRol(int rolId, String identificacion) throws PersonaException, PersonaNoEncontradoException, PersonaValidacionException;
	
	
	/**
	 * Método que lista los estudiantes que esten listos para el proceso de registro historico
	 * @param listaCarreras - listaCarreras lista de carreras que esta asignado
	 * @param idCarrera - idCarrera id de la carrera seleccionada 
	 * @param rol - rol rol del usuario que va ha consultar 
	 * @param tipoInscripcion - tipoInscripcion tipo de la inscripción
	 * @param estadoInscripcion - estadoInscripcion estado de la inscripción en este caso activa
	 * @param estadoPeriodo - estadoPeriodo estado del periodo a buscar
	 * @param identificacion - identificacion numero de identificación del estudiante a buscar
	 * @return retorna una lista de estudiantes para cargar el historico de matrícula
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public List<PersonaDto> listarXCarreraXRolXTipoInscripcionXestadoInscripcionXEstadoPeriodo(List<CarreraDto> listaCarreras, Integer idCarrera, int rol, int tipoInscripcion, int estadoInscripcion, int estadoPeriodo, String identificacion) throws PersonaDtoException, PersonaDtoNoEncontradoException;
	
	/**
	 * Método que busca a la persona con el rol director de carrera de estado activo
	 * @param rolDirCarrera .- id del rol a buscar
	 * @param crrId .- id de la carrera a buscar
	 * @return .- una persona con los parametros de busqueda ingresados
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public PersonaDto buscarPersonaProlDirCarreraPcrrId(int rolDirCarrera, int crrId) throws PersonaDtoException, PersonaDtoNoEncontradoException;
	
	/**
	 * Método que busca a la persona con el rol de decano de la facultad de estado activo
	 * @param rol .- id del rol a buscar
	 * @param fclId .- id de la facultad a buscar
	 * @param estadoRol.- id de la carrera a buscar
	 * @return .- una persona con los parametros de busqueda ingresados
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public PersonaDto buscarPersonaXRolIdXFclId(int rolId, int fclId) throws PersonaDtoException, PersonaDtoNoEncontradoException;
	
	
	/**
	 * Método que busca a la persona con el rol director de carrera por carrera
	 * @param crrId .- crrId  a buscar
	 * @return .- una persona con los parametros de busqueda ingresados
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public PersonaDto buscarDirectorCarreraxidCarrera(int crrId) throws PersonaDtoException, PersonaDtoNoEncontradoException;
	
	
	/**MQ
	 * Método que lista los estudiantes que esten listos para el proceso de registro historico
	 * @param listaCarreras - listaCarreras lista de carreras que esta asignado
	 * @param idCarrera - idCarrera id de la carrera seleccionada 
	 * @param rol - rol rol del usuario que va ha consultar 
	 * @param tipoInscripcion - tipoInscripcion tipo de la inscripción
	 * @param estadoInscripcion - estadoInscripcion estado de la inscripción en este caso activa
	 * @param identificacion - identificacion numero de identificación del estudiante a buscar
	 * @return retorna una lista de estudiantes para cargar el historico de matrícula
	 * @throws PersonaDtoException - PersonaDtoException excepción general lanzada
	 * @throws PersonaDtoNoEncontradoException - PersonaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 */
	public List<PersonaDto> listarXCarreraXRolXTipoInscripcionXestadoInscripcion(List<CarreraDto> listaCarreras, Integer idCarrera, int rol, int tipoInscripcion, int estadoInscripcion, String identificacion) throws PersonaDtoException, PersonaDtoNoEncontradoException;

	
	/**
	 * Método que permite recuperar a los docentes que posean carga horaria y se encuentren vinculados en un grupo.
	 * @author fgguzman
	 * @param periodo - id del periodo academico.
	 * @param carrera - id de la carrera.
	 * @param grupo - id del grupo
	 * @return docentes
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarDocentesPorGrupo(int periodo, int carrera, int grupo) throws PersonaException, PersonaNoEncontradoException;
	
	/**
	 * Método que permite recuperar a los docentes que posean carga horaria y se encuentren vinculados en una dependencia.
	 * @author fgguzman
	 * @param periodo - id del periodo academico.
	 * @param dependencia - id de la dependencia
	 * @param param - identificacion o primer apellido
	 * @param tipoBusqueda - 0 identificacion /1 primer apellido
	 * @return docentes
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarDocentesPorDependencia(int periodo, int dependencia, String param, int tipoBusqueda) throws PersonaException, PersonaNoEncontradoException;

	/**
	 * Método que permite recuperar a los docentes que posean carga horaria y se encuentren vinculados en un grupo.
	 * @author fgguzman
	 * @param crrTipo - tipo de la carrera {0-pre 1-pos 2-niv 3-suf}.
	 * @param tipoBusqueda - 0 identificacion / 1 - primer apellido
	 * @param param parametro de busqueda
	 * @return docentes
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarDocentesPorCarrera(int crrTipo, int tipoBusqueda, String param) throws PersonaException, PersonaNoEncontradoException;
	
	/**
	 * Método que permite encontrar estudiantes que han registrado matricula por carrera.
	 * @author fgguzman
	 * @param carreraId - id de la carrera.
	 * @return estudiantes que han registrado matricula.
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarEstudiantesPorCarrera(int carreraId) throws PersonaException, PersonaNoEncontradoException;
	
	/**
	 * Método que permite buscar un estudiante por cedula o  pasaporte.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte
	 * @return estudiante segun parametro
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarEstudiantePorIdentificacion(String identificacion) throws PersonaException, PersonaNoEncontradoException;

	
	/**
	 * Método que permite encontrar estudiantes que han registrado matricula por carrera.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte
	 * @param carreraId - id de la carrera.
	 * @return estudiantes que han registrado matricula.
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarEstudiantePorIdentificacionCarrera(String identificacion, int carreraId) throws PersonaException, PersonaNoEncontradoException;
	
	/**
	 * Método que permite encontrar estudiantes que han registrado matricula por asignatura y paralelo.
	 * @author fgguzman
	 * @param mallaCurricularParaleloId - id de la asignatura en el paralelo.
	 * @return estudiantes que han registrado matricula.
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarEstudiantesPorAsignaturaParalelo(int mallaCurricularParaleloId) throws PersonaException, PersonaNoEncontradoException;

	/**
	 * Método que permite buscar estudiantes matriculados en Suficiencia Informatica Exoneraciones
	 * @author fgguzman
	 * @param fecha - parametro solicitado
	 * @return estudiantes matriculados esa fecha
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	List<PersonaDto> buscarEstudiantesPorFechaExoneracion(String fecha) throws PersonaException, PersonaNoEncontradoException;
}
