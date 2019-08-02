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

 ARCHIVO:     SolicitudTerceraMatriculaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla SolicitudTerceraMatricula.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 31-ENE-2018            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.dtos.SolicitudTerceraMatriculaDto;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.SolicitudTerceraMatriculaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.SolicitudTerceraMatricula;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface SolicitudTerceraMatriculaServicio
 * Interfase que define las operaciones sobre la tabla SolicitudTerceraMatricula.
 * @author lmquishpei
 * @version 1.0
 */
public interface SolicitudTerceraMatriculaServicio {
	
	/**
	 * Busca una entidad SolicitudTerceraMatricula por su id
	 * @param id - de la SolicitudTerceraMatricula a buscar
	 * @return SolicitudTerceraMatricula con el id solicitado
	 * @throws SolicitudTerceraMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Persona con el id solicitado
	 * @throws SolicitudTerceraMatriculaException - Excepcion general
	 */
	public SolicitudTerceraMatricula buscarPorId(Integer id) throws SolicitudTerceraMatriculaNoEncontradoException, SolicitudTerceraMatriculaException;


	/**
	 * Busca si existe al menos una  entidad Sltrmt por  id del rces 
	 * @param idRces -id del Rces a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idRecordEstudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	public boolean buscarPorRcesId(Integer rcesId);
	
		
		
		/**
		 * Busca si existe al menos una  entidad Sltrmt por  SubId Sltrmtr, se ha realizado una apelacion de una solicitud
		 * @param idSltrmt -id del solicitudTerceraMatricula a buscar
		 * @return true si encuentra por lo menos una Sltrmt con idSltrmt o se produce una Excepcion y devuelve false si no encuentra. 
		 */
	public boolean buscarPorSltrmtId(Integer sltrmtId);
	

	/**
	 * ESTUDIANTES SAU: Busca si existe al menos una  entidad Sltrmt por  id del materia y la id de fichaEstudiante
	 * @param mtrId -id de la Materia a buscar
	 * @param fcesId -id de la FichaEstudiante a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idMateria y id Ficha estudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */

	public boolean buscarXMtrIdXFcesId(Integer mtrId, Integer fcesId) ;
	/**
	 * Crea la entidad SolicitudTerceraMatricula y edita RecordEstudiante
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @param estadoSltrmtSolicitud  -  estado del la solicitud a guardarse en la tabla solicitudTerceraMatricula
	 * @param estadoRcesSolicitud - estado de SolicitudTerceraMatri en la tabla RecordEstudiante
	 * @param periodoActivo - en que se crea la solicitud.
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	/**
	 * Busca si existe al menos una  entidad Sltrmt por  id del materia y la id de fichaEstudiante con estado del registro
	 * @param mtrId -id de la Materia a buscar
	 * @param fcesId -id de la FichaEstudiante a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idMateria y id Ficha estudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */

	public boolean buscarXMtrIdXFcesIdXEstado(Integer mtrId, Integer fcesId, Integer estadoRegistro)  ;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarSolicitudTerceraMatricula(List<MateriaDto> listMateria,  int estadoSltrmtSolicitud, PeriodoAcademico periodoActivo, Usuario usr) throws Exception, SolicitudTerceraMatriculaException;
	
	
	/**
	 * edita  estadoTerceraMatri de RecordEstudiante y edita estado de SolicitudTerceraMatricula.
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @param estadoSltrmtSolicitud  -  estado del la solicitud a guardarse en la tabla solicitudTerceraMatricula
	 * @param estadoRcesSolicitud - estado de SolicitudTerceraMatri en la tabla RecordEstudiante
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarVerificacionSolicitudTerceraMatricula(List<SolicitudTerceraMatriculaDto> listMateria,  int estadoSltrmtSolicitud, String usuario) throws Exception, SolicitudTerceraMatriculaException;

	/**
	 * edita  estadoTerceraMatri de RecordEstudiante y edita estado de SolicitudTerceraMatricula.
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarAprobacionSolicitudTerceraMatricula(List<SolicitudTerceraMatriculaDto> listMateria, int fcinId, String usuario, String observacionFinal) throws Exception, SolicitudTerceraMatriculaException;
	
	
	
	/**
	 * Crea la entidad SolicitudTerceraMatricula y edita RecordEstudiante para el caso de apelaciones
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @param estadoSltrmtSolicitud  -  estado del la solicitud a guardarse en la tabla solicitudTerceraMatricula
	 * @param estadoRcesSolicitud - estado de SolicitudTerceraMatri en la tabla RecordEstudiante
	 * @param periodoActivo - en que se crea la solicitud.
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarApelacionTerceraMatricula(List<MateriaDto> listMateria,  int estadoSltrmtSolicitud, PeriodoAcademico periodoActivo, Usuario usr) throws Exception, SolicitudTerceraMatriculaException;
	

	/**
	 * edita  estadoTerceraMatri de RecordEstudiante y edita estado de SolicitudTerceraMatricula.
	 * @param listMateria - Materias a solicitar tercera matricula
	 * @return True o False
	 * @throws Exception - Excepción general
	 * @throws SolicitudTerceraMatriculaException
	*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean generarAprobacionApelacionTerceraMatricula(List<SolicitudTerceraMatriculaDto> listMateria, int fcinId, String usuario , String  ObservacionFinal) throws Exception, SolicitudTerceraMatriculaException;
	
	/**
	 * Busca si existe al menos una  entidad Sltrmt por  id del materia y la id de fichaEstudiante
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param mtrId -id de la Materia a buscar
	 * @param fcesId -id de la FichaEstudiante a buscar
	 * @return true si encuentra por lo menos un SolicitudTerceraMatricula con idMateria y id Ficha estudiante o se produce una Excepcion y devuelve false si no encuentra. 
	 */
	public SolicitudTerceraMatricula buscarSolicitudXMtrIdXFcesId(Integer mtrId, Integer fcesId)  throws SolicitudTerceraMatriculaNoEncontradoException, SolicitudTerceraMatriculaValidacionException, SolicitudTerceraMatriculaException ;
	
	public SolicitudTerceraMatricula buscarSolicitudXMtrIdXFcesIdxEstado(Integer mtrId, Integer fcesId) throws SolicitudTerceraMatriculaNoEncontradoException, SolicitudTerceraMatriculaValidacionException, SolicitudTerceraMatriculaException;
	
}
