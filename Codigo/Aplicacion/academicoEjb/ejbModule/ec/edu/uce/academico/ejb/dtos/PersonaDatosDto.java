/*****************************
 * *********************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     PersonaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Persona. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 10-10-2017 			Arturo Villafuerte   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) PersonaDto. DTO encargado de manejar los datos de la entidad
 * Persona.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */
public class PersonaDatosDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// TABLA PERSONA
	private int prsId;
	private String prsIdentificacion;
	private String prsIdEvaluador;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailInstitucional;

	// TABLA TIEMPO_DEDICACION
	private int tmddId;
	private String tmddDescripcion;

	// TABLA RELACION_LABORAL
	private int rllbId;
	private String rllbDescripcion;

	// TABLA DETALLE_PUESTO
	private int dtpsId;
	private Integer dtpsCrrTipo;
	private String dtpsCrrDescripcion;

	// TABLA PUESTO
	private Integer pstNivelRangoGradual;
	private Integer pstCategoria;
	private String pstDenominacion;

	// Tabla Materia
	private int mtrId;
	private Integer mtrEstado;
	private Integer mtrHoras;
	private Integer mtrCreditos;
	private Integer mtrHorasPorSemana;
	private Integer mtrNumeroMatricula;
	private String mtrCodigo;
	private String mtrDescripcion;

	// auxiliares apelacion
	private String prsNomApEvaluado;
	private String prsNomApEvaluador;

	private CarreraDto prsCarreraDto;
	private PeriodoAcademicoDto prsPeriodoAcademicoDto;

	public PersonaDatosDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
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

	public int getTmddId() {
		return tmddId;
	}

	public void setTmddId(int tmddId) {
		this.tmddId = tmddId;
	}

	public String getTmddDescripcion() {
		return tmddDescripcion;
	}

	public void setTmddDescripcion(String tmddDescripcion) {
		this.tmddDescripcion = tmddDescripcion;
	}

	public int getRllbId() {
		return rllbId;
	}

	public void setRllbId(int rllbId) {
		this.rllbId = rllbId;
	}

	public String getRllbDescripcion() {
		return rllbDescripcion;
	}

	public void setRllbDescripcion(String rllbDescripcion) {
		this.rllbDescripcion = rllbDescripcion;
	}

	public int getDtpsId() {
		return dtpsId;
	}

	public void setDtpsId(int dtpsId) {
		this.dtpsId = dtpsId;
	}

	public Integer getDtpsCrrTipo() {
		return dtpsCrrTipo;
	}

	public void setDtpsCrrTipo(Integer dtpsCrrTipo) {
		this.dtpsCrrTipo = dtpsCrrTipo;
	}

	public String getPstDenominacion() {
		return pstDenominacion;
	}

	public void setPstDenominacion(String pstDenominacion) {
		this.pstDenominacion = pstDenominacion;
	}

	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}

	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}

	public int getMtrId() {
		return mtrId;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
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

	public Integer getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(Integer mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}

	public Integer getMtrHorasPorSemana() {
		return mtrHorasPorSemana;
	}

	public void setMtrHorasPorSemana(Integer mtrHorasPorSemana) {
		this.mtrHorasPorSemana = mtrHorasPorSemana;
	}

	public Integer getMtrNumeroMatricula() {
		return mtrNumeroMatricula;
	}

	public void setMtrNumeroMatricula(Integer mtrNumeroMatricula) {
		this.mtrNumeroMatricula = mtrNumeroMatricula;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public String getDtpsCrrDescripcion() {
		return dtpsCrrDescripcion;
	}

	public void setDtpsCrrDescripcion(String dtpsCrrDescripcion) {
		this.dtpsCrrDescripcion = dtpsCrrDescripcion;
	}

	public String getPrsIdEvaluador() {
		return prsIdEvaluador;
	}

	public void setPrsIdEvaluador(String prsIdEvaluador) {
		this.prsIdEvaluador = prsIdEvaluador;
	}

	public String getPrsNomApEvaluado() {
		return prsNomApEvaluado;
	}

	public void setPrsNomApEvaluado(String prsNomApEvaluado) {
		this.prsNomApEvaluado = prsNomApEvaluado;
	}

	public String getPrsNomApEvaluador() {
		return prsNomApEvaluador;
	}

	public void setPrsNomApEvaluador(String prsNomApEvaluador) {
		this.prsNomApEvaluador = prsNomApEvaluador;
	}

	public CarreraDto getPrsCarreraDto() {
		return prsCarreraDto;
	}

	public void setPrsCarreraDto(CarreraDto prsCarreraDto) {
		this.prsCarreraDto = prsCarreraDto;
	}

	public PeriodoAcademicoDto getPrsPeriodoAcademicoDto() {
		return prsPeriodoAcademicoDto;
	}

	public void setPrsPeriodoAcademicoDto(PeriodoAcademicoDto prsPeriodoAcademicoDto) {
		this.prsPeriodoAcademicoDto = prsPeriodoAcademicoDto;
	}

	public Integer getPstNivelRangoGradual() {
		return pstNivelRangoGradual;
	}

	public void setPstNivelRangoGradual(Integer pstNivelRangoGradual) {
		this.pstNivelRangoGradual = pstNivelRangoGradual;
	}

	public Integer getPstCategoria() {
		return pstCategoria;
	}

	public void setPstCategoria(Integer pstCategoria) {
		this.pstCategoria = pstCategoria;
	}

	public String getPrsApellidosNombres() {
		StringBuilder sb = new StringBuilder();
		sb.append(prsPrimerApellido == null ? "" : prsPrimerApellido + " ");
		sb.append(prsSegundoApellido == null ? "" : prsSegundoApellido + " ");
		sb.append(prsNombres == null ? "" : prsNombres);
		return sb.toString();
	}
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";

		StringBuilder sb = new StringBuilder();
		sb.append(" Persona ");
		sb.append("prsId : " + prsId);
		sb.append(tabulador + "prsIdentificacion : " + prsIdentificacion);
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido == null ? "NULL" : prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido == null ? "NULL" : prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres == null ? "NULL" : prsNombres));
		sb.append(tabulador + "prsNombres : " + (prsMailInstitucional == null ? "NULL" : prsMailInstitucional));

		sb.append(" Tiempo de Dedicacion ");
		sb.append("tmddId : " + tmddId);
		sb.append(tabulador + "tmddDescripcion : " + (tmddDescripcion == null ? "NULL" : tmddDescripcion));

		sb.append(" Relacion Laboral ");
		sb.append("rllbId : " + rllbId);
		sb.append(tabulador + "rllbDescripcion : " + (rllbDescripcion == null ? "NULL" : rllbDescripcion));

		sb.append(" Detalle Puesto ");
		sb.append("dtpsId : " + dtpsId);
		sb.append("dtpsCrrTipo : " + dtpsCrrTipo);

		sb.append(" Puesto ");
		sb.append(tabulador + "pstDenominacion : " + (pstDenominacion == null ? "NULL" : pstDenominacion));

		return sb.toString();
	}

}
