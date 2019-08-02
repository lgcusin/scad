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
   
 ARCHIVO:     ScheduleServicio.java	  
 DESCRIPCION: Interface que define los procesos a realizar en schedule. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-04-2017			 	Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.schedule.interfaces;

import javax.ejb.Local;
import javax.ejb.Timer;

/**
 * Interface (interface) ScheduleServicio.
 * Interface que define los procesos a realizar en schedule.
 * @author dalbuja.
 * @version 1.0
 */
@Local
public interface ScheduleServicio {

	public void procesoEnvio(Timer timer);

	public void procesoCambioFichaInscripcionMatricula(Timer timer);

	public void procesoCambioEstadoNIvelacion(Timer timer);

	public void procesoCorrecionRecordEstudiante(Timer timer);
	
	public void procesoEnvioEmergente(Timer timer);
	
	public void procesoCorreccionModulares(Timer timer);
	
	public void procesoBorradoDuplicadosModulares(Timer timer);

	public void procesoCambioRecuperacion(Timer timer);
	
//	public void procesoAprobadasoSIAC(Timer timer);
	
}
