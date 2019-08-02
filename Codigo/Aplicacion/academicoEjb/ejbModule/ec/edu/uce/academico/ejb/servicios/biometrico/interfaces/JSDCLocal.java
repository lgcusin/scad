package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;

@Local
public interface JSDCLocal {

	boolean inicializar();

	void onLED();

	BufferedImage capturar();

	int calidad();

	boolean verificar();

	void cerrar();

	void configurar();

	FichaDocente comparar() throws SQLException, IOException;

}
