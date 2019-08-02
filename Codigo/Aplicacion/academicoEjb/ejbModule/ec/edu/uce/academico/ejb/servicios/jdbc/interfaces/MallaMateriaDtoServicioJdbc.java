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

 ARCHIVO:     MallaMateriaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricularMateriaDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 07-AGOS-2017		Dennis Collaguazo 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;


/**
 * Interface MallaMateriaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricularMateriaDto.
 * @author dcollaguazo
 * @version 1.0
 */
public interface MallaMateriaDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas
	 * @param idMalla - idMalla id de la malla curricular 
	 * @param estadoMalla - estadoMalla , estado de la malla curricular
	 * @param estadoMallaMateria - estadoMallaMateria estado de la malla curricular materia
	 * @param estadoMateria - estadoMateria estado de la materia
	 * @param idNivel - idNivel id del nivel
	 * @return Lista todas las mallas curricularers materia por malla, estado malla, estado malla materia, estado materia y nivel
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularDto> listarMallasMateriasXIdMallaXEstadoPeriodo(int idMalla, int estadoMalla, int estadoMallaMateria, int estadoMateria, int idNivel, int estadoPeriodo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;

	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas
	 * @param idMallaMateria - idMallaMateria id de la malla curricular materia
	 * @return retorna la entidad malla curricular materia por id de malla curricular materia a buscar
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MallaCurricularDto buscarXId(int idMallaMateria) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;

	
	/**
	 * Realiza la busqueda de todas las mallas curriculares materia buscadas por nivel y carrera
	 * @return Lista todas las mallas curricularers materia por nivel y carrera
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<MallaCurricularDto> listarMallasMateriasXNivelXCarrera(int idCarrera, int idNivel)
			throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;

	List<MallaCurricularDto> listarMallasMateriasXCarrera(int idCarrera)
			throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;
	
}
