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
   
 ARCHIVO:    EdificioDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad EdificioDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-05-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) EdificioDto.
 * DTO encargado de manejar los datos de la entidad EdificioDto. 
 * @author lmquishpei.
 * @version 1.0
 */
public class EdificioDto implements Serializable{

	private static final long serialVersionUID = -3663263502299711591L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	//  Tabla Dependencia
	
	private int edfId;
	private String edfDescripcion;
	private Integer edfEstado;
	private String edfCodigo;
	private Integer edfLocalizacion;
	private String edfLocalizacionSt;
	
	
	//Tabla Dependencia
	private int dpnId;
	private String dpnDescripcion;
	
	
	//Tabla 
	
	public EdificioDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	

	public int getEdfId() {
		return edfId;
	}

	public void setEdfId(int edfId) {
		this.edfId = edfId;
	}

	public String getEdfDescripcion() {
		return edfDescripcion;
	}

	public void setEdfDescripcion(String edfDescripcion) {
		this.edfDescripcion = edfDescripcion;
	}

	public Integer getEdfEstado() {
		return edfEstado;
	}

	public void setEdfEstado(Integer edfEstado) {
		this.edfEstado = edfEstado;
	}

	public String getEdfCodigo() {
		return edfCodigo;
	}

	public void setEdfCodigo(String edfCodigo) {
		this.edfCodigo = edfCodigo;
	}

	public Integer getEdfLocalizacion() {
		return edfLocalizacion;
	}

	public void setEdfLocalizacion(Integer edfLocalizacion) {
		this.edfLocalizacion = edfLocalizacion;
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

	public String getEdfLocalizacionSt() {
		return edfLocalizacionSt;
	}

	public void setEdfLocalizacionSt(String edfLocalizacionSt) {
		this.edfLocalizacionSt = edfLocalizacionSt;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		
		sb.append(" Edificio " );
		sb.append(tabulador + "edfId : " + edfId);
		sb.append(tabulador + "edfDescripcion : " + (edfDescripcion==null? "NULL":edfDescripcion));
		sb.append(tabulador + "edfEstado : " + (edfEstado==null? "NULL":edfEstado));
		sb.append(tabulador + "edfCodigo : " + (edfCodigo==null? "NULL":edfCodigo));
		sb.append(tabulador + "edfLocalizacion : " + (edfLocalizacion==null? "NULL":edfLocalizacion));
		sb.append(tabulador + "edfLocalizacionSt : " + (edfLocalizacionSt==null? "NULL":edfLocalizacionSt));
		
		sb.append(" Dependencia " );
		sb.append(tabulador + "dpnId : " + dpnId);
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion==null? "NULL":dpnDescripcion));
		
		
		
    
		return sb.toString();
    }

	
	
	
	
}
