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
public class ContenidoEvaluacionDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
//	TABLA CONTENIDO
	private int cntId;
	private String cntDescripcion;
	private Timestamp cntFecha;
	private Integer cntSeleccion;
	private Integer asevCrr;
	private String cntUsuario;
	private int asevId;
	private int evaId;
	private int tpcnfntpevId;
	private String cntOficioApelacion;
	private int cntSeleccionInicial;
	private Timestamp cntRegistroApelacion;

// 	TABLA TIPO_CONTENIDO
	private int tpcnId;
	private String tpcnDescripcion;
	private Integer tpcnEstado;
	private Timestamp tpcnFecha;
	private Integer tpcnNumMax;
	private Integer tpcnNumeral;
	private Integer tpcnObligatoriedad;
	private Integer tpcnTipo;
	private Integer tpcnTipoComponente;
	private Integer tpcnTipoSeleccion;
	private String tpcnUsuario;
	private int tpcnTipoContenido;
	 
	
	public String cntSeleccionAux;
	public Integer cntSeleccionApelacion;
	
	public ContenidoEvaluacionDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	
	public int getCntId() {
		return cntId;
	}

	public void setCntId(int cntId) {
		this.cntId = cntId;
	}

	public String getCntDescripcion() {
		return cntDescripcion;
	}

	public void setCntDescripcion(String cntDescripcion) {
		this.cntDescripcion = cntDescripcion;
	}

	public Timestamp getCntFecha() {
		return cntFecha;
	}

	public void setCntFecha(Timestamp cntFecha) {
		this.cntFecha = cntFecha;
	}

	public Integer getCntSeleccion() {
		return cntSeleccion;
	}

	public void setCntSeleccion(Integer cntSeleccion) {
		this.cntSeleccion = cntSeleccion;
	}

	public String getCntUsuario() {
		return cntUsuario;
	}

	public void setCntUsuario(String cntUsuario) {
		this.cntUsuario = cntUsuario;
	}

	public int getAsevId() {
		return asevId;
	}

	public void setAsevId(int asevId) {
		this.asevId = asevId;
	}

	public int getEvaId() {
		return evaId;
	}

	public void setEvaId(int evaId) {
		this.evaId = evaId;
	}

	public int getTpcnfntpevId() {
		return tpcnfntpevId;
	}

	public void setTpcnfntpevId(int tpcnfntpevId) {
		this.tpcnfntpevId = tpcnfntpevId;
	}

	public int getTpcnId() {
		return tpcnId;
	}

	public void setTpcnId(int tpcnId) {
		this.tpcnId = tpcnId;
	}

	public String getTpcnDescripcion() {
		return tpcnDescripcion;
	}

	public void setTpcnDescripcion(String tpcnDescripcion) {
		this.tpcnDescripcion = tpcnDescripcion;
	}

	public Integer getTpcnEstado() {
		return tpcnEstado;
	}

	public void setTpcnEstado(Integer tpcnEstado) {
		this.tpcnEstado = tpcnEstado;
	}

	public Timestamp getTpcnFecha() {
		return tpcnFecha;
	}

	public void setTpcnFecha(Timestamp tpcnFecha) {
		this.tpcnFecha = tpcnFecha;
	}

	public Integer getTpcnNumMax() {
		return tpcnNumMax;
	}

	public void setTpcnNumMax(Integer tpcnNumMax) {
		this.tpcnNumMax = tpcnNumMax;
	}

	public Integer getTpcnNumeral() {
		return tpcnNumeral;
	}

	public void setTpcnNumeral(Integer tpcnNumeral) {
		this.tpcnNumeral = tpcnNumeral;
	}

	public Integer getTpcnObligatoriedad() {
		return tpcnObligatoriedad;
	}

	public void setTpcnObligatoriedad(Integer tpcnObligatoriedad) {
		this.tpcnObligatoriedad = tpcnObligatoriedad;
	}

	public Integer getTpcnTipo() {
		return tpcnTipo;
	}

	public void setTpcnTipo(Integer tpcnTipo) {
		this.tpcnTipo = tpcnTipo;
	}

	public Integer getTpcnTipoComponente() {
		return tpcnTipoComponente;
	}

	public void setTpcnTipoComponente(Integer tpcnTipoComponente) {
		this.tpcnTipoComponente = tpcnTipoComponente;
	}

	public Integer getTpcnTipoSeleccion() {
		return tpcnTipoSeleccion;
	}

	public void setTpcnTipoSeleccion(Integer tpcnTipoSeleccion) {
		this.tpcnTipoSeleccion = tpcnTipoSeleccion;
	}

	public String getTpcnUsuario() {
		return tpcnUsuario;
	}

	public void setTpcnUsuario(String tpcnUsuario) {
		this.tpcnUsuario = tpcnUsuario;
	}

	public int getTpcnTipoContenido() {
		return tpcnTipoContenido;
	}

	public void setTpcnTipoContenido(int tpcnTipoContenido) {
		this.tpcnTipoContenido = tpcnTipoContenido;
	}

	 
	public Integer getAsevCrr() {
		return asevCrr;
	}

	public void setAsevCrr(Integer asevCrr) {
		this.asevCrr = asevCrr;
	}

	public String getCntOficioApelacion() {
		return cntOficioApelacion;
	}

	public void setCntOficioApelacion(String cntOficioApelacion) {
		this.cntOficioApelacion = cntOficioApelacion;
	}

	
	

	public String getCntSeleccionAux() {
		return cntSeleccionAux;
	}

	public void setCntSeleccionAux(String cntSeleccionAux) {
		this.cntSeleccionAux = cntSeleccionAux;
	}

	
	public int getCntSeleccionInicial() {
		return cntSeleccionInicial;
	}

	public void setCntSeleccionInicial(int cntSeleccionInicial) {
		this.cntSeleccionInicial = cntSeleccionInicial;
	}

	public Timestamp getCntRegistroApelacion() {
		return cntRegistroApelacion;
	}

	public void setCntRegistroApelacion(Timestamp cntRegistroApelacion) {
		this.cntRegistroApelacion = cntRegistroApelacion;
	}

	
	public Integer getCntSeleccionApelacion() {
		return cntSeleccionApelacion;
	}

	public void setCntSeleccionApelacion(Integer cntSeleccionApelacion) {
		this.cntSeleccionApelacion = cntSeleccionApelacion;
	}

	@Override
	public String toString() {
		return "ContenidoEvaluacionDto [cntId=" + cntId + ", cntDescripcion=" + cntDescripcion + ", cntFecha="
				+ cntFecha + ", cntSeleccion=" + cntSeleccion + ", asevCrr=" + asevCrr + ", cntUsuario=" + cntUsuario
				+ ", asevId=" + asevId + ", evaId=" + evaId + ", tpcnfntpevId=" + tpcnfntpevId + ", cntOficioApelacion="
				+ cntOficioApelacion + ", tpcnId=" + tpcnId + ", tpcnDescripcion=" + tpcnDescripcion + ", tpcnEstado="
				+ tpcnEstado + ", tpcnFecha=" + tpcnFecha + ", tpcnNumMax=" + tpcnNumMax + ", tpcnNumeral="
				+ tpcnNumeral + ", tpcnObligatoriedad=" + tpcnObligatoriedad + ", tpcnTipo=" + tpcnTipo
				+ ", tpcnTipoComponente=" + tpcnTipoComponente + ", tpcnTipoSeleccion=" + tpcnTipoSeleccion
				+ ", tpcnUsuario=" + tpcnUsuario + ", tpcnTipoContenido=" + tpcnTipoContenido + "]";
	}

	
	
}
