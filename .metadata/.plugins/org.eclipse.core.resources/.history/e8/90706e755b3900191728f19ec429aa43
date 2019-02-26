package servicios;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import model.Horario;
import model.TipoHorario;

@Local
public interface SrvReporteHorarioLocal {

	// List<Horario> listarHorarios(Integer fcdcId, Integer crrId);
	Collection<String[]> listarHorarios(Integer fcdcId, Integer crrId, Integer tphrId);

	Collection<TipoHorario> listarTipoHorario();

}
