package ec.edu.uce.Biometrico.jsf.managedBeans.biometrico;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ec.edu.uce.academico.ejb.servicios.biometrico.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.academico.ejb.servicios.biometrico.interfaces.SrvReporteHorarioLocal;
import ec.edu.uce.academico.ejb.servicios.biometrico.interfaces.SrvSeguimientoLocal;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
//import ec.edu.uce.academico.jpa.entidades.publico.TipoHorario;

/**
 * @author wilso
 *
 */

@ManagedBean(name = "reporteHorario")
@ViewScoped
public class ReporteHorario {

	@EJB
	private SrvReporteHorarioLocal srvRepHor;
	@EJB
	private SrvSeguimientoLocal srvSegm;
	@EJB
	private SrvEmpleadoLocal srvEmp;
	private Login beanLogin;

	private Carrera selectCrr;
	// private TipoHorario selectTipoHorario;
	private FichaDocente fcDc;
	private Collection<String[]> resultHorarios;
	// private Collection<TipoHorario> lstTH;
	private List<Carrera> lstCr;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		beanLogin = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		if (beanLogin.Docente) {
			lstCr = srvSegm.listarAllCrrByFcdc(beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona()
					.getPrsFichaDocentes().get(0).getFcdcId());
		}
		if (beanLogin.adminFacultad) {
			lstCr = srvEmp.listarCarreras(beanLogin.getDt().get(0).getDtpsCarrera().getCrrDependencia().getDpnId());
		}
		// lstTH = srvRepHor.listarTipoHorario();
		selectCrr = new Carrera();
		// selectTipoHorario = new TipoHorario();

	}

	public void buscarHorarios() {
		System.out.println("Metodo para ver informacion de horario");
		if (selectCrr != null && selectCrr.getCrrId() > 0) {
			// if
			// (beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona().getPrsFichaDocentes().get(0)
			// .getFcdcId() > 0) {
			// resultHorarios =
			// srvRepHor.listarHorarios(beanLogin.getUsuarioRol().getUsroUsuario().getUsrPersona()
			// .getPrsFichaDocentes().get(0).getFcdcId(), selectCrr.getCrrId(),
			// selectTipoHorario.getTphrId());
			// } else {
			// resultHorarios = srvRepHor.listarHorarios(null,
			// selectCrr.getCrrId(), selectTipoHorario.getTphrId());
			// }

		} else {
			resultHorarios = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Verifique los parametros de  busqueda para los horarios", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			System.out.println("Verifique los parametros de  busqueda para los horarios");
		}
	}

	public void limpiarFiltros() {
		selectCrr.setCrrId(0);
		// selectTipoHorario.setTphrId(null);
		resultHorarios = null;
	}

	public String regresar() {
		return "inicio";
	}

	public void imprimir() {
		System.out.println("Metodo de imprimir");
	}

	/**
	 * The selectCrr to get.
	 * 
	 * @return the selectCrr
	 */
	public Carrera getSelectCrr() {
		return selectCrr;
	}

	/**
	 * The selectCrr to set.
	 * 
	 * @param selectCrr
	 */
	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
	}

	/**
	 * The fichaDocente to get.
	 * 
	 * @return the fichaDocente
	 */
	public FichaDocente getFichaDocente() {
		return fcDc;
	}

	/**
	 * The fichaDocente to set.
	 * 
	 * @param fichaDocente
	 */
	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fcDc = fichaDocente;
	}

	/**
	 * The resultHorarios to get.
	 * 
	 * @return the resultHorarios
	 */
	public Collection<String[]> getResultHorarios() {
		return resultHorarios;
	}

	/**
	 * The resultHorarios to set.
	 * 
	 * @param resultHorarios
	 */
	public void setResultHorarios(Collection<String[]> resultHorarios) {
		this.resultHorarios = resultHorarios;
	}

	public List<Carrera> getLstCr() {
		return lstCr;
	}

	public void setLstCr(List<Carrera> lstCr) {
		this.lstCr = lstCr;
	}

}
