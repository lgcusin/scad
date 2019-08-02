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

 ARCHIVO:     MallaCurricularMateriaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricularMateria.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 11-FEB-2019			 Freddy Guzmán 						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.MallaCurricularMateriaValidacionException;

/**
 * Interface MallaCurricularMateriaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla mallaCurricularMateria.
 * @author fgguzman
 * @version 1.0
 */
public interface MallaCurricularMateriaDtoServicioJdbc {
	
	/**
	 * Método que permite buscar los modulos de una materia modular.
	 * @param modularId - id de la materia modular.
	 * @return - modulos.
	 * @throws MallaCurricularMateriaException
	 * @throws MallaCurricularMateriaValidacionException
	 * @throws MallaCurricularMateriaNoEncontradoException
	 */
	List<MateriaDto> buscarModulos(int modularId) throws MallaCurricularMateriaException, MallaCurricularMateriaValidacionException, MallaCurricularMateriaNoEncontradoException;

	/**
	 * Método que permite recuperar el objeto MallaCurricularParalelo de una modular. 
	 * @author fgguzman
	 * @param materiaId - id de la materia modular
	 * @param paraleloId - id del paralelo
	 * @return MallaCurricularParalelo
	 * @throws MallaCurricularMateriaException
	 * @throws MallaCurricularMateriaValidacionException
	 * @throws MallaCurricularMateriaNoEncontradoException
	 */
	MateriaDto buscarModularPorParalelo(int materiaId, int paraleloId) throws MallaCurricularMateriaException, MallaCurricularMateriaValidacionException, MallaCurricularMateriaNoEncontradoException;

	/**
	 * Método que permite contar estudiantes legalizados matriculados
	 * @author fgguzman
	 * @param mlcrprId - id de la materia a contar
	 * @return matriculados numero
	 * @throws MallaCurricularMateriaException
	 * @throws MallaCurricularMateriaValidacionException
	 * @throws MallaCurricularMateriaNoEncontradoException
	 */
	Integer contarEstudiantesMatriculados(int mlcrprId) throws MallaCurricularMateriaException, MallaCurricularMateriaValidacionException, MallaCurricularMateriaNoEncontradoException;
	
	
}
