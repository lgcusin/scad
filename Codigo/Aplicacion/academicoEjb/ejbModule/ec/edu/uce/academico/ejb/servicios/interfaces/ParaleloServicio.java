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

 ARCHIVO:     ParaleloServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Paralelo.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
27-04-2017            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;
import java.util.List;

import ec.edu.uce.academico.ejb.dtos.HoraClaseDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.ParaleloDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.ParaleloException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Interface ParaleloServicio
 * Interfase que define las operaciones sobre la tabla Paralelo.
 * @author lmquishpei
 * @version 1.0
 */
public interface ParaleloServicio {
	/**
	 * Busca una entidad Carrera por su id
	 * @param id - del Paralelo a a buscar
	 * @return Paralelo con el id solicitado
	 * @throws ParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws ParaleloException - Excepcion general
	 */
	//public Paralelo buscarPorId(Integer id) throws ParaleloNoEncontradoException, ParaleloException;

	/**
	 * Lista todas las entidades Paralelo existentes en la BD
	 * @return lista de todas las entidades Paralelo existentes en la BD
	 * @throws ParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra un Paralelo
	 * @throws ParaleloException - Excepcion general
	 */
	//public List<Paralelo> listarTodos() throws ParaleloNoEncontradoException , ParaleloException;
	
	
	/**
	 * Permite añadir l Paralelo
	 * @param entidad ParaleloDto a añadir
	 * @return True o False si fue editada la entidad paralelo existente en la BD
	 * @throws ParaleloException 
	 * @throws ParaleloNoEncontradoException 
	 * @throws ParaleloValidacionException 
	 */
		
	
	
	public boolean anadir(ParaleloDto entidad , PeriodoAcademico periodo, Carrera carrera) throws ParaleloValidacionException, ParaleloException;
	
	/**
	 * Permite editar la entidad Paralelo
	 * @param entidad ParaleloDto a editar
	 * @return True o False si fue editada la entidad paralelo existente en la BD
	 * @throws ParaleloException 
	 * @throws ParaleloNoEncontradoException 
	 * @throws ParaleloValidacionException 
	 */

	public Boolean editar(ParaleloDto entidad) throws  ParaleloValidacionException, ParaleloException;
	
	/**
	 * Permite editar los parametros de la entidad Paralelo 
	 * @param parametros a  editar
	 * @return True o False si fue editada la entidad paralelo existente en la BD
	 * @throws ParaleloException 
	 * @throws ParaleloNoEncontradoException 
	 * @throws ParaleloValidacionException 
	 */

	
	public Boolean editarXParametros(int prlId, Integer cupoNuevo, String descripcionNuevo, String codigoNuevo, Integer estadoNuevo ) throws  ParaleloValidacionException, ParaleloNoEncontradoException, ParaleloException;

	
	/**
	 * Añade un Paralelo y sus mallas curriculares en la BD 
	 * @return Si se añadio o no el paralelo
	 * @throws ParaleloValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws ParaleloException - Excepción general
	 */
	public boolean anadirParaleloMlCrPr(ParaleloDto entidad, List<MallaCurricularDto> listaMallaCurricular)
			throws ParaleloValidacionException, ParaleloException;

	/**
	 * Permite desactivar el paralelo que se seleccionó 
	 * @param parametros a  editar
	 * @return True o False si fue eliminada la entidad paralelo existente en la BD
	 * @throws ParaleloException 
	 * @throws ParaleloNoEncontradoException 
	 * @throws ParaleloValidacionException 
	 */
	public Boolean desactivarPprlId(Integer prlId) throws ParaleloNoEncontradoException, ParaleloException;

	/**
	 * Permite activar el paralelo que se seleccionó 
	 * @param parametros a  editar
	 * @return True o False si fue eliminada la entidad paralelo existente en la BD
	 * @throws ParaleloException 
	 * @throws ParaleloNoEncontradoException 
	 * @throws ParaleloValidacionException 
	 */
	public Boolean activarPprlId(Integer prlId) throws ParaleloNoEncontradoException, ParaleloException;

	public boolean anadirMallasAParalelo(ParaleloDto entidad, List<MallaCurricularDto> listaMallaCurricular)
			throws ParaleloValidacionException, ParaleloException;
		
	/**
	 * Busca una entidad Paralelo por su id
	 * @param id - del Edificio a buscar
	 * @return Paralelo con el id solicitado
	 * @throws ParaleloNoEncontradoException - Excepcion lanzada cuando no se encuentra un Edificio con el id solicitado
	 * @throws ParaleloException - Excepcion general
	 */
	
	public Paralelo buscarPorId(Integer id) throws ParaleloNoEncontradoException, ParaleloException;

	public boolean anadirParaleloMlCrPrNivelacion(ParaleloDto entidad, List<MallaCurricularDto> listaMallaCurricular) throws ParaleloValidacionException, ParaleloException;
	
	/**
	 * Método que permite crear un paralelo
	 * @author fgguzman
	 * @param paraleloDto - entidad ParaleloDto
	 * @param materias - materias para agregar
	 * @return true - si la agregacion es exito.
	 * @throws ParaleloValidacionException
	 * @throws ParaleloException
	 */
	boolean crearParalelo(ParaleloDto paraleloDto, List<MateriaDto> materias) throws ParaleloException ;
	
	/**
	 * Método que permite modificar el estado de un paralelo y modificar las modalides asociadas a la materia.
	 * @author fgguzman
	 * @param entidad - ParaleloDto
	 * @return true si la actualizacion es correcta.
	 * @throws ParaleloNoEncontradoException
	 * @throws ParaleloException
	 */
	boolean editarParalelo(ParaleloDto entidad) throws ParaleloNoEncontradoException, ParaleloException;
	
	/**
	 * Método que permite agregar una Asignatura a un Paralelo.
	 * @author fgguzman
	 * @param paralelo
	 * @param materia
	 * @return true si la agregacion es exitosa.
	 * @throws ParaleloException
	 */
	boolean agregarMateriaAparalelo(ParaleloDto paralelo, MateriaDto materia) throws ParaleloException ;
	
	/**
	 * Método que permite eliminar una Materia de un Paralelo en especifico.
	 * @param materiaDto - materiaDto
	 * @param paraleloId - id del Paralelo
	 * @return true si le eliminacion es exitosa.
	 * @throws ParaleloException
	 */
	boolean eliminarMateriaDelParalelo(MateriaDto materiaDto, int paraleloId) throws ParaleloException ;
	
	
	boolean crearParaleloExoneracion(ParaleloDto paraleloDto, MateriaDto materiaDto, List<HoraClaseDto> horario, PersonaDto docente) throws ParaleloException;
}
