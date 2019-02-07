package managedBeans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialException;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import servicios.SrvDocenteLocal;
import lector.JSDCLocal;
import model.FichaDocente;
import model.TipoHuella;

@ManagedBean(name = "huella")
@ViewScoped
public class Huellas {

	// Interfaz de servicio
	@EJB
	private SrvDocenteLocal srvDcnt;
	@EJB
	private JSDCLocal jsdcBean;

	private FichaDocente selectDcnt;

	private Integer tpId;
	private TipoHuella selectTp;

	private List<TipoHuella> lstTphl;

	private StreamedContent graphic1;
	private StreamedContent graphic2;
	private StreamedContent image;
	private BufferedImage bimg1;
	private BufferedImage bimg2;
	private int quality1;
	private int quality2;
	private boolean verificado;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		Registro beanDcnt = context.getApplication().evaluateExpressionGet(context, "#{registro}", Registro.class);
		jsdcBean.inicializar();
		jsdcBean.onLED();
		selectDcnt = beanDcnt.getSelectDcnt();
		lstTphl = srvDcnt.listarDedos();
		verificado = true;

	}

	// ####### Panel de huellas #######
	public void resetear() {
		InputStream stream = this.getClass().getResourceAsStream("/img/direccion.png");
		image = new DefaultStreamedContent(stream, "image/png");
		this.graphic1 = image;
		this.graphic2 = image;
	}

	public void apagar() {
		jsdcBean.cerrar();
		bimg1 = null;
		bimg2 = null;
		lstTphl = null;

	}

	public void capturar1() throws IOException {
		bimg1 = jsdcBean.capturar();
		quality1 = jsdcBean.calidad();
		graphic1 = toImage(bimg1);
	}

	public void capturar2() throws IOException {
		bimg2 = jsdcBean.capturar();
		quality2 = jsdcBean.calidad();
		graphic2 = toImage(bimg2);
	}

	public void verificar() throws SerialException, IOException, SQLException {
		verificado = !jsdcBean.verificar();
	}

	public void registrar() throws SerialException, IOException, SQLException {
		if (tpId != null) {
			selectTp = new TipoHuella();
			selectTp.setTphlId(tpId);
			srvDcnt.guardarImagen(bimg1, bimg2, selectDcnt, selectTp);
		} else {
			System.out.println("#####ESCOJA LA HUELLA#####");
		}

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

	// ####### Setters y Getters Tipo Huella #######

	public List<TipoHuella> getLstTphl() {
		return lstTphl;
	}

	public void setLstTphl(List<TipoHuella> lstTphl) {
		this.lstTphl = lstTphl;
	}

	// ####### Setters y Getters Panel Huella #######

	public Integer getTpId() {
		return tpId;
	}

	public void setTpId(Integer tpId) {
		this.tpId = tpId;
	}

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

	public boolean isVerificado() {
		return verificado;
	}

	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}

}