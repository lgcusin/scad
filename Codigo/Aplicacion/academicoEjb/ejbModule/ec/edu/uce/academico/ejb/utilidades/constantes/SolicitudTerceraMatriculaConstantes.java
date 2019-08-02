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
   
 ARCHIVO:     SolicitudTerceraMatriculaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad SolicitudTerceraMatricula.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
31-ENERO-2018		 Marcelo Quishpe			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) SolicitudTerceraMatriculaConstantes.
 * Clase que maneja las constantes de la entidad SolicitudTerceraMatricula.
 * @author lmquishpei.
 * @version 1.0
 */
public class SolicitudTerceraMatriculaConstantes {
	
	
	//Constantes para identificar el estado de la solicitud de tercera matricula.
	public static final String ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_LABEL = "SIN SOLICITAR";
	public static final int ESTADO_SIN_SOLICITAR_TERCERA_MATRICULA_VALUE = Integer.valueOf(0);
	public static final String ESTADO_SOLICITADO_TERCERA_MATRICULA_LABEL = "SOLICITADA";
	public static final int ESTADO_SOLICITADO_TERCERA_MATRICULA_VALUE = Integer.valueOf(1);	
	public static final String ESTADO_VERIFICADA_TERCERA_MATRICULA_LABEL = "VERIFICADA";
	public static final int ESTADO_VERIFICADA_TERCERA_MATRICULA_VALUE = Integer.valueOf(2);	
	public static final String ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_LABEL = "SOLICITUD NEGADA";
	public static final int ESTADO_SOLICITUD_NEGADA_TERCERA_MATRICULA_VALUE = Integer.valueOf(3);	
	public static final String ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_LABEL = "SOLICITUD  APROBADA";
	public static final int ESTADO_SOLICITUD_APROBADA_TERCERA_MATRICULA_VALUE = Integer.valueOf(4);
	//Constantes para identificar el estado de la apelación de tercera matricula.
	public static final String ESTADO_APELACION_TERCERA_MATRICULA_LABEL = "EN APELACION";
	public static final int ESTADO_APELACION_TERCERA_MATRICULA_VALUE = Integer.valueOf(5);	
	public static final String ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_LABEL = "APELACION VERIFICADA";
	public static final int ESTADO_APELACION_VERIFICADA_TERCERA_MATRICULA_VALUE = Integer.valueOf(6);
	public static final String ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_LABEL = "APELACION NEGADA";
	public static final int ESTADO_APELACION_NEGADA_TERCERA_MATRICULA_VALUE = Integer.valueOf(7);
	public static final String ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_LABEL = "APELACION APROBADA";
	public static final int ESTADO_APELACION_APROBADA_TERCERA_MATRICULA_VALUE = Integer.valueOf(8);
	
	//Constantes para el tipo de solicitud, se define el tipo de tramite de tercera matricula y el label para generar el documento con una letra
	public static final String TIPO_SOLICITUD_TERCERA_MATRICULA_LABEL = "S";
	public static final int TIPO_SOLICITUD_TERCERA_MATRICULA_VALUE = Integer.valueOf(0);
	public static final String TIPO_APELACION_TERCERA_MATRICULA_LABEL = "A";
	public static final int TIPO_APELACION_TERCERA_MATRICULA_VALUE = Integer.valueOf(1);
	
	//Label para guardar el documento de resolucion
	public static final String RESOLUCION_APELACION_TERCERA_MATRICULA_LABEL = "RA";
	
	//Tipo Solicitud, para combos de seleccion
	public static final String TIPO_SOLICITUD_LABEL = "SOLICITUD";
	public static final int TIPO_SOLICITUD_VALUE = Integer.valueOf(0);
	public static final String TIPO_APELACION_LABEL = "APELACIÓN";
	public static final int TIPO_APELACION_VALUE = Integer.valueOf(1);
	//Estado registro
	public static final String ESTADO_REGISTRO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_REGISTRO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_REGISTRO_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_REGISTRO_INACTIVO_VALUE = Integer.valueOf(1);
	
	
}
