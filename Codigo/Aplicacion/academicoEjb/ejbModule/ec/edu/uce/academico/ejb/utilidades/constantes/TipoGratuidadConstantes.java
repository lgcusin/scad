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
   
 ARCHIVO:     TipoGratuidadConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TipoGratuidad 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-09-2017		 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoGratuidadConstantes.
 * Clase que maneja las constantes de la entidad TipoGratuidad.
 * @author dalbuja.
 * @version 1.0
 */
public class TipoGratuidadConstantes {
	
	public static final int PERDIDA_DEFINITIVA_VALUE = Integer.valueOf(1);
	public static final String PERDIDA_DEFINITIVA_LABEL = "PERDIDA DEFINITIVA";
	public static final int PERDIDA_TEMPORAL_VALUE = Integer.valueOf(2);
	public static final String PERDIDA_TEMPORAL_LABEL = "PERDIDA TEMPORAL";
	public static final int APLICA_GRATUIDAD_VALUE = Integer.valueOf(3);
	public static final String APLICA_GRATUIDAD_LABEL = "APLICA_GRATUIDAD";
	public static final int NO_APLICA_GRATUIDAD_VALUE = Integer.valueOf(4);
	public static final String NO_APLICA_GRATUIDAD_LABEL = "NO APLICA_GRATUIDAD";
	
}
