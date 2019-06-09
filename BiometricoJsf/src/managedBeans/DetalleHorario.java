package managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.RowEditEvent;

import model.Aula;
import model.DiaSemana;
import model.FichaDocente;
import model.Horario;
import model.Materia;
import model.Paralelo;
import model.TipoHorario;
import servicios.SrvHorarioLocal;

@ManagedBean(name = "detalleHorario")
@ViewScoped
public class DetalleHorario {

	@EJB
	private SrvHorarioLocal srvHor;

	@ManagedProperty("#{registroPrl}")
	private RegistroParalelo paralelo;

	private List<DiaSemana> lstDia;
	private List<TipoHorario> lstTipoHorario;
	private List<FichaDocente> lstFichaDocente;
	private List<Aula> lstAula;
	private DiaSemana selectDia;
	private TipoHorario selectTipoHorario;
	private FichaDocente selectDocente;
	private Aula selectAula;
	private Horario selectHorario;
	private Horario registroHorario;
	private List<Horario> lstHorarios;

	private String horaEntrada;
	private String horaSalida;

	@PostConstruct
	public void init() {
		lstHorarios = paralelo.getLstHorarios();
		lstDia = srvHor.listarAllDias();
		lstTipoHorario = srvHor.listarTipoHorario();
		lstFichaDocente = srvHor.listarDocentes();
		lstAula = srvHor.listarAula();
		selectDia = new DiaSemana();
		selectTipoHorario = new TipoHorario();
		selectDocente = new FichaDocente();
		selectHorario = new Horario();
		selectAula = new Aula();
		initRegistroHorario();
	}

	/**
	 * Metodo que inicializa el objeto a guardar
	 */
	private void initRegistroHorario() {
		registroHorario = new Horario();
		registroHorario.setAula(new Aula());
		registroHorario.setDiaSemana(new DiaSemana());
		registroHorario.setFichaDocente(new FichaDocente());
		registroHorario.setMateria(new Materia());
		// registroHorario.setParalelo(new Paralelo());
		registroHorario.setTipoHorario(new TipoHorario());
	}

	public String actualizar(model.Horario hor) {
		System.out.println("Guardar cambios de horario");
		try {
			if (hor.getHrrId() != null) {
				System.out.println("Horario editado nuevo");
				validarHorario(hor);
			} else {
				System.out.println("Es registro nuevo");
				validarHorario(hor);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar horario" + e);
		}
		return "horario";
	}

	/**
	 * Metodo que valida campos para el registro o actualizacion del horario
	 * 
	 * @param paralelo
	 */
	private void validarHorario(Horario hor) {
		boolean cambios = false;
		for (DiaSemana dia : lstDia) {
			if (hor.getDiaSemana().getDsmNombreDia().equals(dia.getDsmNombreDia())) {
				if (hor.getDiaSemana().getDsmId() == null
						|| hor.getDiaSemana().getDsmId().compareTo(dia.getDsmId()) != 0) {
					cambios = true;
					hor.getDiaSemana().setDsmId(dia.getDsmId());
				}
				break;
			}
		}
		for (TipoHorario tipoHorario : lstTipoHorario) {
			if (hor.getTipoHorario().getTphrDescripcion().equals(tipoHorario.getTphrDescripcion())) {
				if (hor.getTipoHorario().getTphrId() == null
						|| hor.getTipoHorario().getTphrId().compareTo(tipoHorario.getTphrId()) != 0) {
					cambios = true;
					hor.getTipoHorario().setTphrId(tipoHorario.getTphrId());
				}
				break;
			}
		}
		for (FichaDocente fichaDocente : lstFichaDocente) {
			if (hor.getFichaDocente().getFcdcPrimerNombre().equals(fichaDocente.getFcdcPrimerNombre())) {
				if (hor.getFichaDocente().getFcdcId() == null
						|| hor.getFichaDocente().getFcdcId().compareTo(fichaDocente.getFcdcId()) != 0) {
					cambios = true;
					hor.getFichaDocente().setFcdcId(fichaDocente.getFcdcId());
				}
				break;
			}
		}
		for (Aula aula : lstAula) {
			if (hor.getAula().getAulNombre().equals(aula.getAulNombre())) {
				if (hor.getAula().getAulId() == null || hor.getAula().getAulId().compareTo(aula.getAulId()) != 0) {
					cambios = true;
					hor.getAula().setAulId(aula.getAulId());
				}
				break;
			}
		}
		if (cambios) {
			srvHor.guardarHorario(hor);
		} else {
			System.out.println("Registro sin cambios: " + hor.getHrrId());
		}
	}

	public String guardar() {
		System.out.println("Guardar cambios de horario");
		registroHorario.getMateria().setMtrId(paralelo.getSelectMtr().getMtrId());
		// registroHorario.getParalelo().setPrlId(horario.getSelectPar().getPrlId());
		srvHor.guardarHorario(registroHorario);
		initRegistroHorario();
		return "horario";
	}

	public String regresar() {
		System.out.println("Regresar al menu de horarios");
		return "registroParalelo";
	}

	public void setDiaID(ValueChangeEvent event) {
		System.out.println("Dia de la semana ");
	}

	public void setTipoHorarioID(ValueChangeEvent event) {
		System.out.println("Tipo horario " + event.getNewValue());
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

	public void onRowEdit(RowEditEvent event) {
		actualizar((Horario) event.getObject());
		FacesMessage msg = new FacesMessage("Registro editado",
				((Horario) event.getObject()).getDiaSemana().getDsmNombreDia());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada",
				((Horario) event.getObject()).getDiaSemana().getDsmNombreDia());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onAddNew() {
		Horario h = new Horario();
		h.setAula(new Aula());
		h.setDiaSemana(new DiaSemana());
		h.setFichaDocente(new FichaDocente());
		h.setMateria(new Materia());

		// h.setParalelo(new Paralelo());
		if (lstHorarios != null && lstHorarios.size() == 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			RegistroParalelo rp = context.getApplication().evaluateExpressionGet(context, "#{registroPrl}",
					RegistroParalelo.class);
			h.getMateria().setMtrId(rp.getMtrId());
		} else {
			h.getMateria().setMtrId(lstHorarios.get(0).getMateria().getMtrId());
		}
		// h.setParalelo(new Paralelo());
		// h.getParalelo().setPrlId(lstHorarios.get(0).getParalelo().getPrlId());

		h.setTipoHorario(new TipoHorario());
		lstHorarios.add(h);

		FacesMessage msg = new FacesMessage("Nuevo registro añadido");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void eliminarHorario(model.Horario hor) {
		System.out.println("Metodo que elimina el horario");
		List<Horario> lstAux = new ArrayList<>();
		for (Horario horario : lstHorarios) {
			if (horario.getHrrId() != hor.getHrrId()) {
				lstAux.add(horario);
			} else {
				srvHor.eliminarHorario(horario);
			}
		}
		System.out.println("Nueva lista de horarios" + lstAux.size());
		lstHorarios = lstAux;
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

	public RegistroParalelo getParalelo() {
		return paralelo;
	}

	public void setParalelo(RegistroParalelo paralelo) {
		this.paralelo = paralelo;
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
