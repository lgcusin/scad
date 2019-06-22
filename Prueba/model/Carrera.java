package model;
// Generated 16/06/2019 20:52:12 by Hibernate Tools 4.3.5.Final

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
 * Carrera generated by hbm2java
 */
@Entity
@Table(name = "CARRERA")
public class Carrera implements java.io.Serializable {

	private BigDecimal crrId;
	private Facultad facultad;
	private String crrNombre;
	private BigDecimal crrCodigo;
	private Set<Paralelo> paralelos = new HashSet<Paralelo>(0);
	private Set<DetallePuesto> detallePuestos = new HashSet<DetallePuesto>(0);
	private Set<Materia> materias = new HashSet<Materia>(0);
	private Set<MallaCurricular> mallaCurriculars = new HashSet<MallaCurricular>(0);

	public Carrera() {
	}

	public Carrera(BigDecimal crrId, Facultad facultad, BigDecimal crrCodigo) {
		this.crrId = crrId;
		this.facultad = facultad;
		this.crrCodigo = crrCodigo;
	}

	public Carrera(BigDecimal crrId, Facultad facultad, String crrNombre, BigDecimal crrCodigo, Set<Paralelo> paralelos,
			Set<DetallePuesto> detallePuestos, Set<Materia> materias, Set<MallaCurricular> mallaCurriculars) {
		this.crrId = crrId;
		this.facultad = facultad;
		this.crrNombre = crrNombre;
		this.crrCodigo = crrCodigo;
		this.paralelos = paralelos;
		this.detallePuestos = detallePuestos;
		this.materias = materias;
		this.mallaCurriculars = mallaCurriculars;
	}

	@Id

	@Column(name = "CRR_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getCrrId() {
		return this.crrId;
	}

	public void setCrrId(BigDecimal crrId) {
		this.crrId = crrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCL_ID", nullable = false)
	public Facultad getFacultad() {
		return this.facultad;
	}

	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}

	@Column(name = "CRR_NOMBRE", length = 400)
	public String getCrrNombre() {
		return this.crrNombre;
	}

	public void setCrrNombre(String crrNombre) {
		this.crrNombre = crrNombre;
	}

	@Column(name = "CRR_CODIGO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getCrrCodigo() {
		return this.crrCodigo;
	}

	public void setCrrCodigo(BigDecimal crrCodigo) {
		this.crrCodigo = crrCodigo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carrera")
	public Set<Paralelo> getParalelos() {
		return this.paralelos;
	}

	public void setParalelos(Set<Paralelo> paralelos) {
		this.paralelos = paralelos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carrera")
	public Set<DetallePuesto> getDetallePuestos() {
		return this.detallePuestos;
	}

	public void setDetallePuestos(Set<DetallePuesto> detallePuestos) {
		this.detallePuestos = detallePuestos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carrera")
	public Set<Materia> getMaterias() {
		return this.materias;
	}

	public void setMaterias(Set<Materia> materias) {
		this.materias = materias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carrera")
	public Set<MallaCurricular> getMallaCurriculars() {
		return this.mallaCurriculars;
	}

	public void setMallaCurriculars(Set<MallaCurricular> mallaCurriculars) {
		this.mallaCurriculars = mallaCurriculars;
	}

}