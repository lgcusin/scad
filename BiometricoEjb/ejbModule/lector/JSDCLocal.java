package lector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

@Local
public interface JSDCLocal {

	void inicializar();

	void onLED();

	BufferedImage capturar();

	int calidad();

	boolean verificar();

	void cerrar();

	void configurar();

	boolean comparar(BufferedImage img) throws SQLException, IOException;
}
