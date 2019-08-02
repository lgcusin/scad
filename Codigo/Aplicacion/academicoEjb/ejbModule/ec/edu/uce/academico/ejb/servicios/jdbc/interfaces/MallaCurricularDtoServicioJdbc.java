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

 ARCHIVO:     MallaCurricularDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricular.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 12-ABRIL-2017		Gabriel Mafla 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MallaCurricularDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;


/**
 * Interface MallaCurricularDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricular.
 * @author ghmafla
 * @version 1.0
 */
public interface MallaCurricularDtoServicioJdbc {
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularDto> listarMallasXCarrera(int carreraId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;

	
	/**
	 * Realiza la busqueda de una malla por codigo en la aplicacion
	 * @return true si encontro y false caso contrario
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public boolean validarMallaCurricularXCodigo (MallaCurricularDto entidad,int tipo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una malla por codigo en la aplicacion
	 * @return true si encontro y false caso contrario
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public boolean validarMallaCurricularXNombre (MallaCurricularDto entidad,int tipo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de mallas curriculares buscado por id de carrera por vigencia y por estado
	 * @param idCarrera - idCarrera id de la carrera a buscar
	 * @param vigencia - vigencia malla curricurar vigencia
	 * @param estado - estado de la malla curricular
	 * @return Lista todas las mallas a buscar por los parametros
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularDto> listarMallasXCarreraXVigenciaXEstadoXestadoPeriodo(int idCarrera, int vigencia, int estado, int estadoPeriodo) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;
	
	/**
	 * Método que permite listar las mallas curriculares segun los parametros ingresados
	 * @author fgguzman
	 * @param dependenciaId - id de la dependencia
	 * @param carreraId -  id de la carrera
	 * @param tipoCarrera - {Nivelacion, Pregrado, Postgrado, Suficiencias}
	 * @param estadoMalla - {Activo, Inactivo}
	 * @return mallas curriculares vinculadas
	 * @throws MallaCurricularDtoException
	 * @throws MallaCurricularDtoNoEncontradoException
	 */
	List<MallaCurricularDto> buscarMallaCurricular(int dependenciaId, int carreraId, Integer[] tiposCarrera,Integer[] estadosMalla) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;
	
	/**
	 * Método que permite recuperar la cantidad de creditos por nivel de la malla curricular segun la carrera.
	 * @author fgguzman
	 * @param mlcrId - id de la carrera
	 * @return cantidad de creditos por nivel segun malla.
	 * @throws MallaCurricularDtoException
	 * @throws MallaCurricularDtoNoEncontradoException
	 */
	List<MallaCurricularDto> buscarMallaCurricularPorNivel(int mlcrId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;

	/**
	 * Método que pemite recuperar las materias vinculadas a una malla curricular.
	 * @author fgguzman
	 * @param mlcrId - id de la Malla Curricular.
	 * @return materias vinculadas a la malla curricular
	 * @throws MallaCurricularDtoException
	 * @throws MallaCurricularDtoNoEncontradoException
	 */
	List<MateriaDto> buscarMaterias(int mlcrId) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;


	List<MallaCurricularDto> buscarMallaCurricularPosgrado(int dependenciaId, int carreraId, Integer tipoCarrera,
			Integer estadoMalla) throws MallaCurricularDtoException, MallaCurricularDtoNoEncontradoException;


	List<MallaCurricularDto> buscarCarreraDependenciaPorPeriodoAcademico(int pracId);

}
