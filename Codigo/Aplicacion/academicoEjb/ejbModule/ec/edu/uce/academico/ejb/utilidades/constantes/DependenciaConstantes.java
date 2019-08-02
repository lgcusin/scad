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
   
 ARCHIVO:     DependenciaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Dependencia
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-ABRIL-2017           Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.utilidades.constantes;

/**
 * Clase (constantes) DependenciaConstantes.
 * Clase que maneja las constantes de la entidad Dependencia.
 * @author ghmafla.
 * @version 1.0
 */
public class DependenciaConstantes {
	
	//constantes que indican el estado de la dependencia
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
	
	//constantes que indican la jerarquia de la dependencia
	public static final int ESTADO_JERARQUIA_RECTORADO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_JERARQUIA_RECTORADO_LABEL = "RECTORADO";
	public static final int ESTADO_JERARQUIA_VICERRECTORADO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_JERARQUIA_VICERRECTORADO_LABEL = "VICERRECTORADO";
	public static final int ESTADO_JERARQUIA_FACULTADES_VALUE = Integer.valueOf(3);
	public static final String ESTADO_JERARQUIA_FACULTADES_LABEL = "FACULTAD";
	
	//constante que indica el id de la dependencia para nivelacion
	public static final int DEPENDENCIA_NIVELACION_VALUE = Integer.valueOf(27);
	public static final String DEPENDENCIA_NIVELACION_LABEL = "NIVELACIÓN";
	
	//constante que indica el id de la dependencia Idiomas.
	public static final int DEPENDENCIA_SUFICIENCIA_IDIOMAS_VALUE = 30;
	public static final String DEPENDENCIA_SUFICIENCIA_IDIOMAS_LABEL = "SUFICIENCIA - IDIOMAS";

	//constante que indica el id de la dependencia Cultura Fisica.
	public static final int DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_VALUE = 14;
	public static final String DEPENDENCIA_SUFICIENCIA_CULTURA_FISICA_LABEL = "SUFICIENCIA - CULTURA FÍSICA";
	
	//constante que indica el id de la dependencia FING.
	public static final int DEPENDENCIA_SUFICIENCIA_INFORMATICA_VALUE = 18;
	public static final String DEPENDENCIA_SUFICIENCIA_INFORMATICA_LABEL = "SUFICIENCIA - INFORMÁTICA";
	
	//constante que indica el id de las dependencias.
	public static final int DPN_CIENCIAS_MEDICAS_VALUE = 10;
	public static final int DPN_ODONTOLOGIA_VALUE = 21;
	public static final int DPN_CIENCIAS_ECONOMICAS_VALUE = 9;
	public static final int DPN_INGENIERIA_GEOLOGIA_MINAS_PETROLEOS_AMBIENTAL_VALUE = 10;
	public static final int DPN_CIENCIAS_ADMINISTRATIVAS_VALUE = 7;
	public static final int DPN_CIENCIAS_AGRICOLAS_VALUE = 25;
	public static final int DPN_FILOSOFIA_LETRAS_CIENCIAS_EDUCACION_VALUE = 15;
	public static final int DPN_CIENCIAS_DISCAPACIDAD_ATENCION_PREHOSPITALARIA_DESASTRES_VALUE = 25;
	public static final int DPN_MEDICINA_VETERINARIA_ZOOTECNIA_VALUE = 20;	
	public static final int DPN_INGENIERIA_QUIMICA_VALUE = 17;
	
	
}
