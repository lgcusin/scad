package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the FERIADO database table.
 * 
 */
@Entity
@Table(name = "FERIADO")
@NamedQuery(name = "Feriado.findAll", query = "SELECT f FROM Feriado f")
public class Feriado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FRD_ID")
	private long frdId;

	@Column(name = "FCL_ID")
	private Integer fclId;

	@Column(name = "FRD_DESCRIPCION")
	private String frdDescripcion;

	@Temporal(TemporalType.DATE)
	@Column(name = "FRD_FECHA")
	private Date frdFecha;

	@Column(name = "FRD_FIN")
	private String frdFin;

	@Column(name = "FRD_INICIO")
	private String frdInicio;

	public Feriado() {
	}

	public long getFrdId() {
		return this.frdId;
	}

	public void setFrdId(long frdId) {
		this.frdId = frdId;
	}

	public Integer getFclId() {
		return this.fclId;
	}

	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}

	public String getFrdDescripcion() {
		return this.frdDescripcion;
	}

	public void setFrdDescripcion(String frdDescripcion) {
		this.frdDescripcion = frdDescripcion;
	}

	public Date getFrdFecha() {
		return this.frdFecha;
	}

	public void setFrdFecha(Date frdFecha) {
		this.frdFecha = frdFecha;
	}

	public String getFrdFin() {
		return this.frdFin;
	}

	public void setFrdFin(String frdFin) {
		this.frdFin = frdFin;
	}

	public String getFrdInicio() {
		return this.frdInicio;
	}

	public void setFrdInicio(String frdInicio) {
		this.frdInicio = frdInicio;
	}

}