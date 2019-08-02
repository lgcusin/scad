package ec.edu.uce.academico.ejb.dtos;

public class ProcesoFlujoDto implements java.io.Serializable {

	private static final long serialVersionUID = -7303387286065810359L;
	private int prflId;
	private String prflDescripcion;
	private Integer prflEstado;

	public ProcesoFlujoDto() {
	}

	public int getPrflId() {
		return prflId;
	}

	public void setPrflId(int prflId) {
		this.prflId = prflId;
	}

	public String getPrflDescripcion() {
		return prflDescripcion;
	}

	public void setPrflDescripcion(String prflDescripcion) {
		this.prflDescripcion = prflDescripcion;
	}

	public Integer getPrflEstado() {
		return prflEstado;
	}

	public void setPrflEstado(Integer prflEstado) {
		this.prflEstado = prflEstado;
	}

	@Override
	public String toString() {
		return "ProcesoFlujoDto [prflId=" + prflId + ", prflDescripcion=" + prflDescripcion + ", prflEstado="
				+ prflEstado + "]";
	}

}
