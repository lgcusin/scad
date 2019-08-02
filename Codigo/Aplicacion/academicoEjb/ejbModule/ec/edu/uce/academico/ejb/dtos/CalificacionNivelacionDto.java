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
   
 ARCHIVO:     AulaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad AulaDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-05-2017 		  Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

import ec.edu.uce.academico.jpa.entidades.publico.Calificacion;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Paralelo;

/**
 * Clase (DTO) AulaDto. DTO encargado de manejar los datos de la entidad
 * AulaDto.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class CalificacionNivelacionDto implements Serializable {

	private static final long serialVersionUID = -3663263502299711591L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	private Calificacion clfAux;
	private Materia mtrAux;
	private Paralelo prl;
	
	
	public CalificacionNivelacionDto() {
	}
	public CalificacionNivelacionDto(Calificacion clfAux, Materia mtrAux) {
		this.clfAux = clfAux;
		this.mtrAux = mtrAux;
	}
	public Calificacion getClfAux() {
		return clfAux;
	}
	public void setClfAux(Calificacion clfAux) {
		this.clfAux = clfAux;
	}
	public Materia getMtrAux() {
		return mtrAux;
	}
	public void setMtrAux(Materia mtrAux) {
		this.mtrAux = mtrAux;
	}
	public Paralelo getPrl() {
		return prl;
	}
	public void setPrl(Paralelo prl) {
		this.prl = prl;
	}
	
	
	
}
