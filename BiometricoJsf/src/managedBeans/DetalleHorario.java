package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Aula;
import model.DiaSemana;
import model.FichaDocente;
import model.FichaEstudiante;
import model.Materia;
import model.Paralelo;
import model.TipoHorario;
import servicios.SrvHorarioLocal;

@ManagedBean(name = "detalleHorario")
@ViewScoped
public class DetalleHorario {

	@EJB
	private SrvHorarioLocal srvHor;

	@ManagedProperty("#{horario}")
	private Horario horario;

	private String title;
	private List<DiaSemana> lstDia;
	private List<TipoHorario> lstTipoHorario;
	private List<FichaDocente> lstFichaDocente;
	private List<Aula> lstAula;
	private DiaSemana selectDia;
	private TipoHorario selectTipoHorario;
	private FichaDocente selectDocente;
	private Aula selectAula;
	private model.Horario selectHorario;
	private model.Horario registroHorario;
	private List<model.Horario> lstHorarios;

	private String horaEntrada;
	private String horaSalida;

	@PostConstruct
	public void init() {
		title = null;
		FacesContext context = FacesContext.getCurrentInstance();
		lstHorarios = horario.getLstHorarios();
		tituloDetalleHorario();
		lstDia = srvHor.listarAllDias();
		lstTipoHorario = srvHor.listarTipoHorario();
		lstFichaDocente = srvHor.listarDocentes();
		lstAula = srvHor.listarAula();
		selectDia = new DiaSemana();
		selectTipoHorario = new TipoHorario();
		selectDocente = new FichaDocente();
		selectHorario = new model.Horario();
		initRegistroHorario();
		selectAula = new Aula();
		horaEntrada = "08:00";
		horaSalida = "10:00";
	}

	/**
	 * Metodo que inicializa el objeto a guardar
	 */
	private void initRegistroHorario() {
		registroHorario = new model.Horario();
		registroHorario.setAula(new Aula());
		registroHorario.setDiaSemana(new DiaSemana());
		registroHorario.setFichaDocente(new FichaDocente());
		registroHorario.setMateria(new Materia());
		// registroHorario.setParalelo(new Paralelo());
		registroHorario.setTipoHorario(new TipoHorario());
	}

	private void tituloDetalleHorario() {
		if (horario.getFlagEditar()) {
			title = "Informaci\u00f3n del horario a editar";
		} else {
			title = "Informaci\u00f3n del horario registrado";
		}
	}

	public String actualizar() {
		System.out.println("Guardar cambios de horario");
		// selectHorario.setHrrId(selectHorario.getHrrId());
		// selectHorario.setHrrInicio(selectHorario.getHrrInicio());
		// selectHorario.setHrrFin(selectHorario.getHrrFin());
		// selectHorario.setTipoHorario(selectTipoHorario);
		// selectHorario.setFichaDocente(selectDocente);
		// selectHorario.setMateria(horario.getSelectMtr());
		// selectHorario.setAula(selectAula);
		// selectHorario.setDiaSemana(selectDia);
		srvHor.guardarHorario(selectHorario);
		return "horario";
	}

	public String guardar() {
		System.out.println("Guardar cambios de horario");
		registroHorario.getMateria().setMtrId(horario.getSelectMtr().getMtrId());
		// registroHorario.getParalelo().setPrlId(horario.getSelectPar().getPrlId());
		srvHor.guardarHorario(registroHorario);
		initRegistroHorario();
		return "horario";
	}

	public String regresar() {
		System.out.println("Regresar al menu de horarios");
		return "horario";
	}

	public void setDiaID(ValueChangeEvent event) {
		System.out.println("Dia de la semana " + event);
	}

	public void setTipoHorarioID(ValueChangeEvent event) {
		System.out.println("Tipo horario " + event);
	}

	public void setDocenteID(ValueChangeEvent event) {
		System.out.println("Docente " + event);
	}

	public void setAulaID(ValueChangeEvent event) {
		System.out.println("Aula " + event);
	}

	public String editarHorario() {
		System.out.println("Metodo para ver informacion de horario" + selectHorario.toString());
		return "horario";
	}

	public void setDataHorario() {
		System.out.println("Boton de abrir modal" + selectHorario);
	}

	public boolean validarRow() {
		if (selectHorario.getDiaSemana() != null) {
			System.out.println("Si hay datos");
			return true;
		} else {
			System.out.println("No hay datos");
			return false;
		}

	}

	public void onAddNew() {
		model.Horario h = new model.Horario();
		h.setAula(new Aula());
		h.setDiaSemana(new DiaSemana());
		h.setFichaDocente(new FichaDocente());
		h.setMateria(new Materia());
		// h.setParalelo(new Paralelo());
		h.setTipoHorario(new TipoHorario());
		// horario.getLstHorarios().add(h);
		lstHorarios.add(h);
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
	 * The horario to get.
	 * 
	 * @return the horario
	 */
	public Horario getHorario() {
		return horario;
	}

	/**
	 * The horario to set.
	 * 
	 * @param horario
	 */
	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	/**
	 * The title to get.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The title to set.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The lstDia to get.
	 * 
	 * @return the lstDia
	 */
	public List<DiaSemana> getLstDia() {
		return lstDia;
	}

	/**
	 * The lstDia to set.
	 * 
	 * @param lstDia
	 */
	public void setLstDia(List<DiaSemana> lstDia) {
		this.lstDia = lstDia;
	}

	/**
	 * The selectDia to get.
	 * 
	 * @return the selectDia
	 */
	public DiaSemana getSelectDia() {
		return selectDia;
	}

	/**
	 * The selectDia to set.
	 * 
	 * @param selectDia
	 */
	public void setSelectDia(DiaSemana selectDia) {
		this.selectDia = selectDia;
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

	/**
	 * The lstTipoHorario to get.
	 * 
	 * @return the lstTipoHorario
	 */
	public List<TipoHorario> getLstTipoHorario() {
		return lstTipoHorario;
	}

	/**
	 * The lstTipoHorario to set.
	 * 
	 * @param lstTipoHorario
	 */
	public void setLstTipoHorario(List<TipoHorario> lstTipoHorario) {
		this.lstTipoHorario = lstTipoHorario;
	}

	/**
	 * The lstFichaDocente to get.
	 * 
	 * @return the lstFichaDocente
	 */
	public List<FichaDocente> getLstFichaDocente() {
		return lstFichaDocente;
	}

	/**
	 * The lstFichaDocente to set.
	 * 
	 * @param lstFichaDocente
	 */
	public void setLstFichaDocente(List<FichaDocente> lstFichaDocente) {
		this.lstFichaDocente = lstFichaDocente;
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
	 * The selectDocente to get.
	 * 
	 * @return the selectDocente
	 */
	public FichaDocente getSelectDocente() {
		return selectDocente;
	}

	/**
	 * The selectDocente to set.
	 * 
	 * @param selectDocente
	 */
	public void setSelectDocente(FichaDocente selectDocente) {
		this.selectDocente = selectDocente;
	}

	/**
	 * The lstAula to get.
	 * 
	 * @return the lstAula
	 */
	public List<Aula> getLstAula() {
		return lstAula;
	}

	/**
	 * The lstAula to set.
	 * 
	 * @param lstAula
	 */
	public void setLstAula(List<Aula> lstAula) {
		this.lstAula = lstAula;
	}

	/**
	 * The selectAula to get.
	 * 
	 * @return the selectAula
	 */
	public Aula getSelectAula() {
		return selectAula;
	}

	/**
	 * The selectAula to set.
	 * 
	 * @param selectAula
	 */
	public void setSelectAula(Aula selectAula) {
		this.selectAula = selectAula;
	}

	/**
	 * The selectHorario to get.
	 * 
	 * @return the selectHorario
	 */
	public model.Horario getSelectHorario() {
		return selectHorario;
	}

	/**
	 * The selectHorario to set.
	 * 
	 * @param selectHorario
	 */
	public void setSelectHorario(model.Horario selectHorario) {
		this.selectHorario = selectHorario;
	}

	/**
	 * The registroHorario to get.
	 * 
	 * @return the registroHorario
	 */
	public model.Horario getRegistroHorario() {
		return registroHorario;
	}

	/**
	 * The registroHorario to set.
	 * 
	 * @param registroHorario
	 */
	public void setRegistroHorario(model.Horario registroHorario) {
		this.registroHorario = registroHorario;
	}

	/**
	 * The lstHorarios to get.
	 * 
	 * @return the lstHorarios
	 */
	public List<model.Horario> getLstHorarios() {
		return lstHorarios;
	}

	/**
	 * The lstHorarios to set.
	 * 
	 * @param lstHorarios
	 */
	public void setLstHorarios(List<model.Horario> lstHorarios) {
		this.lstHorarios = lstHorarios;
	}
}
