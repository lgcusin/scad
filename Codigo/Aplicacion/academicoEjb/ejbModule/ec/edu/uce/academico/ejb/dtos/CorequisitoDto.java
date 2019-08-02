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
   
 ARCHIVO:     CorequisitoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad CorequisitoDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 13-07-2017 		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) CorequisitoDto.
 * DTO encargado de manejar los datos de la entidad CorequisitoDto. 
 * @author lmquishpei.
 * @version 1.0
 */
public class CorequisitoDto implements Serializable{

	private static final long serialVersionUID = -3663263502299711591L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	//  Tabla Corequisitos
	private int crqId;
	private String crqCodigo;
	private String crqDescripcion;
	private Integer crqEstado;
	
	
	// Tabla Materia
	
	private int mtrId;
	private Integer mtrCmfrId;
	private Integer mtrTpmtId;
	private Integer mtrSubId;
	private Integer mtcNcprId;
	private Integer mtrCrrId;
	private String mtrCodigo;
	private String mtrCodigoSniese;
	private String mtrDescripcion;
	private Integer mtrEstado;
	private Integer mtrHoras;
	private Integer mtrIntegradoraHoras;
	private Integer mtrPreProfesionalHoras;
	private Integer mtrHorasCien;
	private Integer mtrRelacionTrabajo;
	
	//Tabla TipoMateria
	private int tpmtId;
	private String tpmtDescripcion;
	private Integer tpmtEstado;
	
	
		
	
	public CorequisitoDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	
	
	
	
	
	public int getCrqId() {
		return crqId;
	}

	public void setCrqId(int crqId) {
		this.crqId = crqId;
	}

	public String getCrqCodigo() {
		return crqCodigo;
	}

	public void setCrqCodigo(String crqCodigo) {
		this.crqCodigo = crqCodigo;
	}

	public String getCrqDescripcion() {
		return crqDescripcion;
	}

	public void setCrqDescripcion(String crqDescripcion) {
		this.crqDescripcion = crqDescripcion;
	}

	public Integer getCrqEstado() {
		return crqEstado;
	}

	public void setCrqEstado(Integer crqEstado) {
		this.crqEstado = crqEstado;
	}
	
	
	public int getMtrId() {
		return mtrId;
	}

	

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
	}

	public Integer getMtrCmfrId() {
		return mtrCmfrId;
	}

	public void setMtrCmfrId(Integer mtrCmfrId) {
		this.mtrCmfrId = mtrCmfrId;
	}

	public Integer getMtrTpmtId() {
		return mtrTpmtId;
	}

	public void setMtrTpmtId(Integer mtrTpmtId) {
		this.mtrTpmtId = mtrTpmtId;
	}

	public Integer getMtrSubId() {
		return mtrSubId;
	}

	public void setMtrSubId(Integer mtrSubId) {
		this.mtrSubId = mtrSubId;
	}

	public Integer getMtcNcprId() {
		return mtcNcprId;
	}

	public void setMtcNcprId(Integer mtcNcprId) {
		this.mtcNcprId = mtcNcprId;
	}

	public Integer getMtrCrrId() {
		return mtrCrrId;
	}

	public void setMtrCrrId(Integer mtrCrrId) {
		this.mtrCrrId = mtrCrrId;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public String getMtrCodigoSniese() {
		return mtrCodigoSniese;
	}

	public void setMtrCodigoSniese(String mtrCodigoSniese) {
		this.mtrCodigoSniese = mtrCodigoSniese;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public Integer getMtrEstado() {
		return mtrEstado;
	}

	public void setMtrEstado(Integer mtrEstado) {
		this.mtrEstado = mtrEstado;
	}

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public Integer getMtrIntegradoraHoras() {
		return mtrIntegradoraHoras;
	}

	public void setMtrIntegradoraHoras(Integer mtrIntegradoraHoras) {
		this.mtrIntegradoraHoras = mtrIntegradoraHoras;
	}

	public Integer getMtrPreProfesionalHoras() {
		return mtrPreProfesionalHoras;
	}

	public void setMtrPreProfesionalHoras(Integer mtrPreProfesionalHoras) {
		this.mtrPreProfesionalHoras = mtrPreProfesionalHoras;
	}

	public Integer getMtrHorasCien() {
		return mtrHorasCien;
	}

	public void setMtrHorasCien(Integer mtrHorasCien) {
		this.mtrHorasCien = mtrHorasCien;
	}

	public Integer getMtrRelacionTrabajo() {
		return mtrRelacionTrabajo;
	}

	public void setMtrRelacionTrabajo(Integer mtrRelacionTrabajo) {
		this.mtrRelacionTrabajo = mtrRelacionTrabajo;
	}

	public int getTpmtId() {
		return tpmtId;
	}

	public void setTpmtId(int tpmtId) {
		this.tpmtId = tpmtId;
	}

	public String getTpmtDescripcion() {
		return tpmtDescripcion;
	}

	public void setTpmtDescripcion(String tpmtDescripcion) {
		this.tpmtDescripcion = tpmtDescripcion;
	}

	public Integer getTpmtEstado() {
		return tpmtEstado;
	}

	public void setTpmtEstado(Integer tpmtEstado) {
		this.tpmtEstado = tpmtEstado;
	}	
	
	
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
	
	public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		
		
		
		sb.append(" Corequisito " );
		
		sb.append(tabulador + "crqId : " + crqId);
		sb.append(tabulador + "crqCodigo : " + (crqCodigo==null? "NULL":crqCodigo));
		sb.append(tabulador + "crqDescripcion : " + (crqDescripcion==null? "NULL":crqDescripcion));
		sb.append(tabulador + "crqEstado : " + (crqEstado==null? "NULL":crqEstado));
		
    	sb.append(" Materia " );
		sb.append(tabulador + "mtrId : " + mtrId);
		sb.append(tabulador + "mtrCmfrId : " + (mtrCmfrId==null? "NULL":mtrCmfrId));
		sb.append(tabulador + "mtrTpmtId : " + (mtrTpmtId==null? "NULL":mtrTpmtId));
		sb.append(tabulador + "mtrSubId : " + (mtrSubId==null? "NULL":mtrSubId));
		sb.append(tabulador + "mtcNcprId : " + (mtcNcprId==null? "NULL":mtcNcprId));
		sb.append(tabulador + "mtrCrrId : " + (mtrCrrId==null? "NULL":mtrCrrId));
		sb.append(tabulador + "mtrCodigo : " + (mtrCodigo==null? "NULL":mtrCodigo));
		sb.append(tabulador + "mtrCodigoSniese : " + (mtrCodigoSniese==null? "NULL":mtrCodigoSniese));
		sb.append(tabulador + "mtrDescripcion : " + (mtrDescripcion==null? "NULL":mtrDescripcion));
		sb.append(tabulador + "mtrEstado : " + (mtrEstado==null? "NULL":mtrEstado));
		sb.append(tabulador + "mtrHoras : " + (mtrHoras==null? "NULL":mtrHoras));
		sb.append(tabulador + "mtrIntegradoraHoras : " + (mtrIntegradoraHoras==null? "NULL":mtrIntegradoraHoras));
		sb.append(tabulador + "mtrPreProfesionalHoras : " + (mtrPreProfesionalHoras==null? "NULL":mtrPreProfesionalHoras));
		sb.append(tabulador + "mtrHorasCien : " + (mtrHorasCien==null? "NULL":mtrHorasCien));
		sb.append(tabulador + "mtrRelacionTrabajo : " + (mtrRelacionTrabajo==null? "NULL":mtrRelacionTrabajo));
		
		sb.append(" Tipo Materia " );		
		sb.append(tabulador + "tpmtId : " + tpmtId);
		sb.append(tabulador + "tpmtDescripcion : " + (tpmtDescripcion==null? "NULL":tpmtDescripcion));
		sb.append(tabulador + "tpmtEstado : " + (tpmtEstado==null? "NULL":tpmtEstado));
		
		
    
		return sb.toString();
    }

	
	

	
}
