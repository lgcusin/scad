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
   
 ARCHIVO:     ReferenciaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Referencia 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 11-12-2018			Dennis Collaguazo			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) ReferenciaConstantes.
 * Clase que maneja las constantes de la entidad Referencia.
 * @author dcollaguazo.
 * @version 1.0
 */
public class ReferenciaConstantes {
	 
	//constantes para el tipo de parentesco
	public static final Integer PARENTESCO_PADRES_VALUE = Integer.valueOf(1);
	public static final String PARENTESCO_PADRES_LABEL = "PADRE/MADRE";
	public static final Integer PARENTESCO_SUEGROS_VALUE = Integer.valueOf(2);
	public static final String PARENTESCO_SUEGROS_LABEL = "SUEGRO/A";
	public static final Integer PARENTESCO_HIJOS_VALUE = Integer.valueOf(3);
	public static final String PARENTESCO_HIJOS_LABEL = "HIJO/A";
	public static final Integer PARENTESCO_YERNO_NUERA_VALUE = Integer.valueOf(4);
	public static final String PARENTESCO_YERNO_NUERA_LABEL = "YERNO/NUERA";
	public static final Integer PARENTESCO_ABUELOS_VALUE = Integer.valueOf(5);
	public static final String PARENTESCO_ABUELOS_LABEL = "ABUELO/A";
	public static final Integer PARENTESCO_HERMANOS_VALUE = Integer.valueOf(6);
	public static final String PARENTESCO_HERMANOS_LABEL = "HERMANO/A";
	public static final Integer PARENTESCO_CUNADO_VALUE = Integer.valueOf(7);
	public static final String PARENTESCO_CUNADO_LABEL = "CUÑADO/A";
	public static final Integer PARENTESCO_NIETOS_VALUE = Integer.valueOf(8);
	public static final String PARENTESCO_NIETOS_LABEL = "NIETO/A";
	public static final Integer PARENTESCO_BISABUELOS_VALUE = Integer.valueOf(9);
	public static final String PARENTESCO_BISABUELOS_LABEL = "BISABUELO/A";
	public static final Integer PARENTESCO_TIOS_VALUE = Integer.valueOf(10);
	public static final String PARENTESCO_TIOS_LABEL = "TIO/A";
	public static final Integer PARENTESCO_SOBRINOS_VALUE = Integer.valueOf(11);
	public static final String PARENTESCO_SOBRINOS_LABEL = "SOBRINO/A";
	public static final Integer PARENTESCO_BIZNIETOS_VALUE = Integer.valueOf(12);
	public static final String PARENTESCO_BIZNIETOS_LABEL = "BIZNIETO/A";
	public static final Integer PARENTESCO_PRIMOS_VALUE = Integer.valueOf(13);
	public static final String PARENTESCO_PRIMOS_LABEL = "PRIMO/A";
	public static final Integer PARENTESCO_CONYUGE_VALUE = Integer.valueOf(14);
	public static final String PARENTESCO_CONYUGE_LABEL = "CONYUGE";
	public static final Integer PARENTESCO_OTROS_VALUE = Integer.valueOf(15);
	public static final String PARENTESCO_OTROS_LABEL = "OTRO/A";
	
	//constantes para el estado
	public static final Integer ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final Integer ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//contantes para el tipo de beneficiario
	public static final Integer TIPO_BENEFICIARIO_SEGURO_VIDA_VALUE = Integer.valueOf(1);
	public static final String TIPO_BENEFICIARIO_SEGURO_VIDA_LABEL = "SEGURO DE VIDA";

}
