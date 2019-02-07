package model;
// Generated 29/01/2019 23:19:04 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * FichaMatricula generated by hbm2java
 */
@Entity
@Table(name = "FICHA_MATRICULA")
public class FichaMatricula implements java.io.Serializable {

	private BigDecimal fcmtId;
	private FichaEstudiante fichaEstudiante;
	private String fcmtEstado;
	private Set<DetalleMatricula> detalleMatriculas = new HashSet<DetalleMatricula>(0);

	public FichaMatricula() {
	}

	public FichaMatricula(BigDecimal fcmtId, FichaEstudiante fichaEstudiante) {
		this.fcmtId = fcmtId;
		this.fichaEstudiante = fichaEstudiante;
	}

	public FichaMatricula(BigDecimal fcmtId, FichaEstudiante fichaEstudiante, String fcmtEstado,
			Set<DetalleMatricula> detalleMatriculas) {
		this.fcmtId = fcmtId;
		this.fichaEstudiante = fichaEstudiante;
		this.fcmtEstado = fcmtEstado;
		this.detalleMatriculas = detalleMatriculas;
	}

	@Id

	@Column(name = "FCMT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getFcmtId() {
		return this.fcmtId;
	}

	public void setFcmtId(BigDecimal fcmtId) {
		this.fcmtId = fcmtId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCES_ID", nullable = false)
	public FichaEstudiante getFichaEstudiante() {
		return this.fichaEstudiante;
	}

	public void setFichaEstudiante(FichaEstudiante fichaEstudiante) {
		this.fichaEstudiante = fichaEstudiante;
	}

	@Column(name = "FCMT_ESTADO", length = 20)
	public String getFcmtEstado() {
		return this.fcmtEstado;
	}

	public void setFcmtEstado(String fcmtEstado) {
		this.fcmtEstado = fcmtEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaMatricula")
	public Set<DetalleMatricula> getDetalleMatriculas() {
		return this.detalleMatriculas;
	}

	public void setDetalleMatriculas(Set<DetalleMatricula> detalleMatriculas) {
		this.detalleMatriculas = detalleMatriculas;
	}

}
