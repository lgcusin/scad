package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CARRERA database table.
 * 
 */
@Entity
@NamedQuery(name="Carrera.findAll", query="SELECT c FROM Carrera c")
public class Carrera implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CRR_ID")
	private long crrId;

	@Column(name="CRR_NOMBRE")
	private String crrNombre;

	//bi-directional many-to-one association to DetallePuesto
	@OneToMany(mappedBy="carrera")
	private List<DetallePuesto> detallePuestos;

	//bi-directional many-to-one association to MallaCurricular
	@OneToMany(mappedBy="carrera")
	private List<MallaCurricular> mallaCurriculars;

	//bi-directional many-to-one association to Materia
	@OneToMany(mappedBy="carrera")
	private List<Materia> materias;

	public Carrera() {
	}

	public long getCrrId() {
		return this.crrId;
	}

	public void setCrrId(long crrId) {
		this.crrId = crrId;
	}

	public String getCrrNombre() {
		return this.crrNombre;
	}

	public void setCrrNombre(String crrNombre) {
		this.crrNombre = crrNombre;
	}

	public List<DetallePuesto> getDetallePuestos() {
		return this.detallePuestos;
	}

	public void setDetallePuestos(List<DetallePuesto> detallePuestos) {
		this.detallePuestos = detallePuestos;
	}

	public DetallePuesto addDetallePuesto(DetallePuesto detallePuesto) {
		getDetallePuestos().add(detallePuesto);
		detallePuesto.setCarrera(this);

		return detallePuesto;
	}

	public DetallePuesto removeDetallePuesto(DetallePuesto detallePuesto) {
		getDetallePuestos().remove(detallePuesto);
		detallePuesto.setCarrera(null);

		return detallePuesto;
	}

	public List<MallaCurricular> getMallaCurriculars() {
		return this.mallaCurriculars;
	}

	public void setMallaCurriculars(List<MallaCurricular> mallaCurriculars) {
		this.mallaCurriculars = mallaCurriculars;
	}

	public MallaCurricular addMallaCurricular(MallaCurricular mallaCurricular) {
		getMallaCurriculars().add(mallaCurricular);
		mallaCurricular.setCarrera(this);

		return mallaCurricular;
	}

	public MallaCurricular removeMallaCurricular(MallaCurricular mallaCurricular) {
		getMallaCurriculars().remove(mallaCurricular);
		mallaCurricular.setCarrera(null);

		return mallaCurricular;
	}

	public List<Materia> getMaterias() {
		return this.materias;
	}

	public void setMaterias(List<Materia> materias) {
		this.materias = materias;
	}

	public Materia addMateria(Materia materia) {
		getMaterias().add(materia);
		materia.setCarrera(this);

		return materia;
	}

	public Materia removeMateria(Materia materia) {
		getMaterias().remove(materia);
		materia.setCarrera(null);

		return materia;
	}

}