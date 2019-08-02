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
   
 ARCHIVO:     FichaInscripcionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad FichaInscripcion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-03-2017 			David Arellano  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) FichaInscripcionDto. DTO encargado de manejar los datos de la
 * entidad FichaInscripcion.
 * 
 * @author darellano.
 * @version 1.0
 */
public class FichaInscripcionDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	// Tabla Periodo_Académico
	private Integer pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	private Date pracFechaIncio;
	private Date pracFechaFin;

	// Tabla FichaEstudiante
	private Integer fcesId;
	private Integer fcesInstitucionAcademica;
	private Date fcesFechaInicio;
	private Date fcesFechaEgresamiento;
	private Date fcesFechaActaGrado;
	private String fcesNumActaGrado;
	private Date fcesFechaRefrendacion;
	private String fcesNumRefrendacion;
	private String fcesCrrEstudPrevios;
	private Integer fcesTiempoEstudRec;
	private Integer fcesTipoDuracRec;
	private Integer fcesTipoColegio;
	private String fcesTipoColegioSniese;
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private String fcesLinkTesis;
	private Integer fcesRecEstudPrevios;
	private String fcesRecEstudPrevSniese;
	private Timestamp fcesFechaCreacion;
	private Integer fcesEstadoUniversitario;
	private Integer fcesEstadoMatricula;
	private String fcesObservacion;
	private String fcesTituloBachiller;

	// tabla persona
	private Integer prsId;
	private Integer prsUbicacionNacimiento;
	private Integer prsUbicacionResidencia;
	private Integer prsEtnia;
	private Integer prsTipoIdentificacion;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private Integer prsSexo;
	private Integer prsSexoSniese;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private String prsCallePrincipal;
	private String prsCalleSecundaria;
	private String prsSectorDomicilio;
	private String prsNumeroCasa;
	private String prsReferenciaDomicilio;
	private String prsCelular;

	// Tabla fichaInscripcion
	private Integer fcinId;
	private Integer fcinUsuarioRol;
	private Integer fcinPeriodoAcademico;
	private Timestamp fcinFechaInscripcion;
	private String fcinObservacion;
	private Integer fcinTipo;
	private Integer fcinMatriculado;
	private Integer fcinEstado;
	private Integer fcinEncuesta;
	private Integer fcinCncrId;
	private Integer fcinCarrera;
	private Integer fcinCncrArea;
	private Integer fcinCarreraSiiu;
	private Integer fcinEstadoRetiro;
	private Integer fcinTipoIngreso;
	private Integer fcinEstadoIngreso;
	private Integer fcinReinicioOrigen;
	private Boolean fcinAcceso;

	// Tabla configuracion carrera
	private Integer cncrId;
	private Integer cncrUbicacion;
	private Integer cncrTipoSede;
	private Integer cncrTitulo;
	private Integer cncrTipoFormacion;
	private Integer cncrDuracion;
	private Integer cncrVigencia;
	private Integer cncrModalidad;

	// tabla carrera
	private Integer crrId;
	private Integer crrDependencia;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrEspeCodigo; // Campo de carreras del sau
	private Integer crrTipo;
	private Integer crrProceso;

	// tabla usuario rol
	private Integer usroId;
	private Integer usroUsuario;
	private Integer usroRol;
	private Integer usroEstado;

	// tabla usuario
	private Integer usrId;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;
	private Timestamp usrFechaCreacion;
	private Date usrFechaCaducidad;
	private Date usrFechaCadPass;
	private Integer usrEstado;
	private Integer usrEstSesion;
	private Integer usrActiveDirectory;

	// --v2
	// tabla dependencia
	private Integer dpnId;
	private String dpnDescripcion;

	public FichaInscripcionDto() {
	}

	public FichaInscripcionDto(int fcinId) {
		this.fcinId = fcinId;
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

	public Date getPracFechaIncio() {
		return pracFechaIncio;
	}

	public void setPracFechaIncio(Date pracFechaIncio) {
		this.pracFechaIncio = pracFechaIncio;
	}

	public Date getPracFechaFin() {
		return pracFechaFin;
	}

	public void setPracFechaFin(Date pracFechaFin) {
		this.pracFechaFin = pracFechaFin;
	}

	public Integer getFcesId() {
		return fcesId;
	}

	public void setFcesId(Integer fcesId) {
		this.fcesId = fcesId;
	}

	public Integer getFcesInstitucionAcademica() {
		return fcesInstitucionAcademica;
	}

	public void setFcesInstitucionAcademica(Integer fcesInstitucionAcademica) {
		this.fcesInstitucionAcademica = fcesInstitucionAcademica;
	}

	public Date getFcesFechaInicio() {
		return fcesFechaInicio;
	}

	public void setFcesFechaInicio(Date fcesFechaInicio) {
		this.fcesFechaInicio = fcesFechaInicio;
	}

	public Date getFcesFechaEgresamiento() {
		return fcesFechaEgresamiento;
	}

	public void setFcesFechaEgresamiento(Date fcesFechaEgresamiento) {
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
	}

	public Date getFcesFechaActaGrado() {
		return fcesFechaActaGrado;
	}

	public void setFcesFechaActaGrado(Date fcesFechaActaGrado) {
		this.fcesFechaActaGrado = fcesFechaActaGrado;
	}

	public String getFcesNumActaGrado() {
		return fcesNumActaGrado;
	}

	public void setFcesNumActaGrado(String fcesNumActaGrado) {
		this.fcesNumActaGrado = fcesNumActaGrado;
	}

	public Date getFcesFechaRefrendacion() {
		return fcesFechaRefrendacion;
	}

	public void setFcesFechaRefrendacion(Date fcesFechaRefrendacion) {
		this.fcesFechaRefrendacion = fcesFechaRefrendacion;
	}

	public String getFcesNumRefrendacion() {
		return fcesNumRefrendacion;
	}

	public void setFcesNumRefrendacion(String fcesNumRefrendacion) {
		this.fcesNumRefrendacion = fcesNumRefrendacion;
	}

	public String getFcesCrrEstudPrevios() {
		return fcesCrrEstudPrevios;
	}

	public void setFcesCrrEstudPrevios(String fcesCrrEstudPrevios) {
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
	}

	public Integer getFcesTiempoEstudRec() {
		return fcesTiempoEstudRec;
	}

	public void setFcesTiempoEstudRec(Integer fcesTiempoEstudRec) {
		this.fcesTiempoEstudRec = fcesTiempoEstudRec;
	}

	public Integer getFcesTipoDuracRec() {
		return fcesTipoDuracRec;
	}

	public void setFcesTipoDuracRec(Integer fcesTipoDuracRec) {
		this.fcesTipoDuracRec = fcesTipoDuracRec;
	}

	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}

	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}

	public String getFcesTipoColegioSniese() {
		return fcesTipoColegioSniese;
	}

	public void setFcesTipoColegioSniese(String fcesTipoColegioSniese) {
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
	}

	public BigDecimal getFcesNotaPromAcumulado() {
		return fcesNotaPromAcumulado;
	}

	public void setFcesNotaPromAcumulado(BigDecimal fcesNotaPromAcumulado) {
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
	}

	public BigDecimal getFcesNotaTrabTitulacion() {
		return fcesNotaTrabTitulacion;
	}

	public void setFcesNotaTrabTitulacion(BigDecimal fcesNotaTrabTitulacion) {
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
	}

	public String getFcesLinkTesis() {
		return fcesLinkTesis;
	}

	public void setFcesLinkTesis(String fcesLinkTesis) {
		this.fcesLinkTesis = fcesLinkTesis;
	}

	public Integer getFcesRecEstudPrevios() {
		return fcesRecEstudPrevios;
	}

	public void setFcesRecEstudPrevios(Integer fcesRecEstudPrevios) {
		this.fcesRecEstudPrevios = fcesRecEstudPrevios;
	}

	public String getFcesRecEstudPrevSniese() {
		return fcesRecEstudPrevSniese;
	}

	public void setFcesRecEstudPrevSniese(String fcesRecEstudPrevSniese) {
		this.fcesRecEstudPrevSniese = fcesRecEstudPrevSniese;
	}

	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}

	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}

	public Integer getFcesEstadoUniversitario() {
		return fcesEstadoUniversitario;
	}

	public void setFcesEstadoUniversitario(Integer fcesEstadoUniversitario) {
		this.fcesEstadoUniversitario = fcesEstadoUniversitario;
	}

	public Integer getFcesEstadoMatricula() {
		return fcesEstadoMatricula;
	}

	public void setFcesEstadoMatricula(Integer fcesEstadoMatricula) {
		this.fcesEstadoMatricula = fcesEstadoMatricula;
	}

	public String getFcesObservacion() {
		return fcesObservacion;
	}

	public void setFcesObservacion(String fcesObservacion) {
		this.fcesObservacion = fcesObservacion;
	}

	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}

	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}

	public Integer getPrsId() {
		return prsId;
	}

	public void setPrsId(Integer prsId) {
		this.prsId = prsId;
	}

	public Integer getPrsUbicacionNacimiento() {
		return prsUbicacionNacimiento;
	}

	public void setPrsUbicacionNacimiento(Integer prsUbicacionNacimiento) {
		this.prsUbicacionNacimiento = prsUbicacionNacimiento;
	}

	public Integer getPrsUbicacionResidencia() {
		return prsUbicacionResidencia;
	}

	public void setPrsUbicacionResidencia(Integer prsUbicacionResidencia) {
		this.prsUbicacionResidencia = prsUbicacionResidencia;
	}

	public Integer getPrsEtnia() {
		return prsEtnia;
	}

	public void setPrsEtnia(Integer prsEtnia) {
		this.prsEtnia = prsEtnia;
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

	public Integer getFcinId() {
		return fcinId;
	}

	public void setFcinId(Integer fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinUsuarioRol() {
		return fcinUsuarioRol;
	}

	public void setFcinUsuarioRol(Integer fcinUsuarioRol) {
		this.fcinUsuarioRol = fcinUsuarioRol;
	}

	public Integer getFcinPeriodoAcademico() {
		return fcinPeriodoAcademico;
	}

	public void setFcinPeriodoAcademico(Integer fcinPeriodoAcademico) {
		this.fcinPeriodoAcademico = fcinPeriodoAcademico;
	}

	public Timestamp getFcinFechaInscripcion() {
		return fcinFechaInscripcion;
	}

	public void setFcinFechaInscripcion(Timestamp fcinFechaInscripcion) {
		this.fcinFechaInscripcion = fcinFechaInscripcion;
	}

	public Integer getFcinEstadoRetiro() {
		return fcinEstadoRetiro;
	}

	public void setFcinEstadoRetiro(Integer fcinEstadoRetiro) {
		this.fcinEstadoRetiro = fcinEstadoRetiro;
	}

	public String getFcinObservacion() {
		return fcinObservacion;
	}

	public void setFcinObservacion(String fcinObservacion) {
		this.fcinObservacion = fcinObservacion;
	}

	public Integer getFcinTipo() {
		return fcinTipo;
	}

	public void setFcinTipo(Integer fcinTipo) {
		this.fcinTipo = fcinTipo;
	}

	public Integer getFcinMatriculado() {
		return fcinMatriculado;
	}

	public void setFcinMatriculado(Integer fcinMatriculado) {
		this.fcinMatriculado = fcinMatriculado;
	}

	public Integer getCncrId() {
		return cncrId;
	}

	public void setCncrId(Integer cncrId) {
		this.cncrId = cncrId;
	}

	public Integer getCncrUbicacion() {
		return cncrUbicacion;
	}

	public void setCncrUbicacion(Integer cncrUbicacion) {
		this.cncrUbicacion = cncrUbicacion;
	}

	public Integer getCncrTipoSede() {
		return cncrTipoSede;
	}

	public void setCncrTipoSede(Integer cncrTipoSede) {
		this.cncrTipoSede = cncrTipoSede;
	}

	public Integer getCncrTitulo() {
		return cncrTitulo;
	}

	public void setCncrTitulo(Integer cncrTitulo) {
		this.cncrTitulo = cncrTitulo;
	}

	public Integer getCncrTipoFormacion() {
		return cncrTipoFormacion;
	}

	public void setCncrTipoFormacion(Integer cncrTipoFormacion) {
		this.cncrTipoFormacion = cncrTipoFormacion;
	}

	public Integer getCncrDuracion() {
		return cncrDuracion;
	}

	public void setCncrDuracion(Integer cncrDuracion) {
		this.cncrDuracion = cncrDuracion;
	}

	public Integer getCncrVigencia() {
		return cncrVigencia;
	}

	public void setCncrVigencia(Integer cncrVigencia) {
		this.cncrVigencia = cncrVigencia;
	}

	public Integer getCncrModalidad() {
		return cncrModalidad;
	}

	public void setCncrModalidad(Integer cncrModalidad) {
		this.cncrModalidad = cncrModalidad;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public Integer getCrrDependencia() {
		return crrDependencia;
	}

	public void setCrrDependencia(Integer crrDependencia) {
		this.crrDependencia = crrDependencia;
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

	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	public Integer getUsroId() {
		return usroId;
	}

	public void setUsroId(Integer usroId) {
		this.usroId = usroId;
	}

	public Integer getUsroUsuario() {
		return usroUsuario;
	}

	public void setUsroUsuario(Integer usroUsuario) {
		this.usroUsuario = usroUsuario;
	}

	public Integer getUsroRol() {
		return usroRol;
	}

	public void setUsroRol(Integer usroRol) {
		this.usroRol = usroRol;
	}

	public Integer getUsroEstado() {
		return usroEstado;
	}

	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}

	public Integer getFcinEstado() {
		return fcinEstado;
	}

	public void setFcinEstado(Integer fcinEstado) {
		this.fcinEstado = fcinEstado;
	}

	public Integer getUsrId() {
		return usrId;
	}

	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}

	public String getUsrIdentificacion() {
		return usrIdentificacion;
	}

	public void setUsrIdentificacion(String usrIdentificacion) {
		this.usrIdentificacion = usrIdentificacion;
	}

	public String getUsrNick() {
		return usrNick;
	}

	public void setUsrNick(String usrNick) {
		this.usrNick = usrNick;
	}

	public String getUsrPassword() {
		return usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	public Timestamp getUsrFechaCreacion() {
		return usrFechaCreacion;
	}

	public void setUsrFechaCreacion(Timestamp usrFechaCreacion) {
		this.usrFechaCreacion = usrFechaCreacion;
	}

	public Date getUsrFechaCaducidad() {
		return usrFechaCaducidad;
	}

	public void setUsrFechaCaducidad(Date usrFechaCaducidad) {
		this.usrFechaCaducidad = usrFechaCaducidad;
	}

	public Date getUsrFechaCadPass() {
		return usrFechaCadPass;
	}

	public void setUsrFechaCadPass(Date usrFechaCadPass) {
		this.usrFechaCadPass = usrFechaCadPass;
	}

	public Integer getUsrEstado() {
		return usrEstado;
	}

	public void setUsrEstado(Integer usrEstado) {
		this.usrEstado = usrEstado;
	}

	public Integer getFcinTipoIngreso() {
		return fcinTipoIngreso;
	}

	public void setFcinTipoIngreso(Integer fcinTipoIngreso) {
		this.fcinTipoIngreso = fcinTipoIngreso;
	}

	public Integer getFcinEstadoIngreso() {
		return fcinEstadoIngreso;
	}

	public void setFcinEstadoIngreso(Integer fcinEstadoIngreso) {
		this.fcinEstadoIngreso = fcinEstadoIngreso;
	}

	public Integer getUsrEstSesion() {
		return usrEstSesion;
	}

	public void setUsrEstSesion(Integer usrEstSesion) {
		this.usrEstSesion = usrEstSesion;
	}

	public Integer getUsrActiveDirectory() {
		return usrActiveDirectory;
	}

	public void setUsrActiveDirectory(Integer usrActiveDirectory) {
		this.usrActiveDirectory = usrActiveDirectory;
	}

	public Integer getFcinEncuesta() {
		return fcinEncuesta;
	}

	public void setFcinEncuesta(Integer fcinEncuesta) {
		this.fcinEncuesta = fcinEncuesta;
	}

	public Integer getFcinCncrId() {
		return fcinCncrId;
	}

	public void setFcinCncrId(Integer fcinCncrId) {
		this.fcinCncrId = fcinCncrId;
	}

	public Integer getFcinCarrera() {
		return fcinCarrera;
	}

	public void setFcinCarrera(Integer fcinCarrera) {
		this.fcinCarrera = fcinCarrera;
	}

	public Integer getFcinCarreraSiiu() {
		return fcinCarreraSiiu;
	}

	public void setFcinCarreraSiiu(Integer fcinCarreraSiiu) {
		this.fcinCarreraSiiu = fcinCarreraSiiu;
	}

	public Integer getFcinCncrArea() {
		return fcinCncrArea;
	}

	public void setFcinCncrArea(Integer fcinCncrArea) {
		this.fcinCncrArea = fcinCncrArea;
	}

	public Integer getDpnId() {
		return dpnId;
	}

	public void setDpnId(Integer dpnId) {
		this.dpnId = dpnId;
	}

	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	public Boolean getFcinAcceso() {
		return fcinAcceso;
	}

	public void setFcinAcceso(Boolean fcinAcceso) {
		this.fcinAcceso = fcinAcceso;
	}

	public Integer getFcinReinicioOrigen() {
		return fcinReinicioOrigen;
	}

	public void setFcinReinicioOrigen(Integer fcinReinicioOrigen) {
		this.fcinReinicioOrigen = fcinReinicioOrigen;
	}

	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/

	public String toString() {
		String tabulador = "\t";
		StringBuilder sb = new StringBuilder();

		sb.append(" PeriodoAcademico ");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion == null ? "NULL" : pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado == null ? "NULL" : pracEstado));
		sb.append(tabulador + "pracFechaIncio : " + (pracFechaIncio == null ? "NULL" : pracFechaIncio));
		sb.append(tabulador + "pracFechaFin : " + (pracFechaFin == null ? "NULL" : pracFechaFin));

		sb.append(" FichaEstudiante ");
		sb.append(tabulador + "fcesId : " + fcesId);
		sb.append(tabulador + "fcesInstitucionAcademica : " + fcesInstitucionAcademica);
		sb.append(tabulador + "fcesFechaInicio : " + (fcesFechaInicio == null ? "NULL" : fcesFechaInicio));
		sb.append(tabulador + "fcesFechaEgresamiento : "
				+ (fcesFechaEgresamiento == null ? "NULL" : fcesFechaEgresamiento));
		sb.append(tabulador + "fcesFechaActaGrado : " + (fcesFechaActaGrado == null ? "NULL" : fcesFechaActaGrado));
		sb.append(tabulador + "fcesNumActaGrado : " + (fcesNumActaGrado == null ? "NULL" : fcesNumActaGrado));
		sb.append(tabulador + "fcesFechaRefrendacion : "
				+ (fcesFechaRefrendacion == null ? "NULL" : fcesFechaRefrendacion));
		sb.append(tabulador + "fcesNumRefrendacion : " + (fcesNumRefrendacion == null ? "NULL" : fcesNumRefrendacion));
		sb.append(tabulador + "fcesCrrEstudPrevios : " + (fcesCrrEstudPrevios == null ? "NULL" : fcesCrrEstudPrevios));
		sb.append(tabulador + "fcesTiempoEstudRec : " + (fcesTiempoEstudRec == null ? "NULL" : fcesTiempoEstudRec));
		sb.append(tabulador + "fcesTipoDuracRec : " + (fcesTipoDuracRec == null ? "NULL" : fcesTipoDuracRec));
		sb.append(tabulador + "fcesTipoColegio : " + (fcesTipoColegio == null ? "NULL" : fcesTipoColegio));
		sb.append(tabulador + "fcesTipoColegioSniese : "
				+ (fcesTipoColegioSniese == null ? "NULL" : fcesTipoColegioSniese));
		sb.append(tabulador + "fcesNotaPromAcumulado : "
				+ (fcesNotaPromAcumulado == null ? "NULL" : fcesNotaPromAcumulado));
		sb.append(tabulador + "fcesNotaTrabTitulacion : "
				+ (fcesNotaTrabTitulacion == null ? "NULL" : fcesNotaTrabTitulacion));
		sb.append(tabulador + "fcesLinkTesis : " + (fcesLinkTesis == null ? "NULL" : fcesLinkTesis));
		sb.append(tabulador + "fcesRecEstudPrevios : " + (fcesRecEstudPrevios == null ? "NULL" : fcesRecEstudPrevios));
		sb.append(tabulador + "fcesRecEstudPrevSniese : "
				+ (fcesRecEstudPrevSniese == null ? "NULL" : fcesRecEstudPrevSniese));
		sb.append(tabulador + "fcesFechaCreacion : " + (fcesFechaCreacion == null ? "NULL" : fcesFechaCreacion));
		sb.append(tabulador + "fcesEstadoUniversitario : "
				+ (fcesEstadoUniversitario == null ? "NULL" : fcesEstadoUniversitario));
		sb.append(tabulador + "fcesEstadoMatricula : " + (fcesEstadoMatricula == null ? "NULL" : fcesEstadoMatricula));
		sb.append(tabulador + "fcesObservacion : " + (fcesObservacion == null ? "NULL" : fcesObservacion));
		sb.append(tabulador + "fcesTituloBachiller : " + (fcesTituloBachiller == null ? "NULL" : fcesTituloBachiller));

		sb.append(" Persona ");
		sb.append(tabulador + "prsId : " + prsId);
		sb.append(tabulador + "prsUbicacionNacimiento : " + prsUbicacionNacimiento);
		sb.append(tabulador + "prsUbicacionResidencia : "
				+ (prsUbicacionResidencia == null ? "NULL" : prsUbicacionResidencia));
		sb.append(tabulador + "prsEtnia : " + (prsEtnia == null ? "NULL" : prsEtnia));
		sb.append(tabulador + "prsTipoIdentificacion : "
				+ (prsTipoIdentificacion == null ? "NULL" : prsTipoIdentificacion));
		sb.append(tabulador + "prsTipoIdentificacionSniese : "
				+ (prsTipoIdentificacionSniese == null ? "NULL" : prsTipoIdentificacionSniese));
		sb.append(tabulador + "prsIdentificacion : " + (prsIdentificacion == null ? "NULL" : prsIdentificacion));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido == null ? "NULL" : prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido == null ? "NULL" : prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres == null ? "NULL" : prsNombres));
		sb.append(tabulador + "prsSexo : " + (prsSexo == null ? "NULL" : prsSexo));
		sb.append(tabulador + "prsSexoSniese : " + (prsSexoSniese == null ? "NULL" : prsSexoSniese));
		sb.append(tabulador + "prsMailPersonal : " + (prsMailPersonal == null ? "NULL" : prsMailPersonal));
		sb.append(
				tabulador + "prsMailInstitucional : " + (prsMailInstitucional == null ? "NULL" : prsMailInstitucional));
		sb.append(tabulador + "prsTelefono : " + (prsTelefono == null ? "NULL" : prsTelefono));
		sb.append(tabulador + "prsFechaNacimiento : " + (prsFechaNacimiento == null ? "NULL" : prsFechaNacimiento));
		sb.append(tabulador + "prsCallePrincipal : " + (prsCallePrincipal == null ? "NULL" : prsCallePrincipal));
		sb.append(tabulador + "prsCalleSecundaria : " + (prsCalleSecundaria == null ? "NULL" : prsCalleSecundaria));
		sb.append(tabulador + "prsSectorDomicilio : " + (prsSectorDomicilio == null ? "NULL" : prsSectorDomicilio));
		sb.append(tabulador + "prsNumeroCasa : " + (prsNumeroCasa == null ? "NULL" : prsNumeroCasa));
		sb.append(tabulador + "prsReferenciaDomicilio : "
				+ (prsReferenciaDomicilio == null ? "NULL" : prsReferenciaDomicilio));
		sb.append(tabulador + "prsCelular : " + (prsCelular == null ? "NULL" : prsCelular));

		sb.append(" FichaInscripcion ");
		sb.append(tabulador + "fcinId : " + fcinId);
		sb.append(tabulador + "fcinUsuarioRol : " + (fcinUsuarioRol == null ? "NULL" : fcinUsuarioRol));
		sb.append(
				tabulador + "fcinPeriodoAcademico : " + (fcinPeriodoAcademico == null ? "NULL" : fcinPeriodoAcademico));
		sb.append(
				tabulador + "fcinFechaInscripcion : " + (fcinFechaInscripcion == null ? "NULL" : fcinFechaInscripcion));
		sb.append(tabulador + "fcinObservacion : " + (fcinObservacion == null ? "NULL" : fcinObservacion));
		sb.append(tabulador + "fcinTipo : " + (fcinTipo == null ? "NULL" : fcinTipo));
		sb.append(tabulador + "fcinMatriculado : " + (fcinMatriculado == null ? "NULL" : fcinMatriculado));
		sb.append(tabulador + "fcinEstado : " + (fcinEstado == null ? "NULL" : fcinEstado));
		sb.append(tabulador + "fcinEncuesta : " + (fcinEncuesta == null ? "NULL" : fcinEncuesta));

		sb.append(" ConfiguracionCarrera ");
		sb.append(tabulador + "cncrId : " + cncrId);
		sb.append(tabulador + "cncrUbicacion : " + cncrUbicacion);
		sb.append(tabulador + "cncrTitulo : " + (cncrTitulo == null ? "NULL" : cncrTitulo));
		sb.append(tabulador + "cncrTipoFormacion : " + (cncrTipoFormacion == null ? "NULL" : cncrTipoFormacion));
		sb.append(tabulador + "cncrDuracion : " + (cncrDuracion == null ? "NULL" : cncrDuracion));
		sb.append(tabulador + "cncrVigencia : " + (cncrVigencia == null ? "NULL" : cncrVigencia));
		sb.append(tabulador + "cncrModalidad : " + (cncrModalidad == null ? "NULL" : cncrModalidad));

		sb.append(" Carrera ");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDependencia : " + (crrDependencia == null ? "NULL" : crrDependencia));
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion == null ? "NULL" : crrDescripcion));
		sb.append(tabulador + "crrCodSniese : " + (crrCodSniese == null ? "NULL" : crrCodSniese));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrFechaCreacion : " + (crrFechaCreacion == null ? "NULL" : crrFechaCreacion));
		sb.append(tabulador + "crrResolucion : " + (crrResolucion == null ? "NULL" : crrResolucion));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle == null ? "NULL" : crrDetalle));
		sb.append(tabulador + "crrEspeCodigo : " + (crrEspeCodigo == null ? "NULL" : crrEspeCodigo));

		sb.append(" Rol ");
		sb.append(tabulador + "usroId : " + usroId);
		sb.append(tabulador + "usroUsuario : " + (usroUsuario == null ? "NULL" : usroUsuario));
		sb.append(tabulador + "usroRol : " + (usroRol == null ? "NULL" : usroRol));
		sb.append(tabulador + "usroEstado : " + (usroEstado == null ? "NULL" : usroEstado));

		sb.append(" Usuario ");
		sb.append(tabulador + "usrId : " + usrId);
		sb.append(tabulador + "usrIdentificacion : " + (usrIdentificacion == null ? "NULL" : usrIdentificacion));
		sb.append(tabulador + "usrNick : " + (usrNick == null ? "NULL" : usrNick));
		sb.append(tabulador + "usrPassword : " + (usrPassword == null ? "NULL" : usrPassword));
		sb.append(tabulador + "usrFechaCreacion : " + (usrFechaCreacion == null ? "NULL" : usrFechaCreacion));
		sb.append(tabulador + "usrFechaCaducidad : " + (usrFechaCaducidad == null ? "NULL" : usrFechaCaducidad));
		sb.append(tabulador + "usrFechaCadPass : " + (usrFechaCadPass == null ? "NULL" : usrFechaCadPass));
		sb.append(tabulador + "usrEstado : " + (usrEstado == null ? "NULL" : usrEstado));
		sb.append(tabulador + "usrEstSesion : " + (usrEstSesion == null ? "NULL" : usrEstSesion));
		sb.append(tabulador + "usrActiveDirectory : " + (usrActiveDirectory == null ? "NULL" : usrActiveDirectory));

		return sb.toString();
	}

}
