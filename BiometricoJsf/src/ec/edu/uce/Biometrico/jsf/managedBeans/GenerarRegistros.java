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
import ec.edu.uce.Biometrico.jsf.utilidades.FacesUtil;
import ec.edu.uce.biometrico.jpa.Carrera;
import ec.edu.uce.biometrico.jpa.FichaDocente;
import ec.edu.uce.biometrico.jpa.HorarioAcademico;

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
	private String hhI;
	private String hhF;
	private Boolean flagGenerado;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		lstCarrera = srvEmp.listarCarrerasxFacultad(beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId());
		iniciarBean();
	}

	private void iniciarBean() {
		selectCarrera = new Carrera();
		selectDocente = new FichaDocente();
		flagGenerado = false;
		hhI = "07:00";
		hhF = "22:00";
	}

	public void setCarreraID(ValueChangeEvent event) {
		if (event.getNewValue() != null && (Integer) event.getNewValue() > 0) {
			selectCarrera.setCrrId((Integer) event.getNewValue());
			lstDocente = srvEmp.listarDocentesxCarrera(selectCarrera.getCrrId());
		}
	}

	public void setDocenteID(ValueChangeEvent event) {
		if (event.getNewValue() != null && (Integer) event.getNewValue() > 0) {
			selectDocente.setFcdcId((Integer) event.getNewValue());
		}
	}

	@SuppressWarnings("deprecation")
	public void buscarHorariosDiarios() throws ParseException {
		if (selectCarrera != null && selectDocente != null) {
			if (fecha != null && fecha.length() > 0) {
				Date date = new SimpleDateFormat(FORMATOFECHA).parse(fecha);
				int dia = date.getDay() - 1;
				if (hhI != null && hhF != null) {

					Integer[] arrayHora = obtenerHoras(hhI, hhF);
					lstHorarioAcademico = srvEmp.listarHorariosxDocentexFechaHora(selectDocente.getFcdcId(), arrayHora,
							dia, selectCarrera.getCrrId());
					if (lstHorarioAcademico.isEmpty()) {
						FacesUtil.mensajeInfo("No existen horarios academicos con los parametros ingresados");
					} else {
						flagGenerado = false;
					}
				} else {
					FacesUtil.mensajeError("Los campos hora de inicio y fin son requerida");
				}

			} else {
				FacesUtil.mensajeError("El campo fecha es requerida");
			}
		} else {
			FacesUtil.mensajeError("Seleccione los campos requeridos");
		}

	}

	private Integer[] obtenerHoras(String hhI, String hhF) {
		Integer[] retorno = { Integer.parseInt(hhI.substring(0, 2)), Integer.parseInt(hhF.substring(0, 2)) };
		return retorno;
	}

	public void limpiar() {
		lstDocente = null;
		fecha = "";
		hhI = "07:00";
		hhF = "22:00";
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

	public String regresar() {
		selectCarrera = null;
		selectDocente = null;
		lstCarrera = null;
		lstDocente = null;
		lstHorarioAcademico = null;
		fecha = null;
		hhI = null;
		hhF = null;
		flagGenerado = false;
		return "principal";
	}
	// GETTERS AND SETTERS

	/**
	 * @return the selectCarrera
	 */
	public Carrera getSelectCarrera() {
		return selectCarrera;
	}

	public String getHhI() {
		return hhI;
	}

	public void setHhI(String hhI) {
		this.hhI = hhI;
	}

	public String getHhF() {
		return hhF;
	}

	public void setHhF(String hhF) {
		this.hhF = hhF;
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
	 * @param flagGenerado
	 *            the flagGenerado to set
	 */
	public void setFlagGenerado(Boolean flagGenerado) {
		this.flagGenerado = flagGenerado;
	}

}
