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

 ARCHIVO:     UbicacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Ubicacion. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 15-MARZ-2017		 Dennis Collaguazo					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;


/**
 * Clase (constantes) UbicacionConstantes.
 * Clase que maneja las constantes de la entidad Ubicacion.
 * @author dcollaguazo.
 * @version 1.0
 */
public class UbicacionConstantes {

	//Constantes para tipo de identificacion UCE
	public static final int TIPO_JERARQUIA_PAIS_VALUE = Integer.valueOf(0);
	public static final String TIPO_JERARQUIA_PAIS_LABEL = "PAIS";
	public static final int TIPO_JERARQUIA_PROVINCIA_VALUE = Integer.valueOf(1);
	public static final String TIPO_JERARQUIA_PROVINCIA_LABEL = "PROVINCIA";
	public static final int TIPO_JERARQUIA_CANTON_VALUE = Integer.valueOf(2);
	public static final String TIPO_JERARQUIA_CANTON_LABEL = "CANTÓN";
	public static final int TIPO_JERARQUIA_PARROQUIA_VALUE = Integer.valueOf(3);
	public static final String TIPO_JERARQUIA_PARROQUIA_LABEL = "PARROQUIA";

	//Constantes para el el valor de ECUADOR
	public static final int ECUADOR_VALUE = Integer.valueOf(56);
	public static final String ECUADOR_LABEL = "ECUADOR";

}
