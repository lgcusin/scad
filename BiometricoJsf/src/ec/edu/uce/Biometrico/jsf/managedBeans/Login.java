package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ec.edu.uce.Biometrico.ejb.servicios.impl.SrvDocente;
import ec.edu.uce.Biometrico.ejb.servicios.impl.SrvLogin;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvLoginLocal;
import ec.uce.edu.biometrico.jpa.DetallePuesto;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.FichaEmpleado;
import ec.uce.edu.biometrico.jpa.Usuario;
import ec.uce.edu.biometrico.jpa.UsuarioRol;

@ManagedBean(name = "login")
@SessionScoped
public class Login {

	@EJB
	private SrvLoginLocal srvlgn;
	private UsuarioRol usuarioRol;
	private List<DetallePuesto> dt;
	boolean Docente = false;
	boolean adminFacultad = false;

	public String nick;
	public String clave;

	@PostConstruct
	public void init() {
		usuarioRol = new UsuarioRol();
	}

	public String ingresar() {
		usuarioRol = srvlgn.verificar(nick, clave);

		if (usuarioRol.getUsroId() != null) {
			if (usuarioRol.getRol().getRolId() == 5) {
				Docente = true;
				dt = srvlgn.listarDetallePuestoDocente(usuarioRol.getUsuario().getPersona().getPrsId());
				List<FichaDocente> lstD = new ArrayList<>();
				lstD.add(dt.get(0).getFichaDocente());
				usuarioRol.getUsuario().getPersona().setFichaDocentes(lstD);
				usuarioRol.getUsuario().getPersona().getFichaDocentes().get(0).setDetallePuestos(dt);
			}
			if (usuarioRol.getRol().getRolId() == 39) {
				adminFacultad = true;
				dt = srvlgn.listarDetallePuestoEmpleado(usuarioRol.getUsuario().getPersona().getPrsId());
				List<FichaEmpleado> lstE = new ArrayList<>();
				lstE.add(dt.get(0).getFichaEmpleado());
				usuarioRol.getUsuario().getPersona().setFichaEmpleados(lstE);
				usuarioRol.getUsuario().getPersona().getFichaEmpleados().get(0).setDetallePuestos(dt);
			}
			return "principal";
		} else {
			System.out.println("usuario o contraseña no validos");
			return "errorLogin";
		}

	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * @return the usuarioRol
	 */
	public UsuarioRol getUsuarioRol() {
		return usuarioRol;
	}

	/**
	 * @param usuarioRol
	 *            the usuarioRol to set
	 */
	public void setUsuarioRol(UsuarioRol usuarioRol) {
		this.usuarioRol = usuarioRol;
	}

	public boolean isDocente() {
		return Docente;
	}

	public void setDocente(boolean docente) {
		Docente = docente;
	}

	public boolean isAdminFacultad() {
		return adminFacultad;
	}

	public void setAdminFacultad(boolean empleado) {
		adminFacultad = empleado;
	}

	public List<DetallePuesto> getDt() {
		return dt;
	}

	public void setDt(List<DetallePuesto> dt) {
		this.dt = dt;
	}

}