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

 ARCHIVO:     EstudianteJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad FichaEstudiante DTO JDBC. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 11-03-2017 			Dennis Collaguazo  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase (DTO) FichaEstudianteJdbcDto. DTO encargado de manejar los datos de la
 * entidad FichaEstudiante DTO JDBC.
 * 
 * @author dcollaguazo.
 * @version 1.0
 */
public class EstudianteJdbcDto implements Serializable {

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
	private String fcesTipoDuracRecSt;
	private Integer fcesTipoColegio;
	private String fcesTipoColegioSt;
	private String fcesTipoColegioSniese;
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private String fcesLinkTesis;
	private Integer fcesRecEstuPrevios;
	private String fcesRecEstuPreviosSt;
	private String fcesRecEstudPrevSniese;
	private Timestamp fcesFechaCreacion;
	private Integer fcesEstadoUniversitario;
	private Integer fcesEstadoMatricula;
	private String fcesObservacion;
	private String fcesTituloBachiller;
	private Float fcesNotaGradoSecundaria;
	private Integer fcesPrsId;
	private Integer fcesCncrId;
	private Integer fcesInacId;
	private Integer fcesInacTtlId;
	private Integer fcesUbcColegio;
	private Integer fcesColegioId;
	private Timestamp fcesFechaActualizacionDatos;

	private Integer fcesTipoUnivEstudPrev;
	private Integer fcesEstadoEstudPrev;
	private Integer fcesUnivEstudPrevId;
	private Integer fcesTitEstudPrevId;
	private String fcesRegTituloPrev;

	// Tabla Institucion Academica
	private int inacId;
	private String inacDescripcion;
	private Integer inacNivel;
	private Integer inacTipo;
	private String inacTipoSniese;
	// Tabla titulo
	private int ttlId;
	private String ttlDescripcion;
	private Integer ttlSexo;
	private Integer ttlEstado;
	private Integer ttlTipo;

	// Tabla Persona
	private int prsId;
	private Integer prsTipoIdentificacion;
	private String prsTipoIdentificacionSt;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private Integer prsSexo;
	private String prsSexoSt;
	private Integer prsSexoSniese;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private Integer prsEtnId;
	private Integer prsUbcNacimiento;
	private Integer prsUbcResidencia;
	private String prsCallePrincipal;
	private String prsCalleSecundaria;
	private String prsSectorDomicilio;
	private String prsNumeroCasa;
	private String prsReferenciaDomicilio;
	private String prsCelular;
	private Integer prsDiscapacidad;
	private Integer prsTipoDiscapacidad;
	private Integer prsPorceDiscapacidad;
	private Integer prsCarnetConadis;
	private String prsNumCarnetConadis;
	private Integer prsEstadoCivil;
	private Timestamp prsFechaActualizacionDatos;
	private Date prsFechaVinculacionSeguro;
	private String prsFormularioSeguro;
	private Timestamp prsFechaFormularioSeguro;
	private Integer prsNumeracion;

	// Tabla Ubicacion
	private Integer ubcPaisId;
	private String ubcPaisDescripcion;
	private Integer ubcCantonId;
	private String ubcCantonDescripcion;
	private Integer ubcParroquiaId;
	private String ubcParroquiaDescripcion;
	private Integer ubcCantonColegioId;
	private String ubcCantonColegioDescripcion;
	private String ubcGentilicio;

	// Tabla Etnia
	private int etnId;
	private String etnDescripcion;

	// Tabla ConfiguracionCarrera
	private int cncrId;
	private Integer cncrCrrId;

	// Tabla Carrera
	private int crrId;
	private Integer crrDpnId;
	private Integer crrEspeCodigo;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private String crrFechaCreacion;
	private String crrResolucion;
	private Integer crrTipo;
	private Integer crrTipoEvaluacion;

	private int crrIdArea;
	private String crrDescripcionArea;
	
	// Tabla Dependencia (Facultad)
	private int dpnId;
	private Integer dpnSubId;
	private Integer dpnUbcId;
	private String dpnDescripcion;
	private Integer dpnJerarquia;
	private Integer dpnEstado;
	private Integer dpnCampus;

	// Tabla MallaCurricular
	private int mlcrId;
	private String mlcrDescripcion;

	// Tabla Nivel
	private int nvlId;
	private String nvlDescripcion;
	private Integer nvlNumeral;

	// Tabla MallaCurriularMateria
	private int mlcrmtId;

	// Tabla MallaCurricularParalelo
	private int mlcrprId;

	// Tabla Paralelo
	private int prlId;
	private String prlDescripcion;
	// Tabla DetalleMatricula
	private int dtmtId;
	private Integer dtmtNumero;
	private Integer dtmtEstado;
	private String dtmtArchivoEstudiantes;
	private Integer dtmtEstadoHistorico;
	private Integer dtmtEstadoCambio;
	private String dtmtObservacionHistorico;
	private String dtmtObservacionCambio;
	private Timestamp dtmtFechaHistorico;
	private Timestamp dtmtFechaCambio;
	private BigDecimal dtmtValorPorMateria;
	private Integer dtmtEstadoSolicitud;
	private Timestamp dtmtFechaSolicitud;
	private Integer dtmtEstadoRespuesta;
	private Timestamp dtmtFechaRespuesta;
	private Integer dtmtCslRetiroId;
	private String dtmtArchivoRespuesta;

	private String dtmtObservacionFinalRetiro;
	private Integer dtmtTipoAnulacion;
	private Timestamp dtmtFechaAnulacion;
	private String dtmtArchivoAnulacion;
	private String dtmtRegistranteAnulacion;
	private String dtmtObservacionAnulacion;
	private boolean dtmtRetiroSolicitudTercera;
	

	// Tabla Materia
	private int mtrId;
	private String mtrCodigo;
	private String mtrDescripcion;
	private int mtrHoras;
	private int mtrCreditos;
	private String mtrStHotas;
	private int mtrHorasCien;
	private int mtrSubMtrId;
	private Integer mtrHorasPAE;

	// Tabla MallaPeriodo
	private int mlprId;

	// Tabla PeriodoAcademico;
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;

	// Tabla Record Academico
	private int rcesId;
	private Integer rcesEstado;
	private Integer rcesIngersoNota;
	private Integer rcesIngersoNota2;
	private Integer rcesIngersoNota3;

	// Tabla Ficha Inscripcion;
	private int fcinId;
	private Integer fcinMatriculado;
	private Integer fcinTipo;
	private Integer fcinConfiguracionCarrera;
	private Integer fcinPeriodoAcademico;
	private Integer fcinCarrera;
	private Integer fcinCarreraSiiu;
	private String fcinCarreraSiiuSt;
	private Integer fcinEstado;
	private Integer fcinEncuesta;
	private Integer fcinUsroId;
	private Integer fcinCncrArea;
	private Float fcinNotaEnes;

	// Tabla Detalle Puesto Puesto;
	private int dtpsId;

	// Tabla Usuario_rol;
	private int usroId;

	// CAMPOS PARA GUARDAR LAS NOTAS Y ASISTENCIA
	private int clfId;
	private Integer clfAsistenciaEstudiante1; // asistencia estudiante primer
												// hemi
	private Integer clfAsistenciaEstudiante2; // asistencia estudiante 2 hemi
	private Integer clfAsistenciaDocente1; // asistencia docente primer hemi
	private Integer clfAsistenciaDocente2; // asistencia docente 2 hemi
	private BigDecimal clfNota1; // nota estudiante 1 hemi
	private String clfNota1String; // nota estudiante 1 hemi
	private BigDecimal clfNota2; // nota estudiante 2 hemi
	private String clfNota2String; // nota estudiante 2 hemi
	private BigDecimal clfTotalAsistencia1; // porcentaje asistencia 1 hemi
	private BigDecimal clfTotalAsistencia2; // porcentaje asistencia 2 hemi
	private BigDecimal clfPromedioAsistencia; // prmedio asistencia semestre
	private BigDecimal clfPromedioNotas; // primedio notas
	private BigDecimal clfSupletorio; // nota suspencion

	private BigDecimal clfAsistenciaTotal; // aistencia total suma de nos dos
											// parciales
	private BigDecimal clfParamRecuperacion1; // nota 40% del la suma de los
												// parciales
	private BigDecimal clfParamRecuperacion2; // nota 60% ponderacion de la la
												// nota supeltorio
	private BigDecimal clfNotalFinalSemestre; // nota igual a la suma de lo
												// sparciales o a la suma del
												// param1 + param2 si el
												// estudiante quedo a
												// recuperacion
	private BigDecimal clfEstado; // estado del registro calificacion
	private BigDecimal clfSumaP1P2;
	private BigDecimal clfAsistenciaTotalDoc;

	// CAMPOS PARA GUARDAR LAS NOTAS Y ASISTENCIA MODULAR
	private int clmdId;
	private Integer clmdAsistenciaEstudiante1; // asistencia estudiante primer
												// hemi
	private Integer clmdAsistenciaEstudiante2; // asistencia estudiante 2 hemi
	private Integer clmdAsistenciaDocente1; // asistencia docente primer hemi
	private Integer clmdAsistenciaDocente2; // asistencia docente 2 hemi
	private BigDecimal clmdNota1; // nota estudiante 1 hemi
	private String clmdNota1String; // nota estudiante 1 hemi
	private BigDecimal clmdNota2; // nota estudiante 2 hemi
	private BigDecimal clmdNota2String; // nota estudiante 2 hemi
	private BigDecimal clmdTotalAsistencia1; // porcentaje asistencia 1 hemi
	private BigDecimal clmdTotalAsistencia2; // porcentaje asistencia 2 hemi
	private BigDecimal clmdPromedioAsistencia; // prmedio asistencia semestre
	private BigDecimal clmdPromedioNotas; // primedio notas
	private BigDecimal clmdSupletorio; // nota suspencion

	private BigDecimal clmdAsistenciaTotal; // aistencia total suma de nos dos
											// parciales
	private BigDecimal clmdParamRecuperacion1; // nota 40% del la suma de los
												// parciales
	private BigDecimal clmdParamRecuperacion2; // nota 60% ponderacion de la la
												// nota supeltorio
	private BigDecimal clmdNotalFinalSemestre; // nota igual a la suma de lo
												// sparciales o a la suma del
												// param1 + param2 si el
												// estudiante quedo a
												// recuperacion
	private BigDecimal clmdEstado; // estado del registro calificacion
	private BigDecimal clmdSumaP1P2;
	private BigDecimal clmdAsistenciaTotalDoc;

	// Tabla Proceso calificacion
	private int prclId;
	private Timestamp prclFecha;
	private int prclTipoPorceso;
	private String prclObservacion;

	// Tabla Ficha Matricula
	private int fcmtId;
	private Timestamp fcmtFechaMatricula;
	private BigDecimal fcmtValorTotal;
	private Integer fcmtPlcrId;
	private Integer fcmtEstado;
	private Integer fcmtNivelUbicacion;
	// Promedio
	private BigDecimal prome;

	// Estado solicitud retiro
	private Integer estadoSolicitudRetiro;

	private Integer cmpaEstadoPago;
	private BigDecimal cmpaTotalPago;
	private String cmpaNumComprobante;

	// Bandera de Modular
	private boolean materiaModulo;
	private boolean seleccionModulo;
	private Integer mlcrprIdModulo;

	// ********* PARA LA SELECCION DE MATERIAS PARA EL PROCESO DE ANULAR
	// **************
	private boolean seleccionado;

	// para controlar disable
	private boolean visualizador;
	private boolean visualizador2;

	// Tabla Causal
	private int cslId;
	private String cslDescripcion;
	private Integer cslEstado;
	private String cslCodigo;

	private String rutaPdf;

	// Lista de Causales Anulación
	private List<CausalDto> listaCausalesDto;
	// Campo Observacion

	private String causalesSeleccionados;
	
	
	//Tabla Solicitud Tercera Matricula
    private int sltrmtId;
	private Integer sltrmtEstado;
	private Integer sltrmtEstadoRegistro;
	private String sltrmtEstadoLabel;
	private Integer sltrmtTipo;
	private Timestamp sltrmtFechaSolicitud;
	private String sltrmtDocumentoSolicitud;
	private Timestamp sltrmtFechaVerificacion;
	private Timestamp sltrmtFechaRespSolicitud;
	private Timestamp sltrmtFechaApelacion;
	private Timestamp sltrmtFechaResolucion;
	private String sltrmtDocumentoResolucion;
	private String sltrmtObservacion;
	private String sltrmtObservacionFinal;

	public EstudianteJdbcDto() {
		materiaModulo = false;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getFcesId() {
		return fcesId;
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

	public String getFcesTipoDuracRecSt() {
		return fcesTipoDuracRecSt;
	}

	public void setFcesTipoDuracRecSt(String fcesTipoDuracRecSt) {
		this.fcesTipoDuracRecSt = fcesTipoDuracRecSt;
	}

	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}

	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}

	public String getFcesTipoColegioSt() {
		return fcesTipoColegioSt;
	}

	public void setFcesTipoColegioSt(String fcesTipoColegioSt) {
		this.fcesTipoColegioSt = fcesTipoColegioSt;
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

	public Integer getFcesRecEstuPrevios() {
		return fcesRecEstuPrevios;
	}

	public void setFcesRecEstuPrevios(Integer fcesRecEstuPrevios) {
		this.fcesRecEstuPrevios = fcesRecEstuPrevios;
	}

	public String getFcesRecEstuPreviosSt() {
		return fcesRecEstuPreviosSt;
	}

	public void setFcesRecEstuPreviosSt(String fcesRecEstuPreviosSt) {
		this.fcesRecEstuPreviosSt = fcesRecEstuPreviosSt;
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

	public Integer getFcesInacId() {
		return fcesInacId;
	}

	public void setFcesInacId(Integer fcesInacId) {
		this.fcesInacId = fcesInacId;
	}

	public int getInacId() {
		return inacId;
	}

	public void setInacId(int inacId) {
		this.inacId = inacId;
	}

	public String getInacDescripcion() {
		return inacDescripcion;
	}

	public void setInacDescripcion(String inacDescripcion) {
		this.inacDescripcion = inacDescripcion;
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

	public String getPrsTipoIdentificacionSt() {
		return prsTipoIdentificacionSt;
	}

	public void setPrsTipoIdentificacionSt(String prsTipoIdentificacionSt) {
		this.prsTipoIdentificacionSt = prsTipoIdentificacionSt;
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

	public String getPrsSexoSt() {
		return prsSexoSt;
	}

	public void setPrsSexoSt(String prsSexoSt) {
		this.prsSexoSt = prsSexoSt;
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

	public Integer getPrsEtnId() {
		return prsEtnId;
	}

	public void setPrsEtnId(Integer prsEtnId) {
		this.prsEtnId = prsEtnId;
	}

	public Integer getPrsUbcNacimiento() {
		return prsUbcNacimiento;
	}

	public void setPrsUbcNacimiento(Integer prsUbcNacimiento) {
		this.prsUbcNacimiento = prsUbcNacimiento;
	}

	public Integer getPrsUbcResidencia() {
		return prsUbcResidencia;
	}

	public void setPrsUbcResidencia(Integer prsUbcResidencia) {
		this.prsUbcResidencia = prsUbcResidencia;
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

	public Integer getUbcPaisId() {
		return ubcPaisId;
	}

	public void setUbcPaisId(Integer ubcPaisId) {
		this.ubcPaisId = ubcPaisId;
	}

	public String getUbcPaisDescripcion() {
		return ubcPaisDescripcion;
	}

	public void setUbcPaisDescripcion(String ubcPaisDescripcion) {
		this.ubcPaisDescripcion = ubcPaisDescripcion;
	}

	public Integer getUbcCantonId() {
		return ubcCantonId;
	}

	public void setUbcCantonId(Integer ubcCantonId) {
		this.ubcCantonId = ubcCantonId;
	}

	public String getUbcCantonDescripcion() {
		return ubcCantonDescripcion;
	}

	public void setUbcCantonDescripcion(String ubcCantonDescripcion) {
		this.ubcCantonDescripcion = ubcCantonDescripcion;
	}

	public int getEtnId() {
		return etnId;
	}

	public void setEtnId(int etnId) {
		this.etnId = etnId;
	}

	public String getEtnDescripcion() {
		return etnDescripcion;
	}

	public void setEtnDescripcion(String etnDescripcion) {
		this.etnDescripcion = etnDescripcion;
	}

	public int getCncrId() {
		return cncrId;
	}

	public void setCncrId(int cncrId) {
		this.cncrId = cncrId;
	}

	public Integer getCncrCrrId() {
		return cncrCrrId;
	}

	public void setCncrCrrId(Integer cncrCrrId) {
		this.cncrCrrId = cncrCrrId;
	}

	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	public Integer getCrrDpnId() {
		return crrDpnId;
	}

	public void setCrrDpnId(Integer crrDpnId) {
		this.crrDpnId = crrDpnId;
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

	public String getCrrFechaCreacion() {
		return crrFechaCreacion;
	}

	public void setCrrFechaCreacion(String crrFechaCreacion) {
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

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public Integer getDpnSubId() {
		return dpnSubId;
	}

	public void setDpnSubId(Integer dpnSubId) {
		this.dpnSubId = dpnSubId;
	}

	public Integer getDpnUbcId() {
		return dpnUbcId;
	}

	public void setDpnUbcId(Integer dpnUbcId) {
		this.dpnUbcId = dpnUbcId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Integer getDpnJerarquia() {
		return dpnJerarquia;
	}

	public void setDpnJerarquia(Integer dpnJerarquia) {
		this.dpnJerarquia = dpnJerarquia;
	}

	public Integer getDpnEstado() {
		return dpnEstado;
	}

	public void setDpnEstado(Integer dpnEstado) {
		this.dpnEstado = dpnEstado;
	}

	public Integer getDpnCampus() {
		return dpnCampus;
	}

	public void setDpnCampus(Integer dpnCampus) {
		this.dpnCampus = dpnCampus;
	}

	public int getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(int mlcrId) {
		this.mlcrId = mlcrId;
	}

	public String getMlcrDescripcion() {
		return mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
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

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public int getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(int mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public int getPrlId() {
		return prlId;
	}

	public void setPrlId(int prlId) {
		this.prlId = prlId;
	}

	public String getPrlDescripcion() {
		return prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
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

	public Timestamp getDtmtFechaHistorico() {
		return dtmtFechaHistorico;
	}

	public void setDtmtFechaHistorico(Timestamp dtmtFechaHistorico) {
		this.dtmtFechaHistorico = dtmtFechaHistorico;
	}

	public Timestamp getDtmtFechaCambio() {
		return dtmtFechaCambio;
	}

	public void setDtmtFechaCambio(Timestamp dtmtFechaCambio) {
		this.dtmtFechaCambio = dtmtFechaCambio;
	}

	public Integer getDtmtEstadoSolicitud() {
		return dtmtEstadoSolicitud;
	}

	public void setDtmtEstadoSolicitud(Integer dtmtEstadoSolicitud) {
		this.dtmtEstadoSolicitud = dtmtEstadoSolicitud;
	}

	public Timestamp getDtmtFechaSolicitud() {
		return dtmtFechaSolicitud;
	}

	public void setDtmtFechaSolicitud(Timestamp dtmtFechaSolicitud) {
		this.dtmtFechaSolicitud = dtmtFechaSolicitud;
	}

	public Integer getDtmtEstadoRespuesta() {
		return dtmtEstadoRespuesta;
	}

	public void setDtmtEstadoRespuesta(Integer dtmtEstadoRespuesta) {
		this.dtmtEstadoRespuesta = dtmtEstadoRespuesta;
	}

	public Timestamp getDtmtFechaRespuesta() {
		return dtmtFechaRespuesta;
	}

	public void setDtmtFechaRespuesta(Timestamp dtmtFechaRespuesta) {
		this.dtmtFechaRespuesta = dtmtFechaRespuesta;
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

	public int getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(int mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public String getMtrStHotas() {
		return mtrStHotas;
	}

	public void setMtrStHotas(String mtrStHotas) {
		this.mtrStHotas = mtrStHotas;
	}

	public int getMlprId() {
		return mlprId;
	}

	public void setMlprId(int mlprId) {
		this.mlprId = mlprId;
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

	public int getRcesId() {
		return rcesId;
	}

	public void setRcesId(int rcesId) {
		this.rcesId = rcesId;
	}

	public Integer getRcesEstado() {
		return rcesEstado;
	}

	public void setRcesEstado(Integer rcesEstado) {
		this.rcesEstado = rcesEstado;
	}

	public Integer getRcesIngersoNota() {
		return rcesIngersoNota;
	}

	public void setRcesIngersoNota(Integer rcesIngersoNota) {
		this.rcesIngersoNota = rcesIngersoNota;
	}

	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinMatriculado() {
		return fcinMatriculado;
	}

	public void setFcinMatriculado(Integer fcinMatriculado) {
		this.fcinMatriculado = fcinMatriculado;
	}

	public Integer getFcinTipo() {
		return fcinTipo;
	}

	public void setFcinTipo(Integer fcinTipo) {
		this.fcinTipo = fcinTipo;
	}

	public int getDtpsId() {
		return dtpsId;
	}

	public void setDtpsId(int dtpsId) {
		this.dtpsId = dtpsId;
	}

	public int getClfId() {
		return clfId;
	}

	public void setClfId(int clfId) {
		this.clfId = clfId;
	}

	public Integer getClfAsistenciaEstudiante1() {
		return clfAsistenciaEstudiante1;
	}

	public void setClfAsistenciaEstudiante1(Integer clfAsistenciaEstudiante1) {
		this.clfAsistenciaEstudiante1 = clfAsistenciaEstudiante1;
	}

	public Integer getClfAsistenciaEstudiante2() {
		return clfAsistenciaEstudiante2;
	}

	public void setClfAsistenciaEstudiante2(Integer clfAsistenciaEstudiante2) {
		this.clfAsistenciaEstudiante2 = clfAsistenciaEstudiante2;
	}

	public Integer getClfAsistenciaDocente1() {
		return clfAsistenciaDocente1;
	}

	public void setClfAsistenciaDocente1(Integer clfAsistenciaDocente1) {
		this.clfAsistenciaDocente1 = clfAsistenciaDocente1;
	}

	public Integer getClfAsistenciaDocente2() {
		return clfAsistenciaDocente2;
	}

	public void setClfAsistenciaDocente2(Integer clfAsistenciaDocente2) {
		this.clfAsistenciaDocente2 = clfAsistenciaDocente2;
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

	public BigDecimal getClfTotalAsistencia1() {
		return clfTotalAsistencia1;
	}

	public void setClfTotalAsistencia1(BigDecimal clfTotalAsistencia1) {
		this.clfTotalAsistencia1 = clfTotalAsistencia1;
	}

	public BigDecimal getClfTotalAsistencia2() {
		return clfTotalAsistencia2;
	}

	public void setClfTotalAsistencia2(BigDecimal clfTotalAsistencia2) {
		this.clfTotalAsistencia2 = clfTotalAsistencia2;
	}

	public BigDecimal getClfPromedioAsistencia() {
		return clfPromedioAsistencia;
	}

	public void setClfPromedioAsistencia(BigDecimal clfPromedioAsistencia) {
		this.clfPromedioAsistencia = clfPromedioAsistencia;
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

	public BigDecimal getClfAsistenciaTotal() {
		return clfAsistenciaTotal;
	}

	public void setClfAsistenciaTotal(BigDecimal clfAsistenciaTotal) {
		this.clfAsistenciaTotal = clfAsistenciaTotal;
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

	public BigDecimal getClfNotalFinalSemestre() {
		return clfNotalFinalSemestre;
	}

	public void setClfNotalFinalSemestre(BigDecimal clfNotalFinalSemestre) {
		this.clfNotalFinalSemestre = clfNotalFinalSemestre;
	}

	public BigDecimal getClfEstado() {
		return clfEstado;
	}

	public void setClfEstado(BigDecimal clfEstado) {
		this.clfEstado = clfEstado;
	}

	public int getPrclId() {
		return prclId;
	}

	public void setPrclId(int prclId) {
		this.prclId = prclId;
	}

	public Timestamp getPrclFecha() {
		return prclFecha;
	}

	public void setPrclFecha(Timestamp prclFecha) {
		this.prclFecha = prclFecha;
	}

	public int getPrclTipoPorceso() {
		return prclTipoPorceso;
	}

	public void setPrclTipoPorceso(int prclTipoPorceso) {
		this.prclTipoPorceso = prclTipoPorceso;
	}

	public String getPrclObservacion() {
		return prclObservacion;
	}

	public void setPrclObservacion(String prclObservacion) {
		this.prclObservacion = prclObservacion;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public int getFcmtId() {
		return fcmtId;
	}

	public void setFcmtId(int fcmtId) {
		this.fcmtId = fcmtId;
	}

	public Timestamp getFcmtFechaMatricula() {
		return fcmtFechaMatricula;
	}

	public void setFcmtFechaMatricula(Timestamp fcmtFechaMatricula) {
		this.fcmtFechaMatricula = fcmtFechaMatricula;
	}

	public boolean isVisualizador() {
		return visualizador;
	}

	public void setVisualizador(boolean visualizador) {
		this.visualizador = visualizador;
	}

	
	
	public boolean isVisualizador2() {
		return visualizador2;
	}

	public void setVisualizador2(boolean visualizador2) {
		this.visualizador2 = visualizador2;
	}

	public Integer getRcesIngersoNota2() {
		return rcesIngersoNota2;
	}

	public void setRcesIngersoNota2(Integer rcesIngersoNota2) {
		this.rcesIngersoNota2 = rcesIngersoNota2;
	}

	public Integer getRcesIngersoNota3() {
		return rcesIngersoNota3;
	}

	public void setRcesIngersoNota3(Integer rcesIngersoNota3) {
		this.rcesIngersoNota3 = rcesIngersoNota3;
	}

	public BigDecimal getClfSumaP1P2() {
		return clfSumaP1P2;
	}

	public void setClfSumaP1P2(BigDecimal clfSumaP1P2) {
		this.clfSumaP1P2 = clfSumaP1P2;
	}

	public BigDecimal getClfAsistenciaTotalDoc() {
		return clfAsistenciaTotalDoc;
	}

	public void setClfAsistenciaTotalDoc(BigDecimal clfAsistenciaTotalDoc) {
		this.clfAsistenciaTotalDoc = clfAsistenciaTotalDoc;
	}

	public BigDecimal getProme() {
		return prome;
	}

	public void setProme(BigDecimal prome) {
		this.prome = prome;
	}

	public Integer getFcinConfiguracionCarrera() {
		return fcinConfiguracionCarrera;
	}

	public void setFcinConfiguracionCarrera(Integer fcinConfiguracionCarrera) {
		this.fcinConfiguracionCarrera = fcinConfiguracionCarrera;
	}

	public Integer getFcinPeriodoAcademico() {
		return fcinPeriodoAcademico;
	}

	public void setFcinPeriodoAcademico(Integer fcinPeriodoAcademico) {
		this.fcinPeriodoAcademico = fcinPeriodoAcademico;
	}

	public Integer getFcinCarrera() {
		return fcinCarrera;
	}

	public void setFcinCarrera(Integer fcinCarrera) {
		this.fcinCarrera = fcinCarrera;
	}

	public Integer getFcinCarreraSiiu() {
		return fcinCarreraSiiu;
	}

	public void setFcinCarreraSiiu(Integer fcinCarreraSiiu) {
		this.fcinCarreraSiiu = fcinCarreraSiiu;
	}

	public String getFcinCarreraSiiuSt() {
		return fcinCarreraSiiuSt;
	}

	public void setFcinCarreraSiiuSt(String fcinCarreraSiiuSt) {
		this.fcinCarreraSiiuSt = fcinCarreraSiiuSt;
	}

	public Integer getFcinEstado() {
		return fcinEstado;
	}

	public void setFcinEstado(Integer fcinEstado) {
		this.fcinEstado = fcinEstado;
	}

	public Integer getFcinEncuesta() {
		return fcinEncuesta;
	}

	public void setFcinEncuesta(Integer fcinEncuesta) {
		this.fcinEncuesta = fcinEncuesta;
	}

	public Integer getFcinUsroId() {
		return fcinUsroId;
	}

	public void setFcinUsroId(Integer fcinUsroId) {
		this.fcinUsroId = fcinUsroId;
	}

	public Integer getFcinCncrArea() {
		return fcinCncrArea;
	}

	public void setFcinCncrArea(Integer fcinCncrArea) {
		this.fcinCncrArea = fcinCncrArea;
	}

	public Float getFcinNotaEnes() {
		return fcinNotaEnes;
	}

	public void setFcinNotaEnes(Float fcinNotaEnes) {
		this.fcinNotaEnes = fcinNotaEnes;
	}

	public Integer getNvlNumeral() {
		return nvlNumeral;
	}

	public void setNvlNumeral(Integer nvlNumeral) {
		this.nvlNumeral = nvlNumeral;
	}

	public BigDecimal getFcmtValorTotal() {
		return fcmtValorTotal;
	}

	public void setFcmtValorTotal(BigDecimal fcmtValorTotal) {
		this.fcmtValorTotal = fcmtValorTotal;
	}

	public int getFcmtNivelUbicacion() {
		return fcmtNivelUbicacion;
	}

	public void setFcmtNivelUbicacion(int fcmtNivelUbicacion) {
		this.fcmtNivelUbicacion = fcmtNivelUbicacion;
	}

	public Integer getPrsDiscapacidad() {
		return prsDiscapacidad;
	}

	public void setPrsDiscapacidad(Integer prsDiscapacidad) {
		this.prsDiscapacidad = prsDiscapacidad;
	}

	public Integer getPrsTipoDiscapacidad() {
		return prsTipoDiscapacidad;
	}

	public void setPrsTipoDiscapacidad(Integer prsTipoDiscapacidad) {
		this.prsTipoDiscapacidad = prsTipoDiscapacidad;
	}

	public Integer getPrsPorceDiscapacidad() {
		return prsPorceDiscapacidad;
	}

	public void setPrsPorceDiscapacidad(Integer prsPorceDiscapacidad) {
		this.prsPorceDiscapacidad = prsPorceDiscapacidad;
	}

	public String getPrsNumCarnetConadis() {
		return prsNumCarnetConadis;
	}

	public void setPrsNumCarnetConadis(String prsNumCarnetConadis) {
		this.prsNumCarnetConadis = prsNumCarnetConadis;
	}

	public Integer getPrsCarnetConadis() {
		return prsCarnetConadis;
	}

	public void setPrsCarnetConadis(Integer prsCarnetConadis) {
		this.prsCarnetConadis = prsCarnetConadis;
	}

	public String getUbcGentilicio() {
		return ubcGentilicio;
	}

	public void setUbcGentilicio(String ubcGentilicio) {
		this.ubcGentilicio = ubcGentilicio;
	}

	public Integer getUbcCantonColegioId() {
		return ubcCantonColegioId;
	}

	public void setUbcCantonColegioId(Integer ubcCantonColegioId) {
		this.ubcCantonColegioId = ubcCantonColegioId;
	}

	public String getUbcCantonColegioDescripcion() {
		return ubcCantonColegioDescripcion;
	}

	public void setUbcCantonColegioDescripcion(String ubcCantonColegioDescripcion) {
		this.ubcCantonColegioDescripcion = ubcCantonColegioDescripcion;
	}

	public Integer getPrsEstadoCivil() {
		return prsEstadoCivil;
	}

	public void setPrsEstadoCivil(Integer prsEstadoCivil) {
		this.prsEstadoCivil = prsEstadoCivil;
	}

	public Timestamp getPrsFechaActualizacionDatos() {
		return prsFechaActualizacionDatos;
	}

	public void setPrsFechaActualizacionDatos(Timestamp prsFechaActualizacionDatos) {
		this.prsFechaActualizacionDatos = prsFechaActualizacionDatos;
	}

	public Date getPrsFechaVinculacionSeguro() {
		return prsFechaVinculacionSeguro;
	}

	public void setPrsFechaVinculacionSeguro(Date prsFechaVinculacionSeguro) {
		this.prsFechaVinculacionSeguro = prsFechaVinculacionSeguro;
	}

	public String getPrsFormularioSeguro() {
		return prsFormularioSeguro;
	}

	public void setPrsFormularioSeguro(String prsFormularioSeguro) {
		this.prsFormularioSeguro = prsFormularioSeguro;
	}

	public Timestamp getPrsFechaFormularioSeguro() {
		return prsFechaFormularioSeguro;
	}

	public void setPrsFechaFormularioSeguro(Timestamp prsFechaFormularioSeguro) {
		this.prsFechaFormularioSeguro = prsFechaFormularioSeguro;
	}

	public Timestamp getFcesFechaActualizacionDatos() {
		return fcesFechaActualizacionDatos;
	}

	public void setFcesFechaActualizacionDatos(Timestamp fcesFechaActualizacionDatos) {
		this.fcesFechaActualizacionDatos = fcesFechaActualizacionDatos;
	}

	public Integer getUbcParroquiaId() {
		return ubcParroquiaId;
	}

	public void setUbcParroquiaId(Integer ubcParroquiaId) {
		this.ubcParroquiaId = ubcParroquiaId;
	}

	public String getUbcParroquiaDescripcion() {
		return ubcParroquiaDescripcion;
	}

	public void setUbcParroquiaDescripcion(String ubcParroquiaDescripcion) {
		this.ubcParroquiaDescripcion = ubcParroquiaDescripcion;
	}

	public Integer getInacNivel() {
		return inacNivel;
	}

	public void setInacNivel(Integer inacNivel) {
		this.inacNivel = inacNivel;
	}

	public Integer getInacTipo() {
		return inacTipo;
	}

	public void setInacTipo(Integer inacTipo) {
		this.inacTipo = inacTipo;
	}

	public String getInacTipoSniese() {
		return inacTipoSniese;
	}

	public void setInacTipoSniese(String inacTipoSniese) {
		this.inacTipoSniese = inacTipoSniese;
	}

	public int getTtlId() {
		return ttlId;
	}

	public void setTtlId(int ttlId) {
		this.ttlId = ttlId;
	}

	public String getTtlDescripcion() {
		return ttlDescripcion;
	}

	public void setTtlDescripcion(String ttlDescripcion) {
		this.ttlDescripcion = ttlDescripcion;
	}

	public Integer getTtlSexo() {
		return ttlSexo;
	}

	public void setTtlSexo(Integer ttlSexo) {
		this.ttlSexo = ttlSexo;
	}

	public Integer getTtlEstado() {
		return ttlEstado;
	}

	public void setTtlEstado(Integer ttlEstado) {
		this.ttlEstado = ttlEstado;
	}

	public Integer getTtlTipo() {
		return ttlTipo;
	}

	public void setTtlTipo(Integer ttlTipo) {
		this.ttlTipo = ttlTipo;
	}

	public Float getFcesNotaGradoSecundaria() {
		return fcesNotaGradoSecundaria;
	}

	public void setFcesNotaGradoSecundaria(Float fcesNotaGradoSecundaria) {
		this.fcesNotaGradoSecundaria = fcesNotaGradoSecundaria;
	}

	public String getClfNota1String() {
		return clfNota1String;
	}

	public void setClfNota1String(String clfNota1String) {
		this.clfNota1String = clfNota1String;
	}

	public Integer getFcesInacTtlId() {
		return fcesInacTtlId;
	}

	public void setFcesInacTtlId(Integer fcesInacTtlId) {
		this.fcesInacTtlId = fcesInacTtlId;
	}

	public Integer getFcesUbcColegio() {
		return fcesUbcColegio;
	}

	public void setFcesUbcColegio(Integer fcesUbcColegio) {
		this.fcesUbcColegio = fcesUbcColegio;
	}

	public Integer getFcesColegioId() {
		return fcesColegioId;
	}

	public void setFcesColegioId(Integer fcesColegioId) {
		this.fcesColegioId = fcesColegioId;
	}

	public int getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(int mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}

	public BigDecimal getDtmtValorPorMateria() {
		return dtmtValorPorMateria;
	}

	public void setDtmtValorPorMateria(BigDecimal dtmtValorPorMateria) {
		this.dtmtValorPorMateria = dtmtValorPorMateria;
	}

	public int getMtrHorasCien() {
		return mtrHorasCien;
	}

	public void setMtrHorasCien(int mtrHorasCien) {
		this.mtrHorasCien = mtrHorasCien;
	}

	public int getMtrSubMtrId() {
		return mtrSubMtrId;
	}

	public void setMtrSubMtrId(int mtrSubMtrId) {
		this.mtrSubMtrId = mtrSubMtrId;
	}

	public int getUsroId() {
		return usroId;
	}

	public void setUsroId(int usroId) {
		this.usroId = usroId;
	}

	public Integer getFcmtEstado() {
		return fcmtEstado;
	}

	public void setFcmtEstado(Integer fcmtEstado) {
		this.fcmtEstado = fcmtEstado;
	}

	public Integer getEstadoSolicitudRetiro() {
		return estadoSolicitudRetiro;
	}

	public void setEstadoSolicitudRetiro(Integer estadoSolicitudRetiro) {
		this.estadoSolicitudRetiro = estadoSolicitudRetiro;
	}

	public Integer getCmpaEstadoPago() {
		return cmpaEstadoPago;
	}

	public void setCmpaEstadoPago(Integer cmpaEstadoPago) {
		this.cmpaEstadoPago = cmpaEstadoPago;
	}

	public boolean isMateriaModulo() {
		return materiaModulo;
	}

	public void setMateriaModulo(boolean materiaModulo) {
		this.materiaModulo = materiaModulo;
	}

	public Integer getMlcrprIdModulo() {
		return mlcrprIdModulo;
	}

	public void setMlcrprIdModulo(Integer mlcrprIdModulo) {
		this.mlcrprIdModulo = mlcrprIdModulo;
	}

	public boolean isSeleccionModulo() {
		return seleccionModulo;
	}

	public void setSeleccionModulo(boolean seleccionModulo) {
		this.seleccionModulo = seleccionModulo;
	}

	public int getClmdId() {
		return clmdId;
	}

	public void setClmdId(int clmdId) {
		this.clmdId = clmdId;
	}

	public Integer getClmdAsistenciaEstudiante1() {
		return clmdAsistenciaEstudiante1;
	}

	public void setClmdAsistenciaEstudiante1(Integer clmdAsistenciaEstudiante1) {
		this.clmdAsistenciaEstudiante1 = clmdAsistenciaEstudiante1;
	}

	public Integer getClmdAsistenciaEstudiante2() {
		return clmdAsistenciaEstudiante2;
	}

	public void setClmdAsistenciaEstudiante2(Integer clmdAsistenciaEstudiante2) {
		this.clmdAsistenciaEstudiante2 = clmdAsistenciaEstudiante2;
	}

	public Integer getClmdAsistenciaDocente1() {
		return clmdAsistenciaDocente1;
	}

	public void setClmdAsistenciaDocente1(Integer clmdAsistenciaDocente1) {
		this.clmdAsistenciaDocente1 = clmdAsistenciaDocente1;
	}

	public Integer getClmdAsistenciaDocente2() {
		return clmdAsistenciaDocente2;
	}

	public void setClmdAsistenciaDocente2(Integer clmdAsistenciaDocente2) {
		this.clmdAsistenciaDocente2 = clmdAsistenciaDocente2;
	}

	public BigDecimal getClmdNota1() {
		return clmdNota1;
	}

	public void setClmdNota1(BigDecimal clmdNota1) {
		this.clmdNota1 = clmdNota1;
	}

	public String getClmdNota1String() {
		return clmdNota1String;
	}

	public void setClmdNota1String(String clmdNota1String) {
		this.clmdNota1String = clmdNota1String;
	}

	public BigDecimal getClmdNota2() {
		return clmdNota2;
	}

	public void setClmdNota2(BigDecimal clmdNota2) {
		this.clmdNota2 = clmdNota2;
	}

	public BigDecimal getClmdTotalAsistencia1() {
		return clmdTotalAsistencia1;
	}

	public void setClmdTotalAsistencia1(BigDecimal clmdTotalAsistencia1) {
		this.clmdTotalAsistencia1 = clmdTotalAsistencia1;
	}

	public BigDecimal getClmdTotalAsistencia2() {
		return clmdTotalAsistencia2;
	}

	public void setClmdTotalAsistencia2(BigDecimal clmdTotalAsistencia2) {
		this.clmdTotalAsistencia2 = clmdTotalAsistencia2;
	}

	public BigDecimal getClmdPromedioAsistencia() {
		return clmdPromedioAsistencia;
	}

	public void setClmdPromedioAsistencia(BigDecimal clmdPromedioAsistencia) {
		this.clmdPromedioAsistencia = clmdPromedioAsistencia;
	}

	public BigDecimal getClmdPromedioNotas() {
		return clmdPromedioNotas;
	}

	public void setClmdPromedioNotas(BigDecimal clmdPromedioNotas) {
		this.clmdPromedioNotas = clmdPromedioNotas;
	}

	public BigDecimal getClmdSupletorio() {
		return clmdSupletorio;
	}

	public void setClmdSupletorio(BigDecimal clmdSupletorio) {
		this.clmdSupletorio = clmdSupletorio;
	}

	public BigDecimal getClmdAsistenciaTotal() {
		return clmdAsistenciaTotal;
	}

	public void setClmdAsistenciaTotal(BigDecimal clmdAsistenciaTotal) {
		this.clmdAsistenciaTotal = clmdAsistenciaTotal;
	}

	public BigDecimal getClmdParamRecuperacion1() {
		return clmdParamRecuperacion1;
	}

	public void setClmdParamRecuperacion1(BigDecimal clmdParamRecuperacion1) {
		this.clmdParamRecuperacion1 = clmdParamRecuperacion1;
	}

	public BigDecimal getClmdParamRecuperacion2() {
		return clmdParamRecuperacion2;
	}

	public void setClmdParamRecuperacion2(BigDecimal clmdParamRecuperacion2) {
		this.clmdParamRecuperacion2 = clmdParamRecuperacion2;
	}

	public BigDecimal getClmdNotalFinalSemestre() {
		return clmdNotalFinalSemestre;
	}

	public void setClmdNotalFinalSemestre(BigDecimal clmdNotalFinalSemestre) {
		this.clmdNotalFinalSemestre = clmdNotalFinalSemestre;
	}

	public BigDecimal getClmdEstado() {
		return clmdEstado;
	}

	public void setClmdEstado(BigDecimal clmdEstado) {
		this.clmdEstado = clmdEstado;
	}

	public BigDecimal getClmdSumaP1P2() {
		return clmdSumaP1P2;
	}

	public void setClmdSumaP1P2(BigDecimal clmdSumaP1P2) {
		this.clmdSumaP1P2 = clmdSumaP1P2;
	}

	public BigDecimal getClmdAsistenciaTotalDoc() {
		return clmdAsistenciaTotalDoc;
	}

	public void setClmdAsistenciaTotalDoc(BigDecimal clmdAsistenciaTotalDoc) {
		this.clmdAsistenciaTotalDoc = clmdAsistenciaTotalDoc;
	}

	public Integer getFcmtPlcrId() {
		return fcmtPlcrId;
	}

	public void setFcmtPlcrId(Integer fcmtPlcrId) {
		this.fcmtPlcrId = fcmtPlcrId;
	}

	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	public void setFcmtNivelUbicacion(Integer fcmtNivelUbicacion) {
		this.fcmtNivelUbicacion = fcmtNivelUbicacion;
	}

	public BigDecimal getClmdNota2String() {
		return clmdNota2String;
	}

	public void setClmdNota2String(BigDecimal clmdNota2String) {
		this.clmdNota2String = clmdNota2String;
	}

	public String getClfNota2String() {
		return clfNota2String;
	}

	public void setClfNota2String(String clfNota2String) {
		this.clfNota2String = clfNota2String;
	}

	public Integer getFcesTipoUnivEstudPrev() {
		return fcesTipoUnivEstudPrev;
	}

	public void setFcesTipoUnivEstudPrev(Integer fcesTipoUnivEstudPrev) {
		this.fcesTipoUnivEstudPrev = fcesTipoUnivEstudPrev;
	}

	public Integer getFcesEstadoEstudPrev() {
		return fcesEstadoEstudPrev;
	}

	public void setFcesEstadoEstudPrev(Integer fcesEstadoEstudPrev) {
		this.fcesEstadoEstudPrev = fcesEstadoEstudPrev;
	}

	public Integer getFcesUnivEstudPrevId() {
		return fcesUnivEstudPrevId;
	}

	public void setFcesUnivEstudPrevId(Integer fcesUnivEstudPrevId) {
		this.fcesUnivEstudPrevId = fcesUnivEstudPrevId;
	}

	public Integer getFcesTitEstudPrevId() {
		return fcesTitEstudPrevId;
	}

	public void setFcesTitEstudPrevId(Integer fcesTitEstudPrevId) {
		this.fcesTitEstudPrevId = fcesTitEstudPrevId;
	}

	public String getFcesRegTituloPrev() {
		return fcesRegTituloPrev;
	}

	public void setFcesRegTituloPrev(String fcesRegTituloPrev) {
		this.fcesRegTituloPrev = fcesRegTituloPrev;
	}

	public int getCslId() {
		return cslId;
	}

	public void setCslId(int cslId) {
		this.cslId = cslId;
	}

	public String getCslDescripcion() {
		return cslDescripcion;
	}

	public void setCslDescripcion(String cslDescripcion) {
		this.cslDescripcion = cslDescripcion;
	}

	public Integer getCslEstado() {
		return cslEstado;
	}

	public void setCslEstado(Integer cslEstado) {
		this.cslEstado = cslEstado;
	}

	public String getCslCodigo() {
		return cslCodigo;
	}

	public void setCslCodigo(String cslCodigo) {
		this.cslCodigo = cslCodigo;
	}

	public String getRutaPdf() {
		return rutaPdf;
	}

	public void setRutaPdf(String rutaPdf) {
		this.rutaPdf = rutaPdf;
	}

	public Integer getDtmtCslRetiroId() {
		return dtmtCslRetiroId;
	}

	public void setDtmtCslRetiroId(Integer dtmtCslRetiroId) {
		this.dtmtCslRetiroId = dtmtCslRetiroId;
	}

	public String getDtmtArchivoRespuesta() {
		return dtmtArchivoRespuesta;
	}

	public void setDtmtArchivoRespuesta(String dtmtArchivoRespuesta) {
		this.dtmtArchivoRespuesta = dtmtArchivoRespuesta;
	}

	public String getDtmtObservacionFinalRetiro() {
		return dtmtObservacionFinalRetiro;
	}

	public void setDtmtObservacionFinalRetiro(String dtmtObservacionFinalRetiro) {
		this.dtmtObservacionFinalRetiro = dtmtObservacionFinalRetiro;
	}

	public Integer getDtmtTipoAnulacion() {
		return dtmtTipoAnulacion;
	}

	public void setDtmtTipoAnulacion(Integer dtmtTipoAnulacion) {
		this.dtmtTipoAnulacion = dtmtTipoAnulacion;
	}

	public Timestamp getDtmtFechaAnulacion() {
		return dtmtFechaAnulacion;
	}

	public void setDtmtFechaAnulacion(Timestamp dtmtFechaAnulacion) {
		this.dtmtFechaAnulacion = dtmtFechaAnulacion;
	}

	public String getDtmtArchivoAnulacion() {
		return dtmtArchivoAnulacion;
	}

	public void setDtmtArchivoAnulacion(String dtmtArchivoAnulacion) {
		this.dtmtArchivoAnulacion = dtmtArchivoAnulacion;
	}

	public String getDtmtRegistranteAnulacion() {
		return dtmtRegistranteAnulacion;
	}

	public void setDtmtRegistranteAnulacion(String dtmtRegistranteAnulacion) {
		this.dtmtRegistranteAnulacion = dtmtRegistranteAnulacion;
	}

	public String getDtmtObservacionAnulacion() {
		return dtmtObservacionAnulacion;
	}

	public void setDtmtObservacionAnulacion(String dtmtObservacionAnulacion) {
		this.dtmtObservacionAnulacion = dtmtObservacionAnulacion;
	}

	public boolean isDtmtRetiroSolicitudTercera() {
		return dtmtRetiroSolicitudTercera;
	}

	public void setDtmtRetiroSolicitudTercera(boolean dtmtRetiroSolicitudTercera) {
		this.dtmtRetiroSolicitudTercera = dtmtRetiroSolicitudTercera;
	}

	public List<CausalDto> getListaCausalesDto() {
		listaCausalesDto = listaCausalesDto == null ? (new ArrayList<CausalDto>()) : listaCausalesDto;

		return listaCausalesDto;
	}

	public void setListaCausalesDto(List<CausalDto> listaCausalesDto) {
		this.listaCausalesDto = listaCausalesDto;
	}

	public String getCausalesSeleccionados() {
		return causalesSeleccionados;
	}

	public void setCausalesSeleccionados(String causalesSeleccionados) {
		this.causalesSeleccionados = causalesSeleccionados;
	}

	public BigDecimal getCmpaTotalPago() {
		return cmpaTotalPago;
	}

	public void setCmpaTotalPago(BigDecimal cmpaTotalPago) {
		this.cmpaTotalPago = cmpaTotalPago;
	}

	public String getCmpaNumComprobante() {
		return cmpaNumComprobante;
	}

	public void setCmpaNumComprobante(String cmpaNumComprobante) {
		this.cmpaNumComprobante = cmpaNumComprobante;
	}

	public Integer getMtrHorasPAE() {
		return mtrHorasPAE;
	}

	public void setMtrHorasPAE(Integer mtrHorasPAE) {
		this.mtrHorasPAE = mtrHorasPAE;
	}

	public Integer getPrsNumeracion() {
		return prsNumeracion;
	}

	public void setPrsNumeracion(Integer prsNumeracion) {
		this.prsNumeracion = prsNumeracion;
	}

	
	
	
	
	public int getCrrIdArea() {
		return crrIdArea;
	}

	public void setCrrIdArea(int crrIdArea) {
		this.crrIdArea = crrIdArea;
	}

	public String getCrrDescripcionArea() {
		return crrDescripcionArea;
	}

	public void setCrrDescripcionArea(String crrDescripcionArea) {
		this.crrDescripcionArea = crrDescripcionArea;
	}

	
	
	public int getSltrmtId() {
		return sltrmtId;
	}

	public void setSltrmtId(int sltrmtId) {
		this.sltrmtId = sltrmtId;
	}

	public Integer getSltrmtEstado() {
		return sltrmtEstado;
	}

	public void setSltrmtEstado(Integer sltrmtEstado) {
		this.sltrmtEstado = sltrmtEstado;
	}

	public String getSltrmtEstadoLabel() {
		return sltrmtEstadoLabel;
	}

	public void setSltrmtEstadoLabel(String sltrmtEstadoLabel) {
		this.sltrmtEstadoLabel = sltrmtEstadoLabel;
	}

	public Integer getSltrmtTipo() {
		return sltrmtTipo;
	}

	public void setSltrmtTipo(Integer sltrmtTipo) {
		this.sltrmtTipo = sltrmtTipo;
	}

	public Timestamp getSltrmtFechaSolicitud() {
		return sltrmtFechaSolicitud;
	}

	public void setSltrmtFechaSolicitud(Timestamp sltrmtFechaSolicitud) {
		this.sltrmtFechaSolicitud = sltrmtFechaSolicitud;
	}

	public String getSltrmtDocumentoSolicitud() {
		return sltrmtDocumentoSolicitud;
	}

	public void setSltrmtDocumentoSolicitud(String sltrmtDocumentoSolicitud) {
		this.sltrmtDocumentoSolicitud = sltrmtDocumentoSolicitud;
	}

	public Timestamp getSltrmtFechaVerificacion() {
		return sltrmtFechaVerificacion;
	}

	public void setSltrmtFechaVerificacion(Timestamp sltrmtFechaVerificacion) {
		this.sltrmtFechaVerificacion = sltrmtFechaVerificacion;
	}

	public Timestamp getSltrmtFechaRespSolicitud() {
		return sltrmtFechaRespSolicitud;
	}

	public void setSltrmtFechaRespSolicitud(Timestamp sltrmtFechaRespSolicitud) {
		this.sltrmtFechaRespSolicitud = sltrmtFechaRespSolicitud;
	}

	public Timestamp getSltrmtFechaApelacion() {
		return sltrmtFechaApelacion;
	}

	public void setSltrmtFechaApelacion(Timestamp sltrmtFechaApelacion) {
		this.sltrmtFechaApelacion = sltrmtFechaApelacion;
	}

	public Timestamp getSltrmtFechaResolucion() {
		return sltrmtFechaResolucion;
	}

	public void setSltrmtFechaResolucion(Timestamp sltrmtFechaResolucion) {
		this.sltrmtFechaResolucion = sltrmtFechaResolucion;
	}

	public String getSltrmtDocumentoResolucion() {
		return sltrmtDocumentoResolucion;
	}

	public void setSltrmtDocumentoResolucion(String sltrmtDocumentoResolucion) {
		this.sltrmtDocumentoResolucion = sltrmtDocumentoResolucion;
	}

	public String getSltrmtObservacion() {
		return sltrmtObservacion;
	}

	public void setSltrmtObservacion(String sltrmtObservacion) {
		this.sltrmtObservacion = sltrmtObservacion;
	}

	public String getSltrmtObservacionFinal() {
		return sltrmtObservacionFinal;
	}

	public void setSltrmtObservacionFinal(String sltrmtObservacionFinal) {
		this.sltrmtObservacionFinal = sltrmtObservacionFinal;
	}

	
	public Integer getSltrmtEstadoRegistro() {
		return sltrmtEstadoRegistro;
	}

	public void setSltrmtEstadoRegistro(Integer sltrmtEstadoRegistro) {
		this.sltrmtEstadoRegistro = sltrmtEstadoRegistro;
	}

	public Integer getCrrTipoEvaluacion() {
		return crrTipoEvaluacion;
	}

	public void setCrrTipoEvaluacion(Integer crrTipoEvaluacion) {
		this.crrTipoEvaluacion = crrTipoEvaluacion;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	@Override
	public String toString() {
		return "EstudianteJdbcDto [fcesId=" + fcesId + ", fcesFechaInicio=" + fcesFechaInicio
				+ ", fcesFechaEgresamiento=" + fcesFechaEgresamiento + ", fcesFechaActaGrado=" + fcesFechaActaGrado
				+ ", fcesNumActaGrado=" + fcesNumActaGrado + ", fcesFechaRefrendacion=" + fcesFechaRefrendacion
				+ ", fcesNumRefrendacion=" + fcesNumRefrendacion + ", fcesCrrEstudPrevios=" + fcesCrrEstudPrevios
				+ ", fcesTiempoEstudRec=" + fcesTiempoEstudRec + ", fcesTipoDuracRec=" + fcesTipoDuracRec
				+ ", fcesTipoDuracRecSt=" + fcesTipoDuracRecSt + ", fcesTipoColegio=" + fcesTipoColegio
				+ ", fcesTipoColegioSt=" + fcesTipoColegioSt + ", fcesTipoColegioSniese=" + fcesTipoColegioSniese
				+ ", fcesNotaPromAcumulado=" + fcesNotaPromAcumulado + ", fcesNotaTrabTitulacion="
				+ fcesNotaTrabTitulacion + ", fcesLinkTesis=" + fcesLinkTesis + ", fcesRecEstuPrevios="
				+ fcesRecEstuPrevios + ", fcesRecEstuPreviosSt=" + fcesRecEstuPreviosSt + ", fcesRecEstudPrevSniese="
				+ fcesRecEstudPrevSniese + ", fcesFechaCreacion=" + fcesFechaCreacion + ", fcesEstadoUniversitario="
				+ fcesEstadoUniversitario + ", fcesEstadoMatricula=" + fcesEstadoMatricula + ", fcesObservacion="
				+ fcesObservacion + ", fcesTituloBachiller=" + fcesTituloBachiller + ", fcesPrsId=" + fcesPrsId
				+ ", fcesCncrId=" + fcesCncrId + ", fcesInacId=" + fcesInacId + ", inacId=" + inacId
				+ ", inacDescripcion=" + inacDescripcion + ", prsId=" + prsId + ", prsTipoIdentificacion="
				+ prsTipoIdentificacion + ", prsTipoIdentificacionSt=" + prsTipoIdentificacionSt
				+ ", prsTipoIdentificacionSniese=" + prsTipoIdentificacionSniese + ", prsIdentificacion="
				+ prsIdentificacion + ", prsPrimerApellido=" + prsPrimerApellido + ", prsSegundoApellido="
				+ prsSegundoApellido + ", prsNombres=" + prsNombres + ", prsSexo=" + prsSexo + ", prsSexoSt="
				+ prsSexoSt + ", prsSexoSniese=" + prsSexoSniese + ", prsMailPersonal=" + prsMailPersonal
				+ ", prsMailInstitucional=" + prsMailInstitucional + ", prsTelefono=" + prsTelefono
				+ ", prsFechaNacimiento=" + prsFechaNacimiento + ", prsEtnId=" + prsEtnId + ", prsUbcNacimiento="
				+ prsUbcNacimiento + ", prsUbcResidencia=" + prsUbcResidencia + ", prsCallePrincipal="
				+ prsCallePrincipal + ", prsCalleSecundaria=" + prsCalleSecundaria + ", prsSectorDomicilio="
				+ prsSectorDomicilio + ", prsNumeroCasa=" + prsNumeroCasa + ", prsReferenciaDomicilio="
				+ prsReferenciaDomicilio + ", prsCelular=" + prsCelular + ", ubcPaisId=" + ubcPaisId
				+ ", ubcPaisDescripcion=" + ubcPaisDescripcion + ", ubcCantonId=" + ubcCantonId
				+ ", ubcCantonDescripcion=" + ubcCantonDescripcion + ", etnId=" + etnId + ", etnDescripcion="
				+ etnDescripcion + ", cncrId=" + cncrId + ", cncrCrrId=" + cncrCrrId + ", crrId=" + crrId
				+ ", crrDpnId=" + crrDpnId + ", crrDescripcion=" + crrDescripcion + ", crrCodSniese=" + crrCodSniese
				+ ", crrDetalle=" + crrDetalle + ", crrFechaCreacion=" + crrFechaCreacion + ", crrResolucion="
				+ crrResolucion + ", crrTipo=" + crrTipo + " , dpnId=" + dpnId + ", dpnSubId=" + dpnSubId
				+ ", dpnUbcId=" + dpnUbcId + ", dpnDescripcion=" + dpnDescripcion + ", dpnJerarquia=" + dpnJerarquia
				+ ", dpnEstado=" + dpnEstado + ", dpnCampus=" + dpnCampus + ", mlcrId=" + mlcrId + ", mlcrDescripcion="
				+ mlcrDescripcion + ", nvlId=" + nvlId + ", nvlDescripcion=" + nvlDescripcion + ", nvlNumeral="
				+ nvlNumeral + ", mlcrmtId=" + mlcrmtId + ", mlcrprId=" + mlcrprId + ", prlId=" + prlId
				+ ", prlDescripcion=" + prlDescripcion + ", dtmtId=" + dtmtId + ", dtmtNumero=" + dtmtNumero
				+ ", dtmtEstado=" + dtmtEstado + ", dtmtArchivoEstudiantes=" + dtmtArchivoEstudiantes
				+ ", dtmtEstadoHistorico=" + dtmtEstadoHistorico + ", dtmtEstadoCambio=" + dtmtEstadoCambio
				+ ", dtmtObservacionHistorico=" + dtmtObservacionHistorico + ", dtmtObservacionCambio="
				+ dtmtObservacionCambio + ", dtmtFechaHistorico=" + dtmtFechaHistorico + ", dtmtFechaCambio="
				+ dtmtFechaCambio + ", mtrId=" + mtrId + ", mtrCodigo=" + mtrCodigo + ", mtrDescripcion="
				+ mtrDescripcion + ", mtrHoras=" + mtrHoras + ", mtrStHotas=" + mtrStHotas + ", mtrSubMtrId="
				+ mtrSubMtrId + ", mtrSubMtrId=" + mtrSubMtrId + ", mlprId=" + mlprId + ", pracId=" + pracId
				+ ", pracDescripcion=" + pracDescripcion + ", pracEstado=" + pracEstado + ", rcesId=" + rcesId
				+ ", rcesEstado=" + rcesEstado + ", rcesIngersoNota=" + rcesIngersoNota + ", rcesIngersoNota2="
				+ rcesIngersoNota2 + ", rcesIngersoNota3=" + rcesIngersoNota3 + ", fcinId=" + fcinId
				+ ", fcinMatriculado=" + fcinMatriculado + ", fcinTipo=" + fcinTipo + ", fcinConfiguracionCarrera="
				+ fcinConfiguracionCarrera + ", fcinPeriodoAcademico=" + fcinPeriodoAcademico + ", fcinCarrera="
				+ fcinCarrera + ", fcinCarreraSiiu=" + fcinCarreraSiiu + ", fcinEstado=" + fcinEstado
				+ ", fcinEncuesta=" + fcinEncuesta + ", fcinUsroId=" + fcinUsroId + ", fcinCncrArea=" + fcinCncrArea
				+ ", fcinNotaEnes=" + fcinNotaEnes + ", dtpsId=" + dtpsId + ", clfId=" + clfId
				+ ", clfAsistenciaEstudiante1=" + clfAsistenciaEstudiante1 + ", clfAsistenciaEstudiante2="
				+ clfAsistenciaEstudiante2 + ", clfAsistenciaDocente1=" + clfAsistenciaDocente1
				+ ", clfAsistenciaDocente2=" + clfAsistenciaDocente2 + ", clfNota1=" + clfNota1 + ", clfNota2="
				+ clfNota2 + ", clfTotalAsistencia1=" + clfTotalAsistencia1 + ", clfTotalAsistencia2="
				+ clfTotalAsistencia2 + ", clfPromedioAsistencia=" + clfPromedioAsistencia + ", clfPromedioNotas="
				+ clfPromedioNotas + ", clfSupletorio=" + clfSupletorio + ", clfAsistenciaTotal=" + clfAsistenciaTotal
				+ ", clfParamRecuperacion1=" + clfParamRecuperacion1 + ", clfParamRecuperacion2="
				+ clfParamRecuperacion2 + ", clfNotalFinalSemestre=" + clfNotalFinalSemestre + ", clfEstado="
				+ clfEstado + ", clfSumaP1P2=" + clfSumaP1P2 + ", clfAsistenciaTotalDoc=" + clfAsistenciaTotalDoc
				+ ", prclId=" + prclId + ", prclFecha=" + prclFecha + ", prclTipoPorceso=" + prclTipoPorceso
				+ ", prclObservacion=" + prclObservacion + ", fcmtId=" + fcmtId + ", fcmtFechaMatricula="
				+ fcmtFechaMatricula + ", fcmtValorTotal=" + fcmtValorTotal + ", fcmtNivelUbicacion="
				+ fcmtNivelUbicacion + ", prome=" + prome + ", seleccionado=" + seleccionado + ", visualizador="
				+ visualizador + ", visualizador2="	+ visualizador2 + ",prsDiscapacidad=" + prsDiscapacidad + " , prsTipoDiscapacidad=" + prsTipoDiscapacidad
				+ " , prsPorceDiscapacidad=" + prsPorceDiscapacidad + " ,prsNumCarnetConadis=" + prsNumCarnetConadis
				+ ",prsCarnetConadis=" + prsCarnetConadis + ",ubcGentilicio=" + ubcGentilicio + " ,ubcCantonColegioId="
				+ ubcCantonColegioId + ",ubcCantonColegioDescripcion=" + ubcCantonColegioDescripcion
				+ " ,prsEstadoCivil=" + prsEstadoCivil + " ,prsFechaActualizacionDatos=" + prsFechaActualizacionDatos
				+ " ,fcesFechaActualizacionDatos=" + fcesFechaActualizacionDatos + " ,ubcParroquiaDescripcion="
				+ ubcParroquiaDescripcion + " ,ubcParroquiaId=" + ubcParroquiaId + " ,inacNivel=" + inacNivel
				+ " ,inacTipo=" + inacTipo + " ,inacTipoSniese=" + inacTipoSniese + " ,ttlId=" + ttlId
				+ " , ttlDescripcion=" + ttlDescripcion + " ,ttlSexo=" + ttlSexo + " ,ttlEstado=" + ttlEstado
				+ " , ttlTipo=" + ttlTipo + ", fcesNotaGradoSecundaria=" + fcesNotaGradoSecundaria + " , fcesInacTtlId="
				+ fcesInacTtlId + ", fcesUbcColegio=" + fcesUbcColegio + " , fcesColegioId=" + fcesColegioId
				+ " , fcesTipoUnivEstudPrev=" + fcesTipoUnivEstudPrev + " , fcesEstadoEstudPrev=" + fcesEstadoEstudPrev
				+ " , fcesUnivEstudPrevId=" + fcesUnivEstudPrevId + " , fcesTitEstudPrevId=" + fcesTitEstudPrevId
				+ " , cslId=" + cslId + " , cslDescripcion=" + cslDescripcion + " , cslEstado=" + cslEstado
				+ " , cslCodigo=" + cslCodigo + " ,rutaPdf=" + rutaPdf + " ,dtmtCslRetiroId=" + dtmtCslRetiroId
				+ " ,dtmtArchivoRespuesta=" + dtmtArchivoRespuesta + " ,dtmtObservacionFinalRetiro="
				+ dtmtObservacionFinalRetiro + " ,dtmtTipoAnulacion=" + dtmtTipoAnulacion + " ,dtmtFechaAnulacion="
				+ dtmtFechaAnulacion + " ,dtmtArchivoAnulacion=" + dtmtArchivoAnulacion + " ,dtmtRegistranteAnulacion="
				+ dtmtRegistranteAnulacion + " ,dtmtObservacionAnulacion=" + dtmtObservacionAnulacion
				+ " ,causalesSeleccionados=" + causalesSeleccionados + " ,sltrmtId=" + sltrmtId 
				+ " ,sltrmtEstado=" + sltrmtEstado +" ,sltrmtEstadoLabel=" + sltrmtEstadoLabel 
				+" ,sltrmtTipo=" + sltrmtTipo +" ,sltrmtFechaSolicitud=" + sltrmtFechaSolicitud 
				+" ,sltrmtDocumentoSolicitud=" + sltrmtDocumentoSolicitud +" ,sltrmtFechaVerificacion=" + sltrmtFechaVerificacion 
				+" ,sltrmtFechaRespSolicitud=" + sltrmtFechaRespSolicitud +" ,sltrmtFechaApelacion=" + sltrmtFechaApelacion
				+" ,sltrmtFechaResolucion=" + sltrmtFechaResolucion +" ,sltrmtDocumentoResolucion=" + sltrmtDocumentoResolucion
				+" ,sltrmtObservacion=" + sltrmtObservacion +" ,sltrmtObservacionFinal=" + sltrmtObservacionFinal	
				+" ,sltrmtEstadoRegistro=" + sltrmtEstadoRegistro
				+ " ,fcesRegTituloPrev=" + fcesRegTituloPrev + "]";

	}
	
	
}
