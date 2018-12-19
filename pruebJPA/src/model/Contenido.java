package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CONTENIDO database table.
 * 
 */
@Entity
@NamedQuery(name="Contenido.findAll", query="SELECT c FROM Contenido c")
public class Contenido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CNT_ID")
	private long cntId;

	//bi-directional many-to-one association to Actividad
	@OneToMany(mappedBy="contenido")
	private List<Actividad> actividads;

	//bi-directional many-to-one association to Herramienta
	@OneToMany(mappedBy="contenido")
	private List<Herramienta> herramientas;

	//bi-directional many-to-one association to UnidadCurricular
	@OneToMany(mappedBy="contenido")
	private List<UnidadCurricular> unidadCurriculars;

	public Contenido() {
	}

	public long getCntId() {
		return this.cntId;
	}

	public void setCntId(long cntId) {
		this.cntId = cntId;
	}

	public List<Actividad> getActividads() {
		return this.actividads;
	}

	public void setActividads(List<Actividad> actividads) {
		this.actividads = actividads;
	}

	public Actividad addActividad(Actividad actividad) {
		getActividads().add(actividad);
		actividad.setContenido(this);

		return actividad;
	}

	public Actividad removeActividad(Actividad actividad) {
		getActividads().remove(actividad);
		actividad.setContenido(null);

		return actividad;
	}

	public List<Herramienta> getHerramientas() {
		return this.herramientas;
	}

	public void setHerramientas(List<Herramienta> herramientas) {
		this.herramientas = herramientas;
	}

	public Herramienta addHerramienta(Herramienta herramienta) {
		getHerramientas().add(herramienta);
		herramienta.setContenido(this);

		return herramienta;
	}

	public Herramienta removeHerramienta(Herramienta herramienta) {
		getHerramientas().remove(herramienta);
		herramienta.setContenido(null);

		return herramienta;
	}

	public List<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(List<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

	public UnidadCurricular addUnidadCurricular(UnidadCurricular unidadCurricular) {
		getUnidadCurriculars().add(unidadCurricular);
		unidadCurricular.setContenido(this);

		return unidadCurricular;
	}

	public UnidadCurricular removeUnidadCurricular(UnidadCurricular unidadCurricular) {
		getUnidadCurriculars().remove(unidadCurricular);
		unidadCurricular.setContenido(null);

		return unidadCurricular;
	}

}