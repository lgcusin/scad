package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the EVALUADOR_EVALUADO database table.
 * 
 */
@Entity
@Table(name="EVALUADOR_EVALUADO")
@NamedQuery(name="EvaluadorEvaluado.findAll", query="SELECT e FROM EvaluadorEvaluado e")
public class EvaluadorEvaluado implements Serializable {
	private static final long serialVersionUID = 1L;
	private int evevId;
	private Integer evevEvaluador;
	private List<AsignacionEvaluador> evevListAsignacionEvaluador;
	private UsuarioRol evevUsuarioRol;

	public EvaluadorEvaluado() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="EVEV_ID", unique=true, nullable=false, precision=6)
	public int getEvevId() {
		return this.evevId;
	}

	public void setEvevId(int evevId) {
		this.evevId = evevId;
	}


	@Column(name="EVEV_EVALUADOR", precision=38)
	public Integer getEvevEvaluador() {
		return this.evevEvaluador;
	}

	public void setEvevEvaluador(Integer evevEvaluador) {
		this.evevEvaluador = evevEvaluador;
	}


	//bi-directional many-to-one association to AsignacionEvaluador
	@OneToMany(mappedBy="asevEvaluadorEvaluado")
	public List<AsignacionEvaluador> getEvevListAsignacionEvaluador() {
		return this.evevListAsignacionEvaluador;
	}

	public void setEvevListAsignacionEvaluador(List<AsignacionEvaluador> evevListAsignacionEvaluador) {
		this.evevListAsignacionEvaluador = evevListAsignacionEvaluador;
	}

	public AsignacionEvaluador addEvevListAsignacionEvaluador(AsignacionEvaluador evevListAsignacionEvaluador) {
		getEvevListAsignacionEvaluador().add(evevListAsignacionEvaluador);
		evevListAsignacionEvaluador.setAsevEvaluadorEvaluado(this);

		return evevListAsignacionEvaluador;
	}

	public AsignacionEvaluador removeEvevListAsignacionEvaluador(AsignacionEvaluador evevListAsignacionEvaluador) {
		getEvevListAsignacionEvaluador().remove(evevListAsignacionEvaluador);
		evevListAsignacionEvaluador.setAsevEvaluadorEvaluado(null);

		return evevListAsignacionEvaluador;
	}


	//bi-directional many-to-one association to UsuarioRol
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USRO_ID")
	public UsuarioRol getEvevUsuarioRol() {
		return this.evevUsuarioRol;
	}

	public void setEvevUsuarioRol(UsuarioRol evevUsuarioRol) {
		this.evevUsuarioRol = evevUsuarioRol;
	}

}