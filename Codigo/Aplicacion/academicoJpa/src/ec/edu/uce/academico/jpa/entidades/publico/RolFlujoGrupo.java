package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * The persistent class for the ROL_FLUJO_GRUPO database table.
 * 
 */
@Entity
@Table(name = "ROL_FLUJO_GRUPO", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "RolFlujoGrupo.findAll", query="SELECT r FROM RolFlujoGrupo r"),
				@NamedQuery(name = "RolFlujoGrupo.findPorUsroGrupoPeriodo", query = "SELECT r FROM RolFlujoGrupo r WHERE r.roflgrUsuarioRol.usroId = :usroId and r.roflgrGrupo.grpId = :grpId and r.roflgrPeriodoAcademico.pracId = :pracId"),
				@NamedQuery(name = "RolFlujoGrupo.findPorRolGrupoPeriodo", query = "SELECT r FROM RolFlujoGrupo r WHERE r.roflgrGrupo.grpId = :grpId and r.roflgrPeriodoAcademico.pracId = :pracId and r.roflgrUsuarioRol.usroRol.rolId = :rolId"),
				@NamedQuery(name = "RolFlujoGrupo.findPorUsroPeriodoNotInGrupo", query = "SELECT r FROM RolFlujoGrupo r WHERE r.roflgrGrupo.grpId <> :grpId and r.roflgrPeriodoAcademico.pracId = :pracId and r.roflgrUsuarioRol.usroId = :usroId")})
public class RolFlujoGrupo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int roflgrId;
	private Integer roflgrEstado;
	private Grupo roflgrGrupo;
	private UsuarioRol roflgrUsuarioRol;
	private PeriodoAcademico roflgrPeriodoAcademico;
	
	public RolFlujoGrupo() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ROFLGR_ID", unique=true, nullable=false, precision=6)
	public int getRoflgrId() {
		return this.roflgrId;
	}

	public void setRoflgrId(int roflgrId) {
		this.roflgrId = roflgrId;
	}


	@Column(name="ROFLGR_ESTADO", precision=38)
	public Integer getRoflgrEstado() {
		return this.roflgrEstado;
	}

	public void setRoflgrEstado(Integer roflgrEstado) {
		this.roflgrEstado = roflgrEstado;
	}


	//bi-directional many-to-one association to Grupo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GRP_ID", nullable=false)
	public Grupo getRoflgrGrupo() {
		return this.roflgrGrupo;
	}

	public void setRoflgrGrupo(Grupo roflgrGrupo) {
		this.roflgrGrupo = roflgrGrupo;
	}


	//bi-directional many-to-one association to UsuarioRol
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="USRO_ID", nullable=false)
	public UsuarioRol getRoflgrUsuarioRol() {
		return this.roflgrUsuarioRol;
	}

	public void setRoflgrUsuarioRol(UsuarioRol roflgrUsuarioRol) {
		this.roflgrUsuarioRol = roflgrUsuarioRol;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRAC_ID", nullable=false)
	public PeriodoAcademico getRoflgrPeriodoAcademico() {
		return roflgrPeriodoAcademico;
	}


	public void setRoflgrPeriodoAcademico(PeriodoAcademico roflgrPeriodoAcademico) {
		this.roflgrPeriodoAcademico = roflgrPeriodoAcademico;
	}
	
	

}