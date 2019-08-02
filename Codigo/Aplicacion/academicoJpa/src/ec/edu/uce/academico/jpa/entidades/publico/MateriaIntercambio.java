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
   
 ARCHIVO:     MateriaIntercambio.java	  
 DESCRIPCION: Entity Bean que representa a la tabla MateriaIntercambio de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
05/Octubre/2018		 Daniel Ortiz 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;

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
import javax.persistence.Table;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;


/**
 * Clase (Entity Bean) MateriaIntercambio.
 * Entity Bean que representa a la tabla MateriaIntercambio de la BD.
 * @author dortiz.
 * @version 1.0
 */
@Entity
@Table(name="MATERIA_INTERCAMBIO", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name="MateriaIntercambio.findAll", query="SELECT m FROM MateriaIntercambio m"),
				@NamedQuery(name = "MateriaIntercambio.findPorCarreraIntercambio", query = "SELECT m FROM MateriaIntercambio m WHERE m.mtinCarreraIntercambio.crinId = :crinId") })
public class MateriaIntercambio implements Serializable {
	
	private static final long serialVersionUID = -6573098025078530475L;
	private int mtinId;
	private Materia mtinMateria;
	private String mtinCodigo;
	private String mtinDescripcion;
	private Integer mtinEstado;
	private CarreraIntercambio mtinCarreraIntercambio;

	public MateriaIntercambio() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name="MTIN_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getMtinId() {
		return this.mtinId;
	}

	public void setMtinId(int mtinId) {
		this.mtinId = mtinId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MTR_ID")
	public Materia getMtinMateria() {
		return mtinMateria;
	}


	public void setMtinMateria(Materia mtinMateria) {
		this.mtinMateria = mtinMateria;
	}


	@Column(name="MTIN_CODIGO")
	public String getMtinCodigo() {
		return this.mtinCodigo;
	}

	public void setMtinCodigo(String mtinCodigo) {
		this.mtinCodigo = mtinCodigo;
	}


	@Column(name="MTIN_DESCRIPCION")
	public String getMtinDescripcion() {
		return this.mtinDescripcion;
	}

	public void setMtinDescripcion(String mtinDescripcion) {
		this.mtinDescripcion = mtinDescripcion;
	}

	@Column(name="MTIN_ESTADO")
	public Integer getMtinEstado() {
		return mtinEstado;
	}

	public void setMtinEstado(Integer mtinEstado) {
		this.mtinEstado = mtinEstado;
	}


	//bi-directional many-to-one association to CarreraIntercambio
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CRIN_ID")
	public CarreraIntercambio getMtinCarreraIntercambio() {
		return mtinCarreraIntercambio;
	}


	public void setMtinCarreraIntercambio(CarreraIntercambio mtinCarreraIntercambio) {
		this.mtinCarreraIntercambio = mtinCarreraIntercambio;
	}

}