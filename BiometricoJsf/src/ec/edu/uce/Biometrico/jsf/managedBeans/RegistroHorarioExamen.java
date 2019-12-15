package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.BeanClassLoaderAware;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvHorarioLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Asistencia;
import ec.edu.uce.biometrico.jpa.Carrera;
import ec.edu.uce.biometrico.jpa.HorarioAcademico;
import ec.edu.uce.biometrico.jpa.HorarioAcademicoExamen;
import ec.edu.uce.biometrico.jpa.Materia;
import ec.edu.uce.biometrico.jpa.Nivel;
import ec.edu.uce.biometrico.jpa.Paralelo;
import ec.edu.uce.biometrico.jpa.PlanificacionCronograma;

@ManagedBean(name = "registroExamen")
@ViewScoped
public class RegistroHorarioExamen {

	@EJB
	private SrvHorarioLocal srvHor;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	private Login dataLogin;

	private static final String FORMATOFECHA = "yyyy-MM-dd";
	private Materia selectMtr;
	private Carrera selectCrr;
	private Nivel selectSem;
	private Paralelo selectPar;
	private Asistencia selectAss;
	private Integer crrId;
	private Integer mtrId;
	private Integer smsId;
	private List<Carrera> lstC;
	private List<Materia> lstM;
	private List<Asistencia> lstAsistencias;
	private List<Asistencia> lstAsistenciasByHorario;
	private List<Nivel> lstS;
	private List<Paralelo> lstParalelos;
	private List<HorarioAcademico> lstHorarios;
	private List<PlanificacionCronograma> lstPlanificacionCronograma;
	private Boolean flagEditar;
	private boolean flagModificar;
	private String fecha;
	private List<Date> semanaExamen;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		dataLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		iniciar();
		flagEditar = verificarCronograma();
		lstC = srvEmp.listarCarreras(
				dataLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getFichaDocentes().get(0).getFcdcId());
	}

	public void iniciar() {
		selectCrr = new Carrera();
		selectSem = new Nivel();
		selectMtr = new Materia();
		selectCrr.setCrrId(GeneralesConstantes.APP_ID_BASE);
		selectSem.setNvlId(GeneralesConstantes.APP_ID_BASE);
		selectMtr.setMtrId(GeneralesConstantes.APP_ID_BASE);
		lstC = null;
		lstS = null;
		lstM = null;
		lstParalelos = null;
		lstAsistencias = null;
		flagEditar = false;
	}

	public void carreraID(ValueChangeEvent event) {
		selectSem.setNvlId(GeneralesConstantes.APP_ID_BASE);
		selectMtr.setMtrId(GeneralesConstantes.APP_ID_BASE);
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstS = srvHor.listarSemestrexCarrera(dataLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), crrId);
			if (lstS.isEmpty()) {
				FacesUtil.mensajeError("No existen nivel para la carrera seleccionada");
			}
		}
	}

	public void nivelID(ValueChangeEvent event) {
		selectMtr.setMtrId(GeneralesConstantes.APP_ID_BASE);
		if (event.getNewValue() != null && crrId != null) {
			smsId = (Integer) event.getNewValue();
			lstM = srvHor.listarMatBySem(dataLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), smsId, crrId);
			if (lstM.isEmpty()) {
				FacesUtil.mensajeError("No existe materias en el semestre seleccionado");
			}
		}
	}

	public void materiaID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de buscar horarios por id materia: " + event.getNewValue());
			mtrId = (Integer) event.getNewValue();
		}
	}

	public void buscarParalelos() {
		System.out.println("Metodo que busca paralelos existentes por materia y semestre");
		if (crrId != null && mtrId != null && smsId != null) {
			lstAsistencias = null;
			lstParalelos = srvHor.listarParalelosHorario(dataLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(),
					smsId, mtrId, crrId);
			if (lstParalelos != null) {
				System.out.println("Paralelos encontrados: " + lstParalelos.size());
			} else {
				System.out.println("Paralelos NO encontrados");
			}
		} else {
			FacesUtil.mensajeError("Seleccione los parametros de busqueda");
		}
	}

	public void limpiarFiltros() {
		lstC = srvEmp.listarCarreras(dataLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId());
		selectCrr.setCrrId(GeneralesConstantes.APP_ID_BASE);
		selectSem.setNvlId(GeneralesConstantes.APP_ID_BASE);
		selectMtr.setMtrId(GeneralesConstantes.APP_ID_BASE);
		crrId = null;
		smsId = null;
		mtrId = null;
		lstParalelos = null;
		lstHorarios = null;
		lstAsistencias = null;
		flagModificar = false;
		flagEditar = verificarCronograma();
	}

	public Boolean verificarCronograma() {
		System.out.println("Metodo para ver informacion de horario");
		Date fechaA = new Date();
		if (dataLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId() == 10) {
			lstPlanificacionCronograma = srvHor.listarPlannificacion(" MEDICINA");
		} else {
			lstPlanificacionCronograma = srvHor.listarPlannificacion("");
		}
		if (fechaA.getTime() < lstPlanificacionCronograma.get(0).getPlcrFechaInicio().getTime()) {
			semanaExamen = obtenerSemanaExamen(lstPlanificacionCronograma.get(0));
			return true;
		} else if (lstPlanificacionCronograma.get(0).getPlcrFechaFin().getTime() < fechaA.getTime()
				&& lstPlanificacionCronograma.get(1).getPlcrFechaInicio().getTime() > fechaA.getTime()) {
			semanaExamen = obtenerSemanaExamen(lstPlanificacionCronograma.get(1));
			return true;
		} else {
			return false;
		}
	}

	private List<Date> obtenerSemanaExamen(PlanificacionCronograma plcr) {
		List<Date> retorno = new ArrayList<>();
		Date semanaInicio = plcr.getPlcrFechaInicio();
		while (semanaInicio.getTime() <= plcr.getPlcrFechaFin().getTime()) {
			try {
				retorno.add(getFormatoFechaProceso(semanaInicio));
			} catch (ParseException e) {
				System.out.println(e);
			}
			semanaInicio.setTime(semanaInicio.getTime() + 86400000);
		}
		return retorno;
	}

	/**
	 * Metodo que permite obtener el formato de la fecha proceso.
	 * 
	 * @param diaSemana
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Date getFormatoFechaProceso(Date diaSemana) throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
		String fechaProceso = formato.format(diaSemana);
		System.out.println(fechaProceso);
		Date fechaProcesoDate = formato.parse(fechaProceso);
		return fechaProcesoDate;
	}

	@SuppressWarnings("deprecation")
	public void editarDataHorario() throws ParseException {
		lstAsistencias = new ArrayList<>();
		lstAsistenciasByHorario = new ArrayList<>();
		lstHorarios = new ArrayList<>();
		lstHorarios = srvHor.listarHorarios(dataLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(),
				selectPar.getPrlId(), selectMtr.getMtrId());
		if (!lstHorarios.isEmpty()) {
			for (Date fecha : semanaExamen) {
				Asistencia asisAux = srvHor.listarAsistenciasByHorario(
						dataLogin.getDt().get(0).getDtpsFichaDocente().getFcdcId(), getIdHorarios(lstHorarios), fecha);
				if (asisAux != null) {
					lstAsistenciasByHorario.add(asisAux);
				}
			}
			if (lstAsistenciasByHorario.isEmpty()) {
				lstAsistencias = new ArrayList<>();
				List<HorarioAcademico> lstAux = new ArrayList<>();
				lstAux.addAll(lstHorarios);
				for (HorarioAcademico h : lstHorarios) {
					List<HorarioAcademico> auxHorario = new ArrayList<>();
					for (HorarioAcademico h2 : lstHorarios) {
						if (h.getHracDia().equals(h2.getHracDia()) && h.getHracMallaCurricularParalelo()
								.getMlcrprId() == h2.getHracMallaCurricularParalelo().getMlcrprId()) {
							auxHorario.add(h2);
						}
					}
					for (int i = 1; i < auxHorario.size(); i++) {
						if (lstAux.contains(auxHorario.get(i))) {
							lstAux.remove(auxHorario.get(i));
						}
					}
				}
				for (Date fecha : semanaExamen) {
					for (HorarioAcademico h : lstAux) {
						if (fecha.getDay() - 1 == h.getHracDia()) {
							Asistencia asis = new Asistencia();
							asis.setAssFecha(fecha);
							asis.setAssEstado("SIN ASIGNAR");
							asis.setAssHorarioAcademico(h);
							asis.setAssHoraEntrada(srvHor.obtenerHoraClasexHorario(h, 1));
							asis.setAssHoraSalida(srvHor.obtenerHoraClasexHorario(h, 2));
							asis.setAssFichaDocente(dataLogin.getDt().get(0).getDtpsFichaDocente());
							lstAsistencias.add(asis);
						}
					}
				}
				flagModificar = false;
			} else {
				lstAsistencias = lstAsistenciasByHorario;
				flagModificar = true;
				FacesMessage msg = new FacesMessage("Horarios de Examenes encontrados");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			FacesUtil.mensajeError("No existen horarios");
		}
	}

	/**
	 * Metodo que permite obtener los id de horarios para examenes.
	 * 
	 * @param lstHorarios
	 * @return
	 */
	private List<Integer> getIdHorarios(List<HorarioAcademico> lstHorarios) {
		List<Integer> idHorarioList = new ArrayList<>();
		if (!lstHorarios.isEmpty()) {
			for (HorarioAcademico horarioAcademico : lstHorarios) {
				idHorarioList.add(horarioAcademico.getHracId());
			}
		}
		return idHorarioList;
	}

	public void guardarAsistenciaHorarioExamen() {
		if (selectAss != null) {
			try {
				for (Asistencia as : lstAsistencias) {
					for (HorarioAcademico hr : lstHorarios) {
						if (selectAss.equals(as) && selectAss.getAssHorarioAcademico().equals(hr)) {
							HorarioAcademicoExamen horEx = new HorarioAcademicoExamen();
							horEx.setHracexHoraInicio(Integer.parseInt(selectAss.getAssHoraEntrada().substring(0, 2)));
							horEx.setHracexHoraFin(Integer.parseInt(selectAss.getAssHoraSalida().substring(0, 2)));
							horEx.setHracexEstado(0);
							srvHor.guardarHorarioExamen(horEx);
							as.setAssHorarioAcademico(hr);
							as.setAssHorarioAcademicoExamen(horEx);
							as.setAssHoraEntrada(null);
							as.setAssHoraSalida(null);
							as.setAssEstado("EXAMEN");
							srvHor.actualizarGuardarAsistencia(as);
							// } else if
							// (hr.getHracDia().equals(as.getHorarioAcademico().getHracDia()))
							// {
						} else if (hr.equals(as.getAssHorarioAcademico())) {
							as.setAssEstado("JUSTIFICADO");
							srvHor.actualizarGuardarAsistencia(as);
						}
					}
				}
				editarDataHorario();
				flagModificar = true;
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			FacesUtil.mensajeError("Seleccione un registro antes de guardar");
		}
	}

	public void modificarAsistenciaHorarioExamen() {
		try {
			for (Asistencia asis : lstAsistenciasByHorario) {
				if (asis.getAssEstado().equals("EXAMEN")) {
					HorarioAcademicoExamen hracex = asis.getAssHorarioAcademicoExamen();
					srvHor.removeAsistencia(asis);
					srvHor.removerHorarioExamen(hracex);
				} else {
					srvHor.removeAsistencia(asis);
				}
			}
			editarDataHorario();
		} catch (ParseException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}

	public String regresar() {
		selectMtr = null;
		selectCrr = null;
		selectSem = null;
		selectPar = null;
		selectAss = null;
		crrId = null;
		mtrId = null;
		smsId = null;
		lstC = null;
		lstM = null;
		lstAsistencias = null;
		lstAsistenciasByHorario = null;
		lstS = null;
		lstParalelos = null;
		lstHorarios = null;
		flagEditar = false;
		fecha = null;
		return "principal";
	}

	// Setters and getters

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

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

	/**
	 * @return the selectCrr
	 */
	public Carrera getSelectCrr() {
		return selectCrr;
	}

	/**
	 * @param selectCrr
	 *            the selectCrr to set
	 */
	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	/**
	 * The srvHor to get.
	 * 
	 * @return the srvHor
	 */
	public SrvHorarioLocal getSrvHor() {
		return srvHor;
	}

	/**
	 * The srvHor to set.
	 * 
	 * @param srvHor
	 */
	public void setSrvHor(SrvHorarioLocal srvHor) {
		this.srvHor = srvHor;
	}

	/**
	 * The selectSem to get.
	 * 
	 * @return the selectSem
	 */
	public Nivel getSelectSem() {
		return selectSem;
	}

	/**
	 * The selectSem to set.
	 * 
	 * @param selectSem
	 */
	public void setSelectSem(Nivel selectSem) {
		this.selectSem = selectSem;
	}

	/**
	 * @return the selectAss
	 */
	public Asistencia getSelectAss() {
		return selectAss;
	}

	/**
	 * @param selectAss
	 *            the selectAss to set
	 */
	public void setSelectAss(Asistencia selectAss) {
		this.selectAss = selectAss;
	}

	/**
	 * The lstS to get.
	 * 
	 * @return the lstS
	 */
	public List<Nivel> getLstS() {
		return lstS;
	}

	/**
	 * The lstS to set.
	 * 
	 * @param lstS
	 */
	public void setLstS(List<Nivel> lstS) {
		this.lstS = lstS;
	}

	/**
	 * @return the lstAsistencias
	 */
	public List<Asistencia> getLstAsistencias() {
		return lstAsistencias;
	}

	/**
	 * @param lstAsistencias
	 *            the lstAsistencias to set
	 */
	public void setLstAsistencias(List<Asistencia> lstAsistencias) {
		this.lstAsistencias = lstAsistencias;
	}

	/**
	 * The mtrId to get.
	 * 
	 * @return the mtrId
	 */
	public Integer getMtrId() {
		return mtrId;
	}

	/**
	 * The mtrId to set.
	 * 
	 * @param mtrId
	 */
	public void setMtrId(Integer mtrId) {
		this.mtrId = mtrId;
	}

	/**
	 * The smsId to get.
	 * 
	 * @return the smsId
	 */
	public Integer getSmsId() {
		return smsId;
	}

	/**
	 * The smsId to set.
	 * 
	 * @param smsId
	 */
	public void setSmsId(Integer smsId) {
		this.smsId = smsId;
	}

	/**
	 * The lstParalelos to get.
	 * 
	 * @return the lstParalelos
	 */
	public List<Paralelo> getLstParalelos() {
		return lstParalelos;
	}

	/**
	 * The lstParalelos to set.
	 * 
	 * @param lstParalelos
	 */
	public void setLstParalelos(List<Paralelo> lstParalelos) {
		this.lstParalelos = lstParalelos;
	}

	/**
	 * The selectPar to get.
	 * 
	 * @return the selectPar
	 */
	public Paralelo getSelectPar() {
		return selectPar;
	}

	/**
	 * The selectPar to set.
	 * 
	 * @param selectPar
	 */
	public void setSelectPar(Paralelo selectPar) {
		this.selectPar = selectPar;
	}

	/**
	 * The flagEditar to get.
	 * 
	 * @return the flagEditar
	 */
	public Boolean getFlagEditar() {
		return flagEditar;
	}

	/**
	 * The flagEditar to set.
	 * 
	 * @param flagEditar
	 */
	public void setFlagEditar(Boolean flagEditar) {
		this.flagEditar = flagEditar;
	}

	/**
	 * @return the lstHorarios
	 */
	public List<HorarioAcademico> getLstHorarios() {
		return lstHorarios;
	}

	/**
	 * @param lstHorarios
	 *            the lstHorarios to set
	 */
	public void setLstHorarios(List<HorarioAcademico> lstHorarios) {
		this.lstHorarios = lstHorarios;
	}

	public boolean isFlagModificar() {
		return flagModificar;
	}

	public void setFlagModificar(boolean flagModificar) {
		this.flagModificar = flagModificar;
	}

}
