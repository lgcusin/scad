package model;
// Generated 22/01/2019 20:17:52 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HuellaDactilarId generated by hbm2java
 */
@Embeddable
public class HuellaDactilarId implements java.io.Serializable {

	private BigDecimal hldcId;
	private BigDecimal fcdcId;
	private BigDecimal tphlId;

	public HuellaDactilarId() {
	}

	public HuellaDactilarId(BigDecimal hldcId, BigDecimal fcdcId, BigDecimal tphlId) {
		this.hldcId = hldcId;
		this.fcdcId = fcdcId;
		this.tphlId = tphlId;
	}

	@Column(name = "HLDC_ID", nullable = false, precision = 22, scale = 0)
	public BigDecimal getHldcId() {
		return this.hldcId;
	}

	public void setHldcId(BigDecimal hldcId) {
		this.hldcId = hldcId;
	}

	@Column(name = "FCDC_ID", nullable = false, precision = 22, scale = 0)
	public BigDecimal getFcdcId() {
		return this.fcdcId;
	}

	public void setFcdcId(BigDecimal fcdcId) {
		this.fcdcId = fcdcId;
	}

	@Column(name = "TPHL_ID", nullable = false, precision = 22, scale = 0)
	public BigDecimal getTphlId() {
		return this.tphlId;
	}

	public void setTphlId(BigDecimal tphlId) {
		this.tphlId = tphlId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HuellaDactilarId))
			return false;
		HuellaDactilarId castOther = (HuellaDactilarId) other;

		return ((this.getHldcId() == castOther.getHldcId()) || (this.getHldcId() != null
				&& castOther.getHldcId() != null && this.getHldcId().equals(castOther.getHldcId())))
				&& ((this.getFcdcId() == castOther.getFcdcId()) || (this.getFcdcId() != null
						&& castOther.getFcdcId() != null && this.getFcdcId().equals(castOther.getFcdcId())))
				&& ((this.getTphlId() == castOther.getTphlId()) || (this.getTphlId() != null
						&& castOther.getTphlId() != null && this.getTphlId().equals(castOther.getTphlId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getHldcId() == null ? 0 : this.getHldcId().hashCode());
		result = 37 * result + (getFcdcId() == null ? 0 : this.getFcdcId().hashCode());
		result = 37 * result + (getTphlId() == null ? 0 : this.getTphlId().hashCode());
		return result;
	}

}
