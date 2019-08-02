package ec.edu.uce.academico.ejb.dtos;


import java.math.BigDecimal;

/**
 * Clase (Entity Bean) ArancelDto. Dto que almacena los datos necesarios
 * del Arancel
 * @author fgguzman.
 * @version 1.0
 */
public class ArancelDto implements java.io.Serializable {

	private static final long serialVersionUID = -6421158488030031838L;
	private int arnId;
	private String arnDescripcion;
	private Integer arnTipoMatricula;
	private Integer arnTipoNumMatricula;
	private BigDecimal arnValor;
	private Integer arnTipo;
	private Integer arnCodigoArancel;
	public int getArnId() {
		return arnId;
	}
	public void setArnId(int arnId) {
		this.arnId = arnId;
	}
	public String getArnDescripcion() {
		return arnDescripcion;
	}
	public void setArnDescripcion(String arnDescripcion) {
		this.arnDescripcion = arnDescripcion;
	}
	public Integer getArnTipoMatricula() {
		return arnTipoMatricula;
	}
	public void setArnTipoMatricula(Integer arnTipoMatricula) {
		this.arnTipoMatricula = arnTipoMatricula;
	}
	public Integer getArnTipoNumMatricula() {
		return arnTipoNumMatricula;
	}
	public void setArnTipoNumMatricula(Integer arnTipoNumMatricula) {
		this.arnTipoNumMatricula = arnTipoNumMatricula;
	}
	public BigDecimal getArnValor() {
		return arnValor;
	}
	public void setArnValor(BigDecimal arnValor) {
		this.arnValor = arnValor;
	}
	public Integer getArnTipo() {
		return arnTipo;
	}
	public void setArnTipo(Integer arnTipo) {
		this.arnTipo = arnTipo;
	}
	public Integer getArnCodigoArancel() {
		return arnCodigoArancel;
	}
	public void setArnCodigoArancel(Integer arnCodigoArancel) {
		this.arnCodigoArancel = arnCodigoArancel;
	}
	 
	

}
