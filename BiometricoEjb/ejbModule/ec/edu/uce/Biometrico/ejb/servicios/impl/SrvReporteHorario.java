package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvReporteHorarioLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.biometrico.jpa.HorarioAcademicoExamen;


/**
 * Session Bean implementation class SrvReporteHorario
 */
@Stateless
@Local
public class SrvReporteHorario implements SrvReporteHorarioLocal {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvReporteHorario() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<String[]> listarHorarios(Integer fcdcId, Integer crrId, Integer tphrId) {
		List<HorarioAcademicoExamen> lstHorario = new ArrayList<>();
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
				lstHorario.add((HorarioAcademicoExamen) objArray[0]);
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

	private void getHorariosMaterias(List<HorarioAcademicoExamen> lstHorario, Collection<String[]> resultHorarios) {
		for (HorarioAcademicoExamen h : lstHorario) {
			List<HorarioAcademicoExamen> auxMatDia = new ArrayList<>();
			for (HorarioAcademicoExamen h2 : lstHorario) {
				if (h.getHracexHoraInicio().equals(h2.getHracexHoraInicio())
						&& h.getHracexHoraFin().equals(h2.getHracexHoraFin())) {
					auxMatDia.add(h2);
				}
			}
			String[] columnaReporte = new String[7];
			if (auxMatDia.size() > 1) {
				for (HorarioAcademicoExamen hAux : auxMatDia) {
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

	private void validarMateriasPorDia(HorarioAcademicoExamen h, String[] columnaReporte) {
		if (h.getHracexHoraInicio() != null && h.getHracexHoraFin() != null) {
			columnaReporte[0] = h.getHracexHoraInicio() + " - " + h.getHracexHoraFin();
		}
		// if (h.getDiaSemana().getDsmId() == 1) {
		// columnaReporte[1] = h.getMateria().getMtrDescripcion();
		// }
		// if (h.getDiaSemana().getDsmId() == 2) {
		// columnaReporte[2] = h.getMateria().getMtrDescripcion();
		// }
		// if (h.getDiaSemana().getDsmId() == 3) {
		// columnaReporte[3] = h.getMateria().getMtrDescripcion();
		// }
		// if (h.getDiaSemana().getDsmId() == 4) {
		// columnaReporte[4] = h.getMateria().getMtrDescripcion();
		// }
		// if (h.getDiaSemana().getDsmId() == 5) {
		// columnaReporte[5] = h.getMateria().getMtrDescripcion();
		// }
		// if (h.getDiaSemana().getDsmId() == 6) {
		// columnaReporte[6] = h.getMateria().getMtrDescripcion();
		// }
	}

	// @Override
	// public Collection<TipoHorario> listarTipoHorario() {
	// Collection<TipoHorario> lstTipoHorario = null;
	// try {
	// Query query = em.createQuery("select th from TipoHorario as th");
	// System.out.println("Valores de la lista");
	// lstTipoHorario = query.getResultList();
	// } catch (Exception e) {
	// System.out.println("Error al consultar los tipos horarios" + e);
	// }
	// return lstTipoHorario;
	// }
}
