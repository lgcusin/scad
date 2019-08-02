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
   
 ARCHIVO:     PeriodoAcademicoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad PeriodoAcademico. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-MAR-2017 		  Vinicio Rosales   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Clase (DTO) PeridoAcademicoDto. DTO encargado de manejar los datos de la
 * entidad PeridoAcademico.
 * 
 * @author jvrosales.
 * @version 1.0
 */
public class PeriodoAcademicoDto implements Serializable, Comparable<PeriodoAcademicoDto> {

	private static final long serialVersionUID = 5790947863915164372L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla PeriodoAcademico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;
	private Integer pracTipo;

	// Tabla Cronograma
	private int crnId;
	private Integer crnPeriodoAcademicoId;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;

	// Tabla Cronograma_Proceso_Flujo
	private int crprflId;
	private Integer crprProcesoFlujoId;
	private Integer crprCronogramaId;
	private Integer crprflOrdinal;

	// Tabla Planificacion_Cronograma
	private int plcrId;
	private Integer plcrCronogramaProcesoFlujoId;
	private Integer plcrEstado;
	private String plcrFechaInicio;
	private String plcrFechaFin;

	// Tabla Proceso_Flujo
	private int prflId;
	private String prflDescripcion;
	private Integer prflEstado;

	private Integer cmpaId;

	private PersonaDto pracPersonaDto;

	private List<RecordEstudianteDto> pracListRecordEstudianteDto;

	public PeriodoAcademicoDto() {
	}

	public PeriodoAcademicoDto(int pracId, String pracDescripcion) {
		super();
		this.pracId = pracId;
		this.pracDescripcion = pracDescripcion;
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

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

	public Date getPracFechaIncio() {
		return pracFechaIncio;
	}

	public void setPracFechaIncio(Date pracFechaIncio) {
		this.pracFechaIncio = pracFechaIncio;
	}

	public Date getPracFechaFin() {
		return pracFechaFin;
	}

	public void setPracFechaFin(Date pracFechaFin) {
		this.pracFechaFin = pracFechaFin;
	}

	public Integer getPracTipo() {
		return pracTipo;
	}

	public void setPracTipo(Integer pracTipo) {
		this.pracTipo = pracTipo;
	}

	public int getCrnId() {
		return crnId;
	}

	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}

	public Integer getCrnPeriodoAcademicoId() {
		return crnPeriodoAcademicoId;
	}

	public void setCrnPeriodoAcademicoId(Integer crnPeriodoAcademicoId) {
		this.crnPeriodoAcademicoId = crnPeriodoAcademicoId;
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

	public Integer getCrprProcesoFlujoId() {
		return crprProcesoFlujoId;
	}

	public void setCrprProcesoFlujoId(Integer crprProcesoFlujoId) {
		this.crprProcesoFlujoId = crprProcesoFlujoId;
	}

	public Integer getCrprCronogramaId() {
		return crprCronogramaId;
	}

	public void setCrprCronogramaId(Integer crprCronogramaId) {
		this.crprCronogramaId = crprCronogramaId;
	}

	public int getPlcrId() {
		return plcrId;
	}

	public void setPlcrId(int plcrId) {
		this.plcrId = plcrId;
	}

	public Integer getPlcrCronogramaProcesoFlujoId() {
		return plcrCronogramaProcesoFlujoId;
	}

	public void setPlcrCronogramaProcesoFlujoId(Integer plcrCronogramaProcesoFlujoId) {
		this.plcrCronogramaProcesoFlujoId = plcrCronogramaProcesoFlujoId;
	}

	public Integer getPlcrEstado() {
		return plcrEstado;
	}

	public void setPlcrEstado(Integer plcrEstado) {
		this.plcrEstado = plcrEstado;
	}

	public String getPlcrFechaInicio() {
		return plcrFechaInicio;
	}

	public void setPlcrFechaInicio(String plcrFechaInicio) {
		this.plcrFechaInicio = plcrFechaInicio;
	}

	public String getPlcrFechaFin() {
		return plcrFechaFin;
	}

	public void setPlcrFechaFin(String plcrFechaFin) {
		this.plcrFechaFin = plcrFechaFin;
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

	public Integer getCmpaId() {
		return cmpaId;
	}

	public void setCmpaId(Integer cmpaId) {
		this.cmpaId = cmpaId;
	}

	public Integer getCrprflOrdinal() {
		return crprflOrdinal;
	}

	public void setCrprflOrdinal(Integer crprflOrdinal) {
		this.crprflOrdinal = crprflOrdinal;
	}

	public List<RecordEstudianteDto> getPracListRecordEstudianteDto() {
		return pracListRecordEstudianteDto;
	}

	public void setPracListRecordEstudianteDto(List<RecordEstudianteDto> pracListRecordEstudianteDto) {
		this.pracListRecordEstudianteDto = pracListRecordEstudianteDto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pracDescripcion == null) ? 0 : pracDescripcion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		PeriodoAcademicoDto other = (PeriodoAcademicoDto) obj;
		if (pracDescripcion == null) {
			if (other.pracDescripcion != null)
				return false;
		} else if (!pracDescripcion.equals(other.pracDescripcion))
			return false;
		return true;
	}

	public PersonaDto getPracPersonaDto() {
		return pracPersonaDto;
	}

	public void setPracPersonaDto(PersonaDto pracPersonaDto) {
		this.pracPersonaDto = pracPersonaDto;
	}

	@Override
	public int compareTo(PeriodoAcademicoDto o) {
		// TODO Auto-generated method stub
		return pracDescripcion.trim().compareToIgnoreCase(o.pracDescripcion.trim());
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	@Override
	public String toString() {
		return "PeriodoAcademicoDto [pracId=" + pracId + ", pracDescripcion=" + pracDescripcion + ", pracEstado="
				+ pracEstado + ", pracFechaIncio=" + pracFechaIncio + ", pracFechaFin=" + pracFechaFin + ", pracTipo="
				+ pracTipo + ", crnId=" + crnId + ", crnPeriodoAcademicoId=" + crnPeriodoAcademicoId + ", crnTipo="
				+ crnTipo + ", crnDescripcion=" + crnDescripcion + ", crnEstado=" + crnEstado + ", crprflId=" + crprflId
				+ ", crprProcesoFlujoId=" + crprProcesoFlujoId + ", crprCronogramaId=" + crprCronogramaId + ", plcrId="
				+ plcrId + ", plcrCronogramaProcesoFlujoId=" + plcrCronogramaProcesoFlujoId + ", plcrEstado="
				+ plcrEstado + ", plcrFechaInicio=" + plcrFechaInicio + ", plcrFechaFin=" + plcrFechaFin + ", prflId="
				+ prflId + ", prflDescripcion=" + prflDescripcion + ", prflEstado=" + prflEstado + "]";
	}

}
