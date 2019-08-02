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
   
 ARCHIVO:     ReporteTemplateForm.java	  
 DESCRIPCION: Bean de sesion que maneja las plantillas para los encabezados y pie de pagina de los reportes. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 28-07-2018			 Freddy Guzmán                          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jsf.managedBeans.reportes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PersonaDto;
import ec.edu.uce.academico.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.academico.jpa.entidades.publico.Persona;

/**
 * Clase (session bean) ReporteTemplateForm. Bean de sesion que maneja las
 * plantillas para los encabezados y pie de pagina de los reportes.
 * 
 * @author fgguzman.
 * @version 1.0
 */

public class ReporteTemplateForm implements Serializable {

	private static final long serialVersionUID = 903755549202583562L;
	public static final String PATH_GENERAL_REPORTE = "/academico/reportes/";
	public static final String PATH_GENERAL_IMG_PIE = "/academico/reportes/imagenes/plantillaPie.png";
	public static final String PATH_GENERAL_IMG_CABECERA = "/academico/reportes/imagenes/plantillaCabecera.png";

	public static final String GENERAL_NOMBRE_INSTITUCION = "UNIVERSIDAD \nCENTRAL \nDEL ECUADOR";
	public static String GENERAL_PIE_PAGINA = "Copyright Universidad Central del Ecuador " + LocalDate.now().getYear();
	public static final String GENERAL_DOC_AUTOGENERADO = "Documento generado en siiu.uce.edu.ec el ";

	
	public static String setearGenero(EstudianteJdbcDto persona, int tipo) {
		String retorno = " - ";

		switch (tipo) {
		case 0:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "Dr.:";
				}else {
					retorno = "Dra.:";
				}
			}
			break;
		case 1:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = " el ";
				}else {
					retorno = " la ";
				}
			}
			break;
		case 2:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "El suscrito SECRETARIO ABOGADO ";
				}else {
					retorno = "La suscrita SECRETARIA ABOGADA ";
				}
			}
			break;
		case 3:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "El Sr. ";
				}else {
					retorno = "La Srta. ";
				}
			}
			break;
		}

		return retorno;
	}
	
	public static String setearGenero(PersonaDto persona, int tipo) {
		String retorno = " - ";

		switch (tipo) {
		case 0:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "Dr.:";
				}else {
					retorno = "Dra.:";
				}
			}
			break;
		case 1:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "El suscrito SECRETARIO ABOGADO ";
				}else {
					retorno = "La suscrita SECRETARIA ABOGADA ";
				}
			}
			break;
		case 2:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = " SECRETARIO ABOGADO ";
				}else {
					retorno = " SECRETARIA ABOGADA ";
				}
			}
			break;
		case 3:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = " DECANO DE LA FACULTAD ";
				}else {
					retorno = " DECANA DE LA FACULTAD ";
				}
			}
			break;
		case 4:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = " DIRECTOR DE CARRERA ";
				}else {
					retorno = " DIRECTORA DE CARRERA ";
				}
			}
			break;
		case 5:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = " INSTRUCTOR ";
				}else {
					retorno = " INSTRUCTORA ";
				}
			}
			break;
		}
		return retorno;
	}
	
	public static String setearGenero(Persona persona, int tipo) {
		String retorno = " - ";

		switch (tipo) {
		case 0:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "Dr.:";
				}else {
					retorno = "Dra.:";
				}
			}
			break;
		case 1:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = " el ";
				}else {
					retorno = " la ";
				}
			}
			break;
		case 2:
			if (persona != null ) {
				if (persona.getPrsSexo().equals(PersonaConstantes.SEXO_HOMBRE_VALUE)) {
					retorno = "El suscrito SECRETARIO ABOGADO ";
				}else {
					retorno = "La suscrita SECRETARIA ABOGADA ";
				}
			}
			break;
		}

		return retorno;
	}
	
	/**
	 * Método que da formato de dos decimales a un BigDecimal.
	 * @param param - bigdecimal
	 * @param simbolo - adiciona simbolo.
	 * @return valor en formato string.
	 */
	public static String getStringToBigDecimal(BigDecimal param, int simbolo){
		
		if (param != null && param.intValue() != 0) {
			if (simbolo == 0) {
				return String.format("%.2f", param) + " %";	
			}else if(param.intValue() == -1 ){
				return "";
			}else if(simbolo == 1 ){
				return String.format("%.2f", param);
			}else if (simbolo == 2) {
				return String.valueOf(param.intValue());
			}
		}
		
		return "";
	}


	public static String getStringToBigDecimal(BigDecimal param1, BigDecimal param2, int simbolo){

		if ((param1 != null && param1.intValue() != 0) && (param2 != null && param2.intValue() != 0)) {
			if (simbolo == 0) {
				return String.valueOf(param1.intValue())+ "/" + String.valueOf(param2.intValue());
			}
		}

		return "";
	}

}
