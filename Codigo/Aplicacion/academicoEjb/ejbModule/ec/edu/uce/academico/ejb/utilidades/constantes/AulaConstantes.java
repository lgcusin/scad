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
   
 ARCHIVO:     AulaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad AulaConstantes 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26-06-2017		Marcelo Quishpe			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) AulaConstantes.
 * Clase que maneja las constantes de la entidad AulaConstantes.
 * @author lmquishpei.
 * @version 1.0
 */
public class AulaConstantes {
	
	//constantes para el tipo de aula
	public static final Integer TIPO_AULA_VALUE = Integer.valueOf(0);
	public static final String TIPO_AULA_LABEL = "AULA";
	public static final Integer TIPO_LABORATORIO_VALUE = Integer.valueOf(1);
	public static final String TIPO_LABORATORIO_LABEL = "LABORATORIO";
	//constantes para el estado del aula
	public static final Integer ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final Integer ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";

}
