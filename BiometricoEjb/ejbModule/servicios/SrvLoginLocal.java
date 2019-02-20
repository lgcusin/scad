package servicios;

import javax.ejb.Local;

import model.FichaDocente;
import model.FichaEmpleado;
import model.Usuario;

@Local
public interface SrvLoginLocal {

	Usuario verificar(String nick, String clave);
	
	FichaDocente getDocente(Integer id);

	FichaEmpleado getEmpleado(Integer fcemId);

}
