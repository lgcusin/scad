package model;
// Generated 17/02/2019 21:49:48 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Seguimiento generated by hbm2java
 */
@Entity
@Table(name = "SEGUIMIENTO")
public class Seguimiento implements java.io.Serializable {

	private BigDecimal sgmId;
	private MallaCurricularMateria mallaCurricularMateria;
	private Asistencia asistencia;

	public Seguimiento() {
	}

	public Seguimiento(BigDecimal sgmId, MallaCurricularMateria mallaCurricularMateria, Asistencia asistencia) {
		this.sgmId = sgmId;
		this.mallaCurricularMateria = mallaCurricularMateria;
		this.asistencia = asistencia;
	}

	@Id

	@Column(name = "SGM_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getSgmId() {
		return this.sgmId;
	}

	public void setSgmId(BigDecimal sgmId) {
		this.sgmId = sgmId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCRMT_ID", nullable = false)
	public MallaCurricularMateria getMallaCurricularMateria() {
		return this.mallaCurricularMateria;
	}

	public void setMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASS_ID", nullable = false)
	public Asistencia getAsistencia() {
		return this.asistencia;
	}

	public void setAsistencia(Asistencia asistencia) {
		this.asistencia = asistencia;
	}

}
