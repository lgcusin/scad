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
   
 ARCHIVO:     ParaleloConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Paralelo
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 28-ABRIL-2017           Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) ParaleloConstantes. Clase que maneja las constantes de la
 * entidad Paralelo.
 * 
 * @author dcollaguazo.
 * @version 1.0
 */
public class ParaleloConstantes {

	// constantes que indican el estado del paralelo
	public static final int ESTADO_ACTIVO_VALUE = 0;
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = 1;
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";

	public static final String PARALELO_HOMOLOGACION_LABEL = "HOMOLOGACION";
	public static final String PARALELO_REGISTRO_HISTORICO_LABEL = "HISTORICO";

	// CONSTANTES PARA LA CREACION DE PARALELOS - SUFICIENCIA INFORMATICA
	public static final String PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_REGULARES = "HI-REG-";
	public static final String PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_INTENSIVOS = "HI-INT-";
	public static final String PRL_SUFICIENCIA_INFORMATICA_ACRONIMO_EXONERACIONES = "HI-EXO-";

	// CONSTANTES PARA LA CREACION DE PARALELOS - SUFICIENCIA IDIOMAS
	public static final String PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_REGULARES = "ING-REG-";
	public static final String PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_INTENSIVOS = "ING-INT-";
	public static final String PRL_SUFICIENCIA_IDIOMA_INGLES_ACRONIMO_ONLINE = "ING-ONLINE-";
	
	public static final String PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_REGULARES = "ITA-REG-";
	public static final String PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_INTENSIVOS = "ITA-INT-";
	public static final String PRL_SUFICIENCIA_IDIOMA_ITALIANO_ACRONIMO_ONLINE = "ITA-ONLINE-";
	
	public static final String PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_REGULARES = "COR-REG-";
	public static final String PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_INTENSIVOS = "COR-INT-";
	public static final String PRL_SUFICIENCIA_IDIOMA_COREANO_ACRONIMO_ONLINE = "COR-ONLINE-";
	
	public static final String PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_REGULARES = "FRA-REG-";
	public static final String PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_INTENSIVOS = "FRA-INT-";
	public static final String PRL_SUFICIENCIA_IDIOMA_FRANCES_ACRONIMO_ONLINE = "FRA-ONLINE-";
	
	public static final String PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_REGULARES = "KIC-REG-";
	public static final String PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_INTENSIVOS = "KIC-INT-";
	public static final String PRL_SUFICIENCIA_IDIOMA_KICHWA_ACRONIMO_ONLINE = "KIC-ONLINE-";

	// CONSTANTES PARA LA CREACION DE PARALELOS - SUFICIENCIA CULTURA FISICA
	public static final String PRL_SUFICIENCIA_CULTURA_FISICA_ACRONIMO_REGULARES = "AFR-REG-";
	public static final String PRL_SUFICIENCIA_CULTURA_FISICA_ACRONIMO_INTENSIVOS = "AFI-INT-";

}
