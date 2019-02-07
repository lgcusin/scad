package model;
// Generated 29/01/2019 23:19:04 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Horario generated by hbm2java
 */
@Entity
@Table(name = "HORARIO")
public class Horario implements java.io.Serializable {

	private BigDecimal hrrId;
	private TipoHorario tipoHorario;
	private Aula aula;
	private DiaSemana diaSemana;
	private FichaEstudiante fichaEstudiante;
	private FichaDocente fichaDocente;
	private Materia materia;
	private String hrrInicio;
	private String hrrFin;

	public Horario() {
	}

	public Horario(BigDecimal hrrId, TipoHorario tipoHorario, Aula aula, DiaSemana diaSemana,
			FichaEstudiante fichaEstudiante, FichaDocente fichaDocente, Materia materia) {
		this.hrrId = hrrId;
		this.tipoHorario = tipoHorario;
		this.aula = aula;
		this.diaSemana = diaSemana;
		this.fichaEstudiante = fichaEstudiante;
		this.fichaDocente = fichaDocente;
		this.materia = materia;
	}

	public Horario(BigDecimal hrrId, TipoHorario tipoHorario, Aula aula, DiaSemana diaSemana,
			FichaEstudiante fichaEstudiante, FichaDocente fichaDocente, Materia materia, String hrrInicio,
			String hrrFin) {
		this.hrrId = hrrId;
		this.tipoHorario = tipoHorario;
		this.aula = aula;
		this.diaSemana = diaSemana;
		this.fichaEstudiante = fichaEstudiante;
		this.fichaDocente = fichaDocente;
		this.materia = materia;
		this.hrrInicio = hrrInicio;
		this.hrrFin = hrrFin;
	}

	@Id

	@Column(name = "HRR_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getHrrId() {
		return this.hrrId;
	}

	public void setHrrId(BigDecimal hrrId) {
		this.hrrId = hrrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TPHR_ID", nullable = false)
	public TipoHorario getTipoHorario() {
		return this.tipoHorario;
	}

	public void setTipoHorario(TipoHorario tipoHorario) {
		this.tipoHorario = tipoHorario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUL_ID", nullable = false)
	public Aula getAula() {
		return this.aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DSM_ID", nullable = false)
	public DiaSemana getDiaSemana() {
		return this.diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCES_ID", nullable = false)
	public FichaEstudiante getFichaEstudiante() {
		return this.fichaEstudiante;
	}

	public void setFichaEstudiante(FichaEstudiante fichaEstudiante) {
		this.fichaEstudiante = fichaEstudiante;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCDC_ID", nullable = false)
	public FichaDocente getFichaDocente() {
		return this.fichaDocente;
	}

	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MTR_ID", nullable = false)
	public Materia getMateria() {
		return this.materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	@Column(name = "HRR_INICIO", length = 10)
	public String getHrrInicio() {
		return this.hrrInicio;
	}

	public void setHrrInicio(String hrrInicio) {
		this.hrrInicio = hrrInicio;
	}

	@Column(name = "HRR_FIN", length = 10)
	public String getHrrFin() {
		return this.hrrFin;
	}

	public void setHrrFin(String hrrFin) {
		this.hrrFin = hrrFin;
	}

}
