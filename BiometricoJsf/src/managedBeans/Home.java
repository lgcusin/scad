package managedBeans;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "home")
@RequestScoped
public class Home {

	public String nombres;
	public Date fecha;

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {
		nombres = "Nombre del Docente";
		fecha = new Date(2018, 1, 14);
	}

	public String verAsistencia() {
		return "detalleAsistencia";
	}

	// setters and getters
	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
