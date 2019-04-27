package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PRM_ID")
	private long prmId;

	@Column(name = "FCT_ID")
	private Integer fctId;

	@Column(name = "PRM_NOMBRE")
	private String prmNombre;

	@Column(name = "PRM_VALOR")
	private Integer prmValor;

	public Parametro() {
	}

	public long getPrmId() {
		return this.prmId;
	}

	public void setPrmId(long prmId) {
		this.prmId = prmId;
	}

	public Integer getFctId() {
		return this.fctId;
	}

	public void setFctId(Integer fctId) {
		this.fctId = fctId;
	}

	public String getPrmNombre() {
		return this.prmNombre;
	}

	public void setPrmNombre(String prmNombre) {
		this.prmNombre = prmNombre;
	}

	public Integer getPrmValor() {
		return this.prmValor;
	}

	public void setPrmValor(Integer prmValor) {
		this.prmValor = prmValor;
	}

}