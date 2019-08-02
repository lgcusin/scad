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
   
 ARCHIVO:     FichaInscripcionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad FichaInscripcion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-MAR-2017           David Arellano                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) FichaInscripcionConstantes.
 * Clase que maneja las constantes de la entidad FichaInscripcion.
 * @author darellano.
 * @version 1.0
 */
public class FichaInscripcionConstantes {
	
	//constantes que indican si la ficha inscripcion esta activa o no
	public static final int ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ACTIVO_LABEL = "ACTIVO";
	public static final int INACTIVO_VALUE = Integer.valueOf(1);	
	public static final String INACTIVO_LABEL = "INACTIVO";
	public static final int FCIN_ESTADO_APROBADO_VALUE = 2;	
	public static final String FCIN_ESTADO_APROBADO_LABEL = "APROBADO";
	
	public static final int VIGENTE_VALUE = Integer.valueOf(0);
	public static final String VIGENTE_LABEL = "VIGENTE";
	public static final int NO_VIGENTE_VALUE = Integer.valueOf(1);	
	public static final String NO_VIGENTE_LABEL = "NO VIGENTE";
	
	//constantes que indican si la ficha inscripcion fue matriculada o no
	public static final int SI_MATRICULADO_VALUE = Integer.valueOf(0);
	public static final String SI_MATRICULADO_LABEL = "SI";
	public static final int NO_MATRICULADO_VALUE = Integer.valueOf(1);	
	public static final String NO_MATRICULADO_LABEL = "NO";
	public static final int MATRICULA_PENDIENTE_PASO_NOTAS_VALUE = Integer.valueOf(2);	
	public static final String MATRICULA_PENDIENTE_PASO_NOTAS_LABEL = "MATRICULA PENDIENTE PASO NOTAS";
	
	
	//constantes que indican si la ficha inscripcion realizo encuensta o no
	public static final int SI_ENCUESTA_LLENA_VALUE = Integer.valueOf(0);
	public static final String SI_ENCUESTA_LLENA_LABEL = "SI";
	public static final int NO_ENCUESTA_LLENA_VALUE = Integer.valueOf(1);	
	public static final String NO_ENCUESTA_LLENA_LABEL = "NO";
	
	//constantes que indican el tipo de inscripcion -> FCIN_TIPO
	public static final int TIPO_INSCRITO_NORMAL_VALUE = Integer.valueOf(0);
	public static final String TIPO_INSCRITO_NORMAL_LABEL = "INSCRITO NORMAL";
	public static final int TIPO_INSCRITO_MIGRADO_SAU_VALUE = Integer.valueOf(1);	
	public static final String TIPO_INSCRITO_MIGRADO_SAU_LABEL = "INSCRITO MIGRADO SAU";
	public static final int TIPO_INSCRITO_HOMOLOGACION_VALUE = Integer.valueOf(2);	
	public static final String TIPO_INSCRITO_HOMOLOGACION_LABEL = "PENDIENTE HOMOLOGACION";
	public static final int TIPO_INSCRITO_NIVELACION_VALUE = Integer.valueOf(3);	
	public static final String TIPO_INSCRITO_NIVELACION_LABEL = "INSCRITO NIVELACIÓN";
	public static final int TIPO_INSCRITO_POSGRADO_VALUE = Integer.valueOf(4);	
	public static final String TIPO_INSCRITO_POSGRADO_LABEL = "INSCRITO POSGRADO";
	public static final int TIPO_INSCRITO_PREGRADO_SNNA_GAR_VALUE = Integer.valueOf(5);	
	public static final String TIPO_INSCRITO_PREGRADO_SNNA_GAR_LABEL = "INSCRITO PREGRADO GAR";
	public static final int TIPO_INSCRITO_SUFICIENCIAS_VALUE = Integer.valueOf(6);	
	public static final String TIPO_INSCRITO_SUFICIENCIAS_LABEL = "INSCRITO SUFICIENCIAS";
	
	
	//constantes para el campo bloq_ter_matr  bloqueo por tercera matricula
	
	public static final int BLOQUEADO_TERCERA_MATRICULA_VALUE = Integer.valueOf(0);
	public static final String BLOQUEADO_TERCERA_MATRICULA_LABEL = "BLOQUEADO";
	public static final int DESBLOQUEADO_TERCERA_MATRICULA_VALUE = Integer.valueOf(1);	
	public static final String DESBLOQUEADO_TERCERA_MATRICULA_LABEL = "DESBLOQUEADO";
	
	
	/**
	 * Constantes de estado retiro
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public static final int ESTADO_RETIRO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_RETIRO_ACTIVO_LABEL = "RETIRADO";
	public static final int ESTADO_RETIRO_INACTIVO_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_RETIRO_INACTIVO_LABEL = "NO RETIRADO";
	public static final int ESTADO_RETIRO_EGRESADO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_RETIRO_EGRESADO_LABEL = "EGRESADO EN SISTEMAS PROPIOS DE LA FACULTAD"; // PARA IDIOMAS
	
	public static final int ESTADO_RETIRO_REINGRESO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_RETIRO_REINGRESO_LABEL = "REINGRESO";
	/*
	 * Constantes de tipo ingreso por homologacion
	 */
	public static final int TIPO_INGRESO_REINGRESO_VALUE = Integer.valueOf(0);
	public static final String TIPO_INGRESO_REINGRESO_LABEL = "REINGRESO";
	public static final int TIPO_INGRESO_CAMBIO_CARRERA_VALUE = Integer.valueOf(1);	
	public static final String TIPO_INGRESO_CAMBIO_CARRERA_LABEL = "CAMBIO DE CARRERA";
	public static final int TIPO_INGRESO_CAMBIO_UNIVERSIDAD_VALUE = Integer.valueOf(2);	
	public static final String TIPO_INGRESO_CAMBIO_UNIVERSIDAD_LABEL = "CAMBIO DE UNIVERSIDAD";
	public static final int TIPO_INGRESO_REINICIO_VALUE = Integer.valueOf(3);	
	public static final String TIPO_INGRESO_REINICIO_LABEL = "REINICIO";
	public static final int TIPO_INGRESO_SEGUNDA_CARRERA_VALUE = Integer.valueOf(4);	
	public static final String TIPO_INGRESO_SEGUNDA_CARRERA_LABEL = "SEGUNDA CARRERA";
	public static final int TIPO_INGRESO_CAMBIO_MALLA_VALUE = Integer.valueOf(5);	
	public static final String TIPO_INGRESO_CAMBIO_MALLA_LABEL = "CAMBIO DE MALLA";
	public static final int TIPO_INGRESO_REINGRESO_CON_REDISENO_VALUE = Integer.valueOf(6);	
	public static final String TIPO_INGRESO_REINGRESO_CON_REDISENO_LABEL = "CAMBIOS POR REDISEÑO";
	public static final int TIPO_INGRESO_INTERCAMBIO_VALUE = Integer.valueOf(7);	
	public static final String TIPO_INGRESO_INTERCAMBIO_LABEL = "INTERCAMBIO-REGISTRO";
	public static final int TIPO_INGRESO_UBICACION_IDIOMAS_VALUE = Integer.valueOf(8);	
    public static final String TIPO_INGRESO_UBICACION_IDIOMAS_LABEL = "UBICACION IDIOMAS-REGISTRO(MANUAL)";
	public static final int TIPO_INGRESO_POSGRADO_VALUE = Integer.valueOf(9);	
	public static final String TIPO_INGRESO_POSGRADO_LABEL = "INGRESO A POSGRADO-REGISTRO";
	public static final int TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_VALUE = Integer.valueOf(10);	
	public static final String TIPO_INGRESO_SUFICIENCIA_CULTURA_FISICA_LABEL = "INGRESO A SUFICIENCIA CULTURA FISICA-REGISTRO";
	public static final int TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_VALUE = Integer.valueOf(11);	
	public static final String TIPO_INGRESO_INTENSIVO_SUFICIENCIA_CULTURA_FISICA_LABEL = "INGRESO A INTENSIVO SUFICIENCIA CULTURA FISICA-REGISTRO";
	public static final int TIPO_INGRESO_DIRECTO_CARRERA_VALUE = Integer.valueOf(12);	
	public static final String TIPO_INGRESO_DIRECTO_CARRERA_LABEL = "INGRESO DIRECTO A CARRERA CON AUTORIZACIÓN";
	public static final int TIPO_INGRESO_IDIOMA_ADICIONAL_VALUE = 13;	
    public static final String TIPO_INGRESO_IDIOMA_ADICIONAL_LABEL = "IDIOMA ADICIONAL";
    public static final int TIPO_INGRESO_UBICACION_IDIOMAS_AUTOMATICO_VALUE = Integer.valueOf(14);	
	public static final String TIPO_INGRESO_UBICACION_IDIOMAS_AUTOMATICO_LABEL = "UBICACION IDIOMAS-PRUEBA(AUTOMATICO)";
	public static final int TIPO_INGRESO_PERIODO_CONTINGENCIA_IDIOMAS_VALUE = 15;	
	public static final String TIPO_INGRESO_PERIODO_CONTINGENCIA_IDIOMAS_LABEL = "PERIODOS CONTINGENCIA IDIOMAS - REGISTROS EN EXCEL";
	
	/*
	 * Constantes de estado de ingreso
	 */
	public static final int ESTADO_HOMOLOGADO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_HOMOLOGADO_LABEL = "HOMOLOGADO";
	public static final int ESTADO_NO_HOMOLOGADO_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_NO_HOMOLOGADO_LABEL = "NO HOMOLOGADO";
	
	
	/*
	 * Constantes de Observación
	 */
	public static final String OBS_NO_NUEVO_NIVELACION = "NO NUEVO - NIVELACION";
	public static final String OBS_NUEVO_CUPO_NIVELACION = "NUEVO CUPO - NIVELACION";
	
	/*
	 * Constantes Reinicio Origen - FCIN_REINICIO_ORIGEN -> ya no importa el origen sino el TIPO
	 */
	 public static final int TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_VALUE = Integer.valueOf(2);	// NO SE HOMOLOGA, ES IGUAL A REINGRESO
	 public static final String TIPO_REINICIO_ORIGEN_CON_RECORD_ANTERIOR_LABEL = "REINICIO CON RECORD ANTERIOR";
	 public static final int TIPO_REINICIO_ORIGEN_SIN_RECORD_ANTERIOR_VALUE = Integer.valueOf(1);	// SE TOMA EN CUENTA SOLO DESDE EL PRAC_ID DEL REINICIO
	 public static final String TIPO_REINICIO_ORIGEN_SIN_RECORD_ANTERIOR_LABEL = "REINICIO SIN RECORD ANTERIOR";
	 
		/*
		 * Constantes aplica_nota_enes
		 */
		 public static final int TIPO_SI_APLICA_NOTA_ENES_VALUE = Integer.valueOf(1);	
		 public static final String TIPO_SI_APLICA_NOTA_ENES_LABEL = "SI APLICA NOTA ENES";
		 public static final int TIPO_NO_APLICA_NOTA_ENES_VALUE = Integer.valueOf(2);	
		 public static final String TIPO_NO_APLICA_NOTA_ENES_LABEL = "NO APLICA NOTA ENES";
	 
}
