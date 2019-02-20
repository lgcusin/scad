package model;
// Generated 17/02/2019 21:49:48 by Hibernate Tools 4.3.5.Final

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
	private Semestre semestre;
	private Materia materia;
	private MallaCurricular mallaCurricular;
	private String mlcrmtEstado;
	private Set<MallaCurricularParalelo> mallaCurricularParalelos = new HashSet<MallaCurricularParalelo>(0);
	private Set<Seguimiento> seguimientos = new HashSet<Seguimiento>(0);
	private Set<Syllabo> syllabos = new HashSet<Syllabo>(0);

	public MallaCurricularMateria() {
	}

	public MallaCurricularMateria(BigDecimal mlcrmtId, Materia materia, MallaCurricular mallaCurricular) {
		this.mlcrmtId = mlcrmtId;
		this.materia = materia;
		this.mallaCurricular = mallaCurricular;
	}

	public MallaCurricularMateria(BigDecimal mlcrmtId, Semestre semestre, Materia materia,
			MallaCurricular mallaCurricular, String mlcrmtEstado, Set<MallaCurricularParalelo> mallaCurricularParalelos,
			Set<Seguimiento> seguimientos, Set<Syllabo> syllabos) {
		this.mlcrmtId = mlcrmtId;
		this.semestre = semestre;
		this.materia = materia;
		this.mallaCurricular = mallaCurricular;
		this.mlcrmtEstado = mlcrmtEstado;
		this.mallaCurricularParalelos = mallaCurricularParalelos;
		this.seguimientos = seguimientos;
		this.syllabos = syllabos;
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
	@JoinColumn(name = "SMS_ID")
	public Semestre getSemestre() {
		return this.semestre;
	}

	public void setSemestre(Semestre semestre) {
		this.semestre = semestre;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MTR_ID", nullable = false)
	public Materia getMateria() {
		return this.materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCR_ID", nullable = false)
	public MallaCurricular getMallaCurricular() {
		return this.mallaCurricular;
	}

	public void setMallaCurricular(MallaCurricular mallaCurricular) {
		this.mallaCurricular = mallaCurricular;
	}

	@Column(name = "MLCRMT_ESTADO", length = 20)
	public String getMlcrmtEstado() {
		return this.mlcrmtEstado;
	}

	public void setMlcrmtEstado(String mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mallaCurricularMateria")
	public Set<MallaCurricularParalelo> getMallaCurricularParalelos() {
		return this.mallaCurricularParalelos;
	}

	public void setMallaCurricularParalelos(Set<MallaCurricularParalelo> mallaCurricularParalelos) {
		this.mallaCurricularParalelos = mallaCurricularParalelos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mallaCurricularMateria")
	public Set<Seguimiento> getSeguimientos() {
		return this.seguimientos;
	}

	public void setSeguimientos(Set<Seguimiento> seguimientos) {
		this.seguimientos = seguimientos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mallaCurricularMateria")
	public Set<Syllabo> getSyllabos() {
		return this.syllabos;
	}

	public void setSyllabos(Set<Syllabo> syllabos) {
		this.syllabos = syllabos;
	}

}
