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
   
 ARCHIVO:     TituloDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad TituloDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-NOV-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;
import java.io.Serializable;

/**
 * Clase (DTO) TituloDto.
 * DTO encargado de manejar los datos de la entidad TituloDto. 
 * @author lmquishpei.
 * @version 1.0
 */

public class TituloDto implements Serializable{

	private static final long serialVersionUID = -3663263502299711591L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	//  Tabla Titulo
	private int ttlId;
	private String ttlDescripcion;
	private Integer ttlSexo;
	private Integer ttlEstado;
	private Integer ttlTipo;
	
	public TituloDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	
	public int getTtlId() {
		return ttlId;
	}

	public void setTtlId(int ttlId) {
		this.ttlId = ttlId;
	}

	public String getTtlDescripcion() {
		return ttlDescripcion;
	}

	public void setTtlDescripcion(String ttlDescripcion) {
		this.ttlDescripcion = ttlDescripcion;
	}

	public Integer getTtlSexo() {
		return ttlSexo;
	}

	public void setTtlSexo(Integer ttlSexo) {
		this.ttlSexo = ttlSexo;
	}

	public Integer getTtlEstado() {
		return ttlEstado;
	}

	public void setTtlEstado(Integer ttlEstado) {
		this.ttlEstado = ttlEstado;
	}

	public Integer getTtlTipo() {
		return ttlTipo;
	}

	public void setTtlTipo(Integer ttlTipo) {
		this.ttlTipo = ttlTipo;
	}
	
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
	
	
	public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(" Titulo " );
		sb.append(tabulador + "ttlId : " + ttlId);
		sb.append(tabulador + "ttlDescripcion : " + (ttlDescripcion==null? "NULL":ttlDescripcion));
		sb.append(tabulador + "ttlSexo : " + (ttlSexo==null? "NULL":ttlSexo));
		sb.append(tabulador + "ttlEstado : " + (ttlEstado==null? "NULL":ttlEstado));
		sb.append(tabulador + "ttlTipo : " + (ttlTipo==null? "NULL":ttlTipo));
		
		return sb.toString();
    }

	
}
