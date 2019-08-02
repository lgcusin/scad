package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the GRUPO database table.
 * 
 */
@Entity
@Table(name = "GRUPO")
@NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int grpId;
	private String grpCodigo;
	private String grpDescripcion;
	private Integer grpEstado;
	private Carrera grpCarrera;
	private List<Materia> grpListMateria;
	private List<RolFlujoGrupo> grpListRolFlujoGrupo;

	public Grupo() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GRP_ID", unique = true, nullable = false, precision = 6)
	public int getGrpId() {
		return this.grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	@Column(name = "GRP_CODIGO", length = 256)
	public String getGrpCodigo() {
		return this.grpCodigo;
	}

	public void setGrpCodigo(String grpCodigo) {
		this.grpCodigo = grpCodigo;
	}

	@Column(name = "GRP_DESCRIPCION", length = 1024)
	public String getGrpDescripcion() {
		return this.grpDescripcion;
	}

	public void setGrpDescripcion(String grpDescripcion) {
		this.grpDescripcion = grpDescripcion;
	}

	@Column(name = "GRP_ESTADO", precision = 38)
	public Integer getGrpEstado() {
		return this.grpEstado;
	}

	public void setGrpEstado(Integer grpEstado) {
		this.grpEstado = grpEstado;
	}

	// bi-directional many-to-one association to Materia
	@OneToMany(mappedBy = "mtrGrupo")
	public List<Materia> getGrpListMateria() {
		return this.grpListMateria;
	}

	public void setGrpListMateria(List<Materia> grpListMateria) {
		this.grpListMateria = grpListMateria;
	}

	public Materia addGrpListMateria(Materia grpListMateria) {
		getGrpListMateria().add(grpListMateria);
		grpListMateria.setMtrGrupo(this);

		return grpListMateria;
	}

	public Materia removeGrpListMateria(Materia grpListMateria) {
		getGrpListMateria().remove(grpListMateria);
		grpListMateria.setMtrGrupo(null);

		return grpListMateria;
	}

	// bi-directional many-to-one association to RolFlujoGrupo
	@OneToMany(mappedBy = "roflgrGrupo")
	public List<RolFlujoGrupo> getGrpListRolFlujoGrupo() {
		return this.grpListRolFlujoGrupo;
	}

	public void setGrpListRolFlujoGrupo(List<RolFlujoGrupo> grpListRolFlujoGrupo) {
		this.grpListRolFlujoGrupo = grpListRolFlujoGrupo;
	}

	public RolFlujoGrupo addGrpListRolFlujoGrupo(RolFlujoGrupo grpListRolFlujoGrupo) {
		getGrpListRolFlujoGrupo().add(grpListRolFlujoGrupo);
		grpListRolFlujoGrupo.setRoflgrGrupo(this);

		return grpListRolFlujoGrupo;
	}

	public RolFlujoGrupo removeGrpListRolFlujoGrupo(RolFlujoGrupo grpListRolFlujoGrupo) {
		getGrpListRolFlujoGrupo().remove(grpListRolFlujoGrupo);
		grpListRolFlujoGrupo.setRoflgrGrupo(null);

		return grpListRolFlujoGrupo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GRP_CRR_ID")
	public Carrera getGrpCarrera() {
		return grpCarrera;
	}

	public void setGrpCarrera(Carrera grpCarrera) {
		this.grpCarrera = grpCarrera;
	}
	
	

}