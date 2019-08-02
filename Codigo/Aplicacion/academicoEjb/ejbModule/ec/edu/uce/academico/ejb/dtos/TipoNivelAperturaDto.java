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
   
 ARCHIVO:     TipoNivelAperturaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad TipoNivelAperturaDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26-06-2017		  Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * Clase (DTO) TipoNivelAperturaDto.
 * DTO encargado de manejar los datos de la entidad TipoNivelAperturaDto. 
 * @author lmquishpei.
 * @version 1.0
 */
public class TipoNivelAperturaDto implements Serializable{

	private static final long serialVersionUID = -3663263502299711591L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	// Tabla Tipo_Nivel_Apertura
	private int tinvapId;
	private Timestamp tinvapFechaInicio;
	private Timestamp tinvapFechaFin;
	private Integer tinvapEstado;
	private String tinvapObservacion;
	
		
	//Tabla periodo_Academico
	private Integer pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	
	//Tabla Cronograma
	
	private int crnId;
	private Integer crnTipo;
	private String crnDescripcion;
	private Integer crnEstado;
  
	
	// Tabla Cronograma_Proceso_Flujo
	private int crprflId;
	
	
	//Tabla Proceso_Flujo	
	private int prflId;
	private String prflDescripcion;
	private Integer prflEstado;
	
	
	//Tabla Planificacion_Cronograma
	
	private int plcrId;
	private Integer plcrEstado;
	private Timestamp plcrFechaInicio;
	private Timestamp plcrFechaFin;

	
	//Tabla Nivel_Apertura
	private int nvapId;
	private String nvapDescripcion;
 
	
	
	//Tabla Tipo_Apertura
	private int tiapId;
	private String tiapDescripcion;
	private Integer tiapEstado;
	

	
	
	public TipoNivelAperturaDto() {
		
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/
	
	
	

	
	
	public Integer getPracId() {
		return pracId;
	}

	public void setPracId(Integer pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

		
	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}


		
	

	public int getTinvapId() {
		return tinvapId;
	}

	public void setTinvapId(int tinvapId) {
		this.tinvapId = tinvapId;
	}

	public Timestamp getTinvapFechaInicio() {
		return tinvapFechaInicio;
	}

	public void setTinvapFechaInicio(Timestamp tinvapFechaInicio) {
		this.tinvapFechaInicio = tinvapFechaInicio;
	}

	public Timestamp getTinvapFechaFin() {
		return tinvapFechaFin;
	}

	public void setTinvapFechaFin(Timestamp tinvapFechaFin) {
		this.tinvapFechaFin = tinvapFechaFin;
	}

	public Integer getTinvapEstado() {
		return tinvapEstado;
	}

	public void setTinvapEstado(Integer tinvapEstado) {
		this.tinvapEstado = tinvapEstado;
	}

	public String getTinvapObservacion() {
		return tinvapObservacion;
	}

	public void setTinvapObservacion(String tinvapObservacion) {
		this.tinvapObservacion = tinvapObservacion;
	}

	public int getCrnId() {
		return crnId;
	}

	public void setCrnId(int crnId) {
		this.crnId = crnId;
	}

	public Integer getCrnTipo() {
		return crnTipo;
	}

	public void setCrnTipo(Integer crnTipo) {
		this.crnTipo = crnTipo;
	}

	public String getCrnDescripcion() {
		return crnDescripcion;
	}

	public void setCrnDescripcion(String crnDescripcion) {
		this.crnDescripcion = crnDescripcion;
	}

	public Integer getCrnEstado() {
		return crnEstado;
	}

	public void setCrnEstado(Integer crnEstado) {
		this.crnEstado = crnEstado;
	}

	public int getCrprflId() {
		return crprflId;
	}

	public void setCrprflId(int crprflId) {
		this.crprflId = crprflId;
	}

	public int getPrflId() {
		return prflId;
	}

	public void setPrflId(int prflId) {
		this.prflId = prflId;
	}

	public String getPrflDescripcion() {
		return prflDescripcion;
	}

	public void setPrflDescripcion(String prflDescripcion) {
		this.prflDescripcion = prflDescripcion;
	}

	public Integer getPrflEstado() {
		return prflEstado;
	}

	public void setPrflEstado(Integer prflEstado) {
		this.prflEstado = prflEstado;
	}

	public int getPlcrId() {
		return plcrId;
	}

	public void setPlcrId(int plcrId) {
		this.plcrId = plcrId;
	}

	
	public Integer getPlcrEstado() {
		return plcrEstado;
	}

	public void setPlcrEstado(Integer plcrEstado) {
		this.plcrEstado = plcrEstado;
	}

	public Timestamp getPlcrFechaInicio() {
		return plcrFechaInicio;
	}

	public void setPlcrFechaInicio(Timestamp plcrFechaInicio) {
		this.plcrFechaInicio = plcrFechaInicio;
	}

	public Timestamp getPlcrFechaFin() {
		return plcrFechaFin;
	}

	public void setPlcrFechaFin(Timestamp plcrFechaFin) {
		this.plcrFechaFin = plcrFechaFin;
	}

	public int getNvapId() {
		return nvapId;
	}

	public void setNvapId(int nvapId) {
		this.nvapId = nvapId;
	}

	public String getNvapDescripcion() {
		return nvapDescripcion;
	}

	public void setNvapDescripcion(String nvapDescripcion) {
		this.nvapDescripcion = nvapDescripcion;
	}

	public int getTiapId() {
		return tiapId;
	}

	public void setTiapId(int tiapId) {
		this.tiapId = tiapId;
	}

	public String getTiapDescripcion() {
		return tiapDescripcion;
	}

	public void setTiapDescripcion(String tiapDescripcion) {
		this.tiapDescripcion = tiapDescripcion;
	}

	public Integer getTiapEstado() {
		return tiapEstado;
	}

	public void setTiapEstado(Integer tiapEstado) {
		this.tiapEstado = tiapEstado;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		
		
		
		sb.append(" Tipo Nivel Apertura " );
		sb.append(tabulador + "tinvapId : " + tinvapId);
		sb.append(tabulador + "tinvapFechaInicio : " + (tinvapFechaInicio==null? "NULL":tinvapFechaInicio));
		sb.append(tabulador + "tinvapFechaFin : " + (tinvapFechaFin==null? "NULL":tinvapFechaFin));
		sb.append(tabulador + "tinvapEstado : " + (tinvapEstado==null? "NULL":tinvapEstado));
		sb.append(tabulador + "tinvapObservacion : " + (tinvapObservacion==null? "NULL":tinvapObservacion));
		
		sb.append(" PeriodoAcademico" );
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion==null? "NULL":pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado==null? "NULL":pracEstado));
		
		
		
		sb.append(" Cronograma " );
		sb.append(tabulador + "crnId : " + crnId);		
		sb.append(tabulador + "crnTipo : " + (crnTipo==null? "NULL":crnTipo));
		sb.append(tabulador + "crnDescripcion : " + (crnDescripcion==null? "NULL":crnDescripcion));
		sb.append(tabulador + "crnEstado : " + (crnEstado==null? "NULL":crnEstado));
		
			
		sb.append(" CronogramaProcesoFlujo " );
		sb.append(tabulador + "crprflId : " + crprflId);
		

		sb.append(" ProcesoFlujo " );
		sb.append(tabulador + "prflId : " + prflId);
		sb.append(tabulador + "prflDescripcion : " + (prflDescripcion==null? "NULL":prflDescripcion));
		sb.append(tabulador + "prflEstado : " + (prflEstado==null? "NULL":prflEstado));
		
				
		sb.append(" PlanificacionCronograma" );
		sb.append(tabulador + "plcrId : " + plcrId);
		sb.append(tabulador + "plcrEstado : " + (plcrEstado==null? "NULL":plcrEstado));
		sb.append(tabulador + "plcrFechaInicio : " + (plcrFechaInicio==null? "NULL":plcrFechaInicio));
		sb.append(tabulador + "plcrFechaFin : " + (plcrFechaFin==null? "NULL":plcrFechaFin));
		
				
		
		sb.append(" NivelApertura" );
		sb.append(tabulador + "nvapId : " + nvapId);
		sb.append(tabulador + "nvapDescripcion : " + (nvapDescripcion==null? "NULL":nvapDescripcion));
		
		sb.append(" TipoApertura" );
		sb.append(tabulador + "tiapId : " + tiapId);
		sb.append(tabulador + "tiapDescripcion : " + (tiapDescripcion==null? "NULL":tiapDescripcion));
		sb.append(tabulador + "tiapEstado : " + (tiapEstado==null? "NULL":tiapEstado));
    
		return sb.toString();
    }	

}
