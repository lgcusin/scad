package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Asistencia;
import servicios.SrvDocente;

@ManagedBean(name = "detalleAss")
@RequestScoped
public class DetalleAsistencia {

	@EJB
	private SrvDocente srvDnt;

	private Asistencia ass;

	public List<Asistencia> lstA;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		lstA = srvDnt.listarAsistencia(p.getFdId());
		ass = new Asistencia();
	}

	public String regresar() {
		return "principal";
	}

	// setters and getters

	public Asistencia getAss() {
		return ass;
	}

	public void setAss(Asistencia ass) {
		this.ass = ass;
	}

	public List<Asistencia> getLstA() {
		return lstA;
	}

	public void setLstA(List<Asistencia> lstA) {
		this.lstA = lstA;
	}

}
