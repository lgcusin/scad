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
   
 ARCHIVO:     SAUConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de procedencia SAU.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-ENERO-2018		 Arturo Villafuerte 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) SAUConstantes.
 * Clase que maneja las constantes de la entidad SAU.
 * @author ajvillafuerte
 * @version 1.0
 */
public class SAUConstantes {
	
	//Constantes de estado
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO"; 

	//Constantes de periodo academico
	public static final int PERIODO_ACADEMICO_2017_2018_VALUE = Integer.valueOf(31);
	
	//Constante numero de matricula
	public static final int PRIMERA_MATRICULA_VALUE = Integer.valueOf(1);
	public static final int SEGUNDA_MATRICULA_VALUE = Integer.valueOf(2);
	public static final int TERCERA_MATRICULA_VALUE = Integer.valueOf(3);

	//Constante estado matricula
	public static final int MATERIA_APROBADA_VALUE = Integer.valueOf(3);
	public static final int MATERIA_REPROBADA_VALUE = Integer.valueOf(4);
	public static final int MATERIA_HOMOLOGADA_VALUE = Integer.valueOf(10);

	//Constantes record academico SAU
	public static final int RCES_MATERIA_ANULADO_VALUE = -1;
	public static final int RCES_MATERIA_DESCONOCIDO_VALUE = 0;
	public static final int RCES_MATERIA_INSCRITO_VALUE = 1;
	public static final int RCES_MATERIA_MATRICULDO_VALUE = 2;
	public static final int RCES_MATERIA_APROBADO_VALUE = 3;
	public static final int RCES_MATERIA_REPROBADO_VALUE = 4;
	public static final int RCES_MATERIA_NINGUNA_NOTA_VALUE = 5;
	public static final int RCES_MATERIA_SUSPENSO_VALUE = 6;
	public static final int RCES_MATERIA_PERDIDO_POR_ASISTENCIA_VALUE = 7;
	public static final int RCES_MATERIA_CONVALIDADO_VALUE = 10;

	public static final String RCES_MATERIA_ANULADO_LABEL = "ANULADO";
	public static final String RCES_MATERIA_DESCONOCIDO_LABEL = "DESCONOCIDO";
	public static final String RCES_MATERIA_INSCRITO_LABEL = "INSCRITO";
	public static final String RCES_MATERIA_MATRICULDO_LABEL = "MATRICULADO";
	public static final String RCES_MATERIA_APROBADO_LABEL = "APROBADO"; 
	public static final String RCES_MATERIA_REPROBADO_LABEL = "REPROBADO";
	public static final String RCES_MATERIA_NINGUNA_NOTA_LABEL = "NO REGISTRA NOTA";
	public static final String RCES_MATERIA_SUSPENSO_LABEL = "SUSPENSO";
	public static final String RCES_MATERIA_PERDIDO_POR_ASISTENCIA_LABEL = "PERDIDO POR ASISTENCIA";
	public static final String RCES_MATERIA_CONVALIDADO_LABEL = "CONVALIDADO";
	
	//Constantes Suficiencia en Idiomas
	public static final int INGLES_APROBACION_NIVELES = 128;
	public static final int INGLES_APROBACION_CERTIFICADO = 165;
	public static final int INGLES_APROBACION_IDIOMA_ADICIONAL = 2256829;
	public static final int INGLES_APROBACION_SUFICIENCIA = 3145351;
	public static final int INGLES_APROBACION_A1 = 3320352;
	public static final int INGLES_APROBACION_A2 = 3320353;
	public static final int INGLES_APROBACION_INTENSIVO1 = 3341633;
	public static final int INGLES_APROBACION_INTENSIVO2 = 3341657;
	public static final int INGLES_APROBACION_ONLINE = 3343431;
	
	public static final int FRANCES_APROBACION_SUFICIENCIA = 132;
	public static final int FRANCES_APROBACION_A1 = 3315583;
	public static final int FRANCES_APROBACION_A2 = 3320031;
	
	public static final int ITALIANO_APROBACION_SUFICIENCIA = 133;
	public static final int ITALIANO_APROBACION_A1 = 3315587;
	public static final int ITALIANO_APROBACION_A2 = 3320038;
	
	public static final int COREANO_APROBACION_SUFICIENCIA = 130;
	public static final int COREANO_APROBACION_A1 = 3320014;
	public static final int COREANO_APROBACION_A2 = 3320018;
	
	public static final int KICHWA_APROBACION_SUFICIENCIA1 = 134;
    public static final int KICHWA_APROBACION_SUFICIENCIA2 = 3644874; 
    
    public static final int CHINO_MANDARIN_APROBACION = 129;
    public static final int APROBADO_A1A2 = 3806332;
    public static final int APROBADO_A2 = 3806334;

	//Constantes Suficiencia en CulturaFisica
    public static final int AFR_DEFENSA_PERSONAL = 170;
    public static final int AFR_ACOND_FISICO= 2268190;
    public static final int AFR_FUTBOL = 2268191;
    public static final int AFR_AEREOBICOS = 2268192;
    public static final int AFR_GIMNASIA_PESAS = 2268193;
    public static final int AFR_BASQUETBALL = 2268194;
    public static final int AFR_VOLEYBALL = 2268195;
    public static final int AFR_TENIS = 2268196;
    public static final int AFR_BAILE = 2268198;
    public static final int AFR_ACTIVIDAD_FISICA_RECREATIVA = 2570291;
    public static final int AFR_BAILE_TROPICAL = 2268199;
    
	//Constante estado materia 
	public static final int MTR_ACTIVA_VALUE = 1;
	public static final int MTR_INACTIVA_VALUE = 0;
	public static final String MTR_ACTIVA_LABEL = "MATERIA ACTIVA"; 
	public static final String MTR_INACTIVA_LABEL = "MATERIA INACTIVA"; 


}
