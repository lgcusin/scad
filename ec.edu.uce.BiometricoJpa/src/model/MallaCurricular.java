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
 * MallaCurricular generated by hbm2java
 */
@Entity
@Table(name = "MALLA_CURRICULAR")
public class MallaCurricular implements java.io.Serializable {

	private BigDecimal mlcrId;
	private Carrera carrera;
	private String mlcrDescripcion;
	private Set<MallaCurricularMateria> mallaCurricularMaterias = new HashSet<MallaCurricularMateria>(0);

	public MallaCurricular() {
	}

	public MallaCurricular(BigDecimal mlcrId, Carrera carrera) {
		this.mlcrId = mlcrId;
		this.carrera = carrera;
	}

	public MallaCurricular(BigDecimal mlcrId, Carrera carrera, String mlcrDescripcion,
			Set<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mlcrId = mlcrId;
		this.carrera = carrera;
		this.mlcrDescripcion = mlcrDescripcion;
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

	@Id

	@Column(name = "MLCR_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getMlcrId() {
		return this.mlcrId;
	}

	public void setMlcrId(BigDecimal mlcrId) {
		this.mlcrId = mlcrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID", nullable = false)
	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	@Column(name = "MLCR_DESCRIPCION", length = 600)
	public String getMlcrDescripcion() {
		return this.mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mallaCurricular")
	public Set<MallaCurricularMateria> getMallaCurricularMaterias() {
		return this.mallaCurricularMaterias;
	}

	public void setMallaCurricularMaterias(Set<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

}
