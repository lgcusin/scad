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
   
 ARCHIVO:     UsuarioDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos de la entidad Usuario. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02-03-2017 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.dtos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) UsuarioDto.
 * DTO encargado de manejar los datos de la entidad Usuario. 
 * @author dalbuja.
 * @version 1.0
 */
public class UsuarioDto implements Serializable{

	private static final long serialVersionUID = -5138307068443715469L;
	
	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	//TABLA USUARIO
	private int usrId;
	private Integer usrPrsId;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;
	private Timestamp usrFechaCreacion;
	private Date usrFechaCaducidad;
	private Date usrFechaCadPass;
	private Integer usrEstado;
	private Integer usrEstSesion;
	private Integer usrActiveDirectory;
//	TABLA PERSONA
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
	private Date prsFechaNacimiento;
	private Integer prsEtnId;
	private Integer prsUbcId;
	
	
	
	public UsuarioDto() {
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

	public Integer getUsrPrsId() {
		return usrPrsId;
	}

	public void setUsrPrsId(Integer usrPrsId) {
		this.usrPrsId = usrPrsId;
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


	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("usrId : " + usrId);
		sb.append(tabulador + "usrPrsId : " + (usrPrsId==null? "NULL":usrPrsId));
		sb.append(tabulador + "usrIdentificacion : " + (usrIdentificacion==null? "NULL":usrIdentificacion));
		sb.append(tabulador + "usrNick : " + (usrNick==null? "NULL":usrNick));
		sb.append(tabulador + "usrFechaCreacion : " + (usrFechaCreacion==null? "NULL":usrFechaCreacion));
		sb.append(tabulador + "usrFechaCaducidad : " + (usrFechaCaducidad==null? "NULL":usrFechaCaducidad));
		sb.append(tabulador + "usrFechaCadPass : " + (usrFechaCadPass==null? "NULL":usrFechaCadPass));
		sb.append(tabulador + "usrEstado : " + (usrEstado==null? "NULL":usrEstado));
		sb.append(tabulador + "usrEstSesion : " + (usrEstSesion==null? "NULL":usrEstSesion));
		sb.append(tabulador + "usrActiveDirectory : " + (usrActiveDirectory==null? "NULL":usrActiveDirectory));
		sb.append("prsId : " + prsId);
		sb.append(tabulador + "prsTipoIdentificacion : " + prsTipoIdentificacion);
		sb.append(tabulador + "prsTipoIdentificacionSniese : " + prsTipoIdentificacionSniese);
		sb.append(tabulador + "prsIdentificacion : " + prsIdentificacion);
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido==null? "NULL":prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido==null? "NULL":prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres==null? "NULL":prsNombres));
		sb.append(tabulador + "prsSexo : " + prsSexo);
		sb.append(tabulador + "prsSexoSniese : " + prsSexoSniese);
		sb.append(tabulador + "prsTelefono : " + prsTelefono);
		sb.append(tabulador + "prsMailPersonal : " + (prsMailPersonal==null? "NULL":prsMailPersonal));
		sb.append(tabulador + "prsMailInstitucional : " + (prsMailInstitucional==null? "NULL":prsMailInstitucional));
		sb.append(tabulador + "prsFechaNacimiento : " + prsFechaNacimiento);
		return sb.toString();
    }
}
