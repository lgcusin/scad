package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the FUNCION_EVALUACION database table.
 * 
 */
@Entity
@Table(name="FUNCION_EVALUACION")
@NamedQuery(name="FuncionEvaluacion.findAll", query="SELECT f FROM FuncionEvaluacion f")
public class FuncionEvaluacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private int fnevId;
	private String fnevDescripcion;
	private Integer fnevEstado;
	private Timestamp fnevFecha;
	private String fnevUsuario;
	private List<FuncionTipoEvaluacion> fnevListFuncionTipoEvaluacion;

	public FuncionEvaluacion() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FNEV_ID", unique=true, nullable=false, precision=6)
	public int getFnevId() {
		return this.fnevId;
	}

	public void setFnevId(int fnevId) {
		this.fnevId = fnevId;
	}


	@Column(name="FNEV_DESCRIPCION", length=256)
	public String getFnevDescripcion() {
		return this.fnevDescripcion;
	}

	public void setFnevDescripcion(String fnevDescripcion) {
		this.fnevDescripcion = fnevDescripcion;
	}


	@Column(name="FNEV_ESTADO", precision=38)
	public Integer getFnevEstado() {
		return this.fnevEstado;
	}

	public void setFnevEstado(Integer fnevEstado) {
		this.fnevEstado = fnevEstado;
	}


	@Column(name="FNEV_FECHA")
	public Timestamp getFnevFecha() {
		return this.fnevFecha;
	}

	public void setFnevFecha(Timestamp fnevFecha) {
		this.fnevFecha = fnevFecha;
	}


	@Column(name="FNEV_USUARIO", length=256)
	public String getFnevUsuario() {
		return this.fnevUsuario;
	}

	public void setFnevUsuario(String fnevUsuario) {
		this.fnevUsuario = fnevUsuario;
	}


	//bi-directional many-to-one association to FuncionTipoEvaluacion
	@OneToMany(mappedBy="fntpevFuncionEvaluacion")
	public List<FuncionTipoEvaluacion> getFnevListFuncionTipoEvaluacion() {
		return this.fnevListFuncionTipoEvaluacion;
	}

	public void setFnevListFuncionTipoEvaluacion(List<FuncionTipoEvaluacion> fnevListFuncionTipoEvaluacion) {
		this.fnevListFuncionTipoEvaluacion = fnevListFuncionTipoEvaluacion;
	}

	public FuncionTipoEvaluacion addFnevListFuncionTipoEvaluacion(FuncionTipoEvaluacion fnevListFuncionTipoEvaluacion) {
		getFnevListFuncionTipoEvaluacion().add(fnevListFuncionTipoEvaluacion);
		fnevListFuncionTipoEvaluacion.setFntpevFuncionEvaluacion(this);

		return fnevListFuncionTipoEvaluacion;
	}

	public FuncionTipoEvaluacion removeFnevListFuncionTipoEvaluacion(FuncionTipoEvaluacion fnevListFuncionTipoEvaluacion) {
		getFnevListFuncionTipoEvaluacion().remove(fnevListFuncionTipoEvaluacion);
		fnevListFuncionTipoEvaluacion.setFntpevFuncionEvaluacion(null);

		return fnevListFuncionTipoEvaluacion;
	}

}