package model;
// Generated 13/02/2019 17:29:01 by Hibernate Tools 4.3.5.Final

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
 * TipoHorario generated by hbm2java
 */
@Entity
@Table(name = "TIPO_HORARIO")
public class TipoHorario implements java.io.Serializable {

	private BigDecimal tphrId;
	private String tphrDescripcion;
	private Set<Horario> horarios = new HashSet<Horario>(0);

	public TipoHorario() {
	}

	public TipoHorario(BigDecimal tphrId) {
		this.tphrId = tphrId;
	}

	public TipoHorario(BigDecimal tphrId, String tphrDescripcion, Set<Horario> horarios) {
		this.tphrId = tphrId;
		this.tphrDescripcion = tphrDescripcion;
		this.horarios = horarios;
	}

	@Id

	@Column(name = "TPHR_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getTphrId() {
		return this.tphrId;
	}

	public void setTphrId(BigDecimal tphrId) {
		this.tphrId = tphrId;
	}

	@Column(name = "TPHR_DESCRIPCION", length = 30)
	public String getTphrDescripcion() {
		return this.tphrDescripcion;
	}

	public void setTphrDescripcion(String tphrDescripcion) {
		this.tphrDescripcion = tphrDescripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoHorario")
	public Set<Horario> getHorarios() {
		return this.horarios;
	}

	public void setHorarios(Set<Horario> horarios) {
		this.horarios = horarios;
	}

}
