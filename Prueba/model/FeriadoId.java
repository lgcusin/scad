package model;
// Generated 22/06/2019 19:40:53 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FeriadoId generated by hbm2java
 */
@Embeddable
public class FeriadoId implements java.io.Serializable {

	private BigDecimal frdId;
	private String frdDescripcion;
	private Date frdFecha;
	private String frdFin;
	private String frdInicio;
	private BigDecimal fclId;

	public FeriadoId() {
	}

	public FeriadoId(BigDecimal frdId, BigDecimal fclId) {
		this.frdId = frdId;
		this.fclId = fclId;
	}

	public FeriadoId(BigDecimal frdId, String frdDescripcion, Date frdFecha, String frdFin, String frdInicio,
			BigDecimal fclId) {
		this.frdId = frdId;
		this.frdDescripcion = frdDescripcion;
		this.frdFecha = frdFecha;
		this.frdFin = frdFin;
		this.frdInicio = frdInicio;
		this.fclId = fclId;
	}

	@Column(name = "FRD_ID", nullable = false, precision = 22, scale = 0)
	public BigDecimal getFrdId() {
		return this.frdId;
	}

	public void setFrdId(BigDecimal frdId) {
		this.frdId = frdId;
	}

	@Column(name = "FRD_DESCRIPCION", length = 30)
	public String getFrdDescripcion() {
		return this.frdDescripcion;
	}

	public void setFrdDescripcion(String frdDescripcion) {
		this.frdDescripcion = frdDescripcion;
	}

	@Column(name = "FRD_FECHA", length = 7)
	public Date getFrdFecha() {
		return this.frdFecha;
	}

	public void setFrdFecha(Date frdFecha) {
		this.frdFecha = frdFecha;
	}

	@Column(name = "FRD_FIN", length = 10)
	public String getFrdFin() {
		return this.frdFin;
	}

	public void setFrdFin(String frdFin) {
		this.frdFin = frdFin;
	}

	@Column(name = "FRD_INICIO", length = 10)
	public String getFrdInicio() {
		return this.frdInicio;
	}

	public void setFrdInicio(String frdInicio) {
		this.frdInicio = frdInicio;
	}

	@Column(name = "FCL_ID", nullable = false, precision = 22, scale = 0)
	public BigDecimal getFclId() {
		return this.fclId;
	}

	public void setFclId(BigDecimal fclId) {
		this.fclId = fclId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FeriadoId))
			return false;
		FeriadoId castOther = (FeriadoId) other;

		return ((this.getFrdId() == castOther.getFrdId()) || (this.getFrdId() != null && castOther.getFrdId() != null
				&& this.getFrdId().equals(castOther.getFrdId())))
				&& ((this.getFrdDescripcion() == castOther.getFrdDescripcion())
						|| (this.getFrdDescripcion() != null && castOther.getFrdDescripcion() != null
								&& this.getFrdDescripcion().equals(castOther.getFrdDescripcion())))
				&& ((this.getFrdFecha() == castOther.getFrdFecha()) || (this.getFrdFecha() != null
						&& castOther.getFrdFecha() != null && this.getFrdFecha().equals(castOther.getFrdFecha())))
				&& ((this.getFrdFin() == castOther.getFrdFin()) || (this.getFrdFin() != null
						&& castOther.getFrdFin() != null && this.getFrdFin().equals(castOther.getFrdFin())))
				&& ((this.getFrdInicio() == castOther.getFrdInicio()) || (this.getFrdInicio() != null
						&& castOther.getFrdInicio() != null && this.getFrdInicio().equals(castOther.getFrdInicio())))
				&& ((this.getFclId() == castOther.getFclId()) || (this.getFclId() != null
						&& castOther.getFclId() != null && this.getFclId().equals(castOther.getFclId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getFrdId() == null ? 0 : this.getFrdId().hashCode());
		result = 37 * result + (getFrdDescripcion() == null ? 0 : this.getFrdDescripcion().hashCode());
		result = 37 * result + (getFrdFecha() == null ? 0 : this.getFrdFecha().hashCode());
		result = 37 * result + (getFrdFin() == null ? 0 : this.getFrdFin().hashCode());
		result = 37 * result + (getFrdInicio() == null ? 0 : this.getFrdInicio().hashCode());
		result = 37 * result + (getFclId() == null ? 0 : this.getFclId().hashCode());
		return result;
	}

}
