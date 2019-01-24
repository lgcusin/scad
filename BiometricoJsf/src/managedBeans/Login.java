package managedBeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import beans.LoginBean;
import beans.LoginBeanLocal;
import model.Usuario;

@ManagedBean(name = "login")
@SessionScoped
public class Login {

	@EJB
	private LoginBeanLocal loginBean;
	private Usuario usr;

	public String nick;
	public String clave;

	@PostConstruct
	public void init() {
		usr = new Usuario();
	}

	public String ingresar() {
		usr = loginBean.verificar(usr);

		if (usr.getUrsId() != null) {
			return "principal";
		} else {
			System.out.println("usuario o contraseña no validos");
		}
		return null;
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

}
