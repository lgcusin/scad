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

 ARCHIVO:     UbicacionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de un Ubicacion. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 15-MARZ-2017 			Dennis Collaguazo  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) UbicacionDto.
 * DTO encargado de manejar los datos para el registro de una Ubicacion.
 * @author dcollaguazo.
 * @version 1.0
 */
public class UbicacionDto implements Serializable {

	private static final long serialVersionUID = 1787779474422839352L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private int ubcId;
	private String ubcDescripcion;
	private Integer ubcJerarquia;
	private String ubcGentilicio;
	private String ubcCodSniese;
	private Integer ubcPadre;
	private String ubcPadreDescripcion;

	public UbicacionDto() {
	}

	public UbicacionDto(int ubcId) {
		this.ubcId = ubcId;
	}

	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getUbcId() {
		return ubcId;
	}
	public void setUbcId(int ubcId) {
		this.ubcId = ubcId;
	}
	public String getUbcDescripcion() {
		return ubcDescripcion;
	}
	public void setUbcDescripcion(String ubcDescripcion) {
		this.ubcDescripcion = ubcDescripcion;
	}
	public Integer getUbcJerarquia() {
		return ubcJerarquia;
	}
	public void setUbcJerarquia(Integer ubcJerarquia) {
		this.ubcJerarquia = ubcJerarquia;
	}
	public String getUbcGentilicio() {
		return ubcGentilicio;
	}
	public void setUbcGentilicio(String ubcGentilicio) {
		this.ubcGentilicio = ubcGentilicio;
	}
	public String getUbcCodSniese() {
		return ubcCodSniese;
	}
	public void setUbcCodSniese(String ubcCodSniese) {
		this.ubcCodSniese = ubcCodSniese;
	}
	public Integer getUbcPadre() {
		return ubcPadre;
	}
	public void setUbcPadre(Integer ubcPadre) {
		this.ubcPadre = ubcPadre;
	}
	public String getUbcPadreDescripcion() {
		return ubcPadreDescripcion;
	}
	public void setUbcPadreDescripcion(String ubcPadreDescripcion) {
		this.ubcPadreDescripcion = ubcPadreDescripcion;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("ubcId : " + ubcId);
		sb.append(tabulador);
		sb.append("ubcDescripcion : " + (ubcDescripcion==null? "NULL":ubcDescripcion));
		sb.append(tabulador);
		sb.append("ubcJerarquia : " + (ubcJerarquia==null? "NULL":ubcJerarquia.intValue()));
		sb.append(tabulador);
		sb.append("ubcGentilicio : " + (ubcGentilicio==null? "NULL":ubcGentilicio));
		sb.append(tabulador);
		sb.append("ubcCodSniese : " + (ubcCodSniese==null? "NULL":ubcCodSniese));
		sb.append(tabulador);
		sb.append("ubcPadre : " + (ubcPadre==null? "NULL":ubcPadre.intValue()));
		sb.append(tabulador);
		sb.append("ubcPadreDescripcion : " + (ubcPadreDescripcion==null? "NULL":ubcPadreDescripcion));
		return sb.toString();
	}
}
