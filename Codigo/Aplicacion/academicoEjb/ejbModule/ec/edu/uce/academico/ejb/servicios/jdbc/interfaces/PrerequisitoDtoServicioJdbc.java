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

 ARCHIVO:     PrerequisitosDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla PrerequisitosDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 13-07-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoException;
import ec.edu.uce.academico.ejb.excepciones.MateriaDtoNoEncontradoException;

/**
 * InterfacePrerequisitosDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla PrerequisitosDto.
 * @author lmquishpei
 * @version 1.0
 */


public interface PrerequisitoDtoServicioJdbc {

	/**
	 * Realiza la busqueda de las materias prerequisitos de una materia
	 * @param materiaId - id de la materia que se desea tener los prerequisitos
	 * @return Lista las  materias prerequisitos  de una materia 
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<MateriaDto> listarXidMateria(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de las materias prerrequisitos de una materia
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @return Lista las  materias prerrequisitos  de una materia 
	 * @param materiaId - id de la materia que se desea tener los prrrequisitos
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateriaFull(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las materias prerrequisitos de una materia
	 * @return Lista las  materias prerrequisitos  de una materia 
	 * @param materiaId - id de la materia que se desea tener los prrrequisitos
	 * @throws MateriaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MateriaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MateriaDto> listarXidMateriaRequisitos(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
	public List<MateriaDto> listarXidMateriaRequisitosInicial(int materiaId) throws MateriaDtoException, MateriaDtoNoEncontradoException;
	
}
