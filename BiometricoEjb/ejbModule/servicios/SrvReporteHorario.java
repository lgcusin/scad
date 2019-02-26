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
	public Collection<String[]> listarHorarios(Integer fcdcId, Integer crrId, Integer tphrId) {
		List<Horario> lstHorario = new ArrayList<>();
		Collection<String[]> resultHorarios = new ArrayList<>();
		try {
			Collection<String[]> auxResult = new ArrayList<>();
			Query query;
			if (fcdcId != null) {
				query = em.createQuery("select h,m,ds from Horario as h join h.materia as m join h.diaSemana as ds"
						+ " where h.fichaDocente.fcdcId=:fcdcId and m.carrera.crrId=:crrId and h.tipoHorario.tphrId=:tphrId order by h.hrrInicio asc");
				query.setParameter("fcdcId", fcdcId);
			} else {
				query = em.createQuery("select h,m,ds from Horario as h join h.materia as m join h.diaSemana as ds "
						+ "where m.carrera.crrId=:crrId and h.tipoHorario.tphrId=:tphrId order by h.hrrInicio asc");
			}
			query.setParameter("crrId", crrId);
			query.setParameter("tphrId", tphrId);
			for (Object obj : query.getResultList()) {
				Object[] objArray = (Object[]) obj;
				lstHorario.add((Horario) objArray[0]);
			}
			System.out.println("Valores de la lista");

			getHorariosMaterias(lstHorario, resultHorarios);

			eliminarDuplicados(resultHorarios, auxResult);
			resultHorarios = auxResult;
			System.out.println("datos del result: " + resultHorarios.size());
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios" + e);
		}
		return resultHorarios;
	}

	/**
	 * @param lstHorario
	 * @param resultHorarios
	 */
	private void getHorariosMaterias(List<Horario> lstHorario, Collection<String[]> resultHorarios) {
		for (Horario h : lstHorario) {
			List<Horario> auxMatDia = new ArrayList<>();
			for (Horario h2 : lstHorario) {
				if (h.getHrrInicio().equals(h2.getHrrInicio()) && h.getHrrFin().equals(h2.getHrrFin())) {
					auxMatDia.add(h2);
				}
			}
			String[] columnaReporte = new String[7];
			if (auxMatDia.size() > 1) {
				for (Horario hAux : auxMatDia) {
					validarMateriasPorDia(hAux, columnaReporte);
				}
			} else {
				validarMateriasPorDia(h, columnaReporte);
			}
			resultHorarios.add(columnaReporte);
		}
	}

	/**
	 * Metodo que elimina los registros duplicados del horario
	 * 
	 * @param resultHorarios
	 * @param auxResult
	 */
	private void eliminarDuplicados(Collection<String[]> resultHorarios, Collection<String[]> auxResult) {
		for (String[] hor1 : resultHorarios) {
			boolean agregar = false;
			for (String[] strings : auxResult) {
				Object[] obj1 = (Object[]) strings;
				Object[] obj2 = (Object[]) hor1;
				if (obj1[0].equals(obj2[0])) {
					agregar = false;
					break;
				} else {
					agregar = true;
				}
			}
			if (agregar) {
				auxResult.add(hor1);
			} else {
				if (auxResult.size() == 0) {
					auxResult.add(hor1);
				}
			}
		}
	}

	/**
	 * Metodo que valida las materia por dia en el horario
	 * 
	 * @param h
	 * @param columnaReporte
	 */
	private void validarMateriasPorDia(Horario h, String[] columnaReporte) {
		if (h.getHrrInicio() != null && h.getHrrFin() != null) {
			columnaReporte[0] = h.getHrrInicio() + " - " + h.getHrrFin();
		}
		if (h.getDiaSemana().getDsmId() == 1) {
			columnaReporte[1] = h.getMateria().getMtrNombre();
		}
		if (h.getDiaSemana().getDsmId() == 2) {
			columnaReporte[2] = h.getMateria().getMtrNombre();
		}
		if (h.getDiaSemana().getDsmId() == 3) {
			columnaReporte[3] = h.getMateria().getMtrNombre();
		}
		if (h.getDiaSemana().getDsmId() == 4) {
			columnaReporte[4] = h.getMateria().getMtrNombre();
		}
		if (h.getDiaSemana().getDsmId() == 5) {
			columnaReporte[5] = h.getMateria().getMtrNombre();
		}
		if (h.getDiaSemana().getDsmId() == 6) {
			columnaReporte[6] = h.getMateria().getMtrNombre();
		}
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
