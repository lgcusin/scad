package managedBeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import model.FichaDocente;
import servicios.SrvDocenteLocal;

@ManagedBean(name = "principal")
@RequestScoped
public class Principal {

	@EJB
	private SrvDocenteLocal srvDcnt;

	private FichaDocente fd;
	public Integer fdId;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Login l = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		fdId = l.getUsr().getFichaDocente().getFcdcId();
		fd = srvDcnt.getDocente(fdId);
	}

	public String verDetalle() {
		return "detalleAsistencia";
	}

	public String verActividades() {
		return "detalleActividades";
	}

	public String verSyllabo() {
		return "syllabo";
	}

	public String verHorarioClases() {
		return "horarioClases";
	}

	public String verHorarioExamen() {
		return "horarioExamen";
	}

	// setters and getters
	public Integer getFdId() {
		return fdId;
	}

	public void setFdId(Integer fdId) {
		this.fdId = fdId;
	}

	public FichaDocente getFd() {
		return fd;
	}

	public void setFd(FichaDocente fd) {
		this.fd = fd;
	}

}
