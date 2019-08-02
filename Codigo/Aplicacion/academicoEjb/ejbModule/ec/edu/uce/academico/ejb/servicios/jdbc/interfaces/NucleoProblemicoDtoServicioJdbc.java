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

 ARCHIVO:     NucleoProblemicoDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla NucleoProblemicoDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 07-Agosto-2017			Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.NucleoProblemicoDto;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoException;
import ec.edu.uce.academico.ejb.excepciones.AulaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.NucleoProblemicoDtoException;
import ec.edu.uce.academico.ejb.excepciones.NucleoProblemicoDtoNoEncontradoException;




/**
 * Interface NucleoProblemicoDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla NucleoProblemicoDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface NucleoProblemicoDtoServicioJdbc {

	/**
	 * Realiza la busqueda de todas las aulas que pertenecen a cada Dependencia, edificio y al que tienen acceso el usuario
	 * @param usuarioId - id del usuario 
	 * @param dependenciaId - id de la dependencia  a buscar
	 * @param edificioId - id del edificio  a buscar
	 * @return Lista de todas los aula pertenecientes a la dependencia y edificio por el usuario que accede 
	 * @throws AulaDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws AulaDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public List<NucleoProblemicoDto> listarXcarrera(int carreraId) throws NucleoProblemicoDtoException, NucleoProblemicoDtoNoEncontradoException;
	
	
	
}
