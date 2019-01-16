package managedBeans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lector.JSDC;
import lector.JSDCLocal;

@ManagedBean(name = "lector")
@ViewScoped
public class LectorHuella {

	@EJB
	private JSDCLocal jsdcBean;
	private JSDC jsdc;

	private ImageIcon graphic1;
	private ImageIcon graphic2;
	private ImageIcon graphic3;

	@PostConstruct
	public void init() {
		jsdc = new JSDC();
	}

	public String initDevice() {
		jsdcBean.inicializar();
		return null;
	}

	public String encenderLed() {
		jsdcBean.onLED();
		return null;
	}

	public String capturar1() {
		BufferedImage bufferedImg = jsdcBean.capturar1();
		graphic1 = new ImageIcon(bufferedImg);
		return null;
	}

	public String capturar2() {
		BufferedImage bufferedImg = jsdcBean.capturar2();
		graphic2 = new ImageIcon(bufferedImg);
		return "prueba";
	}

	public String capturar3() {
		BufferedImage bufferedImg = jsdcBean.capturar3();
		graphic3 = new ImageIcon(bufferedImg);
		return "prueba";
	}

	public String verificar() {
		boolean ver = true;
		ver = jsdcBean.verificar();
		if (ver) {
			return "registro";
		} else {
			System.out.println("Huella no ingresada o registrada");
		}
		return null;
	}

	public void registrar() {

	}

	// setters and getters
	public ImageIcon getGraphic1() {
		return graphic1;
	}

	public void setGraphic1(ImageIcon graphic1) {
		this.graphic1 = graphic1;
	}

	public ImageIcon getGraphic2() {
		return graphic2;
	}

	public void setGraphic2(ImageIcon graphic2) {
		this.graphic2 = graphic2;
	}

	public ImageIcon getGraphic3() {
		return graphic3;
	}

	public void setGraphic3(ImageIcon graphic3) {
		this.graphic3 = graphic3;
	}

	// setters and getters

}
