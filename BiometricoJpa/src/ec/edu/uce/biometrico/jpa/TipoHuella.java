package ec.edu.uce.biometrico.jpa;
// Generated 22/01/2019 16:43:15 by Hibernate Tools 4.3.5.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TipoHuella generated by hbm2java
 */
@Entity
@Table(name = "TIPO_HUELLA")
@NamedQuery(name = "TipoHuella.listar", query = "select tp from TipoHuella as tp where tp.tphlId <> 0 order by tp.tphlId")
public class TipoHuella implements java.io.Serializable {

	private Integer tphlId;
	private String tphlDescripcionDedo;
	private Set<HuellaDactilar> huellaDactilars = new HashSet<HuellaDactilar>(0);

	public TipoHuella() {
	}

	public TipoHuella(Integer tphlId, String tphlDescripcionDedo) {
		this.tphlId = tphlId;
		this.tphlDescripcionDedo = tphlDescripcionDedo;
	}

	public TipoHuella(Integer tphlId, String tphlDescripcionDedo, Set<HuellaDactilar> huellaDactilars) {
		this.tphlId = tphlId;
		this.tphlDescripcionDedo = tphlDescripcionDedo;
		this.huellaDactilars = huellaDactilars;
	}

	@Id

	@Column(name = "TPHL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Integer getTphlId() {
		return this.tphlId;
	}

	public void setTphlId(Integer tphlId) {
		this.tphlId = tphlId;
	}

	@Column(name = "TPHL_DESCRIPCION_DEDO", nullable = false, length = 20)
	public String getTphlDescripcionDedo() {
		return this.tphlDescripcionDedo;
	}

	public void setTphlDescripcionDedo(String tphlDescripcionDedo) {
		this.tphlDescripcionDedo = tphlDescripcionDedo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hldcTipoHuella")
	public Set<HuellaDactilar> getHuellaDactilars() {
		return this.huellaDactilars;
	}

	public void setHuellaDactilars(Set<HuellaDactilar> huellaDactilars) {
		this.huellaDactilars = huellaDactilars;
	}

}
