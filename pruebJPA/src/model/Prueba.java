package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the PRUEBA database table.
 * 
 */
@Entity
@NamedQuery(name="Prueba.findAll", query="SELECT p FROM Prueba p")
@Table(name = "PRUEBA")
public class Prueba implements Serializable {
	private static final long serialVersionUID = 1L;

	private String apellidos;

	@Column(name="FCDC_ID")
	@Id
	private BigDecimal fcdcId;

	@Column(name="PRIMER_NOMBRE")
	
	private String primerNombre;

	@Column(name="SEGUNDO_NOMBRE")
	private String segundoNombre;

	public Prueba() {
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public BigDecimal getFcdcId() {
		return this.fcdcId;
	}

	public void setFcdcId(BigDecimal fcdcId) {
		this.fcdcId = fcdcId;
	}

	public String getPrimerNombre() {
		return this.primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return this.segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

}