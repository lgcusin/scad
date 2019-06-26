package model;
// Generated 22/06/2019 19:40:53 by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Resultados generated by hbm2java
 */
@Entity
@Table(name = "RESULTADOS")
public class Resultados implements java.io.Serializable {

	private BigDecimal rslId;
	private Syllabo syllabo;
	private String rslLogrosAprendizaje;
	private String rslResultadoEstudiante;

	public Resultados() {
	}

	public Resultados(BigDecimal rslId, Syllabo syllabo) {
		this.rslId = rslId;
		this.syllabo = syllabo;
	}

	public Resultados(BigDecimal rslId, Syllabo syllabo, String rslLogrosAprendizaje, String rslResultadoEstudiante) {
		this.rslId = rslId;
		this.syllabo = syllabo;
		this.rslLogrosAprendizaje = rslLogrosAprendizaje;
		this.rslResultadoEstudiante = rslResultadoEstudiante;
	}

	@Id

	@Column(name = "RSL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRslId() {
		return this.rslId;
	}

	public void setRslId(BigDecimal rslId) {
		this.rslId = rslId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABO_SYL_ID", nullable = false)
	public Syllabo getSyllabo() {
		return this.syllabo;
	}

	public void setSyllabo(Syllabo syllabo) {
		this.syllabo = syllabo;
	}

	@Column(name = "RSL_LOGROS_APRENDIZAJE", length = 300)
	public String getRslLogrosAprendizaje() {
		return this.rslLogrosAprendizaje;
	}

	public void setRslLogrosAprendizaje(String rslLogrosAprendizaje) {
		this.rslLogrosAprendizaje = rslLogrosAprendizaje;
	}

	@Column(name = "RSL_RESULTADO_ESTUDIANTE", length = 300)
	public String getRslResultadoEstudiante() {
		return this.rslResultadoEstudiante;
	}

	public void setRslResultadoEstudiante(String rslResultadoEstudiante) {
		this.rslResultadoEstudiante = rslResultadoEstudiante;
	}

}
