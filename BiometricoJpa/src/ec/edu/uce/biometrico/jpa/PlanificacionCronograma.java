package ec.edu.uce.biometrico.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the PLANIFICACION_CRONOGRAMA database table.
 * 
 */
@Entity
@Table(name = "PLANIFICACION_CRONOGRAMA")
public class PlanificacionCronograma implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer plcrId;
	private Integer plcrEstado;
	private Timestamp plcrFechaFin;
	private Timestamp plcrFechaInicio;
	private CronogramaProcesoFlujo plcrCronogramaProcesoFlujo;

	public PlanificacionCronograma() {
	}

	/**
	 * @param plcrId
	 */
	public PlanificacionCronograma(Integer plcrId) {
		super();
		this.plcrId = plcrId;
	}

	/**
	 * @param plcrId
	 * @param plcrEstado
	 * @param plcrFechaFin
	 * @param plcrFechaInicio
	 * @param cronogramaProcesoFlujo
	 */
	public PlanificacionCronograma(Integer plcrId, Integer plcrEstado, Timestamp plcrFechaFin,
			Timestamp plcrFechaInicio, CronogramaProcesoFlujo cronogramaProcesoFlujo) {
		super();
		this.plcrId = plcrId;
		this.plcrEstado = plcrEstado;
		this.plcrFechaFin = plcrFechaFin;
		this.plcrFechaInicio = plcrFechaInicio;
		this.plcrCronogramaProcesoFlujo = cronogramaProcesoFlujo;
	}

	@Id
	@Column(name = "PLCR_ID")
	public Integer getPlcrId() {
		return this.plcrId;
	}

	public void setPlcrId(Integer plcrId) {
		this.plcrId = plcrId;
	}

	@Version
	@Column(name = "PLCR_ESTADO")
	public Integer getPlcrEstado() {
		return this.plcrEstado;
	}

	public void setPlcrEstado(Integer plcrEstado) {
		this.plcrEstado = plcrEstado;
	}

	@Column(name = "PLCR_FECHA_FIN")
	public Timestamp getPlcrFechaFin() {
		return this.plcrFechaFin;
	}

	public void setPlcrFechaFin(Timestamp plcrFechaFin) {
		this.plcrFechaFin = plcrFechaFin;
	}

	@Column(name = "PLCR_FECHA_INICIO")
	public Timestamp getPlcrFechaInicio() {
		return this.plcrFechaInicio;
	}

	public void setPlcrFechaInicio(Timestamp plcrFechaInicio) {
		this.plcrFechaInicio = plcrFechaInicio;
	}

	// bi-directional many-to-one association to CronogramaProcesoFlujo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRPRFL_ID")
	public CronogramaProcesoFlujo getPlcrCronogramaProcesoFlujo() {
		return this.plcrCronogramaProcesoFlujo;
	}

	public void setPlcrCronogramaProcesoFlujo(CronogramaProcesoFlujo cronogramaProcesoFlujo) {
		this.plcrCronogramaProcesoFlujo = cronogramaProcesoFlujo;
	}

}