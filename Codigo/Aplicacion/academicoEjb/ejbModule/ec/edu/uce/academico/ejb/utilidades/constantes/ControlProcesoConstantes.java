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
   
 ARCHIVO:     ControlProcesoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ControlProceso
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-05-2018          Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) CronogramaConstantes.
 * Clase que maneja las constantes de la entidad Cronograma.
 * @author lmquishpe.
 * @version 1.0
 */
public class ControlProcesoConstantes {
	
	//constantes que indican el tipo de accion
	public static final int TIPO_ACCION_INSERTAR_VALUE = Integer.valueOf(0);
	public static final String TIPO_ACCION_INSERTAR_LABEL = "INSERTAR";
	public static final int TIPO_ACCION_ACTUALIZAR_VALUE = Integer.valueOf(1);	
	public static final String TIPO_ACCION_ACTUALIZAR_LABEL = "ACTUALIZAR";
	public static final int TIPO_ACCION_ELIMINAR_VALUE = Integer.valueOf(2);	
	public static final String TIPO_ACCION_ELIMINAR_LABEL = "ELIMINAR";
	
}
