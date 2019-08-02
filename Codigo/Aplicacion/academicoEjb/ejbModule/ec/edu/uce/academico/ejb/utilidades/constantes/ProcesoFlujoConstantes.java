/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducciÃ³n o distribuciÃ³n no autorizada de este programa, 
 * o cualquier porciÃ³n de Ã©l, puede dar lugar a sanciones criminales y 
 * civiles severas, y serÃ¡n procesadas con el grado mÃ¡ximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ProcesoFlujoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ProcesoFlujo 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-03-2017          David Arellano                  EmisiÃ³n Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) ProcesoFlujoConstantes.
 * Clase que maneja las constantes de la entidad ProcesoFlujo.
 * @author darellano.
 * @version 1.0
 */
public class ProcesoFlujoConstantes {
	
	//constantes que indican SI EL ProcesoFlujo ESTÃ� ACTIVA O NO
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican la descripcion del proceso flujo
	public static final int PROCESO_FLUJO_PLANIFICACION_VALUE = Integer.valueOf(1);
	public static final String PROCESO_FLUJO_PLANIFICACION_LABEL = "PLANIFICACIÓN";
	public static final int PROCESO_FLUJO_MATRICULA_ORDINARIA_VALUE = Integer.valueOf(2);
	public static final String PROCESO_FLUJO_MATRICULA_ORDINARIA_LABEL = "MATRICULA ORDINARIA";
	public static final int PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_VALUE = Integer.valueOf(3);
	public static final String PROCESO_FLUJO_MATRICULA_EXTRAORDINARIA_LABEL = "MATRICULA EXTRAORDINARIA";
	
	public static final int PROCESO_FLUJO_REAJUSTE_VALUE = Integer.valueOf(4);
	public static final String PROCESO_FLUJO_REAJUSTE_LABEL = "REAJUSTE DE MATRÍCULA - AGREGACIÓN / ELIMINACIÓN";
	
	public static final int PROCESO_FLUJO_MATRICULA_ESPECIAL_VALUE = Integer.valueOf(5);
	public static final String PROCESO_FLUJO_MATRICULA_ESPECIAL_LABEL = "MATRICULA ESPECIAL";	
	public static final int PROCESO_INICIO_CLASES_VALUE = Integer.valueOf(6);
	public static final String PROCESO_INICIO_CLASES_LABEL = "INICIO CLASES";	
	public static final int PROCESO_NOTAS_PRIMER_PARCIAL_VALUE = Integer.valueOf(7);
	public static final String PROCESO_NOTAS_PRIMER_PARCIAL_LABEL = "NOTAS PRIMER HEMISEMESTRE";	
	public static final int PROCESO_NOTAS_SEGUNDO_PARCIAL_VALUE = Integer.valueOf(8);
	public static final String PROCESO_NOTAS_SEGUNDO_PARCIAL_LABEL = "NOTAS SEGUNDO HEMISEMESTRE";	
	public static final int PROCESO_NOTAS_RECUPERACION_VALUE = Integer.valueOf(9);
	public static final String PROCESO_NOTAS_RECUPERACION_LABEL = "NOTAS RECUPERACIÓN";
//	public static final int PROCESO_NOTAS_RECTIFICACION_VALUE = Integer.valueOf(10);
//	public static final String PROCESO_NOTAS_RECTIFICACION_LABEL = "NOTAS RECTIFICACIÓN";
	public static final int PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_VALUE = Integer.valueOf(10);
	public static final String PROCESO_INICIO_CLASES_SIGUIENTE_PERIODO_LABEL = "INICIO CLASES SIG. PERIODO ACADEMICO";
	
	public static final int PROCESO_REGISTRO_SOLICITUDES_TERCERA_MATRICULA_VALUE = Integer.valueOf(12);
	public static final String PROCESO_REGISTRO_SOLICITUDES_TERCERA_MATRICULA_LABEL ="REGISTRO DE SOLICITUDES TERCERA MATRÍCULA";
	public static final int PROCESO_RECEPCION_SOLICITUDES_TERCERA_MATRICULA_VALUE = Integer.valueOf(13);
	public static final String PROCESO_RECEPCION_SOLICITUDES_TERCERA_MATRICULA_LABEL ="RECEPCIÃ“N DE SOLICITUDES TERCERA MATRÍCULA";
	public static final int PROCESO_VALIDACION_SOLICITUDES_TERCERA_MATRICULA_VALUE = Integer.valueOf(14);
	public static final String PROCESO_VALIDACION_SOLICITUDES_TERCERA_MATRICULA_LABEL ="VALIDACIÃ“N DE SOLICITUDES TERCERA MATRÍCULA";
	
	public static final int PROCESO_REGISTRO_APELACIONES_TERCERA_MATRICULA_VALUE = Integer.valueOf(15);
	public static final String PROCESO_REGISTRO_APELACIONES_TERCERA_MATRICULA_LABEL ="REGISTRO DE APELACIONES TERCERA MATRÍCULA";
	public static final int PROCESO_RECEPCION_APELACIONES_TERCERA_MATRICULA_VALUE = Integer.valueOf(16);
	public static final String PROCESO_RECEPCION_APELACIONES_TERCERA_MATRICULA_LABEL ="RECEPCIÓN DE APELACIONES TERCERA MATRÍCULA";
	public static final int PROCESO_RESOLUCION_APELACIONES_TERCERA_MATRICULA_VALUE = Integer.valueOf(17);
	public static final String PROCESO_RESOLUCION_APELACIONES_TERCERA_MATRICULA_LABEL ="RESOLUCIÓN DE APELACIONES DE TERCERA MATRÍCULA";
	public static final int PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_VALUE = Integer.valueOf(18);
	public static final String PROCESO_NOTAS_PRIMER_HEMISEMESTRE_MEDICINA_LABEL = "NOTAS PRIMER HEMISEMESTRE MEDICINA";	
	public static final int PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_VALUE = Integer.valueOf(19);
	public static final String PROCESO_NOTAS_SEGUNDO_HEMISEMESTRE_MEDICINA_LABEL = "NOTAS SEGUNDO HEMISEMESTRE MEDICINA";
	public static final int PROCESO_RECUPERACION_MEDICINA_VALUE = Integer.valueOf(20);
	public static final String PROCESO_RECUPERACION_MEDICINA_LABEL = "NOTAS RECUPERACION MEDICINA";
	
	public static final int PROCESO_INGRESO_NOTAS_IDIOMAS_VALUE = Integer.valueOf(21);
	public static final String PROCESO_INGRESO_NOTAS_IDIOMAS_LABEL = "REGISTRO NOTAS SUFICIENCIA";
	
	public static final int PRFL_MATRICULAS_REGULARES_VALUE = 22;
	public static final String PRFL_MATRICULAS_REGULARES_LABEL = "MATRÍCULA ESTUDIANTES REGULARES - NIVEL Y PARALELO ÚNICO";
	
	public static final int PRFL_INFORMATICA_MATRICULA_MODALIDAD_REGULARES_VALUE = 23;
	public static final String PRFL_INFORMATICA_MATRICULA_MODALIDAD_REGULARES_LABEL = "MATRÍCULA MODALIDAD PRESENCIAL";
	
	public static final int PRFL_INFORMATICA_MATRICULA_MODALIDAD_INTENSIVOS_VALUE = 24;
	public static final String PRFL_INFORMATICA_MATRICULA_MODALIDAD_INTENSIVOS_LABEL = "MATRÍCULA MODALIDAD INTENSIVOS";
	
	public static final int PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_VALUE = 25;
	/**
	 * 
	 */
	public static final String PRFL_INFORMATICA_MATRICULA_MODALIDAD_EXONERACIONES_LABEL = "MATRÍCULA MODALIDAD EXONERACIONES";
	
	public static final int PRFL_INFORMATICA_CAMBIO_PARALELO_VALUE = 26;
	public static final String PRFL_INFORMATICA_CAMBIO_PARALELO_LABEL = "CAMBIO DE PARALELOS";
	
	public static final int PRFL_EVALUACION_APELACION_DIRECTIVO_VALUE = 27;
	public static final String PRFL_EVALUACION_APELACION_DIRECTIVO_LABEL = "EVALUACION AL DESEMPEÑO - APELACIÓN DIRECTIVO";
	
	//constante para definir el nÃºmero de dÃ­as en el que puede el estudiante retirarse de la matrÃ­cula pregrado
	public static final int HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE = Integer.valueOf(30);
//	public static final int HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_VALUE = Integer.valueOf(34);
	public static final String HABILITADO_RETIRO_MATRICULA_TREINTA_DIAS_LABEL = "RETIRO MATRÍCULA 30 DÍAS";
	
	
	
	public static final int PROCESO_NOTAS_PRIMER_PARCIAL_CIENCIAS_POLICIALES_VALUE = Integer.valueOf(29);
	public static final String PROCESO_NOTAS_PRIMER_PARCIAL_CIENCIAS_POLICIALES_LABEL = "NOTAS PRIMER HEMISEMESTRE CIENCIAS POLICIALES";	
	public static final int PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_VALUE = Integer.valueOf(30);
	public static final String PROCESO_NOTAS_SEGUNDO_PARCIAL_CIENCIAS_POLICIALES_LABEL = "NOTAS SEGUNDO HEMISEMESTRE CIENCIAS POLICIALES";	
	public static final int PROCESO_NOTAS_RECUPERACION_CIENCIAS_POLICIALES_VALUE = Integer.valueOf(31);
	public static final String PROCESO_NOTAS_RECUPERACION_CIENCIAS_POLICIALES_LABEL = "NOTAS RECUPERACIÓN CIENCIAS POLICIALES";
	
	
	//constante para definir el nÃºmero de dÃ­as en el que puede el estudiante retirarse de la matrÃ­cula suficiencia en informática
	public static final int HABILITADO_RETIRO_MATRICULA_SUF_INFORMATICA_DOS_DIAS_VALUE = Integer.valueOf(2);
	public static final String HABILITADO_RETIRO_MATRICULA_SUF_INFORMATICA_DOS_DIAS_LABEL = "RETIRO MATRÍCULA 48 HORAS";
}
