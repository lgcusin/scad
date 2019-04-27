package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.FichaDocente;
import servicios.SrvDocenteLocal;

@ManagedBean(name = "registro")
@SessionScoped
public class Registro {

	// Interfaz de servicio
	@EJB
	private SrvDocenteLocal srvDcnt;

	// ObjetoF
	private FichaDocente selectDcnt;

	// Listas de objetos
	public List<FichaDocente> lstDcnt;

	// Entradas y salidas
	public String parametro;

	@PostConstruct
	public void init() {
		selectDcnt = new FichaDocente();
		lstDcnt = srvDcnt.listar("  ");
	}

	// ####### Pagina busqueda #######
	public void listar() {
		lstDcnt = srvDcnt.listar(parametro);
	}

	public String verHuellas() {
		if (selectDcnt == null) {
			FacesMessage msg = new FacesMessage("No ha seleccionado ningun docente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			Login l = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
			selectDcnt.setDetallePuestos(l.getUsr().getFichaDocente().getDetallePuestos());
			return "huellas";
		}
	}

	public String regresar() {
		lstDcnt = null;
		return "principal";
	}
	
	public String limpiar(){
		lstDcnt = null;
		return null;
	}

	// ####### Setters y Getters Busqueda #######

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
