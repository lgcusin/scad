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
   
 ARCHIVO:     HoraClaseConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad HoraClase 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) HoraClaseConstantes.
 * Clase que maneja las constantes de la entidad HoraClase.
 * @author dcollaguazo.
 * @version 1.0
 */
public class HoraClaseConstantes {
	
	//PARA IDENTIFICAR EL ESTADO
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO"; 
	
	//PARA IDENTIFICAR LAS HORAS STRING
	public static final int ESTADO_7_8_VALUE = Integer.valueOf(1);
	public static final String ESTADO_7_8_LABEL = "07:00 - 08:00";
	
	public static final int ESTADO_8_9_VALUE = Integer.valueOf(2);
	public static final String ESTADO_8_9_LABEL = "08:00 - 09:00";
	
	public static final int ESTADO_9_10_VALUE = Integer.valueOf(3);
	public static final String ESTADO_9_10_LABEL = "09:00 - 10:00";
	
	public static final int ESTADO_10_11_VALUE = Integer.valueOf(4);
	public static final String ESTADO_10_11_LABEL = "10:00 - 11:00";
	
	public static final int ESTADO_11_12_VALUE = Integer.valueOf(5);
	public static final String ESTADO_11_12_LABEL = "11:00 - 12:00";
	
	public static final int ESTADO_12_13_VALUE = Integer.valueOf(6);
	public static final String ESTADO_12_13_LABEL = "12:00 - 13:00";
	
	public static final int ESTADO_13_14_VALUE = Integer.valueOf(7);
	public static final String ESTADO_13_14_LABEL = "13:00 - 14:00";
	
	public static final int ESTADO_14_15_VALUE = Integer.valueOf(8);
	public static final String ESTADO_14_15_LABEL = "14:00 - 15:00";
	
	public static final int ESTADO_15_16_VALUE = Integer.valueOf(9);
	public static final String ESTADO_15_16_LABEL = "15:00 - 16:00";
	
	public static final int ESTADO_16_17_VALUE = Integer.valueOf(10);
	public static final String ESTADO_16_17_LABEL = "16:00 - 17:00";
	
	public static final int ESTADO_17_18_VALUE = Integer.valueOf(11);
	public static final String ESTADO_17_18_LABEL = "17:00 - 18:00";
	
	public static final int ESTADO_18_19_VALUE = Integer.valueOf(12);
	public static final String ESTADO_18_19_LABEL = "18:00 - 19:00";
	
	public static final int ESTADO_19_20_VALUE = Integer.valueOf(13);
	public static final String ESTADO_19_20_LABEL = "19:00 - 20:00";
	
	public static final int ESTADO_20_21_VALUE = Integer.valueOf(14);
	public static final String ESTADO_20_21_LABEL = "20:00 - 21:00";
	
	public static final int ESTADO_21_22_VALUE = Integer.valueOf(15);
	public static final String ESTADO_21_22_LABEL = "21:00 - 22:00";
	
	
	/**
	 * Metodo que busca la hora clase 
	 * @param hora - hora id del hora a traer
	 * @return - retorna la hora clase que selecciono
	 */
	public static String traerHoraClase(int hora){
		String retorno = "";
		if(hora == HoraClaseConstantes.ESTADO_7_8_VALUE){
			retorno = HoraClaseConstantes.ESTADO_7_8_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_8_9_VALUE){
			retorno = HoraClaseConstantes.ESTADO_8_9_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_9_10_VALUE){
			retorno = HoraClaseConstantes.ESTADO_9_10_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_10_11_VALUE){
			retorno = HoraClaseConstantes.ESTADO_10_11_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_11_12_VALUE){
			retorno = HoraClaseConstantes.ESTADO_11_12_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_12_13_VALUE){
			retorno = HoraClaseConstantes.ESTADO_12_13_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_13_14_VALUE){
			retorno = HoraClaseConstantes.ESTADO_13_14_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_14_15_VALUE){
			retorno = HoraClaseConstantes.ESTADO_14_15_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_15_16_VALUE){
			retorno = HoraClaseConstantes.ESTADO_15_16_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_16_17_VALUE){
			retorno = HoraClaseConstantes.ESTADO_16_17_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_17_18_VALUE){
			retorno = HoraClaseConstantes.ESTADO_17_18_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_18_19_VALUE){
			retorno = HoraClaseConstantes.ESTADO_18_19_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_19_20_VALUE){
			retorno = HoraClaseConstantes.ESTADO_19_20_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_20_21_VALUE){
			retorno = HoraClaseConstantes.ESTADO_20_21_LABEL;
		}
		if(hora == HoraClaseConstantes.ESTADO_21_22_VALUE){
			retorno = HoraClaseConstantes.ESTADO_21_22_LABEL;
		}
		
		return retorno;
	}
	
	
}
