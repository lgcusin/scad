package ec.edu.uce.academico.ejb.dtos;

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
   
 ARCHIVO:     DetalleMatriculaDto.java	  
 DESCRIPCION: Dto que almacena los datos necesarios para generar el archivo CSV. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-08-2018		   		Freddy Guzman 					Emisión Inicial
 ***************************************************************************/
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Clase (Entity Bean) DetalleMatriculaDto. Dto que almacena los datos
 * necesarios del Detalle Matricula
 * 
 * @author fgguzman.
 * @version 1.0
 */
public class DetalleMatriculaDto implements java.io.Serializable {

	private static final long serialVersionUID = -1828910417871903627L;
	private int dtmtId;
	private Integer dtmtNumero;
	private Integer dtmtEstado;
	private String dtmtArchivoEstudiantes;
	private Integer dtmtEstadoHistorico;
	private Integer dtmtEstadoCambio;
	private String dtmtObservacionHistorico;
	private String dtmtObservacionCambio;
	private BigDecimal dtmtValorParcial;
	private BigDecimal dtmtValorPorMateria;
	private Timestamp dtmtFechaHistorico;
	private Timestamp dtmtFechaCambio;
	private String dtmtUsuario;
	private Timestamp dtmtModificacion;

	private MallaCurricularParaleloDto dtmtMallaCurricularParaleloDto;

	public int getDtmtId() {
		return dtmtId;
	}

	public void setDtmtId(int dtmtId) {
		this.dtmtId = dtmtId;
	}

	public Integer getDtmtNumero() {
		return dtmtNumero;
	}

	public void setDtmtNumero(Integer dtmtNumero) {
		this.dtmtNumero = dtmtNumero;
	}

	public Integer getDtmtEstado() {
		return dtmtEstado;
	}

	public void setDtmtEstado(Integer dtmtEstado) {
		this.dtmtEstado = dtmtEstado;
	}

	public String getDtmtArchivoEstudiantes() {
		return dtmtArchivoEstudiantes;
	}

	public void setDtmtArchivoEstudiantes(String dtmtArchivoEstudiantes) {
		this.dtmtArchivoEstudiantes = dtmtArchivoEstudiantes;
	}

	public Integer getDtmtEstadoHistorico() {
		return dtmtEstadoHistorico;
	}

	public void setDtmtEstadoHistorico(Integer dtmtEstadoHistorico) {
		this.dtmtEstadoHistorico = dtmtEstadoHistorico;
	}

	public Integer getDtmtEstadoCambio() {
		return dtmtEstadoCambio;
	}

	public void setDtmtEstadoCambio(Integer dtmtEstadoCambio) {
		this.dtmtEstadoCambio = dtmtEstadoCambio;
	}

	public String getDtmtObservacionHistorico() {
		return dtmtObservacionHistorico;
	}

	public void setDtmtObservacionHistorico(String dtmtObservacionHistorico) {
		this.dtmtObservacionHistorico = dtmtObservacionHistorico;
	}

	public String getDtmtObservacionCambio() {
		return dtmtObservacionCambio;
	}

	public void setDtmtObservacionCambio(String dtmtObservacionCambio) {
		this.dtmtObservacionCambio = dtmtObservacionCambio;
	}

	public BigDecimal getDtmtValorParcial() {
		return dtmtValorParcial;
	}

	public void setDtmtValorParcial(BigDecimal dtmtValorParcial) {
		this.dtmtValorParcial = dtmtValorParcial;
	}

	public BigDecimal getDtmtValorPorMateria() {
		return dtmtValorPorMateria;
	}

	public void setDtmtValorPorMateria(BigDecimal dtmtValorPorMateria) {
		this.dtmtValorPorMateria = dtmtValorPorMateria;
	}

	public Timestamp getDtmtFechaHistorico() {
		return dtmtFechaHistorico;
	}

	public void setDtmtFechaHistorico(Timestamp dtmtFechaHistorico) {
		this.dtmtFechaHistorico = dtmtFechaHistorico;
	}

	public Timestamp getDtmtFechaCambio() {
		return dtmtFechaCambio;
	}

	public void setDtmtFechaCambio(Timestamp dtmtFechaCambio) {
		this.dtmtFechaCambio = dtmtFechaCambio;
	}

	public String getDtmtUsuario() {
		return dtmtUsuario;
	}

	public void setDtmtUsuario(String dtmtUsuario) {
		this.dtmtUsuario = dtmtUsuario;
	}

	public Timestamp getDtmtModificacion() {
		return dtmtModificacion;
	}

	public void setDtmtModificacion(Timestamp dtmtModificacion) {
		this.dtmtModificacion = dtmtModificacion;
	}

	public MallaCurricularParaleloDto getDtmtMallaCurricularParaleloDto() {
		return dtmtMallaCurricularParaleloDto;
	}

	public void setDtmtMallaCurricularParaleloDto(MallaCurricularParaleloDto dtmtMallaCurricularParaleloDto) {
		this.dtmtMallaCurricularParaleloDto = dtmtMallaCurricularParaleloDto;
	}

}
