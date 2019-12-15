package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import ec.edu.uce.biometrico.jpa.*;

@Local
public interface SrvDocenteLocal {

	/**
	 * Lista los parametros de holgura para el registro del control academico en
	 * la facultad
	 * 
	 * @param parametro
	 *            Nombre de la holgura del horario academico
	 * @param integer
	 *            Posicion de la holgura
	 * @return
	 */
	List<FichaDocente> listarDocentesxParametroxFacultad(String parametro, Integer integer);

	/**
	 * Metodo que obtiene los diferentes tipos de huellas de los dedos a
	 * registrar del docente
	 * 
	 * @return Tipos de huellas para selecccionar
	 */
	List<TipoHuella> listarTipoHuellas();

	/**
	 * Metodo que obtiene la lista de huellas del docente
	 * 
	 * @param fcdc_id
	 *            del Docente
	 * @return huellas del docente
	 * @throws SQLException
	 * @throws IOException
	 */
	List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException;

	/**
	 * Metodo para guardar un nuevo registro de huella dactilar
	 * 
	 * @param bimg1
	 *            buffer de primera huella
	 * @param bimg2
	 *            buffer de segunda huella
	 * @param fcdc
	 *            del docente
	 * @param tphl
	 *            del tipo huella
	 * @throws IOException
	 * @throws SerialException
	 * @throws SQLException
	 */
	void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException;

	/**
	 * Metodo que obtiene asistencia en la hora de registro academico
	 * 
	 * @param id
	 *            del docente
	 * @param inicio
	 *            fecha inicio del rango
	 * @param fin
	 *            fecha fin del rango
	 * @param crrId
	 *            de la Carrera
	 * @return
	 */
	List<Asistencia> listarAsistencia(Integer id, Date inicio, Date fin, Integer crrId);

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

	/**
	 * Obtiene las actividades microcurricualres de la materia y docente
	 * 
	 * @param fcdcId
	 *            de Docente
	 * @param mtrId
	 *            de Materia
	 * @return
	 */
	List<Seguimiento> listarSeguimientosxDocenteMateria(Integer fcdcId, Integer mtrId);

	/**
	 * Guarda opciones de registro movil o registro mediante login
	 * 
	 * @param selectDcnt
	 *            Docente a enrolar
	 * @param selectTp
	 *            Tipo de huella del dedo a registrar
	 * @param flagMovil
	 *            Estado de registro movil
	 * @param flagSinHuella
	 *            Estado de registro x login
	 */
	void guardarActualizarEstados(FichaDocente selectDcnt, TipoHuella selectTp, boolean flagMovil,
			boolean flagSinHuella);

	/**
	 * Obtiene estados de registro movil o registro movil
	 * 
	 * @param fcdcId
	 *            del Docente
	 * @return Lista de estados
	 */
	List<Boolean> listarestados(Integer fcdcId);

	/**
	 * Verifica si tiene permisos de registro mediante login
	 * 
	 * @param regDcnt
	 *            Docente a verificar
	 * @return estado del permiso
	 */
	boolean verificarLogin(FichaDocente regDcnt);

	/**
	 * Obtiene una lista de materias de una carrera al que pertenece el docente
	 * 
	 * @param fcdcId
	 *            del Docente
	 * @param crrId
	 *            de la Carrera
	 * @return materias x carrera del docente
	 */
	List<Materia> listarMateriasxCarrera(Integer fcdcId, Integer crrId);

	/**
	 * Permite obtener las asistencias de los docentes por facultad y enviar al
	 * mail cada mes.
	 * 
	 * @param fclId
	 * @return
	 */
	List<Asistencia> getAsistenciasReporte(Integer fclId, Date inicio, Date fin);
}
