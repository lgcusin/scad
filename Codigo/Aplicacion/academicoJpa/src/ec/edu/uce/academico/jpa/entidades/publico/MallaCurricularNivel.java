package ec.edu.uce.academico.jpa.entidades.publico;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "MALLA_CURRICULAR_NIVEL", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "MallaCurricularNivel.findAll", query = "SELECT m FROM MallaCurricularNivel m"),
				@NamedQuery(name = "MallaCurricularNivel.findPorMallaCurricularNivel", query = "SELECT m FROM MallaCurricularNivel m WHERE m.mlcrnvMallaCurricular.mlcrId = :mallaCurricularId and m.mlcrnvNivel.nvlId = :nivelId") })
public class MallaCurricularNivel implements java.io.Serializable {

	private static final long serialVersionUID = -3720870946601118825L;

	private int mlcrnvId;
	private Integer mlcrnvCreditos;
	private Integer mlcrnvCreditosAcumulado;
	private MallaCurricular mlcrnvMallaCurricular;
	private Nivel mlcrnvNivel;

	
	public MallaCurricularNivel() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MLCRNV_ID", unique = true, nullable = false)
	public int getMlcrnvId() {
		return mlcrnvId;
	}

	public void setMlcrnvId(int mlcrnvId) {
		this.mlcrnvId = mlcrnvId;
	}

	@Column(name = "MLCRNV_CREDITOS", precision = 22, scale = 0)
	public Integer getMlcrnvCreditos() {
		return mlcrnvCreditos;
	}

	public void setMlcrnvCreditos(Integer mlcrnvCreditos) {
		this.mlcrnvCreditos = mlcrnvCreditos;
	}

	@Column(name = "MLCRNV_CREDITOS_ACUMULADO", precision = 22, scale = 0)
	public Integer getMlcrnvCreditosAcumulado() {
		return mlcrnvCreditosAcumulado;
	}

	public void setMlcrnvCreditosAcumulado(Integer mlcrnvCreditosAcumulado) {
		this.mlcrnvCreditosAcumulado = mlcrnvCreditosAcumulado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MLCR_ID")
	public MallaCurricular getMlcrnvMallaCurricular() {
		return mlcrnvMallaCurricular;
	}

	public void setMlcrnvMallaCurricular(MallaCurricular mlcrnvMallaCurricular) {
		this.mlcrnvMallaCurricular = mlcrnvMallaCurricular;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NVL_ID")
	public Nivel getMlcrnvNivel() {
		return mlcrnvNivel;
	}

	public void setMlcrnvNivel(Nivel mlcrnvNivel) {
		this.mlcrnvNivel = mlcrnvNivel;
	}

}
