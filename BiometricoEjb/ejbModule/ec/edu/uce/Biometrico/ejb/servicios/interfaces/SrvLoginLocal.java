package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.uce.edu.biometrico.jpa.DetallePuesto;
import ec.uce.edu.biometrico.jpa.Usuario;
import ec.uce.edu.biometrico.jpa.UsuarioRol;

@Local
public interface SrvLoginLocal {

	UsuarioRol verificar(String nick, String clave);

	List<DetallePuesto> listarDetallePuestoDocente(Integer prsId);

	List<DetallePuesto> listarDetallePuestoEmpleado(Integer prsId);

}
