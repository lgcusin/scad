package lector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

@Local
public interface JSDCLocal {

	public void inicializar();

	public void onLED();

	public BufferedImage capturar();

	public int calidad();

	public boolean verificar();

	public void cerrar();

	public void configurar();

}
