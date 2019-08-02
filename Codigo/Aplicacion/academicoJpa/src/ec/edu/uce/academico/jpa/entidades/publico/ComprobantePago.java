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
   
 ARCHIVO:     ComprobantePago.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Comprobante_pago de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01/Septiembre/2017		 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.jpa.entidades.publico;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

/**
 * Clase (Entity Bean) Etnia.
 * Entity Bean que representa a la tabla Comprobante_pago de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "COMPROBANTE_PAGO", schema = ConstantesJpa.SCHEMA)
public class ComprobantePago implements java.io.Serializable {

	private static final long serialVersionUID = -5088029760306617729L;
	
	private int cmpaId;
	private String cmpaCodigo;
	private Integer cmpaValorTotal;
	private Date cmpaFechaCaducidad;
	private Integer cmpaTipo;
	private Date cmpaFechaPago;
	private List<DetalleMatricula> cmpaDetalleMatriculas = new ArrayList<DetalleMatricula>(0);
	private Timestamp cmpaFechaEmision;

	private String cmpaNumComprobante;
	private Integer cmpaNumCompSecuencial;
	private String cmpaDescripcion;
	private BigDecimal cmpaTotalPago;
	private Integer cmpaEstado;
	private FichaMatricula cmpaFichaMatricula;
	private BigDecimal cmpaTotalFacultad;
	private Integer cmpaProcSau;
	private Integer cmpaTipoUnidad;
	private BigDecimal cmpaValorPagado;
	private Integer cmpaCantidad;
	private Integer cmpaIdArancel;
	private Integer cmpaPaiCodigo;
	private Integer cmpaAplicaGratuidad;
	private Integer cmpaMatrTipo;
	private Integer cmpaModalidad;
	private Integer cmpaEspeCodigo;
	private Timestamp cmpaFechaCaduca; 
	private Timestamp cmpaFechaEnvio; 
	private Integer cmpaEstadoPago;
	
	public ComprobantePago() {
	}

	public ComprobantePago(int cmpaId) {
		this.cmpaId = cmpaId;
	}

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Basic(optional = false)
	@Column(name = "CMPA_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getCmpaId() {
		return this.cmpaId;
	}

	public void setCmpaId(int cmpaId) {
		this.cmpaId = cmpaId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCMT_ID")
	public FichaMatricula getCmpaFichaMatricula() {
		return this.cmpaFichaMatricula;
	}

	public void setCmpaFichaMatricula(FichaMatricula cmpaFichaMatricula) {
		this.cmpaFichaMatricula = cmpaFichaMatricula;
	}

	@Column(name = "CMPA_CODIGO", length = 128)
	@Length(max = 128)
	public String getCmpaCodigo() {
		return this.cmpaCodigo;
	}

	public void setCmpaCodigo(String cmpaCodigo) {
		this.cmpaCodigo = cmpaCodigo;
	}

	@Column(name = "CMPA_ESTADO", precision = 2, scale = 0)
	public Integer getCmpaEstado() {
		return this.cmpaEstado;
	}

	public void setCmpaEstado(Integer cmpaEstado) {
		this.cmpaEstado = cmpaEstado;
	}

	@Column(name = "CMPA_VALOR_TOTAL", precision = 2)
	public Integer getCmpaValorTotal() {
		return this.cmpaValorTotal;
	}

	public void setCmpaValorTotal(Integer cmpaValorTotal) {
		this.cmpaValorTotal = cmpaValorTotal;
	}

	@Column(name = "CMPA_FECHA_EMISION")
	public Timestamp getCmpaFechaEmision() {
		return this.cmpaFechaEmision;
	}

	public void setCmpaFechaEmision(Timestamp cmpaFechaEmision) {
		this.cmpaFechaEmision = cmpaFechaEmision;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CMPA_FECHA_CADUCIDAD", length = 7)
	public Date getCmpaFechaCaducidad() {
		return this.cmpaFechaCaducidad;
	}

	public void setCmpaFechaCaducidad(Date cmpaFechaCaducidad) {
		this.cmpaFechaCaducidad = cmpaFechaCaducidad;
	}

	@Column(name = "CMPA_TIPO", precision = 2, scale = 0)
	public Integer getCmpaTipo() {
		return this.cmpaTipo;
	}

	public void setCmpaTipo(Integer cmpaTipo) {
		this.cmpaTipo = cmpaTipo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CMPA_FECHA_PAGO", length = 7)
	public Date getCmpaFechaPago() {
		return this.cmpaFechaPago;
	}

	public void setCmpaFechaPago(Date cmpaFechaPago) {
		this.cmpaFechaPago = cmpaFechaPago;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dtmtComprobantePago")
	public List<DetalleMatricula> getCmpaDetalleMatriculas() {
		return this.cmpaDetalleMatriculas;
	}

	public void setCmpaDetalleMatriculas(List<DetalleMatricula> cmpaDetalleMatriculas) {
		this.cmpaDetalleMatriculas = cmpaDetalleMatriculas;
	}

	@Column(name = "cmpa_num_comprobante", length = 256)
	@Length(max = 256)
	public String getCmpaNumComprobante() {
		return cmpaNumComprobante;
	}

	public void setCmpaNumComprobante(String cmpaNumComprobante) {
		this.cmpaNumComprobante = cmpaNumComprobante;
	}

	@Column(name = "cmpa_num_comp_secuencial")
	public Integer getCmpaNumCompSecuencial() {
		return cmpaNumCompSecuencial;
	}

	public void setCmpaNumCompSecuencial(Integer cmpaNumCompSecuencial) {
		this.cmpaNumCompSecuencial = cmpaNumCompSecuencial;
	}

	@Column(name = "cmpa_descripcion", length = 128)
	@Length(max = 128)
	public String getCmpaDescripcion() {
		return cmpaDescripcion;
	}

	public void setCmpaDescripcion(String cmpaDescripcion) {
		this.cmpaDescripcion = cmpaDescripcion;
	}

	@Column(name = "cmpa_total_pago", precision = 2)
	public BigDecimal getCmpaTotalPago() {
		return cmpaTotalPago;
	}

	public void setCmpaTotalPago(BigDecimal cmpaTotalPago) {
		this.cmpaTotalPago = cmpaTotalPago;
	}

	@Column(name = "cmpa_total_facultad", precision = 2)
	public BigDecimal getCmpaTotalFacultad() {
		return cmpaTotalFacultad;
	}

	public void setCmpaTotalFacultad(BigDecimal cmpaTotalFacultad) {
		this.cmpaTotalFacultad = cmpaTotalFacultad;
	}

	@Column(name = "cmpa_proc_sau")
	public Integer getCmpaProcSau() {
		return cmpaProcSau;
	}

	public void setCmpaProcSau(Integer cmpaProcSau) {
		this.cmpaProcSau = cmpaProcSau;
	}

	@Column(name = "cmpa_tipo_unidad")
	public Integer getCmpaTipoUnidad() {
		return cmpaTipoUnidad;
	}

	public void setCmpaTipoUnidad(Integer cmpaTipoUnidad) {
		this.cmpaTipoUnidad = cmpaTipoUnidad;
	}

	@Column(name = "cmpa_valor_pagado", precision = 2)
	public BigDecimal getCmpaValorPagado() {
		return cmpaValorPagado;
	}

	public void setCmpaValorPagado(BigDecimal cmpaValorPagado) {
		this.cmpaValorPagado = cmpaValorPagado;
	}

	@Column(name = "cmpa_cantidad")
	public Integer getCmpaCantidad() {
		return cmpaCantidad;
	}

	public void setCmpaCantidad(Integer cmpaCantidad) {
		this.cmpaCantidad = cmpaCantidad;
	}

	@Column(name = "cmpa_id_arancel")
	public Integer getCmpaIdArancel() {
		return cmpaIdArancel;
	}

	public void setCmpaIdArancel(Integer cmpaIdArancel) {
		this.cmpaIdArancel = cmpaIdArancel;
	}

	@Column(name = "cmpa_pai_codigo")
	public Integer getCmpaPaiCodigo() {
		return cmpaPaiCodigo;
	}

	public void setCmpaPaiCodigo(Integer cmpaPaiCodigo) {
		this.cmpaPaiCodigo = cmpaPaiCodigo;
	}

	@Column(name = "cmpa_aplica_gratuidad")
	public Integer getCmpaAplicaGratuidad() {
		return cmpaAplicaGratuidad;
	}

	public void setCmpaAplicaGratuidad(Integer cmpaAplicaGratuidad) {
		this.cmpaAplicaGratuidad = cmpaAplicaGratuidad;
	}

	@Column(name = "cmpa_matr_tipo")
	public Integer getCmpaMatrTipo() {
		return cmpaMatrTipo;
	}

	public void setCmpaMatrTipo(Integer cmpaMatrTipo) {
		this.cmpaMatrTipo = cmpaMatrTipo;
	}

	@Column(name = "cmpa_modalidad")
	public Integer getCmpaModalidad() {
		return cmpaModalidad;
	}

	public void setCmpaModalidad(Integer cmpaModalidad) {
		this.cmpaModalidad = cmpaModalidad;
	}

	@Column(name = "cmpa_espe_codigo")
	public Integer getCmpaEspeCodigo() {
		return cmpaEspeCodigo;
	}

	public void setCmpaEspeCodigo(Integer cmpaEspeCodigo) {
		this.cmpaEspeCodigo = cmpaEspeCodigo;
	}

	@Column(name = "cmpa_fecha_caduca", length = 29)
	public Timestamp getCmpaFechaCaduca() {
		return cmpaFechaCaduca;
	}

	public void setCmpaFechaCaduca(Timestamp cmpaFechaCaduca) {
		this.cmpaFechaCaduca = cmpaFechaCaduca;
	}
	
	@Column(name = "cmpa_fecha_envio", length = 29)
	public Timestamp getCmpaFechaEnvio() {
		return cmpaFechaEnvio;
	}

	public void setCmpaFechaEnvio(Timestamp cmpaFechaEnvio) {
		this.cmpaFechaEnvio = cmpaFechaEnvio;
	}
	
	@Column(name = "cmpa_estado_pago")
	public Integer getCmpaEstadoPago() {
		return cmpaEstadoPago;
	}

	public void setCmpaEstadoPago(Integer cmpaEstadoPago) {
		this.cmpaEstadoPago = cmpaEstadoPago;
	}
	
	
	
}
