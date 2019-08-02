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
   
 ARCHIVO:     RecordAcademicoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad RecordAcademico. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-03-2017 			David Arellano   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Clase (DTO) RecordAcademicoDto. DTO encargado de manejar los datos de la
 * entidad RecordAcademico.
 * 
 * @author dwarellano.
 * @version 1.0
 */
public class TitulacionDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	
	private BigDecimal numeroInscritos;
	private BigDecimal numeroAprobadosSinGracia;
	private BigDecimal numeroAprobadosGracia;
	private BigDecimal numeroReprobados;

	
	
	//convocatoria
	 private int cnvId;
	 private String cnvDescripcion;
	 
	 //facultad 
	 private int fclId;
	 private String fclDescripcion;
	 
	 //carrera
	 private int crrId;
	 private String crrDescripcion;
	 
	 //persona
	 private String nombres;
	 
	 //asentamiento nota
	 private BigDecimal asnoNotaFinal;
	 //asignacion titulacion
	 private String asttTemaTrabajo;
	 private String asttTutor;
	 
	 //ficha estudiante
	 private String fcesNumeroActa;
	 private Date fcesFechaActaGrado;
	
	private List<TitulacionDto> ListTitulacionDto;

	public TitulacionDto() {
	}

	public BigDecimal getNumeroInscritos() {
		return numeroInscritos;
	}

	public void setNumeroInscritos(BigDecimal numeroInscritos) {
		this.numeroInscritos = numeroInscritos;
	}

	public BigDecimal getNumeroAprobadosSinGracia() {
		return numeroAprobadosSinGracia;
	}

	public void setNumeroAprobadosSinGracia(BigDecimal numeroAprobadosSinGracia) {
		this.numeroAprobadosSinGracia = numeroAprobadosSinGracia;
	}

	public BigDecimal getNumeroAprobadosGracia() {
		return numeroAprobadosGracia;
	}

	public void setNumeroAprobadosGracia(BigDecimal numeroAprobadosGracia) {
		this.numeroAprobadosGracia = numeroAprobadosGracia;
	}

	public BigDecimal getNumeroReprobados() {
		return numeroReprobados;
	}

	public void setNumeroReprobados(BigDecimal numeroReprobados) {
		this.numeroReprobados = numeroReprobados;
	}

	public List<TitulacionDto> getListTitulacionDto() {
		return ListTitulacionDto;
	}

	public void setListTitulacionDto(List<TitulacionDto> listTitulacionDto) {
		ListTitulacionDto = listTitulacionDto;
	}

	public int getCnvId() {
		return cnvId;
	}

	public void setCnvId(int cnvId) {
		this.cnvId = cnvId;
	}

	public String getCnvDescripcion() {
		return cnvDescripcion;
	}

	public void setCnvDescripcion(String cnvDescripcion) {
		this.cnvDescripcion = cnvDescripcion;
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

	public int getFclId() {
		return fclId;
	}

	public void setFclId(int fclId) {
		this.fclId = fclId;
	}

	public String getFclDescripcion() {
		return fclDescripcion;
	}

	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public BigDecimal getAsnoNotaFinal() {
		return asnoNotaFinal;
	}

	public void setAsnoNotaFinal(BigDecimal asnoNotaFinal) {
		this.asnoNotaFinal = asnoNotaFinal;
	}

	public String getAsttTemaTrabajo() {
		return asttTemaTrabajo;
	}

	public void setAsttTemaTrabajo(String asttTemaTrabajo) {
		this.asttTemaTrabajo = asttTemaTrabajo;
	}

	public String getAsttTutor() {
		return asttTutor;
	}

	public void setAsttTutor(String asttTutor) {
		this.asttTutor = asttTutor;
	}

	public String getFcesNumeroActa() {
		return fcesNumeroActa;
	}

	public void setFcesNumeroActa(String fcesNumeroActa) {
		this.fcesNumeroActa = fcesNumeroActa;
	}

	public Date getFcesFechaActaGrado() {
		return fcesFechaActaGrado;
	}

	public void setFcesFechaActaGrado(Date fcesFechaActaGrado) {
		this.fcesFechaActaGrado = fcesFechaActaGrado;
	}

	
	
	

}
