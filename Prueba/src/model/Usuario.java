package model;
// Generated 20/03/2019 20:46:57 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Usuario generated by hbm2java
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements java.io.Serializable {

	private BigDecimal ursId;
	private FichaDocente fichaDocente;
	private FichaEmpleado fichaEmpleado;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;

	public Usuario() {
	}

	public Usuario(BigDecimal ursId) {
		this.ursId = ursId;
	}

	public Usuario(BigDecimal ursId, FichaDocente fichaDocente, FichaEmpleado fichaEmpleado, String usrIdentificacion,
			String usrNick, String usrPassword) {
		this.ursId = ursId;
		this.fichaDocente = fichaDocente;
		this.fichaEmpleado = fichaEmpleado;
		this.usrIdentificacion = usrIdentificacion;
		this.usrNick = usrNick;
		this.usrPassword = usrPassword;
	}

	@Id

	@Column(name = "URS_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getUrsId() {
		return this.ursId;
	}

	public void setUrsId(BigDecimal ursId) {
		this.ursId = ursId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCDC_ID")
	public FichaDocente getFichaDocente() {
		return this.fichaDocente;
	}

	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCEM_ID")
	public FichaEmpleado getFichaEmpleado() {
		return this.fichaEmpleado;
	}

	public void setFichaEmpleado(FichaEmpleado fichaEmpleado) {
		this.fichaEmpleado = fichaEmpleado;
	}

	@Column(name = "USR_IDENTIFICACION", length = 13)
	public String getUsrIdentificacion() {
		return this.usrIdentificacion;
	}

	public void setUsrIdentificacion(String usrIdentificacion) {
		this.usrIdentificacion = usrIdentificacion;
	}

	@Column(name = "USR_NICK", length = 20)
	public String getUsrNick() {
		return this.usrNick;
	}

	public void setUsrNick(String usrNick) {
		this.usrNick = usrNick;
	}

	@Column(name = "USR_PASSWORD", length = 14)
	public String getUsrPassword() {
		return this.usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

}
