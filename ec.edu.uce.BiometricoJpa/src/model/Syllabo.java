package model;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Syllabo generated by hbm2java
 */
@Entity
@Table(name = "SYLLABO")
public class Syllabo implements java.io.Serializable {

	private BigDecimal sylId;
	private String sylDescripcion;
	private Set<UnidadCurricular> unidadCurriculars = new HashSet<UnidadCurricular>(0);

	public Syllabo() {
	}

	public Syllabo(BigDecimal sylId) {
		this.sylId = sylId;
	}

	public Syllabo(BigDecimal sylId, String sylDescripcion, Set<UnidadCurricular> unidadCurriculars) {
		this.sylId = sylId;
		this.sylDescripcion = sylDescripcion;
		this.unidadCurriculars = unidadCurriculars;
	}

	@Id

	@Column(name = "SYL_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getSylId() {
		return this.sylId;
	}

	public void setSylId(BigDecimal sylId) {
		this.sylId = sylId;
	}

	@Column(name = "SYL_DESCRIPCION", length = 200)
	public String getSylDescripcion() {
		return this.sylDescripcion;
	}

	public void setSylDescripcion(String sylDescripcion) {
		this.sylDescripcion = sylDescripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "syllabo")
	public Set<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(Set<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

}
