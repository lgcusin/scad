package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.text.ParseException;
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
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.RowEditEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvHorarioLocal;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.HorarioAcademicoExamen;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Nivel;
import ec.uce.edu.biometrico.jpa.Paralelo;

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
	private Boolean flagEditar;
	private String fecha;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		dataLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		lstC = srvEmp.listarCarreras(
				dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId());
		iniciar();
	}

	public void iniciar() {
		selectCrr = new Carrera();
		selectSem = new Nivel();
		selectMtr = new Materia();
		lstAsistencias = new ArrayList<>();
		flagEditar = false;

	}

	public void setCarreraID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			lstS = srvHor.listarSemestrexCarrera(
					dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId(), crrId);
		} else {
			selectSem.setNvlId(null);
			selectMtr.setMtrId(null);
			System.out.println("No ha seleccionada una carrera: ");
		}
	}

	public void setNivelID(ValueChangeEvent event) {
		if (event.getNewValue() != null && crrId != null) {
			smsId = (Integer) event.getNewValue();
			lstM = srvHor.listarMatBySem(
					dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId(), smsId,
					crrId);
			if (lstM != null) {
				System.out.println("Si hay de materias por semestre seleccionado: " + lstM.size());
			} else {
				System.out.println("No hay de materias por semestre seleccionado");
			}
		}
	}

	public void setMateriaID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de buscar horarios por id materia: " + event.getNewValue());
			mtrId = (Integer) event.getNewValue();
		}
	}

	public void buscarParalelos() {
		System.out.println("Metodo que busca paralelos existentes por materia y semestre");
		if (crrId != null && mtrId != null && smsId != null) {
			lstParalelos = srvHor.listarParalelosHorario(
					dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId(), smsId,
					mtrId, crrId);
			if (lstParalelos != null) {
				System.out.println("Paralelos encontrados: " + lstParalelos.size());
			} else {
				System.out.println("Paralelos NO encontrados");
			}
		} else {
			System.out.println("Seleccione la carrera y materia a listar paralelos");
		}
	}

	public void limpiarFiltros() {
		selectSem.setNvlId(null);
		selectMtr.setMtrId(null);
		lstParalelos = null;
		lstHorarios = null;
		lstAsistencias = null;
		flagEditar = false;
	}

	public void editarDataHorario() throws ParseException {
		System.out.println("Metodo para ver informacion de horario");
		lstHorarios = srvHor.listarHorarios(
				dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId(),
				selectPar.getPrlId(), selectMtr.getMtrId());
		List<HorarioAcademico> lstAux = new ArrayList<>();
		lstAux.addAll(lstHorarios);
		for (HorarioAcademico h : lstHorarios) {
			List<HorarioAcademico> auxHorario = new ArrayList<>();
			for (HorarioAcademico h2 : lstHorarios) {
				if (h.getHracDia().equals(h2.getHracDia()) && h.getMallaCurricularParalelo().getMlcrprId()
						.equals(h2.getMallaCurricularParalelo().getMlcrprId())) {
					auxHorario.add(h2);
				}
			}
			for (int i = 1; i < auxHorario.size(); i++) {
				if (lstAux.contains(auxHorario.get(i))) {
					lstAux.remove(auxHorario.get(i));
				}
			}
		}

		lstAsistenciasByHorario = srvHor.listarAsistenciasByHorario(
				dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0).getFcdcId(),
				getIdHorarios(lstHorarios), getFormatoFechaProceso());

		lstAsistencias = new ArrayList<>();
		for (HorarioAcademico h : lstAux) {
			Asistencia asis = new Asistencia();
			// asis.setDiaSemana(getDiaSemana(h));
			fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			asis.setAssFecha(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			asis.setAssEstado("EXAMEN");
			asis.setHorarioAcademico(h);
			asis.setFichaDocente(dataLogin.getUsuarioRol().getUsuario().getPersona().getFichaDocentes().get(0));
			lstAsistencias.add(asis);
		}

		flagEditar = true;
	}

	/**
	 * Metodo que permite obtener el formato de la fecha proceso.
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Date getFormatoFechaProceso() throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
		String fechaProceso = formato.format(new Date());
		Date fechaProcesoDate = formato.parse(fechaProceso);
		return fechaProcesoDate;
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

	/**
	 * Metodo que permite obtener el dia de la semana segun el horario.
	 * 
	 * @param h
	 * @return
	 */
	private String getDiaSemana(HorarioAcademico h) {
		String result = "";
		switch (h.getHracDia()) {
		case 0:
			result = "LUNES";
			break;
		case 1:
			result = "MARTES";
			break;
		case 2:
			result = "MIERCOLES";
			break;
		case 3:
			result = "JUEVES";
			break;
		case 4:
			result = "VIERNES";
			break;
		case 5:
			result = "SABADO";
			break;
		case 6:
			result = "DOMINGO";
			break;
		default:
			break;
		}
		return result;
	}

	public void onAddNew() {
		HorarioAcademico h = new HorarioAcademico();
		if (lstHorarios != null && lstHorarios.size() == 0) {
		} else {

		}
		lstHorarios.add(h);
		FacesMessage msg = new FacesMessage("Nuevo registro añadido");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowEdit(RowEditEvent event) {
		actualizar((Asistencia) event.getObject());
	}

	public void actualizar(Asistencia asis) {
		System.out.println("Guardar cambios de horario");
		try {
			if (asis.getAssId() != null) {
				srvHor.actualizarGuardarAsistencia(asis);
			} else if (validarHora(asis.getAssHoraEntrada(), asis.getAssHoraSalida())) {
				HorarioAcademicoExamen horEx = new HorarioAcademicoExamen();
				horEx.setHracexHoraInicio(Integer.parseInt(asis.getAssHoraEntrada()));
				horEx.setHracexHoraFin(Integer.parseInt(asis.getAssHoraSalida()));
				horEx.setHracexEstado(0);
				srvHor.guardarHorarioExamen(horEx);
				asis.setHorarioAcademicoExamen(horEx);
				asis.setAssHoraEntrada("");
				asis.setAssHoraSalida("");
				Date date = new SimpleDateFormat(FORMATOFECHA).parse(fecha);
				asis.setAssFecha(date);
				srvHor.actualizarGuardarAsistencia(asis);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar horario" + e);
		}
	}

	private boolean validarHora(String assHoraEntrada, String assHoraSalida) {
		if (assHoraEntrada.isEmpty() || assHoraSalida.isEmpty()) {
			return false;
		} else {
			try {
				int aHE = Integer.parseInt(assHoraEntrada);
				int aHS = Integer.parseInt(assHoraSalida);
				if (aHE >= aHS) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Horario entrada no debe ser mayor o igual a horario salida", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return false;
				} else if (aHE < 6 || aHE > 22 || aHS < 6 || aHS > 22) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Los horarios deben estar dentro del horario academico", null);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return false;
				} else {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}

	}

	public void onRowCancel(RowEditEvent event) {
	}

	public void eliminarHorario(Asistencia asis) {
		System.out.println("Metodo que elimina el horario");
		List<Asistencia> lstAux = new ArrayList<>();
		for (Asistencia as : lstAsistencias) {
			if (asis.getAssId() != as.getAssId()) {
				lstAux.add(asis);
			} else {
				srvHor.eliminarHorario(asis);
			}
		}
		System.out.println("Nueva lista de horarios" + lstAux.size());
		lstAsistencias = lstAux;
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

}
