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
   
 ARCHIVO:     RegistroHomologacionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Matricula. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 16-ABR-2018			Marcelo Quishpe   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) RegistroHomologacionDto.
 * DTO encargado de manejar los datos del registro de homologacion. 
 * @author lmquishpei.
 * @version 1.0
 */
public class RegistroHomologacionDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	// Tabla Periodo_Académico
	private int pracId;
	private String pracDescripcion;
	private Integer pracEstado;
	
	//tabla persona
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
	// Usuario
	private int usrId;
	private String usrIdentificacion;
	
	//Usuario_Rol
	private int usroId;
	
	//Rol
	private int rolId;
	private String rolDescripcion;
	
	//Tabla configuracion carrera
	private int cncrId;
	
	//tabla carrera
	private int crrId;
	private Integer crrDependencia;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Date crrFechaCreacion;
	private String crrResolucion;
	private Integer crrTipo;
	private Integer crrEspeCodigo;


	//comprobante de pago
	private int cmpaId;
	
	//Gratuidad
	private int grtId;
	private Integer grtEstado;
	
	
	// Tabla Ficha_Matricula
	private int fcmtId;
	private Integer fcmtEstado;
	private Date fcmtFechaConvalidacion;
	private Integer fcmtTipo;
	private Integer fcmtModalidad;
	private BigDecimal fcmtValorTotal;
	private Timestamp fcmtFechaMatricula;
	private Integer fcmtPracId;
	

	//Tabla FichaEstudiante
	private int fcesId;
	private Integer fcesInstitucionAcademica;
	private Date fcesFechaInicio;
	private String fcesCrrEstudPrevios;
	private Integer fcesRecEstudPrevios;
	private Timestamp fcesFechaCreacion;
	private String fcesObservacion;
	private Integer fcesTipoUnivEstudPrev;
	private Integer fcesEstadoEstudPrev;
	private Integer fcesUnivEstudPrevId;  
	private Integer fcesTitEstudPrevId;
	private String fcesRegTituloPrev;

	//Tabla fichaInscripcion
	private int fcinId;
	private Timestamp fcinFechaInscripcion;
	private String fcinObservacion;
	private String fcinObservacionIngreso;
	private Integer fcinTipo;
	private Integer fcinMatriculado;
	private Integer fcinEstado;
	private Integer fcinEstadoRetiro;
	private Integer fcinTipoModalidad;
	private Integer fcinNumMaximoCreditos;
	private Integer fcinTipoIngreso;   //Reingreso-CambioCrr-CambioUniv-Reinicio
	private Integer fcinEstadoIngreso;  //Homologa o no homologa
	private Integer fcinNivelIngreso;
	private Integer fcinEncuesta;
	private Float fcinNotaEnes;
	private Integer fcinCarrera;
	private Integer fcinCarreraSiiu;
	private Integer fcinCncrArea;
	private Integer fcinFcinNivelacion;
	private String fcinDocumentoIngreso;
	private Integer fcinAnioAbandonaCarrera;
	private Integer fcinCrrAnteriorId;
	private Integer fcinVigente;
	private Integer fcinPeriodoAcademico;
	private Integer fcinFcinAnteriorId;
	private Integer fcinAplicaNotaEnes;  // 0, 1 
	private Integer fcinNotaCorteId;
	
	
	//campos auxiliares
	
	private Boolean deshabilitaReingreso;
	private Boolean deshabilitaEliminar;
	
	
	public RegistroHomologacionDto() {
	
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

	public int getFcmtId() {
		return fcmtId;
	}

	public void setFcmtId(int fcmtId) {
		this.fcmtId = fcmtId;
	}

	public Integer getFcmtEstado() {
		return fcmtEstado;
	}

	public void setFcmtEstado(Integer fcmtEstado) {
		this.fcmtEstado = fcmtEstado;
	}

	public Date getFcmtFechaConvalidacion() {
		return fcmtFechaConvalidacion;
	}

	public void setFcmtFechaConvalidacion(Date fcmtFechaConvalidacion) {
		this.fcmtFechaConvalidacion = fcmtFechaConvalidacion;
	}

	public Integer getFcmtTipo() {
		return fcmtTipo;
	}

	public void setFcmtTipo(Integer fcmtTipo) {
		this.fcmtTipo = fcmtTipo;
	}

	public Integer getFcmtModalidad() {
		return fcmtModalidad;
	}

	public void setFcmtModalidad(Integer fcmtModalidad) {
		this.fcmtModalidad = fcmtModalidad;
	}

	public BigDecimal getFcmtValorTotal() {
		return fcmtValorTotal;
	}

	public void setFcmtValorTotal(BigDecimal fcmtValorTotal) {
		this.fcmtValorTotal = fcmtValorTotal;
	}

	public Timestamp getFcmtFechaMatricula() {
		return fcmtFechaMatricula;
	}

	public void setFcmtFechaMatricula(Timestamp fcmtFechaMatricula) {
		this.fcmtFechaMatricula = fcmtFechaMatricula;
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
	
	public int getFcesId() {
		return fcesId;
	}

	public void setFcesId(int fcesId) {
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

	public String getFcesCrrEstudPrevios() {
		return fcesCrrEstudPrevios;
	}

	public void setFcesCrrEstudPrevios(String fcesCrrEstudPrevios) {
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
	}
	
	public Integer getFcesRecEstudPrevios() {
		return fcesRecEstudPrevios;
	}

	public void setFcesRecEstudPrevios(Integer fcesRecEstudPrevios) {
		this.fcesRecEstudPrevios = fcesRecEstudPrevios;
	}

	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}

	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}

	public String getFcesObservacion() {
		return fcesObservacion;
	}

	public void setFcesObservacion(String fcesObservacion) {
		this.fcesObservacion = fcesObservacion;
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
	
	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
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

	public int getCncrId() {
		return cncrId;
	}

	public void setCncrId(int cncrId) {
		this.cncrId = cncrId;
	}

	public int getCrrId() {
		return crrId;
	}

	public void setCrrId(int crrId) {
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

	public Integer getCrrTipo() {
		return crrTipo;
	}

	public void setCrrTipo(Integer crrTipo) {
		this.crrTipo = crrTipo;
	}

	

	public Integer getFcinCncrArea() {
		return fcinCncrArea;
	}

	public void setFcinCncrArea(Integer fcinCncrArea) {
		this.fcinCncrArea = fcinCncrArea;
	}
	
	public Integer getFcinCarreraSiiu() {
		return fcinCarreraSiiu;
	}

	public void setFcinCarreraSiiu(Integer fcinCarreraSiiu) {
		this.fcinCarreraSiiu = fcinCarreraSiiu;
	}

	public int getCmpaId() {
		return cmpaId;
	}

	public void setCmpaId(int cmpaId) {
		this.cmpaId = cmpaId;
	}
	
	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}
	
	

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public String getUsrIdentificacion() {
		return usrIdentificacion;
	}

	public void setUsrIdentificacion(String usrIdentificacion) {
		this.usrIdentificacion = usrIdentificacion;
	}

	public int getUsroId() {
		return usroId;
	}

	public void setUsroId(int usroId) {
		this.usroId = usroId;
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

	public int getGrtId() {
		return grtId;
	}

	public void setGrtId(int grtId) {
		this.grtId = grtId;
	}

	public Integer getGrtEstado() {
		return grtEstado;
	}

	public void setGrtEstado(Integer grtEstado) {
		this.grtEstado = grtEstado;
	}

	public Integer getFcmtPracId() {
		return fcmtPracId;
	}

	public void setFcmtPracId(Integer fcmtPracId) {
		this.fcmtPracId = fcmtPracId;
	}

	public Integer getFcesTipoUnivEstudPrev() {
		return fcesTipoUnivEstudPrev;
	}

	public void setFcesTipoUnivEstudPrev(Integer fcesTipoUnivEstudPrev) {
		this.fcesTipoUnivEstudPrev = fcesTipoUnivEstudPrev;
	}

	public Integer getFcesEstadoEstudPrev() {
		return fcesEstadoEstudPrev;
	}

	public void setFcesEstadoEstudPrev(Integer fcesEstadoEstudPrev) {
		this.fcesEstadoEstudPrev = fcesEstadoEstudPrev;
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

	public void setFcesRegTituloPrev(String fcesRegTituloPrev) {
		this.fcesRegTituloPrev = fcesRegTituloPrev;
	}

	public String getFcinObservacionIngreso() {
		return fcinObservacionIngreso;
	}

	public void setFcinObservacionIngreso(String fcinObservacionIngreso) {
		this.fcinObservacionIngreso = fcinObservacionIngreso;
	}

	public Integer getFcinEstado() {
		return fcinEstado;
	}

	public void setFcinEstado(Integer fcinEstado) {
		this.fcinEstado = fcinEstado;
	}

	public Integer getFcinEstadoRetiro() {
		return fcinEstadoRetiro;
	}

	public void setFcinEstadoRetiro(Integer fcinEstadoRetiro) {
		this.fcinEstadoRetiro = fcinEstadoRetiro;
	}

	public Integer getFcinTipoModalidad() {
		return fcinTipoModalidad;
	}

	public void setFcinTipoModalidad(Integer fcinTipoModalidad) {
		this.fcinTipoModalidad = fcinTipoModalidad;
	}

	public Integer getFcinNumMaximoCreditos() {
		return fcinNumMaximoCreditos;
	}

	public void setFcinNumMaximoCreditos(Integer fcinNumMaximoCreditos) {
		this.fcinNumMaximoCreditos = fcinNumMaximoCreditos;
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

	public Integer getFcinNivelIngreso() {
		return fcinNivelIngreso;
	}

	public void setFcinNivelIngreso(Integer fcinNivelIngreso) {
		this.fcinNivelIngreso = fcinNivelIngreso;
	}

	public Integer getFcinEncuesta() {
		return fcinEncuesta;
	}

	public void setFcinEncuesta(Integer fcinEncuesta) {
		this.fcinEncuesta = fcinEncuesta;
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

	public Integer getFcinFcinNivelacion() {
		return fcinFcinNivelacion;
	}

	public void setFcinFcinNivelacion(Integer fcinFcinNivelacion) {
		this.fcinFcinNivelacion = fcinFcinNivelacion;
	}

	public String getFcinDocumentoIngreso() {
		return fcinDocumentoIngreso;
	}

	public void setFcinDocumentoIngreso(String fcinDocumentoIngreso) {
		this.fcinDocumentoIngreso = fcinDocumentoIngreso;
	}

	public Integer getFcinAnioAbandonaCarrera() {
		return fcinAnioAbandonaCarrera;
	}

	public void setFcinAnioAbandonaCarrera(Integer fcinAnioAbandonaCarrera) {
		this.fcinAnioAbandonaCarrera = fcinAnioAbandonaCarrera;
	}

	public Integer getFcinCrrAnteriorId() {
		return fcinCrrAnteriorId;
	}

	public void setFcinCrrAnteriorId(Integer fcinCrrAnteriorId) {
		this.fcinCrrAnteriorId = fcinCrrAnteriorId;
	}

	
	public Integer getFcinVigente() {
		return fcinVigente;
	}

	public void setFcinVigente(Integer fcinVigente) {
		this.fcinVigente = fcinVigente;
	}

	
	

	public Boolean getDeshabilitaReingreso() {
		return deshabilitaReingreso;
	}

	public void setDeshabilitaReingreso(Boolean deshabilitaReingreso) {
		this.deshabilitaReingreso = deshabilitaReingreso;
	}

	
	
	public Boolean getDeshabilitaEliminar() {
		return deshabilitaEliminar;
	}

	public void setDeshabilitaEliminar(Boolean deshabilitaEliminar) {
		this.deshabilitaEliminar = deshabilitaEliminar;
	}

	public Integer getFcinPeriodoAcademico() {
		return fcinPeriodoAcademico;
	}

	public void setFcinPeriodoAcademico(Integer fcinPeriodoAcademico) {
		this.fcinPeriodoAcademico = fcinPeriodoAcademico;
	}
	
	

	public Integer getFcinFcinAnteriorId() {
		return fcinFcinAnteriorId;
	}

	public void setFcinFcinAnteriorId(Integer fcinFcinAnteriorId) {
		this.fcinFcinAnteriorId = fcinFcinAnteriorId;
	}

	public Integer getFcinAplicaNotaEnes() {
		return fcinAplicaNotaEnes;
	}

	public void setFcinAplicaNotaEnes(Integer fcinAplicaNotaEnes) {
		this.fcinAplicaNotaEnes = fcinAplicaNotaEnes;
	}
	
	

	public Integer getFcinNotaCorteId() {
		return fcinNotaCorteId;
	}

	public void setFcinNotaCorteId(Integer fcinNotaCorteId) {
		this.fcinNotaCorteId = fcinNotaCorteId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append(" Matricula ");
		sb.append(tabulador + "fcmtId : " + fcmtId);
		sb.append(tabulador + "fcmtEstado : " + (fcmtEstado==null? "NULL":fcmtEstado));
		sb.append(tabulador + "fcmtFechaConvalidacion : " + (fcmtFechaConvalidacion==null? "NULL":fcmtFechaConvalidacion));
		sb.append(tabulador + "fcmtTipo : " + (fcmtTipo==null? "NULL":fcmtTipo));
		sb.append(tabulador + "fcmtModalidad : " + (fcmtModalidad==null? "NULL":fcmtModalidad));
		sb.append(tabulador + "fcmtValorTotal : " + (fcmtValorTotal==null? "NULL":fcmtValorTotal));
		sb.append(tabulador + "fcmtFechaMatricula : " + (fcmtFechaMatricula==null? "NULL":fcmtFechaMatricula));
		sb.append(tabulador + "fcmtPracId : " + (fcmtPracId==null? "NULL":fcmtPracId));
	
		sb.append(" PeriodoAcadémico ");
		sb.append(tabulador + "pracId : " + pracId);
		sb.append(tabulador + "pracDescripcion : " + (pracDescripcion==null? "NULL":pracDescripcion));
		sb.append(tabulador + "pracEstado : " + (pracEstado==null? "NULL":pracEstado));
		
		sb.append(" FichaEstudiante ");
		sb.append(tabulador + "fcesId : " + fcesId);
		sb.append(tabulador + "fcesInstitucionAcademica : " + (fcesInstitucionAcademica==null? "NULL":fcesInstitucionAcademica));
		sb.append(tabulador + "fcesFechaInicio : " + (fcesFechaInicio==null? "NULL":fcesFechaInicio));
		sb.append(tabulador + "fcesCrrEstudPrevios : " + (fcesCrrEstudPrevios==null? "NULL":fcesCrrEstudPrevios));
		sb.append(tabulador + "fcesRecEstudPrevios : " + (fcesRecEstudPrevios==null? "NULL":fcesRecEstudPrevios));
		sb.append(tabulador + "fcesFechaCreacion : " + (fcesFechaCreacion==null? "NULL":fcesFechaCreacion));
		sb.append(tabulador + "fcesObservacion : " + (fcesObservacion==null? "NULL":fcesObservacion));
		sb.append(tabulador + "fcesTipoUnivEstudPrev : " + (fcesTipoUnivEstudPrev==null? "NULL":fcesTipoUnivEstudPrev));
		sb.append(tabulador + "fcesEstadoEstudPrev : " + (fcesEstadoEstudPrev==null? "NULL":fcesEstadoEstudPrev));
		sb.append(tabulador + "fcesUnivEstudPrevId : " + (fcesUnivEstudPrevId==null? "NULL":fcesUnivEstudPrevId));
		sb.append(tabulador + "fcesTitEstudPrevId : " + (fcesTitEstudPrevId==null? "NULL":fcesTitEstudPrevId));
		sb.append(tabulador + "fcesRegTituloPrev : " + (fcesRegTituloPrev==null? "NULL":fcesRegTituloPrev));
		
		
		sb.append(" Persona ");
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
	
		sb.append(" FichaInscripcion ");
		sb.append(tabulador + "fcinId : " + fcinId);
		sb.append(tabulador + "fcinFechaInscripcion : " + (fcinFechaInscripcion==null? "NULL":fcinFechaInscripcion));
		sb.append(tabulador + "fcinObservacion : " + (fcinObservacion==null? "NULL":fcinObservacion));
		sb.append(tabulador + "fcinTipo : " + (fcinTipo==null? "NULL":fcinTipo));
		sb.append(tabulador + "fcinMatriculado : " + (fcinMatriculado==null? "NULL":fcinMatriculado));
		sb.append(tabulador + "fcinEstado : " + (fcinEstado==null? "NULL":fcinEstado));
		sb.append(tabulador + "fcinEstadoRetiro : " + (fcinEstadoRetiro==null? "NULL":fcinEstadoRetiro));
		sb.append(tabulador + "fcinTipoModalidad : " + (fcinTipoModalidad==null? "NULL":fcinTipoModalidad));
		sb.append(tabulador + "fcinNumMaximoCreditos : " + (fcinNumMaximoCreditos==null? "NULL":fcinNumMaximoCreditos));
		sb.append(tabulador + "fcinTipoIngreso : " + (fcinTipoIngreso==null? "NULL":fcinTipoIngreso));
		sb.append(tabulador + "fcinEstadoIngreso : " + (fcinEstadoIngreso==null? "NULL":fcinEstadoIngreso));
		sb.append(tabulador + "fcinNivelIngreso : " + (fcinNivelIngreso==null? "NULL":fcinNivelIngreso));
		sb.append(tabulador + "fcinEncuesta : " + (fcinEncuesta==null? "NULL":fcinEncuesta));
		sb.append(tabulador + "fcinNotaEnes : " + (fcinNotaEnes==null? "NULL":fcinNotaEnes));
		sb.append(tabulador + "fcinCarrera : " + (fcinCarrera==null? "NULL":fcinCarrera));
		sb.append(tabulador + "fcinObservacionIngreso : " + (fcinObservacionIngreso==null? "NULL":fcinObservacionIngreso));
		sb.append(tabulador + "fcinFcinNivelacion : " + (fcinFcinNivelacion==null? "NULL":fcinFcinNivelacion));
		sb.append(tabulador + "fcinDocumentoIngreso : " + (fcinDocumentoIngreso==null? "NULL":fcinDocumentoIngreso));
		sb.append(tabulador + "fcinAnioAbandonaCarrera : " + (fcinAnioAbandonaCarrera==null? "NULL":fcinAnioAbandonaCarrera));
		sb.append(tabulador + "fcinCrrAnteriorId : " + (fcinCrrAnteriorId==null? "NULL":fcinCrrAnteriorId));
		sb.append(tabulador + "fcinVigente : " + (fcinVigente==null? "NULL":fcinVigente));
		sb.append(tabulador + "fcinPeriodoAcademico : " + (fcinPeriodoAcademico==null? "NULL":fcinPeriodoAcademico));
		sb.append(tabulador + "fcinFcinAnteriorId : " + (fcinFcinAnteriorId==null? "NULL":fcinFcinAnteriorId));
		sb.append(tabulador + "fcinAplicaNotaEnes : " + (fcinAplicaNotaEnes==null? "NULL":fcinAplicaNotaEnes));
		sb.append(tabulador + "fcinNotaCorteId : " + (fcinNotaCorteId==null? "NULL":fcinNotaCorteId));
				
		sb.append(" ConfiguracionCarrera ");
		sb.append(tabulador + "cncrId : " + cncrId);
			
		sb.append(" Carrera ");
		sb.append(tabulador + "crrId : " + crrId);
		sb.append(tabulador + "crrDependencia : " + (crrDependencia==null? "NULL":crrDependencia));
		sb.append(tabulador + "crrDescripcion : " + (crrDescripcion==null? "NULL":crrDescripcion));
		sb.append(tabulador + "crrCodSniese : " + (crrCodSniese==null? "NULL":crrCodSniese));
		sb.append(tabulador + "crrDetalle : " + (crrDetalle==null? "NULL":crrDetalle));
		sb.append(tabulador + "crrFechaCreacion : " + (crrFechaCreacion==null? "NULL":crrFechaCreacion));
		sb.append(tabulador + "crrResolucion : " + (crrResolucion==null? "NULL":crrResolucion));
		sb.append(tabulador + "crrTipo : " + (crrTipo==null? "NULL":crrTipo));
		sb.append(tabulador + "crrEspeCodigo : " + (crrEspeCodigo==null? "NULL":crrEspeCodigo));
	
		sb.append(" Comprobante de pago ");
		sb.append(tabulador + "cmpaId : " + cmpaId);

		sb.append(" Gratuidad ");
		sb.append(tabulador + "grtId : " + grtId);
		sb.append(tabulador + "grtEstado : " + (grtEstado==null? "NULL":grtEstado));
		
		sb.append(" Usuario ");
		sb.append(tabulador + "usrId : " + usrId);
		sb.append(tabulador + "usrIdentificacion : " + (usrIdentificacion==null? "NULL":usrIdentificacion));
		
		sb.append(" Usuario_Rol ");
		sb.append(tabulador + "usroId : " + usroId);
	
		sb.append(" Rol ");
		sb.append(tabulador + "rolId : " + rolId);
		sb.append(tabulador + "rolDescripcion : " + (rolDescripcion==null? "NULL":rolDescripcion));
		
		sb.append(tabulador + "deshabilitaReingreso : " + (deshabilitaReingreso==null? "NULL":deshabilitaReingreso));
		sb.append(tabulador + "deshabilitaEliminar : " + (deshabilitaEliminar==null? "NULL":deshabilitaEliminar));

		return sb.toString();
    }

}
