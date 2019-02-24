package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import model.Carrera;
import model.Materia;
import model.Paralelo;
import model.Semestre;
import servicios.SrvHorarioLocal;

@ManagedBean(name = "horario")
@SessionScoped
public class Horario {

	@EJB
	private SrvHorarioLocal srvHor;

	private Materia selectMtr;
	private Carrera selectCrr;
	private Semestre selectSem;
	private Paralelo selectPar;
	private Integer crrId;
	private Integer mtrId;
	private Integer smsId;
	private List<Carrera> lstC;
	private List<Materia> lstM;
	private List<Materia> lstMbySemestre;
	private List<Semestre> lstS;
	private List<Paralelo> lstParalelos;
	private List<model.Horario> lstHorarios;
	private Boolean flagEditar;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		selectCrr = new Carrera();
		selectMtr = new Materia();
		selectSem = new Semestre();
		lstC = srvHor.listarAllCrr();
		lstM = srvHor.listarAllMat();
		lstS = srvHor.listarAllSem();
	}

	public void buscarParalelos() {
		System.out.println("Metodo que busca paralelos existentes por materia y semestre");
		if (crrId != null && mtrId != null && smsId != null) {
			lstParalelos = srvHor.listarParalelosHorario(mtrId);
			if (lstParalelos != null) {
				System.out.println("Paralelos encontrados: " + lstParalelos.size());
			} else {
				System.out.println("Paralelos NO encontrados");
			}
		} else {
			System.out.println("Seleccione la carrera y materia a listar paralelos");
		}
	}

	public void buscarMateria(ValueChangeEvent event) {
		if (event.getNewValue() != null && crrId != null) {
			lstMbySemestre = null;
			smsId = (Integer) event.getNewValue();
			System.out.println("Metodo de buscar materias por id semestre: " + event.getNewValue());
			lstMbySemestre = srvHor.listarMatBySem((Integer) event.getNewValue(), crrId);
			if (lstMbySemestre != null) {
				System.out.println("Si hay de materias por semestre seleccionado: " + lstMbySemestre.size());
			} else {
				System.out.println("No hay de materias por semestre seleccionado");
			}
		} else {
			lstMbySemestre = null;
		}
	}

	public void mostrarHorariosMateria(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de buscar horarios por id materia: " + event.getNewValue());
			mtrId = (Integer) event.getNewValue();
		}
	}

	public void setCarreraID(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("Metodo de setear codigo carrera: " + event.getNewValue());
			crrId = (Integer) event.getNewValue();
			// lstS= srvHor.listarAllSem();
			selectSem.setSmsId(null);
			selectMtr.setMtrId(null);

		} else {
			selectSem.setSmsId(null);
			selectMtr.setMtrId(null);
			System.out.println("No ha seleccionada una carrera: ");
		}
	}

	public void limpiarFiltros() {
		selectCrr.setCrrId(null);
		selectSem.setSmsId(null);
		selectMtr.setMtrId(null);
	}

	public String verDataHorario() {
		flagEditar = false;
		System.out.println("Metodo para ver informacion de horario");
		// lstHorarios = srvHor.listarHorarios(selectPar.getPrlId(),
		// selectMtr.getMtrId());
		lstHorarios = srvHor.listarHorarios(selectPar.getPrlCodigo());
		return "detalleHorario";
	}

	public String editarDataHorario() {
		flagEditar = true;
		System.out.println("Metodo para ver informacion de horario");
		// lstHorarios = srvHor.listarHorarios(selectPar.getPrlId(),
		// selectMtr.getMtrId());
		return "detalleHorario";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Horario [srvHor=" + srvHor + ", selectMtr=" + selectMtr + ", selectCrr=" + selectCrr + ", selectSem="
				+ selectSem + ", selectPar=" + selectPar + ", crrId=" + crrId + ", mtrId=" + mtrId + ", smsId=" + smsId
				+ ", lstC=" + lstC + ", lstM=" + lstM + ", lstMbySemestre=" + lstMbySemestre + ", lstS=" + lstS
				+ ", lstParalelos=" + lstParalelos + ", flagEditar=" + flagEditar + "]";
	}

	// Setters and getters

	public List<Materia> getLstM() {
		return lstM;
	}

	public Materia getSelectMtr() {
		return selectMtr;
	}

	public void setSelectMtr(Materia selectMtr) {
		this.selectMtr = selectMtr;
	}

	public void setLstM(List<Materia> lstM) {
		this.lstM = lstM;
	}

	public List<Carrera> getLstC() {
		return lstC;
	}

	public void setLstC(List<Carrera> lstC) {
		this.lstC = lstC;
	}

	public Carrera getSelectCrr() {
		return selectCrr;
	}

	public void setSelectCrr(Carrera selectCrr) {
		this.selectCrr = selectCrr;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
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
	 * The selectSem to get.
	 * 
	 * @return the selectSem
	 */
	public Semestre getSelectSem() {
		return selectSem;
	}

	/**
	 * The selectSem to set.
	 * 
	 * @param selectSem
	 */
	public void setSelectSem(Semestre selectSem) {
		this.selectSem = selectSem;
	}

	/**
	 * The lstS to get.
	 * 
	 * @return the lstS
	 */
	public List<Semestre> getLstS() {
		return lstS;
	}

	/**
	 * The lstS to set.
	 * 
	 * @param lstS
	 */
	public void setLstS(List<Semestre> lstS) {
		this.lstS = lstS;
	}

	/**
	 * The lstMbySemestre to get.
	 * 
	 * @return the lstMbySemestre
	 */
	public List<Materia> getLstMbySemestre() {
		return lstMbySemestre;
	}

	/**
	 * The lstMbySemestre to set.
	 * 
	 * @param lstMbySemestre
	 */
	public void setLstMbySemestre(List<Materia> lstMbySemestre) {
		this.lstMbySemestre = lstMbySemestre;
	}

	/**
	 * The mtrId to get.
	 * 
	 * @return the mtrId
	 */
	public Integer getMtrId() {
		return mtrId;
	}

	/**
	 * The mtrId to set.
	 * 
	 * @param mtrId
	 */
	public void setMtrId(Integer mtrId) {
		this.mtrId = mtrId;
	}

	/**
	 * The smsId to get.
	 * 
	 * @return the smsId
	 */
	public Integer getSmsId() {
		return smsId;
	}

	/**
	 * The smsId to set.
	 * 
	 * @param smsId
	 */
	public void setSmsId(Integer smsId) {
		this.smsId = smsId;
	}

	/**
	 * The lstParalelos to get.
	 * 
	 * @return the lstParalelos
	 */
	public List<Paralelo> getLstParalelos() {
		return lstParalelos;
	}

	/**
	 * The lstParalelos to set.
	 * 
	 * @param lstParalelos
	 */
	public void setLstParalelos(List<Paralelo> lstParalelos) {
		this.lstParalelos = lstParalelos;
	}

	/**
	 * The selectPar to get.
	 * 
	 * @return the selectPar
	 */
	public Paralelo getSelectPar() {
		return selectPar;
	}

	/**
	 * The selectPar to set.
	 * 
	 * @param selectPar
	 */
	public void setSelectPar(Paralelo selectPar) {
		this.selectPar = selectPar;
	}

	/**
	 * The flagEditar to get.
	 * 
	 * @return the flagEditar
	 */
	public Boolean getFlagEditar() {
		return flagEditar;
	}

	/**
	 * The flagEditar to set.
	 * 
	 * @param flagEditar
	 */
	public void setFlagEditar(Boolean flagEditar) {
		this.flagEditar = flagEditar;
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
