package servicios;

import javax.ejb.Local;

import model.Usuario;

@Local
public interface LoginBeanLocal {

	public Usuario verificar(String nick, String clave);

}
