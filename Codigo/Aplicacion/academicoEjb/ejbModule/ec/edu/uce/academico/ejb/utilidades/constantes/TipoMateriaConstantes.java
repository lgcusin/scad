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
   
 ARCHIVO:     TipoMateriaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TipoMateria 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-ABRIL-2017		 Gabriel Mafla 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) TipoMateriaConstantes.
 * Clase que maneja las constantes de la entidad TipoMateria.
 * @author ghmafla.
 * @version 1.0
 */
public class TipoMateriaConstantes {
	
	public static final int ESTADO_TIPO_MATERIA_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_TIPO_MATERIA_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_TIPO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_TIPO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes para identificar la sub unidad de analisis y modulares
	public static final int TIPO_UNIDAD_ANALISIS_VALUE = 1;
	public static final String TIPO_UNIDAD_ANALISIS_LABEL = "UNIDAD DE ANÁLISIS";
	public static final int TIPO_SUB_UNIDAD_ANALISIS_VALUE = 11;
	public static final String TIPO_SUB_UNIDAD_ANALISIS_LABEL = "SUB UNIDAD DE ANÁLISIS";
	public static final int TIPO_MODULAR_VALUE = 13;
	public static final String TIPO_MODULAR_LABEL = "MODULAR";
	public static final int TIPO_MODULO_VALUE = 14;
	public static final String TIPO_MODULO_LABEL = "MÓDULO";
	public static final int TIPO_ASIGNATURA_VALUE = 2;
	public static final String TIPO_ASIGNATURA_LABEL = "ASIGNATURA";
	
}
