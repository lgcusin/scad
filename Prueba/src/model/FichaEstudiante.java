package model;
// Generated 23/02/2019 19:40:29 by Hibernate Tools 4.3.5.Final

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
 * FichaEstudiante generated by hbm2java
 */
@Entity
@Table(name = "FICHA_ESTUDIANTE")
public class FichaEstudiante implements java.io.Serializable {

	private BigDecimal fcesId;
	private String fcesPrimerNombre;
	private String fcesSegundoNombre;
	private String fcesApellidos;
	private Set<HorarioFichaEstudiante> horarioFichaEstudiantes = new HashSet<HorarioFichaEstudiante>(0);
	private Set<FichaMatricula> fichaMatriculas = new HashSet<FichaMatricula>(0);

	public FichaEstudiante() {
	}

	public FichaEstudiante(BigDecimal fcesId) {
		this.fcesId = fcesId;
	}

	public FichaEstudiante(BigDecimal fcesId, String fcesPrimerNombre, String fcesSegundoNombre, String fcesApellidos,
			Set<HorarioFichaEstudiante> horarioFichaEstudiantes, Set<FichaMatricula> fichaMatriculas) {
		this.fcesId = fcesId;
		this.fcesPrimerNombre = fcesPrimerNombre;
		this.fcesSegundoNombre = fcesSegundoNombre;
		this.fcesApellidos = fcesApellidos;
		this.horarioFichaEstudiantes = horarioFichaEstudiantes;
		this.fichaMatriculas = fichaMatriculas;
	}

	@Id

	@Column(name = "FCES_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getFcesId() {
		return this.fcesId;
	}

	public void setFcesId(BigDecimal fcesId) {
		this.fcesId = fcesId;
	}

	@Column(name = "FCES_PRIMER_NOMBRE", length = 30)
	public String getFcesPrimerNombre() {
		return this.fcesPrimerNombre;
	}

	public void setFcesPrimerNombre(String fcesPrimerNombre) {
		this.fcesPrimerNombre = fcesPrimerNombre;
	}

	@Column(name = "FCES_SEGUNDO_NOMBRE", length = 30)
	public String getFcesSegundoNombre() {
		return this.fcesSegundoNombre;
	}

	public void setFcesSegundoNombre(String fcesSegundoNombre) {
		this.fcesSegundoNombre = fcesSegundoNombre;
	}

	@Column(name = "FCES_APELLIDOS", length = 100)
	public String getFcesApellidos() {
		return this.fcesApellidos;
	}

	public void setFcesApellidos(String fcesApellidos) {
		this.fcesApellidos = fcesApellidos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaEstudiante")
	public Set<HorarioFichaEstudiante> getHorarioFichaEstudiantes() {
		return this.horarioFichaEstudiantes;
	}

	public void setHorarioFichaEstudiantes(Set<HorarioFichaEstudiante> horarioFichaEstudiantes) {
		this.horarioFichaEstudiantes = horarioFichaEstudiantes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaEstudiante")
	public Set<FichaMatricula> getFichaMatriculas() {
		return this.fichaMatriculas;
	}

	public void setFichaMatriculas(Set<FichaMatricula> fichaMatriculas) {
		this.fichaMatriculas = fichaMatriculas;
	}

}
