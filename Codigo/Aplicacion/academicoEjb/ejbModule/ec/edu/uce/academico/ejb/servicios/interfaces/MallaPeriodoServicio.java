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

 ARCHIVO:     MallaPeriodoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla MallaPeriodo.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 18-JUL-2017            Vinicio Rosales                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaPeriodoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;

/**
 * Interface MallaPeriodoServicio
 * Interfase que define las operaciones sobre la tabla MallaPeriodo.
 * @author jvrosales
 * @version 1.0
 */
public interface MallaPeriodoServicio {
	/**
	 * Busca una entidad MallaPeriodo por su id
	 * @param id - de la MallaPeriodo a buscar
	 * @return MallaPeriodo con el id solicitado
	 * @throws MallaPeriodoNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaPeriodo con el id solicitado
	 * @throws MallaPeriodoException - Excepcion general
	 */
	public MallaPeriodo buscarPorId(Integer id) throws MallaPeriodoNoEncontradoException, MallaPeriodoException;

	/**
	 * Lista todas las entidades MallaPeriodo existentes en la BD
	 * @return lista de todas las entidades MallaPeriodo existentes en la BD
	 * @throws MallaPeriodoNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaPeriodo 
	 * @throws MallaPeriodoException - Excepcion general
	 */
	public List<MallaPeriodo> listarTodos() throws MallaPeriodoNoEncontradoException , MallaPeriodoException;
	
	/**
	 * Añade una MallaPeriodo en la BD
	 * @return lista de todas las entidades MallaPeriodo existentes en la BD
	 * @throws MallaPeriodoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws MallaPeriodoException - Excepción general
	 */
	public MallaPeriodo anadir(MallaPeriodo entidad) throws MallaPeriodoValidacionException, MallaPeriodoException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws MallaPeriodoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws MallaPeriodoNoEncontradoException - Excepción lanzada si no se encontró la entidad a editar
	 * @throws MallaPeriodoException - Excepción general
	*/
	public MallaPeriodo editar(MallaPeriodo entidad) throws MallaPeriodoNoEncontradoException, MallaPeriodoException;
//	
//	/**
//	 * Lista todas las entidades MallaCurricular por carrera por vigencia por estado
//	 * @return la MallaCurricular por carrera por vigencia por estado
//	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
//	 * @throws MallaCurricularException - Excepcion general
//	 */
//	public MallaCurricular buscarXcarreraXvigenciaXestado(int crrId, int vigencia, int estado) throws MallaCurricularNoEncontradoException , MallaCurricularException;
//	
//	/**
//	 * Lista todas las entidades MallaCurricular por codigo de malla
//	 * @return lista de todas las entidades MallaCurricular por carrera por codigo de malla
//	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
//	 * @throws MallaCurricularException - Excepcion general
//	 */
//	public MallaCurricular buscarXcodigoMalla(String codigoMalla) throws MallaCurricularNoEncontradoException , MallaCurricularException;
//	
//	/**
//	 * Edita todos los atributos de la entidad MallaCurricular
//	 * @param entidad - entidad MallaCurricular a editar
//	 * @return null si no se encuentró la entidad a editar, la entidad MallaCurricular editada de lo contrario
//	 * @throws MallaCurricularException - excepcion de validacion de edicion
//	 * @throws MallaCurricularNoEncontradoException - excepcion de validacion de edicion
//	 */
//	public MallaCurricular editar(MallaCurricularDto entidad) throws MallaCurricularException , MallaCurricularNoEncontradoException;
//	
//	/**
//	 * Edita todos los atributos de la entidad MallaCurricular
//	 * @param entidad - entidad MallaCurricular a editar
//	 * @return null si no se encuentró la entidad a editar, la entidad MallaCurricular editada de lo contrario
//	 * @throws MallaCurricularException - excepcion de validacion de edicion
//	 * @throws MallaCurricularNoEncontradoException - excepcion de validacion de edicion
//	 */
//	public MallaCurricular anadir(MallaCurricularDto entidad) throws MallaCurricularException , MallaCurricularNoEncontradoException;
	
	/**
	 * Retorna la entidad buscada con los parametros idMalla, idPeriodo
	 * @param idMalla id de la malla a buscar
	 * @param idPeriodo id del periodo academico a buscar
	 * @return la entidad con los parametros ingresados
	 * @throws MallaPeriodoNoEncontradoException Excepción lanzada si no se encontró la entidad
	 * @throws MallaPeriodoException Excepción general
	 */
	public MallaPeriodo buscarxPeriodoxMallaCurricular(int idMalla, int idPeriodo) throws MallaPeriodoNoEncontradoException , MallaPeriodoException;
	
	/**
	 * Retorna la entidad buscada con los parametros idMalla
	 * @param idMalla id de la malla a buscar
	 * @return la entidad con los parametros ingresados
	 * @throws MallaPeriodoNoEncontradoException Excepción lanzada si no se encontró la entidad
	 * @throws MallaPeriodoException Excepción general
	 */
	public MallaPeriodo buscarxMallaCurricular(int idMalla) throws MallaPeriodoNoEncontradoException , MallaPeriodoException;
	
	/**
	 * Actualiza la entidad buscada con los parametros pracId
	 * @param pracId id de la malla a buscar
	 * @return la entidad con los parametros ingresados
	 * @throws MallaPeriodoNoEncontradoException Excepción lanzada si no se encontró la entidad
	 * @throws MallaPeriodoException Excepción general
	 */
	public void actualizaMallaPeriodo(int pracId) throws MallaPeriodoNoEncontradoException , MallaPeriodoException;

}
