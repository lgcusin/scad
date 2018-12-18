package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MATERIA database table.
 * 
 */
@Entity
@NamedQuery(name="Materia.findAll", query="SELECT m FROM Materia m")
public class Materia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MT_ID")
	private long mtId;

	@Column(name="MT_NOMBRE")
	private String mtNombre;

	public Materia() {
	}

	public long getMtId() {
		return this.mtId;
	}

	public void setMtId(long mtId) {
		this.mtId = mtId;
	}

	public String getMtNombre() {
		return this.mtNombre;
	}

	public void setMtNombre(String mtNombre) {
		this.mtNombre = mtNombre;
	}

}