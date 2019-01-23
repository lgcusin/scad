package model;
// Generated 22/01/2019 20:17:52 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Herramienta generated by hbm2java
 */
@Entity
@Table(name = "HERRAMIENTA")
public class Herramienta implements java.io.Serializable {

	private BigDecimal hrrId;
	private Contenido contenido;
	private String hrrNombre;

	public Herramienta() {
	}

	public Herramienta(BigDecimal hrrId, Contenido contenido) {
		this.hrrId = hrrId;
		this.contenido = contenido;
	}

	public Herramienta(BigDecimal hrrId, Contenido contenido, String hrrNombre) {
		this.hrrId = hrrId;
		this.contenido = contenido;
		this.hrrNombre = hrrNombre;
	}

	@Id

	@Column(name = "HRR_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getHrrId() {
		return this.hrrId;
	}

	public void setHrrId(BigDecimal hrrId) {
		this.hrrId = hrrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CNT_ID", nullable = false)
	public Contenido getContenido() {
		return this.contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	@Column(name = "HRR_NOMBRE", length = 400)
	public String getHrrNombre() {
		return this.hrrNombre;
	}

	public void setHrrNombre(String hrrNombre) {
		this.hrrNombre = hrrNombre;
	}

}
