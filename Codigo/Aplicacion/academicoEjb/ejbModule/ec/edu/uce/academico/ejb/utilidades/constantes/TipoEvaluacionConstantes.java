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
   
 ARCHIVO:     TipoEvaluacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad EvaluacionDocente.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-ENERO-2018		 Arturo Villafuerte 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoEvaluacionConstantes.
 * Clase que maneja las constantes de la entidad TipoEvaluacion.
 * @author ajvillafuerte.
 * @version 1.0
 */
public class TipoEvaluacionConstantes {
	
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	
	
	//Tipo de evaluaciones
	public static final String AUTOEVALUACION_LABEL = "AUTOEVALUACION";
	public static final int AUTOEVALUACION_VALUE = Integer.valueOf(1);
	
	public static final String EVALUACION_ESTUDIANTE_LABEL = "EVALUACION_ESTUDIANTE";
	public static final int EVALUACION_ESTUDIANTE_VALUE = Integer.valueOf(2);
	
	public static final String EVALUACION_DIRECTIVO_LABEL = "EVALUACION_DIRECTIVO";
	public static final int EVALUACION_DIRECTIVO_VALUE = Integer.valueOf(3);
	
	public static final String EVALUACION_PAR_ACADEMICO_LABEL = "EVALUACION_PAR_ACADEMICO";
	public static final int EVALUACION_PAR_ACADEMICO_VALUE = Integer.valueOf(4);
	
	//Nombre de Jasper reporte
	public static final String AUTOEVALUACION_JASPER = "autoEvaluacion";  
	public static final String EVALUACION_DIRECTIVO_JASPER = "evaluacionDirectivo";
	public static final String EVALUACION_PAR_ACADEMICO_JASPER = "evaluacionParAcademico";
	
}
