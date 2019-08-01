package ec.uce.edu.biometrico.jpa;
// Generated 14/02/2019 12:12:32 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * FichaEmpleado generated by hbm2java
 */
@Entity
@Table(name = "FICHA_EMPLEADO")
// @NamedQueries({
// @NamedQuery(name = "Empleado.findByUsrId", query = "select u.fichaEmpleado
// from Usuario as u where u.ursId=:usrId") })
public class FichaEmpleado implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer fcemId;
	private Persona persona;
	private List<DetallePuesto> detallePuestos = new ArrayList<DetallePuesto>(0);

	public FichaEmpleado() {
	}

	public FichaEmpleado(Integer fcemId) {
		this.fcemId = fcemId;
	}

	public FichaEmpleado(Integer fcemId, Persona persona, List<DetallePuesto> detallePuestos) {
		this.fcemId = fcemId;
		this.persona = persona;
		this.detallePuestos = detallePuestos;
	}

	@Id

	@Column(name = "FCEM_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getFcemId() {
		return this.fcemId;
	}

	public void setFcemId(Integer fcemId) {
		this.fcemId = fcemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRS_ID")
	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaEmpleado")
	public List<DetallePuesto> getDetallePuestos() {
		return this.detallePuestos;
	}

	public void setDetallePuestos(List<DetallePuesto> detallePuestos) {
		this.detallePuestos = detallePuestos;
	}

}