package ec.edu.uce.Biometrico.jsf.managedBeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.biometrico.jpa.FichaDocente;
import ec.edu.uce.biometrico.jpa.FichaEmpleado;

@ManagedBean(name = "principal")
@ViewScoped
public class Principal {

	@EJB
	private SrvDocenteLocal srvDcnt;
	public Login beanLogin;

	public FichaDocente docente;
	public FichaEmpleado empleado;
	boolean flagDocente;
	boolean flagEmpleado;
	public Integer fdId;
	public Integer fcId;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		if (beanLogin.isDocente()) {
			fdId = beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaDocentes().get(0).getFcdcId();
			fcId = beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaDocentes().get(0).getFcdcDetallePuestos()
					.get(0).getDtpsCarrera().getCrrDependencia().getDpnId();
		} else if (beanLogin.isAdminFacultad()) {
			fcId = beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaEmpleados().get(0).getDetallePuestos()
					.get(0).getDtpsCarrera().getCrrDependencia().getDpnId();
		}
	}

	public String verDetalleAsistencia(boolean sesionUsuarioForm, String rol) {
		if (sesionUsuarioForm) {
			switch (rol) {
			case "DOCENTE":
				flagDocente = beanLogin.isDocente();
				return "detalleAsistencia";
			case "EMPLEADO":
				flagEmpleado = beanLogin.isAdminFacultad();
				return "detalleAsistencia";
			default:
				return null;
			}
		} else {
			return null;
		}

	}

	public String verDetalleActividades(boolean sesionUsuarioForm, String rol) {
		if (sesionUsuarioForm) {
			switch (rol) {
			case "DOCENTE":
				flagDocente = beanLogin.isDocente();
				return "detalleActividad";
			case "EMPLEADO":
				flagEmpleado = beanLogin.isAdminFacultad();
				return "detalleActividad";
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	public String verSyllabo() {
		return "syllabus";
	}

	public String verReporteHorario(boolean sesionUsuarioForm, String rol) {
		if (sesionUsuarioForm) {
			switch (rol) {
			case "DOCENTE":
				return "reporteHorario";
			case "EMPLEADO":
				return "reporteHorario";
			default:
				System.out.println("Rol no encontrado");
				return null;
			}
		} else {
			System.out.println("Ho hay ningun rol");
			return null;
		}

	}

	public String regExamen() {
		return "registroExamen";
	}

	public String regFeriado() {
		return "registroFeriados";
	}

	// Administrador
	public String verRegistro() {
		return "registro";
	}

	public String verSyllabus() {
		return "syllabus";
	}

	public String verControl() {
		return "control";
	}

	public String verJustificacion() {
		return "justificacion";
	}

	public String verParametros() {
		return "administrarParametros";
	}

	public String verGenerarRegistros() {
		return "generarRegistros";
	}

	// SET AND GET

	public boolean isDocente() {
		return flagDocente;
	}

	public void setDocente(boolean docente) {
		this.flagDocente = docente;
	}

	public boolean isEmpleado() {
		return flagEmpleado;
	}

	public void setEmpleado(boolean empleado) {
		this.flagEmpleado = empleado;
	}

	public Integer getFdId() {
		return fdId;
	}

	public void setFdId(Integer fdId) {
		this.fdId = fdId;
	}

	public Integer getFcId() {
		return fcId;
	}

	public void setFcId(Integer fcId) {
		this.fcId = fcId;
	}

}
