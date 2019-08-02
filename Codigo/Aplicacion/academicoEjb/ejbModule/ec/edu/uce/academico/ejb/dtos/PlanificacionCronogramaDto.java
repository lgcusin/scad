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
   
 ARCHIVO:     PlanificacionCronogramaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad PlanificacionCronograma. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-09-2017 		  Arturo Villafuerte   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) PlanificacionCronogramaDto. DTO encargado de manejar los datos de
 * la entidad PlanificacionCronograma.
 * 
 * @author ajvillafuerte.
 * @version 1.0
 */
public class PlanificacionCronogramaDto implements Serializable {

	private static final long serialVersionUID = 5790947863915164372L;
	// Tabla PLANIFICACION_CRONOGRAMA
	private int plcrId;
	private Integer plcrEstado;
	private Timestamp plcrFechaInicio;
	private Timestamp plcrFechaFin;
	private Date plcrFechaInicial;
	private Date plcrFechaFinal;

	private CronogramaDto plcrCronogramaDto;
	private PeriodoAcademicoDto plcrPeriodoAcademicoDto;
	private ProcesoFlujoDto plcrProcesoFlujoDto;

	public PlanificacionCronogramaDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

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

	public CronogramaDto getPlcrCronogramaDto() {
		return plcrCronogramaDto;
	}

	public void setPlcrCronogramaDto(CronogramaDto plcrCronogramaDto) {
		this.plcrCronogramaDto = plcrCronogramaDto;
	}

	public PeriodoAcademicoDto getPlcrPeriodoAcademicoDto() {
		return plcrPeriodoAcademicoDto;
	}

	public void setPlcrPeriodoAcademicoDto(PeriodoAcademicoDto plcrPeriodoAcademicoDto) {
		this.plcrPeriodoAcademicoDto = plcrPeriodoAcademicoDto;
	}

	public ProcesoFlujoDto getPlcrProcesoFlujoDto() {
		return plcrProcesoFlujoDto;
	}

	public void setPlcrProcesoFlujoDto(ProcesoFlujoDto plcrProcesoFlujoDto) {
		this.plcrProcesoFlujoDto = plcrProcesoFlujoDto;
	}

	public Date getPlcrFechaInicial() {
		return plcrFechaInicial;
	}

	public void setPlcrFechaInicial(Date plcrFechaInicial) {
		this.plcrFechaInicial = plcrFechaInicial;
	}

	public Date getPlcrFechaFinal() {
		return plcrFechaFinal;
	}

	public void setPlcrFechaFinal(Date plcrFechaFinal) {
		this.plcrFechaFinal = plcrFechaFinal;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	@Override
	public String toString() {
		return "PlanificacionCronogramaDto [plcrFechaInicio=" + plcrFechaInicio + ", plcrFechaFin=" + plcrFechaFin
				+ ", plcrId=" + plcrId + ", plcrEstado=" + plcrEstado + "]";
	}

}
