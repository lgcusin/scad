package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the FICHA_DOCENTE database table.
 * 
 */
@Entity
@Table(name="FICHA_DOCENTE")
@NamedQuery(name="FichaDocente.findAll", query="SELECT f FROM FichaDocente f")
public class FichaDocente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FCDC_ID")
	private long fcdcId;

	@Column(name="FCDC_APELLIDOS")
	private String fcdcApellidos;

	@Column(name="FCDC_IDENTIFICACION")
	private BigDecimal fcdcIdentificacion;

	@Column(name="FCDC_PRIMER_NOMBRE")
	private String fcdcPrimerNombre;

	@Column(name="FCDC_SEGUNDO_NOMBRE")
	private String fcdcSegundoNombre;

	//bi-directional many-to-one association to DetallePuesto
	@OneToMany(mappedBy="fichaDocente")
	private List<DetallePuesto> detallePuestos;

	public FichaDocente() {
	}

	public long getFcdcId() {
		return this.fcdcId;
	}

	public void setFcdcId(long fcdcId) {
		this.fcdcId = fcdcId;
	}

	public String getFcdcApellidos() {
		return this.fcdcApellidos;
	}

	public void setFcdcApellidos(String fcdcApellidos) {
		this.fcdcApellidos = fcdcApellidos;
	}

	public BigDecimal getFcdcIdentificacion() {
		return this.fcdcIdentificacion;
	}

	public void setFcdcIdentificacion(BigDecimal fcdcIdentificacion) {
		this.fcdcIdentificacion = fcdcIdentificacion;
	}

	public String getFcdcPrimerNombre() {
		return this.fcdcPrimerNombre;
	}

	public void setFcdcPrimerNombre(String fcdcPrimerNombre) {
		this.fcdcPrimerNombre = fcdcPrimerNombre;
	}

	public String getFcdcSegundoNombre() {
		return this.fcdcSegundoNombre;
	}

	public void setFcdcSegundoNombre(String fcdcSegundoNombre) {
		this.fcdcSegundoNombre = fcdcSegundoNombre;
	}

	public List<DetallePuesto> getDetallePuestos() {
		return this.detallePuestos;
	}

	public void setDetallePuestos(List<DetallePuesto> detallePuestos) {
		this.detallePuestos = detallePuestos;
	}

	public DetallePuesto addDetallePuesto(DetallePuesto detallePuesto) {
		getDetallePuestos().add(detallePuesto);
		detallePuesto.setFichaDocente(this);

		return detallePuesto;
	}

	public DetallePuesto removeDetallePuesto(DetallePuesto detallePuesto) {
		getDetallePuestos().remove(detallePuesto);
		detallePuesto.setFichaDocente(null);

		return detallePuesto;
	}

}