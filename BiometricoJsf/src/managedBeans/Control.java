package managedBeans;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

import lector.JSDCLocal;
import model.Asistencia;
import model.Aula;
import model.Contenido;
import model.FichaDocente;
import model.Horario;
import model.Materia;
import servicios.SrvSeguimiento;
import servicios.SrvSeguimientoLocal;

@ManagedBean(name = "control")
@ViewScoped
public class Control {

	@EJB
	private JSDCLocal srvJsdc;
	@EJB
	private SrvSeguimientoLocal srvSgmt;

	private FichaDocente regDcnt;
	private Horario hrr;
	private Materia mtr;
	private Aula aul;
	private Asistencia regAss;
	private List<Contenido> lstCnt;
	private List<String> selecCnts;

	public String hora;
	public Date fechahora;
	public Date ahora;
	public SimpleDateFormat formateador;

	@PostConstruct
	public void init() {
		srvJsdc.inicializar();
		regDcnt = new FichaDocente();
		fechahora = new Date();

	}

	public void temporizador() {
		ahora = new Date();
		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
	}

	public void registrar() throws SQLException, IOException {

		regDcnt = srvJsdc.comparar();
		if (regDcnt != null) {
			hrr = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), true);
			if (hrr != null) {
				regAss = srvSgmt.marcacionReg(ahora, regDcnt.getFcdcId());
				if (regAss == null) {
					regAss = new Asistencia();
					regAss.setAssId(10);
					formateador = new SimpleDateFormat("dd/MM/yyyy");
					regAss.setAssFecha(new Date(formateador.format(ahora)));
					formateador = new SimpleDateFormat("HH:mm:ss");
					regAss.setAssHoraEntrada(formateador.format(ahora));
					regAss.setAssEstado("Iniciado");
					mtr = srvSgmt.getMateria(hrr.getHrrId());
					aul = srvSgmt.getAula(hrr.getHrrId());
					hrr.setMateria(mtr);
					hrr.setAula(aul);
					lstCnt = srvSgmt.getContenidos(mtr.getMtrId());
				} else {
					if (regAss.getAssHoraEntrada() != null && regAss.getAssEstado().equals("Iniciado")) {
						hrr = srvSgmt.verificarHorario(ahora, regDcnt.getFcdcId(), false);
						if (hrr != null) {
							regAss = new Asistencia();
							regAss.setAssId(20);
							formateador = new SimpleDateFormat("dd/MM/yyyy");
							regAss.setAssFecha(new Date(formateador.format(ahora)));
							formateador = new SimpleDateFormat("HH:mm:ss");
							regAss.setAssHoraSalida(formateador.format(ahora));
							regAss.setAssEstado("Finalizado");
							mtr = srvSgmt.getMateria(hrr.getHrrId());
							aul = srvSgmt.getAula(hrr.getHrrId());
							hrr.setMateria(mtr);
							hrr.setAula(aul);
							lstCnt = srvSgmt.getContenidos(mtr.getMtrId());
						} else {
							System.out.println("Esta fuera del horario de salida!!!!!!!!!!!!!!!!!");
						}

					} else {
						System.out.println("Sin hora de entrada, xfavor justifique su atraso!!!!");
					}
				}
			} else {
				System.out.println("Esta fuera del horario de entrada!!!!!!!!!!!!!!!!!");
			}
		}

	}

	public void guardarAsis() {
		srvSgmt.guardarRegistro(regAss);
	}

	public void cerrarDiag() {
		selecCnts = null;
		lstCnt = null;
		regAss = null;
	}

	// setters and getters

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public FichaDocente getRegDcnt() {
		return regDcnt;
	}

	public void setRegDcnt(FichaDocente regDcnt) {
		this.regDcnt = regDcnt;
	}

	public Horario getHrr() {
		return hrr;
	}

	public void setHrr(Horario hrr) {
		this.hrr = hrr;
	}

	public List<Contenido> getLstCnt() {
		return lstCnt;
	}

	public void setLstCnt(List<Contenido> lstCnt) {
		this.lstCnt = lstCnt;
	}

	public List<String> getSelecCnts() {
		return selecCnts;
	}

	public void setSelecCnts(List<String> selecCnts) {
		this.selecCnts = selecCnts;
	}

	public Asistencia getRegAss() {
		return regAss;
	}

	public void setRegAss(Asistencia regAss) {
		this.regAss = regAss;
	}

}
