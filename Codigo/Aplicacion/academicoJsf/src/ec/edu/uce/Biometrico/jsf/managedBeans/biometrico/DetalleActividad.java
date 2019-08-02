package ec.edu.uce.Biometrico.jsf.managedBeans.biometrico;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ec.edu.uce.academico.ejb.servicios.biometrico.interfaces.SrvDocenteLocal;
import ec.edu.uce.academico.ejb.servicios.biometrico.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ContenidoCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Seguimiento;

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
			lstC = srvEmp.listarCarreras(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId());
		}
		if (beanLogin.adminFacultad) {
			selectDcn = new FichaDocente();
			lstC = srvEmp
					.listarCarrerasxFacultad(beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId());
		}

	}

	public void listarDcnts(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstD = srvEmp.listarDocentesxCarrera(crrId);
		} else {
			System.out.println("No ha seleccionada una carrera: ");
		}

	}

	public void listarMtrs(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstM = new ArrayList<>();
			lstM = srvDnt.listarMateriasxCarrera(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), crrId);
		} else {
			lstD = null;
			System.out.println("No ha seleccionada una carrera: ");
		}

	}

	public void listarActividades() {
		lstSgm = new ArrayList<>();
		if (beanLogin.Docente) {
			lstSgm = srvDnt.listarSeguimientosxDocenteMateria(
					beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), selectMtr.getMtrId());
		} else {
			lstSgm = srvEmp.listarSeguimientosxDocente(selectDcn.getFcdcId());
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
