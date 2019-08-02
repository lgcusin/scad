package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the FUNCION_TIPO_EVALUACION database table.
 * 
 */
@Entity
@Table(name="FUNCION_TIPO_EVALUACION")
@NamedQuery(name="FuncionTipoEvaluacion.findAll", query="SELECT f FROM FuncionTipoEvaluacion f")
public class FuncionTipoEvaluacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private int fnctpevId;
	private Integer fnctpevEstado;
	private Timestamp fnctpevFecha;
	private String fnctpevUsuario;
	private FuncionEvaluacion fntpevFuncionEvaluacion;
	private TipoEvaluacion tpevTipoEvaluacion;
	private List<TpcnFuncionTpev> fntpevListTpcnFuncionTpev;

	public FuncionTipoEvaluacion() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FNCTPEV_ID", unique=true, nullable=false, precision=6)
	public int getFnctpevId() {
		return this.fnctpevId;
	}

	public void setFnctpevId(int fnctpevId) {
		this.fnctpevId = fnctpevId;
	}


	@Column(name="FNCTPEV_ESTADO", precision=38)
	public Integer getFnctpevEstado() {
		return this.fnctpevEstado;
	}

	public void setFnctpevEstado(Integer fnctpevEstado) {
		this.fnctpevEstado = fnctpevEstado;
	}


	@Column(name="FNCTPEV_FECHA")
	public Timestamp getFnctpevFecha() {
		return this.fnctpevFecha;
	}

	public void setFnctpevFecha(Timestamp fnctpevFecha) {
		this.fnctpevFecha = fnctpevFecha;
	}


	@Column(name="FNCTPEV_USUARIO", length=256)
	public String getFnctpevUsuario() {
		return this.fnctpevUsuario;
	}

	public void setFnctpevUsuario(String fnctpevUsuario) {
		this.fnctpevUsuario = fnctpevUsuario;
	}


	//bi-directional many-to-one association to FuncionEvaluacion
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="FNEV_ID")
	public FuncionEvaluacion getFntpevFuncionEvaluacion() {
		return this.fntpevFuncionEvaluacion;
	}

	public void setFntpevFuncionEvaluacion(FuncionEvaluacion fntpevFuncionEvaluacion) {
		this.fntpevFuncionEvaluacion = fntpevFuncionEvaluacion;
	}


	//bi-directional many-to-one association to TipoEvaluacion
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TPEV_ID")
	public TipoEvaluacion getTpevTipoEvaluacion() {
		return this.tpevTipoEvaluacion;
	}

	public void setTpevTipoEvaluacion(TipoEvaluacion tpevTipoEvaluacion) {
		this.tpevTipoEvaluacion = tpevTipoEvaluacion;
	}


	//bi-directional many-to-one association to TpcnFuncionTpev
	@OneToMany(mappedBy="tpcnfntpevFuncionTipoEvaluacion", fetch=FetchType.EAGER)
	public List<TpcnFuncionTpev> getFntpevListTpcnFuncionTpev() {
		return this.fntpevListTpcnFuncionTpev;
	}

	public void setFntpevListTpcnFuncionTpev(List<TpcnFuncionTpev> fntpevListTpcnFuncionTpev) {
		this.fntpevListTpcnFuncionTpev = fntpevListTpcnFuncionTpev;
	}

	public TpcnFuncionTpev addFntpevListTpcnFuncionTpev(TpcnFuncionTpev fntpevListTpcnFuncionTpev) {
		getFntpevListTpcnFuncionTpev().add(fntpevListTpcnFuncionTpev);
		fntpevListTpcnFuncionTpev.setTpcnfntpevFuncionTipoEvaluacion(this);

		return fntpevListTpcnFuncionTpev;
	}

	public TpcnFuncionTpev removeFntpevListTpcnFuncionTpev(TpcnFuncionTpev fntpevListTpcnFuncionTpev) {
		getFntpevListTpcnFuncionTpev().remove(fntpevListTpcnFuncionTpev);
		fntpevListTpcnFuncionTpev.setTpcnfntpevFuncionTipoEvaluacion(null);

		return fntpevListTpcnFuncionTpev;
	}

}