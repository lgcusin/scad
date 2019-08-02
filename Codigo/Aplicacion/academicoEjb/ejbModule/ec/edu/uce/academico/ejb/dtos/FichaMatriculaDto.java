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
   
 ARCHIVO:     FichaMatriculaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Matricula. 
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
 * Clase (DTO) FichaMatriculaDto. DTO encargado de manejar los datos de la
 * entidad Matricula.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class FichaMatriculaDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Matricula
	private int fcmtId;
	private Integer fcmtNivelUbicacion;
	private Integer fcmtEstado;
	private Date fcmtFechaConvalidacion;
	private Integer fcmtTipo;
	private String fcmtTipoMatriculaLabel;
	private Integer fcmtModalidad;
	private String fcmtModalidadLabel;
	private BigDecimal fcmtValorTotal;
	private Timestamp fcmtFechaMatricula;

	// Tabla PlanificacionCronograma
	private Integer plcrId;
	private Integer plcrEstado;
	private Timestamp plcrFechaInicio;
	private Timestamp plcrFechaFin;

	// Tabla cronograma proceso flujo
	private int crprflId;
	private Integer crprProcesoFlujo;
	private Integer crprCronograma;

	// TABLA CRONOGRAMA
	private int crnId;
	private Integer crnPeriodoAcademico;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;

	// Tabla Periodo_Académico
	private int pracId;
	private String pracDescripcion;
	private Integer pracTipo;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;

	// Tabla FichaEstudiante
	private int fcesId;
	private Integer fcesInstitucionAcademica;
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
	private String fcesTituloBachiller;

	// tabla persona
	private int prsId;
	private Integer prsUbicacionNacimiento;
	private Integer prsUbicacionResidencia;
	private Integer prsEtnia;
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
	private String prsCallePrincipal;
	private String prsCalleSecundaria;
	private String prsSectorDomicilio;
	private String prsNumeroCasa;
	private String prsReferenciaDomicilio;
	private String prsCelular;

	// Tabla fichaInscripcion
	private int fcinId;
	private Integer fcinUsuarioRol;
	private Integer fcinPeriodoAcademico;
	private Timestamp fcinFechaInscripcion;
	private String fcinObservacion;
	private Integer fcinTipo;
	private Integer fcinMatriculado;
	private Integer fcinCncrArea;
	private Integer fcinCncr;
	private Integer fcinCarreraSiiu;

	// Tabla configuracion carrera
	private int cncrId;
	private Integer cncrUbicacion;
	private Integer cncrTipoSede;
	private Integer cncrTitulo;
	private Integer cncrTipoFormacion;
	private Integer cncrDuracion;
	private Integer cncrVigencia;
	private Integer cncrModalidad;

	// tabla carrera
	private int crrId;
	private Integer crrDependencia;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrTipo;
	private Integer crrEspeCodigo;
	private Integer crrProceso;

	// tabla dependencia
	private Integer dpnId;
	private String dpnDescripcion;

	// Tabla detalle matricula
	private int dtmtId;
	private Integer dtmtNumero;
	private Integer dtmtEstado;
	private String dtmtArchivoEstudiantes;
	private Integer dtmtEstadoHistorico;
	private Integer dtmtEstadoCambio;
	private String dtmtObservacionHistorico;
	private String dtmtObservacionCambio;

	// Usuario
	private int usrId;
	private Integer usrEstado;

	// Malla Curricular paralelo

	private int mlcrprId;
	private Integer mlcrprCupo;
	private Integer mlcrprInscritos;

	// Malla Curricular materia

	private int mlcrmtId;
	private Integer mlcrmtEstado;

	// Materia
	private int mtrId;
	private String mtrCodigo;
	private String mtrDescripcion;
	private Integer mtrEstado;

	// Nivel
	private int nvlId;
	private String nvlDescripcion;

	// Paralelo
	private int prlId;
	private String prlCodigo;
	private String prlDescripcion;
	private Integer prlEstado;

	// comprobante de pago
	private int cmpaId;

	// Modalidad
	private String mdlDescripcion;

	// Tipo Gratuidad
	private String tpgrDescripcion;

	// Tipo de Materia
	private Integer timtId;

	// Record Estudiantil
	private Integer rcesId;
	private Integer rcesEstadoValue;
	private String rcesEstadoLabel;

	public FichaMatriculaDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getFcmtId() {
		return fcmtId;
	}

	public void setFcmtId(int fcmtId) {
		this.fcmtId = fcmtId;
	}

	public Integer getFcmtNivelUbicacion() {
		return fcmtNivelUbicacion;
	}

	public void setFcmtNivelUbicacion(Integer fcmtNivelUbicacion) {
		this.fcmtNivelUbicacion = fcmtNivelUbicacion;
	}

	public Integer getFcmtEstado() {
		return fcmtEstado;
	}

	public void setFcmtEstado(Integer fcmtEstado) {
		this.fcmtEstado = fcmtEstado;
	}

	public Date getFcmtFechaConvalidacion() {
		return fcmtFechaConvalidacion;
	}

	public void setFcmtFechaConvalidacion(Date fcmtFechaConvalidacion) {
		this.fcmtFechaConvalidacion = fcmtFechaConvalidacion;
	}

	public Integer getFcmtTipo() {
		return fcmtTipo;
	}

	public void setFcmtTipo(Integer fcmtTipo) {
		this.fcmtTipo = fcmtTipo;
	}

	public Integer getFcmtModalidad() {
		return fcmtModalidad;
	}

	public void setFcmtModalidad(Integer fcmtModalidad) {
		this.fcmtModalidad = fcmtModalidad;
	}

	public BigDecimal getFcmtValorTotal() {
		return fcmtValorTotal;
	}

	public void setFcmtValorTotal(BigDecimal fcmtValorTotal) {
		this.fcmtValorTotal = fcmtValorTotal;
	}

	public Timestamp getFcmtFechaMatricula() {
		return fcmtFechaMatricula;
	}

	public void setFcmtFechaMatricula(Timestamp fcmtFechaMatricula) {
		this.fcmtFechaMatricula = fcmtFechaMatricula;
	}

	public Integer getPlcrId() {
		return plcrId;
	}

	public void setPlcrId(Integer plcrId) {
		this.plcrId = plcrId;
	}

	public Integer getPlcrEstado() {
		return plcrEstado;
	}

	public void setPlcrEstado(Integer plcrEstado) {
		this.plcrEstado = plcrEstado;
	}

	public Timestamp getPlcrFechaInicio() {
		return plcrFechaInicio;
	}

	public void setPlcrFechaInicio(Timestamp plcrFechaInicio) {
		this.plcrFechaInicio = plcrFechaInicio;
	}

	public Timestamp getPlcrFechaFin() {
		return plcrFechaFin;
	}

	public void setPlcrFechaFin(Timestamp plcrFechaFin) {
		this.plcrFechaFin = plcrFechaFin;
	}

	public int getCrprflId() {
		return crprflId;
	}

	public void setCrprflId(int crprflId) {
		this.crprflId = crprflId;
	}

	public Integer getCrprProcesoFlujo() {
		return crprProcesoFlujo;
	}

	public void setCrprProcesoFlujo(Integer crprProcesoFlujo) {
		this.crprProcesoFlujo = crprProcesoFlujo;
	}

	public Integer getCrprCronograma() {
		return crprCronograma;
	}

	public void setCrprCronograma(Integer crprCronograma) {
		this.crprCronograma = crprCronograma;
	}

	public int getCrnId() {
		return crnId;
	}

	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}

	public Integer getCrnPeriodoAcademico() {
		return crnPeriodoAcademico;
	}

	public void setCrnPeriodoAcademico(Integer crnPeriodoAcademico) {
		this.crnPeriodoAcademico = crnPeriodoAcademico;
	}

	public Integer getCrnTipo() {
		return crnTipo;
	}

	public void setCrnTipo(Integer crnTipo) {
		this.crnTipo = crnTipo;
	}

	public String getCrnDescripcion() {
		return crnDescripcion;
	}

	public void setCrnDescripcion(String crnDescripcion) {
		this.crnDescripcion = crnDescripcion;
	}

	public Integer getCrnEstado() {
		return crnEstado;
	}

	public void setCrnEstado(Integer crnEstado) {
		this.crnEstado = crnEstado;
	}

	public int getPracId() {
		return pracId;
	}

	public void setPracId(int pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}

	public Date getPracFechaIncio() {
		return pracFechaIncio;
	}

	public void setPracFechaIncio(Date pracFechaIncio) {
		this.pracFechaIncio = pracFechaIncio;
	}

	public Date getPracFechaFin() {
		return pracFechaFin;
	}

	public void setPracFechaFin(Date pracFechaFin) {
		this.pracFechaFin = pracFechaFin;
	}

	public int getFcesId() {
		return fcesId;
	}

	public void setFcesId(int fcesId) {
		this.fcesId = fcesId;
	}

	public Integer getFcesInstitucionAcademica() {
		return fcesInstitucionAcademica;
	}

	public void setFcesInstitucionAcademica(Integer fcesInstitucionAcademica) {
		this.fcesInstitucionAcademica = fcesInstitucionAcademica;
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

	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}

	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}

	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	public Integer getPrsUbicacionNacimiento() {
		return prsUbicacionNacimiento;
	}

	public void setPrsUbicacionNacimiento(Integer prsUbicacionNacimiento) {
		this.prsUbicacionNacimiento = prsUbicacionNacimiento;
	}

	public Integer getPrsUbicacionResidencia() {
		return prsUbicacionResidencia;
	}

	public void setPrsUbicacionResidencia(Integer prsUbicacionResidencia) {
		this.prsUbicacionResidencia = prsUbicacionResidencia;
	}

	public Integer getPrsEtnia() {
		return prsEtnia;
	}

	public void setPrsEtnia(Integer prsEtnia) {
		this.prsEtnia = prsEtnia;
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

	public String getPrsCallePrincipal() {
		return prsCallePrincipal;
	}

	public void setPrsCallePrincipal(String prsCallePrincipal) {
		this.prsCallePrincipal = prsCallePrincipal;
	}

	public String getPrsCalleSecundaria() {
		return prsCalleSecundaria;
	}

	public void setPrsCalleSecundaria(String prsCalleSecundaria) {
		this.prsCalleSecundaria = prsCalleSecundaria;
	}

	public String getPrsSectorDomicilio() {
		return prsSectorDomicilio;
	}

	public void setPrsSectorDomicilio(String prsSectorDomicilio) {
		this.prsSectorDomicilio = prsSectorDomicilio;
	}

	public String getPrsNumeroCasa() {
		return prsNumeroCasa;
	}

	public void setPrsNumeroCasa(String prsNumeroCasa) {
		this.prsNumeroCasa = prsNumeroCasa;
	}

	public String getPrsReferenciaDomicilio() {
		return prsReferenciaDomicilio;
	}

	public void setPrsReferenciaDomicilio(String prsReferenciaDomicilio) {
		this.prsReferenciaDomicilio = prsReferenciaDomicilio;
	}

	public String getPrsCelular() {
		return prsCelular;
	}

	public void setPrsCelular(String prsCelular) {
		this.prsCelular = prsCelular;
	}

	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinUsuarioRol() {
		return fcinUsuarioRol;
	}

	public void setFcinUsuarioRol(Integer fcinUsuarioRol) {
		this.fcinUsuarioRol = fcinUsuarioRol;
	}

	public Integer getFcinPeriodoAcademico() {
		return fcinPeriodoAcademico;
	}

	public void setFcinPeriodoAcademico(Integer fcinPeriodoAcademico) {
		this.fcinPeriodoAcademico = fcinPeriodoAcademico;
	}

	public Timestamp getFcinFechaInscripcion() {
		return fcinFechaInscripcion;
	}

	public void setFcinFechaInscripcion(Timestamp fcinFechaInscripcion) {
		this.fcinFechaInscripcion = fcinFechaInscripcion;
	}

	public String getFcinObservacion() {
		return fcinObservacion;
	}

	public void setFcinObservacion(String fcinObservacion) {
		this.fcinObservacion = fcinObservacion;
	}

	public Integer getFcinTipo() {
		return fcinTipo;
	}

	public void setFcinTipo(Integer fcinTipo) {
		this.fcinTipo = fcinTipo;
	}

	public Integer getFcinMatriculado() {
		return fcinMatriculado;
	}

	public void setFcinMatriculado(Integer fcinMatriculado) {
		this.fcinMatriculado = fcinMatriculado;
	}

	public int getCncrId() {
		return cncrId;
	}

	public void setCncrId(int cncrId) {
		this.cncrId = cncrId;
	}

	public Integer getCncrUbicacion() {
		return cncrUbicacion;
	}

	public void setCncrUbicacion(Integer cncrUbicacion) {
		this.cncrUbicacion = cncrUbicacion;
	}

	public Integer getCncrTipoSede() {
		return cncrTipoSede;
	}

	public void setCncrTipoSede(Integer cncrTipoSede) {
		this.cncrTipoSede = cncrTipoSede;
	}

	public Integer getCncrTitulo() {
		return cncrTitulo;
	}

	public void setCncrTitulo(Integer cncrTitulo) {
		this.cncrTitulo = cncrTitulo;
	}

	public Integer getCncrTipoFormacion() {
		return cncrTipoFormacion;
	}

	public void setCncrTipoFormacion(Integer cncrTipoFormacion) {
		this.cncrTipoFormacion = cncrTipoFormacion;
	}

	public Integer getCncrDuracion() {
		return cncrDuracion;
	}

	public void setCncrDuracion(Integer cncrDuracion) {
		this.cncrDuracion = cncrDuracion;
	}

	public Integer getCncrVigencia() {
		return cncrVigencia;
	}

	public void setCncrVigencia(Integer cncrVigencia) {
		this.cncrVigencia = cncrVigencia;
	}

	public Integer getCncrModalidad() {
		return cncrModalidad;
	}

	public void setCncrModalidad(Integer cncrModalidad) {
		this.cncrModalidad = cncrModalidad;
	}

	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	public Integer getCrrDependencia() {
		return crrDependencia;
	}

	public void setCrrDependencia(Integer crrDependencia) {
		this.crrDependencia = crrDependencia;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public String getCrrCodSniese() {
		return crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Date getCrrFechaCreacion() {
		return crrFechaCreacion;
	}

	public void setCrrFechaCreacion(Date crrFechaCreacion) {
		this.crrFechaCreacion = crrFechaCreacion;
	}

	public String getCrrResolucion() {
		return crrResolucion;
	}

	public void setCrrResolucion(String crrResolucion) {
		this.crrResolucion = crrResolucion;
	}

	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	public int getDtmtId() {
		return dtmtId;
	}

	public void setDtmtId(int dtmtId) {
		this.dtmtId = dtmtId;
	}

	public Integer getDtmtNumero() {
		return dtmtNumero;
	}

	public void setDtmtNumero(Integer dtmtNumero) {
		this.dtmtNumero = dtmtNumero;
	}

	public Integer getDtmtEstado() {
		return dtmtEstado;
	}

	public void setDtmtEstado(Integer dtmtEstado) {
		this.dtmtEstado = dtmtEstado;
	}

	public String getDtmtArchivoEstudiantes() {
		return dtmtArchivoEstudiantes;
	}

	public void setDtmtArchivoEstudiantes(String dtmtArchivoEstudiantes) {
		this.dtmtArchivoEstudiantes = dtmtArchivoEstudiantes;
	}

	public Integer getDtmtEstadoHistorico() {
		return dtmtEstadoHistorico;
	}

	public void setDtmtEstadoHistorico(Integer dtmtEstadoHistorico) {
		this.dtmtEstadoHistorico = dtmtEstadoHistorico;
	}

	public Integer getDtmtEstadoCambio() {
		return dtmtEstadoCambio;
	}

	public void setDtmtEstadoCambio(Integer dtmtEstadoCambio) {
		this.dtmtEstadoCambio = dtmtEstadoCambio;
	}

	public String getDtmtObservacionHistorico() {
		return dtmtObservacionHistorico;
	}

	public void setDtmtObservacionHistorico(String dtmtObservacionHistorico) {
		this.dtmtObservacionHistorico = dtmtObservacionHistorico;
	}

	public String getDtmtObservacionCambio() {
		return dtmtObservacionCambio;
	}

	public void setDtmtObservacionCambio(String dtmtObservacionCambio) {
		this.dtmtObservacionCambio = dtmtObservacionCambio;
	}

	public Integer getFcinCncrArea() {
		return fcinCncrArea;
	}

	public void setFcinCncrArea(Integer fcinCncrArea) {
		this.fcinCncrArea = fcinCncrArea;
	}

	public Integer getFcinCncr() {
		return fcinCncr;
	}

	public void setFcinCncr(Integer fcinCncr) {
		this.fcinCncr = fcinCncr;
	}

	public Integer getFcinCarreraSiiu() {
		return fcinCarreraSiiu;
	}

	public void setFcinCarreraSiiu(Integer fcinCarreraSiiu) {
		this.fcinCarreraSiiu = fcinCarreraSiiu;
	}

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public Integer getUsrEstado() {
		return usrEstado;
	}

	public void setUsrEstado(Integer usrEstado) {
		this.usrEstado = usrEstado;
	}

	public int getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(int mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public Integer getMlcrprCupo() {
		return mlcrprCupo;
	}

	public void setMlcrprCupo(Integer mlcrprCupo) {
		this.mlcrprCupo = mlcrprCupo;
	}

	public Integer getMlcrprInscritos() {
		return mlcrprInscritos;
	}

	public void setMlcrprInscritos(Integer mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Integer getMlcrmtEstado() {
		return mlcrmtEstado;
	}

	public void setMlcrmtEstado(Integer mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	public int getMtrId() {
		return mtrId;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
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

	public Integer getMtrEstado() {
		return mtrEstado;
	}

	public void setMtrEstado(Integer mtrEstado) {
		this.mtrEstado = mtrEstado;
	}

	public int getNvlId() {
		return nvlId;
	}

	public void setNvlId(int nvlId) {
		this.nvlId = nvlId;
	}

	public String getNvlDescripcion() {
		return nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

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

	public int getCmpaId() {
		return cmpaId;
	}

	public void setCmpaId(int cmpaId) {
		this.cmpaId = cmpaId;
	}

	public String getMdlDescripcion() {
		return mdlDescripcion;
	}

	public void setMdlDescripcion(String mdlDescripcion) {
		this.mdlDescripcion = mdlDescripcion;
	}

	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	public String getTpgrDescripcion() {
		return tpgrDescripcion;
	}

	public void setTpgrDescripcion(String tpgrDescripcion) {
		this.tpgrDescripcion = tpgrDescripcion;
	}

	public String getFcmtTipoMatriculaLabel() {
		return fcmtTipoMatriculaLabel;
	}

	public void setFcmtTipoMatriculaLabel(String fcmtTipoMatriculaLabel) {
		this.fcmtTipoMatriculaLabel = fcmtTipoMatriculaLabel;
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

	public String getFcmtModalidadLabel() {
		return fcmtModalidadLabel;
	}

	public void setFcmtModalidadLabel(String fcmtModalidadLabel) {
		this.fcmtModalidadLabel = fcmtModalidadLabel;
	}

	public Integer getPracTipo() {
		return pracTipo;
	}

	public void setPracTipo(Integer pracTipo) {
		this.pracTipo = pracTipo;
	}

	public Integer getRcesId() {
		return rcesId;
	}

	public void setRcesId(Integer rcesId) {
		this.rcesId = rcesId;
	}

	public Integer getRcesEstadoValue() {
		return rcesEstadoValue;
	}

	public void setRcesEstadoValue(Integer rcesEstadoValue) {
		this.rcesEstadoValue = rcesEstadoValue;
	}

	public String getRcesEstadoLabel() {
		return rcesEstadoLabel;
	}

	public void setRcesEstadoLabel(String rcesEstadoLabel) {
		this.rcesEstadoLabel = rcesEstadoLabel;
	}

	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	public Integer getTimtId() {
		return timtId;
	}

	public void setTimtId(Integer timtId) {
		this.timtId = timtId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(" Matricula ");
		sb.append(tabulador + "fcmtId : " + fcmtId);
		sb.append(tabulador + "fcmtNivelUbicacion : " + (fcmtNivelUbicacion == null ? "NULL" : fcmtNivelUbicacion));
		sb.append(tabulador + "fcmtEstado : " + (fcmtEstado == null ? "NULL" : fcmtEstado));
		sb.append(tabulador + "fcmtFechaConvalidacion : "
				+ (fcmtFechaConvalidacion == null ? "NULL" : fcmtFechaConvalidacion));
		sb.append(tabulador + "fcmtTipo : " + (fcmtTipo == null ? "NULL" : fcmtTipo));
		sb.append(tabulador + "fcmtModalidad : " + (fcmtModalidad == null ? "NULL" : fcmtModalidad));
		sb.append(tabulador + "fcmtValorTotal : " + (fcmtValorTotal == null ? "NULL" : fcmtValorTotal));
		sb.append(tabulador + "fcmtFechaMatricula : " + (fcmtFechaMatricula == null ? "NULL" : fcmtFechaMatricula));

		sb.append(" PlanificacionCronograma ");
		sb.append(tabulador + "plcrId : " + plcrId);
		sb.append(tabulador + "plcrEstado : " + (plcrEstado == null ? "NULL" : plcrEstado));
		sb.append(tabulador + "plcrFechaInicio : " + (plcrFechaInicio == null ? "NULL" : plcrFechaInicio));
		sb.append(tabulador + "plcrFechaFin : " + (plcrFechaFin == null ? "NULL" : plcrFechaFin));

		sb.append(" CronogramaProcesoFlujo ");
		sb.append(tabulador + "crprflId : " + crprflId);
		sb.append(tabulador + "crprProcesoFlujo : " + (crprProcesoFlujo == null ? "NULL" : crprProcesoFlujo));
		sb.append(tabulador + "crprCronograma : " + (crprCronograma == null ? "NULL" : crprCronograma));

		sb.append(" Cronograma ");
		sb.append(tabulador + "crnId : " + crnId);
		sb.append(tabulador + "crnPeriodoAcademico : " + (crnPeriodoAcademico == null ? "NULL" : crnPeriodoAcademico));
		sb.append(tabulador + "crnTipo : " + (crnTipo == null ? "NULL" : crnTipo));
		sb.append(tabulador + "crnDescripcion : " + (crnDescripcion == null ? "NULL" : crnDescripcion));
		sb.append(tabulador + "crnEstado : " + (crnEstado == null ? "NULL" : crnEstado));

		sb.append(" PeriodoAcadémico ");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));
		sb.append(tabulador + "pracFechaIncio : " + (pracFechaIncio == null ? "NULL" : pracFechaIncio));
		sb.append(tabulador + "pracFechaFin : " + (pracFechaFin == null ? "NULL" : pracFechaFin));

		sb.append(" FichaEstudiante ");
		sb.append(tabulador + "fcesId : " + fcesId);
		sb.append(tabulador + "fcesInstitucionAcademica : "
				+ (fcesInstitucionAcademica == null ? "NULL" : fcesInstitucionAcademica));
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

		sb.append(" Persona ");
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsUbicacionNacimiento : "
				+ (prsUbicacionNacimiento == null ? "NULL" : prsUbicacionNacimiento));
		sb.append(tabulador + "prsUbicacionResidencia : "
				+ (prsUbicacionResidencia == null ? "NULL" : prsUbicacionResidencia));
		sb.append(tabulador + "prsEtnia : " + (prsEtnia == null ? "NULL" : prsEtnia));
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
		sb.append(tabulador + "prsCallePrincipal : " + (prsCallePrincipal == null ? "NULL" : prsCallePrincipal));
		sb.append(tabulador + "prsCalleSecundaria : " + (prsCalleSecundaria == null ? "NULL" : prsCalleSecundaria));
		sb.append(tabulador + "prsSectorDomicilio : " + (prsSectorDomicilio == null ? "NULL" : prsSectorDomicilio));
		sb.append(tabulador + "prsNumeroCasa : " + (prsNumeroCasa == null ? "NULL" : prsNumeroCasa));
		sb.append(tabulador + "prsReferenciaDomicilio : "
				+ (prsReferenciaDomicilio == null ? "NULL" : prsReferenciaDomicilio));
		sb.append(tabulador + "prsCelular : " + (prsCelular == null ? "NULL" : prsCelular));

		sb.append(" FichaInscripcion ");
		sb.append(tabulador + "fcinId : " + fcinId);
		sb.append(tabulador + "fcinUsuarioRol : " + (fcinUsuarioRol == null ? "NULL" : fcinUsuarioRol));
		sb.append(
				tabulador + "fcinPeriodoAcademico : " + (fcinPeriodoAcademico == null ? "NULL" : fcinPeriodoAcademico));
		sb.append(
				tabulador + "fcinFechaInscripcion : " + (fcinFechaInscripcion == null ? "NULL" : fcinFechaInscripcion));
		sb.append(tabulador + "fcinObservacion : " + (fcinObservacion == null ? "NULL" : fcinObservacion));
		sb.append(tabulador + "fcinTipo : " + (fcinTipo == null ? "NULL" : fcinTipo));
		sb.append(tabulador + "fcinMatriculado : " + (fcinMatriculado == null ? "NULL" : fcinMatriculado));

		sb.append(" ConfiguracionCarrera ");
		sb.append(tabulador + "cncrId : " + cncrId);
		sb.append(tabulador + "cncrUbicacion : " + (cncrUbicacion == null ? "NULL" : cncrUbicacion));
		sb.append(tabulador + "cncrTipoSede : " + (cncrTipoSede == null ? "NULL" : cncrTipoSede));
		sb.append(tabulador + "cncrTitulo : " + (cncrTitulo == null ? "NULL" : cncrTitulo));
		sb.append(tabulador + "cncrTipoFormacion : " + (cncrTipoFormacion == null ? "NULL" : cncrTipoFormacion));
		sb.append(tabulador + "cncrDuracion : " + (cncrDuracion == null ? "NULL" : cncrDuracion));
		sb.append(tabulador + "cncrVigencia : " + (cncrVigencia == null ? "NULL" : cncrVigencia));
		sb.append(tabulador + "cncrModalidad : " + (cncrModalidad == null ? "NULL" : cncrModalidad));

		sb.append(" Carrera ");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDependencia : " + (crrDependencia == null ? "NULL" : crrDependencia));
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrCodSniese : " + (crrCodSniese == null ? "NULL" : crrCodSniese));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrFechaCreacion : " + (crrFechaCreacion == null ? "NULL" : crrFechaCreacion));
		sb.append(tabulador + "crrResolucion : " + (crrResolucion == null ? "NULL" : crrResolucion));
		sb.append(tabulador + "crrTipo : " + (crrTipo == null ? "NULL" : crrTipo));
		sb.append(tabulador + "crrEspeCodigo : " + (crrEspeCodigo == null ? "NULL" : crrEspeCodigo));

		sb.append(" Usuario ");
		sb.append(tabulador + "usrId : " + usrId);
		sb.append(tabulador + "usrEstado : " + (usrEstado == null ? "NULL" : usrEstado));

		sb.append(" Malla Curricular paralelo ");
		sb.append(tabulador + "mlcrprId : " + mlcrprId);
		sb.append(tabulador + "mlcrprCupo : " + (mlcrprCupo == null ? "NULL" : mlcrprCupo));
		sb.append(tabulador + "mlcrprInscritos : " + (mlcrprInscritos == null ? "NULL" : mlcrprInscritos));

		sb.append(" Malla Curricular materia ");
		sb.append(tabulador + "mlcrmtId : " + mlcrmtId);
		sb.append(tabulador + "mlcrmtEstado : " + (mlcrmtEstado == null ? "NULL" : mlcrmtEstado));

		sb.append(" Materia ");
		sb.append(tabulador + "mtrId : " + mtrId);
		sb.append(tabulador + "mtrCodigo : " + (mtrCodigo == null ? "NULL" : mtrCodigo));
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
		sb.append(tabulador + "mtrEstado : " + (mtrEstado == null ? "NULL" : mtrEstado));

		sb.append(" Nivel ");
		sb.append(tabulador + "nvlId : " + nvlId);
		sb.append(tabulador + "nvlDescripcion : " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));

		sb.append(" Paralelo ");
		sb.append(tabulador + "prlId : " + prlId);
		sb.append(tabulador + "prlCodigo : " + (prlCodigo == null ? "NULL" : prlCodigo));
		sb.append(tabulador + "prlDescripcion : " + (prlDescripcion == null ? "NULL" : prlDescripcion));
		sb.append(tabulador + "prlEstado : " + (prlEstado == null ? "NULL" : prlEstado));

		sb.append(" Comprobante de pago ");
		sb.append(tabulador + "cmpaId : " + cmpaId);

		return sb.toString();
	}

}
