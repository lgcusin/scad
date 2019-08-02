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
   
 ARCHIVO:     ContenidoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Contenido.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-ENERO-2018		 Arturo Villafuerte 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) ContenidoConstantes.
 * Clase que maneja las constantes de la entidad Contenido.
 * @author ajvillafuerte.
 * @version 1.0
 */
public class ContenidoConstantes {
	
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO"; 
	
  
	//constantes para el proceso de apelacion
	public static final int ESTADO_APELACION_SOLICITADA_VALUE = Integer.valueOf(0);
	public static final String ESTADO_APELACION_SOLICITADA_LABEL = "SOLICITADA";
	public static final int ESTADO_APELACION_APROBADA_VALUE = Integer.valueOf(1);
	public static final String ESTADO_APELACION_APROBADA_LABEL = "APROBADA";
	public static final int ESTADO_APELACION_NEGADA_VALUE = Integer.valueOf(2);
	public static final String ESTADO_APELACION_NEGADA_LABEL = "NEGADA";
	public static final int ESTADO_APELACION_EJECUTADA_VALUE = Integer.valueOf(3);
	public static final String ESTADO_APELACION_EJECUTADA_LABEL = "EJECUTADA";
	
}
