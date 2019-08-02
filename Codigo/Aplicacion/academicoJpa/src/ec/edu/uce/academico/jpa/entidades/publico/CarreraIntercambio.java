/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     CarreraIntercambio.java	  
 DESCRIPCION: Entity Bean que representa a la tabla CarreraIntercambio de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
05/Octubre/2018		 Daniel Ortiz 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * Clase (Entity Bean) CarreraIntercambio.
 * Entity Bean que representa a la tabla Carrera_Intercambio de la BD.
 * @author dortiz.
 * @version 1.0
 */
@Entity
@Table(name="CARRERA_INTERCAMBIO", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name = "CarreraIntercambio.findAll", query="SELECT c FROM CarreraIntercambio c"),
				@NamedQuery(name = "CarreraIntercambio.findPorFichaInscripcion", query = "SELECT c FROM CarreraIntercambio c WHERE c.crinFichaInscripcion.fcinId = :fcinId") })
public class CarreraIntercambio implements Serializable {
	
	private static final long serialVersionUID = 8793269488067511594L;
	private int crinId;
	private Carrera crinCarrera;
	private String crinAutorizacion;
	private Integer crinEstado;
	private Timestamp crinFechaRegistro;
	private String crinObservacion;
	private String crinUserRegistro;
	private FichaInscripcion crinFichaInscripcion;
	private List<MateriaIntercambio> crinMateriaIntercambios;

	public CarreraIntercambio() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="CRIN_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getCrinId() {
		return this.crinId;
	}

	public void setCrinId(int crrintId) {
		this.crinId = crrintId;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRR_ID")
	public Carrera getCrinCarrera() {
		return this.crinCarrera;
	}


	public void setCrinCarrera(Carrera crinCarrera) {
		this.crinCarrera = crinCarrera;
	}
	

	@Column(name="CRIN_AUTORIZACION")
	public String getCrinAutorizacion() {
		return this.crinAutorizacion;
	}

	public void setCrinAutorizacion(String crinAutorizacion) {
		this.crinAutorizacion = crinAutorizacion;
	}


	@Column(name="CRIN_ESTADO")
	public Integer getCrinEstado() {
		return this.crinEstado;
	}

	public void setCrinEstado(Integer crinEstado) {
		this.crinEstado = crinEstado;
	}


	@Column(name="CRIN_FECHA_REGISTRO")
	public Timestamp getCrinFechaRegistro() {
		return this.crinFechaRegistro;
	}

	public void setCrinFechaRegistro(Timestamp crinFechaRegistro) {
		this.crinFechaRegistro = crinFechaRegistro;
	}


	@Column(name="CRIN_OBSERVACION")
	public String getCrinObservacion() {
		return this.crinObservacion;
	}

	public void setCrinObservacion(String crinObservacion) {
		this.crinObservacion = crinObservacion;
	}


	@Column(name="CRIN_USER_REGISTRO")
	public String getCrinUserRegistro() {
		return this.crinUserRegistro;
	}

	public void setCrinUserRegistro(String crinUserRegistro) {
		this.crinUserRegistro = crinUserRegistro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCIN_ID")
	public FichaInscripcion getCrinFichaInscripcion() {
		return crinFichaInscripcion;
	}


	public void setCrinFichaInscripcion(FichaInscripcion crinFichaInscripcion) {
		this.crinFichaInscripcion = crinFichaInscripcion;
	}


	//bi-directional many-to-one association to MateriaIntercambio
	@OneToMany(fetch = FetchType.LAZY, mappedBy="mtinCarreraIntercambio")
	public List<MateriaIntercambio> getCrinMateriaIntercambios() {
		return this.crinMateriaIntercambios;
	}

	public void setCrinMateriaIntercambios(List<MateriaIntercambio> crinMateriaIntercambios) {
		this.crinMateriaIntercambios = crinMateriaIntercambios;
	}

	public MateriaIntercambio addMateriaIntercambio(MateriaIntercambio crinMateriaIntercambio) {
		getCrinMateriaIntercambios().add(crinMateriaIntercambio);
		crinMateriaIntercambio.setMtinCarreraIntercambio(this);

		return crinMateriaIntercambio;
	}

	public MateriaIntercambio removeMateriaIntercambio(MateriaIntercambio crinMateriaIntercambio) {
		getCrinMateriaIntercambios().remove(crinMateriaIntercambio);
		crinMateriaIntercambio.setMtinCarreraIntercambio(null);

		return crinMateriaIntercambio;
	}

}