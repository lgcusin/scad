package servicios;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import model.Actividad;
import model.Asistencia;
import model.FichaDocente;
import model.MallaCurricularMateria;
import model.TipoHuella;

@Local
public interface SrvDocenteLocal {

	FichaDocente getDocente(Integer id);

	List<FichaDocente> listar(String parametro);

	List<TipoHuella> listarDedos();

	List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException;

	void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException;

	List<Asistencia> listarAsistencia(Integer id);
	
	List<String> listarActividades(Integer id);
}
