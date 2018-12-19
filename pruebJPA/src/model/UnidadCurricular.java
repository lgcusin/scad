package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the UNIDAD_CURRICULAR database table.
 * 
 */
@Entity
@Table(name="UNIDAD_CURRICULAR")
@NamedQuery(name="UnidadCurricular.findAll", query="SELECT u FROM UnidadCurricular u")
public class UnidadCurricular implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UNCR_ID")
	private long uncrId;

	@Column(name="UNCR_DESCRIPCION")
	private String uncrDescripcion;

	//bi-directional many-to-one association to Contenido
	@ManyToOne
	@JoinColumn(name="CNT_ID")
	private Contenido contenido;

	//bi-directional many-to-one association to MallaCurricularMateria
	@ManyToOne
	@JoinColumn(name="MLCRMT_ID")
	private MallaCurricularMateria mallaCurricularMateria;

	//bi-directional many-to-one association to Syllabo
	@ManyToOne
	@JoinColumn(name="SYL_ID")
	private Syllabo syllabo;

	public UnidadCurricular() {
	}

	public long getUncrId() {
		return this.uncrId;
	}

	public void setUncrId(long uncrId) {
		this.uncrId = uncrId;
	}

	public String getUncrDescripcion() {
		return this.uncrDescripcion;
	}

	public void setUncrDescripcion(String uncrDescripcion) {
		this.uncrDescripcion = uncrDescripcion;
	}

	public Contenido getContenido() {
		return this.contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	public MallaCurricularMateria getMallaCurricularMateria() {
		return this.mallaCurricularMateria;
	}

	public void setMallaCurricularMateria(MallaCurricularMateria mallaCurricularMateria) {
		this.mallaCurricularMateria = mallaCurricularMateria;
	}

	public Syllabo getSyllabo() {
		return this.syllabo;
	}

	public void setSyllabo(Syllabo syllabo) {
		this.syllabo = syllabo;
	}

}