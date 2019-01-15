package lector;

import java.awt.image.BufferedImage;

import javax.ejb.Local;

@Local
public interface JSDCLocal {

	public void inicializar();

	public void onLED();

	public BufferedImage capturar1();

	public BufferedImage capturar2();

	public BufferedImage capturar3();

	public boolean verificar();
}
