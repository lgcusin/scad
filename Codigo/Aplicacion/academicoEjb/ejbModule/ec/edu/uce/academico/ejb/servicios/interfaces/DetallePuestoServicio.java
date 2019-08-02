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

 ARCHIVO:     DetallePuestoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla DetallePuesto.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 26-SEPT-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.DetallePuestoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetallePuestoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;

/**
 * Interface DetallePuestoServicio
 * Interfase que define las operaciones sobre la tabla DetallePuesto.
 * @author dcollaguazo
 * @version 1.0
 */
public interface DetallePuestoServicio {
	
	/**
	 * Busca una entidad DetallePuesto por su id
	 * @param id - de la DetallePuesto a buscar
	 * @return FichaEstudiante con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorId(Integer id) throws DetallePuestoNoEncontradoException, DetallePuestoException ;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return true si se guardo la entidad editada, false si no se guardo
	 * @throws DetallePuestoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws DetallePuestoException - Excepción general
	*/
	public Boolean editar(DetallePuesto entidad) throws DetallePuestoValidacionException , DetallePuestoException;

	/**
	 * Busca una entidad DetallePuesto por su Prsid
	 * @param id - de la DetallePuesto a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcId(Integer fcdcId, Integer tipoCarrera) throws DetallePuestoNoEncontradoException, DetallePuestoException;
	
	/**
	 * Busca una entidad DetallePuesto por su Prsid
	 * @param id - de la DetallePuesto a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdVerifica(Integer fcdcId, Integer tipoCarrera) throws DetallePuestoNoEncontradoException, DetallePuestoException ;
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente
	 * @param id - de la ficha docente a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdPostgradoUotro(Integer fcdcId, Integer crrId) throws DetallePuestoNoEncontradoException, DetallePuestoException ;
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente
	 * @param id - de la ficha docente a buscar
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorario(Integer fcdcId, Integer tipoCarrera) throws DetallePuestoNoEncontradoException, DetallePuestoException ;
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioPosgrado(Integer fcdcId, Integer tipoCarrera, Integer crrId) throws DetallePuestoNoEncontradoException, DetallePuestoException ;
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioPosgradoAux(Integer fcdcId, Integer tipoCarrera, Integer crrId) throws DetallePuestoNoEncontradoException, DetallePuestoException;
	
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioSuficiencias(Integer fcdcId, Integer tipoCarrera, Integer dpnId) throws DetallePuestoNoEncontradoException, DetallePuestoException ;
	
	/**
	 * Busca una entidad DetallePuesto por ficha docente para suficiencias
	 * @param fcdcId - de la ficha docente a buscar
	 * @param tipoCarrera - tipo de carrera
	 * @param dpnId - dependencia para saber que que suficiencia es
	 * @return DetallePuesto con el id solicitado
	 * @throws DetallePuestoNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetallePuesto con el id solicitado
	 * @throws DetallePuestoException - Excepcion general
	 */
	public DetallePuesto buscarPorFcdcIdXIdCarreraXTipoCarreraHonorarioSuficienciasAux(Integer fcdcId, Integer tipoCarrera, Integer dpnId) throws DetallePuestoNoEncontradoException, DetallePuestoException;
	
	public Boolean agregar(DetallePuesto entidad) throws DetallePuestoValidacionException , DetallePuestoException ;
	
	/**
	 * Método que permite buscar el DetallePuesto en un periodo determinado.   
	 * @author fgguzman
	 * @param personaId - id de la persona.
	 * @param periodoId - id del periodo academico.
	 * @return objeto DetallePuesto
	 * @throws DetallePuestoNoEncontradoException
	 * @throws DetallePuestoValidacionException
	 * @throws DetallePuestoException
	 */
	DetallePuesto buscarDetallePuesto(Integer personaId, Integer periodoId) throws DetallePuestoNoEncontradoException, DetallePuestoValidacionException, DetallePuestoException ;

	/**
	 * Método que permite guardar un registro en la tabla detallepuesto.
	 * @author fgguzman
	 * @param entidad - detalle puesto.
	 * @return objeto DetallePuesto
	 * @throws DetallePuestoValidacionException
	 * @throws DetallePuestoException
	 */
	DetallePuesto guardar(DetallePuesto entidad) throws DetallePuestoValidacionException , DetallePuestoException ;
	
	/**
	 * Método que permite desactivar las fichas del docente segun el tipo de carrera.
	 * @author fgguzman
	 * @param fichaDocente
	 * @param tipoCarrera
	 * @return true si se desactivo con exito.
	 * @throws DetallePuestoValidacionException
	 * @throws DetallePuestoException
	 */
	boolean desactivarDetallePuesto(int fichaDocente,  int tipoCarrera) throws DetallePuestoValidacionException , DetallePuestoException ;
	
}
