package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.biometrico.jpa.DetallePuesto;
import ec.edu.uce.biometrico.jpa.Usuario;
import ec.edu.uce.biometrico.jpa.UsuarioRol;

@Local
public interface SrvLoginLocal {

	UsuarioRol verificar(String nick, String clave);

	List<DetallePuesto> listarDetallePuestoDocente(Integer prsId);

	List<DetallePuesto> listarDetallePuestoEmpleado(Integer prsId);

	Usuario buscarPorNick(String username);

	List<UsuarioRol> buscarRolesActivoXUsuario(Integer usrId);

}
