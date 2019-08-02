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
   
 ARCHIVO:     TipoSistemaCalificacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TipoSistemaCalificacion 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 30-05-2017          David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoSistemaCalificacionConstantes.
 * Clase que maneja las constantes de la entidad TipoSistemaCalificacion.
 * @author darellano.
 * @version 1.0
 */
public class TipoSistemaCalificacionConstantes {
	
	//constantes para el tipo de sistema de calificacion de nivelación
	public static final int SISTEMA_CALIFICACION_NIVELACION_VALUE = Integer.valueOf(0);
	public static final String SISTEMA_CALIFICACION_NIVELACION_LABEL = "NIVELACIÓN";
	
	public static final int SISTEMA_CALIFICACION_PREGRADO_VALUE = Integer.valueOf(1);
	public static final String SISTEMA_CALIFICACION_PREGRADO_LABEL = "PREGRADO";
	
}
