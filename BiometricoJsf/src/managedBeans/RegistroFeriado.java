/**
 * 
 */
package managedBeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Facultad;
import model.Feriado;
import servicios.SrvRegistroFeriadoLocal;

/**
 * @author wilso
 *
 */

@ManagedBean(name = "registroFeriado")
@SessionScoped
public class RegistroFeriado {

	private static final String FORMATOFECHA = "yyyy-MM-dd";
	@EJB
	private SrvRegistroFeriadoLocal srvFer;

	private Facultad selectFacultad;
	private List<Facultad> lstFacultad;
	private String fechaInicio;
	private String fechaFin;
	private Collection<Feriado> lstFeriados;
	private Feriado feriadoRegistro;
	private String fechaFeriado;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		// Principal p = context.getApplication().evaluateExpressionGet(context,
		// "#{principal}", Principal.class);
		lstFacultad = srvFer.listarFacultades();
		initRegistroFeriado();
	}

	/**
	 * Metodo que inicializa el objeto a guardar
	 */
	private void initRegistroFeriado() {
		feriadoRegistro = new Feriado();
		selectFacultad = new Facultad();
	}

	public void limpiarFiltros() {
	}

	public void limpiarFiltrosModal() {
		System.out.println("Metodo limpiar");
	}

	public void guardarFeriado() {
		System.out.println("Metodo guardar");
		// Date fecha;
		// try {
		// fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaFeriado);
		// feriadoRegistro.setFrdFecha(fecha);
		feriadoRegistro.setFclId(selectFacultad.getFclId());
		srvFer.guardarActualizarFeriado(feriadoRegistro);
		// } catch (ParseException e) {
		// System.out.println("Error al transformar fecha del feriado " + e);
		// }
	}

	public void buscarFeriados() {
		System.out.println("Metodo buscar");
		try {
			Date inicio = new SimpleDateFormat(FORMATOFECHA).parse(fechaInicio);
			Date fin = new SimpleDateFormat(FORMATOFECHA).parse(fechaFin);
			lstFeriados = srvFer.listarFeriados(selectFacultad.getFclId(), inicio, fin);
			if (!lstFeriados.isEmpty()) {
				System.out.println("Feriados encontrados " + lstFeriados.size());
			} else {
				System.out.println("No se ha encontrado feriados");
			}
		} catch (ParseException e) {
			System.out.println("Error al convertir fechas para consulta");
		}
	}

	public void setFacultadID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo facultad: " + event.getNewValue());
			selectFacultad.setFclId((Integer) event.getNewValue());
		} else {
			selectFacultad.setFclId(null);
			System.out.println("No ha seleccionada una carrera: ");
		}
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

	/**
	 * The feriadoRegistro to get.
	 * 
	 * @return the feriadoRegistro
	 */
	public Feriado getFeriadoRegistro() {
		return feriadoRegistro;
	}

	/**
	 * The feriadoRegistro to set.
	 * 
	 * @param feriadoRegistro
	 */
	public void setFeriadoRegistro(Feriado feriadoRegistro) {
		this.feriadoRegistro = feriadoRegistro;
	}

	/**
	 * The fechaInicio to get.
	 * 
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * The fechaInicio to set.
	 * 
	 * @param fechaInicio
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * The fechaFin to get.
	 * 
	 * @return the fechaFin
	 */
	public String getFechaFin() {
		return fechaFin;
	}

	/**
	 * The fechaFin to set.
	 * 
	 * @param fechaFin
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * The fechaFeriado to get.
	 * 
	 * @return the fechaFeriado
	 */
	public String getFechaFeriado() {
		return fechaFeriado;
	}

	/**
	 * The fechaFeriado to set.
	 * 
	 * @param fechaFeriado
	 */
	public void setFechaFeriado(String fechaFeriado) {
		this.fechaFeriado = fechaFeriado;
	}
}
