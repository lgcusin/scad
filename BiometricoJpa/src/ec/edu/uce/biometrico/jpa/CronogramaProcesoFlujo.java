package ec.edu.uce.biometrico.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * The persistent class for the CRONOGRAMA_PROCESO_FLUJO database table.
 * 
 */
@Entity
@Table(name = "CRONOGRAMA_PROCESO_FLUJO")
public class CronogramaProcesoFlujo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer crprflId;
	private Integer crprflOrdinal;
	private Cronograma crprCronograma;
	private ProcesoFlujo crprProcesoFlujo;
	private List<PlanificacionCronograma> planificacionCronogramas = new ArrayList<PlanificacionCronograma>(0);

	public CronogramaProcesoFlujo() {
	}

	public CronogramaProcesoFlujo(Integer crprflId) {
		super();
		this.crprflId = crprflId;
	}

	

	/**
	 * @param crprflId
	 * @param crprflOrdinal
	 * @param cronograma
	 * @param procesoFlujo
	 * @param planificacionCronogramas
	 */
	public CronogramaProcesoFlujo(Integer crprflId, Integer crprflOrdinal, Cronograma cronograma,
			ProcesoFlujo procesoFlujo, List<PlanificacionCronograma> planificacionCronogramas) {
		super();
		this.crprflId = crprflId;
		this.crprflOrdinal = crprflOrdinal;
		this.crprCronograma = cronograma;
		this.crprProcesoFlujo = procesoFlujo;
		this.planificacionCronogramas = planificacionCronogramas;
	}

	@Id
	@Column(name = "CRPRFL_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getCrprflId() {
		return this.crprflId;
	}

	public void setCrprflId(Integer crprflId) {
		this.crprflId = crprflId;
	}

	@Column(name = "CRPRFL_ORDINAL")
	public Integer getCrprflOrdinal() {
		return this.crprflOrdinal;
	}

	public void setCrprflOrdinal(Integer crprflOrdinal) {
		this.crprflOrdinal = crprflOrdinal;
	}

	// bi-directional many-to-one association to Cronograma
	@ManyToOne
	@JoinColumn(name = "CRN_ID")
	public Cronograma getCrprCronograma() {
		return this.crprCronograma;
	}

	public void setCrprCronograma(Cronograma cronograma) {
		this.crprCronograma = cronograma;
	}

	// bi-directional many-to-one association to ProcesoFlujo
	@ManyToOne
	@JoinColumn(name = "PRFL_ID")
	public ProcesoFlujo getCrprProcesoFlujo() {
		return this.crprProcesoFlujo;
	}

	public void setCrprProcesoFlujo(ProcesoFlujo procesoFlujo) {
		this.crprProcesoFlujo = procesoFlujo;
	}

	/**
	 * @return the planificacionCronogramas
	 */
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "plcrCronogramaProcesoFlujo")
	public List<PlanificacionCronograma> getPlanificacionCronogramas() {
		return planificacionCronogramas;
	}

	/**
	 * @param planificacionCronogramas the planificacionCronogramas to set
	 */
	public void setPlanificacionCronogramas(List<PlanificacionCronograma> planificacionCronogramas) {
		this.planificacionCronogramas = planificacionCronogramas;
	}
	

}