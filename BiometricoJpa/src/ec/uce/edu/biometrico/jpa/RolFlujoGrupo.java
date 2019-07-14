package ec.uce.edu.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * RolFlujoGrupo generated by hbm2java
 */
@Entity
@Table(name = "ROL_FLUJO_GRUPO")
public class RolFlujoGrupo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer roflgrId;
	private PeriodoAcademico periodoAcademico;
	private Grupo grupo;
	private UsuarioRol usuarioRol;
	private Integer roflgrEstado;

	public RolFlujoGrupo() {
	}

	public RolFlujoGrupo(Integer roflgrId, PeriodoAcademico periodoAcademico, Grupo grupo, UsuarioRol usuarioRol) {
		this.roflgrId = roflgrId;
		this.periodoAcademico = periodoAcademico;
		this.grupo = grupo;
		this.usuarioRol = usuarioRol;
	}

	public RolFlujoGrupo(Integer roflgrId, PeriodoAcademico periodoAcademico, Grupo grupo, UsuarioRol usuarioRol,
			Integer roflgrEstado) {
		this.roflgrId = roflgrId;
		this.periodoAcademico = periodoAcademico;
		this.grupo = grupo;
		this.usuarioRol = usuarioRol;
		this.roflgrEstado = roflgrEstado;
	}

	@Id

	@Column(name = "ROFLGR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getRoflgrId() {
		return this.roflgrId;
	}

	public void setRoflgrId(Integer roflgrId) {
		this.roflgrId = roflgrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRAC_ID", nullable = false)
	public PeriodoAcademico getPeriodoAcademico() {
		return this.periodoAcademico;
	}

	public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
		this.periodoAcademico = periodoAcademico;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRP_ID", nullable = false)
	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USRO_ID", nullable = false)
	public UsuarioRol getUsuarioRol() {
		return this.usuarioRol;
	}

	public void setUsuarioRol(UsuarioRol usuarioRol) {
		this.usuarioRol = usuarioRol;
	}

	@Column(name = "ROFLGR_ESTADO", precision = 22, scale = 0)
	public Integer getRoflgrEstado() {
		return this.roflgrEstado;
	}

	public void setRoflgrEstado(Integer roflgrEstado) {
		this.roflgrEstado = roflgrEstado;
	}

}
