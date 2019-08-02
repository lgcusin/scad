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

 ARCHIVO:     MallaCurricularParaleloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricularParalelo.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 15-AGOSTO-ABRIL-2017		Gabriel Mafla 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.DocenteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.MallaCurricularParaleloDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularParaleloNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoException;
import ec.edu.uce.academico.ejb.excepciones.ParaleloDtoNoEncontradoException;


/**
 * Interface MallaCurricularParaleloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricularParalelo.
 * @author dalbuja
 * @version 1.0
 */
public interface MallaCurricularParaleloDtoServicioJdbc {
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularParaleloDto> listarMallasXParaleloXMateria(int carreraId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException;

	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularParaleloDto> listarMallasXCarrera(int crrId, List<CarreraDto> listCarreras) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MallaCurricularParaleloDto buscarCupoMlcrprXMateriaXParalelo(Integer paraleloId, Integer materiaId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MallaCurricularParaleloDto> buscarMatriculadosMlcrprXMateriaXParalelo(Integer paraleloId, Integer materiaId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las mallas por de la aplicacion
	 * @return Lista todas las mallas por carrera de la aplicacion
	 * @throws MallaCurricularDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MallaCurricularDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MallaCurricularParaleloDto buscarCupoMlcrprXMateriaXParaleloAlterno(Integer paraleloId, Integer materiaId) throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException;

	public Integer buscarPorMtrIdPrsIdentificacion(Integer materiaId, String cedula, Integer nivelId)
			throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	public MallaCurricularParaleloDto buscarMallaCurricularParaleloPorId(Integer mlcrprId)
			throws MallaCurricularParaleloDtoException, MallaCurricularParaleloDtoNoEncontradoException;


	public Integer buscarPorMtrIdPrsIdentificacionPorPrlId(Integer materiaId, String cedula, Integer nivelId,
			Integer paraleloId) throws MallaCurricularParaleloNoEncontradoException, MallaCurricularParaleloException;

	public Integer buscarMallaCurricularParaleloEnHorarioAcademicoPorMlcrprIdComp(Integer mlcrprId);

	void modificarCupoMallaCurricularParalelo(Integer mlcrprId, Integer tipoOperacion);

	List<DocenteJdbcDto> listarRegistroNotasDocente(int carreraId, int pracId, String identificacion)
			throws ParaleloDtoException, ParaleloDtoNoEncontradoException;


}
