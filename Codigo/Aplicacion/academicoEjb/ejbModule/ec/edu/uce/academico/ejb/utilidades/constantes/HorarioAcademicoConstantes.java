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
   
 ARCHIVO:     HorarioAcademicoConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad HorarioAcademico
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 22-SEPT-2017           Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) HorarioAcademicoConstantes.
 * Clase que maneja las constantes de la entidad HorarioAcademico.
 * @author dcollaguazo.
 * @version 1.0
 */
public class HorarioAcademicoConstantes {
	
	//constantes que indican el día del horario academico
	public static final int DIA_LUNES_VALUE = 0;
	public static final String DIA_LUNES_LABEL = "LUNES";
	public static final int DIA_MARTES_VALUE = 1;
	public static final String DIA_MARTES_LABEL = "MARTES";
	public static final int DIA_MIERCOLES_VALUE = 2;
	public static final String DIA_MIERCOLES_LABEL = "MIÉRCOLES";
	public static final int DIA_JUEVES_VALUE = 3;
	public static final String DIA_JUEVES_LABEL = "JUEVES";
	public static final int DIA_VIERNES_VALUE = 4;
	public static final String DIA_VIERNES_LABEL = "VIERNES";
	public static final int DIA_SABADO_VALUE = 5;
	public static final String DIA_SABADO_LABEL = "SÁBADO";
	public static final int DIA_DOMINGO_VALUE = 6;
	public static final String DIA_DOMINGO_LABEL = "DOMINGO";
	
	//constantes para el estado del horario academico
	public static final Integer ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final Integer ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	/**
	 * Metodo que busca el día del horario academico
	 * @param dia - dia id del día a traer
	 * @return - retorna el día que selecciono
	 */
	public static String traerDiaHorarioAcademico(int dia){
		String retorno = "";
		if(dia == HorarioAcademicoConstantes.DIA_LUNES_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_LUNES_LABEL;
		}
		if(dia == HorarioAcademicoConstantes.DIA_MARTES_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_MARTES_LABEL;
		}
		if(dia == HorarioAcademicoConstantes.DIA_MIERCOLES_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_MIERCOLES_LABEL;
		}
		if(dia == HorarioAcademicoConstantes.DIA_JUEVES_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_JUEVES_LABEL;
		}
		if(dia == HorarioAcademicoConstantes.DIA_VIERNES_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_VIERNES_LABEL;
		}
		if(dia == HorarioAcademicoConstantes.DIA_SABADO_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_SABADO_LABEL;
		}
		if(dia == HorarioAcademicoConstantes.DIA_DOMINGO_VALUE){
			retorno = HorarioAcademicoConstantes.DIA_DOMINGO_LABEL;
		}
		return retorno;
	}
	
}
