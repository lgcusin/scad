package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Asistencia;
import model.Carrera;
import model.FichaDocente;
import servicios.SrvDocente;
import servicios.SrvDocenteLocal;
import servicios.SrvEmpleadoLocal;

@ManagedBean(name = "detalleAss")
@ViewScoped
public class DetalleAsistencia {

	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;

	public List<Carrera> lstC;
	public List<FichaDocente> lstD;
	public List<Asistencia> lstA;

	public Integer crrId;
	public Integer fcdId;
	public Carrera selectCrr;
	public FichaDocente selectDcn;
	private Asistencia selectAss;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		if (p.docente) {
			lstA = srvDnt.listarAsistencia(p.fdId);
			selectAss = new Asistencia();
		}
		if (p.empleado) {
			selectCrr = new Carrera();
			selectDcn = new FichaDocente();
			lstC = srvEmp.listarCarreras();
		}
	}

	public void listarDcnts(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstD = srvEmp.listarDocentes(crrId);
		} else {
			lstD = null;
			System.out.println("No ha seleccionada una carrera: ");
		}

	}

	public void listarAssts() {
		lstA = srvDnt.listarAsistencia(fcdId);
	}

	public String regresar() {
		return "principal";
	}

	// setters and getters Docente

	public Asistencia getAss() {
		return selectAss;
	}

	public void setAss(Asistencia ass) {
		this.selectAss = ass;
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

	public Carrera getSelectCrr() {
		return selectCrr;
	}

	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
	}

	public List<FichaDocente> getLstD() {
		return lstD;
	}

	public void setLstD(List<FichaDocente> lstD) {
		this.lstD = lstD;
	}

	public FichaDocente getSelectDcn() {
		return selectDcn;
	}

	public void setSelectDcn(FichaDocente selectDcn) {
		this.selectDcn = selectDcn;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public Integer getFcdId() {
		return fcdId;
	}

	public void setFcdId(Integer fcdId) {
		this.fcdId = fcdId;
	}

}
