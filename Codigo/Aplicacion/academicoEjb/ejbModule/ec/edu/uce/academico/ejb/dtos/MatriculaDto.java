package ec.edu.uce.academico.ejb.dtos;

import java.util.Date;

public class MatriculaDto {
	private Integer pracId;
	private String pracDescripcion;
	private Integer crrId;
	private String crrDescripcion;
	private Integer mtrId;
	private String mtrDescripcion;
	private Integer fcmtId;
	private Date fcmtFechaMatricula;
	private Integer dtmtId;
	private String dtmtNumeroMatricula;
	private Integer rcesId;
	private Integer rcesEstado;
	private Integer mlcrprId;
	private Integer fcinId;
	private PersonaDto mtrPersonaDto;

	public Integer getPracId() {
		return pracId;
	}

	public void setPracId(Integer pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public Integer getMtrId() {
		return mtrId;
	}

	public void setMtrId(Integer mtrId) {
		this.mtrId = mtrId;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public Integer getFcmtId() {
		return fcmtId;
	}

	public void setFcmtId(Integer fcmtId) {
		this.fcmtId = fcmtId;
	}

	public Date getFcmtFechaMatricula() {
		return fcmtFechaMatricula;
	}

	public void setFcmtFechaMatricula(Date fcmtFechaMatricula) {
		this.fcmtFechaMatricula = fcmtFechaMatricula;
	}

	public Integer getDtmtId() {
		return dtmtId;
	}

	public void setDtmtId(Integer dtmtId) {
		this.dtmtId = dtmtId;
	}

	public String getDtmtNumeroMatricula() {
		return dtmtNumeroMatricula;
	}

	public void setDtmtNumeroMatricula(String dtmtNumeroMatricula) {
		this.dtmtNumeroMatricula = dtmtNumeroMatricula;
	}

	public Integer getRcesId() {
		return rcesId;
	}

	public void setRcesId(Integer rcesId) {
		this.rcesId = rcesId;
	}

	public Integer getRcesEstado() {
		return rcesEstado;
	}

	public void setRcesEstado(Integer rcesEstado) {
		this.rcesEstado = rcesEstado;
	}

	public Integer getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(Integer mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public PersonaDto getMtrPersonaDto() {
		if (mtrPersonaDto == null) {
			mtrPersonaDto = new PersonaDto();
		}
		return mtrPersonaDto;
	}

	public void setMtrPersonaDto(PersonaDto mtrPersonaDto) {
		this.mtrPersonaDto = mtrPersonaDto;
	}

	public Integer getFcinId() {
		return fcinId;
	}

	public void setFcinId(Integer fcinId) {
		this.fcinId = fcinId;
	}

}
