package ec.edu.uce.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * RolFlujoCarrera generated by hbm2java
 */
@Entity
@Table(name = "ROL_FLUJO_CARRERA")
public class RolFlujoCarrera implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6297443755611335714L;
	private Integer roflcrId;
	private UsuarioRol roflcrUsuarioRol;
	private Carrera roflcrCarrera;
	private Integer roflcrEstado;

	public RolFlujoCarrera() {
	}

	public RolFlujoCarrera(Integer roflcrId, UsuarioRol usuarioRol, Carrera carrera) {
		this.roflcrId = roflcrId;
		this.roflcrUsuarioRol = usuarioRol;
		this.roflcrCarrera = carrera;
	}

	public RolFlujoCarrera(Integer roflcrId, UsuarioRol usuarioRol, Carrera carrera, Integer roflcrEstado) {
		this.roflcrId = roflcrId;
		this.roflcrUsuarioRol = usuarioRol;
		this.roflcrCarrera = carrera;
		this.roflcrEstado = roflcrEstado;
	}

	@Id

	@Column(name = "ROFLCR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getRoflcrId() {
		return this.roflcrId;
	}

	public void setRoflcrId(Integer roflcrId) {
		this.roflcrId = roflcrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USRO_ID", nullable = false)
	public UsuarioRol getRoflcrUsuarioRol() {
		return this.roflcrUsuarioRol;
	}

	public void setRoflcrUsuarioRol(UsuarioRol usuarioRol) {
		this.roflcrUsuarioRol = usuarioRol;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID", nullable = false)
	public Carrera getRoflcrCarrera() {
		return this.roflcrCarrera;
	}

	public void setRoflcrCarrera(Carrera carrera) {
		this.roflcrCarrera = carrera;
	}

	@Column(name = "ROFLCR_ESTADO", precision = 22, scale = 0)
	public Integer getRoflcrEstado() {
		return this.roflcrEstado;
	}

	public void setRoflcrEstado(Integer roflcrEstado) {
		this.roflcrEstado = roflcrEstado;
	}

}