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
   
 ARCHIVO:     UsuarioRolConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad UsuarioRolConstantes 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) UsuarioRolConstantes.java
 * Clase que maneja las constantes de la entidad UsuarioRolConstantes.
 * @author dcollaguazo.
 * @version 1.0
 */
public class UsuarioRolConstantes {
	
	//TODO revisar si el comentario y las constantes corresponde a la entidad 
	//constantes para el campo roflcr_estado
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	
}
