package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.TipoHuella;

@Local
public interface SrvDocenteLocal {

	List<FichaDocente> listarDocentesxParametroxFacultad(String parametro, Integer integer);

	List<TipoHuella> listarTipoHuellas();

	List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException;

	void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException;

	List<Asistencia> listarAsistencia(Integer id, Date inicio, Date fin, Integer crrId);

	List<String> listarActividades(Integer id);

	List<ContenidoCurricular> listarContenidos(Integer fdId);

	/**
	 * Permite buscar el horario asignado a la asistencia a justificar
	 * 
	 * @param asistencia
	 * @return
	 */
	HorarioAcademico findHorarioByAsistencia(Asistencia asistencia);

	/**
	 * Actualiza la asistencia justificada
	 * 
	 * @param asistencia
	 */
	void actualizarAsistencia(Asistencia asistencia);


	List<Seguimiento> listarSeguimientosxDocenteMateria(Integer fcdcId, Integer mtrId);

	void guardarActualizarEstados(FichaDocente selectDcnt, TipoHuella selectTp, boolean flagMovil, boolean flagSinHuella);

	List<Boolean> listarestados(Integer fcdcId);

	boolean verificarLogin(FichaDocente regDcnt);

	List<Materia> listarMateriasxCarrera(Integer fcdcId,Integer crrId);
}