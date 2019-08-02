package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

public class FuncionCargaHorariaDto implements Serializable {
	private static final long serialVersionUID = 7930717660150834598L;
	private int fncrhrId;
	private String fncrhrDescripcion;
	private Integer fncrhrHorasAsignadas;
	private Integer fncrhrHorasMinimo;
	private Integer fncrhrHorasMaximo;

	private TipoCargaHorariaDto fncrhrTipoCargaHorariaDto;

	public FuncionCargaHorariaDto() {
		super();
	}

	public int getFncrhrId() {
		return fncrhrId;
	}

	public void setFncrhrId(int fncrhrId) {
		this.fncrhrId = fncrhrId;
	}

	public String getFncrhrDescripcion() {
		return fncrhrDescripcion;
	}

	public void setFncrhrDescripcion(String fncrhrDescripcion) {
		this.fncrhrDescripcion = fncrhrDescripcion;
	}

	public Integer getFncrhrHorasAsignadas() {
		return fncrhrHorasAsignadas;
	}

	public void setFncrhrHorasAsignadas(Integer fncrhrHorasAsignadas) {
		this.fncrhrHorasAsignadas = fncrhrHorasAsignadas;
	}

	public Integer getFncrhrHorasMinimo() {
		return fncrhrHorasMinimo;
	}

	public void setFncrhrHorasMinimo(Integer fncrhrHorasMinimo) {
		this.fncrhrHorasMinimo = fncrhrHorasMinimo;
	}

	public Integer getFncrhrHorasMaximo() {
		return fncrhrHorasMaximo;
	}

	public void setFncrhrHorasMaximo(Integer fncrhrHorasMaximo) {
		this.fncrhrHorasMaximo = fncrhrHorasMaximo;
	}

	public TipoCargaHorariaDto getFncrhrTipoCargaHorariaDto() {
		return fncrhrTipoCargaHorariaDto;
	}

	public void setFncrhrTipoCargaHorariaDto(TipoCargaHorariaDto fncrhrTipoCargaHorariaDto) {
		this.fncrhrTipoCargaHorariaDto = fncrhrTipoCargaHorariaDto;
	}

	@Override
	public String toString() {
		return "FuncionCargaHorariaDto [fncrhrId=" + fncrhrId + ", fncrhrDescripcion=" + fncrhrDescripcion
				+ ", fncrhrHorasAsignadas=" + fncrhrHorasAsignadas + ", fncrhrHorasMinimo=" + fncrhrHorasMinimo
				+ ", fncrhrHorasMaximo=" + fncrhrHorasMaximo + ", fncrhrTipoCargaHorariaDto="
				+ fncrhrTipoCargaHorariaDto + "]";
	}

}
