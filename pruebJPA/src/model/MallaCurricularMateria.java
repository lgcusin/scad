package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the MALLA_CURRICULAR_MATERIA database table.
 * 
 */
@Entity
@Table(name="MALLA_CURRICULAR_MATERIA")
@NamedQuery(name="MallaCurricularMateria.findAll", query="SELECT m FROM MallaCurricularMateria m")
public class MallaCurricularMateria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MLCRMT_ID")
	private long mlcrmtId;

	@Column(name="MLCRMT_ESTADO")
	private BigDecimal mlcrmtEstado;

	//bi-directional many-to-one association to MallaCurricular
	@ManyToOne
	@JoinColumn(name="MLCR_ID")
	private MallaCurricular mallaCurricular;

	//bi-directional many-to-one association to Materia
	@ManyToOne
	@JoinColumn(name="MTR_ID")
	private Materia materia;

	//bi-directional many-to-one association to UnidadCurricular
	@OneToMany(mappedBy="mallaCurricularMateria")
	private List<UnidadCurricular> unidadCurriculars;

	public MallaCurricularMateria() {
	}

	public long getMlcrmtId() {
		return this.mlcrmtId;
	}

	public void setMlcrmtId(long mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public BigDecimal getMlcrmtEstado() {
		return this.mlcrmtEstado;
	}

	public void setMlcrmtEstado(BigDecimal mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	public MallaCurricular getMallaCurricular() {
		return this.mallaCurricular;
	}

	public void setMallaCurricular(MallaCurricular mallaCurricular) {
		this.mallaCurricular = mallaCurricular;
	}

	public Materia getMateria() {
		return this.materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public List<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(List<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

	public UnidadCurricular addUnidadCurricular(UnidadCurricular unidadCurricular) {
		getUnidadCurriculars().add(unidadCurricular);
		unidadCurricular.setMallaCurricularMateria(this);

		return unidadCurricular;
	}

	public UnidadCurricular removeUnidadCurricular(UnidadCurricular unidadCurricular) {
		getUnidadCurriculars().remove(unidadCurricular);
		unidadCurricular.setMallaCurricularMateria(null);

		return unidadCurricular;
	}

}