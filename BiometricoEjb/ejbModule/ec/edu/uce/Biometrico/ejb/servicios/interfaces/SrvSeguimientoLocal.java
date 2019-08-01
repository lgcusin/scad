package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.uce.edu.biometrico.jpa.Actividad;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Aula;
import ec.uce.edu.biometrico.jpa.Bibliografia;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.Herramienta;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.MallaCurricularMateria;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Metodologia;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.Syllabo;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

@Local
public interface SrvSeguimientoLocal {

	List<Carrera> listarAllCrrByFcdc(Integer fdcId);

	List<Materia> listarAllMatByFcdc(Integer fdcID);

	List<Materia> listarMatByCrr(Integer id);

	Carrera getCarrera(Integer crrId);

	Syllabo getSyllabus(Integer mtrId);

	List<UnidadCurricular> listarUnidadCurricular(Integer mtrId);

	List<ContenidoCurricular> listarContenidos(Integer uncrId);

	List<Actividad> listarActividades(Integer cntId);

	List<Herramienta> listarHerramientas(Integer cntId);

	HorarioAcademico verificarHorario(Date fecha, Integer fcdcId, boolean b, Integer fclId, Integer tipo);

	Materia getMateria(Integer hrrId);

	Aula getAula(Integer hrrId);

	List<ContenidoCurricular> buscarContenidos(Integer mtrId);

	List<Asistencia> marcacionReg(Date fecha, Integer fcdcId);

	void guardarRegistro(Asistencia regAss);

	boolean generar(Date ahora, Integer fclId);

	void guardarActualizarSeguimiento(Seguimiento seguimiento);

	void guardarActualizarContenido(ContenidoCurricular cnt);

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

	List<Seguimiento> getSeguimiento(Integer mtrId, Integer fcdcId);

	void generarAsistencias(HorarioAcademico hrac, FichaDocente docente, Date date);

	String obtenerHoraClasexHorario(HorarioAcademico hrrI);

	

}