package managedBeans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import beans.DocenteBeanLocal;
import lector.JSDCLocal;
import model.FichaDocente;
import model.HuellaDactilar;
import model.TipoHuella;

@ManagedBean(name = "registro")
@SessionScoped
public class Registro {

	// Interfaz de servicio
	@EJB
	private DocenteBeanLocal fcdc;
	@EJB
	private JSDCLocal jsdcBean;

	// Objeto
	private FichaDocente selectDcnt;

	// Listas de objetos
	public List<TipoHuella> lstTphl;
	public List<FichaDocente> lstDcnt;
	public List<StreamedContent> lstImg;
	public List<BufferedImage> lstHll;

	// Entradas y salidas
	public String parametro;
	public StreamedContent graphic1;
	public StreamedContent graphic2;
	public StreamedContent graphic3;
	public StreamedContent graphic4;
	public StreamedContent graphic5;
	public StreamedContent graphic6;
	public int quality1;
	public int quality2;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println(context.getExternalContext().getRequestContextPath());
		selectDcnt = new FichaDocente();
		lstDcnt = fcdc.listar("  ");
		lstTphl = fcdc.listarDedos();
	}

	// ####### Pagina busqueda #######
	public void listar() {
		lstDcnt = fcdc.listar(parametro);
	}

	// ####### Panel de huellas #######
	public void encender() throws IOException, SQLException {
		jsdcBean.inicializar();
		jsdcBean.onLED();
		lstHll = fcdc.listarHuellas(selectDcnt.getFcdcId());
		for (BufferedImage bimg : lstHll) {
			lstImg.add(toImage(bimg));
		}
		graphic1 = lstImg.get(1);
		graphic2 = lstImg.get(2);
		graphic3 = lstImg.get(3);
		graphic4 = lstImg.get(4);
		graphic5 = lstImg.get(5);
		graphic6 = lstImg.get(6);
	}

	public void apagar() {
		jsdcBean.cerrar();
	}

	public StreamedContent toImage(BufferedImage bimg) throws IOException {
		ByteArrayOutputStream baos = null;
		StreamedContent grafico = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(bimg, "png", baos);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				baos.close();
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
		grafico = new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()));
		return grafico;
	}

	public void capturar1() throws IOException {
		BufferedImage bimg = jsdcBean.capturar();
		quality1 = jsdcBean.calidad();
		graphic1 = toImage(bimg);
	}

	public void capturar2() throws IOException {
		BufferedImage bimg = jsdcBean.capturar();
		quality2 = jsdcBean.calidad();
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(bimg, "png", baos);
		} finally {
			try {
				baos.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		graphic2 = new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()));
	}

	public void verificar() {
		FacesContext context = FacesContext.getCurrentInstance();

		boolean verificado = false;
		verificado = jsdcBean.verificar();
		if (verificado) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Huella", "Aceptada");

			context.addMessage("botonVerificar", facesMsg);
		} else {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Huella", "Erronea");

			context.addMessage("botonVerificar", facesMsg);
		}
	}

	public void registrar() {

	}

	// ####### Setters y Getters Busqueda #######

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public List<FichaDocente> getLstDcnt() {
		return lstDcnt;
	}

	public void setLstDcnt(List<FichaDocente> lstDcnt) {
		this.lstDcnt = lstDcnt;
	}

	public FichaDocente getSelectDcnt() {
		return selectDcnt;
	}

	public void setSelectDcnt(FichaDocente selectDcnt) {
		this.selectDcnt = selectDcnt;
	}
	// ####### Setters y Getters Tipo Huella #######

	public List<TipoHuella> getLstTphl() {
		return lstTphl;
	}

	public void setLstTphl(List<TipoHuella> lstTphl) {
		this.lstTphl = lstTphl;
	}

	// ####### Setters y Getters Panel Huella #######
	public StreamedContent getGraphic1() {
		return graphic1;
	}

	public void setGraphic1(StreamedContent graphic1) {
		this.graphic1 = graphic1;
	}

	public StreamedContent getGraphic2() {
		return graphic2;
	}

	public void setGraphic2(StreamedContent graphic2) {
		this.graphic2 = graphic2;
	}

	public int getQuality1() {
		return quality1;
	}

	public void setQuality1(int quality1) {
		this.quality1 = quality1;
	}

	public int getQuality2() {
		return quality2;
	}

	public void setQuality2(int quality2) {
		this.quality2 = quality2;
	}

}
