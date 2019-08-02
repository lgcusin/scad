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
   
 ARCHIVO:     MallaCurricularMateriaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad MallaCurricularMateria
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-AGOS-2017           Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) MallaCurricularMateriaConstantes.
 * Clase que maneja las constantes de la entidad MallaCurricularMateria.
 * @author dcollaguazo.
 * @version 1.0
 */
public class MallaCurricularMateriaConstantes {
	
	//constantes que indican el estado de la malla curricular materia
	public static final int ESTADO_MALLA_MATERIA_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_MALLA_MATERIA_ACTIVO_LABEL = "ACTIVA";
	public static final int ESTADO_MALLA_MATERIA_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_MALLA_MATERIA_INACTIVO_LABEL = "INACTIVA";
	
}
