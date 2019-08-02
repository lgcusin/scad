package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TPCN_FUNCION_TPEV database table.
 * 
 */
@Entity
@Table(name="TPCN_FUNCION_TPEV")
@NamedQuery(name="TpcnFuncionTpev.findAll", query="SELECT t FROM TpcnFuncionTpev t")
public class TpcnFuncionTpev implements Serializable, Comparable<TpcnFuncionTpev> {
	private static final long serialVersionUID = 1L;
	private int tpcnfntpevId;
	private Integer tpcnfntpevEstado;
	private Timestamp tpcnfntpevFecha;
	private String tpcnfntpevUsuario;
	private List<Contenido> tpcnfntpevListContenido;
	private FuncionTipoEvaluacion tpcnfntpevFuncionTipoEvaluacion;
	private TipoContenido tpcnfntpevTipoContenido;

	public TpcnFuncionTpev() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TPCNFNTPEV_ID", unique=true, nullable=false, precision=6)
	public int getTpcnfntpevId() {
		return this.tpcnfntpevId;
	}

	public void setTpcnfntpevId(int tpcnfntpevId) {
		this.tpcnfntpevId = tpcnfntpevId;
	}


	@Column(name="TPCNFNTPEV_ESTADO", precision=38)
	public Integer getTpcnfntpevEstado() {
		return this.tpcnfntpevEstado;
	}

	public void setTpcnfntpevEstado(Integer tpcnfntpevEstado) {
		this.tpcnfntpevEstado = tpcnfntpevEstado;
	}


	@Column(name="TPCNFNTPEV_FECHA")
	public Timestamp getTpcnfntpevFecha() {
		return this.tpcnfntpevFecha;
	}

	public void setTpcnfntpevFecha(Timestamp tpcnfntpevFecha) {
		this.tpcnfntpevFecha = tpcnfntpevFecha;
	}


	@Column(name="TPCNFNTPEV_USUARIO", length=256)
	public String getTpcnfntpevUsuario() {
		return this.tpcnfntpevUsuario;
	}

	public void setTpcnfntpevUsuario(String tpcnfntpevUsuario) {
		this.tpcnfntpevUsuario = tpcnfntpevUsuario;
	}


	//bi-directional many-to-one association to Contenido
	@OneToMany(mappedBy="cntTpcnFuncionTpev")
	public List<Contenido> getTpcnfntpevListContenido() {
		return this.tpcnfntpevListContenido;
	}

	public void setTpcnfntpevListContenido(List<Contenido> tpcnfntpevListContenido) {
		this.tpcnfntpevListContenido = tpcnfntpevListContenido;
	}

	public Contenido addTpcnfntpevListContenido(Contenido tpcnfntpevListContenido) {
		getTpcnfntpevListContenido().add(tpcnfntpevListContenido);
		tpcnfntpevListContenido.setCntTpcnFuncionTpev(this);

		return tpcnfntpevListContenido;
	}

	public Contenido removeTpcnfntpevListContenido(Contenido tpcnfntpevListContenido) {
		getTpcnfntpevListContenido().remove(tpcnfntpevListContenido);
		tpcnfntpevListContenido.setCntTpcnFuncionTpev(null);

		return tpcnfntpevListContenido;
	}


	//bi-directional many-to-one association to FuncionTipoEvaluacion
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="FNCTPEV_ID")
	public FuncionTipoEvaluacion getTpcnfntpevFuncionTipoEvaluacion() {
		return this.tpcnfntpevFuncionTipoEvaluacion;
	}

	public void setTpcnfntpevFuncionTipoEvaluacion(FuncionTipoEvaluacion tpcnfntpevFuncionTipoEvaluacion) {
		this.tpcnfntpevFuncionTipoEvaluacion = tpcnfntpevFuncionTipoEvaluacion;
	}


	//bi-directional many-to-one association to TipoContenido
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TPCN_ID")
	public TipoContenido getTpcnfntpevTipoContenido() {
		return this.tpcnfntpevTipoContenido;
	}

	public void setTpcnfntpevTipoContenido(TipoContenido tpcnfntpevTipoContenido) {
		this.tpcnfntpevTipoContenido = tpcnfntpevTipoContenido;
	}


	@Override
	public int compareTo(TpcnFuncionTpev o) { 
		return tpcnfntpevTipoContenido.getTpcnNumeral().compareTo(o.getTpcnfntpevTipoContenido().getTpcnNumeral());
	}

}