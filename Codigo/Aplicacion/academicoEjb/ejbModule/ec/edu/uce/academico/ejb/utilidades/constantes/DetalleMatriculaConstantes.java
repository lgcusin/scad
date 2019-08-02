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
   
 ARCHIVO:     DetalleMatriculaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad DetalleMatricula 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-03-2017          David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) DetalleMatriculaConstantes.
 * Clase que maneja las constantes de la entidad DetalleMatricula.
 * @author darellano.
 * @version 1.0
 */
public class DetalleMatriculaConstantes {
	
	//constantes que indican SI EL DETALLE MATRICULA ESTÁ ACTIVA O NO -> DTMT_ESTADO
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes para el campo de numero de matricula -> DTMT_NUMERO
	public static final int PRIMERA_MATRICULA_VALUE = Integer.valueOf(1);
	public static final String PRIMERA_MATRICULA_LABEL = "1";
	public static final int SEGUNDA_MATRICULA_VALUE = Integer.valueOf(2);
	public static final String SEGUNDA_MATRICULA_LABEL = "2";
	public static final int TERCERA_MATRICULA_VALUE = Integer.valueOf(3);
	public static final String TERCERA_MATRICULA_LABEL = "3";
	public static final int REPROBO_TERCERA_MATRICULA_VALUE = Integer.valueOf(4);
	public static final String REPROBO_TERCERA_MATRICULA_LABEL = "4";
	
	
	//constantes para el estados del detalle matricula -> DTMT_ESTADO_SOLICITUD
	public static final Integer DTMT_ANULACION_MATRICULA_SOLICITADA_VALUE = 10;
	public static final String DTMT_ANULACION_MATRICULA_SOLICITADA_LABEL = "ANULACIÓN MATRÍCULA SOLICITADA";
	public static final int ESTADO_CAMBIO_RETIRO_SOLICITADO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_CAMBIO_RETIRO_SOLICITADO_LABEL = "RETIRO SOLICITADO";
	public static final int ESTADO_CAMBIO_RETIRO_AUTORIZAR_DIRECTOR_VALUE = Integer.valueOf(2);
	public static final String ESTADO_CAMBIO_RETIRO_AUTORIZAR_DIRECTOR_LABEL = "RETIRO SOLICITADO A DIRECCIÓN DE CARRERA";
	public static final int ESTADO_CAMBIO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_VALUE = Integer.valueOf(3);
	public static final String ESTADO_CAMBIO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_LABEL = "RETIRO SOLICITADO A CONSEJO DIRECTIVO";
	
	public static final Integer ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_VALUE = Integer.valueOf(8);
	public static final String ESTADO_CAMBIO_RETIRO_FORTUITAS_SOLICITADO_LABEL = "RETIRO SOLICITADO POR SITUACIONES FORTUITAS";
	public static final Integer ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_VALUE = Integer.valueOf(9);
	public static final String ESTADO_CAMBIO_RETIRO_FORTUITAS_VERIFICADO_LABEL = "RETIRO POR SITUACIONES FORTUITAS VERIFICADO";
	
	//constantes para el estados del detalle matricula por materia -> DTMT_ESTADO_RESPUESTA
	public static final Integer DTMT_ESTADO_RESPUESTA_ANULACION_MATRICULA_ACEPTADA_VALUE = Integer.valueOf(4);
	public static final String DTMT_ESTADO_RESPUESTA_ANULACION_MATRICULA_ACEPTADA_LABEL = "ANULACIÓN MATRÍCULA ACEPTADA";
	public static final Integer DTMT_ESTADO_RESPUESTA_ANULACION_MATRICULA_NEGADA_VALUE = Integer.valueOf(12);
	public static final String DTMT_ESTADO_RESPUESTA_ANULACION_MATRICULA_NEGADA_LABEL = "ANULACIÓN MATRÍCULA NEGADA";
	
	public static final int DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_VALUE = Integer.valueOf(1);
	public static final String DTMT_ESTADO_RESPUESTA_RETIRO_APROBADO_LABEL = "RETIRO APROBADO";
	public static final int DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_VALUE = Integer.valueOf(5);
	public static final String DTMT_ESTADO_RESPUESTA_RETIRO_NEGADO_LABEL = "RETIRO NEGADO";
	
	//Constantes para generar el nombre del archivo  de evidencia por retiro de asignatura por situaciones  fortuitas.
	public static final String DTMT_ARCHIVO_SOLICITUD_RETIRO_FORTUITO_LABEL = "SRF";
	public static final String DTMT_ARCHIVO_RESOLUCION_RETIRO_FORTUITO_LABEL = "RRF";
	
	//Constante para generar el nombre del archivo de Resolución de anulación de matricula
	public static final String DTMT_ARCHIVO_RESOLUCION_ANULACION_LABEL = "RAM";  
	
	// no QUITAR(estados para el dtmt_estado_cambio)
	public static final int ESTADO_CAMBIO_RETIRO_APROBADO_CONSEJO_DIRECTIVO_VALUE = Integer.valueOf(6);
	public static final String ESTADO_CAMBIO_RETIRO_APROBADO_CONSEJO_DIRECTIVO_LABEL = "RETIRO APROBADO POR CONSEJO DIRECTIVO";
	public static final int ESTADO_CAMBIO_RETIRO_NEGADO_CONSEJO_DIRECTIVO_VALUE = Integer.valueOf(7);
	public static final String ESTADO_CAMBIO_RETIRO_NEGADO_CONSEJO_DIRECTIVO_LABEL = "RETIRO NEGADO POR CONSEJO DIRECTIVO";
	
	//Constantes para los estados de detalle_matricula --> DTMT_TIPO_ANULACION
		public static final int DTMT_TIPO_ANULACION_SANCION_VALUE = Integer.valueOf(1);
		public static final String DTMT_TIPO_ANULACION_SANCION_LABEL = "ANULACIÓN POR SANCIÓN";//con sancion
		public static final int DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_VALUE = Integer.valueOf(2);
		public static final String DTMT_TIPO_ANULACION_PROBLEMA_ADMINISTRATIVO_LABEL = "ANULACIÓN POR PROBLEMA ADMINISTRATIVO";	 //sin sancion
	
	
	//constantes para el campo de estado cambio historico
	public static final int ESTADO_HISTORICO_RETIRO_SOLICITADO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_HISTORICO_RETIRO_SOLICITADO_LABEL = "HISTORICO MATRICULA RETIRO SOLICITADO";
	
	//Tipos de retiro volutario, retiro fortuito o anulacion
	
	public static final int TIPO_RETIRO_VOLUNTARIO_VALUE = Integer.valueOf(1);
	public static final String TIPO_RETIRO_VOLUNTARIO_LABEL = "RETIRO VOLUNTARIO";
	public static final int TIPO_RETIRO_FORTUITO_VALUE = Integer.valueOf(2);
	public static final String TIPO_RETIRO_FORTUITO_LABEL = "RETIRO POR SITUACIONES FORTUITAS";
	public static final int TIPO_ANULACION_VALUE = Integer.valueOf(3);
	public static final String TTIPO_ANULACION_LABEL = "ANULACIÓN";
	
}
