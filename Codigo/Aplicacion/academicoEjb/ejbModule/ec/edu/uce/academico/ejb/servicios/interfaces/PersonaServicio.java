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

 ARCHIVO:     PersonaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Persona.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.sql.DatabaseMetaData;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.UbicacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;
import ec.edu.uce.academico.jpa.entidades.publico.Referencia;

/**
 * Interface PersonaServicio
 * Interfase que define las operaciones sobre la tabla Persona.
 * @author dcollaguazo
 * @version 1.0
 */
public interface PersonaServicio {
	/**
	 * Busca una entidad Persona por su id
	 * @param id - de la Persona a buscar
	 * @return Persona con el id solicitado
	 * @throws PersonaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Persona con el id solicitado
	 * @throws PersonaException - Excepcion general
	 */
	public Persona buscarPorId(Integer id) throws PersonaNoEncontradoException, PersonaException;

	/**
	 * Lista todas las entidades Persona existentes en la BD
	 * @return lista de todas las entidades Persona existentes en la BD
	 * @throws PersonaException - PersonaException Excepcion general
	 * @throws PersonaNoEncontradoException - PersonaNoEncontradoException Excepcion lanzada cuando no hay personas
	 */
	public List<Persona> listarTodos() throws PersonaException, PersonaNoEncontradoException ;

	
	/**
	 * Lista todas las entidades Persona existentes en la BD de acuerdo a su identificación
	 * @return busca las entidades Persona existentes en la BD
	 * @throws PersonaException - PersonaException Excepcion general
	 * @throws PersonaNoEncontradoException - PersonaNoEncontradoException Excepcion lanzada cuando no hay personas
	 */

	public Persona buscarPersonaPorIdentificacion(String identificacion) throws  PersonaException;
	
	/**
	 * Edita todos los atributos de la entidad persona, ficha estudiante
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws MateriaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException - Excepción general
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean actualizarEstudianteXSecretaria(EstudianteJdbcDto entidad, UbicacionDto cantonNacimiento, UbicacionDto parroquiaResiencia ) throws  PersonaException;

	
	/**
	 * Edita todos los atributos de la entidad persona por parte del estudiante
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaException - Excepción general
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean actualizarEstudianteXEstudiante(EstudianteJdbcDto entidad, UbicacionDto parroquiaResidencia, List<Referencia> listaReferencia, List<Referencia> listaReferenciaVer ) throws PersonaException;
	
	/**
	 * Edita el atributos de la entidad persona el campo prs_fecha_formulario_seguro
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaException - Excepción general
	*/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean actualizarFormularioSeguroPersona(EstudianteJdbcDto entidad) throws PersonaException ;

	public Persona editarXId(UsuarioRolJdbcDto entidad)	throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException;
	
	Persona buscarPorIdentificacion(String identificacion) throws PersonaNoEncontradoException, PersonaException;
	
	/**
	 * Método que permite buscar personas por identificacion.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte.
	 * @return personas que coinciden con el parametro de entrada.
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	List<Persona> listarPorIdentificacion(String identificacion) throws PersonaNoEncontradoException, PersonaException;

	/**
	 * Método que permite buscar personas por apellido paterno.
	 * @author fgguzman
	 * @param apellido - iniciales del apellido.
	 * @return personas que coinciden con el parametro de entrada.
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	List<Persona> listarPorApellidoPaterno(String apellido) throws PersonaNoEncontradoException, PersonaException;

	public DatabaseMetaData retornarDs();
	
}
