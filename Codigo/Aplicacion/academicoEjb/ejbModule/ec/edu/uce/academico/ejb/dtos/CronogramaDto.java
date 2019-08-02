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
   
 ARCHIVO:     CronogramaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Cronograma. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		         AUTOR          			   COMENTARIOS
 12-12-2017 			Vinicio Rosales   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 * Clase (DTO) CronogramaDto. DTO encargado de manejar los datos de la entidad
 * Cronograma.
 * 
 * @author jvrosales.
 * @version 1.0
 */
public class CronogramaDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	// Tabla Periodo Academico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaInicio;
	private Date pracFechaFin;

	// Tabla Cronograma
	private int crnId;
	private Integer crnPracId;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;

	// Tabla Cronograma Proceso Flujo
	private int crprflId;
	private Integer crprflCrnId;
	private Integer crprflPrflId;
	private Integer crprflOrdinal;

	// Tabla Proceso Flujo
	private int prflId;
	private String prflDescripcion;
	private Integer prflEstado;

	// Planificacion Cronograma
	private int plcrId;
	private Integer plcrCrprflId;
	private Integer plcrEstado;
	private String plcrStrFechaInicio;
	private String plcrStrFechaFin;
	private Date plcrFechaInicio;
	private Date plcrFechaFin;
	private Timestamp plcrFechaInicial;
	private Timestamp plcrFechaFinal;

	public CronogramaDto() {
	}

	public int getPracId() {
		return pracId;
	}

	public void setPracId(int pracId) {
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

	public int getCrnId() {
		return crnId;
	}

	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}

	public Integer getCrnPracId() {
		return crnPracId;
	}

	public void setCrnPracId(Integer crnPracId) {
		this.crnPracId = crnPracId;
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

	public int getCrprflId() {
		return crprflId;
	}

	public void setCrprflId(int crprflId) {
		this.crprflId = crprflId;
	}

	public Integer getCrprflCrnId() {
		return crprflCrnId;
	}

	public void setCrprflCrnId(Integer crprflCrnId) {
		this.crprflCrnId = crprflCrnId;
	}

	public Integer getCrprflPrflId() {
		return crprflPrflId;
	}

	public void setCrprflPrflId(Integer crprflPrflId) {
		this.crprflPrflId = crprflPrflId;
	}

	public int getPrflId() {
		return prflId;
	}

	public void setPrflId(int prflId) {
		this.prflId = prflId;
	}

	public String getPrflDescripcion() {
		return prflDescripcion;
	}

	public void setPrflDescripcion(String prflDescripcion) {
		this.prflDescripcion = prflDescripcion;
	}

	public Integer getPrflEstado() {
		return prflEstado;
	}

	public void setPrflEstado(Integer prflEstado) {
		this.prflEstado = prflEstado;
	}

	public int getPlcrId() {
		return plcrId;
	}

	public void setPlcrId(int plcrId) {
		this.plcrId = plcrId;
	}

	public Integer getPlcrCrprflId() {
		return plcrCrprflId;
	}

	public void setPlcrCrprflId(Integer plcrCrprflId) {
		this.plcrCrprflId = plcrCrprflId;
	}

	public Integer getPlcrEstado() {
		return plcrEstado;
	}

	public void setPlcrEstado(Integer plcrEstado) {
		this.plcrEstado = plcrEstado;
	}

	public Date getPlcrFechaInicio() {
		return plcrFechaInicio;
	}

	public void setPlcrFechaInicio(Date plcrFechaInicio) {
		this.plcrFechaInicio = plcrFechaInicio;
	}

	public Date getPlcrFechaFin() {
		return plcrFechaFin;
	}

	public void setPlcrFechaFin(Date plcrFechaFin) {
		this.plcrFechaFin = plcrFechaFin;
	}

	public String getPlcrStrFechaInicio() {
		return plcrStrFechaInicio;
	}

	public void setPlcrStrFechaInicio(String plcrStrFechaInicio) {
		this.plcrStrFechaInicio = plcrStrFechaInicio;
	}

	public String getPlcrStrFechaFin() {
		return plcrStrFechaFin;
	}

	public void setPlcrStrFechaFin(String plcrStrFechaFin) {
		this.plcrStrFechaFin = plcrStrFechaFin;
	}

	public Integer getCrprflOrdinal() {
		return crprflOrdinal;
	}

	public void setCrprflOrdinal(Integer crprflOrdinal) {
		this.crprflOrdinal = crprflOrdinal;
	}

	public Timestamp getPlcrFechaInicial() {
		return plcrFechaInicial;
	}

	public void setPlcrFechaInicial(Timestamp plcrFechaInicial) {
		this.plcrFechaInicial = plcrFechaInicial;
	}

	public Timestamp getPlcrFechaFinal() {
		return plcrFechaFinal;
	}

	public void setPlcrFechaFinal(Timestamp plcrFechaFinal) {
		this.plcrFechaFinal = plcrFechaFinal;
	}

	public String toString() {
		return "CronogramaDto [pracId=" + pracId + ", pracDescripcion=" + pracDescripcion + ", pracEstado=" + pracEstado
				+ ", pracFechaInicio=" + pracFechaInicio + ", pracFechaFin=" + pracFechaFin + ", crnId=" + crnId
				+ ", crnPracId=" + crnPracId + ", crnTipo=" + crnTipo + ", crnDescripcion=" + crnDescripcion
				+ ", crnEstado=" + crnEstado + ", crprflId=" + crprflId + ", crprflCrnId=" + crprflCrnId
				+ ", crprflPrflId=" + crprflPrflId + ", crprflOrdinal=" + crprflOrdinal + ", prflId=" + prflId
				+ ", prflDescripcion=" + prflDescripcion + ", prflEstado=" + prflEstado + ", plcrId=" + plcrId
				+ ", plcrCrprflId=" + plcrCrprflId + ", plcrEstado=" + plcrEstado + ", plcrStrFechaInicio="
				+ plcrStrFechaInicio + ", plcrStrFechaFin=" + plcrStrFechaFin + ", plcrFechaInicio=" + plcrFechaInicio
				+ ", plcrFechaFin=" + plcrFechaFin + ", plcrFechaInicial=" + plcrFechaInicial + ", plcrFechaFinal="
				+ plcrFechaFinal + "]";
	}

}
