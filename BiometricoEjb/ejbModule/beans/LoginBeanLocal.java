package beans;

import javax.ejb.Local;

@Local
public interface LoginBeanLocal {
	
	public boolean verificar(String nick, String clave);

}
