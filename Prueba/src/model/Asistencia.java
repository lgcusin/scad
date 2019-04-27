package model;
// Generated 15/04/2019 20:41:59 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Asistencia generated by hbm2java
 */
@Entity
@Table(name = "ASISTENCIA")
public class Asistencia implements java.io.Serializable {

	private BigDecimal assId;
	private Horario horario;
	private FichaDocente fichaDocente;
	private Date assFecha;
	private String assHoraEntrada;
	private String assHoraSalida;
	private String assEstado;
	private Set<Seguimiento> seguimientos = new HashSet<Seguimiento>(0);

	public Asistencia() {
	}

	public Asistencia(BigDecimal assId, Horario horario, FichaDocente fichaDocente) {
		this.assId = assId;
		this.horario = horario;
		this.fichaDocente = fichaDocente;
	}

	public Asistencia(BigDecimal assId, Horario horario, FichaDocente fichaDocente, Date assFecha,
			String assHoraEntrada, String assHoraSalida, String assEstado, Set<Seguimiento> seguimientos) {
		this.assId = assId;
		this.horario = horario;
		this.fichaDocente = fichaDocente;
		this.assFecha = assFecha;
		this.assHoraEntrada = assHoraEntrada;
		this.assHoraSalida = assHoraSalida;
		this.assEstado = assEstado;
		this.seguimientos = seguimientos;
	}

	@Id

	@Column(name = "ASS_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getAssId() {
		return this.assId;
	}

	public void setAssId(BigDecimal assId) {
		this.assId = assId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HRR_ID", nullable = false)
	public Horario getHorario() {
		return this.horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCDC_ID", nullable = false)
	public FichaDocente getFichaDocente() {
		return this.fichaDocente;
	}

	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ASS_FECHA", length = 7)
	public Date getAssFecha() {
		return this.assFecha;
	}

	public void setAssFecha(Date assFecha) {
		this.assFecha = assFecha;
	}

	@Column(name = "ASS_HORA_ENTRADA", length = 8)
	public String getAssHoraEntrada() {
		return this.assHoraEntrada;
	}

	public void setAssHoraEntrada(String assHoraEntrada) {
		this.assHoraEntrada = assHoraEntrada;
	}

	@Column(name = "ASS_HORA_SALIDA", length = 8)
	public String getAssHoraSalida() {
		return this.assHoraSalida;
	}

	public void setAssHoraSalida(String assHoraSalida) {
		this.assHoraSalida = assHoraSalida;
	}

	@Column(name = "ASS_ESTADO", length = 20)
	public String getAssEstado() {
		return this.assEstado;
	}

	public void setAssEstado(String assEstado) {
		this.assEstado = assEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencia")
	public Set<Seguimiento> getSeguimientos() {
		return this.seguimientos;
	}

	public void setSeguimientos(Set<Seguimiento> seguimientos) {
		this.seguimientos = seguimientos;
	}

}
