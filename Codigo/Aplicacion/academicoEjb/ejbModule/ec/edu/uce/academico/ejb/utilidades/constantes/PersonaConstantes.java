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
   
 ARCHIVO:     PersonaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Persona. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * Clase (constantes) UsuarioConstantes.
 * Clase que maneja las constantes de la entidad Persona.
 * @author dCollaguazo.
 * @version 1.0
 */
public class PersonaConstantes {
	
	//Constantes para tipo de identificacion UCE
	public static final int TIPO_IDENTIFICACION_CEDULA_VALUE = Integer.valueOf(0);
	public static final String TIPO_IDENTIFICACION_CEDULA_LABEL = "CÉDULA";
	public static final int TIPO_IDENTIFICACION_PASAPORTE_VALUE = Integer.valueOf(1);
	public static final String TIPO_IDENTIFICACION_PASAPORTE_LABEL = "PASAPORTE/OTROS";
	
	//Constantes para tipo de identificacion SNIESE
	public static final int TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE = Integer.valueOf(1);
	public static final String TIPO_IDENTIFICACION_CEDULA_SNIESE_LABEL = "CÉDULA";
	public static final int TIPO_IDENTIFICACION_PASAPORTE_SNIESE_VALUE = Integer.valueOf(2);
	public static final String TIPO_IDENTIFICACION_PASAPORTE_SNIESE_LABEL = "PASAPORTE/OTROS";
	
	//Constantes para tipo de identificacion UCE
	public static final int SEXO_HOMBRE_VALUE = Integer.valueOf(0);
	public static final String SEXO_HOMBRE_LABEL = "HOMBRE";
	public static final int SEXO_MUJER_VALUE = Integer.valueOf(1);
	public static final String SEXO_MUJER_LABEL = "MUJER";
	public static final int SEXO_GENERICO_VALUE = Integer.valueOf(2);
	public static final String SEXO_GENERICO_LABEL = "GENERICO";
	
	//Constantes para tipo de identificacion SNIESE
	public static final int SEXO_HOMBRE_SNIESE_VALUE = Integer.valueOf(1);
	public static final String SEXO_HOMBRE_SNIESE_LABEL = "HOMBRE";
	public static final int SEXO_MUJER_SNIESE_VALUE = Integer.valueOf(2);
	public static final String SEXO_MUJER_SNIESE_LABEL = "MUJER";	
	
	//Constantes para indicar si posee discapacidad 
	public static final int DISCAPACIDAD_SI_VALUE = Integer.valueOf(0);
	public static final String DISCAPACIDAD_SI_LABEL = "SI";
	public static final int  DISCAPACIDAD_NO_VALUE = Integer.valueOf(1);
	public static final String DISCAPACIDAD_NO_LABEL = "NO";
	
	// Constantes para indicar si posee carnet del CONADIS
	public static final int CARNET_CONADIS_SI_VALUE = Integer.valueOf(0);
	public static final String CARNET_CONADIS_SI_LABEL = "SI";
	public static final int CARNET_CONADIS_NO_VALUE = Integer.valueOf(1);
	public static final String CARNET_CONADIS_NO_LABEL = "NO";

	// Constantes para indicar el tipo de discapacidad
	public static final int TIPO_DISCAPACIDAD_AUDITIVA_VALUE = Integer.valueOf(0);
	public static final String TIPO_DISCAPACIDAD_AUDITIVA_LABEL = "AUDITIVA";
	public static final int TIPO_DISCAPACIDAD_FISICA_VALUE = Integer.valueOf(1);
	public static final String TIPO_DISCAPACIDAD_FISICA_LABEL = "FISICA";
	public static final int TIPO_DISCAPACIDAD_INTELECTUAL_VALUE = Integer.valueOf(2);
	public static final String TIPO_DISCAPACIDAD_INTELECTUAL_LABEL = "INTELECTUAL";
	public static final int TIPO_DISCAPACIDAD_VISUAL_VALUE = Integer.valueOf(3);
	public static final String TIPO_DISCAPACIDAD_VISUAL_LABEL = "VISUAL";
	
	
		//Constantes para indicar el estado civil
	public static final int ESTADO_CIVIL_CASADO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_CIVIL_CASADO_LABEL = "CASADO/A";
	public static final int ESTADO_CIVIL_DIVORCIADO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_CIVIL_DIVORCIADO_LABEL = "DIVORCIADO/A";
	public static final int ESTADO_CIVIL_SOLTERO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_CIVIL_SOLTERO_LABEL = "SOLTERO/A";
	public static final int ESTADO_CIVIL_UNION_DE_HECHO_VALUE = Integer.valueOf(3);
	public static final String ESTADO_CIVIL_UNION_DE_HECHO_LABEL = "UNION DE HECHO";
	public static final int ESTADO_CIVIL_VIUDO_VALUE = Integer.valueOf(4);
	public static final String ESTADO_CIVIL_VIUDO_LABEL = "VIUDO/A";
	public static final int ESTADO_CIVIL_SEPARADO_VALUE = Integer.valueOf(5);
	public static final String ESTADO_CIVIL_SEPARADO_LABEL = "SEPARADO/A";
		
	//constantes para el estado
	public static final Integer ESTADO_SEGURO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_SEGURO_ACTIVO_LABEL = "ACTIVO";
	public static final Integer ESTADO_SEGURO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_SEGURO_INACTIVO_LABEL = "INACTIVO";
		
	/**
	 * Retorna el valor del tipo identificacion Sniese
	 * @param tipoIdSniese - valor de un tipo de identificacion Sniese
	 * @return el valor del tipo identificacion Sniese
	 */
	public static int traerTipoIdEsnieseXTipoIdUce(int tipoIdSniese) throws PersonaValidacionException{
		int retorno = -99;
		if(tipoIdSniese == TIPO_IDENTIFICACION_CEDULA_VALUE){
			retorno = TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE;
		}else if(tipoIdSniese == TIPO_IDENTIFICACION_PASAPORTE_VALUE){
			retorno = TIPO_IDENTIFICACION_CEDULA_SNIESE_VALUE;
		}else{
			throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaConstantes.traer.tipo.id.sniese.id.tipoUce.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Retorna el valor del tipo sexo Sniese
	 * @param tipoSexoSniese - valor de un tipo sexo Sniese
	 * @return el valor del tipo sexo Sniese
	 */
	public static int traerSexoEsnieseXSexoUce(int tipoSexoSniese) throws PersonaValidacionException{
		int retorno = -99;
		if(tipoSexoSniese == SEXO_HOMBRE_VALUE){
			retorno = SEXO_HOMBRE_SNIESE_VALUE;
		}else if(tipoSexoSniese == SEXO_MUJER_VALUE){
			retorno = SEXO_MUJER_SNIESE_VALUE;
		}else{
			throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("PersonaConstantes.traer.sexo.sniese.sexo.uce.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo de un tipo de identificacion UCE
	 * @param tipoIdentUce - valor de un tipo de identificacion UCE
	 * @return label del valor representativo de un tipo de identificacion UCE
	 */
	public static String valueToLabelTipoIdentificacionUce(int tipoIdentUce) {
		String retorno = "";
		if(tipoIdentUce == TIPO_IDENTIFICACION_CEDULA_VALUE){
			retorno = TIPO_IDENTIFICACION_CEDULA_LABEL;
		}else if(tipoIdentUce == TIPO_IDENTIFICACION_PASAPORTE_VALUE){
			retorno = TIPO_IDENTIFICACION_PASAPORTE_LABEL;
		}
		return retorno;
	}
	
	
	/**
	 * Retorna el label del valor representativo de un tipo de sexo UCE
	 * @param tipoSexoUce - valor de un tipo de sexo UCE
	 * @return label del valor representativo de un tipo de sexo UCE
	 */
	public static String valueToLabelTipoSexoUce(int tipoSexoUce) {
		String retorno = "";
		if(tipoSexoUce == SEXO_HOMBRE_VALUE){
			retorno = SEXO_HOMBRE_LABEL;
		}else if(tipoSexoUce == SEXO_MUJER_VALUE){
			retorno = SEXO_MUJER_LABEL;
		}
		return retorno;
	}
	
	
	
	
	
}
