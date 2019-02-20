package lector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import model.FichaDocente;
import model.Horario;

@Local
public interface JSDCLocal {

	void inicializar();

	void onLED();

	BufferedImage capturar();

	int calidad();

	boolean verificar();

	void cerrar();

	void configurar();

	FichaDocente comparar() throws SQLException, IOException;

}
