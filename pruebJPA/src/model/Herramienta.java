package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the HERRAMIENTA database table.
 * 
 */
@Entity
@NamedQuery(name="Herramienta.findAll", query="SELECT h FROM Herramienta h")
public class Herramienta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="HRR_ID")
	private long hrrId;

	@Column(name="HRR_NOMBRE")
	private String hrrNombre;

	//bi-directional many-to-one association to Contenido
	@ManyToOne
	@JoinColumn(name="CNT_ID")
	private Contenido contenido;

	public Herramienta() {
	}

	public long getHrrId() {
		return this.hrrId;
	}

	public void setHrrId(long hrrId) {
		this.hrrId = hrrId;
	}

	public String getHrrNombre() {
		return this.hrrNombre;
	}

	public void setHrrNombre(String hrrNombre) {
		this.hrrNombre = hrrNombre;
	}

	public Contenido getContenido() {
		return this.contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

}