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
   
 ARCHIVO:     RelacionLaboralConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad RelacionLaboral 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-01-2018		 	Arturo Villafuerte 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) RelacionLaboralConstantes.
 * Clase que maneja las constantes de la entidad RelacionLaboral.
 * @author ajvillafuerte.
 * @version 1.0
 */
public class RelacionLaboralConstantes {
	 
	public static final String RELACION_LABORAL_CON_RELACION_DEPENDENCIA_LABEL = "CON RELACION DE DEPENDENCIA";
	public static final Integer RELACION_LABORAL_CON_RELACION_DEPENDENCIA_VALUE = Integer.valueOf(1);
	public static final String RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_LABEL = "SIN RELACION DE DEPENDENCIA";
	public static final Integer RELACION_LABORAL_SIN_RELACION_DEPENDENCIA_VALUE = Integer.valueOf(2);
	public static final String RELACION_LABORAL_NOMBRAMIENTO_LABEL = "NOMBRAMIENTO";
	public static final Integer RELACION_LABORAL_NOMBRAMIENTO_VALUE = Integer.valueOf(3);
	 
}
