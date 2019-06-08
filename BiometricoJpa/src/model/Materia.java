package model;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Materia generated by hbm2java
 */
@Entity
@Table(name = "MATERIA")
@NamedQueries({ @NamedQuery(name = "Materia.findAll", query = "select m from Materia as m"),
		@NamedQuery(name = "Materia.findAllById", query = "select m from Materia as m where m.carrera.crrId=:idcr"),
		@NamedQuery(name = "Materia.findByHrId", query = "select h.materia from Horario as h where h.hrrId=:hrrId"),
		@NamedQuery(name = "Materia.findMateriaBySemestre", query = "select m.mtrId,m.mtrNombre from MallaCurricularMateria as mcm"
				+ " join mcm.materia as m join mcm.semestre as s where s.smsId=:smsId") })

public class Materia implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer mtrId;
	private Carrera carrera;
	private String mtrNombre;
	private List<MallaCurricularMateria> mallaCurricularMaterias = new ArrayList<>(0);

	public Materia() {
	}

	public Materia(Integer mtrId, Carrera carrera) {
		this.mtrId = mtrId;
		this.carrera = carrera;
	}

	public Materia(Integer mtrId, Carrera carrera, String mtrNombre,
			List<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mtrId = mtrId;
		this.carrera = carrera;
		this.mtrNombre = mtrNombre;
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

	@Id

	@Column(name = "MTR_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Integer getMtrId() {
		return this.mtrId;
	}

	public void setMtrId(Integer mtrId) {
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
	public List<MallaCurricularMateria> getMallaCurricularMaterias() {
		return this.mallaCurricularMaterias;
	}

	public void setMallaCurricularMaterias(List<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Materia [mtrId=" + mtrId + ", carrera=" + carrera + ", mtrNombre=" + mtrNombre
				+ ", mallaCurricularMaterias=" + mallaCurricularMaterias + "]";
	}
}
