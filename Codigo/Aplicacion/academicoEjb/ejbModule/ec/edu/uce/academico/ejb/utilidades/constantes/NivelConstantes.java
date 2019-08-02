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
   
 ARCHIVO:     NivelConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Nivel
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-MAR-2017           Vinicio Rosales                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) NivelConstantes.
 * Clase que maneja las constantes de la entidad Nivel.
 * @author jvrosales.
 * @version 1.0
 */
public class NivelConstantes {
	
	//constantes que indican el nivel de la materia en la malla -> NVL_ID
	public static final int NIVEL_PRIMERO_VALUE = Integer.valueOf(1);
	public static final String NIVEL_PRIMERO_LABEL = "PRIMER SEMESTRE";
	public static final int NIVEL_SEGUNDO_VALUE = Integer.valueOf(2);
	public static final String NIVEL_SEGUNDO_LABEL = "SEGUNDO SEMESTRE";
	public static final int NIVEL_TERCER_VALUE = Integer.valueOf(3);
	public static final String NIVEL_TERCER_LABEL = "TERCER SEMESTRE";
	public static final int NIVEL_CUARTO_VALUE = Integer.valueOf(4);
	public static final String NIVEL_CUARTO_LABEL = "CUARTO SEMESTRE";
	public static final int NIVEL_QUINTO_VALUE = Integer.valueOf(5);
	public static final String NIVEL_QUINTO_LABEL = "QUINTO SEMESTRE";
	public static final int NIVEL_SEXTO_VALUE = Integer.valueOf(6);
	public static final String NIVEL_SEXTO_LABEL = "SEXTO SEMESTRE";
	public static final int NIVEL_SEPTIMO_VALUE = Integer.valueOf(7);
	public static final String NIVEL_SEPTIMO_LABEL = "SEPTIMO SEMESTRE";
	public static final int NIVEL_OCTAVO_VALUE = Integer.valueOf(8);
	public static final String NIVEL_OCTAVO_LABEL = "OCTAVO SEMESTRE";
	public static final int NIVEL_NOVENO_VALUE = Integer.valueOf(9);
	public static final String NIVEL_NOVENO_LABEL = "NOVENO SEMESTRE";
	public static final int NIVEL_DECIMO_VALUE = Integer.valueOf(10);
	public static final String NIVEL_DECIMO_LABEL = "DECIMO SEMESTRE";
	public static final int NIVEL_NIVELACION_VALUE = Integer.valueOf(11);
	public static final String NIVEL_NIVELACION_LABEL = "NIVELACIÓN";
	public static final int NIVEL_DECIMO_PRIMERO_VALUE = 12;
	public static final String NIVEL_DECIMO_PRIMERO_LABEL = "DÉCIMO PRIMER SEMESTRE";
	public static final int NIVEL_APROBACION_VALUE = 13;
	public static final String NIVEL_APROBACION_LABEL = "APROBACIÓN";
	
	//constantes que indican el estado del nivel
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican el numeral del nivel
	public static final int NUMERAL_NIVELACION_VALUE = 0;
	public static final String NUMERAL_NIVELACION_LABEL = "NIVELACIÓN";
	public static final int NUMERAL_APROBACION_VALUE = -1;
	public static final String NUMERAL_APROBACION_LABEL = "APROBACIÓN";
	public static final int NUMERAL_DECIMO_VALUE = 10;
	public static final int NUMERAL_DECIMO_PRIMERO_VALUE = 11;
	
	//constantes que indican el numeral del nivel de posgrado de ficha_inscripcion
	public static final int PRIMER_NIVEL_POSGRADO_VALUE = Integer.valueOf(101);
	public static final String PRIMER_NIVEL_POSGRADO_LABEL = "PRIMER NIVEL";
	public static final int SEGUNDO_NIVEL_POSGRADO_VALUE = Integer.valueOf(102);
	public static final String SEGUNDO_NIVEL_POSGRADO_LABEL = "SEGUNDO NIVEL";
	public static final int TERCER_NIVEL_POSGRADO_VALUE = Integer.valueOf(103);
	public static final String TERCER_NIVEL_POSGRADO_LABEL = "TERCER NIVEL";
	public static final int CUARTO_NIVEL_POSGRADO_VALUE = Integer.valueOf(104);
	public static final String CUARTO_NIVEL_POSGRADO_LABEL = "CUARTO NIVEL";
	public static final int QUINTO_NIVEL_POSGRADO_VALUE = Integer.valueOf(105);
	public static final String QUINTO_NIVEL_POSGRADO_LABEL = "QUINTO NIVEL";
	public static final int SEXTO_NIVEL_POSGRADO_VALUE = Integer.valueOf(106);
	public static final String SEXTO_NIVEL_POSGRADO_LABEL = "SEXTO NIVEL";
	public static final int SEPTIMO_NIVEL_POSGRADO_VALUE = Integer.valueOf(107);
	public static final String SEPTIMO_NIVEL_POSGRADO_LABEL = "SEPTIMO NIVEL";
	public static final int OCTAVO_NIVEL_POSGRADO_VALUE = Integer.valueOf(108);
	public static final String OCTAVO_NIVEL_POSGRADO_LABEL = "OCTAVO NIVEL";
		
}
