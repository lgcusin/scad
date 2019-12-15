package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.JSDCLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvLoginLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Asistencia;
import ec.edu.uce.biometrico.jpa.ContenidoCurricular;
import ec.edu.uce.biometrico.jpa.DetallePuesto;
import ec.edu.uce.biometrico.jpa.FichaDocente;
import ec.edu.uce.biometrico.jpa.HorarioAcademico;
import ec.edu.uce.biometrico.jpa.Seguimiento;
import ec.edu.uce.biometrico.jpa.UnidadCurricular;
import ec.edu.uce.biometrico.jpa.UsuarioRol;

@ManagedBean(name = "control")
@SessionScoped
public class Control {

	@EJB
	private JSDCLocal srvDvc;
	@EJB
	private SrvSeguimientoLocal srvSgmt;
	@EJB
	private SrvDocenteLocal srvDcn;
	@EJB
	private SrvLoginLocal srvlgn;

	private FichaDocente regDcnt;
	private HorarioAcademico hrrI;
	private HorarioAcademico hrrF;
	private Asistencia regAss;
	private List<Asistencia> lstAss;
	private List<ContenidoCurricular> lstCnt;
	private List<Seguimiento> lstSgmt;
	private List<String> selecCnts;

	private String hora;
	private Date ahora;
	private SimpleDateFormat formateador;
	private boolean generado;
	private boolean envioMail;
	private boolean flagIni;
	private boolean flagFin;
	private Integer fclId;
	private String horaClase;
	private String sgmObservacion;
	private Integer sgmHoraClaseRestante;
	private String sgmEstado;
	private boolean dispositivo;
	private boolean flagReporte;
	private boolean flagAutomatico;
	private UsuarioRol usuarioRol;
	private String nick;
	private String clave;
	private List<DetallePuesto> dt;
	private boolean flagDlg;
	private List<UnidadCurricular> lstUncr;
	private TreeNode rootUC;
	private TreeNode[] selectedNodes2;
	private int horaSyllaboRestante;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Login l = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		fclId = l.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaEmpleados().get(0).getDetallePuestos().get(0)
				.getDtpsCarrera().getCrrDependencia().getDpnId();
		iniciar();
	}

	public void iniciar() {
		regDcnt = new FichaDocente();
		dispositivo = srvDvc.inicializar();
		srvDvc.onLED();
		generado = false;
		envioMail = false;
		flagReporte = false;
		flagAutomatico = false;
	}

	@SuppressWarnings("deprecation")
	public void temporizador() {

		if (!flagReporte) {
			/**
			 * Validar fin de mes para envio reporte asistencia mensual docente
			 */
			validarEnvioReporteAsistencia();
		}
		ahora = new Date();
		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
		if (!generado && ahora.getHours() == 5) {
			generar();
		} else if (ahora.getHours() == 6) {
			generado = false;
		}
	}

	public String automatico() throws SQLException, IOException {
		if (!dispositivo) {
			srvDvc.cerrar();
			dispositivo = srvDvc.inicializar();

		}
		return null;
	}

	public String detectar() throws SQLException, IOException {
		if (dispositivo) {
			regDcnt = srvDvc.comparar();
			// regDcnt = new FichaDocente(1388);
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (regDcnt != null) {
				return registrar();
			} else {
				srvDvc.cerrar();
				dispositivo = srvDvc.inicializar();
			}
		} else {
			dispositivo = srvDvc.inicializar();
		}
		return null;
	}

	public String ingresar() throws SQLException, IOException {
		usuarioRol = srvlgn.verificar(nick, clave);
		if (usuarioRol.getUsroId() != null) {
			if (usuarioRol.getUsroRol().getRolId() == 5) {
				dt = srvlgn.listarDetallePuestoDocente(usuarioRol.getUsroUsuario().getUsrPersona().getPrsId());
				regDcnt = dt.get(0).getDtpsFichaDocente();
				regDcnt.setFcdcDetallePuestos(dt);
				nick = null;
				clave = null;
				if (srvDcn.verificarLogin(regDcnt)) {
					return registrar();
				} else {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No tiene acceso mediante login",
							"");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "usuario o contraseña no validos", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return null;
	}

	public String registrar() throws SQLException, IOException {
		flagIni = false;
		flagFin = false;
		// Compara si hay un horario con la hora de entrada o salida
		hrrI = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true, fclId, 1);
		hrrF = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), false, fclId, 2);

		if (hrrI != null && hrrF != null) {
			lstAss = srvSgmt.obtenerAsistenciasxDocente(ahora, regDcnt.getFcdcId());
			for (Asistencia asistencia : lstAss) {
				if (asistencia.getAssHorarioAcademico().getHracId() == hrrF.getHracId()
						&& asistencia.getAssEstado().equals("INICIADO")
						|| asistencia.getAssEstado().equals("ATRASADO")) {
					return finalizarClase(asistencia);
				} else if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
						&& asistencia.getAssEstado().equals("FALTA")) {
					return inicializarClase(asistencia, "INICIADO");
				} else if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
						&& asistencia.getAssEstado().equals("INICIADO")
						|| asistencia.getAssEstado().equals("ATRASADO")) {
					FacesUtil.mensajeError("Registro de entrada, ya existe");
					return null;
				} else if (asistencia.getAssHorarioAcademico().getHracId() == (hrrF.getHracId())
						&& asistencia.getAssEstado() == null) {

					if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
							&& asistencia.getAssEstado().equals("FALTA")) {
						return inicializarClase(asistencia, "INICIADO");
					}
				}
			}
		}

		if (hrrI != null && hrrF == null) {
			lstAss = srvSgmt.obtenerAsistenciasxDocente(ahora, regDcnt.getFcdcId());
			if (lstAss.isEmpty()) {
				FacesUtil.mensajeError("No existen horarios para iniciar una clase");
				return null;
			} else {
				for (Asistencia asistencia : lstAss) {
					if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
							&& asistencia.getAssEstado().equals("FALTA")) {
						return inicializarClase(asistencia, "INICIADO");
					} else if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
							&& asistencia.getAssEstado().equals("INICIADO")) {
						FacesUtil.mensajeError("Registro de entrada, ya existe");
						return null;
					}
				}
			}
		}

		if (hrrI == null && hrrF != null) {
			lstAss = srvSgmt.obtenerAsistenciasxDocente(ahora, regDcnt.getFcdcId());
			if (lstAss.isEmpty()) {
				FacesUtil.mensajeError("No existen horarios para finalizar una clase");
				return null;
			} else {
				for (Asistencia asistencia : lstAss) {
					if (asistencia.getAssHorarioAcademico().getHracId() == (hrrF.getHracId())
							&& asistencia.getAssEstado().equals("INICIADO")
							|| asistencia.getAssEstado().equals("ATRASADO")) {
						return finalizarClase(asistencia);
					} else if (asistencia.getAssHorarioAcademico().getHracId() == (hrrF.getHracId())
							&& asistencia.getAssEstado().equals("FINALIZADO")) {
						FacesUtil.mensajeError("Registro de salida, ya existe");
						return null;
					}
				}
				FacesUtil.mensajeError("Horario Academico no iniciado en el tiempo establecido");
				return null;
			}
		}

		if (hrrI == null && hrrF == null) {
			hrrI = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true, fclId, 0);
			if (hrrI == null) {
				FacesUtil.mensajeError("No existe un horario o está fuera del plazo establecido");
				return verHorarioFueraTiempo();
			} else {
				lstAss = srvSgmt.obtenerAsistenciasxDocente(ahora, regDcnt.getFcdcId());
				if (lstAss.isEmpty()) {
					return verHorarioFueraTiempo();
				} else {
					for (Asistencia asistencia : lstAss) {
						if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
								&& asistencia.getAssEstado().equals("FALTA")) {
							return inicializarClase(asistencia, "ATRASADO");
						} else if (asistencia.getAssHorarioAcademico().getHracId() == (hrrI.getHracId())
								&& asistencia.getAssEstado().equals("ATRASADO")
								|| asistencia.getAssEstado().equals("INICIADO")) {
							FacesUtil.mensajeError("Registro de entrada, ya existe");
							return null;
						}
					}
				}
			}
		}
		return null;

	}

	@SuppressWarnings("deprecation")
	private String verHorarioFueraTiempo() {
		List<Asistencia> lstAuxA = srvSgmt.obtenerAsistenciasxDocente(ahora, regDcnt.getFcdcId());
		lstAss = new ArrayList<>();
		if (!lstAuxA.isEmpty()) {
			for (Asistencia ass : lstAuxA) {
				if (ass.getAssHorarioAcademico().getHracHoraClaseAula().getHoclalHoraClase()
						.getHoclHoraInicio() <= ahora.getHours()) {
					// ass.setAssHoraEntrada(srvSgmt.obtenerHoraClasexHorario(ass.getAssHorarioAcademico()));
					lstAss.add(ass);
				}
			}
			return "dialogControlHorario";
		} else {
			FacesUtil.mensajeError("No se encontraron registro de asistencias, genere registros manualmente");
		}
		return null;
	}

	private String inicializarClase(Asistencia asistencia, String estado) {
		// Cruzar contenido-syllabus con seguimiento-syllabus
		lstUncr = srvSgmt.buscarAllUnidadesCurriculares(
				hrrI.getHracMallaCurricularParalelo().getMlcrprMallaCurricularMateria().getMlcrmtId());

		if (lstUncr.isEmpty()) {
			FacesUtil.mensajeError("No se encontraron temas de clases");
			return null;
		} else {
			lstCnt = srvSgmt.buscarAllContenidosCurriculares(
					hrrI.getHracMallaCurricularParalelo().getMlcrprMallaCurricularMateria().getMlcrmtId());
			lstSgmt = srvSgmt.obtenerSeguimientosxMateria(hrrI.getHracMallaCurricularParalelo().getMlcrprId(),
					regDcnt.getFcdcId());
			horaClase = srvSgmt.obtenerHoraClasexHorario(hrrI);
			if (lstSgmt.isEmpty()) {
				sgmHoraClaseRestante = lstCnt.get(0).getUnidadCurricular().getSyllabo().getSylHorasClase();
			} else {
				// sgmHoraClaseRestante =
				// srvSgmt.obtenerHoraSyllabusRestante(hrrI.getHracMallaCurricularParalelo()
				// .getMlcrprMallaCurricularMateria().getMlcrmtMateria().getMtrId(),
				// regDcnt.getFcdcId());
				sgmHoraClaseRestante = lstSgmt.get(lstSgmt.size() - 1).getSgmHoraClaseRestante();
			}
			int cntCompletados = 0;
			for (UnidadCurricular uncr : lstUncr) {
				uncr.setContenidos(new ArrayList<>());
				for (ContenidoCurricular cnt : lstCnt) {
					if (cnt.getUnidadCurricular().getUncrId() == uncr.getUncrId()) {
						for (Seguimiento sgm : lstSgmt) {
							if (sgm.getSgmContenidoCurricular().getCncrId() == cnt.getCncrId()
									&& hrrI.getHracMallaCurricularParalelo().getMlcrprId() == sgm
											.getSgmMallaCurricularParalelo().getMlcrprId()) {
								if (sgm.getSgmEstado().equals("COMPLETADO")) {
									cnt.setCncrEstado("COMPLETADO");
								} else {
									cnt.setCncrEstado("PENDIENTE");
								}
							}
						}
						uncr.getContenidos().add(cnt);
					}
				}
				for (Seguimiento sgm : lstSgmt) {
					if (sgm.getSgmContenidoCurricular().getUnidadCurricular().getUncrId() == uncr.getUncrId()
							&& sgm.getSgmEstado().equals("COMPLETADO")) {
						cntCompletados = cntCompletados + 1;
					}
				}
				uncr.setUncrPorcentaje(
						(double) ((((cntCompletados * uncr.getUncrTotalHoras()) / uncr.getContenidos().size()) * 100)
								/ uncr.getUncrTotalHoras()));
				cntCompletados = 0;
			}

			rootUC = crearCheckboxCotenidos(lstUncr);

			regAss = asistencia;
			regAss.setAssFichaDocente(regDcnt);
			formateador = new SimpleDateFormat("HH:mm");
			regAss.setAssHoraEntrada(formateador.format(ahora));
			regAss.setAssEstado(estado);
			sgmObservacion = "";
			flagIni = true;
			return "dialogControlAsistencia";

		}
	}

	private TreeNode crearCheckboxCotenidos(List<UnidadCurricular> lstUncr2) {
		TreeNode rootU = new CheckboxTreeNode();
		for (UnidadCurricular un : lstUncr2) {
			TreeNode rootUC = new CheckboxTreeNode("unidad", un, rootU);
			rootUC.setSelectable(false);
			for (ContenidoCurricular cn : un.getContenidos()) {
				CheckboxTreeNode rootCN = new CheckboxTreeNode("contenido", cn.getCncrDescripcion(), rootUC);
				if (cn.getCncrEstado() == null) {
					rootCN.setSelectable(true);
				} else if (cn.getCncrEstado().equals("COMPLETADO")) {
					rootCN.setSelectable(false);
				} else if (cn.getCncrEstado().equals("PENDIENTE")) {
					rootCN.setSelected(true);
				}
			}
		}

		return rootU;
	}

	private String finalizarClase(Asistencia asistencia) {
		lstUncr = srvSgmt.buscarAllUnidadesCurriculares(
				hrrF.getHracMallaCurricularParalelo().getMlcrprMallaCurricularMateria().getMlcrmtId());

		if (lstUncr.isEmpty()) {
			FacesUtil.mensajeError("No se encontraron temas de clases, verifique si existen el syllabus respectivo");
			return null;
		} else {
			lstCnt = srvSgmt.buscarAllContenidosCurriculares(
					hrrF.getHracMallaCurricularParalelo().getMlcrprMallaCurricularMateria().getMlcrmtId());
			lstSgmt = srvSgmt.obtenerSeguimientosxMateria(hrrF.getHracMallaCurricularParalelo().getMlcrprId(),
					regDcnt.getFcdcId());
			int cntCompletados = 0;
			for (UnidadCurricular uncr : lstUncr) {
				uncr.setContenidos(new ArrayList<>());
				for (ContenidoCurricular cnt : lstCnt) {
					if (cnt.getUnidadCurricular().getUncrId() == uncr.getUncrId()) {
						for (Seguimiento sgm : lstSgmt) {
							if (sgm.getSgmContenidoCurricular().getCncrId() == cnt.getCncrId()
									&& hrrF.getHracMallaCurricularParalelo().getMlcrprId() == sgm
											.getSgmMallaCurricularParalelo().getMlcrprId()) {
								if (sgm.getSgmEstado().equals("COMPLETADO")) {
									cnt.setCncrEstado("COMPLETADO");
								} else {
									cnt.setCncrEstado("PENDIENTE");
								}
							}
						}
						uncr.getContenidos().add(cnt);
					}
				}
				for (Seguimiento sgm : lstSgmt) {
					if (sgm.getSgmContenidoCurricular().getUnidadCurricular().getUncrId() == uncr.getUncrId()
							&& sgm.getSgmEstado().equals("COMPLETADO")) {
						cntCompletados = cntCompletados + 1;
					}
					// selecCnts.add(sgm.getSgmContenidoCurricular().getCncrDescripcion());
				}
				uncr.setUncrPorcentaje(
						(double) ((((cntCompletados * uncr.getUncrTotalHoras()) / uncr.getContenidos().size()) * 100)
								/ uncr.getUncrTotalHoras()));
				cntCompletados = 0;
			}
			rootUC = crearCheckboxCotenidos(lstUncr);
			horaClase = srvSgmt.obtenerHoraClasexHorario(hrrF);
			sgmHoraClaseRestante = lstSgmt.get(lstSgmt.size() - 1).getSgmHoraClaseRestante();
			// sgmObservacion = lstSgmt.get(lstSgmt.size() -
			// 1).getSgmObservacion();
			sgmObservacion = "";
			regAss = asistencia;
			formateador = new SimpleDateFormat("HH:mm");
			regAss.setAssHoraSalida(formateador.format(ahora));
			regAss.setAssEstado("FINALIZADO");
			flagFin = true;
			return "dialogControlAsistencia";

		}

	}

	public String guardarAsistencia() {
		selecCnts = procesarSeleccion(selectedNodes2);
		try {
			if (selecCnts.isEmpty() && flagIni) {
				FacesUtil.mensajeError("Debe escoger un tema de clase");
				return null;
			} else if (selecCnts.isEmpty() && flagFin) {
				if (!sgmObservacion.equals(lstSgmt.get(lstSgmt.size() - 1).getSgmObservacion())) {
					lstSgmt.get(lstSgmt.size() - 1).setSgmObservacion(sgmObservacion);
					srvSgmt.guardarActualizarSeguimiento(lstSgmt.get(lstSgmt.size() - 1));
				}
				actualizarRegistros(regAss);
				cleanClose();
				FacesUtil.mensajeInfo("Asistencia guardada");
				return "control";
			} else {
				for (UnidadCurricular un : lstUncr) {
					for (String selectcnt : selecCnts) {
						for (ContenidoCurricular con : un.getContenidos()) {
							if (selectcnt.equals(con.getCncrDescripcion())) {
								Seguimiento seg = new Seguimiento();
								seg.setAsistencia(regAss);
								if (flagIni) {
									seg.setSgmMallaCurricularParalelo(hrrI.getHracMallaCurricularParalelo());
									sgmEstado = "PENDIENTE";
									seg.setSgmHoraClaseRestante(sgmHoraClaseRestante);
								} else {
									seg.setSgmMallaCurricularParalelo(hrrF.getHracMallaCurricularParalelo());
									if (sgmEstado.equals("COMPLETADO")) {
										horaSyllaboRestante = sgmHoraClaseRestante
												- ((selecCnts.size() * con.getUnidadCurricular().getUncrTotalHoras())
														/ un.getContenidos().size());
										seg.setSgmHoraClaseRestante(horaSyllaboRestante);
									} else {
										seg.setSgmHoraClaseRestante(sgmHoraClaseRestante);
									}
								}

								seg.setSgmContenidoCurricular(con);
								seg.setSgmObservacion(sgmObservacion);
								seg.setSgmEstado(sgmEstado);
								srvSgmt.guardarActualizarSeguimiento(seg);
							}
						}
					}
				}
				actualizarRegistros(regAss);
				cleanClose();
				FacesUtil.mensajeInfo("Asistencia guardada");
				return "control";
			}

		} catch (Exception e) {
			FacesUtil.mensajeError("Error al guardar, intente nuevamente");
			return "control";
		}

	}

	private List<String> procesarSeleccion(TreeNode[] nodes) {
		List<String> retorno = new ArrayList<>();
		if (nodes != null && nodes.length > 0) {
			for (TreeNode node : nodes) {
				retorno.add(node.getData().toString());
			}
		}
		return retorno;
	}

	public void actualizarRegistros(Asistencia regAss) {
		for (Asistencia as : lstAss) {
			if (regAss.getAssHorarioAcademico().getMlcrprIdComp() == null
					&& as.getAssHorarioAcademico().getMlcrprIdComp() != null) {
				if (regAss.getAssHorarioAcademico().getHracMallaCurricularParalelo().getMlcrprId() == as
						.getAssHorarioAcademico().getMlcrprIdComp()) {
					if (flagIni) {
						as.setAssHoraEntrada(regAss.getAssHoraEntrada());
					} else {
						as.setAssHoraSalida(regAss.getAssHoraSalida());
					}
					srvSgmt.guardarRegistro(as);
				}
			} else if (regAss.getAssHorarioAcademico().getMlcrprIdComp() != null
					&& regAss.getAssHorarioAcademico().getMlcrprIdComp() != null) {
				if (regAss.getAssHorarioAcademico().getMlcrprIdComp() == as.getAssHorarioAcademico()
						.getHracMallaCurricularParalelo().getMlcrprId()) {
					if (flagIni) {
						as.setAssHoraEntrada(regAss.getAssHoraEntrada());
					} else {
						as.setAssHoraSalida(regAss.getAssHoraSalida());
					}
					srvSgmt.guardarRegistro(as);
				}

			}

			else if ((regAss.getAssHorarioAcademico().getHracMallaCurricularParalelo().getMlcrprId() == as
					.getAssHorarioAcademico().getHracMallaCurricularParalelo().getMlcrprId())) {
				as.setAssEstado(regAss.getAssEstado());
				if (flagIni) {
					as.setAssHoraEntrada(regAss.getAssHoraEntrada());
				} else {
					as.setAssHoraSalida(regAss.getAssHoraSalida());
				}
				srvSgmt.guardarRegistro(as);
			}

		}
	}

	public String cancelarAsistencia() {
		cleanClose();
		return "control";
	}

	public void cleanClose() {
		selecCnts = null;
		lstCnt = null;
		lstSgmt = null;
		regAss = null;
		regDcnt = null;
		sgmObservacion = null;
		sgmHoraClaseRestante = 0;
	}

	public String regresarFueradeHorario() {
		cleanClose();
		return "control";
	}

	public void generar() {
		generado = srvSgmt.generarAsistecniasxFacultad(ahora, fclId);
	}

	/*
	 * Metodo que permite mostrar el sistema biometrico sin otra aplicacion en
	 * el sistema.
	 * 
	 * @param proceso
	 */
	public static void matarProceso(String proceso) {
		// Ha sido probado en win y linux
		String osName = System.getProperty("os.name");
		String cmd = "";
		if (osName.toUpperCase().contains("WIN")) {// S.O. Windows
			cmd += "tskill " + proceso;
		} else {
			cmd += "killall " + proceso;
		}
		Process hijo;
		try {
			hijo = Runtime.getRuntime().exec(cmd);
			hijo.waitFor();
			if (hijo.exitValue() == 0) {
				System.out.println("proceso matado con exito");
			} else {
				System.out.println("Incapaz de matar proceso. Exit code: " + hijo.exitValue() + "n");
			}
		} catch (IOException e) {
			System.out.println("Incapaz de matar proceso.");
		} catch (InterruptedException e) {
			System.out.println("Incapaz de matar proceso.");
		}
	}

	public void regresar() {
		flagAutomatico = false;
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

	/**
	 * 
	 * @return the lstAss
	 */
	public List<Asistencia> getLstAss() {
		return lstAss;
	}

	/**
	 * @param lstAss
	 *            the lstAss to set
	 */
	public void setLstAss(List<Asistencia> lstAss) {
		this.lstAss = lstAss;
	}

	/**
	 * @return the sgmEstado
	 */
	public String getSgmEstado() {
		return sgmEstado;
	}

	/**
	 * @param sgmEstado
	 *            the sgmEstado to set
	 */
	public void setSgmEstado(String sgmEstado) {
		this.sgmEstado = sgmEstado;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick
	 *            the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * @param clave
	 *            the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * Metodo que permite enviar el reporte de asistencia mensual al docente.
	 */
	private void validarEnvioReporteAsistencia() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int anio = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int ultimoDiaMes = obtenerUltimoDiaMes(anio, mes);
		if (dia == ultimoDiaMes) {
			List<Asistencia> asistenciaList = new ArrayList<>();
			// asistenciaList = srvDcn.getAsistenciasReporte(fclId, inicio,
			// fin);
			if (!asistenciaList.isEmpty()) {
				// Envio de mail
				flagReporte = true;
			}
		}
	}

	/**
	 * Metodo que obtiene la fecha fin del mes
	 *
	 * @param anio
	 * @param mes
	 * @return
	 */
	public int obtenerUltimoDiaMes(int anio, int mes) {
		Calendar calendario = Calendar.getInstance();
		calendario.set(anio, mes - 1, 1);
		return calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * The envioMail to get.
	 * 
	 * @return the envioMail
	 */
	public boolean isEnvioMail() {
		return envioMail;
	}

	/**
	 * The envioMail to set.
	 * 
	 * @param envioMail
	 */
	public void setEnvioMail(boolean envioMail) {
		this.envioMail = envioMail;
	}

	public boolean isFlagAutomatico() {
		return flagAutomatico;
	}

	public void setFlagAutomatico(boolean flagAutomatico) {
		this.flagAutomatico = flagAutomatico;
	}

	public List<UnidadCurricular> getLstUncr() {
		return lstUncr;
	}

	public void setLstUncr(List<UnidadCurricular> lstUncr) {
		this.lstUncr = lstUncr;
	}

	public TreeNode getRootUC() {
		return rootUC;
	}

	public void setRootUC(TreeNode rootUC) {
		this.rootUC = rootUC;
	}

	public TreeNode[] getSelectedNodes2() {
		return selectedNodes2;
	}

	public void setSelectedNodes2(TreeNode[] selectedNodes2) {
		this.selectedNodes2 = selectedNodes2;
	}
}
