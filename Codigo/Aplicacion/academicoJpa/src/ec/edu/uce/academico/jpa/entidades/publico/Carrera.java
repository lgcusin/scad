package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 02/03/2017 8:53:22 by Hibernate Tools 3.5.0.Final

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "CARRERA", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "Carrera.findAll", query = "SELECT c FROM Carrera c"),
				@NamedQuery(name = "Carrera.findPorDependenciaTipoCrr", query = "SELECT c FROM Carrera c WHERE c.crrDependencia.dpnId = :dpnId and c.crrTipo = :crrTipo"),
				@NamedQuery(name = "Carrera.findPorEspeCodigo", query = "SELECT c FROM Carrera c WHERE c.crrEspeCodigo = :espeCodigo")})
public class Carrera implements java.io.Serializable {
	private static final long serialVersionUID = -7469124396881618811L;

	private int crrId;
	private Dependencia crrDependencia;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrTipo;
	private Integer crrCupo;
	private BigDecimal crrArancel;
	private Integer crrEspeCodigo;
	private Integer crrProceso;
	private Integer crrNumMaxCreditos;
	private Integer crrIdArancel;
	private Integer crrTipoEvaluacion;
	private List<NucleoProblemicoCarrera> crrNucleoProblemicoCarreras = new ArrayList<NucleoProblemicoCarrera>(0);
	private List<Materia> crrMaterias = new ArrayList<Materia>(0);
	private List<MallaCurricular> crrMallaCurriculars = new ArrayList<MallaCurricular>(0);
	private List<ConfiguracionCarrera> crrConfiguracionCarreras = new ArrayList<ConfiguracionCarrera>(0);
	private List<DetallePuesto> crrDetallePuestos = new ArrayList<DetallePuesto>(0);
	private List<Itinerario> crrItinerarios = new ArrayList<Itinerario>(0);
	private List<RolFlujoCarrera> rolFlujoCarreras = new ArrayList<RolFlujoCarrera>(0);
	private List<Paralelo> crrParalelos = new ArrayList<Paralelo>(0);
	private List<CarreraArea> crrAreas = new ArrayList<CarreraArea>(0);
	private List<CarreraArea> crrCarreras = new ArrayList<CarreraArea>(0);
	private List<Grupo> crrListGrupo;
	
	private List<NotaCorte> crrNotaCortes = new ArrayList<NotaCorte>(0);

	public Carrera() {
	}

	public Carrera(int crrId) {
		this.crrId = crrId;
	}

	public Carrera(int crrId, Dependencia crrDependencia, String crrDescripcion, String crrCodSniese, String crrDetalle,
			Date crrFechaCreacion, String crrResolucion, Integer crrTipo, Integer crrCupo, BigDecimal crrArancel,
			Integer crrEspeCodigo, Integer crrProceso, Integer crrNumMaxCreditos, Integer crrIdArancel,
			Integer crrTipoEvaluacion, List<NucleoProblemicoCarrera> crrNucleoProblemicoCarreras,
			List<Materia> crrMaterias, List<MallaCurricular> crrMallaCurriculars,
			List<ConfiguracionCarrera> crrConfiguracionCarreras, List<DetallePuesto> crrDetallePuestos,
			List<Itinerario> crrItinerarios, List<RolFlujoCarrera> rolFlujoCarreras, List<Paralelo> crrParalelos,
			List<CarreraArea> crrAreas, List<CarreraArea> crrCarreras, List<Grupo> crrListGrupo, List<NotaCorte> crrNotaCortes) {
		this.crrId = crrId;
		this.crrDependencia = crrDependencia;
		this.crrDescripcion = crrDescripcion;
		this.crrCodSniese = crrCodSniese;
		this.crrDetalle = crrDetalle;
		this.crrFechaCreacion = crrFechaCreacion;
		this.crrResolucion = crrResolucion;
		this.crrTipo = crrTipo;
		this.crrCupo = crrCupo;
		this.crrArancel = crrArancel;
		this.crrEspeCodigo = crrEspeCodigo;
		this.crrProceso = crrProceso;
		this.crrNumMaxCreditos = crrNumMaxCreditos;
		this.crrIdArancel = crrIdArancel;
		this.crrTipoEvaluacion = crrTipoEvaluacion;
		this.crrNucleoProblemicoCarreras = crrNucleoProblemicoCarreras;
		this.crrMaterias = crrMaterias;
		this.crrMallaCurriculars = crrMallaCurriculars;
		this.crrConfiguracionCarreras = crrConfiguracionCarreras;
		this.crrDetallePuestos = crrDetallePuestos;
		this.crrItinerarios = crrItinerarios;
		this.rolFlujoCarreras = rolFlujoCarreras;
		this.crrParalelos = crrParalelos;
		this.crrAreas = crrAreas;
		this.crrCarreras = crrCarreras;
		this.crrListGrupo = crrListGrupo;
		this.crrNotaCortes = crrNotaCortes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "CRR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getCrrId() {
		return this.crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	@ManyToOne
	@JoinColumn(name = "DPN_ID")
	public Dependencia getCrrDependencia() {
		return this.crrDependencia;
	}

	public void setCrrDependencia(Dependencia crrDependencia) {
		this.crrDependencia = crrDependencia;
	}

	@Column(name = "CRR_DESCRIPCION", length = 128)
	@Length(max = 128)
	public String getCrrDescripcion() {
		return this.crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	@Column(name = "CRR_COD_SNIESE", length = 32)
	@Length(max = 32)
	public String getCrrCodSniese() {
		return this.crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	@Column(name = "CRR_DETALLE", length = 256)
	@Length(max = 256)
	public String getCrrDetalle() {
		return this.crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CRR_FECHA_CREACION", length = 7)
	public Date getCrrFechaCreacion() {
		return this.crrFechaCreacion;
	}

	public void setCrrFechaCreacion(Date crrFechaCreacion) {
		this.crrFechaCreacion = crrFechaCreacion;
	}

	@Column(name = "CRR_RESOLUCION", length = 128)
	@Length(max = 128)
	public String getCrrResolucion() {
		return this.crrResolucion;
	}

	public void setCrrResolucion(String crrResolucion) {
		this.crrResolucion = crrResolucion;
	}

	@Column(name = "CRR_TIPO")
	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	@Column(name = "CRR_CUPO")
	public Integer getCrrCupo() {
		return crrCupo;
	}

	public void setCrrCupo(Integer crrCupo) {
		this.crrCupo = crrCupo;
	}

	@Column(name = "CRR_ARANCEL", precision = 8, scale = 2)
	public BigDecimal getCrrArancel() {
		return crrArancel;
	}

	public void setCrrArancel(BigDecimal crrArancel) {
		this.crrArancel = crrArancel;
	}

	@OneToMany(mappedBy = "ncprcrCarrera")
	public List<NucleoProblemicoCarrera> getCrrNucleoProblemicoCarreras() {
		return this.crrNucleoProblemicoCarreras;
	}

	public void setCrrNucleoProblemicoCarreras(List<NucleoProblemicoCarrera> crrNucleoProblemicoCarreras) {
		this.crrNucleoProblemicoCarreras = crrNucleoProblemicoCarreras;
	}

	@OneToMany(mappedBy = "mtrCarrera")
	public List<Materia> getCrrMaterias() {
		return this.crrMaterias;
	}

	public void setCrrMaterias(List<Materia> crrMaterias) {
		this.crrMaterias = crrMaterias;
	}

	@OneToMany(mappedBy = "mlcrCarrera")
	public List<MallaCurricular> getCrrMallaCurriculars() {
		return this.crrMallaCurriculars;
	}

	public void setCrrMallaCurriculars(List<MallaCurricular> crrMallaCurriculars) {
		this.crrMallaCurriculars = crrMallaCurriculars;
	}

	@OneToMany(mappedBy = "cncrCarrera")
	public List<ConfiguracionCarrera> getCrrConfiguracionCarreras() {
		return this.crrConfiguracionCarreras;
	}

	public void setCrrConfiguracionCarreras(List<ConfiguracionCarrera> crrConfiguracionCarreras) {
		this.crrConfiguracionCarreras = crrConfiguracionCarreras;
	}

	@OneToMany(mappedBy = "dtpsCarrera")
	public List<DetallePuesto> getCrrDetallePuestos() {
		return this.crrDetallePuestos;
	}

	public void setCrrDetallePuestos(List<DetallePuesto> crrDetallePuestos) {
		this.crrDetallePuestos = crrDetallePuestos;
	}

	@OneToMany(mappedBy = "itnCarrera")
	public List<Itinerario> getCrrItinerarios() {
		return this.crrItinerarios;
	}

	public void setCrrItinerarios(List<Itinerario> crrItinerarios) {
		this.crrItinerarios = crrItinerarios;
	}

	@OneToMany(mappedBy = "roflcrCarrera")
	public List<RolFlujoCarrera> getRolFlujoCarreras() {
		return rolFlujoCarreras;
	}

	public void setRolFlujoCarreras(List<RolFlujoCarrera> rolFlujoCarreras) {
		this.rolFlujoCarreras = rolFlujoCarreras;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prlCarrera")
	public List<Paralelo> getCrrParalelos() {
		return crrParalelos;
	}

	public void setCrrParalelos(List<Paralelo> crrParalelos) {
		this.crrParalelos = crrParalelos;
	}

	@Column(name = "CRR_ESPE_CODIGO")
	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	@Column(name = "CRR_PROCESO")
	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	@Column(name = "CRR_NUM_MAX_CREDITOS")
	public Integer getCrrNumMaxCreditos() {
		return crrNumMaxCreditos;
	}

	public void setCrrNumMaxCreditos(Integer crrNumMaxCreditos) {
		this.crrNumMaxCreditos = crrNumMaxCreditos;
	}

	@OneToMany(mappedBy = "crarArea")
	public List<CarreraArea> getCrrAreas() {
		return crrAreas;
	}

	public void setCrrAreas(List<CarreraArea> crrAreas) {
		this.crrAreas = crrAreas;
	}

	@OneToMany(mappedBy = "crarCarrera")
	public List<CarreraArea> getCrrCarreras() {
		return crrCarreras;
	}

	public void setCrrCarreras(List<CarreraArea> crrCarreras) {
		this.crrCarreras = crrCarreras;
	}

	@OneToMany(mappedBy = "grpCarrera")
	public List<Grupo> getCrrListGrupo() {
		return crrListGrupo;
	}

	public void setCrrListGrupo(List<Grupo> crrListGrupo) {
		this.crrListGrupo = crrListGrupo;
	}

	@Column(name = "CRR_ID_ARANCEL")
	public Integer getCrrIdArancel() {
		return crrIdArancel;
	}

	public void setCrrIdArancel(Integer crrIdArancel) {
		this.crrIdArancel = crrIdArancel;
	}
	
	@Column(name = "CRR_TIPO_EVALUACION")
	public Integer getCrrTipoEvaluacion() {
		return crrTipoEvaluacion;
	}

	public void setCrrTipoEvaluacion(Integer crrTipoEvaluacion) {
		this.crrTipoEvaluacion = crrTipoEvaluacion;
	}

	@OneToMany(mappedBy = "nocrCarrera")
	public List<NotaCorte> getCrrNotaCortes() {
		return this.crrNotaCortes;
	}

	public void setCrrNotaCortes(List<NotaCorte> crrNotaCortes) {
		this.crrNotaCortes = crrNotaCortes;
	}
	
	
	
}
