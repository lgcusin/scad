package ec.edu.uce.biometrico.jpa;
// Generated 09/07/2019 16:21:52 by Hibernate Tools 4.3.5.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Feriado generated by hbm2java
 */
@Entity
@Table(name = "FERIADO")
public class Feriado implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer frdId;
	private Carrera frdCarrera;
	private String frdDescripcion;
	private Date frdFecha;
	private String frdFin;
	private String frdInicio;

	public Feriado() {
	}

	public Feriado(Integer frdId, Carrera carrera) {
		this.frdId = frdId;
		this.frdCarrera = carrera;
	}

	public Feriado(Integer frdId, Carrera carrera, String frdDescripcion, Date frdFecha, String frdFin,
			String frdInicio) {
		this.frdId = frdId;
		this.frdCarrera = carrera;
		this.frdDescripcion = frdDescripcion;
		this.frdFecha = frdFecha;
		this.frdFin = frdFin;
		this.frdInicio = frdInicio;
	}

	@Id

	@Column(name = "FRD_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getFrdId() {
		return this.frdId;
	}

	public void setFrdId(Integer frdId) {
		this.frdId = frdId;
	}

	
	/**
	 * @return the carrera
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID", nullable = false)
	public Carrera getFrdCarrera() {
		return frdCarrera;
	}

	/**
	 * @param carrera
	 *            the carrera to set
	 */
	public void setFrdCarrera(Carrera carrera) {
		this.frdCarrera = carrera;
	}

	@Column(name = "FRD_DESCRIPCION", length = 30)
	public String getFrdDescripcion() {
		return this.frdDescripcion;
	}

	public void setFrdDescripcion(String frdDescripcion) {
		this.frdDescripcion = frdDescripcion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FRD_FECHA", length = 7)
	public Date getFrdFecha() {
		return this.frdFecha;
	}

	public void setFrdFecha(Date frdFecha) {
		this.frdFecha = frdFecha;
	}

	@Column(name = "FRD_FIN", length = 10)
	public String getFrdFin() {
		return this.frdFin;
	}

	public void setFrdFin(String frdFin) {
		this.frdFin = frdFin;
	}

	@Column(name = "FRD_INICIO", length = 10)
	public String getFrdInicio() {
		return this.frdInicio;
	}

	public void setFrdInicio(String frdInicio) {
		this.frdInicio = frdInicio;
	}

}
