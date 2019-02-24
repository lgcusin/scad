package managedBeans;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Carrera;
import model.FichaDocente;
import model.TipoHorario;
import servicios.SrvReporteHorarioLocal;

/**
 * @author wilso
 *
 */

@ManagedBean(name = "reporteHorario")
@ViewScoped
public class ReporteHorario {

	@EJB
	private SrvReporteHorarioLocal srvRepHor;

	private Carrera selectCrr;
	private FichaDocente fichaDocente;
	private Collection<String[]> resultHorarios;
	private TipoHorario selectTipoHorario;
	private Collection<TipoHorario> lstTH;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Login login = context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
		fichaDocente = login.getFd();
		selectCrr = new Carrera();
		selectTipoHorario = new TipoHorario();
		lstTH = srvRepHor.listarTipoHorario();
	}

	public void buscarHorarios() {
		System.out.println("Metodo para ver informacion de horario");
		// if (fichaDocente.getFcdcId() != null && selectCrr.getCrrId() != null)
		// {
		if (selectCrr.getCrrId() != null && selectTipoHorario.getTphrId() != null) {
			// resultHorarios =
			// srvRepHor.listarHorarios(fichaDocente.getFcdcId(),
			// selectCrr.getCrrId());
			resultHorarios = srvRepHor.listarHorarios(2, selectCrr.getCrrId(), selectTipoHorario.getTphrId());
		} else {
			resultHorarios = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Verifique los parametros de  busqueda para los horarios", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			System.out.println("Verifique los parametros de  busqueda para los horarios");
		}
	}

	public void limpiarFiltros() {
		selectCrr.setCrrId(null);
		selectTipoHorario.setTphrId(null);
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
		return fichaDocente;
	}

	/**
	 * The fichaDocente to set.
	 * 
	 * @param fichaDocente
	 */
	public void setFichaDocente(FichaDocente fichaDocente) {
		this.fichaDocente = fichaDocente;
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

	/**
	 * The selectTipoHorario to get.
	 * 
	 * @return the selectTipoHorario
	 */
	public TipoHorario getSelectTipoHorario() {
		return selectTipoHorario;
	}

	/**
	 * The selectTipoHorario to set.
	 * 
	 * @param selectTipoHorario
	 */
	public void setSelectTipoHorario(TipoHorario selectTipoHorario) {
		this.selectTipoHorario = selectTipoHorario;
	}

	/**
	 * The lstTH to get.
	 * 
	 * @return the lstTH
	 */
	public Collection<TipoHorario> getLstTH() {
		return lstTH;
	}

	/**
	 * The lstTH to set.
	 * 
	 * @param lstTH
	 */
	public void setLstTH(Collection<TipoHorario> lstTH) {
		this.lstTH = lstTH;
	}
}
