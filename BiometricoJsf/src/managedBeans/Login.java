package managedBeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import model.FichaDocente;
import model.FichaEmpleado;
import model.Usuario;
import servicios.SrvLogin;
import servicios.SrvLoginLocal;
import servicios.SrvDocente;

@ManagedBean(name = "login")
@SessionScoped
public class Login {

	@EJB
	private SrvLoginLocal srvlgn;
	private Usuario usr;
	private FichaDocente fd;
	private FichaEmpleado fem;
	boolean Docente = false;
	boolean Empleado = false;

	public String nick;
	public String clave;

	@PostConstruct
	public void init() {
		usr = new Usuario();
	}

	public String ingresar() {

		usr = srvlgn.verificar(nick, clave);

		if (usr != null) {
			fd = usr.getFichaDocente();
			if (fd != null) {
				Docente = true;
			}
			fem = usr.getFichaEmpleado();
			if (fem != null) {
				Empleado = true;
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

	public Usuario getUsr() {
		return usr;
	}

	public void setUsr(Usuario usr) {
		this.usr = usr;
	}

	public FichaDocente getFd() {
		return fd;
	}

	public void setFd(FichaDocente fd) {
		this.fd = fd;
	}

	public FichaEmpleado getFem() {
		return fem;
	}

	public void setFem(FichaEmpleado fem) {
		this.fem = fem;
	}

	public boolean isDocente() {
		return Docente;
	}

	public void setDocente(boolean docente) {
		Docente = docente;
	}

	public boolean isEmpleado() {
		return Empleado;
	}

	public void setEmpleado(boolean empleado) {
		Empleado = empleado;
	}

}
