package ec.uce.edu.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DetallePuesto generated by hbm2java
 */
@Entity
@Table(name = "DETALLE_PUESTO")
public class DetallePuesto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer dtpsId;
	private Puesto puesto;
	private Carrera carrera;
	private FichaDocente fichaDocente;
	private FichaEmpleado fichaEmpleado;
	private Integer dtpsEstado;
	private Integer dtpsEstadoCategoria;
	private Integer dtpsPracId;
	private Integer dtpsTipoCarrera;
	private String dtpsUsuario;
	private Integer dtpsProcesoRegistro;
	private List<CargaHoraria> cargaHorarias = new ArrayList<CargaHoraria>(0);

	public DetallePuesto() {
	}

	public DetallePuesto(Integer dtpsId, Carrera carrera) {
		this.dtpsId = dtpsId;
		this.carrera = carrera;
	}

	public DetallePuesto(Integer dtpsId, Puesto puesto, Carrera carrera, FichaDocente fichaDocente,
			FichaEmpleado fichaEmpleado, Integer dtpsEstado, Integer dtpsEstadoCategoria, Integer dtpsPracId,
			Integer dtpsTipoCarrera, String dtpsUsuario, Integer dtpsProcesoRegistro,
			List<CargaHoraria> cargaHorarias) {
		this.dtpsId = dtpsId;
		this.puesto = puesto;
		this.carrera = carrera;
		this.fichaDocente = fichaDocente;
		this.fichaEmpleado = fichaEmpleado;
		this.dtpsEstado = dtpsEstado;
		this.dtpsEstadoCategoria = dtpsEstadoCategoria;
		this.dtpsPracId = dtpsPracId;
		this.dtpsTipoCarrera = dtpsTipoCarrera;
		this.dtpsUsuario = dtpsUsuario;
		this.dtpsProcesoRegistro = dtpsProcesoRegistro;
		this.cargaHorarias = cargaHorarias;
	}

	@Id

	@Column(name = "DTPS_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Integer getDtpsId() {
		return this.dtpsId;
	}

	public void setDtpsId(Integer dtpsId) {
		this.dtpsId = dtpsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PST_ID")
	public Puesto getPuesto() {
		return this.puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID", nullable = false)
	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCDC_ID")
	public FichaDocente getFichaDocente() {
		return this.fichaDocente;
	}

	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCEM_ID")
	public FichaEmpleado getFichaEmpleado() {
		return this.fichaEmpleado;
	}

	public void setFichaEmpleado(FichaEmpleado fichaEmpleado) {
		this.fichaEmpleado = fichaEmpleado;
	}

	@Column(name = "DTPS_ESTADO", precision = 38, scale = 0)
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

	@Column(name = "DTPS_PRAC_ID", precision = 6, scale = 0)
	public Integer getDtpsPracId() {
		return this.dtpsPracId;
	}

	public void setDtpsPracId(Integer dtpsPracId) {
		this.dtpsPracId = dtpsPracId;
	}

	@Column(name = "DTPS_TIPO_CARRERA", precision = 6, scale = 0)
	public Integer getDtpsTipoCarrera() {
		return this.dtpsTipoCarrera;
	}

	public void setDtpsTipoCarrera(Integer dtpsTipoCarrera) {
		this.dtpsTipoCarrera = dtpsTipoCarrera;
	}

	@Column(name = "DTPS_USUARIO", length = 64)
	public String getDtpsUsuario() {
		return this.dtpsUsuario;
	}

	public void setDtpsUsuario(String dtpsUsuario) {
		this.dtpsUsuario = dtpsUsuario;
	}

	@Column(name = "DTPS_PROCESO_REGISTRO", precision = 6, scale = 0)
	public Integer getDtpsProcesoRegistro() {
		return this.dtpsProcesoRegistro;
	}

	public void setDtpsProcesoRegistro(Integer dtpsProcesoRegistro) {
		this.dtpsProcesoRegistro = dtpsProcesoRegistro;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "detallePuesto")
	public List<CargaHoraria> getCargaHorarias() {
		return this.cargaHorarias;
	}

	public void setCargaHorarias(List<CargaHoraria> cargaHorarias) {
		this.cargaHorarias = cargaHorarias;
	}

}