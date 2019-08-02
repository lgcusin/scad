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

 ARCHIVO:     MallaCurricularServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla MallaCurricular.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.MallaPeriodo;

/**
 * Interface MallaCurricularServicio
 * Interfase que define las operaciones sobre la tabla MallaCurricular.
 * @author dcollaguazo
 * @version 1.0
 */
public interface MallaCurricularServicio {
	/**
	 * Busca una entidad MallaCurricular por su id
	 * @param id - de la MallaCurricular a buscar
	 * @return MallaCurricular con el id solicitado
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular con el id solicitado
	 * @throws MallaCurricularException - Excepcion general
	 */
	public MallaCurricular buscarPorId(Integer id) throws MallaCurricularNoEncontradoException, MallaCurricularException;

	/**
	 * Lista todas las entidades MallaCurricular existentes en la BD
	 * @return lista de todas las entidades MallaCurricular existentes en la BD
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
	 * @throws MallaCurricularException - Excepcion general
	 */
	public List<MallaCurricular> listarTodos() throws MallaCurricularNoEncontradoException , MallaCurricularException;
	
	/**
	 * Lista todas las entidades MallaCurricular por carrera por vigencia por estado
	 * @return la MallaCurricular por carrera por vigencia por estado
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
	 * @throws MallaCurricularException - Excepcion general
	 */
	public MallaCurricular buscarXcarreraXvigenciaXestado(int crrId, int vigencia, int estado) throws MallaCurricularNoEncontradoException , MallaCurricularException;
	
	/**
	 * Lista todas las entidades MallaCurricular por codigo de malla
	 * @return lista de todas las entidades MallaCurricular por carrera por codigo de malla
	 * @throws MallaCurricularNoEncontradoException - Excepcion lanzada cuando no se encuentra una MallaCurricular 
	 * @throws MallaCurricularException - Excepcion general
	 */
	public MallaCurricular buscarXcodigoMalla(String codigoMalla) throws MallaCurricularNoEncontradoException , MallaCurricularException;
	
	/**
	 * Edita todos los atributos de la entidad MallaCurricular
	 * @param entidad - entidad MallaCurricular a editar
	 * @return null si no se encuentró la entidad a editar, la entidad MallaCurricular editada de lo contrario
	 * @throws MallaCurricularException - excepcion de validacion de edicion
	 * @throws MallaCurricularNoEncontradoException - excepcion de validacion de edicion
	 */
	public MallaCurricular editar(MallaCurricularDto entidad, MallaPeriodo mlpr) throws MallaCurricularException , MallaCurricularNoEncontradoException;
	
	/**
	 * Edita todos los atributos de la entidad MallaCurricular
	 * @param entidad - entidad MallaCurricular a editar
	 * @return null si no se encuentró la entidad a editar, la entidad MallaCurricular editada de lo contrario
	 * @throws MallaCurricularException - excepcion de validacion de edicion
	 * @throws MallaCurricularNoEncontradoException - excepcion de validacion de edicion
	 */
	public MallaCurricular anadir(MallaCurricularDto entidad) throws MallaCurricularException , MallaCurricularNoEncontradoException;
	

	MallaCurricular editar(MallaCurricularDto entidad) throws MallaCurricularException , MallaCurricularNoEncontradoException;

}
