package managedBeans;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lector.JSDCLocal;
import model.Asistencia;
import model.Aula;
import model.Contenido;
import model.FichaDocente;
import model.Horario;
import model.Materia;
import servicios.SrvSeguimientoLocal;

@ManagedBean(name = "control")
@ViewScoped
public class Control {

	@EJB
	private JSDCLocal srvJsdc;
	@EJB
	private SrvSeguimientoLocal srvSgmt;

	private FichaDocente regDcnt;
	private Horario hrr;
	private Materia mtr;
	private Aula aul;
	private Asistencia regAss;
	private List<Asistencia> lstAss;
	private List<Contenido> lstCnt;
	private List<String> selecCnts;

	public String hora;
	public Date ahora;
	public SimpleDateFormat formateador;
	public boolean flagDlg;
	public boolean flagIni;
	public boolean flagFin;

	@PostConstruct
	public void init() {
		srvJsdc.inicializar();
		regDcnt = new FichaDocente();
	}

	public void temporizador() {
		ahora = new Date();
		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
	}

	@SuppressWarnings("deprecation")
	public void registrar() throws SQLException, IOException {
		flagDlg = false;
		flagIni = false;
		flagFin = false;
		regDcnt = srvJsdc.comparar();
		if (regDcnt != null) {
			// Compara si hay un horario con la hora de salida
			hrr = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), false);
			if (hrr != null) {
				lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				if (lstAss.isEmpty()) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Asistencia Fin clase, ya registrada", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					regAss = lstAss.get(0);
					formateador = new SimpleDateFormat("HH:mm:ss");
					regAss.setAssHoraSalida(formateador.format(ahora));
					regAss.setAssEstado("Finalizado");
					lstCnt = srvSgmt.getContenidos(mtr.getMtrId());
					flagFin = true;
					flagDlg = true;
					RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
				}

			} else {
				System.out.println("No existe un horario o está fuera del plazo establecido");
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"No existe un horario o está fuera del plazo establecido", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);

			}
			
			// Compara si hay un horario con la hora de entrada
			hrr = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true);
			if (hrr != null) {
				lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				if (lstAss.isEmpty()) {
					regAss = new Asistencia();
					regAss.setFichaDocente(regDcnt);
					formateador = new SimpleDateFormat("yyyy/MM/dd");
					regAss.setAssFecha(new Date(formateador.format(ahora)));
					formateador = new SimpleDateFormat("HH:mm:ss");
					regAss.setAssHoraEntrada(formateador.format(ahora));
					regAss.setAssEstado("Iniciado");
					lstCnt = srvSgmt.getContenidos(mtr.getMtrId());
					flagIni = true;
					flagDlg = true;
					RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
				} else {
					// RequestContext.getCurrentInstance().addCallbackParam("flagDlg",
					// flagDlg);
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Asistencia Inicio clase, ya registrada", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);

				}

			} else {
			

			}
		} else {
			// RequestContext.getCurrentInstance().addCallbackParam("flagDlg",
			// flagDlg);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Docente no registrado", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void guardarAsis() {
		flagDlg = false;
		RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
		try {
			srvSgmt.guardarRegistro(regAss);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Asistencia guardada", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al guardar,intente nuevamente",
					e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void cerrarDiag() {
		selecCnts = null;
		lstCnt = null;
		regAss = null;
	}

	// setters and getters

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public FichaDocente getRegDcnt() {
		return regDcnt;
	}

	public void setRegDcnt(FichaDocente regDcnt) {
		this.regDcnt = regDcnt;
	}

	public Horario getHrr() {
		return hrr;
	}

	public void setHrr(Horario hrr) {
		this.hrr = hrr;
	}

	public List<Contenido> getLstCnt() {
		return lstCnt;
	}

	public void setLstCnt(List<Contenido> lstCnt) {
		this.lstCnt = lstCnt;
	}

	public List<String> getSelecCnts() {
		return selecCnts;
	}

	public void setSelecCnts(List<String> selecCnts) {
		this.selecCnts = selecCnts;
	}

	public Asistencia getRegAss() {
		return regAss;
	}

	public void setRegAss(Asistencia regAss) {
		this.regAss = regAss;
	}

	public boolean isFlagIni() {
		return flagIni;
	}

	public void setFlagIni(boolean flagIni) {
		this.flagIni = flagIni;
	}

	public boolean isFlagFin() {
		return flagFin;
	}

	public void setFlagFin(boolean flagFin) {
		this.flagFin = flagFin;
	}

}
