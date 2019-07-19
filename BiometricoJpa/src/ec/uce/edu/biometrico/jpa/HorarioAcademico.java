package ec.uce.edu.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final

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
 * HorarioAcademico generated by hbm2java
 */
@Entity
@Table(name = "HORARIO_ACADEMICO")
public class HorarioAcademico implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer hracId;
	private MallaCurricularParalelo mallaCurricularParalelo;
	private HoraClaseAula horaClaseAula;
	private String hracDescripcion;
	private Integer hracDia;
	private Integer hracHoraInicio;
	private Integer hracHoraFin;
	private Integer hracEstado;
	private Integer mlcrprIdComp;
	private Integer hracHoraTipo;
	private List<Asistencia > actividads = new ArrayList<Asistencia>();

	public HorarioAcademico() {
	}

	public HorarioAcademico(Integer hracId) {
		this.hracId = hracId;
	}

	public HorarioAcademico(Integer hracId, MallaCurricularParalelo mallaCurricularParalelo,
			HoraClaseAula horaClaseAula, String hracDescripcion, Integer hracDia, Integer hracHoraInicio,
			Integer hracHoraFin, Integer hracEstado, Integer mlcrprIdComp, Integer hracHoraTipo,List<Asistencia > actividads) {
		this.hracId = hracId;
		this.mallaCurricularParalelo = mallaCurricularParalelo;
		this.horaClaseAula = horaClaseAula;
		this.hracDescripcion = hracDescripcion;
		this.hracDia = hracDia;
		this.hracHoraInicio = hracHoraInicio;
		this.hracHoraFin = hracHoraFin;
		this.hracEstado = hracEstado;
		this.mlcrprIdComp = mlcrprIdComp;
		this.hracHoraTipo = hracHoraTipo;
		this.actividads=actividads;
	}

	@Id

	@Column(name = "HRAC_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getHracId() {
		return this.hracId;
	}

	public void setHracId(Integer hracId) {
		this.hracId = hracId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCRPR_ID")
	public MallaCurricularParalelo getMallaCurricularParalelo() {
		return this.mallaCurricularParalelo;
	}

	public void setMallaCurricularParalelo(MallaCurricularParalelo mallaCurricularParalelo) {
		this.mallaCurricularParalelo = mallaCurricularParalelo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOCLAL_ID")
	public HoraClaseAula getHoraClaseAula() {
		return this.horaClaseAula;
	}

	public void setHoraClaseAula(HoraClaseAula horaClaseAula) {
		this.horaClaseAula = horaClaseAula;
	}

	@Column(name = "HRAC_DESCRIPCION", length = 256)
	public String getHracDescripcion() {
		return this.hracDescripcion;
	}

	public void setHracDescripcion(String hracDescripcion) {
		this.hracDescripcion = hracDescripcion;
	}

	@Column(name = "HRAC_DIA", precision = 38, scale = 0)
	public Integer getHracDia() {
		return this.hracDia;
	}

	public void setHracDia(Integer hracDia) {
		this.hracDia = hracDia;
	}

	@Column(name = "HRAC_HORA_INICIO", precision = 38, scale = 0)
	public Integer getHracHoraInicio() {
		return this.hracHoraInicio;
	}

	public void setHracHoraInicio(Integer hracHoraInicio) {
		this.hracHoraInicio = hracHoraInicio;
	}

	@Column(name = "HRAC_HORA_FIN", precision = 38, scale = 0)
	public Integer getHracHoraFin() {
		return this.hracHoraFin;
	}

	public void setHracHoraFin(Integer hracHoraFin) {
		this.hracHoraFin = hracHoraFin;
	}

	@Column(name = "HRAC_ESTADO", precision = 38, scale = 0)
	public Integer getHracEstado() {
		return this.hracEstado;
	}

	public void setHracEstado(Integer hracEstado) {
		this.hracEstado = hracEstado;
	}

	@Column(name = "MLCRPR_ID_COMP", precision = 38, scale = 0)
	public Integer getMlcrprIdComp() {
		return this.mlcrprIdComp;
	}

	public void setMlcrprIdComp(Integer mlcrprIdComp) {
		this.mlcrprIdComp = mlcrprIdComp;
	}

	@Column(name = "HRAC_HORA_TIPO", precision = 22, scale = 0)
	public Integer getHracHoraTipo() {
		return this.hracHoraTipo;
	}

	public void setHracHoraTipo(Integer hracHoraTipo) {
		this.hracHoraTipo = hracHoraTipo;
	}

	/**
	 * @return the actividads
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "horarioAcademico")
	public List<Asistencia> getActividads() {
		return actividads;
	}

	/**
	 * @param actividads the actividads to set
	 */
	public void setActividads(List<Asistencia> actividads) {
		this.actividads = actividads;
	}
	
	

}
