package model;
// Generated 11/04/2019 22:33:42 by Hibernate Tools 4.3.5.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Facultad generated by hbm2java
 */
@Entity
@Table(name = "FACULTAD")
public class Facultad implements java.io.Serializable {

	private Integer fclId;
	private String fclNombre;
	private Set<Carrera> carreras = new HashSet<Carrera>(0);

	public Facultad() {
	}

	public Facultad(Integer fclId) {
		this.fclId = fclId;
	}

	public Facultad(Integer fclId, String fclNombre, Set<Carrera> carreras) {
		this.fclId = fclId;
		this.fclNombre = fclNombre;
		this.carreras = carreras;
	}

	@Id

	@Column(name = "FCL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getFclId() {
		return this.fclId;
	}

	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}

	@Column(name = "FCL_NOMBRE", length = 50)
	public String getFclNombre() {
		return this.fclNombre;
	}

	public void setFclNombre(String fclNombre) {
		this.fclNombre = fclNombre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "facultad")
	public Set<Carrera> getCarreras() {
		return this.carreras;
	}

	public void setCarreras(Set<Carrera> carreras) {
		this.carreras = carreras;
	}

}
