package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MATERIA database table.
 * 
 */
@Entity
@NamedQuery(name="Materia.findAll", query="SELECT m FROM Materia m")
public class Materia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MTR_ID")
	private long mtrId;

	@Column(name="MTR_NOMBRE")
	private String mtrNombre;

	//bi-directional many-to-one association to MallaCurricularMateria
	@OneToMany(mappedBy="materia")
	private List<MallaCurricularMateria> mallaCurricularMaterias;

	//bi-directional many-to-one association to Carrera
	@ManyToOne
	@JoinColumn(name="CRR_ID")
	private Carrera carrera;

	public Materia() {
	}

	public long getMtrId() {
		return this.mtrId;
	}

	public void setMtrId(long mtrId) {
		this.mtrId = mtrId;
	}

	public String getMtrNombre() {
		return this.mtrNombre;
	}

	public void setMtrNombre(String mtrNombre) {
		this.mtrNombre = mtrNombre;
	}

	public List<MallaCurricularMateria> getMallaCurricularMaterias() {
		return this.mallaCurricularMaterias;
	}

	public void setMallaCurricularMaterias(List<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

	public MallaCurricularMateria addMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		getMallaCurricularMaterias().add(mallaCurricularMateria);
		mallaCurricularMateria.setMateria(this);

		return mallaCurricularMateria;
	}

	public MallaCurricularMateria removeMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		getMallaCurricularMaterias().remove(mallaCurricularMateria);
		mallaCurricularMateria.setMateria(null);

		return mallaCurricularMateria;
	}

	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

}