/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     HorarioAcademicoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad HorarioAcademicoDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19-SEPT-2017 		  Dennis Collaguazo   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Clase (DTO) HorarioAcademicoDto. DTO encargado de manejar los datos de la
 * entidad HorarioAcademicoDto.
 * 
 * @author dcollaguazo.
 * @version 1.0
 */
public class ReportesGeneralesDto implements Serializable {

	private static final long serialVersionUID = -5768676106415490425L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla paralelo
	private int prlId;
	private String prlCodigo;
	private String prlDescripcion;
	private Integer prlEstado;
	private Integer prlCupo;
	private Integer prlCarrera;
	private Integer prlPeriodoAcademico;

	// Tabla mallaCurricularMateria
	private Integer mlcrmtId;
	private Integer mlcrmtNvlId;

	// Tabla mallaCurricularParalelo
	private Integer mlcrprId;
	private Integer mlcrprInscritos;
	private Integer mlcrprCupo;

	// Tabla periodoAcademico
	private Integer pracId;
	private String pracDescripcion;
	private Integer pracEstado;

	// Tabla Carrera
	private Integer crrId;
	private String crrDetalle;
	private String crrDescripcion;

	// Tabla Materia
	private Integer mtrId;
	private String mtrCodigo;
	private String mtrDescripcion;
	private Integer mtrEstado;
	private Integer mtrHoras;
	private Integer mtrCreditos;
	private Integer mtrHorasCien;

	// Tabla Nivel
	private Integer nvlId;
	private String nvlDescripcion;
	private Integer nvlNumeral;

	// Tabla Persona
	private Integer prsId;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsIdentificacion;

	// Tabla Aula
	private Integer alaId;
	private String alaCodigo;
	private String alaDescripcion;
	private String alaCapacidad;

	// Tabla Hora Clase
	private Integer hoclId;
	private String hoclDescripcion;
	private String hoclHoraInicio;
	private String hoclHoraFin;
	private Integer hoclHoclalId;
	private LocalTime hoclHoInicio;
	private LocalTime hoclHoFin;

	// Tabla Horario Academico
	private Integer hracId;
	private Integer hracDia;
	private String hracFechaInicio;
	private String hracFechaFin;
	private Integer hracHoclalId;
	private Integer hracMlcrprIdComp;

	// Tabla Dependencia
	private Integer dpnId;
	private String dpnDescripcion;

	// Tabla Edificio
	private Integer edfId;
	private String edfDescripcion;
	
	//Tabla Record Estudiante
	private Integer rcesId;
	private Integer rcesEstado;
	private Integer rcesMlcrprId;
	private Integer rcesNumeroMatriculados;
	private Integer rcesNumeroInscritos;

	private Integer sltrmtEstado;
	private Integer sltrmtTipo;
	
	
	public ReportesGeneralesDto() {

	}
	

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getPrlId() {
		return prlId;
	}

	public void setPrlId(int prlId) {
		this.prlId = prlId;
	}

	public String getPrlCodigo() {
		return prlCodigo;
	}

	public void setPrlCodigo(String prlCodigo) {
		this.prlCodigo = prlCodigo;
	}

	public String getPrlDescripcion() {
		return prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
	}

	public Integer getPrlEstado() {
		return prlEstado;
	}

	public void setPrlEstado(Integer prlEstado) {
		this.prlEstado = prlEstado;
	}

	public Integer getPrlCarrera() {
		return prlCarrera;
	}

	public void setPrlCarrera(Integer prlCarrera) {
		this.prlCarrera = prlCarrera;
	}

	public Integer getPrlPeriodoAcademico() {
		return prlPeriodoAcademico;
	}

	public void setPrlPeriodoAcademico(Integer prlPeriodoAcademico) {
		this.prlPeriodoAcademico = prlPeriodoAcademico;
	}

	public Integer getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(Integer mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Integer getMlcrmtNvlId() {
		return mlcrmtNvlId;
	}

	public void setMlcrmtNvlId(Integer mlcrmtNvlId) {
		this.mlcrmtNvlId = mlcrmtNvlId;
	}

	public Integer getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(Integer mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public Integer getPrlCupo() {
		return prlCupo;
	}

	public void setPrlCupo(Integer prlCupo) {
		this.prlCupo = prlCupo;
	}

	public Integer getMlcrprInscritos() {
		return mlcrprInscritos;
	}

	public void setMlcrprInscritos(Integer mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

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

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
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

	public String getNvlDescripcion() {
		return nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

	public Integer getMlcrprCupo() {
		return mlcrprCupo;
	}

	public void setMlcrprCupo(Integer mlcrprCupo) {
		this.mlcrprCupo = mlcrprCupo;
	}

	public Integer getNvlId() {
		return nvlId;
	}

	public void setNvlId(Integer nvlId) {
		this.nvlId = nvlId;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public Integer getMtrEstado() {
		return mtrEstado;
	}

	public void setMtrEstado(Integer mtrEstado) {
		this.mtrEstado = mtrEstado;
	}

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public Integer getMtrHorasCien() {
		return mtrHorasCien;
	}

	public void setMtrHorasCien(Integer mtrHorasCien) {
		this.mtrHorasCien = mtrHorasCien;
	}

	public Integer getPrsId() {
		return prsId;
	}

	public void setPrsId(Integer prsId) {
		this.prsId = prsId;
	}

	public String getPrsPrimerApellido() {
		return prsPrimerApellido;
	}

	public void setPrsPrimerApellido(String prsPrimerApellido) {
		this.prsPrimerApellido = prsPrimerApellido;
	}

	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}

	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}

	public String getPrsNombres() {
		return prsNombres;
	}

	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}

	public Integer getAlaId() {
		return alaId;
	}

	public void setAlaId(Integer alaId) {
		this.alaId = alaId;
	}

	public String getAlaCodigo() {
		return alaCodigo;
	}

	public void setAlaCodigo(String alaCodigo) {
		this.alaCodigo = alaCodigo;
	}

	public String getAlaDescripcion() {
		return alaDescripcion;
	}

	public void setAlaDescripcion(String alaDescripcion) {
		this.alaDescripcion = alaDescripcion;
	}

	public String getAlaCapacidad() {
		return alaCapacidad;
	}

	public void setAlaCapacidad(String alaCapacidad) {
		this.alaCapacidad = alaCapacidad;
	}

	public Integer getHoclId() {
		return hoclId;
	}

	public void setHoclId(Integer hoclId) {
		this.hoclId = hoclId;
	}

	public String getHoclDescripcion() {
		return hoclDescripcion;
	}

	public void setHoclDescripcion(String hoclDescripcion) {
		this.hoclDescripcion = hoclDescripcion;
	}

	public String getHoclHoraInicio() {
		return hoclHoraInicio;
	}

	public void setHoclHoraInicio(String hoclHoraInicio) {
		this.hoclHoraInicio = hoclHoraInicio;
	}

	public String getHoclHoraFin() {
		return hoclHoraFin;
	}

	public void setHoclHoraFin(String hoclHoraFin) {
		this.hoclHoraFin = hoclHoraFin;
	}

	public Integer getHracId() {
		return hracId;
	}

	public void setHracId(Integer hracId) {
		this.hracId = hracId;
	}

	public Integer getHracDia() {
		return hracDia;
	}

	public void setHracDia(Integer hracDia) {
		this.hracDia = hracDia;
	}

	public String getHracFechaInicio() {
		return hracFechaInicio;
	}

	public void setHracFechaInicio(String hracFechaInicio) {
		this.hracFechaInicio = hracFechaInicio;
	}

	public String getHracFechaFin() {
		return hracFechaFin;
	}

	public void setHracFechaFin(String hracFechaFin) {
		this.hracFechaFin = hracFechaFin;
	}

	public Integer getHoclHoclalId() {
		return hoclHoclalId;
	}

	public void setHoclHoclalId(Integer hoclHoclalId) {
		this.hoclHoclalId = hoclHoclalId;
	}

	public Integer getNvlNumeral() {
		return nvlNumeral;
	}

	public void setNvlNumeral(Integer nvlNumeral) {
		this.nvlNumeral = nvlNumeral;
	}

	public Integer getDpnId() {
		return dpnId;
	}

	public void setDpnId(Integer dpnId) {
		this.dpnId = dpnId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}

	public Integer getEdfId() {
		return edfId;
	}

	public void setEdfId(Integer edfId) {
		this.edfId = edfId;
	}

	public String getEdfDescripcion() {
		return edfDescripcion;
	}

	public void setEdfDescripcion(String edfDescripcion) {
		this.edfDescripcion = edfDescripcion;
	}
	public Integer getMtrCreditos() {
		return mtrCreditos;
	}
	public void setMtrCreditos(Integer mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}

	public LocalTime getHoclHoInicio() {
		return hoclHoInicio;
	}

	public void setHoclHoInicio(LocalTime hoclHoInicio) {
		this.hoclHoInicio = hoclHoInicio;
	}

	public LocalTime getHoclHoFin() {
		return hoclHoFin;
	}

	public void setHoclHoFin(LocalTime hoclHoFin) {
		this.hoclHoFin = hoclHoFin;
	}

	public Integer getHracHoclalId() {
		return hracHoclalId;
	}

	public void setHracHoclalId(Integer hracHoclalId) {
		this.hracHoclalId = hracHoclalId;
	}

	public Integer getHracMlcrprIdComp() {
		return hracMlcrprIdComp;
	}

	public void setHracMlcrprIdComp(Integer hracMlcrprIdComp) {
		this.hracMlcrprIdComp = hracMlcrprIdComp;
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

	public Integer getRcesMlcrprId() {
		return rcesMlcrprId;
	}

	public void setRcesMlcrprId(Integer rcesMlcrprId) {
		this.rcesMlcrprId = rcesMlcrprId;
	}

	public Integer getRcesNumeroMatriculados() {
		return rcesNumeroMatriculados;
	}

	public void setRcesNumeroMatriculados(Integer rcesNumeroMatriculados) {
		this.rcesNumeroMatriculados = rcesNumeroMatriculados;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}
	
		
	public Integer getRcesNumeroInscritos() {
		return rcesNumeroInscritos;
	}


	public void setRcesNumeroInscritos(Integer rcesNumeroInscritos) {
		this.rcesNumeroInscritos = rcesNumeroInscritos;
	}


	public Integer getSltrmtEstado() {
		return sltrmtEstado;
	}


	public void setSltrmtEstado(Integer sltrmtEstado) {
		this.sltrmtEstado = sltrmtEstado;
	}


	public Integer getSltrmtTipo() {
		return sltrmtTipo;
	}


	public void setSltrmtTipo(Integer sltrmtTipo) {
		this.sltrmtTipo = sltrmtTipo;
	}


	@Override
	public String toString() {
		return "ReportesGeneralesDto [prlId=" + prlId + ", prlCodigo=" + prlCodigo + ", prlDescripcion="
				+ prlDescripcion + ", prlEstado=" + prlEstado + ", prlCupo=" + prlCupo + ", prlCarrera=" + prlCarrera
				+ ", prlPeriodoAcademico=" + prlPeriodoAcademico + ", mlcrmtId=" + mlcrmtId + ", mlcrmtNvlId="
				+ mlcrmtNvlId + ", mlcrprId=" + mlcrprId + ", mlcrprInscritos=" + mlcrprInscritos + ", mlcrprCupo="
				+ mlcrprCupo + ", pracId=" + pracId + ", pracDescripcion=" + pracDescripcion + ", pracEstado="
				+ pracEstado + ", crrId=" + crrId + ", crrDetalle=" + crrDetalle + ", crrDescripcion=" + crrDescripcion
				+ ", mtrId=" + mtrId + ", mtrCodigo=" + mtrCodigo + ", mtrDescripcion=" + mtrDescripcion
				+ ", mtrEstado=" + mtrEstado + ", mtrHoras=" + mtrHoras + ", mtrCreditos=" + mtrCreditos
				+ ", mtrHorasCien=" + mtrHorasCien + ", nvlId=" + nvlId + ", nvlDescripcion=" + nvlDescripcion
				+ ", nvlNumeral=" + nvlNumeral + ", prsId=" + prsId + ", prsPrimerApellido=" + prsPrimerApellido
				+ ", prsSegundoApellido=" + prsSegundoApellido + ", prsNombres=" + prsNombres + ", prsIdentificacion="
				+ prsIdentificacion + ", alaId=" + alaId + ", alaCodigo=" + alaCodigo + ", alaDescripcion="
				+ alaDescripcion + ", alaCapacidad=" + alaCapacidad + ", hoclId=" + hoclId + ", hoclDescripcion="
				+ hoclDescripcion + ", hoclHoraInicio=" + hoclHoraInicio + ", hoclHoraFin=" + hoclHoraFin
				+ ", hoclHoclalId=" + hoclHoclalId + ", hoclHoInicio=" + hoclHoInicio + ", hoclHoFin=" + hoclHoFin
				+ ", hracId=" + hracId + ", hracDia=" + hracDia + ", hracFechaInicio=" + hracFechaInicio
				+ ", hracFechaFin=" + hracFechaFin + ", hracHoclalId=" + hracHoclalId + ", hracMlcrprIdComp="
				+ hracMlcrprIdComp + ", dpnId=" + dpnId + ", dpnDescripcion=" + dpnDescripcion + ", edfId=" + edfId
				+ ", edfDescripcion=" + edfDescripcion + ", rcesId=" + rcesId + ", rcesEstado=" + rcesEstado
				+ ", rcesMlcrprId=" + rcesMlcrprId + ", rcesNumeroMatriculados=" + rcesNumeroMatriculados
				+ ", rcesNumeroInscritos=" + rcesNumeroInscritos + ", sltrmtEstado=" + sltrmtEstado + ", sltrmtTipo="
				+ sltrmtTipo + "]";
	}
	


}
