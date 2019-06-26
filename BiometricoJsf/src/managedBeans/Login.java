package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import model.DetallePuesto;
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
	private List<DetallePuesto> dt;
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

		if (usr.getUrsId() != null) {
			if (usr.getFichaDocente().getFcdcId() != 0) {
				Docente = true;
			}
			if (usr.getFichaEmpleado().getFcemId() != 0) {
				Empleado = true;
			}
			dt = srvlgn.buscarDetallePuesto(usr.getFichaDocente().getFcdcId());
			usr.getFichaDocente().setDetallePuestos(dt);
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

	public List<DetallePuesto> getDt() {
		return dt;
	}

	public void setDt(List<DetallePuesto> dt) {
		this.dt = dt;
	}
	
	

}
