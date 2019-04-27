/**
 * 
 */
package managedBeans;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Facultad;
import model.Feriado;
import servicios.SrvRegistroFeriadoLocal;

/**
 * @author wilso
 *
 */

@ManagedBean(name = "registroFeriado")
@ViewScoped
public class RegistroFeriado {

	@EJB
	private SrvRegistroFeriadoLocal srvFer;

	private Facultad selectFacultad;
	private List<Facultad> lstFacultad;
	private Date fechaInicio;
	private Date fechaFin;
	private Collection<Feriado> lstFeriados;
	private Feriado feriadoRegistro;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		lstFacultad = srvFer.listarFacultades();
		initRegistroFeriado();
	}

	/**
	 * Metodo que inicializa el objeto a guardar
	 */
	private void initRegistroFeriado() {
		feriadoRegistro = new Feriado();
	}

	public void limpiarFiltros() {
	}

	public void limpiarFiltrosModal() {
	}

	public void guardarFeriado() {
		srvFer.guardarActualizarFeriado(feriadoRegistro);
	}

	public void buscarFeriados() {
		lstFeriados = srvFer.listarFeriados(1, fechaInicio, fechaFin);
	}

	/**
	 * The srvFer to get.
	 * 
	 * @return the srvFer
	 */
	public SrvRegistroFeriadoLocal getSrvFer() {
		return srvFer;
	}

	/**
	 * The srvFer to set.
	 * 
	 * @param srvFer
	 */
	public void setSrvFer(SrvRegistroFeriadoLocal srvFer) {
		this.srvFer = srvFer;
	}

	/**
	 * The selectFacultad to get.
	 * 
	 * @return the selectFacultad
	 */
	public Facultad getSelectFacultad() {
		return selectFacultad;
	}

	/**
	 * The selectFacultad to set.
	 * 
	 * @param selectFacultad
	 */
	public void setSelectFacultad(Facultad selectFacultad) {
		this.selectFacultad = selectFacultad;
	}

	/**
	 * The lstFacultad to get.
	 * 
	 * @return the lstFacultad
	 */
	public List<Facultad> getLstFacultad() {
		return lstFacultad;
	}

	/**
	 * The lstFacultad to set.
	 * 
	 * @param lstFacultad
	 */
	public void setLstFacultad(List<Facultad> lstFacultad) {
		this.lstFacultad = lstFacultad;
	}

	/**
	 * The fechaInicio to get.
	 * 
	 * @return the fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * The fechaInicio to set.
	 * 
	 * @param fechaInicio
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * The fechaFin to get.
	 * 
	 * @return the fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * The fechaFin to set.
	 * 
	 * @param fechaFin
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * The lstFeriados to get.
	 * 
	 * @return the lstFeriados
	 */
	public Collection<Feriado> getLstFeriados() {
		return lstFeriados;
	}

	/**
	 * The lstFeriados to set.
	 * 
	 * @param lstFeriados
	 */
	public void setLstFeriados(Collection<Feriado> lstFeriados) {
		this.lstFeriados = lstFeriados;
	}

	/**
	 * The feriado to get.
	 * 
	 * @return the feriado
	 */
	public Feriado getFeriado() {
		return feriadoRegistro;
	}

	/**
	 * The feriado to set.
	 * 
	 * @param feriado
	 */
	public void setFeriado(Feriado feriado) {
		this.feriadoRegistro = feriado;
	}
}
