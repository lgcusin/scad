package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the DETALLE_PUESTO database table.
 * 
 */
@Entity
@Table(name="DETALLE_PUESTO")
@NamedQuery(name="DetallePuesto.findAll", query="SELECT d FROM DetallePuesto d")
public class DetallePuesto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DTPS_ID")
	private long dtpsId;

	@Column(name="DTPS_ESTADO")
	private BigDecimal dtpsEstado;

	//bi-directional many-to-one association to Carrera
	@ManyToOne
	@JoinColumn(name="CRR_ID")
	private Carrera carrera;

	//bi-directional many-to-one association to FichaDocente
	@ManyToOne
	@JoinColumn(name="FCDC_ID")
	private FichaDocente fichaDocente;

	public DetallePuesto() {
	}

	public long getDtpsId() {
		return this.dtpsId;
	}

	public void setDtpsId(long dtpsId) {
		this.dtpsId = dtpsId;
	}

	public BigDecimal getDtpsEstado() {
		return this.dtpsEstado;
	}

	public void setDtpsEstado(BigDecimal dtpsEstado) {
		this.dtpsEstado = dtpsEstado;
	}

	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public FichaDocente getFichaDocente() {
		return this.fichaDocente;
	}

	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
	}

}