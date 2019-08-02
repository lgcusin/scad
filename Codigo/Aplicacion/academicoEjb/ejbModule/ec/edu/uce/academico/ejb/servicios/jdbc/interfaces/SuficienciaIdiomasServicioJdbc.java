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

import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;

/**
 * Interface SuficienciaIdiomasServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la Suficiencia Idiomas.
 * @author fgguzman
 * @version 1.0
 */
public interface SuficienciaIdiomasServicioJdbc {

	/**
	 * Método que permite buscar si el estudiante aprobo algun idioma.
	 * @author fgguzman
	 * @param identificacion - cedula o pasaporte
	 * @return idioma aprobado
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarAprobacionIdiomasSIIU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite buscar estudiantes aprobados el idioma segun el periodo academico.
	 * @author fgguzman
	 * @param periodoId - id del periodo.
	 * @return estudiantes aprobados.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarEstudiantesAprobadosIdiomasSIIU(int periodoId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	/**
	 * Método que permite recuperar estudiantes aprobados la suficiencia en idiomas por carrera pregrado.
	 * @author fgguzman
	 * @param periodoId - id periodo idiomas
	 * @param carreraId - id carrera pregrado
	 * @return estudiantes aprobados.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<PersonaDto> cargarEstudiantesAprobadosIdiomasPorCarrera(int periodoId, int carreraId) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

}

