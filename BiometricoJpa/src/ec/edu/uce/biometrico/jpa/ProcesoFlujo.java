package ec.edu.uce.biometrico.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the PROCESO_FLUJO database table.
 * 
 */
@Entity
@Table(name = "PROCESO_FLUJO")
public class ProcesoFlujo implements Serializable {
	private static final long serialVersionUID = 1L;
	private long prflId;
	private String prflDescripcion;
	private Integer prflEstado;
	private List<CronogramaProcesoFlujo> cronogramaProcesoFlujos;

	public ProcesoFlujo() {
	}

	public ProcesoFlujo(long prflId) {
		super();
		this.prflId = prflId;
	}

	public ProcesoFlujo(long prflId, String prflDescripcion, Integer prflEstado,
			List<CronogramaProcesoFlujo> cronogramaProcesoFlujos) {
		super();
		this.prflId = prflId;
		this.prflDescripcion = prflDescripcion;
		this.prflEstado = prflEstado;
		this.cronogramaProcesoFlujos = cronogramaProcesoFlujos;
	}

	@Id
	@Column(name = "PRFL_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public long getPrflId() {
		return this.prflId;
	}

	public void setPrflId(long prflId) {
		this.prflId = prflId;
	}

	@Column(name = "PRFL_DESCRIPCION", length = 512)
	public String getPrflDescripcion() {
		return this.prflDescripcion;
	}

	public void setPrflDescripcion(String prflDescripcion) {
		this.prflDescripcion = prflDescripcion;
	}

	@Column(name = "PRFL_ESTADO", precision = 38, scale = 0)
	public Integer getPrflEstado() {
		return this.prflEstado;
	}

	public void setPrflEstado(Integer prflEstado) {
		this.prflEstado = prflEstado;
	}

	// bi-directional many-to-one association to CronogramaProcesoFlujo
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crprProcesoFlujo")
	public List<CronogramaProcesoFlujo> getCronogramaProcesoFlujos() {
		return this.cronogramaProcesoFlujos;
	}

	public void setCronogramaProcesoFlujos(List<CronogramaProcesoFlujo> cronogramaProcesoFlujos) {
		this.cronogramaProcesoFlujos = cronogramaProcesoFlujos;
	}

}