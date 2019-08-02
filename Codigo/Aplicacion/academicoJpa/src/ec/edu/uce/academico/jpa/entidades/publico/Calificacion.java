package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * The persistent class for the CALIFICACION database table.
 * 
 */
@Entity
@Table(name="CALIFICACION", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "Calificacion.findAll", query="SELECT c FROM Calificacion c"),
				@NamedQuery(name = "Calificacion.findPorRecordEstudiante", query = "SELECT c FROM Calificacion c WHERE c.recordEstudiante.rcesId = :recordId")})
public class Calificacion implements Serializable {

	private static final long serialVersionUID = -776703625538679119L;
	private int clfId;
	private Float clfNota1;
	private Float clfNota2;
	private Float clfExamen;
	private Float clfSupletorio;
	private Float clfAsistencia1;
	private Float clfAsistencia2;
	private Float clfTotalAsistencia1;
	private Float clfTotalAsistencia2;
	private Float clfPromedioNotas;
	private Float clfPromedioAsistencia;
	private Float clfAsistenciaTotal;
	private Float clfParamRecuperacion1;
	private Float clfParamRecuperacion2;
	private Float clfNotaFinalSemestre;	
	private Integer clfEstado;
	private Float clfAsistenciaDocente1;
	private Float clfAsistenciaDocente2;
	private Float clfSumaP1P2;
	private Float clfAsistenciaTotalDoc;
	private RecordEstudiante recordEstudiante;
	private List<ProcesoCalificacion> clfProcesoCalificaciones = new ArrayList<ProcesoCalificacion>(0);		
	
	public Calificacion() {
	}
	
	public Calificacion(int clfId, Float clfNota1, Float clfNota2, Float clfExamen, Float clfSupletorio,
			Float clfAsistencia1, Float clfAsistencia2, Float clfTotalAsistencia1, Float clfTotalAsistencia2,
			Float clfPromedioNotas, Float clfPromedioAsistencia, Float clfAsistenciaTotal, Float clfParamRecuperacion1,
			Float clfParamRecuperacion2, Float clfNotaFinalSemestre, Integer clfEstado, Float clfAsistenciaDocente1,
			Float clfAsistenciaDocente2, Float clfSumaP1P2, Float clfAsistenciaTotalDoc,
			List<ProcesoCalificacion> clfProcesoCalificaciones, RecordEstudiante recordEstudiante) {
		this.clfId = clfId;
		this.clfNota1 = clfNota1;
		this.clfNota2 = clfNota2;
		this.clfExamen = clfExamen;
		this.clfSupletorio = clfSupletorio;
		this.clfAsistencia1 = clfAsistencia1;
		this.clfAsistencia2 = clfAsistencia2;
		this.clfTotalAsistencia1 = clfTotalAsistencia1;
		this.clfTotalAsistencia2 = clfTotalAsistencia2;
		this.clfPromedioNotas = clfPromedioNotas;
		this.clfPromedioAsistencia = clfPromedioAsistencia;
		this.clfAsistenciaTotal = clfAsistenciaTotal;
		this.clfParamRecuperacion1 = clfParamRecuperacion1;
		this.clfParamRecuperacion2 = clfParamRecuperacion2;
		this.clfNotaFinalSemestre = clfNotaFinalSemestre;
		this.clfEstado = clfEstado;
		this.clfAsistenciaDocente1 = clfAsistenciaDocente1;
		this.clfAsistenciaDocente2 = clfAsistenciaDocente2;
		this.clfSumaP1P2 = clfSumaP1P2;
		this.clfAsistenciaTotalDoc = clfAsistenciaTotalDoc;
		this.clfProcesoCalificaciones = clfProcesoCalificaciones;
		this.recordEstudiante = recordEstudiante;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="CLF_ID", unique=true, nullable=false, precision=6, scale = 0)
	public int getClfId() {
		return this.clfId;
	}

	public void setClfId(int clfId) {
		this.clfId = clfId;
	}


	@Column(name="CLF_ASISTENCIA1", precision=126)
	public Float getClfAsistencia1() {
		return this.clfAsistencia1;
	}

	public void setClfAsistencia1(Float clfAsistencia1) {
		this.clfAsistencia1 = clfAsistencia1;
	}


	@Column(name="CLF_ASISTENCIA2", precision=126)
	public Float getClfAsistencia2() {
		return this.clfAsistencia2;
	}

	public void setClfAsistencia2(Float clfAsistencia2) {
		this.clfAsistencia2 = clfAsistencia2;
	}


	@Column(name="CLF_EXAMEN", precision=126)
	public Float getClfExamen() {
		return this.clfExamen;
	}

	public void setClfExamen(Float clfExamen) {
		this.clfExamen = clfExamen;
	}


	@Column(name="CLF_NOTA1", precision=126)
	public Float getClfNota1() {
		return this.clfNota1;
	}

	public void setClfNota1(Float clfNota1) {
		this.clfNota1 = clfNota1;
	}


	@Column(name="CLF_NOTA2", precision=126)
	public Float getClfNota2() {
		return this.clfNota2;
	}

	public void setClfNota2(Float clfNota2) {
		this.clfNota2 = clfNota2;
	}


	@Column(name="CLF_PROMEDIO_ASISTENCIA", precision=126)
	public Float getClfPromedioAsistencia() {
		return this.clfPromedioAsistencia;
	}

	public void setClfPromedioAsistencia(Float clfPromedioAsistencia) {
		this.clfPromedioAsistencia = clfPromedioAsistencia;
	}


	@Column(name="CLF_PROMEDIO_NOTAS", precision=126)
	public Float getClfPromedioNotas() {
		return this.clfPromedioNotas;
	}

	public void setClfPromedioNotas(Float clfPromedioNotas) {
		this.clfPromedioNotas = clfPromedioNotas;
	}


	@Column(name="CLF_SUPLETORIO", precision=126)
	public Float getClfSupletorio() {
		return this.clfSupletorio;
	}

	public void setClfSupletorio(Float clfSupletorio) {
		this.clfSupletorio = clfSupletorio;
	}


	@Column(name="CLF_TOTAL_ASISTENCIA1", precision=126)
	public Float getClfTotalAsistencia1() {
		return this.clfTotalAsistencia1;
	}

	public void setClfTotalAsistencia1(Float clfTotalAsistencia1) {
		this.clfTotalAsistencia1 = clfTotalAsistencia1;
	}


	@Column(name="CLF_TOTAL_ASISTENCIA2", precision=126)
	public Float getClfTotalAsistencia2() {
		return this.clfTotalAsistencia2;
	}

	public void setClfTotalAsistencia2(Float clfTotalAsistencia2) {
		this.clfTotalAsistencia2 = clfTotalAsistencia2;
	}

	
	
	
	
	@Column(name="CLF_ASISTENCIA_TOTAL", precision=126)
	public Float getClfAsistenciaTotal() {
		return this.clfAsistenciaTotal;
	}

	public void setClfAsistenciaTotal(Float clfAsistenciaTotal) {
		this.clfAsistenciaTotal = clfAsistenciaTotal;
	}

	
	
	
	@Column(name="CLF_PARAM_RECUPERACION1", precision=126)
	public Float getClfParamRecuperacion1() {
		return this.clfParamRecuperacion1;
	}

	public void setClfParamRecuperacion1(Float clfParamRecuperacion1) {
		this.clfParamRecuperacion1 = clfParamRecuperacion1;
	}

	
	@Column(name="CLF_PARAM_RECUPERACION2", precision=126)
	public Float getClfParamRecuperacion2() {
		return this.clfParamRecuperacion2;
	}
		

	public void setClfParamRecuperacion2(Float clfParamRecuperacion2) {
		this.clfParamRecuperacion2 = clfParamRecuperacion2;
	}

	@Column(name="CLF_NOTA_FINAL_SEMESTRE", precision=126)
	public Float getClfNotaFinalSemestre() {
		return this.clfNotaFinalSemestre;
	}

	public void setClfNotaFinalSemestre(Float clfNotaFinalSemestre) {
		this.clfNotaFinalSemestre = clfNotaFinalSemestre;
	}

	
	@Column(name = "CLF_ESTADO", precision = 22, scale = 0)
	public Integer getClfEstado() {
		return this.clfEstado;
	}

	public void setClfEstado(Integer clfEstado) {
		this.clfEstado = clfEstado;
	}

	
	
	//bi-directional many-to-one association to RecordEstudiante
	@ManyToOne
	@JoinColumn(name="RCES_ID")
	public RecordEstudiante getRecordEstudiante() {
		return this.recordEstudiante;
	}

	public void setRecordEstudiante(RecordEstudiante recordEstudiante) {
		this.recordEstudiante = recordEstudiante;
	}

	//OneToMany  con tabla Proceso_Calificacion es una lista de Procesos 
	
	@OneToMany( mappedBy = "prclCalificacion")
	public List<ProcesoCalificacion> getClfProcesoCalificaciones() {
		return this.clfProcesoCalificaciones;
	}

	public void setClfProcesoCalificaciones(List<ProcesoCalificacion> clfProcesoCalificaciones) {
		this.clfProcesoCalificaciones = clfProcesoCalificaciones;
	}

	@Column(name="CLF_ASISTENCIA_DOCENTE1", precision=126)
	public Float getClfAsistenciaDocente1() {
		return clfAsistenciaDocente1;
	}

	public void setClfAsistenciaDocente1(Float clfAsistenciaDocente1) {
		this.clfAsistenciaDocente1 = clfAsistenciaDocente1;
	}

	@Column(name="CLF_ASISTENCIA_DOCENTE2", precision=126)
	public Float getClfAsistenciaDocente2() {
		return clfAsistenciaDocente2;
	}

	public void setClfAsistenciaDocente2(Float clfAsistenciaDocente2) {
		this.clfAsistenciaDocente2 = clfAsistenciaDocente2;
	}

	@Column(name="CLF_SUMA_P1_P2", precision=126)
	public Float getClfSumaP1P2() {
		return clfSumaP1P2;
	}

	public void setClfSumaP1P2(Float clfSumaP1P2) {
		this.clfSumaP1P2 = clfSumaP1P2;
	}


	@Column(name="CLF_ASISTENCIA_TOTAL_DOC", precision=126)
	public Float getClfAsistenciaTotalDoc() {
		return clfAsistenciaTotalDoc;
	}



	public void setClfAsistenciaTotalDoc(Float clfAsistenciaTotalDoc) {
		this.clfAsistenciaTotalDoc = clfAsistenciaTotalDoc;
	}
	
	
	
	
	
}