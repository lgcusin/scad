package managedBeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import model.FichaDocente;
import model.FichaEmpleado;
import servicios.SrvDocenteLocal;

@ManagedBean(name = "principal")
@SessionScoped
public class Principal {

	@EJB
	private SrvDocenteLocal srvDcnt;

	boolean docente = false;
	boolean empleado = false;

	public Integer fdId;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Login l = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		if (l.isDocente()) {
			docente = l.isDocente();
			fdId = l.getFd().getFcdcId();
		}
		if (l.isEmpleado()) {
			empleado = l.isEmpleado();
		}
	}

	public String verDetalle(boolean sesionUsuarioForm, String rol) {
		if (sesionUsuarioForm) {
			switch (rol) {
			case "DOCENTE":
				return "detalleAsistencia";
			case "EMPLEADO":
				return "detalleAsistencia";
			default:
				return null;
			}
		} else {
			return null;
		}

	}

	public String verActividades(boolean sesionUsuarioForm, String rol) {
		if (sesionUsuarioForm) {
			switch (rol) {
			case "DOCENTE":
				return "detalleActividad";
			case "EMPLEADO":
				return "detalleActividad";
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	public String verSyllabo() {
		return "syllabo";
	}

	public String regParalelo() {
		return "registroParalelo";
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

	public boolean isDocente() {
		return docente;
	}

	public void setDocente(boolean docente) {
		this.docente = docente;
	}

	public boolean isEmpleado() {
		return empleado;
	}

	public void setEmpleado(boolean empleado) {
		this.empleado = empleado;
	}

	public Integer getFdId() {
		return fdId;
	}

	public void setFdId(Integer fdId) {
		this.fdId = fdId;
	}

	// setters and getters

}
