package model;
// Generated 29/01/2019 23:19:04 by Hibernate Tools 4.3.5.Final

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

	private Integer cntId;
	private UnidadCurricular unidadCurricular;
	private String cntDescripcion;
	private Set<Herramienta> herramientas = new HashSet<Herramienta>(0);
	private Set<Actividad> actividads = new HashSet<Actividad>(0);

	public Contenido() {
	}

	public Contenido(Integer cntId, UnidadCurricular unidadCurricular) {
		this.cntId = cntId;
		this.unidadCurricular = unidadCurricular;
	}

	public Contenido(Integer cntId, UnidadCurricular unidadCurricular, String cntDescripcion,
			Set<Herramienta> herramientas, Set<Actividad> actividads) {
		this.cntId = cntId;
		this.unidadCurricular = unidadCurricular;
		this.cntDescripcion = cntDescripcion;
		this.herramientas = herramientas;
		this.actividads = actividads;
	}

	@Id

	@Column(name = "CNT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getCntId() {
		return this.cntId;
	}

	public void setCntId(Integer cntId) {
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
