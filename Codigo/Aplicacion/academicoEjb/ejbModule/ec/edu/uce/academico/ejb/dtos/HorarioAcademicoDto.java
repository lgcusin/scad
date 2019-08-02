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
public class HorarioAcademicoDto implements Serializable {

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
	private Integer mlcrmtEstado;

	// Tabla mallaCurricularParalelo
	private Integer mlcrprId;
	private Integer mlcrprInscritos;
	private Integer mlcrprCupo;

	// Tabla mallaCurricular
	private Integer mlcrId;

	// Tabla

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
	private String mtrDscHora;
	private String mtrDscLunes;
	private String mtrDscMartes;
	private String mtrDscMiercoles;
	private String mtrDscJueves;
	private String mtrDscViernes;
	private String mtrDscSabado;
	private Integer mtrTimtId;

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
	private PersonaDto hracPersonaDto;

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

	private String hracDiaLabel;

	// Tabla Dependencia
	private Integer dpnId;
	private String dpnDescripcion;

	// Tabla Edificio
	private Integer edfId;
	private String edfDescripcion;

	// Tabla Record Estudiante
	private Integer rcesId;
	private Integer rcesEstado;
	private Integer rcesMlcrprId;
	private Integer rcesNumeroMatriculados;

	public HorarioAcademicoDto() {

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

	public String getHracDiaLabel() {
		return hracDiaLabel;
	}

	public void setHracDiaLabel(String hracDiaLabel) {
		this.hracDiaLabel = hracDiaLabel;
	}

	public String getMtrDscHora() {
		return mtrDscHora;
	}

	public void setMtrDscHora(String mtrDscHora) {
		this.mtrDscHora = mtrDscHora;
	}

	public String getMtrDscLunes() {
		return mtrDscLunes;
	}

	public void setMtrDscLunes(String mtrDscLunes) {
		this.mtrDscLunes = mtrDscLunes;
	}

	public String getMtrDscMartes() {
		return mtrDscMartes;
	}

	public void setMtrDscMartes(String mtrDscMartes) {
		this.mtrDscMartes = mtrDscMartes;
	}

	public String getMtrDscMiercoles() {
		return mtrDscMiercoles;
	}

	public void setMtrDscMiercoles(String mtrDscMiercoles) {
		this.mtrDscMiercoles = mtrDscMiercoles;
	}

	public String getMtrDscJueves() {
		return mtrDscJueves;
	}

	public void setMtrDscJueves(String mtrDscJueves) {
		this.mtrDscJueves = mtrDscJueves;
	}

	public String getMtrDscViernes() {
		return mtrDscViernes;
	}

	public void setMtrDscViernes(String mtrDscViernes) {
		this.mtrDscViernes = mtrDscViernes;
	}

	public String getMtrDscSabado() {
		return mtrDscSabado;
	}

	public void setMtrDscSabado(String mtrDscSabado) {
		this.mtrDscSabado = mtrDscSabado;
	}

	public PersonaDto getHracPersonaDto() {
		return hracPersonaDto;
	}

	public void setHracPersonaDto(PersonaDto hracPersonaDto) {
		this.hracPersonaDto = hracPersonaDto;
	}

	public Integer getMlcrmtEstado() {
		return mlcrmtEstado;
	}

	public void setMlcrmtEstado(Integer mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	public Integer getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(Integer mlcrId) {
		this.mlcrId = mlcrId;
	}

	public Integer getMtrTimtId() {
		return mtrTimtId;
	}

	public void setMtrTimtId(Integer mtrTimtId) {
		this.mtrTimtId = mtrTimtId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();

		sb.append(" Paralelo ");
		sb.append(tabulador + "prlId : " + prlId);
		sb.append(tabulador + "prlCodigo : " + (prlCodigo == null ? "NULL" : prlCodigo));
		sb.append(tabulador + "prlDescripcion : " + (prlDescripcion == null ? "NULL" : prlDescripcion));
		sb.append(tabulador + "prlEstado : " + (prlEstado == null ? "NULL" : prlEstado));
		sb.append(tabulador + "prlCupo : " + (prlCupo == null ? "NULL" : prlCupo));
		sb.append(tabulador + "prlCarrera : " + (prlCarrera == null ? "NULL" : prlCarrera));
		sb.append(tabulador + "prlPeriodoAcademico : " + (prlPeriodoAcademico == null ? "NULL" : prlPeriodoAcademico));

		sb.append(" MallaCurricularMateria ");
		sb.append(tabulador + "mlcrmtId : " + mlcrmtId);
		sb.append(tabulador + "mlcrmtNvlId : " + mlcrmtNvlId);

		sb.append(" MallaCurricularParalelo ");
		sb.append(tabulador + "mlcrprId : " + mlcrprId);
		sb.append(tabulador + "mlcrprInscritos : " + (mlcrprInscritos == null ? "NULL" : mlcrprInscritos));

		sb.append(" PeriodoAcademico");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));

		sb.append(" Carrera");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));

		sb.append(" Materia");
		sb.append(tabulador + "mtrId : " + mtrId);
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
		sb.append(tabulador + "mtrCodigo : " + (mtrCodigo == null ? "NULL" : mtrCodigo));
		sb.append(tabulador + "mtrEstado : " + (mtrEstado == null ? "NULL" : mtrEstado));
		sb.append(tabulador + "mtrHoras : " + (mtrHoras == null ? "NULL" : mtrHoras));
		sb.append(tabulador + "mtrHorasCien : " + (mtrHorasCien == null ? "NULL" : mtrHorasCien));

		sb.append(" Nivel");
		sb.append(tabulador + "nvlDescripcion : " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
		sb.append(tabulador + "mtrCodigo : " + (mtrCodigo == null ? "NULL" : mtrCodigo));
		sb.append(tabulador + "mtrEstado : " + (mtrEstado == null ? "NULL" : mtrEstado));
		sb.append(tabulador + "mtrHoras : " + (mtrHoras == null ? "NULL" : mtrHoras));
		sb.append(tabulador + "mtrCreditos : " + (mtrCreditos == null ? "NULL" : mtrCreditos));
		sb.append(tabulador + "mtrHorasCien : " + (mtrHorasCien == null ? "NULL" : mtrHorasCien));

		sb.append(" Nivel");
		sb.append(tabulador + "nvlDescripcion : " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));
		sb.append(tabulador + "nvlNumeral : " + nvlNumeral);

		sb.append(" Persona");
		sb.append(tabulador + "prsId : " + (prsId == null ? "NULL" : prsId));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido == null ? "NULL" : prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido == null ? "NULL" : prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres == null ? "NULL" : prsNombres));

		sb.append(" Aula");
		sb.append(tabulador + "alaId : " + (alaId == null ? "NULL" : alaId));
		sb.append(tabulador + "alaCodigo : " + (alaCodigo == null ? "NULL" : alaCodigo));
		sb.append(tabulador + "alaDescripcion : " + (alaDescripcion == null ? "NULL" : alaDescripcion));
		sb.append(tabulador + "alaCapacidad : " + (alaCapacidad == null ? "NULL" : alaCapacidad));

		sb.append(" Hora Clase");
		sb.append(tabulador + "hoclId : " + (hoclId == null ? "NULL" : hoclId));
		sb.append(tabulador + "hoclDescripcion : " + (hoclDescripcion == null ? "NULL" : hoclDescripcion));
		sb.append(tabulador + "hoclHoraInicio : " + (hoclHoraInicio == null ? "NULL" : hoclHoraInicio));
		sb.append(tabulador + "hoclHoraFin : " + (hoclHoraFin == null ? "NULL" : hoclHoraFin));

		sb.append(" Horario Academico");
		sb.append(tabulador + "hracId : " + (hracId == null ? "NULL" : hracId));
		sb.append(tabulador + "hracDia : " + (hracDia == null ? "NULL" : hracDia));
		sb.append(tabulador + "hracFechaInicio : " + (hracFechaInicio == null ? "NULL" : hracFechaInicio));
		sb.append(tabulador + "hracFechaFin : " + (hracFechaFin == null ? "NULL" : hracFechaFin));
		sb.append(tabulador + "hracMlcrprIdComp : " + (hracMlcrprIdComp == null ? "NULL" : hracMlcrprIdComp));

		sb.append(" Horario Dependencia");
		sb.append(tabulador + "dpnId : " + (dpnId == null ? "NULL" : dpnId));
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));

		sb.append(" Horario Edificio");
		sb.append(tabulador + "edfId : " + (edfId == null ? "NULL" : edfId));
		sb.append(tabulador + "edfDescripcion : " + (edfDescripcion == null ? "NULL" : edfDescripcion));

		sb.append(" Record Estudiante");
		sb.append(tabulador + "rcesId : " + (rcesId == null ? "NULL" : rcesId));
		sb.append(tabulador + "rcesEstado : " + (rcesEstado == null ? "NULL" : rcesEstado));
		sb.append(tabulador + "rcesMlcrprId : " + (rcesMlcrprId == null ? "NULL" : rcesMlcrprId));
		sb.append(tabulador + "rcesNumeroMatriculados : "
				+ (rcesNumeroMatriculados == null ? "NULL" : rcesNumeroMatriculados));

		return sb.toString();
	}

}
