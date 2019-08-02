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

 ARCHIVO:     CorequisitosDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla CorequisitosDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 13-07-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CorequisitoDtoException;
import ec.edu.uce.academico.ejb.excepciones.CorequisitoDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;

/**
 * Interface CorequisitosDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla CorequisitosDto.
 * @author lmquishpei
 * @version 1.0
 */


public interface CorequisitoDtoServicioJdbc {

	/**
	 * Realiza la busqueda de las materias corequisitos de una materia
	 * @return Lista las materias corequisitos de una materia
	 * @param materiaId - id de la materia que se desea tener los corequisitos
	 * @throws CorequisitoDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CorequisitoDtoNoEncontradoException- Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateria(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las materias correquisitos de una materia
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @return Lista las materias correquisitos de una materia
	 * @param materiaId - id de la materia que se desea tener los correquisitos
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException- Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateriaFull(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
}
