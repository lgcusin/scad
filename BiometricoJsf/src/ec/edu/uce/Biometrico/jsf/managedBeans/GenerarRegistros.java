package ec.edu.uce.Biometrico.jsf.managedBeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;

@ManagedBean(name = "generarRegistros")
@ViewScoped
public class GenerarRegistros {

	@EJB
	private SrvEmpleadoLocal srvEmp;
	@EJB
	private SrvSeguimientoLocal srvSgm;
	private Login beanLogin;
	private static final String FORMATOFECHA = "yyyy-MM-dd";

	private Carrera selectCarrera;
	private FichaDocente selectDocente;
	private List<Carrera> lstCarrera;
	private List<FichaDocente> lstDocente;
	private List<HorarioAcademico> lstHorarioAcademico;
	private String fecha;
	private Integer hhI;
	private Integer mmI;
	private Integer hhF;
	private Integer mmF;
	private Boolean flagGenerado;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		lstCarrera = srvEmp.listarCarreras(beanLogin.getUsuarioRol().getUsuario().getPersona().getFichaEmpleados()
				.get(0).getDetallePuestos().get(0).getCarrera().getDependencia().getDpnId());
		iniciarBean();
	}

	private void iniciarBean() {
		selectCarrera = new Carrera();
		selectDocente = new FichaDocente();
		flagGenerado = false;
	}

	public void setCarreraID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectCarrera.setCrrId((Integer) event.getNewValue());
			lstDocente = srvEmp.listarDocentesxCarrera(selectCarrera.getCrrId());
		} else {
			selectCarrera.setCrrId(null);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado una carrera.",
					"Warning!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void setDocenteID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			selectDocente.setFcdcId((Integer) event.getNewValue());
		} else {
			selectDocente.setFcdcId(null);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado un docente",
					"Warning!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void buscarHorariosDiarios() throws ParseException {
		Date date = new SimpleDateFormat(FORMATOFECHA).parse(fecha);
		int dia = date.getDay() - 1;
		Integer[] arrayHora = { hhI, mmI, hhF, mmF };
		lstHorarioAcademico = srvEmp.listarHorariosxDocentexFechaHora(selectDocente.getFcdcId(), arrayHora, dia);
	}

	public void limpiar() {
		lstDocente = null;
		fecha = "";
		hhI = 0;
		mmI = 0;
		hhF = 0;
		mmF = 0;
		lstHorarioAcademico = null;
		flagGenerado = false;
	}

	public void generarRegistros() throws ParseException {
		for (HorarioAcademico hrac : lstHorarioAcademico) {
			Date date = new SimpleDateFormat(FORMATOFECHA).parse(fecha);
			srvSgm.generarAsistencias(hrac, selectDocente, date);
		}
		flagGenerado = true;
	}

	// GETTERS AND SETTERS

	/**
	 * @return the selectCarrera
	 */
	public Carrera getSelectCarrera() {
		return selectCarrera;
	}

	/**
	 * @param selectCarrera
	 *            the selectCarrera to set
	 */
	public void setSelectCarrera(Carrera selectCarrera) {
		this.selectCarrera = selectCarrera;
	}

	/**
	 * @return the selectDocente
	 */
	public FichaDocente getSelectDocente() {
		return selectDocente;
	}

	/**
	 * @param selectDocente
	 *            the selectDocente to set
	 */
	public void setSelectDocente(FichaDocente selectDocente) {
		this.selectDocente = selectDocente;
	}

	/**
	 * @return the lstCarrera
	 */
	public List<Carrera> getLstCarrera() {
		return lstCarrera;
	}

	/**
	 * @param lstCarrera
	 *            the lstCarrera to set
	 */
	public void setLstCarrera(List<Carrera> lstCarrera) {
		this.lstCarrera = lstCarrera;
	}

	/**
	 * @return the lstDocente
	 */
	public List<FichaDocente> getLstDocente() {
		return lstDocente;
	}

	/**
	 * @param lstDocente
	 *            the lstDocente to set
	 */
	public void setLstDocente(List<FichaDocente> lstDocente) {
		this.lstDocente = lstDocente;
	}

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

	/**
	 * @return the hhI
	 */
	public Integer getHhI() {
		return hhI;
	}

	/**
	 * @param hhI
	 *            the hhI to set
	 */
	public void setHhI(Integer hhI) {
		this.hhI = hhI;
	}

	/**
	 * @return the mmI
	 */
	public Integer getMmI() {
		return mmI;
	}

	/**
	 * @param mmI
	 *            the mmI to set
	 */
	public void setMmI(Integer mmI) {
		this.mmI = mmI;
	}

	/**
	 * @return the hhF
	 */
	public Integer getHhF() {
		return hhF;
	}

	/**
	 * @param hhF
	 *            the hhF to set
	 */
	public void setHhF(Integer hhF) {
		this.hhF = hhF;
	}

	/**
	 * @return the mmF
	 */
	public Integer getMmF() {
		return mmF;
	}

	/**
	 * @param mmF
	 *            the mmF to set
	 */
	public void setMmF(Integer mmF) {
		this.mmF = mmF;
	}

	/**
	 * @return the lstHorarioAcademico
	 */
	public List<HorarioAcademico> getLstHorarioAcademico() {
		return lstHorarioAcademico;
	}

	/**
	 * @param lstHorarioAcademico
	 *            the lstHorarioAcademico to set
	 */
	public void setLstHorarioAcademico(List<HorarioAcademico> lstHorarioAcademico) {
		this.lstHorarioAcademico = lstHorarioAcademico;
	}

	/**
	 * @return the flagGenerado
	 */
	public Boolean getFlagGenerado() {
		return flagGenerado;
	}

	/**
	 * @param flagGenerado the flagGenerado to set
	 */
	public void setFlagGenerado(Boolean flagGenerado) {
		this.flagGenerado = flagGenerado;
	}
	
	

}
