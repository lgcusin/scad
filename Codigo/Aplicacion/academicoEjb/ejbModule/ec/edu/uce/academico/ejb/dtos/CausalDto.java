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
   
 ARCHIVO:     CausalDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad CausalDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-01-2019 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) CausalDto. DTO encargado de manejar los datos de la entidad CausalDto.
 * 
 * @author lmquishpei.
 * @version 1.0
 */
public class CausalDto implements Serializable {

	private static final long serialVersionUID = -3663263502299711591L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Causal
	private int cslId;
	private String cslDescripcion;
	private String cslCodigo;
	private Integer cslEstado;
	
	//Tabla TipoCausal
	private int ticsId;
	private String ticsDescripcion;
	private Integer ticsEstado;
	//item seleccionado
     Boolean seleccionado;
 
	public CausalDto() {

	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getCslId() {
		return cslId;
	}

	public void setCslId(int cslId) {
		this.cslId = cslId;
	}

	
	public String getCslDescripcion() {
		return cslDescripcion;
	}

	public void setCslDescripcion(String cslDescripcion) {
		this.cslDescripcion = cslDescripcion;
	}

	public String getCslCodigo() {
		return cslCodigo;
	}

	public void setCslCodigo(String cslCodigo) {
		this.cslCodigo = cslCodigo;
	}

	public Integer getCslEstado() {
		return cslEstado;
	}

	public void setCslEstado(Integer cslEstado) {
		this.cslEstado = cslEstado;
	}

	

	public int getTicsId() {
		return ticsId;
	}

	public void setTicsId(int ticsId) {
		this.ticsId = ticsId;
	}

	public String getTicsDescripcion() {
		return ticsDescripcion;
	}

	public void setTicsDescripcion(String ticsDescripcion) {
		this.ticsDescripcion = ticsDescripcion;
	}

	public Integer getTicsEstado() {
		return ticsEstado;
	}

	public void setTicsEstado(Integer ticsEstado) {
		this.ticsEstado = ticsEstado;
	}

	
	public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
	
		sb.append(" Causal ");
		sb.append(tabulador + "cslId : " + cslId);
		sb.append(tabulador + "cslDescripcion : " + (cslDescripcion == null ? "NULL" : cslDescripcion));
		sb.append(tabulador + "cslCodigo : " + (cslCodigo == null ? "NULL" : cslCodigo));
		sb.append(tabulador + "cslEstado : " + (cslEstado == null ? "NULL" : cslEstado));
	
		sb.append(" Tipo_Causal ");
		sb.append(tabulador + "ticsId : " + ticsId);
		sb.append(tabulador + "ticsDescripcion : " + (ticsDescripcion == null ? "NULL" : ticsDescripcion));
		sb.append(tabulador + "ticsEstado : " + (ticsEstado == null ? "NULL" : ticsEstado));

		sb.append("EstaSeleccionado");
		sb.append(tabulador + "seleccionado : " + seleccionado);
		
		return sb.toString();
	}

	
}
