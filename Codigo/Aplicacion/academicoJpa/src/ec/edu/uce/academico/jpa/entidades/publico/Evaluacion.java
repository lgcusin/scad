package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the EVALUACION database table.
 * 
 */
@Entity
@Table(name="EVALUACION")
@NamedQuery(name="Evaluacion.findAll", query="SELECT e FROM Evaluacion e")
public class Evaluacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private int evaId;
	private String evaDescripcion;
	private Integer evaEstado;
	private Integer evaPrflId;
	private Timestamp evaFecha;
	private Timestamp evaCronogramaInicio;
	private Timestamp evaCronogramaFin;
	private String evaUsuario;
	private PeriodoAcademico evPeriodoAcademico;
	private TipoEvaluacion evTipoEvaluacion;
	private List<Contenido> evListContenido;
	private List<PlanificacionEvaluacion> evListPlanificacionEvaluacion;

	public Evaluacion() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="EVA_ID", unique=true, nullable=false, precision=6)
	public int getEvaId() {
		return this.evaId;
	}

	public void setEvaId(int evaId) {
		this.evaId = evaId;
	}


	@Column(name="EVA_DESCRIPCION", length=2000)
	public String getEvaDescripcion() {
		return this.evaDescripcion;
	}

	public void setEvaDescripcion(String evaDescripcion) {
		this.evaDescripcion = evaDescripcion;
	}


	@Column(name="EVA_ESTADO", precision=38)
	public Integer getEvaEstado() {
		return this.evaEstado;
	}

	public void setEvaEstado(Integer evaEstado) {
		this.evaEstado = evaEstado;
	}


	@Column(name="EVA_FECHA")
	public Timestamp getEvaFecha() {
		return this.evaFecha;
	}

	public void setEvaFecha(Timestamp evaFecha) {
		this.evaFecha = evaFecha;
	}


	@Column(name="EVA_USUARIO", length=256)
	public String getEvaUsuario() {
		return this.evaUsuario;
	}

	public void setEvaUsuario(String evaUsuario) {
		this.evaUsuario = evaUsuario;
	}
	
	@Column(name="EVA_PRFL_ID", precision=38)
	public Integer getEvaPrflId() {
		return evaPrflId;
	}


	public void setEvaPrflId(Integer evaPrflId) {
		this.evaPrflId = evaPrflId;
	}

	@Column(name="EVA_CRONOGRAMA_INICIO")
	public Timestamp getEvaCronogramaInicio() {
		return evaCronogramaInicio;
	}


	public void setEvaCronogramaInicio(Timestamp evaCronogramaInicio) {
		this.evaCronogramaInicio = evaCronogramaInicio;
	}

	@Column(name="EVA_CRONOGRAMA_FIN")
	public Timestamp getEvaCronogramaFin() {
		return evaCronogramaFin;
	}


	public void setEvaCronogramaFin(Timestamp evaCronogramaFin) {
		this.evaCronogramaFin = evaCronogramaFin;
	}


	//bi-directional many-to-one association to Contenido
	@OneToMany(mappedBy="cntEvaluacion")
	public List<Contenido> getEvListContenido() {
		return this.evListContenido;
	}

	public void setEvListContenido(List<Contenido> evListContenido) {
		this.evListContenido = evListContenido;
	}

	@OneToMany(mappedBy="plevEvaluacion")
	public List<PlanificacionEvaluacion> getEvListPlanificacionEvaluacion() {
		return evListPlanificacionEvaluacion;
	}


	public void setEvListPlanificacionEvaluacion(List<PlanificacionEvaluacion> evListPlanificacionEvaluacion) {
		this.evListPlanificacionEvaluacion = evListPlanificacionEvaluacion;
	}


	public Contenido addEvListContenido(Contenido evListContenido) {
		getEvListContenido().add(evListContenido);
		evListContenido.setCntEvaluacion(this);

		return evListContenido;
	}

	public Contenido removeEvListContenido(Contenido evListContenido) {
		getEvListContenido().remove(evListContenido);
		evListContenido.setCntEvaluacion(null);

		return evListContenido;
	}


	//bi-directional many-to-one association to PeriodoAcademico
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PRAC_ID")
	public PeriodoAcademico getEvPeriodoAcademico() {
		return this.evPeriodoAcademico;
	}

	public void setEvPeriodoAcademico(PeriodoAcademico evPeriodoAcademico) {
		this.evPeriodoAcademico = evPeriodoAcademico;
	}


	//bi-directional many-to-one association to TipoEvaluacion
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TPEV_ID")
	public TipoEvaluacion getEvTipoEvaluacion() {
		return this.evTipoEvaluacion;
	}

	public void setEvTipoEvaluacion(TipoEvaluacion evTipoEvaluacion) {
		this.evTipoEvaluacion = evTipoEvaluacion;
	}

}