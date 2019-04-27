package servicios;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import model.Facultad;
import model.Feriado;

@Local
public interface SrvRegistroFeriadoLocal {

	Collection<Feriado> listarFeriados(Integer fclId, Date fechaInicio, Date fechaFin);

	void guardarActualizarFeriado(Feriado feriado);

	void eliminarFeriado(Feriado feriado);

	List<Facultad> listarFacultades();
}
