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
   
 ARCHIVO:     Comprobante.java	  
 DESCRIPCION: Dto que almacena los datos necesarios para generar el archivo CSV. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 29-04-2017		   		Daniel Albuja		          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.dtos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (Entity Bean) Comprobante.
 * Dto que almacena los datos necesarios para generar el archivo CSV
 * @author dalbuja.
 * @version 1.0
 */
public class ComprobanteCSVDto implements Serializable {

	private static final long serialVersionUID = -4273730586280953983L;

	
	// ***************TABLA Comprobante
	private int cmpaId;
	private String cmpaNumComprobante;
	private Integer cmpaNumComprobanteSecuencial;
	private String cmpaDescripcion;
	private BigDecimal cmpaTotalPago;
	private Integer cmpaEstado;
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
	private Timestamp cmpaFechaEmision;
	private Integer cmpaFichaMatricula;
	
	// ***************TABLA Persona
	private int prsId;
	private Integer prsTipoIdentificacion;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private String prsCelular;
	private Date prsFechaNacimiento;
	private Integer prsSexo;
	private Integer prsSexoSniese;
	private String prsSegundoApellido;
	private Integer prsEtnia;
	private Integer prsUbicacionNacionalidad;
	private String prsDireccion;
	
	// ***************TABLA Carrera
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private Integer crrNivel;
	private String crrDetalle;
	private Integer crrFacultad;
	
	// ***************TABLA Facultad
	private int fclId;
	private String fclDescripcion;
	private String fclCodSori;
	private String fclUej;
	
	private String fechaEmision;
	private String fechaCaduca;
	
	
	public ComprobanteCSVDto() {
	}


	public int getCmpaId() {
		return cmpaId;
	}


	public void setCmpaId(int cmpaId) {
		this.cmpaId = cmpaId;
	}


	public String getCmpaNumComprobante() {
		return cmpaNumComprobante;
	}


	public void setCmpaNumComprobante(String cmpaNumComprobante) {
		this.cmpaNumComprobante = cmpaNumComprobante;
	}


	public Integer getCmpaNumComprobanteSecuencial() {
		return cmpaNumComprobanteSecuencial;
	}


	public void setCmpaNumComprobanteSecuencial(Integer cmpaNumComprobanteSecuencial) {
		this.cmpaNumComprobanteSecuencial = cmpaNumComprobanteSecuencial;
	}


	public String getCmpaDescripcion() {
		return cmpaDescripcion;
	}


	public void setCmpaDescripcion(String cmpaDescripcion) {
		this.cmpaDescripcion = cmpaDescripcion;
	}


	public BigDecimal getCmpaTotalPago() {
		return cmpaTotalPago;
	}


	public void setCmpaTotalPago(BigDecimal cmpaTotalPago) {
		this.cmpaTotalPago = cmpaTotalPago;
	}


	public Integer getCmpaEstado() {
		return cmpaEstado;
	}


	public void setCmpaEstado(Integer cmpaEstado) {
		this.cmpaEstado = cmpaEstado;
	}


	public BigDecimal getCmpaTotalFacultad() {
		return cmpaTotalFacultad;
	}


	public void setCmpaTotalFacultad(BigDecimal cmpaTotalFacultad) {
		this.cmpaTotalFacultad = cmpaTotalFacultad;
	}


	public Integer getCmpaProcSau() {
		return cmpaProcSau;
	}


	public void setCmpaProcSau(Integer cmpaProcSau) {
		this.cmpaProcSau = cmpaProcSau;
	}


	public Integer getCmpaTipoUnidad() {
		return cmpaTipoUnidad;
	}


	public void setCmpaTipoUnidad(Integer cmpaTipoUnidad) {
		this.cmpaTipoUnidad = cmpaTipoUnidad;
	}


	public BigDecimal getCmpaValorPagado() {
		return cmpaValorPagado;
	}


	public void setCmpaValorPagado(BigDecimal cmpaValorPagado) {
		this.cmpaValorPagado = cmpaValorPagado;
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


	public Integer getCmpaAplicaGratuidad() {
		return cmpaAplicaGratuidad;
	}


	public void setCmpaAplicaGratuidad(Integer cmpaAplicaGratuidad) {
		this.cmpaAplicaGratuidad = cmpaAplicaGratuidad;
	}


	public Integer getCmpaMatrTipo() {
		return cmpaMatrTipo;
	}


	public void setCmpaMatrTipo(Integer cmpaMatrTipo) {
		this.cmpaMatrTipo = cmpaMatrTipo;
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


	public Timestamp getCmpaFechaCaduca() {
		return cmpaFechaCaduca;
	}


	public void setCmpaFechaCaduca(Timestamp cmpaFechaCaduca) {
		this.cmpaFechaCaduca = cmpaFechaCaduca;
	}


	public Timestamp getCmpaFechaEmision() {
		return cmpaFechaEmision;
	}


	public void setCmpaFechaEmision(Timestamp cmpaFechaEmision) {
		this.cmpaFechaEmision = cmpaFechaEmision;
	}


	public Integer getCmpaFichaMatricula() {
		return cmpaFichaMatricula;
	}


	public void setCmpaFichaMatricula(Integer cmpaFichaMatricula) {
		this.cmpaFichaMatricula = cmpaFichaMatricula;
	}


	public int getPrsId() {
		return prsId;
	}


	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}


	public Integer getPrsTipoIdentificacion() {
		return prsTipoIdentificacion;
	}


	public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
		this.prsTipoIdentificacion = prsTipoIdentificacion;
	}


	public Integer getPrsTipoIdentificacionSniese() {
		return prsTipoIdentificacionSniese;
	}


	public void setPrsTipoIdentificacionSniese(Integer prsTipoIdentificacionSniese) {
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
	}


	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}


	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}


	public String getPrsPrimerApellido() {
		return prsPrimerApellido;
	}


	public void setPrsPrimerApellido(String prsPrimerApellido) {
		this.prsPrimerApellido = prsPrimerApellido;
	}


	public String getPrsNombres() {
		return prsNombres;
	}


	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}


	public String getPrsMailPersonal() {
		return prsMailPersonal;
	}


	public void setPrsMailPersonal(String prsMailPersonal) {
		this.prsMailPersonal = prsMailPersonal;
	}


	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}


	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}


	public String getPrsTelefono() {
		return prsTelefono;
	}


	public void setPrsTelefono(String prsTelefono) {
		this.prsTelefono = prsTelefono;
	}


	public String getPrsCelular() {
		return prsCelular;
	}


	public void setPrsCelular(String prsCelular) {
		this.prsCelular = prsCelular;
	}


	public Date getPrsFechaNacimiento() {
		return prsFechaNacimiento;
	}


	public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
		this.prsFechaNacimiento = prsFechaNacimiento;
	}


	public Integer getPrsSexo() {
		return prsSexo;
	}


	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}


	public Integer getPrsSexoSniese() {
		return prsSexoSniese;
	}


	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
	}


	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}


	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}


	public Integer getPrsEtnia() {
		return prsEtnia;
	}


	public void setPrsEtnia(Integer prsEtnia) {
		this.prsEtnia = prsEtnia;
	}


	public Integer getPrsUbicacionNacionalidad() {
		return prsUbicacionNacionalidad;
	}


	public void setPrsUbicacionNacionalidad(Integer prsUbicacionNacionalidad) {
		this.prsUbicacionNacionalidad = prsUbicacionNacionalidad;
	}


	public String getPrsDireccion() {
		return prsDireccion;
	}


	public void setPrsDireccion(String prsDireccion) {
		this.prsDireccion = prsDireccion;
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


	public Integer getCrrNivel() {
		return crrNivel;
	}


	public void setCrrNivel(Integer crrNivel) {
		this.crrNivel = crrNivel;
	}


	public String getCrrDetalle() {
		return crrDetalle;
	}


	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}


	public Integer getCrrFacultad() {
		return crrFacultad;
	}


	public void setCrrFacultad(Integer crrFacultad) {
		this.crrFacultad = crrFacultad;
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


	public String getFclCodSori() {
		return fclCodSori;
	}


	public void setFclCodSori(String fclCodSori) {
		this.fclCodSori = fclCodSori;
	}


	public String getFclUej() {
		return fclUej;
	}


	public void setFclUej(String fclUej) {
		this.fclUej = fclUej;
	}


	public String getFechaEmision() {
		return fechaEmision;
	}


	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}


	public String getFechaCaduca() {
		return fechaCaduca;
	}


	public void setFechaCaduca(String fechaCaduca) {
		this.fechaCaduca = fechaCaduca;
	}


	
	
	
}
