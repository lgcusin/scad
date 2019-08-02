package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TIPO_EVALUACION database table.
 * 
 */
@Entity
@Table(name="TIPO_EVALUACION")
@NamedQuery(name="TipoEvaluacion.findAll", query="SELECT t FROM TipoEvaluacion t")
public class TipoEvaluacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private int tpevId;
	private String tpevDescripcion;
	private Integer tpevEstado;
	private Timestamp tpevFecha;
	private String tpevInstrucciones;
	private String tpevIntroduccion;
	private String tpevUsuario;
	private List<Evaluacion> tpevListEvaluacion;
	private List<FuncionTipoEvaluacion> tpevListFuncionTipoEvaluacion;

	public TipoEvaluacion() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TPEV_ID", unique=true, nullable=false, precision=6)
	public int getTpevId() {
		return this.tpevId;
	}

	public void setTpevId(int tpevId) {
		this.tpevId = tpevId;
	}


	@Column(name="TPEV_DESCRIPCION", length=256)
	public String getTpevDescripcion() {
		return this.tpevDescripcion;
	}

	public void setTpevDescripcion(String tpevDescripcion) {
		this.tpevDescripcion = tpevDescripcion;
	}


	@Column(name="TPEV_ESTADO", precision=38)
	public Integer getTpevEstado() {
		return this.tpevEstado;
	}

	public void setTpevEstado(Integer tpevEstado) {
		this.tpevEstado = tpevEstado;
	}


	@Column(name="TPEV_FECHA")
	public Timestamp getTpevFecha() {
		return this.tpevFecha;
	}

	public void setTpevFecha(Timestamp tpevFecha) {
		this.tpevFecha = tpevFecha;
	}


	@Column(name="TPEV_INSTRUCCIONES", length=2000)
	public String getTpevInstrucciones() {
		return this.tpevInstrucciones;
	}

	public void setTpevInstrucciones(String tpevInstrucciones) {
		this.tpevInstrucciones = tpevInstrucciones;
	}


	@Column(name="TPEV_INTRODUCCION", length=2000)
	public String getTpevIntroduccion() {
		return this.tpevIntroduccion;
	}

	public void setTpevIntroduccion(String tpevIntroduccion) {
		this.tpevIntroduccion = tpevIntroduccion;
	}


	@Column(name="TPEV_USUARIO", length=256)
	public String getTpevUsuario() {
		return this.tpevUsuario;
	}

	public void setTpevUsuario(String tpevUsuario) {
		this.tpevUsuario = tpevUsuario;
	}


	//bi-directional many-to-one association to Evaluacion
	@OneToMany(mappedBy="evTipoEvaluacion")
	public List<Evaluacion> getTpevListEvaluacion() {
		return this.tpevListEvaluacion;
	}

	public void setTpevListEvaluacion(List<Evaluacion> tpevListEvaluacion) {
		this.tpevListEvaluacion = tpevListEvaluacion;
	}

	public Evaluacion addTpevListEvaluacion(Evaluacion tpevListEvaluacion) {
		getTpevListEvaluacion().add(tpevListEvaluacion);
		tpevListEvaluacion.setEvTipoEvaluacion(this);

		return tpevListEvaluacion;
	}

	public Evaluacion removeTpevListEvaluacion(Evaluacion tpevListEvaluacion) {
		getTpevListEvaluacion().remove(tpevListEvaluacion);
		tpevListEvaluacion.setEvTipoEvaluacion(null);

		return tpevListEvaluacion;
	}


	//bi-directional many-to-one association to FuncionTipoEvaluacion
	@OneToMany(mappedBy="tpevTipoEvaluacion")
	public List<FuncionTipoEvaluacion> getTpevListFuncionTipoEvaluacion() {
		return this.tpevListFuncionTipoEvaluacion;
	}

	public void setTpevListFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> tpevListFuncionTipoEvaluacion) {
		this.tpevListFuncionTipoEvaluacion = tpevListFuncionTipoEvaluacion;
	}

	public FuncionTipoEvaluacion addTpevListFuncionTipoEvaluacion(FuncionTipoEvaluacion tpevListFuncionTipoEvaluacion) {
		getTpevListFuncionTipoEvaluacion().add(tpevListFuncionTipoEvaluacion);
		tpevListFuncionTipoEvaluacion.setTpevTipoEvaluacion(this);

		return tpevListFuncionTipoEvaluacion;
	}

	public FuncionTipoEvaluacion removeTpevListFuncionTipoEvaluacion(FuncionTipoEvaluacion tpevListFuncionTipoEvaluacion) {
		getTpevListFuncionTipoEvaluacion().remove(tpevListFuncionTipoEvaluacion);
		tpevListFuncionTipoEvaluacion.setTpevTipoEvaluacion(null);

		return tpevListFuncionTipoEvaluacion;
	}

}