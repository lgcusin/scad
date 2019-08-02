package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the ASIGNACION_EVALUADOR database table.
 * 
 */
@Entity
@Table(name="ASIGNACION_EVALUADOR")
@NamedQuery(name="AsignacionEvaluador.findAll", query="SELECT a FROM AsignacionEvaluador a")
public class AsignacionEvaluador implements Serializable {
	private static final long serialVersionUID = 1L;
	private int asevId;
	private Integer asevEstado;
	private Timestamp asevFecha;
	private String asevUsuario;
	private Integer asevEvaluadorCrrId;
	private CargaHoraria asevCargaHoraria;
	private EvaluadorEvaluado asevEvaluadorEvaluado;
	private List<Contenido> asevListContenido;

	public AsignacionEvaluador() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ASEV_ID", unique=true, nullable=false, precision=6)
	public int getAsevId() {
		return this.asevId;
	}

	public void setAsevId(int asevId) {
		this.asevId = asevId;
	}


	@Column(name="ASEV_ESTADO", precision=38)
	public Integer getAsevEstado() {
		return this.asevEstado;
	}

	public void setAsevEstado(Integer asevEstado) {
		this.asevEstado = asevEstado;
	}


	@Column(name="ASEV_FECHA")
	public Timestamp getAsevFecha() {
		return this.asevFecha;
	}

	public void setAsevFecha(Timestamp asevFecha) {
		this.asevFecha = asevFecha;
	}


	@Column(name="ASEV_USUARIO", length=256)
	public String getAsevUsuario() {
		return this.asevUsuario;
	}

	public void setAsevUsuario(String asevUsuario) {
		this.asevUsuario = asevUsuario;
	}

	@Column(name="ASEV_EVALUADOR_CRR_ID",  precision=38)
	public Integer getAsevEvaluadorCrrId() {
		return asevEvaluadorCrrId;
	}

	public void setAsevEvaluadorCrrId(Integer asevEvaluadorCrrId) {
		this.asevEvaluadorCrrId = asevEvaluadorCrrId;
	}


	//bi-directional many-to-one association to CargaHoraria
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CRHR_ID")
	public CargaHoraria getAsevCargaHoraria() {
		return this.asevCargaHoraria;
	}

	public void setAsevCargaHoraria(CargaHoraria asevCargaHoraria) {
		this.asevCargaHoraria = asevCargaHoraria;
	}


	//bi-directional many-to-one association to EvaluadorEvaluado
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EVEV_ID")
	public EvaluadorEvaluado getAsevEvaluadorEvaluado() {
		return this.asevEvaluadorEvaluado;
	}

	public void setAsevEvaluadorEvaluado(EvaluadorEvaluado asevEvaluadorEvaluado) {
		this.asevEvaluadorEvaluado = asevEvaluadorEvaluado;
	}


	//bi-directional many-to-one association to Contenido
	@OneToMany(mappedBy="cntAsignacionEvaluador")
	public List<Contenido> getAsevListContenido() {
		return this.asevListContenido;
	}

	public void setAsevListContenido(List<Contenido> asevListContenido) {
		this.asevListContenido = asevListContenido;
	}

	public Contenido addAsevListContenido(Contenido asevListContenido) {
		getAsevListContenido().add(asevListContenido);
		asevListContenido.setCntAsignacionEvaluador(this);

		return asevListContenido;
	}

	public Contenido removeAsevListContenido(Contenido asevListContenido) {
		getAsevListContenido().remove(asevListContenido);
		asevListContenido.setCntAsignacionEvaluador(null);

		return asevListContenido;
	}

}