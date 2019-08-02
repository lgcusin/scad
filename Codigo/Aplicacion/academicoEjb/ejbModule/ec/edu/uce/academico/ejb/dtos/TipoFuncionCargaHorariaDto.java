package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.util.List;

import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = TipoFuncionCargaHorariaDto.class)
public class TipoFuncionCargaHorariaDto implements Serializable {

	private static final long serialVersionUID = -7048177996691124583L;
	private int tifncrhrId;
	private TipoCargaHorariaDto tifncrhrTipoCargaHorariaDto;
	private FuncionCargaHorariaDto tifncrhrFuncionCargaHorariaDto;
	private Integer tifncrhrEstado;
	private Integer tifncrhrHoraSeleccionada;
	private Boolean tifncrhrIsCheck;
	private Boolean tifncrhrDisabledIsCheck;
	private List<Integer> tifncrhrListHoras;

	public TipoFuncionCargaHorariaDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getTifncrhrId() {
		return tifncrhrId;
	}

	public void setTifncrhrId(int tifncrhrId) {
		this.tifncrhrId = tifncrhrId;
	}

	public TipoCargaHorariaDto getTifncrhrTipoCargaHorariaDto() {
		return tifncrhrTipoCargaHorariaDto;
	}

	public void setTifncrhrTipoCargaHorariaDto(TipoCargaHorariaDto tifncrhrTipoCargaHorariaDto) {
		this.tifncrhrTipoCargaHorariaDto = tifncrhrTipoCargaHorariaDto;
	}

	public FuncionCargaHorariaDto getTifncrhrFuncionCargaHorariaDto() {
		return tifncrhrFuncionCargaHorariaDto;
	}

	public void setTifncrhrFuncionCargaHorariaDto(FuncionCargaHorariaDto tifncrhrFuncionCargaHorariaDto) {
		this.tifncrhrFuncionCargaHorariaDto = tifncrhrFuncionCargaHorariaDto;
	}

	public Integer getTifncrhrEstado() {
		return tifncrhrEstado;
	}

	public void setTifncrhrEstado(Integer tifncrhrEstado) {
		this.tifncrhrEstado = tifncrhrEstado;
	}

	public List<Integer> getTifncrhrListHoras() {
		return tifncrhrListHoras;
	}

	public void setTifncrhrListHoras(List<Integer> tifncrhrListHoras) {
		this.tifncrhrListHoras = tifncrhrListHoras;
	}

	public Boolean getTifncrhrIsCheck() {
		return tifncrhrIsCheck;
	}

	public void setTifncrhrIsCheck(Boolean tifncrhrIsCheck) {
		this.tifncrhrIsCheck = tifncrhrIsCheck;
	}

	public Integer getTifncrhrHoraSeleccionada() {
		return tifncrhrHoraSeleccionada;
	}

	public void setTifncrhrHoraSeleccionada(Integer tifncrhrHoraSeleccionada) {
		this.tifncrhrHoraSeleccionada = tifncrhrHoraSeleccionada;
	}

	public Boolean getTifncrhrDisabledIsCheck() {
		return tifncrhrDisabledIsCheck;
	}

	public void setTifncrhrDisabledIsCheck(Boolean tifncrhrDisabledIsCheck) {
		this.tifncrhrDisabledIsCheck = tifncrhrDisabledIsCheck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tifncrhrId;
		result = prime * result + ((tifncrhrIsCheck == null) ? 0 : tifncrhrIsCheck.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		TipoFuncionCargaHorariaDto other = (TipoFuncionCargaHorariaDto) obj;
		if (tifncrhrId != other.tifncrhrId)
			return false;
		if (tifncrhrIsCheck == null) {
			if (other.tifncrhrIsCheck != null)
				return false;
		} else if (!tifncrhrIsCheck.equals(other.tifncrhrIsCheck))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TipoFuncionCargaHorariaDto [tifncrhrId=" + tifncrhrId + ", tifncrhrIsCheck=" + tifncrhrIsCheck + "]";
	}

}
