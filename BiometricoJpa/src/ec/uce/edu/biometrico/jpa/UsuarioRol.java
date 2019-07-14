package ec.uce.edu.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.List;
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
 * UsuarioRol generated by hbm2java
 */
@Entity
@Table(name = "USUARIO_ROL", uniqueConstraints = @UniqueConstraint(columnNames = { "USR_ID", "ROL_ID" }))
public class UsuarioRol implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer usroId;
	private Rol rol;
	private Usuario usuario;
	private Integer usroEstado;
	private List<RolFlujoCarrera> rolFlujoCarreras = new ArrayList<RolFlujoCarrera>(0);
	private List<RolFlujoGrupo> rolFlujoGrupos = new ArrayList<RolFlujoGrupo>(0);

	public UsuarioRol() {
	}

	public UsuarioRol(Integer usroId, Rol rol, Usuario usuario) {
		this.usroId = usroId;
		this.rol = rol;
		this.usuario = usuario;
	}

	public UsuarioRol(Integer usroId, Rol rol, Usuario usuario, Integer usroEstado,
			List<RolFlujoCarrera> rolFlujoCarreras, List<RolFlujoGrupo> rolFlujoGrupos) {
		this.usroId = usroId;
		this.rol = rol;
		this.usuario = usuario;
		this.usroEstado = usroEstado;
		this.rolFlujoCarreras = rolFlujoCarreras;
		this.rolFlujoGrupos = rolFlujoGrupos;
	}

	@Id

	@Column(name = "USRO_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getUsroId() {
		return this.usroId;
	}

	public void setUsroId(Integer usroId) {
		this.usroId = usroId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROL_ID", nullable = false)
	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USR_ID", nullable = false)
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "USRO_ESTADO", precision = 22, scale = 0)
	public Integer getUsroEstado() {
		return this.usroEstado;
	}

	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioRol")
	public List<RolFlujoCarrera> getRolFlujoCarreras() {
		return this.rolFlujoCarreras;
	}

	public void setRolFlujoCarreras(List<RolFlujoCarrera> rolFlujoCarreras) {
		this.rolFlujoCarreras = rolFlujoCarreras;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioRol")
	public List<RolFlujoGrupo> getRolFlujoGrupos() {
		return this.rolFlujoGrupos;
	}

	public void setRolFlujoGrupos(List<RolFlujoGrupo> rolFlujoGrupos) {
		this.rolFlujoGrupos = rolFlujoGrupos;
	}


}
