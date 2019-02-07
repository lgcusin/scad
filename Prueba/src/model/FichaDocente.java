package model;
// Generated 06/02/2019 19:25:50 by Hibernate Tools 4.3.5.Final

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
 * FichaDocente generated by hbm2java
 */
@Entity
@Table(name = "FICHA_DOCENTE")
public class FichaDocente implements java.io.Serializable {

	private BigDecimal fcdcId;
	private BigDecimal fcdcIdentificacion;
	private String fcdcPrimerNombre;
	private String fcdcSegundoNombre;
	private String fcdcApellidos;
	private Set<HuellaDactilar> huellaDactilars = new HashSet<HuellaDactilar>(0);
	private Set<DetallePuesto> detallePuestos = new HashSet<DetallePuesto>(0);
	private Set<Asistencia> asistencias = new HashSet<Asistencia>(0);
	private Set<Horario> horarios = new HashSet<Horario>(0);
	private Set<Usuario> usuarios = new HashSet<Usuario>(0);

	public FichaDocente() {
	}

	public FichaDocente(BigDecimal fcdcId) {
		this.fcdcId = fcdcId;
	}

	public FichaDocente(BigDecimal fcdcId, BigDecimal fcdcIdentificacion, String fcdcPrimerNombre,
			String fcdcSegundoNombre, String fcdcApellidos, Set<HuellaDactilar> huellaDactilars,
			Set<DetallePuesto> detallePuestos, Set<Asistencia> asistencias, Set<Horario> horarios,
			Set<Usuario> usuarios) {
		this.fcdcId = fcdcId;
		this.fcdcIdentificacion = fcdcIdentificacion;
		this.fcdcPrimerNombre = fcdcPrimerNombre;
		this.fcdcSegundoNombre = fcdcSegundoNombre;
		this.fcdcApellidos = fcdcApellidos;
		this.huellaDactilars = huellaDactilars;
		this.detallePuestos = detallePuestos;
		this.asistencias = asistencias;
		this.horarios = horarios;
		this.usuarios = usuarios;
	}

	@Id

	@Column(name = "FCDC_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getFcdcId() {
		return this.fcdcId;
	}

	public void setFcdcId(BigDecimal fcdcId) {
		this.fcdcId = fcdcId;
	}

	@Column(name = "FCDC_IDENTIFICACION", precision = 22, scale = 0)
	public BigDecimal getFcdcIdentificacion() {
		return this.fcdcIdentificacion;
	}

	public void setFcdcIdentificacion(BigDecimal fcdcIdentificacion) {
		this.fcdcIdentificacion = fcdcIdentificacion;
	}

	@Column(name = "FCDC_PRIMER_NOMBRE", length = 200)
	public String getFcdcPrimerNombre() {
		return this.fcdcPrimerNombre;
	}

	public void setFcdcPrimerNombre(String fcdcPrimerNombre) {
		this.fcdcPrimerNombre = fcdcPrimerNombre;
	}

	@Column(name = "FCDC_SEGUNDO_NOMBRE", length = 200)
	public String getFcdcSegundoNombre() {
		return this.fcdcSegundoNombre;
	}

	public void setFcdcSegundoNombre(String fcdcSegundoNombre) {
		this.fcdcSegundoNombre = fcdcSegundoNombre;
	}

	@Column(name = "FCDC_APELLIDOS", length = 400)
	public String getFcdcApellidos() {
		return this.fcdcApellidos;
	}

	public void setFcdcApellidos(String fcdcApellidos) {
		this.fcdcApellidos = fcdcApellidos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaDocente")
	public Set<HuellaDactilar> getHuellaDactilars() {
		return this.huellaDactilars;
	}

	public void setHuellaDactilars(Set<HuellaDactilar> huellaDactilars) {
		this.huellaDactilars = huellaDactilars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaDocente")
	public Set<DetallePuesto> getDetallePuestos() {
		return this.detallePuestos;
	}

	public void setDetallePuestos(Set<DetallePuesto> detallePuestos) {
		this.detallePuestos = detallePuestos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaDocente")
	public Set<Asistencia> getAsistencias() {
		return this.asistencias;
	}

	public void setAsistencias(Set<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaDocente")
	public Set<Horario> getHorarios() {
		return this.horarios;
	}

	public void setHorarios(Set<Horario> horarios) {
		this.horarios = horarios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaDocente")
	public Set<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}