package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ACTIVIDAD database table.
 * 
 */
@Entity
@NamedQuery(name="Actividad.findAll", query="SELECT a FROM Actividad a")
public class Actividad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ACT_ID")
	private long actId;

	//bi-directional many-to-one association to Contenido
	@ManyToOne
	@JoinColumn(name="CNT_ID")
	private Contenido contenido;

	public Actividad() {
	}

	public long getActId() {
		return this.actId;
	}

	public void setActId(long actId) {
		this.actId = actId;
	}

	public Contenido getContenido() {
		return this.contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

}