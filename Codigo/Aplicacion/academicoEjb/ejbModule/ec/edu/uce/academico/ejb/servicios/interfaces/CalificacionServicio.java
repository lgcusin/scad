/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     CalificacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Calificacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 12-JUN-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CalificacionNivelacionDto;
import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.CalificacionException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CalificacionValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.CalificacionModulo;

/**
 * Interface CalificacionServicio
 * Interfase que define las operaciones sobre la tabla Calificacion.
 * @author lmquishpei
 * @version 1.0
 */
public interface CalificacionServicio {
	/**
	 * Busca una entidad Calificacion por su id
	 * @param id - de la Calificacion a buscar
	 * @return Calificacion con el id solicitado
	 * @throws CalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Calificacion con el id solicitado
	 * @throws CalificacionException - Excepcion general
	 */
	public Calificacion buscarPorId(Integer id) throws CalificacionNoEncontradoException, CalificacionException;
	public CalificacionModulo buscarClmdPorId(Integer id) throws CalificacionNoEncontradoException, CalificacionException;
	/**
	 * Lista todas las entidades Calificacion existentes en la BD
	 * @return lista de todas las entidades Calificacion existentes en la BD
	 * @throws CalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Calificacion 
	 * @throws CalificacionException - Excepcion general
	 */
	public List<Calificacion> listarTodos() throws CalificacionNoEncontradoException , CalificacionException;
	
	public void guardarNotasPregradoPrimerHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente);
	
	public void guardarNotasPregradoSegundoHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException;
	
	public void guardoTemporalNotasPregradoPrimerHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException;
	
	public void guardoTemporalNotasPregradoSegundoHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante , String regCliente)  throws CalificacionValidacionException, CalificacionException;
	
	public void guardarNotasPregradoRecuperacion(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante , String regCliente);
	
	public void guardarNotasRectificacion(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente) throws CalificacionValidacionException, CalificacionException;;
	
	public void guardarNotasRectificacionRecuperacion(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente);

	public Boolean guardarEdicionNotasPrimerHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante,
			String regCliente) throws CalificacionValidacionException, CalificacionException, SQLIntegrityConstraintViolationException;
	
	public Boolean guardarEdicionNotasSegundoHemi(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante,
			String regCliente) throws CalificacionValidacionException, CalificacionException;

	public void guardarNotasPosgrado(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente);
	/**MQ
	 * Busca un calificacion por Id de recordEstudiante
	 * @param rcesId - id de la recordestudiante a buscar
	 * @return Calificacion. 
	 * 
	 */
	public Calificacion buscarPorRces(Integer rcestId) throws  CalificacionException;

	void verificarModulos(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante, String regCliente)
			throws CalificacionValidacionException, CalificacionException;

	public void eliminarCalificacionModulo(Integer clmdId) throws CalificacionException;

	Calificacion buscarPorRcesId(Integer rcesId) throws CalificacionException, CalificacionNoEncontradoException;

	void guardarCorreccion(Calificacion clfAux) throws CalificacionException, CalificacionNoEncontradoException;

	void guardarActualizarModulares(MateriaDto clfAux) throws CalificacionException, CalificacionNoEncontradoException;

	void guardarActualizarModularesConValorNull(MateriaDto clfAux)
			throws CalificacionException, CalificacionNoEncontradoException;

	void guardarNotasRectificacionRecuperacionModular(RecordEstudianteDto recordEstudianteDto,
			EstudianteJdbcDto estudiante, String regCliente);

	void guardarRecalculoModular(MateriaDto item) throws CalificacionException;

	List<CalificacionModulo> listarTodosModularesCorreccion()
			throws CalificacionNoEncontradoException, CalificacionException;

	void guardarActualizarModulares(CalificacionModulo clfAux)
			throws CalificacionException, CalificacionNoEncontradoException;

	void eliminarCalificacion(Integer clfId) throws CalificacionException;

	void eliminarCalificacionXId() throws CalificacionException;

	List<Calificacion> buscarCalificacionCorrecion();


	void guardarCorreccionFull(Integer clfId) throws CalificacionException, CalificacionNoEncontradoException;

	List<Calificacion> buscarCalificacionCorrecionNull();

	List<Calificacion> buscarCalificacionCorrecionIndividual();

	List<Calificacion> buscarCalificacionCorrecionPorPrac();
	void guardarCorreccionModularesFull(List<MateriaDto> lista)
			throws CalificacionException, CalificacionNoEncontradoException;
	List<CalificacionNivelacionDto> buscarCalificacionNivelacion(String identificacion);
	List<Calificacion> buscarCalificacionCorrecionIdiomas();
	void guardarCorreccionFullIdiomas(Integer clfId) throws CalificacionException, CalificacionNoEncontradoException;
	List<Calificacion> buscarCalificacionCorrecionCulturaFisica();
	void guardarCorreccionFullCulturaFisica(Integer clfId)
			throws CalificacionException, CalificacionNoEncontradoException;
	void correcionNivelacionError() throws CalificacionException;
	
	/**
	 * Método que permite regitrar la calificacion de estudiantes de la suficiencia en informatica.
	 * @author fgguzman
	 * @param estudiante
	 * @param cliente
	 * @return true si el registro fue almacenado con exito.
	 */
	boolean registrarCalificacion(PersonaDto estudiante, String cliente);
	Boolean guardarEdicionNotasPosgrado(RecordEstudianteDto recordEstudianteDto, EstudianteJdbcDto estudiante,
			String regCliente) throws CalificacionValidacionException, CalificacionException;

}
