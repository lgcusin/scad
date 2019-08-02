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
   
 ARCHIVO:     MateriaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Materia. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02-03-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Clase (DTO) MateriaDto. DTO encargado de manejar los datos de la entidad
 * Materia.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class MateriaDto implements Serializable, Comparable<MateriaDto> {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Materia
	private int mtrId;
	private Integer mtrCmfrId;
	private Integer mtrTpmtId;
	private Integer mtrSubId;
	private Integer mtcNcprId;
	private Integer mtrCrrId;
	private Integer mtrEstado;
	private Integer mtrHoras;
	private Integer mtrCreditos;
	private Integer mtrHorasPorSemana;
	private Integer mtrHorasCien;
	private Integer mtrIntegradoraHoras;
	private Integer mtrPreProfesionalHoras;
	private Integer mtrRelacionTrabajo;
	private Integer mtrUnidadMedida;
	private Integer mtrNumeroMatricula;
	private Integer mtrHorasPracticas;
	private BigDecimal mtrHorasTrabAutonomo;
	private Integer mtrModalidadId;
	private Integer mtrAsignacionCupo;
	private String mtrCodigo;
	private String mtrCodigoSniese;
	private String mtrDescripcion;
	private String mtrEstadoLabel;
	private String mtrPrerequisito;
	private String mtrCorrequisito;
	private Boolean mtrCmbEstado;
	private Boolean mtrCmbEstadoDisable;
	private Integer mtrHorasPracSema;
	private BigDecimal mtrHorasAutonoSema;
	private Integer mtrIdModular;
	private String mtrIdModularDescripcion;
	private Integer mtrHorasPorSemanaPAE;

	private Integer cahrId;
	private Integer cahrMlcrprId;
	private Integer cahrMlcrprIdPrincipal;

	private String rutaPdf;

	// Aula
	private String alaCodigo;
	private String alaDescripcion;
	private Integer alaCapacidad;

	// PersonaDto
	private PersonaDto mtrPersonaDto;

	// Tabla CampoFormacion
	private int cmfrId;
	private Integer cmfrTipo;
	private String cmfrDescripcion;
	private Integer cmfrEstado;

	// Tabla TipoMateria
	private int tpmtId;
	private String tpmtDescripcion;
	private Integer tpmtEstado;

	// Tabla Carrera
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Integer crrDpnId;
	private Integer crrProceso;
	private Integer crrEstado;
	private Integer crrTipo;
	private CarreraDto mtrCarreraDto;

	// Tabla Carrera para Area
	private int crrIdArea;
	private String crrDescripcionArea;

	// Tabla NucleoProblemico
	private int ncprId;
	private String ncprDescripcion;
	private Integer ncprEstado;
	// Tabla NucleoProblemicoCarrera
	private Integer ncprCrrId;

	// CAMPOS PARA LISTA DE MATERIAS A DESPLEGAR EN LA MATRICULA
	// Paralelo
	private Integer prlId;
	private String prlCodigo;
	private String prlDescripcion;
	private Integer prlEstado;

	// Malla curricular materia
	private int mlcrmtId;
	private Integer mlcrmtNivelPreRequisito;
	private Integer mlcrmtMallaCurricular;
	private Integer mlcrmtUnidadFormacion;
	private Integer mlcrmtEstado;
	private Integer mlcrmtNivel;
	private Integer mlcrmtMateria;

	// Malla curricular paralelo
	private int mlcrprId;
	private Integer mlcrprCupo;
	private Integer mlcrprModalidad;
	private Integer mlcrprInscritos;

	// Horario academico
	private int hracId;
	private Integer hracMlcrprId;
	private Integer hracMlcrprIdComp;

	// Nivel
	private int nvlId;
	private String nvlDescripcion;
	private Integer nvlEstado;
	private Integer nvlNumeral;

	List<ParaleloDto> listParalelos;
	Boolean isSeleccionado;

	// notas de la materia
	private BigDecimal notaUno;
	private BigDecimal notaDos;
	private BigDecimal notaTres;
	private BigDecimal notaPromedio;
	private Boolean aprobado;
	private BigDecimal notaSuma;
	private Integer estadoHomologacion;
	Boolean esHomologado;
	Boolean sinNotas;

	// notas de la materia segundo registro homologaciones

	private BigDecimal notaSumaReg2;
	private Float clfPromedioAsistenciaReg2;
	private Integer estadoHomologacionReg2;
	private int numMatriculaReg2;
	private Boolean aprobadoReg2;
	Boolean esHomologadoReg2;

	// Tabla Calificacion
	private int clfId;
	private Integer clfAsistencia1;
	private Integer clfAsistencia2;
	private Integer clfAsistenciaDocente1;
	private Integer clfAsistenciaDocente2;
	private Float clfNota1;
	private Float clfNota2;
	private Float clfPromedioAsistencia;
	private Float clfPromedioNotas;
	private Float clfSupletorio;
	private Float clfTotalAsistencia1;
	private Float clfTotalAsistencia2;
	private Float clfAsistenciaTotal;
	private Float clfParamRecuperacion1;
	private Float clfParamRecuperacion2;
	private Float clfNotaFinalSemestre;
	private Float clfSumaP1P2;
	private Integer clfEstado;
	private Float clfAsistenciaTotalDoc;

	// Tabla CalificacionModulo
	private int clmdId;

	// Tabla Periodo Academico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;

	// Tabla Persona
	private int prsId;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailInstitucional;

	// Tabla Ficha Estudiante
	private int fcesId;

	// Tabla Record Estudiante
	private int rcesId;
	private Integer rcesEstado;
	private Integer rcesIngresoNota;
	private Integer rcesIngresoNota3;
	private Integer rcesMlcrprId;
	private Integer rcesFcesId;
	private Integer rcesEstadoTerceraMatri;
	private String rcesObservacion;
	private Integer rcesModoAprobacionNota1;
	private Integer rcesModoAprobacionNota2;
	private Boolean rcesComboModoAprobacion1;
	private Boolean rcesComboModoAprobacion2;

	// Tabla Ficha Matricula
	private int fcmtId;
	private Date fcmtFechaMatricula;
	private int numMatricula;

	// Tabla Ficha Inscripcion
	private int fcinId;
	private Integer fcinUsroId;
	private Integer fcinCncrArea;
	private FichaInscripcionDto mtrFichaInscripcionDto;

	// Tabla Causal
	private int cslId;
	private String cslDescripcion;
	private Integer cslEstado;
	private String cslCodigo;

	// Tabla Tipo Causal
	private int ticsId;
	private String ticsDescripcion;
	private Integer ticsEstado;

	// Tabla Solicitud Tercera Matricula
	private int sltrmtId;
	private Integer sltrmtEstado;
	private Timestamp sltrmtFechaSolicitud;
	private String sltrmtDocumentoSolicitud;
	private Timestamp sltrmtFechaResolucion;
	private String sltrmtDocumentoResolucion;
	private String sltrmtObservacion;

	// Valor de matricula por Materia
	private BigDecimal valorMatricula;

	// Tabla detalle_Matricula
	private int dtmtId;
	private Integer dtmtNumero;
	private String dtmtNumeroMatricula;

	// Tabla Comprobante_Pago
	private int cmpaId;

	// Dependecnia

	private int dpnId;
	private String dpnDescripcion;

	// Tabla Corequisitos
	private int crqId;
	private Integer crqMtrId;
	private Integer crqMtrCorequisitoId;
	private String crqCodigo;
	private String crqDescripcion;
	private Integer crqEstado;

	// Tabla Prerequisitos
	private int prrId;
	private Integer prrCodigo;
	private String prrDescripcion;
	private Integer prrEstado;
	private Integer prrMtrId;
	private Integer prrMtrPrrId;

	// Tabla MallaCurricular
	private int mlcrId;
	private Integer mlcrCrrId;
	private String mlcrCodigo;
	private String mlcrDescripcion;
	private Integer mlcrEstado;
	private Integer mlcrVigencia;
	private Integer mlcrTipoAprobacion;

	// malla_periodo
	private int mlprId;
	private Integer mlprMallaCurricular;
	private Integer mlprPeriodoAcademico;
	private Integer mlprEstado;

	// grupo
	private Integer grpId;
	private String grpCodigo;
	private String grpDescripcion;
	private Integer grpEstado;
	private Integer grpCrrId;

	private boolean esModulo;

	private List<ParaleloDto> mtrListParalelo;
	private List<GrupoDto> mtrListGrupoDto;
	private List<ModalidadDto> mtrListModalidadDto;

	public MateriaDto() {
		// prlId = GeneralesConstantes.APP_ID_BASE;
		isSeleccionado = false;
	}

	public MateriaDto(String mtrCodigo, String mtrDescripcion, Integer mtrCreditos, Boolean mtrCmbEstado,
			List<ParaleloDto> mtrListParalelo) {
		super();
		this.mtrCodigo = mtrCodigo;
		this.mtrDescripcion = mtrDescripcion;
		this.mtrCreditos = mtrCreditos;
		this.mtrCmbEstado = mtrCmbEstado;
		this.mtrListParalelo = mtrListParalelo;
	}

	public MateriaDto(int mtrId, String mtrCodigo, String mtrDescripcion, String tpmtDescripcion, Integer crqMtrId,
			Integer crqMtrCorequisitoId, Integer crqEstado) {
		super();
		this.mtrId = mtrId;
		this.mtrCodigo = mtrCodigo;
		this.mtrDescripcion = mtrDescripcion;
		this.tpmtDescripcion = tpmtDescripcion;
		this.crqMtrId = crqMtrId;
		this.crqMtrCorequisitoId = crqMtrCorequisitoId;
		this.crqEstado = crqEstado;
	}

	public MateriaDto(String mtrCodigo, String mtrDescripcion, String tpmtDescripcion, Integer prrMtrId,
			Integer prrMtrPrrId, Integer prrEstado) {
		super();

		this.mtrCodigo = mtrCodigo;
		this.mtrDescripcion = mtrDescripcion;
		this.tpmtDescripcion = tpmtDescripcion;
		this.prrEstado = prrEstado;
		this.prrMtrId = prrMtrId;
		this.prrMtrPrrId = prrMtrPrrId;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinUsroId() {
		return fcinUsroId;
	}

	public void setFcinUsroId(Integer fcinUsroId) {
		this.fcinUsroId = fcinUsroId;
	}

	public int getMtrId() {
		return mtrId;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
	}

	public Integer getMtrCmfrId() {
		return mtrCmfrId;
	}

	public void setMtrCmfrId(Integer mtrCmfrId) {
		this.mtrCmfrId = mtrCmfrId;
	}

	public Integer getMtrTpmtId() {
		return mtrTpmtId;
	}

	public void setMtrTpmtId(Integer mtrTpmtId) {
		this.mtrTpmtId = mtrTpmtId;
	}

	public Integer getMtrSubId() {
		return mtrSubId;
	}

	public void setMtrSubId(Integer mtrSubId) {
		this.mtrSubId = mtrSubId;
	}

	public Integer getMtcNcprId() {
		return mtcNcprId;
	}

	public void setMtcNcprId(Integer mtcNcprId) {
		this.mtcNcprId = mtcNcprId;
	}

	public Integer getMtrCrrId() {
		return mtrCrrId;
	}

	public void setMtrCrrId(Integer mtrCrrId) {
		this.mtrCrrId = mtrCrrId;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public String getMtrCodigoSniese() {
		return mtrCodigoSniese;
	}

	public void setMtrCodigoSniese(String mtrCodigoSniese) {
		this.mtrCodigoSniese = mtrCodigoSniese;
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

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public Integer getMtrIntegradoraHoras() {
		return mtrIntegradoraHoras;
	}

	public void setMtrIntegradoraHoras(Integer mtrIntegradoraHoras) {
		this.mtrIntegradoraHoras = mtrIntegradoraHoras;
	}

	public Integer getMtrPreProfesionalHoras() {
		return mtrPreProfesionalHoras;
	}

	public void setMtrPreProfesionalHoras(Integer mtrPreProfesionalHoras) {
		this.mtrPreProfesionalHoras = mtrPreProfesionalHoras;
	}

	public Integer getMtrHorasCien() {
		return mtrHorasCien;
	}

	public void setMtrHorasCien(Integer mtrHorasCien) {
		this.mtrHorasCien = mtrHorasCien;
	}

	public Integer getMtrRelacionTrabajo() {
		return mtrRelacionTrabajo;
	}

	public void setMtrRelacionTrabajo(Integer mtrRelacionTrabajo) {
		this.mtrRelacionTrabajo = mtrRelacionTrabajo;
	}

	public String getRutaPdf() {
		return rutaPdf;
	}

	public void setRutaPdf(String rutaPdf) {
		this.rutaPdf = rutaPdf;
	}

	public Integer getMtrHorasPracticas() {
		return mtrHorasPracticas;
	}

	public void setMtrHorasPracticas(Integer mtrHorasPracticas) {
		this.mtrHorasPracticas = mtrHorasPracticas;
	}

	public BigDecimal getMtrHorasTrabAutonomo() {
		return mtrHorasTrabAutonomo;
	}

	public void setMtrHorasTrabAutonomo(BigDecimal mtrHorasTrabAutonomo) {
		this.mtrHorasTrabAutonomo = mtrHorasTrabAutonomo;
	}

	public int getCmfrId() {
		return cmfrId;
	}

	public void setCmfrId(int cmfrId) {
		this.cmfrId = cmfrId;
	}

	public Integer getCmfrTipo() {
		return cmfrTipo;
	}

	public void setCmfrTipo(Integer cmfrTipo) {
		this.cmfrTipo = cmfrTipo;
	}

	public String getCmfrDescripcion() {
		return cmfrDescripcion;
	}

	public void setCmfrDescripcion(String cmfrDescripcion) {
		this.cmfrDescripcion = cmfrDescripcion;
	}

	public Integer getCmfrEstado() {
		return cmfrEstado;
	}

	public void setCmfrEstado(Integer cmfrEstado) {
		this.cmfrEstado = cmfrEstado;
	}

	public int getTpmtId() {
		return tpmtId;
	}

	public void setTpmtId(int tpmtId) {
		this.tpmtId = tpmtId;
	}

	public String getTpmtDescripcion() {
		return tpmtDescripcion;
	}

	public void setTpmtDescripcion(String tpmtDescripcion) {
		this.tpmtDescripcion = tpmtDescripcion;
	}

	public Integer getTpmtEstado() {
		return tpmtEstado;
	}

	public void setTpmtEstado(Integer tpmtEstado) {
		this.tpmtEstado = tpmtEstado;
	}

	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
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

	public Integer getCrrDpnId() {
		return crrDpnId;
	}

	public void setCrrDpnId(Integer crrDpnId) {
		this.crrDpnId = crrDpnId;
	}

	public int getNcprId() {
		return ncprId;
	}

	public void setNcprId(int ncprId) {
		this.ncprId = ncprId;
	}

	public String getNcprDescripcion() {
		return ncprDescripcion;
	}

	public void setNcprDescripcion(String ncprDescripcion) {
		this.ncprDescripcion = ncprDescripcion;
	}

	public Integer getNcprEstado() {
		return ncprEstado;
	}

	public void setNcprEstado(Integer ncprEstado) {
		this.ncprEstado = ncprEstado;
	}

	public Integer getNcprCrrId() {
		return ncprCrrId;
	}

	public void setNcprCrrId(Integer ncprCrrId) {
		this.ncprCrrId = ncprCrrId;
	}

	public Integer getPrlId() {
		return prlId;
	}

	public void setPrlId(Integer prlId) {
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

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Integer getMlcrmtNivelPreRequisito() {
		return mlcrmtNivelPreRequisito;
	}

	public void setMlcrmtNivelPreRequisito(Integer mlcrmtNivelPreRequisito) {
		this.mlcrmtNivelPreRequisito = mlcrmtNivelPreRequisito;
	}

	public Integer getMlcrmtMallaCurricular() {
		return mlcrmtMallaCurricular;
	}

	public void setMlcrmtMallaCurricular(Integer mlcrmtMallaCurricular) {
		this.mlcrmtMallaCurricular = mlcrmtMallaCurricular;
	}

	public Integer getMlcrmtUnidadFormacion() {
		return mlcrmtUnidadFormacion;
	}

	public void setMlcrmtUnidadFormacion(Integer mlcrmtUnidadFormacion) {
		this.mlcrmtUnidadFormacion = mlcrmtUnidadFormacion;
	}

	public Integer getMlcrmtEstado() {
		return mlcrmtEstado;
	}

	public void setMlcrmtEstado(Integer mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	public Integer getMlcrmtNivel() {
		return mlcrmtNivel;
	}

	public void setMlcrmtNivel(Integer mlcrmtNivel) {
		this.mlcrmtNivel = mlcrmtNivel;
	}

	public Integer getMlcrmtMateria() {
		return mlcrmtMateria;
	}

	public void setMlcrmtMateria(Integer mlcrmtMateria) {
		this.mlcrmtMateria = mlcrmtMateria;
	}

	public int getMlprId() {
		return mlprId;
	}

	public void setMlprId(int mlprId) {
		this.mlprId = mlprId;
	}

	public Integer getMlprMallaCurricular() {
		return mlprMallaCurricular;
	}

	public void setMlprMallaCurricular(Integer mlprMallaCurricular) {
		this.mlprMallaCurricular = mlprMallaCurricular;
	}

	public Integer getMlprPeriodoAcademico() {
		return mlprPeriodoAcademico;
	}

	public void setMlprPeriodoAcademico(Integer mlprPeriodoAcademico) {
		this.mlprPeriodoAcademico = mlprPeriodoAcademico;
	}

	public Integer getMlprEstado() {
		return mlprEstado;
	}

	public void setMlprEstado(Integer mlprEstado) {
		this.mlprEstado = mlprEstado;
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

	public Integer getNvlEstado() {
		return nvlEstado;
	}

	public void setNvlEstado(Integer nvlEstado) {
		this.nvlEstado = nvlEstado;
	}

	public List<ParaleloDto> getListParalelos() {
		return listParalelos;
	}

	public void setListParalelos(List<ParaleloDto> listParalelos) {
		this.listParalelos = listParalelos;
	}

	public Boolean getIsSeleccionado() {
		return isSeleccionado;
	}

	public void setIsSeleccionado(Boolean isSeleccionado) {
		this.isSeleccionado = isSeleccionado;
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

	public int getNumMatricula() {
		return numMatricula;
	}

	public void setNumMatricula(int numMatricula) {
		this.numMatricula = numMatricula;
	}

	public BigDecimal getNotaUno() {
		return notaUno;
	}

	public void setNotaUno(BigDecimal notaUno) {
		this.notaUno = notaUno;
	}

	public BigDecimal getNotaDos() {
		return notaDos;
	}

	public void setNotaDos(BigDecimal notaDos) {
		this.notaDos = notaDos;
	}

	public BigDecimal getNotaTres() {
		return notaTres;
	}

	public void setNotaTres(BigDecimal notaTres) {
		this.notaTres = notaTres;
	}

	public BigDecimal getNotaPromedio() {
		return notaPromedio;
	}

	public void setNotaPromedio(BigDecimal notaPromedio) {
		this.notaPromedio = notaPromedio;
	}

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}

	public int getClfId() {
		return clfId;
	}

	public void setClfId(int clfId) {
		this.clfId = clfId;
	}

	public Integer getClfAsistencia1() {
		return clfAsistencia1;
	}

	public void setClfAsistencia1(Integer clfAsistencia1) {
		this.clfAsistencia1 = clfAsistencia1;
	}

	public Integer getClfAsistencia2() {
		return clfAsistencia2;
	}

	public void setClfAsistencia2(Integer clfAsistencia2) {
		this.clfAsistencia2 = clfAsistencia2;
	}

	public Float getClfNota1() {
		return clfNota1;
	}

	public void setClfNota1(Float clfNota1) {
		this.clfNota1 = clfNota1;
	}

	public Float getClfNota2() {
		return clfNota2;
	}

	public void setClfNota2(Float clfNota2) {
		this.clfNota2 = clfNota2;
	}

	public Float getClfPromedioAsistencia() {
		return clfPromedioAsistencia;
	}

	public void setClfPromedioAsistencia(Float clfPromedioAsistencia) {
		this.clfPromedioAsistencia = clfPromedioAsistencia;
	}

	public Float getClfPromedioNotas() {
		return clfPromedioNotas;
	}

	public void setClfPromedioNotas(Float clfPromedioNotas) {
		this.clfPromedioNotas = clfPromedioNotas;
	}

	public Float getClfSupletorio() {
		return clfSupletorio;
	}

	public void setClfSupletorio(Float clfSupletorio) {
		this.clfSupletorio = clfSupletorio;
	}

	public Float getClfTotalAsistencia1() {
		return clfTotalAsistencia1;
	}

	public void setClfTotalAsistencia1(Float clfTotalAsistencia1) {
		this.clfTotalAsistencia1 = clfTotalAsistencia1;
	}

	public Float getClfTotalAsistencia2() {
		return clfTotalAsistencia2;
	}

	public void setClfTotalAsistencia2(Float clfTotalAsistencia2) {
		this.clfTotalAsistencia2 = clfTotalAsistencia2;
	}

	public Float getClfAsistenciaTotal() {
		return clfAsistenciaTotal;
	}

	public void setClfAsistenciaTotal(Float clfAsistenciaTotal) {
		this.clfAsistenciaTotal = clfAsistenciaTotal;
	}

	public Float getClfParamRecuperacion1() {
		return clfParamRecuperacion1;
	}

	public void setClfParamRecuperacion1(Float clfParamRecuperacion1) {
		this.clfParamRecuperacion1 = clfParamRecuperacion1;
	}

	public Float getClfParamRecuperacion2() {
		return clfParamRecuperacion2;
	}

	public void setClfParamRecuperacion2(Float clfParamRecuperacion2) {
		this.clfParamRecuperacion2 = clfParamRecuperacion2;
	}

	public Float getClfNotaFinalSemestre() {
		return clfNotaFinalSemestre;
	}

	public void setClfNotaFinalSemestre(Float clfNotaFinalSemestre) {
		this.clfNotaFinalSemestre = clfNotaFinalSemestre;
	}

	public Integer getClfEstado() {
		return clfEstado;
	}

	public void setClfEstado(Integer clfEstado) {
		this.clfEstado = clfEstado;
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

	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	public int getFcesId() {
		return fcesId;
	}

	public void setFcesId(int fcesId) {
		this.fcesId = fcesId;
	}

	public int getFcmtId() {
		return fcmtId;
	}

	public void setFcmtId(int fcmtId) {
		this.fcmtId = fcmtId;
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

	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}

	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
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

	public int getCmpaId() {
		return cmpaId;
	}

	public void setCmpaId(int cmpaId) {
		this.cmpaId = cmpaId;
	}

	public Integer getRcesIngresoNota() {
		return rcesIngresoNota;
	}

	public Integer getRcesMlcrprId() {
		return rcesMlcrprId;
	}

	public void setRcesMlcrprId(Integer rcesMlcrprId) {
		this.rcesMlcrprId = rcesMlcrprId;
	}

	public Integer getRcesFcesId() {
		return rcesFcesId;
	}

	public void setRcesFcesId(Integer rcesFcesId) {
		this.rcesFcesId = rcesFcesId;
	}

	public void setRcesIngresoNota(Integer rcesIngresoNota) {
		this.rcesIngresoNota = rcesIngresoNota;
	}

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Float getClfSumaP1P2() {
		return clfSumaP1P2;
	}

	public void setClfSumaP1P2(Float clfSumaP1P2) {
		this.clfSumaP1P2 = clfSumaP1P2;
	}

	public Float getClfAsistenciaTotalDoc() {
		return clfAsistenciaTotalDoc;
	}

	public void setClfAsistenciaTotalDoc(Float clfAsistenciaTotalDoc) {
		this.clfAsistenciaTotalDoc = clfAsistenciaTotalDoc;
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

	public int getCrqId() {
		return crqId;
	}

	public void setCrqId(int crqId) {
		this.crqId = crqId;
	}

	public String getCrqCodigo() {
		return crqCodigo;
	}

	public void setCrqCodigo(String crqCodigo) {
		this.crqCodigo = crqCodigo;
	}

	public String getCrqDescripcion() {
		return crqDescripcion;
	}

	public void setCrqDescripcion(String crqDescripcion) {
		this.crqDescripcion = crqDescripcion;
	}

	public Integer getCrqEstado() {
		return crqEstado;
	}

	public void setCrqEstado(Integer crqEstado) {
		this.crqEstado = crqEstado;
	}

	public int getPrrId() {
		return prrId;
	}

	public void setPrrId(int prrId) {
		this.prrId = prrId;
	}

	public Integer getPrrCodigo() {
		return prrCodigo;
	}

	public void setPrrCodigo(Integer prrCodigo) {
		this.prrCodigo = prrCodigo;
	}

	public String getPrrDescripcion() {
		return prrDescripcion;
	}

	public void setPrrDescripcion(String prrDescripcion) {
		this.prrDescripcion = prrDescripcion;
	}

	public Integer getPrrEstado() {
		return prrEstado;
	}

	public void setPrrEstado(Integer prrEstado) {
		this.prrEstado = prrEstado;
	}

	public Integer getMtrUnidadMedida() {
		return mtrUnidadMedida;
	}

	public void setMtrUnidadMedida(Integer mtrUnidadMedida) {
		this.mtrUnidadMedida = mtrUnidadMedida;
	}

	public Integer getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(Integer mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}

	public Integer getCrqMtrId() {
		return crqMtrId;
	}

	public void setCrqMtrId(Integer crqMtrId) {
		this.crqMtrId = crqMtrId;
	}

	public Integer getCrqMtrCorequisitoId() {
		return crqMtrCorequisitoId;
	}

	public void setCrqMtrCorequisitoId(Integer crqMtrCorequisitoId) {
		this.crqMtrCorequisitoId = crqMtrCorequisitoId;
	}

	public Integer getPrrMtrId() {
		return prrMtrId;
	}

	public void setPrrMtrId(Integer prrMtrId) {
		this.prrMtrId = prrMtrId;
	}

	public Integer getPrrMtrPrrId() {
		return prrMtrPrrId;
	}

	public void setPrrMtrPrrId(Integer prrMtrPrrId) {
		this.prrMtrPrrId = prrMtrPrrId;
	}

	public int getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(int mlcrId) {
		this.mlcrId = mlcrId;
	}

	public Integer getMlcrCrrId() {
		return mlcrCrrId;
	}

	public void setMlcrCrrId(Integer mlcrCrrId) {
		this.mlcrCrrId = mlcrCrrId;
	}

	public String getMlcrCodigo() {
		return mlcrCodigo;
	}

	public void setMlcrCodigo(String mlcrCodigo) {
		this.mlcrCodigo = mlcrCodigo;
	}

	public String getMlcrDescripcion() {
		return mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
	}

	public Integer getMlcrEstado() {
		return mlcrEstado;
	}

	public void setMlcrEstado(Integer mlcrEstado) {
		this.mlcrEstado = mlcrEstado;
	}

	public Integer getMlcrVigencia() {
		return mlcrVigencia;
	}

	public void setMlcrVigencia(Integer mlcrVigencia) {
		this.mlcrVigencia = mlcrVigencia;
	}

	public Integer getMlcrTipoAprobacion() {
		return mlcrTipoAprobacion;
	}

	public Boolean getMtrCmbEstadoDisable() {
		return mtrCmbEstadoDisable;
	}

	public void setMtrCmbEstadoDisable(Boolean mtrCmbEstadoDisable) {
		this.mtrCmbEstadoDisable = mtrCmbEstadoDisable;
	}

	public Integer getMtrHorasPracSema() {
		return mtrHorasPracSema;
	}

	public void setMtrHorasPracSema(Integer mtrHorasPracSema) {
		this.mtrHorasPracSema = mtrHorasPracSema;
	}

	public BigDecimal getMtrHorasAutonoSema() {
		return mtrHorasAutonoSema;
	}

	public void setMtrHorasAutonoSema(BigDecimal mtrHorasAutonoSema) {
		this.mtrHorasAutonoSema = mtrHorasAutonoSema;
	}

	public void setMlcrTipoAprobacion(Integer mlcrTipoAprobacion) {
		this.mlcrTipoAprobacion = mlcrTipoAprobacion;
	}

	public BigDecimal getNotaSuma() {
		return notaSuma;
	}

	public void setNotaSuma(BigDecimal notaSuma) {
		this.notaSuma = notaSuma;
	}

	public Integer getEstadoHomologacion() {
		return estadoHomologacion;
	}

	public void setEstadoHomologacion(Integer estadoHomologacion) {
		this.estadoHomologacion = estadoHomologacion;
	}

	public Boolean getEsHomologado() {
		return esHomologado;
	}

	public void setEsHomologado(Boolean esHomologado) {
		this.esHomologado = esHomologado;
	}

	public Boolean getSinNotas() {
		return sinNotas;
	}

	public void setSinNotas(Boolean sinNotas) {
		this.sinNotas = sinNotas;
	}

	public List<ParaleloDto> getMtrListParalelo() {
		return mtrListParalelo;
	}

	public void setMtrListParalelo(List<ParaleloDto> mtrListParalelo) {
		this.mtrListParalelo = mtrListParalelo;
	}

	public Integer getFcinCncrArea() {
		return fcinCncrArea;
	}

	public void setFcinCncrArea(Integer fcinCncrArea) {
		this.fcinCncrArea = fcinCncrArea;
	}

	public Integer getNvlNumeral() {
		return nvlNumeral;
	}

	public void setNvlNumeral(Integer nvlNumeral) {
		this.nvlNumeral = nvlNumeral;
	}

	public Boolean getMtrCmbEstado() {
		return mtrCmbEstado;
	}

	public void setMtrCmbEstado(Boolean mtrCmbEstado) {
		this.mtrCmbEstado = mtrCmbEstado;
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

	public String getCslCodigo() {
		return cslCodigo;
	}

	public void setCslCodigo(String cslCodigo) {
		this.cslCodigo = cslCodigo;
	}

	public Integer getCslEstado() {
		return cslEstado;
	}

	public void setCslEstado(Integer cslEstado) {
		this.cslEstado = cslEstado;
	}

	public int getTicsId() {
		return ticsId;
	}

	public void setTicsId(int ticsId) {
		this.ticsId = ticsId;
	}

	public String getTicsDescripcion() {
		return ticsDescripcion;
	}

	public void setTicsDescripcion(String ticsDescripcion) {
		this.ticsDescripcion = ticsDescripcion;
	}

	public Integer getTicsEstado() {
		return ticsEstado;
	}

	public void setTicsEstado(Integer ticsEstado) {
		this.ticsEstado = ticsEstado;
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

	public Integer getRcesEstadoTerceraMatri() {
		return rcesEstadoTerceraMatri;
	}

	public void setRcesEstadoTerceraMatri(Integer rcesEstadoTerceraMatri) {
		this.rcesEstadoTerceraMatri = rcesEstadoTerceraMatri;
	}

	public BigDecimal getValorMatricula() {
		return valorMatricula;
	}

	public void setValorMatricula(BigDecimal valorMatricula) {
		this.valorMatricula = valorMatricula;
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

	public Date getFcmtFechaMatricula() {
		return fcmtFechaMatricula;
	}

	public void setFcmtFechaMatricula(Date fcmtFechaMatricula) {
		this.fcmtFechaMatricula = fcmtFechaMatricula;
	}

	public String getDtmtNumeroMatricula() {
		return dtmtNumeroMatricula;
	}

	public void setDtmtNumeroMatricula(String dtmtNumeroMatricula) {
		this.dtmtNumeroMatricula = dtmtNumeroMatricula;
	}

	public BigDecimal getNotaSumaReg2() {
		return notaSumaReg2;
	}

	public void setNotaSumaReg2(BigDecimal notaSumaReg2) {
		this.notaSumaReg2 = notaSumaReg2;
	}

	public Float getClfPromedioAsistenciaReg2() {
		return clfPromedioAsistenciaReg2;
	}

	public void setClfPromedioAsistenciaReg2(Float clfPromedioAsistenciaReg2) {
		this.clfPromedioAsistenciaReg2 = clfPromedioAsistenciaReg2;
	}

	public Integer getEstadoHomologacionReg2() {
		return estadoHomologacionReg2;
	}

	public void setEstadoHomologacionReg2(Integer estadoHomologacionReg2) {
		this.estadoHomologacionReg2 = estadoHomologacionReg2;
	}

	public int getNumMatriculaReg2() {
		return numMatriculaReg2;
	}

	public void setNumMatriculaReg2(int numMatriculaReg2) {
		this.numMatriculaReg2 = numMatriculaReg2;
	}

	public Boolean getEsHomologadoReg2() {
		return esHomologadoReg2;
	}

	public void setEsHomologadoReg2(Boolean esHomologadoReg2) {
		this.esHomologadoReg2 = esHomologadoReg2;
	}

	public Integer getMlcrprInscritos() {
		return mlcrprInscritos;
	}

	public void setMlcrprInscritos(Integer mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

	public Boolean getAprobadoReg2() {
		return aprobadoReg2;
	}

	public void setAprobadoReg2(Boolean aprobadoReg2) {
		this.aprobadoReg2 = aprobadoReg2;
	}

	public String getRcesObservacion() {
		return rcesObservacion;
	}

	public void setRcesObservacion(String rcesObservacion) {
		this.rcesObservacion = rcesObservacion;
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

	public Integer getRcesModoAprobacionNota1() {
		return rcesModoAprobacionNota1;
	}

	public void setRcesModoAprobacionNota1(Integer rcesModoAprobacionNota1) {
		this.rcesModoAprobacionNota1 = rcesModoAprobacionNota1;
	}

	public Integer getRcesModoAprobacionNota2() {
		return rcesModoAprobacionNota2;
	}

	public void setRcesModoAprobacionNota2(Integer rcesModoAprobacionNota2) {
		this.rcesModoAprobacionNota2 = rcesModoAprobacionNota2;
	}

	public Boolean getRcesComboModoAprobacion1() {
		return rcesComboModoAprobacion1;
	}

	public void setRcesComboModoAprobacion1(Boolean rcesComboModoAprobacion1) {
		this.rcesComboModoAprobacion1 = rcesComboModoAprobacion1;
	}

	public Boolean getRcesComboModoAprobacion2() {
		return rcesComboModoAprobacion2;
	}

	public void setRcesComboModoAprobacion2(Boolean rcesComboModoAprobacion2) {
		this.rcesComboModoAprobacion2 = rcesComboModoAprobacion2;
	}

	public String getMtrEstadoLabel() {
		return mtrEstadoLabel;
	}

	public void setMtrEstadoLabel(String mtrEstadoLabel) {
		this.mtrEstadoLabel = mtrEstadoLabel;
	}

	public Integer getMtrNumeroMatricula() {
		return mtrNumeroMatricula;
	}

	public void setMtrNumeroMatricula(Integer mtrNumeroMatricula) {
		this.mtrNumeroMatricula = mtrNumeroMatricula;
	}

	public boolean isEsModulo() {
		return esModulo;
	}

	public void setEsModulo(boolean esModulo) {
		this.esModulo = esModulo;
	}

	public Integer getRcesIngresoNota3() {
		return rcesIngresoNota3;
	}

	public void setRcesIngresoNota3(Integer rcesIngresoNota3) {
		this.rcesIngresoNota3 = rcesIngresoNota3;
	}

	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	public Integer getCrrEstado() {
		return crrEstado;
	}

	public void setCrrEstado(Integer crrEstado) {
		this.crrEstado = crrEstado;
	}

	public CarreraDto getMtrCarreraDto() {
		return mtrCarreraDto;
	}

	public void setMtrCarreraDto(CarreraDto mtrCarreraDto) {
		this.mtrCarreraDto = mtrCarreraDto;
	}

	public FichaInscripcionDto getMtrFichaInscripcionDto() {
		return mtrFichaInscripcionDto;
	}

	public void setMtrFichaInscripcionDto(FichaInscripcionDto mtrFichaInscripcionDto) {
		this.mtrFichaInscripcionDto = mtrFichaInscripcionDto;
	}

	public Integer getMtrHorasPorSemana() {
		return mtrHorasPorSemana;
	}

	public void setMtrHorasPorSemana(Integer mtrHorasPorSemana) {
		this.mtrHorasPorSemana = mtrHorasPorSemana;
	}

	public List<ModalidadDto> getMtrListModalidadDto() {
		return mtrListModalidadDto;
	}

	public void setMtrListModalidadDto(List<ModalidadDto> mtrListModalidadDto) {
		this.mtrListModalidadDto = mtrListModalidadDto;
	}

	public Integer getMtrModalidadId() {
		return mtrModalidadId;
	}

	public void setMtrModalidadId(Integer mtrModalidadId) {
		this.mtrModalidadId = mtrModalidadId;
	}

	public int getHracId() {
		return hracId;
	}

	public void setHracId(int hracId) {
		this.hracId = hracId;
	}

	public Integer getHracMlcrprId() {
		return hracMlcrprId;
	}

	public void setHracMlcrprId(Integer hracMlcrprId) {
		this.hracMlcrprId = hracMlcrprId;
	}

	public Integer getHracMlcrprIdComp() {
		return hracMlcrprIdComp;
	}

	public void setHracMlcrprIdComp(Integer hracMlcrprIdComp) {
		this.hracMlcrprIdComp = hracMlcrprIdComp;
	}

	public Integer getMlcrprModalidad() {
		return mlcrprModalidad;
	}

	public void setMlcrprModalidad(Integer mlcrprModalidad) {
		this.mlcrprModalidad = mlcrprModalidad;
	}

	public Integer getMtrAsignacionCupo() {
		return mtrAsignacionCupo;
	}

	public void setMtrAsignacionCupo(Integer mtrAsignacionCupo) {
		this.mtrAsignacionCupo = mtrAsignacionCupo;
	}

	public Integer getAlaCapacidad() {
		return alaCapacidad;
	}

	public void setAlaCapacidad(Integer alaCapacidad) {
		this.alaCapacidad = alaCapacidad;
	}

	public Integer getGrpId() {
		return grpId;
	}

	public void setGrpId(Integer grpId) {
		this.grpId = grpId;
	}

	public String getGrpCodigo() {
		return grpCodigo;
	}

	public void setGrpCodigo(String grpCodigo) {
		this.grpCodigo = grpCodigo;
	}

	public String getGrpDescripcion() {
		return grpDescripcion;
	}

	public void setGrpDescripcion(String grpDescripcion) {
		this.grpDescripcion = grpDescripcion;
	}

	public Integer getGrpEstado() {
		return grpEstado;
	}

	public void setGrpEstado(Integer grpEstado) {
		this.grpEstado = grpEstado;
	}

	public Integer getGrpCrrId() {
		return grpCrrId;
	}

	public void setGrpCrrId(Integer grpCrrId) {
		this.grpCrrId = grpCrrId;
	}

	public List<GrupoDto> getMtrListGrupoDto() {
		return mtrListGrupoDto;
	}

	public void setMtrListGrupoDto(List<GrupoDto> mtrListGrupoDto) {
		this.mtrListGrupoDto = mtrListGrupoDto;
	}

	public String getMtrPrerequisito() {
		return mtrPrerequisito;
	}

	public void setMtrPrerequisito(String mtrPrerequisito) {
		this.mtrPrerequisito = mtrPrerequisito;
	}

	public String getMtrCorrequisito() {
		return mtrCorrequisito;
	}

	public void setMtrCorrequisito(String mtrCorrequisito) {
		this.mtrCorrequisito = mtrCorrequisito;
	}

	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	public Integer getCahrId() {
		return cahrId;
	}

	public void setCahrId(Integer cahrId) {
		this.cahrId = cahrId;
	}

	public Integer getCahrMlcrprId() {
		return cahrMlcrprId;
	}

	public void setCahrMlcrprId(Integer cahrMlcrprId) {
		this.cahrMlcrprId = cahrMlcrprId;
	}

	public Integer getCahrMlcrprIdPrincipal() {
		return cahrMlcrprIdPrincipal;
	}

	public void setCahrMlcrprIdPrincipal(Integer cahrMlcrprIdPrincipal) {
		this.cahrMlcrprIdPrincipal = cahrMlcrprIdPrincipal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mtrCodigo == null) ? 0 : mtrCodigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MateriaDto other = (MateriaDto) obj;
		if (mtrCodigo == null) {
			if (other.mtrCodigo != null)
				return false;
		} else if (!mtrCodigo.equals(other.mtrCodigo))
			return false;
		return true;
	}

	@Override
	public int compareTo(MateriaDto o) {
		return o.getMtrCodigo().compareToIgnoreCase(getMtrCodigo());
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

	public Integer getMtrHorasPorSemanaPAE() {
		return mtrHorasPorSemanaPAE;
	}

	public void setMtrHorasPorSemanaPAE(Integer mtrHorasPorSemanaPAE) {
		this.mtrHorasPorSemanaPAE = mtrHorasPorSemanaPAE;
	}

	public int getClmdId() {
		return clmdId;
	}

	public void setClmdId(int clmdId) {
		this.clmdId = clmdId;
	}

	public Integer getMtrIdModular() {
		return mtrIdModular;
	}

	public void setMtrIdModular(Integer mtrIdModular) {
		this.mtrIdModular = mtrIdModular;
	}

	public String getMtrIdModularDescripcion() {
		return mtrIdModularDescripcion;
	}

	public void setMtrIdModularDescripcion(String mtrIdModularDescripcion) {
		this.mtrIdModularDescripcion = mtrIdModularDescripcion;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("Materia");
		sb.append(tabulador + "mtrId : " + mtrId);
		sb.append(tabulador + "mtrCmfrId : " + (mtrCmfrId == null ? "NULL" : mtrCmfrId));
		sb.append(tabulador + "mtrTpmtId : " + (mtrTpmtId == null ? "NULL" : mtrTpmtId));
		sb.append(tabulador + "mtrSubId : " + (mtrSubId == null ? "NULL" : mtrSubId));
		sb.append(tabulador + "mtcNcprId : " + (mtcNcprId == null ? "NULL" : mtcNcprId));
		sb.append(tabulador + "mtrCrrId : " + (mtrCrrId == null ? "NULL" : mtrCrrId));
		sb.append(tabulador + "mtrCodigo : " + (mtrCodigo == null ? "NULL" : mtrCodigo));
		sb.append(tabulador + "mtrCodigoSniese : " + (mtrCodigoSniese == null ? "NULL" : mtrCodigoSniese));
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));
		sb.append(tabulador + "mtrEstado : " + (mtrEstado == null ? "NULL" : mtrEstado));
		sb.append(tabulador + "mtrHoras : " + (mtrHoras == null ? "NULL" : mtrHoras));
		sb.append(tabulador + "mtrIntegradoraHoras : " + (mtrIntegradoraHoras == null ? "NULL" : mtrIntegradoraHoras));
		sb.append(tabulador + "mtrPreProfesionalHoras : "
				+ (mtrPreProfesionalHoras == null ? "NULL" : mtrPreProfesionalHoras));
		sb.append(tabulador + "mtrHorasCien : " + (mtrHorasCien == null ? "NULL" : mtrHorasCien));
		sb.append(tabulador + "mtrRelacionTrabajo : " + (mtrRelacionTrabajo == null ? "NULL" : mtrRelacionTrabajo));
		sb.append(tabulador + "mtrUnidadMedida : " + (mtrUnidadMedida == null ? "NULL" : mtrUnidadMedida));
		sb.append(tabulador + "mtrCreditos : " + (mtrCreditos == null ? "NULL" : mtrCreditos));
		sb.append(tabulador + "rutaPdf : " + (rutaPdf == null ? "NULL" : rutaPdf));
		sb.append(tabulador + "mtrHorasPracticas : " + (mtrHorasPracticas == null ? "NULL" : mtrHorasPracticas));
		sb.append(
				tabulador + "mtrHorasTrabAutonomo : " + (mtrHorasTrabAutonomo == null ? "NULL" : mtrHorasTrabAutonomo));
		sb.append(tabulador + "mtrHorasPracSema : " + (mtrHorasPracSema == null ? "NULL" : mtrHorasPracSema));
		sb.append(tabulador + "mtrHorasAutonoSema : " + (mtrHorasAutonoSema == null ? "NULL" : mtrHorasAutonoSema));

		sb.append("CampoFormacion");
		sb.append(tabulador + "cmfrId : " + cmfrId);
		sb.append(tabulador + "cmfrTipo : " + (cmfrTipo == null ? "NULL" : cmfrTipo));
		sb.append(tabulador + "cmfrDescripcion : " + (cmfrDescripcion == null ? "NULL" : cmfrDescripcion));
		sb.append(tabulador + "cmfrEstado : " + (cmfrEstado == null ? "NULL" : cmfrEstado));

		sb.append("TipoMateria");
		sb.append(tabulador + "tpmtId : " + tpmtId);
		sb.append(tabulador + "tpmtDescripcion : " + (tpmtDescripcion == null ? "NULL" : tpmtDescripcion));
		sb.append(tabulador + "tpmtEstado : " + (tpmtEstado == null ? "NULL" : tpmtEstado));

		sb.append("Carrera");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrCodSniese : " + (crrCodSniese == null ? "NULL" : crrCodSniese));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrDpnId : " + (crrDpnId == null ? "NULL" : crrDpnId));

		sb.append("NucleoProblemicoCarrera");
		sb.append(tabulador + "ncprId : " + ncprId);
		sb.append(tabulador + "ncprDescripcion : " + (ncprDescripcion == null ? "NULL" : ncprDescripcion));
		sb.append(tabulador + "ncprEstado : " + (ncprEstado == null ? "NULL" : ncprEstado));
		sb.append(tabulador + "ncprCrrId : " + (ncprCrrId == null ? "NULL" : ncprCrrId));

		// CAMPOS PARA LISTA DE MATERIAS A DESPLEGAR EN LA MATRICULA

		sb.append("Paralelo");
		sb.append(tabulador + "prlId : " + prlId);
		sb.append(tabulador + "prlCodigo : " + (prlCodigo == null ? "NULL" : prlCodigo));
		sb.append(tabulador + "prlDescripcion : " + (prlDescripcion == null ? "NULL" : prlDescripcion));
		sb.append(tabulador + "prlEstado : " + (prlEstado == null ? "NULL" : prlEstado));

		sb.append("MallaCuricularMateria");
		sb.append(tabulador + "mlcrmtId : " + mlcrmtId);
		sb.append(tabulador + "mlcrmtNivelPreRequisito : "
				+ (mlcrmtNivelPreRequisito == null ? "NULL" : mlcrmtNivelPreRequisito));
		sb.append(tabulador + "mlcrmtMallaCurricular : "
				+ (mlcrmtMallaCurricular == null ? "NULL" : mlcrmtMallaCurricular));
		sb.append(tabulador + "mlcrmtUnidadFormacion : "
				+ (mlcrmtUnidadFormacion == null ? "NULL" : mlcrmtUnidadFormacion));
		sb.append(tabulador + "mlcrmtEstado : " + (mlcrmtEstado == null ? "NULL" : mlcrmtEstado));

		sb.append("MallaCuricularParalelo");
		sb.append(tabulador + "mlcrprId : " + mlcrprId);
		sb.append(tabulador + "mlcrprCupo : " + mlcrprCupo);

		sb.append("Nivel");
		sb.append(tabulador + "nvlId : " + nvlId);
		sb.append(tabulador + "nvlDescripcion : " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));
		sb.append(tabulador + "nvlEstado : " + (nvlEstado == null ? "NULL" : nvlEstado));

		sb.append("Calificacion");
		sb.append(tabulador + "clfId : " + clfId);
		sb.append(tabulador + "clfAsistencia1 : " + clfAsistencia1);
		sb.append(tabulador + "clfAsistencia2 : " + clfAsistencia2);
		sb.append(tabulador + "clfNota1 : " + clfNota1);
		sb.append(tabulador + "clfNota2 : " + clfNota2);
		sb.append(tabulador + "clfPromedioAsistencia : " + clfPromedioAsistencia);
		sb.append(tabulador + "clfPromedioNotas : " + clfPromedioNotas);
		sb.append(tabulador + "clfSupletorio : " + clfSupletorio);
		sb.append(tabulador + "clfTotalAsistencia1 : " + clfTotalAsistencia1);
		sb.append(tabulador + "clfTotalAsistencia2 : " + clfTotalAsistencia2);
		sb.append(tabulador + "clfAsistenciaTotal : " + clfAsistenciaTotal);
		sb.append(tabulador + "clfParamRecuperacion1 : " + clfParamRecuperacion1);
		sb.append(tabulador + "clfParamRecuperacion2 : " + clfParamRecuperacion2);
		sb.append(tabulador + "clfNotaFinalSemestre : " + clfNotaFinalSemestre);
		sb.append(tabulador + "clfEstado : " + clfEstado);
		sb.append(tabulador + "clfSumaP1P2 : " + clfSumaP1P2);
		sb.append(tabulador + "clfAsistenciaTotalDoc : " + clfAsistenciaTotalDoc);
		sb.append(tabulador + "clfAsistenciaDocente1 : " + clfAsistenciaDocente1);
		sb.append(tabulador + "clfAsistenciaDocente2 : " + clfAsistenciaDocente2);

		sb.append("Periodo Academico");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));

		sb.append("Persona");
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsIdentificacion: " + (prsIdentificacion == null ? "NULL" : prsIdentificacion));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido == null ? "NULL" : prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido == null ? "NULL" : prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres == null ? "NULL" : prsNombres));
		sb.append(
				tabulador + "prsMailInstitucional : " + (prsMailInstitucional == null ? "NULL" : prsMailInstitucional));

		sb.append("Ficha Estudiante");
		sb.append(tabulador + "fcesId : " + fcesId);

		sb.append("Ficha Matricula");
		sb.append(tabulador + "fcmtId : " + fcmtId);

		sb.append("NumeroMatricula");
		sb.append(tabulador + "numMatricula : " + numMatricula);

		sb.append("EstadoHomologacion");
		sb.append(tabulador + "estadoHomologacion : " + (estadoHomologacion == null ? "NULL" : estadoHomologacion));

		sb.append("Record Estudiante");
		sb.append(tabulador + "rcesId : " + rcesId);
		sb.append(tabulador + "rcesEstado : " + rcesEstado);
		sb.append(tabulador + "rcesIngresoNota : " + (rcesIngresoNota == null ? "NULL" : rcesIngresoNota));
		sb.append(tabulador + "rcesEstadoTerceraMatri : " + rcesEstadoTerceraMatri);
		sb.append(tabulador + "rcesObservacion : " + (rcesObservacion == null ? "NULL" : rcesObservacion));
		sb.append(tabulador + "rcesModoAprobacionNota1 : "
				+ (rcesModoAprobacionNota1 == null ? "NULL" : rcesModoAprobacionNota1));
		sb.append(tabulador + "rcesModoAprobacionNota2 : "
				+ (rcesModoAprobacionNota2 == null ? "NULL" : rcesModoAprobacionNota2));
		sb.append(tabulador + "rcesComboModoAprobacion1 : "
				+ (rcesComboModoAprobacion1 == null ? "NULL" : rcesComboModoAprobacion1));
		sb.append(tabulador + "rcesComboModoAprobacion2 : "
				+ (rcesComboModoAprobacion2 == null ? "NULL" : rcesComboModoAprobacion2));

		sb.append("Detalle Matricula");
		sb.append(tabulador + "dtmtId : " + dtmtId);
		sb.append(tabulador + "dtmtNumero : " + dtmtNumero);

		sb.append("Comprobante Pago");
		sb.append(tabulador + "cmpaId : " + cmpaId);

		sb.append("EstaSeleccionado");
		sb.append(tabulador + "isSeleccionado : " + isSeleccionado);

		sb.append("Dependencia");
		sb.append(tabulador + "dpnId : " + dpnId);
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));

		sb.append("Corequisito");
		sb.append(tabulador + "crqId : " + crqId);
		sb.append(tabulador + "crqCodigo: " + (crqCodigo == null ? "NULL" : crqCodigo));
		sb.append(tabulador + "crqDescripcion : " + (crqDescripcion == null ? "NULL" : crqDescripcion));
		sb.append(tabulador + "crqEstado : " + crqEstado);
		sb.append(tabulador + "prrMtrId: " + (prrMtrId == null ? "NULL" : prrMtrId));
		sb.append(tabulador + "prrMtrPrrId : " + (prrMtrPrrId == null ? "NULL" : prrMtrPrrId));

		sb.append("Prerequisito");
		sb.append(tabulador + "prrId : " + prrId);
		sb.append(tabulador + "prrCodigo: " + (prrCodigo == null ? "NULL" : prrCodigo));
		sb.append(tabulador + "prrDescripcion : " + (prrDescripcion == null ? "NULL" : prrDescripcion));
		sb.append(tabulador + "prrEstado : " + prrEstado);
		sb.append(tabulador + "crqMtrId: " + (crqMtrId == null ? "NULL" : crqMtrId));
		sb.append(tabulador + "crqMtrCrqId : " + (crqMtrCorequisitoId == null ? "NULL" : crqMtrCorequisitoId));

		sb.append("Lista Paralelos:");
		if (listParalelos != null && listParalelos.size() > 0) {
			for (ParaleloDto paraleloDto : listParalelos) {
				sb.append(tabulador + "paraleloDto : " + paraleloDto + "\n");
			}
		}

		sb.append("MallaCurricular");
		sb.append(tabulador + "mlcrId : " + mlcrId);
		sb.append(tabulador + "mlcrCrrId : " + mlcrCrrId);
		sb.append(tabulador + "mlcrCodigo: " + (mlcrCodigo == null ? "NULL" : mlcrCodigo));
		sb.append(tabulador + "mlcrDescripcion : " + (mlcrDescripcion == null ? "NULL" : mlcrDescripcion));
		sb.append(tabulador + "mlcrEstado : " + mlcrEstado);
		sb.append(tabulador + "mlcrVigencia : " + mlcrVigencia);
		sb.append(tabulador + "mlcrTipoAprobacion : " + mlcrTipoAprobacion);

		sb.append("EstaHomologado");
		sb.append(tabulador + "esHomologado : " + esHomologado);

		sb.append("sinNotas");
		sb.append(tabulador + "sinNotas : " + sinNotas);

		sb.append("Causal");
		sb.append(tabulador + "cslId : " + cslId);
		sb.append(tabulador + "cslDescripcion : " + (cslDescripcion == null ? "NULL" : cslDescripcion));
		sb.append(tabulador + "cslCodigo : " + (cslCodigo == null ? "NULL" : cslCodigo));
		sb.append(tabulador + "cslEstado : " + cslEstado);

		sb.append("Tipo Causal");
		sb.append(tabulador + "ticsId : " + ticsId);
		sb.append(tabulador + "ticsDescripcion : " + (ticsDescripcion == null ? "NULL" : ticsDescripcion));
		sb.append(tabulador + "ticsEstado : " + ticsEstado);

		sb.append(" Solicitud Tercera Matricula ");
		sb.append(tabulador + "sltrmtId : " + sltrmtId);
		sb.append(tabulador + "sltrmtEstado : " + (sltrmtEstado == null ? "NULL" : sltrmtEstado));
		sb.append(
				tabulador + "sltrmtFechaSolicitud : " + (sltrmtFechaSolicitud == null ? "NULL" : sltrmtFechaSolicitud));
		sb.append(tabulador + "sltrmtDocumentoSolicitud : "
				+ (sltrmtDocumentoSolicitud == null ? "NULL" : sltrmtDocumentoSolicitud));
		sb.append(tabulador + "sltrmtFechaResolucion : "
				+ (sltrmtFechaResolucion == null ? "NULL" : sltrmtFechaResolucion));
		sb.append(tabulador + "sltrmtDocumentoResolucion : "
				+ (sltrmtDocumentoResolucion == null ? "NULL" : sltrmtDocumentoResolucion));
		sb.append(tabulador + "sltrmtObservacion : " + (sltrmtObservacion == null ? "NULL" : sltrmtObservacion));

		sb.append(" Segundo registro posgrado homolog ");
		sb.append(tabulador + "notaSumaReg2 : " + (notaSumaReg2 == null ? "NULL" : notaSumaReg2));
		sb.append(tabulador + "clfPromedioAsistenciaReg2 : "
				+ (clfPromedioAsistenciaReg2 == null ? "NULL" : clfPromedioAsistenciaReg2));
		sb.append(tabulador + "estadoHomologacionReg2 : "
				+ (estadoHomologacionReg2 == null ? "NULL" : estadoHomologacionReg2));
		sb.append(tabulador + "numMatriculaReg2 : " + numMatriculaReg2);
		sb.append(tabulador + "esHomologadoReg2 : " + (esHomologadoReg2 == null ? "NULL" : esHomologadoReg2));
		sb.append(tabulador + "aprobadoReg2 : " + aprobadoReg2);

		return sb.toString();

	}

}
