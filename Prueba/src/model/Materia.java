package model;
// Generated 13/02/2019 17:29:01 by Hibernate Tools 4.3.5.Final

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
 * Materia generated by hbm2java
 */
@Entity
@Table(name = "MATERIA")
public class Materia implements java.io.Serializable {

	private BigDecimal mtrId;
	private Carrera carrera;
	private String mtrNombre;
	private Set<MallaCurricularMateria> mallaCurricularMaterias = new HashSet<MallaCurricularMateria>(0);
	private Set<Horario> horarios = new HashSet<Horario>(0);

	public Materia() {
	}

	public Materia(BigDecimal mtrId, Carrera carrera) {
		this.mtrId = mtrId;
		this.carrera = carrera;
	}

	public Materia(BigDecimal mtrId, Carrera carrera, String mtrNombre,
			Set<MallaCurricularMateria> mallaCurricularMaterias, Set<Horario> horarios) {
		this.mtrId = mtrId;
		this.carrera = carrera;
		this.mtrNombre = mtrNombre;
		this.mallaCurricularMaterias = mallaCurricularMaterias;
		this.horarios = horarios;
	}

	@Id

	@Column(name = "MTR_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getMtrId() {
		return this.mtrId;
	}

	public void setMtrId(BigDecimal mtrId) {
		this.mtrId = mtrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID", nullable = false)
	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	@Column(name = "MTR_NOMBRE", length = 200)
	public String getMtrNombre() {
		return this.mtrNombre;
	}

	public void setMtrNombre(String mtrNombre) {
		this.mtrNombre = mtrNombre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materia")
	public Set<MallaCurricularMateria> getMallaCurricularMaterias() {
		return this.mallaCurricularMaterias;
	}

	public void setMallaCurricularMaterias(Set<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materia")
	public Set<Horario> getHorarios() {
		return this.horarios;
	}

	public void setHorarios(Set<Horario> horarios) {
		this.horarios = horarios;
	}

}
