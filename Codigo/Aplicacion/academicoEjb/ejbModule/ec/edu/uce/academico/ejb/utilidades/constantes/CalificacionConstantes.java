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
   
 ARCHIVO:     CalificacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Calificacion 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20-JUNIO-2017		 Gabriel Mafla 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) CalificacionConstantes.
 * Clase que maneja las constantes de la entidad Calificacion.
 * @author ghmafla.
 * @version 1.0
 */
public class CalificacionConstantes {
	
	public static final int PORCENTAJE_100_PORCIENTO_VALUE = Integer.valueOf(100);
	public static final String PORCENTAJE_100_PORCIENTO_LABEL = "100%";
	
	public static final int PUNTAJE_40_PUNTOS_VALUE = Integer.valueOf(40);
	public static final String PUNTAJE_40_PUNTOS_LABEL = "40%";
	
	public static final double PONDERACION_PARAMETRO1_VALUE = Double.valueOf(0.4);
	public static final String PONDERACION_PARAMETRO1_LABEL = "PONDERACIÓN SUMA NOTAS HEMISEMESTRE";
	public static final double PONDERACION_PARAMETRO2_VALUE = Double.valueOf(1.2);
	public static final String PONDERACION_PARAMETRO2_LABEL = "PONDERACIÓN RECUPERACIÓN";
	
	public static final int NOTA_MAX_PARCIAL_VALUE = Integer.valueOf(20);
	public static final String NOTA_MAX_PARCIAL_LABEL = "NOTA MÁXIMA PARCIAL";
	
	public static final int PORCENTAJE_ASISTENCIA_APROBACION_VALUE = Integer.valueOf(80);
	public static final String PORCENTAJE_ASISTENCIA_APROBACION_LABEL = "PORCENTAJE ASISTENCIA APROBACIÓN 80%";
	
	
	public static final double NOTA_APROBACION_MATERIA_VALUE = Double.valueOf(27.50);
	public static final String NOTA_APROBACION_MATERIA_LABEL = "NOTA_APROBACION_MATERIA";
	
	public static final double NOTA_APROBACION_INGLES_RECUPERACION_VALUE = Double.valueOf(14);
	public static final String NOTA_APROBACION_INGLES_RECUPERACION_LABEL = "NOTA_APROBACION_MATERIA_RECUPERACION";
	
	public static final double NOTA_MINIMA_INGRESAR_RECUPERACION_MATERIA_VALUE = Double.valueOf(8.8);
	public static final String NOTA_MINIMA_INGRESAR_RECUPERACION_MATERIA_LABEL = "NOTA MÍNIMA PARA INGRESAR A RECUPERACIÓN";
	
	public static final double NOTA_MAXIMA_INGRESAR_RECUPERACION_MATERIA_VALUE = Double.valueOf(27.49);
	public static final String NOTA_MAXIMA_INGRESAR_RECUPERACION_MATERIA_LABEL = "NOTA MÁXIMA PARA INGRESAR A RECUPERACIÓN";
	
}
