package managedBeans;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

import lector.JSDCLocal;
import model.FichaDocente;
import model.Horario;

@ManagedBean(name = "control")
@ViewScoped
public class Control {

	@EJB
	private JSDCLocal srvJsdc;

	private FichaDocente regDcnt;
	private Horario hrr;

	public String hora;
	public Date fechahora;
	public Date ahora;
	public SimpleDateFormat formateador;
	public boolean exist;

	@PostConstruct
	public void init() {
		srvJsdc.inicializar();
		regDcnt = new FichaDocente();
		fechahora = new Date();

	}

	public void temporizador() {
		ahora = new Date();

		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
	}

	public void registrar() throws SQLException, IOException {

		regDcnt = srvJsdc.comparar();
		if (regDcnt != null) {
			hrr = srvJsdc.verificarHorario(ahora, regDcnt.getFcdcId());
			if (hrr.getHrrId() != null) {
				System.out.println("Si hay datos");
			} else {
				System.out.println("Si hay datos");
			}
		}

	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public FichaDocente getRegDcnt() {
		return regDcnt;
	}

	public void setRegDcnt(FichaDocente regDcnt) {
		this.regDcnt = regDcnt;
	}

	public Horario getHrr() {
		return hrr;
	}

	public void setHrr(Horario hrr) {
		this.hrr = hrr;
	}

}
