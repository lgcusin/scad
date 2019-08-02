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
   
 ARCHIVO:     GratuidadConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Gratuidad 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-03-2017          David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) GratuidadConstantes.
 * Clase que maneja las constantes de la entidad Gratuidad.
 * @author darellano.
 * @version 1.0
 */
public class GratuidadConstantes {
	
	//constantes que indican SI la gratuidad ESTÁ ACTIVA O NO
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	/**
	 * Constantes de gratuidad
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public static final String GRATUIDAD_PERDIDA_DEFINITIVA_LABEL = "PERDIDA DEFINITIVA";
	public static final int GRATUIDAD_PERDIDA_DEFINITIVA_VALUE = Integer.valueOf(1);	
	public static final String GRATUIDAD_PERDIDA_TEMPORAL_LABEL = "PERDIDA TEMPORAL";
	public static final int GRATUIDAD_PERDIDA_TEMPORAL_VALUE = Integer.valueOf(2);
	public static final String GRATUIDAD_APLICA_GRATUIDAD_LABEL = "APLICA GRATUIDAD";
	public static final int GRATUIDAD_APLICA_GRATUIDAD_VALUE = Integer.valueOf(3);
	public static final String GRATUIDAD_ERROR = "ERROR AL CALCULAR GRATUIDAD";
	
}
