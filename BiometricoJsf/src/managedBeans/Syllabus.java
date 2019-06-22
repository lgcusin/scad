package managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.event.RowEditEvent;

import model.Actividad;
import model.Bibliografia;
import model.Carrera;
import model.Contenido;
import model.Feriado;
import model.Herramienta;
import model.MallaCurricularMateria;
import model.Materia;
import model.Metodologia;
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
	public MallaCurricularMateria mllCrrMateria;
	public Syllabo syl;
	public UnidadCurricular uc;
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

	public String tipAsignatura;
	public String prdAcademico;

	public List<UnidadCurricular> lstUnidadCurriculars;
	public List<Contenido> lstContenidos;
	public List<Actividad> lstActividads;
	public List<Herramienta> lstherramientas;
	public List<Metodologia> lstMetodologias;
	public List<Bibliografia> lstBibliografias;

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
		mllCrrMateria = srvSgm.getMallaCurricularMateria(selectMtr.getMtrId());
		// selectCrr = mllCrrMateria.getMateria().getCarrera();
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

	public String crearSyllabus() {
		modiSyllabo = true;
		syl = new Syllabo();
		lstUnidadCurriculars = new ArrayList<>();
		return null;
	}

	public String guardarSyllabus() {
		mllCrrMateria = srvSgm.getMallaCurricularMateria(selectMtr.getMtrId());
		syl.setSylId(mllCrrMateria.getMlcrmtId());
		syl.setMallaCurricularMateria(mllCrrMateria);

		srvSgm.guardarActualizarSyllabus(syl);
		if (!lstUC.isEmpty()) {
			for (UnidadCurricular unidad : lstUC) {
				srvSgm.guardarActualizarUnidad(unidad);
				for (Contenido contenido : unidad.getContenidos()) {
					srvSgm.guardarContenido(contenido);
					for (Actividad actividad : contenido.getActividads()) {
						srvSgm.guardarActualizarActividad(actividad);

					}
					for (Herramienta herramienta : contenido.getHerramientas()) {
						srvSgm.guardarActualizarHerramienta(herramienta);
					}
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
		lstUnidadCurriculars.add(uc);
		lstContenidos = new ArrayList<>();
		lstMetodologias = new ArrayList<>();
		syl.setUnidadCurriculars(lstUnidadCurriculars);
		return "unidadCurricular";
	}

	public void onAddNewCnt() {
		Contenido contenido = new Contenido();
		lstActividads = new ArrayList<>();
		lstherramientas = new ArrayList<>();
		contenido.setUnidadCurricular(uc);
		contenido.setActividads(lstActividads);
		contenido.setHerramientas(lstherramientas);
		lstContenidos.add(contenido);
		uc.setContenidos(lstContenidos);
	}

	public void onAddNewAct(Contenido cnt) {
		Actividad actividad = new Actividad();
		actividad.setContenido(cnt);
		lstActividads = cnt.getActividads();
		cnt.getActividads().add(actividad);
	}

	public void onAddNewHrr(Contenido cnt) {
		Herramienta herramienta = new Herramienta();
		herramienta.setContenido(cnt);
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
		Contenido cnt = (Contenido) event.getObject();
		if (cnt.getCntDescripcion() == null || cnt.getCntDescripcion().trim().isEmpty()) {
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
			for (Contenido contenido : lstContenidos) {
				if (cnt.equals(contenido)) {
					lstContenidos.set(lstContenidos.indexOf(contenido), cnt);
				}
			}
			uc.setContenidos(lstContenidos);
			FacesMessage msg = new FacesMessage("Registro editado", ((Contenido) event.getObject()).getCntId() + "");
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

	public void eliminarContenido(Contenido cnt) {
		uc.getContenidos().remove(cnt);
		lstContenidos = uc.getContenidos();
	}

	public void eliminarActividad(Contenido cnt, Actividad act) {
		cnt.getActividads().remove(act);
	}

	public void eliminarHerramienta(Contenido cnt, Herramienta hrr) {
		cnt.getHerramientas().remove(hrr);
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
		if (lstContenidos.isEmpty()) {
			FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN, "Debe contener uno o mas contenidos",
					null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		} else {
			for (Contenido cnt : lstContenidos) {
				if (cnt.getCntDescripcion() == null || cnt.getCntDescripcion().trim().isEmpty()) {
					FacesMessage msg = new FacesMessage(new FacesMessage().SEVERITY_WARN,
							"Los contenidos no deben ser vacios", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				}
			}
		}

		uc.setUncrDescripcion("UNIDAD CURRICULAR No. " + Integer.toString(lstUnidadCurriculars.indexOf(uc) + 1));
		lstUnidadCurriculars.set(lstUnidadCurriculars.indexOf(uc), uc);
		lstUC = lstUnidadCurriculars;
		uc = null;
		return "syllabo";
	}

	public String CancelUnidadCurricular() {
		lstUnidadCurriculars.remove(uc);
		uc = null;
		return "syllabo";
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

	public List<Contenido> getLstContenidos() {
		return lstContenidos;
	}

	public void setLstContenidos(List<Contenido> lstContenidos) {
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
