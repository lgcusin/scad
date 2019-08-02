package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.DetallePuesto;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

@Local
public interface SrvLoginLocal {

	UsuarioRol verificar(String nick, String clave);

	List<DetallePuesto> listarDetallePuestoDocente(Integer prsId);

	List<DetallePuesto> listarDetallePuestoEmpleado(Integer prsId);

}
