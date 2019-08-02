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

 ARCHIVO:     InstitucionAcademicaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla InstitucionAcademicaDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 29-NOV-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoException;
import ec.edu.uce.academico.ejb.excepciones.InstitucionAcademicaDtoNoEncontradoException;

/**
 * Interface InstitucionAcademicaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla InstitucionAcademicaDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface InstitucionAcademicaDtoServicioJdbc {

	/**
	 * Realiza la busqueda de instituciones académicas por cantón,tipo y nivel
	 * @param cantonId - id del cantón  a buscar
	 * @param tipoId - id del tipo  a buscar
	 * @return Lista de todas los instituciones académicas por cantón y tipo
	 * @throws InstitucionAcademicaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXCantonXTipo(int cantonId, int tipoId) throws InstitucionAcademicaDtoException, InstitucionAcademicaDtoNoEncontradoException;	
	
	/**MQ
	 * Realiza la busqueda de instituciones académicas Nivel (Universidad, Colegio)
	 * @param nivelId - id del cantón  a buscar
	 * @return Lista de todas los instituciones académicas por cantón y tipo
	 * @throws InstitucionAcademicaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXNivel( int nivelId) throws InstitucionAcademicaDtoException, InstitucionAcademicaDtoNoEncontradoException;
}
