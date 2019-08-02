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
   
 ARCHIVO:     FichaEstudianteConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad FichaEstudiante 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-MAR-2017           Vinicio Rosales                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteValidacionException;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * Clase (constantes) FichaEstudiaanteConstantes.
 * Clase que maneja las constantes de la entidad FichaEstudiante.
 * @author jvrosales.
 * @version 1.0
 */
public class FichaEstudianteConstantes {
	
	//constantes que indican si tiene o no reconocimiento de estudios previos de la UCE
	public static final int RECON_ESTUD_PREVIOS_SI_VALUE = Integer.valueOf(0);
	public static final String RECON_ESTUD_PREVIOS_SI_LABEL = "SI";
	public static final int RECON_ESTUD_PREVIOS_NO_VALUE = Integer.valueOf(1);	
	public static final String RECON_ESTUD_PREVIOS_NO_LABEL = "NO";
	
	//constantes que indican si tiene o no reconocimiento de estudios previos de la SNIESE
	public static final int RECON_ESTUD_PREVIOS_SNIESE_SI_VALUE = Integer.valueOf(1);
	public static final String RECON_ESTUD_PREVIOS_SNIESE_SI_LABEL = "SI";
	public static final int RECON_ESTUD_PREVIOS_SNIESE_NO_VALUE = Integer.valueOf(2);	
	public static final String RECON_ESTUD_PREVIOS_SNIESE_NO_LABEL = "NO";	
	
	//constantes que indican el tipo de duracion reconocimiento
	public static final int TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE = Integer.valueOf(0);
	public static final String TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL = "AÑOS";
	public static final int TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE = Integer.valueOf(1);
	public static final String TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL = "SEMESTRES";
	public static final int TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE = Integer.valueOf(2);
	public static final String TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL = "CRÉDITOS";
	
	//constantes que indican el tipo de colegio
	public static final int TIPO_COLEGIO_FISCAL_VALUE = Integer.valueOf(0);
	public static final String TIPO_COLEGIO_FISCAL_LABEL = "FISCAL";
	public static final int TIPO_COLEGIO_FISCOMISIONAL_VALUE = Integer.valueOf(1);
	public static final String TIPO_COLEGIO_FISCOMISIONAL_LABEL = "FISCOMISIONAL";
	public static final int TIPO_COLEGIO_PARTICULAR_VALUE = Integer.valueOf(2);
	public static final String TIPO_COLEGIO_PARTICULAR_LABEL = "PARTICULAR";
	public static final int TIPO_COLEGIO_MUNICIPAL_VALUE = Integer.valueOf(3);
	public static final String TIPO_COLEGIO_MUNICIPAL_LABEL = "MUNICIPAL";
	
	//constantes que indican el estado del estudiante
	public static final int ESTADO_ESTUDIANTE_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ESTUDIANTE_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_ESTUDIANTE_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_ESTUDIANTE_INACTIVO_LABEL = "INACTIVO";
	
	public static final String ESTADO_TERCERA_MATRICULA_REQUIERE_APROBACION_LABEL = "REQUIERE APROBACION";
	public static final String ESTADO_TERCERA_MATRICULA_APROBADO_LABEL = "APROBADO";
	
	/**
	 * Retorna si tiene o no reconocimiento de estu¿dios previos de SNIES a partir del tipo de UEC
	 * @param recoPrevUce - recnocimiento de estudioas previos tipo UCE
	 * @return si tiene o no reconocimiento de estu¿dios previos de SNIES a partir del tipo de UEC
	 */
	public static int traerTipoReconocimientoEsnieseXUce(int recoPrevUce) throws FichaEstudianteValidacionException{
		int retorno = -99;
		if(recoPrevUce == RECON_ESTUD_PREVIOS_SI_VALUE){
			retorno = RECON_ESTUD_PREVIOS_SNIESE_SI_VALUE;
		}else if(recoPrevUce == RECON_ESTUD_PREVIOS_NO_VALUE){
			retorno = RECON_ESTUD_PREVIOS_SNIESE_NO_VALUE;
		}else{
			throw new FichaEstudianteValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudianteConstantes.traer.tipo.reconocimiento.estudios.previos.sniese.exception")));
		}
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo de un tipo de reconocimiento UCE
	 * @param tipoRecUce - valor de un tipo de reconocimiento UCE
	 * @return label del valor representativo de un tipo de reconocimiento UCE
	 */
	public static String valueToLabelTipoRecUce(int tipoRecUce) {
		String retorno = "";
		if(tipoRecUce == RECON_ESTUD_PREVIOS_SI_VALUE){
			retorno = RECON_ESTUD_PREVIOS_SI_LABEL;
		}else if(tipoRecUce == RECON_ESTUD_PREVIOS_NO_VALUE){
			retorno = RECON_ESTUD_PREVIOS_NO_LABEL;
		}
		return retorno;
	}
	
	/**
	 * Retorna el label del valor representativo de un tipo de duracion UCE
	 * @param tipoDuracUce - valor de un tipo de duracion UCE
	 * @return label del valor representativo de un tipo de duracion UCE
	 */
	public static String valueToLabelTipoDuracUce(int tipoDuracUce) {
		String retorno = "";
		if(tipoDuracUce == TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE){
			retorno = TIPO_DURACION_RECONOCIMIENTO_ANIOS_LABEL;
		}else if(tipoDuracUce == TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE){
			retorno = TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_LABEL;
		}else if(tipoDuracUce == TIPO_DURACION_RECONOCIMIENTO_CREDITOS_VALUE){
			retorno = TIPO_DURACION_RECONOCIMIENTO_CREDITOS_LABEL;
		}
		return retorno;
	}
	
	
	
	//constantes tipo universidad de estudios previos
		public static final int TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_VALUE = Integer.valueOf(0);
		public static final String TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PUBLICA_LABEL = "UNIVERSIDAD PÚBLICA";
		public static final int TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PRIVADA_VALUE = Integer.valueOf(1);
		public static final String TIPO_UNIVERSIDAD_ESTUD_PREVIOS_PRIVADA_LABEL = "UNIVERSIDAD PRIVADA";
		
		//constantes tipo universidad de estudios previos
		public static final int ESTADO_ESTUD_PREV_TITULADO_VALUE = Integer.valueOf(0);
		public static final String ESTADO_ESTUD_PREV_TITULADO_LABEL = "TITULADO";
		public static final int ESTADO_ESTUD_PREV_CURSANDO_VALUE = Integer.valueOf(1);
		public static final String ESTADO_ESTUD_PREV_CURSANDO_LABEL = "CURSANDO";
		
}
