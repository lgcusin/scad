package ec.edu.uce.academico.jpa.entidades.publico;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "DETALLE_PUESTO", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "DetallePuesto.findAll", query="SELECT c FROM DetallePuesto c"),
				@NamedQuery(name = "DetallePuesto.findPorFichaDocenteTipoCarrera", query = "SELECT c FROM DetallePuesto c WHERE c.dtpsFichaDocente.fcdcId = :fcdcId AND c.dtpsTipoCarrera = :tipoCarrera") })
public class DetallePuesto implements java.io.Serializable {

	private static final long serialVersionUID = -3306897083453546173L;
	private int dtpsId;
	private RelacionLaboral dtpsRelacionLaboral;
	private Puesto dtpsPuesto;
	private FichaEmpleado dtpsFichaEmpleado;
	private Carrera dtpsCarrera;
	private Integer dtpsTipoCarrera;
	private FichaDocente dtpsFichaDocente;
	private TipoDocumento dtpsTipoDocumento;
	private Integer dtpsEstado;
	private Integer dtpsEstadoCategoria;
	private Integer dtpsPracId;
	private Timestamp dtpsFechaRegistro;
	private String dtpsUsuario;
	private Integer dtpsProcesoRegistro;

	private List<DetalleTramiteContrato> dtpsDetalleTramiteContratos = new ArrayList<DetalleTramiteContrato>(0);
	private List<CargaHoraria> dtpsCargaHorarias = new ArrayList<CargaHoraria>(0);

	public DetallePuesto() {
	}

	public DetallePuesto(int dtpsId) {
		this.dtpsId = dtpsId;
	}

	public DetallePuesto(int dtpsId, RelacionLaboral dtpsRelacionLaboral, Puesto dtpsPuesto,
			FichaEmpleado dtpsFichaEmpleado, Carrera dtpsCarrera, FichaDocente dtpsFichaDocente,
			TipoDocumento dtpsTipoDocumento, Integer dtpsEstado,
			List<DetalleTramiteContrato> dtpsDetalleTramiteContratos, List<CargaHoraria> dtpsCargaHorarias) {
		this.dtpsId = dtpsId;
		this.dtpsRelacionLaboral = dtpsRelacionLaboral;
		this.dtpsPuesto = dtpsPuesto;
		this.dtpsFichaEmpleado = dtpsFichaEmpleado;
		this.dtpsCarrera = dtpsCarrera;
		this.dtpsFichaDocente = dtpsFichaDocente;
		this.dtpsTipoDocumento = dtpsTipoDocumento;
		this.dtpsEstado = dtpsEstado;
		this.dtpsDetalleTramiteContratos = dtpsDetalleTramiteContratos;
		this.dtpsCargaHorarias = dtpsCargaHorarias;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "DTPS_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getDtpsId() {
		return this.dtpsId;
	}

	public void setDtpsId(int dtpsId) {
		this.dtpsId = dtpsId;
	}

	@ManyToOne
	@JoinColumn(name = "RLLB_ID")
	public RelacionLaboral getDtpsRelacionLaboral() {
		return this.dtpsRelacionLaboral;
	}

	public void setDtpsRelacionLaboral(RelacionLaboral dtpsRelacionLaboral) {
		this.dtpsRelacionLaboral = dtpsRelacionLaboral;
	}

	@ManyToOne
	@JoinColumn(name = "PST_ID")
	public Puesto getDtpsPuesto() {
		return this.dtpsPuesto;
	}

	public void setDtpsPuesto(Puesto dtpsPuesto) {
		this.dtpsPuesto = dtpsPuesto;
	}

	@ManyToOne
	@JoinColumn(name = "FCEM_ID")
	public FichaEmpleado getDtpsFichaEmpleado() {
		return this.dtpsFichaEmpleado;
	}

	public void setDtpsFichaEmpleado(FichaEmpleado dtpsFichaEmpleado) {
		this.dtpsFichaEmpleado = dtpsFichaEmpleado;
	}

	@ManyToOne
	@JoinColumn(name = "CRR_ID")
	public Carrera getDtpsCarrera() {
		return this.dtpsCarrera;
	}

	public void setDtpsCarrera(Carrera dtpsCarrera) {
		this.dtpsCarrera = dtpsCarrera;
	}

	@ManyToOne
	@JoinColumn(name = "FCDC_ID")
	public FichaDocente getDtpsFichaDocente() {
		return this.dtpsFichaDocente;
	}

	public void setDtpsFichaDocente(FichaDocente dtpsFichaDocente) {
		this.dtpsFichaDocente = dtpsFichaDocente;
	}

	@ManyToOne
	@JoinColumn(name = "TIDC_ID")
	public TipoDocumento getDtpsTipoDocumento() {
		return this.dtpsTipoDocumento;
	}

	public void setDtpsTipoDocumento(TipoDocumento dtpsTipoDocumento) {
		this.dtpsTipoDocumento = dtpsTipoDocumento;
	}

	@Column(name = "DTPS_ESTADO", precision = 22, scale = 0)
	public Integer getDtpsEstado() {
		return this.dtpsEstado;
	}

	public void setDtpsEstado(Integer dtpsEstado) {
		this.dtpsEstado = dtpsEstado;
	}

	@Column(name = "DTPS_ESTADO_CATEGORIA", precision = 22, scale = 0)
	public Integer getDtpsEstadoCategoria() {
		return this.dtpsEstadoCategoria;
	}

	public void setDtpsEstadoCategoria(Integer dtpsEstadoCategoria) {
		this.dtpsEstadoCategoria = dtpsEstadoCategoria;
	}

	@Column(name = "DTPS_PRAC_ID")
	public Integer getDtpsPracId() {
		return dtpsPracId;
	}

	public void setDtpsPracId(Integer dtpsPracId) {
		this.dtpsPracId = dtpsPracId;
	}

	@Column(name = "DTPS_TIPO_CARRERA")
	public Integer getDtpsTipoCarrera() {
		return dtpsTipoCarrera;
	}

	public void setDtpsTipoCarrera(Integer dtpsTipoCarrera) {
		this.dtpsTipoCarrera = dtpsTipoCarrera;
	}

	@Column(name = "DTPS_FECHA_REGISTRO")
	public Timestamp getDtpsFechaRegistro() {
		return dtpsFechaRegistro;
	}

	public void setDtpsFechaRegistro(Timestamp dtpsFechaRegistro) {
		this.dtpsFechaRegistro = dtpsFechaRegistro;
	}

	@Column(name = "DTPS_USUARIO")
	public String getDtpsUsuario() {
		return dtpsUsuario;
	}

	public void setDtpsUsuario(String dtpsUsuario) {
		this.dtpsUsuario = dtpsUsuario;
	}

	@Column(name = "DTPS_PROCESO_REGISTRO")
	public Integer getDtpsProcesoRegistro() {
		return dtpsProcesoRegistro;
	}

	public void setDtpsProcesoRegistro(Integer dtpsProcesoRegistro) {
		this.dtpsProcesoRegistro = dtpsProcesoRegistro;
	}

	@OneToMany(mappedBy = "dttrcnDetallePuesto")
	public List<DetalleTramiteContrato> getDtpsDetalleTramiteContratos() {
		return this.dtpsDetalleTramiteContratos;
	}

	public void setDtpsDetalleTramiteContratos(List<DetalleTramiteContrato> dtpsDetalleTramiteContratos) {
		this.dtpsDetalleTramiteContratos = dtpsDetalleTramiteContratos;
	}

	@OneToMany(mappedBy = "crhrDetallePuesto")
	public List<CargaHoraria> getDtpsCargaHorarias() {
		return this.dtpsCargaHorarias;
	}

	public void setDtpsCargaHorarias(List<CargaHoraria> dtpsCargaHorarias) {
		this.dtpsCargaHorarias = dtpsCargaHorarias;
	}

}
