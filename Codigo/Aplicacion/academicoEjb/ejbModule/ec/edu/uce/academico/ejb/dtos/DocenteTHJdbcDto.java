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

 ARCHIVO:     DocenteTHJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la base de datos de talento humano, docente 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 28-05-2019 			Dennis Collaguazo  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) DocenteTHJdbcDto.
 * DTO encargado de manejar los datos de la base de datos de talento humano, docente
 * @author dcollaguazo.
 * @version 1.0
 */
public class DocenteTHJdbcDto implements Serializable{

	private static final long serialVersionUID = -21762531804963531L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	//Tabla Persona
	private int prsId;
	private String prsIdentificacion;
	
	//Tabla Puesto
	private int pstId;
	private String pstDenominacion;
	private int pstTipoPuesto;
	
	private int ctrnId;
	
	//Tabla Rango Gradual
	private int rngrId;
	private String rngrDescripcion;
	
	private int cttmId;

	//Tabla Categoria Docente
	private int ctdcId;
	private String ctdcDescripcion;
	
	//Tabla Tiempo de dedicación
	private int tmddId;
	private String tmddDescripcion;
	
	//Tabla Dependencia
	private int dpnId;
	private String dpnDescripcion;
	
	//Tabla Relación Laboral
	private int rllbId;
	private String rllbDescripcion;
	
	//Tabla Carrera 
	private int crrId;
	private String crrDescripcion;
	
	

	public DocenteTHJdbcDto() {
	}


	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getPrsId() {
		return prsId;
	}
	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}
	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}
	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}
	public int getPstId() {
		return pstId;
	}
	public void setPstId(int pstId) {
		this.pstId = pstId;
	}
	public String getPstDenominacion() {
		return pstDenominacion;
	}
	public void setPstDenominacion(String pstDenominacion) {
		this.pstDenominacion = pstDenominacion;
	}
	public int getCtrnId() {
		return ctrnId;
	}
	public void setCtrnId(int ctrnId) {
		this.ctrnId = ctrnId;
	}
	public int getRngrId() {
		return rngrId;
	}
	public void setRngrId(int rngrId) {
		this.rngrId = rngrId;
	}
	public String getRngrDescripcion() {
		return rngrDescripcion;
	}
	public void setRngrDescripcion(String rngrDescripcion) {
		this.rngrDescripcion = rngrDescripcion;
	}
	public int getCttmId() {
		return cttmId;
	}
	public void setCttmId(int cttmId) {
		this.cttmId = cttmId;
	}
	public int getCtdcId() {
		return ctdcId;
	}
	public void setCtdcId(int ctdcId) {
		this.ctdcId = ctdcId;
	}
	public String getCtdcDescripcion() {
		return ctdcDescripcion;
	}
	public void setCtdcDescripcion(String ctdcDescripcion) {
		this.ctdcDescripcion = ctdcDescripcion;
	}
	public int getTmddId() {
		return tmddId;
	}
	public void setTmddId(int tmddId) {
		this.tmddId = tmddId;
	}
	public String getTmddDescripcion() {
		return tmddDescripcion;
	}
	public void setTmddDescripcion(String tmddDescripcion) {
		this.tmddDescripcion = tmddDescripcion;
	}
	public int getDpnId() {
		return dpnId;
	}
	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}
	public String getDpnDescripcion() {
		return dpnDescripcion;
	}
	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}
	public int getRllbId() {
		return rllbId;
	}
	public void setRllbId(int rllbId) {
		this.rllbId = rllbId;
	}
	public String getRllbDescripcion() {
		return rllbDescripcion;
	}
	public void setRllbDescripcion(String rllbDescripcion) {
		this.rllbDescripcion = rllbDescripcion;
	}
	public int getPstTipoPuesto() {
		return pstTipoPuesto;
	}
	public void setPstTipoPuesto(int pstTipoPuesto) {
		this.pstTipoPuesto = pstTipoPuesto;
	}
	public int getCrrId() {
		return crrId;
	}
	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}
	public String getCrrDescripcion() {
		return crrDescripcion;
	}
	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}


	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsIdentificacion : " + (prsIdentificacion==null? "NULL":prsIdentificacion));
		sb.append(tabulador + "pstId : " + pstId);
		sb.append(tabulador + "pstDenominacion : " + pstDenominacion);
		sb.append(tabulador + "ctrnId : " + ctrnId);
		sb.append(tabulador + "rngrId : " + rngrId);
		sb.append(tabulador + "rngrDescripcion : " + rngrDescripcion);
		sb.append(tabulador + "cttmId : " + cttmId);
		sb.append(tabulador + "ctdcId : " + ctdcId);
		sb.append(tabulador + "ctdcDescripcion : " + ctdcDescripcion);
		sb.append(tabulador + "tmddId : " + tmddId);
		sb.append(tabulador + "tmddDescripcion : " + tmddDescripcion);
		sb.append(tabulador + "dpnId : " + dpnId);
		sb.append(tabulador + "dpnDescripcion : " + dpnDescripcion);
		sb.append(tabulador + "rllbId : " + rllbId);
		sb.append(tabulador + "rllbDescripcion : " + rllbDescripcion);
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + crrDescripcion);
		sb.append(tabulador + "pstTipoPuesto : " + pstTipoPuesto);
		return sb.toString();
	}

	


}
