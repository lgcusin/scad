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
   
 ARCHIVO:     NucleoProblemicoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad NucleoProblemico. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 07-Agosto-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) NucleoProblemicoDto.
 * DTO encargado de manejar los datos de la entidad NucleoProblemico. 
 * @author lmquishpei.
 * @version 1.0
 */
public class NucleoProblemicoDto implements Serializable{

	private static final long serialVersionUID = -3663263502299711591L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	//  Tabla Nucleo Problemico
	
	private int ncprId;
	private String ncprDescripcion;
	private Integer ncprEstado;
	
	
	
  //Nucleo Problemico Carrera
	
	private int ncprcrId;
	private Integer ncprcrEstado;
	
  // Carrera	
	
	private int crrId;
	private String crrDescripcion;
	private String crrDetalle;
	
	
		
	
	public NucleoProblemicoDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	
	
	
	
	
	public int getNcprId() {
		return ncprId;
	}

	public void setNcprId(int ncprId) {
		this.ncprId = ncprId;
	}

	public String getNcprDescripcion() {
		return ncprDescripcion;
	}

	public void setNcprDescripcion(String ncprDescripcion) {
		this.ncprDescripcion = ncprDescripcion;
	}

	public Integer getNcprEstado() {
		return ncprEstado;
	}

	public void setNcprEstado(Integer ncprEstado) {
		this.ncprEstado = ncprEstado;
	}

	public int getNcprcrId() {
		return ncprcrId;
	}

	public void setNcprcrId(int ncprcrId) {
		this.ncprcrId = ncprcrId;
	}

	public Integer getNcprcrEstado() {
		return ncprcrEstado;
	}

	public void setNcprcrEstado(Integer ncprcrEstado) {
		this.ncprcrEstado = ncprcrEstado;
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

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
	
	public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		
				
		sb.append(" Nucleo Problemico " );
		sb.append(tabulador + "ncprId : " + ncprId);
		sb.append(tabulador + "ncprEstado : " + (ncprEstado==null? "NULL":ncprEstado));
		sb.append(tabulador + "ncprDescripcion : " + (ncprDescripcion==null? "NULL":ncprDescripcion));
		
		
		sb.append(" Nucleo Problemico Carrera " );
		sb.append(tabulador + "ncprcrId : " + ncprcrId);
		sb.append(tabulador + "ncprcrEstado : " + (ncprcrEstado==null? "NULL":ncprcrEstado));
		
	
    
		return sb.toString();
    }

	

	
}
