package ec.edu.uce.academico.jpa.entidades.publico;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "USUARIO_ROL", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name="UsuarioRol.findAll", query="SELECT m FROM UsuarioRol m"),
				@NamedQuery(name = "UsuarioRol.findPorIdentificacionRol", query = "SELECT m FROM UsuarioRol m WHERE m.usroUsuario.usrIdentificacion = :usrIdentificacion and m.usroRol.rolId = :rolId") })
public class UsuarioRol implements java.io.Serializable {

	private static final long serialVersionUID = 1304699661710992755L;
	private int usroId;
	private Rol usroRol;
	private Usuario usroUsuario;
	private Integer usroEstado;
	private List<Turnero> usroTurneros = new ArrayList<Turnero>(0);
	private List<RolFlujoCarrera> rolFlujoCarreras = new ArrayList<RolFlujoCarrera>(0);
	private List<FichaInscripcion> rolFichaInscripciones = new ArrayList<FichaInscripcion>(0);
	private List<ControlProceso> rolControlProcesos = new ArrayList<ControlProceso>(0);
	private List<EvaluadorEvaluado> usroListEvaluadorEvaluado;

	public UsuarioRol() {
	}

	public UsuarioRol(int usroId) {
		this.usroId = usroId;
	}

	public UsuarioRol(int usroId, Rol usroRol, Usuario usroUsuario, Integer usroEstado, List<Turnero> usroTurneros,
			List<RolFlujoCarrera> rolFlujoCarreras, List<FichaInscripcion> rolFichaInscripciones,
			List<ControlProceso> rolControlProcesos) {
		this.usroId = usroId;
		this.usroRol = usroRol;
		this.usroUsuario = usroUsuario;
		this.usroEstado = usroEstado;
		this.usroTurneros = usroTurneros;
		this.rolFlujoCarreras = rolFlujoCarreras;
		this.rolFichaInscripciones = rolFichaInscripciones;
		this.rolControlProcesos = rolControlProcesos;

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "USRO_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getUsroId() {
		return this.usroId;
	}

	public void setUsroId(int usroId) {
		this.usroId = usroId;
	}

	@ManyToOne
	@JoinColumn(name = "ROL_ID")
	public Rol getUsroRol() {
		return this.usroRol;
	}

	public void setUsroRol(Rol usroRol) {
		this.usroRol = usroRol;
	}

	@ManyToOne
	@JoinColumn(name = "USR_ID")
	public Usuario getUsroUsuario() {
		return this.usroUsuario;
	}

	public void setUsroUsuario(Usuario usroUsuario) {
		this.usroUsuario = usroUsuario;
	}

	@Column(name = "USRO_ESTADO", precision = 22, scale = 0)
	public Integer getUsroEstado() {
		return this.usroEstado;
	}

	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}

	@OneToMany(mappedBy = "trnUsuarioRol")
	public List<Turnero> getUsroTurneros() {
		return this.usroTurneros;
	}

	public void setUsroTurneros(List<Turnero> usroTurneros) {
		this.usroTurneros = usroTurneros;
	}

	@OneToMany(mappedBy = "roflcrUsuarioRol")
	public List<RolFlujoCarrera> getRolFlujoCarreras() {
		return rolFlujoCarreras;
	}

	public void setRolFlujoCarreras(List<RolFlujoCarrera> rolFlujoCarreras) {
		this.rolFlujoCarreras = rolFlujoCarreras;
	}

	@OneToMany(mappedBy = "cnprUsuarioRol")
	public List<ControlProceso> getRolControlProcesos() {
		return rolControlProcesos;
	}

	public void setRolControlProcesos(List<ControlProceso> rolControlProcesos) {
		this.rolControlProcesos = rolControlProcesos;
	}

	@OneToMany(mappedBy = "fcinUsuarioRol")
	public List<FichaInscripcion> getRolFichaInscripciones() {
		return rolFichaInscripciones;
	}

	public void setRolFichaInscripciones(List<FichaInscripcion> rolFichaInscripciones) {
		this.rolFichaInscripciones = rolFichaInscripciones;
	}

	// bi-directional many-to-one association to EvaluadorEvaluado
	@OneToMany(mappedBy = "evevUsuarioRol")
	public List<EvaluadorEvaluado> getUsroListEvaluadorEvaluado() {
		return this.usroListEvaluadorEvaluado;
	}

	public void setUsroListEvaluadorEvaluado(List<EvaluadorEvaluado> usroListEvaluadorEvaluado) {
		this.usroListEvaluadorEvaluado = usroListEvaluadorEvaluado;
	}

	public EvaluadorEvaluado addUsroListEvaluadorEvaluado(EvaluadorEvaluado usroListEvaluadorEvaluado) {
		getUsroListEvaluadorEvaluado().add(usroListEvaluadorEvaluado);
		usroListEvaluadorEvaluado.setEvevUsuarioRol(this);

		return usroListEvaluadorEvaluado;
	}

	public EvaluadorEvaluado removeUsroListEvaluadorEvaluado(EvaluadorEvaluado usroListEvaluadorEvaluado) {
		getUsroListEvaluadorEvaluado().remove(usroListEvaluadorEvaluado);
		usroListEvaluadorEvaluado.setEvevUsuarioRol(null);

		return usroListEvaluadorEvaluado;
	}

}
