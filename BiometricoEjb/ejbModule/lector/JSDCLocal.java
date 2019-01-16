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

	public BufferedImage capturar1() throws SerialException, IOException, SQLException;

	public BufferedImage capturar2();

	public BufferedImage capturar3();

	public boolean verificar();
}
