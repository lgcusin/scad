package ec.edu.uce.biometrico.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CRONOGRAMA")
public class Cronograma implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer crnId;
	private PeriodoAcademico crnPeriodoAcademico;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;
	private List<CronogramaProcesoFlujo> cronogramaProcesoFlujos= new ArrayList<CronogramaProcesoFlujo>(0);

	public Cronograma() {
		super();
	}

	public Cronograma(Integer crnId) {
		super();
		this.crnId = crnId;
	}

	public Cronograma(Integer crnId, PeriodoAcademico peridoAcademico) {
		super();
		this.crnId = crnId;
		this.crnPeriodoAcademico = peridoAcademico;
	}

	public Cronograma(Integer crnId, PeriodoAcademico periodoAcademico, Integer crnTipo, String crnDescripcion,
			Integer crnEstado,List<CronogramaProcesoFlujo> cronogramaProcesoFlujos) {
		super();
		this.crnId = crnId;
		this.crnPeriodoAcademico = periodoAcademico;
		this.crnTipo = crnTipo;
		this.crnDescripcion = crnDescripcion;
		this.crnEstado = crnEstado;
		this.cronogramaProcesoFlujos= cronogramaProcesoFlujos;
	}

	/**
	 * @return the crnId
	 */
	@Id

	@Column(name = "CRN_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public Integer getCrnId() {
		return crnId;
	}

	/**
	 * @param crnId
	 *            the crnId to set
	 */
	public void setCrnId(Integer crnId) {
		this.crnId = crnId;
	}

	/**
	 * @return the crnTipo
	 */
	@Column(name = "CRN_TIPO", precision = 6, scale = 0)
	public Integer getCrnTipo() {
		return crnTipo;
	}

	/**
	 * @return the periodoAcademico
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRAC_ID")
	public PeriodoAcademico getCrnPeriodoAcademico() {
		return crnPeriodoAcademico;
	}

	/**
	 * @param periodoAcademico
	 *            the periodoAcademico to set
	 */
	public void setCrnPeriodoAcademico(PeriodoAcademico periodoAcademico) {
		this.crnPeriodoAcademico = periodoAcademico;
	}

	/**
	 * @param crnTipo
	 *            the crnTipo to set
	 */
	public void setCrnTipo(Integer crnTipo) {
		this.crnTipo = crnTipo;
	}

	/**
	 * @return the crnDescripcion
	 */
	@Column(name = "CRN_DESCRIPCION", length = 512)
	public String getCrnDescripcion() {
		return crnDescripcion;
	}

	/**
	 * @param crnDescripcion
	 *            the crnDescripcion to set
	 */
	public void setCrnDescripcion(String crnDescripcion) {
		this.crnDescripcion = crnDescripcion;
	}

	/**
	 * @return the crnEstado
	 */
	@Column(name = "CRN_ESTADO", precision = 38, scale = 0)
	public Integer getCrnEstado() {
		return crnEstado;
	}

	/**
	 * @param crnEstado
	 *            the crnEstado to set
	 */
	public void setCrnEstado(Integer crnEstado) {
		this.crnEstado = crnEstado;
	}

	/**
	 * @return the cronogramaProcesoFlujos
	 */
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "crprCronograma")
	public List<CronogramaProcesoFlujo> getCronogramaProcesoFlujos() {
		return cronogramaProcesoFlujos;
	}

	/**
	 * @param cronogramaProcesoFlujos the cronogramaProcesoFlujos to set
	 */
	public void setCronogramaProcesoFlujos(List<CronogramaProcesoFlujo> cronogramaProcesoFlujos) {
		this.cronogramaProcesoFlujos = cronogramaProcesoFlujos;
	}

	
	
}
