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
   
 ARCHIVO:     TipoCausalConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TipoCausal.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19-ENERO-2018		 Marcelo Quishpe			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoCausalConstantes.
 * Clase que maneja las constantes de la entidad TipoCausal.
 * @author lmquishpei.
 * @version 1.0
 */
public class TipoCausalConstantes {
	
	public static final String TIPO_CAUSAL_TERCERA_MATRICULA_LABEL = "CAUSALES DE TERCERA MATRICULA";
	public static final int TIPO_CAUSAL_TERCERA_MATRICULA_VALUE = Integer.valueOf(1);
	
	public static final String TIPO_CAUSAL_RETIRO_MATERIA_LABEL = "CAUSALES DE RETIRO DE ASIGNATURA";
	public static final int TIPO_CAUSAL_RETIRO_MATERIA_VALUE = Integer.valueOf(2);
	
	public static final String TIPO_CAUSAL_RETIRO_ASIGNATURA_FORTUITO_LABEL = "CAUSALES DE RETIRO DE ASIGNATURA POR SITUACIONES FORTUITAS";
	public static final int TIPO_CAUSAL_RETIRO_ASIGNATURA_FORTUITO_VALUE = Integer.valueOf(3);
	
	public static final String TIPO_CAUSAL_ANULACION_ASIGNATURA_LABEL = "CAUSALES DE ANULACIÓN DE MATRÍCULA";
	public static final int TIPO_CAUSAL_ANULACION_ASIGNATURA_VALUE = Integer.valueOf(4);
	
	
}
