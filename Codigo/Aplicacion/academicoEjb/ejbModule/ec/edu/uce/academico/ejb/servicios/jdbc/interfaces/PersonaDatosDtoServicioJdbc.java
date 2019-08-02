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

 ARCHIVO:     PersonaDatosDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc del DTO PersonaDatosDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 30-AGOSTO-2017			Arturo Villafuerte				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.PersonaDatosDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;

/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc del DTO PersonaDatosDto.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface PersonaDatosDtoServicioJdbc {

	/**
	 * Método que lista los docentes qu se encuentren en una facultad por periodo academico, solo pregrado.
	 * @author fgguzman 
	 * @param dependenciaId - Id de la facultad a buscar 
	 * @param periodoId - Id de la carrera a buscar
	 * @return docentes del flujo dtps 
	 * @throws PersonaDatosDtoNoEncontradoException
	 * @throws PersonaDatosDtoException 
	 */
	List<PersonaDatosDto> buscarDocentesPorFacultadPeriodo(int dependenciaId, int periodoId) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Método que lista los docentes qu se encuentren en una facultad por periodo academico; nivelacion, pregrado, posgrado, suficiencias.
	 * @author fgguzman 
	 * @param param - identificacion o primer apellido.
	 * @param tipoBusqueda - por identificacion o por primer apellido.
	 * @return docentes del flujo dtps 
	 * @throws PersonaDatosDtoNoEncontradoException
	 * @throws PersonaDatosDtoException 
	 */
	List<PersonaDatosDto> buscarDocentesPorFacultadPeriodoMultipleNivel(String param, int tipoBusqueda) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Método que lista los docentes qu se encuentren en una carrera por periodo academico.
	 * @author fgguzman 
	 * @param dependenciaId - Id de la facultad a buscar 
	 * @param periodoId - Id de la carrera a buscar
	 * @return docentes del flujo dtps 
	 * @throws PersonaDatosDtoNoEncontradoException
	 * @throws PersonaDatosDtoException 
	 */
	List<PersonaDatosDto> buscarDocentesPorCarreraPeriodo(int dependenciaId, int periodoId) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	
	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param personaId
	 * @param periodoId
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarPorId(int personaId , int periodoId) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param idDocente ide del docente a buscar
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarPorIdEntidad(PersonaDatosDto docente) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Busca por id de docente el DTO PersonaDatosDTO con todos los datos del docente
	 * @param idDocente ide del docente a buscar
	 * @return Dto PersonaDatosDTO con la informacion del docente
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarPorIdPorTipoEntidad(PersonaDatosDto docente) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
     * Método que busca un docente por identificacion
     * @param identificacion -identificacion del postulante que se requiere buscar.
     * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
     */
    public List<PersonaDatosDto> buscarDocenteXIdentificacionXApellido(String identificacion, String apellido) throws PersonaDatosDtoException,PersonaDatosDtoNoEncontradoException;
    
    /**
     * Método que busca un docente por identificacion, dependiendo del tipo de carrera
     * @param identificacion -identificacion del postulante que se requiere buscar.
     * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
     * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
     */
    public List<PersonaDatosDto> buscarDocenteTipoCarreraXIdentificacionXApellido(String identificacion, String apellido) throws PersonaDatosDtoException,PersonaDatosDtoNoEncontradoException;
    
    /**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera o enlista todos los docentes 
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idCarrera - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXCarreraConCargaAcademica(int idCarrera, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera o enlista todos los directivos  
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idCarrera - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXCarreraConCargaAcademicaDirectivo(int idCarrera, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera/area o enlista todos los docentes 
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idArea - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXAreaConCargaAcademica(int idArea, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
		
	/** 
	 * Enlista los docentes que se encuentren en una facultad/ o impartan clases en esa facultad/Carrera/area o enlista todos los docentes pares academicos
	 * @param idPeriodo - Id de la periodo a buscar 
	 * @param idArea - Id de la carrera a buscar
	 * @param idRol - Id de rol a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public List<PersonaDatosDto> listarXAreaConCargaAcademicaParAcademico(int idArea, int idPeriodo, int idRol) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
    /**
	 * Busca docente con detalle puesto 
	 * @param idDetallePuesto - Id de detalle puesto a buscar  
	 * @return docente con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	public PersonaDatosDto buscarXDetallePuesto(int idDetallePuesto) throws PersonaDatosDtoNoEncontradoException, PersonaDatosDtoException;
	
	/**
	 * Método que realiza la busqueda de los docentes que estan asignados a un area
	 * @param materiaId - Id de la materia a buscar
	 * @param periodoId - Id del periodo a buscar
	 * @return - retorna la lista de persona datos dto que estan asignados al area 
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public List<PersonaDatosDto> listarXAreaXPeriodo(int materiaId, int periodoId) throws PersonaDatosDtoException, PersonaDatosDtoNoEncontradoException;
	
}
