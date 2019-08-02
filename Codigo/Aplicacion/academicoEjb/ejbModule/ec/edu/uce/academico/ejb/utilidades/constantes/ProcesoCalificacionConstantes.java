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
   
 ARCHIVO:     ProcesoCalificacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ProcesoCalificacion 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-JUNIO-2017		 Gabriel Mafla 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) ProcesoCalificacionConstantes.
 * Clase que maneja las constantes de la entidad ProcesoCalificacion.
 * @author ghmafla.
 * @version 1.0
 */
public class ProcesoCalificacionConstantes {
	
	public static final int ESTADO_EDITADO_GUARDADO_TEMPORAL_NOTA_VALUE = Integer.valueOf(0);
	public static final String ESTADO_EDITADO_GUARDADO_TEMPORAL_NOTA_LABEL = "EDITADO GUARDADO TEMPORAL";
	
	public static final int ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_VALUE = Integer.valueOf(1);
	public static final String ESTADO_GUARDADA_NOTA_1_HEMISEMESTRE_LABEL = "GUARDADA NOTA 1 HEMISEMESTRE";
	
	public static final int ESTADO_GUARDADA_NOTA_POSGRADO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_GUARDADA_NOTA_POSGRADO_LABEL = "GUARDADA NOTA POSGRADO";
	
	public static final int ESTADO_EDITADO_GUARDADO_TEMPORAL_NOTA2_VALUE = Integer.valueOf(0);
	public static final String ESTADO_EDITADO_GUARDADO_TEMPORAL_NOTA2_LABEL = "EDITADO GUARDADO TEMPORAL";
	
	public static final int ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_VALUE = Integer.valueOf(1);
	public static final String ESTADO_GUARDADA_NOTA_2_HEMISEMESTRE_LABEL = "GUARDADA NOTA 2 HEMISEMESTRE";
	
	
	public static final int ESTADO_EDITADO_NOTA_3_RECUPERACION_VALUE = Integer.valueOf(0);
	public static final String ESTADO_EDITADO_NOTA_3_RECUPERACION_LABEL = "EDITADO NOTA 3 RECUPERACION";
	
	public static final int ESTADO_GUARDADA_NOTA_3_RECUPERACION_VALUE = Integer.valueOf(1);
	public static final String ESTADO_GUARDADA_NOTA_3_RECUPERACION_LABEL = "GUARDADA NOTA 3 RECUPERACION";
	
	public static final int ESTADO_CORRECION_VALUE = Integer.valueOf(2);
	public static final String ESTADO_CORRECION_LABEL = "CORRECCION DEL SISTEMA";
	
}
