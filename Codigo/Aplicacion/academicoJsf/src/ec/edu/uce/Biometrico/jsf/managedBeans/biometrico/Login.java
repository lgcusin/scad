package ec.edu.uce.Biometrico.jsf.managedBeans.biometrico;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.academico.ejb.servicios.biometrico.interfaces.SrvLoginLocal;
import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEmpleado;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

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

		if (usuarioRol != null && usuarioRol.getUsroId() > 0) {
			if (usuarioRol.getUsroRol().getRolId() == 5) {
				Docente = true;
				dt = srvlgn.listarDetallePuestoDocente(usuarioRol.getUsroUsuario().getUsrPersona().getPrsId());
				List<FichaDocente> lstD = new ArrayList<>();
				lstD.add(dt.get(0).getDtpsFichaDocente());
				usuarioRol.getUsroUsuario().getUsrPersona().setPrsFichaDocentes(lstD);
				usuarioRol.getUsroUsuario().getUsrPersona().getPrsFichaDocentes().get(0).setFcdcDetallePuestos(dt);
			}
			if (usuarioRol.getUsroRol().getRolId() == 39) {
				adminFacultad = true;
				dt = srvlgn.listarDetallePuestoEmpleado(usuarioRol.getUsroUsuario().getUsrPersona().getPrsId());
				List<FichaEmpleado> lstE = new ArrayList<>();
				lstE.add(dt.get(0).getDtpsFichaEmpleado());
				usuarioRol.getUsroUsuario().getUsrPersona().setPrsFichaEmpleados(lstE);
				usuarioRol.getUsroUsuario().getUsrPersona().getPrsFichaEmpleados().get(0).setFcemDetallePuestos(dt);
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
