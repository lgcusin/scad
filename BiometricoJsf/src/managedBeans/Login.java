package managedBeans;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import beans.LoginBean;
import beans.LoginBeanLocal;

@ManagedBean(name = "login")
@SessionScoped
public class Login {

	@EJB
	private LoginBeanLocal loginBean;
	private LoginBean login;
	
	
	public String nick;
	public String clave;

	@PostConstruct
	public void init() {
		login= new LoginBean();
	}

	public String ingresar() {
		boolean validez = loginBean.verificar(nick, clave);
		if(validez){
			return "home";
		}else{
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

}
