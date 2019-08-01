package ec.uce.edu.biometrico.jpa;
// Generated 22/01/2019 16:43:15 by Hibernate Tools 4.3.5.Final

import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * HuellaDactilar generated by hbm2java
 */
@Entity
@Table(name = "HUELLA_DACTILAR", uniqueConstraints = @UniqueConstraint(columnNames = { "FCDC_ID", "TPHL_ID" }))
@NamedQueries({
		@NamedQuery(name = "HuellaDactilar.findAll", query = "select hd from HuellaDactilar as hd where hd.hldcCodigoAuxiliar<>1 or hd.tipoHuella.tphlId in (1, 2,3)"),
		@NamedQuery(name = "HuellaDactilar.findAllById", query = "select hd from HuellaDactilar as hd where hd.fichaDocente.fcdcId=:idDcnt"),
		@NamedQuery(name = "HuellaDactilar.findByFdicThid", query = "select hd from HuellaDactilar as hd where hd.fichaDocente.fcdcId=:fdId and hd.tipoHuella.tphlId=:thid") })

public class HuellaDactilar implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer hldcId;
	private TipoHuella tipoHuella;
	private FichaDocente fichaDocente;
	private Blob hldPrimerHuella;
	private Blob hldSegundaHuella;
	private Integer hldcCodigoAuxiliar;

	public HuellaDactilar() {
	}

	public HuellaDactilar(Integer hldcId, TipoHuella tipoHuella, FichaDocente fichaDocente) {
		this.hldcId = hldcId;
		this.tipoHuella = tipoHuella;
		this.fichaDocente = fichaDocente;
	}

	public HuellaDactilar(Integer hldcId, TipoHuella tipoHuella, FichaDocente fichaDocente, Blob hldPrimerHuella,
			Blob hldSegundaHuella, Integer hldcCodigoAuxiliar) {
		this.hldcId = hldcId;
		this.tipoHuella = tipoHuella;
		this.fichaDocente = fichaDocente;
		this.hldPrimerHuella = hldPrimerHuella;
		this.hldSegundaHuella = hldSegundaHuella;
		this.hldcCodigoAuxiliar = hldcCodigoAuxiliar;
	}

	@Id
	@Column(name = "HLDC_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getHldcId() {
		return this.hldcId;
	}

	public void setHldcId(Integer hldcId) {
		this.hldcId = hldcId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TPHL_ID", nullable = false)
	public TipoHuella getTipoHuella() {
		return this.tipoHuella;
	}

	public void setTipoHuella(TipoHuella tipoHuella) {
		this.tipoHuella = tipoHuella;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCDC_ID", nullable = false)
	public FichaDocente getFichaDocente() {
		return this.fichaDocente;
	}

	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
	}

	@Column(name = "HLD_PRIMER_HUELLA")
	public Blob getHldPrimerHuella() {
		return this.hldPrimerHuella;
	}

	public void setHldPrimerHuella(Blob hldPrimerHuella) {
		this.hldPrimerHuella = hldPrimerHuella;
	}

	@Column(name = "HLD_SEGUNDA_HUELLA")
	public Blob getHldSegundaHuella() {
		return this.hldSegundaHuella;
	}

	public void setHldSegundaHuella(Blob hldSegundaHuella) {
		this.hldSegundaHuella = hldSegundaHuella;
	}

	@Column(name = "HLDC_CODIGO_AUXILIAR", precision = 22, scale = 0)
	public Integer getHldcCodigoAuxiliar() {
		return this.hldcCodigoAuxiliar;
	}

	public void setHldcCodigoAuxiliar(Integer hldcCodigoAuxiliar) {
		this.hldcCodigoAuxiliar = hldcCodigoAuxiliar;
	}

}