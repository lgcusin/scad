package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.event.RowEditEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.uce.edu.biometrico.jpa.Actividad;
import ec.uce.edu.biometrico.jpa.Bibliografia;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.Feriado;
import ec.uce.edu.biometrico.jpa.Herramienta;
import ec.uce.edu.biometrico.jpa.MallaCurricularMateria;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Metodologia;
import ec.uce.edu.biometrico.jpa.Syllabo;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

@ManagedBean(name = "syllabus")
@SessionScoped
public class Syllabus {

	@EJB
	private SrvSeguimientoLocal srvSgm;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	private Login beanLogin;

	public Materia selectMtr;
	public MallaCurricularMateria mllCrrMateria;
	public Syllabo syl;
	public UnidadCurricular uc;
	public Carrera selectCrr;
	public Integer crrId;
	public List<Carrera> lstC;
	public List<Materia> lstM;
	public List<UnidadCurricular> lstUC;
	public List<ContenidoCurricular> lstCnt;
	public List<Actividad> lstAct;
	public List<Herramienta> lstHrr;
	public boolean dataSyllabo;
	public boolean modiSyllabo;
	public boolean crearSyllabo;

	public String tipAsignatura;
	public String prdAcademico;

	public List<UnidadCurricular> lstUnidadCurriculars;
	public List<ContenidoCurricular> lstContenidos;
	public List<Actividad> lstActividads;
	public List<Herramienta> lstherramientas;
	public List<Metodologia> lstMetodologias;
	public List<Bibliografia> lstBibliografias;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		if (beanLogin.isDocente()) {
			selectMtr = new Materia();
			lstC = srvSgm.listarAllCrrByFcdc(
					beanLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId());
			lstM = srvSgm.listarAllMatByFcdc(
					beanLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId());
		}
		if (beanLogin.isAdminFacultad()) {
			lstC = srvEmp.listarCarrerasxFacultad(beanLogin.getDt().get(0).getCarrera().getDependencia().getDpnId());
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
		mllCrrMateria = srvSgm.getMallaCurricularMateria(selectMtr.getMtrId());
		// selectCrr = mllCrrMateria.getMateria().getCarrera();
		if (selectMtr.getMtrId() != null) {
			syl = srvSgm.getSyllabus(selectMtr.getMtrId());
			lstUC = srvSgm.listarUnidadCurricular(selectMtr.getMtrId());
			if (syl != null && !lstUC.isEmpty()) {
				for (UnidadCurricular unidad : lstUC) {
					lstCnt = srvSgm.listarContenidos(unidad.getUncrId());
					for (ContenidoCurricular contenidoCurricular : lstCnt) {
						lstAct = srvSgm.listarActividades(contenidoCurricular.getCncrId());
						lstHrr = srvSgm.listarHerramientas(contenidoCurricular.getCncrId());
						contenidoCurricular.setActividads(lstAct);
						contenidoCurricular.setHerramientas(lstHrr);
					}
					unidad.setContenidos(lstCnt);
					unidad.setMetodologias(srvSgm.listarMetodologias(unidad.getUncrId()));
					unidad.setBibliografias(srvSgm.listarBibliografias(unidad.getUncrId()));
				}

				syl.setUnidadCurriculars(lstUC);
				dataSyllabo = true;
			} else {
				lstUC = new ArrayList<>();
				dataSyllabo = false;
				crearSyllabo = true;
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione una materia", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		return "syllabo";
	}

	public String modificar() {
		modiSyllabo = true;
		dataSyllabo = false;
		crearSyllabo = false;
		return null;
	}

	public String crearSyllabus() {
		modiSyllabo = true;
		crearSyllabo = false;
		syl = new Syllabo();
		lstUnidadCurriculars = new ArrayList<>();
		return null;
	}

	public String guardarSyllabus() {
		mllCrrMateria = srvSgm.getMallaCurricularMateria(selectMtr.getMtrId());
		if (!lstUC.isEmpty()) {
			syl.setSylId(mllCrrMateria.getMlcrmtId());
			syl.setMallaCurricularMateria(mllCrrMateria);
			srvSgm.guardarActualizarSyllabus(syl);
			for (UnidadCurricular unidad : lstUC) {
				srvSgm.guardarActualizarUnidad(unidad);
				for (ContenidoCurricular contenidoCurricular : unidad.getContenidos()) {
					srvSgm.guardarActualizarContenido(contenidoCurricular);
					for (Actividad actividad : contenidoCurricular.getActividads()) {
						srvSgm.guardarActualizarActividad(actividad);

					}
					for (Herramienta herramienta : contenidoCurricular.getHerramientas()) {
						srvSgm.guardarActualizarHerramienta(herramienta);
					}
				}
				for (Metodologia metodologia : unidad.getMetodologias()) {
					srvSgm.guardarActualizarMetodologia(metodologia);
				}
				for (Bibliografia bibliografia : unidad.getBibliografias()) {
					srvSgm.guardarActualizarBibliografia(bibliografia);
				}
			}
		} else {
			FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN,
					"No existe unidad curricular para guardar", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		dataSyllabo = true;
		syl = null;
		lstUC = null;
		return "syllabus";
	}

	public String regresar() {
		dataSyllabo = false;
		modiSyllabo = false;
		crearSyllabo = false;
		if (syl != null) {
			syl = null;
			lstUC = null;
		}
		return "syllabus";
	}

	public String regresarPrincipal() {
		lstM = null;
		return "principal";
	}

	public void limpiarSesion(ComponentSystemEvent event) {
		lstM = null;

	}

	public String verUnidadCurricular() {
		uc = new UnidadCurricular();
		uc.setSyllabo(syl);
		if (!lstUC.isEmpty()) {
		} else {
			lstUnidadCurriculars.add(uc);
			syl.setUnidadCurriculars(lstUnidadCurriculars);
		}
		lstContenidos = new ArrayList<>();
		lstMetodologias = new ArrayList<>();
		return "unidadCurricular";
	}

	public void onAddNewCnt() {
		ContenidoCurricular contenidoCurricular = new ContenidoCurricular();
		lstActividads = new ArrayList<>();
		lstherramientas = new ArrayList<>();
		contenidoCurricular.setUnidadCurricular(uc);
		contenidoCurricular.setActividads(lstActividads);
		contenidoCurricular.setHerramientas(lstherramientas);
		uc.getContenidos().add(contenidoCurricular);
	}

	public void onAddNewAct(ContenidoCurricular cnt) {
		Actividad actividad = new Actividad();
		actividad.setContenidoCurricular(cnt);
		lstActividads = cnt.getActividads();
		cnt.getActividads().add(actividad);
	}

	public void onAddNewHrr(ContenidoCurricular cnt) {
		Herramienta herramienta = new Herramienta();
		herramienta.setContenidoCurricular(cnt);
		lstherramientas = cnt.getHerramientas();
		cnt.getHerramientas().add(herramienta);
	}

	public void onAddNewMtd() {
		Metodologia mtd = new Metodologia();
		mtd.setUnidadCurricular(uc);
		lstMetodologias = uc.getMetodologias();
		uc.getMetodologias().add(mtd);
	}

	public void onAddNewBbl() {
		Bibliografia bbl = new Bibliografia();
		bbl.setUnidadCurricular(uc);
		lstBibliografias = uc.getBibliografias();
		uc.getBibliografias().add(bbl);
	}

	public void onRowEditCnt(RowEditEvent event) {
		ContenidoCurricular cnt = (ContenidoCurricular) event.getObject();
		if (cnt.getCncrDescripcion() == null || cnt.getCncrDescripcion().trim().isEmpty()) {
			FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN,
					"La descripcion del contenido esta vacio", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else if (cnt.getActividads().isEmpty() || cnt.getHerramientas().isEmpty()) {
			FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN,
					"Actividades y/o Mecanismos no pueden estar vacias", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			lstActividads = cnt.getActividads();
			for (Actividad actividad : lstActividads) {
				if (actividad.getActDescripcion().trim().isEmpty()) {
					lstActividads.remove(actividad);
				}
			}
			cnt.setActividads(lstActividads);
			lstherramientas = cnt.getHerramientas();
			for (Herramienta herramienta : lstherramientas) {
				if (herramienta.getHrrNombre().trim().isEmpty()) {
					lstherramientas.remove(herramienta);
				}
			}
			cnt.setHerramientas(lstherramientas);
			lstContenidos = uc.getContenidos();
			for (ContenidoCurricular contenidoCurricular : lstContenidos) {
				if (cnt.equals(contenidoCurricular)) {
					lstContenidos.set(lstContenidos.indexOf(contenidoCurricular), cnt);
				}
			}
			uc.setContenidos(lstContenidos);
			FacesMessage msg = new FacesMessage("Registro editado",
					((ContenidoCurricular) event.getObject()).getCncrId() + "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void onRowEditAct(RowEditEvent event) {
		Actividad act = (Actividad) event.getObject();
		// lstActividads.set(act.getActId() - 1, act);
		FacesMessage msg = new FacesMessage("Actividad editado", ((Actividad) event.getObject()).getActId() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowEditHrr(RowEditEvent event) {
		Herramienta hrr = (Herramienta) event.getObject();
		// lstherramientas.set(hrr.getHrrId() - 1, hrr);
		FacesMessage msg = new FacesMessage("Registro editado", ((Herramienta) event.getObject()).getHrrId() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowEditMtd(RowEditEvent event) {
		Metodologia mtd = (Metodologia) event.getObject();
		// lstherramientas.set(hrr.getHrrId() - 1, hrr);
		FacesMessage msg = new FacesMessage("Registro editado", ((Metodologia) event.getObject()).getMtdId() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowEditBbl(RowEditEvent event) {
		Bibliografia bbl = (Bibliografia) event.getObject();
		// lstherramientas.set(hrr.getHrrId() - 1, hrr);
		FacesMessage msg = new FacesMessage("Registro editado", ((Bibliografia) event.getObject()).getBblId() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void eliminarContenido(ContenidoCurricular cnt) {
		uc.getContenidos().remove(cnt);
		lstContenidos = uc.getContenidos();
	}

	public void eliminarActividad(ContenidoCurricular cnt, Actividad act) {
		cnt.getActividads().remove(act);
	}

	public void eliminarHerramienta(ContenidoCurricular cnt, Herramienta hrr) {
		cnt.getHerramientas().remove(hrr);
	}

	public void eliminarMetodologia(Metodologia mtd) {
		uc.getMetodologias().remove(mtd);
		lstMetodologias = uc.getMetodologias();
	}

	public void eliminarBibliografia(Bibliografia bbl) {
		uc.getBibliografias().remove(bbl);
		lstBibliografias = uc.getBibliografias();
	}

	public void onRowCancelCnt(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelAct(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelHrr(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelMtd(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelBbl(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String AddNewUnidadCurricular() {
		if (uc.getContenidos().isEmpty()) {
			FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN, "Debe contener uno o mas contenidos",
					null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		} else {
			for (ContenidoCurricular cnt : uc.getContenidos()) {
				if (cnt.getCncrDescripcion() == null || cnt.getCncrDescripcion().trim().isEmpty()) {
					FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN,
							"Los contenidos no deben ser vacios", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				}
			}
		}
		if (uc.getUncrDescripcion() == null) {
			uc.setUncrDescripcion("UNIDAD CURRICULAR No. " + lstUC.indexOf(uc) + 1);
			lstUC.add(uc);
		} else if (uc.getUncrDescripcion().trim().isEmpty()) {
			//uc.setUncrDescripcion("UNIDAD CURRICULAR No. " + lstUC.indexOf(uc)  + 1);
			lstUC.set(lstUC.indexOf(uc), uc);
		}
		uc = null;
		return "syllabo";
	}

	public String CancelUnidadCurricular() {
		uc = null;
		return "syllabo";
	}

	public String modificarUnCr() {
		return "unidadCurricular";

	}

	public void eliminarUnCr(Boolean confirm) {
		if (confirm) {
			syl.getUnidadCurriculars().remove(uc);
			srvSgm.eliminarUnidad(uc);
			FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_INFO, "Unidad eliminada", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	// Setters and getters

	public List<Materia> getLstM() {
		return lstM;
	}

	public Materia getSelectMtr() {
		return selectMtr;
	}

	public MallaCurricularMateria getMllCrrMateria() {
		return mllCrrMateria;
	}

	public void setMllCrrMateria(MallaCurricularMateria mllCrrMateria) {
		this.mllCrrMateria = mllCrrMateria;
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

	public boolean isCrearSyllabo() {
		return crearSyllabo;
	}

	public void setCrearSyllabo(boolean crearSyllabo) {
		this.crearSyllabo = crearSyllabo;
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

	/** Metodos para agregar datos del fomulario del sillabo **/

	public UnidadCurricular getUc() {
		return uc;
	}

	public void setUc(UnidadCurricular uc) {
		this.uc = uc;
	}

	public String getTipAsignatura() {
		return tipAsignatura;
	}

	public void setTipAsignatura(String tipAsignatura) {
		this.tipAsignatura = tipAsignatura;
	}

	public String getPrdAcademico() {
		return prdAcademico;
	}

	public void setPrdAcademico(String prdAcademico) {
		this.prdAcademico = prdAcademico;
	}

	public List<ContenidoCurricular> getLstContenidos() {
		return lstContenidos;
	}

	public void setLstContenidos(List<ContenidoCurricular> lstContenidos) {
		this.lstContenidos = lstContenidos;
	}

	public List<Actividad> getLstActividads() {
		return lstActividads;
	}

	public void setLstActividads(List<Actividad> lstActividads) {
		this.lstActividads = lstActividads;
	}

	public List<Herramienta> getLstherramientas() {
		return lstherramientas;
	}

	public void setLstherramientas(List<Herramienta> lstherramientas) {
		this.lstherramientas = lstherramientas;
	}

	public List<Metodologia> getLstMetodologias() {
		return lstMetodologias;
	}

	public void setLstMetodologias(List<Metodologia> lstMetodologias) {
		this.lstMetodologias = lstMetodologias;
	}

	public List<Bibliografia> getLstBibliografias() {
		return lstBibliografias;
	}

	public void setLstBibliografias(List<Bibliografia> lstBibliografias) {
		this.lstBibliografias = lstBibliografias;
	}

}
