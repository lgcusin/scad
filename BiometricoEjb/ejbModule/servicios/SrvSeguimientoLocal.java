package servicios;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import model.Actividad;
import model.Asistencia;
import model.Aula;
import model.Bibliografia;
import model.Carrera;
import model.Contenido;
import model.Herramienta;
import model.Horario;
import model.MallaCurricularMateria;
import model.Materia;
import model.Metodologia;
import model.Syllabo;
import model.UnidadCurricular;

@Local
public interface SrvSeguimientoLocal {

	List<Carrera> listarAllCrrByFcdc(Integer fdcId);

	List<Materia> listarAllMatByFcdc(Integer fdcID);

	List<Materia> listarMatByCrr(Integer id);

	Carrera getCarrera(Integer crrId);

	Syllabo getSyllabus(Integer mtrId);

	List<UnidadCurricular> listarUnidadCurricular(Integer mtrId);

	List<Contenido> listarContenidos(Integer uncrId);

	List<Actividad> listarActividades(Integer cntId);

	List<Herramienta> listarHerramientas(Integer cntId);

	Horario verificarHorario(Date fecha, Integer fcdcId, Boolean tipo, Integer fclId);

	Materia getMateria(Integer hrrId);

	Aula getAula(Integer hrrId);

	List<Contenido> getContenidos(Integer mtrId);

	List<Asistencia> marcacionReg(Date fecha, Integer fcdcId);

	void guardarRegistro(Asistencia regAss);

	void generar(Date ahora);

	void guardarSeguimiento(Contenido contenido);

	void guardarActualizarContenido(Contenido cnt);

	Syllabo setSyllabus(MallaCurricularMateria mllCrrMateria);
	
	void guardarActualizarUnidad(UnidadCurricular uncr);

	MallaCurricularMateria getMallaCurricularMateria(Integer mtrId);

	void guardarActualizarSyllabus(Syllabo syl);

	void guardarActualizarActividad(Actividad actividad);

	void guardarActualizarHerramienta(Herramienta herramienta);

	void guardarActualizarMetodologia(Metodologia metodologia);

	void guardarActualizarBibliografia(Bibliografia bibliografia);

	List<Metodologia> listarMetodologias(Integer uncrId);

	List<Bibliografia> listarBibliografias(Integer uncrId);

	void eliminarUnidad(UnidadCurricular uc);

	

}
