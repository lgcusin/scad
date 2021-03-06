package ec.edu.uce.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ec.edu.uce.biometrico.jpa.Cronograma;

/**
 * PeriodoAcademico generated by hbm2java
 */
@Entity
@Table(name = "PERIODO_ACADEMICO")
public class PeriodoAcademico implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;
	private Integer pracTipo;
	private Integer pracFkPracId;
	private List<Paralelo> paralelos = new ArrayList<Paralelo>(0);
	private List<CargaHoraria> cargaHorarias = new ArrayList<CargaHoraria>(0);
	private List<RolFlujoGrupo> rolFlujoGrupos = new ArrayList<RolFlujoGrupo>(0);
	private List<MallaPeriodo> mallaPeriodos = new ArrayList<MallaPeriodo>(0);
	private List<Cronograma> cronogramas = new ArrayList<Cronograma>(0);

	public PeriodoAcademico() {
	}

	public PeriodoAcademico(Integer pracId) {
		this.pracId = pracId;
	}

	public PeriodoAcademico(Integer pracId, String pracDescripcion, Integer pracEstado, Date pracFechaIncio,
			Date pracFechaFin, Integer pracTipo, Integer pracFkPracId, List<Paralelo> paralelos,
			List<CargaHoraria> cargaHorarias, List<RolFlujoGrupo> rolFlujoGrupos, List<MallaPeriodo> mallaPeriodos,
			List<Cronograma> cronogramas) {
		this.pracId = pracId;
		this.pracDescripcion = pracDescripcion;
		this.pracEstado = pracEstado;
		this.pracFechaIncio = pracFechaIncio;
		this.pracFechaFin = pracFechaFin;
		this.pracTipo = pracTipo;
		this.pracFkPracId = pracFkPracId;
		this.paralelos = paralelos;
		this.cargaHorarias = cargaHorarias;
		this.rolFlujoGrupos = rolFlujoGrupos;
		this.mallaPeriodos = mallaPeriodos;
		this.cronogramas = cronogramas;
	}

	@Id

	@Column(name = "PRAC_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getPracId() {
		return this.pracId;
	}

	public void setPracId(Integer pracId) {
		this.pracId = pracId;
	}

	@Column(name = "PRAC_DESCRIPCION", length = 256)
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

	@Column(name = "PRAC_TIPO", precision = 22, scale = 0)
	public Integer getPracTipo() {
		return this.pracTipo;
	}

	public void setPracTipo(Integer pracTipo) {
		this.pracTipo = pracTipo;
	}

	@Column(name = "PRAC_FK_PRAC_ID", precision = 22, scale = 0)
	public Integer getPracFkPracId() {
		return this.pracFkPracId;
	}

	public void setPracFkPracId(Integer pracFkPracId) {
		this.pracFkPracId = pracFkPracId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prlPeriodoAcademico")
	public List<Paralelo> getParalelos() {
		return this.paralelos;
	}

	public void setParalelos(List<Paralelo> paralelos) {
		this.paralelos = paralelos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crhrPeriodoAcademico")
	public List<CargaHoraria> getCargaHorarias() {
		return this.cargaHorarias;
	}

	public void setCargaHorarias(List<CargaHoraria> cargaHorarias) {
		this.cargaHorarias = cargaHorarias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodoAcademico")
	public List<RolFlujoGrupo> getRolFlujoGrupos() {
		return this.rolFlujoGrupos;
	}

	public void setRolFlujoGrupos(List<RolFlujoGrupo> rolFlujoGrupos) {
		this.rolFlujoGrupos = rolFlujoGrupos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mlprPeriodoAcademico")
	public List<MallaPeriodo> getMallaPeriodos() {
		return this.mallaPeriodos;
	}

	public void setMallaPeriodos(List<MallaPeriodo> mallaPeriodos) {
		this.mallaPeriodos = mallaPeriodos;
	}

	/**
	 * @return the cronogramas
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crnPeriodoAcademico")
	public List<Cronograma> getCronogramas() {
		return cronogramas;
	}

	/**
	 * @param cronogramas the cronogramas to set
	 */
	public void setCronogramas(List<Cronograma> cronogramas) {
		this.cronogramas = cronogramas;
	}
	
	

}
