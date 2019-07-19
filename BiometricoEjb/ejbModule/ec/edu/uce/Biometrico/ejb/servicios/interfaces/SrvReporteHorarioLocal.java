package ec.edu.uce.Biometrico.ejb.servicios.interfaces;

import java.util.Collection;
import javax.ejb.Local;
import ec.uce.edu.biometrico.jpa.TipoHorario;

@Local
public interface SrvReporteHorarioLocal {

	// List<Horario> listarHorarios(Integer fcdcId, Integer crrId);
	Collection<String[]> listarHorarios(Integer fcdcId, Integer crrId, Integer tphrId);

	Collection<TipoHorario> listarTipoHorario();

}
