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
   
 ARCHIVO:     CampoFormacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Carrera 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-08-2017		 	Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) CarreraConstantes.
 * Clase que maneja las constantes de la entidad Carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class CarreraConstantes {
	
	public static final int CRR_ESTADO_ACTIVO_VALUE = 0;
	public static final int CRR_ESTADO_INACTIVO_VALUE = 1;
	public static final String CRR_ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final String CRR_ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	public static final int TIPO_PREGRADO_VALUE = 0;
	public static final int TIPO_POSGRADO_VALUE = 1;
	public static final int TIPO_NIVELEACION_VALUE = 2;
	public static final int TIPO_SUFICIENCIA_VALUE = 3;
	
	public static final String TIPO_PREGRADO_LABEL = "PREGRADO";
	public static final String TIPO_POSGRADO_LABEL = "POSGRADO";
	public static final String TIPO_NIVELACION_LABEL = "NIVELACIÓN";
	public static final String TIPO_SUFICIENCIA_LABEL = "SUFICIENCIA";

	
	public static final int PROCESO_PREGRADO_REDISENO_VALUE = Integer.valueOf(0);
	public static final String PROCESO_PREGRADO_REDISENO_LABEL = "REDISEÑO";
	public static final int PROCESO_MIGRACION_SIAC_A_SIIU_VALUE = Integer.valueOf(2);
	public static final String PROCESO_MIGRACION_SIAC_A_SIIU_LABEL = "PROCESO MIGRACION SIAC A SIIU";//DISEÑO
	
	
	public static final int PROCESO_POSGRADO_REDISENO_VALUE = Integer.valueOf(1);
	public static final String PROCESO_POSGRADO_REDISENO_LABEL = "PROCESO REDISEÑO";
	
	public static final int EDUCACION_INICIAL_POR_NIVEL_VALUE = Integer.valueOf(3);
	public static final String EDUCACION_INICIAL_POR_NIVEL_VALUE_LABEL = "EDUCACION INICIAL";
	public static final int CARRERA_CULTURA_FISICA_VALUE = Integer.valueOf(9);
	public static final String CARRERA_CULTURA_FISICAL_LABEL = "CULTURA FISICA";
	public static final int CARRERA_CIENCIAS_POLICIALES_VALUE = Integer.valueOf(107);
	public static final String CARRERA_CIENCIAS_POLICIALES_LABEL = "CIENCIAS POLICIALES Y SEGURIDAD CIUDADANA";
	
	public static final int CARRERA_MEDICINA_VALUE = Integer.valueOf(82);
	public static final String CARRERA_MEDICINA_LABEL = "CARRERA MEDICINA REDISEÑO";
	
	public static final int CARRERA_MEDICINA_REDISENO_VALUE = Integer.valueOf(157);
	public static final String CARRERA_MEDICINA_REDISENO_LABEL = "CARRERA MEDICINA REDISEÑO";
	
	public static final int CARRERA_TERAPIA_OCUPACIONAL_REDISENO_VALUE = Integer.valueOf(103);
	public static final String CARRERA_TERAPIA_OCUPACIONAL_LABEL = "CARRERA TERAPIA OCUPACIONAL";
	public static final int CARRERA_ATENCION_PREHOSPITALARIA_REDISENO_VALUE = Integer.valueOf(37);
	public static final String CARRERA_ATENCION_PREHOSPITALARIA_REDISENO_LABEL = "CARRERA ATENCION PREHOSPITALARIA";
	public static final int CARRERA_ENFERMERIA_DISENO_VALUE = 57;
	public static final String CARRERA_ENFERMERIA_DISENO_LABEL = "CARRERA ENFERMERIA";
	public static final int CARRERA_ENFERMERIA_REDISENO_VALUE = 159;
	public static final String CARRERA_ENFERMERIA_REDISENO_LABEL = "CARRERA ENFERMERIA (R)";
	public static final int CARRERA_OBSTETRICIA_DISENO_VALUE = 84;
	public static final String CARRERA_OBSTETRICIA_DISENO_LABEL = "CARRERA OBSTETRICIA";
	public static final int CARRERA_OBSTETRICIA_REDISENO_VALUE = 158;
	public static final String CARRERA_OBSTETRICIA_REDISENO_LABEL = "CARRERA OBSTETRICIA (R)";
	public static final int CARRERA_TRABAJO_SOCIAL_VALUE = 104;
	public static final String CARRERA_TRABAJO_SOCIAL_LABEL = "TRABAJO SOCIAL";
	
	public static final int CARRERA_ADMINITRACION1_VALUE = Integer.valueOf(52);
	public static final int CARRERA_ADMINITRACION2_VALUE = Integer.valueOf(53);
	public static final int CARRERA_ADMINITRACION3_VALUE = Integer.valueOf(54);
	public static final int CARRERA_CONTABILIDAD_DISTANCIA_VALUE = Integer.valueOf(49);
	public static final int CARRERA_EMPRESAS_DISTANCIA_VALUE = Integer.valueOf(50);
	public static final int CARRERA_PUBLICA_DISTANCIA_VALUE = Integer.valueOf(51);
	public static final int CARRERA_LIC_CONTABILIDAD_DISTANCIA_VALUE = Integer.valueOf(52);
	public static final int CARRERA_LIC_EMPRESAS_DISTANCIA_VALUE = Integer.valueOf(53);
	public static final int CARRERA_LIC_PUBLICA_DISTANCIA_VALUE = Integer.valueOf(54);
	public static final int CARRERA_CONTABILIDAD_DISTANCIA_R__VALUE = Integer.valueOf(169);
	public static final int CARRERA_EMPRESAS_DISTANCIA_R_VALUE = Integer.valueOf(170);
	public static final int CARRERA_PUBLICA_DISTANCIA_R_VALUE = Integer.valueOf(171);
	
	public static final int CARRERA_AREA_1L_EDUCACION_EN_LINEA_VALUE = Integer.valueOf(225);
	public static final int CARRERA_AREA_8L_CIENCIAS_SOCIALES_Y_DERECHO_EN_LINEA_VALUE = Integer.valueOf(226);
	
	public static final int CARRERA_SUFICIENCIA_INGLES_VALUE = 185;
	public static final int CARRERA_SUFICIENCIA_FRANCES_VALUE = 186;
	public static final int CARRERA_SUFICIENCIA_ITALIANO_VALUE = 187;
	public static final int CARRERA_SUFICIENCIA_COREANO_VALUE = 188;
	public static final int CARRERA_SUFICIENCIA_KICHWA_VALUE = 189;
	public static final int CARRERA_SUFICIENCIA_CULTURA_FISICA_VALUE = 192;
	public static final int CARRERA_SUFICIENCIA_INFORMATICA_VALUE = 230;
	
	
	public static final int CRR_REAJUSTE_MATRICULA_ACTIVO_VALUE = 0;
	public static final int CRR_REAJUSTE_MATRICULA_INACTIVO_VALUE = 1;
	public static final String CRR_REAJUSTE_MATRICULA_ACTIVO_LABEL = "ACTIVO";
	public static final String CRR_REAJUSTE_MATRICULA_INACTIVO_LABEL = "INACTIVO";
	
	// Constantes que definen el espe codigo de las carreras de suficiencia en idiomas
	public static final int CRR_ESPE_CODIGO_SUFICIENCIA_INGLES= 30001;
	public static final int CRR_ESPE_CODIGO_SUFICIENCIA_FRANCES = 30002;
	public static final int CRR_ESPE_CODIGO_SUFICIENCIA_ITALIANO = 30003;
	public static final int CRR_ESPE_CODIGO_SUFICIENCIA_COREANO = 30004;
	public static final int CRR_ESPE_CODIGO_SUFICIENCIA_KICHWA = 30005;

	//	Constantes que definen el espe codigo de las carreras de suficiencia en cultura fisica
	public static final int CRR_ESPE_CODIGO_SUFICIENCIA_CULTURA_FISICA = 19003;

	public static final int CRR_TIPO_EVALUACION_SOBRE_10_VALUE = 0;
	public static final String CRR_TIPO_EVALUACION_SOBRE_10_LABEL = "Evaluación sobre 10 Ptos.";

	public static final int CRR_TIPO_EVALUACION_SOBRE_20_VALUE = 1;
	public static final String CRR_TIPO_EVALUACION_SOBRE_20_LABEL = "Evaluación sobre 20 Ptos.";
}
