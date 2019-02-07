package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import servicios.SrvDocente;

@ManagedBean(name = "detalleAct")
@RequestScoped
public class DetalleActividades {
	@EJB
	private SrvDocente srvDnt;

	public String act = null;
	public List<String> lstAc;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		act = " ";
		lstAc = srvDnt.listarActividades(p.fdId);
	}

	public String regresar() {
		return "principal";
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public List<String> getLstAc() {
		return lstAc;
	}

	public void setLstAc(List<String> lstAc) {
		this.lstAc = lstAc;
	}

	// setters and getters

	
}
