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

 ARCHIVO:     HistorialAcademicoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc del HistorialAcademico del SAIU.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 30Nov2018			 	 Freddy Guzmán				        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.RecordEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;

/**
 * Interface HistorialAcademicoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc del HistorialAcademico del SAIU.
 * @author fgguzman
 * @version 1.0
 */
public interface HistorialAcademicoServicioJdbc {
	/**
	 * *********************************************************************************
	 * SAU
	 */
	
	/**
	 * Método que permite recuperar el record academico del estudiante del SAU, para el hisorial académico.
	 * Cambiado codificacion Sau correspondiente en SIIU. 
	 * @param identificacion
	 * @param mtrEstados
	 * @param carreras
	 * @return historial academico SAU
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarHistorialAcademicoSAUHomologado(String identificacion, Integer[] mtrEstados) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	
	/**
	 * Método que permite recuperar el historial de matriculas del estudiante del SAU.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial de matriculas del estudiante.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarHistorialMatriculaSAU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

	
	/**
	 * *********************************************************************************
	 * SIIU
	 */
	
	/**
	 * Método que permite recuperar el historial academico del estudiante en SIIU.
	 * @author fgguzman
	 * @param identificacion
	 * @param estadosMateria
	 * @param carreras
	 * @return historial academico SIIU
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarHistorialAcademicoSIIUHomologado(String identificacion, Integer[] estadosMateria) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

	

	/**
	 * Método que permite recuperar el historial de matriculas del estudiante del SIIU.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial de matriculas del estudiante.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarHistorialMatriculaSIIU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

	
	/**
	 * Método que permite recuperar el historial académico de la Suficiencia en Idiomas del estudiante del SIIU.
	 * @author fgguzman
	 * @param identificacion
	 * @return historial academico del estudiante.
	 * @throws RecordEstudianteNoEncontradoException
	 * @throws RecordEstudianteException
	 */
	List<RecordEstudianteDto> buscarHistorialAcademicoIdiomasSIIU(String identificacion) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;
	
	List<RecordEstudianteDto> buscarHistorialAcademicoSIIUPosgrado(String identificacion, Integer[] mtrEstados) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

}

