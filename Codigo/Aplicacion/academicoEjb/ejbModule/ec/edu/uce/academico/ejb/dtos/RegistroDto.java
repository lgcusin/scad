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

 ARCHIVO:     RegistroDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de las entidades que manejan el registro. 
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 09-05-2017 			David Arellano  		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ec.edu.uce.academico.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.PeriodoAcademico;

/**
 * Clase (DTO) RegistroDto.
 * DTO encargado de manejar los datos de las entidades que manejan el registro. 
 * @author darellano.
 * @version 1.0
 */
public class RegistroDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/

	//Tabla usuario (a usuario le llega la persona)
	private int usrId;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;
	private Timestamp usrFechaCreacion;
	private Date usrFechaCaducidad;
	private Date usrFechaCadPass;
	private Integer usrEstado;
	private Integer usrEstSesion;
	private Integer usrActiveDirectory;
	
	//Tabla Persona (a la persona le llega la uicacion y la etnia)
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
	
	//Tabla UsuarioRol
	private int usroId;
	private Integer usroRol;
	private Integer usroUsuario;
	private Integer usroEstado;
	
	
	//Tabla FichaMatricula
	private Integer fcmtNivelUbicacion;
	
	
	//Datos adicionales del registro
	private Integer rgdArea;
	private Integer rgdNuevo; // 0.- SI  1.-NO
	
	//FichaInscripcion
	private int fcinId;
	private Integer fcinUsuarioRol;
	private Integer fcinConfiguracionCarrera;
	private Integer fcinPeriodoAcademico;
	private Timestamp fcinFechaInscripcion;
	private String fcinObservacion;
	private Integer fcinTipo;
	private Integer fcinTipoIngreso; // 0:REINGRESO 1:CAMBIO CARRERA 2:CAMBIO UNIVERSIDAD 3:REINICIO 4:SEGUNDA CARRERA	5:CAMBIO MALLA
	private Integer fcinTipoUniversidad;
	private Integer fcinMatriculado;
	private Integer fcinEstado;
	private Integer fcinEncuesta;
	private Float fcinNotaEnes;
	private Integer fcinCarrera;
	private Integer fcinCarreraSiiu;
	private BigDecimal fcinNotaUbicacion;
	private Integer fcinNotaCorteId;
	
	//entidades creadas para la insericon por JPA
	private ConfiguracionCarrera configuracioCarrera; 
	private PeriodoAcademico periodoAcademico;
	
	//lista de materias	
	private List<MateriaDto> listaMaterias;

	public RegistroDto() {
	}

	/*******************************************************/
	/***************** Métodos Getter y Setter *************/
	/*******************************************************/

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

	public int getUsroId() {
		return usroId;
	}

	public void setUsroId(int usroId) {
		this.usroId = usroId;
	}

	public Integer getUsroRol() {
		return usroRol;
	}

	public void setUsroRol(Integer usroRol) {
		this.usroRol = usroRol;
	}

	public Integer getUsroUsuario() {
		return usroUsuario;
	}

	public void setUsroUsuario(Integer usroUsuario) {
		this.usroUsuario = usroUsuario;
	}

	public Integer getUsroEstado() {
		return usroEstado;
	}

	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}
	
	public Integer getRgdArea() {
		return rgdArea;
	}

	public void setRgdArea(Integer rgdArea) {
		this.rgdArea = rgdArea;
	}

	public Integer getRgdNuevo() {
		return rgdNuevo;
	}

	public void setRgdNuevo(Integer rgdNuevo) {
		this.rgdNuevo = rgdNuevo;
	}

	public int getFcinId() {
		return fcinId;
	}

	public void setFcinId(int fcinId) {
		this.fcinId = fcinId;
	}

	public Integer getFcinUsuarioRol() {
		return fcinUsuarioRol;
	}

	public void setFcinUsuarioRol(Integer fcinUsuarioRol) {
		this.fcinUsuarioRol = fcinUsuarioRol;
	}

	public Integer getFcinConfiguracionCarrera() {
		return fcinConfiguracionCarrera;
	}

	public void setFcinConfiguracionCarrera(Integer fcinConfiguracionCarrera) {
		this.fcinConfiguracionCarrera = fcinConfiguracionCarrera;
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

	public ConfiguracionCarrera getConfiguracioCarrera() {
		return configuracioCarrera;
	}

	public void setConfiguracioCarrera(ConfiguracionCarrera configuracioCarrera) {
		this.configuracioCarrera = configuracioCarrera;
	}

	public PeriodoAcademico getPeriodoAcademico() {
		return periodoAcademico;
	}

	public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
		this.periodoAcademico = periodoAcademico;
	}

	public Integer getFcinCarreraSiiu() {
		return fcinCarreraSiiu;
	}

	public void setFcinCarreraSiiu(Integer fcinCarreraSiiu) {
		this.fcinCarreraSiiu = fcinCarreraSiiu;
	}

	public Integer getFcinTipoIngreso() {
		return fcinTipoIngreso;
	}

	public void setFcinTipoIngreso(Integer fcinTipoIngreso) {
		this.fcinTipoIngreso = fcinTipoIngreso;
	}
	
	

	public Integer getFcinTipoUniversidad() {
		return fcinTipoUniversidad;
	}

	public void setFcinTipoUniversidad(Integer fcinTipoUniversidad) {
		this.fcinTipoUniversidad = fcinTipoUniversidad;
	}

	
	public List<MateriaDto> getListaMaterias() {
	
		return listaMaterias;
	}

	public void setListaMaterias(List<MateriaDto> listaMaterias) {
		
		this.listaMaterias = listaMaterias;
	}

	
	public BigDecimal getFcinNotaUbicacion() {
		return fcinNotaUbicacion;
	}

	public void setFcinNotaUbicacion(BigDecimal fcinNotaUbicacion) {
		this.fcinNotaUbicacion = fcinNotaUbicacion;
	}
	
	

	public Integer getFcmtNivelUbicacion() {
		return fcmtNivelUbicacion;
	}

	public void setFcmtNivelUbicacion(Integer fcmtNivelUbicacion) {
		this.fcmtNivelUbicacion = fcmtNivelUbicacion;
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
		
		sb.append(tabulador + "usrId : " + usrId);
		sb.append(tabulador + "usrIdentificacion : " + (usrIdentificacion==null? "NULL":usrIdentificacion));
		sb.append(tabulador + "usrNick : " + (usrNick==null? "NULL":usrNick));
		sb.append(tabulador + "usrPassword : " + (usrPassword==null? "NULL":usrPassword));
		sb.append(tabulador + "usrFechaCreacion : " + (usrFechaCreacion==null? "NULL":usrFechaCreacion));
		sb.append(tabulador + "usrFechaCaducidad : " + (usrFechaCaducidad==null? "NULL":usrFechaCaducidad));
		sb.append(tabulador + "usrFechaCadPass : " + (usrFechaCadPass==null? "NULL":usrFechaCadPass));
		sb.append(tabulador + "usrEstado : " + (usrEstado==null? "NULL":usrEstado));
		sb.append(tabulador + "usrEstSesion : " + (usrEstSesion==null? "NULL":usrEstSesion));
		sb.append(tabulador + "usrActiveDirectory : " + (usrActiveDirectory==null? "NULL":usrActiveDirectory));
		
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

		sb.append(tabulador + "ubcPaisId : " + ubcPaisId);
		sb.append(tabulador + "ubcPaisDescripcion : " + (ubcPaisDescripcion==null? "NULL":ubcPaisDescripcion));
		sb.append(tabulador + "ubcCantonId : " + ubcCantonId);
		sb.append(tabulador + "ubcCantonDescripcion : " + (ubcCantonDescripcion==null? "NULL":ubcCantonDescripcion));
		
		sb.append(tabulador + "etnId : " + etnId);
		sb.append(tabulador + "etnDescripcion : " + (etnDescripcion==null? "NULL":etnDescripcion));

		sb.append(tabulador + "usroId : " + usroId);
		sb.append(tabulador + "usroRol : " + (usroRol==null? "NULL":usroRol));
		sb.append(tabulador + "usroUsuario : " + (usroUsuario==null? "NULL":usroUsuario));
		sb.append(tabulador + "usroEstado : " + (usroEstado==null? "NULL":usroEstado));
		
		sb.append(tabulador + "rgdArea : " + (rgdArea==null? "NULL":rgdArea));
		sb.append(tabulador + "rgdNuevo : " + (rgdNuevo==null? "NULL":rgdNuevo));
		
		sb.append(tabulador + "fcinId : " + fcinId);
		sb.append(tabulador + "fcinUsuarioRol : " + (fcinUsuarioRol==null? "NULL":fcinUsuarioRol));
		sb.append(tabulador + "fcinConfiguracionCarrera : " + (fcinConfiguracionCarrera==null? "NULL":fcinConfiguracionCarrera));
		sb.append(tabulador + "fcinPeriodoAcademico : " + (fcinPeriodoAcademico==null? "NULL":fcinPeriodoAcademico));
		sb.append(tabulador + "fcinFechaInscripcion : " + (fcinFechaInscripcion==null? "NULL":fcinFechaInscripcion));
		sb.append(tabulador + "fcinObservacion : " + (fcinObservacion==null? "NULL":fcinObservacion));
		sb.append(tabulador + "fcinTipo : " + (fcinTipo==null? "NULL":fcinTipo));
		sb.append(tabulador + "fcinMatriculado : " + (fcinMatriculado==null? "NULL":fcinMatriculado));
		sb.append(tabulador + "fcinEstado : " + (fcinEstado==null? "NULL":fcinEstado));
		sb.append(tabulador + "fcinEncuesta : " + (fcinEncuesta==null? "NULL":fcinEncuesta));
		sb.append(tabulador + "fcinNotaEnes : " + (fcinNotaEnes==null? "NULL":fcinNotaEnes));
		sb.append(tabulador + "fcinCarrera : " + (fcinCarrera==null? "NULL":fcinCarrera));
		sb.append(tabulador + "fcinTipoIngreso : " + (fcinTipoIngreso==null? "NULL":fcinTipoIngreso));
		sb.append(tabulador + "fcinTipoUniversidad : " + (fcinTipoUniversidad==null? "NULL":fcinTipoUniversidad));
		sb.append(tabulador + "fcinNotaUbicacion : " + fcinNotaUbicacion);
		sb.append(tabulador + "fcinNotaCorteId : " + (fcinNotaCorteId==null? "NULL":fcinNotaCorteId));
		
		sb.append("Lista Materias:");
		for (MateriaDto materiaDto : listaMaterias) {
			sb.append(tabulador + "materiaDto : " + materiaDto + "\n");
		}
		
		sb.append(tabulador + "fcmtNivelUbicacion : " + (fcmtNivelUbicacion==null? "NULL":fcmtNivelUbicacion));
		
		return sb.toString();
	}

	


}
