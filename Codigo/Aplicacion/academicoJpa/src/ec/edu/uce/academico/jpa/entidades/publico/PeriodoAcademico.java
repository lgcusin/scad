package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 09/03/2017 11:32:19 by Hibernate Tools 3.5.0.Final

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "PERIODO_ACADEMICO", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "PeriodoAcademico.findAll", query = "SELECT p FROM PeriodoAcademico p"),
		@NamedQuery(name = "PeriodoAcademico.findPorTipoEstado", query = "SELECT p FROM PeriodoAcademico p WHERE p.pracTipo = :pracTipo AND p.pracEstado = :pracEstado") })
public class PeriodoAcademico implements java.io.Serializable {

	private static final long serialVersionUID = 2734935442267437533L;

	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;
	private Integer pracTipo;
	private List<CargaHoraria> pracCargaHorarias = new ArrayList<CargaHoraria>(0);
	private List<Cronograma> pracCronogramas = new ArrayList<Cronograma>(0);
	private List<Proyeccion> pracProyeccions = new ArrayList<Proyeccion>(0);
	private List<MallaPeriodo> pracMallaPeriodos = new ArrayList<MallaPeriodo>(0);
	private List<Paralelo> pracParalelos = new ArrayList<Paralelo>(0);
	private List<FichaInscripcion> pracFichaInscripciones = new ArrayList<FichaInscripcion>(0);
	private List<SolicitudTerceraMatricula> pracSolicitudesTerceraMatricula = new ArrayList<SolicitudTerceraMatricula>(
			0);
	private List<SistemaCalificacion> sistemaCalificacions;
	private List<Evaluacion> pracListEvaluacion;
	private List<RolFlujoGrupo> pracListRolFlujoGrupo;

	public PeriodoAcademico() {
	}

	public PeriodoAcademico(int pracId) {
		this.pracId = pracId;
	}

	public PeriodoAcademico(int pracId, String pracDescripcion) {
		this.pracId = pracId;
		this.pracDescripcion = pracDescripcion;
	}

	public PeriodoAcademico(int pracId, String pracDescripcion, Integer pracEstado, Date pracFechaIncio,
			Date pracFechaFin, List<CargaHoraria> pracCargaHorarias, List<Cronograma> pracCronogramas,
			List<Proyeccion> pracProyeccions, List<MallaPeriodo> pracMallaPeriodos,
			List<MallaCurricularParalelo> pracMallaCurricularParalelos, List<Paralelo> pracParalelos,
			List<FichaInscripcion> pracFichaInscripciones,
			List<SolicitudTerceraMatricula> pracSolicitudesTerceraMatricula) {
		this.pracId = pracId;
		this.pracDescripcion = pracDescripcion;
		this.pracEstado = pracEstado;
		this.pracFechaIncio = pracFechaIncio;
		this.pracFechaFin = pracFechaFin;
		this.pracCargaHorarias = pracCargaHorarias;
		this.pracCronogramas = pracCronogramas;
		this.pracProyeccions = pracProyeccions;
		this.pracMallaPeriodos = pracMallaPeriodos;
		this.pracParalelos = pracParalelos;
		this.pracFichaInscripciones = pracFichaInscripciones;
		this.pracSolicitudesTerceraMatricula = pracSolicitudesTerceraMatricula;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "PRAC_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getPracId() {
		return this.pracId;
	}

	public void setPracId(int pracId) {
		this.pracId = pracId;
	}

	@Column(name = "PRAC_DESCRIPCION", length = 256)
	@Length(max = 256)
	public String getPracDescripcion() {
		return this.pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	@Column(name = "PRAC_ESTADO", precision = 22, scale = 0)
	public Integer getPracEstado() {
		return this.pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PRAC_FECHA_INCIO", length = 7)
	public Date getPracFechaIncio() {
		return this.pracFechaIncio;
	}

	public void setPracFechaIncio(Date pracFechaIncio) {
		this.pracFechaIncio = pracFechaIncio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PRAC_FECHA_FIN", length = 7)
	public Date getPracFechaFin() {
		return this.pracFechaFin;
	}

	public void setPracFechaFin(Date pracFechaFin) {
		this.pracFechaFin = pracFechaFin;
	}

	@Column(name = "PRAC_TIPO")
	public Integer getPracTipo() {
		return pracTipo;
	}

	public void setPracTipo(Integer pracTipo) {
		this.pracTipo = pracTipo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crhrPeriodoAcademico")
	public List<CargaHoraria> getPracCargaHorarias() {
		return this.pracCargaHorarias;
	}

	public void setPracCargaHorarias(List<CargaHoraria> pracCargaHorarias) {
		this.pracCargaHorarias = pracCargaHorarias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crnPeriodoAcademico")
	public List<Cronograma> getPracCronogramas() {
		return this.pracCronogramas;
	}

	public void setPracCronogramas(List<Cronograma> pracCronogramas) {
		this.pracCronogramas = pracCronogramas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pryPeriodoAcademico")
	public List<Proyeccion> getPracProyeccions() {
		return this.pracProyeccions;
	}

	public void setPracProyeccions(List<Proyeccion> pracProyeccions) {
		this.pracProyeccions = pracProyeccions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mlprPeriodoAcademico")
	public List<MallaPeriodo> getPracMallaPeriodos() {
		return this.pracMallaPeriodos;
	}

	public void setPracMallaPeriodos(List<MallaPeriodo> pracMallaPeriodos) {
		this.pracMallaPeriodos = pracMallaPeriodos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prlPeriodoAcademico")
	public List<Paralelo> getPracParalelos() {
		return pracParalelos;
	}

	public void setPracParalelos(List<Paralelo> pracParalelos) {
		this.pracParalelos = pracParalelos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fcinPeriodoAcademico")
	public List<FichaInscripcion> getPracFichaInscripciones() {
		return pracFichaInscripciones;
	}

	public void setPracFichaInscripciones(List<FichaInscripcion> pracFichaInscripciones) {
		this.pracFichaInscripciones = pracFichaInscripciones;
	}

	@OneToMany(mappedBy = "periodoAcademico")
	public List<SistemaCalificacion> getSistemaCalificacions() {
		return this.sistemaCalificacions;
	}

	public void setSistemaCalificacions(List<SistemaCalificacion> sistemaCalificacions) {
		this.sistemaCalificacions = sistemaCalificacions;
	}

	// bi-directional many-to-one association to Evaluacion
	@OneToMany(mappedBy = "evPeriodoAcademico")
	public List<Evaluacion> getPracListEvaluacion() {
		return this.pracListEvaluacion;
	}

	public void setPracListEvaluacion(List<Evaluacion> pracListEvaluacion) {
		this.pracListEvaluacion = pracListEvaluacion;
	}

	public Evaluacion addPracListEvaluacion(Evaluacion pracListEvaluacion) {
		getPracListEvaluacion().add(pracListEvaluacion);
		pracListEvaluacion.setEvPeriodoAcademico(this);
		return pracListEvaluacion;
	}

	public Evaluacion removePracListEvaluacion(Evaluacion pracListEvaluacion) {
		getPracListEvaluacion().remove(pracListEvaluacion);
		pracListEvaluacion.setEvPeriodoAcademico(null);
		return pracListEvaluacion;
	}

	@OneToMany(mappedBy = "sltrmtPeriodoAcademico")
	public List<SolicitudTerceraMatricula> getPracSolicitudesTerceraMatricula() {
		return this.pracSolicitudesTerceraMatricula;
	}

	public void setPracSolicitudesTerceraMatricula(List<SolicitudTerceraMatricula> pracSolicitudesTerceraMatricula) {
		this.pracSolicitudesTerceraMatricula = pracSolicitudesTerceraMatricula;
	}

	@OneToMany(mappedBy = "roflgrPeriodoAcademico")
	public List<RolFlujoGrupo> getPracListRolFlujoGrupo() {
		return pracListRolFlujoGrupo;
	}

	public void setPracListRolFlujoGrupo(List<RolFlujoGrupo> pracListRolFlujoGrupo) {
		this.pracListRolFlujoGrupo = pracListRolFlujoGrupo;
	}

}
