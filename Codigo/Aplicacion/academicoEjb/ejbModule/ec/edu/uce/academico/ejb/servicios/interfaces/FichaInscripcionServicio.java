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

 ARCHIVO:     FichaEstudianteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla FichaEstudiante.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteValidacionException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.FichaInscripcionDtoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;

/**
 * Interface FichaInscripcionServicio Interfase que define las operaciones sobre
 * la tabla FichaInscripcion.
 * @author ghmafla
 * @version 1.0
 */
public interface FichaInscripcionServicio {

	public FichaInscripcion buscarXId(Integer fcinId);

	

	public void desactivarRegistrosUsuarioAprobadosSiac(List<EstudianteJdbcDto> listFcin) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException;

	public void activarMatriculaReprobadosNivelacion(List<EstudianteJdbcDto> listFcin) 	throws FichaEstudianteNoEncontradoException;

	public void modificarMatriculaPendientePasoNotas(Integer fcinId) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException;

	public void desactivarFichaInscripcion(Integer fcinId) throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException;

	/**
	 * Método que permite actualizar una ficha inscripcion segun su ID.
	 * @author fgguzman
	 * @param fcinId - ID Ficha Inscripcion para actualizar.
	 * @param estado - nuevo estado.
	 * @return - ficha inscripcion actualizada
	 * @throws FichaInscripcionDtoException
	 * @throws FichaInscripcionDtoNoEncontradoException
	 */
	public FichaInscripcion updateEstadoFichaInscripcion(int fcinId, int estado)throws  FichaInscripcionDtoException, FichaInscripcionDtoNoEncontradoException;

	public void desactivarFichaInscripcionPosgradoXFcesId(Integer fcesId)
			throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException;

	FichaInscripcion buscarFichaInscripcionNivelacion(String cedula)
			throws FichaEstudianteNoEncontradoException, FichaEstudianteException, FichaEstudianteValidacionException;

	
	/**
	 * Buscar la entidad FichaInscripcion que se encuentre en nivelacion y la
	 * actualiza a grado
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no hay datos
	 */
	void pormoverNivelacionGrado(List<Persona> listFcin) throws FichaEstudianteNoEncontradoException;
	
	/**
	 * Método que permite crear una ficha inscripcion en la suficiencias informatica y cultura fisica.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte
	 * @param configuracionCarrera - id de la cncr
	 * @param rol - pregrado o postgrado
	 * @throws FichaEstudianteNoEncontradoException
	 */
	 void crearFichaInscripcionSuficiencia(String identificacion, Integer configuracionCarrera, Integer rol, PeriodoAcademico periodo) throws FichaInscripcionDtoValidacionException, FichaEstudianteNoEncontradoException, FichaInscripcionDtoException , UsuarioRolNoEncontradoException, UsuarioRolValidacionException;


}
