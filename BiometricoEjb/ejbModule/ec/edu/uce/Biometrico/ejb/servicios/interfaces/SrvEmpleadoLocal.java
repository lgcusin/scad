package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.List;

import javax.ejb.Local;

import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

@Local
public interface SrvEmpleadoLocal {

	List<Carrera> listarCarreras(Integer fdId);

	List<FichaDocente> listarDocentes(Integer crrId);

	List<HorarioAcademico> listarHorariosxDocentexFechaHora(Integer integer, Integer[] arrayHora, int dia, Integer crrId);

	List<FichaDocente> listarDocentesxCarrera(Integer crrId);

	List<Carrera> listarCarrerasxFacultad(Integer dpnId);

	List<Seguimiento> listarSeguimientosxDocente(Integer fcdcId);

}
