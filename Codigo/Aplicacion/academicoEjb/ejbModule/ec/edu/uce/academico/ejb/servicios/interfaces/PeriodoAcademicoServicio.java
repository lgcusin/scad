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

 ARCHIVO:     PeriodoAcademicoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla PeriodoAcademico.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 06-03-2017            David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Interface PeriodoAcademicoServicio
 * Interfase que define las operaciones sobre la tabla PeriodoAcademico.
 * @author darellano
 * @version 1.0
 */
public interface PeriodoAcademicoServicio {
	/**
	 * Busca una entidad PeriodoAcademico por su id
	 * @param id - del PeriodoAcademico a buscar
	 * @return PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoNoEncontradoException - Excepcion lanzada cuando no se encuentra un PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoException - Excepcion general
	 */
	public PeriodoAcademico buscarPorId(Integer id) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	public List<PeriodoAcademico> listarTodos();
	
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD en orden descendente por Id
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD en orden descendente por Id
	 */

	public List<PeriodoAcademico> listarTodosPreGradoOrdenadosXId();
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	public PeriodoAcademico buscarXestado(Integer estado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	public PeriodoAcademico buscarXestadoXtipoPeriodo(Integer estado, int tipoPeriodo) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	public List<PeriodoAcademico> listarXestadoXtipoPeriodoActivoEnCierre(int tipoPeriodo) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Añade un periodo academico en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 * @throws PeriodoAcademicoException - Excepción general
	 * @throws PeriodoAcademicoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * 
	 */
	public boolean anadir(PeriodoAcademico entidad, Integer tipoUsuario,  List<PeriodoAcademicoDto> entidades
			, List<PeriodoAcademicoDto> entidadesNivelacion) throws PeriodoAcademicoException, PeriodoAcademicoValidacionException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PeriodoAcademicoValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PeriodoAcademicoException - Excepción general
	 */
	public Boolean editar(PeriodoAcademico entidad) throws PeriodoAcademicoValidacionException, PeriodoAcademicoException;
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	public List<PeriodoAcademico> buscarXestadolist(Integer estado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	public List<PeriodoAcademico> listarTodosPosgrado();

	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD de acuerdo al usuario y postgrado
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD de acuerdo al usuario y postgrado
	 */
	public List<PeriodoAcademico> listarXUsuarioXPosgradoActivo(Integer crrId);
	
	/**
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	public PeriodoAcademico buscarXestadoPosgrado(Integer crrId,Integer pracEstado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**MQ
	 * Buscar la entidadas PeriodoAcademico que tengan un estado determinado
	 * @return entidad PeriodoAcademico que tengan un estado determinado
	 */
	
	public List<PeriodoAcademico> listarXestadoPosgrado(Integer crrId,Integer pracEstado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	

	public List<PeriodoAcademico> buscarXestadolistPosgrado(Integer estado)
			throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	public Boolean editarPosgrado(PeriodoAcademico entidad)
			 throws PeriodoAcademicoValidacionException, PeriodoAcademicoException;

	public List<PeriodoAcademico> listarTodosPosgradoActivo();
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	public List<PeriodoAcademico> buscarXestadoPracXtipoPrac(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	public List<PeriodoAcademico> buscarXestadoPracXtipoPracTodos(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado en cierre
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	public PeriodoAcademico buscarPeriodoEnCierre() throws PeriodoAcademicoNoEncontradoException;

	/**
	 * Desactiva la entidad PeriodoAcademico que tenga un estado en cierre
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	public boolean desactivarPeriodoEnCierre(PeriodoAcademico pracAux) throws PeriodoAcademicoNoEncontradoException;
	
	/**
	 * Busca una entidad PeriodoAcademico por su descripcion
	 * @param descripcion - del PeriodoAcademico a buscar
	 * @return PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoNoEncontradoException - Excepcion lanzada cuando no se encuentra un PeriodoAcademico con el id solicitado
	 * @throws PeriodoAcademicoException - Excepcion general
	 */
	public PeriodoAcademico buscarPDescripcion(String descripcion) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	List<PeriodoAcademico> listarTodosActivo();
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD
	 */
	public List<PeriodoAcademico> listarTodosActivoXtipoPeriodo(int tipoPeriodo);
	
	/**
	 * Buscar las entidades PeriodoAcademico que tengan un estado determinado y un tipo
	 * @return PeriodoAcademico que tengan un estado determinado.
	 */
	
	public PeriodoAcademico buscarPeriodoXestadoXtipo(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Buscar la entidades PeriodoAcademico que tengan un estado determinado
	 * @return Lista PeriodoAcademico que tengan un estado determinado
	 */
	public List<PeriodoAcademico> buscarPracEstadoEvaluacionXestadoPracXtipoPrac(Integer tipoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD con carga horaria
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD con carga horaria
	 */
	public List<PeriodoAcademico> listarTodosConCargaHoraria();

	
	/**
	 * Método que permite recuperar los periodos academicos segun tipos y estados.
	 * @author fgguzman
	 * @param pracTipo
	 * @param pracEstados 
	 * @return periodos académicos
	 */
	List<PeriodoAcademico> buscarPeriodos(int pracTipo, List<String> pracEstados) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	/**
	 * Método que permite recuperar un periodo academico segun su tipo y estado
	 * @author fgguzman
	 * @param pracTipo
	 * @param pracEstado
	 * @return objeto PeriodoAcademico
	 * @throws PeriodoAcademicoNoEncontradoException
	 * @throws PeriodoAcademicoValidacionException
	 * @throws PeriodoAcademicoException
	 */
	PeriodoAcademico buscarPeriodo(int pracTipo, int pracEstado) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoValidacionException, PeriodoAcademicoException;
	
	public PeriodoAcademico buscarPeriodoXTipoXEstado(Integer tipoPrac, Integer estadoPrac) throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoValidacionException, PeriodoAcademicoException;
	
	/**
	 * Método que permite buscar los periodos academicos que se encuentren vinculados a la evaluacion del desempeño.
	 * @author fgguzman
	 * @return periodos academicos
	 * @throws PeriodoAcademicoNoEncontradoException
	 * @throws PeriodoAcademicoException
	 */
	List<PeriodoAcademico> buscarPeriodosEvaluacionDesempeño() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD con retiros
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD con retiros
	 */
	public List<PeriodoAcademico> listarTodosConRetiros() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	PeriodoAcademico buscarXestadoSinExcepcion(Integer estado)
			throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	List<PeriodoAcademico> listarTodosPosgradoDesc();
	
	/**
	 * Lista todas las entidades PeriodoAcademico existentes en la BD con solicitudes y apelaciones de tercera matricula
	 * @return lista de todas las entidades PeriodoAcademico existentes en la BD con solicitudes y apelaciones de tercera matricula
	 */
	
	public List<PeriodoAcademico> listarTodosConTercerasMatriculas() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

	/**
	 * Método que permite buscar periodos academicos activos de la suficiencia en informatica.
	 * @return periodos academicos.
	 * @throws PeriodoAcademicoNoEncontradoException
	 * @throws PeriodoAcademicoException
	 */
	List<PeriodoAcademico> buscarPeriodosInformatica() throws PeriodoAcademicoNoEncontradoException, PeriodoAcademicoException;

}
