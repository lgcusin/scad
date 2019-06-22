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
 * Contenido generated by hbm2java
 */
@Entity
@Table(name = "CONTENIDO")
public class Contenido implements java.io.Serializable {

	private BigDecimal cntId;
	private UnidadCurricular unidadCurricular;
	private String cntDescripcion;
	private String cntEstado;
	private Set<Herramienta> herramientas = new HashSet<Herramienta>(0);
	private Set<Actividad> actividads = new HashSet<Actividad>(0);

	public Contenido() {
	}

	public Contenido(BigDecimal cntId, UnidadCurricular unidadCurricular) {
		this.cntId = cntId;
		this.unidadCurricular = unidadCurricular;
	}

	public Contenido(BigDecimal cntId, UnidadCurricular unidadCurricular, String cntDescripcion, String cntEstado,
			Set<Herramienta> herramientas, Set<Actividad> actividads) {
		this.cntId = cntId;
		this.unidadCurricular = unidadCurricular;
		this.cntDescripcion = cntDescripcion;
		this.cntEstado = cntEstado;
		this.herramientas = herramientas;
		this.actividads = actividads;
	}

	@Id

	@Column(name = "CNT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCntId() {
		return this.cntId;
	}

	public void setCntId(BigDecimal cntId) {
		this.cntId = cntId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNCR_ID", nullable = false)
	public UnidadCurricular getUnidadCurricular() {
		return this.unidadCurricular;
	}

	public void setUnidadCurricular(UnidadCurricular unidadCurricular) {
		this.unidadCurricular = unidadCurricular;
	}

	@Column(name = "CNT_DESCRIPCION", length = 400)
	public String getCntDescripcion() {
		return this.cntDescripcion;
	}

	public void setCntDescripcion(String cntDescripcion) {
		this.cntDescripcion = cntDescripcion;
	}

	@Column(name = "CNT_ESTADO", length = 15)
	public String getCntEstado() {
		return this.cntEstado;
	}

	public void setCntEstado(String cntEstado) {
		this.cntEstado = cntEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenido")
	public Set<Herramienta> getHerramientas() {
		return this.herramientas;
	}

	public void setHerramientas(Set<Herramienta> herramientas) {
		this.herramientas = herramientas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenido")
	public Set<Actividad> getActividads() {
		return this.actividads;
	}

	public void setActividads(Set<Actividad> actividads) {
		this.actividads = actividads;
	}

}