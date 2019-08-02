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
   
 ARCHIVO:     DependenciaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad DependenciaDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19-05-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) AulaDto. DTO encargado de manejar los datos de la entidad
 * AulaDto.
 * 
 * @author lmquishpei.
 * @version 1.0
 */
public class DependenciaDto implements Serializable, Comparable<DependenciaDto> {

	private static final long serialVersionUID = -3663263502299711591L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Dependencia

	private int dpnId;
	private String dpnDescripcion;
	private Integer dpnEstado;
	private Integer dpnCampus;
	private Integer dpnJerarquia;

	// Tabla Carrera
	private int crrId;
	private String crrDescripcion;
	private String crrDetalle;

	// Tabla

	public DependenciaDto() {

	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Integer getDpnEstado() {
		return dpnEstado;
	}

	public void setDpnEstado(Integer dpnEstado) {
		this.dpnEstado = dpnEstado;
	}

	public Integer getDpnCampus() {
		return dpnCampus;
	}

	public void setDpnCampus(Integer dpnCampus) {
		this.dpnCampus = dpnCampus;
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

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Integer getDpnJerarquia() {
		return dpnJerarquia;
	}

	public void setDpnJerarquia(Integer dpnJerarquia) {
		this.dpnJerarquia = dpnJerarquia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dpnDescripcion == null) ? 0 : dpnDescripcion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		DependenciaDto other = (DependenciaDto) obj;
		if (dpnDescripcion == null) {
			if (other.dpnDescripcion != null)
				return false;
		} else if (!dpnDescripcion.equals(other.dpnDescripcion))
			return false;
		return true;
	}

	public int compareTo(DependenciaDto o) {
		return dpnDescripcion.compareTo(o.getDpnDescripcion());
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();

		sb.append(" Dependencia ");
		sb.append(tabulador + "dpnId : " + dpnId);
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));
		sb.append(tabulador + "dpnEstado : " + (dpnEstado == null ? "NULL" : dpnEstado));
		sb.append(tabulador + "dpnCampus : " + (dpnCampus == null ? "NULL" : dpnCampus));

		sb.append(" Carrera ");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));

		return sb.toString();
	}

}
