package model;
// Generated 16/06/2019 20:52:12 by Hibernate Tools 4.3.5.Final

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
	private BigDecimal uncrHorasTeoricas;
	private BigDecimal uncrHorasPracticas;
	private BigDecimal uncrHorasPresenciales;
	private BigDecimal uncrHorasVirtual;
	private Set<Metodologia> metodologias = new HashSet<Metodologia>(0);
	private Set<RecursosDidacticos> recursosDidacticoses = new HashSet<RecursosDidacticos>(0);
	private Set<Contenido> contenidos = new HashSet<Contenido>(0);
	private Set<Bibliografia> bibliografias = new HashSet<Bibliografia>(0);

	public UnidadCurricular() {
	}

	public UnidadCurricular(BigDecimal uncrId, Syllabo syllabo) {
		this.uncrId = uncrId;
		this.syllabo = syllabo;
	}

	public UnidadCurricular(BigDecimal uncrId, Syllabo syllabo, String uncrDescripcion, BigDecimal uncrTotalHoras,
			String uncrNombre, String uncrObjetivo, String uncrResultado, BigDecimal uncrHorasTeoricas,
			BigDecimal uncrHorasPracticas, BigDecimal uncrHorasPresenciales, BigDecimal uncrHorasVirtual,
			Set<Metodologia> metodologias, Set<RecursosDidacticos> recursosDidacticoses, Set<Contenido> contenidos,
			Set<Bibliografia> bibliografias) {
		this.uncrId = uncrId;
		this.syllabo = syllabo;
		this.uncrDescripcion = uncrDescripcion;
		this.uncrTotalHoras = uncrTotalHoras;
		this.uncrNombre = uncrNombre;
		this.uncrObjetivo = uncrObjetivo;
		this.uncrResultado = uncrResultado;
		this.uncrHorasTeoricas = uncrHorasTeoricas;
		this.uncrHorasPracticas = uncrHorasPracticas;
		this.uncrHorasPresenciales = uncrHorasPresenciales;
		this.uncrHorasVirtual = uncrHorasVirtual;
		this.metodologias = metodologias;
		this.recursosDidacticoses = recursosDidacticoses;
		this.contenidos = contenidos;
		this.bibliografias = bibliografias;
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

	@Column(name = "UNCR_HORAS_TEORICAS", precision = 22, scale = 0)
	public BigDecimal getUncrHorasTeoricas() {
		return this.uncrHorasTeoricas;
	}

	public void setUncrHorasTeoricas(BigDecimal uncrHorasTeoricas) {
		this.uncrHorasTeoricas = uncrHorasTeoricas;
	}

	@Column(name = "UNCR_HORAS_PRACTICAS", precision = 22, scale = 0)
	public BigDecimal getUncrHorasPracticas() {
		return this.uncrHorasPracticas;
	}

	public void setUncrHorasPracticas(BigDecimal uncrHorasPracticas) {
		this.uncrHorasPracticas = uncrHorasPracticas;
	}

	@Column(name = "UNCR_HORAS_PRESENCIALES", precision = 22, scale = 0)
	public BigDecimal getUncrHorasPresenciales() {
		return this.uncrHorasPresenciales;
	}

	public void setUncrHorasPresenciales(BigDecimal uncrHorasPresenciales) {
		this.uncrHorasPresenciales = uncrHorasPresenciales;
	}

	@Column(name = "UNCR_HORAS_VIRTUAL", precision = 22, scale = 0)
	public BigDecimal getUncrHorasVirtual() {
		return this.uncrHorasVirtual;
	}

	public void setUncrHorasVirtual(BigDecimal uncrHorasVirtual) {
		this.uncrHorasVirtual = uncrHorasVirtual;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadCurricular")
	public Set<Metodologia> getMetodologias() {
		return this.metodologias;
	}

	public void setMetodologias(Set<Metodologia> metodologias) {
		this.metodologias = metodologias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadCurricular")
	public Set<RecursosDidacticos> getRecursosDidacticoses() {
		return this.recursosDidacticoses;
	}

	public void setRecursosDidacticoses(Set<RecursosDidacticos> recursosDidacticoses) {
		this.recursosDidacticoses = recursosDidacticoses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadCurricular")
	public Set<Contenido> getContenidos() {
		return this.contenidos;
	}

	public void setContenidos(Set<Contenido> contenidos) {
		this.contenidos = contenidos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadCurricular")
	public Set<Bibliografia> getBibliografias() {
		return this.bibliografias;
	}

	public void setBibliografias(Set<Bibliografia> bibliografias) {
		this.bibliografias = bibliografias;
	}

}
