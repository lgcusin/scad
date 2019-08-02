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
   
 ARCHIVO:     MatriculaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Matricula 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-MAR-2017           Vinicio Rosales                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) MatriculaConstantes.
 * Clase que maneja las constantes de la entidad Matricula.
 * @author jvrosales.
 * @version 1.0
 */
public class MatriculaConstantes {
	
	//constantes que indican el estado de la matricula
	public static final int ESTADO_MATRICULA_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_MATRICULA_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_MATRICULA_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_MATRICULA_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican el tipo de matricula
	public static final int TIPO_MATRICULA_ORDINARIA_VALUE = Integer.valueOf(0);
	public static final String TIPO_MATRICULA_ORDINARIA_LABEL = "ORDINARIA";
	public static final int TIPO_MATRICULA_EXTRAORDINARIA_VALUE = Integer.valueOf(1);
	public static final String TIPO_MATRICULA_EXTRAORDINARIA_LABEL = "EXTRAORDINARIA";
	public static final int TIPO_MATRICULA_ESPECIAL_VALUE = Integer.valueOf(2);
	public static final String TIPO_MATRICULA_ESPECIAL_LABEL = "ESPECIAL";
	
	//constantes que indican el tipo de modalidad de la matricula
	public static final int TIPO_MODALIDAD_PRESENCIAL_VALUE = Integer.valueOf(0);
	public static final String TIPO_MODALIDAD_PRESENCIAL_LABEL = "PRESENCIAL";
	public static final int TIPO_MODALIDAD_SEMIPRESENCIAL_VALUE = Integer.valueOf(1);
	public static final String TIPO_MODALIDAD_SEMIPRESENCIAL_LABEL = "SEMIPRESENCIAL";
	public static final int TIPO_MODALIDAD_DISTANCIA_VALUE = Integer.valueOf(2);
	public static final String TIPO_MODALIDAD_DISTANCIA_LABEL = "DISTANCIA";
}
