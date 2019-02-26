package managedBeans;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Carrera;
import model.FichaDocente;
import model.TipoHorario;
import servicios.SrvEmpleadoLocal;
import servicios.SrvReporteHorarioLocal;
import servicios.SrvSeguimientoLocal;

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

	private Carrera selectCrr;
	private TipoHorario selectTipoHorario;
	private FichaDocente fcDc;
	private Collection<String[]> resultHorarios;
	private Collection<TipoHorario> lstTH;
	private List<Carrera> lstCr;
	public Integer fdId;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		if (p.docente) {
			fdId = p.getFdId();
			lstCr = srvSegm.listarAllCrrByFcdc(fdId);
		}
		if (p.empleado) {
			lstCr = srvEmp.listarCarreras();
		}
		lstTH = srvRepHor.listarTipoHorario();
		selectCrr = new Carrera();
		selectTipoHorario = new TipoHorario();

	}

	public void buscarHorarios() {
		System.out.println("Metodo para ver informacion de horario");
		if (selectCrr.getCrrId() != null && selectTipoHorario.getTphrId() != null) {
			// resultHorarios =
			// srvRepHor.listarHorarios(fichaDocente.getFcdcId(),
			// selectCrr.getCrrId());
			if(fdId!=null){
				resultHorarios = srvRepHor.listarHorarios(fdId, selectCrr.getCrrId(), selectTipoHorario.getTphrId());
			}else{
				resultHorarios = srvRepHor.listarHorarios(selectCrr.getCrrId(), selectTipoHorario.getTphrId());
			}
			
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

	public List<Carrera> getLstCr() {
		return lstCr;
	}

	public void setLstCr(List<Carrera> lstCr) {
		this.lstCr = lstCr;
	}

}
