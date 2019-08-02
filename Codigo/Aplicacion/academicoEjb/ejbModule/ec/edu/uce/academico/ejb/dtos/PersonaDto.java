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
   
 ARCHIVO:     PersonaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Persona. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-03-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) PersonaDto. DTO encargado de manejar los datos de la entidad
 * Persona.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class PersonaDto implements Serializable {

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	// NUMERAL PARA REPORTES
	private int numeral;

	// TABLA PERSONA
	private int prsId;
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
	private String prsTelefonoMovil;
	private String prsDireccionDomicilio;
	private Date prsFechaNacimiento;
	private Integer prsEtnId;
	private Integer prsUbcId;

	private String prsCargo;
	private String prsApellidosNombres;
	private String prsApellidosNombresEvaluacion;

	// TABLA TIEMPO_DEDICACION
	private int tmddId;
	private String tmddDescripcion;

	// TABLA RELACION_LABORAL
	private int rllbId;
	private String rllbDescripcion;

	// TABLA DETALLE_PUESTO
	private int dtpsId;
	private Integer dtpsCrrTipo;

	// TABLA PUESTO
	private String pstDenominacion;
	private Integer pstNivelRangoGradual;
	private Integer pstCategoria;

	// TABLA FICHA_ESTUDIANTE
	private Integer fcesTipoUnivEstudprev;
	private Integer fcesEstadoEstudprev;
	private Integer fcesUnivEstudPrevId;
	private Integer fcesTitEstudPrevId;
	private String fcesRegTituloPrev;

	// TABLA ETNIA
	private int etnId;
	private String etnCodigoSniese;
	private String etnDescripcion;
	// TABLA UBICACION-NACIONALIDAD
	private int ubcPaisId;
	private String ubcPaisDescripcion;
	private Integer ubcPaisJerarquia;
	private String ubcPaisGentilicio;
	private String ubcPaisCodSniese;
	private Integer ubcPaisSubId;
	// TABLA UBICACION-PROVINCIA
	private int ubcProvId;
	private String ubcProvDescripcion;
	private Integer ubcProvJerarquia;
	private String ubcProvGentilicio;
	private String ubcProvCodSniese;
	private Integer ubcProvSubId;
	// TABLA UBICACION-CANTON
	private int ubcCantonId;
	private String ubcCantonDescripcion;
	private Integer ubcCantonJerarquia;
	private String ubcCantonGentilicio;
	private String ubcCantonCodSniese;
	private Integer ubcCantonSubId;
	// TABLA UBICACION-PARROQUIA
	private int ubcPrrqId;
	private String ubcPrrqDescripcion;
	private Integer ubcPrrqJerarquia;
	private String ubcPrrqGentilicio;
	private String ubcPrrqCodSniese;
	private Integer ubcPrrqSubId;

	// Tabla Periodo_Académico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;

	// Tabla fichaInscripcion
	private int fcinId;
	private Integer fcinUsuarioRol;
	private Integer fcinConfiguracionCarrera;
	private Integer fcinPeriodoAcademico;
	private Timestamp fcinFechaInscripcion;
	private String fcinObservacionIngreso;
	private Integer fcinTipoIngreso; //
	private Integer fcinEstadoIngreso; // homologado, no homologado
	private String fcinObservacion;
	private Integer fcinTipo;
	private Integer fcinMatriculado;
	private Integer fcinEstado;
	private Integer fcinEncuesta;
	private Float fcinNotaEnes;
	private Integer fcinCarrera;
	private String fcinDocumentoIngreso;
	private Integer fcinAnioAbandonaCarrera;
	private int fcinCrrAnteriorId; // carrera anterior o con diseño anterior al
									// reingreso
	private Integer fcinPeriodoPosgradoId;

	// tabla usuario rol
	private int usroId;
	private Integer usroUsuario;
	private Integer usroRol;
	private Integer usroEstado;

	// tabla rol
	private int rolId;
	private String rolDescripcion;
	// tabla usuario
	private int usrId;
	private String usrIdentificacion;
	private String usrNick;
	private Integer usrEstado;
	// Tabla configuracion carrera
	private int cncrId;
	// tabla carrera
	private int crrId;
	private String crrDescripcion;
	private String crrDetalle;
	private Integer crrProceso;

	// tabla rol flujo carrera
	private int roflcrId;
	// tabla dependencia
	private int dpnId;
	private String dpnDescripcion;
	// tabla nivel
	private int nvlId;
	private String nvlDescripcion;

	// tabla ficha docente
	private int fcdcId;

	private FichaMatriculaDto prsFichaMatriculaDto;
	private CarreraDto prsCarreraDto;
	private CalificacionDto prsCalificacionDto;
	private DetalleMatriculaDto prsDetalleMatriculaDto;
	private PeriodoAcademicoDto prsPeriodoAcademicoDto;
	private DependenciaDto prsDependenciaDto;
	private ModalidadDto prsModalidadDto;
	private MateriaDto prsMateriaDto;
	private NivelDto prsNivelDto;
	private ParaleloDto prsParaleloDto;
	private RecordEstudianteDto prsRecordEstudianteDto;

	public PersonaDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getPrsId() {
		return prsId;
	}

	public int getNumeral() {
		return numeral;
	}

	public void setNumeral(int numeral) {
		this.numeral = numeral;
	}

	public CalificacionDto getPrsCalificacionDto() {
		return prsCalificacionDto;
	}

	public void setPrsCalificacionDto(CalificacionDto prsCalificacionDto) {
		this.prsCalificacionDto = prsCalificacionDto;
	}

	public DetalleMatriculaDto getPrsDetalleMatriculaDto() {
		return prsDetalleMatriculaDto;
	}

	public void setPrsDetalleMatriculaDto(DetalleMatriculaDto prsDetalleMatriculaDto) {
		this.prsDetalleMatriculaDto = prsDetalleMatriculaDto;
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

	public Integer getPrsEtnId() {
		return prsEtnId;
	}

	public void setPrsEtnId(Integer prsEtnId) {
		this.prsEtnId = prsEtnId;
	}

	public Integer getPrsUbcId() {
		return prsUbcId;
	}

	public void setPrsUbcId(Integer prsUbcId) {
		this.prsUbcId = prsUbcId;
	}

	public int getEtnId() {
		return etnId;
	}

	public void setEtnId(int etnId) {
		this.etnId = etnId;
	}

	public String getEtnCodigoSniese() {
		return etnCodigoSniese;
	}

	public void setEtnCodigoSniese(String etnCodigoSniese) {
		this.etnCodigoSniese = etnCodigoSniese;
	}

	public String getEtnDescripcion() {
		return etnDescripcion;
	}

	public void setEtnDescripcion(String etnDescripcion) {
		this.etnDescripcion = etnDescripcion;
	}

	public int getUbcPaisId() {
		return ubcPaisId;
	}

	public void setUbcPaisId(int ubcPaisId) {
		this.ubcPaisId = ubcPaisId;
	}

	public String getUbcPaisDescripcion() {
		return ubcPaisDescripcion;
	}

	public void setUbcPaisDescripcion(String ubcPaisDescripcion) {
		this.ubcPaisDescripcion = ubcPaisDescripcion;
	}

	public Integer getUbcPaisJerarquia() {
		return ubcPaisJerarquia;
	}

	public void setUbcPaisJerarquia(Integer ubcPaisJerarquia) {
		this.ubcPaisJerarquia = ubcPaisJerarquia;
	}

	public String getUbcPaisGentilicio() {
		return ubcPaisGentilicio;
	}

	public void setUbcPaisGentilicio(String ubcPaisGentilicio) {
		this.ubcPaisGentilicio = ubcPaisGentilicio;
	}

	public String getUbcPaisCodSniese() {
		return ubcPaisCodSniese;
	}

	public void setUbcPaisCodSniese(String ubcPaisCodSniese) {
		this.ubcPaisCodSniese = ubcPaisCodSniese;
	}

	public Integer getUbcPaisSubId() {
		return ubcPaisSubId;
	}

	public void setUbcPaisSubId(Integer ubcPaisSubId) {
		this.ubcPaisSubId = ubcPaisSubId;
	}

	public int getUbcProvId() {
		return ubcProvId;
	}

	public void setUbcProvId(int ubcProvId) {
		this.ubcProvId = ubcProvId;
	}

	public String getUbcProvDescripcion() {
		return ubcProvDescripcion;
	}

	public void setUbcProvDescripcion(String ubcProvDescripcion) {
		this.ubcProvDescripcion = ubcProvDescripcion;
	}

	public Integer getUbcProvJerarquia() {
		return ubcProvJerarquia;
	}

	public void setUbcProvJerarquia(Integer ubcProvJerarquia) {
		this.ubcProvJerarquia = ubcProvJerarquia;
	}

	public String getUbcProvGentilicio() {
		return ubcProvGentilicio;
	}

	public void setUbcProvGentilicio(String ubcProvGentilicio) {
		this.ubcProvGentilicio = ubcProvGentilicio;
	}

	public String getUbcProvCodSniese() {
		return ubcProvCodSniese;
	}

	public void setUbcProvCodSniese(String ubcProvCodSniese) {
		this.ubcProvCodSniese = ubcProvCodSniese;
	}

	public Integer getUbcProvSubId() {
		return ubcProvSubId;
	}

	public void setUbcProvSubId(Integer ubcProvSubId) {
		this.ubcProvSubId = ubcProvSubId;
	}

	public int getUbcCantonId() {
		return ubcCantonId;
	}

	public void setUbcCantonId(int ubcCantonId) {
		this.ubcCantonId = ubcCantonId;
	}

	public String getUbcCantonDescripcion() {
		return ubcCantonDescripcion;
	}

	public void setUbcCantonDescripcion(String ubcCantonDescripcion) {
		this.ubcCantonDescripcion = ubcCantonDescripcion;
	}

	public Integer getUbcCantonJerarquia() {
		return ubcCantonJerarquia;
	}

	public void setUbcCantonJerarquia(Integer ubcCantonJerarquia) {
		this.ubcCantonJerarquia = ubcCantonJerarquia;
	}

	public String getUbcCantonGentilicio() {
		return ubcCantonGentilicio;
	}

	public void setUbcCantonGentilicio(String ubcCantonGentilicio) {
		this.ubcCantonGentilicio = ubcCantonGentilicio;
	}

	public String getUbcCantonCodSniese() {
		return ubcCantonCodSniese;
	}

	public void setUbcCantonCodSniese(String ubcCantonCodSniese) {
		this.ubcCantonCodSniese = ubcCantonCodSniese;
	}

	public Integer getUbcCantonSubId() {
		return ubcCantonSubId;
	}

	public void setUbcCantonSubId(Integer ubcCantonSubId) {
		this.ubcCantonSubId = ubcCantonSubId;
	}

	public int getUbcPrrqId() {
		return ubcPrrqId;
	}

	public void setUbcPrrqId(int ubcPrrqId) {
		this.ubcPrrqId = ubcPrrqId;
	}

	public String getUbcPrrqDescripcion() {
		return ubcPrrqDescripcion;
	}

	public void setUbcPrrqDescripcion(String ubcPrrqDescripcion) {
		this.ubcPrrqDescripcion = ubcPrrqDescripcion;
	}

	public Integer getUbcPrrqJerarquia() {
		return ubcPrrqJerarquia;
	}

	public void setUbcPrrqJerarquia(Integer ubcPrrqJerarquia) {
		this.ubcPrrqJerarquia = ubcPrrqJerarquia;
	}

	public String getUbcPrrqGentilicio() {
		return ubcPrrqGentilicio;
	}

	public void setUbcPrrqGentilicio(String ubcPrrqGentilicio) {
		this.ubcPrrqGentilicio = ubcPrrqGentilicio;
	}

	public String getUbcPrrqCodSniese() {
		return ubcPrrqCodSniese;
	}

	public void setUbcPrrqCodSniese(String ubcPrrqCodSniese) {
		this.ubcPrrqCodSniese = ubcPrrqCodSniese;
	}

	public Integer getUbcPrrqSubId() {
		return ubcPrrqSubId;
	}

	public void setUbcPrrqSubId(Integer ubcPrrqSubId) {
		this.ubcPrrqSubId = ubcPrrqSubId;
	}

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

	public Integer getFcinEstado() {
		return fcinEstado;
	}

	public void setFcinEstado(Integer fcinEstado) {
		this.fcinEstado = fcinEstado;
	}

	public Integer getFcinEncuesta() {
		return fcinEncuesta;
	}

	public void setFcinEncuesta(Integer fcinEncuesta) {
		this.fcinEncuesta = fcinEncuesta;
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

	public Integer getUsrEstado() {
		return usrEstado;
	}

	public void setUsrEstado(Integer usrEstado) {
		this.usrEstado = usrEstado;
	}

	public Timestamp getFcinFechaInscripcion() {
		return fcinFechaInscripcion;
	}

	public void setFcinFechaInscripcion(Timestamp fcinFechaInscripcion) {
		this.fcinFechaInscripcion = fcinFechaInscripcion;
	}

	public String getFcinObservacion() {
		return fcinObservacion;
	}

	public void setFcinObservacion(String fcinObservacion) {
		this.fcinObservacion = fcinObservacion;
	}

	public Integer getFcinConfiguracionCarrera() {
		return fcinConfiguracionCarrera;
	}

	public void setFcinConfiguracionCarrera(Integer fcinConfiguracionCarrera) {
		this.fcinConfiguracionCarrera = fcinConfiguracionCarrera;
	}

	public Float getFcinNotaEnes() {
		return fcinNotaEnes;
	}

	public void setFcinNotaEnes(Float fcinNotaEnes) {
		this.fcinNotaEnes = fcinNotaEnes;
	}

	public Integer getFcinCarrera() {
		return fcinCarrera;
	}

	public void setFcinCarrera(Integer fcinCarrera) {
		this.fcinCarrera = fcinCarrera;
	}

	public Integer getCncrId() {
		return cncrId;
	}

	public void setCncrId(Integer cncrId) {
		this.cncrId = cncrId;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public int getRolId() {
		return rolId;
	}

	public void setRolId(int rolId) {
		this.rolId = rolId;
	}

	public String getRolDescripcion() {
		return rolDescripcion;
	}

	public void setRolDescripcion(String rolDescripcion) {
		this.rolDescripcion = rolDescripcion;
	}

	public int getRoflcrId() {
		return roflcrId;
	}

	public void setRoflcrId(int roflcrId) {
		this.roflcrId = roflcrId;
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

	public void setPracId(int pracId) {
		this.pracId = pracId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public void setUsroId(int usroId) {
		this.usroId = usroId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public String getPrsApellidosNombres() {
		StringBuilder sb = new StringBuilder();
		sb.append(prsPrimerApellido == null ? "" : prsPrimerApellido + " ");
		sb.append(prsSegundoApellido == null ? "" : prsSegundoApellido + " ");
		sb.append(prsNombres == null ? "" : prsNombres);
		prsApellidosNombres = sb.toString();
		return prsApellidosNombres;
	}

	public void setPrsApellidosNombres(String prsApellidosNombres) {
		this.prsApellidosNombres = prsApellidosNombres;
	}

	public void setCncrId(int cncrId) {
		this.cncrId = cncrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	public String getFcinObservacionIngreso() {
		return fcinObservacionIngreso;
	}

	public void setFcinObservacionIngreso(String fcinObservacionIngreso) {
		this.fcinObservacionIngreso = fcinObservacionIngreso;
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

	public Integer getFcesTipoUnivEstudprev() {
		return fcesTipoUnivEstudprev;
	}

	public void setFcesTipoUnivEstudprev(Integer fcesTipoUnivEstudprev) {
		this.fcesTipoUnivEstudprev = fcesTipoUnivEstudprev;
	}

	public Integer getFcesEstadoEstudprev() {
		return fcesEstadoEstudprev;
	}

	public void setFcesEstadoEstudprev(Integer fcesEstadoEstudprev) {
		this.fcesEstadoEstudprev = fcesEstadoEstudprev;
	}

	public String getFcinDocumentoIngreso() {
		return fcinDocumentoIngreso;
	}

	public void setFcinDocumentoIngreso(String fcinDocumentoIngreso) {
		this.fcinDocumentoIngreso = fcinDocumentoIngreso;
	}

	public Integer getFcesUnivEstudPrevId() {
		return fcesUnivEstudPrevId;
	}

	public void setFcesUnivEstudPrevId(Integer fcesUnivEstudPrevId) {
		this.fcesUnivEstudPrevId = fcesUnivEstudPrevId;
	}

	public Integer getFcesTitEstudPrevId() {
		return fcesTitEstudPrevId;
	}

	public void setFcesTitEstudPrevId(Integer fcesTitEstudPrevId) {
		this.fcesTitEstudPrevId = fcesTitEstudPrevId;
	}

	public String getFcesRegTituloPrev() {
		return fcesRegTituloPrev;
	}

	public int getTmddId() {
		return tmddId;
	}

	public void setTmddId(int tmddId) {
		this.tmddId = tmddId;
	}

	public String getTmddDescripcion() {
		return tmddDescripcion;
	}

	public void setTmddDescripcion(String tmddDescripcion) {
		this.tmddDescripcion = tmddDescripcion;
	}

	public int getRllbId() {
		return rllbId;
	}

	public void setRllbId(int rllbId) {
		this.rllbId = rllbId;
	}

	public String getRllbDescripcion() {
		return rllbDescripcion;
	}

	public void setRllbDescripcion(String rllbDescripcion) {
		this.rllbDescripcion = rllbDescripcion;
	}

	public int getDtpsId() {
		return dtpsId;
	}

	public void setDtpsId(int dtpsId) {
		this.dtpsId = dtpsId;
	}

	public Integer getDtpsCrrTipo() {
		return dtpsCrrTipo;
	}

	public void setDtpsCrrTipo(Integer dtpsCrrTipo) {
		this.dtpsCrrTipo = dtpsCrrTipo;
	}

	public String getPstDenominacion() {
		return pstDenominacion;
	}

	public void setPstDenominacion(String pstDenominacion) {
		this.pstDenominacion = pstDenominacion;
	}

	public void setFcesRegTituloPrev(String fcesRegTituloPrev) {
		this.fcesRegTituloPrev = fcesRegTituloPrev;
	}

	public Integer getFcinAnioAbandonaCarrera() {
		return fcinAnioAbandonaCarrera;
	}

	public void setFcinAnioAbandonaCarrera(Integer fcinAnioAbandonaCarrera) {
		this.fcinAnioAbandonaCarrera = fcinAnioAbandonaCarrera;
	}

	public int getFcinCrrAnteriorId() {
		return fcinCrrAnteriorId;
	}

	public void setFcinCrrAnteriorId(int fcinCrrAnteriorId) {
		this.fcinCrrAnteriorId = fcinCrrAnteriorId;
	}

	public synchronized Integer getFcinPeriodoPosgradoId() {
		return fcinPeriodoPosgradoId;
	}

	public synchronized void setFcinPeriodoPosgradoId(Integer fcinPeriodoPosgradoId) {
		this.fcinPeriodoPosgradoId = fcinPeriodoPosgradoId;
	}

	public String getPrsTelefonoMovil() {
		return prsTelefonoMovil;
	}

	public void setPrsTelefonoMovil(String prsTelefonoMovil) {
		this.prsTelefonoMovil = prsTelefonoMovil;
	}

	public String getPrsDireccionDomicilio() {
		return prsDireccionDomicilio;
	}

	public void setPrsDireccionDomicilio(String prsDireccionDomicilio) {
		this.prsDireccionDomicilio = prsDireccionDomicilio;
	}

	public String getPrsCargo() {
		return prsCargo;
	}

	public void setPrsCargo(String prsCargo) {
		this.prsCargo = prsCargo;
	}

	public Integer getPstNivelRangoGradual() {
		return pstNivelRangoGradual;
	}

	public void setPstNivelRangoGradual(Integer pstNivelRangoGradual) {
		this.pstNivelRangoGradual = pstNivelRangoGradual;
	}

	public int getFcdcId() {
		return fcdcId;
	}

	public void setFcdcId(int fcdcId) {
		this.fcdcId = fcdcId;
	}

	public FichaMatriculaDto getPrsFichaMatriculaDto() {
		return prsFichaMatriculaDto;
	}

	public void setPrsFichaMatriculaDto(FichaMatriculaDto prsFichaMatriculaDto) {
		this.prsFichaMatriculaDto = prsFichaMatriculaDto;
	}

	public CarreraDto getPrsCarreraDto() {
		return prsCarreraDto;
	}

	public void setPrsCarreraDto(CarreraDto prsCarreraDto) {
		this.prsCarreraDto = prsCarreraDto;
	}

	public PeriodoAcademicoDto getPrsPeriodoAcademicoDto() {
		return prsPeriodoAcademicoDto;
	}

	public void setPrsPeriodoAcademicoDto(PeriodoAcademicoDto prsPeriodoAcademicoDto) {
		this.prsPeriodoAcademicoDto = prsPeriodoAcademicoDto;
	}

	public DependenciaDto getPrsDependenciaDto() {
		return prsDependenciaDto;
	}

	public void setPrsDependenciaDto(DependenciaDto prsDependenciaDto) {
		this.prsDependenciaDto = prsDependenciaDto;
	}

	public ModalidadDto getPrsModalidadDto() {
		return prsModalidadDto;
	}

	public void setPrsModalidadDto(ModalidadDto prsModalidadDto) {
		this.prsModalidadDto = prsModalidadDto;
	}

	public MateriaDto getPrsMateriaDto() {
		return prsMateriaDto;
	}

	public void setPrsMateriaDto(MateriaDto prsMateriaDto) {
		this.prsMateriaDto = prsMateriaDto;
	}

	public NivelDto getPrsNivelDto() {
		return prsNivelDto;
	}

	public void setPrsNivelDto(NivelDto prsNivelDto) {
		this.prsNivelDto = prsNivelDto;
	}

	public ParaleloDto getPrsParaleloDto() {
		return prsParaleloDto;
	}

	public void setPrsParaleloDto(ParaleloDto prsParaleloDto) {
		this.prsParaleloDto = prsParaleloDto;
	}

	public RecordEstudianteDto getPrsRecordEstudianteDto() {
		return prsRecordEstudianteDto;
	}

	public void setPrsRecordEstudianteDto(RecordEstudianteDto prsRecordEstudianteDto) {
		this.prsRecordEstudianteDto = prsRecordEstudianteDto;
	}

	public String getPrsApellidosNombresEvaluacion() {
		return prsApellidosNombresEvaluacion;
	}

	public void setPrsApellidosNombresEvaluacion(String prsApellidosNombresEvaluacion) {
		this.prsApellidosNombresEvaluacion = prsApellidosNombresEvaluacion;
	}

	public Integer getPstCategoria() {
		return pstCategoria;
	}

	public void setPstCategoria(Integer pstCategoria) {
		this.pstCategoria = pstCategoria;
	}

	public Integer getCrrProceso() {
		return crrProceso;
	}

	public void setCrrProceso(Integer crrProceso) {
		this.crrProceso = crrProceso;
	}

	@Override
	public String toString() {
		return "PersonaDto [numeral=" + numeral + ", prsId=" + prsId + ", prsTipoIdentificacion="
				+ prsTipoIdentificacion + ", prsTipoIdentificacionSniese=" + prsTipoIdentificacionSniese
				+ ", prsIdentificacion=" + prsIdentificacion + ", prsPrimerApellido=" + prsPrimerApellido
				+ ", prsSegundoApellido=" + prsSegundoApellido + ", prsNombres=" + prsNombres + ", prsSexo=" + prsSexo
				+ ", prsSexoSniese=" + prsSexoSniese + ", prsMailPersonal=" + prsMailPersonal
				+ ", prsMailInstitucional=" + prsMailInstitucional + ", prsTelefono=" + prsTelefono
				+ ", prsTelefonoMovil=" + prsTelefonoMovil + ", prsDireccionDomicilio=" + prsDireccionDomicilio
				+ ", prsFechaNacimiento=" + prsFechaNacimiento + ", prsEtnId=" + prsEtnId + ", prsUbcId=" + prsUbcId
				+ ", prsCargo=" + prsCargo + ", tmddId=" + tmddId + ", tmddDescripcion=" + tmddDescripcion + ", rllbId="
				+ rllbId + ", rllbDescripcion=" + rllbDescripcion + ", dtpsId=" + dtpsId + ", dtpsCrrTipo="
				+ dtpsCrrTipo + ", pstDenominacion=" + pstDenominacion + ", pstNivelRangoGradual="
				+ pstNivelRangoGradual + ", fcesTipoUnivEstudprev=" + fcesTipoUnivEstudprev + ", fcesEstadoEstudprev="
				+ fcesEstadoEstudprev + ", fcesUnivEstudPrevId=" + fcesUnivEstudPrevId + ", fcesTitEstudPrevId="
				+ fcesTitEstudPrevId + ", fcesRegTituloPrev=" + fcesRegTituloPrev + ", etnId=" + etnId
				+ ", etnCodigoSniese=" + etnCodigoSniese + ", etnDescripcion=" + etnDescripcion + ", ubcPaisId="
				+ ubcPaisId + ", ubcPaisDescripcion=" + ubcPaisDescripcion + ", ubcPaisJerarquia=" + ubcPaisJerarquia
				+ ", ubcPaisGentilicio=" + ubcPaisGentilicio + ", ubcPaisCodSniese=" + ubcPaisCodSniese
				+ ", ubcPaisSubId=" + ubcPaisSubId + ", ubcProvId=" + ubcProvId + ", ubcProvDescripcion="
				+ ubcProvDescripcion + ", ubcProvJerarquia=" + ubcProvJerarquia + ", ubcProvGentilicio="
				+ ubcProvGentilicio + ", ubcProvCodSniese=" + ubcProvCodSniese + ", ubcProvSubId=" + ubcProvSubId
				+ ", ubcCantonId=" + ubcCantonId + ", ubcCantonDescripcion=" + ubcCantonDescripcion
				+ ", ubcCantonJerarquia=" + ubcCantonJerarquia + ", ubcCantonGentilicio=" + ubcCantonGentilicio
				+ ", ubcCantonCodSniese=" + ubcCantonCodSniese + ", ubcCantonSubId=" + ubcCantonSubId + ", ubcPrrqId="
				+ ubcPrrqId + ", ubcPrrqDescripcion=" + ubcPrrqDescripcion + ", ubcPrrqJerarquia=" + ubcPrrqJerarquia
				+ ", ubcPrrqGentilicio=" + ubcPrrqGentilicio + ", ubcPrrqCodSniese=" + ubcPrrqCodSniese
				+ ", ubcPrrqSubId=" + ubcPrrqSubId + ", pracId=" + pracId + ", pracDescripcion=" + pracDescripcion
				+ ", pracEstado=" + pracEstado + ", fcinId=" + fcinId + ", fcinUsuarioRol=" + fcinUsuarioRol
				+ ", fcinConfiguracionCarrera=" + fcinConfiguracionCarrera + ", fcinPeriodoAcademico="
				+ fcinPeriodoAcademico + ", fcinFechaInscripcion=" + fcinFechaInscripcion + ", fcinObservacionIngreso="
				+ fcinObservacionIngreso + ", fcinTipoIngreso=" + fcinTipoIngreso + ", fcinEstadoIngreso="
				+ fcinEstadoIngreso + ", fcinObservacion=" + fcinObservacion + ", fcinTipo=" + fcinTipo
				+ ", fcinMatriculado=" + fcinMatriculado + ", fcinEstado=" + fcinEstado + ", fcinEncuesta="
				+ fcinEncuesta + ", fcinNotaEnes=" + fcinNotaEnes + ", fcinCarrera=" + fcinCarrera
				+ ", fcinDocumentoIngreso=" + fcinDocumentoIngreso + ", fcinAnioAbandonaCarrera="
				+ fcinAnioAbandonaCarrera + ", fcinCrrAnteriorId=" + fcinCrrAnteriorId + ", fcinPeriodoPosgradoId="
				+ fcinPeriodoPosgradoId + ", usroId=" + usroId + ", usroUsuario=" + usroUsuario + ", usroRol=" + usroRol
				+ ", usroEstado=" + usroEstado + ", rolId=" + rolId + ", rolDescripcion=" + rolDescripcion + ", usrId="
				+ usrId + ", usrIdentificacion=" + usrIdentificacion + ", usrNick=" + usrNick + ", usrEstado="
				+ usrEstado + ", cncrId=" + cncrId + ", crrId=" + crrId + ", crrDescripcion=" + crrDescripcion
				+ ", crrDetalle=" + crrDetalle + ", roflcrId=" + roflcrId + ", dpnId=" + dpnId + ", dpnDescripcion="
				+ dpnDescripcion + ", nvlId=" + nvlId + ", nvlDescripcion=" + nvlDescripcion + ", fcdcId=" + fcdcId
				+ ", prsFichaMatriculaDto=" + prsFichaMatriculaDto + ", prsCarreraDto=" + prsCarreraDto
				+ ", prsCalificacionDto=" + prsCalificacionDto + ", prsDetalleMatriculaDto=" + prsDetalleMatriculaDto
				+ ", prsPeriodoAcademicoDto=" + prsPeriodoAcademicoDto + ", prsDependenciaDto=" + prsDependenciaDto
				+ ", prsModalidadDto=" + prsModalidadDto + ", prsMateriaDto=" + prsMateriaDto + ", prsNivelDto="
				+ prsNivelDto + ", prsParaleloDto=" + prsParaleloDto + ", prsRecordEstudianteDto="
				+ prsRecordEstudianteDto + "]";
	}

}
