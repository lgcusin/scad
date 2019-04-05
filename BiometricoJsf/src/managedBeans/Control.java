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
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import lector.JSDCLocal;
import model.Actividad;
import model.Asistencia;
import model.Aula;
import model.Contenido;
import model.FichaDocente;
import model.Horario;
import model.Materia;
import model.UnidadCurricular;
import servicios.SrvSeguimientoLocal;

@ManagedBean(name = "control")
@ViewScoped
public class Control {

	@EJB
	private JSDCLocal srvJsdc;
	@EJB
	private SrvSeguimientoLocal srvSgmt;

	private FichaDocente regDcnt;
	private Horario hrrI;
	private Horario hrrF;
	private Asistencia regAss;
	private List<Asistencia> lstAss;
	private List<Contenido> lstCnt;
	private List<String> selecCnts;
	private TreeNode root1;
	private TreeNode root2;
	private TreeNode root3;

	public String hora;
	public Date ahora;
	public SimpleDateFormat formateador;
	public boolean generado = true;
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
	public String registrar() throws SQLException, IOException {
		flagDlg = false;
		flagIni = false;
		flagFin = false;
		regDcnt = srvJsdc.comparar();
		if (regDcnt != null) {
			// Compara si hay un horario con la hora de entrada
			hrrI = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true);
			hrrF = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), false);

			if (hrrI != null && hrrF != null) {
				lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				for (Asistencia asistencia : lstAss) {
					if (asistencia.getHorario().getHrrId() == hrrF.getHrrId()
							&& asistencia.getAssEstado() == "Iniciado") {
						return finalizarClase(asistencia);
					} else if (asistencia.getHorario().getHrrId() == hrrI.getHrrId()
							&& asistencia.getAssEstado() == null) {
						return inicializarClase(asistencia);
					} else if (asistencia.getHorario().getHrrId() == hrrI.getHrrId()
							&& asistencia.getAssEstado().equals("Iniciado")) {
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Registro de entrada, ya existe", null);
						FacesContext.getCurrentInstance().addMessage(null, msg);
						return null;
					} else if (asistencia.getHorario().getHrrId() == hrrF.getHrrId()
							&& asistencia.getAssEstado() == null) {
						if (asistencia.getHorario().getHrrId() == hrrI.getHrrId()
								&& asistencia.getAssEstado() == null) {
							return inicializarClase(asistencia);
						}
					}
				}
			}

			if (hrrI != null && hrrF == null) {
				lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				if (lstAss.isEmpty()) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"No existen horarios para iniciar una clase", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				} else {
					for (Asistencia asistencia : lstAss) {
						if (asistencia.getHorario().getHrrId() == hrrI.getHrrId()
								&& asistencia.getAssEstado() == null) {
							return inicializarClase(asistencia);
						} else if (asistencia.getHorario().getHrrId() == hrrI.getHrrId()
								&& asistencia.getAssEstado().equals("Iniciado")) {
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Registro de entrada, ya existe", null);
							FacesContext.getCurrentInstance().addMessage(null, msg);
							return null;
						}
					}
				}
			}

			if (hrrI == null && hrrF != null) {
				lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				if (lstAss.isEmpty()) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"No existen horarios para finalizar una clase", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				} else {
					for (Asistencia asistencia : lstAss) {
						if (asistencia.getHorario().getHrrId() == hrrF.getHrrId()
								&& asistencia.getAssEstado().equals("Iniciado")) {
							return finalizarClase(asistencia);
						} else if (asistencia.getHorario().getHrrId() == hrrF.getHrrId()
								&& asistencia.getAssEstado().equals("Finalizado")) {
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Registro de salida, ya existe", null);
							FacesContext.getCurrentInstance().addMessage(null, msg);
							return null;
						}
					}
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "No existe una clase, iniciada",
							null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				}
			}

			if (hrrI == null && hrrF == null) {
				System.out.println("No existe un horario o está fuera del plazo establecido");
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"No existe un horario o está fuera del plazo establecido", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}
		} else {
			// RequestContext.getCurrentInstance().addCallbackParam("flagDlg",
			// flagDlg);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Docente no registrado", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		return null;

	}

	private String inicializarClase(Asistencia asistencia) {
		regAss = asistencia;
		regAss.setFichaDocente(regDcnt);
		formateador = new SimpleDateFormat("HH:mm:ss");
		regAss.setAssHoraEntrada(formateador.format(ahora));
		regAss.setAssEstado("Iniciado");
		lstCnt = srvSgmt.getContenidos(hrrI.getMateria().getMtrId());
		// root1 = createCheckboxDocuments();
		flagIni = true;
		flagDlg = true;
		RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
		return null;
	}

	private String finalizarClase(Asistencia asistencia) {
		regAss = asistencia;
		formateador = new SimpleDateFormat("HH:mm:ss");
		regAss.setAssHoraSalida(formateador.format(ahora));
		regAss.setAssEstado("Finalizado");
		lstCnt = srvSgmt.getContenidos(hrrF.getMateria().getMtrId());
		flagFin = true;
		flagDlg = true;
		RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
		return null;
	}

	public void guardarAsis() {
		flagDlg = false;
		try {
			if (selecCnts.isEmpty()) {
				flagDlg = true;
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe escoger una Actividad", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			} else {
				srvSgmt.guardarRegistro(regAss);
				for (int i = 0; i < selecCnts.size(); i++) {
					for (int j = 0; j < lstCnt.size(); j++) {
						if (selecCnts.get(i).equals(lstCnt.get(j).getCntDescripcion())) {
							lstCnt.get(j).setCntEstado("COMPLETADO");
							srvSgmt.guardarContenido(lstCnt.get(j));
							break;
						}
					}
				}
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Asistencia guardada", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al guardar,intente nuevamente",
					e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public TreeNode createCheckboxDocuments() {
		TreeNode root = new DefaultTreeNode("Root", null);
		List<UnidadCurricular> lstUn = srvSgmt.listarUnidadCurricular(hrrI.getMateria().getMtrId());
		for (UnidadCurricular unidadCurricular : lstUn) {
			TreeNode unidad = new DefaultTreeNode(unidadCurricular.getUncrDescripcion(), root);
			List<Contenido> lstCn = srvSgmt.listarContenidos(unidadCurricular.getUncrId());
			for (Contenido contenido : lstCn) {
				TreeNode contenidos = new DefaultTreeNode(contenido.getCntDescripcion(), unidad);
				// List<Actividad> lstAc=
				// srvSgmt.listarActividades(contenido.getCntId());
				// for (Actividad actividad : lstAc) {
				// TreeNode actividads = new
				// DefaultTreeNode(actividad.getActDescripcion(),contenidos);
				// }
			}
		}

		return root;

	}

	public void cerrarDiag() {
		selecCnts = null;
		lstCnt = null;
		regAss = null;
	}

	public void generar() {
		srvSgmt.generar(ahora);
		generado = false;
	}

	// setters and getters

	public String getHora() {
		return hora;
	}

	public boolean isGenerado() {
		return generado;
	}

	public void setGenerado(boolean generado) {
		this.generado = generado;
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

	public Horario getHrrI() {
		return hrrI;
	}

	public void setHrrI(Horario hrrI) {
		this.hrrI = hrrI;
	}

	public Horario getHrrF() {
		return hrrF;
	}

	public void setHrrF(Horario hrrF) {
		this.hrrF = hrrF;
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

	public TreeNode getRoot1() {
		return root1;
	}

	public void setRoot1(TreeNode root1) {
		this.root1 = root1;
	}

}
