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
   
 ARCHIVO:     SuficienciaInformaticaServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la Suficiencia Informatica.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-ENE-2018			Freddy Guzmán						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.FichaMatriculaDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;

/**
 * Interface SuficienciaInformaticaServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la Suficiencia Informatica.
 * @author fgguzman
 * @version 1.0
 */
public interface SuficienciaInformaticaServicioJdbc {

	List<RecordEstudianteDto> buscarHistorialMatriculasPresencial(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	List<RecordEstudianteDto> buscarHistorialMatriculasExoneracion(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

	List<FichaMatriculaDto> buscarMatriculasActivas(String identificacion, Integer[] pracTipo) throws FichaMatriculaNoEncontradoException, FichaMatriculaException;
	
	List<RecordEstudianteDto> buscarMatriculaActivaPregrado(String identificacion, int periodo) throws FichaMatriculaNoEncontradoException, FichaMatriculaException;

	List<RecordEstudianteDto> buscarHistorialMatriculasSuficiencia(String identificacion)throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite buscar estudiantes por apellido o cedula/pasaporte matriculados en periodo activo informática.
	 * @author fgguzman
	 * @param param - cedula o pasaporte / primer apellido
	 * @param tipo - GeneralesContantes.FIND_POR_IDENTIFACION / GeneralesContantes.FIND_POR_PRIMER_APELLIDO  
	 * @return personas coinciden param
	 * @throws PersonaException
	 * @throws PersonaNoEncontradoException
	 */
	
	List<PersonaDto> buscarEstudiantesPorParametro(String param , int tipo) throws PersonaException, PersonaNoEncontradoException;
}
