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

 ARCHIVO:     CronogramaActividadJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para las entidades que intervienen en el cronograma de actividades
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 20-06-2017 			Dennis Collaguazo  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) CronogramaActividadJdbcDto.
 * DTO encargado de manejar los datos para las entidades que intervienen en el cronograma de actividades. 
 * @author dcollaguazo.
 * @version 1.0
 */
public class CronogramaActividadJdbcDto implements Serializable{

	private static final long serialVersionUID = 6276443211796044950L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	//Tabla Planificacion Cronograma
	private int plcrId;
	private Integer plcrEstado;
	private Timestamp plcrFechaInicio;
	private Timestamp plcrFechaFin;
	
	//Tabla Cronograma Proceso Flujo
	private Integer crprflId;
	
	//Tabla Proceso Flujo
	private Integer prlfId;
	private String prlfDescripcion;
	private Integer prlfEstado;
	
	//Tabla Cronograma
	private Integer crnId;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;
	
	//Tabla Periodo Academico
	private Integer pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaInicio;
	private Date pracFechaFin;
	
	//Tabla Malla Periodo
	private Integer mlprId;
	private Integer mlprEstado;
	
	//Tabla Malla Curricular
	private Integer mlcrId;
	private Integer mlcrEstado;
	
	//Tabla Carrera
	private Integer crrId;
	private String crrDescripcion;
	
	//Tabla Tipo Nivel Apertura
	private Integer tinvapId;
	private Timestamp tinvapFechaInicio;
	private Timestamp tinvapFechaFin;
	private Integer tinvapEstado;
	private String tinvapObservacion;
	
	//Tabla Nivel Apertura
	private Integer nvapId;
	private String nvapDescripcion;
	
	//Tabla Tipo Apertura
	private Integer tiapId;
	private String tiapDescripcion;
	private Integer tiapEstado;
	

	public CronogramaActividadJdbcDto() {

	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getPlcrId() {
		return plcrId;
	}

	public void setPlcrId(int plcrId) {
		this.plcrId = plcrId;
	}

	public Integer getPlcrEstado() {
		return plcrEstado;
	}

	public void setPlcrEstado(Integer plcrEstado) {
		this.plcrEstado = plcrEstado;
	}

	public Timestamp getPlcrFechaInicio() {
		return plcrFechaInicio;
	}

	public void setPlcrFechaInicio(Timestamp plcrFechaInicio) {
		this.plcrFechaInicio = plcrFechaInicio;
	}

	public Timestamp getPlcrFechaFin() {
		return plcrFechaFin;
	}

	public void setPlcrFechaFin(Timestamp plcrFechaFin) {
		this.plcrFechaFin = plcrFechaFin;
	}

	public Integer getCrprflId() {
		return crprflId;
	}

	public void setCrprflId(Integer crprflId) {
		this.crprflId = crprflId;
	}

	public Integer getPrlfId() {
		return prlfId;
	}

	public void setPrlfId(Integer prlfId) {
		this.prlfId = prlfId;
	}

	public String getPrlfDescripcion() {
		return prlfDescripcion;
	}

	public void setPrlfDescripcion(String prlfDescripcion) {
		this.prlfDescripcion = prlfDescripcion;
	}

	public Integer getPrlfEstado() {
		return prlfEstado;
	}

	public void setPrlfEstado(Integer prlfEstado) {
		this.prlfEstado = prlfEstado;
	}

	public Integer getCrnId() {
		return crnId;
	}

	public void setCrnId(Integer crnId) {
		this.crnId = crnId;
	}

	public Integer getCrnTipo() {
		return crnTipo;
	}

	public void setCrnTipo(Integer crnTipo) {
		this.crnTipo = crnTipo;
	}

	public String getCrnDescripcion() {
		return crnDescripcion;
	}

	public void setCrnDescripcion(String crnDescripcion) {
		this.crnDescripcion = crnDescripcion;
	}

	public Integer getCrnEstado() {
		return crnEstado;
	}

	public void setCrnEstado(Integer crnEstado) {
		this.crnEstado = crnEstado;
	}

	public Integer getPracId() {
		return pracId;
	}

	public void setPracId(Integer pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}

	public Date getPracFechaInicio() {
		return pracFechaInicio;
	}

	public void setPracFechaInicio(Date pracFechaInicio) {
		this.pracFechaInicio = pracFechaInicio;
	}

	public Date getPracFechaFin() {
		return pracFechaFin;
	}

	public void setPracFechaFin(Date pracFechaFin) {
		this.pracFechaFin = pracFechaFin;
	}

	public Integer getMlprId() {
		return mlprId;
	}

	public void setMlprId(Integer mlprId) {
		this.mlprId = mlprId;
	}

	public Integer getMlprEstado() {
		return mlprEstado;
	}

	public void setMlprEstado(Integer mlprEstado) {
		this.mlprEstado = mlprEstado;
	}

	public Integer getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(Integer mlcrId) {
		this.mlcrId = mlcrId;
	}

	public Integer getMlcrEstado() {
		return mlcrEstado;
	}

	public void setMlcrEstado(Integer mlcrEstado) {
		this.mlcrEstado = mlcrEstado;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public Integer getTinvapId() {
		return tinvapId;
	}

	public void setTinvapId(Integer tinvapId) {
		this.tinvapId = tinvapId;
	}

	public Timestamp getTinvapFechaInicio() {
		return tinvapFechaInicio;
	}

	public void setTinvapFechaInicio(Timestamp tinvapFechaInicio) {
		this.tinvapFechaInicio = tinvapFechaInicio;
	}

	public Timestamp getTinvapFechaFin() {
		return tinvapFechaFin;
	}

	public void setTinvapFechaFin(Timestamp tinvapFechaFin) {
		this.tinvapFechaFin = tinvapFechaFin;
	}

	public Integer getTinvapEstado() {
		return tinvapEstado;
	}

	public void setTinvapEstado(Integer tinvapEstado) {
		this.tinvapEstado = tinvapEstado;
	}

	public String getTinvapObservacion() {
		return tinvapObservacion;
	}

	public void setTinvapObservacion(String tinvapObservacion) {
		this.tinvapObservacion = tinvapObservacion;
	}

	public Integer getNvapId() {
		return nvapId;
	}

	public void setNvapId(Integer nvapId) {
		this.nvapId = nvapId;
	}

	public String getNvapDescripcion() {
		return nvapDescripcion;
	}

	public void setNvapDescripcion(String nvapDescripcion) {
		this.nvapDescripcion = nvapDescripcion;
	}

	public Integer getTiapId() {
		return tiapId;
	}

	public void setTiapId(Integer tiapId) {
		this.tiapId = tiapId;
	}

	public String getTiapDescripcion() {
		return tiapDescripcion;
	}

	public void setTiapDescripcion(String tiapDescripcion) {
		this.tiapDescripcion = tiapDescripcion;
	}

	public Integer getTiapEstado() {
		return tiapEstado;
	}

	public void setTiapEstado(Integer tiapEstado) {
		this.tiapEstado = tiapEstado;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	@Override
	public String toString() {
		return "CronogramaActividadJdbcDto ["+
				", plcrId="+plcrId+
				", plcrEstado="+plcrEstado+
				", plcrFechaInicio="+plcrFechaInicio+
				", plcrFechaFin="+plcrFechaFin+
				", crprflId="+crprflId+
				", prlfId="+prlfId+
				", prlfDescripcion="+prlfDescripcion+
				", prlfEstado="+prlfEstado+
				", crnId="+crnId+
				", crnTipo="+crnTipo+
				", crnDescripcion="+crnDescripcion+
				", crnEstado="+crnEstado+
				", pracId="+pracId+
				", pracDescripcion="+pracDescripcion+
				", pracEstado="+pracEstado+
				", pracFechaInicio="+pracFechaInicio+
				", pracFechaFin="+pracFechaFin+
				", mlprId="+mlprId+
				", mlprEstado="+mlprEstado+
				", mlcrId="+mlcrId+
				", mlcrEstado="+mlcrEstado+
				", crrId="+crrId+
				", crrDescripcion="+crrDescripcion+
				", tinvapId="+tinvapId+
				", tinvapFechaInicio="+tinvapFechaInicio+
				", tinvapFechaFin="+tinvapFechaFin+
				", tinvapEstado="+tinvapEstado+
				", tinvapObservacion="+tinvapObservacion+
				", nvapId="+nvapId+
				", nvapDescripcion="+nvapDescripcion+
				", tiapId="+tiapId+
				", tiapDescripcion="+tiapDescripcion+
				", tiapEstado="+tiapEstado+
				"]";
	}
}
