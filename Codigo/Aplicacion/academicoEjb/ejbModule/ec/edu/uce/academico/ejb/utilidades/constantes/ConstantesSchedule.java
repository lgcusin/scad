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
   
 ARCHIVO:     ConstantesSchedule.java	  
 DESCRIPCION: Clase de constantes de los Cron para Schedules 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02-09-2017		 		Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.utilidades.constantes;

import java.util.ResourceBundle;

/**
 * Clase (constantes) ConstantesSchedule.
 * Clase de constantes de los Cron para Schedules
 * @author dalbuja.
 * @version 1.0
 */
public class ConstantesSchedule {
	/*************************************************************/
	/**************** CONSTANTES DE CONFIGURACIONES **************/
	/*************************************************************/
	
	/**
	 * Define la etiqueta que representa las constantes de la implementación de tareas programadas 
	 */
	public static final String COLA_ACTIVEMQ;
	public static final String URL_APLICACION;
	public static final String URL_APLICACION_MAIL;
	public static final String NIO_ACTIVEMQ;
	public static final String SCH_CRON_ENVIO;
	
	static{
		ResourceBundle rb = ResourceBundle.getBundle("META-INF.configuracion.schedule");
		
		NIO_ACTIVEMQ = rb.getString("nio.activemq");
		URL_APLICACION=rb.getString("url.aplicacion");
		URL_APLICACION_MAIL=rb.getString("url.aplicacion.mail");
		COLA_ACTIVEMQ = rb.getString("activemq.queue");
		SCH_CRON_ENVIO = rb.getString("fecha.hora.inicio.envio.comprobantes");
		
		
		
	}
}
