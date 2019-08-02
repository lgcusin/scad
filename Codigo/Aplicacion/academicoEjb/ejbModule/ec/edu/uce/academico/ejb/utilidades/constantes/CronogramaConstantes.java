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
   
 ARCHIVO:     CronogramaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Cronograma 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-03-2017          David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) CronogramaConstantes.
 * Clase que maneja las constantes de la entidad Cronograma.
 * @author darellano.
 * @version 1.0
 */
public class CronogramaConstantes {
	
	//constantes que indican es estado del cronograma
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican el tipo de cronograma
	public static final int TIPO_ACADEMICO_VALUE = Integer.valueOf(0);
	public static final String TIPO_ACADEMICO_LABEL = "CRONOGRAMA PREGRADO";
	public static final int TIPO_NIVELACION_VALUE = Integer.valueOf(1);	
	public static final String TIPO_NIVELACION_LABEL = "CRONOGRAMA NIVELACIÓN";
	public static final int TIPO_CONTRATACION_VALUE = Integer.valueOf(2);	
	public static final String TIPO_CONTRATACION_LABEL = "CRONOGRAMA CONTRATACIÓN";
	public static final int TIPO_POSGRADO_VALUE = Integer.valueOf(3);	
	public static final String TIPO_POSGRADO_LABEL = "CRONOGRAMA POSGRADO";
	public static final int TIPO_HOMOLOGACION_VALUE = Integer.valueOf(4);	
	public static final String TIPO_HOMOLOGACION_LABEL = "CRONOGRAMA HOMOLOGACION";
	public static final int TIPO_SUFICIENCIA_IDIOMAS_VALUE = 5;	
	public static final String TIPO_SUFICIENCIA_IDIOMAS_LABEL = "SUFICIENCIA - IDIOMAS";
	public static final int TIPO_SUFICIENCIA_CULTURA_FISICA_VALUE = 6;	
	public static final String TIPO_SUFICIENCIA_CULTURA_FISICA_LABEL = "SUFICIENCIA - CULTURA FÍSICA";
	public static final int TIPO_SUFICIENCIA_INFORMATICA_VALUE = 7; // presencial	
	public static final String TIPO_SUFICIENCIA_INFORMATICA_LABEL = "SUFICIENCIA - INFORMÁTICA PRESENCIAL";
	public static final int TIPO_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE = 8;	
	public static final String TIPO_SUFICIENCIA_INFORMATICA_INTENSIVO_LABEL = "SUFICIENCIA - INFORMÁTICA INTENSIVO";
	public static final int TIPO_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE = 9;	
	public static final String TIPO_SUFICIENCIA_INFORMATICA_EXONERACION_LABEL = "SUFICIENCIA - INFORMÁTICA EXONERACIÓN";
	public static final int TIPO_VERANO_NIVELACION_EN_LINEA_VALUE = 10;	
	public static final String TIPO_VERANO_NIVELACION_EN_LINEA_LABEL = "CURSO DE VERANO NIVELACION EN LINEA";
	
	
}
