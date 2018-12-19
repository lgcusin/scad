package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SYLLABO database table.
 * 
 */
@Entity
@NamedQuery(name="Syllabo.findAll", query="SELECT s FROM Syllabo s")
public class Syllabo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SYL_ID")
	private long sylId;

	@Column(name="SYL_DESCRIPCION")
	private String sylDescripcion;

	//bi-directional many-to-one association to UnidadCurricular
	@OneToMany(mappedBy="syllabo")
	private List<UnidadCurricular> unidadCurriculars;

	public Syllabo() {
	}

	public long getSylId() {
		return this.sylId;
	}

	public void setSylId(long sylId) {
		this.sylId = sylId;
	}

	public String getSylDescripcion() {
		return this.sylDescripcion;
	}

	public void setSylDescripcion(String sylDescripcion) {
		this.sylDescripcion = sylDescripcion;
	}

	public List<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(List<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

	public UnidadCurricular addUnidadCurricular(UnidadCurricular unidadCurricular) {
		getUnidadCurriculars().add(unidadCurricular);
		unidadCurricular.setSyllabo(this);

		return unidadCurricular;
	}

	public UnidadCurricular removeUnidadCurricular(UnidadCurricular unidadCurricular) {
		getUnidadCurriculars().remove(unidadCurricular);
		unidadCurricular.setSyllabo(null);

		return unidadCurricular;
	}

}