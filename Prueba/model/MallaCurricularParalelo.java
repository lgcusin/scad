package model;
// Generated 22/06/2019 19:40:53 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MallaCurricularParalelo generated by hbm2java
 */
@Entity
@Table(name = "MALLA_CURRICULAR_PARALELO")
public class MallaCurricularParalelo implements java.io.Serializable {

	private BigDecimal mlcrprId;
	private Paralelo paralelo;
	private MallaCurricularMateria mallaCurricularMateria;
	private BigDecimal mlcrprInscritos;

	public MallaCurricularParalelo() {
	}

	public MallaCurricularParalelo(BigDecimal mlcrprId, Paralelo paralelo,
			MallaCurricularMateria mallaCurricularMateria) {
		this.mlcrprId = mlcrprId;
		this.paralelo = paralelo;
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	public MallaCurricularParalelo(BigDecimal mlcrprId, Paralelo paralelo,
			MallaCurricularMateria mallaCurricularMateria, BigDecimal mlcrprInscritos) {
		this.mlcrprId = mlcrprId;
		this.paralelo = paralelo;
		this.mallaCurricularMateria = mallaCurricularMateria;
		this.mlcrprInscritos = mlcrprInscritos;
	}

	@Id

	@Column(name = "MLCRPR_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getMlcrprId() {
		return this.mlcrprId;
	}

	public void setMlcrprId(BigDecimal mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRL_ID", nullable = false)
	public Paralelo getParalelo() {
		return this.paralelo;
	}

	public void setParalelo(Paralelo paralelo) {
		this.paralelo = paralelo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCRMT_ID", nullable = false)
	public MallaCurricularMateria getMallaCurricularMateria() {
		return this.mallaCurricularMateria;
	}

	public void setMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	@Column(name = "MLCRPR_INSCRITOS", precision = 22, scale = 0)
	public BigDecimal getMlcrprInscritos() {
		return this.mlcrprInscritos;
	}

	public void setMlcrprInscritos(BigDecimal mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

}
