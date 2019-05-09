package managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.event.RowEditEvent;

import model.Actividad;
import model.Carrera;
import model.Contenido;
import model.Feriado;
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

	public String tipAsignatura;
	public String nmbDocente;
	public String prdAcademico;
	public int nmrHoras;
	public int nmrTutorias;

	public String descripcion;
	public String objGeneral;
	public String objEspecifico;
	public String contribucion;
	public String resultados;

	public String uncrNombre;
	public String uncrObjetivo;
	public String uncrResultado;

	public List<Contenido> lstContenidos;
	public List<Actividad> lstActividads;
	public List<Herramienta> lstherramientas;

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

	public String regresarPrincipal() {
		lstM = null;
		return "principal";
	}

	public void limpiarSesion(ComponentSystemEvent event) {
		lstM = null;

	}

	public void onAddNewCnt() {
		lstContenidos = new ArrayList<>(1);
		Contenido contenido = new Contenido();
		lstContenidos.add(contenido);
	}

	public void onRowEditCnt(RowEditEvent event) {
		actualizarContenido((Contenido) event.getObject());
		FacesMessage msg = new FacesMessage("Registro editado", ((Contenido) event.getObject()).getCntId() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	private void actualizarContenido(Contenido object) {
		// TODO Auto-generated method stub

	}

	public void onAddNewAct() {
		lstActividads = new ArrayList<>(1);
		Actividad actividad = new Actividad();
		lstActividads.add(actividad);
	}

	public void onRowEditAct(RowEditEvent event) {
		actualizarActividad((Actividad) event.getObject());
		FacesMessage msg = new FacesMessage("Registro editado", ((Actividad) event.getObject()).getActId() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	private void actualizarActividad(Actividad object) {
		// TODO Auto-generated method stub

	}

	public void onRowCancelCnt(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
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

	/** Metodos para agregar datos del fomulario del sillabo **/

	public String getTipAsignatura() {
		return tipAsignatura;
	}

	public void setTipAsignatura(String tipAsignatura) {
		this.tipAsignatura = tipAsignatura;
	}

	public String getNmbDocente() {
		return nmbDocente;
	}

	public void setNmbDocente(String nmbDocente) {
		this.nmbDocente = nmbDocente;
	}

	public String getPrdAcademico() {
		return prdAcademico;
	}

	public void setPrdAcademico(String prdAcademico) {
		this.prdAcademico = prdAcademico;
	}

	public int getNmrHoras() {
		return nmrHoras;
	}

	public void setNmrHoras(int nmrHoras) {
		this.nmrHoras = nmrHoras;
	}

	public int getNmrTutorias() {
		return nmrTutorias;
	}

	public void setNmrTutorias(int nmrTutorias) {
		this.nmrTutorias = nmrTutorias;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObjGeneral() {
		return objGeneral;
	}

	public void setObjGeneral(String objGeneral) {
		this.objGeneral = objGeneral;
	}

	public String getObjEspecifico() {
		return objEspecifico;
	}

	public void setObjEspecifico(String objEspecifico) {
		this.objEspecifico = objEspecifico;
	}

	public String getContribucion() {
		return contribucion;
	}

	public void setContribucion(String contribucion) {
		this.contribucion = contribucion;
	}

	public String getResultados() {
		return resultados;
	}

	public void setResultados(String resultados) {
		this.resultados = resultados;
	}

	public String getUncrNombre() {
		return uncrNombre;
	}

	public void setUncrNombre(String uncrNombre) {
		this.uncrNombre = uncrNombre;
	}

	public String getUncrObjetivo() {
		return uncrObjetivo;
	}

	public void setUncrObjetivo(String uncrObjetivo) {
		this.uncrObjetivo = uncrObjetivo;
	}

	public String getUncrResultado() {
		return uncrResultado;
	}

	public void setUncrResultado(String uncrResultado) {
		this.uncrResultado = uncrResultado;
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

}
