package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * The persistent class for the CALIFICACION_MODULO database table.
 * 
 */
@Entity
@Table(name="CALIFICACION_MODULO", schema = ConstantesJpa.SCHEMA)
public class CalificacionModulo implements Serializable {


	private static final long serialVersionUID = -776703625538679119L;

	private int clmdId;
	private Float clmdNota1;
	private Float clmdNota2;
	private Float clmdExamen;
	private Float clmdSupletorio;
	private Float clmdAsistencia1;
	private Float clmdAsistencia2;
	private Float clmdTotalAsistencia1;
	private Float clmdTotalAsistencia2;
	private Float clmdPromedioNotas;
	private Float clmdPromedioAsistencia;
	private Float clmdAsistenciaTotal;
	private Float clmdParamRecuperacion1;
	private Float clmdParamRecuperacion2;
	private Float clmdNotaFinalSemestre;	
	private Integer clmdEstado;
	private Float clmdAsistenciaDocente1;
	private Float clmdAsistenciaDocente2;
	private Float clmdSumaP1P2;
	private Float clmdAsistenciaTotalDoc;
	private Integer mlcrprIdModulo;
	
	private List<ProcesoCalificacion> clmdProcesoCalificaciones = new ArrayList<ProcesoCalificacion>(0);		
	
	
	private RecordEstudiante recordEstudiante;

	public CalificacionModulo() {
	}

	public CalificacionModulo(int clmdId, Float clmdNota1, Float clmdNota2, Float clmdExamen, Float clmdSupletorio,
			Float clmdAsistencia1, Float clmdAsistencia2, Float clmdTotalAsistencia1, Float clmdTotalAsistencia2,
			Float clmdPromedioNotas, Float clmdPromedioAsistencia, Float clmdAsistenciaTotal,
			Float clmdParamRecuperacion1, Float clmdParamRecuperacion2, Float clmdNotaFinalSemestre, Integer clmdEstado,
			Float clmdAsistenciaDocente1, Float clmdAsistenciaDocente2, Float clmdSumaP1P2,
			Float clmdAsistenciaTotalDoc, List<ProcesoCalificacion> clmdProcesoCalificaciones,
			RecordEstudiante recordEstudiante) {
		this.clmdId = clmdId;
		this.clmdNota1 = clmdNota1;
		this.clmdNota2 = clmdNota2;
		this.clmdExamen = clmdExamen;
		this.clmdSupletorio = clmdSupletorio;
		this.clmdAsistencia1 = clmdAsistencia1;
		this.clmdAsistencia2 = clmdAsistencia2;
		this.clmdTotalAsistencia1 = clmdTotalAsistencia1;
		this.clmdTotalAsistencia2 = clmdTotalAsistencia2;
		this.clmdPromedioNotas = clmdPromedioNotas;
		this.clmdPromedioAsistencia = clmdPromedioAsistencia;
		this.clmdAsistenciaTotal = clmdAsistenciaTotal;
		this.clmdParamRecuperacion1 = clmdParamRecuperacion1;
		this.clmdParamRecuperacion2 = clmdParamRecuperacion2;
		this.clmdNotaFinalSemestre = clmdNotaFinalSemestre;
		this.clmdEstado = clmdEstado;
		this.clmdAsistenciaDocente1 = clmdAsistenciaDocente1;
		this.clmdAsistenciaDocente2 = clmdAsistenciaDocente2;
		this.clmdSumaP1P2 = clmdSumaP1P2;
		this.clmdAsistenciaTotalDoc = clmdAsistenciaTotalDoc;
		this.clmdProcesoCalificaciones = clmdProcesoCalificaciones;
		this.recordEstudiante = recordEstudiante;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="CLMD_ID", unique=true, nullable=false, precision=6, scale = 0)
	public int getClmdId() {
		return clmdId;
	}

	public void setClmdId(int clmdId) {
		this.clmdId = clmdId;
	}

	@Column(name="CLMD_NOTA1", precision=126)
	public Float getClmdNota1() {
		return clmdNota1;
	}

	public void setClmdNota1(Float clmdNota1) {
		this.clmdNota1 = clmdNota1;
	}

	@Column(name="CLMD_NOTA2", precision=126)
	public Float getClmdNota2() {
		return clmdNota2;
	}

	public void setClmdNota2(Float clmdNota2) {
		this.clmdNota2 = clmdNota2;
	}

	@Column(name="CLMD_EXAMEN", precision=126)
	public Float getClmdExamen() {
		return clmdExamen;
	}

	public void setClmdExamen(Float clmdExamen) {
		this.clmdExamen = clmdExamen;
	}

	@Column(name="CLMD_SUPLETORIO", precision=126)
	public Float getClmdSupletorio() {
		return clmdSupletorio;
	}

	public void setClmdSupletorio(Float clmdSupletorio) {
		this.clmdSupletorio = clmdSupletorio;
	}

	@Column(name="CLMD_ASISTENCIA1", precision=126)
	public Float getClmdAsistencia1() {
		return clmdAsistencia1;
	}

	public void setClmdAsistencia1(Float clmdAsistencia1) {
		this.clmdAsistencia1 = clmdAsistencia1;
	}

	@Column(name="CLMD_ASISTENCIA2", precision=126)
	public Float getClmdAsistencia2() {
		return clmdAsistencia2;
	}

	public void setClmdAsistencia2(Float clmdAsistencia2) {
		this.clmdAsistencia2 = clmdAsistencia2;
	}

	@Column(name="CLMD_TOTAL_ASISTENCIA1", precision=126)
	public Float getClmdTotalAsistencia1() {
		return clmdTotalAsistencia1;
	}

	public void setClmdTotalAsistencia1(Float clmdTotalAsistencia1) {
		this.clmdTotalAsistencia1 = clmdTotalAsistencia1;
	}

	@Column(name="CLMD_TOTAL_ASISTENCIA2", precision=126)
	public Float getClmdTotalAsistencia2() {
		return clmdTotalAsistencia2;
	}

	public void setClmdTotalAsistencia2(Float clmdTotalAsistencia2) {
		this.clmdTotalAsistencia2 = clmdTotalAsistencia2;
	}

	@Column(name="CLMD_PROMEDIO_NOTAS", precision=126)
	public Float getClmdPromedioNotas() {
		return clmdPromedioNotas;
	}

	public void setClmdPromedioNotas(Float clmdPromedioNotas) {
		this.clmdPromedioNotas = clmdPromedioNotas;
	}

	@Column(name="CLMD_PROMEDIO_ASISTENCIA", precision=126)
	public Float getClmdPromedioAsistencia() {
		return clmdPromedioAsistencia;
	}

	public void setClmdPromedioAsistencia(Float clmdPromedioAsistencia) {
		this.clmdPromedioAsistencia = clmdPromedioAsistencia;
	}

	@Column(name="CLMD_ASISTENCIA_TOTAL", precision=126)
	public Float getClmdAsistenciaTotal() {
		return clmdAsistenciaTotal;
	}

	public void setClmdAsistenciaTotal(Float clmdAsistenciaTotal) {
		this.clmdAsistenciaTotal = clmdAsistenciaTotal;
	}

	@Column(name="CLMD_PARAM_RECUPERACION1", precision=126)
	public Float getClmdParamRecuperacion1() {
		return clmdParamRecuperacion1;
	}

	public void setClmdParamRecuperacion1(Float clmdParamRecuperacion1) {
		this.clmdParamRecuperacion1 = clmdParamRecuperacion1;
	}

	@Column(name="CLMD_PARAM_RECUPERACION2", precision=126)
	public Float getClmdParamRecuperacion2() {
		return clmdParamRecuperacion2;
	}

	public void setClmdParamRecuperacion2(Float clmdParamRecuperacion2) {
		this.clmdParamRecuperacion2 = clmdParamRecuperacion2;
	}

	@Column(name="CLMD_NOTA_FINAL_SEMESTRE", precision=126)
	public Float getClmdNotaFinalSemestre() {
		return clmdNotaFinalSemestre;
	}

	public void setClmdNotaFinalSemestre(Float clmdNotaFinalSemestre) {
		this.clmdNotaFinalSemestre = clmdNotaFinalSemestre;
	}

	@Column(name = "CLMD_ESTADO", precision = 22, scale = 0)
	public Integer getClmdEstado() {
		return clmdEstado;
	}

	public void setClmdEstado(Integer clmdEstado) {
		this.clmdEstado = clmdEstado;
	}

	@Column(name="CLMD_ASISTENCIA_DOCENTE1", precision=126)
	public Float getClmdAsistenciaDocente1() {
		return clmdAsistenciaDocente1;
	}

	public void setClmdAsistenciaDocente1(Float clmdAsistenciaDocente1) {
		this.clmdAsistenciaDocente1 = clmdAsistenciaDocente1;
	}

	@Column(name="CLMD_ASISTENCIA_DOCENTE2", precision=126)
	public Float getClmdAsistenciaDocente2() {
		return clmdAsistenciaDocente2;
	}

	public void setClmdAsistenciaDocente2(Float clmdAsistenciaDocente2) {
		this.clmdAsistenciaDocente2 = clmdAsistenciaDocente2;
	}

	@Column(name="CLMD_SUMA_P1_P2", precision=126)
	public Float getClmdSumaP1P2() {
		return clmdSumaP1P2;
	}

	public void setClmdSumaP1P2(Float clmdSumaP1P2) {
		this.clmdSumaP1P2 = clmdSumaP1P2;
	}

	@Column(name="CLMD_ASISTENCIA_TOTAL_DOC", precision=126)
	public Float getClmdAsistenciaTotalDoc() {
		return clmdAsistenciaTotalDoc;
	}

	public void setClmdAsistenciaTotalDoc(Float clmdAsistenciaTotalDoc) {
		this.clmdAsistenciaTotalDoc = clmdAsistenciaTotalDoc;
	}

	@ManyToOne
	@JoinColumn(name="RCES_ID")
	public RecordEstudiante getRecordEstudiante() {
		return this.recordEstudiante;
	}

	public void setRecordEstudiante(RecordEstudiante recordEstudiante) {
		this.recordEstudiante = recordEstudiante;
	}

	@OneToMany( mappedBy = "prclCalificacionModulo")
	public List<ProcesoCalificacion> getClmdProcesoCalificaciones() {
		return clmdProcesoCalificaciones;
	}

	public void setClmdProcesoCalificaciones(List<ProcesoCalificacion> clmdProcesoCalificaciones) {
		this.clmdProcesoCalificaciones = clmdProcesoCalificaciones;
	}

	@Column(name = "MLCRPR_ID_MODULO", precision = 22, scale = 0)
	public Integer getMlcrprIdModulo() {
		return mlcrprIdModulo;
	}

	public void setMlcrprIdModulo(Integer mlcrprIdModulo) {
		this.mlcrprIdModulo = mlcrprIdModulo;
	}

	@Override
	public String toString() {
		return "CalificacionModulo [clmdId=" + clmdId + ", clmdNota1=" + clmdNota1 + ", clmdNota2=" + clmdNota2
				+ ", clmdExamen=" + clmdExamen + ", clmdSupletorio=" + clmdSupletorio + ", clmdAsistencia1="
				+ clmdAsistencia1 + ", clmdAsistencia2=" + clmdAsistencia2 + ", clmdTotalAsistencia1="
				+ clmdTotalAsistencia1 + ", clmdTotalAsistencia2=" + clmdTotalAsistencia2 + ", clmdPromedioNotas="
				+ clmdPromedioNotas + ", clmdPromedioAsistencia=" + clmdPromedioAsistencia + ", clmdAsistenciaTotal="
				+ clmdAsistenciaTotal + ", clmdParamRecuperacion1=" + clmdParamRecuperacion1
				+ ", clmdParamRecuperacion2=" + clmdParamRecuperacion2 + ", clmdNotaFinalSemestre="
				+ clmdNotaFinalSemestre + ", clmdEstado=" + clmdEstado + ", clmdAsistenciaDocente1="
				+ clmdAsistenciaDocente1 + ", clmdAsistenciaDocente2=" + clmdAsistenciaDocente2 + ", clmdSumaP1P2="
				+ clmdSumaP1P2 + ", clmdAsistenciaTotalDoc=" + clmdAsistenciaTotalDoc + ", clmdProcesoCalificaciones="
				+ clmdProcesoCalificaciones + ", recordEstudiante=" + recordEstudiante + "]";
	}

}