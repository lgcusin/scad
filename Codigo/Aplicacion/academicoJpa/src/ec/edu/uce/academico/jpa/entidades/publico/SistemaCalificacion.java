package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;

import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * The persistent class for the SISTEMA_CALIFICACION database table.
 * 
 */
@Entity
@Table(name="SISTEMA_CALIFICACION", schema = ConstantesJpa.SCHEMA)
public class SistemaCalificacion implements Serializable {
	
	private static final long serialVersionUID = -8184243661029009521L;
	
	private int ssclId;
	private Integer ssclEstado;
	private Float ssclNotaMaxima;
	private Float ssclNotaMinimaAprobacion;
	private Float ssclNotaMinimaSupletorio;
	private Float ssclPorcentajeAprobacion;
	private Integer ssclRedondeo;
	private PeriodoAcademico periodoAcademico;
	private TipoSistemaCalificacion tipoSistemaCalificacion;

	public SistemaCalificacion() {
	}


	public SistemaCalificacion(int ssclId, Integer ssclEstado, Float ssclNotaMaxima, Float ssclNotaMinimaAprobacion,
			Float ssclNotaMinimaSupletorio, Float ssclPorcentajeAprobacion, Integer ssclRedondeo,
			PeriodoAcademico periodoAcademico, TipoSistemaCalificacion tipoSistemaCalificacion) {
		this.ssclId = ssclId;
		this.ssclEstado = ssclEstado;
		this.ssclNotaMaxima = ssclNotaMaxima;
		this.ssclNotaMinimaAprobacion = ssclNotaMinimaAprobacion;
		this.ssclNotaMinimaSupletorio = ssclNotaMinimaSupletorio;
		this.ssclPorcentajeAprobacion = ssclPorcentajeAprobacion;
		this.ssclRedondeo = ssclRedondeo;
		this.periodoAcademico = periodoAcademico;
		this.tipoSistemaCalificacion = tipoSistemaCalificacion;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="SSCL_ID", unique=true, nullable=false, precision=6,scale = 0)
	public int getSsclId() {
		return this.ssclId;
	}

	public void setSsclId(int ssclId) {
		this.ssclId = ssclId;
	}


	@Column(name="SSCL_ESTADO", precision=38)
	public Integer getSsclEstado() {
		return this.ssclEstado;
	}

	public void setSsclEstado(Integer ssclEstado) {
		this.ssclEstado = ssclEstado;
	}


	@Column(name="SSCL_NOTA_MAXIMA", precision=126)
	public Float getSsclNotaMaxima() {
		return this.ssclNotaMaxima;
	}

	public void setSsclNotaMaxima(Float ssclNotaMaxima) {
		this.ssclNotaMaxima = ssclNotaMaxima;
	}


	@Column(name="SSCL_NOTA_MINIMA_APROBACION", precision=126)
	public Float getSsclNotaMinimaAprobacion() {
		return this.ssclNotaMinimaAprobacion;
	}

	public void setSsclNotaMinimaAprobacion(Float ssclNotaMinimaAprobacion) {
		this.ssclNotaMinimaAprobacion = ssclNotaMinimaAprobacion;
	}


	@Column(name="SSCL_NOTA_MINIMA_SUPLETORIO", precision=126)
	public Float getSsclNotaMinimaSupletorio() {
		return this.ssclNotaMinimaSupletorio;
	}

	public void setSsclNotaMinimaSupletorio(Float ssclNotaMinimaSupletorio) {
		this.ssclNotaMinimaSupletorio = ssclNotaMinimaSupletorio;
	}


	@Column(name="SSCL_PORCENTAJE_APROBACION", precision=126)
	public Float getSsclPorcentajeAprobacion() {
		return this.ssclPorcentajeAprobacion;
	}

	public void setSsclPorcentajeAprobacion(Float ssclPorcentajeAprobacion) {
		this.ssclPorcentajeAprobacion = ssclPorcentajeAprobacion;
	}


	@Column(name="SSCL_REDONDEO", precision=38)
	public Integer getSsclRedondeo() {
		return this.ssclRedondeo;
	}

	public void setSsclRedondeo(Integer ssclRedondeo) {
		this.ssclRedondeo = ssclRedondeo;
	}


	//bi-directional many-to-one association to PeriodoAcademico
	@ManyToOne
	@JoinColumn(name="PRAC_ID")
	public PeriodoAcademico getPeriodoAcademico() {
		return this.periodoAcademico;
	}

	public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
		this.periodoAcademico = periodoAcademico;
	}


	//bi-directional many-to-one association to TipoSistemaCalificacion
	@ManyToOne
	@JoinColumn(name="TISSCL_ID")
	public TipoSistemaCalificacion getTipoSistemaCalificacion() {
		return this.tipoSistemaCalificacion;
	}

	public void setTipoSistemaCalificacion(TipoSistemaCalificacion tipoSistemaCalificacion) {
		this.tipoSistemaCalificacion = tipoSistemaCalificacion;
	}

}