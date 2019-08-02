package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import javax.persistence.*;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

import java.util.List;


/**
 * The persistent class for the TIPO_SISTEMA_CALIFICACION database table.
 * 
 */
@Entity
@Table(name="TIPO_SISTEMA_CALIFICACION", schema = ConstantesJpa.SCHEMA)
public class TipoSistemaCalificacion implements Serializable {
	
	private static final long serialVersionUID = -6959811215849778225L;
	
	private int tissclId;
	private String tissclDescripcion;
	private Integer tissclEstado;
	private String tissclObservacion;
	private List<SistemaCalificacion> sistemaCalificacions;

	public TipoSistemaCalificacion() {
	}

	public TipoSistemaCalificacion(int tissclId, String tissclDescripcion, Integer tissclEstado,
			String tissclObservacion, List<SistemaCalificacion> sistemaCalificacions) {
		this.tissclId = tissclId;
		this.tissclDescripcion = tissclDescripcion;
		this.tissclEstado = tissclEstado;
		this.tissclObservacion = tissclObservacion;
		this.sistemaCalificacions = sistemaCalificacions;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="TISSCL_ID", unique=true, nullable=false, precision=6,scale = 0)
	public int getTissclId() {
		return this.tissclId;
	}

	public void setTissclId(int tissclId) {
		this.tissclId = tissclId;
	}


	@Column(name="TISSCL_DESCRIPCION", length=256)
	public String getTissclDescripcion() {
		return this.tissclDescripcion;
	}

	public void setTissclDescripcion(String tissclDescripcion) {
		this.tissclDescripcion = tissclDescripcion;
	}


	@Column(name="TISSCL_ESTADO", precision=38)
	public Integer getTissclEstado() {
		return this.tissclEstado;
	}

	public void setTissclEstado(Integer tissclEstado) {
		this.tissclEstado = tissclEstado;
	}


	@Column(name="TISSCL_OBSERVACION", length=256)
	public String getTissclObservacion() {
		return this.tissclObservacion;
	}

	public void setTissclObservacion(String tissclObservacion) {
		this.tissclObservacion = tissclObservacion;
	}


	//bi-directional many-to-one association to SistemaCalificacion
	@OneToMany(mappedBy="tipoSistemaCalificacion")
	public List<SistemaCalificacion> getSistemaCalificacions() {
		return this.sistemaCalificacions;
	}

	public void setSistemaCalificacions(List<SistemaCalificacion> sistemaCalificacions) {
		this.sistemaCalificacions = sistemaCalificacions;
	}

	public SistemaCalificacion addSistemaCalificacion(SistemaCalificacion sistemaCalificacion) {
		getSistemaCalificacions().add(sistemaCalificacion);
		sistemaCalificacion.setTipoSistemaCalificacion(this);

		return sistemaCalificacion;
	}

	public SistemaCalificacion removeSistemaCalificacion(SistemaCalificacion sistemaCalificacion) {
		getSistemaCalificacions().remove(sistemaCalificacion);
		sistemaCalificacion.setTipoSistemaCalificacion(null);

		return sistemaCalificacion;
	}

}