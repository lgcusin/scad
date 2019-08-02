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
   
 ARCHIVO:     InstitucionAcademicaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad InstitucionAcademicaDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-NOV-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;

/**
 * Clase (DTO) InstitucionAcademicaDto.
 * DTO encargado de manejar los datos de la entidad InstitucionAcademicaDto. 
 * @author lmquishpei.
 * @version 1.0
 */
public class InstitucionAcademicaDto implements Serializable{

	private static final long serialVersionUID = -3663263502299711591L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	//  Tabla Institucion Academica
	private int inacId;
	private Ubicacion inacUbicacion;
	private String inacCodigoSniese;
	private String inacDescripcion;
	private Integer inacNivel;
	private Integer inacTipo;
	private String inacTipoSniese;
	
	public InstitucionAcademicaDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	public int getInacId() {
		return inacId;
	}

	public void setInacId(int inacId) {
		this.inacId = inacId;
	}

	public Ubicacion getInacUbicacion() {
		return inacUbicacion;
	}

	public void setInacUbicacion(Ubicacion inacUbicacion) {
		this.inacUbicacion = inacUbicacion;
	}

	public String getInacCodigoSniese() {
		return inacCodigoSniese;
	}

	public void setInacCodigoSniese(String inacCodigoSniese) {
		this.inacCodigoSniese = inacCodigoSniese;
	}

	public String getInacDescripcion() {
		return inacDescripcion;
	}

	public void setInacDescripcion(String inacDescripcion) {
		this.inacDescripcion = inacDescripcion;
	}

	public Integer getInacNivel() {
		return inacNivel;
	}

	public void setInacNivel(Integer inacNivel) {
		this.inacNivel = inacNivel;
	}

	public Integer getInacTipo() {
		return inacTipo;
	}

	public void setInacTipo(Integer inacTipo) {
		this.inacTipo = inacTipo;
	}

	public String getInacTipoSniese() {
		return inacTipoSniese;
	}

	public void setInacTipoSniese(String inacTipoSniese) {
		this.inacTipoSniese = inacTipoSniese;
	}	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
	
	
	public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(" Institucion Academica " );
		sb.append(tabulador + "inacId : " + inacId);
		sb.append(tabulador + "inacUbicacion : " + (inacUbicacion==null? "NULL":inacUbicacion));
		sb.append(tabulador + "inacCodigoSniese : " + (inacCodigoSniese==null? "NULL":inacCodigoSniese));
		sb.append(tabulador + "inacDescripcion : " + (inacDescripcion==null? "NULL":inacDescripcion));
		sb.append(tabulador + "inacNivel : " + (inacNivel==null? "NULL":inacNivel));
		sb.append(tabulador + "inacTipo : " + (inacTipo==null? "NULL":inacTipo));
		sb.append(tabulador + "inacTipoSniese : " + (inacTipoSniese==null? "NULL":inacTipoSniese));
		
		return sb.toString();
    }

	
	

	
}
