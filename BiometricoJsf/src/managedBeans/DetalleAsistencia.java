package managedBeans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import model.Asistencia;

@ManagedBean(name="detalleAss")
@SessionScoped
public class DetalleAsistencia {

	private Asistencia ass;
	
	@PostConstruct
	public void init(){
		
	}
	
	
}
