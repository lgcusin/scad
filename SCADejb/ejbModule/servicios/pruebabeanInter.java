package servicios;

import javax.ejb.Local;

import model.Prueba;

@Local
public interface pruebabeanInter {
	
	public Prueba listar(Integer id);

}
