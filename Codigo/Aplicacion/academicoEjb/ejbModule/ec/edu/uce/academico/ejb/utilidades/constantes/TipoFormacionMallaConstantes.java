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
   
 ARCHIVO:     TipoFormacionMallaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TipoFormacionMalla 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 22-MARZ-2017		 Gabriel Mafla 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoFormacionMallaConstantes.
 * Clase que maneja las constantes de la entidad TipoFormacionMalla.
 * @author ghmafla, fgguzman.
 * @version 1.0,2.0
 */
public class TipoFormacionMallaConstantes {
	
	public static final int ESTADO_TIPO_FORMACION_MALLA_ACTIVO_VALUE = 0;
	public static final String ESTADO_TIPO_FORMACION_MALLA_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_TIPO_FORMACION_MALLA_INACTIVO_VALUE = 1;
	public static final String ESTADO_TIPO_FORMACION_MALLA_INACTIVO_LABEL = "INACTIVO";
	
	/**
	 * @author fgguzman
	 */
	public static final int TIFRML_EDUCACION_VALUE = 1;	
	public static final int TIFRML_INGENIERIA_VALUE = 2;	
	public static final int TIFRML_NIVELACION_VALUE = 3;	
	public static final int TIFRML_POSGRADO_VALUE = 4;	
	public static final int TIFRML_MIGRACION_SAU_VALUE = 5;	
	public static final int TIFRML_SUFICIENCIA_VALUE = 6;
	
	public static final String TIFRML_EDUCACION_LABEL = "EDUCACIÓN";	
	public static final String TIFRML_INGENIERIA_LABEL = "INGENIERÍA";	
	public static final String TIFRML_NIVELACION_LABEL = "NIVELACIÓN";	
	public static final String TIFRML_POSGRADO_LABEL = "POSTGRADO";	
	public static final String TIFRML_MIGRACION_SAU_LABEL = "PROCESO DE MIGRACIÓN SAU";	
	public static final String TIFRML_SUFICIENCIA_LABEL = "SUFICIENCIA";	

	
}
