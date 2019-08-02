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

 ARCHIVO:     DetalleMatriculaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla DetalleMatricula.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-OCT-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.DetalleMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;

/**
 * Interface DetalleMatricula
 * Interfase que define las operaciones sobre la tabla DetalleMatricula.
 * @author dcollaguazo
 * @version 1.0
 */
public interface DetalleMatriculaServicio {
	/**
	 * Busca una entidad DetalleMatricula por su id
	 * @param id - de la DetalleMatricula a buscar
	 * @return DetalleMatricula con el id solicitado
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula con el id solicitado
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	public DetalleMatricula buscarPorId(Integer id) throws DetalleMatriculaNoEncontradoException, DetalleMatriculaException;

	/**
	 * Lista todas las entidades DetalleMatricula existentes en la BD
	 * @return lista de todas las entidades DetalleMatricula existentes en la BD
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula 
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	public List<DetalleMatricula> listarTodos() throws DetalleMatriculaNoEncontradoException , DetalleMatriculaException;
	
	/**
	 * Método que actualiza la entidad de detalle matricula con cambios de paralelo.
	 * @author fgguzman
	 * @param detalleVigente - detalle matricula en el que se matriculo.
	 * @param recordVigente - record estudiante en el que se matriculo.
	 * @param mlcrprIdNuevo - id de la mallaCurricularParalelo Nueva
	 * @return true si las tres tablas se actualizan correctamente.
	 * @throws DetalleMatriculaNoEncontradoException
	 * @throws DetalleMatriculaException
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 * @throws MallaCurricularParaleloDtoNoEncontradoException
	 * @throws MallaCurricularParaleloDtoException
	 */
	public boolean editar(DetalleMatriculaDto detalleVigente,  RecordEstudianteDto recordVigente, int mlcrprIdNuevo) throws DetalleMatriculaNoEncontradoException, DetalleMatriculaException, RecordEstudianteNoEncontradoException, RecordEstudianteException, MallaCurricularParaleloDtoNoEncontradoException, MallaCurricularParaleloDtoException;	
	
	/**
	 * Método que elimina el detalle matricula 
	 * @throws DetalleMatriculaNoEncontradoException 
	 * @throws DetalleMatriculaException 
	 */
	public boolean eliminarFull(int dtmtId, int cmpgId, int rcesId, int mlcrprId, List<MateriaDto> listaMaterias, Gratuidad gratuidad, int tipoGratuidadId, int rcesEstado, int fcmtId, Nivel nivel) throws DetalleMatriculaNoEncontradoException, DetalleMatriculaException;
	
	/**
	 * Lista todas las entidades DetalleMatricula existentes en la BD, por comprobante de pago
	 * @return lista de todas las entidades DetalleMatricula existentes en la BD, por comprobante de pago
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula 
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	
	public List<DetalleMatricula> listarXCmpaId(Integer cmpaId) throws DetalleMatriculaException;
	
	/**MQ
	 * Lista todas la entidad DetalleMatricula existentes en la BD, por comprobante de pago y malla curricular paralelo
	 * @return la entidad DetalleMatricula existentes en la BD, por comprobante de pago y malla curricular paralelo
	 * @throws DetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleMatricula 
	 * @throws DetalleMatriculaException - Excepcion general
	 */
	public DetalleMatricula buscarXCmpaIdxMlcrprId(Integer cmpaId, Integer mlcrprId) throws  DetalleMatriculaException, DetalleMatriculaNoEncontradoException;
}
