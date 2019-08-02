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
   
 ARCHIVO:     UsuarioRolJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del usuario, rol y rol_flujo_carrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-FEBRERO-2016 		 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import ec.edu.uce.academico.jpa.entidades.publico.Ubicacion;

/**
 * Clase (DTO) UsuarioRolJdbcDto.
 * DTO encargado de manejar los datos del usuario, rol y rol_flujo_carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class UsuarioRolJdbcDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** UBICACION *****************************
	private int ubcId;
	private String ubcDescripcion;
	private Integer ubcJerarquia;
	private String ubcGentilicio;
	private String ubcCodSniese;
	private Ubicacion ubcPadre;
	
	//****************** ETNIA *****************************
	private Integer etnId;
	private String etnDescripcion;
	private String etnCodigoSniese;
	
	//****************** PERSONA *****************************
	private Integer prsId;
	private Integer prsTipoIdentificacion;
	private String prsTipoIdentificacionSt;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private Integer prsSexo;
	private String prsSexoSt;
	private Integer prsSexoSniese;
	private Integer prsEtnId;
	private Integer prsUbcId;
		
	//****************** CARRRERA *****************************
	private Integer crrId;
	private String crrDescripcion;
	private String crrDetalle;
	private String crrCodSniese;
	
	private String dpnDescripcion;
	
	public String getCrrCodSniese() {
		return crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	//****************** FACULTAD *****************************
	private Integer fclId;
	private String fclDescripcion;
	
	//****************** ROL **********************************
	private Integer rolId;
	private String rolDescripcion;
	private Integer rolTipo;
	private String rolDetalle;

	//****************** USUARIO *****************************
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
	
	//****************** USUARIO_ROL **************************
	private Integer usroId;
	private Integer usroEstado;
	
	//****************** ROL_FLUJO_CARRERA ********************
	private Integer roflcrId;
	private Integer roflcrEstado;
		
	public UsuarioRolJdbcDto(){
	
	}

	public int getUbcId() {
		return ubcId;
	}

	public void setUbcId(int ubcId) {
		this.ubcId = ubcId;
	}

	public String getUbcDescripcion() {
		return ubcDescripcion;
	}

	public void setUbcDescripcion(String ubcDescripcion) {
		this.ubcDescripcion = ubcDescripcion;
	}

	public Integer getUbcJerarquia() {
		return ubcJerarquia;
	}

	public void setUbcJerarquia(Integer ubcJerarquia) {
		this.ubcJerarquia = ubcJerarquia;
	}

	public String getUbcGentilicio() {
		return ubcGentilicio;
	}

	public void setUbcGentilicio(String ubcGentilicio) {
		this.ubcGentilicio = ubcGentilicio;
	}

	public String getUbcCodSniese() {
		return ubcCodSniese;
	}

	public void setUbcCodSniese(String ubcCodSniese) {
		this.ubcCodSniese = ubcCodSniese;
	}

	public Ubicacion getUbcPadre() {
		return ubcPadre;
	}

	public void setUbcPadre(Ubicacion ubcPadre) {
		this.ubcPadre = ubcPadre;
	}

	public Integer getEtnId() {
		return etnId;
	}

	public void setEtnId(Integer etnId) {
		this.etnId = etnId;
	}

	public String getEtnDescripcion() {
		return etnDescripcion;
	}

	public void setEtnDescripcion(String etnDescripcion) {
		this.etnDescripcion = etnDescripcion;
	}

	
	public String getEtnCodigoSniese() {
		return etnCodigoSniese;
	}

	public void setEtnCodigoSniese(String etnCodigoSniese) {
		this.etnCodigoSniese = etnCodigoSniese;
	}

	public Integer getPrsId() {
		return prsId;
	}

	public void setPrsId(Integer prsId) {
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

	public Integer getFclId() {
		return fclId;
	}

	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}

	public String getFclDescripcion() {
		return fclDescripcion;
	}

	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}

	public Integer getRolId() {
		return rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
	}

	public String getRolDescripcion() {
		return rolDescripcion;
	}

	public void setRolDescripcion(String rolDescripcion) {
		this.rolDescripcion = rolDescripcion;
	}

	public Integer getRolTipo() {
		return rolTipo;
	}

	public void setRolTipo(Integer rolTipo) {
		this.rolTipo = rolTipo;
	}

	public String getRolDetalle() {
		return rolDetalle;
	}

	public void setRolDetalle(String rolDetalle) {
		this.rolDetalle = rolDetalle;
	}

	public Integer getUsroId() {
		return usroId;
	}

	public void setUsroId(Integer usroId) {
		this.usroId = usroId;
	}

	public Integer getUsroEstado() {
		return usroEstado;
	}

	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}

	public Integer getRoflcrId() {
		return roflcrId;
	}

	public void setRoflcrId(Integer roflcrId) {
		this.roflcrId = roflcrId;
	}

	public Integer getRoflcrEstado() {
		return roflcrEstado;
	}

	public void setRoflcrEstado(Integer roflcrEstado) {
		this.roflcrEstado = roflcrEstado;
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

	public String getDpnDescripcion() {
		return dpnDescripcion;
	}

	public void setDpnDescripcion(String dpnDescripcion) {
		this.dpnDescripcion = dpnDescripcion;
	}
	
	public String nombresCompletos() {
		return prsNombres + " " + prsPrimerApellido + " " + prsSegundoApellido + "";
	}
	
	
}
