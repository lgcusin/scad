package model;
// Generated 20/03/2019 20:46:57 by Hibernate Tools 4.3.5.Final

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
 * Aula generated by hbm2java
 */
@Entity
@Table(name = "AULA")
public class Aula implements java.io.Serializable {

	private BigDecimal aulId;
	private String aulNombre;
	private Set<Horario> horarios = new HashSet<Horario>(0);

	public Aula() {
	}

	public Aula(BigDecimal aulId) {
		this.aulId = aulId;
	}

	public Aula(BigDecimal aulId, String aulNombre, Set<Horario> horarios) {
		this.aulId = aulId;
		this.aulNombre = aulNombre;
		this.horarios = horarios;
	}

	@Id

	@Column(name = "AUL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getAulId() {
		return this.aulId;
	}

	public void setAulId(BigDecimal aulId) {
		this.aulId = aulId;
	}

	@Column(name = "AUL_NOMBRE", length = 30)
	public String getAulNombre() {
		return this.aulNombre;
	}

	public void setAulNombre(String aulNombre) {
		this.aulNombre = aulNombre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "aula")
	public Set<Horario> getHorarios() {
		return this.horarios;
	}

	public void setHorarios(Set<Horario> horarios) {
		this.horarios = horarios;
	}

}
