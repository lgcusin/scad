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
   
 ARCHIVO:     MateriaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Materia 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-MAR-2017           Vinicio Rosales                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) MateriaConstantes.
 * Clase que maneja las constantes de la entidad Materia.
 * @author jvrosales.
 * @version 1.0
 */
public class MateriaConstantes {
	
	//constantes que indican el estado de la materia
	public static final int ESTADO_MATERIA_ACTIVO_VALUE = 0;
	public static final String ESTADO_MATERIA_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_MATERIA_INACTIVO_VALUE = 1;
	public static final String ESTADO_MATERIA_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_MATERIA_EN_CIERRE_VALUE = 2;
	public static final String ESTADO_MATERIA_EN_CIERRE_LABEL = "EN CIERRE";

	//constantes que indican el tipo de relacion de trabajo  de la materia
	public static final int RELACIONTRABAJO_MATERIA_1_1_5_VALUE = Integer.valueOf(0);
	public static final String RELACIONTRABAJO_MATERIA_1_1_5_LABEL = "1:1.5";
	public static final int RELACIONTRABAJO_MATERIA_1_2_VALUE = Integer.valueOf(1);
	public static final String RELACIONTRABAJO_MATERIA_1_2_LABEL = "1:2";
	public static final int RELACIONTRABAJO_MATERIA_OTROS_VALUE = Integer.valueOf(2);
	public static final String RELACIONTRABAJO_MATERIA_OTROS_LABEL = "OTROS";
	
	//constantes que indican el tipo de medida que se usa para definir hora, o créditos
	public static final int MATERIA_POR_HORAS_VALUE = Integer.valueOf(0);
	public static final String MATERIA_POR_HORAS_LABEL = "HORAS";
	public static final int MATERIA_POR_CREDITOS_VALUE = Integer.valueOf(1);
	public static final String MATERIA_POR_CREDITOS_LABEL = "CREDITOS";
	public static final int MATERIA_POR_HORAS_Y_CREDITOS_VALUE = Integer.valueOf(2);
	public static final String MATERIA_POR_HORAS_Y_CREDITOS_LABEL = "HORAS _ CREDITOS";
	
	//constantes que indican el tipo de materia, si es prerrequisito o correquisito 
		public static final int MATERIA_PRERREQUISITO_VALUE = Integer.valueOf(0);
		public static final String MATERIA_PRERREQUISITO_LABEL = "PRERREQUISITO";
		public static final int MATERIA_CORREQUISITO_VALUE = Integer.valueOf(1);
		public static final String MATERIA_CORREQUISITO_LABEL = "CORREQUISITO";
		
	
}
