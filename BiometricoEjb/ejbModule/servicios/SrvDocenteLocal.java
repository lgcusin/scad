package servicios;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import model.Asistencia;
import model.Contenido;
import model.FichaDocente;
import model.Horario;
import model.TipoHuella;

@Local
public interface SrvDocenteLocal {

	List<FichaDocente> listar(String parametro);

	List<TipoHuella> listarDedos();

	List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException;

	void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException;

	List<Asistencia> listarAsistencia(Integer id, Date inicio, Date fin, Integer crrId);

	List<String> listarActividades(Integer id);

	List<Contenido> listarContenidos(Integer fdId);

	/**
	 * Permite buscar el horario asignado a la asistencia a justificar
	 * 
	 * @param assId
	 * @return
	 */
	Horario findHorarioByAsistencia(Integer assId);

	/**
	 * Actualiza la asistencia justificada
	 * 
	 * @param asistencia
	 */
	void actualizarAsistencia(Asistencia asistencia);
}
