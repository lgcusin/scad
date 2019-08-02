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
 15-05-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) GrupoDto. DTO encargado de manejar los datos de la entidad
 * GrupoDto.
 * 
 * @author fgguzman.
 * @version 1.0
 */
public class GrupoDto implements Serializable {

	private static final long serialVersionUID = -3663263502299711591L;
	private int grpId;
	private String grpCodigo;
	private String grpDescripcion;
	private Integer grpEstado;
	private Integer grpCrrId;

	private PersonaDto grpPersonaDtoCoordinador;
	private PersonaDto grpPersonaDtoEvaluador;
	private CarreraDto grpCarreraDto;

	public GrupoDto() {

	}

	public int getGrpId() {
		return grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	public String getGrpCodigo() {
		return grpCodigo;
	}

	public void setGrpCodigo(String grpCodigo) {
		this.grpCodigo = grpCodigo;
	}

	public String getGrpDescripcion() {
		return grpDescripcion;
	}

	public void setGrpDescripcion(String grpDescripcion) {
		this.grpDescripcion = grpDescripcion;
	}

	public Integer getGrpEstado() {
		return grpEstado;
	}

	public void setGrpEstado(Integer grpEstado) {
		this.grpEstado = grpEstado;
	}

	public Integer getGrpCrrId() {
		return grpCrrId;
	}

	public void setGrpCrrId(Integer grpCrrId) {
		this.grpCrrId = grpCrrId;
	}

	public CarreraDto getGrpCarreraDto() {
		return grpCarreraDto;
	}

	public void setGrpCarreraDto(CarreraDto grpCarreraDto) {
		this.grpCarreraDto = grpCarreraDto;
	}

	public PersonaDto getGrpPersonaDtoEvaluador() {
		return grpPersonaDtoEvaluador;
	}

	public void setGrpPersonaDtoEvaluador(PersonaDto grpPersonaDtoEvaluador) {
		this.grpPersonaDtoEvaluador = grpPersonaDtoEvaluador;
	}

	public PersonaDto getGrpPersonaDtoCoordinador() {
		return grpPersonaDtoCoordinador;
	}

	public void setGrpPersonaDtoCoordinador(PersonaDto grpPersonaDtoCoordinador) {
		this.grpPersonaDtoCoordinador = grpPersonaDtoCoordinador;
	}

}
