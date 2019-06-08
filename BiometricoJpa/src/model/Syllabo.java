package model;
// Generated 29/01/2019 23:19:04 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Syllabo generated by hbm2java
 */
@Entity
@Table(name = "SYLLABO", uniqueConstraints = @UniqueConstraint(columnNames = "MCM_ID"))
@NamedQuery(name = "Syllabo.findByMtrId", query = "select s from Syllabo as s where s.mallaCurricularMateria.materia.mtrId=:mtrId")
public class Syllabo implements java.io.Serializable {

	private Integer sylId;
	private MallaCurricularMateria mallaCurricularMateria;
	private String sylDescripcion;
	private Integer sylHorasClase;
	private Integer sylHorasTutorias;
	private String sylObjetivoGnrl;
	private String sylObjetivoEspc;
	private String sylContribucionProfesional;
	private String sylResultadosAprendizaje;
	private List<UnidadCurricular> unidadCurriculars = new ArrayList<>();

	public Syllabo() {
	}

	public Syllabo(Integer sylId, MallaCurricularMateria mallaCurricularMateria) {
		this.sylId = sylId;
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	public Syllabo(Integer sylId, MallaCurricularMateria mallaCurricularMateria, String sylDescripcion,
			Integer sylHorasClase, Integer sylHorasTutorias, String sylObjetivoGnrl, String sylObjetivoEspc,
			String sylContribucionProfesional, String sylResultadosAprendizaje,
			List<UnidadCurricular> unidadCurriculars) {
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
	public Integer getSylId() {
		return this.sylId;
	}

	public void setSylId(Integer sylId) {
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
	public Integer getSylHorasClase() {
		return this.sylHorasClase;
	}

	public void setSylHorasClase(Integer sylHorasClase) {
		this.sylHorasClase = sylHorasClase;
	}

	@Column(name = "SYL_HORAS_TUTORIAS", precision = 22, scale = 0)
	public Integer getSylHorasTutorias() {
		return this.sylHorasTutorias;
	}

	public void setSylHorasTutorias(Integer sylHorasTutorias) {
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
	public List<UnidadCurricular> getUnidadCurriculars() {
		return this.unidadCurriculars;
	}

	public void setUnidadCurriculars(List<UnidadCurricular> unidadCurriculars) {
		this.unidadCurriculars = unidadCurriculars;
	}

}
