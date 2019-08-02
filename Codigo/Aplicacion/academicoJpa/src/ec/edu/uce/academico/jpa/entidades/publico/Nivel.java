package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 02/03/2017 8:53:22 by Hibernate Tools 3.5.0.Final

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "NIVEL", schema = ConstantesJpa.SCHEMA)
public class Nivel implements java.io.Serializable {

	private static final long serialVersionUID = 4300487347129046L;
	private int nvlId;
	private String nvlDescripcion;
	private Integer nvlEstado;
	private Integer nvlNumeral;
	private List<MallaCurricularMateria> nvlPrerequisitoMallaCurricularMaterias = new ArrayList<>(0);
	private List<MallaCurricularMateria> nvlMallaCurricularMaterias = new ArrayList<>(0);
	private List<MallaCurricularNivel> nvlListMallaCurricularNivel;

	public Nivel() {
	}

	public Nivel(int nvlId) {
		this.nvlId = nvlId;
	}

	public Nivel(int nvlId, String nvlDescripcion, Integer nvlEstado,
			List<MallaCurricularMateria> nvlPrerequisitoMallaCurricularMaterias,
			List<MallaCurricularMateria> nvlMallaCurricularMaterias) {
		this.nvlId = nvlId;
		this.nvlDescripcion = nvlDescripcion;
		this.nvlEstado = nvlEstado;
		this.nvlPrerequisitoMallaCurricularMaterias = nvlPrerequisitoMallaCurricularMaterias;
		this.nvlMallaCurricularMaterias = nvlMallaCurricularMaterias;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "NVL_ID", unique = true, nullable = false, precision = 2, scale = 0)
	public int getNvlId() {
		return this.nvlId;
	}

	public void setNvlId(int nvlId) {
		this.nvlId = nvlId;
	}

	@Column(name = "NVL_DESCRIPCION", length = 50)
	@Length(max = 50)
	public String getNvlDescripcion() {
		return this.nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

	@Column(name = "NVL_ESTADO", precision = 2, scale = 0)
	public Integer getNvlEstado() {
		return this.nvlEstado;
	}

	public void setNvlEstado(Integer nvlEstado) {
		this.nvlEstado = nvlEstado;
	}

	@Column(name = "NVL_NUMERAL")
	public Integer getNvlNumeral() {
		return nvlNumeral;
	}

	public void setNvlNumeral(Integer nvlNumeral) {
		this.nvlNumeral = nvlNumeral;
	}

	@OneToMany(mappedBy = "mlcrmtNivelPreRequisito")
	public List<MallaCurricularMateria> getNvlPrerequisitoMallaCurricularMaterias() {
		return this.nvlPrerequisitoMallaCurricularMaterias;
	}

	public void setNvlPrerequisitoMallaCurricularMaterias(
			List<MallaCurricularMateria> nvlPrerequisitoMallaCurricularMaterias) {
		this.nvlPrerequisitoMallaCurricularMaterias = nvlPrerequisitoMallaCurricularMaterias;
	}

	@OneToMany(mappedBy = "mlcrmtNivel")
	public List<MallaCurricularMateria> getNvlMallaCurricularMaterias() {
		return this.nvlMallaCurricularMaterias;
	}

	public void setNvlMallaCurricularMaterias(List<MallaCurricularMateria> nvlMallaCurricularMaterias) {
		this.nvlMallaCurricularMaterias = nvlMallaCurricularMaterias;
	}

	@OneToMany(mappedBy = "mlcrnvNivel")
	public List<MallaCurricularNivel> getNvlListMallaCurricularNivel() {
		return nvlListMallaCurricularNivel;
	}

	public void setNvlListMallaCurricularNivel(List<MallaCurricularNivel> nvlListMallaCurricularNivel) {
		this.nvlListMallaCurricularNivel = nvlListMallaCurricularNivel;
	}

}
