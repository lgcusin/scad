package ec.uce.edu.biometrico.jpa;
// Generated 08/07/2019 20:53:07 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TipoCargaHoraria generated by hbm2java
 */
@Entity
@Table(name = "TIPO_CARGA_HORARIA")
public class TipoCargaHoraria implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ticrhrId;
	private String ticrhrDescripcion;
	private Integer ticrhrEstado;
	private List<TipoFuncionCargaHoraria> tipoFuncionCargaHorarias = new ArrayList<TipoFuncionCargaHoraria>(0);

	public TipoCargaHoraria() {
	}

	public TipoCargaHoraria(Integer ticrhrId) {
		this.ticrhrId = ticrhrId;
	}

	public TipoCargaHoraria(Integer ticrhrId, String ticrhrDescripcion, Integer ticrhrEstado,
			List<TipoFuncionCargaHoraria> tipoFuncionCargaHorarias) {
		this.ticrhrId = ticrhrId;
		this.ticrhrDescripcion = ticrhrDescripcion;
		this.ticrhrEstado = ticrhrEstado;
		this.tipoFuncionCargaHorarias = tipoFuncionCargaHorarias;
	}

	@Id

	@Column(name = "TICRHR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getTicrhrId() {
		return this.ticrhrId;
	}

	public void setTicrhrId(Integer ticrhrId) {
		this.ticrhrId = ticrhrId;
	}

	@Column(name = "TICRHR_DESCRIPCION", length = 128)
	public String getTicrhrDescripcion() {
		return this.ticrhrDescripcion;
	}

	public void setTicrhrDescripcion(String ticrhrDescripcion) {
		this.ticrhrDescripcion = ticrhrDescripcion;
	}

	@Column(name = "TICRHR_ESTADO", precision = 22, scale = 0)
	public Integer getTicrhrEstado() {
		return this.ticrhrEstado;
	}

	public void setTicrhrEstado(Integer ticrhrEstado) {
		this.ticrhrEstado = ticrhrEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoCargaHoraria")
	public List<TipoFuncionCargaHoraria> getTipoFuncionCargaHorarias() {
		return this.tipoFuncionCargaHorarias;
	}

	public void setTipoFuncionCargaHorarias(List<TipoFuncionCargaHoraria> tipoFuncionCargaHorarias) {
		this.tipoFuncionCargaHorarias = tipoFuncionCargaHorarias;
	}

}