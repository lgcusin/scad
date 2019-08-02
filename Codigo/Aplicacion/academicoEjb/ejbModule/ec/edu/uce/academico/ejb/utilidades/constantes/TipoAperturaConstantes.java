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
   
 ARCHIVO:     TipoAperturaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TipoAperturaConstantes 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-03-2018			Arturo Villafuerte			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoAperturaConstantes.
 * Clase que maneja las constantes de la entidad TipoAperturaConstantes.
 * @author ajvillafuerte.
 * @version 1.0
 */
public class TipoAperturaConstantes {
	 
	//constantes para tipo TipoApertura
	public static final Integer TPAP_POR_UNIVERSIDAD_VALUE = Integer.valueOf(1);
	public static final String TPAP_POR_UNIVERSIDAD_LABEL = "POR UNIVERSIDAD";
	public static final Integer TPAP_POR_FACULTAD_VALUE = Integer.valueOf(2);
	public static final String TPAP_POR_FACULTAD_LABEL = "POR FACULTAD";
	public static final Integer TPAP_POR_CARRERA_VALUE = Integer.valueOf(3);
	public static final String TPAP_POR_CARRERA_LABEL = "POR CARRERA";
	public static final Integer TPAP_POR_DOCENTE_VALUE = Integer.valueOf(4);
	public static final String TPAP_POR_DOCENTE_LABEL = "POR DOCENTE";
}
