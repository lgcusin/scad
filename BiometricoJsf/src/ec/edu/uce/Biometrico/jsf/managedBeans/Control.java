package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.JSDCLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

@ManagedBean(name = "control")
@ViewScoped
public class Control {

	@EJB
	private JSDCLocal srvDvc;
	@EJB
	private SrvSeguimientoLocal srvSgmt;

	private FichaDocente regDcnt;
	private HorarioAcademico hrrI;
	private HorarioAcademico hrrF;
	private Asistencia regAss;
	private List<Asistencia> lstAss;
	private List<ContenidoCurricular> lstCnt;
	private List<Seguimiento> lstSgmt;
	private List<String> selecCnts;

	public String hora;
	public Date ahora;
	public SimpleDateFormat formateador;
	public boolean generado = true;
	public boolean flagDlg;
	public boolean flagIni;
	public boolean flagFin;
	public Integer fclId;
	private String horaClase;
	private String sgmObservacion;
	private Integer sgmHoraClaseRestante;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Login l = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		fclId = l.getUsuarioRol().getUsuario().getPersona().getFichaEmpleados().get(0).getDetallePuestos().get(0)
				.getCarrera().getDependencia().getDpnId();
		iniciar();
	}

	public void iniciar() {
		srvDvc.inicializar();
		srvDvc.onLED();
	}

	@SuppressWarnings("deprecation")
	public void temporizador() throws SQLException, IOException {
		ahora = new Date();
		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
		if (generado && ahora.getHours() == 5) {
			generar();
			generado = false;
		} else if (ahora.getHours() == 6) {
			generado = true;
		}
		// regDcnt = srvDvc.comparar();
		// if (regDcnt != null) {
		// registrar();
		// }

	}

	public String registrar() throws SQLException, IOException {
		flagDlg = false;
		flagIni = false;
		flagFin = false;
		// srvDvc.inicializar();
		regDcnt = new FichaDocente();
		regDcnt = srvDvc.comparar();
		// if (regDcnt == null) {
		// regDcnt.setPersona(new Persona(1388));
		// regDcnt.getPersona().setPrsPrimerApellido("ROSAS");
		// regDcnt.getPersona().setPrsSegundoApellido("LARA");
		// regDcnt.getPersona().setPrsNombres("MAURO LEONARDO");
		// }
		// Compara si hay un horario con la hora de entrada o salida
		hrrI = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true, fclId, 1);
		hrrF = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), false, fclId, 2);

		if (hrrI != null && hrrF.getHracId() != null) {
			lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
			for (Asistencia asistencia : lstAss) {
				if (asistencia.getHorarioAcademico().getHracId().equals(hrrF.getHracId())
						&& asistencia.getAssEstado().equals("INICIADO")
						|| asistencia.getAssEstado().equals("ATRASADO")) {
					return finalizarClase(asistencia);
				} else if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
						&& asistencia.getAssEstado().equals("FALTA")) {
					return inicializarClase(asistencia, "INICIADO");
				} else if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
						&& asistencia.getAssEstado().equals("INICIADO")
						|| asistencia.getAssEstado().equals("ATRASADO")) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Registro de entrada, ya existe",
							null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				} else if (asistencia.getHorarioAcademico().getHracId().equals(hrrF.getHracId())
						&& asistencia.getAssEstado() == null) {

					if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
							&& asistencia.getAssEstado().equals("FALTA")) {
						return inicializarClase(asistencia, "INICIADO");
					}
				}
			}
		}

		if (hrrI.getHracId() != null && hrrF.getHracId() == null) {
			lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
			if (lstAss.isEmpty()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"No existen horarios para iniciar una clase", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			} else {
				for (Asistencia asistencia : lstAss) {
					if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
							&& asistencia.getAssEstado().equals("FALTA")) {
						return inicializarClase(asistencia, "INICIADO");
					} else if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
							&& asistencia.getAssEstado().equals("INICIADO")) {
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Registro de entrada, ya existe", null);
						FacesContext.getCurrentInstance().addMessage(null, msg);
						return null;
					}
				}
			}
		}

		if (hrrI.getHracId() == null && hrrF.getHracId() != null) {
			lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
			if (lstAss.isEmpty()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"No existen horarios para finalizar una clase", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			} else {
				for (Asistencia asistencia : lstAss) {
					if (asistencia.getHorarioAcademico().getHracId().equals(hrrF.getHracId())
							&& asistencia.getAssEstado().equals("INICIADO")
							|| asistencia.getAssEstado().equals("ATRASADO")) {
						return finalizarClase(asistencia);
					} else if (asistencia.getHorarioAcademico().getHracId().equals(hrrF.getHracId())
							&& asistencia.getAssEstado().equals("FINALIZADO")) {
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Registro de salida, ya existe", null);
						FacesContext.getCurrentInstance().addMessage(null, msg);
						return null;
					}
				}
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "No existe una clase, iniciada", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}
		}

		if (hrrI.getHracId() == null && hrrF.getHracId() == null) {
			hrrI = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true, fclId, 0);
			if (hrrI.getHracId() == null) {
				System.out.println("Horario de atraso");
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"No existe un horario o está fuera del plazo establecido", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			} else {
				lstAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				if (lstAss.isEmpty()) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"No existen horarios para iniciar una clase", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null;
				} else {
					for (Asistencia asistencia : lstAss) {
						if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
								&& asistencia.getAssEstado().equals("FALTA")) {
							return inicializarClase(asistencia, "ATRASADO");
						} else if (asistencia.getHorarioAcademico().getHracId().equals(hrrI.getHracId())
								&& asistencia.getAssEstado().equals("ATRASADO") || asistencia.getAssEstado().equals("INICIADO")) {
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Registro de entrada, ya existe", null);
							FacesContext.getCurrentInstance().addMessage(null, msg);
							return null;
						}
					}
				}
			}
		}

		return null;

	}

	private String inicializarClase(Asistencia asistencia, String estado) {
		// Cruzar contenido-syllabus con seguimiento-syllabus
		lstCnt = srvSgmt
				.buscarContenidos(hrrI.getMallaCurricularParalelo().getMallaCurricularMateria().getMateria().getMtrId());
		if (lstCnt.isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "No se encontraron temas de clases",
					"Verifique si existen el syllabus respectivo");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			return null;
		} else {
			lstSgmt = srvSgmt.getSeguimiento(
					hrrI.getMallaCurricularParalelo().getMallaCurricularMateria().getMateria().getMtrId(),
					regDcnt.getFcdcId());
			horaClase = srvSgmt.obtenerHoraClasexHorario(hrrI);
			int tiempo = hrrI.getHoraClaseAula().getHoraClase().getHoclHoraFin()
					- hrrI.getHoraClaseAula().getHoraClase().getHoclHoraInicio();
			List<ContenidoCurricular> lstAux = new ArrayList<>();
			if (lstSgmt.isEmpty()) {
				sgmHoraClaseRestante = lstCnt.get(0).getUnidadCurricular().getSyllabo().getSylHorasClase() - tiempo;
				for (ContenidoCurricular cnt : lstCnt) {
					lstAux.add(cnt);
					if (lstAux.size() == 3) {
						break;
					}
				}
				lstCnt = lstAux;
			} else {
				sgmHoraClaseRestante = lstSgmt.get(lstSgmt.size() - 1).getSgmHoraClaseRestante() - tiempo;
				for (ContenidoCurricular cnt : lstCnt) {
					for (Seguimiento sgm : lstSgmt) {
						if (cnt.getCncrId().equals(sgm.getContenidoCurricular().getCncrId())
								&& sgm.getSgmEstado().equals("COMPLETADO")) {
						} else {
							lstAux.add(cnt);
							break;
						}
					}
					if (lstAux.size() == 3) {
						break;
					}
				}
				lstCnt = lstAux;
			}
			// root1 = createCheckboxDocuments();
			regAss = asistencia;
			regAss.setFichaDocente(regDcnt);
			formateador = new SimpleDateFormat("HH:mm");
			regAss.setAssHoraEntrada(formateador.format(ahora));
			regAss.setAssEstado(estado);
			sgmObservacion = "";
			flagIni = true;
			flagDlg = true;
			RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			return null;

		}
	}

	private String finalizarClase(Asistencia asistencia) {
		lstCnt = srvSgmt
				.buscarContenidos(hrrF.getMallaCurricularParalelo().getMallaCurricularMateria().getMateria().getMtrId());
		if (lstCnt.isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "No se encontraron temas de clases",
					"Verifique si existen el syllabus respectivo");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		} else {
			lstSgmt = srvSgmt.getSeguimiento(
					hrrF.getMallaCurricularParalelo().getMallaCurricularMateria().getMateria().getMtrId(),
					regDcnt.getFcdcId());
			horaClase = srvSgmt.obtenerHoraClasexHorario(hrrI);
			int tiempo = hrrI.getHoraClaseAula().getHoraClase().getHoclHoraFin()
					- hrrI.getHoraClaseAula().getHoraClase().getHoclHoraInicio();
			if (!lstSgmt.isEmpty()) {
				sgmHoraClaseRestante = lstSgmt.get(lstSgmt.size() - 1).getSgmHoraClaseRestante() - tiempo;
				sgmObservacion = lstSgmt.get(lstSgmt.size() - 1).getSgmObservacion();
			}
			// root1 = createCheckboxDocuments();
			regAss = asistencia;
			formateador = new SimpleDateFormat("HH:mm");
			regAss.setAssHoraSalida(formateador.format(ahora));
			regAss.setAssEstado("Finalizado");
			flagFin = true;
			flagDlg = true;
			RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			return null;
		}

	}

	public void guardarAsistencia() {
		flagDlg = false;
		try {
			if (selecCnts.isEmpty() && flagIni) {
				flagDlg = true;
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe escoger una Actividad", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			} else if (selecCnts.isEmpty() && flagFin) {
				srvSgmt.guardarRegistro(regAss);
				if (!sgmObservacion.equals(lstSgmt.get(lstSgmt.size() - 1).getSgmObservacion())) {
					lstSgmt.get(lstSgmt.size() - 1).setSgmObservacion(sgmObservacion);
					srvSgmt.guardarActualizarSeguimiento(lstSgmt.get(lstSgmt.size() - 1));
				}
				cleanClose();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Asistencia guardada", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			} else {
				for (String selectcnt : selecCnts) {
					for (ContenidoCurricular con : lstCnt) {
						if (selectcnt.equals(con.getCncrDescripcion())) {
							// con.setCncrEstado("COMPLETADO");
							srvSgmt.guardarActualizarContenido(con);
							Seguimiento seg = new Seguimiento();
							seg.setAsistencia(regAss);
							if (flagIni) {
								seg.setMallaCurricularParalelo(hrrI.getMallaCurricularParalelo());
							} else {
								seg.setMallaCurricularParalelo(hrrF.getMallaCurricularParalelo());
							}
							seg.setContenidoCurricular(con);
							seg.setSgmObservacion(sgmObservacion);
							seg.setSgmHoraClaseRestante(sgmHoraClaseRestante);
							seg.setSgmEstado("PENDIENTE");
							srvSgmt.guardarActualizarSeguimiento(seg);
						}
					}

				}
				srvSgmt.guardarRegistro(regAss);
				cleanClose();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Asistencia guardada", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			}

		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("flagDlg", flagDlg);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al guardar, intente nuevamente",
					e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public TreeNode createCheckboxDocuments() {
		TreeNode root = new DefaultTreeNode("Root", null);
		List<UnidadCurricular> lstUn = srvSgmt.listarUnidadCurricular(
				hrrI.getMallaCurricularParalelo().getMallaCurricularMateria().getMateria().getMtrId());
		for (UnidadCurricular unidadCurricular : lstUn) {
			TreeNode unidad = new DefaultTreeNode(unidadCurricular.getUncrDescripcion(), root);
			List<ContenidoCurricular> lstCn = srvSgmt.listarContenidos(unidadCurricular.getUncrId());
			for (ContenidoCurricular contenidoCurricular : lstCn) {
				TreeNode contenidos = new DefaultTreeNode(contenidoCurricular.getCncrDescripcion(), unidad);
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

	public void cleanClose() {
		selecCnts = null;
		lstCnt = null;
		lstSgmt = null;
		regAss = null;
		sgmObservacion = null;
		sgmHoraClaseRestante = 0;
	}

	public void generar() {
		if (srvDvc.inicializar()) {
			srvSgmt.generar(ahora, fclId);
			generado = false;
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Lector no encontrado", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

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

	/**
	 * @return the hrrI
	 */
	public HorarioAcademico getHrrI() {
		return hrrI;
	}

	/**
	 * @param hrrI
	 *            the hrrI to set
	 */
	public void setHrrI(HorarioAcademico hrrI) {
		this.hrrI = hrrI;
	}

	/**
	 * @return the hrrF
	 */
	public HorarioAcademico getHrrF() {
		return hrrF;
	}

	/**
	 * @param hrrF
	 *            the hrrF to set
	 */
	public void setHrrF(HorarioAcademico hrrF) {
		this.hrrF = hrrF;
	}

	public List<ContenidoCurricular> getLstCnt() {
		return lstCnt;
	}

	public void setLstCnt(List<ContenidoCurricular> lstCnt) {
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

	public String getSgmObservacion() {
		return sgmObservacion;
	}

	public void setSgmObservacion(String sgmObservacion) {
		this.sgmObservacion = sgmObservacion;
	}

	public Integer getSgmHoraClaseRestante() {
		return sgmHoraClaseRestante;
	}

	public void setSgmHoraClaseRestante(Integer sgmHoraClaseRestante) {
		this.sgmHoraClaseRestante = sgmHoraClaseRestante;
	}

	/**
	 * The fclId to get.
	 * 
	 * @return the fclId
	 */
	public Integer getFclId() {
		return fclId;
	}

	/**
	 * The fclId to set.
	 * 
	 * @param fclId
	 */
	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}

	/**
	 * @return the horaClase
	 */
	public String getHoraClase() {
		return horaClase;
	}

	/**
	 * @param horaClase
	 *            the horaClase to set
	 */
	public void setHoraClase(String horaClase) {
		this.horaClase = horaClase;
	}

}
