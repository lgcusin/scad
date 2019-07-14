
package ec.uce.edu.biometrico.jpa;
// Generated 09/07/2019 16:21:52 by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Parametro generated by hbm2java
 */
@Entity
@Table(name = "PARAMETRO")
public class Parametro implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int prmId;
	private Dependencia dependencia;
	private String prmDescripcion;
	private String prmValor;

	public Parametro() {
	}

	public Parametro(int prmId, Dependencia dependencia) {
		this.prmId = prmId;
		this.dependencia = dependencia;
	}

	public Parametro(int prmId, Dependencia dependencia, String prmNombre, String prmValor) {
		this.prmId = prmId;
		this.dependencia = dependencia;
		this.prmDescripcion = prmNombre;
		this.prmValor = prmValor;
	}

	@Id

	@Column(name = "PRM_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getPrmId() {
		return this.prmId;
	}

	public void setPrmId(int prmId) {
		this.prmId = prmId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DPN_ID", nullable = false)
	public Dependencia getDependencia() {
		return this.dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	@Column(name = "PRM_DESCRIPCION", length = 30)
	public String getPrmDescripcion() {
		return this.prmDescripcion;
	}

	public void setPrmDescripcion(String prmNombre) {
		this.prmDescripcion = prmNombre;
	}

	@Column(name = "PRM_VALOR", length = 10)
	public String getPrmValor() {
		return this.prmValor;
	}

	public void setPrmValor(String prmValor) {
		this.prmValor = prmValor;
	}

}
