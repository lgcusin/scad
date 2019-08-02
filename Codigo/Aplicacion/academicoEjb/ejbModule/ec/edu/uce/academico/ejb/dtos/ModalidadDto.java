package ec.edu.uce.academico.ejb.dtos;

public class ModalidadDto {
	private int mdlId;
	private String mdlDescripcion;

	public ModalidadDto() {
		super();
	}

	public ModalidadDto(int mdlId) {
		super();
		this.mdlId = mdlId;
	}

	public ModalidadDto(int mdlId, String mdlDescripcion) {
		super();
		this.mdlId = mdlId;
		this.mdlDescripcion = mdlDescripcion;
	}

	public int getMdlId() {
		return mdlId;
	}

	public void setMdlId(int mdlId) {
		this.mdlId = mdlId;
	}

	public String getMdlDescripcion() {
		return mdlDescripcion;
	}

	public void setMdlDescripcion(String mdlDescripcion) {
		this.mdlDescripcion = mdlDescripcion;
	}

}
