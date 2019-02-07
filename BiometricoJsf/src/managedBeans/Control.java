package managedBeans;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lector.JSDCLocal;

@ManagedBean(name = "control")
@ViewScoped
public class Control {

	@EJB
	private JSDCLocal srvJsdc;
	
	
	
	public BufferedImage bimg1;
	public String hora;
	public Date ahora;
	public SimpleDateFormat formateador;
	

	@PostConstruct
	public void init() {
		srvJsdc.inicializar();
		
	}

	public void temporizador() {
		ahora = new Date();
		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
	}
	

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

}
