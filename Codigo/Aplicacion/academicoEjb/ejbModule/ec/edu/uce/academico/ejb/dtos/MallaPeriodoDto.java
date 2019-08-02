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
   
 ARCHIVO:     MallaPeriodoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad MallaPeriodo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 31-08-2017 			Vinicio Rosales   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) MallaPeriodoDto. DTO encargado de manejar los datos de la entidad
 * MallaPeriodo.
 * 
 * @author jvrosales.
 * @version 1.0
 */
public class MallaPeriodoDto implements Serializable {

	private static final long serialVersionUID = 8717963973935127559L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	private int mlprId;
	private Integer mlprMlcrId;
	private Integer mlprPracId;
	private Integer mlprEstado;

	private PeriodoAcademicoDto mlprPeriodoAcademicoDto;
	private MallaCurricularDto mlprMallaCurricularDto;
	private CarreraDto mlprCarreraDto;

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	public int getMlprId() {
		return mlprId;
	}

	public void setMlprId(int mlprId) {
		this.mlprId = mlprId;
	}

	public Integer getMlprMlcrId() {
		return mlprMlcrId;
	}

	public void setMlprMlcrId(Integer mlprMlcrId) {
		this.mlprMlcrId = mlprMlcrId;
	}

	public Integer getMlprPracId() {
		return mlprPracId;
	}

	public void setMlprPracId(Integer mlprPracId) {
		this.mlprPracId = mlprPracId;
	}

	public Integer getMlprEstado() {
		return mlprEstado;
	}

	public void setMlprEstado(Integer mlprEstado) {
		this.mlprEstado = mlprEstado;
	}

	public PeriodoAcademicoDto getMlprPeriodoAcademicoDto() {
		return mlprPeriodoAcademicoDto;
	}

	public void setMlprPeriodoAcademicoDto(PeriodoAcademicoDto mlprPeriodoAcademicoDto) {
		this.mlprPeriodoAcademicoDto = mlprPeriodoAcademicoDto;
	}

	public MallaCurricularDto getMlprMallaCurricularDto() {
		return mlprMallaCurricularDto;
	}

	public void setMlprMallaCurricularDto(MallaCurricularDto mlprMallaCurricularDto) {
		this.mlprMallaCurricularDto = mlprMallaCurricularDto;
	}

	public CarreraDto getMlprCarreraDto() {
		return mlprCarreraDto;
	}

	public void setMlprCarreraDto(CarreraDto mlprCarreraDto) {
		this.mlprCarreraDto = mlprCarreraDto;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	@Override
	public String toString() {
		return "MallaPeriodoDto [mlprId=" + mlprId + ", mlprMlcrId=" + mlprMlcrId + ", mlprPracId=" + mlprPracId
				+ ", mlprEstado=" + mlprEstado + "]";
	}

}
