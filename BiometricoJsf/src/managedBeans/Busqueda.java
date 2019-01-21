package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import beans.DocenteBeanLocal;
import model.FichaDocente;

@ManagedBean(name = "docentes")
@SessionScoped
public class Busqueda {

	@EJB
	private DocenteBeanLocal fcdc;
	private FichaDocente selectDcnt;

	public List<FichaDocente> lstDcnt;
	public String parametro;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		selectDcnt = new FichaDocente();
		lstDcnt = fcdc.listar("");
	}

	public void listar() {
		lstDcnt = fcdc.listar(parametro);
	}

	// Setters y Getters
	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public List<FichaDocente> getLstDcnt() {
		return lstDcnt;
	}

	public void setLstDcnt(List<FichaDocente> lstDcnt) {
		this.lstDcnt = lstDcnt;
	}

	public FichaDocente getSelectDcnt() {
		return selectDcnt;
	}

	public void setSelectDcnt(FichaDocente selectDcnt) {
		this.selectDcnt = selectDcnt;
	}

	
}