package ec.edu.uce.biometrico.jpa;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Seguimiento generated by hbm2java
 */
@Entity
@Table(name = "SEGUIMIENTO")
public class Seguimiento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer sgmId;
	private Asistencia asistencia;
	private MallaCurricularParalelo sgmMallaCurricularParalelo;
	private ContenidoCurricular sgmContenidoCurricular;
	private Integer sgmHoraClaseRestante;
	private String sgmEstado;
	private String sgmObservacion;

	public Seguimiento() {
	}

	public Seguimiento(Integer sgmId, Asistencia asistencia) {
		this.sgmId = sgmId;
		this.asistencia = asistencia;
	}

	public Seguimiento(Integer sgmId, Asistencia asistencia, MallaCurricularParalelo mallaCurricularParalelo,
			ContenidoCurricular contenidoCurricular, Integer sgmHoraClaseRestante, String sgmEstado,
			String sgmObservacion) {
		this.sgmId = sgmId;
		this.asistencia = asistencia;
		this.sgmMallaCurricularParalelo = mallaCurricularParalelo;
		this.sgmContenidoCurricular = contenidoCurricular;
		this.sgmHoraClaseRestante = sgmHoraClaseRestante;
		this.sgmEstado = sgmEstado;
		this.sgmObservacion = sgmObservacion;
	}

	@Id

	@Column(name = "SGM_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getSgmId() {
		return this.sgmId;
	}

	public void setSgmId(Integer sgmId) {
		this.sgmId = sgmId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASS_ID", nullable = false)
	public Asistencia getAsistencia() {
		return this.asistencia;
	}

	public void setAsistencia(Asistencia asistencia) {
		this.asistencia = asistencia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCRPR_ID", nullable = false)
	public MallaCurricularParalelo getSgmMallaCurricularParalelo() {
		return this.sgmMallaCurricularParalelo;
	}

	public void setSgmMallaCurricularParalelo(MallaCurricularParalelo mallaCurricularParalelo) {
		this.sgmMallaCurricularParalelo = mallaCurricularParalelo;
	}

	/**
	 * @return the contenidoCurricular
	 */
	@ManyToOne
	@JoinColumn(name = "CNCR_ID", nullable = false)
	public ContenidoCurricular getSgmContenidoCurricular() {
		return sgmContenidoCurricular;
	}

	/**
	 * @param contenidoCurricular
	 *            the contenidoCurricular to set
	 */
	public void setSgmContenidoCurricular(ContenidoCurricular contenidoCurricular) {
		this.sgmContenidoCurricular = contenidoCurricular;
	}

	@Column(name = "SGM_HORA_CLASE_RESTANTE", precision = 22, scale = 0)
	public Integer getSgmHoraClaseRestante() {
		return this.sgmHoraClaseRestante;
	}

	public void setSgmHoraClaseRestante(Integer sgmHoraClaseRestante) {
		this.sgmHoraClaseRestante = sgmHoraClaseRestante;
	}

	/**
	 * @return the sgmEstado
	 */
	@Column(name = "SGM_ESTADO", length = 15)
	public String getSgmEstado() {
		return sgmEstado;
	}

	/**
	 * @param sgmEstado
	 *            the sgmEstado to set
	 */
	public void setSgmEstado(String sgmEstado) {
		this.sgmEstado = sgmEstado;
	}

	@Column(name = "SGM_OBSERVACION", length = 400)
	public String getSgmObservacion() {
		return this.sgmObservacion;
	}

	public void setSgmObservacion(String sgmObservacion) {
		this.sgmObservacion = sgmObservacion;
	}

}
