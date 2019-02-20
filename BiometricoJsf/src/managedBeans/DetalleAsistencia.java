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
import model.Carrera;
import servicios.SrvDocente;
import servicios.SrvDocenteLocal;
import servicios.SrvEmpleadoLocal;

@ManagedBean(name = "detalleAss")
@RequestScoped
public class DetalleAsistencia {

	@EJB
	private SrvDocenteLocal srvDnt;
	private SrvEmpleadoLocal srvEmp;

	private Asistencia ass;

	public List<Asistencia> lstA;
	public List<Carrera> lstC;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		if (p.docente) {
			lstA = srvDnt.listarAsistencia(p.fdId);
			ass = new Asistencia();
		} else if (p.empleado) {
			lstC = srvEmp.listarCarrera();
		}

	}

	public String regresar() {
		return "principal";
	}

	// setters and getters Docente

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

	// setters and getters Empleado

	public List<Carrera> getLstC() {
		return lstC;
	}

	public void setLstC(List<Carrera> lstC) {
		this.lstC = lstC;
	}

}
