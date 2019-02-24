package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Asistencia;
import model.Carrera;
import model.Contenido;
import model.FichaDocente;
import servicios.SrvDocente;
import servicios.SrvDocenteLocal;
import servicios.SrvEmpleadoLocal;

@ManagedBean(name = "detalleAct")
@ViewScoped
public class DetalleActividad {
	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;

	public List<Contenido> lstCn;
	public List<Carrera> lstC;
	public List<FichaDocente> lstD;

	public Integer crrId;
	public Integer fcdId;
	public Contenido selecCnt;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		if (p.docente) {
			selecCnt = new Contenido();
			lstCn = srvDnt.listarContenidos(p.fdId);
		}
		if (p.empleado) {
			// selectCrr = new Carrera();
			// selectDcn = new FichaDocente();
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

	public void listarActividades() {
		lstCn = srvDnt.listarContenidos(fcdId);
	}

	public String regresar() {
		return "principal";
	}

	// setters and getters

	public List<Contenido> getLstCn() {
		return lstCn;
	}

	public Contenido getSelecCnt() {
		return selecCnt;
	}

	public void setSelecCnt(Contenido selecCnt) {
		this.selecCnt = selecCnt;
	}

	public void setLstCn(List<Contenido> lstCn) {
		this.lstCn = lstCn;
	}

	public List<Carrera> getLstC() {
		return lstC;
	}

	public void setLstC(List<Carrera> lstC) {
		this.lstC = lstC;
	}

	public List<FichaDocente> getLstD() {
		return lstD;
	}

	public void setLstD(List<FichaDocente> lstD) {
		this.lstD = lstD;
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