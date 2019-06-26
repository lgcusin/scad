package model;
// Generated 22/06/2019 19:40:53 by Hibernate Tools 4.3.5.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Parametro generated by hbm2java
 */
@Entity
@Table(name = "PARAMETRO")
public class Parametro implements java.io.Serializable {

	private ParametroId id;
	private Facultad facultad;

	public Parametro() {
	}

	public Parametro(ParametroId id, Facultad facultad) {
		this.id = id;
		this.facultad = facultad;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "prmId", column = @Column(name = "PRM_ID", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "prmNombre", column = @Column(name = "PRM_NOMBRE", length = 30)),
			@AttributeOverride(name = "prmValor", column = @Column(name = "PRM_VALOR", length = 10)),
			@AttributeOverride(name = "fclId", column = @Column(name = "FCL_ID", nullable = false, precision = 22, scale = 0)) })
	public ParametroId getId() {
		return this.id;
	}

	public void setId(ParametroId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCL_ID", nullable = false, insertable = false, updatable = false)
	public Facultad getFacultad() {
		return this.facultad;
	}

	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}

}
