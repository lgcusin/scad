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

 ARCHIVO:     PlanificacionCronogramaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc del DTO PlanificacionCronogramaDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 07-09-2017			Arturo Villafuerte				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;
 

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaValidacionException;

/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc del DTO PlanificacionCronogramaDto.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface PlanificacionCronogramaDtoServicioJdbc {

	/**
	 * Busca por id de proceso flujo para encontrar PlanificacionCronogramaDTO con todos los datos del cronograma
	 * @param idProcesoFlujo
	 * @return Dto PlanificacionCronogramaDTO con la informacion del cronograma
	 * @throws PlanificacionCronogramaDtoNoEncontradoException PlanificacionCronogramaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PlanificacionCronogramaDtoException PlanificacionCronogramaDtoException excepción general lanzada
	 */
	PlanificacionCronogramaDto buscarCronogramaXProcesoFlujo(int idProcesoFlujo) throws PlanificacionCronogramaDtoNoEncontradoException, PlanificacionCronogramaDtoException;
	
	/**MQ
	 * Busca PlanificacionCronogramaDTO  por procesoFlujo, tipoCronograma y periodo
	 * @param idPrfl- id del proceso flujo
	 * @param tipoCrn - tipo de cronograma
	 * @param idPeriodo -periodo academico
	 * @return Dto PlanificacionCronogramaDto con la informacion del cronograma
	 * @throws PlanificacionCronogramaDtoNoEncontradoException PlanificacionCronogramaDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PlanificacionCronogramaDtoException PlanificacionCronogramaDtoException excepción general lanzada
	 */
	PlanificacionCronogramaDto buscarXProcesoFlujoXTipoCrnXPeriodo(int idPrfl, int tipoCrn,int idPeriodo) throws PlanificacionCronogramaDtoNoEncontradoException, PlanificacionCronogramaDtoException;
	
	/**
	 * Método que permite listar la Planificacion de los Procesos segun tipo de cronograma y periodo academico.
	 * @author fgguzman
	 * @param pracTipo - tipo del periodo academico
	 * @param crnTipo - id del tipo de cronpgrama
	 * @param estado - estado de los cronogramas
	 * @return cronogramas y procesos.
	 * @throws PlanificacionCronogramaNoEncontradoException
	 * @throws PlanificacionCronogramaException
	 */
	List<PlanificacionCronogramaDto> buscarCronogramaProcesos(Integer[] pracTipo, Integer[] crnTipo, Integer estado) throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException ;
	
	/**
	 * Método que permite recuperar los procesos asociados a la planificacion academica del periodo solicitado.
	 * @author fgguzman
	 * @param pracId - id del periodo academico
	 * @return procesos asociados al periodo academico
	 * @throws PlanificacionCronogramaNoEncontradoException
	 * @throws PlanificacionCronogramaException
	 */
	List<PlanificacionCronogramaDto> buscarCronogramaProcesosPorPeriodo(int  pracId) throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException ;

	/**
	 * Método que permite recuperar el cronograma de un proceso segun parametros.
	 * @author fgguzman
	 * @param periodoId
	 * @param cronogramaTipo
	 * @param procesoId
	 * @return objeto tipo PlanificacionCronogramaDto
	 * @throws PlanificacionCronogramaValidacionException
	 * @throws PlanificacionCronogramaNoEncontradoException
	 * @throws PlanificacionCronogramaException
	 */
	PlanificacionCronogramaDto buscarPlanificacion(int  periodoId, int cronogramaTipo, int procesoId) throws PlanificacionCronogramaValidacionException, PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException; 
}
