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
   
 ARCHIVO:     TipoContenidoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ContenidoDocente.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-ENERO-2018		 Arturo Villafuerte 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoContenidoConstantes.
 * Clase que maneja las constantes de la entidad TipoContenido.
 * @author ajvillafuerte.
 * @version 1.0
 */
public class TipoContenidoConstantes {
	
	//-- Estados
	
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	
	//--
	public static final String TIPO_CABECERA_LABEL = "CABECERA";
	public static final int TIPO_CABECERA_VALUE = Integer.valueOf(0);
	
	public static final String TIPO_PREGUNTA_LABEL = "PREGUNTA";
	public static final int TIPO_PREGUNTA_VALUE = Integer.valueOf(1);
	
	//-- Tipo Seleccion
	public static final String TIPO_SELECCION_UNICO_LABEL = "SELECCION UNICA";
	public static final int TIPO_SELECCION_UNICO_VALUE = Integer.valueOf(0);
	public static final String VALORACION_SI_LABEL = "SI";
	public static final String VALORACION_NO_LABEL = "NO";
	public static final int VALORACION_SI_VALUE = Integer.valueOf(0);
	public static final int VALORACION_NO_VALUE = Integer.valueOf(1);
	
	public static final String TIPO_SELECCION_MULTIPLE_LABEL = "SELECCION MULTIPLE";
	public static final int TIPO_SELECCION_MULTIPLE_VALUE = Integer.valueOf(1);
	public static final String VALORACION_NUNCA_LABEL = "N";
	public static final String VALORACION_A_VECES_LABEL = "AV";
	public static final String VALORACION_CASI_SIEMPRE_LABEL = "CS";
	public static final String VALORACION_SIEMPRE_LABEL = "S";
	
	public static final String VALORACION_NUNCA_LABEL_COMPLETO = "NUNCA";
	public static final String VALORACION_A_VECES_LABEL_COMPLETO = "A VECES";
	public static final String VALORACION_CASI_SIEMPRE_LABEL_COMPLETO = "CASI SIEMPRE";
	public static final String VALORACION_SIEMPRE_LABEL_COMPLETO = "SIEMPRE";
	
	public static final int VALORACION_NUNCA_VALUE = Integer.valueOf(0);
	public static final int VALORACION_A_VECES_VALUE = Integer.valueOf(1);
	public static final int VALORACION_CASI_SIEMPRE_VALUE = Integer.valueOf(2);
	public static final int VALORACION_SIEMPRE_VALUE = Integer.valueOf(3);
	
	public static final String TIPO_SELECCION_MULTIPLE_TRES_LABEL = "SELECCION MULTIPLE TRES";
	public static final int TIPO_SELECCION_MULTIPLE_TRES_VALUE = Integer.valueOf(2);
	public static final String VALORACION_NO_APLICA_LABEL = "N/A";
	public static final int VALORACION_NO_APLICA_VALUE = Integer.valueOf(3);
	
	public static final String VALORACION_SIN_ETIQUETA_LABEL = "SIN ETIQUETA"; 
	
}
