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

 ARCHIVO:     CargaHorariaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla CargaHoraria.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.HorarioAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CargaHorariaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.CargaHoraria;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularParalelo;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Interface CargaHorariaServicio
 * Interfase que define las operaciones sobre la tabla CargaHoraria.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface CargaHorariaServicio {
	/**
	 * Busca una entidad CargaHoraria por su id
	 * @author Arturo VIllafuerte
	 * @param id - de la CargaHoraria a buscar
	 * @return CargaHoraria con el id solicitado
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria con el id solicitado
	 * @throws CargaHorariaException - Excepcion general
	 */
	public CargaHoraria buscarPorId(Integer id) throws CargaHorariaNoEncontradoException, CargaHorariaException;

	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD
	 * @author Arturo VIllafuerte
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */
	public List<CargaHoraria> listarTodos() throws CargaHorariaNoEncontradoException , CargaHorariaException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @author Arturo VIllafuerte
	 * @param entidad - entidad a editar
	 * @return true si se guardo la entidad editada, false si no se guardo
	 * @throws CargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws CargaHorariaException - Excepción general
	*/
	public Boolean editar(CargaHoraria entidad) throws CargaHorariaValidacionException , CargaHorariaException;
	
	/**
	 * Añade una carga horaria en la BD
	 * @author Arturo VIllafuerte
	 * @return Si se añadio o no la carga horaria
	 * @throws CargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws CargaHorariaException - Excepción general
	 */
	public CargaHoraria anadir(CargaHoraria entidad) throws CargaHorariaValidacionException, CargaHorariaException;
	
	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD por detalle puesto
	 * @author Arturo VIllafuerte
	 * @param dtpsId Id del detalle puesto a ser buscado
	 * @param tfcrhrId Id de tipo funcion carga horaria a buscar
	 * @param pracId Id del periodo en el cual se va a realizar carga horaria
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */
	public List<CargaHoraria> buscarPorDetallePuesto(int dtpsId, int tfcrhrId, int pracId) throws CargaHorariaNoEncontradoException , CargaHorariaException;
	
	
	public CargaHoraria buscarCahrPorDetallePuesto(int dtpsId, int tfcrhrId, int pracId) throws CargaHorariaNoEncontradoException , CargaHorariaException, CargaHorariaValidacionException;

	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD por tipo de funcion carga horaria
	 * @author Arturo VIllafuerte
	 * @param tfcrhrId Id de tipo funcion carga horaria a buscar
	 * @param pracId Id del periodo en el cual se va a realizar carga horaria
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */ 
	public List<CargaHoraria> buscarPorTipFncCrHr(int tfcrhrId, int pracId) throws CargaHorariaNoEncontradoException , CargaHorariaException;
	
	/**
	 * Lista todas las entidades CargaHoraria existentes en la BD por malla curricular materia
	 * @author Arturo VIllafuerte
	 * @param prlId Id de paralelo a buscar
	 * @param pracId peridodo academico a buscar
	 * @param mlcrprId Id de malla curricular paralelo a buscar 
	 * @return lista de todas las entidades CargaHoraria existentes en la BD
	 * @throws CargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CargaHoraria 
	 * @throws CargaHorariaException - Excepcion general
	 */  
	public CargaHoraria buscarPorMlCrMtXPeriodoXParalelo(int mlcrmtId, int pracId, int prlId) throws CargaHorariaNoEncontradoException , CargaHorariaException;
	
	/**
	 * Método que sirve para agregar una nueva carga horaria
	 * @param dtps - dtps, entidad detalle puesto
	 * @param mlcrpr - mlcrpr entidad malla curricular paralelo
	 * @param prac - prac entidad, periodo academico
	 * @param numHoras - numHoras número de horas de la materia
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws CargaHorariaException - CargaHorariaException, excepción genearal.
	 */
	public boolean nuevo(DetallePuesto dtps, MallaCurricularParalelo mlcrpr, PeriodoAcademico prac, Integer numHoras, Integer numCreditos) throws CargaHorariaException, Exception;
	
	/**
	 * Método que sirve para agregar una nueva carga horaria
	 * @param dtps - dtps, entidad detalle puesto
	 * @param mlcrpr - mlcrpr entidad malla curricular paralelo
	 * @param prac - prac entidad, periodo academico
	 * @param numHoras - numHoras número de horas de la materia
	 * @param numCreditos - numCreditos número de creditos de la materia
	 * @return, retorna verdadero si se ejecuto la transacción de nuevo
	 * @throws Exception - Exception excepción general
	 * @throws CargaHorariaException - CargaHorariaException, excepción genearal.
	 */
	public boolean editarDocente(Integer crhrId, Integer dtpsId, Integer mlcrprId, Integer pracId, Integer numHoras, Integer numCreditos) throws CargaHorariaException, Exception;
	
	/**
	 * Método que sirve para eliminar un docente de la carga horaria
	 * @param crhrId - crhrId, id de la carga horaria a eliminar
	 * @param listaEliminarMlcrprDocente - listaEliminarMlcrprDocente, lista de los horarios academicos a ser eliminados el docente
	 * @param mlcrprIdPadre - mlcrprIdPadre, id de la mallacurricular que se encuentra en mlcrprIdComp
	 * @return, retorna verdadero si se ejecuto la eliminacion
	 * @throws Exception - Exception excepción general
	 * @throws CargaHorariaException - CargaHorariaException, excepción genearal.
	 */
	public boolean eliminarDocente(int crhrId, List<HorarioAcademicoDto> listaEliminarMlcrprDocente, int mlcrprIdPadre) throws CargaHorariaException, Exception;

	/**
	 * Método que permite encontrar la Carga Horaria de un Docente asignada a una Materia. 
	 * @param mlcrprId - id de la Materia asociada al Paralelo.
	 * @param cahrEstado - true para casos vigentes.
	 * @return - Carga Horaria del Docente vinculado a la Asignatura -Paralelo.
	 * @throws CargaHorariaValidacionException
	 * @throws CargaHorariaNoEncontradoException
	 * @throws CargaHorariaException
	 */
	CargaHoraria buscarPorMallaCurricularParalelo(int mlcrprId, int cahrEstado) throws CargaHorariaNoEncontradoException , CargaHorariaValidacionException, CargaHorariaException;
	
	/**
	 * 
	 * @author fgguzman
	 * @param entidad
	 * @return
	 * @throws CargaHorariaValidacionException
	 * @throws CargaHorariaNoEncontradoException
	 * @throws CargaHorariaException
	 */
	boolean editarPorHorarioAcademico(CargaHoraria entidad) throws CargaHorariaValidacionException , CargaHorariaNoEncontradoException, CargaHorariaException;
}

