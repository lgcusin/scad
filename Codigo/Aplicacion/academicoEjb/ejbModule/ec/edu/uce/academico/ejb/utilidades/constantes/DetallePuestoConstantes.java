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
   
 ARCHIVO:     DetallePuestoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad DetallePuesto
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-ABRIL-2017           Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) DependenciaConstantes.
 * Clase que maneja las constantes de la entidad DetallePuesto.
 * @author dcollaguazo.
 * @version 1.0
 */
public class DetallePuestoConstantes {
	
	//constantes que indican el estado del docente en el detalle puesto
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican el tipo proceso registro
	public static final int TIPO_PROCESO_REGISTRO_HORARIO_VALUE = Integer.valueOf(1);
	public static final String TIPO_PROCESO_REGISTRO_HORARIO_LABEL = "HORARIO";
	public static final int TIPO_PROCESO_REGISTRO_ADMINISTRACION_VALUE = Integer.valueOf(2);
	public static final String TIPO_PROCESO_REGISTRO_ADMINISTRACION_LABEL = "ADMINISTRACIÓN";
	
}
