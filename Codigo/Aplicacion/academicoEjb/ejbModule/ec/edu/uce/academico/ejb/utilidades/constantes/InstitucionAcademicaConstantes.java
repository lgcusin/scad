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
   
 ARCHIVO:     InstitucionAcademicaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad InstitucionAcademica. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-MARZ-2017		 Dennis Collaguazo					Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * Clase (constantes) InstitucionAcademicaConstantes.
 * Clase que maneja las constantes de la entidad InstitucionAcademica.
 * @author dcollaguazo.
 * @version 1.0
 */
public class InstitucionAcademicaConstantes {
	
	//Constantes para tipo nivel de intitucion academica
	public static final int NIVEL_SECUNDARIA_VALUE = Integer.valueOf(0);
	public static final String NIVEL_SECUNDARIA_LABEL = "SECUNDARIA";
	public static final int NIVEL_UNIVERSIDAD_VALUE = Integer.valueOf(1);
	public static final String NIVEL_UNIVERSIDAD_LABEL = "TERCER NIVEL";
	
	//Constantes para tipo tipo de intitucion academica
	public static final int TIPO_FISCAL_VALUE = Integer.valueOf(0);
	public static final String TIPO_FISCAL_LABEL = "FISCAL";
	public static final int TIPO_FISCOMISIONAL_VALUE = Integer.valueOf(1);
	public static final String TIPO_FISCOMISIONAL_LABEL = "FISCOMISIONAL";
	public static final int TIPO_PARTICULAR_VALUE = Integer.valueOf(2);
	public static final String TIPO_PARTICULAR_LABEL = "PARTICULAR";
	public static final int TIPO_MUNICIPAL_VALUE = Integer.valueOf(3);
	public static final String TIPO_MUNICIPAL_LABEL = "MUNICIPAL";
	
	//Constantes para tipo tipo de intitucion academica
	public static final int TIPO_SNIESE_FISCAL_VALUE = Integer.valueOf(1);
	public static final String TIPO_SNIESE_FISCAL_LABEL = "FISCAL";
	public static final int TIPO_SNIESE_FISCOMISIONAL_VALUE = Integer.valueOf(2);
	public static final String TIPO_SNIESE_FISCOMISIONAL_LABEL = "FISCOMISIONAL";
	public static final int TIPO_SNIESE_PARTICULAR_VALUE = Integer.valueOf(3);
	public static final String TIPO_SNIESE_PARTICULAR_LABEL = "PARTICULAR";
	public static final int TIPO_SNIESE_MUNICIPAL_VALUE = Integer.valueOf(4);
	public static final String TIPO_SNIESE_MUNICIPAL_LABEL = "MUNICIPAL";
	
	public static int traerTipoEsnieseXTipoUce(int tipoSniese) throws PersonaValidacionException{
		int retorno = -99;
		if(tipoSniese == TIPO_FISCAL_VALUE){
			retorno = TIPO_SNIESE_FISCAL_VALUE;
		}else if(tipoSniese == TIPO_FISCOMISIONAL_VALUE){
			retorno = TIPO_SNIESE_FISCOMISIONAL_VALUE;
		}else if(tipoSniese == TIPO_PARTICULAR_VALUE){
			retorno = TIPO_SNIESE_PARTICULAR_VALUE;
		}else if(tipoSniese == TIPO_MUNICIPAL_VALUE){
			retorno = TIPO_SNIESE_MUNICIPAL_VALUE;
		}else{
			throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaConstantes.traer.tipo.identificacion.sniese.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Retorna el label del valor representativo de un tipo de colegio UCE
	 * @param tipoColegioUce - valor de un tipo de colegio UCE
	 * @return label del valor representativo de un tipo de colegio UCE
	 */
	public static String valueToLabelTipoColegioUce(int tipoColegioUce) {
		String retorno = "";
		if(tipoColegioUce == TIPO_FISCAL_VALUE){
			retorno = TIPO_FISCAL_LABEL;
		}else if(tipoColegioUce == TIPO_FISCOMISIONAL_VALUE){
			retorno = TIPO_FISCOMISIONAL_LABEL;
		}else if(tipoColegioUce == TIPO_PARTICULAR_VALUE){
			retorno = TIPO_PARTICULAR_LABEL;
		}else if(tipoColegioUce == TIPO_MUNICIPAL_VALUE){
			retorno = TIPO_MUNICIPAL_LABEL;
		}
		return retorno;
	}
	
}
