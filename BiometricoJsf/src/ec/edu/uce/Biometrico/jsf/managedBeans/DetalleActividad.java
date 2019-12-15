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
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Asistencia;
import ec.edu.uce.biometrico.jpa.Carrera;
import ec.edu.uce.biometrico.jpa.ContenidoCurricular;
import ec.edu.uce.biometrico.jpa.DetallePuesto;
import ec.edu.uce.biometrico.jpa.FichaDocente;
import ec.edu.uce.biometrico.jpa.Materia;
import ec.edu.uce.biometrico.jpa.Seguimiento;

@ManagedBean(name = "detalleActividad")
@ViewScoped
public class DetalleActividad {
	@EJB
	private SrvDocenteLocal srvDnt;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	@EJB
	private SrvSeguimientoLocal srvSgm;
	
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
	private boolean mostrarFiltro;
	private Integer mtrId;
	private Integer fcdcId;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		iniciar();
		if (beanLogin.Docente) {
			mostrarFiltro = false;
			lstC = srvEmp.listarCarreras(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId());
		}
		if (beanLogin.adminFacultad) {
			mostrarFiltro = true;
			lstC = srvEmp
					.listarCarrerasxFacultad(beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId());
		}

	}

	public void iniciar() {
		crrId = null;
		mtrId = null;
		fcdcId = null;
		selectCrr = new Carrera();
		selectMtr = new Materia();
		selectDcn = new FichaDocente();
		selectCrr.setCrrId(GeneralesConstantes.APP_ID_BASE);
		selectMtr.setMtrId(GeneralesConstantes.APP_ID_BASE);
		selectDcn.setFcdcId(GeneralesConstantes.APP_ID_BASE);
	}

	public void listarDcnts(ValueChangeEvent event) {
		selectDcn.setFcdcId(GeneralesConstantes.APP_ID_BASE);
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstD = srvEmp.listarDocentesxCarrera(crrId);
			if (lstD.isEmpty()) {
				FacesUtil.mensajeError("No existen docentes para la carrera seleccionada.");
			}
		}
	}

	public void listarMtrs(ValueChangeEvent event) {
		selectMtr.setMtrId(GeneralesConstantes.APP_ID_BASE);
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstM = srvDnt.listarMateriasxCarrera(beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), crrId);
			if (lstM.isEmpty()) {
				FacesUtil.mensajeError("No exsten materias en la carrera selecciondad.");
			}
		}

	}

	public void listarActividades() {
		lstSgm = new ArrayList<>();
		if (crrId != null && (mtrId != null || fcdcId != null)) {
			if (beanLogin.Docente) {
				lstSgm = srvDnt.listarSeguimientosxDocenteMateria(
						beanLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), mtrId);
				if (lstSgm.isEmpty()) {
					FacesUtil.mensajeError("No existen actividades realizadas.");
				}
			} else {
				lstSgm = srvEmp.listarSeguimientosxDocente(fcdcId);
				if (lstSgm.isEmpty()) {
					FacesUtil.mensajeError("No existen actividades realizadas.");
				}
			}
		} else {
			FacesUtil.mensajeError("Selecciones los campos de busqueda");
		}
	}

	public void guardarVerificacion() {
		for (Seguimiento sgm : lstSgm) {
			srvSgm.guardarActualizarSeguimiento(sgm);
		}
	}

	public String regresar() {
		crrId = null;
		mtrId = null;
		fcdcId = null;
		selectCrr = null;
		selectMtr = null;
		selectDcn = null;
		lstC = null;
		lstD = null;
		lstM = null;
		lstSgm = null;
		return "principal";
	}

	// setters and getters

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

	public Integer getFcdcId() {
		return fcdcId;
	}

	public void setFcdcId(Integer fcdcId) {
		this.fcdcId = fcdcId;
	}

	public Integer getMtrId() {
		return mtrId;
	}

	public void setMtrId(Integer mtrId) {
		this.mtrId = mtrId;
	}

	public Carrera getSelectCrr() {
		return selectCrr;
	}

	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
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

	public boolean isMostrarFiltro() {
		return mostrarFiltro;
	}

	public void setMostrarFiltro(boolean mostrarFiltro) {
		this.mostrarFiltro = mostrarFiltro;
	}

}
