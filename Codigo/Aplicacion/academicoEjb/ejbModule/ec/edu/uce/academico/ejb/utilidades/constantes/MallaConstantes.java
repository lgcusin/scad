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
   
 ARCHIVO:     MallaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Malla 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-MAR-2017           Vinicio Rosales                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) MallaConstantes.
 * Clase que maneja las constantes de la entidad Malla.
 * @author jvrosales.
 * @version 1.0
 */
public class MallaConstantes {
	
	//constantes que indican el estado de la malla
	public static final int ESTADO_MALLA_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_MALLA_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_MALLA_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_MALLA_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican la vigencia de la malla
	public static final int VIGENTE_MALLA_SI_VALUE = Integer.valueOf(0);
	public static final String VIGENTE_MALLA_SI_LABEL = "SI";
	public static final int VIGENTE_MALLA_NO_VALUE = Integer.valueOf(1);
	public static final String VIGENTE_MALLA_NO_LABEL = "NO";
	
	//constantes que indican el tipo de organizacion de aprendizaje de la malla
	public static final int TIPO_ORG_APREN_1_VALUE = Integer.valueOf(0);
	public static final String TIPO_ORG_APREN_1_LABEL = "1 : 1.5";
	public static final int TIPO_ORG_APREN_2_VALUE = Integer.valueOf(1);
	public static final String TIPO_ORG_APREN_2_LABEL = "1 : 2";
	public static final int TIPO_ORG_APREN_3_VALUE = Integer.valueOf(2);
	public static final String TIPO_ORG_APREN_3_LABEL = "1 : 1";
	
	//constantes que indican el tipo de aprobacion
	public static final int POR_MATERIA_VALUE = Integer.valueOf(0);
	public static final String POR_MATERIA_LABEL = "POR MATERIA";
	public static final int POR_NIVEL_VALUE = Integer.valueOf(1);
	public static final String POR_NIVEL_LABEL = "POR NIVEL";
	
	
}
