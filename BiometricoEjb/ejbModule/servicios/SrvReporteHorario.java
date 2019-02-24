package servicios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Horario;
import model.TipoHorario;

/**
 * Session Bean implementation class SrvReporteHorario
 */
@Stateless
@LocalBean
public class SrvReporteHorario implements SrvReporteHorarioLocal {

	@PersistenceContext
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvReporteHorario() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	// public List<Horario> listarHorarios(Integer fcdcId, Integer crrId) {
	public Collection<String[]> listarHorarios(Integer fcdcId, Integer crrId, Integer tphrId) {
		List<Horario> lstHorario = new ArrayList<>();
		Collection<String[]> resultHorarios = new ArrayList<>();
		try {
			Query query = em.createQuery("select h,m,ds from Horario as h join h.materia as m join h.diaSemana as ds"
					+ " where h.fichaDocente.fcdcId=:fcdcId and m.carrera.crrId=:crrId and h.tipoHorario.tphrId=:tphrId order by h.hrrInicio asc");
			query.setParameter("fcdcId", fcdcId);
			query.setParameter("crrId", crrId);
			query.setParameter("tphrId", tphrId);
			for (Object obj : query.getResultList()) {
				Object[] objArray = (Object[]) obj;
				lstHorario.add((Horario) objArray[0]);
			}
			System.out.println("Valores de la lista");

			for (Horario h : lstHorario) {
				String[] numeros = new String[7];
				if (h.getHrrInicio() != null && h.getHrrFin() != null) {
					numeros[0] = h.getHrrInicio() + " " + h.getHrrFin();
				}
				if (h.getDiaSemana().getDsmId() == 1) {
					numeros[1] = h.getMateria().getMtrNombre();
				}
				if (h.getDiaSemana().getDsmId() == 2) {
					numeros[2] = h.getMateria().getMtrNombre();
				}
				if (h.getDiaSemana().getDsmId() == 3) {
					numeros[3] = h.getMateria().getMtrNombre();
				}
				if (h.getDiaSemana().getDsmId() == 4) {
					numeros[4] = h.getMateria().getMtrNombre();
				}
				if (h.getDiaSemana().getDsmId() == 5) {
					numeros[5] = h.getMateria().getMtrNombre();
				}
				if (h.getDiaSemana().getDsmId() == 6) {
					numeros[6] = h.getMateria().getMtrNombre();
				}
				resultHorarios.add(numeros);
			}
			System.out.println("datos del result: " + resultHorarios.size());
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios" + e);
		}
		// return lstHorario;
		return resultHorarios;
	}

	@Override
	public Collection<TipoHorario> listarTipoHorario() {
		Collection<TipoHorario> lstTipoHorario = null;
		try {
			Query query = em.createQuery("select th from TipoHorario as th");
			System.out.println("Valores de la lista");
			lstTipoHorario = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar los tipos horarios" + e);
		}
		return lstTipoHorario;
	}
}
