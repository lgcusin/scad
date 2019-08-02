package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 02/03/2017 8:53:22 by Hibernate Tools 3.5.0.Final

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "FICHA_INSCRIPCION", schema = ConstantesJpa.SCHEMA)
public class FichaInscripcion implements java.io.Serializable {

	private static final long serialVersionUID = -6171015051802154021L;
	private int fcinId;
	private UsuarioRol fcinUsuarioRol;
	private ConfiguracionCarrera fcinConfiguracionCarrera;
	private PeriodoAcademico fcinPeriodoAcademico;
	private Timestamp fcinFechaInscripcion;
	private String fcinObservacion;
	private String fcinObservacionIngreso;
	private Integer fcinTipo;
	private Integer fcinMatriculado;
	private Integer fcinEstado;
	private Integer fcinEstadoRetiro;
	private Integer fcinTipoModalidad;
	private Integer fcinNumMaximoCreditos;
	private Integer fcinTipoIngreso;   //Reingreso-CambioCrr-CambioUniv-Reinicio
	private Integer fcinEstadoIngreso;  //Homologa o no homologa
	private Integer fcinNivelIngreso;
	private Integer fcinEncuesta;
	private Float fcinNotaEnes;
	private Integer fcinCarrera;
	private Integer fcinCarreraSiiu;
	private Integer fcinCncrArea;
	private Integer fcinFcinNivelacion;
	private String fcinDocumentoIngreso;
	private Integer fcinAnioAbandonaCarrera;
	private Integer fcinCrrAnteriorId;
	private Integer fcinCncrIdNivelacion;
	private Integer fcinFcinAnteriorId;
	private Float fcinNotaUbicacion; //Nota de prueba de ubicacion suficiencia idiomas
	private Integer fcinVigente;
	private Integer fcinReinicioOrigen;
	private Integer fcinAplicaNotaEnes;
	private Integer fcinNotaCorteId;
	
	private List<FichaEstudiante> fcdcFichaEstudiantes= new ArrayList<FichaEstudiante>(0);

	public FichaInscripcion() {
	}

	public FichaInscripcion(int fcinId) {
		this.fcinId = fcinId;
	}

	public FichaInscripcion(int fcinId, UsuarioRol fcinUsuarioRol, ConfiguracionCarrera fcinConfiguracionCarrera,
			PeriodoAcademico fcinPeriodoAcademico, Timestamp fcinFechaInscripcion, String fcinObservacion,String fcinObservacionIngreso,
			Integer fcinTipo, Integer fcinMatriculado, Integer fcinEstado, Integer fcinTipoIngreso,Integer fcinEstadoIngreso, Integer fcinNivelIngreso,
			String fcinDocumentoIngreso, Integer fcinEncuesta, Float fcinNotaEnes, Integer fcinAnioAbandonaCarrera,
			Integer fcinCarrera, Integer fcinCarreraSiiu, Integer fcinCrrAnteriorId, Integer fcinFcinAnteriorId,Float fcinNotaUbicacion,Integer fcinVigente, 
			Integer fcinAplicaNotaEnes, Integer fcinNotaCorteId,List<FichaEstudiante> fcdcFichaEstudiantes) {
		this.fcinId = fcinId;
		this.fcinUsuarioRol = fcinUsuarioRol;
		this.fcinConfiguracionCarrera = fcinConfiguracionCarrera;
		this.fcinPeriodoAcademico = fcinPeriodoAcademico;
		this.fcinFechaInscripcion = fcinFechaInscripcion;
		this.fcinObservacion = fcinObservacion;
		this.fcinObservacionIngreso = fcinObservacionIngreso;
		this.fcinTipo = fcinTipo;
		this.fcinTipoIngreso = fcinTipoIngreso;
		this.fcinEstadoIngreso = fcinEstadoIngreso;
		this.fcinDocumentoIngreso = fcinDocumentoIngreso;
		this.fcinAnioAbandonaCarrera = fcinAnioAbandonaCarrera;
		this.fcinMatriculado = fcinMatriculado;
		this.fcinEstado = fcinEstado;
		this.fcinNivelIngreso = fcinNivelIngreso;
		this.fcinEncuesta = fcinEncuesta;
		this.fcinNotaEnes = fcinNotaEnes;
		this.fcinCarrera = fcinCarrera;
		this.fcinCarreraSiiu = fcinCarreraSiiu;
		this.fcdcFichaEstudiantes = fcdcFichaEstudiantes;
		this.fcinCrrAnteriorId= fcinCrrAnteriorId;
		this.fcinFcinAnteriorId= fcinFcinAnteriorId;
		this.fcinNotaUbicacion= fcinNotaUbicacion;
		this.fcinVigente = fcinVigente;
		this.fcinAplicaNotaEnes = fcinAplicaNotaEnes;
		this.fcinNotaCorteId = fcinNotaCorteId;
	}

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Basic(optional = false)
	@Column(name = "FCIN_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getFcinId() {
		return this.fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	@ManyToOne
	@JoinColumn(name = "CNCR_ID")
	public ConfiguracionCarrera getFcinConfiguracionCarrera() {
		return this.fcinConfiguracionCarrera;
	}

	public void setFcinConfiguracionCarrera(ConfiguracionCarrera fcinConfiguracionCarrera) {
		this.fcinConfiguracionCarrera = fcinConfiguracionCarrera;
	}
	
	@ManyToOne
	@JoinColumn(name = "USRO_ID")
	public UsuarioRol getFcinUsuarioRol() {
		return this.fcinUsuarioRol;
	}

	public void setFcinUsuarioRol(UsuarioRol fcinUsuarioRol) {
		this.fcinUsuarioRol = fcinUsuarioRol;
	}
	
	@ManyToOne
	@JoinColumn(name = "PRAC_ID")
	public PeriodoAcademico getFcinPeriodoAcademico() {
		return this.fcinPeriodoAcademico;
	}

	public void setFcinPeriodoAcademico(PeriodoAcademico fcinPeriodoAcademico) {
		this.fcinPeriodoAcademico = fcinPeriodoAcademico;
	}

	@Column(name = "FCIN_FECHA_INSCRIPCION")
	public Timestamp getFcinFechaInscripcion() {
		return this.fcinFechaInscripcion;
	}

	public void setFcinFechaInscripcion(Timestamp fcinFechaInscripcion) {
		this.fcinFechaInscripcion = fcinFechaInscripcion;
	}
	
	@Column(name = "FCIN_OBSERVACION", length = 256)
	@Length(max = 256)
	public String getFcinObservacion() {
		return this.fcinObservacion;
	}

	public void setFcinObservacion(String fcinObservacion) {
		this.fcinObservacion = fcinObservacion;
	}
	
	
	@Column(name = "FCIN_DOCUMENTO_INGRESO", length = 256)
	@Length(max = 256)
	public String getFcinDocumentoIngreso() {
		return this.fcinDocumentoIngreso;
	}

	public void setFcinDocumentoIngreso(String fcinDocumentoIngreso) {
		this.fcinDocumentoIngreso = fcinDocumentoIngreso;
	}
	
	@Column(name = "FCIN_OBSERVACION_INGRESO", length = 256)
	@Length(max = 256)
	public String getFcinObservacionIngreso() {
		return this.fcinObservacionIngreso;
	}

	public void setFcinObservacionIngreso(String fcinObservacionIngreso) {
		this.fcinObservacionIngreso = fcinObservacionIngreso;
	}
	
	@Column(name = "FCIN_TIPO", precision = 22, scale = 0)
	public Integer getFcinTipo() {
		return this.fcinTipo;
	}

	public void setFcinTipo(Integer fcinTipo) {
		this.fcinTipo = fcinTipo;
	}
	
	@Column(name = "FCIN_TIPO_INGRESO", precision = 22, scale = 0)
	public Integer getFcinTipoIngreso() {
		return this.fcinTipoIngreso;
	}

	public void setFcinTipoIngreso(Integer fcinTipoIngreso) {
		this.fcinTipoIngreso = fcinTipoIngreso;
	}
	
	@Column(name = "FCIN_ESTADO_INGRESO", precision = 22, scale = 0)
	public Integer getFcinEstadoIngreso() {
		return this.fcinEstadoIngreso;
	}

	public void setFcinEstadoIngreso(Integer fcinEstadoIngreso) {
		this.fcinEstadoIngreso = fcinEstadoIngreso;
	}
	
	@Column(name = "FCIN_ANIO_ABANDONA_CARRERA", precision = 22, scale = 0)
	public Integer getFcinAnioAbandonaCarrera() {
		return this.fcinAnioAbandonaCarrera;
	}

	public void setFcinAnioAbandonaCarrera(Integer fcinAnioAbandonaCarrera) {
		this.fcinAnioAbandonaCarrera = fcinAnioAbandonaCarrera;
	}
	
	@Column(name = "FCIN_MATRICULADO", precision = 22, scale = 0)
	public Integer getFcinMatriculado() {
		return this.fcinMatriculado;
	}
	
	public void setFcinMatriculado(Integer fcinMatriculado) {
		this.fcinMatriculado = fcinMatriculado;
	}
	
	@Column(name = "FCIN_ESTADO", precision = 22, scale = 0)
	public Integer getFcinEstado() {
		return this.fcinEstado;
	}

	public void setFcinEstado(Integer fcinEstado) {
		this.fcinEstado = fcinEstado;
	}
	
	
	@Column(name = "FCIN_NIVEL_INGRESO", precision = 22, scale = 0)
	public Integer getFcinNivelIngreso() {
		return this.fcinNivelIngreso;
	}

	public void setFcinNivelIngreso(Integer fcinNivelIngreso) {
		this.fcinNivelIngreso = fcinNivelIngreso;
	}
	
	
	@Column(name = "FCIN_NOTA_ENES", precision = 126)
	public Float getFcinNotaEnes() {
		return fcinNotaEnes;
	}

	public void setFcinNotaEnes(Float fcinNotaEnes) {
		this.fcinNotaEnes = fcinNotaEnes;
	}
	
	
	@Column(name = "FCIN_CARRERA", precision = 22, scale = 0)
	public Integer getFcinCarrera() {
		return fcinCarrera;
	}

	public void setFcinCarrera(Integer fcinCarrera) {
		this.fcinCarrera = fcinCarrera;
	}
	
	@OneToMany( mappedBy = "fcesFichaInscripcion")
	public List<FichaEstudiante> getFcdcFichaEstudiantes() {
		return this.fcdcFichaEstudiantes;
	}

	public void setFcdcFichaEstudiantes(List<FichaEstudiante> fcdcFichaEstudiantes) {
		this.fcdcFichaEstudiantes = fcdcFichaEstudiantes;
	}
	@Column(name = "FCIN_ENCUESTA", precision = 22, scale = 0)
	public Integer getFcinEncuesta() {
		return fcinEncuesta;
	}

	public void setFcinEncuesta(Integer fcinEncuesta) {
		this.fcinEncuesta = fcinEncuesta;
	}

	@Column(name = "FCIN_CARRERA_SIIU", precision = 2, scale = 0)
	public Integer getFcinCarreraSiiu() {
		return fcinCarreraSiiu;
	}

	public void setFcinCarreraSiiu(Integer fcinCarreraSiiu) {
		this.fcinCarreraSiiu = fcinCarreraSiiu;
	}

	@Column(name = "FCIN_CNCR_AREA", precision = 3, scale = 0)
	public Integer getFcinCncrArea() {
		return fcinCncrArea;
	}

	public void setFcinCncrArea(Integer fcinCncrArea) {
		this.fcinCncrArea = fcinCncrArea;
	}

	@Column(name = "FCIN_FCIN_NIVELACION", precision = 3, scale = 0)
	public Integer getFcinFcinNivelacion() {
		return fcinFcinNivelacion;
	}

	public void setFcinFcinNivelacion(Integer fcinFcinNivelacion) {
		this.fcinFcinNivelacion = fcinFcinNivelacion;
	}

	@Column(name = "FCIN_ESTADO_RETIRO", precision = 22, scale = 0)
	public Integer getFcinEstadoRetiro() {
		return fcinEstadoRetiro;
	}

	public void setFcinEstadoRetiro(Integer fcinEstadoRetiro) {
		this.fcinEstadoRetiro = fcinEstadoRetiro;
	}

	@Column(name = "FCIN_TIPO_MODALIDAD", precision = 22, scale = 0)
	public Integer getFcinTipoModalidad() {
		return fcinTipoModalidad;
	}

	public void setFcinTipoModalidad(Integer fcinTipoModalidad) {
		this.fcinTipoModalidad = fcinTipoModalidad;
	}

	@Column(name = "FCIN_NUM_MAX_CREDITOS", precision = 22, scale = 0)
	public Integer getFcinNumMaximoCreditos() {
		return fcinNumMaximoCreditos;
	}

	public void setFcinNumMaximoCreditos(Integer fcinNumMaximoCreditos) {
		this.fcinNumMaximoCreditos = fcinNumMaximoCreditos;
	}
	
	@Column(name = "FCIN_FCIN_ANTERIOR_ID", precision = 22, scale = 0)  
	public Integer getFcinFcinAnteriorId() {
		return this.fcinFcinAnteriorId;
	}

	public void setFcinFcinAnteriorId(Integer fcinFcinAnteriorId) {
		this.fcinFcinAnteriorId = fcinFcinAnteriorId;
	}

	@Column(name = "FCIN_CRR_ANTERIOR_ID", precision = 22, scale = 0)
	public Integer getFcinCrrAnteriorId() {
		return this.fcinCrrAnteriorId;
	}

	public void setFcinCrrAnteriorId(Integer fcinCrrAnteriorId) {
		this.fcinCrrAnteriorId = fcinCrrAnteriorId;
	}
	
	@Column(name = "FCIN_CNCR_ID_NIVELACION", precision = 3, scale = 0)
	public Integer getFcinCncrIdNivelacion() {
		return fcinCncrIdNivelacion;
	}

	public void setFcinCncrIdNivelacion(Integer fcinCncrIdNivelacion) {
		this.fcinCncrIdNivelacion = fcinCncrIdNivelacion;
	}
	
	
	@Column(name="FCIN_NOTA_UBICACION", precision=126)
	public Float getFcinNotaUbicacion() {
		return this.fcinNotaUbicacion;
	}

	public void setFcinNotaUbicacion(Float fcinNotaUbicacion) {
		this.fcinNotaUbicacion = fcinNotaUbicacion;
	}

	@Column(name = "FCIN_VIGENTE", precision = 22, scale = 0)
	public Integer getFcinVigente() {
		return this.fcinVigente;
	}

	public void setFcinVigente(Integer fcinVigente) {
		this.fcinVigente = fcinVigente;
	}
	
	@Column(name = "FCIN_REINICIO_ORIGEN")
	public Integer getFcinReinicioOrigen() {
		return fcinReinicioOrigen;
	}

	public void setFcinReinicioOrigen(Integer fcinReinicioOrigen) {
		this.fcinReinicioOrigen = fcinReinicioOrigen;
	}

	
	@Column(name = "FCIN_APLICA_NOTA_ENES", precision = 22, scale = 0)
	public Integer getFcinAplicaNotaEnes() {
		return this.fcinAplicaNotaEnes;
	}

	public void setFcinAplicaNotaEnes(Integer fcinAplicaNotaEnes) {
		this.fcinAplicaNotaEnes = fcinAplicaNotaEnes;
	}
	
	@Column(name = "FCIN_NOTA_CORTE_ID", precision = 22, scale = 0)
	public Integer getFcinNotaCorteId() {
		return this.fcinNotaCorteId;
	}

	public void setFcinNotaCorteId(Integer fcinNotaCorteId) {
		this.fcinNotaCorteId = fcinNotaCorteId;
	}
	
}
