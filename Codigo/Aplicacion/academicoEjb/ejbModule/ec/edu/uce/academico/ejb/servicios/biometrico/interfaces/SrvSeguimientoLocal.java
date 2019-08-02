package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.Actividad;
import ec.edu.uce.academico.jpa.entidades.publico.Asistencia;
import ec.edu.uce.academico.jpa.entidades.publico.Aula;
import ec.edu.uce.academico.jpa.entidades.publico.Bibliografia;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.ContenidoCurricular;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.academico.jpa.entidades.publico.Herramienta;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.MallaCurricularMateria;
import ec.edu.uce.academico.jpa.entidades.publico.Materia;
import ec.edu.uce.academico.jpa.entidades.publico.Metodologia;
import ec.edu.uce.academico.jpa.entidades.publico.Seguimiento;
import ec.edu.uce.academico.jpa.entidades.publico.Syllabo;
import ec.edu.uce.academico.jpa.entidades.publico.UnidadCurricular;

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
