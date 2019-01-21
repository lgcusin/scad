package beans;

import javax.ejb.Local;

import model.Usuario;

@Local
public interface LoginBeanLocal {

	public Usuario verificar(Usuario usuario);

}
