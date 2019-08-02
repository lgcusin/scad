package ec.edu.uce.academico.ejb.dtos;

import java.math.BigDecimal;

public class CalificacionDto {
	private int clfId;
	private BigDecimal clfNota1;
	private BigDecimal clfNota2;
	private BigDecimal clfNotaRecuperacion1;
	private BigDecimal clfNotaRecuperacion2;
	private BigDecimal clfAsistencia1;
	private BigDecimal clfAsistencia2;
	private BigDecimal clfAsistenciaDocente1;
	private BigDecimal clfAsistenciaDocente2;
	private BigDecimal clfAsistenciaTotal;
	private BigDecimal clfAsistenciaTotalDocente;
	private BigDecimal clfPromedioNotas;
	private BigDecimal clfSumaNotas;
	private BigDecimal clfSupletorio;
	private BigDecimal clfPorcentajeAsistencia;
	private BigDecimal clfParamRecuperacion1;
	private BigDecimal clfParamRecuperacion2;
	private BigDecimal clfNotaFinalSemestre;
	private Integer clfEstadoValue;
	private Boolean clfIsDisable;
	private String clfEstadoLabel;

	public CalificacionDto() {
		super();
	}

	public int getClfId() {
		return clfId;
	}

	public void setClfId(int clfId) {
		this.clfId = clfId;
	}

	public BigDecimal getClfNota1() {
		return clfNota1;
	}

	public void setClfNota1(BigDecimal clfNota1) {
		this.clfNota1 = clfNota1;
	}

	public BigDecimal getClfNota2() {
		return clfNota2;
	}

	public void setClfNota2(BigDecimal clfNota2) {
		this.clfNota2 = clfNota2;
	}

	public BigDecimal getClfNotaRecuperacion1() {
		return clfNotaRecuperacion1;
	}

	public void setClfNotaRecuperacion1(BigDecimal clfNotaRecuperacion1) {
		this.clfNotaRecuperacion1 = clfNotaRecuperacion1;
	}

	public BigDecimal getClfNotaRecuperacion2() {
		return clfNotaRecuperacion2;
	}

	public void setClfNotaRecuperacion2(BigDecimal clfNotaRecuperacion2) {
		this.clfNotaRecuperacion2 = clfNotaRecuperacion2;
	}

	public BigDecimal getClfSumaNotas() {
		return clfSumaNotas;
	}

	public void setClfSumaNotas(BigDecimal clfSumaNotas) {
		this.clfSumaNotas = clfSumaNotas;
	}

	public BigDecimal getClfAsistencia1() {
		return clfAsistencia1;
	}

	public void setClfAsistencia1(BigDecimal clfAsistencia1) {
		this.clfAsistencia1 = clfAsistencia1;
	}

	public BigDecimal getClfAsistencia2() {
		return clfAsistencia2;
	}

	public void setClfAsistencia2(BigDecimal clfAsistencia2) {
		this.clfAsistencia2 = clfAsistencia2;
	}

	public BigDecimal getClfAsistenciaTotal() {
		return clfAsistenciaTotal;
	}

	public void setClfAsistenciaTotal(BigDecimal clfAsistenciaTotal) {
		this.clfAsistenciaTotal = clfAsistenciaTotal;
	}

	public BigDecimal getClfAsistenciaDocente1() {
		return clfAsistenciaDocente1;
	}

	public void setClfAsistenciaDocente1(BigDecimal clfAsistenciaDocente1) {
		this.clfAsistenciaDocente1 = clfAsistenciaDocente1;
	}

	public BigDecimal getClfAsistenciaDocente2() {
		return clfAsistenciaDocente2;
	}

	public void setClfAsistenciaDocente2(BigDecimal clfAsistenciaDocente2) {
		this.clfAsistenciaDocente2 = clfAsistenciaDocente2;
	}

	public BigDecimal getClfAsistenciaTotalDocente() {
		return clfAsistenciaTotalDocente;
	}

	public void setClfAsistenciaTotalDocente(BigDecimal clfAsistenciaTotalDocente) {
		this.clfAsistenciaTotalDocente = clfAsistenciaTotalDocente;
	}

	public BigDecimal getClfPromedioNotas() {
		return clfPromedioNotas;
	}

	public void setClfPromedioNotas(BigDecimal clfPromedioNotas) {
		this.clfPromedioNotas = clfPromedioNotas;
	}

	public BigDecimal getClfSupletorio() {
		return clfSupletorio;
	}

	public void setClfSupletorio(BigDecimal clfSupletorio) {
		this.clfSupletorio = clfSupletorio;
	}

	public BigDecimal getClfPorcentajeAsistencia() {
		return clfPorcentajeAsistencia;
	}

	public void setClfPorcentajeAsistencia(BigDecimal clfPorcentajeAsistencia) {
		this.clfPorcentajeAsistencia = clfPorcentajeAsistencia;
	}

	public BigDecimal getClfParamRecuperacion1() {
		return clfParamRecuperacion1;
	}

	public void setClfParamRecuperacion1(BigDecimal clfParamRecuperacion1) {
		this.clfParamRecuperacion1 = clfParamRecuperacion1;
	}

	public BigDecimal getClfParamRecuperacion2() {
		return clfParamRecuperacion2;
	}

	public void setClfParamRecuperacion2(BigDecimal clfParamRecuperacion2) {
		this.clfParamRecuperacion2 = clfParamRecuperacion2;
	}

	public BigDecimal getClfNotaFinalSemestre() {
		return clfNotaFinalSemestre;
	}

	public void setClfNotaFinalSemestre(BigDecimal clfNotaFinalSemestre) {
		this.clfNotaFinalSemestre = clfNotaFinalSemestre;
	}

	public Integer getClfEstadoValue() {
		return clfEstadoValue;
	}

	public void setClfEstadoValue(Integer clfEstadoValue) {
		this.clfEstadoValue = clfEstadoValue;
	}

	public String getClfEstadoLabel() {
		return clfEstadoLabel;
	}

	public void setClfEstadoLabel(String clfEstadoLabel) {
		this.clfEstadoLabel = clfEstadoLabel;
	}

	public Boolean getClfIsDisable() {
		return clfIsDisable;
	}

	public void setClfIsDisable(Boolean clfIsDisable) {
		this.clfIsDisable = clfIsDisable;
	}

}
