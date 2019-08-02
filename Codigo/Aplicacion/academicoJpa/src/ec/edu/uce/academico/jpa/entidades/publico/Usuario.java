package ec.edu.uce.academico.jpa.entidades.publico;
// Generated 02/03/2017 8:53:22 by Hibernate Tools 3.5.0.Final

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

import ec.edu.uce.academico.jpa.entidades.publico.constantes.ConstantesJpa;

@Entity
@Table(name = "USUARIO", schema = ConstantesJpa.SCHEMA)
@NamedQueries({ @NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
				@NamedQuery(name = "Usuario.findPorIdentificacion", query = "SELECT u FROM Usuario u WHERE u.usrIdentificacion = :usrIdentificacion") })
public class Usuario implements java.io.Serializable {

	private static final long serialVersionUID = 8384500090638473714L;
	private int usrId;
	private Persona usrPersona;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;
	private Timestamp usrFechaCreacion;
	private Date usrFechaCaducidad;
	private Date usrFechaCadPass;
	private Integer usrEstado;
	private Integer usrEstSesion;
	private Integer usrActiveDirectory;
	private List<UsuarioRol> usrUsuarioRols = new ArrayList<UsuarioRol>(0);

	public Usuario() {
	}

	public Usuario(int usrId) {
		this.usrId = usrId;
	}

	public Usuario(int usrId, Persona usrPersona, String usrIdentificacion, String usrNick, String usrPassword,
			Timestamp usrFechaCreacion, Date usrFechaCaducidad, Date usrFechaCadPass, Integer usrEstado,
			Integer usrEstSesion, Integer usrActiveDirectory, List<UsuarioRol> usrUsuarioRols) {
		this.usrId = usrId;
		this.usrPersona = usrPersona;
		this.usrIdentificacion = usrIdentificacion;
		this.usrNick = usrNick;
		this.usrPassword = usrPassword;
		this.usrFechaCreacion = usrFechaCreacion;
		this.usrFechaCaducidad = usrFechaCaducidad;
		this.usrFechaCadPass = usrFechaCadPass;
		this.usrEstado = usrEstado;
		this.usrEstSesion = usrEstSesion;
		this.usrActiveDirectory = usrActiveDirectory;
		this.usrUsuarioRols = usrUsuarioRols;
	}

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Basic(optional = false)
	@Column(name = "USR_ID", unique = true, nullable = false, precision = 6, scale = 0)
	public int getUsrId() {
		return this.usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	@ManyToOne
	@JoinColumn(name = "PRS_ID")
	public Persona getUsrPersona() {
		return this.usrPersona;
	}

	public void setUsrPersona(Persona usrPersona) {
		this.usrPersona = usrPersona;
	}

	@Column(name = "USR_IDENTIFICACION", length = 13)
	@Length(max = 13)
	public String getUsrIdentificacion() {
		return this.usrIdentificacion;
	}

	public void setUsrIdentificacion(String usrIdentificacion) {
		this.usrIdentificacion = usrIdentificacion;
	}

	@Column(name = "USR_NICK", length = 20)
	@Length(max = 20)
	public String getUsrNick() {
		return this.usrNick;
	}

	public void setUsrNick(String usrNick) {
		this.usrNick = usrNick;
	}

	@Column(name = "USR_PASSWORD", length = 64)
	@Length(max = 64)
	public String getUsrPassword() {
		return this.usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	@Column(name = "USR_FECHA_CREACION")
	public Timestamp getUsrFechaCreacion() {
		return this.usrFechaCreacion;
	}

	public void setUsrFechaCreacion(Timestamp usrFechaCreacion) {
		this.usrFechaCreacion = usrFechaCreacion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "USR_FECHA_CADUCIDAD", length = 7)
	public Date getUsrFechaCaducidad() {
		return this.usrFechaCaducidad;
	}

	public void setUsrFechaCaducidad(Date usrFechaCaducidad) {
		this.usrFechaCaducidad = usrFechaCaducidad;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "USR_FECHA_CAD_PASS", length = 7)
	public Date getUsrFechaCadPass() {
		return this.usrFechaCadPass;
	}

	public void setUsrFechaCadPass(Date usrFechaCadPass) {
		this.usrFechaCadPass = usrFechaCadPass;
	}

	@Column(name = "USR_ESTADO", precision = 22, scale = 0)
	public Integer getUsrEstado() {
		return this.usrEstado;
	}

	public void setUsrEstado(Integer usrEstado) {
		this.usrEstado = usrEstado;
	}

	@Column(name = "USR_EST_SESION", precision = 22, scale = 0)
	public Integer getUsrEstSesion() {
		return this.usrEstSesion;
	}

	public void setUsrEstSesion(Integer usrEstSesion) {
		this.usrEstSesion = usrEstSesion;
	}

	@Column(name = "USR_ACTIVE_DIRECTORY", precision = 22, scale = 0)
	public Integer getUsrActiveDirectory() {
		return this.usrActiveDirectory;
	}

	public void setUsrActiveDirectory(Integer usrActiveDirectory) {
		this.usrActiveDirectory = usrActiveDirectory;
	}

	@OneToMany( mappedBy = "usroUsuario")
	public List<UsuarioRol> getUsrUsuarioRols() {
		return this.usrUsuarioRols;
	}

	public void setUsrUsuarioRols(List<UsuarioRol> usrUsuarioRols) {
		this.usrUsuarioRols = usrUsuarioRols;
	}

}
