/*****************************
 * *********************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ContenidoEvaluacionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Contenido. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 10-10-2017 			Arturo Villafuerte   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Clase (DTO) ContenidoEvaluacionDto.
 * DTO encargado de manejar los datos de la entidad Contenido. 
 * @author ajvillafuerte.
 * @version 1.0
 */
public class ContenidoEvaluacionDatosDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
//	TABLA EVALUACION
	private int evaId;
	private String evaDescripcion;
	private Integer evaEstado;
	private Integer evaPrflId;
	private Timestamp evaFecha;
	private Timestamp evaCronogramaInicio;
	private Timestamp evaCronogramaFin;
	private String evaUsuario; 
	
	//TABLA CARRERA
	private int crrId;
	private String crrDescripcion;
	
	//TABLA PERIODO ACADEMICO
	private int pracId;
	private String pracDescripcion; 
	
	//TABLA TIPO EVALUACION
	private int tpevId;
	private String tpevDescripcion; 

	//TABLA TIPO ASIGNACION_EVALUACION
	private int asevId;
	
	public ContenidoEvaluacionDatosDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	 
	public int getEvaId() {
		return evaId;
	}

	public void setEvaId(int evaId) {
		this.evaId = evaId;
	}

	public String getEvaDescripcion() {
		return evaDescripcion;
	}

	public void setEvaDescripcion(String evaDescripcion) {
		this.evaDescripcion = evaDescripcion;
	}

	public Integer getEvaEstado() {
		return evaEstado;
	}

	public void setEvaEstado(Integer evaEstado) {
		this.evaEstado = evaEstado;
	}

	public Integer getEvaPrflId() {
		return evaPrflId;
	}

	public void setEvaPrflId(Integer evaPrflId) {
		this.evaPrflId = evaPrflId;
	}

	public Timestamp getEvaFecha() {
		return evaFecha;
	}

	public void setEvaFecha(Timestamp evaFecha) {
		this.evaFecha = evaFecha;
	}

	public Timestamp getEvaCronogramaInicio() {
		return evaCronogramaInicio;
	}

	public void setEvaCronogramaInicio(Timestamp evaCronogramaInicio) {
		this.evaCronogramaInicio = evaCronogramaInicio;
	}

	public Timestamp getEvaCronogramaFin() {
		return evaCronogramaFin;
	}

	public void setEvaCronogramaFin(Timestamp evaCronogramaFin) {
		this.evaCronogramaFin = evaCronogramaFin;
	}

	public String getEvaUsuario() {
		return evaUsuario;
	}

	public void setEvaUsuario(String evaUsuario) {
		this.evaUsuario = evaUsuario;
	}

	public int getPracId() {
		return pracId;
	}

	public void setPracId(int pracId) {
		this.pracId = pracId;
	}

	public int getTpevId() {
		return tpevId;
	}

	public void setTpevId(int tpevId) {
		this.tpevId = tpevId;
	}
 
	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public String getTpevDescripcion() {
		return tpevDescripcion;
	}

	public void setTpevDescripcion(String tpevDescripcion) {
		this.tpevDescripcion = tpevDescripcion;
	}

	public int getAsevId() {
		return asevId;
	}

	public void setAsevId(int asevId) {
		this.asevId = asevId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
  
	@Override
	public String toString() {
		return "ContenidoEvaluacionDatosDto [evaId=" + evaId + ", evaDescripcion=" + evaDescripcion + ", evaEstado="
				+ evaEstado + ", evaPrflId=" + evaPrflId + ", evaFecha=" + evaFecha + ", evaCronogramaInicio="
				+ evaCronogramaInicio + ", evaCronogramaFin=" + evaCronogramaFin + ", evaUsuario=" + evaUsuario
				+ ", pracId=" + pracId + ", tpevId=" + tpevId + ", crrId=" + crrId + ", crrDescripcion="
				+ crrDescripcion + "]";
	}
	
}
