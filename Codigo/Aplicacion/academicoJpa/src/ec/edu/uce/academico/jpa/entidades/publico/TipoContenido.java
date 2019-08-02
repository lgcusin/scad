package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TIPO_CONTENIDO database table.
 * 
 */
@Entity
@Table(name="TIPO_CONTENIDO")
@NamedQuery(name="TipoContenido.findAll", query="SELECT t FROM TipoContenido t")
public class TipoContenido implements Serializable, Comparable<TipoContenido>{
	private static final long serialVersionUID = 1L;
	private int tpcnId;
	private String tpcnDescripcion;
	private Integer tpcnEstado;
	private Timestamp tpcnFecha;
	private Integer tpcnNumMax;
	private Integer tpcnNumeral;
	private Integer tpcnObligatoriedad;
	private Integer tpcnTipo;
	private Integer tpcnTipoComponente;
	private Integer tpcnTipoSeleccion;
	private String tpcnUsuario;
	private TipoContenido tpcn_TipoContenido;
	private List<TipoContenido> tpcnLisTipoContenido;
	private List<TpcnFuncionTpev> tpcnListTpcnFuncionTpev;

	public TipoContenido() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TPCN_ID", unique=true, nullable=false, precision=6)
	public int getTpcnId() {
		return this.tpcnId;
	}

	public void setTpcnId(int tpcnId) {
		this.tpcnId = tpcnId;
	}


	@Column(name="TPCN_DESCRIPCION", length=2000)
	public String getTpcnDescripcion() {
		return this.tpcnDescripcion;
	}

	public void setTpcnDescripcion(String tpcnDescripcion) {
		this.tpcnDescripcion = tpcnDescripcion;
	}


	@Column(name="TPCN_ESTADO", precision=38)
	public Integer getTpcnEstado() {
		return this.tpcnEstado;
	}

	public void setTpcnEstado(Integer tpcnEstado) {
		this.tpcnEstado = tpcnEstado;
	}


	@Column(name="TPCN_FECHA")
	public Timestamp getTpcnFecha() {
		return this.tpcnFecha;
	}

	public void setTpcnFecha(Timestamp tpcnFecha) {
		this.tpcnFecha = tpcnFecha;
	}


	@Column(name="TPCN_NUM_MAX", precision=38)
	public Integer getTpcnNumMax() {
		return this.tpcnNumMax;
	}

	public void setTpcnNumMax(Integer tpcnNumMax) {
		this.tpcnNumMax = tpcnNumMax;
	}


	@Column(name="TPCN_NUMERAL", precision=38)
	public Integer getTpcnNumeral() {
		return this.tpcnNumeral;
	}

	public void setTpcnNumeral(Integer tpcnNumeral) {
		this.tpcnNumeral = tpcnNumeral;
	}


	@Column(name="TPCN_OBLIGATORIEDAD", precision=38)
	public Integer getTpcnObligatoriedad() {
		return this.tpcnObligatoriedad;
	}

	public void setTpcnObligatoriedad(Integer tpcnObligatoriedad) {
		this.tpcnObligatoriedad = tpcnObligatoriedad;
	}


	@Column(name="TPCN_TIPO", precision=38)
	public Integer getTpcnTipo() {
		return this.tpcnTipo;
	}

	public void setTpcnTipo(Integer tpcnTipo) {
		this.tpcnTipo = tpcnTipo;
	}


	@Column(name="TPCN_TIPO_COMPONENTE", precision=38)
	public Integer getTpcnTipoComponente() {
		return this.tpcnTipoComponente;
	}

	public void setTpcnTipoComponente(Integer tpcnTipoComponente) {
		this.tpcnTipoComponente = tpcnTipoComponente;
	}


	@Column(name="TPCN_TIPO_SELECCION", precision=38)
	public Integer getTpcnTipoSeleccion() {
		return this.tpcnTipoSeleccion;
	}

	public void setTpcnTipoSeleccion(Integer tpcnTipoSeleccion) {
		this.tpcnTipoSeleccion = tpcnTipoSeleccion;
	}


	@Column(name="TPCN_USUARIO", length=256)
	public String getTpcnUsuario() {
		return this.tpcnUsuario;
	}

	public void setTpcnUsuario(String tpcnUsuario) {
		this.tpcnUsuario = tpcnUsuario;
	}


	//bi-directional many-to-one association to TipoContenido
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TPCN_FK_TPCN_ID")
	public TipoContenido getTpcn_TipoContenido() {
		return this.tpcn_TipoContenido;
	}

	public void setTpcn_TipoContenido(TipoContenido tpcn_TipoContenido) {
		this.tpcn_TipoContenido = tpcn_TipoContenido;
	}


	//bi-directional many-to-one association to TipoContenido
	@OneToMany(mappedBy="tpcn_TipoContenido", fetch=FetchType.EAGER)
	public List<TipoContenido> getTpcnLisTipoContenido() {
		return this.tpcnLisTipoContenido;
	}

	public void setTpcnLisTipoContenido(List<TipoContenido> tpcnLisTipoContenido) {
		this.tpcnLisTipoContenido = tpcnLisTipoContenido;
	}

	public TipoContenido addTpcnLisTipoContenido(TipoContenido tpcnLisTipoContenido) {
		getTpcnLisTipoContenido().add(tpcnLisTipoContenido);
		tpcnLisTipoContenido.setTpcn_TipoContenido(this);

		return tpcnLisTipoContenido;
	}

	public TipoContenido removeTpcnLisTipoContenido(TipoContenido tpcnLisTipoContenido) {
		getTpcnLisTipoContenido().remove(tpcnLisTipoContenido);
		tpcnLisTipoContenido.setTpcn_TipoContenido(null);

		return tpcnLisTipoContenido;
	}


	//bi-directional many-to-one association to TpcnFuncionTpev
	@OneToMany(mappedBy="tpcnfntpevTipoContenido")
	public List<TpcnFuncionTpev> getTpcnListTpcnFuncionTpev() {
		return this.tpcnListTpcnFuncionTpev;
	}

	public void setTpcnListTpcnFuncionTpev(List<TpcnFuncionTpev> tpcnListTpcnFuncionTpev) {
		this.tpcnListTpcnFuncionTpev = tpcnListTpcnFuncionTpev;
	}

	public TpcnFuncionTpev addTpcnListTpcnFuncionTpev(TpcnFuncionTpev tpcnListTpcnFuncionTpev) {
		getTpcnListTpcnFuncionTpev().add(tpcnListTpcnFuncionTpev);
		tpcnListTpcnFuncionTpev.setTpcnfntpevTipoContenido(this);

		return tpcnListTpcnFuncionTpev;
	}

	public TpcnFuncionTpev removeTpcnListTpcnFuncionTpev(TpcnFuncionTpev tpcnListTpcnFuncionTpev) {
		getTpcnListTpcnFuncionTpev().remove(tpcnListTpcnFuncionTpev);
		tpcnListTpcnFuncionTpev.setTpcnfntpevTipoContenido(null);

		return tpcnListTpcnFuncionTpev;
	}
	
	@Override
	public int compareTo(TipoContenido o) {
       return tpcnNumeral.compareTo(o.getTpcnNumeral());
   }

}