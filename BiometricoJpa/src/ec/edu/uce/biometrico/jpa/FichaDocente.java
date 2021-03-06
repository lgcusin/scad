package ec.edu.uce.biometrico.jpa;
// Generated 15/01/2019 9:06:49 by Hibernate Tools 4.3.5.Final

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

/**
 * FichaDocente generated by hbm2java
 */
@Entity
@Table(name = "FICHA_DOCENTE")
public class FichaDocente implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6171015051802154021L;
	private int fcdcId;
	private Integer fcdcEstado;
	private Persona fcdcPersona;
	private List<DetallePuesto> fcdcDetallePuestos = new ArrayList<DetallePuesto>(0);
	private List<HuellaDactilar> fcdcHuellaDactilares = new ArrayList<HuellaDactilar>(0);
	private List<Asistencia> fcdcAsistencias = new ArrayList<Asistencia>(0);

	public FichaDocente() {
	}

	public FichaDocente(int fcdcId) {
		this.fcdcId = fcdcId;
	}

	public FichaDocente(int fcdcId, Persona fcdcPersona, List<DetallePuesto> fcdcDetallePuestos,
			List<HuellaDactilar> huellaDactilars, List<Asistencia> asistencias) {
		this.fcdcId = fcdcId;
		this.fcdcPersona = fcdcPersona;
		this.fcdcDetallePuestos = fcdcDetallePuestos;
		this.fcdcHuellaDactilares = huellaDactilars;
		this.fcdcAsistencias = asistencias;
	}

	@Id
	@Column(name = "FCDC_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getFcdcId() {
		return this.fcdcId;
	}

	public void setFcdcId(int fcdcId) {
		this.fcdcId = fcdcId;
	}

	@ManyToOne
	@JoinColumn(name = "PRS_ID")
	public Persona getFcdcPersona() {
		return this.fcdcPersona;
	}

	public void setFcdcPersona(Persona fcdcPersona) {
		this.fcdcPersona = fcdcPersona;
	}

	@OneToMany(mappedBy = "dtpsFichaDocente")
	public List<DetallePuesto> getFcdcDetallePuestos() {
		return this.fcdcDetallePuestos;
	}

	public void setFcdcDetallePuestos(List<DetallePuesto> fcdcDetallePuestos) {
		this.fcdcDetallePuestos = fcdcDetallePuestos;
	}

	@Column(name = "fcdc_estado")
	public Integer getFcdcEstado() {
		return fcdcEstado;
	}

	public void setFcdcEstado(Integer fcdcEstado) {
		this.fcdcEstado = fcdcEstado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hldcFichaDocente")
	public List<HuellaDactilar> getFcdcHuellaDactilares() {
		return this.fcdcHuellaDactilares;
	}

	public void setFcdcHuellaDactilares(List<HuellaDactilar> huellaDactilars) {
		this.fcdcHuellaDactilares = huellaDactilars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assFichaDocente")
	public List<Asistencia> getFcdcAsistencias() {
		return this.fcdcAsistencias;
	}

	public void setFcdcAsistencias(List<Asistencia> asistencias) {
		this.fcdcAsistencias = asistencias;
	}

}
