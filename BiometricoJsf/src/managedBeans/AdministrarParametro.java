/**
 * 
 */
package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Facultad;
import model.Parametro;
import servicios.SrvAdministrarParametroLocal;

/**
 * @author wilso
 *
 */

@ManagedBean(name = "administrarParametro")
@ViewScoped
public class AdministrarParametro {

	@EJB
	private SrvAdministrarParametroLocal srvAdm;

	private List<Parametro> lstParametro;
	private Parametro registroParametro;
	private Facultad selectFacultad;
	private List<Facultad> lstFacultad;
	private String horaEntrada;
	private String horaSalida;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Principal p = context.getApplication().evaluateExpressionGet(context, "#{principal}", Principal.class);
		selectFacultad = new Facultad();
		lstFacultad = srvAdm.listarFacultades();
		initRegistroParametro();
	}

	/**
	 * Metodo que inicializa el objeto a guardar
	 */
	private void initRegistroParametro() {
		registroParametro = new Parametro();
	}

	public void limpiarFiltros() {
	}

	public void buscarParametros(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de buscar parametros por facultad: " + event.getNewValue());
			lstParametro = srvAdm.listarParametro((Integer) event.getNewValue());
			if (!lstParametro.isEmpty()) {
				horaEntrada = transformMillisToHour(lstParametro.get(0).getPrmValor());
				horaSalida = transformMillisToHour(lstParametro.get(1).getPrmValor());
				System.out.println("Si hay Parametros: " + lstParametro.size());
			} else {
				horaEntrada = "";
				horaSalida = "";
				System.out.println("No hay Parametros para la facultad seleccionada.");
			}
		} else {
			lstParametro = null;
		}
	}

	/**
	 * Metodo para transformar tiempo a horas
	 * 
	 * @param prmValor
	 * @return
	 */
	private String transformMillisToHour(Integer prmValor) {
		String result;
		int minutes = (prmValor / (1000 * 60)) % 60;
		int hours = (prmValor / (1000 * 60 * 60)) % 24;
		if (hours < 10) {
			result = "0" + hours;
		} else {
			result = "" + hours;
		}
		if (minutes < 10) {
			result += ":0" + minutes;
		} else {
			result += ":" + minutes;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	private String guardarParametro() {
		System.out.println("Metodo guardar");
		return "principal";
	}

	/**
	 * The srvAdm to get.
	 * 
	 * @return the srvAdm
	 */
	public SrvAdministrarParametroLocal getSrvAdm() {
		return srvAdm;
	}

	/**
	 * The srvAdm to set.
	 * 
	 * @param srvAdm
	 */
	public void setSrvAdm(SrvAdministrarParametroLocal srvAdm) {
		this.srvAdm = srvAdm;
	}

	/**
	 * The lstParametro to get.
	 * 
	 * @return the lstParametro
	 */
	public List<Parametro> getLstParametro() {
		return lstParametro;
	}

	/**
	 * The lstParametro to set.
	 * 
	 * @param lstParametro
	 */
	public void setLstParametro(List<Parametro> lstParametro) {
		this.lstParametro = lstParametro;
	}

	/**
	 * The registroParametro to get.
	 * 
	 * @return the registroParametro
	 */
	public Parametro getRegistroParametro() {
		return registroParametro;
	}

	/**
	 * The registroParametro to set.
	 * 
	 * @param registroParametro
	 */
	public void setRegistroParametro(Parametro registroParametro) {
		this.registroParametro = registroParametro;
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
	 * The horaEntrada to get.
	 * 
	 * @return the horaEntrada
	 */
	public String getHoraEntrada() {
		return horaEntrada;
	}

	/**
	 * The horaEntrada to set.
	 * 
	 * @param horaEntrada
	 */
	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	/**
	 * The horaSalida to get.
	 * 
	 * @return the horaSalida
	 */
	public String getHoraSalida() {
		return horaSalida;
	}

	/**
	 * The horaSalida to set.
	 * 
	 * @param horaSalida
	 */
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
}
