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
   
 ARCHIVO:     MallaCurricularDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad MallaCurricular. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-08-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) MallaCurricularParaleloDto. DTO encargado de manejar los datos de
 * la entidad MallaCurricularParaleloDto.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class MallaCurricularParaleloDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	// Tabla MallaCurricularParalelo
	private int mlcrprId;
	private Integer mlcrprCupo;
	private Integer mlcrprParaleloId;
	private Integer mlcrprMallaCurricularMateriaId;
	private Integer mlcrprInscritos;
	private Integer mlcrprReservaRepetidos;
	private Integer mlcrprModalidad;

	// Tabla Materia
	private Integer mtrId;
	private Integer mtrHoras;
	private Integer mtrCreditos;
	private String mtrDescripcion;
	private Integer mtrSubId;
	private String mtrCodigo;

	// Tabla carrera
	private Integer crrId;

	public MallaCurricularParaleloDto() {
	}

	public MallaCurricularParaleloDto(int mlcrprId) {
		super();
		this.mlcrprId = mlcrprId;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(int mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public Integer getMlcrprCupo() {
		return mlcrprCupo;
	}

	public void setMlcrprCupo(Integer mlcrprCupo) {
		this.mlcrprCupo = mlcrprCupo;
	}

	public Integer getMlcrprParaleloId() {
		return mlcrprParaleloId;
	}

	public void setMlcrprParaleloId(Integer mlcrprParaleloId) {
		this.mlcrprParaleloId = mlcrprParaleloId;
	}

	public Integer getMlcrprMallaCurricularMateriaId() {
		return mlcrprMallaCurricularMateriaId;
	}

	public void setMlcrprMallaCurricularMateriaId(Integer mlcrprMallaCurricularMateriaId) {
		this.mlcrprMallaCurricularMateriaId = mlcrprMallaCurricularMateriaId;
	}

	public Integer getMlcrprInscritos() {
		return mlcrprInscritos;
	}

	public void setMlcrprInscritos(Integer mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public Integer getMtrId() {
		return mtrId;
	}

	public void setMtrId(Integer mtrId) {
		this.mtrId = mtrId;
	}

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public Integer getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(Integer mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public Integer getMtrSubId() {
		return mtrSubId;
	}

	public void setMtrSubId(Integer mtrSubId) {
		this.mtrSubId = mtrSubId;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public Integer getMlcrprReservaRepetidos() {
		return mlcrprReservaRepetidos;
	}

	public void setMlcrprReservaRepetidos(Integer mlcrprReservaRepetidos) {
		this.mlcrprReservaRepetidos = mlcrprReservaRepetidos;
	}

	public Integer getMlcrprModalidad() {
		return mlcrprModalidad;
	}

	public void setMlcrprModalidad(Integer mlcrprModalidad) {
		this.mlcrprModalidad = mlcrprModalidad;
	}

	@Override
	public String toString() {
		return "MallaCurricularParaleloDto [mlcrprId=" + mlcrprId + ", mlcrprCupo=" + mlcrprCupo + ", mlcrprParaleloId="
				+ mlcrprParaleloId + ", mlcrprMallaCurricularMateriaId=" + mlcrprMallaCurricularMateriaId
				+ ", mlcrprInscritos=" + mlcrprInscritos + "]";
	}

}
