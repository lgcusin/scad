package model;
// Generated 11/01/2019 10:51:26 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Actividad generated by hbm2java
 */
@Entity
@Table(name = "ACTIVIDAD")
public class Actividad implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal actId;
	private Contenido contenido;

	public Actividad() {
	}

	public Actividad(BigDecimal actId, Contenido contenido) {
		this.actId = actId;
		this.contenido = contenido;
	}

	@Id

	@Column(name = "ACT_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getActId() {
		return this.actId;
	}

	public void setActId(BigDecimal actId) {
		this.actId = actId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CNT_ID", nullable = false)
	public Contenido getContenido() {
		return this.contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

}
