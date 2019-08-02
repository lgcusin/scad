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
   
 ARCHIVO:     FichaEstudianteDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad FichaEstudiante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-03-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) FichaEstudianteDto. DTO encargado de manejar los datos de la
 * entidad FichaEstudiante.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class FichaEstudianteDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla FichaEstudiante
	private int fcesId;
	private Date fcesFechaInicio;
	private Date fcesFechaEgresamiento;
	private Date fcesFechaActaGrado;
	private String fcesNumActaGrado;
	private Date fcesFechaRefrendacion;
	private String fcesNumRefrendacion;
	private String fcesCrrEstudPrevios;
	private Integer fcesTiempoEstudRec;
	private Integer fcesTipoDuracRec;
	private Integer fcesTipoColegio;
	private String fcesTipoColegioSniese;
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private String fcesLinkTesis;
	private Integer fcesRecEstudPrevios;
	private String fcesRecEstudPrevSniese;
	private Timestamp fcesFechaCreacion;
	private Integer fcesEstadoUniversitario;
	private Integer fcesEstadoMatricula;
	private String fcesObservacion;
	private Integer fcesPrsId;
	private Integer fcesCncrId;
	private Integer fcesFcinId;
	private String fcesTituloBachiller;
	private Integer fcesInstitucionAcademicaId;

	// Tabla Persona
	private int prsId;
	private Integer prsTipoIdentificacion;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private Integer prsSexo;
	private Integer prsSexoSniese;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private Integer fcesEtnId;
	private Integer fcesUbcId;

	// Tabla Record_Estudiante
	private int rcesId;
	private Integer rcesMlcrprId;
	

	// Tabala Detalle_matricula
	private int dtmtId;
	private Integer dtmtMlcrprId;
	
	public FichaEstudianteDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getFcesId() {
		return fcesId;
	}

	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}

	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}

	public Integer getFcesFcinId() {
		return fcesFcinId;
	}

	public void setFcesFcinId(Integer fcesFcinId) {
		this.fcesFcinId = fcesFcinId;
	}

	public void setFcesId(int fcesId) {
		this.fcesId = fcesId;
	}

	public Date getFcesFechaInicio() {
		return fcesFechaInicio;
	}

	public void setFcesFechaInicio(Date fcesFechaInicio) {
		this.fcesFechaInicio = fcesFechaInicio;
	}

	public Date getFcesFechaEgresamiento() {
		return fcesFechaEgresamiento;
	}

	public void setFcesFechaEgresamiento(Date fcesFechaEgresamiento) {
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
	}

	public Date getFcesFechaActaGrado() {
		return fcesFechaActaGrado;
	}

	public void setFcesFechaActaGrado(Date fcesFechaActaGrado) {
		this.fcesFechaActaGrado = fcesFechaActaGrado;
	}

	public String getFcesNumActaGrado() {
		return fcesNumActaGrado;
	}

	public void setFcesNumActaGrado(String fcesNumActaGrado) {
		this.fcesNumActaGrado = fcesNumActaGrado;
	}

	public Date getFcesFechaRefrendacion() {
		return fcesFechaRefrendacion;
	}

	public void setFcesFechaRefrendacion(Date fcesFechaRefrendacion) {
		this.fcesFechaRefrendacion = fcesFechaRefrendacion;
	}

	public String getFcesNumRefrendacion() {
		return fcesNumRefrendacion;
	}

	public void setFcesNumRefrendacion(String fcesNumRefrendacion) {
		this.fcesNumRefrendacion = fcesNumRefrendacion;
	}

	public String getFcesCrrEstudPrevios() {
		return fcesCrrEstudPrevios;
	}

	public void setFcesCrrEstudPrevios(String fcesCrrEstudPrevios) {
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
	}

	public Integer getFcesTiempoEstudRec() {
		return fcesTiempoEstudRec;
	}

	public void setFcesTiempoEstudRec(Integer fcesTiempoEstudRec) {
		this.fcesTiempoEstudRec = fcesTiempoEstudRec;
	}

	public Integer getFcesTipoDuracRec() {
		return fcesTipoDuracRec;
	}

	public void setFcesTipoDuracRec(Integer fcesTipoDuracRec) {
		this.fcesTipoDuracRec = fcesTipoDuracRec;
	}

	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}

	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}

	public String getFcesTipoColegioSniese() {
		return fcesTipoColegioSniese;
	}

	public void setFcesTipoColegioSniese(String fcesTipoColegioSniese) {
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
	}

	public BigDecimal getFcesNotaPromAcumulado() {
		return fcesNotaPromAcumulado;
	}

	public void setFcesNotaPromAcumulado(BigDecimal fcesNotaPromAcumulado) {
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
	}

	public BigDecimal getFcesNotaTrabTitulacion() {
		return fcesNotaTrabTitulacion;
	}

	public void setFcesNotaTrabTitulacion(BigDecimal fcesNotaTrabTitulacion) {
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
	}

	public String getFcesLinkTesis() {
		return fcesLinkTesis;
	}

	public void setFcesLinkTesis(String fcesLinkTesis) {
		this.fcesLinkTesis = fcesLinkTesis;
	}

	public Integer getFcesRecEstudPrevios() {
		return fcesRecEstudPrevios;
	}

	public void setFcesRecEstudPrevios(Integer fcesRecEstudPrevios) {
		this.fcesRecEstudPrevios = fcesRecEstudPrevios;
	}

	public String getFcesRecEstudPrevSniese() {
		return fcesRecEstudPrevSniese;
	}

	public void setFcesRecEstudPrevSniese(String fcesRecEstudPrevSniese) {
		this.fcesRecEstudPrevSniese = fcesRecEstudPrevSniese;
	}

	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}

	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}

	public Integer getFcesEstadoUniversitario() {
		return fcesEstadoUniversitario;
	}

	public void setFcesEstadoUniversitario(Integer fcesEstadoUniversitario) {
		this.fcesEstadoUniversitario = fcesEstadoUniversitario;
	}

	public Integer getFcesEstadoMatricula() {
		return fcesEstadoMatricula;
	}

	public void setFcesEstadoMatricula(Integer fcesEstadoMatricula) {
		this.fcesEstadoMatricula = fcesEstadoMatricula;
	}

	public String getFcesObservacion() {
		return fcesObservacion;
	}

	public void setFcesObservacion(String fcesObservacion) {
		this.fcesObservacion = fcesObservacion;
	}

	public Integer getFcesPrsId() {
		return fcesPrsId;
	}

	public void setFcesPrsId(Integer fcesPrsId) {
		this.fcesPrsId = fcesPrsId;
	}

	public Integer getFcesCncrId() {
		return fcesCncrId;
	}

	public void setFcesCncrId(Integer fcesCncrId) {
		this.fcesCncrId = fcesCncrId;
	}

	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	public Integer getPrsTipoIdentificacion() {
		return prsTipoIdentificacion;
	}

	public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
		this.prsTipoIdentificacion = prsTipoIdentificacion;
	}

	public Integer getPrsTipoIdentificacionSniese() {
		return prsTipoIdentificacionSniese;
	}

	public void setPrsTipoIdentificacionSniese(Integer prsTipoIdentificacionSniese) {
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
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

	public Integer getPrsSexo() {
		return prsSexo;
	}

	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}

	public Integer getPrsSexoSniese() {
		return prsSexoSniese;
	}

	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
	}

	public String getPrsMailPersonal() {
		return prsMailPersonal;
	}

	public void setPrsMailPersonal(String prsMailPersonal) {
		this.prsMailPersonal = prsMailPersonal;
	}

	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}

	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}

	public String getPrsTelefono() {
		return prsTelefono;
	}

	public void setPrsTelefono(String prsTelefono) {
		this.prsTelefono = prsTelefono;
	}

	public Date getPrsFechaNacimiento() {
		return prsFechaNacimiento;
	}

	public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
		this.prsFechaNacimiento = prsFechaNacimiento;
	}

	public Integer getFcesEtnId() {
		return fcesEtnId;
	}

	public void setFcesEtnId(Integer fcesEtnId) {
		this.fcesEtnId = fcesEtnId;
	}

	public Integer getFcesUbcId() {
		return fcesUbcId;
	}

	public void setFcesUbcId(Integer fcesUbcId) {
		this.fcesUbcId = fcesUbcId;
	}

	public Integer getFcesInstitucionAcademicaId() {
		return fcesInstitucionAcademicaId;
	}

	public void setFcesInstitucionAcademicaId(Integer fcesInstitucionAcademicaId) {
		this.fcesInstitucionAcademicaId = fcesInstitucionAcademicaId;
	}

	public int getRcesId() {
		return rcesId;
	}

	public void setRcesId(int rcesId) {
		this.rcesId = rcesId;
	}

	public Integer getRcesMlcrprId() {
		return rcesMlcrprId;
	}

	public void setRcesMlcrprId(Integer rcesMlcrprId) {
		this.rcesMlcrprId = rcesMlcrprId;
	}

	public int getDtmtId() {
		return dtmtId;
	}

	public void setDtmtId(int dtmtId) {
		this.dtmtId = dtmtId;
	}

	public Integer getDtmtMlcrprId() {
		return dtmtMlcrprId;
	}

	public void setDtmtMlcrprId(Integer dtmtMlcrprId) {
		this.dtmtMlcrprId = dtmtMlcrprId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(tabulador + "fcesId : " + fcesId);
		sb.append(tabulador + "fcesFechaInicio : " + (fcesFechaInicio == null ? "NULL" : fcesFechaInicio));
		sb.append(tabulador + "fcesFechaEgresamiento : "
				+ (fcesFechaEgresamiento == null ? "NULL" : fcesFechaEgresamiento));
		sb.append(tabulador + "fcesFechaActaGrado : " + (fcesFechaActaGrado == null ? "NULL" : fcesFechaActaGrado));
		sb.append(tabulador + "fcesNumActaGrado : " + (fcesNumActaGrado == null ? "NULL" : fcesNumActaGrado));
		sb.append(tabulador + "fcesFechaRefrendacion : "
				+ (fcesFechaRefrendacion == null ? "NULL" : fcesFechaRefrendacion));
		sb.append(tabulador + "fcesNumRefrendacion : " + (fcesNumRefrendacion == null ? "NULL" : fcesNumRefrendacion));
		sb.append(tabulador + "fcesCrrEstudPrevios : " + (fcesCrrEstudPrevios == null ? "NULL" : fcesCrrEstudPrevios));
		sb.append(tabulador + "fcesTiempoEstudRec : " + (fcesTiempoEstudRec == null ? "NULL" : fcesTiempoEstudRec));
		sb.append(tabulador + "fcesTipoDuracRec : " + (fcesTipoDuracRec == null ? "NULL" : fcesTipoDuracRec));
		sb.append(tabulador + "fcesTipoColegio : " + (fcesTipoColegio == null ? "NULL" : fcesTipoColegio));
		sb.append(tabulador + "fcesTipoColegioSniese : "
				+ (fcesTipoColegioSniese == null ? "NULL" : fcesTipoColegioSniese));
		sb.append(tabulador + "fcesNotaPromAcumulado : "
				+ (fcesNotaPromAcumulado == null ? "NULL" : fcesNotaPromAcumulado));
		sb.append(tabulador + "fcesNotaTrabTitulacion : "
				+ (fcesNotaTrabTitulacion == null ? "NULL" : fcesNotaTrabTitulacion));
		sb.append(tabulador + "fcesLinkTesis : " + (fcesLinkTesis == null ? "NULL" : fcesLinkTesis));
		sb.append(tabulador + "fcesRecEstudPrevios : " + (fcesRecEstudPrevios == null ? "NULL" : fcesRecEstudPrevios));
		sb.append(tabulador + "fcesRecEstudPrevSniese : "
				+ (fcesRecEstudPrevSniese == null ? "NULL" : fcesRecEstudPrevSniese));
		sb.append(tabulador + "fcesFechaCreacion : " + (fcesFechaCreacion == null ? "NULL" : fcesFechaCreacion));
		sb.append(tabulador + "fcesEstadoUniversitario : "
				+ (fcesEstadoUniversitario == null ? "NULL" : fcesEstadoUniversitario));
		sb.append(tabulador + "fcesEstadoMatricula : " + (fcesEstadoMatricula == null ? "NULL" : fcesEstadoMatricula));
		sb.append(tabulador + "fcesObservacion : " + (fcesObservacion == null ? "NULL" : fcesObservacion));
		sb.append(tabulador + "fcesTituloBachiller : " + (fcesTituloBachiller == null ? "NULL" : fcesTituloBachiller));
		sb.append(tabulador + "fcesPrsId : " + fcesPrsId);
		sb.append(tabulador + "fcesCncrId : " + fcesCncrId);
		sb.append(tabulador + "fcesFcinId : " + fcesFcinId);
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsTipoIdentificacion : "
				+ (prsTipoIdentificacion == null ? "NULL" : prsTipoIdentificacion));
		sb.append(tabulador + "prsTipoIdentificacionSniese : "
				+ (prsTipoIdentificacionSniese == null ? "NULL" : prsTipoIdentificacionSniese));
		sb.append(tabulador + "prsIdentificacion : " + (prsIdentificacion == null ? "NULL" : prsIdentificacion));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido == null ? "NULL" : prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido == null ? "NULL" : prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres == null ? "NULL" : prsNombres));
		sb.append(tabulador + "prsSexo : " + (prsSexo == null ? "NULL" : prsSexo));
		sb.append(tabulador + "prsSexoSniese : " + (prsSexoSniese == null ? "NULL" : prsSexoSniese));
		sb.append(tabulador + "prsMailPersonal : " + (prsMailPersonal == null ? "NULL" : prsMailPersonal));
		sb.append(
				tabulador + "prsMailInstitucional : " + (prsMailInstitucional == null ? "NULL" : prsMailInstitucional));
		sb.append(tabulador + "prsTelefono : " + (prsTelefono == null ? "NULL" : prsTelefono));
		sb.append(tabulador + "prsFechaNacimiento : " + (prsFechaNacimiento == null ? "NULL" : prsFechaNacimiento));

		return sb.toString();
	}

}
