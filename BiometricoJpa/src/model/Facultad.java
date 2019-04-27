package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the FACULTAD database table.
 * 
 */
@Entity
@Table(name = "FACULTAD")
@NamedQuery(name = "Facultad.findAll", query = "SELECT f FROM Facultad f")
public class Facultad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FCL_ID")
	private Integer fclId;

	@Column(name = "FCL_NOMBRE")
	private String fclNombre;

	public Facultad() {
	}

	public Integer getFclId() {
		return fclId;
	}

	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}

	public String getFclNombre() {
		return this.fclNombre;
	}

	public void setFclNombre(String fclNombre) {
		this.fclNombre = fclNombre;
	}

}