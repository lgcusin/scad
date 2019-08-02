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
 * Clase (DTO) AulaDto. DTO encargado de manejar los datos de la entidad
 * AulaDto.
 * 
 * @author lmquishpei.
 * @version 1.0
 */
public class AulaDto implements Serializable {

	private static final long serialVersionUID = -3663263502299711591L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Aula
	private int alaId;
	private String alaCodigo;
	private String alaDescripcion;
	private Integer alaTipo;
	private Integer alaCapacidad;
	private Integer alaPiso;
	private Integer alaEstado;

	// Tabla Paralelo
	private Integer prlId;
	private String prlCodigo;
	private String prlDescripcion;
	private Integer prlEstado;
	private Integer prlEdificio;

	// Tabla Edificio
	private Integer edfId;
	private String edfCodigo;
	private String edfDescripcion;
	private Integer edfEstado;

	// Tabla Dependencia
	private Integer dpnId;
	private Integer dpnSubId;
	private String dpnDescripcion;

	// Tabla Horario Academico
	private Integer hracId;
	private Integer hracDia;
	private Integer hracEstado;

	// Tabla MallaCurricularParalelo
	private Integer mlcrprId;
	private Integer mlcrprCupo;
	private Integer mlcrprInscritos;
	private Integer mlcrprParaleloId;
	private Integer mlcrprMallaCurricularMateriaId;

	public AulaDto() {

	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getAlaId() {
		return alaId;
	}

	public void setAlaId(int alaId) {
		this.alaId = alaId;
	}

	public String getAlaCodigo() {
		return alaCodigo;
	}

	public void setAlaCodigo(String alaCodigo) {
		this.alaCodigo = alaCodigo;
	}

	public String getAlaDescripcion() {
		return alaDescripcion;
	}

	public void setAlaDescripcion(String alaDescripcion) {
		this.alaDescripcion = alaDescripcion;
	}

	public Integer getAlaTipo() {
		return alaTipo;
	}

	public void setAlaTipo(Integer alaTipo) {
		this.alaTipo = alaTipo;
	}

	public Integer getAlaCapacidad() {
		return alaCapacidad;
	}

	public void setAlaCapacidad(Integer alaCapacidad) {
		this.alaCapacidad = alaCapacidad;
	}

	public Integer getAlaPiso() {
		return alaPiso;
	}

	public void setAlaPiso(Integer alaPiso) {
		this.alaPiso = alaPiso;
	}

	public Integer getPrlEdificio() {
		return prlEdificio;
	}

	public void setPrlEdificio(Integer prlEdificio) {
		this.prlEdificio = prlEdificio;
	}

	public Integer getEdfId() {
		return edfId;
	}

	public void setEdfId(Integer edfId) {
		this.edfId = edfId;
	}

	public String getEdfDescripcion() {
		return edfDescripcion;
	}

	public void setEdfDescripcion(String edfDescripcion) {
		this.edfDescripcion = edfDescripcion;
	}

	public Integer getDpnId() {
		return dpnId;
	}

	public void setDpnId(Integer dpnId) {
		this.dpnId = dpnId;
	}

	public Integer getDpnSubId() {
		return dpnSubId;
	}

	public void setDpnSubId(Integer dpnSubId) {
		this.dpnSubId = dpnSubId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Integer getAlaEstado() {
		return alaEstado;
	}

	public void setAlaEstado(Integer alaEstado) {
		this.alaEstado = alaEstado;
	}

	public String getEdfCodigo() {
		return edfCodigo;
	}

	public void setEdfCodigo(String edfCodigo) {
		this.edfCodigo = edfCodigo;
	}

	public Integer getEdfEstado() {
		return edfEstado;
	}

	public void setEdfEstado(Integer edfEstado) {
		this.edfEstado = edfEstado;
	}

	public Integer getPrlId() {
		return prlId;
	}

	public void setPrlId(Integer prlId) {
		this.prlId = prlId;
	}

	public String getPrlCodigo() {
		return prlCodigo;
	}

	public void setPrlCodigo(String prlCodigo) {
		this.prlCodigo = prlCodigo;
	}

	public String getPrlDescripcion() {
		return prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
	}

	public Integer getPrlEstado() {
		return prlEstado;
	}

	public void setPrlEstado(Integer prlEstado) {
		this.prlEstado = prlEstado;
	}

	public Integer getHracId() {
		return hracId;
	}

	public void setHracId(Integer hracId) {
		this.hracId = hracId;
	}

	public Integer getHracDia() {
		return hracDia;
	}

	public void setHracDia(Integer hracDia) {
		this.hracDia = hracDia;
	}

	public Integer getHracEstado() {
		return hracEstado;
	}

	public void setHracEstado(Integer hracEstado) {
		this.hracEstado = hracEstado;
	}

	public Integer getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(Integer mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public Integer getMlcrprCupo() {
		return mlcrprCupo;
	}

	public void setMlcrprCupo(Integer mlcrprCupo) {
		this.mlcrprCupo = mlcrprCupo;
	}

	public Integer getMlcrprInscritos() {
		return mlcrprInscritos;
	}

	public void setMlcrprInscritos(Integer mlcrprInscritos) {
		this.mlcrprInscritos = mlcrprInscritos;
	}

	public Integer getMlcrprParaleloId() {
		return mlcrprParaleloId;
	}

	public void setMlcrprParaleloId(Integer mlcrprParaleloId) {
		this.mlcrprParaleloId = mlcrprParaleloId;
	}

	public Integer getMlcrprMallaCurricularMateriaId() {
		return mlcrprMallaCurricularMateriaId;
	}

	public void setMlcrprMallaCurricularMateriaId(Integer mlcrprMallaCurricularMateriaId) {
		this.mlcrprMallaCurricularMateriaId = mlcrprMallaCurricularMateriaId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();

		sb.append(" Aula ");
		sb.append(tabulador + "alaId : " + alaId);
		sb.append(tabulador + "alaCodigo : " + (alaCodigo == null ? "NULL" : alaCodigo));
		sb.append(tabulador + "alaDescripcion : " + (alaDescripcion == null ? "NULL" : alaDescripcion));
		sb.append(tabulador + "alaEstado : " + (alaEstado == null ? "NULL" : alaEstado));
		sb.append(tabulador + "alaTipo : " + (alaTipo == null ? "NULL" : alaTipo));
		sb.append(tabulador + "alaCapacidad : " + (alaCapacidad == null ? "NULL" : alaCapacidad));
		sb.append(tabulador + "alaPiso : " + (alaPiso == null ? "NULL" : alaPiso));

		sb.append(tabulador + "prlEdificio : " + (prlEdificio == null ? "NULL" : prlEdificio));

		sb.append(" Edificio ");
		sb.append(tabulador + "edfId : " + edfId);
		sb.append(tabulador + "edfDescripcion : " + (edfDescripcion == null ? "NULL" : edfDescripcion));

		sb.append(" Dependencia");
		sb.append(tabulador + "dpnId : " + dpnId);
		sb.append(tabulador + "dpnDeSubId : " + (dpnSubId == null ? "NULL" : dpnSubId));
		sb.append(tabulador + "dpnDescricion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));

		return sb.toString();
	}

}
