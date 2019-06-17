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
 * Bibliografia generated by hbm2java
 */
@Entity
@Table(name = "BIBLIOGRAFIA")
public class Bibliografia implements java.io.Serializable {

	private Integer bblId;
	private UnidadCurricular unidadCurricular;
	private String bblTipo;
	private String bblObraFisica;
	private String bblDisponibilidad;
	private String bblVirtual;
	private String bblNombreVirtual;

	public Bibliografia() {
	}

	public Bibliografia(Integer bblId, UnidadCurricular unidadCurricular) {
		this.bblId = bblId;
		this.unidadCurricular = unidadCurricular;
	}

	public Bibliografia(Integer bblId, UnidadCurricular unidadCurricular, String bblTipo, String bblObraFisica,
			String bblDisponibilidad, String bblVirtual, String bblNombreVirtual) {
		this.bblId = bblId;
		this.unidadCurricular = unidadCurricular;
		this.bblTipo = bblTipo;
		this.bblObraFisica = bblObraFisica;
		this.bblDisponibilidad = bblDisponibilidad;
		this.bblVirtual = bblVirtual;
		this.bblNombreVirtual = bblNombreVirtual;
	}

	@Id

	@Column(name = "BBL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getBblId() {
		return this.bblId;
	}

	public void setBblId(Integer bblId) {
		this.bblId = bblId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNCR_ID", nullable = false)
	public UnidadCurricular getUnidadCurricular() {
		return this.unidadCurricular;
	}

	public void setUnidadCurricular(UnidadCurricular unidadCurricular) {
		this.unidadCurricular = unidadCurricular;
	}

	@Column(name = "BBL_TIPO", length = 20)
	public String getBblTipo() {
		return this.bblTipo;
	}

	public void setBblTipo(String bblTipo) {
		this.bblTipo = bblTipo;
	}

	@Column(name = "BBL_OBRA_FISICA", length = 75)
	public String getBblObraFisica() {
		return this.bblObraFisica;
	}

	public void setBblObraFisica(String bblObraFisica) {
		this.bblObraFisica = bblObraFisica;
	}

	@Column(name = "BBL_DISPONIBILIDAD", length = 2)
	public String getBblDisponibilidad() {
		return this.bblDisponibilidad;
	}

	public void setBblDisponibilidad(String bblDisponibilidad) {
		this.bblDisponibilidad = bblDisponibilidad;
	}

	@Column(name = "BBL_VIRTUAL", length = 150)
	public String getBblVirtual() {
		return this.bblVirtual;
	}

	public void setBblVirtual(String bblVirtual) {
		this.bblVirtual = bblVirtual;
	}

	@Column(name = "BBL_NOMBRE_VIRTUAL", length = 150)
	public String getBblNombreVirtual() {
		return this.bblNombreVirtual;
	}

	public void setBblNombreVirtual(String bblNombreVirtual) {
		this.bblNombreVirtual = bblNombreVirtual;
	}

}
