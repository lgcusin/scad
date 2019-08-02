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

 ARCHIVO:     MateriaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Materia.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaException;
import ec.edu.uce.academico.ejb.excepciones.MateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;

/**
 * Interface MateriaServicio
 * Interfase que define las operaciones sobre la tabla Materia.
 * @author dcollaguazo
 * @version 1.0
 */
public interface MateriaServicio {
	/**
	 * Busca una entidad Materia por su id
	 * @param id - de la Materia a buscar
	 * @return Materia con el id solicitado
	 * @throws MateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Materia con el id solicitado
	 * @throws MateriaException - Excepcion general
	 */
	public Materia buscarPorId(Integer id) throws MateriaNoEncontradoException, MateriaException;

	/**
	 * Lista todas las entidades Materia existentes en la BD
	 * @return lista de todas las entidades Materia existentes en la BD
	 * @throws MateriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Materia 
	 * @throws MateriaException - Excepcion general
	 */
	public List<Materia> listarTodos() throws MateriaNoEncontradoException , MateriaException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws MateriaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException - Excepción general
	*/
	public Boolean editar(MateriaDto entidad) throws MateriaValidacionException , MateriaException;

	
	/**
	 * Añade una materia en la BD
	 * @return Si se añadio o no la materia
	 * @throws MateriaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws MateriaException - Excepción general
	 */
	
	public boolean anadir(MateriaDto entidad) throws MateriaValidacionException, MateriaException;
	
	/**
	 * Busca la materia por codigo y carrera
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param codigo - codigo de materia a buscar
	 * @param carrera - id de carrera a buscar
	 * @throws MateriaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException  - Excepcion lanzada no se controla el error
	 */
	public Materia buscarXCodigoXEspeCodigo(String codigo, int carrera) throws MateriaValidacionException, MateriaException;
	
	/**
	 * Busca la materia por codigo y carrera
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param codigo - codigo de materia a buscar
	 * @param carrera - id de carrera SIIU a buscar 
	 * @throws MateriaValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MateriaException  - Excepcion lanzada no se controla el error
	 */
	public Materia buscarXCodigoXCarrera(String codigo, int carrera) throws MateriaValidacionException, MateriaException;

	public List<Materia> listarTodosModulos(Integer mtrId) throws MateriaNoEncontradoException, MateriaException;

	public Materia buscarMateriaXMlCrPrPeriodoPregradoActivo(Integer mlcrpr)
			throws MateriaNoEncontradoException, MateriaException;

	List<Materia> listarTodosModularesXFacultad(Integer fclId) throws MateriaNoEncontradoException, MateriaException;
	
	/**
	 * Método que permite actualizar un registro en la tabla materia
	 * @author fgguzman
	 * @param entidad - Materia.class
	 * @return true si es exitoso
	 * @throws MateriaException
	 */
	boolean editarMateriaPorGrupo(Materia entidad) throws  MateriaException;

	/**
	 * Método que permite listar materias por carrera. 
	 * @author fgguzman
	 * @param carreraId - id de la carrera que solicita sus materias
	 * @return - materias
	 * @throws MateriaNoEncontradoException
	 * @throws MateriaException
	 */
	List<Materia> buscarMateriasPorCarrera(int carreraId) throws MateriaNoEncontradoException , MateriaException;
		
}
