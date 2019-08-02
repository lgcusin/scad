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
   
 ARCHIVO:     RecordEstudianteConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad RecordEstudiante 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-03-2017		David Arellano			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) RecordEstudianteConstantes. Clase que maneja las
 * constantes de la entidad RecordEstudiante.
 * 
 * @author darellano.
 * @version 1.0
 */
public class RecordEstudianteConstantes {

	// constantes para el estado del record -> RCES_ESTADO - INICIALES
	public static final Integer ESTADO_INSCRITO_VALUE = Integer.valueOf(-1);
	public static final String ESTADO_INSCRITO_LABEL = "INSCRITO";
	public static final Integer ESTADO_MATRICULADO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_MATRICULADO_LABEL = "MATRICULADO";
	public static final Integer ESTADO_HOMOLOGADO_VALUE = Integer.valueOf(8);
	public static final String ESTADO_HOMOLOGADO_LABEL = "HOMOLOGADO";

	// constantes para el estado del record -> RCES_ESTADO - FINALES
	public static final Integer ESTADO_APROBADO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_APROBADO_LABEL = "APROBADO";
	public static final Integer ESTADO_REPROBADO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_REPROBADO_LABEL = "REPROBADO";
	public static final Integer ESTADO_RETIRADO_VALUE = Integer.valueOf(3);
	public static final String ESTADO_RETIRADO_LABEL = "RETIRO APROBADO";
	public static final Integer ESTADO_ANULACION_MATRICULA_VALUE = Integer.valueOf(10);
	public static final String ESTADO_ANULACION_MATRICULA_LABEL = "ANULACIÓN POR SANCIÓN";
	public static final Integer ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_VALUE = Integer.valueOf(13);
	public static final String ESTADO_ANULACION_MATRICULA_PROBLEMAS_ADMINISTRATIVOS_LABEL = "ANULACIÓN POR PROBLEMAS ADMINISTRATIVOS";

	// constantes para el estado del record -> RCES_ESTADO - NOTAS
	public static final Integer ESTADO_RECUPERACION_VALUE = Integer.valueOf(7);
	public static final String ESTADO_RECUPERACION_LABEL = "RECUPERACION";

	// constantes para el INGRESO NOTA
	public static final Integer ESTADO_NOTA_NO_INGRESADA_VALUE = Integer.valueOf(0);
	public static final String ESTADO_NOTA_NO_INGRESADA_LABEL = "NOTA NO INGRESADA";
	public static final Integer ESTADO_NOTA_GUARDADO_TEMPORAL_VALUE = Integer.valueOf(1);
	public static final String ESTADO_NOTA_GUARDADO_TEMPORAL_LABEL = "NOTA GUARDADO TEMPORAL";
	public static final Integer ESTADO_NOTA_GUARDADO_FINAL_VALUE = Integer.valueOf(2);
	public static final String ESTADO_NOTA_GUARDADO_FINAL_LABEL = "NOTA GUARDADO FINAL";

	// constantes de tipo de nota
	public static final Integer TIPO_NOTA_PRIMER_PARCIAL_VALUE = Integer.valueOf(0);
	public static final String TIPO_NOTA_PRIMER_PARCIAL_LABEL = "PRIMER HEMISEMESTRE";
	public static final Integer TIPO_NOTA_SEGUNDO_PARCIAL_VALUE = Integer.valueOf(1);
	public static final String TIPO_NOTA_SEGUNDO_PARCIAL_LABEL = "SEGUNDO HEMISEMESTRE";
	public static final Integer TIPO_NOTA_RECUPERACION_VALUE = Integer.valueOf(2);
	public static final String TIPO_NOTA_RECUPERACION_LABEL = "RECUPERACIÓN";

	// Constantes del campo modo de aprobación
	public static final Integer MODO_APROBACION_NORMAL_VALUE = Integer.valueOf(0);
	public static final String MODO_APROBACION_NORMAL_LABEL = "NORMAL";
	public static final Integer MODO_APROBACION_RECUPERACION_VALUE = Integer.valueOf(1);
	public static final String MODO_APROBACION_RECUPERACION_LABEL = "RECUPERACIÓN";
	public static final Integer MODO_APROBACION_RECONOCIMIENTO_VALUE = Integer.valueOf(2);
	public static final String MODO_APROBACION_RECONOCIMIENTO_LABEL = "RECONOCIMIENTO";
	public static final Integer MODO_APROBACION_OTRA_COHORTE_VALUE = Integer.valueOf(3);
	public static final String MODO_APROBACION_OTRA_COHORTE_LABEL = "OTRA COHORTE";
	// Modo de aprobacion para prueba de ubicacion idiomas

	public static final Integer MODO_APROBACION_PRUEBA_UBICACION_VALUE = Integer.valueOf(4);
	public static final String MODO_APROBACION_PRUEBA_UBICACION_LABEL = "PRUEBA DE UBICACIÓN";

	// constantes que definen el origen del record academico
	public static final int RCES_ORIGEN_SAU = 0;
	public static final int RCES_ORIGEN_SIIU = 1;

	// QUITAR - TRATAR DE QUITAR DE LOS METODOS , ESTADOS TRANSITORIOS SOLO EN DTMT
	public static final Integer ESTADO_MIGRADO_SAU_VALUE = Integer.valueOf(4);
	public static final String ESTADO_MIGRADO_SAU_LABEL = "MIGRADO SAU";
	public static final Integer ESTADO_CONVALIDADO_VALUE = Integer.valueOf(5);
	public static final String ESTADO_CONVALIDADO_LABEL = "ESTADO CONVALIDADO";
	public static final Integer ESTADO_RETIRADO_SOLICITADO_VALUE = Integer.valueOf(6);
	public static final String ESTADO_RETIRADO_SOLICITADO_LABEL = "RETIRO SOLICITADO";
	public static final Integer ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_VALUE = Integer.valueOf(9);
	public static final String ESTADO_RETIRO_AUTORIZAR_CONSEJO_DIRECTIVO_LABEL = "RETIRO SOLICITADO A CONSEJO DIRECTIVO";
	
	public static final Integer ESTADO_RETIRADO_FORTUITAS_SOLICITADO_VALUE = Integer.valueOf(11);
	public static final String ESTADO_RETIRADO_FORTUITAS_SOLICITADO_LABEL = "RETIRO SOLICITADO POR SITUACIONES FORTUITAS O FUERZA MAYOR";
	public static final Integer ESTADO_RETIRADO_FORTUITAS_VALUE = Integer.valueOf(12);
	public static final String ESTADO_RETIRADO_FORTUITAS_LABEL = "RETIRO POR SITUACIONES FORTUITAS O FUERZA MAYOR";

}
