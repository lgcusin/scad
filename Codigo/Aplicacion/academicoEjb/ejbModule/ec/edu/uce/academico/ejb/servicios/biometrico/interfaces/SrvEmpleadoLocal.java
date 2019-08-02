package ec.edu.uce.academico.ejb.servicios.biometrico.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.academico.jpa.entidades.publico.HorarioAcademico;
import ec.edu.uce.academico.jpa.entidades.publico.Seguimiento;

@Local
public interface SrvEmpleadoLocal {

	List<Carrera> listarCarreras(Integer fdId);

	List<FichaDocente> listarDocentes(Integer crrId);

	List<HorarioAcademico> listarHorariosxDocentexFechaHora(Integer integer, Integer[] arrayHora, int dia,
			Integer crrId);

	List<FichaDocente> listarDocentesxCarrera(Integer crrId);

	List<Carrera> listarCarrerasxFacultad(Integer dpnId);

	List<Seguimiento> listarSeguimientosxDocente(Integer fcdcId);

}
