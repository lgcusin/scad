package model;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * UnidadCurricular generated by hbm2java
 */
@Entity
@Table(name = "UNIDAD_CURRICULAR")
public class UnidadCurricular implements java.io.Serializable {

	private BigDecimal uncrId;
	private Contenido contenido;
	private MallaCurricularMateria mallaCurricularMateria;
	private Syllabo syllabo;
	private String uncrDescripcion;

	public UnidadCurricular() {
	}

	public UnidadCurricular(BigDecimal uncrId, Contenido contenido, MallaCurricularMateria mallaCurricularMateria,
			Syllabo syllabo) {
		this.uncrId = uncrId;
		this.contenido = contenido;
		this.mallaCurricularMateria = mallaCurricularMateria;
		this.syllabo = syllabo;
	}

	public UnidadCurricular(BigDecimal uncrId, Contenido contenido, MallaCurricularMateria mallaCurricularMateria,
			Syllabo syllabo, String uncrDescripcion) {
		this.uncrId = uncrId;
		this.contenido = contenido;
		this.mallaCurricularMateria = mallaCurricularMateria;
		this.syllabo = syllabo;
		this.uncrDescripcion = uncrDescripcion;
	}

	@Id

	@Column(name = "UNCR_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getUncrId() {
		return this.uncrId;
	}

	public void setUncrId(BigDecimal uncrId) {
		this.uncrId = uncrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CNT_ID", nullable = false)
	public Contenido getContenido() {
		return this.contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCRMT_ID", nullable = false)
	public MallaCurricularMateria getMallaCurricularMateria() {
		return this.mallaCurricularMateria;
	}

	public void setMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SYL_ID", nullable = false)
	public Syllabo getSyllabo() {
		return this.syllabo;
	}

	public void setSyllabo(Syllabo syllabo) {
		this.syllabo = syllabo;
	}

	@Column(name = "UNCR_DESCRIPCION", length = 400)
	public String getUncrDescripcion() {
		return this.uncrDescripcion;
	}

	public void setUncrDescripcion(String uncrDescripcion) {
		this.uncrDescripcion = uncrDescripcion;
	}

}
