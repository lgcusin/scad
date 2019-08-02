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
   
 ARCHIVO:     NivelDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Nivel. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-MAR-2017 		  Vinicio Rosales   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) NivelDto. DTO encargado de manejar los datos de la entidad Nivel.
 * 
 * @author jvrosales.
 * @version 1.0
 */
public class NivelDto implements Serializable {

	private static final long serialVersionUID = -3663263502299711591L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Nivel
	private int nvlId;
	private String nvlDescripcion;
	private Integer nvlEstado;
	private Integer nvlNumeral;

	public NivelDto() {
	}

	public NivelDto(int nvlId, String nvlDescripcion, Integer nvlEstado) {
		super();
		this.nvlId = nvlId;
		this.nvlDescripcion = nvlDescripcion;
		this.nvlEstado = nvlEstado;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getNvlId() {
		return nvlId;
	}

	public void setNvlId(int nvlId) {
		this.nvlId = nvlId;
	}

	public String getNvlDescripcion() {
		return nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

	public Integer getNvlEstado() {
		return nvlEstado;
	}

	public void setNvlEstado(Integer nvlEstado) {
		this.nvlEstado = nvlEstado;
	}

	public Integer getNvlNumeral() {
		return nvlNumeral;
	}

	public void setNvlNumeral(Integer nvlNumeral) {
		this.nvlNumeral = nvlNumeral;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	@Override
	public String toString() {
		return "NivelDto [nvlId=" + nvlId + ", nvlDescripcion=" + nvlDescripcion + ", nvlEstado=" + nvlEstado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nvlEstado == null) ? 0 : nvlEstado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NivelDto other = (NivelDto) obj;
		if (nvlEstado == null) {
			if (other.nvlEstado != null)
				return false;
		} else if (!nvlEstado.equals(other.nvlEstado))
			return false;
		return true;
	}

}
