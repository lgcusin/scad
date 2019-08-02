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
   
 ARCHIVO:     RecordAcademicoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad RecordAcademico. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-03-2017 			David Arellano   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Clase (DTO) RecordAcademicoDto. DTO encargado de manejar los datos de la
 * entidad RecordAcademico.
 * 
 * @author dwarellano.
 * @version 1.0
 */
public class RecordEstudianteDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	// NUMERAL PARA REPORTES
	private int numeral;
	// RECORD_ESTUDIANTE
	private int rcesId;
	private int fcesId;
	private String rcesPracDescripcion;
	private Integer rcesMallaCurricularParalelo;
	private Integer rcesEstado;
	private String rcesEstadoLabel;
	private String rcesObservacion;
	private Integer rcesFolio;
	private Integer rcesIndice;
	private BigDecimal rcesPromedioGeneral;
	private Integer rcesPracId;
	private Integer rcesOrigen;
	private Date rcesFechaPrueba;

	private PeriodoAcademicoDto rcesPeriodoAcademicoDto;
	private DependenciaDto rcesDependenciaDto;
	private CarreraDto rcesCarreraDto;
	private ModalidadDto rcesModalidadDto;
	private MateriaDto rcesMateriaDto;
	private NivelDto rcesNivelDto;
	private CalificacionDto rcesCalificacionDto;
	private PersonaDto rcesEstudianteDto;
	private PersonaDto rcesDirectorCarrera;
	private PersonaDto rcesSecretarioAbogado;
	private PersonaDto rcesDocente;
	private FichaMatriculaDto rcesFichaMatriculaDto;
	private MallaCurricularDto rcesMallaCurricularDto;
	private FichaInscripcionDto rcesFichaInscripcionDto;
	private FichaEstudianteDto rcesFichaEstudianteDto;
	private ParaleloDto rcesParaleloDto;
	private HoraClaseDto rcesHoraClaseDto;
	private MallaCurricularParaleloDto rcesMallaCurricularParaleloDto;
	private ComprobantePagoDto rcesComprobantePagoDto;
	private DetalleMatriculaDto rcesDetalleMatriculaDto;

	// PERSONA
	private int prsId;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsNombreCompleto;
	private String prsMailInstitucional;
	// DEPENDENCIA_ESTUDIANTE
	private int dpnId;
	private String dpnDescipcion;
	// CARRERA_ESTUDIANTE
	private int crrId;
	private String crrDescripcion;
	// CALIFICACION
	private int clfId;
	private BigDecimal clfNota1;
	private Integer clfAsistencia1;
	private Integer clfAsistenciaTotal1;
	private BigDecimal clfNota2;
	private Integer clfAsistencia2;
	private Integer clfAsistenciaTotal2;
	private BigDecimal clfParamRecuperacion1;
	private BigDecimal clfParamRecuperacion2;
	private BigDecimal clfNotaRecuperacion;
	private BigDecimal clfNotaFinal;
	private Integer clfAsistenciaFinal;
	private Integer clfEstado;
	private String clfEstadoLabel;
	// Malla Curricular Paralelo
	private Integer mlcrprMallaCurricularParalelo;
	// Paralelo
	private int prlId;
	private Integer prlCarreraId;
	private Integer prlPeriodoAcademicoId;
	private String prlPeriodoAcademicoLabel;
	// Malla Curricular Materia
	private Integer mlcrmtMtrId;
	private Integer mlcrmtMallaCurricularMateria;
	// Tabla Materia
	private int mtrId;
	private String mtrCodigo;
	private String mtrDescripcion;
	private Integer mtrHoras;
	// NIVEL
	private int nvlId;
	private String nvlDescripcion;
	// numero de matricula
	private Integer dtmtNumMatricula;

	private List<RecordEstudianteDto> rcesListRecordEstudianteDto;

	public RecordEstudianteDto() {
	}

	public int getRcesId() {
		return rcesId;
	}

	public String getPrlPeriodoAcademicoLabel() {
		return prlPeriodoAcademicoLabel;
	}

	public void setPrlPeriodoAcademicoLabel(String prlPeriodoAcademicoLabel) {
		this.prlPeriodoAcademicoLabel = prlPeriodoAcademicoLabel;
	}

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public String getDpnDescipcion() {
		return dpnDescipcion;
	}

	public void setDpnDescipcion(String dpnDescipcion) {
		this.dpnDescipcion = dpnDescipcion;
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

	public int getClfId() {
		return clfId;
	}

	public void setClfId(int clfId) {
		this.clfId = clfId;
	}

	public Integer getMlcrprMallaCurricularParalelo() {
		return mlcrprMallaCurricularParalelo;
	}

	public void setMlcrprMallaCurricularParalelo(Integer mlcrprMallaCurricularParalelo) {
		this.mlcrprMallaCurricularParalelo = mlcrprMallaCurricularParalelo;
	}

	public int getPrlId() {
		return prlId;
	}

	public void setPrlId(int prlId) {
		this.prlId = prlId;
	}

	public Integer getPrlCarreraId() {
		return prlCarreraId;
	}

	public void setPrlCarreraId(Integer prlCarreraId) {
		this.prlCarreraId = prlCarreraId;
	}

	public Integer getPrlPeriodoAcademicoId() {
		return prlPeriodoAcademicoId;
	}

	public void setPrlPeriodoAcademicoId(Integer prlPeriodoAcademicoId) {
		this.prlPeriodoAcademicoId = prlPeriodoAcademicoId;
	}

	public Integer getRcesFolio() {
		return rcesFolio;
	}

	public void setRcesFolio(Integer rcesFolio) {
		this.rcesFolio = rcesFolio;
	}

	public Integer getRcesIndice() {
		return rcesIndice;
	}

	public void setRcesIndice(Integer rcesIndice) {
		this.rcesIndice = rcesIndice;
	}

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public String getClfEstadoLabel() {
		return clfEstadoLabel;
	}

	public void setClfEstadoLabel(String clfEstadoLabel) {
		this.clfEstadoLabel = clfEstadoLabel;
	}

	public String getPrsNombreCompleto() {
		return prsNombreCompleto;
	}

	public void setPrsNombreCompleto(String prsNombreCompleto) {
		this.prsNombreCompleto = prsNombreCompleto;
	}

	public Integer getClfEstado() {
		return clfEstado;
	}

	public void setClfEstado(Integer clfEstado) {
		this.clfEstado = clfEstado;
	}

	public BigDecimal getClfNota1() {
		return clfNota1;
	}

	public void setClfNota1(BigDecimal clfNota1) {
		this.clfNota1 = clfNota1;
	}

	public Integer getClfAsistencia1() {
		return clfAsistencia1;
	}

	public void setClfAsistencia1(Integer clfAsistencia1) {
		this.clfAsistencia1 = clfAsistencia1;
	}

	public Integer getClfAsistenciaTotal1() {
		return clfAsistenciaTotal1;
	}

	public void setClfAsistenciaTotal1(Integer clfAsistenciaTotal1) {
		this.clfAsistenciaTotal1 = clfAsistenciaTotal1;
	}

	public BigDecimal getClfNota2() {
		return clfNota2;
	}

	public void setClfNota2(BigDecimal clfNota2) {
		this.clfNota2 = clfNota2;
	}

	public Integer getClfAsistencia2() {
		return clfAsistencia2;
	}

	public void setClfAsistencia2(Integer clfAsistencia2) {
		this.clfAsistencia2 = clfAsistencia2;
	}

	public Integer getClfAsistenciaTotal2() {
		return clfAsistenciaTotal2;
	}

	public void setClfAsistenciaTotal2(Integer clfAsistenciaTotal2) {
		this.clfAsistenciaTotal2 = clfAsistenciaTotal2;
	}

	public BigDecimal getClfNotaRecuperacion() {
		return clfNotaRecuperacion;
	}

	public void setClfNotaRecuperacion(BigDecimal clfNotaRecuperacion) {
		this.clfNotaRecuperacion = clfNotaRecuperacion;
	}

	public BigDecimal getClfNotaFinal() {
		return clfNotaFinal;
	}

	public void setClfNotaFinal(BigDecimal clfNotaFinal) {
		this.clfNotaFinal = clfNotaFinal;
	}

	public Integer getClfAsistenciaFinal() {
		return clfAsistenciaFinal;
	}

	public void setClfAsistenciaFinal(Integer clfAsistenciaFinal) {
		this.clfAsistenciaFinal = clfAsistenciaFinal;
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

	public int getNumeral() {
		return numeral;
	}

	public void setNumeral(int numeral) {
		this.numeral = numeral;
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

	public void setRcesId(int rcesId) {
		this.rcesId = rcesId;
	}

	public Integer getRcesMallaCurricularParalelo() {
		return rcesMallaCurricularParalelo;
	}

	public void setRcesMallaCurricularParalelo(Integer rcesMallaCurricularParalelo) {
		this.rcesMallaCurricularParalelo = rcesMallaCurricularParalelo;
	}

	public Integer getRcesEstado() {
		return rcesEstado;
	}

	public void setRcesEstado(Integer rcesEstado) {
		this.rcesEstado = rcesEstado;
	}

	public String getRcesObservacion() {
		return rcesObservacion;
	}

	public void setRcesObservacion(String rcesObservacion) {
		this.rcesObservacion = rcesObservacion;
	}

	public int getFcesId() {
		return fcesId;
	}

	public void setFcesId(int fcesId) {
		this.fcesId = fcesId;
	}

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

	public Integer getMlcrmtMallaCurricularMateria() {
		return mlcrmtMallaCurricularMateria;
	}

	public void setMlcrmtMallaCurricularMateria(Integer mlcrmtMallaCurricularMateria) {
		this.mlcrmtMallaCurricularMateria = mlcrmtMallaCurricularMateria;
	}

	public Integer getDtmtNumMatricula() {
		return dtmtNumMatricula;
	}

	public void setDtmtNumMatricula(Integer dtmtNumMatricula) {
		this.dtmtNumMatricula = dtmtNumMatricula;
	}

	public FichaInscripcionDto getRcesFichaInscripcionDto() {
		return rcesFichaInscripcionDto;
	}

	public void setRcesFichaInscripcionDto(FichaInscripcionDto rcesFichaInscripcionDto) {
		this.rcesFichaInscripcionDto = rcesFichaInscripcionDto;
	}

	public FichaEstudianteDto getRcesFichaEstudianteDto() {
		return rcesFichaEstudianteDto;
	}

	public void setRcesFichaEstudianteDto(FichaEstudianteDto rcesFichaEstudianteDto) {
		this.rcesFichaEstudianteDto = rcesFichaEstudianteDto;
	}

	public CarreraDto getRcesCarreraDto() {
		return rcesCarreraDto;
	}

	public void setRcesCarreraDto(CarreraDto rcesCarreraDto) {
		this.rcesCarreraDto = rcesCarreraDto;
	}

	public PersonaDto getRcesEstudianteDto() {
		return rcesEstudianteDto;
	}

	public void setRcesEstudianteDto(PersonaDto rcesEstudianteDto) {
		this.rcesEstudianteDto = rcesEstudianteDto;
	}

	public String getRcesEstadoLabel() {
		return rcesEstadoLabel;
	}

	public void setRcesEstadoLabel(String rcesEstadoLabel) {
		this.rcesEstadoLabel = rcesEstadoLabel;
	}

	public PeriodoAcademicoDto getRcesPeriodoAcademicoDto() {
		return rcesPeriodoAcademicoDto;
	}

	public void setRcesPeriodoAcademicoDto(PeriodoAcademicoDto rcesPeriodoAcademicoDto) {
		this.rcesPeriodoAcademicoDto = rcesPeriodoAcademicoDto;
	}

	public DependenciaDto getRcesDependenciaDto() {
		return rcesDependenciaDto;
	}

	public void setRcesDependenciaDto(DependenciaDto rcesDependenciaDto) {
		this.rcesDependenciaDto = rcesDependenciaDto;
	}

	public ModalidadDto getRcesModalidadDto() {
		return rcesModalidadDto;
	}

	public void setRcesModalidadDto(ModalidadDto rcesModalidadDto) {
		this.rcesModalidadDto = rcesModalidadDto;
	}

	public MateriaDto getRcesMateriaDto() {
		return rcesMateriaDto;
	}

	public void setRcesMateriaDto(MateriaDto rcesMateriaDto) {
		this.rcesMateriaDto = rcesMateriaDto;
	}

	public NivelDto getRcesNivelDto() {
		return rcesNivelDto;
	}

	public void setRcesNivelDto(NivelDto rcesNivelDto) {
		this.rcesNivelDto = rcesNivelDto;
	}

	public PersonaDto getRcesDirectorCarrera() {
		return rcesDirectorCarrera;
	}

	public void setRcesDirectorCarrera(PersonaDto rcesDirectorCarrera) {
		this.rcesDirectorCarrera = rcesDirectorCarrera;
	}

	public PersonaDto getRcesSecretarioAbogado() {
		return rcesSecretarioAbogado;
	}

	public void setRcesSecretarioAbogado(PersonaDto rcesSecretarioAbogado) {
		this.rcesSecretarioAbogado = rcesSecretarioAbogado;
	}

	public FichaMatriculaDto getRcesFichaMatriculaDto() {
		return rcesFichaMatriculaDto;
	}

	public void setRcesFichaMatriculaDto(FichaMatriculaDto rcesFichaMatriculaDto) {
		this.rcesFichaMatriculaDto = rcesFichaMatriculaDto;
	}

	public MallaCurricularDto getRcesMallaCurricularDto() {
		return rcesMallaCurricularDto;
	}

	public void setRcesMallaCurricularDto(MallaCurricularDto rcesMallaCurricularDto) {
		this.rcesMallaCurricularDto = rcesMallaCurricularDto;
	}

	public CalificacionDto getRcesCalificacionDto() {
		return rcesCalificacionDto;
	}

	public void setRcesCalificacionDto(CalificacionDto rcesCalificacionDto) {
		this.rcesCalificacionDto = rcesCalificacionDto;
	}

	public List<RecordEstudianteDto> getRcesListRecordEstudianteDto() {
		return rcesListRecordEstudianteDto;
	}

	public void setRcesListRecordEstudianteDto(List<RecordEstudianteDto> rcesListRecordEstudianteDto) {
		this.rcesListRecordEstudianteDto = rcesListRecordEstudianteDto;
	}

	public ParaleloDto getRcesParaleloDto() {
		return rcesParaleloDto;
	}

	public void setRcesParaleloDto(ParaleloDto rcesParaleloDto) {
		this.rcesParaleloDto = rcesParaleloDto;
	}

	public String getRcesPracDescripcion() {
		return rcesPracDescripcion;
	}

	public void setRcesPracDescripcion(String rcesPracDescripcion) {
		this.rcesPracDescripcion = rcesPracDescripcion;
	}

	public BigDecimal getRcesPromedioGeneral() {
		return rcesPromedioGeneral;
	}

	public void setRcesPromedioGeneral(BigDecimal rcesPromedioGeneral) {
		this.rcesPromedioGeneral = rcesPromedioGeneral;
	}

	public Integer getRcesPracId() {
		return rcesPracId;
	}

	public void setRcesPracId(Integer rcesPracId) {
		this.rcesPracId = rcesPracId;
	}

	public Integer getRcesOrigen() {
		return rcesOrigen;
	}

	public void setRcesOrigen(Integer rcesOrigen) {
		this.rcesOrigen = rcesOrigen;
	}

	public Date getRcesFechaPrueba() {
		return rcesFechaPrueba;
	}

	public void setRcesFechaPrueba(Date rcesFechaPrueba) {
		this.rcesFechaPrueba = rcesFechaPrueba;
	}

	public PersonaDto getRcesDocente() {
		return rcesDocente;
	}

	public void setRcesDocente(PersonaDto rcesDocente) {
		this.rcesDocente = rcesDocente;
	}

	public HoraClaseDto getRcesHoraClaseDto() {
		return rcesHoraClaseDto;
	}

	public void setRcesHoraClaseDto(HoraClaseDto rcesHoraClaseDto) {
		this.rcesHoraClaseDto = rcesHoraClaseDto;
	}

	public MallaCurricularParaleloDto getRcesMallaCurricularParaleloDto() {
		return rcesMallaCurricularParaleloDto;
	}

	public void setRcesMallaCurricularParaleloDto(MallaCurricularParaleloDto rcesMallaCurricularParaleloDto) {
		this.rcesMallaCurricularParaleloDto = rcesMallaCurricularParaleloDto;
	}

	public Integer getMlcrmtMtrId() {
		return mlcrmtMtrId;
	}

	public void setMlcrmtMtrId(Integer mlcrmtMtrId) {
		this.mlcrmtMtrId = mlcrmtMtrId;
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

	public ComprobantePagoDto getRcesComprobantePagoDto() {
		return rcesComprobantePagoDto;
	}

	public void setRcesComprobantePagoDto(ComprobantePagoDto rcesComprobantePagoDto) {
		this.rcesComprobantePagoDto = rcesComprobantePagoDto;
	}

	public DetalleMatriculaDto getRcesDetalleMatriculaDto() {
		return rcesDetalleMatriculaDto;
	}

	public void setRcesDetalleMatriculaDto(DetalleMatriculaDto rcesDetalleMatriculaDto) {
		this.rcesDetalleMatriculaDto = rcesDetalleMatriculaDto;
	}

	public boolean equals(Object obj) {
		RecordEstudianteDto other = (RecordEstudianteDto) obj;
		if (clfEstadoLabel == null) {
			if (other.clfEstadoLabel != null)
				return false;
		} else if (!clfEstadoLabel.equals(other.clfEstadoLabel))
			return false;
		return true;
	}

}
