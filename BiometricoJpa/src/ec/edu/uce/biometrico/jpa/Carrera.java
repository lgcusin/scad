package ec.edu.uce.biometrico.jpa;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Carrera generated by hbm2java
 */
@Entity
@Table(name = "CARRERA")
@NamedQueries({
		@NamedQuery(name = "Carrera.findAllByFdId", query = "select dp from DetallePuesto as dp where dp.dtpsFichaDocente.fcdcId=:fcdcId"),
		@NamedQuery(name = "Carrera.findByMtrId", query = "select m.mtrCarrera from Materia as m where m.mtrId=:mtrId") })
public class Carrera implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer crrId;
	private Dependencia crrDependencia;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private Integer crrTipo;
	private Integer crrCupo;
	private Integer crrEspeCodigo;
	private Integer crrProceso;
	private Integer crrNumMaxCreditos;
	private List<Paralelo> paralelos = new ArrayList<Paralelo>(0);
	private List<DetallePuesto> detallePuestos = new ArrayList<DetallePuesto>(0);
	private List<Materia> materias = new ArrayList<Materia>(0);
	private List<MallaCurricular> mallaCurriculars = new ArrayList<MallaCurricular>(0);
	private List<RolFlujoCarrera> rolFlujoCarreras = new ArrayList<RolFlujoCarrera>(0);
	private List<Feriado> feriados = new ArrayList<Feriado>(0);

	public Carrera() {
	}

	public Carrera(Integer crrId) {
		this.crrId = crrId;
	}

	public Carrera(Integer crrId, Dependencia dependencia, String crrDescripcion, String crrCodSniese,
			String crrDetalle, Date crrFechaCreacion, Integer crrTipo, Integer crrCupo, Integer crrEspeCodigo,
			Integer crrProceso, Integer crrNumMaxCreditos, List<Paralelo> paralelos, List<DetallePuesto> detallePuestos,
			List<Materia> materias, List<MallaCurricular> mallaCurriculars, List<RolFlujoCarrera> rolFlujoCarreras, List<Feriado> feriados) {
		this.crrId = crrId;
		this.crrDependencia = dependencia;
		this.crrDescripcion = crrDescripcion;
		this.crrCodSniese = crrCodSniese;
		this.crrDetalle = crrDetalle;
		this.crrFechaCreacion = crrFechaCreacion;
		this.crrTipo = crrTipo;
		this.crrCupo = crrCupo;
		this.crrEspeCodigo = crrEspeCodigo;
		this.crrProceso = crrProceso;
		this.crrNumMaxCreditos = crrNumMaxCreditos;
		this.paralelos = paralelos;
		this.detallePuestos = detallePuestos;
		this.materias = materias;
		this.mallaCurriculars = mallaCurriculars;
		this.rolFlujoCarreras = rolFlujoCarreras;
		this.feriados = feriados;
	}

	@Id

	@Column(name = "CRR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getCrrId() {
		return this.crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DPN_ID")
	public Dependencia getCrrDependencia() {
		return this.crrDependencia;
	}

	public void setCrrDependencia(Dependencia dependencia) {
		this.crrDependencia = dependencia;
	}

	@Column(name = "CRR_DESCRIPCION", length = 128)
	public String getCrrDescripcion() {
		return this.crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	@Column(name = "CRR_COD_SNIESE", length = 32)
	public String getCrrCodSniese() {
		return this.crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	@Column(name = "CRR_DETALLE", length = 256)
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

	@Column(name = "CRR_TIPO", precision = 38, scale = 0)
	public Integer getCrrTipo() {
		return this.crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	@Column(name = "CRR_CUPO", precision = 38, scale = 0)
	public Integer getCrrCupo() {
		return this.crrCupo;
	}

	public void setCrrCupo(Integer crrCupo) {
		this.crrCupo = crrCupo;
	}

	@Column(name = "CRR_ESPE_CODIGO", precision = 38, scale = 0)
	public Integer getCrrEspeCodigo() {
		return this.crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	@Column(name = "CRR_PROCESO", precision = 38, scale = 0)
	public Integer getCrrProceso() {
		return this.crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	@Column(name = "CRR_NUM_MAX_CREDITOS", precision = 22, scale = 0)
	public Integer getCrrNumMaxCreditos() {
		return this.crrNumMaxCreditos;
	}

	public void setCrrNumMaxCreditos(Integer crrNumMaxCreditos) {
		this.crrNumMaxCreditos = crrNumMaxCreditos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prlCarrera")
	public List<Paralelo> getParalelos() {
		return this.paralelos;
	}

	public void setParalelos(List<Paralelo> paralelos) {
		this.paralelos = paralelos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dtpsCarrera")
	public List<DetallePuesto> getDetallePuestos() {
		return this.detallePuestos;
	}

	public void setDetallePuestos(List<DetallePuesto> detallePuestos) {
		this.detallePuestos = detallePuestos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mtrCarrera")
	public List<Materia> getMaterias() {
		return this.materias;
	}

	public void setMaterias(List<Materia> materias) {
		this.materias = materias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mlcrCarrera")
	public List<MallaCurricular> getMallaCurriculars() {
		return this.mallaCurriculars;
	}

	public void setMallaCurriculars(List<MallaCurricular> mallaCurriculars) {
		this.mallaCurriculars = mallaCurriculars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roflcrCarrera")
	public List<RolFlujoCarrera> getRolFlujoCarreras() {
		return this.rolFlujoCarreras;
	}

	public void setRolFlujoCarreras(List<RolFlujoCarrera> rolFlujoCarreras) {
		this.rolFlujoCarreras = rolFlujoCarreras;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "frdCarrera")
	public List<Feriado> getFeriados() {
		return this.feriados;
	}

	public void setFeriados(List<Feriado> feriados) {
		this.feriados = feriados;
	}

}
