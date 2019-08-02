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
   
 ARCHIVO:     PeriodoAcademicoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad PeriodoAcademico 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 22-MARZ-2017		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) PeriodoAcademicoConstantes. Clase que maneja las
 * constantes de la entidad PeriodoAcademico.
 * 
 * @author dcollaguazo.
 * @version 1.0
 */
public class PeriodoAcademicoConstantes {

	public static final int ESTADO_ACTIVO_VALUE = 0;
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = 1;
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_EN_CIERRE_VALUE = 2;
	public static final String ESTADO_EN_CIERRE_LABEL = "EN CIERRE";

	// constantes que definen el atributo PRAC_ID
	public static final int PRAC_DIESISEIS_DIECISIETE_VALUE = Integer.valueOf(2);// constante para especificar el período academico para registrar el histórico de los estudiantes
	public static final String PRAC_DIESISEIS_DIECISIETE_LABEL = "2016-2017";
	public static final int PRAC_HOMOLOGACION_VALUE = Integer.valueOf(63);// constante para especificar el período academico para registrar homologaciones
	public static final String PRAC_HOMOLOGACION_LABEL = "HOMOLOGACION";
	public static final int PRAC_PERIODO_MIGRACION_SUFICIENCIA_CULTURA_FISICA_VALUE = 210;
	public static final int PRAC_PERIODO_SUFICIENCIA_INFORMATICA_EXONERACIONES_VALUE = 450; // PRAC_ID Suficiencia iInformatica - Exoneraciones
	
	// Constantes que definen el atributo PRAC_TIPO, para especificar el período academico.
	public static final int PRAC_PREGRADO_VALUE = Integer.valueOf(0);
	public static final int PRAC_POSGRADO_VALUE = Integer.valueOf(1);
	public static final int PRAC_IDIOMAS_VALUE = Integer.valueOf(2);
	public static final int PRAC_SUFICIENCIA_CULTURA_FISICA_VALUE = 3;
	public static final int PRAC_SUFICIENCIA_INFORMATICA_VALUE = 5;//REGULARES
	public static final int PRAC_SUFICIENCIA_INFORMATICA_INTENSIVO_VALUE = 6;
	public static final int PRAC_SUFICIENCIA_INFORMATICA_EXONERACION_VALUE = 7;
	
	public static final String PRAC_PREGRADO_LABEL = "PREGRADO";
	public static final String PRAC_POSGRADO_LABEL = "POSGRADO";
	public static final String PRAC_IDIOMAS_LABEL = "IDIOMAS";
	public static final String PRAC_SUFICIENCIA_CULTURA_FISICA_LABEL = "SUFICIENCIA CULTURA FISICA";
	public static final String PRAC_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_LABEL = "INTENSIVO SUFICIENCIA CULTURA FISICA";
	
	
}
