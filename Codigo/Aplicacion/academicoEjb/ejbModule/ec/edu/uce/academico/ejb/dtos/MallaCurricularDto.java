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
   
 ARCHIVO:     MallaCurricularDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad MallaCurricular. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-03-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase (DTO) MallaCurricularDto. DTO encargado de manejar los datos de la
 * entidad MallaCurricular.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class MallaCurricularDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	// Tabla MallaCurricular
	private int mlcrId;
	private Integer mlcrTpfrmlId;
	private Integer mlcrCrrId;
	private String mlcrCodigo;
	private String mlcrDescripcion;
	private Integer mlcrEstado;
	private Date mlcrFechaInicio;
	private Date mlcrFechaFin;
	private Integer mlcrHorasReprobados;
	private Integer mlcrHorasAprobados;
	private Integer mlcrTotalHoras;
	private Integer mlcrCreditosReprobados;
	private Integer mlcrCreditosAprobados;
	private Integer mlcrTotalCreditos;
	private BigDecimal mlcrPorcentajeReprobado;
	private String mlcrObservacion;
	private Integer mlcrTipoOrgAprendizaje;
	private Integer mlcrVigencia;
	private Integer mlcrTotalMaterias;
	private Integer mlcrTipoAprobacion;

	// Tabla Carrera
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrDpnId;
	private Integer crrProceso;

	// Tabla TipoFormacionMalla

	private int tpfrmlId;
	private String tpfrmlDescripcion;
	private Integer tpfrmlEstado;

	// Tabla Dependencia
	private int dpnId;
	private String dpnDescripcion;

	// Tabla PeriodoAcademico
	private int pracId;
	private String pracDescripcion;

	// Tabla MallaCurricularMateria
	private int mlcrmtId;
	private Integer mlcrmtEstado;

	// Tabla Nivel
	private int nvlId;
	private String nvlDescripcion;

	// Tabla UnidadFormacion
	private int unfrId;
	private String unfrDescripcion;

	// Tabla Materia
	private int mtrId;
	private String mtrDescripcion;
	private String mtrCodigo;
	private Integer mtrSubId;
	// Tabla TipoMateria
	private int timtId;
	private String timtDescripcion;

	// Variable para agregar a paralelo
	private boolean mtrAsignada;
	private Integer mlcrprCupo;
	private Integer mlcrprReservaRepetidos;

	public MallaCurricularDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(int mlcrId) {
		this.mlcrId = mlcrId;
	}

	public Integer getMlcrTpfrmlId() {
		return mlcrTpfrmlId;
	}

	public void setMlcrTpfrmlId(Integer mlcrTpfrmlId) {
		this.mlcrTpfrmlId = mlcrTpfrmlId;
	}

	public Integer getMlcrCrrId() {
		return mlcrCrrId;
	}

	public void setMlcrCrrId(Integer mlcrCrrId) {
		this.mlcrCrrId = mlcrCrrId;
	}

	public String getMlcrCodigo() {
		return mlcrCodigo;
	}

	public void setMlcrCodigo(String mlcrCodigo) {
		this.mlcrCodigo = mlcrCodigo;
	}

	public String getMlcrDescripcion() {
		return mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
	}

	public Integer getMlcrEstado() {
		return mlcrEstado;
	}

	public void setMlcrEstado(Integer mlcrEstado) {
		this.mlcrEstado = mlcrEstado;
	}

	public Date getMlcrFechaInicio() {
		return mlcrFechaInicio;
	}

	public void setMlcrFechaInicio(Date mlcrFechaInicio) {
		this.mlcrFechaInicio = mlcrFechaInicio;
	}

	public Date getMlcrFechaFin() {
		return mlcrFechaFin;
	}

	public void setMlcrFechaFin(Date mlcrFechaFin) {
		this.mlcrFechaFin = mlcrFechaFin;
	}

	public Integer getMlcrTotalHoras() {
		return mlcrTotalHoras;
	}

	public void setMlcrTotalHoras(Integer mlcrTotalHoras) {
		this.mlcrTotalHoras = mlcrTotalHoras;
	}

	public Integer getMlcrTotalMaterias() {
		return mlcrTotalMaterias;
	}

	public void setMlcrTotalMaterias(Integer mlcrTotalMaterias) {
		this.mlcrTotalMaterias = mlcrTotalMaterias;
	}

	public Integer getMlcrTipoOrgAprendizaje() {
		return mlcrTipoOrgAprendizaje;
	}

	public void setMlcrTipoOrgAprendizaje(Integer mlcrTipoOrgAprendizaje) {
		this.mlcrTipoOrgAprendizaje = mlcrTipoOrgAprendizaje;
	}

	public Integer getMlcrVigencia() {
		return mlcrVigencia;
	}

	public void setMlcrVigencia(Integer mlcrVigencia) {
		this.mlcrVigencia = mlcrVigencia;
	}

	public Integer getMlcrTipoAprobacion() {
		return mlcrTipoAprobacion;
	}

	public void setMlcrTipoAprobacion(Integer mlcrTipoAprobacion) {
		this.mlcrTipoAprobacion = mlcrTipoAprobacion;
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

	public String getCrrCodSniese() {
		return crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Date getCrrFechaCreacion() {
		return crrFechaCreacion;
	}

	public void setCrrFechaCreacion(Date crrFechaCreacion) {
		this.crrFechaCreacion = crrFechaCreacion;
	}

	public String getCrrResolucion() {
		return crrResolucion;
	}

	public void setCrrResolucion(String crrResolucion) {
		this.crrResolucion = crrResolucion;
	}

	public Integer getCrrDpnId() {
		return crrDpnId;
	}

	public void setCrrDpnId(Integer crrDpnId) {
		this.crrDpnId = crrDpnId;
	}

	public int getTpfrmlId() {
		return tpfrmlId;
	}

	public void setTpfrmlId(int tpfrmlId) {
		this.tpfrmlId = tpfrmlId;
	}

	public String getTpfrmlDescripcion() {
		return tpfrmlDescripcion;
	}

	public void setTpfrmlDescripcion(String tpfrmlDescripcion) {
		this.tpfrmlDescripcion = tpfrmlDescripcion;
	}

	public Integer getTpfrmlEstado() {
		return tpfrmlEstado;
	}

	public void setTpfrmlEstado(Integer tpfrmlEstado) {
		this.tpfrmlEstado = tpfrmlEstado;
	}

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public int getPracId() {
		return pracId;
	}

	public void setPracId(int pracId) {
		this.pracId = pracId;
	}

	public String getPracDescripcion() {
		return pracDescripcion;
	}

	public void setPracDescripcion(String pracDescripcion) {
		this.pracDescripcion = pracDescripcion;
	}

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public Integer getMlcrmtEstado() {
		return mlcrmtEstado;
	}

	public void setMlcrmtEstado(Integer mlcrmtEstado) {
		this.mlcrmtEstado = mlcrmtEstado;
	}

	public int getNvlId() {
		return nvlId;
	}

	public void setNvlId(int nvlId) {
		this.nvlId = nvlId;
	}

	public String getNvlDescripcion() {
		return nvlDescripcion;
	}

	public void setNvlDescripcion(String nvlDescripcion) {
		this.nvlDescripcion = nvlDescripcion;
	}

	public int getUnfrId() {
		return unfrId;
	}

	public void setUnfrId(int unfrId) {
		this.unfrId = unfrId;
	}

	public String getUnfrDescripcion() {
		return unfrDescripcion;
	}

	public void setUnfrDescripcion(String unfrDescripcion) {
		this.unfrDescripcion = unfrDescripcion;
	}

	public int getMtrId() {
		return mtrId;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public int getTimtId() {
		return timtId;
	}

	public void setTimtId(int timtId) {
		this.timtId = timtId;
	}

	public String getTimtDescripcion() {
		return timtDescripcion;
	}

	public void setTimtDescripcion(String timtDescripcion) {
		this.timtDescripcion = timtDescripcion;
	}

	public boolean isMtrAsignada() {
		return mtrAsignada;
	}

	public void setMtrAsignada(boolean mtrAsignada) {
		this.mtrAsignada = mtrAsignada;
	}

	public Integer getMlcrprCupo() {
		return mlcrprCupo;
	}

	public void setMlcrprCupo(Integer mlcrprCupo) {
		this.mlcrprCupo = mlcrprCupo;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public Integer getMlcrprReservaRepetidos() {
		return mlcrprReservaRepetidos;
	}

	public void setMlcrprReservaRepetidos(Integer mlcrprReservaRepetidos) {
		this.mlcrprReservaRepetidos = mlcrprReservaRepetidos;
	}

	public Integer getMtrSubId() {
		return mtrSubId;
	}

	public void setMtrSubId(Integer mtrSubId) {
		this.mtrSubId = mtrSubId;
	}

	public Integer getMlcrCreditosReprobados() {
		return mlcrCreditosReprobados;
	}

	public void setMlcrCreditosReprobados(Integer mlcrCreditosReprobados) {
		this.mlcrCreditosReprobados = mlcrCreditosReprobados;
	}

	public Integer getMlcrTotalCreditos() {
		return mlcrTotalCreditos;
	}

	public void setMlcrTotalCreditos(Integer mlcrTotalCreditos) {
		this.mlcrTotalCreditos = mlcrTotalCreditos;
	}

	public BigDecimal getMlcrPorcentajeReprobado() {
		return mlcrPorcentajeReprobado;
	}

	public void setMlcrPorcentajeReprobado(BigDecimal mlcrPorcentajeReprobado) {
		this.mlcrPorcentajeReprobado = mlcrPorcentajeReprobado;
	}

	public String getMlcrObservacion() {
		return mlcrObservacion;
	}

	public void setMlcrObservacion(String mlcrObservacion) {
		this.mlcrObservacion = mlcrObservacion;
	}

	public Integer getMlcrHorasReprobados() {
		return mlcrHorasReprobados;
	}

	public void setMlcrHorasReprobados(Integer mlcrHorasReprobados) {
		this.mlcrHorasReprobados = mlcrHorasReprobados;
	}

	public Integer getMlcrHorasAprobados() {
		return mlcrHorasAprobados;
	}

	public void setMlcrHorasAprobados(Integer mlcrHorasAprobados) {
		this.mlcrHorasAprobados = mlcrHorasAprobados;
	}

	public Integer getMlcrCreditosAprobados() {
		return mlcrCreditosAprobados;
	}

	public void setMlcrCreditosAprobados(Integer mlcrCreditosAprobados) {
		this.mlcrCreditosAprobados = mlcrCreditosAprobados;
	}

	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("MallaCurricular");
		sb.append("mlcrId : " + mlcrId);
		sb.append(tabulador + "mlcrTpfrmlId : " + (mlcrTpfrmlId == null ? "NULL" : mlcrTpfrmlId));
		sb.append(tabulador + "mlcrCrrId : " + (mlcrCrrId == null ? "NULL" : mlcrCrrId));
		sb.append(tabulador + "mlcrCodigo : " + (mlcrCodigo == null ? "NULL" : mlcrCodigo));
		sb.append(tabulador + "mlcrDescripcion : " + (mlcrDescripcion == null ? "NULL" : mlcrDescripcion));
		sb.append(tabulador + "mlcrEstado : " + (mlcrEstado == null ? "NULL" : mlcrEstado));
		sb.append(tabulador + "mlcrFechaInicio : " + (mlcrFechaInicio == null ? "NULL" : mlcrFechaInicio));
		sb.append(tabulador + "mlcrFechaFin : " + (mlcrFechaFin == null ? "NULL" : mlcrFechaFin));
		sb.append(tabulador + "mlcrTotalHoras : " + (mlcrTotalHoras == null ? "NULL" : mlcrTotalHoras));
		sb.append(tabulador + "mlcrTotalMaterias : " + (mlcrTotalMaterias == null ? "NULL" : mlcrTotalMaterias));
		sb.append(tabulador + "mlcrTipoOrgAprendizaje : "
				+ (mlcrTipoOrgAprendizaje == null ? "NULL" : mlcrTipoOrgAprendizaje));
		sb.append(tabulador + "mlcrVigencia : " + (mlcrVigencia == null ? "NULL" : mlcrVigencia));
		sb.append(tabulador + "mlcrTipoAprobacion : " + (mlcrTipoAprobacion == null ? "NULL" : mlcrTipoAprobacion));

		sb.append("Carrera");
		sb.append("crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrCodSniese : " + (crrCodSniese == null ? "NULL" : crrCodSniese));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrFechaCreacion : " + (crrFechaCreacion == null ? "NULL" : crrFechaCreacion));
		sb.append(tabulador + "crrResolucion : " + (crrResolucion == null ? "NULL" : crrResolucion));
		sb.append(tabulador + "crrDpnId : " + (crrDpnId == null ? "NULL" : crrDpnId));

		sb.append("TipoFormacionMalla");
		sb.append("tpfrmlId : " + tpfrmlId);
		sb.append(tabulador + "tpfrmlDescripcion : " + (tpfrmlDescripcion == null ? "NULL" : tpfrmlDescripcion));
		sb.append(tabulador + "tpfrmlEstado : " + (tpfrmlEstado == null ? "NULL" : tpfrmlEstado));

		sb.append("Dependencia");
		sb.append("dpnId : " + dpnId);
		sb.append(tabulador + "dpnDescripcion : " + (dpnDescripcion == null ? "NULL" : dpnDescripcion));

		sb.append("PeriodoAcademico");
		sb.append("pracId: " + pracId);
		sb.append(tabulador + "pracDescripcion: " + (pracDescripcion == null ? "NULL" : pracDescripcion));

		sb.append("MallaCurricularMateria");
		sb.append("mlcrmtId: " + mlcrmtId);
		sb.append(tabulador + "mlcrmtEstado: " + (mlcrmtEstado == null ? "NULL" : mlcrmtEstado));

		sb.append("Nivel");
		sb.append("nvlId: " + nvlId);
		sb.append(tabulador + "nvlDescripcion: " + (nvlDescripcion == null ? "NULL" : nvlDescripcion));

		sb.append("UnidadFormacion");
		sb.append("unfrId: " + unfrId);
		sb.append(tabulador + "unfrDescripcion: " + (unfrDescripcion == null ? "NULL" : unfrDescripcion));

		sb.append("Materia");
		sb.append("mtrId: " + mtrId);
		sb.append(tabulador + "mtrDescripcion: " + (mtrDescripcion == null ? "NULL" : mtrDescripcion));

		sb.append("TipoMateria");
		sb.append("timtId: " + timtId);
		sb.append(tabulador + "timtDescripcion: " + (timtDescripcion == null ? "NULL" : timtDescripcion));

		return sb.toString();
	}
}
