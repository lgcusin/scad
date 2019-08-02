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
   
 ARCHIVO:     RecordEstudianteSAUDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad RecordEstudianteSAUDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		        AUTOR          					COMENTARIOS
 10-03-2018  			Arturo Villafuerte  	     	          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase (DTO) RecordEstudianteSAUDto. DTO encargado de manejar los datos de la
 * entidad RecordEstudianteSAUDto.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */
public class RecordEstudianteSAUDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	// Record academico
	private int pracGrupo;
	private String pracDescripcion;

	private String escDescipcion;

	private int espeCodigo;
	private String espeDescripcion;

	private String prsNombresApellidos;
	private String prsIdentificacion;

	private int numSemestre;
	private int numMatricula;

	private int mtrEstado;
	private String mtrCodigo;
	private String mtrDescripcion;
	private int mtrCreditos;
	private String mtrEstadoDescripcion;

	private BigDecimal nota1;
	private Integer asistencia1;
	private Integer asistenciaTotal1;

	private BigDecimal nota2;
	private Integer asistencia2;
	private Integer asistenciaTotal2;

	private BigDecimal notaRecuperacion;

	private BigDecimal notaFinal;
	private Integer asistenciaFinal;

	private String prlDescripcion;

	private int rcesFolio;
	private int rcesIndice;

	// Lista simple
	private Integer carreraId;
	private String codigoMateria;
	private Integer estado;
	private Integer numeroMtricula;
	private Integer periodoAcademico;
	private Integer semestre;

	// complementos
	private PeriodoAcademicoDto rcesPeriodoAcademicoDto;
	private DependenciaDto rcesDependenciaDto;
	private ModalidadDto rcesModalidadDto;
	private CarreraDto rcesCarreraDto;
	private MateriaDto rcesMateriaDto;
	private NivelDto rcesNivelDto;
	private PersonaDto rcesEstudiante;
	private PersonaDto rcesDirectorCarrera;
	private PersonaDto rcesSecretarioAbogado;
	private FichaMatriculaDto rcesFichaMatriculaDto;
	private MallaCurricularDto rcesMallaCurricularDto;
	private CalificacionDto rcesCalificacionDto;

	public int getPracGrupo() {
		return pracGrupo;
	}

	public void setPracGrupo(int pracGrupo) {
		this.pracGrupo = pracGrupo;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public String getEscDescipcion() {
		return escDescipcion;
	}

	public void setEscDescipcion(String escDescipcion) {
		this.escDescipcion = escDescipcion;
	}

	public int getEspeCodigo() {
		return espeCodigo;
	}

	public void setEspeCodigo(int espeCodigo) {
		this.espeCodigo = espeCodigo;
	}

	public String getEspeDescripcion() {
		return espeDescripcion;
	}

	public void setEspeDescripcion(String espeDescripcion) {
		this.espeDescripcion = espeDescripcion;
	}

	public String getPrsNombresApellidos() {
		return prsNombresApellidos;
	}

	public void setPrsNombresApellidos(String prsNombresApellidos) {
		this.prsNombresApellidos = prsNombresApellidos;
	}

	public int getNumSemestre() {
		return numSemestre;
	}

	public void setNumSemestre(int numSemestre) {
		this.numSemestre = numSemestre;
	}

	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}

	public int getNumMatricula() {
		return numMatricula;
	}

	public void setNumMatricula(int numMatricula) {
		this.numMatricula = numMatricula;
	}

	public int getMtrEstado() {
		return mtrEstado;
	}

	public void setMtrEstado(int mtrEstado) {
		this.mtrEstado = mtrEstado;
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

	public Integer getAsistenciaTotal1() {
		return asistenciaTotal1;
	}

	public void setAsistenciaTotal1(Integer asistenciaTotal1) {
		this.asistenciaTotal1 = asistenciaTotal1;
	}

	public Integer getAsistenciaTotal2() {
		return asistenciaTotal2;
	}

	public void setAsistenciaTotal2(Integer asistenciaTotal2) {
		this.asistenciaTotal2 = asistenciaTotal2;
	}

	public int getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(int mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
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

	public String getMtrEstadoDescripcion() {
		return mtrEstadoDescripcion;
	}

	public void setMtrEstadoDescripcion(String mtrEstadoDescripcion) {
		this.mtrEstadoDescripcion = mtrEstadoDescripcion;
	}

	public BigDecimal getNota1() {
		return nota1;
	}

	public void setNota1(BigDecimal nota1) {
		this.nota1 = nota1;
	}

	public Integer getAsistencia1() {
		return asistencia1;
	}

	public void setAsistencia1(Integer asistencia1) {
		this.asistencia1 = asistencia1;
	}

	public BigDecimal getNota2() {
		return nota2;
	}

	public void setNota2(BigDecimal nota2) {
		this.nota2 = nota2;
	}

	public Integer getAsistencia2() {
		return asistencia2;
	}

	public void setAsistencia2(Integer asistencia2) {
		this.asistencia2 = asistencia2;
	}

	public BigDecimal getNotaRecuperacion() {
		return notaRecuperacion;
	}

	public void setNotaRecuperacion(BigDecimal notaRecuperacion) {
		this.notaRecuperacion = notaRecuperacion;
	}

	public BigDecimal getNotaFinal() {
		return notaFinal;
	}

	public void setNotaFinal(BigDecimal notaFinal) {
		this.notaFinal = notaFinal;
	}

	public Integer getAsistenciaFinal() {
		return asistenciaFinal;
	}

	public void setAsistenciaFinal(Integer asistenciaFinal) {
		this.asistenciaFinal = asistenciaFinal;
	}

	public Integer getCarreraId() {
		return carreraId;
	}

	public void setCarreraId(Integer carreraId) {
		this.carreraId = carreraId;
	}

	public String getCodigoMateria() {
		return codigoMateria;
	}

	public void setCodigoMateria(String codigoMateria) {
		this.codigoMateria = codigoMateria;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Integer getNumeroMtricula() {
		return numeroMtricula;
	}

	public void setNumeroMtricula(Integer numeroMtricula) {
		this.numeroMtricula = numeroMtricula;
	}

	public Integer getPeriodoAcademico() {
		return periodoAcademico;
	}

	public void setPeriodoAcademico(Integer periodoAcademico) {
		this.periodoAcademico = periodoAcademico;
	}

	public Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}

	public String getPrlDescripcion() {
		return prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
	}

	public DependenciaDto getRcesDependenciaDto() {
		return rcesDependenciaDto;
	}

	public void setRcesDependenciaDto(DependenciaDto rcesDependenciaDto) {
		this.rcesDependenciaDto = rcesDependenciaDto;
	}

	public CarreraDto getRcesCarreraDto() {
		return rcesCarreraDto;
	}

	public void setRcesCarreraDto(CarreraDto rcesCarreraDto) {
		this.rcesCarreraDto = rcesCarreraDto;
	}

	public MateriaDto getRcesMateriaDto() {
		return rcesMateriaDto;
	}

	public void setRcesMateriaDto(MateriaDto rcesMateriaDto) {
		this.rcesMateriaDto = rcesMateriaDto;
	}

	public PeriodoAcademicoDto getRcesPeriodoAcademicoDto() {
		return rcesPeriodoAcademicoDto;
	}

	public void setRcesPeriodoAcademicoDto(PeriodoAcademicoDto rcesPeriodoAcademicoDto) {
		this.rcesPeriodoAcademicoDto = rcesPeriodoAcademicoDto;
	}

	public PersonaDto getRcesEstudiante() {
		return rcesEstudiante;
	}

	public void setRcesEstudiante(PersonaDto rcesEstudiante) {
		this.rcesEstudiante = rcesEstudiante;
	}

	public MallaCurricularDto getRcesMallaCurricularDto() {
		return rcesMallaCurricularDto;
	}

	public void setRcesMallaCurricularDto(MallaCurricularDto rcesMallaCurricularDto) {
		this.rcesMallaCurricularDto = rcesMallaCurricularDto;
	}

	public int getRcesFolio() {
		return rcesFolio;
	}

	public void setRcesFolio(int rcesFolio) {
		this.rcesFolio = rcesFolio;
	}

	public int getRcesIndice() {
		return rcesIndice;
	}

	public void setRcesIndice(int rcesIndice) {
		this.rcesIndice = rcesIndice;
	}

	public ModalidadDto getRcesModalidadDto() {
		return rcesModalidadDto;
	}

	public void setRcesModalidadDto(ModalidadDto rcesModalidadDto) {
		this.rcesModalidadDto = rcesModalidadDto;
	}

	public NivelDto getRcesNivelDto() {
		return rcesNivelDto;
	}

	public void setRcesNivelDto(NivelDto rcesNivelDto) {
		this.rcesNivelDto = rcesNivelDto;
	}

	public CalificacionDto getRcesCalificacionDto() {
		return rcesCalificacionDto;
	}

	public void setRcesCalificacionDto(CalificacionDto rcesCalificacionDto) {
		this.rcesCalificacionDto = rcesCalificacionDto;
	}

	public FichaMatriculaDto getRcesFichaMatriculaDto() {
		return rcesFichaMatriculaDto;
	}

	public void setRcesFichaMatriculaDto(FichaMatriculaDto rcesFichaMatriculaDto) {
		this.rcesFichaMatriculaDto = rcesFichaMatriculaDto;
	}

	@Override
	public boolean equals(Object obj) {
		RecordEstudianteSAUDto other = (RecordEstudianteSAUDto) obj;
		if (mtrEstadoDescripcion == null) {
			if (other.mtrEstadoDescripcion != null)
				return false;
		} else if (!mtrEstadoDescripcion.equals(other.mtrEstadoDescripcion))
			return false;
		return true;
	}

}
