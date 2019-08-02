package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 09/03/2017 14:32:38 by Hibernate Tools 3.5.0.Final

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "CARGA_HORARIA", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "CargaHoraria.findAll", query="SELECT c FROM CargaHoraria c"),
				@NamedQuery(name = "CargaHoraria.findPorMallaCurricularParaleloId", query = "SELECT c FROM CargaHoraria c WHERE c.crhrMallaCurricularParalelo.mlcrprId = :mlcrprId AND c.crhrEstado = :crhrEstado and c.crhrEstadoEliminacion = :crhrEstadoEliminacion"),
				@NamedQuery(name = "CargaHoraria.findCargaHorariaPorMlcrprIdPrincipal", query = "SELECT c FROM CargaHoraria c WHERE c.crhrMlcrprIdPrincipal = :mlcrprIdPrincipal AND c.crhrEstado = :crhrEstado and c.crhrEstadoEliminacion = :crhrEstadoEliminacion")})
public class CargaHoraria implements java.io.Serializable {

	private static final long serialVersionUID = -4293101984087230351L;
	
	private int crhrId;
	private MallaCurricularParalelo crhrMallaCurricularParalelo;
	private PeriodoAcademico crhrPeriodoAcademico;
	private TipoFuncionCargaHoraria crhrTipoFuncionCargaHoraria;
	private DetallePuesto crhrDetallePuesto;
	private String crhrObservacion;
	private Integer crhrEstado;
	private Integer crhrEstadoEliminacion; 
	private Integer crhrNumHoras;
	private Integer crhrCarreraId; 
	private Integer crhrMlcrprIdPrincipal;
	private List<AsignacionEvaluador> chhrListAsignacionEvaluador;
	
	public CargaHoraria() {
	}

	public CargaHoraria(int crhrId) {
		this.crhrId = crhrId;
	}
  
	public CargaHoraria(int crhrId, MallaCurricularParalelo crhrMallaCurricularParalelo,
			PeriodoAcademico crhrPeriodoAcademico, TipoFuncionCargaHoraria crhrTipoFuncionCargaHoraria,
			DetallePuesto crhrDetallePuesto, String crhrObservacion, Integer crhrEstado, Integer crhrEstadoEliminacion,
			Integer crhrNumHoras, Integer crhrCarreraId) { 
		this.crhrId = crhrId;
		this.crhrMallaCurricularParalelo = crhrMallaCurricularParalelo;
		this.crhrPeriodoAcademico = crhrPeriodoAcademico;
		this.crhrTipoFuncionCargaHoraria = crhrTipoFuncionCargaHoraria;
		this.crhrDetallePuesto = crhrDetallePuesto;
		this.crhrObservacion = crhrObservacion;
		this.crhrEstado = crhrEstado;
		this.crhrEstadoEliminacion = crhrEstadoEliminacion;
		this.crhrNumHoras = crhrNumHoras;
		this.crhrCarreraId = crhrCarreraId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Basic(optional = false)
	@Column(name = "CRHR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getCrhrId() {
		return this.crhrId;
	}

	public void setCrhrId(int crhrId) {
		this.crhrId = crhrId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MLCRPR_ID")
	public MallaCurricularParalelo getCrhrMallaCurricularParalelo() {
		return this.crhrMallaCurricularParalelo;
	}

	public void setCrhrMallaCurricularParalelo(MallaCurricularParalelo crhrMallaCurricularParalelo) {
		this.crhrMallaCurricularParalelo = crhrMallaCurricularParalelo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRAC_ID")
	public PeriodoAcademico getCrhrPeriodoAcademico() {
		return this.crhrPeriodoAcademico;
	}

	public void setCrhrPeriodoAcademico(PeriodoAcademico crhrPeriodoAcademico) {
		this.crhrPeriodoAcademico = crhrPeriodoAcademico;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIFNCRHR_ID")
	public TipoFuncionCargaHoraria getCrhrTipoFuncionCargaHoraria() {
		return this.crhrTipoFuncionCargaHoraria;
	}

	public void setCrhrTipoFuncionCargaHoraria(TipoFuncionCargaHoraria crhrTipoFuncionCargaHoraria) {
		this.crhrTipoFuncionCargaHoraria = crhrTipoFuncionCargaHoraria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DTPS_ID")
	public DetallePuesto getCrhrDetallePuesto() {
		return this.crhrDetallePuesto;
	}

	public void setCrhrDetallePuesto(DetallePuesto crhrDetallePuesto) {
		this.crhrDetallePuesto = crhrDetallePuesto;
	}

	@Column(name = "CRHR_OBSERVACION", length = 1024)
	@Length(max = 1024)
	public String getCrhrObservacion() {
		return this.crhrObservacion;
	}

	public void setCrhrObservacion(String crhrObservacion) {
		this.crhrObservacion = crhrObservacion;
	}

	@Column(name = "CRHR_ESTADO", precision = 2, scale = 0)
	public Integer getCrhrEstado() {
		return this.crhrEstado;
	}

	public void setCrhrEstado(Integer crhrEstado) {
		this.crhrEstado = crhrEstado;
	}

	@Column(name = "CRHR_NUM_HORAS", precision = 2, scale = 0)
	public Integer getCrhrNumHoras() {
		return this.crhrNumHoras;
	}

	public void setCrhrNumHoras(Integer crhrNumHoras) {
		this.crhrNumHoras = crhrNumHoras;
	}

	@Column(name = "CRHR_ESTADO_ELIMINACION", precision = 2, scale = 0)
	public Integer getCrhrEstadoEliminacion() {
		return crhrEstadoEliminacion;
	}

	public void setCrhrEstadoEliminacion(Integer crhrEstadoEliminacion) {
		this.crhrEstadoEliminacion = crhrEstadoEliminacion;
	}
	
	@Column(name= "CRHR_CARRERA_ID")
	public Integer getCrhrCarreraId() {
		return crhrCarreraId;
	}

	public void setCrhrCarreraId(Integer crhrCarreraId) {
		this.crhrCarreraId = crhrCarreraId;
	}
	
	@Column(name= "MLCRPR_ID_PRINCIPAL")
	public Integer getCrhrMlcrprIdPrincipal() {
		return crhrMlcrprIdPrincipal;
	}

	public void setCrhrMlcrprIdPrincipal(Integer crhrMlcrprIdPrincipal) {
		this.crhrMlcrprIdPrincipal = crhrMlcrprIdPrincipal;
	}

	//bi-directional many-to-one association to AsignacionEvaluador
	@OneToMany(mappedBy="asevCargaHoraria")
	public List<AsignacionEvaluador> getChhrListAsignacionEvaluador() {
		return this.chhrListAsignacionEvaluador;
	}

	public void setChhrListAsignacionEvaluador(List<AsignacionEvaluador> chhrListAsignacionEvaluador) {
		this.chhrListAsignacionEvaluador = chhrListAsignacionEvaluador;
	}

	public AsignacionEvaluador addChhrListAsignacionEvaluador(AsignacionEvaluador chhrListAsignacionEvaluador) {
		getChhrListAsignacionEvaluador().add(chhrListAsignacionEvaluador);
		chhrListAsignacionEvaluador.setAsevCargaHoraria(this);

		return chhrListAsignacionEvaluador;
	}

	public AsignacionEvaluador removeChhrListAsignacionEvaluador(AsignacionEvaluador chhrListAsignacionEvaluador) {
		getChhrListAsignacionEvaluador().remove(chhrListAsignacionEvaluador);
		chhrListAsignacionEvaluador.setAsevCargaHoraria(null);

		return chhrListAsignacionEvaluador;
	}
	
	
}
