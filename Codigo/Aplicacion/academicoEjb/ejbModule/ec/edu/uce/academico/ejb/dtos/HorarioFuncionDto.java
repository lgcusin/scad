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
   
 ARCHIVO:     HorarioFuncionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad HorarioFuncionDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 115-ABR-2019 		  	Freddy Guzman 						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) HorarioFuncionDto. DTO encargado de manejar los datos de la
 * entidad HorarioFuncionDto.
 * 
 * @author fgguzman.
 * @version 1.0
 */
public class HorarioFuncionDto implements Serializable {

	private static final long serialVersionUID = -170929921624298403L;
	private int hrfnId;
	private String hrfnDescripcion;
	private Integer hrfnDia;
	private Integer hrfnHoraInicio;
	private Integer hrfnHoraFin;
	private Integer hrfnFuncion;
	private Integer hrfnActividad;
	private Integer hrfnPeriodoAcademico;
	private Integer hrfnDetallePuesto;
	private Integer hrfnEstado;

	public int getHrfnId() {
		return hrfnId;
	}

	public void setHrfnId(int hrfnId) {
		this.hrfnId = hrfnId;
	}

	public String getHrfnDescripcion() {
		return hrfnDescripcion;
	}

	public void setHrfnDescripcion(String hrfnDescripcion) {
		this.hrfnDescripcion = hrfnDescripcion;
	}

	public Integer getHrfnDia() {
		return hrfnDia;
	}

	public void setHrfnDia(Integer hrfnDia) {
		this.hrfnDia = hrfnDia;
	}

	public Integer getHrfnHoraInicio() {
		return hrfnHoraInicio;
	}

	public void setHrfnHoraInicio(Integer hrfnHoraInicio) {
		this.hrfnHoraInicio = hrfnHoraInicio;
	}

	public Integer getHrfnHoraFin() {
		return hrfnHoraFin;
	}

	public void setHrfnHoraFin(Integer hrfnHoraFin) {
		this.hrfnHoraFin = hrfnHoraFin;
	}

	public Integer getHrfnFuncion() {
		return hrfnFuncion;
	}

	public void setHrfnFuncion(Integer hrfnFuncion) {
		this.hrfnFuncion = hrfnFuncion;
	}

	public Integer getHrfnActividad() {
		return hrfnActividad;
	}

	public void setHrfnActividad(Integer hrfnActividad) {
		this.hrfnActividad = hrfnActividad;
	}

	public Integer getHrfnPeriodoAcademico() {
		return hrfnPeriodoAcademico;
	}

	public void setHrfnPeriodoAcademico(Integer hrfnPeriodoAcademico) {
		this.hrfnPeriodoAcademico = hrfnPeriodoAcademico;
	}

	public Integer getHrfnDetallePuesto() {
		return hrfnDetallePuesto;
	}

	public void setHrfnDetallePuesto(Integer hrfnDetallePuesto) {
		this.hrfnDetallePuesto = hrfnDetallePuesto;
	}

	public Integer getHrfnEstado() {
		return hrfnEstado;
	}

	public void setHrfnEstado(Integer hrfnEstado) {
		this.hrfnEstado = hrfnEstado;
	}

}
