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

 ARCHIVO:     RecordEstudianteDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla RecordEstudianteDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 15-03-2017			David Arellano					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.FichaEstudianteDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteDtoNoEncontradoException;


/**
 * Interface RecordEstudianteDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla RecordEstudianteDto.
 * @author darellano
 * @version 1.0
 */

public interface RecordEstudianteDtoServicioJdbc {

	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacion(String personaIdentificacion) throws RecordEstudianteDtoException;
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona y la carrera seleccionada
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - Id de la carrera a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXcarrera(String personaIdentificacion, int carreraId) throws RecordEstudianteDtoException;
	
	/**
	 * MQ:
	 * Realiza la busqueda del record academico por la identificacion de la persona y la carrera seleccionada Asisgnaturas en todos los estados.
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param carreraId - Id de la carrera a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXcarreraTodas(String personaIdentificacion, int carreraId) throws RecordEstudianteDtoException;
	
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona y el estado del record
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @param estadoid - id del estado a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXestado(String personaIdentificacion, int estadoid) throws RecordEstudianteDtoException;
	
	/**
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public RecordEstudianteDto buscarXFcesIdXMlcrprXPracActivo(int fcesId, int mlcrprId) throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de un RecordEstudianteDto por id
	 * @param fcesId - id del ficha_estudiante
	 * @return mlcrprId con el id malla_curricular_paralelo 
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public RecordEstudianteDto buscarXFcesIdXMlcrprXPracActivoCambio(int fcesId, int mlcrprId) throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException;	
		
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona y la carrera seleccionada
	 * @param personaIdentificacion - String de la indentificación de la persona a buscar
	 * @return Lista de RecordEstudianteDto que corresponde a la persona buscada
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<RecordEstudianteDto> buscarXidentificacionXpracActivoXprcacEnCierre(String personaIdentificacion) throws RecordEstudianteDtoException;

	
	
	/**
	 * Realiza la busqueda del record academico por la identificacion de la persona en un periodo activo.
	 * @param identificacion - cedula o pasaporte del estudiante.
	 * @return Record estudiantil.
	 * @throws RecordEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 * @throws RecordEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 */
	public List<MateriaDto> buscarRecordEstudiantePorIdentificacionPeriodoActivo(String identificacion) throws RecordEstudianteDtoNoEncontradoException, RecordEstudianteDtoException;

	RecordEstudianteDto buscarXFcesIdXMlcrprXPracEnCierreCambio(int fcesId, int mlcrprId)
			throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException;

	RecordEstudianteDto buscarXRcesId(int rcesId)
			throws RecordEstudianteDtoException, RecordEstudianteDtoNoEncontradoException;

	List<RecordEstudianteDto> listarRecordEstudianteRecuperacion();



	void actualizarRecordEstudianteDetalleMatricula(Integer rcesId, Integer dtmtId, Integer mlcrprIdNuevo)
			throws FichaEstudianteDtoException, FichaEstudianteDtoNoEncontradoException;

	void eliminarRecordEstudianteDetalleMatricula(Integer rcesId, Integer dtmtId)
			throws FichaEstudianteDtoException, FichaEstudianteDtoNoEncontradoException;


	void insertarRecordEstudianteDetalleMatricula(Integer rcesId, Integer dtmtId, Integer mlcrprId,
			Integer numMatricula) throws FichaEstudianteDtoException, FichaEstudianteDtoNoEncontradoException;

	List<FichaEstudianteDto> buscarRecordEstudianteDetalleMatricula(String prsIdentificacion, Integer pracId)
			throws FichaEstudianteDtoException, FichaEstudianteDtoNoEncontradoException;

	Integer buscarDetalleMatricula(Integer mlcrprId, Integer fcesId, Integer pracId)
			throws FichaEstudianteDtoException, FichaEstudianteDtoNoEncontradoException;
}
