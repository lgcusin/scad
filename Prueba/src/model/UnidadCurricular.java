package model;
// Generated 15/04/2019 20:41:59 by Hibernate Tools 4.3.5.Final

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
 * UnidadCurricular generated by hbm2java
 */
@Entity
@Table(name = "UNIDAD_CURRICULAR")
public class UnidadCurricular implements java.io.Serializable {

	private BigDecimal uncrId;
	private Syllabo syllabo;
	private String uncrDescripcion;
	private BigDecimal uncrTotalHoras;
	private String uncrNombre;
	private String uncrObjetivo;
	private String uncrResultado;
	private Set<Contenido> contenidos = new HashSet<Contenido>(0);

	public UnidadCurricular() {
	}

	public UnidadCurricular(BigDecimal uncrId, Syllabo syllabo) {
		this.uncrId = uncrId;
		this.syllabo = syllabo;
	}

	public UnidadCurricular(BigDecimal uncrId, Syllabo syllabo, String uncrDescripcion, BigDecimal uncrTotalHoras,
			String uncrNombre, String uncrObjetivo, String uncrResultado, Set<Contenido> contenidos) {
		this.uncrId = uncrId;
		this.syllabo = syllabo;
		this.uncrDescripcion = uncrDescripcion;
		this.uncrTotalHoras = uncrTotalHoras;
		this.uncrNombre = uncrNombre;
		this.uncrObjetivo = uncrObjetivo;
		this.uncrResultado = uncrResultado;
		this.contenidos = contenidos;
	}

	@Id

	@Column(name = "UNCR_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getUncrId() {
		return this.uncrId;
	}

	public void setUncrId(BigDecimal uncrId) {
		this.uncrId = uncrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SYL_ID", nullable = false)
	public Syllabo getSyllabo() {
		return this.syllabo;
	}

	public void setSyllabo(Syllabo syllabo) {
		this.syllabo = syllabo;
	}

	@Column(name = "UNCR_DESCRIPCION", length = 100)
	public String getUncrDescripcion() {
		return this.uncrDescripcion;
	}

	public void setUncrDescripcion(String uncrDescripcion) {
		this.uncrDescripcion = uncrDescripcion;
	}

	@Column(name = "UNCR_TOTAL_HORAS", precision = 22, scale = 0)
	public BigDecimal getUncrTotalHoras() {
		return this.uncrTotalHoras;
	}

	public void setUncrTotalHoras(BigDecimal uncrTotalHoras) {
		this.uncrTotalHoras = uncrTotalHoras;
	}

	@Column(name = "UNCR_NOMBRE", length = 100)
	public String getUncrNombre() {
		return this.uncrNombre;
	}

	public void setUncrNombre(String uncrNombre) {
		this.uncrNombre = uncrNombre;
	}

	@Column(name = "UNCR_OBJETIVO", length = 400)
	public String getUncrObjetivo() {
		return this.uncrObjetivo;
	}

	public void setUncrObjetivo(String uncrObjetivo) {
		this.uncrObjetivo = uncrObjetivo;
	}

	@Column(name = "UNCR_RESULTADO", length = 400)
	public String getUncrResultado() {
		return this.uncrResultado;
	}

	public void setUncrResultado(String uncrResultado) {
		this.uncrResultado = uncrResultado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadCurricular")
	public Set<Contenido> getContenidos() {
		return this.contenidos;
	}

	public void setContenidos(Set<Contenido> contenidos) {
		this.contenidos = contenidos;
	}

}
