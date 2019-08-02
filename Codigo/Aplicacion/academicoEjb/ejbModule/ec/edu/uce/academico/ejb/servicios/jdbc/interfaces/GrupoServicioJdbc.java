/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     GrupoServicioJdbc.java      
 DESCRIPCIÓN: Interfaz que define las operaciones para el proceso de EVALUACION AL DESEMPEÑO.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 25-NOV-2018            Freddy Guzman   	                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;


import java.util.List;

import ec.edu.uce.academico.ejb.dtos.GrupoDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.excepciones.GrupoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.GrupoValidacionException;


/**
 * Interfaz GrupoServicioJdbc
 * Interfaz que define las operaciones del proceso matriculas de intercambio.
 * @author fgguzman
 * @version 1.0
 */
public interface GrupoServicioJdbc {

	/**
	 * Método que permite recuperar los coordinadores  de cada grupo por carrera y periodo academico.
	 * @author fgguzman
	 * @param carreraId
	 * @param periodoId
	 * @return Grupos con coordinador 
	 * @throws GrupoNoEncontradoException
	 * @throws GrupoException
	 */
	List<GrupoDto> buscarCoordinadoresPorGrupo(int carreraId, int periodoId) throws GrupoNoEncontradoException , GrupoValidacionException ,  GrupoException;

	/**
	 * Método que permite actualizar el coordinador - evalucion pares
	 * @author fgguzman
	 * @param coordinador - datos del coordinador
	 * @param periodo - id del periodo
	 * @param carrera - id de la carrera
	 * @param grupo - id del grupo
	 * @return
	 * @throws GrupoNoEncontradoException
	 * @throws GrupoException
	 */
	PersonaDto agregarCoordinador(PersonaDto coordinador, int periodo, int carrera, int grupo)throws GrupoNoEncontradoException  ,  GrupoException; 
	
	/**
	 * Método que permite actualizar el evaluador del coordinador - evalucion pares
	 * @author fgguzman
	 * @param evaluador - quien evualua al coordinador
	 * @param periodo - id del periodo
	 * @param carrera - id de la carrera
	 * @param grupo - id del grupo
	 * @return
	 * @throws GrupoNoEncontradoException
	 * @throws GrupoException
	 */
	PersonaDto agregarEvaluador(PersonaDto evaluador, int periodo, int carrera, int grupo)throws GrupoNoEncontradoException  ,  GrupoException; 
}
