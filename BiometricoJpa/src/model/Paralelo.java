package model;
// Generated 29/01/2019 23:19:04 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
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
 * Paralelo generated by hbm2java
 */
@Entity
@Table(name = "PARALELO")
public class Paralelo implements java.io.Serializable {

	private BigDecimal prlId;
	private Carrera carrera;
	private String prlCodigo;
	private String prlDescripcion;
	private BigDecimal prlEstado;
	private BigDecimal prlCupo;
	private Set<MallaCurricularParalelo> mallaCurricularParalelos = new HashSet<MallaCurricularParalelo>(0);

	public Paralelo() {
	}

	public Paralelo(BigDecimal prlId, Carrera carrera) {
		this.prlId = prlId;
		this.carrera = carrera;
	}

	public Paralelo(BigDecimal prlId, Carrera carrera, String prlCodigo, String prlDescripcion, BigDecimal prlEstado,
			BigDecimal prlCupo, Set<MallaCurricularParalelo> mallaCurricularParalelos) {
		this.prlId = prlId;
		this.carrera = carrera;
		this.prlCodigo = prlCodigo;
		this.prlDescripcion = prlDescripcion;
		this.prlEstado = prlEstado;
		this.prlCupo = prlCupo;
		this.mallaCurricularParalelos = mallaCurricularParalelos;
	}

	@Id

	@Column(name = "PRL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getPrlId() {
		return this.prlId;
	}

	public void setPrlId(BigDecimal prlId) {
		this.prlId = prlId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID", nullable = false)
	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	@Column(name = "PRL_CODIGO", length = 128)
	public String getPrlCodigo() {
		return this.prlCodigo;
	}

	public void setPrlCodigo(String prlCodigo) {
		this.prlCodigo = prlCodigo;
	}

	@Column(name = "PRL_DESCRIPCION", length = 250)
	public String getPrlDescripcion() {
		return this.prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
	}

	@Column(name = "PRL_ESTADO", precision = 22, scale = 0)
	public BigDecimal getPrlEstado() {
		return this.prlEstado;
	}

	public void setPrlEstado(BigDecimal prlEstado) {
		this.prlEstado = prlEstado;
	}

	@Column(name = "PRL_CUPO", precision = 22, scale = 0)
	public BigDecimal getPrlCupo() {
		return this.prlCupo;
	}

	public void setPrlCupo(BigDecimal prlCupo) {
		this.prlCupo = prlCupo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paralelo")
	public Set<MallaCurricularParalelo> getMallaCurricularParalelos() {
		return this.mallaCurricularParalelos;
	}

	public void setMallaCurricularParalelos(Set<MallaCurricularParalelo> mallaCurricularParalelos) {
		this.mallaCurricularParalelos = mallaCurricularParalelos;
	}

}
