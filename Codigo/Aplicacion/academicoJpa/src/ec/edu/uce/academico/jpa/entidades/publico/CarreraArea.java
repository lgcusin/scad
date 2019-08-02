package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * The persistent class for the CALIFICACION database table.
 * 
 */
@Entity
@Table(name="CARRERA_AREA", schema = ConstantesJpa.SCHEMA)
public class CarreraArea implements Serializable {


	private static final long serialVersionUID = -776703625538679119L;

	private int crarId;
	
	
	private Carrera crarCarrera;
	private Carrera crarArea;
	

	public CarreraArea() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="CRAR_ID", unique=true, nullable=false, precision=6, scale = 0)
	public int getCrarId() {
		return this.crarId;
	}

	public void setCrarId(int crarId) {
		this.crarId = crarId;
	}

	
	@ManyToOne
	@JoinColumn(name="CRR_ID")
	public Carrera getCrarCarrera() {
		return this.crarCarrera;
	}

	public void setCrarCarrera(Carrera crarCarrera) {
		this.crarCarrera = crarCarrera;
	}
	@ManyToOne
	@JoinColumn(name="AREA_ID")
	public Carrera getCrarArea() {
		return this.crarArea;
	}

	public void setCrarArea(Carrera crarArea) {
		this.crarArea = crarArea;
	}
	
	
	
}