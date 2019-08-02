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

 ARCHIVO:     DocenteJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad FichaDocente DTO JDBC. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 27-04-2017 			Dennis Collaguazo  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase (DTO) DocenteJdbcDto.
 * DTO encargado de manejar los datos de la entidad FichaDocente DTO JDBC. 
 * @author dcollaguazo.
 * @version 1.0
 */
public class DocenteJdbcDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	//Tabla FichaDocente
	private int fcdcId;

	//Tabla Persona
	private int prsId;
	private Integer prsTipoIdentificacion;
	private String prsTipoIdentificacionSt;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private Integer prsSexo;
	private String prsSexoSt;
	private Integer prsSexoSniese;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private Integer prsEtnId;
	private Integer prsUbcNacimiento;
	private Integer prsUbcResidencia;
	private String prsCallePrincipal;
	private String prsCalleSecundaria;
	private String prsSectorDomicilio;
	private String prsNumeroCasa;
	private String prsReferenciaDomicilio;
	private String prsCelular;
	
	//Tabla Ubicacion
	private Integer ubcPaisId;
	private String ubcPaisDescripcion;
	private Integer ubcCantonId;
	private String ubcCantonDescripcion;

	//Tabla Etnia
	private int etnId;
	private String etnDescripcion;

	//Tabla Carrera
	private int crrId;
	private Integer crrDpnId;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private String crrFechaCreacion;
	private String crrResolucion;
	private Integer crrTipo;

	//Tabla Dependencia (Facultad)
	private int dpnId;
	private Integer dpnSubId;
	private Integer dpnUbcId;
	private String dpnDescripcion;
	private Integer dpnJerarquia;
	private Integer dpnEstado;
	private Integer dpnCampus;

	//Tabla MallaCurricular
	private int mlcrId;
	private String mlcrDescripcion;

	//Tabla Nivel
	private int nvlId;
	private String nvlDescripcion;

	//Tabla MallaCurriularMateria
	private int mlcrmtId;

	//Tabla MallaCurricularParalelo
	private int mlcrprId;
	private Integer mlcrprPrlId;

	//Tabla Paralelo
	private int prlId;
	private String prlDescripcion;

	//Tabla Materia
	private int mtrId;
	private String mtrCodigo;
	private String mtrDescripcion;
	private Integer mtrHoras;
	private Integer mtrCreditos;
	private Integer mtrTimtId;

	//Tabla MallaPeriodo
	private int mlprId;
	
	//Tabla PeriodoAcademico;
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	
	//Tabla DetallePuesto
	private int dtpsId;
	private Integer dtpsEstado;
	private Integer dtpsPracId;
	private Integer dtpsTipoCarrera;
	
	//Tabla Puesto
	private int pstId;
	private String pstDenominacion;
	
	//Tabla RelacionLaboral
	private int rllbId;
	private String rllbDescripcion;
	
	//Tabla carga_horaria
	private int crhrId;
	private Integer crhrMlcrprId;
	private Integer crhrDtpsId;
	private Integer crhrNumHoras;
	
	//Tabla horario_academico
	private Integer hracMlcrprId;
	private Integer hracMlcrprIdComp;
	
	//Tabla Asignacion Evaluador
	private Integer asevId;
	private Integer asevCrhrId;
	
	//Variables para compartidos
	private String crrDescripcionComp;
	private String mtrDescripcionComp;
	private String prlDescripcionComp;
	private BigDecimal crhrDescripcionSuma;
	
	//Tiempo de dedicacion
	private Integer tmddId;
	private String tmddDescripcion;
	
	//variables reporte docencia por paralelo
	private Integer numeroMatriculados;
	private Integer numeroAprobados;
	private Integer numeroSupletorio;
	private Integer numeroAprobadosSupl;
	private Integer numeroReprobados;

	private Integer numeroInscritos;
	private Integer pasoNotas;
	private BigDecimal desviacionEstandar;
	
	
	public DocenteJdbcDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getFcdcId() {
		return fcdcId;
	}

	public void setFcdcId(int fcdcId) {
		this.fcdcId = fcdcId;
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

	public String getPrsTipoIdentificacionSt() {
		return prsTipoIdentificacionSt;
	}

	public void setPrsTipoIdentificacionSt(String prsTipoIdentificacionSt) {
		this.prsTipoIdentificacionSt = prsTipoIdentificacionSt;
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

	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}

	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}

	public String getPrsNombres() {
		return prsNombres;
	}

	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}

	public Integer getPrsSexo() {
		return prsSexo;
	}

	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}

	public String getPrsSexoSt() {
		return prsSexoSt;
	}

	public void setPrsSexoSt(String prsSexoSt) {
		this.prsSexoSt = prsSexoSt;
	}

	public Integer getPrsSexoSniese() {
		return prsSexoSniese;
	}

	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
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

	public Date getPrsFechaNacimiento() {
		return prsFechaNacimiento;
	}

	public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
		this.prsFechaNacimiento = prsFechaNacimiento;
	}

	public Integer getPrsEtnId() {
		return prsEtnId;
	}

	public void setPrsEtnId(Integer prsEtnId) {
		this.prsEtnId = prsEtnId;
	}

	public Integer getPrsUbcNacimiento() {
		return prsUbcNacimiento;
	}

	public void setPrsUbcNacimiento(Integer prsUbcNacimiento) {
		this.prsUbcNacimiento = prsUbcNacimiento;
	}

	public Integer getPrsUbcResidencia() {
		return prsUbcResidencia;
	}

	public void setPrsUbcResidencia(Integer prsUbcResidencia) {
		this.prsUbcResidencia = prsUbcResidencia;
	}

	public String getPrsCallePrincipal() {
		return prsCallePrincipal;
	}

	public void setPrsCallePrincipal(String prsCallePrincipal) {
		this.prsCallePrincipal = prsCallePrincipal;
	}

	public String getPrsCalleSecundaria() {
		return prsCalleSecundaria;
	}

	public void setPrsCalleSecundaria(String prsCalleSecundaria) {
		this.prsCalleSecundaria = prsCalleSecundaria;
	}

	public String getPrsSectorDomicilio() {
		return prsSectorDomicilio;
	}

	public void setPrsSectorDomicilio(String prsSectorDomicilio) {
		this.prsSectorDomicilio = prsSectorDomicilio;
	}

	public String getPrsNumeroCasa() {
		return prsNumeroCasa;
	}

	public void setPrsNumeroCasa(String prsNumeroCasa) {
		this.prsNumeroCasa = prsNumeroCasa;
	}

	public String getPrsReferenciaDomicilio() {
		return prsReferenciaDomicilio;
	}

	public void setPrsReferenciaDomicilio(String prsReferenciaDomicilio) {
		this.prsReferenciaDomicilio = prsReferenciaDomicilio;
	}

	public String getPrsCelular() {
		return prsCelular;
	}

	public void setPrsCelular(String prsCelular) {
		this.prsCelular = prsCelular;
	}

	public Integer getUbcPaisId() {
		return ubcPaisId;
	}

	public void setUbcPaisId(Integer ubcPaisId) {
		this.ubcPaisId = ubcPaisId;
	}

	public String getUbcPaisDescripcion() {
		return ubcPaisDescripcion;
	}

	public void setUbcPaisDescripcion(String ubcPaisDescripcion) {
		this.ubcPaisDescripcion = ubcPaisDescripcion;
	}

	public Integer getUbcCantonId() {
		return ubcCantonId;
	}

	public void setUbcCantonId(Integer ubcCantonId) {
		this.ubcCantonId = ubcCantonId;
	}

	public String getUbcCantonDescripcion() {
		return ubcCantonDescripcion;
	}

	public void setUbcCantonDescripcion(String ubcCantonDescripcion) {
		this.ubcCantonDescripcion = ubcCantonDescripcion;
	}

	public int getEtnId() {
		return etnId;
	}

	public void setEtnId(int etnId) {
		this.etnId = etnId;
	}

	public String getEtnDescripcion() {
		return etnDescripcion;
	}

	public void setEtnDescripcion(String etnDescripcion) {
		this.etnDescripcion = etnDescripcion;
	}

	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	public Integer getCrrDpnId() {
		return crrDpnId;
	}

	public void setCrrDpnId(Integer crrDpnId) {
		this.crrDpnId = crrDpnId;
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

	public String getCrrFechaCreacion() {
		return crrFechaCreacion;
	}

	public void setCrrFechaCreacion(String crrFechaCreacion) {
		this.crrFechaCreacion = crrFechaCreacion;
	}

	public String getCrrResolucion() {
		return crrResolucion;
	}

	public void setCrrResolucion(String crrResolucion) {
		this.crrResolucion = crrResolucion;
	}

	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	public int getDpnId() {
		return dpnId;
	}

	public void setDpnId(int dpnId) {
		this.dpnId = dpnId;
	}

	public Integer getDpnSubId() {
		return dpnSubId;
	}

	public void setDpnSubId(Integer dpnSubId) {
		this.dpnSubId = dpnSubId;
	}

	public Integer getDpnUbcId() {
		return dpnUbcId;
	}

	public void setDpnUbcId(Integer dpnUbcId) {
		this.dpnUbcId = dpnUbcId;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	public Integer getDpnJerarquia() {
		return dpnJerarquia;
	}

	public void setDpnJerarquia(Integer dpnJerarquia) {
		this.dpnJerarquia = dpnJerarquia;
	}

	public Integer getDpnEstado() {
		return dpnEstado;
	}

	public void setDpnEstado(Integer dpnEstado) {
		this.dpnEstado = dpnEstado;
	}

	public Integer getDpnCampus() {
		return dpnCampus;
	}

	public void setDpnCampus(Integer dpnCampus) {
		this.dpnCampus = dpnCampus;
	}

	public int getMlcrId() {
		return mlcrId;
	}

	public void setMlcrId(int mlcrId) {
		this.mlcrId = mlcrId;
	}

	public String getMlcrDescripcion() {
		return mlcrDescripcion;
	}

	public void setMlcrDescripcion(String mlcrDescripcion) {
		this.mlcrDescripcion = mlcrDescripcion;
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

	public int getMlcrmtId() {
		return mlcrmtId;
	}

	public void setMlcrmtId(int mlcrmtId) {
		this.mlcrmtId = mlcrmtId;
	}

	public int getMlcrprId() {
		return mlcrprId;
	}

	public void setMlcrprId(int mlcrprId) {
		this.mlcrprId = mlcrprId;
	}

	public int getPrlId() {
		return prlId;
	}

	public void setPrlId(int prlId) {
		this.prlId = prlId;
	}

	public String getPrlDescripcion() {
		return prlDescripcion;
	}

	public void setPrlDescripcion(String prlDescripcion) {
		this.prlDescripcion = prlDescripcion;
	}

	public int getMtrId() {
		return mtrId;
	}

	public void setMtrId(int mtrId) {
		this.mtrId = mtrId;
	}

	public String getMtrCodigo() {
		return mtrCodigo;
	}

	public void setMtrCodigo(String mtrCodigo) {
		this.mtrCodigo = mtrCodigo;
	}

	public String getMtrDescripcion() {
		return mtrDescripcion;
	}

	public void setMtrDescripcion(String mtrDescripcion) {
		this.mtrDescripcion = mtrDescripcion;
	}

	public int getMlprId() {
		return mlprId;
	}

	public void setMlprId(int mlprId) {
		this.mlprId = mlprId;
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

	public Integer getPracEstado() {
		return pracEstado;
	}

	public void setPracEstado(Integer pracEstado) {
		this.pracEstado = pracEstado;
	}
	
	public int getDtpsId() {
		return dtpsId;
	}

	public void setDtpsId(int dtpsId) {
		this.dtpsId = dtpsId;
	}

	public Integer getDtpsEstado() {
		return dtpsEstado;
	}

	public void setDtpsEstado(Integer dtpsEstado) {
		this.dtpsEstado = dtpsEstado;
	}

	public Integer getDtpsPracId() {
		return dtpsPracId;
	}

	public void setDtpsPracId(Integer dtpsPracId) {
		this.dtpsPracId = dtpsPracId;
	}

	public Integer getDtpsTipoCarrera() {
		return dtpsTipoCarrera;
	}

	public void setDtpsTipoCarrera(Integer dtpsTipoCarrera) {
		this.dtpsTipoCarrera = dtpsTipoCarrera;
	}

	public int getPstId() {
		return pstId;
	}

	public void setPstId(int pstId) {
		this.pstId = pstId;
	}

	public int getRllbId() {
		return rllbId;
	}

	public void setRllbId(int rllbId) {
		this.rllbId = rllbId;
	}

	
	public Integer getCrhrMlcrprId() {
		return crhrMlcrprId;
	}

	public void setCrhrMlcrprId(Integer crhrMlcrprId) {
		this.crhrMlcrprId = crhrMlcrprId;
	}

	
	public Integer getMlcrprPrlId() {
		return mlcrprPrlId;
	}

	public void setMlcrprPrlId(Integer mlcrprPrlId) {
		this.mlcrprPrlId = mlcrprPrlId;
	}

	public int getCrhrId() {
		return crhrId;
	}

	public void setCrhrId(int crhrId) {
		this.crhrId = crhrId;
	}

	public Integer getCrhrDtpsId() {
		return crhrDtpsId;
	}

	public void setCrhrDtpsId(Integer crhrDtpsId) {
		this.crhrDtpsId = crhrDtpsId;
	}

	public Integer getHracMlcrprIdComp() {
		return hracMlcrprIdComp;
	}

	public void setHracMlcrprIdComp(Integer hracMlcrprIdComp) {
		this.hracMlcrprIdComp = hracMlcrprIdComp;
	}

	public Integer getMtrHoras() {
		return mtrHoras;
	}

	public void setMtrHoras(Integer mtrHoras) {
		this.mtrHoras = mtrHoras;
	}

	public Integer getMtrCreditos() {
		return mtrCreditos;
	}

	public void setMtrCreditos(Integer mtrCreditos) {
		this.mtrCreditos = mtrCreditos;
	}
	
	public Integer getMtrTimtId() {
		return mtrTimtId;
	}

	public void setMtrTimtId(Integer mtrTimtId) {
		this.mtrTimtId = mtrTimtId;
	}

	public Integer getCrhrNumHoras() {
		return crhrNumHoras;
	}

	public void setCrhrNumHoras(Integer crhrNumHoras) {
		this.crhrNumHoras = crhrNumHoras;
	}

	public Integer getHracMlcrprId() {
		return hracMlcrprId;
	}

	public void setHracMlcrprId(Integer hracMlcrprId) {
		this.hracMlcrprId = hracMlcrprId;
	}

	public Integer getAsevId() {
		return asevId;
	}

	public void setAsevId(Integer asevId) {
		this.asevId = asevId;
	}

	public Integer getAsevCrhrId() {
		return asevCrhrId;
	}

	public void setAsevCrhrId(Integer asevCrhrId) {
		this.asevCrhrId = asevCrhrId;
	}

	public String getCrrDescripcionComp() {
		return crrDescripcionComp;
	}

	public void setCrrDescripcionComp(String crrDescripcionComp) {
		this.crrDescripcionComp = crrDescripcionComp;
	}

	public String getMtrDescripcionComp() {
		return mtrDescripcionComp;
	}

	public void setMtrDescripcionComp(String mtrDescripcionComp) {
		this.mtrDescripcionComp = mtrDescripcionComp;
	}

	public String getPrlDescripcionComp() {
		return prlDescripcionComp;
	}

	public void setPrlDescripcionComp(String prlDescripcionComp) {
		this.prlDescripcionComp = prlDescripcionComp;
	}
	
	

	public String getPstDenominacion() {
		return pstDenominacion;
	}

	public void setPstDenominacion(String pstDenominacion) {
		this.pstDenominacion = pstDenominacion;
	}
	

	public Integer getTmddId() {
		return tmddId;
	}

	public void setTmddId(Integer tmddId) {
		this.tmddId = tmddId;
	}

	public String getTmddDescripcion() {
		return tmddDescripcion;
	}

	public void setTmddDescripcion(String tmddDescripcion) {
		this.tmddDescripcion = tmddDescripcion;
	}


	
	public BigDecimal getCrhrDescripcionSuma() {
		return crhrDescripcionSuma;
	}

	public void setCrhrDescripcionSuma(BigDecimal crhrDescripcionSuma) {
		this.crhrDescripcionSuma = crhrDescripcionSuma;
	}

	public String getRllbDescripcion() {
		return rllbDescripcion;
	}

	public void setRllbDescripcion(String rllbDescripcion) {
		this.rllbDescripcion = rllbDescripcion;
	}

	
	public Integer getNumeroMatriculados() {
		return numeroMatriculados;
	}

	public void setNumeroMatriculados(Integer numeroMatriculados) {
		this.numeroMatriculados = numeroMatriculados;
	}

	public Integer getNumeroAprobados() {
		return numeroAprobados;
	}

	public void setNumeroAprobados(Integer numeroAprobados) {
		this.numeroAprobados = numeroAprobados;
	}

	public Integer getNumeroSupletorio() {
		return numeroSupletorio;
	}

	public void setNumeroSupletorio(Integer numeroSupletorio) {
		this.numeroSupletorio = numeroSupletorio;
	}

	public Integer getNumeroAprobadosSupl() {
		return numeroAprobadosSupl;
	}

	public void setNumeroAprobadosSupl(Integer numeroAprobadosSupl) {
		this.numeroAprobadosSupl = numeroAprobadosSupl;
	}

	public Integer getNumeroReprobados() {
		return numeroReprobados;
	}

	public void setNumeroReprobados(Integer numeroReprobados) {
		this.numeroReprobados = numeroReprobados;
	}

	public Integer getNumeroInscritos() {
		return numeroInscritos;
	}

	public void setNumeroInscritos(Integer numeroInscritos) {
		this.numeroInscritos = numeroInscritos;
	}

	public BigDecimal getDesviacionEstandar() {
		return desviacionEstandar;
	}

	public void setDesviacionEstandar(BigDecimal desviacionEstandar) {
		this.desviacionEstandar = desviacionEstandar;
	}
	
	public Integer getPasoNotas() {
		return pasoNotas;
	}

	public void setPasoNotas(Integer pasoNotas) {
		this.pasoNotas = pasoNotas;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(tabulador + "fcdcId : " + fcdcId);
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsTipoIdentificacion : " + (prsTipoIdentificacion==null? "NULL":prsTipoIdentificacion));
		sb.append(tabulador + "prsTipoIdentificacionSniese : " + (prsTipoIdentificacionSniese==null? "NULL":prsTipoIdentificacionSniese));
		sb.append(tabulador + "prsIdentificacion : " + (prsIdentificacion==null? "NULL":prsIdentificacion));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido==null? "NULL":prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido==null? "NULL":prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres==null? "NULL":prsNombres));
		sb.append(tabulador + "prsSexo : " + (prsSexo==null? "NULL":prsSexo));
		sb.append(tabulador + "prsSexoSniese : " + (prsSexoSniese==null? "NULL":prsSexoSniese));
		sb.append(tabulador + "prsMailPersonal : " + (prsMailPersonal==null? "NULL":prsMailPersonal));
		sb.append(tabulador + "prsMailInstitucional : " + (prsMailInstitucional==null? "NULL":prsMailInstitucional));
		sb.append(tabulador + "prsTelefono : " + (prsTelefono==null? "NULL":prsTelefono));
		sb.append(tabulador + "prsFechaNacimiento : " + (prsFechaNacimiento==null? "NULL":prsFechaNacimiento));
		


		return sb.toString();
	}

	


}
