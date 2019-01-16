package model;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

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
 * MallaCurricularMateria generated by hbm2java
 */
@Entity
@Table(name = "MALLA_CURRICULAR_MATERIA")
public class MallaCurricularMateria implements java.io.Serializable {

	private BigDecimal mlcrmtId;
	private MallaCurricular mallaCurricular;
	private Materia materia;
	private BigDecimal mlcrmtEstado;
	private Set<UnidadCurricular> unidadCurriculars = new HashSet<UnidadCurricular>(0);
	private Set<Seguimiento> seguimientos = new HashSet<Seguimiento>(0);

	public MallaCurricularMateria() {
	}

	public MallaCurricularMateria(BigDecimal mlcrmtId, MallaCurricular mallaCurricular, Materia materia) {
		this.mlcrmtId = mlcrmtId;
		this.mallaCurricular = mallaCurricular;
		this.materia = materia;
	}

	public MallaCurricularMateria(BigDecimal mlcrmtId, MallaCurricular mallaCurricular, Materia materia,
			BigDecimal mlcrmtEstado, Set<UnidadCurricular> unidadCurriculars, Set<Seguimiento> seguimientos) {
		this.mlcrmtId = mlcrmtId;
		this.mallaCurricular = mallaCurricular;
		this.materia = materia;
		this.mlcrmtEstado = mlcrmtEstado;
		this.unidadCurriculars = unidadCurriculars;
		this.seguimientos = seguimientos;
	}

	@Id

	@Column(name = "MLCRMT_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getMlcrmtId() {
		return this.mlcrmtId;
	}

	public void setMlcrmtId(BigDecimal mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCR_ID", nullable = false)
	public MallaCurricular getMallaCurricular() {
		return this.mallaCurricular;
	}

	public void setMallaCurricular(MallaCurricular mallaCurricular) {
		this.mallaCurricular = mallaCurricular;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MTR_ID", nullable = false)
	public Materia getMateria() {
		return this.materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	@Column(name = "MLCRMT_ESTADO", precision = 38, scale = 0)
	public BigDecimal getMlcrmtEstado() {
		return this.mlcrmtEstado;
	}

	public void setMlcrmtEstado(BigDecimal mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mallaCurricularMateria")
	public Set<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(Set<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mallaCurricularMateria")
	public Set<Seguimiento> getSeguimientos() {
		return this.seguimientos;
	}

	public void setSeguimientos(Set<Seguimiento> seguimientos) {
		this.seguimientos = seguimientos;
	}

}