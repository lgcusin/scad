package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

public class TipoCargaHorariaDto implements Serializable {

	private static final long serialVersionUID = -2882186096250782027L;
	private int ticrhrId;
	private String ticrhrDescripcion;
	private Integer ticrhrEstado;

	public TipoCargaHorariaDto() {
		super();
	}

	public int getTicrhrId() {
		return ticrhrId;
	}

	public void setTicrhrId(int ticrhrId) {
		this.ticrhrId = ticrhrId;
	}

	public String getTicrhrDescripcion() {
		return ticrhrDescripcion;
	}

	public void setTicrhrDescripcion(String ticrhrDescripcion) {
		this.ticrhrDescripcion = ticrhrDescripcion;
	}

	public Integer getTicrhrEstado() {
		return ticrhrEstado;
	}

	public void setTicrhrEstado(Integer ticrhrEstado) {
		this.ticrhrEstado = ticrhrEstado;
	}

	@Override
	public String toString() {
		return "TipoCargaHorariaDto [ticrhrId=" + ticrhrId + ", ticrhrDescripcion=" + ticrhrDescripcion
				+ ", ticrhrEstado=" + ticrhrEstado + "]";
	}

}
