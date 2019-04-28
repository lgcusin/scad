package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the PARAMETRO database table.
 * 
 */
@Entity
@Table(name = "PARAMETRO")
@NamedQuery(name = "Parametro.findAll", query = "SELECT p FROM Parametro p")
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PRM_ID")
	private long prmId;

	@Column(name = "FCL_ID")
	private Integer fclId;

	@Column(name = "PRM_NOMBRE")
	private String prmNombre;

	@Column(name = "PRM_VALOR")
	private String prmValor;

	public Parametro() {
	}

	public long getPrmId() {
		return this.prmId;
	}

	public void setPrmId(long prmId) {
		this.prmId = prmId;
	}

	public Integer getFclId() {
		return this.fclId;
	}

	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}

	public String getPrmNombre() {
		return this.prmNombre;
	}

	public void setPrmNombre(String prmNombre) {
		this.prmNombre = prmNombre;
	}

	public String getPrmValor() {
		return this.prmValor;
	}

	public void setPrmValor(String prmValor) {
		this.prmValor = prmValor;
	}

}