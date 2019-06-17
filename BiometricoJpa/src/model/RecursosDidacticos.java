package model;
// Generated 14/06/2019 23:30:02 by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * RecursosDidacticos generated by hbm2java
 */
@Entity
@Table(name = "RECURSOS_DIDACTICOS")
public class RecursosDidacticos implements java.io.Serializable {

	private Integer rcddId;
	private UnidadCurricular unidadCurricular;
	private String rcddDescripcion;

	public RecursosDidacticos() {
	}

	public RecursosDidacticos(Integer rcddId, UnidadCurricular unidadCurricular) {
		this.rcddId = rcddId;
		this.unidadCurricular = unidadCurricular;
	}

	public RecursosDidacticos(Integer rcddId, UnidadCurricular unidadCurricular, String rcddDescripcion) {
		this.rcddId = rcddId;
		this.unidadCurricular = unidadCurricular;
		this.rcddDescripcion = rcddDescripcion;
	}

	@Id

	@Column(name = "RCDD_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getRcddId() {
		return this.rcddId;
	}

	public void setRcddId(Integer rcddId) {
		this.rcddId = rcddId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNCR_ID", nullable = false)
	public UnidadCurricular getUnidadCurricular() {
		return this.unidadCurricular;
	}

	public void setUnidadCurricular(UnidadCurricular unidadCurricular) {
		this.unidadCurricular = unidadCurricular;
	}

	@Column(name = "RCDD_DESCRIPCION", length = 100)
	public String getRcddDescripcion() {
		return this.rcddDescripcion;
	}

	public void setRcddDescripcion(String rcddDescripcion) {
		this.rcddDescripcion = rcddDescripcion;
	}

}
