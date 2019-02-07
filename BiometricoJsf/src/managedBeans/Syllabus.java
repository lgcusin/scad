package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Carrera;
import model.Materia;
import model.Syllabo;
import servicios.SrvSeguimientoLocal;

@ManagedBean(name = "syllabus")
public class Syllabus {

	@EJB
	private SrvSeguimientoLocal srvSgm;

	public Materia mtr;
	public Syllabus syl;
	public Carrera selectCrr;
	public Integer crrId;
	public List<Carrera> lstC;
	public List<Materia> lstM;

	@PostConstruct
	public void init() {
		selectCrr = new Carrera();
		lstC = srvSgm.listarAllCrr();
		lstM = srvSgm.listarAllMat();
	}

	public void buscar() {
		if (crrId != null) {
			lstM = srvSgm.listarMatByCrr(crrId);
		} else {
			lstM = srvSgm.listarAllMat();
		}
	}

	// Setters and getters
	public Materia getMtr() {
		return mtr;
	}

	public void setMtr(Materia mtr) {
		this.mtr = mtr;
	}

	public List<Materia> getLstM() {
		return lstM;
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

}
