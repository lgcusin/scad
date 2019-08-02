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
   
 ARCHIVO:     ComprobantePagoDto.java	  
 DESCRIPCION: Dto que almacena los datos necesarios para generar el ComprobantePagoDto. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-08-2018		   		Freddy Guzman 					Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import ec.edu.uce.academico.jpa.entidades.publico.DetalleMatricula;

/**
 * Clase (Entity Bean) ComprobantePagoDto. Dto que almacena los datos necesarios
 * del Comprobante de Pago
 * 
 * @author fgguzman.
 * @version 1.0
 */
public class ComprobantePagoDto implements Serializable {

	private static final long serialVersionUID = -4273730586280953983L;
	private int cmpaId;
	private String cmpaCodigo;
	private String cmpaNumero;
	private String cmpaObservacion;
	private Integer cmpaTipo;
	private Integer cmpaNumCompSecuencial;
	private Integer cmpaEstadoValue;
	private Integer cmpaTipoUnidad;
	private Integer cmpaCantidad;
	private Integer cmpaIdArancel;
	private Integer cmpaPaiCodigo;
	private Integer cmpaTipoMatricula;
	private Integer cmpaModalidad;
	private Integer cmpaEspeCodigo;
	private String cmpaEstadoLabel;
	private Boolean cmpaAcceso;
	private BigDecimal cmpaTotalAPagar;
	private BigDecimal cmpaTotalMatricula;
	private BigDecimal cmpaTotalCobrado;
	private Timestamp cmpaFechaPago;
	private Timestamp cmpaFechaEmision;
	private Timestamp cmpaFechaCaducidad;
	private Timestamp cmpaFechaEnvio;

	private PersonaDto cmpaEstudianteDto;
	private DependenciaDto cmpaDependenciaDto;
	private CarreraDto cmpaCarreraDto;
	private FichaMatriculaDto cmpaFichaMatriculaDto;
	private ArancelDto cmpaArancelDto;

	private List<DetalleMatricula> cmpaListDetalleMatriculaDto;

	public int getCmpaId() {
		return cmpaId;
	}

	public void setCmpaId(int cmpaId) {
		this.cmpaId = cmpaId;
	}

	public String getCmpaCodigo() {
		return cmpaCodigo;
	}

	public void setCmpaCodigo(String cmpaCodigo) {
		this.cmpaCodigo = cmpaCodigo;
	}

	public String getCmpaNumero() {
		return cmpaNumero;
	}

	public void setCmpaNumero(String cmpaNumero) {
		this.cmpaNumero = cmpaNumero;
	}

	public String getCmpaObservacion() {
		return cmpaObservacion;
	}

	public void setCmpaObservacion(String cmpaObservacion) {
		this.cmpaObservacion = cmpaObservacion;
	}

	public Integer getCmpaTipo() {
		return cmpaTipo;
	}

	public void setCmpaTipo(Integer cmpaTipo) {
		this.cmpaTipo = cmpaTipo;
	}

	public Integer getCmpaNumCompSecuencial() {
		return cmpaNumCompSecuencial;
	}

	public void setCmpaNumCompSecuencial(Integer cmpaNumCompSecuencial) {
		this.cmpaNumCompSecuencial = cmpaNumCompSecuencial;
	}

	public Integer getCmpaTipoUnidad() {
		return cmpaTipoUnidad;
	}

	public void setCmpaTipoUnidad(Integer cmpaTipoUnidad) {
		this.cmpaTipoUnidad = cmpaTipoUnidad;
	}

	public Integer getCmpaCantidad() {
		return cmpaCantidad;
	}

	public void setCmpaCantidad(Integer cmpaCantidad) {
		this.cmpaCantidad = cmpaCantidad;
	}

	public Integer getCmpaIdArancel() {
		return cmpaIdArancel;
	}

	public void setCmpaIdArancel(Integer cmpaIdArancel) {
		this.cmpaIdArancel = cmpaIdArancel;
	}

	public Integer getCmpaPaiCodigo() {
		return cmpaPaiCodigo;
	}

	public void setCmpaPaiCodigo(Integer cmpaPaiCodigo) {
		this.cmpaPaiCodigo = cmpaPaiCodigo;
	}

	public Integer getCmpaTipoMatricula() {
		return cmpaTipoMatricula;
	}

	public void setCmpaTipoMatricula(Integer cmpaTipoMatricula) {
		this.cmpaTipoMatricula = cmpaTipoMatricula;
	}

	public Integer getCmpaModalidad() {
		return cmpaModalidad;
	}

	public void setCmpaModalidad(Integer cmpaModalidad) {
		this.cmpaModalidad = cmpaModalidad;
	}

	public Integer getCmpaEspeCodigo() {
		return cmpaEspeCodigo;
	}

	public void setCmpaEspeCodigo(Integer cmpaEspeCodigo) {
		this.cmpaEspeCodigo = cmpaEspeCodigo;
	}

	public BigDecimal getCmpaTotalAPagar() {
		return cmpaTotalAPagar;
	}

	public void setCmpaTotalAPagar(BigDecimal cmpaTotalAPagar) {
		this.cmpaTotalAPagar = cmpaTotalAPagar;
	}

	public BigDecimal getCmpaTotalCobrado() {
		return cmpaTotalCobrado;
	}

	public void setCmpaTotalCobrado(BigDecimal cmpaTotalCobrado) {
		this.cmpaTotalCobrado = cmpaTotalCobrado;
	}

	public Timestamp getCmpaFechaPago() {
		return cmpaFechaPago;
	}

	public void setCmpaFechaPago(Timestamp cmpaFechaPago) {
		this.cmpaFechaPago = cmpaFechaPago;
	}

	public Timestamp getCmpaFechaEmision() {
		return cmpaFechaEmision;
	}

	public void setCmpaFechaEmision(Timestamp cmpaFechaEmision) {
		this.cmpaFechaEmision = cmpaFechaEmision;
	}

	public Timestamp getCmpaFechaCaducidad() {
		return cmpaFechaCaducidad;
	}

	public void setCmpaFechaCaducidad(Timestamp cmpaFechaCaducidad) {
		this.cmpaFechaCaducidad = cmpaFechaCaducidad;
	}

	public Timestamp getCmpaFechaEnvio() {
		return cmpaFechaEnvio;
	}

	public void setCmpaFechaEnvio(Timestamp cmpaFechaEnvio) {
		this.cmpaFechaEnvio = cmpaFechaEnvio;
	}

	public PersonaDto getCmpaEstudianteDto() {
		return cmpaEstudianteDto;
	}

	public void setCmpaEstudianteDto(PersonaDto cmpaEstudianteDto) {
		this.cmpaEstudianteDto = cmpaEstudianteDto;
	}

	public FichaMatriculaDto getCmpaFichaMatriculaDto() {
		return cmpaFichaMatriculaDto;
	}

	public void setCmpaFichaMatriculaDto(FichaMatriculaDto cmpaFichaMatriculaDto) {
		this.cmpaFichaMatriculaDto = cmpaFichaMatriculaDto;
	}

	public List<DetalleMatricula> getCmpaListDetalleMatriculaDto() {
		return cmpaListDetalleMatriculaDto;
	}

	public void setCmpaListDetalleMatriculaDto(List<DetalleMatricula> cmpaListDetalleMatriculaDto) {
		this.cmpaListDetalleMatriculaDto = cmpaListDetalleMatriculaDto;
	}

	public DependenciaDto getCmpaDependenciaDto() {
		return cmpaDependenciaDto;
	}

	public void setCmpaDependenciaDto(DependenciaDto cmpaDependenciaDto) {
		this.cmpaDependenciaDto = cmpaDependenciaDto;
	}

	public CarreraDto getCmpaCarreraDto() {
		return cmpaCarreraDto;
	}

	public void setCmpaCarreraDto(CarreraDto cmpaCarreraDto) {
		this.cmpaCarreraDto = cmpaCarreraDto;
	}

	public Integer getCmpaEstadoValue() {
		return cmpaEstadoValue;
	}

	public void setCmpaEstadoValue(Integer cmpaEstadoValue) {
		this.cmpaEstadoValue = cmpaEstadoValue;
	}

	public String getCmpaEstadoLabel() {
		return cmpaEstadoLabel;
	}

	public void setCmpaEstadoLabel(String cmpaEstadoLabel) {
		this.cmpaEstadoLabel = cmpaEstadoLabel;
	}

	public Boolean getCmpaAcceso() {
		return cmpaAcceso;
	}

	public void setCmpaAcceso(Boolean cmpaAcceso) {
		this.cmpaAcceso = cmpaAcceso;
	}

	public BigDecimal getCmpaTotalMatricula() {
		return cmpaTotalMatricula;
	}

	public void setCmpaTotalMatricula(BigDecimal cmpaTotalMatricula) {
		this.cmpaTotalMatricula = cmpaTotalMatricula;
	}

	public ArancelDto getCmpaArancelDto() {
		return cmpaArancelDto;
	}

	public void setCmpaArancelDto(ArancelDto cmpaArancelDto) {
		this.cmpaArancelDto = cmpaArancelDto;
	}

	@Override
	public String toString() {
		return "ComprobantePagoDto [cmpaId=" + cmpaId + ", cmpaCodigo=" + cmpaCodigo + ", cmpaNumero=" + cmpaNumero
				+ ", cmpaObservacion=" + cmpaObservacion + ", cmpaTipo=" + cmpaTipo + ", cmpaNumCompSecuencial="
				+ cmpaNumCompSecuencial + ", cmpaEstadoValue=" + cmpaEstadoValue + ", cmpaTipoUnidad=" + cmpaTipoUnidad
				+ ", cmpaCantidad=" + cmpaCantidad + ", cmpaIdArancel=" + cmpaIdArancel + ", cmpaPaiCodigo="
				+ cmpaPaiCodigo + ", cmpaTipoMatricula=" + cmpaTipoMatricula + ", cmpaModalidad=" + cmpaModalidad
				+ ", cmpaEspeCodigo=" + cmpaEspeCodigo + ", cmpaEstadoLabel=" + cmpaEstadoLabel + ", cmpaAcceso="
				+ cmpaAcceso + ", cmpaTotalAPagar=" + cmpaTotalAPagar + ", cmpaTotalMatricula=" + cmpaTotalMatricula
				+ ", cmpaTotalCobrado=" + cmpaTotalCobrado + ", cmpaFechaPago=" + cmpaFechaPago + ", cmpaFechaEmision="
				+ cmpaFechaEmision + ", cmpaFechaCaducidad=" + cmpaFechaCaducidad + ", cmpaFechaEnvio=" + cmpaFechaEnvio
				+ ", cmpaEstudianteDto=" + cmpaEstudianteDto + ", cmpaDependenciaDto=" + cmpaDependenciaDto
				+ ", cmpaCarreraDto=" + cmpaCarreraDto + ", cmpaFichaMatriculaDto=" + cmpaFichaMatriculaDto
				+ ", cmpaListDetalleMatriculaDto=" + cmpaListDetalleMatriculaDto + "]";
	}

}
