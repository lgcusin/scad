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
   
 ARCHIVO:     ModalidadConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ModalidadConstantes 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-03-2018			Arturo Villafuerte			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) ModalidadConstantes.
 * Clase que maneja las constantes de la entidad ModalidadConstantes.
 * @author ajvillafuerte.
 * @version 1.0
 */
public class ModalidadConstantes {
	 
	//constantes para tipo Modalidad
	public static final Integer TIPO_MODALIDAD_PRESENCIAL_VALUE = Integer.valueOf(1);
	public static final String TIPO_MODALIDAD_PRESENCIAL_LABEL = "PRESENCIAL";
	public static final Integer TIPO_MODALIDAD_SEMIPRESENCIAL_VALUE = Integer.valueOf(2);
	public static final String TIPO_MODALIDAD_SEMIPRESENCIAL_LABEL = "SEMIPRESENCIAL";
	public static final Integer TIPO_MODALIDAD_DISTANCIA_VALUE = Integer.valueOf(3);
	public static final String TIPO_MODALIDAD_DISTANCIA_LABEL = "DISTANCIA";

	//constantes para tipoModalidad - suficiencia informatica / idiomas
	public static final int MDL_PRESENCIAL_REGULAR_VALUE = 1;
	public static final String MDL_PRESENCIAL_REGULAR_LABEL = "REGULARES";// L-V
	public static final int MDL_PRESENCIAL_INTENSIVO_VALUE = 2;
	public static final String MDL_PRESENCIAL_INTENSIVO_LABEL = "INTENSIVOS"; // L-S
	public static final int MDL_PRESENCIAL_EXONERACION_VALUE = 3;
	public static final String MDL_PRESENCIAL_EXONERACION_LABEL = "EXONERACIONES";//solo informatica
	public static final int MDL_PRESENCIAL_ON_LINE_VALUE = 4;
	public static final String MDL_PRESENCIAL_ON_LINE_LABEL = "ON-LINE";//solo idiomas
}
