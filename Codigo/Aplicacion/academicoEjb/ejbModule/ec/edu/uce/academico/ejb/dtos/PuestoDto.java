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
   
 ARCHIVO:     PuestoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Puesto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 25-JULIO-2019		 	 FREDDY GUZMÁN						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) PuestoDto. DTO encargado de manejar los datos de la entidad
 * Puesto.
 * 
 * @author fgguzman.
 * @version 1.0
 */
public class PuestoDto implements Serializable {

	private static final long serialVersionUID = -4241964904702040703L;
	private int pstId;
	private String pstDenominacion;
	private Integer pstNivelRangoGradual;
	private Integer pstCategoriaValue;
	private String pstCategoriaLabel;
	private Integer pstTiempoDedicacion;
	private Integer pstEstado;

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

	public Integer getPstNivelRangoGradual() {
		return pstNivelRangoGradual;
	}

	public void setPstNivelRangoGradual(Integer pstNivelRangoGradual) {
		this.pstNivelRangoGradual = pstNivelRangoGradual;
	}

	public Integer getPstCategoriaValue() {
		return pstCategoriaValue;
	}

	public void setPstCategoriaValue(Integer pstCategoriaValue) {
		this.pstCategoriaValue = pstCategoriaValue;
	}

	public String getPstCategoriaLabel() {
		return pstCategoriaLabel;
	}

	public void setPstCategoriaLabel(String pstCategoriaLabel) {
		this.pstCategoriaLabel = pstCategoriaLabel;
	}

	public Integer getPstTiempoDedicacion() {
		return pstTiempoDedicacion;
	}

	public void setPstTiempoDedicacion(Integer pstTiempoDedicacion) {
		this.pstTiempoDedicacion = pstTiempoDedicacion;
	}

	public Integer getPstEstado() {
		return pstEstado;
	}

	public void setPstEstado(Integer pstEstado) {
		this.pstEstado = pstEstado;
	}

}
