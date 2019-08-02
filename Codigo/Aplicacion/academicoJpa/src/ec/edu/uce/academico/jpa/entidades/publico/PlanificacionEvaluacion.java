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
   
 ARCHIVO:     PlanificacionEvaluacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla PlanificacionEvaluacion de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
15/ENERO/2018		     Freddy Guzmán			            Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jpa.entidades.publico;

import java.io.Serializable;
import java.sql.Timestamp;

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
 * Clase (Entity Bean) PlanificacionEvaluacion. Entity Bean que representa a la
 * tabla PlanificacionEvaluacion de la BD.
 * @author fgguzman.
 * @version 1.0
 */
@Entity
@Table(name = "PLANIFICACION_EVALUACION", schema = ConstantesJpa.SCHEMA)
@NamedQueries({
		@NamedQuery(name = "PlanificacionEvaluacion.findAll", query = "SELECT pe FROM PlanificacionEvaluacion pe"),
		@NamedQuery(name = "PlanificacionEvaluacion.findPorProcesoEvaluacion", query = "SELECT pe FROM PlanificacionEvaluacion pe WHERE pe.plevEvaluacion.evaId = :evaluacionId and pe.plevProcesoFlujo.prflId = :procesoId and pe.plevEstado = :plevEstado") })
public class PlanificacionEvaluacion implements Serializable {

	private static final long serialVersionUID = -6573098025078530475L;
	private int plevId;
	private Timestamp plevFechaInicio;
	private Timestamp plevFechaFin;
	private Integer plevEstado;
	private ProcesoFlujo plevProcesoFlujo;
	private Evaluacion plevEvaluacion;

	public PlanificacionEvaluacion() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "PLEV_ID", unique = true, nullable = false)
	public int getPlevId() {
		return plevId;
	}

	public void setPlevId(int plevId) {
		this.plevId = plevId;
	}

	@Column(name = "PLEV_FECHA_INICIO")
	public Timestamp getPlevFechaInicio() {
		return plevFechaInicio;
	}

	public void setPlevFechaInicio(Timestamp plevFechaInicio) {
		this.plevFechaInicio = plevFechaInicio;
	}

	@Column(name = "PLEV_FECHA_FIN")
	public Timestamp getPlevFechaFin() {
		return plevFechaFin;
	}

	public void setPlevFechaFin(Timestamp plevFechaFin) {
		this.plevFechaFin = plevFechaFin;
	}

	@Column(name = "PLEV_ESTADO")
	public Integer getPlevEstado() {
		return plevEstado;
	}

	public void setPlevEstado(Integer plevEstado) {
		this.plevEstado = plevEstado;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRFL_ID")
	public ProcesoFlujo getPlevProcesoFlujo() {
		return plevProcesoFlujo;
	}

	public void setPlevProcesoFlujo(ProcesoFlujo plevProcesoFlujo) {
		this.plevProcesoFlujo = plevProcesoFlujo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EVL_ID")
	public Evaluacion getPlevEvaluacion() {
		return plevEvaluacion;
	}

	public void setPlevEvaluacion(Evaluacion plevEvaluacion) {
		this.plevEvaluacion = plevEvaluacion;
	}

}