package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MALLA_CURRICULAR database table.
 * 
 */
@Entity
@Table(name="MALLA_CURRICULAR")
@NamedQuery(name="MallaCurricular.findAll", query="SELECT m FROM MallaCurricular m")
public class MallaCurricular implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MLCR_ID")
	private long mlcrId;

	@Column(name="MLCR_DESCRIPCION")
	private String mlcrDescripcion;

	//bi-directional many-to-one association to Carrera
	@ManyToOne
	@JoinColumn(name="CRR_ID")
	private Carrera carrera;

	//bi-directional many-to-one association to MallaCurricularMateria
	@OneToMany(mappedBy="mallaCurricular")
	private List<MallaCurricularMateria> mallaCurricularMaterias;

	public MallaCurricular() {
	}

	public long getMlcrId() {
		return this.mlcrId;
	}

	public void setMlcrId(long mlcrId) {
		this.mlcrId = mlcrId;
	}

	public String getMlcrDescripcion() {
		return this.mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
	}

	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public List<MallaCurricularMateria> getMallaCurricularMaterias() {
		return this.mallaCurricularMaterias;
	}

	public void setMallaCurricularMaterias(List<MallaCurricularMateria> mallaCurricularMaterias) {
		this.mallaCurricularMaterias = mallaCurricularMaterias;
	}

	public MallaCurricularMateria addMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		getMallaCurricularMaterias().add(mallaCurricularMateria);
		mallaCurricularMateria.setMallaCurricular(this);

		return mallaCurricularMateria;
	}

	public MallaCurricularMateria removeMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		getMallaCurricularMaterias().remove(mallaCurricularMateria);
		mallaCurricularMateria.setMallaCurricular(null);

		return mallaCurricularMateria;
	}

}