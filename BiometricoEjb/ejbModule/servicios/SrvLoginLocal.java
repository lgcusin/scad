package servicios;

import java.util.List;

import javax.ejb.Local;

import model.DetallePuesto;
import model.Usuario;

@Local
public interface SrvLoginLocal {

	Usuario verificar(String nick, String clave);

	List<DetallePuesto> buscarFacultad(Integer fcdcId);

}
