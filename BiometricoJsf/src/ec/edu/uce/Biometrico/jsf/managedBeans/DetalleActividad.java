package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ec.edu.uce.Biometrico.ejb.servicios.impl.SrvDocente;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.DetallePuesto;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Seguimiento;

@ManagedBean(name = "detalleAct")
@ViewScoped
public class DetalleActividad {
	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	public Login beanLogin;

	public List<Seguimiento> lstSgm;
	public List<Carrera> lstC;
	public List<FichaDocente> lstD;
	public List<Materia> lstM;

	public Integer crrId;
	public ContenidoCurricular selecCnt;
	public Carrera selectCrr;
	public Materia selectMtr;
	public FichaDocente selectDcn;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		selectCrr = new Carrera();
		if (beanLogin.Docente) {
			selecCnt = new ContenidoCurricular();
			selectMtr = new Materia();
			lstC = new ArrayList<Carrera>();
			for (DetallePuesto dt : beanLogin.getDt()) {
				lstC.add(dt.getCarrera());
			}

			// lstCn =
			// srvDnt.listarContenidos(beanLogin.getUsr().getFichaDocente().getFcdcId());

		}
		if (beanLogin.adminFacultad) {
			selectDcn = new FichaDocente();
			lstC = srvEmp.listarCarreras(beanLogin.getDt().get(0).getCarrera().getDependencia().getDpnId());
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

	public void listarMtrs(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstM = new ArrayList<>();
			for (Materia mt : srvDnt.listarMaterias(crrId)) {
				lstM.add(mt);
			}
		} else {
			lstD = null;
			System.out.println("No ha seleccionada una carrera: ");
		}

	}

	public void listarActividades() {
		if (beanLogin.Docente) {
			// lstCn =
			// srvDnt.listarContenidos(beanLogin.getUsr().getFichaDocente().getFcdcId());
			lstSgm = srvDnt.listarSeguimientos(
					beanLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId(),
					selectMtr.getMtrId());
		} else {

		}
	}

	public String regresar() {
		return "principal";
	}

	// setters and getters

	public ContenidoCurricular getSelecCnt() {
		return selecCnt;
	}

	/**
	 * @return the selectDcn
	 */
	public FichaDocente getSelectDcn() {
		return selectDcn;
	}

	/**
	 * @param selectDcn
	 *            the selectDcn to set
	 */
	public void setSelectDcn(FichaDocente selectDcn) {
		this.selectDcn = selectDcn;
	}

	public void setSelecCnt(ContenidoCurricular selecCnt) {
		this.selecCnt = selecCnt;
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

	/**
	 * @return the selectMtr
	 */
	public Materia getSelectMtr() {
		return selectMtr;
	}

	/**
	 * @param selectMtr
	 *            the selectMtr to set
	 */
	public void setSelectMtr(Materia selectMtr) {
		this.selectMtr = selectMtr;
	}

	/**
	 * @return the lstM
	 */
	public List<Materia> getLstM() {
		return lstM;
	}

	/**
	 * @param lstM
	 *            the lstM to set
	 */
	public void setLstM(List<Materia> lstM) {
		this.lstM = lstM;
	}

	/**
	 * @return the lstSgm
	 */
	public List<Seguimiento> getLstSgm() {
		return lstSgm;
	}

	/**
	 * @param lstSgm
	 *            the lstSgm to set
	 */
	public void setLstSgm(List<Seguimiento> lstSgm) {
		this.lstSgm = lstSgm;
	}

}
