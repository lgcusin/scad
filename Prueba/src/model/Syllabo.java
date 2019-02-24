package model;
// Generated 14/02/2019 12:12:32 by Hibernate Tools 4.3.5.Final

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
import javax.persistence.UniqueConstraint;

/**
 * Syllabo generated by hbm2java
 */
@Entity
@Table(name = "SYLLABO", uniqueConstraints = @UniqueConstraint(columnNames = "MCM_ID"))
public class Syllabo implements java.io.Serializable {

	private BigDecimal sylId;
	private MallaCurricularMateria mallaCurricularMateria;
	private String sylDescripcion;
	private BigDecimal sylHorasClase;
	private BigDecimal sylHorasTutorias;
	private String sylObjetivoGnrl;
	private String sylObjetivoEspc;
	private String sylContribucionProfesional;
	private String sylResultadosAprendizaje;
	private Set<UnidadCurricular> unidadCurriculars = new HashSet<UnidadCurricular>(0);

	public Syllabo() {
	}

	public Syllabo(BigDecimal sylId, MallaCurricularMateria mallaCurricularMateria) {
		this.sylId = sylId;
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	public Syllabo(BigDecimal sylId, MallaCurricularMateria mallaCurricularMateria, String sylDescripcion,
			BigDecimal sylHorasClase, BigDecimal sylHorasTutorias, String sylObjetivoGnrl, String sylObjetivoEspc,
			String sylContribucionProfesional, String sylResultadosAprendizaje,
			Set<UnidadCurricular> unidadCurriculars) {
		this.sylId = sylId;
		this.mallaCurricularMateria = mallaCurricularMateria;
		this.sylDescripcion = sylDescripcion;
		this.sylHorasClase = sylHorasClase;
		this.sylHorasTutorias = sylHorasTutorias;
		this.sylObjetivoGnrl = sylObjetivoGnrl;
		this.sylObjetivoEspc = sylObjetivoEspc;
		this.sylContribucionProfesional = sylContribucionProfesional;
		this.sylResultadosAprendizaje = sylResultadosAprendizaje;
		this.unidadCurriculars = unidadCurriculars;
	}

	@Id

	@Column(name = "SYL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getSylId() {
		return this.sylId;
	}

	public void setSylId(BigDecimal sylId) {
		this.sylId = sylId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MCM_ID", unique = true, nullable = false)
	public MallaCurricularMateria getMallaCurricularMateria() {
		return this.mallaCurricularMateria;
	}

	public void setMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	@Column(name = "SYL_DESCRIPCION", length = 700)
	public String getSylDescripcion() {
		return this.sylDescripcion;
	}

	public void setSylDescripcion(String sylDescripcion) {
		this.sylDescripcion = sylDescripcion;
	}

	@Column(name = "SYL_HORAS_CLASE", precision = 22, scale = 0)
	public BigDecimal getSylHorasClase() {
		return this.sylHorasClase;
	}

	public void setSylHorasClase(BigDecimal sylHorasClase) {
		this.sylHorasClase = sylHorasClase;
	}

	@Column(name = "SYL_HORAS_TUTORIAS", precision = 22, scale = 0)
	public BigDecimal getSylHorasTutorias() {
		return this.sylHorasTutorias;
	}

	public void setSylHorasTutorias(BigDecimal sylHorasTutorias) {
		this.sylHorasTutorias = sylHorasTutorias;
	}

	@Column(name = "SYL_OBJETIVO_GNRL", length = 700)
	public String getSylObjetivoGnrl() {
		return this.sylObjetivoGnrl;
	}

	public void setSylObjetivoGnrl(String sylObjetivoGnrl) {
		this.sylObjetivoGnrl = sylObjetivoGnrl;
	}

	@Column(name = "SYL_OBJETIVO_ESPC", length = 700)
	public String getSylObjetivoEspc() {
		return this.sylObjetivoEspc;
	}

	public void setSylObjetivoEspc(String sylObjetivoEspc) {
		this.sylObjetivoEspc = sylObjetivoEspc;
	}

	@Column(name = "SYL_CONTRIBUCION_PROFESIONAL", length = 700)
	public String getSylContribucionProfesional() {
		return this.sylContribucionProfesional;
	}

	public void setSylContribucionProfesional(String sylContribucionProfesional) {
		this.sylContribucionProfesional = sylContribucionProfesional;
	}

	@Column(name = "SYL_RESULTADOS_APRENDIZAJE", length = 700)
	public String getSylResultadosAprendizaje() {
		return this.sylResultadosAprendizaje;
	}

	public void setSylResultadosAprendizaje(String sylResultadosAprendizaje) {
		this.sylResultadosAprendizaje = sylResultadosAprendizaje;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "syllabo")
	public Set<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(Set<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

}
