package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Actividad;
import model.Carrera;
import model.Contenido;
import model.Herramienta;
import model.Materia;
import model.Syllabo;
import model.UnidadCurricular;
import servicios.SrvEmpleadoLocal;
import servicios.SrvSeguimientoLocal;

@ManagedBean(name = "syllabus")
@SessionScoped
public class Syllabus {

	@EJB
	private SrvSeguimientoLocal srvSgm;
	@EJB
	private SrvEmpleadoLocal srvEmp;

	public Materia selectMtr;
	public Syllabo syl;
	public Carrera selectCrr;
	public Integer crrId;
	public List<Carrera> lstC;
	public List<Materia> lstM;
	public List<UnidadCurricular> lstUC;
	public List<Contenido> lstCnt;
	public List<Actividad> lstAct;
	public List<Herramienta> lstHrr;
	public boolean dataSyllabo;
	public boolean modiSyllabo;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		if (p.docente) {
			selectMtr = new Materia();
			lstC = srvSgm.listarAllCrrByFcdc(p.fdId);
			lstM = srvSgm.listarAllMatByFcdc(p.fdId);
		}
		if (p.empleado) {
			lstC = srvEmp.listarCarreras(p.fcId);
		}

	}

	public void buscar() {
		if (crrId != null) {
			lstM = srvSgm.listarMatByCrr(crrId);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione una carrera", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String limpiar() {
		lstM = null;
		return null;
	}

	public String verDetaSyllabus() {
		selectCrr = srvSgm.getCarrera(selectMtr.getMtrId());
		if (selectMtr.getMtrId() != null) {
			syl = srvSgm.getSyllabus(selectMtr.getMtrId());
			lstUC = srvSgm.listarUnidadCurricular(selectMtr.getMtrId());
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione una materia", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (syl != null && !lstUC.isEmpty()) {
			for (UnidadCurricular unidad : lstUC) {
				lstCnt = srvSgm.listarContenidos(unidad.getUncrId());
				for (Contenido contenido : lstCnt) {
					lstAct = srvSgm.listarActividades(contenido.getCntId());
					lstHrr = srvSgm.listarHerramientas(contenido.getCntId());
					contenido.setActividads(lstAct);
					contenido.setHerramientas(lstHrr);
				}
				unidad.setContenidos(lstCnt);
			}
			dataSyllabo = true;
		} else {
			dataSyllabo = false;
		}
		return "syllabo";
	}

	public String modificar() {
		modiSyllabo = true;
		dataSyllabo = false;
		return null;
	}

	public String regresar() {
		modiSyllabo = false;
		return "syllabus";
	}

	// Setters and getters

	public List<Materia> getLstM() {
		return lstM;
	}

	public Materia getSelectMtr() {
		return selectMtr;
	}

	public void setSelectMtr(Materia selectMtr) {
		this.selectMtr = selectMtr;
	}

	public void setLstM(List<Materia> lstM) {
		this.lstM = lstM;
	}

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

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	// Getters Syllabus

	public boolean isModiSyllabo() {
		return modiSyllabo;
	}

	public void setModiSyllabo(boolean modiSyllabo) {
		this.modiSyllabo = modiSyllabo;
	}

	public Syllabo getSyl() {
		return syl;
	}

	public void setSyl(Syllabo syl) {
		this.syl = syl;
	}

	public List<UnidadCurricular> getLstUC() {
		return lstUC;
	}

	public void setLstUC(List<UnidadCurricular> lstUC) {
		this.lstUC = lstUC;
	}

	public boolean isDataSyllabo() {
		return dataSyllabo;
	}

	public void setDataSyllabo(boolean dataSyllabo) {
		this.dataSyllabo = dataSyllabo;
	}

}
